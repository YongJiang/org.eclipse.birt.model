/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.command;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryEvent;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.ElementStructureUtil;
import org.eclipse.birt.report.model.util.LevelContentIterator;

/**
 * Represents the command for adding and dropping library from report design.
 */

public class LibraryCommand extends AbstractElementCommand
{

	/**
	 * The action to reload the library.
	 */

	static final int RELOAD_ACTION = 1;

	/**
	 * The simple action like add/remove.
	 */

	static final int SIMPLE_ACTION = 2;

	/**
	 * Construct the command with the report design.
	 * 
	 * @param module
	 *            the report design
	 */

	public LibraryCommand( Module module )
	{
		super( module, module );
	}

	/**
	 * Adds new library file to report design.
	 * 
	 * @param libraryFileName
	 *            library file name
	 * @param namespace
	 *            library namespace
	 * @throws DesignFileException
	 *             if the library file is not found or has fatal errors.
	 * @throws SemanticException
	 *             if failed to add <code>IncludeLibrary</code> strcutre
	 */

	public void addLibrary( String libraryFileName, String namespace )
			throws DesignFileException, SemanticException
	{
		if ( StringUtil.isBlank( namespace ) )
			namespace = StringUtil.extractFileName( libraryFileName );

		if ( module.isDuplicateNamespace( namespace ) )
		{
			throw new LibraryException(
					module,
					new String[]{namespace},
					LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE );
		}

		// the library has already been included.

		URL url = module.findResource( libraryFileName,
				IResourceLocator.LIBRARY );
		if ( url != null
				&& module.getLibraryByLocation( url.toString( ) ) != null )
		{
			throw new LibraryException( module, new String[]{url.toString( )},
					LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED );
		}

		// can not include itself.

		if ( url != null && url.toString( ).equals( module.getLocation( ) ) )
			throw new LibraryException(
					module,
					new String[]{namespace},
					LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY );

		doAddLibrary( libraryFileName, namespace, SIMPLE_ACTION, null );
	}

	/**
	 * Drop the given libary from the design. And break all the parent/child
	 * relationships. All child element will be localized in the module.
	 * 
	 * @param library
	 *            a given library to be dropped.
	 * @throws SemanticException
	 * 
	 */

	public void dropLibraryAndBreakExtends( Library library )
			throws SemanticException
	{
		// library not found.

		if ( !module.getLibraries( ).contains( library ) )
		{
			throw new LibraryException( library,
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );
		}

		ActivityStack stack = getActivityStack( );
		stack.startTrans( );
		try
		{
			for ( int slotID = 0; slotID < library.getDefn( ).getSlotCount( ); slotID++ )
			{
				if ( slotID == Library.THEMES_SLOT )
					continue;

				for ( Iterator iter = library.getSlot( slotID ).iterator( ); iter
						.hasNext( ); )
				{
					DesignElement element = (DesignElement) iter.next( );
					List derived = element.getDerived( );
					for ( int i = 0; i < derived.size( ); i++ )
					{
						DesignElement child = (DesignElement) derived.get( i );
						if ( child.getRoot( ) == getModule( ) )
						{
							ExtendsCommand command = new ExtendsCommand(
									getModule( ), child );
							command.localizeElement( );
						}
					}
				}
			}

			LibraryRecord record = new LibraryRecord( module, library, false );
			getActivityStack( ).execute( record );

			// Remove the include library structure.

			String libraryFileName = library.getFileName( );
			assert libraryFileName != null;
			removeIncludeLibrary( libraryFileName, library.getNamespace( ) );

		}
		catch ( SemanticException ex )
		{
			stack.rollback( );
			throw ex;
		}
		getActivityStack( ).commit( );
	}

	/**
	 * Drops the given library.
	 * 
	 * @param library
	 *            the library to drop
	 * @param inForce
	 *            <code>true</code> if drop the library even the module has
	 *            element reference to this library. <code>false</code> if do
	 *            not drop library when the module has element reference to this
	 *            library.
	 * @throws SemanticException
	 *             if failed to remove <code>IncludeLibrary</code> strcutre
	 */

	public void dropLibrary( Library library ) throws SemanticException
	{
		// library not found.

		if ( !module.getLibraries( ).contains( library ) )
		{
			throw new LibraryException( library,
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );
		}

		// library has decendents in the current module. And check the inForce
		// flag.

		dealAllElementDecendents( library, SIMPLE_ACTION );

		doDropLibrary( library );
	}

	/**
	 * Performs the action to drop the library from the module.
	 * 
	 * @param library
	 *            the library to drop
	 * @param action
	 *            can be RELOAD or SIMPLE.
	 * @throws SemanticException
	 *             if error occurs during insert an included library
	 */

	private void doDropLibrary( Library library ) throws SemanticException
	{
		// Remove the include library structure.

		ActivityStack stack = getActivityStack( );
		stack.startTrans( );

		// Drop the library and update the client references.

		LibraryRecord record = new LibraryRecord( module, library, false );

		getActivityStack( ).execute( record );

		try
		{
			String libFileName = library.getFileName( );
			assert libFileName != null;
			removeIncludeLibrary( libFileName, library.getNamespace( ) );
		}
		catch ( SemanticException ex )
		{
			stack.rollback( );
			throw ex;
		}
		getActivityStack( ).commit( );

	}

	/**
	 * Checks possible extends references for the given element. If extends
	 * reference is unresolve, virtual elements of extends children are removed.
	 * And local property values of virtual elements are returned.
	 * 
	 * @param parent
	 *            the design element
	 * @return the map containing local values of virtual elements. Each key is
	 *         the id of extends child. Each value is another map of which the
	 *         key is the base id of virtual element and the value is property
	 *         name/value pair.
	 * @throws SemanticException
	 *             if error occurs during removing virtual elements.
	 */

	private Map dealElementDecendents( Library library, DesignElement parent,
			int actionCode ) throws SemanticException
	{
		List allDescendents = new ArrayList( );
		getAllDescdents( parent, allDescendents );

		Map overriddenValues = new HashMap( );

		for ( int i = 0; i < allDescendents.size( ); i++ )
		{
			DesignElement child = (DesignElement) allDescendents.get( i );
			do
			{
				if ( child.getRoot( ) != module )
					continue;

				if ( actionCode == RELOAD_ACTION )
				{
					Map values = unresolveElementDescendent( module, child );
					values.put( new Long( child.getID( ) ), values );
					overriddenValues.putAll( values );
				}

				if ( actionCode == SIMPLE_ACTION )
					throw new LibraryException(
							library,
							new String[]{child.getHandle( module )
									.getDisplayLabel( )},
							LibraryException.DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS );

			} while ( child.hasDerived( ) );
		}

		return overriddenValues;
	}

	/**
	 * Reloads the library with the given file path. After reloading, acticity
	 * stack is cleared.
	 * 
	 * @param location
	 *            the URL file path of the library file.
	 * @throws DesignFileException
	 *             if the file does no exist.
	 * @throws SemanticException
	 *             if the library is not included in the current module.
	 */

	public void reloadLibrary( String location ) throws DesignFileException,
			SemanticException
	{
		Library library = module.getLibraryByLocation( location );

		// library not found.

		if ( !module.getLibraries( ).contains( library ) )
		{
			throw new LibraryException( library,
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );
		}

		String namespace = library.getNamespace( );
		IncludedLibrary includedItem = module.findIncludedLibrary( namespace );

		String path = includedItem.getFileName( );
		URL url = module.findResource( path, IResourceLocator.LIBRARY );
		if ( url == null )
		{
			DesignParserException ex = new DesignParserException(
					new String[]{path},
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND );
			List exceptionList = new ArrayList( );
			exceptionList.add( ex );
			throw new DesignFileException( path, exceptionList );
		}

		Map overriddenValues = null;

		ActivityStack activityStack = getActivityStack( );

		// TODO only send the library reload event in the end.

		activityStack.startFilterEventTrans( null );

		try
		{
			// must use content command to remove all virtual elements if
			// required. This can solve unresolving issues like DataSet, Style
			// references, as well as removing names from name space.

			overriddenValues = dealAllElementDecendents( library, RELOAD_ACTION );
			doDropLibrary( library );
			doAddLibrary( path, namespace, RELOAD_ACTION, overriddenValues );
		}
		catch ( SemanticException e )
		{
			activityStack.rollback( );
			throw e;
		}
		catch ( DesignFileException e )
		{
			activityStack.rollback( );
			throw e;
		}

		activityStack.commit( );

		// clears the activity stack to avoid potential problems.

		activityStack.flush( );

		LibraryEvent event = new LibraryEvent( module
				.getLibraryByLocation( location ), LibraryEvent.RELOAD );

		module.broadcast( event );
	}

	/**
	 * Checks possible extends references to element in the given Library. If
	 * extends reference is unresolve, virtual elements are removed. And local
	 * property values of virtual elements are returned.
	 * 
	 * @param library
	 *            the library instance
	 * @return the map containing local values of virtual elements. Each key is
	 *         the id of extends child. Each value is another map of which the
	 *         key is the base id of virtual element and the value is property
	 *         name/value pair.
	 * @throws LibraryException
	 *             if there is any extends reference.
	 */

	private Map dealAllElementDecendents( Library library, int actionCode )
			throws SemanticException
	{
		// library has decendents in the current module. And check the inForce
		// flag.

		Map overriddenValues = new HashMap( );

		LevelContentIterator contentIter = new LevelContentIterator( library, 1 );
		while ( contentIter.hasNext( ) )
		{
			DesignElement tmpElement = (DesignElement) contentIter.next( );
			if ( !tmpElement.getDefn( ).canExtend( ) )
				continue;

			Map values = dealElementDecendents( library, tmpElement, actionCode );
			if ( actionCode == RELOAD_ACTION )
				overriddenValues.putAll( values );
		}

		return overriddenValues;
	}

	/**
	 * Performs the action to add the library to the module.
	 * 
	 * @param libraryFileName
	 *            the library path
	 * @param namespace
	 *            the library namespace
	 * @param action
	 *            can be RELOAD or SIMPLE.
	 * @param overriddenValues
	 *            the overridden values.
	 * @throws SemanticException
	 *             if the library file is invalid.
	 */

	private void doAddLibrary( String libraryFileName, String namespace,
			int action, Map overriddenValues ) throws SemanticException,
			DesignFileException
	{
		Library library = module.loadLibrary( libraryFileName, namespace );

		library.setReadOnly( );

		ActivityStack activityStack = getActivityStack( );

		activityStack.startTrans( );

		LibraryRecord record = null;

		if ( action == SIMPLE_ACTION )
			record = new LibraryRecord( module, library, true );
		if ( action == RELOAD_ACTION )
			record = new LibraryRecord( module, library, overriddenValues );

		getActivityStack( ).execute( record );

		// Add includedLibraries

		IncludedLibrary includeLibrary = StructureFactory
				.createIncludeLibrary( );
		includeLibrary.setFileName( libraryFileName );
		includeLibrary.setNamespace( namespace );

		ElementPropertyDefn propDefn = module
				.getPropertyDefn( Module.LIBRARIES_PROP );
		PropertyCommand propCommand = new PropertyCommand( module, module );
		propCommand.addItem( new CachedMemberRef( propDefn ), includeLibrary );

		activityStack.commit( );
	}

	/**
	 * Unresolves extends reference for the given element. Besides the change on
	 * extends reference, all virtual elements in the given element are removed.
	 * Their element references are also cleared. Moreover, their names are
	 * removed from name spaces.
	 * 
	 * @param module
	 *            the root of the element
	 * @param child
	 *            the design element
	 * @return the map containing local values of virtual elements. The key is
	 *         the base id of virtual element and the value is property
	 *         name/value pair.
	 * @throws SemanticException
	 *             if error occurs during removing virtual elements.
	 */

	private Map unresolveElementDescendent( Module module, DesignElement child )
			throws SemanticException
	{
		ElementRefValue value = (ElementRefValue) child.getLocalProperty(
				module, DesignElement.EXTENDS_PROP );

		DesignElement parent = value.getElement( );
		assert parent != null;

		// not layout structure involved.

		if ( child.getDefn( ).getSlotCount( ) == 0 )
			return Collections.EMPTY_MAP;

		Map overriddenValues = ElementStructureUtil
				.collectPropertyValues( child );

		// remove virtual elements in the element

		LevelContentIterator contentIter = new LevelContentIterator( child, 1 );
		while ( contentIter.hasNext( ) )
		{
			DesignElement tmpElement = (DesignElement) contentIter.next( );
			ContentCommand command = new ContentCommand( module, child );
			command.remove( tmpElement, tmpElement.getContainerSlot( ), false,
					true );
		}

		// unresolves the extends child

		parent.dropDerived( child );
		value.unresolved( value.getName( ) );

		return overriddenValues;
	}

	/**
	 * Recursively collect all the descendents of the given element.
	 * 
	 * @param element
	 *            a given element.
	 * @param results
	 *            the result list containing all the childs.
	 */

	private void getAllDescdents( DesignElement element, List results )
	{
		List descends = element.getDerived( );
		results.addAll( descends );

		for ( int i = 0; i < descends.size( ); i++ )
		{
			getAllDescdents( (DesignElement) descends.get( i ), results );
		}
	}

	/**
	 * drop the include library structure.
	 * 
	 * @param fileName
	 *            file name of the library.
	 * 
	 * @param namespace
	 *            namespace of the library.
	 * 
	 * @throws PropertyValueException
	 */

	private void removeIncludeLibrary( String fileName, String namespace )
			throws PropertyValueException
	{
		assert fileName != null;
		assert namespace != null;

		List includeLibraries = module.getIncludedLibraries( );
		Iterator iter = includeLibraries.iterator( );
		while ( iter.hasNext( ) )
		{
			IncludedLibrary includeLibrary = (IncludedLibrary) iter.next( );

			if ( !namespace.equals( includeLibrary.getNamespace( ) ) )
				continue;

			if ( !fileName.endsWith( includeLibrary.getFileName( ) ) )
				continue;

			ElementPropertyDefn propDefn = module
					.getPropertyDefn( Module.LIBRARIES_PROP );
			PropertyCommand propCommand = new PropertyCommand( module, module );
			propCommand.removeItem( new CachedMemberRef( propDefn ),
					includeLibrary );
			break;
		}
	}

}

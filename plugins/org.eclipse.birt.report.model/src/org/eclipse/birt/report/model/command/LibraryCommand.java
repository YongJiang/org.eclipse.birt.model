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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.IncludeLibrary;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Represents the command for adding and dropping library from report design.
 */

public class LibraryCommand extends AbstractElementCommand
{

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

		if ( isDuplicateNamespace( namespace ) )
		{
			throw new LibraryException(
					module,
					new String[]{namespace},
					LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE );
		}

		Library library = module.loadLibrary( libraryFileName, namespace );
		assert library != null;

		getActivityStack( ).startTrans( );

		LibraryRecord record = new LibraryRecord( module, library, true );
		getActivityStack( ).execute( record );

		// Add includedLibraries

		IncludeLibrary includeLibrary = StructureFactory.createIncludeLibrary( );
		includeLibrary.setFileName( libraryFileName );	
		includeLibrary.setNamespace( namespace );
	
		ElementPropertyDefn propDefn = module
				.getPropertyDefn( Module.INCLUDE_LIBRARIES_PROP );
		PropertyCommand propCommand = new PropertyCommand( module, module );
		propCommand.addItem( new CachedMemberRef( propDefn ), includeLibrary );

		getActivityStack( ).commit( );
	}

	/**
	 * Returns whether the namespace to check is duplicate in target module.
	 * This method helps to judge whether the library to check can be included
	 * in target module.
	 * 
	 * @param namespaceToCheck
	 *            the namespace to check
	 * @return true if the namespace to check is duplicate.
	 */

	private boolean isDuplicateNamespace( String namespaceToCheck )
	{
		List libraries = module.getAllLibraries( );
		Iterator iter = libraries.iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = (Library) iter.next( );

			if ( library.getNamespace( ).equals( namespaceToCheck ) )
				return true;
		}

		return false;
	}

	/**
	 * Drops the given library.
	 * 
	 * @param library
	 *            the library to drop
	 * @throws SemanticException
	 *             if failed to remove <code>IncludeLibrary</code> strcutre
	 */

	public void dropLibrary( Library library ) throws SemanticException
	{
		getActivityStack( ).startTrans( );

		LibraryRecord record = new LibraryRecord( module, library, false );
		getActivityStack( ).execute( record );

		String libraryFileName = library.getFileName( );
		assert libraryFileName != null;

		List includeLibraries = module.getIncludeLibraries( );
		Iterator iter = includeLibraries.iterator( );
		while ( iter.hasNext( ) )
		{
			IncludeLibrary includeLibrary = (IncludeLibrary) iter.next( );

			if ( libraryFileName.endsWith( includeLibrary.getFileName( ) ) )
			{
				ElementPropertyDefn propDefn = module
						.getPropertyDefn( Module.INCLUDE_LIBRARIES_PROP );
				PropertyCommand propCommand = new PropertyCommand( module,
						module );
				propCommand.removeItem( new CachedMemberRef( propDefn ),
						includeLibrary );
				break;
			}
		}

		getActivityStack( ).commit( );

	}

}

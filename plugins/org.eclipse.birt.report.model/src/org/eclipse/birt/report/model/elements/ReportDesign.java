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

package org.eclipse.birt.report.model.elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.validators.MasterPageRequiredValidator;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.core.LayoutModule;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetAdapter;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.writer.DesignWriter;
import org.eclipse.birt.report.model.writer.ModuleWriter;

/**
 * This class represents the root element in the report design hierarchy.
 * Contains the list of data sets, data sources, master pages, components, body
 * content, scratch pad and more. Code modules in the report gives
 * specifications for global scripts that apply to the report as a whole.Report
 * design is valid if it is opened without error or with semantic error.
 * Otherwise, it's invalid.
 * 
 */

public class ReportDesign extends LayoutModule
		implements
			IReportDesignModel,
			ICssStyleSheetOperation
{

	private ICssStyleSheetOperation operation = null;

	/**
	 * Default constructor.
	 * 
	 * @deprecated
	 */

	public ReportDesign( )
	{
		super( null );
		initSlots( );
		onCreate( );
	}

	/**
	 * Constructs the report design with the session.
	 * 
	 * @param session
	 *            the session that owns this design
	 */

	public ReportDesign( DesignSessionImpl session )
	{
		super( session );
		initSlots( );
		onCreate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitReportDesign( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public List<SemanticException> validate( Module module )
	{
		List<SemanticException> list = super.validate( module );

		// Must there is more than one master page in setup page

		list.addAll( MasterPageRequiredValidator.getInstance( ).validate( this,
				this ) );

		list.addAll( validateStructureList( module, IMAGES_PROP ) );
		list.addAll( validateStructureList( module, COLOR_PALETTE_PROP ) );

		list.addAll( validateStructureList( module, INCLUDE_SCRIPTS_PROP ) );
		list.addAll( validateStructureList( module, LIBRARIES_PROP ) );
		list.addAll( validateStructureList( module, PROPERTY_BINDINGS_PROP ) );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.REPORT_DESIGN_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @return an API handle for this element
	 */

	public ReportDesignHandle handle( )
	{
		if ( handle == null )
		{
			handle = new ReportDesignHandle( this );
		}
		return (ReportDesignHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty
	 * (java.lang.String)
	 */

	/**
	 * Finds an include script by the file name.
	 * 
	 * @param fileName
	 *            the script file name
	 * @return the defined include script that matches, or <code>null</code> if
	 *         the file name was not found in the include scripts list.
	 */

	public IncludeScript findIncludeScript( String fileName )
	{
		List<Object> list = (ArrayList) getLocalProperty( null,
				INCLUDE_SCRIPTS_PROP );
		if ( list == null )
			return null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			IncludeScript script = (IncludeScript) list.get( i );
			if ( script.getFileName( ) != null
					&& script.getFileName( ).equals( fileName ) )
				return script;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.module#getSlotCount()
	 */

	protected int getSlotCount( )
	{
		return SLOT_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getWriter()
	 */

	public ModuleWriter getWriter( )
	{
		return new DesignWriter( this );
	}

	/**
	 * Gets all TOCs or bookmarks defined in the slot of the module.
	 * 
	 * @param slotId
	 *            slot id in which the items hold TOCs.
	 * @param propName
	 *            property name
	 * @return all TOCs or bookmarks defined in the slot of the module.
	 */

	public List<Object> collectPropValues( int slotId, String propName )
	{
		List<Object> rtnList = new ArrayList<Object>( );
		ContentIterator contents = new ContentIterator( this,
				new ContainerContext( this, slotId ) );

		while ( contents.hasNext( ) )
		{
			DesignElement ele = contents.next( );
			Object obj = ele.getProperty( this, propName );
			if ( obj != null )
				rtnList.add( obj );
		}

		return rtnList;
	}

	/**
	 * Gets the thumbnail image in Base64 encoding.
	 * 
	 * @return the thumbnail image in Base64 encoding
	 */

	public byte[] getThumbnail( )
	{
		String data = getStringProperty( this, THUMBNAIL_PROP );
		if ( data == null )
			return null;

		try
		{
			return data.getBytes( CHARSET );
		}
		catch ( UnsupportedEncodingException e )
		{
			assert false;
		}

		return null;
	}

	/**
	 * Drops the given css from css list.
	 * 
	 * @param css
	 *            the css to drop
	 * @return the position of the css to drop
	 */

	public int dropCss( CssStyleSheet css )
	{
		if ( operation == null )
			return -1;
		return operation.dropCss( css );
	}

	/**
	 * Adds the given css to css list.
	 * 
	 * @param css
	 *            the css to insert
	 */

	public void addCss( CssStyleSheet css )
	{
		if ( operation == null )
			operation = new CssStyleSheetAdapter( );
		operation.addCss( css );
	}

	/**
	 * Insert the given css to the given position
	 * 
	 * @param css
	 * @param index
	 */

	public void insertCss( CssStyleSheet css, int index )
	{
		if ( operation == null )
			operation = new CssStyleSheetAdapter( );
		operation.insertCss( css, index );
	}

	/**
	 * Returns only csses this module includes directly.
	 * 
	 * @return list of csses. each item is <code>CssStyleSheet</code>
	 */

	public List<CssStyleSheet> getCsses( )
	{
		if ( operation == null )
			return Collections.emptyList( );
		return operation.getCsses( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#doClone(org.eclipse.birt.report
	 * .model.elements.strategy.CopyPolicy)
	 */

	public Object doClone( CopyPolicy policy )
			throws CloneNotSupportedException
	{
		Module module = (Module) super.doClone( policy );
		if ( isCached( ) )
		{
			cacheValues( );
		}

		return module;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#cacheValues()
	 */
	public void cacheValues( )
	{
		setIsCached( true );

		super.cacheValues( );

		ContentIterator iter1 = new ContentIterator( this,
				new ContainerContext( this, BODY_SLOT ) );
		while ( iter1.hasNext( ) )
		{
			DesignElement tmpElement = iter1.next( );
			if ( !( tmpElement instanceof ReportItem ) )
				continue;

			( (ReportItem) tmpElement ).cacheValues( );
		}

	}

	/**
	 * Finds a variable element in this report design itself.
	 * 
	 * @param name
	 *            the variable name.
	 * @return variable element.
	 */
	public VariableElement findVariableElement( String name )
	{
		return (VariableElement) nameHelper.getNameSpace(
				VARIABLE_ELEMENT_NAME_SPACE ).getElement( name );
	}
}

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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Included css style sheet structure
 *
 */

public class IncludedCssStyleSheet extends Structure
{
	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String INCLUDED_CSS_STRUCT = "IncludedCssStyleSheet"; //$NON-NLS-1$

	/**
	 * Name of the file name member. This member is required for the structure.
	 */

	public static final String FILE_NAME_MEMBER = "fileName"; //$NON-NLS-1$


	/**
	 * The file name of the included library.
	 */

	protected String fileName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return INCLUDED_CSS_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( FILE_NAME_MEMBER.equals( propName ) )
			return fileName;
		
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( FILE_NAME_MEMBER.equals( propName ) )
			fileName = (String) value;
		else
			assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate( Module module, DesignElement element )
	{
		ArrayList list = new ArrayList( );

		PropertyDefn memberDefn = (PropertyDefn) getDefn( ).getMember(
				FILE_NAME_MEMBER );
		String fileName = (String) getProperty( module, memberDefn );
		if ( StringUtil.isBlank( fileName ) )
		{
			list.add( new PropertyValueException( element, getDefn( )
					.getMember( FILE_NAME_MEMBER ), fileName,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}

		return list;
	}

	/**
	 * Gets the file name of the include css.
	 * 
	 * @return the file name of the include css
	 */

	public String getFileName( )
	{
		return fileName;
	}

	/**
	 * Sets the file name of the include css.
	 * 
	 * @param theFileName
	 *            the new file name to set
	 */

	public void setFileName( String theFileName )
	{
		fileName = theFileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */
	protected StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new IncludedCssStyleSheetHandle( valueHandle, index );
	}
}

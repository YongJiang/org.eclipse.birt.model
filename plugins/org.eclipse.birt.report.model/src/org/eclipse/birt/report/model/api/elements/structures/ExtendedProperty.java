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

import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * This class represents one Extended property.
 *  
 */

public class ExtendedProperty extends Structure
{

	/**
	 * The Extended property structure name.
	 */

	public static final String Extended_PROPERTY_STRUCT = "ExtendedProperty"; //$NON-NLS-1$

	/**
	 * The member name of the name of Extended property.
	 */

	public final static String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * The member name of the value of Extended property.
	 */

	public final static String VALUE_MEMBER = "value"; //$NON-NLS-1$

	/**
	 * The Extended property name.
	 */

	private String name = null;

	/**
	 * The Extended property value.
	 */

	private String value = null;

	/**
	 * Default constructor.
	 */

	public ExtendedProperty( )
	{
	}

	/**
	 * Constructs the extended property with the given name and value.
	 * 
	 * @param name
	 *            the name of a extended property
	 * @param value
	 *            the value of a extended property
	 */

	public ExtendedProperty( String name, String value )
	{
		this.name = name;
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return Extended_PROPERTY_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#getProperty(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public Object getProperty( ReportDesign design, PropertyDefn prop )
	{
		String valueName = prop.getName( );
		if ( valueName.equals( NAME_MEMBER ) )
			return name;
		else if ( valueName.equals( VALUE_MEMBER ) )
			return value;
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#setProperty(org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	public void setProperty( PropertyDefn prop, Object value )
	{
		String valueName = prop.getName( );
		if ( valueName.equals( NAME_MEMBER ) )
			name = (String) value;
		else if ( valueName.equals( VALUE_MEMBER ) )
			this.value = (String) value;
		else
			assert false;
	}

	/**
	 * Returns the Extended property name.
	 * 
	 * @return the Extended property name
	 */

	public String getName( )
	{
		return name;
	}

	/**
	 * Sets the Extended property name.
	 * 
	 * @param name
	 *            the Extended property name to set
	 */

	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * Returns the Extended property value.
	 * 
	 * @return the Extended property value
	 */

	public String getValue( )
	{
		return value;
	}

	/**
	 * Sets the Extended property value.
	 * 
	 * @param value
	 *            the Extended property value to set
	 */

	public void setValue( String value )
	{
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate( ReportDesign design, DesignElement element )
	{
		ArrayList list = new ArrayList( );

		if ( StringUtil.isBlank( name ) )
		{
			list.add( new PropertyValueException( element,
					(PropertyDefn) getDefn( ).getMember( NAME_MEMBER ), name,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */
	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new ExtendedPropertyHandle( valueHandle, index );
	}
}
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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * String property type.
 * <p>
 * All string values are valid. However, if the caller provides a type other
 * than a string, the value is converted to a string using default conversion
 * rules.
 * 
 */

public class StringPropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.string"; //$NON-NLS-1$

	/**
	 * Constructor
	 */

	public StringPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/**
	 * Validates a string property value. The value can be any object. If the
	 * value is an integer, float, BigDecimal or other BIRT-supported property
	 * value, it is converted to a string using the rules for the current
	 * locale. Others are converted using the toString( ) method.
	 * 
	 * @return a <code>String</code> object or null
	 */

	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;

		return value.toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyValidator#validateXml(java.lang.String)
	 */

	public Object validateXml( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return STRING_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return STRING_TYPE_NAME;
	}

	/**
	 * Converts the string property value to a double, this method will always
     * return 0.  
	 */

	public double toDouble( ReportDesign design, Object value )
	{
		// Strings cannot be converted to doubles because the conversion
		// rules are locale-dependent.

		return 0;
	}

    
    /**
     * Converts the string property value to a string.
     * 
     * @return <code>value</code> as a string.
     */

	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		return (String) value;
	}


    /**
	 * Converts the string property value to an integer.
	 * 
	 * @return integer value of the string representation, return <code>0</code>
	 *         if <code>value</code> is null.
	 */
    
	public int toInteger( ReportDesign design, Object value )
	{
		if ( value == null )
			return 0;

		Integer conv = Integer.decode( (String) value );
		if ( conv == null )
			return 0;
		return conv.intValue( );
	}

}

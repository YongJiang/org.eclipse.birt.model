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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Number property type. A number is represented internally by a
 * <code>BigDecimal</code> object, and represents money and similar business
 * values.
 */

public class NumberPropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.number"; //$NON-NLS-1$

	/**
	 * Constructor
	 */

	public NumberPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/**
	 * Validates that the number represents an BIRT number type (represented as
	 * a BigDecimal). Value can be one of the following:
	 * <p>
	 * <ul>
	 * <li>null, meaning to clear up the property.</li>
	 * <li>A BigDecimal object.</li>
	 * <li>An Integer or Long object.</li>
	 * <li>A Double or Float object. The object must be within the range of a
	 * BigDecimal.</li>
	 * <li>A string that parses to a number. The decimal separator is locale
	 * specific.</li>
	 * </ul>
	 * <p>
	 * A number is represented internally by a <code>BigDecimal</code> object
	 * 
	 * @return object of type <code>BigDecimal</code> or null.
	 */

	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;
		if ( value instanceof BigDecimal )
			return value;
		if ( value instanceof Double )
			return new BigDecimal( ( (Double) value ).doubleValue( ) );
		if ( value instanceof Integer )
			return new BigDecimal( ( (Integer) value ).intValue( ) );
		if ( value instanceof String )
		{
			return validateInputString( design, defn, (String) value );
		}
		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NUMBER_TYPE );
	}

	/**
	 * Validates the xml representation of this number property value. The xml
	 * value will be translated into a BigDecimal object.
	 * 
	 * @return a <code>BigDecimal</code> object that is translated from the
	 *         string value. Return null if value is null or blank string.
	 * @throws PropertyValueException
	 *             if the xml value can not be properly translated into a
	 *             BigDecimal object.
	 * 
	 * @see BigDecimal#BigDecimal(java.lang.String)
	 */

	public Object validateXml( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		value = StringUtil.trimString( value );
		if ( value == null )
			return null;
		try
		{
			return new BigDecimal( value );
		}
		catch ( NumberFormatException e )
		{
			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NUMBER_TYPE );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return NUMBER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return NUMBER_TYPE_NAME;
	}

	/**
	 * Returns the double value of the input number property value. The number
	 * property value is represented by a <code>BigDecimal</code> object.
	 * 
	 * @return double value of the input number property value. Return 0.0 if
	 *         value is null;
	 *  
	 */

	public double toDouble( ReportDesign design, Object value )
	{
		if ( value == null )
			return 0.0;

		return ( (BigDecimal) value ).doubleValue( );
	}

	/**
	 * Returns the locale-independent string representation of the input number
	 * property value. The number property value is represented by a
	 * <code>BigDecimal</code> object. The value will be formatted in a
	 * locale-independent way.
	 * 
	 * @return locale-independent string representation of the number property
	 *         value. Return null if value is null.
	 */

	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		if ( value == null )
			return null;

		NumberFormat formatter = NumberFormat.getNumberInstance( );
		return formatter.format( ( (BigDecimal) value ).doubleValue( ) );
	}

	/**
	 * Returns the integer value of the input number property value. The number
	 * property value is represented by a <code>BigDecimal</code> object.
	 * 
	 * @return integer value of the input number property value. Return 0 if
	 *         input value is null.
	 *  
	 */

	public int toInteger( ReportDesign design, Object value )
	{
		if ( value == null )
			return 0;

		return ( (BigDecimal) value ).intValue( );
	}

	/**
	 * Converts the input number property value into a <code>BigDecimal</code>.
	 * 
	 * @return return the number property value as a <code>BigDecimal</code>.
	 */

	public BigDecimal toNumber( ReportDesign design, Object value )
	{
		return (BigDecimal) value;
	}

	/**
	 * Returns the localized string representation of the input number property
	 * value. The number property value is represented by a
	 * <code>BigDecimal</code> object. The value will be formatted in a
	 * locale-dependent way.
	 * 
	 * @return locale-dependent string representation of the number property
	 *         value. Return null if value is null.
	 */

	public String toDisplayString( ReportDesign design, PropertyDefn defn,
			Object value )
	{
		if ( value == null )
			return null;

		NumberFormat formatter = NumberFormat
				.getNumberInstance( ThreadResources.getLocale( ) );
		return formatter.format( ( (BigDecimal) value ).doubleValue( ) );
	}

	/**
	 * Validates the number property value in a locale-specific way. The string
	 * value will be parsed into a BigDecimal object.
	 * 
	 * @return a <code>BigDecimal</code> object that is translated from the
	 *         string value. Return null if value is null or blank string.
	 * @throws PropertyValueException
	 *             if the value can not be properly parsed in the current
	 *             locale.
	 */

	public Object validateInputString( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{

		if( StringUtil.isBlank( value ) )
			return null;
		
		NumberFormat formatter = NumberFormat
				.getNumberInstance( ThreadResources.getLocale( ) );
		Number number = null;
		try
		{
			// Parse in locale-dependent way.
			number = formatter.parse( value );

			// TODO: current NumberFormat( even DecimalFormmater ) do not
			// provide means to parse a
			// input string in arbitrary-precision way. It does not express the
			// accurate value.

		}
		catch ( ParseException e )
		{
			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NUMBER_TYPE );
		}

		return new BigDecimal( number.doubleValue( ) );
	}
}
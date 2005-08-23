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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;

/**
 * Element name property type. Represents the name of an element.
 * 
 */

public class NamePropertyType extends TextualPropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.name"; //$NON-NLS-1$

	/**
	 * Compiled regular expression for allowed values.
	 * <ul>
	 * <li>abc_.abc allowed
	 * <li>abc abc allowed
	 * <li>_abc allowed
	 * <li>9abc allowed
	 * <li>.abc not allowed
	 * </ul>
	 */

	private static final Pattern pattern = Pattern
			.compile( "[\\w][\\w. ]*" ); //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public NamePropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return NAME_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return NAME_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	public Object validateValue( Module module, PropertyDefn defn, Object value )
			throws PropertyValueException
	{
		assert defn != null;
		if ( value == null )
		{
			if ( defn.isStructureMember( ) )
				throw new PropertyValueException( value,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
						NAME_TYPE );
			return null;
		}
		if ( value instanceof String )
		{
			String stringValue = StringUtil.trimString( (String) value ) ;
			if ( stringValue == null )
			{
				if ( defn.isStructureMember( ) )
					throw new PropertyValueException(
							value,
							PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
							NAME_TYPE );

				return null;
			}

			Matcher matcher = pattern.matcher( stringValue );
			if ( !matcher.matches( ) )
				throw new PropertyValueException( stringValue,
						PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
						NAME_TYPE );

			return stringValue;
		}
		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				NAME_TYPE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateXml(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.String)
	 */

	public Object validateXml( Module module, PropertyDefn defn, String value )
			throws PropertyValueException
	{
		if ( value == null )
			return null;
		return StringUtil.trimString( value );
	}
}
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

package org.eclipse.birt.report.model.parser;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.xml.sax.SAXException;

/**
 * TODO:javadoc
 */

public class Base64PropertyState extends PropertyState
{

	/**
	 * The base 64 codec.
	 */

	private static Base64 base = new Base64( );

	/**
	 * Charset of the string.
	 */

	private final String charSet;

	/**
	 * Constructor.
	 * 
	 * @param theHandler
	 * @param element
	 * @param charSet
	 */

	Base64PropertyState( ModuleParserHandler theHandler,
			DesignElement element, String charSet )
	{
		super( theHandler, element );
		assert charSet != null;
		this.charSet = charSet;
	}

	/**
	 * Constructor.
	 * 
	 * @param theHandler
	 * @param element
	 * @param propDefn
	 * @param struct
	 * @param charSet
	 */

	Base64PropertyState( ModuleParserHandler theHandler,
			DesignElement element, PropertyDefn propDefn, IStructure struct,
			String charSet )
	{
		super( theHandler, element, propDefn, struct );
		assert charSet != null;
		this.charSet = charSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );
		value = getDecodedBase64Value( value );

		super.doEnd( value );
	}

	/**
	 * Sets the image data value as base 64 string.
	 * 
	 * @param value
	 *            the value to set
	 */

	private String getDecodedBase64Value( String value )
	{
		String encodedValue = StringUtil.trimString( value );
		if ( encodedValue == null )
			return null;

		// replace whitespace with the empty string.
		Pattern p = Pattern.compile( "\\s" ); //$NON-NLS-1$
		Matcher m = p.matcher( encodedValue );
		encodedValue = m.replaceAll( "" ); //$NON-NLS-1$

		byte[] data = null;

		try
		{
			data = base.decode( encodedValue.getBytes( charSet ) );
			return new String( data, charSet );
		}
		catch ( UnsupportedEncodingException e )
		{
			assert false;
			return null;
		}

	}
}

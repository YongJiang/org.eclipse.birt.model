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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * Parses the abstract property, that is the XML file like:
 * 
 * <pre>
 * 
 *  
 *     &lt;property-tag name=&quot;propName&quot;&gt;property value&lt;/property-tag&gt;
 *   
 *  
 * </pre>
 * 
 * The supported tags are:
 * <ul>
 * <li>property,
 * <li>expression,
 * <li>xml,
 * <li>method,
 * <li>structure,
 * <li>list-property,
 * <li>text-property,
 * <li>html-property
 * </ul>
 * 
 * This class parses the "name" attribute and keeps it. Other attributes are
 * parsed by the inherited classes.
 *  
 */

public class AbstractPropertyState extends AbstractParseState
{

	/**
	 * The design file parser handler
	 */

	protected DesignParserHandler handler = null;

	/**
	 * The element holding this property
	 */

	protected DesignElement element = null;

	/**
	 * The element property name or structure member name
	 */

	protected String name = null;

	/**
	 * The structure which holds this property as a member
	 */

	protected IStructure struct = null;

	/**
	 * Whether the property of this state is defined.
	 */

	protected boolean valid = true;

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * This constructor is used when this property to parse is a property of one
	 * element.
	 * 
	 * @param theHandler
	 *            the design file parser handler
	 * @param element
	 *            the element which holds this property
	 */

	public AbstractPropertyState( DesignParserHandler theHandler,
			DesignElement element )
	{
		handler = theHandler;
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		name = attrs.getValue( DesignSchemaConstants.NAME_ATTRIB );
		if ( StringUtil.isBlank( name ) )
		{
			handler.semanticError( new DesignParserException(
					DesignParserException.NAME_REQUIRED ) );
			valid = false;
			return;
		}
		super.parseAttrs( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler( )
	{
		return handler;
	}

	/**
	 * Sets the member of a structure.
	 * 
	 * @param struct
	 *            the structure that contains the member to set
	 * @param propName
	 *            the property in which the structure appears
	 * @param member
	 *            the structure member name
	 * @param value
	 *            the value parsed from the XML file
	 */

	void setMember( IStructure struct, String propName, String member,
			String value )
	{
		if ( StringUtil.isBlank( value ) )
			return;

		// Ensure that the member is defined.

		StructureDefn structDefn = struct.getDefn( );
		assert structDefn != null;

		StructPropertyDefn memberDefn = structDefn.getMember( member );
		if ( memberDefn == null )
		{
			handler.semanticWarning( new DesignParserException(
					DesignParserException.UNDEFINED_PROPERTY ) );
			valid = false;
			return;
		}

		// Validate the value.

		try
		{
			Object propValue = memberDefn.validateXml( handler.getDesign( ),
					value.trim( ) );
			struct.setProperty( memberDefn, propValue );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( propName + "." + member ); //$NON-NLS-1$
			handler.semanticError( ex );
			valid = false;
		}
	}

	/**
	 * Sets the value of a property with a string parsed from the XML file.
	 * Performs any required semantic checks.
	 * 
	 * @param propName
	 *            property name
	 * @param value
	 *            value string from the XML file
	 */

	protected void setProperty( String propName, String value )
	{
		assert propName != null;

		if ( StringUtil.isBlank( value ) )
			return;

		if ( propName.equalsIgnoreCase( DesignElement.NAME_PROP )
				|| propName.equalsIgnoreCase( DesignElement.EXTENDS_PROP ) )
		{
			handler.semanticError( new DesignParserException(
					DesignParserException.INVALID_PROPERTY_SYNTAX ) );
			valid = false;
			return;
		}

		// The property definition is not found, including user
		// properties.

		ElementPropertyDefn prop = element.getPropertyDefn( propName );
		if ( prop == null )
		{
			handler.semanticWarning( new DesignParserException(
					DesignParserException.UNDEFINED_PROPERTY ) );
			valid = false;
			return;
		}

		// Validate the value.

		try
		{
			Object propValue = prop.validateXml( handler.getDesign( ), value
					.trim( ) );
			element.setProperty( propName, propValue );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( propName );
			handler.semanticError( ex );
			valid = false;
		}
	}

	/**
	 * Sets the value of the attribute "name". This method is used when the
	 * specific state is defined. When the generic state jumps to the specific
	 * one, the <code>parseAttrs</code> will not be called. So the value of
	 * the attribute "name" should be set by the generic state.
	 * 
	 * @param name
	 *            the name to set
	 */

	protected void setName( String name )
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#jumpTo()
	 */
	public AbstractParseState jumpTo( )
	{
		// If this state can not be parsed properly, any states in it are
		// ignored.

		if ( !valid )
			return new AnyElementState( getHandler( ) );

		return super.jumpTo( );
	}
}
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

import java.util.ArrayList;

import org.eclipse.birt.report.model.command.UserPropertyException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.UserChoice;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses one user-defined property and adds the property definition into the
 * element, if the element allows user-defined properties and the definition is
 * valid.
 */

public class UserPropertyStructureState extends StructureState
{

	/**
	 * Constructs the state of the structure which is user property.
	 * 
	 * @param theHandler
	 *            the design parser handler
	 * @param element
	 *            the element holding this user property
	 * @param theList
	 *            the list of user properties
	 */

	UserPropertyStructureState( DesignParserHandler theHandler,
			DesignElement element, ArrayList theList )
	{
		super( theHandler, element );

		this.propDefn = new UserPropertyDefn( );
		this.struct = (IStructure) propDefn;
		this.list = theList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
			return new UserPropertyState( handler, element, propDefn, struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_PROPERTY_TAG ) )
			return new ChoiceStructureListState( handler, element, propDefn,
					struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_PROPERTY_TAG ) )
			return new TextPropertyState( handler, element, struct );

		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		try
		{
			( (UserPropertyDefn) struct ).checkUserPropertyDefn( element );
			element.addUserPropertyDefn( (UserPropertyDefn) struct );
		}
		catch ( UserPropertyException e )
		{
			handler.semanticError( e );
		}
		catch ( MetaDataException e )
		{
			handler.semanticError( new UserPropertyException( element,
					( (UserPropertyDefn) struct ).getName( ),
					UserPropertyException.DESIGN_EXCEPTION_INVALID_DEFINITION,
					e ) );
		}
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the
	 * ReportElement tag.
	 */

	class InnerParseState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}
	}

	/**
	 * Parses the value choices of use-defined properties, which will read the
	 * choice and store it as <code>UserChoice</code>.
	 */

	class ChoiceStructureListState extends PropertyListState
	{

		ChoiceStructureListState( DesignParserHandler theHandler,
				DesignElement element, PropertyDefn propDefn, IStructure struct )
		{
			super( theHandler, element, propDefn, struct );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs.getValue( DesignSchemaConstants.NAME_ATTRIB );
			if ( StringUtil.isBlank( name ) )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				valid = false;
				return;
			}

			if ( !UserPropertyDefn.CHOICES_MEMBER.equalsIgnoreCase( name ) )
			{
				handler
						.semanticWarning( new DesignParserException(
								DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY ) );
				valid = false;
				return;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG ) )
				return new ChoiceStructureState( list );

			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			UserChoice[] choiceArray = new UserChoice[list.size( )];
			list.toArray( choiceArray );

			( (UserPropertyDefn) struct ).setChoices( choiceArray );
		}
	}

	/**
	 * Parses one user-defined choice of the user-defined property.
	 */

	class ChoiceStructureState extends InnerParseState
	{

		ArrayList choices = null;
		UserChoice choice = new UserChoice( null, null );

		ChoiceStructureState( ArrayList theChoices )
		{
			this.choices = theChoices;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
				return new ChoicePropertyState( choice );

			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.TEXT_PROPERTY_TAG ) )
				return new ChoiceTextPropertyState( choice );

			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end( ) throws SAXException
		{
			choices.add( choice );
		}

	}

	/**
	 * Parses the member of the user-defined choice( <code>UserChoice</code>),
	 * that is name, display name key, display name.
	 */

	class ChoicePropertyState extends InnerParseState
	{

		UserChoice choice = null;
		String choiceName = null;

		ChoicePropertyState( UserChoice theChoice )
		{
			this.choice = theChoice;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			choiceName = attrs.getValue( DesignSchemaConstants.NAME_ATTRIB );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end( ) throws SAXException
		{
			String value = text.toString( );

			if ( UserChoice.NAME_PROP.equalsIgnoreCase( choiceName ) )
				choice.setName( value );
			else if ( UserChoice.VALUE_PROP.equalsIgnoreCase( choiceName ) )
			{
				UserPropertyDefn propDefn = (UserPropertyDefn) struct;
				Object objValue = value;

				if ( propDefn.getTypeCode( ) != PropertyType.CHOICE_TYPE )
				{
					try
					{
						objValue = propDefn.validateValue(
								handler.getDesign( ), value );
					}
					catch ( PropertyValueException e )
					{
						handler
								.semanticError( new UserPropertyException(
										element,
										name,
										UserPropertyException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE ) );
						return;
					}
				}
				choice.setValue( objValue );
			}
			else
				assert false;
		}
	}

	/**
	 * Parses the member of the user-defined choice( <code>UserChoice</code>),
	 * that is name, display name key, display name.
	 */

	class ChoiceTextPropertyState extends InnerParseState
	{

		UserChoice choice = null;
		String displayNamePropName = null;
		String resourceKeyValue = null;

		ChoiceTextPropertyState( UserChoice theChoice )
		{
			this.choice = theChoice;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			displayNamePropName = attrs
					.getValue( DesignSchemaConstants.NAME_ATTRIB );
			resourceKeyValue = attrs
					.getValue( DesignSchemaConstants.KEY_ATTRIB );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end( ) throws SAXException
		{
			String value = text.toString( );

			if ( UserChoice.DISPLAY_NAME_PROP
					.equalsIgnoreCase( displayNamePropName ) )
			{
				choice.setDisplayName( value );
				choice.setDisplayNameKey( resourceKeyValue );
			}
		}

	}

	/**
	 * Parses the one property of user-define property definition.
	 */

	class UserPropertyState extends PropertyState
	{

		UserPropertyState( DesignParserHandler theHandler,
				DesignElement element, PropertyDefn propDefn, IStructure struct )
		{
			super( theHandler, element, propDefn, struct );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			String value = text.toString( );
			if ( StringUtil.isBlank( value ) )
				return;

			// if the property tag is to set the type of a user-defined
			// property, then we implement it like this.

			if ( UserPropertyDefn.TYPE_MEMBER.equalsIgnoreCase( name ) )
			{
				MetaDataDictionary dictionary = MetaDataDictionary
						.getInstance( );
				if ( StringUtil.isBlank( value ) )
				{
					value = PropertyType.STRING_TYPE_NAME;
				}

				PropertyType typeDefn = dictionary.getPropertyType( value );
				if ( typeDefn == null )
				{
					handler
							.semanticError( new UserPropertyException(
									element,
									( (UserPropertyDefn) struct ).getName( ),
									UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE ) );
					return;
				}

				( (UserPropertyDefn) struct ).setType( typeDefn );
			}
			else
			{
				super.end( );
			}
		}
	}
}
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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.xml.sax.SAXException;

/**
 * Parses the simple structure list, each of which has only one member. So it
 * also can be considered as String List.
 */

public class SimpleStructureListState extends PropertyListState
{

	private String memberName = null;

	SimpleStructureListState( DesignParserHandler theHandler,
			DesignElement element )
	{
		super( theHandler, element );
	}

	/**
	 * Sets the member name which is the unique member in structure.
	 * 
	 * @param memberName
	 *            the member name to set
	 */

	public void setMemberName( String memberName )
	{
		this.memberName = memberName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	public AbstractParseState startElement( String tagName )
	{
		// The unique member name should be specified.

		assert memberName != null;

		if ( DesignSchemaConstants.PROPERTY_TAG.equalsIgnoreCase( tagName ) )
		{
			AbstractPropertyState state = new SimpleStructureState( handler,
					element, propDefn, list );
			return state;
		}

		return super.startElement( tagName );
	}

	class SimpleStructureState extends StructureState
	{

		SimpleStructureState( DesignParserHandler theHandler,
				DesignElement element, PropertyDefn propDefn, ArrayList theList )
		{
			super( theHandler, element, propDefn, theList );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			struct = createStructure( propDefn.getStructDefn( ) );
			assert struct != null;

			String value = text.toString( );
			if ( StringUtil.isBlank( value ) )
				return;

			setMember( struct, propDefn.getName( ), memberName, value );

			super.end( );
		}
	}
}
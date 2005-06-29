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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PrivateStyleHandle;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * This class parses the Table (table item) tag.
 * 
 */

public class TableItemState extends ListingItemState
{

	/**
	 * Constructs the table item state with the design parser handler, the
	 * container element and the container slot of the table item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public TableItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new TableItem( );
		initElement( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.COLUMN_TAG ) )
			return new TableColumnState( handler, element,
					TableItem.COLUMN_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HEADER_TAG ) )
			return new TableBandState( element, TableItem.HEADER_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GROUP_TAG ) )
			return new TableGroupState( handler, element, TableItem.GROUP_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DETAIL_TAG ) )
			return new TableBandState( element, TableItem.DETAIL_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FOOTER_TAG ) )
			return new TableBandState( element, TableItem.FOOTER_SLOT );
		return super.startElement( tagName );
	}

	
	/**
	 * Parses the contents of the list of TableGroup.
	 */

	class TableGroupState extends GroupState
	{

		/**
		 * Constructs the group state with the design parser handler, the
		 * container element and the container slot of the group element.
		 * 
		 * @param handler
		 *            the design file parser handler
		 * @param theContainer
		 *            the element that contains this one
		 * @param slot
		 *            the slot in which this element appears
		 */

		public TableGroupState( DesignParserHandler handler,
				DesignElement theContainer, int slot )
		{
			super( handler, theContainer, slot );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			group = new TableGroup( );
			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HEADER_TAG ) )
				return new TableBandState( group, TableGroup.HEADER_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FOOTER_TAG ) )
				return new TableBandState( group, TableGroup.FOOTER_SLOT );
			return super.startElement( tagName );
		}

	}

	/**
	 * Parses the contents of the list of tablebands.
	 */

	class TableBandState extends AbstractParseState
	{

		protected int rowSlotID;

		protected DesignElement table;

		public TableBandState( DesignElement container, int slot )
		{
			table = container;
			rowSlotID = slot;
		}

		public XMLParserHandler getHandler( )
		{
			return handler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.ROW_TAG ) )
				return new TableRowState( handler, table, rowSlotID );
			return super.startElement( tagName );
		}

	}
}

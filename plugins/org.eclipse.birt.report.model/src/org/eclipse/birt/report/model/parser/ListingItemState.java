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
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.DataColumnNameValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.xml.sax.SAXException;

/**
 * This class parses common properties for both list and table report items.
 * 
 * @see org.eclipse.birt.report.model.elements.ListingElement
 */

public abstract class ListingItemState extends ReportItemState
{

	/**
	 * The listing element (table or list) being built.
	 */

	protected ListingElement element;

	/**
	 * Constructs a state to parse the common properties of the list and table
	 * report items.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ListingItemState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		makeTestExpressionCompatible( );

		Set elements = handler.tempValue.keySet( );
		ContainerSlot groups = element.getSlot( ListingElement.GROUP_SLOT );
		for ( int i = 0; i < groups.getCount( ); i++ )
		{
			GroupElement group = (GroupElement) groups.getContent( i );

			handler.getModule( ).getNameManager( ).makeUniqueName( group );

			String groupName = (String) group.getLocalProperty( handler
					.getModule( ), GroupElement.GROUP_NAME_PROP );

			if ( !elements.contains( group ) )
				continue;

			if ( StringUtil.compareVersion( handler.getVersion( ), "3.2.2" ) >= 0 ) //$NON-NLS-1$
			{
				continue;
			}

			List columns = (List) handler.tempValue.get( group );
			if ( columns == null || columns.isEmpty( ) )
				continue;

			List tmpList = (List) element.getLocalProperty( handler.module,
					ListingElement.BOUND_DATA_COLUMNS_PROP );

			if ( tmpList == null )
			{
				tmpList = new ArrayList( );
				element.setProperty( ListingElement.BOUND_DATA_COLUMNS_PROP,
						tmpList );
			}

			if ( StringUtil.compareVersion( handler.getVersion( ), "3" ) <= 0 ) //$NON-NLS-1$
			{
				addCachedListWithAggregateOnToListing( columns, tmpList, group,
						groupName );
				continue;
			}

			addCachedListToListing( columns, tmpList, group, groupName );

		}

		super.end( );
	}

	/**
	 * Returns the bound column of which expression and aggregateOn values are
	 * equals to the input column.
	 * 
	 * @param columns
	 *            the bound column list
	 * @param column
	 *            the input bound column
	 * @return the matched bound column
	 */

	private ComputedColumn checkMatchedBoundColumnForGroup( List columns,
			String expression, String aggregateOn )
	{
		if ( ( columns == null ) || ( columns.size( ) == 0 )
				|| expression == null )
			return null;

		for ( int i = 0; i < columns.size( ); i++ )
		{
			ComputedColumn column = (ComputedColumn) columns.get( i );
			if ( expression.equals( column.getExpression( ) ) )
			{
				if ( aggregateOn == null && column.getAggregrateOn( ) == null )
					return column;

				if ( aggregateOn != null
						&& aggregateOn.equals( column.getAggregrateOn( ) ) )
					return column;
			}
		}

		return null;
	}

	/**
	 * Creates a unique bound column name in the column bound list.
	 * 
	 * @param columns
	 *            the bound column list
	 * @param checkColumn
	 *            the column of which name to check
	 * @return the newly created column name
	 */

	private String getUniqueBoundColumnNameForGroup( List columns,
			ComputedColumn checkColumn )
	{
		String oldName = checkColumn.getName( );
		String tmpName = oldName;
		int index = 0;

		while ( true )
		{
			ComputedColumn column = DataColumnNameValidator.getColumn( columns,
					tmpName );
			if ( column == null )
				break;

			tmpName = oldName + "_" + ++index; //$NON-NLS-1$
		}

		return tmpName;
	}

	/**
	 * Reset the result column name for the data item. Since the bound column
	 * name may recreated in this state, the corresponding result set colum must
	 * be resetted.
	 * 
	 * @param group
	 *            the group element
	 * @param columns
	 *            the bound column list
	 */

	private void reCheckResultSetColumnName( GroupElement group, List columns )
	{
		int level = -1;
		if ( group instanceof TableGroup )
			level = 3;
		if ( group instanceof ListGroup )
			level = 1;

		LevelContentIterator contentIter = new LevelContentIterator( group,
				level );
		while ( contentIter.hasNext( ) )
		{
			DesignElement item = (DesignElement) contentIter.next( );
			if ( !( item instanceof DataItem ) )
				continue;

			String resultSetColumn = (String) item.getLocalProperty(
					handler.module, DataItem.RESULT_SET_COLUMN_PROP );

			if ( StringUtil.isBlank( resultSetColumn ) )
				continue;

			ComputedColumn foundColumn = DataColumnNameValidator.getColumn(
					columns, resultSetColumn );

			assert foundColumn != null;

			foundColumn = checkMatchedBoundColumnForGroup( columns, foundColumn
					.getExpression( ), (String) group.getLocalProperty(
					handler.module, GroupElement.GROUP_NAME_PROP ) );

			item.setProperty( DataItem.RESULT_SET_COLUMN_PROP, foundColumn
					.getName( ) );
		}
	}

	/**
	 * Add cached bound columns for the given group to the group's listing
	 * container. This is for old design file that do not have bound column
	 * features.
	 * 
	 * @param columns
	 *            bound columns to add
	 * @param tmpList
	 *            bound column values of the listing container
	 * @param group
	 *            the list/table group
	 * @param groupName
	 *            the group name
	 */

	public void addCachedListWithAggregateOnToListing( List columns,
			List tmpList, GroupElement group, String groupName )
	{
		for ( int j = 0; j < columns.size( ); j++ )
		{
			ComputedColumn column = (ComputedColumn) columns.get( j );

			column.setAggregrateOn( groupName );

			ComputedColumn foundColumn = checkMatchedBoundColumnForGroup(
					tmpList, column.getExpression( ), column.getAggregrateOn( ) );
			if ( foundColumn == null
					|| !foundColumn.getName( ).equals( column.getName( ) ) )
			{
				String newName = getUniqueBoundColumnNameForGroup( tmpList,
						column );
				column.setName( newName );
				tmpList.add( column );
			}
		}

		reCheckResultSetColumnName( group, tmpList );
	}

	/**
	 * Add cached bound columns for the given group to the group's listing
	 * container. This method is for the design file with the bound column
	 * feature and the group defined the bound column properties.
	 * 
	 * @param columns
	 *            bound columns to add
	 * @param tmpList
	 *            bound column values of the listing container
	 * @param group
	 *            the list/table group
	 * @param groupName
	 *            the group name
	 */

	public void addCachedListToListing( List columns, List tmpList,
			GroupElement group, String groupName )
	{
		for ( int j = 0; j < columns.size( ); j++ )
		{
			ComputedColumn column = (ComputedColumn) columns.get( j );

			if ( !tmpList.contains( column ) )
			{
				column.setAggregrateOn( groupName );
				tmpList.add( column );
			}
		}
	}
}
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

package org.eclipse.birt.report.model.elements;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a Grid item. A grid item contains a set of report
 * items, but the items are arranged into a grid. Each cell in the grid can
 * contain a single item. However, the user can easily add multiple items by
 * placing a container into a cell, and placing other items into the container.
 * Grid layout is familiar to anyone who has used HTML tables, Word tables or
 * Excel: data is divided into a series of rows and columns. Items can span both
 * rows and columns. The grid layout is ideal for many simple reports and
 * dashboards. Grids help align report items for a clean layout. The grid
 * section is divided into rows and columns. Rows grow or shrink depending on
 * content. Columns ensure that items line up vertically. Columns can also grow
 * or shrink depending on their contents. The following terminology applies to
 * grids:
 * 
 * <p>
 * <dl>
 * <dt><strong>Grid </strong></dt>
 * <dd>a tabular layout with a fixed set of columns and variable number of
 * rows. (Contrast this with a matrix that can have a variable number of
 * columns.)</dd>
 * 
 * <dt><strong>Column </strong></dt>
 * <dd>a vertical slice though the grid. Columns help organize the layout, but
 * do not represent a specific bit of data as they do in matrices.</dd>
 * 
 * <dt><strong>Row </strong></dt>
 * <dd>a horizontal slice through the grid.</dd>
 * 
 * <dt><strong>Cell </strong></dt>
 * <dd>a point at which a row and column intersect. A cell can span rows and
 * columns.</dd>
 * </dl>
 * <p>
 * 
 * The grid layout is ideal for reports that will be exported to Excel or shown
 * on the web. The grid layout can be applied to a list to align column headings
 * with detail rows. It can be applied to a dashboard to create a clean,
 * organized layout. It can also be applied to the entire report to align data
 * in a group of dashboards and lists. Use the
 * {@link org.eclipse.birt.report.model.api.GridHandle}class to set a number of
 * properties for the grid as a whole.
 * 
 * <p>
 * <dl>
 * <dt><strong>Style </strong></dt>
 * <dd>The style defines the font to use within grid cells, the border style
 * for the grid, fill color, and so on.</dd>
 * 
 * <dt><strong>Fixed or variable size </strong></dt>
 * <dd>A grid will normally adjust based on the available space on the page.
 * When viewed on the web, the grid columns will expand to make use of the full
 * width of the browser window in the expected way.</dd>
 * 
 * <dt><strong>Row alignment </strong></dt>
 * <dd>how to align items within a row. Options are top, middle, bottom or
 * baseline.</dd>
 * </dl>
 *  
 */

public class GridItem extends ReportItem
{

	/**
	 * Identifier of the columns slot.
	 */

	public static final int COLUMN_SLOT = 0;

	/**
	 * Identifier of the row slot.
	 */

	public static final int ROW_SLOT = 1;

	/**
	 * Number of slots in the this item.
	 */

	public static final int SLOT_COUNT = 2;

	/**
	 * The set of slots for the listing.
	 */

	protected ContainerSlot slots[] = null;

	/**
	 * Default Constructor.
	 */

	public GridItem( )
	{
		super( );
		initSlots( );
	}

	/**
	 * Constructs the grid with the name for it.
	 * 
	 * @param theName
	 *            the optional name of the grid
	 */

	public GridItem( String theName )
	{
		super( theName );
		initSlots( );
	}

	/**
	 * Privates method to create the slots.
	 */

	private void initSlots( )
	{
		slots = new ContainerSlot[SLOT_COUNT];
		for ( int i = 0; i < slots.length; i++ )
			slots[i] = new MultiElementSlot( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitGrid( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.GRID_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design for the grid
	 * 
	 * @return an API handle for this element.
	 */

	public GridHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new GridHandle( design, this );
		}
		return (GridHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert ( slot >= 0 && slot < SLOT_COUNT );
		return slots[slot];
	}

	/**
	 * Computes the number of columns in the Grid. The number is defined as the
	 * sum of columns describe in the Columns slot.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of columns in the Grid
	 */

	public int getColumnCount( ReportDesign design )
	{
		// Method 1: sum columns in the column slot.

		int colCount = getColDefnCount( design );
		if ( colCount != 0 )
			return colCount;

		// Method 2: find the widest row.

		return findMaxCols( design );
	}

	/**
	 * Gets the number of columns described in the column definition section.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of columns described by column definitions
	 */

	private int getColDefnCount( ReportDesign design )
	{
		int colCount = 0;
		ContainerSlot cols = getSlot( COLUMN_SLOT );
		int colDefnCount = cols.getCount( );
		for ( int i = 0; i < colDefnCount; i++ )
		{
			TableColumn col = (TableColumn) cols.getContent( i );
			colCount += col.getIntProperty( design, TableColumn.REPEAT_PROP );
		}
		return colCount;
	}

	/**
	 * Finds the maximum column width for this grid.
	 * 
	 * @param design
	 *            the report design
	 * @return the maximum number of columns
	 */

	private int findMaxCols( ReportDesign design )
	{
		ContainerSlot rows = getSlot( ROW_SLOT );
		int maxCols = 0;
		int count = rows.getCount( );
		for ( int i = 0; i < count; i++ )
		{
			TableRow row = (TableRow) rows.getContent( i );
			int cols = row.getColumnCount( design );
			if ( cols > maxCols )
				maxCols = cols;
		}
		return maxCols;
	}

	/**
	 * Returns the style property defined on the column for the cell
	 * <code>target</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param target
	 *            the target cell to search
	 * @param prop
	 *            the property definition.
	 * 
	 * @return the value of a style property
	 */

	protected Object getPropertyFromColumn( ReportDesign design, Cell target,
			ElementPropertyDefn prop )
	{
		assert prop.isStyleProperty( );

		ContainerSlot columnSlot = slots[COLUMN_SLOT];
		if ( columnSlot.getCount( ) == 0 )
			return null;
		
		int columnNum = findCellColumn( design, target );

		assert columnNum > 0;
		TableColumn column = ColumnHelper.findColumn( design,
				slots[COLUMN_SLOT], columnNum );

		if ( column != null )
			return column.getPropertyFromElement(design, prop);

		return null;
	}

	/**
	 * Returns the column number for the cell that has no "column" property
	 * defined.
	 * 
	 * @param design
	 *            the report design
	 * @param target
	 *            the cell to find
	 * 
	 * @return the column number
	 */

	private int findCellColumn( ReportDesign design, Cell target )
	{
		int pos = target.getColumn( design );
		if ( pos > 0 )
			return pos;

		// the first column is 1.

		pos = 1;

		TableRow row = (TableRow) target.getContainer( );
		List list = row.getContentsSlot( );

		for ( Iterator iter = list.iterator( ); iter.hasNext( ); )
		{
			Cell cell = (Cell) iter.next( );
			int cellPos = target.getColumn( design );
			if ( cellPos > 0 )
				pos = cellPos;

			if ( cell == target )
				return pos;

			pos = pos + target.getColSpan( design );

		}

		return pos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// If column definitions are defined, then they must describe the
		// number of columns actually used by the table. It is legal to
		// have a table with zero columns.

		int colDefnCount = getColDefnCount( design );
		int maxCols = findMaxCols( design );
		if ( colDefnCount != maxCols && colDefnCount != 0 )
			list.add( new SemanticError( this,
					SemanticError.INCONSITENT_GRID_COL_COUNT ) );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getDisplayLabel(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      int)
	 */

	public String getDisplayLabel( ReportDesign design, int level )
	{
		String displayLabel = super.getDisplayLabel( design, level );
		if ( level == DesignElement.FULL_LABEL )
		{
			GridHandle handle = handle( design );
			int rows = handle.getRows( ).getCount( );
			int cols = handle.getColumns( ).getCount( );
			displayLabel += "(" + rows + " x " + cols + ")"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}
		return displayLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		GridItem element = (GridItem) super.clone( );
		element.initSlots( );
		for ( int i = 0; i < slots.length; i++ )
		{
			element.slots[i] = slots[i].copy( element, i );
		}
		return element;

	}
}
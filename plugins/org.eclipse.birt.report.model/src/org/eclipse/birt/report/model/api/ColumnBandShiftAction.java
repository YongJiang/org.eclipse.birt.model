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

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.TableColumn;

/**
 * The action to shift one column from one position to another in the same
 * Table/Grid.
 * 
 */

class ColumnBandShiftAction extends ColumnBandAction
{

	/**
	 * Constructs a default <code>ColumnBandShiftAction</code>.
	 */

	public ColumnBandShiftAction( ColumnBandAdapter adapter )
	{
		super( adapter );
	}

	/**
	 * Returns the shift data of the column band.
	 * 
	 * @param sourceIndex
	 *            the source column number to shift
	 * @return a <code>ColumnBandData</code> object containing the data to
	 *         shift
	 * @throws SemanticException
	 *             if the source column is forbidden to shift becuase of column
	 *             spans and dropping properties.
	 */

	protected ColumnBandData getShiftData( int sourceIndex )
			throws SemanticException
	{
		if ( sourceIndex <= 0 )
			throw new IllegalArgumentException( "wrong column to shift" ); //$NON-NLS-1$

		ColumnBandData data = new ColumnBandData( );

		TableColumn column = ColumnHelper.findColumn( adapter.getDesign( ),
				adapter.getColumns( ).getSlot( ), sourceIndex );

		if ( column != null )
		{
			try
			{
				column = (TableColumn) column.clone( );
				column.setProperty( TableColumn.REPEAT_PROP, new Integer( 1 ) );
				data.setColumn( column );
			}
			catch ( CloneNotSupportedException e )
			{
				assert false;
			}
		}

		List cells = getCellsContextInfo( adapter
				.getCellsUnderColumn( sourceIndex ) );

		data.setCells( cells );

		if ( !isRectangleArea( cells, 1 ) )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{Integer.toString( sourceIndex ),
							adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN );

		if ( adapter.hasDroppingCell( cells ) )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{Integer.toString( sourceIndex ),
							adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN );

		return data;
	}

	/**
	 * Returns the actual position for the shift action. This is to keep
	 * consistent with the behavior in <code>ShiftHandle</code> and
	 * <code>PropertyHandle</code>.
	 * 
	 * @param posn
	 *            the source position
	 * @param newPosn
	 *            the new position
	 */

	private int adjustDestPosn( int posn, int newPosn )
	{
		int columnCount = adapter.getColumnCount( );

		if ( newPosn > columnCount )
			return columnCount;

		// If the new position is the same as the old, then skip the operation.

		if ( ( posn == newPosn ) || ( newPosn + 1 == posn ) )
			return -1;

		return newPosn;
	}

	/**
	 * Moves one column band from <code>sourceColumn</code> to
	 * <code>destColumn</code>.
	 * 
	 * @param sourceColumn
	 *            the source column to shift
	 * @param destColumn
	 *            the target column to shift
	 * @throws SemanticException
	 */

	protected void shiftColumnBand( int sourceColumn, int destColumn )
			throws SemanticException
	{

		ColumnBandData data = getShiftData( sourceColumn );

		// If the new position is the same as the old, then skip the operation.

		int newPosn = adjustDestPosn( sourceColumn, destColumn );
		if ( newPosn == -1 )
			return;

		// shift in the same table, the layout must be same.

		if ( !checkTargetColumn( sourceColumn, destColumn ) )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{Integer.toString( sourceColumn ),
							adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN );

		try
		{
			adapter.getDesign( ).getActivityStack( ).startTrans( );
			shiftColumn( data.getColumn( ), sourceColumn, newPosn );
			shiftCells( data.getCells( ), sourceColumn, newPosn );
		}
		catch ( SemanticException e )
		{
			adapter.getDesign( ).getActivityStack( ).rollback( );
			throw e;
		}

		adapter.getDesign( ).getActivityStack( ).commit( );
	}

	/**
	 * Moves one column from <code>sourceColumn</code> to
	 * <code>destColumn</code>.
	 * 
	 * @param sourceIndex
	 *            the source column to shift
	 * @param destIndex
	 *            the target column to shift
	 * @throws SemanticException
	 *             if any error occurs during moving the column
	 */

	private void shiftColumn( TableColumn column, int sourceIndex, int destIndex )
			throws SemanticException
	{
		if ( column == null )
			return;

		SlotHandle columns = adapter.getColumns( );
		TableColumn sourceColumn = ColumnHelper.findColumn(
				adapter.getDesign( ), columns.getSlot( ), sourceIndex );
		ColumnHandle sourceCol = (ColumnHandle) sourceColumn.getHandle( adapter
				.getDesign( ) );

		pasteColumn( column, destIndex, true );

		int repeat = sourceCol.getRepeatCount( );

		if ( repeat == 1 )
			columns.drop( sourceCol );
		else
		{
			sourceCol.setRepeatCount( repeat - 1 );
		}
	}

	/**
	 * Moves cells from one column band to another column band.
	 * 
	 * @param sourceIndex
	 *            the source column to shift
	 * @param destIndex
	 *            the target column to shift
	 * @throws SemanticException
	 *             if any error occurs during moving cells
	 */

	private void shiftCells( List cellInfos, int sourceIndex, int destIndex )
			throws SemanticException
	{
		// adds the copied cells to the destination.

		for ( int i = 0; i < cellInfos.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) cellInfos.get( i );

			// groupId is equal to -1, means this is a top slot in the table

			RowHandle row = adapter.getRow( contextInfo.getSlotId( ),
					contextInfo.getGroupId( ), contextInfo.getRowNumber( ) );

			assert row != null;

			CellHandle cell = contextInfo.getCell( ).handle(
					adapter.getDesign( ) );
			cell.setColumn( 0 );

			// if this is only paste operation, then paste it to the old
			// position. Otherwise, apppend it to the next avaiable position.

			row.getCells( ).shift( cell, destIndex );
		}
	}

	/**
	 * Checks whether the paste operation can be done with the given copied
	 * column band data, the column index and the operation flag.
	 * 
	 * @param sourceColumn
	 *            the source column to shift
	 * @param destColumn
	 *            the target column to shift
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean checkTargetColumn( int sourceColumn, int destColumn )
	{

		// If the new position is the same as the old, then skip the operation.

		int newPosn = adjustDestPosn( sourceColumn, destColumn );
		if ( newPosn == -1 )
			return true;

		int columnCount = adapter.getColumnCount( );
		if ( newPosn == 0 || newPosn == columnCount )
			return true;

		List originalCells = getCellsContextInfo( adapter
				.getCellsUnderColumn( newPosn ) );

		if ( !isRectangleArea( originalCells, 1 ) )
			return false;

		return true;
	}

}

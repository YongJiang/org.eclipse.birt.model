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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.interfaces.ITableColumnModel;

/**
 * Represents a column within a table. The application generally does not create
 * column handles directly. Instead, it uses one of the navigation methods
 * available on other element handles like: <code>TableHandle</code>.
 * 
 * 
 * @see TableHandle#getColumns()
 * @see org.eclipse.birt.report.model.elements.TableColumn
 */

public class ColumnHandle extends ReportElementHandle
		implements
			ITableColumnModel
{

	/**
	 * Constructs a handle for the given design and a column element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the column element
	 */

	public ColumnHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the repeat count for this column. The repeat count is the number
	 * of contiguous table or grid columns described by this column definition.
	 * It simplifies the design because one column definition can describe a
	 * group of adjacent columns.
	 * 
	 * @return the number of contiguous columns described by this column
	 *         definition
	 */

	public int getRepeatCount( )
	{
		return getIntProperty( TableColumn.REPEAT_PROP );
	}

	/**
	 * Sets the repeat count for this column.
	 * 
	 * @param count
	 *            the number of contiguous columns described by this column
	 *            definition
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 * 
	 * @see #getRepeatCount()
	 */

	public void setRepeatCount( int count ) throws SemanticException
	{
		setIntProperty( TableColumn.REPEAT_PROP, count );
	}

	/**
	 * Gets the column width as a dimension handle.
	 * 
	 * @return a dimension handle to for the column width.
	 */

	public DimensionHandle getWidth( )
	{
		return super.getDimensionProperty( TableColumn.WIDTH_PROP );
	}

	/**
	 * Gets the column alignment.
	 * 
	 * @return the column alignment
	 * 
	 * @deprecated by the {@link StyleHandle#getTextAlign()}
	 */

	public String getAlignment( )
	{
		return ""; //$NON-NLS-1$
	}

	/**
	 * Sets the column alignment.
	 * 
	 * @param alignment
	 *            the alignment to set
	 * @throws SemanticException
	 *             if the value is not in choice.
	 * 
	 * @deprecated by the {@link StyleHandle#setTextAlign(String)}
	 */

	public void setAlignment( String alignment ) throws SemanticException
	{
	}

	/**
	 * Get the suppress duplicates property of this column.
	 * 
	 * @return a boolean value which indicates if this column is suppress
	 *         duplicates.
	 */

	public boolean suppressDuplicates( )
	{
		return getBooleanProperty( SUPPRESS_DUPLICATES_PROP );
	}

	/**
	 * Set the suppress duplicates property of this column.
	 * 
	 * @param suppressDuplicates
	 *            the suppress duplicates value.
	 */
	public void setSuppressDuplicates( boolean suppressDuplicates )
	{
		try
		{
			setProperty( SUPPRESS_DUPLICATES_PROP, String
					.valueOf( suppressDuplicates ) );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}
}
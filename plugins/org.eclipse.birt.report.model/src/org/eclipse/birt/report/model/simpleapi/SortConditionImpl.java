/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;

/**
 * Implements of Sort Condition
 * 
 */

public class SortConditionImpl implements ISortCondition
{

	private SortKey sort;

	private SortKeyHandle sortHandle;

	/**
	 * Constructor
	 * 
	 * @param sortHandle
	 */

	public SortConditionImpl( )
	{
		sort = createSortCondition( );
	}

	/**
	 * Constructor
	 * 
	 * @param sortHandle
	 */

	public SortConditionImpl( SortKeyHandle sortHandle )
	{
		if ( sortHandle == null )
		{
			sort = createSortCondition( );
		}
		else
		{
			this.sortHandle = sortHandle;
			sort = (SortKey) sortHandle.getStructure( );
		}
	}

	/**
	 * Constructor
	 * 
	 * @param sort
	 */
	public SortConditionImpl( SortKey sort )
	{
		if ( sort == null )
		{
			sort = createSortCondition( );
		}
		else
		{
			this.sort = sort;
		}
	}

	/**
	 * Create instance of <code>HideRule</code>
	 * 
	 * @return instance
	 */
	private SortKey createSortCondition( )
	{
		SortKey s = new SortKey( );
		return s;
	}

	public String getDirection( )
	{
		return sort.getDirection( );
	}

	public String getKey( )
	{
		return sort.getKey( );
	}

	public void setDirection( String direction ) throws SemanticException
	{
		if ( sortHandle != null )
		{
			sortHandle.setDirection( direction );
			return;
		}

		sort.setDirection( direction );
	}

	public void setKey( String key )  throws SemanticException
	{
		// key is required
		if ( sortHandle != null )
		{
			sortHandle.setKey( key );
			return;
		}
		
		sort.setKey( key );
	}

	public IStructure getStructure( )
	{
		return sort;
	}

}

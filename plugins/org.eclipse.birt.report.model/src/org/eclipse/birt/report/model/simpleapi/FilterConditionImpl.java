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

import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;

/**
 * Implements of FilterCondition.
 * 
 */

public class FilterConditionImpl implements IFilterCondition
{

	private FilterCondition condition;
	
	private FilterConditionHandle conditionHandle;
	
	/**
	 * Constructor
	 * 
	 * @param condition
	 */
	
	public FilterConditionImpl( )
	{	
		condition = createFilterCondition( );
	}

	/**
	 * Constructor
	 * 
	 * @param condition
	 */
	
	public FilterConditionImpl( FilterCondition condition )
	{
		if ( condition == null )
		{
			condition = createFilterCondition( );
		}
		else
		{

			this.condition = condition;
		}
	}

	/**
	 * Constructor
	 * 
	 * @param conditionHandle
	 */
	
	public FilterConditionImpl( FilterConditionHandle conditionHandle )
	{
		if ( conditionHandle == null )
		{
			condition = createFilterCondition( );
		}
		else
		{
			this.conditionHandle = conditionHandle; 
			condition = (FilterCondition) conditionHandle.getStructure( );
		}
	}

	private FilterCondition createFilterCondition( )
	{
		FilterCondition f = new FilterCondition( );
		return f;
	}

	public String getOperator( )
	{
		return condition.getOperator( );
	}

	public String getValue1( )
	{
		return condition.getValue1( );
	}

	public String getValue2( )
	{
		return condition.getValue2( );
	}

	public void setOperator( String operator ) throws SemanticException
	{
		if (conditionHandle != null)
		{
			conditionHandle.setOperator( operator );
			return;
		}
		
		condition.setOperator( operator );
	}

	public void setValue1( String value1 ) throws SemanticException
	{
		if (conditionHandle != null)
		{
			conditionHandle.setValue1( value1 );
			return;
		}
		
		condition.setValue1( value1 );
	}

	public void setValue2( String value2 ) throws SemanticException
	{
		if (conditionHandle != null)
		{
			conditionHandle.setValue2( value2 );
			return;
		}
		
		condition.setValue2( value2 );
	}

	public IStructure getStructure( )
	{
		return condition;
	}

	public String getExpr( )
	{
		return condition.getExpr( );
	}

	public void setExpr( String expr ) throws SemanticException
	{
		if (conditionHandle != null)
		{
			conditionHandle.setExpr( expr );
			return;
		}
		
		condition.setExpr( expr );
	}

}

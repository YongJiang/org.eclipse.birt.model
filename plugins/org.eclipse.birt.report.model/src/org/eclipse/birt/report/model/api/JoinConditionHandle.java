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
import org.eclipse.birt.report.model.api.elements.structures.JoinCondition;

/**
 * Represents a handle of condition used for joint dataset. The joint dataset is
 * dataset joined by several normal datasets on join conditions.
 * 
 * Each join condition has following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Join Type </strong></dt>
 * <dd>the join type of the join condition which can be inner join, left out
 * join and right out join.</dd>
 * </dl>
 * <p>
 * 
 * <p>
 * <dl>
 * <dt><strong>Operator </strong></dt>
 * <dd>the join condition's comparison operator</dd>
 * </dl>
 * <p>
 * 
 * <p>
 * <dl>
 * <dt><strong>Left Dataset </strong></dt>
 * <dd>the left dataset of the join condition.</dd>
 * </dl>
 * <p>
 * 
 * <p>
 * <dl>
 * <dt><strong>Right Dataset </strong></dt>
 * <dd>the right dataset of the join condition.</dd>
 * </dl>
 * <p>
 * 
 * <p>
 * <dl>
 * <dt><strong>Left Expression </strong></dt>
 * <dd>the left expression of the join condition.</dd>
 * </dl>
 * <p>
 * 
 * <p>
 * <dl>
 * <dt><strong>Right Expression </strong></dt>
 * <dd>the right expression of the join condition.</dd>
 * </dl>
 * <p>
 * 
 */

public class JoinConditionHandle extends StructureHandle
{

	/**
	 * Constructs the handle of join condition.
	 * 
	 * @param valueHandle
	 *            the value handle for join condition list of one property
	 * @param index
	 *            the position of this join condition in the list
	 */

	public JoinConditionHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Sets the join type value of this condition.
	 * 
	 * @param type
	 *            the join type to set
	 * @throws SemanticException
	 */

	public void setJoinType( String type ) throws SemanticException
	{
		setProperty( JoinCondition.JOIN_TYPE_MEMBER, type );
	}

	/**
	 * Returns join type value this condition.
	 * 
	 * @return the join type value
	 */

	public String getJoinType( )
	{
		return getStringProperty( JoinCondition.JOIN_TYPE_MEMBER );
	}

	/**
	 * Sets the operator value of this condition.
	 * 
	 * @param operator
	 *            the operator to set
	 * @throws SemanticException
	 */

	public void setOperator( String operator ) throws SemanticException
	{
		setProperty( JoinCondition.JOIN_OPERATOR_MEMBER, operator );
	}

	/**
	 * Returns operator value this condition.
	 * 
	 * @return the operator value
	 */

	public String getOperator( )
	{
		return getStringProperty( JoinCondition.JOIN_OPERATOR_MEMBER );
	}

	/**
	 * Sets the left data set value of this condition.
	 * 
	 * @param leftDataset
	 *            the left data set to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setLeftDataSet( String leftDataset ) throws SemanticException
	{
		setProperty( JoinCondition.LEFT_DATASET_MEMBER, leftDataset );
	}

	/**
	 * Returns left data set value this condition.
	 * 
	 * @return the left data set value
	 */

	public String getLeftDataSet( )
	{
		return getStringProperty( JoinCondition.LEFT_DATASET_MEMBER );
	}

	/**
	 * Sets the right data set value of this condition.
	 * 
	 * @param rightDataset
	 *            the right data set to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setRightDataSet( String rightDataset ) throws SemanticException
	{
		setProperty( JoinCondition.RIGHT_DATASET_MEMBER, rightDataset );
	}

	/**
	 * Returns right data set value this condition.
	 * 
	 * @return the right data set value
	 */

	public String getRightDataSet( )
	{
		return getStringProperty( JoinCondition.RIGHT_DATASET_MEMBER );
	}

	/**
	 * Sets the left expression value of this condition.
	 * 
	 * @param leftExpression
	 *            the left expression to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setLeftExpression( String leftExpression )
			throws SemanticException
	{
		setProperty( JoinCondition.LEFT_EXPRESSION_MEMBER, leftExpression );
	}

	/**
	 * Returns left expression value this condition.
	 * 
	 * @return the left expression value
	 */

	public String getLeftExpression( )
	{
		return getStringProperty( JoinCondition.LEFT_EXPRESSION_MEMBER );
	}

	/**
	 * Sets the right expression value of this condition.
	 * 
	 * @param rightExpression
	 *            the right expression to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setRightExpression( String rightExpression )
			throws SemanticException
	{
		setProperty( JoinCondition.RIGHT_EXPRESSION_MEMBER, rightExpression );
	}

	/**
	 * Returns right expression value this condition.
	 * 
	 * @return the right expression value
	 */

	public String getRightExpression( )
	{
		return getStringProperty( JoinCondition.RIGHT_EXPRESSION_MEMBER );
	}

}

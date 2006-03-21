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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.List;

import org.eclipse.birt.report.model.api.JoinConditionHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents a condition used for joint data set. The joint data set is data
 * set joined by several normal data sets on join conditions.
 * 
 * Each join condition has the following properties:
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
 * <dd>the left Expression of the join condition.</dd>
 * </dl>
 * <p>
 * 
 * <p>
 * <dl>
 * <dt><strong>Right Expression </strong></dt>
 * <dd>the right Expression of the join condition.</dd>
 * </dl>
 * <p>
 * 
 */

public class JoinCondition extends Structure
{

	/**
	 * Name of the structure.
	 */

	public static final String STRUCTURE_NAME = "JoinCondition"; //$NON-NLS-1$

	/**
	 * Name of the type property.
	 */

	public static final String JOIN_TYPE_MEMBER = "joinType"; //$NON-NLS-1$

	/**
	 * Name of the operator property.
	 */

	public static final String JOIN_OPERATOR_MEMBER = "joinOperator"; //$NON-NLS-1$

	/**
	 * Name of the left dataset property.
	 */

	public static final String LEFT_DATASET_MEMBER = "leftDataSet"; //$NON-NLS-1$

	/**
	 * Name of the right dataset property.
	 */

	public static final String RIGHT_DATASET_MEMBER = "rightDataSet"; //$NON-NLS-1$

	/**
	 * Name of the left coloumn property.
	 */

	public static final String LEFT_EXPRESSION_MEMBER = "leftExpression"; //$NON-NLS-1$

	/**
	 * Name of the right coloumn property.
	 */

	public static final String RIGHT_EXPRESSION_MEMBER = "rightExpression"; //$NON-NLS-1$

	/**
	 * Value of the type property.
	 */

	protected String joinType = null;

	/**
	 * Value of the operator property.
	 */

	protected String joinOperator = null;

	/**
	 * Value of the left dataset property.
	 */

	protected String leftDataSet = null;

	/**
	 * Value of the right dataset property.
	 */

	protected String rightDataSet = null;

	/**
	 * Value of the left coloumn property.
	 */

	protected String leftExpression = null;

	/**
	 * Value of the right coloumn property.
	 */

	protected String rightExpression = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( JOIN_TYPE_MEMBER.equals( propName ) )
			return joinType;
		else if ( JOIN_OPERATOR_MEMBER.equals( propName ) )
			return joinOperator;
		else if ( LEFT_DATASET_MEMBER.equals( propName ) )
			return leftDataSet;
		else if ( RIGHT_DATASET_MEMBER.equals( propName ) )
			return rightDataSet;
		else if ( LEFT_EXPRESSION_MEMBER.equals( propName ) )
			return leftExpression;
		else if ( RIGHT_EXPRESSION_MEMBER.equals( propName ) )
			return rightExpression;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( JOIN_TYPE_MEMBER.equals( propName ) )
			joinType = (String) value;
		else if ( JOIN_OPERATOR_MEMBER.equals( propName ) )
			joinOperator = (String) value;
		else if ( LEFT_DATASET_MEMBER.equals( propName ) )
			leftDataSet = (String) value;
		else if ( RIGHT_DATASET_MEMBER.equals( propName ) )
			rightDataSet = (String) value;
		else if ( LEFT_EXPRESSION_MEMBER.equals( propName ) )
			leftExpression = (String) value;
		else if ( RIGHT_EXPRESSION_MEMBER.equals( propName ) )
			rightExpression = (String) value;

		else
			assert false;
	}

	/**
	 * Sets the join type value of this condition.
	 * 
	 * @param type
	 *            the join type to set
	 */

	public void setJoinType( String type )
	{
		setProperty( JOIN_TYPE_MEMBER, type );
	}

	/**
	 * Returns join type value this condition.
	 * 
	 * @return the join type value
	 */

	public String getJoinType( )
	{
		return (String) getProperty( null, JOIN_TYPE_MEMBER );
	}

	/**
	 * Sets the operator value of this condition.
	 * 
	 * @param operator
	 *            the operator to set
	 */

	public void setOperator( String operator )
	{
		setProperty( JOIN_OPERATOR_MEMBER, operator );
	}

	/**
	 * Returns operator value this condition.
	 * 
	 * @return the operator value
	 */

	public String getOperator( )
	{
		return (String) getProperty( null, JOIN_OPERATOR_MEMBER );
	}

	/**
	 * Sets the left data set value of this condition.
	 * 
	 * @param leftDataSet
	 *            the left data set to set
	 */

	public void setLeftDataSet( String leftDataSet )
	{
		setProperty( LEFT_DATASET_MEMBER, leftDataSet );
	}

	/**
	 * Returns left data set value this condition.
	 * 
	 * @return the left data set value
	 */

	public String getLeftDataSet( )
	{
		return (String) getProperty( null, LEFT_DATASET_MEMBER );
	}

	/**
	 * Sets the right data set value of this condition.
	 * 
	 * @param rightDataSet
	 *            the right data set to set
	 */

	public void setRightDataSet( String rightDataSet )
	{
		setProperty( RIGHT_DATASET_MEMBER, rightDataSet );
	}

	/**
	 * Returns right data set value this condition.
	 * 
	 * @return the right data set value
	 */

	public String getRightDataSet( )
	{
		return (String) getProperty( null, RIGHT_DATASET_MEMBER );
	}

	/**
	 * Sets the left expression value of this condition.
	 * 
	 * @param leftExpression
	 *            the left expression to set
	 */

	public void setLeftExpression( String leftExpression )
	{
		setProperty( LEFT_EXPRESSION_MEMBER, leftExpression );
	}

	/**
	 * Returns left expression value this condition.
	 * 
	 * @return the left expression value
	 */

	public String getLeftExpression( )
	{
		return (String) getProperty( null, LEFT_EXPRESSION_MEMBER );
	}

	/**
	 * Sets the right expression value of this condition.
	 * 
	 * @param rightExpression
	 *            the right expression to set
	 */

	public void setRightExpression( String rightExpression )
	{
		setProperty( RIGHT_EXPRESSION_MEMBER, rightExpression );
	}

	/**
	 * Returns right expression value this condition.
	 * 
	 * @return the right expression value
	 */

	public String getRightExpression( )
	{
		return (String) getProperty( null, RIGHT_EXPRESSION_MEMBER );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */

	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new JoinConditionHandle( valueHandle, index );
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li> The join type is required.
	 * <li> The operator is required.
	 * <li> The left data set is required.
	 * <li> The right data set is required.
	 * <li> The left expression is required.
	 * <li> The right expression is required.
	 * </ul>
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate( Module module, DesignElement element )
	{
		List list = super.validate( module, element );

		checkStringMember( joinType, JOIN_TYPE_MEMBER, element, list );
		checkStringMember( joinOperator, JOIN_OPERATOR_MEMBER, element, list );
		checkStringMember( leftDataSet, LEFT_DATASET_MEMBER, element, list );
		checkStringMember( rightDataSet, RIGHT_DATASET_MEMBER, element, list );
		checkStringMember( leftExpression, LEFT_EXPRESSION_MEMBER, element,
				list );
		checkStringMember( rightExpression, RIGHT_EXPRESSION_MEMBER, element,
				list );

		checkDataSet( module, leftDataSet, list, element );
		checkDataSet( module, rightDataSet, list, element );
		return list;
	}

	/**
	 * Checks if a string member is blank. If the member is blank, a smantic
	 * error is added in to the given error list tolding that the member value
	 * is required.
	 * 
	 * @param value
	 *            the member
	 * @param memberName
	 *            the name of the member
	 * @param element
	 *            the element contain's this join condition
	 * @param errorList
	 *            the error list
	 */

	private void checkStringMember( String value, String memberName,
			DesignElement element, List errorList )
	{
		if ( StringUtil.isBlank( value ) )
		{
			errorList.add( new PropertyValueException( element, getDefn( )
					.getMember( memberName ), value,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}
	}

	/**
	 * Check if a data set is added into this joint data set.
	 * 
	 * @param module
	 *            the module.
	 * @param dataSetName
	 *            the name of the data set.
	 * @param errors
	 *            the errors.
	 */
	private void checkDataSet( Module module, String dataSetName, List errors,
			DesignElement element )
	{
		if ( dataSetName == null)
		{
			return;
		}

		List dataSetsReferences = (List) element.getProperty( module,
				JointDataSet.DATA_SETS_PROP );
		if ( dataSetsReferences != null )
		{
			int dataSetIndex;
			for ( dataSetIndex = 0; dataSetIndex < dataSetsReferences.size( ); dataSetIndex++ )
			{
				if ( ( (ElementRefValue) dataSetsReferences.get( dataSetIndex ) )
						.getName( ).equals( dataSetName ) )
				{
					return;
				}
			}
		}

		errors
				.add( new SemanticError(
						element,
						new String[]{element.getName( ), dataSetName},
						SemanticError.DESIGN_EXCEPTION_DATA_SET_MISSED_IN_JOINT_DATA_SET ) );
	}
}
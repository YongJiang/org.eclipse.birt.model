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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.PropertyStructure;

/**
 * Base class for highlight and map rules in the style. Choices for the operand
 * are defined in <code>DesignChoiceConstants</code>.
 * 
 * @see DesignChoiceConstants
 */

public abstract class StyleRule extends PropertyStructure
{

	/**
	 * Name of the comparison operator member.
	 */

	public static final String OPERATOR_MEMBER = "operator"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the expression for the first operator
	 * operand.
	 */

	public static final String VALUE1_MEMBER = "value1"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the expression for the second operator
	 * operand.
	 */

	public static final String VALUE2_MEMBER = "value2"; //$NON-NLS-1$

	/**
	 * The comparison operator. Default value is <code>MAP_OPERATOR_EQ</code>
	 */

	protected String operator = null;

	/**
	 * Expression for the first operand.
	 */

	protected String value1 = null;

	/**
	 * Expression for the second operand.
	 */

	protected String value2 = null;

	/**
	 * Default constructor.
	 */

	public StyleRule( )
	{
	}

	/**
	 * Constructs the style rule with an operator and its operands.
	 * 
	 * @param op
	 *            the choice name for the operand
	 * @param v1
	 *            expression for the first operand
	 * @param v2
	 *            expression for the second operand
	 */

	public StyleRule( String op, String v1, String v2 )
	{
		operator = op;
		value1 = v1;
		value2 = v2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.PropertyStructure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( OPERATOR_MEMBER.equals( propName ) )
			return operator;
		if ( VALUE1_MEMBER.equals( propName ) )
			return value1;
		if ( VALUE2_MEMBER.equals( propName ) )
			return value2;

		return super.getIntrinsicProperty( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.PropertyStructure#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( OPERATOR_MEMBER.equals( propName ) )
			operator = (String) value;
		else if ( VALUE1_MEMBER.equals( propName ) )
			value1 = (String) value;
		else if ( VALUE2_MEMBER.equals( propName ) )
			value2 = (String) value;
		else
			super.setIntrinsicProperty( propName, value );
	}

	/**
	 * Returns the operator. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and they
	 * are:
	 * <ul>
	 * <li>MAP_OPERATOR_EQ
	 * <li>MAP_OPERATOR_NE
	 * <li>MAP_OPERATOR_LT
	 * <li>MAP_OPERATOR_LE
	 * <li>MAP_OPERATOR_GE
	 * <li>MAP_OPERATOR_GT
	 * <li>MAP_OPERATOR_BETWEEN
	 * <li>MAP_OPERATOR_NOT_BETWEEN
	 * <li>MAP_OPERATOR_NULL
	 * <li>MAP_OPERATOR_NOT_NULL
	 * <li>MAP_OPERATOR_TRUE
	 * <li>MAP_OPERATOR_FALSE
	 * <li>MAP_OPERATOR_LIKE
	 * <li>MAP_OPERATOR_ANY
	 * </ul>
	 * 
	 * @return the operator
	 */

	public String getOperator( )
	{
		return (String) getProperty( null, OPERATOR_MEMBER );
	}

	/**
	 * Sets the operator. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and they
	 * are:
	 * <ul>
	 * <li>MAP_OPERATOR_EQ
	 * <li>MAP_OPERATOR_NE
	 * <li>MAP_OPERATOR_LT
	 * <li>MAP_OPERATOR_LE
	 * <li>MAP_OPERATOR_GE
	 * <li>MAP_OPERATOR_GT
	 * <li>MAP_OPERATOR_BETWEEN
	 * <li>MAP_OPERATOR_NOT_BETWEEN
	 * <li>MAP_OPERATOR_NULL
	 * <li>MAP_OPERATOR_NOT_NULL
	 * <li>MAP_OPERATOR_TRUE
	 * <li>MAP_OPERATOR_FALSE
	 * <li>MAP_OPERATOR_LIKE
	 * <li>MAP_OPERATOR_ANY
	 * </ul>
	 * 
	 * @param operator
	 *            the operator to set
	 */

	public void setOperator( String operator )
	{
		setProperty( OPERATOR_MEMBER, operator );
	}

	/**
	 * Returns the expression for the first operand.
	 * 
	 * @return the first operand expression
	 */

	public String getValue1( )
	{
		return (String) getProperty( null, VALUE1_MEMBER );
	}
    
    
    /**
     * Set expression for the first operand.
     * 
     * @param value the first operand expression.
     */
    
    public void setValue1( String value )
    {
    	setProperty( VALUE1_MEMBER, value );
    }

	/**
	 * Returns the expression for the second operand.
	 * 
	 * @return the second operand expression
	 */

	public String getValue2( )
	{
		return (String) getProperty( null, VALUE2_MEMBER );
	}
    
    /**
     * Set expression for the second operand.
     * 
     * @param value the second operand expression.
     */
    
    public void setValue2( String value )
    {
        setProperty( VALUE2_MEMBER, value );
    }

}
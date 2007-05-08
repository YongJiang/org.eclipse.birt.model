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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an DataBinding in the scripting environment
 * 
 */

public interface IDataBinding
{

	/**
	 * Returns the name of column binding.
	 * 
	 * @return name name of column binding.
	 */

	public String getName( );

	/**
	 * Sets the name of column binding.
	 * 
	 * @param name
	 *            name of column binding.
	 * @exception ScriptException
	 */

	public void setName( String name );

	/**
	 * Returns expression of column binding
	 * 
	 * @return expression of column binding
	 */

	public String getExpression( );

	/**
	 * Sets expression of column binding.
	 * 
	 * @param expression
	 *            expression of column binding.
	 * @exception ScriptException
	 */

	public void setExpression( String expression );

	/**
	 * Returns data type of column binding.
	 * 
	 * <p>
	 * <ul>
	 * <li><code>any</code>
	 * <li><code>integer</code>
	 * <li><code>string</code>
	 * <li><code>date-time</code>
	 * <li><code>decimal</code>
	 * <li><code>float</code>
	 * <li><code>boolean</code>
	 * </ul>
	 * 
	 * @return data type of column binding
	 */
	
	public String getDataType( );

	/**
	 * Sets data type of column binding
	 * 
	 * @param dataType
	 * @exception ScriptException
	 */
	
	public void setDataType( String dataType );

	/**
	 * Returns aggregateOn of column binding
	 * 
	 * @return aggregateOn of column binding
	 */
	
	public String getAggregateOn( );

	/**
	 * Sets aggregateOn of column binding.
	 * 
	 * @param on
	 *            aggregateOn of column binding.
	 * @exception ScriptException
	 */
	
	public void setAggregateOn( String on );

	/**
	 * Returns structure.
	 * 
	 * @return structure
	 */

	public IStructure getStructure( );
}
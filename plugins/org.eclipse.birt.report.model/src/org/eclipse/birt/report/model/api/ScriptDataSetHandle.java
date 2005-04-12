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
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ScriptDataSet;

/**
 * Represents a script data set item. The scripted data set gives the report
 * developer the ability to implement a data set in code. The developer
 * implements a series of simple scripts to open the data set, fetch each row,
 * and to close the data set.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.ScriptDataSet
 */

public class ScriptDataSetHandle extends DataSetHandle
{

	/**
	 * Constructs a handle for script data set.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the script data set element
	 */
	public ScriptDataSetHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the script for opening data set.
	 * 
	 * @return the script for opening data set.
	 */

	public String getOpen( )
	{
		return getStringProperty( ScriptDataSet.OPEN_METHOD );
	}

	/**
	 * Sets the script for opening data set.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOpen( String value ) throws SemanticException
	{
		setProperty( ScriptDataSet.OPEN_METHOD, value );
	}

	/**
	 * Returns the script for describing the result set dynamically.
	 * 
	 * @return the script for describing the result set dynamically
	 */

	public String getDescribe( )
	{
		return getStringProperty( ScriptDataSet.DESCRIBE_METHOD );
	}

	/**
	 * Sets the script for describing the result set dynamically.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDescribe( String value ) throws SemanticException
	{
		setProperty( ScriptDataSet.DESCRIBE_METHOD, value );
	}
	
	/**
	 * Returns the script for providing the data for the next row from the
	 * result set. When the data set has returned the last row from the result
	 * set, subsequent calls to Fetch should return null. (A<code>null</code>
	 * return value indicates the end of the result set.)
	 * 
	 * @return the script for providing the data for the next row from the
	 *         result set.
	 */

	public String getFetch( )
	{
		return getStringProperty( ScriptDataSet.FETCH_METHOD );
	}

	/**
	 * Sets the script for providing the data for the next row from the result
	 * set.
	 * 
	 * @param value
	 *            the script to set
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 * @see #getFetch()
	 */

	public void setFetch( String value ) throws SemanticException
	{
		setProperty( ScriptDataSet.FETCH_METHOD, value );
	}

	/**
	 * Returns the script for closing data set.
	 * 
	 * @return the script for closing data set.
	 */

	public String getClose( )
	{
		return getStringProperty( ScriptDataSet.CLOSE_METHOD );
	}

	/**
	 * Sets the script for closing data set.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setClose( String value ) throws SemanticException
	{
		setStringProperty( ScriptDataSet.CLOSE_METHOD, value );
	}

}
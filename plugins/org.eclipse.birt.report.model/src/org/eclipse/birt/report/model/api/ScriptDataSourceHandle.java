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
import org.eclipse.birt.report.model.elements.ScriptDataSource;

/**
 * Represents a script data source. Script data source is one that is defined in
 * JavaScript. The application is responsible for implementing two operations:
 * <ul>
 * <li>Open: connect to the external system. Report an error if the connection
 * fails.
 * <li>Close: drop the connection to the external system.
 * </ul>
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.ScriptDataSource
 */

public class ScriptDataSourceHandle extends DataSourceHandle
{

	/**
	 * Constructs a handle for script data source.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the script data source element
	 */

	public ScriptDataSourceHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Sets the script for opening data connection.
	 * 
	 * @param value
	 *            the script to set.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOpen( String value ) throws SemanticException
	{
		setProperty( ScriptDataSource.OPEN_METHOD, value );
	}

	/**
	 * Returns the script name for opening data connection.
	 * 
	 * @return the script name for opening data connection.
	 */

	public String getOpen( )
	{
		return getStringProperty( ScriptDataSource.OPEN_METHOD );
	}

	/**
	 * Sets the script name for closing data connection.
	 * 
	 * @param value
	 *            the script name to set.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setClose( String value ) throws SemanticException
	{
		setProperty( ScriptDataSource.CLOSE_METHOD, value );
	}

	/**
	 * Returns the script name for closing data connection.
	 * 
	 * @return the script name for closing data connection.
	 */

	public String getClose( )
	{
		return getStringProperty( ScriptDataSource.CLOSE_METHOD );
	}

}
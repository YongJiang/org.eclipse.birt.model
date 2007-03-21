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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Included css style sheet exception
 *
 */

public class CssException  extends SemanticException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5798109731640445551L;
	
	/**
	 * Indicates the css is not found in module.
	 */

	public final static String DESIGN_EXCEPTION_CSS_NOT_FOUND = MessageConstants.CSS_EXCEPTION_CSS_NOT_FOUND;

	/**
	 * Indicates the css is using is duplicate.
	 */

	public final static String DESIGN_EXCEPTION_DUPLICATE_CSS = MessageConstants.CSS_EXCEPTION_DUPLICATE_CSS;

	/**
	 * Bad css file. 
	 */
	
	public final static String DESIGN_EXCEPTION_BADCSSFILE = MessageConstants.CSS_EXCEPTION_BADCSSFILE;
	
	/**
	 * Read-only style
	 */
	
	public final static String DESIGN_EXCEPTION_READONLY = MessageConstants.CSS_EXCEPTION_READONLY;
	
	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module which has errors
	 * @param errCode
	 *            the error code
	 */

	public CssException( Module module, String errCode )
	{
		super( module, errCode );
	}

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module which has errors
	 * @param values
	 *            value array used for error message
	 * @param errCode
	 *            the error code
	 */

	public CssException( Module module, String[] values, String errCode )
	{
		super( module, values, errCode );
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	
	public String getLocalizedMessage( )
	{
		return ModelMessages.getMessage( sResourceKey );
	}

}
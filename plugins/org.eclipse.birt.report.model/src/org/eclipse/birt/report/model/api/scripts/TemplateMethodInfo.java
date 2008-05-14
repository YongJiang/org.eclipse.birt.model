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

package org.eclipse.birt.report.model.api.scripts;

import java.lang.reflect.Method;

import org.eclipse.birt.report.model.api.metadata.ITemplateMethodInfo;

/**
 * Represents the method information that can provide code snippet as the
 * template.
 */

public class TemplateMethodInfo extends MethodInfo
		implements
			ITemplateMethodInfo
{

	/**
	 * Default constructor.
	 * 
	 * @param method
	 */

	public TemplateMethodInfo( Method method )
	{
		super( method );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.ITemplateMethodInfo#getCodeTemplate()
	 */

	public String getCodeTemplate( )
	{
		return ""; //$NON-NLS-1$
	}

}
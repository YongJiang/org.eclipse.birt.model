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

package org.eclipse.birt.report.model.i18n;

/**
 * Returns the localized messages from I18N message file. This class can not be
 * instantiated.
 */

public class ModelMessages
{

	/**
	 * This class can not be instantiated.
	 */

	private ModelMessages( )
	{
	}

	/**
	 * The I18N resource handler.
	 */

	private static ThreadResources threadResources = null;

	static
	{
		String packageName = ModelMessages.class.getPackage( ).getName( );

		threadResources = new ThreadResources( ModelMessages.class
				.getClassLoader( ), packageName + ".Messages" ); //$NON-NLS-1$
	}

	/**
	 * Returns the localized message with the given resource key.
	 * 
	 * @param key
	 *            the resource key
	 * @return the localized message
	 */

	public static String getMessage( String key )
	{
		return threadResources.getMessage( key );
	}

	/**
	 * Returns the localized message with the given resource key and arguments.
	 * 
	 * @param key
	 *            the resource key
	 * @param arguments
	 *            the arguments for localized message
	 * @return the localized message
	 */

	public static String getMessage( String key, Object[] arguments )
	{
		return threadResources.getMessage( key, arguments );
	}

}
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

import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the definition of argument. The argument definition includes the
 * data type, internal name, and display name.
 */

public class ArgumentInfo implements IArgumentInfo
{

	private Class clazz;

	/**
	 * Constructor.
	 * 
	 * @param argumentType
	 *            the argument type.
	 */

	protected ArgumentInfo( Class argumentType )
	{
		this.clazz = argumentType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IArgumentInfo#getName()
	 */

	public String getName( )
	{
		return StringUtil.EMPTY_STRING;
	}

	/**
	 * Returns the display name for the property if the resource key of display
	 * name is defined. Otherwise, return empty string.
	 * 
	 * @return the user-visible, localized display name for the property
	 */

	public String getDisplayName( )
	{
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the resource key for the display name.
	 * 
	 * @return The display name message ID.
	 */

	public String getDisplayNameKey( )
	{
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the script type of this argument.
	 * 
	 * @return the script type to set
	 */

	public String getType( )
	{
		return clazz.getName( );
	}

	/**
	 * Returns the class type of this argument.
	 * 
	 * @return the class type to set
	 */

	public IClassInfo getClassType( )
	{
		return new ClassInfo( clazz );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString( )
	{
		if ( !StringUtil.isBlank( getName( ) ) )
			return getName( );
		return super.toString( );
	}
}
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


package org.eclipse.birt.report.model.plugin;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.datatools.connectivity.oda.util.manifest.PropertyChoice;

/**
 * Represents the choice wrapping ODA choice.
 */

public class ODAChoice implements IChoice
{

	private PropertyChoice choice;

	/**
	 * Constructs the choice with ODA choice definition.
	 * 
	 * @param choice
	 *            ODA choice definition
	 */

	public ODAChoice( PropertyChoice choice )
	{
		this.choice = choice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoice#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return choice.getDisplayName( );
	}

	/**
	 * Returns <code>null</code> always. No display name key of ODA choice can
	 * be got.
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoice#getDisplayNameKey()
	 */

	public String getDisplayNameKey( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoice#getName()
	 */

	public String getName( )
	{
		return choice.getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoice#getValue()
	 */

	public Object getValue( )
	{
		return choice.getValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoice#copy()
	 */

	public IChoice copy( )
	{
		try
		{
			return (IChoice) clone( );
		}
		catch ( CloneNotSupportedException e )
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	protected Object clone( ) throws CloneNotSupportedException
	{
		ODAChoice clone = (ODAChoice) super.clone( );
		clone.choice = choice;

		return clone;

	}
}
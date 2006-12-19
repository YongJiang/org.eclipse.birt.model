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

package org.eclipse.birt.report.model.elements.olap;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;

/**
 * This class represents a Level element. Level is the real element which
 * defines the column expression from the dataset.Use the
 * {@link org.eclipse.birt.report.model.api.olap.LevelHandle}class to change
 * the properties.
 * 
 */

public class Level extends ReferenceableElement implements ILevelModel
{

	/**
	 * Default constructor.
	 */

	public Level( )
	{

	}

	/**
	 * Constructs the level with an optional name.
	 * 
	 * @param name
	 *            the optional name for the level element
	 */

	public Level( String name )
	{
		super( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */
	public void apply( ElementVisitor visitor )
	{
		visitor.visitLevel( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName( )
	{
		return ReportDesignConstants.LEVEL_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.birt.report.model.core.Module)
	 */
	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the module of the level
	 * 
	 * @return an API handle for this element.
	 */

	public LevelHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new LevelHandle( module, this );
		}
		return (LevelHandle) handle;
	}
}

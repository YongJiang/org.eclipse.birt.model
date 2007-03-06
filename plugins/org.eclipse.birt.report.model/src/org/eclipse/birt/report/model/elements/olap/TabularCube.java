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
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;

/**
 * This class represents a Cube element. Cube is collection of dimensions and
 * measures. It specifies a dataset to refer to o outside data set element.Use
 * the {@link org.eclipse.birt.report.model.api.olap.CubeHandle}class to change
 * the properties.
 * 
 */

public class TabularCube extends Cube
{

	/**
	 * Default constructor.
	 */

	public TabularCube( )
	{
	}

	/**
	 * Constructs a cube element with the given name.
	 * 
	 * @param name
	 *            the name given for the element
	 */

	public TabularCube( String name )
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
		visitor.visitTabularCube( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.TABULAR_CUBE_ELEMENT;
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
	 *            the module of the cube
	 * 
	 * @return an API handle for this element.
	 */

	public TabularCubeHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new TabularCubeHandle( module, this );
		}
		return (TabularCubeHandle) handle;
	}
}

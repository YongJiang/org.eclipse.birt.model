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

package org.eclipse.birt.report.model.core.namespace;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Abstract element name space in one module. Subclass must override one or more
 * resolve methods in this class. Otherwise, assertion error may occur.
 */
abstract public class AbstractModuleNameContext extends AbstractNameContext
{

	protected Module module = null;
	
	protected int nameSpaceID = -1;

	/**
	 * Constructs the name space with module and name space ID.
	 * 
	 * @param module
	 *            the module this name space is assocaited.
	 * @param nameSpaceID 
	 */

	public AbstractModuleNameContext( Module module, int nameSpaceID )
	{
		this.module = module;
		this.nameSpaceID = nameSpaceID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getElement()
	 */
	public DesignElement getElement( )
	{
		return module;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getNameSpaceID()
	 */
	public int getNameSpaceID( )
	{
		return nameSpaceID;
	}	
	
}

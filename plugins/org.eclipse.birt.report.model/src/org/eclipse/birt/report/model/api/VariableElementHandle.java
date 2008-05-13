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
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IVariableElementModel;

/**
 * Represents a variable.
 */

public class VariableElementHandle extends ContentElementHandle
		implements
			IVariableElementModel
{

	/**
	 * Constructs a variable handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public VariableElementHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the name of the variable.
	 * 
	 * @return the variable name
	 * 
	 */

	public String getVariableName( )
	{
		return getStringProperty( IVariableElementModel.VARIABLE_NAME_PROP );
	}

	/**
	 * Sets the name of the variable.
	 * 
	 * @param name
	 *            the name to set
	 * 
	 * @throws SemanticException
	 * 
	 * @see #getVariableName()
	 */

	public void setVariableName( String name ) throws SemanticException
	{
		setStringProperty( IVariableElementModel.VARIABLE_NAME_PROP, name );
	}

	/**
	 * Returns the value of the variable.
	 * 
	 * 
	 * @return the variable value
	 */

	public String getValue( )
	{
		return getStringProperty( VALUE_PROP );
	}

	/**
	 * Sets the value of the variable.
	 * 
	 * @param value
	 *            the value to set
	 * @throws SemanticException

	 */

	public void setValue( String value ) throws SemanticException
	{
		setStringProperty( VALUE_PROP, value );
	}
}
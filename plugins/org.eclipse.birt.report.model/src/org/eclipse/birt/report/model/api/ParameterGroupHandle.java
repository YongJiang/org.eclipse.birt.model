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
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IParameterGroupModel;

/**
 * Represents a group of parameters. A parameter group creates a visual grouping
 * of parameters.
 * 
 * @see org.eclipse.birt.report.model.elements.ParameterGroup
 */

public class ParameterGroupHandle extends ReportElementHandle
		implements
			IParameterGroupModel
{

	/**
	 * Constructs the handle for a group parameters with the given design and
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public ParameterGroupHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns a slot handle to work with the parameters within the parameter
	 * group.
	 * 
	 * @return a handle for working with the parameters in this group
	 * 
	 * @see SlotHandle
	 */

	public SlotHandle getParameters( )
	{
		return getSlot( ParameterGroup.PARAMETERS_SLOT );
	}

	/**
	 * Returns additional pop-up help text associated with the group.
	 * 
	 * @return the help text
	 */

	public String getHelpText( )
	{
		return getStringProperty( ParameterGroup.HELP_TEXT_PROP );
	}

	/**
	 * Returns additional pop-up help text associated with the group.
	 * 
	 * @param text
	 *            the help text
	 * 
	 * @throws SemanticException
	 *             if the help text property is locked.
	 */

	public void setHelpText( String text ) throws SemanticException
	{
		setStringProperty( ParameterGroup.HELP_TEXT_PROP, text );
	}

	/**
	 * Returns the resource key of the help text for this parameter group.
	 * 
	 * @return the resource key of the help text
	 */

	public String getHelpTextKey( )
	{
		return getStringProperty( ParameterGroup.HELP_TEXT_KEY_PROP );
	}

	/**
	 * Sets the resource key of the help text for this parameter group.
	 * 
	 * @param text
	 *            the resource key of the help text
	 * 
	 * @throws SemanticException
	 *             if the resource-key of the help text property is locked.
	 */

	public void setHelpTextKey( String text ) throws SemanticException
	{
		setStringProperty( ParameterGroup.HELP_TEXT_KEY_PROP, text );
	}

	/**
	 * Tests whether the UI can expand and collapse groups.
	 * 
	 * @return <code>true</code> if can expand, otherwise <code>false</code>.
	 */

	public boolean startExpanded( )
	{
		return getBooleanProperty( ParameterGroup.START_EXPANDED_PROP );
	}

	/**
	 * Sets whether the UI can expand and collapse groups.
	 * 
	 * @param value
	 *            <code>true</code> if can expand, <code>false</code> not.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setStartExpanded( boolean value ) throws SemanticException
	{
		setProperty( ParameterGroup.START_EXPANDED_PROP, Boolean
				.valueOf( value ) );
	}

}
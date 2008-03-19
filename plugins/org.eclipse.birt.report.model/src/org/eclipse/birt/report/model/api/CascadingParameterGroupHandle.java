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
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.interfaces.ICascadingParameterGroupModel;

/**
 * Represents the group of cascading parameters. Cascading parameters are
 * created under the group.
 */

public class CascadingParameterGroupHandle extends ParameterGroupHandle
		implements
			ICascadingParameterGroupModel
{

	/**
	 * Constructs the handle for a group of cascading parameters with the given
	 * design and element.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the cascading parameter group element instance.
	 */

	public CascadingParameterGroupHandle( Module module,
			CascadingParameterGroup element )
	{
		super( module, element );
	}

	/**
	 * Returns the handle for the data set defined on the cascading parameter
	 * group.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		DesignElement dataSet = ( (CascadingParameterGroup) getElement( ) )
				.getDataSetElement( module );
		if ( dataSet == null )
			return null;

		return (DataSetHandle) dataSet.getHandle( dataSet.getRoot( ) );
	}

	/**
	 * Sets the data set of the report item.
	 * 
	 * @param handle
	 *            the handle of the data set, if <code>handle</code> is null,
	 *            data set property will be cleared.
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDataSet( DataSetHandle handle ) throws SemanticException
	{
		if ( handle == null )
			setStringProperty( DATA_SET_PROP, null );
		else
		{
			ModuleHandle moduleHandle = handle.getRoot( );
			String valueToSet = handle.getElement( ).getFullName( );
			if ( moduleHandle instanceof LibraryHandle )
			{
				String namespace = ( (LibraryHandle) moduleHandle )
						.getNamespace( );
				valueToSet = StringUtil.buildQualifiedReference( namespace,
						valueToSet );
			}

			setStringProperty( DATA_SET_PROP, valueToSet );
		}
	}

	/**
	 * Sets the value for the display prompt context.
	 * 
	 * @param promptValue
	 *            The display prompt context.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setPromptText( String promptValue ) throws SemanticException
	{
		setStringProperty( PROMPT_TEXT_PROP, promptValue );
	}

	/**
	 * Gets the display prompt text.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public String getPromptText( )
	{
		return getStringProperty( PROMPT_TEXT_PROP );
	}

	/**
	 * Sets the mode for data set support. It can be one of the following
	 * values:
	 * 
	 * <ul>
	 * <li> <code>DesignChoiceConstants.SINGLE_MODE</code>
	 * <code>DesignChoiceConstants.MULTIPLE_MODE</code>
	 * <li>
	 * </ul>
	 * 
	 * @param mode
	 *            either the single data set or multiple data set.
	 * 
	 * @throws SemanticException
	 *             if the input value is not one of above values.
	 * 
	 */

	public void setDataSetMode( String mode ) throws SemanticException
	{
		setStringProperty( DATA_SET_MODE_PROP, mode );
	}

	/**
	 * Gets the mode for data set support.
	 * 
	 * @return the mode for data set support.
	 * 
	 * @see #setDataSetMode(String)
	 * 
	 */

	public String getDataSetMode( )
	{
		return getStringProperty( DATA_SET_MODE_PROP );
	}

	/**
	 * Set the value for the prompt text key.
	 * 
	 * @param promptIDValue
	 *            The prompt text key.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setPromptTextKey( String promptIDValue )
			throws SemanticException
	{
		setStringProperty( PROMPT_TEXT_ID_PROP, promptIDValue );
	}

	/**
	 * Returns the prompt text key.
	 * 
	 * @return the prompt text key.
	 * 
	 */

	public String getPromptTextKey( )
	{
		return getStringProperty( PROMPT_TEXT_ID_PROP );
	}

	/**
	 * Returns the localized text for prompt text. If the localized text for the
	 * text resource key is found, it will be returned. Otherwise, the static
	 * text will be returned.
	 * 
	 * @return the localized text for the prompt text
	 */

	public String getDisplayPromptText( )
	{
		return getExternalizedValue( PROMPT_TEXT_ID_PROP, PROMPT_TEXT_PROP );
	}
}

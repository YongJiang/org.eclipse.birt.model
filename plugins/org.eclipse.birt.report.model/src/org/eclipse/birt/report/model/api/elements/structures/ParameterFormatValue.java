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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;

/**
 * Represents the format for the default value of scalar parameter.
 * 
 */

public class ParameterFormatValue extends FormatValue
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String FORMAT_VALUE_STRUCT = "ParameterFormatValue"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return FORMAT_VALUE_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		assert false;
		return null;
	}

	/**
	 * Return an <code>FormatValueHandle</code> to deal with the parameter
	 * format.
	 * 
	 * @param valueHandle
	 *            the property or member handle
	 * @return the structure handle
	 * 
	 */

	public StructureHandle getHandle( SimpleValueHandle valueHandle )
	{
		return new FormatValueHandle( valueHandle.getElementHandle( ),
				getContext( ) );
	}
}

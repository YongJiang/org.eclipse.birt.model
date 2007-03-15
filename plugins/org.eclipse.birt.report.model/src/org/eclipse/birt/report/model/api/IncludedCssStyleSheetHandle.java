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

import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;

/**
 * Represents a included css style sheet in report design and theme.
 *
 */

public class IncludedCssStyleSheetHandle  extends StructureHandle
{
	/**
	 * Constructs the handle of the included css style sheet.
	 * 
	 * @param valueHandle
	 *            the value handle for the included css style sheet list of one property
	 * @param index
	 *            the position of this included css style sheet in the list
	 */

	public IncludedCssStyleSheetHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}
    
	/**
	 * Gets the file name of the include css style sheet.
	 * 
	 * @return the file name of the include css style sheet
	 */

	public String getFileName( )
	{
        return getStringProperty( IncludedCssStyleSheet.FILE_NAME_MEMBER );
	}

}

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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for Level element to store the constants on it.
 */

public interface ILevelModel
{

	/**
	 * Name of the property that list of attributes defined for this level
	 * element. Each attribute inside is a string which refer to a dataset
	 * column name.
	 */

	String ATTRIBUTES_PROP = "attributes"; //$NON-NLS-1$

	/**
	 * Name of the property that refers a column name in the dataset.
	 */

	String COLUMN_NAME_PROP = "columnName"; //$NON-NLS-1$

	/**
	 * Name of the property that is used to create static group based on the
	 * rules define in this property.
	 */
	String STATIC_VALUES_PROP = "staticValues"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the interval base of search.
	 */
	String INTERVAL_BASE_PROP = "intervalBase"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the interval range of search.
	 */
	String INTERVAL_RANGE_PROP = "intervalRange"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the interval of search.
	 */
	String INTERVAL_PROP = "interval"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether this level is static, dynamic
	 * or mirrored.
	 */
	String LEVEL_TYPE_PROP = "levelType"; //$NON-NLS-1$
}

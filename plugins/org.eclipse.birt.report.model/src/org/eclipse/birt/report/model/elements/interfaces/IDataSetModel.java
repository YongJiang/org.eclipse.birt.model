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
 * The interface for DataSet to store the constants.
 */
public interface IDataSetModel
{

	/**
	 * The property name of the structures of the expected result set.
	 */

	public static final String RESULT_SET_PROP = "resultSet"; //$NON-NLS-1$

	/**
	 * The property name of the columns computed with expression.
	 */

	public static final String COMPUTED_COLUMNS_PROP = "computedColumns"; //$NON-NLS-1$

	/**
	 * The property name of the column hint elements.
	 */

	public static final String COLUMN_HINTS_PROP = "columnHints"; //$NON-NLS-1$

	/**
	 * The property name of the filters to apply to the data set.
	 */

	public static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameters definitions.
	 */

	public static final String PARAMETERS_PROP = "parameters"; //$NON-NLS-1$

}

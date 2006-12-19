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
 * The interface for Hierarchy element to store the constants on it.
 */

public interface IHierarchyModel
{

	/**
	 * Name of the property that defines its own dataset for a hierarchy. It can
	 * be used by the sub levels.
	 */

	static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$

	/**
	 * Name of the property that refers a list of column name from the dataset
	 * defined in this hierarchy.
	 */

	static final String PRIMARY_KEYS_PROP = "primaryKeys"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds all the level elements.
	 */

	static final int LEVEL_SLOT = 0;
}
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

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.olap.Dimension;

/**
 * Represents the factory to produce module name space according to different
 * name space ID.
 */

public class NameContextFactory
{

	private NameContextFactory( )
	{
		// Doesn't allow instantiation.
	}

	/**
	 * Returns the module name space with the attached module and name space ID.
	 * 
	 * @param module
	 *            the attached module
	 * @param nameSpaceID
	 *            the name space ID. The different module name space for
	 *            different name space ID.
	 * @return the produced element name space
	 */

	public static INameContext createModuleNameContext( Module module,
			int nameSpaceID )
	{
		if ( nameSpaceID == Module.STYLE_NAME_SPACE )
			return new StyleNameContext( module );

		return new SimpleModuleNameContext( module, nameSpaceID );
	}

	/**
	 * 
	 * @param dimension
	 * @param nameSpaceID
	 * @return
	 */
	public static INameContext createDimensionNameContext( Dimension dimension,
			int nameSpaceID )
	{
		if ( nameSpaceID == Dimension.LEVEL_NAME_SPACE )
			return new DimensionNameContext( dimension );
		return null;
	}
}

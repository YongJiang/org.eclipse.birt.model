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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the function to cache values in the report design.
 */

public class ReportDesignCacheTest extends BaseTestCase
{

	/**
	 * If any activity stack operation invovles, clears the flag for caching.
	 * 
	 * @throws Exception
	 */

	public void testClearCacheFlag( ) throws Exception
	{
		createDesign( );
		designHandle.cacheValues( );
		assertTrue( design.isCached( ) );

		LabelHandle label = designHandle.getElementFactory( ).newLabel(
				"newLabel" ); //$NON-NLS-1$

		designHandle.getBody( ).add( label );
		assertFalse( design.isCached( ) );

		designHandle.cacheValues( );
		assertTrue( design.isCached( ) );

		label.setText( "abc" );//$NON-NLS-1$
		assertFalse( design.isCached( ) );
	}

	/**
	 * If cached, the style properties can be retrieved correctly.
	 * 
	 * @throws Exception
	 */

	public void testCacheStyles( ) throws Exception
	{
		createDesign( );

		StyleHandle tmpStyle = designHandle.getElementFactory( ).newStyle(
				"style1" ); //$NON-NLS-1$
		designHandle.getStyles( ).add( tmpStyle );
		tmpStyle.setProperty( StyleHandle.COLOR_PROP, ColorPropertyType.RED );

		LabelHandle label = designHandle.getElementFactory( ).newLabel(
				"newLabel" ); //$NON-NLS-1$
		label.setStyle( (SharedStyleHandle) tmpStyle );
		designHandle.getBody( ).add( label );

		designHandle.cacheValues( );
		assertTrue( design.isCached( ) );

		assertEquals( ColorPropertyType.RED, label
				.getStringProperty( StyleHandle.COLOR_PROP ) );

	}
}

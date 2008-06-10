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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Class to test VariableElement cases.
 */
public class VariableElementParseTest extends BaseTestCase
{

	/**
	 * The extension element that uses variable element.
	 */

	private static final String FILE_NAME = "VariableElementParseTest.xml"; //$NON-NLS-1$

	/**
	 * Tests to get values for variable element.
	 * 
	 * @throws Exception
	 */

	public void testParse( ) throws Exception
	{
		openDesign( FILE_NAME );

		ExtendedItemHandle action1 = (ExtendedItemHandle) designHandle
				.findElement( "action1" ); //$NON-NLS-1$

		List variables = action1.getListProperty( "variables" ); //$NON-NLS-1$
		VariableElementHandle var1 = (VariableElementHandle) variables.get( 0 );

		assertEquals( "variable1", var1.getVariableName( ) ); //$NON-NLS-1$
		assertEquals( "expression for variable", var1.getValue( ) ); //$NON-NLS-1$

		var1.setVariableName( "new variable1" ); //$NON-NLS-1$
		var1.setValue( "new expression for variable" ); //$NON-NLS-1$

		save( );

		assertTrue( compareFile( "VariableElementParseTest_golden.xml" ) ); //$NON-NLS-1$
	}
}
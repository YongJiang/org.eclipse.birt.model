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

package org.eclipse.birt.report.model.library;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.JoinConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for use library joint data set in the report design.
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * 
 * <tr>
 * <td>{@link #testGetJointConditionDataSet()}</td>
 * <td>Report uses a joint data set from library.</td>
 * <td>Values from getLeftDataSet/getRightDataSet are with library namespace.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class LibraryJointDataSetTest extends BaseTestCase
{

	/**
	 * Tests JointDataSetHandle.getLeftDataSet/getLeftDataSet
	 * 
	 * @throws Exception
	 */

	public void testGetJointConditionDataSet( ) throws Exception
	{
		openDesign( "DesignIncludeJointDataSet.xml" ); //$NON-NLS-1$

		JointDataSetHandle dataSet = designHandle.findJointDataSet( "Data Set" ); //$NON-NLS-1$
		Iterator conditions = dataSet.joinConditionsIterator( );

		JoinConditionHandle cond = (JoinConditionHandle) conditions.next( );
		assertEquals( "new_library.Rev", cond.getLeftDataSet( ) ); //$NON-NLS-1$
		assertEquals( "new_library.HistUnitsSales", cond.getRightDataSet( ) ); //$NON-NLS-1$
	}
}

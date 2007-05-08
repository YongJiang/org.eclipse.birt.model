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

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.TabularLevel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the name helper.
 */
public class NameHelperTest extends BaseTestCase
{

	private static final String FILE_NAME = "NameHelperTest.xml"; //$NON-NLS-1$

	/**
	 * 
	 * @throws Exception
	 */
	public void testParser( ) throws Exception
	{
		openDesign( FILE_NAME );
		INameHelper nameHelper = design.getNameHelper( );

		// find element with unqiue name in general name scope,such as dimension
		Cube cube = (Cube) nameHelper.getNameSpace( Module.CUBE_NAME_SPACE )
				.getElement( "testCube" ); //$NON-NLS-1$
		assertNotNull( cube );

		Dimension dimension = (Dimension) nameHelper.getNameSpace(
				Module.CUBE_NAME_SPACE ).getElement( "testDimension" ); //$NON-NLS-1$
		assertNotNull( dimension );

		// find element with unique name in general name scope, such as level
		Level level = (Level) dimension.getNameHelper( ).getNameSpace(
				Dimension.LEVEL_NAME_SPACE ).getElement( "testLevel" ); //$NON-NLS-1$
		assertNotNull( level );
		assertNull( nameHelper.getNameSpace( Module.CUBE_NAME_SPACE )
				.getElement( level.getName( ) ) );

		// test name count
		assertEquals( 2, dimension.getNameHelper( ).getNameSpace(
				Dimension.LEVEL_NAME_SPACE ).getCount( ) );
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testMakeUniqueName( ) throws Exception
	{
		openDesign( FILE_NAME );

		// generate a unique name for level if it has not
		Dimension dimension = (Dimension) design
				.findOLAPElement( "testDimension" ); //$NON-NLS-1$
		Level level = new TabularLevel( null );
		dimension.makeUniqueName( level );
		assertNotNull( level.getName( ) );

		// if name exists, then make a new name
		level = new TabularLevel( "testLevel" ); //$NON-NLS-1$
		dimension.makeUniqueName( level );
		assertEquals( "testLevel1", level.getName( ) ); //$NON-NLS-1$

		// if call module.makeUniqueName, if level can not find the container
		// dimension, then do nothing
		level = new TabularLevel( null );
		design.makeUniqueName( level );
		assertNull( level.getName( ) );

		// if level can get dimension container, then the name will be not null;
		DesignElement hierarchy = design.findOLAPElement( "testHierarchy" ); //$NON-NLS-1$
		level = new TabularLevel( null );
		hierarchy.add( design, level, IHierarchyModel.LEVELS_PROP );
		design.makeUniqueName( level );
		assertNotNull( level.getName( ) );
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testResolve( ) throws Exception
	{
		openDesign( FILE_NAME );

		String propName = "level"; //$NON-NLS-1$
		DesignElementHandle testExtended = designHandle
				.findElement( "testTable" ); //$NON-NLS-1$
		ElementRefValue refValue = (ElementRefValue) testExtended.getElement( )
				.getProperty( design, propName );
		assertTrue( refValue.isResolved( ) );
		assertEquals( refValue, design.getNameHelper( ).resolve(
				refValue.getName( ),
				(PropertyDefn) testExtended.getPropertyDefn( propName ), null ) );
		Dimension dimension = (Dimension) design
				.findOLAPElement( "testDimension" ); //$NON-NLS-1$
		assertEquals( refValue, dimension.getNameHelper( ).resolve(
				refValue.getName( ),
				(PropertyDefn) testExtended.getPropertyDefn( propName ), null ) );
	}
}

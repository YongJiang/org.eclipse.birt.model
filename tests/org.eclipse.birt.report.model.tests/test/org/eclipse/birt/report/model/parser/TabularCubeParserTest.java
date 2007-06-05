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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.AccessControlHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.ValueAccessControlHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IAccessControlModel;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IValueAccessControlModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the cube element.
 */

public class TabularCubeParserTest extends BaseTestCase
{

	private final String FILE_NAME = "CubeParserTest.xml"; //$NON-NLS-1$
	private final String FILE_NAME_EXTENDS = "CubeParserTest_3.xml"; //$NON-NLS-1$

	/**
	 * 
	 * @throws Exception
	 */

	public void testParser( ) throws Exception
	{
		openDesign( FILE_NAME );
		assertNotNull( designHandle );

		// cube
		TabularCubeHandle cube = (TabularCubeHandle) designHandle
				.findCube( "testCube" ); //$NON-NLS-1$
		assertEquals(
				designHandle.findDataSet( "firstDataSet" ), cube.getDataSet( ) ); //$NON-NLS-1$
		// filter
		Iterator iter = cube.filtersIterator( );
		FilterConditionHandle filterConditionHandle = (FilterConditionHandle) iter
				.next( );
		assertEquals( "filter expression", filterConditionHandle.getExpr( ) ); //$NON-NLS-1$
		// join condition
		iter = cube.joinConditionsIterator( );
		DimensionConditionHandle cubeJoinConditionHandle = (DimensionConditionHandle) iter
				.next( );
		assertEquals(
				design.findOLAPElement( "testHierarchy" ), cubeJoinConditionHandle.getHierarchy( ).getElement( ) ); //$NON-NLS-1$		
		MemberHandle conditionMemberHandle = cubeJoinConditionHandle
				.getJoinConditions( );
		assertEquals( 3, conditionMemberHandle.getListValue( ).size( ) );
		DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionMemberHandle
				.getAt( 0 );
		assertEquals( "cubeKey", joinCondition.getCubeKey( ) ); //$NON-NLS-1$
		assertEquals( "key", joinCondition.getHierarchyKey( ) ); //$NON-NLS-1$
		joinCondition = (DimensionJoinConditionHandle) conditionMemberHandle
				.getAt( 1 );
		assertEquals( "cubeKey2", joinCondition.getCubeKey( ) ); //$NON-NLS-1$
		assertEquals( "key2", joinCondition.getHierarchyKey( ) ); //$NON-NLS-1$
		joinCondition = (DimensionJoinConditionHandle) conditionMemberHandle
				.getAt( 2 );
		assertEquals( "cubeKey4", joinCondition.getCubeKey( ) ); //$NON-NLS-1$
		assertEquals( "key4", joinCondition.getHierarchyKey( ) ); //$NON-NLS-1$
		cubeJoinConditionHandle = (DimensionConditionHandle) iter.next( );
		assertNull( cubeJoinConditionHandle.getHierarchy( ) );
		assertEquals(
				"nonExistingHierarchy", cubeJoinConditionHandle.getHierarchyName( ) ); //$NON-NLS-1$
		conditionMemberHandle = cubeJoinConditionHandle.getJoinConditions( );
		assertEquals( 1, conditionMemberHandle.getListValue( ).size( ) );

		// access controls

		// access controls on cube.

		Iterator iter1 = cube.accessControlsIterator( );
		AccessControlHandle accessControl = (AccessControlHandle) iter1.next( );

		assertEquals( "cube user1; cube user2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.USER_NAMES_PROP )
				.getStringValue( ) );
		assertEquals( "cube role1; cube role2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.ROLES_PROP )
				.getStringValue( ) );
		assertEquals( DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW,
				accessControl.getPermission( ) );

		PropertyHandle propHandle = cube
				.getPropertyHandle( TabularCubeHandle.DIMENSIONS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, cube
				.getContentCount( TabularCubeHandle.DIMENSIONS_PROP ) );

		// dimension
		DimensionHandle dimension = (DimensionHandle) propHandle.getContent( 0 );
		assertEquals( dimension, cube.getContent(
				TabularCubeHandle.DIMENSIONS_PROP, 0 ) );
		assertEquals( "testDimension", dimension.getName( ) ); //$NON-NLS-1$
		assertTrue( dimension.isTimeType( ) );
		propHandle = dimension
				.getPropertyHandle( DimensionHandle.HIERARCHIES_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, dimension
				.getContentCount( DimensionHandle.HIERARCHIES_PROP ) );

		// hierarchy
		TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) propHandle
				.getContent( 0 );
		assertEquals( hierarchy, dimension.getContent(
				DimensionHandle.HIERARCHIES_PROP, 0 ) );
		// test getDefaultHierarchy in dimension
		assertEquals( hierarchy, dimension.getDefaultHierarchy( ) );
		assertEquals( "testHierarchy", hierarchy.getName( ) ); //$NON-NLS-1$
		assertEquals(
				designHandle.findDataSet( "secondDataSet" ), hierarchy.getDataSet( ) ); //$NON-NLS-1$

		// access controls on hierarchy.

		iter1 = hierarchy.accessControlsIterator( );
		accessControl = (AccessControlHandle) iter1.next( );

		assertEquals( "hierarchy user1; hierarchy user2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.USER_NAMES_PROP )
				.getStringValue( ) );
		assertEquals( "hierarchy role1; hierarchy role2", accessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.ROLES_PROP )
				.getStringValue( ) );
		assertEquals( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW,
				accessControl.getPermission( ) );

		propHandle = cube.getPropertyHandle( TabularCubeHandle.DIMENSIONS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, cube
				.getContentCount( TabularCubeHandle.DIMENSIONS_PROP ) );

		// filter
		iter = hierarchy.filtersIterator( );
		filterConditionHandle = (FilterConditionHandle) iter.next( );
		assertEquals( "filter expression", filterConditionHandle.getExpr( ) ); //$NON-NLS-1$
		List primaryKeys = hierarchy.getPrimaryKeys( );
		assertEquals( 3, primaryKeys.size( ) );
		assertEquals( "key", primaryKeys.get( 0 ) ); //$NON-NLS-1$
		assertEquals( "key2", primaryKeys.get( 1 ) ); //$NON-NLS-1$
		assertEquals( "key4", primaryKeys.get( 2 ) ); //$NON-NLS-1$
		propHandle = hierarchy
				.getPropertyHandle( TabularHierarchyHandle.LEVELS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, hierarchy
				.getContentCount( TabularHierarchyHandle.LEVELS_PROP ) );

		// level
		TabularLevelHandle level = (TabularLevelHandle) propHandle
				.getContent( 0 );
		assertEquals( level, hierarchy.getContent(
				TabularHierarchyHandle.LEVELS_PROP, 0 ) );
		assertEquals( "testLevel", level.getName( ) ); //$NON-NLS-1$
		assertEquals( "column1", level.getColumnName( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, level
				.getDataType( ) );
		assertEquals( "none", level.getInterval( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH, level
				.getDateTimeLevelType( ) );
		assertEquals( 3.0, level.getIntervalRange( ), 0.00 );
		assertEquals( "Jan", level.getIntervalBase( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC, level
				.getLevelType( ) );
		iter = level.staticValuesIterator( );
		RuleHandle rule = (RuleHandle) iter.next( );
		assertEquals( "rule expression", rule.getRuleExpression( ) ); //$NON-NLS-1$
		assertEquals( "display expression", rule.getDisplayExpression( ) ); //$NON-NLS-1$
		rule = (RuleHandle) iter.next( );
		assertEquals( "rule expression2", rule.getRuleExpression( ) ); //$NON-NLS-1$
		assertEquals( "display expression2", rule.getDisplayExpression( ) ); //$NON-NLS-1$
		iter = level.attributesIterator( );

		LevelAttributeHandle attribute = (LevelAttributeHandle) iter.next( );
		assertEquals( "var1", attribute.getName( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, attribute
				.getDataType( ) );
		attribute = (LevelAttributeHandle) iter.next( );
		assertEquals( "var2", attribute.getName( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, attribute
				.getDataType( ) );

		// access controls on level.

		iter1 = level.valueAccessControlsIterator( );
		ValueAccessControlHandle valueAccessControl = (ValueAccessControlHandle) iter1
				.next( );

		assertEquals( "level user1; level user2", valueAccessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.USER_NAMES_PROP )
				.getStringValue( ) );
		assertEquals( "level role1; level role2", valueAccessControl //$NON-NLS-1$
				.getPropertyHandle( IAccessControlModel.ROLES_PROP )
				.getStringValue( ) );
		assertEquals( "level value1; level value2", valueAccessControl //$NON-NLS-1$
				.getPropertyHandle( IValueAccessControlModel.VALUES_PROP )
				.getStringValue( ) );
		assertEquals( DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW,
				valueAccessControl.getPermission( ) );

		// measure group
		propHandle = cube
				.getPropertyHandle( TabularCubeHandle.MEASURE_GROUPS_PROP );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( 1, cube
				.getContentCount( TabularCubeHandle.MEASURE_GROUPS_PROP ) );
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) propHandle
				.getContent( 0 );
		assertEquals( measureGroup, cube.getContent(
				TabularCubeHandle.MEASURE_GROUPS_PROP, 0 ) );
		assertEquals( "testMeasureGroup", measureGroup.getName( ) ); //$NON-NLS-1$
		// test getDefaultMeasureGroup in cube
		assertEquals( measureGroup, cube.getDefaultMeasureGroup( ) );
		propHandle = measureGroup
				.getPropertyHandle( MeasureGroupHandle.MEASURES_PROP );

		// measure
		MeasureHandle measure = (MeasureHandle) propHandle.getContent( 0 );
		assertEquals( "testMeasure", measure.getName( ) ); //$NON-NLS-1$
		assertEquals( "column", measure.getMeasureExpression( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.MEASURE_FUNCTION_MIN, measure
				.getFunction( ) );
		assertFalse( measure.isCalculated( ) );
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, measure
				.getDataType( ) );

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testWriter( ) throws Exception
	{
		openDesign( FILE_NAME );
		assertNotNull( designHandle );
		String namePrix = "new"; //$NON-NLS-1$
		String valuePrix = "updated "; //$NON-NLS-1$
		ElementFactory factory = designHandle.getElementFactory( );

		// cube
		TabularCubeHandle cube = (TabularCubeHandle) designHandle
				.findCube( "testCube" ); //$NON-NLS-1$
		cube.setDataSet( designHandle.findDataSet( "secondDataSet" ) ); //$NON-NLS-1$
		cube.setName( namePrix + cube.getName( ) );
		cube.setDefaultMeasureGroup( factory
				.newTabularMeasureGroup( "testDefaultMeasureGroup" ) ); //$NON-NLS-1$
		PropertyHandle propHandle = cube
				.getPropertyHandle( TabularCubeHandle.DIMENSION_CONDITIONS_PROP );
		propHandle.removeItem( 1 );
		DimensionCondition condition = new DimensionCondition( );
		DimensionConditionHandle structHandle = cube
				.addDimensionCondition( condition );
		DimensionJoinConditionHandle joinConditionHandle = structHandle
				.addJoinCondition( new DimensionJoinCondition( ) );
		joinConditionHandle.setCubeKey( "addCubeKey" ); //$NON-NLS-1$
		joinConditionHandle.setHierarchyKey( "addHierarchyKey" ); //$NON-NLS-1$
		structHandle.setLevel( "testDimension/noLevel" ); //$NON-NLS-1$
		structHandle = (DimensionConditionHandle) propHandle.get( 0 );
		MemberHandle memberHandle = structHandle
				.getMember( DimensionCondition.HIERARCHY_MEMBER );
		memberHandle.setValue( valuePrix + "hierarchy" ); //$NON-NLS-1$
		structHandle.removeJoinCondition( 1 );

		// access controls on cube.

		AccessControlHandle accessControl = (AccessControlHandle) cube
				.accessControlsIterator( ).next( );

		accessControl.addUserName( "new cube user1" ); //$NON-NLS-1$
		accessControl.addUserName( "new cube user2" ); //$NON-NLS-1$

		accessControl.addRole( "new cube role1" ); //$NON-NLS-1$
		accessControl.addRole( "new cube role2" ); //$NON-NLS-1$

		accessControl
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );

		// add a new access control

		propHandle = cube
				.getPropertyHandle( TabularCubeHandle.ACCESS_CONTROLS_PROP );

		accessControl = designHandle.getElementFactory( ).newAccessControl( );
		propHandle.add( accessControl );

		// dimension
		cube.add( TabularCubeHandle.DIMENSIONS_PROP, factory
				.newTabularDimension( null ) );
		DimensionHandle dimension = (DimensionHandle) cube.getContent(
				TabularCubeHandle.DIMENSIONS_PROP, 0 );
		dimension.setName( namePrix + dimension.getName( ) );
		dimension.setTimeType( false );
		dimension.setDefaultHierarchy( factory
				.newTabularHierarchy( "testDefaultHierarchy" ) ); //$NON-NLS-1$

		// hierarchy
		dimension.add( DimensionHandle.HIERARCHIES_PROP, factory
				.newTabularHierarchy( null ) );
		TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
				.getContent( DimensionHandle.HIERARCHIES_PROP, 0 );
		hierarchy.setName( namePrix + hierarchy.getName( ) );
		hierarchy.setDataSet( designHandle.findDataSet( "firstDataSet" ) ); //$NON-NLS-1$
		propHandle = hierarchy
				.getPropertyHandle( TabularHierarchyHandle.PRIMARY_KEYS_PROP );
		propHandle.removeItem( "key2" ); //$NON-NLS-1$
		propHandle.addItem( valuePrix + "key" ); //$NON-NLS-1$

		// access controls on hierarchy.

		accessControl = (AccessControlHandle) hierarchy
				.accessControlsIterator( ).next( );

		accessControl.addUserName( "new hierarchy user1" ); //$NON-NLS-1$
		accessControl.addUserName( "new hierarchy user2" ); //$NON-NLS-1$

		accessControl.addRole( "new hierarchy role1" ); //$NON-NLS-1$
		accessControl.addRole( "new hierarchy role2" ); //$NON-NLS-1$

		accessControl
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW );

		// add a new access control

		propHandle = hierarchy
				.getPropertyHandle( TabularHierarchyHandle.ACCESS_CONTROLS_PROP );

		accessControl = designHandle.getElementFactory( ).newAccessControl( );
		propHandle.add( accessControl );

		// level
		hierarchy.add( TabularHierarchyHandle.LEVELS_PROP, factory
				.newTabularLevel( dimension, null ) );
		TabularLevelHandle level = (TabularLevelHandle) hierarchy.getContent(
				TabularHierarchyHandle.LEVELS_PROP, 0 );
		level.setName( namePrix + level.getName( ) );
		level.setColumnName( valuePrix + level.getColumnName( ) );
		level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING );
		level
				.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER );
		// level.setInterval( DesignChoiceConstants.INTERVAL_MONTH );
		level.setIntervalRange( 5 );
		level.setIntervalBase( valuePrix + level.getIntervalBase( ) );
		level.setLevelType( DesignChoiceConstants.LEVEL_TYPE_MIRRORED );
		propHandle = level.getPropertyHandle( LevelHandle.STATIC_VALUES_PROP );
		propHandle.removeItem( 0 );
		Rule rule = new Rule( );
		rule.setProperty( Rule.DISPLAY_EXPRE_MEMBER, "new display expression" ); //$NON-NLS-1$
		rule.setProperty( Rule.RULE_EXPRE_MEMBER, "new rule expression" ); //$NON-NLS-1$
		propHandle.insertItem( rule, 0 );
		propHandle = level.getPropertyHandle( LevelHandle.ATTRIBUTES_PROP );
		propHandle.removeItem( propHandle.get( 1 ) );

		LevelAttribute config = new LevelAttribute( );
		config.setName( "var3" ); //$NON-NLS-1$
		config.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN );
		propHandle.insertItem( config, 0 );

		// access controls on hierarchy.

		ValueAccessControlHandle valueAccess = (ValueAccessControlHandle) level
				.valueAccessControlsIterator( ).next( );

		valueAccess.addUserName( "new level user1" ); //$NON-NLS-1$
		valueAccess.addUserName( "new level user2" ); //$NON-NLS-1$

		valueAccess.addRole( "new level role1" ); //$NON-NLS-1$
		valueAccess.addRole( "new level role2" ); //$NON-NLS-1$

		valueAccess.addValue( "new level value1" ); //$NON-NLS-1$
		valueAccess.addValue( "new level value2" ); //$NON-NLS-1$

		valueAccess
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );

		// add a new value access control

		propHandle = level
				.getPropertyHandle( LevelHandle.VALUE_ACCESS_CONTROLS_PROP );

		valueAccess = designHandle.getElementFactory( ).newValueAccessControl( );
		propHandle.add( valueAccess );

		// measure group
		cube.add( TabularCubeHandle.MEASURE_GROUPS_PROP, factory
				.newTabularMeasureGroup( null ) );
		MeasureGroupHandle measureGroup = (MeasureGroupHandle) cube.getContent(
				TabularCubeHandle.MEASURE_GROUPS_PROP, 0 );
		measureGroup.setName( namePrix + measureGroup.getName( ) );

		// measure
		measureGroup.add( MeasureGroupHandle.MEASURES_PROP, factory
				.newTabularMeasure( null ) );
		MeasureHandle measure = (MeasureHandle) measureGroup.getContent(
				MeasureGroupHandle.MEASURES_PROP, 0 );
		measure.setName( namePrix + measure.getName( ) );
		measure.setMeasureExpression( valuePrix
				+ measure.getMeasureExpression( ) );
		measure.setFunction( DesignChoiceConstants.MEASURE_FUNCTION_COUNT );
		measure.setCalculated( true );
		measure.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN );

		save( );
		assertTrue( compareFile( "CubeParserTest_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testCommand( ) throws Exception
	{
		openDesign( FILE_NAME );
		assertNotNull( designHandle );

		// cube
		TabularCubeHandle cube = (TabularCubeHandle) designHandle
				.findCube( "testCube" ); //$NON-NLS-1$
		try
		{
			cube.setName( null );
			fail( );
		}
		catch ( NameException e )
		{

		}

		IDesignElement clonedCube = cube.copy( );
		assertNotNull( clonedCube );
		designHandle.rename( clonedCube.getHandle( design ) );
		designHandle.getSlot( IReportDesignModel.CUBE_SLOT ).add(
				clonedCube.getHandle( design ) );

		// save

		save( );
		assertTrue( compareFile( "CubeParserTest_golden_1.xml" ) ); //$NON-NLS-1$		
	}

	/**
	 * Tests cases for cube1 extends another cube2.
	 * 
	 * <ul>
	 * <li>if one new access control is added to the cube1. Access controls on
	 * cube2 will be copies to cube1 first.
	 * <li>if one new value access control is added to the level in the cube1.
	 * Value access controls on the level cube2 will be copies to the level of
	 * cube1 first.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testExtends( ) throws Exception
	{
		openDesign( FILE_NAME_EXTENDS );
		assertNotNull( designHandle );

		// cube

		libraryHandle = designHandle.getLibrary( "Lib1" ); //$NON-NLS-1$
		TabularCubeHandle cube = (TabularCubeHandle) libraryHandle
				.findCube( "testCube" ); //$NON-NLS-1$

		TabularCubeHandle newCube = (TabularCubeHandle) designHandle
				.getElementFactory( ).newElementFrom( cube, null );

		designHandle.getCubes( ).add( newCube );

		save( );
		assertTrue( compareFile( "TabularCubeParserTest_extends_golden_1.xml" ) ); //$NON-NLS-1$

		AccessControlHandle control = designHandle.getElementFactory( )
				.newAccessControl( );
		control.addUserName( "new user name 1" ); //$NON-NLS-1$

		newCube.add( ICubeModel.ACCESS_CONTROLS_PROP, control );

		save( );
		assertTrue( compareFile( "TabularCubeParserTest_extends_golden_2.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		LevelHandle level = designHandle.findLevel( "testDimension1/testLevel" ); //$NON-NLS-1$

		ValueAccessControlHandle valueAccess = designHandle.getElementFactory( )
				.newValueAccessControl( );
		valueAccess.addValue( "new value 1" ); //$NON-NLS-1$

		level.add( ILevelModel.VALUE_ACCESS_CONTROLS_PROP, valueAccess );

		save( );
		assertTrue( compareFile( "TabularCubeParserTest_extends_golden_3.xml" ) ); //$NON-NLS-1$

		valueAccess
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );
	}

	/**
	 * Notification should be only sent to cube if access control/value access
	 * controls are changed. It should be only one PROPERTY event.
	 * 
	 * <ul>
	 * <li>for local value change.
	 * <li>for extends case.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testNotification( ) throws Exception
	{
		openDesign( "CubeParserTest_2.xml" ); //$NON-NLS-1$

		// cube
		TabularCubeHandle cube = (TabularCubeHandle) designHandle
				.findCube( "testCube" ); //$NON-NLS-1$

		MyListener listener = new MyListener( );
		cube.addListener( listener );

		PropertyHandle propHandle = cube
				.getPropertyHandle( TabularCubeHandle.ACCESS_CONTROLS_PROP );

		// adds access control to the cube

		AccessControlHandle accessControl = designHandle.getElementFactory( )
				.newAccessControl( );
		propHandle.add( accessControl );

		// test add/remove items on list.

		accessControl.addUserName( "new cube user1" ); //$NON-NLS-1$
		checkNotificationStatus( listener );

		propHandle = accessControl
				.getPropertyHandle( IAccessControlModel.USER_NAMES_PROP );
		propHandle.addItem( "new cube user2" ); //$NON-NLS-1$
		checkNotificationStatus( listener );

		propHandle.removeItem( 1 );
		checkNotificationStatus( listener );

		assertEquals( "new cube user1", propHandle.getStringValue( ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );
		checkNotificationStatus( listener );

		assertEquals(
				"new cube user1; new cube user2", propHandle.getStringValue( ) ); //$NON-NLS-1$
		propHandle.removeItem( "new cube user1" ); //$NON-NLS-1$
		checkNotificationStatus( listener );

		assertEquals( "new cube user2", propHandle.getStringValue( ) ); //$NON-NLS-1$

		accessControl.addRole( "new cube role1" ); //$NON-NLS-1$

		checkNotificationStatus( listener );

		accessControl.removeRole( "new cube role1" ); //$NON-NLS-1$

		checkNotificationStatus( listener );

		// test setValue().

		accessControl
				.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );

		checkNotificationStatus( listener );

		// test add/remove elements on list.

		accessControl = designHandle.getElementFactory( ).newAccessControl( );
		cube.add( TabularCubeHandle.ACCESS_CONTROLS_PROP, accessControl );

		checkNotificationStatus( listener );

		propHandle = cube
				.getPropertyHandle( TabularCubeHandle.ACCESS_CONTROLS_PROP );
		propHandle.drop( 0 );

		checkNotificationStatus( listener );

		assertEquals( 1, cube.getListProperty(
				TabularCubeHandle.ACCESS_CONTROLS_PROP ).size( ) );

		// for extends cases.

		openDesign( FILE_NAME_EXTENDS );

		cube = (TabularCubeHandle) designHandle.findCube( "testCube1" ); //$NON-NLS-1$
		cube.addListener( listener );

		// set property on cube access control

		Iterator cubeAccess = cube.accessControlsIterator( );
		AccessControlHandle tmpAccess = (AccessControlHandle) cubeAccess.next( );

		tmpAccess.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );
		checkNotificationStatus( listener );
	}

	/**
	 * When cube1 extends another cube2. Get access control from cube1.
	 * 
	 * <ul>
	 * <li>set permission
	 * <li>add role
	 * <li>remove role
	 * </ul>
	 * 
	 * will copies access controls from cube2 first. Then change the
	 * corresponding value. Undo is also tested.
	 * 
	 * @throws Exception
	 */

	public void testContentElementCommandOnCube( ) throws Exception
	{
		openDesign( FILE_NAME_EXTENDS );

		// cube

		TabularCubeHandle cube = (TabularCubeHandle) designHandle
				.findCube( "testCube1" ); //$NON-NLS-1$

		// set property on cube access control

		Iterator cubeAccess = cube.accessControlsIterator( );
		AccessControlHandle tmpAccess = (AccessControlHandle) cubeAccess.next( );

		tmpAccess.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );

		save( );
		assertTrue( compareFile( "TabularCubeAccessControl_golden_1.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		save( );
		assertTrue( compareFile( "TabularCubeAccessControl_golden_2.xml" ) ); //$NON-NLS-1$

		tmpAccess.addRole( "new 2nd role" ); //$NON-NLS-1$

		save( );
		assertTrue( compareFile( "TabularCubeAccessControl_golden_3.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		tmpAccess.removeRole( "cube role1" ); //$NON-NLS-1$
		save( );
		assertTrue( compareFile( "TabularCubeAccessControl_golden_4.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );
		tmpAccess.removeRole( "cube role1" ); //$NON-NLS-1$

		tmpAccess.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );
		save( );

		assertTrue( compareFile( "TabularCubeAccessControl_golden_5.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * When cube1 extends another cube2. Get access control from cube1.
	 * 
	 * <ul>
	 * <li>set permission
	 * <li>add role
	 * <li>remove role
	 * </ul>
	 * 
	 * will copies value access controls from the level of cube2 first. Then
	 * change the corresponding value. Undo is also tested.
	 * 
	 * @throws Exception
	 */

	public void testContentElementCommandOnLevel( ) throws Exception
	{
		openDesign( FILE_NAME_EXTENDS );

		// cube

		TabularLevelHandle cube = (TabularLevelHandle) designHandle
				.findLevel( "testDimension/testLevel" ); //$NON-NLS-1$

		// set property on cube access control

		Iterator cubeAccess = cube.valueAccessControlsIterator( );
		ValueAccessControlHandle tmpAccess = (ValueAccessControlHandle) cubeAccess
				.next( );

		tmpAccess.setPermission( DesignChoiceConstants.ACCESS_PERMISSION_ALLOW );

		save( );
		assertTrue( compareFile( "TabularLevelAccessControl_golden_1.xml" ) );//$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		save( );
		assertTrue( compareFile( "TabularLevelAccessControl_golden_2.xml" ) );//$NON-NLS-1$

		tmpAccess.addRole( "new 2nd role" ); //$NON-NLS-1$

		save( );
		assertTrue( compareFile( "TabularLevelAccessControl_golden_3.xml" ) );//$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		tmpAccess.removeRole( "level role1" ); //$NON-NLS-1$
		save( );
		assertTrue( compareFile( "TabularLevelAccessControl_golden_4.xml" ) );//$NON-NLS-1$

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testMergeDimensionConditions( ) throws Exception
	{
		openDesign( "CubeParserTest_4.xml" ); //$NON-NLS-1$

		TabularCubeHandle cube = (TabularCubeHandle) designHandle
				.findCube( "testCube" ); //$NON-NLS-1$

		assertNotNull( cube.findDimensionCondition( "testHierarchy" ) ); //$NON-NLS-1$
		assertEquals(
				cube.findDimensionCondition( "testHierarchy" ).getStructure( ), cube.findDimensionCondition( (HierarchyHandle) design.findOLAPElement( "testHierarchy" ).getHandle( design ) ).getStructure( ) ); //$NON-NLS-1$//$NON-NLS-2$

		save( );
		assertTrue( compareFile( "CubeParserTest_golden_3.xml" ) ); //$NON-NLS-1$
	}

	private static void checkNotificationStatus( MyListener listener )
	{
		assertEquals( NotificationEvent.PROPERTY_EVENT, listener.getEventType( ) );
		assertEquals( TabularCubeHandle.ACCESS_CONTROLS_PROP, listener
				.getPropName( ) );

		listener.reset( );
	}

	private static class MyListener implements Listener
	{

		private String propName = null;
		private int eventType = -1;

		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			eventType = ev.getEventType( );

			assert eventType == NotificationEvent.PROPERTY_EVENT;

			propName = ( (PropertyEvent) ev ).getPropertyName( );
		}

		MyListener( )
		{

		}

		String getPropName( )
		{
			return propName;
		}

		int getEventType( )
		{
			return eventType;
		}

		void reset( )
		{
			propName = null;
			eventType = -1;
		}
	}
}

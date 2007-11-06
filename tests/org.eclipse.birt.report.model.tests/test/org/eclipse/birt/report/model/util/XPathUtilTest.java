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

package org.eclipse.birt.report.model.util;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;

/**
 * Test cases for XPathUtil.
 * 
 */

public class XPathUtilTest extends BaseTestCase
{

	private static final String INPUT_FILE = "XPathUtilTest.xml"; //$NON-NLS-1$

	/**
	 * Test cases to get XPath string from element, slot and structure.
	 * 
	 * @throws Exception
	 */

	public void testGetXPath( ) throws Exception
	{
		openDesign( INPUT_FILE );

		SlotHandle tmpSlot = designHandle.getBody( );
		assertEquals( "/report/body", XPathUtil.getXPath( tmpSlot ) ); //$NON-NLS-1$

		tmpSlot = designHandle.findElement( "My table1 nested label1" ) //$NON-NLS-1$
				.getContainerSlotHandle( );

		assertEquals(
				"/report/body/table[@id=\"10\"]/detail/row/cell[@id=\"17\"][@slotName=\"content\"]", //$NON-NLS-1$
				XPathUtil.getXPath( tmpSlot ) );

		ListHandle list = (ListHandle) designHandle.findElement( "My list1" ); //$NON-NLS-1$
		assertEquals( "/report/body/list[@id=\"21\"]/header", //$NON-NLS-1$
				XPathUtil.getXPath( list.getHeader( ) ) );

		PropertyHandle propHandle = designHandle
				.getPropertyHandle( IModuleModel.IMAGES_PROP );
		assertEquals( "/report/list-property[@name=\"images\"]", //$NON-NLS-1$
				XPathUtil.getXPath( propHandle ) );

		propHandle = designHandle.findParameter( "Param 1" ).getPropertyHandle( //$NON-NLS-1$
				DesignElement.NAME_PROP );
		assertEquals( "/report/parameters/scalar-parameter[@id=\"2\"]/@name", //$NON-NLS-1$
				XPathUtil.getXPath( propHandle ) );

		propHandle = designHandle.findElement( "My text1" ).getPropertyHandle( //$NON-NLS-1$
				ITextItemModel.CONTENT_PROP );
		assertEquals(
				"/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]", //$NON-NLS-1$
				XPathUtil.getXPath( propHandle ) );

		propHandle = designHandle.findElement( "My text1" ).getPropertyHandle( //$NON-NLS-1$
				ITextItemModel.CONTENT_RESOURCE_KEY_PROP );
		assertEquals(
				"/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]/@key", //$NON-NLS-1$
				XPathUtil.getXPath( propHandle ) );

		EmbeddedImageHandle image = (EmbeddedImageHandle) designHandle
				.imagesIterator( ).next( );

		assertEquals( "/report/list-property[@name=\"images\"]/structure[1]", //$NON-NLS-1$
				XPathUtil.getXPath( image ) );

		OdaDataSetHandle tmpDataSet = (OdaDataSetHandle) designHandle
				.findDataSet( "firstDataSet" ); //$NON-NLS-1$
		assertEquals(
				"/report/data-sets/oda-data-set", XPathUtil.getXPath( tmpDataSet ) ); //$NON-NLS-1$

		ExtendedItemHandle tmpMatrix = (ExtendedItemHandle) designHandle
				.findElement( "matrix1" ); //$NON-NLS-1$
		assertEquals( "/report/body/extended-item[@id=\"20\"]", XPathUtil //$NON-NLS-1$
				.getXPath( tmpMatrix ) );

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle
				.findDataSet( "firstDataSet" ); //$NON-NLS-1$
		Iterator iter1 = dataSet.getCachedMetaDataHandle( ).getResultSet( )
				.iterator( );
		ResultSetColumnHandle setColumn = (ResultSetColumnHandle) iter1.next( );
		assertEquals(
				"/report/data-sets/oda-data-set/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[1]", //$NON-NLS-1$
				XPathUtil.getXPath( setColumn ) );

		setColumn = (ResultSetColumnHandle) iter1.next( );
		assertEquals(
				"/report/data-sets/oda-data-set/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[2]", //$NON-NLS-1$
				XPathUtil.getXPath( setColumn ) );

		LabelHandle label1 = (LabelHandle) designHandle.getElementByID( 62l );
		propHandle = label1.getPropertyHandle( LabelHandle.ON_PREPARE_METHOD );
		String path = XPathUtil.getXPath( propHandle );
		assertEquals(
				"/report/body/label[@id=\"62\"]/method[@name=\"onPrepare\"]", //$NON-NLS-1$
				path );

//		propHandle = label1
//				.getPropertyHandle( StyleHandle.HIGHLIGHT_RULES_PROP );
//		StructureHandle structHandle = (StructureHandle) propHandle.get( 0 );
//		MemberHandle member = (MemberHandle) structHandle
//				.getMember( StyleRule.TEST_EXPR_MEMBER );
//		path = XPathUtil.getXPath( member );
//		assertEquals(
//				"/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/expression[@name=\"testExpr\"]", //$NON-NLS-1$
//				path );
//
//		member = (MemberHandle) structHandle
//				.getMember( StyleRule.VALUE1_MEMBER );
//		path = XPathUtil.getXPath( member, 0 );
//		assertEquals(
//				"/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/list-property[@name=\"value1\"]/value[1]", //$NON-NLS-1$
//				path );
//
//		LabelHandle label2 = (LabelHandle) designHandle.getElementByID( 63l );
//
//		TOCHandle tochandle = label2.getTOC( );
//		member = tochandle.getMember( TOC.TOC_EXPRESSION );
//
//		path = XPathUtil.getXPath( member );
//		assertEquals(
//				"/report/body/label[@id=\"63\"]/structure[@name=\"toc\"]/expression[@name=\"expressionValue\"]", //$NON-NLS-1$
//				path );
	}

	/**
	 * @throws Exception
	 */

	public void testGetInstance( ) throws Exception
	{
		openDesign( INPUT_FILE );

		Object retValue = XPathUtil.getInstance( designHandle, "/report" ); //$NON-NLS-1$
		assertTrue( retValue instanceof ReportDesignHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/parameters/scalar-parameter" ); //$NON-NLS-1$
		assertTrue( retValue instanceof ScalarParameterHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/body/image[@id=\"9\"]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof ImageHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/body/table/group/header/row" ); //$NON-NLS-1$
		assertTrue( retValue instanceof RowHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/body/table/group/header/row/" ); //$NON-NLS-1$
		assertTrue( retValue instanceof RowHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/body/table/group/header/row/*" ); //$NON-NLS-1$
		assertTrue( retValue instanceof RowHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/body/table/group/header/row[@slotName=\"cells\"]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof SlotHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/body/list[@id=\"21\"]/header]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof SlotHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/list-property[@name=\"images\"]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof PropertyHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/parameters/scalar-parameter[@id=\"2\"]/@name" ); //$NON-NLS-1$
		assertTrue( retValue instanceof PropertyHandle );
		assertEquals( DesignElement.NAME_PROP, ( (PropertyHandle) retValue )
				.getDefn( ).getName( ) );

		retValue = XPathUtil
				.getInstance( designHandle,
						"/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof PropertyHandle );
		assertEquals( ITextItemModel.CONTENT_PROP, ( (PropertyHandle) retValue )
				.getDefn( ).getName( ) );

		retValue = XPathUtil
				.getInstance( designHandle,
						"/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]/@key" ); //$NON-NLS-1$
		assertTrue( retValue instanceof PropertyHandle );
		assertEquals( ITextItemModel.CONTENT_RESOURCE_KEY_PROP,
				( (PropertyHandle) retValue ).getDefn( ).getName( ) );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/list-property[@name=\"images\"]/structure" ); //$NON-NLS-1$
		assertTrue( retValue instanceof EmbeddedImageHandle );

		retValue = XPathUtil.getInstance( designHandle,
				"/report/list-property[@name=\"images\"]/structure[1]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof EmbeddedImageHandle );

		retValue = XPathUtil
				.getInstance(
						designHandle,
						"/report/data-sets/oda-data-set/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[1]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof ResultSetColumnHandle );
		ResultSetColumnHandle column = (ResultSetColumnHandle) retValue;
		assertEquals( "name1", column.getColumnName( ) ); //$NON-NLS-1$
		assertTrue( 1 == column.getPosition( ).intValue( ) );

		retValue = XPathUtil
				.getInstance(
						designHandle,
						"/report/data-sets/oda-data-set/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[2]" ); //$NON-NLS-1$
		assertTrue( retValue instanceof ResultSetColumnHandle );
		column = (ResultSetColumnHandle) retValue;
		assertEquals( "date1", column.getColumnName( ) ); //$NON-NLS-1$
		assertTrue( 2 == column.getPosition( ).intValue( ) );

		// the instance for the data set.

		retValue = XPathUtil.getInstance( designHandle,
				"/report/data-sets/oda-data-set" ); //$NON-NLS-1$
		assertTrue( retValue instanceof OdaDataSetHandle );

		// invalid test cases. Must avoid NPE.

		assertNull( XPathUtil.getInstance( designHandle, "*/" ) ); //$NON-NLS-1$
		assertNull( XPathUtil.getInstance( designHandle, "/library" ) ); //$NON-NLS-1$
		assertNull( XPathUtil.getInstance( designHandle, "/report/*/body" ) ); //$NON-NLS-1$

		assertNull( XPathUtil.getInstance( designHandle,
				"/report/list-property[@name=\"images\"]/structure[3]" ) ); //$NON-NLS-1$

		PropertyHandle prop = (PropertyHandle) XPathUtil.getInstance(
				designHandle,
				"/report/body/label[@id=\"62\"]/method[@name=\"onPrepare\"]" ); //$NON-NLS-1$

		assertEquals( "\"prepare\"", prop.getValue( ) );//$NON-NLS-1$

//		MemberHandle member = (MemberHandle) XPathUtil
//				.getInstance(
//						designHandle,
//						"/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/expression[@name=\"testExpr\"]" ); //$NON-NLS-1$
//		assertEquals( "row[\"LASTNAME\"]", member.getValue( ) );//$NON-NLS-1$
//
//		member = (MemberHandle) XPathUtil
//				.getInstance(
//						designHandle,
//						"/report/body/label[@id=\"63\"]/structure[@name=\"toc\"]/expression[@name=\"expressionValue\"]" ); //$NON-NLS-1$
//		assertEquals( "toc expression", member.getValue( ) );//$NON-NLS-1$
//
//		String value = (String) XPathUtil
//				.getInstance(
//						designHandle,
//						"/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/list-property[@name=\"value1\"]/value[1]" ); //$NON-NLS-1$
//
//		assertEquals( "\"Tseng\"", value );//$NON-NLS-1$

	}
}

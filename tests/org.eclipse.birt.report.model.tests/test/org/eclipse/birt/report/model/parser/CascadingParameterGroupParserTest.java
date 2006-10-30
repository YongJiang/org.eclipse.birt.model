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

import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.util.BaseTestCase;

public class CascadingParameterGroupParserTest extends BaseTestCase
{

	private final static String INPUT = "CascadingParameterGroupParserTest.xml"; //$NON-NLS-1$
	private final static String INPUT_SEMANTIC_ERRORS = "CascadingParameterGroupParserTest_1.xml"; //$NON-NLS-1$
	private final static String OUTPUT = "CascadingParameterParserGroupTest_out.xml"; //$NON-NLS-1$
	private final static String GOLDEN = "CascadingParameterGroupParserTest_golden.xml"; //$NON-NLS-1$

	/**
	 * Tests get properties and get contents.
	 * 
	 * @throws DesignFileException
	 */

	public void testGetPropertiesAndContents( ) throws DesignFileException
	{
		openDesign( INPUT );
		CascadingParameterGroupHandle groupHandle = getGroupHandle( "Country-State-City" ); //$NON-NLS-1$
		assertEquals(
				"Group for Country-State-City", groupHandle.getDisplayName( ) ); //$NON-NLS-1$
		assertEquals( "Prompt Text for Country-State-City", groupHandle //$NON-NLS-1$
				.getPromptText( ) );
		assertEquals( DesignChoiceConstants.DATA_SET_MODE_MULTIPLE, groupHandle
				.getDataSetMode( ) );

		SlotHandle parameters = groupHandle.getParameters( );
		assertEquals( 3, parameters.getCount( ) );

		ScalarParameterHandle p1 = (ScalarParameterHandle) parameters.get( 0 );

		assertEquals( "dynamic", p1.getValueType( ) ); //$NON-NLS-1$
		assertEquals( "Country", p1.getName( ) ); //$NON-NLS-1$
		assertEquals( "ds1", p1.getDataSetName( ) ); //$NON-NLS-1$
		assertEquals( "country", p1.getValueExpr( ) ); //$NON-NLS-1$
		assertEquals( "Enter country:", p1.getLabelExpr( ) ); //$NON-NLS-1$
	}

	/**
	 * Test writer
	 * 
	 * @throws Exception
	 */

	public void testWriter( ) throws Exception
	{
		openDesign( INPUT );
		CascadingParameterGroupHandle groupHandle = getGroupHandle( "Country-State-City" ); //$NON-NLS-1$
		groupHandle.setDisplayName( "new name" ); //$NON-NLS-1$
		groupHandle.setPromptText( "new prompt text" ); //$NON-NLS-1$
		groupHandle.setDataSetMode( DesignChoiceConstants.DATA_SET_MODE_SINGLE );
		groupHandle.setDataSet( designHandle.findDataSet( "ds1" ) ); //$NON-NLS-1$
		saveAs( OUTPUT );
		assertTrue( compareTextFile( GOLDEN, OUTPUT ) );
	}

	/**
	 * Returns the parameter group handle given the name of the parameter group.
	 * 
	 * @param name
	 * @return
	 */

	private CascadingParameterGroupHandle getGroupHandle( String name )
	{
		SlotHandle parameters = this.designHandle.getParameters( );
		for ( int i = 0; i < parameters.getCount( ); i++ )
		{
			DesignElementHandle elementHandle = parameters.get( i );
			if ( elementHandle.getName( ).equals( name ) )
				return (CascadingParameterGroupHandle) elementHandle;
		}

		return null;
	}

	/**
	 * 1) Cacading parameter type should be "dynamic" 2) Dataset for the
	 * cascading parameter should be valid.
	 * 
	 * @throws Exception
	 */

	public void testSemanticErrors( ) throws Exception
	{
		openDesign( INPUT_SEMANTIC_ERRORS );
		List errors = designHandle.getErrorList( );
		assertEquals( 2, errors.size( ) );

		ErrorDetail error1 = (ErrorDetail) errors.get( 0 );
		assertEquals(
				SemanticError.DESIGN_EXCEPTION_INVALID_SCALAR_PARAMETER_TYPE,
				error1.getErrorCode( ) );

		ErrorDetail error2 = (ErrorDetail) errors.get( 1 );
		assertEquals( SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF,
				error2.getErrorCode( ) );
	}
}

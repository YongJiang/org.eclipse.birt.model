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

package org.eclipse.birt.report.model.metadata;

import java.math.BigDecimal;
import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Test case for <code>FloatPropertyType</code>.
 *  
 */
public class FloatPropertyTypeTest extends PropertyTypeTestCase
{

	FloatPropertyType type = new FloatPropertyType( );

	PropertyDefn propDefn = new PropertyDefnFake( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode()
	 */
	public void testGetTypeCode( )
	{
		assertEquals( PropertyType.FLOAT_TYPE, type.getTypeCode( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName( )
	{
		assertEquals( PropertyType.FLOAT_TYPE_NAME, type.getName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue()
	 */
	public void testValidateValue( ) throws PropertyValueException
	{
		assertEquals( null, type.validateValue( design, propDefn, null ) );
		assertEquals( null, type.validateValue( design, propDefn, "" ) ); //$NON-NLS-1$

		assertEquals( 12.34d, ( (Double) type.validateValue( design, propDefn,
				new Double( 12.34d ) ) ).doubleValue( ), 2 );
		assertEquals( 12.34d, ( (Double) type.validateValue( design, propDefn,
				new Float( 12.34f ) ) ).doubleValue( ), 2 );

		assertEquals( 12.34d, ( (Double) type.validateValue( design, propDefn,
				new BigDecimal( 12.34 ) ) ).doubleValue( ), 2 );
		assertEquals( 12, ( (Double) type.validateValue( design, propDefn,
				new Integer( 12 ) ) ).intValue( ) );
		assertEquals( 1, ( (Double) type.validateValue( design, propDefn,
				new Boolean( true ) ) ).intValue( ) );
		assertEquals( 0, ( (Double) type.validateValue( design, propDefn,
				new Boolean( false ) ) ).intValue( ) );

		// String
		ThreadResources.setLocale( ULocale.ENGLISH );
		assertEquals( 1234.123d, ( (Double) type.validateValue( design,
				propDefn, "1,234.123" ) ).doubleValue( ), 3 ); //$NON-NLS-1$
		assertEquals( 1234.123d, ( (Double) type.validateValue( design,
				propDefn, "1234.123" ) ).doubleValue( ), 3 ); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateInputString()
	 */
	public void testValidateInputString( ) throws PropertyValueException
	{

		assertEquals( null, type.validateInputString( design, propDefn, null ) );
		assertEquals( null, type.validateInputString( design, propDefn, "" ) ); //$NON-NLS-1$

		// String
		ThreadResources.setLocale( ULocale.ENGLISH );
		assertEquals( 1234.123d, ( (Double) type.validateInputString( design,
				propDefn, "1,234.123" ) ).doubleValue( ), 3 ); //$NON-NLS-1$
		assertEquals( 1234.123d, ( (Double) type.validateInputString( design,
				propDefn, "1234.123" ) ).doubleValue( ), 3 ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml()
	 */
	public void testValidateXml( ) throws PropertyValueException
	{
		assertEquals( null, type.validateXml( design, propDefn, null ) );
		assertEquals( null, type.validateXml( design, propDefn, "" ) ); //$NON-NLS-1$
		assertEquals( null, type.validateXml( design, propDefn, "  " ) ); //$NON-NLS-1$

		assertEquals(
				12.34d,
				( (Double) type.validateXml( null, null, "12.34" ) ).doubleValue( ), 2 ); //$NON-NLS-1$

		try
		{
			type.validateXml( design, propDefn, "abc.abc" ); //$NON-NLS-1$
			fail( );
		}
		catch ( PropertyValueException e )
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble()
	 */
	public void testToDouble( )
	{
		assertEquals( 0.0d, type.toDouble( design, null ), 1 );
		assertEquals( 0.0d, type.toDouble( design, new Double( 0.0d ) ), 1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger()
	 */
	public void testToInteger( )
	{
		assertEquals( 0, type.toInteger( design, null ) );
		assertEquals( 0, type.toInteger( design, new Double( 0.0d ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml( )
	{
		ULocale preULocale = ULocale.getDefault( );

		ULocale.setDefault( ULocale.GERMAN );
		assertEquals( null, type.toXml( design, propDefn, null ) );
		assertEquals(
				"123.456", type.toXml( design, propDefn, new Double( 123.456d ) ) ); //$NON-NLS-1$
		assertEquals(
				"123456.789", type.toXml( design, propDefn, new Double( "123456.789" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$
		ULocale.setDefault( ULocale.ENGLISH );
		assertEquals( null, type.toXml( design, propDefn, null ) );
		assertEquals(
				"123.456", type.toXml( design, propDefn, new Double( 123.456d ) ) ); //$NON-NLS-1$
		assertEquals(
				"123456.789", type.toXml( design, propDefn, new Double( "123456.789" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$

		ULocale.setDefault( preULocale );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString()
	 */
	public void testToString( )
	{
		assertEquals( null, type.toString( design, propDefn, null ) );
		assertEquals(
				"123.456", type.toString( design, propDefn, new Double( 123.456d ) ) ); //$NON-NLS-1$
		assertEquals(
				"3.0", type.toString( design, propDefn, new Double( 3.0d ) ) ); //$NON-NLS-1$
		assertEquals(
				"1234567890123456800000000.0", type.toString( design, propDefn, new Double( 1234567890123456789012345.12345678d ) ) ); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDisplayString()
	 */
	public void testToDisplayString( )
	{
		ThreadResources.setLocale( ULocale.ENGLISH );
		assertEquals( null, type.toString( design, propDefn, null ) );
		assertEquals(
				"123456.789", type.toString( design, propDefn, new Double( 123456.789d ) ) ); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber()
	 */
	public void testToNumber( )
	{
		assertEquals( 0.0d, type.toNumber( design, null ).doubleValue( ), 1 );
		assertEquals( 0.0d, type.toNumber( design, new Double( 0.0d ) )
				.doubleValue( ), 1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean()
	 */
	public void testToBoolean( )
	{
	}

}
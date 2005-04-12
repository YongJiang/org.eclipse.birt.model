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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Abstract class for working with properties that have internal structure, such
 * as a color, a dimension or a font.
 */

public abstract class ComplexValueHandle extends ValueHandle
{

	/**
	 * Property definition.
	 */

	protected ElementPropertyDefn propDefn = null;

	/**
	 * Path to the property within an element, a list or a structure.
	 */

	protected MemberRef memberRef = null;

	/**
	 * Constructs a handle given an element handle and definition of a property.
	 * The element property definition cannot be null.
	 * 
	 * @param element
	 *            handle to the report element that contains the element
	 *            property.
	 * @param thePropDefn
	 *            element property definition.
	 */

	public ComplexValueHandle( DesignElementHandle element,
			ElementPropertyDefn thePropDefn )
	{
		super( element );
		assert thePropDefn != null;

		propDefn = thePropDefn;
	}

	/**
	 * Constructs a handle given an element handle and member reference. The
	 * element property definition can not be null.
	 * 
	 * @param element
	 *            handle to the report element that has the property that
	 *            contains the structure that contains the member.
	 * @param theMemberRef
	 *            The reference to the member.
	 */

	public ComplexValueHandle( DesignElementHandle element,
			MemberRef theMemberRef )
	{
		super( element );
		assert theMemberRef != null;

		propDefn = theMemberRef.getPropDefn( );

		assert propDefn != null;

		memberRef = theMemberRef;
	}

	/**
	 * Sets the value of a property to the given value. If the value is null,
	 * then the property value is cleared.
	 * 
	 * @param value
	 *            The new value.
	 * @throws SemanticException
	 *             If the value is not valid for the property or member.
	 */

	public void setValue( Object value ) throws SemanticException
	{
		PropertyCommand cmd = new PropertyCommand( getDesign( ), getElement( ) );

		if ( memberRef == null )
		{
			cmd.setProperty( propDefn, value );
		}
		else
		{
			cmd.setMember( memberRef, value );
		}
	}

	/**
	 * Gets the value of the property as a generic object.
	 * 
	 * @return The value of the property or member as a generic object.
	 */

	public Object getValue( )
	{
		Object value = null;
		if ( memberRef == null )
		{
			value = getElement( ).getProperty( getDesign( ), propDefn );

		}
		else
		{
			value = memberRef.getValue( getDesign( ), getElement( ) );
		}

		return value;
	}

	/**
	 * Sets the value of a property or member to a string. Call this method to
	 * set a input string from the user( localized or non-localized value).
	 * 
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 *             if the string value is not valid for the property or member.
	 */

	public void setStringValue( String value ) throws SemanticException
	{
		setValue( value );
	}

	/**
	 * Gets the property value converted to a string value.
	 * 
	 * @return The property or member value as a string.
	 */

	public String getStringValue( )
	{
		PropertyDefn prop = memberRef == null ? propDefn : memberRef
				.getMemberDefn( );
		return prop.getStringValue( getDesign( ), getValue( ) );
	}

	/**
	 * Returns the value of the property or member in a localized format.
	 * 
	 * @return Returns the value of the property or member in a localized
	 *         format.
	 */

	public String getDisplayValue( )
	{
		PropertyDefn prop = memberRef == null ? propDefn : memberRef
				.getMemberDefn( );
		return prop.getDisplayValue( getDesign( ), getValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getPropertyDefn()
	 */

	public IElementPropertyDefn getPropertyDefn( )
	{
		return this.propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getReference()
	 */

	public MemberRef getReference( )
	{
		return this.memberRef;
	}

	/**
	 * Returns a definition for the handle.
	 * 
	 * @return the definition of the handle.
	 */

	protected IPropertyDefn getDefn( )
	{
		// For a member in a structure, the return value
		// is <code>StructPropertyDefn</code>. For an element property, the
		// return value is <code>ElementPropertyDefn</code>.

		if ( memberRef != null )
			return memberRef.getMemberDefn( );

		return propDefn;
	}

	/**
	 * Tests whether this property value is set for this element or the
	 * structure.
	 * <p>
	 * <ul>
	 * <li>For an element property, it is set if it is defined on this element
	 * property or any of its parents, or in the element's private style
	 * property. It is considered unset if it is set on a shared style.</li>
	 * <li>For a member, it is set if the value is not <code>null</code>,
	 * otherwise it is considered unset.</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if the value is set, <code>false</code> if
	 *         it is not set
	 */

	public boolean isSet( )
	{
		if ( memberRef == null )
		{
			FactoryPropertyHandle handle = new FactoryPropertyHandle(
					elementHandle, propDefn );
			return handle.isSet( );
		}

		Object value = memberRef.getValue( getDesign( ), getElement( ) );
		return ( value != null );
	}
}
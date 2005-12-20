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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * A handle for working with a top-level property of a collection of elements.
 * Use this handle to set/get values of a property if this property is common
 * across the given collection of elements.
 */

public class GroupPropertyHandle
{

	/**
	 * Definition of the property.
	 */

	protected ElementPropertyDefn propDefn = null;

	/**
	 * Handle to a collection of elements.
	 */

	protected GroupElementHandle handle = null;

	/**
	 * Constructs a handle to deal with an common property within a group of
	 * elements. The given property definition should be a common property
	 * shared by the collection of elements.
	 * 
	 * @param handle
	 *            Handles to a collection of elements.
	 * @param propDefn
	 *            definition of the property.
	 */

	GroupPropertyHandle( GroupElementHandle handle, ElementPropertyDefn propDefn )
	{
		assert propDefn != null;
		assert handle instanceof SimpleGroupElementHandle;

		this.handle = handle;
		this.propDefn = propDefn;
	}

	/**
	 * Indicates whether the group of element share the same value for this
	 * property.
	 * <p>
	 * If all element has a <code>null</code> value for this property, it is
	 * considered that they share the same value.
	 * 
	 * @return <code>true</code> if the group of element share the same value.
	 */

	public final boolean shareSameValue( )
	{
		Iterator iter = handle.getElements( ).iterator( );

		// List with no content.

		if ( !iter.hasNext( ) )
			return false;

		DesignElementHandle elemHandle = (DesignElementHandle) iter.next( );

		// use the value set on the first element as the base value.

		String baseValue = elemHandle.getStringProperty( propDefn.getName( ) );

		while ( iter.hasNext( ) )
		{
			elemHandle = (DesignElementHandle) iter.next( );
			String value = elemHandle.getStringProperty( propDefn.getName( ) );

			if ( baseValue == null )
			{
				if ( value != null )
					return false;
			}
			else
			{
				if ( !baseValue.equals( value ) )
					return false;
			}
		}

		return true;
	}

	/**
	 * Value will be returned as string only if all values of this property are
	 * equal within the collection of elements.
	 * 
	 * @return The value as string if all the element values for the property
	 *         are equal. Return null, if elements have different value for the
	 *         property.
	 * @see SimpleValueHandle#getStringValue()
	 */

	public String getStringValue( )
	{
		if ( !shareSameValue( ) )
			return null;

		// List must contain at least one element.
		// return the property value from the first element.

		List elements = handle.getElements( );

		return ( (DesignElementHandle) elements.get( 0 ) )
				.getStringProperty( propDefn.getName( ) );
	}

	/**
	 * Value will be returned as string only if all values of this property are
	 * equal within the collection of elements. The value return are localized.
	 * 
	 * @return The localized value as string if all the element values for the
	 *         property are equal. Return null, if elements have different value
	 *         for the property.
	 * @see SimpleValueHandle#getDisplayValue()
	 */

	public String getDisplayValue( )
	{
		if ( !shareSameValue( ) )
			return null;

		// List must contain at least one element.
		// return the property value from the first element.

		List elements = handle.getElements( );

		return ( (DesignElementHandle) elements.get( 0 ) )
				.getDisplayProperty( propDefn.getName( ) );
	}

	/**
	 * Set the object value on a group of elements. This operation will be
	 * executed within a transaction, it will be rollbacked if any set operation
	 * failed.
	 * 
	 * @param value
	 *            the object value to set
	 * @throws SemanticException
	 *             if the property is undefined on an element or the value is
	 *             invalid.
	 * @see PropertyHandle#setValue(Object)
	 */

	public void setValue( Object value ) throws SemanticException
	{
		assert handle.getModule( ) != null;
		ActivityStack actStack = handle.getModule( ).getActivityStack( );
		actStack.startTrans( );

		try
		{
			for ( Iterator iter = handle.getElements( ).iterator( ); iter
					.hasNext( ); )
			{
				DesignElementHandle elemHandle = (DesignElementHandle) iter
						.next( );
				elemHandle.setProperty( propDefn.getName( ), value );
			}
		}
		catch ( SemanticException e )
		{
			actStack.rollback( );
			throw e;
		}

		actStack.commit( );
	}

	/**
	 * Set the string value on a group of elements. This operation will be
	 * executed within a transaction, it will be rollbacked if any set operation
	 * failed.
	 * 
	 * @param value
	 *            the string value to set
	 * @throws SemanticException
	 *             if the property is undefined on an element or the string
	 *             value is invalid.
	 * @see SimpleValueHandle#setStringValue(String)
	 */

	public void setStringValue( String value ) throws SemanticException
	{
		setValue( value );
	}

	/**
	 * Return the property definition.
	 * 
	 * @return the property definition.
	 */

	public IElementPropertyDefn getPropertyDefn( )
	{
		return this.propDefn;
	}

	/**
	 * Clears the value of the property on every element.
	 * 
	 * @throws SemanticException
	 *             If the value cannot be cleared.
	 */

	public void clearValue( ) throws SemanticException
	{
		setValue( null );
	}

	/**
	 * Returns the element reference value list if the property is element
	 * referenceable type. The list of available elements are sorted by their
	 * names lexicographically.
	 * 
	 * @return list of the reference element value.
	 */

	public List getReferenceableElementList( )
	{
		if ( propDefn.getTypeCode( ) != PropertyType.ELEMENT_REF_TYPE )
			return Collections.EMPTY_LIST;

		ElementDefn elementDefn = (ElementDefn) propDefn.getTargetElementType( );
		assert elementDefn != null;

		List elementList = null;
		assert handle.getModuleHandle() != null;
		if ( ReportDesignConstants.DATA_SET_ELEMENT.equals( elementDefn
				.getName( ) ) )
		{
			elementList = handle.getModuleHandle( ).getAllDataSets( );
		}
		else if ( ReportDesignConstants.DATA_SOURCE_ELEMENT.equals( elementDefn
				.getName( ) ) )
		{
			elementList = handle.getModuleHandle( ).getAllDataSources( );
		}
		else if ( ReportDesignConstants.STYLE_ELEMENT.equals( elementDefn
				.getName( ) ) )
		{
			elementList = handle.getModuleHandle( ).getAllStyles( );
		}
		else if ( ReportDesignConstants.THEME_ITEM.equals( elementDefn
				.getName( ) ) )
		{
			elementList = handle.getModuleHandle( ).getAllThemes( );
		}

		return ModelUtil.sortElementsByName( elementList );
	}

	/**
	 * Compares the specified Object with this <code>GroupPropertyHandle</code>
	 * for equality. Returns <code>true</code> in the following cases:
	 * <ul>
	 * <li><code>target</code> is a <code>PropertyHandle</code>. The
	 * element of <code>target</code> is in the
	 * <code>GroupElementHandle</code> and two property definitions are same.
	 * </li>
	 * <li><code>target</code> is a <code>GroupPropertyHandle</code>.
	 * <code>GroupElementHandle</code> and the the property definition are
	 * same.</li>
	 * </ul>
	 * 
	 * @param target
	 *            the property or group property handle
	 * @return <code>true</code> if the two property handles are considerred
	 *         as same. Otherwise <code>false</code>.
	 */

	public boolean equals( Object target )
	{
		if ( !( target instanceof PropertyHandle )
				&& !( target instanceof GroupPropertyHandle ) )
			return false;

		if ( target instanceof PropertyHandle )
		{
			DesignElementHandle targetElement = ( (PropertyHandle) target )
					.getElementHandle( );
			IPropertyDefn targetPropDefn = ( (PropertyHandle) target )
					.getDefn( );
			return ( handle.isInGroup( targetElement ) && targetPropDefn == this.propDefn );
		}

		GroupPropertyHandle propHandle = (GroupPropertyHandle) target;
		return ( propHandle.handle == this.handle && propHandle.propDefn == getPropertyDefn( ) );
	}

	/**
	 * Checks whether a property is visible in the property sheet. The visible
	 * property is visible in all <code>elements</code>.
	 * 
	 * @return <code>true</code> if it is visible. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isVisible( )
	{
		return handle.isPropertyVisible( propDefn.getName( ) );
	}

	/**
	 * Checks whether a property is read-only in the property sheet. The
	 * read-only property is read-only in all <code>elements</code>.
	 * 
	 * @return <code>true</code> if it is read-only. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isReadOnly( )
	{
		return handle.isPropertyReadOnly( propDefn.getName( ) );
	}
}
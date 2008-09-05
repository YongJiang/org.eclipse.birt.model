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

import java.util.List;

import org.eclipse.birt.report.model.api.validators.StyleReferenceValidator;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Base class for all report elements with a style. Implements operations that
 * are specific to styled elements.
 * 
 */

public abstract class StyledElement extends DesignElement
		implements
			IStyledElementModel
{

	/**
	 * The shared style which this element references, if any.
	 */

	protected ElementRefValue style = null;

	/**
	 * Default constructor.
	 */

	public StyledElement( )
	{
	}

	/**
	 * Constructs the styled element with an optional name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public StyledElement( String theName )
	{
		super( theName );
	}

	/**
	 * Makes a clone of this styled element. The style that was referenced by
	 * this element if any, will be set to a unresolved element reference for
	 * the cloned one.
	 * 
	 * @return Object the cloned styled element.
	 * 
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object doClone( CopyPolicy policy )
			throws CloneNotSupportedException
	{
		StyledElement element = (StyledElement) super.doClone( policy );
		if ( style != null )
			element.style = new ElementRefValue( null, style.getName( ) );
		else
			element.style = null;
		return element;
	}

	/**
	 * Gets the style which defined on this element itself. This method will try
	 * to resolve the style.
	 * 
	 * @param module
	 *            the module
	 * @return style element. Null if the style is not defined on this element
	 *         itself.
	 * 
	 */
	public StyleElement getStyle( Module module )
	{
		if ( style == null )
			return null;

		if ( style.isResolved( ) )
			return (StyleElement) style.getElement( );

		if ( module == null )
			return null;

		DesignElement resolvedElement = module.resolveElement(
				style.getName( ), getPropertyDefn( STYLE_PROP ), null );

		StyleElement target = null;
		if ( resolvedElement != null )
		{
			// target = (StyleElement) refValue.getElement( );
			target = (StyleElement) resolvedElement;
			style.resolve( target );
			target.addClient( this, STYLE_PROP );
		}

		return target;
	}

	/**
	 * Gets the name of the referenced style on this element.
	 * 
	 * @return style name. null if the style is not defined on the element.
	 */

	public String getStyleName( )
	{
		if ( style == null )
			return null;
		return style.getName( );
	}

	/**
	 * Gets the style which defined on this element. The style element can be
	 * retrieved from this element extends hierarchy.
	 * 
	 * @return style element. null if this element didn't define a style on it.
	 * 
	 * 
	 */
	public StyleElement getStyle( )
	{
		if ( style == null )
			return null;
		return (StyleElement) style.getElement( );
	}

	/**
	 * Sets the style. If null, the style is cleared.
	 * 
	 * @param newStyle
	 *            the style to set
	 */

	public void setStyle( StyleElement newStyle )
	{
		StyleElement oldStyle = null;
		if ( style != null )
			oldStyle = (StyleElement) style.getElement( );

		// if the style is null and new style is null, return
		// if the style is resolved and the resolved element equals to the new
		// style, return

		if ( oldStyle == newStyle && ( style == null || style.isResolved( ) ) )
			return;

		if ( oldStyle != null )
			oldStyle.dropClient( this );
		if ( newStyle != null )
		{
			if ( style == null )
				style = new ElementRefValue( null, newStyle );
			else
				style.resolve( newStyle );
			newStyle.addClient( this, STYLE_PROP );
		}
		else
			style = null;
	}

	/**
	 * Sets the shared style by name. If null, the style is cleared. Use this
	 * form to represent an "unresolved" style: a reference to an undefined
	 * style, or a forward reference while parsing a design file.
	 * 
	 * @param theName
	 *            the style name
	 */

	public void setStyleName( String theName )
	{
		if ( style == null && theName == null )
			return;
		setStyle( null );
		assert style == null;
		style = new ElementRefValue( null, theName );
	}

	/**
	 * Returns the value of an intrinsic property.If the property name is
	 * <code>style</code> then return the style element.
	 * 
	 * @param propName
	 *            name of the intrinsic property
	 * @return intrinsic property
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( propName.equals( STYLE_PROP ) )
			return style;
		return super.getIntrinsicProperty( propName );
	}

	/**
	 * If the style name is represented as a name, then attempts to resolve the
	 * style name to obtain the referenced style.
	 * 
	 * @see #validate(Module)
	 * 
	 */

	public List validate( Module module )
	{
		List list = super.validate( module );

		// Resolve style

		list.addAll( StyleReferenceValidator.getInstance( ).validate( module,
				this ) );

		list.addAll( Style.validateStyleProperties( module, this ) );

		return list;
	}

	/**
	 * Gets a property value by its definition. The search will not search the
	 * container element extends hierarchy.
	 * 
	 * @param module
	 *            module
	 * @param prop
	 *            definition of the property to get
	 * 
	 * @return The property value, or null if no value is set.
	 */

	public Object getFactoryProperty( Module module, ElementPropertyDefn prop )
	{
		if ( !prop.isStyleProperty( ) )
			return super.getFactoryProperty( module, prop );

		// Get the value from this element and its parent.

		return getStrategy( ).getPropertyFromElement( module, this, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#clearAllProperties()
	 */
	public void clearAllProperties( )
	{
		super.clearAllProperties( );
		this.style = null;
	}

}
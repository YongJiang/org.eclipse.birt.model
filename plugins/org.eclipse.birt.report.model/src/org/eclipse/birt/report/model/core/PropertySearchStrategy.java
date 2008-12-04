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

import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.command.ContentElementInfo;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Default property searching strategy. Applied to most design element.
 */

public class PropertySearchStrategy
{

	/**
	 * Default strategy instance.
	 */

	private final static PropertySearchStrategy instance = new PropertySearchStrategy( );

	/**
	 * Protected constructor.
	 */

	protected PropertySearchStrategy( )
	{
	}

	/**
	 * Returns the instance of <code>PropertySearchStrategy</code> which provide
	 * the specific property searching route for most design elements.
	 * 
	 * @return the instance of <code>PropertySearchStrategy</code>
	 */
	public static PropertySearchStrategy getInstance( )
	{
		return instance;
	}

	/**
	 * Gets a property value given its definition. This version does the
	 * property search with style reference, extends reference and containment.
	 * The default value style property defined in session is also searched, but
	 * the default value defined in ROM will not be returned.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to start search
	 * @param prop
	 *            definition of the property to get
	 * @return The property value, or null if no value is set.
	 */

	public Object getPropertyExceptRomDefault( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		if ( prop.isIntrinsic( ) )
		{
			// This is an intrinsic system-defined property.

			return element.getIntrinsicProperty( prop.getName( ) );
		}

		// Repeat the search up the inheritance or style
		// hierarchy, starting with this element.

		DesignElement e = element;
		Object value = null;
		while ( e != null )
		{
			PropertySearchStrategy tmpStrategy = e.getStrategy( );

			// Check if this element or parent provides the value

			value = tmpStrategy.getNonIntrinsicPropertyFromElement( module, e,
					prop );
			if ( value != null )
				return value;

			if ( !prop.isStyleProperty( ) || e.isStyle( )
					|| !tmpStrategy.isInheritableProperty( e, prop ) )
				break;

			// Try to get the value of this property from container
			// hierarchy.

			e = getStyleContainer( e );
		}

		// Still not found. Use the default.

		return getSessionDefaultValue( module, prop );
	}

	/**
	 * Gets the container element.
	 * 
	 * @param designElement
	 *            the design element
	 * @return the container of design element.
	 */

	protected DesignElement getStyleContainer( DesignElement designElement )
	{
		return designElement.getContainer( );
	}

	/**
	 * Gets a property value given its definition. This version does the
	 * property search as defined by the given derived component. That is, it
	 * gets the "effective" property value. The definition can be for a system
	 * or user-defined property.
	 * <p>
	 * The search won't search up the containment hierarchy. Meanwhile, it won't
	 * the inheritance hierarchy if the non-style property is not inheritable.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to search
	 * @param prop
	 *            definition of the property to get
	 * @return The property value, or null if no value is set.
	 */

	public final Object getPropertyFromElement( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		if ( prop.isIntrinsic( ) )
		{
			// This is an intrinsic system-defined property.

			return element.getIntrinsicProperty( prop.getName( ) );
		}

		return getNonIntrinsicPropertyFromElement( module, element, prop );

	}

	/**
	 * Gets a non-intrinsic property value given its definition. This version
	 * does the property search as defined by the given derived component. That
	 * is, it gets the "effective" property value. The definition can be for a
	 * system or user-defined property.
	 * <p>
	 * The search won't search up the containment hierarchy. Meanwhile, it won't
	 * the inheritance hierarchy if the non-style property is not inheritable.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to search
	 * @param prop
	 *            definition of the property to get
	 * @return The property value, or null if no value is set.
	 */

	protected Object getNonIntrinsicPropertyFromElement( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		assert !prop.isIntrinsic( );

		Object value = null;

		value = getPropertyFromSelf( module, element, prop );
		if ( value != null )
			return value;

		// Can we search the parent element ?
		// search the parent element: (1) the property is not a style property
		// and "canInherit" is true; (2) the property is a style property

		if ( isInheritableProperty( element, prop ) || prop.isStyleProperty( ) )
		{
			value = getPropertyFromParent( module, element, prop );

			if ( value != null )
				return value;
		}

		// Check if this element predefined style provides
		// the property value

		if ( module == null )
			return null;

		if ( prop.isStyleProperty( ) )
		{
			value = getPropertyFromSelfSelector( module, element, prop );
			if ( value != null )
				return value;

			// Check if the container/slot predefined style provides
			// the value

			value = getPropertyFromSlotSelector( module, element, prop );
			if ( value != null )
				return value;

			// for the special case that may relates to the container.

			value = getPropertyRelatedToContainer( module, element, prop );
			if ( value != null )
				return value;
		}

		return null;
	}

	/**
	 * Returns the property value from this element. The value is only from
	 * local properties or local style of this element.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to start search
	 * @param prop
	 *            the definition of property
	 * @return the property value, or null if no value is set.
	 */

	protected Object getPropertyFromSelf( Module module, DesignElement element,
			ElementPropertyDefn prop )
	{
		// 1). If we can find the value here, return it.

		Object value = element.getLocalProperty( module, prop );
		if ( value != null )
		{
			updateContainerForContentElement( module, element, prop, value );
			return value;
		}

		// 2). Does the style provide the value of this property ?

		StyleElement style = element.getStyle( module );
		if ( style != null )
		{
			value = style.getLocalProperty( module, prop );
			if ( value != null )
				return value;
		}

		return null;
	}

	/**
	 * Returns the property value from this element's parent, or its virtual
	 * parent. The value is only from local properties, or local style of its
	 * ancestor.
	 * 
	 * @param element
	 *            the element to start search parent
	 * @param module
	 *            module
	 * @param prop
	 *            definition of the property to get.
	 * @return property value, or <code>null</code> if no value is set.
	 */

	protected Object getPropertyFromParent( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		Object value = null;
		DesignElement e = element;

		do
		{
			if ( e.isVirtualElement( ) )
			{
				// Does the virtual parent provide the value of this property ?

				e = e.getVirtualParent( );
			}
			else
			{
				// Does the parent provide the value of this property?

				e = e.getExtendsElement( );
			}

			if ( e != null )
			{
				Module currentRoot = e.getRoot( );
				assert currentRoot != null;

				// If we can find the value here, return it.

				value = getPropertyFromSelf( currentRoot, e, prop );
				if ( value != null )
				{
					updateContainerForContentElement( module, element, prop,
							value );
					return value;
				}

			}

		} while ( e != null );

		return value;
	}

	/**
	 * Updates the container information for the content element.
	 * 
	 * @param module
	 *            the module
	 * @param prop
	 *            definition of the property to get
	 * @param value
	 *            the property value, or null if no value is set.
	 */

	private void updateContainerForContentElement( Module module,
			DesignElement element, ElementPropertyDefn prop, Object value )
	{
		if ( prop.getTypeCode( ) != IPropertyType.CONTENT_ELEMENT_TYPE
				&& prop.getSubTypeCode( ) != IPropertyType.CONTENT_ELEMENT_TYPE )
			return;

		ContentElementInfo info = null;
		if ( element instanceof ContentElement )
			info = ( (ContentElement) element ).getValueContainer( );
		else if ( prop.getTypeCode( ) == IPropertyType.CONTENT_ELEMENT_TYPE
				|| prop.getSubTypeCode( ) == IPropertyType.CONTENT_ELEMENT_TYPE )
			info = new ContentElementInfo( element, prop );

		if ( value instanceof ContentElement )
		{
			Module root = ( (ContentElement) value ).getRoot( );

			ContentElementInfo tmpInfo = null;
			if ( root != module )
			{
				tmpInfo = info;
			}
			( (ContentElement) value ).setValueContainer( tmpInfo );
		}
		else if ( value instanceof List )
		{
			List items = (List) value;
			Module root = null;

			ContentElementInfo tmpInfo = null;

			for ( int i = 0; i < items.size( ); i++ )
			{
				ContentElement item = (ContentElement) items.get( i );

				if ( root == null )
				{
					root = item.getRoot( );
					if ( root != module )
						tmpInfo = info;
				}
				item.setValueContainer( tmpInfo );
			}
		}
	}

	/**
	 * Returns the property value which is related to element selector. It is
	 * from the selector style.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to start search
	 * @param prop
	 *            the definition of property
	 * @return the property value, or null if no value is set.
	 */

	protected Object getPropertyFromSelfSelector( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		String selector = ( (ElementDefn) element.getDefn( ) ).getSelector( );
		return getPropertyFromSelector( module, prop, selector );
	}

	/**
	 * Returns the property value which is related to slot selector. It is from
	 * the selector style which represents the slot or combination of container
	 * and slot.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to search
	 * @param prop
	 *            the definition of property
	 * @return the property value, or null if no value is set.
	 */

	protected Object getPropertyFromSlotSelector( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		if ( element.getContainer( ) == null )
			return null;

		String selector = element.getContainerInfo( ).getSelector( );

		return getPropertyFromSelector( module, prop, selector );
	}

	/**
	 * Returns property value with predefined style.
	 * 
	 * @param module
	 *            module
	 * @param prop
	 *            definition of property to get
	 * @param selector
	 *            predefined style
	 * @return The property value, or null if no value is set.
	 */

	protected Object getPropertyFromSelector( Module module,
			ElementPropertyDefn prop, String selector )
	{
		assert module != null;

		if ( selector == null )
			return null;

		// Find the predefined style

		StyleElement style = module.findStyle( selector );
		if ( style != null )
		{
			return style.getLocalProperty( module, prop );
		}

		return null;
	}

	/**
	 * Returns the property value which is related to container.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element
	 * @param prop
	 *            the definition of property
	 * @return the property value, or null if no value is set.
	 */

	protected Object getPropertyRelatedToContainer( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		return null;
	}

	/**
	 * Gets the session default value of the specified property if it is style
	 * property.
	 * 
	 * @param module
	 *            module
	 * @param prop
	 *            definition of the property to get
	 * @return The session default property value, or null if no default value
	 *         is set.
	 */

	protected Object getSessionDefaultValue( Module module,
			ElementPropertyDefn prop )
	{
		if ( prop.isStyleProperty( ) )
		{
			// Does session define default value for this property ?

			return module.getSession( ).getDefaultValue( prop.getName( ) );
		}

		return null;
	}

	/**
	 * Tests if the property is inheritable in the context.
	 * 
	 * @param element
	 *            the element to test
	 * @param prop
	 *            definition of the property to test
	 * @return <code>true</code> if the property is inheritable in the context,
	 *         otherwise, <code>false</code>.
	 */

	protected boolean isInheritableProperty( DesignElement element,
			ElementPropertyDefn prop )
	{
		assert prop != null;

		boolean inherit = prop.canInherit( );
		if ( !inherit )
			return false;

		if ( element instanceof ReportItem )
		{
			if ( IReportItemModel.BOUND_DATA_COLUMNS_PROP.equals( prop
					.getName( ) ) )
			{
				// If there is data set property, and want to get
				// bounddatacolumn property,
				// the bounddatacolumn property can't inherit from parent

				if ( element.getLocalProperty( element.getRoot( ),
						IReportItemModel.DATA_SET_PROP ) != null )
					return false;
			}
		}
		else if ( element instanceof ScalarParameter )
		{
			if ( IScalarParameterModel.BOUND_DATA_COLUMNS_PROP.equals( prop
					.getName( ) ) )
			{
				// If there is data set property, and want to get
				// bounddatacolumn property,
				// the bounddatacolumn property can't inherit from parent

				if ( element.getLocalProperty( element.getRoot( ),
						IScalarParameterModel.DATASET_NAME_PROP ) != null )
					return false;
			}
		}

		return inherit;
	}
}

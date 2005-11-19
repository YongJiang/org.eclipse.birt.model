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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameEvent;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents an element that defines a style. An element that uses this style
 * is called a <em>client</em> element. This class manages the inverse
 * style-to-client relationship. It also handles sending notifications to the
 * client elements.
 */

public abstract class StyleElement extends ReferenceableElement
{

	private final static String REPORT_SELECTOR = "report"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public StyleElement( )
	{
	}

	/**
	 * Constructor with the element name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public StyleElement( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#setDeliveryPath(org.eclipse.birt.report.model.activity.NotificationEvent)
	 */

	protected void adjustDeliveryPath( NotificationEvent ev )
	{
		ev.setDeliveryPath( NotificationEvent.STYLE_CLIENT );
	}

	/**
	 * Returns true if the element is style.
	 * 
	 * @return true if the element is style, otherwise return false.
	 */

	public boolean isStyle( )
	{
		return true;
	}

	/**
	 * Gets the value of property.
	 * 
	 * @param module
	 *            module
	 * @param prop
	 *            definition of the property to get
	 * @return the value of the property.
	 */

	public Object getFactoryProperty( Module module, ElementPropertyDefn prop )
	{
		return getLocalProperty( module, prop );
	}

	/**
	 * Gets the extended element of this element. Always return null cause style
	 * element is not allowed to extend.
	 * 
	 * @return null
	 */
	public DesignElement getExtendsElement( )
	{
		return null;
	}

	/**
	 * Gets the name if the extended element. Always return null cause style
	 * element is not allowed to extend.
	 * 
	 * @return null
	 */

	public String getExtendsName( )
	{
		return null;
	}

	/**
	 * Sets the extended element. This operation is not allowed to do for style
	 * element.
	 * 
	 * @param base
	 *            the base element to set
	 */

	public void setExtendsElement( DesignElement base )
	{
		assert false;
	}

	/**
	 * Sets the extended element name. This operation is not allowed to do for
	 * style element.
	 * 
	 * @param name
	 *            name of the base element to set
	 */

	public void setExtendsName( String name )
	{
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#broadcastToClients(org.eclipse.birt.report.model.activity.NotificationEvent,
	 *      org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	protected void broadcastToClients( NotificationEvent ev, Module module )
	{
		super.broadcastToClients( ev, module );

		// Broad the event to the elements selected by selector style.

		String selector = null;

		if ( ev instanceof NameEvent )
		{
			String oldName = ( (NameEvent) ev ).getOldName( );
			String newName = ( (NameEvent) ev ).getNewName( );

			if ( MetaDataDictionary.getInstance( ).getPredefinedStyle( oldName ) != null )
				selector = oldName;
			else if ( MetaDataDictionary.getInstance( ).getPredefinedStyle(
					newName ) != null )
				selector = newName;
		}
		else
		{
			if ( MetaDataDictionary.getInstance( ).getPredefinedStyle(
					getName( ) ) != null )
				selector = getName( );
		}

		if ( selector != null )
		{
			// broadcast the change to theme references.

			DesignElement tmpContainer = getContainer( );
			List modules = new ArrayList( );

			if ( getContainer( ) instanceof Theme )
			{
				Theme containerTheme = (Theme) tmpContainer;
				if ( containerTheme.hasReferences( ) )
				{
					List refs = ( (Theme) tmpContainer ).getClientList( );
					for ( int i = 0; i < refs.size( ); i++ )
						modules.add( ( (BackRef) refs.get( i ) ).getElement( ) );
				}
			}
			else
				modules.add( module );

			for ( int i = 0; i < modules.size( ); i++ )
				broadcastToModule( (Module) modules.get( i ), selector );
		}
	}

	/**
	 * @param module
	 *            the module of the style element
	 * @param selectorName
	 *            the selector name
	 */

	private void broadcastToModule( Module module, String selectorName )
	{
		assert selectorName != null;

		// Work around for renaming selector style.
		if ( REPORT_SELECTOR.equals( selectorName ) )
		{
			NotificationEvent event = null;
			event = new StyleEvent( module );
			event.setDeliveryPath( NotificationEvent.STYLE_CLIENT );
			module.broadcast( event );

		}
		else
		{
			broadcastToSelectedElementsInSlot( module, module
					.getSlot( Module.COMPONENT_SLOT ), selectorName );
			broadcastToSelectedElementsInSlot( module, module
					.getSlot( Module.PAGE_SLOT ), selectorName );

			// only report design has the body, scratch pad slots.

			if ( module instanceof ReportDesign )
			{
				broadcastToSelectedElementsInSlot( module, module
						.getSlot( ReportDesign.BODY_SLOT ), selectorName );

				broadcastToSelectedElementsInSlot( module, module
						.getSlot( ReportDesign.SCRATCH_PAD_SLOT ), selectorName );
			}
		}

	}

	/**
	 * Broadcasts the event to all elements in the given slot if the elements
	 * are selected by selector style.
	 * 
	 * @param module
	 *            the module
	 * @param slot
	 *            the slot to send
	 * @param selectorName
	 *            the selector name
	 */

	private void broadcastToSelectedElementsInSlot( Module module,
			ContainerSlot slot, String selectorName )
	{
		Iterator iter = slot.iterator( );

		NotificationEvent event = null;

		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			assert element != null;

			event = new StyleEvent( element );
			event.setDeliveryPath( NotificationEvent.STYLE_CLIENT );

			// Broadcast the element which is selected by this style

			String selector = getMatchedElementSelector( element, selectorName );
			if ( selector != null )
			{
				element.broadcast( event, module );
				continue;
			}

			// check if the element slot has the selector with the same name as
			// the given selector name.

			if ( checkSlotSelector( element, selectorName, event, module ) )
				continue;

			int count = element.getDefn( ).getSlotCount( );
			for ( int i = 0; i < count; i++ )
			{
				broadcastToSelectedElementsInSlot( module,
						element.getSlot( i ), selectorName );
			}
		}
	}

	/**
	 * Returns the element selector if its selector matches the given
	 * <code>selectorName</code>.
	 * 
	 * @param element
	 *            the design element
	 * @param selectorName
	 *            the name to match
	 * @return the matched selector name or null if not matched.
	 */

	private String getMatchedElementSelector( DesignElement element,
			String selectorName )
	{
		String selector = null;
		
		// get extension defined style 
		
		if ( element instanceof ExtendedItem )
		{
			String tmpSelector = null;
			
			// get extension element definition of the extended item.
			
			ElementDefn elementDefn = ( (ExtendedItem) element ).getExtDefn( );
			if ( elementDefn != null )
				tmpSelector = elementDefn.getSelector( );

			if ( tmpSelector != null
					&& tmpSelector.equalsIgnoreCase( selectorName ) )
				selector = tmpSelector;
		}

		// get ROM defined style 
		
		if ( selector == null )
			selector = ( (ElementDefn) element.getDefn( ) ).getSelector( );

		if ( selector != null && selector.equalsIgnoreCase( selectorName ) )
			return selector;

		return null;
	}

	private boolean checkSlotSelector( DesignElement element,
			String selectorName, NotificationEvent event, Module module )
	{

		String selector = element.getContainer( ).getSelector(
				element.getContainerSlot( ) );

		if ( selector != null && selector.equalsIgnoreCase( selectorName ) )
		{
			element.broadcast( event, module );
			return true;
		}
		return false;

	}
}
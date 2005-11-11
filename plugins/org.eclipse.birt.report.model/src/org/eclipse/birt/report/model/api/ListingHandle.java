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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * Represents slots and common properties in the two list-oriented elements:
 * table and list. A listing has on-start, on-row and on-finish script
 * properties as well as detail, footer, groups and header slots.
 * <p>
 * <ul>
 * <li>The on-start script called before the first row is retrieved from the
 * data set for this element. Called after the data set is open but before the
 * header band is created.
 * <li>The on-row script called for each row retrieved from the data set for
 * this element, but before creating any content for that row.
 * <li>The on-finish script called after the last row is read from the data set
 * for this element, but before the footer band is created.
 * </ul>
 * 
 * @see org.eclipse.birt.report.model.elements.ListingElement
 * @see SlotHandle
 */

public abstract class ListingHandle extends ReportItemHandle
		implements
			IListingElementModel
{

	/**
	 * Constructs a listing handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ListingHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the header slot. The header slot represents subsections that
	 * print at the start of the listing.
	 * 
	 * @return a handle to the header slot
	 */

	public SlotHandle getHeader( )
	{
		return getSlot( ListingElement.HEADER_SLOT );
	}

	/**
	 * Returns the footer slot. The footer slot represents subsections that
	 * print at the end of the listing.
	 * 
	 * @return a handle to the footer slot
	 */

	public SlotHandle getFooter( )
	{
		return getSlot( ListingElement.FOOTER_SLOT );
	}

	/**
	 * Returns the detail slot. The detail slot represents subsections that
	 * print for each row in the data set.
	 * 
	 * @return a handle to the detail slot
	 */

	public SlotHandle getDetail( )
	{
		return getSlot( ListingElement.DETAIL_SLOT );
	}

	/**
	 * Returns the group slot. The group slot represents the grouping levels
	 * within the report. Groups appear with the most general first, the most
	 * detailed last.
	 * 
	 * @return a handle to the group slot
	 */

	public SlotHandle getGroups( )
	{
		return getSlot( ListingElement.GROUP_SLOT );
	}

	/**
	 * Returns the iterator for sort list defined on a table or list. The
	 * element in the iterator is the corresponding <code>StructureHandle</code>
	 * that deal with a <code>SortKey</code> in the list.
	 * 
	 * @return the iterator for <code>SortKey</code> structure list defined on
	 *         a table or list.
	 */

	public Iterator sortsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ListingElement.SORT_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the iterator for filter list defined on a table or list. The
	 * element in the iterator is the corresponding <code>StructureHandle</code>
	 * that deal with a <code>FilterCond</code> in the list.
	 * 
	 * @return the iterator for <code>FilterCond</code> structure list defined
	 *         on a table or list.
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ListingElement.FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the script called before the first row is retrieved from the data
	 * set.
	 * 
	 * @return the script that executes
	 */

	public String getOnStart( )
	{
		return getStringProperty( ListingElement.ON_START_METHOD );
	}

	/**
	 * Sets the script called before the first row is retrieved from the data
	 * set.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOnStart( String value ) throws SemanticException
	{
		setProperty( ListingElement.ON_START_METHOD, value );
	}

	/**
	 * Returns the script called when one row retrieved from the data set.
	 * 
	 * @return the script that executes
	 */

	public String getOnRow( )
	{
		return getStringProperty( ListingElement.ON_ROW_METHOD );
	}

	/**
	 * Sets the script called when one row retrieved from the data set.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOnRow( String value ) throws SemanticException
	{
		setProperty( ListingElement.ON_ROW_METHOD, value );
	}

	/**
	 * Returns the script called after the last row is read from the data set.
	 * 
	 * @return the script that executes
	 */

	public String getOnFinish( )
	{
		return getStringProperty( ListingElement.ON_FINISH_METHOD );
	}

	/**
	 * Sets the script called after the last row is read from the data set.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOnFinish( String value ) throws SemanticException
	{
		setProperty( ListingElement.ON_FINISH_METHOD, value );
	}

	/**
	 * Returns the page break interval value
	 * 
	 * @return the page break interval value
	 */
	
	public int getPageBreakInterval( )
	{
		return getIntProperty( ListingElement.PAGE_BREAK_INTERVAL_PROP );
	}

	/**
	 * Sets the page break interval value
	 * 
	 * @param pageBreakInterval
	 *            the page break interval
	 * @throws SemanticException
	 *             if the parameter is not a valid integer
	 */
	
	public void setPageBreakInterval( int pageBreakInterval )
			throws SemanticException
	{
		setIntProperty( ListingElement.PAGE_BREAK_INTERVAL_PROP,
				pageBreakInterval );
	}

}
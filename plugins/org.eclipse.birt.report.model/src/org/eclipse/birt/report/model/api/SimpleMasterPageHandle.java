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
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;

/**
 * Represents a simple master page. The simple master page provides a header and
 * footer that appear on every page.
 *  
 */

public class SimpleMasterPageHandle extends MasterPageHandle
{

	/**
	 * Constructs the handle for a simple master page with the given design and
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public SimpleMasterPageHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Tests whether to show the page header on the first page of the report.
	 * 
	 * @return <code>true</code> if allows to show the header on the first
	 *         page.
	 */

	public boolean showHeaderOnFirst( )
	{
		return getBooleanProperty( SimpleMasterPage.SHOW_HEADER_ON_FIRST_PROP );
	}

	/**
	 * Changes the status to show page header on the first page or not.
	 * 
	 * @param showHeaderOnFirst
	 *            <code>true</code> if allow show header on the first page,
	 *            <code>false</code> otherwise.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setShowHeaderOnFirst( boolean showHeaderOnFirst )
			throws SemanticException
	{
		setProperty( SimpleMasterPage.SHOW_HEADER_ON_FIRST_PROP, Boolean
				.valueOf( showHeaderOnFirst ) );
	}

	/**
	 * Tests whether to show the page footer on the last page.
	 * 
	 * @return <code>true</code> if allows to show the footer on the last
	 *         page.
	 */

	public boolean showFooterOnLast( )
	{
		return getBooleanProperty( SimpleMasterPage.SHOW_FOOTER_ON_LAST_PROP );
	}

	/**
	 * Changes the status to show footer on the last page.
	 * 
	 * @param showFooterOnLast
	 *            <code>true</code> to allow to show footer on last page,
	 *            <code>false</code> otherwise.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setShowFooterOnLast( boolean showFooterOnLast )
			throws SemanticException
	{
		setProperty( SimpleMasterPage.SHOW_FOOTER_ON_LAST_PROP, Boolean
				.valueOf( showFooterOnLast ) );
	}

	/**
	 * Tests whether allows the footer ��floats�� after the last content on each
	 * page.
	 * 
	 * @return <code>true</code> if the simple master page allows floating
	 *         footer.
	 */

	public boolean isFloatingFooter( )
	{
		return getBooleanProperty( SimpleMasterPage.FLOATING_FOOTER );
	}

	/**
	 * Changes the status to say if it has a floating footer or not.
	 * 
	 * @param isFloatingFooter
	 *            <code>true</code> to allow the footer floating,
	 *            <code>false</code> not.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setFloatingFooter( boolean isFloatingFooter )
			throws SemanticException
	{
		setProperty( SimpleMasterPage.FLOATING_FOOTER, Boolean
				.valueOf( isFloatingFooter ) );
	}

	/**
	 * Returns the page header slot of this simple master page.
	 * 
	 * @return the page header slot handle.
	 */

	public SlotHandle getPageHeader( )
	{
		return getSlot( SimpleMasterPage.PAGE_HEADER_SLOT );
	}

	/**
	 * Returns the page footer slot of this simple master page.
	 * 
	 * @return the page footer slot handle.
	 */

	public SlotHandle getPageFooter( )
	{
		return getSlot( SimpleMasterPage.PAGE_FOOTER_SLOT );
	}

}
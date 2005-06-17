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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Records a change to the back reference of an element.
 * 
 * @see org.eclipse.birt.report.model.core.ReferenceableElement
 * @see org.eclipse.birt.report.model.core.ReferencableStructure
 */

abstract public class BackRefRecord extends SimpleRecord
{

	/**
	 * The element that refers to another element.
	 */

	protected DesignElement reference = null;

	/**
	 * The property name.
	 */

	protected String propName = null;

	/**
	 * Report design
	 */
	
	protected ReportDesign design = null;

	/**
	 * Constructor.
	 * 
	 * @param design
	 *            report design
	 * @param reference
	 *            the element that refers to another element.
	 * @param propName
	 *            the property name. The type of the property must be
	 *            <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *            must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *            <code>DesignElement.STYLE_PROP</code>
	 */

	public BackRefRecord( ReportDesign design, DesignElement reference,
			String propName )
	{
		this.design = design;
		this.reference = reference;
		this.propName = propName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations(boolean)
	 */
	public void sendNotifcations( boolean transactionStarted )
	{

	}
}
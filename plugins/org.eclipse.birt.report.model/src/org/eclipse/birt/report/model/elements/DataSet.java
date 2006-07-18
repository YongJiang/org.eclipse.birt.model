
package org.eclipse.birt.report.model.elements;

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

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;

/**
 * Abastract parent of simple data set and joint data set.
 * 
 */

public abstract class DataSet extends ReferenceableElement
		implements
			IDataSetModel
{

	/**
	 * Default constructor.
	 */

	public DataSet( )
	{
	}

	/**
	 * Constructs this data set by name.
	 * 
	 * @param name
	 *            of the data set.
	 */

	public DataSet( String name )
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#setDeliveryPath(org.eclipse.birt.report.model.activity.NotificationEvent)
	 */

	protected void adjustDeliveryPath( NotificationEvent ev )
	{
		ev.setDeliveryPath( NotificationEvent.ELEMENT_CLIENT );
	}

}

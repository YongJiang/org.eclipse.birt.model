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

import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Records setting the value of a structure member.
 * 
 */

public class MemberRecord extends SimpleRecord
{

	/**
	 * The design element that contains the property that contains the structure
	 * that contains the member.
	 */

	protected DesignElement element;

	/**
	 * Reference to the member.
	 */

	protected MemberRef memberRef;

	/**
	 * The structure that contains the member.
	 */

	protected Structure structure;

	/**
	 * The new value.
	 */

	protected Object newValue;

	/**
	 * The original value.
	 */

	protected Object oldValue;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the element that contains the property that contains the
	 *            structure that contains the member.
	 * @param ref
	 *            reference to the member to be changed
	 * @param value
	 *            new value for the member
	 */

	public MemberRecord( DesignElement obj, MemberRef ref, Object value )
	{
		element = obj;
		memberRef = ref;
		newValue = value;
		structure = memberRef.getStructure( null, element );
		oldValue = memberRef.getValue( null, element );

		label = ThreadResources.getMessage(
				MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[]{memberRef.getPropDefn( ).getDisplayName( )} );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		PropertyDefn prop = memberRef.getMemberDefn( );
		structure.setProperty( prop, undo ? oldValue : newValue );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		// Use the same notification for the done/redone and undone states.

		return new PropertyEvent( element, memberRef.getPropDefn( ).getName( ) );
	}

}

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
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
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
	 * The module of the element to be changed.
	 */

	protected Module module;

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
	 * @param module
	 *            the module
	 * @param obj
	 *            the element that contains the property that contains the
	 *            structure that contains the member.
	 * @param ref
	 *            reference to the member to be changed
	 * @param value
	 *            new value for the member
	 */

	public MemberRecord( Module module, DesignElement obj, MemberRef ref,
			Object value )
	{
		element = obj;
		memberRef = ref;
		newValue = value;
		assert module != null;
		structure = memberRef.getStructure( module, element );
		oldValue = memberRef.getLocalValue( module, element );

		label = ModelMessages.getMessage(
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
		if ( structure != null )
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations(boolean)
	 */

	public void sendNotifcations( boolean transactionStarted )
	{
		super.sendNotifcations( transactionStarted );

		// if the structure is referencable, then send notification to the
		// clients

		if ( structure != null && structure.isReferencable( ) )
		{
			ReferencableStructure refValue = (ReferencableStructure) structure;
			refValue.broadcast( getEvent( ) );
		}
	}
}
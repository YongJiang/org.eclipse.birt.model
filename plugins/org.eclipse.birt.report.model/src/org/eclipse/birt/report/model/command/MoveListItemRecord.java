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

import java.util.List;

import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Records the move of a structure within a property list or member list.
 *  
 */

public class MoveListItemRecord extends SimpleRecord
{

	/**
	 * The element that contains the list.
	 */

	protected DesignElement element = null;

	/**
	 * Reference to the item to move.
	 */

	protected MemberRef itemRef = null;

	/**
	 * The list that contains the item.
	 */

	protected List list = null;

	/**
	 * The old position of the item.
	 */

	protected int oldPosn = 0;

	/**
	 * The new position of the item.
	 */

	protected int newPosn = 0;

	/**
	 * Constructs a record to remove an item within a list to a new position.
	 * 
	 * @param obj
	 *            the element that contains the list
	 * @param ref
	 *            reference to the list.
	 * @param theList
	 *            the list that contains the item
	 * @param from
	 *            the old position of the item
	 * @param to
	 *            the new position of the item
	 */

	public MoveListItemRecord( DesignElement obj, MemberRef ref, List theList,
			int from, int to )
	{
		assert obj != null;
		assert ref != null;
		assert theList != null;

		assert from >= 0 && from < theList.size( );
		assert to >= 0 && to < theList.size( );

		assert obj.getPropertyDefn( ref.getPropDefn( ).getName( ) ) == ref
				.getPropDefn( );

		element = obj;
		itemRef = ref;
		list = theList;

		oldPosn = from;
		newPosn = to;

		label = ModelMessages.getMessage( MessageConstants.MOVE_ITEM_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		int from = undo ? newPosn : oldPosn;
		int to = undo ? oldPosn : newPosn;

		Object value = list.remove( from );
		list.add( to, value );
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
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		// Use the same notification for the done/redone and undone states.

		return new PropertyEvent( element, itemRef.getPropDefn( ).getName( ) );
	}

}
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Replaces one structure in the structure list property.
 * 
 */

public class PropertyReplaceRecord extends SimpleRecord
{

	/**
	 * The element which contains the property list.
	 */

	protected DesignElement element = null;

	/**
	 * Reference to the property list.
	 */

	protected MemberRef listRef = null;

	/**
	 * The old item to be replaced
	 */

	IStructure oldItem = null;

	/**
	 * The new item.
	 */

	IStructure newItem = null;

	/**
	 * The list itself.
	 */

	protected List list = null;

	/**
	 * The position of the old item in the structure list.
	 */

	private int position;

	/**
	 * Constructor for replacing an item within a list with a new structure.
	 * 
	 * @param obj
	 *            the design element which contains the structure list.
	 * @param ref
	 *            reference to the structure list
	 * @param theList
	 *            the structure list.
	 * @param posn
	 *            the position of the old item to be removed.
	 * @param newItem
	 *            the new item that will replace the old one.
	 */

	public PropertyReplaceRecord( DesignElement obj, MemberRef ref,
			List theList, int posn, IStructure newItem )
	{
		assert obj != null;
		assert ref != null;
		assert theList != null;
		assert ref.isListRef( );
		assert obj.getPropertyDefn( ref.getPropDefn( ).getName( ) ) == ref
				.getPropDefn( );

		this.element = obj;
		this.listRef = ref;
		this.list = theList;

		this.position = posn;

		this.oldItem = (IStructure) list.get( posn );
		this.newItem = newItem;

		assert oldItem != null && newItem != null;

		label = CommandLabelFactory
				.getCommandLabel( MessageConstants.REPLACE_ITEM_MESSAGE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.SimpleRecord#perform(boolean
	 * )
	 */
	protected void perform( boolean undo )
	{
		if ( undo )
		{
			StructureContext context = ( (Structure) newItem ).getContext( );
			context.remove( (Structure) newItem );

			context.add( position, (Structure) oldItem );
		}
		else
		{
			StructureContext context = ( (Structure) oldItem ).getContext( );
			context.remove( (Structure) oldItem );

			context.add( position, (Structure) newItem );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.AbstractElementRecord#getTarget
	 * ()
	 */
	public DesignElement getTarget( )
	{
		if ( eventTarget != null )
			return eventTarget.getElement( );

		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent( )
	{
		if ( eventTarget != null )
			return new PropertyEvent( eventTarget.getElement( ), eventTarget
					.getPropName( ) );

		return new PropertyEvent( element, listRef.getPropDefn( ).getName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.activity.ActivityRecord#getNotificationChain
	 * ()
	 */

	protected List getPostTasks( )
	{
		List retList = new ArrayList( );
		retList.addAll( super.getPostTasks( ) );

		retList.add( new NotificationRecordTask( element, getEvent( ) ) );

		// if the structure is referencable, then send notification to the
		// clients

		if ( oldItem != null && oldItem.isReferencable( ) )
		{
			ReferencableStructure refValue = (ReferencableStructure) oldItem;
			retList.add( new NotificationRecordTask( refValue, getEvent( ) ) );

		}

		return retList;
	}

}
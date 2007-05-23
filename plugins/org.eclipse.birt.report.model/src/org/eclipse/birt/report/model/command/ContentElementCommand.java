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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.api.activity.IEventFilter;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.activity.TransactionOption;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.command.ContentElementInfo.Step;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * 
 */

class ContentElementCommand extends AbstractContentCommand
{

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module
	 *            the module
	 * @param containerInfo
	 *            the container infor
	 */

	public ContentElementCommand( Module module, ContainerContext containerInfo )
	{
		super( module, containerInfo );
	}

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module
	 *            the root
	 * @param element
	 *            the element to set value
	 * @param eventTarget
	 *            the container for the element
	 * @param containerInfo
	 *            the container infor
	 */

	public ContentElementCommand( Module module, DesignElement element,
			ContentElementInfo eventTarget )
	{
		super( module, element );
		this.eventTarget = eventTarget;
	}

	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value,
	 * if it has, the method returns, otherwise, a copy of the list value
	 * inherited from container or parent will be set locally on the element
	 * itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element.
	 * This method will be called to ensure that a local copy will be made, so
	 * change to the child won't affect the original value in the parent.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 */

	private DesignElement makeLocalCompositeValue( DesignElement content )
	{
		String propName = focus.getPropertyName( );
		ElementPropertyDefn prop = element.getPropertyDefn( propName );

		return makeLocalCompositeValue( element, prop, content );
	}

	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value,
	 * if it has, the method returns, otherwise, a copy of the list value
	 * inherited from container or parent will be set locally on the element
	 * itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element.
	 * This method will be called to ensure that a local copy will be made, so
	 * change to the child won't affect the original value in the parent.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 */

	private DesignElement makeLocalCompositeValue( DesignElement topElement,
			ElementPropertyDefn prop, DesignElement content )
	{
		// Top level property is a list.

		ArrayList list = (ArrayList) topElement.getLocalProperty( module, prop );

		if ( list != null )
			return content;

		// Make a local copy of the inherited list value.

		ArrayList inherited = (ArrayList) topElement.getProperty( module, prop );

		// if the action is add, the inherited can be null.

		if ( inherited == null )
			return null;

		int index = -1;

		if ( content != null )
			index = inherited.indexOf( content );

		List value = (List) ModelUtil.copyValue( prop, inherited );
		ActivityStack activityStack = module.getActivityStack( );

		list = new ArrayList( );
		PropertyRecord propRecord = new PropertyRecord( topElement, prop, list );
		activityStack.execute( propRecord );

		ContainerContext context = new ContainerContext( topElement, prop
				.getName( ) );

		for ( int i = 0; i < value.size( ); i++ )
		{
			DesignElement tmpContent = (DesignElement) value.get( i );
			ContentRecord addRecord = new ContentRecord( module, context,
					tmpContent, i );
			activityStack.execute( addRecord );
		}

		if ( index != -1 )
			return (DesignElement) value.get( index );

		return null;
	}

	protected void doAdd( int newPos, DesignElement content )
			throws ContentException, NameException
	{
		ActivityStack stack = getActivityStack( );
		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.ADD_ELEMENT_MESSAGE ),
				getTransOption( ) );

		// for add action, the content parameter can be ignored.

		makeLocalCompositeValue( content );

		// add the element

		super.doAdd( newPos, content );

		stack.commit( );
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content
	 *            the content to remove
	 * @param unresolveReference
	 *            status whether to un-resolve the references
	 * @throws SemanticException
	 */

	protected void doDelectAction( DesignElement content )
			throws SemanticException
	{
		DesignElement toRemove = makeLocalCompositeValue( content );
		super.doDelectAction( toRemove );
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content
	 *            the content to remove
	 * @param unresolveReference
	 *            status whether to un-resolve the references
	 * @throws SemanticException
	 */

	protected void doMove( DesignElement content,
			ContainerContext toContainerInfor, int newPos )
			throws ContentException
	{
		ActivityStack stack = getActivityStack( );

		String label = ModelMessages
				.getMessage( MessageConstants.MOVE_ELEMENT_MESSAGE );
		stack.startTrans( label, getTransOption( ) );

		DesignElement toMove = makeLocalCompositeValue( content );
		super.doMove( toMove, toContainerInfor, newPos );

		stack.commit( );
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content
	 *            the content to remove
	 * @param unresolveReference
	 *            status whether to un-resolve the references
	 * @throws SemanticException
	 */

	protected void doMovePosition( DesignElement content, int newPosn )
			throws ContentException
	{
		// Skip the step if the slotID/propName has only single content.
		if ( !focus.isContainerMultipleCardinality( ) )
			return;

		ActivityStack stack = getActivityStack( );

		String label = ModelMessages
				.getMessage( MessageConstants.MOVE_ELEMENT_MESSAGE );
		stack.startTrans( label, getTransOption( ) );

		DesignElement toMove = makeLocalCompositeValue( content );
		super.doMovePosition( toMove, newPosn );

		stack.commit( );
	}

	/**
	 * The method to set property.
	 * 
	 * @param prop
	 *            the definition of the property to set.
	 * @param value
	 *            the new property value.
	 * @throws SemanticException
	 *             if the element is a template element and users try to set the
	 *             value of template definition to "null" or a non-exsiting
	 *             element
	 */

	protected void doSetProperty( ElementPropertyDefn prop, Object value )
			throws SemanticException
	{

		ActivityStack stack = getActivityStack( );

		TransactionOption options = getTransOption( );
		stack.startTrans( null, options );

		// for add action, the content parameter can be ignored.

		DesignElement tmpElement = copyTopCompositeValue( );

		PropertyRecord propRecord = new PropertyRecord( tmpElement, prop, value );
		getActivityStack( ).execute( propRecord );

		stack.commit( );
	}

	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value,
	 * if it has, the method returns, otherwise, a copy of the list value
	 * inherited from container or parent will be set locally on the element
	 * itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element.
	 * This method will be called to ensure that a local copy will be made, so
	 * change to the child won't affect the original value in the parent.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 */

	private DesignElement copyTopCompositeValue( ) throws SemanticException
	{
		if ( !( element instanceof ContentElement ) )
		{
			return null;
		}

		DesignElement topElement = eventTarget.getElement( );
		String propName = eventTarget.getPropName( );
		ElementPropertyDefn prop = topElement.getPropertyDefn( propName );

		makeLocalCompositeValue( topElement, prop, null );

		return matchElement( topElement );
	}

	private DesignElement matchElement( DesignElement topElement )
	{
		Iterator path = eventTarget.stepIterator( );

		DesignElement tmpElement = topElement;
		while ( path.hasNext( ) )
		{
			Step step = (Step) path.next( );
			PropertyDefn stepPropDefn = step.stepPropDefn;
			int index = step.index;

			Object stepValue = tmpElement.getLocalProperty( module,
					(ElementPropertyDefn) stepPropDefn );

			if ( stepPropDefn.isListType( ) )
			{
				tmpElement = (DesignElement) ( (List) stepValue ).get( index );
			}
		}

		return tmpElement;
	}

	/**
	 * @param ref
	 * @param value
	 * @throws SemanticException
	 */

	protected void addItem( MemberRef ref, Object value )
			throws SemanticException
	{
		ActivityStack stack = getActivityStack( );

		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.ADD_ITEM_MESSAGE ),
				getTransOption( ) );

		// for add action, the content parameter can be ignored.

		DesignElement tmpElement = copyTopCompositeValue( );

		List list = null;

		PropertyDefn propDefn = ref.getPropDefn( );
		list = (List) tmpElement.getLocalProperty( module,
				(ElementPropertyDefn) propDefn );

		PropertyListRecord record = new PropertyListRecord( tmpElement,
				new CachedMemberRef( ref, list.size( ) ), list, value );
		stack.execute( record );
		record.setEventTarget( eventTarget );

		stack.commit( );
	}

	/**
	 * @param ref
	 * @param value
	 * @throws SemanticException
	 */

	protected void removeItem( MemberRef ref ) throws SemanticException
	{
		ActivityStack stack = getActivityStack( );
		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.REMOVE_ITEM_MESSAGE ),
				getTransOption( ) );

		// for add action, the content parameter can be ignored.

		DesignElement tmpElement = copyTopCompositeValue( );

		List list = ref.getList( module, tmpElement );

		PropertyDefn propDefn = ref.getPropDefn( );
		if ( ref.getMemberDefn( ) != null )
			propDefn = ref.getMemberDefn( );

		Object value = list.get( ref.getIndex( ) );

		PropertyListRecord record = new PropertyListRecord( tmpElement, ref,
				list );
		stack.execute( record );
		record.setEventTarget( eventTarget );

		if ( value instanceof ElementRefValue )
		{
			ElementRefValue refValue = (ElementRefValue) value;
			if ( refValue.isResolved( ) )
			{
				ElementRefRecord refRecord = new ElementRefRecord( element,
						refValue.getTargetElement( ), propDefn.getName( ),
						false );
				stack.execute( refRecord );

			}
		}

		stack.commit( );
	}

	/**
	 * Returns the transaction option for the transaction in this command. ONLY
	 * ONE property event should be sent out.
	 * 
	 * @return
	 */

	private TransactionOption getTransOption( )
	{
		TransactionOption options = new TransactionOption( );
		options.setSendTime( TransactionOption.SELF_TRANSACTION_SEND_TIME );

		options.setEventfilter( new EventFilter( eventTarget.getElement( ),
				eventTarget.getPropName( ) ) );

		return options;
	}

	private static class EventFilter implements IEventFilter
	{

		private final NotificationEvent ev;

		private EventFilter( DesignElement target, String propName )
		{
			ev = new PropertyEvent( target, propName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.activity.IEventFilter#filter(java.util.List)
		 */

		public List filter( List events )
		{
			List retList = new ArrayList( );
			retList.add( new NotificationRecordTask( ev.getTarget( ), ev ) );
			return retList;
		}
	}
}
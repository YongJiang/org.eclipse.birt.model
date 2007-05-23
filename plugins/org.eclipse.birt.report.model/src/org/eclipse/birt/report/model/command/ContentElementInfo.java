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

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Describle where to send out the event.
 * 
 */

public class ContentElementInfo
{

	private DesignElement element;

	private PropertyDefn propDefn;

	private Stack path = null;

	private boolean enablePath = false;

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the element
	 * @param propDefn
	 *            the property definition
	 */

	public ContentElementInfo( DesignElement element, PropertyDefn propDefn )
	{
		this.element = element;
		this.propDefn = propDefn;
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the element
	 * @param propDefn
	 *            the property definition
	 */

	public ContentElementInfo( boolean enablePath )
	{
		this.enablePath = enablePath;
		path = new Stack( );
	}

	/**
	 * Returns the event destination.
	 * 
	 * @return the element
	 */

	public DesignElement getElement( )
	{
		if ( element != null )
			return element;

		return null;
	}

	/**
	 * Returns the property name of the target event.
	 * 
	 * @return the property name
	 */

	public String getPropName( )
	{
		if ( propDefn != null )
			return propDefn.getName( );

		if ( path.isEmpty( ) )
			return null;

		Step topStep = (Step) path.peek( );
		propDefn = topStep.stepPropDefn;
		return propDefn.getName( );

	}

	/**
	 * Adds one step.
	 * 
	 * @param stepPropDefn
	 * @param index
	 */

	public void pushStep( PropertyDefn stepPropDefn, int index )
	{
		if ( enablePath )
			path.push( new Step( stepPropDefn, index ) );
	}

	/**
	 * Sets the top container that is not content element.
	 * 
	 * @param topElement
	 */

	public void setTopElement( DesignElement topElement )
	{
		assert !( topElement instanceof ContentElement );
		element = topElement;
	}

	/**
	 * Returns the iterator for the steps.
	 * 
	 * @return
	 */
	
	public Iterator stepIterator( )
	{
		if ( path == null )
			return Collections.EMPTY_LIST.iterator( );

		return path.iterator( );
	}

	/**
	 * Copies the path from the target to this info.
	 * 
	 * @param target
	 */
	
	public void copyPath( ContentElementInfo target )
	{
		if ( target == null )
			return;

		path = target.path;
	}

	/**
	 * 
	 * 
	 */

	static class Step
	{

		protected PropertyDefn stepPropDefn;
		protected int index = -1;

		Step( PropertyDefn propDefn, int index )
		{
			this.stepPropDefn = propDefn;
			this.index = index;
		}
	}
}
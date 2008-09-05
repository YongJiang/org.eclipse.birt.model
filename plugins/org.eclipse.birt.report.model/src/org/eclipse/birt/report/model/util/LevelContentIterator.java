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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Iterate content elements within the given level.
 */

public class LevelContentIterator implements Iterator
{

	/**
	 * The maximal level.
	 */

	protected static final int MAX_LEVEL = Integer.MAX_VALUE;

	/**
	 * List of content elements.
	 */

	List elementContents = null;

	/**
	 * Current iteration position.
	 */

	protected int posn = 0;

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given <code>element</code>
	 * 
	 * @param module
	 * 
	 * @param element
	 *            the element to visit.
	 * @param level
	 *            the depth of elements to iterate
	 */

	public LevelContentIterator( Module module, DesignElement element, int level )
	{
		assert element != null;

		elementContents = new ArrayList( );
		buildContentsList( module, element, level );
	}

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given slot id of the given <code>element</code>
	 * 
	 * @param module
	 * 
	 * @param containerInfor
	 *            the container information to visit.
	 * @param level
	 *            the depth of elements to iterate.
	 */

	public LevelContentIterator( Module module, ContainerContext containerInfor,
			int level )
	{
		assert containerInfor != null;

		elementContents = new ArrayList( );

		buildContentsList( module, containerInfor, level );
	}

	/**
	 * Adds the content elements in the given container element into
	 * <code>elementContents</code>
	 * 
	 * @param element
	 *            the next element to build.
	 */

	private void buildContentsList( Module module, DesignElement element,
			int level )
	{
		if ( level < 0 || !element.getDefn( ).isContainer( ) )
			return;

		ElementDefn defn = (ElementDefn) element.getDefn( );
		// build slot
		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			buildContentsList( module, new ContainerContext( element, i ), level );
		}

		// build properties
		List properties = defn.getContents( );
		for ( int i = 0; i < properties.size( ); i++ )
		{
			buildContentsList( module, new ContainerContext( element,
					( (PropertyDefn) properties.get( i ) ).getName( ) ), level );
		}
	}

	/**
	 * Adds the content elements of the given slot in the given container
	 * element into <code>elementContents</code>
	 * 
	 * @param element
	 *            the next element to build.
	 * @param slotId
	 *            the slot id.
	 */

	private void buildContentsList( Module module,
			ContainerContext containerInfor, int level )
	{
		if ( level <= 0 )
			return;

		List contents = containerInfor.getContents( module );

		for ( Iterator iter = contents.iterator( ); iter.hasNext( ); )
		{
			DesignElement e = (DesignElement) iter.next( );
			elementContents.add( e );

			buildContentsList( module, e, level - 1 );
		}
	}

	/**
	 * Not allowed.
	 */

	public void remove( )
	{
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */

	public boolean hasNext( )
	{
		return posn < elementContents.size( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */

	public Object next( )
	{
		return elementContents.get( posn++ );
	}

}
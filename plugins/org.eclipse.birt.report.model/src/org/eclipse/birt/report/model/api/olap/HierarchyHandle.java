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

package org.eclipse.birt.report.model.api.olap;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents a Hierarchy.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Hierarchy
 */

public abstract class HierarchyHandle extends ReportElementHandle
		implements
			IHierarchyModel
{

	/**
	 * Constructs a handle for the given design and design element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public HierarchyHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Gets the count of the level elements within this hierarchy.
	 * 
	 * @return count of the level elements if set, otherwise 0
	 */
	public int getLevelCount( )
	{
		return getPropertyHandle( LEVELS_PROP ).getContentCount( );
	}

	/**
	 * Gets the level handle by the name within this hierarchy.
	 * 
	 * @param levelName
	 *            name of the level to find
	 * @return the level within this hierarchy if found, otherwise null
	 */
	public LevelHandle getLevel( String levelName )
	{
		DesignElement level = module.findOLAPElement( levelName );
		if ( level != null
				&& level.getDefn( ).isKindOf(
						MetaDataDictionary.getInstance( ).getElement(
								ReportDesignConstants.LEVEL_ELEMENT ) )
				&& level.isContentOf( getElement( ) ) )
			return (LevelHandle) level.getHandle( module );
		return null;
	}

	/**
	 * Gets the level handle at the specified position within this hierarchy.
	 * 
	 * @param index
	 *            0-based integer
	 * @return the level handle at the given index, <code>null</code> if index
	 *         is out of range
	 */
	public LevelHandle getLevel( int index )
	{
		return (LevelHandle) getPropertyHandle( LEVELS_PROP )
				.getContent( index );
	}

	/**
	 * Returns an iterator for the filter list defined on this hierarchy. Each
	 * object returned is of type <code>StructureHandle</code>.
	 * 
	 * @return the iterator for <code>FilterCond</code> structure list defined
	 *         on this hierarchy.
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns an iterator for the access controls. Each object returned is of
	 * type <code>AccessControlHandle</code>.
	 * 
	 * @return the iterator for user accesses defined on this cube.
	 */

	public Iterator accessControlsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ACCESS_CONTROLS_PROP );
		return propHandle.getContents( ).iterator( );
	}
}

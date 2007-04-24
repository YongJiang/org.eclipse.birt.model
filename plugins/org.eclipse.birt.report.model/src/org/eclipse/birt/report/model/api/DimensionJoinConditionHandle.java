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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;

/**
 * Represents a dimension join condition in the DimensionCondition. It defines
 * two keys for the cube and hierarchy join, one is from cube and another is
 * from hierarchy.
 */
public class DimensionJoinConditionHandle extends StructureHandle
{

	/**
	 * Constructs a dimension join condition handle with the given
	 * <code>SimpleValueHandle</code> and the index of the dimension join
	 * condition in the dimension condition.
	 * 
	 * @param valueHandle
	 *            handle to a list property or member
	 * @param index
	 *            index of the structure within the list
	 */

	public DimensionJoinConditionHandle( SimpleValueHandle valueHandle,
			int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Gets the cube key in this dimension join condition.
	 * 
	 * @return the cube key in this dimension join condition
	 */
	public String getCubeKey( )
	{
		return getStringProperty( DimensionJoinCondition.CUBE_KEY_MEMBER );
	}

	/**
	 * Sets the cube key in this dimension join condition.
	 * 
	 * @param cubeKey
	 *            the cube key to set
	 */
	public void setCubeKey( String cubeKey )
	{
		setPropertySilently( DimensionJoinCondition.CUBE_KEY_MEMBER, cubeKey );
	}

	/**
	 * Gets the hierarchy key in this dimension join condition.
	 * 
	 * @return the hierarchy key in this dimension join condition
	 */
	public String getHierarchyKey( )
	{
		return getStringProperty( DimensionJoinCondition.HIERARCHY_KEY_MEMBER );
	}

	/**
	 * Sets the hierarchy key in this dimension join condition.
	 * 
	 * @param hierarchyKey
	 *            the hierarchy key to set
	 */
	public void setHierarchyKey( String hierarchyKey )
	{
		setPropertySilently( DimensionJoinCondition.HIERARCHY_KEY_MEMBER,
				hierarchyKey );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( obj == this )
			return true;
		if ( !( obj instanceof DimensionJoinConditionHandle ) )
			return false;

		DimensionJoinConditionHandle temp = (DimensionJoinConditionHandle) obj;

		return ( temp.structRef.equals( this.structRef )
				&& temp.elementHandle.equals( this.elementHandle ) && temp.structRef
				.getIndex( ) == this.structRef.getIndex( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */

	public int hashCode( )
	{
		int hash = 17;

		hash = 37 * hash + this.elementHandle.hashCode( );
		hash = 37 * hash + this.structRef.hashCode( );
		hash = 37 * hash + this.structRef.getIndex( );
		return hash;
	}
}

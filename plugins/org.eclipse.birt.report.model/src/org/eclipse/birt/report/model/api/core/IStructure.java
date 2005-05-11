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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.core.IPropertySet;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.PropertyDefn;



/**
 * Interface for objects that appear in a property list. Provides methods
 * for generically accessing or updating object members, and provides a
 * meta-data definition for the object. This interface allows an object
 * to participate in the generic property type, command and related
 * mechanisms.
 *
 */

public interface IStructure extends IPropertySet
{
	/**
	 * Returns the name of the structure. The name is the one used to
	 * define the structure in the meta-data dictionary.
	 * 
	 * @return the internal name of the structure a defined in the
	 * meta-data dictionary.
	 */
	
	String getStructName( );
	
	/**
	 * Creates a deep copy of this structure.
	 * 
	 * @return a copy of this structure.
	 */
	
	IStructure copy( );
	
	/**
	 * Returns the structure definition from the meta-data dictionary.
	 * 
	 * @return the structure definition
	 */
	
	IStructureDefn getDefn( );
	
	/**
	 * Gets the locale value of a property.
	 * 
	 * @param design
	 *            the report design
	 * 
	 * @param propDefn
	 *            definition of the property to get
	 * @return value of the item as an object, or null if the item is not set
	 *         locally or is not found.
	 */

	public Object getLocalProperty( ReportDesign design, PropertyDefn propDefn );
}

/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Script wrapper of DesignElementHandle
 * 
 */
public interface IDesignElement
{

	/**
	 * Gets design element style.
	 * 
	 * @return style
	 */
	IStyle getStyle( );

	/**
	 * Gets the name of this element. The returned element name will be the same
	 * as <CODE>getName()</CODE>, plus the namespace of the module that the
	 * elment is contained, if any. If the element is existed in the current
	 * module,this method and <CODE>getName()</CODE> will return identical
	 * results.
	 * 
	 * @return the qualified name of thie element.
	 */

	String getQualifiedName( );

	/**
	 * Returns name of user property defined in this element.
	 * 
	 * @param name
	 * @return name of user property
	 */
	String getNamedExpression( String name );

	/**
	 * Sets name of user property defined in this element.
	 * 
	 * @param name
	 *            name of user property
	 * @param exp
	 *            name expression
	 * @throws SemanticException
	 */

	void setNamedExpression( String name, String exp ) throws SemanticException;

	/**
	 * Returns user property defined in this element.
	 * 
	 * @param name
	 * @return user property
	 */

	Object getUserProperty( String name );

	/**
	 * Sets user property defined in this element.
	 * 
	 * @param name
	 * @param value
	 * @throws SemanticException
	 */
	void setUserProperty( String name, String value ) throws SemanticException;

	/**
	 * Sets user property defined in this element.
	 * 
	 * @param name
	 * @param value
	 * @param type
	 * @throws SemanticException
	 */

	void setUserProperty( String name, Object value, String type )
			throws SemanticException;

	/**
	 * Return the parent of this element
	 * 
	 * @return the parent
	 */
	IDesignElement getParent( );
}

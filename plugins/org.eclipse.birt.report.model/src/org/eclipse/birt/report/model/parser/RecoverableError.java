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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;

/**
 * Handles recoverable errors during parsing the design file.
 */

class RecoverableError
{

	/**
	 * Handles property value exceptions with the given value exception and the
	 * property value.
	 * 
	 * @param handler
	 *            the handler for the design parser
	 * @param valueException
	 *            the exception thrown by the parser
	 */

	protected static void dealInvalidPropertyValue(
			ModuleParserHandler handler, PropertyValueException valueException )
	{
		Object retValue = valueException.getInvalidValue( );

		DesignElement element = valueException.getElement( );
		String propName = valueException.getPropertyName( );

		element.setProperty( propName, retValue );
		handler.semanticWarning( valueException );
	}

	/**
	 * Handles property value exceptions with the given value exception and the
	 * property value.
	 * 
	 * @param handler
	 *            the handler for the design parser
	 * @param valueException
	 *            the exception thrown by the parser
	 * @param structre
	 *            the structure that have this member value
	 * @param memberDefn
	 *            the member definition
	 */

	protected static void dealInvalidMemberValue( ModuleParserHandler handler,
			PropertyValueException valueException, IStructure structre,
			StructPropertyDefn memberDefn )
	{
		assert structre != null;

		Object retValue = valueException.getInvalidValue( );
		structre.setProperty( memberDefn, retValue );
		handler.semanticWarning( valueException );
	}

	/**
	 * Handles design parser exceptions with the given parser exception.
	 * 
	 * @param handler
	 *            the handler for the design parser
	 * @param exception
	 *            the design parser exception to record
	 */

	protected static void dealUndefinedProperty( ModuleParserHandler handler,
			DesignParserException exception )
	{
		handler.semanticWarning( exception );
	}

	/**
	 * Handles the semantic error when an extended item has a invalid extension.
	 * 
	 * @param handler
	 *            the handler for the design parser
	 * @param exception
	 *            the exception thrown by the parser
	 */

	protected static void dealMissingInvalidExtension(
			ModuleParserHandler handler, SemanticError exception )
	{
		handler.semanticWarning( exception );
	}

}
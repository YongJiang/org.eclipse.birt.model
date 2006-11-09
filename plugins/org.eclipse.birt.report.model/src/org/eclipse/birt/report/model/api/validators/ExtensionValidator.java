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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the extension is valid, which is provided by
 * <code>IReportItem</code>.
 * 
 * <h3>Rule</h3>
 * The rule is defined by
 * {@link org.eclipse.birt.report.model.api.extension.IReportItem#validate()}.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>TableItem</code>.
 */

public class ExtensionValidator extends AbstractElementValidator
{

	/**
	 * Name of this validator.
	 */

	public final static String NAME = "ExtensionValidator"; //$NON-NLS-1$

	private final static ExtensionValidator instance = new ExtensionValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static ExtensionValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether any cell in the given row overlaps others.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the row to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( Module module, DesignElement element )
	{
		if ( !( element instanceof ExtendedItem ) )
			return Collections.EMPTY_LIST;

		return doValidate( module, (ExtendedItem) element );
	}

	private List doValidate( Module module, ExtendedItem toValidate )
	{
		List list = new ArrayList( );

		// if the module is a library and not includded by any report, it is not
		// necessary to initialized it
		// by the validate. This method will be called by the parser in the end
		// document method. The library error information will not be displayed
		// in the report design console.
		if ( toValidate.getExtendedElement( ) == null )
		{
			if ( !( ( module instanceof Library ) && ( (Library) module )
					.getHost( ) != null ) )
			{
				try
				{
					toValidate.initializeReportItem( module );
				}
				catch ( ExtendedElementException e )
				{
					return list;
				}
			}
		}

		if ( toValidate.getExtendedElement( ) != null )
		{
			List exceptions = toValidate.getExtendedElement( ).validate( );

			if ( exceptions != null )
				list.addAll( exceptions );
		}
		return list;
	}
}
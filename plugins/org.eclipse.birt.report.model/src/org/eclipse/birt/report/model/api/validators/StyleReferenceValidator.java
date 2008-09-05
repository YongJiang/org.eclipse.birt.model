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

import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the style reference value for <code>StyledElement</code>. If the
 * value can refer to an actual style, it will be resolved after validation.
 * 
 * <h3>Rule</h3>
 * The rule is that the style reference value should refer to an actual style in
 * the same report.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to the <code>StyledElement.STYLE_PROP</code>
 * value of <code>StyledElement</code>.
 * 
 */

public class StyleReferenceValidator extends AbstractElementValidator
{

	/**
	 * Name of this validator.
	 */

	public static final String NAME = "StyleReferenceValidator"; //$NON-NLS-1$

	private final static StyleReferenceValidator instance = new StyleReferenceValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static StyleReferenceValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates the style reference value can refer to an actual style.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the styled element holding the style reference
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( Module module, DesignElement element )
	{
		if ( !( element instanceof StyledElement ) )
			return Collections.EMPTY_LIST;

		return doValidate( module, (StyledElement) element );
	}

	private List doValidate( Module module, StyledElement toValidate )
	{
		List list = new ArrayList( );

		String styleName = toValidate.getStyleName( );
		StyleElement style = toValidate.getStyle( );

		if ( styleName != null && style == null )
		{
			DesignElement resolvedElement = module
					.resolveElement( styleName, toValidate
							.getPropertyDefn( IStyledElementModel.STYLE_PROP ),
							null );

			// IModuleNameSpace nameSpace = module.getModuleNameSpace(
			// Module.STYLE_NAME_SPACE );
			// ElementRefValue refValue = nameSpace.resolve( styleName );

			// StyleElement theStyle = (StyleElement) nameSpace.getElement(
			// styleName );
			// NameSpace ns = module.getNameSpace( Module.STYLE_NAME_SPACE );
			// StyleElement theStyle = (StyleElement) ns.getElement( styleName
			// );
			if ( resolvedElement == null )
			{
				list.add( new StyleException( toValidate, styleName,
						StyleException.DESIGN_EXCEPTION_NOT_FOUND ) );
			}
			else
			{
				// toValidate.setStyle( (StyleElement) refValue.getElement( ) );
				toValidate.setStyle( (StyleElement) resolvedElement );
			}
		}

		return list;
	}
}
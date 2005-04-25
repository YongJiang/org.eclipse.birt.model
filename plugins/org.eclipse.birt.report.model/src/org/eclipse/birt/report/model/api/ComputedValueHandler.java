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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents the handler which processes the computed length value as CSS
 * specification describes.
 */

final class ComputedValueHandler extends CSSLengthValueHandler
{

	ComputedValueHandler( DimensionHandle dimensionHandle )
	{
		super( dimensionHandle );
	}


	/**
	 * Returns the absolute value for other length property, instead of font
	 * size.
	 * 
	 * @param relativeDimensionValue
	 *            the relative dimension value
	 * @return the absolute dimension value
	 */

	DimensionValue getAbsoluteValueForLength(
			DimensionValue relativeDimensionValue )
	{
		DimensionValue absoluteFontSizeValue = getDefaultFontSizeValue( );

		DimensionHandle factualFontSizeHanle = getFactualFontSizeHandle( );
		if ( factualFontSizeHanle != null )
		{
			DimensionValue absoluteFontSizeValueTemp = factualFontSizeHanle
					.getAbsoluteValue( );
			if ( absoluteFontSizeValueTemp != null )
			{
				absoluteFontSizeValue = absoluteFontSizeValueTemp;
			}
		}

		return computeRelativeValue( absoluteFontSizeValue,
				relativeDimensionValue );
	}

	/**
	 * Returns the font size dimension handle of the element which provides the
	 * factual font size.
	 * 
	 * @return the font size dimension handle
	 */

	private DimensionHandle getFactualFontSizeHandle( )
	{
		String unit = dimensionHandle.getUnits( );
		if ( !DesignChoiceConstants.UNITS_EM.equalsIgnoreCase( unit )
				&& !DesignChoiceConstants.UNITS_EX.equalsIgnoreCase( unit )
				&& !DesignChoiceConstants.UNITS_PERCENTAGE
						.equalsIgnoreCase( unit ) )
		{
			assert false;

			return dimensionHandle.elementHandle
					.getDimensionProperty( Style.FONT_SIZE_PROP );
		}

		Object propValue = null;
		ElementPropertyDefn fontSizePropDefn = (ElementPropertyDefn) MetaDataDictionary
				.getInstance( )
				.getElement( ReportDesignConstants.STYLE_ELEMENT ).getProperty(
						Style.FONT_SIZE_PROP );

		DesignElementHandle e = dimensionHandle.getElementHandle( );

		// Located the element which has the property this dimension represents.
		boolean computedPropertyFound = false;
		while ( e != null )
		{
			if ( !computedPropertyFound )
			{
				propValue = e.getElement( ).getPropertyFromElement(
						dimensionHandle.getDesign( ), dimensionHandle.propDefn );
				if ( propValue != null )
				{
					computedPropertyFound = true;
				}
			}

			if ( computedPropertyFound )
			{
				propValue = e.getElement( ).getPropertyFromElement(
						dimensionHandle.getDesign( ), fontSizePropDefn );
				if ( propValue != null )
					break;
			}

			// If the property this dimension represents can not be inherited.

			if ( !dimensionHandle.propDefn.canInherit( ) )
				break;

			e = e.getContainer( );
		}

		if ( e != null )
			return e.getDimensionProperty( Style.FONT_SIZE_PROP );

		return null;
	}

}
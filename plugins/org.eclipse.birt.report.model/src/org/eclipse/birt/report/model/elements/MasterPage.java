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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.Point;
import org.eclipse.birt.report.model.api.util.Rectangle;
import org.eclipse.birt.report.model.api.validators.MasterPageSizeValidator;
import org.eclipse.birt.report.model.api.validators.MasterPageTypeValidator;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;

/**
 * This class represents a Master Page element in the report design. This class
 * provides methods to access the most common properties. Use the
 * {@link org.eclipse.birt.report.model.api.MasterPageHandle}class to change
 * the properties.
 * 
 */

public abstract class MasterPage extends StyledElement implements IMasterPageModel
{


	/**
	 * Default constructor.
	 */

	public MasterPage( )
	{
	}

	/**
	 * Constructs the master page with a required name.
	 * 
	 * @param theName
	 *            the required name
	 */

	public MasterPage( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	abstract public void apply( ElementVisitor visitor );

	/**
	 * Returns the size of the page in application units. Considers the page
	 * type and orientation. If the page type is set to one of the standard
	 * sizes, then the height and width properties are ignored. Orientation
	 * affects standard sizes, but is ignored for custom sizes.
	 * 
	 * @param design
	 *            the report design
	 * @return the page size in application units
	 */

	public Point getSize( ReportDesign design )
	{
		// Determine height and width dimensions.

		Point size = new Point( );
		String type = getStringProperty( design, TYPE_PROP );
		String height = null;
		String width = null;

		if ( type.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_CUSTOM ) )
		{
			height = getStringProperty( design, HEIGHT_PROP );
			width = getStringProperty( design, WIDTH_PROP );
		}
		else if ( type
				.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_US_LETTER ) )
		{
			height = US_LETTER_HEIGHT;
			width = US_LETTER_WIDTH;
		}
		else if ( type
				.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_US_LEGAL ) )
		{
			height = US_LEGAL_HEIGHT;
			width = US_LEGAL_WIDTH;
		}
		else if ( type.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_A4 ) )
		{
			height = A4_HEIGHT;
			width = A4_WIDTH;
		}
		else
		{
			// Choice should have been validated.

			assert false;
			return size;
		}

		// Consider orientation for standard pages sizes, but not custom size.

		if ( type.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_CUSTOM )
				&& ( getStringProperty( design, ORIENTATION_PROP )
						.equalsIgnoreCase( DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE ) ) )
		{
			String temp = height;
			height = width;
			width = temp;
		}

		// Convert to application units.
		try
		{
			size.y = DimensionUtil.convertTo( height, design.getSession( ).getUnits( ),
					design.getSession( ).getUnits( ) ).getMeasure( );
			size.x = DimensionUtil.convertTo( width, design.getSession( ).getUnits( ),
					design.getSession( ).getUnits( ) ).getMeasure( );

		}
		catch ( PropertyValueException e )
		{
			// dimension value should have be validated.

			assert false;
		}

		return size;
	}

	/**
	 * Returns the content area rectangle in application units. The content area
	 * is the portion of the page after subtracting the four margins.
	 * 
	 * @param design
	 *            the report design
	 * @return the content area rectangle in application units
	 */

	public Rectangle getContentArea( ReportDesign design )
	{
		Point size = getSize( design );
		Rectangle margins = new Rectangle( );
		margins.y = getFloatProperty( design, TOP_MARGIN_PROP );
		margins.x = getFloatProperty( design, LEFT_MARGIN_PROP );
		margins.height = size.y - margins.y
				- getFloatProperty( design, BOTTOM_MARGIN_PROP );
		margins.width = size.x - margins.x
				- getFloatProperty( design, RIGHT_MARGIN_PROP );
		return margins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		List pageSizeErrors = MasterPageTypeValidator.getInstance( ).validate(
				design, this );
		if ( !pageSizeErrors.isEmpty( ) )
		{
			list.addAll( pageSizeErrors );
			return list;
		}

		list.addAll( MasterPageSizeValidator.getInstance( ).validate( design,
				this ) );

		return list;
	}

}

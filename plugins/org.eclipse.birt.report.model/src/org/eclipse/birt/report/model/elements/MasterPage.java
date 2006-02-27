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
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.Point;
import org.eclipse.birt.report.model.api.util.Rectangle;
import org.eclipse.birt.report.model.api.validators.MasterPageContextContainmentValidator;
import org.eclipse.birt.report.model.api.validators.MasterPageSizeValidator;
import org.eclipse.birt.report.model.api.validators.MasterPageTypeValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a Master Page element in the report design. This class
 * provides methods to access the most common properties. Use the
 * {@link org.eclipse.birt.report.model.api.MasterPageHandle}class to change
 * the properties.
 * 
 */

public abstract class MasterPage extends StyledElement
		implements
			IMasterPageModel
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
	 * @param module
	 *            the report design
	 * @return the page size in application units
	 */

	public Point getSize( Module module )
	{
		// Determine height and width dimensions.

		Point size = new Point( );
		String type = getStringProperty( module, TYPE_PROP );
		String height = null;
		String width = null;

		if ( type.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_CUSTOM ) )
		{
			height = getStringProperty( module, HEIGHT_PROP );
			width = getStringProperty( module, WIDTH_PROP );
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

		// Convert to application units.
		try
		{
			String sessionUnit = module.getSession( ).getUnits( );

			if ( height != null )
				size.y = DimensionUtil.convertTo( height, sessionUnit,
						sessionUnit ).getMeasure( );

			if ( width != null )
				size.x = DimensionUtil.convertTo( width, sessionUnit,
						sessionUnit ).getMeasure( );
		}
		catch ( PropertyValueException e )
		{
			// dimension value should have be validated.

			assert false;
		}

		return size;
	}

	/**
	 * Checks if current orientation type is "Landscape".
	 * 
	 * @param module
	 *            module
	 * @param type
	 *            master page type
	 * @return true if and only if the orientation type is "Landscape".
	 */
	public boolean isLandscape( Module module )
	{
		return ( !getStringProperty( module, TYPE_PROP ).equalsIgnoreCase(
				DesignChoiceConstants.PAGE_SIZE_CUSTOM ) )
				&& ( getStringProperty( module, ORIENTATION_PROP )
						.equalsIgnoreCase( DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE ) );
	}

	/**
	 * Checks if current page size type is DeisgnChoiceConstants..
	 * 
	 * @param module
	 *            module.
	 * @param type
	 *            master page type.
	 * @return true if and only if current page size type is "Customer".
	 */

	public boolean isCustomType( Module module )
	{
		return DesignChoiceConstants.PAGE_SIZE_CUSTOM
				.equalsIgnoreCase( getStringProperty( module, TYPE_PROP ) );
	}

	/**
	 * Returns the content area rectangle in application units. The content area
	 * is the portion of the page after subtracting the four margins.
	 * 
	 * @param module
	 *            the report design
	 * @return the content area rectangle in application units
	 */

	public Rectangle getContentArea( Module module )
	{
		Point size = getSize( module );
		Rectangle margins = new Rectangle( );
		margins.y = getFloatProperty( module, TOP_MARGIN_PROP );
		margins.x = getFloatProperty( module, LEFT_MARGIN_PROP );
		margins.height = size.y - margins.y
				- getFloatProperty( module, BOTTOM_MARGIN_PROP );
		margins.width = size.x - margins.x
				- getFloatProperty( module, RIGHT_MARGIN_PROP );
		return margins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( Module module )
	{
		List list = super.validate( module );

		List pageSizeErrors = MasterPageTypeValidator.getInstance( ).validate(
				module, this );
		if ( !pageSizeErrors.isEmpty( ) )
		{
			list.addAll( pageSizeErrors );
			return list;
		}

		list.addAll( MasterPageSizeValidator.getInstance( ).validate( module,
				this ) );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected List checkContent( Module module, DesignElement container,
			int slotId, DesignElement content )
	{
		List errors = super.checkContent( module, container, slotId, content );
		if ( !errors.isEmpty( ) )
			return errors;

		errors.addAll( MasterPageContextContainmentValidator.getInstance( )
				.validateForAdding( module, container, slotId, content ) );

		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.elements.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.metadata.IElementDefn)
	 */

	protected List checkContent( Module module, DesignElement container,
			int slotId, IElementDefn defn )
	{
		List errors = super.checkContent( module, container, slotId, defn );
		if ( !errors.isEmpty( ) )
			return errors;

		errors.addAll( MasterPageContextContainmentValidator.getInstance( )
				.validateForAdding( module, container, defn ) );

		return errors;
	}

	/**
	 * Returns the predefined height/width with the given property name, the
	 * orientation of the page and predefined values.
	 * 
	 * @param propName
	 *            the property name
	 * @param isLandScape
	 *            <code>true</code> if the page orientation is landscape.
	 *            Otherwise <code>false</code>.
	 * @param predefinedWidth
	 *            the predefined width
	 * @param predefinedHeight
	 *            the predefined height
	 * @return the height or width in <code>DimensionValue</code>.
	 * @throws PropertyValueException
	 *             if <code>predefinedWidth</code> or
	 *             <code>predefinedHeight</code> is not a valid dimension
	 *             value.
	 */

	private DimensionValue getPredefinedDimension( String propName,
			boolean isLandScape, String predefinedWidth, String predefinedHeight )
			throws PropertyValueException
	{
		if ( MasterPage.HEIGHT_PROP.equals( propName ) )
		{
			if ( !isLandScape )
				return DimensionValue.parse( predefinedHeight );

			return DimensionValue.parse( predefinedWidth );

		}

		if ( MasterPage.WIDTH_PROP.equals( propName ) )
		{
			if ( !isLandScape )
				return DimensionValue.parse( predefinedWidth );

			return DimensionValue.parse( predefinedHeight );
		}

		assert false;

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getProperty(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getProperty( Module module, ElementPropertyDefn prop )
	{
		String propName = prop.getName( );

		if ( MasterPage.HEIGHT_PROP.equals( propName )
				|| MasterPage.WIDTH_PROP.equals( propName ) )
		{
			String pageType = (String) getProperty( module, TYPE_PROP );

			// for the custom page, do not need the special function call.

			if ( DesignChoiceConstants.PAGE_SIZE_CUSTOM
					.equalsIgnoreCase( pageType ) )
				return super.getProperty( module, prop );

			boolean isLandScape = isLandscape( module );
			try
			{
				if ( DesignChoiceConstants.PAGE_SIZE_A4
						.equalsIgnoreCase( pageType ) )
				{
					return getPredefinedDimension( propName, isLandScape,
							MasterPage.A4_WIDTH, MasterPage.A4_HEIGHT );
				}
				else if ( DesignChoiceConstants.PAGE_SIZE_US_LEGAL
						.equalsIgnoreCase( pageType ) )
				{
					return getPredefinedDimension( propName, isLandScape,
							MasterPage.US_LEGAL_WIDTH,
							MasterPage.US_LEGAL_HEIGHT );
				}
				else if ( DesignChoiceConstants.PAGE_SIZE_US_LETTER
						.equalsIgnoreCase( pageType ) )
				{
					return getPredefinedDimension( propName, isLandScape,
							MasterPage.US_LETTER_WIDTH,
							MasterPage.US_LETTER_HEIGHT );
				}
			}
			catch ( PropertyValueException e )
			{
				assert false;
			}
		}

		return super.getProperty( module, prop );
	}
}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * This class represents a shared style.
 * 
 */

public class Style extends StyleElement implements IStyleModel
{

	private static Map cssDictionary = null;

	/**
	 * Default constructor.
	 */

	public Style( )
	{
	}

	/**
	 * Constructs the style element with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public Style( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitStyle( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.STYLE_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design of the style
	 * 
	 * @return an API handle for this element
	 */

	public SharedStyleHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new SharedStyleHandle( design, this );
		}
		return (SharedStyleHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		list.addAll( validateStyleProperties( design, this ) );

		return list;
	}

	/**
	 * Checks the style properties of style elements and styled elements.
	 * 
	 * @param design
	 *            the report design of the element
	 * @param element
	 *            the element to check
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	public static List validateStyleProperties( ReportDesign design,
			DesignElement element )
	{
		List list = new ArrayList( );

		List rules = element.getListProperty( design, HIGHLIGHT_RULES_PROP );
		if ( rules != null )
		{
			for ( int i = 0; i < rules.size( ); i++ )
			{
				list.addAll( ( (HighlightRule) rules.get( i ) ).validate(
						design, element ) );
			}
		}

		return list;
	}

	/**
	 * Returns the CSS style property name given style property name.
	 * <p>
	 * The CSS property name looks like "font-size", while style property name
	 * is "fontSize".
	 * 
	 * @param propName
	 *            the style property name
	 * @return the CSS property name. Return null, if the property name is not
	 *         style property name.
	 */

	public static String getCSSPropertyName( String propName )
	{
		if ( cssDictionary == null )
		{
			cssDictionary = new HashMap( );

			populateCSSDictionary( );
		}

		return (String) cssDictionary.get( propName );
	}

	/**
	 * Populates the CSS property dictionary which contains the mapping from
	 * style property name to CSS property name.
	 */

	private static void populateCSSDictionary( )
	{
		cssDictionary.put( Style.BACKGROUND_ATTACHMENT_PROP,
				DesignSchemaConstants.BACKGROUND_ATTACHMENT_ATTRIB );
		cssDictionary.put( Style.BACKGROUND_COLOR_PROP,
				DesignSchemaConstants.BACKGROUND_COLOR_ATTRIB );
		cssDictionary.put( Style.BACKGROUND_IMAGE_PROP,
				DesignSchemaConstants.BACKGROUND_IMAGE_ATTRIB );
		cssDictionary.put( Style.BACKGROUND_POSITION_X_PROP,
				DesignSchemaConstants.BACKGROUND_POSITION_X_ATTRIB );
		cssDictionary.put( Style.BACKGROUND_POSITION_Y_PROP,
				DesignSchemaConstants.BACKGROUND_POSITION_Y_ATTRIB );
		cssDictionary.put( Style.BACKGROUND_REPEAT_PROP,
				DesignSchemaConstants.BACKGROUND_REPEAT_ATTRIB );

		cssDictionary.put( Style.BORDER_BOTTOM_COLOR_PROP,
				DesignSchemaConstants.BORDER_BOTTOM_COLOR_ATTRIB );
		cssDictionary.put( Style.BORDER_BOTTOM_STYLE_PROP,
				DesignSchemaConstants.BORDER_BOTTOM_STYLE_ATTRIB );
		cssDictionary.put( Style.BORDER_BOTTOM_WIDTH_PROP,
				DesignSchemaConstants.BORDER_BOTTOM_WIDTH_ATTRIB );
		cssDictionary.put( Style.BORDER_LEFT_COLOR_PROP,
				DesignSchemaConstants.BORDER_LEFT_COLOR_ATTRIB );
		cssDictionary.put( Style.BORDER_LEFT_STYLE_PROP,
				DesignSchemaConstants.BORDER_LEFT_STYLE_ATTRIB );
		cssDictionary.put( Style.BORDER_LEFT_WIDTH_PROP,
				DesignSchemaConstants.BORDER_BOTTOM_WIDTH_ATTRIB );
		cssDictionary.put( Style.BORDER_RIGHT_COLOR_PROP,
				DesignSchemaConstants.BORDER_RIGHT_COLOR_ATTRIB );
		cssDictionary.put( Style.BORDER_RIGHT_STYLE_PROP,
				DesignSchemaConstants.BORDER_BOTTOM_STYLE_ATTRIB );
		cssDictionary.put( Style.BORDER_RIGHT_WIDTH_PROP,
				DesignSchemaConstants.BORDER_BOTTOM_WIDTH_ATTRIB );
		cssDictionary.put( Style.BORDER_TOP_COLOR_PROP,
				DesignSchemaConstants.BORDER_TOP_COLOR_ATTRIB );
		cssDictionary.put( Style.BORDER_TOP_STYLE_PROP,
				DesignSchemaConstants.BORDER_TOP_STYLE_ATTRIB );
		cssDictionary.put( Style.BORDER_TOP_WIDTH_PROP,
				DesignSchemaConstants.BORDER_TOP_WIDTH_ATTRIB );

		cssDictionary.put( Style.CAN_SHRINK_PROP,
				DesignSchemaConstants.CAN_SHRINK_ATTRIB );
		cssDictionary
				.put( Style.COLOR_PROP, DesignSchemaConstants.COLOR_ATTRIB );
		cssDictionary.put( Style.DISPLAY_PROP,
				DesignSchemaConstants.SECTION_DISPLAY_ATTRIB );

		cssDictionary.put( Style.FONT_FAMILY_PROP,
				DesignSchemaConstants.FONT_FAMILY_ATTRIB );
		cssDictionary.put( Style.FONT_SIZE_PROP,
				DesignSchemaConstants.FONT_SIZE_ATTRIB );
		cssDictionary.put( Style.FONT_STYLE_PROP,
				DesignSchemaConstants.FONT_STYLE_ATTRIB );
		cssDictionary.put( Style.FONT_VARIANT_PROP,
				DesignSchemaConstants.FONT_VARIANT_ATTRIB );
		cssDictionary.put( Style.FONT_WEIGHT_PROP,
				DesignSchemaConstants.FONT_WEIGHT_ATTRIB );

		cssDictionary.put( Style.LETTER_SPACING_PROP,
				DesignSchemaConstants.TEXT_LETTER_SPACING_ATTRIB );
		cssDictionary.put( Style.LINE_HEIGHT_PROP,
				DesignSchemaConstants.TEXT_LINE_HEIGHT_ATTRIB );

		cssDictionary.put( Style.MARGIN_BOTTOM_PROP,
				DesignSchemaConstants.MARGIN_BOTTOM_ATTRIB );
		cssDictionary.put( Style.MARGIN_LEFT_PROP,
				DesignSchemaConstants.MARGIN_LEFT_ATTRIB );
		cssDictionary.put( Style.MARGIN_RIGHT_PROP,
				DesignSchemaConstants.MARGIN_RIGHT_ATTRIB );
		cssDictionary.put( Style.MARGIN_TOP_PROP,
				DesignSchemaConstants.MARGIN_TOP_ATTRIB );

		cssDictionary.put( Style.ORPHANS_PROP,
				DesignSchemaConstants.TEXT_ORPHANS_ATTRIB );

		cssDictionary.put( Style.PADDING_BOTTOM_PROP,
				DesignSchemaConstants.PADDING_BOTTOM_ATTRIB );
		cssDictionary.put( Style.PADDING_LEFT_PROP,
				DesignSchemaConstants.PADDING_LEFT_ATTRIB );
		cssDictionary.put( Style.PADDING_RIGHT_PROP,
				DesignSchemaConstants.PADDING_RIGHT_ATTRIB );
		cssDictionary.put( Style.PADDING_TOP_PROP,
				DesignSchemaConstants.PADDING_TOP_ATTRIB );

		cssDictionary.put( Style.PAGE_BREAK_AFTER_PROP,
				DesignSchemaConstants.SECTION_PAGE_BREAK_AFTER_ATTRIB );
		cssDictionary.put( Style.PAGE_BREAK_BEFORE_PROP,
				DesignSchemaConstants.SECTION_PAGE_BREAK_BEFORE_ATTRIB );
		cssDictionary.put( Style.PAGE_BREAK_INSIDE_PROP,
				DesignSchemaConstants.SECTION_PAGE_BREAK_INSIDE_ATTRIB );

		cssDictionary.put( Style.SHOW_IF_BLANK_PROP,
				DesignSchemaConstants.SECTION_SHOW_LF_BLANK_ATTRIB );

		cssDictionary.put( Style.TEXT_ALIGN_PROP,
				DesignSchemaConstants.TEXT_ALIGN_ATTRIB );
		cssDictionary.put( Style.TEXT_INDENT_PROP,
				DesignSchemaConstants.TEXT_INDENT_ATTRIB );
		cssDictionary.put( Style.TEXT_LINE_THROUGH_PROP,
				DesignSchemaConstants.TEXT_LINE_THROUGH_ATTRIB );
		cssDictionary.put( Style.TEXT_OVERLINE_PROP,
				DesignSchemaConstants.TEXT_OVERLINE_ATTRIB );
		cssDictionary.put( Style.TEXT_TRANSFORM_PROP,
				DesignSchemaConstants.TEXT_TRANSFORM_ATTRIB );
		cssDictionary.put( Style.TEXT_UNDERLINE_PROP,
				DesignSchemaConstants.TEXT_UNDERLINE_ATTRIB );
		cssDictionary.put( Style.VERTICAL_ALIGN_PROP,
				DesignSchemaConstants.TEXT_VERTICAL_ALIGN_ATTRIB );
		cssDictionary.put( Style.WHITE_SPACE_PROP,
				DesignSchemaConstants.TEXT_WHITE_SPACE_ATTRIB );
		cssDictionary.put( Style.WIDOWS_PROP,
				DesignSchemaConstants.TEXT_WIDOWS_ATTRIB );
		cssDictionary.put( Style.WORD_SPACING_PROP,
				DesignSchemaConstants.TEXT_WORD_SPACING_ATTRIB );
	}
}

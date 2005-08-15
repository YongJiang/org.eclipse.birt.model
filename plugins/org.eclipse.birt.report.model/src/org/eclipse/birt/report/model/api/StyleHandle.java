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

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents the style properties for either a shared style or an element with
 * a style. A style is defined by a name and a set of style property values.
 * Most styles define values for a small subset of possible values.
 * <p>
 * A style includes a collection of properties such as font face name, font
 * color, fill color and so on. A property is simply a (name, value) pair. The
 * name identifies the property, and the value is what has been set for the
 * property: ��red�� or ��Arial�� or ��10 pt.�� A property value can be blank, meaning
 * that the user has not specified anything for that property.
 * <p>
 * Each style has a highlight. Each highlight rule has a condition and a set of
 * formatting options to apply if the rule is true. BIRT evaluates each rule in
 * term, and applies the first one that evaluates to true. As a result, the
 * rules need not be mutually exclusive, and the order of the rules matters.
 * <p>
 * Each style has a map. The map has a condition and a set of rules. A map rule
 * transforms a value in the input into a different value for display. It works
 * best for fields with a limited set of values, such as converting ��Y�� to ��Yes��
 * and ��N�� to ��No��. Mappings with many rules are better handled in the data
 * access layer. Another common use of mapping is to convert a null value into a
 * display value, such as ��No Data.��
 * 
 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
 */

public abstract class StyleHandle extends ReportElementHandle
		implements
			IStyleModel
{

	/**
	 * Constructs a style handle with the given design and the element.. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public StyleHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns a background attachment as a string. The return value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>BACKGROUND_ATTACHMENT_SCROLL
	 * <li>BACKGROUND_ATTACHMENT_FIXED
	 * </ul>
	 * 
	 * @return the background attachment
	 */

	public String getBackgroundAttachment( )
	{
		return getStringProperty( Style.BACKGROUND_ATTACHMENT_PROP );
	}

	/**
	 * Sets the background attachment. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>BACKGROUND_ATTACHMENT_SCROLL
	 * <li>BACKGROUND_ATTACHMENT_FIXED
	 * </ul>
	 * 
	 * @param value
	 *            the new background attachment
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setBackgroundAttachment( String value )
			throws SemanticException
	{
		setStringProperty( Style.BACKGROUND_ATTACHMENT_PROP, value );
	}

	/**
	 * Returns the address of the background image.
	 * 
	 * @return the address of the background image as a string
	 */

	public String getBackgroundImage( )
	{
		return getStringProperty( Style.BACKGROUND_IMAGE_PROP );
	}

	/**
	 * Sets the address of the background image. The value is a URL as a string.
	 * 
	 * @param value
	 *            the new background image address
	 * @throws SemanticException
	 *             if the property is locked
	 */

	public void setBackgroundImage( String value ) throws SemanticException
	{
		setStringProperty( Style.BACKGROUND_IMAGE_PROP, value );
	}

	/**
	 * Returns the pattern of the repeat for a background image. The return
	 * value is defined in <code>DesignChoiceConstants</code> and can be one
	 * of:
	 * <ul>
	 * <li>BACKGROUND_REPEAT_REPEAT
	 * <li>BACKGROUND_REPEAT_REPEAT_X
	 * <li>BACKGROUND_REPEAT_REPEAT_Y
	 * <li>BACKGROUND_REPEAT_NO_REPEAT
	 * </ul>
	 * 
	 * @return the repeat pattern
	 */

	public String getBackgroundRepeat( )
	{
		return getStringProperty( Style.BACKGROUND_REPEAT_PROP );
	}

	/**
	 * Sets the repeat pattern for a background image. The input value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>BACKGROUND_REPEAT_REPEAT
	 * <li>BACKGROUND_REPEAT_REPEAT_X
	 * <li>BACKGROUND_REPEAT_REPEAT_Y
	 * <li>BACKGROUND_REPEAT_NO_REPEAT
	 * </ul>
	 * 
	 * @param value
	 *            the new repeat pattern
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setBackgroundRepeat( String value ) throws SemanticException
	{
		setStringProperty( Style.BACKGROUND_REPEAT_PROP, value );
	}

	/**
	 * Returns the style of the bottom line of the border. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the bottom line
	 */

	public String getBorderBottomStyle( )
	{
		return getStringProperty( Style.BORDER_BOTTOM_STYLE_PROP );
	}

	/**
	 * Sets the style of the bottom line of the border. The input value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new style of the bottom line
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setBorderBottomStyle( String value ) throws SemanticException
	{
		setStringProperty( Style.BORDER_BOTTOM_STYLE_PROP, value );
	}

	/**
	 * Returns the style of the left line of the border. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the left line
	 */

	public String getBorderLeftStyle( )
	{
		return getStringProperty( Style.BORDER_LEFT_STYLE_PROP );
	}

	/**
	 * Sets the style of the left line of the border. The input value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new style of the left line
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setBorderLeftStyle( String value ) throws SemanticException
	{
		setStringProperty( Style.BORDER_LEFT_STYLE_PROP, value );
	}

	/**
	 * Returns the style of the right line of the border. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the right line
	 */

	public String getBorderRightStyle( )
	{
		return getStringProperty( Style.BORDER_RIGHT_STYLE_PROP );
	}

	/**
	 * Sets the style of the right line of the border. The input value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new style of the right line
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setBorderRightStyle( String value ) throws SemanticException
	{
		setStringProperty( Style.BORDER_RIGHT_STYLE_PROP, value );
	}

	/**
	 * Returns the style of the top line of the border. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the top line
	 */

	public String getBorderTopStyle( )
	{
		return getStringProperty( Style.BORDER_TOP_STYLE_PROP );
	}

	/**
	 * Sets the style of the top line of the border. The input value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new style of the right line
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setBorderTopStyle( String value ) throws SemanticException
	{
		setStringProperty( Style.BORDER_TOP_STYLE_PROP, value );
	}

	/**
	 * Tests whether the section can shrink if the actual content is smaller
	 * than the design size.
	 * 
	 * @return <code>true</code> if can shrink, otherwise <code>false</code>
	 * @see #setCanShrink(boolean)
	 */

	public boolean canShrink( )
	{
		return getBooleanProperty( Style.CAN_SHRINK_PROP );
	}

	/**
	 * Sets whether the section can shrink if the actual content is smaller than
	 * the design size.
	 * 
	 * @param value
	 *            <code>true</code> if can shrink, <code>false</code> not.
	 * @throws SemanticException
	 *             if the property is locked
	 * @see #canShrink()
	 */

	public void setCanShrink( boolean value ) throws SemanticException
	{
		setProperty( Style.CAN_SHRINK_PROP, Boolean.valueOf( value ) );
	}

	/**
	 * Returns the pattern of a string format.
	 * 
	 * @return the pattern of a string format
	 */

	public String getStringFormat( )
	{
		Object value = getProperty( Style.STRING_FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof StringFormatValue;

		return ( (StringFormatValue) value ).getPattern( );
	}

	/**
	 * Returns the category of a string format.
	 * 
	 * @return the category of a string format
	 */

	public String getStringFormatCategory( )
	{
		Object value = getProperty( Style.STRING_FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof StringFormatValue;

		return ( (StringFormatValue) value ).getCategory( );
	}

	/**
	 * Sets the pattern of a string format.
	 * 
	 * @param pattern
	 *            the pattern of a string forma
	 * @throws SemanticException
	 *             if the property is locked
	 */

	public void setStringFormat( String pattern ) throws SemanticException
	{
		setFormatValue( Style.STRING_FORMAT_PROP, FormatValue.PATTERN_MEMBER,
				pattern );
	}

	/**
	 * Sets the category of a string format. The <code>pattern</code> can be
	 * one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER</code>
	 * <li>
	 * <code>DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER</code>
	 * </ul>
	 * 
	 * @param pattern
	 *            the category of a string format
	 * @throws SemanticException
	 *             if <code>pattern</code> is not one of the above values.
	 */

	public void setStringFormatCategory( String pattern )
			throws SemanticException
	{
		setFormatValue( Style.STRING_FORMAT_PROP, FormatValue.CATEGORY_MEMBER,
				pattern );
	}

	/**
	 * Returns the pattern of a number format for a style.
	 * 
	 * @return the pattern of a number format
	 */

	public String getNumberFormat( )
	{
		Object value = getProperty( Style.NUMBER_FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof NumberFormatValue;

		return ( (NumberFormatValue) value ).getPattern( );
	}

	/**
	 * Returns the category of a number format for a style.
	 * 
	 * @return the category of a number format
	 */

	public String getNumberFormatCategory( )
	{
		Object value = getProperty( Style.NUMBER_FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof NumberFormatValue;

		return ( (NumberFormatValue) value ).getCategory( );
	}

	/**
	 * Sets the pattern of a number format.
	 * 
	 * @param pattern
	 *            the pattern of a number format
	 * @throws SemanticException
	 *             if the property is locked
	 */

	public void setNumberFormat( String pattern ) throws SemanticException
	{
		setFormatValue( Style.NUMBER_FORMAT_PROP, FormatValue.PATTERN_MEMBER,
				pattern );
	}

	/**
	 * Sets the category of a number format for a highlight rule. The
	 * <code>pattern</code> can be one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM</code>
	 * </ul>
	 * 
	 * @param category
	 *            the category of a number format
	 * @throws SemanticException
	 *             if <code>category</code> is not one of the above values.
	 */

	public void setNumberFormatCategory( String category )
			throws SemanticException
	{
		setFormatValue( Style.NUMBER_FORMAT_PROP, FormatValue.CATEGORY_MEMBER,
				category );
	}

	/**
	 * Returns the pattern of the date-time-format.
	 * 
	 * @return the pattern of the date-time-format
	 */

	public String getDateTimeFormat( )
	{
		Object value = getProperty( Style.DATE_TIME_FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof DateTimeFormatValue;

		return ( (DateTimeFormatValue) value ).getPattern( );
	}

	/**
	 * Returns the category of the date-time-format.
	 * 
	 * @return the category of the date-time-format
	 */

	public String getDateTimeFormatCategory( )
	{
		Object value = getProperty( Style.DATE_TIME_FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof DateTimeFormatValue;

		return ( (DateTimeFormatValue) value ).getCategory( );
	}

	/**
	 * Sets the pattern of a date time format for a highlight rule.
	 * 
	 * @param pattern
	 *            the pattern of a date time format
	 * @throws SemanticException
	 *             if the property is locked
	 */

	public void setDateTimeFormat( String pattern ) throws SemanticException
	{
		setFormatValue( Style.DATE_TIME_FORMAT_PROP,
				FormatValue.PATTERN_MEMBER, pattern );
	}

	/**
	 * Sets the category of a number format. The <code>pattern</code> can be
	 * one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM</code>
	 * </ul>
	 * 
	 * @param pattern
	 *            the category of a date-time format
	 * @throws SemanticException
	 *             if <code>pattern</code> is not one of the above values.
	 */

	public void setDateTimeFormatCategory( String pattern )
			throws SemanticException
	{
		setFormatValue( Style.DATE_TIME_FORMAT_PROP,
				FormatValue.CATEGORY_MEMBER, pattern );
	}

	/**
	 * Sets the category/pattern value for a string/number/date-time format.
	 * 
	 * @param propName
	 *            the property name
	 * @param memberName
	 *            the category or pattern member name
	 * @param valueToSet
	 *            the value to set
	 * @throws SemanticException
	 *             if the category is not one of BIRT defined.
	 */

	private void setFormatValue( String propName, String memberName,
			String valueToSet ) throws SemanticException
	{
		DesignElement element = getElement( );
		Object value = element.getLocalProperty( design, propName );

		if ( value == null )
		{
			FormatValue formatValueToSet = null;

			if ( Style.DATE_TIME_FORMAT_PROP.equalsIgnoreCase( propName ) )
				formatValueToSet = new DateTimeFormatValue( );
			else if ( Style.NUMBER_FORMAT_PROP.equalsIgnoreCase( propName ) )
				formatValueToSet = new NumberFormatValue( );
			else if ( Style.STRING_FORMAT_PROP.equalsIgnoreCase( propName ) )
				formatValueToSet = new StringFormatValue( );
			else
				assert false;

			if ( FormatValue.CATEGORY_MEMBER.equalsIgnoreCase( memberName ) )
				formatValueToSet.setCategory( valueToSet );
			else if ( FormatValue.PATTERN_MEMBER.equalsIgnoreCase( memberName ) )
				formatValueToSet.setPattern( valueToSet );
			else
				assert false;

			setProperty( propName, formatValueToSet );
		}
		else
		{
			PropertyHandle propHandle = getPropertyHandle( propName );
			FormatValue formatValueToSet = (FormatValue) value;

			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet
					.getHandle( propHandle );

			if ( FormatValue.CATEGORY_MEMBER.equalsIgnoreCase( memberName ) )
				formatHandle.setCategory( valueToSet );
			else if ( FormatValue.PATTERN_MEMBER.equalsIgnoreCase( memberName ) )
				formatHandle.setPattern( valueToSet );
			else
				assert false;
		}
	}

	/**
	 * Returns the value that specifies if a top-level element should be a block
	 * or in-line element. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>DISPLAY_NONE</code>
	 * <li><code>DISPLAY_INLINE</code>
	 * <li><code>DISPLAY_BLOCK</code>
	 * </ul>
	 * 
	 * @return the display value as a string
	 */

	public String getDisplay( )
	{
		return getStringProperty( Style.DISPLAY_PROP );
	}

	/**
	 * Sets the value that specifies if a top-level element should be a block or
	 * in-line element. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>DISPLAY_NONE</code>
	 * <li><code>DISPLAY_INLINE</code>
	 * <li><code>DISPLAY_BLOCK</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new display value
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setDisplay( String value ) throws SemanticException
	{
		setStringProperty( Style.DISPLAY_PROP, value );
	}

	/**
	 * Returns the name of the master page on which to start this section.
	 * 
	 * @return the master page name
	 * @see #setMasterPage(String)
	 */

	public String getMasterPage( )
	{
		return getStringProperty( Style.MASTER_PAGE_PROP );
	}

	/**
	 * Sets the master page name on which to start this section. If blank, the
	 * normal page sequence is used. If defined, the section starts on a new
	 * page, and the master page is the one defined here. The subsequent pages
	 * are those defined by the report��s page sequence.
	 * 
	 * @param value
	 *            the new master page name
	 * @throws SemanticException
	 *             if the property is locked
	 * @see #getMasterPage()
	 */

	public void setMasterPage( String value ) throws SemanticException
	{
		setProperty( Style.MASTER_PAGE_PROP, value );
	}

	/**
	 * Returns the value of orphans. The return value is either an integer as as
	 * string or one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>ORPHANS_INHERIT</code>
	 * </ul>
	 * 
	 * @return the orphans property
	 * @see #setOrphans(String)
	 */

	public String getOrphans( )
	{
		return getStringProperty( Style.ORPHANS_PROP );
	}

	/**
	 * Sets the orphans property. A orphan occurs if the first line of a
	 * multi-line paragraph appears on its own at the bottom of a page due to a
	 * page break. The input value is either an integer as as string or one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>ORPHANS_INHERIT</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new orphans property
	 * @throws SemanticException
	 *             if the value is not an integer or one of the above constants.
	 * @see #getOrphans()
	 */

	public void setOrphans( String value ) throws SemanticException
	{
		setStringProperty( Style.ORPHANS_PROP, value );
	}

	/**
	 * Returns the page break after property for block-level elements. The
	 * return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @return the page break after property
	 */

	public String getPageBreakAfter( )
	{
		return getStringProperty( Style.PAGE_BREAK_AFTER_PROP );
	}

	/**
	 * Sets the page break after property for block-level elements. The input
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new page break after property
	 * @throws SemanticException
	 *             if the value is not pre-defined.
	 */

	public void setPageBreakAfter( String value ) throws SemanticException
	{
		setStringProperty( Style.PAGE_BREAK_AFTER_PROP, value );
	}

	/**
	 * Returns the page break before property for block-level elements. The
	 * return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @return the page break before property
	 */

	public String getPageBreakBefore( )
	{
		return getStringProperty( Style.PAGE_BREAK_BEFORE_PROP );
	}

	/**
	 * Sets the page break before property for block-level elements. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new page break before property
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setPageBreakBefore( String value ) throws SemanticException
	{
		setStringProperty( Style.PAGE_BREAK_BEFORE_PROP, value );
	}

	/**
	 * Returns the page break inside property for block-level elements. The
	 * return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGEBREAK_INSIDE_AVOID</code>
	 * <li><code>PAGEBREAK_INSIDE_AUTO</code>
	 * <li><code>PAGEBREAK_INSIDE_INHERIT</code>
	 * </ul>
	 * 
	 * @return the page break inside property
	 */

	public String getPageBreakInside( )
	{
		return getStringProperty( Style.PAGE_BREAK_INSIDE_PROP );
	}

	/**
	 * Sets the page break inside property for block-level elements. The input
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGEBREAK_INSIDE_AVOID</code>
	 * <li><code>PAGEBREAK_INSIDE_AUTO</code>
	 * <li><code>PAGEBREAK_INSIDE_INHERIT</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new page break inside property
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setPageBreakInside( String value ) throws SemanticException
	{
		setStringProperty( Style.PAGE_BREAK_INSIDE_PROP, value );
	}

	/**
	 * Tests whether to show this frame even if it is empty, or all its data
	 * elements are empty. If <code>false</code>, the section is
	 * automatically hidden when empty.
	 * 
	 * @return <code>true</code> if show-if-blank, otherwise
	 *         <code>false</code>
	 * @see #setShowIfBlank(boolean)
	 */

	public boolean showIfBlank( )
	{
		return getBooleanProperty( Style.SHOW_IF_BLANK_PROP );
	}

	/**
	 * Sets whether to show this frame even if it is empty, or all its data
	 * elements are empty.
	 * 
	 * @param value
	 *            <code>true</code> if show the frame. <code>false</code>
	 *            not.
	 * @throws SemanticException
	 *             if the property is locked
	 * @see #showIfBlank()
	 */

	public void setShowIfBlank( boolean value ) throws SemanticException
	{
		setProperty( Style.SHOW_IF_BLANK_PROP, Boolean.valueOf( value ) );
	}

	/**
	 * Returns one 'text-decoration' property to set underline styles. The
	 * return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 * 
	 * @return the text underline value
	 */

	public String getTextUnderline( )
	{
		return getStringProperty( Style.TEXT_UNDERLINE_PROP );
	}

	/**
	 * Sets one 'text-decoration' property to set underline styles. The input
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text underline
	 * @throws SemanticException
	 *             if the value is not pre-defined.
	 */

	public void setTextUnderline( String value ) throws SemanticException
	{
		setStringProperty( Style.TEXT_UNDERLINE_PROP, value );
	}

	/**
	 * Returns one 'text-decoration' property to set overline styles. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 * 
	 * @return the text overline value
	 */

	public String getTextOverline( )
	{
		return getStringProperty( Style.TEXT_OVERLINE_PROP );
	}

	/**
	 * Sets one 'text-decoration' property to set overline styles. The input
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text overline value
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextOverline( String value ) throws SemanticException
	{
		setStringProperty( Style.TEXT_OVERLINE_PROP, value );
	}

	/**
	 * Returns one 'text-decoration' property to set line-through styles. The
	 * return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 * 
	 * @return the text line-through value
	 */

	public String getTextLineThrough( )
	{
		return getStringProperty( Style.TEXT_LINE_THROUGH_PROP );
	}

	/**
	 * Sets one 'text-decoration' property to set line-through styles. The input
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text line-through value
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextLineThrough( String value ) throws SemanticException
	{
		setStringProperty( Style.TEXT_LINE_THROUGH_PROP, value );
	}

	/**
	 * Returns the text align for block-level elements. The return value is one
	 * of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * @return the text align value
	 */

	public String getTextAlign( )
	{
		return getStringProperty( Style.TEXT_ALIGN_PROP );
	}

	/**
	 * Sets the text align for block-level elements. The input value is one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text align
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextAlign( String value ) throws SemanticException
	{
		setStringProperty( Style.TEXT_ALIGN_PROP, value );
	}

	/**
	 * Returns the value to transform the text. The return value is one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 * 
	 * @return the text transform
	 */

	public String getTextTransform( )
	{
		return getStringProperty( Style.TEXT_TRANSFORM_PROP );
	}

	/**
	 * Sets the value used to transform the text. The input value is one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text transform
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextTransform( String value ) throws SemanticException
	{
		setStringProperty( Style.TEXT_TRANSFORM_PROP, value );
	}

	/**
	 * Returns the value of the vertical align property for inline elements. The
	 * return value is defined in <code>DesignChoiceConstants</code> and can
	 * be one of:
	 * <ul>
	 * <li>VERTICAL_ALIGN_BASELINE
	 * <li>VERTICAL_ALIGN_SUB
	 * <li>VERTICAL_ALIGN_SUPER
	 * <li>VERTICAL_ALIGN_TOP
	 * <li>VERTICAL_ALIGN_TEXT_TOP
	 * <li>VERTICAL_ALIGN_MIDDLE
	 * <li>VERTICAL_ALIGN_BOTTOM
	 * <li>VERTICAL_ALIGN_TEXT_BOTTOM
	 * </ul>
	 * 
	 * @return the value of the vertical align property
	 */

	public String getVerticalAlign( )
	{
		return getStringProperty( Style.VERTICAL_ALIGN_PROP );
	}

	/**
	 * Sets the value of the vertical align property for inline elements. The
	 * input value is defined in <code>DesignChoiceConstants</code> and can be
	 * one of:
	 * <ul>
	 * <li>VERTICAL_ALIGN_BASELINE
	 * <li>VERTICAL_ALIGN_SUB
	 * <li>VERTICAL_ALIGN_SUPER
	 * <li>VERTICAL_ALIGN_TOP
	 * <li>VERTICAL_ALIGN_TEXT_TOP
	 * <li>VERTICAL_ALIGN_MIDDLE
	 * <li>VERTICAL_ALIGN_BOTTOM
	 * <li>VERTICAL_ALIGN_TEXT_BOTTOM
	 * </ul>
	 * 
	 * @param value
	 *            the new vertical align
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setVerticalAlign( String value ) throws SemanticException
	{
		setStringProperty( Style.VERTICAL_ALIGN_PROP, value );
	}

	/**
	 * Returns the white space for block elements. The return value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>WHITE_SPACE_NORMAL</code>
	 * <li><code>WHITE_SPACE_PRE</code>
	 * <li><code>WHITE_SPACE_NOWRAP</code>
	 * </ul>
	 * 
	 * @return the white space
	 */

	public String getWhiteSpace( )
	{
		return getStringProperty( Style.WHITE_SPACE_PROP );
	}

	/**
	 * Sets the white space property for block elements. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>WHITE_SPACE_NORMAL</code>
	 * <li><code>WHITE_SPACE_PRE</code>
	 * <li><code>WHITE_SPACE_NOWRAP</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new white space
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setWhiteSpace( String value ) throws SemanticException
	{
		setStringProperty( Style.WHITE_SPACE_PROP, value );
	}

	/**
	 * Returns the value of widows. The return value is either an integer as as
	 * string or one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>WIDOWS_INHERIT</code>
	 * </ul>
	 * 
	 * @return the widows property
	 * @see #setWidows(String)
	 */

	public String getWidows( )
	{
		return getStringProperty( Style.WIDOWS_PROP );
	}

	/**
	 * Sets the widows property. A ��widow�� occurs when the last line of a
	 * multi-line paragraph appears on its own at the top of a page due to a
	 * page break. The input value is either an integer as as string or one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>WIDOWS_INHERIT</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new windows property
	 * @throws SemanticException
	 *             if the value is not an integer or one of the above constants.
	 * @see #getWidows()
	 */

	public void setWidows( String value ) throws SemanticException
	{
		setStringProperty( Style.WIDOWS_PROP, value );
	}

	/**
	 * Returns the test expression for the map.
	 * 
	 * @return the map test expression
	 * @deprecated
	 */

	public String getMapTestExpr( )
	{
		return null;
	}

	/**
	 * Sets the test expression for the map.
	 * 
	 * @param value
	 *            the value of new map test expression
	 * @throws SemanticException
	 *             if the expression is invalid.
	 * @deprecated
	 */
	public void setMapTestExpr( String value ) throws SemanticException
	{
	}

	/**
	 * Returns the iterator for map rules. The element in the iterator is the
	 * corresponding <code>StructureHandle</code> that deal with a
	 * <code>MapRule</code> in the list.
	 * 
	 * @return the iterator of map rule structure list
	 */

	public Iterator mapRulesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( Style.MAP_RULES_PROP );
		if ( propHandle == null )
			return Collections.EMPTY_LIST.iterator( );

		return propHandle.iterator( );
	}

	/**
	 * Returns the test expression for the highlight.
	 * 
	 * @return the highlight test expression
	 * 
	 * @deprecated
	 */

	public String getHighlightTestExpr( )
	{
		return null;
	}

	/**
	 * Sets the highlight test expression.
	 * 
	 * @param value
	 *            the value of new highlight test expression
	 * @throws SemanticException
	 *             if the expression is invalid.
	 * 
	 * @deprecated
	 */

	public void setHighlightTestExpr( String value ) throws SemanticException
	{
		return;
	}

	/**
	 * Returns the iterator of highlight rules. The element in the iterator is
	 * the corresponding <code>HighlightRuleHandle</code> that deal with a
	 * <code>HighRule</code>.
	 * 
	 * @return the iterator of highlight rule structure list
	 */

	public Iterator highlightRulesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( Style.HIGHLIGHT_RULES_PROP );
		if ( propHandle == null )
			return Collections.EMPTY_LIST.iterator( );

		return propHandle.iterator( );
	}

	/**
	 * Gets a color handle to deal with the font color.
	 * 
	 * @return a ColorHandle to for the font color
	 */

	public ColorHandle getColor( )
	{
		return super.getColorProperty( Style.COLOR_PROP );
	}

	/**
	 * Gets a color handle to deal with the background color.
	 * 
	 * @return a ColorHandle for the background color.
	 */

	public ColorHandle getBackgroundColor( )
	{
		return super.getColorProperty( Style.BACKGROUND_COLOR_PROP );
	}

	/**
	 * Gets a color handle to deal with the color of the top side of the border.
	 * 
	 * @return a ColorHandle to for the color of the top side of the border
	 */

	public ColorHandle getBorderTopColor( )
	{
		return super.getColorProperty( Style.BORDER_TOP_COLOR_PROP );
	}

	/**
	 * Gets a color handle to deal with the color of the left side of the
	 * border.
	 * 
	 * @return a ColorHandle to for the color of the left side of the border
	 */

	public ColorHandle getBorderLeftColor( )
	{
		return super.getColorProperty( Style.BORDER_LEFT_COLOR_PROP );
	}

	/**
	 * Gets a color handle to deal with the color of the right side of the
	 * border.
	 * 
	 * @return a ColorHandle to for the color of the right side of the border
	 */

	public ColorHandle getBorderRightColor( )
	{
		return super.getColorProperty( Style.BORDER_RIGHT_COLOR_PROP );
	}

	/**
	 * Gets a color handle to deal with the color of the bottom side of the
	 * border.
	 * 
	 * @return a ColorHandle to for the color of the bottom side of the border
	 */

	public ColorHandle getBorderBottomColor( )
	{
		return super.getColorProperty( Style.BORDER_BOTTOM_COLOR_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the x position for the background.
	 * Besides the dimension value, the dimension handle may return one of
	 * constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>SECTION_ALIGN_LEFT</code>
	 * <li><code>SECTION_ALIGN_CENTER</code>
	 * <li><code>SECTION_ALIGN_RIGHT</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the x position
	 */

	public DimensionHandle getBackGroundPositionX( )
	{
		return super.getDimensionProperty( Style.BACKGROUND_POSITION_X_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the y position for the background.
	 * Besides the dimension value, the dimension handle may return one of
	 * constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>BACKGROUND_POSITION_TOP</code>
	 * <li><code>BACKGROUND_POSITION_CENTER</code>
	 * <li><code>BACKGROUND_POSITION_BOTTOM</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the y position
	 */

	public DimensionHandle getBackGroundPositionY( )
	{
		return super.getDimensionProperty( Style.BACKGROUND_POSITION_Y_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the spacing between individual
	 * letters. Besides the dimension value, the dimension handle may return one
	 * of constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>NORMAL_NORMAL</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the spacing between individual letters
	 */

	public DimensionHandle getLetterSpacing( )
	{
		return super.getDimensionProperty( Style.LETTER_SPACING_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the height of a line. Implies
	 * spacing between lines. Besides the dimension value, the dimension handle
	 * may return one of constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>NORMAL_NORMAL</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the line height.
	 */

	public DimensionHandle getLineHeight( )
	{
		return super.getDimensionProperty( Style.LINE_HEIGHT_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the text indent.
	 * 
	 * @return a DimensionHandle for the text indent.
	 */

	public DimensionHandle getTextIndent( )
	{
		return super.getDimensionProperty( Style.TEXT_INDENT_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the spacing between two words.
	 * Besides the dimension value, the dimension handle may return one of
	 * constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>NORMAL_NORMAL</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle to deal with the spacing among words.
	 */

	public DimensionHandle getWordSpacing( )
	{
		return super.getDimensionProperty( Style.WORD_SPACING_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the width of the top side of the
	 * border. Besides the dimension value, the dimension handle may return one
	 * of constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the width of the top side of the border
	 */

	public DimensionHandle getBorderTopWidth( )
	{
		return super.getDimensionProperty( Style.BORDER_TOP_WIDTH_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the width of the left side of the
	 * border. Besides the dimension value, the dimension handle may return one
	 * of constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the width of the left side of the border
	 */

	public DimensionHandle getBorderLeftWidth( )
	{
		return super.getDimensionProperty( Style.BORDER_LEFT_WIDTH_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the width of the right side of the
	 * border. Besides the dimension value, the dimension handle may return one
	 * of constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the width of the right side of the border
	 */

	public DimensionHandle getBorderRightWidth( )
	{
		return super.getDimensionProperty( Style.BORDER_RIGHT_WIDTH_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the width of the bottom side of the
	 * border. Besides the dimension value, the dimension handle may return one
	 * of constants defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the width of the bottom side of the border
	 */

	public DimensionHandle getBorderBottomWidth( )
	{
		return super.getDimensionProperty( Style.BORDER_BOTTOM_WIDTH_PROP );
	}

	/**
	 * Gets a handle to deal with the margin of the top side. Besides the
	 * dimension value, the dimension handle may return one of constants defined
	 * in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>MARGIN_AUTO</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the margin of the top side
	 */

	public DimensionHandle getMarginTop( )
	{
		return super.getDimensionProperty( Style.MARGIN_TOP_PROP );
	}

	/**
	 * Gets a handle to deal with the margin of the right side. Besides the
	 * dimension value, the dimension handle may return one of constants defined
	 * in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>MARGIN_AUTO</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the margin of the right side
	 */

	public DimensionHandle getMarginRight( )
	{
		return super.getDimensionProperty( Style.MARGIN_RIGHT_PROP );
	}

	/**
	 * Gets a handle to deal with the margin of the left side. Besides the
	 * dimension value, the dimension handle may return one of constants defined
	 * in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>MARGIN_AUTO</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the margin of the left side
	 */

	public DimensionHandle getMarginLeft( )
	{
		return super.getDimensionProperty( Style.MARGIN_LEFT_PROP );
	}

	/**
	 * Gets a handle to deal with the margin of the bottom side. Besides the
	 * dimension value, the dimension handle may return one of constants defined
	 * in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>MARGIN_AUTO</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the margin of the bottom side
	 */

	public DimensionHandle getMarginBottom( )
	{
		return super.getDimensionProperty( Style.MARGIN_BOTTOM_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the padding of the top side.
	 * 
	 * @return a DimensionHandle for the padding of the top side
	 */

	public DimensionHandle getPaddingTop( )
	{
		return super.getDimensionProperty( Style.PADDING_TOP_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the padding of the right side.
	 * 
	 * @return a DimensionHandle for the padding of the right side
	 */

	public DimensionHandle getPaddingRight( )
	{
		return super.getDimensionProperty( Style.PADDING_RIGHT_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the padding of the left side.
	 * 
	 * @return a DimensionHandle for the padding of the left side
	 */

	public DimensionHandle getPaddingLeft( )
	{
		return super.getDimensionProperty( Style.PADDING_LEFT_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the padding of the bottom side.
	 * 
	 * @return a DimensionHandle for the padding of the bottom side
	 */

	public DimensionHandle getPaddingBottom( )
	{
		return super.getDimensionProperty( Style.PADDING_BOTTOM_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the font size.
	 * 
	 * @return a DimensionHandle for the font size.
	 */

	public DimensionHandle getFontSize( )
	{
		return super.getDimensionProperty( Style.FONT_SIZE_PROP );
	}

	/**
	 * Returns the font handle to deal with the font family.
	 * 
	 * @return a FontHandle for the font family.
	 * @see FontHandle
	 */

	public FontHandle getFontFamilyHandle( )
	{
		return super.getFontProperty( );
	}

	/**
	 * Returns the weight of the font. The return value is one of constants
	 * defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_WEIGHT_NORMAL</code>
	 * <li><code>FONT_WEIGHT_BOLD</code>
	 * <li><code>FONT_WEIGHT_BOLDER</code>
	 * <li><code>FONT_WEIGHT_LIGHTER</code>
	 * <li><code>FONT_WEIGHT_100</code>
	 * <li><code>FONT_WEIGHT_200</code>
	 * <li><code>FONT_WEIGHT_300</code>
	 * <li><code>FONT_WEIGHT_400</code>
	 * <li><code>FONT_WEIGHT_500</code>
	 * <li><code>FONT_WEIGHT_600</code>
	 * <li><code>FONT_WEIGHT_700</code>
	 * <li><code>FONT_WEIGHT_800</code>
	 * <li><code>FONT_WEIGHT_900</code>
	 * </ul>
	 * 
	 * @return the font weight in a string
	 */

	public String getFontWeight( )
	{
		return super.getStringProperty( Style.FONT_WEIGHT_PROP );
	}

	/**
	 * Sets the weight of the font. The input value is one of constants defined
	 * in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_WEIGHT_NORMAL</code>
	 * <li><code>FONT_WEIGHT_BOLD</code>
	 * <li><code>FONT_WEIGHT_BOLDER</code>
	 * <li><code>FONT_WEIGHT_LIGHTER</code>
	 * <li><code>FONT_WEIGHT_100</code>
	 * <li><code>FONT_WEIGHT_200</code>
	 * <li><code>FONT_WEIGHT_300</code>
	 * <li><code>FONT_WEIGHT_400</code>
	 * <li><code>FONT_WEIGHT_500</code>
	 * <li><code>FONT_WEIGHT_600</code>
	 * <li><code>FONT_WEIGHT_700</code>
	 * <li><code>FONT_WEIGHT_800</code>
	 * <li><code>FONT_WEIGHT_900</code>
	 * </ul>
	 * 
	 * @param fontWeight
	 *            the new font weight
	 * @throws SemanticException
	 *             if the input value is not one of the above.
	 */

	public void setFontWeight( String fontWeight ) throws SemanticException
	{
		setStringProperty( Style.FONT_WEIGHT_PROP, fontWeight );
	}

	/**
	 * Returns the variant of the font. The return value is one of constants
	 * defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 * 
	 * @return the font variant in a string.
	 */

	public String getFontVariant( )
	{
		return super.getStringProperty( Style.FONT_VARIANT_PROP );
	}

	/**
	 * Sets the variant of the font. The input value is one of constants defined
	 * in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 * 
	 * @param fontVariant
	 *            the new font variant.
	 * @throws SemanticException
	 *             if the input value is not one of the above.
	 */

	public void setFontVariant( String fontVariant ) throws SemanticException
	{
		setStringProperty( Style.FONT_VARIANT_PROP, fontVariant );
	}

	/**
	 * Returns the style of the font. The return value is one of constants
	 * defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 * 
	 * @return the font style in string.
	 */

	public String getFontStyle( )
	{
		return super.getStringProperty( Style.FONT_STYLE_PROP );
	}

	/**
	 * Sets the style of the font. The input value is one of constants defined
	 * in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 * 
	 * @param fontStyle
	 *            the new font style.
	 * @throws SemanticException
	 *             if the input value is not one of the above.
	 */

	public void setFontStyle( String fontStyle ) throws SemanticException
	{
		setStringProperty( Style.FONT_STYLE_PROP, fontStyle );
	}

	/**
	 * checks whether this style is created by user or predefined by BIRT.
	 * 
	 * @return True if is predefined, false if not.
	 */

	public boolean isPredefined( )
	{

		if ( MetaDataDictionary.getInstance( ).getPredefinedStyle( getName( ) ) != null )
			return true;

		return false;

	}

}
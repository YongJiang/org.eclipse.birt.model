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

package org.eclipse.birt.report.model.api.elements;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * The class provides the error code and the element with semantic error. The
 * semantic error has two levels: error and warning. The default level is error.
 */

public class SemanticError extends SemanticException
{

	/**
	 * Error code indicating the table has inconsistent column count. The column
	 * count should match the maximum cell count in header, detail, and foot
	 * slots.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT = MessageConstants.SEMANTIC_ERROR_INCONSITENT_TABLE_COL_COUNT;

	/**
	 * Error code indicating the table has inconsistent column count because of
	 * drop effects of some cells. The column count should match the maximum
	 * cell count in header, detail, and foot slots.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT_WITH_DROP = MessageConstants.SEMANTIC_ERROR_INCONSITENT_TABLE_COL_COUNT_COZ_DROP;

	/**
	 * Error code indicating the grid has inconsistent column count. The column
	 * count should match the maximum cell count in rows.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_GRID_COL_COUNT = MessageConstants.SEMANTIC_ERROR_INCONSITENT_GRID_COL_COUNT;

	/**
	 * Error code indicating the table has overlapping cells. Cell is forbidden
	 * to overlap other cells.
	 */

	public static final String DESIGN_EXCEPTION_OVERLAPPING_TABLE_CELLS = MessageConstants.SEMANTIC_ERROR_OVERLAPPING_TABLE_CELLS;

	/**
	 * Error code indicating the grid has overlapping cells. Cell is forbidden
	 * to overlap other cells.
	 */

	public static final String DESIGN_EXCEPTION_OVERLAPPING_GRID_CELLS = MessageConstants.SEMANTIC_ERROR_OVERLAPPING_GRID_CELLS;

	/**
	 * Error code indicating the table has a conflict among dropping cells in
	 * group header of the table.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_DROP_HEADINGS = MessageConstants.SEMANTIC_ERROR_INCONSITENT_DROP_HEADINGS;

	/**
	 * Error code indicating the master page size is invalid. The size should be
	 * positive.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PAGE_SIZE = MessageConstants.SEMANTIC_ERROR_INVALID_PAGE_SIZE;

	/**
	 * Error code indicating the page size is missing when page type is custom.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_PAGE_SIZE = MessageConstants.SEMANTIC_ERROR_MISSING_PAGE_SIZE;

	/**
	 * Error code indicating page size can not be specified if page type is not
	 * custom.
	 */

	public static final String DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE = MessageConstants.SEMANTIC_ERROR_CANNOT_SPECIFY_PAGE_SIZE;

	/**
	 * Error code indicating the page margin is larger than the whole page.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PAGE_MARGINS = MessageConstants.SEMANTIC_ERROR_INVALID_PAGE_MARGINS;

	/**
	 * Error code indicating the report has no master page.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_MASTER_PAGE = MessageConstants.SEMANTIC_ERROR_MISSING_MASTER_PAGE;

	/**
	 * Error code indicating the columns span outside the page content.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_MULTI_COLUMN = MessageConstants.SEMANTIC_ERROR_INVALID_MULTI_COLUMN;

	/**
	 * Error code indicating the element referred is not found.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_ELEMENT_REF = MessageConstants.SEMANTIC_ERROR_INVALID_ELEMENT_REF;

	/**
	 * Error code indicating the JDBC select data set has no SQL statement.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_SQL_STMT = MessageConstants.SEMANTIC_ERROR_MISSING_SQL_STMT;

	/**
	 * Error code indicating List or Table can not access any data set.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_DATA_SET = MessageConstants.SEMANTIC_ERROR_MISSING_DATA_SET;

	/**
	 * Error code indicating the image file is not found.
	 */

	public static final String DESIGN_EXCEPTION_IMAGE_FILE_NOT_EXIST = MessageConstants.SEMANTIC_ERROR_IMAGE_FILE_NOT_EXIST;

	/**
	 * Error code indicating the property name of property mask does not be
	 * defined on the element.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PROPERTY_NAME = MessageConstants.SEMANTIC_ERROR_INVALID_PROPERTY_NAME;

	/**
	 * Error code indicating the element is not supported, but implemented in
	 * this release.
	 */

	public static final String DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT = MessageConstants.SEMANTIC_ERROR_UNSUPPORTED_ELEMENT;

	/**
	 * Error code indicating the result set has no result set column.
	 */

	public static final String DESIGN_EXCEPTION_AT_LEAST_ONE_COLUMN = MessageConstants.SEMANTIC_ERROR_AT_LEAST_ONE_COLUMN;

	/**
	 * Error code indicating the table/list has duplicate group name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME = MessageConstants.SEMANTIC_ERROR_DUPLICATE_GROUP_NAME;

	/**
	 * Error code indicating the custom color name is the same as CSS standard
	 * color name.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_CUSTOM_COLOR_NAME = MessageConstants.SEMANTIC_ERROR_INVALID_CUSTOM_COLOR_NAME;

	/**
	 * Error code indicating the custom color name is duplicate.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_CUSTOM_COLOR_NAME = MessageConstants.SEMANTIC_ERROR_DUPLICATE_CUSTOM_COLOR_NAME;

	/**
	 * The extension name for the extended item is not found in our meta.
	 */

	public static final String DESIGN_EXCEPTION_EXTENSION_NOT_FOUND = MessageConstants.SEMANTIC_ERROR_EXTENSION_NOT_FOUND;

	/**
	 * The extension name for the extended item is not defined.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_EXTENSION = MessageConstants.SEMANTIC_ERROR_MISSING_EXTENSION;

	/**
	 * Error code indicating to copy one property is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_PROPERTY_COPY_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_PROPERTY_COPY_FORBIDDEN;

	/**
	 * Error code indicating to copy one column is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_COLUMN_COPY_FORBIDDEN;

	/**
	 * Error code indicating to paste one column is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_COLUMN_PASTE_FORBIDDEN;

	/**
	 * Error code indicating to paste one column is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT = MessageConstants.SEMANTIC_ERROR_COLUMN_PASTE_DIFFERENT_LAYOUT;

	/**
	 * The constant for the semantic error.
	 */

	public static final int ERROR = 0;

	/**
	 * The constant for the semantic warning.
	 */

	public static final int WARNING = 1;

	/**
	 * The level for the semantic error. Can be error or warning.
	 */

	private int errorLevel = ERROR;

	/**
	 * Constructs a SemanticError with the default serious level.
	 * 
	 * @param element
	 *            the element causing this semantic error
	 * @param errCode
	 *            the semantic error code
	 */

	public SemanticError( DesignElement element, String errCode )
	{
		super( element, errCode );
	}

	/**
	 * Constructs a SemanticError with the default serious level.
	 * 
	 * @param element
	 *            the element causing this semantic error
	 * @param values
	 *            value array used for error message
	 * @param errCode
	 *            the semantic error code
	 */

	public SemanticError( DesignElement element, String[] values, String errCode )
	{
		super( element, values, errCode );
	}

	/**
	 * Constructs a SemanticError with the specified level.
	 * 
	 * @param element
	 *            the element causing this semantic error
	 * @param errCode
	 *            the semantic error code
	 * @param level
	 *            the level of the error. Can be <code>ERROR</code> or
	 *            <code>WARNING</code>.
	 */

	public SemanticError( DesignElement element, String errCode, int level )
	{
		super( element, errCode );
		errorLevel = level;
	}

	/**
	 * Constructs a SemanticError with the specified level.
	 * 
	 * @param element
	 *            the element causing this semantic error
	 * @param values
	 *            value array used for error message
	 * @param errCode
	 *            the semantic error code
	 * @param level
	 *            the level of the error. Can be <code>ERROR</code> or
	 *            <code>WARNING</code>.
	 */

	public SemanticError( DesignElement element, String[] values,
			String errCode, int level )
	{
		super( element, values, errCode );
		errorLevel = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage( )
	{
		if ( sResourceKey == DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT
				|| sResourceKey == DESIGN_EXCEPTION_MISSING_EXTENSION )
		{
			return ModelMessages.getMessage( sResourceKey, new String[]{element
					.getElementName( )} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_INVALID_ELEMENT_REF )
		{
			assert oaMessageArguments != null;

			return ModelMessages.getMessage( sResourceKey, new String[]{
					element.getName( ), (String) oaMessageArguments[0],
					(String) oaMessageArguments[1]} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME
				|| sResourceKey == DESIGN_EXCEPTION_EXTENSION_NOT_FOUND )
		{
			assert oaMessageArguments != null;

			return ModelMessages.getMessage( sResourceKey, new String[]{
					element.getName( ), (String) oaMessageArguments[0]} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_IMAGE_FILE_NOT_EXIST
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_PROPERTY_NAME )
		{
			assert oaMessageArguments != null;

			return ModelMessages.getMessage( sResourceKey, new String[]{
					(String) oaMessageArguments[0], element.getName( )} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_INVALID_CUSTOM_COLOR_NAME
				|| sResourceKey == DESIGN_EXCEPTION_DUPLICATE_CUSTOM_COLOR_NAME
				|| sResourceKey == DESIGN_EXCEPTION_PROPERTY_COPY_FORBIDDEN )
		{
			assert oaMessageArguments != null;

			return ModelMessages.getMessage( sResourceKey,
					new String[]{(String) oaMessageArguments[0]} );
		}

		return ModelMessages.getMessage( sResourceKey, new String[]{element
				.getName( )} );
	}

	/**
	 * Returns the level of the error. The level can be <code>ERROR</code> or
	 * <code>WARNING</code>.
	 * 
	 * @return the level of the error
	 */

	public int getErrorLevel( )
	{
		return errorLevel;
	}
}

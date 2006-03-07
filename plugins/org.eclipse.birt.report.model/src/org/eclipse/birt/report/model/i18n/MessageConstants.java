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

package org.eclipse.birt.report.model.i18n;

/**
 * Provide message key constants for a message that needs to be localized.
 * 
 */
public interface MessageConstants
{

	// messages.

	public static final String NAME_PREFIX_NEW_MESSAGE = "Message.NamePrefixNew"; //$NON-NLS-1$

	public static final String CHANGE_PROPERTY_MESSAGE = "Message.ChangeProperty"; //$NON-NLS-1$

	public static final String CHANGE_PROPERTY_DEFINITION_MESSAGE = "Message.ChangePropertyDefinition"; //$NON-NLS-1$

	public static final String MOVE_ITEM_MESSAGE = "Message.MoveItem"; //$NON-NLS-1$
	public static final String ADD_TRANSLATION_MESSAGE = "Message.AddTranslation"; //$NON-NLS-1$
	public static final String DROP_TRANSLATION_MESSAGE = "Message.DropTranslation"; //$NON-NLS-1$
	public static final String CHANGE_TRANSLATION_MESSAGE = "Message.ChangeTranslation"; //$NON-NLS-1$

	public static final String MOVE_CONTENT_MESSAGE = "Message.MoveContent"; //$NON-NLS-1$
	public static final String REPLACE_ELEMENT_MESSAGE = "Message.ReplaceElement"; //$NON-NLS-1$
	public static final String TRANSFORM_TEMPLATE_MESSAGE = "Message.TransformTemplate"; //$NON-NLS-1$

	public static final String ADD_PROPERTY_MESSAGE = "Message.AddProperty"; //$NON-NLS-1$
	public static final String DROP_PROPERTY_MESSAGE = "Message.DropProperty"; //$NON-NLS-1$
	public static final String SET_NAME_MESSAGE = "Message.SetName"; //$NON-NLS-1$
	public static final String ADD_ELEMENT_MESSAGE = "Message.AddElement"; //$NON-NLS-1$
	public static final String DROP_ELEMENT_MESSAGE = "Message.DropElement"; //$NON-NLS-1$
	public static final String SET_STYLE_MESSAGE = "Message.SetStyle"; //$NON-NLS-1$
	public static final String REPLACE_ITEM_MESSAGE = "Message.ReplaceItem"; //$NON-NLS-1$
	public static final String SET_LOCALE_MESSAGE = "Message.SetLocale"; //$NON-NLS-1$
	public static final String SET_TRANSLATION_TEXT_MESSAGE = "Message.SetTranslationText"; //$NON-NLS-1$
	public static final String SET_THEME_MESSAGE = "Message.SetTheme"; //$NON-NLS-1$
	
	public static final String MOVE_ELEMENT_MESSAGE = "Message.MoveElement"; //$NON-NLS-1$
	public static final String INSERT_ELEMENT_MESSAGE = "Message.InsertElement"; //$NON-NLS-1$
	public static final String DELETE_ELEMENT_MESSAGE = "Message.DeleteElement"; //$NON-NLS-1$

	public static final String SET_EXTENDS_MESSAGE = "Message.SetExtends"; //$NON-NLS-1$
	public static final String CHANGE_ITEM_MESSAGE = "Message.ChangeItem"; //$NON-NLS-1$
	public static final String ADD_ITEM_MESSAGE = "Message.AddItem"; //$NON-NLS-1$
	public static final String INSERT_ITEM_MESSAGE = "Message.InsertItem"; //$NON-NLS-1$

	public static final String REMOVE_ITEM_MESSAGE = "Message.RemoveItem"; //$NON-NLS-1$

	// Errors

	// ContentException
	public static final String CONTENT_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN = "Error.ContentException.STRUCTURE_CHANGE_FORBIDDEN";  //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_SLOT_NOT_FOUND = "Error.ContentException.SLOT_NOT_FOUND"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_WRONG_TYPE = "Error.ContentException.WRONG_TYPE"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_DROP_FORBIDDEN = "Error.ContentException.DROP_FORBIDDEN"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_CONTENT_NOT_FOUND = "Error.ContentException.CONTENT_NOT_FOUND"; //$NON-NLS-1$

	public static final String CONTENT_EXCEPTION_NOT_CONTAINER = "Error.ContentException.NOT_CONTAINER"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_SLOT_IS_FULL = "Error.ContentException.SLOT_IS_FULL"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_RECURSIVE = "Error.ContentException.RECURSIVE"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_HAS_NO_CONTAINER = "Error.ContentException.HAS_NO_CONTAINER"; //$NON-NLS-1$

	public static final String CONTENT_EXCEPTION_MOVE_FORBIDDEN = "Error.ContentException.MOVE_FORBIDDEN"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_HAS_DESCENDENTS = "Error.ContentException.HAS_DESCENDENTS"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_INVALID_CONTEXT_CONTAINMENT = "Error.ContentException.INVALID_CONTEXT_CONTAINMENT"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_CONTENT_NAME_REQUIRED = "Error.ContentException.CONTENT_NAME_REQUIRED"; //$NON-NLS-1$

	public static final String CONTENT_EXCEPTION_INVALID_TEMPLATE_ELEMENT = "Error.ContentException.INVALID_TEMPLATE_ELEMENT"; //$NON-NLS-1$
	public static final String CONTENT_EXCEPTION_TEMPLATE_TRANSFORM_FORBIDDEN = "Error.ContentException.TEMPLATE_TRANSFORM_FORBIDDEN"; //$NON-NLS-1$
	
	// CustomMsgException
	public static final String CUSTOM_MSG_EXCEPTION_RESOURCE_KEY_REQUIRED = "Error.CustomMsgException.RESOURCE_KEY_REQUIRED"; //$NON-NLS-1$

	public static final String CUSTOM_MSG_EXCEPTION_DUPLICATE_LOCALE = "Error.CustomMsgException.DUPLICATE_LOCALE"; //$NON-NLS-1$
	public static final String CUSTOM_MSG_EXCEPTION_INVALID_LOCALE = "Error.CustomMsgException.INVALID_LOCALE"; //$NON-NLS-1$
	public static final String CUSTOM_MSG_EXCEPTION_TRANSLATION_NOT_FOUND = "Error.CustomMsgException.TRANSLATION_NOT_FOUND"; //$NON-NLS-1$

	// ExtendsException
	public static final String EXTENDS_EXCEPTION_NOT_FOUND = "Error.ExtendsException.NOT_FOUND"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_WRONG_TYPE = "Error.ExtendsException.WRONG_TYPE"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_WRONG_EXTENSION_TYPE = "Error.ExtendsException.WRONG_EXTENSION_TYPE"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_EXTENDS_FORBIDDEN = "Error.ExtendsException.EXTENDS_FORBIDDEN"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_CANT_EXTEND = "Error.ExtendsException.CANT_EXTEND"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_SELF_EXTEND = "Error.ExtendsException.SELF_EXTEND"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_CIRCULAR = "Error.ExtendsException.CIRCULAR"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_UNNAMED_PARENT = "Error.ExtendsException.UNNAMED_PARENT"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_PARENT_NOT_IN_COMPONENT = "Error.ExtendsException.PARENT_NOT_IN_COMPONENT"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_PARENT_NOT_INCLUDE = "Error.ExtendsException.PARENT_NOT_INCLUDE"; //$NON-NLS-1$
	public static final String EXTENDS_EXCEPTION_NO_PARENT = "Error.ExtendsException.NO_PARENT"; //$NON-NLS-1$
	
	// NameException
	public static final String NAME_EXCEPTION_NAME_REQUIRED = "Error.NameException.NAME_REQUIRED"; //$NON-NLS-1$
	public static final String NAME_EXCEPTION_NAME_FORBIDDEN = "Error.NameException.NAME_FORBIDDEN"; //$NON-NLS-1$
	public static final String NAME_EXCEPTION_DUPLICATE = "Error.NameException.DUPLICATE"; //$NON-NLS-1$
	public static final String NAME_EXCEPTION_HAS_REFERENCES = "Error.NameException.HAS_REFERENCES"; //$NON-NLS-1$
	public static final String NAME_EXCEPTION_DOT_FORBIDDEN = "Error.NameException.DOT_FORBIDDEN"; //$NON-NLS-1$
	
	// PropertyNameException
	public static final String PROPERTY_NAME_EXCEPTION_PROPERTY_NOT_VALID = "Error.PropertyNameException.PROPERTY_NOT_VALID"; //$NON-NLS-1$
	public static final String PROPERTY_NAME_EXCEPTION_MEMBER_NOT_VALID = "Error.PropertyNameException.MEMBER_NOT_VALID"; //$NON-NLS-1$

	// PropertyValueException

	public static final String PROPERTY_VALUE_EXCEPTION_INVALID_VALUE = "Error.PropertyValueException.INVALID_VALUE"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_CHOICE_NOT_FOUND = "Error.PropertyValueException.CHOICE_NOT_FOUND"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXTENSION_SETTING_FORBIDDEN = "Error.PropertyValueException.EXTENSION_SETTING_FORBIDDEN"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN = "Error.PropertyValueException.PROPERTY_CHANGE_FORBIDDEN"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_NOT_LIST_TYPE = "Error.PropertyValueException.NOT_LIST_TYPE"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_ITEM_NOT_FOUND = "Error.PropertyValueException.ITEM_NOT_FOUND"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_WRONG_ELEMENT_TYPE = "Error.PropertyValueException.WRONG_ELEMENT_TYPE"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_VALUE_EXISTS = "Error.PropertyValueException.VALUE_EXISTS"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_VALUE_REQUIRED = "Error.PropertyValueException.VALUE_REQUIRED"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_VALUE_LOCKED = "Error.PropertyValueException.VALUE_LOCKED"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_UNIT_NOT_ALLOWED = "Error.PropertyValueException.UNIT_NOT_ALLOWED"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_CHOICE_NOT_ALLOWED = "Error.PropertyValueException.CHOICE_NOT_ALLOWED"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_WRONG_ITEM_TYPE = "Error.PropertyValueException.WRONG_ITEM_TYPE"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_NEGATIVE_VALUE = "Error.PropertyValueException.NEGATIVE_VALUE"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_NON_POSITIVE_VALUE = "Error.PropertyValueException.NON_POSITIVE_VALUE"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE_EXCEPTION_DOT_FORBIDDEN = "Error.PropertyValueException.DOT_FORBIDDEN"; //$NON-NLS-1$
	
	// SemanticError
	public static final String SEMANTIC_ERROR_INCONSISTENT_TABLE_COL_COUNT = "Error.SemanticError.INCONSISTENT_TABLE_COL_COUNT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INCONSISTENT_TABLE_COL_COUNT_COZ_DROP = "Error.SemanticError.INCONSISTENT_TABLE_COL_COUNT_COZ_DROP"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_OVERLAPPING_CELLS = "Error.SemanticError.OVERLAPPING_CELLS"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INCONSISTENT_DROP_HEADINGS = "Error.SemanticError.INCONSISTENT_TABLE_DROP_HEADINGS"; //$NON-NLS-1$	
	public static final String SEMANTIC_ERROR_INVALID_PAGE_SIZE = "Error.SemanticError.INVALID_PAGE_SIZE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_PAGE_MARGINS = "Error.SemanticError.INVALID_PAGE_MARGINS"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_MISSING_PAGE_SIZE = "Error.SemanticError.MISSING_PAGE_SIZE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_MISSING_MASTER_PAGE = "Error.SemanticError.MISSING_MASTER_PAGE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_MULTI_COLUMN = "Error.SemanticError.INVALID_MULTI_COLUMN"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INCONSISTENT_GRID_COL_COUNT = "Error.SemanticError.INCONSISTENT_GRID_COL_COUNT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_ELEMENT_REF = "Error.SemanticError.INVALID_ELEMENT_REF"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_MISSING_SQL_STMT = "Error.SemanticError.MISSING_SQL_STMT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_IMAGE_SCALE_VALUE = "Error.SemanticError.INVALID_IMAGE_SCALE_VALUE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_CANNOT_SPECIFY_PAGE_SIZE = "Error.SemanticError.CANNOT_SPECIFY_PAGE_SIZE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_IMAGE_FILE_NOT_EXIST = "Error.SemanticError.IMAGE_FILE_NOT_FOUND"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_PROPERTY_NAME = "Error.SemanticError.INVALID_PROPERTY_NAME"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_NEGATIVE_REPORT_REFRESH_RATE = "Error.SemanticError.NEGATIVE_REPORT_REFRESH_RATE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_NEGATIVE_CELL_COLUMN = "Error.SemanticError.NEGATIVE_CELL_COLUMN"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_NEGATIVE_WIDTH = "Error.SemanticError.NEGATIVE_WIDTH"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_NEGATIVE_HEIGHT = "Error.SemanticError.NEGATIVE_HEIGHT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_COLUMN_REPEAT = "Error.SemanticError.INVALID_COLUMN_REPEAT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_PAGE_COLUMNS = "Error.SemanticError.INVALID_PAGE_COLUMNS"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_NEGATIVE_FONT_SIZE = "Error.SemanticError.NEGATIVE_FONT_SIZE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_NEGATIVE_BORDER_WIDTH = "Error.SemanticError.NEGATIVE_BORDER_WIDTH"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_MISSING_DATA_SET = "Error.SemanticError.MISSING_DATA_SET"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_UNSUPPORTED_ELEMENT = "Error.SemanticError.UNSUPPORTED_ELEMENT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_NEGATIVE_LINE_HEIGHT = "Error.SemanticError.NEGATIVE_LINE_HEIGHT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_AT_LEAST_ONE_COLUMN = "Error.SemanticError.AT_LEAST_ONE_COLUMN"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_DUPLICATE_GROUP_NAME = "Error.SemanticError.DUPLICATE_GROUP_NAME"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_CUSTOM_COLOR_NAME = "Error.SemanticError.INVALID_CUSTOM_COLOR_NAME"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_DUPLICATE_CUSTOM_COLOR_NAME = "Error.SemanticError.DUPLICATE_CUSTOM_COLOR_NAME"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_EXTENSION_NOT_FOUND = "Error.SemanticError.EXTENSION_NOT_FOUND"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_MISSING_EXTENSION = "Error.SemanticError.MISSING_EXTENSION"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_PROPERTY_COPY_FORBIDDEN = "Error.SemanticError.PROPERTY_COPY_FORBIDDEN"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_COLUMN_COPY_FORBIDDEN = "Error.SemanticError.COLUMN_COPY_FORBIDDEN"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_COLUMN_PASTE_FORBIDDEN = "Error.SemanticError.COLUMN_PASTE_FORBIDDEN"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_COLUMN_PASTE_DIFFERENT_LAYOUT = "Error.SemanticError.COLUMN_PASTE_DIFFERENT_LAYOUT"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_STRUCTURE_REF = "Error.SemanticError.INVALID_STRUCTURE_REF"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_SCALAR_PARAMETER_TYPE = "Error.SemanticError.INVALID_SCALAR_PARAMETER_TYPE"; //$NON-NLS-1$
	public static final String STYLE_EXCEPTION_FORBIDDEN = "Error.StyleException.FORBIDDEN"; //$NON-NLS-1$
	public static final String STYLE_EXCEPTION_NOT_FOUND = "Error.StyleException.NOT_FOUND"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_MISSING_TEMPLATE_PARAMETER_TYPE = "Error.SemanticError.MISSING_TEMPLATE_PARAMETER_TYPE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INCONSISTENT_TEMPLATE_PARAMETER_TYPE = "Error.SemanticError.INCONSISTENT_TEMPLATE_PARAMETER_TYPE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_LIBRARY_REFERENCE = "Error.SemanticError.INVALID_LIBRARY_REFERENCE"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_PROPERTY_BINDING_ID = "Error.SemanticError.INVALID_PROPERTY_BINDING_ID"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_PROPERTY_BINDING_FORBIDDEN = "Error.SemanticError.PROPERTY_BINDING_FORBIDDEN"; //$NON-NLS-1$
	public static final String SEMANTIC_ERROR_INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT = "Error.SemanticError.INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT"; //$NON-NLS-1$
	
	// ThemeException
	public static final String THEME_EXCEPTION_NOT_FOUND = "Error.ThemeException.NOT_FOUND"; //$NON-NLS-1$
	
	// DesignParserException
	public static final String DESIGN_PARSER_EXCEPTION_FILE_NOT_FOUND = "Error.DesignParserException.FILE_NOT_FOUND"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_RGB_REQUIRED = "Error.DesignParserException.RGB_REQUIRED"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_COLOR_NAME_REQUIRED = "Error.DesignParserException.COLOR_NAME_REQUIRED"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_ILLEGAL_EXTENDS = "Error.DesignParserException.ILLEGAL_EXTENDS"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_IMAGE_REF_CONFLICT = "Error.DesignParserException.IMAGE_REF_CONFLICT"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INVALID_IMAGEREF_EXPR_VALUE = "Error.DesignParserException.INVALID_IMAGEREF_EXPR_VALUE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INVALID_IMAGE_URL_VALUE = "Error.DesignParserException.INVALID_IMAGE_URL_VALUE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INVALID_IMAGE_NAME_VALUE = "Error.DesignParserException.INVALID_IMAGE_NAME_VALUE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_ACTION_REPORTNAME_REQUIRED = "Error.DesignParserException.ACTION_REPORTNAME_REQUIRED"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_ACTION_PARAMETER_NAME_REQUIRED = "Error.DesignParserException.ACTION_PARAMETER_NAME_REQUIRED"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_CHOICE_RESTRICTION_VIOLATION = "Error.DesignParserException.CHOICE_RESTRICTION_VIOLATION"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_MESSAGE_KEY_REQUIRED = "Error.DesignParserException.MESSAGE_KEY_REQUIRED"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_DUPLICATE_TRANSLATION_LOCALE = "Error.DesignParserException.DUPLICATE_TRANSLATION_LOCALE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_NAME_REQUIRED = "Error.DesignParserException.NAME_REQUIRED"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_WRONG_STRUCTURE_LIST_TYPE = "Error.DesignParserException.WRONG_STRUCTURE_LIST_TYPE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_WRONG_EXTENDED_PROPERTY_TYPE = "Error.DesignParserException.WRONG_EXTENDED_PROPERTY_TYPE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INVALID_STRUCTURE_NAME = "Error.DesignParserException.INVALID_STRUCTURE_NAME"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INVALID_PROPERTY_SYNTAX = "Error.DesignParserException.INVALID_PROPERTY_SYNTAX"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_UNDEFINED_PROPERTY = "Error.DesignParserException.UNDEFINED_PROPERTY"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_UNSUPPORTED_ENCODING = "Error.DesignParserException.UNSUPPORTED_ENCODING"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_PROPERTY_IS_NOT_ENCRYPTABLE = "Error.DesignParserException.PROPERTY_IS_NOT_ENCRYPTABLE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_UNSUPPORTED_VERSION = "Error.DesignParserException.UNSUPPORTED_VERSION"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INVALID_VERSION = "Error.DesignParserException.INVALID_VERSION"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INVALID_ELEMENT_ID = "Error.DesignParserException.INVALID_ELEMENT_ID"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_VIRTUAL_PARENT_NOT_FOUND = "Error.DesignParserException.VIRTUAL_PARENT_NOT_FOUND"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_DUPLICATE_ELEMENT_ID = "Error.DesignParserException.DUPLICATE_ELEMENT_ID"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE = "Error.DesignParserException.INCONSISTENT_TEMPLATE_ELEMENT_TYPE"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_MISSING_TEMPLATE_PARAMETER_DEFAULT = "Error.DesignParserException.MISSING_TEMPLATE_PARAMETER_DEFAULT"; //$NON-NLS-1$
	public static final String DESIGN_PARSER_EXCEPTION_WRONG_SIMPLE_LIST_TYPE = "Error.DesignParserException.WRONG_SIMPLE_LIST_TYPE"; //$NON-NLS-1$
	
	// DesignFileException
	public static final String DESIGN_FILE_EXCEPTION_SYNTAX_ERROR = "Error.DesignFileException.SYNTAX_ERROR"; //$NON-NLS-1$
	public static final String DESIGN_FILE_EXCEPTION_SEMANTIC_ERROR = "Error.DesignFileException.SEMANTIC_ERROR"; //$NON-NLS-1$
	public static final String DESIGN_FILE_EXCEPTION_INVALID_XML = "Error.DesignFileException.INVALID_XML"; //$NON-NLS-1$
	public static final String DESIGN_FILE_EXCEPTION_SEMANTIC_WARNING = "Error.DesignFileException.SEMANTIC_WARNING"; //$NON-NLS-1$

	// XMLParserException
	public static final String XML_PARSER_EXCEPTION_UNKNOWN_TAG = "Error.XMLParserException.UNKNOWN_TAG"; //$NON-NLS-1$
	public static final String XML_PARSER_EXCEPTION_SAX_ERROR = "Error.XMLParserException.SAX_ERROR"; //$NON-NLS-1$
	public static final String XML_PARSER_EXCEPTION_INVALID_BOOLEAN = "Error.XMLParserException.INVALID_BOOLEAN"; //$NON-NLS-1$
	public static final String XML_PARSER_EXCEPTION_WARNINGS_FOUND = "Error.XMLParserException.WARNINGS_FOUND"; //$NON-NLS-1$
	public static final String XML_PARSER_EXCEPTION_EXCEPTION = "Error.XMLParserException.EXCEPTION"; //$NON-NLS-1$
	public static final String XML_PARSER_EXCEPTION_INVALID_INTEGER = "Error.XMLParserException.INVALID_INTEGER"; //$NON-NLS-1$

	// UserPropertyException
	public static final String USER_PROPERTY_EXCEPTION_NAME_REQUIRED = "Error.UserPropertyException.NAME_REQUIRED"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_DUPLICATE_NAME = "Error.UserPropertyException.DUPLICATE_NAME"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_INVALID_TYPE = "Error.UserPropertyException.INVALID_TYPE"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_MISSING_CHOICES = "Error.UserPropertyException.MISSING_CHOICES"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_INVALID_DISPLAY_ID = "Error.UserPropertyException.INVALID_DISPLAY_ID"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_NOT_FOUND = "Error.UserPropertyException.NOT_FOUND"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_USER_PROP_DISALLOWED = "Error.UserPropertyException.USER_PROP_DISALLOWED"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_CHOICE_VALUE_REQUIRED = "Error.UserPropertyException.CHOICE_VALUE_REQUIRED"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_INVALID_CHOICE_VALUE = "Error.UserPropertyException.INVALID_CHOICE_VALUE"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_CHOICE_NAME_REQUIRED = "Error.UserPropertyException.CHOICE_NAME_REQUIRED"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_INVALID_DEFINITION = "Error.UserPropertyException.INVALID_DEFINITION"; //$NON-NLS-1$
	public static final String USER_PROPERTY_EXCEPTION_INVALID_DEFAULT_VALUE = "Error.UserPropertyException.INVALID_DEFAULT_VALUE"; //$NON-NLS-1$
	
	// MetaDataReaderException
	public static final String META_DATA_READER_EXCEPTION_META_DATA_ERROR = "Error.MetaDataReaderException.META_DATA_ERROR"; //$NON-NLS-1$

	// LibraryException
	public static final String LIBRARY_EXCEPTION_LIBRARY_NOT_FOUND = "Error.LibraryException.LIBRARY_NOT_FOUND"; //$NON-NLS-1$
	public static final String LIBRARY_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE = "Error.LibraryException.DUPLICATE_LIBRARY_NAMESPACE"; //$NON-NLS-1$
	public static final String LIBRARY_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY = "Error.LibraryException.LIBRARY_INCLUDED_RECURSIVELY"; //$NON-NLS-1$
	public static final String LIBRARY_EXCEPTION_LIBRARY_HAS_DESCENDENTS = "Error.LibraryException.LIBRARY_HAS_DESCENDENTS"; //$NON-NLS-1$
	public static final String LIBRARY_EXCEPTION_LIBRARY_ALREADY_INCLUDED = "Error.LibraryException.LIBRARY_ALREADY_INCLUDED"; //$NON-NLS-1$
	
	// StyleSheetException
	public static final String STYLE_SHEET_EXCEPTION_STYLE_SHEET_NOT_FOUND = "Error.StyleSheetException.STYLE_SHEET_NOT_FOUND"; //$NON-NLS-1$
	public static final String STYLE_SHEET_EXCEPTION_SYNTAX_ERROR = "Error.StyleSheetException.SYNTAX_ERROR"; //$NON-NLS-1$
	
	// StyleSheetParserException
	public static final String STYLE_SHEET_PARSER_EXCEPTION_RULE_NOT_SUPPORTED = "Error.StyleSheetParserException.RULE_NOT_SUPPORTED"; //$NON-NLS-1$
	public static final String STYLE_SHEET_PARSER_EXCEPTION_STYLE_NOT_SUPPORTED = "Error.StyleSheetParserException.STYLE_NOT_SUPPORTED"; //$NON-NLS-1$
	public static final String STYLE_SHEET_PARSER_EXCEPTION_PROPERTY_NOT_SUPPORTED = "Error.StyleSheetParserException.PROPERTY_NOT_SUPPORTED"; //$NON-NLS-1$
	public static final String STYLE_SHEET_PARSER_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE = "Error.StyleSheetParserException.INVALID_SHORT_HAND_CSSPROPERTY_VALUE"; //$NON-NLS-1$
	public static final String STYLE_SHEET_PARSER_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE = "Error.StyleSheetParserException.INVALID_SIMPLE_CSSPROPERTY_VALUE"; //$NON-NLS-1$

	// TemplateException	
	public static final String TEMPLATE_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE = "Error.TemplateException.INVALID_TEMPLATE_ELEMENT_TYPE"; //$NON-NLS-1$
	public static final String TEMPLATE_EXCEPTION_TRANSFORM_TO_REPORT_ITEM_FORBIDDEN = "Error.TemplateException.TRANSFORM_TO_REPORT_ITEM_FORBIDDEN"; //$NON-NLS-1$
	public static final String TEMPLATE_EXCEPTION_TRANSFORM_TO_DATA_SET_FORBIDDEN = "Error.TemplateException.TRANSFORM_TO_DATA_SET_FORBIDDEN"; //$NON-NLS-1$
	public static final String TEMPLATE_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN = "Error.TemplateException.CREATE_TEMPLATE_ELEMENT_FORBIDDEN"; //$NON-NLS-1$
	public static final String TEMPLATE_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED = "Error.TemplateException.TEMPLATE_ELEMENT_NOT_SUPPORTED"; //$NON-NLS-1$
	public static final String TEMPLATE_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN = "Error.TemplateException.REVERT_TO_TEMPLATE_FORBIDDEN"; //$NON-NLS-1$

		
}

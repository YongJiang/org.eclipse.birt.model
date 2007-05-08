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
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * Parses the "expression" tag. If the element property or structure member is
 * expression type, then we will use the expression not the property tag.
 */

class ExpressionState extends PropertyState
{

	/**
	 * 
	 * @param theHandler
	 * @param element
	 */
	ExpressionState( ModuleParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, String propName, IStructure
	 *      struct)
	 */

	ExpressionState( ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element, propDefn, struct );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		super.parseAttrs( attrs );
	}

	private static final int CONTENT_TYPE_EXPR = "contentTypeExpr"
			.toLowerCase( ).hashCode( );
	private static final int MAP_TEST_EXPR = "mapTestExpr".toLowerCase( )
			.hashCode( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#generalJumpTo()
	 */

	protected AbstractParseState generalJumpTo( )
	{
		int nameValue = name.toLowerCase( ).hashCode( );
		if ( ( element instanceof TextDataItem )
				&& CONTENT_TYPE_EXPR == nameValue )
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, "contentTypeExpr" ); //$NON-NLS-1$
			state.setName( TextDataItem.CONTENT_TYPE_PROP );
			return state;
		}
		if ( MAP_TEST_EXPR == nameValue )
			return new CompatibleTestExpreState( handler, element,
					Style.MAP_RULES_PROP );
		return super.generalJumpTo( );
	}

	private static final int HIGHLIGHT_TEST_EXPR = "highlightTestExpr"
			.toLowerCase( ).hashCode( );
	private static final int VALUE_EXPR = "valueExpr".toLowerCase( ).hashCode( );
	private static final int IMAGE_NAME_PROP = ImageItem.IMAGE_NAME_PROP
			.toLowerCase( ).hashCode( );

	private static final int EXPRESSION_MEMBER = ComputedColumn.EXPRESSION_MEMBER
			.toLowerCase( ).hashCode( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#versionConditionalJumpTo()
	 */

	protected AbstractParseState versionConditionalJumpTo( )
	{
		if ( HIGHLIGHT_TEST_EXPR == nameValue )
		{
			if ( handler.isVersion( VersionUtil.VERSION_0 )
					|| handler.isVersion( VersionUtil.VERSION_1_0_0 ) )
				return new CompatibleTestExpreState( handler, element,
						Style.HIGHLIGHT_RULES_PROP );
		}
		if ( element instanceof DataItem && VALUE_EXPR == nameValue
				&& struct == null
				&& handler.versionNumber < VersionUtil.VERSION_3_1_0 )

		{
			CompatibleDataValueExprState state = new CompatibleDataValueExprState(
					handler, element );
			state.setName( IDataItemModel.RESULT_SET_COLUMN_PROP );
			return state;
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_1
				&& element instanceof ImageItem && struct == null
				&& IMAGE_NAME_PROP == nameValue )
		{
			PropertyState state = new PropertyState( handler, element );
			state.setName( name );
			return state;
		}

		if ( struct instanceof ParamBinding
				&& handler.versionNumber < VersionUtil.VERSION_3_2_3 )
		{
			if ( propDefn == null )
				propDefn = element.getPropertyDefn( name );

			CompatibleParamBindingValueState state = new CompatibleParamBindingValueState(
					handler, element, propDefn, struct );
			state.setName( name );
			return state;
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_3 )
		{
			if ( propDefn == null )
				propDefn = element.getPropertyDefn( name );

			if ( struct instanceof ComputedColumn
					&& element instanceof ReportItem
					&& EXPRESSION_MEMBER == nameValue )
			{
				CompatibleBoundColumnExprState state = new CompatibleBoundColumnExprState(
						handler, element, propDefn, struct );
				state.setName( name );
				return state;
			}

			CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(
					handler, element );
			state.setName( name );
			state.struct = struct;
			state.propDefn = propDefn;
			return state;
		}
		return super.versionConditionalJumpTo( );
	}

}

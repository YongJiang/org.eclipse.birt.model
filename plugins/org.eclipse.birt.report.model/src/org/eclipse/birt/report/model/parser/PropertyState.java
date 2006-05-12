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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.xml.sax.SAXException;

/**
 * Parses the "property" tag. The tag may give the property value of the element
 * or the member of the structure.
 */

class PropertyState extends AbstractPropertyState
{

	protected PropertyDefn propDefn = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, )
	 */

	PropertyState( ModuleParserHandler theHandler, DesignElement element )
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

	PropertyState( ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element );

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/**
	 * Sets the name in attribute.
	 * 
	 * @param name
	 *            the value of the attribute name
	 */

	protected void setName( String name )
	{
		super.setName( name );

		if ( struct != null )
		{
			propDefn = (PropertyDefn) struct.getDefn( ).getMember( name );
		}
		else
		{
			propDefn = element.getPropertyDefn( name );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );
		doEnd( value );
	}

	/**
	 * @param value
	 */

	protected void doEnd( String value )
	{
		if ( struct != null )
		{
			setMember( struct, propDefn.getName( ), name, value );
			return;
		}

		if ( StyledElement.STYLE_PROP.equalsIgnoreCase( name ) )
		{
			// Ensure that the element can have a style.

			if ( !element.getDefn( ).hasStyle( ) )
			{
				DesignParserException e = new DesignParserException(
						new String[]{name},
						DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY );
				RecoverableError.dealUndefinedProperty( handler, e );
				return;
			}

			( (StyledElement) element ).setStyleName( value );
		}

		else
		{
			setProperty( name, value );
		}

	}

	public AbstractParseState jumpTo( )
	{
		if ( !valid )
			return new AnyElementState( getHandler( ) );

		if ( ( StringUtil.compareVersion( handler.getVersion( ), "3.2.2" ) < 0 ) //$NON-NLS-1$
				&& ( DesignChoiceConstants.CHOICE_VERTICAL_ALIGN.equals( name ) ) )
		{
			CompatibleVerticalAlignState state = new CompatibleVerticalAlignState(
					handler, element );
			state.setName( DesignChoiceConstants.CHOICE_VERTICAL_ALIGN );
			return state;
		}

		IPropertyDefn jmpDefn = null;
		if ( struct != null )
			jmpDefn = struct.getDefn( ).getMember( name );
		else
			jmpDefn = element.getPropertyDefn( name );

		if ( element instanceof ListGroup
				&& ListGroup.GROUP_START_PROP.equalsIgnoreCase( name ) )
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, ListGroup.GROUP_START_PROP );
			state.setName( ListGroup.INTERVAL_BASE_PROP );
			return state;
		}

		if ( element instanceof ReportDesign
				&& "cheetSheet".equalsIgnoreCase( name ) ) //$NON-NLS-1$
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, "cheetSheet" ); //$NON-NLS-1$
			state.setName( ReportDesign.CHEAT_SHEET_PROP );
			return state;
		}

		if ( jmpDefn != null
				&& ( FilterCondition.OPERATOR_MEMBER.equalsIgnoreCase( jmpDefn
						.getName( ) ) || MapRule.OPERATOR_MEMBER
						.equalsIgnoreCase( jmpDefn.getName( ) ) ) )
		{
			CompatibleOperatorState state = new CompatibleOperatorState(
					handler, element, propDefn, struct );
			state.setName( name );
			return state;
		}

		if ( ( jmpDefn != null ) && ( jmpDefn.getStructDefn( ) != null ) )
		{
			if ( DateTimeFormatValue.FORMAT_VALUE_STRUCT
					.equalsIgnoreCase( jmpDefn.getStructDefn( ).getName( ) )
					|| NumberFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) )
					|| StringFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) )
					|| ParameterFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) ) )
			{
				CompatibleFormatPropertyState state = new CompatibleFormatPropertyState(
						handler, element, propDefn, struct );
				state.setName( name );
				state.createStructure( );
				return state;
			}
		}

		if ( ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT
				.equalsIgnoreCase( element.getDefn( ).getName( ) )
				&& ( name.equalsIgnoreCase( "headerHeight" ) || name //$NON-NLS-1$
						.equalsIgnoreCase( "footerHeight" ) ) ) //$NON-NLS-1$
			return new CompatibleIgnorePropertyState( handler, element );

		if ( ( element instanceof ListingElement || element instanceof GroupElement ) )
		{
			if ( IListingElementModel.PAGE_BREAK_INTERVAL_PROP
					.equalsIgnoreCase( name ) )
				return new CompatibleIgnorePropertyState( handler, element );

			if ( name.equalsIgnoreCase( "onStart" ) || name //$NON-NLS-1$
					.equalsIgnoreCase( "onFinish" ) ) //$NON-NLS-1$
				return new CompatibleIgnorePropertyState( handler, element );

			if ( "onRow".equalsIgnoreCase( name ) //$NON-NLS-1$
					&& !( element instanceof TableItem ) )
				return new CompatibleIgnorePropertyState( handler, element );

			if ( "onRow".equalsIgnoreCase( name ) )//$NON-NLS-1$
				return new CompatibleOnRowPropertyState( handler, element );
		}

		if ( element instanceof GroupElement )
		{
			if ( "onCreate".equalsIgnoreCase( name ) || //$NON-NLS-1$
					"onRender".equalsIgnoreCase( name ) ) //$NON-NLS-1$
				return new CompatibleIgnorePropertyState( handler, element );
		}

		if ( IStyleModel.PAGE_BREAK_BEFORE_PROP.equalsIgnoreCase( name )
				|| IStyleModel.PAGE_BREAK_AFTER_PROP.equalsIgnoreCase( name )
				|| IStyleModel.PAGE_BREAK_INSIDE_PROP.equalsIgnoreCase( name ) )
		{
			CompatiblePageBreakPropState state = new CompatiblePageBreakPropState(
					handler, element );
			state.setName( name );
			return state;
		}

		if ( ( ICellModel.ON_CREATE_METHOD.equalsIgnoreCase( name )
				|| ITableRowModel.ON_CREATE_METHOD.equalsIgnoreCase( name ) || IReportItemModel.ON_CREATE_METHOD
				.equalsIgnoreCase( name ) )
				&& StringUtil.compareVersion( handler.getVersion( ), "3.2.0" ) < 0 ) //$NON-NLS-1$
		{
			CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(
					handler, element );
			state.setName( name );
			return state;
		}

		return super.jumpTo( );
	}
}
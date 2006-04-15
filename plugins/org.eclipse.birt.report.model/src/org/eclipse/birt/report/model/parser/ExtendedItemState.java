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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.ICompatibleReportItem;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the Extended Item (extended item) tag.
 */

public class ExtendedItemState extends ReportItemState
{

	/**
	 * The extended item being created.
	 */

	public ExtendedItem element;

	/**
	 * Constructs the extended item state with the design parser handler, the
	 * container element and the container slot of the extended item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ExtendedItemState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new ExtendedItem( );

		parseExtensionName( attrs, true );

		initElement( attrs );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		if ( StringUtil.compareVersion( handler.getVersion( ), "3.2.1" ) >= 0 ) //$NON-NLS-1$
		{
			super.end( );
			return;
		}

		try
		{
			element.initializeReportItem( handler.module );
		}
		catch ( ExtendedElementException e )
		{
			return;
		}

		Object reportItem = element.getExtendedElement( );

		if ( reportItem != null && reportItem instanceof ICompatibleReportItem )
		{
			List jsExprs = ( (ICompatibleReportItem) reportItem )
					.getRowExpressions( );
			Map updatedExprs = handleJavaExpression( jsExprs );
			( (ICompatibleReportItem) reportItem )
					.updateRowExpressions( updatedExprs );
		}

		super.end( );
	}

	/**
	 * Does backward compatiblility work for the extended item from BIRT 2.1M5
	 * to BIRT 2.1.0.
	 * 
	 * @param jsExprs
	 *            the expression from the extended item.
	 * @return a map containing updated expressions.
	 */

	private Map handleJavaExpression( List jsExprs )
	{
		Map retMap = new HashMap( );
				
		if (jsExprs == null || jsExprs.isEmpty( ) )
			return retMap;

		for ( int i = 0; i < jsExprs.size( ); i++ )
		{
			String jsExpr = (String) jsExprs.get( i );

			List newExprs = null;

			try
			{
				newExprs = ExpressionUtil.extractColumnExpressions( jsExpr );
			}
			catch ( BirtException e )
			{
				newExprs = null;
			}

			if ( newExprs == null || newExprs.isEmpty( ) )
				continue;

			for ( int j = 0; j < newExprs.size( ); j++ )
			{
				IColumnBinding boundColumn = (IColumnBinding) newExprs.get( j );
				DesignElement tmpElement = DataBoundColumnUtil
						.findTargetOfBoundColumns( element, boundColumn
								.getBoundExpression( ), handler.module );

				String columnName = boundColumn.getResultSetColumnName( );

				if ( tmpElement != null )
				{
					String tmpName = DataBoundColumnUtil
							.createBoundDataColumn( tmpElement, columnName,
									boundColumn.getBoundExpression( ), handler
											.getModule( ) );
					if ( tmpName != null )
						columnName = tmpName;
				}

				retMap.put( jsExpr, ExpressionUtil
						.createRowExpression( columnName ) );
			}
		}

		return retMap;
	}
}
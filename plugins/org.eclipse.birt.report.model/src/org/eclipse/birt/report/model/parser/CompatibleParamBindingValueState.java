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

import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.xml.sax.SAXException;

/**
 * Parse the value of ParamBinding in BIRT 2.1M5 to BIRT 2.1 RC0.
 */

public class CompatibleParamBindingValueState
		extends
			CompatibleMiscExpressionState
{

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler
	 *            the handler to parse the design file.
	 * @param element
	 *            the data item
	 */

	CompatibleParamBindingValueState( ModuleParserHandler theHandler,
			DesignElement element, PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element, propDefn, struct );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );

		if ( value == null )
			return;

		// keep the expression as same.

		doEnd( value );

		List newExprs = null;

		try
		{
			newExprs = ExpressionUtil.extractColumnExpressions( value );
		}
		catch ( BirtException e )
		{
			newExprs = null;
		}

		if ( newExprs == null || newExprs.isEmpty( ) )
		{
			return;
		}

		DesignElement target = findTargetElement( );
		for ( int i = 0; i < newExprs.size( ); i++ )
		{
			IColumnBinding boundColumn = (IColumnBinding) newExprs.get( i );
			String newExpression = boundColumn.getBoundExpression( );
			if ( newExpression == null )
				continue;

			DataBoundColumnUtil.setupBoundDataColumn( target, boundColumn
					.getResultSetColumnName( ), newExpression, handler.module );
		}
	}

	/**
	 * Returns the nearest outer container that is either a list/table or a grid
	 * with dataset.
	 * 
	 * @return the element has the dataSet value or <code>null</code> when not
	 *         found.
	 */

	private DesignElement findTargetElement( )
	{
		DesignElement tmpElement = element.getContainer( );
		while ( tmpElement != null )
		{
			if ( !( tmpElement instanceof ReportItem ) )
			{
				tmpElement = tmpElement.getContainer( );
				continue;
			}

			if ( tmpElement instanceof ListingElement )
				break;

			if ( tmpElement instanceof GridItem )
			{
				ElementRefValue dataSetRef = (ElementRefValue) tmpElement
						.getProperty( handler.getModule( ),
								IReportItemModel.DATA_SET_PROP );
				if ( dataSetRef != null )
					break;
			}

			tmpElement = tmpElement.getContainer( );
		}

		return tmpElement;
	}
}

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

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a style. This class is used in two distinct ways. First, it
 * is used to parse and create a named shared style stored in the styles slot of
 * the report design. Second, it is used to parse "private style" information
 * for a report item. Since both contexts use exactly the same XML (except for
 * the name and extends attributes), and both use the same property names,
 * having one state handle both contexts makes the parser simpler.
 * 
 */

class StyleState extends ReportElementState
{

	/**
	 * The element being built. Either a shared style or a report item.
	 */

	protected DesignElement element = null;

	/**
	 * Temporary storage of the list of map rules.
	 */

	protected ArrayList mapRules = null;

	/**
	 * Temporary storage of one map rule as it is being built.
	 */

	protected MapRule mapRule = null;

	/**
	 * Temporary storage of the list of highlight rules.
	 */

	protected ArrayList highlightRules = null;

	/**
	 * Temporary storage of one highlight rule as it is being built.
	 */

	protected HighlightRule highlightRule = null;

	/**
	 * Constructs for creating a named shared style with the design file parser
	 * handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	StyleState( ModuleParserHandler handler )
	{
		super( handler, handler.getModule( ), IReportDesignModel.STYLE_SLOT );
	}

	/**
	 * Constructs for parsing the "private style" of an existing report item
	 * with the design file parser handler and the design element that has
	 * style.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param obj
	 *            the element being built
	 */

	StyleState( ModuleParserHandler handler, DesignElement obj )
	{
		super( handler );
		element = obj;
	}

	/**
	 * Constructs for creating a named shared style with the design file parser
	 * handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	StyleState( ModuleParserHandler handler, Theme element, int slotId )
	{
		super( handler, element, slotId );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new Style( );
		initElement( attrs, true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#isNameSpaceRequired(org.eclipse.birt.report.model.core.DesignElement,
	 *      int, org.eclipse.birt.report.model.core.DesignElement)
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	public void end( )
	{
		makeTestExpressionCompatible( );
	}
}
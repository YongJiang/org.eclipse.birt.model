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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.olap.MeasureGroup;
import org.eclipse.birt.report.model.elements.olap.OdaMeasureGroup;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a measure group within a cube element.
 */
public class OdaMeasureGroupState extends ReportElementState
{

	/**
	 * The measure group being created.
	 */

	protected MeasureGroup element = null;

	/**
	 * Constructs text data item state with the design parser handler, the
	 * container element and the container property name of the report element.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param prop
	 *            the slot in which this element appears
	 */

	public OdaMeasureGroupState( ModuleParserHandler handler,
			DesignElement theContainer, String prop )
	{
		super( handler, theContainer, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new OdaMeasureGroup( );
		initElement( attrs, false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#getElement()
	 */
	public DesignElement getElement( )
	{
		return element;
	}
}

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
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a data set based on javascript. Note: this is temporary
 * syntax, the structure of a data set will be defined by a different team
 * later.
 * 
 */

public class ScriptDataSetState extends DataSetState
{

	/**
	 * Constructs the script data set state with the design file parser handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public ScriptDataSetState( ModuleParserHandler handler )
	{
		super( handler );
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
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new ScriptDataSet( );
		super.parseAttrs( attrs );
	}
}

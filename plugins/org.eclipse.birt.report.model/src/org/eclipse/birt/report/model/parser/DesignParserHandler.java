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

import java.net.URL;
import java.util.Map;

import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * Top-level handler for the XML design file. Recognizes the top-level tags in
 * the file.
 * 
 */

public class DesignParserHandler extends ModuleParserHandler
{

	/**
	 * Constructs the design parser handler with the design session.
	 * 
	 * @param theSession
	 *            the design session that is to own the design
	 * @param systemId
	 *            the uri path for the design file
	 * @param fileName
	 *            name of the design file
	 * @param options
	 *            the options set for this module
	 */

	public DesignParserHandler( DesignSession theSession, URL systemId,
			String fileName, ModuleOption options )
	{
		super( theSession, fileName );
		module = new ReportDesign( session );
		module.setSystemId( systemId );
		module.setFileName( fileName );
		module.setOptions( options );

		initLineNumberMarker( options );
	}

	/**
	 * Constructs the design parser handler with the design session.
	 * 
	 * @param theSession
	 *            the design session that is to own the design
	 * @param systemId
	 *            the uri path for the design file
	 * @param fileName
	 *            name of the design file
	 * @param options
	 *            the options set for this module
	 */

	public DesignParserHandler( DesignSession theSession, URL systemId,
			String fileName, ModuleOption options,
			Map<String, Library> reloadLibs )
	{
		super( theSession, fileName, reloadLibs );
		module = new ReportDesign( session );
		module.setSystemId( systemId );
		module.setFileName( fileName );
		module.setOptions( options );

		initLineNumberMarker( options );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.XMLParserHandler#createStartState()
	 */

	public AbstractParseState createStartState( )
	{
		return new StartState( );
	}

	/**
	 * Recognizes the top-level tags: Report.
	 */

	class StartState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.REPORT_TAG ) )
			{
				if ( markLineNumber )
					tempLineNumbers.put( module, new Integer( locator
							.getLineNumber( ) ) );

				return new ReportState( DesignParserHandler.this );
			}
			return super.startElement( tagName );
		}
	}

}
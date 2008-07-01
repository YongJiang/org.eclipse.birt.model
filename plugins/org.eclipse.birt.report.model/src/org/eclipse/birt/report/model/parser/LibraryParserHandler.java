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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * Top-level handler for the XML library file. Recognizes the top-level tags in
 * the file.
 */

public class LibraryParserHandler extends ModuleParserHandler
{
	/**
	 * Constructor.
	 * 
	 * @param theSession
	 *            the design session
	 * @param host
	 *            the host module
	 * @param systemId
	 *            the library system id
	 * @param fileName
	 *            the file name
	 * @param options
	 *            module options.
	 */

	LibraryParserHandler( DesignSession theSession, Module host, URL systemId,
			String fileName, ModuleOption options )
	{
		super( theSession, fileName );
		module = new Library( theSession, host );
		module.setSystemId( systemId );
		module.setFileName( fileName );
		module.setOptions( options );

		initLineNumberMarker( options );
	}

	/**
	 * Constructor.
	 * 
	 * @param theSession
	 *            the design session
	 * @param host
	 *            the host module
	 * @param systemId
	 *            the library system id
	 * @param fileName
	 *            the file name
	 * @param options
	 *            module options.
	 * @param reloadLibs
	 *            libraries that have been reload
	 */

	LibraryParserHandler( DesignSession theSession, Module host, URL systemId,
			String fileName, ModuleOption options,
			Map<String, Library> reloadLibs )
	{
		super( theSession, fileName, reloadLibs );
		module = new Library( theSession, host );
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
	 * Recognizes the top-level tags: Library.
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
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIBRARY_TAG ) )
			{
				if ( markLineNumber )
					tempLineNumbers.put( module, new Integer( locator
							.getLineNumber( ) ) );
				return new LibraryState( LibraryParserHandler.this );
			}
			return super.startElement( tagName );
		}
	}
}

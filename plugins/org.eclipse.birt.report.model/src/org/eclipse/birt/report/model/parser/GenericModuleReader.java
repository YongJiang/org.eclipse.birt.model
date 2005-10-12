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

import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;

public class GenericModuleReader extends ModuleReader
{

	/**
	 * The one and only design reader.
	 */

	private static GenericModuleReader instance = new GenericModuleReader( );

	/**
	 * Default constructor.
	 * 
	 */

	private GenericModuleReader( )
	{
		// Forbid to instance this class outside.
	}

	/**
	 * Gets the only instance of the design reader.
	 * 
	 * @return the only instance of the design reader
	 */

	public static GenericModuleReader getInstance( )
	{
		return instance;
	}

	/**
	 * Parses an XML module(design/library) file given an input stream. Creates and returns the
	 * internal representation of the module.
	 * 
	 * @param session
	 *            the session of the module
	 * @param fileName
	 *            the module file that the input stream is associated to.
	 * @param inputStream
	 *            the input stream that reads the module file
	 * 
	 * @return the internal representation of the module
	 * @throws DesignFileException
	 *             if the file is not found or has syntax error. The
	 *             syntax errors include that input stream is not well-formed
	 *             xml, that there is unsupported tags and that there is
	 *             run-time exception.
	 */

	public Module read( DesignSession session, String fileName,
			InputStream inputStream ) throws DesignFileException
	{
		URL systemId = URIUtil.getDirectory( fileName );
		GenericModuleParserHandler handler = new GenericModuleParserHandler( session, systemId, fileName );
		return readModule( handler, inputStream );
	}

	/**
	 * Parses an XML library file given an input stream. Creates and returns the
	 * internal representation of the library
	 * 
	 * @param session
	 *            the session of the library
	 * @param systemId
	 *            the uri path for the library file
	 * @param inputStream
	 *            the input stream that reads the library file
	 * @throws DesignFileException
	 *             if the input stream is not well-formed xml, there is
	 *             unsupported tags and there is run-time exception.
	 * @return the internal representation of the library
	 */

	public Module read( DesignSession session, URL systemId,
			InputStream inputStream ) throws DesignFileException
	{
		GenericModuleParserHandler handler = new GenericModuleParserHandler( session, systemId, null );
		return readModule( handler, inputStream );
	}

	/**
	 * Parses an XML module file given a file name. Creates and returns the
	 * internal representation of the module
	 * 
	 * @param session
	 *            the session of the report
	 * @param fileName
	 *            the module file to parse
	 * 
	 * @return the internal representation of the module
	 * @throws DesignFileException
	 *             if the module file is not found or has syntax error. The
	 *             syntax errors include that input stream is not well-formed
	 *             xml, that there is unsupported tags and that there is
	 *             run-time exception.
	 */

	public Module read( DesignSession session, String fileName )
			throws DesignFileException
	{
		URL systemId = URIUtil.getDirectory( fileName );
		GenericModuleParserHandler handler = new GenericModuleParserHandler( session, systemId, fileName );
		return readModule( handler );
	}
}

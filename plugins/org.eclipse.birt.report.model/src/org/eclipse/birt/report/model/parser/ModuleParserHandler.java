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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Abstract handler for the XML module files. Holds the module being created.
 */

public abstract class ModuleParserHandler extends XMLParserHandler
{

	/**
	 * The design session that will own this module.
	 */

	protected DesignSession session = null;

	/**
	 * The module being created.
	 */

	protected Module module = null;

	/**
	 * Catched name of the module file.
	 */

	protected String fileName = null;

	/**
	 * The version of the module file this handle is parsing.
	 */

	String version = "0"; //$NON-NLS-1$

	/**
	 * The temperate value for parser compatible.
	 */

	protected HashMap tempValue = new HashMap( );

	/**
	 * Constructs the module parser handler with the design session.
	 * 
	 * @param theSession
	 *            the design session that is to own this module
	 * @param fileName
	 *            name of the module file
	 */

	public ModuleParserHandler( DesignSession theSession, String fileName )
	{
		super( new ModuleParserErrorHandler( ) );
		this.session = theSession;
		this.fileName = fileName;
	}

	/**
	 * Returns the file name the handler is treating.
	 * 
	 * @return the file name the handler is treating.
	 */

	String getFileName( )
	{
		return this.fileName;
	}

	/**
	 * Returns the version of the module file this handler is parsing.
	 * 
	 * @return version of module file.
	 */

	String getVersion( )
	{
		return version;
	}

	/**
	 * Sets the version of the module file this handler is parsing.
	 * 
	 * @param version
	 *            The version to set.
	 */

	void setVersion( String version )
	{
		this.version = version;
	}

	/**
	 * Returns <code>true</code> if the version of the module file this
	 * handler is parsing equals the given version.
	 * 
	 * @param toCompare
	 *            the version to compare
	 * @return <code>true</code> if the version of the module file this
	 *         handler is parsing equals <code>toCompare</code>.
	 */

	public boolean isVersion( String toCompare )
	{
		assert toCompare != null;

		return version.equals( toCompare );
	}

	/**
	 * Returns the module being created.
	 * 
	 * @return the module being created
	 */

	public Module getModule( )
	{
		return module;
	}

	/**
	 * Overrides the super method. This method first parses attributes of the
	 * current state, and then query whether to use a new state or the current
	 * one according to the attributes value.
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */

	public void startElement( String namespaceURI, String localName,
			String qName, Attributes atts ) throws SAXException
	{
		errorHandler.setCurrentElement( qName );
		AbstractParseState newState = topState( ).startElement( qName );
		newState.parseAttrs( atts );
		AbstractParseState jumpToState = newState.jumpTo( );
		if ( jumpToState != null )
		{
			pushState( jumpToState );
			return;
		}

		newState.setElementName( qName );
		pushState( newState );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */

	public void endDocument( ) throws SAXException
	{
		super.endDocument( );

		// add all the exceptions to the module

		module.getAllExceptions( ).addAll( getErrorHandler( ).getErrors( ) );

		// Check whether duplicate library namespace exists.

		List libraries = module.getAllLibraries( );
		{
			Iterator iter = libraries.iterator( );
			while ( iter.hasNext( ) )
			{
				Library library = (Library) iter.next( );

				if ( !library.isValid( ) )
				{
					// Forward the fatal error to top level.

					Exception fatalException = ModelUtil
							.getFirstFatalException( library.getAllExceptions( ) );
					if ( fatalException != null )
					{
						XMLParserException exception = errorHandler
								.semanticError( fatalException );
						module.getAllExceptions( ).add( exception );
					}
				}
			}
		}

		// Skip the semantic check if we've already found errors.
		// Doing the semantic check would just uncover bogus errors
		// due to the ones we've already seen.

		if ( !module.getAllErrors( ).isEmpty( )
				|| module.getFatalException( ) != null )
		{
			// The most errors which are found during parsing cannot be
			// recovered.

			module.setValid( false );
			List allExceptions = new ArrayList( );
			allExceptions.addAll( module.getAllExceptions( ) );
			allExceptions.addAll( errorHandler.getWarnings( ) );

			DesignFileException exception = new DesignFileException( module
					.getFileName( ), allExceptions );

			throw new SAXException( exception );
		}

		// Perform semantic check. The semantic error is recoverable.

		module.semanticCheck( module );

		// translates warnings during parsing design files to ErrorDetail.

		if ( errorHandler.getWarnings( ) != null )
		{
			module.getAllExceptions( ).addAll( errorHandler.getWarnings( ) );
		}
	}
}

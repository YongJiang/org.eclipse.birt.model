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

package org.eclipse.birt.report.model.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.eclipse.birt.report.model.util.XMLWriter;

/**
 * Writes the XML file with indents.
 * 
 */

public class IndentableXMLWriter extends XMLWriter
{

	/**
	 * Maximum characters in one line.
	 */

	protected final static int MAX_CHARS_PER_LINE = 80;

	/**
	 * Platform-independent line separator.
	 */

	public static final String LINE_SEPARATOR = "\n"; //$NON-NLS-1$

	/**
	 * The indent space.
	 */

	protected static final String TAB = "    "; //$NON-NLS-1$

	/**
	 * The indents which are cached for writing.
	 */

	protected ArrayList cachedIndents = new ArrayList( );

	/**
	 * The name of the tag that is being written.
	 */

	private String currentTagName = null;

	/**
	 * Constructor.
	 * 
	 * @param outputFile
	 *            the file to write
	 * @param signature
	 *            the unicode signature of the design file
	 * @throws java.io.IOException
	 *             if write error occurs
	 */

	public IndentableXMLWriter( File outputFile, String signature )
			throws IOException
	{
		super( outputFile, signature );
	}

	/**
	 * Constructor.
	 * 
	 * @param os
	 *            the output stream to which the design file is written.
	 * @param signature
	 *            the unicode signature of the design file
	 * @throws IOException
	 *             if write error occurs
	 */

	public IndentableXMLWriter( OutputStream os, String signature )
			throws IOException
	{
		super( os, signature );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLWriter#emitStartTag(java.lang.String)
	 */

	protected IndentableXMLWriter( )
	{}
	

	protected void emitStartTag( String tagName )
	{
		// Record the current tag name for endDocument()

		currentTagName = tagName;

		literal( getIndent( elementStack.size( ) ) );

		super.emitStartTag( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLWriter#endElement()
	 */

	public void endElement( )
	{
		String tagName = ""; //$NON-NLS-1$

		// Get the tag name from the top of stack

		if ( elementStack.size( ) > 0 )
			tagName = (String) elementStack.get( elementStack.size( ) - 1 );

		// No indent for the ending tag following the starting tag

		if ( !tagName.equalsIgnoreCase( currentTagName ) )
		{

			literal( getIndent( elementStack.size( ) - 1 ) );
		}
		super.endElement( );
	}

	/**
	 * Returns the indent space for the given level. The level should be step
	 * one each time.
	 * 
	 * @param level
	 *            the indent level
	 * @return the indent space
	 */

	private String getIndent( int level )
	{
		String indent = ""; //$NON-NLS-1$

		if ( cachedIndents.size( ) == 0 )
		{
			cachedIndents.add( indent );
		}
		else if ( cachedIndents.size( ) <= level )
		{
			indent = (String) cachedIndents.get( cachedIndents.size( ) - 1 );
			indent += TAB;
			cachedIndents.add( indent );
		}
		else
		{
			indent = (String) cachedIndents.get( level );
		}

		return indent;
	}

	/**
	 * Returns tabs for the current element.
	 * 
	 * @return the indents for the current XML element.
	 */

	private String getCurElementIndent( )
	{
		if ( cachedIndents.size( ) == 0 )
			return ""; //$NON-NLS-1$

		String indent = (String) cachedIndents.get( elementStack.size( ) - 1 );
		return indent;
	}

	/**
	 * Writes long text to the output.
	 * 
	 * @param text
	 *            the text to write
	 */

	protected void indentLongText( String text )
	{
		assert text != null;
		assert text.length( ) >= MAX_CHARS_PER_LINE;

		closeTextTag( );
		String curTabs = getCurElementIndent( ).concat( TAB );
		literal( LINE_SEPARATOR + curTabs );

		// Write the text character-by-character to encode special characters.

		int len = text.length( );
		for ( int i = 0; i < len; i++ )
		{
			char c = text.charAt( i );
			if ( c == '&' )
				literal( "&amp;" ); //$NON-NLS-1$ 
			else if ( c == '<' )
				literal( "&lt;" ); //$NON-NLS-1$ 
			else
				literal( Character.toString( c ) );

			// append CRLF to the end of the line.

			if ( ( i + 1 != len ) && ( i + 1 ) % MAX_CHARS_PER_LINE == 0 )
				literal( LINE_SEPARATOR + curTabs );
		}

		literal( LINE_SEPARATOR + getCurElementIndent( ) );

	}
}
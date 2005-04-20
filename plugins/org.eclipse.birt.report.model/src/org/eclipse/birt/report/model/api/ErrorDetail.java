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

package org.eclipse.birt.report.model.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Provides detail information about errors found when opening a design file.
 * Includes the error type, error code, error message and so on.
 * <p>
 * Errors with types INVALID_XML, SYNTAX_ERROR or SEMANTIC_ERROR, can be
 * translated to an instance of this class. All error type constants are defined
 * in <code>DesignFileException</code>.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111" align="center">
 * <th width="25%"></th>
 * <th width="25%">Invalid XML File</th>
 * <th width="25%">Syntax Error</th>
 * <th width="25%">Semantic Error</th>
 * 
 * <tr>
 * <td>type</td>
 * <td  align="center">INVALID_XML</td>
 * <td  align="center">SYNTAX_ERROR</td>
 * <td  align="center">SEMANTIC_ERROR</td>
 * </tr>
 * 
 * <tr>
 * <td>error code</td>
 * <td align="center">N/A</td>
 * <td align="center">Y</td>
 * <td align="center">Y</td>
 * </tr>
 * 
 * <tr>
 * <td>exception name</td>
 * <td align="center">Y</td>
 * <td align="center">Y</td>
 * <td align="center">Y</td>
 * </tr>
 * 
 * <tr>
 * <td>message</td>
 * <td align="center">Y</td>
 * <td align="center">Y</td>
 * <td align="center">Y</td>
 * </tr>
 * 
 * <tr>
 * <td>line number</td>
 * <td align="center">Y</td>
 * <td align="center">Y</td>
 * <td align="center">N/A</td>
 * </tr>
 * 
 * <tr>
 * <td>tag number</td>
 * <td align="center">Y</td>
 * <td align="center">Y</td>
 * <td align="center">N/A</td>
 * 
 * <tr>
 * <td>element type</td>
 * <td align="center">N/A</td>
 * <td align="center">N/A</td>
 * <td align="center">Y</td>
 * 
 * <tr>
 * <td>element name</td>
 * <td align="center">N/A</td>
 * <td align="center">N/A</td>
 * <td align="center">Y</td>
 * </tr>
 * </table>
 * 
 * Note output message in this class are locale independent. ONLY for debugging,
 * not user-visible. Debugging messages are defined to be in English.
 * 
 * @see DesignFileException
 */

public final class ErrorDetail
{

	/**
	 * The error type, whose value is defined in {@link DesignFileException}.
	 */

	private String type = null;

	/**
	 * The error code, whose value is defined by the exception class.
	 */

	private String errorCode = null;

	/**
	 * The line number in design file.
	 */

	private int lineNo = 0;

	/**
	 * The tag name which causes error.
	 */

	private String tagName;

	/**
	 * The element which causes error.
	 */

	private DesignElement element;

	/**
	 * The name of the exception class.
	 */

	private String exceptionName;

	/**
	 * The localized error message.
	 */

	private String message;

	/**
	 * The validation ID which is used to identify one specific validation.
	 */

	private String validationID;

	/**
	 * The error description, which is used for <code>toString()</code>.
	 * Generally, it's only used for debug purposes.
	 */

	private StringBuffer description = new StringBuffer( );

	/**
	 * Constructs the error detail with a given exception.
	 * 
	 * @param e
	 *            the exception types that can be translated are
	 *            <code>XMLParserException</code>,
	 *            <code>DesignParserException</code>,
	 *            <code>SemanticException</code> and <code>SAXException</code>.
	 */

	public ErrorDetail( Exception e )
	{
		if ( e instanceof XMLParserException )
		{
			translate( (XMLParserException) e );
		}
		else if ( e instanceof DesignParserException )
		{
			translate( (DesignParserException) e );
		}
		else if ( e instanceof SemanticException )
		{
			translate( (SemanticException) e );
		}
		else if ( e instanceof SAXParseException )
		{
			translate( (SAXParseException) e );
		}
		else if ( e instanceof SAXException )
		{
			translate( (SAXException) e );
		}
		else if ( e instanceof ParserConfigurationException
				|| e instanceof IOException )
		{
			assert false;
		}
		else
		{
			assert false;
		}
	}

	/**
	 * Translates <code>XMLParserException</code> to <code>ErrorDetail</code>.
	 * 
	 * @param e
	 *            a xml parser exception to translate
	 */

	private void translate( XMLParserException e )
	{
		type = DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR;
		lineNo = e.getLineNumber( );
		tagName = e.getTag( );

		description.append( " ( line = " ); //$NON-NLS-1$
		description.append( e.getLineNumber( ) );
		description.append( ", " ); //$NON-NLS-1$
		description.append( "tag = " ); //$NON-NLS-1$
		description.append( e.getTag( ) );
		description.append( ") " ); //$NON-NLS-1$

		if ( e.getErrorCode( ).equalsIgnoreCase(
				XMLParserException.DESIGN_EXCEPTION_EXCEPTION ) ) //$NON-NLS-1$
		{
			if ( e.getException( ) instanceof DesignParserException )
			{
				translate( (DesignParserException) e.getException( ) );
			}
			else if ( e.getException( ) instanceof SemanticException )
			{
				translate( (SemanticException) e.getException( ) );
			}
		}
		else
		{
			exceptionName = e.getClass( ).getName( );
			errorCode = e.getErrorCode( );
			message = e.getMessage( );

			description.append( e.getClass( ).getName( ) );
			description.append( " (" ); //$NON-NLS-1$
			description.append( "code = " ); //$NON-NLS-1$
			description.append( e.getErrorCode( ) );
			description.append( ", " ); //$NON-NLS-1$
			description.append( "message : " ); //$NON-NLS-1$
			description.append( e.getMessage( ) );
			description.append( ")" ); //$NON-NLS-1$
		}
	}

	/**
	 * Translates <code>DesignParserException</code> to
	 * <code>ErrorDetail</code>.
	 * 
	 * @param e
	 *            a design file exception to translate
	 */

	private void translate( DesignParserException e )
	{
		type = DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR;
		exceptionName = e.getClass( ).getName( );
		errorCode = e.getErrorCode( );
		message = e.getMessage( );

		description.append( " " ); //$NON-NLS-1$
		description.append( e.getClass( ).getName( ) );
		description.append( " (" ); //$NON-NLS-1$
		description.append( "code = " ); //$NON-NLS-1$
		description.append( e.getErrorCode( ) );
		description.append( ", " ); //$NON-NLS-1$
		description.append( "message : " ); //$NON-NLS-1$
		description.append( e.getMessage( ) );
		description.append( ")" ); //$NON-NLS-1$

	}

	/**
	 * Translates <code>SemanticException</code> to <code>ErrorDetail</code>.
	 * 
	 * @param e
	 *            a semantic exception to translate
	 */

	private void translate( SemanticException e )
	{
		type = DesignFileException.DESIGN_EXCEPTION_SEMANTIC_ERROR;

		if ( ( e instanceof SemanticError )
				&& ( ( (SemanticError) e ).getErrorLevel( ) == SemanticError.WARNING ) )
			type = DesignFileException.DESIGN_EXCEPTION_SEMANTIC_WARNING;

		element = e.getElement( );

		exceptionName = e.getClass( ).getName( );
		errorCode = e.getErrorCode( );
		message = e.getMessage( );

		description.append( " " ); //$NON-NLS-1$
		if ( e.getElement( ) != null )
		{
			description.append( " (Element = " ); //$NON-NLS-1$
			description.append( e.getElement( ).getElementName( ) );
			description.append( ", Name = " ); //$NON-NLS-1$
			description.append( e.getElement( ).getName( ) );
			description.append( "), " ); //$NON-NLS-1$
		}
		description.append( e.getClass( ).getName( ) );
		description.append( " (" ); //$NON-NLS-1$
		if ( e instanceof SemanticError )
		{
			int level = ( (SemanticError) e ).getErrorLevel( );

			if ( level == SemanticError.ERROR )
				description.append( "level = error, " ); //$NON-NLS-1$					
			else if ( level == SemanticError.WARNING )
				description.append( "level = warning, " ); //$NON-NLS-1$
		}
		description.append( "code = " ); //$NON-NLS-1$
		description.append( e.getErrorCode( ) );
		description.append( ", " ); //$NON-NLS-1$
		description.append( "message : " ); //$NON-NLS-1$
		description.append( e.getMessage( ) );
		description.append( ")" ); //$NON-NLS-1$
	}

	/**
	 * Translates <code>SAXParseException</code>.
	 * 
	 * @param e
	 *            exception to translate
	 */

	private void translate( SAXParseException e )
	{
		type = DesignFileException.DESIGN_EXCEPTION_INVALID_XML;

		exceptionName = e.getClass( ).getName( );
		message = e.getMessage( );

		description.append( " ( line = " ); //$NON-NLS-1$
		description.append( e.getLineNumber( ) );
		description.append( ") " ); //$NON-NLS-1$
		description.append( e.getClass( ).getName( ) );
		description.append( " (" ); //$NON-NLS-1$
		description.append( "message : " ); //$NON-NLS-1$
		description.append( e.getMessage( ) );
		description.append( ")" ); //$NON-NLS-1$

		if ( e.getCause( ) != null && e.getCause( ) instanceof RuntimeException )
		{
			translateRuntimeException( (RuntimeException) e.getCause( ) );
		}
	}

	/**
	 * Translates <code>SAXException</code>.
	 * 
	 * @param e
	 *            a SAX exception to translate
	 */

	private void translate( SAXException e )
	{
		type = DesignFileException.DESIGN_EXCEPTION_INVALID_XML;

		exceptionName = e.getClass( ).getName( );
		message = e.getMessage( );

		description.append( " " ); //$NON-NLS-1$
		description.append( e.getClass( ).getName( ) );
		description.append( " (" ); //$NON-NLS-1$
		description.append( "message : " ); //$NON-NLS-1$
		description.append( e.getMessage( ) );
		description.append( ")" ); //$NON-NLS-1$

		if ( e.getCause( ) != null && e.getCause( ) instanceof RuntimeException )
		{
			translateRuntimeException( (RuntimeException) e.getCause( ) );
		}
	}

	/**
	 * Translates the <code>RuntimeException</code> to printable string.
	 * 
	 * @param e
	 *            the runtime exception
	 */

	private void translateRuntimeException( RuntimeException e )
	{
		assert e != null;

		StackTraceElement[] elements = e.getStackTrace( );
		for ( int i = 0; i < elements.length; i++ )
		{
			description.append( "\tat " ); //$NON-NLS-1$
			description.append( elements[i].getClassName( ) );
			description.append( "(" ); //$NON-NLS-1$
			description.append( elements[i].getFileName( ) );
			description.append( ":" ); //$NON-NLS-1$
			description.append( elements[i].getLineNumber( ) );
			description.append( ")\n" ); //$NON-NLS-1$
		}
	}

	/**
	 * Note output message are locale independent. ONLY for debugging, not
	 * user-visible. Therefore, no NON-NLS required.
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString( )
	{
		return description.toString( );
	}

	/**
	 * Returns the element that causes error.
	 * 
	 * @return the element that causes error.
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/**
	 * Returns the error code of the cause of the exception. The value is
	 * defined in the cause exception class.
	 * 
	 * @return error code
	 */

	public String getErrorCode( )
	{
		return errorCode; //$NON-NLS-1$
	}

	/**
	 * Returns the name of exception with package prefix.
	 * 
	 * @return name of exception.
	 */

	public String getExceptionName( )
	{
		return exceptionName;
	}

	/**
	 * Returns line number in design file, where error is found. Note: For
	 * INVALID_XML, the line number is not accurate. Basically, the error can be
	 * found after the indicated line a bit.
	 * 
	 * @return line number in design file
	 */

	public int getLineNo( )
	{
		return lineNo;
	}

	/**
	 * Returns the error message.
	 * 
	 * @return the localized error message as a string
	 */

	public String getMessage( )
	{
		return message;
	}

	/**
	 * Returns the tag name that causes error.
	 * 
	 * @return the tag name
	 */

	public String getTagName( )
	{
		return tagName;
	}

	/**
	 * Returns the error type. The value is defined in
	 * <code>DesignFileException</code>.
	 * 
	 * @return the error type
	 * 
	 * @see DesignFileException
	 */

	public String getType( )
	{
		return type;
	}

	/**
	 * Sets the validation ID, which identifies one specific validation.
	 * 
	 * @param validationID
	 *            the validation ID
	 */

	public void setValidationID( String validationID )
	{
		this.validationID = validationID;
	}

	/**
	 * Returns the validation ID, which identifies one specific validation.
	 * 
	 * @return the validator ID
	 */

	public String getValidationID( )
	{
		return validationID;
	}

	/**
	 * Converts the exception list to the error detail list.
	 * 
	 * @param exceptionList
	 *            list of <code>SemanticException</code>.
	 * @return the error detail list of <code>ErrorDetail</code>
	 */

	public static List convertExceptionList( List exceptionList )
	{
		List errorDetailList = new ArrayList( );

		Iterator iterError = exceptionList.iterator( );
		while ( iterError.hasNext( ) )
		{
			SemanticException e = (SemanticException) iterError.next( );

			ErrorDetail errorDetail = new ErrorDetail( e );
			errorDetailList.add( errorDetail );
		}

		return errorDetailList;
	}
}
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.parser.ActionStructureState;
import org.eclipse.birt.report.model.parser.DesignReader;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.parser.GenericModuleReader;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.parser.ModuleParserHandler;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.eclipse.birt.report.model.writer.IndentableXMLWriter;
import org.eclipse.birt.report.model.writer.ModuleWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Provides some tool methods about the modules.
 */

public class ModuleUtil
{

	/**
	 * The library type.
	 */

	public final static int LIBRARY = 0;

	/**
	 * The report design type.
	 */

	public final static int REPORT_DESIGN = 1;

	/**
	 * The invalid module.
	 */

	public final static int INVALID_MODULE = 2;

	/**
	 * Parser handler used to parse only the Action part of the design. The
	 * existing action state and property validating is reused.
	 */

	private static class ActionParserHandler extends ModuleParserHandler
	{

		/**
		 * A fake element with the given action. Used to reuse the existing
		 * action parser logic.
		 */

		DesignElement element = null;

		public ActionParserHandler( DesignElement element )
		{
			super( null, null );
			this.element = element;
			module = new ReportDesign( null );
			
			setVersion( DesignSchemaConstants.REPORT_VERSION );
		}

		public AbstractParseState createStartState( )
		{
			return new StartState( );
		}

		/**
		 * Recognizes the top-level tags: Report or Library
		 */

		class StartState extends InnerParseState
		{

			public AbstractParseState startElement( String tagName )
			{
				if ( DesignSchemaConstants.STRUCTURE_TAG
						.equalsIgnoreCase( tagName ) )
					return new ActionStructureState( ActionParserHandler.this,
							element );
				return super.startElement( tagName );
			}
		}

	}

	/**
	 * Deserialize an input stream into an Action.
	 * 
	 * @param streamData
	 *            a stream represent an action.
	 * @return an internal Action structure
	 * @throws DesignFileException
	 *             if the exception occur when interpret the stream data.
	 */

	public static ActionHandle deserializeAction( InputStream streamData )
			throws DesignFileException
	{

		// A fake element with the given action. Used to reuse the existing
		// action parser logic.

		ImageItem image = new ImageItem( );
		ActionParserHandler handler = new ActionParserHandler( image );

		if ( streamData == null )
		{
			Action action = StructureFactory.createAction( );
			image.setProperty( ImageHandle.ACTION_PROP, action );
			return ( (ImageHandle) image.getHandle( handler.getModule( ) ) )
					.getActionHandle( );
		}

		if ( !streamData.markSupported( ) )
			streamData = new BufferedInputStream( streamData );

		assert streamData.markSupported( );

		try
		{
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance( );
			SAXParser parser = saxParserFactory.newSAXParser( );
			InputSource inputSource = new InputSource( streamData );
			inputSource.setEncoding( UnicodeUtil.SIGNATURE_UTF_8 );
			parser.parse( inputSource, handler );
		}
		catch ( SAXException e )
		{
			List errors = handler.getErrorHandler( ).getErrors( );

			// Syntax error is found

			if ( e.getException( ) instanceof DesignFileException )
			{
				throw (DesignFileException) e.getException( );
			}

			// Invalid xml error is found

			throw new DesignFileException( null, errors, e );
		}
		catch ( ParserConfigurationException e )
		{
			throw new DesignFileException( null, handler.getErrorHandler( )
					.getErrors( ), e );
		}
		catch ( IOException e )
		{
			throw new DesignFileException( null, handler.getErrorHandler( )
					.getErrors( ), e );
		}

		ImageHandle imageHandle = (ImageHandle) image.getHandle( handler
				.getModule( ) );
		return imageHandle.getActionHandle( );
	}

	/**
	 * Deserialize a string into an ActionHandle, notice that the handle is
	 * faked, the action is not in the design tree, the operation to the handle
	 * is not able to be undoned.
	 * 
	 * @param strData
	 *            a string represent an action.
	 * @return a handle to the action.
	 * @throws DesignFileException
	 *             if the exception occur when interpret the stream data.
	 */

	public static ActionHandle deserializeAction( String strData )
			throws DesignFileException
	{
		InputStream is = null;
		strData = StringUtil.trimString( strData );
		if ( strData != null )
		{
			try
			{
				is = new ByteArrayInputStream( strData
						.getBytes( UnicodeUtil.SIGNATURE_UTF_8 ) );
			}
			catch ( UnsupportedEncodingException e )
			{
				assert false;
			}

		}
		return deserializeAction( is );
	}

	/**
	 * Serialize an action into a stream, the stream is in UTF-8 encoding.
	 * 
	 * @param action
	 *            a given action structure.
	 * @return an output stream represent the action xml data.
	 * @throws IOException
	 *             if I/O exception occur when writing the stream.
	 */

	public static String serializeAction( ActionHandle action )
			throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream( );
		ActionWriter writer = new ActionWriter( );
		writer.write( os, (Action) action.getStructure( ) );
		try
		{
			return os.toString( UnicodeUtil.SIGNATURE_UTF_8 );
		}
		catch ( UnsupportedEncodingException e )
		{
			assert false;
		}

		return os.toString( );
	}

	private static class SectionXMLWriter extends IndentableXMLWriter
	{

		public SectionXMLWriter( OutputStream os, String signature )
				throws IOException
		{
			super( );
			out = new PrintStream( os, false, OUTPUT_ENCODING );
		}
	}

	/**
	 * Write an action into a stream in UTF-8 encoding.
	 */

	private static class ActionWriter extends ModuleWriter
	{

		/**
		 * Wirte the action in to a stream.
		 * 
		 * @param os
		 * @param action
		 * @throws IOException
		 */
		public void write( OutputStream os, Action action ) throws IOException
		{
			writer = new SectionXMLWriter( os, UnicodeUtil.SIGNATURE_UTF_8 );
			writeAction( action, ImageItem.ACTION_PROP );
		}

		protected Module getModule( )
		{
			return null;
		}
	}

	/**
	 * Justifies whether a given input stream is a valid report design.
	 * 
	 * @param sessionHandle
	 *            the current session of the report design
	 * @param fileName
	 *            the file name of the report design
	 * @param is
	 *            the input stream of the report design
	 * @return true if the input stream is a valid report design, otherwise
	 *         false
	 */

	public static boolean isValidDesign( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		ReportDesign design = null;
		try
		{
			design = DesignReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is );
			return design != null;
		}
		catch ( DesignFileException e )
		{
			return false;
		}
	}

	/**
	 * Justifies whether a library resource with the given file name is a valid
	 * library.
	 * 
	 * @param sessionHandle
	 *            the current session of the library
	 * @param fileName
	 *            the file name of the library
	 * @param is
	 *            the input stream of the library
	 * @return true if the library resource is a valid library, otherwise false
	 */

	public static boolean isValidLibrary( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		Library lib = null;
		try
		{
			lib = LibraryReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is );
			return lib != null;
		}
		catch ( DesignFileException e )
		{
			return false;
		}
	}

	/**
	 * Checks the input stream with given file name. If it is a valid
	 * design/library, return <code>ModuleUtil.REPORT_DESIGN</code>/<code>ModuleUtil.LIBRARY</code>,
	 * otherwise, <code>ModuleUtil.INVALID</code> is return.
	 * 
	 * @param sessionHandle
	 *            the current session of the library
	 * @param fileName
	 *            the file name of the library
	 * @param is
	 *            the input stream of the library
	 * @return <code>ModuleUtil.REPORT_DESIGN</code> if the input stream is a
	 *         report design, <code>ModuleUtil.LIBRARY</code> if the input
	 *         stream is a library, <code>ModuleUtil.INVALID</code> otherwise.
	 */

	public static int checkModule( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		Module rtnModule = null;
		try
		{
			rtnModule = GenericModuleReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is );
		}
		catch ( DesignFileException e )
		{
			return INVALID_MODULE;
		}

		return rtnModule instanceof Library ? LIBRARY : REPORT_DESIGN;
	}

	/**
	 * Finds the result columns on DataSet and update the bound column with the
	 * element that uses this data set. Operations are in persistent
	 * transaction. Hence, operations in the method can not undo/redo.
	 * 
	 * Parts of migration work from BIRT 2.1M5 to BIRT 2.1.0 for bound data
	 * columns.
	 * 
	 * @param module
	 *            the libray/report instance
	 * 
	 * @throws SemanticException
	 *             if values in result set columns are not valid in data bound
	 *             columns.
	 * @deprecated temporarily since another way to implement this feature.
	 * 
	 */

	public static void updateBoundDataColumns( ModuleHandle module )
			throws SemanticException
	{
		ContentIterator contentIter = new ContentIterator( module.getElement( ) );
		if ( !contentIter.hasNext( ) )
			return;

		CommandStack cmdStack = module.getCommandStack( );
		cmdStack.startPersistentTrans( null );

		try
		{
			while ( contentIter.hasNext( ) )
			{
				DesignElement element = (DesignElement) contentIter.next( );
				if ( !( element instanceof ReportItem ) )
					continue;

				ReportItemHandle reportItem = (ReportItemHandle) element
						.getHandle( module.getModule( ) );

				DataSetHandle dataSet = reportItem.getDataSet( );
				if ( dataSet == null )
					continue;

				setupBoundDataColumns( dataSet, reportItem );
			}

		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
	}

	/**
	 * Creates data bound columns from result set columns of the given data set
	 * for the given report item.
	 * 
	 * @param dataSet
	 *            the data set cached result set columns.
	 * @param reportItem
	 *            the target report item.
	 */

	private static void setupBoundDataColumns( DataSetHandle dataSet,
			ReportItemHandle reportItem ) throws SemanticException
	{
		CachedMetaDataHandle cachedMetaData = dataSet.getCachedMetaDataHandle( );
		if ( cachedMetaData == null )
			return;

		List columns = cachedMetaData.getResultSet( ).getListValue( );
		if ( columns == null || columns.isEmpty( ) )
			return;

		for ( int i = 0; i < columns.size( ); i++ )
		{
			ResultSetColumn setColumn = (ResultSetColumn) columns.get( i );
			String name = setColumn.getColumnName( );
			String dataType = setColumn.getDataType( );

			createDataBinding( reportItem, ExpressionUtil
					.createRowExpression( name ), dataType );
		}
	}

	/**
	 * Creates a data binding on the target element.
	 * 
	 * @param target
	 *            the element
	 * @param value
	 *            the column expression. In default, it is also the column name.
	 */

	private static void createDataBinding( ReportItemHandle target,
			String expression, String dataType ) throws SemanticException
	{
		List columns = target
				.getListProperty( IReportItemModel.BOUND_DATA_COLUMNS_PROP );

		ComputedColumn column = DataBoundColumnUtil.getColumn( columns,
				expression );
		if ( column == null )
		{
			column = StructureFactory.createComputedColumn( );
			String newName = DataBoundColumnUtil.handleDuplicateName( columns );
			column.setName( newName );
			column.setDataType( dataType );
			column.setExpression( expression );

			target.addColumnBinding( column, false );
		}
		else
		{
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) column
					.handle(
							target
									.getPropertyHandle( IReportItemModel.BOUND_DATA_COLUMNS_PROP ),
							columns.indexOf( column ) );
			columnHandle.setDataType( dataType );
		}

		return;
	}

}

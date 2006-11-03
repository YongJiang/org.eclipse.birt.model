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

package org.eclipse.birt.report.model.adapter.oda.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.util.SerializerImpl;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;

import com.ibm.icu.util.ULocale;

/**
 * This class is abstract class used for tests, which contains the design file
 * name and report design handle, and provides the basic design file reading
 * methods.
 * 
 * This class performs mainly the following functionalities:
 * <p>
 * <ul>
 * <li>In Setup(), initialize the meda data and store information about meta
 * data in MetaDataDictionary</li>
 * <li>Open a design file, and store the
 * {@link org.eclipse.birt.report.model.elements.ReportDesign}instance and its
 * handle, {@link org.eclipse.birt.report.model.api.ReportDesignHandle}</li>
 * <li>After opening the design file, if the design file contains some syntax
 * or semantic error, the error list can be accessed by this class. This is to
 * make it easy when developing the test cases</li>
 * </ul>
 * <p>
 * Note:
 * <li>The derived unit test class must call 'super.setUp()' in their 'setUp'
 * method.</li>
 * <li>In testing environment, when open a design file by calling openDesign(
 * String fileName, InputStream is ), you can simply pass 'null' as the file
 * name; but, when printing out the error list, to make the file name appear in
 * the message, you can call 'design.setfileName( fileName )' in the child test
 * case.</li>
 * 
 */
public abstract class BaseTestCase extends TestCase
{

	/**
	 * The report design handle.
	 */

	protected ReportDesignHandle designHandle = null;

	/**
	 * The report design handle.
	 */

	protected LibraryHandle libraryHandle = null;

	/**
	 * The opened module handle.
	 */

	protected ModuleHandle moduleHandle = null;

	/**
	 * The session handle.
	 */

	protected SessionHandle sessionHandle = null;

	/**
	 * The file name of metadata file.
	 */
	protected static final String ROM_DEF_NAME = "rom.def"; //$NON-NLS-1$

	protected static final String TEST_FOLDER = "test/"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "/output/"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "/input/"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "/golden/"; //$NON-NLS-1$

	protected static final ULocale TEST_LOCALE = new ULocale( "aa" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		if ( designHandle != null )
			designHandle.close( );

		super.tearDown( );
	}

	/**
	 * Creates a new report.
	 * 
	 * @return the handle for new report
	 */

	protected ReportDesignHandle createDesign( )
	{
		return createDesign( null );
	}

	/**
	 * Creates a new report with given locale.
	 * 
	 * @param locale
	 *            the user locale
	 * @return the handle for new report
	 */

	protected ReportDesignHandle createDesign( ULocale locale )
	{
		sessionHandle = DesignEngine.newSession( locale );
		designHandle = sessionHandle.createDesign( );

		return designHandle;
	}

	/**
	 * Creates a new library with default locale.
	 * 
	 * @return the handle for new library
	 */

	protected LibraryHandle createLibrary( )
	{
		return createLibrary( null );
	}

	/**
	 * Creates library with given locale.
	 * 
	 * @param locale
	 *            the user locale
	 * @return the handle for new library
	 */

	protected LibraryHandle createLibrary( ULocale locale )
	{
		sessionHandle = DesignEngine.newSession( locale );
		libraryHandle = sessionHandle.createLibrary( );

		return libraryHandle;
	}

	/**
	 * Opens design file with default locale.
	 * 
	 * @param fileName
	 *            design file name
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openDesign( String fileName ) throws DesignFileException
	{
		openDesign( fileName, ULocale.getDefault( ) );
	}

	/**
	 * Opens design file providing the file name and the locale.
	 * 
	 * @param fileName
	 *            the design file to be opened
	 * @param locale
	 *            the user locale
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesign( String fileName, ULocale locale )
			throws DesignFileException
	{
		fileName = getClassFolder( ) + INPUT_FOLDER + fileName;
		sessionHandle = DesignEngine.newSession( locale );
		assertNotNull( sessionHandle );

		designHandle = sessionHandle.openDesign( fileName );
	}

	/**
	 * Opens library file with given file name.
	 * 
	 * @param fileName
	 *            the library file name
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openLibrary( String fileName ) throws DesignFileException
	{
		openLibrary( fileName, ULocale.getDefault( ) );
	}

	/**
	 * Opens library file with given file name and locale.
	 * 
	 * @param fileName
	 *            the library file name
	 * @param locale
	 *            the user locale
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openLibrary( String fileName, ULocale locale )
			throws DesignFileException
	{
		fileName = getClassFolder( ) + INPUT_FOLDER + fileName;
		sessionHandle = DesignEngine.newSession( locale );
		assertNotNull( sessionHandle );

		libraryHandle = sessionHandle.openLibrary( fileName );
	}

	/**
	 * Opens a module file with given file name.
	 * 
	 * @param fileName
	 *            the module file name
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openModule( String fileName ) throws DesignFileException
	{
		openModule( fileName, ULocale.getDefault( ) );
	}

	/**
	 * Opend a module given file name and locale.
	 * 
	 * @param fileName
	 *            the module file name
	 * @param locale
	 *            the user locale
	 * @throws DesignFileException
	 */

	protected void openModule( String fileName, ULocale locale )
			throws DesignFileException
	{
		fileName = getClassFolder( ) + INPUT_FOLDER + fileName;
		sessionHandle = DesignEngine.newSession( locale );
		assertNotNull( sessionHandle );

		moduleHandle = sessionHandle.openModule( fileName );
	}

	/**
	 * Opens design file as resource with default locale.
	 * 
	 * @param fileName
	 *            the file name without path
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesignAsResource( Class theClass, String fileName )
			throws DesignFileException
	{
		openDesignAsResource( theClass, fileName, ULocale.getDefault( ) );
	}

	/**
	 * Opens design file as resource with the given locale.
	 * 
	 * @param fileName
	 *            the file name without path
	 * @param locale
	 *            the given locale
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesignAsResource( Class theClass, String fileName,
			ULocale locale ) throws DesignFileException
	{
		fileName = getFullQualifiedClassName( ) + INPUT_FOLDER + fileName;
		sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		assertNotNull( sessionHandle );

		InputStream stream = theClass.getResourceAsStream( fileName );
		designHandle = sessionHandle.openDesign( fileName, stream );
	}

	/**
	 * Reads design file as InputStream.
	 * 
	 * @param fileName
	 *            Design file name
	 * @param is
	 *            InputStream of this design file
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesign( String fileName, InputStream is )
			throws DesignFileException
	{
		openDesign( fileName, is, ULocale.getDefault( ) );
	}

	/**
	 * Opens a design file.
	 * 
	 * @param fileName
	 *            the design file name
	 * @param is
	 *            the input stream of the design file.
	 * @param locale
	 *            the user locale.
	 * @throws DesignFileException
	 *             if any exception.
	 */
	protected void openDesign( String fileName, InputStream is, ULocale locale )
			throws DesignFileException
	{
		sessionHandle = DesignEngine.newSession( locale );
		designHandle = sessionHandle.openDesign( fileName, is );
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param goldenFileName
	 *            the 1st file name to be compared.
	 * @param outputFileName
	 *            the 2nd file name to be compared.
	 * @return true if two text files are same line by line
	 * @throws Exception
	 *             if any exception.
	 */
	protected boolean compareTextFile( String goldenFileName,
			String outputFileName ) throws Exception
	{
		FileReader readerA = null;
		FileReader readerB = null;
		boolean same = true;
		StringBuffer errorText = new StringBuffer( );

		try
		{
			goldenFileName = getClassFolder( ) + GOLDEN_FOLDER + goldenFileName;
			outputFileName = getClassFolder( ) + OUTPUT_FOLDER + outputFileName;

			readerA = new FileReader( goldenFileName );
			readerB = new FileReader( outputFileName );

			same = compareTextFile( readerA, readerB );
		}
		catch ( IOException e )
		{
			errorText.append( e.toString( ) );
			errorText.append( "\n" ); //$NON-NLS-1$
			e.printStackTrace( );
		}
		finally
		{
			try
			{
				readerA.close( );
				readerB.close( );
			}
			catch ( Exception e )
			{
				readerA = null;
				readerB = null;

				errorText.append( e.toString( ) );

				throw new Exception( errorText.toString( ) );
			}
		}

		return same;
	}

	/**
	 * Compares the two text files.
	 * 
	 * @param goldenReader
	 *            the reader for golden file
	 * @param outputReader
	 *            the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception
	 *             if any exception
	 */
	private boolean compareTextFile( Reader goldenReader, Reader outputReader )
			throws Exception
	{
		StringBuffer errorText = new StringBuffer( );

		BufferedReader lineReaderA = null;
		BufferedReader lineReaderB = null;
		boolean same = true;
		int lineNo = 1;
		try
		{
			lineReaderA = new BufferedReader( goldenReader );
			lineReaderB = new BufferedReader( outputReader );

			String strA = lineReaderA.readLine( ).trim( );
			String strB = lineReaderB.readLine( ).trim( );
			while ( strA != null )
			{
				same = strA.trim( ).equals( strB.trim( ) );
				if ( !same )
				{
					StringBuffer message = new StringBuffer( );

					message.append( "line=" ); //$NON-NLS-1$
					message.append( lineNo );
					message.append( " is different:\n" );//$NON-NLS-1$
					message.append( " The line from golden file: " );//$NON-NLS-1$
					message.append( strA );
					message.append( "\n" );//$NON-NLS-1$
					message.append( " The line from output file: " );//$NON-NLS-1$
					message.append( strB );
					message.append( "\n" );//$NON-NLS-1$
					throw new Exception( message.toString( ) );
				}

				strA = lineReaderA.readLine( );
				strB = lineReaderB.readLine( );
				lineNo++;
			}
			same = strA == null && strB == null;
		}
		finally
		{
			try
			{
				lineReaderA.close( );
				lineReaderB.close( );
			}
			catch ( Exception e )
			{
				lineReaderA = null;
				lineReaderB = null;

				errorText.append( e.toString( ) );

				throw new Exception( errorText.toString( ) );
			}
		}

		return same;
	}

	/**
	 * Prints out all semantic errors stored in the error list during parsing
	 * the design file.
	 * 
	 * @param design
	 *            report design
	 */

	protected void printSemanticError( ReportDesign design )
	{
		if ( design != null )
			printErrorList( design.getAllErrors( ) );
	}

	/**
	 * Prints out all syntax errors stored in the error list during parsing the
	 * design file.
	 * 
	 * @param e
	 *            <code>DesignFileException</code> containing syntax error
	 *            list.
	 */

	protected void printSyntaxError( DesignFileException e )
	{
		if ( e != null )
			printErrorList( e.getErrorList( ) );
	}

	/**
	 * Prints error list.
	 * 
	 * @param errors
	 *            error list
	 */
	private void printErrorList( List errors )
	{
		if ( errors != null && !errors.isEmpty( ) )
		{
			for ( Iterator iter = errors.iterator( ); iter.hasNext( ); )
			{
				ErrorDetail ex = (ErrorDetail) iter.next( );
				System.out.println( ex );
			}
		}
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#saveAs(String)}to save the output file of some
	 * unit test. The output test file will be saved in the folder of 'output'
	 * under the folder where the unit test java source file locates, so before
	 * calling {@link ReportDesignHandle#saveAs(String)}, the file name will be
	 * modified to include the path information. For example, in a unit test
	 * class, it can call saveAs( "PropertyCommandTest.out" ).
	 * 
	 * @param filename
	 *            the test output file to be saved.
	 * @throws IOException
	 *             if error occurs while saving the file.
	 */

	protected void saveAs( String filename ) throws IOException
	{
		saveAs( designHandle, filename );
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#saveAs(String)}to save the output file of some
	 * unit test. The output test file will be saved in the folder of 'output'
	 * under the folder where the unit test java source file locates, so before
	 * calling {@link ReportDesignHandle#saveAs(String)}, the file name will be
	 * modified to include the path information. For example, in a unit test
	 * class, it can call saveAs( "PropertyCommandTest.out" ).
	 * 
	 * @param moduleHandle
	 *            the module to save, either a report design or a library
	 * @param filename
	 *            the test output file to be saved.
	 * @throws IOException
	 *             if error occurs while saving the file.
	 */

	protected void saveAs( ModuleHandle moduleHandle, String filename )
			throws IOException
	{
		if ( moduleHandle == null )
			return;
		String outputPath = getClassFolder( ) + OUTPUT_FOLDER;
		File outputFolder = new File( outputPath );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" ); //$NON-NLS-1$
		}
		moduleHandle.saveAs( outputPath + filename );
	}

	/**
	 * Saves library as the given file name.
	 * 
	 * @param filename
	 *            the file name for saving
	 * @throws IOException
	 *             if any exception
	 */

	protected void saveLibraryAs( String filename ) throws IOException
	{
		saveAs( libraryHandle, filename );
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder( )
	{
		String pathBase = null;

		ProtectionDomain domain = this.getClass( ).getProtectionDomain( );
		if ( domain != null )
		{
			CodeSource source = domain.getCodeSource( );
			if ( source != null )
			{
				URL url = source.getLocation( );
				pathBase = url.getPath( );

				if ( pathBase.endsWith( "bin/" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 4 );
				if ( pathBase.endsWith( "bin" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 3 );
			}
		}

		pathBase = pathBase + TEST_FOLDER;
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = pathBase + className.replace( '.', '/' );

		return className;
	}

	/**
	 * Returns the full qualified class name. For example,
	 * "/org/eclipse/birt/report/model".
	 * 
	 * @return the full qualified class name
	 */

	protected String getFullQualifiedClassName( )
	{
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = "/" + className.replace( '.', '/' ); //$NON-NLS-1$

		return className;
	}

	/**
	 * @param values
	 * @param fileName
	 * @throws IOException
	 */

	protected void saveDesignValuesToFile( DesignValues values, String fileName )
			throws IOException
	{
		File outputFolder = new File( getClassFolder( ) + OUTPUT_FOLDER );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" ); //$NON-NLS-1$
		}

		File f = new File( outputFolder, fileName );
		FileOutputStream fos = new FileOutputStream( f );

		SerializerImpl.instance( ).write( values, fos );

		fos.close( );
	}

}
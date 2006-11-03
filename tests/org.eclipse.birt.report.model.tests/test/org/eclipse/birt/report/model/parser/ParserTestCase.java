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

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * This abstract test case provides basic operation for parser and writer test.
 * 
 */
public abstract class ParserTestCase extends BaseTestCase
{
 
    /**
     * Opens the design file and write it to out file. Compares the out file
     * with godlen file. 
     * 
     * @param fileName design file to open
     * @param outFileName out file to write
     * @param goldenFileName golden file to compare
     * @return Return true, if the out file is same as golden file.
     * @throws Exception
     */
    protected boolean openWriteAndCompare( String fileName, String outFileName, 
                                           String goldenFileName)
    	throws Exception
    {
        openDesign( fileName );

        saveAs( outFileName ); 

        return compareTextFile( goldenFileName, outFileName ) ; 
    }
}
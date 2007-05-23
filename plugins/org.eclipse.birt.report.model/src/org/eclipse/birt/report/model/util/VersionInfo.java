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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.IVersionInfo;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Holds instructions that inform the user if opening the old version design
 * file may cause some auto-conversion to the original design file.
 */

public class VersionInfo implements IVersionInfo
{

	/**
	 * The version of the design file.
	 */

	private String version = null;

	/**
	 * Info key to get the info message.
	 */

	private int infoCode = -1;

	/**
	 * The opening design file is not a valid design file or the file does not
	 * exsit.
	 */

	private final static String INVALID_DESIGN_FILE_MSG = MessageConstants.VERSION_INFO_INVALID_DESIGN_FILE;

	/**
	 * Information for user that the opening design file holds a version number
	 * before the "column binding" feature is supported. To Open the file may
	 * need convert the original file automatically.
	 */

	private final static String CONVERT_INFO_MSG = MessageConstants.VERSION_INFO_CONVERT_INFO;

	/**
	 * Code for the opening design file is not a valid design file or the file
	 * does not exsit.
	 */

	public final static int INVALID_DESIGN_FILE = 0x00;

	/**
	 * Code for information for user that the opening design file holds a
	 * version number before the "column binding" feature is supported. To Open
	 * the file may need convert the original file automatically.
	 */

	public final static int CONVERT_FOR_COLUMN_BINDING = 0x01;

	/**
	 * Code for the invalid design version.
	 */

	public final static int INVALID_VERSION = 0x02;

	/**
	 * The opening design file is not a valid design file or the file does not
	 * exsit.
	 */

	private final static String INVALID_VERSION_MSG = MessageConstants.VERSION_INFO_INVALID_VERSION;

	/**
	 * BIRT version from which BIRT began to support column binding feature.
	 */

	public final static int COLUMN_BINDING_FROM_VERSION = 3000000;

	/**
	 * Column binding feature.
	 */

	public final static String COLUMN_BINDING_FEATURE = "column binding"; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param version 
	 * @param convertCode 
	 */

	public VersionInfo( String version, int convertCode )
	{
		this.version = version;
		infoCode = convertCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.IVersionInfo#getLocalizedMessage()
	 */

	public String getLocalizedMessage( )
	{
		switch ( infoCode )
		{
			case CONVERT_FOR_COLUMN_BINDING :
				return ModelMessages.getMessage( CONVERT_INFO_MSG );
			case INVALID_DESIGN_FILE :
				return ModelMessages.getMessage( INVALID_DESIGN_FILE_MSG );
			case INVALID_VERSION :
				return ModelMessages.getMessage( INVALID_VERSION_MSG,
						new String[]{version} );

			default :
				assert false;
				return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.IVersionInfo#getVersion()
	 */

	public String getDesignFileVersion( )
	{
		return version;
	}

}

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

import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Utility class to do the operations of the design file version.
 */

public class VersionUtil
{

	private static final int SUPPORTED_VERSION_TOKEN_LENGTH = 4;

	private static final int[] expoArray = new int[]{1000000, 10000, 100, 1};

	public final static int VERSION_0 = 0;

	public final static int VERSION_1_0_0 = 1000000;

	public final static int VERSION_3_0_0 = 3000000;

	public final static int VERSION_3_1_0 = 3010000;

	public final static int VERSION_3_2_0 = 3020000;

	public final static int VERSION_3_2_1 = 3020100;

	public final static int VERSION_3_2_2 = 3020200;

	public final static int VERSION_3_2_3 = 3020300;

	public final static int VERSION_3_2_4 = 3020400;

	public final static int VERSION_3_2_6 = 3020600;
	
	public final static int VERSION_3_2_7 = 3020700;
	
	/**
	 * 
	 * @param version
	 * @return the parsed version number
	 */

	public static int parseVersion( String version )
	{
		if ( StringUtil.isBlank( version ) )
			return 0;

		// parse the version string, for example
		// 3.1.2(.0) -- 3010200, two biye for one version token

		String[] versionTokers = version.split( "\\." ); //$NON-NLS-1$
		int parsedVersionNumber = 0;
		for ( int i = 0; i < versionTokers.length; i++ )
		{
			if ( i > SUPPORTED_VERSION_TOKEN_LENGTH )
				break;

			byte versionShort = Byte.parseByte( versionTokers[i] );
			if ( versionShort > 99 )
				throw new IllegalArgumentException(
						"the version string is wrong!" ); //$NON-NLS-1$
			parsedVersionNumber += versionShort * expoArray[i];
		}
		// add the parsed version to the cache map

		return parsedVersionNumber;
	}

	
}

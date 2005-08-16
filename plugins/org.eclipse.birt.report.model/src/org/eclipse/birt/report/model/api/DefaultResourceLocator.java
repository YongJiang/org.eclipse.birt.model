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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The default file search algorithm. It searches for a given file in the 'base'
 * folder of a design. If the 'base' property of the design was not set, then
 * this class looks in folder where the design file is located.
 * <p>
 * The detail search mechanism is:
 * <ul>
 * <li>If the the file to be found is given by an absolute path, returns that
 * path.
 * <li>If it is a relative file path, search the 'base' folder of the design.
 * <li>If the 'base' property of the design is not set, then search the file in
 * the folder where the design file locates.
 * </ul>
 * 
 * @see IResourceLocator
 * @see SessionHandle
 */

public class DefaultResourceLocator implements IResourceLocator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.IResourceLocator#findResource(org.eclipse.birt.report.model.api.ModuleHandle,
	 *      java.lang.String, int)
	 */

	public URL findResource( ModuleHandle moduleHandle, String fileName,
			int type )
	{
		assert moduleHandle != null;
		if ( fileName == null )
			return null;

		String base = moduleHandle.getFileName( );
		if ( base != null )
		{
			File baseFile = new File( base );
			File f = new File( baseFile.getParent(), fileName );
			if ( f.exists( ) && f.isFile( ) )
			{
				try
				{
					return f.toURL( );
				}
				catch( MalformedURLException e )
				{
					return null;
				}
			}
		}

		return null;
	}

}
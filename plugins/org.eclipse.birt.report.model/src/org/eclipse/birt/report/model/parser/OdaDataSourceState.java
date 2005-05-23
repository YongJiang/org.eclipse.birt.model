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

import org.eclipse.birt.data.oda.util.manifest.ExtensionManifest;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.oda.ODAManifestUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the oda data source element.
 * 
 */

public class OdaDataSourceState extends DataSourceState
{

	/**
	 * Constructs the oda data source state with the design parser handler, the
	 * container element and the container slot of the oda data source.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public OdaDataSourceState( DesignParserHandler handler )
	{
		super( handler );
		element = new OdaDataSource( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		parseODADataSourceExtensionID( attrs, false );

		initElement( attrs, true );
	}

	/**
	 * Parse the attribute of "extensionId" for extendable element.
	 * 
	 * @param attrs
	 *            the SAX attributes object
	 * @param extensionNameRequired
	 *            whether extension name is required
	 */

	private void parseODADataSourceExtensionID( Attributes attrs,
			boolean extensionNameRequired )
	{
		String extensionID = getAttrib( attrs,
				DesignSchemaConstants.EXTENSION_ID_ATTRIB );

		if ( StringUtil.isBlank( extensionID ) )
		{
			if ( !extensionNameRequired )
				return;

			SemanticError e = new SemanticError( element,
					SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION );
			RecoverableError.dealMissingInvalidExtension( handler, e );
		}
		else
		{
			ExtensionManifest extension = ODAManifestUtil
					.getDataSourceExtension( extensionID );
			if ( extension == null )
			{
				SemanticError e = new SemanticError( element,
						new String[]{extensionID},
						SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND );
				RecoverableError.dealMissingInvalidExtension( handler, e );
			}
		}

		setProperty( IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID );
	}

}
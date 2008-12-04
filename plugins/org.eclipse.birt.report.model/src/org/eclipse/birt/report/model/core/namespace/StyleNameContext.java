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

package org.eclipse.birt.report.model.core.namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.Style;

import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * 
 */
public class StyleNameContext extends AbstractModuleNameContext
{

	/**
	 * Map of the styles that can be resolved by calling resolve(String).
	 */
	protected Map<String, DesignElement> cachedStyles = new HashMap<String, DesignElement>( );

	/**
	 * Cached default toc map.
	 */
	protected Map<String, DesignElement> cachedTOCStyles = new HashMap<String, DesignElement>( );

	/**
	 * Status identifying whether the caching work is ok or not.
	 */
	protected boolean isCacheOk = false;

	/**
	 * Constructs one style element name space.
	 * 
	 * @param module
	 *            the attached module
	 */

	StyleNameContext( Module module )
	{
		super( module, Module.STYLE_NAME_SPACE );
	}

	private void buildTOCStyles( )
	{
		List defaultTocStyle = module.getSession( ).getDefaultTOCStyleValue( );
		addAllStyles( cachedTOCStyles, defaultTocStyle );
	}

	/**
	 * Returns all elements in the module this module namespace is associated
	 * and those in the included modules. For the style name scope, the depth of
	 * the library is ignored.
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.IModuleNameScope#getElements(int)
	 */

	public List getElements( int level )
	{
		if ( isCacheOk )
			return new ArrayList( cachedStyles.values( ) );

		Map elements = new LinkedHashMap<String, StyleElement>( );

		Theme theme = module.getTheme( module );

		if ( theme == null && module instanceof Library )
		{
			if ( module.isCached( ) )
			{
				cachedStyles.putAll( elements );
				isCacheOk = true;
			}
			return new ArrayList( elements.values( ) );
		}

		if ( theme != null )
		{
			List allStyles = theme.getAllStyles( );
			addAllStyles( elements, allStyles );
		}

		if ( module instanceof Library )
		{
			if ( module.isCached( ) )
			{
				cachedStyles.putAll( elements );
				isCacheOk = true;
			}
			return new ArrayList( elements.values( ) );
		}

		// find in css file

		List csses = CssNameManager
				.getStyles( (ICssStyleSheetOperation) module );
		addAllStyles( elements, csses );

		// find all styles in report design.

		NameSpace ns = module.getNameHelper( ).getNameSpace( nameSpaceID );
		List styles = ns.getElements( );
		addAllStyles( elements, styles );

		if ( module.isCached( ) )
		{
			cachedStyles.putAll( elements );
			isCacheOk = true;
		}
		return new ArrayList( elements.values( ) );
	}

	private void addAllStyles( Map<String, DesignElement> styleMap,
			List<DesignElement> styleList )
	{
		assert styleMap != null;
		if ( styleList != null )
		{
			for ( int i = 0; i < styleList.size( ); i++ )
			{
				DesignElement style = styleList.get( i );
				styleMap.put( style.getName( ), style );
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.IModuleNameSpace#resolve(org.eclipse
	 * .birt.report.model.core.DesignElement)
	 */

	private ElementRefValue resolve( DesignElement element )
	{
		return new ElementRefValue( null, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.IModuleNameSpace#resolve(java.lang
	 * .String)
	 */

	private ElementRefValue resolve( String elementName )
	{
		if ( isCacheOk )
		{
			DesignElement style = cachedStyles.get( elementName );

			// firstly, find it in the cached style list
			if ( style != null )
				return new ElementRefValue( null, style );

			// if not found, find it in default TOC styles
			if ( cachedTOCStyles.isEmpty( ) )
				buildTOCStyles( );
			style = cachedTOCStyles.get( elementName );
			if ( style != null )
				return new ElementRefValue( null, style );
			return new ElementRefValue( null, elementName );
		}

		// this name is not cached, so find it directly
		Theme theme = module.getTheme( module );

		if ( theme == null && module instanceof Library )
		{
			return new ElementRefValue( null, elementName );
		}

		// find the style first in the report design first.

		DesignElement target = null;

		if ( module instanceof ReportDesign )
		{
			NameSpace ns = module.getNameHelper( ).getNameSpace( nameSpaceID );
			target = ns.getElement( elementName );

			if ( target != null )
			{
				return new ElementRefValue( null, target );
			}

			// find in css file

			List csses = CssNameManager
					.getStyles( (ICssStyleSheetOperation) module );
			for ( int i = 0; csses != null && i < csses.size( ); ++i )
			{
				CssStyle s = (CssStyle) csses.get( i );
				if ( elementName.equalsIgnoreCase( s.getFullName( ) ) )
				{
					return new ElementRefValue( null, s );
				}
			}
		}

		// find the style in the library.

		DesignElement libraryStyle = null;
		if ( theme != null )
		{
			libraryStyle = theme.findStyle( elementName );
		}

		if ( libraryStyle != null )
		{
			return new ElementRefValue( null, libraryStyle );
		}

		// find style in toc default style
		if ( cachedTOCStyles.isEmpty( ) )
			buildTOCStyles( );
		DesignElement tocStyle = cachedTOCStyles.get( elementName );
		if ( tocStyle != null )
			return new ElementRefValue( null, tocStyle );

		// if the style is not find, return a unresolved element reference
		// value.

		return new ElementRefValue( null, elementName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.AbstractNameScope#resolve
	 * (org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public ElementRefValue resolve( DesignElement element, PropertyDefn propDefn )
	{
		return resolve( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.AbstractNameScope#resolve
	 * (java.lang.String, org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public ElementRefValue resolve( String elementName, PropertyDefn propDefn )
	{
		return resolve( elementName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameContext#findElement
	 * (java.lang.String,
	 * org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */
	public DesignElement findElement( String elementName,
			IElementDefn elementDefn )
	{
		ElementRefValue refValue = resolve( elementName );
		return refValue == null ? null : refValue.getElement( );
	}
}

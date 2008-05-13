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

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Represents the handle of library element. The library contains the resuable
 * element components.
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Content Item</th>
 * <th width="40%">Description</th>
 * 
 * <tr>
 * <td>Code Modules</td>
 * <td>Global scripts that apply to the report as a whole.</td>
 * </tr>
 * 
 * <tr>
 * <td>Parameters</td>
 * <td>A list of Parameter elements that describe the data that the user can
 * enter when running the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sources</td>
 * <td>The connections used by the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sets</td>
 * <td>Data sets defined in the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Color Palette</td>
 * <td>A set of custom color names as part of the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Styles</td>
 * <td>User-defined styles used to format elements in the report. Each style
 * must have a unique name within the set of styles for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Page Setup</td>
 * <td>The layout of the master pages within the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Components</td>
 * <td>Reusable report items defined in this design. Report items can extend
 * these items. Defines a "private library" for this design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Translations</td>
 * <td>The list of externalized messages specifically for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Images</td>
 * <td>A list of images embedded in this report.</td>
 * </tr>
 * 
 * </table>
 * 
 * <p>
 * There are two cases that this library handle represents:
 * <ul>
 * <li>the library file that user opens directly <br>
 * In this case, user can take operations as same as design file.
 * <li>the library file that is included in one design file <br>
 * User can not take operations, because the included library file is read-only.
 * Any operation on the library file of this type will throw runtime exception.
 * </ul>
 */

public class LibraryHandle extends ModuleHandle implements ILibraryModel
{

	/**
	 * Constructs one library handle with the given library element.
	 * 
	 * @param library
	 *            library element
	 */

	public LibraryHandle( Library library )
	{
		super( library );
	}

	/**
	 * Returns the host handle which includes the library.
	 * 
	 * @return the host handle which include this library.
	 */

	public ModuleHandle getHostHandle( )
	{
		Module host = ( (Library) getElement( ) ).getHost( );
		if ( host == null )
			return null;
		return (ModuleHandle) host.getHandle( getModule( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getElement()
	 */

	public DesignElement getElement( )
	{
		return module;
	}

	/**
	 * Returns the library namespace, which identifies one library unqiuely in
	 * one design file.
	 * 
	 * @return the library namespace
	 */

	public String getNamespace( )
	{
		return ( (Library) module ).getNamespace( );
	}

	/**
	 * Returns a slot handle to work with the themes within the library. Note
	 * that the order of the data sets within the slot is unimportant.
	 * 
	 * @return A handle for working with the themes.
	 */

	public SlotHandle getThemes( )
	{
		return getSlot( THEMES_SLOT );
	}

	/**
	 * Returns a slot handle to work with the styles within the library.
	 * 
	 * @return A handle for working with the styles. Or <code>null</code> if
	 *         the library has no values for the theme property
	 * @deprecated uses the theme instead
	 */

	public SlotHandle getStyles( )
	{
		ThemeHandle theme = getTheme( );
		if ( theme == null )
			return null;

		return theme.getStyles( );
	}

	// TODO should we support for public List getAllStyles() method?

	/**
	 * Import css file to theme.
	 * 
	 * @param stylesheet
	 *            the style sheet handle that contains all the selected styles
	 * @param selectedStyles
	 *            the selected style list
	 */

	public void importCssStyles( CssStyleSheetHandle stylesheet,
			List selectedStyles )
	{
		String themeName = ( (Module) getElement( ) ).getThemeName( );

		if ( themeName == null )
			themeName = ModelMessages
					.getMessage( IThemeModel.DEFAULT_THEME_NAME );

		importCssStyles( stylesheet, selectedStyles, themeName );

	}

	/**
	 * Creates the theme with the given name if it does not exist. Sets the
	 * theme value of the library to the given name if this value was
	 * <code>null</code>.
	 * 
	 * @param themeName
	 *            the theme name
	 * @return the theme handle
	 */

	private ThemeHandle setupTheme( String themeName )
	{
		Library libElement = (Library) getElement( ).getRoot( );
		Theme theme = libElement.findNativeTheme( themeName );

		if ( theme == null )
		{
			ThemeHandle newTheme = getModuleHandle( ).getElementFactory( )
					.newTheme( themeName );
			try
			{
				getThemes( ).add( newTheme );
			}
			catch ( ContentException e )
			{
				assert false;
			}
			catch ( NameException e )
			{
				assert false;
			}
			theme = (Theme) newTheme.getElement( );
		}

		try
		{
			if ( libElement.getThemeName( ) == null )
				setThemeName( themeName );
		}
		catch ( SemanticException e )
		{
			assert false;
		}

		return (ThemeHandle) theme.getHandle( module );
	}

	/**
	 * Imports the selected styles in a <code>CssStyleSheetHandle</code> to
	 * the given theme of the library. Each in the list is instance of
	 * <code>SharedStyleHandle</code> .If any style selected has a duplicate
	 * name with that of one style already existing in the report design, this
	 * method will rename it and then add it to the design.
	 * 
	 * @param stylesheet
	 *            the style sheet handle that contains all the selected styles
	 * @param selectedStyles
	 *            the selected style list
	 * @param themeName
	 *            the name of the theme to put styles
	 */

	public void importCssStyles( CssStyleSheetHandle stylesheet,
			List selectedStyles, String themeName )
	{
		if ( StringUtil.isBlank( themeName ) )
			return;

		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( );

		// creates the theme if it does not exist

		ThemeHandle themeHandle = setupTheme( themeName );

		for ( int i = 0; i < selectedStyles.size( ); i++ )
		{
			SharedStyleHandle style = (SharedStyleHandle) selectedStyles
					.get( i );
			if ( stylesheet.findStyle( style.getName( ) ) != null )
			{
				try
				{
					style.getElement( )
							.setName(
									themeHandle.makeUniqueStyleName( style
											.getName( ) ) );
					// Copy CssStyle to Style

					SharedStyleHandle newStyle = ModelUtil
							.TransferCssStyleToSharedStyle( module, style );
					if ( newStyle == null )
						continue;
					themeHandle.getStyles( ).add( newStyle );
				}
				catch ( ContentException e )
				{
					assert false;
				}
				catch ( NameException e )
				{
					assert false;
				}
			}
		}

		stack.commit( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getCubes()
	 */
	public SlotHandle getCubes( )
	{
		return getSlot( CUBE_SLOT );
	}

	/**
	 * If this library is included by a module, return the relative file name
	 * that is defined in the host's xml file.
	 * 
	 * @return the relative file name that is defined in the host's xml file
	 */

	public String getRelativeFileName( )
	{
		ModuleHandle hostHandle = getHostHandle( );
		if ( hostHandle == null )
			return null;

		Module host = (Module) hostHandle.getElement( );
		if ( host == null )
			return null;

		IncludedLibrary libStruct = host.findIncludedLibrary( getNamespace( ) );
		if ( libStruct != null )
			return libStruct.getFileName( );

		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#isDirectionRTL()
	 * 
	 */

	public boolean isDirectionRTL( )
	{
		return false;
	}
}
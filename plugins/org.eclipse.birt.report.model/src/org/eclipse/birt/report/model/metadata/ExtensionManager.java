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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.Style;

/**
 * Represents the extension manager which is responsible to load all extensions
 * that Model supports. This class can not be instantiated and derived.
 */

public final class ExtensionManager
{

	/**
	 * the singleton instance
	 */
	static protected ExtensionManager instance;

	/**
	 * The map that stores all implementation of encryption helpers. The key is
	 * extension id of the encryption helper and value is the class instance of
	 * IEncryptionHelper.
	 */

	private Map<String, IEncryptionHelper> encryptionHelperMap = null;

	/**
	 * Default encryption helper id for the whole birt.
	 */
	private String defaultEncryptionHelperID = SimpleEncryptionHelper.ENCRYPTION_ID;

	/**
	 * Provides the list of extension elements registered in our meta-data keyed
	 * by their internal names.
	 */

	private HashMap<String, IElementDefn> extensionNameMap = null;

	/**
	 * The factory to create scriptable classes.
	 */

	private IScriptableObjectClassInfo scriptableFactory = null;

	/**
	 * The predefined style instance list
	 */
	private Map<String, Style> extensionFactoryStyles = null;

	/**
	 * Don't allow to instantiate.
	 */

	private ExtensionManager( )
	{
		encryptionHelperMap = new HashMap<String, IEncryptionHelper>( );
		extensionNameMap = new HashMap<String, IElementDefn>( );
		extensionFactoryStyles = new HashMap<String, Style>( );
	}

	/**
	 * Initializes all extensions that Model supports.
	 */

	void initialize( )
	{
		// ensure the ROM is initialized first
		assert !MetaDataDictionary.getInstance( ).isEmpty( );

		// load extensions in all the extension points; for encryption helper is
		// independent and peer extension depends on it, so load encryption
		// first, then the peer extension, for scriptable extension depends on
		// peer, and last is scriptable extension
		new EncryptionHelperExtensionLoader( ).load( );
		new PeerExtensionLoader( ).load( );
		new ScriptableObjectExtensionLoader( ).load( );

	}

	/**
	 * create the static instance. It is a separate function so that getInstance
	 * do not need to be synchronized
	 */
	private synchronized static void createInstance( )
	{
		if ( instance == null )
			instance = new ExtensionManager( );
	}

	/**
	 * @return the single instance for the extension manager
	 */
	static public ExtensionManager getInstance( )
	{
		if ( instance == null )
			createInstance( );

		return instance;
	}

	/**
	 * Release all the resources in this class.
	 */
	void releaseInstance( )
	{
		instance = null;
	}

	/**
	 * Finds the element definition by its internal name.
	 * 
	 * @param name
	 *            The internal element definition name.
	 * @return The element definition, or null if the name was not found in the
	 *         dictionary.
	 */

	public IElementDefn getElement( String name )
	{
		return extensionNameMap.get( name );
	}

	/**
	 * Returns the extension list. Each one is the instance of
	 * {@link IElementDefn}.
	 * 
	 * @return the extension definition list. Return empty list if no extension
	 *         is found.
	 */

	public List<IElementDefn> getExtensions( )
	{
		return new ArrayList<IElementDefn>( extensionNameMap.values( ) );
	}

	/**
	 * Adds the extension definition to the dictionary.
	 * 
	 * @param extDefn
	 *            the definition of the extension element to add
	 * @throws MetaDataException
	 *             if the extension name is not provided or duplicate.
	 */

	void addExtension( ExtensionElementDefn extDefn ) throws MetaDataException
	{
		assert extDefn != null;
		String elementName = extDefn.getName( );

		if ( StringUtil.isBlank( elementName ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_EXTENSION_NAME );
		if ( MetaDataDictionary.getInstance( ).getElement( elementName ) != null )
			throw new MetaDataException( new String[]{elementName},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_EXTENSION_NAME );

		extensionNameMap.put( elementName, extDefn );
	}

	/**
	 * Returns the encryption helper with the extension id.
	 * 
	 * @param id
	 *            the extension id for the encryption helper to find
	 * @return the encryption helper if found, otherwise false.
	 */

	public IEncryptionHelper getEncryptionHelper( String id )
	{
		if ( id == null )
			return null;
		if ( SimpleEncryptionHelper.ENCRYPTION_ID.equals( id ) )
			return SimpleEncryptionHelper.getInstance( );
		return encryptionHelperMap.get( id );
	}

	/**
	 * Gets all the encryption helpers.
	 * 
	 * @return the list of the encryption helpers
	 */
	public List<IEncryptionHelper> getEncryptionHelpers( )
	{
		ArrayList<IEncryptionHelper> encryptions = new ArrayList<IEncryptionHelper>( );
		encryptions.add( SimpleEncryptionHelper.getInstance( ) );
		if ( encryptionHelperMap != null )
		{
			encryptions.addAll( encryptionHelperMap.values( ) );
		}
		return encryptions;
	}

	/**
	 * Returns the encryption helper with the extension id.
	 * 
	 * @param id
	 *            the extension id for the encryption helper to find
	 * @return the encryption helper if found, otherwise false.
	 */

	public IEncryptionHelper getDefaultEncryptionHelper( )
	{
		return getEncryptionHelper( defaultEncryptionHelperID );
	}

	/**
	 * Gets the default encryption id.
	 * 
	 * @return the default encryption helper id
	 */
	public String getDefaultEncryptionHelperID( )
	{
		return defaultEncryptionHelperID;
	}

	/**
	 * Sets the default encryption id.
	 * 
	 * @param encryptionID
	 */
	public void setDefaultEncryptionHelper( String encryptionID )
	{
		if ( getEncryptionHelper( encryptionID ) != null )
		{
			defaultEncryptionHelperID = encryptionID;
		}
	}

	/**
	 * Sets the encryption helper.
	 * 
	 * @param id
	 *            the extension id
	 * @param encryptionHelper
	 *            the encryption helper to set
	 * @throws MetaDataException
	 */

	void addEncryptionHelper( String id, IEncryptionHelper encryptionHelper )
			throws MetaDataException
	{
		assert id != null;
		assert encryptionHelper != null;

		if ( getEncryptionHelper( id ) != null )
			throw new ExtensionException(
					new String[]{id},
					MetaDataException.DESIGN_EXCEPTION_ENCYRPTION_EXTENSION_EXISTS );

		encryptionHelperMap.put( id, encryptionHelper );
	}

	/**
	 * Returns the factory to create scriptable class for ROM defined elements.
	 * 
	 * @return the scriptable factory
	 */

	public IScriptableObjectClassInfo getScriptableFactory( )
	{
		return scriptableFactory;
	}

	/**
	 * Sets the factory to create scriptable class for ROM defined elements.
	 * 
	 * @param scriptableFactory
	 *            the scriptable factory to set
	 */

	void setScriptableFactory( IScriptableObjectClassInfo scriptableFactory )
	{
		this.scriptableFactory = scriptableFactory;
	}

	/**
	 * return the predefined styls instance of the extension element.
	 * 
	 * @return the list of style intance for the extension element.
	 */
	public List<Style> getExtensionFactoryStyles( )
	{
		if ( extensionFactoryStyles != null )
			return new ArrayList<Style>( extensionFactoryStyles.values( ) );

		return Collections.emptyList( );
	}

	/**
	 * add the predefined style into the list.
	 * 
	 * @param style
	 */
	void addExtensionFactoryStyle( Style style )
	{
		if ( extensionFactoryStyles == null )
			extensionFactoryStyles = new HashMap<String, Style>( );
		if ( extensionFactoryStyles.containsKey( style.getName( ) ) )
			MetaLogManager
					.log( "the extension predefined style has duplicated name, will be ignored." ); //$NON-NLS-1$
		else
			extensionFactoryStyles.put( style.getName( ), style );

	}
}

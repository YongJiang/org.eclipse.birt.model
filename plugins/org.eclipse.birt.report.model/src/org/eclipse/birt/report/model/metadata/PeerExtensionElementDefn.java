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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ExtensionValidator;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the extension element definition for peer extension support. The
 * details of peer extension, please refer to
 * {@link org.eclipse.birt.report.model.extension.PeerExtensibilityProvider}.
 * This class is only used for those extension element definition from
 * third-party, not the BIRT-defined standard elements. The extension element
 * definition must include an instance of
 * {@link org.eclipse.birt.report.model.api.extension.IReportItemFactory}. The
 * included IElmentFactory gives the information about the internal model
 * properties of the extension element, how to instantiate
 * {@link org.eclipse.birt.report.model.api.extension.IReportItem}and other
 * information.
 */

public final class PeerExtensionElementDefn extends ExtensionElementDefn
{

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger
			.getLogger( PeerExtensionElementDefn.class.getName( ) );

	/**
	 * The element factory of the extended element.
	 */

	protected IReportItemFactory reportItemFactory = null;

	/**
	 * Override property information map.key is property name, value is
	 * <code>OverridePropertyInfo</code>.
	 */

	protected Map<String, OverridePropertyInfo> overridePropertyInfoMap = new HashMap<String, OverridePropertyInfo>( );

	/**
	 * The factory to create scriptable classes.
	 */

	private IScriptableObjectClassInfo scriptableFactory = null;

	/**
	 * Constructs the peer extension element definition with the element
	 * definition name and report item factory.
	 * 
	 * @param name
	 *            the name of the extension element definition
	 * @param reportItemFactory
	 *            the report item factory of the extension element
	 */

	public PeerExtensionElementDefn( String name,
			IReportItemFactory reportItemFactory )
	{
		assert name != null;
		assert reportItemFactory != null;
		this.name = name;
		this.reportItemFactory = reportItemFactory;
	}

	/**
	 * Gets the report item factory of this extension element definition.
	 * 
	 * @return the report item factory of the extension element definition
	 */

	public IReportItemFactory getReportItemFactory( )
	{
		return reportItemFactory;
	}

	/*
	 * Returns the localized display name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return name of
	 * this element definition.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ObjectDefn#getDisplayName()
	 */

	public String getDisplayName( )
	{
		if ( displayNameKey != null && reportItemFactory != null )
		{
			IMessages messages = reportItemFactory.getMessages( );

			if ( messages != null )
			{
				String displayName = messages.getMessage( displayNameKey,
						ThreadResources.getLocale( ) );

				if ( !StringUtil.isBlank( displayName ) )
					return displayName;
			}
		}

		return getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#buildProperties()
	 */

	protected void buildProperties( ) throws MetaDataException
	{
		super.buildProperties( );

		if ( PeerExtensionLoader.EXTENSION_POINT
				.equalsIgnoreCase( extensionPoint ) )
		{
			// extensions must have 'extensionName' property
			ElementDefn extendedItem = (ElementDefn) MetaDataDictionary
					.getInstance( ).getElement(
							ReportDesignConstants.EXTENDED_ITEM );
			IElementPropertyDefn extensionName = extendedItem
					.getProperty( IExtendedItemModel.EXTENSION_NAME_PROP );
			if ( getProperty( IExtendedItemModel.EXTENSION_NAME_PROP ) == null )
			{
				properties.put( IExtendedItemModel.EXTENSION_NAME_PROP,
						extensionName );
				cachedProperties.put( IExtendedItemModel.EXTENSION_NAME_PROP,
						extensionName );

				addPropertyVisibility( IExtendedItemModel.EXTENSION_NAME_PROP,
						HIDDEN_IN_PROPERTY_SHEET );
			}

			// extensions must have 'extensionVersion' property
			IElementPropertyDefn extensionVersion = extendedItem
					.getProperty( IExtendedItemModel.EXTENSION_VERSION_PROP );
			if ( getProperty( IExtendedItemModel.EXTENSION_VERSION_PROP ) == null )
			{
				properties.put( IExtendedItemModel.EXTENSION_VERSION_PROP,
						extensionVersion );
				cachedProperties.put(
						IExtendedItemModel.EXTENSION_VERSION_PROP,
						extensionVersion );

				addPropertyVisibility(
						IExtendedItemModel.EXTENSION_VERSION_PROP,
						HIDDEN_IN_PROPERTY_SHEET );
			}
		}

		// modify extended item's allowed unit.
		// especially for 'height' and 'width' property in Chart.

		overrideProperty( );
	}

	/**
	 * Override allowedUnits property.
	 * 
	 */

	private void overrideProperty( )
	{
		// inherit override property definition from parent.

		ElementDefn tmpDefn = parent;
		while ( tmpDefn != null && tmpDefn instanceof PeerExtensionElementDefn )
		{
			PeerExtensionElementDefn tmpPeerDefn = (PeerExtensionElementDefn) tmpDefn;
			if ( tmpPeerDefn.overridePropertyInfoMap == null
					|| tmpPeerDefn.overridePropertyInfoMap.isEmpty( ) )
			{
				tmpDefn = tmpDefn.parent;
				continue;
			}

			Iterator<String> iterator = tmpPeerDefn.overridePropertyInfoMap
					.keySet( ).iterator( );
			while ( iterator.hasNext( ) )
			{
				String propName = iterator.next( );
				cachedProperties.put( propName, tmpDefn.cachedProperties
						.get( propName ) );
			}
			tmpDefn = tmpDefn.parent;
		}

		// set override property value
		Set<String> set = overridePropertyInfoMap.keySet( );
		Iterator<String> iterator = set.iterator( );
		while ( iterator.hasNext( ) )
		{
			String propName = iterator.next( );

			// don't support override local property.

			if ( properties.get( propName ) != null )
			{
				ElementPropertyDefn defn = (ElementPropertyDefn) properties
						.get( propName );
				if ( defn.definedBy == this )
					continue;
			}

			OverridePropertyInfo propInfo = overridePropertyInfoMap
					.get( propName );

			if ( propInfo == null )
				continue;

			String units = propInfo.getAllowedUnits( );
			String choices = propInfo.getAllowedChoices( );
			boolean useOwnSearch = propInfo.useOwnSearch( );

			ChoiceSet unitSet = buildChoiceSet( MetaDataDictionary
					.getInstance( ).getChoiceSet(
							DesignChoiceConstants.CHOICE_UNITS ), units );

			ElementPropertyDefn defn = (ElementPropertyDefn) cachedProperties
					.get( propName );
			if ( defn == null )
				continue;

			ChoiceSet choiceSet = null;

			if ( choices != null )
			{
				IChoiceSet tmpSet = defn.getChoices( );
				if ( tmpSet != null )
					choiceSet = buildChoiceSet( tmpSet, choices );
				else
					choiceSet = null;
			}

			if ( unitSet == null && choiceSet == null && !useOwnSearch )
				continue;

			ElementPropertyDefn clonedDefn = (ElementPropertyDefn) reflectClass( defn );
			if ( clonedDefn == null )
				continue;

			if ( choiceSet != null )
				clonedDefn.allowedChoices = choiceSet;

			if ( unitSet != null )
				clonedDefn.allowedUnits = unitSet;

			clonedDefn.useOwnSearch = useOwnSearch;

			cachedProperties.put( propName, clonedDefn );
		}
	}

	/**
	 * build Choice Set.
	 * 
	 * @param units
	 *            units such as 'in,cm'
	 * @return choice set.
	 */

	private ChoiceSet buildChoiceSet( IChoiceSet romSet, String units )
	{
		List<IChoice> choiceList = new ArrayList<IChoice>( );
		if ( units != null && units.length( ) > 0 )
		{
			String[] eachUnit = units.split( "," ); //$NON-NLS-1$

			for ( int i = 0; eachUnit != null && i < eachUnit.length; ++i )
			{
				String unit = eachUnit[i];
				if ( unit != null && unit.length( ) > 0 )
				{
					IChoice romChoice = romSet.findChoice( unit );

					if ( romChoice != null )
					{
						choiceList.add( romChoice );
					}
				}
			}
		}
		if ( choiceList.size( ) == 0 )
			return null;

		ChoiceSet choiceSet = new ChoiceSet( );

		Choice[] choices = new Choice[choiceList.size( )];
		choiceList.toArray( choices );
		choiceSet.setChoices( choices );

		return choiceSet;
	}

	/**
	 * Reflects to clone new instance of property definition.
	 * 
	 * @param defn
	 *            property definition
	 * @return shadow cloned property definition.
	 */

	private PropertyDefn reflectClass( PropertyDefn defn )
	{
		ElementPropertyDefn retDefn = null;

		String className = defn.getClass( ).getName( );
		try
		{
			Class<? extends Object> clazz = Class.forName( className );
			retDefn = (ElementPropertyDefn) clazz.newInstance( );

			Class<? extends Object> ownerClass = defn.getClass( );
			Class<? extends Object> clonedClass = retDefn.getClass( );

			shadowCopyProperties( defn, retDefn, ownerClass, clonedClass );
		}
		catch ( InstantiationException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$				
		}
		catch ( IllegalAccessException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
		}
		catch ( ClassNotFoundException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
		}

		if ( retDefn == null )
			return null;

		shadowCopyProperties( defn, retDefn, defn.getClass( ),
				ExtensionPropertyDefn.class );

		return retDefn;
	}

	/**
	 * Shadow copy all properties to cloned property definition instance.
	 * 
	 * @param defn
	 *            property definition
	 * @param clonedDefn
	 *            cloned property definition
	 * @param ownerClass
	 *            property definition class
	 * @param clonedClass
	 *            cloned property definition class
	 */

	private void shadowCopyProperties( PropertyDefn defn,
			PropertyDefn clonedDefn, Class<? extends Object> ownerClass,
			Class<? extends Object> clonedClass )
	{
		if ( ownerClass == null || clonedClass == null )
			return;

		Field[] fields = ownerClass.getDeclaredFields( );
		for ( int i = 0; i < fields.length; ++i )
		{
			Field field = fields[i];
			if ( ( field.getModifiers( ) & Modifier.STATIC ) != 0 )
				continue;

			try
			{
				Object property = field.get( defn );
				Field clonedField = ownerClass.getDeclaredField( field
						.getName( ) );
				clonedField.set( clonedDefn, property );
			}
			catch ( IllegalArgumentException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	

				continue;
			}
			catch ( IllegalAccessException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
			catch ( SecurityException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
			catch ( NoSuchFieldException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
		}
		shadowCopyProperties( defn, clonedDefn, ownerClass.getSuperclass( ),
				clonedClass.getSuperclass( ) );
	}

	/**
	 * Sets override property information.
	 * 
	 * @param prop
	 * @param propInfo
	 */

	protected void setOverridePropertyInfo( String prop,
			OverridePropertyInfo propInfo )
	{
		overridePropertyInfoMap.put( prop, propInfo );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#isContainer()
	 */
	public boolean isContainer( )
	{
		return isContainer;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.ElementDefn#buildTriggerDefnSet()
	 */

	protected void buildTriggerDefnSet( )
	{
		super.buildTriggerDefnSet( );

		if ( !StringUtil.isBlank( name ) )
		{
			SemanticTriggerDefnSet tmpTriggerSet = getTriggerDefnSet( );
			String tmpTriggerDefnName = ExtensionValidator.NAME;
			if ( !tmpTriggerSet.contain( tmpTriggerDefnName ) )
			{
				SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(
						tmpTriggerDefnName );
				triggerDefn.setPropertyName( getName( ) );
				triggerDefn.setValidator( ExtensionValidator.getInstance( ) );

				tmpTriggerSet.add( triggerDefn );
			}
		}
	}
}

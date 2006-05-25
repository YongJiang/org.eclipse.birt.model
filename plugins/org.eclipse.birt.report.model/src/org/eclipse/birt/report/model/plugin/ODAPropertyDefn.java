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

package org.eclipse.birt.report.model.plugin;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;
import org.eclipse.datatools.connectivity.oda.util.manifest.Property;
import org.eclipse.datatools.connectivity.oda.util.manifest.PropertyChoice;

/**
 */

public class ODAPropertyDefn extends SystemPropertyDefn
{

	private Property property = null;

	ODAPropertyDefn( Property property )
	{
		assert property != null;

		this.property = property;

		name = property.getName( );
		type = MetaDataDictionary.getInstance( ).getPropertyType(
				property.getType( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#isList()
	 */

	public boolean isList( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getName()
	 */
	public String getName( )
	{
		return property.getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getTypeCode()
	 */
	public int getTypeCode( )
	{
		PropertyType type = MetaDataDictionary.getInstance( ).getPropertyType(
				property.getType( ) );
		return type == null ? -1 : type.getTypeCode( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getDisplayName()
	 */

	public String getDisplayName( )
	{
		return property.getDisplayName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getDisplayNameID()
	 */

	public String getDisplayNameID( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getChoices()
	 */

	public IChoiceSet getChoices( )
	{
		PropertyChoice[] propertyChoices = property.getChoices( );

		ChoiceSet choiceSet = null;
		if ( propertyChoices != null && propertyChoices.length > 0 )
		{
			choiceSet = new ChoiceSet( );
			IChoice[] choices = new ODAChoice[propertyChoices.length];
			for ( int i = 0; i < propertyChoices.length; i++ )
			{
				choices[i] = new ODAChoice( propertyChoices[i] );

			}
			choiceSet.setChoices( choices );
		}

		return choiceSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#hasChoices()
	 */
	public boolean hasChoices( )
	{
		return property.getChoices( ) != null
				&& property.getChoices( ).length > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getStructDefn()
	 */

	public IStructureDefn getStructDefn( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getDefault()
	 */

	public Object getDefault( )
	{
		return property.getDefaultValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getTargetElementType()
	 */
	public IElementDefn getTargetElementType( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getAllowedChoices()
	 */

	public IChoiceSet getAllowedChoices( )
	{
		return getChoices( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#isEncryptable()
	 */
	public boolean isEncryptable( )
	{
		return property.isEncryptable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getValueType()
	 */

	public int getValueType( )
	{
		return ODA_PROPERTY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getType()
	 */
	public PropertyType getType( )
	{
		return MetaDataDictionary.getInstance( ).getPropertyType(
				property.getType( ) );
	}
}
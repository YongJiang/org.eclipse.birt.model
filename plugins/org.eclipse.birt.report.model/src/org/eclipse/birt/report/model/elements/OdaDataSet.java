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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.ExtensionValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.extension.oda.OdaExtensibilityProvider;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;

/**
 * Represents an extended data set.
 */

public class OdaDataSet extends DataSet
		implements
			IExtendableElement,
			IOdaDataSetModel,
			IOdaExtendableElementModel
{

	/**
	 * Extensibility provider which provides the extension logic.
	 */

	private OdaExtensibilityProvider provider = null;

	/**
	 * ID of the extension which extends this ODA data set
	 */

	protected String extensionID = null;

	/**
	 * Default constructor.
	 */

	public OdaDataSet( )
	{
		super( );
	}

	/**
	 * Constructs an extended data set with name.
	 * 
	 * @param theName
	 *            the name of this extended data set
	 */

	public OdaDataSet( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */
	public void apply( ElementVisitor visitor )
	{
		visitor.visitOdaDataSet( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName( )
	{
		return ReportDesignConstants.ODA_DATA_SET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */
	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the report design
	 * @return an API handle for this element
	 */

	public OdaDataSetHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new OdaDataSetHandle( module, this );
		}
		return (OdaDataSetHandle) handle;
	}

	/**
	 * Gets the definition of the extension element.
	 * 
	 * @return the definition of the extension element if found, or null if the
	 *         extended item is not extensible or the extension element is not
	 *         registered in BIRT
	 */

	public ExtensionElementDefn getExtDefn( )
	{
		if ( provider != null )
			return provider.getExtDefn( );

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefns()
	 */

	public List getPropertyDefns( )
	{
		if ( provider != null )
			return provider.getPropertyDefns( );

		return super.getPropertyDefns( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefn(java.lang.String)
	 */

	public ElementPropertyDefn getPropertyDefn( String propName )
	{
		assert propName != null;

		ElementPropertyDefn propDefn = super.getPropertyDefn( propName );
		if ( propDefn != null )
			return propDefn;

		if ( provider != null )
			return (ElementPropertyDefn) provider.getPropertyDefn( propName );

		return propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( EXTENSION_ID_PROP.equals( propName ) )
			return extensionID;

		return super.getIntrinsicProperty( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( EXTENSION_ID_PROP.equals( propName ) )
		{
			extensionID = (String) value;
			if ( extensionID != null )
				provider = new OdaExtensibilityProvider( this, extensionID );
			else
				provider = null;
		}
		else
		{
			super.setIntrinsicProperty( propName, value );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkExtends(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		super.checkExtends( parent );

		if ( provider != null )
			provider.checkExtends( parent );
		else
		{
			OdaDataSet odaParent = (OdaDataSet) parent;

			if ( odaParent.extensionID != null
					&& !odaParent.extensionID.equals( extensionID ) )
				throw new ExtendsException( this, parent,
						ExtendsException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE );

			if ( extensionID != null
					&& !extensionID.equals( odaParent.extensionID ) )
				throw new ExtendsException( this, parent,
						ExtendsException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( Module module )
	{
		List list = super.validate( module );

		list
				.addAll( ExtensionValidator.getInstance( ).validate( module,
						this ) );

		return list;
	}
}
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

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.ContentIterator;

/**
 * Base class for all report items. Represents anything that can be placed in a
 * layout container. Items have a size and position that are used in some of the
 * containers.
 * 
 */

public abstract class ReportItem extends ReferencableStyledElement
		implements
			IReportItemModel
{

	/**
	 * Default constructor.
	 */

	public ReportItem( )
	{
		this( null );
	}

	/**
	 * Constructs the report item with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public ReportItem( String theName )
	{
		super( theName );
		cachedPropStrategy = ReportItemPropSearchStrategy.getInstance( );
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param module
	 *            the report design of the report item
	 * 
	 * @return the data set element defined on this specific element
	 */

	public DesignElement getDataSetElement( Module module )
	{
		ElementRefValue dataSetRef = (ElementRefValue) getProperty( module,
				DATA_SET_PROP );
		if ( dataSetRef == null )
			return null;
		return dataSetRef.getElement( );
	}

	/**
	 * Returns the cube element, if any, for this element.
	 * 
	 * @param module
	 *            the report design of the report item
	 * 
	 * @return the cube element defined on this specific element
	 */

	public DesignElement getCubeElement( Module module )
	{
		ElementRefValue cubeRef = (ElementRefValue) getProperty( module,
				CUBE_PROP );
		if ( cubeRef == null )
			return null;
		return cubeRef.getElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public List validate( Module module )
	{
		List list = super.validate( module );

		// Check the element reference of dataSet property

		list.addAll( ElementReferenceValidator.getInstance( ).validate( module,
				this, DATA_SET_PROP ) );

		list.addAll( validateStructureList( module, PARAM_BINDINGS_PROP ) );

		return list;
	}

	/**
	 * Checks whether the listing element refers to another listing element.
	 * 
	 * @param module
	 *            the root of the listing element
	 * @return <code>true</code> if the listing element shares data with other
	 *         listing element. Otherwise <code>false</code>.
	 */

	public boolean isDataBindingReferring( Module module )
	{
		ElementRefValue refValue = (ElementRefValue) getLocalProperty( module,
				IReportItemModel.DATA_BINDING_REF_PROP );
		if ( refValue == null || !refValue.isResolved( ) )
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getProperty(org.eclipse
	 * .birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */
	public Object getProperty( Module module, ElementPropertyDefn prop )
	{

		String propName = prop.getName( );
		if ( IReportItemModel.CASCADE_ACL_PROP.equals( propName )
				&& ( !getDefn( ).isContainer( ) ) )
		{
			return false;
		}
		return super.getProperty( module, prop );
	}

	/**
	 * Determines whether this report item can cascade ACL or not. True if and
	 * only if this item has define
	 * <code>IReportItemModel.CASCADE_ACL_PROP</code> property and it is a
	 * container.
	 * 
	 * @return true if this item has define
	 *         <code>IReportItemModel.CASCADE_ACL_PROP</code> property and it is
	 *         a container, otherwise false
	 */
	public boolean canCascadeACL( )
	{
		if ( getPropertyDefn( IReportItemModel.CASCADE_ACL_PROP ) != null
				&& getDefn( ).isContainer( ) )
			return true;
		return false;
	}

	/**
	 * Caches values for the element. The caller must be the report design.
	 */

	public void cacheValues( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#checkExtends(org.eclipse
	 * .birt.report.model.core.DesignElement)
	 */
	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		super.checkExtends( parent );
		Module lib = parent.getRoot( );
		checkDataBindingReferring( lib, parent );

		ContentIterator iter = new ContentIterator( lib, parent );
		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			checkDataBindingReferring( lib, element );
		}

	}

	/**
	 * Checks whether the report item refers to another report item.
	 * 
	 * @param lib
	 *            the library.
	 * @param element
	 *            the design element.
	 * @throws ExtendsException
	 *             if the listing element shares data with other listing
	 *             element.
	 */
	private void checkDataBindingReferring( Module lib, DesignElement element )
			throws ExtendsException
	{
		if ( element instanceof ReportItem
				&& ( (ReportItem) element ).isDataBindingReferring( lib ) )
		{
			throw new ExtendsForbiddenException(
					null,
					element,
					ExtendsForbiddenException.DESIGN_EXCEPTION_RESULT_SET_SHARED_CANT_EXTEND );
		}
	}
}

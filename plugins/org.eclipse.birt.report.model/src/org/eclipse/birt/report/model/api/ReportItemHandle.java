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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.IReportItemMethodContext;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.UnusedBoundColumnsMgr;

/**
 * Represents a report item: any element that can appear within a section of the
 * report. Report items have a size and position that are used in some
 * containers. Report items also have a style. Report items can references to
 * the data set to use for itself. Many report items can be the target of
 * hyperlinks. The bookmark property identifies the item location. It also has a
 * set of visibility rules that say when a report item should be hidden. The
 * bindings allow a report item to pass data into its data source. Call
 * {@link DesignElementHandle#getPrivateStyle}( ) to get a handle with
 * getter/setter methods for the style properties.
 * 
 * @see org.eclipse.birt.report.model.elements.ReportItem
 */

public abstract class ReportItemHandle extends ReportElementHandle
		implements
			IReportItemModel,
			IStyledElementModel,
			IReportItemMethodContext
{

	/**
	 * Data binding type "none".
	 */

	public static final int DATABINDING_TYPE_NONE = 0;

	/**
	 * Data binding type "data", means the report item is binding to a data set
	 * or cube.
	 */

	public static final int DATABINDING_TYPE_DATA = 1;

	/**
	 * Data binding type "reportItemRef", means the report item is binding to
	 * another report item.
	 */

	public static final int DATABINDING_TYPE_REPORT_ITEM_REF = 2;

	/**
	 * Constructs the handle for a report item with the given design and
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ReportItemHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the data set of the report item.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		DesignElement dataSet = ( (ReportItem) getElement( ) )
				.getDataSetElement( module );
		if ( dataSet == null )
			return null;

		assert dataSet instanceof DataSet;

		return (DataSetHandle) dataSet.getHandle( dataSet.getRoot( ) );
	}

	/**
	 * Sets the data set of the report item.
	 * 
	 * @param handle
	 *            the handle of the data set
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDataSet( DataSetHandle handle ) throws SemanticException
	{
		if ( handle == null )
			setStringProperty( DATA_SET_PROP, null );
		else
		{
			ModuleHandle moduleHandle = handle.getRoot( );
			String valueToSet = handle.getElement( ).getFullName( );
			if ( moduleHandle instanceof LibraryHandle )
			{
				String namespace = ( (LibraryHandle) moduleHandle )
						.getNamespace( );
				valueToSet = StringUtil.buildQualifiedReference( namespace,
						valueToSet );
			}
			setStringProperty( DATA_SET_PROP, valueToSet );
		}
	}

	/**
	 * Returns the cube of the report item.
	 * 
	 * @return the handle to the cube
	 */

	public CubeHandle getCube( )
	{
		DesignElement cube = ( (ReportItem) getElement( ) )
				.getCubeElement( module );
		if ( cube == null )
			return null;

		assert cube instanceof Cube;

		return (CubeHandle) cube.getHandle( cube.getRoot( ) );
	}

	/**
	 * Sets the cube of the report item.
	 * 
	 * @param handle
	 *            the handle of the cube
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setCube( CubeHandle handle ) throws SemanticException
	{
		if ( handle == null )
			setStringProperty( CUBE_PROP, null );
		else
		{
			ModuleHandle moduleHandle = handle.getRoot( );
			String valueToSet = handle.getElement( ).getFullName( );
			if ( moduleHandle instanceof LibraryHandle )
			{
				String namespace = ( (LibraryHandle) moduleHandle )
						.getNamespace( );
				valueToSet = StringUtil.buildQualifiedReference( namespace,
						valueToSet );
			}
			setStringProperty( CUBE_PROP, valueToSet );
		}
	}

	/**
	 * Gets a handle to deal with the item's x (horizontal) position.
	 * 
	 * @return a DimensionHandle for the item's x position.
	 */

	public DimensionHandle getX( )
	{
		return super.getDimensionProperty( IReportItemModel.X_PROP );
	}

	/**
	 * Gets a handle to deal with the item's y (vertical) position.
	 * 
	 * @return a DimensionHandle for the item's y position.
	 */

	public DimensionHandle getY( )
	{
		return super.getDimensionProperty( IReportItemModel.Y_PROP );
	}

	/**
	 * Sets the item's x position using a dimension string with optional unit
	 * suffix such as "10" or "10pt". If no suffix is provided, then the units
	 * are assumed to be in the design's default units. Call this method to set
	 * a string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setX( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.X_PROP, dimension );
	}

	/**
	 * Sets the item's x position to a value in default units. The default unit
	 * may be defined by the property in BIRT or the application unit defined in
	 * the design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setX( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.X_PROP, dimension );
	}

	/**
	 * Sets the item's y position using a dimension string with optional unit
	 * suffix such as "10" or "10pt". If no suffix is provided, then the units
	 * are assumed to be in the design's default units. Call this method to set
	 * a string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setY( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.Y_PROP, dimension );
	}

	/**
	 * Sets the item's y position to a value in default units. The default unit
	 * may be defined by the property in BIRT or the application unit defined in
	 * the design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setY( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.Y_PROP, dimension );
	}

	/**
	 * Sets the item's height using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are
	 * assumed to be in the design's default units. Call this method to set a
	 * string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setHeight( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.HEIGHT_PROP, dimension );
	}

	/**
	 * Sets the item's height to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setHeight( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.HEIGHT_PROP, dimension );
	}

	/**
	 * Sets the item's width using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are
	 * assumed to be in the design's default units. Call this method to set a
	 * string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setWidth( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.WIDTH_PROP, dimension );
	}

	/**
	 * Sets the item's width to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setWidth( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.WIDTH_PROP, dimension );
	}

	/**
	 * Gets a handle to deal with the item's width.
	 * 
	 * @return a DimensionHandle for the item's width.
	 */

	public DimensionHandle getWidth( )
	{
		return super.getDimensionProperty( IReportItemModel.WIDTH_PROP );
	}

	/**
	 * Gets a handle to deal with the item's height.
	 * 
	 * @return a DimensionHandle for the item's height.
	 */
	public DimensionHandle getHeight( )
	{
		return super.getDimensionProperty( IReportItemModel.HEIGHT_PROP );
	}

	/**
	 * Returns the bookmark of the report item.
	 * 
	 * @return the book mark as a string
	 */

	public String getBookmark( )
	{
		return getStringProperty( IReportItemModel.BOOKMARK_PROP );
	}

	/**
	 * Sets the bookmark of the report item.
	 * 
	 * @param value
	 *            the property value to be set.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setBookmark( String value ) throws SemanticException
	{
		setStringProperty( IReportItemModel.BOOKMARK_PROP, value );
	}

	/**
	 * Returns visibility rules defined on the report item. The element in the
	 * iterator is the corresponding <code>StructureHandle</code> that deal
	 * with a <code>Hide</code> in the list.
	 * 
	 * @return the iterator for visibility rules defined on this report item.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.HideRule
	 */

	public Iterator visibilityRulesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		if ( propHandle == null )
			return Collections.EMPTY_LIST.iterator( );
		return propHandle.iterator( );
	}

	/**
	 * Returns the script executed when the element is created in the Factory.
	 * Called after the item is created, but before the item is saved to the
	 * report document file.
	 * 
	 * @return the script that executes
	 */

	public String getOnCreate( )
	{
		return getStringProperty( IReportItemModel.ON_CREATE_METHOD );
	}

	/**
	 * Sets the script executed when the element is created in the Factory.
	 * Called after the item is created, but before the item is saved to the
	 * report document file.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOnCreate( String value ) throws SemanticException
	{
		setProperty( IReportItemModel.ON_CREATE_METHOD, value );
	}

	/**
	 * Returns the script executed when the element is prepared for rendering in
	 * the Presentation engine.
	 * 
	 * @return the script that executes
	 */

	public String getOnRender( )
	{
		return getStringProperty( IReportItemModel.ON_RENDER_METHOD );
	}

	/**
	 * Sets the script executed when the element is prepared for rendering in
	 * the Presentation engine.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOnRender( String value ) throws SemanticException
	{
		setProperty( IReportItemModel.ON_RENDER_METHOD, value );
	}

	/**
	 * Returns the iterator for parameter binding list defined on this report
	 * item. The element in the iterator is the corresponding
	 * <code>StructureHandle</code> that deal with a <code>ParamBinding</code>
	 * in the list.
	 * 
	 * @return the iterator for parameter binding structure list defined on this
	 *         data set.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ParamBinding
	 */

	public Iterator paramBindingsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( IReportItemModel.PARAM_BINDINGS_PROP );
		if ( propHandle == null )
			return Collections.EMPTY_LIST.iterator( );
		return propHandle.iterator( );
	}

	/**
	 * Sets a table of contents entry for this item. The TOC property defines an
	 * expression that returns a string that is to appear in the Table of
	 * Contents for this item or its container.
	 * 
	 * @param expression
	 *            the expression that returns a string
	 * @throws SemanticException
	 *             if the TOC property is locked by the property mask.
	 * 
	 * @see #getTocExpression()
	 * @deprecated
	 */

	public void setTocExpression( String expression ) throws SemanticException
	{
		if ( StringUtil.isEmpty( expression ) )
		{
			setProperty( IReportItemModel.TOC_PROP, null );
			return;
		}
		TOCHandle tocHandle = getTOC( );
		if ( StringUtil.isBlank( expression ) )
			return;
		if ( tocHandle == null )
		{
			TOC toc = StructureFactory.createTOC( expression );
			addTOC( toc );
		}
		else
		{
			tocHandle.setExpression( expression );
		}

	}

	/**
	 * Returns the expression evalueated as a table of contents entry for this
	 * item.
	 * 
	 * @return the expression evaluated as a table of contents entry for this
	 *         item
	 * @see #setTocExpression(String)
	 * @deprecated
	 */

	public String getTocExpression( )
	{
		TOCHandle tocHandle = getTOC( );
		if ( tocHandle == null )
			return null;
		return tocHandle.getExpression( );
	}

	/**
	 * Gets the on-prepare script of the group. Startup phase. No data binding
	 * yet. The design of an element can be changed here.
	 * 
	 * @return the on-prepare script of the group
	 * 
	 */

	public String getOnPrepare( )
	{
		return getStringProperty( IReportItemModel.ON_PREPARE_METHOD );
	}

	/**
	 * Sets the on-prepare script of the group element.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnPrepare()
	 */

	public void setOnPrepare( String script ) throws SemanticException
	{
		setProperty( IReportItemModel.ON_PREPARE_METHOD, script );
	}

	/**
	 * Gets the on-pageBreak script of the report item. Presentation phase. It
	 * is for a script executed when the element is prepared for page breaking
	 * in the Presentation engine.
	 * 
	 * @return the on-pageBreak script of the report item
	 * 
	 */

	public String getOnPageBreak( )
	{
		return getStringProperty( ON_PAGE_BREAK_METHOD );
	}

	/**
	 * Sets the on-pageBreak script of the report item.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnPageBreak()
	 */

	public void setOnPageBreak( String script ) throws SemanticException
	{
		setProperty( ON_PAGE_BREAK_METHOD, script );
	}

	/**
	 * Returns the bound columns that binds the data set columns. The item in
	 * the iterator is the corresponding <code>ComputedColumnHandle</code>.
	 * 
	 * @return a list containing the bound columns.
	 */

	public Iterator columnBindingsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( BOUND_DATA_COLUMNS_PROP );
		if ( propHandle == null )
			return Collections.EMPTY_LIST.iterator( );
		return propHandle.iterator( );
	}

	/**
	 * Get a handle to deal with the bound column.
	 * 
	 * @return a handle to deal with the boudn data column.
	 */

	public PropertyHandle getColumnBindings( )
	{
		return getPropertyHandle( BOUND_DATA_COLUMNS_PROP );
	}

	/**
	 * Adds a bound column to the list.
	 * 
	 * @param addColumn
	 *            the bound column to add
	 * @param inForce
	 *            <code>true</code> the column is added to the list regardless
	 *            of duplicate expression. <code>false</code> do not add the
	 *            column if the expression already exist
	 * @param column
	 *            the bound column
	 * @return the newly created <code>ComputedColumnHandle</code> or the
	 *         existed <code>ComputedColumnHandle</code> in the list
	 * @throws SemanticException
	 *             if expression is not duplicate but the name duplicates the
	 *             exsiting bound column. Or, if the both name/expression are
	 *             duplicate, but <code>inForce</code> is <code>true</code>.
	 */

	public ComputedColumnHandle addColumnBinding( ComputedColumn addColumn,
			boolean inForce ) throws SemanticException
	{
		if ( addColumn == null )
			return null;

		String expr = addColumn.getExpression( );

		List columns = (List) getProperty( BOUND_DATA_COLUMNS_PROP );
		if ( columns == null )
			return (ComputedColumnHandle) getPropertyHandle(
					BOUND_DATA_COLUMNS_PROP ).addItem( addColumn );

		ComputedColumn column = BoundDataColumnUtil.getColumn( columns, expr,
				addColumn.getAggregateFunction( ), addColumn
						.getAggregateOnList( ) );

		if ( column != null && !inForce )
		{
			return (ComputedColumnHandle) column.handle(
					getPropertyHandle( BOUND_DATA_COLUMNS_PROP ), columns
							.indexOf( column ) );
		}
		return (ComputedColumnHandle) getPropertyHandle(
				BOUND_DATA_COLUMNS_PROP ).addItem( addColumn );
	}

	/**
	 * Finds a column binding with the given name.
	 * 
	 * @param name
	 *            name of the binding to find
	 * @return column binding with the given name if found, otherwise null
	 */
	public ComputedColumnHandle findColumnBinding( String name )
	{
		if ( name == null )
			return null;
		List columns = (List) getProperty( BOUND_DATA_COLUMNS_PROP );
		if ( columns == null )
			return null;
		for ( int i = 0; i < columns.size( ); i++ )
		{
			ComputedColumn column = (ComputedColumn) columns.get( i );
			if ( name.equals( column.getName( ) ) )
				return (ComputedColumnHandle) column.handle(
						getPropertyHandle( BOUND_DATA_COLUMNS_PROP ), i );
		}
		return null;
	}

	/**
	 * Removes unused bound columns from the element. Bound columns of nested
	 * elements will not be removed. For example, if calls this method for a
	 * list thaat contains a text-data, unused bound columns on list are
	 * removed. While, unused columns on text-data still are kept.
	 * 
	 * @throws SemanticException
	 *             if bound column property is locked.
	 */

	public void removedUnusedColumnBindings( ) throws SemanticException
	{
		UnusedBoundColumnsMgr.removedUnusedBoundColumns( this );
	}

	/**
	 * Gets TOC handle.
	 * 
	 * @return toc handle
	 */

	public TOCHandle getTOC( )
	{
		PropertyHandle propHandle = getPropertyHandle( IReportItemModel.TOC_PROP );
		if ( propHandle == null )
			return null;
		TOC toc = (TOC) propHandle.getValue( );

		if ( toc == null )
			return null;

		return (TOCHandle) toc.getHandle( propHandle );
	}

	/**
	 * Adds toc structure.
	 * 
	 * @param expression
	 *            toc expression
	 * @return toc handle
	 * @throws SemanticException
	 */

	public TOCHandle addTOC( String expression ) throws SemanticException
	{
		if ( StringUtil.isEmpty( expression ) )
			return null;

		TOC toc = StructureFactory.createTOC( expression );
		setProperty( IReportItemModel.TOC_PROP, toc );

		return (TOCHandle) toc
				.getHandle( getPropertyHandle( IReportItemModel.TOC_PROP ) );
	}

	/**
	 * Adds toc structure.
	 * 
	 * @param toc
	 *            toc structure
	 * @return toc handle
	 * @throws SemanticException
	 */

	public TOCHandle addTOC( TOC toc ) throws SemanticException
	{
		setProperty( IReportItemModel.TOC_PROP, toc );

		if ( toc == null )
			return null;
		return (TOCHandle) toc
				.getHandle( getPropertyHandle( IReportItemModel.TOC_PROP ) );
	}

	/**
	 * Gets the item's z position as an integer.
	 * 
	 * @return the z depth. Start from 0
	 */

	public int getZIndex( )
	{
		return super.getIntProperty( IReportItemModel.Z_INDEX_PROP );
	}

	/**
	 * Sets the item's z position to an integer.
	 * 
	 * @param zIndex
	 *            the z depth. Start from 0
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setZIndex( int zIndex ) throws SemanticException
	{
		setIntProperty( IReportItemModel.Z_INDEX_PROP, zIndex );
	}

	/**
	 * Returns functions that can be called in the given method.
	 * 
	 * @param methodName
	 *            the method name in string
	 * 
	 * @return a list containing <code>IMethodInfo</code> for functions
	 */

	public List getMethods( String methodName )
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * Sets the report item of which data binding are referred by.
	 * 
	 * @param item
	 *            the report item
	 * @throws SemanticException
	 *             if the element reference makes a circle
	 */

	public void setDataBindingReference( ReportItemHandle item )
			throws SemanticException
	{
		setProperty( DATA_BINDING_REF_PROP, item );
	}

	/**
	 * Returns the name of the report item of which data binding are referred
	 * by.
	 * 
	 * @return the report item name
	 */

	public String getDataBindingReferenceName( )
	{
		return (String) getProperty( DATA_BINDING_REF_PROP );
	}

	/**
	 * Returns the report item of which data binding are referred by.
	 * 
	 * @return the report item
	 */

	public ReportItemHandle getDataBindingReference( )
	{
		ElementRefValue refValue = (ElementRefValue) getElement( ).getProperty(
				module, DATA_BINDING_REF_PROP );
		if ( refValue == null || !refValue.isResolved( ) )
			return null;

		DesignElement tmpElement = refValue.getElement( );
		return (ReportItemHandle) tmpElement.getHandle( tmpElement.getRoot( ) );
	}

	/**
	 * Returns the data binding type of this report item. The return value
	 * should be one of following:
	 * 
	 * <ul>
	 * <li>DATABINDING_TYPE_NONE, no data binding.
	 * <li>DATABINDING_TYPE_DATA, data binding to data set or cube.
	 * <li>DATABINDING_TYPE_REPORT_ITEM_REF, data binding to another report
	 * item.
	 * </ul>
	 * 
	 * @return the data binding type of this report item
	 */

	public int getDataBindingType( )
	{
		if ( getDataBindingReferenceName( ) != null )
			return DATABINDING_TYPE_REPORT_ITEM_REF;

		// TODO: after add the "cube" property on ReportItem in ROM, substitute
		// below literal string with constants variable.

		if ( getDataSet( ) != null ||
				element.getProperty( module, "cube" ) != null ) //$NON-NLS-1$
			return DATABINDING_TYPE_DATA;

		return DATABINDING_TYPE_NONE;
	}

	/**
	 * Returns report items that can be referred by other report items by data
	 * binding reference property.
	 * <p>
	 * Two kinds of report items can be referred:
	 * <ul>
	 * <li>The report item has dataset property defined. That is, data set
	 * property is set locally and databinding ref property is null.
	 * <li>The report item has data binding reference to other report items.
	 * </ul>
	 * ReportItem in the design are all applicable. Each entry of the return
	 * list is of <code>ReportItemHandle</code> type.
	 * 
	 * @return returns report items that has dataset property defined
	 */

	public List getAvailableDataBindingReferenceList( )
	{
		List rtnList = new ArrayList( );

		NameSpace ns = module.getNameHelper( ).getNameSpace(
				Module.ELEMENT_NAME_SPACE );
		List elements = ns.getElements( );
		for ( int i = 0; i < elements.size( ); i++ )
		{
			DesignElement e = (DesignElement) elements.get( i );
			if ( e == getElement( ) )
				continue;

			if ( e instanceof ReportItem )
			{
				ReportItemHandle elementHandle = (ReportItemHandle) e
						.getHandle( module );
				int bindingType = elementHandle.getDataBindingType( );

				if ( e.getName( ) == null )
					continue;

				if ( bindingType == DATABINDING_TYPE_DATA )
					rtnList.add( elementHandle );
				else if ( bindingType == DATABINDING_TYPE_REPORT_ITEM_REF )
				{

					DesignElementHandle tmpElementHandle = elementHandle
							.getDataBindingReference( );

					if ( tmpElementHandle == null )
					{
						rtnList.add( elementHandle );
						continue;
					}

					if ( element instanceof IReferencableElement &&
							!ModelUtil.isRecursiveReference( tmpElementHandle
									.getElement( ),
									(IReferencableElement) element ) )
						rtnList.add( elementHandle );
				}
			}
		}

		if ( rtnList.isEmpty( ) )
			return Collections.EMPTY_LIST;

		return Collections.unmodifiableList( rtnList );
	}

	/**
	 * Removes bound columns from the element.
	 * 
	 * @param bindingNameList
	 *            the binding name list to be removed, each entry should be
	 *            instanceof <code>java.lang.String</code>.
	 * @throws SemanticException
	 *             if bound column property is locked.
	 */

	public void removedColumnBindings( List bindingNameList )
			throws SemanticException
	{
		if ( bindingNameList == null || bindingNameList.isEmpty( ) )
			return;

		for ( int i = 0; i < bindingNameList.size( ); i++ )
		{
			removedColumnBinding( (String) bindingNameList.get( i ) );
		}
	}

	/**
	 * Removes bound column from the element.
	 * 
	 * @param bindingName
	 *            the binding name to be removed
	 * @throws SemanticException
	 *             if bound column property is locked.
	 */

	public void removedColumnBinding( String bindingName )
			throws SemanticException
	{
		ComputedColumnHandle toRemoveColumn = findColumnBinding( bindingName );
		if ( toRemoveColumn == null )
			return;

		toRemoveColumn.drop( );
	}
	
	/**
	 * Returns the view that is being used.
	 * 
	 * @return the view that is being used
	 */

	public DesignElementHandle getCurrentView( )
	{
		MultiViewsAPIProvider provider = new MultiViewsAPIProvider(
				this, MULTI_VIEWS_PROP );
		return provider.getCurrentView( );
	}

	/**
	 * Adds a new element as the view.
	 * 
	 * @param viewElement
	 *            the element
	 * @throws SemanticException
	 */

	public void addView( DesignElementHandle viewElement )
			throws SemanticException
	{
		MultiViewsAPIProvider provider = new MultiViewsAPIProvider(
				this, MULTI_VIEWS_PROP );
		provider.addView( viewElement );		
	}

	/**
	 * Deletes the given view.
	 * 
	 * @param viewElement
	 *            the element
	 * @throws SemanticException
	 */

	public void dropView( DesignElementHandle viewElement )
			throws SemanticException
	{
		MultiViewsAPIProvider provider = new MultiViewsAPIProvider(
				this, MULTI_VIEWS_PROP );
		provider.dropView( viewElement );
	}

	/**
	 * Sets the index for the view to be used. If the given element is not in
	 * the multiple view, it will be added and set as the active view.
	 * 
	 * @param viewElement
	 *            the view element
	 * 
	 * @throws SemanticException
	 *             if the given element resides in the other elements.
	 */

	public void setCurrentView( DesignElementHandle viewElement )
			throws SemanticException
	{
		if ( viewElement == null )
			return;
		
		MultiViewsAPIProvider provider = new MultiViewsAPIProvider(
				this, MULTI_VIEWS_PROP );
		provider.setCurrentView( viewElement );
	}
}
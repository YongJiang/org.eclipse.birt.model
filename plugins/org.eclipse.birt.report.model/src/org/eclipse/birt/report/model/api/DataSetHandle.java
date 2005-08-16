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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Abstract handle for data set elements. A data set is a named object that
 * provides a result set defined as a sequence of data rows. Report elements use
 * data sets to retrieve data for display.
 * <p>
 * A data set has three key parts:
 * <p>
 * <ul>
 * <li>Data access: Retrieving data from an external data source.
 * <li>Report-specific properties: Properties for how the data is to be used in
 * the report such as rules for searching, data export and so on.
 * <li>Data transforms: Rules for processing the data for use by the report.
 * Data transforms are most frequently defined by report items that use the data
 * set, and are applied to the result set by BIRT.
 * </ul>
 * <p>
 * Data transforms that can be defined on a data set include:
 * <ul>
 * <li>Column projections: identifying additional required columns, assigning
 * column aliases, and providing column meta-data.
 * <li>Filters.
 * <li>Computed columns. Sorting, grouping, aggregations and similar transforms
 * are defined by the report elements that use the data set.
 * </ul>
 * <p>
 * The application can use scripts to execute code on certain data set events.
 * <p>
 * To get a handle for the data source, uses the following example:
 * 
 * <pre>
 * 
 *     
 * 		DataSetHandle dataHandle = designHandle
 *         findDataSet( &quot;My First Data Set &quot; );
 *  
 * </pre>
 * 
 * <p>
 * This class works with the static design definition of the data set. Many
 * clients will prefer to work with the TBD class that provides both the static
 * definition and additional design information retrieved from the data
 * provider. For example, a particular data set may not define a result set in
 * the design file if the data provider can provide the result set definition
 * itself. This handle will return null for the result set handle. However, the
 * TBD class will provide the full result set: either by returning the one in
 * the design file, or by obtaining it from the data provider.
 * 
 * @see org.eclipse.birt.report.model.elements.DataSet
 */

public abstract class DataSetHandle extends ReportElementHandle
		implements
			IDataSetModel
{

	/**
	 * Constructs a data set handle with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public DataSetHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns a handle to the data source for this data set.
	 * 
	 * @return handle to the data source, or <code>null</code> if the data
	 *         source is not set or is undefined.
	 */

	public DataSourceHandle getDataSource( )
	{
		return (DataSourceHandle) getElementProperty( DataSet.DATA_SOURCE_PROP );
	}

	/**
	 * Returns the name of the data source for this data set.
	 * 
	 * @return the data source name as a string
	 * 
	 * @see #setDataSource(String)
	 */

	public String getDataSourceName( )
	{
		return getStringProperty( DataSet.DATA_SOURCE_PROP );
	}

	/**
	 * Sets the name of the data source for this data set. This method checks
	 * whether the data source name exists in the report design.
	 * 
	 * @param name
	 *            the data source name
	 * @throws SemanticException
	 *             if the data source does not exist in the report design, or
	 *             the property if locked.
	 * @see #getDataSource()
	 */

	public void setDataSource( String name ) throws SemanticException
	{
		setStringProperty( DataSet.DATA_SOURCE_PROP, name );
	}

	/**
	 * Returns an iterator over the list of output parameter definitions. The
	 * iterator returns instances of <code>DataSetParameterHandle</code> that
	 * represents parameter objects.
	 * 
	 * @return iterator over output parameter definitions.
	 * @see org.eclipse.birt.report.model.api.elements.structures.DataSetParameter
	 */

	public Iterator parametersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( DataSet.PARAMETERS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns an iterator over the list of parameter bindings. The iterator
	 * returns instances of <code>ParamBindingHandle</code> that represents
	 * parameter binding object.
	 * 
	 * @return iterator over parameter binding.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ParamBinding
	 */

	public Iterator paramBindingsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( DataSet.PARAM_BINDINGS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns an iterator over the result set columns. The iterator returns
	 * instances of <code>ResultSetColumnHandle</code> that represents result
	 * set column object.
	 * 
	 * @return iterator over result set columns.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn
	 */

	public Iterator resultSetIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( DataSet.RESULT_SET_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns an iterator for the list of computed columns. The iterator
	 * returns instances of <code>ComputedColumnHandle</code> that represents
	 * computed column object.
	 * 
	 * @return iterator over computed columns.
	 * @see org.eclipse.birt.report.model.api.elements.structures.ComputedColumn
	 */

	public Iterator computedColumnsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( DataSet.COMPUTED_COLUMNS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns an iterator over column hints. The iterator returns instances of
	 * <code>ColumnHintHandle</code> that represents column hint object.
	 * 
	 * @return iterator over column hints.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ColumnHint
	 */

	public Iterator columnHintsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( DataSet.COLUMN_HINTS_PROP );

		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns an iterator over filter. The iterator returns instances of
	 * <code>FilterConditionHandle</code> that represents filter condition
	 * object.
	 * 
	 * @return iterator over filters.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.FilterCondition
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( DataSet.FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the code of the beforeOpen method. This is the script called just
	 * before opening this data set.
	 * 
	 * @return the code of the method
	 */

	public String getBeforeOpen( )
	{
		return getStringProperty( DataSet.BEFORE_OPEN_METHOD );
	}

	/**
	 * Sets the code for the beforeOpen method. This is the script called just
	 * before opening this data set.
	 * 
	 * @param code
	 *            the code for the method
	 * @throws SemanticException
	 *             If the method is locked.
	 */

	public void setBeforeOpen( String code ) throws SemanticException
	{
		setProperty( DataSet.BEFORE_OPEN_METHOD, code );
	}

	/**
	 * Returns the code of the beforeClose method. This is the script called
	 * just before closing this data set.
	 * 
	 * @return the code of the method
	 */

	public String getBeforeClose( )
	{
		return getStringProperty( DataSet.BEFORE_CLOSE_METHOD );
	}

	/**
	 * Sets the code for the beforeClose method. This is the script called just
	 * before closing this data set.
	 * 
	 * @param code
	 *            the code for the method
	 * @throws SemanticException
	 *             If the method is locked.
	 */

	public void setBeforeClose( String code ) throws SemanticException
	{
		setProperty( DataSet.BEFORE_CLOSE_METHOD, code );
	}

	/**
	 * Returns the code of the afterOpen method. This is the script called just
	 * after opening this data set.
	 * 
	 * @return the code of the method
	 */

	public String getAfterOpen( )
	{
		return getStringProperty( DataSet.AFTER_OPEN_METHOD );
	}

	/**
	 * Sets the code for the afterOpen method. This is the script called just
	 * after opening this data set.
	 * 
	 * @param code
	 *            the code for the method
	 * @throws SemanticException
	 *             If the method is locked.
	 */

	public void setAfterOpen( String code ) throws SemanticException
	{
		setProperty( DataSet.AFTER_OPEN_METHOD, code );
	}

	/**
	 * Returns the code of the afterClose method. This is the script called just
	 * after closing this data set.
	 * 
	 * @return the code of the method
	 */

	public String getAfterClose( )
	{
		return getStringProperty( DataSet.AFTER_CLOSE_METHOD );
	}

	/**
	 * Sets the code for the afterClose method. This is the script called just
	 * after closing this data set.
	 * 
	 * @param code
	 *            the code for the method
	 * @throws SemanticException
	 *             If the method is locked.
	 */

	public void setAfterClose( String code ) throws SemanticException
	{
		setProperty( DataSet.AFTER_CLOSE_METHOD, code );
	}

	/**
	 * Returns the code of the onFetch method. This is the script called just
	 * after fetching each row.
	 * 
	 * @return the code of the method
	 */

	public String getOnFetch( )
	{
		return getStringProperty( DataSet.ON_FETCH_METHOD );
	}

	/**
	 * Sets the code for the onFetch method. This is the script called just
	 * after fetching each row.
	 * 
	 * @param code
	 *            the code for the method
	 * @throws SemanticException
	 *             If the method is locked.
	 */

	public void setOnFetch( String code ) throws SemanticException
	{
		setProperty( DataSet.ON_FETCH_METHOD, code );
	}

	/**
	 * Return a handle to deal with the cached data set information that include
	 * output column information when it gets from databases, input/output
	 * parameter definitions.
	 * 
	 * @return a <code>CachedMetaDataHandle</code> to deal with the cached
	 *         data set information, return <code>null</code> if the property
	 *         has not been set.
	 */

	public CachedMetaDataHandle getCachedMetaDataHandle( )
	{
		PropertyHandle propHandle = this
				.getPropertyHandle( DataSet.CACHED_METADATA_PROP );
		assert propHandle != null;

		CachedMetaData value = (CachedMetaData) propHandle.getValue( );
		if ( value == null )
			return null;

		return (CachedMetaDataHandle) value.getHandle( propHandle );
	}

	/**
	 * Set the value for the cached data set information.
	 * 
	 * @param metadata
	 *            a structure value include output column information ,
	 *            input/output parameter definitions.
	 * @return <code>CachedMetaDataHandle</code> to the input
	 *         <code>metadata</code>, return <code>null</code> if
	 *         <code>metadata</code> is <code>null</code>.
	 * @throws SemanticException
	 *             the input data is not valid.
	 */

	public CachedMetaDataHandle setCachedMetaData( CachedMetaData metadata )
			throws SemanticException
	{
		setProperty( DataSet.CACHED_METADATA_PROP, metadata );
		if ( metadata == null )
			return null;

		return (CachedMetaDataHandle) metadata
				.getHandle( getPropertyHandle( DataSet.CACHED_METADATA_PROP ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getPropertyHandle(java.lang.String)
	 */
	public PropertyHandle getPropertyHandle( String propName )
	{
		if ( PARAMETERS_PROP.equals( propName ) )
			return new DataSetParametersPropertyHandle( this, propName );

		return super.getPropertyHandle( propName );
	}

	/**
	 * Represents the property handle which handles the structure list of data
	 * set parameters.
	 */

	final static class DataSetParametersPropertyHandle extends PropertyHandle
	{

		/**
		 * Constructs the handle for a top-level property with the given element
		 * handle and the definition of the property.
		 * 
		 * @param element
		 *            a handle to a report element
		 * @param prop
		 *            the definition of the property.
		 */

		public DataSetParametersPropertyHandle( DesignElementHandle element,
				ElementPropertyDefn prop )
		{
			super( element, prop );
		}

		/**
		 * Constructs the handle for a top-level property with the given element
		 * handle and property name.
		 * 
		 * @param element
		 *            a handle to a report element
		 * @param propName
		 *            the name of the property
		 */

		public DataSetParametersPropertyHandle( DesignElementHandle element,
				String propName )
		{
			super( element, propName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#removeItem(int)
		 */

		public void removeItem( int posn ) throws PropertyValueException
		{
			DataSetParameterHandle paramHandle = (DataSetParameterHandle) getAt( posn );
			DataSetParameter param = (DataSetParameter) paramHandle
					.getStructure( );

			getModule( ).getActivityStack( ).startTrans( );

			try
			{
				super.removeItem( posn );
			}
			catch ( PropertyValueException e )
			{
				getModule( ).getActivityStack( ).rollback( );
				throw e;
			}

			try
			{
				// Drop the parameter binding for data set parameters

				removeParamBindingsFor( param.getName( ) );
			}
			catch ( PropertyValueException e )
			{
			}

			getModule( ).getActivityStack( ).commit( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#removeItem(org.eclipse.birt.report.model.api.core.IStructure)
		 */

		public void removeItem( IStructure item ) throws PropertyValueException
		{
			DataSetParameter param = (DataSetParameter) item;

			getModule( ).getActivityStack( ).startTrans( );

			try
			{
				super.removeItem( item );
			}
			catch ( PropertyValueException e )
			{
				getModule( ).getActivityStack( ).rollback( );
				throw e;
			}

			try
			{
				// Drop the parameter binding for data set parameters

				removeParamBindingsFor( param.getName( ) );
			}
			catch ( PropertyValueException e )
			{
			}

			getModule( ).getActivityStack( ).commit( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#removeItems(java.util.List)
		 */

		public void removeItems( List items ) throws PropertyValueException
		{
			getModule( ).getActivityStack( ).startTrans( );

			try
			{
				super.removeItems( items );
			}
			catch ( PropertyValueException e )
			{
				getModule( ).getActivityStack( ).rollback( );
				throw e;
			}

			try
			{
				// Drop the parameter binding for data set parameters

				removeParamBindingsFor( items );
			}
			catch ( PropertyValueException e )
			{
			}

			getModule( ).getActivityStack( ).commit( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#replaceItem(org.eclipse.birt.report.model.api.core.IStructure,
		 *      org.eclipse.birt.report.model.api.core.IStructure)
		 */

		public void replaceItem( IStructure oldItem, IStructure newItem )
				throws SemanticException
		{
			getModule( ).getActivityStack( ).startTrans( );

			try
			{
				super.replaceItem( oldItem, newItem );
			}
			catch ( PropertyValueException e )
			{
				getModule( ).getActivityStack( ).rollback( );
				throw e;
			}

			// Drop the parameter binding for data set parameters

			updateParamBindings( ( (DataSetParameter) oldItem ).getName( ),
					( (DataSetParameter) newItem ).getName( ) );

			getModule( ).getActivityStack( ).commit( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#setValue(java.lang.Object)
		 */

		public void setValue( Object value ) throws SemanticException
		{
			List paramList = getListValue( );

			getModule( ).getActivityStack( ).startTrans( );

			try
			{
				super.setValue( value );
			}
			catch ( PropertyValueException e )
			{
				getModule( ).getActivityStack( ).rollback( );
				throw e;
			}

			try
			{
				// Drop the parameter binding for data set parameters

				removeParamBindingsFor( paramList );
			}
			catch ( PropertyValueException e )
			{
			}

			getModule( ).getActivityStack( ).commit( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#clearValue()
		 */

		public void clearValue( ) throws SemanticException
		{
			List paramList = getListValue( );

			getModule( ).getActivityStack( ).startTrans( );

			try
			{
				super.clearValue( );
			}
			catch ( PropertyValueException e )
			{
				getModule( ).getActivityStack( ).rollback( );
				throw e;
			}

			try
			{
				// Drop the parameter binding for data set parameters

				removeParamBindingsFor( paramList );
			}
			catch ( PropertyValueException e )
			{
			}

			getModule( ).getActivityStack( ).commit( );
		}

		/**
		 * Removes the parameter binding for the parameters in the given
		 * parameter list. The parameter binding is defined in clients of the
		 * data set in which this handle exists. The instance in the list can be
		 * both <code>DataSetParameterHandle</code> and
		 * <code>DataSetParameter</code>.
		 * 
		 * @param params
		 *            list of the data set parameters
		 * @throws PropertyValueException
		 *             if error occurs when removing parameter binding.
		 */

		private void removeParamBindingsFor( List params )
				throws PropertyValueException
		{
			if ( params == null )
				return;

			Iterator iter = params.iterator( );
			while ( iter.hasNext( ) )
			{
				String paramName = null;
				Object item = iter.next( );
				if ( item instanceof DataSetParameterHandle )
				{
					paramName = ( (DataSetParameterHandle) item ).getName( );
				}
				else if ( item instanceof DataSetParameter )
				{
					paramName = ( (DataSetParameter) item ).getName( );
				}
				else
				{
					assert false;
				}

				// Drop the parameter binding for data set parameters

				removeParamBindingsFor( paramName );
			}
		}

		/**
		 * Removes the parameter binding for the parameter with the given
		 * parameter name. The parameter binding is defined in clients of the
		 * data set in which this handle exists.
		 * 
		 * @param paramName
		 *            name of the parameter
		 * @throws PropertyValueException
		 *             if error occurs when removing parameter binding.
		 */

		private void removeParamBindingsFor( String paramName )
				throws PropertyValueException
		{
			if ( paramName == null )
				return;

			Iterator iter = getElementHandle( ).clientsIterator( );
			while ( iter.hasNext( ) )
			{
				DesignElementHandle client = (DesignElementHandle) iter.next( );

				// Remove bindings from report items

				PropertyHandle paramBindingsPropHandle = client
						.getPropertyHandle( ReportItem.PARAM_BINDINGS_PROP );
				removeParamBindingFor( paramBindingsPropHandle, paramName );

				// Remove binding from action

				PropertyHandle actionPropHandle = client
						.getPropertyHandle( ImageItem.ACTION_PROP );
				if ( actionPropHandle != null )
				{
					Action action = (Action) actionPropHandle.getValue( );
					ActionHandle actionHandle = (ActionHandle) action
							.getHandle( actionPropHandle );
					MemberHandle paramBindingsMemberHandle = actionHandle
							.getMember( Action.PARAM_BINDINGS_MEMBER );
					removeParamBindingFor( paramBindingsMemberHandle, paramName );
				}
			}

			// Remove the bindings with the given name from data set itself

			PropertyHandle paramBindingsPropHandle = getElementHandle( )
					.getPropertyHandle( PARAM_BINDINGS_PROP );
			removeParamBindingFor( paramBindingsPropHandle, paramName );
		}

		/**
		 * Removes the parameter binding with the given name from the given
		 * parameter binding property handle.
		 * 
		 * @param paramBindingsPropHandle
		 *            the parameter binding property handle from which parameter
		 *            binding will be removed.
		 * @param paramName
		 *            the name of the parameter with which parameter binding
		 *            will be removed.
		 * @throws PropertyValueException
		 *             if error occurs when removing parameter binding
		 */

		private static void removeParamBindingFor(
				SimpleValueHandle paramBindingsPropHandle, String paramName )
				throws PropertyValueException
		{
			if ( paramBindingsPropHandle == null )
				return;

			List bindings = new ArrayList( );

			Iterator bindingIter = paramBindingsPropHandle.iterator( );
			while ( bindingIter.hasNext( ) )
			{
				ParamBindingHandle bindingHandle = (ParamBindingHandle) bindingIter
						.next( );

				if ( paramName.equals( bindingHandle.getParamName( ) ) )
				{
					bindings.add( bindingHandle );
				}
			}

			paramBindingsPropHandle.removeItems( bindings );
		}

		/**
		 * Updates the parameter binding from old parameter name to new one.
		 * 
		 * @param oldParamName
		 *            old parameter name
		 * @param newParamName
		 *            new parameter name
		 */

		void updateParamBindings( String oldParamName, String newParamName )
		{
			Iterator iter = getElementHandle( ).clientsIterator( );
			while ( iter.hasNext( ) )
			{
				DesignElementHandle client = (DesignElementHandle) iter.next( );

				// Update parameter name in report items

				PropertyHandle paramBindingsPropHandle = client
						.getPropertyHandle( ReportItem.PARAM_BINDINGS_PROP );
				updateParamBindings( paramBindingsPropHandle, oldParamName,
						newParamName );

				// Update parameter name in action

				PropertyHandle actionPropHandle = client
						.getPropertyHandle( ImageItem.ACTION_PROP );
				if ( actionPropHandle != null )
				{
					Action action = (Action) actionPropHandle.getValue( );
					ActionHandle actionHandle = (ActionHandle) action
							.getHandle( actionPropHandle );
					MemberHandle paramBindingsMemberHandle = actionHandle
							.getMember( Action.PARAM_BINDINGS_MEMBER );
					updateParamBindings( paramBindingsMemberHandle,
							oldParamName, newParamName );
				}
			}

			// update the bindings with the given name from data set itself

			PropertyHandle paramBindingsPropHandle = getElementHandle( )
					.getPropertyHandle( PARAM_BINDINGS_PROP );
			updateParamBindings( paramBindingsPropHandle, oldParamName,
					newParamName );
		}

		/**
		 * Updates the parameter name in parameter binding.
		 * 
		 * @param paramBindingsPropHandle
		 *            the parameter binding to update
		 * @param oldParamName
		 *            old parameter name
		 * @param newParamName
		 *            new parameter name
		 */

		private static void updateParamBindings(
				SimpleValueHandle paramBindingsPropHandle, String oldParamName,
				String newParamName )
		{
			if ( paramBindingsPropHandle == null )
				return;

			Iterator bindingIter = paramBindingsPropHandle.iterator( );
			while ( bindingIter.hasNext( ) )
			{
				ParamBindingHandle bindingHandle = (ParamBindingHandle) bindingIter
						.next( );

				if ( oldParamName.equals( bindingHandle.getParamName( ) ) )
				{
					bindingHandle.setParamName( newParamName );
				}
			}

		}

	}

}
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

package org.eclipse.birt.report.model.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.activity.LayoutActivityTask;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * The utility class which provides many static methods used in Model.
 */

public class ModelUtil
{

	/**
	 * Clone the structure list, a list value contains a list of
	 * <code>IStructure</code>.
	 * 
	 * @param list
	 *            The structure list to be cloned.
	 * @return The cloned structure list.
	 */

	public static ArrayList cloneStructList( ArrayList list )
	{
		if ( list == null )
			return null;

		ArrayList returnList = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object item = list.get( i );
			if ( item instanceof IStructure )
			{
				returnList.add( ( (IStructure) item ).copy( ) );
			}
			else
			{
				assert false;
			}
		}
		return returnList;
	}

	/**
	 * Clones the value.
	 * <ul>
	 * <li>If the value is of simple type, like integer, or string, the
	 * original value will be returned.
	 * <li>If the value is strcuture list, the cloned structure list will be
	 * cloned.
	 * <li>If the value is structure, the cloned structure will be cloned.
	 * <li>If the value is element/strucuture reference value, the
	 * element/structure name will be returned.
	 * </ul>
	 * 
	 * @param propDefn
	 *            definition of property
	 * @param value
	 *            value to clone
	 * @return new value
	 */

	public static Object copyValue( IPropertyDefn propDefn, Object value )
	{

		if ( value == null )
			return null;

		switch ( propDefn.getTypeCode( ) )
		{
			case PropertyType.STRUCT_TYPE :

				if ( propDefn.isList( ) )
					return ModelUtil.cloneStructList( (ArrayList) value );

				return ( (Structure) value ).copy( );

			case PropertyType.ELEMENT_REF_TYPE :

				ElementRefValue refValue = (ElementRefValue) value;
				return new ElementRefValue( null, refValue.getName( ) );

			case PropertyType.STRUCT_REF_TYPE :

				StructRefValue structRefValue = (StructRefValue) value;
				return new StructRefValue( structRefValue.getName( ) );
		}

		return value;
	}

	/**
	 * Filtrates the notification tasks.
	 * 
	 * @param tasks
	 *            the notification tasks
	 * @return a list contained filtrated notification tasks
	 */

	public static List filterNotificationTasks( List tasks )
	{
		List notifications = new ArrayList( );
		for ( int i = 0; i < tasks.size( ); i++ )
		{
			RecordTask task = (RecordTask) tasks.get( i );
			if ( task instanceof NotificationRecordTask )
				notifications.add( task );
		}

		return EventFilter.getInstance( ).filter( notifications );
	}

	/**
	 * Filtrates the table layout tasks.
	 * 
	 * @param tasks
	 *            the table layout tasks
	 * @return a list contained filtrated table layout tasks
	 */

	public static List filterTableLayoutTasks( List tasks )
	{
		List retList = new ArrayList( );
		Set tables = new LinkedHashSet( );

		for ( int i = 0; i < tasks.size( ); i++ )
		{
			RecordTask task = (RecordTask) tasks.get( i );

			if ( task instanceof LayoutActivityTask )
			{
				DesignElement table = (DesignElement) ( (LayoutActivityTask) task )
						.getTarget( );
				if ( !tables.contains( table ) )
				{
					retList.add( task );
					tables.add( table );
				}
			}
		}

		return retList;
	}

	/**
	 * @param module
	 * @param systemId
	 * @param base
	 */

	// public static void setupSystemIdAndFileName( Module module, URI systemId,
	// String base )
	// {
	// String fullPath = null; //$NON-NLS-1$
	// String fileName = base;
	// if ( systemId != null )
	// fullPath = systemId.toString( );
	// if ( base != null )
	// {
	// int index = Math.max( base.lastIndexOf( '/' ), base
	// .lastIndexOf( '\\' ) );
	// if ( index > 0 )
	// {
	// fileName = base.substring( index + 1 );
	//
	// if ( fullPath == null )
	// fullPath = ""; //$NON-NLS-1$
	//
	// fullPath = fullPath + base.substring( 0, index );
	// }
	// }
	//
	// module.setFileName( fileName );
	//
	// if ( fullPath == null )
	// return;
	//
	// fullPath = fullPath.replace( '\\', '/' );
	//
	// try
	// {
	// module
	// .setSystemId( new URI( URLEncoder
	// .encode( fullPath, "UTF-8" ) ) ); //$NON-NLS-1$
	// }
	// catch ( UnsupportedEncodingException e )
	// {
	// assert false;
	// }
	// catch ( URISyntaxException e )
	//		{
	//			assert false;
	//		}
	//	}

	/**
	 * 
	 * Performs property name sorting on a list of properties. Properties
	 * returned are sorted by their (locale-specific) display name. The name for
	 * sorting is assumed to be "groupName.displayName" in which "groupName" is
	 * the localized name of the property group, if any; and "displayName" is
	 * the localized name of the property. That is, properties without groups
	 * sort by their property display names. Properties with groups sort first
	 * by group name within the overall list, then by property name within the
	 * group. Sorting in English ignores case.
	 * <p>
	 * For example, if we have the groups "G" and "R", and the properties
	 * "alpha", "G.beta", "G.sigma", "iota", "R.delta", "R.epsilon" and "theta",
	 * the Properties returned is assumed to be sorted into that order.
	 * 
	 * Sorts a list of <code>PropertyDefn</code> s by there localized name.
	 * Uses <code>Collator</code> to do the comparison, sorting in English
	 * ignores case.
	 * 
	 * @param propDefns
	 *            a list that contains PropertyDefns.
	 * @return the list of <code>PropertyDefn</code>s that is sorted by their
	 *         display name.
	 */

	public static List sortPropertiesByLocalizedName( List propDefns )
	{
		// Use the static factory method, getInstance, to obtain the appropriate
		// Collator object for the current
		// locale.

		// The Collator instance that performs locale-sensitive String
		// comparison.

		Locale locale = ThreadResources.getLocale( );
		Collator collator = Collator.getInstance( locale );

		// Sorting in English should ignore case.
		if ( Locale.ENGLISH.equals( locale ) )
		{

			// Set Collator strength value as PRIMARY, only PRIMARY differences
			// are considered significant during comparison. The assignment of
			// strengths to language features is locale defendant. A common
			// example is for different base letters ("a" vs "b") to be
			// considered a PRIMARY difference.

			collator.setStrength( Collator.PRIMARY );
		}

		final Map keysMap = new HashMap( );
		for ( int i = 0; i < propDefns.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) propDefns.get( i );

			// Transforms the String into a series of bits that can be compared
			// bitwise to other CollationKeys.
			// CollationKeys provide better performance than Collator.

			CollationKey key = collator.getCollationKey( propDefn
					.getDisplayName( ) );
			keysMap.put( propDefn, key );
		}

		Collections.sort( propDefns, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				PropertyDefn p1 = (PropertyDefn) o1;
				PropertyDefn p2 = (PropertyDefn) o2;

				CollationKey key1 = (CollationKey) keysMap.get( p1 );
				CollationKey key2 = (CollationKey) keysMap.get( p2 );

				// Comparing two CollationKeys returns the relative order of the
				// Strings they represent. Using CollationKeys to compare
				// Strings is generally faster than using Collator.compare.

				return key1.compareTo( key2 );
			}
		} );

		return propDefns;
	}

	/**
	 * Sorts a list of element by their internal names.
	 * 
	 * @param elements
	 *            a list of <code>DesignElementHandle</code>
	 * @return a sorted list of element.
	 */

	public static List sortElementsByName( List elements )
	{
		List temp = new ArrayList( elements );
		Collections.sort( temp, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				DesignElementHandle handle1 = (DesignElementHandle) o1;
				DesignElementHandle handle2 = (DesignElementHandle) o2;

				String name1 = handle1.getName( );
				String name2 = handle2.getName( );

				if ( null == name1 )
				{
					if ( null == name2 )
						return 0;

					return -1;
				}

				// name1 != null

				if ( null == name2 )
				{
					return 1;
				}

				return name1.compareTo( name2 );
			}

		} );

		return temp;
	}
}


package org.eclipse.birt.report.model.api.util;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Utility class to provide some methods about the style element, styled element
 * and style properties.
 * 
 */
public class StyleUtil
{

	/**
	 * Gets the design element handle with all the style properties are copied
	 * to the returned value. If <code>isCascaded</code> is set to TRUE, the
	 * copied style property value will be the cascaded value, otherwise will be
	 * the factory value.
	 * 
	 * @param source
	 * @param isCascaded
	 * @return
	 */
	public static DesignElementHandle copyStyles( DesignElementHandle source,
			DesignElementHandle target, boolean isCascaded )
	{
		return copyStyleProperties( source, target, isCascaded, false );
	}

	/**
	 * Gets the design element handle with all the style properties are copied
	 * to the returned value. The copied style property value will be the
	 * factory value and not cascaded.
	 * 
	 * @param source
	 * @return
	 */
	public static DesignElementHandle copyStyles( DesignElementHandle source,
			DesignElementHandle target )
	{
		return copyStyleProperties( source, target, false, false );
	}

	/**
	 * Gets the design element handle with all the style properties are copied
	 * to the returned value. If <code>isCascaded</code> is set to TRUE, the
	 * copied style property value will be the cascaded value, otherwise will be
	 * the factory value.
	 * 
	 * @param source
	 * @param isCascaded
	 * @return
	 */
	public static DesignElementHandle copyLocalStyles(
			DesignElementHandle source, DesignElementHandle target )
	{
		return copyStyleProperties( source, target, false, true );
	}

	/**
	 * Gets the design element handle with all the style properties are copied
	 * to the returned value. If <code>isCascaded</code> is set to TRUE, the
	 * copied style property value will be the cascaded value, otherwise will be
	 * the factory value.
	 * 
	 * @param source
	 * @param isCascaded
	 * @return
	 */
	private static DesignElementHandle copyStyleProperties(
			DesignElementHandle source, DesignElementHandle target,
			boolean isCascaded, boolean isLocal )
	{
		if ( source == null )
		{
			return null;
		}

		IElementDefn elementDefn = source.getDefn( );
		if ( target == null )
		{
			target = source.getElementFactory( ).newElement(
					elementDefn.getName( ), null );
		}

		// if the two elements are different types or same element, do nothing
		// and return directly
		if ( target.getDefn( ) != elementDefn || source == target )
		{
			return target;
		}

		// if this element can not define style properties, return directly
		if ( !elementDefn.hasStyle( ) )
		{
			return target;
		}

		DesignElement copiedElement = target.getElement( );
		DesignElement sourceElement = source.getElement( );
		Module module = source.getModule( );

		// handle style name
		ElementPropertyDefn propDefn = sourceElement
				.getPropertyDefn( IStyledElementModel.STYLE_PROP );
		copiedElement.setProperty( propDefn, sourceElement.getProperty( module,
				propDefn ) );

		// handle style properties
		IElementDefn styleDefn = MetaDataDictionary.getInstance( ).getStyle( );
		List<IElementPropertyDefn> styleProps = styleDefn.getProperties( );
		for ( int i = 0; i < styleProps.size( ); i++ )
		{
			propDefn = (ElementPropertyDefn) styleProps.get( i );
			if ( !propDefn.isStyleProperty( ) )
				continue;

			ElementPropertyDefn sourcePropDefn = sourceElement
					.getPropertyDefn( propDefn.getName( ) );
			ElementPropertyDefn targetPropDefn = copiedElement
					.getPropertyDefn( propDefn.getName( ) );

			if ( sourcePropDefn == null || targetPropDefn == null )
				continue;

			Object value = null;
			if ( isLocal )
			{
				value = sourceElement.getLocalProperty( module, sourcePropDefn );
			}
			else if ( isCascaded )
			{
				value = sourceElement.getProperty( module, sourcePropDefn );
			}
			else
			{
				value = sourceElement.getFactoryProperty( module,
						sourcePropDefn );
			}

			// set the value to the copied one
			if ( value != null )
			{
				copiedElement.setProperty( targetPropDefn, value );
			}
		}

		return target;
	}

	/**
	 * Adds selectors for extended elements to the report design. This action
	 * will be non-undoable, that is, once the selectors are inserted to the
	 * design handle, it will not be removed by undo action.
	 * 
	 * @param designHandle
	 */
	public static void addExtensionSelectors( ReportDesignHandle designHandle )
	{
		if ( designHandle == null )
			return;

		DesignSession.addExtensionDefaultStyles( (ReportDesign) designHandle
				.getModule( ), true );
	}

}

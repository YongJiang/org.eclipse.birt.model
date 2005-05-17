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


package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;


/**
 * Parses the ComputedColumn structure tag for compatibility.
 *
 * Provide back-compatibility for original "ColumnName" member for ComputedColumn, current version use "name" 
 * instead.
 *
 * The compatible version is 0 and 1.
 *  
 */

public class CompatibleComputedColumnStructureState extends StructureState
{
    CompatibleComputedColumnStructureState( DesignParserHandler theHandler,
            DesignElement element, PropertyDefn propDefn, ArrayList theList )
    {
        super( theHandler, element, propDefn, theList );
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
    
	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
            return new CompatibleComputedColumnPropertyState( handler, element, propDefn, struct );
		return super.startElement( tagName );
	}
    
    class CompatibleComputedColumnPropertyState extends PropertyState
    {
        CompatibleComputedColumnPropertyState( DesignParserHandler theHandler,
                DesignElement element, PropertyDefn propDefn, IStructure struct )
        {
            super( theHandler, element, propDefn, struct );
        }
        
		/* (non-Javadoc)
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		 
		public void end( ) throws SAXException
		{
            if ( ComputedColumn.COLUMN_NAME_MEMBER.equals( name ) )
            {
                String value = text.toString( );
                if ( StringUtil.isBlank( value ) )
                    return;

                assert ( struct != null );
                assert ( propDefn != null );

                setMember( struct, propDefn.getName( ),
                        ComputedColumn.NAME_MEMBER, value );
                return;
            }

            
			super.end( );
		}
        
        
    }
    
    
}

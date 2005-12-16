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

package org.eclipse.birt.report.model.writer;

import java.util.List;

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.DataSourceParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * Writes the design to an XML design file that follows the BIRT design schema.
 * Uses a visitor pattern to traverse each element. BIRT elements support
 * inheritance in several forms. Because of this, the design writer writes only
 * those properties "local" to the element being written -- it does not write
 * inherited properties.
 * <p>
 * Because the XML schema was designed for to be understood by humans, the
 * schema is not a literal representation of the model. Instead, properties are
 * named and grouped in a way that is easiest to explain and understand. This
 * means that the writer has to do a bit more work to write the design, the the
 * extra work here is well worth the savings to the many customers who will read
 * the design format.
 * 
 */

public class DesignWriter extends ModuleWriter
{

	/**
	 * The design context used to convert units.
	 */

	private ReportDesign design;

	/**
	 * Constructs a writer with the specified design.
	 * 
	 * @param design
	 *            the internal representation of the design
	 */

	public DesignWriter( ReportDesign design )
	{
		this.design = design;
	}

	/**
	 * Write the top-level Report tag, and the properties and contents of the
	 * report itself.
	 * 
	 * @param obj
	 *            the object to write
	 */

	public void visitReportDesign( ReportDesign obj )
	{
		writer.startElement( DesignSchemaConstants.REPORT_TAG );

		super.visitReportDesign( obj );

		property( obj, ReportDesign.REFRESH_RATE_PROP );
		property( obj, Module.INITIALIZE_METHOD );
		property( obj, ReportDesign.BEFORE_FACTORY_METHOD );
		property( obj, ReportDesign.AFTER_FACTORY_METHOD );
//		property( obj, ReportDesign.BEFORE_OPEN_DOC_METHOD );
//		property( obj, ReportDesign.AFTER_OPEN_DOC_METHOD );
//		property( obj, ReportDesign.BEFORE_CLOSE_DOC_METHOD );
//		property( obj, ReportDesign.AFTER_CLOSE_DOC_METHOD );
		property( obj, ReportDesign.BEFORE_RENDER_METHOD );
		property( obj, ReportDesign.AFTER_RENDER_METHOD );
		property( obj, IModuleModel.THEME_PROP );
		resourceKey( obj, DesignElement.DISPLAY_NAME_ID_PROP,
				DesignElement.DISPLAY_NAME_PROP );
		property( obj, ReportDesign.ICON_FILE_PROP );
		property( obj, ReportDesign.CHEET_SHEET_PROP );
		property( obj, ReportDesign.EVENT_HANDLER_CLASS_PROP );
		
		// include libraries and scripts

		writeStructureList( obj, ReportDesign.LIBRARIES_PROP );
		writeSimpleStructureList( obj, ReportDesign.INCLUDE_SCRIPTS_PROP,
				IncludeScript.FILE_NAME_MEMBER );

		// config variables

		writeStructureList( obj, ReportDesign.CONFIG_VARS_PROP );

		writeContents( obj, ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT,
				DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITIONS_TAG );
		writeArrangedContents( obj, ReportDesign.PARAMETER_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeArrangedContents( obj, ReportDesign.DATA_SOURCE_SLOT,
				DesignSchemaConstants.DATA_SOURCES_TAG );
		writeArrangedContents( obj, ReportDesign.DATA_SET_SLOT,
				DesignSchemaConstants.DATA_SETS_TAG );

		// ColorPalette tag

		writeCustomColors( obj );

		// Data bindings property
		
		writeDataSourceBinding( obj );

		// Translations. ( Custom-defined messages )

		writeTranslations( obj );

		writeContents( obj, IReportDesignModel.STYLE_SLOT,
				DesignSchemaConstants.STYLES_TAG );
		writeArrangedContents( obj, ReportDesign.COMPONENT_SLOT,
				DesignSchemaConstants.COMPONENTS_TAG );
		writeArrangedContents( obj, ReportDesign.PAGE_SLOT,
				DesignSchemaConstants.PAGE_SETUP_TAG );
		writeContents( obj, ReportDesign.BODY_SLOT,
				DesignSchemaConstants.BODY_TAG );
		writeContents( obj, ReportDesign.SCRATCH_PAD_SLOT,
				DesignSchemaConstants.SCRATCH_PAD_TAG );

		// Embedded images

		writeEmbeddedImages( obj );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.writer.ModuleWriter#getModule()
	 */

	protected Module getModule( )
	{
		return design;
	}

	/**
	 * Visits the data source parameter bindings of the module.
	 * 
	 * @param obj
	 *            the module to traverse
	 */

	protected void writeDataSourceBinding( Module obj )
	{
		List list = (List) obj.getLocalProperty( obj,
				ReportDesign.DATA_SOURCE_BINDINGS_PROP );

		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					ReportDesign.DATA_SOURCE_BINDINGS_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				DataSourceParamBinding dataSourceBinding = (DataSourceParamBinding) list
						.get( i );

				writer.conditionalStartElement( DesignSchemaConstants.STRUCTURE_TAG );
				
				property( dataSourceBinding,
						DataSourceParamBinding.DATASOURCE_NAME_MEMBER );

				writeStructureList( dataSourceBinding,
						DataSourceParamBinding.PARAMETER_BINDINGS_MEMBER );

				writer.endElement( );
			}
			
			writer.endElement( );
		}
	}
}
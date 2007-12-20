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

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class provides parser state for the top-level module element.
 */

public abstract class ModuleState extends DesignParseState
{

	/**
	 * 
	 */

	protected Module module = null;

	/**
	 * Constructs the module state with the module file parser handler.
	 * 
	 * @param theHandler
	 *            The module parser handler.
	 */

	public ModuleState( ModuleParserHandler theHandler )
	{
		super( theHandler );
		module = theHandler.getModule( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return module;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		String version = attrs.getValue( DesignSchemaConstants.VERSION_ATTRIB );
		module.getVersionManager( ).setVersion( version );

		if ( !StringUtil.isBlank( version ) )
		{
			int result;
			try
			{
				handler.versionNumber = VersionUtil.parseVersion( version );
				result = ( DesignSchemaConstants.REPORT_VERSION_NUMBER < handler.versionNumber
						? -1
						: ( DesignSchemaConstants.REPORT_VERSION_NUMBER == handler.versionNumber
								? 0
								: 1 ) );
			}
			catch ( NumberFormatException ex )
			{
				// The format of version string is invalid.

				DesignParserException e = new DesignParserException(
						new String[]{version},
						DesignParserException.DESIGN_EXCEPTION_INVALID_VERSION );
				throw new XMLParserException( e );
			}
			catch ( IllegalArgumentException ex )
			{
				// The format of version string is invalid.

				DesignParserException e = new DesignParserException(
						new String[]{version},
						DesignParserException.DESIGN_EXCEPTION_INVALID_VERSION );
				throw new XMLParserException( e );
			}

			if ( result < 0 )
			{
				DesignParserException e = new DesignParserException(
						new String[]{version},
						DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_VERSION );
				throw new XMLParserException( e );
			}

			if ( result == 0 )
				handler.isCurrentVersion = true;
		}

		super.parseAttrs( attrs );
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the Report
	 * tag.
	 */

	class InnerParseState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}
	}

	/**
	 * Parses the contents of the list of data sources.
	 */

	static class DataSourcesState extends SlotState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		protected DataSourcesState( ModuleParserHandler handler,
				DesignElement container, int slot )
		{
			super( handler, container, slot );
		}

		public AbstractParseState startElement( String tagName )
		{
			int tagValue = tagName.toLowerCase( ).hashCode( );
			if ( ParserSchemaConstants.SCRIPT_DATA_SOURCE_TAG == tagValue )
				return new ScriptDataSourceState( handler );
			if ( ParserSchemaConstants.ODA_DATA_SOURCE_TAG == tagValue
					|| ParserSchemaConstants.EXTENDED_DATA_SOURCE_TAG == tagValue )
			{
				return new OdaDataSourceState( handler );
			}
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the list of data sets.
	 */

	static class DataSetsState extends SlotState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		protected DataSetsState( ModuleParserHandler handler,
				DesignElement container, int slot )
		{
			super( handler, container, slot );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			int tagValue = tagName.toLowerCase( ).hashCode( );
			if ( ParserSchemaConstants.SCRIPT_DATA_SET_TAG == tagValue )
				return new ScriptDataSetState( handler );
			if ( ParserSchemaConstants.ODA_DATA_SET_TAG == tagValue
					|| ParserSchemaConstants.EXTENDED_DATA_SET_TAG == tagValue )
			{
				return new OdaDataSetState( handler );
			}
			if ( ParserSchemaConstants.TEMPLATE_DATA_SET_TAG == tagValue )
				return new TemplateDataSetState( handler );
			if ( ParserSchemaConstants.JOINT_DATA_SET_TAG == tagValue )
				return new JointDataSetState( handler );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parse the contents of translations.
	 */
	class TranslationsState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( DesignSchemaConstants.RESOURCE_TAG.equalsIgnoreCase( tagName ) )
				return new ResourceState( );

			return super.startElement( tagName );
		}
	}

	/**
	 * Parse one user-defined Message.
	 */
	class ResourceState extends InnerParseState
	{

		private String key = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			key = attrs.getValue( DesignSchemaConstants.KEY_ATTRIB );

			if ( StringUtil.isEmpty( key ) )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
										DesignParserException.DESIGN_EXCEPTION_MESSAGE_KEY_REQUIRED ) );
				return;
			}

			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( DesignSchemaConstants.TRANSLATION_TAG
					.equalsIgnoreCase( tagName ) )
				return new TranslationState( key );

			return super.startElement( tagName );
		}
	}

	/**
	 * Parse one entry for the user-defined Message.
	 */
	class TranslationState extends InnerParseState
	{

		private String resourceKey = null;

		private String locale = null;

		TranslationState( String key )
		{
			this.resourceKey = key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			locale = attrs.getValue( DesignSchemaConstants.LOCALE_ATTRIB );
			locale = StringUtil.trimString( locale );

			// TODO: text format of the locale should be checked.
			// TODO: Should we define a ChoiceSet for the supported locales?
			// Also see ReportDesign.locale

			if ( module.findTranslation( resourceKey, locale ) != null )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
										DesignParserException.DESIGN_EXCEPTION_DUPLICATE_TRANSLATION_LOCALE ) );
				return;
			}

			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			module.addTranslation( new Translation( resourceKey, locale, text
					.toString( ) ) );
			super.end( );
		}
	}

	/**
	 * Parses the contents of the page setup tag.
	 */

	static class PageSetupState extends SlotState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		protected PageSetupState( ModuleParserHandler handler,
				DesignElement container, int slot )
		{
			super( handler, container, slot );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			int tagValue = tagName.toLowerCase( ).hashCode( );
			if ( ParserSchemaConstants.GRAPHIC_MASTER_PAGE_TAG == tagValue )
				return new GraphicMasterPageState( handler );
			if ( ParserSchemaConstants.SIMPLE_MASTER_PAGE_TAG == tagValue )
				return new SimpleMasterPageState( handler );
			if ( ParserSchemaConstants.PAGE_SEQUENCE_TAG == tagValue )
				return new AnyElementState( handler );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the components tag that contains the list of
	 * shared elements, which can be derived from.
	 */

	static class ComponentsState extends SlotState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		protected ComponentsState( ModuleParserHandler handler,
				DesignElement container, int slot )
		{
			super( handler, container, slot );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			int tagValue = tagName.toLowerCase( ).hashCode( );

			if ( ParserSchemaConstants.BROWSER_CONTROL_TAG == tagValue )
				return new AnyElementState( handler );
			if ( ParserSchemaConstants.FREE_FORM_TAG == tagValue )
				return new FreeFormState( handler, container, slotID );
			if ( ParserSchemaConstants.DATA_TAG == tagValue )
				return new DataItemState( handler, container, slotID );
			if ( ParserSchemaConstants.EXTENDED_ITEM_TAG == tagValue )
				return new ExtendedItemState( handler, container, slotID );
			if ( ParserSchemaConstants.GRID_TAG == tagValue )
				return new GridItemState( handler, container, slotID );
			if ( ParserSchemaConstants.IMAGE_TAG == tagValue )
				return new ImageState( handler, container, slotID );
			if ( ParserSchemaConstants.INCLUDE_TAG == tagValue )
				return new AnyElementState( handler );
			if ( ParserSchemaConstants.LABEL_TAG == tagValue )
				return new LabelState( handler, container, slotID );
			if ( ParserSchemaConstants.TEXT_TAG == tagValue )
				return new TextItemState( handler, container, slotID );
			if ( ParserSchemaConstants.LINE_TAG == tagValue )
				return new LineItemState( handler, container, slotID );
			if ( ParserSchemaConstants.LIST_TAG == tagValue )
				return new ListItemState( handler, container, slotID );
			if ( ParserSchemaConstants.RECTANGLE_TAG == tagValue )
				return new RectangleState( handler, container, slotID );
			if ( ParserSchemaConstants.TABLE_TAG == tagValue )
				return new TableItemState( handler, container, slotID );
			if ( ParserSchemaConstants.TEXT_TAG == tagValue )
				return new TextItemState( handler, container, slotID );
			if ( ParserSchemaConstants.TOC_TAG == tagValue )
				return new AnyElementState( handler );
			if ( ParserSchemaConstants.MULTI_LINE_DATA_TAG == tagValue
					|| ParserSchemaConstants.TEXT_DATA_TAG == tagValue )
				return new TextDataItemState( handler, container, slotID );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the list of styles.
	 */

	static class CubesState extends SlotState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		protected CubesState( ModuleParserHandler handler,
				DesignElement container, int slot )
		{
			super( handler, container, slot );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.TABULAR_CUBE_TAG ) )
				return new TabularCubeState( handler, container, slotID );

			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.ODA_CUBE_TAG ) )
				return new OdaCubeState( handler, container, slotID );

			return super.startElement( tagName );
		}

	}

}

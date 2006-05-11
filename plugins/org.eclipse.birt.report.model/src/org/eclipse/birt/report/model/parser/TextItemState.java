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

import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.template.TemplateParser;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the text item.
 * 
 */

public class TextItemState extends ReportItemState
{

	/**
	 * The text item being created.
	 */

	protected TextItem element;

	/**
	 * Constructs the text item state with the design parser handler, the
	 * container element and the container slot of the text item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public TextItemState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new TextItem( );
		initElement( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		super.end( );

		if ( StringUtil.compareVersion( handler.getVersion( ), "3.2.1" ) >= 0 ) //$NON-NLS-1$
			return;

		String content = (String) element.getLocalProperty(
				handler.getModule( ), TextItem.CONTENT_PROP );
		if ( StringUtil.isBlank( content ) )
			return;

		List jsExprs = DataBoundColumnUtil.getExpressions( content, element,
				handler.getModule( ) );
		updateExpressions( content, DataBoundColumnUtil.handleJavaExpression(
				jsExprs, element, handler.getModule( ) ) );
	}

	/**
	 * Updated the given content with the input updated expressions.
	 * 
	 * @param contentText
	 *            the content text
	 * @param updatedExprs
	 *            a mapping containing updated expressions. The keys are existed
	 *            expressions, while, the values are the new expressions to
	 *            replace existed ones.
	 */

	private void updateExpressions( String contentText, Map updatedExprs )
	{
		if ( updatedExprs.isEmpty( ) )
			return;

		String contentType = (String) element.getProperty(
				handler.getModule( ), TextItem.CONTENT_TYPE_PROP );

		if ( !( DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO
				.equalsIgnoreCase( contentType ) || ( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML
				.equalsIgnoreCase( contentType ) ) ) )
			return;

		TextTemplate template = new TemplateParser( ).parse( contentText );

		DataBoundColumnUtil.ContentVisitor templateVisitor = new DataBoundColumnUtil.ContentVisitor(
				template, updatedExprs );

		String content = templateVisitor.execute( );

		// reset the content.

		element.setProperty( TextItem.CONTENT_PROP, content );
	}

}

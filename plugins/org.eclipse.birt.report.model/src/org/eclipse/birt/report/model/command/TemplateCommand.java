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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.namespace.IModuleNameSpace;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TemplateDataSet;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateFactory;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * This class replaces a template element with a report item or data set,
 * replace a report item or data set with a template element and do some checks
 * about the replacements.
 */

public class TemplateCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param obj
	 *            the element to modify.
	 */

	public TemplateCommand( Module module, DesignElement obj )
	{
		super( module, obj );
	}

	/**
	 * Checks the <code>REF_TEMPLATE_PARAMETER_PROP</code> of template
	 * elements to avoid that tit refers a non-exsiting template parameter
	 * definition or a wrong type definition.
	 * 
	 * @param prop
	 *            the definition of property
	 *            <code>REF_TEMPLATE_PARAMETER_PROP</code> in template
	 *            elements
	 * @param value
	 *            the new value to set
	 * @throws PropertyValueException
	 *             throws exception if it refers non-exsiting template parameter
	 *             definition or a wrong type definition
	 */

	public void checkProperty( ElementPropertyDefn prop, Object value )
			throws PropertyValueException
	{
		if ( value == null )
			return;

		// element is a template element, the referred template definition can
		// not be set to a non-exsiting parameter definition or a wrong type
		// definition

		if ( ( element instanceof ReportItem || element instanceof TemplateReportItem )
				&& DesignElement.REF_TEMPLATE_PARAMETER_PROP.equals( prop
						.getName( ) ) )
		{
			assert value instanceof ElementRefValue;
			if ( ( (ElementRefValue) value ).getElement( ) == null )
			{
				ElementRefValue refValue = resolveTemplateParameterDefinition(
						module, ( (ElementRefValue) value ).getName( ) );
				TemplateParameterDefinition templateParam = (TemplateParameterDefinition) refValue
						.getElement( );
				if ( !( templateParam.getDefaultElement( ) instanceof ReportItem ) )
					throw new PropertyValueException(
							element,
							prop.getName( ),
							value,
							PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE );
			}
		}

		if ( ( element instanceof DataSet || element instanceof TemplateDataSet )
				&& DesignElement.REF_TEMPLATE_PARAMETER_PROP.equals( prop
						.getName( ) ) )
		{
			assert value instanceof ElementRefValue;
			if ( ( (ElementRefValue) value ).getElement( ) == null )
			{
				ElementRefValue refValue = resolveTemplateParameterDefinition(
						module, ( (ElementRefValue) value ).getName( ) );
				TemplateParameterDefinition templateParam = (TemplateParameterDefinition) refValue
						.getElement( );
				if ( !( templateParam.getDefaultElement( ) instanceof DataSet ) )
					throw new PropertyValueException(
							element,
							prop.getName( ),
							value,
							PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE );
			}
		}
	}

	/**
	 * Resolve the property value of <code>REF_TEMPLATE_PARAMETER_PROP</code>
	 * in a template element.
	 * 
	 * @param module
	 *            the module of the template element
	 * @param name
	 *            the name to resolve
	 * @return the element reference value
	 */

	private ElementRefValue resolveTemplateParameterDefinition( Module module,
			String name )
	{
		PropertyDefn prop = element
				.getPropertyDefn( DesignElement.REF_TEMPLATE_PARAMETER_PROP );
		if ( prop == null )
			return null;
		ElementDefn targetDefn = (ElementDefn) prop.getTargetElementType( );
		IModuleNameSpace elementResolver = module
				.getModuleNameSpace( targetDefn.getNameSpaceID( ) );
		ElementRefValue refValue = elementResolver.resolve( name );
		return refValue;
	}

	/**
	 * Checks the validation of the template element to insert. Verify that the
	 * template element to insert refers a valid and exsiting template parameter
	 * definition. If the referred the template definition doesn't exsit, then
	 * return the template parameter definition to add it to the module.
	 * 
	 * @param content
	 * @param slotID
	 * @return a template parameter definition to add, otherwise null
	 * @throws ContentException
	 *             if the value of property
	 *             <code>REF_TEMPLATE_PARAMETER_PROP</code> in template
	 *             element is null, or an un-resolved value
	 */

	public TemplateParameterDefinition checkAdd( DesignElement content,
			int slotID ) throws ContentException
	{
		// if this element is a template element, check the template parameter
		// definition and add it first if the definition is not defined in the

		if ( content instanceof TemplateElement )
		{
			ElementRefValue templateParam = (ElementRefValue) content
					.getProperty( module,
							DesignElement.REF_TEMPLATE_PARAMETER_PROP );
			if ( templateParam == null )
			{
				throw new ContentException(
						element,
						slotID,
						content,
						ContentException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT );
			}
			else if ( templateParam.getElement( ) == null )
			{
				// try to resolve the unresolved value and finally the referred
				// template definition does not exsit in the module, fire an
				// error

				ElementRefValue refValue = resolveTemplateParameterDefinition(
						module, templateParam.getName( ) );
				assert refValue != null;
				if ( refValue.getElement( ) == null )
				{
					throw new ContentException(
							element,
							slotID,
							content,
							ContentException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT );
				}
			}
			else
			{
				// the template parameter reference is resolved and the
				// definition is not inserted to the module, then clone it and
				// add the cloned definition into module

				TemplateParameterDefinition definition = (TemplateParameterDefinition) templateParam
						.getElement( );
				if ( module.findTemplateParameterDefinition( templateParam
						.getName( ) ) != definition )
				{
					try
					{
						definition = (TemplateParameterDefinition) definition
								.clone( );
					}
					catch ( CloneNotSupportedException e )
					{
						assert false;
					}
					module.makeUniqueName( definition );
					return definition;
				}

			}
		}

		return null;
	}

	/**
	 * Creates a template element and replace the content element with this
	 * created template element. In this method, create a template definition
	 * based on the given content element and add it to the report design first.
	 * Then, let the created template element refer the added template parameter
	 * definition.
	 * 
	 * @param base
	 *            the base report item or data set element to be transformed to
	 *            a template element
	 * @param slotID
	 *            the slot of the container
	 * @param name
	 *            the given name of the created template element
	 * @return the created template element
	 * @throws SemanticException
	 *             if the content can not be transformed to a template element,
	 *             current module is not a repor design and can not support
	 *             template elements, or the replacement fails
	 */

	public TemplateElement createTemplateElement( DesignElement base,
			int slotID, String name ) throws SemanticException
	{
		assert base != null;

		TemplateElement template = TemplateFactory.createTemplate( module,
				base, name );

		// if content element is not a report
		// item or data set, then the operarion is forbidden

		if ( template == null )
			throw new TemplateException(
					base,
					TemplateException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE );
		if ( !( module instanceof ReportDesign ) )
			throw new TemplateException(
					module,
					TemplateException.DESIGN_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED );

		ActivityStack stack = getActivityStack( );
		stack.startTrans( );

		try
		{
			createTemplateFromDesignElement( template, base );

			ContentCommand cmd = new ContentCommand( module, element );
			cmd.transformTemplate( base, template, slotID, true );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );

		return template;

	}

	/**
	 * Creates a template definition based on the "base" element, add it to the
	 * report design and let the given template element handle refer the
	 * template definition.
	 * 
	 * @param template
	 *            the template element
	 * @param base
	 *            the base element to create a template parameter definition
	 */

	private void createTemplateFromDesignElement( TemplateElement template,
			DesignElement base )
	{
		assert base instanceof ReportItem || base instanceof DataSet;
		assert template != null;
		assert module instanceof ReportDesign;

		// create a template parameter definition element and make a unique name
		// for it

		try
		{
			TemplateParameterDefinition templateParam = new TemplateParameterDefinition( );
			module.makeUniqueName( templateParam );

			// get the handle to do the next operations and add it to the module

			PropertyCommand propertyCmd = new PropertyCommand( module,
					templateParam );
			if ( base instanceof DataSet )
				propertyCmd.setProperty(
						TemplateParameterDefinition.ALLOWED_TYPE_PROP,
						DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_DATA_SET );
			else
				propertyCmd.setProperty(
						TemplateParameterDefinition.ALLOWED_TYPE_PROP, base
								.getElementName( ) );

			ContainerSlot defaultSlot = templateParam
					.getSlot( TemplateParameterDefinition.DEFAULT_SLOT );
			assert defaultSlot != null;

			// clone the base element and add it to the default slot

			DesignElement defaultElement = null;

			defaultElement = (DesignElement) base.clone( );

			assert defaultElement != null;

			ContentCommand contentCmd = new ContentCommand( module,
					templateParam );
			contentCmd.add( defaultElement,
					TemplateParameterDefinition.DEFAULT_SLOT );

			// let the template handle refer the template parameter definition

			contentCmd = new ContentCommand( module, module );
			contentCmd.add( templateParam,
					ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT );

			propertyCmd = new PropertyCommand( module, template );
			propertyCmd.setProperty(
					TemplateElement.REF_TEMPLATE_PARAMETER_PROP, templateParam
							.getName( ) );
		}
		catch ( ContentException e )
		{
			assert false;
		}
		catch ( NameException e )
		{
			assert false;
		}
		catch ( SemanticException e )
		{
			assert false;
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
		}

	}

	/**
	 * Transforms the given template report item to a report item with the given
	 * real report item.
	 * 
	 * @param templateItem
	 *            the template report item to be transformed
	 * @param reportItem
	 *            the real report item to transform
	 * @param slotID
	 *            the slot ID where the transformation occurs
	 * @throws SemanticException
	 */

	public void transformToReportItem( TemplateReportItem templateItem,
			ReportItem reportItem, int slotID ) throws SemanticException
	{
		// if the template report item has no template definition, it can not be
		// transformed to a report item

		TemplateParameterDefinition templateparam = templateItem
				.getTemplateParameterElement( module );
		if ( templateparam == null )
			throw new TemplateException(
					templateItem,
					TemplateException.DESIGN_EXCEPTION_TRANSFORM_TO_REPORT_ITEM_FORBIDDEN );

		ActivityStack stack = getActivityStack( );

		stack.startTrans( );

		try
		{
			PropertyCommand pcmd = new PropertyCommand( module, reportItem );
			pcmd.setProperty( ReportItem.REF_TEMPLATE_PARAMETER_PROP,
					templateparam.getName( ) );

			ContentCommand cmd = new ContentCommand( module, element );
			cmd.transformTemplate( templateItem, reportItem, slotID, false );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );

	}

	/**
	 * Transforms the given template data set to a data set with the given real
	 * data set.
	 * 
	 * @param templateDataSet
	 *            the template data set to be transformed
	 * @param dataSet
	 *            the real data set to transform
	 * @param slotID
	 *            the slot ID where the transformation occurs
	 * @throws SemanticException
	 */

	public void transformToDataSet( TemplateDataSet templateDataSet,
			DataSet dataSet, int slotID ) throws SemanticException
	{
		// if the template data set has no template definition, it can not be
		// transformed to a data set

		TemplateParameterDefinition templateparam = templateDataSet
				.getTemplateParameterElement( module );
		if ( templateparam == null )
			throw new TemplateException(
					templateDataSet,
					TemplateException.DESIGN_EXCEPTION_TRANSFORM_TO_DATA_SET_FORBIDDEN );

		ActivityStack stack = getActivityStack( );

		stack.startTrans( );

		try
		{
			PropertyCommand pcmd = new PropertyCommand( module, dataSet );
			pcmd.setProperty( DataSet.REF_TEMPLATE_PARAMETER_PROP,
					templateparam.getName( ) );

			ContentCommand cmd = new ContentCommand( module, element );
			cmd.transformTemplate( templateDataSet, dataSet, slotID, false );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );

	}

	/**
	 * Reverts a report item or data set to a template element. In this method,
	 * create a template element and let the created template element refer the
	 * template parameter definition of the base element.
	 * 
	 * @param base
	 *            the base report item or data set element to be reverted to a
	 *            template element
	 * @param slotID
	 *            the slot of the container
	 * @param name
	 *            the given name of the created template element
	 * @return the created template element
	 * @throws SemanticException
	 *             if the content can not be revert to a template element,
	 *             current module is not a repor design and can not support
	 *             template elements, base element has no template definition,
	 *             or the replacement fails
	 */

	public TemplateElement revertToTemplate( DesignElement base, int slotID,
			String name ) throws SemanticException
	{
		assert base != null;

		TemplateElement template = TemplateFactory.createTemplate( module,
				base, name );

		// if content element is not a report
		// item or data set, then the operarion is forbidden

		if ( template == null )
			throw new TemplateException(
					base,
					TemplateException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE );
		if ( !( module instanceof ReportDesign ) )
			throw new TemplateException(
					module,
					TemplateException.DESIGN_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED );

		// if the design element has no template definition, it can not be
		// reverted.

		TemplateParameterDefinition templateParam = base
				.getTemplateParameterElement( module );
		if ( templateParam == null )
			throw new TemplateException(
					base,
					TemplateException.DESIGN_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN );
		try
		{
			PropertyCommand propertyCmd = new PropertyCommand( module, template );
			propertyCmd.setProperty(
					TemplateElement.REF_TEMPLATE_PARAMETER_PROP, templateParam
							.getName( ) );
		}
		catch ( SemanticException e )
		{
			assert false;
		}

		ContentCommand cmd = new ContentCommand( module, element );
		cmd.transformTemplate( base, template, slotID, false );

		return template;
	}

}

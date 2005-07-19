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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.MasterPageRequiredValidator;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.RootElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.validators.ValidationExecutor;

/**
 * This class represents the root element in the report design hierarchy.
 * Contains the list of data sets, data sources, master pages, components, body
 * content, scratch pad and more. Code modules in the report gives
 * specifications for global scripts that apply to the report as a whole.Report
 * design is valid if it is opened without error or with semantic error.
 * Otherwise, it's invalid.
 *  
 */

public class ReportDesign extends RootElement implements IReportDesignModel
{

	/**
	 * The set of slots for the design.
	 */

	protected ContainerSlot slots[] = null;

	/**
	 * The file name. Null means that the design has not yet been saved to a
	 * file.
	 */

	protected String fileName = null;

	/**
	 * The UTF signature.
	 */

	protected String signature = null;

	/**
	 * The default units for the design.
	 */

	protected String units = null;

	/**
	 * Accumulates errors and warnings during a batch operation.
	 */

	private List allErrors = new ArrayList( );

	/**
	 * Internal table to store a bunch of user-defined messages. One message can
	 * be defined in several translations, one translation per locale.
	 */

	private TranslationTable translations = new TranslationTable( );

	/**
	 * The validation executor. It performs the semantic validation and sends
	 * validation event to listeners.
	 */

	private ValidationExecutor validationExecutor = new ValidationExecutor(
			this );

	private List validationListeners = null;

	/**
	 * The property definition list of all the referencable structure list
	 * property. Each one in the list is instance of <code>IPropertyDefn</code>
	 */

	private List referencableProperties = null;

	/**
	 * Default constructor.
	 * 
	 * @deprecated
	 */

	public ReportDesign( )
	{
		super( null );
		initSlots( );
		onCreate( );
	}

	/**
	 * Constructs the report design with the session.
	 * 
	 * @param session
	 *            the session that owns this design
	 */

	public ReportDesign( DesignSession session )
	{
		super( session );
		initSlots( );
		onCreate( );
	}

	/**
	 * Privates method to create the slots.
	 */

	private void initSlots( )
	{
		slots = new ContainerSlot[SLOT_COUNT];
		for ( int i = 0; i < slots.length; i++ )
			slots[i] = new MultiElementSlot( );
	}

	/**
	 * Makes a clone of this root element. The error list, file name are set to
	 * empty. A new ID map was generated for the new cloned element.
	 * 
	 * @return Object the cloned report design element.
	 * 
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		ReportDesign design = (ReportDesign) super.clone( );
		design.allErrors = new ArrayList( );
		design.initSlots( );
		for ( int i = 0; i < slots.length; i++ )
		{
			design.slots[i] = slots[i].copy( design, i );
		}
		design.translations = (TranslationTable) translations.clone( );
		design.fileName = null;

		NameSpace.rebuildNamespace( design );

		return design;
	}

	/**
	 * Generate new ID map for the cloned report design.
	 * 
	 * @param design
	 *            the new design
	 * @param element
	 *            the element is traversed on
	 */
	private void generateIdMap( ReportDesign design, DesignElement element )
	{

		if ( element == null )
			return;

		IElementDefn defn = element.getDefn( );
		int id = design.getNextID( );
		element.setID( id );
		design.addElementID( element );
		// design.idMap.put( new Integer(id), element);

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = element.getSlot( i );

			if ( slot == null )
				continue;

			for ( int pos = 0; pos < slot.getCount( ); pos++ )
			{
				DesignElement innerElement = slot.getContent( pos );
				generateIdMap( design, innerElement );
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot >= 0 && slot < SLOT_COUNT;
		return slots[slot];
	}

	/**
	 * Finds a data source by name.
	 * 
	 * @param name
	 *            the name of the data source to find.
	 * @return the data source, or null if the data source is not found.
	 */

	public DesignElement findDataSource( String name )
	{
		return nameSpaces[DATA_SOURCE_NAME_SPACE].getElement( name );
	}

	/**
	 * Finds a data set by name.
	 * 
	 * @param name
	 *            the name of the data set to find.
	 * @return the data set, or null if the data set is not found.
	 */

	public DesignElement findDataSet( String name )
	{
		return nameSpaces[DATA_SET_NAME_SPACE].getElement( name );
	}

	/**
	 * Finds a master page by name.
	 * 
	 * @param name
	 *            the master page name.
	 * @return the master page, if found, otherwise null.
	 */

	public DesignElement findPage( String name )
	{
		return nameSpaces[PAGE_NAME_SPACE].getElement( name );
	}

	/**
	 * Builds the default elements for a new design. The elements include the
	 * standard styles. The elements are build directly, without the use of
	 * commands or the command stack. The creation operation cannot be undone.
	 * This operation must be done before the first activity stack operation.
	 */

	protected void onCreate( )
	{
		super.onCreate( );

		// Pass the validation executor to activity stack.

		activityStack.setValidationExecutor( validationExecutor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitReportDesign( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// Must there is more than one master page in setup page

		list.addAll( MasterPageRequiredValidator.getInstance( ).validate( this,
				this ) );

		list.addAll( validateStructureList( design, IMAGES_PROP ) );
		list.addAll( validateStructureList( design, COLOR_PALETTE_PROP ) );

		list.addAll( validateStructureList( design, INCLUDE_SCRIPTS ) );
		list.addAll( validateStructureList( design, INCLUDE_LIBRARIES ) );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.REPORT_DESIGN_ELEMENT;
	}

	/**
	 * Returns the file name of the design file.
	 * 
	 * @return the design file name. Returns null if the design has not yet been
	 *         saved to a file.
	 */

	public String getFileName( )
	{
		return fileName;
	}

	/**
	 * Sets the design file name. This method is only called by design reader,
	 * it's illegal to be called for other purpose.
	 * 
	 * @param newName
	 *            the new file name
	 */

	public void setFileName( String newName )
	{
		fileName = newName;
	}

	/**
	 * Sets the UTF signature of this design file.
	 * 
	 * @param signature
	 *            the UTF signature of the design file.
	 */

	public void setUTFSignature( String signature )
	{
		this.signature = signature;
	}

	/**
	 * Gets the UTF signature of this design file.
	 * 
	 * @return the UTF signature of the design file.
	 */

	public String getUTFSignature( )
	{
		return signature;
	}

	/**
	 * Adds a new Translation entry to the design. A report file can reference
	 * message IDs that are defined by the customers. One entry of
	 * <code>Translation</code> represents a translated message for a specific
	 * locale.
	 * <p>
	 * 
	 * @param translation
	 *            new entry of <code>Translation</code> that are to be added
	 *            to the design.
	 */

	public void addTranslation( Translation translation )
	{
		translations.add( translation );
	}

	/**
	 * Drops a Translation from the design.
	 * <p>
	 * 
	 * @param translation
	 *            the translation to be dropped from the design.
	 * 
	 * @return <code>true</code> if the report design contains the given
	 *         translation.
	 */

	public boolean dropTranslation( Translation translation )
	{
		return translations.remove( translation );
	}

	/**
	 * Finds a <code>Translation</code> by the message resource key and the
	 * locale.
	 * <p>
	 * 
	 * @param resourceKey
	 *            resourceKey of the user-defined message where the translation
	 *            is defined in.
	 * @param locale
	 *            locale for the translation. Locale is in java-defined format(
	 *            en, en-US, zh_CN, etc.)
	 * @return the <code>Translation</code> that matches. return null if the
	 *         translation is not found in the report.
	 */

	public Translation findTranslation( String resourceKey, String locale )
	{
		return translations.findTranslation( resourceKey, locale );
	}

	/**
	 * Returns if the specified translation is contained in the translation
	 * table.
	 * 
	 * @param trans
	 *            a given <code>Translation</code>
	 * @return <code>true</code> if the <code>Translation</code> is
	 *         contained in the translation talbe, return <code>false</code>
	 *         otherwise.
	 */

	public boolean contains( Translation trans )
	{
		return translations.contains( trans );
	}

	/**
	 * Returns the whole collection of translations defined for the report
	 * design.
	 * <p>
	 * 
	 * @return a list containing all the Translations.
	 */

	public List getTranslations( )
	{
		return translations.getTranslations( );
	}

	/**
	 * Returns the collection of translations defined for a specific message.
	 * The message is presented by its resourceKey.
	 * <p>
	 * 
	 * @param resourceKey
	 *            resource key for the message.
	 * @return a list containing all the Translations defined for the message.
	 */

	public List getTranslations( String resourceKey )
	{
		return translations.getTranslations( resourceKey );
	}

	/**
	 * Return a list of user-defined message keys. The list contained resource
	 * keys defined in the report itself and the keys defined in the referenced
	 * message files for the current thread's locale. The list returned contains
	 * no duplicate keys.
	 * 
	 * @return a list of user-defined message keys.
	 */

	public List getMessageKeys( )
	{
		Set keys = new LinkedHashSet( );

		String[] transKeys = translations.getResourceKeys( );
		if ( transKeys != null )
		{
			for ( int i = 0; i < transKeys.length; i++ )
				keys.add( transKeys[i] );
		}

		// find from the referenced message files.
		// e.g: message

		String baseName = getStringProperty( this, INCLUDE_RESOURCE_PROP );
		if ( baseName == null )
			return new ArrayList( keys );

		File msgFolder = getDesignFolder( );
		if ( msgFolder == null )
			return new ArrayList( keys );

		Collection msgKeys = BundleHelper.getHelper( msgFolder, baseName )
				.getMessageKeys( ThreadResources.getLocale( ) );
		keys.addAll( msgKeys );

		return new ArrayList( keys );
	}

	/**
	 * Finds user defined messages for the current thread's locale.
	 * 
	 * @param resourceKey
	 *            Resource key of the user defined message.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>""</code> if resoueceKey is blank or the message is not
	 *         found.
	 * @see #getMessage(String, Locale)
	 */

	public String getMessage( String resourceKey )
	{
		return getMessage( resourceKey, ThreadResources.getLocale( ) );
	}

	/**
	 * Finds user-defined messages for the given locale.
	 * <p>
	 * First we look up in the report itself, then look into the referenced
	 * message file. Each search uses a reduced form of Java locale-driven
	 * search algorithm: Language&Country, language, default.
	 * 
	 * @param resourceKey
	 *            Resource key of the user defined message.
	 * @param locale
	 *            locale of message, if the input <code>locale</code> is
	 *            <code>null</code>, the locale for the current thread will
	 *            be used instead.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>""</code> if translation can not be found, or
	 *         <code>resourceKey</code> is blank or <code>null</code>.
	 */

	public String getMessage( String resourceKey, Locale locale )
	{
		if ( StringUtil.isBlank( resourceKey ) )
			return ""; //$NON-NLS-1$

		if ( locale == null )
			locale = ThreadResources.getLocale( );

		// find it in the design itself.

		String msg = translations.getMessage( resourceKey, locale );
		if ( msg != null )
			return msg;

		// find it in the linked resource file.

		String baseName = getStringProperty( this, INCLUDE_RESOURCE_PROP );
		if ( baseName == null )
			return null;

		File msgFolder = getDesignFolder( );
		if ( msgFolder == null )
			return ""; //$NON-NLS-1$

		return BundleHelper.getHelper( msgFolder, baseName ).getMessage(
				resourceKey, locale );
	}

	/**
	 * Return the folder in which the design file is located. The search depend
	 * on the {@link #getFileName()}.
	 * 
	 * @return the folder in which the design file is located. Return
	 *         <code>null</code> if the folder can not be found.
	 */

	private File getDesignFolder( )
	{
		String designPath = getFileName( );
		File designFile = new File( designPath );
		if ( !designFile.exists( ) )
			return null;

		if ( designFile.isFile( ) )
			return designFile.getParentFile( );

		return null;
	}

	/**
	 * Returns a string array containing all the resource keys defined for
	 * messages.
	 * <p>
	 * 
	 * @return a string array containing all the resource keys defined for
	 *         messages return null if there is no messages stored.
	 */

	public String[] getTranslationResourceKeys( )
	{
		return translations.getResourceKeys( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @return an API handle for this element
	 */

	public ReportDesignHandle handle( )
	{
		if ( handle == null )
		{
			handle = new ReportDesignHandle( this );
		}
		return (ReportDesignHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( propName.equals( UNITS_PROP ) )
		{
			return units;
		}
		return super.getIntrinsicProperty( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( propName.equals( UNITS_PROP ) )
			units = (String) value;
		else
			super.setIntrinsicProperty( propName, value );
	}

	/**
	 * Gets the default units for the design.
	 * 
	 * @return the default units used in the design
	 */

	public String getUnits( )
	{
		if ( !StringUtil.isBlank( units ) )
			return units;
		return (String) getPropertyDefn( ReportDesign.UNITS_PROP ).getDefault( );
	}

	/**
	 * Finds a custom color by name.
	 * 
	 * @param colorName
	 *            the custom color name
	 * @return the custom defined color that matches, or <code>null</code> if
	 *         the color name was not found in the custom color palette.
	 */

	public CustomColor findColor( String colorName )
	{
		ArrayList list = (ArrayList) getLocalProperty( null, COLOR_PALETTE_PROP );
		if ( list == null )
			return null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			CustomColor color = (CustomColor) list.get( i );
			if ( color.getName( ).equals( colorName ) )
				return color;
		}

		return null;
	}

	/**
	 * Gets a list containing all the user-defined colors.
	 * 
	 * @return a list containing all the user-defined colors. Return
	 *         <code>null</code> if there were no colors defined.
	 */

	public List getColors( )
	{
		return (ArrayList) getLocalProperty( null, COLOR_PALETTE_PROP );
	}

	/**
	 * Gets a list containing all the config variables.
	 * 
	 * @return a list containing all the config variables. Return
	 *         <code>null</code> if there were no config variable defined.
	 */

	public List getConfigVars( )
	{
		return (ArrayList) getLocalProperty( this, CONFIG_VARS_PROP );
	}

	/**
	 * Gets a list containing all the embedded images.
	 * 
	 * @return a list containing all the embedded images. Return
	 *         <code>null</code> if there were no embedded images defined.
	 */

	public List getImages( )
	{
		return (ArrayList) getLocalProperty( this, IMAGES_PROP );
	}

	/**
	 * Finds an embedded image by name.
	 * 
	 * @param imageName
	 *            the embedded image name
	 * @return the defined image that matches, or <code>null</code> if the
	 *         image name was not found in the embedded images.
	 */

	public EmbeddedImage findImage( String imageName )
	{
		ArrayList list = (ArrayList) getLocalProperty( null, IMAGES_PROP );
		if ( list == null )
			return null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			EmbeddedImage image = (EmbeddedImage) list.get( i );
			if ( image.getName( ) != null
					&& image.getName( ).equals( imageName ) )
				return image;
		}

		return null;
	}

	/**
	 * Finds a parameter by name.
	 * 
	 * @param name
	 *            The parameter name.
	 * @return The parameter, if found, otherwise null.
	 */

	public DesignElement findParameter( String name )
	{
		return nameSpaces[PARAMETER_NAME_SPACE].getElement( name );
	}

	/**
	 * Prepares to save this design. Sets the modification date.
	 */

	public void prepareToSave( )
	{
		super.prepareToSave( );
	}

	/**
	 * Makes a unique name for an element. There are several cases.
	 * <p>
	 * <dl>
	 * <dt>Blank name, name is optional</dt>
	 * <dd>Leave the name blank, for some elements the name is optional.</dd>
	 * 
	 * <dt>Blank name, name is required</dt>
	 * <dd>Create a default name of the form "NewTable" where "New" is
	 * localized, and "Table" is the localized element name for creating a new
	 * element.</dd>
	 * 
	 * <dt>Name already exists in the name space</dt>
	 * <dd>This can occur either for the name provided, or for the default name
	 * created above. Add a number suffix to make the name unique. Example:
	 * "MyName4".</dd>
	 * </dl>
	 * 
	 * @param element
	 *            element for which to create a unique name
	 */

	public void makeUniqueName( DesignElement element )
	{
		ElementDefn eDefn = (ElementDefn) element.getDefn( );
		String name = StringUtil.trimString( element.getName( ) );

		// Some elements can have a blank name.

		if ( eDefn.getNameOption( ) != MetaDataConstants.REQUIRED_NAME
				&& name == null )
			return;

		// If the element already has a unique name, us it.

		NameSpace nameSpace = getNameSpace( eDefn.getNameSpaceID( ) );
		if ( name != null && !nameSpace.contains( name ) )
			return;

		// If the element has no name, create it as "New<new name>" where
		// "<new name>" is the new element display name for the element. Both
		// "New" and the new element display name are localized to the user's
		// locale.

		if ( name == null )
		{
			// When creating a new report element which requires a name, the
			// default
			// name will be "New" followed by the element name, such as "New
			// Label";
			// also, if "NewLabel" already exists, then a number will be
			// appended, such
			// as "NewLabel1", etc.

			name = ModelMessages
					.getMessage( MessageConstants.NAME_PREFIX_NEW_MESSAGE );

			name += ModelMessages.getMessage( "New." //$NON-NLS-1$
					+ element.getDefn( ).getName( ) );
			name = name.trim( );
		}

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String baseName = name;
		while ( nameSpace.contains( name ) )
		{
			name = baseName + ++index; //$NON-NLS-1$
		}
		element.setName( name.trim( ) );
	}

	/**
	 * Records a semantic error during build and similar batch operations. This
	 * implementation is preliminary.
	 * 
	 * @param ex
	 *            the exception to record
	 */

	public void semanticError( SemanticException ex )
	{
		if ( allErrors == null )
			allErrors = new ArrayList( );
		allErrors.add( ex );
	}

	/**
	 * Returns the list of errors accumulated during a batch operation. These
	 * errors can be serious errors or warnings.
	 * 
	 * @return the list of errors or warning
	 */

	public List getAllErrors( )
	{
		return allErrors;
	}

	/**
	 * Returns a list containing all errors during parsing the design file.
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public List getErrorList( )
	{
		List list = ErrorDetail.getSemanticErrors( allErrors,
				DesignFileException.DESIGN_EXCEPTION_SEMANTIC_ERROR );
		list.addAll( ErrorDetail.getSemanticErrors( allErrors,
				DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR ) );
		return list;
	}

	/**
	 * Returns a list containing warnings during parsing the design file.
	 * 
	 * @return a list containing parsing warnings. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public List getWarningList( )
	{
		return ErrorDetail.getSemanticErrors( allErrors,
				DesignFileException.DESIGN_EXCEPTION_SEMANTIC_WARNING );
	}

	/**
	 * Performs a semantic check of this element, and all its contained
	 * elements. Records errors in the design context.
	 * <p>
	 * Checks the contents of this element.
	 * 
	 * @param design
	 *            the report design information needed for the check, and
	 *            records any errors
	 */

	public final void semanticCheck( ReportDesign design )
	{
		List exceptionList = validateWithContents( design );
		allErrors = ErrorDetail.convertExceptionList( exceptionList );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.RootElement#close()
	 */

	public void close( )
	{
		isValid = false;
		saveState = activityStack.getCurrentTransNo( );
		super.close( );
	}

	/**
	 * Checks if the file with <code>fileName</code> exists. The search steps
	 * are described in {@link #findResource(String, int)}.
	 * 
	 * @param fileName
	 *            the file name to check
	 * @param fileType
	 *            the file type
	 * @return true if the file exists, false otherwise.
	 */

	public boolean isFileExist( String fileName, int fileType )
	{
		URL url = findResource( fileName, fileType );

		return url != null;
	}

	/**
	 * Returns the <code>URL</code> object if the file with
	 * <code>fileName</code> exists. This method takes the following search
	 * steps:
	 * <ul>
	 * <li>Search file taking <code>fileName</code> as absolute file name;
	 * <li>Search file taking <code>fileName</code> as relative file name and
	 * basing "base" property of report design;
	 * <li>Search file with the file locator (<code>IResourceLocator</code>)
	 * in session.
	 * </ul>
	 * 
	 * @param fileName
	 *            file name to search
	 * @param fileType
	 *            file type. The value should be one of:
	 *            <ul>
	 *            <li><code>IResourceLocator.IMAGE</code>
	 *            <li><code>IResourceLocator.LIBRARY</code>
	 *            </ul>
	 *            Any invalid value will be treated as
	 *            <code>IResourceLocator.IMAGE</code>.
	 * @return the <code>URL</code> object if the file with
	 *         <code>fileName</code> is found, or null otherwise.
	 */

	public URL findResource( String fileName, int fileType )
	{
		try
		{
			File f = new File( fileName );
			if ( f.isAbsolute( ) )
				return f.exists( ) ? f.toURL( ) : null;

			String base = getStringProperty( this, BASE_PROP );
			if ( base != null )
			{
				f = new File( base, fileName );
				if ( f.exists( ) )
					return f.toURL( );
			}
		}
		catch ( MalformedURLException e )
		{
			return null;
		}

		URL url = getSession( ).getResourceLocator( ).findResource( handle( ),
				fileName, fileType );
		return url;
	}

	/**
	 * Gets a list containing all the include scripts.
	 * 
	 * @return a list containing all the include scripts. Return
	 *         <code>null</code> if there were no scripts defined.
	 */

	public List getIncludeScripts( )
	{
		return (ArrayList) getLocalProperty( this, INCLUDE_SCRIPTS );
	}

	/**
	 * Finds an include script by the file name.
	 * 
	 * @param fileName
	 *            the script file name
	 * @return the defined include script that matches, or <code>null</code>
	 *         if the file name was not found in the include scripts list.
	 */

	public IncludeScript findIncludeScript( String fileName )
	{
		ArrayList list = (ArrayList) getLocalProperty( null, INCLUDE_SCRIPTS );
		if ( list == null )
			return null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			IncludeScript script = (IncludeScript) list.get( i );
			if ( script.getFileName( ) != null
					&& script.getFileName( ).equals( fileName ) )
				return script;
		}

		return null;
	}

	/**
	 * Gets a list containing all the include libraries.
	 * 
	 * @return a list containing all the include libraries. Return
	 *         <code>null</code> if there were no include libraries defined.
	 */

	public ArrayList getIncludeLibraries( )
	{
		return (ArrayList) getLocalProperty( this, INCLUDE_LIBRARIES );
	}

	/**
	 * Returns the validation executor.
	 * 
	 * @return the validation executor
	 */

	public ValidationExecutor getValidationExecutor( )
	{
		return validationExecutor;
	}

	/**
	 * Adds one validation listener. The duplicate listener will not be added.
	 * 
	 * @param listener
	 *            the validation listener to add
	 */

	public void addValidationListener( IValidationListener listener )
	{
		if ( validationListeners == null )
			validationListeners = new ArrayList( );

		if ( !validationListeners.contains( listener ) )
			validationListeners.add( listener );
	}

	/**
	 * Removes one validation listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener
	 *            the validation listener to remove
	 * @return <code>true</code> if <code>listener</code> is sucessfully
	 *         removed. Otherwise <code>false</code>.
	 *  
	 */

	public boolean removeValidationListener( IValidationListener listener )
	{
		if ( validationListeners == null )
			return false;
		return validationListeners.remove( listener );
	}

	/**
	 * Broadcasts the validation event to validation listeners.
	 * 
	 * @param element
	 *            the validated element
	 * @param event
	 *            the validation event
	 */

	public void broadcastValidationEvent( DesignElement element,
			ValidationEvent event )
	{
		if ( validationListeners != null )
		{
			Iterator iter = validationListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				IValidationListener listener = (IValidationListener) iter
						.next( );

				listener.elementValidated( element.getHandle( this ), event );
			}
		}
	}

	/**
	 * Gets the property definition list of the structure list type and its
	 * structure can be referred by other elements. Each one in the list is
	 * instance of <code>IElementPropertyDefn</code>.
	 * 
	 * @return the property definition list of the structure list type and its
	 *         structure can be referred by other elements
	 */

	public List getReferencablePropertyDefns( )
	{
		if ( referencableProperties == null )
			referencableProperties = new ArrayList( );
		if ( referencableProperties.size( ) > 0 )
			return referencableProperties;
		referencableProperties
				.add( getPropertyDefn( IReportDesignModel.CONFIG_VARS_PROP ) );
		referencableProperties
				.add( getPropertyDefn( IReportDesignModel.COLOR_PALETTE_PROP ) );
		referencableProperties
				.add( getPropertyDefn( IReportDesignModel.IMAGES_PROP ) );
		return referencableProperties;
	}
}
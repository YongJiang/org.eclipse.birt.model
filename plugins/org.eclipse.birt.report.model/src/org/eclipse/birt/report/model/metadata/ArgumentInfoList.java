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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an optional argument list of a method.
 *  
 */

public class ArgumentInfoList
{

	/**
	 * The list contains a set of arguments.
	 */

	private List arguments = null;

	/**
	 * Constructs a default <code>ArgumentInfoList</code>.
	 */

	ArgumentInfoList( )
	{
	}

	/**
	 * Adds argument to this method definition.
	 * 
	 * @param argument
	 *            the argument definition to add
	 * @throws MetaDataException
	 *             if the argument name exists.
	 */

	void addArgument( ArgumentInfo argument ) throws MetaDataException
	{
		if ( arguments == null )
			arguments = new ArrayList( );

		if ( getArgument( argument.getName( ) ) != null )
		{
			throw new MetaDataException(
					new String[]{null, argument.getName( )},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ARGUMENT_NAME );
		}
		arguments.add( argument );
	}

	/**
	 * Returns the argument definition given the name.
	 * 
	 * @param argumentName
	 *            name of the argument to get
	 * @return the argument definition with the specified name.
	 */

	public ArgumentInfo getArgument( String argumentName )
	{
		if ( arguments == null )
			return null;

		for ( Iterator iter = ( (ArrayList) arguments ).iterator( ); iter
				.hasNext( ); )
		{
			ArgumentInfo argument = (ArgumentInfo) iter.next( );

			if ( argument.name.equalsIgnoreCase( argumentName ) )
				return argument;
		}

		return null;
	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that
	 * contains <code>ArgumentInfo</code>.
	 * 
	 * @return iterator of argument definition.
	 */

	public Iterator argumentsIterator( )
	{
		if ( arguments == null )
			return Collections.EMPTY_LIST.iterator( );

		return arguments.iterator( );
	}
}
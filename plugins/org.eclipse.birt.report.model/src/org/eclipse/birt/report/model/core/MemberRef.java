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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;

/**
 * Reference to a property, list entry, or member in a list. All the following
 * are supported:
 * <p>
 * <ul>
 * <li>property</li>
 * 
 * <li>property.member</li>
 * <li>property.member.member</li>
 * <li>property.member.list[n]</li>
 * <li>property.member.list[n].member</li>
 * 
 * <li>property.list[n]</li>
 * <li>property.list[n].member</li>
 * <li>property.list[n].member.list[n]</li>
 * <li>property.list[n].member.list[n].member</li>
 * <li>property.list[n].member.member</li>
 * 
 * </ul>
 * <p>
 * The reference supports up to two level of list.member identification. This is
 * the most used by the element definitions.
 *  
 */

public class MemberRef
{

	public final static int PROPERTY = 0;

	public final static int PROPERTY_MEMBER = 1;
	public final static int PROPERTY_MEMBER_MEMBER = 2;
	public final static int PROPERTY_MEMBER_LISTn = 3;
	public final static int PROPERTY_MEMBER_LISTn_MEMBER = 4;

	public final static int PROPERTY_LISTn = 5;
	public final static int PROPERTY_LISTn_MEMBER = 6;
	public final static int PROPERTY_LISTn_MEMBER_LISTn = 7;
	public final static int PROPERTY_LISTn_MEMBER_LISTn_MEMBER = 8;
	public final static int PROPERTY_LISTn_MEMBER_MEMBER = 9;

	public final int refType;

	/**
	 * The property definition. Identifies the property.
	 */

	protected final ElementPropertyDefn propDefn;

	/**
	 * Array of two list indexes. Represents the ".list[n]" portion of the
	 * references.
	 */

	protected final int index[] = new int[2];

	/**
	 * Array of two member names. Represents the ".member" portion of the
	 * reference.
	 */

	protected final StructPropertyDefn member[] = new StructPropertyDefn[2];

	/**
	 * Number of list indexes: always 1 or 2.
	 */

	protected final int depth;

	/**
	 * Property (list, structure)
	 * 
	 * Reference to the top-level property list. Points to the first index
	 * within that list.
	 * 
	 * @param prop
	 *            the property definition
	 */

	public MemberRef( ElementPropertyDefn prop )
	{
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE;

		refType = PROPERTY;
		index[0] = -1;
		depth = 1;
	}

	/**
	 * Reference to the nth item within the top-level property list.
	 * <p>
	 * property.list[n]
	 * 
	 * @param prop
	 *            the property definition
	 * @param n
	 *            the list index
	 */

	public MemberRef( ElementPropertyDefn prop, int n )
	{
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE;
		assert propDefn.isList( );
		assert n >= 0;

		refType = PROPERTY_LISTn;
		index[0] = n;
		depth = 1;
	}

	/**
     * Reference to a member of a top-level structure.
     * <p>
	 * property.member
	 * 
	 * @param prop
	 * @param memberName
	 */

	public MemberRef( ElementPropertyDefn prop, String memberName )
	{
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE;
		assert !propDefn.isList( );

		IPropertyDefn memberDefn = propDefn.getStructDefn( ).getMember(
				memberName );
		assert memberDefn != null;

		refType = PROPERTY_MEMBER;
		member[0] = (StructPropertyDefn)memberDefn;
		index[0] = -1;
		depth = 1;
	}

	/**
     * Reference to a member of a top-level structure.
     * <p>
	 * property.member
	 * 
	 * @param prop
	 * @param memberDef
	 */

	public MemberRef( ElementPropertyDefn prop, StructPropertyDefn memberDef )
	{
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE;
		assert !propDefn.isList( );

		refType = PROPERTY_MEMBER;
		member[0] = memberDef;
		index[0] = -1;
		depth = 1;
	}

	/**
	 * Reference to the the named member in the nth structure in the top-level
	 * list.
	 * <p>
	 * property.list[n].member
	 * 
	 * @param prop
	 *            the property definition
	 * @param n
	 *            the list index
	 * @param memberName
	 *            the name of a member
	 */

	public MemberRef( ElementPropertyDefn prop, int n, String memberName )
	{
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE;
		assert propDefn.isList( );
		assert n >= 0;

		StructPropertyDefn memberDefn = (StructPropertyDefn)propDefn.getStructDefn( ).getMember(
				memberName );
		assert memberDefn != null;

		refType = PROPERTY_LISTn_MEMBER;
		index[0] = n;
		depth = 1;
		member[0] = memberDefn;
	}

	/**
	 * Reference to the identified member of the structure at the nth position
	 * within the top-level list.
	 * <p>
	 * property.list[n].member
	 * 
	 * @param prop
	 *            the property definition
	 * @param n
	 *            the list index
	 * @param memberDef
	 *            the definition of the member
	 */

	public MemberRef( ElementPropertyDefn prop, int n,
			StructPropertyDefn memberDef )
	{
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE;
		assert propDefn.isList( );
		assert n >= 0;
        assert memberDef != null;

		refType = PROPERTY_LISTn_MEMBER;
		index[0] = n;
		member[0] = memberDef;
		depth = 1;
	}

	/**
	 * Reference to the nth item in a first or second level list.
	 * <p>
	 * property.member.list[n] <br>
	 * 
	 * property.list[n] <br>
	 * property.list[n].member.list[n]
	 * 
	 * @param ref
	 *            reference to a property or member
	 * @param n
	 *            the list index
	 */

	public MemberRef( MemberRef ref, int n )
	{
		propDefn = ref.propDefn;

		assert ref.depth < 2;
		assert n >= 0;
        assert ref.isListRef();

		if ( propDefn.isList( ) )
		{
			if ( ref.refType == PROPERTY )
			{
				// property.list[n]

				refType = PROPERTY_LISTn;
				index[0] = n;
				depth = 1;
			}
			else
			{
				// property.list[n].member.list[n]

				assert ref.refType == PROPERTY_LISTn_MEMBER;
				assert ref.member[0].isList( );

				refType = PROPERTY_LISTn_MEMBER_LISTn;
				index[0] = ref.index[0];
				member[0] = ref.member[0];

				index[1] = n;
				depth = 2;
			}
		}
		else
		{
			// property.member.list[n]

			assert ref.refType == PROPERTY_MEMBER;
			assert ref.member[0].isList( );

			refType = PROPERTY_MEMBER_LISTn;
			index[0] = n;
			member[0] = ref.member[0];
			depth = 1;
		}
	}

	/**
	 * Reference to a member in the nth item in a second-level list. The
	 * top-level list item is given by the member ref.
	 * <p>
	 * property.member.list[n].member <br>
	 * 
	 * property.list[n].member <br>
	 * property.list[n].member.list[n].member
	 * 
	 * @param ref
	 *            reference to a property or member
	 * @param n
	 *            the list index
	 * @param memberDefn
	 *            the definition of the member
	 */

	public MemberRef( MemberRef ref, int n, StructPropertyDefn memberDefn )
	{
		propDefn = ref.propDefn;
		assert propDefn != null;
		assert n >= 0;
		assert memberDefn != null;

		if ( propDefn.isList( ) )
		{
			if ( ref.refType == PROPERTY_LISTn_MEMBER )
			{
				// property.list[n].member.list[n].member

				assert ref.member[0].isList( );

				refType = PROPERTY_LISTn_MEMBER_LISTn_MEMBER;

				member[0] = ref.member[0];
				index[0] = ref.index[0];

				member[1] = memberDefn;
				index[1] = n;

				depth = 2;
			}
			else
			{
				// property.list[n].member

				assert ref.refType == PROPERTY;

				refType = PROPERTY_LISTn_MEMBER;
				member[0] = memberDefn;
				index[0] = n;

				depth = 1;
			}
		}
		else
		{
			// property.member.list[n].member

			assert ref.refType == PROPERTY_MEMBER;
			assert ref.member[0].isList( );

			refType = PROPERTY_MEMBER_LISTn_MEMBER;
			index[0] = n;
			member[0] = ref.member[0];

			member[1] = memberDefn;

			depth = 1;
		}
	}

	/**
	 * Reference to a member within a first- or second-level structure. The
	 * structure is identified with the member ref.
	 * <p>
	 * property.member <br>
	 * property.member.member <br>
	 * 
	 * property.list[n].member <br>
	 * property.list[n].member.list[n].member <br>
	 * 
	 * property.list[n].member.member
	 * 
	 * @param ref
	 *            reference a structure
	 * @param memberName
	 *            the name of a member
	 */

	public MemberRef( MemberRef ref, String memberName )
	{
		this( ref, (StructPropertyDefn)ref.getStructDefn( ).getMember( memberName ) );
	}

	/**
	 * Reference to a member within a first- or second-level structure. The
	 * structure is identified with the member ref.
	 * <p>
	 * property.member <br>
	 * property.member.member <br>
	 * property.member.list[n].member
	 * 
	 * property.list[n].member <br>
	 * property.list[n].member.list[n].member <br>
	 * 
	 * property.list[n].member.member
	 * 
	 * @param ref
	 *            reference a structure
	 * @param memberDefn
	 *            the definition of the member
	 */

	public MemberRef( MemberRef ref, StructPropertyDefn memberDefn )
	{
		assert memberDefn != null;

		propDefn = ref.propDefn;

		if ( propDefn.isList( ) )
		{
			if ( ref.refType == PROPERTY_LISTn )
			{
				// property.list[n].member

				refType = PROPERTY_LISTn_MEMBER;
				member[0] = memberDefn;
				index[0] = ref.index[0];

				depth = 1;
			}
			else
			{
				if ( ref.refType == PROPERTY_LISTn_MEMBER_LISTn )
				{
					// property.list[n].member.list[n].member

					assert ref.member[0].isList( );

					refType = PROPERTY_LISTn_MEMBER_LISTn_MEMBER;

					member[0] = ref.member[0];
					index[0] = ref.index[0];

					member[1] = memberDefn;
					index[1] = ref.index[1];

					depth = 2;
				}
				else
				{
					// property.list[n].member.member

					assert ref.refType == PROPERTY_LISTn_MEMBER;
					assert !ref.member[0].isList( );

					refType = PROPERTY_LISTn_MEMBER_MEMBER;
					member[0] = ref.member[0];
					index[0] = ref.index[0];

					member[1] = memberDefn;

					depth = 1;
				}
			}
		}
		else
		{
			if ( ref.refType == PROPERTY )
			{
				// property.member

				refType = PROPERTY_MEMBER;

				member[0] = memberDefn;
				index[0] = -1;

				depth = 1;
			}
			else if ( ref.refType == PROPERTY_MEMBER )
			{
				// property.member.member

				assert !ref.member[0].isList( );

				refType = PROPERTY_MEMBER_MEMBER;

				member[0] = ref.member[0];
				index[0] = -1;

				member[1] = memberDefn;
				depth = 1;
			}
			else
			{
				// property.member.list[n].member

				assert ref.refType == PROPERTY_MEMBER_LISTn;

				refType = PROPERTY_MEMBER_LISTn_MEMBER;

				member[0] = ref.member[0];
				index[0] = ref.index[0];

				member[1] = memberDefn;
				depth = 1;
			}
		}

	}

	/**
	 * Returns a reference to the parent.
	 * <p>
	 * <strong>property.list[n].member <\b>.list[n][.member]
	 * 
	 * @return a reference to the parent member
	 */

	public MemberRef getParentRef( )
	{
		if ( depth == 1 )
			return null;
		return new MemberRef( propDefn, index[0], member[0] );
	}

	/**
	 * Gets the value of the referenced property, structure, or member.
	 * 
	 * @param design
	 *            the report design
	 * 
	 * @param element
	 *            the element for which to retrieve the value
	 * @return the retrieved value, which may be null
	 */

	public Object getValue( ReportDesign design, DesignElement element )
	{
		Structure struct = getStructure( design, element );
		if ( propDefn.isList( ) )
		{
			// property
			// property.list[n]
			// property.list[n].member
			// property.list[n].member.list[n]
			// property.list[n].member.list[n].member
			// property.list[n].member.member

			switch ( refType )
			{
				case PROPERTY :
					return getList( design, element );
				case PROPERTY_LISTn :
				case PROPERTY_LISTn_MEMBER_LISTn :
					return struct;
				case PROPERTY_LISTn_MEMBER : // reference the list itself.
					return struct.getProperty( design, member[0] );
				case PROPERTY_LISTn_MEMBER_MEMBER :
				case PROPERTY_LISTn_MEMBER_LISTn_MEMBER :
					return struct.getProperty( design, member[1] );
				default :
				{
					assert false;
					return null;
				}
			}
		}

		// property

		// property.member
		// property.member.member
		// property.member.list[n]
		// property.member.list[n].member

		switch ( refType )
		{
			case PROPERTY :
			case PROPERTY_MEMBER_LISTn :
				return struct;
			case PROPERTY_MEMBER :
				return struct.getProperty( design, member[0] );
			case PROPERTY_MEMBER_MEMBER :
			case PROPERTY_MEMBER_LISTn_MEMBER :
				return struct.getProperty( design, member[1] );
			default :
			{
				assert false;
				return null;
			}
		}
	}

    
    /**
	 * Returns the definition of the property portion of the reference.
	 * 
	 * @return the property definition
	 */

	public ElementPropertyDefn getPropDefn( )
	{
		return propDefn;
	}

	/**
	 * Returns the definition of the member to which the reference points.
	 * <p>
	 * property. <strong>member </strong> <br>
	 * property.member. <strong>member </strong> <br>
	 * property.member.list[n]. <strong>member <strong><br>
	 * 
	 * property.list[n]. <strong>member </strong> <br>
	 * property.list[n].member.list[n]. <strong>member </strong> <br>
	 * 
	 * @return the definition of the target member
	 */

	public PropertyDefn getMemberDefn( )
	{
		return member[1] == null ? member[0] : member[1];
	}

	/**
	 * Returns the structure pointed to by the reference.
	 * <p>
	 * <strong>property </strong>[.member] <br>
	 * property. <strong>member </strong>.member <br>
	 * property.member. <strong>list[n] </strong>[.member] <br>
	 * 
	 * property. <strong>list[n] </strong>[.member] <br>
	 * property.list[n].member. <strong>list[n] </strong>[.member] <br>
	 * property.list[n]. <strong>member </strong>.member
	 * 
	 * @param design
	 *            the report design
	 * 
	 * @param element
	 *            the element from which to retrieve the structure
	 * @return the value of the referenced structure
	 */

	public Structure getStructure( ReportDesign design, DesignElement element )
	{
		if ( propDefn.isList( ) )
		{
			List list = getList( design, element );
			if ( list == null )
				return null;

			// Get the entry within the list.

			if ( depth == 1 )
			{
				if ( index[0] < 0 || index[0] >= list.size( ) )
					return null;

				Structure struct = (Structure) list.get( index[0] );

				if ( member[1] == null )
					return struct;

				// property.list[n].member.member

				assert !member[0].isList( );
				return (Structure) struct.getProperty( design, member[0] );
			}
			else if ( depth == 2 )
			{
				if ( index[1] < 0 || index[1] >= list.size( ) )
					return null;
				return (Structure) list.get( index[1] );
			}

			return null;
		}

		Structure struct = (Structure) element.getProperty( design, propDefn );
		if ( struct == null )
			return null;

		if ( index[0] >= 0 )
		{
			// property.member.list[n]
			// property.member.list[n].member

			assert member[0].isList( );
			ArrayList list = (ArrayList) struct.getProperty( design, member[0] );

			if ( index[0] < 0 || index[0] >= list.size( ) )
				return null;
			return (Structure) list.get( index[0] );
		}

		if ( member[1] != null )
		{
			// property.member.member

			assert !member[0].isList( );
			return (Structure) struct.getProperty( design, member[0] );
		}

		// property.member
		// property

		return struct;
	}

	/**
	 * Returns the definition of the structure pointed to by the reference.
	 * <p>
	 * <strong>property </strong> <br>
	 * <strong>property </strong>.member <br>
	 * 
	 * property. <strong>member </strong>.member <br>
	 * property.member. <strong>list[n] </strong> <br>
	 * property.member. <strong>list[n] </strong>.member <br>
	 * 
	 * property. <strong>list[n] </strong>[.member] <br>
	 * property.list[n].member. <strong>list[n] </strong>[.member] <br>
	 * property.list[n]. <strong>member </strong>.member
	 * 
	 * @return the definition of the structure pointed to by the reference
	 */

	public IStructureDefn getStructDefn( )
	{
		switch ( refType )
		{
			case PROPERTY :
			case PROPERTY_LISTn :
			case PROPERTY_LISTn_MEMBER :
			case PROPERTY_MEMBER :
				return propDefn.getStructDefn( );
			case PROPERTY_LISTn_MEMBER_MEMBER :
			case PROPERTY_LISTn_MEMBER_LISTn :
			case PROPERTY_LISTn_MEMBER_LISTn_MEMBER :
			case PROPERTY_MEMBER_MEMBER :
			case PROPERTY_MEMBER_LISTn_MEMBER :
			case PROPERTY_MEMBER_LISTn :
				return member[0].getStructDefn( );
			default :
				assert false;
				return null;
		}

	}

	/**
	 * Returns the list index pointed to by this reference.
	 * <p>
	 * property.member.list[ <strong>n </strong> ][.member] <br>
	 * 
	 * property.list[ <strong>n </strong>][.member] <br>
	 * property.list[n].member.list[ <strong>n </strong>][.member]
	 * 
	 * @return the list index pointed to by this reference
	 */

	public int getIndex( )
	{
		if ( propDefn.isList( ) )
			return index[depth - 1];

		return index[0];
	}

	/**
	 * Returns the depth of the reference: 1 or 2.
	 * <p>
	 * property.list[n][.member] --&gt 1 <br>
	 * property.member[.list[n]]
	 * 
	 * property.list[n].member.list[n][.member] --&gt 2
	 * property.member.list[n].member property.member.member
	 * 
	 * @return the depth of the reference
	 */

	public int getDepth( )
	{
		return depth;
	}

    
    /**
	 * Returns the list pointed to by this reference.
	 * <p>
     * <strong>property</strong> <br>
	 * property. <strong>list </strong>[n][.member] <br>
	 * property.list[n].member. <strong>list </strong>[n][.member] 
     * property.list[n] </strong>.member.member <br>
	 * 
	 * property.member. <strong>list </strong>[n][.member] <br>
	 * 
	 * @param design
	 *            the report design
	 * 
	 * @param element
	 *            the element for which to retrieve the list
	 * @return the list of structures
	 */

	public List getList( ReportDesign design, DesignElement element )
	{
		if ( propDefn.isList( ) )
		{
			// Get the property list. If the list is null, there
			// is no value.

			ArrayList list = (ArrayList) element.getProperty( design, propDefn );
			if ( list == null )
				return null;

			if ( depth == 2 )
			{
				// If the top-level index is out of range, then there
				// is no value.

				if ( index[0] < 0 || index[0] >= list.size( ) )
					return null;
				Structure struct = (Structure) list.get( index[0] );

				// Check the second-level list if needed.

				assert member[0].isList( );
				list = (ArrayList) struct.getProperty( design, member[0] );
			}

			return list;
		}

		// not a list property

		Structure struct = (Structure) element.getProperty( design, propDefn );
		if ( struct != null && member[0] != null && member[0].isList( ) )
			return (ArrayList) struct.getProperty( design, member[0] );

		return null;
	}

    /**
     * Indicates whether this member reference points to a list.
     * 
     * @return true if points to a list.
     */
    
    public boolean isListRef()
    {
    	switch( refType )
        {
    		case PROPERTY:
                return propDefn.isList(); 
            case PROPERTY_MEMBER:
            case PROPERTY_LISTn_MEMBER:
                return member[0].isList(); 
            case PROPERTY_MEMBER_MEMBER:
            case PROPERTY_MEMBER_LISTn_MEMBER:
            case PROPERTY_LISTn_MEMBER_LISTn_MEMBER:
            case PROPERTY_LISTn_MEMBER_MEMBER:
                return member[1].isList();
            default:
                return false;
        }
        
    }
}
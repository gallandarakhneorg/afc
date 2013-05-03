/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.attrs.attr;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This class contains an attribute value.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeImpl extends AttributeValueImpl implements Attribute {
	
	/** Compare the two specified attributes.
	 *
	 * @param arg0
	 * @param arg1
	 * @return replies a negative value if <var>arg0</var> is lesser than
	 * <var>arg1</var>, a positive value if <var>arg0</var> is greater than
	 * <var>arg1</var>, or <code>0</code> if they are equal.
	 * @see AttributeComparator
	 */
	public static int compareAttrs(Attribute arg0, Attribute arg1) {
		if (arg0==arg1) return 0;
		if (arg0==null) return 1;
		if (arg1==null) return -1;
		
		String n0 = arg0.getName();
		String n1 = arg1.getName();
		int cmp = compareAttrNames(n0, n1);
		
		if (cmp==0)
			return compareValues(arg0,arg1);
		
		return cmp;
	}
	
	/** Compare the two specified attribute names.
	 *
	 * @param arg0
	 * @param arg1
	 * @return replies a negative value if <var>arg0</var> is lesser than
	 * <var>arg1</var>, a positive value if <var>arg0</var> is greater than
	 * <var>arg1</var>, or <code>0</code> if they are equal.
	 * @see AttributeNameComparator
	 */
	public static int compareAttrNames(String arg0, String arg1) {
		if (arg0==arg1) return 0;
		if (arg0==null) return Integer.MAX_VALUE;
		if (arg1==null) return Integer.MIN_VALUE;
		return arg0.compareToIgnoreCase(arg1);
	}

	/**
	 * Name of the metadata.
	 */
	private String name = null;

	/**
	 * Uninitialized attribute.
	 */
	public AttributeImpl() {
		//
	}

	/**
	 * Uninitialized attribute.
	 * 
	 * @param type is the type of the attribute.
	 */
	public AttributeImpl(AttributeType type) {
		super(type);
	}
	
	/**
	 * Uninitialized attribute.
	 * 
	 * @param name is the name of the attribute
	 */
	public AttributeImpl(String name) {
		this.name = name;
	}

	/**
	 * Uninitialized attribute.
	 * 
	 * @param name is the name of the attribute
	 * @param type is the type of the attribute.
	 */
	public AttributeImpl(String name, AttributeType type) {
		super(type);
		this.name = name;
	}

	/**
	 * Initialized attribute with the given raw value.
	 * The raw value must be compatible with the internal
	 * representation of the value.
	 * 
	 * @param name is the name of the attribute
	 * @param type is the type of the attribute.
	 * @param rawValue is the rawValue.
	 */
	public AttributeImpl(String name, AttributeType type, Object rawValue) {
		super(type, rawValue);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, AttributeValue value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(Attribute value) {
		super(value);
		this.name = value.getName();
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, boolean value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, Color value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, Date value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, float value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, double value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, Image value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, int value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, long value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, Object value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, Point2D value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 */
	public AttributeImpl(String name, float x, float y) {
		super(x,y);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 */
	public AttributeImpl(String name, double x, double y) {
		super(x,y);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the vaule of this new attribute.
	 */
	public AttributeImpl(String name, Point3D value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 * @param z is the value of this new attribute.
	 */
	public AttributeImpl(String name, float x, float y, float z) {
		super(x,y,z);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 * @param z is the value of this new attribute.
	 */
	public AttributeImpl(String name, double x, double y, double z) {
		super(x,y,z);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, String value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Point2D[] value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Point3D[] value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Enum<?> value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, InetAddress value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, InetSocketAddress value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, URI value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, URL value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, UUID value) {
		super(value);
		this.name = name;
	}

	/**
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Class<?> value) {
		super(value);
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param o {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Attribute) {
			return compareAttrs(this, (Attribute)o)==0;
		}
		return super.equals(o);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (this.name!=null ? this.name.hashCode() : 0);
        result = PRIME * result + super.hashCode();
        return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append('[');
		str.append((this.name==null)
				? "???" //$NON-NLS-1$
				: this.name);
		str.append('=');
		try {
			str.append((getValue()==null)
					? "???" //$NON-NLS-1$
					: getValue().toString());
		}
		catch (AttributeException e) {
			str.append("???"); //$NON-NLS-1$
		}
		str.append(':');
		str.append(getType().toString());
		str.append(']');
		return str.toString();
	}
	
	/** Assert that the attribute value was assigned and not <code>null</code>.
	 */
	@Override
	protected void assertAssignedAndNotNull() throws AttributeNotInitializedException {
		try {
			super.assertAssignedAndNotNull();
		}
		catch(AttributeNotInitializedException _) {
			throw new AttributeNotInitializedException(this.name);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setAttribute(Attribute value) throws InvalidAttributeTypeException {
		setValue(value);
		this.name = value.getName();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Comparator<? extends Attribute> nameComparator() {
		return new AttributeNameComparator();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Comparator<? extends Attribute> comparator() {
		return new AttributeComparator();
	}

}
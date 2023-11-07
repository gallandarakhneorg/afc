/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.attrs.attr;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class contains an attribute value.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeImpl extends AttributeValueImpl implements Attribute {

	private static final long serialVersionUID = -3805997544158892016L;

	/**
	 * Name of the metadata.
	 */
	private String name;

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

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, AttributeValue value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(Attribute value) {
		super(value);
		this.name = value.getName();
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, boolean value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Date value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, float value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, double value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, int value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, long value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Object value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Point2D<?, ?> value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 */
	public AttributeImpl(String name, float x, float y) {
		super(x, y);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 */
	public AttributeImpl(String name, double x, double y) {
		super(x, y);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Point3D<?, ?> value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 * @param z is the value of this new attribute.
	 */
	public AttributeImpl(String name, float x, float y, float z) {
		super(x, y, z);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param x is the value of this new attribute.
	 * @param y is the value of this new attribute.
	 * @param z is the value of this new attribute.
	 */
	public AttributeImpl(String name, double x, double y, double z) {
		super(x, y, z);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, String value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Point2D<?, ?>[] value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Point3D<?, ?>[] value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Enum<?> value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, InetAddress value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, InetSocketAddress value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, URI value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, URL value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, UUID value) {
		super(value);
		this.name = name;
	}

	/** Constructor from the given value.
	 * @param name is the name of the attribute
	 * @param value is the value of this new attribute.
	 */
	public AttributeImpl(String name, Class<?> value) {
		super(value);
		this.name = name;
	}

	/** Compare the two specified attributes.
	 *
	 * @param arg0 first attribute
	 * @param arg1 second attribute.
	 * @return replies a negative value if {@code arg0} is lesser than
	 *     {@code arg1}, a positive value if {@code arg0} is greater than
	 *     {@code arg1}, or {@code 0} if they are equal.
	 * @see AttributeComparator
	 */
	@Pure
	public static int compareAttrs(Attribute arg0, Attribute arg1) {
		if (arg0 == arg1) {
			return 0;
		}
		if (arg0 == null) {
			return 1;
		}
		if (arg1 == null) {
			return -1;
		}

		final String n0 = arg0.getName();
		final String n1 = arg1.getName();
		final int cmp = compareAttrNames(n0, n1);

		if (cmp == 0) {
			return compareValues(arg0, arg1);
		}

		return cmp;
	}

	/** Compare the two specified attribute names.
	 *
	 * @param arg0 first attribute.
	 * @param arg1 second attribute.
	 * @return replies a negative value if {@code arg0} is lesser than
	 * {@code arg1}, a positive value if {@code arg0} is greater than
	 * {@code arg1}, or {@code 0} if they are equal.
	 * @see AttributeNameComparator
	 */
	@Pure
	public static int compareAttrNames(String arg0, String arg1) {
		if (arg0 == arg1) {
			return 0;
		}
		if (arg0 == null) {
			return Integer.MAX_VALUE;
		}
		if (arg1 == null) {
			return Integer.MIN_VALUE;
		}
		return arg0.compareToIgnoreCase(arg1);
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Attribute) {
			return compareAttrs(this, (Attribute) obj) == 0;
		}
		return super.equals(obj);
	}

	@Pure
	@Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hashCode(this.name);
        result = prime * result + super.hashCode();
        return result ^ (result >> 31);
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("name", this.name); //$NON-NLS-1$
	}

	/** Assert that the attribute value was assigned and not {@code null}.
	 */
	@Override
	protected void assertAssignedAndNotNull() throws AttributeNotInitializedException {
		try {
			super.assertAssignedAndNotNull();
		} catch (AttributeNotInitializedException exception) {
			throw new AttributeNotInitializedException(this.name);
		}
	}

	@Pure
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setAttribute(Attribute value) throws InvalidAttributeTypeException {
		setValue(value);
		this.name = value.getName();
	}

	@Pure
	@Override
	public Comparator<? extends Attribute> nameComparator() {
		return new AttributeNameComparator();
	}

	@Pure
	@Override
	public Comparator<? extends Attribute> comparator() {
		return new AttributeComparator();
	}

}

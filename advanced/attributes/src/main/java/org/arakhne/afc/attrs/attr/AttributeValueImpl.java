/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import static org.arakhne.afc.attrs.attr.AttributeConstants.DEFAULT_SCHEME;
import static org.arakhne.afc.attrs.attr.AttributeConstants.FALSE_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.F_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.NON_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.NO_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.N_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.OUI_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.O_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.TRUE_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.T_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.YES_CONSTANT;
import static org.arakhne.afc.attrs.attr.AttributeConstants.Y_CONSTANT;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.arakhne.afc.math.MathUtil;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.VectorToolkit;

/**
 * This class contains an attribute value.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"deprecation", "checkstyle:methodcount"})
public class AttributeValueImpl implements AttributeValue {

	private static final long serialVersionUID = 4014368008512085546L;

	/**
	 * Type of the metadata.
	 */
	private AttributeType type;

	/**
	 * Value of the metadata.
	 */
	private Object value;

	/** Indicates if this attribute was assigned.
	 */
	private boolean assigned;

	/**
	 * Uninitialized value.
	 */
	public AttributeValueImpl() {
		this.type = AttributeType.OBJECT;
		this.value = this.type.getDefaultValue();
	}

	/**
	 * Uninitialized value.
	 *
	 * @param type is the type of the value.
	 */
	public AttributeValueImpl(AttributeType type) {
		this.type = type;
		this.value = null;
		this.assigned = false;
	}

	/**
	 * @param value is the value to initialize this new instance.
	 */
	public AttributeValueImpl(AttributeValue value) {
		if (value != null) {
			this.type = value.getType();
			try {
				this.value = value.getValue();
				this.assigned = isNullAllowed() || (this.value != null);
			} catch (AttributeException exception) {
				this.value = null;
				this.assigned = false;
			}
		} else {
			this.value = null;
			this.assigned = false;
		}
	}

	/**
	 * @param type is the type of this value.
	 * @param rawValue is the value.
	 */
	public AttributeValueImpl(AttributeType type, Object rawValue) {
		this.type = type;
		if (rawValue == null) {
			this.value = rawValue;
		} else {
			try {
				this.value = type.cast(rawValue);
			} catch (Exception exception) {
				this.value = null;
			}
		}
		this.assigned = this.value != null;
	}


	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(boolean value) {
		this.type = AttributeType.BOOLEAN;
		this.value = new Boolean(value);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 * @deprecated No replacement
	 */
	@Deprecated
	public AttributeValueImpl(Color value) {
		this.type = AttributeType.COLOR;
		this.value = (value != null) ? VectorToolkit.color(value.getRed(), value.getGreen(),
				value.getBlue(), value.getAlpha()) : null;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(UUID value) {
		this.type = AttributeType.UUID;
		this.value = (value != null) ? new UUID(value.getMostSignificantBits(),
				value.getLeastSignificantBits()) : null;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(URL value) {
		this.type = AttributeType.URL;
		try {
			this.value = (value != null) ? new URL(value.toExternalForm()) : null;
		} catch (MalformedURLException e) {
			this.value = null;
		}
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(URI value) {
		this.type = AttributeType.URI;
		try {
			this.value = (value != null) ? new URI(value.toASCIIString()) : null;
		} catch (URISyntaxException e) {
			this.value = null;
		}
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(InetAddress value) {
		this.type = AttributeType.INET_ADDRESS;
		this.value = (value != null) ? value : null;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(InetSocketAddress value) {
		this.type = AttributeType.INET_ADDRESS;
		this.value = (value != null) ? value.getAddress() : null;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Enum<?> value) {
		this.type = AttributeType.ENUMERATION;
		this.value = (value != null) ? value : null;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Class<?> value) {
		this.type = AttributeType.TYPE;
		this.value = (value != null) ? value : null;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Date value) {
		this.type = AttributeType.DATE;
		this.value = (value != null) ? new Date(value.getTime()) : null;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(float value) {
		this.type = AttributeType.REAL;
		this.value = new Double(value);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(double value) {
		this.type = AttributeType.REAL;
		this.value = new Double(value);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 * @deprecated no replacement.
	 */
	@Deprecated
	public AttributeValueImpl(Image value) {
		this.type = AttributeType.IMAGE;
		this.value = value;
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(int value) {
		this.type = AttributeType.INTEGER;
		this.value = new Long(value);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(long value) {
		this.type = AttributeType.INTEGER;
		this.value = new Long(value);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Point2D<?, ?> value) {
		this.type = AttributeType.POINT;
		this.value = value;
		this.assigned = this.value != null;
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 */
	public AttributeValueImpl(float x, float y) {
		this.type = AttributeType.POINT;
		this.value = new Point2d(x, y);
		this.assigned = true;
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 */
	public AttributeValueImpl(double x, double y) {
		this.type = AttributeType.POINT;
		this.value = new Point2d(x, y);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Point3D value) {
		this.type = AttributeType.POINT3D;
		this.value = value;
		this.assigned = this.value != null;
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 * @param z is the value.
	 */
	public AttributeValueImpl(float x, float y, float z) {
		this.type = AttributeType.POINT3D;
		//TODO: Fix code: new Point3fp(x, y, z);
		this.value = null;
		this.assigned = true;
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 * @param z is the value.
	 */
	public AttributeValueImpl(double x, double y, double z) {
		this.type = AttributeType.POINT3D;
		// TODO: Fix code: new Point3fp(x, y, z);
		this.value = null;
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(String value) {
		this.type = AttributeType.STRING;
		this.value = value;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Point2D<?, ?>[] value) {
		this.type = AttributeType.POLYLINE;
		this.value = value;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Point3D[] value) {
		this.type = AttributeType.POLYLINE3D;
		this.value = value;
		this.assigned = this.value != null;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Object value) {
		final AttributeType detectedType = AttributeType.fromValue(value);
		this.type = detectedType;
		this.value = detectedType.cast(value);
		this.assigned = isNullAllowed() || (this.value != null);
	}

	/** Replies the best attribute value that is representing
	 * the given text.
	 *
	 * @param text the text.
	 * @return the attribute value, never <code>null</code>.
	 */
	@Pure
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public static AttributeValueImpl parse(String text) {
		final AttributeValueImpl value = new AttributeValueImpl(text);
		if (text != null && !text.isEmpty()) {
			Object binValue;
			for (final AttributeType type : AttributeType.values()) {
				try {
					binValue = null;
					switch (type) {
					case BOOLEAN:
						binValue = value.getBoolean();
						break;
					case COLOR:
						binValue = parseColor((String) value.value, true);
						break;
					case DATE:
						binValue = value.getDate();
						break;
					case ENUMERATION:
						binValue = value.getEnumeration();
						break;
					case IMAGE:
						binValue = value.getImage();
						break;
					case INET_ADDRESS:
						binValue = value.getInetAddress();
						break;
					case INTEGER:
						binValue = value.getInteger();
						break;
					case OBJECT:
						binValue = value.getJavaObject();
						break;
					case POINT:
						binValue = parsePoint((String) value.value, true);
						break;
					case POINT3D:
						binValue = parsePoint3D((String) value.value, true);
						break;
					case POLYLINE:
						binValue = parsePolyline((String) value.value, true);
						break;
					case POLYLINE3D:
						binValue = parsePolyline3D((String) value.value, true);
						break;
					case REAL:
						binValue = value.getReal();
						break;
					case STRING:
						binValue = value.getString();
						break;
					case TIMESTAMP:
						binValue = value.getTimestamp();
						break;
					case TYPE:
						binValue = value.getJavaClass();
						break;
					case URI:
						binValue = parseURI((String) value.value);
						break;
					case URL:
						binValue = value.getURL();
						break;
					case UUID:
						binValue = parseUUID((String) value.value);
						break;
					default:
						//
					}
					if (binValue != null) {
						return new AttributeValueImpl(type, binValue);
					}
				} catch (Throwable exception) {
					//
				}
			}
		}
		return value;
	}

	/** Compare the two specified values.
	 *
	 * @param arg0 first value.
	 * @param arg1 second value.
	 * @return replies a negative value if {@code arg0} is lesser than
	 * {@code arg1}, a positive value if {@code arg0} is greater than
	 * {@code arg1}, or <code>0</code> if they are equal.
	 * @see AttributeValueComparator
	 */
	@Pure
	public static int compareValues(AttributeValue arg0, AttributeValue arg1) {
		if (arg0 == arg1) {
			return 0;
		}
		if (arg0 == null) {
			return Integer.MAX_VALUE;
		}
		if (arg1 == null) {
			return Integer.MIN_VALUE;
		}

		Object v0;
		Object v1;

		try {
			v0 = arg0.getValue();
		} catch (Exception exception) {
			v0 = null;
		}

		try {
			v1 = arg1.getValue();
		} catch (Exception exception) {
			v1 = null;
		}

		return compareRawValues(v0, v1);
	}

	/** Compare the internal objects of two specified values
	 *
	 * @param arg0 first value.
	 * @param arg1 second value.
	 * @return replies a negative value if {@code arg0} is lesser than
	 * {@code arg1}, a positive value if {@code arg0} is greater than
	 * {@code arg1}, or <code>0</code> if they are equal.
	 */
	@Pure
	@SuppressWarnings({"unchecked", "rawtypes", "checkstyle:returncount", "checkstyle:npathcomplexity"})
	private static int compareRawValues(Object arg0, Object arg1) {
		if (arg0 == arg1) {
			return 0;
		}
		if (arg0 == null) {
			return Integer.MAX_VALUE;
		}
		if (arg1 == null) {
			return Integer.MIN_VALUE;
		}

		if ((arg0 instanceof Number) && (arg1 instanceof Number)) {
			return Double.compare(((Number) arg0).doubleValue(), ((Number) arg1).doubleValue());
		}

		try {
			if (arg0 instanceof Comparable<?>) {
				return ((Comparable) arg0).compareTo(arg1);
			}
		} catch (RuntimeException exception) {
			//
		}

		try {
			if (arg1 instanceof Comparable<?>) {
				return -((Comparable) arg1).compareTo(arg0);
			}
		} catch (RuntimeException exception) {
			//
		}

		if (arg0.equals(arg1)) {
			return 0;
		}

		final String sv0 = arg0.toString();
		final String sv1 = arg1.toString();

		if (sv0 == sv1) {
			return 0;
		}
		if (sv0 == null) {
			return Integer.MAX_VALUE;
		}
		if (sv1 == null) {
			return Integer.MIN_VALUE;
		}

		return sv0.compareTo(sv1);
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AttributeValue) {
			return compareValues(this, (AttributeValue) obj) == 0;
		}
		return compareRawValues(this.value, obj) == 0;
	}

	@Pure
	@Override
	public int hashCode() {
		return 31 + Objects.hashCode(this.value);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("["); //$NON-NLS-1$
		str.append((this.value == null)
				? "???" //$NON-NLS-1$
				: this.value.toString());
		str.append(":"); //$NON-NLS-1$
		str.append(this.type.toString());
		str.append("]"); //$NON-NLS-1$
		return str.toString();
	}

	@Pure
	@Override
	public boolean isAssignableFrom(AttributeType type) {
		return getType().isAssignableFrom(type);
	}

	@Pure
	@Override
	public boolean isAssignableFrom(AttributeValue value) {
		return getType().isAssignableFrom(value.getType());
	}

	/** Replies if this value was assigned and
	 * supposes that the <code>null</code> value is
	 * allowed.
	 *
	 * @return <code>true</code> if the value is not assigned or equals to <code>null</code>.
	 */
	private boolean isNotAssignedOrNull() {
		return !this.assigned || this.value == null;
	}

	/** Assert that the attribute value was assigned.
	 */
	private void assertAssigned() throws AttributeNotInitializedException {
		if ((this.type == null) || (!this.assigned)) {
			throw new AttributeNotInitializedException();
		}
	}

	/** Assert that the attribute value was assigned and not <code>null</code>.
	 *
	 * @throws AttributeNotInitializedException attribute not initialized.
	 */
	protected void assertAssignedAndNotNull() throws AttributeNotInitializedException {
		if ((this.type == null) || (!this.assigned) || (this.value == null)) {
			throw new AttributeNotInitializedException();
		}
	}

	@Pure
	@Override
	public boolean isBaseType() {
		return this.type.isBaseType();
	}

	@Pure
	@Override
	public boolean isNullAllowed() {
		return this.type.isNullAllowed();
	}

	@Pure
	@Override
	public AttributeType getType() {
		return this.type;
	}

	@Override
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public void setType(AttributeType type) throws InvalidAttributeTypeException {
		try {
			switch (type) {
			case INTEGER:
				this.value = new Long(getInteger());
				break;
			case REAL:
				this.value = new Double(getReal());
				break;
			case STRING:
				this.value = getString();
				break;
			case BOOLEAN:
				this.value = new Boolean(getBoolean());
				break;
			case DATE:
				this.value = getDate();
				break;
			case TIMESTAMP:
				this.value = new Timestamp(getTimestamp());
				break;
			case OBJECT:
				this.value = getJavaObject();
				break;
			case POINT:
				this.value = getPoint();
				break;
			case POINT3D:
				this.value = getPoint3D();
				break;
			case COLOR:
				this.value = getColor();
				break;
			case UUID:
				this.value = getUUID();
				break;
			case IMAGE:
				this.value = getImage();
				break;
			case POLYLINE:
				this.value = getPolyline();
				break;
			case POLYLINE3D:
				this.value = getPolyline3D();
				break;
			case URI:
				this.value = getURI();
				break;
			case URL:
				this.value = getURL();
				break;
			case INET_ADDRESS:
				this.value = getInetAddress();
				break;
			case ENUMERATION:
				this.value = getEnumeration();
				break;
			case TYPE:
				this.value = getJavaClass();
				break;
			default:
				throw new InvalidAttributeTypeException();
			}
		} catch (NumberFormatException ex) {
			throw new InvalidAttributeTypeException();
		} catch (AttributeNotInitializedException e) {
			this.value = type.getDefaultValue();
		}
		this.type = type;
	}

	@Override
	public boolean cast(AttributeType attrType) {
		boolean b = true;
		try {
			setType(attrType);
		} catch (InvalidAttributeTypeException ex) {
			this.value = attrType.getDefaultValue();
			b = false;
		}
		this.type = attrType;
		return b;
	}

	@Override
	public void castAndSet(AttributeType attrType, Object attrValue) {
		try {
			if (attrValue instanceof AttributeValue) {
				this.type = ((AttributeValue) attrValue).getType();
				try {
					this.value = ((AttributeValue) attrValue).getValue();
					this.assigned = true;
				} catch (AttributeNotInitializedException e) {
					this.value = attrType.getDefaultValue();
					this.assigned = true;
				}
			} else {
				this.type = attrType;
				this.value = attrType.cast(attrValue);
				this.assigned = true;
			}
			if (attrValue != null) {
				setType(attrType);
			} else {
				this.value = attrType.getDefaultValue();
				this.assigned = true;
			}
		} catch (InvalidAttributeTypeException ex) {
			this.value = attrType.getDefaultValue();
			this.assigned = true;
		}
	}

	@Pure
	@Override
	public Object getValue() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		assertAssigned();
		return this.value;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
	public final Class<?> getInternalStorageType() {
		switch (this.type) {
		case BOOLEAN:
			return Boolean.class;
		case COLOR:
			return Color.class;
		case DATE:
			return Date.class;
		case ENUMERATION:
			return Enum.class;
		case IMAGE:
			return Image.class;
		case INET_ADDRESS:
			return InetAddress.class;
		case INTEGER:
			return Long.class;
		case OBJECT:
			return Object.class;
		case POINT:
			return Point2D.class;
		case POINT3D:
			return Point3D.class;
		case POLYLINE:
			return Point2D[].class;
		case POLYLINE3D:
			return Point3D[].class;
		case REAL:
			return Double.class;
		case STRING:
			return String.class;
		case TIMESTAMP:
			return Timestamp.class;
		case TYPE:
			return Class.class;
		case URI:
			return URI.class;
		case URL:
			return URL.class;
		case UUID:
			return UUID.class;
		default:
		}
		return null;
	}

	@Override
	public void setValue(AttributeValue value) {
		this.type = value.getType();
		try {
			this.assigned = value.isAssigned();
			this.value = value.getValue();
		} catch (AttributeException e) {
			this.value = this.type.getDefaultValue();
		}
	}

	@Override
	public void setValue(Object value) {
		final AttributeType detectedType = AttributeType.fromValue(value);
		this.value = detectedType.cast(value);
		this.assigned = this.value != null;
	}

	/** Set this value with the content of the specified one.
	 *
	 * <p>The type of the attribute will be NOT detected from the type
	 * of the object. It means that it is not changed by this function.
	 * The given value must be compatible with internal representation
	 * of the attribute implementation.
	 *
	 * @param value is the raw value to put inside this attribute value.
	 */
	protected void setInternalValue(Object value) {
		this.value = value;
		this.assigned = this.value != null;
	}

	/** Set this value with the content of the specified one.
	 *
	 * <p>The type of the attribute will be NOT detected from the type
	 * of the object. It means that it will be equal to the given type,
	 * even if the given value is incompatible.
	 * The given value must be compatible with internal representation
	 * of the attribute implementation.
	 *
	 * @param value is the raw value to put inside this attribute value.
	 * @param type is the type of the value.
	 */
	protected void setInternalValue(Object value, AttributeType type) {
		this.value = value;
		this.assigned = this.value != null;
		this.type = type;
	}

	@Override
	public void setToDefault() {
		this.assigned = true;
		this.value = this.type.getDefaultValue();
	}

	@Override
	public void setToDefaultIfUninitialized() {
		if (!isAssigned()) {
			setToDefault();
		}
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
	public long getInteger() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case INTEGER:
				return ((Long) this.value).longValue();
			case TIMESTAMP:
				return ((Timestamp) this.value).longValue();
			case REAL:
				return ((Double) this.value).longValue();
			case STRING:
				return Long.parseLong((String) this.value);
			case DATE:
				return ((Date) this.value).getTime();
			case BOOLEAN:
				return ((Boolean) this.value).booleanValue() ? 1 : 0;
			case COLOR:
				return ((Color) this.value).getRGB();
			case OBJECT:
				if (this.value instanceof Number) {
					return ((Number) this.value).longValue();
				}
				break;
			case ENUMERATION:
				if (this.value instanceof Enum<?>) {
					return ((Enum<?>) this.value).ordinal();
				}
				break;
			case IMAGE:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case TYPE:
				break;
			default:
				throw new InvalidAttributeTypeException();
			}
		} catch (NumberFormatException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setInteger(int value) {
		this.value = new Long(value);
		this.type = AttributeType.INTEGER;
		this.assigned = true;
	}

	@Override
	public void setInteger(long value) {
		this.value = new Long(value);
		this.type = AttributeType.INTEGER;
		this.assigned = true;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
	public double getReal() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case INTEGER:
				return ((Long) this.value).doubleValue();
			case TIMESTAMP:
				return ((Timestamp) this.value).doubleValue();
			case REAL:
				return ((Double) this.value).doubleValue();
			case STRING:
				return Double.parseDouble((String) this.value);
			case DATE:
				return ((Date) this.value).getTime();
			case BOOLEAN:
				return ((Boolean) this.value).booleanValue() ? 1. : 0.;
			case COLOR:
				return ((Color) this.value).getRGB();
			case OBJECT:
				if (this.value instanceof Number) {
					return ((Number) this.value).doubleValue();
				}
				break;
			case ENUMERATION:
				if (this.value instanceof Enum<?>) {
					return ((Enum<?>) this.value).ordinal();
				}
				break;
			case IMAGE:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case TYPE:
			default:
				throw new InvalidAttributeTypeException();
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setReal(double value) {
		this.value = new Double(value);
		this.type = AttributeType.REAL;
		this.assigned = true;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
	public String getString() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case STRING:
				return (String) this.value;
			case BOOLEAN:
				return ((Boolean) this.value).toString();
			case COLOR:
				final Color col = (Color) this.value;
				return Integer.toString(col.getRed())
						+ ';' + col.getGreen()
						+ ';' + col.getBlue()
						+ ';' + col.getAlpha();
			case UUID:
				final UUID uuid = (UUID) this.value;
				return uuid.toString();
			case URL:
				final URL url = (URL) this.value;
				return url.toExternalForm();
			case URI:
				final URI uri = (URI) this.value;
				return uri.toASCIIString();
			case TIMESTAMP:
				final SimpleDateFormat tmformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
				return tmformat.format(new Date(((Timestamp) this.value).longValue()));
			case INTEGER:
				return ((Long) this.value).toString();
			case REAL:
				return ((Double) this.value).toString();
			case POINT:
				final Point2D<?, ?> pt2 = (Point2D<?, ?>) this.value;
				final StringBuilder buffer1 = new StringBuilder();
				buffer1.append(pt2.getX());
				buffer1.append(";"); //$NON-NLS-1$
				buffer1.append(pt2.getY());
				return buffer1.toString();
			case POINT3D:
				final Point3D pt3 = (Point3D) this.value;
				final StringBuilder buffer2 = new StringBuilder();
				buffer2.append(pt3.getX());
				buffer2.append(";"); //$NON-NLS-1$
				buffer2.append(pt3.getY());
				buffer2.append(";"); //$NON-NLS-1$
				buffer2.append(pt3.getZ());
				return buffer2.toString();
			case DATE:
				final SimpleDateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
				return dtformat.format((Date) this.value);
			case POLYLINE:
				final StringBuilder buffer3 = new StringBuilder();
				final Point2D<?, ?>[] lstpt2 = (Point2D<?, ?>[]) this.value;
				for (int i = 0; i < lstpt2.length; ++i) {
					if (lstpt2[i] != null) {
						if (buffer3.length() > 0) {
							buffer3.append(";"); //$NON-NLS-1$
						}
						buffer3.append(lstpt2[i].getX());
						buffer3.append(";"); //$NON-NLS-1$
						buffer3.append(lstpt2[i].getY());
					}
				}
				return buffer3.toString();
			case POLYLINE3D:
				final StringBuilder buffer4 = new StringBuilder();
				final Point3D[] lstpt3 = (Point3D[]) this.value;
				for (int i = 0; i < lstpt3.length; ++i) {
					if (lstpt3[i] != null) {
						if (buffer4.length() > 0) {
							buffer4.append(";"); //$NON-NLS-1$
						}
						buffer4.append(lstpt3[i].getX());
						buffer4.append(";"); //$NON-NLS-1$
						buffer4.append(lstpt3[i].getY());
						buffer4.append(";"); //$NON-NLS-1$
						buffer4.append(lstpt3[i].getZ());
					}
				}
				return buffer4.toString();
			case ENUMERATION:
				final StringBuilder buffer5 = new StringBuilder();
				final Enum<?> enumeration = (Enum<?>) this.value;
				final Class<?> enumerationType = enumeration.getDeclaringClass();
				final String typeName = enumerationType.getCanonicalName();
				buffer5.append(typeName);
				buffer5.append("."); //$NON-NLS-1$
				buffer5.append(((Enum<?>) this.value).name());
				return buffer5.toString();
			case TYPE:
				return ((Class<?>) this.value).getCanonicalName();
			case INET_ADDRESS:
			case OBJECT:
			case IMAGE:
				return this.value.toString();
			default:
				throw new InvalidAttributeTypeException();
			}
		} catch (ClassCastException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setString(String value) {
		this.value = value;
		this.type = AttributeType.STRING;
		this.assigned = this.value != null;
	}

	/** Parse a date according to the specified locale.
	 */
	private static Date extractDate(String text, Locale locale) {
		DateFormat fmt;
		for (int style = 0; style <= 3; ++style) {
			// Date and time parsing
			for (int style2 = 0; style2 <= 3; ++style2) {
				fmt = DateFormat.getDateTimeInstance(style, style2, locale);
				try {
					return fmt.parse(text);
				} catch (ParseException exception) {
					//
				}
			}
			// Date only parsing
			fmt = DateFormat.getDateInstance(style, locale);
			try {
				return fmt.parse(text);
			} catch (ParseException exception) {
				//
			}
			// Time only parsing
			fmt = DateFormat.getTimeInstance(style, locale);
			try {
				return fmt.parse(text);
			} catch (ParseException exception) {
				//
			}
		}
		return null;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public Date getDate() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case DATE:
				return (Date) ((Date) this.value).clone();
			case REAL:
				return new Date(((Double) this.value).longValue());
			case INTEGER:
				return new Date(((Long) this.value).longValue());
			case TIMESTAMP:
				return new Date(((Timestamp) this.value).longValue());
			case STRING:
				final String txt = (String) this.value;
				DateFormat fmt;
				Date dt;

				dt = extractDate(txt, Locale.getDefault());
				if (dt != null) {
					return dt;
				}

				dt = extractDate(txt, Locale.US);
				if (dt != null) {
					return dt;
				}

				dt = extractDate(txt, Locale.UK);
				if (dt != null) {
					return dt;
				}

				try {
					fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
					return fmt.parse(txt);
				} catch (ParseException exception) {
					//
				}
				fmt = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
				return fmt.parse(txt);
			case OBJECT:
				if (this.value instanceof Date) {
					return (Date) this.value;
				}
				if (this.value instanceof Calendar) {
					return ((Calendar) this.value).getTime();
				}
				if (this.value instanceof Number) {
					return new Date(((Number) this.value).longValue());
				}
				break;
			case BOOLEAN:
			case COLOR:
			case IMAGE:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (Exception e) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setDate(Date value) {
		this.value = value;
		this.type = AttributeType.DATE;
		this.assigned = this.value != null;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public boolean getBoolean() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case BOOLEAN:
				return ((Boolean) this.value).booleanValue();
			case STRING:
				// Do not use the function Boolean.parseBoolean() because
				// it replies false when the string does not contains "true"
				if (TRUE_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return true;
				}
				if (YES_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return true;
				}
				if (OUI_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return true;
				}
				if (T_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return true;
				}
				if (Y_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return true;
				}
				if (O_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return true;
				}

				if (FALSE_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return false;
				}
				if (NO_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return false;
				}
				if (NON_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return false;
				}
				if (F_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return false;
				}
				if (N_CONSTANT.compareToIgnoreCase((String) this.value) == 0) {
					return false;
				}

				break;
			case INTEGER:
				return ((Long) this.value).longValue() != 0;
			case TIMESTAMP:
				return ((Timestamp) this.value).longValue() != 0;
			case REAL:
				return !MathUtil.isZero(((Double) this.value).doubleValue());
			case OBJECT:
				if (this.value instanceof Boolean) {
					return ((Boolean) this.value).booleanValue();
				}
				break;
			case COLOR:
			case DATE:
			case IMAGE:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setBoolean(boolean value) {
		this.value = new Boolean(value);
		this.type = AttributeType.BOOLEAN;
		this.assigned = true;
	}

	@Pure
	@Override
	public boolean isObjectValue() {
		return !isBaseType();
	}

	@Pure
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getJavaObject() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		if (!isObjectValue()) {
			throw new InvalidAttributeTypeException();
		}
		if (isNotAssignedOrNull()) {
			return null;
		}
		try {
			return (T) this.value;
		} catch (ClassCastException exception) {
			throw new InvalidAttributeTypeException();
		}
	}

	@Override
	public void setJavaObject(Object value) {
		this.value = value;
		this.type = AttributeType.OBJECT;
		this.assigned = true;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public long getTimestamp() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case INTEGER:
				return ((Long) this.value).longValue();
			case TIMESTAMP:
				return ((Timestamp) this.value).longValue();
			case REAL:
				return ((Double) this.value).longValue();
			case STRING:
				return Long.parseLong((String) this.value);
			case DATE:
				return ((Date) this.value).getTime();
			case BOOLEAN:
				return ((Boolean) this.value).booleanValue() ? 1 : 0;
			case COLOR:
				return ((Color) this.value).getRGB();
			case OBJECT:
				if (this.value instanceof Number) {
					return ((Number) this.value).longValue();
				}
				if (this.value instanceof Date) {
					return ((Date) this.value).getTime();
				}
				if (this.value instanceof Calendar) {
					return ((Calendar) this.value).getTimeInMillis();
				}
				break;
			case IMAGE:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setTimestamp(long value) {
		this.value = new Timestamp(value);
		this.type = AttributeType.TIMESTAMP;
		this.assigned = true;
	}

	private static Point3D parsePoint3D(String text, boolean isStrict) {
		final String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && comp.length != 3) {
			return null;
		}
		//FIXME: fix code. new Point3fp();
		final Point3D pt3 = null;
		//		if (comp.length>0) pt3.setX(Float.parseFloat(comp[0]));
		//		if (comp.length>1) pt3.setY(Float.parseFloat(comp[1]));
		//		if (comp.length>2) pt3.setZ(Float.parseFloat(comp[2]));
		return pt3;
	}

	@Pure
	@Override
	public Point3D getPoint3D() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		//FIXME: fix code
		//		try {
		//			assertAssignedAndNotNull();
		//			switch(this.type) {
		//			case COLOR:
		//				Color col = (Color)this.value;
		//				return new Point3f(col.getRed(), col.getGreen(), col.getBlue());
		//			case REAL:
		//				Double flt = (Double)this.value;
		//				return new Point3f(flt.floatValue(), 0, 0);
		//			case INTEGER:
		//				Long lg = (Long)this.value;
		//				return new Point3f(lg.floatValue(), 0, 0);
		//			case TIMESTAMP:
		//				Timestamp ts = (Timestamp)this.value;
		//				return new Point3f(ts.floatValue(), 0, 0);
		//			case DATE:
		//				Date dt = (Date)this.value;
		//				return new Point3f(dt.getTime(), 0, 0);
		//			case POINT:
		//				Point2D pt2 = (Point2D)this.value;
		//				return new Point3f(pt2.getX(), pt2.getY(), 0);
		//			case POINT3D:
		//				return ((Point3D)this.value).clone();
		//			case STRING:
		//				return parsePoint3D((String)this.value, false);
		//			case OBJECT:
		//				if (this.value instanceof Tuple3D) {
		//					Tuple3D<?> t3 = (Tuple3D<?>)this.value;
		//					return new Point3f(t3.getX(), t3.getY(), t3.getZ());
		//				}
		//				if (this.value instanceof Tuple2D) {
		//					Tuple2D<?> t2 = (Tuple2D<?>)this.value;
		//					return new Point3f(t2.getX(), t2.getY(), 0);
		//				}
		//				break;
		//			case BOOLEAN:
		//			case IMAGE:
		//			case POLYLINE:
		//			case POLYLINE3D:
		//			case URI:
		//			case URL:
		//			case UUID:
		//			case INET_ADDRESS:
		//			case ENUMERATION:
		//			case TYPE:
		//			default:
		//			}
		//		}
		//		catch(ClassCastException exception) {
		//			//
		//		}
		//		catch(NumberFormatException exception) {
		//			//
		//		}
		//		throw new InvalidAttributeTypeException();
		return null;
	}

	@Override
	public void setPoint3D(Point3D value) {
		this.value = value;
		this.type = AttributeType.POINT3D;
		this.assigned = this.value != null;
	}

	@Override
	public void setPoint3D(float x, float y, float z) {
		//FIXME: Fix code: new Point3f(x, y, z);
		this.value = null;
		this.type = AttributeType.POINT3D;
		this.assigned = true;
	}

	private static Point2D<?, ?> parsePoint(String text, boolean isStrict) {
		final String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && comp.length != 2) {
			return null;
		}
		final float x;
		final float y;
		if (comp.length > 0) {
			x = Float.parseFloat(comp[0]);
		} else {
			x = 0;
		}
		if (comp.length > 1) {
			y = Float.parseFloat(comp[1]);
		} else {
			y = 0;
		}
		return new Point2d(x, y);

	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
	public Point2D<?, ?> getPoint() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case COLOR:
				final Color col = (Color) this.value;
				return new Point2d(col.getRed(), col.getGreen());
			case REAL:
				final Double flt = (Double) this.value;
				return new Point2d(flt.floatValue(), 0f);
			case INTEGER:
				final Long lg = (Long) this.value;
				return new Point2d(lg.floatValue(), 0f);
			case TIMESTAMP:
				final Timestamp ts = (Timestamp) this.value;
				return new Point2d(ts.floatValue(), 0f);
			case DATE:
				final Date dt = (Date) this.value;
				return new Point2d(dt.getTime(), 0f);
			case POINT:
				return new Point2d(((Point2D<?, ?>) this.value).getX(),
						((Point2D<?, ?>) this.value).getY());
			case POINT3D:
				final Point3D pt3 = (Point3D) this.value;
				return new Point2d(pt3.getX(), pt3.getY());
			case STRING:
				return parsePoint((String) this.value, false);
			case OBJECT:
				if (this.value instanceof Tuple3D<?>) {
					final Tuple3D<?> t3 = (Tuple3D<?>) this.value;
					return new Point2d(t3.getX(), t3.getY());
				}
				if (this.value instanceof Tuple2D<?>) {
					final Tuple2D<?> t2 = (Tuple2D<?>) this.value;
					return new Point2d(t2.getX(), t2.getY());
				}
				break;
			case BOOLEAN:
			case IMAGE:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setPoint(Point2D<?, ?> value) {
		this.value = value;
		this.type = AttributeType.POINT;
		this.assigned = this.value != null;
	}

	@Override
	public void setPoint(float x, float y) {
		this.value = new Point2d(x, y);
		this.type = AttributeType.POINT;
		this.assigned = true;
	}

	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:booleanexpressioncomplexity"})
	private static Color parseColor(String text, boolean isStrict) {
		final String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && comp.length != 3 && comp.length != 4) {
			return null;
		}
		int red = 0;
		int green = 0;
		int blue = 0;
		int alpha = 255;
		if (comp.length > 0) {
			red = Integer.parseInt(comp[0]);
		}
		if (comp.length > 1) {
			green = Integer.parseInt(comp[1]);
		}
		if (comp.length > 2) {
			blue = Integer.parseInt(comp[2]);
		}
		if (comp.length > 3) {
			alpha = Integer.parseInt(comp[3]);
		}
		if ((red < 256) && (red >= 0) && (green < 256) && (green >= 0) && (blue < 256) && (blue >= 0)
				&& (alpha < 256) && (alpha >= 0)) {
			return VectorToolkit.color(red, green, blue, alpha);
		}
		return null;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
	public Color getColor() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case COLOR:
				final Color col = (Color) this.value;
				return VectorToolkit.color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
			case POINT:
				final Point2D<?, ?> pt2 = (Point2D<?, ?>) this.value;
				return VectorToolkit.color((float) pt2.getX(), (float) pt2.getY(), 0f, 1f);
			case POINT3D:
				final Point3D pt3 = (Point3D) this.value;
				return VectorToolkit.color((float) pt3.getX(), (float) pt3.getY(), (float) pt3.getZ(), 1f);
			case STRING:
				final Color color = parseColor((String) this.value, false);
				if (color != null) {
					return color;
				}
				break;
			case INTEGER:
				return VectorToolkit.color(((Long) this.value).intValue());
			case TIMESTAMP:
				return VectorToolkit.color(((Timestamp) this.value).intValue());
			case REAL:
				return VectorToolkit.color(((Double) this.value).intValue());
			case DATE:
				return VectorToolkit.color((int) ((Date) this.value).getTime());
			case OBJECT:
				if (this.value instanceof Color) {
					return (Color) this.value;
				}
				if (this.value instanceof Number) {
					return VectorToolkit.color(((Number) this.value).intValue());
				}
				if (this.value instanceof Date) {
					return VectorToolkit.color((int) ((Date) this.value).getTime());
				}
				if (this.value instanceof Calendar) {
					return VectorToolkit.color((int) ((Calendar) this.value).getTimeInMillis());
				}
				break;
			case BOOLEAN:
			case IMAGE:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setColor(Color color) {
		this.value = color;
		this.type = AttributeType.COLOR;
		this.assigned = this.value != null;
	}

	@Override
	public void setColor(float red, float green, float blue) {
		this.value = VectorToolkit.color(red, green, blue, 1f);
		this.type = AttributeType.COLOR;
		this.assigned = true;
	}

	@Override
	public void setColor(float red, float green, float blue, float alpha) {
		this.value = VectorToolkit.color(red, green, blue, alpha);
		this.type = AttributeType.COLOR;
		this.assigned = true;
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void setColor(int red, int green, int blue) {
		this.value = VectorToolkit.color(red, green, blue, 255);
		this.type = AttributeType.COLOR;
		this.assigned = true;
	}

	@Override
	public void setColor(int red, int green, int blue, int alpha) {
		this.value = VectorToolkit.color(red, green, blue, alpha);
		this.type = AttributeType.COLOR;
		this.assigned = true;
	}

	private static UUID parseUUID(String text) {
		try {
			final URI uri = new URI(text);
			if ("uuid".equalsIgnoreCase(uri.getScheme())) { //$NON-NLS-1$
				return UUID.fromString(uri.getHost());
			}
		} catch (Throwable exception) {
			//
		}
		try {
			return UUID.fromString(text);
		} catch (Throwable exception) {
			//
		}
		return null;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	public UUID getUUID() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case UUID:
				final UUID id = (UUID) this.value;
				return new UUID(id.getMostSignificantBits(), id.getLeastSignificantBits());
			case URI:
				final URI uri = (URI) this.value;
				if ("uuid".equalsIgnoreCase(uri.getScheme())) { //$NON-NLS-1$
					try {
						return UUID.fromString(uri.getHost());
					} catch (AssertionError e) {
						throw e;
					} catch (Throwable exception) {
						//
					}
				}
				break;
			case OBJECT:
				if (this.value instanceof UUID) {
					return (UUID) this.value;
				}
				if (this.value instanceof URI
						&& "uuid".equalsIgnoreCase(((URI) this.value).getScheme())) { //$NON-NLS-1$
					try {
						return UUID.fromString(((URI) this.value).getHost());
					} catch (AssertionError e) {
						throw e;
					} catch (Throwable exception) {
						//
					}
				}
				break;
			case STRING:
			case BOOLEAN:
			case COLOR:
			case DATE:
			case IMAGE:
			case INTEGER:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case REAL:
			case TIMESTAMP:
			case URL:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
				break;
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		}
		if (this.value == null) {
			return (UUID) AttributeType.UUID.getDefaultValue();
		}
		final String s = this.value.toString();
		if (s == null || "".equals(s)) { //$NON-NLS-1$
			return (UUID) AttributeType.UUID.getDefaultValue();
		}
		try {
			return UUID.fromString(s);
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			//
		}
		return UUID.nameUUIDFromBytes(s.getBytes());
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	public URL getURL() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case URL:
				return new URL(((URL) this.value).toExternalForm());
			case URI:
				return ((URI) this.value).toURL();
			case STRING:
				return new URL((String) this.value);
			case OBJECT:
				if (this.value instanceof URL) {
					return (URL) this.value;
				}
				if (this.value instanceof URI) {
					return ((URI) this.value).toURL();
				}
				break;
			case INET_ADDRESS:
				return new URL(DEFAULT_SCHEME.name(), ((InetAddress) this.value).getHostAddress(), ""); //$NON-NLS-1$
			case UUID:
			case BOOLEAN:
			case COLOR:
			case DATE:
			case IMAGE:
			case INTEGER:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case REAL:
			case TIMESTAMP:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (MalformedURLException exception) {
			//
		}
		if (this.value == null) {
			return (URL) AttributeType.URL.getDefaultValue();
		}
		final String s = this.value.toString();
		if (s == null) {
			return (URL) AttributeType.URL.getDefaultValue();
		}
		try {
			return new URL(s);
		} catch (MalformedURLException exception) {
			throw new InvalidAttributeTypeException();
		}
	}

	private static URI parseURI(String text) {
		try {
			final URI uri = new URI(text);
			if (uri.getScheme() != null && !uri.getScheme().isEmpty()) {
				return uri;
			}
		} catch (Throwable exception) {
			//
		}
		return null;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	public URI getURI() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case URL:
				return ((URL) this.value).toURI();
			case URI:
				return new URI(((URI) this.value).toASCIIString());
			case STRING:
				return new URI((String) this.value);
			case UUID:
				return new URI("uuid:" + ((UUID) this.value).toString()); //$NON-NLS-1$
			case OBJECT:
				if (this.value instanceof URI) {
					return (URI) this.value;
				}
				if (this.value instanceof URL) {
					return ((URL) this.value).toURI();
				}
				if (this.value instanceof UUID) {
					return new URI("uuid:" + ((UUID) this.value).toString()); //$NON-NLS-1$
				}
				break;
			case INET_ADDRESS:
				return new URI(DEFAULT_SCHEME.name(), ((InetAddress) this.value).getHostAddress(), ""); //$NON-NLS-1$
			case BOOLEAN:
			case COLOR:
			case DATE:
			case IMAGE:
			case INTEGER:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case REAL:
			case TIMESTAMP:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (URISyntaxException exception) {
			//
		}
		if (this.value == null) {
			return (URI) AttributeType.URI.getDefaultValue();
		}
		final String s = this.value.toString();
		if (s == null) {
			return (URI) AttributeType.URI.getDefaultValue();
		}
		try {
			return new URI(s);
		} catch (URISyntaxException exception) {
			throw new InvalidAttributeTypeException();
		}
	}

	@Override
	public void setUUID(UUID u) {
		this.value = (u == null) ? AttributeType.UUID.getDefaultValue() : u;
		this.type = AttributeType.UUID;
		this.assigned = this.value != null;
	}

	/**
	 * Set the value of this metadata.
	 *
	 * @param id identifier.
	 */
	public void setUUID(String id) {
		try {
			this.value = (id != null) ? UUID.fromString(id) : null;
		} catch (Throwable exception) {
			assert id != null;
			this.value = UUID.nameUUIDFromBytes(id.getBytes());
		}
		this.type = AttributeType.UUID;
		this.assigned = this.value != null;
	}

	@Override
	public void setURL(URL u) {
		this.value = (u == null) ? AttributeType.URL.getDefaultValue() : u;
		this.type = AttributeType.URL;
		this.assigned = this.value != null;
	}

	/**
	 * Set the value of this metadata.
	 *
	 * @param url the url.
	 */
	public void setURL(String url) {
		try {
			this.value = (url != null) ? new URL(url) : null;
		} catch (Throwable exception) {
			this.value = null;
		}
		this.type = AttributeType.URL;
		this.assigned = this.value != null;
	}

	@Override
	public void setURI(URI u) {
		this.value = (u == null) ? AttributeType.URI.getDefaultValue() : u;
		this.type = AttributeType.URI;
		this.assigned = this.value != null;
	}

	/**
	 * Set the value of this metadata.
	 *
	 * @param uri the uri.
	 */
	public void setURI(String uri) {
		try {
			this.value = (uri != null) ? new URI(uri) : null;
		} catch (Throwable exception) {
			this.value = null;
		}
		this.type = AttributeType.URI;
		this.assigned = this.value != null;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	public Image getImage() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssigned();
			switch (this.type) {
			case IMAGE:
				assertAssignedAndNotNull();
				return (Image) this.value;
			case OBJECT:
				if ((this.value == null) || (this.value instanceof Image)) {
					return (Image) this.value;
				}
				break;
			case BOOLEAN:
			case COLOR:
			case DATE:
			case INTEGER:
			case POINT:
			case POINT3D:
			case POLYLINE:
			case POLYLINE3D:
			case REAL:
			case STRING:
			case TIMESTAMP:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setImage(Image image) {
		this.value = image;
		this.type = AttributeType.IMAGE;
		this.assigned = true;
	}

	private static Point3D[] parsePolyline3D(String text, boolean isStrict) {
		final String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && (comp.length % 3) != 0) {
			return null;
		}
		final int fullPoints = comp.length / 3;
		final boolean addPt = fullPoints * 3 != comp.length;
		final Point3D[] tab = new Point3D[addPt ? fullPoints + 1 : fullPoints];
		for (int i = 2, j = 0; (i < comp.length) && (j < tab.length); i += 3, ++j) {
			// FIXME: fix code: new Point3f(
			//					Float.parseFloat(comp[i-2]),
			//					Float.parseFloat(comp[i-1]),
			//					Float.parseFloat(comp[i]));
			tab[j] = null;
		}
		if (addPt) {
			final float x = Float.parseFloat(comp[fullPoints * 3]);
			final int idx = fullPoints * 3 + 1;
			final float y = idx < comp.length ? Float.parseFloat(comp[idx]) : 0;
			// FIXME: Fix code new Point3f(x, y, 0);
			tab[tab.length - 1]  = null;
		}
		return tab;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	public Point3D[] getPolyline3D() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case POINT:
				final Point2D<?, ?> pt2 = (Point2D<?, ?>) this.value;
				//TODO: Fix code: new Point3fp(pt2.getX(), pt2.getY(), 0f)
				return new Point3D[] {};
			case POINT3D:
				return new Point3D[] {
						((Point3D) this.value).clone(),
				};
			case POLYLINE:
				final Point2D<?, ?>[] current = (Point2D<?, ?>[]) this.value;
				final Point3D[] tab = new Point3D[current.length];
				for (int i = 0; i < current.length; ++i) {
					//					TODO: Fix code tab[i] = new Point3f(
					//								current[i].getX(),
					//								current[i].getY(),
					//								0f);
				}
				return tab;
			case POLYLINE3D:
				return (Point3D[]) this.value;
			case STRING:
				return parsePolyline3D((String) this.value, false);
			case OBJECT:
				if (this.value instanceof Tuple2D<?>) {
					final Tuple2D<?> t2 = (Tuple2D<?>) this.value;
					// FIXME: Fixcode: new Point3f(t2.getX(), t2.getY(), 0)
					return new Point3D[] {};
				} else if (this.value instanceof Tuple3D<?>) {
					final Tuple3D<?> t2 = (Tuple3D<?>) this.value;
					// FIXME: Fixcode: new Point3f(t2.getX(), t2.getY(), t2.getZ())
					return new Point3D[] {};
				} else if (this.value.getClass().isArray()) {
					final Class<?> elementType = this.value.getClass().getComponentType();
					if (Point3D.class.equals(elementType)) {
						return (Point3D[]) this.value;
					}
					final int size = Array.getLength(this.value);
					if (Tuple3D.class.isAssignableFrom(elementType)) {
						final Point3D[] pa3 = new Point3D[size];
						for (int i = 0; i < pa3.length; ++i) {
							final Tuple3D<?> t = (Tuple3D<?>) Array.get(this.value, i);
							// FIXME: Fixcode: new Point3f(t);
							pa3[i] = null;
						}
						return pa3;
					}
					if (Tuple2D.class.isAssignableFrom(elementType)) {
						final Point3D[] pa3 = new Point3D[size];
						for (int i = 0; i < pa3.length; ++i) {
							final Tuple2D<?> t = (Tuple2D<?>) Array.get(this.value, i);
							// FIXME: Fixcode: new Point3f(t.getX(), t.getY(), 0);
							pa3[i] = null;
						}
						return pa3;
					}
				}
				break;
			case BOOLEAN:
			case COLOR:
			case DATE:
			case IMAGE:
			case INTEGER:
			case REAL:
			case TIMESTAMP:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setPolyline3D(Point3D... value) {
		this.value = value;
		this.type = AttributeType.POLYLINE3D;
		this.assigned = this.value != null;
	}

	@Override
	public void setPolyline3D(Collection<? extends Point3D> value) {
		if (value == null) {
			this.value = null;
		} else {
			final Point3D[] tab = new Point3D[value.size()];
			value.toArray(tab);
			this.value = tab;
		}
		this.type = AttributeType.POLYLINE3D;
		this.assigned = this.value != null;
	}

	@Override
	public void addToPolyline3D(Point3D... pts) {
		final Point3D[] tab;
		if (this.value instanceof Point3D[]) {
			final int size = ((Point3D[]) this.value).length;
			tab = new Point3D[size + pts.length];
			System.arraycopy(this.value, 0, tab, 0, size);
			System.arraycopy(pts, 0, tab, size, pts.length);
		} else {
			tab = pts;
		}
		this.value = tab;
		this.assigned = this.value != null;
		this.type = AttributeType.POLYLINE3D;
	}

	@Override
	public void addToPolyline3D(Collection<? extends Point3D> pts) {
		final Point3D[] tab;
		if (this.value instanceof Point3D[]) {
			final int size = ((Point3D[]) this.value).length;
			tab = new Point3D[size + pts.size()];
			System.arraycopy(this.value, 0, tab, 0, size);
			System.arraycopy(pts.toArray(), 0, tab, size, pts.size());
		} else {
			tab = new Point3D[pts.size()];
			pts.toArray(tab);
		}
		this.value = tab;
		this.assigned = this.value != null;
		this.type = AttributeType.POLYLINE3D;
	}

	private static Point2D<?, ?>[] parsePolyline(String text, boolean isStrict) {
		final String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && (comp.length % 2) != 0) {
			return null;
		}
		final int fullPoints = comp.length / 2;
		final boolean addPt = fullPoints * 2 != comp.length;
		final Point2D<?, ?>[] tab = new Point2D[addPt ? fullPoints + 1 : fullPoints];
		for (int i = 1, j = 0; (i < comp.length) && (j < fullPoints); i += 2, ++j) {
			// FIXME: Fixcode: tab[j] = new Point2f(
			//					Float.parseFloat(comp[i-1]),
			//					Float.parseFloat(comp[i]));
		}
		if (addPt) {
			// FIXME: Fixcode: tab[tab.length-1]  = new Point2f(
			//					Float.parseFloat(comp[comp.length-1]),
			//					0);
		}
		return tab;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	public Point2D<?, ?>[] getPolyline() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case POINT:
				return new Point2D[] {
					new Point2d(((Point2D<?, ?>) this.value).getX(), ((Point2D<?, ?>) this.value).getY()),
				};
			case POINT3D:
				final Point3D pt3 = (Point3D) this.value;
				return new Point2D[] {
					new Point2d(pt3.getX(), pt3.getY()),
				};
			case POLYLINE:
				return (Point2D[]) this.value;
			case POLYLINE3D:
				final Point3D[] current = (Point3D[]) this.value;
				final Point2D<?, ?>[] tab = new Point2D<?, ?>[current.length];
				for (int i = 0; i < current.length; ++i) {
					tab[i] = new Point2d(
							current[i].getX(),
							current[i].getY());
				}
				return tab;
			case STRING:
				return parsePolyline((String) this.value, false);
			case OBJECT:
				if (this.value instanceof Tuple2D) {
					return new Point2D[] {
						new Point2d(((Tuple2D<?>) this.value).getX(), ((Tuple2D<?>) this.value).getY()),
					};
				} else if (this.value instanceof Tuple3D) {
					final Tuple3D<?> t3 = (Tuple3D<?>) this.value;
					return new Point2D[] {
						new Point2d(t3.getX(), t3.getY()),
					};
				} else if (this.value instanceof Point2D[]) {
					return (Point2D[]) this.value;
				} else if (this.value instanceof Tuple2D[]) {
					final Tuple2D<?>[] ta2 = (Tuple2D[]) this.value;
					final Point2D<?, ?>[] pa2 = new Point2D[ta2.length];
					for (int i = 0; i < pa2.length; ++i) {
						pa2[i] = new Point2d(ta2[i]);
					}
				} else if (this.value instanceof Tuple3D[]) {
					final Tuple3D<?>[] ta3 = (Tuple3D[]) this.value;
					final Point2D<?, ?>[] pa2 = new Point2D<?, ?>[ta3.length];
					for (int i = 0; i < pa2.length; ++i) {
						pa2[i] = new Point2d(ta3[i].getX(), ta3[i].getY());
					}
				}
				break;
			case BOOLEAN:
			case COLOR:
			case DATE:
			case IMAGE:
			case INTEGER:
			case REAL:
			case TIMESTAMP:
			case URI:
			case URL:
			case UUID:
			case INET_ADDRESS:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setPolyline(Point2D<?, ?>... value) {
		this.value = value;
		this.type = AttributeType.POLYLINE;
		this.assigned = this.value != null;
	}

	@Override
	public void setPolyline(Collection<? extends Point2D<?, ?>> value) {
		if (value == null) {
			this.value = null;
		} else {
			final Point2D<?, ?>[] tab = new Point2D<?, ?>[value.size()];
			value.toArray(tab);
			this.value = tab;
		}
		this.value = value;
		this.type = AttributeType.POLYLINE;
		this.assigned = this.value != null;
	}

	@Override
	public void addToPolyline(Point2D<?, ?>... pts) {
		final Point2D<?, ?>[] tab;
		if (this.value instanceof Point2D[]) {
			final int size = ((Point2D<?, ?>[]) this.value).length;
			tab = new Point2D[size + pts.length];
			System.arraycopy(this.value, 0, tab, 0, size);
			System.arraycopy(this.value, 0, tab, size, pts.length);
		} else {
			tab = pts;
		}
		this.value = tab;
		this.assigned = this.value != null;
		this.type = AttributeType.POLYLINE;
	}

	@Override
	public void addToPolyline(Collection<? extends Point2D<?, ?>> pts) {
		final Point2D<?, ?>[] tab;
		if (this.value instanceof Point2D[]) {
			final int size = ((Point2D<?, ?>[]) this.value).length;
			tab = new Point2D[size + pts.size()];
			System.arraycopy(this.value, 0, tab, 0, size);
			System.arraycopy(this.value, 0, tab, size, pts.size());
		} else {
			tab = new Point2D[pts.size()];
			pts.toArray(tab);
		}
		this.value = tab;
		this.assigned = this.value != null;
		this.type = AttributeType.POLYLINE;
	}

	@Pure
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	public InetAddress getInetAddress() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case INET_ADDRESS:
				return (InetAddress) this.value;
			case STRING:
				try {
					final InetAddress adr = InetAddress.getByName(this.value.toString());
					if (adr != null) {
						return adr;
					}
				} catch (Throwable exception) {
					//
				}
				break;
			case OBJECT:
				if (this.value instanceof InetAddress) {
					return (InetAddress) this.value;
				}
				if (this.value instanceof InetSocketAddress) {
					return ((InetSocketAddress) this.value).getAddress();
				}
				if (this.value != null) {
					final InetAddress adr;
					try {
						adr = InetAddress.getByName(this.value.toString());
						if (adr != null) {
							return adr;
						}
					} catch (Throwable exception) {
						//
					}
				} else {
					return null;
				}
				break;
			case URI:
				final URI uri = (URI) this.value;
				return InetAddress.getByName(uri.getHost());
			case URL:
				final URL url = (URL) this.value;
				return InetAddress.getByName(url.getHost());
			case COLOR:
			case POINT:
			case POINT3D:
			case INTEGER:
			case TIMESTAMP:
			case REAL:
			case DATE:
			case BOOLEAN:
			case IMAGE:
			case POLYLINE:
			case POLYLINE3D:
			case UUID:
			case ENUMERATION:
			case TYPE:
			default:
			}
		} catch (ClassCastException exception) {
			//
		} catch (NumberFormatException exception) {
			//
		} catch (UnknownHostException e) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setInetAddress(InetAddress address) {
		this.value = address;
		this.type = AttributeType.INET_ADDRESS;
		this.assigned = this.value != null;
	}

	/**
	 * Set the value of this metadata.
	 *
	 * @param address the address.
	 */
	public void setInetAddress(InetSocketAddress address) {
		this.value = address.getAddress();
		this.type = AttributeType.INET_ADDRESS;
		this.assigned = this.value != null;
	}

	/**
	 * Set the value of this metadata.
	 *
	 * @param hostname the hostname
	 */
	public void setInetAddress(String hostname) {
		this.type = AttributeType.INET_ADDRESS;
		try {
			this.value = InetAddress.getByName(hostname);
		} catch (Throwable exception) {
			this.value = null;
		}
		this.assigned = this.value != null;
	}

	@Pure
	@Override
	@SuppressWarnings({"unchecked", "rawtypes", "checkstyle:cyclomaticcomplexity"})
	public Enum<?> getEnumeration() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		if (this.value == null) {
			return null;
		}
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case ENUMERATION:
				return (Enum<?>) this.value;
			case STRING:
				final int index = ((String) this.value).lastIndexOf('.');
				if (index >= 0) {
					final String classname = ((String) this.value).substring(0, index);
					final String enumName = ((String) this.value).substring(index + 1);
					final Class classType = Class.forName(classname);
					if (Enum.class.isAssignableFrom(classType)) {
						return Enum.valueOf(classType, enumName);
					}
				}
				break;
			case OBJECT:
				if (this.value instanceof Enum<?>) {
					return (Enum<?>) this.value;
				}
				break;
			case REAL:
			case INTEGER:
				break;
			case INET_ADDRESS:
			case COLOR:
			case POINT:
			case POINT3D:
			case TIMESTAMP:
			case DATE:
			case BOOLEAN:
			case IMAGE:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case TYPE:
			default:
			}
		} catch (Throwable exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public <T extends Enum<T>> T getEnumeration(Class<T> type) throws InvalidAttributeTypeException,
			AttributeNotInitializedException {
		if (this.value == null) {
			return null;
		}
		try {
			assertAssignedAndNotNull();
			switch (this.type) {
			case ENUMERATION:
				return type.cast(this.value);
			case STRING:
				final int index = ((String) this.value).lastIndexOf('.');
				if (index >= 0) {
					final String classname = ((String) this.value).substring(0, index);
					final String enumName = ((String) this.value).substring(index + 1);
					final Class<?> classType = Class.forName(classname);
					assert type.equals(classType);
					return Enum.valueOf(type, enumName);
				}
				break;
			case OBJECT:
				if (this.value instanceof Enum<?>) {
					return type.cast(this.value);
				}
				break;
			case REAL:
			case INTEGER:
				break;
			case INET_ADDRESS:
			case COLOR:
			case POINT:
			case POINT3D:
			case TIMESTAMP:
			case DATE:
			case BOOLEAN:
			case IMAGE:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			case TYPE:
			default:
			}
		} catch (Throwable exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setEnumeration(Enum<?> enumConstant) {
		this.value = enumConstant;
		this.type = AttributeType.ENUMERATION;
		this.assigned = this.value != null;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public Class<?> getJavaClass() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		assertAssignedAndNotNull();
		try {
			switch (this.type) {
			case TYPE:
			case OBJECT:
				return (Class<?>) this.value;
			case STRING:
				return Class.forName((String) this.value);
			case ENUMERATION:
			case REAL:
			case INTEGER:
			case INET_ADDRESS:
			case COLOR:
			case POINT:
			case POINT3D:
			case TIMESTAMP:
			case DATE:
			case BOOLEAN:
			case IMAGE:
			case POLYLINE:
			case POLYLINE3D:
			case URI:
			case URL:
			case UUID:
			default:
			}
		} catch (Throwable exception) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	@Override
	public void setJavaClass(Class<?> type) {
		this.value = type;
		this.type = AttributeType.TYPE;
		this.assigned = this.value != null;
	}

	@Pure
	@Override
	public boolean isAssigned() {
		return this.assigned;
	}

	@Override
	public void uninitializeValue() {
		this.value = null;
		this.assigned = false;
	}

	@Override
	public boolean flush() {
		return true;
	}

	@Pure
	@Override
	public Comparator<? extends AttributeValue> valueComparator() {
		return new AttributeValueComparator();
	}

}

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
import java.util.UUID;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.math.generic.Tuple2D;
import org.arakhne.afc.math.generic.Tuple3D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.VectorToolkit;

/**
 * This class contains an attribute value.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeValueImpl implements AttributeValue, AttributeConstants {

	/** Replies the best attribute value that is representing
	 * the given text.
	 * 
	 * @param text
	 * @return the attribute value, never <code>null</code>.
	 */
	public static AttributeValueImpl parse(String text) {
		AttributeValueImpl value = new AttributeValueImpl(text);
		if (text!=null && !text.isEmpty()) {
			Object binValue;
			for(AttributeType type : AttributeType.values()) {
				try {
					binValue = null;
					switch(type) {
					case BOOLEAN:
						binValue = value.getBoolean();
						break;
					case COLOR:
						binValue = parseColor((String)value.value, true);
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
						binValue = parsePoint((String)value.value, true);
						break;
					case POINT3D:
						binValue = parsePoint3D((String)value.value, true);
						break;
					case POLYLINE:
						binValue = parsePolyline((String)value.value, true);
						break;
					case POLYLINE3D:
						binValue = parsePolyline3D((String)value.value, true);
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
						binValue = parseURI((String)value.value);
						break;
					case URL:
						binValue = value.getURL();
						break;
					case UUID:
						binValue = parseUUID((String)value.value);
						break;
					default:
						//
					}
					if (binValue!=null) {
						return new AttributeValueImpl(type, binValue);
					}
				}
				catch(Throwable _) {
					//
				}
			}
		}
		return value;
	}
	
	/** Compare the two specified values.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return replies a negative value if <var>arg0</var> is lesser than
	 * <var>arg1</var>, a positive value if <var>arg0</var> is greater than
	 * <var>arg1</var>, or <code>0</code> if they are equal.
	 * @see AttributeValueComparator
	 */
	public static int compareValues(AttributeValue arg0, AttributeValue arg1) {
		if (arg0==arg1) return 0;
		if (arg0==null) return Integer.MAX_VALUE;
		if (arg1==null) return Integer.MIN_VALUE;
		
		Object v0, v1;
		
		try {
			v0 = arg0.getValue();
		}
		catch (Exception _) {
			v0 = null;
		}

		try {
			v1 = arg1.getValue();
		}
		catch (Exception _) {
			v1 = null;
		}

		return compareRawValues(v0, v1);
	}
	
	/** Compare the internal objects of two specified values
	 * 
	 * @param arg0
	 * @param arg1
	 * @return replies a negative value if <var>arg0</var> is lesser than
	 * <var>arg1</var>, a positive value if <var>arg0</var> is greater than
	 * <var>arg1</var>, or <code>0</code> if they are equal.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static int compareRawValues(Object arg0, Object arg1) {
		if (arg0==arg1) return 0;
		if (arg0==null) return Integer.MAX_VALUE;
		if (arg1==null) return Integer.MIN_VALUE;
		
		if ((arg0 instanceof Number)&&(arg1 instanceof Number))
			return Double.compare(((Number)arg0).doubleValue(),((Number)arg1).doubleValue());

		try {
			if (arg0 instanceof Comparable<?>)
				return ((Comparable)arg0).compareTo(arg1);
		}
		catch(RuntimeException _) {
			//
		}
		
		try {
			if (arg1 instanceof Comparable<?>)
				return - ((Comparable)arg1).compareTo(arg0);
		}
		catch(RuntimeException _) {
			//
		}
		
		if (arg0.equals(arg1)) return 0;
		
		String sv0 = arg0.toString();
		String sv1 = arg1.toString();

		if (sv0==sv1) return 0;
		if (sv0==null) return Integer.MAX_VALUE;
		if (sv1==null) return Integer.MIN_VALUE;
		
		return sv0.compareTo(sv1);
	}

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
	private boolean assigned = false;

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
		if (value!=null) {
			this.type = value.getType();
			try {
				this.value = value.getValue();
				this.assigned = isNullAllowed() || (this.value!=null);
			}
			catch (AttributeException _) {
				this.value = null;
				this.assigned = false;
			}
		}
		else {
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
		if (rawValue==null) {
			this.value = rawValue;
		}
		else {
			try {
				this.value = type.cast(rawValue);
			}
			catch(Exception _) {
				this.value = null;
			}
		}
		this.assigned = this.value!=null;
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
	 */
	public AttributeValueImpl(Color value) {
		this.type = AttributeType.COLOR;
		this.value = (value!=null) ? VectorToolkit.color(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha()) : null;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(UUID value) {
		this.type = AttributeType.UUID;
		this.value = (value!=null) ? new UUID(value.getMostSignificantBits(), value.getLeastSignificantBits()) : null;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(URL value) {
		this.type = AttributeType.URL;
		try {
			this.value = (value!=null) ? new URL(value.toExternalForm()) : null;
		}
		catch (MalformedURLException e) {
			this.value = null;
		}
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(URI value) {
		this.type = AttributeType.URI;
		try {
			this.value = (value!=null) ? new URI(value.toASCIIString()) : null;
		}
		catch (URISyntaxException e) {
			this.value = null;
		}
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(InetAddress value) {
		this.type = AttributeType.INET_ADDRESS;
		this.value = (value!=null) ? value : null;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(InetSocketAddress value) {
		this.type = AttributeType.INET_ADDRESS;
		this.value = (value!=null) ? value.getAddress() : null;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Enum<?> value) {
		this.type = AttributeType.ENUMERATION;
		this.value = (value!=null) ? value : null;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Class<?> value) {
		this.type = AttributeType.TYPE;
		this.value = (value!=null) ? value : null;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Date value) {
		this.type = AttributeType.DATE;
		this.value = (value!=null) ? new Date(value.getTime()) : null;
		this.assigned = (this.value!=null);
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
	 */
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
	public AttributeValueImpl(Point2D value) {
		this.type = AttributeType.POINT;
		this.value = value;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 */
	public AttributeValueImpl(float x, float y) {
		this.type = AttributeType.POINT;
		this.value = new Point2f(x,y);
		this.assigned = true;
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 */
	public AttributeValueImpl(double x, double y) {
		this.type = AttributeType.POINT;
		this.value = new Point2f(x,y);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Point3D value) {
		this.type = AttributeType.POINT3D;
		this.value = value;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 * @param z is the value.
	 */
	public AttributeValueImpl(float x, float y, float z) {
		this.type = AttributeType.POINT3D;
		this.value = new Point3f(x,y,z);
		this.assigned = true;
	}

	/**
	 * @param x is the value.
	 * @param y is the value.
	 * @param z is the value.
	 */
	public AttributeValueImpl(double x, double y, double z) {
		this.type = AttributeType.POINT3D;
		this.value = new Point3f(x,y,z);
		this.assigned = true;
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(String value) {
		this.type = AttributeType.STRING;
		this.value = value;
		this.assigned = (value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Point2D[] value) {
		this.type = AttributeType.POLYLINE;
		this.value = value;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Point3D[] value) {
		this.type = AttributeType.POLYLINE3D;
		this.value = value;
		this.assigned = (this.value!=null);
	}

	/**
	 * @param value is the value.
	 */
	public AttributeValueImpl(Object value) {
		AttributeType detectedType = AttributeType.fromValue(value);
		this.type = detectedType;
		this.value = detectedType.cast(value);
		this.assigned = isNullAllowed() || (this.value!=null);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @param o {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof AttributeValue) {
			return compareValues(this, (AttributeValue)o)==0;
		}
		return compareRawValues(this.value, o) == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
        int PRIME = 31;
        int result = 1;
        result = PRIME * result + (this.value!=null ? this.value.hashCode() : 0);
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
		str.append("["); //$NON-NLS-1$
		str.append((this.value==null)
				? "???" //$NON-NLS-1$
				: this.value.toString());
		str.append(":"); //$NON-NLS-1$
		str.append(this.type.toString());
		str.append("]"); //$NON-NLS-1$
		return str.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isAssignableFrom(AttributeType type) {
		return getType().isAssignableFrom(type);
	}
	
	/** {@inheritDoc}
	 */
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
		return ((!this.assigned)||(this.value==null));
	}
	
	/** Assert that the attribute value was assigned.
	 */
	private void assertAssigned() throws AttributeNotInitializedException {
		if ((this.type==null)||(!this.assigned)) throw new AttributeNotInitializedException();
	}

	/** Assert that the attribute value was assigned and not <code>null</code>.
	 * 
	 * @throws AttributeNotInitializedException
	 */
	protected void assertAssignedAndNotNull() throws AttributeNotInitializedException {
		if ((this.type==null)||(!this.assigned)||(this.value==null)) throw new AttributeNotInitializedException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isBaseType() {
		return this.type.isBaseType();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isNullAllowed() {
		return this.type.isNullAllowed();
	}

	/** {@inheritDoc}
	 */
	@Override
	public AttributeType getType() {
		return this.type;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setType(AttributeType type) throws InvalidAttributeTypeException {
		try {
			switch(type) {
			case INTEGER:
				this.value = new Long(getInteger()); break;
			case REAL:
				this.value = new Double(getReal()); break;
			case STRING:
				this.value = getString(); break;
			case BOOLEAN:
				this.value = new Boolean(getBoolean()); break;
			case DATE:
				this.value = getDate(); break;
			case TIMESTAMP:
				this.value = new Timestamp(getTimestamp()); break;
			case OBJECT:
				this.value = getJavaObject(); break;
			case POINT:
				this.value = getPoint(); break;
			case POINT3D:
				this.value = getPoint3D(); break;
			case COLOR:
				this.value = getColor(); break;
			case UUID:
				this.value = getUUID(); break;
			case IMAGE:
				this.value = getImage(); break;
			case POLYLINE:
				this.value = getPolyline(); break;
			case POLYLINE3D:
				this.value = getPolyline3D(); break;
			case URI:
				this.value = getURI(); break;
			case URL:
				this.value = getURL(); break;
			case INET_ADDRESS:
				this.value = getInetAddress(); break;
			case ENUMERATION:
				this.value = getEnumeration(); break;
			case TYPE:
				this.value = getJavaClass(); break;
			default:
				throw new InvalidAttributeTypeException();
			}			
		}
		catch(NumberFormatException ex) {
			throw new InvalidAttributeTypeException();
		}
		catch (AttributeNotInitializedException e) {
			this.value = type.getDefaultValue();
		}
		this.type = type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean cast(AttributeType attrType) {
		boolean b = true;
		try {
			setType(attrType);
		}
		catch(InvalidAttributeTypeException ex) {
			this.value = attrType.getDefaultValue();
			b = false;
		}
		this.type = attrType;
		return b;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void castAndSet(AttributeType attrType, Object attrValue) {
		try {
			if (attrValue instanceof AttributeValue) {
				this.type = ((AttributeValue)attrValue).getType();
				try {
					this.value = ((AttributeValue)attrValue).getValue();
					this.assigned = true;
				}
				catch (AttributeNotInitializedException e) {
					this.value = attrType.getDefaultValue();
					this.assigned = true;
				}
			}
			else {
				this.type = attrType;
				this.value = attrType.cast(attrValue);
				this.assigned = true;
			}
			if (attrValue!=null) {
				setType(attrType);
			}
			else {
				this.value = attrType.getDefaultValue();
				this.assigned = true;
			}
		}
		catch(InvalidAttributeTypeException ex) {
			this.value = attrType.getDefaultValue();
			this.assigned = true;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object getValue() throws InvalidAttributeTypeException, AttributeNotInitializedException{
		assertAssigned();
		return this.value;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Class<?> getInternalStorageType() {
		switch(this.type) {
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

	/** {@inheritDoc}
	 */
	@Override
	public void setValue(AttributeValue value) {
		this.type = value.getType();
		try {
			this.assigned = value.isAssigned();
			this.value = value.getValue();
		}
		catch (AttributeException e) {
			this.value = this.type.getDefaultValue();
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setValue(Object value) {
		AttributeType detectedType = AttributeType.fromValue(value);
		this.value = detectedType.cast(value);
		this.assigned = (this.value!=null);
	}

	/** Set this value with the content of the specified one.
	 * <p>
	 * The type of the attribute will be NOT detected from the type
	 * of the object. It means that it is not changed by this function.
	 * The given value must be compatible with internal representation
	 * of the attribute implementation.
	 * 
	 * @param value is the raw value to put inside this attribute value.
	 */
	protected void setInternalValue(Object value) {
		this.value = value;
		this.assigned = (this.value!=null);
	}

	/** Set this value with the content of the specified one.
	 * <p>
	 * The type of the attribute will be NOT detected from the type
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
		this.assigned = (this.value!=null);
		this.type = type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setToDefault() {
		this.assigned = true;
		this.value = this.type.getDefaultValue();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setToDefaultIfUninitialized() {
		if (!isAssigned()) setToDefault();
	}

	/** {@inheritDoc}
	 */
	@Override
	public long getInteger() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case INTEGER:
				return ((Long)this.value).longValue();
			case TIMESTAMP:
				return ((Timestamp)this.value).longValue();
			case REAL:
				return ((Double)this.value).longValue();
			case STRING:
				return Long.parseLong((String)this.value);
			case DATE:
				return ((Date)this.value).getTime();
			case BOOLEAN:
				return ((Boolean)this.value).booleanValue() ? 1 : 0;
			case COLOR:
				return ((Color)this.value).getRGB();
			case OBJECT:
				if (this.value instanceof Number)
					return ((Number)this.value).longValue();
				break;
			case ENUMERATION:
				if (this.value instanceof Enum<?>)
					return ((Enum<?>)this.value).ordinal();
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
		}
		catch(NumberFormatException _) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setInteger(int value) {
		this.value = new Long(value);
		this.type = AttributeType.INTEGER;
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setInteger(long value) {
		this.value = new Long(value);
		this.type = AttributeType.INTEGER;
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getReal() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case INTEGER:
				return ((Long)this.value).doubleValue();
			case TIMESTAMP:
				return ((Timestamp)this.value).doubleValue();
			case REAL:
				return ((Double)this.value).doubleValue();
			case STRING:
				return Double.parseDouble((String)this.value);
			case DATE:
				return ((Date)this.value).getTime();
			case BOOLEAN:
				return ((Boolean)this.value).booleanValue() ? 1. : 0.;
			case COLOR:
				return ((Color)this.value).getRGB();
			case OBJECT:
				if (this.value instanceof Number)
					return ((Number)this.value).doubleValue();
				break;
			case ENUMERATION:
				if (this.value instanceof Enum<?>)
					return ((Enum<?>)this.value).ordinal();
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setReal(double value) {
		this.value = new Double(value);
		this.type = AttributeType.REAL;
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getString() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case STRING:
				return (String)this.value;
			case BOOLEAN:
				return ((Boolean)this.value).toString();
			case COLOR:
			{
				Color col = ((Color)this.value);
				return Integer.toString(col.getRed())
					+';'+col.getGreen()
					+';'+col.getBlue()
					+';'+col.getAlpha();
			}
			case UUID:
			{
				UUID uuid = ((UUID)this.value);
				return uuid.toString();
			}
			case URL:
			{
				URL url = ((URL)this.value);
				return url.toExternalForm();
			}
			case URI:
			{
				URI uri = ((URI)this.value);
				return uri.toASCIIString();
			}
			case TIMESTAMP:
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
				return format.format(new Date(((Timestamp)this.value).longValue()));
			}
			case INTEGER:
				return ((Long)this.value).toString();
			case REAL:
				return ((Double)this.value).toString();
			case POINT:
				Point2D pt2 = ((Point2D)this.value);
				StringBuilder buffer = new StringBuilder();
				buffer.append(pt2.getX());
				buffer.append(";"); //$NON-NLS-1$
				buffer.append(pt2.getY());
				return buffer.toString();
			case POINT3D:
				Point3D pt3 = ((Point3D)this.value);
				buffer = new StringBuilder();
				buffer.append(pt3.getX());
				buffer.append(";"); //$NON-NLS-1$
				buffer.append(pt3.getY());
				buffer.append(";"); //$NON-NLS-1$
				buffer.append(pt3.getZ());
				return buffer.toString();
			case DATE:
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
				return format.format((Date)this.value);
			}
			case POLYLINE:
			{ 
				buffer = new StringBuilder();
				Point2D[] lstpt2 = ((Point2D[])this.value);
				for(int i=0; i<lstpt2.length; ++i) {
					if (lstpt2[i]!=null) {
						if (buffer.length()>0) buffer.append(";"); //$NON-NLS-1$
						buffer.append(lstpt2[i].getX());
						buffer.append(";"); //$NON-NLS-1$
						buffer.append(lstpt2[i].getY());
					}
				}
				return buffer.toString();
			}
			case POLYLINE3D:
			{ 
				buffer = new StringBuilder();
				Point3D[] lstpt3 = ((Point3D[])this.value);
				for(int i=0; i<lstpt3.length; ++i) {
					if (lstpt3[i]!=null) {
						if (buffer.length()>0) buffer.append(";"); //$NON-NLS-1$
						buffer.append(lstpt3[i].getX());
						buffer.append(";"); //$NON-NLS-1$
						buffer.append(lstpt3[i].getY());
						buffer.append(";"); //$NON-NLS-1$
						buffer.append(lstpt3[i].getZ());
					}
				}
				return buffer.toString();
			}
			case ENUMERATION:
			{
				StringBuilder b = new StringBuilder();
				Enum<?> enumeration = (Enum<?>)this.value;
				Class<?> enumerationType = enumeration.getDeclaringClass();
				String typeName = enumerationType.getCanonicalName();
				b.append(typeName);
				b.append("."); //$NON-NLS-1$
				b.append(((Enum<?>)this.value).name());
				return b.toString();
			}
			case TYPE:
			{
				return ((Class<?>)this.value).getCanonicalName();
			}
			case INET_ADDRESS:
			case OBJECT:
			case IMAGE:
				return this.value.toString();
			default:
				throw new InvalidAttributeTypeException();
			}
		}
		catch(ClassCastException _) {
			//
		}
		throw new InvalidAttributeTypeException();
	}
		
	/** {@inheritDoc}
	 */
	@Override
	public void setString(String value) {
		this.value = value;
		this.type = AttributeType.STRING;
		this.assigned = this.value!=null;
	}
	
	/** Parse a date according to the specified locale.
	 */
	private static Date extractDate(String text, Locale locale) {
		DateFormat fmt;
		for(int style=0; style<=3; ++style) {
			// Date and time parsing
			for(int style2=0; style2<=3; ++style2) {
				fmt = DateFormat.getDateTimeInstance(style,style2,locale);
				try {
					return fmt.parse(text);
				}
				catch(ParseException _) {
					//
				}
			}
			// Date only parsing
			fmt = DateFormat.getDateInstance(style,locale);
			try {
				return fmt.parse(text);
			}
			catch(ParseException _) {
				//
			}
			// Time only parsing
			fmt = DateFormat.getTimeInstance(style,locale);
			try {
				return fmt.parse(text);
			}
			catch(ParseException _) {
				//
			}
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Date getDate() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case DATE:
				return (Date)((Date)this.value).clone();
			case REAL:
				 return new Date(((Double)this.value).longValue());				
			case INTEGER:
				 return new Date(((Long)this.value).longValue());				
			case TIMESTAMP:
				 return new Date(((Timestamp)this.value).longValue());				
			case STRING:
				String txt = (String)this.value;
				DateFormat fmt;
				Date dt;
				
				dt = extractDate(txt, Locale.getDefault());
				if (dt!=null) return dt;
				
				dt = extractDate(txt, Locale.US);
				if (dt!=null) return dt;
				
				dt = extractDate(txt, Locale.UK);
				if (dt!=null) return dt;

				try {
					fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
					return fmt.parse(txt);
				}
				catch(ParseException _) {
					//
				}
				fmt = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
				return fmt.parse(txt);
			case OBJECT:
				if (this.value instanceof Date)
					return (Date)this.value;
				if (this.value instanceof Calendar)
					return ((Calendar)this.value).getTime();
				if (this.value instanceof Number)
					return new Date(((Number)this.value).longValue());
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
		}
		catch(Exception e) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setDate(Date value) {
		this.value = value;
		this.type = AttributeType.DATE;
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean getBoolean() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case BOOLEAN:
				return ((Boolean)this.value).booleanValue();
			case STRING:
				// Do not use the function Boolean.parseBoolean() because
				// it replies false when the string does not contains "true"
				if (TRUE_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return true;
				if (YES_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return true;
				if (OUI_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return true;
				if (T_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return true;
				if (Y_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return true;
				if (O_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return true;

				if (FALSE_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return false;
				if (NO_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return false;
				if (NON_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return false;
				if (F_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return false;
				if (N_CONSTANT.compareToIgnoreCase((String)this.value)==0)
					return false;

				break;
			case INTEGER:
				return ((Long)this.value).longValue() != 0;
			case TIMESTAMP:
				return ((Timestamp)this.value).longValue() != 0;
			case REAL:
				return ((Double)this.value).doubleValue() != 0.;
			case OBJECT:
				if (this.value instanceof Boolean)
					return ((Boolean)this.value).booleanValue();
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
		}
		catch(ClassCastException _) {
			//
		}			
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setBoolean(boolean value) {
		this.value = new Boolean(value);
		this.type = AttributeType.BOOLEAN;
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isObjectValue() {
		return !isBaseType();
	}

	/** {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getJavaObject() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		if (!isObjectValue())
			throw new InvalidAttributeTypeException();
		if (isNotAssignedOrNull()) return null;
		try {
			return (T)this.value;
		}
		catch(ClassCastException _) {
			throw new InvalidAttributeTypeException();
		}			
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setJavaObject(Object value) {
		this.value = value;
		this.type = AttributeType.OBJECT;
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public long getTimestamp() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case INTEGER:
				return ((Long)this.value).longValue();
			case TIMESTAMP:
				return ((Timestamp)this.value).longValue();
			case REAL:
				return ((Double)this.value).longValue();
			case STRING:
				return Long.parseLong((String)this.value);
			case DATE:
				return ((Date)this.value).getTime();
			case BOOLEAN:
				return ((Boolean)this.value).booleanValue() ? 1 : 0;
			case COLOR:
				return ((Color)this.value).getRGB();
			case OBJECT:
				if (this.value instanceof Number)
					return ((Number)this.value).longValue();
				if (this.value instanceof Date)
					return ((Date)this.value).getTime();
				if (this.value instanceof Calendar)
					return ((Calendar)this.value).getTimeInMillis();
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setTimestamp(long value) {
		this.value = new Timestamp(value);
		this.type = AttributeType.TIMESTAMP;
		this.assigned = true;
	}
	
	private static Point3D parsePoint3D(String text, boolean isStrict) {
		String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && comp.length!=3) {
			return null;
		}
		Point3f pt3 = new Point3f();
		if (comp.length>0) pt3.setX(Float.parseFloat(comp[0]));
		if (comp.length>1) pt3.setY(Float.parseFloat(comp[1]));
		if (comp.length>2) pt3.setZ(Float.parseFloat(comp[2]));
		return pt3;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3D getPoint3D() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case COLOR:
				Color col = (Color)this.value;
				return new Point3f(col.getRed(),col.getGreen(),col.getBlue()); 
			case REAL:
				Double flt = (Double)this.value;
				return new Point3f(flt.floatValue(),0,0); 
			case INTEGER:
				Long lg = (Long)this.value;
				return new Point3f(lg.floatValue(),0,0); 
			case TIMESTAMP:
				Timestamp ts = (Timestamp)this.value;
				return new Point3f(ts.floatValue(),0,0); 
			case DATE:
				Date dt = (Date)this.value;
				return new Point3f(dt.getTime(),0,0); 
			case POINT:
				Point2D pt2 = (Point2D)this.value;
				return new Point3f(pt2.getX(), pt2.getY(),0); 
			case POINT3D:
				return ((Point3D)this.value).clone(); 
			case STRING:
				return parsePoint3D((String)this.value, false);
			case OBJECT:
				if (this.value instanceof Tuple3D) {
					Tuple3D<?> t3 = (Tuple3D<?>)this.value;
					return new Point3f(t3.getX(), t3.getY(), t3.getZ()); 
				}
				if (this.value instanceof Tuple2D) {
					Tuple2D<?> t2 = (Tuple2D<?>)this.value;
					return new Point3f(t2.getX(), t2.getY(), 0); 
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}			
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPoint3D(Point3D value) {
		this.value = value;
		this.type = AttributeType.POINT3D;
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPoint3D(float x, float y, float z) {
		this.value = new Point3f(x,y,z);
		this.type = AttributeType.POINT3D;
		this.assigned = true;
	}
	
	private static Point2D parsePoint(String text, boolean isStrict) {
		String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && comp.length!=2) {
			return null;
		}
		float x=0,y=0;
		if (comp.length>0) x = Float.parseFloat(comp[0]);
		if (comp.length>1) y = Float.parseFloat(comp[1]);
		return new Point2f(x,y);
		
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2D getPoint() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case COLOR:
				Color col = (Color)this.value;
				return new Point2f(col.getRed(),col.getGreen()); 
			case REAL:
				Double flt = (Double)this.value;
				return new Point2f(flt.floatValue(),0f); 
			case INTEGER:
				Long lg = (Long)this.value;
				return new Point2f(lg.floatValue(),0f); 
			case TIMESTAMP:
				Timestamp ts = (Timestamp)this.value;
				return new Point2f(ts.floatValue(),0f); 
			case DATE:
				Date dt = (Date)this.value;
				return new Point2f(dt.getTime(),0f); 
			case POINT:
				return new Point2f(((Point2D)this.value).getX(),
						((Point2D)this.value).getY());
			case POINT3D:
				Point3D pt3 = (Point3D)this.value;
				return new Point2f(pt3.getX(),pt3.getY()); 
			case STRING:
				return parsePoint((String)this.value, false);
			case OBJECT:
				if (this.value instanceof Tuple3D) {
					Tuple3D<?> t3 = (Tuple3D<?>)this.value;
					return new Point2f(t3.getX(), t3.getY()); 
				}
				if (this.value instanceof Tuple2D) {
					Tuple2D<?> t2 = (Tuple2D<?>)this.value;
					return new Point2f(t2.getX(), t2.getY()); 
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
		}
		catch(ClassCastException _) {
			//
		}			
		catch(NumberFormatException _) {
			//
		}			
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPoint(Point2D value) {
		this.value = value;
		this.type = AttributeType.POINT;
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPoint(float x, float y) {
		this.value = new Point2f(x,y);
		this.type = AttributeType.POINT;
		this.assigned = true;
	}
	
	private static Color parseColor(String text, boolean isStrict) {
		String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && comp.length!=3 && comp.length!=4) {
			return null;
		}
		int r=0,g=0,b=0,a=255;
		if (comp.length>0) r = Integer.parseInt(comp[0]);
		if (comp.length>1) g = Integer.parseInt(comp[1]);
		if (comp.length>2) b = Integer.parseInt(comp[2]);
		if (comp.length>3) a = Integer.parseInt(comp[3]);
		if ((r<256)&&(r>=0)&&(g<256)&&(g>=0)&&(b<256)&&(b>=0)&&
			(a<256)&&(a>=0))
			return VectorToolkit.color(r,g,b,a);
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Color getColor() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case COLOR:
				Color col = (Color)this.value;
				return VectorToolkit.color(col.getRed(),col.getGreen(),col.getBlue(),col.getAlpha());
			case POINT:
				Point2D pt2 = (Point2D)this.value;
				return VectorToolkit.color(pt2.getX(),pt2.getY(),0f, 1f);
			case POINT3D:
				Point3D pt3 = (Point3D)this.value;
				return VectorToolkit.color(pt3.getX(),pt3.getY(),pt3.getZ(), 1f);
			case STRING:
				Color color = parseColor((String)this.value, false);
				if (color!=null) return color;
				break;
			case INTEGER:
				return VectorToolkit.color(((Long)this.value).intValue());
			case TIMESTAMP:
				return VectorToolkit.color(((Timestamp)this.value).intValue());
			case REAL:
				return VectorToolkit.color(((Double)this.value).intValue());
			case DATE:
				return VectorToolkit.color((int)((Date)this.value).getTime());
			case OBJECT:
				if (this.value instanceof Color)
					return (Color)this.value;
				if (this.value instanceof Number)
					return VectorToolkit.color(((Number)this.value).intValue());
				if (this.value instanceof Date)
					return VectorToolkit.color((int)((Date)this.value).getTime());
				if (this.value instanceof Calendar)
					return VectorToolkit.color((int)((Calendar)this.value).getTimeInMillis());
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}
		throw new InvalidAttributeTypeException();		
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setColor(Color c) {
		this.value = c;
		this.type = AttributeType.COLOR;		
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setColor(float r, float g, float b) {
		this.value = VectorToolkit.color(r,g,b, 1f);
		this.type = AttributeType.COLOR;				
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setColor(float r, float g, float b, float a) {
		this.value = VectorToolkit.color(r,g,b,a);
		this.type = AttributeType.COLOR;				
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setColor(int r, int g, int b) {
		this.value = VectorToolkit.color(r,g,b,255);
		this.type = AttributeType.COLOR;				
		this.assigned = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setColor(int r, int g, int b, int a) {
		this.value = VectorToolkit.color(r,g,b,a);
		this.type = AttributeType.COLOR;				
		this.assigned = true;
	}

	private static UUID parseUUID(String text) {
		try {
			URI uri = new URI(text);
			if ("uuid".equalsIgnoreCase(uri.getScheme())) { //$NON-NLS-1$
				return UUID.fromString(uri.getHost());
			}
		}
		catch(Throwable _) {
			//
		}
		try {
			return UUID.fromString(text);
		}
		catch(Throwable _) {
			//
		}
		return null;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public UUID getUUID() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case UUID:
				UUID id = (UUID)this.value;
				return new UUID(id.getMostSignificantBits(), id.getLeastSignificantBits());
			case URI:
				URI uri = (URI)this.value;
				if ("uuid".equalsIgnoreCase(uri.getScheme())) { //$NON-NLS-1$
					try {
						return UUID.fromString(uri.getHost());
					}
					catch(AssertionError e) {
						throw e;
					}
					catch(Throwable _) {
						//
					}
				}
				break; // see below for the other cases
			case OBJECT:
				if (this.value instanceof UUID) {
					return (UUID)this.value;
				}
				if (this.value instanceof URI
					&& "uuid".equalsIgnoreCase(((URI)this.value).getScheme())) { //$NON-NLS-1$
					try {
						return UUID.fromString(((URI)this.value).getHost());
					}
					catch(AssertionError e) {
						throw e;
					}
					catch(Throwable _) {
						//
					}
				}
				break; // see below for the other cases
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
				break; // see below, for the computation of an UUID from the value
			}
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}
		if (this.value==null)
			return (UUID)AttributeType.UUID.getDefaultValue();
		String s = this.value.toString();
		if (s==null || "".equals(s)) //$NON-NLS-1$
			return (UUID)AttributeType.UUID.getDefaultValue();
		try {
			return UUID.fromString(s);
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable _) {
			//
		}
		return UUID.nameUUIDFromBytes(s.getBytes());
	}

	/** {@inheritDoc}
	 */
	@Override
	public URL getURL() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case URL:
				return new URL(((URL)this.value).toExternalForm());
			case URI:
				return ((URI)this.value).toURL();
			case STRING:
				return new URL((String)this.value);
			case OBJECT:
				if (this.value instanceof URL)
					return (URL)this.value;
				if (this.value instanceof URI)
					return ((URI)this.value).toURL();
				break; // Other objects, see below
			case INET_ADDRESS:
				return new URL(DEFAULT_SCHEME.name(), ((InetAddress)this.value).getHostAddress(), ""); //$NON-NLS-1$
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(MalformedURLException _) {
			//
		}
		if (this.value==null)
			return (URL)AttributeType.URL.getDefaultValue();
		String s = this.value.toString();
		if (s==null)
			return (URL)AttributeType.URL.getDefaultValue();
		try {
			return new URL(s);
		}
		catch (MalformedURLException _) {
			throw new InvalidAttributeTypeException();
		}
	}
	
	private static URI parseURI(String text) {
		try {
			URI uri = new URI(text);
			if (uri.getScheme()!=null && !uri.getScheme().isEmpty())
				return uri;
		}
		catch(Throwable _) {
			//
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public URI getURI() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case URL:
				return ((URL)this.value).toURI();
			case URI:
				return new URI(((URI)this.value).toASCIIString());
			case STRING:
				return new URI((String)this.value);
			case UUID:
				return new URI("uuid:"+((UUID)this.value).toString()); //$NON-NLS-1$
			case OBJECT:
				if (this.value instanceof URI)
					return (URI)this.value;
				if (this.value instanceof URL)
					return ((URL)this.value).toURI();
				if (this.value instanceof UUID)
					return new URI("uuid:"+((UUID)this.value).toString()); //$NON-NLS-1$
				break; // Other objects, see below
			case INET_ADDRESS:
				return new URI(DEFAULT_SCHEME.name(), ((InetAddress)this.value).getHostAddress(), ""); //$NON-NLS-1$
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(URISyntaxException _) {
			//
		}
		if (this.value==null)
			return (URI)AttributeType.URI.getDefaultValue();
		String s = this.value.toString();
		if (s==null)
			return (URI)AttributeType.URI.getDefaultValue();
		try {
			return new URI(s);
		}
		catch (URISyntaxException _) {
			throw new InvalidAttributeTypeException();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setUUID(UUID u) {
		this.value = (u==null) ? AttributeType.UUID.getDefaultValue() : u;
		this.type = AttributeType.UUID;		
		this.assigned = this.value!=null;
	}

	/**
	 * Set the value of this metadata.
	 * 
	 * @param id
	 */
	public void setUUID(String id) {
		try {
			this.value = (id!=null) ? UUID.fromString(id) : null;
		}
		catch(Throwable _) {
			assert(id!=null);
			this.value = UUID.nameUUIDFromBytes(id.getBytes());
		}
		this.type = AttributeType.UUID;				
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setURL(URL u) {
		this.value = (u==null) ? AttributeType.URL.getDefaultValue() : u;
		this.type = AttributeType.URL;		
		this.assigned = this.value!=null;
	}

	/**
	 * Set the value of this metadata.
	 * 
	 * @param url
	 */
	public void setURL(String url) {
		try {
			this.value = (url!=null) ? new URL(url) : null;
		}
		catch(Throwable _) {
			this.value = null;
		}
		this.type = AttributeType.URL;				
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setURI(URI u) {
		this.value = (u==null) ? AttributeType.URI.getDefaultValue() : u;
		this.type = AttributeType.URI;		
		this.assigned = this.value!=null;
	}

	/**
	 * Set the value of this metadata.
	 * 
	 * @param uri
	 */
	public void setURI(String uri) {
		try {
			this.value = (uri!=null) ? new URI(uri) : null;
		}
		catch(Throwable _) {
			this.value = null;
		}
		this.type = AttributeType.URI;				
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Image getImage() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssigned();
			switch(this.type) {
			case IMAGE:
				assertAssignedAndNotNull();
				return (Image)this.value;
			case OBJECT:
				if ((this.value==null)||(this.value instanceof Image)) return (Image)this.value;
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
		}
		catch(ClassCastException _) {
			//
		}
		throw new InvalidAttributeTypeException();		
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setImage(Image c) {
		this.value = c;
		this.type = AttributeType.IMAGE;		
		this.assigned = true;
	}

	private static Point3D[] parsePolyline3D(String text, boolean isStrict) {
		String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && (comp.length%3)!=0) {
			return null;
		}
		int fullPoints = comp.length/3;
		boolean addPt = (fullPoints*3!=comp.length); 
		Point3D[] tab = new Point3D[addPt ? fullPoints+1 : fullPoints];
		for(int i=2,j=0; (i<comp.length)&&(j<tab.length); i+=3,++j) {
			tab[j] = new Point3f(
					Float.parseFloat(comp[i-2]),
					Float.parseFloat(comp[i-1]),
					Float.parseFloat(comp[i]));
		}
		if (addPt) {
			float x = Float.parseFloat(comp[fullPoints*3]);
			int idx = fullPoints*3+1;
			float y = idx<comp.length ? Float.parseFloat(comp[idx]) : 0;
			tab[tab.length-1]  = new Point3f(x, y, 0);
		}
		return tab;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Point3D[] getPolyline3D() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case POINT:
				Point2D pt2 = (Point2D)this.value;
				return new Point3D[] {
						new Point3f(pt2.getX(),pt2.getY(),0f) 
				};
			case POINT3D:
				return new Point3D[] {
					((Point3D)this.value).clone() 
				};
			case POLYLINE:
			{
				Point2D[] current = (Point2D[])this.value;
				Point3D[] tab = new Point3D[current.length];
				for(int i=0; i<current.length; ++i) {
					tab[i] = new Point3f(
								current[i].getX(),
								current[i].getY(),
								0f); 
				}
				return tab;
			}
			case POLYLINE3D:
				return (Point3D[])this.value;
			case STRING:
				return parsePolyline3D((String)this.value, false);
			case OBJECT:
				if (this.value instanceof Tuple2D<?>) {
					Tuple2D<?> t2 = (Tuple2D<?>)this.value;
					return new Point3D[] {
						new Point3f(t2.getX(),t2.getY(),0) 
					};
				}
				else if (this.value instanceof Tuple3D<?>) {
					Tuple3D<?> t2 = (Tuple3D<?>)this.value;
					return new Point3D[] {
							new Point3f(t2.getX(), t2.getY(), t2.getZ())
					};
				}
				else if (this.value.getClass().isArray()) {
					Class<?> elementType = this.value.getClass().getComponentType();
					if (Point3D.class.equals(elementType)) {
						return (Point3D[])this.value;
					}
					int size = Array.getLength(this.value);
					if (Tuple3D.class.isAssignableFrom(elementType)) {
						Point3D[] pa3 = new Point3D[size];
						for(int i=0; i<pa3.length; ++i) {
							Tuple3D<?> t = (Tuple3D<?>)Array.get(this.value, i);
							pa3[i] = new Point3f(t);
						}
						return pa3;
					}
					if (Tuple2D.class.isAssignableFrom(elementType)) {
						Point3D[] pa3 = new Point3D[size];
						for(int i=0; i<pa3.length; ++i) {
							Tuple2D<?> t = (Tuple2D<?>)Array.get(this.value, i);
							pa3[i] = new Point3f(t.getX(), t.getY(), 0);
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}			
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPolyline3D(Point3D... value) {
		this.value = value;
		this.type = AttributeType.POLYLINE3D;
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPolyline3D(Collection<? extends Point3D> value) {
		if (value==null) {
			this.value = null;
		}
		else {
			Point3D[] tab = new Point3D[value.size()];
			value.toArray(tab);
			this.value = tab;
		}
		this.type = AttributeType.POLYLINE3D;
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addToPolyline3D(Point3D... p) {
		Point3D[] tab;
		if (this.value instanceof Point3D[]) {
			int size = ((Point3D[])this.value).length;
			tab = new Point3D[size+p.length];
			System.arraycopy(this.value,0,tab,0,size);
			System.arraycopy(p,0,tab,size,p.length);
		}
		else {
			tab = p;
		}
		this.value = tab;
		this.assigned = this.value!=null;
		this.type = AttributeType.POLYLINE3D;		
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void addToPolyline3D(Collection<? extends Point3D> p) {
		Point3D[] tab;
		if (this.value instanceof Point3D[]) {
			int size = ((Point3D[])this.value).length;
			tab = new Point3D[size+p.size()];
			System.arraycopy(this.value,0,tab,0,size);
			System.arraycopy(p.toArray(),0,tab,size,p.size());
		}
		else {
			tab = new Point3D[p.size()];
			p.toArray(tab);
		}
		this.value = tab;
		this.assigned = this.value!=null;
		this.type = AttributeType.POLYLINE3D;		
	}

	private static Point2D[] parsePolyline(String text, boolean isStrict) {
		String[] comp = text.split(";"); //$NON-NLS-1$
		if (isStrict && (comp.length%2)!=0) {
			return null;
		}
		int fullPoints = comp.length/2;
		boolean addPt = (fullPoints*2!=comp.length); 
		Point2D[] tab = new Point2D[addPt ? fullPoints+1 : fullPoints];
		for(int i=1,j=0; (i<comp.length)&&(j<fullPoints); i+=2,++j) {
			tab[j] = new Point2f(
					Float.parseFloat(comp[i-1]),
					Float.parseFloat(comp[i]));
		}
		if (addPt) {
			tab[tab.length-1]  = new Point2f(
					Float.parseFloat(comp[comp.length-1]),
					0);
		}
		return tab;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2D[] getPolyline() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case POINT:
				return new Point2D[] {
						new Point2f(((Point2D)this.value).getX(),
								((Point2D)this.value).getY())
				};
			case POINT3D:
				Point3D pt3 = (Point3D)this.value;
				return new Point2D[] {
					new Point2f(pt3.getX(),pt3.getY()) 
				};
			case POLYLINE:
				return (Point2D[])this.value;
			case POLYLINE3D:
			{
				Point3D[] current = (Point3D[])this.value;
				Point2D[] tab = new Point2D[current.length];
				for(int i=0; i<current.length; ++i) {
					tab[i] = new Point2f(
								current[i].getX(),
								current[i].getY()); 
				}
				return tab;
			}
			case STRING:
				return parsePolyline((String)this.value, false);
			case OBJECT:
				if (this.value instanceof Tuple2D) {
					return new Point2D[] {
							new Point2f(((Tuple2D<?>)this.value).getX(),
									((Tuple2D<?>)this.value).getY())
					};
				}
				else if (this.value instanceof Tuple3D) {
					Tuple3D<?> t3 = (Tuple3D<?>)this.value;
					return new Point2D[] {
						new Point2f(t3.getX(),t3.getY()) 
					};
				}
				else if (this.value instanceof Point2D[]) {
					return (Point2D[])this.value;
				}
				else if (this.value instanceof Tuple2D[]) {
					Tuple2D<?>[] ta2 = (Tuple2D[])this.value;
					Point2D[] pa2 = new Point2D[ta2.length];
					for(int i=0; i<pa2.length; ++i) pa2[i] = new Point2f(ta2[i]);
				}
				else if (this.value instanceof Tuple3D[]) {
					Tuple3D<?>[] ta3 = (Tuple3D[])this.value;
					Point2D[] pa2 = new Point2D[ta3.length];
					for(int i=0; i<pa2.length; ++i) pa2[i] = new Point2f(ta3[i].getX(), ta3[i].getY());
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}			
		throw new InvalidAttributeTypeException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPolyline(Point2D... value) {
		this.value = value;
		this.type = AttributeType.POLYLINE;
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPolyline(Collection<? extends Point2D> value) {
		if (value==null) {
			this.value = null;
		}
		else {
			Point2D[] tab = new Point2D[value.size()];
			value.toArray(tab);
			this.value = tab;
		}
		this.value = value;
		this.type = AttributeType.POLYLINE;
		this.assigned = this.value!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addToPolyline(Point2D... p) {
		Point2D[] tab;
		if (this.value instanceof Point2D[]) {
			int size = ((Point2D[])this.value).length;
			tab = new Point2D[size+p.length];
			System.arraycopy(this.value,0,tab,0,size);
			System.arraycopy(this.value,0,tab,size,p.length);
		}
		else {
			tab = p;
		}
		this.value = tab;
		this.assigned = this.value!=null;
		this.type = AttributeType.POLYLINE;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addToPolyline(Collection<? extends Point2D> p) {
		Point2D[] tab;
		if (this.value instanceof Point2D[]) {
			int size = ((Point2D[])this.value).length;
			tab = new Point2D[size+p.size()];
			System.arraycopy(this.value,0,tab,0,size);
			System.arraycopy(this.value,0,tab,size,p.size());
		}
		else {
			tab = new Point2D[p.size()];
			p.toArray(tab);
		}
		this.value = tab;
		this.assigned = this.value!=null;
		this.type = AttributeType.POLYLINE;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public InetAddress getInetAddress() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case INET_ADDRESS:
				return (InetAddress)this.value;
			case STRING:
				try {
					InetAddress adr = InetAddress.getByName(this.value.toString());
					if (adr!=null) return adr;
				}
				catch(Throwable _) {
					//
				}
				break;
			case OBJECT:
				if (this.value instanceof InetAddress)
					return (InetAddress)this.value;
				if (this.value instanceof InetSocketAddress)
					return ((InetSocketAddress)this.value).getAddress();
				if (this.value!=null) {
					InetAddress adr;
					try {
						adr = InetAddress.getByName(this.value.toString());
						if (adr!=null) return adr;
					}
					catch(Throwable _) {
						//
					}
				}
				else {
					return null;
				}
				break;
			case URI:
				URI uri = (URI)this.value;
				return InetAddress.getByName(uri.getHost());
			case URL:
				URL url = (URL)this.value;
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
		}
		catch(ClassCastException _) {
			//
		}
		catch(NumberFormatException _) {
			//
		}
		catch (UnknownHostException e) {
			//
		}
		throw new InvalidAttributeTypeException();		
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setInetAddress(InetAddress a) {
		this.value = a;
		this.type = AttributeType.INET_ADDRESS;		
		this.assigned = this.value!=null;
	}

	/**
	 * Set the value of this metadata.
	 * 
	 * @param a
	 */
	public void setInetAddress(InetSocketAddress a) {
		this.value = a.getAddress();
		this.type = AttributeType.INET_ADDRESS;				
		this.assigned = this.value!=null;
	}

	/**
	 * Set the value of this metadata.
	 * 
	 * @param hostname
	 */
	public void setInetAddress(String hostname) {
		this.type = AttributeType.INET_ADDRESS;				
		try {
			this.value = InetAddress.getByName(hostname);
		}
		catch(Throwable _) {
			this.value = null;
		}
		this.assigned = this.value!=null;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Enum<?> getEnumeration() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		if (this.value==null) return null;
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case ENUMERATION:
				return (Enum<?>)this.value;
			case STRING:
				int index = ((String)this.value).lastIndexOf('.');
				if (index>=0) {
					String classname = ((String)this.value).substring(0, index);
					String enumName = ((String)this.value).substring(index+1);
					Class classType = Class.forName(classname);
					if (Enum.class.isAssignableFrom(classType)) {
						return Enum.valueOf(classType, enumName);
					}
				}
				break;
			case OBJECT:
				if (this.value instanceof Enum<?>)
					return (Enum<?>)this.value;
				
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
		}
		catch(Throwable _) {
			//
		}
		throw new InvalidAttributeTypeException();		
	}

	/** {@inheritDoc}
	 */
	@Override
	public <T extends Enum<T>> T getEnumeration(Class<T> type) throws InvalidAttributeTypeException, AttributeNotInitializedException {
		if (this.value==null) return null;
		try {
			assertAssignedAndNotNull();
			switch(this.type) {
			case ENUMERATION:
				return type.cast(this.value);
			case STRING:
				int index = ((String)this.value).lastIndexOf('.');
				if (index>=0) {
					String classname = ((String)this.value).substring(0, index);
					String enumName = ((String)this.value).substring(index+1);
					Class<?> classType = Class.forName(classname);
					assert(type.equals(classType));
					return Enum.valueOf(type, enumName);
				}
				break;
			case OBJECT:
				if (this.value instanceof Enum<?>)
					return type.cast(this.value);
				
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
		}
		catch(Throwable _) {
			//
		}
		throw new InvalidAttributeTypeException();		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnumeration(Enum<?> e) {
		this.value = e;
		this.type = AttributeType.ENUMERATION;		
		this.assigned = this.value!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getJavaClass() throws InvalidAttributeTypeException, AttributeNotInitializedException {
		assertAssignedAndNotNull();
		try {
			switch(this.type) {
			case TYPE:
			case OBJECT:
				return (Class<?>)this.value;
			case STRING:
				return Class.forName((String)this.value);
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
		}
		catch(Throwable _) {
			//
		}
		throw new InvalidAttributeTypeException();		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJavaClass(Class<?> e) {
		this.value = e;
		this.type = AttributeType.TYPE;		
		this.assigned = this.value!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAssigned() {
		return this.assigned;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uninitializeValue() {
		this.value = null;
		this.assigned = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean flush() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comparator<? extends AttributeValue> valueComparator() {
		return new AttributeValueComparator();
	}

}
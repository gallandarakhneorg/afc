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
 * version 2.1 of the License, or (at your option) any later version.
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
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.math.generic.Tuple2D;
import org.arakhne.afc.math.generic.Tuple3D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Colors;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.VectorToolkit;
import org.arakhne.vmutil.locale.Locale;

/**
 * List of supported types for the metadata.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum AttributeType {

	/** Represents an enumeration.
	 * @see Enum
	 */
	ENUMERATION,

	/** Represents a Java type.
	 */
	TYPE,

	/** Represents an unique universal identifier.
	 */
	UUID,

	/** Represents an integer.
	 */
	INTEGER,
	
	/** Represents a floating number.
	 */
	REAL,
	
	/** Represents a date.
	 */
	DATE,
	
	/** Represents a boolean value.
	 */
	BOOLEAN,
	
	/** Represents an Internet address.
	 * @see Inet4Address
	 * @see Inet6Address
	 * @see InetSocketAddress
	 */
	INET_ADDRESS,

	/** Represents a color value.
	 */
	COLOR,

	/** Represents an URL.
	 * @see java.net.URL
	 */
	URL,

	/** Represents an URI.
	 * @see java.net.URI
	 */
	URI,

	/** Represents a timestamp value.
	 */
	TIMESTAMP,

	/** Represents a 3d point value.
	 */
	POINT3D,

	/** Represents a 2d point value.
	 */
	POINT,

	/** Represents a list of 3d points.
	 */
	POLYLINE3D,

	/** Represents a list of 2d points.
	 */
	POLYLINE,

	/** Represents an image value.
	 */
	IMAGE,
		
	/** Represents a string.
	 */
	STRING,
	
	/** Represents a java-object value.
	 */
	OBJECT;	
	
	private static final String NAME_RESOURCE_FILE;
	
	static {
		String pName = AttributeType.class.getPackage().getName();
		NAME_RESOURCE_FILE = pName+".types"; //$NON-NLS-1$
	}
	
	/** Replies the name of this type (localized).
	 * 
	 * @return the localized name of this type.
	 */
	public String getName() {
		switch(this) {
		case INTEGER:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"INTEGER"); //$NON-NLS-1$ 
		case REAL:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"FLOAT"); //$NON-NLS-1$ 
		case STRING:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"STRING"); //$NON-NLS-1$ 
		case BOOLEAN:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"BOOLEAN"); //$NON-NLS-1$ 
		case DATE:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"DATE"); //$NON-NLS-1$ 
		case TIMESTAMP:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"TIMESTAMP"); //$NON-NLS-1$ 
		case OBJECT:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"OBJECT"); //$NON-NLS-1$ 
		case POINT3D:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"POINT3D"); //$NON-NLS-1$ 
		case POINT:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"POINT2D"); //$NON-NLS-1$ 
		case COLOR:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"COLOR"); //$NON-NLS-1$ 
		case IMAGE:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"ICON"); //$NON-NLS-1$ 
		case POLYLINE:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"POLYLINE"); //$NON-NLS-1$ 
		case POLYLINE3D:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"POLYLINE3D"); //$NON-NLS-1$ 
		case UUID:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"UUID"); //$NON-NLS-1$ 
		case URL:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"URL"); //$NON-NLS-1$ 
		case URI:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"URI"); //$NON-NLS-1$ 
		case INET_ADDRESS:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"INET_ADDRESS"); //$NON-NLS-1$ 
		case ENUMERATION:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"ENUMERATION"); //$NON-NLS-1$ 
		case TYPE:
			return Locale.getStringFrom(NAME_RESOURCE_FILE,"TYPE"); //$NON-NLS-1$ 
		default:
		}		
		return Locale.getStringFrom(NAME_RESOURCE_FILE,"OTHER"); //$NON-NLS-1$ 
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

	/** Replies the Attribute type that corresponds to the
	 * specified internal code.
	 * 
	 * @param type is an integer representing an attribute type.
	 * @return the type that corresponds to the given integer.
	 */
	public static AttributeType fromInteger(int type) {
		AttributeType[] vals = values();
		if ((vals!=null)&&(type>=0)&&(type<vals.length))
			return vals[type];
		return OBJECT;
	}

	/** Replies the Attribute type that corresponds to the
	 * specified value.
	 * 
	 *  @param value is the value to test.
	 *  @return the type that corresponds to the given value.
	 */
	public static AttributeType fromValue(Object value) {
		if (value!=null) {
			if (value instanceof NullAttribute)
				return ((NullAttribute)value).getType();
			return fromClass(value.getClass());
		}
		return OBJECT;
	}

	/** Replies the Attribute type that corresponds to the
	 * specified type.
	 * 
	 *  @param type is the type to test.
	 *  @return the type that corresponds to the given value.
	 */
	public static AttributeType fromClass(Class<?> type) {
		if (type!=null) {
			if (byte.class.isAssignableFrom(type)) return INTEGER;
			if (Byte.class.isAssignableFrom(type)) return INTEGER;
			if (short.class.isAssignableFrom(type)) return INTEGER;
			if (Short.class.isAssignableFrom(type)) return INTEGER;
			if (int.class.isAssignableFrom(type)) return INTEGER;
			if (Integer.class.isAssignableFrom(type)) return INTEGER;
			if (long.class.isAssignableFrom(type)) return INTEGER;
			if (Long.class.isAssignableFrom(type)) return INTEGER;
			if (BigInteger.class.isAssignableFrom(type)) return INTEGER;
			if (AtomicInteger.class.isAssignableFrom(type)) return INTEGER;
			if (AtomicLong.class.isAssignableFrom(type)) return INTEGER;

			if (Timestamp.class.isAssignableFrom(type)) return TIMESTAMP;
			
			if (float.class.isAssignableFrom(type)) return REAL;
			if (double.class.isAssignableFrom(type)) return REAL;
			if (Number.class.isAssignableFrom(type)) return REAL;

			if (char.class.isAssignableFrom(type)) return STRING;
			if (Character.class.isAssignableFrom(type)) return STRING;
			if (CharSequence.class.isAssignableFrom(type)) return STRING;
	
			if (boolean.class.isAssignableFrom(type)) return BOOLEAN;
			if (Boolean.class.isAssignableFrom(type)) return BOOLEAN;
	
			if (Date.class.isAssignableFrom(type)) return DATE;
			if (Calendar.class.isAssignableFrom(type)) return DATE;
	
			if (Tuple3D.class.isAssignableFrom(type)) return POINT3D;

			if (Tuple2D.class.isAssignableFrom(type)) return POINT;

			if (Color.class.isAssignableFrom(type)) return COLOR;

			if (UUID.class.isAssignableFrom(type)) return UUID;

			if (java.net.URL.class.isAssignableFrom(type)) return URL;

			if (java.net.URI.class.isAssignableFrom(type)) return URI;

			if (Image.class.isAssignableFrom(type)) return IMAGE;

			if (InetAddress.class.isAssignableFrom(type)) return INET_ADDRESS;
			if (InetSocketAddress.class.isAssignableFrom(type)) return INET_ADDRESS;

			if (type.isArray()) {
				Class<?> elementType = type.getComponentType();
				if (Point2D.class.isAssignableFrom(elementType)) return POLYLINE;
				if (Point3D.class.isAssignableFrom(elementType)) return POLYLINE3D;
			}


			if (Enum.class.isAssignableFrom(type)) return ENUMERATION;

			if (Class.class.isAssignableFrom(type)) return TYPE;
		}
		return OBJECT;
	}

	/** Replies if the specified value is an instanceof the type..
	 * 
	 *  @param value is the value to test.
	 *  @return <code>true</code> if the given value is an instance of
	 *  this attribute type, otherwise <code>false</code>.
	 */
	public boolean instanceOf(Object value) {
		return this == fromValue(value);
	}

	/** Replies the default value for the specified type.
	 * 
	 * @return the default value.
	 */
	public Object getDefaultValue() {
		switch(this) {
		case INTEGER:
			return new Long(0);
		case REAL:
			return new Double(0);
		case STRING:
			return new String();
		case BOOLEAN:
			return Boolean.FALSE;
		case DATE:
			return new Date();
		case TIMESTAMP:
			return new Timestamp(System.currentTimeMillis());
		case POINT3D:
			return new Point3f();
		case POINT:
			return new Point2f();
		case COLOR:
			return Colors.BLACK;
		case UUID:
			return java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"); //$NON-NLS-1$
		case URL:
			return null;
		case URI:
			return null;
		case POLYLINE3D:
			return new Point3D[0];
		case POLYLINE:
			return new Point2D[0];
		case OBJECT:
			return null;
		case IMAGE:
			return null;
		case INET_ADDRESS:
			try {
				return InetAddress.getLocalHost();
			}
			catch (UnknownHostException _) {
				return null;
			}
		case ENUMERATION:
			return null;
		case TYPE:
		default:
			return Object.class;
		}		
	}

	/**
	 * Replies if this attribute type is
	 * a base type, ie. a number, a boolean
	 * or a string.
	 * 
	 * @return <code>true</code> if this type is a base type,
	 * otherwise <code>false</code>
	 */
	public boolean isBaseType() {
		return this==INTEGER
			|| this==REAL
			|| this==TIMESTAMP
			|| this==BOOLEAN
			|| this==STRING;
	}

	/**
	 * Replies if this attribute type is
	 * a number type.
	 * A number type is always a base type.
	 * 
	 * @return <code>true</code> if this type is a number type,
	 * otherwise <code>false</code>
	 * @since 4.0
	 */
	public boolean isNumberType() {
		return this==INTEGER
			|| this==REAL
			|| this==TIMESTAMP;
	}

	/**
	 * Replies if a null value is allowed for this attribute type.
	 * 
	 * @return <code>true</code> if this type allows <code>null</code> value,
	 * otherwise <code>false</code>
	 */
	public boolean isNullAllowed() {
		return this==OBJECT
			|| this==IMAGE
			|| this==URI
			|| this==URL
			|| this==INET_ADDRESS
			|| this==ENUMERATION;
	}
	
	/** Replies if a value of the given attribute type may
	 * be cast to a value of this attribute type.
	 * <p>
	 * Caution: even if isAssignableFrom is replying <code>true</code>,
	 * the {@link AttributeValue#cast(AttributeType)} and
	 * {@link AttributeValue#castAndSet(AttributeType, Object)} may fail
	 * if the target type does not support a specifical value of the
	 * source type. The isAssignableFrom function replies <code>true</code>
	 * if a least one value of the source type is assignable to a value
	 * of the target type.
	 * 
	 * @param type
	 * @return <code>true</code> if a value of the given
	 * <var>type</var> may be cast to a value of <code>this</code>;
	 * otherwise <code>false</code>.
	 * @since 4.0
	 */
	public boolean isAssignableFrom(AttributeType type) {
		switch(this) {
		case INTEGER:
		case REAL:
			return type==INTEGER || type==REAL || type==TIMESTAMP || type==STRING || type==DATE || type==BOOLEAN || type==COLOR || type==ENUMERATION ||  type==OBJECT;
		case TIMESTAMP:
			return type==INTEGER || type==REAL || type==TIMESTAMP || type==STRING || type==DATE || type==BOOLEAN || type==COLOR || type==OBJECT;
		case BOOLEAN:
			return type==BOOLEAN || type==STRING || type==INTEGER || type==TIMESTAMP || type==REAL || type==OBJECT;
		case DATE:
			return type==DATE || type==REAL || type==INTEGER || type==TIMESTAMP || type==STRING || type==OBJECT;
		case POINT3D:
		case POINT:
			return type==POINT || type==POINT3D || type==COLOR || type==REAL || type==INTEGER || type==TIMESTAMP || type==DATE || type==STRING || type==OBJECT;
		case COLOR:
			return type==COLOR || type==POINT || type==POINT3D || type==STRING || type==INTEGER || type==REAL || type==TIMESTAMP || type==DATE || type==OBJECT;
		case URL:
			return type==URI || type==URL || type==INET_ADDRESS || type==STRING || type==OBJECT;
		case URI:
			return type==URI || type==URL || type==INET_ADDRESS || type==STRING || type==UUID || type==OBJECT;
		case POLYLINE3D:
		case POLYLINE:
			return type==POLYLINE || type==POLYLINE3D || type==POINT || type==POINT3D || type==STRING || type==OBJECT;
		case IMAGE:
			return type==IMAGE || type==OBJECT;
		case INET_ADDRESS:
			return type==INET_ADDRESS || type==STRING || type==URL || type==URI  || type==OBJECT;
		case ENUMERATION:
			return type==ENUMERATION || type==STRING || type==OBJECT;
		case TYPE:
			return type==TYPE || type==STRING || type==OBJECT;
		case UUID:
		case STRING:
		case OBJECT:
			return true;
		default:
		}
		return false;
	}

	/** Cast the specified value to corresponds to the
	 * default storage standard for attributes.
	 * 
	 * @param obj is the object to cast
	 * @return the casted value
	 * @throws ClassCastException if is impossible to cast.
	 * @throws NullPointerException if null value is not allowed.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object cast(Object obj) {
		if (obj instanceof NullAttribute) return null;
		switch(this) {
		case INTEGER:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof Enum<?>) return (long)((Enum<?>)obj).ordinal();
			return ((Number)obj).longValue();
		case REAL:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof Enum<?>) return (double)((Enum<?>)obj).ordinal();
			return ((Number)obj).doubleValue();
		case STRING:
			if (obj==null) return ""; //$NON-NLS-1$
			if (obj instanceof Enum<?>) {
				Enum<?> enumValue = (Enum<?>)obj;
				return enumValue.getClass().getCanonicalName()
						+"." //$NON-NLS-1$
						+enumValue.name();
			}
			return obj.toString();
		case BOOLEAN:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			return Boolean.class.cast(obj);
		case DATE:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof Number)
				return new Date(((Number)obj).longValue());
			if (obj instanceof Calendar) return ((Calendar)obj).getTime();
			return Date.class.cast(obj);
		case TIMESTAMP:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof Calendar) return ((Calendar)obj).getTimeInMillis();
			if (obj instanceof Date) return ((Date)obj).getTime();
			if (obj instanceof Number && !(obj instanceof Timestamp)) {
				return new Timestamp(((Number)obj).longValue());
			}
			return Timestamp.class.cast(obj);
		case POINT3D:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof Tuple3D && !(obj instanceof Point3D)) {
				return new Point3f((Tuple3D)obj);
			}
			return Point3D.class.cast(obj);
		case POINT:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof Tuple2D && !(obj instanceof Point2D)) {
				return new Point2f((Tuple2D)obj);
			}
			return Point2D.class.cast(obj);
		case COLOR:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof Number) {
				return VectorToolkit.color(((Number)obj).intValue());
			}
			return Color.class.cast(obj);
		case UUID:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			return UUID.class.cast(obj);
		case URL:
			// Possible ClassCastException
			if (obj==null) return null;
			if (obj instanceof java.net.URI) {
				try {
					return ((java.net.URI)obj).toURL();
				}
				catch (MalformedURLException e) {
					//
				}
			}
			if (obj instanceof InetAddress) {
				try {
					return new java.net.URL(AttributeConstants.DEFAULT_SCHEME.name(), ((InetAddress)obj).getHostAddress(), ""); //$NON-NLS-1$
				}
				catch (MalformedURLException e) {
					//
				}
			}
			if (obj instanceof InetSocketAddress) {
				try {
					return new java.net.URL(AttributeConstants.DEFAULT_SCHEME.name(), ((InetSocketAddress)obj).getAddress().getHostAddress(), ""); //$NON-NLS-1$
				}
				catch (MalformedURLException e) {
					//
				}
			}
			return java.net.URL.class.cast(obj);
		case URI:
			// Possible ClassCastException
			if (obj==null) return null;
			if (obj instanceof java.net.URL) {
				try {
					return ((java.net.URL)obj).toURI();
				}
				catch (URISyntaxException e) {
					//
				}
			}
			if (obj instanceof InetAddress) {
				try {
					return new java.net.URI(AttributeConstants.DEFAULT_SCHEME.name(), ((InetAddress)obj).getHostAddress(), ""); //$NON-NLS-1$
				}
				catch (URISyntaxException e) {
					//
				}
			}
			if (obj instanceof InetSocketAddress) {
				try {
					return new java.net.URI(AttributeConstants.DEFAULT_SCHEME.name(), ((InetSocketAddress)obj).getAddress().getHostAddress(), ""); //$NON-NLS-1$
				}
				catch (URISyntaxException e) {
					//
				}
			}
			return java.net.URI.class.cast(obj);
		case POLYLINE3D:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj.getClass().isArray()) {
				Class<?> elementType = obj.getClass().getComponentType();
				if (Tuple3D.class.isAssignableFrom(elementType) &&
					!Point3D.class.isAssignableFrom(elementType)) {
					int length = Array.getLength(obj);
					Point3D[] tab = new Point3D[length];
					for(int i=0; i<length; ++i)
						tab[i] = new Point3f((Tuple3D)Array.get(obj, i));
					return tab;
				}
			}
			return Point3D[].class.cast(obj);
		case POLYLINE:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj.getClass().isArray()) {
				Class<?> elementType = obj.getClass().getComponentType();
				if (Tuple2D.class.isAssignableFrom(elementType) &&
					!Point2D.class.isAssignableFrom(elementType)) {
					int length = Array.getLength(obj);
					Point2D[] tab = new Point2D[length];
					for(int i=0; i<length; ++i)
						tab[i] = new Point2f((Tuple2D)Array.get(obj, i));
					return tab;
				}
			}
			return Point2D[].class.cast(obj);
		case IMAGE:
			if (obj==null) return null;
			return Image.class.cast(obj);
		case OBJECT:
			if (obj==null) return null;
			break;
		case INET_ADDRESS:
			if (obj==null) return null;
			if (obj instanceof InetSocketAddress) {
				return ((InetSocketAddress)obj).getAddress();
			}
			if (obj instanceof java.net.URL) {
				java.net.URL url = (java.net.URL)obj;
				try {
					return InetAddress.getByName(url.getHost());
				}
				catch (UnknownHostException _) {
					//
				}
			}
			if (obj instanceof java.net.URI) {
				java.net.URI uri = (java.net.URI)obj;
				try {
					return InetAddress.getByName(uri.getHost());
				}
				catch (UnknownHostException _) {
					//
				}
			}
			if (obj instanceof CharSequence) {
				try {
					String ipStr = obj.toString();
					int index = ipStr.lastIndexOf("/"); //$NON-NLS-1$
					if (index>=0) {
						try {
							return InetAddress.getByName(ipStr.substring(index+1));
						}
						catch (UnknownHostException _) {
							//
						}
					}
					return InetAddress.getByName(ipStr);
				}
				catch (UnknownHostException _) {
					//
				}
			}
			return InetAddress.class.cast(obj);
		case ENUMERATION:
			if (obj==null) return null;
			if (obj instanceof CharSequence) {
				String enumStr = obj.toString();
				int index = enumStr.lastIndexOf('.');
				if (index>0) {
					String enumName = enumStr.substring(0, index);
					String constantName = enumStr.substring(index+1);
					try {
						Class type = Class.forName(enumName);
						if (Enum.class.isAssignableFrom(type)) {
							 Enum<?> v = Enum.valueOf(type, constantName.toUpperCase());
							 if (v!=null) return v;
						}
					}
					catch(Throwable _) {
						//
					}
				}
			}
			return Enum.class.cast(obj);
		case TYPE:
			// Possible ClassCastException
			if (obj==null) throw new NullPointerException();
			if (obj instanceof CharSequence) {
				try {
					return Class.forName(((CharSequence)obj).toString());
				}
				catch (ClassNotFoundException e) {
					//
				}
			}
			return Class.class.cast(obj);
		default:
			throw new ClassCastException();
		}
		return obj;
	}
	
}
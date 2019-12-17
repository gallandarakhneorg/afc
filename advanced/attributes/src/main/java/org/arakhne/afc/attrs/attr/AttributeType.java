/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * List of supported types for the metadata.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum AttributeType {

	/** Represents an enumeration.
	 * @see Enum
	 */
	ENUMERATION {
		@Override
		public Object getDefaultValue() {
			return null;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Object cast(Object obj) {
			if (obj == null || obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof CharSequence) {
				final String enumStr = obj.toString();
				final int index = enumStr.lastIndexOf('.');
				if (index > 0) {
					final String enumName = enumStr.substring(0, index);
					final String constantName = enumStr.substring(index + 1);
					try {
						final Class type = Class.forName(enumName);
						if (Enum.class.isAssignableFrom(type)) {
							final Enum<?> v = Enum.valueOf(type, constantName.toUpperCase());
							if (v != null) {
								return v;
							}
						}
					} catch (Throwable exception) {
						//
					}
				}
			}
			return Enum.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return true;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == ENUMERATION || type == STRING || type == OBJECT;
		}
	},

	/** Represents a Java type.
	 */
	TYPE {
		@Override
		public Object getDefaultValue() {
			return Object.class;
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof CharSequence) {
				try {
					return Class.forName(((CharSequence) obj).toString());
				} catch (ClassNotFoundException e) {
					//
				}
			}
			return Class.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == TYPE || type == STRING || type == OBJECT;
		}
	},

	/** Represents an unique universal identifier.
	 */
	UUID {
		@Override
		public Object getDefaultValue() {
			return java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"); //$NON-NLS-1$
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			return java.util.UUID.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return true;
		}
	},

	/** Represents an integer.
	 */
	INTEGER {
		@Override
		public Object getDefaultValue() {
			return Long.valueOf(0);
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			final long value;
			if (obj instanceof Enum<?>) {
				value = ((Enum<?>) obj).ordinal();
			} else {
				value = ((Number) obj).longValue();
			}
			return Long.valueOf(value);
		}

		@Override
		public boolean isNumberType() {
			return true;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return true;
		}

		@Override
		@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
		public boolean isAssignableFrom(AttributeType type) {
			return type == INTEGER || type == REAL || type == TIMESTAMP || type == STRING
					|| type == DATE || type == BOOLEAN || 	type == ENUMERATION ||  type == OBJECT;
		}
	},

	/** Represents a floating number.
	 */
	REAL {
		@Override
		public Object getDefaultValue() {
			return Double.valueOf(0);
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			final double value;
			if (obj instanceof Enum<?>) {
				value = ((Enum<?>) obj).ordinal();
			} else {
				value = ((Number) obj).doubleValue();
			}
			return Double.valueOf(value);
		}

		@Override
		public boolean isNumberType() {
			return true;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return true;
		}

		@Override
		@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
		public boolean isAssignableFrom(AttributeType type) {
			return type == INTEGER || type == REAL || type == TIMESTAMP || type == STRING
					|| type == DATE || type == BOOLEAN || type == ENUMERATION ||  type == OBJECT;
		}
	},

	/** Represents a date.
	 */
	DATE {
		@Override
		public Object getDefaultValue() {
			return new Date();
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof Number) {
				return new Date(((Number) obj).longValue());
			}
			if (obj instanceof Calendar) {
				return ((Calendar) obj).getTime();
			}
			return Date.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == DATE || type == REAL || type == INTEGER || type == TIMESTAMP || type == STRING || type == OBJECT;
		}
	},

	/** Represents a boolean value.
	 */
	BOOLEAN {
		@Override
		public Object getDefaultValue() {
			return Boolean.FALSE;
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			return Boolean.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return true;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == BOOLEAN || type == STRING || type == INTEGER || type == TIMESTAMP || type == REAL || type == OBJECT;
		}
	},

	/** Represents an Internet address.
	 * @see Inet4Address
	 * @see Inet6Address
	 * @see InetSocketAddress
	 */
	INET_ADDRESS {
		@Override
		public Object getDefaultValue() {
			try {
				return InetAddress.getLocalHost();
			} catch (UnknownHostException exception) {
				return null;
			}
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null || obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof InetSocketAddress) {
				return ((InetSocketAddress) obj).getAddress();
			}
			if (obj instanceof java.net.URL) {
				final java.net.URL url = (java.net.URL) obj;
				try {
					return InetAddress.getByName(url.getHost());
				} catch (UnknownHostException exception) {
					//
				}
			}
			if (obj instanceof java.net.URI) {
				final java.net.URI uri = (java.net.URI) obj;
				try {
					return InetAddress.getByName(uri.getHost());
				} catch (UnknownHostException exception) {
					//
				}
			}
			if (obj instanceof CharSequence) {
				return getInetAddressFromCharacterSequence(obj);
			}
			return InetAddress.class.cast(obj);
		}

		private InetAddress getInetAddressFromCharacterSequence(Object obj) {
			try {
				final String ipStr = obj.toString();
				final int index = ipStr.lastIndexOf("/"); //$NON-NLS-1$
				if (index >= 0) {
					return InetAddress.getByName(ipStr.substring(index + 1));
				}
				return InetAddress.getByName(ipStr);
			} catch (UnknownHostException exception) {
				//
			}
			return null;
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return true;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == INET_ADDRESS || type == STRING || type == URL || type == URI  || type == OBJECT;
		}
	},

	/** Represents an URL.
	 * @see java.net.URL
	 */
	URL {
		@Override
		public Object getDefaultValue() {
			return null;
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null || obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof java.net.URI) {
				try {
					return ((java.net.URI) obj).toURL();
				} catch (MalformedURLException e) {
					//
				}
			}
			if (obj instanceof InetAddress) {
				try {
					return new java.net.URL(AttributeConstants.DEFAULT_SCHEME.name(),
							((InetAddress) obj).getHostAddress(), ""); //$NON-NLS-1$
				} catch (MalformedURLException e) {
					//
				}
			}
			if (obj instanceof InetSocketAddress) {
				try {
					return new java.net.URL(AttributeConstants.DEFAULT_SCHEME.name(),
							((InetSocketAddress) obj).getAddress().getHostAddress(), ""); //$NON-NLS-1$
				} catch (MalformedURLException e) {
					//
				}
			}
			return java.net.URL.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return true;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == URI || type == URL || type == INET_ADDRESS || type == STRING || type == OBJECT;
		}
	},

	/** Represents an URI.
	 * @see java.net.URI
	 */
	URI {
		@Override
		public Object getDefaultValue() {
			return null;
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null || obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof java.net.URL) {
				try {
					return ((java.net.URL) obj).toURI();
				} catch (URISyntaxException e) {
					//
				}
			}
			if (obj instanceof InetAddress) {
				try {
					return new java.net.URI(AttributeConstants.DEFAULT_SCHEME.name(),
							((InetAddress) obj).getHostAddress(), ""); //$NON-NLS-1$
				} catch (URISyntaxException e) {
					//
				}
			}
			if (obj instanceof InetSocketAddress) {
				try {
					return new java.net.URI(AttributeConstants.DEFAULT_SCHEME.name(),
							((InetSocketAddress) obj).getAddress().getHostAddress(), ""); //$NON-NLS-1$
				} catch (URISyntaxException e) {
					//
				}
			}
			return java.net.URI.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return true;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == URI || type == URL || type == INET_ADDRESS || type == STRING || type == UUID || type == OBJECT;
		}
	},

	/** Represents a timestamp value.
	 */
	TIMESTAMP {
		@Override
		public Object getDefaultValue() {
			return new Timestamp(System.currentTimeMillis());
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof Calendar) {
				return ((Calendar) obj).getTimeInMillis();
			}
			if (obj instanceof Date) {
				return ((Date) obj).getTime();
			}
			if (obj instanceof Number && !(obj instanceof Timestamp)) {
				return ((Number) obj).longValue();
			}
			return Timestamp.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return true;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return true;
		}

		@Override
		@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
		public boolean isAssignableFrom(AttributeType type) {
			return type == INTEGER || type == REAL || type == TIMESTAMP || type == STRING || type == DATE
					|| type == BOOLEAN || type == OBJECT;
		}
	},

	/** Represents a 3d point value.
	 */
	POINT3D {
		@Override
		public Object getDefaultValue() {
			return new Point3d();
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof Tuple3D && !(obj instanceof Point3D)) {
				return new Point3d((Tuple3D<?>) obj);
			}
			return Point3D.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
		public boolean isAssignableFrom(AttributeType type) {
			return type == POINT || type == POINT3D || type == REAL || type == INTEGER
					|| type == TIMESTAMP || type == DATE || type == STRING || type == OBJECT;
		}
	},

	/** Represents a 2d point value.
	 */
	POINT {
		@Override
		public Object getDefaultValue() {
			return new Point2d();
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof Tuple2D && !(obj instanceof Point2D)) {
				return new Point2d((Tuple2D<?>) obj);
			}
			return Point2D.class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
		public boolean isAssignableFrom(AttributeType type) {
			return type == POINT || type == POINT3D || type == REAL || type == INTEGER
					|| type == TIMESTAMP || type == DATE || type == STRING || type == OBJECT;
		}
	},

	/** Represents a list of 3d points.
	 */
	POLYLINE3D {
		@Override
		public Object getDefaultValue() {
			return  new Point3D[0];
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj.getClass().isArray()) {
				final Class<?> elementType = obj.getClass().getComponentType();
				if (Tuple3D.class.isAssignableFrom(elementType)
						&& !Point3D.class.isAssignableFrom(elementType)) {
					final int length = Array.getLength(obj);
					final Point3D<?, ?>[] tab = new Point3D[length];
					for (int i = 0; i < length; ++i) {
						tab[i] = new Point3d((Tuple3D<?>) Array.get(obj, i));
					}
					return tab;
				}
			}
			return Point3D[].class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == POLYLINE || type == POLYLINE3D || type == POINT || type == POINT3D || type == STRING || type == OBJECT;
		}
	},

	/** Represents a list of 2d points.
	 */
	POLYLINE {
		@Override
		public Object getDefaultValue() {
			return new Point2D[0];
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				throw new NullPointerException();
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj.getClass().isArray()) {
				final Class<?> elementType = obj.getClass().getComponentType();
				if (Tuple2D.class.isAssignableFrom(elementType)
						&& !Point2D.class.isAssignableFrom(elementType)) {
					final int length = Array.getLength(obj);
					final Point2D<?, ?>[] tab = new Point2D[length];
					for (int i = 0; i < length; ++i) {
						tab[i] = new Point2d((Tuple2D<?>) Array.get(obj, i));
					}
					return tab;
				}
			}
			return Point2D[].class.cast(obj);
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return type == POLYLINE || type == POLYLINE3D || type == POINT || type == POINT3D || type == STRING || type == OBJECT;
		}
	},

	/** Represents a string.
	 */
	STRING {
		@Override
		public Object getDefaultValue() {
			return new String();
		}

		@Override
		public Object cast(Object obj) {
			if (obj == null) {
				return ""; //$NON-NLS-1$
			}
			if (obj instanceof NullAttribute) {
				return null;
			}
			if (obj instanceof Enum<?>) {
				final Enum<?> enumValue = (Enum<?>) obj;
				return enumValue.getClass().getCanonicalName()
						+ "." //$NON-NLS-1$
						+ enumValue.name();
			}
			return obj.toString();
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return false;
		}

		@Override
		public boolean isBaseType() {
			return true;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return true;
		}
	},

	/** Represents a java-object value.
	 */
	OBJECT {
		@Override
		public Object getDefaultValue() {
			return null;
		}

		@Override
		public Object cast(Object obj) {
			if (obj instanceof NullAttribute) {
				return null;
			}
			return obj;
		}

		@Override
		public boolean isNumberType() {
			return false;
		}

		@Override
		public boolean isNullAllowed() {
			return true;
		}

		@Override
		public boolean isBaseType() {
			return false;
		}

		@Override
		public boolean isAssignableFrom(AttributeType type) {
			return true;
		}
	};

	private static final String NAME_RESOURCE_FILE;

	private String localizedName;

	static {
		final String pName = AttributeType.class.getPackage().getName();
		NAME_RESOURCE_FILE = pName.replaceAll("\\.",  "/") + "/types"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	/** Replies the name of this type (localized).
	 *
	 * @return the localized name of this type.
	 */
	@Pure
	public String getLocalizedName() {
		if (this.localizedName == null) {
			this.localizedName = Locale.getStringWithDefaultFrom(NAME_RESOURCE_FILE, name(), null);
			if (this.localizedName == null) {
				this.localizedName = Locale.getStringFrom(NAME_RESOURCE_FILE, "OTHER"); //$NON-NLS-1$
			}
		}
		return this.localizedName;
	}

	@Pure
	@Override
	public String toString() {
		return getLocalizedName();
	}

	/** Replies the Attribute type that corresponds to the
	 * specified internal code.
	 *
	 * @param type is an integer representing an attribute type.
	 * @return the type that corresponds to the given integer.
	 */
	@Pure
	public static AttributeType fromInteger(int type) {
		final AttributeType[] vals = values();
		if ((type >= 0) && (type < vals.length)) {
			return vals[type];
		}
		return OBJECT;
	}

	/** Replies the Attribute type that corresponds to the
	 * specified value.
	 *
	 *  @param value is the value to test.
	 *  @return the type that corresponds to the given value.
	 */
	@Pure
	public static AttributeType fromValue(Object value) {
		if (value != null) {
			if (value instanceof NullAttribute) {
				return ((NullAttribute) value).getType();
			}
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
	@Pure
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public static AttributeType fromClass(Class<?> type) {
		if (type != null) {
			if (byte.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (Byte.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (short.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (Short.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (int.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (Integer.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (long.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (Long.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (BigInteger.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (AtomicInteger.class.isAssignableFrom(type)) {
				return INTEGER;
			}
			if (AtomicLong.class.isAssignableFrom(type)) {
				return INTEGER;
			}

			if (Timestamp.class.isAssignableFrom(type)) {
				return TIMESTAMP;
			}

			if (float.class.isAssignableFrom(type)) {
				return REAL;
			}
			if (double.class.isAssignableFrom(type)) {
				return REAL;
			}
			if (Number.class.isAssignableFrom(type)) {
				return REAL;
			}

			if (char.class.isAssignableFrom(type)) {
				return STRING;
			}
			if (Character.class.isAssignableFrom(type)) {
				return STRING;
			}
			if (CharSequence.class.isAssignableFrom(type)) {
				return STRING;
			}

			if (boolean.class.isAssignableFrom(type)) {
				return BOOLEAN;
			}
			if (Boolean.class.isAssignableFrom(type)) {
				return BOOLEAN;
			}

			if (Date.class.isAssignableFrom(type)) {
				return DATE;
			}
			if (Calendar.class.isAssignableFrom(type)) {
				return DATE;
			}

			if (Tuple3D.class.isAssignableFrom(type)) {
				return POINT3D;
			}

			if (Tuple2D.class.isAssignableFrom(type)) {
				return POINT;
			}

			if (UUID.class.isAssignableFrom(type)) {
				return UUID;
			}

			if (java.net.URL.class.isAssignableFrom(type)) {
				return URL;
			}

			if (java.net.URI.class.isAssignableFrom(type)) {
				return URI;
			}

			if (InetAddress.class.isAssignableFrom(type)) {
				return INET_ADDRESS;
			}
			if (InetSocketAddress.class.isAssignableFrom(type)) {
				return INET_ADDRESS;
			}

			if (type.isArray()) {
				final Class<?> elementType = type.getComponentType();
				if (Point2D.class.isAssignableFrom(elementType)) {
					return POLYLINE;
				}
				if (Point3D.class.isAssignableFrom(elementType)) {
					return POLYLINE3D;
				}
			}


			if (Enum.class.isAssignableFrom(type)) {
				return ENUMERATION;
			}

			if (Class.class.isAssignableFrom(type)) {
				return TYPE;
			}
		}
		return OBJECT;
	}

	/** Replies if the specified value is an instanceof the type..
	 *
	 *  @param value is the value to test.
	 *  @return <code>true</code> if the given value is an instance of
	 *      this attribute type, otherwise <code>false</code>.
	 */
	@Pure
	public boolean instanceOf(Object value) {
		return this == fromValue(value);
	}

	/** Replies the default value for the specified type.
	 *
	 * @return the default value.
	 */
	@Pure
	public abstract Object getDefaultValue();

	/**
	 * Replies if this attribute type is
	 * a base type, ie. a number, a boolean
	 * or a string.
	 *
	 * @return <code>true</code> if this type is a base type,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public abstract boolean isBaseType();

	/**
	 * Replies if this attribute type is
	 * a number type.
	 * A number type is always a base type.
	 *
	 * @return <code>true</code> if this type is a number type,
	 *     otherwise <code>false</code>
	 * @since 4.0
	 */
	@Pure
	public abstract boolean isNumberType();

	/**
	 * Replies if a null value is allowed for this attribute type.
	 *
	 * @return <code>true</code> if this type allows <code>null</code> value,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public abstract boolean isNullAllowed();

	/** Replies if a value of the given attribute type may
	 * be cast to a value of this attribute type.
	 *
	 * <p>Caution: even if isAssignableFrom is replying <code>true</code>,
	 * the {@link AttributeValue#cast(AttributeType)} and
	 * {@link AttributeValue#castAndSet(AttributeType, Object)} may fail
	 * if the target type does not support a specifical value of the
	 * source type. The isAssignableFrom function replies <code>true</code>
	 * if a least one value of the source type is assignable to a value
	 * of the target type.
	 *
	 * @param type the type.
	 * @return <code>true</code> if a value of the given
	 *     {@code type} may be cast to a value of <code>this</code>;
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 */
	@Pure
	public abstract boolean isAssignableFrom(AttributeType type);

	/** Cast the specified value to corresponds to the
	 * default storage standard for attributes.
	 *
	 * @param obj is the object to cast
	 * @return the casted value
	 * @throws ClassCastException if is impossible to cast.
	 * @throws NullPointerException if null value is not allowed.
	 */
	@Pure
	public abstract Object cast(Object obj);

}

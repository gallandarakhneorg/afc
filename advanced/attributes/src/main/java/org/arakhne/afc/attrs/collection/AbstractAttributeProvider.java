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
package org.arakhne.afc.attrs.collection;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.NullAttribute;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This class implements an abstract attribute provider.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractAttributeProvider implements AttributeProvider, Iterable<Attribute> {
		
	private static final long serialVersionUID = 1219373996718945571L;

	/** Ensure that the <code>null</code> value for <var>rawAttributeValue</var>
	 * is catched and replaced by a dedicated representant object.
	 * This function permits to keep the type of a value even if it is <code>null</code>.
	 * 
	 * @param rawAttributeValue is the value to protect.
	 * @param type is the type of the attribute to preserve over time.
	 * @return the value, or the representant of the java <code>null</code> value.
	 * @see #unprotectNull(Object)
	 */
	protected static Object protectNull(Object rawAttributeValue, AttributeType type) {
		if (rawAttributeValue==null) {
			if (type.isNullAllowed())
				return new NullAttribute(type);
			throw new NullPointerException();
		}
		return rawAttributeValue;
	}

	/** Ensure that the <code>null</code> value for <var>rawAttributeValue</var>
	 * is catched and the dedicated representant object for <code>null</code>
	 * if replace by the real <code>null</code> java value.
	 * 
	 * @param rawAttributeValue is the value to protect.
	 * @return the value.
	 * @see #protectNull(Object, AttributeType)
	 */
	protected static Object unprotectNull(Object rawAttributeValue) {
		if (rawAttributeValue instanceof NullAttribute)
			return null;
		return rawAttributeValue;
	}

	@Override
	public final Map<String, Object> toMap() {
		Map<String,Object> map = new TreeMap<>();
		toMap(map);
		return map;
	}

	/** Replies an iterator on the attributes.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Iterator<Attribute> iterator() {
		return new AttributeIterator(this);
	}
	
	/** Make a deep copy of this object and replies the copy.
	 * 
	 * @return the deep copy.
	 */
	@Override
	public AttributeProvider clone() {
		try {
			return (AttributeProvider)super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public Iterable<Attribute> attributes() {
		return this;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean getAttributeAsBool(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getBoolean();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getAttributeAsInt(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return (int)value.getInteger();
	}

	/** {@inheritDoc}
	 */
	@Override
	public long getAttributeAsLong(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getInteger();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getAttributeAsFloat(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return (float)value.getReal();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getAttributeAsDouble(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getReal();
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getAttributeAsString(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getString();
	}

	/** {@inheritDoc}
	 */
	@Override
	public UUID getAttributeAsUUID(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getUUID();
	}

	/** {@inheritDoc}
	 */
	@Override
	public URL getAttributeAsURL(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getURL();
	}

	/** {@inheritDoc}
	 */
	@Override
	public URI getAttributeAsURI(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getURI();
	}

	/** {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public Image getAttributeAsImage(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getImage();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Date getAttributeAsDate(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getDate();
	}

	/** {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public Color getAttributeAsColor(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getColor();
	}

	/** {@inheritDoc}
	 */
	@Override
	public InetAddress getAttributeAsInetAddress(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getInetAddress();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Enum<?> getAttributeAsEnumeration(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getEnumeration();
	}

	/** {@inheritDoc}
	 */
	@Override
	public <T extends Enum<T>> T getAttributeAsEnumeration(String name, Class<T> type) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getEnumeration(type);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<?> getAttributeAsJavaClass(String name) throws AttributeException {
		AttributeValue value = getAttribute(name);
		if (value==null) throw new NoAttributeFoundException(name);
		return value.getJavaClass();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean getAttribute(String name, boolean defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getBoolean();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getAttribute(String name, int defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return (int)value.getInteger();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public long getAttribute(String name, long defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getInteger();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getAttribute(String name, float defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return (float)value.getReal();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getAttribute(String name, double defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getReal();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getAttribute(String name, String defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getString();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public UUID getAttribute(String name, UUID defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getUUID();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public URL getAttribute(String name, URL defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getURL();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public URI getAttribute(String name, URI defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getURI();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public Image getAttribute(String name, Image defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getImage();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Date getAttribute(String name, Date defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getDate();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public Color getAttribute(String name, Color defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getColor();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public InetAddress getAttribute(String name, InetAddress defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getInetAddress();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public InetAddress getAttribute(String name, InetSocketAddress defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getInetAddress();
			}
			catch(AttributeException exception) {
				//
			}
		}
		return defaultValue==null ? null : defaultValue.getAddress();
	}

	/** {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T getAttribute(String name, T defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return (T)value.getEnumeration();
			}
			catch(AttributeException exception) {
				//
			}
			catch(ClassCastException exception) {
				//
			}
		}
		return defaultValue;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<?> getAttribute(String name, Class<?> defaultValue) {
		AttributeValue value = getAttribute(name);
		if (value!=null) {
			try {
				return value.getJavaClass();
			}
			catch(AttributeException exception) {
				//
			}
			catch(ClassCastException exception) {
				//
			}
		}
		return defaultValue;
	}

}

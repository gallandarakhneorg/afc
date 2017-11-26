/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.attrs.collection;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import com.google.common.collect.Iterables;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.NullAttribute;
import org.arakhne.afc.vmutil.json.JsonBuffer;

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

	/** Ensure that the <code>null</code> value for {@code rawAttributeValue}
	 * is catched and replaced by a dedicated representant object.
	 * This function permits to keep the type of a value even if it is <code>null</code>.
	 *
	 * @param rawAttributeValue is the value to protect.
	 * @param type is the type of the attribute to preserve over time.
	 * @return the value, or the representant of the java <code>null</code> value.
	 * @see #unprotectNull(Object)
	 */
	protected static Object protectNull(Object rawAttributeValue, AttributeType type) {
		if (rawAttributeValue == null) {
			if (type.isNullAllowed()) {
				return new NullAttribute(type);
			}
			throw new NullPointerException();
		}
		return rawAttributeValue;
	}

	/** Ensure that the <code>null</code> value for {@code rawAttributeValue}
	 * is catched and the dedicated representant object for <code>null</code>
	 * if replace by the real <code>null</code> java value.
	 *
	 * @param rawAttributeValue is the value to protect.
	 * @return the value.
	 * @see #protectNull(Object, AttributeType)
	 */
	protected static Object unprotectNull(Object rawAttributeValue) {
		if (rawAttributeValue instanceof NullAttribute) {
			return null;
		}
		return rawAttributeValue;
	}

	/** Replies an iterator on the attributes.
	 *
	 * @return {@inheritDoc}
	 */
	@Pure
	@Override
	public Iterator<Attribute> iterator() {
		return new AttributeIterator(this);
	}

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Pure
	@Override
	public AttributeProvider clone() {
		try {
			return (AttributeProvider) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Pure
	@Override
	public Iterable<Attribute> attributes() {
		return this;
	}

	@Pure
	@Override
	public boolean getAttributeAsBool(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getBoolean();
	}

	@Pure
	@Override
	public int getAttributeAsInt(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return (int) value.getInteger();
	}

	@Pure
	@Override
	public long getAttributeAsLong(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getInteger();
	}

	@Pure
	@Override
	public float getAttributeAsFloat(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return (float) value.getReal();
	}

	@Pure
	@Override
	public double getAttributeAsDouble(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getReal();
	}

	@Pure
	@Override
	public String getAttributeAsString(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getString();
	}

	@Pure
	@Override
	public UUID getAttributeAsUUID(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getUUID();
	}

	@Pure
	@Override
	public URL getAttributeAsURL(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getURL();
	}

	@Pure
	@Override
	public URI getAttributeAsURI(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getURI();
	}

	@Pure
	@Override
	public Date getAttributeAsDate(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getDate();
	}

	@Pure
	@Override
	public InetAddress getAttributeAsInetAddress(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getInetAddress();
	}

	@Pure
	@Override
	public Enum<?> getAttributeAsEnumeration(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getEnumeration();
	}

	@Pure
	@Override
	public <T extends Enum<T>> T getAttributeAsEnumeration(String name, Class<T> type) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getEnumeration(type);
	}

	@Pure
	@Override
	public Class<?> getAttributeAsJavaClass(String name) throws AttributeException {
		final AttributeValue value = getAttribute(name);
		if (value == null) {
			throw new NoAttributeFoundException(name);
		}
		return value.getJavaClass();
	}

	@Pure
	@Override
	public boolean getAttribute(String name, boolean defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getBoolean();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public int getAttribute(String name, int defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return (int) value.getInteger();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public long getAttribute(String name, long defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getInteger();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public float getAttribute(String name, float defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return (float) value.getReal();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public double getAttribute(String name, double defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getReal();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public String getAttribute(String name, String defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getString();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public UUID getAttribute(String name, UUID defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getUUID();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public URL getAttribute(String name, URL defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getURL();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public URI getAttribute(String name, URI defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getURI();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public Date getAttribute(String name, Date defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getDate();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public InetAddress getAttribute(String name, InetAddress defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getInetAddress();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public InetAddress getAttribute(String name, InetSocketAddress defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getInetAddress();
			} catch (AttributeException exception) {
				//
			}
		}
		return defaultValue == null ? null : defaultValue.getAddress();
	}

	@Pure
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T getAttribute(String name, T defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return (T) value.getEnumeration();
			} catch (AttributeException | ClassCastException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Pure
	@Override
	public Class<?> getAttribute(String name, Class<?> defaultValue) {
		final AttributeValue value = getAttribute(name);
		if (value != null) {
			try {
				return value.getJavaClass();
			} catch (AttributeException | ClassCastException exception) {
				//
			}
		}
		return defaultValue;
	}

	@Override
	public final String toString() {
		final JsonBuffer buffer = new JsonBuffer();
		toJson(buffer);
		return buffer.toString();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("attributes", Iterables.filter(attributes(), attr -> { //$NON-NLS-1$
			return attr.isAssigned();
		}));
	}

}

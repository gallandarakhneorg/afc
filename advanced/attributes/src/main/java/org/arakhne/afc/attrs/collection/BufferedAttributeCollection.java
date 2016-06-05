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

package org.arakhne.afc.attrs.collection;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.references.SoftValueTreeMap;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This class implements an abstract attribute provider that use
 * a memory cache.
 *
 * <p>XXX: Make this provider to save asynchronously on the remote storage area.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class BufferedAttributeCollection extends AbstractAttributeCollection {

	private static final long serialVersionUID = 1865614675044905721L;

	private transient Map<String, AttributeValue> cache = new SoftValueTreeMap<>();

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Pure
	@Override
	public BufferedAttributeCollection clone() {
		final BufferedAttributeCollection clone = (BufferedAttributeCollection) super.clone();
		this.cache = new SoftValueTreeMap<>();
		return clone;
	}

	/** Load a value from the data source.
	 *
	 * @param name is the name of the attribute to load
	 * @return the value of the attribute.
	 * @throws AttributeException on error or when the attribute does not exist
	 */
	protected abstract AttributeValue loadValue(String name) throws AttributeException;

	@Pure
	@Override
	public abstract Collection<String> getAllAttributeNames();

	/** Save a value into the data source.
	 *
	 * @param name is the name of the attribute to save
	 * @param value is the value of the attribute.
	 * @throws AttributeException on error.
	 */
	protected abstract void saveValue(String name, AttributeValue value) throws AttributeException;

	/** Remove a value from the data source.
	 *
	 * @param name is the name of the attribute to remove
	 * @return the removed value
	 * @throws AttributeException on error
	 */
	protected abstract AttributeValue removeValue(String name) throws AttributeException;

	/** Remove all the values from the data source.
	 *
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 * @throws AttributeException on error
	 */
	protected abstract boolean removeAllValues() throws AttributeException;

	/** Replies the value associated to the specified name.
	 * @throws AttributeException on error.
	 */
	private AttributeValue extractValueFor(String name) throws AttributeException {
		AttributeValue value = null;
		if (this.cache.containsKey(name)) {
			value = this.cache.get(name);
		} else {
			value = loadValue(name);
			this.cache.put(name, value);
		}
		return value;
	}

	@Pure
	@Override
	public boolean hasAttribute(String name) {
		return getAllAttributeNames().contains(name);
	}

	@Pure
	@Override
	public Collection<Attribute> getAllAttributes() {
		final ArrayList<Attribute> list = new ArrayList<>(getAttributeCount());
		for (final String name : getAllAttributeNames()) {
			if (name != null) {
				try {
					final  Attribute newAttr = new AttributeImpl(name, extractValueFor(name));
					list.add(newAttr);
				} catch (AttributeException exception) {
					//
				}
			}
		}
		return list;
	}

	@Pure
	@Override
	public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
		final Map<AttributeType, Collection<Attribute>> map = new TreeMap<>();
		for (final String name : getAllAttributeNames()) {
			if (name != null) {
				try {
					final Attribute newAttr = new AttributeImpl(name, extractValueFor(name));
					Collection<Attribute> list = map.get(newAttr.getType());
					if (list == null) {
						list = new ArrayList<>();
						map.put(newAttr.getType(), list);
					}
					list.add(newAttr);
				} catch (AttributeException exception) {
					//
				}
			}
		}
		return map;
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name) {
		try {
			return new AttributeValueImpl(extractValueFor(name));
		} catch (AttributeException exception) {
			//
		}
		return null;
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		AttributeValue value;
		try {
			value = new AttributeValueImpl(extractValueFor(name));
		} catch (AttributeException exception) {
			value = default_value;
		}
		return value;
	}

	@Pure
	@Override
	public Attribute getAttributeObject(String name) {
		try {
			return new AttributeImpl(name, extractValueFor(name));
		} catch (AttributeException exception) {
			//
		}
		return null;
	}

	@Override
	public void freeMemory() {
		this.cache.clear();
	}

	/** Set the attribute value.
	 *
	 * @param name is the name of the attribute
	 * @param value is the raw value to store.
	 * @return the new created attribute
	 * @throws AttributeException on error.
	 */
	protected Attribute setAttributeFromRawValue(String name, AttributeValue value) throws AttributeException {
		return setAttributeFromRawValue(name, value.getType(), value.getValue());
	}

	/** Set the attribute value.
	 *
	 * @param name is the name of the attribute
	 * @param type is the type of the attribute
	 * @param value is the raw value to store.
	 * @return the new created attribute
	 * @throws AttributeException on error.
	 */
	protected Attribute setAttributeFromRawValue(String name, AttributeType type, Object value) throws AttributeException {
		AttributeValue oldValue;
		try {
			oldValue = new AttributeValueImpl(extractValueFor(name));
		} catch (AttributeException exception) {
			oldValue = null;
		}

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		final Attribute attr = new AttributeImpl(name, type);
		attr.setValue(type.cast(value));

		saveValue(name, attr);

		this.cache.put(name, attr);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		AttributeValue oldValue;
		try {
			oldValue = new AttributeValueImpl(extractValueFor(name));
		} catch (AttributeException exception) {
			oldValue = null;
		}
		final AttributeType oldType = (oldValue == null) ? null : oldValue.getType();

		if (oldValue == null || oldType == null || type == null || type == oldType) {
			return null;
		}

		final Attribute attr = new AttributeImpl(name, oldValue.getValue());
		attr.cast(type);

		this.cache.put(name, attr);

		fireAttributeChangedEvent(name, oldValue, attr);

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		return setAttributeFromRawValue(name, value);
	}

	@Override
	public Attribute setAttribute(String name, boolean value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.BOOLEAN, Boolean.valueOf(value));
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, int value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INTEGER, Long.valueOf(value));
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, long value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INTEGER, Long.valueOf(value));
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, float value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.REAL, Double.valueOf(value));
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, double value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.REAL, Double.valueOf(value));
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, String value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.STRING, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, UUID value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.UUID, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, URL value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.URL, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, URI value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.URI, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @deprecated since 13.0
	 */
	@Override
	@Deprecated
	public Attribute setAttribute(String name, Image value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.IMAGE, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, Date value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.DATE, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @deprecated since 13.0
	 */
	@Override
	@Deprecated
	public Attribute setAttribute(String name, Color value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.COLOR, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INET_ADDRESS, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INET_ADDRESS, value == null ? null : value.getAddress());
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.ENUMERATION, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.TYPE, value);
		} catch (AttributeException exception) {
			return null;
		}
	}

	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		return setAttributeFromRawValue(value.getName(), value);
	}

	@Override
	public boolean removeAttribute(String name) {
		try {
			if (hasAttribute(name)) {
				final AttributeValue currentValue = extractValueFor(name);
				this.cache.remove(name);
				removeValue(name);
				fireAttributeRemovedEvent(name, currentValue);
				return true;
			}
		} catch (AttributeException exception) {
			//
		}
		return false;
	}

	@Override
	public boolean removeAllAttributes() {
		try {
			if (getAttributeCount() > 0) {
				this.cache.clear();
				if (removeAllValues()) {
					fireAttributeClearedEvent();
					return true;
				}
			}
		} catch (AttributeException exception) {
			//
		}
		return false;
	}

    private   AttributeValue getValueForName(String name) {
		final AttributeValue valueForName = null;
		try {
			return  extractValueFor(name);
		} catch (AttributeException exception) {
			//
		}
		return valueForName;
	}

	@Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		try {
			final AttributeValue valueForOldName = getValueForName(oldname);
			// The source attribute does not exist.
			if (valueForOldName == null) {
				return false;
			}
			final AttributeValue oldValueForNewName = getValueForName(newname);
			// Target attribute is existing and overwrite was disabled.
			if ((!overwrite) && (oldValueForNewName != null)) {
				return false;
			}
			final AttributeValue oldValueCopyForNewName = new AttributeValueImpl(oldValueForNewName);

			removeValue(oldname);
			this.cache.remove(oldname);
			if (valueForOldName instanceof Attribute) {
				((Attribute) valueForOldName).setName(newname);
			}
			saveValue(newname, valueForOldName);
			this.cache.put(newname, valueForOldName);

			if (oldValueForNewName != null) {
				fireAttributeRemovedEvent(newname, oldValueCopyForNewName);
			}

			fireAttributeRenamedEvent(oldname, newname, valueForOldName);

			return true;
		} catch (AttributeException exception) {
			//
		}
		return false;
	}

	@Override
	public void flush() {
		//
	}

}


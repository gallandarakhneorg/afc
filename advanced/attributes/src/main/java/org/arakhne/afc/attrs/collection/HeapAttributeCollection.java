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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeNotInitializedException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.attr.InvalidAttributeTypeException;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This class implements an attribute provider which
 * only works with the Heap space.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("deprecation")
public class HeapAttributeCollection extends AbstractAttributeCollection {

	private static final long serialVersionUID = 4362736589775617590L;

	private Map<String, Object> heap = new TreeMap<>(new AttributeNameStringComparator());

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Pure
	@Override
	public HeapAttributeCollection clone() {
		final HeapAttributeCollection clone = (HeapAttributeCollection) super.clone();
		clone.heap = new TreeMap<>(new AttributeNameStringComparator());
		clone.heap.putAll(this.heap);
		return clone;
	}

	@Override
	public void addAttributes(Map<String, Object> content) {
		for (final Entry<String, Object> pair : content.entrySet()) {
			Object value = pair.getValue();
			final AttributeType type = AttributeType.fromValue(value);
			value = type.cast(value);
			final Object oldValue = this.heap.put(pair.getKey(), value);
			if (oldValue == null) {
				fireAttributeAddedEvent(pair.getKey(), new AttributeValueImpl(type, value));
			} else {
				fireAttributeChangedEvent(pair.getKey(),
						new AttributeValueImpl(type, oldValue),
						new AttributeValueImpl(type, value));
			}
		}
	}

	@Override
	public void addAttributes(AttributeProvider content) throws AttributeException {
		for (final Attribute attr : content.attributes()) {
			final Object value = attr.getValue();
			final Object oldValue = this.heap.put(attr.getName(), value);
			if (oldValue == null) {
				fireAttributeAddedEvent(attr.getName(),
						new AttributeValueImpl(attr.getType(), value));
			} else {
				fireAttributeChangedEvent(attr.getName(),
						new AttributeValueImpl(AttributeType.fromValue(oldValue), oldValue),
						new AttributeValueImpl(attr.getType(), value));
			}
		}
	}

	@Override
	public void setAttributes(Map<String, Object> content) {
		setAttributesInternal(new TreeMap<>(content));
	}

	@Override
	public void setAttributes(AttributeProvider content) throws AttributeException {
		final Map<String, Object> newAttributes = new TreeMap<>();
		content.toMap(newAttributes);
		setAttributesInternal(newAttributes);
	}

	private void setAttributesInternal(Map<String, Object> newAttributes) {
		final Iterator<Entry<String, Object>> iterator = this.heap.entrySet().iterator();
		while (iterator.hasNext()) {
			final Entry<String, Object> entry = iterator.next();
			Object newValue = newAttributes.remove(entry.getKey());
			if (newValue == null) {
				iterator.remove();
				fireAttributeRemovedEvent(entry.getKey(),
						new AttributeValueImpl(AttributeType.fromValue(entry.getValue()), entry.getValue()));
			} else {
				final AttributeType type = AttributeType.fromValue(newValue);
				newValue = type.cast(newValue);
				entry.setValue(newValue);
				fireAttributeChangedEvent(entry.getKey(),
						new AttributeValueImpl(AttributeType.fromValue(entry.getValue()), entry.getValue()),
						new AttributeValueImpl(type, newValue));
			}
		}

		for (final Entry<String, Object> e : newAttributes.entrySet()) {
			Object newValue = e.getValue();
			final AttributeType type = AttributeType.fromValue(newValue);
			newValue = type.cast(newValue);
			if (newValue != null) {
				this.heap.put(e.getKey(), newValue);
				fireAttributeAddedEvent(e.getKey(),
						new AttributeValueImpl(type, newValue));
			}
		}
	}

	@Override
	public void toMap(Map<String, Object> mapToFill) {
		mapToFill.putAll(this.heap);
	}

	@Pure
	@Override
	public int getAttributeCount() {
		return this.heap.size();
	}

	@Pure
	@Override
	public boolean hasAttribute(String name) {
		return this.heap.containsKey(name);
	}

	@Pure
	@Override
	public Collection<Attribute> getAllAttributes() {
		final List<Attribute> list = new ArrayList<>(getAttributeCount());
		for (final Entry<String, Object> entry : this.heap.entrySet()) {
			final String name = entry.getKey();
			if (name != null) {
				final Object rawValue = entry.getValue();
				final Attribute newAttr = new AttributeImpl(name);
				newAttr.castAndSet(
						AttributeType.fromValue(rawValue),
						unprotectNull(rawValue));
				list.add(newAttr);
			}
		}
		return list;
	}

	@Pure
	@Override
	public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
		final Map<AttributeType, Collection<Attribute>> map = new TreeMap<>();
		for (final Entry<String, Object> entry : this.heap.entrySet()) {
			Object value = entry.getValue();
			if (value != null) {
				final AttributeType type = AttributeType.fromValue(value);
				value = unprotectNull(value);
				Collection<Attribute> list = map.get(type);
				if (list == null) {
					list = new ArrayList<>();
					map.put(type, list);
				}
				final Attribute attr = new AttributeImpl(entry.getKey());
				attr.castAndSet(type, value);
				list.add(attr);
			}
		}
		return map;
	}

	@Pure
	@Override
	public Collection<String> getAllAttributeNames() {
		return Collections.unmodifiableCollection(this.heap.keySet());
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name) {
		return getStoredAttributeValue(name, null);
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		final AttributeValue value = getStoredAttributeValue(name,
				default_value == null ? null : default_value.getType());
		if (value == null) {
			return default_value;
		}
		return value;
	}

	@Pure
	@Override
	public Attribute getAttributeObject(String name) {
		return getStoredAttribute(name, null);
	}

	/** Replies the attribute with the given name.
	 *
	 * @param name is the name of the attribute to retreive
	 * @param expectedType is the expected type for the attribute.
	 * @return the value or <code>null</code>
	 */
	@Pure
	protected Attribute getStoredAttribute(String name, AttributeType expectedType) {
		Object val = this.heap.get(name);
		if (val != null) {
			final AttributeType currentType = AttributeType.fromValue(val);
			val = unprotectNull(val);
			final Attribute attr = new AttributeImpl(name);
			if (expectedType == null) {
				attr.castAndSet(currentType, val);
			} else {
				attr.castAndSet(expectedType, val);
			}
			return attr;
		}
		return null;
	}

	/** Replies the attribute with the given name.
	 *
	 * @param name is the name of the attribute to retreive
	 * @param expectedType is the expected type for the attribute.
	 * @return the value or <code>null</code>
	 */
	@Pure
	protected AttributeValue getStoredAttributeValue(String name, AttributeType expectedType) {
		Object val = this.heap.get(name);
		if (val != null) {
			final AttributeType currentType = AttributeType.fromValue(val);
			val = unprotectNull(val);
			final AttributeValue attr = new AttributeValueImpl(name);
			if (expectedType == null) {
				attr.castAndSet(currentType, val);
			} else {
				attr.castAndSet(expectedType, val);
			}
			return attr;
		}
		return null;
	}

	private AttributeValue copyValue(String name) {
		final Object currentValue = this.heap.get(name);
		if (currentValue != null) {
			final AttributeType oldType = AttributeType.fromValue(currentValue);
			final AttributeValue oldValue = new AttributeValueImpl();
			oldValue.castAndSet(oldType, currentValue);
			return oldValue;
		}
		return null;
	}

	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);
		final AttributeType oldType = (oldValue == null) ? null : oldValue.getType();

		if (oldType == null || type == null || oldType == type) {
			return null;
		}

		final Attribute attr = new AttributeImpl(name, (oldValue == null) ? null : oldValue.getValue());
		attr.cast(type);

		this.heap.put(name, protectNull(attr.getValue(), type));

		fireAttributeChangedEvent(name, oldValue, attr);

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		assert name != null && value != null;
		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, protectNull(value.getValue(), value.getType()));

		final Attribute attr = new AttributeImpl(name, value.getValue());

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, value);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, boolean value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, Boolean.valueOf(value));

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, int value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, Long.valueOf(value));

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, long value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, Long.valueOf(value));

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, float value) {
		assert name != null;

		final AttributeValue oldValue;
		final Object currentValue = this.heap.get(name);
		if (currentValue != null) {
			oldValue = new AttributeValueImpl();
			oldValue.castAndSet(AttributeType.fromValue(currentValue), currentValue);
		} else {
			oldValue = null;
		}

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, Double.valueOf(value));

		final Attribute attr = new AttributeImpl(name, value);

		if (currentValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, double value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, Double.valueOf(value));

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, String value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		final Object rv = (value == null) ? AttributeType.STRING.getDefaultValue() : value;
		this.heap.put(name, rv);

		final Attribute attr = new AttributeImpl(name, rv);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, UUID value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		final Object rv = (value == null) ? AttributeType.UUID.getDefaultValue() : value;
		this.heap.put(name, rv);

		final Attribute attr = new AttributeImpl(name, rv);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, URL value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, protectNull(value, AttributeType.URL));

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, URI value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, protectNull(value, AttributeType.URI));

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	/**
	 * {@inheritDoc}
	 * @deprecated since 13.0
	 */
	@Override
	@Deprecated
	public Attribute setAttribute(String name, Image value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, protectNull(value, AttributeType.IMAGE));

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, Date value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		final Object rv = (value == null) ? AttributeType.DATE.getDefaultValue() : value;
		this.heap.put(name, rv);

		final Attribute attr = new AttributeImpl(name, rv);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	/**
	 * {@inheritDoc}
	 * @deprecated since 13.0
	 */
	@Override
	@Deprecated
	public Attribute setAttribute(String name, Color value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		final Object rv = (value == null) ? AttributeType.COLOR.getDefaultValue() : value;
		this.heap.put(name, rv);

		final Attribute attr = new AttributeImpl(name, rv);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		assert value != null;
		final String name = value.getName();
		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, protectNull(value.getValue(), value.getType()));

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, value);
		} else {
			fireAttributeAddedEvent(name, value);
		}

		return value;
	}

	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, value);

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		return setAttribute(name, (value == null) ? null : value.getAddress());
	}

	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, value);

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		assert name != null;

		final AttributeValue oldValue = copyValue(name);

		if (oldValue != null && oldValue.equals(value)) {
			return null;
		}

		this.heap.put(name, value);

		final Attribute attr = new AttributeImpl(name, value);

		if (oldValue != null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		} else {
			fireAttributeAddedEvent(name, attr);
		}

		return attr;
	}

	@Override
	public boolean removeAttribute(String name) {
		assert name != null;

		final AttributeValue oldValue;
		final Object currentValue = this.heap.remove(name);
		if (currentValue != null) {
			oldValue = new AttributeValueImpl();
			oldValue.castAndSet(AttributeType.fromValue(currentValue), currentValue);
			fireAttributeRemovedEvent(name, oldValue);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAllAttributes() {
		if (!this.heap.isEmpty()) {
			this.heap.clear();
			fireAttributeClearedEvent();
			return true;
		}
		return false;
	}

	@Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		if (oldname == null || newname == null || oldname.equals(newname)) {
			return false;
		}

		final AttributeValue valueOfOldName = copyValue(oldname);

		// The attribute does not exist.
		if (valueOfOldName == null) {
			return false;
		}

		final AttributeValue oldValueOfNewName = copyValue(newname);

		// The target attribute is existing and overwrite was disabled
		if ((!overwrite) && (oldValueOfNewName != null)) {
			return false;
		}

		Object rawValue;
		try {
			rawValue = valueOfOldName.getValue();
		} catch (InvalidAttributeTypeException | AttributeNotInitializedException e) {
			rawValue = null;
		}

		this.heap.remove(oldname);
		this.heap.put(newname, protectNull(rawValue, valueOfOldName.getType()));

		if (oldValueOfNewName != null) {
			fireAttributeRemovedEvent(newname, oldValueOfNewName);
		}

		fireAttributeRenamedEvent(oldname, newname, valueOfOldName);

		return true;
	}

	@Override
	public void freeMemory() {
		// Do nothing
	}

	@Override
	public void flush() {
		// Do nothing
	}

	@Pure
	@Override
	public String toString() {
		return this.heap.toString();
	}

}


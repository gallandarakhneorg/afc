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

package org.arakhne.afc.io.dbase.attr;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeComparator;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent.Type;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.attrs.collection.ROMBasedAttributeCollection;


/**
 * This class permits to access to the attributes
 * stored inside a dBase file.
 *
 * <p>This class uses a syndicate to access to a single dBase file,
 * assuming that an attribute provider
 * accesses only one record of the dBase file.
 *
 * <p>This class is a container of attributes that access to
 * one record of a dBase file.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see DBaseFileAttributeProvider
 */
@SuppressWarnings({"checkstyle:methodcount"})
public class DBaseFileAttributeCollection extends DBaseFileAttributeProvider implements ROMBasedAttributeCollection {

	private static final long serialVersionUID = -8104118627013838862L;

	private Collection<AttributeChangeListener> listeners;

	private Map<String, Attribute> buffer;

	private int addedElementCount;

	private boolean isEventFirable = true;

	/** Constructor.
	 * @param accessor is the object that permits to access to the dBase resource.
	 */
	DBaseFileAttributeCollection(DBaseFileAttributeAccessor accessor) {
		super(accessor);
	}

	@Override
	@Pure
	public boolean isEventFirable() {
		return this.isEventFirable;
	}

	@Override
	public void setEventFirable(boolean isFirable) {
		this.isEventFirable = isFirable;
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	public void finalize() throws Throwable {
		this.listeners = null;
		this.buffer = null;
		super.finalize();
	}

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Pure
	@Override
	public DBaseFileAttributeCollection clone() {
		final DBaseFileAttributeCollection clone = (DBaseFileAttributeCollection) super.clone();
		this.listeners = null;
		this.buffer = null;
		return clone;
	}

	@Pure
	@Override
	public Collection<String> getAllBufferedAttributeNames() {
		if (this.buffer == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableSet(this.buffer.keySet());
	}

	@Pure
	@Override
	public int getBufferedAttributeCount() {
		if (this.buffer == null) {
			return 0;
		}
		return this.buffer.size();
	}

	@Pure
	@Override
	public Collection<Attribute> getAllBufferedAttributes() {
		if (this.buffer == null) {
			return Collections.emptyList();
		}
		final Collection<Attribute> list = this.buffer.values();
		final ArrayList<Attribute> tab = new ArrayList<>(list.size());
		for (final Attribute attr : list) {
			tab.add(new AttributeImpl(attr));
		}
		return tab;
	}

	@Pure
	@Override
	public boolean isBufferedAttribute(String attributeName) {
		if (this.buffer == null) {
			return false;
		}
		return this.buffer.containsKey(attributeName);
	}

	private Attribute setBuffer(String name, Attribute attr) {
		if (this.buffer == null) {
			this.buffer = new TreeMap<>((key1, key2) -> AttributeImpl.compareAttrNames(key1, key2));
		}
		final Attribute oldValue = this.buffer.put(name, attr);
		if (oldValue == null) {
			// First add inside the buffer. Added elements?
			final boolean alreadyInside = hasAttributeInDBase(name);
			if (alreadyInside) {
				fireAttributeChangedEvent(name, getAttributeObjectFromDBase(name), attr);
			} else {
				fireAttributeAddedEvent(name, attr);
				++this.addedElementCount;
			}
		} else {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		return attr;
	}

	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		final AttributeValue oldValue = getAttribute(name);
		final AttributeType oldType = oldValue == null ? null : oldValue.getType();

		if (oldValue == null || oldType == null || type == null || type == oldType) {
			return null;
		}

		final Attribute attr = new AttributeImpl(name, oldValue.getValue());
		attr.cast(type);

		setBuffer(name, attr);

		fireAttributeChangedEvent(name, oldValue, attr);

		return attr;
	}

	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, boolean value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, int value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, long value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, float value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, double value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, String value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, UUID value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, URL value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, URI value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, Date value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		return setBuffer(name, new AttributeImpl(name, value));
	}

	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		return setBuffer(value.getName(), new AttributeImpl(value));
	}

	@Override
	public boolean removeAttribute(String name) {
		if (this.buffer == null) {
			return false;
		}
		final Attribute oldValue = this.buffer.remove(name);
		if (this.buffer.isEmpty()) {
			this.buffer = null;
		}
		final Attribute fromDBase = getAttributeObjectFromDBase(name);
		if (oldValue != null) {
			if (fromDBase != null) {
				fireAttributeChangedEvent(name, oldValue, fromDBase);
			} else {
				fireAttributeRemovedEvent(name, oldValue);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAllAttributes() {
		if (this.buffer != null) {
			this.buffer.clear();
			this.buffer = null;
			fireAttributeClearedEvent();
			return true;
		}
		return false;
	}

	@Override
	public final boolean renameAttribute(String oldname, String newname) {
		return renameAttribute(oldname, newname, false);
	}

	@Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		if (this.buffer == null) {
			return false;
		}
		if (!overwrite && this.buffer.containsKey(newname)) {
			return false;
		}
		final Attribute value = this.buffer.remove(oldname);
		if (value != null) {
			this.buffer.put(newname, value);
			fireAttributeRenamedEvent(oldname, newname, value);
			return true;
		}
		return false;
	}

	@Override
	public void setAttributes(AttributeProvider otherContainer) throws AttributeException {
		removeAllAttributes();
		addAttributes(otherContainer);
	}

	@Override
	public void setAttributes(Map<String, Object> otherContainer) {
		removeAllAttributes();
		addAttributes(otherContainer);
	}

	@Override
	public void addAttributes(Map<String, Object> otherContainer) {
		if (otherContainer.size() > 0) {
			for (final Entry<String, Object> entry : otherContainer.entrySet()) {
				final Attribute attr = new AttributeImpl(entry.getKey(), entry.getValue());
				setBuffer(entry.getKey(), attr);
			}
		}
	}

	@Override
	public void addAttributes(AttributeProvider otherContainer) throws AttributeException {
		if (otherContainer.getAttributeCount() > 0) {
			for (final Attribute attr : otherContainer.getAllAttributes()) {
				setAttribute(attr);
			}
		}
	}

	@Override
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<>();
		}
		this.listeners.add(listener);
	}

	@Override
	public void removeAttributeChangeListener(AttributeChangeListener listener) {
		if (this.listeners == null) {
			return;
		}
		this.listeners.remove(listener);
		if (this.listeners.isEmpty()) {
			this.listeners = null;
		}
	}

	@Pure
	@Override
	public int getAttributeCount() {
		return super.getAttributeCount() + this.addedElementCount;
	}

	@Pure
	@Override
	public boolean hasAttribute(String name) {
		if (this.buffer != null && this.buffer.containsKey(name)) {
			return true;
		}
		return super.hasAttribute(name);
	}

	@Pure
	@Override
	public Collection<Attribute> getAllAttributes() {
		final Set<Attribute> attrs = new TreeSet<>(new AttributeComparator());
		attrs.addAll(super.getAllAttributes());
		if (this.buffer != null) {
			attrs.addAll(this.buffer.values());
		}
		return attrs;
	}

	@Pure
	@Override
	public Collection<String> getAllAttributeNames() {
		final Set<String> names = new TreeSet<>((key1, key2) -> AttributeImpl.compareAttrNames(key1, key2));
		names.addAll(super.getAllAttributeNames());
		if (this.buffer != null) {
			names.addAll(this.buffer.keySet());
		}
		return names;
	}

	@Pure
	@Override
	public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
		final Map<AttributeType, Collection<Attribute>> map = super.getAllAttributesByType();
		if (this.buffer != null) {
			Collection<Attribute> col;
			for (final Attribute attr : this.buffer.values()) {
				col = map.get(attr.getType());
				if (col == null) {
					col = new HashSet<>();
					map.put(attr.getType(), col);
				}
				if (!(col instanceof Set<?>)) {
					col.remove(attr);
				}
				col.add(attr);
			}
		}
		return map;
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return new AttributeValueImpl(value);
			}
		}
		return super.getAttribute(name);
	}

	@Pure
	@Override
	public boolean getAttribute(String name, boolean defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getBoolean();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public long getAttribute(String name, long defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getInteger();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public int getAttribute(String name, int defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return (int) value.getInteger();
				} catch (AttributeException excpetion) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public float getAttribute(String name, float defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return (float) value.getReal();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public double getAttribute(String name, double defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getReal();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public String getAttribute(String name, String defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getString();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public UUID getAttribute(String name, UUID defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getUUID();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public URL getAttribute(String name, URL defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getURL();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public URI getAttribute(String name, URI defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getURI();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public Date getAttribute(String name, Date defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getDate();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public InetAddress getAttribute(String name, InetAddress defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return value.getInetAddress();
				} catch (AttributeException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	public <ET extends Enum<ET>> ET getAttribute(String name, ET defaultValue) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				try {
					return (ET) value.getEnumeration();
				} catch (AttributeException exception) {
					return defaultValue;
				} catch (ClassCastException exception) {
					return defaultValue;
				}
			}
		}
		return super.getAttribute(name, defaultValue);
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value;
			}
		}
		return super.getAttribute(name, default_value);
	}

	@Pure
	@Override
	public Attribute getAttributeObject(String name) {
		if (this.buffer != null) {
			final Attribute value = this.buffer.get(name);
			if (value != null) {
				return new AttributeImpl(value);
			}
		}
		return super.getAttributeObject(name);
	}

	@Pure
	@Override
	public boolean getAttributeAsBool(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getBoolean();
			}
		}
		return super.getAttributeAsBool(name);
	}

	@Pure
	@Override
	public int getAttributeAsInt(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return (int) value.getInteger();
			}
		}
		return super.getAttributeAsInt(name);
	}

	@Pure
	@Override
	public long getAttributeAsLong(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getInteger();
			}
		}
		return super.getAttributeAsLong(name);
	}

	@Pure
	@Override
	public float getAttributeAsFloat(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return (float) value.getReal();
			}
		}
		return super.getAttributeAsFloat(name);
	}

	@Pure
	@Override
	public double getAttributeAsDouble(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getReal();
			}
		}
		return super.getAttributeAsDouble(name);
	}

	@Pure
	@Override
	public String getAttributeAsString(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getString();
			}
		}
		return super.getAttributeAsString(name);
	}

	@Pure
	@Override
	public UUID getAttributeAsUUID(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getUUID();
			}
		}
		return super.getAttributeAsUUID(name);
	}

	@Pure
	@Override
	public URL getAttributeAsURL(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getURL();
			}
		}
		return super.getAttributeAsURL(name);
	}

	@Pure
	@Override
	public URI getAttributeAsURI(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getURI();
			}
		}
		return super.getAttributeAsURI(name);
	}

	@Pure
	@Override
	public Date getAttributeAsDate(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getDate();
			}
		}
		return super.getAttributeAsDate(name);
	}

	@Pure
	@Override
	public InetAddress getAttributeAsInetAddress(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getInetAddress();
			}
		}
		return super.getAttributeAsInetAddress(name);
	}

	@Pure
	@Override
	public Enum<?> getAttributeAsEnumeration(String name) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getEnumeration();
			}
		}
		return super.getAttributeAsEnumeration(name);
	}

	@Pure
	@Override
	public <ET extends Enum<ET>> ET getAttributeAsEnumeration(String name, Class<ET> type) throws AttributeException {
		if (this.buffer != null) {
			final AttributeValue value = this.buffer.get(name);
			if (value != null) {
				return value.getEnumeration(type);
			}
		}
		return super.getAttributeAsEnumeration(name, type);
	}

	/** Clean the internal memory-storage structures if they exist.
	 *
	 * <p>This function permits to limit the memory usage without
	 * removing the attribute value from a hard storage area (database,
	 * files...). The attribute which are freed by this method could
	 * be reloaded in memory with a call to a getting method.
	 */
	@Override
	public void freeMemory() {
		if (this.buffer != null) {
			this.buffer.clear();
		}
		this.buffer = null;
		super.freeMemory();
	}

	@Override
	public void flush() {
		//
	}

	/** Fire the addition event.
	 *
	 * @param name is the name of the attribute for which the event occured.
	 * @param attr is the value of the attribute.
	 */
	protected void fireAttributeAddedEvent(String name, AttributeValue attr) {
		if (this.listeners != null && isEventFirable()) {
			final AttributeChangeEvent event = new AttributeChangeEvent(
					//source
					this,
					//type
					Type.ADDITION,
					//old name
					null,
					//old value
					null,
					//current name
					name,
					//current value
					attr);
			for (final AttributeChangeListener listener : this.listeners) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the attribute change event.
	 *
	 * @param name is the name of the attribute for which the event occured.
	 * @param oldValue is the previous value of the attribute
	 * @param currentValue is the current value of the attribute
	 */
	protected void fireAttributeChangedEvent(String name, AttributeValue oldValue, AttributeValue currentValue) {
		if (this.listeners != null && isEventFirable()) {
			final AttributeChangeEvent event = new AttributeChangeEvent(
					//source
					this,
					//type
					Type.VALUE_UPDATE,
					//old name
					name,
					//old value
					oldValue,
					//current name
					name,
					//current value
					currentValue);
			for (final AttributeChangeListener listener : this.listeners) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the all attribute removal event.
	 */
	protected void fireAttributeClearedEvent() {
		if (this.listeners != null && isEventFirable()) {
			final AttributeChangeEvent event = new AttributeChangeEvent(
					//source
					this,
					//type
					Type.REMOVE_ALL,
					//old name
					null,
					//old value
					null,
					//current name
					null,
					//current value
					null);
			for (final AttributeChangeListener listener : this.listeners) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the an attribute removal event.
	 *
	 * @param name is the name of the attribute for which the event occured.
	 * @param oldValue is the previous value of the attribute
	 */
	protected void fireAttributeRemovedEvent(String name, AttributeValue oldValue) {
		if (this.listeners != null && isEventFirable()) {
			final AttributeChangeEvent event = new AttributeChangeEvent(
					//source
					this,
					//type
					Type.REMOVAL,
					//old name
					name,
					//old value
					oldValue,
					//current name
					name,
					//current value
					oldValue);
			for (final AttributeChangeListener listener : this.listeners) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the renaming event.
	 *
	 * @param oldName is the previous name of the attribute (before renaming)
	 * @param newName is the new name of the attribute (after renaming)
	 * @param attr is the value of the attribute.
	 */
	protected void fireAttributeRenamedEvent(String oldName, String newName, AttributeValue attr) {
		if (this.listeners != null && isEventFirable()) {
			final AttributeChangeEvent event = new AttributeChangeEvent(
					//source
					this,
					//type
					Type.RENAME,
					//old name
					oldName,
					//old value
					attr,
					//current name
					newName,
					//current value
					attr);
			for (final AttributeChangeListener listener : this.listeners) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

}


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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

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
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HeapAttributeCollection extends AbstractAttributeCollection {
	
	private Map<String,Object> heap = new TreeMap<String,Object>(new AttributeNameStringComparator());
	
	/** Make a deep copy of this object and replies the copy.
	 * 
	 * @return the deep copy.
	 */
	@Override
	public HeapAttributeCollection clone() {
		HeapAttributeCollection clone = (HeapAttributeCollection)super.clone();
		clone.heap = new TreeMap<String,Object>(new AttributeNameStringComparator());
		clone.heap.putAll(this.heap);
		return clone;
	}

	@Override
	public void addAttributes(Map<String, Object> content) {
		Object value, oldValue;
		AttributeType type;
		for(Entry<String,Object> pair : content.entrySet()) {
			value = pair.getValue();
			type = AttributeType.fromValue(value);
			value = type.cast(value);
			oldValue = this.heap.put(pair.getKey(), value);
			if (oldValue==null) {
				fireAttributeAddedEvent(pair.getKey(), new AttributeValueImpl(type, value));
			}
			else {
				fireAttributeChangedEvent(pair.getKey(),
						new AttributeValueImpl(type, oldValue),
						new AttributeValueImpl(type, value));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttributes(AttributeProvider content) throws AttributeException {
		Object value, oldValue;
		for(Attribute attr : content.attributes()) {
			value = attr.getValue();
			oldValue = this.heap.put(attr.getName(), value);
			if (oldValue==null) {
				fireAttributeAddedEvent(attr.getName(),
						new AttributeValueImpl(attr.getType(), value));
			}
			else {
				fireAttributeChangedEvent(attr.getName(),
						new AttributeValueImpl(AttributeType.fromValue(oldValue), oldValue),
						new AttributeValueImpl(attr.getType(), value));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttributes(Map<String, Object> content) {
		setAttributesInternal(new TreeMap<String,Object>(content));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttributes(AttributeProvider content) throws AttributeException {
		Map<String,Object> newAttributes = new TreeMap<String,Object>();
		content.toMap(newAttributes);
		setAttributesInternal(newAttributes);
	}

	private void setAttributesInternal(Map<String, Object> newAttributes) {
		Iterator<Entry<String,Object>> iterator = this.heap.entrySet().iterator();
		Entry<String,Object> entry;
		Object newValue;
		AttributeType type;
		while (iterator.hasNext()) {
			entry = iterator.next();
			newValue = newAttributes.remove(entry.getKey());
			if (newValue==null) {
				iterator.remove();
				fireAttributeRemovedEvent(entry.getKey(),
						new AttributeValueImpl(AttributeType.fromValue(entry.getValue()), entry.getValue()));
			}
			else {
				type = AttributeType.fromValue(newValue);
				newValue = type.cast(newValue);
				entry.setValue(newValue);
				fireAttributeChangedEvent(entry.getKey(),
						new AttributeValueImpl(AttributeType.fromValue(entry.getValue()), entry.getValue()),
						new AttributeValueImpl(type, newValue));
			}
		}
		
		for(Entry<String,Object> e : newAttributes.entrySet()) {
			newValue = e.getValue();
			type = AttributeType.fromValue(newValue);
			newValue = type.cast(newValue);
			if (newValue!=null) {
				this.heap.put(e.getKey(), newValue);
				fireAttributeAddedEvent(e.getKey(),
						new AttributeValueImpl(type, newValue));
			}
		}
	}
			
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toMap(Map<String,Object> mapToFill) {
		mapToFill.putAll(this.heap);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttributeCount() {
		return this.heap.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAttribute(String name) {
		return this.heap.containsKey(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Attribute> getAllAttributes() {
		ArrayList<Attribute> list = new ArrayList<Attribute>(getAttributeCount());
		String name;
		AttributeImpl newAttr;
		Object rawValue;
		for(Entry<String, Object> entry : this.heap.entrySet()) {
			name = entry.getKey();
			if (name!=null) {
				rawValue = entry.getValue();
				newAttr = new AttributeImpl(name);
				newAttr.castAndSet(
						AttributeType.fromValue(rawValue),
						unprotectNull(rawValue));
				list.add(newAttr);
			}
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<AttributeType,Collection<Attribute>> getAllAttributesByType() {
		TreeMap<AttributeType,Collection<Attribute>> map = new TreeMap<AttributeType,Collection<Attribute>>();
		AttributeType type;
		Attribute attr;
		Object value;
		for(Entry<String,Object> entry : this.heap.entrySet()) {
			value = entry.getValue(); 
			if (value!=null) {
				type = AttributeType.fromValue(value);
				value = unprotectNull(value);
				Collection<Attribute> list = map.get(type);
				if (list==null) {
					list = new ArrayList<Attribute>();
					map.put(type, list);
				}
				attr = new AttributeImpl(entry.getKey());
				attr.castAndSet(type, value);
				list.add(attr);
			}
		}
		return map;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getAllAttributeNames() {
		return Collections.unmodifiableCollection(this.heap.keySet());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeValue getAttribute(String name) {
		return getStoredAttributeValue(name,null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		AttributeValue value = getStoredAttributeValue(name,
				default_value==null ? null : default_value.getType());
		if (value==null) return default_value;
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute getAttributeObject(String name) {
		return getStoredAttribute(name,null);
	}

	/** Replies the attribute with the given name.
	 * 
	 * @param name is the name of the attribute to retreive
	 * @param expectedType is the expected type for the attribute.
	 * @return the value or <code>null</code>
	 */
	protected Attribute getStoredAttribute(String name, AttributeType expectedType) {
		Object val = this.heap.get(name);
		if (val!=null) {
			AttributeType currentType = AttributeType.fromValue(val);
			val = unprotectNull(val);
			Attribute attr = new AttributeImpl(name);
			if (expectedType==null)
				attr.castAndSet(currentType,val);
			else
				attr.castAndSet(expectedType,val);
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
	protected AttributeValue getStoredAttributeValue(String name, AttributeType expectedType) {
		Object val = this.heap.get(name);
		if (val!=null) {
			AttributeType currentType = AttributeType.fromValue(val);
			val = unprotectNull(val);
			AttributeValue attr = new AttributeValueImpl(name);
			if (expectedType==null)
				attr.castAndSet(currentType, val);
			else
				attr.castAndSet(expectedType,val);
			return attr;
		}
		return null;
	}
	
	private AttributeValue copyValue(String name) {
		AttributeValue oldValue = null;
		Object currentValue = this.heap.get(name);
		if (currentValue!=null) {
			AttributeType oldType = AttributeType.fromValue(currentValue);
			oldValue = new AttributeValueImpl();
			oldValue.castAndSet(oldType, currentValue);
			return oldValue;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		AttributeType oldType = (oldValue==null) ? null : oldValue.getType();
	
		if (oldType==null || type==null || oldType==type) return null;
		
		Attribute attr = new AttributeImpl(name,(oldValue==null) ? null : oldValue.getValue());
		attr.cast(type);
		
		this.heap.put(name, protectNull(attr.getValue(), type));
		
		fireAttributeChangedEvent(name, oldValue, attr);

		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		assert(name!=null && value!=null);
		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, protectNull(value.getValue(), value.getType()));
		
		Attribute attr = new AttributeImpl(name,value.getValue());

		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, value);
		else
			fireAttributeAddedEvent(name, attr);

		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, boolean value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, Boolean.valueOf(value));
		
		Attribute attr = new AttributeImpl(name,value);
		
		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, int value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, Long.valueOf(value));
		
		Attribute attr = new AttributeImpl(name,value);		
		
		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, long value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, Long.valueOf(value));
		
		Attribute attr = new AttributeImpl(name,value);		
		
		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, float value) {
		assert(name!=null);

		AttributeValue oldValue;
		Object currentValue = this.heap.get(name);
		if (currentValue!=null) {
			oldValue = new AttributeValueImpl();
			oldValue.castAndSet(AttributeType.fromValue(currentValue), currentValue);
		}
		else {
			oldValue = null;
		}
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, Double.valueOf(value));
		
		Attribute attr = new AttributeImpl(name,value);		
		
		if (currentValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, double value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, Double.valueOf(value));
		
		Attribute attr = new AttributeImpl(name,value);		
		
		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, String value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		Object rv = (value==null) ? AttributeType.STRING.getDefaultValue() : value;
		this.heap.put(name, rv);
		
		Attribute attr = new AttributeImpl(name,rv);
		
		if (oldValue!=null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		else {
			fireAttributeAddedEvent(name, attr);
		}
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, UUID value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		Object rv = (value==null) ? AttributeType.UUID.getDefaultValue() : value;
		this.heap.put(name, rv);
		
		Attribute attr = new AttributeImpl(name,rv);
		
		if (oldValue!=null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		else {
			fireAttributeAddedEvent(name, attr);
		}
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URL value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, protectNull(value, AttributeType.URL));
		
		Attribute attr = new AttributeImpl(name,value);
		
		if (oldValue!=null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		else {
			fireAttributeAddedEvent(name, attr);
		}
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URI value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, protectNull(value, AttributeType.URI));
		
		Attribute attr = new AttributeImpl(name,value);
		
		if (oldValue!=null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		else {
			fireAttributeAddedEvent(name, attr);
		}
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Image value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, protectNull(value, AttributeType.IMAGE));
		
		Attribute attr = new AttributeImpl(name,value);
		
		if (oldValue!=null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		else {
			fireAttributeAddedEvent(name, attr);
		}
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Date value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		Object rv = (value==null) ? AttributeType.DATE.getDefaultValue() : value;
		this.heap.put(name, rv);
		
		Attribute attr = new AttributeImpl(name,rv);
		
		if (oldValue!=null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		else {
			fireAttributeAddedEvent(name, attr);
		}
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Color value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		Object rv = (value==null) ? AttributeType.COLOR.getDefaultValue() : value;
		this.heap.put(name, rv);
		
		Attribute attr = new AttributeImpl(name,rv);
		
		if (oldValue!=null) {
			fireAttributeChangedEvent(name, oldValue, attr);
		}
		else {
			fireAttributeAddedEvent(name, attr);
		}
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		assert(value!=null);
		String name = value.getName();
		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;
		
		this.heap.put(name, protectNull(value.getValue(), value.getType()));

		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, value);
		else
			fireAttributeAddedEvent(name, value);
		
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, value);
		
		Attribute attr = new AttributeImpl(name,value);
		
		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		return setAttribute(name, (value==null) ? null : value.getAddress());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, value);
		
		Attribute attr = new AttributeImpl(name,value);
		
		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		assert(name!=null);

		AttributeValue oldValue = copyValue(name);
		
		if (oldValue!=null && oldValue.equals(value)) return null;

		this.heap.put(name, value);
		
		Attribute attr = new AttributeImpl(name,value);
		
		if (oldValue!=null)
			fireAttributeChangedEvent(name, oldValue, attr);
		else
			fireAttributeAddedEvent(name, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAttribute(String name) {
		assert(name!=null);

		AttributeValue oldValue;
		Object currentValue = this.heap.remove(name);
		if (currentValue!=null) {
			oldValue = new AttributeValueImpl();
			oldValue.castAndSet(AttributeType.fromValue(currentValue), currentValue);
			fireAttributeRemovedEvent(name,oldValue);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllAttributes() {
		if (!this.heap.isEmpty()) {
			this.heap.clear();
			fireAttributeClearedEvent();
			return true;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		if (oldname==null || newname==null || oldname.equals(newname)) return false;
		
		AttributeValue valueOfOldName = copyValue(oldname);
		
		// The attribute does not exist.
		if (valueOfOldName==null) return false;
		
		AttributeValue oldValueOfNewName = copyValue(newname);
		
		// The target attribute is existing and overwrite was disabled
		if ((!overwrite)&&(oldValueOfNewName!=null)) return false;
		
		Object rawValue;
		
		try {
			rawValue = valueOfOldName.getValue();
		}
		catch (InvalidAttributeTypeException e) {
			rawValue = null;
		}
		catch (AttributeNotInitializedException e) {
			rawValue = null;
		}

		this.heap.remove(oldname);
		this.heap.put(newname, protectNull(rawValue, valueOfOldName.getType()));
		
		if (oldValueOfNewName!=null)
			fireAttributeRemovedEvent(newname,oldValueOfNewName);
		
		fireAttributeRenamedEvent(oldname,newname,valueOfOldName);
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void freeMemory() {
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.heap.toString();
	}

}


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
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

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
 * XXX: Make this provider to save asynchronously on the remote storage area.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class BufferedAttributeCollection extends AbstractAttributeCollection {
	
	private static final long serialVersionUID = 1865614675044905721L;
	
	private transient Map<String,AttributeValue> cache = new SoftValueTreeMap<String,AttributeValue>();
	
	/** Make a deep copy of this object and replies the copy.
	 * 
	 * @return the deep copy.
	 */
	@Override
	public BufferedAttributeCollection clone() {
		BufferedAttributeCollection clone = (BufferedAttributeCollection)super.clone();
		this.cache = new SoftValueTreeMap<String,AttributeValue>();
		return clone;
	}

	/** Load a value from the data source.
	 * 
	 * @param name is the name of the attribute to load
	 * @return the value of the attribute.
	 * @throws AttributeException on error or when the attribute does not exist
	 */
	protected abstract AttributeValue loadValue(String name) throws AttributeException;
	
	/**
	 * {@inheritDoc}
	 */
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
	 * @throws AttributeException 
	 */
	private AttributeValue extractValueFor(String name) throws AttributeException {
		AttributeValue value = null;
		if (this.cache.containsKey(name)) {
			value = this.cache.get(name);
		}
		else {
			value = loadValue(name);
			this.cache.put(name, value);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAttribute(String name) {
		return getAllAttributeNames().contains(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Attribute> getAllAttributes() {
		ArrayList<Attribute> list = new ArrayList<Attribute>(getAttributeCount());
		Attribute newAttr;
		for(String name : getAllAttributeNames()) {
			if (name!=null) {
				try {
					newAttr = new AttributeImpl(name, extractValueFor(name));
					list.add(newAttr);
				}
				catch(AttributeException _) {
					//
				}
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
		Attribute newAttr;
		for(String name : getAllAttributeNames()) {
			if (name!=null) {
				try {
					newAttr = new AttributeImpl(name, extractValueFor(name));
					Collection<Attribute> list = map.get(newAttr.getType());
					if (list==null) {
						list = new ArrayList<Attribute>();
						map.put(newAttr.getType(), list);
					}
					list.add(newAttr);
				}
				catch(AttributeException _) {
					//
				}
			}
		}
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeValue getAttribute(String name) {
		try {
			return new AttributeValueImpl(extractValueFor(name));
		}
		catch(AttributeException _) {
			//
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		AttributeValue value;
		try {
			value = new AttributeValueImpl(extractValueFor(name));
		}
		catch(AttributeException _) {
			value = default_value;
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute getAttributeObject(String name) {
		try {
			return new AttributeImpl(name,extractValueFor(name));
		}
		catch(AttributeException _) {
			//
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void freeMemory() {
		this.cache.clear();
	}
	
	/** Set the attribute value.
	 * 
	 * @param name is the name of the attribute
	 * @param value is the raw value to store.
	 * @return the new created attribute
	 * @throws AttributeException 
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
	 * @throws AttributeException 
	 */
	protected Attribute setAttributeFromRawValue(String name, AttributeType type, Object value) throws AttributeException {
		AttributeValue oldValue;
		try {
			oldValue = new AttributeValueImpl(extractValueFor(name));
		}
		catch(AttributeException _) {
			oldValue = null;
		}
				
		if (oldValue!=null && oldValue.equals(value)) return null;

		Attribute attr = new AttributeImpl(name,type);
		attr.setValue(type.cast(value));

		saveValue(name,attr);
	
		this.cache.put(name, attr);	
		
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
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		AttributeValue oldValue;
		try {
			oldValue = new AttributeValueImpl(extractValueFor(name));
		}
		catch(AttributeException _) {
			oldValue = null;
		}
		AttributeType oldType = (oldValue==null) ? null : oldValue.getType();

		if (oldValue==null || oldType==null || type==null || type==oldType) return null;
		
		Attribute attr = new AttributeImpl(name,oldValue.getValue());
		attr.cast(type);
		
		this.cache.put(name, attr);
		
		fireAttributeChangedEvent(name, oldValue, attr);
		
		return attr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		return setAttributeFromRawValue(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, boolean value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.BOOLEAN, Boolean.valueOf(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, int value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INTEGER, Long.valueOf(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, long value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INTEGER, Long.valueOf(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, float value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.REAL, Double.valueOf(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, double value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.REAL, Double.valueOf(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, String value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.STRING, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, UUID value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.UUID, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URL value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.URL, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URI value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.URI, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Image value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.IMAGE, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Date value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.DATE, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Color value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.COLOR, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INET_ADDRESS, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.INET_ADDRESS, value==null ? null : value.getAddress());
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.ENUMERATION, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		try {
			return setAttributeFromRawValue(name, AttributeType.TYPE, value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		return setAttributeFromRawValue(value.getName(), value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAttribute(String name) {
		try {
			if (hasAttribute(name)) {
				AttributeValue currentValue = extractValueFor(name);
				this.cache.remove(name);
				removeValue(name);
				fireAttributeRemovedEvent(name,currentValue);
				return true;
			}
		}
		catch(AttributeException _) {
			//
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllAttributes() {
		try {
			if (getAttributeCount()>0) {
				this.cache.clear();
				if (removeAllValues()) {
					fireAttributeClearedEvent();
					return true;
				}
			}
		}
		catch(AttributeException _) {
			//
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		try {
			AttributeValue valueForOldName = null;
			
			try {
				valueForOldName = extractValueFor(oldname);
			}
			catch(AttributeException _) {
				//
			}

			// The source attribute does not exist.
			if (valueForOldName==null) return false;
			
			AttributeValue oldValueForNewName = null;
			
			try {
				oldValueForNewName = extractValueFor(newname);
			}
			catch(AttributeException _) {
				//
			}
			
			// Target attribute is existing and overwrite was disabled.
			if ((!overwrite)&&(oldValueForNewName!=null)) return false;
			
			AttributeValue oldValueCopyForNewName = new AttributeValueImpl(oldValueForNewName);
			
			removeValue(oldname);
			this.cache.remove(oldname);
			if (valueForOldName instanceof Attribute) {
				((Attribute)valueForOldName).setName(newname);
			}
			saveValue(newname, valueForOldName);
			this.cache.put(newname, valueForOldName);
			
			if (oldValueForNewName!=null)
				fireAttributeRemovedEvent(newname,oldValueCopyForNewName);
			
			fireAttributeRenamedEvent(oldname,newname,valueForOldName);
			
			return true;
		}
		catch(AttributeException _) {
			//
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
		//
	}

}


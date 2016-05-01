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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.references.SoftValueTreeMap;

/**
 * This class implements an abstract attribute container that use
 * a memory cache.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBufferedAttributeProvider extends AbstractAttributeProvider {
	
	private static final long serialVersionUID = -4231000555346674004L;
	
	private transient Map<String,AttributeValue> cache = new SoftValueTreeMap<>();
	
	/** Make a deep copy of this object and replies the copy.
	 * 
	 * @return the deep copy.
	 */
	@Override
	public AbstractBufferedAttributeProvider clone() {
		AbstractBufferedAttributeProvider clone = (AbstractBufferedAttributeProvider)super.clone();
		clone.cache = new SoftValueTreeMap<>(this.cache);
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
	
	/** Replies the value associated to the specified name.
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
		ArrayList<Attribute> list = new ArrayList<>(getAttributeCount());
		Attribute newAttr;
		for(String name : getAllAttributeNames()) {
			if (name!=null) {
				try {
					newAttr = new AttributeImpl(name, extractValueFor(name));
					list.add(newAttr);
				}
				catch(AttributeException exception) {
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
		TreeMap<AttributeType,Collection<Attribute>> map = new TreeMap<>();
		Attribute newAttr;
		for(String name : getAllAttributeNames()) {
			if (name!=null) {
				try {
					newAttr = new AttributeImpl(name, extractValueFor(name));
					Collection<Attribute> list = map.get(newAttr.getType());
					if (list==null) {
						list = new ArrayList<>();
						map.put(newAttr.getType(), list);
					}
					list.add(newAttr);
				}
				catch(AttributeException exception) {
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
		catch(AttributeException exception) {
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
		catch(AttributeException exception) {
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
		catch(AttributeException exception) {
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
	
}


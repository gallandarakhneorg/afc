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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.xtext.xbase.lib.Pure;

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

	private transient Map<String, AttributeValue> cache = new SoftValueTreeMap<>();

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Override
	public AbstractBufferedAttributeProvider clone() {
		final AbstractBufferedAttributeProvider clone = (AbstractBufferedAttributeProvider) super.clone();
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

	@Pure
	@Override
	public abstract Collection<String> getAllAttributeNames();

	/** Replies the value associated to the specified name.
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
		Attribute newAttr;
		for (final String name : getAllAttributeNames()) {
			if (name != null) {
				try {
					newAttr = new AttributeImpl(name, extractValueFor(name));
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
		Attribute newAttr;
		for (final String name : getAllAttributeNames()) {
			if (name != null) {
				try {
					newAttr = new AttributeImpl(name, extractValueFor(name));
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

}


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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.references.SoftValueTreeMap;

/**
 * This class contains a collection of attribute containers and
 * tries to gather the data.
 * This class contains a collection of AttributeContainers and
 * exhibites the values of the attributes of all these containers.
 * This class follows the following rules (in that order)
 * to retreive the value of an attribute:<ol>
 * <li>If the attribute is defined in none of the containers, throws the standard exception;</li>
 * <li>If the attribute is defined in only one of the containers, replies the attribute value itself;</li>
 * <li>If the attribute is defined in more than one container:<ol>
 * 		<li>if all the values are equal, then replies one of the attribute values;</li>
 * 		<li>if the values are not equal and all the values have equivalent types (as replied
 * 			by {@link AttributeType#isAssignableFrom(AttributeType)}), then replies an
 * 			attribute value with a "undefined" value and of the type of one of the values;</li>
 * 		<li>if the values are not equal and one of the value has not an equivalent type to
 * 			the others (as replied by {@link AttributeType#isAssignableFrom(AttributeType)}),
 * 			then replies an attribute value with a "undefined" value and of the type OBJECT.</li>
 * 		</ol></li>
 * </ol>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class MultiAttributeProvider extends AbstractAttributeProvider {

	private static final long serialVersionUID = -2673023673767450220L;

	/** Cache of the attribute values.
	 */
	transient Map<String, AttributeValue> cache = new SoftValueTreeMap<>();

	/** Cache of the names of the attributes.
	 */
	Set<String> names;

	private Collection<AttributeProvider> containers = new ArrayList<>();

	@Override
	public void toMap(Map<String, Object> mapToFill) {
		for (final AttributeProvider provider : this.containers) {
			mapToFill.putAll(provider.toMap());
		}
	}

	/** Replies the value associated to the specified name.
	 */
	private Attribute extract(String name) {
		AttributeValue value = null;
		if (this.cache.containsKey(name)) {
			value = this.cache.get(name);
		} else {
			final ManyValueAttributeValue result = new ManyValueAttributeValue();
			AttributeValue attrValue;
			for (final AttributeProvider c : this.containers) {
				attrValue = c.getAttribute(name);
				assign(result, attrValue);
			}
			value = canonize(result);
			this.cache.put(name, value);
		}
		return (value != null) ? new AttributeImpl(name, value) : null;
	}

	/** Assign the value v2 to v1 and change v1 according
	 * to the types of v1 and v2.
	 * Do not forget to invoke {@link #canonize(ManyValueAttributeValue)} on
	 * {@code v1}.
	 *
	 * @param v1 first value.
	 * @param v2 second value.
	 */
	static void assign(ManyValueAttributeValue v1, AttributeValue v2) {
		if (v2 != null) {
			v1.setTopType(v2.getType());
		}
		if (v2 == null || !v2.isAssigned()) {
			v1.setMultipleValues(true);
		} else {
			assert v2.isAssigned();
			if (!v1.isAssigned()) {
				v1.setValue(v2);
			} else if (!v2.equals(v1)) {
				v1.setMultipleValues(true);
				if (!v1.isAssignableFrom(v2)) {
					if (!v2.isAssignableFrom(v1)) {
						v1.setInternalValue(null, AttributeType.OBJECT);
					} else {
						v1.setInternalValue(null, v2.getType());
					}
				}
			}
		}
	}

	/** Replace any indicator put by {@link #assign(ManyValueAttributeValue, AttributeValue)}
	 * to retreive a standard attribute value.
	 *
	 * @param v is the value to canonize.
	 * @return the canonized value.
	 */
	static AttributeValue canonize(ManyValueAttributeValue v) {
		final AttributeValueImpl attr;
		if (v.hasMultipleValues()) {
			if (v.getTopType() == null) {
				return null;
			}
			attr = new AttributeValueImpl(v.getTopType(), null);
		} else {
			attr = new AttributeValueImpl();
			attr.setValue(v);
		}
		return attr;
	}

	/** Replies a collection on the containers.
	 *
	 * @return a collection on the containers.
	 */
	@Pure
	protected Collection<AttributeProvider> containers() {
		return Collections.unmodifiableCollection(this.containers);
	}

	/** Add a container in this set.
	 *
	 * @param container the container.
	 * @return <code>true</code> if the container has been added,
	 *     otherwise <code>false</code>
	 */
	public boolean addAttributeContainer(AttributeProvider container) {
		if (this.containers.add(container)) {
			freeMemory();
			return true;
		}
		return false;
	}

	/** Remove a container in this set.
	 *
	 * @param container the container.
	 * @return <code>true</code> if the container has been removed,
	 *     otherwise <code>false</code>
	 */
	public boolean removeAttributeContainer(AttributeProvider container) {
		if (this.containers.remove(container)) {
			freeMemory();
			return true;
		}
		return false;
	}

	/** Replies the number of attribute containers in this MultiAttributeContainer.
	 *
	 * @return the number of attribute containers in this MultiAttributeContainer.
	 */
	@Pure
	public int getAttributeContainerCount() {
		return this.containers.size();
	}

	@Pure
	@Override
	public MultiAttributeProvider clone() {
		final MultiAttributeProvider clone = (MultiAttributeProvider) super.clone();
		clone.cache = new SoftValueTreeMap<>();
		for (final Entry<String, AttributeValue> e : this.cache.entrySet()) {
			clone.cache.put(e.getKey(), new AttributeValueImpl(e.getValue()));
		}
		clone.containers = new ArrayList<>(this.containers);
		if (this.names != null) {
			clone.names = new TreeSet<>(new AttributeNameStringComparator());
			clone.names.addAll(this.names);
		}
		return clone;
	}

	@Override
	public void freeMemory() {
		this.cache.clear();
		if (this.names != null) {
			this.names.clear();
		}
		this.names = null;
	}

	@Pure
	@Override
	public Collection<Attribute> getAllAttributes() {
		final List<Attribute> list = new ArrayList<>(getAttributeCount());
		Attribute newAttr;
		for (final String name : getAllAttributeNames()) {
			if (name != null) {
				newAttr = extract(name);
				if (newAttr != null) {
					list.add(newAttr);
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
				newAttr = extract(name);
				if (newAttr != null) {
					Collection<Attribute> list = map.get(newAttr.getType());
					if (list == null) {
						list = new ArrayList<>();
						map.put(newAttr.getType(), list);
					}
					list.add(newAttr);
				}
			}
		}
		return map;
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name) {
		return extract(name);
	}

	@Pure
	@Override
	public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
		AttributeValue value = extract(name);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	@Pure
	@Override
	public int getAttributeCount() {
		return getAllAttributeNames().size();
	}

	@Pure
	@Override
	public Collection<String> getAllAttributeNames() {
		if (this.names == null) {
			final Set<String> names = new TreeSet<>(new AttributeNameStringComparator());
			for (final AttributeProvider c : this.containers) {
				names.addAll(c.getAllAttributeNames());
			}
			this.names = names;
		}
		return Collections.unmodifiableSet(this.names);
	}

	@Pure
	@Override
	public Attribute getAttributeObject(String name) {
		return extract(name);
	}

	@Pure
	@Override
	public boolean hasAttribute(String name) {
		for (final AttributeProvider c : this.containers) {
			if (c.hasAttribute(name)) {
				return true;
			}
		}
		return false;
	}

	/** This class provides an implementation of attribute value
	 * that may be marked with an indicators that many
	 * values are possibles for the attribute.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	static class ManyValueAttributeValue extends AttributeValueImpl {

		private static final long serialVersionUID = -5153126369737554181L;

		private boolean hasMultipleValues;

		private AttributeType topType;

		/** Replies the type type associated to this attribute value.
		 *
		 * @return the top type associated to this attribute value.
		 */
		@Pure
		public AttributeType getTopType() {
			return this.topType;
		}

		/** Set the top type associated to this attribute value.
		 *
		 * @param type is the top type associated to this attribute value.
		 */
		public void setTopType(AttributeType type) {
			if (this.topType == null) {
				this.topType = type;
			} else if (this.topType != type && !this.topType.isAssignableFrom(type)) {
				if (type.isAssignableFrom(this.topType)) {
					this.topType = type;
				} else {
					this.topType = AttributeType.OBJECT;
				}
			}
		}

		@Override
		public final void setInternalValue(Object value) {
			super.setInternalValue(value);
		}

		@Override
		public final void setInternalValue(Object value, AttributeType type) {
			super.setInternalValue(value, type);
		}

		/** Replies if this attribute has multiple values.
		 *
		 * @return <code>true</code> if this attribute has multiple
		 *     values, otherwise <code>false</code>.
		 */
		@Pure
		public boolean hasMultipleValues() {
			return this.hasMultipleValues;
		}

		/** Set if this attribute has multiple values.
		 *
		 * @param v is <code>true</code> if this attribute has multiple
		 *     values, otherwise <code>false</code>.
		 */
		public void setMultipleValues(boolean v) {
			this.hasMultipleValues = v;
		}

	}

}


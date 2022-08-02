/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;

/**
 * Iterator on attributes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeIterator implements Iterator<Attribute> {

	private AttributeProvider provider;

	private final ArrayList<String> names = new ArrayList<>();

	private String lastName;

	/**
	 * Create an iterator on the given container.
	 *
	 * @param provider is the container of attributes.
	 */
	public AttributeIterator(AttributeProvider provider) {
		this.provider = provider;
		this.names.addAll(provider.getAllAttributeNames());
	}

	@Pure
	@Override
	public boolean hasNext() {
		if ((this.provider == null) || (this.names.isEmpty())) {
			this.provider = null;
			return false;
		}
		return true;
	}

	@Override
	public Attribute next() {
		if ((this.provider == null) || (this.names.isEmpty())) {
			throw new NoSuchElementException();
		}

		final String name = this.names.remove(0);
		if (name == null) {
			throw new NoSuchElementException();
		}

		final Attribute attr = this.provider.getAttributeObject(name);
		if (attr == null) {
			throw new NoSuchElementException();
		}

		this.lastName = name;

		if (this.names.isEmpty()) {
			this.provider = null;
		}

		return attr;
	}

	@Override
	public void remove() {
		if ((this.provider == null) || (this.lastName == null)) {
			throw new NoSuchElementException();
		}
		if (this.provider instanceof AttributeCollection) {
			((AttributeCollection) this.provider).removeAttribute(this.lastName);
		}
		this.lastName = null;
	}

}

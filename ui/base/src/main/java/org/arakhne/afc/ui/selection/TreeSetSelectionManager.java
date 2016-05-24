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

package org.arakhne.afc.ui.selection ;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/** Implementation of a SelectionManager based on a Tree Set.
 *
 * @param <OBJ> is the type of the objects inside this manager.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class TreeSetSelectionManager<OBJ extends Selectable & Comparable<? super OBJ>> extends SelectionManager<OBJ> {

	/** The collection of Selection instances.
	 */
	protected Set<OBJ> storage = new TreeSet<>();

	/** Create a new SelectionManager.
	 * 
	 * @param elementType is the type of the elements inside this selection manager.
	 */
	public TreeSetSelectionManager(Class<OBJ> elementType) {
		super(elementType);
	}

	@Override
	protected synchronized boolean removeFromStorage(OBJ object) {
		return this.storage.remove(object);
	}

	@Override
	protected synchronized boolean addInStorage(OBJ object) {
		return this.storage.add(object);
	}
	
	@Override
	protected synchronized Iterator<OBJ> getIteratorOnStorage() {
		return this.storage.iterator();
	}

	@Override
	protected synchronized Collection<OBJ> clearStorage() {
		Set<OBJ> old = this.storage;
		this.storage = new TreeSet<>();
		return old;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final int size() {
		return this.storage.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean isEmpty() {
		return this.storage.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean contains(Object o) {
		return this.storage.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final Object[] toArray() {
		return this.storage.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final <T> T[] toArray(T[] a) {
		return this.storage.toArray(a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean containsAll(Collection<?> c) {
		return this.storage.containsAll(c);
	}

}

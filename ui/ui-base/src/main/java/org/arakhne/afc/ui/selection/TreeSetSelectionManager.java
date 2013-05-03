/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.ui.selection ;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/** Implementation of a SelectionManager based on a Tree Set.
 *
 * @param <OBJ> is the type of the objects inside this manager.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TreeSetSelectionManager<OBJ extends Selectable & Comparable<? super OBJ>> extends SelectionManager<OBJ> {

	/** The collection of Selection instances.
	 */
	protected Set<OBJ> storage = new TreeSet<OBJ>();

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
		this.storage = new TreeSet<OBJ>();
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

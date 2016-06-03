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

package org.arakhne.afc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A collection composed of collections.
 *
 * <p>This collection is not thread-safe.
 *
 * <p>This collection is read-only.
 *
 * @param <E> is the type of elements in the collections.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiCollection<E> implements Collection<E> {

	private final List<Collection<? extends E>> collections = new ArrayList<>();

	/** Construct the collection.
	 */
	public MultiCollection() {
		//
	}

	/** Add a collection inside this multicollection.
	 *
	 * @param collection the collection to add.
	 */
	public void addCollection(Collection<? extends E> collection) {
		if (collection != null && !collection.isEmpty()) {
			this.collections.add(collection);
		}
	}

	/** Remove a collection from this multicollection.
	 *
	 * @param collection the collection to remove.
	 * @return <code>true</code> if the multi-collection has changed,
	 *      otherwise <code>false</code>.
	 */
	public boolean removeCollection(Collection<? extends E> collection) {
		return this.collections.remove(collection);
	}

	@Override
	public void clear() {
		this.collections.clear();
	}

	/**
	 * This function is not supported, see {@link #addCollection(Collection)}.
	 */
	@Override
	public boolean add(E value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This function is not supported, see {@link #addCollection(Collection)}.
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public boolean contains(Object obj) {
		for (final Collection<? extends E> c : this.collections) {
			if (c.contains(obj)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Pure
	public boolean containsAll(Collection<?> collection) {
		for (final Object o : collection) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Pure
	public boolean isEmpty() {
		for (final Collection<? extends E> c : this.collections) {
			if (!c.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Pure
	public Iterator<E> iterator() {
		return new MultiIterator<>(this.collections.iterator());
	}

	/**
	 * This function is not supported, see {@link #removeCollection(Collection)}.
	 */
	@Override
	public boolean remove(Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This function is not supported, see {@link #removeCollection(Collection)}.
	 */
	@Override
	public boolean removeAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This function is not supported, see {@link #removeCollection(Collection)}.
	 */
	@Override
	public boolean retainAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public int size() {
		int t = 0;
		for (final Collection<? extends E> c : this.collections) {
			t += c.size();
		}
		return t;
	}

	@Override
	@Pure
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		throw new UnsupportedOperationException();
	}

	/** Iterator on multicollection.
	 *
	 * @param <E> is the type of elements in the collections.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class MultiIterator<E> implements Iterator<E> {

		private final Iterator<Collection<? extends E>> iterator;

		private Iterator<? extends E> currentIterator;

		/** Construct the iterator.
		 *
		 * @param iterator original iterator.
		 */
		public MultiIterator(Iterator<Collection<? extends E>> iterator) {
			this.iterator = iterator;
			searchNext();
		}

		private void searchNext() {
			if (this.currentIterator == null || !this.currentIterator.hasNext()) {
				this.currentIterator = null;
				while (this.currentIterator == null && this.iterator.hasNext()) {
					final Collection<? extends E> iterable = this.iterator.next();
					final Iterator<? extends E> iter = iterable.iterator();
					if (iter.hasNext()) {
						this.currentIterator = iter;
					}
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.currentIterator != null && this.currentIterator.hasNext();
		}

		@Override
		public E next() {
			if (this.currentIterator == null) {
				throw new NoSuchElementException();
			}
			final E n = this.currentIterator.next();
			searchNext();
			return n;
		}

	}

}

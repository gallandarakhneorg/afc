/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2014 Stephane GALLAND.
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
package org.arakhne.afc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A collection composed of collections.
 * <p>
 * This collection is not thread-safe.
 * <p>
 * This collection is read-only.
 * 
 * @param <E> is the type of elements in the collections.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiCollection<E>
implements Collection<E> {
	
	private final List<Collection<? extends E>> collections = new ArrayList<Collection<? extends E>>();
	
	/**
	 */
	public MultiCollection() {
		//
	}

	/** Add a collection inside this multicollection.
	 * 
	 * @param collection
	 */
	public void addCollection(Collection<? extends E> collection) {
		if (collection!=null && !collection.isEmpty()) {
			this.collections.add(collection);
		}
	}
	
	/** Remove a collection from this multicollection.
	 * 
	 * @param collection
	 * @return <code>true</code> if the multi-collection has changed,
	 * otherwise <code>false</code>.
	 */
	public boolean removeCollection(Collection<? extends E> collection) {
		return this.collections.remove(collection);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.collections.clear();
	}

	/**
	 * This function is not supported, see {@link #addCollection(Collection)}.
	 */
	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This function is not supported, see {@link #addCollection(Collection)}.
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		for(Collection<? extends E> c : this.collections) {
			if (c.contains(o)) return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c) {
			if (!contains(o)) return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		for(Collection<? extends E> c : this.collections) {
			if (!c.isEmpty()) return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator() {
		return new MultiIterator<E>(this.collections.iterator());
	}

	/**
	 * This function is not supported, see {@link #removeCollection(Collection)}.
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This function is not supported, see {@link #removeCollection(Collection)}.
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This function is not supported, see {@link #removeCollection(Collection)}.
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		int t = 0;
		for(Collection<? extends E> c : this.collections) {
			t += c.size();
		}
		return t;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param <E> is the type of elements in the collections.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class MultiIterator<E> implements Iterator<E> {
		
		private final Iterator<Collection<? extends E>> iterator;
		private Iterator<? extends E> currentIterator;
		
		/**
		 * @param i
		 */
		public MultiIterator(Iterator<Collection<? extends E>> i) {
			this.iterator = i;
			searchNext();
		}
		
		private void searchNext() {
			if (this.currentIterator==null || !this.currentIterator.hasNext()) {
				this.currentIterator = null;
				while (this.currentIterator==null && this.iterator.hasNext()) {
					Collection<? extends E> iterable = this.iterator.next();
					Iterator<? extends E> iter = iterable.iterator();
					if (iter.hasNext()) {
						this.currentIterator = iter;
					}
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.currentIterator!=null && this.currentIterator.hasNext();
		}

		@Override
		public E next() {
			if (this.currentIterator==null) throw new NoSuchElementException();
			E n = this.currentIterator.next();
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		
	}
	
}

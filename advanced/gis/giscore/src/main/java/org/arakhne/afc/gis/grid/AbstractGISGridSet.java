/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.gis.grid;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.GISSet;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.ReflectionUtil;

/**
 * This class describes a grid that contains GIS primitives
 * and thatp permits to find them according to there geo-location.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
abstract class AbstractGISGridSet<P extends GISPrimitive> implements GISSet<P> {

	/** Internal representation.
	 */
	protected final Grid<P> grid;

	private Class<? extends P> clazz;

	private boolean updateWhenRemove;

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param bounds are the bounds of the grid cell.
	 */
	AbstractGISGridSet(int nRows, int nColumns, Rectangle2d bounds) {
		this(nRows, nColumns, bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
	}

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	AbstractGISGridSet(int nRows, int nColumns, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		this.grid = new Grid<>(nRows, nColumns,
				new Rectangle2d(
						boundsX, boundsY,
						boundsWidth, boundsHeight));
	}

	//-----------------------------------------------------------------
	// Utilities
	//----------------------------------------------------------------

	/** Extract the upper class that contains all the elements of
	 * this array.
	 *
	 * @param <E> is the type of the list's elements.
	 * @param collection is the collection to scan
	 * @return the top class of all the elements.
	 */
	@SuppressWarnings("unchecked")
	protected static <E> Class<? extends E> extractClassFrom(Collection<? extends E> collection) {
		Class<? extends E> clazz = null;
		for (final E elt : collection) {
			clazz = (Class<? extends E>) ReflectionUtil.getCommonType(clazz, elt.getClass());
		}
		return clazz == null ? (Class<E>) Object.class : clazz;
	}

	/** Replies the bounds of the cell.
	 *
	 * @return the cell.
	 */
	@Pure
	public Rectangle2d getBounds() {
		return this.grid.getBounds();
	}

	/** Replies the number of rows in the grid.
	 *
	 * @return the number of rows in the grid.
	 */
	@Pure
	public int getRowCount() {
		return this.grid.getRowCount();
	}

	/** Replies the number of columns in the grid.
	 *
	 * @return the number of columns in the grid.
	 */
	@Pure
	public int getColumnCount() {
		return this.grid.getColumnCount();
	}

	@Override
	public boolean isTypeRecomputedAfterRemoval() {
		return this.updateWhenRemove;
	}

	@Override
	public void setTypeRecomputedAfterRemoval(boolean update) {
		this.updateWhenRemove = update;
	}

	/** Update the component type information with
	 * the type of the new array element.
	 *
	 * @param newElement is the element for which the known top type in this array must be eventually updated.
	 */
	@SuppressWarnings("unchecked")
	protected final void updateComponentType(P newElement) {
		final Class<? extends P> lclazz = (Class<? extends P>) newElement.getClass();
		this.clazz = (Class<? extends P>) ReflectionUtil.getCommonType(this.clazz, lclazz);
	}

	/** Update the component type information with
	 * the type of the new array element.
	 *
	 * @param newElements are the elements for which the known top type in this array must be eventually updated.
	 */
	@SuppressWarnings("unchecked")
	protected final void updateComponentType(Collection<? extends P> newElements) {
		final Class<? extends P> lclazz = extractClassFrom(newElements);
		this.clazz = (Class<? extends P>) ReflectionUtil.getCommonType(this.clazz, lclazz);
	}

	@Override
	@Pure
	public Class<? extends P> getElementType() {
		return this.clazz;
	}

	@Override
	@Pure
	public P get(GeoId identifier) {
		if (identifier != null) {
			final Rectangle2d objBounds = identifier.toBounds2D();
			if (objBounds != null) {
				final Iterator<P> iterator = this.grid.iterator(objBounds);
				P element;
				while (iterator.hasNext()) {
					element = iterator.next();
					if (element.getGeoId().equals(identifier)) {
						return element;
					}
				}
			}
		}
		return null;
	}

	@Override
	@Pure
	public P get(GeoLocation location) {
		if (location != null) {
			final Rectangle2d objBounds = location.toBounds2D();
			if (objBounds != null) {
				final Iterator<P> iterator = this.grid.iterator(objBounds);
				P element;
				while (iterator.hasNext()) {
					element = iterator.next();
					if (element.getGeoLocation().equals(location)) {
						return element;
					}
				}
			}
		}
		return null;
	}

	@Override
	@Pure
	public P get(int index) {
		return this.grid.getElementAt(index);
	}

	//-----------------------------------------------------------------
	// Collection Interface
	//----------------------------------------------------------------

	@Override
	public void clear() {
		this.grid.clear();
		this.clazz = null;
	}

	@Override
	@Pure
	public boolean isEmpty() {
		return this.grid.isEmpty();
	}

	@Override
	@Pure
	public int size() {
		return this.grid.getElementCount();
	}

	@Override
	@Pure
	public int computeSize() {
		return this.grid.getElementCount();
	}

	@Override
	@Pure
	public Iterator<P> iterator() {
		return this.grid.iterator();
	}

	@Override
	@Pure
	public Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
		return iterator(clipBounds, -1);
	}

	@Override
	@Pure
	public Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, int budget) {
		return this.grid.iterator(clipBounds, budget);
	}

	@Override
	@Pure
	public Object[] toArray() {
		final int count = this.grid.getElementCount();
		final Object[] tab = new Object[count];
		int i = 0;
		for (final P element : this.grid) {
			if (i >= count) {
				break;
			}
			tab[i] = element;
			++i;
		}
		return tab;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Pure
	public <T> T[] toArray(T[] array) {
		assert array != null;
		final Class<T[]> clazz1 = (Class<T[]>) array.getClass();
		final Class<? extends T> clazz2 = (Class<? extends T>) clazz1.getComponentType();

		int count = this.grid.getElementCount();
		T[] tab = array;

		if (array.length > count) {
			count = array.length;
		}
		if (array.length < count) {
			tab = clazz1.cast(Array.newInstance(clazz2, count));
		}

		int i = 0;
		for (final P element : this.grid) {
			if (i >= count) {
				break;
			}
			tab[i] = clazz2.cast(element);
			++i;
		}
		return tab;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Pure
	public boolean contains(Object obj) {
		if (obj == null) {
			return false;
		}
		try {
			final P primitive = (P) obj;
			for (final GridCell<P> cell : this.grid.getGridCellsOn(primitive.getGeoLocation().toBounds2D())) {
				for (final P data : cell) {
					if (data.equals(obj)) {
						return true;
					}
				}
			}
		} catch (ClassCastException exception) {
			//
		}
		return false;
	}

	@Override
	@Pure
	public boolean slowContains(Object obj) {
		return contains(obj);
	}

	@Override
	@Pure
	public boolean containsAll(Collection<?> col) {
		for (final Object object : col) {
			if (!contains(object)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean remove(Object obj) {
		if (this.clazz != null && this.clazz.isInstance(obj)) {
			return this.grid.removeElement(this.clazz.cast(obj));
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> col) {
		boolean changed = false;
		for (final Object o : col) {
			if (remove(o)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> col) {
		clear();
		return addAll(new CheckedCollection(col));
	}

	@Override
	public boolean addAll(Collection<? extends P> col) {
		if (col == null) {
			return false;
		}
		boolean changed = false;
		for (final P element : col) {
			if (add(element)) {
				changed = true;
			}
		}
		return changed;
	}

	//-----------------------------------------------------------------
	// Several functions compliant with List interface
	//----------------------------------------------------------------

	@Override
	@Pure
	public int indexOf(Object obj) {
		if (this.clazz.isInstance(obj)) {
			return this.grid.indexOf(this.clazz.cast(obj));
		}
		return -1;
	}

	//-----------------------------------------------------------------
	// Dedicated API
	//----------------------------------------------------------------

	@Override
	@Pure
	public Iterator<Rectangle2afp<?, ?, ?, ?, ?, ?>> boundsIterator() {
		return new BoundsIterator(this.grid.getGridCells().iterator());
	}

	@Override
	@Pure
	public Iterable<P> toIterable(final Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
		return () -> AbstractGISGridSet.this.iterator(clipBounds);
	}

	@Override
	@Pure
	public Iterable<P> toIterable(final Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, final int budget) {
		return () -> AbstractGISGridSet.this.iterator(clipBounds, budget);
	}

	//-----------------------------------------------------------------
	// Subclasses
	//----------------------------------------------------------------

	/** Internal collection.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class CheckedCollection implements Collection<P>, Serializable {

		private static final long serialVersionUID = 1867729331611401138L;

		private final Collection<?> collection;

		/** Constructor.
		 * @param col the collection.
		 */
		CheckedCollection(Collection<?> col) {
			if (col == null) {
				throw new NullPointerException();
			}
			this.collection = col;
		}

		@Override
		public int size() {
			return this.collection.size();
		}

		@Override
		@Pure
		public boolean isEmpty() {
			return this.collection.isEmpty();
		}

		@Override
		@Pure
		public boolean contains(Object obj) {
			return this.collection.contains(obj);
		}

		@Override
		@Pure
		public Object[] toArray() {
			return this.collection.toArray();
		}

		@Pure
		@Override
		public <T> T[] toArray(T[] array) {
			return this.collection.toArray(array);
		}

		@Override
		@Pure
		public String toString() {
			return this.collection.toString();
		}

		@Override
		public boolean remove(Object obj) {
			return this.collection.remove(obj);
		}

		@Override
		public boolean containsAll(Collection<?> coll) {
			return this.collection.containsAll(coll);
		}

		@Override
		public boolean removeAll(Collection<?> coll) {
			return this.collection.removeAll(coll);
		}

		@Override
		public boolean retainAll(Collection<?> coll) {
			return this.collection.retainAll(coll);
		}

		@Override
		public void clear() {
			this.collection.clear();
		}

		@Override
		@Pure
		public Iterator<P> iterator() {
			return new CheckedIterator();
		}

		@Override
		public boolean add(P elt) {
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends P> coll) {
			return false;
		}

		/** Internal iterator.
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 * @since 14.0
		 */
		private class CheckedIterator implements Iterator<P> {

			private P next;

			private final Iterator<?> it;

			@SuppressWarnings({ "unchecked" })
			CheckedIterator() {
				this.it = CheckedCollection.this.collection.iterator();
				this.next = null;
				while (this.it.hasNext()) {
					try {
						this.next = (P) this.it.next();
						if (this.next != null) {
							break;
						}
					} catch (ClassCastException exception) {
						this.next = null;
					}
				}
			}

			@Override
			@Pure
			public boolean hasNext() {
				return this.next != null;
			}

			@Override
			@SuppressWarnings("unchecked")
			public P next() {
				final P n = this.next;
				this.next = null;
				while (this.it.hasNext()) {
					try {
						this.next = (P) this.it.next();
						if (this.next != null) {
							break;
						}
					} catch (Exception exception) {
						this.next = null;
					}
				}
				return n;
			}

			/** {@inheritDoc}
			 */
			@Override
			public void remove() {
				this.it.remove();
			}
		}

	} /* class CheckedCollection */

	/**
	 * This class describes an iterator on node bounds.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @see GISPrimitive
	 */
	private static class BoundsIterator implements Iterator<Rectangle2afp<?, ?, ?, ?, ?, ?>> {

		private final Iterator<? extends GridCell<?>> iterator;

		/** Constructor.
		 *
		 * @param iterator the iterator.
		 */
		BoundsIterator(Iterator<? extends GridCell<?>> iterator) {
			this.iterator = iterator;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public Rectangle2afp<?, ?, ?, ?, ?, ?> next() {
			final GridCell<?> node = this.iterator.next();
			return node.getBounds();
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

	} /* class BoundsIterator */

}

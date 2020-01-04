/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * Element of the grid.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
class GridCell<P extends GISPrimitive> implements Iterable<P> {

	private final int row;

	private final int col;

	private final Rectangle2d bounds;

	@SuppressWarnings("checkstyle:illegaltype")
	private final TreeSet<GridCellElement<P>> elements = new TreeSet<>();

	private int referenceElementCount;

	/** Constructor.
	 * @param row is the row index of the cell.
	 * @param column is the column index of the cell.
	 * @param bounds are the bounds of the grid cell.
	 */
	GridCell(int row, int column, Rectangle2d bounds) {
		this.row = row;
		this.col = column;
		this.bounds = bounds;
	}

	@Override
	@Pure
	public String toString() {
		return "{r=" + this.row + ",c=" + this.col + "}"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	/** Replies if the cell is empty, or not.
	 *
	 * @return <code>true</code> if the cell is empty; otherwise <code>false</code>.
	 */
	@Pure
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	/** Replies the number of elements that are inside this cell and
	 * for which this cell is the reference cell.
	 *
	 * @return the referenced cells.
	 */
	@Pure
	public int getReferenceElementCount() {
		return this.referenceElementCount;
	}

	/** Replies the reference element at the specified index in this cell.
	 *
	 * @param index the index.
	 * @return the element.
	 */
	@Pure
	public P getElementAt(int index) {
		if (index >= 0 && index < this.referenceElementCount) {
			int idx = 0;
			for (final GridCellElement<P> element : this.elements) {
				if (element.isReferenceCell(this)) {
					if (idx == index) {
						return element.get();
					}
					++idx;
				}
			}
		}
		throw new IndexOutOfBoundsException();
	}

	/** Replies the index of the specified element.
	 *
	 * @param element the element.
	 * @return the index of the specified element or {@code -1} if it was not found.
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Pure
	public int indexOf(P element) {
		int idx = 0;
		for (final GridCellElement<P> e : this.elements) {
			if (e.isReferenceCell(this)) {
				if (e.equals(element)) {
					return idx;
				}
				++idx;
			}
		}
		return -1;
	}

	/** Replies the row index of the cell.
	 *
	 * @return the row index of the cell.
	 */
	@Pure
	public int row() {
		return this.row;
	}

	/** Replies the column index of the cell.
	 *
	 * @return the column index of the cell.
	 */
	@Pure
	public int column() {
		return this.col;
	}

	/** Replies the bounds of the cell.
	 *
	 * @return the cell.
	 */
	@Pure
	public Rectangle2d getBounds() {
		return this.bounds;
	}

	@Override
	@Pure
	public Iterator<P> iterator() {
		return new DereferenceIterator<>(this.elements.iterator());
	}

	/**
	 * Replies the iterator on the elements of the cell that
	 * are intersecting the specified bounds.
	 *
	 * @param bounds the bounds
	 * @return the iterator.
	 */
	@Pure
	public Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return new BoundsIterator<>(this.elements.iterator(), bounds);
	}

	/** Replies the grid cell elements in this cell.
	 *
	 * @return the grid cell elements in this cell.
	 */
	@Pure
	public Iterator<GridCellElement<P>> getGridCellElements() {
		return this.elements.iterator();
	}

	/** Add an element in the cell.
	 *
	 * @param element the element.
	 * @return <code>true</code> if the element is added;
	 *     otherwise <code>false</code>.
	 */
	public boolean addElement(GridCellElement<P> element) {
		if (element != null && this.elements.add(element)) {
			if (element.addCellLink(this)) {
				++this.referenceElementCount;
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("unlikely-arg-type")
	private GridCellElement<P> remove(P element) {
		final SortedSet<GridCellElement<P>> elt = this.elements.tailSet(new GridCellElement<>(element));
		assert elt != null;
		final Iterator<GridCellElement<P>> iterator = elt.iterator();
		while (iterator.hasNext()) {
			final GridCellElement<P> e = iterator.next();
			if (e.equals(element)) {
				iterator.remove();
				return e;
			}
		}
		return null;
	}

	/** Remove the specified element from the cell.
	 *
	 * @param element the element.
	 * @return the removed grid-cell element.
	 */
	public GridCellElement<P> removeElement(P element) {
		final GridCellElement<P> elt = remove(element);
		if (elt != null) {
			if (elt.removeCellLink(this)) {
				--this.referenceElementCount;
			}
		}
		return elt;
	}

	/**
	 * This class describes an iterator on node bounds.
	 *
	 * @param <P> is the type of the user data inside the node.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 * @see GISPrimitive
	 */
	private static class BoundsIterator<P extends GISPrimitive> implements Iterator<P> {

		private final Iterator<GridCellElement<P>> iterator;

		private final Rectangle2afp<?, ?, ?, ?, ?, ?> bounds;

		private P next;

		private boolean searched;

		/** Constructor.
		 *
		 * @param iterator the iterator.
		 * @param bounds the bounds.
		 */
		BoundsIterator(Iterator<GridCellElement<P>> iterator, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
			this.iterator = iterator;
			this.bounds = bounds;
		}

		private void searchNext() {
			this.next = null;
			this.searched = true;
			while (this.next == null && this.iterator.hasNext()) {
				final GridCellElement<P> element = this.iterator.next();
				if (element != null) {
					final P e = element.get();
					if (e != null && e.getGeoLocation().toBounds2D().intersects(this.bounds)) {
						this.next = e;
					}
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (!this.searched) {
				searchNext();
			}
			return this.next != null;
		}

		@Override
		public P next() {
			if (!this.searched) {
				searchNext();
			}
			final P element = this.next;
			if (element == null) {
				throw new NoSuchElementException();
			}
			this.searched = false;
			return element;
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

	} /* class BoundsIterator */

	/** Internal iterator.
	 * @param <P> is the type of the user data inside the node.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 * @see GISPrimitive
	 */
	private static class DereferenceIterator<P extends GISPrimitive> implements Iterator<P> {

		private final Iterator<GridCellElement<P>> iterator;

		private P next;

		private boolean searched;

		/** Constructor.
		 * @param iterator the iterator.
		 */
		DereferenceIterator(Iterator<GridCellElement<P>> iterator) {
			this.iterator = iterator;
		}

		private void searchNext() {
			this.next = null;
			this.searched = true;
			while (this.next == null && this.iterator.hasNext()) {
				final GridCellElement<P> element = this.iterator.next();
				if (element != null) {
					final P e = element.get();
					if (e != null) {
						this.next = e;
					}
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (!this.searched) {
				searchNext();
			}
			return this.next != null;
		}

		@Override
		public P next() {
			if (!this.searched) {
				searchNext();
			}
			final P element = this.next;
			if (element == null) {
				throw new NoSuchElementException();
			}
			this.searched = false;
			return element;
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

	} /* class DereferenceIterator */

}

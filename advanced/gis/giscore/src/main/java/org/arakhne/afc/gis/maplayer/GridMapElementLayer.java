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

package org.arakhne.afc.gis.maplayer;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.grid.MapElementGridSet;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

/**
 * This class represents a layer that contains map elements
 * stored in a grid data structure.
 * The minimum size of the cells is defined by {@link #MINIMUM_CELL_SIZE}.
 * The preferred number of cells per row or per column is given
 * by {@link #MAXIMUM_CELL_COUNT}.
 *
 * @param <E> is the type of the elements inside this layer.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see ArrayMapElementLayer
 * @see TreeMapElementLayer
 */
public class GridMapElementLayer<E extends MapElement> extends MapElementLayer<E> {

	/** The minimum size of the cells (in meters).
	 */
	public static final float MINIMUM_CELL_SIZE = 100f;

	/** The maximum number of cells per row or per column.
	 */
	public static final int MAXIMUM_CELL_COUNT = 100;

	private static final long serialVersionUID = -2038847862529467386L;

	private MapElementGridSet<E> mapElements;

	/** Create a new layer with the specified attribute source.
	 *
	 * @param bounds are the bounds covered by the layer
	 */
	public GridMapElementLayer(Rectangle2d bounds) {
		this(null, null, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param bounds are the bounds covered by the layer
	 */
	public GridMapElementLayer(AttributeCollection attributeSource, Rectangle2d bounds) {
		this(null, attributeSource, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds covered by the layer
	 */
	public GridMapElementLayer(boolean isTemp, Rectangle2d bounds) {
		this(null, null, isTemp, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds covered by the layer
	 */
	public GridMapElementLayer(AttributeCollection attributeSource, boolean isTemp, Rectangle2d bounds) {
		this(null, attributeSource, isTemp, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param bounds are the bounds covered by the layer
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public GridMapElementLayer(UUID id, Rectangle2d bounds) {
		super(id, null);
		this.mapElements = new MapElementGridSet<>(100, 100, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param bounds are the bounds covered by the layer
	 */
	public GridMapElementLayer(UUID id, AttributeCollection attributeSource, Rectangle2d bounds) {
		super(id, attributeSource);
		this.mapElements = new MapElementGridSet<>(
				computeNumber(bounds.getHeight()),
				computeNumber(bounds.getWidth()),
				bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds covered by the layer
	 */
	public GridMapElementLayer(UUID id, boolean isTemp, Rectangle2d bounds) {
		super(id, null, isTemp);
		this.mapElements = new MapElementGridSet<>(
				computeNumber(bounds.getHeight()),
				computeNumber(bounds.getWidth()),
				bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds covered by the layer
	 */
	public GridMapElementLayer(UUID id, AttributeCollection attributeSource, boolean isTemp, Rectangle2d bounds) {
		super(id, attributeSource, isTemp);
		this.mapElements = new MapElementGridSet<>(
				computeNumber(bounds.getHeight()),
				computeNumber(bounds.getWidth()),
				bounds);
	}

	private static int computeNumber(double size) {
		final double s = size / MAXIMUM_CELL_COUNT;
		if (s < MINIMUM_CELL_SIZE) {
			return (int) Math.ceil(s / MINIMUM_CELL_SIZE);
		}
		return MAXIMUM_CELL_COUNT;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public GridMapElementLayer<E> clone() {
		final GridMapElementLayer<E> layer = (GridMapElementLayer<E>) super.clone();
		layer.mapElements = new MapElementGridSet<>(
				this.mapElements.getRowCount(),
				this.mapElements.getColumnCount(),
				this.mapElements.getBounds());
		E cloneElt;
		for (final E elt : this.mapElements) {
			cloneElt = (E) elt.clone();
			layer.addMapElement(cloneElt);
		}
		resetBoundingBox();
		return layer;
	}

	/** Replies the number of rows in the grid.
	 *
	 * @return the number of rows in the grid.
	 */
	@Pure
	public int getRowCount() {
		return this.mapElements.getRowCount();
	}

	/** Replies the number of columns in the grid.
	 *
	 * @return the number of columns in the grid.
	 */
	@Pure
	public int getColumnCount() {
		return this.mapElements.getColumnCount();
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		Rectangle2d subBounds;
		for (final E mapelement : this.mapElements) {
			subBounds = mapelement.getBoundingBox();
			if (subBounds != null && !subBounds.isEmpty()) {
				if (first) {
					first = false;
					r.set(subBounds);
				} else {
					r.setUnion(subBounds);
				}
			}
		}
		return first ? null : r;
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	@Pure
	public int size() {
		return this.mapElements.size();
	}

	@Override
	@Pure
	public E getMapElementAt(int index) {
		return this.mapElements.get(index);
	}

	@Override
	@Pure
	public Set<E> getAllMapElements() {
		return Collections.unmodifiableSet(this.mapElements);
	}

	@Override
	public boolean addMapElements(Collection<? extends E> elements) {
		if (this.mapElements.addAll(elements)) {
			for (final E e : elements) {
				e.setContainer(this);
			}
			resetBoundingBox();
			fireLayerContentChangedEvent();
			return true;
		}
		return false;
	}

	@Override
	public boolean addMapElement(E element) {
		if (this.mapElements.add(element)) {
			element.setContainer(this);
			resetBoundingBox();
			fireLayerContentChangedEvent();
			return true;
		}
		return false;
	}

	@Override
	public boolean removeMapElement(MapElement element) {
		if (this.mapElements.remove(element)) {
			element.setContainer(null);
			resetBoundingBox();
			fireLayerContentChangedEvent();
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAllMapElements() {
		if (!this.mapElements.isEmpty()) {
			for (final MapElement e : this.mapElements) {
				e.setContainer(null);
			}
			this.mapElements.clear();
			resetBoundingBox();
			fireLayerContentChangedEvent();
			return true;
		}
		return false;
	}

	@Override
	@Pure
	public Class<? extends E> getElementType() {
		return this.mapElements.getElementType();
	}

	@Override
	@Pure
	public int getMapElementCount() {
		return this.mapElements.size();
	}

	/** Iterates on the elements that intersect the specified bounds.
	 *
	 * @param bounds is the rectangle inside which the replied elements must be located
	 */
	@Override
	@Pure
	public Iterator<E> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return new BoundedElementIterator<>(bounds, iterator());
	}

	@Override
	@Pure
	public Iterator<E> iterator() {
		return new IteratorWrapper(this.mapElements.iterator());
	}

	/**
	 * Iterator on map elements inside a rectangle.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 * @see MapElementLayer
	 */
	private static class BoundedElementIterator<E extends MapElement> implements Iterator<E> {

		private final Rectangle2afp<?, ?, ?, ?, ?, ?> bounds;

		private final Iterator<E> iterator;

		private E next;

		/** Constructor.
		 * @param bounds the clipping bounds.
		 * @param iIterator the original iterator.
		 */
		BoundedElementIterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, Iterator<E> iIterator) {
			this.bounds = bounds;
			this.iterator = iIterator;
			this.next = null;
			detectNext();
		}

		private void detectNext() {
			this.next = null;
			while (this.iterator.hasNext()) {
				final E n = this.iterator.next();
				final Rectangle2afp<?, ?, ?, ?, ?, ?> nBounds = n.getBoundingBox();
				if (nBounds != null && nBounds.intersects(this.bounds)) {
					this.next = n;
					return;
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public E next() {
			final E toReply = this.next;
			detectNext();
			return toReply;
		}

	} /* class BoundedElementIterator */

	/** Internal iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class IteratorWrapper implements Iterator<E> {

		private final Iterator<E> iterator;

		private E lastReplied;

		/** Constructor.
		 * @param iterator the iterator.
		 */
		IteratorWrapper(Iterator<E> iterator) {
			this.iterator = iterator;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public E next() {
			this.lastReplied = this.iterator.next();
			return this.lastReplied;
		}

		@Override
		public void remove() {
			final E removed = this.lastReplied;
			this.lastReplied = null;
			this.iterator.remove();
			if (removed == null) {
				throw new NoSuchElementException();
			}
			removed.setContainer(null);
			resetBoundingBox();
			fireLayerContentChangedEvent();
		}

	} /* class IteratorWrapper */

}

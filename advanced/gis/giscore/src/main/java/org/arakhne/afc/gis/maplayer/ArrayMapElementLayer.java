/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.util.InformedArrayList;

/**
 * This class represents a layer that contains map elements
 * stored in an array data structure.
 *
 * @param <E> is the type of the elements inside this layer.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see TreeMapElementLayer
 * @see GridMapElementLayer
 */
public class ArrayMapElementLayer<E extends MapElement> extends MapElementLayer<E> {

	private static final long serialVersionUID = -1211690940331272178L;

	private InformedArrayList<E> mapElements = new InformedArrayList<>();

	/** Create a new layer.
	 */
	public ArrayMapElementLayer() {
		this(null, null);
	}

	/** Create a new layer with the specified element type.
	 *
	 * @param type the type of the map elements.
	 */
	public ArrayMapElementLayer(Class<? extends E> type) {
		this(null, null);
		this.mapElements.setElementType(type);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 */
	public ArrayMapElementLayer(AttributeCollection attributeSource) {
		this(null, attributeSource);
	}

	/** Create a new layer with the specified attribute source.
	 * @param isTemp indicates if this layer is temporary.
	 */
	public ArrayMapElementLayer(boolean isTemp) {
		this(null, null, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 */
	public ArrayMapElementLayer(AttributeCollection attributeSource, boolean isTemp) {
		this(null, attributeSource, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @since 4.0
	 */
	public ArrayMapElementLayer(UUID id) {
		super(id, null);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @since 4.0
	 */
	public ArrayMapElementLayer(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param isTemp indicates if this layer is temporary.
	 * @since 4.0
	 */
	public ArrayMapElementLayer(UUID id, boolean isTemp) {
		super(id, null, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @since 4.0
	 */
	public ArrayMapElementLayer(UUID id, AttributeCollection attributeSource, boolean isTemp) {
		super(id, attributeSource, isTemp);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public ArrayMapElementLayer<E> clone() {
		final ArrayMapElementLayer<E> layer = (ArrayMapElementLayer<E>) super.clone();
		layer.mapElements = new InformedArrayList<>();
		E cloneElt;
		for (final E elt : this.mapElements) {
			cloneElt = (E) elt.clone();
			layer.addMapElement(cloneElt);
		}
		resetBoundingBox();
		return layer;
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		Rectangle2d subBounds;
		for (final  E mapelement : this.mapElements) {
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
	public List<E> getAllMapElements() {
		return Collections.unmodifiableList(this.mapElements);
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
		return new ElementIterator();
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

	/**
	 * Iterator on map elements.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 * @see MapElementLayer
	 */
	private class ElementIterator implements Iterator<E> {

		private int index;

		ElementIterator() {
			this.index = 0;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.index < ArrayMapElementLayer.this.size();
		}

		@Override
		public E next() {
			if (this.index < 0 || this.index >= ArrayMapElementLayer.this.size()) {
				throw new NoSuchElementException();
			}

			final E toReply = ArrayMapElementLayer.this.getMapElementAt(this.index);
			if (toReply == null) {
				throw new ConcurrentModificationException();
			}

			++this.index;

			return toReply;
		}

		@Override
		public void remove() {
			try {
				final E elt = ArrayMapElementLayer.this.getMapElementAt(this.index - 1);
				removeMapElement(elt);
				--this.index;
			} catch (IndexOutOfBoundsException exception) {
				throw new NoSuchElementException();
			}
		}

	} /* class ElementIterator */

}

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

package org.arakhne.afc.gis.maplayer;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.tree.MapElementTreeSet;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

/**
 * This class represents a layer that contains map elements
 * stored inside a tree data-structure.
 *
 * @param <E> is the type of the elements inside this layer.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see ArrayMapElementLayer
 * @see GridMapElementLayer
 */
public class TreeMapElementLayer<E extends MapElement> extends MapElementLayer<E> {

	private static final long serialVersionUID = -2435314165368116725L;

	private MapElementTreeSet<E> mapElements;

	/** Create a new layer with the specified attribute source.
	 */
	public TreeMapElementLayer() {
		this(null, (AttributeCollection) null);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 */
	public TreeMapElementLayer(AttributeCollection attributeSource) {
		this(null, attributeSource);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param isTemp indicates if this layer is temporary.
	 */
	public TreeMapElementLayer(boolean isTemp) {
		this(null, null, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 */
	public TreeMapElementLayer(AttributeCollection attributeSource, boolean isTemp) {
		this(null, attributeSource, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 */
	public TreeMapElementLayer(UUID id) {
		super(id, null);
		this.mapElements = new MapElementTreeSet<>();
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 */
	public TreeMapElementLayer(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
		this.mapElements = new MapElementTreeSet<>();
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param isTemp indicates if this layer is temporary.
	 */
	public TreeMapElementLayer(UUID id, boolean isTemp) {
		super(id, null, isTemp);
		this.mapElements = new MapElementTreeSet<>();
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 */
	public TreeMapElementLayer(UUID id, AttributeCollection attributeSource, boolean isTemp) {
		super(id, attributeSource, isTemp);
		this.mapElements = new MapElementTreeSet<>();
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		this(null, null, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(AttributeCollection attributeSource, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		this(null, attributeSource, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(boolean isTemp, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		this(null, null, isTemp, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(AttributeCollection attributeSource, boolean isTemp, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		this(null, attributeSource, isTemp, bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(UUID id, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(id, null);
		this.mapElements = new MapElementTreeSet<>(bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(UUID id, AttributeCollection attributeSource, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(id, attributeSource);
		this.mapElements = new MapElementTreeSet<>(bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(UUID id, boolean isTemp, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(id, null, isTemp);
		this.mapElements = new MapElementTreeSet<>(bounds);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @param bounds are the bounds of the world used to ease the tree building.
	 */
	public TreeMapElementLayer(UUID id, AttributeCollection attributeSource, boolean isTemp,
			Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(id, attributeSource, isTemp);
		this.mapElements = new MapElementTreeSet<>(bounds);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public TreeMapElementLayer<E> clone() {
		final TreeMapElementLayer<E> layer = (TreeMapElementLayer<E>) super.clone();
		final Rectangle2d b = getBoundingBox();
		if (b == null || b.isEmpty()) {
			layer.mapElements = new MapElementTreeSet<>();
		} else {
			layer.mapElements = new MapElementTreeSet<>(b);
		}
		E cloneElt;
		for (final E elt : this.mapElements) {
			cloneElt = (E) elt.clone();
			layer.addMapElement(cloneElt);
		}
		resetBoundingBox();
		return layer;
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

	@Override
	@Pure
	public Iterator<E> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return new IteratorWrapper(this.mapElements.iterator(bounds));
	}

	@Override
	@Pure
	public Iterator<E> iterator() {
		return new IteratorWrapper(this.mapElements.iterator());
	}

	/** Internal iterator.
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
		 * @param iterator the original iterator.
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

	}

}

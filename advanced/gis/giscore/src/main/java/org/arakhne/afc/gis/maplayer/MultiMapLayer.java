/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.maplayer.MapLayerHierarchyEvent.Type;
import org.arakhne.afc.gis.primitive.ChangeListener;
import org.arakhne.afc.gis.primitive.GISTreeBrowsable;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.util.InformedArrayList;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class is a container of layers.
 *
 * <p>The order of the layers in this container is important.
 * It is assumed to be from the bottom-most layer
 * (at the end of the layer collection)
 * to the top-most layer (at index 0).
 * Iterator on this container must go from top to bottom.
 *
 * @param <L> is the type of the layers in this container.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MultiMapLayer<L extends MapLayer> extends MapLayer implements GISTreeBrowsable<L>, GISLayerContainer<L> {

	private static final long serialVersionUID = 2125894425580476243L;

	/** Visible bounds of this element.
	 */
	private Rectangle2d visibleBounds;

	/** List of contained sub-layers.
	 */
	private InformedArrayList<L> subLayers = new InformedArrayList<>();

	private transient volatile ChangeListener listener;

	/** Create a new layer with the specified attribute source.
	 */
	public MultiMapLayer() {
		this(null, null);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 */
	public MultiMapLayer(AttributeCollection attributeSource) {
		this(null, attributeSource);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param isTemp indicates if this layer is temporary.
	 */
	public MultiMapLayer(boolean isTemp) {
		this(null, null, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 */
	public MultiMapLayer(AttributeCollection attributeSource, boolean isTemp) {
		this(null, attributeSource, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @since 4.0
	 */
	public MultiMapLayer(UUID id) {
		super(id, null);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @since 4.0
	 */
	public MultiMapLayer(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param isTemp indicates if this layer is temporary.
	 * @since 4.0
	 */
	public MultiMapLayer(UUID id, boolean isTemp) {
		super(id, null, isTemp);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @since 4.0
	 */
	public MultiMapLayer(UUID id, AttributeCollection attributeSource, boolean isTemp) {
		super(id, attributeSource, isTemp);
	}

	@Override
	public void bindChangeListener(ChangeListener listener) {
		this.listener = listener;
	}

	private void fireChangeListener() {
		if (this.listener != null) {
			this.listener.changed(this);
		}
	}

	@Override
	public void fireLayerContentChangedEvent(MapLayerContentEvent event) {
		super.fireLayerContentChangedEvent(event);
		fireChangeListener();
	}

	@Override
	public void fireLayerHierarchyChangedEvent(MapLayerHierarchyEvent event) {
		super.fireLayerHierarchyChangedEvent(event);
		fireChangeListener();
	}

	@Override
	public void fireElementChanged() {
		super.fireElementChanged();
		fireChangeListener();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public MultiMapLayer<L> clone() {
		final MultiMapLayer<L> clone = (MultiMapLayer<L>) super.clone();

		clone.subLayers = new InformedArrayList<>();

		for (final L subLayer : this.subLayers) {
			clone.subLayers.add((L) subLayer.clone());
		}

		resetBoundingBox();

		return clone;
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("layers", getAllMapLayers()); //$NON-NLS-1$
	}

	@Override
	public Class<? extends L> getElementType() {
		return this.subLayers.getElementType();
	}

	@Override
	protected Rectangle2d calcBounds() {
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		for (final  MapLayer sublayer : this.subLayers) {
			final Rectangle2d subBounds = sublayer.getBoundingBox();
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
		return getVisibleBoundingBox();
	}

	/** Compute the bounds of this visible elements.
	 * This function does not update the internal
	 * attribute replied by {@link #getVisibleBoundingBox()}
	 *
	 * @return the visible bounds or <code>null</code> if no layer.
	 */
	protected Rectangle2d calcVisibleBounds() {
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		Rectangle2d subBounds;
		for (final MapLayer sublayer : this.subLayers) {
			if (sublayer.isVisible()) {
				if (sublayer instanceof GISLayerContainer<?>)  {
					subBounds = ((GISLayerContainer<?>) sublayer).getVisibleBoundingBox();
				} else {
					subBounds = sublayer.getBoundingBox();
				}
				if (subBounds != null && !subBounds.isEmpty()) {
					if (first) {
						first = false;
						r.set(subBounds);
					} else {
						r.setUnion(subBounds);
					}
				}
			}
		}
		return first ? null : r;
	}

	@Override
	public void fireLayerAttributeChangedEvent(MapLayerAttributeChangeEvent event) {
		if (ATTR_VISIBLE.equalsIgnoreCase(event.getName())) {
			this.visibleBounds = null;
		}
		super.fireLayerAttributeChangedEvent(event);
	}

	@Override
	@Pure
	public Rectangle2d getVisibleBoundingBox() {
		if (this.visibleBounds == null) {
			this.visibleBounds = calcVisibleBounds();
		}
		return this.visibleBounds;
	}

	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		this.visibleBounds = null;
	}

	@Override
	@Pure
	public int size() {
		return this.subLayers.size();
	}

	@Override
	@Pure
	public int getMapLayerCount() {
		return size();
	}

	@Override
	public boolean addMapLayer(L layer) {
		this.subLayers.add(0, layer);
		layer.setContainer(this);
		resetBoundingBox();
		fireLayerAddedEvent(layer, 0);
		return true;
	}

	@Override
	public void addMapLayer(int index, L layer) {
		this.subLayers.add(index, layer);
		layer.setContainer(this);
		resetBoundingBox();
		fireLayerAddedEvent(layer, index);
	}

	/** Put the given layer at the given index but
	 * do not fire any event.
	 *
	 * <p>This function does not test if the layer
	 * is already present in the list of layers.
	 *
	 * @param index index.
	 * @param layer layer.
	 * @throws IndexOutOfBoundsException if the index
	 *     is invalid.
	 */
	protected void setMapLayerAt(int index, L layer) {
		this.subLayers.set(index, layer);
		layer.setContainer(this);
		resetBoundingBox();
	}

	@Override
	public boolean removeMapLayer(MapLayer layer) {
		final int index = this.subLayers.indexOf(layer);
		if (index >= 0 && index < this.subLayers.size()) {
			this.subLayers.remove(index);
			layer.setContainer(null);
			resetBoundingBox();
			fireLayerRemovedEvent(layer, index);
			return true;
		}
		return false;
	}

	@Override
	public L removeMapLayerAt(int index) {
		final L removed = this.subLayers.remove(index);
		if (removed != null) {
			removed.setContainer(null);
			resetBoundingBox();
			fireLayerRemovedEvent(removed, index);
		}
		return removed;
	}

	@Override
	public boolean moveLayerUp(MapLayer layer) {
		return moveLayerUp(this.subLayers.indexOf(layer));
	}

	@Override
	public boolean moveLayerUp(int index) {
		if (index > 0 && index < this.subLayers.size()) {
			final L layer1 = this.subLayers.get(index);
			final L layer2 = this.subLayers.get(index - 1);
			this.subLayers.set(index, layer2);
			this.subLayers.set(index - 1, layer1);
			fireLayerMovedUpEvent(layer1, index - 1);
			return true;
		}
		return false;
	}

	@Override
	public boolean moveLayerDown(MapLayer layer) {
		return moveLayerDown(this.subLayers.indexOf(layer));
	}

	@Override
	public boolean moveLayerDown(int index) {
		if (index >= 0 && index < (this.subLayers.size() - 1)) {
			final L layer1 = this.subLayers.get(index);
			final L layer2 = this.subLayers.get(index + 1);
			this.subLayers.set(index, layer2);
			this.subLayers.set(index + 1, layer1);
			fireLayerMovedDownEvent(layer1, index + 1);
			return true;
		}
		return false;
	}

	@Override
	@Pure
	public final L getMapLayerAt(int index) {
		return getChildAt(index);
	}

	@Override
	@Pure
	public List<L> getAllMapLayers() {
		return Collections.unmodifiableList(this.subLayers);
	}

	@Override
	public void clear() {
		final InformedArrayList<L> oldSubLayers = this.subLayers;
		assert oldSubLayers != null;
		this.subLayers = new InformedArrayList<>();
		for (final L sublayer : oldSubLayers) {
			sublayer.setContainer(null);
		}
		resetBoundingBox();
		fireLayerRemoveAllEvent(oldSubLayers);
	}

	@Override
	@Pure
	public boolean isBottomLayer(MapLayer layer) {
		if (this.subLayers.isEmpty()) {
			return false;
		}
		return this.subLayers.get(this.subLayers.size() - 1) == layer;
	}

	@Override
	@Pure
	public boolean isTopLayer(MapLayer layer) {
		if (this.subLayers.isEmpty()) {
			return false;
		}
		return this.subLayers.get(0) == layer;
	}

	@Override
	@Pure
	public final Iterator<L> iterator() {
		return getTopDownIterator();
	}

	@Override
	@Pure
	public final Iterator<L> reverseIterator() {
		return getBottomUpIterator();
	}

	@Override
	@Pure
	public Iterator<L> getBottomUpIterator() {
		return new BottomUpLayerIterator();
	}

	@Override
	@Pure
	public Iterator<L> getTopDownIterator() {
		return new TopDownLayerIterator();
	}

	@Override
	public void onAttributeChangeEvent(AttributeChangeEvent event) {
		if (ATTR_VISIBLE.equals(event.getName())) {
			final AttributeValue aValue = event.getValue();
			final boolean isSpecial = aValue instanceof SpecialAttributeValue;
			if (!isSpecial) {
				boolean cvalue;
				try {
					cvalue = aValue.getBoolean();
				} catch (Exception exception) {
					cvalue = isVisible();
				}
				for (final L sublayer : this.subLayers) {
					sublayer.setVisible(cvalue);
				}
			}
		}
		super.onAttributeChangeEvent(event);
	}

	/** Fire the event that indicates a layer was added.
	 *
	 * @param layer is the added layer.
	 * @param index is the insertion index.
	 */
	protected void fireLayerAddedEvent(MapLayer layer, int index) {
		fireLayerHierarchyChangedEvent(new MapLayerHierarchyEvent(this, layer, Type.ADD_CHILD, index, layer.isTemporaryLayer()));
	}

	/** Fire the event that indicates a layer was removed.
	 *
	 * @param layer is the removed layer.
	 * @param oldChildIndex the old child index.
	 */
	protected void fireLayerRemovedEvent(MapLayer layer, int oldChildIndex) {
		fireLayerHierarchyChangedEvent(new MapLayerHierarchyEvent(
				this, layer, Type.REMOVE_CHILD, oldChildIndex, layer.isTemporaryLayer()));
	}

	/** Fire the event that indicates a layer was moved up.
	 *
	 * @param layer is the moved layer.
	 * @param newIndex is the new index of the layer.
	 */
	protected void fireLayerMovedUpEvent(MapLayer layer, int newIndex) {
		fireLayerHierarchyChangedEvent(new MapLayerHierarchyEvent(this, layer,
				Type.MOVE_CHILD_UP, newIndex, layer.isTemporaryLayer()));
	}

	/** Fire the event that indicates a layer was moved down.
	 *
	 * @param layer is the moved layer.
	 * @param newIndex is the new index of the layer.
	 */
	protected void fireLayerMovedDownEvent(MapLayer layer, int newIndex) {
		fireLayerHierarchyChangedEvent(new MapLayerHierarchyEvent(this, layer,
				Type.MOVE_CHILD_DOWN, newIndex, layer.isTemporaryLayer()));
	}

	/** Fire the event that indicates all the layers were removed.
	 * @param removedObjects are the removed objects.
	 */
	protected void fireLayerRemoveAllEvent(List<? extends L> removedObjects) {
		fireLayerHierarchyChangedEvent(
				new MapLayerHierarchyEvent(
						this,
						removedObjects,
						Type.REMOVE_ALL_CHILDREN,
						-1,
						isTemporaryLayer()));
	}

	@Override
	@Pure
	public GISTreeBrowsable<?> getParent() {
		final GISLayerContainer<?> c = getContainer();
		if (c instanceof GISTreeBrowsable<?>) {
			return (GISTreeBrowsable<?>) c;
		}
		return null;
	}

	@Override
	@Pure
	public final int getChildCount() {
		return size();
	}

	@Override
	@Pure
	public L getChildAt(int childIndex) {
		return this.subLayers.get(childIndex);
	}

	@Override
	@Pure
	public final int indexOf(MapLayer layer) {
		return this.subLayers.indexOf(layer);
	}

	/**
	 * Iterator on sublayers from top to down.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class TopDownLayerIterator implements Iterator<L> {

		private int index;

		TopDownLayerIterator() {
			this.index = 0;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.index < MultiMapLayer.this.size();
		}

		@Override
		public L next() {
			if (this.index < 0 || this.index >= MultiMapLayer.this.size()) {
				throw new NoSuchElementException();
			}

			final L toReply = MultiMapLayer.this.getMapLayerAt(this.index);
			if (toReply == null) {
				throw new ConcurrentModificationException();
			}

			++this.index;

			return toReply;
		}

		@Override
		public void remove() {
			try {
				MultiMapLayer.this.removeMapLayerAt(this.index - 1);
				--this.index;
			} catch (IndexOutOfBoundsException exception) {
				throw new NoSuchElementException();
			}
		}

	} /* class TopDownLayerIterator */

	/**
	 * Iterator on sublayers from bottom to up.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class BottomUpLayerIterator implements Iterator<L> {

		private int index;

		BottomUpLayerIterator() {
			this.index = MultiMapLayer.this.size() - 1;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.index < MultiMapLayer.this.size() && this.index >= 0;
		}

		@Override
		public L next() {
			if (this.index < 0 || this.index >= MultiMapLayer.this.size()) {
				throw new NoSuchElementException();
			}

			final L toReply = MultiMapLayer.this.getMapLayerAt(this.index);
			if (toReply == null) {
				throw new ConcurrentModificationException();
			}

			--this.index;

			return toReply;
		}

		@Override
		public void remove() {
			try {
				MultiMapLayer.this.removeMapLayerAt(this.index + 1);
				++this.index;
			} catch (IndexOutOfBoundsException exception) {
				throw new NoSuchElementException();
			}
		}

	} /* class BottomUpLayerIterator */

}

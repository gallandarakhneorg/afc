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

import java.util.Iterator;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISContainer;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Container of layers for a GIS application.
 *
 * <p>The order of the layers in this container is important.
 * It is assumed to be from the bottom-most layer
 * (at the end of the layer collection)
 * to the top-most layer (at index 0).
 * Iterator on this container must go from top to bottom.
 *
 * @param <L> is the type of the layers inside this container.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISLayerContainer<L extends MapLayer> extends GISContainer<L> {

	/** Replies the bounding box of this visible elements.
	 *
	 * @return the bounding box or {@code null} if not applicable.
	 */
	@Pure
	Rectangle2d getVisibleBoundingBox();

	/** Remove all the children.
	 */
	void clear();

	/** Replies the count of map layers  inside this container.
	 *
	 * @return the count of map layers
	 */
	@Pure
	int getMapLayerCount();

	/** Replies the map layer at the specified index.
	 *
	 * @param index the index.
	 * @return the map layer at the given index
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	L getMapLayerAt(int index);

	/** Replies the map layers.
	 *
	 * @return the map layers
	 */
	@Pure
	List<L> getAllMapLayers();

	/** Add a map layer inside this container at the top-most position.
	 *
	 * @param layer the layer.
	 * @return {@code true} if the layer was added successfully,
	 *     otherwise {@code false}
	 */
	boolean addMapLayer(L layer);

	/** Insert a map layer inside this container at the specified index.
	 *
	 * @param index the layer position.
	 * @param layer the layer.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	void addMapLayer(int index, L layer);

	/** Remove a map layer from this container.
	 *
	 * @param layer the layer.
	 * @return {@code true} if the layer was removed successfully,
	 *     otherwise {@code false}
	 */
	boolean removeMapLayer(MapLayer layer);

	/** Remove the map layer inside this container.
	 *
	 * @param index the index.
	 * @return the removed map layer
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	L removeMapLayerAt(int index);

	/** Move the specified layer up.
	 *
	 * <p>The layer position is important for
	 * drawing and event handling. Indeed
	 * the layers will be drawn from the
	 * index replied by {@link #getMapLayerCount()}
	 * minus 1 (last) to the index {@code 0} (first).
	 * Moreover the events will be forwarded to the
	 * layers from the first to the last.
	 *
	 * @param layer the layer.
	 * @return {@code true} if the layer
	 *     was moved. otherwise {@code false}
	 */
	boolean moveLayerUp(MapLayer layer);

	/** Move the specified layer up.
	 *
	 * <p>The layer position is important for
	 * drawing and event handling. Indeed
	 * the layers will be drawn from the
	 * index replied by {@link #getMapLayerCount()}
	 * minus 1 (last) to the index {@code 0} (first).
	 * Moreover the events will be forwarded to the
	 * layers from the first to the last.
	 *
	 * @param index the index.
	 * @return {@code true} if the layer
	 *     was moved. otherwise {@code false}
	 */
	boolean moveLayerUp(int index);

	/** Move the specified layer up.
	 *
	 * <p>The layer position is important for
	 * drawing and event handling. Indeed
	 * the layers will be drawn from the
	 * index replied by {@link #getMapLayerCount()}
	 * minus 1 (last) to the index {@code 0} (first).
	 * Moreover the events will be forwarded to the
	 * layers from the first to the last.
	 *
	 * @param layer the layer.
	 * @return {@code true} if the layer
	 *     was moved. otherwise {@code false}
	 */
	boolean moveLayerDown(MapLayer layer);

	/** Move the specified layer up.
	 *
	 * <p>The layer position is important for
	 * drawing and event handling. Indeed
	 * the layers will be drawn from the
	 * index replied by {@link #getMapLayerCount()}
	 * minus 1 (last) to the index {@code 0} (first).
	 * Moreover the events will be forwarded to the
	 * layers from the first to the last.
	 *
	 * @param index the index.
	 * @return {@code true} if the layer
	 *     was moved. otherwise {@code false}
	 */
	boolean moveLayerDown(int index);

	/** Add a listener on the layer's events.
	 *
	 * @param listener the listener.
	 */
	void addLayerListener(MapLayerListener listener);

	/** Remove a listener on the layer's events.
	 *
	 * @param listener the listener.
	 */
	void removeLayerListener(MapLayerListener listener);

	/** Replies a reverse iterator (from bottom to top).
	 *
	 * @return a reverse iterator on layers.
	 * @see #iterator()
	 * @see #getBottomUpIterator()
	 * @see #getTopDownIterator()
	 */
	@Pure
	Iterator<L> reverseIterator();

	/** Replies a bottom-up iterator.
	 *
	 * @return a bottom-up iterator on layers.
	 * @see #iterator()
	 * @see #reverseIterator()
	 * @see #getTopDownIterator()
	 */
	@Pure
	Iterator<L> getBottomUpIterator();

	/** Replies a top-down iterator.
	 *
	 * @return a top-down iterator on layers.
	 * @see #iterator()
	 * @see #reverseIterator()
	 * @see #getBottomUpIterator()
	 */
	@Pure
	Iterator<L> getTopDownIterator();

	/** Replies if the specified layer is the bottom-most inside this container.
	 *
	 * @param layer the layer.
	 * @return {@code true} if the given layer is the bottom-most layer in this container,
	 *     otherwise {@code false}
	 */
	@Pure
	boolean isBottomLayer(MapLayer layer);

	/** Replies if the specified layer is the top-most inside this container.
	 *
	 * @param layer the layer.
	 * @return {@code true} if the given layer is the top-most layer in this container,
	 *     otherwise {@code false}
	 */
	@Pure
	boolean isTopLayer(MapLayer layer);

	/** Replies the index of the given layer in this container.
	 *
	 * @param layer the layer.
	 * @return the index of the layer or {@code -1} if not found
	 */
	@Pure
	int indexOf(MapLayer layer);

	/** Fire the event that indicates the hierarchy of layers has changed.
	 *
	 * @param event the event.
	 */
	void fireLayerHierarchyChangedEvent(MapLayerHierarchyEvent event);

	/** Forward to the parent layer the event that indicates the content of a child layer was changed.
	 *
	 * @param event the event.
	 */
	void fireLayerContentChangedEvent(MapLayerContentEvent event);

	/** Forward to the parent layer the event that indicates the content of a child layer was changed.
	 *
	 * @param event the event.
	 */
	void fireLayerAttributeChangedEvent(MapLayerAttributeChangeEvent event);

}

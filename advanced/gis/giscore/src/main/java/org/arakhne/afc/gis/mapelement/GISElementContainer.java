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

package org.arakhne.afc.gis.mapelement;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.primitive.GISContainer;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;

/** Container of elements for a GIS application.
 *
 * @param <T> is the type of the containing elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISElementContainer<T extends MapElement> extends GISContainer<T> {

	/** Replies the count of map elements.
	 *
	 * @return the count of map elements.
	 */
	int getMapElementCount();

	/** Replies the map element at the specified index.
	 *
	 * @param index the index.
	 * @return the element at the given index
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	T getMapElementAt(int index);

	/** Replies the map elements as an array.
	 *
	 * @return all the elements.
	 */
	Collection<T> getAllMapElements();

	/** Add map elements inside this container.
	 *
	 * @param elements the new elements.
	 * @return {@code true} if at least one element was added,
	 *     otherwise {@code false}
	 */
	boolean addMapElements(Collection<? extends T> elements);

	/** Add a map element inside this container.
	 *
	 * @param element the new element.
	 * @return {@code true} if the element was added successfully,
	 *     otherwise {@code false}
	 */
	boolean addMapElement(T element);

	/** Remove a map element from this container.
	 *
	 * @param element the element to remove.
	 * @return {@code true} if the element was removed successfully,
	 *     otherwise {@code false}
	 */
	boolean removeMapElement(MapElement element);

	/** Remove all the map elements from this container.
	 *
	 * @return {@code true} if container has changed,
	 *     otherwise {@code false}
	 * @since 4.0
	 */
	boolean removeAllMapElements();

	/** Invoked when one of the graphical attributes of this element
	 * has changed, including those that change the bounding box.
	 */
	void onMapElementGraphicalAttributeChanged();

	/** Replies the URL from which the geometry of
	 * the elements in this
	 * container were read.
	 *
	 * <p>Basically, the replied URL may be the URL
	 * of a ESRI Shape file.
	 *
	 * @return the URL of the source of the geometry of
	 *     the elements, or {@code null} if no known source.
	 */
	URL getElementGeometrySourceURL();

	/** Replies the map projection of the data inside
	 * the file for which the URL is replied by
	 * {@link #getElementGeometrySourceURL()}.
	 *
	 * @return the map projection of the data, or {@code null}
	 *     if {@link #getElementGeometrySourceURL()} replies {@code null}.
	 * @since 4.1
	 */
	MapMetricProjection getElementGeometrySourceProjection();

	/** Replies the URL from which the attributes of
	 * the elements in this container were read.
	 *
	 * <p>Basically, the replied URL may be the URL
	 * of a dBase file.
	 *
	 * @return the URL of the source of the attributes of
	 *     the elements, or {@code null} if no known source.
	 */
	URL getElementAttributeSourceURL();

	/** Set the URL from which the attributes of
	 * the elements in this container were read.
	 *
	 * <p>Basically, the URL may be the URL
	 * of a dBase file.
	 *
	 * @param url is the URL of the source of the attributes of
	 *     the elements, or {@code null} if no known source.
	 */
	void setElementAttributeSourceURL(URL url);

	/** Set the URL from which the geometry of
	 * the elements in this
	 * container were read.
	 *
	 * <p>Basically, the URL may be the URL
	 * of a ESRI Shape file.
	 *
	 * @param url is the URL of the source of the geometry of
	 *     the elements, or {@code null} if no known source.
	 * @param mapProjection is the map projection used to describe the
	 *     data inside the file with the given URL.
	 * @since 4.1
	 */
	void setElementGeometrySource(URL url, MapMetricProjection mapProjection);

	/** Iterates on the elements that intersect the specified bounds.
	 *
	 * @param bounds is the rectangle inside which the replied elements must be located
	 * @return an iterator on the map elements.
	 * @since 14.0
	 */
	@Pure
	Iterator<T> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds);

}

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

package org.arakhne.afc.io.shape;

import java.io.IOException;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;

/**
 * This interface describes a service which is creating
 * element instances from shape file data.
 *
 * <p>For an {@code ElementFactory} that creates GIS map elements, you should directly use
 * a {@code GISShapeFileReader}, which embeds the element factory.
 *
 * @param <E> is the type of elements which is red by this reader.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface ElementFactory<E> {

	//---- PRE

	/** Called after the reader was initialized and before the
	 * header of the shape file was red.
	 *
	 * @throws IOException in case of error.
	 */
	default void preReadingStage() throws IOException {
		//
	}

	/** Called after the header of the shape file was red.
	 *
	 * @throws IOException in case of error.
	 */
	default void postHeaderReadingStage() throws IOException {
		//
	}

	//---- CREATION

	/** Create an attribute provider which will be used by a new element.
	 *
	 * @param elementIndex is the index of the element for which an attribute provider must be created.
	 * @return the new attribute provider which will be passed to one of the creation functions.
	 * @see #createMultiPoint(AttributeCollection, int, ESRIPoint[], boolean)
	 * @see #createPoint(AttributeCollection, int, ESRIPoint)
	 * @see #createPolygon(AttributeCollection, int, int[], ESRIPoint[], boolean)
	 * @see #createPolyline(AttributeCollection, int, int[], ESRIPoint[], boolean)
	 */
	default AttributeCollection createAttributeCollection(int elementIndex) {
		return new HeapAttributeCollection();
	}

	/** Create a polyline.
	 *
	 * @param provider is the attribute provider which must be used by the new map element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param parts is the list of the parts, ie the index of the first point in the parts.
	 * @param points is the list of the points.
	 * @param hasZ indicates if the z-coordinates were set.
	 * @return an object representing the creating element, depending of your implementation.
	 *     This value will be passed to {@link #postEntryReadingStage(Object)}.
	 */
	default E createPolyline(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		return null;
	}

	/** Create a polygon.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param parts is the list of the parts, ie the index of the first point in the parts.
	 * @param points is the list of the points.
	 * @param hasZ indicates if the z-coordinates were set.
	 * @return an object representing the creating element, depending of your implementation.
	 *     This value will be passed to {@link #postEntryReadingStage(Object)}.
	 */
	default E createPolygon(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		return null;
	}

	/** Create a multipoint.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param points is the list of the points.
	 * @param hasZ indicates if the z-coordinates were set.
	 * @return an object representing the creating element, depending of your implementation.
	 *     This value will be passed to {@link #postEntryReadingStage(Object)}.
	 */
	default E createMultiPoint(AttributeCollection provider, int shapeIndex, ESRIPoint[] points, boolean hasZ) {
		return null;
	}

	/** Create a point instance.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shape_index is the index of the element in the shape file.
	 * @param point is the location of the point.
	 * @return an object representing the creating point, depending of your implementation.
	 *     This value will be passed to {@link #postEntryReadingStage(Object)}.
	 */
	default E createPoint(AttributeCollection provider, int shape_index, ESRIPoint point) {
		return null;
	}

	/**
	 * Create a multipatch.
	 *
	 * <p>A MultiPatch consists of a number of surface patches. Each surface patch describes a
	 * surface. The surface patches of a MultiPatch are referred to as its parts, and the type of
	 * part controls how the order of vertices of an MultiPatch part is interpreted.
	 *
	 * <p>A single Triangle Strip, or Triangle Fan, represents a single surface patch.
	 *
	 * <p>A sequence of parts that are rings can describe a polygonal surface patch with holes. The
	 * sequence typically consists of an Outer Ring, representing the outer boundary of the
	 * patch, followed by a number of Inner Rings representing holes. When the individual
	 * types of rings in a collection of rings representing a polygonal patch with holes are
	 * unknown, the sequence must start with First Ring, followed by a number of Rings. A
	 * sequence of Rings not preceded by an First Ring is treated as a sequence of Outer Rings
	 * without holes.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param parts is the list of the parts, ie the index of the first point in the parts.
	 * @param partTypes is the list of the types of the parts.
	 * @param points is the list of the points.
	 * @return an object representing the creating multipatch, depending of your implementation.
	 *     This value will be passed to {@link #postShapeReadingStage(Object)}.
	 */
	default E createMultiPatch(AttributeCollection provider, int shapeIndex, int[] parts,
			ShapeMultiPatchType[] partTypes, ESRIPoint[] points) {
		return null;
	}

	/** Invoked to put an attribute in the element.
	 *
	 * @param element is the element in which the attribute should be put.
	 * @param attributeName is the name of the attribute.
	 * @param value is the value of the attribute.
	 */
	default void putAttributeIn(E element, String attributeName, AttributeValue value) {
		//
	}

	//---- POST

	/** Called just after an entry was red but just before the dBase attributes
	 * were red.
	 *
	 * @param element is the value returned by the reading function.
	 * @return {@code true} if the object is assumed to be valid (ie. it will be replies by
	 *     the reading function), otherwise {@code false}.
	 * @throws IOException in case of error.
	 */
	default boolean postEntryReadingStage(E element) throws IOException {
		return true;
	}

	/** Invoked after the element's attributes were red from the dBase source.
	 *
	 * @param element the element.
	 * @return {@code true} if the object is assumed to be valid (ie. it will be replies by
	 *     the reading function), otherwise {@code false}.
	 * @throws IOException in case of error.
	 */
	default boolean postAttributeReadingStage(E element) throws IOException {
		return true;
	}

	/** Invoked after the element's shape was red but before the attributes were retrieved.
	 *
	 * @param element the element.
	 * @return {@code true} if the object is assumed to be valid (ie. it will be replies by
	 *     the reading function), otherwise {@code false}.
	 * @throws IOException in case of error.
	 */
	default boolean postShapeReadingStage(E element) throws IOException {
		return true;
	}

	/** Called after all the entries was red.
	 *
	 * @param success is {@code true} is the reading was successful,
	 *     otherwise {@code false}
	 * @throws IOException in case of error.
	 */
	default void postReadingStage(boolean success) throws IOException {
		//
	}

}

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

package org.arakhne.afc.io.shape;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeProvider;

/**
 * This interface describes functions which are used
 * by a {@link ShapeFileWriter} to export elements
 * inside an ESRI shape file.
 *
 * <p>For an {@code ElementExporter} that exports GIS map elements, you should directly use
 * a {@code GISShapeFileWriter}, which embeds the element exporter.
 *
 * @param <E> is the type of element to write inside the ESRI shape file.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface ElementExporter<E> {

	/** Replies all the attribute containers of the given elements.
	 * The attributes will be put in a DBase file header as columns.
	 *
	 * @param elements are the element from which the attribute containers must be extracted
	 * @return the attribute containers
	 * @throws IOException in case of error.
	 */
	@Pure
	AttributeProvider[] getAttributeProviders(Collection<? extends E> elements) throws IOException;

	/** Replies the attribute container of the given element.
	 * The attributes will be put in a DBase file.
	 *
	 * @param element is the element from which the attribute container must be extracted
	 * @return the attribute container
	 * @throws IOException in case of error.
	 */
	@Pure
	AttributeProvider getAttributeProvider(E element) throws IOException;

	/** Invoked to retrieve the bounds of the world.
	 *
	 * @return the bounds.
	 */
	@Pure
	ESRIBounds getFileBounds();

	/** Replies the count of points inside the given part.
	 * This function is also invoked for a multipoint. In this case,
	 * the group index is always equal to zero.
	 *
	 * @param element is the element from which the point count must be extracted
	 * @param groupIndex is the index of the group from which the point count must be replied.
	 * @return the count of points in the part at the given index.
	 * @throws IOException in case of error.
	 */
	@Pure
	int getPointCountFor(E element, int groupIndex) throws IOException;

	/** Replies the count of groups of points.
	 *
	 * @param element is the element from which the group count must be extracted
	 * @return the count of groups of points.
	 * @throws IOException in case of error.
	 */
	@Pure
	int getGroupCountFor(E element) throws IOException;

	/** Replies the point inside the given part at the given index.
	 *
	 * <p>This function is also invoked for a simple point. In this case,
	 * the group and point indexes are both equal to zero.
	 * This function is also invoked for a multipoint. In this case,
	 * the group index is always equal to zero.
	 *
	 * @param element is the element from which the point must be extracted
	 * @param groupIndex is the index of the group from which the point must be extracted.
	 * @param pointIndex is the index of the point in the group.
	 * @param expectM indicates if the M value is expected.
	 * @param expectZ indicates if the Z value is expected.
	 * @return the point.
	 * @throws IOException in case of error.
	 */
	@Pure
	ESRIPoint getPointAt(E element, int groupIndex, int pointIndex, boolean expectM, boolean expectZ) throws IOException;

	/** Replies the type of the given part for a multipatch element.
	 *
	 * <p>A MultiPatch consists of a number of surface patches. Each surface patch describes a
	 * surface. The surface patches of a MultiPatch are referred to as its parts, and the type of
	 * part controls how the order of vertices of an MultiPatch part is interpreted.
	 *
	 * <p>A single Triangle Strip, or Triangle Fan, represents a single surface patch.
	 *
	 * <p>A sequence of parts that are rings can describe a polygon surface patch with holes. The
	 * sequence typically consists of an Outer Ring, representing the outer boundary of the
	 * patch, followed by a number of Inner Rings representing holes. When the individual
	 * types of rings in a collection of rings representing a polygon patch with holes are
	 * unknown, the sequence must start with First Ring, followed by a number of Rings. A
	 * sequence of Rings not preceded by an First Ring is treated as a sequence of Outer Rings
	 * without holes.
	 *
	 * @param element is the element from which the point count must be extracted
	 * @param groupIndex is the index of the group from which the point count must be replied.
	 * @return the type of the part.
	 * @throws IOException in case of error.
	 */
	@Pure
	ShapeMultiPatchType getGroupTypeFor(E element, int groupIndex) throws IOException;

}

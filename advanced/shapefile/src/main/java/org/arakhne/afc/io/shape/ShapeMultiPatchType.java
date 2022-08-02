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

package org.arakhne.afc.io.shape;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Supported types of multipatches in an ESRI shape file.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum ShapeMultiPatchType {

	/** A triangle strip.
	 *
	 * <p>A linked strip of triangles, where every vertex (after the first two)
	 * completes a new triangle. A new triangle is always formed by
	 * connecting the new vertex with its two immediate predecessors.
	 */
	TRIANGLE_STRIP(0),

	/** A triangle fan.
	 *
	 * <p>A linked fan of triangles, where every vertex (after the first two)
	 * completes a new triangle. A new triangle is always formed by
	 * connecting the new vertex with its immediate predecessor and the
	 * first vertex of the part.
	 */
	TRIANGLE_FAN(1),

	/** An outer ring.
	 *
	 * <p>The outer ring of a polygon.
	 */
	OUTER_RING(2),

	/** An inner ring.
	 *
	 * <p>A hole of a polygon.
	 */
	INNER_RING(3),

	/** First ring.
	 *
	 * <p>The first ring of a polygon of an unspecified type.
	 */
	FIRST_RING(4),

	/** A ring.
	 *
	 * <p>A ring of a polygon of an unspecified type.
	 */
	RING(5);

	/** Is the type identifier used inside ESRI shape files.
	 */
	@SuppressWarnings("checkstyle:visibilitymodifier")
	public final int partType;

	ShapeMultiPatchType(int shapeType) {
		this.partType = shapeType;
	}

	/** Replies the element type which is corresponding to the
	 * given number (from ESRI shape file specification).
	 *
	 * @param esriNumber is the number from the ESRI shape file specification.
	 * @return the element type corresponding to the number.
	 * @throws ShapeFileException if the given parameter is not a valid integer.
	 */
	@Pure
	public static ShapeMultiPatchType fromESRIInteger(int esriNumber) throws ShapeFileException {
		for (final ShapeMultiPatchType type : values()) {
			if (type.partType == esriNumber) {
				return type;
			}
		}
		throw new InvalidMultipatchTypeException(esriNumber);
	}

}

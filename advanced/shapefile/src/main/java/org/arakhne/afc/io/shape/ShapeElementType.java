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

package org.arakhne.afc.io.shape;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Supported types of elements in an ESRI shape file.
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
public enum ShapeElementType {

	/**
	 * The type of element is unsupported yet.
	 */
	UNSUPPORTED(-1),

	/**
	 * The element is not set.
	 */
	NULL(0),

	/**
	 * The element is a point.
	 */
	POINT(1),

	/**
	 * The element is a polyline.
	 */
	POLYLINE(3),

	/**
	 * The element is a polygon.
	 */
	POLYGON(5),

	/**
	 * The element is a collection of points.
	 */
	MULTIPOINT(8),

	/**
	 * The element is a point with Z coordinate and a measure.
	 */
	POINT_Z(11),

	/**
	 * The element is a polyline with Z coordinate and a measure.
	 */
	POLYLINE_Z(13),

	/**
	 * The element is a polygon with Z coordinate and a measure.
	 */
	POLYGON_Z(15),

	/**
	 * The element is a multipoint with Z coordinate and a measure.
	 */
	MULTIPOINT_Z(18),

	/**
	 * The element is a point with a measure.
	 */
	POINT_M(21),

	/**
	 * The element is a polyline with a measure.
	 */
	POLYLINE_M(23),

	/**
	 * The element is a polygonwith a measure.
	 */
	POLYGON_M(25),

	/**
	 * The element is a multipoint with a measure.
	 */
	MULTIPOINT_M(28),

	/**
	 * The element is a multipatch.
	 */
	MULTIPATCH(31);

	/** Is the type identifier used inside ESRI shape files.
	 */
	@SuppressWarnings("checkstyle:visibilitymodifier")
	public final int shapeType;

	ShapeElementType(int shapeType) {
		this.shapeType = shapeType;
	}

	/** Replies the element type which is corresponding to the
	 * given number (from ESRI shape file specification).
	 *
	 * @param esriNumber is the number from the ESRI shape file specification.
	 * @return the element type corresponding to the number.
	 */
	@Pure
	public static ShapeElementType fromESRIInteger(int esriNumber) {
		for (final ShapeElementType type : values()) {
			if (type.shapeType == esriNumber) {
				return type;
			}
		}
		return UNSUPPORTED;
	}

	/** Replies if this type of shape element supports the Z coordinates.
	 *
	 * <p>According to ESRI, only objects of type *_Z contain Z coordinates.
	 *
	 * @return <code>true</code> if the z-coordinate are supported, otherwise <code>false</code>
	 */
	@Pure
	public boolean hasZ() {
		return this == POINT_Z || this == POLYGON_Z || this == POLYLINE_Z || this == MULTIPOINT_Z || this == MULTIPATCH;
	}

	/** Replies if this type of shape element supports the M coordinates.
	 *
	 * <p>According to ESRI, objects of type *_Z or *_M contain M coordinates.
	 *
	 * <p>M coordinate has different meanings depending on the type of object:
	 * height from floor to top, some measure on object...
	 *
	 * @return <code>true</code> if the m-coordinate are supported, otherwise <code>false</code>
	 */
	@Pure
	@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
	public boolean hasM() {
		return this == POINT_Z || this == POLYGON_Z || this == POLYLINE_Z || this == MULTIPOINT_Z
				|| this == POINT_M || this == POLYGON_M || this == POLYLINE_M || this == MULTIPOINT_M || this == MULTIPATCH;
	}

	/** Replies if this type is a ponctual element and not a collection of elements.
	 *
	 * @return <code>true</code> if the type corresponds to a ponctual element,
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 */
	@Pure
	public boolean isPonctualElementType() {
		return this == POINT || this == POINT_Z || this == POINT_M;
	}

	/** Replies if this type is a collection of element and not a ponctual element.
	 *
	 * @return <code>true</code> if the type corresponds to a collection of elements,
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
	public boolean isElementCollectionType() {
		return this == MULTIPOINT || this == MULTIPOINT_Z || this == MULTIPOINT_M
				|| this == POLYGON || this == POLYGON_Z || this == POLYGON_M
				|| this == POLYLINE || this == POLYLINE_Z || this == POLYLINE_M;
	}

}

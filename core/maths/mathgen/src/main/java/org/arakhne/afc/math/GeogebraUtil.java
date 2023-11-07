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

package org.arakhne.afc.math;

import org.eclipse.xtext.xbase.lib.Pure;

/** Utilities for Geogebra.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public final class GeogebraUtil {

	private static final String GEOGEBRA_START_BRACES = "{"; //$NON-NLS-1$

	private static final String GEOGEBRA_START_PARENTHS = "("; //$NON-NLS-1$

	private static final String GEOGEBRA_SEP = ","; //$NON-NLS-1$

	private static final String GEOGEBRA_END_BRACES = "}"; //$NON-NLS-1$

	private static final String GEOGEBRA_END_PARENTHS = ")"; //$NON-NLS-1$

	private static final String GEOGEBRA_POLYGON = "Polygon"; //$NON-NLS-1$

	private static final String GEOGEBRA_SEGMENT = "Segment"; //$NON-NLS-1$

	private static final String GEOGEBRA_ELLIPSE = "Ellipse"; //$NON-NLS-1$

	private static final String GEOGEBRA_CIRCLE = "Circle"; //$NON-NLS-1$

	private static final String GEOGEBRA_SPHERE = "Sphere"; //$NON-NLS-1$

	private static final String GEOGEBRA_PRISM = "Prism"; //$NON-NLS-1$

	private static final String GEOGEBRA_X = "x"; //$NON-NLS-1$

	private static final String GEOGEBRA_Y = "y"; //$NON-NLS-1$

	private static final String GEOGEBRA_Z = "z"; //$NON-NLS-1$

	private static final String GEOGEBRA_EQ = "="; //$NON-NLS-1$

	private static final String GEOGEBRA_MUL = "*"; //$NON-NLS-1$

	private static final String GEOGEBRA_PLUS = "+"; //$NON-NLS-1$

	private static final String GEOGEBRA_MINUS = "-"; //$NON-NLS-1$

	private GeogebraUtil() {
		//
	}

	/** Replies the representation of this matrix.
	 *
	 * @param size the size of the matrix, i.e. the number of components in the tuple.
	 * @param values the matrix values.
	 * @return the matrix representation.
	 */
	@Pure
	public static String toMatrixDefinition(int size, double... values) {
		assert values.length == size * size;
		final StringBuilder value = new StringBuilder(GEOGEBRA_START_BRACES);
		int k = 0;
		for (int i = 0; i < size; ++i) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(GEOGEBRA_START_BRACES);
			for (int j = 0; j < size; ++j, ++k) {
				if (j > 0) {
					value.append(GEOGEBRA_SEP);
				}
				value.append(values[k]);
			}
			value.append(GEOGEBRA_END_BRACES);
		}
		value.append(GEOGEBRA_END_BRACES);
		return value.toString();
	}

	/** Replies the tuple representation.
	 *
	 * @param size the size of the tuple, i.e. the number of components in the tuple.
	 * @param values the tuple values.
	 * @return the Geogebra representation of the tuple.
	 */
	public static String toTupleDefinition(int size, double... values) {
		assert values.length == size;
		final StringBuilder value = new StringBuilder(GEOGEBRA_START_PARENTHS);
		int k = 0;
		for (int i = 0; i < size; ++i) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(values[k]);
		}
		value.append(GEOGEBRA_END_PARENTHS);
		return value.toString();
	}

	/** Replies the polygon representation.
	 *
	 * @param size the number of components per point.
	 * @param values the values of the components. It must contains 2 points, i.e., {@code size}*2 components.
	 * @return the Geogebra representation of the polygon.
	 */
	public static String toPolygonDefinition(int size, double... values) {
		assert values.length >= size * 2;
		final StringBuilder value = new StringBuilder(GEOGEBRA_POLYGON);
		value.append(GEOGEBRA_START_PARENTHS);
		int k = 0;
		for (int i = 0; i < values.length; i += size) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(GEOGEBRA_START_PARENTHS);
			for (int j = 0; j < size; ++j, ++k) {
				if (j > 0) {
					value.append(GEOGEBRA_SEP);
				}
				value.append(values[k]);
			}
			value.append(GEOGEBRA_END_PARENTHS);
		}
		value.append(GEOGEBRA_END_PARENTHS);
		return value.toString();
	}

	/** Replies the segment representation.
	 *
	 * @param size the number of components per point.
	 * @param values the values of the components. It must contains 2 points, i.e., {@code size}*2 components.
	 * @return the Geogebra representation of the segment.
	 */
	public static String toSegmentDefinition(int size, double... values) {
		assert values.length >= size * 2;
		final StringBuilder value = new StringBuilder(GEOGEBRA_SEGMENT);
		value.append(GEOGEBRA_START_PARENTHS);
		int k = 0;
		for (int i = 0; i < values.length; i += size) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(GEOGEBRA_START_PARENTHS);
			for (int j = 0; j < size; ++j, ++k) {
				if (j > 0) {
					value.append(GEOGEBRA_SEP);
				}
				value.append(values[k]);
			}
			value.append(GEOGEBRA_END_PARENTHS);
		}
		value.append(GEOGEBRA_END_PARENTHS);
		return value.toString();
	}

	/** Replies the ellipse representation.
	 *
	 * @param size the number of components per point.
	 * @param values the values of the components. It must contains 2 points and the maximal radius of the ellipse, i.e., {@code size}*2+1 components.
	 * @return the Geogebra representation of the ellipse.
	 */
	public static String toEllipseDefinition(int size, double... values) {
		assert values.length >= size * 2 + 1;
		final StringBuilder value = new StringBuilder(GEOGEBRA_ELLIPSE);
		// Point 1
		value.append(GEOGEBRA_START_PARENTHS).append(GEOGEBRA_START_PARENTHS);
		int k = 0;
		for (int i = 0; i < size; ++i, ++k) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(values[k]);
		}
		// Point 2
		value.append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_SEP).append(GEOGEBRA_START_PARENTHS);
		for (int i = 0; i < size; ++i, ++k) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(values[k]);
		}
		// Max radius
		value.append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_SEP).append(values[k]).append(GEOGEBRA_SEP);
		return value.toString();
	}

	/** Replies the circle or sphere representation.
	 *
	 * @param size the number of components per point.
	 * @param values the values of the components. It must contains 1 points and the radius of the circle, i.e., {@code size}+1 components.
	 * @return the Geogebra representation of the circle/sphere.
	 */
	public static String toCircleDefinition(int size, double... values) {
		assert values.length >= size + 1;
		final StringBuilder value = new StringBuilder(size <= 2 ? GEOGEBRA_CIRCLE : GEOGEBRA_SPHERE);
		value.append(GEOGEBRA_START_PARENTHS).append(GEOGEBRA_START_PARENTHS);
		int k = 0;
		for (int i = 0; i < size; ++i, ++k) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(values[k]);
		}
		// Radius
		value.append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_SEP).append(values[k]).append(GEOGEBRA_SEP);
		return value.toString();
	}

	/** Replies the prism representation.
	 *
	 * @param size the number of components per point.
	 * @param values the values of the components. It must contains 2 co-planar points and a extrusion point.
	 * @return the Geogebra representation of the prism.
	 */
	public static String toPrismDefinition(int size, double... values) {
		assert values.length >= size * 3;
		final StringBuilder value = new StringBuilder(GEOGEBRA_PRISM);
		value.append(GEOGEBRA_START_PARENTHS);
		// Polygon
		value.append(GEOGEBRA_POLYGON).append(GEOGEBRA_START_PARENTHS);
		int k = 0;
		for (int i = 0; i < values.length - size; i += size) {
			if (i > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(GEOGEBRA_START_PARENTHS);
			for (int j = 0; j < size; ++j, ++k) {
				if (j > 0) {
					value.append(GEOGEBRA_SEP);
				}
				value.append(values[k]);
			}
			value.append(GEOGEBRA_END_PARENTHS);
		}
		value.append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_SEP).append(GEOGEBRA_START_PARENTHS);
		// Extrusion point
		for (int j = 0; j < size; ++j, ++k) {
			if (j > 0) {
				value.append(GEOGEBRA_SEP);
			}
			value.append(values[k]);
		}
		value.append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_END_PARENTHS);
		return value.toString();
	}

	/** Replies the plane representation.
	 *
	 * @param px x coordinate of the point that is one the plane.
	 * @param py y coordinate of the point that is one the plane.
	 * @param pz z coordinate of the point that is one the plane.
	 * @param vx x coordinate of the normal vector.
	 * @param vy y coordinate of the normal vector.
	 * @param vz z coordinate of the normal vector.
	 * @return the Geogebra representation of the plane.
	 */
	public static String toPlaneDefinition(double px,  double py, double pz, double vx, double vy, double vz) {
		final StringBuilder value = new StringBuilder(GEOGEBRA_START_PARENTHS);
		value.append(vx).append(GEOGEBRA_SEP);
		value.append(vy).append(GEOGEBRA_SEP);
		value.append(vz).append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_START_PARENTHS);
		value.append(GEOGEBRA_X).append(GEOGEBRA_SEP);
		value.append(GEOGEBRA_Y).append(GEOGEBRA_SEP);
		value.append(GEOGEBRA_Z).append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_EQ).append(GEOGEBRA_START_PARENTHS);
		value.append(vx).append(GEOGEBRA_SEP);
		value.append(vy).append(GEOGEBRA_SEP);
		value.append(vz).append(GEOGEBRA_END_PARENTHS).append(GEOGEBRA_START_PARENTHS);
		value.append(px).append(GEOGEBRA_SEP);
		value.append(py).append(GEOGEBRA_SEP);
		value.append(pz).append(GEOGEBRA_END_PARENTHS);
		return value.toString();
	}

	/** Replies the plane representation.
	 *
	 * @param a the a component of the equation plane.
	 * @param b the b component of the equation plane.
	 * @param c the c component of the equation plane.
	 * @param d the d component of the equation plane.
	 * @return the Geogebra representation of the plane.
	 */
	public static String toPlaneDefinition(double a,  double b, double c, double d) {
		final StringBuilder value = new StringBuilder();
		value.append(a).append(GEOGEBRA_MUL).append(GEOGEBRA_X);
		if (b < 0.) {
			value.append(GEOGEBRA_MINUS).append(Math.abs(b));
		} else {
			value.append(GEOGEBRA_PLUS).append(b);
		}
		value.append(GEOGEBRA_MUL).append(GEOGEBRA_Y);
		if (c < 0.) {
			value.append(GEOGEBRA_MINUS).append(Math.abs(c));
		} else {
			value.append(GEOGEBRA_PLUS).append(c);
		}
		value.append(GEOGEBRA_MUL).append(GEOGEBRA_Z);
		if (d < 0.) {
			value.append(GEOGEBRA_MINUS).append(Math.abs(d));
		} else {
			value.append(GEOGEBRA_PLUS).append(d);
		}
		value.append(GEOGEBRA_EQ).append(0.);
		return value.toString();
	}

}

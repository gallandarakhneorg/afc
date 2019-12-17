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

package org.arakhne.afc.gis.location;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystemConstants;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.text.Encryption;

/**
 * Some geo-location functions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GeoLocationUtil {

	/** Size of the area covered by a GIS point when
	 * computing its bounds (in meters).
	 */
	public static final double GIS_POINT_SIZE = .3;

	/** Demo-size of the area covered by a GIS point when
	 * computing its bounds (in meters).
	 */
	public static final double GIS_POINT_DEMI_SIZE = GIS_POINT_SIZE / 2.;

	private static final double DEFAULT_DISTANCE_PRECISION = 0.1;

	private static double distancePrecision = DEFAULT_DISTANCE_PRECISION;

	/** Constructor.
	 */
	protected GeoLocationUtil() {
		//
	}

	/** Replies the precision used to test a distance value (in meters).
	 *
	 * @return the epsilon
	 */
	@Pure
	public static double getDistanceEpsilon() {
		return distancePrecision;
	}

	/** Replies the precision used to test a distance value.
	 *
	 * @param newPrecisionValue the new precision value (in meters).
	 * @return the old precision value.
	 */
	public static double setDistanceEpsilon(double newPrecisionValue) {
		if ((newPrecisionValue >= 1) || (newPrecisionValue <= 0)) {
			throw new IllegalArgumentException();
		}
		final double old = distancePrecision;
		distancePrecision = newPrecisionValue;
		return old;
	}

	/** Cast the specified geo-distance to the nearest distance according
	 * to the current supported precision.
	 *
	 * @param distance the distance to cast.
	 * @return the casted distance.
	 */
	@Pure
	public static float castDistance(double distance) {
		return (float) (Math.round(distance / distancePrecision) * distancePrecision);
	}

	/** Replies if the specified points are approximatively equal.
	 * This function uses the distance precision.
	 *
	 * @param p1 the first point.
	 * @param p2 the second point.
	 * @return <code>true</code> if both points are equal, otherwise <code>false</code>
	 */
	@Pure
	public static boolean epsilonEqualsDistance(Point3D<?, ?> p1, Point3D<?, ?> p2) {
		final double distance = p1.getDistance(p2);
		return distance >= -distancePrecision && distance <= distancePrecision;
	}

	/** Replies if the specified points are approximatively equal.
	 * This function uses the distance precision.
	 *
	 * @param p1 the first point.
	 * @param p2 the second point.
	 * @return <code>true</code> if both points are equal, otherwise <code>false</code>
	 */
	@Pure
	public static boolean epsilonEqualsDistance(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		final double distance = p1.getDistance(p2);
		return distance >= -distancePrecision && distance <= distancePrecision;
	}

	/** Replies if the specified distance value is approximatively equal to zero.
	 *
	 * @param value the value to test.
	 * @return <code>true</code> if the given distance is near zero, otherwise <code>false</code>
	 */
	@Pure
	public static boolean epsilonEqualsDistance(double value) {
		return value >= -distancePrecision && value <= distancePrecision;
	}

	/** Replies if the specified distances are approximatively equal.
	 *
	 * @param value1 the first value.
	 * @param value2 the second value.
	 * @return <code>true</code> if both values are equal, otherwise <code>false</code>
	 */
	@Pure
	public static boolean epsilonEqualsDistance(double value1, double value2) {
		return value1 >= (value2 - distancePrecision) && value1 <= (value2 + distancePrecision);
	}

	/** Replies if the specified distance value is approximatively equal to zero,
	 * less or greater than zero.
	 *
	 * @param value the value.
	 * @return a negative value if the parameter is negative, a positive
	 *     value of the parameter is positive, or zero if the parameter is
	 *     approximatively equal to zero.
	 */
	@Pure
	public static int epsilonCompareToDistance(double value) {
		if (value >= -distancePrecision && value <= distancePrecision) {
			return 0;
		}
		if (value < -distancePrecision) {
			return -1;
		}
		return 1;
	}

	/** Replies if the specified distances are approximatively equal,
	 * less or greater than.
	 *
	 * @param distance1 the first distance.
	 * @param distance2 the second distance.
	 * @return a negative value if the parameter <var>distance1</var> is
	 *     lower than <var>distance2</var>, a positive if <var>distance1</var>
	 *     is greater than <var>distance2</var>, zero if the two parameters
	 *     are approximatively equal.
	 */
	@Pure
	public static int epsilonCompareToDistance(double distance1, double distance2) {
		final double min = distance2 - distancePrecision;
		final double max = distance2 + distancePrecision;
		if (distance1 >= min && distance1 <= max) {
			return 0;
		}
		if (distance1 < min) {
			return -1;
		}
		return 1;
	}

	/**
	 * Compares its two arguments for order.  Returns a negative integer,
	 * zero, or a positive integer as the first argument is less than, equal
	 * to, or greater than the second.
	 *
	 * <p>In the foregoing description, the notation
	 * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
	 * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
	 * <tt>0</tt>, or <tt>1</tt> according to whether the value of
	 * <i>expression</i> is negative, zero or positive.
	 *
	 * <p>The implementor must ensure that <tt>sgn(compare(x, y)) ==
	 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>compare(x, y)</tt> must throw an exception if and only
	 * if <tt>compare(y, x)</tt> throws an exception.)
	 *
	 * <p>The implementor must also ensure that the relation is transitive:
	 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
	 * <tt>compare(x, z)&gt;0</tt>.
	 *
	 * <p>Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
	 * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
	 * <tt>z</tt>.
	 *
	 * <p>It is generally the case, but <i>not</i> strictly required that
	 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
	 * any comparator that violates this condition should clearly indicate
	 * this fact.  The recommended language is "Note: this comparator
	 * imposes orderings that are inconsistent with equals."
	 *
	 * @param l1 the first object to be compared.
	 * @param l2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the
	 * 	       first argument is less than, equal to, or greater than the
	 *	       second.
	 * @throws ClassCastException if the arguments' types prevent them from
	 * 	       being compared by this comparator.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	@Pure
	public static int compare(GeoLocation l1, GeoLocation l2) {
		if (l1 == l2) {
			return 0;
		}
		if (l1 == null && l2 != null) {
			return Integer.MIN_VALUE;
		}
		if (l2 == null && l1 != null) {
			return Integer.MAX_VALUE;
		}

		assert l1 != null && l2 != null;

		final Rectangle2d b1 = l1.toBounds2D().toBoundingBox();
		final Rectangle2d b2 = l2.toBounds2D().toBoundingBox();

		assert b1 != null && b2 != null;

		if (b1.isEmpty() && b2.isEmpty()) {
			final GeoId id1 = l1.toGeoId();
			final GeoId id2 = l2.toGeoId();
			assert id1 != null && id2 != null;
			return id1.compareTo(id2);
		}

		if (b1.isEmpty()) {
			assert !b2.isEmpty();
			return Integer.MIN_VALUE;
		}
		if (b2.isEmpty()) {
			assert !b1.isEmpty();
			return Integer.MAX_VALUE;
		}

		assert !b1.isEmpty() && !b2.isEmpty();

		int cmp = MathUtil.compareEpsilon(b1.getMinX(), b2.getMinX());
		if (cmp != 0) {
			return cmp;
		}
		cmp = MathUtil.compareEpsilon(b1.getMinY(), b2.getMinY());
		if (cmp != 0) {
			return cmp;
		}
		cmp = MathUtil.compareEpsilon(b1.getMaxX(), b2.getMaxX());
		if (cmp != 0) {
			return cmp;
		}
		return MathUtil.compareEpsilon(b1.getMaxY(), b2.getMaxY());
	}

	/** Compute and replies the internal identifier that may
	 * be used to create a GeoId from the given point coordinates.
	 * The coordinates are the (x;y) pairs of the points. X coordinates
	 * are at even indexes, and Y coordinates at odd indexes.
	 *
	 * @param coordinates are the point pairs.
	 * @param bounds are the bounds of the points.
	 * @return the internal identifier.
	 */
	@Pure
	public static String makeInternalId(float[] coordinates, Rectangle2d bounds) {
		final StringBuilder buf = new StringBuilder("points"); //$NON-NLS-1$
		float minx = Float.NaN;
		float miny = Float.NaN;
		float maxx = Float.NaN;
		float maxy = Float.NaN;
		if (bounds != null) {
			bounds.clear();
		}
		for (int idx = 0; idx < coordinates.length - 1; idx += 2) {
			if (idx > 0) {
				buf.append('-');
			}
			buf.append('(');
			buf.append(coordinates[idx]);
			buf.append(';');
			buf.append(coordinates[idx + 1]);
			buf.append(')');
			if (idx == 0) {
				minx = coordinates[idx];
				maxx = minx;
				miny = coordinates[idx + 1];
				maxy = miny;
			} else {
				if (coordinates[idx] < minx) {
					minx = coordinates[idx];
				}
				if (coordinates[idx] > maxx) {
					maxx = coordinates[idx];
				}
				if (coordinates[idx + 1] < miny) {
					miny = coordinates[idx + 1];
				}
				if (coordinates[idx + 1] > maxy) {
					maxy = coordinates[idx + 1];
				}
			}
		}

		if (bounds != null && !Float.isNaN(minx)) {
			bounds.setFromCorners(minx, miny, maxx, maxy);
		}
		return Encryption.md5(buf.toString());
	}

	/** Compute and replies the internal identifier that may
	 * be used to create a GeoId from the given point.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate
	 * @return the internal identifier.
	 */
	@Pure
	public static String makeInternalId(float x, float y) {
		final StringBuilder buf = new StringBuilder("point"); //$NON-NLS-1$
		buf.append(x);
		buf.append(';');
		buf.append(y);
		return Encryption.md5(buf.toString());
	}

	/** Compute and replies the internal identifier that may
	 * be used to create a GeoId from the given identifier.
	 * The replied identifier is not geolocalized.
	 *
	 * @param uid the identifier
	 * @return the internal identifier.
	 */
	@Pure
	public static String makeInternalId(UUID uid) {
		final StringBuilder buf = new StringBuilder("nowhere(?"); //$NON-NLS-1$
		buf.append(uid.toString());
		buf.append("?)"); //$NON-NLS-1$
		return Encryption.md5(buf.toString());
	}

	/** Compute and replies the internal identifier that may
	 * be used to create a GeoId from the given rectangle.
	 *
	 * @param minx minimum x coordinate of the bounding rectangle.
	 * @param miny minimum y coordinate of the bounding rectangle.
	 * @param maxx maximum x coordinate of the bounding rectangle.
	 * @param maxy maximum y coordinate of the bounding rectangle.
	 * @return the internal identifier.
	 */
	@Pure
	public static String makeInternalId(float minx, float miny, float maxx, float maxy) {
		final StringBuilder buf = new StringBuilder("rectangle"); //$NON-NLS-1$
		buf.append('(');
		buf.append(minx);
		buf.append(';');
		buf.append(miny);
		buf.append(")-("); //$NON-NLS-1$
		buf.append(maxx);
		buf.append(';');
		buf.append(maxy);
		buf.append(')');
		return Encryption.md5(buf.toString());
	}

	/** Force the GIS coordinate systems to be the default coordinates systems.
	 * This function sets the default coordinate systems
	 * to the value specified in the system properties "fr.utbm.set.math.defaultCoordinateSystem2D" and
	 * "fr.utbm.set.math.defaultCoordinateSystem3D", or
	 * if not set to {@link CoordinateSystemConstants#GIS_2D} and {@link CoordinateSystemConstants#GIS_3D}.
	 *
	 * @since 15.0
	 */
	public static void setGISCoordinateSystemAsDefault() {
		String v;

		try {
			v = System.getProperty("fr.utbm.set.math.defaultCoordinateSystem2D"); //$NON-NLS-1$
		} catch (Throwable exception) {
			v = null;
		}
		CoordinateSystem2D cs2d = null;
		if (v != null) {
			cs2d = CoordinateSystem2D.valueOf(v);
		}
		if (cs2d == null) {
			cs2d = CoordinateSystemConstants.GIS_2D;
		}
		CoordinateSystem2D.setDefaultCoordinateSystem(cs2d);

		try {
			v = System.getProperty("fr.utbm.set.math.defaultCoordinateSystem3D"); //$NON-NLS-1$
		} catch (Throwable exception) {
			v = null;
		}
		CoordinateSystem3D cs3d = null;
		if (v != null) {
			cs3d = CoordinateSystem3D.valueOf(v);
		}
		if (cs3d == null) {
			cs3d = cs2d.toCoordinateSystem3D();
		}
		CoordinateSystem2D.setDefaultCoordinateSystem(cs2d);
	}

}

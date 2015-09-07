/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stéphane GALLAND
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.math;

import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_BOTTOM;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_INSIDE;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_LEFT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_RIGHT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_TOP;
import static org.arakhne.afc.math.MathConstants.EPSILON;
import static org.arakhne.afc.math.MathConstants.PI;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.continuous.Circle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Ellipse2f;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Segment2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.geometry.d2.discrete.Segment2i;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.math.physics.MeasureUnitUtil;
import org.arakhne.afc.util.Pair;

/** Mathematic and geometric utilities.
 * 
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class MathUtil {

	private MathUtil() {
		//
	}
	
	/** Clamp the given angle in radians to {@code [0;2PI)}.
	 * 
	 * @param radians is the angle to clamp
	 * @return the angle in {@code [0;2PI)} range.
	 * @deprecated see {@link #clampRadian0To2PI(double)}
	 */
	@Deprecated
	public static double clampRadian(double radians) {
		return clampRadian0To2PI(radians);
	}

	/** Clamp the given value to the given range.
	 * <p>
	 * If the value is outside the {@code [min;max]}
	 * range, it is clamp to the nearest bounding value
	 * <var>min</var> or <var>max</var>.
	 * 
	 * @param v is the value to clamp.
	 * @param min is the min value of the range.
	 * @param max is the max value of the range.
	 * @return the value in {@code [min;max]} range.
	 */
	public static double clamp(double v, double min, double max) {
		if (min<max) {
			if (v<min) return min;
			if (v>max) return max;
		}
		else {
			if (v>min) return min;
			if (v<max) return max;
		}
		return v;
	}	

	/** Replies if the given value is near zero.
	 * 
	 * @param value is the value to test.
	 * @return <code>true</code> if the given <var>value</var>
	 * is near zero, otherwise <code>false</code>.
	 * @see MathConstants#EPSILON
	 */
	public static boolean isEpsilonZero(double value) {
		return Math.abs(value) <= EPSILON;
	}

	/** Replies if the given value is near zero.
	 * 
	 * @param value is the value to test.
	 * @param epsilon the approximation epsilon.
	 * @return <code>true</code> if the given <var>value</var>
	 * is near zero, otherwise <code>false</code>.
	 */
	public static boolean isEpsilonZero(double value, double epsilon) {
		return Math.abs(value) <= epsilon;
	}

	/** Replies if the given values are near.
	 * 
	 * @param v1
	 * @param v2
	 * @return <code>true</code> if the given <var>v1</var>
	 * is near <var>v2</var>, otherwise <code>false</code>.
	 * @see MathConstants#EPSILON
	 */
	public static boolean isEpsilonEqual(double v1, double v2) {
		return Math.abs(v1 - v2) <= EPSILON;
	}

	/** Replies if the given values are near.
	 * 
	 * @param v1
	 * @param v2
	 * @param epsilon the approximation epsilon.
	 * @return <code>true</code> if the given <var>v1</var>
	 * is near <var>v2</var>, otherwise <code>false</code>.
	 */
	public static boolean isEpsilonEqual(double v1, double v2, double epsilon) {
		return Math.abs(v1 - v2) <= epsilon;
	}

	/** Compares its two arguments for order.
	 * Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
	 * 
	 * @param v1
	 * @param v2
	 * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
	 */
	public static int compareEpsilon(double v1, double v2) {
		double v = v1 - v2;
		if (Math.abs(v) <= EPSILON) {
			return 0;
		}
		if (v <= 0.) {
			return (int) v - 1;
		}
		return (int) v + 1;
	}

	/** Compares its two arguments for order.
	 * Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
	 * 
	 * @param v1
	 * @param v2
	 * @param epsilon approximation epsilon
	 * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
	 */
	public static int compareEpsilon(double v1, double v2, double epsilon) {
		double v = v1 - v2;
		if (Math.abs(v) <= epsilon) {
			return 0;
		}
		if (v <= 0.) {
			return (int) v - 1;
		}
		return (int) v + 1;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static double max(double... values) {
		if (values==null || values.length==0) return Double.NaN;
		double m = values[0];
		for(double v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static double max(float... values) {
		if (values==null || values.length==0) return Float.NaN;
		float m = values[0];
		for(float v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static int max(int... values) {
		if (values==null || values.length==0) return 0;
		int m = values[0];
		for(int v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static long max(long... values) {
		if (values==null || values.length==0) return 0;
		long m = values[0];
		for(long v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static short max(short... values) {
		if (values==null || values.length==0) return 0;
		short m = values[0];
		for(short v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static double min(double... values) {
		if (values==null || values.length==0) return Double.NaN;
		double m = values[0];
		for(double v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static float min(float... values) {
		if (values==null || values.length==0) return Float.NaN;
		float m = values[0];
		for(float v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static int min(int... values) {
		if (values==null || values.length==0) return 0;
		int m = values[0];
		for(int v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static long min(long... values) {
		if (values==null || values.length==0) return 0;
		long m = values[0];
		for(long v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static short min(short... values) {
		if (values==null || values.length==0) return 0;
		short m = values[0];
		for(short v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @return the distance beetween the point and the segment.
	 * @deprecated see {@link Segment2f#distanceSegmentPoint(double, double, double, double, double, double, Point2D)}
	 */
	@Deprecated
	public static double distancePointToSegment(double px, double py, double x1, double y1, double x2, double y2) {
		return distancePointToSegment(px, py, x1, y1, x2, y2, (Point2D) null);
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @return the distance beetween the point and the segment.
	 * @deprecated see {@link Segment2f#distanceSquaredSegmentPoint(double, double, double, double, double, double, Point2D)}
	 */
	@Deprecated
	public static double distanceSquaredPointToSegment(double px, double py, double x1, double y1, double x2, double y2) {
		return distanceSquaredPointToSegment(px, py, x1, y1, x2, y2, (Point2D) null);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 * @deprecated see {@link Segment2f#distanceSegmentPoint(double, double, double, double, double, double, Point2D)}
	 */
	@Deprecated
	public static double distancePointToSegment(double px, double py, double x1, double y1, double x2, double y2, Point2D pts) {
		return Segment2f.distanceSegmentPoint(x1, y1, x2, y2, px, py, pts);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 * @deprecated see {@link #distancePointToSegment(double, double, double, double, double, double, Point2D)}
	 */
	@Deprecated
	public static double distancePointToSegment(double px, double py, double x1, double y1, double x2, double y2,
			org.arakhne.afc.math.generic.Point2D pts) {
		double r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distancePointToPoint(px, py, x1, y1);
		double r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		double ratio = r_numerator / r_denomenator;

		if (ratio<=0.) {
			if (pts!=null) pts.set((double) x1, (double) y1);
			return Math.sqrt((px-x1)*(px-x1) + (py-y1)*(py-y1));
		}

		if (ratio>=1.) {
			if (pts!=null) pts.set((double) x2, (double) y2);
			return Math.sqrt((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}

		if (pts!=null) pts.set(
				(double) (ratio * (x2-x1)),
				(double) (ratio * (y2-y1)));

		double s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return Math.abs(s) * Math.sqrt(r_denomenator);
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 * @deprecated see {@link Segment2f#distanceSquaredSegmentPoint(double, double, double, double, double, double, Point2D)}
	 */
	@Deprecated
	public static double distanceSquaredPointToSegment(double px, double py, double x1, double y1, double x2, double y2, Point2D pts) {
		return Segment2f.distanceSquaredSegmentPoint(x1, y1, x2, y2, px, py, pts);
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @return the distance beetween the point and the line.
	 * @see #distanceSquaredPointToLine(double, double, double, double, double, double)
	 * @see #relativeDistancePointToLine(double, double, double, double, double, double)
	 * @deprecated see {@link Segment2f#distanceLinePoint(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static double distancePointToLine(double px, double py, double x1, double y1, double x2, double y2) {
		return Segment2f.distanceLinePoint(x1, y1, x2, y2, px, py);
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @return the distance beetween the point and the line.
	 * @see #distancePointToLine(double, double, double, double, double, double)
	 * @deprecated see {@link Segment2f#distanceSquaredLinePoint(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static double distanceSquaredPointToLine(double px, double py, double x1, double y1, double x2, double y2) {
		return Segment2f.distanceSquaredLinePoint(x1, y1, x2, y2, px, py);
	}

	/** Compute the distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distanceSquaredPointToPoint(double, double, double, double)
	 * @see #distanceL1PointToPoint(double, double, double, double)
	 * @deprecated see {@link Point2f#distancePointPoint(double, double, double, double)}
	 */
	@Deprecated
	public static double distancePointToPoint(double x1, double y1, double x2, double y2) {
		return Point2f.distancePointPoint(x1, y1, x2, y2);
	}

	/** Compute the squared distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointToPoint(double, double, double, double)
	 * @see #distanceL1PointToPoint(double, double, double, double)
	 * @deprecated see {@link Point2f#distanceSquaredPointPoint(double, double, double, double)}
	 */
	@Deprecated
	public static double distanceSquaredPointToPoint(double x1, double y1, double x2, double y2) {
		return Point2f.distanceSquaredPointPoint(x1, y1, x2, y2);
	}

	/** Compute the L-1 (Manhattan) distance between 2 points.
	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointToPoint(double, double, double, double)
	 * @see #distanceSquaredPointToPoint(double, double, double, double)
	 * @deprecated see {@link Point2f#distanceL1PointPoint(double, double, double, double)}
	 */
	@Deprecated
	public static double distanceL1PointToPoint(double x1, double y1, double x2, double y2) {
		return Point2f.distanceL1PointPoint(x1, y1, x2, y2);
	}

	/** Compute the L-infinite distance between 2 points.
	 * The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointToPoint(double, double, double, double)
	 * @see #distanceSquaredPointToPoint(double, double, double, double)
	 * @deprecated see {@link Point2f#distanceLinfPointPoint(double, double, double, double)}
	 */
	@Deprecated
	public static double distanceLinfPointToPoint(double x1, double y1, double x2, double y2) {
		return Point2f.distanceLinfPointPoint(x1, y1, x2, y2);
	}

	/**
	 * Replies the specified angle translated between 0 and 2PI.
	 * 
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 * @deprecated see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	public static double clampRadian0To2PI(double radian) {
		if ((!Double.isNaN(radian)) && (!Double.isInfinite(radian))) {
			return clampTrigo(radian, 0, 2*PI);
		}
		return radian;
	}

	/**
	 * Replies the specified angle translated between -PI and PI.
	 * 
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 * @deprecated see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	public static double clampRadianMinusPIToPI(double radian) {
		if ((!Double.isNaN(radian)) && (!Double.isInfinite(radian))) {
			return clampTrigo(radian, -PI, PI);
		}
		return radian;
	}

	/**
	 * Replies the specified angle translated between 0 and 360.
	 * 
	 * @param degree
	 *            is an angle
	 * @return normalized angle
	 * @deprecated see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	public static double clampDegree0To360(double degree) {
		if ((!Double.isNaN(degree)) && (!Double.isInfinite(degree))) {
			return clampTrigo(degree, 0, 360);
		}
		return degree;
	}

	/**
	 * Replies the specified angle translated between -180 and 180.
	 * 
	 * @param degree
	 *            is an angle
	 * @return normalized angle
	 * @deprecated see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	public static double clampDegreeMinus180To180(double degree) {
		if ((!Double.isNaN(degree)) && (!Double.isInfinite(degree))) {
			return clampTrigo(degree, -180, 180);
		}
		return degree;
	}

	/** Clamp the given value to fit between the min and max values
	 * according to a cyclic heuristic.
	 * If the given value is not between the minimum and maximum
	 * values, the replied value
	 * is modulo the min-max range.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return the clamped value
	 */
	public static double clampCyclic(double value, double min, double max) {
		if (Double.isNaN(max)) { // NaN is lower than all the number according to double.compareTo()
			return Double.NaN;
		}
		if (Double.isNaN(min)) {
			// Clamp max only
			if (value>max) return max - Double.MAX_VALUE + value;
		}
		else {
			assert(min<=max);
			if (min==max) return min; // special case: empty interval
			if (value<min || value >max) {
				double perimeter = max - min;
				double nvalue = value - min;
				double rest = nvalue % perimeter;
				return (value<0) ? max+rest : rest+min;
			}
		}
		return value;
	}

	/** Clamp the given value to fit between the min and max values
	 * according to a cyclic heuristic.
	 * If the given value is not between the minimum and maximum
	 * values, the replied value
	 * is modulo the min-max range.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return the clamped value
	 * @deprecated see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	public static double clampTrigo(double value, double min, double max) {
		return clampCyclic(value, min, max);
	}

	/**
	 * Compute the signed angle between two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 * @deprecated see {@link Vector2f#signedAngle(double, double, double, double)}
	 */
	@Deprecated
	public static double signedAngle(double x1, double y1, double x2, double y2) {
		return Vector2f.signedAngle(x1, y1, x2, y2);
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (p2.x-p1.x;p2.y-p1.y).
	 *
	 * @param p1 first point.
	 * @param p2 second point.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 * @deprecated see {@link #angleOfVector(double, double, double, double)}
	 */
	@Deprecated
	public static double angleOfVector( Point2D p1, Point2D p2 ) {
		return signedAngle(
				1, 0, 
				p2.getX() - p1.getX(),
				p2.getY() - p1.getY());
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (p2.x-p1.x;p2.y-p1.y).
	 *
	 * @param p1 first point.
	 * @param p2 second point.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 * @deprecated see {@link #angleOfVector(Point2D, Point2D)} 
	 */
	@Deprecated
	public static double angleOfVector( org.arakhne.afc.math.generic.Point2D p1, org.arakhne.afc.math.generic.Point2D p2 ) {
		return signedAngle(
				1, 0, 
				p2.getX() - p1.getX(),
				p2.getY() - p1.getY());
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x2-x1;y2-y1).
	 *
	 * @param x1 is the coordinate of the vector origin point.
	 * @param y1 is the coordinate of the vector origin point.
	 * @param x2 is the coordinate of the vector target point.
	 * @param y2 is the coordinate of the vector target point.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 * @deprecated see {@link Vector2f#angleOfVector(double, double, double, double)}
	 */
	@Deprecated
	public static double angleOfVector(double x1, double y1, double x2, double y2) {
		return Vector2f.angleOfVector(x1, y1, x2, y2);
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x;y).
	 *
	 * @param x is the coordinate of the vector.
	 * @param y is the coordinate of the vector.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 * @deprecated see {@link Vector2f#angleOfVector(double, double)}
	 */
	@Deprecated
	public static double angleOfVector(double x, double y) {
		return Vector2f.angleOfVector(x, y);
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 horizontal location of the first-segment begining.
	 * @param y1 vertical location of the first-segment ending.
	 * @param x2 horizontal location of the second-segment begining.
	 * @param y2 vertical location of the second-segment ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 * @deprecated see {@link Segment2f#isPointClosedToSegment(double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isPointClosedToSegment( double x1, double y1, 
			double x2, double y2, 
			double x, double y, double hitDistance ) {
		return Segment2f.isPointClosedToSegment(x1, y1, x2, y2, x, y, hitDistance);
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 horizontal location of the first-line begining.
	 * @param y1 vertical location of the first-line ending.
	 * @param x2 horizontal location of the second-line begining.
	 * @param y2 vertical location of the second-line ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 * @deprecated see {@link Segment2f#isPointClosedToLine(double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isPointClosedToLine( double x1, double y1, 
			double x2, double y2, 
			double x, double y, double hitDistance ) {
		return Segment2f.isPointClosedToLine(x1, y1, x2, y2, x, y, hitDistance);
	}

	/** Replies the meters from inches.
	 *
	 * @param i the inch value
	 * @return a value in centimeters
	 * @deprecated see {@link MeasureUnitUtil#inchToMetric(double)}.
	 */
	@Deprecated
	public static double inchToMetric( double i ) {
		return MeasureUnitUtil.inchToMetric(i);
	}

	/** Replies the inches from metrics.
	 *
	 * @param m the metric value
	 * @return a value in inches
	 * @deprecated see {@link MeasureUnitUtil#metricToInch(double)}
	 */
	@Deprecated
	public static double metricToInch( double m ) {
		return m * 0.3937f ;
	}

	/** Replies the <var>value</var> clamped in
	 * the specified interval assuming the
	 * it is a angle in radians.
	 *
	 * @param value is the value to clamp.
	 * @param min is the minimal allowed value.
	 * @param max is the maximal allowed value.
	 * @return the clamped value.
	 * @deprecated see {@link #clampRadian(double, double, double)}
	 */
	@Deprecated
	public static double clampAngle( double value, double min, double max ) {
		return clampRadian(value, min, max);
	}

	/** Replies the <var>value</var> clamped in
	 * the specified interval assuming the
	 * it is a angle in radians.
	 *
	 * @param value is the value to clamp.
	 * @param min is the minimal allowed value.
	 * @param max is the maximal allowed value.
	 * @return the clamped value.
	 * @deprecated see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	public static double clampRadian( double value, double min, double max ) {
		assert(min<=max);
		double v = value;
		while (v>max) {
			v -= 2*Math.PI;
		}
		if (v<min) return min;
		return v;
	}

	/** Replies the <var>value</var> clamped to
	 * the nearest bounds.
	 * If |<var>value</var>-<var>minBounds</var>| &gt;
	 * |<var>value</var>-<var>maxBounds</var>| then the
	 * returned value is <var>maxBounds</var>; otherwise
	 * it is <var>minBounds</var>.
	 *
	 * @param value is the value to clamp.
	 * @param minBounds is the minimal allowed value.
	 * @param maxBounds is the maximal allowed value.
	 * @return <var>minBounds</var> or <var>maxBounds</var>.
	 */
	public static double clampToNearestBounds( double value, double minBounds, double maxBounds ) {
		assert(minBounds<=maxBounds);
		double center = (minBounds+maxBounds) / 2f;
		if (value<=center) return minBounds;
		return maxBounds;
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 * @since 3.0
	 * @deprecated see {@link Segment2f#computeSegmentSegmentIntersectionFactor(double, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static double getSegmentSegmentIntersectionFactor(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return Segment2f.computeSegmentSegmentIntersectionFactor(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	/** Compute the determinant of two vectors.
	 * <p>
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>. 
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the determinant
	 * @deprecated see {@link Vector2f#determinant(double, double, double, double)}
	 */
	@Deprecated
	public static double determinant(double x1, double y1, double x2, double y2) {
		return Vector2f.determinant(x1, y1, x2, y2);
	}

	/**
	 * Replies the intersection point between two segments.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @deprecated see {@link Segment2f#computeSegmentSegmentIntersection(double, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static Point2D getSegmentSegmentIntersectionPoint(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return Segment2f.computeSegmentSegmentIntersection(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	/** Compute the interpolate point between the two points.
	 * 
	 * @param p1
	 * @param p2
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 * @deprecated see {@link #interpolate(double, double, double, double, double)}
	 */
	@Deprecated
	public static Point2D interpolate(Point2D p1, Point2D p2, double factor) {
		return interpolate(p1.getX(), p1.getY(), p2.getX(), p2.getY(), factor);
	}

	/** Compute the interpolate point between the two points.
	 * 
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 * @deprecated see {@link Segment2f#interpolate(double, double, double, double, double)}
	 */
	@Deprecated
	public static Point2D interpolate(double p1x, double p1y, double p2x, double p2y, double factor) {
		double f = (factor<0f) ? 0f : factor;
		if (f>1f) f = 1f;
		double vx = p2x - p1x;
		double vy = p2y - p1y;
		return new Point2f(
				p1x + factor * vx,
				p1y + factor * vy);
	}

	/** Compute the dot product of two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the dot product.
	 * @deprecated see {@link Vector2f#dotProduct(double, double, double, double)}
	 */
	@Deprecated
	public static double dotProduct(double x1, double y1, double x2, double y2) {
		return Vector2f.dotProduct(x1, y1, x2, y2);
	}

	/**
	 * Compute the dot product of two 3D vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the dot product.
	 * @deprecated see {@link Vector3f#dotProduct(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static double dotProduct(double x1, double y1, double z1, double x2,
			double y2, double z2) {
		return Vector3f.dotProduct(x1, y1, z1, x2, y2, z2);
	}

	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where 
	 * the specified point {@code (px,py)} lies with respect to the line segment from {@code (x1,y1)}
	 *  to {@code (x2,y2)}. The return value can be either 1, -1, or 0 and indicates in which 
	 *  direction the specified line must pivot around its first end point, {@code (x1,y1)}, in 
	 *  order to point at the specified point {@code (px,py)}.
	 * In other words, given three point P1, P2, and P, is the segments (P1-P2-P) a counterclockwise turn?
	 * <p>
	 * In opposite to {@link #sidePointLine(double, double, double, double, double, double, boolean)},
	 * this function tries to classifies the point if it is colinear to the segment.
	 * The classification is explained below.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the direction that takes the 
	 * positive X axis towards the negative Y axis. In the default coordinate system used by Java 2D, 
	 * this direction is counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the direction that takes the 
	 * positive X axis towards the positive Y axis. In the default coordinate system, this 
	 * direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line segment. 
	 * Note that an indicator value of 0 is rare and not useful for determining colinearity 
	 * because of floating point rounding issues.
	 * <p>
	 * If the point is colinear with the line segment, but not between the end points, then the value will be 
	 * -1 if the point lies "beyond {@code (x1,y1)}" or 1 if the point lies "beyond {@code (x2,y2)}".
	 * 
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param approximateZero
	 *            indicates if zero may be approximated or not.
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see #relativeDistancePointToLine(double, double, double, double, double, double)
	 * @see #sidePointLine(double, double, double, double, double, double, boolean)
	 * @deprecated see {@link Segment2f#ccw(double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static int ccw(double x1, double y1, double x2, double y2, double px, double py, boolean approximateZero) {
		return Segment2f.ccw(x1, y1, x2, y2, px, py, approximateZero ? EPSILON : 0.);
	}

	/**
	 * Replies on which side of a line the given point is located.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the direction
	 * that takes the positive X axis towards the negative Y axis. In the default
	 * coordinate system used by Java 2D, this direction is counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the direction that takes the positive X axis towards the positive Y axis. In the default coordinate system, this direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line segment. Note that an indicator value of 0 is rare and not useful for determining colinearity because of floating point rounding issues.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link #EPSILON}.
	 * <p>
	 * In opposite of {@link #ccw(double, double, double, double, double, double, boolean)},
	 * this function does not try to classify the point if it is colinear
	 * to the segment. If the point is colinear, O is always returns. 
	 * 
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param approximateZero
	 *            indicates if zero may be approximated or not.
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see #relativeDistancePointToLine(double, double, double, double, double, double)
	 * @see #isEpsilonZero(double)
	 * @see #ccw(double, double, double, double, double, double, boolean)
	 * @deprecated see {@link Segment2f#computeSideLinePoint(double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static int sidePointLine(double x1, double y1, double x2, double y2, double px, double py, boolean approximateZero) {
		return Segment2f.computeSideLinePoint(x1, y1, x2, y2, px, py, approximateZero ? EPSILON : 0.);
	}

	/**
	 * Replies the relative distance from the given point to the given line.
	 * The replied distance may be negative, depending on which side of 
	 * the line the point is.
	 * 
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @return the positive or negative distance from the point to the line
	 * @see #ccw(double, double, double, double, double, double, boolean)
	 * @see #sidePointLine(double, double, double, double, double, double, boolean)
	 * @see #distancePointToLine(double, double, double, double, double, double)
	 * @deprecated see {@link Segment2f#computeRelativeDistanceLinePoint(double, double, double, double, double, double)}.
	 */
	@Deprecated
	public static double relativeDistancePointToLine(double x1, double y1, double x2, double y2, double px, double py) {
		return Segment2f.computeRelativeDistanceLinePoint(x1, y1, x2, y2, px, py);
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 * 
	 * @param p1 is a point of the first line.
	 * @param v1 is the direction of the first line.
	 * @param p2 is a point of the second line.
	 * @param v2 is the direction of the second line.
	 * @return the intersection point or <code>null</code> if there is no intersection.
	 * @deprecated see {@link Segment2f#computeLineLineIntersection(double, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static Point2D computeLineIntersection(Point2D p1, Vector2D v1, Point2D p2, Vector2D v2) {
		return Segment2f.computeLineLineIntersection(
				p1.getX(), p1.getY(),
				p1.getX() + v1.getX(), p1.getY() + v1.getY(),
				p2.getX(), p2.getY(),
				p2.getX() + v2.getX(), p2.getY() + v2.getY());
	}

	/**
	 * Replies the projection a point on a segment.
	 * 
	 * @param px
	 *            is the coordiante of the point to project
	 * @param py
	 *            is the coordiante of the point to project
	 * @param s1x
	 *            is the x-coordinate of the first line point.
	 * @param s1y
	 *            is the y-coordinate of the first line point.
	 * @param s2x
	 *            is the x-coordinate of the second line point.
	 * @param s2y
	 *            is the y-coordinate of the second line point.
	 * @return the projection of the specified point on the line. If 
	 * equal to {@code 0}, the projection is equal to the first segment point. 
	 * If equal to {@code 1}, the projection is equal to the second segment point. 
	 * If inside {@code ]0;1[}, the projection is between the two segment points. 
	 * If inside {@code ]-inf;0[}, the projection is outside on the side of the 
	 * first segment point. If inside {@code ]1;+inf[}, the projection is 
	 * outside on the side of the second segment point.
	 * @deprecated see {@link Segment2f#computeProjectedPointOnLine(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static double projectsPointOnLine(double px, double py, double s1x, double s1y, double s2x, double s2y) {
		return Segment2f.computeProjectedPointOnLine(px, py, s1x, s1y, s2x, s2y);
	}

	/**
	 * Replies the position factory for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 * @deprecated see {@link Segment2f#computeLineLineIntersectionFactor(double, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static double getLineLineIntersectionFactor(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return Segment2f.computeLineLineIntersectionFactor(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	/**
	 * Replies if the given point is inside the given ellipse.
	 * 
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param ellx is the min corner of the ellipse.
	 * @param elly is the min corner of the ellipse.
	 * @param ellw is the width of the ellipse.
	 * @param ellh is the height of the ellipse.
	 * @return <code>true</code> if the point is inside the ellipse;
	 * <code>false</code> if not.
	 * @deprecated see {@link Ellipse2f#containsEllipsePoint(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isPointInEllipse(double px, double py, double ellx, double elly, double ellw, double ellh) {
		return Ellipse2f.containsEllipsePoint(ellx, elly, ellw, ellh, px, py);
	}

	/**
	 * Replies if the given point is inside the given ellipse.
	 * 
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @return <code>true</code> if the point is inside the circle;
	 * <code>false</code> if not.
	 * @deprecated see {@link Circle2f#containsCirclePoint(double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isPointInCircle(double px, double py, double cx, double cy, double radius) {
		return Circle2f.containsCirclePoint(cx, cy, radius, px, py);
	}

	/** Replies the closest point from the given point in the solid ellipse.
	 * A solid ellipse is an ellipse with a border and an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @return the closest point in the ellipse
	 * @see #getClosestPointToSolidEllipse(double, double, double, double, double, double, boolean)
	 * @see #getClosestPointToShallowEllipse(double, double, double, double, double, double)
	 * @deprecated see {@link Ellipse2f#computeClosestPointToSolidEllipse(double, double, double, double, double, double, boolean)}
	 */
	@Deprecated
	public static Point2D getClosestPointToSolidEllipse(double px, double py, double ex, double ey, double ew, double eh) {
		return getClosestPointToSolidEllipse(px, py, ex, ey, ew, eh, false);
	}

	/** Replies the closest point from the given point in the solid ellipse.
	 * A solid ellipse is an ellipse with a border and an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @param returnNullWhenInside indicates if a <code>null</code> value
	 * may be returned when the point is inside the ellipse, if
	 * <code>true</code>; or a point all the time if <code>false</code>.
	 * @return the closest point in the ellipse
	 * @see #getClosestPointToShallowEllipse(double, double, double, double, double, double)
	 * @deprecated see {@link Ellipse2f#computeClosestPointToSolidEllipse(double, double, double, double, double, double, boolean)}
	 */
	@Deprecated
	public static Point2D getClosestPointToSolidEllipse(double px, double py, double ex, double ey, double ew, double eh, boolean returnNullWhenInside) {
		return Ellipse2f.computeClosestPointToSolidEllipse(px, py, ex, ey, ew, eh, returnNullWhenInside);
	}

	/** Replies the closest point from the given point in the shallow ellipse.
	 * A shallow ellipse is an ellipse with a border and not an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @return the closest point in the ellipse, or <code>null</code> if 
	 * the given point is exactly at the center of the ellipse.
	 * @deprecated see {@link Ellipse2f#computeClosestPointToShallowEllipse(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static Point2D getClosestPointToShallowEllipse(double px, double py, double ex, double ey, double ew, double eh) {
		return Ellipse2f.computeClosestPointToShallowEllipse(px, py, ex, ey, ew, eh);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are colinear, see {@link #isCollinearLines(double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(double, double, double, double, double, double, double, double)
	 * @since 3.0
	 * @deprecated see {@link Segment2f#isParallelLines(double, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isParallelLines(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return Segment2f.isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * Line through (x<sub>0</sub>,y<sub>0</sub>,z<sub>0</sub>) in direction (a<sub>0</sub>,b<sub>0</sub>,c<sub>0</sub>) and line through (x<sub>1</sub>,yx<sub>1</sub>,zx<sub>1</sub>) in direction (ax<sub>1</sub>,bx<sub>1</sub>,cx<sub>1</sub>): <center><img src="doc-files/parallellines3d.gif" alt="Parallel lines"></center>
	 * <p>
	 * Two lines specified by point and direction are coplanar if and only if the determinant in the numerator is zero. In this case they are concurrent (if the denominator is nonzero) or parallel (if the denominator is zero).
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @since 3.0
	 * @deprecated no replacement, see {@link Vector3f#isCollinearVectors(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isParallelLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		return isCollinearVectors(x2 - x1, y2 - y1, z2 - z1, x4 - x3, y4 - y3, z4 - z3);
	}

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link #EPSILON}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 * @see #isEpsilonZero(double)
	 * @deprecated see {@link Point2f#isCollinearPoints(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isCollinearPoints(double x1, double y1, double x2, double y2, double x3, double y3) {
		// Test if three points are colinears
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computations.
		return isEpsilonZero(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
	}

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link #EPSILON}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param z1
	 *            is the Z coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param z2
	 *            is the Z coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @param z3
	 *            is the Z coordinate of the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 * @see #isEpsilonZero(double)
	 * @deprecated see {@link Point3f#isCollinearPoints(double, double, double, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isCollinearPoints(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
		return Point3f.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3, EPSILON);
	}

	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link #EPSILON}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 3.0
	 * @see #isEpsilonZero(double)
	 * @deprecated see {@link Vector2f#isCollinearVectors(double, double, double, double)}
	 */
	@Deprecated
	public static boolean isCollinearVectors(double x1, double y1, double x2, double y2) {
		return Vector2f.isCollinearVectors(x1, y1, x2, y2);
	}

	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link #EPSILON}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param z1
	 *            is the Z coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @param z2
	 *            is the Z coordinate of the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 3.0
	 * @see #isEpsilonZero(double)
	 * @deprecated see {@link Vector3f#isCollinearVectors(double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isCollinearVectors(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Vector3f.isCollinearVectors(x1, y1, z1, x2, y2, z2);
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double)
	 * @see #isCollinearPoints(double, double, double, double, double, double)
	 * @deprecated see {@link Segment2f#isCollinearLines(double, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public static boolean isCollinearLines(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return Segment2f.isCollinearLines(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(double, double, double, double, double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double, double, double, double, double)
	 * @see #isCollinearPoints(double, double, double, double, double, double, double, double, double)
	 * @deprecated no replacement.
	 */
	@Deprecated
	public static boolean isCollinearLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		return (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4) && isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3));
	}

	/** Compute the zone where the point is against the given rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param px is the coordinates of the points.
	 * @param py is the coordinates of the points.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return the zone
	 */
	public static int getCohenSutherlandCode(int px, int py, int rxmin, int rymin, int rxmax, int rymax) {
		assert(rxmin<=rxmax);
		assert(rymin<=rymax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px<rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px>rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py<rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py>rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		return code;
	}

	/** Compute the zone where the point is against the given rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param px is the coordinates of the points.
	 * @param py is the coordinates of the points.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return the zone
	 */
	public static int getCohenSutherlandCode(double px, double py, double rxmin, double rymin, double rxmax, double rymax) {
		assert(rxmin<=rxmax);
		assert(rymin<=rymax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px<rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px>rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py<rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py>rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		return code;
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 * @deprecated see {@link Segment2f#clipToRectangle(double, double, double, double)}
	 */
	@Deprecated
	public static boolean clipSegmentToRectangle(Point2D p1, Point2D p2, double rxmin, double rymin, double rxmax, double rymax) {
		Segment2f r = new Segment2f(p1, p2);
		return r.clipToRectangle(rxmin, rymin, rxmax, rymax);
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 * @deprecated see {@link #clipSegmentToRectangle(Point2D, Point2D, double, double, double, double)}
	 */
	@Deprecated
	public static boolean clipSegmentToRectangle(org.arakhne.afc.math.generic.Point2D p1,
			org.arakhne.afc.math.generic.Point2D p2, double rxmin, double rymin, double rxmax, double rymax) {
		double x0 = p1.getX();
		double y0 = p1.getY();
		double x1 = p2.getX();
		double y1 = p2.getY();
		int code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		double x, y;
		x = y = 0;

		while (cont) {
			if ((code1 | code2)==0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			}
			else if ((code1 & code2)!=0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			}
			else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge

				// At least one endpoint is outside the clip rectangle; pick it.
				int code3 = code1!=0 ? code1 : code2;

				// Now find the intersection point;
				// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
				if ((code3 & COHEN_SUTHERLAND_TOP)!=0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				}
				else if ((code3 & COHEN_SUTHERLAND_BOTTOM)!=0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				}
				else if ((code3 & COHEN_SUTHERLAND_RIGHT)!=0) { 
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				}
				else if ((code3 & COHEN_SUTHERLAND_LEFT)!=0) {
					// point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (rxmin - x0) / (x1 - x0);
					x = rxmin;
				}
				else {
					code3 = 0;
				}

				if (code3!=0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = x;
						y0 = y;
						code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					}
					else {
						x1 = x;
						y1 = y;
						code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			p1.set((double) x0, (double) y0);
			p2.set((double) x1, (double) y1);
		}
		return accept;
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 * @deprecated see {@link Segment2i#clipToRectangle(int, int, int, int)}
	 */
	@Deprecated
	public static boolean clipSegmentToRectangle(Point2D p1, Point2D p2, int rxmin, int rymin, int rxmax, int rymax) {
		Segment2i s = new Segment2i(p1, p2);
		return s.clipToRectangle(rxmin, rymin, rxmax, rymax);
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 * @deprecated see {@link #distanceSquaredPointToSegment(double, double, double, double, double, double, Point2D)}
	 */
	@Deprecated
	public static double distanceSquaredPointToSegment(double px, double py, double x1, double y1, double x2, double y2,
			org.arakhne.afc.math.generic.Point2D pts) {
		double r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0f) return distanceSquaredPointToPoint(px, py, x1, y1);
		double r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		double ratio = r_numerator / r_denomenator;

		if (ratio<=0f) {
			if (pts!=null) pts.set((double) x1, (double) y1);
			return Math.abs((px-x1)*(px-x1) + (py-y1)*(py-y1));
		}

		if (ratio>=1f) {
			if (pts!=null) pts.set((double) x2, (double) y2);
			return Math.abs((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}

		if (pts!=null) pts.set(
				(double) (ratio * (x2-x1)),
				(double) (ratio * (y2-y1)));

		double s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}

	/** Compute the interpolate point between the two points.
	 * 
	 * @param p1
	 * @param p2
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 * @deprecated se {@link #interpolate(Point2D, Point2D, double)}
	 */
	@Deprecated
	public static org.arakhne.afc.math.generic.Point2D interpolate(org.arakhne.afc.math.generic.Point2D p1,
			org.arakhne.afc.math.generic.Point2D p2, double factor) {
		throw new UnsupportedOperationException();
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 * 
	 * @param p1 is a point of the first line.
	 * @param v1 is the direction of the first line.
	 * @param p2 is a point of the second line.
	 * @param v2 is the direction of the second line.
	 * @return the intersection point or <code>null</code> if there is no intersection.
	 * @deprecated see {@link #computeLineIntersection(Point2D, Vector2D, Point2D, Vector2D)}
	 */
	@Deprecated
	public static Point2D computeLineIntersection(org.arakhne.afc.math.generic.Point2D p1,
			org.arakhne.afc.math.generic.Vector2D v1, org.arakhne.afc.math.generic.Point2D p2,
			org.arakhne.afc.math.generic.Vector2D v2) {
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = x1 + v1.getX();
		double y2 = y1 + v1.getY();
		double x3 = p2.getX();
		double y3 = p2.getY();
		double x4 = x3 + v2.getX();
		double y4 = y3 + v2.getY();
		double denom = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		if (denom==0.) return null;
		double ua = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3));
		double ub = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3));
		if (ua==ub) return null;
		ua = ua / denom;
		return new Point2f(
				x1 + ua * (x2 - x1),
				y1 + ua * (y2 - y1));
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 * @deprecated see {@link #clipSegmentToRectangle(Point2D, Point2D, int, int, int, int)}
	 */
	@Deprecated
	public static boolean clipSegmentToRectangle(org.arakhne.afc.math.generic.Point2D p1,
			org.arakhne.afc.math.generic.Point2D p2, int rxmin, int rymin, int rxmax, int rymax) {
		int x0 = p1.x();
		int y0 = p1.y();
		int x1 = p2.x();
		int y1 = p2.y();
		int code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		int x, y;
		x = y = 0;

		while (cont) {
			if ((code1 | code2)==0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			}
			else if ((code1 & code2)!=0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			}
			else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge

				// At least one endpoint is outside the clip rectangle; pick it.
				int code3 = code1!=0 ? code1 : code2;

				// Now find the intersection point;
				// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
				if ((code3 & COHEN_SUTHERLAND_TOP)!=0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				}
				else if ((code3 & COHEN_SUTHERLAND_BOTTOM)!=0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				}
				else if ((code3 & COHEN_SUTHERLAND_RIGHT)!=0) { 
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				}
				else if ((code3 & COHEN_SUTHERLAND_LEFT)!=0) {
					// point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (rxmin - x0) / (x1 - x0);
					x = rxmin;
				}
				else {
					code3 = 0;
				}

				if (code3!=0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = x;
						y0 = y;
						code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					}
					else {
						x1 = x;
						y1 = y;
						code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			p1.set(x0, y0);
			p2.set(x1, y1);
		}
		return accept;
	}

	/** Determine the min and max values from a set of three values.
	 * <p>
	 * This function has an algorithm that is efficient for 3 values.
	 *
	 * @param a the first value.
	 * @param b the second value.
	 * @param c the third value.
	 * @param minmax the pair with the min and max values.
	 * @since 13.0
	 */
	public static void getMinMax(double a, double b, double c, Pair<Double, Double> minmax) {
		// Efficient implementation of the min/max determination
		double min, max;
		
		// ---------------------------------
		// Table of cases
		// ---------------------------------
		// a-b a-c b-c sequence min max case
		//  <   <   <  a b c     a   c   1
		//  <   <   >  a c b     a   b   2
		//  <   >   <            -   -
		//  <   >   >  c a b     c   b   3
		//  >   <   <  b a c     b   c   4
		//  >   <   >            -   -
		//  >   >   <  b c a     b   a   5
		//  >   >   >  c b a     c   a   6
		// ---------------------------------
		
		if (a <= b) {
			// case candidates: 123
			if (a <= c) {
				// case candidates: 12
				min = a;
				if (b <= c) {
					// case: 1
					max = c;
				} else {
					// case: 2
					max = b;
				}
			} else {
				// 3
				min = c;
				max = b;
			}
		} else {
			// case candidates: 456
			if (a <= c) {
				// case: 4
				min = b;
				max = c;
			} else {
				// case candidates: 56
				max = a;
				if (b <= c) {
					// case: 5
					min = b;
				} else {
					// case: 6
					min = c;
				}
			}
		}
		minmax.set(min, max);
	}

	/** Replies the cosecant of the specified angle.
	 * <p>
	 * <code>csc(a) = 1/sin(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Cosecant function]">
	 * <img src="./doc-files/trigo3.png" alt="[Cosecant function]">
	 * 
	 * @param angle
	 * @return the cosecant.
	 */
	public static double csc(double angle) {
		return 1./Math.sin(angle);
	}

	/** Replies the secant of the specified angle.
	 * <p>
	 * <code>csc(a) = 1/cos(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Secant function]">
	 * <img src="./doc-files/trigo2.png" alt="[Secant function]">
	 * 
	 * @param angle
	 * @return the secant.
	 */
	public static double sec(double angle) {
		return 1./Math.cos(angle);
	}

	/** Replies the cotangent of the specified angle.
	 * <p>
	 * <code>csc(a) = 1/tan(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Cotangent function]">
	 * <img src="./doc-files/trigo3.png" alt="[Cotangent function]">
	 * 
	 * @param angle
	 * @return the cotangent.
	 */
	public static double cot(double angle) {
		return 1./Math.tan(angle);
	}

	/** Replies the versine of the specified angle.
	 * <p>
	 * <code>versin(a) = 1 - cos(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Versine function]">
	 * 
	 * @param angle
	 * @return the cotangent.
	 */
	public static double versin(double angle) {
		return 1. - Math.cos(angle);
	}

	/** Replies the exsecant of the specified angle.
	 * <p>
	 * <code>exsec(a) = sec(a) - 1</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Exsecant function]">
	 * 
	 * @param angle
	 * @return the cotangent.
	 */
	public static double exsec(double angle) {
		return sec(angle) - 1.;
	}

	/** Replies the chord of the specified angle.
	 * <p>
	 * <code>crd(a) = 2 sin(a/2)</code>
	 * <p>
	 * <img src="./doc-files/chord.png" alt="[Chord function]">
	 * 
	 * @param angle
	 * @return the chord.
	 */
	public static double crd(double angle) {
		return 2. * Math.sin(angle/2.);
	}

	/** Replies the half of the versine (aka, haversine) of the specified angle.
	 * <p>
	 * <code>haversine(a) = sin<sup>2</sup>(a/2) = (1-cos(a)) / 2</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Excosecant function]">
	 * 
	 * @param angle
	 * @return the chord.
	 */
	public static double haversine(double angle) {
		double sin2 = Math.sin(angle/2.);
		return sin2*sin2;
	}

}
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

import java.util.Arrays;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.EulerAngle;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Vector2D;
import org.arakhne.afc.math.generic.Vector3D;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.math.object.Direction2D;
import org.arakhne.afc.math.object.Direction3D;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.plane.Plane4f;
import org.arakhne.afc.math.system.CoordinateSystem2D;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.math.transform.Transform3D;
import org.arakhne.afc.util.OutputParameter;
import org.arakhne.afc.util.Pair;

/** Mathematic and geometric utilities.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MathUtil implements MathConstants {
	
	private static float RADIAN_PRECISION = MathConstants.PI / 100.f;

	private static float TRIGO_PRECISION = 0.001f;

	/**
	 * The default distance precision is the millimeter if the unit is for meter.
	 */
	private static float DISTANCE_PRECISION = 0.001f;

	/**
	 * The default floating point precision.
	 */
	private static float FLOAT_PRECISION = 0.0000001f;

	/**
	 * The default floating point precision.
	 */
	private static float DOUBLE_PRECISION = 10e-12f;

	/** Clamp the given angle in radians to {@code [0;2PI)}.
	 * 
	 * @param radians is the angle to clamp
	 * @return the angle in {@code [0;2PI)} range.
	 */
	public static float clampRadian(float radians) {
		float r = radians;
		while (r<0f) r += TWO_PI;
		while (r>=TWO_PI) r -= TWO_PI;
		return r;
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
	public static float clamp(float v, float min, float max) {
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
	 */
	public static boolean isEpsilonZero(float value) {
		return Math.abs(value) <= EPSILON;
	}

	/** Replies if the given values are near.
	 * 
	 * @param v1
	 * @param v2
	 * @return <code>true</code> if the given <var>v1</var>
	 * is near <var>v2</var>, otherwise <code>false</code>.
	 */
	public static boolean isEpsilonEqual(float v1, float v2) {
		return Math.abs(v1 - v2) <= EPSILON;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static float max(float... values) {
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

	/** Compute the distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @return the distance beetween the point and the segment.
	 */
	public static final float distancePointToSegment(float px, float py, float x1, float y1, float x2, float y2) {
		return distancePointToSegment(px, py, x1, y1, x2, y2, null);
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
	 */
	public static final float distanceSquaredPointToSegment(float px, float py, float x1, float y1, float x2, float y2) {
		return distanceSquaredPointToSegment(px, py, x1, y1, x2, y2, null);
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
	 */
	public static final float distancePointToSegment(float px, float py, float x1, float y1, float x2, float y2, Point2D pts) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distancePointToPoint(px, py, x1, y1);
		float r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		float ratio = r_numerator / r_denomenator;

		if (ratio<=0.) {
			if (pts!=null) pts.set(x1, y1);
			return (float)Math.sqrt((px-x1)*(px-x1) + (py-y1)*(py-y1));
		}

		if (ratio>=1.) {
			if (pts!=null) pts.set(x2, y2);
			return (float)Math.sqrt((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}

		if (pts!=null) pts.set(
				ratio * (x2-x1),
				ratio * (y2-y1));

		float s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (float)(Math.abs(s) * Math.sqrt(r_denomenator));
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
	 */
	public static final float distanceSquaredPointToSegment(float px, float py, float x1, float y1, float x2, float y2, Point2D pts) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0f) return distanceSquaredPointToPoint(px, py, x1, y1);
		float r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		float ratio = r_numerator / r_denomenator;

		if (ratio<=0f) {
			if (pts!=null) pts.set(x1, y1);
			return Math.abs((px-x1)*(px-x1) + (py-y1)*(py-y1));
		}

		if (ratio>=1f) {
			if (pts!=null) pts.set(x2, y2);
			return Math.abs((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}

		if (pts!=null) pts.set(
				ratio * (x2-x1),
				ratio * (y2-y1));

		float s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
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
	 * @see #distanceSquaredPointToLine(float, float, float, float, float, float)
	 * @see #relativeDistancePointToLine(float, float, float, float, float, float)
	 */
	public static final float distancePointToLine(float px, float py, float x1, float y1, float x2, float y2) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distancePointToPoint(px, py, x1, y1);
		float s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (float)(Math.abs(s) * Math.sqrt(r_denomenator));
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
	 * @see #distancePointToLine(float, float, float, float, float, float)
	 */
	public static final float distanceSquaredPointToLine(float px, float py, float x1, float y1, float x2, float y2) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distanceSquaredPointToPoint(px, py, x1, y1);
		float s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}

	/** Compute the distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distanceSquaredPointToPoint(float, float, float, float)
	 */
	public static final float distancePointToPoint(float x1, float y1, float x2, float y2) {
		float dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}

	/** Compute the squared distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointToPoint(float, float, float, float)
	 */
	public static final float distanceSquaredPointToPoint(float x1, float y1, float x2, float y2) {
		float dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return dx*dx+dy*dy;
	}

	/**
	 * Replies the specified angle translated between 0 and 2PI.
	 * 
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 */
	public static float clampRadian0To2PI(float radian) {
		if ((!Float.isNaN(radian)) && (!Float.isInfinite(radian))) {
			return clampTrigo(radian, 0, 2f*PI);
		}
		return radian;
	}

	/**
	 * Replies the specified angle translated between -PI and PI.
	 * 
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 */
	public static float clampRadianMinusPIToPI(float radian) {
		if ((!Float.isNaN(radian)) && (!Float.isInfinite(radian))) {
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
	 */
	public static float clampDegree0To360(float degree) {
		if ((!Float.isNaN(degree)) && (!Float.isInfinite(degree))) {
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
	 */
	public static float clampDegreeMinus180To180(float degree) {
		if ((!Float.isNaN(degree)) && (!Float.isInfinite(degree))) {
			return clampTrigo(degree, -180, 180);
		}
		return degree;
	}

	/** Clamp the given value to fit between the min and max values
	 * according to a trigonometric-like heuristic.
	 * If the given value is not between the minimum and maximum
	 * values and the value is positive, the value is modulo the perimeter
	 * of the counterclockwise circle.
	 * If the given value is not between the minimum and maximum
	 * values and the value is negative, the value is modulo the perimeter
	 * of the clockwise circle.
	 * The perimeter is the distance between <var>min</var> and <var>max</var>.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return the clamped value
	 */
	public static float clampTrigo(float value, float min, float max) {
		if (Float.isNaN(max)) { // NaN is lower than all the number according to float.compareTo()
			return Float.NaN;
		}
		if (Float.isNaN(min)) {
			// Clamp max only
			if (value>max) return max - Float.MAX_VALUE + value;
		}
		else {
			assert(min<=max);
			if (min==max) return min; // special case: empty interval
			if (value<min || value >max) {
				float perimeter = max - min;
				float nvalue = value - min;
				float rest = nvalue % perimeter;
				return (value<0) ? max+rest : rest+min;
			}
		}
		return value;
	}

	/**
	 * Compute the signed angle between two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 */
	public static float signedAngle(float x1, float y1, float x2, float y2) {
		float length1 = (float)Math.sqrt(x1 * x1 + y1 * y1);
		float length2 = (float)Math.sqrt(x2 * x2 + y2 * y2);

		if ((length1 == 0f) || (length2 == 0f))
			return Float.NaN;

		float cx1 = x1;
		float cy1 = y1;
		float cx2 = x2;
		float cy2 = y2;

		// A and B are normalized
		if (length1 != 1) {
			cx1 /= length1;
			cy1 /= length1;
		}

		if (length2 != 1) {
			cx2 /= length2;
			cy2 /= length2;
		}

		/*
		 * // First method // Angle // A . B = |A|.|B|.cos(theta) = cos(theta) float dot = x1 * x2 + y1 * y2; float angle = Math.acos(dot);
		 * 
		 * // On which side of A, B is located? if ((dot > -1) && (dot < 1)) { dot = MathUtil.determinant(x2, y2, x1, y1); if (dot < 0) angle = -angle; }
		 */

		// Second method
		// A . B = |A|.|B|.cos(theta) = cos(theta)
		float cos = cx1 * cx2 + cy1 * cy2;
		// A x B = |A|.|B|.sin(theta).N = sin(theta) (where N is the unit vector perpendicular to plane AB)
		float sin = cx1 * cy2 - cy1 * cx2;

		float angle = (float)Math.atan2(sin, cos);

		return angle;
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
	 */
	public static final float angleOfVector( Point2D p1, Point2D p2 ) {
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
	 */
	public static final float angleOfVector(float x1, float y1, float x2, float y2) {
		return signedAngle(
				1, 0, 
				x2-x1, y2-y1);
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
	 */
	public static final float angleOfVector(float x, float y) {
		return signedAngle(
				1, 0, 
				x, y);
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
	 */
	public static boolean isPointClosedToSegment( float x1, float y1, 
			float x2, float y2, 
			float x, float y, float hitDistance ) {
		return ( distancePointToSegment(x, y, x1, y1, x2, y2) < hitDistance ) ;
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
	 */
	public static boolean isPointClosedToLine( float x1, float y1, 
			float x2, float y2, 
			float x, float y, float hitDistance ) {
		return ( distancePointToLine(x, y, x1, y1, x2, y2) < hitDistance ) ;
	}

	/** Replies the metrics from inches.
	 *
	 * @param i the inch value
	 * @return a value in centimeters
	 */
	public static float inchToMetric( float i ) {
		return i / 0.3937f ;
	}

	/** Replies the inches from metrics.
	 *
	 * @param m the metric value
	 * @return a value in inches
	 */
	public static float metricToInch( float m ) {
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
	 */
	public static float clampAngle( float value, float min, float max ) {
		assert(min<=max);
		float v = value;
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
	public static float clampToNearestBounds( float value, float minBounds, float maxBounds ) {
		assert(minBounds<=maxBounds);
		float center = (minBounds+maxBounds) / 2f;
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
	 * @return <code>factor1</code> or {@link Float#NaN} if no intersection.
	 * @since 3.0
	 */
	public static float getSegmentSegmentIntersectionFactor(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		float det = determinant(X1, Y1, X2, Y2);
		if (det == 0.)
			return Float.NaN;

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		float u = determinant(X1, Y1, x1 - x3, y1 - y3) / det;
		if (u < 0. || u > 1.)
			return Float.NaN;
		u = determinant(X2, Y2, x1 - x3, y1 - y3) / det;
		return (u < 0. || u > 1.) ? Float.NaN : u;
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
	 */
	public static float determinant(float x1, float y1, float x2, float y2) {
		return x1*y2 - x2*y1;
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
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the determinant
	 */
	public static float determinant(float x1, float y1, float z1, float x2, float y2, float z2) {
		/* First method:
		 * 
		 * det(A,B) = |A|.|B|.sin(theta)
		 * A x B = |A|.|B|.sin(theta).N, where N is the unit vector
		 * A x B = det(A,B).N
		 * A x B = [ y1*z2 - z1*y2 ] = det(A,B).N
		 *         [ z1*x2 - x1*z2 ]
		 *         [ x1*y2 - y1*x2 ]
		 * det(A,B) = sum(A x B)        
		 * 
		 * Second method:
		 * 
		 * det(A,B) = det( [ x1 x2 1 ]
		 *                 [ y1 y2 1 ]
		 *                 [ z1 z2 1 ] )
		 * det(A,B) = x1*y2*1 + y1*z2*1 + z1*x2*1 - 1*y2*z1 - 1*z2*x1 - 1*x2*y1
		 */
		return x1*y2 + y1*z2 + z1*x2 - y2*z1 - z2*x1 - x2*y1;
	}
	
	/** Compute the determinant of three vectors, or 3x3 matrix where
	 * columns are vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param x3
	 * @param y3
	 * @param z3
	 * @return the determinant
	 */
	public static float determinant(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3) {
		/* det(A,B,C) = det( [ x1 x2 x3 ]
		 *                   [ y1 y2 y3 ]
		 *                   [ z1 z2 z3 ] )
		 */
	    // From Matrix3f of vecmath lib.
		return
			  x1 * (y2 * z3 - y3 * z2)
			+ x2 * (y3 * z1 - y1 * z3)
			+ x3 * (y1 * z2 - y2 * z1);
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
	 */
	public static Point2D getSegmentSegmentIntersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float m = getSegmentSegmentIntersectionFactor(x1, y1, x2, y2, x3, y3, x4, y4);
		if (Float.isNaN(m))
			return null;
		return new Point2f(x1 + m * (x2 - x1), y1 + m * (y2 - y1));
	}

	/** Compute the interpolate point between the two points.
	 * 
	 * @param p1
	 * @param p2
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 */
	public static Point2D interpolate(Point2D p1, Point2D p2, float factor) {
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
	 */
	public static Point2D interpolate(float p1x, float p1y, float p2x, float p2y, float factor) {
		float f = (factor<0f) ? 0f : factor;
		if (f>1f) f = 1f;
		float vx = p2x - p1x;
		float vy = p2y - p1y;
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
	 */
	public static float dotProduct(float x1, float y1, float x2, float y2) {
		return x1*x2 + y1*y2;
	}

	/** Compute the dot-product of two vectors.
	 * <p>
	 * The specified vectors are normalized by this function.
	 * See {@link #dotProduct(float, float, float, float)}
	 * to compute the dot product without normalization of the vectors.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the dot-product (a value between <code>-1</code> and <code>1</code>.
	 */
	public static float dotProductNorm(float x1, float y1, float x2, float y2) {
		float num = (x1*x2 + y1*y2);
		float denum = (x1*x1+y1*y1)*(x2*x2+y2*y2);
		if (Double.isInfinite(num)||Double.isInfinite(denum)) return Float.NaN; // Capacity exceeded
		return (float) (num / Math.sqrt(denum));
	}
	
	/** Compute the dot-product of two vectors.
	 * <p>
	 * The specified vectors are not normalized by this function.
	 * See {@link #dotProductNorm(float, float, float, float, float, float)}
	 * to normalize the vectors because computing the dot product.
	 *
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the dot-product.
	 */
	public static float dotProduct(float x1, float y1, float z1, float x2, float y2, float z2) {
		return x1*x2 + y1*y2 + z1*z2;
	}

	/** Compute the dot-product of two vectors.
	 * <p>
	 * The specified vectors are normalized by this function.
	 * See {@link #dotProduct(float, float, float, float, float, float)}
	 * to compute the dot product without normalization of the vectors.
	 *
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the dot-product (a value between <code>-1</code> and <code>1</code>.
	 */
	public static float dotProductNorm(float x1, float y1, float z1, float x2, float y2, float z2) {
		float num = (x1*x2 + y1*y2 + z1*z2);
		float denum = (x1*x1+y1*y1+z1*z1)*(x2*x2+y2*y2+z2*z2);
		if (Double.isInfinite(num)||Double.isInfinite(denum)) return Float.NaN; // Capacity exceeded
		return (float) (num / Math.sqrt(denum));
	}
	
	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where 
	 * the specified point {@code (px,py)} lies with respect to the line segment from {@code (x1,y1)}
	 *  to {@code (x2,y2)}. The return value can be either 1, -1, or 0 and indicates in which 
	 *  direction the specified line must pivot around its first end point, {@code (x1,y1)}, in 
	 *  order to point at the specified point {@code (px,py)}.
	 * In other words, given three point P1, P2, and P, is the segments (P1-P2-P) a counterclockwise turn?
	 * <p>
	 * In opposite to {@link #sidePointLine(float, float, float, float, float, float, boolean)},
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
	 * @see #relativeDistancePointToLine(float, float, float, float, float, float)
	 * @see #sidePointLine(float, float, float, float, float, float, boolean)
	 */
	public static int ccw(float x1, float y1, float x2, float y2, float px, float py, boolean approximateZero) {
		float cx2 = x2 - x1;
		float cy2 = y2 - y1;
		float cpx = px - x1;
		float cpy = py - y1;
		float ccw = cpx * cy2 - cpy * cx2;
		if ((approximateZero && isEpsilonZero(ccw)) || (!approximateZero && ccw == 0.)) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = cpx * cx2 + cpy * cy2;
			if (ccw > 0.) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				cpx -= cx2;
				cpy -= cy2;
				ccw = cpx * cx2 + cpy * cy2;
				if (ccw < 0f) {
					ccw = 0f;
				}
			}
		}
		return (ccw < 0f) ? -1 : ((ccw > 0f) ? 1 : 0);
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
	 * In opposite of {@link #ccw(float, float, float, float, float, float, boolean)},
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
	 * @see #relativeDistancePointToLine(float, float, float, float, float, float)
	 * @see #isEpsilonZero(float)
	 * @see #ccw(float, float, float, float, float, float, boolean)
	 */
	public static int sidePointLine(float x1, float y1, float x2, float y2, float px, float py, boolean approximateZero) {
		float cx2 = x2 - x1;
		float cy2 = y2 - y1;
		float cpx = px - x1;
		float cpy = py - y1;
		float side = cpx * cy2 - cpy * cx2;
		if (approximateZero && side!=0f && isEpsilonZero(side)) {
			side = 0f;
		}
		return (side < 0f) ? -1 : ((side > 0f) ? 1 : 0);
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
	 * @see #ccw(float, float, float, float, float, float, boolean)
	 * @see #sidePointLine(float, float, float, float, float, float, boolean)
	 * @see #distancePointToLine(float, float, float, float, float, float)
	 */
	public static float relativeDistancePointToLine(float x1, float y1, float x2, float y2, float px, float py) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distancePointToPoint(px, py, x1, y1);
		float s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (float)(s * Math.sqrt(r_denomenator));
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 * 
	 * @param p1 is a point of the first line.
	 * @param v1 is the direction of the first line.
	 * @param p2 is a point of the second line.
	 * @param v2 is the direction of the second line.
	 * @return the intersection point or <code>null</code> if there is no intersection.
	 */
	public static Point2D computeLineIntersection(Point2D p1, Vector2D v1, Point2D p2, Vector2D v2) {
		float x1 = p1.getX();
		float y1 = p1.getY();
		float x2 = x1 + v1.getX();
		float y2 = y1 + v1.getY();
		float x3 = p2.getX();
		float y3 = p2.getY();
		float x4 = x3 + v2.getX();
		float y4 = y3 + v2.getY();
		float denom = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		if (denom==0.) return null;
		float ua = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3));
		float ub = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3));
		if (ua==ub) return null;
		ua = ua / denom;
		return new Point2f(
				x1 + ua * (x2 - x1),
				y1 + ua * (y2 - y1));
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
	 */
	public static float projectsPointOnLine(float px, float py, float s1x, float s1y, float s2x, float s2y) {
		float r_numerator = (px-s1x)*(s2x-s1x) + (py-s1y)*(s2y-s1y);
		float r_denomenator = (s2x-s1x)*(s2x-s1x) + (s2y-s1y)*(s2y-s1y);
		return r_numerator / r_denomenator;
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
	 * @return <code>factor1</code> or {@link Float#NaN} if no intersection.
	 */
	public static float getLineLineIntersectionFactor(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		float det = MathUtil.determinant(X1, Y1, X2, Y2);
		if (det == 0.)
			return Float.NaN;

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		return MathUtil.determinant(X2, Y2, x1 - x3, y1 - y3) / det;
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
	 */
	public static boolean isPointInEllipse(float px, float py, float ellx, float elly, float ellw, float ellh) {
		// Copied from AWT Ellipse2D

		// Normalize the coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		if (ellw <= 0f || ellh <= 0f) {
			return false;
		}
		float normx = (px - ellx) / ellw - 0.5f;
		float normy = (py - elly) / ellh - 0.5f;
		return (normx * normx + normy * normy) < 0.25f;
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
	 */
	public static boolean isPointInCircle(float px, float py, float cx, float cy, float radius) {
		return distanceSquaredPointToPoint(
				px, py,
				cx, cy) <= (radius * radius);
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
	 * @see #getClosestPointToSolidEllipse(float, float, float, float, float, float, boolean)
	 * @see #getClosestPointToShallowEllipse(float, float, float, float, float, float)
	 */
	public static Point2D getClosestPointToSolidEllipse(float px, float py, float ex, float ey, float ew, float eh) {
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
	 * @see #getClosestPointToShallowEllipse(float, float, float, float, float, float)
	 */
	public static Point2D getClosestPointToSolidEllipse(float px, float py, float ex, float ey, float ew, float eh, boolean returnNullWhenInside) {
		float x, y;
		if (ew<=0f || eh<=0f) {
			x = ex;
			y = ey;
		}
		else {
			// Normalize the coordinates compared to the ellipse
			// having a center at 0,0 and a radius of 0.5.
			float normx = (px - ex) / ew - 0.5f;
			float normy = (py - ey) / eh - 0.5f;
			if ((normx * normx + normy * normy) < 0.25f) {
				// The point is inside the ellipse
				if (returnNullWhenInside) return null;
				x = px;
				y = py;
			}
			else {
				// Compute the intersection between the ellipse
				// centered on (0;0) and the line (0;0)-(x0;y0)
				float a = ew / 2f;
				float b = eh / 2f;
				float x0 = px - (ex + a);
				float y0 = py - (ey + b);

				float denom = a*a*y0*y0 + b*b*x0*x0;
				assert(denom>0f); // because the "inside"-test should discard this case.

				denom = (float)Math.sqrt(denom);
				float factor = (a * b) / denom;
				x = factor * x0;
				y = factor * y0;

				// Translate the point to the original coordinate system
				x += (ex + a);
				y += (ey + b);
			}
		}
		return new Point2f(x, y);
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
	 */
	public static Point2D getClosestPointToShallowEllipse(float px, float py, float ex, float ey, float ew, float eh) {
		float x, y;
		if (ew<=0f || eh<=0f) {
			x = ex;
			y = ey;
		}
		else {
			// Compute the intersection between the ellipse
			// centered on (0;0) and the line (0;0)-(x0;y0)
			float a = ew / 2f;
			float b = eh / 2f;
			float x0 = px - (ex + a);
			float y0 = py - (ey + b);

			float denom = a*a*y0*y0 + b*b*x0*x0;
			if (denom==0f) {
				// The point is at the center of the ellipse.
				// Replies allways the same point.
				return null;
			}

			denom = (float)Math.sqrt(denom);
			float factor = (a * b) / denom;
			x = factor * x0;
			y = factor * y0;

			// Translate the point to the original coordinate system
			x += (ex + a);
			y += (ey + b);
		}
		return new Point2f(x, y);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are colinear, see {@link #isCollinearLines(float, float, float, float, float, float, float, float)}.
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
	 * @see #isCollinearLines(float, float, float, float, float, float, float, float)
	 * @since 3.0
	 */
	public static boolean isParallelLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		return isCollinearVectors(x2 - x1, y2 - y1, x4 - x3, y4 - y3);
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
	 */
	public static boolean isParallelLines(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
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
	 * @see #isEpsilonZero(float)
	 */
	public static boolean isCollinearPoints(float x1, float y1, float x2, float y2, float x3, float y3) {
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
	 * @see #isEpsilonZero(float)
	 */
	public static boolean isCollinearPoints(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		float dx1 = x2 - x1;
		float dy1 = y2 - y1;
		float dz1 = z2 - z1;
		float dx2 = x3 - x1;
		float dy2 = y3 - y1;
		float dz2 = z3 - z1;

		float cx = dy1 * dz2 - dy2 * dz1;
		float cy = dx2 * dz1 - dx1 * dz2;
		float cz = dx1 * dy2 - dx2 * dy1;

		float sum = cx * cx + cy * cy + cz * cz;

		return isEpsilonZero(sum);
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
	 * @see #isEpsilonZero(float)
	 */
	public static boolean isCollinearVectors(float x1, float y1, float x2, float y2) {
		// Test if three points are colinears
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computation consumption.
		return isEpsilonZero(x1 * y2 - x2 * y1);
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
	 * @see #isEpsilonZero(float)
	 */
	public static boolean isCollinearVectors(float x1, float y1, float z1, float x2, float y2, float z2) {
		// Cross product
		float cx = y1 * z2 - z1 * y2;
		float cy = z1 * x2 - x1 * z2;
		float cz = x1 * y2 - y1 * x2;

		float sum = cx * cx + cy * cy + cz * cz;

		return isEpsilonZero(sum);
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(float, float, float, float, float, float, float, float)}.
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
	 * @see #isParallelLines(float, float, float, float, float, float, float, float)
	 * @see #isCollinearPoints(float, float, float, float, float, float)
	 */
	public static boolean isCollinearLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		return (isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4) && isCollinearPoints(x1, y1, x2, y2, x3, y3));
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(float, float, float, float, float, float, float, float, float, float, float, float)}.
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
	 * @see #isParallelLines(float, float, float, float, float, float, float, float, float, float, float, float)
	 * @see #isCollinearPoints(float, float, float, float, float, float, float, float, float)
	 */
	public static boolean isCollinearLines(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
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
	public static int getCohenSutherlandCode(float px, float py, float rxmin, float rymin, float rxmax, float rymax) {
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
	 */
	public static boolean clipSegmentToRectangle(Point2D p1, Point2D p2, float rxmin, float rymin, float rxmax, float rymax) {
		float x0 = p1.getX();
		float y0 = p1.getY();
		float x1 = p2.getX();
		float y1 = p2.getY();
		int code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		float x, y;
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
	 */
	public static boolean clipSegmentToRectangle(Point2D p1, Point2D p2, int rxmin, int rymin, int rxmax, int rymax) {
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
	

	/** Replies the cosecant of the specified angle.
	 * <p>
	 * <code>csc(a) = 1/sin(a)</code>
	 * <p>
	 * 
	 * @param angle
	 * @return the cosecant.
	 */
	public static float csc(float angle) {
		return (float) (1.f/Math.sin(angle));
	}

	/** Replies the secant of the specified angle.
	 * <p>
	 * <code>sec(a) = 1/cos(a)</code>
	 * <p>
	 * 
	 * @param angle
	 * @return the secant.
	 */
	public static float sec(float angle) {
		return (float) (1.f/Math.cos(angle));
	}

	/** Replies the cotangent of the specified angle.
	 * <p>
	 * <code>cot(a) = 1/tan(a)</code>
	 * <p>
	 * 
	 * @param angle
	 * @return the cotangent.
	 */
	public static float cot(float angle) {
		return (float) (1.f/Math.tan(angle));
	}

	/** Replies the versine of the specified angle.
	 * <p>
	 * <code>versin(a) = 1 - cos(a)</code>
	 * <p>
	 * 
	 * @param angle
	 * @return the versine.
	 */
	public static float versin(float angle) {
		return (float) (1.f - Math.cos(angle));
	}

	/** Replies the exsecant of the specified angle.
	 * <p>
	 * <code>exsec(a) = sec(a) - 1</code>
	 * <p>
	 * 
	 * @param angle
	 * @return the exsecant.
	 */
	public static float exsec(float angle) {
		return sec(angle) - 1.f;
	}

	/** Replies the chord of the specified angle.
	 * <p>
	 * <code>crd(a) = 2 sin(a/2)</code>
	 * <p>
	 * 
	 * @param angle
	 * @return the chord.
	 */
	public static float crd(float angle) {
		return (float) (2.f * Math.sin(angle/2.f));
	}

	/** Replies the half of the versine (aka, haversine) of the specified angle.
	 * <p>
	 * <code>haversine(a) = sin<sup>2</sup>(a/2) = (1-cos(a)) / 2</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Excosecant function]">
	 * 
	 * @param angle
	 * @return the haversine.
	 */
	public static float haversine(float angle) {
		float sin2 = (float) Math.sin(angle/2.f);
		return sin2*sin2;
	}

	/** Replies the cubic root of the given number.
	 * 
	 * @param n
	 * @return the cubic root of the given number.
	 */
	public static float curt(float n) {
		if (n<0.f) return (float) -Math.pow(-n, 1.f/3.f);
		return (float) Math.pow(n, 1.f/3.f);
	}
	
	/**
	 * Solving cubic polynomials of the form {@code t<sup>3</sup> + at<sup>2</sup> + bt + c = 0}.
	 * <p>
	 * Any necessary division must be performed to produce a leading coefficient of 1.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return the values for {@code t}
	 * @see "Mathematics for 3D Game Programming and Computer Graphics, 2nd edition; pp.135." 
	 */
	public static float[] solvesCubicPolynomials(float a, float b, float c) {
		// The substitution: t = x - (a/3)
		// gives the equation to solve: x^3 + px + q = 0
		// where: p = -(1/3) a^2 + b
		// and: q = (2/27) a^3 - (1/3) ab + c
		
		float _aon3 = a / 3.f;
		
		float pprime = -((a*a)/9.f) + (b/3.f);
		float qprime = (a*a*a)/27.f - (a*b)/6.f + c/2.f;
		float pprime3 = pprime*pprime*pprime;
		
		float Dprime = -(pprime3+qprime*qprime);
				
		if (Dprime<0.) {
			// One root
			// x = r + s
			float sqrtDprime = (float) Math.sqrt(-Dprime);
			float r = curt(-qprime + sqrtDprime);
			float s = curt(-qprime - sqrtDprime);
			return new float [] { r + s - _aon3 };
		}
		else if (Dprime>0.) {
			// Three roots:
			assert(pprime<=0);
			float Theta = (float) (Math.acos(-qprime / Math.sqrt(-pprime3)) / 3.f);
			float omega = (float) (2.f*Math.sqrt(-pprime));
			float _2pion3 = 2.f*MathConstants.ONE_HALF_PI;
			return new float[] {
					(float) (omega * Math.cos(Theta) - _aon3),
					(float) (omega * Math.cos(Theta+_2pion3) - _aon3),
					(float) (omega * Math.cos(Theta-_2pion3) - _aon3),
			};
		}
		else {
			// Two roots:
			// because r = s,
			// x1 = 2r
			// x2 = -r
			float r = curt(-qprime);
			return new float[] {
					2.f*r - _aon3,
					-r - _aon3
			};
		}
	}
	
	
	

	/**
	 * Replies the precision used to test a floating point value.
	 * 
	 * @return the precision used to test a floating point value.
	 */
	public static float getEpsilon() {
		return FLOAT_PRECISION;
	}

	/**
	 * Replies the precision used to test a radian value.
	 * 
	 * @return the precision used to test a radian value.
	 */
	public static float getRadianEpsilon() {
		return RADIAN_PRECISION;
	}

	/**
	 * Replies the precision used to test a trigonometrical value.
	 * 
	 * @return the precision used to test a trigonometrical value.
	 */
	public static float getTrigonometricEpsilon() {
		return TRIGO_PRECISION;
	}

	/**
	 * Replies the precision used to test a distance value.
	 * 
	 * @return the precision used to test a distance value.
	 */
	public static float getDistanceEpsilon() {
		return DISTANCE_PRECISION;
	}

	/**
	 * Replies the precision used to test a floating point value.
	 * 
	 * @param newPrecisionValue
	 *            the new precision value.
	 * @return the old precision value.
	 */
	public static float setEpsilon(float newPrecisionValue) {
		float old = FLOAT_PRECISION;
		FLOAT_PRECISION = newPrecisionValue;
		return old;
	}

	/**
	 * Replies the precision used to test a radian value.
	 * 
	 * @param newPrecisionValue
	 *            the new precision value.
	 * @return the old precision value.
	 */
	public static float setRadianEpsilon(float newPrecisionValue) {
		float old = RADIAN_PRECISION;
		RADIAN_PRECISION = newPrecisionValue;
		return old;
	}

	/**
	 * Replies the precision used to test a trigonometric value.
	 * 
	 * @param newPrecisionValue
	 *            the new precision value.
	 * @return the old precision value.
	 */
	public static float setTrigonometricEpsilon(float newPrecisionValue) {
		float old = TRIGO_PRECISION;
		TRIGO_PRECISION = newPrecisionValue;
		return old;
	}

	/**
	 * Replies the precision used to test a distance value.
	 * 
	 * @param newPrecisionValue
	 *            the new precision value.
	 * @return the old precision value.
	 */
	public static float setDistanceEpsilon(float newPrecisionValue) {
		float old = DISTANCE_PRECISION;
		DISTANCE_PRECISION = newPrecisionValue;
		return old;
	}

	/**
	 * Replies if the specified vectors are approximatively colinear. This function uses the dot product and the trigonometrical precision.
	 * 
	 * @param vec1
	 * @param vec2
	 * @return <code>true</code> if the vectors are colinear
	 */
	public static boolean epsilonColinear(Vector3f vec1, Vector3f vec2) {
		if ((vec1.getX() == 0 && vec1.getY() == 0 && vec1.getZ() == 0) || (vec2.getX() == 0 && vec2.getY() == 0 && vec2.getZ() == 0))
			return true;
		float dotProduct = MathUtil.dotProductNorm(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
		if (Float.isNaN(dotProduct) || Float.isInfinite(dotProduct))
			return false;
		dotProduct = 1.f - Math.abs(dotProduct);
		return ((dotProduct >= -TRIGO_PRECISION) && (dotProduct <= TRIGO_PRECISION));
	}

	/**
	 * Replies if the specified vectors are approximatively equal. This function uses the dot product and the trigonometrical precision.
	 * 
	 * @param vec1
	 * @param vec2
	 * @return <code>true</code> if the vectors are equals
	 */
	public static boolean epsilonEquals(Vector3f vec1, Vector3f vec2) {
		if (vec1.getX() == 0 && vec1.getY() == 0 && vec1.getZ() == 0 && vec2.getX() == 0 && vec2.getY() == 0 && vec2.getZ() == 0)
			return true;
		float dotProduct = MathUtil.dotProductNorm(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
		if (Float.isNaN(dotProduct) || Float.isInfinite(dotProduct))
			return false;
		dotProduct = 1.f - dotProduct;
		return ((dotProduct >= -TRIGO_PRECISION) && (dotProduct <= TRIGO_PRECISION));
	}

	/**
	 * Replies if the specified points are approximatively equal. This function uses {@link #epsilonEqualsZeroDistanceSquared(float)}
	 * 
	 * @param vec1
	 * @param vec2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(Point3f vec1, Point3f vec2) {
		return epsilonEqualsZeroDistanceSquared(vec1.distanceSquared(vec2));
	}

	/**
	 * Replies if the specified points are approximatively equal according to the specified precision. This function uses {@link #epsilonEqualsZeroDistanceSquared(float)}
	 * 
	 * @param vec1
	 * @param vec2
	 * @param precision
	 *            of the equality test
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(Point3f vec1, Point3f vec2, float precision) {
		return epsilonEqualsZeroDistanceSquared(vec1.distanceSquared(vec2), precision);
	}

	/**
	 * Replies if the specified points are approximatively equal. This function uses {@link #epsilonEqualsZeroDistanceSquared(float)}
	 * 
	 * @param vec1
	 * @param vec2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(Point2f vec1, Point2f vec2) {
		return epsilonEqualsZeroDistanceSquared(vec1.distanceSquared(vec2));
	}

	/**
	 * Replies if the specified points are approximatively equal according to the specified precision. This function uses {@link #epsilonEqualsZeroDistanceSquared(float)}
	 * 
	 * @param vec1
	 * @param vec2
	 * @param precision
	 *            of the equality test
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(Point2f vec1, Point2f vec2, float precision) {
		return epsilonEqualsZeroDistanceSquared(vec1.distanceSquared(vec2), precision);
	}

	/**
	 * Replies if the specified points are approximatively equal. This function uses {@link #epsilonEqualsZeroDistanceSquared(float)}
	 * 
	 * @param ax
	 * @param ay
	 * @param az
	 * @param bx
	 * @param by
	 * @param bz
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(float ax, float ay, float az, float bx, float by, float bz) {
		return epsilonEqualsZeroDistanceSquared(distanceSquared(ax, ay, az, bx, by, bz));
	}

	/**
	 * Replies if the specified points are approximatively equal. This function uses {@link #epsilonEqualsZeroDistanceSquared(float)}
	 * 
	 * @param ax
	 * @param ay
	 * @param bx
	 * @param by
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(float ax, float ay, float bx, float by) {
		return epsilonEqualsZeroDistanceSquared(distanceSquared(ax, ay, bx, by));
	}

	/**
	 * Replies if the specified vectors are approximatively colinear. This function uses the dot product and the trigonometrical precision.
	 * 
	 * @param vec1
	 * @param vec2
	 * @return <code>true</code> if the given parameters are colinear
	 */
	public static boolean epsilonColinear(Vector2f vec1, Vector2f vec2) {
		if ((vec1.getX() == 0 && vec1.getY() == 0) || (vec2.getX() == 0 && vec2.getY() == 0))
			return true;
		float dotProduct = MathUtil.dotProductNorm(vec1.getX(), vec1.getY(), vec2.getX(), vec2.getY());
		if (Float.isNaN(dotProduct) || Float.isInfinite(dotProduct))
			return false;
		dotProduct = 1.f - Math.abs(dotProduct);
		return ((dotProduct >= -TRIGO_PRECISION) && (dotProduct <= TRIGO_PRECISION));
	}

	/**
	 * Replies if the specified vectors are approximatively equal. This function uses the dot product and the trigonometrical precision.
	 * 
	 * @param vec1
	 * @param vec2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(Vector2f vec1, Vector2f vec2) {
		if (vec1.getX() == 0 && vec1.getY() == 0 && vec2.getX() == 0 && vec2.getY() == 0)
			return true;
		float dotProduct = MathUtil.dotProductNorm(vec1.getX(), vec1.getY(), vec2.getX(), vec2.getY());
		if (Float.isNaN(dotProduct) || Float.isInfinite(dotProduct))
			return false;
		dotProduct = 1.f - dotProduct;
		return ((dotProduct >= -TRIGO_PRECISION) && (dotProduct <= TRIGO_PRECISION));
	}

	/**
	 * Replies if the specified points are approximatively equal. This function uses the distance precision.
	 * 
	 * @param p1
	 * @param p2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsDistance(Point3f p1, Point3f p2) {
		float distance2 = distanceSquared(p1, p2);
		return ((distance2 >= -(DISTANCE_PRECISION*DISTANCE_PRECISION)) && (distance2 <= (DISTANCE_PRECISION*DISTANCE_PRECISION)));
	}

	/**
	 * Replies if the specified points are approximatively equal. This function uses the distance precision.
	 * 
	 * @param p1
	 * @param p2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsDistance(Point2f p1, Point2f p2) {
		float distance2 = distanceSquared(p1, p2);
		return ((distance2 >= -(DISTANCE_PRECISION*DISTANCE_PRECISION)) && (distance2 <= (DISTANCE_PRECISION*DISTANCE_PRECISION)));
	}

	/**
	 * Replies if the specified value is approximatively equal to zero.
	 * 
	 * @param value
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsZero(float value) {
		if (Float.isNaN(value) || Float.isInfinite(value))
			return false;
		return Math.abs(value) <= FLOAT_PRECISION;
	}

	/**
	 * Replies if the specified values are approximatively equal.
	 * 
	 * @param value1
	 * @param value2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEquals(float value1, float value2) {
		if (Float.isNaN(value1) || Float.isNaN(value2) || Float.isInfinite(value1) || Float.isInfinite(value2)) {
			return Float.compare(value1, value2) == 0;
		}

		return Math.abs(value1 - value2) <= FLOAT_PRECISION;
	}

	/**
	 * Replies if the specified value is approximatively equal to zero, less or greater than zero.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>value</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>[-Infinity;-epsilon[</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>[-epsilon;epsilon]</code></td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>]epsilon;Infinity]</code></td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and greater than all other according to the definition of {@link float#compareTo(float)}.
	 * 
	 * @param value
	 * @return a negative value if the parameter is negative, a positive value of the parameter is positive, or zero if the parameter is approximatively equal to zero.
	 */
	public static int epsilonSign(float value) {
		if (Float.isNaN(value))
			return 1;
		if (epsilonEqualsZero(value))
			return 0;
		return (int) Math.signum(value);
	}

	/**
	 * Replies if the first specified value is approximatively equal, less or greater than to the second sepcified value.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>value1</var></th>
	 * <th><var>value2</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td>any number</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td>any number</td>
	 * <td><code>NaN</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;n2[</td>
	 * <td><code>n2</code> in ]n1;+Infinity]</td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;Infinity]</td>
	 * <td><code>n2</code> in [n1-epsilon;n1+epsilon]</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in ]n2;Infinity]</td>
	 * <td><code>n2</code> in [-Infinity;n1[</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and greater than all other according to the definition of {@link float#compareTo(float)}.
	 * 
	 * @param value1
	 * @param value2
	 * @return a negative value if the parameter <var>value1</var> is lower than <var>value2</var>, a positive if <var>value1</var> is greater than <var>value2</var>, zero if the two parameters are approximatively equal.
	 */
	public static int epsilonCompareTo(float value1, float value2) {
		if (Float.isNaN(value1) || Float.isNaN(value2) || Float.isInfinite(value1) || Float.isInfinite(value2)) {
			return Float.compare(value1, value2);
		}
		return (Math.abs(value1 - value2) <= FLOAT_PRECISION) ? 0 : Float.compare(value1, value2);
	}

	/**
	 * Replies if the specified angle in radians is approximatively equal to zero.
	 * 
	 * @param radian
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsZeroRadian(float radian) {
		if (Float.isNaN(radian) || Float.isInfinite(radian))
			return false;
		float r = clampRadian0To2PI(radian);
		return ((r >= TWO_PI - RADIAN_PRECISION) || (r <= RADIAN_PRECISION));
	}

	/**
	 * Replies if the specified angles in radians are approximatively equal.
	 * 
	 * @param value1
	 * @param value2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsRadian(float value1, float value2) {
		if (Float.isNaN(value1) || Float.isNaN(value2) || Float.isInfinite(value1) || Float.isInfinite(value2)) {
			return Float.compare(value1, value2) == 0;
		}

		float c1 = clampRadian0To2PI(value1);
		float c2 = clampRadian0To2PI(value2);

		if (c1 <= c2) {
			c2 -= c1;
		} else {
			c2 = c1 - c2;
		}

		return (c2 >= TWO_PI - RADIAN_PRECISION || c2 <= RADIAN_PRECISION);
	}

	/**
	 * Replies if the specified angle in radians is approximatively equal to zero, less or greater than zero.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>radian</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>[-Infinity;-epsilon[</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>[-epsilon;epsilon]</code></td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>]epsilon;Infinity]</code></td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and greater than all other according to the definition of {@link float#compareTo(float)}.
	 * 
	 * @param radian
	 * @return a negative value if the parameter is negative, a positive value of the parameter is positive, or zero if the parameter is approximatively equal to zero.
	 */
	public static int epsilonRadianSign(float radian) {
		if (Float.isNaN(radian))
			return 1;
		if (epsilonEqualsZeroRadian(radian))
			return 0;
		return (int) Math.signum(radian);
	}

	/**
	 * Replies if the specified angles (in radians) are approximatively equal, less or greater than. The angles are clamped in [0;2PI] before compare them.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>radian1</var></th>
	 * <th><var>radian2</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td>any number</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td>any number</td>
	 * <td><code>NaN</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;n2[</td>
	 * <td><code>n2</code> in ]n1;+Infinity]</td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;Infinity]</td>
	 * <td><code>n2</code> in [n1-epsilon;n1+epsilon]</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in ]n2;Infinity]</td>
	 * <td><code>n2</code> in [-Infinity;n1[</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and greater than all other according to the definition of {@link float#compareTo(float)}.
	 * 
	 * @param radian1
	 * @param radian2
	 * @return a negative value if the parameter <var>radian1</var> is lower than <var>radian2</var>, a positive if <var>radian1</var> is greater than <var>radian2</var>, zero if the two parameters are approximatively equal.
	 */
	public static int epsilonCompareToRadian(float radian1, float radian2) {
		if (Float.isNaN(radian1) || Float.isNaN(radian2) || Float.isInfinite(radian1) || Float.isInfinite(radian2)) {
			return Float.compare(radian1, radian2);
		}
		float c1 = clampRadian0To2PI(radian1);
		float c2 = clampRadian0To2PI(radian2);
		int result;

		if (c1 <= c2) {
			c2 -= c1;
			result = -1;
		} else {
			c2 = c1 - c2;
			result = 1;
		}

		return (c2 >= TWO_PI - RADIAN_PRECISION || c2 <= RADIAN_PRECISION) ? 0 : result;
	}

	/**
	 * Replies if the specified angle in degrees is approximatively equal to zero.
	 * 
	 * @param degree
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsZeroDegree(float degree) {
		return epsilonEqualsZeroRadian((float) Math.toRadians(degree));
	}

	/**
	 * Replies if the specified angle in degrees is approximatively equal to zero, less or greater than zero.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>degree</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>[-Infinity;-epsilon[</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>[-epsilon;epsilon]</code></td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>]epsilon;Infinity]</code></td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and greater than all other according to the definition of {@link float#compareTo(float)}.
	 * 
	 * @param degree
	 * @return a negative value if the parameter is negative, a positive value of the parameter is positive, or zero if the parameter is approximatively equal to zero.
	 */
	public static int epsilonDegreeSign(float degree) {
		return epsilonRadianSign((float) Math.toRadians(degree));
	}

	/**
	 * Replies if the specified angles in degrees is approximatively equal, less or greater than. The angles are clamped in [0;360] before compare them.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>radian1</var></th>
	 * <th><var>radian2</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td>any number</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td>any number</td>
	 * <td><code>NaN</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;n2[</td>
	 * <td><code>n2</code> in ]n1;+Infinity]</td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;Infinity]</td>
	 * <td><code>n2</code> in [n1-epsilon;n1+epsilon]</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in ]n2;Infinity]</td>
	 * <td><code>n2</code> in [-Infinity;n1[</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and greater than all other according to the definition of {@link float#compareTo(float)}.
	 * 
	 * @param degree1
	 * @param degree2
	 * @return a negative value if the parameter <var>degree1</var> is lower than <var>degree2</var>, a positive if <var>degree1</var> is greater than <var>degree2</var>, zero if the two parameters are approximatively equal.
	 */
	public static int epsilonCompareToDegree(float degree1, float degree2) {
		return epsilonCompareToRadian((float) Math.toRadians(degree1), (float) Math.toRadians(degree2));
	}

	/**
	 * Replies if the specified angles in degrees are approximatively equal.
	 * 
	 * @param value1
	 * @param value2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsDegree(float value1, float value2) {
		return epsilonEqualsRadian((float) Math.toRadians(value1), (float) Math.toRadians(value2));
	}

	/**
	 * Replies if the specified trigonometric value is approximatively equal to zero.
	 * 
	 * @param value
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsZeroTrigo(float value) {
		return ((value >= -TRIGO_PRECISION) && (value <= TRIGO_PRECISION));
	}

	/**
	 * Replies if the specified trigonometric value is approximatively equal to zero, less or greater than zero.
	 * 
	 * @param value
	 * @return a negative value if the parameter is negative, a positive value of the parameter is positive, or zero if the parameter is approximatively equal to zero.
	 */
	public static int epsilonTrigoSign(float value) {
		if (Float.isNaN(value))
			return 1;
		if ((value >= -TRIGO_PRECISION) && (value <= TRIGO_PRECISION))
			return 0;
		if (value < -TRIGO_PRECISION)
			return -1;
		return 1;
	}

	/**
	 * Replies if the specified trigonometric values are approximatively equal, less or greater than. The values are clamped in [-1;1] before compare them.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>value1</var></th>
	 * <th><var>value2</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td>any number</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td>any number</td>
	 * <td><code>NaN</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;n2[</td>
	 * <td><code>n2</code> in ]n1;+Infinity]</td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;Infinity]</td>
	 * <td><code>n2</code> in [n1-epsilon;n1+epsilon]</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in ]n2;Infinity]</td>
	 * <td><code>n2</code> in [-Infinity;n1[</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and greater than all other according to the definition of {@link float#compareTo(float)}.
	 * 
	 * @param value1
	 * @param value2
	 * @return a negative value if the parameter <var>value1</var> is lower than <var>value2</var>, a positive if <var>value1</var> is greater than <var>value2</var>, zero if the two parameters are approximatively equal.
	 */
	public static int epsilonCompareToTrigo(float value1, float value2) {
		if (Float.isNaN(value1) || Float.isNaN(value2) || Float.isInfinite(value1) || Float.isInfinite(value2)) {
			return Float.compare(value1, value2);
		}
		float v1 = MathUtil.clamp(value1, -1.f, 1.f);
		float v2 = MathUtil.clamp(value2, -1.f, 1.f);
		if (Math.abs(v2 - v1) <= TRIGO_PRECISION)
			return 0;
		return Float.compare(v1, v2);
	}

	/**
	 * Replies if the specified trigonometric values are approximatively equal.
	 * 
	 * @param value1
	 * @param value2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsTrigo(float value1, float value2) {
		if (Float.isNaN(value1) || Float.isNaN(value2) || Float.isInfinite(value1) || Float.isInfinite(value2)) {
			return Float.compare(value1, value2) == 0;
		}
		float v1 = MathUtil.clamp(value1, -1.f, 1.f);
		float v2 = MathUtil.clamp(value2, -1.f, 1.f);
		return Math.abs(v2 - v1) <= TRIGO_PRECISION;
	}

	/**
	 * Replies if the specified distance value is approximatively equal to zero.
	 * 
	 * @param value
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsZeroDistance(float value) {
		return ((value >= -DISTANCE_PRECISION) && (value <= DISTANCE_PRECISION));
	}
	
	/**
	 * Replies if the specified distance value is approximatively equal to zero.
	 * (testing on square distance)
	 * @param squareValue square distance
	 * @return <code>true</code> if the given parameters are equal
	 */
	private static boolean epsilonEqualsZeroDistanceSquared(float squareValue) {
		return ((squareValue >= -(DISTANCE_PRECISION*DISTANCE_PRECISION)) && (squareValue <= (DISTANCE_PRECISION*DISTANCE_PRECISION)));
	}
	
	

	/**
	 * Replies if the specified distance value is approximatively equal to zero.
	 * 
	 * @param value
	 *            to test
	 * @param precision
	 *            of the equility test
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsZeroDistance(float value, float precision) {
		return ((value >= -precision) && (value <= precision));
	}
	
	/**
	 * Replies if the specified distance value is approximatively equal to zero.
	 * (testing on square distance
	 * @param squareValue
	 *            square distance to test
	 * @param precision
	 *            of the equility test
	 * @return <code>true</code> if the given parameters are equal
	 */
	private static boolean epsilonEqualsZeroDistanceSquared(float squareValue, float precision) {
		return ((squareValue >= -(precision*precision)) && (squareValue <= precision*precision));
	}

	/**
	 * Replies if the specified distance value is approximatively equal to zero, less or greater than zero.
	 * 
	 * @param value
	 * @return a negative value if the parameter is negative, a positive value of the parameter is positive, or zero if the parameter is approximatively equal to zero.
	 */
	public static int epsilonDistanceSign(float value) {
		if (Float.isNaN(value))
			return 1;
		if (Math.abs(value) <= DISTANCE_PRECISION)
			return 0;
		return (int) Math.signum(value);
	}

	/**
	 * Replies if the specified distances are approximatively equal, less or greater than.
	 * 
	 * @param distance1
	 * @param distance2
	 * @return a negative value if the parameter <var>distance1</var> is lower than <var>distance2</var>, a positive if <var>distance1</var> is greater than <var>distance2</var>, zero if the two parameters are approximatively equal.
	 */
	public static int epsilonCompareToDistance(float distance1, float distance2) {
		if (Math.abs(distance1 - distance2) <= DISTANCE_PRECISION)
			return 0;
		return Float.compare(distance1, distance2);
	}

	/**
	 * Replies if the specified distances are approximatively equal.
	 * 
	 * @param value1
	 * @param value2
	 * @return <code>true</code> if the given parameters are equal
	 */
	public static boolean epsilonEqualsDistance(float value1, float value2) {
		return (Math.abs(value1 - value2) <= DISTANCE_PRECISION);
	}

	/**
	 * Compute and replies the euclidian distance between the two points represented by the parameters.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the distance between the two given points
	 */
	public static float distance(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}

	/**
	 * Compute and replies the squared euclidian distance between the two points represented by the parameters.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the distance between the two given points
	 */
	public static float distanceSquared(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		return (dx * dx) + (dy * dy) + (dz * dz);
	}

	/**
	 * Compute the length of a vector.
	 * 
	 * @param vx
	 * @param vy
	 * @return the length
	 */
	public static float distance(float vx, float vy) {
		return (float) Math.sqrt(vx * vx + vy * vy);
	}

	/**
	 * Compute the length of a vector.
	 * 
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return the length
	 */
	public static float distance(float vx, float vy, float vz) {
		return (float) Math.sqrt(vx * vx + vy * vy + vz * vz);
	}

	/**
	 * Compute and replies the euclidian distance between the two points represented by the parameters.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance between the two given points
	 */
	public static float distance(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		return (float) Math.sqrt((dx * dx) + (dy * dy));
	}

	/**
	 * Compute and replies the squared euclidian distance between the two points represented by the parameters.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance between the two given points
	 */
	public static float distanceSquared(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		return (dx * dx) + (dy * dy);
	}

	/**
	 * Compute and replies the squared euclidian distance between the two points represented by the parameters.
	 * 
	 * @param a
	 * @param b
	 * @return the distance between the two given points
	 */
	public static float distanceSquared(Tuple3f<?> a, Tuple3f<?> b) {
		float dx = a.getX() - b.getX();
		float dy = a.getY() - b.getY();
		float dz = a.getZ() - b.getZ();
		return (dx * dx) + (dy * dy) + (dz * dz);
	}

	/**
	 * Compute and replies the euclidian distance between the two points represented by the parameters.
	 * 
	 * @param a
	 * @param b
	 * @return the distance between the two given points
	 */
	public static float distance(Tuple3f<?> a, Tuple3f<?> b) {
		float dx = a.getX() - b.getX();
		float dy = a.getY() - b.getY();
		float dz = a.getZ() - b.getZ();
		return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}

	/**
	 * Compute and replies the squared euclidian distance between the two points represented by the parameters.
	 * 
	 * @param a
	 * @param b
	 * @return the distance between the two given points
	 */
	public static float distanceSquared(Tuple2f<?> a, Tuple2f<?> b) {
		float dx = a.getX() - b.getX();
		float dy = a.getY() - b.getY();
		return (dx * dx) + (dy * dy);
	}

	/**
	 * Compute and replies the euclidian distance between the two points represented by the parameters.
	 * 
	 * @param a
	 * @param b
	 * @return the distance between the two given points
	 */
	public static float distance(Tuple2f<?> a, Tuple2f<?> b) {
		float dx = a.getX() - b.getX();
		float dy = a.getY() - b.getY();
		return (float) Math.sqrt((dx * dx) + (dy * dy));
	}

	/**
	 * Compute and replies the perpendicular distance from a point to a segment.
	 * <p>
	 * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @return the distance
	 */
	public static float distancePointSegment(float px, float py, float sx1, float sy1, float sx2, float sy2) {
		float r_denomenator = (sx2-sx1)*(sx2-sx1) + (sy2-sy1)*(sy2-sy1);
		if (r_denomenator==0.) return distance(px, py, sx1, sy1);
		float r_numerator = (px-sx1)*(sx2-sx1) + (py-sy1)*(sy2-sy1);
		float ratio = r_numerator / r_denomenator;

		if (ratio<=0.) {
			return (float) Math.sqrt((px-sx1)*(px-sx1) + (py-sy1)*(py-sy1));
		}

		if (ratio>=1.) {
			return (float) Math.sqrt((px-sx2)*(px-sx2) + (py-sy2)*(py-sy2));
		}

		float s =  ((sy1-py)*(sx2-sx1)-(sx1-px)*(sy2-sy1) ) / r_denomenator;
		return (float) (Math.abs(s) * Math.sqrt(r_denomenator));
	}

	/**
	 * Compute and replies the perpendicular squared distance from a point to a segment.
	 * <p>
	 * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @return the distance
	 */
	public static float distanceSquaredPointSegment(float px, float py, float sx1, float sy1, float sx2, float sy2) {
		float r_denomenator = (sx2-sx1)*(sx2-sx1) + (sy2-sy1)*(sy2-sy1);
		if (r_denomenator==0.) return distanceSquared(px, py, sx1, sy1);
		float r_numerator = (px-sx1)*(sx2-sx1) + (py-sy1)*(sy2-sy2);
		float ratio = r_numerator / r_denomenator;

		if (ratio<=0.) {
			return Math.abs((px-sx1)*(px-sx1) + (py-sy1)*(py-sy1));
		}

		if (ratio>=1.) {
			return Math.abs((px-sx2)*(px-sx2) + (py-sy2)*(py-sy2));
		}

		float s =  ((sy1-py)*(sx2-sx1)-(sx1-px)*(sy2-sy1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}

	/**
	 * Compute and replies the nearest point on a segment from a given point.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param nearestPoint
	 *            is the nearest point to set.
	 */
	public static void getNearestPointOnSegment(float px, float py, float sx1, float sy1, float sx2, float sy2, Tuple2f<?> nearestPoint) {
		float r_denomenator = (sx2-sx1)*(sx2-sx1) + (sy2-sy1)*(sy2-sy1);
		if (r_denomenator==0.) {
			nearestPoint.set(sx1, sy1);
		}
		else {
			float r_numerator = (px-sx1)*(sx2-sx1) + (py-sy1)*(sy2-sy2);
			float ratio = r_numerator / r_denomenator;

			if (ratio>=0. && ratio<=1.) {
				nearestPoint.set(sx1 + ratio*(sx2-sx1), sy1 + ratio*(sy2-sy1));
			}
			else {
				// Reuse vars, but loose the name semantic 
				r_numerator = (px-sx1)*(px-sx1) + (py-sy1)*(py-sy1);
				r_denomenator = (px-sx2)*(px-sx2) + (py-sy2)*(py-sy2);
				if (r_numerator<r_denomenator) {
					nearestPoint.set(sx1, sy1);
				}
				else {
					nearestPoint.set(sx2, sy2);
				}
			}
		}
	}

	/**
	 * Compute and replies the farest point on a segment from a given point.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param farestPoint
	 *            is the farest point to set.
	 */
	public static void getFarestPointOnSegment(float px, float py, float sx1, float sy1, float sx2, float sy2, Tuple2f<?> farestPoint) {
		float d1 = (px-sx1)*(px-sx1) + (py-sy1)*(py-sy1);
		float d2 = (px-sx2)*(px-sx2) + (py-sy2)*(py-sy2);
		if (d1<d2) {
			farestPoint.set(sx2, sy2);
		}
		else {
			farestPoint.set(sx1, sy1);
		}
	}

	/**
	 * Compute and replies the perpendicular distance from a point to a segment.
	 * <p>
	 * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @return the distance
	 */
	public static float distancePointSegment(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2) {
		float ratio = projectsPointOnSegment(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

		if (ratio <= 0.)
			return distance(px, py, pz, sx1, sy1, sz1);

		if (ratio >= 1.)
			return distance(px, py, pz, sx2, sy2, sz2);

		return distance(px, py, pz, (1.f - ratio) * sx1 + ratio * sx2, (1.f - ratio) * sy1 + ratio * sy2, (1.f - ratio) * sz1 + ratio * sz2);
	}

	/**
	 * Compute and replies the perpendicular squared distance from a point to a segment.
	 * <p>
	 * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @return the distance
	 */
	public static float distanceSquaredPointSegment(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2) {
		float ratio = projectsPointOnSegment(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

		if (ratio <= 0.)
			return distanceSquared(px, py, pz, sx1, sy1, sz1);

		if (ratio >= 1.)
			return distanceSquared(px, py, pz, sx2, sy2, sz2);

		return distanceSquared(px, py, pz, (1.f - ratio) * sx1 + ratio * sx2, (1.f - ratio) * sy1 + ratio * sy2, (1.f - ratio) * sz1 + ratio * sz2);
	}

	/**
	 * Compute and replies the nearest point on a segment from a given point.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @param nearestPoint
	 *            is the nearest point to set.
	 */
	public static void getNearestPointOnSegment(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2, Tuple3f<?> nearestPoint) {
		if (sx1 == sx2 && sy1 == sy2 && sz1 == sz2) {
			nearestPoint.set(sx1, sy1, sz1);
		} else {
			float ratio = projectsPointOnSegment(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

			if (ratio <= 0.) {
				nearestPoint.set(sx1, sy1, sz1);
			} else if (ratio >= 1.) {
				nearestPoint.set(sx2, sy2, sz2);
			} else {
				nearestPoint.set((1.f - ratio) * sx1 + ratio * sx2, (1.f - ratio) * sy1 + ratio * sy2, (1.f - ratio) * sz1 + ratio * sz2);
			}
		}
	}

	/**
	 * Compute and replies the farest point on a segment from a given point.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @param farestPoint
	 *            is the farest point to set.
	 */
	public static void getFarestPointOnSegment(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2, Tuple3f<?> farestPoint) {
		if (sx1 == sx2 && sy1 == sy2 && sz1 == sz2) {
			farestPoint.set(sx1, sy1, sz1);
		} else {
			float ratio = projectsPointOnSegment(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

			if (ratio <= .5) {
				farestPoint.set(sx2, sy2, sz2);
			} else {
				farestPoint.set(sx1, sy1, sz1);
			}
		}
	}

	/**
	 * Compute and replies the perpendicular distance from a point to a line.
	 * <p>
	 * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @return the distance
	 */
	public static float distancePointLine(float px, float py, float sx1, float sy1, float sx2, float sy2) {
		float r_denomenator = (sx2-sx1)*(sx2-sx1) + (sy2-sy1)*(sy2-sy1);
		if (r_denomenator==0.) return distance(px, py, sx1, sy1);
		float s = ((sy1-py)*(sx2-sx1)-(sx1-px)*(sy2-sy1) ) / r_denomenator;
		return (float) (Math.abs(s) * Math.sqrt(r_denomenator));
	}

	/**
	 * Compute and replies the perpendicular squared distance from a point to a line.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @return the distance
	 */
	public static float distanceSquaredPointLine(float px, float py, float sx1, float sy1, float sx2, float sy2) {
		float r_denomenator = (sx2-sx1)*(sx2-sx1) + (sy2-sy1)*(sy2-sy1);
		if (r_denomenator==0.) return distance(px, py, sx1, sy1);
		float s = ((sy1-py)*(sx2-sx1)-(sx1-px)*(sy2-sy1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}

	/**
	 * Compute and replies the perpendicular distance from a point to a line.
	 * <p>
	 * Call the point in space <code>(i,j,k)</code> and let <code>(a,b,c)</code> and <code>(x,y,z)</code> be two points on the line (call it line A). The crucial fact we'll use is that the minimum distance between the point and the line is the perpendicular distance. So we're looking for a point <code>(L,M,N)</code> which is on A, and such that the line connecting <code>(L,M,N)</code> and <code>(i,j,k)</code> is perpendicular to A.
	 * <p>
	 * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
	 * 
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @return the distance
	 */
	public static float distancePointLine(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2) {
		// We use the method of the parallelogram
		// P
		// o------+------o
		// \ | \
		// \ |h \
		// \ | \
		// ------o--+----------o-----------
		// S1 S2

		// vector S1S2 = <vx1, vy1, vz1>
		float vx1 = sx2 - sx1;
		float vy1 = sy2 - sy1;
		float vz1 = sz2 - sz1;

		// vector S1P = <vx2, vy2, vz2>
		float vx2 = px - sx1;
		float vy2 = py - sy1;
		float vz2 = pz - sz1;

		// Let's take the cross product S1S2 X S1P.
		Vector3f cross = MathUtil.crossProductRightHand(vx1, vy1, vz1, vx2, vy2, vz2);

		// Let's let (a) be the length of S1S2 X S1P.
		float a = cross.length();

		// If you divide (a) by the distance S1S2 you will get the distance of P
		// from line S1S2.
		float s1s2 = (float) Math.sqrt(vx1 * vx1 + vy1 * vy1 + vz1 * vz1);

		// Thus the distance we are looking for is a/s1s2.
		return a / s1s2;
	}

	/**
	 * Compares the two specified tuples at epsilon.
	 * <p>
	 * To compare tuples with exact test, please use {@link Tuple3fComparator} insteed.
	 * 
	 * @param ta
	 * @param tb
	 * @return <code>-1</code> if <var>ta</var> if lower than <var>tb</var>, <code>-1</code> if <var>ta</var> if greater than <var>tb</var>, otherwhise <code>0</code>
	 * @see Tuple3fComparator
	 */
	public static int epsilonCompare(Point3f ta, Point3f tb) {
		assert (ta != null && tb != null);
		if (epsilonEquals(ta, tb))
			return 0;
		int cmp;

		cmp = epsilonCompareTo(ta.getX(), tb.getX());
		if (cmp == 0) {
			cmp = epsilonCompareTo(ta.getY(), tb.getY());
			if (cmp == 0) {
				cmp = Float.compare(ta.getZ(), tb.getZ());
			}
		}

		return cmp;
	}

	/**
	 * Compares the two specified tuples at epsilon.
	 * <p>
	 * To compare tuples with exact test, please use {@link Tuple2fComparator} insteed.
	 * 
	 * @param ta
	 * @param tb
	 * @return <code>-1</code> if <var>ta</var> if lower than <var>tb</var>, <code>-1</code> if <var>ta</var> if greater than <var>tb</var>, otherwhise <code>0</code>
	 * @see Tuple2fComparator
	 */
	public static int epsilonCompare(Point2f ta, Point2f tb) {
		assert (ta != null && tb != null);
		if (epsilonEquals(ta, tb))
			return 0;
		int cmp;

		cmp = epsilonCompareTo(ta.getX(), tb.getX());
		if (cmp == 0) {
			cmp = Float.compare(ta.getY(), tb.getY());
		}

		return cmp;
	}

	/**
	 * Compares the two specified tuples at epsilon.
	 * <p>
	 * To compare tuples with exact test, please use {@link Tuple3fComparator} insteed.
	 * 
	 * @param ta
	 * @param tb
	 * @return <code>-1</code> if <var>ta</var> if lower than <var>tb</var>, <code>-1</code> if <var>ta</var> if greater than <var>tb</var>, otherwhise <code>0</code>
	 * @see Tuple3fComparator
	 */
	public static int epsilonCompare(Vector3f ta, Vector3f tb) {
		assert (ta != null && tb != null);
		if (epsilonEquals(ta, tb))
			return 0;
		int cmp;

		cmp = epsilonCompareTo(ta.getX(), tb.getX());
		if (cmp == 0) {
			cmp = epsilonCompareTo(ta.getY(), tb.getY());
			if (cmp == 0) {
				cmp = Float.compare(ta.getZ(), tb.getZ());
			}
		}

		return cmp;
	}

	/**
	 * Compares the two specified tuples at epsilon.
	 * <p>
	 * To compare tuples with exact test, please use {@link Tuple2fComparator} insteed.
	 * 
	 * @param ta
	 * @param tb
	 * @return <code>-1</code> if <var>ta</var> if lower than <var>tb</var>, <code>-1</code> if <var>ta</var> if greater than <var>tb</var>, otherwhise <code>0</code>
	 * @see Tuple2fComparator
	 */
	public static int epsilonCompare(Vector2f ta, Vector2f tb) {
		assert (ta != null && tb != null);
		if (epsilonEquals(ta, tb))
			return 0;
		int cmp;

		cmp = epsilonCompareTo(ta.getX(), tb.getX());
		if (cmp == 0) {
			cmp = Float.compare(ta.getY(), tb.getY());
		}

		return cmp;
	}

	/**
	 * Replies the projection on the plane of a point.
	 * <p>
	 * <strong>First Approach: arithmetic resolution</strong>
	 * <p>
	 * Let <math xmlns="http://www.w3.org/1998/Math/MathML"><mover><mrow><mi>u</mi></mrow><mo stretchy="false">&#x02192;</mo></mover></math> a vector colinear to the line <math><mi mathvariant="normal">&#x00394;</mi></math> with components <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo></math>.<br>
	 * Let the equation of the plane <math><mi mathvariant="normal">&#x003A0;</mi></math> as <math><mi>a</mi><mo>.</mo><mi>x</mi><mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><mi>y</mi><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <mi>z</mi><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn></math><br>
	 * Let the point <math><mi>A</mi></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>A</mi></msub><mo stretchy="false">)</mo></math> and its projection point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub>
	 * <mo>&#x0002C;</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo stretchy="false">)</mo></math>.
	 * <p>
	 * Since <math><mo stretchy="false">(</mo><mi>A</mi><msup><mi>A</mi><mo>&#x02032;</mo></msup><mo stretchy="false">)</mo></math> is parallel to <math><mi mathvariant="normal">&#x00394;</mi></math>, a scalar <math><mi>k</mi></math> exists such as <math><mover><mrow><mi>A</mi><mrow><mi>A</mi><msup><mo>&#x02032;</mo></msup></mrow></mrow><mo stretchy="true">&#x02192;</mo></mover> <mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><mover><mi>u</mi><mo
	 * stretchy="false">&#x02192;</mo></mover></math><br>
	 * that <math><mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi> <mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x02212;</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0003D;</mo> <mi>k</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub> </mtd></mtr><mtr><mtd><msub><mi>y</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x02212;</mo><msub> <mi>y</mi><mi>A</mi></msub><mo>&#x0003D;</mo><mi>k</mi>
	 * <mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub></mtd></mtr> <mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x02212;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mtd></mtr></mtable></mrow></math>.
	 * <p>
	 * 
	 * In addition, <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> is on <math><mi mathvariant="normal">&#x003A0;</mi></math>, what means that <math> <mi>a</mi><mo>.</mo><msub><mi>x</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0002B;</mo><mi>b</mi> <mo>.</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup>
	 * </mrow></msub><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn> </math>.
	 * <p>
	 * The result is a system of 4 equations with 4 unknown variables: <math><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math> and <math><mi>k</mi><math>.
	 * <p>
	 * <math> <mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow> <msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0003D;</mo><mfrac><mrow><mi>b</mi><mo>&#x022C5;</mo><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><msub> <mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo
	 * stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub>
	 * <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo
	 * stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>y</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo>
	 * <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi>
	 * <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>b</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi>
	 * <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * In the case of an orthogonal projection and if the reference axis are orthonormal, one can choose <math><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>a</mi></math>, <math><msub><mi>y</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>b</mi></math>, <math><msub><mi>z</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>c</mi></math>, then:
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mrow> <mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow><msup> <mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo> <mfrac><mrow><mo stretchy="false">(</mo><msup><mi>b</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>a</mi><mi>b</mi><mo>&#x022C5;</mo>
	 * <msub><mi>y</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>a</mi> <mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi></msub> <mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>a</mi></mrow> <mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup> <mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi> <mn>2</mn></msup></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow>
	 * </msub><mo>&#x0003D;</mo><mfrac><mrow><mo>&#x02212;</mo><mi>a</mi> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>b</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>d</mi>
	 * <mo>&#x022C5;</mo><mi>b</mi></mrow><mrow><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup> <mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup></mrow> </mfrac></mtd></mtr><mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo><mfrac> <mrow><mo>&#x02212;</mo><mi>a</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>b</mi>
	 * <mi>c</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>c</mi> </mrow><mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo> <msup><mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi>
	 * <mn>2</mn></msup></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * <strong>Second Approach: vectorial resolution</strong>
	 * <p>
	 * Let the normal of the plane be <math><mover><mi>n</mi><mo stretchy="false">&#x02192;</mo></mover></math> with the coordinates <math><mo stretchy="false">(</mo><msub><mi>n</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>n</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>n</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.<br>
	 * Let <math><mi>A</mi></math> the point of coordinates <math><mo stretchy="false">(</mo><msub><mi>A</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>A</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>A</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math> and its projection on the plane <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> with the coordinates <math><mo
	 * stretchy="false">(</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>x</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>y</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.
	 * <p>
	 * Let <math><mi>s</mi></math> the distance between the point <math><mi>A</mi></math> and the nearest point on the plane, ie. the point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> such as: <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mi>s</mi><mo>&#x0003D;</mo> <mi>a</mi><mo>.</mo><msub><mi>A</mi><mi>x</mi></msub> <mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><msub><mi>A</mi><mi>y</mi> </msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo><msub><mi>A</mi>
	 * <mi>z</mi></msub><mo>&#x0002B;</mo><mi>d</mi></math>. If <math><mi>s</mi></math> is positive, the point is in the front of the plane. If <math><mi>s</mi></math> is negative, the point is behind the plane.
	 * <p>
	 * Consequently <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><msup><mi>A</mi> <mo>&#x02032;</mo></msup><mo>&#x0003D;</mo><mi>A</mi><mo>&#x02212;</mo> <mfrac><mrow><mi>s</mi></mrow><mrow><mo stretchy="false">&#x0007C;</mo> <mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover><mo stretchy="false">&#x0007C;</mo> </mrow></mfrac><mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover></math>.
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"> <mrow> <mo>&#x0007B;</mo> <mtable> <mtr> <mtd> <msub> <mi>x</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>x</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>x</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>y</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>y</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>y</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>z</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>z</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>z</mi> </msub> </mtd> </mtr> </mtable> </mrow> </math>
	 * 
	 * @param p
	 *            is the point to project
	 * @param plane
	 *            is the projection plane
	 * @return the projection of the specified point on the plane.
	 */
	public static Point3f projectsPointOnPlaneIn3d(Point3f p, Plane plane) {
		// Arithmetic resolution
		/*
		 * plane.normalize();
		 * 
		 * Vector3f n = plane.getNormal(); float d = plane.getEquationComponentD(); // normalization apply that a*a+b*b+c*c = 1
		 * 
		 * float xaprime = (n.y*n.y+n.z*n.z)*px -n.x*n.y*py -n.x*n.z*pz -d*n.x;
		 * 
		 * float yaprime = -n.x*n.y*px +(n.x*n.x+n.z*n.z)*py +n.y*n.z*pz -d*n.y;
		 * 
		 * float zaprime = -n.x*n.z*px +n.y*n.z*py +(n.x*n.x+n.y*n.y)*pz -d*n.z;
		 * 
		 * return new Point3f(xaprime,yaprime,zaprime);
		 */

		// Vectorial resolution
		Vector3f normal = plane.getNormal();
		normal.normalize();
		normal.negate();

		float s = plane.distanceTo(p.getX(), p.getY(), p.getZ());

		float xaprime = p.getX() + s * normal.getX();
		float yaprime = p.getY() + s * normal.getY();
		float zaprime = p.getZ() + s * normal.getZ();

		return new Point3f(xaprime, yaprime, zaprime);
	}

	/**
	 * Replies the projection on the plane of a point.
	 * 
	 * @param point
	 *            is the point to project
	 * @param plane
	 *            is the projection plane
	 * @return the projection of the specified point on the plane.
	 */
	public static Point2f projectsPointOnPlaneIn2d(Tuple3f<?> point, Plane plane) {
		Point3f eye = projectsPointOnPlaneIn3d(new Point3f(), plane);

		Vector3f normal = plane.getNormal();

		Transform3D m = new Transform3D();

		Vector3f up = new Vector3f(0, 1, 0);
		if (epsilonColinear(up, normal))
			up.set(1, 0, 0);

		m.lookAt(eye, normal, up);
		m.invert();

		Matrix4f m2 = new Matrix4f();
		m2.setIdentity();
		m2.m03 = eye.getX();
		m2.m13 = eye.getY();
		m2.m23 = eye.getZ();
		m2.invert();

		m.mul(m2);

		float xprime = m.m00 * point.getX() + m.m01 * point.getY() + m.m02 * point.getZ() + m.m03;
		float yprime = m.m10 * point.getX() + m.m11 * point.getY() + m.m12 * point.getZ() + m.m13;
		float wprime = m.m30 * point.getX() + m.m31 * point.getY() + m.m32 * point.getZ() + m.m33;

		return new Point2f(xprime / wprime, yprime / wprime);
	}

	/**
	 * Replies the projection a point on a line.
	 * 
	 * @param p
	 *            is the point to project
	 * @param s1
	 *            is first line point.
	 * @param s2
	 *            is the second line point.
	 * @return the projection of the specified point on the line.
	 */
	public static Point2f projectsPointOnLine(Point2f p, Point2f s1, Point2f s2) {
		float denom = s1.getX() - s2.getX();

		if (denom == 0.) {
			return new Point2f(s1.getX(), p.getY());
		}

		float a = (s1.getY() - s2.getY()) / denom;
		float b = s1.getY() - a * s1.getX();
		float m2 = a * a;
		float x = (a * p.getY() + p.getX() - a * b) / (m2 + 1);
		float y = (m2 * p.getY() + a * p.getX() + b) / (m2 + 1);
		return new Point2f(x, y);
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
	 * @return the projection of the specified point on the line. If equal to {@code 0}, the projection is equal to the first segment point. If equal to {@code 1}, the projection is equal to the second segment point. If inside {@code ]0;1[}, the projection is between the two segment points. If inside {@code ]-inf;0[}, the projection is outside on the side of the first segment point. If inside {@code ]1;+inf[}, the projection is outside on the side of the second segment point.
	 */
	public static float projectsPointOnSegment(float px, float py, float s1x, float s1y, float s2x, float s2y) {
		float r_numerator = (px-s1x)*(s2x-s1x) + (py-s1y)*(s2y-s1y);
		float r_denomenator = (s2x-s1x)*(s2x-s1x) + (s2y-s1y)*(s2y-s1y);
		return r_numerator / r_denomenator;
	}

	/**
	 * Replies the projection a point on a segment.
	 * 
	 * @param px
	 *            is the coordiante of the point to project
	 * @param py
	 *            is the coordiante of the point to project
	 * @param pz
	 *            is the coordiante of the point to project
	 * @param s1x
	 *            is the x-coordinate of the first line point.
	 * @param s1y
	 *            is the y-coordinate of the first line point.
	 * @param s1z
	 *            is the z-coordinate of the first line point.
	 * @param s2x
	 *            is the x-coordinate of the second line point.
	 * @param s2y
	 *            is the y-coordinate of the second line point.
	 * @param s2z
	 *            is the z-coordinate of the second line point.
	 * @return the projection of the specified point on the line. If equal to {@code 0}, the projection is equal to the first segment point. If equal to {@code 1}, the projection is equal to the second segment point. If inside {@code ]0;1[}, the projection is between the two segment points. If inside {@code ]-inf;0[}, the projection is outside on the side of the first segment point. If inside {@code ]1;+inf[}, the projection is outside on the side of the second segment point.
	 */
	public static float projectsPointOnSegment(float px, float py, float pz, float s1x, float s1y, float s1z, float s2x, float s2y, float s2z) {
		float dx = s2x - s1x;
		float dy = s2y - s1y;
		float dz = s2z - s1z;

		if (dx == 0. && dy == 0. && dz == 0.)
			return 0.f;

		return ((px - s1x) * dx + (py - s1y) * dy + (pz - s1z) * dz) / (dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Compute the signed angle between two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 */
	public static float signedAngle(float x1, float y1, float z1, float x2, float y2, float z2) {
		float lengths = (float) (Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1) * Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2));
		if (lengths == 0.)
			return Float.NaN;

		// First method
		// Angle
		// A . B = |A|.|B|.cos(theta)
		float dot = MathUtil.dotProduct(x1, y1, z1, x2, y2, z2) / lengths;
		if (dot < -1.0f)
			dot = -1.0f;
		if (dot > 1.0f)
			dot = 1.0f;
		float angle = (float) Math.acos(dot);

		// On which side of A, B is located?
		if ((dot > -1) && (dot < 1)) {
			// det(A,B) = |A|.|B|.sin(theta)
			dot = MathUtil.determinant(x1, y1, z1, x2, y2, z2) / lengths;
			if (dot < 0)
				angle = -angle;
		}

		return angle;
	}

	/**
	 * Turn the given 2D vector of the given angle.
	 * 
	 * @param vector
	 *            is the vector to turn.
	 * @param angle
	 *            is the rotation angle.
	 */
	public static void turnVector(Vector2f vector, float angle) {
		assert (vector != null);
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float x = cos * vector.getX() - sin * vector.getY();
		float y = sin * vector.getX() + cos * vector.getY();
		vector.set(x, y);
	}

	/**
	 * Computes the distance from a line segment AB to a line segment CD
	 * 
	 * @param Ax
	 * @param Ay
	 * @param Bx
	 * @param By
	 * @param Cx
	 * @param Cy
	 * @param Dx
	 * @param Dy
	 * @return the distance
	 */
	public static float distanceSegmentSegment(float Ax, float Ay, float Bx, float By, float Cx, float Cy, float Dx, float Dy) {

		// check for zero-length segments
		float d_a_cd = distancePointSegment(Ax, Ay, Cx, Cy, Dx, Dy);
		if (epsilonEquals(Ax, Ay, Bx, By))
			return d_a_cd;
		float d_c_ab = distancePointSegment(Cx, Cy, Ax, Ay, Bx, By);
		if (epsilonEquals(Cx, Cy, Dx, Dy))
			return d_c_ab;

		float Sx;
		float Sy;

		float pCD = (Cy - Dy) / (Cx - Dx);
		float pAB = (Ay - By) / (Ax - Bx);
		float oCD = Cy - pCD * Cx;
		float oAB = Ay - pAB * Ax;
		Sx = (oAB - oCD) / (pCD - pAB);
		Sy = pCD * Sx + oCD;

		if ((Sx < Ax && Sx < Bx) | (Sx > Ax && Sx > Bx) | (Sx < Cx && Sx < Dx) | (Sx > Cx && Sx > Dx) | (Sy < Ay && Sy < By) | (Sy > Ay && Sy > By) | (Sy < Cy && Sy < Dy) | (Sy > Cy && Sy > Dy)) {
			// No intersection
			float d_b_cd = distancePointSegment(Bx, By, Cx, Cy, Dx, Dy);
			float d_d_ab = distancePointSegment(Dx, Dy, Ax, Ay, Bx, By);
			return MathUtil.min(d_a_cd, d_b_cd, d_c_ab, d_d_ab);
		}
		return 0.f; // intersection exists
	}
	
	/** Replies the angle of a quaternion.
	 * The quaternion is translated into an axis-angle
	 * and the angle is replied.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return the angle of the given quaternion.
	 * 
	 */
	public static float angleQuat(float x, float y, float z, float w) {
		float mag = x*x + y*y + z*z;  
		if ( mag > DOUBLE_PRECISION ) {
		    mag = (float) Math.sqrt(mag);
		    return 2.f * (float) Math.atan2(mag, w);
        }
		return 0.f;
	}

	/**
	 * Compute the unsigned angle between two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the angle between <code>0</code> and <code>PI</code>.
	 */
	public static float angle(float x1, float y1, float z1, float x2, float y2, float z2) {
		float lengths = (float) (Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1) * Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2));
		if (lengths == 0.)
			return Float.NaN;

		// A . B = |A|.|B|.cos(theta)
		float dot = MathUtil.dotProduct(x1, y1, z1, x2, y2, z2) / lengths;
		if (dot < -1.0f)
			dot = -1.0f;
		if (dot > 1.0f)
			dot = 1.0f;
		return (float) Math.acos(dot);
	}

	/**
	 * Compute the unsigned angle between two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the angle between <code>0</code> and <code>PI</code>.
	 */
	public static float angle(float x1, float y1, float x2, float y2) {
		float length1 = (float) Math.sqrt(x1 * x1 + y1 * y1);
		float length2 = (float) Math.sqrt(x2 * x2 + y2 * y2);

		if ((length1 == 0.) || (length2 == 0.))
			return Float.NaN;

		float cx1 = x1;
		float cx2 = x2;
		float cy1 = y1;
		float cy2 = y2;

		// A and B are normalized
		if (length1 != 1) {
			cx1 /= length1;
			cy1 /= length1;
		}

		if (length2 != 1) {
			cx2 /= length2;
			cy2 /= length2;
		}

		// Angle
		// A . B = |A|.|B|.cos(theta) = cos(theta)
		float dot = cx1 * cx2 + cy1 * cy2;
		float angle = (float) Math.acos(dot);

		return angle;
	}

	/**
	 * Replies the matrix that permits to rotate around the given pivot.
	 * <p>
	 * Basically, it computes the three matrix T, R and I respectively the translation matrix from origin to pivot, the rotation matrix, and the translation matrix from pivot to origin.
	 * <p>
	 * <code>M = T * R * I = (T * R) * I</code>
	 * 
	 * @param rotation
	 *            is the rotation quaternion.
	 * @param pivot
	 *            is the point that is the rotation's pivot.
	 * @return the matrix M that permits to rotate about the given rotation around the specified pivot point.
	 */
	public static Matrix4f rotateAround(Quaternion rotation, Tuple3f<?> pivot) {
		Vector3f v = new Vector3f(pivot);

		// T

		Matrix4f T = new Matrix4f();
		T.m00 = T.m11 = T.m22 = T.m33 = 1.f;
		T.setTranslation(v);

		// R
		Matrix4f R = new Matrix4f();
		R.m00 = R.m11 = R.m22 = R.m33 = 1.f;
		R.setRotation(rotation);

		// T' <- T * R (M is replaced by T to limit memory consumption)
		T.mul(R);

		// I
		Matrix4f I = new Matrix4f();
		I.m00 = I.m11 = I.m22 = I.m33 = 1.f;
		v.negate();
		I.setTranslation(v);

		// M <- T' * I
		T.mul(I);

		return T;
	}

	private static boolean makePlaneNormal(Vector3f v1, Vector3f normal, CoordinateSystem3D system) {
		// cross with the least-aligned cardinal basis vector

		assert (epsilonEquals(v1.length(), 1.f));

		int flag = 0;
		flag |= ((epsilonEqualsZero(v1.getX())) ? 1 : 0) << 2;
		flag |= ((epsilonEqualsZero(v1.getY())) ? 1 : 0) << 1;
		flag |= (epsilonEqualsZero(v1.getZ())) ? 1 : 0;
		flag = (~flag) & 0x7;

		assert (flag > 0 && flag <= 7);

		switch (flag) {
		case 1: // 001
		case 3: // 011
			normal.set(0.f, -v1.getZ(), v1.getY());
			break;
		case 4: // 100
		case 5: // 101
			normal.set(v1.getZ(), 0.f, -v1.getX());
			break;
		case 2: // 010
		case 6: // 110
		case 7: // 111
			normal.set(-v1.getY(), v1.getX(), 0.f);
			break;
		default:
			return false;
		}

		return true;
	}

	/**
	 * Replies the matrix that permits to change the orientation of an object looking at the given direction.
	 * <p>
	 * The object is supported to rotate around the point (0,0,0). After the transformation, the object is assumed to look along the x axis, and its up direction is corresponding to the z axis. Consequently, the side direction is corresponding to the y axis (according to the right-hand coordinate system).
	 * <p>
	 * This returned matrix does not change the position of the object.
	 * <p>
	 * If the two given vectors are colinears, an arbitrary up vector is selected.
	 * 
	 * @param eyeDirection
	 *            is the direction of the eye
	 * @param upDirection
	 *            is the desired up direction.
	 * @param system
	 *            defines the up, left and front axis.
	 * @return the transformation matrix that permits to change the orientation
	 */
	public static Matrix4f lookAt(Vector3f eyeDirection, Vector3f upDirection, CoordinateSystem3D system) {
		Matrix4f m = new Matrix4f();

		Vector3f eye = new Vector3f(eyeDirection);
		eye.normalize();

		Vector3f up = new Vector3f(upDirection);
		up.normalize();

		Vector3f side;

		if (system.isRightHanded()) {
			side = MathUtil.crossProductRightHand(eye.getX(), eye.getY(), eye.getZ(), up.getX(), up.getY(), up.getZ());
			// side is at the right
		} else {
			side = MathUtil.crossProductLeftHand(eye.getX(), eye.getY(), eye.getZ(), up.getX(), up.getY(), up.getZ());
			// side is at the left
		}

		float length = side.length();
		if (length == 0.) {
			if (!makePlaneNormal(eye, side, system)) {
				m.setIdentity();
				return m;
			}
			length = side.length();
		}

		side.setX(side.getX() / length);
		side.setY(side.getY() / length);
		side.setZ(side.getZ() / length);

		Vector3f newUp;

		if (system.isRightHanded()) {
			newUp = MathUtil.crossProductRightHand(side.getX(), side.getY(), side.getZ(), eye.getX(), eye.getY(), eye.getZ());
		} else {
			newUp = MathUtil.crossProductLeftHand(side.getX(), side.getY(), side.getZ(), eye.getX(), eye.getY(), eye.getZ());
		}

		newUp.normalize();

		// Face to OX
		int viewColumn, upColumn, sideColumn;
		float toOtherside;

		viewColumn = 0; // OX
		upColumn = sideColumn = -1;
		toOtherside = 0.f;

		switch (system) {
		case XYZ_RIGHT_HAND:
			upColumn = 2; // OZ
			sideColumn = 1; // OY
			toOtherside = -1;
			break;
		case XZY_RIGHT_HAND:
			upColumn = 1; // OY
			sideColumn = 2; // OZ
			toOtherside = 1;
			break;
		case XYZ_LEFT_HAND:
			upColumn = 2; // OZ
			sideColumn = 1; // OY
			toOtherside = -1;
			break;
		case XZY_LEFT_HAND:
			upColumn = 1; // OY
			sideColumn = 2; // OZ
			toOtherside = 1;
			break;
		}

		m.setColumn(viewColumn, eye.getX(), eye.getY(), eye.getZ(), 0.f);
		m.setColumn(upColumn, newUp.getX(), newUp.getY(), newUp.getZ(), 0.f);
		m.setColumn(sideColumn, toOtherside * side.getX(), toOtherside * side.getY(), toOtherside * side.getZ(), 0.f);
		m.setColumn(3, 0.f, 0.f, 0.f, 1.f);

		return m;
	}

	/**
	 * Replies the matrix that permits to change the orientation of an object looking at the given direction. After the transformation, the object is assumed to look along the x axis, and its up direction is corresponding to default coordinate system replied by {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 * <p>
	 * This returned matrix does not change the position of the object.
	 * <p>
	 * If the given vector and the default up vector are colinears, an arbitrary up vector is selected.
	 * 
	 * @param viewDirection
	 *            is view direction, not a rotation.
	 * @return the transformation matrix that permits to change the orientation.
	 * @since 3.0
	 */
	public static Matrix4f lookAt(Direction3D viewDirection) {
		return lookAt(viewDirection, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the matrix that permits to change the orientation of an object looking at the given direction. After the transformation, the object is assumed to look along the x axis, and its up direction is corresponding to default specified coordinate system.
	 * <p>
	 * This returned matrix does not change the position of the object.
	 * <p>
	 * If the given vector and the up vector are colinears, an arbitrary up vector is selected.
	 * 
	 * @param viewDirection
	 *            is view direction, not a rotation.
	 * @param system
	 *            is the coordinate system to use.
	 * @return the transformation matrix that permits to change the orientation.
	 * @since 3.0
	 */
	public static Matrix4f lookAt(Direction3D viewDirection, CoordinateSystem3D system) {
		Vector3f eyeDirection = new Vector3f(viewDirection.x, viewDirection.y, viewDirection.z);
		eyeDirection.normalize();
		Vector3f upDirection = new Vector3f();
		system.getUpVector(upDirection);
		Vector3f side;

		if (system.isRightHanded()) {
			side = MathUtil.crossProductRightHand(upDirection.getX(), upDirection.getY(), upDirection.getZ(), eyeDirection.getX(), eyeDirection.getY(), eyeDirection.getZ());
		} else {
			side = MathUtil.crossProductLeftHand(upDirection.getX(), upDirection.getY(), upDirection.getZ(), eyeDirection.getX(), eyeDirection.getY(), eyeDirection.getZ());
		}
		assert (side != null);
		if ((side.length() == 0.) && (!makePlaneNormal(eyeDirection, side, system))) {
			Matrix4f mat = new Matrix4f();
			mat.setIdentity();
			return mat;
		}
		if (system.isRightHanded()) {
			MathUtil.crossProductRightHand(eyeDirection.getX(), eyeDirection.getY(), eyeDirection.getZ(), side.getX(), side.getY(), side.getZ(), upDirection);
		} else {
			MathUtil.crossProductLeftHand(eyeDirection.getX(), eyeDirection.getY(), eyeDirection.getZ(), side.getX(), side.getY(), side.getZ(), upDirection);
		}
		if (viewDirection.angle != 0.) {
			Matrix4f mat = new Matrix4f();
			mat.set(viewDirection);
			mat.transform(upDirection);
		}
		return lookAt(eyeDirection, upDirection, system);
	}

	/**
	 * Replies the matrix that permits to change the orientation of an object looking at the given direction. After the transformation, the object is assumed to look along the x axis, and its up direction is corresponding to default coordinate system replied by {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 * <p>
	 * This returned matrix does not change the position of the object.
	 * <p>
	 * If the given vector and the default up vector are colinears, an arbitrary up vector is selected.
	 * 
	 * @param rotation
	 *            is a rotation to apply to the viewer.
	 * @return the transformation matrix that permits to change the orientation.
	 */
	public static Matrix4f lookAt(Quaternion rotation) {
		return lookAt(rotation, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the matrix that permits to change the orientation of an object looking at the given direction. After the transformation, the object is assumed to look along the x axis, and its up direction is corresponding to default coordinate system replied by {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 * <p>
	 * This returned matrix does not change the position of the object.
	 * <p>
	 * If the given vector and the up vector are colinears, an arbitrary up vector is selected.
	 * 
	 * @param rotation
	 *            is a rotation to apply to the viewer.
	 * @param system
	 *            is the coordinate system to use.
	 * @return the transformation matrix that permits to change the orientation.
	 */
	public static Matrix4f lookAt(Quaternion rotation, CoordinateSystem3D system) {
		Vector3f eyeDirection = new Vector3f();
		system.getViewVector(eyeDirection);
		Vector3f upDirection = new Vector3f();
		system.getUpVector(upDirection);
		Matrix4f mat = new Matrix4f();
		mat.set(rotation);
		mat.transform(eyeDirection);
		mat.transform(upDirection);
		return lookAt(eyeDirection, upDirection, system);
	}

	/**
	 * Replies the quaternion that corresponds to the given euler's angles.
	 * <p>
	 * The Euler angles are applied in a predefined order:
	 * <ol>
	 * <li>heading : rotation around the up vector;</li>
	 * <li>attitude : rotation around the left vector;</li>
	 * <li>bank : rotation around the front vector.</li>
	 * </ol>
	 * <p>
	 * The used coordinate system is {@link CoordinateSystem3D#getDefaultCoordinateSystem()}
	 * 
	 * @param euler
	 *            are the Euler's angles
	 * @return the quaternion that corresponds to the Euler's angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 */
	public static Quaternion euler2quat(EulerAngle euler) {
		return euler2quat(euler.heading, euler.attitude, euler.bank, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the quaternion that corresponds to the given euler's angles.
	 * <p>
	 * The Euler angles are applied in a predefined order:
	 * <ol>
	 * <li>heading : rotation around the up vector;</li>
	 * <li>attitude : rotation around the left vector;</li>
	 * <li>bank : rotation around the front vector.</li>
	 * </ol>
	 * <p>
	 * The used coordinate system is {@link CoordinateSystem3D#getDefaultCoordinateSystem()}
	 * 
	 * @param heading
	 * @param attitude
	 * @param bank
	 * @return the quaternion that corresponds to the Euler's angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 */
	public static Quaternion euler2quat(float heading, float attitude, float bank) {
		return euler2quat(heading, attitude, bank, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the quaternion that corresponds to the given euler's angles.
	 * <p>
	 * The Euler angles are applied in a predefined order:
	 * <ol>
	 * <li>heading : rotation around the up vector;</li>
	 * <li>attitude : rotation around the left vector;</li>
	 * <li>bank : rotation around the front vector.</li>
	 * </ol>
	 * 
	 * @param heading
	 * @param attitude
	 * @param bank
	 * @param system
	 *            is the coordinate system used to define the up,left and front vectors.
	 * @return the quaternion that corresponds to the Euler's angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	public static Quaternion euler2quat(float heading, float attitude, float bank, CoordinateSystem3D system) {
		assert (system != null);

		float c1 = (float) Math.cos(heading / 2.f);
		float s1 = (float) Math.sin(heading / 2.f);
		float c2 = (float) Math.cos(attitude / 2.f);
		float s2 = (float) Math.sin(attitude / 2.f);
		float c3 = (float) Math.cos(bank / 2.f);
		float s3 = (float) Math.sin(bank / 2.f);

		float x, y, z, w;

		// Source: http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
		// Standard used: XZY_RIGHT_HAND
		float c1c2 = c1 * c2;
		float s1s2 = s1 * s2;
		w = c1c2 * c3 - s1s2 * s3;
		x = c1c2 * s3 + s1s2 * c3;
		y = s1 * c2 * c3 + c1 * s2 * s3;
		z = c1 * s2 * c3 - s1 * c2 * s3;

		Quaternion q = new Quaternion(x, y, z, w);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(q, system);
		return q;
	}

	/**
	 * Replies the Euler's angles that corresponds to the given quaternion.
	 * <p>
	 * The Euler angles are applied in a predefined order:
	 * <ol>
	 * <li>heading : rotation around the up vector;</li>
	 * <li>attitude : rotation around the left vector;</li>
	 * <li>bank : rotation around the front vector.</li>
	 * </ol>
	 * <p>
	 * The used coordinate system is {@link CoordinateSystem3D#getDefaultCoordinateSystem()}
	 * 
	 * @param quaternion
	 *            is the quaternion to convert.
	 * @return the heading, attitude and bank angles.
	 */
	public static EulerAngle quat2euler(Quaternion quaternion) {
		EulerAngle euler = new EulerAngle();
		quat2euler(quaternion, euler, CoordinateSystem3D.getDefaultCoordinateSystem());
		return euler;
	}

	/**
	 * Replies the Euler's angles that corresponds to the given quaternion.
	 * <p>
	 * The Euler angles are applied in a predefined order:
	 * <ol>
	 * <li>heading : rotation around the up vector;</li>
	 * <li>attitude : rotation around the left vector;</li>
	 * <li>bank : rotation around the front vector.</li>
	 * </ol>
	 * <p>
	 * The used coordinate system is {@link CoordinateSystem3D#getDefaultCoordinateSystem()}
	 * 
	 * @param quaternion
	 *            is the quaternion to convert.
	 * @param euler
	 *            is set with the heading, attitude and bank angles.
	 */
	public static void quat2euler(Quaternion quaternion, EulerAngle euler) {
		quat2euler(quaternion, euler, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the Euler's angles that corresponds to the given quaternion.
	 * <p>
	 * The Euler angles are applied in a predefined order:
	 * <ol>
	 * <li>heading : rotation around the up vector;</li>
	 * <li>attitude : rotation around the left vector;</li>
	 * <li>bank : rotation around the front vector.</li>
	 * </ol>
	 * 
	 * @param quaternion
	 *            is the quaternion to convert.
	 * @param euler
	 *            is set with the heading, attitude and bank angles.
	 * @param system
	 *            is the coordinate system used to define the up,left and front vectors.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">Quaternion to Euler</a>
	 */
	public static void quat2euler(Quaternion quaternion, EulerAngle euler, CoordinateSystem3D system) {

		// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Standard used: XZY_RIGHT_HAND
		Quaternion q = new Quaternion(quaternion);
		system.toSystem(q, CoordinateSystem3D.XZY_RIGHT_HAND);

		float sqw = q.getW() * q.getW();
		float sqx = q.getX() * q.getX();
		float sqy = q.getY() * q.getY();
		float sqz = q.getZ() * q.getZ();
		float unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
		float test = q.getX() * q.getY() + q.getZ() * q.getW();

		if (epsilonCompareTo(test, .5f * unit) >= 0) { // singularity at north pole
			euler.set(2.f * (float) Math.atan2(q.getX(), q.getW()), // heading
					DEMI_PI, // attitude
					0.f);
		} else if (epsilonCompareTo(test, -.5f * unit) <= 0) { // singularity at south pole
			euler.set(-2.f * (float) Math.atan2(q.getX(), q.getW()), // heading
					-DEMI_PI, // attitude
					0.f);
		} else {
			euler.set((float) Math.atan2(2.f * q.getY() * q.getW() - 2.f * q.getX() * q.getZ(), sqx - sqy - sqz + sqw),
					(float) Math.asin(2.f * test / unit),
					(float) Math.atan2(2.f * q.getX() * q.getW() - 2.f * q.getY() * q.getZ(), -sqx + sqy - sqz + sqw));
		}
	}

	/**
	 * Convert a rotation matrix to a view vector according to the given default view vector.
	 * <p>
	 * This function assumes that the given default view vector should be replied when the rotation matrix is identity. Basically, the default view vector is rotated by the rotation matrix to obtain the view direction.
	 * 
	 * @param rotation
	 *            is the rotation matrix.
	 * @param system
	 *            is the coordinate system which is defining the up, left and up directions.
	 * @return the 3D view direction and the rolling amount. Caution, this is not a rotation.
	 */
	public static Direction3D rotation2viewVector3D(Quaternion rotation, CoordinateSystem3D system) {
		Matrix4f mat = new Matrix4f();
		mat.set(rotation);

		Vector3f viewDirection = new Vector3f();
		system.getViewVector(viewDirection);
		mat.transform(viewDirection);
		viewDirection.normalize();

		Vector3f originalUpDirection = new Vector3f();
		system.getUpVector(originalUpDirection);
		Vector3f upDirection = new Vector3f(originalUpDirection);
		mat.transform(upDirection);
		upDirection.normalize();

		Direction3D aa = new Direction3D();

		aa.x = viewDirection.getX();
		aa.y = viewDirection.getY();
		aa.z = viewDirection.getZ();

		Vector3f v = MathUtil.crossProductRightHand(originalUpDirection.getX(), originalUpDirection.getY(), originalUpDirection.getZ(), viewDirection.getX(), viewDirection.getY(), viewDirection.getZ());
		v = MathUtil.crossProductRightHand(viewDirection.getX(), viewDirection.getY(), viewDirection.getZ(), v.getX(), v.getY(), v.getZ());

		aa.angle = MathUtil.signedAngle(upDirection.getX(), upDirection.getY(), upDirection.getZ(), v.getX(), v.getY(), v.getZ());

		return aa;
	}

	/**
	 * Convert a rotation matrix to a view vector according to the given default view vector.
	 * <p>
	 * This function assumes that the given default view vector should be replied when the rotation matrix is identity. Basically, the default view vector is rotated by the rotation matrix to obtain the view direction. The replied vector is the projection on the 2D plane of the view vector and the rotated vector.
	 * 
	 * @param rotation
	 *            is the rotation matrix.
	 * @param system
	 *            is the coordinate system which is defining the up, left and front directions.
	 * @return the 2D normalized view direction.
	 */
	public static Direction2D rotation2viewVector2D(Quaternion rotation, CoordinateSystem3D system) {
		Matrix4f mat = new Matrix4f();
		mat.set(rotation);

		Vector3f direction = new Vector3f();
		system.getViewVector(direction);
		mat.transform(direction);

		Vector2f v = system.toCoordinateSystem2D(direction);
		v.normalize();
		return new Direction2D(v);
	}

	/**
	 * Convert a view vector to a rotation matrix according to the given coordinate system.
	 * <p>
	 * Basically, a rotation angle between the given view vector and the default view vector is computed.
	 * 
	 * @param viewVector
	 *            is the 2d view vector.
	 * @param system
	 *            is the coordinate system which is defining the up, left and front directions.
	 * @return the rotation.
	 * @since 3.0
	 */
	public static Direction3D viewVector2rotation2D(Direction2D viewVector, CoordinateSystem3D system) {
		system.toCoordinateSystem2D();
		Vector2f front = CoordinateSystem2D.getViewVector();
		Vector3f up = system.getUpVector();
		return new Direction3D(up.getX(), up.getY(), up.getZ(), signedAngle(front.getX(), front.getY(), viewVector.getX(), viewVector.getY()));
	}

	/**
	 * Convert a rotation matrix to a view vector according to the given default view vector.
	 * <p>
	 * This function assumes that the given default view vector should be replied when the rotation matrix is identity. Basically, the default view vector is rotated by the rotation matrix to obtain the view direction. The replied angle is the angle between the projections on the 2D plane of the view vector and the rotated vector.
	 * 
	 * @param rotation
	 *            is the rotation matrix.
	 * @param system
	 *            is the coordinate system which is defining the up, left and up directions.
	 * @return the 2D view direction.
	 */
	public static float rotation2viewVector1D(Quaternion rotation, CoordinateSystem3D system) {
		Matrix4f mat = new Matrix4f();
		mat.set(rotation);

		Vector3f direction = new Vector3f();
		system.getViewVector(direction);

		Vector2f projectedViewDirection = system.toCoordinateSystem2D(direction);

		mat.transform(direction);

		Vector2f projectedDirection = system.toCoordinateSystem2D(direction);

		return MathUtil.signedAngle(projectedViewDirection.getX(), projectedViewDirection.getY(), projectedDirection.getX(), projectedDirection.getY());
	}

	/**
	 * Convert a rotation matrix to a view vector according to the given default view vector.
	 * <p>
	 * This function assumes that the given default view vector should be replied when the rotation matrix is identity. Basically, the default view vector is rotated by the rotation matrix to obtain the view direction. The replied angle is the angle between the projections on the 2D plane of the view vector and the rotated vector.
	 * <p>
	 * This function assumes the coordinate system replied by {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 * 
	 * @param rotation
	 *            is the rotation matrix.
	 * @return the 2D view direction.
	 */
	public static float rotation2viewVector1D(Quaternion rotation) {
		return rotation2viewVector1D(rotation, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies an up vector which is perpendicular to the given view vector, according to the given coordinate system.
	 * 
	 * @param view
	 *            is the view vector to match to the replied vector.
	 * @param system
	 *            is the coordinate system to use.
	 * @return the computed up vector.
	 */
	public static Vector3f getUpVectorForView(Vector3f view, CoordinateSystem3D system) {
		return getUpVectorForView(view.getX(), view.getY(), view.getZ(), system);
	}

	/**
	 * Replies an up vector which is perpendicular to the given view vector, according to the given coordinate system.
	 * <p>
	 * The used coordinate system is {@link CoordinateSystem3D#getDefaultCoordinateSystem()}
	 * 
	 * @param view
	 *            is the view vector to match to the replied vector.
	 * @return the computed up vector.
	 */
	public static Vector3f getUpVectorForView(Vector3f view) {
		return getUpVectorForView(view.getX(), view.getY(), view.getZ(), CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies an up vector which is perpendicular to the given view vector, according to the given coordinate system.
	 * <p>
	 * The used coordinate system is {@link CoordinateSystem3D#getDefaultCoordinateSystem()}
	 * 
	 * @param viewx
	 *            is the x-coordinate of the view vector to match to the replied vector.
	 * @param viewy
	 *            is the y-coordinate of the view vector to match to the replied vector.
	 * @param viewz
	 *            is the z-coordinate of the view vector to match to the replied vector.
	 * @return the computed up vector.
	 */
	public static Vector3f getUpVectorForView(float viewx, float viewy, float viewz) {
		return getUpVectorForView(viewx, viewy, viewz, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies an up vector which is perpendicular to the given view vector, according to the given coordinate system.
	 * 
	 * @param viewx
	 *            is the x-coordinate of the view vector to match to the replied vector.
	 * @param viewy
	 *            is the y-coordinate of the view vector to match to the replied vector.
	 * @param viewz
	 *            is the z-coordinate of the view vector to match to the replied vector.
	 * @param system
	 *            is the coordinate system to use.
	 * @return the computed up vector.
	 */
	public static Vector3f getUpVectorForView(float viewx, float viewy, float viewz, CoordinateSystem3D system) {
		assert (system != null);
		Vector3f up = new Vector3f();
		system.getUpVector(up);
		perpendicularize(viewx, viewy, viewz, up);
		return up;
	}

	/**
	 * Make the second vector perpendicular to the first one.
	 * <p>
	 * The plane between the two given vectors is coplanar to the plane in which the unchanged vector and the perpendicular plane lie.
	 * 
	 * @param unchangedVector
	 *            is a vector that must not changed.
	 * @param vectorToChange
	 *            is the vector which is updated by this function.
	 */
	public static void perpendicularize(Vector3f unchangedVector, Vector3f vectorToChange) {
		assert (unchangedVector != null);
		perpendicularize(unchangedVector.getX(), unchangedVector.getY(), unchangedVector.getZ(), vectorToChange);
	}

	/**
	 * Make the second vector perpendicular to the first one.
	 * <p>
	 * The plane between the two given vectors is coplanar to the plane in which the unchanged vector and the perpendicular plane lie.
	 * 
	 * @param unchangedVectorX
	 *            is a vector that must not changed.
	 * @param unchangedVectorY
	 *            is a vector that must not changed.
	 * @param unchangedVectorZ
	 *            is a vector that must not changed.
	 * @param vectorToChange
	 *            is the vector which is updated by this function.
	 */
	public static void perpendicularize(float unchangedVectorX, float unchangedVectorY, float unchangedVectorZ, Vector3f vectorToChange) {
		assert (vectorToChange != null);

		float length = vectorToChange.length();

		Vector3f side = MathUtil.crossProductRightHand(unchangedVectorX, unchangedVectorY, unchangedVectorZ, vectorToChange.getX(), vectorToChange.getY(), vectorToChange.getZ());
		MathUtil.crossProductRightHand(side.getX(), side.getY(), side.getZ(), unchangedVectorX, unchangedVectorY, unchangedVectorZ, vectorToChange);
		vectorToChange.normalize();
		vectorToChange.scale(length);
	}

	/**
	 * Replies a perpendicular vector to the given one.
	 * <p>
	 * The coordinate system used to compute the perpendicular vector is given by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @param v
	 * @return a perpendicular unit vector to the vector {@code v}.
	 */
	public static Vector2f perpendicularVector(Vector2f v) {
		Vector2f r = new Vector2f();
		perpendicularVector(v.getX(), v.getY(), r, CoordinateSystem2D.getDefaultCoordinateSystem());
		return r;
	}

	/**
	 * Replies a perpendicular vector to the given one.
	 * <p>
	 * The coordinate system used to compute the perpendicular vector is given by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @param v
	 * @param result
	 *            is a perpendicular unit vector to the vector {@code v}.
	 */
	public static void perpendicularVector(Vector2f v, Vector2f result) {
		perpendicularVector(v.getX(), v.getY(), result, CoordinateSystem2D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies a perpendicular vector to the given one.
	 * <p>
	 * The coordinate system used to compute the perpendicular vector is given by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @param vx
	 * @param vy
	 * @return a perpendicular unit vector to the vector {@code (vx,vy)}.
	 */
	public static Vector2f perpendicularVector(float vx, float vy) {
		Vector2f r = new Vector2f();
		perpendicularVector(vx, vy, r, CoordinateSystem2D.getDefaultCoordinateSystem());
		return r;
	}

	/**
	 * Replies a perpendicular vector to the given one.
	 * 
	 * @param v
	 * @param system
	 *            is the coordinate system used to compute the perpendicular system.
	 * @return a perpendicular unit vector to the vector {@code (vx,vy)} according to the given coordinate system.
	 */
	public static Vector2f perpendicularVector(Vector2f v, CoordinateSystem2D system) {
		Vector2f r = new Vector2f();
		perpendicularVector(v.getX(), v.getY(), r, system);
		return r;
	}

	/**
	 * Replies a perpendicular vector to the given one.
	 * 
	 * @param vx
	 * @param vy
	 * @param system
	 *            is the coordinate system used to compute the perpendicular system.
	 * @return a perpendicular unit vector to the vector {@code (vx,vy)} according to the given coordinate system.
	 */
	public static Vector2f perpendicularVector(float vx, float vy, CoordinateSystem2D system) {
		Vector2f r = new Vector2f();
		perpendicularVector(vx, vy, r, system);
		return r;
	}

	/**
	 * Replies a perpendicular vector to the given one.
	 * 
	 * @param vx
	 * @param vy
	 * @param system
	 *            is the coordinate system used to compute the perpendicular system.
	 * @param result
	 *            is set to a perpendicular unit vector to the vector {@code (vx,vy)} according to the given coordinate system.
	 */
	public static void perpendicularVector(float vx, float vy, Vector2f result, CoordinateSystem2D system) {
		assert (result != null);
		if (system.isRightHanded()) {
			// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), right-handed
			float length = (float) Math.sqrt(vx * vx + vy * vy);
			result.set(vy / length, -(vx / length));
		}
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), left-handed
		float length = (float) Math.sqrt(vx * vx + vy * vy);
		result.set(-(vy / length), vx / length);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by {@code (p1,p2)}, and {@code (p3,p4)}.
	 * 
	 * @param p1
	 *            is the first point of the first line.
	 * @param p2
	 *            is the second point of the first line.
	 * @param p3
	 *            is the first point of the second line.
	 * @param p4
	 *            is the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(Point2f, Point2f, Point2f, Point2f)
	 * @since 3.0
	 */
	public static boolean isParallelLines(Point2f p1, Point2f p2, Point2f p3, Point2f p4) {
		return isParallelLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY());
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by {@code (p1,p2)}, and {@code (p3,p4)}.
	 * <p>
	 * Line through (x<sub>0</sub>,y<sub>0</sub>,z<sub>0</sub>) in direction (a<sub>0</sub>,b<sub>0</sub>,c<sub>0</sub>) and line through (x<sub>1</sub>,yx<sub>1</sub>,zx<sub>1</sub>) in direction (ax<sub>1</sub>,bx<sub>1</sub>,cx<sub>1</sub>): <center><img src="doc-files/parallellines3d.gif" alt="Parallel lines"></center>
	 * <p>
	 * Two lines specified by point and direction are coplanar if and only if the determinant in the numerator is zero. In this case they are concurrent (if the denominator is nonzero) or parallel (if the denominator is zero).
	 * <p>
	 * If you are interested to test if the two lines are collinear, see {@link #isCollinearLines(Point3f, Point3f, Point3f, Point3f)}.
	 * 
	 * @param p1
	 *            is the first point of the first line.
	 * @param p2
	 *            is the second point of the first line.
	 * @param p3
	 *            is the first point of the second line.
	 * @param p4
	 *            is the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(Point3f, Point3f, Point3f, Point3f)
	 * @since 3.0
	 */
	public static boolean isParallelLines(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
		return isParallelLines(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
	}

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * 
	 * @param p1
	 *            is the first point
	 * @param p2
	 *            is the second point
	 * @param p3
	 *            is the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 */
	public static boolean isCollinearPoints(Point2f p1, Point2f p2, Point2f p3) {
		return isCollinearPoints(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	}

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * 
	 * @param p1
	 *            is the first point
	 * @param p2
	 *            is the second point
	 * @param p3
	 *            is the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 */
	public static boolean isCollinearPoints(Point3f p1, Point3f p2, Point3f p3) {
		return isCollinearPoints(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
	}

	/**
	 * Replies if two vectors are colinear.
	 * 
	 * @param v1
	 *            is the first vector
	 * @param v2
	 *            is the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 3.0
	 */
	public static boolean isCollinearVectors(Vector2f v1, Vector2f v2) {
		return isCollinearVectors(v1.getX(), v1.getY(), v2.getX(), v2.getY());
	}

	/**
	 * Replies if two vectors are colinear.
	 * 
	 * @param v1
	 *            is the first vector
	 * @param v2
	 *            is the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 3.0
	 */
	public static boolean isCollinearVectors(Vector3f v1, Vector3f v2) {
		return isCollinearVectors(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ());
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(float, float, float, float, float, float, float, float)}.
	 * 
	 * @param p1
	 *            is the first point of the first line.
	 * @param p2
	 *            is the second point of the first line.
	 * @param p3
	 *            is the first point of the second line.
	 * @param p4
	 *            is the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(Point2f, Point2f, Point2f, Point2f)
	 * @see #isCollinearPoints(Point2f, Point2f, Point2f)
	 * @since 3.0
	 */
	public static boolean isCollinearLines(Point2f p1, Point2f p2, Point2f p3, Point2f p4) {
		return isCollinearLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY());
	}

	/**
	 * Replies if two lines are collinear.
	 * <p>
	 * The given two lines are described respectivaly by {@code (p1,p2)}, and {@code (p3,p4)}.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(Point3f, Point3f, Point3f, Point3f)}.
	 * 
	 * @param p1
	 *            is the first point of the first line.
	 * @param p2
	 *            is the second point of the first line.
	 * @param p3
	 *            is the first point of the second line.
	 * @param p4
	 *            is the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isParallelLines(Point3f, Point3f, Point3f, Point3f)
	 * @since 3.0
	 */
	public static boolean isCollinearLines(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
		return isCollinearLines(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
	}

	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where the specified point {@code (px,py)} lies with respect to the line segment from {@code (x1,y1)} to {@code (x2,y2)}. The return value can be either 1, -1, or 0 and indicates in which direction the specified line must pivot around its first end point, {@code (x1,y1)}, in order to point at the specified point {@code (px,py)}.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the direction that takes the positive X axis towards the negative Y axis. In the default coordinate system used by Java 2D, this direction is counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the direction that takes the positive X axis towards the positive Y axis. In the default coordinate system, this direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line segment. Note that an indicator value of 0 is rare and not useful for determining colinearity because of floating point rounding issues.
	 * <p>
	 * If the point is colinear with the line segment, but not between the end points, then the value will be -1 if the point lies "beyond {@code (x1,y1)}" or 1 if the point lies "beyond {@code (x2,y2)}".
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
	 */
	public static int relativeSegmentCCW(float x1, float y1, float x2, float y2, float px, float py, boolean approximateZero) {
		float cx2 = x2 - x1;
		float cy2 = y2 - y1;
		float cpx = px - x1;
		float cpy = py - y1;
		float ccw = cpx * cy2 - cpy * cx2;
		if ((approximateZero && MathUtil.epsilonEqualsZero(ccw)) || (!approximateZero && ccw == 0.)) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = cpx * cx2 + cpy * cy2;
			if (ccw > 0.) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				cpx -= cx2;
				cpy -= cy2;
				ccw = cpx * cx2 + cpy * cy2;
				if (ccw < 0.) {
					ccw = 0.f;
				}
			}
		}
		return (ccw < 0.) ? -1 : ((ccw > 0.) ? 1 : 0);
	}

	/** Does 3D points are coplanar?
	 * <p>
	 * In geometry, a set of points in space is coplanar if all the points 
	 * lie in the same geometric plane. For example, three distinct points 
	 * are always coplanar; but a fourth point or more added in space can 
	 * exist in another plane, incoplanarly.
	 * 
	 * @param epsilon is the distance error allowed during test.
	 * @param points is the list of points to test.
	 * @return <code>true</code> if all the points are coplanar at epsilon,
	 * otherwise <code>false</code>.
	 */
	public static boolean isCoplanarPoints(float epsilon, Point3f... points) {
		assert(points!=null);
		if (points.length<=3) return true;
		Plane4f plane = new Plane4f(points[0], points[1], points[2]);
		float d;
		for(int i=3; i<points.length; i++) {
			d = plane.distanceTo(points[i]);
			if (Math.abs(d)>epsilon) return false;
		}
		return true;
	}

	/** Does 3D points are coplanar?
	 * <p>
	 * In geometry, a set of points in space is coplanar if all the points 
	 * lie in the same geometric plane. For example, three distinct points 
	 * are always coplanar; but a fourth point or more added in space can 
	 * exist in another plane, incoplanarly.
	 * 
	 * @param epsilon is the distance error allowed during test.
	 * @param points is the list of the triplets of the point coordinates to test.
	 * @return <code>true</code> if all the points are coplanar at epsilon,
	 * otherwise <code>false</code>.
	 */
	public static boolean isCoplanarPoints(float epsilon, float... points) {
		assert(points!=null);
		assert(points.length%3==0);
		if (points.length<=9) return true;
		Plane4f plane = new Plane4f(
				points[0], points[1], points[2],
				points[3], points[4], points[5],
				points[6], points[7], points[8]);
		float d;
		for(int i=9; i<points.length; i+=3) {
			d = plane.distanceTo(points[i], points[i+1], points[i+2]);
			if (Math.abs(d)>epsilon) return false;
		}
		return true;
	}

	/**
	 * Replies the position factory for the intersection point between two lines.
	 * <p>
	 * Caution: This function is able to find the intersection between the lines iff
	 * the two lines are coplanar.
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
	 * @return <code>factor1</code> or {@link float#NaN} if no intersection.
	 * @see #isCoplanarPoints(float, float...)
	 * @since 3.0
	 */
	public static float getLineLineIntersectionFactor(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float Z1 = z2 - z1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;
		float Z2 = z4 - z3;

		boolean isCoplanar = isCoplanarPoints(
				0.f,
				x1, y1, z1,
				x2, y2, z2,
				x3, y3, z3,
				x4, y4, z4);

		if (!isCoplanar) return Float.NaN;

		float Tx = x3 - x1;
		float Ty = y3 - y1;
		float Tz = z3 - z1;

		// determinant is zero when parallel = det(L1,L2)
		float det1 = MathUtil.determinant(X2, Y2, X1, Y1);
		float det2;
		if (det1==0.) {
			det1 = MathUtil.determinant(X2, Z2, X1, Z1);
			if (det1==0.) return Float.NaN;
			det2 = MathUtil.determinant(X2, Z2, Tx, Tz);
		}
		else {
			det2 = MathUtil.determinant(X2, Y2, Tx, Ty);
		}

		return det2 / det1;
	}

	/**
	 * Replies the position factory for the intersection point between two lines.
	 * <p>
	 * Caution: This function is able to find the intersection between the lines iff
	 * the two lines are coplanar.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 * 
	 * @param p1
	 *            is the first point of the first line.
	 * @param p2
	 *            is the second point of the first line.
	 * @param p3
	 *            is the first point of the second line.
	 * @param p4
	 *            is the second point of the second line.
	 * @return <code>factor1</code> or {@link float#NaN} if no intersection.
	 * @see #isCoplanarPoints(float, Point3f...)
	 * @since 3.0
	 */
	public static float getLineLineIntersectionFactor(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
		return getLineLineIntersectionFactor(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
	}

	/**
	 * Replies the intersection point between two lines.
	 * <p>
	 * Caution: This function is able to find the intersection between the lines iff
	 * the two lines are coplanar.
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
	 * @return the intersection point or <code>null</code> if none.
	 * @see #isCoplanarPoints(float, float...)
	 * @since 3.0
	 */
	public static Point3f getLineLineIntersectionPoint(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		float m = getLineLineIntersectionFactor(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (Float.isNaN(m))
			return null;
		return new Point3f(x1 + m * (x2 - x1), y1 + m * (y2 - y1), z1 + m * (z2 - z1));
	}

	/**
	 * Replies the intersection point between two lines.
	 * <p>
	 * Caution: This function is able to find the intersection between the lines iff
	 * the two lines are coplanar.
	 * 
	 * @param p1
	 *            is the first point of the first line.
	 * @param p2
	 *            is the second point of the first line.
	 * @param p3
	 *            is the first point of the second line.
	 * @param p4
	 *            is the second point of the second line.
	 * @return the intersection point or <code>null</code> if none.
	 * @see #isCoplanarPoints(float, Point3f...)
	 * @since 3.0
	 */
	public static Point3f getLineLineIntersectionPoint(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
		float m = getLineLineIntersectionFactor(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		if (Float.isNaN(m))
			return null;
		return new Point3f(p1.getX() + m * (p2.getX() - p1.getX()), p1.getY() + m * (p2.getY() - p1.getY()), p1.getZ() + m * (p2.getZ() - p1.getZ()));
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
	 * @param p1
	 *            is the first point of the first line.
	 * @param p2
	 *            is the second point of the first line.
	 * @param p3
	 *            is the first point of the second line.
	 * @param p4
	 *            is the second point of the second line.
	 * @return <code>factor1</code> or {@link float#NaN} if no intersection.
	 * @since 3.0
	 */
	public static float getLineLineIntersectionFactor(Point2f p1, Point2f p2, Point2f p3, Point2f p4) {
		return getLineLineIntersectionFactor(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY());
	}

	/**
	 * Replies the intersection point between two lines.
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
	 * @return the intersection point or <code>null</code> if none.
	 * @since 3.0
	 */
	public static Point2f getLineLineIntersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float ua = getLineLineIntersectionFactor(x1, y1, x2, y2, x3, y3, x4, y4);
		if (Float.isNaN(ua))
			return null;
		return new Point2f(x1 + ua * (x2 - x1), y1 + ua * (y2 - y1));
	}

	/**
	 * Replies the position factors for the intersection point between two lines.
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
	 * @return <code>factor1</code> and <code>factor2</code> or 
	 * <code>null</code> if no intersection.
	 * @since 4.1
	 */
	public static Pair<Float,Float> getSegmentSegmentIntersectionFactors(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		float det = MathUtil.determinant(X1, Y1, X2, Y2);
		if (det == 0.) return null;

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		float ub = MathUtil.determinant(X1, Y1, x1 - x3, y1 - y3) / det;
		if (ub < 0. || ub > 1.) return null;
		float ua = MathUtil.determinant(X2, Y2, x1 - x3, y1 - y3) / det;
		if (ua < 0. || ua > 1.) return null;
		return new Pair<Float, Float>(ua, ub);
	}

	/**
	 * Replies the intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
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
	 * @return the intersection points or <code>null</code> if none.
	 * @since 4.1
	 */
	public static Pair<Point2f,Point2f> getSegmentSegmentIntersectionPoints(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(x1, y1, x2, y2, x3, y3, x4, y4);
		if (m==null) return null;
		return new Pair<Point2f, Point2f>(
				new Point2f(x1 + m.getA() * (x2 - x1), y1 + m.getA() * (y2 - y1)),
				new Point2f(x3 + m.getB() * (x4 - x3), y3 + m.getB() * (y4 - y3)));
	}

	/**
	 * Replies the average intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
	 * So this function computes the average point of intersection.
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
	 * @since 4.1
	 */
	public static Point2f getSegmentSegmentAverageIntersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(x1, y1, x2, y2, x3, y3, x4, y4);
		if (m==null) return null;
		return new Point2f(
				((x1 + m.getA() * (x2 - x1))  +  (x3 + m.getB() * (x4 - x3))) / 2.,
				((y1 + m.getA() * (y2 - y1))  +  (y3 + m.getB() * (y4 - y3))) / 2.);
	}

	/**
	 * Replies the intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
	 * 
	 * @param p1 is the first point of the first segment.
	 * @param p2 is the second point of the first segment.
	 * @param p3 is the first point of the second segment.
	 * @param p4 is the second point of the seconf segment.
	 * @return the intersection points or <code>null</code> if none.
	 * @since 4.1
	 */
	public static Pair<Point2f,Point2f> getSegmentSegmentIntersectionPoints(Point2f p1, Point2f p2, Point2f p3, Point2f p4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY());
		if (m==null) return null;
		return new Pair<Point2f, Point2f>(
				new Point2f(p1.getX() + m.getA() * (p2.getX() - p1.getX()), p1.getY() + m.getA() * (p2.getY() - p1.getY())),
				new Point2f(p3.getX() + m.getB() * (p4.getX() - p3.getX()), p3.getY() + m.getB() * (p4.getY() - p3.getY())));
	}

	/**
	 * Replies the average intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
	 * So this function computes the average point of intersection.
	 * 
	 * @param p1 is the first point of the first segment.
	 * @param p2 is the second point of the first segment.
	 * @param p3 is the first point of the second segment.
	 * @param p4 is the second point of the seconf segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @since 4.1
	 */
	public static Point2f getSegmentSegmentAverageIntersectionPoint(Point2f p1, Point2f p2, Point2f p3, Point2f p4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY());
		if (m==null) return null;
		return new Point2f(
				((p1.getX() + m.getA() * (p2.getX() - p1.getX()))  +  (p3.getX() + m.getB() * (p4.getX() - p3.getX()))) / 2.,
				((p1.getY() + m.getA() * (p2.getY() - p1.getY()))  +  (p3.getY() + m.getB() * (p4.getY() - p3.getY()))) / 2.);
	}

	/**
	 * Replies the intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param z1
	 *            is the Z coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param z2
	 *            is the Z coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param z3
	 *            is the Z coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @param z4
	 *            is the Z coordinate of the second point of the second segment.
	 * @return the intersection points or <code>null</code> if none.
	 * @since 4.1
	 */
	public static Pair<Point3f,Point3f> getSegmentSegmentIntersectionPoints(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (m==null) return null;
		return new Pair<Point3f, Point3f>(
				new Point3f(x1 + m.getA() * (x2 - x1), y1 + m.getA() * (y2 - y1), z1 + m.getA() * (z2 - z1)),
				new Point3f(x3 + m.getB() * (x4 - x3), y3 + m.getB() * (y4 - y3), z3 + m.getB() * (z4 - z3)));
	}

	/**
	 * Replies the average intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
	 * So this function computes the average point of intersection.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param z1
	 *            is the Z coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param z2
	 *            is the Z coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param z3
	 *            is the Z coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @param z4
	 *            is the Z coordinate of the second point of the second segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @since 4.1
	 */
	public static Point3f getSegmentSegmentAverageIntersectionPoint(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (m==null) return null;
		return new Point3f(
				((x1 + m.getA() * (x2 - x1))  +  (x3 + m.getB() * (x4 - x3))) / 2.,
				((y1 + m.getA() * (y2 - y1))  +  (y3 + m.getB() * (y4 - y3))) / 2.,
				((z1 + m.getA() * (z2 - z1))  +  (z3 + m.getB() * (z4 - z3))) / 2.);
	}

	/**
	 * Replies the intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
	 * 
	 * @param p1 is the first point of the first segment.
	 * @param p2 is the second point of the first segment.
	 * @param p3 is the first point of the second segment.
	 * @param p4 is the second point of the seconf segment.
	 * @return the intersection points or <code>null</code> if none.
	 * @since 4.1
	 */
	public static Pair<Point3f,Point3f> getSegmentSegmentIntersectionPoints(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		if (m==null) return null;
		return new Pair<Point3f, Point3f>(
				new Point3f(p1.getX() + m.getA() * (p2.getX() - p1.getX()), p1.getY() + m.getA() * (p2.getY() - p1.getY()), p1.getZ() + m.getA() * (p2.getZ() - p1.getZ())),
				new Point3f(p3.getX() + m.getB() * (p4.getX() - p3.getX()), p3.getY() + m.getB() * (p4.getY() - p3.getY()), p3.getZ() + m.getB() * (p4.getZ() - p3.getZ())));
	}

	/**
	 * Replies the average intersection points between two segments.
	 * This function replies the points on the two segments. Theoritically,
	 * they must be equal but many times that differ due to computational imprecision.
	 * So this function computes the average point of intersection.
	 * 
	 * @param p1 is the first point of the first segment.
	 * @param p2 is the second point of the first segment.
	 * @param p3 is the first point of the second segment.
	 * @param p4 is the second point of the seconf segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @since 4.1
	 */
	public static Point3f getSegmentSegmentAverageIntersectionPoint(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
		Pair<Float,Float> m = getSegmentSegmentIntersectionFactors(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		if (m==null) return null;
		return new Point3f(
				((p1.getX() + m.getA() * (p2.getX() - p1.getX()))  +  (p3.getX() + m.getB() * (p4.getX() - p3.getX()))) / 2.,
				((p1.getY() + m.getA() * (p2.getY() - p1.getY()))  +  (p3.getY() + m.getB() * (p4.getY() - p3.getY()))) / 2.,
				((p1.getZ() + m.getA() * (p2.getZ() - p1.getZ()))  +  (p3.getZ() + m.getB() * (p4.getZ() - p3.getZ()))) / 2.);
	}

	/**
	 * Replies the intersection point between two segments.
	 * 
	 * @param p1
	 *            is the first point of the first segment.
	 * @param p2
	 *            is the second point of the first segment.
	 * @param p3
	 *            is the first point of the second segment.
	 * @param p4
	 *            is the second point of the second segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @since 3.0
	 */
	public static Point2f getSegmentSegmentIntersectionPoint(Point2f p1, Point2f p2, Point2f p3, Point2f p4) {
		float m = getSegmentSegmentIntersectionFactor(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY());
		if (Float.isNaN(m))
			return null;
		return new Point2f(p1.getX() + m * (p2.getX() - p1.getX()), p1.getY() + m * (p2.getY() - p1.getY()));
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
	 * @param z1
	 *            is the Z coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param z2
	 *            is the Z coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param z3
	 *            is the Z coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @param z4
	 *            is the Z coordinate of the second point of the second segment.
	 * @return <code>factor1</code> or {@link float#NaN} if no intersection.
	 * @since 3.0
	 */
	public static float getSegmentSegmentIntersectionFactor(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {

		Vector3f u = new Vector3f(x1 - x3, y1 - y3, z1 - z3);
		Vector3f dirA = new Vector3f(x2 - x1, y2 - y1, z2 - z1);
		Vector3f dirB = new Vector3f(x4 - x3, y4 - y3, z4 - z3);

		float a = dirA.dot(dirA);
		float b = dirA.dot(dirB);
		float c = dirB.dot(dirB);
		float d = dirA.dot(u);
		float e = dirB.dot(u);
		float det = a * c - b * b, sNum, tNum, sDenom = det, tDenom = det;

		if(epsilonEquals(det, 0.0f)) {
			return Float.NaN;
		}

		sNum = b * e - c * d;
		tNum = a * e - b * d;

		float s = sNum / sDenom;
		float t = tNum / tDenom;

		if (s < 0.0d || s > 1.0d) return Float.NaN;
		if (t < 0.0d || t > 1.0d) return Float.NaN;
		return t;
	}

	/**
	 * Replies the position factors for the intersection point between two lines.
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
	 * @param z1
	 *            is the Z coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param z2
	 *            is the Z coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param z3
	 *            is the Z coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @param z4
	 *            is the Z coordinate of the second point of the second segment.
	 * @return <code>factor1</code> and <code>factor2</code> or <code>null</code>.
	 * @since 4.1
	 */
	public static Pair<Float,Float> getSegmentSegmentIntersectionFactors(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {

		Vector3f u = new Vector3f(x1 - x3, y1 - y3, z1 - z3);
		Vector3f dirA = new Vector3f(x2 - x1, y2 - y1, z2 - z1);
		Vector3f dirB = new Vector3f(x4 - x3, y4 - y3, z4 - z3);

		float a = dirA.dot(dirA);
		float b = dirA.dot(dirB);
		float c = dirB.dot(dirB);
		float d = dirA.dot(u);
		float e = dirB.dot(u);
		float det = a * c - b * b, sNum, tNum, sDenom = det, tDenom = det;

		if(epsilonEquals(det, 0.0f)) {
			return null;
		}

		sNum = b * e - c * d;
		tNum = a * e - b * d;

		float s = sNum / sDenom;
		float t = tNum / tDenom;

		if (s < 0.0d || s > 1.0d) return null;
		if (t < 0.0d || t > 1.0d) return null;
		return new Pair<Float,Float>(s, t);
	}

	/**
	 * Replies the intersection point between two segments.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param z1
	 *            is the Z coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param z2
	 *            is the Z coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param z3
	 *            is the Z coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @param z4
	 *            is the Z coordinate of the second point of the second segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @since 3.0
	 */
	public static Point3f getSegmentSegmentIntersectionPoint(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		float m = getSegmentSegmentIntersectionFactor(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (Float.isNaN(m))
			return null;
		return new Point3f(x1 + m * (x2 - x1), y1 + m * (y2 - y1), z1 + m * (z2 - z1));
	}

	/**
	 * Replies the intersection point between two segments.
	 * 
	 * @param p1
	 *            is the first point of the first segment.
	 * @param p2
	 *            is the second point of the first segment.
	 * @param p3
	 *            is the first point of the second segment.
	 * @param p4
	 *            is the second point of the second segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @since 3.0
	 */
	public static Point3f getSegmentSegmentIntersectionPoint(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
		float m = getSegmentSegmentIntersectionFactor(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		if (Float.isNaN(m))
			return null;
		return new Point3f(p1.getX() + m * (p2.getX() - p1.getX()), p1.getY() + m * (p2.getY() - p1.getY()), p1.getZ() + m * (p2.getZ() - p1.getZ()));
	}

	/**
	 * Interpolate the coordinate of a point on a line according to a factor.
	 * <p>
	 * If ratio equals to {@code 0}, the interpolated point is equal to the first segment point. If ratio equals to {@code 1}, the interpolated point is equal to the second segment point. If inside {@code ]0;1[}, the interpolated point is between the two segment points. If inside {@code ]-inf;0[}, the interpolated point is outside on the side of the first segment point. If inside {@code ]1;+inf[}, the interpolated point is outside on the side of the second segment point.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the line.
	 * @param y1
	 *            is the Y coordinate of the first point of the line.
	 * @param z1
	 *            is the Z coordinate of the first point of the line.
	 * @param x2
	 *            is the X coordinate of the second point of the line.
	 * @param y2
	 *            is the Y coordinate of the second point of the line.
	 * @param z2
	 *            is the Z coordinate of the second point of the line.
	 * @param ratio
	 *            is the position ratio.
	 * @return the intersection point, never <code>null</code>
	 * @see #interpolateSegment(float, float, float, float, float, float, float)
	 * @since 3.0
	 */
	public static Point3f interpolateLine(float x1, float y1, float z1, float x2, float y2, float z2, float ratio) {
		// P = P1 + ratio * (P2-P1)
		return new Point3f(x1 + ratio * (x2 - x1), y1 + ratio * (y2 - y1), z1 + ratio * (z2 - z1));
	}

	/**
	 * Interpolate the coordinate of a point on a line according to a factor.
	 * <p>
	 * If ratio equals to {@code ]-inf;0]}, the interpolated point is equal to the first segment point. If ratio equals to {@code [1;+inf[}, the interpolated point is equal to the second segment point. If inside {@code ]0;1[}, the interpolated point is between the two segment points.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the segment.
	 * @param z1
	 *            is the Z coordinate of the first point of the segment.
	 * @param x2
	 *            is the X coordinate of the second point of the segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the segment.
	 * @param z2
	 *            is the Z coordinate of the second point of the segment.
	 * @param ratio
	 *            is the position ratio.
	 * @return the intersection point, never <code>null</code>
	 * @see #interpolateLine(float, float, float, float, float, float, float)
	 * @since 3.0
	 */
	public static Point3f interpolateSegment(float x1, float y1, float z1, float x2, float y2, float z2, float ratio) {
		if (ratio <= 0.)
			return new Point3f(x1, y1, z1);
		if (ratio >= 1.)
			return new Point3f(x2, y2, z2);
		// P = P1 + ratio * (P2-P1)
		return new Point3f(x1 + ratio * (x2 - x1), y1 + ratio * (y2 - y1), z1 + ratio * (z2 - z1));
	}

	/**
	 * Interpolate the coordinate of a point on a line according to a factor.
	 * <p>
	 * If ratio equals to {@code 0}, the interpolated point is equal to the first segment point. If ratio equals to {@code 1}, the interpolated point is equal to the second segment point. If inside {@code ]0;1[}, the interpolated point is between the two segment points. If inside {@code ]-inf;0[}, the interpolated point is outside on the side of the first segment point. If inside {@code ]1;+inf[}, the interpolated point is outside on the side of the second segment point.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the line.
	 * @param y1
	 *            is the Y coordinate of the first point of the line.
	 * @param x2
	 *            is the X coordinate of the second point of the line.
	 * @param y2
	 *            is the Y coordinate of the second point of the line.
	 * @param ratio
	 *            is the position ratio.
	 * @return the intersection point, never <code>null</code>
	 * @see #interpolateSegment(float, float, float, float, float)
	 * @since 3.0
	 */
	public static Point2f interpolateLine(float x1, float y1, float x2, float y2, float ratio) {
		// P = P1 + ratio * (P2-P1)
		return new Point2f(x1 + ratio * (x2 - x1), y1 + ratio * (y2 - y1));
	}

	/**
	 * Interpolate the coordinate of a point on a line according to a factor.
	 * <p>
	 * If ratio equals to {@code ]-inf;0]}, the interpolated point is equal to the first segment point. If ratio equals to {@code [1;+inf[}, the interpolated point is equal to the second segment point. If inside {@code ]0;1[}, the interpolated point is between the two segment points.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the segment.
	 * @param x2
	 *            is the X coordinate of the second point of the segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the segment.
	 * @param ratio
	 *            is the position ratio.
	 * @return the intersection point, never <code>null</code>
	 * @see #interpolateLine(float, float, float, float, float)
	 * @since 3.0
	 */
	public static Point2f interpolateSegment(float x1, float y1, float x2, float y2, float ratio) {
		if (ratio <= 0.)
			return new Point2f(x1, y1);
		if (ratio >= 1.)
			return new Point2f(x2, y2);
		// P = P1 + ratio * (P2-P1)
		return new Point2f(x1 + ratio * (x2 - x1), y1 + ratio * (y2 - y1));
	}

	/**
	 * Computes closest points C1 and C2 of S1(s)=P1+s*(Q1-P1) and S2(t)=P2+t*(Q2-P2), returning s and t. 
	 * Function result is squared distance between between S1(s) and S2(t) 
	 * see Real Time Collision book page 149
	 * @param p1
	 *            - first point of the first segment.
	 * @param q1
	 *            - second point of the first segment.
	 * @param p2
	 *            - first point of the second segment.
	 * @param q2
	 *            - second point of the second segment.
	 * @param s is the ratio on the first segment. Could be <code>null</code>
	 * @param t is the ratio on the second segment. Could be <code>null</code>
	 * @param c1
	 *            - the first closest point, filled by this method. Could be <code>null</code>.
	 * @param c2
	 *            - the second closest point, filled by this method. Could be <code>null</code>.
	 * @return squared distance between between S1(s) and S2(t)
	 * @since 4.0
	 */
	public static float closestPointSegmentSegment(Point3f p1, Point3f q1, Point3f p2, Point3f q2, OutputParameter<Float> s, OutputParameter<Float> t, Point3f c1, Point3f c2) {
		return closestPointSegmentSegment(
				p1.getX(), p1.getY(), p1.getZ(),
				q1.getX(), q1.getY(), q1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(),
				q2.getX(), q2.getY(), q2.getZ(),
				s, t,
				c1, c2);
	}

	/**
	 * Computes closest points C1 and C2 of S1(s)=P1+s*(Q1-P1) and S2(t)=P2+t*(Q2-P2), returning s and t. 
	 * Function result is squared distance between between S1(s) and S2(t) 
	 * see Real Time Collision book page 149
	 * @param p1x
	 *            - x coordinate of the first point of the first segment.
	 * @param p1y
	 *            - y coordinate of the first point of the first segment.
	 * @param p1z
	 *            - z coordinate of the first point of the first segment.
	 * @param q1x
	 *            - x coordinate of the second point of the first segment.
	 * @param q1y
	 *            - y coordinate of the second point of the first segment.
	 * @param q1z
	 *            - z coordinate of the second point of the first segment.
	 * @param p2x
	 *            - x coordinate of the first point of the second segment.
	 * @param p2y
	 *            - y coordinate of the first point of the second segment.
	 * @param p2z
	 *            - z coordinate of the first point of the second segment.
	 * @param q2x
	 *            - x coordinate of the second point of the second segment.
	 * @param q2y
	 *            - y coordinate of the second point of the second segment.
	 * @param q2z
	 *            - z coordinate of the second point of the second segment.
	 * @param s is the ratio on the first segment. Could be <code>null</code>
	 * @param t is the ratio on the second segment. Could be <code>null</code>
	 * @param c1
	 *            - the first closest point, filled by this method. Could be <code>null</code>.
	 * @param c2
	 *            - the second closest point, filled by this method. Could be <code>null</code>.
	 * @return squared distance between between S1(s) and S2(t)
	 * @since 4.0
	 */
	public static float closestPointSegmentSegment(
			float p1x, float p1y, float p1z,
			float q1x, float q1y, float q1z,
			float p2x, float p2y, float p2z,
			float q2x, float q2y, float q2z,
			OutputParameter<Float> s,
			OutputParameter<Float> t,
			Point3f c1,
			Point3f c2) {
		float EPSILON = MathUtil.getEpsilon();
		float innerS = Float.NaN, innerT = Float.NaN;

		Point3f innerC1 = (c1==null) ? new Point3f() : c1;
		Point3f innerC2 = (c2==null) ? new Point3f() : c2;

		// the closest points

		// Direction vector of segment S1
		Vector3f d1 = new Vector3f(q1x-p1x, q1y-p1y, q1z-p1z);

		// Direction vector of segment S2
		Vector3f d2 = new Vector3f(q2x-p2x, q2y-p2y, q2z-p2z);

		Vector3f r = new Vector3f(p1x-p2x, p1y-p2y, p1z-p2z);

		float a = d1.dot(d1); // Squared length of segment S1, always nonnegative
		float e = d2.dot(d2); // Squared length of segment S2, always nonnegative
		float f = d2.dot(r);

		// Check if either or both segments degenerate into points
		if (a <= EPSILON && e <= EPSILON) {
			// Both segments degenerate into points
			innerS = 0.f;
			innerT = 0.f;
			innerC1.set(p1x, p1y, p1z);
			innerC2.set(p2x, p2y, p2z);
			Vector3f c1minusc2 = new Vector3f(innerC1);
			c1minusc2.sub(innerC2);
			Vector3f c2minusc1 = new Vector3f(innerC2);
			c2minusc1.sub(innerC1);
			return Math.abs(c1minusc2.dot(c2minusc1));
		}

		if (a <= EPSILON) {
			// First segment degenerates into a point
			innerS = 0.f;
			innerT = f / e;// s = 0 => t = (b*s + f) / e = f / e
			innerT = MathUtil.clamp(innerT, 0.f, 1.f);
		} else {
			float c = d1.dot(r);
			if (e <= EPSILON) {
				// Second segment degenerates into a point
				innerT = 0.f;
				innerS = MathUtil.clamp(-c / a, 0.f, 1.f); // t = 0 => s = (b*t - c) / a = -c / a
			} else {
				// The general nondegenerate case starts here
				float b = d1.dot(d2);
				float denom = a * e - b * b; // Always nonnegative
				// If segments not parallel, compute closest point on L1 to L2 and
				// clamp to segment S1. Else pick arbitrary s (here 0)
				if (denom != 0.f) {
					innerS = MathUtil.clamp((b * f - c * e) / denom, 0.f, 1.f);
				} else
					innerS = 0.f;
				// Compute point on L2 closest to S1(s) using
				// t = Dot((P1 + D1*s) - P2,D2) / Dot(D2,D2) = (b*s + f) / e
				innerT = (b * innerS + f) / e;
				// If t in [0,1] done. Else clamp t, recompute s for the new value
				// of t using s = Dot((P2 + D2*t) - P1,D1) / Dot(D1,D1)= (t*b - c) / a
				// and clamp s to [0, 1]
				if (innerT < 0.f) {
					innerT = 0.f;
					innerS = MathUtil.clamp(-c / a, 0.f, 1.f);
				} else if (innerT > 1.f) {
					innerT = 1.f;
					innerS = MathUtil.clamp((b - c) / a, 0.f, 1.f);
				}
			}
		}
		// c1 = p1 + d1 * s;
		d1.scale(innerS);
		innerC1.set(p1x, p1y, p1z);
		innerC1.add(d1);

		// c2 = p2 + d2 * t;
		d2.scale(innerT);
		innerC2.set(p2x, p2y, p2z);
		innerC2.add(d2);

		Vector3f c1minusc2 = new Vector3f(innerC1);
		c1minusc2.sub(innerC2);
		Vector3f c2minusc1 = new Vector3f(innerC2);
		c2minusc1.sub(innerC1);

		if (s!=null) s.set(innerS);
		if (t!=null) t.set(innerT);

		return Math.abs(c1minusc2.dot(c2minusc1));
	}


	/** Compute the right-handed cross-product of two vectors.
	 * <p>
	 * The right-handed cross product is described by the following figure:<br>
	 * <a href="doc-files/right_handed_cross_product.png"><img src="doc-files/right_handed_cross_product.png" alt="[Right Handed Cross Product]" width="200" border="0"></a>
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the cross-product.
	 * @see #crossProductLeftHand(float, float, float, float, float, float)
	 * @see #crossProductRightHand(float, float, float, float, float, float, Vector3f)
	 */
	public static Vector3f crossProductRightHand(float x1, float y1, float z1, float x2, float y2, float z2) {
		float x = y1*z2 - z1*y2;
		float y = z1*x2 - x1*z2;
		float z = x1*y2 - y1*x2;
		return new Vector3f(x,y,z);
	}

	/** Compute the right-handed cross-product of two vectors.
	 * <p>
	 * The right-handed cross product is described by the following figure:<br>
	 * <a href="doc-files/right_handed_cross_product.png"><img src="doc-files/right_handed_cross_product.png" alt="[Right Handed Cross Product]" width="200" border="0"></a>
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param result
	 * @see #crossProductLeftHand(float, float, float, float, float, float, Vector3f)
	 * @see #crossProductRightHand(float, float, float, float, float, float)
	 */
	public static void crossProductRightHand(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f result) {
		assert(result!=null);
		float x = y1*z2 - z1*y2;
		float y = z1*x2 - x1*z2;
		float z = x1*y2 - y1*x2;
		result.set(x,y,z);
	}
	
	/** Compute the left-handed cross-product of two vectors.
	 * <p>
	 * The left-handed cross product is described by the following figure:<br>
	 * <a href="doc-files/left_handed_cross_product.png"><img src="doc-files/left_handed_cross_product.png" alt="[Left Handed Cross Product]" width="200" border="0"></a>
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the cross-product.
	 * @see #crossProductLeftHand(float, float, float, float, float, float, Vector3f)
	 * @see #crossProductRightHand(float, float, float, float, float, float)
	 */
	public static Vector3f crossProductLeftHand(float x1, float y1, float z1, float x2, float y2, float z2) {
		float x = y2*z1 - z2*y1;
		float y = z2*x1 - x2*z1;
		float z = x2*y1 - y2*x1;
		return new Vector3f(x,y,z);
	}

	/** Compute the left-handed cross-product of two vectors.
	 * <p>
	 * The left-handed cross product is described by the following figure:<br>
	 * <a href="doc-files/left_handed_cross_product.png"><img src="doc-files/left_handed_cross_product.png" alt="[Left Handed Cross Product]" width="200" border="0"></a>
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param result
	 * @see #crossProductRightHand(float, float, float, float, float, float, Vector3f)
	 * @see #crossProductLeftHand(float, float, float, float, float, float)
	 */
	public static void crossProductLeftHand(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f result) {
		assert(result!=null);
		float x = y2*z1 - z2*y1;
		float y = z2*x1 - x2*z1;
		float z = x2*y1 - y2*x1;
		result.set(x,y,z);
	}
	

	/**
	 * Compute the covariance matrix for the given set of points.
	 * <p>
	 * The computed matrix is symmetric.
	 * 
	 * @param cov is the matrix to fill with the covariance elements.
	 * @param points are the points.
	 * @return the mean of the points.
	 */
	public static Tuple3f cov(Matrix3f cov, Tuple3f... points) {
		return cov(cov, Arrays.asList(points));
	}

	/**
	 * Compute the covariance matrix for the given set of points.
	 * <p>
	 * The computed matrix is symmetric.
	 * 
	 * @param cov is the matrix to fill with the covariance elements.
	 * @param points are the points.
	 * @return the mean of the points.
	 */
	public static Tuple2f cov(Matrix2f cov, Tuple2f... points) {
		return cov(cov, Arrays.asList(points));
	}

	/**
	 * Compute the covariance matrix for the given set of points.
	 * <p>
	 * The computed matrix is symmetric.
	 * 
	 * @param cov is the matrix to fill with the covariance elements.
	 * @param points are the points.
	 * @return the mean of the points.
	 */
	public static Tuple2f cov(Matrix2f cov, Iterable<? extends Tuple2f> points) {
		assert(cov!=null);

		cov.setZero();
		
		// Compute the mean m
		Point2f m = new Point2f();
		int count = 0;
		for(Tuple2f p : points) {
			m.add(p);
			count ++;
		}
		
		if (count==0) return null;
		
		m.scale(1.f/count);
		
		// Compute the covariance term [Gottshalk2000]
		// c_ij = sum(p'_i * p'_j) / n
		// c_ij = sum((p_i - m_i) * (p_j - m_j)) / n
		for(Tuple2f p : points) {
			cov.m00 += (p.getX() - m.getX()) * (p.getX() - m.getX());
			cov.m01 += (p.getX() - m.getX()) * (p.getY() - m.getY()); // same as m10
			//cov.m10 += (p.getY() - m.getY()) * (p.getX() - m.getX()); // same as m01
			cov.m11 += (p.getY() - m.getY()) * (p.getY() - m.getY());
		}
		
		cov.m00 /= count;
		cov.m01 /= count;
		cov.m10 = cov.m01;
		cov.m11 /= count;
		
		return m;
	}

	/**
	 * Compute the covariance matrix for the given set of points.
	 * <p>
	 * The computed matrix is symmetric.
	 * 
	 * @param cov is the matrix to fill with the covariance elements.
	 * @param points are the points.
	 * @return the mean of the points.
	 */
	public static Tuple3f cov(Matrix3f cov, Iterable<? extends Tuple3f> points) {
		assert(cov!=null);

		cov.setZero();
		
		// Compute the mean m
		Point3f m = new Point3f();
		int count = 0;
		for(Tuple3f p : points) {
			m.add(p);
			count ++;
		}
		
		if (count==0) return null;
		
		m.scale(1.f/count);
		
		// Compute the covariance term [Gottshalk2000]
		// c_ij = sum(p'_i * p'_j) / n
		// c_ij = sum((p_i - m_i) * (p_j - m_j)) / n
		for(Tuple3f p : points) {
			cov.m00 += (p.getX() - m.getX()) * (p.getX() - m.getX());
			cov.m01 += (p.getX() - m.getX()) * (p.getY() - m.getY()); // same as m10
			cov.m02 += (p.getX() - m.getX()) * (p.getZ() - m.getZ()); // same as m20
			//cov.m10 += (p.getY() - m.getY()) * (p.getX() - m.getX()); // same as m01
			cov.m11 += (p.getY() - m.getY()) * (p.getY() - m.getY());
			cov.m12 += (p.getY() - m.getY()) * (p.getZ() - m.getZ()); // same as m21
			//cov.m20 += (p.getZ() - m.getZ()) * (p.getX() - m.getX()); // same as m02
			//cov.m21 += (p.getZ() - m.getZ()) * (p.getY() - m.getY()); // same as m12
			cov.m22 += (p.getZ() - m.getZ()) * (p.getZ() - m.getZ());
		}
		
		cov.m00 /= count;
		cov.m01 /= count;
		cov.m02 /= count;
		cov.m10 = cov.m01;
		cov.m11 /= count;
		cov.m12 /= count;
		cov.m20 = cov.m02;
		cov.m21 = cov.m12;
		cov.m22 /= count;
		
		return m;
	}

	/**
	 * Solving quadratic polynomials of the form {@code at<sup>2</sup> + bt + c = 0}.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return the values for {@code t}
	 * @see "Mathematics for 3D Game Programming and Computer Graphics, 2nd edition; pp.134." 
	 */
	public static float[] solvesQuadraticPolynomials(float a, float b, float c) {
		float discriminant = b*b-4.f*a*c;
		if (discriminant<0.) {
			// no root
			return new float[0];
		}
		if (discriminant>0.) {
			// two roots
			float u = (float) Math.sqrt(discriminant);
			return new float[] {
				(-b + u) / (2.f*a),
				(-b - u) / (2.f*a),
			};
		}
		// one root
		return new float[] {
			-b / (2.f*a)
		};
	}
	

	/**
	 * Compute the eigenvectors of the given symmetric matrix
	 * according to the Jacobi Cyclic Method.
	 * 
	 * @param matrix is the symmetric matrix.
	 * @param eigenVectors are the matrix of vectors to fill. Eigen vectors are the
	 * columns of the matrix.
	 * @return the eigenvalues which are corresponding to the <var>eigenVectors</var> columns.
	 * @see #eigenVectorsOfSymmetricMatrix(Matrix3, Matrix3) 
	 */
	public static float[] eigenVectorsOfSymmetricMatrix(Matrix2f matrix, Matrix2f eigenVectors) {

		// Copy values up to the diagonal
		float m11 = matrix.getElement(0,0);
		float m12 = matrix.getElement(0,1);
		float m22 = matrix.getElement(1,1);
		
		eigenVectors.setIdentity();
		
		boolean sweepsConsumed = true;
		int i;
		float u, u2, u2p1, t, c, s;
		float ri0, ri1;
		
		for(int a=0; a<JACOBI_MAX_SWEEPS; ++a) {
			
			// Exit loop if off-diagonal entries are small enough
			if ((Math.abs(m12) < MathConstants.EPSILON)) {
				sweepsConsumed = false;
				break;
			}
			
			// Annihilate (1,2) entry
			if (m12 != 0.) {
				u = (m22 - m11) *.5f / m12;
				u2 = u*u;
				u2p1 = u2 + 1.f;
				
				if (u2p1!=u2)
					t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
				else
					t = .5f / u;
				
				c = (float) (1.f / Math.sqrt(t*t + 1));
				s = c * t;
				
				m11 -= t * m12;
				m22 += t * m12;
				m12 = 0.f;
				
				for(i=0; i<2; ++i) {
					ri0 = eigenVectors.getElement(i,0);
					ri1 = eigenVectors.getElement(i,1);
					eigenVectors.setElement(i, 0, c * ri0 - s * ri1);
					eigenVectors.setElement(i, 1, s * ri0 + c * ri1);
				}
			}
		}
		
		assert(!sweepsConsumed) : "Sweep count consumed during eigenvector computation"; //$NON-NLS-1$
		
		// eigenvalues are on the diagonal
		return new float[] { m11, m22 };
	}
	

	/**
	 * Compute the eigenvectors of the given symmetric matrix
	 * according to the Jacobi Cyclic Method.
	 * <p>
	 * Given the n x n real symmetric matrix A, the routine 
	 * Jacobi_Cyclic_Method calculates the eigenvalues and 
	 * eigenvectors of A by successively sweeping through the 
	 * matrix A annihilating off-diagonal non-zero elements 
	 * by a rotation of the row and column in which the 
	 * non-zero element occurs.  
	 * <p>
	 * The Jacobi procedure for finding the eigenvalues and eigenvectors of a
	 * symmetric matrix A is based on finding a similarity transformation
	 * which diagonalizes A.  The similarity transformation is given by a
	 * product of a sequence of orthogonal (rotation) matrices each of which
	 * annihilates an off-diagonal element and its transpose.  The rotation
	 * effects only the rows and columns containing the off-diagonal element 
	 * and its transpose, i.e. if a[i][j] is an off-diagonal element, then
	 * the orthogonal transformation rotates rows a[i][] and a[j][], and
	 * equivalently it rotates columns a[][i] and a[][j], so that a[i][j] = 0
	 * and a[j][i] = 0.
	 * The cyclic Jacobi method considers the off-diagonal elements in the
	 * following order: (0,1),(0,2),...,(0,n-1),(1,2),...,(n-2,n-1).  If the
	 * the magnitude of the off-diagonal element is greater than a treshold,
	 * then a rotation is performed to annihilate that off-diagnonal element.
	 * The process described above is called a sweep.  After a sweep has been
	 * completed, the threshold is lowered and another sweep is performed
	 * with the new threshold. This process is completed until the final
	 * sweep is performed with the final threshold.
	 * The orthogonal transformation which annihilates the matrix element
	 * a[k][m], k != m, is Q = q[i][j], where q[i][j] = 0 if i != j, i,j != k
	 * i,j != m and q[i][j] = 1 if i = j, i,j != k, i,j != m, q[k][k] =
	 * q[m][m] = cos(phi), q[k][m] = -sin(phi), and q[m][k] = sin(phi), where
	 * the angle phi is determined by requiring a[k][m] -> 0.  This condition
	 * on the angle phi is equivalent to<br>
	 * cot(2 phi) = 0.5 * (a[k][k] - a[m][m]) / a[k][m]<br>
	 * Since tan(2 phi) = 2 tan(phi) / (1.0 - tan(phi)^2),<br>
	 * tan(phi)^2 + 2cot(2 phi) * tan(phi) - 1 = 0.<br>
	 * Solving for tan(phi), choosing the solution with smallest magnitude,
	 * tan(phi) = - cot(2 phi) + sgn(cot(2 phi)) sqrt(cot(2phi)^2 + 1).
	 * Then cos(phi)^2 = 1 / (1 + tan(phi)^2) and sin(phi)^2 = 1 - cos(phi)^2.
	 * Finally by taking the sqrts and assigning the sign to the sin the same
	 * as that of the tan, the orthogonal transformation Q is determined.
	 * Let A" be the matrix obtained from the matrix A by applying the
	 * similarity transformation Q, since Q is orthogonal, A" = Q'AQ, where Q'
	 * is the transpose of Q (which is the same as the inverse of Q).  Then
	 * a"[i][j] = Q'[i][p] a[p][q] Q[q][j] = Q[p][i] a[p][q] Q[q][j],
	 * where repeated indices are summed over.
	 * If i is not equal to either k or m, then Q[i][j] is the Kronecker
	 * delta.   So if both i and j are not equal to either k or m,
	 * a"[i][j] = a[i][j].
	 * If i = k, j = k,<br>
	 * a"[k][k] = a[k][k]*cos(phi)^2 + a[k][m]*sin(2 phi) + a[m][m]*sin(phi)^2<br>
	 * If i = k, j = m,<br>
	 * a"[k][m] = a"[m][k] = 0 = a[k][m]*cos(2 phi) + 0.5 * (a[m][m] - a[k][k])*sin(2 phi)<br>
	 * If i = k, j != k or m,<br>
	 * a"[k][j] = a"[j][k] = a[k][j] * cos(phi) + a[m][j] * sin(phi)<br>
	 * If i = m, j = k, a"[m][k] = 0<br>
	 * If i = m, j = m,<br>
	 * a"[m][m] = a[m][m]*cos(phi)^2 - a[k][m]*sin(2 phi) + a[k][k]*sin(phi)^2<br>
	 * If i= m, j != k or m,<br>
	 * a"[m][j] = a"[j][m] = a[m][j] * cos(phi) - a[k][j] * sin(phi)
	 * <p>
	 * If X is the matrix of normalized eigenvectors stored so that the ith
	 * column corresponds to the ith eigenvalue, then AX = X Lamda, where
	 * Lambda is the diagonal matrix with the ith eigenvalue stored at
	 * Lambda[i][i], i.e. X'AX = Lambda and X is orthogonal, the eigenvectors
	 * are normalized and orthogonal.  So, X = Q1 Q2 ... Qs, where Qi is
	 * the ith orthogonal matrix,  i.e. X can be recursively approximated by
	 * the recursion relation X" = X Q, where Q is the orthogonal matrix and
	 * the initial estimate for X is the identity matrix.<br>
	 * If j = k, then x"[i][k] = x[i][k] * cos(phi) + x[i][m] * sin(phi),<br>
	 * if j = m, then x"[i][m] = x[i][m] * cos(phi) - x[i][k] * sin(phi), and<br>
	 * if j != k and j != m, then x"[i][j] = x[i][j].
	 *  
	 * @param matrix is the symmetric matrix.
	 * @param eigenVectors are the matrix of vectors to fill. Eigen vectors are the
	 * columns of the matrix.
	 * @return the eigenvalues which are corresponding to the <var>eigenVectors</var> columns.
	 * @see "Mathematics for 3D Game Programming and Computer Graphics, 2nd edition; pp.437." 
	 */
	public static float[] eigenVectorsOfSymmetricMatrix(Matrix3f matrix, Matrix3f eigenVectors) {
		return eigenVectorsJacobi(new Matrix3f(matrix), new Matrix3f(eigenVectors));
	}


	private static float[] eigenVectorsJacobi(Matrix3f m, Matrix3f r) {

		// Copy values up to the diagonal
		float m11 = m.getElement(0,0);
		float m12 = m.getElement(0,1);
		float m13 = m.getElement(0,2);
		float m22 = m.getElement(1,1);
		float m23 = m.getElement(1,2);
		float m33 = m.getElement(2,2);
		
		r.setIdentity();
		
		boolean sweepsConsumed = true;
		int i;
		float u, u2, u2p1, t, c, s;
		float tmp, ri0, ri1, ri2;
		
		for(int a=0; a<JACOBI_MAX_SWEEPS; ++a) {
			
			// Exit loop if off-diagonal entries are small enough
			if ((Math.abs(m12) < MathConstants.EPSILON)
				&& (Math.abs(m13) < MathConstants.EPSILON)
				&& (Math.abs(m23) < MathConstants.EPSILON)) {
				sweepsConsumed = false;
				break;
			}
			
			// Annihilate (1,2) entry
			if (m12 != 0.) {
				u = (m22 - m11) *.5f / m12;
				u2 = u*u;
				u2p1 = u2 + 1.f;
				
				if (u2p1!=u2)
					t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
				else
					t = .5f / u;
				
				c = (float) (1.f / Math.sqrt(t*t + 1));
				s = c * t;
				
				m11 -= t * m12;
				m22 += t * m12;
				m12 = 0.f;
				
				tmp = c * m13 - s * m23;
				m23 = s * m13 + c * m23;
				m13 = tmp;
				
				for(i=0; i<3; ++i) {
					ri0 = r.getElement(i,0);
					ri1 = r.getElement(i,1);
					r.setElement(i, 0, c * ri0 - s * ri1);
					r.setElement(i, 1, s * ri0 + c * ri1);
				}
			}
			
			// Annihilate (1,3) entry
			if (m13 != 0.) {
				u = (m33 - m11) *.5f / m13;
				u2 = u*u;
				u2p1 = u2 + 1.f;
				
				if (u2p1!=u2)
					t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
				else
					t = .5f / u;
				
				c = (float) (1.f / Math.sqrt(t*t + 1));
				s = c * t;
				
				m11 -= t * m13;
				m33 += t * m13;
				m13 = 0.f;
				
				tmp = c * m12 - s * m23;
				m23 = s * m12 + c * m23;
				m12 = tmp;
				
				for(i=0; i<3; ++i) {
					ri0 = r.getElement(i,0);
					ri2 = r.getElement(i,2);
					r.setElement(i, 0, c * ri0 - s * ri2);
					r.setElement(i, 2, s * ri0 + c * ri2);
				}
			}

			// Annihilate (2,3) entry
			if (m23 != 0.) {
				u = (m33 - m22) *.5f / m23;
				u2 = u*u;
				u2p1 = u2 + 1.f;
				
				if (u2p1!=u2)
					t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
				else
					t = .5f / u;
				
				c = (float) (1.f / Math.sqrt(t*t + 1));
				s = c * t;
				
				m22 -= t * m23;
				m33 += t * m23;
				m23 = 0.f;
				
				tmp = c * m12 - s * m13;
				m13 = s * m12 + c * m13;
				m12 = tmp;
				
				for(i=0; i<3; ++i) {
					ri1 = r.getElement(i,1);
					ri2 = r.getElement(i,2);
					r.setElement(i, 1, c * ri1 - s * ri2);
					r.setElement(i, 2, s * ri1 + c * ri2);
				}
			}
		}
		
		assert(!sweepsConsumed) : "Sweep count consumed during eigenvector computation"; //$NON-NLS-1$
		
		// eigenvalues are on the diagonal
		return new float[] { m11, m22, m33 };
	}

	/** Add several vectors.
	 * 
	 * @param vectors
	 * @return the result of the addition of the vectors.
	 */
	public static Vector3f add(Vector3f... vectors) {
		Vector3f r = new Vector3f();
		sum(r, vectors);
		return r;
	}
	
	/** {@code r = sum(tuples)}
	 * 
	 * @param r is the result.
	 * @param tuples are the tuples to add.
	 */
	public static void sum(Tuple3f r, Tuple3f... tuples) {
		if (tuples.length==0) {
			r.set(0,0,0);
		}
		else {
			r.set(tuples[0]);
			for(int i=1; i<tuples.length; ++i) {
				r.add(tuples[i]);
			}
		}
	}

}
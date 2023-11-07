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

package org.arakhne.afc.math.geometry.d3.afp;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;


/** Fonctional interface that represented a 3D segment/line.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Segment3afp<
			ST extends Shape3afp<?, ?, IE, P, V, Q, B>,
			IT extends Segment3afp<?, ?, IE, P, V, Q, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>,
			B extends AlignedBox3afp<?, ?, IE, P, V, Q, B>>
			extends Shape3afp<ST, IT, IE, P, V, Q, B> {

	/**
	 * Replies if two lines are colinear.
	 *
	 *<p>The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first
	 * line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 *
	 * <p>If you are interested to test if the two lines are parallel, see
	 * {@link #isParallelLines(double, double, double, double, double, double, double, double, double, double, double, double)}.
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
	 * @return {@code true} if the two given lines are collinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double, double, double, double, double)
	 * @see Point3D#isCollinearPoints(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean isColinearLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		return isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)
				&& Point3D.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3);
	}

	/**
	 * Replies if two lines are parallel.
	 *
	 * <p>The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first
	 * line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 *
	 * <p>If you are interested to test if the two lines are colinear, see
	 * {@link #isColinearLines(double, double, double, double, double, double, double, double, double, double, double, double)}.
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
	 * @return {@code true} if the two given lines are parallel.
	 * @see #isColinearLines(double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean isParallelLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		return Vector3D.isColinearVectors(x2 - x1, y2 - y1, z2 - z1, x4 - x3, y4 - y3, z4 - z3);
	}

	/**
	 * Replies the intersection point between two segments.
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
	 * @param result the intersection point.
	 * @return {@code true} if an intersection exists.
	 * @see #calculatesLineLineIntersection(double, double, double, double, double, double, double, double, double, double, double, double, Point3D)
	 */
	@Pure
	static boolean calculatesSegmentSegmentIntersection(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4,
			Point3D<?, ?, ?> result) {
		final SegmentIntersection factors = calculatesSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		result.set(
				x1 + factors.position1 * (x2 - x1),
				y1 + factors.position1 * (y2 - y1),
				z1 + factors.position1 * (z2 - z1));
		return true;
	}

	/**
	 * Replies position factors for the intersection point between two segments.
	 *
	 * <p>Let segment equations for S1 and S2:<br>
	 * {@code L1: P1 + factor1 * (P2-P1)} with {@code factor1} in {@code [0;1]}<br>
	 * {@code L2: P3 + factor2 * (P4-P3)} with {@code factor2} in {@code [0;1]}<br>
	 * If segments are intersecting, then<br>
	 * {@code I = P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)}
	 *
	 * <p>This function computes the intersection point(s) according to:<ul>
	 * 	<li>If {@code S1} and {@code S2} are not colinear and intersecting:<ul>
	 * 		<li>{@code position1} such that {@code I = P1 + position1 * (P2-P1)} with {@code position1} in {@code [0;1]}</li>
	 * 		<li>{@code position2} such that {@code I = P3 + position2 * (P4-P3)} with {@code position2} in {@code [0;1]}</li>
	 * 		<li>{@code position3} and {@code position4} are not computed, i.e. {@code NaN}</li>
	 * 		</ul></li>
	 * 	<li>If {@code S1} and {@code S2} are colinear and intersecting:<ul>
	 * 		<li>{@code position1} is the position of {@code P1} on {@code S1}, i.e., {@code 0}.</li>
	 * 		<li>{@code position2} is the position of {@code P2} on {@code S1}, i.e., {@code 1}.</li>
	 * 		<li>{@code position3} is the position of {@code P3} on {@code S1}, such that {@code P3 = P1 + position4 * (P2-P1)}.</li>
	 * 		<li>{@code position4} is the position of {@code P4} on {@code L1}, such that {@code P4 = P1 + position4 * (P2-P1)}.</li>
	 * 		</ul></li>
	 * </ul>
	 *
	 * <p>This function computes and replies {@code factor1}.
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
	 * @return the intersection description if there is intersection; or {@code null} if no intersection.
	 * @see #calculatesLineLineIntersectionFactors(double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static SegmentIntersection calculatesSegmentSegmentIntersectionFactors(double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		final LineIntersection factors = calculatesLineLineIntersectionFactors(
				x1, y1, z1, x2, y2, z2,
				x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return null;
		}
		if (factors.colinear) {
			if (1. < factors.position3 || factors.position4 < 0.) {
				return null;
			}
			// Selection heuristic:
			// 			p1 = max(1,3)
			// 			p2 = min(2,4)
			// Cases of colinear segments and position values:
			//			Segment Interval	Intersection	p1	p2
			// 			3..4 1..2			n/a
			// 			3..1|4..2			(1; 4)			1	4
			// 			1|3|4..2			(1; 4)			1	4
			// 			3..1..4..2			(1; 4)			1	4
			// 			3|1..4..2			(1; 4)			1	4
			// 			1..3|4..2			(3; 4)			3	4
			// 			3..1..2|4			(1; 2)			1	2
			// 			3|1..2|4			(1; 2)			1	2
			// 			1..3..2|4			(3; 2)			3	2
			// 			1..2|3|4			(3; 2)			3	2
			// 			3..1..2..4			(1; 2)			1	2
			// 			1|3..2..4			(1; 2)			1	2
			// 			1..3..2..4			(3; 2)			3	2
			// 			1..2|3..4			(3; 2)			3	2
			// 			1..2 3..4			n/a
			return new SegmentIntersection(factors.colinear,
					Math.max(factors.position1, factors.position3),
					Math.min(factors.position2, factors.position4));
		}
		if (factors.position1 < 0. || factors.position1 > 1. || factors.position2 < 0. || factors.position2 > 1.) {
			return null;
		}
		return new SegmentIntersection(factors.colinear, factors.position1, factors.position2);
	}

	/**
	 * Replies the position factors for the intersection point between two lines.
	 *
	 * <p>Let line equations for L1 and L2:<br>
	 * {@code L1: P1 + factor1 * (P2-P1)}<br>
	 * {@code L2: P3 + factor2 * (P4-P3)}<br>
	 * If lines are intersecting, then<br>
	 * {@code I = P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)}
	 *
	 * <p>This function computes the intersection point(s) according to:<ul>
	 * 	<li>If {@code L1} and {@code L2} are not colinear:<ul>
	 * 		<li>{@code position1} such that {@code I = P1 + position1 * (P2-P1)}</li>
	 * 		<li>{@code position2} such that {@code I = P3 + position2 * (P4-P3)}</li>
	 * 		<li>{@code position3} and {@code position4} are not computed, i.e. {@code NaN}</li>
	 * 		</ul></li>
	 * 	<li>If {@code L1} and {@code L2} are colinear:<ul>
	 * 		<li>{@code position1} is the position of {@code P1} on {@code L1}, i.e., {@code 0}.</li>
	 * 		<li>{@code position2} is the position of {@code P2} on {@code L1}, i.e., {@code 1}.</li>
	 * 		<li>{@code position3} is the position of {@code P3} on {@code L1}, such that {@code P3 = P1 + position4 * (P2-P1)}.</li>
	 * 		<li>{@code position4} is the position of {@code P4} on {@code L1}, such that {@code P4 = P1 + position4 * (P2-P1)}.</li>
	 * 		</ul></li>
	 * </ul>
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
	 * @return the intersection description if there is intersection; or {@code null} if no intersection.
	 * @see #calculatesLineLineIntersectionFactors(double, double, double, double, double, double, double, double, double, double, double, double)
	 * @since 18.0
	 */
	@Pure
	static LineIntersection calculatesLineLineIntersectionFactors(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		//We compute the 4 vectors
		final double ax = x2 - x1;
		final double ay = y2 - y1;
		final double az = z2 - z1;

		final double bx = x4 - x3;
		final double by = y4 - y3;
		final double bz = z4 - z3;

		final double cx = x3 - x1;
		final double cy = y3 - y1;
		final double cz = z3 - z1;

		// v = a X b (right-handed cross product)
		// Only the right-handed cross product contributes to the computation of the correct factor.
		final double vx = ay * bz - az * by;
		final double vy = az * bx - ax * bz;
	    final double vz = ax * by - ay * bx;

        // If the cross product is zero then the two segments are parallel
        final double vsqlength = vx * vx + vy * vy + vz * vz;
		if (MathUtil.isEpsilonZero(vsqlength)) {
			// Lines are parallel, are they colinear?
			final double dx = ay * cz - cy * az;
			final double dy = az * cx - cz * ax;
			final double dz = ax * cy - cx * ay;
			final double dl = dx * dx + dy * dy + dz * dz;
			if (MathUtil.isEpsilonZero(dl)) {
				// Compute the best factors on both segments
				final double length = ax * ax + ay * ay + az * az;
				final double proj3on1 = (cx * ax + cy * ay + cz * az) / length;
				final double proj4on1 = ((x4 - x1) * ax + (y4 - y1) * ay + (z4 - z1) * az) / length;
				final double f1;
				final double f2;
				if (proj3on1 <= proj4on1) {
					f1 = proj3on1;
					f2 = proj4on1;
				} else {
					f1 = proj4on1;
					f2 = proj3on1;
				}
				return new LineIntersection(true, 0., 1., f1, f2);
			}
			return null;
		}

		// w = c . v
        final double w = Vector3D.dotProduct(cx, cy, cz, vx, vy, vz);
		// If the determinant det(c,a,b) = c.(a x b) != 0 then the two segments are not colinear
		if (!MathUtil.isEpsilonZero(w)) {
			return null;
		}

		final double factor1 = Vector3D.determinant(
				cx, cy, cz,
				bx, by, bz,
				vx, vy, vz) / vsqlength;

		final double factor2 = Vector3D.determinant(
				cx, cy, cz,
				ax, ay, az,
				vx, vy, vz) / vsqlength;

		return new LineIntersection(false, factor1, factor2, Double.NaN, Double.NaN);
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 *
	 * @param x1 x position of the first point of the line.
	 * @param y1 y position of the first point of the line.
	 * @param z1 z position of the first point of the line.
	 * @param x2 x position of the second point of the line.
	 * @param y2 y position of the second point of the line.
	 * @param z2 z position of the second point of the line.
	 * @param x3 x position of the first point of the line.
	 * @param y3 y position of the first point of the line.
	 * @param z3 z position of the first point of the line.
	 * @param x4 x position of the second point of the line.
	 * @param y4 y position of the second point of the line.
	 * @param z4 z position of the second point of the line.
	 * @param result the intersection point.
	 * @return {@code true} if there is an intersection.
	 * @see #calculatesLineLineIntersectionFactor(double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean calculatesLineLineIntersection(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4,
			Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter(13);
		final LineIntersection factors = calculatesLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		final double x = x1 + factors.position1() * (x2 - x1);
		final double y = y1 + factors.position1() * (y2 - y1);
		final double z = z1 + factors.position1() * (z2 - z1);
		result.set(x, y, z);
		return true;
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 x position of the first point of the line.
	 * @param y1 y position of the first point of the line.
	 * @param z1 z position of the first point of the line.
	 * @param x2 x position of the second point of the line.
	 * @param y2 y position of the second point of the line.
	 * @param z2 z position of the second point of the line.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #calculatesDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static double calculatesDistanceSquaredLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double d = calculatesDistanceLinePoint(x1, y1, z1, x2, y2, z2, px, py, pz);
		return d * d;
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param x1 x position of the first point of the segment.
	 * @param y1 y position of the first point of the segment.
	 * @param z1 z position of the first point of the segment.
	 * @param x2 x position of the second point of the segment.
	 * @param y2 y position of the second point of the segment.
	 * @param z2 y position of the second point of the segment.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	@Pure
	static double calculatesDistanceSquaredSegmentPoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double ratio = calculatesProjectedPointOnLine(px, py, pz, x1, y1, z1, x2, y2, z2);

		if (ratio <= 0.) {
			return Point3D.getDistanceSquaredPointPoint(px, py, pz, x1, y1, z1);
		}

		if (ratio >= 1.) {
			return Point3D.getDistanceSquaredPointPoint(px, py, pz, x2, y2, z2);
		}

		return Point3D.getDistanceSquaredPointPoint(
				px, py, pz,
				(1. - ratio) * x1 + ratio * x2,
				(1. - ratio) * y1 + ratio * y2,
				(1. - ratio) * z1 + ratio * z2);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param x1 x position of the first point of the segment.
	 * @param y1 y position of the first point of the segment.
	 * @param z1 z position of the first point of the segment.
	 * @param x2 x position of the second point of the segment.
	 * @param y2 y position of the second point of the segment.
	 * @param z2 z position of the second point of the segment.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	static double calculatesDistanceSegmentPoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double ratio = calculatesProjectedPointOnLine(px, py, pz, x1, y1, z1, x2, y2, z2);

		if (ratio <= 0.) {
			return Point3D.getDistancePointPoint(px, py, pz, x1, y1, z1);
		}

		if (ratio >= 1.) {
			return Point3D.getDistancePointPoint(px, py, pz, x2, y2, z2);
		}

		return Point3D.getDistancePointPoint(
				px, py, pz,
				(1. - ratio) * x1 + ratio * x2,
				(1. - ratio) * y1 + ratio * y2,
				(1. - ratio) * z1 + ratio * z2);
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 x position of the first point of the line.
	 * @param y1 y position of the first point of the line.
	 * @param z1 z position of the first point of the line.
	 * @param x2 x position of the second point of the line.
	 * @param y2 y position of the second point of the line.
	 * @param z2 z position of the second point of the line.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #calculatesDistanceSquaredLinePoint(double, double, double, double, double, double, double, double, double)
	 * @see #calculatesRelativeDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static double calculatesDistanceLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py,
			double pz) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;
		final double vz1 = z2 - z1;

		final double vx2 = px - x1;
		final double vy2 = py - y1;
		final double vz2 = pz - z1;

		final double x;
		final double y;
		final double z;
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
			x = vy2 * vz1 - vz2 * vy1;
			y = vz2 * vx1 - vx2 * vz1;
			z = vx2 * vy1 - vy2 * vx1;
		} else {
			x = vy1 * vz2 - vz1 * vy2;
			y = vz1 * vx2 - vx1 * vz2;
			z = vx1 * vy2 - vy1 * vx2;
		}

		return Math.sqrt(x * x + y * y + z * z) / Math.sqrt(vx1 * vx1 + vy1 * vy1 + vz1 * vz1);
	}

	/** Compute the square distance between two segments.
	 *
	 * @param x1 x position of the first point of the first segment.
	 * @param y1 y position of the first point of the first segment.
	 * @param z1 z position of the first point of the first segment.
	 * @param x2 x position of the second point of the first segment.
	 * @param y2 y position of the second point of the first segment.
	 * @param z2 y position of the second point of the first segment.
	 * @param x3 x position of the first point of the second segment.
	 * @param y3 y position of the first point of the second segment.
	 * @param z3 z position of the first point of the second segment.
	 * @param x4 x position of the second point of the second segment.
	 * @param y4 y position of the second point of the second segment.
	 * @param z4 y position of the second point of the second segment.
	 * @return the distance beetween the segments.
	 * @since 18.0
	 */
	@Pure
	static double calculatesDistanceSquaredSegmentSegment(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		final InnerComputationVector3afp u = new InnerComputationVector3afp(x2 - x1, y2 - y1, z2 - z1);
		final InnerComputationVector3afp v = new InnerComputationVector3afp(x4 - x3, y4 - y3, z4 - z3);
		final InnerComputationVector3afp w = new InnerComputationVector3afp(x1 - x3, y1 - y3, z1 - z3);

		final double a = u.dot(u);
		final double b = u.dot(v);
		final double c = v.dot(v);
		final double d = u.dot(w);
		final double e = v.dot(w);
		final double D = a * c - b * b;

		double sc, sN, tc, tN;
		double sD = D;
		double tD = D;

		// Compute the line parameters of the two closest points
		if (MathUtil.isEpsilonZero(D)) { 
			// The lines are almost parallel
			// force using point P0 on segment S1
			// to prevent possible division by 0.0 later
			sN = 0.;
			sD = 1.;         
			tN = e;
			tD = c;
		}
		else {
			// Get the closest points on the infinite lines
			sN = b*e - c*d;
			tN = a*e - b*d;
			if (sN < 0.) {
				// sc < 0 => the s=0 edge is visible
				sN = 0.;
				tN = e;
				tD = c;
			} else if (sN > sD) {
				// sc > 1  => the s=1 edge is visible
				sN = sD;
				tN = e + b;
				tD = c;
			}
		}

		if (tN < 0.) {
			// tc < 0 => the t=0 edge is visible
			tN = 0.;
			// Recompute sc for this edge
			if (-d < 0.)
				sN = 0.;
			else if (-d > a)
				sN = sD;
			else {
				sN = -d;
				sD = a;
			}
		}
		else if (tN > tD) {
			// tc > 1  => the t=1 edge is visible
			tN = tD;
			// Recompute sc for this edge
			if ((-d + b) < 0.)
				sN = 0;
			else if ((-d + b) > a)
				sN = sD;
			else {
				sN = (-d +  b);
				sD = a;
			}
		}
		// Finally do the division to get sc and tc
		sc = (MathUtil.isEpsilonZero(sN) ? 0. : sN / sD);
		tc = (MathUtil.isEpsilonZero(tN) ? 0. : tN / tD);

		// get the difference of the two closest points
		// =  S1(sc) - S2(tc)

		// Reuse u, v, w
		u.scale(sc);
		w.add(u);
		v.scale(tc);
		w.sub(v);

		return w.getLengthSquared();
	}

	/** Compute the square distance between two segments.
	 *
	 * @param x1 x position of the first point of the first segment.
	 * @param y1 y position of the first point of the first segment.
	 * @param z1 z position of the first point of the first segment.
	 * @param x2 x position of the second point of the first segment.
	 * @param y2 y position of the second point of the first segment.
	 * @param z2 y position of the second point of the first segment.
	 * @param x3 x position of the first point of the second segment.
	 * @param y3 y position of the first point of the second segment.
	 * @param z3 z position of the first point of the second segment.
	 * @param x4 x position of the second point of the second segment.
	 * @param y4 y position of the second point of the second segment.
	 * @param z4 y position of the second point of the second segment.
	 * @return the distance beetween the segments.
	 * @since 18.0
	 */
	@Pure
	static double calculatesDistanceSegmentSegment(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		return Math.sqrt(calculatesDistanceSquaredSegmentSegment(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4));
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 x location of the segment begining.
	 * @param y1 y location of the segment begining.
	 * @param z1 z location of the segment begining.
	 * @param x2 x location of the second-segment ending.
	 * @param y2 y location of the second-segment ending.
	 * @param z2 z location of the second-segment ending.
	 * @param x x location of the point.
	 * @param y y location of the point.
	 * @param z z location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return {@code true} if the point and the
	 *         line have closed locations.
	 */
	@Pure
	static boolean isPointClosedToSegment(double x1, double y1, double z1, double x2, double y2, double z2, double x, double y,
			double z, double hitDistance) {
		assert hitDistance >= 0. : AssertMessages.positiveOrZeroParameter(9);
		return calculatesDistanceSegmentPoint(x1, y1, z1, x2, y2, z2, x, y, z) < hitDistance;
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 x location of the line begining.
	 * @param y1 y location of the line begining.
	 * @param z1 z location of the line begining.
	 * @param x2 x location of the line ending.
	 * @param y2 y location of the line ending.
	 * @param z2 z location of the line ending.
	 * @param x x location of the point.
	 * @param y y location of the point.
	 * @param z z location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return {@code true} if the point and the
	 *         line have closed locations.
	 */
	@Pure
	static boolean isPointClosedToLine(double x1, double y1, double z1, double x2, double y2, double z2, double x, double y,
			double z, double hitDistance) {
		assert hitDistance >= 0. : AssertMessages.positiveOrZeroParameter(9);
		return calculatesDistanceLinePoint(x1, y1, z1, x2, y2, z2, x, y, z) < hitDistance;
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
	 * @return the projection of the specified point on the line. If
	 *     equal to {@code 0}, the projection is equal to the first segment point.
	 *     If equal to {@code 1}, the projection is equal to the second segment point.
	 *     If inside {@code ]0;1[}, the projection is between the two segment points.
	 *     If inside {@code ]-inf;0[}, the projection is outside on the side of the
	 *     first segment point. If inside {@code ]1;+inf[}, the projection is
	 *     outside on the side of the second segment point.
	 */
	@Pure
	static double calculatesProjectedPointOnLine(double px, double py, double pz, double s1x, double s1y, double s1z, double s2x,
			double s2y, double s2z) {
		final double vx = s2x - s1x;
		final double vy = s2y - s1y;
		final double vz = s2z - s1z;
		final double numerator = (px - s1x) * vx + (py - s1y) * vy + (pz - s1z) * vz;
		final double denomenator = vx * vx + vy * vy + vz * vz;
		return numerator / denomenator;
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
	 * @param z1
	 *            the Z coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param z2
	 *            the Z coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param pz
	 *            the Z coordinate of the specified point to be compared with the specified line segment
	 * @return the positive or negative distance from the point to the line
	 * @see #ccw(double, double, double, double, double, double, double, double, double, double)
	 * @see #calculatesSideLinePoint(double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static double calculatesRelativeDistanceLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double z21 = z2 - z1;
		final double denomenator = x21 * x21 + y21 * y21 + z21 * z21;
		if (denomenator == 0.) {
			return Point3D.getDistancePointPoint(px, py, pz, x1, y1, z1);
		}
		final double factor = ((y1 - py) * x21 - (x1 - px) * y21) / denomenator;
		return factor * Math.sqrt(denomenator);
	}

	/** Compute the interpolate point between the two points.
	 *
	 * @param p1x x coordinate of the first point.
	 * @param p1y y coordinate of the first point.
	 * @param p1z z coordinate of the first point.
	 * @param p2x x coordinate of the second point.
	 * @param p2y y coordinate of the second point.
	 * @param p2z z coordinate of the second point.
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @param result the interpolated point.
	 */
	@Pure
	static void interpolate(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double factor,
			Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		assert factor >= 0. && factor <= 1. : AssertMessages.outsideRangeInclusiveParameter(factor, 0, 1);
		final double vx = p2x - p1x;
		final double vy = p2y - p1y;
		final double vz = p2z - p1z;
		result.set(
				p1x + factor * vx,
				p1y + factor * vy,
				p1z + factor * vz);
	}

	/** Replies the point on the segment that is farthest to the given point.
	 *
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param az is the z coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param bz is the z coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param pz is the z coordinate of the point.
	 * @param result the farthest point on the shape.
	 */
	@Pure
	static void calculatesFarthestPointTo(double ax, double ay, double az, double bx, double by, double bz, double px, double py,
			double pz, Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		final double xpa = px - ax;
		final double ypa = py - ay;
		final double zpa = pz - az;
		final double xpb = px - bx;
		final double ypb = py - by;
		final double zpb = pz - bz;
		if ((xpa * xpa + ypa * ypa + zpa * zpa) >= (xpb * xpb + ypb * ypb + zpb * zpb)) {
			result.set(ax, ay, az);
		} else {
			result.set(bx, by, bz);
		}
	}

	/** Replies the point on the segment that is closest to the given point.
	 *
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param az is the z coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param bz is the z coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param pz is the z coordinate of the point.
	 * @param result the is point on the shape.
	 */
	@Pure
	static void calculatesClosestPointToPoint(
			double ax, double ay, double az, double bx, double by, double bz, double px, double py, double pz,
			Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		final double ratio = Segment3afp.calculatesProjectedPointOnLine(px, py, pz, ax, ay, az, bx, by, bz);
		if (ratio <= 0.) {
			result.set(ax, ay, az);
		} else if (ratio >= 1.) {
			result.set(bx, by, bz);
		} else {
			result.set(
					ax + (bx - ax) * ratio,
					ay + (by - ay) * ratio,
					az + (bz - az) * ratio);
		}
	}

	/** Replies the point on the segment that is closest to the aligned box.
	 *
	 * @param sx1 is the x coordinate of the first point of the segment.
	 * @param sy1 is the y coordinate of the first point of the segment.
	 * @param sz1 is the z coordinate of the first point of the segment.
	 * @param sx2 is the x coordinate of the second point of the segment.
	 * @param sy2 is the y coordinate of the second point of the segment.
	 * @param sz2 is the z coordinate of the second point of the segment.
	 * @param rx is the x coordinate of the rectangle.
	 * @param ry is the y coordinate of the rectangle.
	 * @param rz is the z coordinate of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @param rdepth is the depth of the rectangle.
	 * @param result the is point on the segment.
	 */
	@Pure
	static void calculatesClosestPointToAlignedBox(double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double rx, double ry, double rz, double rwidth, double rheight, double rdepth, Point3D<?, ?, ?> result) {
		assert rwidth >= 0. : AssertMessages.positiveOrZeroParameter(10);
		assert rheight >= 0. : AssertMessages.positiveOrZeroParameter(11);
		assert rdepth >= 0. : AssertMessages.positiveOrZeroParameter(12);
		final double rmaxx = rx + rwidth;
		final double rmaxy = ry + rheight;
		final double rmaxz = rz + rdepth;
		final int code1 = MathUtil.getCohenSutherlandCode3D(sx1, sy1, sz1, rx, ry, rz, rmaxx, rmaxy, rmaxz);
		final int code2 = MathUtil.getCohenSutherlandCode3D(sx2, sy2, sz1, rx, ry, rz, rmaxx, rmaxy, rmaxz);
		final Point3D<?, ?, ?> tmp1 = new InnerComputationPoint3afp();
		final Point3D<?, ?, ?> tmp2 = new InnerComputationPoint3afp();
		final int zone = AlignedBox3afp.reduceCohenSutherlandZoneAlignedBoxSegment(
				rx, ry, rz, rmaxx, rmaxy, rmaxz,
				sx1, sy1, sz1, sx2, sy2, sz2,
				code1, code2,
				tmp1, tmp2);
		if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
			calculatesClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rz, rx, rmaxy, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
			calculatesClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rmaxx, ry, rz, rmaxx, rmaxy, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
			calculatesClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rz, rmaxx, ry, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
			calculatesClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, rmaxy, rz, rmaxx, rmaxy, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_FRONT) != 0) {
			calculatesClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rz, rmaxx, rmaxy, rz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
			calculatesClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rmaxz, rmaxx, rmaxy, rmaxz, result);
		} else {
			calculatesClosestPointToPoint(
					tmp1.getX(), tmp1.getY(), tmp1.getZ(), tmp2.getX(), tmp2.getY(), tmp2.getZ(),
					(rx + rmaxx) / 2., (ry + rmaxy) / 2., (rz + rmaxz) / 2., result);
		}
	}

	/** Replies the point on the first segment that is closest to the second segment.
	 *
	 * @param s1x1 is the x coordinate of the first point of the first segment.
	 * @param s1y1 is the y coordinate of the first point of the first segment.
	 * @param s1z1 is the z coordinate of the first point of the first segment.
	 * @param s1x2 is the x coordinate of the second point of the first segment.
	 * @param s1y2 is the y coordinate of the second point of the first segment.
	 * @param s1z2 is the z coordinate of the second point of the first segment.
	 * @param s2x1 is the x coordinate of the first point of the second segment.
	 * @param s2y1 is the y coordinate of the first point of the second segment.
	 * @param s2z1 is the z coordinate of the first point of the second segment.
	 * @param s2x2 is the x coordinate of the second point of the second segment.
	 * @param s2y2 is the y coordinate of the second point of the second segment.
	 * @param s2z2 is the z coordinate of the second point of the second segment.
	 * @param result the is point on the shape.
	 * @return the square distance between the segments.
	 */
	@Pure
	static double calculatesClosestPointToSegment(
			double s1x1, double s1y1, double s1z1, double s1x2, double s1y2, double s1z2,
			double s2x1, double s2y1, double s2z1, double s2x2, double s2y2, double s2z2,
			Point3D<?, ?, ?> result) {
		return calculatesClosestPointToSegment(
				s1x1, s1y1, s1z1, s1x2, s1y2, s1z2, s2x1, s2y1, s2z1, s2x2, s2y2, s2z2,
				result, null);
	}

	/** Replies the point on the first segment that is closest to the second segment.
	 *
	 * @param s1x1 is the x coordinate of the first point of the first segment.
	 * @param s1y1 is the y coordinate of the first point of the first segment.
	 * @param s1z1 is the z coordinate of the first point of the first segment.
	 * @param s1x2 is the x coordinate of the second point of the first segment.
	 * @param s1y2 is the y coordinate of the second point of the first segment.
	 * @param s1z2 is the z coordinate of the second point of the first segment.
	 * @param s2x1 is the x coordinate of the first point of the second segment.
	 * @param s2y1 is the y coordinate of the first point of the second segment.
	 * @param s2z1 is the z coordinate of the first point of the second segment.
	 * @param s2x2 is the x coordinate of the second point of the second segment.
	 * @param s2y2 is the y coordinate of the second point of the second segment.
	 * @param s2z2 is the z coordinate of the second point of the second segment.
	 * @param resultOnFirstSegment the point on the first segment.
	 * @param resultOnSecondSegment the point on the second segment.
	 * @return the square distance between the segments.
	 */
	@Pure
	static double calculatesClosestPointToSegment(
			double s1x1, double s1y1, double s1z1, double s1x2, double s1y2, double s1z2,
			double s2x1, double s2y1, double s2z1, double s2x2, double s2y2, double s2z2,
			Point3D<?, ?, ?> resultOnFirstSegment, Point3D<?, ?, ?> resultOnSecondSegment) {
		final double ux = s1x2 - s1x1;
		final double uy = s1y2 - s1y1;
		final double uz = s1z2 - s1z1;
		final double vx = s2x2 - s2x1;
		final double vy = s2y2 - s2y1;
		final double vz = s2z2 - s2z1;
		final double wx = s1x1 - s2x1;
		final double wy = s1y1 - s2y1;
		final double wz = s1z1 - s2z1;
		final double a = Vector2D.dotProduct(ux, uy, ux, uy);
		final double b = Vector2D.dotProduct(ux, uy, vx, vy);
		final double c = Vector2D.dotProduct(vx, vy, vx, vy);
		final double d = Vector2D.dotProduct(ux, uy, wx, wy);
		final double e = Vector2D.dotProduct(vx, vy, wx, wy);
		final double bigD = a * c - b * b;
		double svD = bigD;
		double tvD = bigD;
		double svN;
		double tvN;
		// compute the line parameters of the two closest points
		if (MathUtil.isEpsilonZero(bigD)) {
			// the lines are almost parallel
			// force using point P0 on segment S1
			svN = 0.;
			// to prevent possible division by 0.0 later
			svD = 1.;
			tvN = e;
			tvD = c;
		} else {
			// get the closest points on the infinite lines
			svN = b * e - c * d;
			tvN = a * e - b * d;
			if (svN < 0.) {
				// sc < 0 => the s=0 edge is visible
				svN = 0.;
				tvN = e;
				tvD = c;
			} else if (svN > svD) {
				// sc > 1  => the s=1 edge is visible
				svN = svD;
				tvN = e + b;
				tvD = c;
			}
		}

		if (tvN < 0.) {
			// tc < 0 => the t=0 edge is visible
			tvN = 0.;
			// recompute sc for this edge
			if (-d < 0.) {
				svN = 0.0;
			} else if (-d > a) {
				svN = svD;
			} else {
				svN = -d;
				svD = a;
			}
		} else if (tvN > tvD) {
			// tc > 1  => the t=1 edge is visible
			tvN = tvD;
			// recompute sc for this edge
			if ((-d + b) < 0.) {
				svN = 0;
			} else if ((-d + b) > a) {
				svN = svD;
			} else {
				svN = -d +  b;
				svD = a;
			}
		}

		// finally do the division to get sc and tc
		final double sc = MathUtil.isEpsilonZero(svN) ? 0. : (svN / svD);
		final double tc = MathUtil.isEpsilonZero(tvN) ? 0. : (tvN / tvD);

		// get the difference of the two closest points
		// =  S1(sc) - S2(tc)
		final double dPx = wx + (sc * ux) - (tc * vx);
		final double dPy = wy + (sc * uy) - (tc * vy);
		final double dPz = wz + (sc * uz) - (tc * vz);

		if (resultOnFirstSegment != null) {
			resultOnFirstSegment.set(s1x1 + sc * ux, s1y1 + sc * uy, s1z1 + sc * uz);
		}

		if (resultOnSecondSegment != null) {
			resultOnSecondSegment.set(s2x1 + tc * vx, s2y1 + tc * vy, s2z1 + tc * vz);
		}

		return dPx * dPx + dPy * dPy + dPz * dPz;
	}

	/**
	 * Calculates the number of times the line from (x0,y0,z0) to (x1,y1,z1)
	 * crosses the ray extending to the right from (px,py,pz).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 *
	 * <p>This function differs from
	 * {@link #calculatesCrossingsFromPointWithoutEquality(double, double, double, double, double, double, double, double, double)}.
	 * The equality test is used in this function.
	 *
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param z1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	@Pure
	static int calculatesCrossingsFromPoint(
			double px, double py, double pz,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py >= y0 && py >= y1) {
			return 0;
		}
		// assert(y0 != y1);
		if (px >= x0 && px >= x1) {
			return 0;
		}
		if (px <  x0 && px <  x1) {
			return (y0 < y1) ? 1 : -1;
		}
		final double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px >= xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 *
	 * <p>This function differs from
	 * {@link #calculatesCrossingsFromPoint(double, double, double, double, double, double, double, double, double)}.
	 * The equality test is not used in this function.
	 *
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the second point of the line.
	 * @param z1 is the second point of the line.
	 * @return the crossing.
	 */
	@Pure
	static int calculatesCrossingsFromPointWithoutEquality(
			double px, double py, double pz,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py > y0 && py > y1) {
			return 0;
		}
		// assert(y0 != y1);
		if (px > x0 && px > x1) {
			return 0;
		}
		if (px < x0 && px < x1) {
			return (y0 < y1) ? 1 : -1;
		}
		final double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px > xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/** Replies if two lines are intersecting.
	 *
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param z1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param z2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 * {@code false}
	 */
	@Pure
	static boolean intersectsLineLine(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		if (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)) {
			return Point3D.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		}
		return true;
	}

	/** Replies if a segment and a line are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentLineWithoutEnds(double, double, double, double, double,
	 *     double, double, double, double, double, double, double)}.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 * {@code false}
	 * @since 18.0
	 */
	@Pure
	static boolean intersectsSegmentLineWithEnds(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		final LineIntersection factors = calculatesLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		return factors.colinear || (factors.position1 >= 0. && factors.position1 <= 1.);
	}

	/** Replies if a segment and a line are intersecting.
	 * This function considers that the ends of
	 * the segments are not intersecting.
	 * To include the ends of the segments, see
	 * {@link #intersectsSegmentLineWithEnds(double, double, double, double, double,
	 *     double, double, double, double, double, double, double)}.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 * {@code false}
	 * @since 18.0
	 */
	@Pure
	static boolean intersectsSegmentLineWithoutEnds(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		final LineIntersection factors = calculatesLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		return factors.colinear || (factors.position1 > 0. && factors.position1 < 1.);
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are not intersecting.
	 * To include the ends of the segments in the intersection ranges, see
	 * {@link #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double,
	 *     double, double, double, double, double, double)}.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param z3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param z4 is the second point of the second segment.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 * {@code false}
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double,
	 *     double, double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithoutEnds(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final SegmentIntersection factors = calculatesSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		if (factors.colinear) {
			return factors.position1 < 1. && factors.position2 > 0.; 
		}
		return factors.position1 > 0. && factors.position1 < 1. && factors.position2 > 0. && factors.position2 < 1.;
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double,
	 *     double, double, double, double, double, double, double)}.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param z3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param z4 is the second point of the second segment.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 * {@code false}
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double,
	 *     double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithEnds(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final SegmentIntersection factors = calculatesSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		return factors.colinear || factors.position1 >= 0. && factors.position1 <= 1. && factors.position2 >= 0. && factors.position2 <= 1.;
	}

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}
		return getX1() == shape.getX1()
				&& getY1() == shape.getY1()
				&& getZ1() == shape.getZ1()
				&& getX2() == shape.getX2()
				&& getY2() == shape.getY2()
				&& getZ2() == shape.getZ2();
	}

	@Override
	default boolean isEmpty() {
		return getX1() == getX2() && getY1() == getY2() && getZ1() == getZ2();
	}

	/** Change the line.
	 *
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param z1 z coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 * @param z2 z coordinate of the second point.
	 */
	// No default implementation to ensure atomic change.
	void set(double x1, double y1, double z1, double x2, double y2, double z2);

	/** Change the line.
	 *
	 * @param firstPoint the first point.
	 * @param secondPoint the second point.
	 */
	default void set(Point3D<?, ?, ?> firstPoint, Point3D<?, ?, ?> secondPoint) {
		assert firstPoint != null : AssertMessages.notNullParameter(0);
		assert secondPoint != null : AssertMessages.notNullParameter(1);
		set(firstPoint.getX(), firstPoint.getY(), firstPoint.getZ(), secondPoint.getX(), secondPoint.getY(), secondPoint.getZ());
	}

	@Override
	default void set(IT shape) {
		assert shape != null : AssertMessages.notNullParameter();
		set(shape.getX1(), shape.getY1(), shape.getZ1(), shape.getX2(), shape.getY2(), shape.getZ2());
	}

	/** Sets a new value in the X of the first point.
	 *
	 * @param x the new value double x
	 */
	void setX1(double x);

	/**Sets a new value in the Y of the first point.
	 *
	 * @param y the new value double y
	 */
	void setY1(double y);

	/**Sets a new value in the Z of the first point.
	 *
	 * @param z the new value double z
	 */
	void setZ1(double z);

	/**Sets a new value in the X of the second point.
	 *
	 * @param x the new value double x
	 */
	void setX2(double x);

	/**Sets a new value in the Y of the second point.
	 *
	 * @param y the new value double y
	 */
	void setY2(double y);

	/**Sets a new value in the Z of the second point.
	 *
	 * @param z the new value double z
	 */
	void setZ2(double z);

	/** Replies the X of the first point.
	 *
	 * @return the x of the first point.
	 */
	@Pure
	double getX1();

	/** Replies the Y of the first point.
	 *
	 * @return the y of the first point.
	 */
	@Pure
	double getY1();

	/** Replies the Z of the first point.
	 *
	 * @return the z of the first point.
	 */
	@Pure
	double getZ1();

	/** Replies the X of the second point.
	 *
	 * @return the x of the second point.
	 */
	@Pure
	double getX2();

	/** Replies the Y of the second point.
	 *
	 * @return the y of the second point.
	 */
	@Pure
	double getY2();

	/** Replies the Z of the second point.
	 *
	 * @return the z of the second point.
	 */
	@Pure
	double getZ2();

	/** Replies the first point.
	 *
	 * @return the first point.
	 */
	@Pure
	default P getP1() {
		return getGeomFactory().newPoint(getX1(), getY1(), getZ1());
	}

	/** Replies the second point.
	 *
	 * @return the second point.
	 */
	@Pure
	default P getP2() {
		return getGeomFactory().newPoint(getX2(), getY2(), getZ2());
	}

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 * @param z z coordinate of the first point.
	 */
	default void setP1(double x, double y, double z) {
		set(x, y, z, getX2(), getY2(), getZ2());
	}

	/** Change the first point.
	 *
	 * @param point the first point.
	 */
	default void setP1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		set(point.getX(), point.getY(), point.getZ(), getX2(), getY2(), getZ2());
	}

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 * @param z z coordinate of the first point.
	 */
	default void setP2(double x, double y, double z) {
		set(getX1(), getY1(), getZ1(), x, y, z);
	}

	/** Change the second point.
	 *
	 * @param point the second point.
	 */
	default void setP2(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		set(getX1(), getY1(), getZ1(), point.getX(), point.getY(), point.getZ());
	}

	/** Replies the length of the segment.
	 *
	 * @return the length.
	 */
	@Pure
	default double getLength() {
		return Point3D.getDistancePointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Replies the squared length of the segment.
	 *
	 * @return the squared length.
	 */
	@Pure
	default double getLengthSquared() {
		return Point3D.getDistanceSquaredPointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		box.setFromCorners(
				this.getX1(),
				this.getY1(),
				this.getZ1(),
				this.getX2(),
				this.getY2(),
				this.getZ2());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return calculatesDistanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				point.getX(), point.getY(), point.getZ());
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		double ratio = calculatesProjectedPointOnLine(point.getX(), point.getY(), point.getZ(), getX1(), getY1(), getZ1(), getX2(),
				getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final double vx = (getX2() - getX1()) * ratio;
		final double vy = (getY2() - getY1()) * ratio;
		final double vz = (getZ2() - getZ1()) * ratio;
		return Math.abs(getX1() + vx - point.getX())
				+ Math.abs(getY1() + vy - point.getY())
				+ Math.abs(getZ1() + vz - point.getZ());
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		double ratio = calculatesProjectedPointOnLine(point.getX(), point.getY(), point.getZ(), getX1(), getY1(), getZ1(), getX2(),
				getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final double vx = (getX2() - getX1()) * ratio;
		final double vy = (getY2() - getY1()) * ratio;
		final double vz = (getZ2() - getZ1()) * ratio;
		return MathUtil.max(
				Math.abs(this.getX1() + vx - point.getX()),
				Math.abs(this.getY1() + vy - point.getY()),
				Math.abs(this.getZ1() + vz - point.getZ()));
	}

	/** {@inheritDoc}
	 *
	 * <p>This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 *
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Override
	default boolean contains(double x, double y, double z) {
		return MathUtil.isEpsilonZero(
				calculatesDistanceSquaredSegmentPoint(
						getX1(), getY1(), getZ1(),
						getX2(), getY2(), getZ2(),
						x, y, z));
	}

	@Override
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> AlignedBox) {
		assert AlignedBox != null : AssertMessages.notNullParameter();
		return (getX1() == getX2() || getY1() == getY2() || getZ1() == getZ2())
				&& contains(AlignedBox.getMinX(), AlignedBox.getMinY(), AlignedBox.getMinZ())
				&& contains(AlignedBox.getMaxX(), AlignedBox.getMaxY(), AlignedBox.getMaxZ());
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		set(getX1() + dx, getY1() + dy, getZ1() + dz, getX2() + dx, getY2() + dy, getZ2() + dz);
	}

	/** Rotates this segment according to the given quaternion around the given rotation center (pivot).
	 *
	 * @param qx x component of the quaternion.
	 * @param qy y component of the quaternion.
	 * @param qz z component of the quaternion.
	 * @param qw w component of the quaternion.
	 * @param px x components of the pivot point.
	 * @param py y components of the pivot point.
	 * @param pz z components of the pivot point.
	 * @since 18.0
	 */
	default void rotate(double qx, double qy, double qz, double qw, double px, double py, double pz) {
		final Transform3D m = new Transform3D();
		m.setRotation(qx, qy, qz, qw);

		final Point3D<?, ?, ?> point1 = new InnerComputationPoint3afp(
				getX1() - px, getY1() - py, getZ1() - pz);
		m.transform(point1);
		point1.add(px, py, pz);

		final Point3D<?, ?, ?> point2 = new InnerComputationPoint3afp(
				getX2() - px, getY2() - py, getZ2() - pz);
		m.transform(point2);
		point2.add(px, py, pz);

		set(point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
	}

	/** Rotates this segment according to the given quaternion around the given rotation center (pivot).
	 *
	 * @param quaternion the rotation quaternion.
	 * @param pivot the pivot point. If the pivot is {@code null}, the pivot point {@code (0,0,0)} is assumed.
	 * @since 18.0
	 */
	default void rotate(Quaternion<?, ?, ?> quaternion, Point3D<?, ?, ?> pivot) {
		assert quaternion != null : AssertMessages.notNullParameter(0);
		if (pivot == null) {
			rotate(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.getW());
		} else {
			rotate(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.getW(),
					pivot.getX(), pivot.getY(), pivot.getZ());
		}
	}

	/** Rotates this segment according to the given quaternion around the rotation center {@code (0, 0, 0)}.
	 *
	 * @param qx x component of the quaternion.
	 * @param qy y component of the quaternion.
	 * @param qz z component of the quaternion.
	 * @param qw w component of the quaternion.
	 * @since 18.0
	 */
	default void rotate(double qx, double qy, double qz, double qw) {
		final Transform3D m = new Transform3D();
		m.setRotation(qx, qy, qz, qw);

		final Point3D<?, ?, ?> point1 = new InnerComputationPoint3afp(getX1(), getY1(), getZ1());
		m.transform(point1);

		final Point3D<?, ?, ?> point2 = new InnerComputationPoint3afp(getX2(), getY2(), getZ2());
		m.transform(point2);

		set(point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
	}

	/** Rotates this segment according to the given quaternion around the rotation center {@code (0, 0, 0)}.
	 *
	 * @param quaternion the rotation quaternion.
	 * @since 18.0
	 */
	default void rotate(Quaternion<?, ?, ?> quaternion) {
		assert quaternion != null : AssertMessages.notNullParameter();
		rotate(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.getW());
	}

	/** Transform the current segment.
	 * This function changes the current segment.
	 *
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	default void transform(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final Point3D<?, ?, ?> p = new InnerComputationPoint3afp(getX1(),  getY1(), getZ1());
		transform.transform(p);
		final double x1 = p.getX();
		final double y1 = p.getY();
		final double z1 = p.getZ();
		p.set(getX2(), getY2(), getZ2());
		transform.transform(p);
		set(x1, y1, z1, p.getX(), p.getY(), p.getZ());
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0, 0, 0);
	}

	/** Clip the segment against the clipping box
	 * according to the adaptation of the
	 * <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>
	 * to 3D.
	 *
	 * @param box the box that corresponds to the clipping area.
	 * @return {@code true} if the segment has an intersection with the
	 *     box and the segment was clipped; {@code false} if the segment
	 *     does not intersect the box.
	 */
	@Pure
	default boolean clipToBox(Box3afp<?, ?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		return clipToBox(
				box.getMinX(), box.getMinY(), box.getMinZ(),
				box.getMaxX(), box.getMaxY(), box.getMaxZ());
	}

	/** Calculate the intersection point between a line and a plane.
	 *
	 * @param lx1 x coordinate of the first point of the line.
	 * @param ly1 y coordinate of the first point of the line.
	 * @param lz1 z coordinate of the first point of the line.
	 * @param lx2 x coordinate of the second point of the line.
	 * @param ly2 y coordinate of the second point of the line.
	 * @param lz2 z coordinate of the second point of the line.
	 * @param a a component of the plane equation.
	 * @param b b component of the plane equation.
	 * @param c c component of the plane equation.
	 * @param d d component of the plane equation.
	 * @param result the intersection point.
	 * @return {@code true} if there is an intersection point; {@code false} if there is no intersection.
	 * @since 18.0
	 */
	@Pure
	public static boolean calculatesLinePlaneIntersection(
			double lx1, double ly1, double lz1, double lx2, double ly2, double lz2,
			double a, double b, double c, double d,
			Point3D<?, ?, ?> result) {
		final double nx = a;
		final double ny = b;
		final double nz = c;

		double ux = lx2 - lx1;
		double uy = ly2 - ly1;
		double uz = lz2 - lz1;
		final double ul = Math.sqrt(ux * ux + uy * uy + uz * uz);
		ux /= ul;
		uy /= ul;
		uz /= ul;
		
		final double s = Vector3D.dotProduct(nx, ny, nz, ux, uy, uz);
		if (MathUtil.isEpsilonZero(s)) {
			// line and plane are parallel
			return false;
		}

		// Assuming L: P0 + si * u
		//
		//      -(a x0 + b y0 + c z0 + d)
		// si = -------------------------
		//               n.u
		double si = -(a * lx1 + b * ly1 + c * lz1 + d) / s;

		ux *= si;
		uy *= si;
		uz *= si;

		if (result != null) {
			result.set(lx1 + ux, ly1 + uy, lz1 + uz);
		}

		return true;
	}

	/** Clip the segment against the clipping box
	 * according to the adaptation of the
	 * <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>
	 * to 3D.
	 *
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rzmin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @param rzmax is the max of the coordinates of the rectangle.
	 * @return {@code true} if the segment has an intersection with the
	 *     box and the segment was clipped; {@code false} if the segment
	 *     does not intersect the box.
	 */
	@Pure
	default boolean clipToBox(double rxmin, double rymin, double rzmin, double rxmax, double rymax, double rzmax) {
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(0, rxmin, 3, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(1, rymin, 4, rymax);
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(2, rzmin, 5, rzmax);
		double x0 = getX1();
		double y0 = getY1();
		double z0 = getZ1();
		double x1 = getX2();
		double y1 = getY2();
		double z1 = getZ2();
		int code1 = MathUtil.getCohenSutherlandCode3D(x0, y0, z0, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		int code2 = MathUtil.getCohenSutherlandCode3D(x1, y1, z1, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		boolean accept = false;
		boolean cont = true;
		final Point3D<?, ?, ?> point = new InnerComputationPoint3afp();

		while (cont) {
			if ((code1 | code2) == 0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			} else if ((code1 & code2) != 0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			} else {
				// Failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip plan

				// At least one endpoint is outside the clip box; pick it.
				int code3 = code1 != 0 ? code1 : code2;

				// Now find the intersection point;
				if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
					// y > rymax
					calculatesLinePlaneIntersection(
							x0, y0, z0, x1, y1, z1,
							0, 1, 0, -rymax,
							point);
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
					// y < rymin
					calculatesLinePlaneIntersection(
							x0, y0, z0, x1, y1, z1,
							0, 1, 0, -rymin,
							point);
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
					// x > rxmax
					calculatesLinePlaneIntersection(
							x0, y0, z0, x1, y1, z1,
							1, 0, 0, -rxmax,
							point);
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
					// x < rxmin
					calculatesLinePlaneIntersection(
							x0, y0, z0, x1, y1, z1,
							1, 0, 0, -rxmin,
							point);
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
					// z > rzmax
					calculatesLinePlaneIntersection(
							x0, y0, z0, x1, y1, z1,
							0, 0, 1, -rzmax,
							point);
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_FRONT) != 0) {
					// z < rzmin
					calculatesLinePlaneIntersection(
							x0, y0, z0, x1, y1, z1,
							0, 0, 1, -rzmin,
							point);
				} else {
					code3 = 0;
				}

				if (code3 != 0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = point.getX();
						y0 = point.getY();
						z0 = point.getZ();
						code1 = MathUtil.getCohenSutherlandCode3D(x0, y0, z0, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
					} else {
						x1 = point.getX();
						y1 = point.getY();
						z1 = point.getZ();
						code2 = MathUtil.getCohenSutherlandCode3D(x1, y1, z1, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
					}
				}
			}
		}
		if (accept) {
			set(x0, y0, z0, x1, y1, z1);
		}
		return accept;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return Sphere3afp.intersectsSphereSegment(
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		return AlignedBox3afp.intersectsAlignedBoxSegment(
				prism.getMinX(), prism.getMinY(), prism.getMinZ(),
				prism.getMaxX(), prism.getMaxY(), prism.getMaxZ(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return intersectsSegmentSegmentWithEnds(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
		return new SegmentPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Segment3afp.calculatesClosestPointToPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return getClosestPointTo(sphere.getCenter());
	}

	@Override
	@Unefficient
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Path3afp.getClosestPointTo(getPathIterator(), path.getPathIterator(), point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		calculatesClosestPointToAlignedBox(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(), rectangle.getMinX(),
				rectangle.getMinY(), rectangle.getMinZ(), rectangle.getWidth(), rectangle.getHeight(), rectangle.getDepth(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		calculatesClosestPointToSegment(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(),
				segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2(), point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Segment3afp.calculatesFarthestPointTo(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	/** Replies the unit vector for the direction of this segment.
	 * This function replies a vector that is colinear to the one
	 * replied by {@link #getSegmentVector()}, but it is always
	 * a unit vector. The vector replied by {@link #getSegmentVector()}
	 * has a length equal to the distanc ebetween the two segment points.
	 *
	 * @return the direction's unit vector.
	 * @see #getSegmentVector()
	 */
	@Pure
	default V getDirection() {
		final double vx = getX2() - getX1();
		final double vy = getY2() - getY1();
		final double vz = getZ2() - getZ1();
		final double l = Math.sqrt(vx * vx + vy * vy + vz * vz);
		return getGeomFactory().newVector(vx / l, vy / l, vz / l);
	}

	/** Replies the vector between the two points of the segment.
	 * This function replies a vector that is colinear to the one
	 * replied by {@link #getDirection()}, but with a length equals
	 * to the distance between the two segment points.
	 *
	 * @return the segment vector.
	 * @see #getDirection()
	 */
	@Pure
	default V getSegmentVector() {
		final double vx = getX2() - getX1();
		final double vy = getY2() - getY1();
		final double vz = getZ2() - getZ1();
		return getGeomFactory().newVector(vx, vy, vz);
	}

	/** Replies this segment with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the segment.
	 * @since 18.0
	 */
	default String toGeogebra() {
		return GeogebraUtil.toSegmentDefinition(3, getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Description of an intersection between two lines.
	 *
	 * @param colinear indicates if the two elements are colinear, i.e., the intersection concerns multiple points of the two geometric elements.
	 * @param position1 the position factor of the intersection point on the first geometric element. The value is in [0;1] if it is located on the first segment.
	 * @param position2 if elements are not colinear, it is the position factor of the intersection point on the second geometric element. The value is in [0;1] if it is located on the second segment.
	 *     If the elements are colinear, it is the position factor of the second point of the first geometric element. The value is in [0;1] if it is located on the first segment.
	 * @param position3 if the elements are colinear, it is the position factor of the first point of the second geometric element. The value is in [0;1] if it is located on the first segment.
	 * @param position4 if the elements are colinear, it is the position factor of the second point of the second geometric element. The value is in [0;1] if it is located on the first segment.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	record LineIntersection(boolean colinear, double position1, double position2, double position3, double position4) {
		//
	}

	/** Description of an intersection between two segments.
	 *
	 * @param colinear indicates if the two elements are colinear, i.e., the intersection concerns multiple points of the two geometric elements.
	 * @param position1 the position factor of the intersection point on the first geometric element. The value is in [0;1] if it is located on the first segment.
	 * @param position2 if elements are not colinear, it is the position factor of the intersection point on the second geometric element. The value is in [0;1] if it is located on the second segment.
	 *     If the elements are colinear, it is the position factor of the second point of intersection on the first geometric element. The value is in [0;1] if it is located on the first segment.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	record SegmentIntersection(boolean colinear, double position1, double position2) {
		//
	}

	/** Iterator on the path elements of the segment.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class SegmentPathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		private final Segment3afp<?, ?, T, ?, ?, ?, ?> segment;

		private final Point3D<?, ?, ?> p1;

		private final Point3D<?, ?, ?> p2;

		private final Transform3D transform;

		private final double x1;

		private final double y1;

		private final double z1;

		private final double x2;

		private final double y2;

		private final double z2;

		private int index;

		/** Constructor.
		 * @param segment the iterated segment.
		 * @param transform the transformation, or {@code null}.
		 */
		public SegmentPathIterator(Segment3afp<?, ?, T, ?, ?, ?, ?> segment, Transform3D transform) {
			assert segment != null : AssertMessages.notNullParameter();
			this.segment = segment;
			this.p1 = new InnerComputationPoint3afp();
			this.p2 = new InnerComputationPoint3afp();
			this.transform = (transform == null || transform.isIdentity()) ? null : transform;
			this.x1 = segment.getX1();
			this.y1 = segment.getY1();
			this.z1 = segment.getZ1();
			this.x2 = segment.getX2();
			this.y2 = segment.getY2();
			this.z2 = segment.getZ2();
			if (this.x1 == this.x2 && this.y1 == this.y2 && this.z1 == this.z2) {
				this.index = 2;
			}
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new SegmentPathIterator<>(this.segment, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 1;
		}

		@Override
		public T next() {
			if (this.index > 1) {
				throw new NoSuchElementException();
			}
			final int idx = this.index;
			++this.index;
			switch (idx) {
			case 0:
				this.p2.set(this.x1, this.y1, this.z1);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z2);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			default:
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return true;
		}

		@Pure
		@Override
		public boolean isCurved() {
			return false;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return false;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public GeomFactory3afp<T, ?, ?, ?, ?> getGeomFactory() {
			return this.segment.getGeomFactory();
		}

	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** Functional interface that represented a 3D segment/line.
 *
 * @param <ST> is the general type of all the shapes.
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
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:magicnumber"})
public interface Segment3afp<
			ST extends Shape3afp<?, IE, P, V, Q, B>,
			IT extends Segment3afp<?, ?, IE, P, V, Q, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>,
			B extends AlignedBox3afp<?, IE, P, V, Q, B>>
		extends TransformableShape3afp<ST, IT, IE, P, V, Q, B> {

	@Override
	default Shape3DType getType() {
		return Shape3DType.SEGMENT;
	}

	/**
	 * Replies if two lines are colinear.
	 *
	 *<p>The given two lines are described respectively by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first
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
	 * @return {@code true} if the two given lines are colinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double, double, double, double, double)
	 * @see Point3D#isCollinearPoints(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
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
	@SuppressWarnings("checkstyle:parameternumber")
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
	 * @see #calculatesLineLineIntersection(double, double, double, double, double,
	 * double, double, double, double, double, double, double, Point3D)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean calculatesSegmentSegmentIntersection(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4,
			Point3D<?, ?, ?> result) {
		final var factors = calculatesSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
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
	@SuppressWarnings("checkstyle:parameternumber")
	static SegmentIntersection calculatesSegmentSegmentIntersectionFactors(double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		final var factors = calculatesLineLineIntersectionFactors(
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
	 * @since 18.0
	 * @see #calculatesLineLineIntersectionFactors(double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static LineIntersection calculatesLineLineIntersectionFactors(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		//We compute the 4 vectors
		final var ax = x2 - x1;
		final var ay = y2 - y1;
		final var az = z2 - z1;

		final var bx = x4 - x3;
		final var by = y4 - y3;
		final var bz = z4 - z3;

		final var cx = x3 - x1;
		final var cy = y3 - y1;
		final var cz = z3 - z1;

		// v = a X b (right-handed cross product)
		// Only the right-handed cross product contributes to the computation of the correct factor.
		final var vx = ay * bz - az * by;
		final var vy = az * bx - ax * bz;
		final var vz = ax * by - ay * bx;

		// If the cross product is zero then the two segments are parallel
		final var vsqlength = vx * vx + vy * vy + vz * vz;
		if (MathUtil.isEpsilonZero(vsqlength)) {
			// Lines are parallel, are they colinear?
			final var dx = ay * cz - cy * az;
			final var dy = az * cx - cz * ax;
			final var dz = ax * cy - cx * ay;
			final var dl = dx * dx + dy * dy + dz * dz;
			if (MathUtil.isEpsilonZero(dl)) {
				// Compute the best factors on both segments
				final var length = ax * ax + ay * ay + az * az;
				final var proj3on1 = (cx * ax + cy * ay + cz * az) / length;
				final var proj4on1 = ((x4 - x1) * ax + (y4 - y1) * ay + (z4 - z1) * az) / length;
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
		final var w = Vector3D.dotProduct(cx, cy, cz, vx, vy, vz);
		// If the determinant det(c,a,b) = c.(a x b) != 0 then the two segments are not colinear
		if (!MathUtil.isEpsilonZero(w)) {
			return null;
		}

		final var factor1 = Vector3D.determinant(
				cx, cy, cz,
				bx, by, bz,
				vx, vy, vz) / vsqlength;

		final var factor2 = Vector3D.determinant(
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
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean calculatesLineLineIntersection(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4,
			Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter(13);
		final var factors = calculatesLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		final var x = x1 + factors.position1() * (x2 - x1);
		final var y = y1 + factors.position1() * (y2 - y1);
		final var z = z1 + factors.position1() * (z2 - z1);
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
	 * @return the distance between the point and the line.
	 * @see #calculatesDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static double calculatesDistanceSquaredLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final var d = calculatesDistanceLinePoint(x1, y1, z1, x2, y2, z2, px, py, pz);
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
	 * @return the distance between the point and the segment.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static double calculatesDistanceSquaredSegmentPoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final var ratio = calculatesProjectedPointOnLine(px, py, pz, x1, y1, z1, x2, y2, z2);

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
	 * @return the distance between the point and the segment.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static double calculatesDistanceSegmentPoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final var ratio = calculatesProjectedPointOnLine(px, py, pz, x1, y1, z1, x2, y2, z2);

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
	 * @return the distance between the point and the line.
	 * @see #calculatesDistanceSquaredLinePoint(double, double, double, double, double, double, double, double, double)
	 * @see #calculatesRelativeDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static double calculatesDistanceLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py,
			double pz) {
		final var vx1 = x2 - x1;
		final var vy1 = y2 - y1;
		final var vz1 = z2 - z1;

		final var vx2 = px - x1;
		final var vy2 = py - y1;
		final var vz2 = pz - z1;

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
		final var num = Math.sqrt(x * x + y * y + z * z);
		final var denum = Math.sqrt(vx1 * vx1 + vy1 * vy1 + vz1 * vz1);
		final var distance = num / denum;
		return distance;
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
	 * @return the distance between the segments.
	 * @since 18.0
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localfinalvariablename", "checkstyle:localvariablename"})
	static double calculatesDistanceSquaredSegmentSegment(double x1, double y1, double z1, double x2,
			double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		final var u = new InnerComputationVector3D(x2 - x1, y2 - y1, z2 - z1);
		final var v = new InnerComputationVector3D(x4 - x3, y4 - y3, z4 - z3);
		final var w = new InnerComputationVector3D(x1 - x3, y1 - y3, z1 - z3);

		final var a = u.dot(u);
		final var b = u.dot(v);
		final var c = v.dot(v);
		final var d = u.dot(w);
		final var e = v.dot(w);
		final var D = a * c - b * b;

		// Compute the line parameters of the two closest points
		double sN;
		var sD = D;
		double tN;
		double tD = D;
		if (MathUtil.isEpsilonZero(D)) {
			// The lines are almost parallel
			// force using point P0 on segment S1
			// to prevent possible division by 0.0 later
			sN = 0.;
			sD = 1.;
			tN = e;
			tD = c;
		} else {
			// Get the closest points on the infinite lines
			sN = b * e - c * d;
			tN = a * e - b * d;
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
			if (-d < 0.) {
				sN = 0.;
			} else if (-d > a) {
				sN = sD;
			} else {
				sN = -d;
				sD = a;
			}
		} else if (tN > tD) {
			// tc > 1  => the t=1 edge is visible
			tN = tD;
			// Recompute sc for this edge
			if ((-d + b) < 0.) {
				sN = 0;
			} else if ((-d + b) > a) {
				sN = sD;
			} else {
				sN = -d +  b;
				sD = a;
			}
		}
		// Finally do the division to get sc and tc
		final var sc = MathUtil.isEpsilonZero(sN) ? 0. : sN / sD;
		final var tc = MathUtil.isEpsilonZero(tN) ? 0. : tN / tD;

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
	 * @return the distance between the segments.
	 * @since 18.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static double calculatesDistanceSegmentSegment(double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		return Math.sqrt(calculatesDistanceSquaredSegmentSegment(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4));
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 x location of the segment beginning.
	 * @param y1 y location of the segment beginning.
	 * @param z1 z location of the segment beginning.
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
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean isPointClosedToSegment(double x1, double y1, double z1, double x2, double y2, double z2, double x, double y,
			double z, double hitDistance) {
		assert hitDistance >= 0. : AssertMessages.positiveOrZeroParameter(9);
		return calculatesDistanceSegmentPoint(x1, y1, z1, x2, y2, z2, x, y, z) < hitDistance;
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 x location of the line beginning.
	 * @param y1 y location of the line beginning.
	 * @param z1 z location of the line beginning.
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
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean isPointClosedToLine(double x1, double y1, double z1, double x2, double y2, double z2, double x, double y,
			double z, double hitDistance) {
		assert hitDistance >= 0. : AssertMessages.positiveOrZeroParameter(9);
		return calculatesDistanceLinePoint(x1, y1, z1, x2, y2, z2, x, y, z) < hitDistance;
	}

	/**
	 * Replies the projection a point on a segment.
	 *
	 * @param px
	 *            is the coordinate of the point to project
	 * @param py
	 *            is the coordinate of the point to project
	 * @param pz
	 *            is the coordinate of the point to project
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
	 *     If {@link Double#NaN}, the line points are the same and the projection
	 *     point cannot be determined because there is an infinite set of points.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static double calculatesProjectedPointOnLine(double px, double py, double pz, double s1x, double s1y, double s1z, double s2x,
			double s2y, double s2z) {
		final var vx = s2x - s1x;
		final var vy = s2y - s1y;
		final var vz = s2z - s1z;
		final var denomenator = vx * vx + vy * vy + vz * vz;
		final var numerator = (px - s1x) * vx + (py - s1y) * vy + (pz - s1z) * vz;
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
	@SuppressWarnings("checkstyle:parameternumber")
	static double calculatesRelativeDistanceLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final var x21 = x2 - x1;
		final var y21 = y2 - y1;
		final var z21 = z2 - z1;
		final var denomenator = x21 * x21 + y21 * y21 + z21 * z21;
		if (denomenator == 0.) {
			return Point3D.getDistancePointPoint(px, py, pz, x1, y1, z1);
		}
		final var factor = ((y1 - py) * x21 - (x1 - px) * y21) / denomenator;
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
		assert factor >= 0. && factor <= 1.
				: AssertMessages.outsideRangeInclusiveParameter(Double.valueOf(factor), Double.valueOf(0), Double.valueOf(1));
		final var vx = p2x - p1x;
		final var vy = p2y - p1y;
		final var vz = p2z - p1z;
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
	@SuppressWarnings("checkstyle:parameternumber")
	static void calculatesFarthestPointToPoint(double ax, double ay, double az, double bx, double by, double bz, double px, double py,
			double pz, Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		final var xpa = px - ax;
		final var ypa = py - ay;
		final var zpa = pz - az;
		final var xpb = px - bx;
		final var ypb = py - by;
		final var zpb = pz - bz;
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
	 * @param result the is point on the shape. It cannot be {@code null}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsClosestPointToPoint(
			double ax, double ay, double az, double bx, double by, double bz, double px, double py, double pz,
			Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		final var ratio = Segment3afp.calculatesProjectedPointOnLine(px, py, pz, ax, ay, az, bx, by, bz);
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
	 * @param result the is point on the shape. It cannot be {@code null}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsClosestPointToSegment(
			double s1x1, double s1y1, double s1z1, double s1x2, double s1y2, double s1z2,
			double s2x1, double s2y1, double s2z1, double s2x2, double s2y2, double s2z2,
			Point3D<?, ?, ?> result) {
		findsClosestPointToSegment(
				s1x1, s1y1, s1z1, s1x2, s1y2, s1z2, s2x1, s2y1, s2z1, s2x2, s2y2, s2z2,
				result, null);
	}

	/** Replies the point on the first segment that is closest to the second segment.
	 *
	 * @param ax is the x coordinate of the first point of the first segment.
	 * @param ay is the y coordinate of the first point of the first segment.
	 * @param az is the z coordinate of the first point of the first segment.
	 * @param bx is the x coordinate of the second point of the first segment.
	 * @param by is the y coordinate of the second point of the first segment.
	 * @param bz is the z coordinate of the second point of the first segment.
	 * @param cx is the x coordinate of the first point of the second segment.
	 * @param cy is the y coordinate of the first point of the second segment.
	 * @param cz is the z coordinate of the first point of the second segment.
	 * @param dx is the x coordinate of the second point of the second segment.
	 * @param dy is the y coordinate of the second point of the second segment.
	 * @param dz is the z coordinate of the second point of the second segment.
	 * @param resultOnFirstSegment the point on the first segment. It can be {@code null}.
	 * @param resultOnSecondSegment the point on the second segment. It can be {@code null}.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:methodlength"})
	static void findsClosestPointToSegment(
			double ax, double ay, double az, double bx, double by, double bz,
			double cx, double cy, double cz, double dx, double dy, double dz,
			Point3D<?, ?, ?> resultOnFirstSegment, Point3D<?, ?, ?> resultOnSecondSegment) {
		assert resultOnFirstSegment != null || resultOnSecondSegment != null
				: AssertMessages.constraintViolation("at least on result argument must be not null"); //$NON-NLS-1$
		assert resultOnFirstSegment != resultOnSecondSegment
				: AssertMessages.constraintViolation("resultOnFirstSegment != resultOnSecondSegment"); //$NON-NLS-1$

		// Direction vectors
		// ab  = B - A
		final var abx = bx - ax;
		final var aby = by - ay;
		final var abz = bz - az;
		// cd  = D - C
		final var cdx = dx - cx;
		final var cdy = dy - cy;
		final var cdz = dz - cz;

		// |ab|^2
		final var abQuaredLength = Vector3D.dotProduct(abx, aby, abz, abx, aby, abz);
		// |cd|^2
		final var cdSquaredLength = Vector3D.dotProduct(cdx, cdy, cdz, cdx, cdy, cdz);

		// Case 0: both segments degenerate to points
		if (MathUtil.isEpsilonZero(abQuaredLength) && MathUtil.isEpsilonZero(cdSquaredLength)) {
			if (resultOnFirstSegment != null) {
				resultOnFirstSegment.set(ax, ay, az);
			}
			if (resultOnSecondSegment != null) {
				resultOnSecondSegment.set(cx, cy, cz);
			}
			return;
		}
		// Case 1: S1 is a point; project C onto S2
		if (MathUtil.isEpsilonZero(abQuaredLength)) {
			if (resultOnFirstSegment != null) {
				resultOnFirstSegment.set(ax, ay, az);
			}
			if (resultOnSecondSegment != null) {
				Segment3afp.findsClosestPointToPoint(cx, cy, cz, dx, dy, dz, ax, ay, az, resultOnSecondSegment);
			}
			return;
		}
		// Case 2: S2 is a point; project A onto S1
		if (MathUtil.isEpsilonZero(cdSquaredLength)) {
			if (resultOnFirstSegment != null) {
				Segment3afp.findsClosestPointToPoint(ax, ay, az, bx, by, bz, cx, cy, cz, resultOnFirstSegment);
			}
			if (resultOnSecondSegment != null) {
				resultOnSecondSegment.set(cx, cy, cz);
			}
			return;
		}

		// General case
		// w0 = A - C
		final var w0x = ax - cx;
		final var w0y = ay - cy;
		final var w0z = az - cz;
		final var eDotW0 = Vector3D.dotProduct(abx, aby, abz, w0x, w0y, w0z);

		final var beta = Vector3D.dotProduct(abx, aby, abz, cdx, cdy, cdz);
		final var fDotW0 = Vector3D.dotProduct(cdx, cdy, cdz, w0x, w0y, w0z);
		// |d x e|^2  (>= 0)
		final var denom  = abQuaredLength * cdSquaredLength - beta * beta;

		double sfinal;
		double tfinal;

		if (MathUtil.isEpsilonZero(denom)) {
			// Case 3: segments are (nearly) parallel
			// Fix s = 0, find the best t, then re-derive s and clamp both.
			sfinal = 0.;
			tfinal = MathUtil.clamp(fDotW0 / cdSquaredLength, 0., 1.);
			// (no need to re-derive s because it is already clamped to 0)
		} else {
			// Case 4: general skew / crossing segments
			final var sRaw = (beta * fDotW0 - cdSquaredLength * eDotW0) / denom;
			final var tRaw = (abQuaredLength * fDotW0 - beta * eDotW0) / denom;

			// Check whether both parameters are already inside [0,1].
			// If so we can skip all clamping branches (common hot path).
			if (sRaw >= 0. && sRaw <= 1. && tRaw >= 0. && tRaw <= 1.) {
				sfinal = sRaw;
				tfinal = tRaw;
			} else {
				// Clamp s and re-derive t (or vice-versa if t drives the clamp).
				// We evaluate the four edge-face candidates and pick the minimum.
				sfinal = MathUtil.clamp(sRaw, 0., 1.);
				tfinal = MathUtil.clamp(tRaw, 0., 1.);
				// Re-derive the *other* parameter from the clamped one on each face
				// and keep whichever boundary point is actually closest.

				// re-derive t
				final var tFromS = MathUtil.clamp((beta * sfinal + fDotW0) / cdSquaredLength, 0., 1.);
				// re-derive s
				final var sFromT = MathUtil.clamp((beta * tfinal - eDotW0) / abQuaredLength, 0., 1.);

				// Pick the boundary (s,t) pair with the smaller gap vector norm^2
				var vx = w0x + abx * sfinal - cdx * tFromS;
				var vy = w0y + aby * sfinal - cdy * tFromS;
				var vz = w0z + abz * sfinal - cdz * tFromS;
				final var gapST = Vector3D.dotProduct(vx, vy, vz, vx, vy, vz);

				vx = w0x + abx * sFromT - cdx * tfinal;
				vy = w0y + aby * sFromT - cdy * tfinal;
				vz = w0z + abz * sFromT - cdz * tfinal;
				final var gapTS = Vector3D.dotProduct(vx, vy, vz, vx, vy, vz);

				if (gapST <= gapTS) {
					tfinal = tFromS;
				} else {
					sfinal = sFromT;
				}
			}
		}

		final var r1x = ax + abx * sfinal;
		final var r1y = ay + aby * sfinal;
		final var r1z = az + abz * sfinal;
		final var r2x = cx + cdx * tfinal;
		final var r2y = cy + cdy * tfinal;
		final var r2z = cz + cdz * tfinal;
		if (resultOnFirstSegment != null) {
			resultOnFirstSegment.set(r1x, r1y, r1z);
		}
		if (resultOnSecondSegment != null) {
			resultOnSecondSegment.set(r2x, r2y, r2z);
		}
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
	 *     {@code false}
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
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
	 *     {@code false}
	 * @since 18.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean intersectsSegmentLineWithEnds(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		final var factors = calculatesLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		return factors.colinear || factors.position1 >= 0. && factors.position1 <= 1.;
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
	 *     {@code false}
	 * @since 18.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean intersectsSegmentLineWithoutEnds(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		final var factors = calculatesLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		return factors.colinear || factors.position1 > 0. && factors.position1 < 1.;
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
	 *     {@code false}
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double,
	 *     double, double, double, double, double, double)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean intersectsSegmentSegmentWithoutEnds(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final var factors = calculatesSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
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
	 *     {@code false}
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double,
	 *     double, double, double, double, double)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean intersectsSegmentSegmentWithEnds(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final var factors = calculatesSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
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

	@Pure
	@Override
	@Inline("isEmpty()")
	default boolean isDegeneratedPoint() {
		return isEmpty();
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
		var ratio = calculatesProjectedPointOnLine(point.getX(), point.getY(), point.getZ(), getX1(), getY1(), getZ1(), getX2(),
				getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final var vx = (getX2() - getX1()) * ratio;
		final var vy = (getY2() - getY1()) * ratio;
		final var vz = (getZ2() - getZ1()) * ratio;
		return Math.abs(getX1() + vx - point.getX())
				+ Math.abs(getY1() + vy - point.getY())
				+ Math.abs(getZ1() + vz - point.getZ());
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		var ratio = calculatesProjectedPointOnLine(point.getX(), point.getY(), point.getZ(), getX1(), getY1(), getZ1(), getX2(),
				getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final var vx = (getX2() - getX1()) * ratio;
		final var vy = (getY2() - getY1()) * ratio;
		final var vz = (getZ2() - getZ1()) * ratio;
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
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
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
		final var m = new Transform3D();
		m.setRotation(qx, qy, qz, qw);

		final var point1 = new InnerComputationPoint3D(
				getX1() - px, getY1() - py, getZ1() - pz);
		m.transform(point1);
		point1.add(px, py, pz);

		final var point2 = new InnerComputationPoint3D(
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
		final var m = new Transform3D();
		m.setRotation(qx, qy, qz, qw);

		final var point1 = new InnerComputationPoint3D(getX1(), getY1(), getZ1());
		m.transform(point1);

		final var point2 = new InnerComputationPoint3D(getX2(), getY2(), getZ2());
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

	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform3D transform) {
		final var p1 = getP1().clone();
		transform.transform(p1);
		final var p2 = getP2().clone();
		transform.transform(p2);
		return (ST) getGeomFactory().newSegment(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}

	@Override
	default void transform(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final var p = new InnerComputationPoint3D(getX1(),  getY1(), getZ1());
		transform.transform(p);
		final var x1 = p.getX();
		final var y1 = p.getY();
		final var z1 = p.getZ();
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
	default boolean clipToBox(Box3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		return clipToBox(
				box.getMinX(), box.getMinY(), box.getMinZ(),
				box.getMaxX(), box.getMaxY(), box.getMaxZ());
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
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(0, Double.valueOf(rxmin), 3, Double.valueOf(rxmax));
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(1, Double.valueOf(rymin), 4, Double.valueOf(rymax));
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(2, Double.valueOf(rzmin), 5, Double.valueOf(rzmax));
		var x0 = getX1();
		var y0 = getY1();
		var z0 = getZ1();
		var x1 = getX2();
		var y1 = getY2();
		var z1 = getZ2();
		var code1 = MathUtil.getCohenSutherlandCode3D(x0, y0, z0, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		var code2 = MathUtil.getCohenSutherlandCode3D(x1, y1, z1, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		var accept = false;
		var cont = true;
		final var point = new InnerComputationPoint3D();

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
				var code3 = code1 != 0 ? code1 : code2;

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
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
	static boolean calculatesLinePlaneIntersection(
			double lx1, double ly1, double lz1, double lx2, double ly2, double lz2,
			double a, double b, double c, double d,
			Point3D<?, ?, ?> result) {
		final var nx = a;
		final var ny = b;
		final var nz = c;

		var ux = lx2 - lx1;
		var uy = ly2 - ly1;
		var uz = lz2 - lz1;
		final var ul = Math.sqrt(ux * ux + uy * uy + uz * uz);
		ux /= ul;
		uy /= ul;
		uz /= ul;

		final var s = Vector3D.dotProduct(nx, ny, nz, ux, uy, uz);
		if (MathUtil.isEpsilonZero(s)) {
			// line and plane are parallel
			return false;
		}

		// Assuming L: P0 + si * u
		//
		//      -(a x0 + b y0 + c z0 + d)
		// si = -------------------------
		//               n.u
		final var si = -(a * lx1 + b * ly1 + c * lz1 + d) / s;

		ux *= si;
		uy *= si;
		uz *= si;

		if (result != null) {
			result.set(lx1 + ux, ly1 + uy, lz1 + uz);
		}

		return true;
	}

	/** Calculate the factor that corrsponds to the intersection point between a line and a plane.
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
	 * @return The factor; or {@link Double#NaN} if there is no intersection.
	 * @since 18.0
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
	static double calculatesLinePlaneIntersectionFactor(
			double lx1, double ly1, double lz1, double lx2, double ly2, double lz2,
			double a, double b, double c, double d) {
		final var nx = a;
		final var ny = b;
		final var nz = c;

		final var ux = lx2 - lx1;
		final var uy = ly2 - ly1;
		final var uz = lz2 - lz1;

		final var s = Vector3D.dotProduct(nx, ny, nz, ux, uy, uz);
		if (MathUtil.isEpsilonZero(s)) {
			// line and plane are parallel
			return Double.NaN;
		}

		// Assuming L: P0 + si * u
		//
		//      -(a x0 + b y0 + c z0 + d)
		// si = -------------------------
		//               n.u
		final var si = -(a * lx1 + b * ly1 + c * lz1 + d) / s;

		return si;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return Sphere3afp.intersectsSphereSegment(
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
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
		//final var mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		//return false;
		throw new Error();

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		Segment3afp.findsClosestPointToPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return getClosestPointTo(sphere.getCenter());
	}

	@Override
	@Unefficient
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		//final var point = getGeomFactory().newPoint();
		//TODO Path3afp.getClosestPointTo(getPathIterator(), path.getPathIterator(), point);
		throw new Error();
		//return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		final var boxPoint = new InnerComputationPoint3D();
		AlignedBox3afp.findsClosestPointAlignedBoxSegment(
				rectangle.getMinY(), rectangle.getMinY(), rectangle.getMinZ(),
				rectangle.getMaxX(), rectangle.getMaxY(), rectangle.getMaxZ(),
				getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(),
				boxPoint, point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		findsClosestPointToSegment(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(),
				segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2(), point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		Segment3afp.calculatesFarthestPointToPoint(
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
	 * has a length equal to the distance between the two segment points.
	 *
	 * @return the direction's unit vector.
	 * @see #getSegmentVector()
	 */
	@Pure
	default V getDirection() {
		final var vx = getX2() - getX1();
		final var vy = getY2() - getY1();
		final var vz = getZ2() - getZ1();
		final var l = Math.sqrt(vx * vx + vy * vy + vz * vz);
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
		final var vx = getX2() - getX1();
		final var vy = getY2() - getY1();
		final var vz = getZ2() - getZ1();
		return getGeomFactory().newVector(vx, vy, vz);
	}

	/** Replies this segment with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the segment.
	 * @since 18.0
	 */
	@Override
	default String toGeogebra() {
		return GeogebraUtil.toSegmentDefinition(3, getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Description of an intersection between two lines.
	 *
	 * @param colinear indicates if the two elements are colinear, i.e., the intersection concerns multiple points of the two geometric elements.
	 * @param position1 the position factor of the intersection point on the first geometric element. The value is in [0;1] if it is
	 *     located on the first segment.
	 * @param position2 if elements are not colinear, it is the position factor of the intersection point on the second geometric
	 *     element. The value is in [0;1] if it is located on the second segment.
	 *     If the elements are colinear, it is the position factor of the second point of the first geometric element. The value
	 *     is in [0;1] if it is located on the first segment.
	 * @param position3 if the elements are colinear, it is the position factor of the first point of the second geometric element.
	 *     The value is in [0;1] if it is located on the first segment.
	 * @param position4 if the elements are colinear, it is the position factor of the second point of the second geometric element.
	 *     The value is in [0;1] if it is located on the first segment.
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
	 * @param position1 the position factor of the intersection point on the first geometric element. The value is in [0;1]
	 *     if it is located on the first segment.
	 * @param position2 if elements are not colinear, it is the position factor of the intersection point on the second
	 *     geometric element. The value is in [0;1] if it is located on the second segment.
	 *     If the elements are colinear, it is the position factor of the second point of intersection on the first
	 *     geometric element. The value is in [0;1] if it is located on the first segment.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	record SegmentIntersection(boolean colinear, double position1, double position2) {
		//
	}

}

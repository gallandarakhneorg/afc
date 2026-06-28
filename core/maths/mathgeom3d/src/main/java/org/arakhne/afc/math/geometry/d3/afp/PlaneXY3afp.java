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

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Plane3D;
import org.arakhne.afc.math.geometry.base.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.PointVector3DReceiver;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** This class represents a 3D X-Y plane with double floating point coordinates.
 * This type of plane represents all the planes that are always parallel to the 0-X-Y plane.
 *
 * @param <PT> is the type of the plane.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <S> is the type of the of the geometric structure that represents the intersection of two planes.
 * @author $Author: sgalland$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public interface PlaneXY3afp<PT extends PlaneXY3afp<?, S, P, V, Q>,
		S extends Segment3afp<?, ?, ?, P, V, Q, ?>,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>>
	extends Plane3afp<PT, S, P, V, Q> {

	/** Calculates the distance between the given plane and this XY plane.
	 *
	 * <p>The replied distance may be positive if the second plane is located on the
	 * half-space of the normal of the plane. The distance may be negative of
	 * the second plane is located on the half-space that is opposite to the plane
	 * normal.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param a the a coordinate of the other plane.
	 * @param b the b coordinate of the other plane.
	 * @param c the c coordinate of the other plane.
	 * @param d the d coordinate of the other plane.
	 * @return the distance between the two planes. This distance may be
	 *     positive if the point is located on the half-space of the normal of the plane.
	 *     This distance may be negative of the point is located on the half-space that
	 *     is opposite to the plane normal.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parametername")
	static double calculatesPlaneXYPlaneDistance(
			boolean positive, double z,
			double a, double b, double c, double d) {
		final var length = Math.sqrt(a * a + b * b + c * c);
		final double nx;
		final double ny;
		final double nz;
		if (length != 1.) {
			nx = a / length;
			ny = b / length;
			nz = c / length;
		} else {
			nx = a;
			ny = b;
			nz = c;
		}
		if (Vector3D.isColinearVectors(nx, ny, nz, 0., 0., 1.)) {
			final double pz = nz >= 0. ? -d : d;
			return positive ? pz - z : z - pz;
		}
		return 0.;
	}

	/** Calculates the distance between the given point and this XY plane.
	 *
	 * <p>The replied distance may be positive if the point is located on the
	 * half-space of the normal of the plane. The distance may be negative of
	 * the point is located on the half-space that is opposite to the plane
	 * normal.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the c coordinate of the point.
	 * @return the distance from the plane to the point. This distance may be
	 *     positive if the point is located on the half-space of the normal of the plane.
	 *     This distance may be negative of the point is located on the half-space that
	 *     is opposite to the plane normal.
	 */
	@Pure
	static double calculatesPlaneXYPointDistance(
			boolean positive, double z,
			double px, double py, double pz) {
		return positive ? pz - z : z - pz;
	}

	/** Classifies the given point against to the XY plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the c coordinate of the point.
	 * @return the classification.
	 */
	@Pure
	static PlaneClassification classifiesPlaneXYPoint(
			boolean positive, double z,
			double px, double py, double pz) {
		if (pz == z) {
			return PlaneClassification.COINCIDENT;
		}
		if (pz > z) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
	}

	/** Classifies the given sphere against to the XY plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param sx the x coordinate of the sphere center.
	 * @param sy the y coordinate of the sphere center.
	 * @param sz the c coordinate of the sphere center.
	 * @param radius the radius of the sphere.
	 * @return the classification.
	 */
	@Pure
	static PlaneClassification classifiesPlaneXYSphere(
			boolean positive, double z,
			double sx, double sy, double sz, double radius) {
		if (sz - radius < z && z < sz + radius) {
			return PlaneClassification.COINCIDENT;
		}
		if (sz > z) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
	}

	/** Replies the intersection factor of the given segment when it is intersecting the XY plane.
	 *
	 * <p>If the segment and the plane are not intersecting, this
	 * function replies {@link Double#NaN}.
	 * If the segment and the plane are intersecting, two cases:<ol>
	 * <li>the segment is colinear to the plane, then this function replies
	 * {@link Double#POSITIVE_INFINITY}.</li>
	 * <li>the segment and the plane have a single point of intersection,
	 * then this function replies the factor of the line's equation that
	 * permits to retrieve the intersection point from the segment definition,
	 * i.e, {@code I = P1 + factor * (P2 - P1)}.
	 * </ol>
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return the factor that permits to compute the intersection point,
	 *     {@link Double#NaN} when no intersection, {@link Double#POSITIVE_INFINITY}
	 *     when an infinite number of intersection points.
	 */
	@Pure
	static double calculatesPlaneXYSegmentIntersectionFactor(
			boolean positive, double z,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		final var d = positive ? -z : z;
		final var c = positive ? 1. : -1.;
		final var denom = c * (sz2 - sz1);
		if (denom == 0.) {
			// Segment and plane are parallel
			// Compute the distance between a point of the segment and the plane.
			final var dist = c * sz1 + d;
			if (MathUtil.isEpsilonZero(dist)) {
				return Double.POSITIVE_INFINITY;
			}
		} else {
			final var factor = (-c * sz1 - d) / denom;
			if (factor >= 0. && factor <= 1.) {
				return factor;
			}
		}
		return Double.NaN;
	}

	/** Replies the intersection point of the given segment when it is intersecting the XY plane.
	 *
	 * <p>If the segment and the plane are not intersecting, this
	 * function replies {@code false}.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @param result the receiver of the intersection coordinates if an intersection exists.
	 * @return {@code true} if an intersection exists; otherwise {@code false}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean findsPlaneXYSegmentIntersection(
			boolean positive, double z,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			Point3D<?, ?, ?> result) {
		final var factor = calculatesPlaneXYSegmentIntersectionFactor(
				positive, z, sx1, sy1, sz1, sx2, sy2, sz2);
		if (Double.isNaN(factor)) {
			return false;
		}
		if (result != null) {
			if (Double.isInfinite(factor)) {
				result.set(sx1, sy1, sz1);
			} else {
				result.set(
						sx1 + factor * (sx2 - sx1),
						sy1 + factor * (sy2 - sy1),
						sz1 + factor * (sz2 - sz1));
			}
		}
		return true;
	}

	/**
	 * Computes the closest points between a 3D segment and an axis-aligned
	 * rectangle coplanar to the XY plane (constant z).
	 *
	 * <p>The rectangle is defined by its minimum corner (rx, ry, z) and its
	 * maximum corner (rmaxx, rmaxy, z).
	 *
	 * <p>Returns:
	 * <ul>
	 *   <li>the closest point on the segment,</li>
	 *   <li>the closest point on the rectangle,</li>
	 *   <li>the squared distance between them.</li>
	 * </ul>
	 *
	 * @param rx  left (min-x) edge of the rectangle.
	 * @param ry  bottom (min-y) edge of the rectangle.
	 * @param rmaxx  right (max-x) edge of the rectangle.
	 * @param rmaxy  top (max-y) edge of the rectangle.
	 * @param z  z-level of the rectangle's plane.
	 * @param sx1 x of segment start
	 * @param sy1 y of segment start
	 * @param sz1 z of segment start
	 * @param sx2 x of segment end
	 * @param sy2 y of segment end
	 * @param sz2 z of segment end
	 * @param resultRectangle is the closest point on the rectangle. It cannot be {@code null}.
	 * @param resultSegment is the closest point on the segment. It can be {@code null}.
	 * @return the squared distance between the plane and the segment.
	 */
	@Unefficient
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
	static double findsClosestPointRectangleXYSegment(
			double rx, double ry,
			double rmaxx, double rmaxy,
			double z,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			Point3D<?, ?, ?> resultRectangle, Point3D<?, ?, ?> resultSegment) {
		assert resultRectangle != null : AssertMessages.notNullParameter(11);

		// Step 1 – Fast path: does the segment pierce the plane z = z at a point inside the rectangle?

		// Parametric form: P(t) = S1 + t*(S2-S1),  t in [0,1].
		// z-component: sz1 + t*(sz2-sz1) = z  =>  t = (z-sz1) / dz
		var dz = sz2 - sz1;
		if (!MathUtil.isEpsilonZero(dz)) {
			final var t = (z - sz1) / dz;
			if (t >= 0. && t <= 1.) {
				final var ix = sx1 + t * (sx2 - sx1);
				final var iy = sy1 + t * (sy2 - sy1);
				// Is the intersection XY-point inside the rectangle?
				if (ix >= rx && ix <= rmaxx && iy >= ry && iy <= rmaxy) {
					// Distance is 0; both closest points are the intersection.
					resultRectangle.set(ix, iy, z);
					if (resultSegment != null) {
						resultSegment.set(ix, iy, z);
					}
					return 0.;
				}
			}
		}

		// Step 2 – General case.

		// Rectangle corners (all at height z):
		//   A = (rx,    ry,    z)   B = (rmaxx, ry,    z)
		//   C = (rmaxx, rmaxy, z)   D = (rx,    rmaxy, z)
		//
		// The four edges are AB, BC, CD, DA.
		// For each edge we compute the closest pair (segment <-> edge).
		// findsClosestPointToSegment returns the squared distance and writes:
		//   resultOnFirstSegment ->closest point on *our* segment (= seg side)
		//   resultOnSecondSegment -> closest point on the *edge* (= rect side)

		// Edge AB: A=(rx,ry,z) -> B=(rmaxx,ry,z)
		final var segment = new InnerComputationPoint3D();
		final var rectangle = new InnerComputationPoint3D();
		Segment3afp.findsClosestPointToSegment(
				sx1, sy1, sz1, sx2, sy2, sz2,
				rx, ry, z, rmaxx, ry, z,
				segment, rectangle);

		var bestDistance = segment.getDistanceSquared(rectangle);
		final var bestSegment = new InnerComputationPoint3D(segment);
		final var bestRectangle = new InnerComputationPoint3D(rectangle);

		// Edge BC: B=(rmaxx,ry,z) -> C=(rmaxx,rmaxy,z)
		Segment3afp.findsClosestPointToSegment(
				sx1, sy1, sz1, sx2, sy2, sz2,
				rmaxx, ry, z, rmaxx, rmaxy, z,
				segment, rectangle);
		var distance = segment.getDistanceSquared(rectangle);
		if (distance < bestDistance) {
			bestDistance = distance;
			bestSegment.set(segment);
			bestRectangle.set(rectangle);
		}

		// Edge CD: C=(rmaxx,rmaxy,z) -> D=(rx,rmaxy,z)
		Segment3afp.findsClosestPointToSegment(
				sx1, sy1, sz1, sx2, sy2, sz2,
				rmaxx, rmaxy, z, rx, rmaxy, z,
				segment, rectangle);
		distance = segment.getDistanceSquared(rectangle);
		if (distance < bestDistance) {
			bestDistance = distance;
			bestSegment.set(segment);
			bestRectangle.set(rectangle);
		}

		// Edge DA: D=(rx,rmaxy,z) -> A=(rx,ry,z)
		Segment3afp.findsClosestPointToSegment(
				sx1, sy1, sz1, sx2, sy2, sz2,
				rx, rmaxy, z, rx, ry, z,
				segment, rectangle);
		distance = segment.getDistanceSquared(rectangle);
		if (distance < bestDistance) {
			bestDistance = distance;
			bestSegment.set(segment);
			bestRectangle.set(rectangle);
		}

		// Step 3 – Interior projection

		// Project each segment endpoint onto the plane, clamp to the
		// rectangle, then find the closest point on the segment to that
		// clamped point.  This handles the case where the segment "hovers"
		// directly above the rectangle interior (closest rect point is not
		// on any edge but in the filled interior).

		// Projection of S1 onto plane, clamped to rectangle
		var crx = MathUtil.clamp(sx1, rx, rmaxx);
		var cry = MathUtil.clamp(sy1, ry, rmaxy);
		// Closest point on segment to (crx, cry, z)
		var ratio = MathUtil.clamp(
				Segment3afp.calculatesProjectedPointOnLine(
						crx, cry, z,
						sx1, sy1, sz1, sx2, sy2, sz2),
				0., 1.);
		double segx;
		double segy;
		double segz;
		if (Double.isNaN(ratio)) {
			segx = sx1;
			segy = sy1;
			segz = sz1;
		} else {
			segx = sx1 + ratio * (sx2 - sx1);
			segy = sy1 + ratio * (sy2 - sy1);
			segz = sz1 + ratio * (sz2 - sz1);
		}
		var dx = segx - crx;
		var dy = segy - cry;
		dz = segz - z;
		distance = dx * dx + dy * dy + dz * dz;
		if (distance < bestDistance) {
			bestDistance = distance;
			bestSegment.set(segx, segy, segz);
			bestRectangle.set(crx, cry, z);
		}

		// Projection of S2 onto plane, clamped to rectangle
		crx = MathUtil.clamp(sx2, rx, rmaxx);
		cry = MathUtil.clamp(sy2, ry, rmaxy);
		// Closest point on segment to (crx, cry, z)
		ratio = MathUtil.clamp(
				Segment3afp.calculatesProjectedPointOnLine(
						crx, cry, z,
						sx1, sy1, sz1, sx2, sy2, sz2),
				0., 1.);
		if (Double.isNaN(ratio)) {
			segx = sx1;
			segy = sy1;
			segz = sz1;
		} else {
			segx = sx1 + ratio * (sx2 - sx1);
			segy = sy1 + ratio * (sy2 - sy1);
			segz = sz1 + ratio * (sz2 - sz1);
		}
		dx = segx - crx;
		dy = segy - cry;
		dz = segz - z;
		distance = dx * dx + dy * dy + dz * dz;
		if (distance < bestDistance) {
			bestDistance = distance;
			bestSegment.set(segx, segy, segz);
			bestRectangle.set(crx, cry, z);
		}

		resultRectangle.set(bestRectangle);
		if (resultSegment != null) {
			resultSegment.set(bestSegment);
		}
		return bestDistance;
	}

	/** Calculates the line that corresponds to the intersection between the XY plane and the general plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param result the receiver of the intersection values.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean findsPlaneXYPlaneIntersection(
			boolean positive, double z,
			double a2, double b2, double c2, double d2,
			Segment3afp<?, ?, ?, ?, ?, ?, ?> result) {
		return findsPlaneXYPlaneIntersection(positive, z, a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (result != null) {
						result.set(px, py, pz, px + vx, py + vy, pz + vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the XY plane and the general plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param coordinateReceiver the receiver of the segment coordinate. The arguments are
	 *     the x, y and z coordinates of the first point of the segment, and the
	 *     x, y and z coordinates of the second point of the segment.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean findsPlaneXYPlaneIntersection(
			boolean positive, double z,
			double a2, double b2, double c2, double d2,
			PointVector3DReceiver coordinateReceiver) {
		return Plane3afp.findsPlanePlaneIntersection(
				0., 0., positive ? 1. : -1., positive ? -z : z,
						a2, b2, c2, d2,
						coordinateReceiver);
	}

	/** Calculates the line that corresponds to the intersection between the XY plane and the general plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param intersectionPoint the receiver of the coordinates of a point of the intersection line.
	 * @param intersectionDirection the receiver of the coordinates of the direction vector of the intersection line.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean findsPlaneXYPlaneIntersection(
			boolean positive, double z,
			double a2, double b2, double c2, double d2,
			Point3D<?, ?, ?> intersectionPoint,
			Vector3D<?, ?, ?> intersectionDirection) {
		return findsPlaneXYPlaneIntersection(positive, z, a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (intersectionPoint != null) {
						intersectionPoint.set(px, py, pz);
					}
					if (intersectionDirection != null) {
						intersectionDirection.set(vx, vy, vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the XY plane and the general plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param firstSegmentPoint the receiver of the coordinates of the first point of the intersection line.
	 * @param secondSegmentPoint the receiver of the coordinates of the second point of the intersection line.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean findsPlaneXYPlaneIntersection(
			boolean positive, double z,
			double a2, double b2, double c2, double d2,
			Point3D<?, ?, ?> firstSegmentPoint,
			Point3D<?, ?, ?> secondSegmentPoint) {
		return findsPlaneXYPlaneIntersection(positive, z, a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (firstSegmentPoint != null) {
						firstSegmentPoint.set(px, py, pz);
					}
					if (secondSegmentPoint != null) {
						secondSegmentPoint.set(px + vx, py + vy, pz + vz);
					}
				});
	}

	/** Classifies the given segment against to the XY plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param z1 the z coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @param z2 the z coordinate of the second point of the segment.
	 * @return the classification.
	 */
	@Pure
	static PlaneClassification classifiesPlaneXYSegment(
			boolean positive, double z,
			double x1, double y1, double z1,
			double x2, double y2, double z2) {
		if (z1 < z) {
			if (z2 > z) {
				return PlaneClassification.COINCIDENT;
			}
			return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
		}
		if (z1 > z) {
			if (z2 < z) {
				return PlaneClassification.COINCIDENT;
			}
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		if (z2 == z) {
			return PlaneClassification.COINCIDENT;
		}
		if (z2 > z) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
	}

	/** Classifies the given segment against to the XY plane.
	 *
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param z the z coordinate that is for all the points of the plane.
	 * @param minx the x coordinate of the minimum point of the aligned box.
	 * @param miny the y coordinate of the minimum point of the aligned box.
	 * @param minz the z coordinate of the minimum point of the aligned box.
	 * @param maxx the x coordinate of the maximum point of the aligned box.
	 * @param maxy the y coordinate of the maximum point of the aligned box.
	 * @param maxz the z coordinate of the maximum point of the aligned box.
	 * @return the classification.
	 */
	@Pure
	static PlaneClassification classifiesPlaneXYAlignedBox(
			boolean positive, double z,
			double minx, double miny, double minz,
			double maxx, double maxy, double maxz) {
		assert minx <= maxx : AssertMessages.lowerEqualParameters(2, Double.valueOf(minx), 5, Double.valueOf(maxx));
		assert miny <= maxy : AssertMessages.lowerEqualParameters(3, Double.valueOf(miny), 6, Double.valueOf(maxy));
		assert minz <= maxz : AssertMessages.lowerEqualParameters(4, Double.valueOf(minz), 7, Double.valueOf(maxz));
		if (minz < z) {
			if (maxz > z) {
				return PlaneClassification.COINCIDENT;
			}
			return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
		}
		if (minz > z) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		if (maxz == z) {
			return PlaneClassification.COINCIDENT;
		}
		return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
	}

	/** Indicates if this plane is oriented to the positive side.
	 * If {@code true}, the normal of the plane is directed
	 * to the positive infinity.
	 *
	 * @return {@code true} if the plan normal is positive.
	 */
	boolean isPositive();

	/** Change if this plane is oriented to the positive side.
	 * If {@code true}, the normal of the plane is directed
	 * to the positive infinity.
	 *
	 * @param positive {@code true} if the plan normal is positive.
	 */
	void setPositive(boolean positive);

	/** Set the z coordinate of the plane.
	 *
	 * @param z z coordinate of the point that is inside the plane.
	 */
	void setZ(double z);

	/** Replies the z coordinate of the plane.
	 *
	 * @return the z coordinate of the plane.
	 */
	@Pure
	double getZ();

	@Override
	default P getPivot() {
		return getGeomFactory().newPoint(0., 0., getZ());
	}

	@Override
	default void setPivotToDefault() {
		//
	}

	@Pure
	@Override
	default double getNormalX() {
		return 0.;
	}

	@Pure
	@Override
	default double getNormalY() {
		return 0.;
	}

	@Pure
	@Override
	default double getNormalZ() {
		return isPositive() ? 1 : -1;
	}

	@Pure
	@Override
	default double getEquationComponentA() {
		return 0.;
	}

	@Pure
	@Override
	default double getEquationComponentB() {
		return 0.;
	}

	@Pure
	@Override
	default double getEquationComponentC() {
		return isPositive() ? 1 : -1;
	}

	@Pure
	@Override
	default double getEquationComponentD() {
		return isPositive() ? -getZ() : getZ();
	}

	@Override
	@SuppressWarnings("checkstyle:parametername")
	default void set(double a, double b, double c, double d) {
		final var pos = c >= 0.;
		setPositive(pos);
		setZ(pos ? -d : d);
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	default void set(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z) {
		final var crossProductZ = (p2x - p1x) * (p3y - p1y) - (p2y - p1y) * (p3x - p1x);
		setPositive(crossProductZ >= 0.);
		setZ((p1z + p2z + p3z) / 3.);
	}

	@Override
	default void set(Point3D<?, ?, ?> pivot, Vector3D<?, ?, ?> vector1, Vector3D<?, ?, ?> vector2) {
		assert pivot != null : AssertMessages.notNullParameter(0);
		assert vector1 != null : AssertMessages.notNullParameter(1);
		assert vector2 != null : AssertMessages.notNullParameter(2);
		final var crossProductZ = vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX();
		setPositive(crossProductZ >= 0.);
		setZ(pivot.getZ());
	}

	@Override
	default void set(Point3D<?, ?, ?> pivot, Vector3D<?, ?, ?> normal) {
		assert pivot != null : AssertMessages.notNullParameter(0);
		assert normal != null : AssertMessages.notNullParameter(1);
		setPositive(normal.getZ() >= 0.);
		setZ(pivot.getZ());
	}

	@Override
	default void clear() {
		setPositive(true);
		setZ(0.);
	}

	@Override
	@Inline("setZ($2)")
	default void setPivot(double x, double y, double z) {
		setZ(z);
	}

	@Override
	default P getProjection(double x, double y, double z) {
		return getGeomFactory().newPoint(x, y, getZ());
	}

	@Override
	default double getDistanceTo(double x, double y, double z) {
		return calculatesPlaneXYPointDistance(isPositive(), getZ(), x, y, z);
	}

	@Override
	@SuppressWarnings("checkstyle:parametername")
	default double getDistanceTo(double a, double b, double c, double d) {
		return calculatesPlaneXYPlaneDistance(isPositive(), getZ(), a, b, c, d);
	}

	@Override
	default void translate(double x, double y, double z) {
		setZ(getZ() + z);
	}

	@Override
	default void translate(double z) {
		setZ(getZ() + z);
	}

	@Override
	default void transform(Transform3D transform, Point3D<?, ?, ?> pivot) {
		assert transform != null : AssertMessages.notNullParameter(0);

		final var newNormal = new InnerComputationVector3D(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC());
		transform.transform(newNormal);

		final var newZ = getZ() + transform.getTranslationZ();

		setPositive(newNormal.getZ() >= 0.);
		setZ(newZ);
	}

	@Override
	@SuppressWarnings({"unchecked", "checkstyle:parametername"})
	default S getIntersection(double a, double b, double c, double d) {
		final var result = (S) getGeomFactory().newSegment();
		if (findsPlaneXYPlaneIntersection(isPositive(), getZ(), a, b, c, d, result)) {
			return result;
		}
		return null;
	}

	@Override
	default P getIntersection(Segment3afp<?, ?, ?, ?, ?, ?, ?> line) {
		assert line != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		if (findsPlaneXYSegmentIntersection(isPositive(), getZ(),
				line.getX1(), line.getY1(), line.getZ1(),
				line.getX2(), line.getY2(), line.getZ2(), point)) {
			return point;
		}
		return null;
	}

	@Override
	default PlaneClassification classifies(double x, double y, double z) {
		return classifiesPlaneXYPoint(isPositive(), getZ(), x, y, z);
	}

	@Override
	default PlaneClassification classifies(double x, double y, double z, double radius) {
		return classifiesPlaneXYSphere(
				isPositive(), getZ(),
				x, y, z, radius);
	}

	@Override
	default PlaneClassification classifies(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return classifiesPlaneXYSegment(
				isPositive(), getZ(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Override
	default PlaneClassification classifies(double lx, double ly, double lz, double ux, double uy, double uz) {
		return classifiesPlaneXYAlignedBox(isPositive(), getZ(), lx, ly, lz, ux, uy, uz);
	}

	@Override
	default PlaneClassification classifies(Plane3D<?, ?, ?, ?, ?> otherPlane) {
		assert otherPlane != null : AssertMessages.notNullParameter();
		return classifiesPlaneXYPlane(
				isPositive(), getZ(),
				otherPlane.getEquationComponentA(), otherPlane.getEquationComponentB(),
				otherPlane.getEquationComponentC(), otherPlane.getEquationComponentD());
	}

	@Override
	default boolean intersects(double x, double y, double z) {
		return z == getZ();
	}

	@Override
	default boolean intersects(double x, double y, double z, double radius) {
		final var pz = getZ();
		return z - radius < pz && pz < z + radius;
	}

	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final var pz = getZ();
		final var z1 = segment.getZ1();
		final var z2 = segment.getZ2();
		return z1 < pz && z2 > pz || z2 < pz && z1 > pz;
	}

	/** Classifies the given XY plane against to another plane.
	 *
	 * @param positive indicates of the plane is positive.
	 * @param z is the z coordinate of the points of the plane.
	 * @param a first component of the second plane equation.
	 * @param b second component of the second plane equation.
	 * @param c third component of the second plane equation.
	 * @param d fourth component of the second plane equation.
	 * @return the classification.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parametername")
	static PlaneClassification classifiesPlaneXYPlane(
			boolean positive, double z,
			double a, double b, double c, double d) {
		final var normalSqLength = a * a + b * b + c * c;
		final double nz;
		if (MathUtil.isEpsilonEqual(normalSqLength, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nz = c;
		} else {
			final var normalLength = Math.sqrt(normalSqLength);
			nz = c / normalLength;
		}
		if (MathUtil.isEpsilonEqual(Math.abs(nz), 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			// planes are parallel
			final var z2 = nz >= 0. ? -d : d;
			// Plane normals are to the same direction
			if (MathUtil.isEpsilonEqual(z, z2, GeomConstants.UNIT_VECTOR_EPSILON)) {
				return PlaneClassification.COINCIDENT;
			}
			if (positive) {
				if (z2 >= z) {
					return PlaneClassification.IN_FRONT_OF;
				}
				return PlaneClassification.BEHIND;
			}
			if (z2 >= z) {
				return PlaneClassification.BEHIND;
			}
			return PlaneClassification.IN_FRONT_OF;
		}
		return PlaneClassification.COINCIDENT;
	}

}

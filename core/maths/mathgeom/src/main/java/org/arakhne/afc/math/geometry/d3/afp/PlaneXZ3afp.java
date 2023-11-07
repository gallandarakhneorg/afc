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

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.PointVector3DReceiver;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** This class represents a 3D X-Z plane with double floating point coordinates.
 * This type of plane represents all the planes that are always parallel to the 0-X-Z plane.
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
public interface PlaneXZ3afp<PT extends PlaneXZ3afp<?, S, P, V, Q>,
		S extends Segment3afp<?, ?, ?, P, V, Q, ?>,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>>
	extends Plane3afp<PT, S, P, V, Q> {
	
	/** Calculates the distance between the given plane and this XZ plane.
	 *
	 * <p>The replied distance may be positive if the second plane is located on the
	 * half-space of the normal of the plane. The distance may be negative of
	 * the second plane is located on the half-space that is opposite to the plane
	 * normal. 
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
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
	static double calculatesPlaneXZPlaneDistance(
			boolean positive, double y,
			double a, double b, double c, double d) {
		final double length = Math.sqrt(a * a + b * b + c * c);
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
		if (Vector3D.isColinearVectors(nx, ny, nz, 0., 1., 0.)) {
			final double py = ny >= 0. ? -d : d;
			return positive ? py - y : y - py;
		}
		return 0.;
	}

	/** Calculates the distance between the given point and this XZ plane.
	 *
	 * <p>The replied distance may be positive if the point is located on the
	 * half-space of the normal of the plane. The distance may be negative of
	 * the point is located on the half-space that is opposite to the plane
	 * normal. 
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the c coordinate of the point.
	 * @return the distance from the plane to the point. This distance may be
	 *     positive if the point is located on the half-space of the normal of the plane.
	 *     This distance may be negative of the point is located on the half-space that
	 *     is opposite to the plane normal. 
	 */
	@Pure
	static double calculatesPlaneXZPointDistance(
			boolean positive, double y,
			double px, double py, double pz) {
		return positive ? py - y : y - py;
	}

	/** Classifies the given point against to the XZ plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the c coordinate of the point.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlaneXZPoint(
			boolean positive, double y,
			double px, double py, double pz) {
		if (py == y) {
			return PlaneClassification.COINCIDENT;
		}
		if (py > y) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
	}

	/** Classifies the given sphere against to the XZ plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param sx the x coordinate of the sphere center.
	 * @param sy the y coordinate of the sphere center.
	 * @param sz the c coordinate of the sphere center.
	 * @param radius the radius of the sphere.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlaneXZSphere(
			boolean positive, double y,
			double sx, double sy, double sz, double radius) {
		if (sy - radius < y && y < sy + radius) {
			return PlaneClassification.COINCIDENT;
		}
		if (sy > y) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
	}

	/** Replies the intersection factor of the given segment when it is intersecting the XZ plane.
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
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return the factor that permits to compute the intersection point,
	 * {@link Double#NaN} when no intersection, {@link Double#POSITIVE_INFINITY}
	 * when an infinite number of intersection points.
	 */
	@Pure
	static double calculatesPlaneXZSegmentIntersectionFactor(
			boolean positive, double y,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		final double d = positive ? -y : y;
		final double b = positive ? 1. : -1.;
		final double denom = b * (sy2 - sy1);
		if (denom == 0.) {
			// Segment and plane are parallel
			// Compute the distance between a point of the segment and the plane.
			final double dist = b * sy1 + d;
			if (MathUtil.isEpsilonZero(dist)) {
				return Double.POSITIVE_INFINITY;
			}
		} else {
			final double factor = (-b * sy1 - d) / denom;
			if (factor >= 0. && factor <= 1.) {
				return factor;
			}
		}
		return Double.NaN;
	}

	/** Replies the intersection point of the given segment when it is intersecting the XZ plane.
	 *
	 * <p>If the segment and the plane are not intersecting, this
	 * function replies {@code false}.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
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
	static boolean calculatesPlaneXZSegmentIntersection(
			boolean positive, double y,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			Point3D<?, ?, ?> result) {
		final double factor = calculatesPlaneXZSegmentIntersectionFactor(positive, y, sx1, sy1, sz1, sx2, sy2, sz2);
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

	/** Calculates the line that corresponds to the intersection between the XZ plane and the general plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param result the receiver of the intersection values.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean calculatesPlaneXZPlaneIntersection(
			boolean positive, double y,
			double a2, double b2, double c2, double d2,
			Segment3afp<?, ?, ?, ?, ?, ?, ?> result) {
		return calculatesPlaneXZPlaneIntersection(positive, y, a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (result != null) {
						result.set(px, py, pz, px + vx, py + vy, pz + vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the XZ plane and the general plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param firstSegmentPoint the receiver of the coordinates of the first point of the intersection line.
	 * @param secondSegmentPoint the receiver of the coordinates of the second point of the intersection line.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean calculatesPlaneXZPlaneIntersection(
			boolean positive, double y,
			double a2, double b2, double c2, double d2,
			Point3D<?, ?, ?> firstSegmentPoint,
			Point3D<?, ?, ?> secondSegmentPoint) {
		return calculatesPlaneXZPlaneIntersection(positive, y, a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (firstSegmentPoint != null) {
						firstSegmentPoint.set(px, py, pz);
					}
					if (secondSegmentPoint != null) {
						secondSegmentPoint.set(px + vx, py + vy, pz + vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the XZ plane and the general plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param intersectionPoint the receiver of the coordinates of a point of the intersection line.
	 * @param intersectionDirection the receiver of the coordinates of the direction vector of the intersection line.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean calculatesPlaneXZPlaneIntersection(
			boolean positive, double y,
			double a2, double b2, double c2, double d2,
			Point3D<?, ?, ?> intersectionPoint,
			Vector3D<?, ?, ?> intersectionDirection) {
		return calculatesPlaneXZPlaneIntersection(positive, y, a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (intersectionPoint != null) {
						intersectionPoint.set(px, py, pz);
					}
					if (intersectionDirection != null) {
						intersectionDirection.set(vx, vy, vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the XZ plane and the general plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
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
	static boolean calculatesPlaneXZPlaneIntersection(
			boolean positive, double y,
			double a2, double b2, double c2, double d2,
			PointVector3DReceiver coordinateReceiver) {
		return Plane3afp.calculatesPlanePlaneIntersection(
				0., positive ? 1. : -1., 0., positive ? -y : y,
				a2, b2, c2, d2,
				coordinateReceiver);
	}

	/** Classifies the given segment against to the XZ plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param z1 the z coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @param z2 the z coordinate of the second point of the segment.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlaneXZSegment(
			boolean positive, double y,
			double x1, double y1, double z1,
			double x2, double y2, double z2) {
		if (y1 < y) {
			if (y2 > y) {
				return PlaneClassification.COINCIDENT;
			}
			return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
		}
		if (y1 > y) {
			if (y2 < y) {
				return PlaneClassification.COINCIDENT;
			}
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		if (y2 == y) {
			return PlaneClassification.COINCIDENT;
		}
		if (y2 > y) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
	}

	/** Classifies the given segment against to the XZ plane.
	 * 
	 * @param positive indicates if the normal of the plan is positive or not.
	 * @param y the y coordinate that is for all the points of the plane.
	 * @param minx the x coordinate of the minimum point of the aligned box.
	 * @param miny the y coordinate of the minimum point of the aligned box.
	 * @param minz the z coordinate of the minimum point of the aligned box.
	 * @param maxx the x coordinate of the maximum point of the aligned box.
	 * @param maxy the y coordinate of the maximum point of the aligned box.
	 * @param maxz the z coordinate of the maximum point of the aligned box.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlaneXZAlignedBox(
			boolean positive, double y,
			double minx, double miny, double minz,
			double maxx, double maxy, double maxz) {
		assert minx <= maxx : AssertMessages.lowerEqualParameters(2, minx, 5, maxx);
		assert miny <= maxy : AssertMessages.lowerEqualParameters(3, miny, 6, maxy);
		assert minz <= maxz : AssertMessages.lowerEqualParameters(4, minz, 7, maxz);
		if (miny < y) {
			if (maxy > y) {
				return PlaneClassification.COINCIDENT;
			}
			return positive ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
		}
		if (miny > y) {
			return positive ? PlaneClassification.IN_FRONT_OF : PlaneClassification.BEHIND;
		}
		if (maxy == y) {
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

	/** Set the y coordinate of the plane.
	 *
	 * @param y y coordinate of the point that is inside the plane.
	 */
	void setY(double y);

	/** Replies the y coordinate of the plane.
	 *
	 * @return the y coordinate of the plane.
	 */
	@Pure
	double getY();

	@Override
	default P getPivot() {
		return getGeomFactory().newPoint(0., getY(), 0.);
	}

	@Pure
	@Override
	default double getNormalX() {
		return 0.;
	}

	@Pure
	@Override
	default double getNormalY() {
		return isPositive() ? 1 : -1;
	}

	@Pure
	@Override
	default double getNormalZ() {
		return 0.;
	}

	@Pure
	@Override
	default double getEquationComponentA() {
		return 0.;
	}

	@Pure
	@Override
	default double getEquationComponentB() {
		return isPositive() ? 1 : -1;
	}

	@Pure
	@Override
	default double getEquationComponentC() {
		return 0.;
	}

	@Pure
	@Override
	default double getEquationComponentD() {
		return isPositive() ? -getY() : getY();
	}

	@Override
	default void set(double a, double b, double c, double d) {
		final boolean pos = b >= 0.;
		setPositive(pos);
		setY(pos ? -d : d);
	}

	@Override
	@Inline("setY(0.0)")
	default void clear() {
		setPositive(true);
		setY(0.);
	}

	@Override
	@Inline("setY($2)")
	default void setPivot(double x, double y, double z) {
		setY(y);
	}

	@Override
	default P getProjection(double x, double y, double z) {
		return getGeomFactory().newPoint(x, getY(), z);
	}

	@Override
	default double getDistanceTo(double x, double y, double z) {
		return calculatesPlaneXZPointDistance(isPositive(), getY(), x, y ,z);
	}

	@Override
	default void translate(double x, double y, double z) {
		setY(getY() + y);
	}

	@Override
	default void translate(double y) {
		setY(getY() + y);
	}

	@Override
	default double getDistanceTo(double a, double b, double c, double d) {
		return calculatesPlaneXZPlaneDistance(isPositive(), getY(), a, b, c, d);
	}

	@Override
	@SuppressWarnings("unchecked")
	default S getIntersection(double a, double b, double c, double d) {
		final S result = (S) getGeomFactory().newSegment();
		if (calculatesPlaneXZPlaneIntersection(isPositive(), getY(), a, b, c, d, result)) {
			return result;
		}
		return null;
	}

	@Override
	default P getIntersection(Segment3afp<?, ?, ?, ?, ?, ?, ?> line) {
		assert line != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		if (calculatesPlaneXZSegmentIntersection(isPositive(), getY(),
				line.getX1(), line.getY1(), line.getZ1(),
				line.getX2(), line.getY2(), line.getZ2(), point)) {
			return point;
		}
		return null;
	}

	@Override
	default PlaneClassification classifies(double x, double y, double z) {
		return classifiesPlaneXZPoint(isPositive(), getY(), x, y, z);
	}

	@Override
	default boolean intersects(double x, double y, double z) {
		return y == getY();
	}

	@Override
	default PlaneClassification classifies(double x, double y, double z, double radius) {
		return classifiesPlaneXZSphere(
				isPositive(), getY(),
				x, y, z, radius);
	}

	@Override
	default boolean intersects(double x, double y, double z, double radius) {
		final double py = getY();
		return y - radius < py && py < y + radius;
	}

	@Override
	default PlaneClassification classifies(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return classifiesPlaneXZSegment(
				isPositive(), getY(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final double py = getY();
		final double y1 = segment.getY1();
		final double y2 = segment.getY2();
		return (y1 < py && y2 > py) || (y2 < py && y1 > py);
	}

	@Override
	default PlaneClassification classifies(double lx, double ly, double lz, double ux, double uy, double uz) {
		return classifiesPlaneXZAlignedBox(isPositive(), getY(), lx, ly, lz, ux, uy, uz);
	}

}

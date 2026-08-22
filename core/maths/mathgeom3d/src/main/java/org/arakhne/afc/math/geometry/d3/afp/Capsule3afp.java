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

import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;

/** Functional interface that represented a bounding capsule.
 * A bounding capsule is a swept sphere (i.e. the volume that a sphere takes as it moves
 * along a straight line segment) containing the object. Capsules can be represented
 * by the radius of the swept sphere and the segment that the sphere is swept across).
 * It has traits similar to a cylinder, but is easier to use, because the intersection test
 * is simpler. A capsule and another object intersect if the distance between the
 * capsule's defining segment and some feature of the other object is smaller than the
 * capsule's radius. For example, two capsules intersect if the distance
 * between the capsules' segments is smaller than the sum of their radii. This holds for
 * arbitrarily rotated capsules, which is why they're more appealing than cylinders in practice.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: hjaffali$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:magicnumber"})
public interface Capsule3afp<
		IT extends Capsule3afp<?, IE, P, V, Q, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, IE, P, V, Q, B>>
	extends Box3afp<IT, IE, P, V, Q, B> {

	//	@SuppressWarnings({"checkstyle:booleanexpressioncomplexity", "checkstyle:cyclomaticcomplexity"})
	//	@Pure
	//	@Override
	//	default boolean equalsToShape(IT shape) {
	//		if (shape == null) {
	//			return false;
	//		}
	//		if (shape == this) {
	//			return true;
	//		}
	//
	//		final var ax = getX1();
	//		final var ay = getY1();
	//		final var az = getZ1();
	//		final var bx = getX2();
	//		final var by = getY2();
	//		final var bz = getZ2();
	//		final var cx = getX3();
	//		final var cy = getY3();
	//		final var cz = getZ3();
	//
	//		final var ox1 = shape.getX1();
	//		final var oy1 = shape.getY1();
	//		final var oz1 = shape.getZ1();
	//		final var ox2 = shape.getX2();
	//		final var oy2 = shape.getY2();
	//		final var oz2 = shape.getZ2();
	//		final var ox3 = shape.getX3();
	//		final var oy3 = shape.getY3();
	//		final var oz3 = shape.getZ3();
	//
	//		// Order-independent equality: same 3 vertices in any permutation.
	//		return
	//			// A -> O1
	//			ax == ox1 && ay == oy1 && az == oz1
	//				&& bx == ox2 && by == oy2 && bz == oz2
	//				&& cx == ox3 && cy == oy3 && cz == oz3
	//			|| ax == ox1 && ay == oy1 && az == oz1
	//				&& bx == ox3 && by == oy3 && bz == oz3
	//				&& cx == ox2 && cy == oy2 && cz == oz2
	//
	//			// A -> O2
	//			|| ax == ox2 && ay == oy2 && az == oz2
	//				&& bx == ox1 && by == oy1 && bz == oz1
	//				&& cx == ox3 && cy == oy3 && cz == oz3
	//			|| ax == ox2 && ay == oy2 && az == oz2
	//				&& bx == ox3 && by == oy3 && bz == oz3
	//				&& cx == ox1 && cy == oy1 && cz == oz1
	//
	//			// A -> O3
	//			|| ax == ox3 && ay == oy3 && az == oz3
	//				&& bx == ox1 && by == oy1 && bz == oz1
	//				&& cx == ox2 && cy == oy2 && cz == oz2
	//			|| ax == ox3 && ay == oy3 && az == oz3
	//				&& bx == ox2 && by == oy2 && bz == oz2
	//				&& cx == ox1 && cy == oy1 && cz == oz1;
	//	}
	//
	//	@Override
	//	default Shape3DType getType() {
	//		return Shape3DType.CAPSULE;
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean contains(double x, double y, double z) {
	//		return containsTrianglePoint(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				x, y, z,
	//				true, GeomConstants.DISTANCE_EPSILON);
	//	}
	//
	//	@Override
	//	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
	//		assert box != null : AssertMessages.notNullParameter();
	//		return box.isDegeneratedPoint() && contains(box.getMinX(), box.getMinY(), box.getMinZ());
	//	}
	//
	//	/** Change the coordinates of the three points of the triangle.
	//	 *
	//     * @param x1 x coordinate of the first point.
	//     * @param y1 y coordinate of the first point.
	//     * @param z1 z coordinate of the first point.
	//     * @param x2 x coordinate of the second point.
	//     * @param y2 y coordinate of the second point.
	//     * @param z2 z coordinate of the second point.
	//     * @param x3 x coordinate of the third point.
	//     * @param y3 y coordinate of the third point.
	//     * @param z3 z coordinate of the third point.
	//	 */
	//	@SuppressWarnings("checkstyle:parameternumber")
	//	void set(xdouble x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3);
	//
	//	/** Change the coordinates of the three points of the triangle.
	//	 *
	//    * @param p1 the first point.
	//    * @param p2 the second point.
	//    * @param p3 the third point.
	//	 */
	//	@SuppressWarnings("checkstyle:parameternumber")
	//	default void set(xPoint3D<?, ?, ?> p1, Point3D<?, ?, ?> p2, Point3D<?, ?, ?> p3) {
	//		assert p1 != null : AssertMessages.notNullParameter(0);
	//		assert p2 != null : AssertMessages.notNullParameter(1);
	//		assert p3 != null : AssertMessages.notNullParameter(2);
	//		set(
	//				p1.getX(), p1.getY(), p1.getZ(),
	//				p2.getX(), p2.getY(), p2.getZ(),
	//				p3.getX(), p3.getY(), p3.getZ());
	//	}
	//
	//	@Override
	//	default void set(IT capsule) {
	//		assert capsule != null : AssertMessages.notNullParameter();
	//		set(
	//				capsule.getX1(), capsule.getY1(), capsule.getZ1(),
	//				capsule.getX2(), capsule.getY2(), capsule.getZ2(),
	//				capsule.getX3(), capsule.getY3(), capsule.getZ3());
	//	}
	//
	//	@Override
	//	default void clear() {
	//		set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	//	}
	//
	//	@Pure
	//	@Override
	//	default String toGeogebra() {
	//		final var buffer = new StringBuilder();
	//		buffer.append("(") //$NON-NLS-1$
	//			.append(getX1()).append(",") //$NON-NLS-1$
	//			.append(getY1()).append(",") //$NON-NLS-1$
	//			.append(getZ1()).append("),(") //$NON-NLS-1$
	//			.append(getX2()).append(",") //$NON-NLS-1$
	//			.append(getY2()).append(",") //$NON-NLS-1$
	//			.append(getZ2()).append("),(") //$NON-NLS-1$
	//			.append(getX3()).append(",") //$NON-NLS-1$
	//			.append(getY3()).append(",") //$NON-NLS-1$
	//			.append(getZ3()).append(")"); //$NON-NLS-1$
	//		return buffer.toString();
	//	}
	//
	//	@Pure
	//	@Override
	//	default double getDistanceL1(Point3D<?, ?, ?> point) {
	//		assert point != null : AssertMessages.notNullParameter();
	//		final var c = getClosestPointTo(point);
	//		return c.getDistanceL1(point);
	//	}
	//
	//	@Pure
	//	@Override
	//	default double getDistanceLinf(Point3D<?, ?, ?> point) {
	//		assert point != null : AssertMessages.notNullParameter();
	//		final var c = getClosestPointTo(point);
	//		return c.getDistanceLinf(point);
	//	}
	//
	//	@Pure
	//	@Override
	//	default double getDistanceSquared(Point3D<?, ?, ?> point) {
	//		assert point != null : AssertMessages.notNullParameter();
	//		if (containsTrianglePoint(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				point.getX(), point.getY(), point.getZ(),
	//				false, 0)) {
	//			final var n = getNormal();
	//			var dist = n.getX() * getX1() + n.getY() * getY1() + n.getZ() * getZ1();
	//			dist = n.getX() * point.getX() + n.getY() * point.getY() + n.getZ() * point.getZ() - dist;
	//			return dist * dist;
	//		}
	//		final var d1 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				point.getX(), point.getY(), point.getZ());
	//		final var d2 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
	//				getX1(), getY1(), getZ1(),
	//				getX3(), getY3(), getZ3(),
	//				point.getX(), point.getY(), point.getZ());
	//		final var d3 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				point.getX(), point.getY(), point.getZ());
	//		return MathUtil.min(d1, d2, d3);
	//	}
	//
	//	@Override
	//	default void translate(double dx, double dy, double dz) {
	//		final var x1 = getX1() + dx;
	//		final var y1 = getY1() + dy;
	//		final var z1 = getZ1() + dz;
	//		final var x2 = getX2() + dx;
	//		final var y2 = getY2() + dy;
	//		final var z2 = getZ2() + dz;
	//		final var x3 = getX3() + dx;
	//		final var y3 = getY3() + dy;
	//		final var z3 = getZ3() + dz;
	//		final var pivot = getPivot();
	//		if (pivot != null) {
	//			setPivot(pivot.getX() + dx, pivot.getY() + dy, pivot.getZ() + dz);
	//		}
	//		set(x1, y1, z1, x2, y2, z2, x3, y3, z3);
	//	}
	//
	//	/** Rotate the triangle around its pivot point.
	//	 * By default, the pivot point is the first point of the triangle.
	//	 *
	//	 * @param rotation the rotation.
	//	 * @see #getPivot()
	//	 * @see #getP1()
	//	 */
	//	default void rotate(Quaternion<?, ?, ?> rotation) {
	//		assert rotation != null : AssertMessages.notNullParameter(0);
	//		TransformTools.rotateAroundOrigin(this, rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
	//	}
	//
	//	/** Rotate the segment around a given pivot point.
	//	 * The default pivot point of the segment is its first point.
	//	 *
	//	 * @param rotation the rotation.
	//	 * @param pivot the pivot point. If {@code null} the triangle's point is used.
	//	 * @see #getPivot()
	//	 * @see #getP1()
	//	 */
	//	default void rotate(Quaternion<?, ?, ?> rotation, Point3D<?, ?, ?> pivot) {
	//		assert rotation != null : AssertMessages.notNullParameter(0);
	//		if (pivot == null) {
	//			final var piv = getPivot();
	//			if (piv == null) {
	//				TransformTools.rotateAroundOrigin(
	//						this,
	//						rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
	//			} else {
	//				TransformTools.rotateAroundPivot(
	//						this,
	//						rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW(),
	//						piv.getX(), piv.getY(), piv.getZ());
	//			}
	//		} else {
	//			TransformTools.rotateAroundPivot(
	//					this,
	//					rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW(),
	//					pivot.getX(), pivot.getY(), pivot.getZ());
	//		}
	//	}
	//
	//	@Pure
	//	@Override
	//	default B toBoundingBox() {
	//		final var box = getGeomFactory().newBox();
	//		toBoundingBox(box);
	//		return box;
	//	}
	//
	//	@Pure
	//	@Override
	//	default void toBoundingBox(BoundsReceiver3D box) {
	//		assert box != null : AssertMessages.notNullParameter();
	//		final var rangex = MathUtil.getMinMax(getX1(), getX2(), getX3());
	//		final var rangey = MathUtil.getMinMax(getY1(), getY2(), getY3());
	//		final var rangez = MathUtil.getMinMax(getZ1(), getZ2(), getZ3());
	//		box.setFromCorners(
	//				rangex.getMin(), rangey.getMin(), rangez.getMin(),
	//				rangex.getMax(), rangey.getMax(), rangez.getMax());
	//	}
	//
	//	/**
	//	 * Checks if the projection of a point on the triangle's plane is inside the triangle.
	//	 *
	//	 * @param point is the the point to project on the triangle's plane.
	//	 * @return {@code true} if the projection of the point is in the triangle, otherwise {@code false}.
	//	 * @see #getPlane()
	//	 */
	//	@Pure
	//	default boolean containsProjectionOf(Point3D<?, ?, ?> point) {
	//		assert point != null : AssertMessages.notNullParameter();
	//		return containsProjectionOf(point.getX(), point.getY(), point.getZ());
	//	}
	//
	//	/**
	//	 * Checks if the projection of a point on the triangle's plane is inside the triangle.
	//	 *
	//	 * @param x x coordinate of the point to project on the triangle's plane.
	//	 * @param y y coordinate of the point to project on the triangle's plane.
	//	 * @param z z coordinate of the point to project on the triangle's plane.
	//	 * @return {@code true} if the projection of the point is in the triangle, otherwise {@code false}.
	//	 * @see #getPlane()
	//	 */
	//	@Pure
	//	default boolean containsProjectionOf(double x, double y, double z) {
	//		final var proj = getPlane().getProjection(x, y, z);
	//		if (proj == null) {
	//			return false;
	//		}
	//		return contains(proj);
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean isEmpty() {
	//		// A triangle is empty iff its 3 points are collinear (area == 0),
	//		// including special cases where 2 or 3 points are identical.
	//		final var ay = getY1();
	//		final var az = getZ1();
	//		final var by = getY2();
	//		final var bz = getZ2();
	//		final var cy = getY3();
	//		final var cz = getZ3();
	//
	//		// AB = B - A
	//		final var aby = by - ay;
	//		final var abz = bz - az;
	//		// AC = C - A
	//		final var acy = cy - ay;
	//		final var acz = cz - az;
	//		// |AB x AC|^2 (proportional to squared area)
	//		final var nx = aby * acz - abz * acy;
	//
	//		if (nx != 0.) {
	//			return false;
	//		}
	//
	//		final var ax = getX1();
	//		final var bx = getX2();
	//		final var cx = getX3();
	//
	//		// AB = B - A
	//		final var abx = bx - ax;
	//		// AC = C - A
	//		final var acx = cx - ax;
	//		// |AB x AC|^2 (proportional to squared area)
	//		final var ny = abz * acx - abx * acz;
	//
	//		if (ny != 0.) {
	//			return false;
	//		}
	//
	//		final var nz = abx * acy - aby * acx;
	//
	//		return nz == 0.;
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean isDegeneratedPoint() {
	//		return getX1() == getX2() && getX1() == getX3()
	//				&& getY1() == getY2() && getY1() == getY3()
	//				&& getZ1() == getZ2() && getZ1() == getZ3();
	//	}
	//
	//	@Pure
	//	@Override
	//	default P getClosestPointTo(Point3D<?, ?, ?> point) {
	//		assert point != null : AssertMessages.notNullParameter();
	//		final var c = getGeomFactory().newPoint();
	//		findsClosestPointToTrianglePoint(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				point.getX(), point.getY(), point.getZ(),
	//				c);
	//		return c;
	//	}
	//
	//	@Pure
	//	@Override
	//	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
	//		assert sphere != null : AssertMessages.notNullParameter();
	//		final var c = getGeomFactory().newPoint();
	//		findsClosestPointToTrianglePoint(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				sphere.getX(), sphere.getY(), sphere.getZ(),
	//				c);
	//		return c;
	//	}
	//
	//	@Pure
	//	@Override
	//	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
	//		assert box != null : AssertMessages.notNullParameter();
	//		final var point = getGeomFactory().newPoint();
	//		findsClosestPointToTriangleAlignedBox(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				box.getMinX(), box.getMinY(), box.getMinZ(),
	//				box.getMaxX(), box.getMaxY(), box.getMaxZ(),
	//				GeomConstants.DISTANCE_EPSILON,
	//				point, null);
	//		return point;
	//	}
	//
	//	@Pure
	//	@Override
	//	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
	//		assert segment != null : AssertMessages.notNullParameter();
	//		final var point = getGeomFactory().newPoint();
	//		findsClosestPointToTriangleSegment(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				segment.getX1(), segment.getY1(), segment.getZ1(),
	//				segment.getX2(), segment.getY2(), segment.getZ2(),
	//				GeomConstants.DISTANCE_EPSILON,
	//				point, null);
	//		return point;
	//	}
	//
	//	@Override
	//	default P getClosestPointTo(Capsule3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
	//		assert triangle != null : AssertMessages.notNullParameter();
	//		final var point = getGeomFactory().newPoint();
	//		findsClosestPointToTriangleTriangle(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
	//				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
	//				triangle.getX3(), triangle.getY3(), triangle.getZ3(),
	//				GeomConstants.DISTANCE_EPSILON,
	//				point, null);
	//		return point;
	//	}
	//
	//	@Pure
	//	@Override
	//	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
	//		assert path != null : AssertMessages.notNullParameter();
	//		final var point = getGeomFactory().newPoint();
	//		if (findsClosestPointToTrianglePathIterator(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				path.getPathIterator(),
	//				GeomConstants.DISTANCE_EPSILON,
	//				point, null)) {
	//			return point;
	//		}
	//		return null;
	//	}
	//
	//	@Pure
	//	@Override
	//	default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
	//		assert multishape != null : AssertMessages.notNullParameter();
	//		final var pointOnShape = multishape.getClosestPointTo(this);
	//		final var point = getGeomFactory().newPoint();
	//		findsClosestPointToTrianglePoint(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				pointOnShape.getX(), pointOnShape.getY(), pointOnShape.getZ(),
	//				point);
	//		return point;
	//	}
	//
	//	@Pure
	//	@Override
	//	default P getFarthestPointTo(Point3D<?, ?, ?> point) {
	//		assert point != null : AssertMessages.notNullParameter();
	//		final var px = point.getX();
	//		final var py = point.getY();
	//		final var pz = point.getZ();
	//		final var d1 = Point3D.getDistanceSquaredPointPoint(getX1(), getY1(), getZ1(), px, py, pz);
	//		final var d2 = Point3D.getDistanceSquaredPointPoint(getX2(), getY2(), getZ2(), px, py, pz);
	//		final var d3 = Point3D.getDistanceSquaredPointPoint(getX3(), getY3(), getZ3(), px, py, pz);
	//		if (d1 >= d2) {
	//			if (d3 >= d1) {
	//				return getP3();
	//			}
	//			return getP1();
	//		} else if (d3 >= d2) {
	//			return getP3();
	//		}
	//		return getP2();
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
	//		assert sphere != null : AssertMessages.notNullParameter();
	//		return intersectsTriangleSphere(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				sphere.getX(), sphere.getY(), sphere.getZ(),
	//				sphere.getRadius());
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
	//		assert prism != null : AssertMessages.notNullParameter();
	//		return intersectsTriangleAlignedBox(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				prism.getMinX(), prism.getMinY(), prism.getMinZ(),
	//				prism.getMaxX(), prism.getMaxY(), prism.getMaxZ(),
	//				GeomConstants.DISTANCE_EPSILON);
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
	//		assert segment != null : AssertMessages.notNullParameter();
	//		return intersectsTriangleSegment(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				segment.getX1(), segment.getY1(), segment.getZ1(),
	//				segment.getX2(), segment.getY2(), segment.getZ2(),
	//				GeomConstants.DISTANCE_EPSILON);
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean intersects(Capsule3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
	//		assert triangle != null :  AssertMessages.notNullParameter();
	//		return intersectsTriangleTriangle(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
	//				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
	//				triangle.getX3(), triangle.getY3(), triangle.getZ3(),
	//				GeomConstants.DISTANCE_EPSILON);
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> path) {
	//		assert path != null : AssertMessages.notNullParameter();
	//		return intersectsTrianglePathIterator(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				path.getPathIterator(),
	//				GeomConstants.DISTANCE_EPSILON);
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean intersects(PathIterator3afp<?> iterator) {
	//		assert iterator != null : AssertMessages.notNullParameter();
	//		return intersectsTrianglePathIterator(
	//				getX1(), getY1(), getZ1(),
	//				getX2(), getY2(), getZ2(),
	//				getX3(), getY3(), getZ3(),
	//				iterator,
	//				GeomConstants.DISTANCE_EPSILON);
	//	}
	//
	//	@Pure
	//	@Override
	//	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
	//		assert multishape != null : AssertMessages.notNullParameter();
	//		return multishape.intersects(this);
	//	}

}

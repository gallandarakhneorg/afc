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

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.d3.BoundsReceiver3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

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
 * @param <ST> is the general type of all the shapes.
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
			ST extends Shape3afp<?, IE, P, V, Q, B>,
			IT extends Capsule3afp<?, ?, IE, P, V, Q, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>,
			B extends AlignedBox3afp<?, IE, P, V, Q, B>>
		extends TransformableShape3afp<ST, IT, IE, P, V, Q, B> {

	@Override
	default Shape3DType getType() {
		return Shape3DType.CAPSULE;
	}

	@Pure
	@Override
	default String toGeogebra() {
		final var buffer = new StringBuilder();
		buffer.append("(") //$NON-NLS-1$
			.append(getX1()).append(",") //$NON-NLS-1$
			.append(getY1()).append(",") //$NON-NLS-1$
			.append(getZ1()).append("),(") //$NON-NLS-1$
			.append(getX2()).append(",") //$NON-NLS-1$
			.append(getY2()).append(",") //$NON-NLS-1$
			.append(getZ2()).append("),") //$NON-NLS-1$
			.append(getRadius());
		return buffer.toString();
	}

	@Pure
	@Override
	default boolean isEmpty() {
		return getX1() == getX2() && getY1() == getY2() && getZ1() == getZ2() && getRadius() == 0.;
	}

	@Pure
	@Override
	default boolean isDegeneratedPoint() {
		return isEmpty();
	}

	/** Change the coordinates of the two points of the capsule, and its radius.
	 *
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     * @param radius the radius of the capsule.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	void set(double x1, double y1, double z1, double x2, double y2, double z2, double radius);

	/** Change the coordinates of the two points of the capsule, and its radius.
	 *
	 * @param p1 the first point.
	 * @param p2 the second point.
	 * @param radius the radius.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	default void set(Point3D<?, ?, ?> p1, Point3D<?, ?, ?> p2, double radius) {
		assert p1 != null : AssertMessages.notNullParameter(0);
		assert p2 != null : AssertMessages.notNullParameter(1);
		assert radius >= 0. : AssertMessages.positiveOrZeroParameter(2);
		set(
				p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(),
				radius);
	}

	@Override
	default void set(IT capsule) {
		assert capsule != null : AssertMessages.notNullParameter();
		set(
				capsule.getX1(), capsule.getY1(), capsule.getZ1(),
				capsule.getX2(), capsule.getY2(), capsule.getZ2(),
				capsule.getRadius());
	}

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

	/** Replies the radius.
	 *
	 * @return the radius.
	 */
	@Pure
	double getRadius();

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

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 * @param z z coordinate of the first point.
	 */
	default void setP1(double x, double y, double z) {
		set(x, y, z, getX2(), getY2(), getZ2(), getRadius());
	}

	/** Change the first point.
	 *
	 * @param point the first point.
	 */
	default void setP1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		set(point.getX(), point.getY(), point.getZ(), getX2(), getY2(), getZ2(), getRadius());
	}

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 * @param z z coordinate of the first point.
	 */
	default void setP2(double x, double y, double z) {
		set(getX1(), getY1(), getZ1(), x, y, z, getRadius());
	}

	/** Change the second point.
	 *
	 * @param point the second point.
	 */
	default void setP2(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		set(getX1(), getY1(), getZ1(), point.getX(), point.getY(), point.getZ(), getRadius());
	}

	/** Set the radius.
	 *
	 * @param radius is the radius.
	 */
	void setRadius(double radius);

	/** Replies the length of the segment between the two medial points of the capsule.
	 *
	 * @return the length from the first medial point to the second medial point.
	 */
	@Pure
	default double getInnerLength() {
		return Point3D.getDistancePointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Replies the squared length of the segment between the two medial points of the capsule.
	 *
	 * @return the squared length from the first medial point to the second medial point.
	 */
	@Pure
	default double getInnerLengthSquared() {
		return Point3D.getDistanceSquaredPointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Replies the length of the capsule along the line colinear to the two medial points.
	 * It is equivalent to {@link #getInternalLength()} plus the two radii.
	 *
	 * @return the length of the capsule.
	 */
	@Pure
	default double getOuterLength() {
		return Math.fma(2., getRadius(), getInnerLength());
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0, 0, 0, 0);
	}

	@Pure
	@Override
	default void toBoundingBox(BoundsReceiver3D box) {
		assert box != null : AssertMessages.notNullParameter();
		final var x1 = getX1();
		final var y1 = getY1();
		final var z1 = getZ1();
		final var x2 = getX2();
		final var y2 = getY2();
		final var z2 = getZ2();
		final var radius = getRadius();
		final double minx;
		final double miny;
		final double minz;
		final double maxx;
		final double maxy;
		final double maxz;
		if (x1 <= x2) {
			minx = x1;
			maxx = x2;
		} else {
			minx = x2;
			maxx = x1;
		}
		if (y1 <= y2) {
			miny = y1;
			maxy = y2;
		} else {
			miny = y2;
			maxy = y1;
		}
		if (z1 <= z2) {
			minz = z1;
			maxz = z2;
		} else {
			minz = z2;
			maxz = z1;
		}
		box.setFromCorners(minx - radius, miny - radius, minz - radius,
				maxx + radius, maxy + radius, maxz + radius);
	}

	@SuppressWarnings({"checkstyle:booleanexpressioncomplexity", "checkstyle:cyclomaticcomplexity"})
	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}

		final var x1 = getX1();
		final var y1 = getY1();
		final var z1 = getZ1();
		final var x2 = getX2();
		final var y2 = getY2();
		final var z2 = getZ2();
		final var r = getRadius();

		final var ox1 = shape.getX1();
		final var oy1 = shape.getY1();
		final var oz1 = shape.getZ1();
		final var ox2 = shape.getX2();
		final var oy2 = shape.getY2();
		final var oz2 = shape.getZ2();
		final var or = shape.getRadius();

		// Order-independent equality: same 2 vertices in any permutation.
		return r == or
				&& (x1 == ox1 && y1 == oy1 && z1 == oz1
				&& x2 == ox2 && y2 == oy2 && z2 == oz2
				|| x1 == ox2 && y1 == oy2 && z1 == oz2
				&& x2 == ox1 && y2 == oy1 && z2 == oz1);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		final var r = getRadius();
		return Segment3afp.calculatesDistanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				x, y, z) <= r * r;
	}

	/**
	 * Replies if the given axis-aligned box is fully contained in this capsule.
	 *
	 * <p>Algorithm: test the 8 corners of the box with {@link #contains(double, double, double)}.
	 * This is exact because a capsule is convex, and an axis-aligned box is the convex
	 * hull of its 8 corners; if all corners are inside a convex set, then the whole box
	 * is inside.
	 *
	 * <p>References:<ul>
	 *   <li>R. T. Rockafellar, <em>Convex Analysis</em>, Princeton University Press, 1970
	 *       (convex-set / convex-combination property).</li>
	 *   <li>S. Boyd and L. Vandenberghe, <em>Convex Optimization</em>, Cambridge University Press, 2004
	 *       (basic convex-set properties).</li>
	 * </ul>
	 *
	 * @param box the box to test.
	 * @return {@code true} if the box is fully inside this capsule.
	 */
	@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
	@Pure
	@Override
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		final var minX = box.getMinX();
		final var minY = box.getMinY();
		final var minZ = box.getMinZ();
		if (box.isEmpty()) {
			return contains(minX, minY, minZ);
		}
		final var maxX = box.getMaxX();
		final var maxY = box.getMaxY();
		final var maxZ = box.getMaxZ();
		return contains(minX, minY, minZ)
			&& contains(minX, minY, maxZ)
			&& contains(minX, maxY, minZ)
			&& contains(minX, maxY, maxZ)
			&& contains(maxX, minY, minZ)
			&& contains(maxX, minY, maxZ)
			&& contains(maxX, maxY, minZ)
			&& contains(maxX, maxY, maxZ);
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		final var x1 = getX1() + dx;
		final var y1 = getY1() + dy;
		final var z1 = getZ1() + dz;
		final var x2 = getX2() + dx;
		final var y2 = getY2() + dy;
		final var z2 = getZ2() + dz;
		final var r = getRadius();
		set(x1, y1, z1, x2, y2, z2, r);
	}

	@Override
	default void transform(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		if (!transform.isIdentity()) {
			final var p1 = new InnerComputationPoint3D(getX1(), getY1(), getZ1());
			transform.transform(p1);
			final var p2 = new InnerComputationPoint3D(getX2(), getY2(), getZ2());
			transform.transform(p2);
			final var v = new InnerComputationVector3D(getRadius(), 0., 0.);
			transform.transform(v);
			set(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), v.getLength());
		}
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
    default ST createTransformedShape(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final var shape = clone();
		shape.transform(transform);
		return (ST) shape;
    }

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var px = point.getX();
		final var py = point.getY();
		final var pz = point.getZ();
		final var pts = getGeomFactory().newPoint();
		Segment3afp.findsFarthestPointToPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				px, py, pz,
				pts);
		final var r = getRadius();
		var vx = pts.getX() - px;
		var vy = pts.getY() - py;
		var vz = pts.getZ() - pz;
		final var dist = Math.sqrt(Vector3D.dotProduct(vx, vy, vz, vx, vy, vz));
		final var ndist = dist + r;
		vx = (vx * ndist) / dist;
		vy = (vy * ndist) / dist;
		vz = (vz * ndist) / dist;
		pts.set(px + vx, py + vy, pz + vz);
		return pts;
	}

	/** Finds the closest point on the capsule to the given point.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param px x coordinate of the point.
	 * @param py y coordinate of the point.
	 * @param pz z coordinate of the point.
	 * @param pointOnCapsule the closest point on the capsule.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static void findsClosestPointToCapsulePoint(double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double px, double py, double pz,
			Point3D<?, ?, ?> pointOnCapsule) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert pointOnCapsule != null : AssertMessages.notNullParameter(10);
		final var factor = Segment3afp.findsProjectedPointOnLine(
				px, py, pz,
				cx1, cy1, cz1,
				cx2, cy2, cz2);
		final double clx;
		final double cly;
		final double clz;
		if (factor <= 0.) {
			clx = cx1;
			cly = cy1;
			clz = cz1;
		} else if (factor >= 1.) {
			clx = cx2;
			cly = cy2;
			clz = cz2;
		} else {
			clx = Math.fma(factor, cx2 - cx1, cx1);
			cly = Math.fma(factor, cy2 - cy1, cy1);
			clz = Math.fma(factor, cz2 - cz1, cz1);
		}
		final var vx = px - clx;
		final var vy = py - cly;
		final var vz = pz - clz;
		var length = Vector3D.dotProduct(vx, vy, vz, vx, vy, vz);
		if (length > 0.) {
			length = Math.sqrt(length);
			if (length >= cradius) {
				pointOnCapsule.set(
					(cradius * vx) / length + clx,
					(cradius * vy) / length + cly,
					(cradius * vz) / length + clz);
			} else {
				pointOnCapsule.set(px, py, pz);
			}
		} else {
			pointOnCapsule.set(clx, cly, clz);
		}
	}

	/** Finds the closest point on the capsule to the given sphere.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param sx x coordinate of the sphere center.
	 * @param sy y coordinate of the sphere center.
	 * @param sz z coordinate of the sphere center.
	 * @param sradius the radius of the sphere.
	 * @param pointOnCapsule the closest point on the capsule. It could be {@code null}.
	 * @param pointOnSphere the closest point on the sphere. It could be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static void findsClosestPointToCapsuleSphere(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double sx, double sy, double sz, double sradius,
			Point3D<?, ?, ?> pointOnCapsule, Point3D<?, ?, ?> pointOnSphere) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert sradius >= 0. : AssertMessages.positiveOrZeroParameter(10);
		assert pointOnCapsule != null || pointOnSphere != null : AssertMessages.notNullParameter(11);
		final var factor = Segment3afp.findsProjectedPointOnLine(
				sx, sy, sz,
				cx1, cy1, cz1,
				cx2, cy2, cz2);
		final double clx;
		final double cly;
		final double clz;
		if (factor <= 0.) {
			clx = cx1;
			cly = cy1;
			clz = cz1;
		} else if (factor >= 1.) {
			clx = cx2;
			cly = cy2;
			clz = cz2;
		} else {
			clx = Math.fma(factor, cx2 - cx1, cx1);
			cly = Math.fma(factor, cy2 - cy1, cy1);
			clz = Math.fma(factor, cz2 - cz1, cz1);
		}
		final var vx = sx - clx;
		final var vy = sy - cly;
		final var vz = sz - clz;
		var length = Vector3D.dotProduct(vx, vy, vz, vx, vy, vz);
		if (length > 0.) {
			length = Math.sqrt(length);
			if (length >= cradius + sradius) {
				final double nx = vx / length;
				final double ny = vy / length;
				final double nz = vz / length;
				if (pointOnCapsule != null) {
					pointOnCapsule.set(
						Math.fma(cradius, nx, clx),
						Math.fma(cradius, ny, cly),
						Math.fma(cradius, nz, clz));
				}
				if (pointOnSphere != null) {
					pointOnSphere.set(
							Math.fma(-nx, sradius, sx),
							Math.fma(-ny, sradius, sy),
							Math.fma(-nz, sradius, sz));
				}
			} else {
				if (pointOnCapsule != null) {
					pointOnCapsule.set(sx, sy, sz);
				}
				if (pointOnSphere != null) {
					pointOnSphere.set(sx, sy, sz);
				}
			}
		} else {
			if (pointOnCapsule != null) {
				pointOnCapsule.set(clx, cly, clz);
			}
			if (pointOnSphere != null) {
				pointOnSphere.set(clx, cly, clz);
			}
		}
	}

	/** Finds the closest point on the capsule to the given segment.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first point of the segment.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second point of the segment.
	 * @param pointOnCapsule the closest point on the capsule. It could be {@code null}.
	 * @param pointOnSegment the closest point on the segment. It could be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static void findsClosestPointToCapsuleSegment(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			Point3D<?, ?, ?> pointOnCapsule, Point3D<?, ?, ?> pointOnSegment) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert pointOnCapsule != null || pointOnSegment != null : AssertMessages.notNullParameter(13);
		final var onCapsuleSegment = new InnerComputationPoint3D();
		final var onSegment = new InnerComputationPoint3D();
		Segment3afp.findsClosestPointToSegment(
				cx1, cy1, cz1, cx2, cy2, cz2,
				sx1, sy1, sz1, sx2, sy2, sz2,
				onCapsuleSegment, onSegment);
		final var sqDist = onCapsuleSegment.getDistanceSquared(onSegment);
		final var sqRadius = cradius * cradius;
		if (sqDist > sqRadius) {
			if (pointOnCapsule != null) {
				final var dist = Math.sqrt(sqDist);
				final var px = onCapsuleSegment.getX();
				final var py = onCapsuleSegment.getY();
				final var pz = onCapsuleSegment.getZ();
				final var vx = (cradius * (onSegment.getX() - px)) / dist;
				final var vy = (cradius * (onSegment.getY() - py)) / dist;
				final var vz = (cradius * (onSegment.getZ() - pz)) / dist;
				pointOnCapsule.set(px + vx, py + vy, pz + vz);
			}
			if (pointOnSegment != null) {
				pointOnSegment.set(onSegment);
			}
		} else {
			if (pointOnCapsule != null) {
				pointOnCapsule.set(onSegment);
			}
			if (pointOnSegment != null) {
				pointOnSegment.set(onSegment);
			}
		}
	}

	/** Finds the closest point on the capsule to the given triangle.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param epsilon the approximation factor to be used.
	 * @param pointOnCapsule the closest point on the capsule. It could be {@code null}.
	 * @param pointOnTriangle the closest point on the triangle. It could be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static void findsClosestPointToCapsuleTriangle(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double epsilon,
			Point3D<?, ?, ?> pointOnCapsule, Point3D<?, ?, ?> pointOnTriangle) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert epsilon >= 0. : AssertMessages.positiveOrZeroParameter(16);
		assert pointOnCapsule != null || pointOnTriangle != null : AssertMessages.notNullParameter(17);
		final var onCapsuleSegment = new InnerComputationPoint3D();
		final var onTriangle = new InnerComputationPoint3D();
		Triangle3afp.findsClosestPointToTriangleSegment(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				cx1, cy1, cz1,
				cx2, cy2, cz2,
				epsilon,
				onTriangle, onCapsuleSegment);
		final var sqDist = onCapsuleSegment.getDistanceSquared(onTriangle);
		final var sqRadius = cradius * cradius;
		if (sqDist > sqRadius) {
			if (pointOnCapsule != null) {
				final var dist = Math.sqrt(sqDist);
				final var px = onCapsuleSegment.getX();
				final var py = onCapsuleSegment.getY();
				final var pz = onCapsuleSegment.getZ();
				final var vx = (cradius * (onTriangle.getX() - px)) / dist;
				final var vy = (cradius * (onTriangle.getY() - py)) / dist;
				final var vz = (cradius * (onTriangle.getZ() - pz)) / dist;
				pointOnCapsule.set(px + vx, py + vy, pz + vz);
			}
			if (pointOnTriangle != null) {
				pointOnTriangle.set(onTriangle);
			}
		} else {
			if (pointOnCapsule != null) {
				pointOnCapsule.set(onTriangle);
			}
			if (pointOnTriangle != null) {
				pointOnTriangle.set(onTriangle);
			}
		}
	}

	/** Finds the closest point on the capsule to the given aligned box.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param bx1 x coordinate of the minimum corner of the aligned box.
	 * @param by1 y coordinate of the minimum corner of the aligned box.
	 * @param bz1 z coordinate of the minimum corner of the aligned box.
	 * @param bx2 x coordinate of the maximum corner of the aligned box.
	 * @param by2 y coordinate of the maximum corner of the aligned box.
	 * @param bz2 z coordinate of the maximum corner of the aligned box.
	 * @param pointOnCapsule the closest point on the capsule. It could be {@code null}.
	 * @param pointOnBox the closest point on the box. It could be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static void findsClosestPointToCapsuleAlignedBox(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double bx1, double by1, double bz1,
			double bx2, double by2, double bz2,
			Point3D<?, ?, ?> pointOnCapsule, Point3D<?, ?, ?> pointOnBox) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert bx1 <= bx2 : AssertMessages.lowerEqualParameter(7, Double.valueOf(bx1), Double.valueOf(bx2));
		assert by1 <= by2 : AssertMessages.lowerEqualParameter(8, Double.valueOf(by1), Double.valueOf(by2));
		assert bz1 <= bz2 : AssertMessages.lowerEqualParameter(9, Double.valueOf(bz1), Double.valueOf(bz2));
		assert pointOnCapsule != null || pointOnBox != null : AssertMessages.notNullParameter(13);
		final var onCapsuleSegment = new InnerComputationPoint3D();
		final var onBox = new InnerComputationPoint3D();
		AlignedBox3afp.findsClosestPointToAlignedBoxSegment(
				bx1, by1, bz1,
				bx2, by2, bz2,
				cx1, cy1, cz1,
				cx2, cy2, cz2,
				onBox, onCapsuleSegment);
		final var sqDist = onCapsuleSegment.getDistanceSquared(onBox);
		final var sqRadius = cradius * cradius;
		if (sqDist > sqRadius) {
			if (pointOnCapsule != null) {
				final var dist = Math.sqrt(sqDist);
				final var px = onCapsuleSegment.getX();
				final var py = onCapsuleSegment.getY();
				final var pz = onCapsuleSegment.getZ();
				final var vx = (cradius * (onBox.getX() - px)) / dist;
				final var vy = (cradius * (onBox.getY() - py)) / dist;
				final var vz = (cradius * (onBox.getZ() - pz)) / dist;
				pointOnCapsule.set(px + vx, py + vy, pz + vz);
			}
			if (pointOnBox != null) {
				pointOnBox.set(onBox);
			}
		} else {
			if (pointOnCapsule != null) {
				pointOnCapsule.set(onBox);
			}
			if (pointOnBox != null) {
				pointOnBox.set(onBox);
			}
		}
	}

	/** Finds the closest point on the capsule to the given path.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param iterator iterator on the path elements.
	 * @param pointOnCapsule the closest point on the capsule. It could be {@code null}.
	 * @param pointOnPath the closest point on the path. It could be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static void findsClosestPointToCapsulePathIterator(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			PathIterator3afp<?> iterator,
			Point3D<?, ?, ?> pointOnCapsule, Point3D<?, ?, ?> pointOnPath) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert iterator != null : AssertMessages.notNullParameter(7);
		assert pointOnCapsule != null || pointOnPath != null : AssertMessages.notNullParameter(8);
		final var onCapsuleSegment = new InnerComputationPoint3D();
		final var onPath = new InnerComputationPoint3D();
		Path3afp.findsClosestPointToSegment(iterator,
				cx1, cy1, cz1,
				cx2, cy2, cz2,
				onPath, onCapsuleSegment);
		final var sqDist = onCapsuleSegment.getDistanceSquared(onPath);
		final var sqRadius = cradius * cradius;
		if (sqDist > sqRadius) {
			if (pointOnCapsule != null) {
				final var dist = Math.sqrt(sqDist);
				final var px = onCapsuleSegment.getX();
				final var py = onCapsuleSegment.getY();
				final var pz = onCapsuleSegment.getZ();
				final var vx = (cradius * (onPath.getX() - px)) / dist;
				final var vy = (cradius * (onPath.getY() - py)) / dist;
				final var vz = (cradius * (onPath.getZ() - pz)) / dist;
				pointOnCapsule.set(px + vx, py + vy, pz + vz);
			}
			if (pointOnPath != null) {
				pointOnPath.set(onPath);
			}
		} else {
			if (pointOnCapsule != null) {
				pointOnCapsule.set(onPath);
			}
			if (pointOnPath != null) {
				pointOnPath.set(onPath);
			}
		}
	}

	/** Finds the closest point on the first capsule to the given second capsule.
	 *
	 * @param ax1 x coordinate of the first point of the first capsule segment.
	 * @param ay1 y coordinate of the first point of the first capsule segment.
	 * @param az1 y coordinate of the first point of the first capsule segment.
	 * @param ax2 x coordinate of the second point of the first capsule segment.
	 * @param ay2 y coordinate of the second point of the first capsule segment.
	 * @param az2 y coordinate of the second point of the first capsule segment.
	 * @param aradius the radius of the first capsule.
	 * @param bx1 x coordinate of the first point of the second capsule segment.
	 * @param by1 y coordinate of the first point of the second capsule segment.
	 * @param bz1 y coordinate of the first point of the second capsule segment.
	 * @param bx2 x coordinate of the second point of the second capsule segment.
	 * @param by2 y coordinate of the second point of the second capsule segment.
	 * @param bz2 y coordinate of the second point of the second capsule segment.
	 * @param bradius the radius of the second capsule.
	 * @param pointOnCapsule1 the closest point on the first capsule. It could be {@code null}.
	 * @param pointOnCapsule2 the closest point on the second capsule. It could be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static void findsClosestPointToCapsuleCapsule(
			double ax1, double ay1, double az1,
			double ax2, double ay2, double az2, double aradius,
			double bx1, double by1, double bz1,
			double bx2, double by2, double bz2, double bradius,
			Point3D<?, ?, ?> pointOnCapsule1, Point3D<?, ?, ?> pointOnCapsule2) {
		assert aradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert bradius >= 0. : AssertMessages.positiveOrZeroParameter(13);
		assert pointOnCapsule1 != null || pointOnCapsule2 != null : AssertMessages.notNullParameter(14);
		final var onCapsuleSegment1 = new InnerComputationPoint3D();
		final var onCapsuleSegment2 = new InnerComputationPoint3D();
		Segment3afp.findsClosestPointToSegment(
				ax1, ay1, az1, ax2, ay2, az2,
				bx1, by1, bz1, bx2, by2, bz2,
				onCapsuleSegment1, onCapsuleSegment2);
		final var sqDist = onCapsuleSegment1.getDistanceSquared(onCapsuleSegment2);
		final var radii = aradius + bradius;
		final var sqRadii = radii * radii;
		if (sqDist > sqRadii) {
			final var dist = Math.sqrt(sqDist);
			final var px1 = onCapsuleSegment1.getX();
			final var py1 = onCapsuleSegment1.getY();
			final var pz1 = onCapsuleSegment1.getZ();
			final var px2 = onCapsuleSegment2.getX();
			final var py2 = onCapsuleSegment2.getY();
			final var pz2 = onCapsuleSegment2.getZ();
			final var vx = (px2 - px1) / dist;
			final var vy = (py2 - py1) / dist;
			final var vz = (pz2 - pz1) / dist;
			if (pointOnCapsule1 != null) {
				pointOnCapsule1.set(
						Math.fma(vx, aradius, px1),
						Math.fma(vy, aradius, py1),
						Math.fma(vz, aradius, pz1));
			}
			if (pointOnCapsule2 != null) {
				pointOnCapsule2.set(
						Math.fma(-vx, bradius, px2),
						Math.fma(-vy, bradius, py2),
						Math.fma(-vz, bradius, pz2));
			}
		} else {
			if (pointOnCapsule1 != null) {
				pointOnCapsule1.set(onCapsuleSegment2);
			}
			if (pointOnCapsule2 != null) {
				pointOnCapsule2.set(onCapsuleSegment2);
			}
		}
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var pointOnCapsule = getGeomFactory().newPoint();
		findsClosestPointToCapsulePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				point.getX(), point.getY(), point.getZ(),
				pointOnCapsule);
		return pointOnCapsule;
	}

	@Pure
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		final var pointOnCapsule = getGeomFactory().newPoint();
		findsClosestPointToCapsuleSphere(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius(),
				pointOnCapsule, null);
		return pointOnCapsule;
	}

	@Pure
	@Override
	default P getClosestPointTo(Capsule3afp<?, ?, ?, ?, ?, ?, ?> capsule) {
		assert capsule != null : AssertMessages.notNullParameter();
		final var pointOnCapsule = getGeomFactory().newPoint();
		findsClosestPointToCapsuleCapsule(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				capsule.getX1(), capsule.getY1(), capsule.getZ1(),
				capsule.getX2(), capsule.getY2(), capsule.getZ2(),
				capsule.getRadius(),
				pointOnCapsule, null);
		return pointOnCapsule;
	}

	@Pure
	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		final var pointOnCapsule = getGeomFactory().newPoint();
		findsClosestPointToCapsuleAlignedBox(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				prism.getMinX(), prism.getMinY(), prism.getMinZ(),
				prism.getMaxX(), prism.getMaxY(), prism.getMaxZ(),
				pointOnCapsule, null);
		return pointOnCapsule;
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final var pointOnCapsule = getGeomFactory().newPoint();
		findsClosestPointToCapsuleSegment(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2(),
				pointOnCapsule, null);
		return pointOnCapsule;
	}

	@Pure
	@Override
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final var pointOnCapsule = getGeomFactory().newPoint();
		findsClosestPointToCapsulePathIterator(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				path.getPathIterator(),
				pointOnCapsule, null);
		return pointOnCapsule;
	}

	@Pure
	@Override
	default P getClosestPointTo(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		final var pointOnCapsule = getGeomFactory().newPoint();
		findsClosestPointToCapsuleTriangle(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
				triangle.getX3(), triangle.getY3(), triangle.getZ3(),
				GeomConstants.DISTANCE_EPSILON,
				pointOnCapsule, null);
		return pointOnCapsule;
	}

	/** Replies if the given capsule is intersecting the given segment.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first point of the segment.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second point of the segment.
	 * @param epsilon the approximation distance to consider for intersection.
	 * @return {@code true} if the two shapes are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static boolean intersectsCapsuleSegment(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			double epsilon) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert epsilon >= 0. : AssertMessages.positiveOrZeroParameter(13);
		final var sqDist = Segment3afp.calculatesDistanceSquaredSegmentSegment(
				cx1, cy1, cz1, cx2, cy2, cz2,
				sx1, sy1, sz1, sx2, sy2, sz2);
		final var r = cradius + epsilon;
		return sqDist <= r * r;
	}

	/** Replies if the given first capsule is intersecting the given second capsule.
	 *
	 * @param ax1 x coordinate of the first point of the first capsule segment.
	 * @param ay1 y coordinate of the first point of the first capsule segment.
	 * @param az1 y coordinate of the first point of the first capsule segment.
	 * @param ax2 x coordinate of the second point of the first capsule segment.
	 * @param ay2 y coordinate of the second point of the first capsule segment.
	 * @param az2 y coordinate of the second point of the first capsule segment.
	 * @param aradius the radius of the first capsule.
	 * @param bx1 x coordinate of the first point of the second capsule segment.
	 * @param by1 y coordinate of the first point of the second capsule segment.
	 * @param bz1 y coordinate of the first point of the second capsule segment.
	 * @param bx2 x coordinate of the second point of the second capsule segment.
	 * @param by2 y coordinate of the second point of the second capsule segment.
	 * @param bz2 y coordinate of the second point of the second capsule segment.
	 * @param bradius the radius of the second capsule.
	 * @param epsilon the approximation distance to consider for intersection.
	 * @return {@code true} if the two shapes are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static boolean intersectsCapsuleCapsule(
			double ax1, double ay1, double az1,
			double ax2, double ay2, double az2, double aradius,
			double bx1, double by1, double bz1,
			double bx2, double by2, double bz2, double bradius,
			double epsilon) {
		assert aradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert bradius >= 0. : AssertMessages.positiveOrZeroParameter(13);
		assert epsilon >= 0. : AssertMessages.positiveOrZeroParameter(14);
		final var sqDist = Segment3afp.calculatesDistanceSquaredSegmentSegment(
				ax1, ay1, az1, ax2, ay2, az2,
				bx1, by1, bz1, bx2, by2, bz2);
		final var r = aradius + bradius + epsilon;
		return sqDist <= r * r;
	}

	/** Replies if the given capsule is intersecting the given aligned box.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param bx1 x coordinate of the minimum corner of the aligned box.
	 * @param by1 y coordinate of the minimum corner of the aligned box.
	 * @param bz1 z coordinate of the minimum corner of the aligned box.
	 * @param bx2 x coordinate of the maximum corner of the aligned box.
	 * @param by2 y coordinate of the maximum corner of the aligned box.
	 * @param bz2 z coordinate of the maximum corner of the aligned box.
	 * @param epsilon the approximation distance to consider for intersection.
	 * @return {@code true} if the two shapes are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static boolean intersectsCapsuleAlignedBox(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double bx1, double by1, double bz1,
			double bx2, double by2, double bz2,
			double epsilon) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert epsilon >= 0. : AssertMessages.positiveOrZeroParameter(13);
		final var pointOnCapsule = new InnerComputationPoint3D();
		final var pointOnBox = new InnerComputationPoint3D();
		AlignedBox3afp.findsClosestPointToAlignedBoxSegment(
				bx1, by1, bz1, bx2, by2, bz2,
				cx1, cy1, cz1, cx2, cy2, cz2,
				pointOnBox, pointOnCapsule);
		final var sqDist = pointOnBox.getDistanceSquared(pointOnCapsule);
		final var r = cradius + epsilon;
		return sqDist <= r * r;
	}

	/** Replies if the given capsule is intersecting the given path.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param iterator iterator on the path elements.
	 * @param epsilon the approximation distance to consider for intersection.
	 * @return {@code true} if the two shapes are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static boolean intersectsCapsulePathIterator(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			PathIterator3afp<?> iterator,
			double epsilon) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert epsilon >= 0. : AssertMessages.positiveOrZeroParameter(13);
		final var sqDist = Path3afp.calculatesDistanceSquaredPathIteratorSegment(iterator,
				cx1, cy1, cz1, cx2, cy2, cz2);
		final var r = cradius + epsilon;
		return sqDist <= r * r;
	}

	/** Replies if the given capsule is intersecting the given sphere.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param sx x coordinate of the sphere center.
	 * @param sy y coordinate of the sphere center.
	 * @param sz z coordinate of the sphere center.
	 * @param sradius the radius of the sphere.
	 * @param epsilon the approximation distance to consider for intersection.
	 * @return {@code true} if the two shapes are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static boolean intersectsCapsuleSphere(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double sx, double sy, double sz, double sradius,
			double epsilon) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert sradius >= 0. : AssertMessages.positiveOrZeroParameter(10);
		assert epsilon >= 0. : AssertMessages.positiveOrZeroParameter(11);
		final var sqDist = Segment3afp.calculatesDistanceSquaredSegmentPoint(
				cx1, cy1, cz1, cx2, cy2, cz2,
				sx, sy, sz);
		final var r = cradius + sradius + epsilon;
		return sqDist <= r * r;
	}

	/** Replies if the given capsule is intersecting the given triangle.
	 *
	 * @param cx1 x coordinate of the first point of the capsule segment.
	 * @param cy1 y coordinate of the first point of the capsule segment.
	 * @param cz1 y coordinate of the first point of the capsule segment.
	 * @param cx2 x coordinate of the second point of the capsule segment.
	 * @param cy2 y coordinate of the second point of the capsule segment.
	 * @param cz2 y coordinate of the second point of the capsule segment.
	 * @param cradius the radius of the capsule.
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param epsilon the approximation distance to consider for intersection.
	 * @return {@code true} if the two shapes are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static boolean intersectsCapsuleTriangle(
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2, double cradius,
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double epsilon) {
		assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
		assert epsilon >= 0. : AssertMessages.positiveOrZeroParameter(11);
		final var sqDist = Triangle3afp.calculatesDistanceSquaredTriangleSegment(
				tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
				cx1, cy1, cz1, cx2, cy2, cz2,
				epsilon);
		final var r = cradius + epsilon;
		return sqDist <= r * r;
	}

	@Pure
	@Override
	default boolean intersects(Capsule3afp<?, ?, ?, ?, ?, ?, ?> capsule) {
		assert capsule != null : AssertMessages.notNullParameter();
		return intersectsCapsuleCapsule(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				capsule.getX1(), capsule.getY1(), capsule.getZ1(),
				capsule.getX2(), capsule.getY2(), capsule.getZ2(),
				capsule.getRadius(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return intersectsCapsuleSphere(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		return intersectsCapsuleAlignedBox(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				prism.getMinX(), prism.getMinY(), prism.getMinZ(),
				prism.getMaxX(), prism.getMaxY(), prism.getMaxZ(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return intersectsCapsuleSegment(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		return intersectsCapsuleTriangle(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
				triangle.getX3(), triangle.getY3(), triangle.getZ3(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		return intersectsCapsulePathIterator(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				iterator,
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default double getDistance(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var x1 = getX1();
		final var y1 = getY1();
		final var z1 = getZ1();
		final var x2 = getX2();
		final var y2 = getY2();
		final var z2 = getZ2();
		final var px = point.getX();
		final var py = point.getY();
		final var pz = point.getZ();
		final var factor = Segment3afp.findsProjectedPointOnLine(
				px, py, pz,
				x1, y1, z1,
				x2, y2, z2);
		final double cx;
		final double cy;
		final double cz;
		if (factor <= 0.) {
			cx = x1;
			cy = y1;
			cz = z1;
		} else if (factor >= 1.) {
			cx = x2;
			cy = y2;
			cz = z2;
		} else {
			cx = Math.fma(factor, x2 - x1, x1);
			cy = Math.fma(factor, y2 - y1, y1);
			cz = Math.fma(factor, z2 - z1, z1);
		}
		final var vx = px - cx;
		final var vy = py - cy;
		final var vz = pz - cz;
		final var length = Math.sqrt(Vector3D.dotProduct(vx, vy, vz, vx, vy, vz));
		return Math.max(length - getRadius(), 0.);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var length = getDistance(point);
		return length * length;
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var cls = getGeomFactory().newPoint();
		findsClosestPointToCapsulePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				point.getX(), point.getY(), point.getZ(),
				cls);
		final var x = Math.abs(point.getX() - cls.getX());
		final var y = Math.abs(point.getY() - cls.getY());
		final var z = Math.abs(point.getZ() - cls.getZ());
		return x + y + z;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var cls = getGeomFactory().newPoint();
		findsClosestPointToCapsulePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getRadius(),
				point.getX(), point.getY(), point.getZ(),
				cls);
		final var x = Math.abs(point.getX() - cls.getX());
		final var y = Math.abs(point.getY() - cls.getY());
		final var z = Math.abs(point.getZ() - cls.getZ());
		return Math.max(x, Math.max(y, z));
	}

}

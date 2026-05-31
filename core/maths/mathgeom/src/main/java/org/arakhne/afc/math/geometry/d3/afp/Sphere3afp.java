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
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Shape3DType;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** Interface that represented a 3D sphere independently of the storage of the coordinates.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public interface Sphere3afp<
			IT extends Sphere3afp<?, IE, P, V, Q, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>,
			B extends AlignedBox3afp<?, IE, P, V, Q, B>>
		extends Box3afp<IT, IE, P, V, Q, B> {

	@Override
	default Shape3DType getType() {
		return Shape3DType.SPHERE;
	}

	/**
	 * Replies if the given point is inside the given ellipse.
	 *
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param pz is the point to test.
	 * @param cx is the center of the sphere.
	 * @param cy is the center of the sphere.
	 * @param cz is the center of the sphere.
	 * @param radius is the radius of the sphere.
	 * @return {@code true} if the point is inside the sphere;
	 *     {@code false} if not.
	 */
	@Pure
	static boolean containsSpherePoint(double cx, double cy, double cz, double radius, double px, double py, double pz) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return Point3D.getDistanceSquaredPointPoint(
				px, py, pz,
				cx, cy, cz) <= (radius * radius);
	}

	/** Replies if an aligned box is inside in the sphere.
	 *
	 * @param cx is the center of the sphere.
	 * @param cy is the center of the sphere.
	 * @param cz is the center of the sphere.
	 * @param radius is the radius of the sphere.
	 * @param rxmin is the lowest corner of the rectangle.
	 * @param rymin is the lowest corner of the rectangle.
	 * @param rzmin is the lowest corner of the rectangle.
	 * @param rxmax is the uppest corner of the rectangle.
	 * @param rymax is the uppest corner of the rectangle.
	 * @param rzmax is the uppest corner of the rectangle.
	 * @return {@code true} if the given rectangle is inside the sphere;
	 *     otherwise {@code false}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    static boolean containsSphereAlignedBox(double cx, double cy, double cz, double radius, double rxmin, double rymin,
            double rzmin, double rxmax, double rymax, double rzmax) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(4, Double.valueOf(rxmin), 7, Double.valueOf(rxmax));
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(5, Double.valueOf(rymin), 8, Double.valueOf(rymax));
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(6, Double.valueOf(rzmin), 9, Double.valueOf(rzmax));
		final var rcx = (rxmin + rxmax) / 2;
		final var rcy = (rymin + rymax) / 2;
		final var rcz = (rzmin + rzmax) / 2;
		final double farX;
        if (cx <= rcx) {
			farX = rxmax;
		} else {
			farX = rxmin;
		}
		final double farY;
        if (cy <= rcy) {
			farY = rymax;
		} else {
			farY = rymin;
		}
		final double farZ;
        if (cz <= rcz) {
			farZ = rzmax;
		} else {
			farZ = rzmin;
		}
		return containsSpherePoint(cx, cy, cz, radius, farX, farY, farZ);
	}

	/** Replies if two spheres are intersecting.
	 *
	 * @param x1 is the center of the first sphere
	 * @param y1 is the center of the first sphere
	 * @param z1 is the center of the first sphere
	 * @param radius1 is the radius of the first sphere
	 * @param x2 is the center of the second sphere
	 * @param y2 is the center of the second sphere
	 * @param z2 is the center of the second sphere
	 * @param radius2 is the radius of the second sphere
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 *     {@code false}
	 */
	@Pure
    static boolean intersectsSphereSphere(double x1, double y1, double z1, double radius1, double x2, double y2, double z2,
            double radius2) {
		assert radius1 >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert radius2 >= 0 : AssertMessages.positiveOrZeroParameter(7);
		final var r = radius1 + radius2;
		return Point3D.getDistanceSquaredPointPoint(x1, y1, z1, x2, y2, z2) < (r * r);
	}

	/** Replies if a sphere and an aligned box are intersecting.
	 *
	 * @param x1 is the center of the sphere
	 * @param y1 is the center of the sphere
	 * @param z1 is the center of the sphere
	 * @param radius is the radius of the sphere
	 * @param x2 is the first corner of the rectangle.
	 * @param y2 is the first corner of the rectangle.
	 * @param z2 is the first corner of the rectangle.
	 * @param x3 is the second corner of the rectangle.
	 * @param y3 is the second corner of the rectangle.
	 * @param z3 is the second corner of the rectangle.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 *     {@code false}
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    static boolean intersectsSphereAlignedBox(double x1, double y1, double z1, double radius, double x2, double y2, double z2,
            double x3, double y3, double z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert x2 <= x3 : AssertMessages.lowerEqualParameters(4, Double.valueOf(x2), 7, Double.valueOf(x3));
		assert y2 <= y3 : AssertMessages.lowerEqualParameters(5, Double.valueOf(y2), 7, Double.valueOf(y3));
		assert z2 <= z3 : AssertMessages.lowerEqualParameters(6, Double.valueOf(z2), 7, Double.valueOf(z3));
		final double dx;
		if (x1 < x2) {
			dx = x2 - x1;
		} else if (x1 > x3) {
			dx = x1 - x3;
		} else {
			dx = 0;
		}
		final double dy;
		if (y1 < y2) {
			dy = y2 - y1;
		} else if (y1 > y3) {
			dy = y1 - y3;
		} else {
			dy = 0;
		}
		final double dz;
		if (z1 < z2) {
			dz = z2 - z1;
		} else if (z1 > z3) {
			dz = z1 - z3;
		} else {
			dz = 0;
		}
		return (dx * dx + dy * dy + dz * dz) < (radius * radius);
	}

	/** Replies if a sphere and a line are intersecting.
	 *
	 * @param x1 is the center of the sphere
	 * @param y1 is the center of the sphere
	 * @param z1 is the center of the sphere
	 * @param radius is the radius of the sphere
	 * @param x2 is the first point of the line.
	 * @param y2 is the first point of the line.
	 * @param z2 is the first point of the line.
	 * @param x3 is the second point of the line.
	 * @param y3 is the second point of the line.
	 * @param z3 is the second point of the line.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 *     {@code false}
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    static boolean intersectsSphereLine(double x1, double y1, double z1, double radius, double x2, double y2, double z2,
            double x3, double y3, double z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return Segment3afp.calculatesDistanceSquaredLinePoint(x2, y2, z2, x3, y3, z3, x1, y1, z1) < (radius * radius);
	}

	/** Replies if a sphere and a segment are intersecting.
	 *
	 * @param x1 is the center of the sphere
	 * @param y1 is the center of the sphere
	 * @param z1 is the center of the sphere
	 * @param radius is the radius of the sphere
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param z2 is the first point of the segment.
	 * @param x3 is the second point of the segment.
	 * @param y3 is the second point of the segment.
	 * @param z3 is the second point of the segment.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 *     {@code false}
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    static boolean intersectsSphereSegment(double x1, double y1, double z1, double radius, double x2, double y2, double z2,
            double x3, double y3, double z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return Segment3afp.calculatesDistanceSquaredSegmentPoint(x2, y2, z2, x3, y3, z3, x1, y1, z1) < (radius * radius);
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
		return getX() == shape.getX()
			&& getY() == shape.getY()
			&& getZ() == shape.getZ()
			&& getRadius() == shape.getRadius();
	}

	/** Replies the center X.
	 *
	 * @return the center x.
	 */
	@Pure
	double getX();

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	double getY();

	/** Replies the center z.
	 *
	 * @return the center z.
	 */
	@Pure
	double getZ();

	/** Replies the center.
	 *
	 * @return a copy of the center.
	 */
	@Override
	@Pure
	default P getCenter() {
		return getGeomFactory().newPoint(getX(), getY(), getZ());
	}

	/** Change the center.
	 *
	 * @param center the center point.
	 */
	@Override
	default void setCenter(Point3D<?, ?, ?> center) {
		assert center != null : AssertMessages.notNullParameter();
		set(center.getX(), center.getY(), center.getZ(), getRadius());
	}

	/** Change the center.
	 *
	 * @param x x coordinate of the center point.
	 * @param y y coordinate of the center point.
     * @param z z coordinate of the center point.
	 */
	@Override
	default void setCenter(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	/** Change the x coordinate of the center.
	 *
	 * @param x x coordinate of the center point.
	 */
	void setX(double x);

	/** Change the y coordinate of the center.
	 *
	 * @param y y coordinate of the center point.
	 */
	void setY(double y);

	/** Change the z coordinate of the center.
	 *
	 * @param z z coordinate of the center point.
	 */
	void setZ(double z);

	/** Replies the radius.
	 *
	 * @return the radius.
	 */
	@Pure
	double getRadius();

	/** Set the radius.
	 *
	 * @param radius is the radius.
	 */
	void setRadius(double radius);

	/** Change the frame of the sphere.
	 *
     * @param x x coordinate of the center point.
     * @param y y coordinate of the center point.
     * @param z z coordinate of the center point.
     * @param radius the radius.
	 */
	// Not a default implementation for ensuring atomic change.
	void set(double x, double y, double z, double radius);

	/** Change the frame of the sphere.
	 *
     * @param center the center point.
     * @param radius the radius.
	 */
	default void set(Point3D<?, ?, ?> center, double radius) {
		assert center != null : AssertMessages.notNullParameter();
		set(center.getX(), center.getY(), center.getZ(), radius);
	}

	@Override
	default void set(IT shape) {
		assert shape != null : AssertMessages.notNullParameter();
		set(shape.getX(), shape.getY(), shape.getZ(), shape.getRadius());
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0);
	}

	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		final var x = getX();
		final var y = getY();
		final var z = getZ();
		final var radius = getRadius();
		box.setFromCorners(
				x - radius, y - radius, z - radius,
				x + radius, y + radius, z + radius);
	}

	@Override
	default boolean isEmpty() {
		return MathUtil.isEpsilonZero(getRadius());
	}

	@Pure
	@Override
	default double getDistance(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		var distance = Point3D.getDistancePointPoint(getX(), getY(), getZ(), pt.getX(), pt.getY(), pt.getZ());
		distance = distance - getRadius();
		return Math.max(0., distance);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var x = getX();
		final var y = getY();
		final var z = getZ();
		final var radius = getRadius();
		final var vx = pt.getX() - x;
		final var vy = pt.getY() - y;
		final var vz = pt.getZ() - z;
		final var sqLength = vx * vx + vy * vy + vz * vz;
		final var sqRadius = radius * radius;
		if (sqLength <= sqRadius) {
			return 0;
		}
		return Math.max(0., sqLength - 2 * Math.sqrt(sqLength) * radius + sqRadius);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var r = getClosestPointTo(pt);
		return r.getDistanceL1(pt);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var r = getClosestPointTo(pt);
		return r.getDistanceLinf(pt);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return containsSpherePoint(getX(), getY(), getZ(), getRadius(), x, y, z);
	}

	@Override
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
		assert AlignedBox != null : AssertMessages.notNullParameter();
        return containsSphereAlignedBox(getX(), getY(), getZ(), getRadius(), AlignedBox.getMinX(),
                AlignedBox.getMinY(), AlignedBox.getMinZ(), AlignedBox.getMaxX(), AlignedBox.getMaxY(),
                AlignedBox.getMaxZ());
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		setCenter(getX() + dx, getY() + dy, getZ() + dz);
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		return intersectsSphereAlignedBox(
				getX(), getY(), getZ(), getRadius(),
				prism.getMinX(), prism.getMinY(), prism.getMinZ(), prism.getMaxX(), prism.getMaxY(), prism.getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return intersectsSphereSphere(
				getX(), getY(), getZ(), getRadius(),
				sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return intersectsSphereSegment(
				getX(), getY(), getZ(), getRadius(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final var mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;

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
		final var x = getX();
		final var y = getY();
		final var z = getZ();
		final var radius = getRadius();
		final var vx = pt.getX() - x;
		final var vy = pt.getY() - y;
		final var vz = pt.getZ() - z;
		final var sqLength = vx * vx + vy * vy + vz * vz;
		if (sqLength <= (radius * radius)) {
			return getGeomFactory().convertToPoint(pt);
		}
		final var s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s, z + vz * s);
	}

    @Pure
    @Override
    default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
        assert sphere != null : AssertMessages.notNullParameter();
        final Point3D<?, ?, ?> point = sphere.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
        assert AlignedBox != null : AssertMessages.notNullParameter();
        final var point = AlignedBox.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        final var point = segment.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final var point = path.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        final var point = multishape.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var x = getX();
		final var y = getY();
		final var z = getZ();
		final var vx = x - pt.getX();
		final var vy = y - pt.getY();
		final var vz = z - pt.getZ();
		final var radius = getRadius();
		final var sqLength = vx * vx + vy * vy + vz * vz;
		if (sqLength <= 0.) {
			return getGeomFactory().newPoint(radius, 0, 0);
		}
		final var s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s, z + vz * s);
	}

	@Override
	@Pure
	default double getHeight() {
		return getRadius();
	}

	@Override
	@Pure
	default double getDepth() {
		return getRadius();
	}

	@Override
	@Pure
	default double getWidth() {
		return getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>The sphere is set in order to be enclosed inside the given box.
	 * It means that the center of the sphere is the center of the box, and the
	 * radius of the sphere is the minimum of the demi-width, demi-height and demi-depth.
	 */
	@Override
	default void setFromCenter(double centerX, double centerY, double centerZ, double cornerX, double cornerY, double cornerZ) {
		final var demiWidth = Math.abs(cornerX - centerX);
		final var demiHeight = Math.abs(cornerY - centerY);
		final var demiDepth = Math.abs(cornerZ - centerZ);
		set(centerX, centerY, centerZ, MathUtil.min(demiHeight, demiWidth, demiDepth));
	}

	/** {@inheritDoc}
	 *
	 * <p>The sphere is set in order to be enclosed inside the given box.
	 * It means that the center of the sphere is the center of the box, and the
	 * radius of the sphere is the minimum of the demi-width, demi-height and demi-depth.
	 */
	@Override
	default void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2) {
		setFromCenter((x1 + x2) / 2., (y1 + y2) / 2., (z1 + z2) / 2., x2, y2, z2);
	}

	@Override
	default double getMinX() {
		return getX() - getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the maximum X coordinate should not change, the center of
	 * the sphere is the point between the new minimum and the current maximum coordinates,
	 * and the radius of the sphere is set to the difference between the new minimum and
	 * center coordinates.
	 *
	 * <p>If the new minimum is greater than the current maximum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMinX(double x) {
		final var cx = (x + getX() + getRadius()) / 2.;
		final var radius = Math.abs(cx - x);
		set(cx, getY(), getZ(), radius);
	}

	@Override
	default double getMaxX() {
		return getX() + getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the minimum X coordinate should not change, the center of
	 * the sphere is the point between the new maximum and the current minimum coordinates,
	 * and the radius of the sphere is set to the difference between the new maximum and
	 * center coordinates.
	 *
	 * <p>If the new maximum is lower than the current minimum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMaxX(double x) {
		final var cx = (x + getX() - getRadius()) / 2.;
		final var radius = Math.abs(cx - x);
		set(cx, getY(), getZ(), radius);
	}

	@Override
	default double getMinY() {
		return getY() - getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the maximum Y coordinate should not change, the center of
	 * the sphere is the point between the new minimum and the current maximum coordinates,
	 * and the radius of the sphere is set to the difference between the new minimum and
	 * center coordinates.
	 *
	 * <p>If the new minimum is greater than the current maximum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMinY(double y) {
		final var cy = (y + getY() + getRadius()) / 2.;
		final var radius = Math.abs(cy - y);
		set(getX(), cy, getZ(), radius);
	}

	@Override
	default double getMaxY() {
		return getY() + getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the minimum Y coordinate should not change, the center of
	 * the sphere is the point between the new maximum and the current minimum coordinates,
	 * and the radius of the sphere is set to the difference between the new maximum and
	 * center coordinates.
	 *
	 * <p>If the new maximum is lower than the current minimum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMaxY(double y) {
		final var cy = (y + getY() - getRadius()) / 2.;
		final var radius = Math.abs(cy - y);
		set(getX(), cy, getZ(), radius);
	}

	@Override
	default double getMinZ() {
		return getZ() - getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the maximum Z coordinate should not change, the center of
	 * the sphere is the point between the new minimum and the current maximum coordinates,
	 * and the radius of the sphere is set to the difference between the new minimum and
	 * center coordinates.
	 *
	 * <p>If the new minimum is greater than the current maximum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMinZ(double z) {
		final var cz = (z + getZ() + getRadius()) / 2.;
		final var radius = Math.abs(cz - z);
		set(getX(), getY(), cz, radius);
	}

	@Override
	default double getMaxZ() {
		return getZ() + getRadius();
	}

	/** Replies this sphere with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the sphere.
	 * @since 18.0
	 */
	@Override
	default String toGeogebra() {
		return GeogebraUtil.toCircleDefinition(3, getX(), getY(), getZ(), getRadius());
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the minimum Z coordinate should not change, the center of
	 * the sphere is the point between the new maximum and the current minimum coordinates,
	 * and the radius of the sphere is set to the difference between the new maximum and
	 * center coordinates.
	 *
	 * <p>If the new maximum is lower than the current minimum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMaxZ(double z) {
		final var cz = (z + getZ() - getRadius()) / 2.;
		final var radius = Math.abs(cz - z);
		set(getX(), getY(), cz, radius);
	}

}

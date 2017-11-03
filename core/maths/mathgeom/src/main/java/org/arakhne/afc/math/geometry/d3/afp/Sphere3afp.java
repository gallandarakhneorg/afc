/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 2D sphere on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Sphere3afp<
		ST extends Shape3afp<?, ?, IE, P, V, B>,
		IT extends Sphere3afp<?, ?, IE, P, V, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
		extends Prism3afp<ST, IT, IE, P, V, B> {

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
	 * @return <code>true</code> if the point is inside the sphere;
	 * <code>false</code> if not.
	 */
	@Pure
	static boolean containsSpherePoint(double cx, double cy, double cz, double radius, double px, double py, double pz) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return Point3D.getDistanceSquaredPointPoint(
				px, py, pz,
				cx, cy, cz) <= (radius * radius);
	}

	/** Replies if a rectangular prism is inside in the sphere.
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
	 * @return <code>true</code> if the given rectangle is inside the sphere;
	 *     otherwise <code>false</code>.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean containsSphereRectangularPrism(double cx, double cy, double cz, double radius, double rxmin, double rymin,
            double rzmin, double rxmax, double rymax, double rzmax) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(4, rxmin, 7, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(5, rymin, 8, rymax);
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(6, rzmin, 9, rzmax);
		final double rcx = (rxmin + rxmax) / 2;
		final double rcy = (rymin + rymax) / 2;
		final double rcz = (rzmin + rzmax) / 2;
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
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsSphereSphere(double x1, double y1, double z1, double radius1, double x2, double y2, double z2,
            double radius2) {
		assert radius1 >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert radius2 >= 0 : AssertMessages.positiveOrZeroParameter(7);
		final double r = radius1 + radius2;
		return Point3D.getDistanceSquaredPointPoint(x1, y1, z1, x2, y2, z2) < (r * r);
	}

	/** Replies if a sphere and a rectangle are intersecting.
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
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsSpherePrism(double x1, double y1, double z1, double radius, double x2, double y2, double z2,
            double x3, double y3, double z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert x2 <= x3 : AssertMessages.lowerEqualParameters(4, x2, 7, x3);
		assert y2 <= y3 : AssertMessages.lowerEqualParameters(5, y2, 7, y3);
		assert z2 <= z3 : AssertMessages.lowerEqualParameters(6, z2, 7, z3);
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
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    static boolean intersectsSphereLine(double x1, double y1, double z1, double radius, double x2, double y2, double z2,
            double x3, double y3, double z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return Segment3afp.computeDistanceSquaredLinePoint(x2, y2, z2, x3, y3, z3, x1, y1, z1) < (radius * radius);
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
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    static boolean intersectsSphereSegment(double x1, double y1, double z1, double radius, double x2, double y2, double z2,
            double x3, double y3, double z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return Segment3afp.computeDistanceSquaredSegmentPoint(x2, y2, z2, x3, y3, z3, x1, y1, z1) < (radius * radius);
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
	default void setCenter(Point3D<?, ?> center) {
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
	default void set(Point3D<?, ?> center, double radius) {
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
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		final double radius = getRadius();
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
	default double getDistance(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		double distance = Point3D.getDistancePointPoint(getX(), getY(), getZ(), pt.getX(), pt.getY(), pt.getZ());
		distance = distance - getRadius();
		return Math.max(0., distance);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		final double radius = getRadius();
		final double vx = pt.getX() - x;
		final double vy = pt.getY() - y;
		final double vz = pt.getZ() - z;
		final double sqLength = vx * vx + vy * vy + vz * vz;
		final double sqRadius = radius * radius;
		if (sqLength <= sqRadius) {
			return 0;
		}
		return Math.max(0., sqLength - 2 * Math.sqrt(sqLength) * radius + sqRadius);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final Point3D<?, ?> r = getClosestPointTo(pt);
		return r.getDistanceL1(pt);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final Point3D<?, ?> r = getClosestPointTo(pt);
		return r.getDistanceLinf(pt);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return containsSpherePoint(getX(), getY(), getZ(), getRadius(), x, y, z);
	}

	@Override
	default boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangularPrism) {
		assert rectangularPrism != null : AssertMessages.notNullParameter();
        return containsSphereRectangularPrism(getX(), getY(), getZ(), getRadius(), rectangularPrism.getMinX(),
                rectangularPrism.getMinY(), rectangularPrism.getMinZ(), rectangularPrism.getMaxX(), rectangularPrism.getMaxY(),
                rectangularPrism.getMaxZ());
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		setCenter(getX() + dx, getY() + dy, getZ() + dz);
	}

	@Pure
	@Override
	default boolean intersects(RectangularPrism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		return intersectsSpherePrism(
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
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
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
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path3afp.computeCrossingsFromSphere(
				0,
				iterator,
				getX(), getY(), getZ(), getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		final double radius = getRadius();
		final double vx = pt.getX() - x;
		final double vy = pt.getY() - y;
		final double vz = pt.getZ() - z;
		final double sqLength = vx * vx + vy * vy + vz * vz;
		if (sqLength <= (radius * radius)) {
			return getGeomFactory().convertToPoint(pt);
		}
		final double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s, z + vz * s);
	}

    @Pure
    @Override
    default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
        assert sphere != null : AssertMessages.notNullParameter();
        final Point3D<?, ?> point = sphere.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangularPrism) {
        assert rectangularPrism != null : AssertMessages.notNullParameter();
        final Point3D<?, ?> point = rectangularPrism.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        final Point3D<?, ?> point = segment.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final Point3D<?, ?> point = path.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

    @Pure
    @Override
    default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        final Point3D<?, ?> point = multishape.getClosestPointTo(getCenter());
        return getClosestPointTo(point);
    }

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		final double vx = x - pt.getX();
		final double vy = y - pt.getY();
		final double vz = z - pt.getZ();
		final double radius = getRadius();
		final double sqLength = vx * vx + vy * vy + vz * vz;
		if (sqLength <= 0.) {
			return getGeomFactory().newPoint(radius, 0, 0);
		}
		final double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s, z + vz * s);
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
        if (transform == null || transform.isIdentity()) {
			return new SpherePathIterator<>(this);
		}
		return new TransformedCirclePathIterator<>(this, transform);
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
		final double demiWidth = Math.abs(cornerX - centerX);
		final double demiHeight = Math.abs(cornerY - centerY);
		final double demiDepth = Math.abs(cornerZ - centerZ);
		set(centerX, centerY, centerZ, MathUtil.min(demiHeight, demiWidth, demiDepth));
	}

	/** {@inheritDoc}
	 *
	 * <p>The sphere is set in order to be enclosed inside the given box.
	 * It means that the center of the sphere is the center of the box, and the
	 * radius of the sphere is the minimum of the demi-width and demi-height.
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
		final double cx = (x + getX() + getRadius()) / 2.;
		final double radius = Math.abs(cx - x);
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
		final double cx = (x + getX() - getRadius()) / 2.;
		final double radius = Math.abs(cx - x);
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
		final double cy = (y + getY() + getRadius()) / 2.;
		final double radius = Math.abs(cy - y);
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
		final double cy = (y + getY() - getRadius()) / 2.;
		final double radius = Math.abs(cy - y);
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
		final double cz = (z + getZ() + getRadius()) / 2.;
		final double radius = Math.abs(cz - z);
		set(getX(), getY(), cz, radius);
	}

	@Override
	default double getMaxZ() {
		return getZ() + getRadius();
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
		final double cz = (z + getZ() - getRadius()) / 2.;
		final double radius = Math.abs(cz - z);
		set(getX(), getY(), cz, radius);
	}

	/** Abstract iterator on the path elements of the sphere.
	 *
	 * <h3>Discretization of the sphere with Bezier</h3>
	 *
	 * <p>For n segments on the sphere, the optimal distance to the control points, in the sense that the
	 * middle of the curve lies on the sphere itself, is (4/3)*tan(pi/(2n)).
	 *
	 * <p><img src="../../d2/afp/doc-files/circlebezier.png" width="100%" />
	 *
	 * <p>In the case of a discretization with 4 bezier curves, the distance is is
	 * (4/3)*tan(pi/8) = 4*(sqrt(2)-1)/3 = 0.552284749831.
	 *
	 * <p><img src="../../d2/afp/doc-files/circlepathiterator.png" width="100%" />
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractSpherePathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		/**
		 * Distance from a Bezier curve control point on the sphere to the other control point.
		 *
		 * <p>4/3 tan (PI/(2*n)), where n is the number on points on the sphere.
		 */
		public static final double CTRL_POINT_DISTANCE = 0.5522847498307933;

		/**
		 * Contains the control points for a set of 4 cubic
		 * bezier curves that approximate a sphere of radius 1
		 * centered at (0,0,0).
		 */
		// TODO : change from 2D to 3D
		public static final double[][] BEZIER_CONTROL_POINTS = {
			// First quarter: max x, max y.
			{1, CTRL_POINT_DISTANCE, CTRL_POINT_DISTANCE, 1, 0, 1 },
			// Second quarter: min x, max y.
			{-CTRL_POINT_DISTANCE, 1, -1, CTRL_POINT_DISTANCE, -1, 0 },
			// Third quarter: min x, min y.
			{-1, -CTRL_POINT_DISTANCE, -CTRL_POINT_DISTANCE, -1, 0, -1 },
			// Fourth quarter: max x, min y.
			{CTRL_POINT_DISTANCE, -1, 1, -CTRL_POINT_DISTANCE, 1, 0 },
		};

		/** 4 segments + close.
		 */
		protected static final int NUMBER_ELEMENTS = 5;

		/** The iterated shape.
		 */
		protected final Sphere3afp<?, ?, T, ?, ?, ?> sphere;

		/** Constructor.
		 * @param sphere the sphere.
		 */
		public AbstractSpherePathIterator(Sphere3afp<?, ?, T, ?, ?, ?> sphere) {
			assert sphere != null : AssertMessages.notNullParameter();
			this.sphere = sphere;
		}

		@Override
		public GeomFactory3afp<T, ?, ?, ?> getGeomFactory() {
			return this.sphere.getGeomFactory();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return false;
		}

		@Override
		public boolean isCurved() {
			return true;
		}

		@Override
		public boolean isPolygon() {
			return true;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

	}

	/** Iterator on the path elements of the sphere.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class SpherePathIterator<T extends PathElement3afp> extends AbstractSpherePathIterator<T> {

		private double x;

		private double y;

		private double z;

		private double radius;

		private int index;

		private double movex;

		private double movey;

		private double movez;

		private double lastx;

		private double lasty;

		private double lastz;

		/** Constructor.
		 * @param sphere the sphere to iterate on.
		 */
		public SpherePathIterator(Sphere3afp<?, ?, T, ?, ?, ?> sphere) {
			super(sphere);
			if (sphere.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
			} else {
				this.radius = sphere.getRadius();
				this.x = sphere.getX();
				this.y = sphere.getY();
				this.z = sphere.getZ();
				this.index = -1;
			}
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new SpherePathIterator<>(this.sphere);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < NUMBER_ELEMENTS;
		}

		@Override
		public T next() {
			if (this.index >= NUMBER_ELEMENTS) {
				throw new NoSuchElementException();
			}
			final int idx = this.index;
			++this.index;
			if (idx < 0) {
				final double[] ctrls = BEZIER_CONTROL_POINTS[3];
				this.movex = this.x + ctrls[4] * this.radius;
				this.movey = this.y + ctrls[5] * this.radius;
				this.movez = this.z + ctrls[6] * this.radius;
				this.lastx = this.movex;
				this.lasty = this.movey;
				this.lastz = this.movez;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty, this.lastz);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				final double[] ctrls = BEZIER_CONTROL_POINTS[idx];
				final double ppx = this.lastx;
				final double ppy = this.lasty;
				final double ppz = this.lastz;
				this.lastx = this.x + ctrls[4] * this.radius;
				this.lasty = this.y + ctrls[5] * this.radius;
				this.lastz = this.z + ctrls[6] * this.radius;
				return getGeomFactory().newCurvePathElement(
						ppx, ppy, ppz,
						this.x + ctrls[0] * this.radius,
						this.y + ctrls[1] * this.radius,
						this.z + ctrls[2] * this.radius,
						this.x + ctrls[3] * this.radius,
						this.y + ctrls[4] * this.radius,
						this.z + ctrls[5] * this.radius,
						this.lastx, this.lasty, this.lastz);
			}
			return getGeomFactory().newClosePathElement(
					this.lastx, this.lasty, this.lastz,
					this.movex, this.movey, this.movez);
		}

	}

	/** Iterator on the path elements of the sphere.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class TransformedCirclePathIterator<T extends PathElement3afp> extends AbstractSpherePathIterator<T> {

		private final Transform3D transform;

		private final Point3D<?, ?> tmpPoint;

		private double x;

		private double y;

		private double z;

		private double radius;

		private double movex;

		private double movey;

		private double movez;

		private double lastx;

		private double lasty;

		private double lastz;

		private int index;

		/** Constructor.
		 * @param sphere the iterated sphere.
		 * @param transform the transformation to apply.
		 */
		public TransformedCirclePathIterator(Sphere3afp<?, ?, T, ?, ?, ?> sphere, Transform3D transform) {
			super(sphere);
			assert transform != null : AssertMessages.notNullParameter();
			this.transform = transform;
			if (sphere.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
				this.tmpPoint = null;
			} else {
				this.tmpPoint = new InnerComputationPoint3afp();
				this.radius = sphere.getRadius();
				this.x = sphere.getX();
				this.y = sphere.getY();
				this.z = sphere.getZ();
				this.index = -1;
			}
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new TransformedCirclePathIterator<>(this.sphere, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < NUMBER_ELEMENTS;
		}

		@Override
		public T next() {
			if (this.index >= NUMBER_ELEMENTS) {
				throw new NoSuchElementException();
			}
			final int idx = this.index;
			++this.index;
			if (idx < 0) {
				final double[] ctrls = BEZIER_CONTROL_POINTS[3];
                this.tmpPoint.set(this.x + ctrls[4] * this.radius, this.y + ctrls[5] * this.radius,
                        this.z + ctrls[6] * this.radius);
				this.transform.transform(this.tmpPoint);
				this.movex = this.tmpPoint.getX();
				this.lastx = this.movex;
				this.movey = this.tmpPoint.getY();
				this.lasty = this.movey;
				this.movez = this.tmpPoint.getZ();
				this.lastz = this.movez;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty, this.lastz);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				final double[] ctrls = BEZIER_CONTROL_POINTS[idx];
				final double ppx = this.lastx;
				final double ppy = this.lasty;
				final double ppz = this.lastz;
                this.tmpPoint.set(this.x + ctrls[0] * this.radius, this.y + ctrls[1] * this.radius,
                        this.z + ctrls[2] * this.radius);
				this.transform.transform(this.tmpPoint);
				final double ctrlX1 = this.tmpPoint.getX();
				final double ctrlY1 = this.tmpPoint.getY();
				final double ctrlZ1 = this.tmpPoint.getZ();
                this.tmpPoint.set(this.x + ctrls[3] * this.radius, this.y + ctrls[4] * this.radius,
                        this.z + ctrls[5] * this.radius);
				this.transform.transform(this.tmpPoint);
				final double ctrlX2 = this.tmpPoint.getX();
				final double ctrlY2 = this.tmpPoint.getY();
				final double ctrlZ2 = this.tmpPoint.getZ();
                this.tmpPoint.set(this.x + ctrls[6] * this.radius, this.y + ctrls[7] * this.radius,
                        this.z + ctrls[8] * this.radius);
				this.transform.transform(this.tmpPoint);
				this.lastx = this.tmpPoint.getX();
				this.lasty = this.tmpPoint.getY();
				this.lastz = this.tmpPoint.getZ();
				return getGeomFactory().newCurvePathElement(
						ppx, ppy, ppz,
						ctrlX1, ctrlY1, ctrlZ1,
						ctrlX2, ctrlY2, ctrlZ2,
						this.lastx, this.lasty, this.lastz);
			}
			return getGeomFactory().newClosePathElement(
					this.lastx, this.lasty, this.lastz,
					this.movex, this.movey, this.movez);
		}

	}

}

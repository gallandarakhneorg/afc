/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.arakhne.afc.math.geometry.d3.ad;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ad.Path3ad.CrossingComputationType;
import org.eclipse.xtext.xbase.lib.Pure;

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
public interface Sphere3ad<
		ST extends Shape3ad<?, ?, IE, P, V, B>,
		IT extends Sphere3ad<?, ?, IE, P, V, B>,
		IE extends PathElement3ad,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ad<?, ?, IE, P, V, B>>
		extends Prism3ad<ST, IT, IE, P, V, B> {

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
	static boolean containsCirclePoint(double cx, double cy, double cz, double radius, double px, double py, double pz) {
		assert (radius >= 0) : "sphere radius must be positive or zero"; //$NON-NLS-1$
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
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsCircleRectangularPrism(double cx, double cy, double cz, double radius, double rxmin, double rymin, double rzmin, double rxmax, double rymax, double rzmax) {
		assert (radius >= 0) : "sphere radius must be positive or zero"; //$NON-NLS-1$
		assert (rxmin <= rxmax) : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert (rymin <= rymax) : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
		assert (rzmin <= rzmax) : "rzmin must be lower or equal to rzmax"; //$NON-NLS-1$
		double rcx = (rxmin + rxmax) / 2;
		double rcy = (rymin + rymax) / 2;
		double rcz = (rzmin + rzmax) / 2;
		double farX;
		if (cx<=rcx) {
			farX = rxmax;
		} else {
			farX = rxmin;
		}
		double farY;
		if (cy<=rcy) {
			farY = rymax;
		} else {
			farY = rymin;
		}
		double farZ;
		if (cz<=rcz) {
			farZ = rzmax;
		} else {
			farZ = rzmin;
		}
		return containsCirclePoint(cx, cy, cz, radius, farX, farY, farZ);
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
	static boolean intersectsSphereSphere(double x1, double y1, double z1, double radius1, double x2, double y2, double z2, double radius2) {
		assert (radius1 >= 0) : "First sphere radius must be positive or zero"; //$NON-NLS-1$
		assert (radius2 >= 0) : "Second sphere radius must be positive or zero"; //$NON-NLS-1$
		double r = radius1 + radius2;
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
	static boolean intersectsCircleRectangle(double x1, double y1, double z1, double radius, double x2, double y2, double z2, double x3, double y3, double z3) {
		assert (radius >= 0) : "sphere radius must be positive or zero"; //$NON-NLS-1$
		assert (x2 <= x3) : "x2 must be lower or equal to x3"; //$NON-NLS-1$
		assert (y2 <= y3) : "y2 must be lower or equal to y3"; //$NON-NLS-1$
		double dx;
		if (x1 < x2) {
			dx = x2 - x1;
		} else if (x1 > x3) {
			dx = x1 - x3;
		} else {
			dx = 0;
		}
		double dy;
		if (y1 < y2) {
			dy = y2 - y1;
		} else if (y1 > y3) {
			dy = y1 - y3;
		} else {
			dy = 0;
		}
		double dz;
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
	static boolean intersectsSphereLine(double x1, double y1, double z1, double radius, double x2, double y2, double z2, double x3, double y3, double z3) {
		assert (radius >= 0) : "sphere radius must be positive or zero"; //$NON-NLS-1$
		double d = Segment3ad.computeDistanceSquaredLinePoint(x2, y2, z2, x3, y3, z3, x1, y1, z1);
		return d < (radius * radius);
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
	static boolean intersectsSphereSegment(double x1, double y1, double z1, double radius, double x2, double y2, double z2, double x3, double y3, double z3) {
		assert (radius >= 0) : "sphere radius must be positive or zero"; //$NON-NLS-1$
		double d = Segment3ad.computeDistanceSquaredSegmentPoint(x2, y2, z2, x3, y3, z3, x1, y1, z1);
		return d < (radius * radius);
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
	 * @param center
	 */
	@Override
	default void setCenter(Point3D<?, ?> center) {
		assert (center != null) : "Point must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), center.getZ(), getRadius());
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	default void setCenter(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	/** Change the x coordinate of the center.
	 * 
	 * @param x
	 */
	void setX(double x);
	
	/** Change the y coordinate of the center.
	 * 
	 * @param y
	 */
	void setY(double y);

	/** Change the z coordinate of the center.
	 * 
	 * @param z
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
	 * @param x
	 * @param y
	 * @param z 
	 * @param radius
	 */
	// Not a default implementation for ensuring atomic change.
	void set(double x, double y, double z, double radius);

	/** Change the frame of the sphere.
	 * 
	 * @param center
	 * @param radius
	 */
	default void set(Point3D<?, ?> center, double radius) {
		assert (center != null) : "Point must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), center.getZ(), radius);
	}

	@Override
	default void set(IT s) {
		assert (s != null) : "Shape must be not null"; //$NON-NLS-1$
		set(s.getX(), s.getY(), s.getZ(), s.getRadius());
	}
	
	@Override
	default void clear() {
		set(0, 0, 0, 0);
	}
	
	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double z = getZ();
		double radius = getRadius();
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
	default double getDistance(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double d = Point3D.getDistancePointPoint(getX(), getY(), getZ(), p.getX(), p.getY(), p.getZ());
		d = d - getRadius();
		return Math.max(0., d);
	}
	
	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double z = getZ();
		double radius = getRadius();
		double vx = p.getX() - x;
		double vy = p.getY() - y;
		double vz = p.getZ() - z;
		double sqLength = vx * vx + vy * vy + vz * vz;
		double sqRadius = radius * radius;
		if (sqLength <= sqRadius) {
			return 0;
		}
		return Math.max(0., sqLength - 2 * Math.sqrt(sqLength) * radius + sqRadius);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point3D<?, ?> r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point3D<?, ?> r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return containsCirclePoint(getX(), getY(), getZ(), getRadius(), x, y, z);
	}
	
	@Override
	default boolean contains(RectangularPrism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return containsCircleRectangularPrism(getX(), getY(), getZ(), getRadius(),
				r.getMinX(), r.getMinY(), r.getMinZ(), r.getMaxX(), r.getMaxY(), r.getMaxZ());
	}
	
	@Override
	default void translate(double dx, double dy, double dz) {
		setCenter(getX() + dx, getY() + dy, getZ() + dz);
	}

	@Pure
	@Override
	default boolean intersects(Prism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return intersectsCircleRectangle(
				getX(), getY(), getZ(), getRadius(),
				r.getMinX(), r.getMinY(), r.getMinZ(), r.getMaxX(), r.getMaxY(), r.getMaxZ());
	}
	
	@Pure
	@Override
	default boolean intersects(Sphere3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "sphere must be not null"; //$NON-NLS-1$
		return intersectsSphereSphere(
				getX(), getY(), getZ(), getRadius(),
				s.getX(), s.getY(), s.getZ(), s.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Segment3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		return intersectsSphereSegment(
				getX(), getY(), getZ(), getRadius(),
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2());
	}


	@Pure
	@Override
	default boolean intersects(PathIterator3ad<?> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path3ad.computeCrossingsFromSphere(
				0,
				iterator,
				getX(), getY(), getZ(), getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}

//	@Pure
//	@Override
//	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> s) {
//		assert (s!= null) : "Round rectangle must be not null"; //$NON-NLS-1$
//		return RoundRectangle2afp.intersectsRoundRectangleCircle(
//				s.getMinX(), s.getMinY(),
//				s.getMaxX(), s.getMaxY(),
//				s.getArcWidth(), s.getArcHeight(),
//				getX(), getY(), getRadius());
//	}
		
	@Pure
	@Override
	default boolean intersects(MultiShape3ad<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double z = getZ();
		double radius = getRadius();
		double vx = p.getX() - x;
		double vy = p.getY() - y;
		double vz = p.getZ() - z;
		double sqLength = vx * vx + vy * vy + vz * vz;
		if (sqLength <= (radius * radius)) {
			return getGeomFactory().convertToPoint(p);
		}
		double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s, z + vz * s);
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double z = getZ();
		double vx = x - p.getX();
		double vy = y - p.getY();
		double vz = z - p.getZ();
		double radius = getRadius();
		double sqLength = vx * vx + vy * vy + vz * vz;
		if (sqLength <= 0.) {
			return getGeomFactory().newPoint(radius, 0, 0);
		}
		double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s, z + vz * s);
	}

	@Pure
	@Override
	default PathIterator3ad<IE> getPathIterator(Transform3D transform) {
		if (transform==null || transform.isIdentity()) {
			return new SpherePathIterator<>(this);
		}
		return new TransformedCirclePathIterator<>(this, transform);
	}
	
//	@Override
//	@Pure
//	default double getHorizontalRadius() {
//		return getRadius();
//	}
//	
//	@Override
//	@Pure
//	default double getVerticalRadius() {
//		return getRadius();
//	}

	/** {@inheritDoc}
	 *
	 * <p>The sphere is set in order to be enclosed inside the given box.
	 * It means that the center of the sphere is the center of the box, and the
	 * radius of the sphere is the minimum of the demi-width, demi-height and demi-depth.
	 */
	@Override
	default void setFromCenter(double centerX, double centerY, double centerZ, double cornerX, double cornerY, double cornerZ) {
		double demiWidth = Math.abs(cornerX - centerX);
		double demiHeight = Math.abs(cornerY - centerY);
		double demiDepth = Math.abs(cornerZ - centerZ);
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
		double cx = (x + getX() + getRadius()) / 2.;
		double radius = Math.abs(cx - x);
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
		double cx = (x + getX() - getRadius()) / 2.;
		double radius = Math.abs(cx - x);
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
		double cy = (y + getY() + getRadius()) / 2.;
		double radius = Math.abs(cy - y);
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
		double cy = (y + getY() - getRadius()) / 2.;
		double radius = Math.abs(cy - y);
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
		double cz = (z + getZ() + getRadius()) / 2.;
		double radius = Math.abs(cz - z);
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
		double cz = (z + getZ() - getRadius()) / 2.;
		double radius = Math.abs(cz - z);
		set(getX(), getY(), cz, radius);
	}

	/** Abstract iterator on the path elements of the sphere.
	 * 
	 * <h3>Discretization of the sphere with Bezier</h3>
	 * 
	 * <p>For n segments on the sphere, the optimal distance to the control points, in the sense that the
	 * middle of the curve lies on the sphere itself, is (4/3)*tan(pi/(2n)).
	 * 
	 * <p><center><img src="./doc-files/circlebezier.png" width="100%" /></center>
	 * 
	 * <p>In the case of a discretization with 4 bezier curves, the distance is is
	 * (4/3)*tan(pi/8) = 4*(sqrt(2)-1)/3 = 0.552284749831.
	 * 
	 * <p><center><img src="./doc-files/circlepathiterator.png" width="100%" /></center>
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractSpherePathIterator<T extends PathElement3ad> implements PathIterator3ad<T> {
		
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
		public static double BEZIER_CONTROL_POINTS[][] = {
			// First quarter: max x, max y.
			{ 1, CTRL_POINT_DISTANCE, CTRL_POINT_DISTANCE, 1, 0, 1 },
			// Second quarter: min x, max y.
			{ -CTRL_POINT_DISTANCE, 1, -1, CTRL_POINT_DISTANCE, -1, 0 },
			// Third quarter: min x, min y.
			{ -1, -CTRL_POINT_DISTANCE, -CTRL_POINT_DISTANCE, -1, 0, -1 },
			// Fourth quarter: max x, min y.
			{ CTRL_POINT_DISTANCE, -1, 1, -CTRL_POINT_DISTANCE, 1, 0 },
		};
		
		/** 4 segments + close
		 */
		protected static final int NUMBER_ELEMENTS = 5;

		/** The iterated shape.
		 */
		protected final Sphere3ad<?, ?, T, ?, ?, ?> sphere;

		/**
		 * @param sphere the sphere.
		 */
		public AbstractSpherePathIterator(Sphere3ad<?, ?, T, ?, ?, ?> sphere) {
			assert (sphere != null) : "sphere must be not null"; //$NON-NLS-1$
			this.sphere = sphere;
		}

		@Override
		public GeomFactory3ad<T, ?, ?, ?> getGeomFactory() {
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
	class SpherePathIterator<T extends PathElement3ad> extends AbstractSpherePathIterator<T> {

		private double x;
		
		private double y;

		private double z;
		
		private double r;
		
		private int index;
		
		private double movex;
		
		private double movey;

		private double movez;
		
		private double lastx;
		
		private double lasty;

		private double lastz;
		
		/**
		 * @param sphere the sphere to iterate on.
		 */
		public SpherePathIterator(Sphere3ad<?, ?, T, ?, ?, ?> sphere) {
			super(sphere);
			if (sphere.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
			} else {
				this.r = sphere.getRadius();
				this.x = sphere.getX();
				this.y = sphere.getY();
				this.z = sphere.getZ();
				this.index = -1;
			}
		}
		
		@Override
		public PathIterator3ad<T> restartIterations() {
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
			int idx = this.index;
			++this.index;
			if (idx < 0) {
				double ctrls[] = BEZIER_CONTROL_POINTS[3];
				this.movex = (this.x + ctrls[4] * this.r);
				this.movey = (this.y + ctrls[5] * this.r);
				this.movez = (this.z + ctrls[6] * this.r);
				this.lastx = this.movex;
				this.lasty = this.movey;
				this.lastz = this.movez;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty, this.lastz);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				double ctrls[] = BEZIER_CONTROL_POINTS[idx];
				double ppx = this.lastx;
				double ppy = this.lasty;
				double ppz = this.lastz;
				this.lastx = (this.x + ctrls[4] * this.r);
				this.lasty = (this.y + ctrls[5] * this.r);
				this.lastz = (this.z + ctrls[6] * this.r);
				return getGeomFactory().newCurvePathElement(
						ppx, ppy, ppz,
						(this.x + ctrls[0] * this.r),
						(this.y + ctrls[1] * this.r),
						(this.z + ctrls[2] * this.r),
						(this.x + ctrls[3] * this.r),
						(this.y + ctrls[4] * this.r),
						(this.z + ctrls[5] * this.r),
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
	class TransformedCirclePathIterator<T extends PathElement3ad> extends AbstractSpherePathIterator<T> {
			
		private final Transform3D transform;

		private final Point3D<?, ?> tmpPoint;
		
		private double x;
		
		private double y;

		private double z;
		
		private double r;
		
		private double movex;
		
		private double movey;

		private double movez;

		private double lastx;
		
		private double lasty;

		private double lastz;

		private int index;

		/**
		 * @param sphere the iterated sphere.
		 * @param transform the transformation to apply.
		 */
		public TransformedCirclePathIterator(Sphere3ad<?, ?, T, ?, ?, ?> sphere, Transform3D transform) {
			super(sphere);
			assert(transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			if (sphere.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
				this.tmpPoint = null;
			} else {
				this.tmpPoint = new InnerComputationPoint3ad();
				this.r = sphere.getRadius();
				this.x = sphere.getX();
				this.y = sphere.getY();
				this.z = sphere.getZ();
				this.index = -1;
			}
		}

		@Override
		public PathIterator3ad<T> restartIterations() {
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
			int idx = this.index;
			++this.index;
			if (idx < 0) {
				double ctrls[] = BEZIER_CONTROL_POINTS[3];
				this.tmpPoint.set(this.x + ctrls[4] * this.r, this.y + ctrls[5] * this.r, this.z + ctrls[6] * this.r);
				this.transform.transform(this.tmpPoint);
				this.movex = this.lastx = this.tmpPoint.getX();
				this.movey = this.lasty = this.tmpPoint.getY();
				this.movez = this.lastz = this.tmpPoint.getZ();
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty, this.lastz);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				double ctrls[] = BEZIER_CONTROL_POINTS[idx];
				double ppx = this.lastx;
				double ppy = this.lasty;
				double ppz = this.lastz;
				this.tmpPoint.set(this.x + ctrls[0] * this.r, this.y + ctrls[1] * this.r, this.z + ctrls[2] * this.r);
				this.transform.transform(this.tmpPoint);
				double ctrlX1 = this.tmpPoint.getX();
				double ctrlY1 = this.tmpPoint.getY();
				double ctrlZ1 = this.tmpPoint.getZ();
				this.tmpPoint.set(this.x + ctrls[3] * this.r, this.y + ctrls[4] * this.r, this.z + ctrls[5] * this.r);
				this.transform.transform(this.tmpPoint);
				double ctrlX2 = this.tmpPoint.getX();
				double ctrlY2 = this.tmpPoint.getY();
				double ctrlZ2 = this.tmpPoint.getZ();
				this.tmpPoint.set(this.x + ctrls[6] * this.r, this.y + ctrls[7] * this.r, this.z + ctrls[8] * this.r);
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

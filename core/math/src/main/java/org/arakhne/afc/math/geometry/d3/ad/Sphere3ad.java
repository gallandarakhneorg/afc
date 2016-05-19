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
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;
import org.arakhne.afc.math.geometry.d2.afp.InnerComputationPoint2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp.CrossingComputationType;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D circle on a plane.
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
		extends Shape3ad<ST, IT, IE, P, V, B> {

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
	@Pure
	static boolean containsCirclePoint(double cx, double cy, double radius, double px, double py) {
		assert (radius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		return Point2D.getDistanceSquaredPointPoint(
				px, py,
				cx, cy) <= (radius * radius);
	}

	/** Replies if a rectangle is inside in the circle.
	 * 
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param rxmin is the lowest corner of the rectangle.
	 * @param rymin is the lowest corner of the rectangle.
	 * @param rxmax is the uppest corner of the rectangle.
	 * @param rymax is the uppest corner of the rectangle.
	 * @return <code>true</code> if the given rectangle is inside the circle;
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsCircleRectangle(double cx, double cy, double radius, double rxmin, double rymin, double rxmax, double rymax) {
		assert (radius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		assert (rxmin <= rxmax) : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert (rymin <= rymax) : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
		double rcx = (rxmin + rxmax) / 2;
		double rcy = (rymin + rymax) / 2;
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
		return containsCirclePoint(cx, cy, radius, farX, farY);
	}

	/** Replies if two circles are intersecting.
	 * 
	 * @param x1 is the center of the first circle
	 * @param y1 is the center of the first circle
	 * @param radius1 is the radius of the first circle
	 * @param x2 is the center of the second circle
	 * @param y2 is the center of the second circle
	 * @param radius2 is the radius of the second circle
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsCircleCircle(double x1, double y1, double radius1, double x2, double y2, double radius2) {
		assert (radius1 >= 0) : "First circle radius must be positive or zero"; //$NON-NLS-1$
		assert (radius1 >= 0) : "Second circle radius must be positive or zero"; //$NON-NLS-1$
		double r = radius1 + radius2;
		return Point2D.getDistanceSquaredPointPoint(x1, y1, x2, y2) < (r * r);
	}

	/** Replies if a circle and a rectangle are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first corner of the rectangle.
	 * @param y2 is the first corner of the rectangle.
	 * @param x3 is the second corner of the rectangle.
	 * @param y3 is the second corner of the rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsCircleRectangle(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
		assert (radius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
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
		return (dx * dx + dy * dy) < (radius * radius);
	}

	/** Replies if a circle and a line are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first point of the line.
	 * @param y2 is the first point of the line.
	 * @param x3 is the second point of the line.
	 * @param y3 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsCircleLine(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
		assert (radius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		double d = Segment2afp.computeDistanceSquaredLinePoint(x2, y2, x3, y3, x1, y1);
		return d < (radius * radius);
	}

	/** Replies if a circle and a segment are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param x3 is the second point of the segment.
	 * @param y3 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsCircleSegment(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
		assert (radius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		double d = Segment2afp.computeDistanceSquaredSegmentPoint(x2, y2, x3, y3, x1, y1);
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

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	default P getCenter() {
		return getGeomFactory().newPoint(getX(), getY());
	}

	/** Change the center.
	 * 
	 * @param center
	 */
	default void setCenter(Point2D<?, ?> center) {
		assert (center != null) : "Point must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), getRadius());
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 */
	default void setCenter(double x, double y) {
		setX(x);
		setY(y);
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
	
	/** Change the frame of the circle.
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 */
	// Not a default implementation for ensuring atomic change.
	void set(double x, double y, double radius);

	/** Change the frame of the circle.
	 * 
	 * @param center
	 * @param radius
	 */
	default void set(Point2D<?, ?> center, double radius) {
		assert (center != null) : "Point must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), radius);
	}

	@Override
	default void set(IT s) {
		assert (s != null) : "Shape must be not null"; //$NON-NLS-1$
		set(s.getX(), s.getY(), s.getRadius());
	}
	
	@Override
	default void clear() {
		set(0, 0, 0);
	}
	
	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double radius = getRadius();
		box.setFromCorners(
				x - radius, y - radius,
				x + radius, y + radius);
	}

	@Override
	default boolean isEmpty() {
		return MathUtil.isEpsilonZero(getRadius());
	}

	@Pure
	@Override
	default double getDistance(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double d = Point2D.getDistancePointPoint(getX(), getY(), p.getX(), p.getY());
		d = d - getRadius();
		return Math.max(0., d);
	}
	
	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double radius = getRadius();
		double vx = p.getX() - x;
		double vy = p.getY() - y;
		double sqLength = vx * vx + vy * vy;
		double sqRadius = radius * radius;
		if (sqLength <= sqRadius) {
			return 0;
		}
		return Math.max(0., sqLength - 2 * Math.sqrt(sqLength) * radius + sqRadius);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point2D<?, ?> r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point2D<?, ?> r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsCirclePoint(getX(), getY(), getRadius(), x, y);
	}
	
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return containsCircleRectangle(getX(), getY(), getRadius(),
				r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY());
	}
	
	@Override
	default void translate(double dx, double dy) {
		setCenter(getX() + dx, getY() + dy);
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return intersectsCircleRectangle(
				getX(), getY(), getRadius(),
				r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY());
	}

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Ellipse must be not null"; //$NON-NLS-1$
		return Ellipse2afp.intersectsEllipseCircle(
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight(),
				getX(), getY(), getRadius());
	}
	
	@Pure
	@Override
	default boolean intersects(Sphere3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		return intersectsCircleCircle(
				getX(), getY(), getRadius(),
				s.getX(), s.getY(), s.getRadius());
	}
	
	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Triangle must be not null"; //$NON-NLS-1$
		return Triangle2afp.intersectsTriangleCircle(
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2(),
				s.getX3(), s.getY3(),
				getX(), getY(),
				getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		return intersectsCircleSegment(
				getX(), getY(), getRadius(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Oriented rectangle must be not null"; //$NON-NLS-1$
		return OrientedRectangle2afp.intersectsOrientedRectangleCircle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisExtent(),
				getX(), getY(), getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Parallelogram must be not null"; //$NON-NLS-1$
		return Parallelogram2afp.intersectsParallelogramCircle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				getX(), getY(), getRadius());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path3ad.computeCrossingsFromCircle(
				0,
				iterator,
				getX(), getY(), getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s!= null) : "Round rectangle must be not null"; //$NON-NLS-1$
		return RoundRectangle2afp.intersectsRoundRectangleCircle(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				s.getArcWidth(), s.getArcHeight(),
				getX(), getY(), getRadius());
	}
		
	@Pure
	@Override
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double radius = getRadius();
		double vx = p.getX() - x;
		double vy = p.getY() - y;
		double sqLength = vx * vx + vy * vy;
		if (sqLength <= (radius * radius)) {
			return getGeomFactory().convertToPoint(p);
		}
		double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s);
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x = getX();
		double y = getY();
		double vx = x - p.getX();
		double vy = y - p.getY();
		double radius = getRadius();
		double sqLength = vx * vx + vy * vy;
		if (sqLength <= 0.) {
			return getGeomFactory().newPoint(radius, 0);
		}
		double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s);
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform==null || transform.isIdentity()) {
			return new CirclePathIterator<>(this);
		}
		return new TransformedCirclePathIterator<>(this, transform);
	}
	
	@Override
	@Pure
	default double getHorizontalRadius() {
		return getRadius();
	}
	
	@Override
	@Pure
	default double getVerticalRadius() {
		return getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>The circle is set in order to be enclosed inside the given box.
	 * It means that the center of the circle is the center of the box, and the
	 * radius of the circle is the minimum of the demi-width and demi-height.
	 */
	@Override
	default void setFromCenter(double centerX, double centerY, double cornerX, double cornerY) {
		double demiWidth = Math.abs(cornerX - centerX);
		double demiHeight = Math.abs(cornerY- centerY);
		if (demiWidth <= demiHeight) {
			set(centerX, centerY, demiWidth);
		} else {
			set(centerX, centerY, demiHeight);
		}
	}
	
	/** {@inheritDoc}
	 *
	 * <p>The circle is set in order to be enclosed inside the given box.
	 * It means that the center of the circle is the center of the box, and the
	 * radius of the circle is the minimum of the demi-width and demi-height.
	 */
	@Override
	default void setFromCorners(double x1, double y1, double x2, double y2) {
		setFromCenter((x1 + x2) / 2., (y1 + y2) / 2., x2, y2);
	}

	@Override
	default double getMinX() {
		return getX() - getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the maximum X coordinate should not change, the center of
	 * the circle is the point between the new minimum and the current maximum coordinates,
	 * and the radius of the circle is set to the difference between the new minimum and
	 * center coordinates.
	 *
	 * <p>If the new minimum is greater than the current maximum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMinX(double x) {
		double cx = (x + getX() + getRadius()) / 2.;
		double radius = Math.abs(cx - x);
		set(cx, getY(), radius);
	}

	@Override
	default double getMaxX() {
		return getX() + getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the minimum X coordinate should not change, the center of
	 * the circle is the point between the new maximum and the current minimum coordinates,
	 * and the radius of the circle is set to the difference between the new maximum and
	 * center coordinates.
	 *
	 * <p>If the new maximum is lower than the current minimum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMaxX(double x) {
		double cx = (x + getX() - getRadius()) / 2.;
		double radius = Math.abs(cx - x);
		set(cx, getY(), radius);
	}

	@Override
	default double getMinY() {
		return getY() - getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the maximum Y coordinate should not change, the center of
	 * the circle is the point between the new minimum and the current maximum coordinates,
	 * and the radius of the circle is set to the difference between the new minimum and
	 * center coordinates.
	 *
	 * <p>If the new minimum is greater than the current maximum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMinY(double y) {
		double cy = (y + getY() + getRadius()) / 2.;
		double radius = Math.abs(cy - y);
		set(getX(), cy, radius);
	}

	@Override
	default double getMaxY() {
		return getY() + getRadius();
	}

	/** {@inheritDoc}
	 *
	 * <p>Assuming that the minimum Y coordinate should not change, the center of
	 * the circle is the point between the new maximum and the current minimum coordinates,
	 * and the radius of the circle is set to the difference between the new maximum and
	 * center coordinates.
	 *
	 * <p>If the new maximum is lower than the current minimum, the coordinates are automatically
	 * swapped.
	 */
	@Override
	default void setMaxY(double y) {
		double cy = (y + getY() - getRadius()) / 2.;
		double radius = Math.abs(cy - y);
		set(getX(), cy, radius);
	}

	/** Abstract iterator on the path elements of the circle.
	 * 
	 * <h3>Discretization of the circle with Bezier</h3>
	 * 
	 * <p>For n segments on the circle, the optimal distance to the control points, in the sense that the
	 * middle of the curve lies on the circle itself, is (4/3)*tan(pi/(2n)).
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
	abstract class AbstractCirclePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {
		
		/**
		 * Distance from a Bezier curve control point on the circle to the other control point.
		 *
		 * <p>4/3 tan (PI/(2*n)), where n is the number on points on the circle.
		 */ 
		public static final double CTRL_POINT_DISTANCE = 0.5522847498307933;

		/**
		 * Contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 1
		 * centered at (0,0).
		 */
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
		protected final Sphere3ad<?, ?, T, ?, ?, ?> circle;

		/**
		 * @param circle the circle.
		 */
		public AbstractCirclePathIterator(Sphere3ad<?, ?, T, ?, ?, ?> circle) {
			assert (circle != null) : "Circle must be not null"; //$NON-NLS-1$
			this.circle = circle;
		}

		@Override
		public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
			return this.circle.getGeomFactory();
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
	
	/** Iterator on the path elements of the circle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class CirclePathIterator<T extends PathElement2afp> extends AbstractCirclePathIterator<T> {

		private double x;
		
		private double y;
		
		private double r;
		
		private int index;
		
		private double movex;
		
		private double movey;
		
		private double lastx;
		
		private double lasty;
		
		/**
		 * @param circle the circle to iterate on.
		 */
		public CirclePathIterator(Sphere3ad<?, ?, T, ?, ?, ?> circle) {
			super(circle);
			if (circle.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
			} else {
				this.r = circle.getRadius();
				this.x = circle.getX();
				this.y = circle.getY();
				this.index = -1;
			}
		}
		
		@Override
		public PathIterator2afp<T> restartIterations() {
			return new CirclePathIterator<>(this.circle);
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
				this.lastx = this.movex;
				this.lasty = this.movey;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				double ctrls[] = BEZIER_CONTROL_POINTS[idx];
				double ppx = this.lastx;
				double ppy = this.lasty;
				this.lastx = (this.x + ctrls[4] * this.r);
				this.lasty = (this.y + ctrls[5] * this.r);
				return getGeomFactory().newCurvePathElement(
						ppx, ppy,
						(this.x + ctrls[0] * this.r),
						(this.y + ctrls[1] * this.r),
						(this.x + ctrls[2] * this.r),
						(this.y + ctrls[3] * this.r),
						this.lastx, this.lasty);
			}
			return getGeomFactory().newClosePathElement(
					this.lastx, this.lasty,
					this.movex, this.movey);
		}
	
	}

	/** Iterator on the path elements of the circle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedCirclePathIterator<T extends PathElement2afp> extends AbstractCirclePathIterator<T> {
			
		private final Transform2D transform;

		private final Point2D<?, ?> tmpPoint;
		
		private double x;
		
		private double y;
		
		private double r;
		
		private double movex;
		
		private double movey;

		private double lastx;
		
		private double lasty;

		private int index;

		/**
		 * @param circle the iterated circle.
		 * @param transform the transformation to apply.
		 */
		public TransformedCirclePathIterator(Sphere3ad<?, ?, T, ?, ?, ?> circle, Transform2D transform) {
			super(circle);
			assert(transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			if (circle.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
				this.tmpPoint = null;
			} else {
				this.tmpPoint = new InnerComputationPoint2afp();
				this.r = circle.getRadius();
				this.x = circle.getX();
				this.y = circle.getY();
				this.index = -1;
			}
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new TransformedCirclePathIterator<>(this.circle, this.transform);
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
				this.tmpPoint.set(this.x + ctrls[4] * this.r, this.y + ctrls[5] * this.r);
				this.transform.transform(this.tmpPoint);
				this.movex = this.lastx = this.tmpPoint.getX();
				this.movey = this.lasty = this.tmpPoint.getY();
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				double ctrls[] = BEZIER_CONTROL_POINTS[idx];
				double ppx = this.lastx;
				double ppy = this.lasty;
				this.tmpPoint.set(this.x + ctrls[0] * this.r, this.y + ctrls[1] * this.r);
				this.transform.transform(this.tmpPoint);
				double ctrlX1 = this.tmpPoint.getX();
				double ctrlY1 = this.tmpPoint.getY();
				this.tmpPoint.set(this.x + ctrls[2] * this.r, this.y + ctrls[3] * this.r);
				this.transform.transform(this.tmpPoint);
				double ctrlX2 = this.tmpPoint.getX();
				double ctrlY2 = this.tmpPoint.getY();
				this.tmpPoint.set(this.x + ctrls[4] * this.r, this.y + ctrls[5] * this.r);
				this.transform.transform(this.tmpPoint);
				this.lastx = this.tmpPoint.getX();
				this.lasty = this.tmpPoint.getY();
				return getGeomFactory().newCurvePathElement(
						ppx, ppy,
						ctrlX1, ctrlY1,
						ctrlX2, ctrlY2,
						this.lastx, this.lasty);
			}
			return getGeomFactory().newClosePathElement(
					this.lastx, this.lasty,
					this.movex, this.movey);
		}
	
	}

}

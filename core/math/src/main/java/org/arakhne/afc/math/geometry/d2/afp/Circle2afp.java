/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

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
public interface Circle2afp<
		ST extends Shape2afp<?, ?, IE, P, V, B>,
		IT extends Circle2afp<?, ?, IE, P, V, B>,
		IE extends PathElement2afp,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, IE, P, V, B>>
		extends Ellipse2afp<ST, IT, IE, P, V, B> {

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
		assert radius >= 0 : "Circle radius must be positive or zero"; 
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
	 *     otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsCircleRectangle(double cx, double cy, double radius, double rxmin,
			double rymin, double rxmax, double rymax) {
		assert radius >= 0 : "Circle radius must be positive or zero"; 
		assert rxmin <= rxmax : "rxmin must be lower or equal to rxmax"; 
		assert rymin <= rymax : "rymin must be lower or equal to rymax"; 
		final double rcx = (rxmin + rxmax) / 2;
		final double rcy = (rymin + rymax) / 2;
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
		assert radius1 >= 0 : "First circle radius must be positive or zero"; 
		assert radius1 >= 0 : "Second circle radius must be positive or zero"; 
		final double r = radius1 + radius2;
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
		assert radius >= 0 : "Circle radius must be positive or zero"; 
		assert x2 <= x3 : "x2 must be lower or equal to x3"; 
		assert y2 <= y3 : "y2 must be lower or equal to y3"; 
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
		assert radius >= 0 : "Circle radius must be positive or zero"; 
		final double d = Segment2afp.computeDistanceSquaredLinePoint(x2, y2, x3, y3, x1, y1);
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
		assert radius >= 0 : "Circle radius must be positive or zero"; 
		final double d = Segment2afp.computeDistanceSquaredSegmentPoint(x2, y2, x3, y3, x1, y1);
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

	@Pure
	@Override
	default P getCenter() {
		return getGeomFactory().newPoint(getX(), getY());
	}

	/** Change the center.
	 *
	 * @param center the center point.
	 */
	default void setCenter(Point2D<?, ?> center) {
		assert center != null : "Point must be not null"; 
		set(center.getX(), center.getY(), getRadius());
	}

	/** Change the center.
	 *
	 * @param x x coordinate of the center point.
	 * @param y y coordinate of the center point.
	 */
	default void setCenter(double x, double y) {
		setX(x);
		setY(y);
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
	 * @param x x coordinate of the center point.
	 * @param y y coordinate of the center point.
	 * @param radius the radius.
	 */
	// Not a default implementation for ensuring atomic change.
	void set(double x, double y, double radius);

	/** Change the frame of the circle.
	 *
	 * @param center the center point.
	 * @param radius the radius.
	 */
	default void set(Point2D<?, ?> center, double radius) {
		assert center != null : "Point must be not null"; 
		set(center.getX(), center.getY(), radius);
	}

	@Override
	default void set(IT shape) {
		assert shape != null : "Shape must be not null"; 
		set(shape.getX(), shape.getY(), shape.getRadius());
	}

	@Override
	default void clear() {
		set(0, 0, 0);
	}

	@Override
	default void toBoundingBox(B box) {
		assert box != null : "Rectangle must be not null"; 
		final double x = getX();
		final double y = getY();
		final double radius = getRadius();
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
	default double getDistance(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; 
		double distance = Point2D.getDistancePointPoint(getX(), getY(), pt.getX(), pt.getY());
		distance = distance - getRadius();
		return Math.max(0., distance);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; 
		final double x = getX();
		final double y = getY();
		final double radius = getRadius();
		final double vx = pt.getX() - x;
		final double vy = pt.getY() - y;
		final double sqLength = vx * vx + vy * vy;
		final double sqRadius = radius * radius;
		if (sqLength <= sqRadius) {
			return 0;
		}
		return Math.max(0., sqLength - 2 * Math.sqrt(sqLength) * radius + sqRadius);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; 
		final Point2D<?, ?> r = getClosestPointTo(pt);
		return r.getDistanceL1(pt);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; 
		final Point2D<?, ?> r = getClosestPointTo(pt);
		return r.getDistanceLinf(pt);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsCirclePoint(getX(), getY(), getRadius(), x, y);
	}

	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : "Rectangle must be not null"; 
		return containsCircleRectangle(getX(), getY(), getRadius(),
				rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY());
	}

	@Override
	default void translate(double dx, double dy) {
		setCenter(getX() + dx, getY() + dy);
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : "Rectangle must be not null"; 
		return intersectsCircleRectangle(
				getX(), getY(), getRadius(),
				rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY());
	}

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : "Ellipse must be not null"; 
		return Ellipse2afp.intersectsEllipseCircle(
				ellipse.getMinX(), ellipse.getMinY(),
				ellipse.getWidth(), ellipse.getHeight(),
				getX(), getY(), getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : "Circle must be not null"; 
		return intersectsCircleCircle(
				getX(), getY(), getRadius(),
				circle.getX(), circle.getY(), circle.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : "Triangle must be not null"; 
		return Triangle2afp.intersectsTriangleCircle(
				triangle.getX1(), triangle.getY1(),
				triangle.getX2(), triangle.getY2(),
				triangle.getX3(), triangle.getY3(),
				getX(), getY(),
				getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must be not null"; 
		return intersectsCircleSegment(
				getX(), getY(), getRadius(),
				segment.getX1(), segment.getY1(),
				segment.getX2(), segment.getY2());
	}

	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		assert orientedRectangle != null : "Oriented rectangle must be not null"; 
		return OrientedRectangle2afp.intersectsOrientedRectangleCircle(
				orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
				orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(), orientedRectangle.getFirstAxisExtent(),
				orientedRectangle.getSecondAxisExtent(),
				getX(), getY(), getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : "Parallelogram must be not null"; 
		return Parallelogram2afp.intersectsParallelogramCircle(
				parallelogram.getCenterX(), parallelogram.getCenterY(),
				parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(), parallelogram.getFirstAxisExtent(),
				parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(), parallelogram.getSecondAxisExtent(),
				getX(), getY(), getRadius());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		assert iterator != null : "Iterator must be not null"; 
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path2afp.computeCrossingsFromCircle(
				0,
				iterator,
				getX(), getY(), getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : "Round rectangle must be not null"; 
		return RoundRectangle2afp.intersectsRoundRectangleCircle(
				roundRectangle.getMinX(), roundRectangle.getMinY(),
				roundRectangle.getMaxX(), roundRectangle.getMaxY(),
				roundRectangle.getArcWidth(), roundRectangle.getArcHeight(),
				getX(), getY(), getRadius());
	}

	@Pure
	@Override
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : "MultiShape must be not null"; 
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; 
		final double x = getX();
		final double y = getY();
		final double radius = getRadius();
		final double vx = pt.getX() - x;
		final double vy = pt.getY() - y;
		final double sqLength = vx * vx + vy * vy;
		if (sqLength <= (radius * radius)) {
			return getGeomFactory().convertToPoint(pt);
		}
		final double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s);
	}

	@Pure
	@Override
	default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : "Ellipse must be not null"; 
		final Point2D<?, ?> point = ellipse.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : "Circle must be not null"; 
		final Point2D<?, ?> point = circle.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : "Rectangle must be not null"; 
		final Point2D<?, ?> point = rectangle.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must be not null"; 
		final Point2D<?, ?> point = segment.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : "Triangle must be not null"; 
		final Point2D<?, ?> point = triangle.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : "Path must be not null"; 
		final Point2D<?, ?> point = path.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		assert orientedRectangle != null : "Oriented rectangle must be not null"; 
		final Point2D<?, ?> point = orientedRectangle.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : "Parallelogram must be not null"; 
		final Point2D<?, ?> point = parallelogram.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : "Round rectangle must be not null"; 
		final Point2D<?, ?> point = roundRectangle.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getClosestPointTo(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : "Multishape must be not null"; 
		final Point2D<?, ?> point = multishape.getClosestPointTo(getCenter());
		return getClosestPointTo(point);
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; 
		final double x = getX();
		final double y = getY();
		final double vx = x - pt.getX();
		final double vy = y - pt.getY();
		final double radius = getRadius();
		final double sqLength = vx * vx + vy * vy;
		if (sqLength <= 0.) {
			return getGeomFactory().newPoint(radius, 0);
		}
		final double s = radius / Math.sqrt(sqLength);
		return getGeomFactory().newPoint(x + vx * s, y + vy * s);
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
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
		final double demiWidth = Math.abs(cornerX - centerX);
		final double demiHeight = Math.abs(cornerY - centerY);
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
		final double cx = (x + getX() + getRadius()) / 2.;
		final double radius = Math.abs(cx - x);
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
		final double cx = (x + getX() - getRadius()) / 2.;
		final double radius = Math.abs(cx - x);
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
		final double cy = (y + getY() + getRadius()) / 2.;
		final double radius = Math.abs(cy - y);
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
		final double cy = (y + getY() - getRadius()) / 2.;
		final double radius = Math.abs(cy - y);
		set(getX(), cy, radius);
	}

	/** Abstract iterator on the path elements of the circle.
	 *
	 * <h3>Discretization of the circle with Bezier</h3>
	 *
	 * <p>For n segments on the circle, the optimal distance to the control points, in the sense that the
	 * middle of the curve lies on the circle itself, is (4/3)*tan(pi/(2n)).
	 *
	 * <p><img src="./doc-files/circlebezier.png" width="100%" />
	 *
	 * <p>In the case of a discretization with 4 bezier curves, the distance is is
	 * (4/3)*tan(pi/8) = 4*(sqrt(2)-1)/3 = 0.552284749831.
	 *
	 * <p><img src="./doc-files/circlepathiterator.png" width="100%" />
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
		 * centered at (0, 0).
		 */
		public static final double[][] BEZIER_CONTROL_POINTS = {
			// First quarter: max x, max y.
			{1, CTRL_POINT_DISTANCE, CTRL_POINT_DISTANCE, 1, 0, 1},
			// Second quarter: min x, max y.
			{-CTRL_POINT_DISTANCE, 1, -1, CTRL_POINT_DISTANCE, -1, 0},
			// Third quarter: min x, min y.
			{-1, -CTRL_POINT_DISTANCE, -CTRL_POINT_DISTANCE, -1, 0, -1},
			// Fourth quarter: max x, min y.
			{CTRL_POINT_DISTANCE, -1, 1, -CTRL_POINT_DISTANCE, 1, 0},
		};

		/** 4 segments + close.
		 */
		protected static final int NUMBER_ELEMENTS = 5;

		/** The iterated shape.
		 */
		protected final Circle2afp<?, ?, T, ?, ?, ?> circle;

		/**
		 * @param circle the circle.
		 */
		public AbstractCirclePathIterator(Circle2afp<?, ?, T, ?, ?, ?> circle) {
			assert circle != null : "Circle must be not null"; 
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

		private double radius;

		private int index;

		private double movex;

		private double movey;

		private double lastx;

		private double lasty;

		/**
		 * @param circle the circle to iterate on.
		 */
		public CirclePathIterator(Circle2afp<?, ?, T, ?, ?, ?> circle) {
			super(circle);
			if (circle.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
			} else {
				this.radius = circle.getRadius();
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
		@SuppressWarnings("checkstyle:magicnumber")
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
				this.lastx = this.movex;
				this.lasty = this.movey;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				final double[] ctrls = BEZIER_CONTROL_POINTS[idx];
				final double ppx = this.lastx;
				final double ppy = this.lasty;
				this.lastx = this.x + ctrls[4] * this.radius;
				this.lasty = this.y + ctrls[5] * this.radius;
				return getGeomFactory().newCurvePathElement(
						ppx, ppy,
						this.x + ctrls[0] * this.radius,
						this.y + ctrls[1] * this.radius,
						this.x + ctrls[2] * this.radius,
						this.y + ctrls[3] * this.radius,
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

		private double radius;

		private double movex;

		private double movey;

		private double lastx;

		private double lasty;

		private int index;

		/**
		 * @param circle the iterated circle.
		 * @param transform the transformation to apply.
		 */
		public TransformedCirclePathIterator(Circle2afp<?, ?, T, ?, ?, ?> circle, Transform2D transform) {
			super(circle);
			assert transform != null : "Transformation must be not null"; 
			this.transform = transform;
			if (circle.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
				this.tmpPoint = null;
			} else {
				this.tmpPoint = new InnerComputationPoint2afp();
				this.radius = circle.getRadius();
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
		@SuppressWarnings("checkstyle:magicnumber")
		public T next() {
			if (this.index >= NUMBER_ELEMENTS) {
				throw new NoSuchElementException();
			}
			final int idx = this.index;
			++this.index;
			if (idx < 0) {
				final double[] ctrls = BEZIER_CONTROL_POINTS[3];
				this.tmpPoint.set(this.x + ctrls[4] * this.radius, this.y + ctrls[5] * this.radius);
				this.transform.transform(this.tmpPoint);
				this.movex = this.tmpPoint.getX();
				this.lastx = this.movex;
				this.movey = this.tmpPoint.getY();
				this.lasty = this.movey;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			if (idx < (NUMBER_ELEMENTS - 1)) {
				final double[] ctrls = BEZIER_CONTROL_POINTS[idx];
				final double ppx = this.lastx;
				final double ppy = this.lasty;
				this.tmpPoint.set(this.x + ctrls[0] * this.radius, this.y + ctrls[1] * this.radius);
				this.transform.transform(this.tmpPoint);
				final double ctrlX1 = this.tmpPoint.getX();
				final double ctrlY1 = this.tmpPoint.getY();
				this.tmpPoint.set(this.x + ctrls[2] * this.radius, this.y + ctrls[3] * this.radius);
				this.transform.transform(this.tmpPoint);
				final double ctrlX2 = this.tmpPoint.getX();
				final double ctrlY2 = this.tmpPoint.getY();
				this.tmpPoint.set(this.x + ctrls[4] * this.radius, this.y + ctrls[5] * this.radius);
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

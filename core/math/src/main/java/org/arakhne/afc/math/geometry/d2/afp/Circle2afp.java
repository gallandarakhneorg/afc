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

package org.arakhne.afc.math.geometry.d2.afp;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D circle on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Circle2afp<
		ST extends Shape2afp<?, ?, IE, P, B>,
		IT extends Circle2afp<?, ?, IE, P, B>,
		IE extends PathElement2afp,
		P extends Point2D,
		B extends Rectangle2afp<?, ?, IE, P, B>>
		extends Shape2afp<ST, IT, IE, P, B> {

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
		return Point2D.getDistanceSquaredPointPoint(
				px, py,
				cx, cy) <= (radius * radius);
	}

	/** Replies if a rectangle is inside in the circle.
	 * 
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param rx is the lowest corner of the rectangle.
	 * @param ry is the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @return <code>true</code> if the given rectangle is inside the circle;
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsCircleRectangle(double cx, double cy, double radius, double rx, double ry, double rwidth, double rheight) {
		double rcx = (rx + rwidth/2f);
		double rcy = (ry + rheight/2f);
		double farX;
		if (cx<=rcx) farX = rx + rwidth;
		else farX = rx;
		double farY;
		if (cy<=rcy) farY = ry + rheight;
		else farY = ry;
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
		double r = radius1+radius2;
		return Point2D.getDistanceSquaredPointPoint(x1, y1, x2, y2) < (r*r);
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
		double dx;
		if (x1<x2) {
			dx = x2 - x1;
		}
		else if (x1>x3) {
			dx = x1 - x3;
		}
		else {
			dx = 0f;
		}
		double dy;
		if (y1<y2) {
			dy = y2 - y1;
		}
		else if (y1>y3) {
			dy = y1 - y3;
		}
		else {
			dy = 0f;
		}
		return (dx*dx+dy*dy) < (radius*radius);
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
		double d = Segment2afp.getDistanceSquaredLinePoint(x2, y2, x3, y3, x1, y1);
		return d<(radius*radius);
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
		double d = Segment2afp.getDistanceSquaredSegmentPoint(x2, y2, x3, y3, x1, y1, null);
		return d<(radius*radius);
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
	default void setCenter(Point2D center) {
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
	default void set(Point2D center, double radius) {
		set(center.getX(), center.getY(), radius);
	}

	@Override
	default void set(IT s) {
		Rectangle2afp<?, ?, ?, ?, ?> box = s.toBoundingBox();
		set(box.getCenterX(), box.getCenterX(), Math.min(box.getWidth(), box.getHeight()) / 2);
	}
	
	@Override
	default void clear() {
		set(0, 0, 0);
	}
	
	@Override
	default void toBoundingBox(B box) {
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
	default double getDistance(Point2D p) {
		double d = Point2D.getDistancePointPoint(getX(), getY(), p.getX(), p.getY()) - getRadius();
		return Math.max(0., d);
	}
	
	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		double d = Point2D.getDistanceSquaredPointPoint(getX(), getY(), p.getX(), p.getY()) - getRadius();
		return Math.max(0., d);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsCirclePoint(getX(), getY(), getRadius(), x, y);
	}
	
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		return containsCircleRectangle(getX(), getY(), getRadius(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}
	
	@Override
	default void translate(double dx, double dy) {
		setCenter(getX() + dx, getY() + dy);
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> r) {
		return intersectsCircleRectangle(
				getX(), getY(), getRadius(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		double x = getX();
		double y = getY();
		double r = getRadius();
		return Ellipse2afp.intersectsEllipseEllipse(
				x - r, y - r,
				x + r, y + r,
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}
	
	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		return intersectsCircleCircle(
				getX(), getY(), getRadius(),
				s.getX(), s.getY(), s.getRadius());
	}
	
	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		return intersectsCircleSegment(
				getX(), getY(), getRadius(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		return OrientedRectangle2afp.intersectsOrientedRectangleSolidCircle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				getX(), getY(), getRadius());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2afp.computeCrossingsFromCircle(
				0,
				iterator,
				getX(), getY(), getRadius(),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		return s.intersects(this);
	}
		
	@Pure
	@Override
	default P getClosestPointTo(Point2D p) {
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
	default P getFarthestPointTo(Point2D p) {
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

	/** Abstract iterator on the path elements of the circle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractCirclePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {
		
		/**
		 * ArcIterator.btan(Math.PI/2)
		 */ 
		protected static final double CTRL_VAL = 0.5522847498307933;

		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static final double PCV = 0.5 + CTRL_VAL * 0.5;
	
		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static final double NCV = 0.5 - CTRL_VAL * 0.5;
		
		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static double CTRL_PTS[][] = {
			{  1.0,  PCV,  PCV,  1.0,  0.5,  1.0 },
			{  NCV,  1.0,  0.0,  PCV,  0.0,  0.5 },
			{  0.0,  NCV,  NCV,  0.0,  0.5,  0.0 },
			{  PCV,  0.0,  1.0,  NCV,  1.0,  0.5 }
		};
		
		private final GeomFactory2afp<T, ?, ?> factory;

		/**
		 * @param factory the factory.
		 */
		public AbstractCirclePathIterator(GeomFactory2afp<T, ?, ?> factory) {
			this.factory = factory;
		}

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.factory;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
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
		
		private int index = 0;
		
		private double movex;
		
		private double movey;
		
		private double lastx;
		
		private double lasty;
		
		/**
		 * @param circle the circle to iterate on.
		 */
		public CirclePathIterator(Circle2afp<?, ?, T, ?, ?> circle) {
			super(circle.getGeomFactory());
			if (circle.isEmpty()) {
				this.index = 6;
			} else {
				this.r = circle.getRadius();
				this.x = circle.getX();
				this.y = circle.getY();
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 5;
		}
	
		@Override
		public T next() {
			if (this.index > 5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			if (idx==0) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[3];
				this.movex = (this.x + ctrls[4] * dr);
				this.movey = (this.y + ctrls[5] * dr);
				this.lastx = this.movex;
				this.lasty = this.movey;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			else if (idx<5) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				double ppx = this.lastx;
				double ppy = this.lasty;
				this.lastx = (this.x + ctrls[4] * dr);
				this.lasty = (this.y + ctrls[5] * dr);
				return getGeomFactory().newCurvePathElement(
						ppx, ppy,
						(this.x + ctrls[0] * dr),
						(this.y + ctrls[1] * dr),
						(this.x + ctrls[2] * dr),
						(this.y + ctrls[3] * dr),
						this.lastx, this.lasty);
			}
			double ppx = this.lastx;
			double ppy = this.lasty;
			this.lastx = this.movex;
			this.lasty = this.movey;
			return getGeomFactory().newClosePathElement(
					ppx, ppy,
					this.lastx, this.lasty);
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

		private Point2D p1;
		
		private Point2D p2;
		
		private Point2D ptmp1;
		
		private Point2D ptmp2;
		
		private double x;
		
		private double y;
		
		private double r;
		
		private double movex, movey;
		
		private int index = 0;

		/**
		 * @param circle the iterated circle.
		 * @param transform the transformation to apply.
		 */
		public TransformedCirclePathIterator(Circle2afp<?, ?, T, ?, ?> circle, Transform2D transform) {
			super(circle.getGeomFactory());
			assert(transform!=null);
			this.transform = transform;
			if (this.r<=0f) {
				this.index = 6;
			} else {
				GeomFactory2afp<T, ?, ?> factory = getGeomFactory();
				this.p1 = factory.newPoint();
				this.p2 = factory.newPoint();
				this.ptmp1 = factory.newPoint();
				this.ptmp2 = factory.newPoint();
				this.r = circle.getRadius();
				this.x = circle.getX() - this.r;
				this.y = circle.getY() - this.r;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}
	
		@Override
		public T next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			if (idx==0) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[3];
				this.movex = (this.x + ctrls[4] * dr);
				this.movey = (this.y + ctrls[5] * dr);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				return getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY());
			}
			else if (idx<5) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				this.p1.set(this.p2);
				this.p2.set(
						(this.x + ctrls[4] * dr),
						(this.y + ctrls[5] * dr));
				this.transform.transform(this.p2);
				this.ptmp1.set(
						(this.x + ctrls[0] * dr),
						(this.y + ctrls[1] * dr));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						(this.x + ctrls[2] * dr),
						(this.y + ctrls[3] * dr));
				this.transform.transform(this.ptmp2);
				return getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.p2.getX(), this.p2.getY());
			}
			this.p1.set(this.p2);
			this.p2.set(this.movex, this.movey);
			this.transform.transform(this.p2);
			return getGeomFactory().newClosePathElement(
					this.p1.getX(), this.p1.getY(),
					this.p2.getX(), this.p2.getY());
		}
	
	}

}

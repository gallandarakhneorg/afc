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
package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;



/** 2D circle with floating-point points.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Circle2f extends AbstractShape2f<Circle2f> {

	private static final long serialVersionUID = -5535463117356287850L;

	/**
	 * ArcIterator.btan(Math.PI/2)
	 */
	private static final double CTRL_VAL = 0.5522847498307933f;

	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	private static final double PCV = 0.5f + CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	private static final double NCV = 0.5f - CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	private static double CTRL_PTS[][] = {
		{  1.0f,  PCV,  PCV,  1.0f,  0.5f,  1.0f },
		{  NCV,  1.0f,  0.0f,  PCV,  0.0f,  0.5f },
		{  0.0f,  NCV,  NCV,  0.0f,  0.5f,  0.0f },
		{  PCV,  0.0f,  1.0f,  NCV,  1.0f,  0.5f }
	};

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
	public static boolean containsCirclePoint(double cx, double cy, double radius, double px, double py) {
		return Point2f.distanceSquaredPointPoint(
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
	public static boolean containsCircleRectangle(double cx, double cy, double radius, double rx, double ry, double rwidth, double rheight) {
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
	public static boolean intersectsCircleCircle(double x1, double y1, double radius1, double x2, double y2, double radius2) {
		double r = radius1+radius2;
		return Point2f.distanceSquaredPointPoint(x1, y1, x2, y2) < (r*r);
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
	public static boolean intersectsCircleRectangle(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
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
	public static boolean intersectsCircleLine(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
		double d = Segment2f.distanceSquaredLinePoint(x2, y2, x3, y3, x1, y1);
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
	public static boolean intersectsCircleSegment(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
		double d = Segment2f.distanceSquaredSegmentPoint(x2, y2, x3, y3, x1, y1, null);
		return d<(radius*radius);
	}

	/** X-coordinate of the circle center. */
	protected double cx = 0f;
	/** Y-coordinate of the circle center. */
	protected double cy = 0f;
	/** Radius of the circle center (must be always positive). */
	protected double radius = 0f;

	/**
	 */
	public Circle2f() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Circle2f(Point2D center, double radius) {
		set(center, radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle2f(double x, double y, double radius) {
		set(x, y, radius);
	}
	
	/** Construct a circle from a circle.
	 * @param c
	 */
	public Circle2f(Circle2f c) {
		this.cx = c.cx;
		this.cy = c.cy;
		this.radius = c.radius;
	}

	@Override
	public void clear() {
		this.cx = this.cy = 0f;
		this.radius = 0f;
	}
	
	/** Replies if the circle is empty.
	 * The circle is empty when the radius is nul.
	 * 
	 * @return <code>true</code> if the radius is nul;
	 * otherwise <code>false</code>.
	 */
	@Override
	public boolean isEmpty() {
		return this.radius<=+0f;
	}

	/** Change the frame of the circle.
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void set(double x, double y, double radius) {
		this.cx = x;
		this.cy = y;
		this.radius = Math.abs(radius);
	}

	/** Change the frame of te circle.
	 * 
	 * @param center
	 * @param radius
	 */
	public void set(Point2D center, double radius) {
		this.cx = center.getX();
		this.cy = center.getY();
		this.radius = Math.abs(radius);
	}

	@Override
	public void set(Shape2f s) {
		if (s instanceof Circle2f) {
			Circle2f c = (Circle2f) s;
			set(c.getX(), c.getY(), c.getRadius());
		} else {
			Rectangle2f r = s.toBoundingBox();
			set(r.getCenterX(), r.getCenterY(),
					Math.min(r.getWidth(), r.getHeight()) / 2.);
		}
	}

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	public double getX() {
		return this.cx;
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	public double getY() {
		return this.cy;
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	public Point2f getCenter() {
		return new Point2f(this.cx, this.cy);
	}

	/** Change the center.
	 * 
	 * @param center
	 */
	public void setCenter(Point2D center) {
		this.cx = center.getX();
		this.cy = center.getY();
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCenter(double x, double y) {
		this.cx = x;
		this.cy = y;
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	public double getRadius() {
		return this.radius;
	}

	/** Set the radius.
	 * 
	 * @param radius is the radius.
	 */
	public void setRadius(double radius) {
		this.radius = Math.abs(radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2f toBoundingBox() {
		Rectangle2f r = new Rectangle2f();
		r.setFromCorners(
				this.cx-this.radius,
				this.cy-this.radius,
				this.cx+this.radius,
				this.cy+this.radius);
		return r;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void toBoundingBox(Rectangle2f box) {
		box.setFromCorners(this.cx-this.radius,
				this.cy-this.radius,
				this.cx+this.radius,
				this.cy+this.radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distance(Point2D p) {
		double d = Point2f.distancePointPoint(getX(), getY(), p.getX(), p.getY()) - getRadius();
		return Math.max(0., d);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double distanceSquared(Point2D p) {
		double d = Point2f.distanceSquaredPointPoint(getX(), getY(), p.getX(), p.getY()) - getRadius();
		return Math.max(0., d);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceL1(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceLinf(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(double x, double y) {
		return containsCirclePoint(getX(), getY(), getRadius(), x, y);
	}
	
	@Override
	public boolean contains(Rectangle2f r) {
		return containsCircleRectangle(getX(), getY(), getRadius(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getClosestPointTo(Point2D p) {
		Vector2f v = new Vector2f(p);
		v.sub(this.cx, this.cy);
		double l = v.lengthSquared();
		if (l<=(this.radius*this.radius)) {
			if (p instanceof Point2f) return (Point2f)p;
			return new Point2f(p);
		}
		double s = this.radius/Math.sqrt(l);
		v.scale(s);
		return new Point2f(this.cx + v.getX(), this.cy + v.getY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getFarthestPointTo(Point2D p) {
		Vector2f v = new Vector2f(
				this.cx - p.getX(),
				this.cy - p.getY());
		v.setLength(this.radius);
		return new Point2f(this.cx + v.getX(), this.cy + v.getY());
	}

	@Override
	public void translate(double dx, double dy) {
		this.cx += dx;
		this.cy += dy;
	}
	
	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform==null)
			return new CopyPathIterator(getX(), getY(), getRadius());
		return new TransformPathIterator(getX(), getY(), getRadius(), transform);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Circle2f) {
			Circle2f rr2d = (Circle2f) obj;
			return ((getX() == rr2d.getX()) &&
					(getY() == rr2d.getY()) &&
					(getRadius() == rr2d.getRadius()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getX());
		bits = 31L * bits + doubleToLongBits(getY());
		bits = 31L * bits + doubleToLongBits(getRadius());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return intersectsCircleRectangle(
				getX(), getY(), getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return Ellipse2f.intersectsEllipseEllipse(
				getX()-getRadius(), getY()-getRadius(),
				getX()+getRadius(), getY()+getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return intersectsCircleCircle(
				getX(), getY(), getRadius(),
				s.getX(), s.getY(), s.getRadius());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return intersectsCircleSegment(
				getX(), getY(), getRadius(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}
	
	@Override
	public boolean intersects(PathIterator2f s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromCircle(
				0,
				s,
				getX(), getY(), getRadius(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return OrientedRectangle2f.intersectsOrientedRectangleSolidCircle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				this.cx, this.cy, this.radius);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX());
		b.append(";"); //$NON-NLS-1$
		b.append(getY());
		b.append(";"); //$NON-NLS-1$
		b.append(getRadius());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator implements PathIterator2f {
		
		private final double x;
		private final double y;
		private final double r;
		private int index = 0;
		private double movex, movey;
		private double lastx, lasty;
		
		/**
		 * @param x
		 * @param y
		 * @param r
		 */
		public CopyPathIterator(double x, double y, double r) {
			this.r = Math.max(0f, r);
			this.x = x - this.r;
			this.y = y - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}
	
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}
	
		@SuppressWarnings("synthetic-access")
		@Override
		public PathElement2f next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			if (idx==0) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[3];
				this.movex = (this.x + ctrls[4] * dr);
				this.movey = (this.y + ctrls[5] * dr);
				this.lastx = this.movex;
				this.lasty = this.movey;
				return new PathElement2f.MovePathElement2f(
						this.lastx, this.lasty);
			}
			else if (idx<5) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				double ppx = this.lastx;
				double ppy = this.lasty;
				this.lastx = (this.x + ctrls[4] * dr);
				this.lasty = (this.y + ctrls[5] * dr);
				return new PathElement2f.CurvePathElement2f(
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
			return new PathElement2f.ClosePathElement2f(
					ppx, ppy,
					this.lastx, this.lasty);
		}
	
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}
		
		@Override
		public boolean isPolyline() {
			return false;
		}

	}
	
	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator implements PathIterator2f {
			
		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		private final Point2D ptmp1 = new Point2f();
		private final Point2D ptmp2 = new Point2f();
		private final Transform2D transform;
		private final double x;
		private final double y;
		private final double r;
		private int index = 0;
		private double movex, movey;
		
		/**
		 * @param x
		 * @param y
		 * @param r
		 * @param transform
		 */
		public TransformPathIterator(double x, double y, double r, Transform2D transform) {
			assert(transform!=null);
			this.transform = transform;
			this.r = Math.max(0f, r);
			this.x = x - this.r;
			this.y = y - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}
	
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}
	
		@SuppressWarnings("synthetic-access")
		@Override
		public PathElement2f next() {
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
				return new PathElement2f.MovePathElement2f(
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
				return new PathElement2f.CurvePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.p2.getX(), this.p2.getY());
			}
			this.p1.set(this.p2);
			this.p2.set(this.movex, this.movey);
			this.transform.transform(this.p2);
			return new PathElement2f.ClosePathElement2f(
					this.p1.getX(), this.p1.getY(),
					this.p2.getX(), this.p2.getY());
		}
	
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Override
		public boolean isPolyline() {
			return false;
		}

	}

}
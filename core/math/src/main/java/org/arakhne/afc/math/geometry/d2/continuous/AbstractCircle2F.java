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
import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 
 * @author $Author: hjaffali$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCircle2F<T extends Shape2F> extends AbstractShape2F<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5472002757745805926L;

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
	@Pure
	public static boolean containsCirclePoint(double cx, double cy, double radius, double px, double py) {
		return FunctionalPoint2D.distanceSquaredPointPoint(
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
	@Pure
	public static boolean intersectsCircleCircle(double x1, double y1, double radius1, double x2, double y2, double radius2) {
		double r = radius1+radius2;
		return FunctionalPoint2D.distanceSquaredPointPoint(x1, y1, x2, y2) < (r*r);
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
	@Pure
	public static boolean intersectsCircleLine(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
		double d = AbstractSegment2F.distanceSquaredLinePoint(x2, y2, x3, y3, x1, y1);
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
	public static boolean intersectsCircleSegment(double x1, double y1, double radius, double x2, double y2, double x3, double y3) {
		double d = AbstractSegment2F.distanceSquaredSegmentPoint(x2, y2, x3, y3, x1, y1, null);
		return d<(radius*radius);
	}
	
	/** Replies if the circle is empty.
	 * The circle is empty when the radius is nul.
	 * 
	 * @return <code>true</code> if the radius is nul;
	 * otherwise <code>false</code>.
	 */
	@Pure
	@Override
	public boolean isEmpty() {
		return this.getRadius()<=+0f;
	}

	/** Change the frame of the circle.
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 */
	abstract public void set(double x, double y, double radius);

	/** Change the frame of the circle.
	 * 
	 * @param center
	 * @param radius
	 */
	abstract public void set(Point2D center, double radius);
	
	@Override
	abstract public void set(Shape2F s);

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	@Pure
	abstract public double getX();

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	abstract public double getY();

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	abstract public Point2f getCenter();

	/** Change the center.
	 * 
	 * @param center
	 */
	abstract public void setCenter(Point2D center);

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 */
	abstract public void setCenter(double x, double y);

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	@Pure
	abstract public double getRadius();

	/** Set the radius.
	 * 
	 * @param radius is the radius.
	 */
	abstract public void setRadius(double radius);

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Rectangle2f toBoundingBox() {
		Rectangle2f r = new Rectangle2f();
		r.setFromCorners(
				this.getX()-this.getRadius(),
				this.getY()-this.getRadius(),
				this.getX()+this.getRadius(),
				this.getY()+this.getRadius());
		return r;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public void toBoundingBox(AbstractRectangle2F<?> box) {
		box.setFromCorners(this.getX()-this.getRadius(),
				this.getY()-this.getRadius(),
				this.getX()+this.getRadius(),
				this.getY()+this.getRadius());
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distance(Point2D p) {
		double d = FunctionalPoint2D.distancePointPoint(this.getX(), this.getY(), p.getX(), p.getY()) - this.getRadius();
		return Math.max(0., d);
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceSquared(Point2D p) {
		double d = FunctionalPoint2D.distanceSquaredPointPoint(this.getX(), this.getY(), p.getX(), p.getY()) - this.getRadius();
		return Math.max(0., d);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceL1(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceLinf(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean contains(double x, double y) {
		return containsCirclePoint(this.getX(), this.getY(), this.getRadius(), x, y);
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean contains(AbstractRectangle2F<?> r) {
		return containsCircleRectangle(this.getX(), this.getY(), this.getRadius(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point2f getClosestPointTo(Point2D p) {
		Vector2f v = new Vector2f(p);
		v.sub(this.getX(), this.getY());
		double l = v.lengthSquared();
		if (l<=(this.getRadius()*this.getRadius())) {
			if (p instanceof Point2f) return (Point2f)p;
			return new Point2f(p);
		}
		double s = this.getRadius()/Math.sqrt(l);
		v.scale(s);
		return new Point2f(this.getX() + v.getX(), this.getY() + v.getY());
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point2f getFarthestPointTo(Point2D p) {
		Vector2f v = new Vector2f(
				this.getX() - p.getX(),
				this.getY() - p.getY());
		v.setLength(this.getRadius());
		return new Point2f(this.getX() + v.getX(), this.getY() + v.getY());
	}

	@Override
	public void translate(double dx, double dy) {
		this.set(this.getX() + dx,this.getY() + dy,this.getRadius());
	}

	@Pure
	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform==null)
			return new CopyPathIterator2f(this.getX(), this.getY(), this.getRadius());
		return new TransformPathIterator2f(this.getX(), this.getY(), this.getRadius(), transform);
	}

	@Pure
	@Override
	public PathIterator2d getPathIteratorProperty(Transform2D transform) {
		if (transform==null)
			return new CopyPathIterator2d(this.getX(), this.getY(), this.getRadius());
		return new TransformPathIterator2d(this.getX(), this.getY(), this.getRadius(), transform);
	}
	
	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof AbstractCircle2F) {
			AbstractCircle2F<?> rr2d = (AbstractCircle2F<?>) obj;
			return ((this.getX() == rr2d.getX()) &&
					(this.getY() == rr2d.getY()) &&
					(this.getRadius() == rr2d.getRadius()));
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(this.getX());
		bits = 31L * bits + doubleToLongBits(this.getY());
		bits = 31L * bits + doubleToLongBits(this.getRadius());
		return (int) (bits ^ (bits >> 32));
	}

	@Pure
	@Override
	public boolean intersects(AbstractRectangle2F<?> s) {
		return intersectsCircleRectangle(
				this.getX(), this.getY(), this.getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractEllipse2F<?> s) {
		return AbstractEllipse2F.intersectsEllipseEllipse(
				this.getX()-this.getRadius(),this.getY()-this.getRadius(),
				this.getX()+this.getRadius(), this.getY()+this.getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractCircle2F<?> s) {
		return intersectsCircleCircle(
				this.getX(), this.getY(), this.getRadius(),
				s.getX(), s.getY(), s.getRadius());
	}

	@Pure
	@Override
	public boolean intersects(AbstractSegment2F<?> s) {
		return intersectsCircleSegment(
				this.getX(), this.getY(), this.getRadius(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Pure
	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}
	
	@Pure
	@Override
	public boolean intersects(Path2d s) {
		return intersects(s.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}
	
	@Override
	public boolean intersects(PathIterator2f s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromCircle(
				0,
				s,
				this.getX(), this.getY(), this.getRadius(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	@Override
	public boolean intersects(PathIterator2d s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2d.computeCrossingsFromCircle(
				0,
				s,
				this.getX(), this.getY(), this.getRadius(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(AbstractOrientedRectangle2F<?> s) {
		return AbstractOrientedRectangle2F.intersectsOrientedRectangleSolidCircle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				this.getX(), this.getY(), this.getRadius());
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(this.getX());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getY());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getRadius());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator2f implements PathIterator2f {
		
		private final double x;
		private final double y;
		private final double r;
		private int index = 0;
		private double movex, movey;
		private double lastx, lasty;
		
		/**
		 * @param x1
		 * @param y1
		 * @param r1
		 */
		public CopyPathIterator2f(double x1, double y1, double r1) {
			this.r = Math.max(0f, r1);
			this.x = x1 - this.r;
			this.y = y1 - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}
	
		@SuppressWarnings("synthetic-access")
		@Override
		public AbstractPathElement2F next() {
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
				return new AbstractPathElement2F.MovePathElement2f(
						this.lastx, this.lasty);
			}
			else if (idx<5) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				double ppx = this.lastx;
				double ppy = this.lasty;
				this.lastx = (this.x + ctrls[4] * dr);
				this.lasty = (this.y + ctrls[5] * dr);
				return new AbstractPathElement2F.CurvePathElement2f(
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
			return new AbstractPathElement2F.ClosePathElement2f(
					ppx, ppy,
					this.lastx, this.lasty);
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

	}
	
	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator2d implements PathIterator2d {
		
		private final double x;
		private final double y;
		private final double r;
		private int index = 0;
		private double movex, movey;
		private double lastx, lasty;
		
		/**
		 * @param x1
		 * @param y1
		 * @param r1
		 */
		public CopyPathIterator2d(double x1, double y1, double r1) {
			this.r = Math.max(0f, r1);
			this.x = x1 - this.r;
			this.y = y1 - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}
	
		@SuppressWarnings("synthetic-access")
		@Override
		public AbstractPathElement2D next() {
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
				return new AbstractPathElement2D.MovePathElement2d(
						this.lastx, this.lasty);
			}
			else if (idx<5) {
				double dr = 2f * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				double ppx = this.lastx;
				double ppy = this.lasty;
				this.lastx = (this.x + ctrls[4] * dr);
				this.lasty = (this.y + ctrls[5] * dr);
				return new AbstractPathElement2D.CurvePathElement2d(
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
			return new AbstractPathElement2D.ClosePathElement2d(
					ppx, ppy,
					this.lastx, this.lasty);
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

	}
	
	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator2f implements PathIterator2f {
			
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
		 * @param x1
		 * @param y1
		 * @param r1
		 * @param transform1
		 */
		public TransformPathIterator2f(double x1, double y1, double r1, Transform2D transform1) {
			assert(transform1!=null);
			this.transform = transform1;
			this.r = Math.max(0f, r1);
			this.x = x1 - this.r;
			this.y = y1 - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}
	
		@SuppressWarnings("synthetic-access")
		@Override
		public AbstractPathElement2F next() {
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
				return new AbstractPathElement2F.MovePathElement2f(
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
				return new AbstractPathElement2F.CurvePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.p2.getX(), this.p2.getY());
			}
			this.p1.set(this.p2);
			this.p2.set(this.movex, this.movey);
			this.transform.transform(this.p2);
			return new AbstractPathElement2F.ClosePathElement2f(
					this.p1.getX(), this.p1.getY(),
					this.p2.getX(), this.p2.getY());
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

	}
	
	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator2d implements PathIterator2d {
			
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
		 * @param x1
		 * @param y1
		 * @param r1
		 * @param transform1
		 */
		public TransformPathIterator2d(double x1, double y1, double r1, Transform2D transform1) {
			assert(transform1!=null);
			this.transform = transform1;
			this.r = Math.max(0f, r1);
			this.x = x1 - this.r;
			this.y = y1 - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}
	
		@SuppressWarnings("synthetic-access")
		@Override
		public AbstractPathElement2D next() {
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
				return new AbstractPathElement2D.MovePathElement2d(
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
				return new AbstractPathElement2D.CurvePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.p2.getX(), this.p2.getY());
			}
			this.p1.set(this.p2);
			this.p2.set(this.movex, this.movey);
			this.transform.transform(this.p2);
			return new AbstractPathElement2D.ClosePathElement2d(
					this.p1.getX(), this.p1.getY(),
					this.p2.getX(), this.p2.getY());
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

	}

}

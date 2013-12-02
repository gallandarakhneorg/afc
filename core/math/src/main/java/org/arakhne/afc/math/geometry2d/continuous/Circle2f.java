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
package org.arakhne.afc.math.geometry2d.continuous;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.IntersectionUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry2d.Point2D;



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
	static final float CTRL_VAL = 0.5522847498307933f;

	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static final float PCV = 0.5f + CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static final float NCV = 0.5f - CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static float CTRL_PTS[][] = {
		{  1.0f,  PCV,  PCV,  1.0f,  0.5f,  1.0f },
		{  NCV,  1.0f,  0.0f,  PCV,  0.0f,  0.5f },
		{  0.0f,  NCV,  NCV,  0.0f,  0.5f,  0.0f },
		{  PCV,  0.0f,  1.0f,  NCV,  1.0f,  0.5f }
	};

	/** X-coordinate of the circle center. */
	protected float cx = 0f;
	/** Y-coordinate of the circle center. */
	protected float cy = 0f;
	/** Radius of the circle center (must be always positive). */
	protected float radius = 0f;

	/**
	 */
	public Circle2f() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Circle2f(Point2D center, float radius) {
		set(center, radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle2f(float x, float y, float radius) {
		set(x, y, radius);
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
	public void set(float x, float y, float radius) {
		this.cx = x;
		this.cy = y;
		this.radius = Math.abs(radius);
	}

	/** Change the frame of te circle.
	 * 
	 * @param center
	 * @param radius
	 */
	public void set(Point2D center, float radius) {
		this.cx = center.getX();
		this.cy = center.getY();
		this.radius = Math.abs(radius);
	}

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	public float getX() {
		return this.cx;
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	public float getY() {
		return this.cy;
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	public Point2f getCenter() {
		return new Point2f(this.cx, this.cy);
	}

	/** Replies the center.
	 * 
	 * @param center
	 */
	public void setCenter(Point2D center) {
		this.cx = center.getX();
		this.cy = center.getY();
	}

	/** Replies the center.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCenter(float x, float y) {
		this.cx = x;
		this.cy = y;
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	public float getRadius() {
		return this.radius;
	}

	/** Set the radius.
	 * 
	 * @param radius is the radius.
	 */
	public void setRadius(float radius) {
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
	public float distance(Point2D p) {
		float d = GeometryUtil.distancePointPoint(getX(), getY(), p.getX(), p.getY()) - getRadius();
		return Math.max(0f, d);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2D p) {
		float d = distance(p);
		return d * d;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceL1(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.distanceL1(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceLinf(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.distanceLinf(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(float x, float y) {
		return GeometryUtil.isInsidePointCircle(x, y, getX(), getY(), getRadius());
	}
	
	@Override
	public boolean contains(Rectangle2f r) {
		return GeometryUtil.isInsideRectangleCircle(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight(),
				getX(), getY(), getRadius());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getClosestPointTo(Point2D p) {
		
		Point2f m = new Point2f();
		GeometryUtil.closestPointPointCircle(p.getX(), p.getY(), this.cx, this.cy, this.radius, m);
		return m;
	}

	@Override
	public void translate(float dx, float dy) {
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
		bits = 31L * bits + floatToIntBits(getX());
		bits = 31L * bits + floatToIntBits(getY());
		bits = 31L * bits + floatToIntBits(getRadius());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return IntersectionUtil.intersectsCircleRectangle(
				getX(), getY(), getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return IntersectionUtil.intersectsEllipseEllipse(
				getX()-getRadius(), getY()-getRadius(),
				getX()+getRadius(), getY()+getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return IntersectionUtil.intersectsCircleCircle(
				getX(), getY(), getRadius(),
				s.getX(), s.getY(), s.getRadius());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return IntersectionUtil.intersectsCircleSegment(
				getX(), getY(), getRadius(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return IntersectionUtil.intersectsSolidCircleOrientedRectangle(
				this.cx,this.cy, this.radius,
				s.getCx(), s.getCy(), s.getRx(), s.getRy(), s.getSx(), s.getSy(), s.getExtentR(), s.getExtentS(), 0);
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
		
		private final float x;
		private final float y;
		private final float r;
		private int index = 0;
		private float movex, movey;
		private float lastx, lasty;
		
		/**
		 * @param x
		 * @param y
		 * @param r
		 */
		public CopyPathIterator(float x, float y, float r) {
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
	
		@Override
		public PathElement2f next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			if (idx==0) {
				float dr = 2f * this.r;
				float ctrls[] = CTRL_PTS[3];
				this.movex = (this.x + ctrls[4] * dr);
				this.movey = (this.y + ctrls[5] * dr);
				this.lastx = this.movex;
				this.lasty = this.movey;
				return new PathElement2f.MovePathElement2f(
						this.lastx, this.lasty);
			}
			else if (idx<5) {
				float dr = 2f * this.r;
				float ctrls[] = CTRL_PTS[idx - 1];
				float ppx = this.lastx;
				float ppy = this.lasty;
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
			float ppx = this.lastx;
			float ppy = this.lasty;
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
		private final float x;
		private final float y;
		private final float r;
		private int index = 0;
		private float movex, movey;
		
		/**
		 * @param x
		 * @param y
		 * @param r
		 * @param transform
		 */
		public TransformPathIterator(float x, float y, float r, Transform2D transform) {
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
	
		@Override
		public PathElement2f next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			if (idx==0) {
				float dr = 2f * this.r;
				float ctrls[] = CTRL_PTS[3];
				this.movex = (this.x + ctrls[4] * dr);
				this.movey = (this.y + ctrls[5] * dr);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				return new PathElement2f.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
			}
			else if (idx<5) {
				float dr = 2f * this.r;
				float ctrls[] = CTRL_PTS[idx - 1];
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
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


/** 2D ellipse with floating-point points.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Ellipse2f extends AbstractRectangularShape2f<Ellipse2f> {

	private static final long serialVersionUID = -2745313055404516167L;

	// ArcIterator.btan(Math.PI/2)
	private static final float CTRL_VAL = 0.5522847498307933f;

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
	static final float CTRL_PTS[][] = {
		{  1.0f,  PCV,  PCV,  1.0f,  0.5f,  1.0f },
		{  NCV,  1.0f,  0.0f,  PCV,  0.0f,  0.5f },
		{  0.0f,  NCV,  NCV,  0.0f,  0.5f,  0.0f },
		{  PCV,  0.0f,  1.0f,  NCV,  1.0f,  0.5f }
	};

	/**
	 */
	public Ellipse2f() {
		//
	}

	/**
	 * @param min is the min corner of the ellipse.
	 * @param max is the max corner of the ellipse.
	 */
	public Ellipse2f(Point2f min, Point2f max) {
		super(min, max);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Ellipse2f(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2f toBoundingBox() {
		return new Rectangle2f(getMinX(), getMinY(), getMaxX(), getMaxY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.distanceSquared(p);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(float x, float y) {
		return GeometryUtil.isInsidePointEllipse(
				x, y,
				getMinX(), getMinY(), getWidth(), getHeight());
	}

	@Override
	public boolean contains(Rectangle2f r) {
		return GeometryUtil.isInsideRectangleEllipse(
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight(),
				getMinX(), getMinY(), getWidth(), getHeight());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2D getClosestPointTo(Point2D p) {
		
		Point2f closest = new Point2f();
		GeometryUtil.closestPointPointSolidEllipse(
				p.getX(), p.getY(),
				getMinX(), getMinY(),
				getWidth(), getHeight(),false, closest);
		return closest;
	}

	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform==null) {
			return new CopyPathIterator(
					getMinX(), getMinY(),
					getMaxX(), getMaxY());
		}
		return new TransformPathIterator(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				transform);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Ellipse2f) {
			Ellipse2f rr2d = (Ellipse2f) obj;
			return ((getMinX() == rr2d.getMinX()) &&
					(getMinY() == rr2d.getMinY()) &&
					(getWidth() == rr2d.getWidth()) &&
					(getHeight() == rr2d.getHeight()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + floatToIntBits(getMinX());
		bits = 31L * bits + floatToIntBits(getMinY());
		bits = 31L * bits + floatToIntBits(getMaxX());
		bits = 31L * bits + floatToIntBits(getMaxY());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return IntersectionUtil.intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return IntersectionUtil.intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return IntersectionUtil.intersectsEllipseEllipse(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX()-s.getRadius(), s.getY()-s.getRadius(),
				s.getX()+s.getRadius(), s.getY()+s.getRadius());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return IntersectionUtil.intersectsEllipseSegment(
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return IntersectionUtil.intersectsEllipseOrientedRectangle(
				minx, miny, maxy - minx, maxy - maxy,
				s.getCx(), s.getCy(), s.getRx(), s.getRy(), s.getSx(), s.getSy(), s.getExtentR(), s.getExtentS());
		}

	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromEllipse(
				0,
				s,
				getMinX(), getMinY(), getWidth(), getHeight(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getMinX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMinY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CopyPathIterator implements PathIterator2f {

		private final float x1;
		private final float y1;
		private final float w;
		private final float h;
		private int index;
		private float lastX, lastY;

		/**
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 */
		public CopyPathIterator(float x1, float y1, float x2, float y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.w = x2 - x1;
			this.h = y2 - y1;
			if (this.w==0f && this.h==0f) {
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
				float ctrls[] = CTRL_PTS[3];
				this.lastX = this.x1 + ctrls[4] * this.w;
				this.lastY = this.y1 + ctrls[5] * this.h;
				return new PathElement2f.MovePathElement2f(
						this.lastX,  this.lastY);
			}
			else if (idx<5) {
				float ctrls[] = CTRL_PTS[idx - 1];
				float ix = this.lastX;
				float iy = this.lastY;
				this.lastX = (this.x1 + ctrls[4] * this.w);
				this.lastY = (this.y1 + ctrls[5] * this.h);
				return new PathElement2f.CurvePathElement2f(
						ix,  iy,
						(this.x1 + ctrls[0] * this.w),
						(this.y1 + ctrls[1] * this.h),
						(this.x1 + ctrls[2] * this.w),
						(this.y1 + ctrls[3] * this.h),
						this.lastX,
						this.lastY);
			}

			return new PathElement2f.ClosePathElement2f(
					this.lastX, this.lastY,
					this.lastX, this.lastY);
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

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class TransformPathIterator implements PathIterator2f {

		private final Point2D lastPoint = new Point2f();
		private final Point2D ptmp1 = new Point2f();
		private final Point2D ptmp2 = new Point2f();
		private final Transform2D transform;
		private final float x1;
		private final float y1;
		private final float w;
		private final float h;
		private int index;

		/**
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @param transform
		 */
		public TransformPathIterator(float x1, float y1, float x2, float y2, Transform2D transform) {
			this.transform = transform;
			this.x1 = x1;
			this.y1 = y1;
			this.w = x2 - x1;
			this.h = y2 - y1;
			if (this.w==0f && this.h==0f) {
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
				float ctrls[] = CTRL_PTS[3];
				this.lastPoint.set(
						this.x1 + ctrls[4] * this.w,
						this.y1 + ctrls[5] * this.h);
				this.transform.transform(this.lastPoint);
				return new PathElement2f.MovePathElement2f(
						this.lastPoint.getX(), this.lastPoint.getY());
			}
			else if (idx<5) {
				float ctrls[] = CTRL_PTS[idx - 1];
				float ix = this.lastPoint.getX();
				float iy = this.lastPoint.getY();
				this.lastPoint.set(
						(this.x1 + ctrls[4] * this.w),
						(this.y1 + ctrls[5] * this.h));
				this.transform.transform(this.lastPoint);
				this.ptmp1.set(
						(this.x1 + ctrls[0] * this.w),
						(this.y1 + ctrls[1] * this.h));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						(this.x1 + ctrls[2] * this.w),
						(this.y1 + ctrls[3] * this.h));
				this.transform.transform(this.ptmp2);
				return new PathElement2f.CurvePathElement2f(
						ix,  iy,
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.lastPoint.getX(), this.lastPoint.getY());
			}

			float ix = this.lastPoint.getX();
			float iy = this.lastPoint.getY();
			return new PathElement2f.ClosePathElement2f(
					ix, iy,
					ix, iy);
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
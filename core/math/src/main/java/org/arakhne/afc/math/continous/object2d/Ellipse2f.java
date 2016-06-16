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

package org.arakhne.afc.math.continous.object2d;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.d.Ellipse2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.vmutil.ReflectionUtil;


/** 2D ellipse with floating-point points.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link Ellipse2d}
 */
@Deprecated
@SuppressWarnings("all")
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

	/** Replies if a rectangle is inside in the ellipse.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ewidth is the width of the ellipse.
	 * @param eheight is the height of the ellipse.
	 * @param rx is the lowest corner of the rectangle.
	 * @param ry is the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	public static boolean containsEllipseRectangle(float ex, float ey, float ewidth, float eheight, float rx, float ry, float rwidth, float rheight) {
		float ecx = (ex + ewidth/2f);
		float ecy = (ey + eheight/2f);
		float rcx = (rx + rwidth/2f);
		float rcy = (ry + rheight/2f);
		float farX;
		if (ecx<=rcx) farX = rx + rwidth;
		else farX = rx;
		float farY;
		if (ecy<=rcy) farY = ry + rheight;
		else farY = ry;
		return Ellipse2afp.containsEllipsePoint(ex, ey, ewidth, eheight, farX, farY);
	}

	/** Replies if two ellipses are intersecting.
	 * 
	 * @param x1 is the first corner of the first ellipse.
	 * @param y1 is the first corner of the first ellipse.
	 * @param x2 is the second corner of the first ellipse.
	 * @param y2 is the second corner of the first ellipse.
	 * @param x3 is the first corner of the second ellipse.
	 * @param y3 is the first corner of the second ellipse.
	 * @param x4 is the second corner of the second ellipse.
	 * @param y4 is the second corner of the second ellipse.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsEllipseEllipse(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float ell2w = Math.abs(x4 - x3);
		float ell2h = Math.abs(y4 - y3);
		float ellw = Math.abs(x2 - x1);
		float ellh = Math.abs(y2 - y1);

		if (ell2w <= 0f || ell2h <= 0f) return false;
		if (ellw <= 0f || ellh <= 0f) return false;

		// Normalize the second ellipse coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		float normx0 = (x3 - x1) / ellw - 0.5f;
		float normx1 = normx0 + ell2w / ellw;
		float normy0 = (y3 - y1) / ellh - 0.5f;
		float normy1 = normy0 + ell2h / ellh;

		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		float nearx, neary;
		if (normx0 > 0f) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0f) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0f;
		}
		if (normy0 > 0f) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0f) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0f;
		}
		return (nearx * nearx + neary * neary) < 0.25f;
	}

	/** Replies if an ellipse and a line are intersecting.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param x1 is the first point of the line.
	 * @param y1 is the first point of the line.
	 * @param x2 is the second point of the line.
	 * @param y2 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
	 */
	public static boolean intersectsEllipseLine(float ex, float ey, float ew, float eh, float x1, float y1, float x2, float y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0f || ew<=0f) {
			return false;
		}

		// Get the semimajor and semiminor axes.
		float a = ew / 2f;
		float b = eh / 2f;

		// Translate so the ellipse is centered at the origin.
		float ecx = ex + a;
		float ecy = ey + b;
		float px1 = x1 - ecx;
		float py1 = y1 - ecy;
		float px2 = x2 - ecx;
		float py2 = y2 - ecy;
		
		float sq_a = a*a;
		float sq_b = b*b;
		float vx = px2 - px1;
		float vy = py2 - py1;
		
		assert(sq_a!=0f && sq_b!=0f);

		// Calculate the quadratic parameters.
		float A = vx * vx / sq_a +
				vy * vy / sq_b;
		float B = 2f * px1 * vx / sq_a +
				2f * py1 * vy / sq_b;
		float C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;

		// Calculate the discriminant.
		float discriminant = B * B - 4f * A * C;
		return (discriminant>=0f);
	}

	/** Replies if an ellipse and a segment are intersecting.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the second point of the segment.
	 * @param y2 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
	 */
	public static boolean intersectsEllipseSegment(float ex, float ey, float ew, float eh, float x1, float y1, float x2, float y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0f || ew<=0f) {
			return false;
		}

		// Get the semimajor and semiminor axes.
		float a = ew / 2f;
		float b = eh / 2f;

		// Translate so the ellipse is centered at the origin.
		float ecx = ex + a;
		float ecy = ey + b;
		float px1 = x1 - ecx;
		float py1 = y1 - ecy;
		float px2 = x2 - ecx;
		float py2 = y2 - ecy;
		
		float sq_a = a*a;
		float sq_b = b*b;
		float vx = px2 - px1;
		float vy = py2 - py1;
		
		assert(sq_a!=0f && sq_b!=0f);

		// Calculate the quadratic parameters.
		float A = vx * vx / sq_a + vy * vy / sq_b;
		float B = 2f * px1 * vx / sq_a + 2f * py1 * vy / sq_b;
		float C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;

		// Calculate the discriminant.
		float discriminant = B * B - 4f * A * C;
		if (discriminant<0f) {
			// No solution
			return false;
		}
		
		if (discriminant==0f) {
			// One real solution.
			float t = -B / 2f / A;
			return ((t >= 0f) && (t <= 1f));
		}

		assert(discriminant>0f);
		
		// Two real solutions.
		float t1 = (-B + (float)Math.sqrt(discriminant)) / 2f / A;
		float t2 = (-B - (float)Math.sqrt(discriminant)) / 2f / A;
		
		return (t1>=0 || t2>=0) && (t1<=1 || t2<=1);
	}

	/** Replies if two ellipses are intersecting.
	 * 
	 * @param x1 is the first corner of the first ellipse.
	 * @param y1 is the first corner of the first ellipse.
	 * @param x2 is the second corner of the first ellipse.
	 * @param y2 is the second corner of the first ellipse.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsEllipseRectangle(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		// From AWT Ellipse2D

		float rectw = Math.abs(x4 - x3);
		float recth = Math.abs(y4 - y3);
		float ellw = Math.abs(x2 - x1);
		float ellh = Math.abs(y2 - y1);

		if (rectw <= 0f || recth <= 0f) return false;
		if (ellw <= 0f || ellh <= 0f) return false;

		// Normalize the rectangular coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		float normx0 = (x3 - x1) / ellw - 0.5f;
		float normx1 = normx0 + rectw / ellw;
		float normy0 = (y3 - y1) / ellh - 0.5f;
		float normy1 = normy0 + recth / ellh;
		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		float nearx, neary;
		if (normx0 > 0f) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0f) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0f;
		}
		if (normy0 > 0f) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0f) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0f;
		}
		return (nearx * nearx + neary * neary) < 0.25f;
	}

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

	/**
	 * @param e
	 */
	public Ellipse2f(Ellipse2f e) {
		super(e);
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
		return Ellipse2afp.containsEllipsePoint(
				getMinX(), getMinY(), getWidth(), getHeight(),
				x, y);
	}

	@Override
	public boolean contains(Rectangle2f r) {
		return containsEllipseRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2D getClosestPointTo(Point2D p) {
		Point2d pts = new Point2d();
		Ellipse2afp.findsClosestPointSolidEllipsePoint(
				p.getX(), p.getY(),
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				pts);
		return new Point2f((float) pts.getX(), (float) pts.getY());
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
		return intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return intersectsEllipseEllipse(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX()-s.getRadius(), s.getY()-s.getRadius(),
				s.getX()+s.getRadius(), s.getY()+s.getRadius());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return intersectsEllipseSegment(
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator((float) MathConstants.SPLINE_APPROXIMATION_RATIO));
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
		return ReflectionUtil.toString(this);
	}

	/**
	 * @author $Author: sgalland$
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
	 * @author $Author: sgalland$
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

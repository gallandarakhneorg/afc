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
import org.arakhne.afc.math.generic.PathElementType;
import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.RoundRectangle2d;
import org.arakhne.afc.math.matrix.Transform2D;


/** 2D round rectangle with floating-point points.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link RoundRectangle2d}
 */
@Deprecated
@SuppressWarnings("all")
public class RoundRectangle2f extends AbstractRectangularShape2f<RoundRectangle2f> {

	private static final long serialVersionUID = 4681356809053380781L;

	/** Replies if a rectangle is inside in the round rectangle.
	 * 
	 * @param rx1 is the lowest corner of the round rectangle.
	 * @param ry1 is the lowest corner of the round rectangle.
	 * @param rwidth1 is the width of the round rectangle.
	 * @param rheight1 is the height of the round rectangle.
	 * @param awidth is the width of the arc of the round rectangle.
	 * @param aheight is the height of the arc of the round rectangle.
	 * @param rx2 is the lowest corner of the inner-candidate rectangle.
	 * @param ry2 is the lowest corner of the inner-candidate rectangle.
	 * @param rwidth2 is the width of the inner-candidate rectangle.
	 * @param rheight2 is the height of the inner-candidate rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	public static boolean containsRoundRectangleRectangle(float rx1, float ry1, float rwidth1, float rheight1, float awidth, float aheight, float rx2, float ry2, float rwidth2, float rheight2) {
		float rcx1 = (rx1 + rwidth1/2f);
		float rcy1 = (ry1 + rheight1/2f);
		float rcx2 = (rx2 + rwidth2/2f);
		float rcy2 = (ry2 + rheight2/2f);
		float farX;
		if (rcx1<=rcx2) farX = rx2 + rwidth2;
		else farX = rx2;
		float farY;
		if (rcy1<=rcy2) farY = ry2 + rheight2;
		else farY = ry2;
		return containsRoundRectanglePoint(rx1, ry1, rwidth1, rheight1, awidth, aheight, farX, farY);
	}

	/** Replies if a point is inside in the round rectangle.
	 * 
	 * @param rx is the lowest corner of the round rectangle.
	 * @param ry is the lowest corner of the round rectangle.
	 * @param rwidth is the width of the round rectangle.
	 * @param rheight is the height of the round rectangle.
	 * @param awidth is the width of the arc of the round rectangle.
	 * @param aheight is the height of the arc of the round rectangle.
	 * @param px is the point.
	 * @param py is the point.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	public static boolean containsRoundRectanglePoint(float rx, float ry, float rwidth, float rheight, float awidth, float aheight, float px, float py) {
		if (rwidth<=0f && rheight<=0f) {
			return rx==px && ry==py;
		}
		float rx0 = rx;
		float ry0 = ry;
		float rrx1 = rx0 + rwidth;
		float rry1 = ry0 + rheight;
		// Check for trivial rejection - point is outside bounding rectangle
		if (px < rx0 || py < ry0 || px >= rrx1 || py >= rry1) {
			return false;
		}
		float aw = Math.min(rwidth, Math.abs(awidth)) / 2f;
		float ah = Math.min(rheight, Math.abs(aheight)) / 2f;
		// Check which corner point is in and do circular containment
		// test - otherwise simple acceptance
		if (px >= (rx0 += aw) && px < (rx0 = rrx1 - aw)) {
			return true;
		}
		if (py >= (ry0 += ah) && py < (ry0 = rry1 - ah)) {
			return true;
		}
		float xx = (px - rx0) / aw;
		float yy = (py - ry0) / ah;
		return (xx * xx + yy * yy <= 1f);
	}

	private static final float ANGLE = (float) MathConstants.PI / 4f;
	private static final float A = 1f - (float)Math.cos(ANGLE);
	private static final float B = (float)Math.tan(ANGLE);
	private static final float C = (float)Math.sqrt(1f + B * B) - 1f + A;
	private static final float CV = 4f / 3f * A * B / C;
	private static final float ACV = (1f - CV) / 2f;

	/** For each array:
	 * 4 values for each point {v0, v1, v2, v3}:
	 * point = (x + v0 * w + v1 * arcWidth,
	 * y + v2 * h + v3 * arcHeight)
	 */
	static float CTRL_PTS[][] = {
		{  0f,  0f,  0f,  .5f },
		{  0f,  0f,  1f, -.5f },
		{  0f,  0f,  1f, -ACV,
			0f,  ACV,  1f,  0f,
			0f,  .5f,  1f,  0f },
			{  1f, -.5f,  1f,  0f },
			{  1f, -ACV,  1f,  0f,
				1f,  0f,  1f, -ACV,
				1f,  0f,  1f, -.5f },
				{  1f,  0f,  0f,  .5f },
				{  1f,  0f,  0f,  ACV,
					1f, -ACV,  0f,  0f,
					1f, -.5f,  0f,  0f },
					{  0f,  .5f,  0f,  0f },
					{  0f,  ACV,  0f,  0f,
						0f,  0f,  0f,  ACV,
						0f,  0f,  0f,  .5f },
						{},
	};

	/** Types of path elements for the round rectangle.
	 */
	static PathElementType TYPES[] = {
		PathElementType.MOVE_TO,
		PathElementType.LINE_TO, PathElementType.CURVE_TO,
		PathElementType.LINE_TO, PathElementType.CURVE_TO,
		PathElementType.LINE_TO, PathElementType.CURVE_TO,
		PathElementType.LINE_TO, PathElementType.CURVE_TO,
		PathElementType.CLOSE,
	};


	/** Width of the arcs at the corner of the box. */
	protected float arcWidth;
	/** Height of the arcs at the corner of the box. */
	protected float arcHeight;

	/**
	 */
	public RoundRectangle2f() {
		this.arcHeight = this.arcWidth = 0f;
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth
	 * @param arcHeight
	 */
	public RoundRectangle2f(Point2f min, Point2f max, float arcWidth, float arcHeight) {
		super(min, max);
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2f(RoundRectangle2f rr) {
		super(rr);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth
	 * @param arcHeight
	 */
	public RoundRectangle2f(float x, float y, float width, float height, float arcWidth, float arcHeight) {
		super(x, y, width, height);
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
	}

	@Override
	public void clear() {
		this.arcHeight = this.arcWidth = 0f;
		super.clear();
	}

	/**
	 * Gets the width of the arc that rounds off the corners.
	 * @return the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public float getArcWidth() {
		return this.arcWidth;
	}

	/**
	 * Gets the height of the arc that rounds off the corners.
	 * @return the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public float getArcHeight() {
		return this.arcHeight;
	}

	/**
	 * Set the width of the arc that rounds off the corners.
	 * @param a is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public void setArcWidth(float a) {
		this.arcWidth = a;
	}

	/**
	 * Set the height of the arc that rounds off the corners.
	 * @param a is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public void setArcHeight(float a) {
		this.arcHeight = a;
	}

	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 * @param arcHeight is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public void set(float x, float y, float width, float height, float arcWidth, float arcHeight) {
		setFromCorners(x, y, x+width, y+height);
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(float x, float y) {
		return containsRoundRectanglePoint(
				getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight(),
				x, y);
	}

	@Override
	public boolean contains(Rectangle2f r) {
		return containsRoundRectangleRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	@Override
	public float distanceSquared(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.distanceSquared(p);
	}

	@Override
	public float distanceL1(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.distanceL1(p);
	}

	@Override
	public float distanceLinf(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.distanceLinf(p);
	}

	@Override
	public Point2D getClosestPointTo(Point2D p) {
		float px = p.getX();
		float py = p.getY();
		float rx1 = getMinX();
		float ry1 = getMinY();
		float rx2 = getMaxX();
		float ry2 = getMaxY();

		float aw = getArcWidth();
		float ah = getArcHeight();

		int same = 0;
		float x, y;

		if (px<rx1+aw) {
			if (py<ry1+ah) {
				Point2d pts = new Point2d();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx1, ry1,
						aw, ah,
						pts);
				return new Point2f(pts.getX(), pts.getY());
			}
			if (py>ry2-ah) {
				Point2d pts = new Point2d();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx1, ry2-ah,
						aw, ah,
						pts);
				return new Point2f(pts.getX(), pts.getY());
			}
		}
		else if (px>rx2-aw) {
			if (py<ry1+ah) {
				Point2d pts = new Point2d();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx2-aw, ry1,
						aw, ah,
						pts);
				return new Point2f(pts.getX(), pts.getY());
			}
			if (py>ry2-ah) {
				Point2d pts = new Point2d();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx2-aw, ry2-ah,
						aw, ah,
						pts);
				return new Point2f(pts.getX(), pts.getY());
			}
		}


		if (px<rx1) {
			x = rx1;
		}
		else if (px>rx2) {
			x = rx2;
		}
		else {
			x = px;
			++same;
		}

		if (py<ry1) {
			y = ry1;
		}
		else if (py>ry2) {
			y = ry2;
		}
		else {
			y = py;
			++same;
		}

		if (same==2) return p;
		return new Point2f(x,y);
	}

	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform==null) {
			return new CopyPathIterator(
					getMinX(), getMinY(),
					getWidth(), getHeight(),
					getArcWidth(), getArcHeight());
		}
		return new TransformPathIterator(
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				getArcWidth(), getArcHeight(),
				transform);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof RoundRectangle2f) {
			RoundRectangle2f rr2d = (RoundRectangle2f) obj;
			return ((getMinX() == rr2d.getMinX()) &&
					(getMinY() == rr2d.getMinY()) &&
					(getWidth() == rr2d.getWidth()) &&
					(getHeight() == rr2d.getHeight()) &&
					(getArcWidth() == rr2d.getArcWidth()) &&
					(getArcHeight() == rr2d.getArcHeight()));
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
		bits = 31L * bits + floatToIntBits(getArcWidth());
		bits = 31L * bits + floatToIntBits(getArcHeight());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return Rectangle2f.intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return Ellipse2f.intersectsEllipseRectangle(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return Circle2f.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return Rectangle2f.intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator((float) MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		// Copied from AWT API
		if (isEmpty()) return false;
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromRect(
				s,
				getMinX(), getMinY(), getMaxX(), getMaxY(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); 
		b.append(getMinX());
		b.append(";"); 
		b.append(getMinY());
		b.append(";"); 
		b.append(getMaxX());
		b.append(";"); 
		b.append(getMaxY());
		b.append("|"); 
		b.append(getArcWidth());
		b.append("x"); 
		b.append(getArcHeight());
		b.append("]"); 
		return b.toString();
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator implements PathIterator2f {

		private final float x;
		private final float y;
		private final float w;
		private final float h;
		private final float aw;
		private final float ah;
		private int index = 0;

		private float moveX, moveY, lastX,  lastY;

		/**
		 * @param x
		 * @param y
		 * @param w
		 * @param h
		 * @param aw
		 * @param ah
		 */
		public CopyPathIterator(float x, float y, float w, float h, float aw, float ah) {
			this.x = x;
			this.y = y;
			this.w = Math.max(0f, w);
			this.h = Math.max(0f, h);
			this.aw = Math.min(Math.abs(aw), w);
			this.ah = Math.min(Math.abs(ah), h);
			if (this.w<=0f || this.h<=0f) {
				this.index = TYPES.length;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<TYPES.length;
		}

		@Override
		public PathElement2f next() {
			if (this.index>=TYPES.length) throw new NoSuchElementException();
			int idx = this.index;

			PathElement2f element = null;
			PathElementType type = TYPES[idx];
			float ctrls[] = CTRL_PTS[idx];
			float ix, iy;
			float ctrlx1, ctrly1, ctrlx2, ctrly2;

			switch(type) {
			case MOVE_TO:
				this.moveX = this.lastX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.moveY = this.lastY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				element = new PathElement2f.MovePathElement2f(
						this.lastX, this.lastY);
				break;
			case LINE_TO:
				ix = this.lastX;
				iy = this.lastY;
				this.lastX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.lastY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				element = new PathElement2f.LinePathElement2f(
						ix, iy,
						this.lastX, this.lastY);
				break;
			case CURVE_TO:
				ix = this.lastX;
				iy = this.lastY;
				ctrlx1 = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				ctrly1 = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				ctrlx2 = this.x + ctrls[4] * this.w + ctrls[5] * this.aw;
				ctrly2 = this.y + ctrls[6] * this.h + ctrls[7] * this.ah;
				this.lastX = this.x + ctrls[8] * this.w + ctrls[9] * this.aw;
				this.lastY = this.y + ctrls[10] * this.h + ctrls[11] * this.ah;
				element = new PathElement2f.CurvePathElement2f(
						ix, iy,
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.lastX, this.lastY);
				break;
			case CLOSE:
				ix = this.lastX;
				iy = this.lastY;
				this.lastX = this.moveX;
				this.lastY = this.moveY;
				element = new PathElement2f.ClosePathElement2f(
						ix, iy,
						this.lastX, this.lastY);
				break;
			case QUAD_TO:
			default:
				throw new NoSuchElementException();
			}

			assert(element!=null);

			++this.index;

			return element;
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

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator implements PathIterator2f {

		private final Transform2D transform;
		private final float x;
		private final float y;
		private final float w;
		private final float h;
		private final float aw;
		private final float ah;
		private int index = 0;

		private float moveX, moveY;
		private final Point2D last = new Point2f();
		private final Point2D ctrl1 = new Point2f();
		private final Point2D ctrl2 = new Point2f();

		/**
		 * @param x
		 * @param y
		 * @param w
		 * @param h
		 * @param aw
		 * @param ah
		 * @param transform
		 */
		public TransformPathIterator(float x, float y, float w, float h, float aw, float ah, Transform2D transform) {
			this.transform = transform;
			this.x = x;
			this.y = y;
			this.w = Math.max(0f, w);
			this.h = Math.max(0f, h);
			this.aw = Math.min(Math.abs(aw), w);
			this.ah = Math.min(Math.abs(ah), h);
			if (this.w<=0f || this.h<=0f) {
				this.index = TYPES.length;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<TYPES.length;
		}

		@Override
		public PathElement2f next() {
			if (this.index>=TYPES.length) throw new NoSuchElementException();
			int idx = this.index;

			PathElement2f element = null;
			PathElementType type = TYPES[idx];
			float ctrls[] = CTRL_PTS[idx];
			float ix, iy;

			switch(type) {
			case MOVE_TO:
				this.moveX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.moveY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				this.last.set(this.moveX, this.moveY);
				this.transform.transform(this.last);
				element = new PathElement2f.MovePathElement2f(
						this.last.getX(), this.last.getY());
				break;
			case LINE_TO:
				ix = this.last.getX();
				iy = this.last.getY();
				this.last.set(
						this.x + ctrls[0] * this.w + ctrls[1] * this.aw,
						this.y + ctrls[2] * this.h + ctrls[3] * this.ah);
				this.transform.transform(this.last);
				element = new PathElement2f.LinePathElement2f(
						ix, iy,
						this.last.getX(), this.last.getY());
				break;
			case CURVE_TO:
				ix = this.last.getX();
				iy = this.last.getY();
				this.ctrl1.set(
						this.x + ctrls[0] * this.w + ctrls[1] * this.aw,
						this.y + ctrls[2] * this.h + ctrls[3] * this.ah);
				this.transform.transform(this.ctrl1);
				this.ctrl2.set(
						this.x + ctrls[4] * this.w + ctrls[5] * this.aw,
						this.y + ctrls[6] * this.h + ctrls[7] * this.ah);
				this.transform.transform(this.ctrl2);
				this.last.set(
						this.x + ctrls[8] * this.w + ctrls[9] * this.aw,
						this.y + ctrls[10] * this.h + ctrls[11] * this.ah);
				this.transform.transform(this.last);
				element = new PathElement2f.CurvePathElement2f(
						ix, iy,
						this.ctrl1.getX(), this.ctrl1.getY(),
						this.ctrl2.getX(), this.ctrl2.getY(),
						this.last.getX(), this.last.getY());
				break;
			case CLOSE:
				ix = this.last.getX();
				iy = this.last.getY();
				this.last.set(this.moveX, this.moveY);
				this.transform.transform(this.last);
				element = new PathElement2f.ClosePathElement2f(
						ix, iy,
						this.last.getX(), this.last.getY());
				break;
			case QUAD_TO:
			default:
				throw new NoSuchElementException();
			}

			assert(element!=null);

			++this.index;

			return element;
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

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
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry2d.Point2D;


/** 2D round rectangle with floating-point points.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RoundRectangle2f extends AbstractRectangularShape2f<RoundRectangle2f> {

	private static final long serialVersionUID = 4681356809053380781L;

	private static final float ANGLE = MathConstants.PI / 4f;
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
		return GeometryUtil.isInsidePointRoundRectangle(
				x, y,
				getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
	}

	@Override
	public boolean contains(Rectangle2f r) {
		return GeometryUtil.isInsideRectangleRoundRectangle(
				r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY(),
				getMinX(), getMinY(), getMaxX(), getMaxY(), getArcWidth(), getArcHeight());
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
		
		Point2f closest = new Point2f();
		GeometryUtil.closestPointPointRoundRectangle(p.getX(), p.getY(), getMinX(), getMinY(), getMaxX(), getMaxY(), getArcWidth(), getArcHeight(), closest);
		return closest;
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
	public Rectangle2f toBoundingBox() {
		return new Rectangle2f(getMinX(), getMinY(), getWidth(), getHeight());
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return IntersectionUtil.intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return IntersectionUtil.intersectsEllipseRectangle(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return IntersectionUtil.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return IntersectionUtil.intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return IntersectionUtil.intersectsAlignedRectangleOrientedRectangle(
				this.minx, this.miny, this.maxy, this.maxy,
				s.getCx(), s.getCy(), s.getRx(), s.getRy(), s.getSx(), s.getSy(), s.getExtentR(), s.getExtentS());
	}

	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
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
		b.append("["); //$NON-NLS-1$
		b.append(getMinX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMinY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append("|"); //$NON-NLS-1$
		b.append(getArcWidth());
		b.append("x"); //$NON-NLS-1$
		b.append(getArcHeight());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
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
	 * @author $Author: galland$
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
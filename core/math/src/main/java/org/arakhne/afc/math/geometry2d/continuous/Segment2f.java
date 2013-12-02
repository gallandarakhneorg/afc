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
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.IntersectionUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry2d.Point2D;



/** 2D line segment with floating-point coordinates.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment2f extends AbstractShape2f<Segment2f> {

	private static final long serialVersionUID = -82425036308183925L;

	/** X-coordinate of the first point. */
	protected float ax = 0f;
	/** Y-coordinate of the first point. */
	protected float ay = 0f;
	/** X-coordinate of the second point. */
	protected float bx = 0f;
	/** Y-coordinate of the second point. */
	protected float by = 0f;

	/**
	 */
	public Segment2f() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2f(Point2D a, Point2D b) {
		set(a, b);
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Segment2f(float x1, float y1, float x2, float y2) {
		set(x1, y1, x2, y2);
	}

	@Override
	public void clear() {
		this.ax = this.ay = this.bx = this.by = 0f;
	}

	/**
	 * Replies if this segment is empty.
	 * The segment is empty when the two
	 * points are equal.
	 * 
	 * @return <code>true</code> if the two points are
	 * equal.
	 */
	@Override
	public boolean isEmpty() {
		return this.ax==this.bx && this.ay==this.by;
	}

	/** Change the line.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void set(float x1, float y1, float x2, float y2) {
		this.ax = x1;
		this.ay = y1;
		this.bx = x2;
		this.by = y2;
	}

	/** Change the line.
	 * 
	 * @param a
	 * @param b
	 */
	public void set(Point2D a, Point2D b) {
		this.ax = a.getX();
		this.ay = a.getY();
		this.bx = b.getX();
		this.by = b.getY();
	}

	/** Replies the X of the first point.
	 * 
	 * @return the x of the first point.
	 */
	public float getX1() {
		return this.ax;
	}

	/** Replies the Y of the first point.
	 * 
	 * @return the y of the first point.
	 */
	public float getY1() {
		return this.ay;
	}

	/** Replies the X of the second point.
	 * 
	 * @return the x of the second point.
	 */
	public float getX2() {
		return this.bx;
	}

	/** Replies the Y of the second point.
	 * 
	 * @return the y of the second point.
	 */
	public float getY2() {
		return this.by;
	}

	/** Replies the first point.
	 * 
	 * @return the first point.
	 */
	public Point2D getP1() {
		return new Point2f(this.ax, this.ay);
	}

	/** Replies the second point.
	 * 
	 * @return the second point.
	 */
	public Point2D getP2() {
		return new Point2f(this.bx, this.by);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2f toBoundingBox() {
		Rectangle2f r = new Rectangle2f();
		r.setFromCorners(
				this.ax,
				this.ay,
				this.bx,
				this.by);
		return r;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void toBoundingBox(Rectangle2f box) {
		box.setFromCorners(
				this.ax,
				this.ay,
				this.bx,
				this.by);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2D p) {
		return GeometryUtil.distanceSquaredPointSegment(p.getX(), p.getY(),
				this.ax, this.ay,
				this.bx, this.by, null);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceL1(Point2D p) {
		float ratio = GeometryUtil.getPointProjectionFactorOnLine(p.getX(), p.getY(), this.ax, this.ay, this.bx, this.by);
		ratio = MathUtil.clamp(ratio, 0f, 1f);
		Vector2f v = new Vector2f(this.bx, this.by);
		v.sub(this.ax, this.ay);
		v.scale(ratio);
		return Math.abs(this.ax + v.getX() - p.getX())
				+ Math.abs(this.ay + v.getY() - p.getY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceLinf(Point2D p) {
		float ratio = GeometryUtil.getPointProjectionFactorOnLine(p.getX(), p.getY(), this.ax, this.ay, this.bx, this.by);
		ratio = MathUtil.clamp(ratio, 0f, 1f);
		Vector2f v = new Vector2f(this.bx, this.by);
		v.sub(this.ax, this.ay);
		v.scale(ratio);
		return Math.max(
				Math.abs(this.ax + v.getX() - p.getX()),
				Math.abs(this.ay + v.getY() - p.getY()));
	}

	/** {@inheritDoc}
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
	 * 
	 * @see MathUtil#isEpsilonZero(float)
	 */
	@Override
	public boolean contains(float x, float y) {
		return GeometryUtil.isInsidePointSegment(x, y,
				this.ax, this.ay,
				this.bx, this.by, 0);
	}
	
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(Rectangle2f r) {
		return contains(r.getMinX(), r.getMinY())
				&& contains(r.getMaxX(), r.getMaxY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getClosestPointTo(Point2D p) {
		Point2f closest = new Point2f();
		GeometryUtil.closestPointPointSegment(p.getX(), p.getY(), this.ax, this.ay, this.bx, this.by, closest);
		return closest;
	}

	@Override
	public void translate(float dx, float dy) {
		this.ax += dx;
		this.ay += dy;
		this.bx += dx;
		this.by += dy;
	}

	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		return new SegmentPathIterator(
				getX1(), getY1(), getX2(), getY2(),
				transform);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Segment2f) {
			Segment2f rr2d = (Segment2f) obj;
			return ((getX1() == rr2d.getX1()) &&
					(getY1() == rr2d.getY1()) &&
					(getX2() == rr2d.getX2()) &&
					(getY2() == rr2d.getY2()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + floatToIntBits(getX1());
		bits = 31L * bits + floatToIntBits(getY1());
		bits = 31L * bits + floatToIntBits(getX2());
		bits = 31L * bits + floatToIntBits(getY2());
		return (int) (bits ^ (bits >> 32));
	}

	/** Transform the current segment.
	 * This function changes the current segment.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape(Transform2D)
	 */
	public void transform(Transform2D transform) {
		Point2f p = new Point2f(this.ax,  this.ay);
		transform.transform(p);
		this.ax = p.getX();
		this.ay = p.getY();
		p.set(this.bx, this.by);
		transform.transform(p);
		this.bx = p.getX();
		this.by = p.getY();
	}

	@Override
	public Shape2f createTransformedShape(Transform2D transform) {
		Point2D p1 = transform.transform(this.ax, this.ay);
		Point2D p2 = transform.transform(this.bx, this.by);
		return new Segment2f(p1, p2);
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return IntersectionUtil.intersectsRectangleSegment(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return IntersectionUtil.intersectsEllipseSegment(
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return IntersectionUtil.intersectsCircleSegment(
				s.getX(), s.getY(),
				s.getRadius(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return IntersectionUtil.intersectsSegmentSegmentWithoutEnds(
				getX1(), getY1(),
				getX2(), getY2(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return IntersectionUtil.intersectsSegmentOrientedRectangle(
				this.ax, this.ay, this.bx, this.bx,
				s.getCx(), s.getCy(), s.getRx(), s.getRy(), s.getSx(), s.getSy(), s.getExtentR(), s.getExtentS());
	}

	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromSegment(
				0,
				s,
				getX1(), getY1(), getX2(), getY2(),
				false);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX1());
		b.append(";"); //$NON-NLS-1$
		b.append(getY1());
		b.append("|"); //$NON-NLS-1$
		b.append(getX2());
		b.append(";"); //$NON-NLS-1$
		b.append(getY2());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}


	/** Iterator on the path elements of the segment.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class SegmentPathIterator implements PathIterator2f {

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		private final Transform2D transform;
		private final float x1;
		private final float y1;
		private final float x2;
		private final float y2;
		private int index = 0;

		/**
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @param transform
		 */
		public SegmentPathIterator(float x1, float y1, float x2, float y2, Transform2D transform) {
			this.transform = transform;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			if (this.x1==this.x2 && this.y1==this.y2) {
				this.index = 2;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=1;
		}

		@Override
		public PathElement2f next() {
			if (this.index>1) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			default:
				throw new NoSuchElementException();
			}
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
			return true;
		}

	}
}
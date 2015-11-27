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
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;

/** 
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractRoundRectangle2F<T extends AbstractRectangularShape2F<T>> extends AbstractRectangularShape2F<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5372906383276525324L;

	
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
	public static boolean containsRoundRectangleRectangle(double rx1, double ry1, double rwidth1, double rheight1, double awidth, double aheight, double rx2, double ry2, double rwidth2, double rheight2) {
		double rcx1 = (rx1 + rwidth1/2f);
		double rcy1 = (ry1 + rheight1/2f);
		double rcx2 = (rx2 + rwidth2/2f);
		double rcy2 = (ry2 + rheight2/2f);
		double farX; 
		if (rcx1<=rcx2) farX = rx2 + rwidth2;
		else farX = rx2;
		double farY;
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
	public static boolean containsRoundRectanglePoint(double rx, double ry, double rwidth, double rheight, double awidth, double aheight, double px, double py) {
		if (rwidth<=0f && rheight<=0f) {
			return rx==px && ry==py;
		}
		double rx0 = rx;
		double ry0 = ry;
		double rrx1 = rx0 + rwidth;
		double rry1 = ry0 + rheight;
		// Check for trivial rejection - point is outside bounding rectangle
		if (px < rx0 || py < ry0 || px >= rrx1 || py >= rry1) {
			return false;
		}
		double aw = Math.min(rwidth, Math.abs(awidth)) / 2f;
		double ah = Math.min(rheight, Math.abs(aheight)) / 2f;
		// Check which corner point is in and do circular containment
		// test - otherwise simple acceptance
		if (px >= (rx0 += aw) && px < (rx0 = rrx1 - aw)) {
			return true;
		}
		if (py >= (ry0 += ah) && py < (ry0 = rry1 - ah)) {
			return true;
		}
		double xx = (px - rx0) / aw;
		double yy = (py - ry0) / ah;
		return (xx * xx + yy * yy <= 1f);
	}

	private static final double ANGLE = MathConstants.PI / 4f;
	private static final double A = 1f - Math.cos(ANGLE);
	private static final double B = Math.tan(ANGLE);
	private static final double C = Math.sqrt(1f + B * B) - 1f + A;
	private static final double CV = 4f / 3f * A * B / C;
	private static final double ACV = (1f - CV) / 2f;

	/** For each array:
	 * 4 values for each point {v0, v1, v2, v3}:
	 * point = (x + v0 * w + v1 * arcWidth,
	 * y + v2 * h + v3 * arcHeight)
	 */
	static double CTRL_PTS[][] = {
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

	/**
	 */
	public AbstractRoundRectangle2F() {
		//
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth
	 * @param arcHeight
	 */
	public AbstractRoundRectangle2F(Point2f min, Point2f max, double arcWidth, double arcHeight) {
		super(min, max);
		this.setArcWidth(arcWidth);
		this.setArcHeight(arcHeight);
	}

	/**
	 * @param rr
	 */
	public AbstractRoundRectangle2F(AbstractRoundRectangle2F<T> rr) {
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
	public AbstractRoundRectangle2F(double x, double y, double width, double height, double arcWidth, double arcHeight) {
		super(x, y, width, height);
		this.setArcWidth(arcWidth);
		this.setArcHeight(arcHeight);
	}

	@Override
	public void clear() {
		this.set(0f,0f,0f,0f,0f,0f);
	}

	
	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	@Override
	abstract public void set(double x, double y, double width, double height);
	
	/** Change the frame of te rectangle.
	 * 
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	@Override
	abstract public void set(Point2f min, Point2f max);
	
	/** Change the width of the rectangle, not the min corner.
	 * 
	 * @param width
	 */
	@Override
	abstract public void setWidth(double width);

	/** Change the height of the rectangle, not the min corner.
	 * 
	 * @param height
	 */
	@Override
	abstract public void setHeight(double height);

	/** Change the frame of the rectangle.
	 * 
	 * @param p1 is the coordinate of the first corner.
	 * @param p2 is the coordinate of the second corner.
	 */
	@Override
	abstract public void setFromCorners(Point2D p1, Point2D p2);

	/** Change the frame of the rectangle.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 */
	@Override
	abstract public void setFromCorners(double x1, double y1, double x2, double y2);
	
	/**
     * Sets the framing rectangle of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     */
	@Override
	abstract public void setFromCenter(double centerX, double centerY, double cornerX, double cornerY);
	
	/** Replies the min X.
	 * 
	 * @return the min x.
	 */
	@Override
	abstract public double getMinX();

	/** Set the min X.
	 * 
	 * @param x the min x.
	 */
	@Override
	abstract public void setMinX(double x);

	/** Replies the center x.
	 * 
	 * @return the center x.
	 */
	@Override
	abstract public double getCenterX();

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	@Override
	abstract public double getMaxX();

	/** Set the max X.
	 * 
	 * @param x the max x.
	 */
	@Override
	abstract public void setMaxX(double x);

	/** Replies the min y.
	 * 
	 * @return the min y.
	 */
	@Override
	abstract public double getMinY();

	/** Set the min Y.
	 * 
	 * @param y the min y.
	 */
	@Override
	abstract public void setMinY(double y);

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Override
	abstract public double getCenterY();

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	@Override
	abstract public double getMaxY();
	
	/** Set the max Y.
	 * 
	 * @param y the max y.
	 */
	@Override
	abstract public void setMaxY(double y);

	/** Replies the width.
	 * 
	 * @return the width.
	 */
	@Override
	abstract public double getWidth();

	/** Replies the height.
	 * 
	 * @return the height.
	 */
	@Override
	abstract public double getHeight();
	
	/**
	 * Gets the width of the arc that rounds off the corners.
	 * @return the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	abstract public double getArcWidth();

	/**
	 * Gets the height of the arc that rounds off the corners.
	 * @return the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	abstract public double getArcHeight();

	/**
	 * Set the width of the arc that rounds off the corners.
	 * @param a is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	abstract public void setArcWidth(double a);

	/**
	 * Set the height of the arc that rounds off the corners.
	 * @param a is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	abstract public void setArcHeight(double a);

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
	abstract public void set(double x, double y, double width, double height, double arcWidth, double arcHeight);

	@Override
	abstract public void set(Shape2F s);

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(double x, double y) {
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
	public double distanceSquared(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.getDistanceSquared(p);
	}

	@Override
	public double distanceL1(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.getDistanceL1(p);
	}

	@Override
	public double distanceLinf(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.getDistanceLinf(p);
	}

	@Override
	public Point2D getClosestPointTo(Point2D p) {
		double px = p.getX();
		double py = p.getY();
		double rx1 = getMinX();
		double ry1 = getMinY();
		double rx2 = getMaxX();
		double ry2 = getMaxY();

		double aw = getArcWidth();
		double ah = getArcHeight();

		int same = 0;
		double x, y;

		if (px<rx1+aw) {
			if (py<ry1+ah) {
				return AbstractEllipse2F.computeClosestPointToSolidEllipse(
						px, py,
						rx1, ry1,
						aw, ah,
						false);
			}
			if (py>ry2-ah) {
				return AbstractEllipse2F.computeClosestPointToSolidEllipse(
						px, py,
						rx1, ry2-ah,
						aw, ah,
						false);
			}
		}
		else if (px>rx2-aw) {
			if (py<ry1+ah) {
				return AbstractEllipse2F.computeClosestPointToSolidEllipse(
						px, py,
						rx2-aw, ry1,
						aw, ah,
						false);
			}
			if (py>ry2-ah) {
				return AbstractEllipse2F.computeClosestPointToSolidEllipse(
						px, py,
						rx2-aw, ry2-ah,
						aw, ah,
						false);
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
	public Point2D getFarthestPointTo(Point2D p) {
		double px = p.getX();
		double py = p.getY();
		double rx1 = getMinX();
		double ry1 = getMinY();
		double rx2 = getMaxX();
		double ry2 = getMaxY();

		double aw = getArcWidth();
		double ah = getArcHeight();

		if (px<=getCenterX()) {
			if (py<=getCenterY()) {
				return AbstractEllipse2F.computeFarthestPointToShallowEllipse(
						px, py,
						rx2-aw, ry2-ah,
						aw, ah);
			}
			return AbstractEllipse2F.computeFarthestPointToShallowEllipse(
					px, py,
					rx2-aw, ry1,
					aw, ah);
		}
		else if (py<=getCenterY()) {
			return AbstractEllipse2F.computeFarthestPointToShallowEllipse(
					px, py,
					rx1, ry2-ah,
					aw, ah);
		}
		return AbstractEllipse2F.computeFarthestPointToShallowEllipse(
				px, py,
				rx1, ry1,
				aw, ah);
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof AbstractRoundRectangle2F) {
			AbstractRoundRectangle2F<T> rr2d = (AbstractRoundRectangle2F<T>) obj;
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
		bits = 31L * bits + doubleToLongBits(getMinX());
		bits = 31L * bits + doubleToLongBits(getMinY());
		bits = 31L * bits + doubleToLongBits(getMaxX());
		bits = 31L * bits + doubleToLongBits(getMaxY());
		bits = 31L * bits + doubleToLongBits(getArcWidth());
		bits = 31L * bits + doubleToLongBits(getArcHeight());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean intersects(AbstractRectangle2F<?> s) {
		return AbstractRectangle2F.intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return AbstractEllipse2F.intersectsEllipseRectangle(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return AbstractCircle2F.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return AbstractRectangle2F.intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
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
	public boolean intersects(OrientedRectangle2f s) {
		return AbstractOrientedRectangle2F.intersectsOrientedRectangleRectangle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				this.getMinX(), this.getMinY(), getWidth(), getHeight());
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

		private final double x;
		private final double y;
		private final double w;
		private final double h;
		private final double aw;
		private final double ah;
		private int index = 0;

		private double moveX, moveY, lastX,  lastY;

		/**
		 * @param x1
		 * @param y1
		 * @param w1
		 * @param h1
		 * @param aw1
		 * @param ah1
		 */
		public CopyPathIterator(double x1, double y1, double w1, double h1, double aw1, double ah1) {
			this.x = x1;
			this.y = y1;
			this.w = Math.max(0f, w1);
			this.h = Math.max(0f, h1);
			this.aw = Math.min(Math.abs(aw1), w1);
			this.ah = Math.min(Math.abs(ah1), h1);
			if (this.w<=0f || this.h<=0f) {
				this.index = TYPES.length;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<TYPES.length;
		}

		@Override
		public AbstractPathElement2F next() {
			if (this.index>=TYPES.length) throw new NoSuchElementException();
			int idx = this.index;

			AbstractPathElement2F element = null;
			PathElementType type = TYPES[idx];
			double ctrls[] = CTRL_PTS[idx];
			double ix, iy;
			double ctrlx1, ctrly1, ctrlx2, ctrly2;

			switch(type) {
			case MOVE_TO:
				this.moveX = this.lastX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.moveY = this.lastY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				element = new AbstractPathElement2F.MovePathElement2f(
						this.lastX, this.lastY);
				break;
			case LINE_TO:
				ix = this.lastX;
				iy = this.lastY;
				this.lastX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.lastY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				element = new AbstractPathElement2F.LinePathElement2f(
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
				element = new AbstractPathElement2F.CurvePathElement2f(
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
				element = new AbstractPathElement2F.ClosePathElement2f(
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
		private final double x;
		private final double y;
		private final double w;
		private final double h;
		private final double aw;
		private final double ah;
		private int index = 0;

		private double moveX, moveY;
		private final Point2D last = new Point2f();
		private final Point2D ctrl1 = new Point2f();
		private final Point2D ctrl2 = new Point2f();

		/**
		 * @param x1
		 * @param y1
		 * @param w1
		 * @param h1
		 * @param aw1
		 * @param ah1
		 * @param transform1
		 */
		public TransformPathIterator(double x1, double y1, double w1, double h1, double aw1, double ah1, Transform2D transform1) {
			this.transform = transform1;
			this.x = x1;
			this.y = y1;
			this.w = Math.max(0f, w1);
			this.h = Math.max(0f, h1);
			this.aw = Math.min(Math.abs(aw1), w1);
			this.ah = Math.min(Math.abs(ah1), h1);
			if (this.w<=0f || this.h<=0f) {
				this.index = TYPES.length;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<TYPES.length;
		}

		@Override
		public AbstractPathElement2F next() {
			if (this.index>=TYPES.length) throw new NoSuchElementException();
			int idx = this.index;

			AbstractPathElement2F element = null;
			PathElementType type = TYPES[idx];
			double ctrls[] = CTRL_PTS[idx];
			double ix, iy;

			switch(type) {
			case MOVE_TO:
				this.moveX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.moveY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				this.last.set(this.moveX, this.moveY);
				this.transform.transform(this.last);
				element = new AbstractPathElement2F.MovePathElement2f(
						this.last.getX(), this.last.getY());
				break;
			case LINE_TO:
				ix = this.last.getX();
				iy = this.last.getY();
				this.last.set(
						this.x + ctrls[0] * this.w + ctrls[1] * this.aw,
						this.y + ctrls[2] * this.h + ctrls[3] * this.ah);
				this.transform.transform(this.last);
				element = new AbstractPathElement2F.LinePathElement2f(
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
				element = new AbstractPathElement2F.CurvePathElement2f(
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
				element = new AbstractPathElement2F.ClosePathElement2f(
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

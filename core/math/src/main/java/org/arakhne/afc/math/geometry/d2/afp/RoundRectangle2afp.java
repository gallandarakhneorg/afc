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
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D round rectangle on a plane.
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
 */
public interface RoundRectangle2afp<
		ST extends Shape2afp<?, ?, IE, P, B>,
		IT extends RoundRectangle2afp<?, ?, IE, P, B>,
		IE extends PathElement2afp,
		P extends Point2D,
		B extends Rectangle2afp<?, ?, IE, P, B>>
		extends RectangularShape2afp<ST, IT, IE, P, B> {

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
	@Pure
	static boolean containsRoundRectangleRectangle(double rx1, double ry1, double rwidth1, double rheight1, double awidth, double aheight, double rx2, double ry2, double rwidth2, double rheight2) {
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
	@Pure
	static boolean containsRoundRectanglePoint(double rx, double ry, double rwidth, double rheight, double awidth, double aheight, double px, double py) {
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

	/**
	 * Gets the width of the arc that rounds off the corners.
	 * @return the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 */
	@Pure
	double getArcWidth();

	/**
	 * Gets the height of the arc that rounds off the corners.
	 * @return the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 */
	@Pure
	double getArcHeight();

	/**
	 * Set the width of the arc that rounds off the corners.
	 * @param a is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 */
	void setArcWidth(double a);

	/**
	 * Set the height of the arc that rounds off the corners.
	 * @param a is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 */
	void setArcHeight(double a);

	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 * @param arcHeight is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 */
	default void set(double x, double y, double width, double height, double arcWidth, double arcHeight) {
		setFromCorners(x, y, x + width, y + height, arcWidth, arcHeight);
	}

	@Override
	default void setFromCorners(double x1, double y1, double x2, double y2) {
		setFromCorners(x1, y1, x2, y2, getArcWidth(), getArcHeight());
	}
	
	/** Change the frame of the rectangle.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 * @param arcWidth is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 * @param arcHeight is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2afp</code>.
	 */
	void setFromCorners(double x1, double y1, double x2, double y2, double arcWidth, double arcHeight);

	@Override
	default boolean contains(double x, double y) {
		return containsRoundRectanglePoint(
				getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight(),
				x, y);
	}
	
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		return containsRoundRectangleRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}
	
	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		Point2D n = getClosestPointTo(p);
		return n.getDistanceLinf(p);
	}
	
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		return Circle2afp.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}
	
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		return Ellipse2afp.intersectsEllipseRectangle(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}
	
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		return OrientedRectangle2afp.intersectsOrientedRectangleRectangle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				getMinX(), getMinY(), getWidth(), getHeight());
	}
	
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> s) {
		return Rectangle2afp.intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}
	
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		return Rectangle2afp.intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}
	
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		return Rectangle2afp.intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2afp.computeCrossingsFromRect(
				iterator,
				getMinX(), getMinY(), getWidth(), getHeight(),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}
	
	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new RoundRectanglePathIterator<>(this);
		}
		return new TransformedRoundRectanglePathIterator<>(this, transform);
	}
	
	@Pure
	@Override
	default P getClosestPointTo(Point2D p) {
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
		
		GeomFactory2afp<?, P, ?> factory = getGeomFactory();

		if (px<rx1+aw) {
			if (py<ry1+ah) {
				P point = factory.newPoint();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx1, ry1,
						aw, ah,
						point);
				return point;
			}
			if (py>ry2-ah) {
				P point = factory.newPoint();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx1, ry2-ah,
						aw, ah,
						point);
				return point;
			}
		}
		else if (px>rx2-aw) {
			if (py<ry1+ah) {
				P point = factory.newPoint();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx2-aw, ry1,
						aw, ah,
						point);
				return point;
			}
			if (py>ry2-ah) {
				P point = factory.newPoint();
				Ellipse2afp.computeClosestPointToSolidEllipse(
						px, py,
						rx2-aw, ry2-ah,
						aw, ah,
						point);
				return point;
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

		if (same==2) return factory.convertToPoint(p);
		return factory.newPoint(x,y);
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D p) {
		double px = p.getX();
		double py = p.getY();
		double rx1 = getMinX();
		double ry1 = getMinY();
		double rx2 = getMaxX();
		double ry2 = getMaxY();

		double aw = getArcWidth();
		double ah = getArcHeight();

		GeomFactory2afp<?, P, ?> factory = getGeomFactory();
		P point = factory.newPoint();
		
		if (px<=getCenterX()) {
			if (py<=getCenterY()) {
				Ellipse2afp.computeFarthestPointToShallowEllipse(
						px, py,
						rx2-aw, ry2-ah,
						aw, ah,
						point);
			} else { 
				Ellipse2afp.computeFarthestPointToShallowEllipse(
					px, py,
					rx2-aw, ry1,
					aw, ah,
					point);
			}
		} else if (py<=getCenterY()) {
			Ellipse2afp.computeFarthestPointToShallowEllipse(
					px, py,
					rx1, ry2-ah,
					aw, ah,
					point);
		} else {
			Ellipse2afp.computeFarthestPointToShallowEllipse(
				px, py,
				rx1, ry1,
				aw, ah,
				point);
		}
		return point;
	}


	/** Abstract iterator on the path elements of the round rectangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractRoundRectanglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {
				
		private static final double ANGLE = Math.PI / 4;
		
		private static final double A = 1 - Math.cos(ANGLE);
		
		private static final double B = Math.tan(ANGLE);
		
		private static final double C = Math.sqrt(1 + B * B) - 1 + A;
		
		private static final double CV = 4 / 3 * A * B / C;

		private static final double ACV = (1f- CV) / 2;

		/** For each array:
		 * 4 values for each point {v0, v1, v2, v3}:
		 * point = (x + v0 * w + v1 * arcWidth,
		 * y + v2 * h + v3 * arcHeight)
		 */
		protected static double CTRL_PTS[][] = {
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
		protected static PathElementType TYPES[] = {
				PathElementType.MOVE_TO,
				PathElementType.LINE_TO, PathElementType.CURVE_TO,
				PathElementType.LINE_TO, PathElementType.CURVE_TO,
				PathElementType.LINE_TO, PathElementType.CURVE_TO,
				PathElementType.LINE_TO, PathElementType.CURVE_TO,
				PathElementType.CLOSE,
		};

		private final GeomFactory2afp<T, ?, ?> factory;

		/**
		 * @param factory the factory.
		 */
		public AbstractRoundRectanglePathIterator(GeomFactory2afp<T, ?, ?> factory) {
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

		@Pure
		@Override
		public boolean isCurved() {
			return true;
		}

		@Pure
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

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class RoundRectanglePathIterator<T extends PathElement2afp> extends AbstractRoundRectanglePathIterator<T> {

		private double x;

		private double y;
		
		private double w;
		
		private double h;
		
		private double aw;
		
		private double ah;
		
		private int index = 0;

		private double moveX;
		
		private double moveY;
		
		private double lastX;
		
		private double lastY;
		
		/**
		 * @param rectangle the round rectangle to iterate on.
		 */
		public RoundRectanglePathIterator(RoundRectangle2afp<?, ?, T, ?, ?> rectangle) {
			super(rectangle.getGeomFactory());
			if (rectangle.isEmpty()) {
				this.index = TYPES.length;
			} else {
				this.x = rectangle.getMinX();
				this.y = rectangle.getMinY();
				this.w = rectangle.getWidth();
				this.h = rectangle.getHeight();
				this.aw = rectangle.getArcWidth();
				this.ah = rectangle.getArcHeight();
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<TYPES.length;
		}

		@Override
		public T next() {
			if (this.index>=TYPES.length) throw new NoSuchElementException();
			int idx = this.index;

			T element = null;
			PathElementType type = TYPES[idx];
			double ctrls[] = CTRL_PTS[idx];
			double ix, iy;
			double ctrlx1, ctrly1, ctrlx2, ctrly2;

			switch(type) {
			case MOVE_TO:
				this.moveX = this.lastX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.moveY = this.lastY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				element = getGeomFactory().newMovePathElement(
						this.lastX, this.lastY);
				break;
			case LINE_TO:
				ix = this.lastX;
				iy = this.lastY;
				this.lastX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.lastY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				element = getGeomFactory().newLinePathElement(
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
				element = getGeomFactory().newCurvePathElement(
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
				element = getGeomFactory().newClosePathElement(
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

	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedRoundRectanglePathIterator<T extends PathElement2afp> extends AbstractRoundRectanglePathIterator<T> {

		private final Transform2D transform;
		
		private double x;
		
		private double y;
		
		private double w;
		
		private double h;
		
		private double aw;
		
		private double ah;
		
		private int index = 0;

		private double moveX;
		
		private double moveY;
		
		private Point2D last;
		
		private Point2D ctrl1;
		
		private Point2D ctrl2;

		/**
		 * @param rectangle the roudn rectangle to iterate on.
		 * @param transform the transformation to apply.
		 */
		public TransformedRoundRectanglePathIterator(RoundRectangle2afp<?, ?, T, ?, ?> rectangle, Transform2D transform) {
			super(rectangle.getGeomFactory());
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = TYPES.length;
			} else {
				GeomFactory2afp<T, ?, ?> factory = getGeomFactory();
				this.last = factory.newPoint();
				this.ctrl1 = factory.newPoint();
				this.ctrl2 = factory.newPoint();
				this.x = rectangle.getMinX();
				this.y = rectangle.getMinY();
				this.w = rectangle.getWidth();
				this.h = rectangle.getHeight();
				this.aw = rectangle.getArcWidth();
				this.ah = rectangle.getArcHeight();
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<TYPES.length;
		}

		@Override
		public T next() {
			if (this.index>=TYPES.length) throw new NoSuchElementException();
			int idx = this.index;

			T element = null;
			PathElementType type = TYPES[idx];
			double ctrls[] = CTRL_PTS[idx];
			double ix, iy;

			switch(type) {
			case MOVE_TO:
				this.moveX = this.x + ctrls[0] * this.w + ctrls[1] * this.aw;
				this.moveY = this.y + ctrls[2] * this.h + ctrls[3] * this.ah;
				this.last.set(this.moveX, this.moveY);
				this.transform.transform(this.last);
				element = getGeomFactory().newMovePathElement(
						this.last.getX(), this.last.getY());
				break;
			case LINE_TO:
				ix = this.last.getX();
				iy = this.last.getY();
				this.last.set(
						this.x + ctrls[0] * this.w + ctrls[1] * this.aw,
						this.y + ctrls[2] * this.h + ctrls[3] * this.ah);
				this.transform.transform(this.last);
				element = getGeomFactory().newLinePathElement(
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
				element = getGeomFactory().newCurvePathElement(
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
				element = getGeomFactory().newClosePathElement(
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

	}

}

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
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D rectangle on a plane.
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
 * @since 13.0
 */
public interface Rectangle2afp<
		ST extends Shape2afp<?, ?, IE, P, B>,
		IT extends Rectangle2afp<?, ?, IE, P, B>,
		IE extends PathElement2afp,
		P extends Point2D,
		B extends Rectangle2afp<?, ?, IE, P, B>>
		extends RectangularShape2afp<ST, IT, IE, P, B> {

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the first rectangle.
	 * @param y1 is the first corner of the first rectangle.
	 * @param x2 is the second corner of the first rectangle.
	 * @param y2 is the second corner of the first rectangle.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsRectangleRectangle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		assert(x1<=x2);
		assert(y1<=y2);
		assert(x3<=x4);
		assert(y3<=y4);
		return x2 > x3
				&&
				x1 < x4
				&&
				y2 > y3
				&&
				y1 < y4;
	}

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param x3 is the first point of the line.
	 * @param y3 is the first point of the line.
	 * @param x4 is the second point of the line.
	 * @param y4 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsRectangleLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		int a, b;
		a = Segment2afp.ccw(x3, y3, x4, y4, x1, y1, 0.);
		b = Segment2afp.ccw(x3, y3, x4, y4, x2, y1, 0.);
		if (a!=b && b!=0) return true;
		b = Segment2afp.ccw(x3, y3, x4, y4, x2, y2, 0.);
		if (a!=b && b!=0) return true;
		b = Segment2afp.ccw(x3, y3, x4, y4, x1, y2, 0.);
		return (a!=b && b!=0);
	}

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param x3 is the first point of the segment.
	 * @param y3 is the first point of the segment.
	 * @param x4 is the second point of the segment.
	 * @param y4 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsRectangleSegment(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double px1 = x3;
		double py1 = y3;
		double px2 = x4;
		double py2 = y4;

		// Cohen–Sutherland algorithm
		int r1 = MathUtil.getCohenSutherlandCode(x1, y1, x2, y2, px1, py1);
		int r2 = MathUtil.getCohenSutherlandCode(x1, y1, x2, y2, px2, py2);
		boolean accept = false;

		while (true) {
			if ((r1 | r2)==0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept =  true;
				break;//to speed up the algorithm
			}
			if ((r1 & r2)!=0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				break;
			}

			// failed both tests, so calculate the line segment to clip
			// from an outside point to an intersection with clip edge
			double x, y;

			// At least one endpoint is outside the clip rectangle; pick it.
			int outcodeOut = r1!=0 ? r1 : r2;

			// Now find the intersection point;
			// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
			if ((outcodeOut & MathConstants.COHEN_SUTHERLAND_TOP)!=0) { // point is above the clip rectangle
				x = px1 + (px2 - px1) * (y2 - py1) / (py2 - py1);
				y = y2;
			}
			else if ((outcodeOut & MathConstants.COHEN_SUTHERLAND_BOTTOM)!=0) { // point is below the clip rectangle
				x = px1 + (px2 - px1) * (y1- py1) / (py2 - py1);
				y = y1;
			}
			else if ((outcodeOut & MathConstants.COHEN_SUTHERLAND_RIGHT)!=0) {  // point is to the right of clip rectangle
				y = py1 + (py2 - py1) * (x2 - px1) / (px2 - px1);
				x = x2;
			}
			else {
				//else if ((outcodeOut & MathConstants.COHEN_SUTHERLAND_LEFT)!=0) {   // point is to the left of clip rectangle
				y = py1 + (py2 - py1) * (x1 - px1) / (px2 - px1);
				x = x1;
			}

			//NOTE:*****************************************************************************************

			/* if you follow this algorithm exactly(at least for c#), then you will fall into an infinite loop 
            in case a line crosses more than two segments. to avoid that problem, leave out the last else
            if(outcodeOut & LEFT) and just make it else*/

			//**********************************************************************************************

			// Now we move outside point to intersection point to clip
			// and get ready for next pass.
			if (outcodeOut == r1) {
				px1 = x;
				py1 = y;
				r1 = MathUtil.getCohenSutherlandCode(x1, y1, x2, y2, px1, py1);
			}
			else {
				px2 = x;
				py2 = y;
				r2 = MathUtil.getCohenSutherlandCode(x1, y1, x2, y2, px2, py2);
			}
		}

		return accept;
	}

	/** Replies if a rectangle is inside in the rectangle.
	 * 
	 * @param rx1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param ry1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param rwidth1 is the width of the enclosing-candidate rectangle.
	 * @param rheight1 is the height of the enclosing-candidate rectangle.
	 * @param rx2 is the lowest corner of the inner-candidate rectangle.
	 * @param ry2 is the lowest corner of the inner-candidate rectangle.
	 * @param rwidth2 is the width of the inner-candidate rectangle.
	 * @param rheight2 is the height of the inner-candidate rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	@Pure
	public static boolean containsRectangleRectangle(double rx1, double ry1, double rwidth1, double rheight1, double rx2, double ry2, double rwidth2, double rheight2) {
		if (rwidth1<=0f || rwidth2<=0f || rheight1<=0 || rheight2<=0f) {
			return false;
		}
		return (rx2 >= rx1 &&
				ry2 >= ry1 &&
				(rx2 + rwidth2) <= rx1 + rwidth1 &&
				(ry2 + rheight2) <= ry1 + rheight1);
	}

	/** Replies if a point is inside in the rectangle.
	 * 
	 * @param px is the point.
	 * @param py is the point.
	 * @param rx is the lowest corner of the rectangle.
	 * @param ry is the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @return <code>true</code> if the given point is inside the rectangle;
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsRectanglePoint(
			double px, double py,
			double rx, double ry, double rwidth, double rheight) {
		return (px >= rx && px <= (rx + rwidth)) && (py >= ry && py <= (ry + rheight));
	}

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}
		return getMinX() == shape.getMinX()
			&& getMinY() == shape.getMinY()
			&& getMaxX() == shape.getMaxX()
			&& getMaxY() == shape.getMaxY();
	}

	@Override
	default void set(IT s) {
		Rectangle2afp<?, ?, ?, ?, ?> box = s.toBoundingBox();
		setFromCorners(box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		double dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		double dy;
		if (p.getY()<getMinY()) {
			dy = getMinY() - p.getY();
		}
		else if (p.getY()>getMaxY()) {
			dy = p.getY() - getMaxY();
		}
		else {
			dy = 0f;
		}
		return dx*dx+dy*dy;
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		double dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		double dy;
		if (p.getY()<getMinY()) {
			dy = getMinY() - p.getY();
		}
		else if (p.getY()>getMaxY()) {
			dy = p.getY() - getMaxY();
		}
		else {
			dy = 0f;
		}
		return dx + dy;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		double dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		double dy;
		if (p.getY()<getMinY()) {
			dy = getMinY() - p.getY();
		}
		else if (p.getY()>getMaxY()) {
			dy = p.getY() - getMaxY();
		}
		else {
			dy = 0f;
		}
		return Math.max(dx, dy);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return (x>=getMinX() && x<=getMaxX())
				&&
				(y>=getMinY() && y<=getMaxY());
	}

	@Pure
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		return containsRectangleRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** Add the given coordinate in the rectangle.
	 * <p>
	 * The corners of the rectangles are moved to
	 * enclosed the given coordinate.
	 * 
	 * @param p
	 */
	default void add(Point2D p) {
		add(p.getX(), p.getY());
	}

	/** Add the given coordinate in the rectangle.
	 * <p>
	 * The corners of the rectangles are moved to
	 * enclosed the given coordinate.
	 * 
	 * @param x
	 * @param y
	 */
	default void add(double x, double y) {
		if (x<getMinX()) {
			setMinX(x);
		}
		else if (x>getMaxX()) {
			setMaxX(x);
		}
		if (y<getMinY()) {
			setMinY(y);
		}
		else if (y>getMaxY()) {
			setMaxY(y);
		}
	}
	
	/** Compute and replies the union of this rectangle and the given rectangle.
	 * This function does not change this rectangle.
	 * <p>
	 * It is equivalent to (where <code>ur</code> is the union):
	 * <pre><code>
	 * Rectangle2f ur = new Rectangle2f();
	 * Rectangle2f.union(ur, this, r);
	 * </code></pre>
	 * 
	 * @param r
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setUnion(RectangularShape2afp)
	 */
	@Pure
	default B createUnion(RectangularShape2afp<?, ?, ?, ?, ?> r) {
		B rr = getGeomFactory().newBox();
		rr.set(getMinX(), getMinY(), getMaxX(), getMaxY());
		rr.setUnion(r);
		return rr;
	}

	/** Compute and replies the intersection of this rectangle and the given rectangle.
	 * This function does not change this rectangle.
	 * <p>
	 * It is equivalent to (where <code>ir</code> is the intersection):
	 * <pre><code>
	 * Rectangle2f ir = new Rectangle2f();
	 * Rectangle2f.intersection(ir, this, r);
	 * </code></pre>
	 * 
	 * @param r
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setIntersection(RectangularShape2afp)
	 */
	@Pure
	default B createIntersection(RectangularShape2afp<?, ?, ?, ?, ?> r) {
		B rr = getGeomFactory().newBox();
		double x1 = Math.max(getMinX(), r.getMinX());
		double y1 = Math.max(getMinY(), r.getMinY());
		double x2 = Math.min(getMaxX(), r.getMaxX());
		double y2 = Math.min(getMaxY(), r.getMaxY());
		if (x1<=x2 && y1<=y2) {
			rr.setFromCorners(x1, y1, x2, y2);
		}
		else {
			rr.clear();
		}
		return rr;
	}

	/** Compute the union of this rectangle and the given rectangle and
	 * change this rectangle with the result of the union.
	 * <p>
	 * It is equivalent to:
	 * <pre><code>
	 * Rectangle2f.union(this, this, r);
	 * </code></pre>
	 * 
	 * @param r
	 * @see #createUnion(RectangularShape2afp)
	 */
	default void setUnion(RectangularShape2afp<?, ?, ?, ?, ?> r) {
		setFromCorners(
				Math.min(getMinX(), r.getMinX()),
				Math.min(getMinY(), r.getMinY()),
				Math.max(getMaxX(), r.getMaxX()),
				Math.max(getMaxY(), r.getMaxY()));
	}

	/** Compute the intersection of this rectangle and the given rectangle.
	 * This function changes this rectangle.
	 * <p>
	 * It is equivalent to:
	 * <pre><code>
	 * Rectangle2f.intersection(this, this, r);
	 * </code></pre>
	 * 
	 * @param r
	 * @see #createIntersection(RectangularShape2afp)
	 */
	default void setIntersection(RectangularShape2afp<?, ?, ?, ?, ?> r) {
		double x1 = Math.max(getMinX(), r.getMinX());
		double y1 = Math.max(getMinY(), r.getMinY());
		double x2 = Math.min(getMaxX(), r.getMaxX());
		double y2 = Math.min(getMaxY(), r.getMaxY());
		if (x1<=x2 && y1<=y2) {
			setFromCorners(x1, y1, x2, y2);
		}
		else {
			clear();
		}
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> s) {
		return intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}
	
	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		return Ellipse2afp.intersectsEllipseRectangle(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}
	
	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		return Circle2afp.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}
	
	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		return intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		return OrientedRectangle2afp.intersectsOrientedRectangleRectangle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				getMinX(), getMinY(), getWidth(), getHeight());
	}
	
	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		return s.intersects(this);
	}
	
	@Pure
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
	
	/** Move this rectangle to avoid collision 
	 * with the reference rectangle.
	 * 
	 * @param reference is the rectangle to avoid collision with.
	 * @param result the displacement vector.
	 */
	default void avoidCollisionWith(Rectangle2afp<?, ?, ?, ?, ?> reference, Vector2D result) {
		double dx1 = reference.getMaxX() - getMinX();
		double dx2 = getMaxX() - reference.getMinX();
		double dy1 = reference.getMaxY() - getMinY();
		double dy2 = getMaxY() - reference.getMinY();

		double absdx1 = Math.abs(dx1);
		double absdx2 = Math.abs(dx2);
		double absdy1 = Math.abs(dy1);
		double absdy2 = Math.abs(dy2);

		double dx = 0;
		double dy = 0;

		if (dx1>=0 && absdx1<=absdx2 && absdx1<=absdy1 && absdx1<=absdy2) {
			// Move according to dx1
			dx = dx1; 
		}
		else if (dx2>=0 && absdx2<=absdx1 && absdx2<=absdy1 && absdx2<=absdy2) {
			// Move according to dx2
			dx = - dx2;
		}
		else if (dy1>=0 && absdy1<=absdx1 && absdy1<=absdx2 && absdy1<=absdy2) {
			// Move according to dy1
			dy = dy1; 
		}
		else {
			// Move according to dy2
			dy = - dy2;
		}

		set(
				getMinX()+dx,
				getMinY()+dy,
				getWidth(),
				getHeight());

		result.set(dx, dy);
	}

	/** Move this rectangle to avoid collision 
	 * with the reference rectangle.
	 * 
	 * @param reference is the rectangle to avoid collision with.
	 * @param displacementDirection is the direction of the allowed displacement.
	 * @param result the displacement vector.
	 */
	default void avoidCollisionWith(Rectangle2afp<?, ?, ?, ?, ?> reference, Vector2D displacementDirection, Vector2D result) {
		if (displacementDirection==null || displacementDirection.lengthSquared()==0f) {
			avoidCollisionWith(reference, result);
			return;
		}

		double dx1 = reference.getMaxX() - getMinX();
		double dx2 = reference.getMinX() - getMaxX();
		double dy1 = reference.getMaxY() - getMinY();
		double dy2 = reference.getMinY() - getMaxY();

		double absdx1 = Math.abs(dx1);
		double absdx2 = Math.abs(dx2);
		double absdy1 = Math.abs(dy1);
		double absdy2 = Math.abs(dy2);

		double dx, dy;

		if (displacementDirection.getX()<0) {
			dx = -Math.min(absdx1, absdx2);
		}
		else {
			dx = Math.min(absdx1, absdx2);
		}

		if (displacementDirection.getY()<0) {
			dy = -Math.min(absdy1, absdy2);
		}
		else {
			dy = Math.min(absdy1, absdy2);
		}

		set(
				getMinX()+dx,
				getMinY()+dy,
				getWidth(),
				getHeight());

		displacementDirection.set(dx, dy);
		result.set(dx, dy);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D p) {
		double x;
		int same = 0;
		if (p.getX()<getMinX()) {
			x = getMinX();
		}
		else if (p.getX()>getMaxX()) {
			x = getMaxX();
		}
		else {
			x = p.getX();
			++same;
		}
		double y;
		if (p.getY()<getMinY()) {
			y = getMinY();
		}
		else if (p.getY()>getMaxY()) {
			y = getMaxY();
		}
		else {
			y = p.getY();
			++same;
		}
		if (same==2) {
			return getGeomFactory().convertToPoint(p);
		}
		return getGeomFactory().newPoint(x, y);
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D p) {
		double x;
		if (p.getX()<=getCenterX()) {
			x = getMaxX();
		}
		else {
			x = getMinX();
		}
		double y;
		if (p.getY()<=getCenterY()) {
			y = getMaxY();
		}
		else {
			y = getMinY();
		}
		return getGeomFactory().newPoint(x, y);
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new RectanglePathIterator<>(this);
		}
		return new TransformedRectanglePathIterator<>(this, transform);
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
	class RectanglePathIterator<T extends PathElement2afp>
			implements PathIterator2afp<T> {

		private final GeomFactory2afp<T, ?, ?> factory;

		private double x1;
		
		private double y1;
		
		private double x2;
		
		private double y2;
		
		private int index = 0;
		
		/**
		 * @param rectangle the iterated rectangle.
		 */
		public RectanglePathIterator(Rectangle2afp<?, ?, T, ?, ?> rectangle) {
			this.factory = rectangle.getGeomFactory();
			if (rectangle.isEmpty()) {
				this.index = 6;
			} else {
				this.x1 = rectangle.getMinX();
				this.x2 = rectangle.getMinY();
				this.y1 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
			}
		}
		
		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 5;
		}

		@Override
		public T next() {
			int idx = this.index;
			++ this.index;
			switch(idx) {
			case 0:
				return this.factory.newMovePathElement(
						this.x1, this.y1);
			case 1:
				return this.factory.newLinePathElement(
						this.x1, this.y1,
						this.x2, this.y1);
			case 2:
				return this.factory.newLinePathElement(
						this.x2, this.y1,
						this.x2, this.y2);
			case 3:
				return this.factory.newLinePathElement(
						this.x2, this.y2,
						this.x1, this.y2);
			case 4:
				return this.factory.newLinePathElement(
						this.x1, this.y2,
						this.x1, this.y1);
			case 5:
				return this.factory.newClosePathElement(
						this.x1, this.y1,
						this.x1, this.y1);
			default:
				throw new NoSuchElementException();
			}
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
			return false;
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

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.factory;
		}

	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedRectanglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		private final Transform2D transform;

		private final GeomFactory2afp<T, ?, ?> factory;

		private Point2D p1;

		private Point2D p2;

		private double x1;
		
		private double y1;
		
		private double x2;
		
		private double y2;

		private int index = 0;

		/**
		 * @param rectangle the iterated rectangle.
		 * @param transform the transformation.
		 */
		public TransformedRectanglePathIterator(Rectangle2afp<?, ?, T, ?, ?> rectangle, Transform2D transform) {
			this.factory = rectangle.getGeomFactory();
			this.transform = transform;
			if (Math.abs(this.x1-this.x2)<=0f || Math.abs(this.y1-this.y2)<=0f) {
				this.index = 6;
			} else {
				this.p1 = this.factory.newPoint();
				this.p2 = this.factory.newPoint();
				this.x1 = rectangle.getMinX();
				this.x2 = rectangle.getMinY();
				this.y1 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public T next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.factory.newMovePathElement(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.factory.newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.factory.newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.factory.newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.factory.newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return this.factory.newClosePathElement(
						this.p2.getX(), this.p2.getY(),
						this.p2.getX(), this.p2.getY());
			default:
				throw new NoSuchElementException();
			}
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
			return false;
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

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.factory;
		}

	}

}

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
package org.arakhne.afc.math.continous.object2d;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Vector2D;
import org.arakhne.afc.math.matrix.Transform2D;



/** 2D rectangle with floating-point points.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.continuous.Rectangle2f}
 */
@Deprecated
public class Rectangle2f extends AbstractRectangularShape2f<Rectangle2f> {

	private static final long serialVersionUID = 8716296371653330467L;

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
	public static boolean intersectsRectangleRectangle(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
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
	public static boolean intersectsRectangleLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		int a, b;
		a = MathUtil.ccw(x3, y3, x4, y4, x1, y1, false);
		b = MathUtil.ccw(x3, y3, x4, y4, x2, y1, false);
		if (a!=b && b!=0) return true;
		b = MathUtil.ccw(x3, y3, x4, y4, x2, y2, false);
		if (a!=b && b!=0) return true;
		b = MathUtil.ccw(x3, y3, x4, y4, x1, y2, false);
		return (a!=b && b!=0);
	}

	private static final int CS_INSIDE = 0; // 0000
	private static final int CS_LEFT = 1; // 0001
	private static final int CS_RIGHT = 2; // 0010
	private static final int CS_BOTTOM = 4; // 0100
	private static final int CS_TOP = 8; // 1000

	private static int getCohenSutherlandRegion(float x1, float y1, float x2, float y2, float px, float py) {
		int region = CS_INSIDE;
		if (px<x1) {
			region |= CS_LEFT;
		}
		else if (px>x2) {
			region |= CS_RIGHT;
		}
		if (py<y1) {
			region |= CS_BOTTOM;
		}
		else if (py>y2) {
			region |= CS_TOP;
		}
		return region;
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
	public static boolean intersectsRectangleSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float px1 = x3;
		float py1 = y3;
		float px2 = x4;
		float py2 = y4;

		// Cohen–Sutherland algorithm
		int r1 = getCohenSutherlandRegion(x1, y1, x2, y2, px1, py1);
		int r2 = getCohenSutherlandRegion(x1, y1, x2, y2, px2, py2);
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
			float x, y;

			// At least one endpoint is outside the clip rectangle; pick it.
			int outcodeOut = r1!=0 ? r1 : r2;

			// Now find the intersection point;
			// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
			if ((outcodeOut & CS_TOP)!=0) { // point is above the clip rectangle
				x = px1 + (px2 - px1) * (y2 - py1) / (py2 - py1);
				y = y2;
			}
			else if ((outcodeOut & CS_BOTTOM)!=0) { // point is below the clip rectangle
				x = px1 + (px2 - px1) * (y1- py1) / (py2 - py1);
				y = y1;
			}
			else if ((outcodeOut & CS_RIGHT)!=0) {  // point is to the right of clip rectangle
				y = py1 + (py2 - py1) * (x2 - px1) / (px2 - px1);
				x = x2;
			}
			else {
				//else if ((outcodeOut & CS_LEFT)!=0) {   // point is to the left of clip rectangle
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
				r1 = getCohenSutherlandRegion(x1, y1, x2, y2, px1, py1);
			}
			else {
				px2 = x;
				py2 = y;
				r2 = getCohenSutherlandRegion(x1, y1, x2, y2, px2, py2);
			}
		}

		return accept;
	}

	/** Compute the union of r1 and r2.
	 * 
	 * @param dest is the union.
	 * @param r1
	 * @param r2
	 */
	public static void union(Rectangle2f dest, Rectangle2f r1, Rectangle2f r2) {
		dest.setFromCorners(
				Math.min(r1.getMinX(), r2.getMinX()),
				Math.min(r1.getMinY(), r2.getMinY()),
				Math.max(r1.getMaxX(), r2.getMaxX()),
				Math.max(r1.getMaxY(), r2.getMaxY()));
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
	public static boolean containsRectangleRectangle(float rx1, float ry1, float rwidth1, float rheight1, float rx2, float ry2, float rwidth2, float rheight2) {
		if (rwidth1<=0f || rwidth2<=0f || rheight1<=0 || rheight2<=0f) {
			return false;
		}
		return (rx2 >= rx1 &&
				ry2 >= ry1 &&
				(rx2 + rwidth2) <= rx1 + rwidth1 &&
				(ry2 + rheight2) <= ry1 + rheight1);
	}

	/** Compute the intersection of r1 and r2.
	 * 
	 * @param dest is the intersection.
	 * @param r1
	 * @param r2
	 */
	public static void intersection(Rectangle2f dest, Rectangle2f r1, Rectangle2f r2) {
		float x1 = Math.max(r1.getMinX(), r2.getMinX());
		float y1 = Math.max(r1.getMinY(), r2.getMinY());
		float x2 = Math.min(r1.getMaxX(), r2.getMaxX());
		float y2 = Math.min(r1.getMaxY(), r2.getMaxY());
		if (x1<=x2 && y1<=y2) {
			dest.setFromCorners(x1, y1, x2, y2);
		}
		else {
			dest.set(0, 0, 0, 0);
		}
	}

	/**
	 */
	public Rectangle2f() {
		//
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2f(Point2f min, Point2f max) {
		super(min, max);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectangle2f(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	/**
	 * @param r
	 */
	public Rectangle2f(Rectangle2f r) {
		super(r);
	}

	@Override
	public void set(Shape2f s) {
		s.toBoundingBox(this);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2f toBoundingBox() {
		return this;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2D p) {
		float dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		float dy;
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

	/** {@inheritDoc}
	 */
	@Override
	public float distanceL1(Point2D p) {
		float dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		float dy;
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

	/** {@inheritDoc}
	 */
	@Override
	public float distanceLinf(Point2D p) {
		float dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		float dy;
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

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(float x, float y) {
		return (x>=getMinX() && x<=getMaxX())
				&&
				(y>=getMinY() && y<=getMaxY());
	}

	@Override
	public boolean contains(Rectangle2f r) {
		return containsRectangleRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2D getClosestPointTo(Point2D p) {
		float x;
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
		float y;
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
		if (same==2) return p;
		return new Point2f(x,y);
	}

	/** Add the given coordinate in the rectangle.
	 * <p>
	 * The corners of the rectangles are moved to
	 * enclosed the given coordinate.
	 * 
	 * @param p
	 */
	public void add(Point2D p) {
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
	public void add(float x, float y) {
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
	 * @see #union(Rectangle2f, Rectangle2f, Rectangle2f)
	 * @see #setUnion(Rectangle2f)
	 */
	public Rectangle2f createUnion(Rectangle2f r) {
		Rectangle2f rr = new Rectangle2f();
		union(rr, this, r);
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
	 * @see #intersection(Rectangle2f, Rectangle2f, Rectangle2f)
	 * @see #setIntersection(Rectangle2f)
	 */
	public Rectangle2f createIntersection(Rectangle2f r) {
		Rectangle2f rr = new Rectangle2f();
		intersection(rr, this, r);
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
	 * @see #union(Rectangle2f, Rectangle2f, Rectangle2f)
	 * @see #createUnion(Rectangle2f)
	 */
	public void setUnion(Rectangle2f r) {
		union(this, this, r);
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
	 * @see #intersection(Rectangle2f, Rectangle2f, Rectangle2f)
	 * @see #createIntersection(Rectangle2f)
	 */
	public void setIntersection(Rectangle2f r) {
		intersection(this, this, r);
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
		if (obj instanceof Rectangle2f) {
			Rectangle2f rr2d = (Rectangle2f) obj;
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
		return intersectsRectangleRectangle(
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
		return intersectsRectangleSegment(
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

	/** Move this rectangle to avoid collision 
	 * with the reference rectangle.
	 * 
	 * @param reference is the rectangle to avoid collision with.
	 * @return the displacement vector.
	 */
	public Vector2D avoidCollisionWith(Rectangle2f reference) {
		float dx1 = reference.getMaxX() - getMinX();
		float dx2 = getMaxX() - reference.getMinX();
		float dy1 = reference.getMaxY() - getMinY();
		float dy2 = getMaxY() - reference.getMinY();

		float absdx1 = Math.abs(dx1);
		float absdx2 = Math.abs(dx2);
		float absdy1 = Math.abs(dy1);
		float absdy2 = Math.abs(dy2);

		float dx = 0;
		float dy = 0;

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

		return new Vector2f(dx, dy);
	}

	/** Move this rectangle to avoid collision 
	 * with the reference rectangle.
	 * 
	 * @param reference is the rectangle to avoid collision with.
	 * @param displacementDirection is the direction of the allowed displacement.
	 * @return the displacement vector.
	 */
	public Vector2D avoidCollisionWith(Rectangle2f reference, Vector2D displacementDirection) {
		if (displacementDirection==null || displacementDirection.lengthSquared()==0f)
			return avoidCollisionWith(reference);

		float dx1 = reference.getMaxX() - getMinX();
		float dx2 = reference.getMinX() - getMaxX();
		float dy1 = reference.getMaxY() - getMinY();
		float dy2 = reference.getMinY() - getMaxY();

		float absdx1 = Math.abs(dx1);
		float absdx2 = Math.abs(dx2);
		float absdy1 = Math.abs(dy1);
		float absdy2 = Math.abs(dy2);

		float dx, dy;

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
		return displacementDirection;
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator implements PathIterator2f {

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
		 */
		public CopyPathIterator(float x1, float y1, float x2, float y2) {
			this.x1 = Math.min(x1, x2);
			this.y1 = Math.min(y1, y2);
			this.x2 = Math.max(x1, x2);
			this.y2 = Math.max(y1, y2);
			if (Math.abs(this.x1-this.x2)<=0f || Math.abs(this.y1-this.y2)<=0f) {
				this.index = 6;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2f next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new PathElement2f.MovePathElement2f(
						this.x1, this.y1);
			case 1:
				return new PathElement2f.LinePathElement2f(
						this.x1, this.y1,
						this.x2, this.y1);
			case 2:
				return new PathElement2f.LinePathElement2f(
						this.x2, this.y1,
						this.x2, this.y2);
			case 3:
				return new PathElement2f.LinePathElement2f(
						this.x2, this.y2,
						this.x1, this.y2);
			case 4:
				return new PathElement2f.LinePathElement2f(
						this.x1, this.y2,
						this.x1, this.y1);
			case 5:
				return new PathElement2f.ClosePathElement2f(
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

		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}
		
		@Override
		public boolean isPolyline() {
			return true;
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
		private final float x1;
		private final float y1;
		private final float x2;
		private final float y2;
		private int index = 0;

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();

		/**
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @param transform
		 */
		public TransformPathIterator(float x1, float y1, float x2, float y2, Transform2D transform) {
			this.transform = transform;
			this.x1 = Math.min(x1, x2);
			this.y1 = Math.min(y1, y2);
			this.x2 = Math.max(x1, x2);
			this.y2 = Math.max(y1, y2);
			if (Math.abs(this.x1-this.x2)<=0f || Math.abs(this.y1-this.y2)<=0f) {
				this.index = 6;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2f next() {
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
				this.p2.set(this.x2, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return new PathElement2f.ClosePathElement2f(
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
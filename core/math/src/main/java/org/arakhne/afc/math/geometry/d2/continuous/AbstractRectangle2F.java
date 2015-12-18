package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

public abstract class AbstractRectangle2F<T extends AbstractRectangularShape2F<T>> extends AbstractRectangularShape2F<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2715111131004442746L;


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
	public static boolean intersectsRectangleRectangle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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
	public static boolean intersectsRectangleLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		int a, b;
		a = AbstractSegment2F.ccw(x3, y3, x4, y4, x1, y1, 0.);
		b = AbstractSegment2F.ccw(x3, y3, x4, y4, x2, y1, 0.);
		if (a!=b && b!=0) return true;
		b = AbstractSegment2F.ccw(x3, y3, x4, y4, x2, y2, 0.);
		if (a!=b && b!=0) return true;
		b = AbstractSegment2F.ccw(x3, y3, x4, y4, x1, y2, 0.);
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
	public static boolean intersectsRectangleSegment(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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

	/** Compute the union of r1 and r2.
	 * 
	 * @param dest is the union.
	 * @param abstractRectangle2F
	 * @param r2
	 */
	public static void union(AbstractRectangle2F<?> dest, AbstractRectangle2F<?> abstractRectangle2F, AbstractRectangle2F<?> r2) {
		dest.setFromCorners(
				Math.min(abstractRectangle2F.getMinX(), r2.getMinX()),
				Math.min(abstractRectangle2F.getMinY(), r2.getMinY()),
				Math.max(abstractRectangle2F.getMaxX(), r2.getMaxX()),
				Math.max(abstractRectangle2F.getMaxY(), r2.getMaxY()));
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
	public static boolean containsRectanglePoint(
			double px, double py,
			double rx, double ry, double rwidth, double rheight) {
		return (px >= rx && px <= (rx + rwidth)) && (py >= ry && py <= (ry + rheight));
	}

	/** Compute the intersection of r1 and r2.
	 * 
	 * @param dest is the intersection.
	 * @param r1
	 * @param r2
	 */
	public static void intersection(AbstractRectangle2F<?> dest, AbstractRectangle2F<?> r1, AbstractRectangle2F<?> r2) {
		double x1 = Math.max(r1.getMinX(), r2.getMinX());
		double y1 = Math.max(r1.getMinY(), r2.getMinY());
		double x2 = Math.min(r1.getMaxX(), r2.getMaxX());
		double y2 = Math.min(r1.getMaxY(), r2.getMaxY());
		if (x1<=x2 && y1<=y2) {
			dest.setFromCorners(x1, y1, x2, y2);
		}
		else {
			dest.set(0, 0, 0, 0);
		}
	}

	/**
	 */
	public AbstractRectangle2F() {
		//
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public AbstractRectangle2F(FunctionalPoint2D min, FunctionalPoint2D max) {
		super(min, max);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AbstractRectangle2F(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	/**
	 * @param r
	 */
	public AbstractRectangle2F(AbstractRectangle2F<T> r) {
		super(r);
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
	@Pure
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
	@Pure
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
	@Pure
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
	@Pure
	@Override
	abstract public double getCenterY();

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	@Pure
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
	@Pure
	@Override
	abstract public double getWidth();

	/** Replies the height.
	 * 
	 * @return the height.
	 */
	@Pure
	@Override
	abstract public double getHeight();

	
	
	@Override
	abstract public void set(Shape2F s);

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	abstract public Rectangle2f toBoundingBox();

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceSquared(Point2D p) {
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

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceL1(Point2D p) {
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

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceLinf(Point2D p) {
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

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean contains(double x, double y) {
		return (x>=getMinX() && x<=getMaxX())
				&&
				(y>=getMinY() && y<=getMaxY());
	}

	@Pure
	@Override
	public boolean contains(AbstractRectangle2F<?> r) {
		return containsRectangleRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point2D getClosestPointTo(Point2D p) {
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
		if (same==2) return p;
		return new Point2f(x,y);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point2D getFarthestPointTo(Point2D p) {
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
	public void add(double x, double y) {
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
	@Pure
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
	@Pure
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

	@Pure
	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform==null) {
			return new CopyPathIterator2f(
					getMinX(), getMinY(),
					getMaxX(), getMaxY());
		}
		return new TransformPathIterator2f(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				transform);
	}
	
	@Pure
	@Override
	public PathIterator2d getPathIteratorProperty(Transform2D transform) {
		if (transform==null) {
			return new CopyPathIterator2d(
					getMinX(), getMinY(),
					getMaxX(), getMaxY());
		}
		return new TransformPathIterator2d(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				transform);
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof AbstractRectangle2F) {
			AbstractRectangle2F<T> rr2d = (AbstractRectangle2F<T>) obj;
			return ((getMinX() == rr2d.getMinX()) &&
					(getMinY() == rr2d.getMinY()) &&
					(getWidth() == rr2d.getWidth()) &&
					(getHeight() == rr2d.getHeight()));
		}
		return false;
	}

	public void clear() {
		this.set(0f, 0f, 0f, 0f);
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getMinX());
		bits = 31L * bits + doubleToLongBits(getMinY());
		bits = 31L * bits + doubleToLongBits(getMaxX());
		bits = 31L * bits + doubleToLongBits(getMaxY());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean intersects(AbstractRectangle2F<?> s) {
		return intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractEllipse2F<?> s) {
		return AbstractEllipse2F.intersectsEllipseRectangle(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractCircle2F<?> s) {
		return AbstractCircle2F.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractSegment2F<?> s) {
		return intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Pure
	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}
	
	@Pure
	@Override
	public boolean intersects(Path2d s) {
		return intersects(s.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO));
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
	public boolean intersects(PathIterator2d s) {
		// Copied from AWT API
		if (isEmpty()) return false;
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2d.computeCrossingsFromRect(
				s,
				getMinX(), getMinY(), getMaxX(), getMaxY(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(AbstractOrientedRectangle2F<?> s) {
		return AbstractOrientedRectangle2F.intersectsOrientedRectangleRectangle(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				this.getMinX(), this.getMinY(), getWidth(), getHeight());
	}

	@Pure
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
		return displacementDirection;
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator2f implements PathIterator2f {

		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;
		private int index = 0;

		/**
		 * @param x11
		 * @param y11
		 * @param x21
		 * @param y21
		 */
		public CopyPathIterator2f(double x11, double y11, double x21, double y21) {
			this.x1 = Math.min(x11, x21);
			this.y1 = Math.min(y11, y21);
			this.x2 = Math.max(x11, x21);
			this.y2 = Math.max(y11, y21);
			if (Math.abs(this.x1-this.x2)<=0f || Math.abs(this.y1-this.y2)<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2F next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new AbstractPathElement2F.MovePathElement2f(
						this.x1, this.y1);
			case 1:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x1, this.y1,
						this.x2, this.y1);
			case 2:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x2, this.y1,
						this.x2, this.y2);
			case 3:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x2, this.y2,
						this.x1, this.y2);
			case 4:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x1, this.y2,
						this.x1, this.y1);
			case 5:
				return new AbstractPathElement2F.ClosePathElement2f(
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
			return true;
		}

	}
	
	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator2d implements PathIterator2d {

		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;
		private int index = 0;

		/**
		 * @param x11
		 * @param y11
		 * @param x21
		 * @param y21
		 */
		public CopyPathIterator2d(double x11, double y11, double x21, double y21) {
			this.x1 = Math.min(x11, x21);
			this.y1 = Math.min(y11, y21);
			this.x2 = Math.max(x11, x21);
			this.y2 = Math.max(y11, y21);
			if (Math.abs(this.x1-this.x2)<=0f || Math.abs(this.y1-this.y2)<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2D next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new AbstractPathElement2D.MovePathElement2d(
						this.x1, this.y1);
			case 1:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x1, this.y1,
						this.x2, this.y1);
			case 2:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x2, this.y1,
						this.x2, this.y2);
			case 3:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x2, this.y2,
						this.x1, this.y2);
			case 4:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x1, this.y2,
						this.x1, this.y1);
			case 5:
				return new AbstractPathElement2D.ClosePathElement2d(
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
			return true;
		}

	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator2f implements PathIterator2f {

		private final Transform2D transform;
		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;
		private int index = 0;

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();

		/**
		 * @param x11
		 * @param y11
		 * @param x21
		 * @param y21
		 * @param transform1
		 */
		public TransformPathIterator2f(double x11, double y11, double x21, double y21, Transform2D transform1) {
			this.transform = transform1;
			this.x1 = Math.min(x11, x21);
			this.y1 = Math.min(y11, y21);
			this.x2 = Math.max(x11, x21);
			this.y2 = Math.max(y11, y21);
			if (Math.abs(this.x1-this.x2)<=0f || Math.abs(this.y1-this.y2)<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2F next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return new AbstractPathElement2F.ClosePathElement2f(
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
			return true;
		}

	}
	
	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator2d implements PathIterator2d {

		private final Transform2D transform;
		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;
		private int index = 0;

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();

		/**
		 * @param x11
		 * @param y11
		 * @param x21
		 * @param y21
		 * @param transform1
		 */
		public TransformPathIterator2d(double x11, double y11, double x21, double y21, Transform2D transform1) {
			this.transform = transform1;
			this.x1 = Math.min(x11, x21);
			this.y1 = Math.min(y11, y21);
			this.x2 = Math.max(x11, x21);
			this.y2 = Math.max(y11, y21);
			if (Math.abs(this.x1-this.x2)<=0f || Math.abs(this.y1-this.y2)<=0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2D next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.MovePathElement2d(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return new AbstractPathElement2D.ClosePathElement2d(
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
			return true;
		}

	}
}

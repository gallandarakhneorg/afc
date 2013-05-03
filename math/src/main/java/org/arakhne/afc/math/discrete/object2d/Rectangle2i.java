/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.math.discrete.object2d;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;



/** 2D rectangle with integer coordinates.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Rectangle2i extends AbstractRectangularShape2i<Rectangle2i> {

	private static final long serialVersionUID = 9061018868216880896L;

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
	public static boolean intersectsRectangleRectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
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
	
	private static int code(int x, int y, int minx, int miny, int maxx, int maxy) {
		int code = 0;
		if (x<minx) code |= 0x8;
		if (x>maxx) code |= 0x4;
		if (y<miny) code |= 0x2;
		if (y>maxy) code |= 0x1;
		return code;
	}

	/** Replies if a rectangle is intersecting a segment.
	 * <p>
	 * The intersection test is partly based on the Cohen-Sutherland
	 * classification of the segment.
	 * This classification permits to detect the base cases;
	 * and to run a clipping-like algorithm for the intersection
	 * detection.
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
	public static boolean intersectsRectangleSegment(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		int c1 = code(x3, y3, x1, y1, x2, y2);
		int c2 = code(x4, y4, x1, y1, x2, y2);
		
		if (c1==0x0 || c2==0x0) return true;
		if ((c1&c2)!=0x0) return false;
		
		int sx1 = x3;
		int sy1 = y3;
		int sx2 = x4;
		int sy2 = y4;

		Point2i pts = new Point2i();
		Segment2i.LineIterator iterator = new Segment2i.LineIterator(sx1, sy1, sx2, sy2);
		
		while (iterator.hasNext() && c1!=0x0 && c2!=0x0 && (c1&c2)==0) {
			if ((c1&0x1)!=0) {
				do {
					iterator.next(pts);
					sy1 = pts.y();
				}
				while (iterator.hasNext() && sy1!=y2);
				if (sy1!=y2) return false;
				sx1 = pts.x();
			}
			else if ((c1&0x2)!=0) {
				do {
					iterator.next(pts);
					sy1 = pts.y();
				}
				while (iterator.hasNext() && sy1!=y1);
				if (sy1!=y1) return false;
				sx1 = pts.x();
			}
			else if ((c1&0x4)!=0) {
				do {
					iterator.next(pts);
					sx1 = pts.x();
				}
				while (iterator.hasNext() && sx1!=x2);
				if (sx1!=x2) return false;
				sy1 = pts.y();
			}
			else {
				do {
					iterator.next(pts);
					sx1 = pts.x();
				}
				while (iterator.hasNext() && sx1!=x1);
				if (sx1!=x1) return false;
				sy1 = pts.y();
			}
			c1 = code(sx1, sy1, x1, y1, x2, y2);
		}
		
		return c1==0x0 || c2==0x0;
	}

	/** Compute the closest point on the rectangle from the given point.
	 * 
	 * @param minx is the x-coordinate of the lowest coordinate of the rectangle.
	 * @param miny is the y-coordinate of the lowest coordinate of the rectangle.
	 * @param maxx is the x-coordinate of the highest coordinate of the rectangle.
	 * @param maxy is the y-coordinate of the highest coordinate of the rectangle.
	 * @param px is the x-coordinate of the point.
	 * @param py is the y-coordinate of the point.
	 * @return the closest point.
	 */
	public static Point2i computeClosestPoint(int minx, int miny, int maxx, int maxy, int px, int py) {
		int x;
		int same = 0;
		if (px<minx) {
			x = minx;
		}
		else if (px>maxx) {
			x = maxx;
		}
		else {
			x = px;
			++same;
		}
		int y;
		if (py<miny) {
			y = miny;
		}
		else if (py>maxy) {
			y = maxy;
		}
		else {
			y = py;
			++same;
		}
		if (same==2) {
			return new Point2i(px,py);
		}
		return new Point2i(x,y);
	}

	/**
	 */
	public Rectangle2i() {
		//
	}
	
	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2i(Point2i min, Point2i max) {
		setFromCorners(min.x(), min.y(), max.x(), max.y());
	}
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectangle2i(int x, int y, int width, int height) {
		setFromCorners(x, y, x+width, y+height);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2i toBoundingBox() {
		return clone();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2D p) {
		int dx;
		if (p.x()<this.minx) {
			dx = this.minx - p.x();
		}
		else if (p.x()>this.maxx) {
			dx = p.x() - this.maxx;
		}
		else {
			dx = 0;
		}
		int dy;
		if (p.y()<this.miny) {
			dy = this.miny - p.y();
		}
		else if (p.y()>this.maxy) {
			dy = p.y() - this.maxy;
		}
		else {
			dy = 0;
		}
		return dx*dx+dy*dy;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceL1(Point2D p) {
		int dx;
		if (p.x()<this.minx) {
			dx = this.minx - p.x();
		}
		else if (p.x()>this.maxx) {
			dx = p.x() - this.maxx;
		}
		else {
			dx = 0;
		}
		int dy;
		if (p.y()<this.miny) {
			dy = this.miny - p.y();
		}
		else if (p.y()>this.maxy) {
			dy = p.y() - this.maxy;
		}
		else {
			dy = 0;
		}
		return dx + dy;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float distanceLinf(Point2D p) {
		int dx;
		if (p.x()<this.minx) {
			dx = this.minx - p.x();
		}
		else if (p.x()>this.maxx) {
			dx = p.x() - this.maxx;
		}
		else {
			dx = 0;
		}
		int dy;
		if (p.y()<this.miny) {
			dy = this.miny - p.y();
		}
		else if (p.y()>this.maxy) {
			dy = p.y() - this.maxy;
		}
		else {
			dy = 0;
		}
		return Math.max(dx, dy);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2i getClosestPointTo(Point2D p) {
		return computeClosestPoint(this.minx, this.miny, this.maxx, this.maxy, p.x(), p.y());
	}
	
	@Override
	public boolean intersects(Rectangle2i s) {
		return intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Circle2i s) {
		return Circle2i.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Override
	public boolean intersects(Segment2i s) {
		return intersectsRectangleSegment(
				this.minx, this.miny, this.maxx, this.maxy,
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	@Override
	public PathIterator2i getPathIterator(Transform2D transform) {
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
	public boolean contains(int x, int y) {
		return x>=this.minx && x<=this.maxx && y>=this.miny && y<=this.maxy;
	}

	@Override
	public boolean contains(Rectangle2i r) {
		return r.getMinX()>=getMinX() && r.getMaxX()<=getMaxX()
				&& r.getMinY()>=getMinY() && r.getMaxY()<=getMaxY();
	}

	/** Replies the points on the bounds of the rectangle starting from
	 * the top border.
	 * 
	 * @return the points on the bounds of the rectangle.
	 */
	@Override
	public Iterator<Point2i> getPointIterator() {
		return getPointIterator(Side.TOP);
	}

	/** Replies the points on the bounds of the rectangle.
	 *
	 * @param startingBorder is the first border to reply.
	 * @return the points on the bounds of the rectangle.
	 */
	public Iterator<Point2i> getPointIterator(Side startingBorder) {
		return new RectangleSideIterator(this.minx, this.miny, this.maxx, this.maxy, startingBorder);
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator implements PathIterator2i {
		
		private final int x1;
		private final int y1;
		private final int x2;
		private final int y2;
		private int index = 0;
		
		/**
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 */
		public CopyPathIterator(int x1, int y1, int x2, int y2) {
			this.x1 = Math.min(x1, x2);
			this.y1 = Math.min(y1, y2);
			this.x2 = Math.max(x1, x2);
			this.y2 = Math.max(y1, y2);
			if (Math.abs(this.x1-this.x2)<=0 || Math.abs(this.y1-this.y2)<=0) {
				this.index = 6;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2i next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new PathElement2i.MovePathElement2i(
						this.x1, this.y1);
			case 1:
				return new PathElement2i.LinePathElement2i(
						this.x1, this.y1,
						this.x2, this.y1);
			case 2:
				return new PathElement2i.LinePathElement2i(
						this.x2, this.y1,
						this.x2, this.y2);
			case 3:
				return new PathElement2i.LinePathElement2i(
						this.x2, this.y2,
						this.x1, this.y2);
			case 4:
				return new PathElement2i.LinePathElement2i(
						this.x1, this.y2,
						this.x1, this.y1);
			case 5:
				return new PathElement2i.ClosePathElement2i(
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
		
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator implements PathIterator2i {
		
		private final Transform2D transform;
		private final int x1;
		private final int y1;
		private final int x2;
		private final int y2;
		private int index = 0;
		
		private final Point2D p1 = new Point2i();
		private final Point2D p2 = new Point2i();
		
		/**
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @param transform
		 */
		public TransformPathIterator(int x1, int y1, int x2, int y2, Transform2D transform) {
			this.transform = transform;
			this.x1 = Math.min(x1, x2);
			this.y1 = Math.min(y1, y2);
			this.x2 = Math.max(x1, x2);
			this.y2 = Math.max(y1, y2);
			if (Math.abs(this.x1-this.x2)<=0 || Math.abs(this.y1-this.y2)<=0) {
				this.index = 6;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2i next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2i.MovePathElement2i(
						this.p2.x(), this.p2.y());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2i.LinePathElement2i(
						this.p1.x(), this.p1.y(),
						this.p2.x(), this.p2.y());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2i.LinePathElement2i(
						this.p1.x(), this.p1.y(),
						this.p2.x(), this.p2.y());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2i.LinePathElement2i(
						this.p1.x(), this.p1.y(),
						this.p2.x(), this.p2.y());
			case 4:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2i.LinePathElement2i(
						this.p1.x(), this.p1.y(),
						this.p2.x(), this.p2.y());
			case 5:
				return new PathElement2i.ClosePathElement2i(
						this.p2.x(), this.p2.y(),
						this.p2.x(), this.p2.y());
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
		
	}

	/** Sides of a rectangle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static enum Side {
		/** Top.
		 */
		TOP,
		/** Right.
		 */
		RIGHT,
		/** Bottom.
		 */
		BOTTOM,
		/** Left.
		 */
		LEFT;
	}

	/** Iterates on points on the sides of a rectangle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class RectangleSideIterator implements Iterator<Point2i> {

		private final int x0;
		private final int y0;
		private final int x1;
		private final int y1;
		private final Side firstSide;
		
		private Side currentSide;
		private int i;
		
		/**
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @param firstSide
		 */
		public RectangleSideIterator(int x1, int y1, int x2, int y2, Side firstSide) {
			assert(x1<=x2 && y1<=y2);
			this.firstSide = firstSide;
			this.x0 = x1;
			this.y0 = y1;
			this.x1 = x2;
			this.y1 = y2;
			
			this.currentSide = (x2>x1 && y2>y1) ? this.firstSide : null;
			this.i = 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.currentSide!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2i next() {
			int x, y;
			
			switch(this.currentSide) {
			case TOP:
				x = this.x0+this.i;
				y = this.y0;
				break;
			case RIGHT:
				x = this.x1;
				y = this.y0+this.i+1;
				break;
			case BOTTOM:
				x = this.x1-this.i-1;
				y = this.y1;
				break;
			case LEFT:
				x = this.x0;
				y = this.y1-this.i-1;
				break;
			default:
				throw new NoSuchElementException();
			}
			
			++ this.i;
			Side newSide = null;
			
			switch(this.currentSide) {
			case TOP:
				if (x>=this.x1) {
					newSide = Side.RIGHT;
					this.i = 0;
				}
				break;
			case RIGHT:
				if (y>=this.y1) {
					newSide = Side.BOTTOM;
					this.i = 0;
				}
				break;
			case BOTTOM:
				if (x<=this.x0) {
					newSide = Side.LEFT;
					this.i = 0;
				}
				break;
			case LEFT:
				if (y<=this.y0+1) {
					newSide = Side.TOP;
					this.i = 0;
				}
				break;
			default:
				throw new NoSuchElementException();
			}

			if (newSide!=null) {
				this.currentSide = (this.firstSide==newSide) ? null : newSide;
			}
			
			return new Point2i(x,y);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	} // class RectangleIterator
	
}
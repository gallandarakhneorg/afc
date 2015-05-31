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
package org.arakhne.afc.math.geometry.d2.discrete;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;



/** 2D circle with integer coordinates.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Circle2i extends AbstractShape2i<Circle2i> {

	private static final long serialVersionUID = -2396327310912728347L;

	/**
	 * ArcIterator.btan(Math.PI/2)
	 */
	static final double CTRL_VAL = 0.5522847498307933f;

	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static final double PCV = 0.5f + CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static final double NCV = 0.5f - CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static double CTRL_PTS[][] = {
		{  1.0f,  PCV,  PCV,  1.0f,  0.5f,  1.0f },
		{  NCV,  1.0f,  0.0f,  PCV,  0.0f,  0.5f },
		{  0.0f,  NCV,  NCV,  0.0f,  0.5f,  0.0f },
		{  PCV,  0.0f,  1.0f,  NCV,  1.0f,  0.5f }
	};

	/** Replies if the given point is inside the circle.
	 * 
	 * @param cx is the x-coordinate of the circle center
	 * @param cy is the y-coordinate of the circle center
	 * @param cr is the radius of the circle center
	 * @param x is the x-coordinate of the point
	 * @param y is the y-coordinate of the point
	 * @return <code>true</code> if the point is inside the circle.
	 */
	public static boolean contains(int cx, int cy, int cr, int x, int y) {
		int vx = x - cx;
		int vy = y - cy;

		if (vx>=-cr && vx<=cr
				&& vy>=-cr && vy<=cr) {
			int octant;
			boolean xpos = (vx>=0);
			boolean ypos = (vy>=0);
			if (xpos) {
				if (ypos) {
					octant = 0;
				}
				else {
					octant = 2;
				}
			}
			else {
				if (ypos) {
					octant = 6;
				}
				else {
					octant = 4;
				}
			}

			int px, py, ccw, cpx, cpy;
			boolean allNull = true;
			Point2i p;
			CirclePerimeterIterator iterator = new CirclePerimeterIterator(
					cx, cy, cr, octant, octant+2, false);

			while (iterator.hasNext()) {
				p = iterator.next();
				
				// Trivial case
				if (p.x()==x && p.y()==y) return true;
				
				px = cy - p.y();
				py = p.x() - cx;
				cpx = x - p.x();
				cpy = y - p.y();
				ccw = cpx * py - cpy * px;

				if (ccw>0) return false;
				if (ccw<0) allNull = false;
			}

			return !allNull;
		}

		return false;
	}

	/** Replies if the given point is inside the quadrant of the given circle.
	 * 
	 * @param cx is the x-coordinate of the circle center
	 * @param cy is the y-coordinate of the circle center
	 * @param cr is the radius of the circle center
	 * @param quadrant is the quadrant: <table>
	 * <thead>
	 * <th><td>quadrant</td><td>x</td><td>y</td></th>
	 * </thead>
	 * <tbody>
	 * <tr><td>0</td><td>&ge;cx</td><td>&ge;cy</td></tr>
	 * <tr><td>1</td><td>&ge;cx</td><td>&lt;cy</td></tr>
	 * <tr><td>2</td><td>&lt;cx</td><td>&ge;cy</td></tr>
	 * <tr><td>3</td><td>&lt;cx</td><td>&lt;cy</td></tr>
	 * </tbody>
	 * </table>
	 * @param x is the x-coordinate of the point
	 * @param y is the y-coordinate of the point
	 * @return <code>true</code> if the point is inside the circle.
	 */
	public static boolean contains(int cx, int cy, int cr, int quadrant, int x, int y) {
		int vx = x - cx;
		int vy = y - cy;

		if (vx>=-cr && vx<=cr
				&& vy>=-cr && vy<=cr) {
			int octant;
			boolean xpos = (vx>=0);
			boolean ypos = (vy>=0);
			if (xpos) {
				if (ypos) {
					octant = 0;
				}
				else {
					octant = 2;
				}
			}
			else {
				if (ypos) {
					octant = 6;
				}
				else {
					octant = 4;
				}
			}
			
			if (quadrant*2!=octant) return false;

			int px, py, ccw, cpx, cpy;
			Point2i p;
			CirclePerimeterIterator iterator = new CirclePerimeterIterator(
					cx, cy, cr, octant, octant+2, false);

			while (iterator.hasNext()) {
				p = iterator.next();
				px = cy - p.y();
				py = p.x() - cx;
				cpx = x - p.x();
				cpy = y - p.y();
				ccw = cpx * py - cpy * px;

				if (ccw>0) return false;
			}

			return true;
		}

		return false;
	}

	/** Replies the closest point in a circle to a point.
	 * 
	 * @param cx is the center of the circle
	 * @param cy is the center of the circle
	 * @param cr is the radius of the circle
	 * @param x is the point
	 * @param y is the point
	 * @return the closest point in the circle to the point.
	 */
	public static Point2i computeClosestPointTo(int cx, int cy, int cr, int x, int y) {
		int vx = x - cx;
		int vy = y - cy;

		int octant;
		boolean xpos = (vx>=0);
		boolean ypos = (vy>=0);
		if (xpos) {
			if (ypos) {
				octant = 0;
			}
			else {
				octant = 2;
			}
		}
		else {
			if (ypos) {
				octant = 6;
			}
			else {
				octant = 4;
			}
		}

		int d, px, py, cpx, cpy, ccw;
		Point2i p;
		CirclePerimeterIterator iterator = new CirclePerimeterIterator(
				cx, cy, cr, octant, octant+2, false);

		boolean isInside = true;
		int minDist = Integer.MAX_VALUE;
		Point2i close = new Point2i();
		
		while (iterator.hasNext()) {
			p = iterator.next();
			px = cy - p.y();
			py = p.x() - cx;
			cpx = x - p.x();
			cpy = y - p.y();
			ccw = cpx * py - cpy * px;
			if (ccw>=0) {
				isInside = false;
				d = cpx*cpx + cpy*cpy;
				if (d<minDist) {
					minDist = d;
					close.set(p);
				}
			}
		}

		// inside the circle
		if (isInside) close.set(x,y);
		return close;
	}

	/** Replies the farthest point in a circle to a point.
	 * 
	 * @param cx is the center of the circle
	 * @param cy is the center of the circle
	 * @param cr is the radius of the circle
	 * @param x is the point
	 * @param y is the point
	 * @return the farthest point in the circle to the point.
	 */
	public static Point2i computeFarthestPointTo(int cx, int cy, int cr, int x, int y) {
		int vx = x - cx;
		int vy = y - cy;

		int octant;
		boolean xpos = (vx>=0);
		boolean ypos = (vy>=0);
		if (xpos) {
			if (ypos) {
				octant = 4;
			}
			else {
				octant = 6;
			}
		}
		else {
			if (ypos) {
				octant = 2;
			}
			else {
				octant = 0;
			}
		}

		int d, cpx, cpy;
		Point2i p;
		CirclePerimeterIterator iterator = new CirclePerimeterIterator(
				cx, cy, cr, octant, octant+2, false);

		int maxDist = Integer.MIN_VALUE;
		Point2i close = new Point2i(x, y);
		
		while (iterator.hasNext()) {
			p = iterator.next();
			cpx = x - p.x();
			cpy = y - p.y();
			d = cpx*cpx + cpy*cpy;
			if (d>maxDist) {
				maxDist = d;
				close.set(p);
			}
		}

		return close;
	}

	/** Replies if two circles are intersecting.
	 * 
	 * @param x1 is the center of the first circle
	 * @param y1 is the center of the first circle
	 * @param radius1 is the radius of the first circle
	 * @param x2 is the center of the second circle
	 * @param y2 is the center of the second circle
	 * @param radius2 is the radius of the second circle
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsCircleCircle(int x1, int y1, int radius1, int x2, int y2, int radius2) {
		Point2i c = computeClosestPointTo(x1, y1, radius1, x2, y2);
		return contains(x2, y2, radius2, c.x(), c.y());
	}

	/** Replies if a circle and a rectangle are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first corner of the rectangle.
	 * @param y2 is the first corner of the rectangle.
	 * @param x3 is the second corner of the rectangle.
	 * @param y3 is the second corner of the rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsCircleRectangle(int x1, int y1, int radius, int x2, int y2, int x3, int y3) {
		Point2i c = Rectangle2i.computeClosestPoint(x2, y2, x3, y3, x1, y1);
		return contains(x1, y1, radius, c.x(), c.y());
	}

	/** Replies if a circle and a segment are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param x3 is the second point of the segment.
	 * @param y3 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsCircleSegment(int x1, int y1, int radius, int x2, int y2, int x3, int y3) {
		Point2i p = Segment2i.computeClosestPointTo(x2, y2, x3, y3, x1, y1);
		return contains(x1, y1, radius, p.x(), p.y());
	}

	/** X-coordinate of the center of the circle. */
	protected int cx = 0;
	/** Y-coordinate of the center of the circle. */
	protected int cy = 0;
	/** Radius of the circle. */
	protected int radius = 0;

	/**
	 */
	public Circle2i() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Circle2i(Point2D center, int radius) {
		set(center, radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle2i(int x, int y, int radius) {
		set(x, y, radius);
	}

	/**
	 * @param c
	 * 
	 */
	public Circle2i(Circle2i c) {
		this.cx = c.cx;
		this.cy = c.cy;
		this.radius = c.radius;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.cx = this.cy = 0;
		this.radius = 0;
	}

	/** Replies if this circle is empty.
	 * The circle is empty when the radius is nul.
	 * 
	 * @return <code>true</code> if the radius is nul;
	 * otherwise <code>false</code>.
	 */
	@Override
	public boolean isEmpty() {
		return this.radius<=0;
	}

	/** Change the frame of the circle.
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void set(int x, int y, int radius) {
		this.cx = x;
		this.cy = y;
		this.radius = Math.abs(radius);
	}
	
	@Override
	public void set(Shape2i s) {
		if (s instanceof Circle2i) {
			Circle2i c = (Circle2i) s;
			set(c.getX(), c.getY(), c.getRadius());
		} else {
			Rectangle2i r = s.toBoundingBox();
			set(r.getCenterX(), r.getCenterY(),
					Math.min(r.getWidth(), r.getHeight()) / 2);
		}
	}

	/** Change the frame of te circle.
	 * 
	 * @param center
	 * @param radius
	 */
	public void set(Point2D center, int radius) {
		this.cx = center.x();
		this.cy = center.y();
		this.radius = Math.abs(radius);
	}

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	public int getX() {
		return this.cx;
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	public int getY() {
		return this.cy;
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	public Point2i getCenter() {
		return new Point2i(this.cx, this.cy);
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	public int getRadius() {
		return this.radius;
	}

	@Override
	public Rectangle2i toBoundingBox() {
		Rectangle2i r = new Rectangle2i();
		r.setFromCorners(
				this.cx-this.radius,
				this.cy-this.radius,
				this.cx+this.radius,
				this.cy+this.radius);
		return r;
	}

	@Override
	public void toBoundingBox(Rectangle2i box) {
		box.setFromCorners(
				this.cx-this.radius,
				this.cy-this.radius,
				this.cx+this.radius,
				this.cy+this.radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceSquared(Point2D p) {
		Point2i c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceL1(Point2D p) {
		Point2i c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceLinf(Point2D p) {
		Point2i c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2i getClosestPointTo(Point2D p) {
		return computeClosestPointTo(this.cx, this.cy, this.radius, p.x(), p.y());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2i getFarthestPointTo(Point2D p) {
		return computeFarthestPointTo(this.cx, this.cy, this.radius, p.x(), p.y());
	}

	@Override
	public boolean intersects(Rectangle2i s) {
		return intersectsCircleRectangle(
				getX(), getY(), getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Override
	public boolean intersects(Circle2i s) {
		return intersectsCircleCircle(
				getX(), getY(), getRadius(),
				s.getX(), s.getY(), s.getRadius());
	}

	@Override
	public boolean intersects(Segment2i s) {
		return intersectsCircleSegment(
				getX(), getY(), getRadius(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Override
	public Shape2i createTransformedShape(Transform2D transform) {
		if (transform==null || transform.isIdentity()) return clone();
		return new Path2i(getPathIterator(transform));
	}

	@Override
	public void translate(int dx, int dy) {
		this.cx += dx;
		this.cy += dy;
	}

	@Override
	public boolean contains(int x, int y) {
		return contains(this.cx, this.cy, this.radius, x, y);
	}

	private static void m(int[] quadrants, int k, int x, int y) {
		if (x>0) {
			if (y>0) {
				quadrants[0] |= k;
			}
			else {
				quadrants[1] |= k;
			}
		}
		else {
			if (y>0) {
				quadrants[3] |= k;
			}
			else {
				quadrants[2] |= k;
			}
		}
	}

	@Override
	public boolean contains(Rectangle2i r) {
		int vx1 = r.getMinX() - this.cx;
		int vy1 = r.getMinY() - this.cy;
		int vx2 = r.getMaxX() - this.cx;
		int vy2 = r.getMaxY() - this.cy;

		if (vx1>=-this.radius && vx1<=this.radius && vy1>=-this.radius && vy1<=this.radius &&
				vx2>=-this.radius && vx2<=this.radius && vy2>=-this.radius && vy2<=this.radius) {
			int[] quadrants = new int[4];
			int[] x = new int[] {vx1, vx2, vx2, vx1};
			int[] y = new int[] {vy1, vy1, vy2, vy2};
			for(int i=0; i<4; ++i) {
				m(quadrants, (1<<i), x[i], y[i]);
			}

			for(int i=0; i<quadrants.length; ++i) {
				if (quadrants[i]!=0) {
					CirclePerimeterIterator iterator = new CirclePerimeterIterator(
							this.cx, this.cy, this.radius, i*2, i*2+2, false);
					int px, py, ccw, cpx, cpy;
					Point2i p;

					while (iterator.hasNext()) {
						p = iterator.next();
						px = this.cy - p.y();
						py = p.x() - this.cx;

						for(int j=0; j<4; ++j) {
							if ((quadrants[i] & (1<<j))!=0) {
								cpx = x[j] - p.x();
								cpy = y[j] - p.y();
								ccw = cpx * py - cpy * px;				
								if (ccw>0) return false;
							}
						}
					}
				}
			}

			return true;
		}

		return false;
	}

	/** Replies the points of the circle perimeters starting by the first octant.
	 * 
	 * @return the points on the perimeters.
	 */
	@Override
	public Iterator<Point2i> getPointIterator() {
		return new CirclePerimeterIterator(this.cx, this.cy, this.radius, 0, 8, true);
	}

	/** Replies the points of the circle perimeters starting by the first octant.
	 * 
	 * @param firstOctantIndex is the index of the first octant (see figure) to treat.
	 * @param nbOctants is the number of octants to traverse (greater than zero).
	 * @return the points on the perimeters.
	 */
	public Iterator<Point2i> getPointIterator(int firstOctantIndex, int nbOctants) {
		return getPointIterator(this.cx, this.cy,  this.radius, firstOctantIndex, nbOctants);
	}

	/** Replies the points of the circle perimeters starting by the first octant.
	 * 
	 * @param cx is the center of the radius.
	 * @param cy is the center of the radius.
	 * @param radius is the radius of the radius.
	 * @param firstOctantIndex is the index of the first octant (see figure) to treat.
	 * @param nbOctants is the number of octants to traverse (greater than zero).
	 * @return the points on the perimeters.
	 */
	public static Iterator<Point2i> getPointIterator(int cx, int cy,  int radius, int firstOctantIndex, int nbOctants) {
		int startOctant, maxOctant;
		if (firstOctantIndex<=0)
			startOctant = 0;
		else if (firstOctantIndex>=8)
			startOctant = 7;
		else
			startOctant = firstOctantIndex;
		maxOctant = startOctant + nbOctants;
		if (maxOctant>8) maxOctant = 8;
		return new CirclePerimeterIterator(
				cx, cy, radius,
				startOctant, maxOctant, true);
	}

	@Override
	public PathIterator2i getPathIterator(Transform2D transform) {
		if (transform==null || transform.isIdentity())
			return new CopyPathIterator(this.cx, this.cy, this.radius);
		return new TransformPathIterator(this.cx, this.cy, this.radius, transform);
	}

	/** Iterates on points on the perimeter of a circle.
	 * <p>
	 * The rastrerization is based on a Bresenham algorithm.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CirclePerimeterIterator implements Iterator<Point2i> {

		private final int cx;
		private final int cy;
		private final int cr;

		private final boolean skip;
		private final int maxOctant;

		private int currentOctant;
		private int x, y, d;
		
		private Point2i next = null;
		
		private final Set<Point2i> junctionPoint = new TreeSet<>(new Tuple2iComparator());

		/**
		 * @param x
		 * @param y
		 * @param r
		 * @param initialOctant
		 * @param maxOctant
		 * @param skip
		 */
		public CirclePerimeterIterator(int x, int y, int r, int initialOctant, int maxOctant, boolean skip) {
			assert(r>=0);
			this.cx = x;
			this.cy = y;
			this.cr = r;
			this.skip = skip;
			this.maxOctant = maxOctant;
			this.currentOctant = initialOctant;
			reset();
			searchNext();
		}

		private void reset() {
			this.x = 0;
			this.y = this.cr;
			this.d = 3 - 2 * this.cr;
			if (this.skip && (this.currentOctant==3 || this.currentOctant==4 || this.currentOctant==6 || this.currentOctant==7)) {
				// skip the first point because already replied in previous octant
				if (this.d<=0) {
					this.d += 4 * this.x + 6;
				}
				else {
					this.d += 4 * (this.x - this.y) + 10;
					--this.y;
				}
				++this.x;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}
		
		private void searchNext() {
			if (this.currentOctant>=this.maxOctant) {
				this.next = null;
			}
			else {
				this.next = new Point2i();
				while (true) {
					switch(this.currentOctant) {
					case 0:
						this.next.set(this.cx + this.x, this.cy + this.y);
						break;
					case 1:
						this.next.set(this.cx + this.y, this.cy + this.x);
						break;
					case 2:
						this.next.set(this.cx + this.x, this.cy - this.y);
						break;
					case 3:
						this.next.set(this.cx + this.y, this.cy - this.x);
						break;
					case 4:
						this.next.set(this.cx - this.x, this.cy - this.y);
						break;
					case 5:
						this.next.set(this.cx - this.y, this.cy - this.x);
						break;
					case 6:
						this.next.set(this.cx - this.x, this.cy + this.y);
						break;
					case 7:
						this.next.set(this.cx - this.y, this.cy + this.x);
						break;
					default:
						throw new NoSuchElementException();
					}
		
					if (this.d<=0) {
						this.d += 4 * this.x + 6;
					}
					else {
						this.d += 4 * (this.x - this.y) + 10;
						--this.y;
					}
					++this.x;
	
					if (this.x>this.y) {
						// The octant is finished.
						// Save the junction.
						boolean cont = this.junctionPoint.contains(this.next);
						if (!cont) this.junctionPoint.add(new Point2i(this.next));
						// Goto next.
						++this.currentOctant;
						reset();
						if (this.currentOctant>=this.maxOctant) {
							if (cont) this.next = null;
							cont = false;
						}
						if (!cont) return;
					}
					else {
						return;
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2i next() {
			Point2i pixel = this.next;
			if (pixel==null) throw new NoSuchElementException();
			searchNext();
			return pixel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class CirclePerimeterIterator

	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator implements PathIterator2i {

		private final int x;
		private final int y;
		private final int r;
		private int index = 0;
		private int movex, movey;
		private int lastx, lasty;

		/**
		 * @param x
		 * @param y
		 * @param r
		 */
		public CopyPathIterator(int x, int y, int r) {
			this.r = Math.max(0, r);
			this.x = x - this.r;
			this.y = y - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2i next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			if (idx==0) {
				int dr = 2 * this.r;
				double ctrls[] = CTRL_PTS[3];
				this.movex = (int)(this.x + ctrls[4] * dr);
				this.movey = (int)(this.y + ctrls[5] * dr);
				this.lastx = this.movex;
				this.lasty = this.movey;
				return new PathElement2i.MovePathElement2i(
						this.lastx, this.lasty);
			}
			else if (idx<5) {
				int dr = 2 * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				int ppx = this.lastx;
				int ppy = this.lasty;
				this.lastx = (int)(this.x + ctrls[4] * dr);
				this.lasty = (int)(this.y + ctrls[5] * dr);
				return new PathElement2i.CurvePathElement2i(
						ppx, ppy,
						(int)(this.x + ctrls[0] * dr),
						(int)(this.y + ctrls[1] * dr),
						(int)(this.x + ctrls[2] * dr),
						(int)(this.y + ctrls[3] * dr),
						this.lastx, this.lasty);
			}
			int ppx = this.lastx;
			int ppy = this.lasty;
			this.lastx = this.movex;
			this.lasty = this.movey;
			return new PathElement2i.ClosePathElement2i(
					ppx, ppy,
					this.lastx, this.lasty);
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

	/** Iterator on the path elements of the circle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator implements PathIterator2i {

		private final Point2D p1 = new Point2i();
		private final Point2D p2 = new Point2i();
		private final Point2D ptmp1 = new Point2i();
		private final Point2D ptmp2 = new Point2i();
		private final Transform2D transform;
		private final int x;
		private final int y;
		private final int r;
		private int index = 0;
		private int movex, movey;

		/**
		 * @param x
		 * @param y
		 * @param r
		 * @param transform
		 */
		public TransformPathIterator(int x, int y, int r, Transform2D transform) {
			assert(transform!=null);
			this.transform = transform;
			this.r = Math.max(0, r);
			this.x = x - this.r;
			this.y = y - this.r;
			if (this.r<=0f) {
				this.index = 6;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2i next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			if (idx==0) {
				int dr = 2 * this.r;
				double ctrls[] = CTRL_PTS[3];
				this.movex = (int)(this.x + ctrls[4] * dr);
				this.movey = (int)(this.y + ctrls[5] * dr);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				return new PathElement2i.MovePathElement2i(
						this.p2.x(), this.p2.y());
			}
			else if (idx<5) {
				int dr = 2 * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				this.p1.set(this.p2);
				this.p2.set(
						(this.x + ctrls[4] * dr),
						(this.y + ctrls[5] * dr));
				this.transform.transform(this.p2);
				this.ptmp1.set(
						(this.x + ctrls[0] * dr),
						(this.y + ctrls[1] * dr));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						(this.x + ctrls[2] * dr),
						(this.y + ctrls[3] * dr));
				this.transform.transform(this.ptmp2);
				return new PathElement2i.CurvePathElement2i(
						this.p1.x(), this.p1.y(),
						this.ptmp1.x(), this.ptmp1.y(),
						this.ptmp2.x(), this.ptmp2.y(),
						this.p2.x(), this.p2.y());
			}
			this.p1.set(this.p2);
			this.p2.set(this.movex, this.movey);
			this.transform.transform(this.p2);
			return new PathElement2i.ClosePathElement2i(
					this.p1.x(), this.p1.y(),
					this.p2.x(), this.p2.y());
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
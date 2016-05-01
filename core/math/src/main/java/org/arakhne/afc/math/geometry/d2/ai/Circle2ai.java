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

package org.arakhne.afc.math.geometry.d2.ai;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2iComparator;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai.CrossingComputationType;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D circle on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Circle2ai<
		ST extends Shape2ai<?, ?, IE, P, V, B>,
		IT extends Circle2ai<?, ?, IE, P, V, B>,
		IE extends PathElement2ai,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, IE, P, V, B>>
		extends Shape2ai<ST, IT, IE, P, V, B> {

	/** Replies if the given point is inside the circle.
	 * 
	 * @param cx is the x-coordinate of the circle center
	 * @param cy is the y-coordinate of the circle center
	 * @param cr is the radius of the circle center
	 * @param x is the x-coordinate of the point
	 * @param y is the y-coordinate of the point
	 * @return <code>true</code> if the point is inside the circle.
	 */
	@Pure
	static boolean contains(int cx, int cy, int cr, int x, int y) {
		assert (cr >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$
		
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
			Point2D<?, ?> p;
			CirclePerimeterIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
					new CirclePerimeterIterator<>(
							InnerComputationGeomFactory.SINGLETON, 
							cx, cy, cr, octant, octant+2, false);

			while (iterator.hasNext()) {
				p = iterator.next();
				
				// Trivial case
				if (p.ix()==x && p.iy()==y) return true;
				
				px = cy - p.iy();
				py = p.ix() - cx;
				cpx = x - p.ix();
				cpy = y - p.iy();
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
	@Pure
	static boolean contains(int cx, int cy, int cr, int quadrant, int x, int y) {
		assert (cr >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$
		assert (quadrant >= 0 && quadrant <= 3) : "invalid quadrant value"; //$NON-NLS-1$

		int vx = x - cx;
		int vy = y - cy;

		if (vx>=-cr && vx<=cr && vy>=-cr && vy<=cr) {
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
			Point2D<?, ?> p;
			CirclePerimeterIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
					new CirclePerimeterIterator<>(
							InnerComputationGeomFactory.SINGLETON, 
							cx, cy, cr, octant, octant+2, false);

			while (iterator.hasNext()) {
				p = iterator.next();
				px = cy - p.iy();
				py = p.ix() - cx;
				cpx = x - p.ix();
				cpy = y - p.iy();
				ccw = cpx * py - cpy * px;

				if (ccw>0) return false;
			}

			return true;
		}

		return false;
	}

	/** Replies the closest point in a circle to a point.
	 * 
	 * <p>The closest point is the point on the perimeter or inside the circle's disk that 
	 * has the lowest Manhatan distance to the given origin point.
	 * 
	 * @param cx is the center of the circle
	 * @param cy is the center of the circle
	 * @param cr is the radius of the circle
	 * @param x is the point
	 * @param y is the point
	 * @param result the closest point in the circle to the point.
	 */
	@Pure
	static void computeClosestPointTo(int cx, int cy, int cr, int x, int y, Point2D<?, ?> result) {
		assert (cr >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$
		assert (result != null) : "Result point must be positive or zero."; //$NON-NLS-1$
		
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
		Point2D<?, ?> p;
		CirclePerimeterIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
				new CirclePerimeterIterator<>(
						InnerComputationGeomFactory.SINGLETON, 
						cx, cy, cr, octant, octant+2, false);

		boolean isInside = true;
		int minDist = Integer.MAX_VALUE;
		
		while (iterator.hasNext()) {
			p = iterator.next();
			px = cy - p.iy();
			py = p.ix() - cx;
			cpx = x - p.ix();
			cpy = y - p.iy();
			ccw = cpx * py - cpy * px;
			if (ccw >= 0) {
				isInside = false;
				// Mahantan distance
				d = Math.abs(cpx) + Math.abs(cpy);
				if (d < minDist) {
					minDist = d;
					result.set(p);
				}
			}
		}

		// inside the circle
		if (isInside) {
			result.set(x,y);
		}
	}

	/** Replies the farthest point in a circle to a point.
	 * 
	 * <p>The farthest point is the point on the perimeter of the circle that has the highest Manhatan distance
	 * to the given origin point.
	 * 
	 * @param cx is the center of the circle
	 * @param cy is the center of the circle
	 * @param cr is the radius of the circle
	 * @param x is the point
	 * @param y is the point
	 * @param result the farthest point in the circle to the point.
	 */
	@Pure
	static void computeFarthestPointTo(int cx, int cy, int cr, int x, int y, Point2D<?, ?> result) {
		assert (cr >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$

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

		int l1, linfinv, cpx, cpy;
		Point2D<?, ?> p;
		CirclePerimeterIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
				new CirclePerimeterIterator<>(
						InnerComputationGeomFactory.SINGLETON, 
						cx, cy, cr, octant, octant+2, false);

		int maxL1Dist = Integer.MIN_VALUE;
		int maxLinfDist = Integer.MIN_VALUE;
		result.set(x, y);
		
		while (iterator.hasNext()) {
			p = iterator.next();
			cpx = Math.abs(p.ix() - x);
			cpy = Math.abs(p.iy() - y);
			// Mahantan distance
			l1 = cpx + cpy;
			linfinv = Math.min(cpx, cpy);
			if (l1 > maxL1Dist || (l1 == maxL1Dist && linfinv < maxLinfDist)) {
				maxL1Dist = l1;
				maxLinfDist = linfinv;
				result.set(p);
			}
		}
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
	@Pure
	static boolean intersectsCircleCircle(int x1, int y1, int radius1, int x2, int y2, int radius2) {
		assert (radius1 >= 0) : "Radius of the first circle must be positive or zero."; //$NON-NLS-1$
		assert (radius2 >= 0) : "Radius of the second circle must be positive or zero."; //$NON-NLS-1$
		Point2D<?, ?> point = new InnerComputationPoint2ai();
		computeClosestPointTo(x1, y1, radius1, x2, y2, point);
		return contains(x2, y2, radius2, point.ix(), point.iy());
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
	@Pure
	static boolean intersectsCircleRectangle(int x1, int y1, int radius, int x2, int y2, int x3, int y3) {
		assert (radius >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$
		Point2D<?, ?> point = new InnerComputationPoint2ai();
		Rectangle2ai.computeClosestPoint(x2, y2, x3, y3, x1, y1, point);
		return contains(x1, y1, radius, point.ix(), point.iy());
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
		assert (radius >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$
		Point2D<?, ?> point = new InnerComputationPoint2ai();
		Segment2ai.computeClosestPointTo(x2, y2, x3, y3, x1, y1, point);
		return contains(x1, y1, radius, point.ix(), point.iy());
	}

	/** Replies the points of the circle perimeters starting by the first octant.
	 * 
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @param cx is the center of the radius.
	 * @param cy is the center of the radius.
	 * @param radius is the radius of the radius.
	 * @param firstOctantIndex is the index of the first octant to treat (value in [0;7].
	 * @param nbOctants is the number of octants to traverse (value in [0; 7 - firstOctantIndex].
	 * @param factory the factory to use for creating the points.
	 * @return the points on the perimeters.
	 */
	@Pure
	static <P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>>
	Iterator<P> getPointIterator(int cx, int cy,  int radius, int firstOctantIndex, int nbOctants,
			GeomFactory2ai<?, P, V, ?> factory) {
		assert (radius >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$
		assert (firstOctantIndex >= 0 && firstOctantIndex < 8) : "invalid quadrant value"; //$NON-NLS-1$
		int maxOctant;
		maxOctant = Math.min(8, firstOctantIndex + nbOctants);
		if (maxOctant > 8) {
			maxOctant = 8;
		}
		return new CirclePerimeterIterator<>(
				factory,
				cx, cy, radius,
				firstOctantIndex, maxOctant, true);
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
		return getX() == shape.getX()
			&& getY() == shape.getY()
			&& getRadius() == shape.getRadius();
	}
	
	@Override
	default void clear() {
		set(0, 0, 0);
	}

	@Pure
	@Override
	default boolean isEmpty() {
		return getRadius() <= 0;
	}

	@Override
	default void set(IT s) {
		set(s.getX(), s.getY(), s.getRadius());
	}

	/** Change the circle.
	 * 
	 * @param center the center of the circle.
	 * @param radius the radius of the circle.
	 */
	default void set(Point2D<?, ?> center, int radius) {
		set(center.ix(), center.iy(), Math.abs(radius));
	}

	/** Change the circle's center.
	 * 
	 * @param center the center of the circle.
	 */
	default void setCenter(Point2D<?, ?> center) {
		set(center.ix(), center.iy(), getRadius());
	}

	/** Change the circle's center.
	 * 
	 * @param x x coordinate of the center of the circle.
	 * @param y y coordinate of the center of the circle.
	 */
	default void setCenter(int x, int y) {
		set(x, y, getRadius());
	}

	/** Change the circle.
	 * 
	 * @param x the x coordinate of the center.
	 * @param y the y coordinate of the center.
	 * @param radius  the radiusof the center.
	 */
	void set(int x, int y, int radius);

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	@Pure
	int getX();

	/** Change the center X.
	 * 
	 * @param x the center x.
	 */
	@Pure
	void setX(int x);

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	int getY();

	/** Change the center Y.
	 * 
	 * @param y the center y.
	 */
	@Pure
	void setY(int y);

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	default P getCenter() {
		return getGeomFactory().newPoint(getX(), getY());
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	@Pure
	int getRadius();

	/** Change the radius.
	 * 
	 * @param radius the radius.
	 */
	@Pure
	void setRadius(int radius);

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "Box must be not null."; //$NON-NLS-1$
		int centerX = getX();
		int centerY = getY();
		int radius = getRadius();
		box.setFromCorners(
				centerX - radius,
				centerY - radius,
				centerX + radius,
				centerY + radius);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null."; //$NON-NLS-1$
		P c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null."; //$NON-NLS-1$
		P c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> p) {
		P c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null."; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeClosestPointTo(getX(), getY(), getRadius(), p.ix(), p.iy(), point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null."; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeFarthestPointTo(getX(), getY(), getRadius(), p.ix(), p.iy(), point);
		return point;
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null."; //$NON-NLS-1$
		return intersectsCircleRectangle(
				getX(), getY(), getRadius(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Pure
	@Override
	default boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null."; //$NON-NLS-1$
		return intersectsCircleCircle(
				getX(), getY(), getRadius(),
				s.getX(), s.getY(), s.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null."; //$NON-NLS-1$
		return intersectsCircleSegment(
				getX(), getY(), getRadius(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2ai<?> iterator) {
		assert (iterator != null) : "Iterator must be not null."; //$NON-NLS-1$
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2ai.computeCrossingsFromCircle(
				0,
				iterator,
				getX(), getY(), getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	@Pure
	@Override
	default boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Override
	default void translate(int dx, int dy) {
		setCenter(getX() + dx, getY() + dy);
	}

	@Pure
	@Override
	default boolean contains(int x, int y) {
		return contains(getX(), getY(), getRadius(), x, y);
	}

	@Pure
	@Override
	default boolean contains(Rectangle2ai<?, ?, ?, ?, ?, ?> box) {
		assert (box != null) : "Rectangle must be not null."; //$NON-NLS-1$
		int cx = getX();
		int cy = getY();
		int radius = getRadius();
		int vx1 = box.getMinX() - cx;
		int vy1 = box.getMinY() - cy;
		int vx2 = box.getMaxX() - cx;
		int vy2 = box.getMaxY() - cy;

		if (vx1>=-radius && vx1<=radius && vy1>=-radius && vy1<=radius &&
				vx2>=-radius && vx2<=radius && vy2>=-radius && vy2<=radius) {
			int[] quadrants = new int[4];
			int[] x = new int[] {vx1, vx2, vx2, vx1};
			int[] y = new int[] {vy1, vy1, vy2, vy2};
			for(int i=0; i<4; ++i) {
				int xcoord = x[i];
				int ycoord = y[i];
				int flag = 1 << i;
				if (xcoord > 0) {
					if (ycoord > 0) {
						quadrants[0] |= flag;
					}
					else {
						quadrants[1] |= flag;
					}
				}
				else {
					if (ycoord > 0) {
						quadrants[3] |= flag;
					}
					else {
						quadrants[2] |= flag;
					}
				}
			}

			for(int i=0; i<quadrants.length; ++i) {
				if (quadrants[i]!=0) {
					CirclePerimeterIterator<P, V> iterator = new CirclePerimeterIterator<>(
							getGeomFactory(), cx, cy, radius, i*2, i*2+2, false);
					int px, py, ccw, cpx, cpy;
					P p;

					while (iterator.hasNext()) {
						p = iterator.next();
						px = cy - p.iy();
						py = p.ix() - cx;

						for(int j=0; j<4; ++j) {
							if ((quadrants[i] & (1<<j))!=0) {
								cpx = x[j] - p.ix();
								cpy = y[j] - p.iy();
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
	@Pure
	@Override
	default Iterator<P> getPointIterator() {
		return new CirclePerimeterIterator<>(getGeomFactory(), getX(), getY(), getRadius(), 0, 8, true);
	}

	/** Replies the points of the circle perimeters starting by the first octant.
	 * 
	 * @param firstOctantIndex is the index of the first octant (see figure) to treat.
	 * @param nbOctants is the number of octants to traverse (greater than zero).
	 * @return the points on the perimeters.
	 */
	@Pure
	default Iterator<P> getPointIterator(int firstOctantIndex, int nbOctants) {
		return getPointIterator(getX(), getY(),  getRadius(), firstOctantIndex, nbOctants, getGeomFactory());
	}

	@Pure
	@Override
	default PathIterator2ai<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new CirclePathIterator<>(this);
		}
		return new TransformedCirclePathIterator<>(this, transform);
	}

	/** Abstract iterator on the path elements of the circle.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	abstract class AbstractCirclePathIterator<IE extends PathElement2ai> implements PathIterator2ai<IE> {
		
		/**
		 * ArcIterator.btan(Math.PI/2)
		 */
		protected static final double CTRL_VAL = 0.5522847498307933f;

		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static final double PCV = 0.5f + CTRL_VAL * 0.5f;
		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static final double NCV = 0.5f - CTRL_VAL * 0.5f;
		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static double CTRL_PTS[][] = {
			{  1.0f,  PCV,  PCV,  1.0f,  0.5f,  1.0f },
			{  NCV,  1.0f,  0.0f,  PCV,  0.0f,  0.5f },
			{  0.0f,  NCV,  NCV,  0.0f,  0.5f,  0.0f },
			{  PCV,  0.0f,  1.0f,  NCV,  1.0f,  0.5f }
		};
		
		/**
		 * The element factory.
		 */
		protected final Circle2ai<?, ?, IE, ?, ?, ?> circle;
		
		/**
		 * @param circle the circle.
		 */
		public AbstractCirclePathIterator(Circle2ai<?, ?, IE, ?, ?, ?> circle) {
			assert (circle != null) : "Circle must be not null."; //$NON-NLS-1$
			this.circle = circle;
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

		@Override
		public GeomFactory2ai<IE, ?, ?, ?> getGeomFactory() {
			return this.circle.getGeomFactory();
		}

		@Override
		public boolean isCurved() {
			return true;
		}

		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public boolean isPolygon() {
			return true;
		}

	}

	/** Iterator on the path elements of the circle.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class CirclePathIterator<IE extends PathElement2ai> extends AbstractCirclePathIterator<IE> {

		private int x;

		private int y;
		
		private int r;
		
		private int index = 0;
		
		private int movex;
		
		private int movey;

		private int lastx;

		private int lasty;

		/**
		 * @param circle the circle to iterate on.
		 */
		public CirclePathIterator(Circle2ai<?, ?, IE, ?, ?, ?> circle) {
			super(circle);
			if (circle.isEmpty()) {
				this.index = 6;
			} else {
				this.r = circle.getRadius();
				this.x = circle.getX() - this.r;
				this.y = circle.getY() - this.r;
			}
		}
		
		@Override
		public PathIterator2ai<IE> restartIterations() {
			return new CirclePathIterator<>(this.circle);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 5;
		}

		@Override
		public IE next() {
			if (this.index > 5) {
				throw new NoSuchElementException();
			}
			int idx = this.index;
			++this.index;
			if (idx==0) {
				int dr = 2 * this.r;
				double ctrls[] = CTRL_PTS[3];
				this.movex = (int)(this.x + ctrls[4] * dr);
				this.movey = (int)(this.y + ctrls[5] * dr);
				this.lastx = this.movex;
				this.lasty = this.movey;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			else if (idx<5) {
				int dr = 2 * this.r;
				double ctrls[] = CTRL_PTS[idx - 1];
				int ppx = this.lastx;
				int ppy = this.lasty;
				this.lastx = (int)(this.x + ctrls[4] * dr);
				this.lasty = (int)(this.y + ctrls[5] * dr);
				return getGeomFactory().newCurvePathElement(
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
			return getGeomFactory().newClosePathElement(
					ppx, ppy,
					this.lastx, this.lasty);
		}
		
	}

	/** Iterator on the path elements of the circle.
	 * 
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class TransformedCirclePathIterator<IE extends PathElement2ai> extends AbstractCirclePathIterator<IE> {

		private final Transform2D transform;

		private int x;
		
		private int y;
		
		private int r;
		
		private int index = 0;
		
		private int movex;
		
		private int movey;

		private Point2D<?, ?> p1;

		private Point2D<?, ?> p2;
		
		private Point2D<?, ?> ptmp1;
		
		private Point2D<?, ?> ptmp2;

		/**
		 * @param circle the circle to iterate on.
		 * @param transform the transformation to apply.
		 */
		public TransformedCirclePathIterator(Circle2ai<?, ?, IE, ?, ?, ?> circle, Transform2D transform) {
			super(circle);
			assert(transform != null) : "Transformation must not be null."; //$NON-NLS-1$
			this.transform = transform;
			if (circle.isEmpty()) {
				this.index = 6;
			} else {
				this.p1 = new InnerComputationPoint2ai();
				this.p2 = new InnerComputationPoint2ai();
				this.ptmp1 = new InnerComputationPoint2ai();
				this.ptmp2 = new InnerComputationPoint2ai();
				this.r = circle.getRadius();
				this.x = circle.getX() - this.r;
				this.y = circle.getY() - this.r;
			}
		}
		
		@Override
		public PathIterator2ai<IE> restartIterations() {
			return new TransformedCirclePathIterator<>(this.circle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 5;
		}

		@Override
		public IE next() {
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
				return getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy());
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
				return getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						this.ptmp1.ix(), this.ptmp1.iy(),
						this.ptmp2.ix(), this.ptmp2.iy(),
						this.p2.ix(), this.p2.iy());
			}
			this.p1.set(this.p2);
			this.p2.set(this.movex, this.movey);
			this.transform.transform(this.p2);
			return getGeomFactory().newClosePathElement(
					this.p1.ix(), this.p1.iy(),
					this.p2.ix(), this.p2.iy());
		}

	}

	/** Iterates on points on the perimeter of a circle.
	 * <p>
	 * The rastrerization is based on a Bresenham algorithm.
	 * 
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class CirclePerimeterIterator<P extends Point2D<? super P, ? super V>,
			V extends Vector2D<? super V, ? super P>> implements Iterator<P> {

		private final GeomFactory<V, P> factory;
		
		private final int cx;
		private final int cy;
		private final int cr;

		private final boolean skip;
		private final int maxOctant;

		private int currentOctant;
		private int x, y, d;
		
		private P next = null;
		
		private final Set<P> junctionPoint = new TreeSet<>(new Tuple2iComparator());

		/** Construct the iterator from the initialOctant (inclusive) to the lastOctant (exclusive).
		 *
		 * @param factory the point factory.
		 * @param centerX the x coordinate of the center of the circle. 
		 * @param centerY the y coordinate of the center of the circle. 
		 * @param radius the radius of the circle. 
		 * @param initialOctant the octant from which the iteration must start.
		 * @param lastOctant the first octant that must not be iterated on.
		 * @param skip indicates if the first point on an octant must be skip, because it is already replied when treating the
		 *     previous octant.
		 */
		public CirclePerimeterIterator(GeomFactory<V, P> factory,
				int centerX, int centerY, int radius, int initialOctant, int lastOctant, boolean skip) {
			assert (factory != null) : "Factory must be not null."; //$NON-NLS-1$			
			assert (radius >= 0) : "Circle radius must be positive or zero."; //$NON-NLS-1$
			assert (initialOctant >= 0 && initialOctant < 8) : "Initial octant must be in [0; 7]"; //$NON-NLS-1$
			assert (lastOctant > initialOctant && lastOctant <= 8) : "Last octant must be in [initialOctant + 1; 8]"; //$NON-NLS-1$
			this.factory = factory;
			this.cx = centerX;
			this.cy = centerY;
			this.cr = radius;
			this.skip = skip;
			this.maxOctant = lastOctant;
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
		@Pure
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}
		
		private void searchNext() {
			if (this.currentOctant>=this.maxOctant) {
				this.next = null;
			}
			else {
				this.next = this.factory.newPoint();
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
						if (!cont) {
							P point = this.factory.newPoint();
							point.set(this.next.ix(), this.next.iy());
							this.junctionPoint.add(point);
						}
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

		@Override
		public P next() {
			P pixel = this.next;
			if (pixel==null) throw new NoSuchElementException();
			searchNext();
			return pixel;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}

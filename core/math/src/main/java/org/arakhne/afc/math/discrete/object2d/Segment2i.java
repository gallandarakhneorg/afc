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
package org.arakhne.afc.math.discrete.object2d;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;



/** 2D line segment with integer coordinates.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment2i extends AbstractShape2i<Segment2i> {

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ellipse (ex0,ey0) to (ex1,ey1) extending to the right.
	 * <p>
	 * When the line (x0;y0) to (x1;y1) is crossing one of the up or
	 * bottom borders of the shadow of the circle, the crossings
	 * count is increased or decreased, depending if the line is
	 * going down or up, respectively.
	 * In the following figure, the circle is represented.
	 * The "shadow" is the projection of the circle on the right.
	 * The red lines represent the up and bottom borders.
	 * <center>
	 * <a href="doc-files/crossing_circle.png"><img src="doc-files/crossing_circle.png" width="300"/></a>
	 * </center>
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param cx is the center of the circle to extend.
	 * @param cy is the center of the circle to extend.
	 * @param radius is the radius of the circle to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromCircle(
			int crossings,
			int cx, int cy,
			int radius,
			int x0, int y0,
			int x1, int y1) {
		int numCrosses = crossings;

		int xmin = cx - Math.abs(radius);
		int xmax = cx + Math.abs(radius);
		int ymin = cy - Math.abs(radius);
		int ymax = cy + Math.abs(radius);

		// The line is entirely on the top or on the bottom of the shadow
		if (y0<ymin && y1<ymin) return numCrosses;
		if (y0>ymax && y1>ymax) return numCrosses;
		// The line is entierly on the left of the shadow.
		if (x0<xmin && x1<xmin) return numCrosses;
		
		if (x0>xmax && x1>xmax) {
			// The line is entirely at the right of the center of the shadow.
			// We may use the standard "rectangle" crossing computation
			if (y0<y1) {
				if (y0<ymin) ++numCrosses;
				if (y1>ymax) ++numCrosses;
			}
			else {
				if (y1<ymin) --numCrosses;
				if (y0>ymax) --numCrosses;
			}
		}
		else if (Circle2i.intersectsCircleSegment(
				cx, cy, radius,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			numCrosses = computeCrossingsFromPoint(numCrosses, cx, ymin, x0, y0, x1, y1, true, false);
			numCrosses = computeCrossingsFromPoint(numCrosses, cx, ymax, x0, y0, x1, y1, false, true);
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the segment (sx0,sy0) to (sx1,sy1) extending to the right.
	 * <p>
	 * When the line (x0;y0) to (x1;y1) is crossing one of the up or
	 * bottom borders of the shadow of the segment, the crossings
	 * count is increased or decreased, depending if the line is
	 * going down or up, respectively.
	 * In the following figure, the segment is represented.
	 * The "shadow" is the projection of the segment on the right.
	 * The red lines represent the up and bottom borders.
	 * <center>
	 * <a href="doc-files/crossing_segment.png"><img src="doc-files/crossing_segment.png" width="300"/></a>
	 * </center>
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param sx1 is the first point of the segment to extend.
	 * @param sy1 is the first point of the segment to extend.
	 * @param sx2 is the second point of the segment to extend.
	 * @param sy2 is the second point of the segment to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromSegment(
			int crossings,
			int sx1, int sy1,
			int sx2, int sy2,
			int x0, int y0,
			int x1, int y1) {
		/* CAUTION:
		 * --------
		 * In the comment of this function, it is assumed that y0<=y1,
		 * to simplify the explanations.
		 * The source code is handled y0<=y1 and y0>y1.
		 */
		int numCrosses = crossings;

		int xmin = Math.min(sx1, sx2);
		int xmax = Math.max(sx1, sx2);
		int ymin = Math.min(sy1, sy2);
		int ymax = Math.max(sy1, sy2);

		// The line is entirely below or up to the shadow of the segment 
		if (y0<ymin && y1<ymin) return numCrosses;
		if (y0>ymax && y1>ymax) return numCrosses;
		// The line is entirely at te left of the segment
		if (x0<xmin && x1<xmin) return numCrosses;

		if (x0>xmax && x1>xmax) {
			// The line is entirely at the right of the shadow
			if (y0<y1) {
				if (y0<ymin) ++numCrosses;
				if (y1>ymax) ++numCrosses;
			}
			else {
				if (y1<ymin) --numCrosses;
				if (y0>ymax) --numCrosses;
			}
		}
		else if (intersectsSegmentSegment(x0, y0, x1, y1, sx1, sy1, sx2, sy2, true, true, null)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			// The line is intersectly partly the bounding rectangle of the segment.
			// We must determine on which side of the segment the points of the line are.
			// If side1 is positive, the first point of the line is on the side of the shadow, relatively to the segment
			// If it is negative, the first point is on the opposite side of the shadow, relatively to the segment.
			// If it is nul, the point is on line colinear to the segment.
			// Same for side2 and the second point of the line.
			int side1, side2;
			boolean firstIsTop = (sy1<=sy2);
			if (firstIsTop) {
				side1 = MathUtil.sidePointLine(sx1, sy1, sx2, sy2, x0, y0, false);
				side2 = MathUtil.sidePointLine(sx1, sy1, sx2, sy2, x1, y1, false);
			}
			else {
				side1 = MathUtil.sidePointLine(sx2, sy2, sx1, sy1, x0, y0, false);
				side2 = MathUtil.sidePointLine(sx2, sy2, sx1, sy1, x1, y1, false);
			}
			if (side1>=0 || side2>=0) {
				// At least one point is on the side of the shadow.
				// Now we compute the intersection with the up and bottom borders.
				// Intersection is obtained by computed the crossing value from
				// the two points of the segment.
				int n1, n2;
				n1 = computeCrossingsFromPoint(0, sx1, sy1, x0, y0, x1, y1, firstIsTop, !firstIsTop);
				n2 = computeCrossingsFromPoint(0, sx2, sy2, x0, y0, x1, y1, !firstIsTop, firstIsTop);

				// The total crossing value must be updated with the border's crossing values.
				numCrosses += n1 + n2;
			}
		}

		return numCrosses;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the rectangle.
	 * <p>
	 * When the line (x0;y0) to (x1;y1) is intersecting the rectangle,
	 * the value {@link MathConstants#SHAPE_INTERSECTS} is returned.
	 * When the line (x0;y0) to (x1;y1) is crossing one of the up or
	 * bottom borders of the shadow of the rectangle, the crossings
	 * count is increased or decreased, depending if the line is
	 * going down or up, respectively.
	 * In the following figure, the rectangle is represented.
	 * The "shadow" is the projection of the rectangle on the right.
	 * The red lines represent the up and bottom borders.
	 * <center>
	 * <a href="doc-files/crossing_rect.png"><img src="doc-files/crossing_rect.png" width="300"/></a>
	 * </center>
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromRect(
			int crossings,
			int rxmin, int rymin,
			int rxmax, int rymax,
			int x0, int y0,
			int x1, int y1) {
		int numCrosses = crossings;
		// The line is horizontal, only SHAPE_INTERSECT may be replies
		if (y0==y1) {
			if (y0>=rymin && y0<=rymax &&
				(x0>=rxmin || x1>=rxmin) &&
				(x0<=rxmax || x1<=rxmax)) {
				return MathConstants.SHAPE_INTERSECTS;
			}
			return crossings;
		}
		// The line is entirely at the top or at the bottom of the rectangle
		if (y0 > rymax && y1 > rymax) return numCrosses;
		if (y0 < rymin && y1 < rymin) return numCrosses;
		// The line is entirely on the left of the rectangle
		if (x0 < rxmin && x1 < rxmin) return numCrosses;

		if (x0 > rxmax && x1 > rxmax) {
			// Line is entirely to the right of the rect
			// and the vertical ranges of the two overlap by a non-empty amount
			// Thus, this line segment is partially in the "right-shadow"
			// Path may have done a complete crossing
			// Or path may have entered or exited the right-shadow
			if (y0 < y1) {
				// y-increasing line segment...
				// We know that y0 < rymax and y1 > rymin
				if (y0 < rymin) ++numCrosses;
				if (y1 > rymax) ++numCrosses;
			}
			else if (y1 < y0) {
				// y-decreasing line segment...
				// We know that y1 < rymax and y0 > rymin
				if (y1 < rymin) --numCrosses;
				if (y0 > rymax) --numCrosses;
			}
		}
		else {
			// Remaining case:
			// Both x and y ranges overlap by a non-empty amount
			// First do trivial INTERSECTS rejection of the cases
			// where one of the endpoints is inside the rectangle.
			if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
				(x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
				return MathConstants.SHAPE_INTERSECTS;
			}

			// Otherwise calculate the y intercepts and see where
			// they fall with respect to the rectangle
			LineIterator iterator;
			int ymaxline;
			if (y0<=y1) {
				iterator = new LineIterator(x0, y0, x1, y1);
				ymaxline = y1;
			}
			else {
				iterator = new LineIterator(x1, y1, x0, y0);
				ymaxline = y0;
			}
			Point2i p = new Point2i();
			Integer xintercept1 = null;
			Integer xintercept2 = null;
			boolean cont = true;
			while (iterator.hasNext() && cont) {
				iterator.next(p);
				if (p.y()==rymin && (xintercept1==null || xintercept1>p.x())) {
					xintercept1 = p.x();
				}
				if (p.y()==rymax && (xintercept2==null || xintercept2>p.x())) {
					xintercept2 = p.x();
				}
				cont = (p.y()<=ymaxline);
			}
			
			if (xintercept1!=null && xintercept2!=null) {
				if (xintercept1<rxmin && xintercept2<rxmin) {
					// the intersection points are entirely on the left
				}
				else if (xintercept1>rxmax && xintercept2>rxmax) {
					// the intersection points are entirely on the right
					if (y0 < y1) {
						// y-increasing line segment...
						// We know that y0 < rymax and y1 > rymin
						if (y0 <= rymin) ++numCrosses;
						if (y1 >= rymax) ++numCrosses;
					}
					else if (y1 < y0) {
						// y-decreasing line segment...
						// We know that y1 < rymax and y0 > rymin
						if (y1 <= rymin) --numCrosses;
						if (y0 >= rymax) --numCrosses;
					}
				}
				else {
					return MathConstants.SHAPE_INTERSECTS;
				}
			}
			else if (xintercept1!=null) {
				// Only the top line of the rectangle is intersecting the segment
				if (xintercept1<rxmin) {
					// the intersection point is at entirely on the left
				}
				else if (xintercept1>rxmax) {
					if (y0 < y1) {
						// y-increasing line segment...
						// We know that y0 < rymax and y1 > rymin
						if (y0 <= rymin) ++numCrosses;
					}
					else if (y1 < y0) {
						// y-decreasing line segment...
						// We know that y1 < rymax and y0 > rymin
						if (y1 <= rymin) --numCrosses;
					}
				}
				else {
					return MathConstants.SHAPE_INTERSECTS;
				}
			}
			else if (xintercept2!=null) {
				// Only the bottom line of the rectangle is intersecting the segment
				if (xintercept2<rxmin) {
					// the intersection point is at entirely on the left
				}
				else if (xintercept2>rxmax) {
					if (y0 < y1) {
						// y-increasing line segment...
						// We know that y0 < rymax and y1 > rymin
						if (y0 <= rymax) ++numCrosses;
					}
					else if (y1 < y0) {
						// y-decreasing line segment...
						// We know that y1 < rymax and y0 > rymin
						if (y1 <= rymax) --numCrosses;
					}
				}
				else {
					return MathConstants.SHAPE_INTERSECTS;
				}
			}
		}
		
		return numCrosses;
	}
	
	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the up/bottom borders of the ray extending to the right from (px,py).
	 * +x is returned for a crossing where the Y coordinate is increasing.
	 * -x is returned for a crossing where the Y coordinate is decreasing.
	 * x is the number of border crossed by the lines.
	 * <p>
	 * The borders of the segment are the two side limits between the cells covered by the segment
	 * and the adjacents cells (not covered by the segment).
	 * In the following figure, the point (px;py) is represented.
	 * The "shadow line" is the projection of (px;py) on the right.
	 * The red lines represent the up and bottom borders.
	 * <center>
	 * <a href="doc-files/crossing_point.png"><img src="doc-files/crossing_point.png" width="300"/></a>
	 * </center>
	 * 
	 * @param crossing is the initial value of the crossing.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, {@link MathConstants#SHAPE_INTERSECTS}
	 */
	public static int computeCrossingsFromPoint(
			int crossing,
			int px, int py,
			int x0, int y0,
			int x1, int y1) {
		return computeCrossingsFromPoint(crossing, px, py, x0, y0, x1, y1, true, true);
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the up/bottom borders of the ray extending to the right from (px,py).
	 * +x is returned for a crossing where the Y coordinate is increasing.
	 * -x is returned for a crossing where the Y coordinate is decreasing.
	 * x is the number of border crossed by the lines.
	 * <p>
	 * The borders of the segment are the two side limits between the cells covered by the segment
	 * and the adjacents cells (not covered by the segment).
	 * In the following figure, the point (px;py) is represented.
	 * The "shadow line" is the projection of (px;py) on the right.
	 * The red lines represent the up and bottom borders.
	 * <center>
	 * <a href="doc-files/crossing_point.png"><img src="doc-files/crossing_point.png" width="300"/></a>
	 * </center>
	 * 
	 * @param crossing is the initial value of the crossing.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param enableTopBorder indicates if the top border must be enabled in the crossing computation.
	 * @param enableBottomBorder indicates if the bottom border must be enabled in the crossing computation.
	 * @return the crossing; or {@link MathConstants#SHAPE_INTERSECTS} if the segment is on the point.
	 */
	public static int computeCrossingsFromPoint(
			int crossing,
			int px, int py,
			int x0, int y0,
			int x1, int y1,
			boolean enableTopBorder,
			boolean enableBottomBorder) {
		// The line is horizontal, impossible to intersect the borders.
		if (y0==y1) return crossing;
		// The line does cross the shadow line 
		if (py<y0 && py<y1) return crossing;
		if (py>y0 && py>y1) return crossing;
		// The line is entirely on the left of the point
		if (px>x0 && px>x1) return crossing;

		// General case: try to detect crossing
		
		LineIterator iterator = new LineIterator(x0, y0, x1, y1);
		
		Point2i p = new Point2i();
		while (iterator.hasNext()) {
			iterator.next(p);
			if (p.y()==py) {
				if (p.x()==px)
					return MathConstants.SHAPE_INTERSECTS;
				if (p.x()>px) {
					// Found an intersection
					int numCrosses = crossing;
					if (y0<=y1) {
						if (y0<py && enableTopBorder) ++numCrosses;
						if (y1>py && enableBottomBorder) ++numCrosses;
					}
					else {
						if (y0>py && enableBottomBorder) --numCrosses;
						if (y1<py && enableTopBorder) --numCrosses;
					}
					return numCrosses;
				}
			}
		}
		
		return crossing;
	}
	
	/** Replies if two segments are intersecting.
	 * This function determines if the segments' lines
	 * are intersecting because using the pixel-based test.
	 * This function uses the pixels of the segments that are
	 * computed according to a Bresenham line algorithm.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsSegmentSegment(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		int side1 = MathUtil.sidePointLine(x1, y1, x2, y2, x3, y3, false);
		int side2 = MathUtil.sidePointLine(x1, y1, x2, y2, x4, y4, false);
		if ((side1*side2)<=0) {
			return intersectsSegmentSegment1(x1, y1, x2, y2, x3, y3, x4, y4, true, true, null)!=0;
		}
		return false;
	}
	
	/** Replies if two segments are intersecting pixel per pixel.
	 * This function does not determine if the segments' lines
	 * are intersecting because using the pixel-based test.
	 * This function uses the pixels of the segments that are
	 * computed according to a Bresenham line algorithm.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param enableThirdPoint indicates if the intersection on the third point is computed.
	 * @param enableFourthPoint indicates if the intersection on the fourth point is computed.
	 * @param intersectionPoint are the coordinates of the intersection, if exist.
	 * @return <code>true</code> if the two segments are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsSegmentSegment(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, boolean enableThirdPoint, boolean enableFourthPoint, Point2D intersectionPoint) {
		return intersectsSegmentSegment1(x1, y1, x2, y2, x3, y3, x4, y4, enableThirdPoint, enableFourthPoint, intersectionPoint)!=0;
	}
	
	/** Replies if two segments are intersecting pixel per pixel.
	 * This function does not determine if the segments' lines
	 * are intersecting because using the pixel-based test.
	 * This function uses the pixels of the segments that are
	 * computed according to a Bresenham line algorithm.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param enableThirdPoint indicates if the intersection on the third point is computed.
	 * @param enableFourthPoint indicates if the intersection on the fourth point is computed.
	 * @param intersectionPoint are the coordinates of the intersection, if exist.
	 * @return an integer value; if <code>0</code> the two segments are not intersecting;
	 * <code>1</code> if the two segments are intersecting and the segment 2 has pixels on both
	 * sides of the segment 1; <code>2</code> if the segments are intersecting and the segment 2
	 * is only in one side of the segment 1.
	 */
	static int intersectsSegmentSegment1(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, boolean enableThirdPoint, boolean enableFourthPoint, Point2D intersectionPoint) {
		LineIterator it1;
		if (x1<x2) {
			it1 = new LineIterator(x1, y1, x2, y2);
		}
		else {
			it1 = new LineIterator(x2, y2, x1, y1);
		}
		LineIterator it2;
		if (x3<x4) {
			it2 = new LineIterator(x3, y3, x4, y4);
		}
		else {
			it2 = new LineIterator(x4, y4, x3, y3);
		}

		if (it1.hasNext() && it2.hasNext()) {
			Point2i p1 = new Point2i();
			Point2i p2 = new Point2i();

			boolean isFirstPointOfSecondSegment = true;

			it1.next(p1);
			it2.next(p2);

			do {

				if (p1.x()<p2.x()) {
					while (it1.hasNext() && p1.x()<p2.x()) {
						it1.next(p1);
					}
				}
				else if (p2.x()<p1.x()) {
					while (it2.hasNext() && p2.x()<p1.x()) {
						it2.next(p2);
						isFirstPointOfSecondSegment = false;
					}
				}

				int x = p1.x();
				int min1 = p1.y();
				int max1 = p1.y();
				int min2 = isFirstPointOfSecondSegment && !enableThirdPoint ? Integer.MAX_VALUE : p2.y();
				int max2 = isFirstPointOfSecondSegment && !enableThirdPoint ? Integer.MIN_VALUE : p2.y();

				while (it1.hasNext()) {
					it1.next(p1);
					if (p1.x()==x) {
						if (p1.y()<min1) min1 = p1.y();
						if (p1.y()>max1) max1 = p1.y();
					}
					else {
						break;
					}
				}

				while (it2.hasNext()) {
					it2.next(p2);
					isFirstPointOfSecondSegment = false;
					if (p2.x()==x) {
						if (p2.y()<min2) min2 = p2.y();
						if (p2.y()>max2) max2 = p2.y();
					}
					else {
						break;
					}
				}

				if (max2>=min1 && max1>=min2) {
					if (intersectionPoint!=null) {
						intersectionPoint.set(x, Math.max(min1, min2));
					}
					return !isFirstPointOfSecondSegment && (it2.hasNext()) ? 1 : 2;
				}
			}
			while (it1.hasNext() && it2.hasNext());

			if (enableFourthPoint && p1.equals(p2)) {
				if (intersectionPoint!=null) {
					intersectionPoint.set(p1);
				}
				return !isFirstPointOfSecondSegment && (it2.hasNext()) ? 1 : 2;
			}
		}

		return 0;
	}

	private static final long serialVersionUID = -82425036308183925L;

	/** X-coordinate of the first point. */
	protected int ax = 0;
	/** Y-coordinate of the first point. */
	protected int ay = 0;
	/** X-coordinate of the second point. */
	protected int bx = 0;
	/** Y-coordinate of the second point. */
	protected int by = 0;

	/**
	 */
	public Segment2i() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2i(Point2D a, Point2D b) {
		set(a, b);
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Segment2i(int x1, int y1, int x2, int y2) {
		set(x1, y1, x2, y2);
	}

	@Override
	public void clear() {
		this.ax = this.ay = this.bx = this.by = 0;
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
	public void set(int x1, int y1, int x2, int y2) {
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
		this.ax = a.x();
		this.ay = a.y();
		this.bx = b.x();
		this.by = b.y();
	}
	
	@Override
	public void set(Shape2i s) {
		Rectangle2i r = s.toBoundingBox();
		this.ax = r.getMinX();
		this.ay = r.getMinY();
		this.bx = r.getMaxX();
		this.by = r.getMaxY();
	}

	/** Replies the X of the first point.
	 * 
	 * @return the x of the first point.
	 */
	public int getX1() {
		return this.ax;
	}

	/** Replies the Y of the first point.
	 * 
	 * @return the y of the first point.
	 */
	public int getY1() {
		return this.ay;
	}

	/** Replies the X of the second point.
	 * 
	 * @return the x of the second point.
	 */
	public int getX2() {
		return this.bx;
	}

	/** Replies the Y of the second point.
	 * 
	 * @return the y of the second point.
	 */
	public int getY2() {
		return this.by;
	}

	/** Replies the first point.
	 * 
	 * @return the first point.
	 */
	public Point2D getP1() {
		return new Point2i(this.ax, this.ay);
	}

	/** Replies the second point.
	 * 
	 * @return the second point.
	 */
	public Point2D getP2() {
		return new Point2i(this.bx, this.by);
	}

	@Override
	public Rectangle2i toBoundingBox() {
		Rectangle2i r = new Rectangle2i();
		r.setFromCorners(
				this.ax,
				this.ay,
				this.bx,
				this.by);
		return r;
	}

	@Override
	public void toBoundingBox(Rectangle2i box) {
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
		Point2D closestPoint = getClosestPointTo(p);
		return closestPoint.distanceSquared(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceL1(Point2D p) {
		Point2D closestPoint = getClosestPointTo(p);
		return closestPoint.distanceL1(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceLinf(Point2D p) {
		Point2D closestPoint = getClosestPointTo(p);
		return closestPoint.distanceLinf(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(int x, int y) {
		if (x>=this.ax && x<=this.bx && y>=this.ay && y<=this.by) {
			if (this.ax==this.bx || this.ay==this.by) {
				return true;
			}

			int minDist = Integer.MAX_VALUE;
			int d;
			int a,b;
			Point2i p = new Point2i();
			LineIterator iterator = new LineIterator(this.ax, this.ay, this.bx, this.by);
			while (iterator.hasNext()) {
				iterator.next(p);
				a = Math.abs(x-p.x());
				b = Math.abs(y-p.y());
				d = a*a + b*b ;
				if (d==0) return true;
				if (d>minDist) {
					return false;
				}
				minDist = d;
			}
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(Rectangle2i r) {
		return r.isEmpty() && contains(r.getMinX(), r.getMinY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2i getClosestPointTo(Point2D p) {
		return computeClosestPointTo(this.ax, this.ay, this.bx, this.by, p.x(), p.y());
	}

	/** Replies the closest point in a circle to a point.
	 * 
	 * @param ax is the x-coordinate of the first point of the segment
	 * @param ay is the y-coordinate of the first point of the segment
	 * @param bx is the x-coordinate of the second point of the segment
	 * @param by is the y-coordinate of the second point of the segment
	 * @param px is the x-coordinate of the point
	 * @param py is the x-coordinate of the point
	 * @return the closest point in the segment to the point.
	 */
	public static Point2i computeClosestPointTo(int ax, int ay, int bx, int by, int px, int py) {
		// Special case
		//    0 1 2 3 4 5 6 7 8 9 10
		// 5) | | | | | | | | | | |X|
		// 4) | | |O| | | | | |X|X| |
		// 3) | | | | | | |X|X| | | |
		// 2) | | | | |X|X| | | | | |
		// 1) | | |X|X| | | | | | | |
		// 0) |X|X| | | | | | | | | |
		//
		// The closest point to point O is (4;2) even
		// if the distance is increasing between (2;1)
		// and (4;2). The algo must take this special
		// case into account.

		int minDist = Integer.MAX_VALUE;
		int d;
		int a,b;
		boolean oneBestFound = false;
		Point2i solution = new Point2i(ax, ay);
		Point2i cp = new Point2i();
		LineIterator iterator = new LineIterator(ax, ay, bx, by);
		while (iterator.hasNext()) {
			iterator.next(cp);
			a = Math.abs(px-cp.x());
			b = Math.abs(py-cp.y());
			d = a*a + b*b ;
			if (d==0) {
				// We are sure that the closest point was found
				return cp;
			}
			if (d>minDist) {
				// here we have found a good candidate, but
				// but due to the rasterization the optimal solution
				// may be one pixel after the already found.
				// See the special case configuration at the beginning
				// of this function.
				if (oneBestFound) return solution;
				oneBestFound = true;
			}
			else {
				minDist = d;
				solution.set(cp);
				// here we have found a good candidate, but
				// but due to the rasterization the optimal solution
				// may be one pixel after the already found.
				// See the special case configuration at the beginning
				// of this function.
				if (oneBestFound) return solution;
			}
		}
		return solution;
	}

	@Override
	public void translate(int dx, int dy) {
		this.ax += dx;
		this.ay += dy;
		this.bx += dx;
		this.by += dy;
	}

	@Override
	public PathIterator2i getPathIterator(Transform2D transform) {
		return new SegmentPathIterator(
				this.ax, this.ay, this.bx, this.by,
				transform);
	}

	/** Replies an iterator on the points of the segment.
	 * <p>
	 * The Bresenham line algorithm is an algorithm which determines which points in 
	 * an n-dimensional raster should be plotted in order to form a close 
	 * approximation to a straight line between two given points. It is 
	 * commonly used to draw lines on a computer screen, as it uses only 
	 * integer addition, subtraction and bit shifting, all of which are 
	 * very cheap operations in standard computer architectures. It is one of the 
	 * earliest algorithms developed in the field of computer graphics. A minor extension 
	 * to the original algorithm also deals with drawing circles.
	 * <p>
	 * While algorithms such as Wu's algorithm are also frequently used in modern 
	 * computer graphics because they can support antialiasing, the speed and 
	 * simplicity of Bresenham's line algorithm mean that it is still important. 
	 * The algorithm is used in hardware such as plotters and in the graphics 
	 * chips of modern graphics cards. It can also be found in many software 
	 * graphics libraries. Because the algorithm is very simple, it is often 
	 * implemented in either the firmware or the hardware of modern graphics cards.
	 * 
	 * @return an iterator on the points along the Bresenham line.
	 */
	@Override
	public Iterator<Point2i> getPointIterator() {
		return new LineIterator(this.ax, this.ay, this.bx, this.by);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Segment2i) {
			Segment2i rr2d = (Segment2i) obj;
			return ((this.ax == rr2d.getX1()) &&
					(this.ay == rr2d.getY1()) &&
					(this.bx == rr2d.getX2()) &&
					(this.by == rr2d.getY2()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + this.ax;
		bits = 31L * bits + this.ay;
		bits = 31L * bits + this.bx;
		bits = 31L * bits + this.by;
		return (int) (bits ^ (bits >> 32));
	}

	/** Transform the current segment.
	 * This function changes the current segment.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape(Transform2D)
	 */
	public void transform(Transform2D transform) {
		Point2i p = new Point2i(this.ax,  this.ay);
		transform.transform(p);
		this.ax = p.x();
		this.ay = p.y();
		p.set(this.bx, this.by);
		transform.transform(p);
		this.bx = p.x();
		this.by = p.y();
	}

	@Override
	public Shape2i createTransformedShape(Transform2D transform) {
		Point2D p1 = transform.transform(this.ax, this.ay);
		Point2D p2 = transform.transform(this.bx, this.by);
		return new Segment2i(p1, p2);
	}

	@Override
	public boolean intersects(Rectangle2i s) {
		return Rectangle2i.intersectsRectangleSegment(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Circle2i s) {
		return Circle2i.intersectsCircleSegment(
				s.getX(), s.getY(),
				s.getRadius(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Segment2i s) {
		return intersectsSegmentSegment(
				getX1(), getY1(), getX2(), getY2(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
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
	private static class SegmentPathIterator implements PathIterator2i {

		private final Point2D p1 = new Point2i();
		private final Point2D p2 = new Point2i();
		private final Transform2D transform;
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
		 * @param transform
		 */
		public SegmentPathIterator(int x1, int y1, int x2, int y2, Transform2D transform) {
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
		public PathElement2i next() {
			if (this.index>1) throw new NoSuchElementException();
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
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2i.LinePathElement2i(
						this.p1.x(), this.p1.y(),
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

	/** The Bresenham line algorithm is an algorithm which determines which points in 
	 * an n-dimensional raster should be plotted in order to form a close 
	 * approximation to a straight line between two given points. It is 
	 * commonly used to draw lines on a computer screen, as it uses only 
	 * integer addition, subtraction and bit shifting, all of which are 
	 * very cheap operations in standard computer architectures. It is one of the 
	 * earliest algorithms developed in the field of computer graphics. A minor extension 
	 * to the original algorithm also deals with drawing circles.
	 * <p>
	 * While algorithms such as Wu's algorithm are also frequently used in modern 
	 * computer graphics because they can support antialiasing, the speed and 
	 * simplicity of Bresenham's line algorithm mean that it is still important. 
	 * The algorithm is used in hardware such as plotters and in the graphics 
	 * chips of modern graphics cards. It can also be found in many software 
	 * graphics libraries. Because the algorithm is very simple, it is often 
	 * implemented in either the firmware or the hardware of modern graphics cards.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	static class LineIterator implements Iterator<Point2i> {

		private final boolean steep;
		private final int ystep;
		private final int xstep;
		private final int deltax;
		private final int deltay;
		private final int x1;
		private int y, x;
		private int error;

		/**
		 * @param x0 is the x-coordinate of the first point of the Bresenham line.
		 * @param y0 is the y-coordinate of the first point of the Bresenham line.
		 * @param x1 is the x-coordinate of the last point of the Bresenham line.
		 * @param y1 is the y-coordinate of the last point of the Bresenham line.
		 */
		public LineIterator(int x0, int y0, int x1, int y1) {
			int _x0 = x0;
			int _y0 = y0;
			int _x1 = x1;
			int _y1 = y1;

			this.steep = Math.abs(_y1 - _y0) > Math.abs(_x1 - _x0);

			int swapv;
			if (this.steep) {
				//swap(x0, y0);
				swapv = _x0;
				_x0 = _y0;
				_y0 = swapv;
				//swap(x1, y1);
				swapv = _x1;
				_x1 = _y1;
				_y1 = swapv;
			}
			/*if (_x0 > _x1) {
				//swap(x0, x1);
				swapv = _x0;
				_x0 = _x1;
				_x1 = swapv;
				//swap(y0, y1);
				swapv = _y0;
				_y0 = _y1;
				_y1 = swapv;
			}*/

			this.deltax = Math.abs(_x1 - _x0);
			this.deltay = Math.abs(_y1 - _y0);
			this.error = this.deltax / 2;
			this.y = _y0;

			if (_x0 < _x1) this.xstep = 1;
			else this.xstep = -1;

			if (_y0 < _y1) this.ystep = 1;
			else this.ystep = -1;

			this.x1 = _x1;
			this.x = _x0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return ((this.xstep>0) && (this.x <= this.x1))
					||((this.xstep<0) && (this.x1 <= this.x));
		}

		/** Replies the next point in the given parameter.
		 * 
		 * @param p
		 */
		public void next(Point2i p) {
			if (this.steep) {
				p.set(this.y, this.x);
			}
			else {
				p.set(this.x, this.y);
			}

			this.error = this.error - this.deltay;

			if (this.error < 0) {
				this.y = this.y + this.ystep;
				this.error = this.error + this.deltax;
			}

			this.x += this.xstep;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2i next() {
			Point2i p = new Point2i();
			next(p);
			return p;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class LineIterator

}
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
import org.arakhne.afc.math.matrix.Transform2D;



/** 2D line segment with floating-point coordinates.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment2f extends AbstractShape2f<Segment2f> {

	private static final long serialVersionUID = -82425036308183925L;

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 * 
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	public static int computeCrossingsFromPoint(
			float px, float py,
			float x0, float y0,
			float x1, float y1) {
		// Copied from AWT API
		if (py <  y0 && py <  y1) return 0;
		if (py >= y0 && py >= y1) return 0;
		// assert(y0 != y1);
		if (px >= x0 && px >= x1) return 0;
		if (px <  x0 && px <  x1) return (y0 < y1) ? 1 : -1;
		float xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px >= xintercept) return 0;
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 * <p>
	 * This function differs from {@link #computeCrossingsFromPoint(float, float, float, float, float, float)}.
	 * The equality test is not used in this function.
	 * 
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	private static int computeCrossingsFromPoint1(
			float px, float py,
			float x0, float y0,
			float x1, float y1) {
		// Copied from AWT API
		if (py <  y0 && py <  y1) return 0;
		if (py > y0 && py > y1) return 0;
		// assert(y0 != y1);
		if (px > x0 && px > x1) return 0;
		if (px <  x0 && px <  x1) return (y0 < y1) ? 1 : -1;
		float xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px > xintercept) return 0;
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the segment (sx0,sy0) to (sx1,sy1) extending to the right.
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
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	public static int computeCrossingsFromSegment(
			int crossings,
			float sx1, float sy1,
			float sx2, float sy2,
			float x0, float y0,
			float x1, float y1) {
		int numCrosses = crossings;

		float xmin = Math.min(sx1, sx2);
		float xmax = Math.max(sx1, sx2);
		float ymin = Math.min(sy1, sy2);
		float ymax = Math.max(sy1, sy2);

		if (y0<=ymin && y1<=ymin) return numCrosses;
		if (y0>=ymax && y1>=ymax) return numCrosses;
		if (x0<=xmin && x1<=xmin) return numCrosses;
		if (x0>=xmax && x1>=xmax) {
			// The line is entirely at the right of the shadow
			if (y0<y1) {
				if (y0<=ymin) ++numCrosses;
				if (y1>=ymax) ++numCrosses;
			}
			else {
				if (y1<=ymin) --numCrosses;
				if (y0>=ymax) --numCrosses;
			}
		}
		else if (intersectsSegmentSegmentWithoutEnds(x0, y0, x1, y1, sx1, sy1, sx2, sy2)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			int side1, side2;
			if (sy1<=sy2) {
				side1 = MathUtil.sidePointLine(sx1, sy1, sx2, sy2, x0, y0, false);
				side2 = MathUtil.sidePointLine(sx1, sy1, sx2, sy2, x1, y1, false);
			}
			else {
				side1 = MathUtil.sidePointLine(sx2, sy2, sx1, sy1, x0, y0, false);
				side2 = MathUtil.sidePointLine(sx2, sy2, sx1, sy1, x1, y1, false);
			}
			if (side1>0 || side2>0) {
				int n1 = computeCrossingsFromPoint(sx1, sy1, x0, y0, x1, y1);
				int n2;
				if (n1!=0) {
					n2 = computeCrossingsFromPoint1(sx2, sy2, x0, y0, x1, y1);
				}
				else {
					n2 = computeCrossingsFromPoint(sx2, sy2, x0, y0, x1, y1);
				}
				numCrosses += n1;
				numCrosses += n2;
			}
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ellipse (ex0,ey0) to (ex1,ey1) extending to the right.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param ex is the first corner of the ellipse to extend.
	 * @param ey is the first corner of the ellipse to extend.
	 * @param ew is the width of the ellipse to extend.
	 * @param eh is the height of the ellipse  to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromEllipse(
			int crossings,
			float ex, float ey,
			float ew, float eh,
			float x0, float y0,
			float x1, float y1) {
		int numCrosses = crossings;

		float xmin = ex;
		float ymin = ey;
		float xmax = ex + ew;
		float ymax = ey + eh;

		if (y0<=ymin && y1<=ymin) return numCrosses;
		if (y0>=ymax && y1>=ymax) return numCrosses;
		if (x0<=xmin && x1<=xmin) return numCrosses;

		if (x0>=xmax && x1>=xmax) {
			// The line is entirely at the right of the shadow
			if (y0<y1) {
				if (y0<=ymin) ++numCrosses;
				if (y1>=ymax) ++numCrosses;
			}
			else {
				if (y1<=ymin) --numCrosses;
				if (y0>=ymax) --numCrosses;
			}
		}
		else if (Ellipse2f.intersectsEllipseSegment(
				xmin, ymin, xmax-xmin, ymax-ymin,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			float xcenter = (xmin+xmax)/2f;
			numCrosses += computeCrossingsFromPoint(xcenter, ymin, x0, y0, x1, y1);
			numCrosses += computeCrossingsFromPoint(xcenter, ymax, x0, y0, x1, y1);
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ellipse (ex0,ey0) to (ex1,ey1) extending to the right.
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
			float cx, float cy,
			float radius,
			float x0, float y0,
			float x1, float y1) {
		int numCrosses = crossings;

		float xmin = cx - Math.abs(radius);
		float ymin = cy - Math.abs(radius);
		float ymax = cy + Math.abs(radius);

		if (y0<=ymin && y1<=ymin) return numCrosses;
		if (y0>=ymax && y1>=ymax) return numCrosses;
		if (x0<=xmin && x1<=xmin) return numCrosses;

		if (x0>=cx+radius && x1>=cx+radius) {
			// The line is entirely at the right of the shadow
			if (y0<y1) {
				if (y0<=ymin) ++numCrosses;
				if (y1>=ymax) ++numCrosses;
			}
			else {
				if (y1<=ymin) --numCrosses;
				if (y0>=ymax) --numCrosses;
			}
		}
		else if (Circle2f.intersectsCircleSegment(
				cx, cy, radius,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			numCrosses += computeCrossingsFromPoint(cx, ymin, x0, y0, x1, y1);
			numCrosses += computeCrossingsFromPoint(cx, ymax, x0, y0, x1, y1);
		}

		return numCrosses;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the rectangle.  See the comment
	 * for the {@link MathUtil#SHAPE_INTERSECTS} constant for more complete details.
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
			float rxmin, float rymin,
			float rxmax, float rymax,
			float x0, float y0,
			float x1, float y1) {
		int numCrosses = crossings;

		if (y0 >= rymax && y1 >= rymax) return numCrosses;
		if (y0 <= rymin && y1 <= rymin) return numCrosses;
		if (x0 <= rxmin && x1 <= rxmin) return numCrosses;
		if (x0 >= rxmax && x1 >= rxmax) {
			// Line is entirely to the right of the rect
			// and the vertical ranges of the two overlap by a non-empty amount
			// Thus, this line segment is partially in the "right-shadow"
			// Path may have done a complete crossing
			// Or path may have entered or exited the right-shadow
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
			return numCrosses;
		}
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
		float xi0 = x0;
		if (y0 < rymin) {
			xi0 += ((rymin - y0) * (x1 - x0) / (y1 - y0));
		}
		else if (y0 > rymax) {
			xi0 += ((rymax - y0) * (x1 - x0) / (y1 - y0));
		}
		float xi1 = x1;
		if (y1 < rymin) {
			xi1 += ((rymin - y1) * (x0 - x1) / (y0 - y1));
		}
		else if (y1 > rymax) {
			xi1 += ((rymax - y1) * (x0 - x1) / (y0 - y1));
		}
		if (xi0 <= rxmin && xi1 <= rxmin) return numCrosses;
		if (xi0 >= rxmax && xi1 >= rxmax) {
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
			return numCrosses;
		}
		return MathConstants.SHAPE_INTERSECTS;
	}

	/** Replies if two lines are intersecting.
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsLineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		if (MathUtil.isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return MathUtil.isCollinearPoints(x1, y1, x2, y2, x3, y3);
		}
		return true;
	}

	/** Replies if a segment and a line are intersecting.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsSegmentLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		return (MathUtil.sidePointLine(x3, y3, x4, y4, x1, y1, true) *
				MathUtil.sidePointLine(x3, y3, x4, y4, x2, y2, true)) <= 0;
	}

	private static boolean intersectsSSWE(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float vx1, vy1, vx2a, vy2a, vx2b, vy2b, f1, f2, sign;

		vx1 = x2 - x1;
		vy1 = y2 - y1;

		// Based on CCW. It is different than MathUtil.ccw(); because
		// this small algorithm is computing a ccw of 0 for colinear points.
		vx2a = x3 - x1;
		vy2a = y3 - y1;
		f1 = vx2a * vy1 - vy2a * vx1;

		vx2b = x4 - x1;
		vy2b = y4 - y1;
		f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  ON SEGMENT?
		// -1   1  -1  T
		//  0  -1   0  ON SEGMENT?
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  ON SEGMENT?
		//  1  -1  -1  T
		//  1   0   0  ON SEGMENT?
		//  1   1   1  F
		sign = f1 * f2;

		if (sign<0f) return true;
		if (sign>0f) return false;

		float squaredLength = vx1*vx1 + vy1*vy1;

		if (f1==0f && f2==0f) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return (f1>=0f || f2>=0) && (f1<=1f || f2<=1f);
		}

		if (f1==0f) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return (f1>=0f && f1<=1f);
		}

		if (f2==0f) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return (f2>=0 && f2<=1f);
		}
		
		return false;
	}

	private static boolean intersectsSSWoE(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float vx1, vy1, vx2a, vy2a, vx2b, vy2b, f1, f2, sign;

		vx1 = x2 - x1;
		vy1 = y2 - y1;

		vx2a = x3 - x1;
		vy2a = y3 - y1;
		f1 = vx2a * vy1 - vy2a * vx1;

		vx2b = x4 - x1;
		vy2b = y4 - y1;
		f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  F
		// -1   1  -1  T
		//  0  -1   0  F
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  F
		//  1  -1  -1  T
		//  1   0   0  F
		//  1   1   1  F

		sign = f1 * f2;

		if (sign<0f) return true;
		if (sign>0f) return false;

		if (f1==0f && f2==0f) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			float squaredLength = vx1*vx1 + vy1*vy1;

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return (f1>0f || f2>0) && (f1<1f || f2<1f);
		}

		return false;
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are not intersecting.
	 * To include the ends of the segments in the intersection ranges, see
	 * {@link #intersectsSegmentSegmentWithEnds(float, float, float, float, float, float, float, float)}.
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
	 * @see #intersectsSegmentSegmentWithEnds(float, float, float, float, float, float, float, float)
	 */
	public static boolean intersectsSegmentSegmentWithoutEnds(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		boolean r;
		r = intersectsSSWoE(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r) return r;
		return intersectsSSWoE(x3, y3, x4, y4, x1, y1, x2, y2);
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(float, float, float, float, float, float, float, float)}.
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
	 * @see #intersectsSegmentSegmentWithoutEnds(float, float, float, float, float, float, float, float)
	 */
	public static boolean intersectsSegmentSegmentWithEnds(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		boolean r;
		r = intersectsSSWE(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r) return r;
		return intersectsSSWE(x3, y3, x4, y4, x1, y1, x2, y2);
	}

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
		return MathUtil.distanceSquaredPointToSegment(p.getX(), p.getY(),
				this.ax, this.ay,
				this.bx, this.by);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceL1(Point2D p) {
		float ratio = MathUtil.projectsPointOnLine(p.getX(), p.getY(), this.ax, this.ay, this.bx, this.by);
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
		float ratio = MathUtil.projectsPointOnLine(p.getX(), p.getY(), this.ax, this.ay, this.bx, this.by);
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
		return MathUtil.isEpsilonZero(MathUtil.distanceSquaredPointToSegment(x, y,
				this.ax, this.ay,
				this.bx, this.by));
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
		float ratio = MathUtil.projectsPointOnLine(p.getX(), p.getY(),
				this.ax, this.ay,
				this.bx, this.by);
		if (ratio<=0f) return new Point2f(this.ax, this.ay);
		if (ratio>=1f) return new Point2f(this.bx, this.by);
		return new Point2f(
				this.ax + (this.bx - this.ax) * ratio,
				this.ay + (this.by - this.ay) * ratio); 
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
		return Rectangle2f.intersectsRectangleSegment(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return Ellipse2f.intersectsEllipseSegment(
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return Circle2f.intersectsCircleSegment(
				s.getX(), s.getY(),
				s.getRadius(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return intersectsSegmentSegmentWithoutEnds(
				getX1(), getY1(),
				getX2(), getY2(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
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
/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.discrete;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractSegment2F;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2d;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;


/** A generic path with integer coordinates.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("hiding")
public class Path2i extends AbstractShape2i<Path2i> implements Path2D<Shape2i,Rectangle2i,PathElement2i,PathIterator2i> {

	private static final long serialVersionUID = -4229773257722403127L;

	/** Multiple of cubic & quad curve size.
	 */
	static final int GROW_SIZE = 24;

	/**
	 * Calculates the number of times the given path
	 * crosses the given segment extending to the right.
	 * 
	 * @param pi is the description of the path.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @return the crossing or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromSegment(PathIterator2i pi, int x1, int y1, int x2, int y2) {
		return computeCrossingsFromSegment(0, pi, x1, y1, x2, y2, true);
	}
	
	/**
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param pi is the description of the path.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @return the crossing
	 */
	static int computeCrossingsFromSegment(int crossings, PathIterator2i pi, int x1, int y1, int x2, int y2, boolean closeable) {	
		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement2i element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.toX;
		int movy = element.toY;
		int curx = movx;
		int cury = movy;
		int endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.toX;
				movy = cury = element.toY;
				break;
			case LINE_TO:
				endx = element.toX;
				endy = element.toY;
				numCrosses = Segment2i.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, x2, y2,
						curx, cury,
						endx, endy);
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.toX;
				endy = element.toY;
				Path2i localPath = new Path2i();
				localPath.moveTo(element.fromX, element.fromY);
				localPath.quadTo(
						element.ctrlX1, element.ctrlY1,
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false);
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.toX;
				endy = element.toY;
				Path2i localPath = new Path2i();
				localPath.moveTo(element.fromX, element.fromY);
				localPath.curveTo(
						element.ctrlX1, element.ctrlY1,
						element.ctrlX2, element.ctrlY2,
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false);
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = Segment2i.computeCrossingsFromSegment(
							numCrosses,
							x1, y1, x2, y2,
							curx, cury,
							movx, movy);
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		if (numCrosses!=MathConstants.SHAPE_INTERSECTS && closeable && cury != movy) {
			numCrosses = Segment2i.computeCrossingsFromSegment(
					numCrosses,
					x1, y1, x2, y2,
					curx, cury,
					movx, movy);
		}
		
		return numCrosses;
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given ellipse extending to the right.
	 * 
	 * @param pi is the description of the path.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @return the crossing or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromCircle(PathIterator2i pi, int cx, int cy, int radius) {
		return computeCrossingsFromCircle(0, pi, cx, cy, radius, true);
	}
	
	/**
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param pi is the description of the path.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @return the crossing
	 */
	static int computeCrossingsFromCircle(int crossings, PathIterator2i pi, int cx, int cy, int radius, boolean closeable) {	
		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement2i element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.toX;
		int movy = element.toY;
		int curx = movx;
		int cury = movy;
		int endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.toX;
				movy = cury = element.toY;
				break;
			case LINE_TO:
				endx = element.toX;
				endy = element.toY;
				numCrosses = Segment2i.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, radius,
						curx, cury,
						endx, endy);
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.toX;
				endy = element.toY;
				Path2i localPath = new Path2i();
				localPath.moveTo(element.fromX, element.fromY);
				localPath.quadTo(
						element.ctrlX1, element.ctrlY1,
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						false);
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.toX;
				endy = element.toY;
				Path2i localPath = new Path2i();
				localPath.moveTo(element.fromX, element.fromY);
				localPath.curveTo(
						element.ctrlX1, element.ctrlY1,
						element.ctrlX2, element.ctrlY2,
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						false);
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = Segment2i.computeCrossingsFromCircle(
							numCrosses,
							cx, cy, radius,
							curx, cury,
							movx, movy);
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		if (numCrosses!=MathConstants.SHAPE_INTERSECTS && closeable && cury != movy) {
			numCrosses = Segment2i.computeCrossingsFromCircle(
					numCrosses,
					cx, cy, radius,
					curx, cury,
					movx, movy);
		}
		
		return numCrosses;
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on a part of the path,
	 * then no crossings are counted for that intersection.
	 * +1 is added for each crossing where the Y coordinate is increasing
	 * -1 is added for each crossing where the Y coordinate is decreasing
	 * The return value is the sum of all crossings for every segment in
	 * the path.
	 * The path must start with a MOVE_TO, otherwise an exception is
	 * thrown.
	 * 
	 * @param pi is the description of the path.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @return the crossing
	 */
	public static int computeCrossingsFromPoint(PathIterator2i pi, int px, int py) {
		return computeCrossingsFromPoint(pi, px, py, true);
	}
	
	/**
	 * Calculates the number of times the given path
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on a part of the path,
	 * then no crossings are counted for that intersection.
	 * +1 is added for each crossing where the Y coordinate is increasing
	 * -1 is added for each crossing where the Y coordinate is decreasing
	 * The return value is the sum of all crossings for every segment in
	 * the path.
	 * The path must start with a MOVE_TO, otherwise an exception is
	 * thrown.
	 * 
	 * @param pi is the description of the path.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param autoClose indicates if the shape is automatically assumed as closed.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	static int computeCrossingsFromPoint(PathIterator2i pi, int px, int py, boolean autoClose) {
		// Copied and adapted from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement2i element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.toX;
		int movy = element.toY;
		int curx = movx;
		int cury = movy;
		int endx, endy;
		int crossings = 0;
		
		while (pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.toX;
				movy = cury = element.toY;
				break;
			case LINE_TO:
				endx = element.toX;
				endy = element.toY;
				crossings = Segment2i.computeCrossingsFromPoint(
						crossings,
						px, py,
						curx, cury,
						endx, endy);
				if (crossings==MathConstants.SHAPE_INTERSECTS) {
					return crossings;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.toX;
				endy = element.toY;
				Path2i curve = new Path2i();
				curve.moveTo(element.fromX, element.fromY);
				curve.quadTo(element.ctrlX1, element.ctrlY1, endx, endy);
				int numCrosses = computeCrossingsFromPoint(
						curve.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				crossings += numCrosses;
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = element.toX;
				endy = element.toY;
				curve = new Path2i();
				curve.moveTo(element.fromX, element.fromY);
				curve.curveTo(
						element.ctrlX1, element.ctrlY1,
						element.ctrlX2, element.ctrlY2,
						endx, endy);
				numCrosses = computeCrossingsFromPoint(
						curve.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				crossings += numCrosses;
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					crossings = Segment2i.computeCrossingsFromPoint(
							crossings,
							px, py,
							curx, cury,
							movx, movy);
					if (crossings==MathConstants.SHAPE_INTERSECTS) {
						return crossings;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		if (autoClose && cury != movy && curx != movx) {
			crossings = Segment2i.computeCrossingsFromPoint(
					crossings,
					px, py,
					curx, cury,
					movx, movy);
		}
		
		return crossings;
	}
	
	/**
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator2i}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2i} interface to implement support for the
	 * {@link Shape2i#contains(int, int)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @return {@code true} if the specified coordinates are inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise
	 */
	public static boolean contains(PathIterator2i pi, int x, int y) {
		// Copied from the AWT API
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int cross = computeCrossingsFromPoint(pi, x, y);
		return ((cross & mask) != 0);
	}
	
	/**
	 * Accumulate the number of times the path crosses the shadow
	 * extending to the right of the rectangle.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details.
	 * The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path,
	 * or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the rectangle.
	 * The path must start with a SEG_MOVETO, otherwise an exception is
	 * thrown.
	 * The caller must check r[xy]{min,max} for NaN values.
	 * 
	 * @param pi is the iterator on the path elements.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @return the crossings.
	 */
	public static int computeCrossingsFromRect(PathIterator2i pi,
			int rxmin, int rymin,
			int rxmax, int rymax) {
		return __computeCrossingsFromRect(pi, rxmin, rymin, rxmax, rymax, true, true);
	}
	
	private static int crossingHelper1(
			int crossings,
			int rxmin, int rymin,
			int rxmax, int rymax,
			int curx, int cury,
			int movx, int movy,
			boolean intersectingBehavior) {
		int crosses = Segment2i.computeCrossingsFromRect(crossings,
					rxmin, rymin,
					rxmax, rymax,
					curx, cury,
					movx, movy);
		if (!intersectingBehavior && crosses==MathConstants.SHAPE_INTERSECTS) {
			int x1 = rxmin+1;
			int x2 = rxmax-1;
			int y1 = rymin+1;
			int y2 = rymax-1;
			crosses = Segment2i.computeCrossingsFromRect(crossings,
					x1, y1,
					x2, y2,
					curx, cury,
					movx, movy);
		}
		return crosses;
	}
	
	/**
	 * Accumulate the number of times the path crosses the shadow
	 * extending to the right of the rectangle.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details.
	 * The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path,
	 * or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the rectangle.
	 * The path must start with a SEG_MOVETO, otherwise an exception is
	 * thrown.
	 * The caller must check r[xy]{min,max} for NaN values.
	 * 
	 * @param pi is the iterator on the path elements.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param autoClose indicates if the line from the last point to the last move
	 * point must be include in the crossing computation.
	 * @param intersectingBehavior indicates the function is called to determine if the rectangle
	 * is inside the shape or not. This function determines
	 * {@link MathConstants#SHAPE_INTERSECTS} in a different way if the
	 * function is used for containing or intersecting tests.
	 * @return the crossings count or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	static int __computeCrossingsFromRect(PathIterator2i pi,
			int rxmin, int rymin,
			int rxmax, int rymax,
			boolean autoClose,
			boolean intersectingBehavior) {
		// Copied from AWT API
		if (rxmax <= rxmin || rymax <= rymin) return 0;
		if (!pi.hasNext()) return 0;

		PathElement2i pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement.toX;
		cury = movy = pathElement.toY;
		int crossings = 0;

		while (crossings != MathConstants.SHAPE_INTERSECTS
				&& pi.hasNext()) {
			pathElement = pi.next();
			switch (pathElement.type) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				movx = curx = pathElement.toX;
				movy = cury = pathElement.toY;
				break;
			case LINE_TO:
				endx = pathElement.toX;
				endy = pathElement.toY;
				crossings = crossingHelper1(crossings,
						rxmin, rymin, rxmax, rymax,
						curx, cury, endx, endy,
						intersectingBehavior);
				if (crossings==MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement.toX;
				endy = pathElement.toY;
				Path2i curve = new Path2i();
				curve.moveTo(pathElement.fromX, pathElement.fromY);
				curve.quadTo(pathElement.ctrlX1, pathElement.ctrlY1, endx, endy);
				int numCrosses = __computeCrossingsFromRect(
						curve.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rxmax, rymax,
						false, intersectingBehavior);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				crossings += numCrosses;
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement.toX;
				endy = pathElement.toY;
				curve = new Path2i();
				curve.moveTo(pathElement.fromX, pathElement.fromY);
				curve.curveTo(pathElement.ctrlX1, pathElement.ctrlY1, pathElement.ctrlX2, pathElement.ctrlY2, endx, endy);
				numCrosses = __computeCrossingsFromRect(
						curve.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rxmax, rymax,
						false, intersectingBehavior);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				crossings += numCrosses;
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					crossings = crossingHelper1(crossings,
							rxmin, rymin, rxmax, rymax,
							curx, cury, movx, movy,
							intersectingBehavior);
					if (crossings==MathConstants.SHAPE_INTERSECTS) {
						return crossings;
					}
				}
				curx = movx;
				cury = movy;
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				break;
			default:
			}
		}

		if (autoClose && crossings != MathConstants.SHAPE_INTERSECTS && (curx != movx || cury != movy)) {
			crossings = crossingHelper1(crossings,
					rxmin, rymin, rxmax, rymax,
					curx, cury, movx, movy,
					intersectingBehavior);
		}

		// Count should always be a multiple of 2 here.
		// assert((crossings & 1) != 0);
		return crossings;
	}
	
	/**
	 * Tests if the specified rectangle is inside the closed
	 * boundary of the specified {@link PathIterator2i}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2i} interface to implement support for the
	 * {@link Shape2i#contains(Rectangle2i)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	public static boolean contains(PathIterator2i pi, int rx, int ry, int rwidth, int rheight) {
		// Copied and adapted from AWT API
        if (rwidth <= 0 || rheight <= 0) {
            return false;
        }
        int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
        int crossings = __computeCrossingsFromRect(pi, rx, ry, rx+rwidth, ry+rheight, true, false);
        return (crossings != MathConstants.SHAPE_INTERSECTS &&
                (crossings & mask) != 0);
	}
	
	/**
	 * Tests if the interior of the specified {@link PathIterator2i}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2i} interface to implement support for the
	 * {@code intersects()} method.
	 * <p>
	 * This method object may conservatively return true in
	 * cases where the specified rectangular area intersects a
	 * segment of the path, but that segment does not represent a
	 * boundary between the interior and exterior of the path.
	 * Such a case may occur if some set of segments of the
	 * path are retraced in the reverse direction such that the
	 * two sets of segments cancel each other out without any
	 * interior area between them.
	 * To determine whether segments represent true boundaries of
	 * the interior of the path would require extensive calculations
	 * involving all of the segments of the path and the winding
	 * rule and are thus beyond the scope of this implementation.
	 *
	 * @param pi the specified {@code PathIterator}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param w the width of the specified rectangular coordinates
	 * @param h the height of the specified rectangular coordinates
	 * @return {@code true} if the specified {@code PathIterator} and
	 *         the interior of the specified set of rectangular
	 *         coordinates intersect each other; {@code false} otherwise.
	 */
	public static boolean intersects(PathIterator2i pi, int x, int y, int w, int h) {
		if (w <= 0f || h <= 0f) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = __computeCrossingsFromRect(pi, x, y, x+w, y+h, true, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	/** Array of types.
	 */
	PathElementType[] types;

	/** Array of coords.
	 */
	int[] coords;

	/** Number of types in the array.
	 */
	int numTypes = 0;

	/** Number of coords in the array.
	 */
	int numCoords = 0;

	/** Winding rule for the path.
	 */
	PathWindingRule windingRule;

	/** Indicates if the path is empty.
	 * The path is empty when there is no point inside, or
	 * all the points are at the same coordinate, or
	 * when the path does not represents a drawable path
	 * (a path with a line or a curve).
	 */
	private Boolean isEmpty = Boolean.TRUE;
	
	/** Indicates if the path contains base primitives
	 * (no curve).
	 */
	private Boolean isPolyline = Boolean.TRUE;

	/** Buffer for the bounds of the path that corresponds
	 * to the points really on the path (eg, the pixels
	 * drawn). The control points of the curves are
	 * not considered in this bounds.
	 */
	private SoftReference<Rectangle2i> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<Rectangle2i> logicalBounds = null;

	/**
	 */
	public Path2i() {
		this(PathWindingRule.NON_ZERO);
	}

	/**
	 * @param iterator
	 */
	public Path2i(Iterator<PathElement2i> iterator) {
		this(PathWindingRule.NON_ZERO, iterator);
	}

	/**
	 * @param windingRule
	 */
	public Path2i(PathWindingRule windingRule) {
		assert(windingRule!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new int[GROW_SIZE];
		this.windingRule = windingRule;
	}

	/**
	 * @param windingRule
	 * @param iterator
	 */
	public Path2i(PathWindingRule windingRule, Iterator<PathElement2i> iterator) {
		assert(windingRule!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new int[GROW_SIZE];
		this.windingRule = windingRule;
		add(iterator);
	}

	/**
	 * @param p
	 */
	public Path2i(Path2i p) {
		this.coords = p.coords.clone();
		this.isEmpty = p.isEmpty;
		this.isPolyline = p.isPolyline;
		this.numCoords = p.numCoords;
		this.types = p.types.clone();
		this.windingRule = p.windingRule;
		Rectangle2i box;
		box = p.graphicalBounds==null ? null : p.graphicalBounds.get();
		if (box!=null) {
			this.graphicalBounds = new SoftReference<>(box.clone());
		}
		box = p.logicalBounds==null ? null : p.logicalBounds.get();
		if (box!=null) {
			this.logicalBounds = new SoftReference<>(box.clone());
		}
	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new int[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoords = 0;
		this.numTypes = 0;
		this.isEmpty = Boolean.TRUE;
		this.isPolyline = Boolean.TRUE;
		this.logicalBounds = null;
		this.graphicalBounds = null;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		if (this.numCoords>0) {
			b.append(this.coords[0]);
			for(int i=1; i<this.numCoords; ++i) {
				b.append(", "); //$NON-NLS-1$
				b.append(this.coords[i]);
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Override
	public Path2i clone() {
		Path2i clone = super.clone();
		clone.coords = this.coords.clone();
		clone.types = this.types.clone();
		return clone;
	}

	@Override
	public PathWindingRule getWindingRule() {
		return this.windingRule;
	}

	/** Set the winding rule for the path.
	 * 
	 * @param r is the winding rule for the path.
	 */
	public void setWindingRule(PathWindingRule r) {
		assert(r!=null);
		this.windingRule = r;
	}

	/** Add the elements replied by the iterator into this path.
	 * 
	 * @param iterator
	 */
	public void add(Iterator<PathElement2i> iterator) {
		PathElement2i element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.type) {
			case MOVE_TO:
				moveTo(element.toX, element.toY);
				break;
			case LINE_TO:
				lineTo(element.toX, element.toY);
				break;
			case QUAD_TO:
				quadTo(element.ctrlX1, element.ctrlY1, element.toX, element.toY);
				break;
			case CURVE_TO:
				curveTo(element.ctrlX1, element.ctrlY1, element.ctrlX2, element.ctrlY2, element.toX, element.toY);
				break;
			case CLOSE:
				closePath();
				break;
			default:
			}
		}
	}

	private void ensureSlots(boolean needMove, int n) {
		if (needMove && this.numTypes==0) {
			throw new IllegalStateException("missing initial moveto in path definition"); //$NON-NLS-1$
		}
		if (this.types.length==this.numTypes) {
			this.types = Arrays.copyOf(this.types, this.types.length+GROW_SIZE);
		}
		while ((this.numCoords+n)>=this.coords.length) {
			this.coords = Arrays.copyOf(this.coords, this.coords.length+GROW_SIZE);
		}
	}

	/**
	 * Adds a point to the path by moving to the specified
	 * coordinates specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	public void moveTo(int x, int y) {
		if (this.numTypes>0 && this.types[this.numTypes-1]==PathElementType.MOVE_TO) {
			this.coords[this.numCoords-2] = x;
			this.coords[this.numCoords-1] = y;
		}
		else {
			ensureSlots(false, 2);
			this.types[this.numTypes++] = PathElementType.MOVE_TO;
			this.coords[this.numCoords++] = x;
			this.coords[this.numCoords++] = y;
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	public void lineTo(int x, int y) {
		ensureSlots(true, 2);
		this.types[this.numTypes++] = PathElementType.LINE_TO;
		this.coords[this.numCoords++] = x;
		this.coords[this.numCoords++] = y;
		this.isEmpty = null;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a curved segment, defined by two new points, to the path by
	 * drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x2,y2)},
	 * using the specified point {@code (x1,y1)} as a quadratic
	 * parametric control point.
	 * All coordinates are specified in double precision.
	 *
	 * @param x1 the X coordinate of the quadratic control point
	 * @param y1 the Y coordinate of the quadratic control point
	 * @param x2 the X coordinate of the final end point
	 * @param y2 the Y coordinate of the final end point
	 */
	public void quadTo(int x1, int y1, int x2, int y2) {
		ensureSlots(true, 4);
		this.types[this.numTypes++] = PathElementType.QUAD_TO;
		this.coords[this.numCoords++] = x1;
		this.coords[this.numCoords++] = y1;
		this.coords[this.numCoords++] = x2;
		this.coords[this.numCoords++] = y2;
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x3,y3)},
	 * using the specified points {@code (x1,y1)} and {@code (x2,y2)} as
	 * B&eacute;zier control points.
	 * All coordinates are specified in double precision.
	 *
	 * @param x1 the X coordinate of the first B&eacute;zier control point
	 * @param y1 the Y coordinate of the first B&eacute;zier control point
	 * @param x2 the X coordinate of the second B&eacute;zier control point
	 * @param y2 the Y coordinate of the second B&eacute;zier control point
	 * @param x3 the X coordinate of the final end point
	 * @param y3 the Y coordinate of the final end point
	 */
	public void curveTo(int x1, int y1,
			int x2, int y2,
			int x3, int y3) {
		ensureSlots(true, 6);
		this.types[this.numTypes++] = PathElementType.CURVE_TO;
		this.coords[this.numCoords++] = x1;
		this.coords[this.numCoords++] = y1;
		this.coords[this.numCoords++] = x2;
		this.coords[this.numCoords++] = y2;
		this.coords[this.numCoords++] = x3;
		this.coords[this.numCoords++] = y3;
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Closes the current subpath by drawing a straight line back to
	 * the coordinates of the last {@code moveTo}.  If the path is already
	 * closed or if the previous coordinates are for a {@code moveTo}
	 * then this method has no effect.
	 */
	public void closePath() {
		if (this.numTypes<=0 ||
				(this.types[this.numTypes-1]!=PathElementType.CLOSE
				&&this.types[this.numTypes-1]!=PathElementType.MOVE_TO)) {
			ensureSlots(true, 0);
			this.types[this.numTypes++] = PathElementType.CLOSE;
		}
	}


	@Override
	public PathIterator2i getPathIteratorDiscrete(double flatness) {
		return new FlatteningPathIterator(getWindingRule(), getPathIteratorDiscrete(null), flatness, 10);
	}

	/** Replies an iterator on the path elements.
	 * <p>
	 * Only {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, and 
	 * {@link PathElementType#CLOSE} types are returned by the iterator.
	 * <p>
	 * The amount of subdivision of the curved segments is controlled by the 
	 * flatness parameter, which specifies the maximum distance that any point 
	 * on the unflattened transformed curve can deviate from the returned
	 * flattened path segments. Note that a limit on the accuracy of the
	 * flattened path might be silently imposed, causing very small flattening
	 * parameters to be treated as larger values. This limit, if there is one,
	 * is defined by the particular implementation that is used.
	 * <p>
	 * The iterator for this class is not multi-threaded safe.
	 *
	 * @param transform is an optional affine Transform2D to be applied to the
	 * coordinates as they are returned in the iteration, or <code>null</code> if 
	 * untransformed coordinates are desired.
	 * @param flatness is the maximum distance that the line segments used to approximate
	 * the curved segments are allowed to deviate from any point on the original curve.
	 * @return an iterator on the path elements.
	 */
	public PathIterator2i getPathIteratorDiscrete(Transform2D transform, double flatness) {
		return new FlatteningPathIterator(getWindingRule(), getPathIteratorDiscrete(transform), flatness, 10);
	}

	/** {@inheritDoc}
	 */
	@Override
	public PathIterator2i getPathIteratorDiscrete(Transform2D transform) {
		if (transform == null) {
			return new CopyPathIterator();
		}
		return new TransformPathIterator(transform);
	}

	
	/** {@inheritDoc}
	 */
	@Override
	public PathIterator2i getPathIteratorDiscrete() {
		return getPathIteratorDiscrete(null);
	}
	
	/** Transform the current path.
	 * This function changes the current path.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape(Transform2D)
	 */
	public void transform(Transform2D transform) {
		if (transform!=null) {
			Point2D p = new Point2i();
			for(int i=0; i<this.numCoords;) {
				p.set(this.coords[i], this.coords[i+1]);
				transform.transform(p);
				this.coords[i++] = p.ix();
				this.coords[i++] = p.iy();
			}
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void translate(int dx, int dy) {
		for(int i=0; i<this.numCoords;) {
			this.coords[i++] += dx;
			this.coords[i++] += dy;
		}
		Rectangle2i bb;
		bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
		bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Shape2i createTransformedShape(Transform2D transform) {
		Path2i newPath = new Path2i(getWindingRule());
		PathIterator2i pi = getPathIteratorDiscrete();
		Point2i p = new Point2i();
		Point2i t1 = new Point2i();
		Point2i t2 = new Point2i();
		PathElement2i e;
		while (pi.hasNext()) {
			e = pi.next();
			switch(e.type) {
			case MOVE_TO:
				p.set(e.toX, e.toY);
				transform.transform(p);
				newPath.moveTo(p.ix(), p.iy());
				break;
			case LINE_TO:
				p.set(e.toX, e.toY);
				transform.transform(p);
				newPath.lineTo(p.ix(), p.iy());
				break;
			case QUAD_TO:
				t1.set(e.ctrlX1, e.ctrlY1);
				transform.transform(t1);
				p.set(e.toX, e.toY);
				transform.transform(p);
				newPath.quadTo(t1.ix(), t1.iy(), p.ix(), p.iy());
				break;
			case CURVE_TO:
				t1.set(e.ctrlX1, e.ctrlY1);
				transform.transform(t1);
				t2.set(e.ctrlX2, e.ctrlY2);
				transform.transform(t2);
				p.set(e.toX, e.toY);
				transform.transform(p);
				newPath.curveTo(t1.ix(), t1.iy(), t2.ix(), t2.iy(), p.ix(), p.iy());
				break;
			case CLOSE:
				newPath.closePath();
				break;
			default:
			}
		}
		return newPath;
	}

	@Override
	public double distanceSquared(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	@Override
	public double distanceL1(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	@Override
	public double distanceLinf(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
	}

	@Override
	public boolean contains(int x, int y) {
		return contains(getPathIteratorDiscrete(), x, y);
	}

	@Override
	public boolean contains(Rectangle2i r) {
		return contains(getPathIteratorDiscrete(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	@Override
	public boolean intersects(Rectangle2i s) {
		// Copied from AWT API
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = __computeCrossingsFromRect(
				getPathIteratorDiscrete(),
				s.getMinX(), s.getMinY(), s.getMaxX(), s.getMaxY(),
				true, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(Circle2i s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromCircle(
				getPathIteratorDiscrete(),
				s.getX(), s.getY(), s.getRadius());
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(Segment2i s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromSegment(
				getPathIteratorDiscrete(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	private static boolean buildGraphicalBoundingBox(PathIterator2i iterator, Rectangle2i box) {
		boolean foundOneLine = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		PathElement2i element;
		Path2i subPath;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.type) {
			case LINE_TO:
				if (element.fromX<xmin) xmin = element.fromX;
				if (element.fromY<ymin) ymin = element.fromY;
				if (element.fromX>xmax) xmax = element.fromX;
				if (element.fromY>ymax) ymax = element.fromY;
				if (element.toX<xmin) xmin = element.toX;
				if (element.toY<ymin) ymin = element.toY;
				if (element.toX>xmax) xmax = element.toX;
				if (element.toY>ymax) ymax = element.toY;
				foundOneLine = true;
				break;
			case CURVE_TO:
				subPath = new Path2i();
				subPath.moveTo(element.fromX, element.fromY);
				subPath.curveTo(
						element.ctrlX1, element.ctrlY1,
						element.ctrlX2, element.ctrlY2,
						element.toX, element.toY);
				if (buildGraphicalBoundingBox(
						subPath.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMinX()>ymax) ymax = box.getMinX();
					foundOneLine = true;
				}
				break;
			case QUAD_TO:
				subPath = new Path2i();
				subPath.moveTo(element.fromX, element.fromY);
				subPath.quadTo(
						element.ctrlX1, element.ctrlY1,
						element.toX, element.toY);
				if (buildGraphicalBoundingBox(
						subPath.getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMinX()>ymax) ymax = box.getMinX();
					foundOneLine = true;
				}
				break;
			case MOVE_TO:
			case CLOSE:
			default:
			}
		}
		if (foundOneLine) {
			box.setFromCorners(xmin, ymin, xmax, ymax);
		}
		else {
			box.clear();
		}
		return foundOneLine;
	}

	private boolean buildLogicalBoundingBox(Rectangle2i box) {
		if (this.numCoords>0) {
			int xmin = Integer.MAX_VALUE;
			int ymin = Integer.MAX_VALUE;
			int xmax = Integer.MIN_VALUE;
			int ymax = Integer.MIN_VALUE;
			for(int i=0; i<this.numCoords; i+= 2) {
				if (this.coords[i]<xmin) xmin = this.coords[i];
				if (this.coords[i+1]<ymin) ymin = this.coords[i+1];
				if (this.coords[i]>xmax) xmax = this.coords[i];
				if (this.coords[i+1]>ymax) ymax = this.coords[i+1];
			}
			box.setFromCorners(xmin,  ymin, xmax, ymax);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The replied bounding box does not consider the control points
	 * of the path. Only the "visible" points are considered.
	 * 
	 * @see #toBoundingBoxWithCtrlPoints()
	 */
	@Override
	public Rectangle2i toBoundingBox() {
		Rectangle2i bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2i();
			buildGraphicalBoundingBox(
					getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}
	
	/** Replies the bounding box of all the points added in this path.
	 * <p>
	 * The replied bounding box includes the (invisible) control points.
	 * 
	 * @return the bounding box with the control points.
	 * @see #toBoundingBox()
	 */
	public Rectangle2i toBoundingBoxWithCtrlPoints() {
		Rectangle2i bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2i();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The replied bounding box does not consider the control points
	 * of the path. Only the "visible" points are considered.
	 * 
	 * @see #toBoundingBoxWithCtrlPoints(Rectangle2i)
	 */
	@Override
	public void toBoundingBox(Rectangle2i box) {
		Rectangle2i bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2i();
			buildGraphicalBoundingBox(
					getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	/** Compute the bounding box of all the points added in this path.
	 * <p>
	 * The replied bounding box includes the (invisible) control points.
	 * 
	 * @param box is the rectangle to set with the bounds.
	 * @see #toBoundingBox()
	 */
	public void toBoundingBoxWithCtrlPoints(Rectangle2i box) {
		Rectangle2i bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2i();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	public Point2D getClosestPointTo(Point2D p) {
		Point2D solution = new Point2i();
		double bestDist = Double.POSITIVE_INFINITY;
		Point2D candidate;
		PathIterator2i pi = getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO);
		PathElement2i pe;

		Segment2i seg = new Segment2i();
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int crossings = 0;
		boolean isClosed = false;
		int moveX, moveY, currentX, currentY;
		moveX = moveY = currentX = currentY = 0;
		
		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			currentX = pe.toX;
			currentY = pe.toY;

			switch(pe.type) {
			case MOVE_TO:
				moveX = pe.toX;
				moveY = pe.toY;
				isClosed = false;
				break;
			case LINE_TO:
			{
				seg.set(pe.fromX, pe.fromY, pe.toX, pe.toY);
				isClosed = false;
				candidate = seg.getClosestPointTo(p);
				if (crossings!=MathConstants.SHAPE_INTERSECTS) {
					crossings = Segment2i.computeCrossingsFromPoint(
							crossings,
							p.ix(), p.iy(),
							pe.fromX, pe.fromY, pe.toX, pe.toY);
				}
				break;
			}
			case CLOSE:
				isClosed = true;
				if (!pe.isEmpty()) {
					seg.set(pe.fromX, pe.fromY, pe.toX, pe.toY);
					candidate = seg.getClosestPointTo(p);
					if (crossings!=MathConstants.SHAPE_INTERSECTS) {
						crossings = Segment2i.computeCrossingsFromPoint(
								crossings,
								p.ix(), p.iy(),
								pe.fromX, pe.fromY, pe.toX, pe.toY);
					}
				}
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException();
			}

			if (candidate!=null) {
				double d = p.getDistanceSquared(candidate);
				if (d<=0f) return candidate;
				if (d<bestDist) {
					bestDist = d;
					solution.set(candidate);
				}
			}
		}
		
		if (!isClosed && crossings!=MathConstants.SHAPE_INTERSECTS) {
			crossings = Segment2i.computeCrossingsFromPoint(
					crossings,
					p.ix(), p.iy(),
					currentX, currentY,
					moveX, moveY);
		}
		
		if (crossings==MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0) return p;
		return solution;
	}

	@Override
	public Point2D getFarthestPointTo(Point2D p) {
		Point2D solution = new Point2i();
		double bestDist = Double.NEGATIVE_INFINITY;
		Point2D candidate;
		PathIterator2i pi = getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO);
		PathElement2i pe;

		Segment2i seg = new Segment2i();
		
		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			switch(pe.type) {
			case MOVE_TO:
				break;
			case LINE_TO:
				seg.set(pe.fromX, pe.fromY, pe.toX, pe.toY);
				candidate = seg.getFarthestPointTo(p);
				break;
			case CLOSE:
				if (!pe.isEmpty()) {
					seg.set(pe.fromX, pe.fromY, pe.toX, pe.toY);
					candidate = seg.getClosestPointTo(p);
				}
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException();
			}

			if (candidate!=null) {
				double d = p.getDistanceSquared(candidate);
				if (d>bestDist) {
					bestDist = d;
					solution.set(candidate);
				}
			}
		}
		
		return solution;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Path2i) {
			Path2i p = (Path2i)obj;
			return (this.numCoords==p.numCoords
					&&this.numTypes==p.numTypes
					&&Arrays.equals(this.coords, p.coords)
					&&Arrays.equals(this.types, p.types)
					&&this.windingRule==p.windingRule);
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + this.numCoords;
		bits = 31L * bits + this.numTypes;
		bits = 31L * bits + Arrays.hashCode(this.coords);
		bits = 31L * bits + Arrays.hashCode(this.types);
		bits = 31L * bits + this.windingRule.ordinal();
		return (int) (bits ^ (bits >> 32));
	}

	/** Replies the coordinates of this path in an array of
	 * integers.
	 * 
	 * @return the coordinates.
	 */
	public final int[] toIntArray() {
		return toIntArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * integers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	public int[] toIntArray(Transform2D transform) {
		if (transform==null) {
			return Arrays.copyOf(this.coords, this.numCoords);
		}
		Point2i p = new Point2i();
		int[] clone = new int[this.numCoords];
		for(int i=0; i<clone.length;) {
			p.x = this.coords[i];
			p.y = this.coords[i+1];
			transform.transform(p);
			clone[i++] = p.x;
			clone[i++] = p.y;
		}
		return clone;
	}

	/** Replies the points of this path in an array.
	 * 
	 * @return the points.
	 */
	public final Point2D[] toPointArray() {
		return toPointArray(null);
	}

	/** Replies the points of this path in an array.
	 * 
	 * @param transform is the transformation to apply to all the points.
	 * @return the points.
	 */
	public Point2D[] toPointArray(Transform2D transform) {
		Point2D[] clone = new Point2D[this.numCoords/2];
		if (transform==null) {
			for(int i=0, j=0; j<this.numCoords; ++i) {
				clone[i] = new Point2i(
						this.coords[j++],
						this.coords[j++]);
			}
		}
		else {
			for(int i=0, j=0; j<clone.length; ++i) {
				clone[i] = new Point2i(
						this.coords[j++],
						this.coords[j++]);
				transform.transform(clone[i]);
			}
		}
		return clone;
	}

	/** Replies the collection that is contains all the points of the path.
	 * 
	 * @return the point collection.
	 */
	public final Collection<Point2D> toCollection() {
		return new PointCollection();
	}

	/** Replies the coordinate at the given index.
	 * The index is in [0;{@link #size()}*2).
	 *
	 * @param index
	 * @return the coordinate at the given index.
	 */
	public double getCoordAt(int index) {
		return this.coords[index];
	}

	/** Replies the point at the given index.
	 * The index is in [0;{@link #size()}).
	 *
	 * @param index
	 * @return the point at the given index.
	 */
	public Point2i getPointAt(int index) {
		return new Point2i(
				this.coords[index*2],
				this.coords[index*2+1]);
	}

	/** Replies the number of points in the path.
	 *
	 * @return the number of points in the path.
	 */
	public int size() {
		return this.numCoords/2;
	}

	/** Replies if this path is empty.
	 * The path is empty when there is no point inside, or
	 * all the points are at the same coordinate, or
	 * when the path does not represents a drawable path
	 * (a path with a line or a curve).
	 * 
	 * @return <code>true</code> if the path does not contain
	 * a coordinate; otherwise <code>false</code>.
	 */
	@Override
	public boolean isEmpty() {
		if (this.isEmpty==null) {
			this.isEmpty = Boolean.TRUE;
			PathIterator2i pi = getPathIteratorDiscrete();
			PathElement2i pe;
			while (this.isEmpty()==Boolean.TRUE.booleanValue() && pi.hasNext()) {
				pe = pi.next();
				if (pe.isDrawable()) { 
					this.isEmpty = Boolean.FALSE;
				}
			}
		}
		return this.isEmpty.booleanValue();
	}

	/** Replies if the given points exists in the coordinates of this path.
	 * 
	 * @param p
	 * @return <code>true</code> if the point is a control point of the path.
	 */
	boolean containsPoint(Point2D p) {
		double x, y;
		for(int i=0; i<this.numCoords;) {
			x = this.coords[i++];
			y = this.coords[i++];
			if (x==p.getX() && y==p.getY()) {
				return true;
			}
		}
		return false;
	}


	/** Remove the point with the given coordinates.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(int x, int y) {
		for(int i=0, j=0; i<this.numCoords && j<this.numTypes;) {
			switch(this.types[j]) {
			case MOVE_TO:
			case LINE_TO:
				if (x==this.coords[i] && y==this.coords[i+1]) {
					this.numCoords -= 2;
					--this.numTypes;
					System.arraycopy(this.coords, i+2, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					return true;
				}
				i += 2;
				++j;
				break;
			case CURVE_TO:
				if ((x==this.coords[i] && y==this.coords[i+1])
						||(x==this.coords[i+2] && y==this.coords[i+3])
						||(x==this.coords[i+4] && y==this.coords[i+5])) {
					this.numCoords -= 6;
					--this.numTypes;
					System.arraycopy(this.coords, i+6, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
					return true;
				}
				i += 6;
				++j;
				break;
			case QUAD_TO:
				if ((x==this.coords[i] && y==this.coords[i+1])
						||(x==this.coords[i+2] && y==this.coords[i+3])) {
					this.numCoords -= 4;
					--this.numTypes;
					System.arraycopy(this.coords, i+4, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
					return true;
				}
				i += 4;
				++j;
				break;
			case CLOSE:
				++j;
				break;
			default:
				break;
			}
		}
		return false;
	}

	/** Remove the last action.
	 */
	public void removeLast() {
		if (this.numTypes>0) {
			switch(this.types[this.numTypes-1]) {
			case CLOSE:
				// no coord to remove
				break;
			case MOVE_TO:
			case LINE_TO:
				this.numCoords -= 2;
				break;
			case CURVE_TO:
				this.numCoords -= 6;
				this.isPolyline = null;
				break;
			case QUAD_TO:
				this.numCoords -= 4;
				this.isPolyline = null;
				break;
			default:
				throw new IllegalStateException();
			}
			--this.numTypes;
			this.isEmpty = null;
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	/** Change the coordinates of the last inserted point.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLastPoint(int x, int y) {
		if (this.numCoords>=2) {
			this.coords[this.numCoords-2] = x;
			this.coords[this.numCoords-1] = y;
			this.logicalBounds = null;
			this.graphicalBounds = null;
		}
	}
	
	@Override
	public void set(Shape2i s) {
		clear();
		add(s.getPathIteratorDiscrete());
	}

	/** Replies the points along the path.
	 * <p>
	 * This function is equivalent to a
	 * call to {@link #getPathIteratorDiscrete(double)}
	 * with the default flatness.
	 * 
	 * @return the points
	 */
	@Override
	public Iterator<Point2i> getPointIterator() {
		PathIterator2i pathIterator = getPathIteratorDiscrete(MathConstants.SPLINE_APPROXIMATION_RATIO);
		return new PixelIterator(pathIterator);
	}

	@Override
	public boolean isPolyline() {
		if (this.isPolyline==null) {
			this.isPolyline = Boolean.TRUE;
			PathIterator2i pi = getPathIteratorDiscrete();
			PathElement2i pe;
			PathElementType t;
			while (this.isPolyline==Boolean.TRUE && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				if (t==PathElementType.CURVE_TO || t==PathElementType.QUAD_TO) { 
					this.isPolyline = Boolean.FALSE;
				}
			}
		}
		return this.isPolyline.booleanValue();
	}

	/** A path iterator that does not transform the coordinates.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class CopyPathIterator implements PathIterator2i {

		private final Point2D p1 = new Point2i();
		private final Point2D p2 = new Point2i();
		private int iType = 0;
		private int iCoord = 0;
		private int movex, movey;

		/**
		 */
		public CopyPathIterator() {
			//
		}

		@Override
		public boolean hasNext() {
			return this.iType<Path2i.this.numTypes;
		}

		@Override
		public PathElement2i next() {
			int type = this.iType;
			if (this.iType>=Path2i.this.numTypes) {
				throw new NoSuchElementException();
			}
			PathElement2i element = null;
			switch(Path2i.this.types[type]) {
			case MOVE_TO:
				if (this.iCoord+2>Path2i.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.movex = Path2i.this.coords[this.iCoord++];
				this.movey = Path2i.this.coords[this.iCoord++];
				this.p2.set(this.movex, this.movey);
				element = new PathElement2i.MovePathElement2i(
						this.p2.ix(), this.p2.iy());
				break;
			case LINE_TO:
				if (this.iCoord+2>Path2i.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				element = new PathElement2i.LinePathElement2i(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case QUAD_TO:
			{
				if (this.iCoord+4>Path2i.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				int ctrlx = Path2i.this.coords[this.iCoord++];
				int ctrly = Path2i.this.coords[this.iCoord++];
				this.p2.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				element = new PathElement2i.QuadPathElement2i(
						this.p1.ix(), this.p1.iy(),
						ctrlx, ctrly,
						this.p2.ix(), this.p2.iy());
			}
			break;
			case CURVE_TO:
			{
				if (this.iCoord+6>Path2i.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				int ctrlx1 = Path2i.this.coords[this.iCoord++];
				int ctrly1 = Path2i.this.coords[this.iCoord++];
				int ctrlx2 = Path2i.this.coords[this.iCoord++];
				int ctrly2 = Path2i.this.coords[this.iCoord++];
				this.p2.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				element = new PathElement2i.CurvePathElement2i(
						this.p1.ix(), this.p1.iy(),
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.p2.ix(), this.p2.iy());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				element = new PathElement2i.ClosePathElement2i(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();

			++this.iType;

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return Path2i.this.getWindingRule();
		}
		
		@Override
		public boolean isPolyline() {
			return Path2i.this.isPolyline();
		}

	} // class CopyPathIterator

	/** A path iterator that transforms the coordinates.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TransformPathIterator implements PathIterator2i {

		private final Transform2D transform;
		private final Point2D p1 = new Point2i();
		private final Point2D p2 = new Point2i();
		private final Point2D ptmp1 = new Point2i();
		private final Point2D ptmp2 = new Point2i();
		private int iType = 0;
		private int iCoord = 0;
		private int movex, movey;

		/**
		 * @param transform
		 */
		public TransformPathIterator(Transform2D transform) {
			assert(transform!=null);
			this.transform = transform;
		}

		@Override
		public boolean hasNext() {
			return this.iType<Path2i.this.numTypes;
		}

		@Override
		public PathElement2i next() {
			if (this.iType>=Path2i.this.numTypes) {
				throw new NoSuchElementException();
			}
			PathElement2i element = null;
			switch(Path2i.this.types[this.iType++]) {
			case MOVE_TO:
				this.movex = Path2i.this.coords[this.iCoord++];
				this.movey = Path2i.this.coords[this.iCoord++];
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = new PathElement2i.MovePathElement2i(
						this.p2.ix(), this.p2.iy());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new PathElement2i.LinePathElement2i(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp1);
				this.p2.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new PathElement2i.QuadPathElement2i(
						this.p1.ix(), this.p1.iy(),
						this.ptmp1.ix(), this.ptmp1.iy(),
						this.p2.ix(), this.p2.iy());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp2);
				this.p2.set(
						Path2i.this.coords[this.iCoord++],
						Path2i.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new PathElement2i.CurvePathElement2i(
						this.p1.ix(), this.p1.iy(),
						this.ptmp1.ix(), this.ptmp1.iy(),
						this.ptmp2.ix(), this.ptmp2.iy(),
						this.p2.ix(), this.p2.iy());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = new PathElement2i.ClosePathElement2i(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return Path2i.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path2i.this.isPolyline();
		}

	}  // class TransformPathIterator

	/** An collection of the points of the path.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PointCollection implements Collection<Point2D> {

		/**
		 */
		public PointCollection() {
			//
		}

		@Override
		public int size() {
			return Path2i.this.size();
		}

		@Override
		public boolean isEmpty() {
			return Path2i.this.size()<=0;
		}

		@Override
		public boolean contains(Object o) {
			if (o instanceof Point2D) {
				return Path2i.this.containsPoint((Point2D)o);
			}
			return false;
		}

		@Override
		public Iterator<Point2D> iterator() {
			return new PointIterator();
		}

		@Override
		public Object[] toArray() {
			return Path2i.this.toPointArray();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Iterator<Point2D> iterator = new PointIterator();
			for(int i=0; i<a.length && iterator.hasNext(); ++i) {
				a[i] = (T)iterator.next();
			}
			return a;
		}

		@Override
		public boolean add(Point2D e) {
			if (e!=null) {
				if (Path2i.this.size()==0) {
					Path2i.this.moveTo(e.ix(), e.iy());
				}
				else {
					Path2i.this.lineTo(e.ix(), e.iy());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point2D) {
				Point2D p = (Point2D)o;
				return Path2i.this.remove(p.ix(), p.iy());
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for(Object obj : c) {
				if ((!(obj instanceof Point2D))
						||(!Path2i.this.containsPoint((Point2D)obj))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends Point2D> c) {
			boolean changed = false;
			for(Point2D pts : c) {
				if (add(pts)) {
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			boolean changed = false;
			for(Object obj : c) {
				if (obj instanceof Point2D) {
					Point2D pts = (Point2D)obj;
					if (Path2i.this.remove(pts.ix(), pts.iy())) {
						changed = true;
					}
				}
			}
			return changed;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			Path2i.this.clear();
		}

	} // class PointCollection

	/** Iterator on the points of the path.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PointIterator implements Iterator<Point2D> {

		private int index = 0;
		private Point2D lastReplied = null;

		/**
		 */
		public PointIterator() {
			//
		}

		@Override
		public boolean hasNext() {
			return this.index<Path2i.this.size();
		}

		@Override
		public Point2D next() {
			try {
				this.lastReplied = Path2i.this.getPointAt(this.index++);
				return this.lastReplied;
			}
			catch(Throwable e) {
				e.printStackTrace();throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			Point2D p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			Path2i.this.remove(p.ix(), p.iy());
		}

	}

	/** Iterator on the pixels of the path.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class PixelIterator implements Iterator<Point2i> {

		private final PathIterator2i pathIterator;
		private Iterator<Point2i> lineIterator = null;
		private Point2i next = null;

		public PixelIterator(PathIterator2i pi) {
			this.pathIterator = pi;
			searchNext();
		}

		private void searchNext() {
			Point2i old = this.next;
			this.next = null;
			while (this.pathIterator.hasNext() && (this.lineIterator==null || !this.lineIterator.hasNext())) {
				this.lineIterator = null;
				PathElement2i elt = this.pathIterator.next();
				if (elt.isDrawable()) {
					switch(elt.type) {
					case LINE_TO:
						this.lineIterator = new Segment2i(
								elt.fromX, elt.fromY,
								elt.toX, elt.toY).getPointIterator();
						break;
					case MOVE_TO:
					case CLOSE:
					case CURVE_TO:
					case QUAD_TO:
					default:
						throw new IllegalStateException();
					}
				}
			}
			if (this.lineIterator!=null && this.lineIterator.hasNext()) {
				this.next = this.lineIterator.next();
				while (this.next.equals(old)) {
					this.next = this.lineIterator.next();
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public Point2i next() {
			Point2i n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class PixelIterator

	/** A path iterator that is flattening the path.
	 * This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class FlatteningPathIterator implements PathIterator2i {

		/** Winding rule of the path.
		 */
		private final PathWindingRule windingRule;

		/** The source iterator.
		 */
		private final Iterator<PathElement2i> pathIterator;

		/**
		 * Square of the flatness parameter for testing against squared lengths.
		 */
		private final double squaredFlatness;

		/**
		 * Maximum number of recursion levels.
		 */
		private final int limit; 

		/** The recursion level at which each curve being held in storage was generated.
		 */
		private int levels[];

		/** The cache of interpolated coords.
		 * Note that this must be long enough
		 * to store a full cubic segment and
		 * a relative cubic segment to avoid
		 * aliasing when copying the coords
		 * of a curve to the end of the array.
		 * This is also serendipitously equal
		 * to the size of a full quad segment
		 * and 2 relative quad segments.
		 */
		private double hold[] = new double[14];

		/** The index of the last curve segment being held for interpolation.
		 */
		private int holdEnd;

		/**
		 * The index of the curve segment that was last interpolated.  This
		 * is the curve segment ready to be returned in the next call to
		 * next().
		 */
		private int holdIndex;

		/** The ending x of the last segment.
		 */
		private double currentX;

		/** The ending y of the last segment.
		 */
		private double currentY;

		/** The x of the last move segment.
		 */
		private double moveX;

		/** The y of the last move segment.
		 */
		private double moveY;

		/** The index of the entry in the
		 * levels array of the curve segment
		 * at the holdIndex
		 */
		private int levelIndex;

		/** True when iteration is done.
		 */
		private boolean done;

		/** The type of the path element.
		 */
		private PathElementType holdType;

		/** The x of the last move segment replied by next.
		 */
		private int lastNextX;

		/** The y of the last move segment replied by next.
		 */
		private int lastNextY;

		/**
		 * @param windingRule is the winding rule of the path.
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 * control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions
		 * allowed for any curved segment
		 */
		public FlatteningPathIterator(PathWindingRule windingRule, Iterator<PathElement2i> pathIterator, double flatness, int limit) {
			assert(windingRule!=null);
			assert(flatness>=0f);
			assert(limit>=0);
			this.windingRule = windingRule;
			this.pathIterator = pathIterator;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit;
			this.levels = new int[limit + 1];
			searchNext(true);
		}

		/**
		 * Ensures that the hold array can hold up to (want) more values.
		 * It is currently holding (hold.length - holdIndex) values.
		 */
		private void ensureHoldCapacity(int want) {
			if (this.holdIndex - want < 0) {
				int have = this.hold.length - this.holdIndex;
				int newsize = this.hold.length + GROW_SIZE;
				double newhold[] = new double[newsize];
				System.arraycopy(this.hold, this.holdIndex,
						newhold, this.holdIndex + GROW_SIZE,
						have);
				this.hold = newhold;
				this.holdIndex += GROW_SIZE;
				this.holdEnd += GROW_SIZE;
			}
		}

		/**
		 * Returns the square of the flatness, or maximum distance of a
		 * control point from the line connecting the end points, of the
		 * quadratic curve specified by the control points stored in the
		 * indicated array at the indicated index.
		 * @param coords an array containing coordinate values
		 * @param offset the index into <code>coords</code> from which to
		 *          to start getting the values from the array
		 * @return the flatness of the quadratic curve that is defined by the
		 *          values in the specified array at the specified index.
		 */
		private static double getQuadSquaredFlatness(double coords[], int offset) {
			return AbstractSegment2F.distanceSquaredLinePoint(
					coords[offset + 0], coords[offset + 1],
					coords[offset + 4], coords[offset + 5],
					coords[offset + 2], coords[offset + 3]);
		}

		/**
		 * Subdivides the quadratic curve specified by the coordinates
		 * stored in the <code>src</code> array at indices
		 * <code>srcoff</code> through <code>srcoff</code>&nbsp;+&nbsp;5
		 * and stores the resulting two subdivided curves into the two
		 * result arrays at the corresponding indices.
		 * Either or both of the <code>left</code> and <code>right</code>
		 * arrays can be <code>null</code> or a reference to the same array
		 * and offset as the <code>src</code> array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve.  Thus,
		 * it is possible to pass the same array for <code>left</code> and
		 * <code>right</code> and to use offsets such that
		 * <code>rightoff</code> equals <code>leftoff</code> + 4 in order
		 * to avoid allocating extra storage for this common point.
		 * @param src the array holding the coordinates for the source curve
		 * @param srcoff the offset into the array of the beginning of the
		 * the 6 source coordinates
		 * @param left the array for storing the coordinates for the first
		 * half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 * the 6 left coordinates
		 * @param right the array for storing the coordinates for the second
		 * half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 * the 6 right coordinates
		 */
		private static void subdivideQuad(double src[], int srcoff,
				double left[], int leftoff,
				double right[], int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double ctrlx = src[srcoff + 2];
			double ctrly = src[srcoff + 3];
			double x2 = src[srcoff + 4];
			double y2 = src[srcoff + 5];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
			}
			if (right != null) {
				right[rightoff + 4] = x2;
				right[rightoff + 5] = y2;
			}
			x1 = (x1 + ctrlx) / 2f;
			y1 = (y1 + ctrly) / 2f;
			x2 = (x2 + ctrlx) / 2f;
			y2 = (y2 + ctrly) / 2f;
			ctrlx = (x1 + x2) / 2f;
			ctrly = (y1 + y2) / 2f;
			if (left != null) {
				left[leftoff + 2] = x1;
				left[leftoff + 3] = y1;
				left[leftoff + 4] = ctrlx;
				left[leftoff + 5] = ctrly;
			}
			if (right != null) {
				right[rightoff + 0] = ctrlx;
				right[rightoff + 1] = ctrly;
				right[rightoff + 2] = x2;
				right[rightoff + 3] = y2;
			}
		}

		/**
		 * Returns the square of the flatness of the cubic curve specified
		 * by the control points stored in the indicated array at the
		 * indicated index. The flatness is the maximum distance
		 * of a control point from the line connecting the end points.
		 * @param coords an array containing coordinates
		 * @param offset the index of <code>coords</code> from which to begin
		 *          getting the end points and control points of the curve
		 * @return the square of the flatness of the <code>CubicCurve2D</code>
		 *          specified by the coordinates in <code>coords</code> at
		 *          the specified offset.
		 */
		private static double getCurveSquaredFlatness(double coords[], int offset) {
			return Math.max(
					AbstractSegment2F.distanceSquaredSegmentPoint(
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 2],
							coords[offset + 3],
							coords[offset + 0],
							coords[offset + 1],
							null),
					AbstractSegment2F.distanceSquaredSegmentPoint(
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 4],
							coords[offset + 5],
							coords[offset + 0],
							coords[offset + 1],
							null));
		}

		/**
		 * Subdivides the cubic curve specified by the coordinates
		 * stored in the <code>src</code> array at indices <code>srcoff</code>
		 * through (<code>srcoff</code>&nbsp;+&nbsp;7) and stores the
		 * resulting two subdivided curves into the two result arrays at the
		 * corresponding indices.
		 * Either or both of the <code>left</code> and <code>right</code>
		 * arrays may be <code>null</code> or a reference to the same array
		 * as the <code>src</code> array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve. Thus,
		 * it is possible to pass the same array for <code>left</code>
		 * and <code>right</code> and to use offsets, such as <code>rightoff</code>
		 * equals (<code>leftoff</code> + 6), in order
		 * to avoid allocating extra storage for this common point.
		 * @param src the array holding the coordinates for the source curve
		 * @param srcoff the offset into the array of the beginning of the
		 * the 6 source coordinates
		 * @param left the array for storing the coordinates for the first
		 * half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 * the 6 left coordinates
		 * @param right the array for storing the coordinates for the second
		 * half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 * the 6 right coordinates
		 */
		private static void subdivideCurve(
				double src[], int srcoff,
				double left[], int leftoff,
				double right[], int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double ctrlx1 = src[srcoff + 2];
			double ctrly1 = src[srcoff + 3];
			double ctrlx2 = src[srcoff + 4];
			double ctrly2 = src[srcoff + 5];
			double x2 = src[srcoff + 6];
			double y2 = src[srcoff + 7];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
			}
			if (right != null) {
				right[rightoff + 6] = x2;
				right[rightoff + 7] = y2;
			}
			x1 = (x1 + ctrlx1) / 2f;
			y1 = (y1 + ctrly1) / 2f;
			x2 = (x2 + ctrlx2) / 2f;
			y2 = (y2 + ctrly2) / 2f;
			double centerx = (ctrlx1 + ctrlx2) / 2f;
			double centery = (ctrly1 + ctrly2) / 2f;
			ctrlx1 = (x1 + centerx) / 2f;
			ctrly1 = (y1 + centery) / 2f;
			ctrlx2 = (x2 + centerx) / 2f;
			ctrly2 = (y2 + centery) / 2f;
			centerx = (ctrlx1 + ctrlx2) / 2f;
			centery = (ctrly1 + ctrly2) / 2f;
			if (left != null) {
				left[leftoff + 2] = x1;
				left[leftoff + 3] = y1;
				left[leftoff + 4] = ctrlx1;
				left[leftoff + 5] = ctrly1;
				left[leftoff + 6] = centerx;
				left[leftoff + 7] = centery;
			}
			if (right != null) {
				right[rightoff + 0] = centerx;
				right[rightoff + 1] = centery;
				right[rightoff + 2] = ctrlx2;
				right[rightoff + 3] = ctrly2;
				right[rightoff + 4] = x2;
				right[rightoff + 5] = y2;
			}
		}
		
		private void searchNext(boolean isFirst) {
			do {
				flattening();
			}
			while (!this.done && !isFirst && isSame());
		}
		
		private boolean isSame() {
			PathElementType type = this.holdType;
			long x, y;
			if (type==PathElementType.CLOSE) {
				x = Math.round(this.moveX);
				y = Math.round(this.moveY);
			}
			else {
				x = Math.round(this.hold[this.holdIndex + 0]);
				y = Math.round(this.hold[this.holdIndex + 1]);
			}
			return x==this.lastNextX && y==this.lastNextY;
		}

		private void flattening() {
			int level;

			if (this.holdIndex >= this.holdEnd) {
				if (!this.pathIterator.hasNext()) {
					this.done = true;
					return;
				}
				PathElement2i pathElement = this.pathIterator.next();
				this.holdType = pathElement.type;
				pathElement.toArray(this.hold);
				this.levelIndex = 0;
				this.levels[0] = 0;
			}

			switch (this.holdType) {
			case MOVE_TO:
			case LINE_TO:
				this.currentX = this.hold[0];
				this.currentY = this.hold[1];
				if (this.holdType == PathElementType.MOVE_TO) {
					this.moveX = this.currentX;
					this.moveY = this.currentY;
				}
				this.holdIndex = 0;
				this.holdEnd = 0;
				break;
			case CLOSE:
				this.currentX = this.moveX;
				this.currentY = this.moveY;
				this.holdIndex = 0;
				this.holdEnd = 0;
				break;
			case QUAD_TO:
				if (this.holdIndex >= this.holdEnd) {
					// Move the coordinates to the end of the array.
					this.holdIndex = this.hold.length - 6;
					this.holdEnd = this.hold.length - 2;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.hold[0];
					this.hold[this.holdIndex + 3] = this.hold[1];
					this.hold[this.holdIndex + 4] = this.currentX = this.hold[2];
					this.hold[this.holdIndex + 5] = this.currentY = this.hold[3];
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getQuadSquaredFlatness(this.hold, this.holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(4);
					subdivideQuad(
							this.hold, this.holdIndex,
							this.hold, this.holdIndex - 4,
							this.hold, this.holdIndex);
					this.holdIndex -= 4;

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+4 and holdIndex+5 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 4;
				this.levelIndex--;
				break;
			case CURVE_TO:
				if (this.holdIndex >= this.holdEnd) {
					// Move the coordinates to the end of the array.
					this.holdIndex = this.hold.length - 8;
					this.holdEnd = this.hold.length - 2;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.hold[0];
					this.hold[this.holdIndex + 3] = this.hold[1];
					this.hold[this.holdIndex + 4] = this.hold[2];
					this.hold[this.holdIndex + 5] = this.hold[3];
					this.hold[this.holdIndex + 6] = this.currentX = this.hold[4];
					this.hold[this.holdIndex + 7] = this.currentY = this.hold[5];
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getCurveSquaredFlatness(this.hold,this. holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(6);
					subdivideCurve(
							this.hold, this.holdIndex,
							this.hold, this.holdIndex - 6,
							this.hold, this.holdIndex);
					this.holdIndex -= 6;

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+6 and holdIndex+7 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 6;
				this.levelIndex--;
				break;
			default:
			}
		}

		@Override
		public boolean hasNext() {
			return !this.done;
		}

		@Override
		public PathElement2i next() {
			if (this.done) {
				throw new NoSuchElementException("flattening iterator out of bounds"); //$NON-NLS-1$
			}

			PathElement2i element;
			PathElementType type = this.holdType;
			if (type!=PathElementType.CLOSE) {
				int x = (int) Math.round(this.hold[this.holdIndex + 0]);
				int y = (int) Math.round(this.hold[this.holdIndex + 1]);
				if (type == PathElementType.MOVE_TO) {
					element = new PathElement2i.MovePathElement2i(x, y);
				}
				else {
					element = new PathElement2i.LinePathElement2i(
							this.lastNextX, this.lastNextY,
							x, y);
				}
				this.lastNextX = x;
				this.lastNextY = y;
			}
			else {
				int x = (int) Math.round(this.moveX);
				int y = (int) Math.round(this.moveY);
				element = new PathElement2i.ClosePathElement2i(
						this.lastNextX, this.lastNextY,
						x, y);
				this.lastNextX = x;
				this.lastNextY = y;
			}

			searchNext(false);

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.windingRule;
		}
		
		@Override
		public boolean isPolyline() {
			return false; // Because the iterator flats the path, this is no curve inside.
		}

	} // class FlatteningPathIterator


	@Override
	public PathIterator2f getPathIterator(double flatness) {	
		return null;
	}

	@Override
	public PathIterator2d getPathIteratorProperty(double flatness) {
		return null;
	}

	@Override
	public PathIterator2d getPathIteratorProperty() {	
		return null;
	}

	@Override
	public PathIterator2f getPathIterator() {
		return null;
	}

	

}
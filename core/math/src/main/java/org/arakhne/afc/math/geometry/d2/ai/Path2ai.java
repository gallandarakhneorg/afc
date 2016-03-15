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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.arakhne.afc.math.geometry.d2.PathIterator2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D path on a plane.
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
public interface Path2ai<
ST extends Shape2ai<?, ?, IE, P, B>,
IT extends Path2ai<?, ?, IE, P, B>,
IE extends PathElement2ai,
P extends Point2D,
B extends Rectangle2ai<?, ?, IE, P, B>>
extends Shape2ai<ST, IT, IE, P, B>, Path2D<ST, IT, PathIterator2ai<IE>, P, B> {

	/** Multiple of cubic & quad curve size.
	 */
	static final int GROW_SIZE = 24;

	/** The default winding rule: {@link PathWindingRule#NON_ZERO}.
	 */
	static final PathWindingRule DEFAULT_WINDING_RULE = PathWindingRule.NON_ZERO;

	/** Compute the box that corresponds to the drawable elements of the path.
	 * 
	 * <p>An element is drawable if it is a line, a curve, or a closing path element.
	 * 
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a drawable element was found.
	 */
	static boolean computeDrawableElementBoundingBox(PathIterator2ai<?> iterator,
			Rectangle2ai<?, ?, ?, ?, ?> box) {
		GeomFactory2ai<?, ?, ?> factory = iterator.getGeomFactory();
		boolean foundOneLine = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		PathElement2ai element;
		Path2ai<?, ?, ?, ?, ?> subPath;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.getType()) {
			case LINE_TO:
				if (element.getFromX()<xmin) xmin = element.getFromX();
				if (element.getFromY()<ymin) ymin = element.getFromY();
				if (element.getFromX()>xmax) xmax = element.getFromX();
				if (element.getFromY()>ymax) ymax = element.getFromY();
				if (element.getToX()<xmin) xmin = element.getToX();
				if (element.getToY()<ymin) ymin = element.getToY();
				if (element.getToX()>xmax) xmax = element.getToX();
				if (element.getToY()>ymax) ymax = element.getToY();
				foundOneLine = true;
				break;
			case CURVE_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						element.getToX(), element.getToY());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMinY()>ymax) ymax = box.getMinY();
					foundOneLine = true;
				}
				break;
			case QUAD_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getToX(), element.getToY());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMinY()>ymax) ymax = box.getMinY();
					foundOneLine = true;
				}
				break;
			case MOVE_TO:
			case CLOSE:
			default:
			}
		}
		if (box != null) {
			if (foundOneLine) {
				box.setFromCorners(xmin, ymin, xmax, ymax);
			}
			else {
				box.clear();
			}
		}
		return foundOneLine;
	}

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
	static int computeCrossingsFromSegment(PathIterator2ai<?> pi, int x1, int y1, int x2, int y2) {
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
	static int computeCrossingsFromSegment(int crossings, PathIterator2ai<?> pi, int x1, int y1, int x2, int y2, boolean closeable) {	
		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement2ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int curx = movx;
		int cury = movy;
		int endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = Segment2ai.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, x2, y2,
						curx, cury,
						endx, endy);
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false);
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false);
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = Segment2ai.computeCrossingsFromSegment(
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
			numCrosses = Segment2ai.computeCrossingsFromSegment(
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
	static int computeCrossingsFromCircle(PathIterator2ai<?> pi, int cx, int cy, int radius) {
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
	static int computeCrossingsFromCircle(int crossings, PathIterator2ai<?> pi, int cx, int cy, int radius, boolean closeable) {	
		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement2ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int curx = movx;
		int cury = movy;
		int endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = Segment2ai.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, radius,
						curx, cury,
						endx, endy);
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						false);
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						false);
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = Segment2ai.computeCrossingsFromCircle(
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
			numCrosses = Segment2ai.computeCrossingsFromCircle(
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
	static int computeCrossingsFromPoint(PathIterator2ai<?> pi, int px, int py) {
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
	static int computeCrossingsFromPoint(PathIterator2ai<?> pi, int px, int py, boolean autoClose) {
		// Copied and adapted from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement2ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int curx = movx;
		int cury = movy;
		int endx, endy;
		int crossings = 0;

		while (pi.hasNext()) {
			element = pi.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				crossings = Segment2ai.computeCrossingsFromPoint(
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
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY());
				curve.quadTo(element.getCtrlX1(), element.getCtrlY1(), endx, endy);
				int numCrosses = computeCrossingsFromPoint(
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				crossings += numCrosses;
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY());
				curve.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromPoint(
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
					crossings = Segment2ai.computeCrossingsFromPoint(
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
			crossings = Segment2ai.computeCrossingsFromPoint(
					crossings,
					px, py,
					curx, cury,
					movx, movy);
		}

		return crossings;
	}

	/**
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator2ai}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2ai} interface to implement support for the
	 * {@link Shape2ai#contains(int, int)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @return {@code true} if the specified coordinates are inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise
	 */
	static boolean contains(PathIterator2ai<?> pi, int x, int y) {
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
	static int computeCrossingsFromRect(PathIterator2ai<?> pi,
			int rxmin, int rymin,
			int rxmax, int rymax) {
		return PrivateAPI.computeCrossingsFromRect(pi, rxmin, rymin, rxmax, rymax, true, true);
	}

	/**
	 * Tests if the specified rectangle is inside the closed
	 * boundary of the specified {@link PathIterator2ai}.
	 *
	 * @param pi the specified {@code PathIterator2ai}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	static boolean contains(PathIterator2ai<?> pi, int rx, int ry, int rwidth, int rheight) {
		// Copied and adapted from AWT API
		if (rwidth <= 0 || rheight <= 0) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = PrivateAPI.computeCrossingsFromRect(pi, rx, ry, rx+rwidth, ry+rheight, true, false);
		return (crossings != MathConstants.SHAPE_INTERSECTS &&
				(crossings & mask) != 0);
	}

	/**
	 * Tests if the interior of the specified {@link PathIterator2ai}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2ai} interface to implement support for the
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
	static boolean intersects(PathIterator2ai<?> pi, int x, int y, int w, int h) {
		if (w <= 0f || h <= 0f) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = PrivateAPI.computeCrossingsFromRect(pi, x, y, x+w, y+h, true, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	/** Replies the point on the path that is closest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2D#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getClosestPointTo(Point2D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param result the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	static void getClosestPointTo(PathIterator2ai<? extends PathElement2ai> pi, int x, int y, Point2D result) {
		double bestDist = Double.POSITIVE_INFINITY;
		Point2D candidate;
		PathElement2ai pe;

		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int crossings = 0;
		boolean isClosed = false;
		int moveX, moveY, currentX, currentY;
		moveX = moveY = currentX = currentY = 0;

		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			currentX = pe.getToX();
			currentY = pe.getToY();

			switch(pe.getType()) {
			case MOVE_TO:
				moveX = pe.getToX();
				moveY = pe.getToY();
				isClosed = false;
				break;
			case LINE_TO:
			{
				isClosed = false;
				candidate = pi.getGeomFactory().newPoint();
				Segment2ai.computeClosestPointTo(pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(), x,y, candidate);
				if (crossings!=MathConstants.SHAPE_INTERSECTS) {
					crossings = Segment2ai.computeCrossingsFromPoint(
							crossings,
							x, y,
							pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				}
				break;
			}
			case CLOSE:
				isClosed = true;
				if (!pe.isEmpty()) {
					candidate = pi.getGeomFactory().newPoint();
					Segment2ai.computeClosestPointTo(pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(), x, y, candidate);
					if (crossings!=MathConstants.SHAPE_INTERSECTS) {
						crossings = Segment2ai.computeCrossingsFromPoint(
								crossings,
								x, y,
								pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
					}
				}
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException();
			}

			if (candidate!=null) {
				double d = Point2D.getDistanceSquaredPointPoint(x, y, candidate.ix(), candidate.iy());
				if (d<=0) {
					result.set(candidate);
					return;
				}
				if (d<bestDist) {
					bestDist = d;
					result.set(candidate);
				}
			}
		}

		if (!isClosed && crossings!=MathConstants.SHAPE_INTERSECTS) {
			crossings = Segment2ai.computeCrossingsFromPoint(
					crossings,
					x, y,
					currentX, currentY,
					moveX, moveY);
		}

		if (crossings==MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0) {
			result.set(x, y);
		}
	}

	/** Replies the point on the path that is farthest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2D#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getFarthestPointTo(Point2D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param result the farthest point on the shape.
	 */
	static void getFarthestPointTo(PathIterator2ai<? extends PathElement2ai> pi, int x, int y, Point2D result) {
		double bestX = Double.NaN;
		double bestY = Double.NaN;
		double bestDist = Double.NEGATIVE_INFINITY;
		PathElement2ai pe;
		// Only for internal use.
		Point2D point = new FakePoint();

		while (pi.hasNext()) {
			pe = pi.next();

			boolean foundCandidate = false;
			double candidateX = Double.NaN;
			double candidateY = Double.NaN;

			switch(pe.getType()) {
			case MOVE_TO:
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				break;
			case LINE_TO:
			case CLOSE:
				Segment2ai.computeFarthestPointTo(
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(),
						x, y, point);
				foundCandidate = true;
				candidateX = point.getX();
				candidateY = point.getY();
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException(
						pe.getType() == null ? null : pe.getType().toString());
			}

			if (foundCandidate) {
				double d = Point2D.getDistanceSquaredPointPoint(x, y, candidateX, candidateY);
				if (d > bestDist) {
					bestDist = d;
					bestX = candidateX;
					bestY = candidateX;
				}
			}
		}

		result.set(bestX, bestY);
	}

	/** Add the elements replied by the iterator into this path.
	 * 
	 * @param iterator
	 */
	default void add(Iterator<? extends PathElement2ai> iterator) {
		PathElement2ai element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.getType()) {
			case MOVE_TO:
				moveTo(element.getToX(), element.getToY());
				break;
			case LINE_TO:
				lineTo(element.getToX(), element.getToY());
				break;
			case QUAD_TO:
				quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getToX(), element.getToY());
				break;
			case CURVE_TO:
				curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlX2(), element.getCtrlY2(), element.getToX(), element.getToY());
				break;
			case CLOSE:
				closePath();
				break;
			default:
			}
		}
	}

	/** Set the path.
	 *
	 * @param s the path to copy.
	 */
	default void set(Path2ai<?, ?, ?, ?, ?> s) {
		clear();
		add(s.getPathIterator());
	}

	/**
	 * Adds a point to the path by moving to the specified
	 * coordinates specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	void moveTo(int x, int y);

	@Override
	default void moveTo(Point2D position) {
		moveTo(position.ix(), position.iy());
	}

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	void lineTo(int x, int y);

	@Override
	default void lineTo(Point2D to) {
		lineTo(to.ix(), to.iy());
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
	void quadTo(int x1, int y1, int x2, int y2);

	@Override
	default void quadTo(Point2D ctrl, Point2D to) {
		quadTo(ctrl.ix(), ctrl.iy(), to.ix(), to.iy());
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
	void curveTo(int x1, int y1,
			int x2, int y2,
			int x3, int y3);

	@Override
	default void curveTo(Point2D ctrl1, Point2D ctrl2, Point2D to) {
		curveTo(ctrl1.ix(), ctrl1.iy(), ctrl2.ix(), ctrl2.iy(), to.ix(), to.iy());

	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
	}

	@Override
	default P getClosestPointTo(Point2D p) {
		P point = getGeomFactory().newPoint();
		getClosestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), p.ix(), p.iy(), point);
		return point;
	}

	@Override
	default P getFarthestPointTo(Point2D p) {
		P point = getGeomFactory().newPoint();
		getFarthestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), p.ix(), p.iy(), point);
		return point;
	}

	@Override
	default boolean equals(PathIterator2ai<IE> iterator) {
		if (iterator != null) {
			PathIterator2ai<IE> myIterator = getPathIterator();
			while (iterator.hasNext() && myIterator.hasNext()) {
				IE element1 = iterator.next();
				IE element2 = myIterator.next();
				if (!element1.equals(element2)) {
					return false;
				}
			}
			return !iterator.hasNext() && !myIterator.hasNext();
		}
		return false;
	}

	@Override
	default double lengthSquared() {
		if (isEmpty()) return 0;

		double length = 0;

		PathIterator2ai<?> pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		PathElement2ai pathElement = pi.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		// only for internal use
		GeomFactory2ai<IE, P, B> factory = getGeomFactory();
		Path2ai<?, ?, ?, ?, ?> subPath;
		int curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();

		while (pi.hasNext()) {
			pathElement = pi.next();

			switch (pathElement.getType()) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();

				length += Point2D.getDistanceSquaredPointPoint(
						curx, cury,  
						endx, endy);

				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = factory.newPath(getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						endx, endy);

				length += subPath.lengthSquared();

				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = factory.newPath(getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(),
						endx, endy);

				length += subPath.lengthSquared();

				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					length += Point2D.getDistanceSquaredPointPoint(
							curx, cury, 
							movx, movy);
				}

				curx = movx;
				cury = movy;
				break;
			default:
			}

		}

		return length;
	}

	/** Replies the coordinate at the given index.
	 * The index is in [0;{@link #size()}*2).
	 *
	 * @param index
	 * @return the coordinate at the given index.
	 */
	@Pure
	int getCoordAt(int index);

	/** Change the coordinates of the last inserted point.
	 * 
	 * @param x
	 * @param y
	 */
	void setLastPoint(int x, int y);

	@Override
	default void setLastPoint(Point2D point) {
		setLastPoint(point.ix(), point.iy());
	}

	/** Transform the current path.
	 * This function changes the current path.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform2D transform);

	@Pure
	@Override
	default boolean contains(Rectangle2ai<?, ?, ?, ?, ?> box) {
		return contains(getPathIterator(),
				box.getMinX(), box.getMinY(), box.getWidth(), box.getHeight());
	}

	@Override
	default boolean contains(int x, int y) {
		return contains(getPathIterator(), x, y);
	}

	/** Remove the point with the given coordinates.
	 * 
	 * <p>If the given coordinates do not match exactly a point in the path, nothing is removed.
	 * 
	 * @param x the x coordinate of the point to remove.
	 * @param y the y coordinate of the point to remove.
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(int x, int y);

	@Override
	default PathIterator2ai<IE> getPathIterator(double flatness) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default PathIterator2ai<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new PathPathIterator<>(this);
		}
		return new TransformedPathIterator<>(this, transform);
	}

	@Override
	default Iterator<P> getPointIterator() {
		PathIterator2ai<IE> pathIterator = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		return new PixelIterator<>(pathIterator, getGeomFactory());
	}

	@Override
	default boolean intersects(Circle2ai<?, ?, ?, ?, ?> s) {
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromCircle(
				getPathIterator(),
				s.getX(), s.getY(), s.getRadius());
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	default boolean intersects(Rectangle2ai<?, ?, ?, ?, ?> s) {
		// Copied from AWT API
		if (s.isEmpty()) return false;
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = PrivateAPI.computeCrossingsFromRect(
				getPathIterator(),
				s.getMinX(), s.getMinY(), s.getMaxX(), s.getMaxY(),
				true, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	default boolean intersects(Segment2ai<?, ?, ?, ?, ?> s) {
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromSegment(
				getPathIterator(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	default Collection<Point2D> toCollection() {
		return new PointCollection(this);
	}
	
	/** Private API for Path2ai.
	 * 
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	final class PrivateAPI {

		private PrivateAPI() {
			//
		}

		/** Not documented.
		 *
		 * @param crossings the previous value of the crossing.
		 * @param rxmin not documented.
		 * @param rymin not documented.
		 * @param rxmax not documented.
		 * @param rymax not documented.
		 * @param curx not documented.
		 * @param cury not documented.
		 * @param movx not documented.
		 * @param movy not documented.
		 * @param intersectingBehavior
		 * @return thr crossing.
		 */
		@Pure
		private static int crossingHelper(
				int crossings,
				int rxmin, int rymin,
				int rxmax, int rymax,
				int curx, int cury,
				int movx, int movy,
				boolean intersectingBehavior) {
			int crosses = Segment2ai.computeCrossingsFromRect(crossings,
					rxmin, rymin,
					rxmax, rymax,
					curx, cury,
					movx, movy);
			if (!intersectingBehavior && crosses==MathConstants.SHAPE_INTERSECTS) {
				int x1 = rxmin+1;
				int x2 = rxmax-1;
				int y1 = rymin+1;
				int y2 = rymax-1;
				crosses = Segment2ai.computeCrossingsFromRect(crossings,
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
		public static int computeCrossingsFromRect(PathIterator2ai<?> pi,
				int rxmin, int rymin,
				int rxmax, int rymax,
				boolean autoClose,
				boolean intersectingBehavior) {
			// Copied from AWT API
			if (rxmax <= rxmin || rymax <= rymin) return 0;
			if (!pi.hasNext()) return 0;

			PathElement2ai pathElement = pi.next();

			if (pathElement.getType() != PathElementType.MOVE_TO) {
				throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
			}

			int curx, cury, movx, movy, endx, endy;
			curx = movx = pathElement.getToX();
			cury = movy = pathElement.getToY();
			int crossings = 0;

			while (crossings != MathConstants.SHAPE_INTERSECTS
					&& pi.hasNext()) {
				pathElement = pi.next();
				switch (pathElement.getType()) {
				case MOVE_TO:
					// Count should always be a multiple of 2 here.
					// assert((crossings & 1) != 0);
					movx = curx = pathElement.getToX();
					movy = cury = pathElement.getToY();
					break;
				case LINE_TO:
					endx = pathElement.getToX();
					endy = pathElement.getToY();
					crossings = crossingHelper(crossings,
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
					endx = pathElement.getToX();
					endy = pathElement.getToY();
					Path2ai<?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
					curve.moveTo(pathElement.getFromX(), pathElement.getFromY());
					curve.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), endx, endy);
					int numCrosses = computeCrossingsFromRect(
							curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
					endx = pathElement.getToX();
					endy = pathElement.getToY();
					curve = pi.getGeomFactory().newPath(pi.getWindingRule());
					curve.moveTo(pathElement.getFromX(), pathElement.getFromY());
					curve.curveTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlX2(), pathElement.getCtrlY2(), endx, endy);
					numCrosses = computeCrossingsFromRect(
							curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
						crossings = crossingHelper(crossings,
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
				crossings = crossingHelper(crossings,
						rxmin, rymin, rxmax, rymax,
						curx, cury, movx, movy,
						intersectingBehavior);
			}

			// Count should always be a multiple of 2 here.
			// assert((crossings & 1) != 0);
			return crossings;
		}

	}

	/** An abstract path iterator.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	abstract class AbstractPathIterator<E extends PathElement2ai> implements PathIterator2ai<E> {

		/** Element factory.
		 */
		protected final GeomFactory2ai<E, ?, ?> factory;

		/** Path.
		 */
		protected final Path2ai<?, ?, E, ?, ?> path;

		/**
		 * @param path the path.
		 */
		public AbstractPathIterator(Path2ai<?, ?, E, ?, ?> path) {
			this.path = path;
			this.factory = path.getGeomFactory();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.path.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return this.path.isPolyline();
		}

		@Override
		public boolean isCurved() {
			return this.path.isCurved();
		}

		@Override
		public boolean isMultiParts() {
			return this.path.isMultiParts();
		}

		@Override
		public boolean isPolygon() {
			return this.path.isPolygon();
		}

		@Override
		public GeomFactory2ai<E, ?, ?> getGeomFactory() {
			return this.factory;
		}

	}

	/** A path iterator that does not transform the coordinates.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PathPathIterator<E extends PathElement2ai> extends AbstractPathIterator<E> {

		private final Point2D p1;

		private final Point2D p2;

		private int iType = 0;

		private int iCoord = 0;

		private int movex;

		private int movey;

		/**
		 * @param path the path.
		 */
		public PathPathIterator(Path2ai<?, ?, E, ?, ?> path) {
			super(path);
			this.p1 = this.factory.newPoint();
			this.p2 = this.factory.newPoint();
		}

		@Override
		public boolean hasNext() {
			return this.iType < this.path.getPathElementCount();
		}

		@Override
		public E next() {
			int type = this.iType;
			if (this.iType>=this.path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			E element = null;
			switch(this.path.getPathElementTypeAt(type)) {
			case MOVE_TO:
				if (this.iCoord+2>(this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.movex = this.path.getCoordAt(this.iCoord++);
				this.movey = this.path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey);
				element = this.factory.newMovePathElement(
						this.p2.ix(), this.p2.iy());
				break;
			case LINE_TO:
				if (this.iCoord+2>(this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				element = this.factory.newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case QUAD_TO:
			{
				if (this.iCoord+4>(this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				int ctrlx = this.path.getCoordAt(this.iCoord++);
				int ctrly = this.path.getCoordAt(this.iCoord++);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				element = this.factory.newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						ctrlx, ctrly,
						this.p2.ix(), this.p2.iy());
			}
			break;
			case CURVE_TO:
			{
				if (this.iCoord+6>(this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				int ctrlx1 = this.path.getCoordAt(this.iCoord++);
				int ctrly1 = this.path.getCoordAt(this.iCoord++);
				int ctrlx2 = this.path.getCoordAt(this.iCoord++);
				int ctrly2 = this.path.getCoordAt(this.iCoord++);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				element = this.factory.newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.p2.ix(), this.p2.iy());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				element = this.factory.newClosePathElement(
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

	}

	/** A path iterator that transforms the coordinates.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class TransformedPathIterator<E extends PathElement2ai> extends AbstractPathIterator<E> {

		private final Transform2D transform;

		private final Point2D p1;

		private final Point2D p2;

		private final Point2D ptmp1;

		private final Point2D ptmp2;

		private int iType = 0;

		private int iCoord = 0;

		private int movex, movey;

		/**
		 * @param path the path.
		 * @param transform the transformation.
		 */
		public TransformedPathIterator(Path2ai<?, ?, E, ?, ?> path, Transform2D transform) {
			super(path);
			assert(transform!=null);
			this.transform = transform;
			this.p1 = this.factory.newPoint();
			this.p2 = this.factory.newPoint();
			this.ptmp1 = this.factory.newPoint();
			this.ptmp2 = this.factory.newPoint();
		}

		@Override
		public boolean hasNext() {
			return this.iType<this.path.getPathElementCount();
		}

		@Override
		public E next() {
			if (this.iType>=this.path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			E element = null;
			switch(this.path.getPathElementTypeAt(this.iType++)) {
			case MOVE_TO:
				this.movex = this.path.getCoordAt(this.iCoord++);
				this.movey = this.path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = this.factory.newMovePathElement(
						this.p2.ix(), this.p2.iy());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = this.factory.newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = this.factory.newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						this.ptmp1.ix(), this.ptmp1.iy(),
						this.p2.ix(), this.p2.iy());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp2);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = this.factory.newCurvePathElement(
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
				element = this.factory.newClosePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();
			return element;
		}

	}

	/** Iterator on the pixels of the path.
	 *
	 * @param <P> the type of the points.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PixelIterator<P extends Point2D> implements Iterator<P> {

		private final PathIterator2ai<?> pathIterator;

		private final GeomFactory2ai<?, P, ?> factory;

		private Iterator<P> lineIterator = null;

		private P next = null;

		/**
		 * @param pi the iterator.
		 * @param factory the element factory.
		 */
		public PixelIterator(PathIterator2ai<?> pi, GeomFactory2ai<?, P, ?> factory) {
			this.pathIterator = pi;
			this.factory = factory;
			searchNext();
		}

		private void searchNext() {
			P old = this.next;
			this.next = null;
			while (this.pathIterator.hasNext() && (this.lineIterator==null || !this.lineIterator.hasNext())) {
				this.lineIterator = null;
				PathElement2ai elt = this.pathIterator.next();
				if (elt.isDrawable()) {
					switch(elt.getType()) {
					case LINE_TO:
						Segment2ai<?, ?, ?, P, ?> segment = this.factory.newSegment(
								elt.getFromX(), elt.getFromY(),
								elt.getToX(), elt.getToY());
						this.lineIterator = segment.getPointIterator();
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
		public P next() {
			P n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/** An collection of the points of the path.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointCollection implements Collection<Point2D> {

		private final Path2ai<?, ?, ?, ?, ?> path;
		
		/**
		 * @param path the path from which the points are extracted.
		 */
		public PointCollection(Path2ai<?, ?, ?, ?, ?> path) {
			this.path = path;
		}

		@Override
		public int size() {
			return this.path.size();
		}

		@Override
		public boolean isEmpty() {
			return this.path.size()<=0;
		}

		@Override
		public boolean contains(Object o) {
			if (o instanceof Point2D) {
				return this.path.contains((Point2D)o);
			}
			return false;
		}

		@Override
		public Iterator<Point2D> iterator() {
			return new PointIterator(this.path);
		}

		@Override
		public Object[] toArray() {
			return this.path.toPointArray();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Iterator<Point2D> iterator = new PointIterator(this.path);
			for(int i=0; i<a.length && iterator.hasNext(); ++i) {
				a[i] = (T)iterator.next();
			}
			return a;
		}

		@Override
		public boolean add(Point2D e) {
			if (e!=null) {
				if (this.path.size()==0) {
					this.path.moveTo(e.ix(), e.iy());
				}
				else {
					this.path.lineTo(e.ix(), e.iy());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point2D) {
				Point2D p = (Point2D)o;
				return this.path.remove(p.ix(), p.iy());
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for(Object obj : c) {
				if ((!(obj instanceof Point2D))
						||(!this.path.contains((Point2D)obj))) {
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
					if (this.path.remove(pts.ix(), pts.iy())) {
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
			this.path.clear();
		}

	}

	/** Iterator on the points of the path.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointIterator implements Iterator<Point2D> {

		private final Path2ai<?, ?, ?, ?, ?> path;
		
		private int index = 0;
		
		private Point2D lastReplied = null;

		/**
		 * @param path the path to iterate on.
		 */
		public PointIterator(Path2ai<?, ?, ?, ?, ?> path) {
			this.path = path;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.path.size();
		}

		@Override
		public Point2D next() {
			try {
				this.lastReplied = this.path.getPointAt(this.index++);
				return this.lastReplied;
			} catch(Throwable exception) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			Point2D p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			this.path.remove(p.ix(), p.iy());
		}

	}

}

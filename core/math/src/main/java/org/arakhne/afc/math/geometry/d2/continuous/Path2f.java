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
package org.arakhne.afc.math.geometry.d2.continuous;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.discrete.PathIterator2i;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


/** A generic path.
 *
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Path2f extends AbstractShape2F<Path2f> implements Path2D<Shape2F,Rectangle2f,AbstractPathElement2F,PathIterator2f> {

	private static final long serialVersionUID = -873231223923726975L;

	/** Multiple of cubic & quad curve size.
	 */
	static final int GROW_SIZE = 24;

	/** Replies the point on the path that is closest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2f#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getClosestPointTo(Point2D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @return the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	public static Point2D getClosestPointTo(PathIterator2f pi, double x, double y) {
		Point2D closest = null;
		double bestDist = Double.POSITIVE_INFINITY;
		Point2D candidate;
		AbstractPathElement2F pe;

		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int crossings = 0;

		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			switch(pe.type) {
			case MOVE_TO:
				candidate = new Point2f(pe.getToX(), pe.getToY());
				break;
			case LINE_TO:
			{
				double factor =  AbstractSegment2F.computeProjectedPointOnLine(
						x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				factor = MathUtil.clamp(factor, 0f, 1f);
				Vector2f v = new Vector2f(pe.getToX(), pe.getToY());
				v.sub(pe.getFromX(), pe.getFromY());
				v.scale(factor);
				candidate = new Point2f(
						pe.getFromX() + v.getX(),
						pe.getFromY() + v.getY());
				crossings += AbstractSegment2F.computeCrossingsFromPoint(
						x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				break;
			}
			case CLOSE:
				crossings += AbstractSegment2F.computeCrossingsFromPoint(
						x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				if ((crossings & mask) != 0) return new Point2f(x, y);

				if (!pe.isEmpty()) {
					double factor =  AbstractSegment2F.computeProjectedPointOnLine(
							x, y,
							pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
					factor = MathUtil.clamp(factor, 0f, 1f);
					Vector2f v = new Vector2f(pe.getToX(), pe.getToY());
					v.sub(pe.getFromX(), pe.getFromY());
					v.scale(factor);
					candidate = new Point2f(
							pe.getFromX() + v.getX(),
							pe.getFromY() + v.getY());
				}
				crossings = 0;
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException(
						pe.type==null ? null : pe.type.toString());
			}

			if (candidate!=null) {
				double d = candidate.getDistanceSquared(new Point2f(x,y));
				if (d<bestDist) {
					bestDist = d;
					closest = candidate;
				}
			}
		}

		return closest;
	}

	/** Replies the point on the path that is farthest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2f#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getFarthestPointTo(Point2D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @return the farthest point on the shape.
	 */
	public static Point2D getFarthestPointTo(PathIterator2f pi, double x, double y) {
		Point2D closest = null;
		double bestDist = Double.NEGATIVE_INFINITY;
		Point2D candidate;
		AbstractPathElement2F pe;

		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			switch(pe.type) {
			case MOVE_TO:
				candidate = new Point2f(pe.getToX(), pe.getToY());
				break;
			case LINE_TO:
			case CLOSE:
				candidate = AbstractSegment2F.computeFarthestPointTo(
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(),
						x, y);
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException(
						pe.type==null ? null : pe.type.toString());
			}

			if (candidate!=null) {
				double d = candidate.getDistanceSquared(new Point2f(x,y));
				if (d>bestDist) {
					bestDist = d;
					closest = candidate;
				}
			}
		}

		return closest;
	}

	/**
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator2f}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2F} interface to implement support for the
	 * {@link Shape2F#contains(double, double)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @return {@code true} if the specified coordinates are inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise
	 */
	public static boolean contains(PathIterator2f pi, double x, double y) {
		// Copied from the AWT API
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int cross = computeCrossingsFromPoint(pi, x, y, false, true);
		return ((cross & mask) != 0);
	}

	/**
	 * Tests if the specified rectangle is inside the closed
	 * boundary of the specified {@link PathIterator2f}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2F} interface to implement support for the
	 * {@link Shape2F#contains(Rectangle2f)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	public static boolean contains(PathIterator2f pi, double rx, double ry, double rwidth, double rheight) {
		// Copied from AWT API
		if (rwidth <= 0 || rheight <= 0) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(
				pi, 
				rx, ry, rx+rwidth, ry+rheight,
				false,
				true);
		return (crossings != MathConstants.SHAPE_INTERSECTS &&
				(crossings & mask) != 0);
	}

	/**
	 * Tests if the interior of the specified {@link PathIterator2f}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2F} interface to implement support for the
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
	public static boolean intersects(PathIterator2f pi, double x, double y, double w, double h) {
		if (w <= 0f || h <= 0f) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(pi, x, y, x+w, y+h, false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
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
	public static int computeCrossingsFromPoint(PathIterator2f pi, double px, double py) {
		return computeCrossingsFromPoint(pi, px, py, true, true);
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
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossing
	 */
	public static int computeCrossingsFromPoint(
			PathIterator2f pi,
			double px, double py,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {	
		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		AbstractPathElement2F element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path2f subPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int r, crossings = 0;
		while (pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				if (endx==px && endy==py)
					return MathConstants.SHAPE_INTERSECTS;
				crossings += AbstractSegment2F.computeCrossingsFromPoint(
						px, py,
						curx, cury,
						endx, endy);
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				if (endx==px && endy==py)
					return MathConstants.SHAPE_INTERSECTS;
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				r = computeCrossingsFromPoint(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py,
						false,
						false);
				if (r==MathConstants.SHAPE_INTERSECTS)
					return r;
				crossings += r;
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				if (endx==px || endy==py)
					return MathConstants.SHAPE_INTERSECTS;
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				r = computeCrossingsFromPoint(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py,
						false,
						false);
				if (r==MathConstants.SHAPE_INTERSECTS) {
					return r;
				}
				crossings += r;
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					if (movx==px && movy==py)
						return MathConstants.SHAPE_INTERSECTS;
					crossings += AbstractSegment2F.computeCrossingsFromPoint(
							px, py,
							curx, cury,
							movx, movy);
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(crossings!=MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				// Not closed
				if (movx==px && movy==py)
					return MathConstants.SHAPE_INTERSECTS;
				crossings += AbstractSegment2F.computeCrossingsFromPoint(
						px, py,
						curx, cury,
						movx, movy);
			}
			else if (onlyIntersectWhenOpen) {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				crossings = 0;
			}
		}

		return crossings;
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given ellipse extending to the right.
	 * 
	 * @param pi is the description of the path.
	 * @param ex is the first point of the ellipse.
	 * @param ey is the first point of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @return the crossing or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	public static int computeCrossingsFromEllipse(PathIterator2f pi, double ex, double ey, double ew, double eh) {
		return computeCrossingsFromEllipse(0, pi, ex, ey, ew, eh, true, true);
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given ellipse extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param pi is the description of the path.
	 * @param ex is the first point of the ellipse.
	 * @param ey is the first point of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossing or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	public static int computeCrossingsFromEllipse(
			int crossings, 
			PathIterator2f pi, 
			double ex, double ey, double ew, double eh, 
			boolean closeable,
			boolean onlyIntersectWhenOpen) {	
		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		AbstractPathElement2F element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = AbstractSegment2F.computeCrossingsFromEllipse(
						numCrosses,
						ex, ey, ew, eh,
						curx, cury,
						endx, endy);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				Path2f localPath = new Path2f();
				localPath.moveTo(curx, cury);
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = computeCrossingsFromEllipse(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						ex, ey, ew, eh,
						false,
						false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2f localPath = new Path2f();
				localPath.moveTo(curx, cury);
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromEllipse(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						ex, ey, ew, eh,
						false,
						false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = AbstractSegment2F.computeCrossingsFromEllipse(
							numCrosses,
							ex, ey, ew, eh,
							curx, cury,
							movx, movy);
					if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(numCrosses!=MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				// Not closed
				numCrosses = AbstractSegment2F.computeCrossingsFromEllipse(
						numCrosses,
						ex, ey, ew, eh,
						curx, cury,
						movx, movy);
			}
			else if (onlyIntersectWhenOpen) {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrosses = 0;
			}
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
	public static int computeCrossingsFromCircle(PathIterator2f pi, double cx, double cy, double radius) {
		return computeCrossingsFromCircle(0, pi, cx, cy, radius, true, true);
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
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossing
	 */
	public static int computeCrossingsFromCircle(
			int crossings, 
			PathIterator2f pi,
			double cx, double cy, double radius,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {	
		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		AbstractPathElement2F element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = AbstractSegment2F.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, radius,
						curx, cury,
						endx, endy);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				Path2f localPath = new Path2f();
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						false,
						false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2f localPath = new Path2f();
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						false,
						false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = AbstractSegment2F.computeCrossingsFromCircle(
							numCrosses,
							cx, cy, radius,
							curx, cury,
							movx, movy);
					if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(numCrosses!=MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				// Not closed
				numCrosses = AbstractSegment2F.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, radius,
						curx, cury,
						movx, movy);
			}
			else if (onlyIntersectWhenOpen) {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrosses = 0;
			}
		}

		return numCrosses;
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
	public static int computeCrossingsFromSegment(PathIterator2f pi, double x1, double y1, double x2, double y2) {
		return computeCrossingsFromSegment(0, pi, x1, y1, x2, y2, true);
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given segment extending to the right.
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
	public static int computeCrossingsFromSegment(int crossings, PathIterator2f pi, double x1, double y1, double x2, double y2, boolean closeable) {	
		// Copied from the AWT API
		if (!pi.hasNext() || crossings==MathConstants.SHAPE_INTERSECTS) return crossings;
		AbstractPathElement2F element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = AbstractSegment2F.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, x2, y2,
						curx, cury,
						endx, endy);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS)
					return numCrosses;
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				Path2f localPath = new Path2f();
				localPath.moveTo(curx, cury);
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS)
					return numCrosses;
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2f localPath = new Path2f();
				localPath.moveTo(curx, cury);
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS)
					return numCrosses;
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = AbstractSegment2F.computeCrossingsFromSegment(
							numCrosses,
							x1, y1, x2, y2,
							curx, cury,
							movx, movy);
				}
				if (numCrosses!=0)	return numCrosses;
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(numCrosses!=MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				numCrosses = AbstractSegment2F.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, x2, y2,
						curx, cury,
						movx, movy);
			}
			else {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrosses = 0;
			}
		}

		return numCrosses;
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
	public static int computeCrossingsFromRect(PathIterator2f pi,
			double rxmin, double rymin,
			double rxmax, double rymax) {
		return computeCrossingsFromRect(pi, rxmin, rymin, rxmax, rymax, true, true);
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
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossings.
	 */
	public static int computeCrossingsFromRect(PathIterator2f pi,
			double rxmin, double rymin,
			double rxmax, double rymax,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {
		// Copied from AWT API
		if (rxmax <= rxmin || rymax <= rymin) return 0;
		if (!pi.hasNext()) return 0;

		AbstractPathElement2F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path2f subPath;
		double curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		int crossings = 0;
		int n;

		while (crossings != MathConstants.SHAPE_INTERSECTS
				&& pi.hasNext()) {
			pathElement = pi.next();
			switch (pathElement.type) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				crossings = AbstractSegment2F.computeCrossingsFromRect(crossings,
						rxmin, rymin,
						rxmax, rymax,
						curx, cury,
						endx, endy);
				if (crossings==MathConstants.SHAPE_INTERSECTS)
					return crossings;
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						endx, endy);
				n = computeCrossingsFromRect(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin,
						rxmax, rymax,
						false,
						false);
				if (n==MathConstants.SHAPE_INTERSECTS)
					return n;
				crossings += n;
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(),
						endx, endy);
				n = computeCrossingsFromRect(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin,
						rxmax, rymax,
						false,
						false);
				if (n==MathConstants.SHAPE_INTERSECTS)
					return n;
				crossings += n;
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					crossings = AbstractSegment2F.computeCrossingsFromRect(crossings,
							rxmin, rymin,
							rxmax, rymax,
							curx, cury,
							movx, movy);
				}
				// Stop as soon as possible
				if (crossings!=0) return crossings;
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(crossings != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				// Not closed
				crossings = AbstractSegment2F.computeCrossingsFromRect(crossings,
						rxmin, rymin,
						rxmax, rymax,
						curx, cury,
						movx, movy);
			}
			else if (onlyIntersectWhenOpen) {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				crossings = 0;
			}
		}

		return crossings;
	}

	/** Array of types.
	 */
	PathElementType[] types;

	/** Array of coords.
	 */
	double[] coords;

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
	private SoftReference<Rectangle2f> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<Rectangle2f> logicalBounds = null;

	/**
	 */
	public Path2f() {
		this(PathWindingRule.NON_ZERO);
	}

	/**
	 * @param iterator
	 */
	public Path2f(Iterator<AbstractPathElement2F> iterator) {
		this(PathWindingRule.NON_ZERO, iterator);
	}

	/**
	 * @param windingRule1
	 */
	public Path2f(PathWindingRule windingRule1) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule1;
	}

	/**
	 * @param windingRule1
	 * @param iterator
	 */
	public Path2f(PathWindingRule windingRule1, Iterator<AbstractPathElement2F> iterator) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule1;
		add(iterator);
	}

	/**
	 * @param p
	 */
	public Path2f(Path2f p) {
		this.coords = p.coords.clone();
		this.isEmpty = p.isEmpty;
		this.isPolyline = p.isPolyline;
		this.numCoords = p.numCoords;
		this.numTypes = p.numTypes;
		this.types = p.types.clone();
		this.windingRule = p.windingRule;
		Rectangle2f box;
		box = p.graphicalBounds==null ? null : p.graphicalBounds.get();
		if (box!=null) {
			this.graphicalBounds = new SoftReference<>(box.clone());
		}
		box = p.logicalBounds==null ? null : p.logicalBounds.get();
		if (box!=null) {
			this.logicalBounds = new SoftReference<>(box.clone());
		}
	}

	/**
	 * @param p
	 */
	public Path2f(Path2d p) {
		this.coords = new double[p.coordsProperty.length];
		for(int i=0;i<p.numCoordsProperty.get();i++) {
			this.coords[i] = p.coordsProperty[i].get();
		}
		this.isEmpty = Boolean.valueOf(p.isEmpty());
		this.isPolyline = Boolean.valueOf(p.isPolyline());
		this.numCoords = p.numCoordsProperty.get();
		this.numTypes = p.numTypesProperty.get();
		this.types = p.types.clone();
		this.windingRule = p.windingRule;
		Rectangle2f box;
		box = new Rectangle2f(p.toBoundingBox());
		
		this.graphicalBounds = new SoftReference<>(box.clone());
		
		box = new Rectangle2f(p.toBoundingBoxWithCtrlPoints());

		this.logicalBounds = new SoftReference<>(box.clone());

	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoords = 0;
		this.numTypes = 0;
		this.isEmpty = Boolean.TRUE;
		this.isPolyline = Boolean.TRUE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Pure
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

	@Pure
	@Override
	public Path2f clone() {
		Path2f clone = super.clone();
		clone.coords = this.coords.clone();
		clone.types = this.types.clone();
		clone.windingRule = this.windingRule;
		return clone;
	}


	@Pure
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
	public void add(Iterator<AbstractPathElement2F> iterator) {
		AbstractPathElement2F element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.type) {
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
	public void moveTo(double x, double y) {
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
	public void lineTo(double x, double y) {
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
	public void quadTo(double x1, double y1, double x2, double y2) {
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
	public void curveTo(double x1, double y1,
			double x2, double y2,
			double x3, double y3) {
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



	/** Transform the current path.
	 * This function changes the current path.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape(Transform2D)
	 */
	public void transform(Transform2D transform) {
		if (transform!=null) {
			Point2D p = new Point2f();
			for(int i=0; i<this.numCoords;) {
				p.set(this.coords[i], this.coords[i+1]);
				transform.transform(p);
				this.coords[i++] = p.getX();
				this.coords[i++] = p.getY();
			}
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void translate(double dx, double dy) {
		for(int i=0; i<this.numCoords;) {
			this.coords[i++] += dx;
			this.coords[i++] += dy;
		}
		Rectangle2f bb;
		bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
		bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform == null) {
			return new CopyPathIterator2f();
		}
		return new TransformPathIterator2f(transform);
	}

	@Pure
	@Override
	public PathIterator2d getPathIteratorProperty(Transform2D transform) {
		if (transform == null) {
			return new CopyPathIterator2d();
		}
		return new TransformPathIterator2d(transform);
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
	@Pure
	public PathIterator2f getPathIterator(Transform2D transform, double flatness) {
		return new FlatteningPathIterator(getWindingRule(), getPathIterator(transform), flatness, 10);
	}

	@Pure
	@Override
	public PathIterator2f getPathIterator(double flatness) {
		return new FlatteningPathIterator(getWindingRule(), getPathIterator(null), flatness, 10);
	}

	@Pure
	@Override
	public PathIterator2d getPathIteratorProperty(double flatness) {
		return new Path2d.FlatteningPathIterator(getWindingRule(), getPathIteratorProperty(null), flatness, 10);
	}

	@Pure
	@Override
	public PathIterator2i getPathIteratorDiscrete(double flatness) {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Shape2F createTransformedShape(Transform2D transform) {
		Path2f newPath = new Path2f(getWindingRule());
		PathIterator2f pi = getPathIterator();
		Point2f p = new Point2f();
		Point2f t1 = new Point2f();
		Point2f t2 = new Point2f();
		AbstractPathElement2F e;
		while (pi.hasNext()) {
			e = pi.next();
			switch(e.type) {
			case MOVE_TO:
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.moveTo(p.getX(), p.getY());
				break;
			case LINE_TO:
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.lineTo(p.getX(), p.getY());
				break;
			case QUAD_TO:
				t1.set(e.getCtrlX1(), e.getCtrlY1());
				transform.transform(t1);
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.quadTo(t1.getX(), t1.getY(), p.getX(), p.getY());
				break;
			case CURVE_TO:
				t1.set(e.getCtrlX1(), e.getCtrlY1());
				transform.transform(t1);
				t2.set(e.getCtrlX2(), e.getCtrlY2());
				transform.transform(t2);
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.curveTo(t1.getX(), t1.getY(), t2.getX(), t2.getY(), p.getX(), p.getY());
				break;
			case CLOSE:
				newPath.closePath();
				break;
			default:
			}
		}
		return newPath;
	}

	@Pure
	@Override
	public double distanceSquared(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	@Pure
	@Override
	public double distanceL1(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	@Pure
	@Override
	public double distanceLinf(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
	}

	@Pure
	@Override
	public boolean contains(double x, double y) {
		return contains(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), x, y);
	}

	@Pure
	@Override
	public boolean contains(AbstractRectangle2F<?> r) {
		return contains(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	@Pure
	@Override
	public boolean intersects(AbstractRectangle2F<?> s) {
		// Copied from AWT API
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getMinX(), s.getMinY(), s.getMaxX(), s.getMaxY(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(AbstractEllipse2F<?> s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromEllipse(
				0,
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getMinX(), s.getMinY(), s.getWidth(), s.getHeight(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(AbstractCircle2F<?> s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromCircle(
				0,
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getX(), s.getY(), s.getRadius(),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(AbstractSegment2F<?> s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromSegment(
				0,
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getX1(), s.getY1(), s.getX2(), s.getY2(),
				false);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(Path2f s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				new PathShadow2f(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(Path2d s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				s.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
				new PathShadow2d(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(PathIterator2f s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				s,
				new PathShadow2f(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(PathIterator2d s) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				s,
				new PathShadow2d(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(AbstractOrientedRectangle2F<?> s) {
		return s.intersects(this);
	}


	/**
	 * Accumulate the number of times the path crosses the shadow
	 * extending to the right of the second path.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details.
	 * The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path,
	 * or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the rectangle.
	 * The path must start with a SEG_MOVETO, otherwise an exception is
	 * thrown.
	 * The caller must check r[xy]{min,max} for NaN values.
	 * 
	 * @param iterator1 is the iterator on the path elements.
	 * @param shadow is the description of the shape to project to the right.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	public static int computeCrossingsFromPath(
			PathIterator2f iterator1, 
			PathShadow2f shadow,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {
		if (!iterator1.hasNext()) return 0;

		AbstractPathElement2F pathElement1 = iterator1.next();

		if (pathElement1.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in the first path definition"); //$NON-NLS-1$
		}

		Path2f subPath;
		double curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement1.getToX();
		cury = movy = pathElement1.getToY();
		int crossings = 0;
		int n;

		while (crossings != MathConstants.SHAPE_INTERSECTS
				&& iterator1.hasNext()) {
			pathElement1 = iterator1.next();
			switch (pathElement1.type) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				movx = curx = pathElement1.getToX();
				movy = cury = pathElement1.getToY();
				break;
			case LINE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				crossings = shadow.computeCrossings(crossings,
						curx, cury,
						endx, endy);
				if (crossings==MathConstants.SHAPE_INTERSECTS)
					return crossings;
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(),
						endx, endy);
				n = computeCrossingsFromPath(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						false,
						false);
				if (n==MathConstants.SHAPE_INTERSECTS)
					return n;
				crossings += n;
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(),
						pathElement1.getCtrlX2(), pathElement1.getCtrlY2(),
						endx, endy);
				n = computeCrossingsFromPath(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						false,
						false);
				if (n==MathConstants.SHAPE_INTERSECTS)
					return n;
				crossings += n;
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					crossings = shadow.computeCrossings(crossings,
							curx, cury,
							movx, movy);
				}
				// Stop as soon as possible
				if (crossings!=0) return crossings;
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(crossings != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				// Not closed
				crossings = shadow.computeCrossings(crossings,
						curx, cury,
						movx, movy);
			}
			else if (onlyIntersectWhenOpen) {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				crossings = 0;
			}
		}

		return crossings;
	}

	/**
	 * Accumulate the number of times the path crosses the shadow
	 * extending to the right of the second path.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details.
	 * The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path,
	 * or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the rectangle.
	 * The path must start with a SEG_MOVETO, otherwise an exception is
	 * thrown.
	 * The caller must check r[xy]{min,max} for NaN values.
	 * 
	 * @param iterator1 is the iterator on the path elements.
	 * @param shadow is the description of the shape to project to the right.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	public static int computeCrossingsFromPath(
			PathIterator2d iterator1, 
			PathShadow2d shadow,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {
		if (!iterator1.hasNext()) return 0;

		AbstractPathElement2D pathElement1 = iterator1.next();

		if (pathElement1.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in the first path definition"); //$NON-NLS-1$
		}

		Path2d subPath;
		double curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement1.getToX();
		cury = movy = pathElement1.getToY();
		int crossings = 0;
		int n;

		while (crossings != MathConstants.SHAPE_INTERSECTS
				&& iterator1.hasNext()) {
			pathElement1 = iterator1.next();
			switch (pathElement1.type) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				movx = curx = pathElement1.getToX();
				movy = cury = pathElement1.getToY();
				break;
			case LINE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				crossings = shadow.computeCrossings(crossings,
						curx, cury,
						endx, endy);
				if (crossings==MathConstants.SHAPE_INTERSECTS)
					return crossings;
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				subPath = new Path2d();
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(),
						endx, endy);
				n = computeCrossingsFromPath(
						subPath.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						false,
						false);
				if (n==MathConstants.SHAPE_INTERSECTS)
					return n;
				crossings += n;
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				subPath = new Path2d();
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(),
						pathElement1.getCtrlX2(), pathElement1.getCtrlY2(),
						endx, endy);
				n = computeCrossingsFromPath(
						subPath.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						false,
						false);
				if (n==MathConstants.SHAPE_INTERSECTS)
					return n;
				crossings += n;
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					crossings = shadow.computeCrossings(crossings,
							curx, cury,
							movx, movy);
				}
				// Stop as soon as possible
				if (crossings!=0) return crossings;
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(crossings != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				// Not closed
				crossings = shadow.computeCrossings(crossings,
						curx, cury,
						movx, movy);
			}
			else if (onlyIntersectWhenOpen) {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				crossings = 0;
			}
		}

		return crossings;
	}

	private static boolean buildGraphicalBoundingBox(PathIterator2f iterator, Rectangle2f box) {
		boolean foundOneLine = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		AbstractPathElement2F element;
		Path2f subPath;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.type) {
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
				subPath = new Path2f();
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						element.getToX(), element.getToY());
				if (buildGraphicalBoundingBox(
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
				subPath = new Path2f();
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getToX(), element.getToY());
				if (buildGraphicalBoundingBox(
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
		if (foundOneLine) {
			box.setFromCorners(xmin, ymin, xmax, ymax);
		}
		else {
			box.clear();
		}
		return foundOneLine;
	}

	private boolean buildLogicalBoundingBox(Rectangle2f box) {
		if (this.numCoords>0) {
			double xmin = Double.POSITIVE_INFINITY;
			double ymin = Double.POSITIVE_INFINITY;
			double xmax = Double.NEGATIVE_INFINITY;
			double ymax = Double.NEGATIVE_INFINITY;
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
	public Rectangle2f toBoundingBox() {
		Rectangle2f bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2f();
			buildGraphicalBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
	public Rectangle2f toBoundingBoxWithCtrlPoints() {
		Rectangle2f bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2f();
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
	 * @see #toBoundingBoxWithCtrlPoints(Rectangle2f)
	 */
	@Override
	public void toBoundingBox(AbstractRectangle2F<?> box) {
		Rectangle2f bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2f();
			buildGraphicalBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
	public void toBoundingBoxWithCtrlPoints(Rectangle2f box) {
		Rectangle2f bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2f();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Pure
	@Override
	public Point2D getClosestPointTo(Point2D p) {
		return getClosestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY());
	}

	@Pure
	@Override
	public Point2D getFarthestPointTo(Point2D p) {
		return getFarthestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY());
	}


	@Pure
	public boolean equals(Path2f path) {
		return (this.numCoords==path.numCoords
				&& this.numTypes==path.numTypes
				&& Arrays.equals(this.coords, path.coords)
				&& Arrays.equals(this.types, path.types)
				&& this.windingRule==path.windingRule);
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Path2f) {
			Path2f p = (Path2f)obj;
			return (this.numCoords==p.numCoords
					&&this.numTypes==p.numTypes
					&&Arrays.equals(this.coords, p.coords)
					&&Arrays.equals(this.types, p.types)
					&&this.windingRule==p.windingRule);
		}
		return false;
	}

	@Pure
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
	 * single precision floating-point numbers.
	 * 
	 * @return the coordinates.
	 */
	@Pure
	public final double[] toFloatArray() {
		return toFloatArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * single precision floating-point numbers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	@Pure
	public double[] toFloatArray(Transform2D transform) {
		if (transform==null) {
			return Arrays.copyOf(this.coords, this.numCoords);
		}
		Point2f p = new Point2f();
		double[] clone = new double[this.numCoords];
		for(int i=0; i<clone.length;) {
			p.x = this.coords[i];
			p.y = this.coords[i+1];
			transform.transform(p);
			clone[i++] = p.x;
			clone[i++] = p.y;
		}
		return clone;
	}

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @return the coordinates.
	 */
	@Pure
	public final double[] toDoubleArray() {
		return toDoubleArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	@Pure
	public double[] toDoubleArray(Transform2D transform) {
		double[] clone = new double[this.numCoords];
		if (transform==null) {
			for(int i=0; i<this.numCoords; ++i) {
				clone[i] = this.coords[i];
			}
		}
		else {
			Point2f p = new Point2f();
			for(int i=0; i<clone.length;) {
				p.x = this.coords[i];
				p.y = this.coords[i+1];
				transform.transform(p);
				clone[i++] = p.x;
				clone[i++] = p.y;
			}
		}
		return clone;
	}

	/** Replies the points of this path in an array.
	 * 
	 * @return the points.
	 */
	@Pure
	public final Point2D[] toPointArray() {
		return toPointArray(null);
	}

	/** Replies the points of this path in an array.
	 * 
	 * @param transform is the transformation to apply to all the points.
	 * @return the points.
	 */
	@Pure
	public Point2D[] toPointArray(Transform2D transform) {
		Point2D[] clone = new Point2D[this.numCoords/2];
		if (transform==null) {
			for(int i=0, j=0; j<this.numCoords; ++i) {
				clone[i] = new Point2f(
						this.coords[j++],
						this.coords[j++]);
			}
		}
		else {
			for(int i=0, j=0; j<clone.length; ++i) {
				clone[i] = new Point2f(
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
	@Pure
	public final Collection<Point2D> toCollection() {
		PointCollection pC = new PointCollection();
		Point2D[] array = this.toPointArray();
		for(Point2D p : array) {
			pC.add(p);
		}
		return pC;
	}

	/** Replies the coordinate at the given index.
	 * The index is in [0;{@link #size()}*2).
	 *
	 * @param index
	 * @return the coordinate at the given index.
	 */
	@Pure
	public double getCoordAt(int index) {
		return this.coords[index];
	}

	/** Replies the point at the given index.
	 * The index is in [0;{@link #size()}).
	 *
	 * @param index
	 * @return the point at the given index.
	 */
	@Pure
	public Point2f getPointAt(int index) {
		return new Point2f(
				this.coords[index*2],
				this.coords[index*2+1]);
	}

	/** Replies the last point in the path.
	 *
	 * @return the last point.
	 */
	@Pure
	public Point2f getCurrentPoint() {
		return new Point2f(
				this.coords[this.numCoords-2],
				this.coords[this.numCoords-1]);
	}

	/** Replies the number of points in the path.
	 *
	 * @return the number of points in the path.
	 */
	@Pure
	public int size() {
		return this.numCoords/2;
	}

	/** Replies the total lentgh of the path.
	 *
	 * @return the length of the path.
	 */
	public double length() {
		
		if (this.isEmpty()) return 0;
		
		double length = 0;
		
		PathIterator2f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		
		AbstractPathElement2F pathElement = pi.next();
		
		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}
		
		Path2f subPath;
		double curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		
		while (pi.hasNext()) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();

				length += FunctionalPoint2D.distancePointPoint(
						curx, cury,  
						endx, endy);

				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						endx, endy);

				length += subPath.length();

				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = new Path2f();
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(),
						endx, endy);

				length += subPath.length();

				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					length += FunctionalPoint2D.distancePointPoint(
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
			PathIterator2f pi = getPathIterator();
			AbstractPathElement2F pe;
			while (this.isEmpty==Boolean.TRUE && pi.hasNext()) {
				pe = pi.next();
				if (pe.isDrawable()) { 
					this.isEmpty = Boolean.FALSE;
				}
			}
		}
		return this.isEmpty.booleanValue();
	}

	@Override
	public boolean isPolyline() {
		if (this.isPolyline==null) {
			this.isPolyline = Boolean.TRUE;
			PathIterator2f pi = getPathIterator();
			AbstractPathElement2F pe;
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
	/** Replies if the given points exists in the coordinates of this path.
	 * 
	 * @param p
	 * @return <code>true</code> if the point is a control point of the path.
	 */
	@Pure
	boolean containsControlPoint(Point2D p) {
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
	boolean remove(double x, double y) {
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
	public void setLastPoint(double x, double y) {
		if (this.numCoords>=2) {
			this.coords[this.numCoords-2] = x;
			this.coords[this.numCoords-1] = y;
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	@Override
	public void set(Shape2F s) {
		clear();
		add(s.getPathIterator());
	}

	/** A path iterator that does not transform the coordinates.
	 *
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class CopyPathIterator2f implements PathIterator2f {

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		private int iType = 0;
		private int iCoord = 0;
		private double movex, movey;

		/**
		 */
		public CopyPathIterator2f() {
			//
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType<Path2f.this.numTypes;
		}

		@Override
		public AbstractPathElement2F next() {
			int type = this.iType;
			if (this.iType>=Path2f.this.numTypes) {
				throw new NoSuchElementException();
			}
			AbstractPathElement2F element = null;
			switch(Path2f.this.types[type]) {
			case MOVE_TO:
				if (this.iCoord+2>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.movex = Path2f.this.coords[this.iCoord++];
				this.movey = Path2f.this.coords[this.iCoord++];
				this.p2.set(this.movex, this.movey);
				element = new AbstractPathElement2F.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				if (this.iCoord+2>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				element = new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
			{
				if (this.iCoord+4>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx = Path2f.this.coords[this.iCoord++];
				double ctrly = Path2f.this.coords[this.iCoord++];
				this.p2.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				element = new AbstractPathElement2F.QuadPathElement2f(
						this.p1.getX(), this.p1.getY(),
						ctrlx, ctrly,
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CURVE_TO:
			{
				if (this.iCoord+6>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx1 = Path2f.this.coords[this.iCoord++];
				double ctrly1 = Path2f.this.coords[this.iCoord++];
				double ctrlx2 = Path2f.this.coords[this.iCoord++];
				double ctrly2 = Path2f.this.coords[this.iCoord++];
				this.p2.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				element = new AbstractPathElement2F.CurvePathElement2f(
						this.p1.getX(), this.p1.getY(),
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				element = new AbstractPathElement2F.ClosePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path2f.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path2f.this.isPolyline();
		}

	} // class CopyPathIterator2d

	/** A path iterator that does not transform the coordinates.
	 *
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class CopyPathIterator2d implements PathIterator2d {

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		private IntegerProperty iTypeProperty = new SimpleIntegerProperty(0);
		private IntegerProperty iCoordProperty = new SimpleIntegerProperty(0);
		private DoubleProperty movexProperty, moveyProperty;

		/**
		 */
		public CopyPathIterator2d() {
			this.movexProperty = new SimpleDoubleProperty();
			this.moveyProperty = new SimpleDoubleProperty();
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iTypeProperty.get()<Path2f.this.numTypes;
		}

		@Override
		public AbstractPathElement2D next() {
			int type = this.iTypeProperty.get();
			if (this.iTypeProperty.get()>=Path2f.this.numTypes) {
				throw new NoSuchElementException();
			}
			AbstractPathElement2D element = null;
			switch(Path2f.this.types[type]) {
			case MOVE_TO:
				if (this.iCoordProperty.get()+2>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.movexProperty.set(Path2f.this.coords[this.iCoordProperty.get()]);
				this.iCoordProperty.set(this.iCoordProperty.get()+1);
				
				this.moveyProperty.set(Path2f.this.coords[this.iCoordProperty.get()]);
				this.iCoordProperty.set(this.iCoordProperty.get()+1);
				
				this.p2.set(this.movexProperty.get(), this.movexProperty.get());
				element = new AbstractPathElement2D.MovePathElement2d(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				if (this.iCoordProperty.get()+2>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				element = new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
			{
				if (this.iCoordProperty.get()+4>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx = Path2f.this.coords[this.iCoordProperty.get()];
				double ctrly = Path2f.this.coords[this.iCoordProperty.get()+1];
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.p2.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				element = new AbstractPathElement2D.QuadPathElement2d(
						this.p1.getX(), this.p1.getY(),
						ctrlx, ctrly,
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CURVE_TO:
			{
				if (this.iCoordProperty.get()+6>Path2f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx1 = Path2f.this.coords[this.iCoordProperty.get()];
				double ctrly1 = Path2f.this.coords[this.iCoordProperty.get()+1];
				double ctrlx2 = Path2f.this.coords[this.iCoordProperty.get()+2];
				double ctrly2 = Path2f.this.coords[this.iCoordProperty.get()+3];
				this.iCoordProperty.set(this.iCoordProperty.get()+4);
				
				this.p2.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				element = new AbstractPathElement2D.CurvePathElement2d(
						this.p1.getX(), this.p1.getY(),
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movexProperty.get(), this.moveyProperty.get());
				element = new AbstractPathElement2D.ClosePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();

			this.iTypeProperty.set(this.iTypeProperty.get()+1);

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path2f.this.getWindingRule();
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return Path2f.this.isPolyline();
		}

	} // class CopyPathIterator2d

	/** A path iterator that transforms the coordinates.
	 *
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TransformPathIterator2f implements PathIterator2f {

		private final Transform2D transform;
		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		private final Point2D ptmp1 = new Point2f();
		private final Point2D ptmp2 = new Point2f();
		private int iType = 0;
		private int iCoord = 0;
		private double movex, movey;

		/**
		 * @param transform1
		 */
		public TransformPathIterator2f(Transform2D transform1) {
			assert(transform1!=null);
			this.transform = transform1;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType<Path2f.this.numTypes;
		}

		@Override
		public AbstractPathElement2F next() {
			if (this.iType>=Path2f.this.numTypes) {
				throw new NoSuchElementException();
			}
			AbstractPathElement2F element = null;
			switch(Path2f.this.types[this.iType++]) {
			case MOVE_TO:
				this.movex = Path2f.this.coords[this.iCoord++];
				this.movey = Path2f.this.coords[this.iCoord++];
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = new AbstractPathElement2F.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp1);
				this.p2.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new AbstractPathElement2F.QuadPathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp2);
				this.p2.set(
						Path2f.this.coords[this.iCoord++],
						Path2f.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new AbstractPathElement2F.CurvePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = new AbstractPathElement2F.ClosePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path2f.this.getWindingRule();
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return Path2f.this.isPolyline();
		}

	}  // class TransformPathIterator2f

	/** A path iterator that transforms the coordinates.
	 *
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TransformPathIterator2d implements PathIterator2d {

		private final Transform2D transform;
		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		private final Point2D ptmp1 = new Point2f();
		private final Point2D ptmp2 = new Point2f();
		private IntegerProperty iTypeProperty = new SimpleIntegerProperty(0);
		private IntegerProperty iCoordProperty = new SimpleIntegerProperty(0);
		private DoubleProperty movexProperty, moveyProperty;

		/**
		 * @param transform1
		 */
		public TransformPathIterator2d(Transform2D transform1) {
			assert(transform1!=null);
			this.transform = transform1;
			this.movexProperty = new SimpleDoubleProperty();
			this.moveyProperty = new SimpleDoubleProperty();
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iTypeProperty.get()<Path2f.this.numTypes;
		}

		@Override
		public AbstractPathElement2D next() {
			if (this.iTypeProperty.get()>=Path2f.this.numTypes) {
				throw new NoSuchElementException();
			}
			AbstractPathElement2D element = null;
			switch(Path2f.this.types[this.iTypeProperty.get()]) {
			case MOVE_TO:
				this.movexProperty.set(Path2f.this.coords[this.iCoordProperty.get()]);
				this.moveyProperty.set(Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.p2.set(this.movexProperty.get(), this.moveyProperty.get());
				this.transform.transform(this.p2);
				element = new AbstractPathElement2D.MovePathElement2d(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.transform.transform(this.p2);
				element = new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.transform.transform(this.ptmp1);
				this.p2.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.transform.transform(this.p2);
				element = new AbstractPathElement2D.QuadPathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.transform.transform(this.ptmp2);
				this.p2.set(
						Path2f.this.coords[this.iCoordProperty.get()],
						Path2f.this.coords[this.iCoordProperty.get()+1]);
				this.iCoordProperty.set(this.iCoordProperty.get()+2);
				
				this.transform.transform(this.p2);
				element = new AbstractPathElement2D.CurvePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movexProperty.get(), this.moveyProperty.get());
				this.transform.transform(this.p2);
				element = new AbstractPathElement2D.ClosePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();
			
			this.iTypeProperty.set(this.iTypeProperty.get()+1);
			
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path2f.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path2f.this.isPolyline();
		}

	}  // class TransformPathIterator2d



	/** A path iterator that is flattening the path.
	 * This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class FlatteningPathIterator implements PathIterator2f {

		/** Winding rule of the path.
		 */
		private final PathWindingRule windingRule;

		/** The source iterator.
		 */
		private final PathIterator2f pathIterator;

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
		private double lastNextX;

		/** The y of the last move segment replied by next.
		 */
		private double lastNextY;

		/**
		 * @param windingRule1 is the winding rule of the path.
		 * @param pathIterator1 is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 * control points and the flattened curve
		 * @param limit1 the maximum number of recursive subdivisions
		 * allowed for any curved segment
		 */
		public FlatteningPathIterator(PathWindingRule windingRule1, PathIterator2f pathIterator1, double flatness, int limit1) {
			assert(windingRule1!=null);
			assert(flatness>=0f);
			assert(limit1>=0);
			this.windingRule = windingRule1;
			this.pathIterator = pathIterator1;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit1;
			this.levels = new int[limit1 + 1];
			searchNext();
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
		@Pure
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
		@Pure
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

		private void searchNext() {
			int level;

			if (this.holdIndex >= this.holdEnd) {
				if (!this.pathIterator.hasNext()) {
					this.done = true;
					return;
				}
				AbstractPathElement2F pathElement = this.pathIterator.next();
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

		@Pure
		@Override
		public boolean hasNext() {
			return !this.done;
		}

		@Override
		public AbstractPathElement2F next() {
			if (this.done) {
				throw new NoSuchElementException("flattening iterator out of bounds"); //$NON-NLS-1$
			}

			AbstractPathElement2F element;
			PathElementType type = this.holdType;
			if (type!=PathElementType.CLOSE) {
				double x = this.hold[this.holdIndex + 0];
				double y = this.hold[this.holdIndex + 1];
				if (type == PathElementType.MOVE_TO) {
					element = new AbstractPathElement2F.MovePathElement2f(x, y);
				}
				else {
					element = new AbstractPathElement2F.LinePathElement2f(
							this.lastNextX, this.lastNextY,
							x, y);
				}
				this.lastNextX = x;
				this.lastNextY = y;
			}
			else {
				element = new AbstractPathElement2F.ClosePathElement2f(
						this.lastNextX, this.lastNextY,
						this.moveX, this.moveY);
				this.lastNextX = this.moveX;
				this.lastNextY = this.moveY;
			}

			searchNext();

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return this.windingRule;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return false; // Because the iterator flats the path, this is no curve inside.
		}

	} // class FlatteningPathIterator2f


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

		@Pure
		@Override
		public int size() {
			return Path2f.this.size();
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return Path2f.this.size()<=0;
		}

		@Pure
		@Override
		public boolean contains(Object o) {
			if (o instanceof Point2D) {
				return Path2f.this.containsControlPoint((Point2D)o);
			}
			return false;
		}

		@Pure
		@Override
		public Iterator<Point2D> iterator() {
			return new PointIterator();
		}

		@Pure
		@Override
		public Object[] toArray() {
			return Path2f.this.toPointArray();
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
				if (Path2f.this.size()==0) {
					Path2f.this.moveTo(e.getX(), e.getY());
				}
				else {
					Path2f.this.lineTo(e.getX(), e.getY());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point2D) {
				Point2D p = (Point2D)o;
				return Path2f.this.remove(p.getX(), p.getY());
			}
			return false;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> c) {
			for(Object obj : c) {
				if ((!(obj instanceof Point2D))
						||(!Path2f.this.containsControlPoint((Point2D)obj))) {
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
					if (Path2f.this.remove(pts.getX(), pts.getY())) {
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
			Path2f.this.clear();
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

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<Path2f.this.size();
		}

		@Override
		public Point2D next() {
			try {
				this.lastReplied = Path2f.this.getPointAt(this.index++);
				return this.lastReplied;
			}
			catch( Throwable e) {
				e.printStackTrace();throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			Point2D p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			Path2f.this.remove(p.getX(), p.getY());
		}

	}

	@Pure
	@Override
	public PathIterator2i getPathIteratorDiscrete() {
		return null;
	}


}
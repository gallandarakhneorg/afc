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

package org.arakhne.afc.math.geometry.d2.afp;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.arakhne.afc.math.geometry.d2.PathIterator2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.fp.Point2fp;
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
public interface Path2afp<
		ST extends Shape2afp<?, ?, IE, P, B>,
		IT extends Path2afp<?, ?, IE, P, B>,
		IE extends PathElement2afp,
		P extends Point2D,
		B extends Rectangle2afp<?, ?, IE, P, B>>
		extends Shape2afp<ST, IT, IE, P, B>, Path2D<ST, IT, PathIterator2afp<IE>, P, B> {

	/** Multiple of cubic & quad curve size.
	 */
	static final int GROW_SIZE = 24;

	/** The default flatening depth limit.
	 */
	static final int DEFAULT_FLATENING_LIMIT = 10;
	
	/** The default winding rule: {@link PathWindingRule#NON_ZERO}.
	 */
	static final PathWindingRule DEFAULT_WINDING_RULE = PathWindingRule.NON_ZERO;
	
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
	 * @param iterator is the iterator on the path elements.
	 * @param shadow is the description of the shape to project to the right.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	static int computeCrossingsFromPath(
			PathIterator2afp<?> iterator, 
			PathShadow2afp<?> shadow,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {
		if (!iterator.hasNext()) return 0;

		PathElement2afp pathElement1 = iterator.next();

		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in the first path definition"); //$NON-NLS-1$
		}

		GeomFactory2afp<?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?> subPath;
		double curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement1.getToX();
		cury = movy = pathElement1.getToY();
		int crossings = 0;
		int n;

		while (crossings != MathConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement1 = iterator.next();
			switch (pathElement1.getType()) {
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
				// only for local use.
				subPath = factory.newPath(iterator.getWindingRule());
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
				// only for local use
				subPath = factory.newPath(iterator.getWindingRule());
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
	static void getClosestPointTo(PathIterator2afp<? extends PathElement2afp> pi, double x, double y, Point2D result) {
		double bestDist = Double.POSITIVE_INFINITY;
		PathElement2afp pe;

		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int crossings = 0;

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
			{
				double factor =  Segment2afp.computeProjectedPointOnLine(
						x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				factor = MathUtil.clamp(factor, 0f, 1f);
				double vx = (pe.getToX() - pe.getFromX()) * factor;
				double vy = (pe.getToY() - pe.getFromY()) * factor;
				foundCandidate = true;
				candidateX = pe.getFromX() + vx;
				candidateY = pe.getFromY() + vy;
				crossings += Segment2afp.computeCrossingsFromPoint(
						x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				break;
			}
			case CLOSE:
				crossings += Segment2afp.computeCrossingsFromPoint(
						x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				if ((crossings & mask) != 0) {
					result.set(x, y);
					return;
				}

				if (!pe.isEmpty()) {
					double factor =  Segment2afp.computeProjectedPointOnLine(
							x, y,
							pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
					factor = MathUtil.clamp(factor, 0f, 1f);
					double vx = (pe.getToX() - pe.getFromX()) * factor;
					double vy = (pe.getToY() - pe.getFromY()) * factor;
					foundCandidate = true;
					candidateX = pe.getFromX() + vx;
					candidateY = pe.getFromY() + vy;
				}
				crossings = 0;
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException(
						pe.getType() == null ? null : pe.getType().toString());
			}

			if (foundCandidate) {
				double d = Point2D.getDistanceSquaredPointPoint(x, y, candidateX, candidateY);
				if (d < bestDist) {
					bestDist = d;
					result.set(candidateX, candidateY);
				}
			}
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
	static void getFarthestPointTo(PathIterator2afp<? extends PathElement2afp> pi, double x, double y, Point2D result) {
		double bestX = Double.NaN;
		double bestY = Double.NaN;
		double bestDist = Double.NEGATIVE_INFINITY;
		PathElement2afp pe;
		// Only for internal use.
		Point2D point = new Point2fp();

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
				Segment2afp.computeFarthestPointTo(
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

	/**
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator2afp}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2afp} interface to implement support for the
	 * {@link Shape2afp#contains(double, double)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @return {@code true} if the specified coordinates are inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise
	 */
	static boolean contains(PathIterator2afp<? extends PathElement2afp> pi, double x, double y) {
		// Copied from the AWT API
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int cross = computeCrossingsFromPoint(pi, x, y, false, true);
		return ((cross & mask) != 0);
	}

	/**
	 * Tests if the specified rectangle is inside the closed
	 * boundary of the specified {@link PathIterator2afp}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2afp} interface to implement support for the
	 * {@link Shape2afp#contains(Rectangle2afp)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	static boolean contains(PathIterator2afp<? extends PathElement2afp> pi, double rx, double ry, double rwidth, double rheight) {
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
	 * Tests if the interior of the specified {@link PathIterator2afp}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape2afp} interface to implement support for the
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
	static boolean intersects(PathIterator2afp<? extends PathElement2afp> pi, double x, double y, double w, double h) {
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
	static int computeCrossingsFromPoint(PathIterator2afp<? extends PathElement2afp> pi, double px, double py) {
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
	 * @param iterator is the description of the path.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossing
	 */
	static int computeCrossingsFromPoint(
			PathIterator2afp<? extends PathElement2afp> iterator,
			double px, double py,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {	
		// Copied from the AWT API
		if (!iterator.hasNext()) return 0;
		PathElement2afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory2afp<?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?> subPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int r, crossings = 0;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				if (endx==px && endy==py)
					return MathConstants.SHAPE_INTERSECTS;
				crossings += Segment2afp.computeCrossingsFromPoint(
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
				// For internal use only
				subPath = factory.newPath(iterator.getWindingRule());
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
				// For internal use only
				subPath = factory.newPath(iterator.getWindingRule());
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
					crossings += Segment2afp.computeCrossingsFromPoint(
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
				crossings += Segment2afp.computeCrossingsFromPoint(
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
	static int computeCrossingsFromEllipse(PathIterator2afp<? extends PathElement2afp> pi, double ex, double ey, double ew, double eh) {
		return computeCrossingsFromEllipse(0, pi, ex, ey, ew, eh, true, true);
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given ellipse extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param ex is the first point of the ellipse.
	 * @param ey is the first point of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossing or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	static int computeCrossingsFromEllipse(
			int crossings, 
			PathIterator2afp<? extends PathElement2afp> iterator, 
			double ex, double ey, double ew, double eh, 
			boolean closeable,
			boolean onlyIntersectWhenOpen) {	
		// Copied from the AWT API
		if (!iterator.hasNext()) return 0;
		PathElement2afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory2afp<?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = Segment2afp.computeCrossingsFromEllipse(
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
				localPath = factory.newPath(iterator.getWindingRule());
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
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
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
					numCrosses = Segment2afp.computeCrossingsFromEllipse(
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
				numCrosses = Segment2afp.computeCrossingsFromEllipse(
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
	static int computeCrossingsFromCircle(PathIterator2afp<? extends PathElement2afp> pi, double cx, double cy, double radius) {
		return computeCrossingsFromCircle(0, pi, cx, cy, radius, true, true);
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossing
	 */
	static int computeCrossingsFromCircle(
			int crossings, 
			PathIterator2afp<? extends PathElement2afp> iterator,
			double cx, double cy, double radius,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {	
		// Copied from the AWT API
		if (!iterator.hasNext()) return 0;
		PathElement2afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory2afp<?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = Segment2afp.computeCrossingsFromCircle(
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
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
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
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
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
					numCrosses = Segment2afp.computeCrossingsFromCircle(
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
				numCrosses = Segment2afp.computeCrossingsFromCircle(
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
	static int computeCrossingsFromSegment(PathIterator2afp<? extends PathElement2afp> pi, double x1, double y1, double x2, double y2) {
		return computeCrossingsFromSegment(0, pi, x1, y1, x2, y2, true);
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given segment extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @return the crossing
	 */
	static int computeCrossingsFromSegment(int crossings, PathIterator2afp<? extends PathElement2afp> iterator, double x1, double y1, double x2, double y2, boolean closeable) {	
		// Copied from the AWT API
		if (!iterator.hasNext() || crossings==MathConstants.SHAPE_INTERSECTS) return crossings;
		PathElement2afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory2afp<?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = Segment2afp.computeCrossingsFromSegment(
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
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
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
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
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
					numCrosses = Segment2afp.computeCrossingsFromSegment(
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
				numCrosses = Segment2afp.computeCrossingsFromSegment(
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
	static int computeCrossingsFromRect(PathIterator2afp<? extends PathElement2afp> pi,
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
	 * @param iterator is the iterator on the path elements.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param closeable indicates if the shape is automatically closed or not.
	 * @param onlyIntersectWhenOpen indicates if the crossings is set to 0 when
	 * the path is open and there is not SHAPE_INTERSECT.
	 * @return the crossings.
	 */
	static int computeCrossingsFromRect(PathIterator2afp<? extends PathElement2afp> iterator,
			double rxmin, double rymin,
			double rxmax, double rymax,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {
		// Copied from AWT API
		if (rxmax <= rxmin || rymax <= rymin) return 0;
		if (!iterator.hasNext()) return 0;

		PathElement2afp pathElement = iterator.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory2afp<?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?> localPath;
		double curx, cury, movx, movy, endx, endy;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		int crossings = 0;
		int n;

		while (crossings != MathConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement = iterator.next();
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
				crossings = Segment2afp.computeCrossingsFromRect(crossings,
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
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						endx, endy);
				n = computeCrossingsFromRect(
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(),
						endx, endy);
				n = computeCrossingsFromRect(
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
					crossings = Segment2afp.computeCrossingsFromRect(crossings,
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
				crossings = Segment2afp.computeCrossingsFromRect(crossings,
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

	/** Compute the box that corresponds to the drawable elements of the path.
	 * 
	 * <p>An element is drawable if it is a line, a curve, or a closing path element.
	 * 
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a drawable element was found.
	 */
	static boolean computeDrawableElementBoundingBox(PathIterator2afp<?> iterator,
			Rectangle2afp<?, ?, ?, ?, ?> box) {
		GeomFactory2afp<?, ?, ?> factory = iterator.getGeomFactory();
		boolean foundOneLine = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		PathElement2afp element;
		Path2afp<?, ?, ?, ?, ?> subPath;
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

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}
		return equalsToPathIterator(shape.getPathIterator());
	}

	/** Add the elements replied by the iterator into this path.
	 * 
	 * @param iterator
	 */
	default void add(Iterator<? extends PathElement2afp> iterator) {
		PathElement2afp element;
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
	default void set(Path2afp<?, ?, ?, ?, ?> s) {
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
	void moveTo(double x, double y);

	@Override
	default void moveTo(Point2D position) {
		moveTo(position.getX(), position.getY());
	}

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	void lineTo(double x, double y);

	@Override
	default void lineTo(Point2D to) {
		lineTo(to.getX(), to.getY());
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
	void quadTo(double x1, double y1, double x2, double y2);

	@Override
	default void quadTo(Point2D ctrl, Point2D to) {
		quadTo(ctrl.getX(), ctrl.getY(), to.getX(), to.getY());
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
	void curveTo(double x1, double y1,
			double x2, double y2,
			double x3, double y3);

	@Override
	default void curveTo(Point2D ctrl1, Point2D ctrl2, Point2D to) {
		curveTo(ctrl1.getX(), ctrl1.getY(), ctrl2.getX(), ctrl2.getY(), to.getX(), to.getY());

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

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return contains(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), x, y);
	}

	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		return contains(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> s) {
		// Copied from AWT API
		if (s.isEmpty()) return false;
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getMinX(), s.getMinY(), s.getMaxX(), s.getMaxY(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		return s.intersects(this);
	}
	
	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
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
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
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
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
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
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		return s.intersects(this);
	}

	@Pure
	@Override
	default boolean intersects(Path2afp<?, ?, ?, ?, ?> s) {
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				new PathShadow2afp<>(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				iterator,
				new PathShadow2afp<>(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	/** Replies the coordinate at the given index.
	 * The index is in [0;{@link #size()}*2).
	 *
	 * @param index
	 * @return the coordinate at the given index.
	 */
	@Pure
	double getCoordAt(int index);

	/** Change the coordinates of the last inserted point.
	 * 
	 * @param x
	 * @param y
	 */
	void setLastPoint(double x, double y);
	
	@Override
	default void setLastPoint(Point2D point) {
		setLastPoint(point.getX(), point.getY());
	}

	/** Transform the current path.
	 * This function changes the current path.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform2D transform);

	@Override
	default double lengthSquared() {
		if (isEmpty()) return 0;
		
		double length = 0;
		
		PathIterator2afp<?> pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		
		PathElement2afp pathElement = pi.next();
		
		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}
		
		// only for internal use
		GeomFactory2afp<IE, P, B> factory = getGeomFactory();
		Path2afp<?, ?, ?, ?, ?> subPath;
		double curx, cury, movx, movy, endx, endy;
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

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform == null) {
			return new PathPathIterator<>(this);
		}
		return new TransformedPathPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(double flatness) {
		return new FlatteningPathIterator<>(getPathIterator(null), flatness, DEFAULT_FLATENING_LIMIT);
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
	default PathIterator2afp<IE> getPathIterator(Transform2D transform, double flatness) {
		return new FlatteningPathIterator<>(getPathIterator(transform), flatness, DEFAULT_FLATENING_LIMIT);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D p) {
		P point = getGeomFactory().newPoint();
		Path2afp.getClosestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D p) {
		P point = getGeomFactory().newPoint();
		Path2afp.getFarthestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(),
				point);
		return point;
	}

	@Override
	default Collection<Point2D> toCollection() {
		PointCollection pC = new PointCollection(this);
		Point2D[] array = toPointArray();
		for(Point2D p : array) {
			pC.add(p);
		}
		return pC;
	}
	
	/** Remove the point with the given coordinates.
	 * 
	 * <p>If the given coordinates do not match exactly a point in the path, nothing is removed.
	 * 
	 * @param x the x coordinate of the ponit to remove.
	 * @param y the y coordinate of the ponit to remove.
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(double x, double y);

	/** Abstract iterator on the path elements of the path.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractPathPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {
				
		private final Path2afp<?, ?, T, ?, ?> path;

		/**
		 * @param path the iterated path.
		 */
		public AbstractPathPathIterator(Path2afp<?, ?, T, ?, ?> path) {
			this.path = path;
		}

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
		}

		/** Replies the path.
		 *
		 * @return the path.
		 */
		public Path2afp<?, ?, T, ?, ?> getPath() {
			return this.path;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
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
		public boolean isPolygon() {
			return this.path.isPolygon();
		}

		@Override
		public boolean isMultiParts() {
			return this.path.isMultiParts();
		}

	}

	/** A path iterator that does not transform the coordinates.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PathPathIterator<T extends PathElement2afp> extends AbstractPathPathIterator<T> {

		private Point2D p1;
		private Point2D p2;
		private int iType = 0;
		private int iCoord = 0;
		private double movex, movey;

		/**
		 * @param path the path to iterate on.
		 */
		public PathPathIterator(Path2afp<?, ?, T, ?, ?> path) {
			super(path);
			this.p1 = getGeomFactory().newPoint();
			this.p2 = getGeomFactory().newPoint();
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			Path2afp<?, ?, T, ?, ?> path = getPath();
			int type = this.iType;
			if (this.iType >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			T element = null;
			switch(path.getPathElementTypeAt(type)) {
			case MOVE_TO:
				if ((this.iCoord + 2) > (getPath().size() * 2)) {
					throw new NoSuchElementException();
				}
				this.movex = path.getCoordAt(this.iCoord++);
				this.movey = path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey);
				element = getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				if ((this.iCoord + 2) > (path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				element = getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
			{
				if ((this.iCoord + 4) > (path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx = path.getCoordAt(this.iCoord++);
				double ctrly = path.getCoordAt(this.iCoord++);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						ctrlx, ctrly,
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CURVE_TO:
			{
				if ((this.iCoord + 6) > (path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx1 = path.getCoordAt(this.iCoord++);
				double ctrly1 = path.getCoordAt(this.iCoord++);
				double ctrlx2 = path.getCoordAt(this.iCoord++);
				double ctrly2 = path.getCoordAt(this.iCoord++);
				this.p2.set(
						getPath().getCoordAt(this.iCoord++),
						getPath().getCoordAt(this.iCoord++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				element = getGeomFactory().newClosePathElement(
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

	}

	/** A path iterator that transforms the coordinates.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedPathPathIterator<T extends PathElement2afp> extends AbstractPathPathIterator<T> {

		private final Transform2D transform;
		
		private final Point2D p1;
		private final Point2D p2;
		private final Point2D ptmp1;
		private final Point2D ptmp2;
		
		private int iType = 0;
		
		private int iCoord = 0;
		
		private double movex;
		
		private double movey;

		/**
		 * @param path the path to iterate on.
		 * @param transform the transformation to apply on the path.
		 */
		public TransformedPathPathIterator(Path2afp<?, ?, T, ?, ?> path, Transform2D transform) {
			super(path);
			assert(transform!=null);
			this.transform = transform;
			this.p1 = getGeomFactory().newPoint();
			this.p2 = getGeomFactory().newPoint();
			this.ptmp1 = getGeomFactory().newPoint();
			this.ptmp2 = getGeomFactory().newPoint();
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			Path2afp<?, ?, T, ?, ?> path = getPath();
			if (this.iType >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			T element = null;
			switch(path.getPathElementTypeAt(this.iType++)) {
			case MOVE_TO:
				this.movex = path.getCoordAt(this.iCoord++);
				this.movey = path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.p2.getX(), this.p2.getY());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp2);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
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
				element = getGeomFactory().newClosePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			default:
			}
			if (element==null) {
				throw new NoSuchElementException();
			}
			return element;
		}

	}

	/** A path iterator that is flattening the path.
	 * This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class FlatteningPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		/** The source iterator.
		 */
		private final PathIterator2afp<T> pathIterator;

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
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 * control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions
		 * allowed for any curved segment
		 */
		public FlatteningPathIterator(PathIterator2afp<T> pathIterator, double flatness, int limit) {
			assert(flatness >= 0.);
			assert(limit >= 0);
			this.pathIterator = pathIterator;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit;
			this.levels = new int[limit + 1];
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
		private static double getQuadSquaredFlatness(double coords[], int offset) {
			return Segment2afp.getDistanceSquaredLinePoint(
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
			x1 = (x1 + ctrlx) / 2;
			y1 = (y1 + ctrly) / 2;
			x2 = (x2 + ctrlx) / 2;
			y2 = (y2 + ctrly) / 2;
			ctrlx = (x1 + x2) / 2;
			ctrly = (y1 + y2) / 2;
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
					Segment2afp.getDistanceSquaredSegmentPoint(
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 2],
							coords[offset + 3],
							coords[offset + 0],
							coords[offset + 1],
							null),
					Segment2afp.getDistanceSquaredSegmentPoint(
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
			x1 = (x1 + ctrlx1) / 2;
			y1 = (y1 + ctrly1) / 2;
			x2 = (x2 + ctrlx2) / 2;
			y2 = (y2 + ctrly2) / 2;
			double centerx = (ctrlx1 + ctrlx2) / 2;
			double centery = (ctrly1 + ctrly2) / 2;
			ctrlx1 = (x1 + centerx) / 2;
			ctrly1 = (y1 + centery) / 2;
			ctrlx2 = (x2 + centerx) / 2;
			ctrly2 = (y2 + centery) / 2;
			centerx = (ctrlx1 + ctrlx2) / 2;
			centery = (ctrly1 + ctrly2) / 2;
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
				T pathElement = this.pathIterator.next();
				this.holdType = pathElement.getType();
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
		public T next() {
			if (this.done) {
				throw new NoSuchElementException("flattening iterator out of bounds"); //$NON-NLS-1$
			}

			T element;
			PathElementType type = this.holdType;
			if (type!=PathElementType.CLOSE) {
				double x = this.hold[this.holdIndex + 0];
				double y = this.hold[this.holdIndex + 1];
				if (type == PathElementType.MOVE_TO) {
					element = getGeomFactory().newMovePathElement(x, y);
				}
				else {
					element = getGeomFactory().newLinePathElement(
							this.lastNextX, this.lastNextY,
							x, y);
				}
				this.lastNextX = x;
				this.lastNextY = y;
			}
			else {
				element = getGeomFactory().newClosePathElement(
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
			return this.pathIterator.getWindingRule();
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return this.pathIterator.isPolyline();
		}

		@Pure
		@Override
		public boolean isCurved() {
			// Because the iterator flats the path, this is no curve inside.
			return false;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return this.pathIterator.isPolygon();
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return this.pathIterator.isMultiParts();
		}

		@Pure
		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.pathIterator.getGeomFactory();
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

		private final Path2afp<?, ?, ?, ?, ?> path;
		
		/**
		 * @param path the path to iterate on.
		 */
		public PointCollection(Path2afp<?, ?, ?, ?, ?> path) {
			this.path = path;
		}

		@Pure
		@Override
		public int size() {
			return this.path.size();
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return this.path.size() <= 0;
		}

		@Pure
		@Override
		public boolean contains(Object o) {
			if (o instanceof Point2D) {
				return this.path.containsControlPoint((Point2D)o);
			}
			return false;
		}

		@Pure
		@Override
		public Iterator<Point2D> iterator() {
			return new PointIterator(this.path);
		}

		@Pure
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
					this.path.moveTo(e.getX(), e.getY());
				}
				else {
					this.path.lineTo(e.getX(), e.getY());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point2D) {
				Point2D p = (Point2D)o;
				return this.path.remove(p.getX(), p.getY());
			}
			return false;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> c) {
			for(Object obj : c) {
				if ((!(obj instanceof Point2D))
						||(!this.path.containsControlPoint((Point2D)obj))) {
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
					if (this.path.remove(pts.getX(), pts.getY())) {
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

		private final Path2afp<?, ?, ?, ?, ?> path;
		
		private int index = 0;

		private Point2D lastReplied = null;

		/**
		 * @param path the path to iterate on.
		 */
		public PointIterator(Path2afp<?, ?, ?, ?, ?> path) {
			this.path = path;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < this.path.size();
		}

		@Override
		public Point2D next() {
			try {
				this.lastReplied = this.path.getPointAt(this.index++);
				return this.lastReplied;
			}
			catch(Throwable e) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			Point2D p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			this.path.remove(p.getX(), p.getY());
		}

	}


}

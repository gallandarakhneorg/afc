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

package org.arakhne.afc.math.geometry.d3.afp;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Path3D;
import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D path on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Path3afp<
		ST extends Shape3afp<?, ?, IE, P, V, B>,
		IT extends Path3afp<?, ?, IE, P, V, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
		extends Shape3afp<ST, IT, IE, P, V, B>, Path3D<ST, IT, PathIterator3afp<IE>, P, V, B> {

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
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the iterator on the path elements.
	 * @param shadow is the description of the shape to project to the right.
	 * @param type is the type of special computation to apply. If <code>null</code>, it 
	 * is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	static int computeCrossingsFromPath(
			int crossings,
			PathIterator3afp<?> iterator, 
			PathShadow3afp<?> shadow,
			CrossingComputationType type) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (shadow != null) : "Shadow to the right must be not null"; //$NON-NLS-1$

		if (!iterator.hasNext()) return 0;

		PathElement3afp pathElement1 = iterator.next();

		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in the first path definition"); //$NON-NLS-1$
		}

		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?> subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement1.getToX();
		cury = movy = pathElement1.getToY();
		curz = movz = pathElement1.getToZ();
		int numCrossings = crossings;

		while (numCrossings != MathConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement1 = iterator.next();
			switch (pathElement1.getType()) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				movx = curx = pathElement1.getToX();
				movy = cury = pathElement1.getToY();
				movz = curz = pathElement1.getToZ();
				break;
			case LINE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				numCrossings = shadow.computeCrossings(numCrossings,
						curx, cury, curz,
						endx, endy, endz);
				if (numCrossings==MathConstants.SHAPE_INTERSECTS)
					return numCrossings;
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				// only for local use.
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.quadTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(), pathElement1.getCtrlZ1(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPath(
						numCrossings,
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				// only for local use
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.curveTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(), pathElement1.getCtrlZ1(),
						pathElement1.getCtrlX2(), pathElement1.getCtrlY2(), pathElement1.getCtrlZ2(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPath(
						numCrossings,
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					numCrossings = shadow.computeCrossings(numCrossings,
							curx, cury, curz,
							movx, movy, movz);
				}
				// Stop as soon as possible
				if (numCrossings!=0) return numCrossings;
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}
		}

		assert(numCrossings != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = shadow.computeCrossings(numCrossings,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrossings = 0;
				break;
			case STANDARD:
			default:
				break;
			}
		}

		return numCrossings;
	}

	/** Replies the point on the path that is closest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of <var>pi</var> is replying
	 * <code>false</code>.
	 * {@link #getClosestPointTo(Point3D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param z
	 * @param result the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	static void getClosestPointTo(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z, Point3D<?, ?> result) {
		assert (pi != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (!pi.isCurved()) : "The path iterator is not iterating on a polyline path"; //$NON-NLS-1$
		assert (result != null) : "Result point must be not null"; //$NON-NLS-1$

		double bestDist = Double.POSITIVE_INFINITY;
		PathElement3afp pe;

		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int crossings = 0;

		while (pi.hasNext()) {
			pe = pi.next();

			boolean foundCandidate = false;
			double candidateX = Double.NaN;
			double candidateY = Double.NaN;
			double candidateZ = Double.NaN;

			switch(pe.getType()) {
			case MOVE_TO:
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				candidateZ = pe.getToZ();
				break;
			case LINE_TO:
			{
				double factor =  Segment3afp.computeProjectedPointOnLine(
						x, y, z,
						pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
				factor = MathUtil.clamp(factor, 0, 1);
				double vx = (pe.getToX() - pe.getFromX()) * factor;
				double vy = (pe.getToY() - pe.getFromY()) * factor;
				double vz = (pe.getToZ() - pe.getFromZ()) * factor;
				foundCandidate = true;
				candidateX = pe.getFromX() + vx;
				candidateY = pe.getFromY() + vy;
				candidateZ = pe.getFromZ() + vz;
				crossings += Segment3afp.computeCrossingsFromPoint(
						x, y, z,
						pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
				break;
			}
			case CLOSE:
				crossings += Segment3afp.computeCrossingsFromPoint(
						x, y, z,
						pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
				if ((crossings & mask) != 0) {
					result.set(x, y, z);
					return;
				}

				if (!pe.isEmpty()) {
					double factor =  Segment3afp.computeProjectedPointOnLine(
							x, y, z,
							pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
					factor = MathUtil.clamp(factor, 0, 1);
					double vx = (pe.getToX() - pe.getFromX()) * factor;
					double vy = (pe.getToY() - pe.getFromY()) * factor;
					double vz = (pe.getToZ() - pe.getFromZ()) * factor;
					foundCandidate = true;
					candidateX = pe.getFromX() + vx;
					candidateY = pe.getFromY() + vy;
					candidateZ = pe.getFromY() + vz;
				}
				crossings = 0;
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException(pe.getType().toString());
			}

			if (foundCandidate) {
				double d = Point3D.getDistanceSquaredPointPoint(x, y, z, candidateX, candidateY, candidateZ);
				if (d < bestDist) {
					bestDist = d;
					result.set(candidateX, candidateY, candidateZ);
				}
			}
		}
	}

	/** Replies the point on the path that is farthest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of <var>pi</var> is replying
	 * <code>false</code>.
	 * {@link #getFarthestPointTo(Point3D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param z
	 * @param result the farthest point on the shape.
	 */
	static void getFarthestPointTo(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z, Point3D<?, ?> result) {
		assert (pi != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (!pi.isCurved()) : "The path iterator is not iterating on a polyline path"; //$NON-NLS-1$
		assert (result != null) : "Result point must be not null"; //$NON-NLS-1$

		double bestDist = Double.NEGATIVE_INFINITY;
		PathElement3afp pe;
		// Only for internal use.
		Point3D<?, ?> point = new InnerComputationPoint3afp();

		while (pi.hasNext()) {
			pe = pi.next();

			switch(pe.getType()) {
			case MOVE_TO:
				break;
			case LINE_TO:
			case CLOSE:
				Segment3afp.computeFarthestPointTo(
						pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ(),
						x, y, z, point);
				double d = Point3D.getDistanceSquaredPointPoint(x, y, z, point.getX(), point.getY(), point.getZ());
				if (d > bestDist) {
					bestDist = d;
					result.set(point.getX(), point.getY(), point.getZ());
				}
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException(pe.getType().toString());
			}
		}
	}

	/**
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator3afp}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape3afp} interface to implement support for the
	 * {@link Shape3afp#contains(double, double)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Z coordinate
	 * @return {@code true} if the specified coordinates are inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise
	 */
	static boolean containsPoint(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double  z) {
		assert (pi != null) : "Iterator must be not null"; //$NON-NLS-1$
		// Copied from the AWT API
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int cross = computeCrossingsFromPoint(0, pi, x, y, z, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return ((cross & mask) != 0);
	}

	/**
	 * Tests if the specified rectangle is inside the closed
	 * boundary of the specified {@link PathIterator3afp}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape3afp} interface to implement support for the
	 * {@link Shape3afp#contains(RectangularPrism3afp)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rz the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @param rdepth is the depth of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	static boolean containsRectangle(PathIterator3afp<? extends PathElement3afp> pi, double rx, double ry, double rz, double rwidth, double rheight, double rdepth) {
		assert (pi != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (rwidth >= 0.) : "Rectangle width must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0.) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		assert (rdepth >= 0.) : "Rectangle depth must be positive or zero"; //$NON-NLS-1$
		// Copied from AWT API
		if (rwidth <= 0 || rheight <= 0 || rdepth <= 0) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(
				0,
				pi, 
				rx, ry, rz, rx+rwidth, ry+rheight, rz+rdepth,
				CrossingComputationType.AUTO_CLOSE);
		return (crossings != MathConstants.SHAPE_INTERSECTS &&
				(crossings & mask) != 0);
	}

	/**
	 * Tests if the interior of the specified {@link PathIterator3afp}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 *
	 * @param pi the specified {@link PathIterator3afp}.
	 * @param x the specified X coordinate of the rectangle.
	 * @param y the specified Y coordinate of the rectangle.
	 * @param z the specified Y coordinate of the rectangle.
	 * @param w the width of the specified rectangular coordinates.
	 * @param h the height of the specified rectangular coordinates.
	 * @param d the depth of the specified rectangular coordinates.
	 * @return <code>true</code> if the specified {@link PathIterator3afp} and
	 *         the interior of the specified set of rectangular
	 *         coordinates intersect each other; <code>false</code> otherwise.
	 */
	static boolean intersectsPathIteratorRectangle(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z, double w, double h, double d) {
		assert (pi != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (w >= 0.) : "Rectangle width must be positive or zero"; //$NON-NLS-1$
		assert (h >= 0.) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		assert (d >= 0.) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		if (w <= 0 || h <= 0 || d <= 0) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(0, pi, x, y, z, x+w, y+h, z+d,
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
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
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param type is the type of special computation to apply. If <code>null</code>, it 
	 * is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int computeCrossingsFromPoint(
			int crossings,
			PathIterator3afp<? extends PathElement3afp> iterator,
			double px, double py, double pz,
			CrossingComputationType type) {	
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		// Copied from the AWT API
		if (!iterator.hasNext()) return 0;
		PathElement3afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?> subPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double movz = element.getToZ();
		double curx = movx;
		double cury = movy;
		double curz = movz;
		double endx, endy, endz;
		int numCrossings = crossings;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				movz = curz = element.getToZ();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				if (endx==px && endy==py && endz==pz)
					return MathConstants.SHAPE_INTERSECTS;
				numCrossings += Segment3afp.computeCrossingsFromPoint(
						px, py, pz,
						curx, cury, curz,
						endx, endy, endz);
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				if (endx==px && endy==py && endz==pz)
					return MathConstants.SHAPE_INTERSECTS;
				// For internal use only
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, pz,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				if (endx == px && endy == py && endz == pz) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				// For internal use only
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, pz,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					if (movx==px && movy==py && movz==pz)
						return MathConstants.SHAPE_INTERSECTS;
					numCrossings += Segment3afp.computeCrossingsFromPoint(
							px, py, pz,
							curx, cury, curz,
							movx, movy, movz);
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}
		}

		assert(numCrossings != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				if (movx==px && movy==py && movz==pz)
					return MathConstants.SHAPE_INTERSECTS;
				numCrossings += Segment3afp.computeCrossingsFromPoint(
						px, py, pz,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrossings = 0;
				break;
			case STANDARD:
			default:
				break;
			}
		}

		return numCrossings;
	}

//	/**
//	 * Calculates the number of times the given path
//	 * crosses the given ellipse extending to the right.
//	 * 
//	 * @param crossings is the initial value for crossing.
//	 * @param iterator is the description of the path.
//	 * @param ex is the first point of the ellipse.
//	 * @param ey is the first point of the ellipse.
//	 * @param ez is the first point of the ellipse.
//	 * @param ew is the width of the ellipse.
//	 * @param eh is the height of the ellipse.
//	 * @param ed is the depth of the ellipse.
//	 * @param type is the type of special computation to apply. If <code>null</code>, it 
//	 * is equivalent to {@link CrossingComputationType#STANDARD}.
//	 * @return the crossing or {@link MathConstants#SHAPE_INTERSECTS}
//	 */
//	static int computeCrossingsFromEllipse(
//			int crossings, 
//			PathIterator3afp<? extends PathElement3afp> iterator, 
//			double ex, double ey, double ez, double ew, double eh, double ed, 
//			CrossingComputationType type) {	
//		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
//		assert (ew >= 0.) : "Ellipse width must be positive or zero"; //$NON-NLS-1$
//		assert (eh >= 0.) : "Ellipse height must be positive or zero"; //$NON-NLS-1$
//		assert (ed >= 0.) : "Ellipse depth must be positive or zero"; //$NON-NLS-1$
//		// Copied from the AWT API
//		if (!iterator.hasNext()) return 0;
//		PathElement3afp element;
//
//		element = iterator.next();
//		if (element.getType() != PathElementType.MOVE_TO) {
//			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
//		}
//
//		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
//		Path3afp<?, ?, ?, ?, ?, ?> localPath;
//		double movx = element.getToX();
//		double movy = element.getToY();
//		double movz = element.getToZ();
//		double curx = movx;
//		double cury = movy;
//		double curz = movz;
//		double endx, endy, endz;
//		int numCrosses = crossings;
//		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && iterator.hasNext()) {
//			element = iterator.next();
//			switch (element.getType()) {
//			case MOVE_TO:
//				movx = curx = element.getToX();
//				movy = cury = element.getToY();
//				movz = curz = element.getToZ();
//				break;
//			case LINE_TO:
//				endx = element.getToX();
//				endy = element.getToY();
//				endz = element.getToZ();
//				numCrosses = Segment3afp.computeCrossingsFromEllipse(
//						numCrosses,
//						ex, ey, ez, ew, eh, ed,
//						curx, cury, curz,
//						endx, endy, endz);
//				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
//					return numCrosses;
//				}
//				curx = endx;
//				cury = endy;
//				curz = endz;
//				break;
//			case QUAD_TO:
//			{
//				endx = element.getToX();
//				endy = element.getToY();
//				endz = element.getToZ();
//				localPath = factory.newPath(iterator.getWindingRule());
//				localPath.moveTo(curx, cury, curz);
//				localPath.quadTo(
//						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
//						endx, endy, endz);
//				numCrosses = computeCrossingsFromEllipse(
//						numCrosses,
//						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
//						ex, ey, ez, ew, eh, ed,
//						CrossingComputationType.STANDARD);
//				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
//					return numCrosses;
//				}
//				curx = endx;
//				cury = endy;
//				curz = endz;
//				break;
//			}
//			case CURVE_TO:
//				endx = element.getToX();
//				endy = element.getToY();
//				endz = element.getToZ();
//				// for internal use only
//				localPath = factory.newPath(iterator.getWindingRule());
//				localPath.moveTo(curx, cury, curz);
//				localPath.curveTo(
//						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
//						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
//						endx, endy, endz);
//				numCrosses = computeCrossingsFromEllipse(
//						numCrosses,
//						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
//						ex, ey, ez, ew, eh, ed,
//						CrossingComputationType.STANDARD);
//				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
//					return numCrosses;
//				}
//				curx = endx;
//				cury = endy;
//				curz = endz;
//				break;
//			case CLOSE:
//				if (cury != movy || curx != movx || curz != movz) {
//					numCrosses = Segment3afp.computeCrossingsFromEllipse(
//							numCrosses,
//							ex, ey, ez, ew, eh, ed,
//							curx, cury, curz,
//							movx, movy, movz);
//					if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
//						return numCrosses;
//					}
//				}
//				curx = movx;
//				cury = movy;
//				curz = movz;
//				break;
//			default:
//			}
//		}
//
//		assert(numCrosses!=MathConstants.SHAPE_INTERSECTS);
//
//		boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);
//
//		if (isOpen && type != null) {
//			switch (type) {
//			case AUTO_CLOSE:
//				numCrosses = Segment3afp.computeCrossingsFromEllipse(
//						numCrosses,
//						ex, ey, ez, ew, eh, ed,
//						curx, cury, curz,
//						movx, movy, movz);
//				break;
//			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
//				// Assume that when is the path is open, only
//				// SHAPE_INTERSECTS may be return
//				numCrosses = 0;
//				break;
//			case STANDARD:
//			default:
//				break;
//			}
//		}
//
//		return numCrosses;
//	}

//	/**
//	 * Calculates the number of times the given path
//	 * crosses the given ellipse extending to the right.
//	 * 
//	 * @param crossings is the initial value for crossing.
//	 * @param iterator is the description of the path.
//	 * @param x1 is the first corner of the rectangle.
//	 * @param y1 is the first corner of the rectangle.
//	 * @param x2 is the second corner of the rectangle.
//	 * @param y2 is the second corner of the rectangle.
//	 * @param arcWidth is the width of the arc.
//	 * @param arcHeight is the width of the arc.
//	 * @param type is the type of special computation to apply. If <code>null</code>, it 
//	 * is equivalent to {@link CrossingComputationType#STANDARD}.
//	 * @return the crossing or {@link MathConstants#SHAPE_INTERSECTS}
//	 */
//	static int computeCrossingsFromRoundRect(
//			int crossings, 
//			PathIterator3afp<? extends PathElement3afp> iterator, 
//			double x1, double y1, double x2, double y2,
//			double arcWidth, double arcHeight,
//			CrossingComputationType type) {	
//		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
//		assert (x1 <= x2) : "x1 must be lower or equal to x2"; //$NON-NLS-1$
//		assert (y1 <= y2) : "y1 must be lower or equal to y2"; //$NON-NLS-1$
//
//		if (!iterator.hasNext()) return 0;
//
//		PathElement3afp pathElement = iterator.next();
//
//		if (pathElement.getType() != PathElementType.MOVE_TO) {
//			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
//		}
//
//		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
//		Path3afp<?, ?, ?, ?, ?, ?> localPath;
//		double curx, cury, movx, movy, endx, endy;
//		curx = movx = pathElement.getToX();
//		cury = movy = pathElement.getToY();
//		int numCrossings = crossings;
//
//		while (numCrossings != MathConstants.SHAPE_INTERSECTS
//				&& iterator.hasNext()) {
//			pathElement = iterator.next();
//			switch (pathElement.getType()) {
//			case MOVE_TO:
//				// Count should always be a multiple of 2 here.
//				// assert((crossings & 1) != 0);
//				movx = curx = pathElement.getToX();
//				movy = cury = pathElement.getToY();
//				break;
//			case LINE_TO:
//				endx = pathElement.getToX();
//				endy = pathElement.getToY();
//				numCrossings = Segment3afp.computeCrossingsFromRoundRect(numCrossings,
//						x1, y1, x2, y2, arcWidth, arcHeight,
//						curx, cury,
//						endx, endy);
//				if (numCrossings==MathConstants.SHAPE_INTERSECTS)
//					return numCrossings;
//				curx = endx;
//				cury = endy;
//				break;
//			case QUAD_TO:
//				endx = pathElement.getToX();
//				endy = pathElement.getToY();
//				// for internal use only
//				localPath = factory.newPath(iterator.getWindingRule());
//				localPath.moveTo(curx, cury);
//				localPath.quadTo(
//						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
//						endx, endy);
//				numCrossings = computeCrossingsFromRoundRect(
//						numCrossings,
//						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
//						x1, y1, x2, y2, arcWidth, arcHeight,
//						CrossingComputationType.STANDARD);
//				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
//					return numCrossings;
//				}
//				curx = endx;
//				cury = endy;
//				break;
//			case CURVE_TO:
//				endx = pathElement.getToX();
//				endy = pathElement.getToY();
//				// for internal use only
//				localPath = factory.newPath(iterator.getWindingRule());
//				localPath.moveTo(curx, cury);
//				localPath.curveTo(
//						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
//						pathElement.getCtrlX2(), pathElement.getCtrlY2(),
//						endx, endy);
//				numCrossings = computeCrossingsFromRoundRect(
//						numCrossings,
//						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
//						x1, y1, x2, y2, arcWidth, arcHeight,
//						CrossingComputationType.STANDARD);
//				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
//					return numCrossings;
//				}
//				curx = endx;
//				cury = endy;
//				break;
//			case CLOSE:
//				if (curx != movx || cury != movy) {
//					numCrossings = Segment3afp.computeCrossingsFromRoundRect(numCrossings,
//							x1, y1, x2, y2, arcWidth, arcHeight,
//							curx, cury,
//							movx, movy);
//				}
//				// Stop as soon as possible
//				if (numCrossings!=0) return numCrossings;
//				curx = movx;
//				cury = movy;
//				break;
//			default:
//			}
//		}
//
//		assert(numCrossings != MathConstants.SHAPE_INTERSECTS);
//
//		boolean isOpen = (curx != movx) || (cury != movy);
//
//		if (isOpen && type != null) {
//			switch (type) {
//			case AUTO_CLOSE:
//				// Not closed
//				numCrossings = Segment3afp.computeCrossingsFromRoundRect(numCrossings,
//						x1, y1, x2, y2, arcWidth, arcHeight,
//						curx, cury,
//						movx, movy);
//				break;
//			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
//				// Assume that when is the path is open, only
//				// SHAPE_INTERSECTS may be return
//				numCrossings = 0;
//				break;
//			case STANDARD:
//			default:
//				break;
//			}
//		}
//
//		return numCrossings;
//	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param cz is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param type is the type of special computation to apply. If <code>null</code>, it 
	 * is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int computeCrossingsFromSphere(
			int crossings, 
			PathIterator3afp<? extends PathElement3afp> iterator,
			double cx, double cy, double cz, double radius,
			CrossingComputationType type) {	
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (radius >= 0.) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		// Copied from the AWT API
		if (!iterator.hasNext()) return 0;
		PathElement3afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double movz = element.getToZ();
		double curx = movx;
		double cury = movy;
		double curz = movz;
		double endx, endy, endz;
		int numCrosses = crossings;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				movz = curz = element.getToZ();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				numCrosses = Segment3afp.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, cz, radius,
						curx, cury, curz,
						endx, endy, endz);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						endx, endy, endz);
				numCrosses = computeCrossingsFromSphere(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, cz, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				numCrosses = computeCrossingsFromSphere(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, cz, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					numCrosses = Segment3afp.computeCrossingsFromCircle(
							numCrosses,
							cx, cy, cz, radius,
							curx, cury, curz,
							movx, movy, movz);
					if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}
		}

		assert (numCrosses != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch(type) {
			case AUTO_CLOSE:
				// Auto close
				numCrosses = Segment3afp.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, cz, radius,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrosses = 0;
				break;
			case STANDARD:
			default:
				// Standard behavior
				break;
			}
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given segment extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param z1 is the first point of the segment.
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param z2 is the first point of the segment.
	 * @param type is the type of special computation to apply. If <code>null</code>, it 
	 * is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int computeCrossingsFromSegment(int crossings, PathIterator3afp<? extends PathElement3afp> iterator,
			double x1, double y1, double z1, double x2, double y2, double z2, CrossingComputationType type) {	
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		// Copied from the AWT API
		if (!iterator.hasNext() || crossings==MathConstants.SHAPE_INTERSECTS) return crossings;
		PathElement3afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double movz = element.getToZ();
		double curx = movx;
		double cury = movy;
		double curz = movz;
		double endx, endy, endz;
		int numCrosses = crossings;
		while (numCrosses!=MathConstants.SHAPE_INTERSECTS && iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				movz = curz = element.getToZ();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				numCrosses = Segment3afp.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, z1, x2, y2, z2,
						curx, cury, curz,
						endx, endy, endz);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS)
					return numCrosses;
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury, curz);
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						endx, endy, endz);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2,
						CrossingComputationType.STANDARD);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS)
					return numCrosses;
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury, curz);
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2,
						CrossingComputationType.STANDARD);
				if (numCrosses==MathConstants.SHAPE_INTERSECTS)
					return numCrosses;
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					numCrosses = Segment3afp.computeCrossingsFromSegment(
							numCrosses,
							x1, y1, z1, x2, y2, z2,
							curx, cury, curz,
							movx, movy, movz);
				}
				if (numCrosses!=0)	return numCrosses;
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}
		}

		assert(numCrosses!=MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				numCrosses = Segment3afp.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, z1, x2, y2, z2,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrosses = 0;
				break;
			case STANDARD:
			default:
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
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the iterator on the path elements.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rzmin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param rzmax is the second corner of the rectangle.
	 * @param type is the type of special computation to apply. If <code>null</code>, it 
	 * is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 */
	static int computeCrossingsFromRect(
			int crossings,
			PathIterator3afp<? extends PathElement3afp> iterator,
			double rxmin, double rymin, double rzmin,
			double rxmax, double rymax, double rzmax,
			CrossingComputationType type) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (rxmin <= rxmax) : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert (rymin <= rymax) : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
		assert (rzmin <= rzmax) : "rzmin must be lower or equal to rzmax"; //$NON-NLS-1$
		// Copied from AWT API
		if (!iterator.hasNext()) return 0;

		PathElement3afp pathElement = iterator.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?> localPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();
		int numCrossings = crossings;

		while (numCrossings != MathConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement = iterator.next();
			switch (pathElement.getType()) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = curz = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				numCrossings = Segment3afp.computeCrossingsFromRect(
						numCrossings,
						rxmin, rymin, rzmin,
						rxmax, rymax, rzmax,
						curx, cury, curz,
						endx, endy, endz);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury, curz);
				localPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rzmin,
						rxmax, rymax, rzmax,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury, curz);
				localPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rzmin,
						rxmax, rymax, rzmax,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					numCrossings = Segment3afp.computeCrossingsFromRect(
							numCrossings,
							rxmin, rymin, rzmin,
							rxmax, rymax, rzmax,
							curx, cury, curz,
							movx, movy, movz);
				}
				// Stop as soon as possible
				if (numCrossings != 0) {
					return numCrossings;
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}
		}

		assert(numCrossings != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = Segment3afp.computeCrossingsFromRect(
						numCrossings,
						rxmin, rymin, rzmin,
						rxmax, rymax, rzmax,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrossings = 0;
				break;
			case STANDARD:
			default:
				break;
			}
		}

		return numCrossings;
	}

//	/**
//	 * Accumulate the number of times the path crosses the shadow
//	 * extending to the right of the triangle.  See the comment
//	 * for the SHAPE_INTERSECTS constant for more complete details.
//	 * The return value is the sum of all crossings for both the
//	 * top and bottom of the shadow for every segment in the path,
//	 * or the special value SHAPE_INTERSECTS if the path ever enters
//	 * the interior of the triangle.
//	 * The path must start with a SEG_MOVETO, otherwise an exception is
//	 * thrown.
//	 * The caller must check for NaN values.
//	 * 
//	 * @param crossings is the initial value for crossing.
//	 * @param iterator is the iterator on the path elements.
//	 * @param x1 is the first point of the triangle.
//	 * @param y1 is the first point of the triangle.
//	 * @param x2 is the second point of the triangle.
//	 * @param y2 is the second point of the triangle.
//	 * @param x3 is the third point of the triangle.
//	 * @param y3 is the third point of the triangle.
//	 * @param type is the type of special computation to apply. If <code>null</code>, it 
//	 * is equivalent to {@link CrossingComputationType#STANDARD}.
//	 * @return the crossings.
//	 */
//	static int computeCrossingsFromTriangle(
//			int crossings,
//			PathIterator3afp<? extends PathElement3afp> iterator,
//			double x1, double y1,
//			double x2, double y2,
//			double x3, double y3,
//			CrossingComputationType type) {
//		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
//		if (!iterator.hasNext()) return 0;
//
//		PathElement3afp pathElement = iterator.next();
//
//		if (pathElement.getType() != PathElementType.MOVE_TO) {
//			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
//		}
//
//		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
//		Path3afp<?, ?, ?, ?, ?, ?> localPath;
//		double curx, cury, movx, movy, endx, endy;
//		curx = movx = pathElement.getToX();
//		cury = movy = pathElement.getToY();
//		int numCrossings = crossings;
//
//		while (numCrossings != MathConstants.SHAPE_INTERSECTS
//				&& iterator.hasNext()) {
//			pathElement = iterator.next();
//			switch (pathElement.getType()) {
//			case MOVE_TO:
//				// Count should always be a multiple of 2 here.
//				// assert((crossings & 1) != 0);
//				movx = curx = pathElement.getToX();
//				movy = cury = pathElement.getToY();
//				break;
//			case LINE_TO:
//				endx = pathElement.getToX();
//				endy = pathElement.getToY();
//				numCrossings = Segment3afp.computeCrossingsFromTriangle(numCrossings,
//						x1, y1, x2, y2, x3, y3,
//						curx, cury,
//						endx, endy);
//				if (numCrossings==MathConstants.SHAPE_INTERSECTS)
//					return numCrossings;
//				curx = endx;
//				cury = endy;
//				break;
//			case QUAD_TO:
//				endx = pathElement.getToX();
//				endy = pathElement.getToY();
//				// for internal use only
//				localPath = factory.newPath(iterator.getWindingRule());
//				localPath.moveTo(curx, cury);
//				localPath.quadTo(
//						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
//						endx, endy);
//				numCrossings = computeCrossingsFromTriangle(
//						numCrossings,
//						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
//						x1, y1, x2, y2, x3, y3,
//						CrossingComputationType.STANDARD);
//				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
//					return numCrossings;
//				}
//				curx = endx;
//				cury = endy;
//				break;
//			case CURVE_TO:
//				endx = pathElement.getToX();
//				endy = pathElement.getToY();
//				// for internal use only
//				localPath = factory.newPath(iterator.getWindingRule());
//				localPath.moveTo(curx, cury);
//				localPath.curveTo(
//						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
//						pathElement.getCtrlX2(), pathElement.getCtrlY2(),
//						endx, endy);
//				numCrossings = computeCrossingsFromTriangle(
//						numCrossings,
//						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
//						x1, y1, x2, y2, x3, y3,
//						CrossingComputationType.STANDARD);
//				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
//					return numCrossings;
//				}
//				curx = endx;
//				cury = endy;
//				break;
//			case CLOSE:
//				if (curx != movx || cury != movy) {
//					numCrossings = Segment3afp.computeCrossingsFromTriangle(numCrossings,
//							x1, y1, x2, y2, x3, y3,
//							curx, cury,
//							movx, movy);
//				}
//				// Stop as soon as possible
//				if (numCrossings!=0) return numCrossings;
//				curx = movx;
//				cury = movy;
//				break;
//			default:
//			}
//		}
//
//		assert(numCrossings != MathConstants.SHAPE_INTERSECTS);
//
//		boolean isOpen = (curx != movx) || (cury != movy);
//
//		if (isOpen && type != null) {
//			switch (type) {
//			case AUTO_CLOSE:
//				// Not closed
//				numCrossings = Segment3afp.computeCrossingsFromTriangle(numCrossings,
//						x1, y1, x2, y2, x3, y3,
//						curx, cury,
//						movx, movy);
//				break;
//			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
//				// Assume that when is the path is open, only
//				// SHAPE_INTERSECTS may be return
//				numCrossings = 0;
//				break;
//			case STANDARD:
//			default:
//				break;
//			}
//		}
//
//		return numCrossings;
//	}

	/** Compute the box that corresponds to the drawable elements of the path.
	 * 
	 * <p>An element is drawable if it is a line, a curve, or a closing path element.
	 * The box fits the drawn lines and the drawn curves. The control points of the
	 * curves may be outside the output box. For obtaining the bounding box
	 * of the path's points, use {@link #computeControlPointBoundingBox(PathIterator3afp, RectangularPrism3afp)}.
	 * 
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a drawable element was found.
	 * @see #computeControlPointBoundingBox(PathIterator3afp, RectangularPrism3afp)
	 */
	static boolean computeDrawableElementBoundingBox(PathIterator3afp<?> iterator,
			RectangularPrism3afp<?, ?, ?, ?, ?, ?> box) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		boolean foundOneLine = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double zmin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		double zmax = Double.NEGATIVE_INFINITY;
		PathElement3afp element;
		Path3afp<?, ?, ?, ?, ?, ?> subPath;
		RectangularPrism3afp<?, ?, ?, ?, ?, ?> subBox;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.getType()) {
			case LINE_TO:
				if (element.getFromX()<xmin) xmin = element.getFromX();
				if (element.getFromY()<ymin) ymin = element.getFromY();
				if (element.getFromZ()<zmin) zmin = element.getFromZ();
				if (element.getFromX()>xmax) xmax = element.getFromX();
				if (element.getFromY()>ymax) ymax = element.getFromY();
				if (element.getFromZ()>zmax) zmax = element.getFromZ();
				if (element.getToX()<xmin) xmin = element.getToX();
				if (element.getToY()<ymin) ymin = element.getToY();
				if (element.getToZ()<zmin) zmin = element.getToZ();
				if (element.getToX()>xmax) xmax = element.getToX();
				if (element.getToY()>ymax) ymax = element.getToY();
				if (element.getToZ()>zmax) zmax = element.getToZ();
				foundOneLine = true;
				break;
			case CURVE_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subBox = factory.newBox();
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						element.getToX(), element.getToY(), element.getToZ());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						subBox)) {
					if (subBox.getMinX()<xmin) xmin = subBox.getMinX();
					if (subBox.getMinY()<ymin) ymin = subBox.getMinY();
					if (subBox.getMinZ()<zmin) zmin = subBox.getMinZ();
					if (subBox.getMaxX()>xmax) xmax = subBox.getMaxX();
					if (subBox.getMaxY()>ymax) ymax = subBox.getMaxY();
					if (subBox.getMaxZ()>zmax) zmax = subBox.getMaxZ();
					foundOneLine = true;
				}
				break;
			case QUAD_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subBox = factory.newBox();
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getToX(), element.getToY(), element.getToZ());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						subBox)) {
					if (subBox.getMinX()<xmin) xmin = subBox.getMinX();
					if (subBox.getMinY()<ymin) ymin = subBox.getMinY();
					if (subBox.getMinZ()<zmin) zmin = subBox.getMinZ();
					if (subBox.getMaxX()>xmax) xmax = subBox.getMaxX();
					if (subBox.getMaxY()>ymax) ymax = subBox.getMaxY();
					if (subBox.getMaxZ()>zmax) zmax = subBox.getMaxZ();
					foundOneLine = true;
				}
				break;
			case MOVE_TO:
			case CLOSE:
			default:
			}
		}
		if (foundOneLine) {
			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
		}
		else {
			box.clear();
		}
		return foundOneLine;
	}

	/** Compute the box that corresponds to the control points of the path.
	 * 
	 * <p>An element is drawable if it is a line, a curve, or a closing path element.
	 * The box fits the drawn lines and the drawn curves. The control points of the
	 * curves may be outside the output box. For obtaining the bounding box
	 * of the drawn lines and cruves, use
	 * {@link #computeDrawableElementBoundingBox(PathIterator3afp, RectangularPrism3afp)}.
	 * 
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a control point was found.
	 * @see #computeDrawableElementBoundingBox(PathIterator3afp, RectangularPrism3afp)
	 */
	static boolean computeControlPointBoundingBox(PathIterator3afp<?> iterator,
			RectangularPrism3afp<?, ?, ?, ?, ?, ?> box) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		boolean foundOneControlPoint = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double zmin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		double zmax = Double.NEGATIVE_INFINITY;
		PathElement3afp element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.getType()) {
			case LINE_TO:
				if (element.getFromX()<xmin) xmin = element.getFromX();
				if (element.getFromY()<ymin) ymin = element.getFromY();
				if (element.getFromZ()<zmin) zmin = element.getFromZ();
				if (element.getFromX()>xmax) xmax = element.getFromX();
				if (element.getFromY()>ymax) ymax = element.getFromY();
				if (element.getFromZ()>zmax) zmax = element.getFromZ();
				if (element.getToX()<xmin) xmin = element.getToX();
				if (element.getToY()<ymin) ymin = element.getToY();
				if (element.getToZ()<zmin) zmin = element.getToZ();
				if (element.getToX()>xmax) xmax = element.getToX();
				if (element.getToY()>ymax) ymax = element.getToY();
				if (element.getToZ()>zmax) zmax = element.getToZ();
				foundOneControlPoint = true;
				break;
			case CURVE_TO:
				if (element.getFromX()<xmin) xmin = element.getFromX();
				if (element.getFromY()<ymin) ymin = element.getFromY();
				if (element.getFromZ()<zmin) zmin = element.getFromZ();
				if (element.getFromX()>xmax) xmax = element.getFromX();
				if (element.getFromY()>ymax) ymax = element.getFromY();
				if (element.getFromZ()>zmax) zmax = element.getFromZ();
				if (element.getCtrlX1()<xmin) xmin = element.getCtrlX1();
				if (element.getCtrlY1()<ymin) ymin = element.getCtrlY1();
				if (element.getCtrlZ1()<zmin) zmin = element.getCtrlZ1();
				if (element.getCtrlX1()>xmax) xmax = element.getCtrlX1();
				if (element.getCtrlY1()>ymax) ymax = element.getCtrlY1();
				if (element.getCtrlZ1()>zmax) zmax = element.getCtrlZ1();
				if (element.getCtrlX2()<xmin) xmin = element.getCtrlX2();
				if (element.getCtrlY2()<ymin) ymin = element.getCtrlY2();
				if (element.getCtrlZ2()<zmin) zmin = element.getCtrlZ2();
				if (element.getCtrlX2()>xmax) xmax = element.getCtrlX2();
				if (element.getCtrlY2()>ymax) ymax = element.getCtrlY2();
				if (element.getCtrlZ2()>zmax) zmax = element.getCtrlZ2();
				if (element.getToX()<xmin) xmin = element.getToX();
				if (element.getToY()<ymin) ymin = element.getToY();
				if (element.getToZ()<zmin) zmin = element.getToZ();
				if (element.getToX()>xmax) xmax = element.getToX();
				if (element.getToY()>ymax) ymax = element.getToY();
				if (element.getToZ()>zmax) zmax = element.getToZ();
				foundOneControlPoint = true;
				break;
			case QUAD_TO:
				if (element.getFromX()<xmin) xmin = element.getFromX();
				if (element.getFromY()<ymin) ymin = element.getFromY();
				if (element.getFromZ()<zmin) zmin = element.getFromZ();
				if (element.getFromX()>xmax) xmax = element.getFromX();
				if (element.getFromY()>ymax) ymax = element.getFromY();
				if (element.getFromZ()>zmax) zmax = element.getFromZ();
				if (element.getCtrlX1()<xmin) xmin = element.getCtrlX1();
				if (element.getCtrlY1()<ymin) ymin = element.getCtrlY1();
				if (element.getCtrlZ1()<zmin) zmin = element.getCtrlZ1();
				if (element.getCtrlX1()>xmax) xmax = element.getCtrlX1();
				if (element.getCtrlY1()>ymax) ymax = element.getCtrlY1();
				if (element.getCtrlZ1()>zmax) zmax = element.getCtrlZ1();
				if (element.getToX()<xmin) xmin = element.getToX();
				if (element.getToY()<ymin) ymin = element.getToY();
				if (element.getToZ()<zmin) zmin = element.getToZ();
				if (element.getToX()>xmax) xmax = element.getToX();
				if (element.getToY()>ymax) ymax = element.getToY();
				if (element.getToZ()>zmax) zmax = element.getToZ();
				foundOneControlPoint = true;
				break;
			case MOVE_TO:
			case CLOSE:
			default:
			}
		}
		if (foundOneControlPoint) {			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
		}
		else {
			box.clear();
		}
		return foundOneControlPoint;
	}

	/** Compute the total squared length of the path.
	 *
	 * @param iterator the iterator on the path elements.
	 * @return the squared length of the path.
	 */
	static double computeLength(PathIterator3afp<?> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		PathElement3afp pathElement = iterator.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		// only for internal use
		GeomFactory3afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?> subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		double length = 0;

		while (iterator.hasNext()) {
			pathElement = iterator.next();

			switch (pathElement.getType()) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = curz = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				length += Point3D.getDistancePointPoint(
						curx, cury, curz,  
						endx, endy, endz);
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);
				length += computeLength(subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);
				length += computeLength(subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					length += Point3D.getDistancePointPoint(
							curx, cury, curz,
							movx, movy, movz);
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return length;
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
	default void add(Iterator<? extends PathElement3afp> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		PathElement3afp element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.getType()) {
			case MOVE_TO:
				moveTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case LINE_TO:
				lineTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case QUAD_TO:
				quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getToX(), element.getToY(), element.getToZ());
				break;
			case CURVE_TO:
				curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),  element.getToX(), element.getToY(), element.getToZ());
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
	default void set(Path3afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Path must be not null"; //$NON-NLS-1$
		clear();
		add(s.getPathIterator());
	}

	/**
	 * Adds a point to the path by moving to the specified
	 * coordinates specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Y coordinate
	 */
	void moveTo(double x, double y, double z);

	@Override
	default void moveTo(Point3D<?, ?> position) {
		assert (position != null) : "Point must be not null"; //$NON-NLS-1$
		moveTo(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Y coordinate
	 */
	void lineTo(double x, double y, double z);

	@Override
	default void lineTo(Point3D<?, ?> to) {
		assert (to != null) : "Point must be not null"; //$NON-NLS-1$
		lineTo(to.getX(), to.getY(), to.getZ());
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
	 * @param z1 the Z coordinate of the quadratic control point
	 * @param x2 the X coordinate of the final end point
	 * @param y2 the Y coordinate of the final end point
	 * @param z2 the Y coordinate of the final end point
	 */
	void quadTo(double x1, double y1, double z1, double x2, double y2, double z2);

	@Override
	default void quadTo(Point3D<?, ?> ctrl, Point3D<?, ?> to) {
		assert (ctrl != null) : "Control point must be not null"; //$NON-NLS-1$
		assert (to != null) : "Target point must be not null"; //$NON-NLS-1$
		quadTo(ctrl.getX(), ctrl.getY(), ctrl.getZ(), to.getX(), to.getY(), to.getZ());
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
	 * @param z1 the Z coordinate of the first B&eacute;zier control point
	 * @param x2 the X coordinate of the second B&eacute;zier control point
	 * @param y2 the Y coordinate of the second B&eacute;zier control point
	 * @param z2 the Z coordinate of the second B&eacute;zier control point
	 * @param x3 the X coordinate of the final end point
	 * @param y3 the Y coordinate of the final end point
	 * @param z3 the Z coordinate of the final end point
	 */
	void curveTo(double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3);

	@Override
	default void curveTo(Point3D<?, ?> ctrl1, Point3D<?, ?> ctrl2, Point3D<?, ?> to) {
		assert (ctrl1 != null) : "First control point must be not null"; //$NON-NLS-1$
		assert (ctrl2 != null) : "Second control point must be not null"; //$NON-NLS-1$
		assert (to != null) : "Taarget point must be not null"; //$NON-NLS-1$
		curveTo(ctrl1.getX(), ctrl1.getY(), ctrl1.getZ(), ctrl2.getX(), ctrl2.getY(), ctrl2.getZ(), to.getX(), to.getY(), to.getZ());

	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point3D<?, ?> c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point3D<?, ?> c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point3D<?, ?> c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return containsPoint(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), x, y, z);
	}

	@Override
	default boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return containsRectangle(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				r.getMinX(), r.getMinY(), r.getMinZ(), r.getWidth(), r.getHeight(), r.getDepth());
	}

	@Pure
	@Override
	default boolean intersects(Prism3afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null"; //$NON-NLS-1$
		// Copied from AWT API
		if (s.isEmpty()) return false;
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(
				0, getPathIterator(),
				s.getMinX(), s.getMinY(), s.getMinZ(), s.getMaxX(), s.getMaxY(), s.getMaxZ(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromSphere(
				0,
				getPathIterator(),
				s.getX(), s.getY(), s.getZ(), s.getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromSegment(
				0,
				getPathIterator(),
				s.getX1(), s.getY1(), s.getZ1(), s.getX2(), s.getY2(), s.getZ2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}


	@Pure
	@Override
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Path must be not null"; //$NON-NLS-1$
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				0,
				s.getPathIterator(),
				new PathShadow3afp<>(this),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				0,
				iterator,
				new PathShadow3afp<>(this),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
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
	 * @param z
	 */
	void setLastPoint(double x, double y, double z);

	@Override
	default void setLastPoint(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		setLastPoint(point.getX(), point.getY(), point.getZ());
	}

	/** Transform the current path.
	 * This function changes the current path.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform3D transform);

	@Override
	default double getLength() {
		return computeLength(getPathIterator());
	}

	@Override
	default double getLengthSquared() {
		double length = getLength();
		return length * length;
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
		if (transform == null) {
			return new PathPathIterator<>(this);
		}
		return new TransformedPathPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(double flatness) {
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
	 * @param transform is an optional affine Transform3D to be applied to the
	 * coordinates as they are returned in the iteration, or <code>null</code> if 
	 * untransformed coordinates are desired.
	 * @param flatness is the maximum distance that the line segments used to approximate
	 * the curved segments are allowed to deviate from any point on the original curve.
	 * @return an iterator on the path elements.
	 */
	@Pure
	default PathIterator3afp<IE> getPathIterator(Transform3D transform, double flatness) {
		return new FlatteningPathIterator<>(getPathIterator(transform), flatness, DEFAULT_FLATENING_LIMIT);
	}
	

	/** Replies the x coordinate of the last point in the path.
	 *
	 * @return the x coordinate of the last point in the path.
	 */
	@Pure
	double getCurrentX();
	
	/** Replies the y coordinate of the last point in the path.
	 *
	 * @return the y coordinate of the last point in the path.
	 */
	@Pure
	double getCurrentY();
	
	/** Replies the z coordinate of the last point in the path.
	 *
	 * @return the z coordinate of the last point in the path.
	 */
	@Pure
	double getCurrentZ();

	@Override
	@Pure
	default P getCurrentPoint() {
		return getGeomFactory().newPoint(getCurrentX(), getCurrentY(), getCurrentZ());
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		Path3afp.getClosestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(), p.getZ(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		Path3afp.getFarthestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(), p.getZ(),
				point);
		return point;
	}

	@Override
	default Collection<P> toCollection() {
		return new PointCollection<>(this);
	}

	/** Remove the point with the given coordinates.
	 * 
	 * <p>If the given coordinates do not match exactly a point in the path, nothing is removed.
	 * 
	 * @param x the x coordinate of the ponit to remove.
	 * @param y the y coordinate of the ponit to remove.
	 * @param z the z coordinate of the ponit to remove.
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(double x, double y, double z);

	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		Path3afp.computeDrawableElementBoundingBox(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				box);
	}

	/** Abstract iterator on the path elements of the path.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractPathPathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		private final Path3afp<?, ?, T, ?, ?, ?> path;

		/**
		 * @param path the iterated path.
		 */
		public AbstractPathPathIterator(Path3afp<?, ?, T, ?, ?, ?> path) {
			assert (path != null) : "Path must be not null"; //$NON-NLS-1$
			this.path = path;
		}

		@Override
		public GeomFactory3afp<T, ?, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
		}

		/** Replies the path.
		 *
		 * @return the path.
		 */
		public Path3afp<?, ?, T, ?, ?, ?> getPath() {
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
	class PathPathIterator<T extends PathElement3afp> extends AbstractPathPathIterator<T> {

		private Point3D<?, ?> p1;
		private Point3D<?, ?> p2;
		private int iType = 0;
		private int iCoord = 0;
		private double movex, movey, movez;

		/**
		 * @param path the path to iterate on.
		 */
		public PathPathIterator(Path3afp<?, ?, T, ?, ?, ?> path) {
			super(path);
			this.p1 = new InnerComputationPoint3afp();
			this.p2 = new InnerComputationPoint3afp();
		}
		
		@Override
		public PathIterator3afp<T> restartIterations() {
			return new PathPathIterator<>(getPath());
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			Path3afp<?, ?, T, ?, ?, ?> path = getPath();
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
				this.movez = path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				if ((this.iCoord + 3) > (path.size() * 3)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				element = getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p1.getZ());
				break;
			case QUAD_TO:
			{
				if ((this.iCoord + 6) > (path.size() * 3)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx = path.getCoordAt(this.iCoord++);
				double ctrly = path.getCoordAt(this.iCoord++);
				double ctrlz = path.getCoordAt(this.iCoord++);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx, ctrly, ctrlz,
						this.p2.getX(), this.p2.getY(), this.p1.getZ());
			}
			break;
			case CURVE_TO:
			{
				if ((this.iCoord + 9) > (path.size() * 3)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx1 = path.getCoordAt(this.iCoord++);
				double ctrly1 = path.getCoordAt(this.iCoord++);
				double ctrlz1 = path.getCoordAt(this.iCoord++);
				double ctrlx2 = path.getCoordAt(this.iCoord++);
				double ctrly2 = path.getCoordAt(this.iCoord++);
				double ctrlz2 = path.getCoordAt(this.iCoord++);
				this.p2.set(
						getPath().getCoordAt(this.iCoord++),
						getPath().getCoordAt(this.iCoord++),
						getPath().getCoordAt(this.iCoord++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx1, ctrly1, ctrlz1,
						ctrlx2, ctrly2, ctrlz2,
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newClosePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
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
	class TransformedPathPathIterator<T extends PathElement3afp> extends AbstractPathPathIterator<T> {

		private final Transform3D transform;

		private final Point3D<?, ?> p1;
		private final Point3D<?, ?> p2;
		private final Point3D<?, ?> ptmp1;
		private final Point3D<?, ?> ptmp2;

		private int iType = 0;

		private int iCoord = 0;

		private double movex;

		private double movey;

		private double movez;

		/**
		 * @param path the path to iterate on.
		 * @param transform the transformation to apply on the path.
		 */
		public TransformedPathPathIterator(Path3afp<?, ?, T, ?, ?, ?> path, Transform3D transform) {
			super(path);
			assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			this.p1 = new InnerComputationPoint3afp();
			this.p2 = new InnerComputationPoint3afp();
			this.ptmp1 = new InnerComputationPoint3afp();
			this.ptmp2 = new InnerComputationPoint3afp();
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new TransformedPathPathIterator<>(getPath(), this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			Path3afp<?, ?, T, ?, ?, ?> path = getPath();
			if (this.iType >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			T element = null;
			switch(path.getPathElementTypeAt(this.iType++)) {
			case MOVE_TO:
				this.movex = path.getCoordAt(this.iCoord++);
				this.movey = path.getCoordAt(this.iCoord++);
				this.movez = path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.ptmp1.getX(), this.ptmp1.getY(), this.ptmp1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp2);
				this.p2.set(
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++),
						path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.ptmp1.getX(), this.ptmp1.getY(), this.ptmp1.getZ(),
						this.ptmp2.getX(), this.ptmp2.getY(), this.ptmp2.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newClosePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
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
	// TODO integrate Z coordinate
	class FlatteningPathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		/** The source iterator.
		 */
		private final PathIterator3afp<T> pathIterator;

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

		/** The ending z of the last segment.
		 */
		private double currentZ;

		/** The x of the last move segment.
		 */
		private double moveX;

		/** The y of the last move segment.
		 */
		private double moveY;
		
		/** The z of the last move segment.
		 */
		private double moveZ;

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

		/** The z of the last move segment replied by next.
		 */
		private double lastNextZ;

		/**
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 * control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions
		 * allowed for any curved segment
		 */
		public FlatteningPathIterator(PathIterator3afp<T> pathIterator, double flatness, int limit) {
			assert (pathIterator != null) : "Iterator must be not null"; //$NON-NLS-1$
			assert (flatness >= 0.) : "Flatness must be positive or zero"; //$NON-NLS-1$
			assert (limit >= 0) : "Recursive subdivisions number must be positive or zero"; //$NON-NLS-1$
			this.pathIterator = pathIterator;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit;
			this.levels = new int[limit + 1];
			searchNext();
		}
		
		@Override
		public PathIterator3afp<T> restartIterations() {
			return new FlatteningPathIterator<>(
					this.pathIterator.restartIterations(),
					Math.sqrt(this.squaredFlatness),
					this.limit);
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
//			return Segment3afp.computeDistanceSquaredLinePoint(
//					coords[offset + 0], coords[offset + 1],
//					coords[offset + 4], coords[offset + 5],
//					coords[offset + 2], coords[offset + 3]);
			// TODO : correct indexes
			return -1;
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
//			double z1 = src[srcoff + 2];
			double ctrlx = src[srcoff + 2];
			double ctrly = src[srcoff + 3];
//			double ctrlz = src[srcoff + 4];
			double x2 = src[srcoff + 4];
			double y2 = src[srcoff + 5];
//			double z2 = src[srcoff + 7];
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
//			z1 = (z1 + ctrlz) / 2;
			x2 = (x2 + ctrlx) / 2;
			y2 = (y2 + ctrly) / 2;
//			z2 = (z2 + ctrlz) / 2;
			ctrlx = (x1 + x2) / 2;
			ctrly = (y1 + y2) / 2;
//			ctrlz = (z1 + z2) / 2;
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
//			return Math.max(
//					Segment3afp.computeDistanceSquaredSegmentPoint(
//							coords[offset + 6],
//							coords[offset + 7],
//							coords[offset + 2],
//							coords[offset + 3],
//							coords[offset + 0],
//							coords[offset + 1]),
//					Segment3afp.computeDistanceSquaredSegmentPoint(
//							coords[offset + 6],
//							coords[offset + 7],
//							coords[offset + 4],
//							coords[offset + 5],
//							coords[offset + 0],
//							coords[offset + 1]));
			// TODO correct indexes
			return -1;
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
//			double z1 = src[srcoff + 2];
			double ctrlx1 = src[srcoff + 2];
			double ctrly1 = src[srcoff + 3];
//			double ctrlz1 = src[srcoff + 4];
			double ctrlx2 = src[srcoff + 4];
			double ctrly2 = src[srcoff + 5];
//			double ctrlz2 = src[srcoff + 6];
			double x2 = src[srcoff + 6];
			double y2 = src[srcoff + 7];
//			double 2 = src[srcoff + 8];
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
				this.currentY = this.hold[2];
				if (this.holdType == PathElementType.MOVE_TO) {
					this.moveX = this.currentX;
					this.moveY = this.currentY;
					this.moveZ = this.currentZ;
				}
				this.holdIndex = 0;
				this.holdEnd = 0;
				break;
			case CLOSE:
				this.currentX = this.moveX;
				this.currentY = this.moveY;
				this.currentZ = this.moveZ;
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
				double z = this.hold[this.holdIndex + 2];
				if (type == PathElementType.MOVE_TO) {
					element = getGeomFactory().newMovePathElement(x, y, z);
				}
				else {
					element = getGeomFactory().newLinePathElement(
							this.lastNextX, this.lastNextY, this.lastNextZ,
							x, y, z);
				}
				this.lastNextX = x;
				this.lastNextY = y;
				this.lastNextZ = z;
			}
			else {
				element = getGeomFactory().newClosePathElement(
						this.lastNextX, this.lastNextY, this.lastNextZ,
						this.moveX, this.moveY, this.moveZ);
				this.lastNextX = this.moveX;
				this.lastNextY = this.moveY;
				this.lastNextZ = this.moveZ;
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
			return this.pathIterator.isPolyline() || (!this.pathIterator.isMultiParts() && !this.pathIterator.isPolygon());
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
		public GeomFactory3afp<T, ?, ?, ?> getGeomFactory() {
			return this.pathIterator.getGeomFactory();
		}

	}

	/** An collection of the points of the path.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointCollection<P extends Point3D<? super P, ? super V>, V extends Vector3D<? super V, ? super P>> implements Collection<P> {

		private final Path3afp<?, ?, ?, P, V, ?> path;

		/**
		 * @param path the path to iterate on.
		 */
		public PointCollection(Path3afp<?, ?, ?, P, V, ?> path) {
			assert (path != null) : "Path must be not null"; //$NON-NLS-1$
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
			if (o instanceof Point3D) {
				return this.path.containsControlPoint((Point3D<?, ?>) o);
			}
			return false;
		}

		@Pure
		@Override
		public Iterator<P> iterator() {
			return new PointIterator<>(this.path);
		}

		@Pure
		@Override
		public Object[] toArray() {
			return this.path.toPointArray();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			assert (a != null) : "Array must be not null"; //$NON-NLS-1$
			Iterator<P> iterator = new PointIterator<>(this.path);
			for(int i=0; i<a.length && iterator.hasNext(); ++i) {
				a[i] = (T)iterator.next();
			}
			return a;
		}

		@Override
		public boolean add(P e) {
			if (e!=null) {
				if (this.path.size()==0) {
					this.path.moveTo(e.getX(), e.getY(), e.getZ());
				}
				else {
					this.path.lineTo(e.getX(), e.getY(), e.getZ());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point3D) {
				Point3D<?, ?> p = (Point3D<?, ?>) o;
				return this.path.remove(p.getX(), p.getY(), p.getZ());
			}
			return false;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> c) {
			assert (c != null) : "Collection must be not null"; //$NON-NLS-1$
			for(Object obj : c) {
				if ((!(obj instanceof Point3D))
						||(!this.path.containsControlPoint((Point3D<?, ?>) obj))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends P> c) {
			assert (c != null) : "Collection must be not null"; //$NON-NLS-1$
			boolean changed = false;
			for(P pts : c) {
				if (add(pts)) {
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			assert (c != null) : "Collection must be not null"; //$NON-NLS-1$
			boolean changed = false;
			for(Object obj : c) {
				if (obj instanceof Point3D) {
					Point3D<?, ?> pts = (Point3D<?, ?>) obj;
					if (this.path.remove(pts.getX(), pts.getY(), pts.getZ())) {
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
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointIterator<P extends Point3D<? super P, ? super V>, V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final Path3afp<?, ?, ?, P, V, ?> path;

		private int index = 0;

		private P lastReplied = null;

		/**
		 * @param path the path to iterate on.
		 */
		public PointIterator(Path3afp<?, ?, ?, P, V, ?> path) {
			assert (path != null) : "Path must be not null"; //$NON-NLS-1$
			this.path = path;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < this.path.size();
		}

		@Override
		public P next() {
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
			Point3D<?, ?> p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			this.path.remove(p.getX(), p.getY(), p.getZ());
		}

	}

	/** Type of computation for the crossing of the path's shadow with a shape.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	enum CrossingComputationType {
		/** The crossing is computed with the default standard approach.
		 */
		STANDARD,

		/** The path is automatically close by the crossing computation function.
		 */
		AUTO_CLOSE,

		/** When the path is not a polygon, i.e. not closed,the crossings will
		 * only consider the shape intersection only. The other crossing values
		 * will be assumed to be always equal to zero. 
		 */
		SIMPLE_INTERSECTION_WHEN_NOT_POLYGON;
	}

}

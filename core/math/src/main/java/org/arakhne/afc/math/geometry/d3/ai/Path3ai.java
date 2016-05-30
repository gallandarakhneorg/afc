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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.PathIterator2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp.AbstractCirclePathIterator;
import org.arakhne.afc.math.geometry.d3.Path3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
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
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Path3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends Path3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Shape3ai<ST, IT, IE, P, V, B>, Path3D<ST, IT, PathIterator3ai<IE>, P, V, B> {

	/** Multiple of cubic & quad curve size.
	 */
	static final int GROW_SIZE = 24;

	/** Default depth for the flattening of the path.
	 */
	static final int DEFAULT_FLATTENING_LIMIT = 10;

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
	static boolean computeDrawableElementBoundingBox(PathIterator3ai<?> iterator,
			RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
		assert (iterator != null) : "Iterator must not be null"; //$NON-NLS-1$
		assert (box != null) : "Rectangle must not be null"; //$NON-NLS-1$
		GeomFactory3ai<?, ?, ?, ?> factory = iterator.getGeomFactory();
		boolean foundOneLine = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int zmin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		int zmax = Integer.MIN_VALUE;
		PathElement3ai element;
		Path3ai<?, ?, ?, ?, ?, ?> subPath;
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
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						element.getToX(), element.getToY(), element.getToZ());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMinZ()<zmin) zmin = box.getMinZ();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMaxY()>ymax) ymax = box.getMaxY();
					if (box.getMaxZ()>zmax) zmax = box.getMaxZ();
					foundOneLine = true;
				}
				break;
			case QUAD_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getToX(), element.getToY(), element.getToZ());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMinZ()<zmin) zmin = box.getMinZ();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMaxY()>ymax) ymax = box.getMaxY();
					if (box.getMaxZ()>zmax) zmax = box.getMaxZ();
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
	 * {@link #computeDrawableElementBoundingBox(PathIterator3ai, RectangularPrism3ai)}.
	 * 
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a control point was found.
	 * @see #computeDrawableElementBoundingBox(PathIterator3ai, RectangularPrism3ai)
	 */
	static boolean computeControlPointBoundingBox(PathIterator3ai<?> iterator,
			RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		boolean foundOneControlPoint = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int zmin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		int zmax = Integer.MIN_VALUE;
		PathElement3ai element;
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
		if (foundOneControlPoint) {
			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
		}
		else {
			box.clear();
		}
		return foundOneControlPoint;
	}
	
	/**
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param pi is the description of the path.
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
	static int computeCrossingsFromSegment(int crossings, PathIterator3ai<?> pi, int x1, int y1, int z1, int x2, int y2, int z2,
			CrossingComputationType type) {	
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$

		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement3ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int movz = element.getToZ();
		int curx = movx;
		int cury = movy;
		int curz = movz;
		int endx, endy, endz;
		int numCrosses = crossings;
		while (pi.hasNext()) {
			element = pi.next();
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
				numCrosses = Segment3ai.computeCrossingsFromSegment(
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
				Path3ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
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
				Path3ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
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
					numCrosses = Segment3ai.computeCrossingsFromSegment(
							numCrosses,
							x1, y1, z1, x2, y2, z2,
							curx, cury, curz,
							movx, movy, movz);
					if (numCrosses==MathConstants.SHAPE_INTERSECTS)
						return numCrosses;
				}
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
				numCrosses = Segment3ai.computeCrossingsFromSegment(
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
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 * 
	 * @param crossings is the initial value for crossing.
	 * @param pi is the description of the path.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param cz is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param type is the type of special computation to apply. If <code>null</code>, it 
	 * is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int computeCrossingsFromSphere(int crossings, PathIterator3ai<?> pi, int cx, int cy, int cz, int radius,
			CrossingComputationType type) {	
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$
		assert (radius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$

		// Copied from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement3ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int movz = element.getToZ();
		int curx = movx;
		int cury = movy;
		int curz = movz;
		int endx, endy, endz;
		int numCrosses = crossings;
		while (pi.hasNext()) {
			element = pi.next();
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
				numCrosses = Segment3ai.computeCrossingsFromCircle(
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
				Path3ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
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
				Path3ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
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
					numCrosses = Segment3ai.computeCrossingsFromCircle(
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
				numCrosses = Segment3ai.computeCrossingsFromCircle(
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
	 * @param crossings the initial crossing.
	 * @param pi is the description of the path.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param type is the type of special computation to apply. If <code>null</code>, it 
	 * is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	static int computeCrossingsFromPoint(int crossings, PathIterator3ai<?> pi, int px, int py, int pz, CrossingComputationType type) {
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$

		// Copied and adapted from the AWT API
		if (!pi.hasNext()) return 0;
		PathElement3ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int movz = element.getToZ();
		int curx = movx;
		int cury = movy;
		int curz = movz;
		int endx, endy, endz;
		int numCrossings = crossings;

		while (pi.hasNext()) {
			element = pi.next();
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
				numCrossings = Segment3ai.computeCrossingsFromPoint(
						numCrossings,
						px, py, pz,
						curx, cury, curz,
						endx, endy, endz);
				if (numCrossings==MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				Path3ai<?, ?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				curve.quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), endx, endy, endz);
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, pz, CrossingComputationType.STANDARD);
				if (numCrossings==MathConstants.SHAPE_INTERSECTS) {
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
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				curve.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, pz, CrossingComputationType.STANDARD);
				if (numCrossings==MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					numCrossings = Segment3ai.computeCrossingsFromPoint(
							numCrossings,
							px, py, pz,
							curx, cury, curz,
							movx, movy, movz);
					if (numCrossings==MathConstants.SHAPE_INTERSECTS) {
						return numCrossings;
					}
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
				numCrossings = Segment3ai.computeCrossingsFromPoint(
						numCrossings,
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
	 * the path is open and there is not SHAPE_INTERSECT. If <code>true</code> assumes that
	 * the function can only reply <code>0</code> or {@link MathConstants#SHAPE_INTERSECTS}.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	static int computeCrossingsFromPath(
			PathIterator3ai<?> iterator, 
			PathShadow3ai<?> shadow,
			boolean closeable,
			boolean onlyIntersectWhenOpen) {
		assert (iterator != null) : "Iterator must not be null"; //$NON-NLS-1$
		assert (shadow != null) : "The shadow projected on the right must not be null"; //$NON-NLS-1$

		if (!iterator.hasNext()) return 0;

		PathElement3ai pathElement1 = iterator.next();

		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in the first path definition"); //$NON-NLS-1$
		}

		GeomFactory3ai<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3ai<?, ?, ?, ?, ?, ?> subPath;
		int curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement1.getToX();
		cury = movy = pathElement1.getToY();
		curz = movz = pathElement1.getToZ();
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
				movz = curz = pathElement1.getToZ();
				break;
			case LINE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				crossings = shadow.computeCrossings(crossings,
						curx, cury, curz,
						endx, endy, endz);
				if (crossings==MathConstants.SHAPE_INTERSECTS)
					return crossings;
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
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					crossings = shadow.computeCrossings(crossings,
							curx, cury, curz,
							movx, movy, movz);
				}
				// Stop as soon as possible
				if (crossings!=0) return crossings;
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}
		}

		assert(crossings != MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen) {
			if (closeable) {
				// Not closed
				crossings = shadow.computeCrossings(crossings,
						curx, cury, curz,
						movx, movy, movz);
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
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator3ai}.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape3ai} interface to implement support for the
	 * {@link Shape3ai#contains(int, int)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Z coordinate
	 * @return {@code true} if the specified coordinates are inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise
	 */
	static boolean contains(PathIterator3ai<?> pi, int x, int y, int z) {
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$
		// Copied from the AWT API
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int cross = computeCrossingsFromPoint(0, pi, x, y, z, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
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
	 * @param crossings the initial crossing.
	 * @param pi is the iterator on the path elements.
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
			PathIterator3ai<?> pi,
			int rxmin, int rymin, int rzmin,
			int rxmax, int rymax, int rzmax,
			CrossingComputationType type) {
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$
		// Copied from AWT API
		if (!pi.hasNext()) return 0;

		PathElement3ai pathElement = pi.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();
		int numCrossings = crossings;

		while (pi.hasNext()) {
			pathElement = pi.next();
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
				numCrossings = Segment3ai.computeCrossingsFromRect(
						numCrossings,
						rxmin, rymin, rzmin,
						rxmax, rymax, rzmax,
						curx, cury, curz,
						endx, endy, endz);
				if (numCrossings==MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				Path3ai<?, ?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ());
				curve.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), endx, endy, endz);
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rzmin, rxmax, rymax, rzmax,
						CrossingComputationType.STANDARD);
				if (numCrossings==MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ());
				curve.curveTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(), endx, endy, endz);
				numCrossings = computeCrossingsFromRect(
						numCrossings, 
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rzmin, rxmax, rymax, rzmax,
						CrossingComputationType.STANDARD);
				if (numCrossings==MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					numCrossings = Segment3ai.computeCrossingsFromRect(
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
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
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
				numCrossings = Segment3ai.computeCrossingsFromRect(
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

	/**
	 * Tests if the specified rectangle is inside the closed
	 * boundary of the specified {@link PathIterator3ai}.
	 *
	 * <p>The points on the path are assumed to be outside the path area.
	 * It means that is the rectangle is intersecting the path, this
	 * function replies <code>false</code>.
	 *
	 * @param pi the specified {@code PathIterator3ai}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rz the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @param rdepth is the width of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	static boolean contains(PathIterator3ai<?> pi, int rx, int ry, int rz, int rwidth, int rheight, int rdepth) {
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$
		assert (rwidth >= 0) : "Rectangle width must be positive or zero."; //$NON-NLS-1$
		assert (rheight >= 0) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		assert (rdepth >= 0) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		// Copied from AWT API
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
	 * Tests if the interior of the specified {@link PathIterator3ai}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 * <p>
	 * This method provides a basic facility for implementors of
	 * the {@link Shape3ai} interface to implement support for the
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
	 * @param z the specified Z coordinate
	 * @param w the width of the specified rectangular coordinates
	 * @param h the height of the specified rectangular coordinates
	 * @param d the depth of the specified rectangular coordinates
	 * @return {@code true} if the specified {@code PathIterator} and
	 *         the interior of the specified set of rectangular
	 *         coordinates intersect each other; {@code false} otherwise.
	 */
	static boolean intersects(PathIterator3ai<?> pi, int x, int y, int z, int w, int h, int d) {
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$
		assert (w >= 0) : "Rectangle width must be positive or zero."; //$NON-NLS-1$
		assert (h >= 0) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		assert (d >= 0) : "Rectangle height must be positive or zero"; //$NON-NLS-1$

		if (w == 0 || h == 0 || d == 0) {
			return false;
		}
		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromRect(0, pi, x, y, z, x+w, y+h, z+d,
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	/** Replies the point on the path that is closest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2D#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getClosestPointTo(Point3D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param z
	 * @param result the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	static void getClosestPointTo(PathIterator3ai<? extends PathElement3ai> pi, int x, int y, int z, Point3D<?, ?> result) {
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$

		int bestManhantanDist = Integer.MAX_VALUE;
		int bestLinfinvDist = Integer.MAX_VALUE;
		Point3D<?, ?> candidate;
		PathElement3ai pe;

		int mask = (pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1);
		int crossings = 0;
		boolean isClosed = false;
		int moveX, moveY, moveZ, currentX, currentY, currentZ;
		moveX = moveY = moveZ = currentX = currentY = currentZ = 0;

		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			currentX = pe.getToX();
			currentY = pe.getToY();
			currentZ = pe.getToZ();

			switch(pe.getType()) {
			case MOVE_TO:
				moveX = pe.getToX();
				moveY = pe.getToY();
				moveZ = pe.getToZ();
				isClosed = false;
				break;
			case LINE_TO:
			{
				isClosed = false;
				candidate = new InnerComputationPoint3ai();
				Segment3ai.computeClosestPointTo(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ(), x, y, z, candidate);
				if (crossings!=MathConstants.SHAPE_INTERSECTS) {
					crossings = Segment3ai.computeCrossingsFromPoint(
							crossings,
							x, y, z,
							pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
				}
				break;
			}
			case CLOSE:
				isClosed = true;
				if (!pe.isEmpty()) {
					candidate = new InnerComputationPoint3ai();
					Segment3ai.computeClosestPointTo(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ(), x, y, z, candidate);
					if (crossings!=MathConstants.SHAPE_INTERSECTS) {
						crossings = Segment3ai.computeCrossingsFromPoint(
								crossings,
								x, y, z,
								pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
					}
				}
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException();
			}

			if (candidate!=null) {
				int dx = Math.abs(x - candidate.ix());
				int dy = Math.abs(y - candidate.iy());
				int dz = Math.abs(z - candidate.iz());
				int manhatanDist = dx + dy + dz;
				if (manhatanDist <= 0) {
					result.set(candidate);
					return;
				}
				int linfinvDist = MathUtil.min(dx, dy, dz);
				if (manhatanDist < bestManhantanDist || (manhatanDist == bestManhantanDist && linfinvDist < bestLinfinvDist)) {
					bestManhantanDist = manhatanDist;
					bestLinfinvDist = linfinvDist;
					result.set(candidate);
				}
			}
		}

		if (!isClosed && crossings!=MathConstants.SHAPE_INTERSECTS) {
			crossings = Segment3ai.computeCrossingsFromPoint(
					crossings,
					x, y, z,
					currentX, currentY, currentZ,
					moveX, moveY, moveZ);
		}

		if (crossings==MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0) {
			result.set(x, y, z);
		}
	}

	/** Replies the point on the path that is farthest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2D#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getFarthestPointTo(Point3D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param z
	 * @param result the farthest point on the shape.
	 */
	static void getFarthestPointTo(PathIterator3ai<? extends PathElement3ai> pi, int x, int y, int z, Point3D<?, ?> result) {
		assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$

		int bestX = x;
		int bestY = y;
		int bestZ = y;
		int bestManhatanDist = Integer.MIN_VALUE;
		int bestLinfinvDist = Integer.MIN_VALUE;
		PathElement3ai pe;
		Point3D<?, ?> point = new InnerComputationPoint3ai();

		while (pi.hasNext()) {
			pe = pi.next();

			boolean foundCandidate = false;
			int candidateX, candidateY, candidateZ;

			switch(pe.getType()) {
			case MOVE_TO:
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				candidateZ = pe.getToZ();
				break;
			case LINE_TO:
			case CLOSE:
				Segment3ai.computeFarthestPointTo(
						pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ(),
						x, y, z, point);
				foundCandidate = true;
				candidateX = point.ix();
				candidateY = point.iy();
				candidateZ = point.iz();
				break;
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalStateException(
						pe.getType() == null ? null : pe.getType().toString());
			}

			if (foundCandidate) {
				int dx = Math.abs(x - candidateX);
				int dy = Math.abs(y - candidateY);
				int dz = Math.abs(y - candidateY);
				int manhatanDist = dx + dy + dz;
				int linfinvDist = MathUtil.min(dx, dy, dz);
				if ((manhatanDist > bestManhatanDist) || (manhatanDist == bestManhatanDist && linfinvDist < bestLinfinvDist)) {
					bestManhatanDist = manhatanDist;
					bestLinfinvDist = linfinvDist;
					bestX = candidateX;
					bestY = candidateY;
					bestZ = candidateZ;
				}
			}
		}

		result.set(bestX, bestY, bestZ);
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
	default void add(Iterator<? extends PathElement3ai> iterator) {
		assert (iterator != null) : "Iterator must not be null"; //$NON-NLS-1$
		PathElement3ai element;
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
				curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(), element.getToX(), element.getToY(), element.getToZ());
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
	default void set(Path3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Path must not be null"; //$NON-NLS-1$
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
	void moveTo(int x, int y, int z);

	@Override
	default void moveTo(Point3D<?, ?> position) {
		assert (position != null) : "Position must not be null"; //$NON-NLS-1$
		moveTo(position.ix(), position.iy(), position.iz());
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
	void lineTo(int x, int y, int z);

	@Override
	default void lineTo(Point3D<?, ?> to) {
		assert (to != null) : "Position must not be null"; //$NON-NLS-1$
		lineTo(to.ix(), to.iy(), to.iz());
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
	 * @param z2 the Z coordinate of the final end point
	 */
	void quadTo(int x1, int y1, int z1, int x2, int y2, int z2);

	@Override
	default void quadTo(Point3D<?, ?> ctrl, Point3D<?, ?> to) {
		assert (ctrl != null) : "Control point must not be null"; //$NON-NLS-1$
		assert (to != null) : "Destination point must not be null"; //$NON-NLS-1$
		quadTo(ctrl.ix(), ctrl.iy(), ctrl.iz(), to.ix(), to.iy(), to.iz());
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
	void curveTo(int x1, int y1, int z1,
			int x2, int y2, int z2,
			int x3, int y3, int z3);

	@Override
	default void curveTo(Point3D<?, ?> ctrl1, Point3D<?, ?> ctrl2, Point3D<?, ?> to) {
		assert (ctrl1 != null) : "First control point must not be null"; //$NON-NLS-1$
		assert (ctrl2 != null) : "Second control point must not be null"; //$NON-NLS-1$
		assert (to != null) : "Destination point must not be null"; //$NON-NLS-1$
		curveTo(ctrl1.ix(), ctrl1.iy(), ctrl1.iz(), ctrl2.ix(), ctrl2.iy(), ctrl2.iz(), to.ix(), to.iy(), to.iz());
	}

   /* *//**
     * Adds a section of an shallow ellipse to the current path.
     * The ellipse from which a quadrant is taken is the ellipse that would be
     * inscribed in a parallelogram defined by 3 points,
     * The current point which is considered to be the midpoint of the edge
     * leading into the corner of the ellipse where the ellipse grazes it,
     * {@code (ctrlx, ctrly)} which is considered to be the location of the
     * corner of the parallelogram in which the ellipse is inscribed,
     * and {@code (tox, toy)} which is considered to be the midpoint of the
     * edge leading away from the corner of the oval where the oval grazes it.
     * 
     * <p><img src="../doc-files/arcto0.png"/>
     *
     * <p>Only the portion of the ellipse from {@code tfrom} to {@code tto}
     * will be included where {@code 0f} represents the point where the
     * ellipse grazes the leading edge, {@code 1f} represents the point where
     * the oval grazes the trailing edge, and {@code 0.5f} represents the
     * point on the oval closest to the control point.
     * The two values must satisfy the relation
     * {@code (0 <= tfrom <= tto <= 1)}.
     * 
     * <p>If {@code tfrom} is not {@code 0f} then the caller would most likely
     * want to use one of the arc {@code type} values that inserts a segment
     * leading to the initial point.
     * An initial {@link #moveTo(int, int, int)} or {@link #lineTo(int, int, int)} can be added to direct
     * the path to the starting point of the ellipse section if
     * {@link org.arakhne.afc.math.geometry.d3.Path3D.ArcType#MOVE_THEN_ARC} or
     * {@link org.arakhne.afc.math.geometry.d3.Path3D.ArcType#LINE_THEN_ARC} are
     * specified by the type argument.
     * The {@code lineTo} path segment will only be added if the current point
     * is not already at the indicated location to avoid spurious empty line
     * segments.
     * The type can be specified as
     * {@link ArcType#CORNER_ONLY} if the current point
     * on the path is known to be at the starting point of the ellipse section.
     *
     * @param ctrlx the x coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed. 
     * @param ctrly the y coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed. 
     * @param ctrlz the z coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed. 
     * @param tox the x coordinate of the target point.
     * @param toy the y coordinate of the target point.
     * @param toz the z coordinate of the target point.
     * @param tfrom the fraction of the ellipse section where the curve should start.
     * @param tto the fraction of the ellipse section where the curve should end
     * @param type the specification of what additional path segments should
     *               be appended to lead the current path to the starting point.
     *//*
    default void arcTo(int ctrlx, int ctrly, int ctrlz, int tox, int toy, int toz, double tfrom, double tto, ArcType type) {
    	// Copied from JavaFX Path2D
    	assert (tfrom >= 0.) : "tfrom must be positive or zero"; //$NON-NLS-1$
    	assert (tto >= tfrom) : "tto must be greater than or equal to tfrom"; //$NON-NLS-1$
    	assert (tto <= 1.) : "tto must be lower than 1"; //$NON-NLS-1$
    	int currentx = getCurrentX();
    	int currenty = getCurrentY();
    	int currentz = getCurrentZ();
    	final int ocurrentx = currentx;
    	final int ocurrenty = currenty;
    	final int ocurrentz = currenty;
        double cx0 = currentx + (ctrlx - currentx) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
        double cy0 = currenty + (ctrly - currenty) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
        double cz0 = currentz + (ctrlz - currentz) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
        double cx1 = tox + (ctrlx - tox) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
        double cy1 = toy + (ctrly - toy) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
        double cz1 = toz + (ctrlz - toz) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
        if (tto < 1.) {
            double t = 1. - tto;
            tox += (cx1 - tox) * t;
            toy += (cy1 - toy) * t;
            cx1 += (cx0 - cx1) * t;
            cy1 += (cy0 - cy1) * t;
            cx0 += (currentx - cx0) * t;
            cy0 += (currenty - cy0) * t;
            tox += (cx1 - tox) * t;
            toy += (cy1 - toy) * t;
            cx1 += (cx0 - cx1) * t;
            cy1 += (cy0 - cy1) * t;
            tox += (cx1 - tox) * t;
            toy += (cy1 - toy) * t;
        }
        if (tfrom > 0.) {
            if (tto < 1.) {
                tfrom = tfrom / tto;
            }
            currentx += (cx0 - currentx) * tfrom;
            currenty += (cy0 - currenty) * tfrom;
            cx0 += (cx1 - cx0) * tfrom;
            cy0 += (cy1 - cy0) * tfrom;
            cx1 += (tox - cx1) * tfrom;
            cy1 += (toy - cy1) * tfrom;
            currentx += (cx0 - currentx) * tfrom;
            currenty += (cy0 - currenty) * tfrom;
            cx0 += (cx1 - cx0) * tfrom;
            cy0 += (cy1 - cy0) * tfrom;
            currentx += (cx0 - currentx) * tfrom;
            currenty += (cy0 - currenty) * tfrom;
        }
        if (type == ArcType.MOVE_THEN_ARC) {
            if (currentx != ocurrentx || currenty != ocurrenty) {
            	moveTo(currentx, currenty, currentz);
            }
        } else if (type == ArcType.LINE_THEN_ARC) {
            if (currentx != ocurrentx || currenty != ocurrenty) {
            	lineTo(currentx, currenty, currentz);
            }
        }
        if (tfrom == tto ||
            (currentx == cx0 && cx0 == cx1 && cx1 == tox &&
            currenty == cy0 && cy0 == cy1 && cy1 == toy)) {
            if (type != ArcType.LINE_THEN_ARC) {
                lineTo(tox, toy, toz);
            }
        } else {
            curveTo((int) Math.round(cx0), (int) Math.round(cy0), (int) Math.round(cz0), (int) Math.round(cx1), (int) Math.round(cy1), (int) Math.round(cz1), tox, toy, toz);
        }
    }
    
    @Override
    default void arcTo(Point3D<?, ?> ctrl, Point3D<?, ?> to, double tfrom, double tto,
    		org.arakhne.afc.math.geometry.d3.Path3D.ArcType type) {
    	assert (ctrl != null) : "Control point must be not null"; //$NON-NLS-1$
    	assert (to != null) : "Target point must be not null"; //$NON-NLS-1$
    	arcTo(ctrl.ix(), ctrl.iy(), ctrl.iz(), to.ix(), to.iy(), to.iz(), tfrom, tto, type);
    }
    
    *//**
     * Adds a section of an shallow ellipse to the current path.
     * 
     * <p>This function is equivalent to:<pre><code>
     * this.arcTo(ctrlx, ctrly, tox, toy, 0.0, 1.0, ArcType.ARCONLY);
     * </code></pre>
     *
     * @param ctrlx the x coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed. 
     * @param ctrly the y coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed. 
     * @param ctrlz the z coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed. 
     * @param to the x coordinate of the target point.
     * @param to the y coordinate of the target point.
     * @param to the y coordinate of the target point.
     *//*
    default void arcTo(int ctrlx, int ctrly, int ctrlz, int tox, int toy, int toz) {
    	arcTo(ctrlx, ctrly, ctrlz, tox, toy, toz, 0., 1., ArcType.ARC_ONLY);
    }

    @Override
    default void arcTo(Point3D<?, ?> to, Vector3D<?, ?> radii, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
    	assert (radii != null) : "Radii vector must be not null"; //$NON-NLS-1$
    	assert (to != null) : "Target point must be not null"; //$NON-NLS-1$
    	arcTo(to.ix(), to.iy(), to.iz(), radii.ix(), radii.iy(), radii.iz(), xAxisRotation, largeArcFlag, sweepFlag);
    }

    *//**
     * Adds a section of an shallow ellipse to the current path.
     * The ellipse from which the portions are extracted follows the rules:
     * <ul>
     * <li>The ellipse will have its X axis tilted from horizontal by the
     * angle {@code xAxisRotation} specified in radians.</li>
     * <li>The ellipse will have the X and Y radii (viewed from its tilted
     * coordinate system) specified by {@code radiusx} and {@code radiusy}
     * unless that ellipse is too small to bridge the gap from the current
     * point to the specified destination point in which case a larger
     * ellipse with the same ratio of dimensions will be substituted instead.</li>
     * <li>The ellipse may slide perpendicular to the direction from the
     * current point to the specified destination point so that it just
     * touches the two points.
     * The direction it slides (to the "left" or to the "right") will be
     * chosen to meet the criteria specified by the two boolean flags as
     * described below.
     * Only one direction will allow the method to meet both criteria.</li>
     * <li>If the {@code largeArcFlag} is true, then the ellipse will sweep
     * the longer way around the ellipse that meets these criteria.</li>
     * <li>If the {@code sweepFlag} is true, then the ellipse will sweep
     * clockwise around the ellipse that meets these criteria.</li>
     * </ul>
     * 
     * <p><img src="../doc-files/arcto1.png" />
     *
     * <p>The method will do nothing if the destination point is the same as
     * the current point.
     * The method will draw a simple line segment to the destination point
     * if either of the two radii are zero.
     *
     * @param tox the X coordinate of the target point.
     * @param toy the Y coordinate of the target point.
     * @param toz the Z coordinate of the target point.
     * @param radiusx the X radius of the tilted ellipse.
     * @param radiusy the Y radius of the tilted ellipse.
     * @param radiusz the Z radius of the tilted ellipse.
     * @param xAxisRotation the angle of tilt of the ellipse.
     * @param largeArcFlag <code>true</code> iff the path will sweep the long way around the ellipse.
     * @param sweepFlag <code>true</code> iff the path will sweep clockwise around the ellipse.
     * @see "http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands"
     *//*
    default void arcTo(int tox, int toy, int toz, int radiusx, int radiusy, int radiusz, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
    	// Copied for JavaFX
    	assert (radiusx >= 0.) : "X radius must be positive or zero."; //$NON-NLS-1$
    	assert (radiusy >= 0.) : "Y radius must be positive or zero."; //$NON-NLS-1$
        if (radiusx == 0. || radiusy == 0.) {
            lineTo(tox, toy, toz);
            return;
        }
        int ocurrentx = getCurrentX();
        int ocurrenty = getCurrentY();
        int ocurrentz = getCurrentZ();
        int x1 = ocurrentx;
        int y1 = ocurrenty;
        int z1 = ocurrentz;
        int x2 = tox;
        int y2 = toy;
        int z2 = toz;
        if (x1 == x2 && y1 == y2) {
            return;
        }
        double cosphi, sinphi;
        if (xAxisRotation == 0.) {
            cosphi = 1.;
            sinphi = 0.;
        } else {
            cosphi = Math.cos(xAxisRotation);
            sinphi = Math.sin(xAxisRotation);
        }
        double mx = (x1 + x2) / 2.;
        double my = (y1 + y2) / 2.;
        double mz = (z1 + z2) / 2.;
        double relx1 = x1 - mx;
        double rely1 = y1 - my;
        double x1p = (cosphi * relx1 + sinphi * rely1) / radiusx;
        double y1p = (cosphi * rely1 - sinphi * relx1) / radiusy;
        double lenpsq = x1p * x1p + y1p * y1p;
        if (lenpsq >= 1.) {
            double xqpr = y1p * radiusx;
            double yqpr = x1p * radiusy;
            if (sweepFlag) {
            	xqpr = -xqpr;
            } else {
            	yqpr = -yqpr;
            }
            double relxq = cosphi * xqpr - sinphi * yqpr;
            double relyq = cosphi * yqpr + sinphi * xqpr;
            double relzq = 0;								// FIXME : incorrect value
            int xq = (int) Math.round(mx + relxq);
            int yq = (int) Math.round(my + relyq);
            int zq = (int) Math.round(mz + relzq);
            double xc = x1 + relxq;
            double yc = y1 + relyq;
            double zc = z1 + relzq;
            if (x1 != ocurrentx || y1 != ocurrenty) {
            	lineTo(x1, y1, z1);
            }
            arcTo((int) Math.round(xc), (int) Math.round(yc), (int) Math.round(zc), xq, yq, zq, 0, 1, ArcType.ARC_ONLY);
            xc = x2 + relxq;
            yc = y2 + relyq;
            arcTo((int) Math.round(xc), (int) Math.round(yc), (int) Math.round(zc), x2, y2, z2, 0, 1, ArcType.ARC_ONLY);
            return;
        }
        double scalef = Math.sqrt((1. - lenpsq) / lenpsq);
        double cxp = scalef * y1p;
        double cyp = scalef * x1p;
        if (largeArcFlag == sweepFlag) {
        	cxp = -cxp;
        } else {
        	cyp = -cyp;
        }
        mx += (cosphi * cxp * radiusx - sinphi * cyp * radiusy);
        my += (cosphi * cyp * radiusy + sinphi * cxp * radiusx);
        double ux = x1p - cxp;
        double uy = y1p - cyp;
        double vx = -(x1p + cxp);
        double vy = -(y1p + cyp);
        boolean done = false;
        double quadlen = 1.;
        boolean wasclose = false;
        do {
            double xqp = uy;
            double yqp = ux;
            if (sweepFlag) {
            	xqp = -xqp;
            } else {
            	yqp = -yqp;
            }
            if (xqp * vx + yqp * vy > 0.) {
                double dot = ux * vx + uy * vy;
                if (dot >= 0) {
                    quadlen = Math.acos(dot) / MathConstants.DEMI_PI;
                    done = true;
                }
                wasclose = true;
            } else if (wasclose) {
                break;
            }
            double relxq = (cosphi * xqp * radiusx - sinphi * yqp * radiusy);
            double relyq = (cosphi * yqp * radiusy + sinphi * xqp * radiusx);
            double relzq = 0;				// FIXME : incorrect value
            int xq = (int) Math.round(mx + relxq);
            int yq = (int) Math.round(my + relyq);
            int zq = (int) Math.round(mz + relzq);
            double xc = x1 + relxq;
            double yc = y1 + relyq;
            double zc = z1 + relzq;
            arcTo((int) Math.round(xc), (int) Math.round(yc), (int) Math.round(zc), xq, yq, zq, 0, quadlen, ArcType.ARC_ONLY);
            x1 = xq;
            y1 = yq;
            ux = xqp;
            uy = yqp;
        } while (!done);
    }*/

    @Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		Point3D<?, ?> c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		Point3D<?, ?> c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		Point3D<?, ?> c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
	}

	@Override
	default P getClosestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		getClosestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), p.ix(), p.iy(), p.iz(), point);
		return point;
	}

	@Override
	default P getFarthestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		getFarthestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), p.ix(), p.iy(), p.iz(), point);
		return point;
	}

	@Override
	default double getLengthSquared() {
		if (isEmpty()) return 0;

		double length = 0;

		PathIterator3ai<?> pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		PathElement3ai pathElement = pi.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		while (pi.hasNext()) {
			pathElement = pi.next();

			switch (pathElement.getType()) {
			case LINE_TO:
				length += Point3D.getDistanceSquaredPointPoint(
						pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ(), 
						pathElement.getToX(), pathElement.getToY(), pathElement.getToZ());
				break;
			case CLOSE:
				length += Point3D.getDistanceSquaredPointPoint(
						pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ(),
						pathElement.getToX(), pathElement.getToY(), pathElement.getToZ());
				break;
			case QUAD_TO:
			case CURVE_TO:
				throw new IllegalStateException("Curve in path is not supported"); //$NON-NLS-1$
			case MOVE_TO: 
			default:
			}

		}

		return length;
	}
	
	@Pure
	@Override
	default P getCurrentPoint() {
		return getGeomFactory().newPoint(getCurrentX(), getCurrentY(), getCurrentZ());
	}
	
	/** Replies the x coordinate of the last point in the path.
	 *
	 * @return the x coordinate of the last point in the path.
	 */
	@Pure
	int getCurrentX();
	
	/** Replies the x coordinate of the last point in the path.
	 *
	 * @return the x coordinate of the last point in the path.
	 */
	@Pure
	int getCurrentY();

	/** Replies the z coordinate of the last point in the path.
	 *
	 * @return the z coordinate of the last point in the path.
	 */
	@Pure
	int getCurrentZ();

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
	void setLastPoint(int x, int y, int z);

	@Override
	default void setLastPoint(Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		setLastPoint(point.ix(), point.iy(), point.iz());
	}

	/** Transform the current path.
	 * This function changes the current path.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform3D transform);

	@Pure
	@Override
	default boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
		assert (box != null) : "Rectangle must not be null"; //$NON-NLS-1$
		return contains(getPathIterator(),
				box.getMinX(), box.getMinY(), box.getMinZ(), box.getWidth(), box.getHeight(), box.getHeight());
	}

	@Override
	default boolean contains(int x, int y, int z) {
		return contains(getPathIterator(), x, y, z);
	}

	/** Remove the point with the given coordinates.
	 * 
	 * <p>If the given coordinates do not match exactly a point in the path, nothing is removed.
	 * 
	 * @param x the x coordinate of the point to remove.
	 * @param y the y coordinate of the point to remove.
	 * @param z the z coordinate of the point to remove.
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(int x, int y, int z);

	@Pure
	@Override
	default PathIterator3ai<IE> getPathIterator(double flatness) {
		return new FlatteningPathIterator<>(this, getPathIterator(null), flatness, DEFAULT_FLATTENING_LIMIT);
	}

	@Pure
	@Override
	default PathIterator3ai<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new PathPathIterator<>(this);
		}
		return new TransformedPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default Iterator<P> getPointIterator() {
		PathIterator3ai<IE> pathIterator = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		return new PixelIterator<>(pathIterator, getGeomFactory());
	}

	@Pure
	@Override
	default boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must not be null"; //$NON-NLS-1$
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
	default boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return intersects(getPathIterator(), s.getMinX(), s.getMinY(), s.getMinZ(), s.getWidth(), s.getHeight(), s.getDepth());
	}

	@Pure
	@Override
	default boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must not be null"; //$NON-NLS-1$
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromSegment(
				0,
				getPathIterator(),
				s.getX1(), s.getY1(), s.getZ1(), s.getX2(), s.getY2(), s.getZ2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	default boolean intersects(PathIterator3ai<?> iterator) {
		assert (iterator != null) : "Iterator must not be null"; //$NON-NLS-1$
		int mask = (getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = computeCrossingsFromPath(
				iterator,
				new PathShadow3ai<>(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	@Pure
	@Override
	default boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default Collection<P> toCollection() {
		return new PointCollection<>(this);
	}
	
	/** Private API for Path3ai.
	 * 
	 * @author $Author: sgalland$
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
				int rxmin, int rymin, int rzmin,
				int rxmax, int rymax, int rzmax,
				int curx, int cury, int curz,
				int movx, int movy, int movz,
				boolean intersectingBehavior) {
			int crosses = Segment3ai.computeCrossingsFromRect(crossings,
					rxmin, rymin, rzmin,
					rxmax, rymax, rzmax,
					curx, cury, curz,
					movx, movy, movz);
			if (!intersectingBehavior && crosses==MathConstants.SHAPE_INTERSECTS) {
				int x1 = rxmin+1;
				int x2 = rxmax-1;
				int y1 = rymin+1;
				int y2 = rymax-1;
				int z1 = rzmin+1;
				int z2 = rzmax-1;
				crosses = Segment3ai.computeCrossingsFromRect(crossings,
						x1, y1, z1,
						x2, y2, z2,
						curx, cury, curz,
						movx, movy, movz);
			}
			return crosses;
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
	abstract class AbstractPathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {

		/** Path.
		 */
		protected final Path3ai<?, ?, E, ?, ?, ?> path;

		/**
		 * @param path the path.
		 */
		public AbstractPathIterator(Path3ai<?, ?, E, ?, ?, ?> path) {
			assert (path != null) : "Path must not be null"; //$NON-NLS-1$
			this.path = path;
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
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
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
	class PathPathIterator<E extends PathElement3ai> extends AbstractPathIterator<E> {

		private final Point3D<?, ?> p1;

		private final Point3D<?, ?> p2;

		private int iType = 0;

		private int iCoord = 0;

		private int movex;

		private int movey;

		private int movez;

		/**
		 * @param path the path.
		 */
		public PathPathIterator(Path3ai<?, ?, E, ?, ?, ?> path) {
			super(path);
			this.p1 = new InnerComputationPoint3ai();
			this.p2 = new InnerComputationPoint3ai();
		}
		
		@Override
		public PathIterator3ai<E> restartIterations() {
			return new PathPathIterator<>(this.path);
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
				this.movez = this.path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case LINE_TO:
				if (this.iCoord+2>(this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				element = getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case QUAD_TO:
			{
				if (this.iCoord+4>(this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				int ctrlx = this.path.getCoordAt(this.iCoord++);
				int ctrly = this.path.getCoordAt(this.iCoord++);
				int ctrlz = this.path.getCoordAt(this.iCoord++);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						ctrlx, ctrly, ctrlz,
						this.p2.ix(), this.p2.iy(), this.p2.iz());
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
				int ctrlz1 = this.path.getCoordAt(this.iCoord++);
				int ctrlx2 = this.path.getCoordAt(this.iCoord++);
				int ctrly2 = this.path.getCoordAt(this.iCoord++);
				int ctrlz2 = this.path.getCoordAt(this.iCoord++);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						ctrlx1, ctrly1, ctrlz1,
						ctrlx2, ctrly2, ctrlz2,
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newClosePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
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
	class TransformedPathIterator<E extends PathElement3ai> extends AbstractPathIterator<E> {

		private final Transform3D transform;

		private final Point3D<?, ?> p1;

		private final Point3D<?, ?> p2;

		private final Point3D<?, ?> ptmp1;

		private final Point3D<?, ?> ptmp2;

		private int iType = 0;

		private int iCoord = 0;

		private int movex, movey, movez;

		/**
		 * @param path the path.
		 * @param transform the transformation.
		 */
		public TransformedPathIterator(Path3ai<?, ?, E, ?, ?, ?> path, Transform3D transform) {
			super(path);
			assert(transform != null) : "Transformation must not be null"; //$NON-NLS-1$
			this.transform = transform;
			this.p1 = new InnerComputationPoint3ai();
			this.p2 = new InnerComputationPoint3ai();
			this.ptmp1 = new InnerComputationPoint3ai();
			this.ptmp2 = new InnerComputationPoint3ai();
		}
		
		@Override
		public PathIterator3ai<E> restartIterations() {
			return new TransformedPathIterator<>(this.path, this.transform);
		}

		@Override
		public boolean hasNext() {
			return this.iType < this.path.getPathElementCount();
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
				this.movez = this.path.getCoordAt(this.iCoord++);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.ptmp1.ix(), this.ptmp1.iy(), this.ptmp1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.ptmp2);
				this.p2.set(
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++),
						this.path.getCoordAt(this.iCoord++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.ptmp1.ix(), this.ptmp1.iy(), this.ptmp1.iz(),
						this.ptmp2.ix(), this.ptmp2.iy(), this.ptmp2.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newClosePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p1.iz());
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
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PixelIterator<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final PathIterator3ai<?> pathIterator;

		private final GeomFactory3ai<?, P, V, ?> factory;

		private Iterator<P> lineIterator = null;

		private P next = null;

		/**
		 * @param pi the iterator.
		 * @param factory the element factory.
		 */
		public PixelIterator(PathIterator3ai<?> pi, GeomFactory3ai<?, P, V, ?> factory) {
			assert (pi != null) : "Iterator must not be null"; //$NON-NLS-1$
			assert (factory != null) : "Factory must not be null"; //$NON-NLS-1$
			this.pathIterator = pi;
			this.factory = factory;
			searchNext();
		}

		private void searchNext() {
			P old = this.next;
			this.next = null;
			while (this.pathIterator.hasNext() && (this.lineIterator==null || !this.lineIterator.hasNext())) {
				this.lineIterator = null;
				PathElement3ai elt = this.pathIterator.next();
				if (elt.isDrawable()) {
					switch(elt.getType()) {
					case LINE_TO:
					case CLOSE:
						Segment3ai<?, ?, ?, P, V, ?> segment = this.factory.newSegment(
								elt.getFromX(), elt.getFromY(), elt.getFromZ(),
								elt.getToX(), elt.getToY(), elt.getToZ());
						this.lineIterator = segment.getPointIterator();
						break;
					case MOVE_TO:
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
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointCollection<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Collection<P> {

		private final Path3ai<?, ?, ?, P, V, ?> path;
		
		/**
		 * @param path the path from which the points are extracted.
		 */
		public PointCollection(Path3ai<?, ?, ?, P, V, ?> path) {
			assert (path != null) : "Path must not be null"; //$NON-NLS-1$
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
			if (o instanceof Point3D) {
				return this.path.contains((Point3D<?, ?>) o);
			}
			return false;
		}

		@Override
		public Iterator<P> iterator() {
			return new PointIterator<>(this.path);
		}

		@Override
		public Object[] toArray() {
			return this.path.toPointArray();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			assert (a != null) : "Array must not be null"; //$NON-NLS-1$
			Iterator<P> iterator = new PointIterator<>(this.path);
			for(int i=0; i<a.length && iterator.hasNext(); ++i) {
				a[i] = (T)iterator.next();
			}
			return a;
		}

		@Override
		public boolean add(P e) {
			if (e != null) {
				if (this.path.size()==0) {
					this.path.moveTo(e.ix(), e.iy(), e.iz());
				}
				else {
					this.path.lineTo(e.ix(), e.iy(), e.iz());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point3D) {
				Point3D<?, ?> p = (Point3D<?, ?>) o;
				return this.path.remove(p.ix(), p.iy(), p.iz());
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			assert (c != null) : "Collection must not be null"; //$NON-NLS-1$
			for(Object obj : c) {
				if ((!(obj instanceof Point3D))
						||(!this.path.contains((Point3D<?, ?>) obj))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends P> c) {
			assert (c != null) : "Collection must not be null"; //$NON-NLS-1$
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
			assert (c != null) : "Collection must not be null"; //$NON-NLS-1$
			boolean changed = false;
			for(Object obj : c) {
				if (obj instanceof Point3D) {
					Point3D<?, ?> pts = (Point3D<?, ?>) obj;
					if (this.path.remove(pts.ix(), pts.iy(), pts.iz())) {
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
	class PointIterator<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final Path3ai<?, ?, ?, P, V, ?> path;
		
		private int index = 0;
		
		private P lastReplied = null;

		/**
		 * @param path the path to iterate on.
		 */
		public PointIterator(Path3ai<?, ?, ?, P, V, ?> path) {
			assert (path != null) : "Path must not be null"; //$NON-NLS-1$
			this.path = path;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.path.size();
		}

		@Override
		public P next() {
			try {
				this.lastReplied = this.path.getPointAt(this.index++);
				return this.lastReplied;
			} catch(Throwable exception) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			Point3D<?, ?> p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			this.path.remove(p.ix(), p.iy(), p.iz());
		}

	}

	/** A path iterator that is flattening the path.
	 * This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	// TODO : integrate z coordinate
	class FlatteningPathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {

		/** Path.
		 */
		private final Path3ai<?, ?, E, ?, ?, ?> path;
		
		/** The source iterator.
		 */
		private final PathIterator3ai<? extends E> pathIterator;

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
		
		/** The ending y of the last segment.
		 */
		private double currentZ;

		/** The x of the last move segment.
		 */
		private double moveX;

		/** The y of the last move segment.
		 */
		private double moveY;

		/** The y of the last move segment.
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
		private int lastNextX;
		
		/** The y of the last move segment replied by next.
		 */
		private int lastNextY;

		/** The z of the last move segment replied by next.
		 */
		private int lastNextZ;

		/**
		 * @param path is the path.
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 *     control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions
		 *     allowed for any curved segment
		 */
		public FlatteningPathIterator(Path3ai<?, ?, E, ?, ?, ?> path, PathIterator3ai<? extends E> pathIterator,
				double flatness, int limit) {
			assert (path != null) : "Path must not be null"; //$NON-NLS-1$
			assert (pathIterator != null) : "Iterator must not be null"; //$NON-NLS-1$
			assert (flatness > 0f) : "Flatness factor must be positive."; //$NON-NLS-1$
			assert (limit >= 0) : "Number of recursive subdivisions must be positive or zero."; //$NON-NLS-1$
			this.path = path;
			this.pathIterator = pathIterator;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit;
			this.levels = new int[limit + 1];
			searchNext(true);
		}

		@Override
		public PathIterator3ai<E> restartIterations() {
			return new FlatteningPathIterator<>(this.path, this.pathIterator.restartIterations(),
					Math.sqrt(this.squaredFlatness), this.limit);
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
		// TODO : validate indexes
		private static double getQuadSquaredFlatness(double coords[], int offset) {
			return Segment3afp.computeDistanceSquaredLinePoint(
					coords[offset + 0], coords[offset + 1], coords[offset + 2],
					coords[offset + 6], coords[offset + 7], coords[offset + 8],
					coords[offset + 3], coords[offset + 4], coords[offset + 5]);
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
		// TODO : integrate z coordinate
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
		// TODO : validate indexes
		private static double getCurveSquaredFlatness(double coords[], int offset) {
			return Math.max(
					Segment3afp.computeDistanceSquaredSegmentPoint(
							coords[offset + 9],
							coords[offset + 10],
							coords[offset + 11],
							coords[offset + 3],
							coords[offset + 4],
							coords[offset + 5],
							coords[offset + 0],
							coords[offset + 1],
							coords[offset + 2]),							
					Segment3afp.computeDistanceSquaredSegmentPoint(
							coords[offset + 9],
							coords[offset + 10],
							coords[offset + 11],
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 8],
							coords[offset + 0],
							coords[offset + 1],
							coords[offset + 2]));
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
		// TODO : integrate z coordinate
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
			int x, y, z;
			if (type==PathElementType.CLOSE) {
				x = (int) Math.round(this.moveX);
				y = (int) Math.round(this.moveY);
				z = (int) Math.round(this.moveZ);
			}
			else {
				x = (int) Math.round(this.hold[this.holdIndex + 0]);
				y = (int) Math.round(this.hold[this.holdIndex + 1]);
				z = (int) Math.round(this.hold[this.holdIndex + 2]);
			}
			return x==this.lastNextX && y==this.lastNextY && z==this.lastNextZ;
		}

		// TODO : integrate z coordinate
		private void flattening() {
			int level;

			if (this.holdIndex >= this.holdEnd) {
				if (!this.pathIterator.hasNext()) {
					this.done = true;
					return;
				}
				PathElement3ai pathElement = this.pathIterator.next();
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

		@Override
		public boolean hasNext() {
			return !this.done;
		}

		@Override
		public E next() {
			if (this.done) {
				throw new NoSuchElementException("flattening iterator out of bounds"); //$NON-NLS-1$
			}

			E element;
			PathElementType type = this.holdType;
			if (type!=PathElementType.CLOSE) {
				int x = (int) Math.round(this.hold[this.holdIndex + 0]);
				int y = (int) Math.round(this.hold[this.holdIndex + 1]);
				int z = (int) Math.round(this.hold[this.holdIndex + 2]);
				if (type == PathElementType.MOVE_TO) {
					element = this.path.getGeomFactory().newMovePathElement(x, y, z);
				}
				else {
					element = this.path.getGeomFactory().newLinePathElement(
							this.lastNextX, this.lastNextY, this.lastNextZ,
							x, y, z);
				}
				this.lastNextX = x;
				this.lastNextY = y;
				this.lastNextY = z;
			}
			else {
				int x = (int) Math.round(this.moveX);
				int y = (int) Math.round(this.moveY);
				int z = (int) Math.round(this.moveZ);
				element = this.path.getGeomFactory().newClosePathElement(
						this.lastNextX, this.lastNextY, this.lastNextZ,
						x, y, z);
				this.lastNextX = x;
				this.lastNextY = y;
				this.lastNextZ = z;
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
			return this.path.getWindingRule();
		}
		
		@Override
		public boolean isPolyline() {
			return !isMultiParts() && !isPolygon();
		}

		@Override
		public boolean isCurved() {
			// Because the iterator flats the path, this is no curve inside.
			return false;
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
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
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

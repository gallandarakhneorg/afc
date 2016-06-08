/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d2.ai;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.arakhne.afc.math.geometry.d2.PathIterator2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp.AbstractCirclePathIterator;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

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
public interface Path2ai<
		ST extends Shape2ai<?, ?, IE, P, V, B>,
		IT extends Path2ai<?, ?, IE, P, V, B>,
		IE extends PathElement2ai,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, IE, P, V, B>>
		extends Shape2ai<ST, IT, IE, P, V, B>, Path2D<ST, IT, PathIterator2ai<IE>, P, V, B> {

	/** Multiple of cubic & quad curve size.
	 */
	int GROW_SIZE = 24;

	/** Default depth for the flattening of the path.
	 */
	int DEFAULT_FLATTENING_LIMIT = 10;

	/** The default winding rule: {@link PathWindingRule#NON_ZERO}.
	 */
	PathWindingRule DEFAULT_WINDING_RULE = PathWindingRule.NON_ZERO;

	/** Compute the box that corresponds to the drawable elements of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element.
	 *
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a drawable element was found.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static boolean computeDrawableElementBoundingBox(PathIterator2ai<?> iterator,
			Rectangle2ai<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		assert box != null : AssertMessages.notNullParameter(1);
		final GeomFactory2ai<?, ?, ?, ?> factory = iterator.getGeomFactory();
		boolean foundOneLine = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		PathElement2ai element;
		Path2ai<?, ?, ?, ?, ?, ?> subPath;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case LINE_TO:
				if (element.getFromX() < xmin) {
					xmin = element.getFromX();
				}
				if (element.getFromY() < ymin) {
					ymin = element.getFromY();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
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
					if (box.getMinX() < xmin) {
						xmin = box.getMinX();
					}
					if (box.getMinY() < ymin) {
						ymin = box.getMinY();
					}
					if (box.getMaxX() > xmax) {
						xmax = box.getMaxX();
					}
					if (box.getMaxY() > ymax) {
						ymax = box.getMaxY();
					}
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
					if (box.getMinX() < xmin) {
						xmin = box.getMinX();
					}
					if (box.getMinY() < ymin) {
						ymin = box.getMinY();
					}
					if (box.getMaxX() > xmax) {
						xmax = box.getMaxX();
					}
					if (box.getMaxY() > ymax) {
						ymax = box.getMaxY();
					}
					foundOneLine = true;
				}
				break;
			case ARC_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.arcTo(
						element.getToX(), element.getToY(),
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX() < xmin) {
						xmin = box.getMinX();
					}
					if (box.getMinY() < ymin) {
						ymin = box.getMinY();
					}
					if (box.getMaxX() > xmax) {
						xmax = box.getMaxX();
					}
					if (box.getMaxY() > ymax) {
						ymax = box.getMaxY();
					}
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
		} else {
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
	 * {@link #computeDrawableElementBoundingBox(PathIterator2ai, Rectangle2ai)}.
	 *
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a control point was found.
	 * @see #computeDrawableElementBoundingBox(PathIterator2ai, Rectangle2ai)
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static boolean computeControlPointBoundingBox(PathIterator2ai<?> iterator,
			Rectangle2ai<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		assert box != null : AssertMessages.notNullParameter(1);
		boolean foundOneControlPoint = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		PathElement2ai element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case LINE_TO:
				if (element.getFromX() < xmin) {
					xmin = element.getFromX();
				}
				if (element.getFromY() < ymin) {
					ymin = element.getFromY();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
				foundOneControlPoint = true;
				break;
			case CURVE_TO:
				if (element.getFromX() < xmin) {
					xmin = element.getFromX();
				}
				if (element.getFromY() < ymin) {
					ymin = element.getFromY();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getCtrlX1() < xmin) {
					xmin = element.getCtrlX1();
				}
				if (element.getCtrlY1() < ymin) {
					ymin = element.getCtrlY1();
				}
				if (element.getCtrlX1() > xmax) {
					xmax = element.getCtrlX1();
				}
				if (element.getCtrlY1() > ymax) {
					ymax = element.getCtrlY1();
				}
				if (element.getCtrlX2() < xmin) {
					xmin = element.getCtrlX2();
				}
				if (element.getCtrlY2() < ymin) {
					ymin = element.getCtrlY2();
				}
				if (element.getCtrlX2() > xmax) {
					xmax = element.getCtrlX2();
				}
				if (element.getCtrlY2() > ymax) {
					ymax = element.getCtrlY2();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
				foundOneControlPoint = true;
				break;
			case QUAD_TO:
				if (element.getFromX() < xmin) {
					xmin = element.getFromX();
				}
				if (element.getFromY() < ymin) {
					ymin = element.getFromY();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getCtrlX1() < xmin) {
					xmin = element.getCtrlX1();
				}
				if (element.getCtrlY1() < ymin) {
					ymin = element.getCtrlY1();
				}
				if (element.getCtrlX1() > xmax) {
					xmax = element.getCtrlX1();
				}
				if (element.getCtrlY1() > ymax) {
					ymax = element.getCtrlY1();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
				foundOneControlPoint = true;
				break;
			case ARC_TO:
				if (element.getFromX() < xmin) {
					xmin = element.getFromX();
				}
				if (element.getFromY() < ymin) {
					ymin = element.getFromY();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
				foundOneControlPoint = true;
				break;
			case MOVE_TO:
			case CLOSE:
			default:
			}
		}
		if (foundOneControlPoint) {
			box.setFromCorners(xmin, ymin, xmax, ymax);
		} else {
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
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static int computeCrossingsFromSegment(int crossings, PathIterator2ai<?> pi, int x1, int y1, int x2, int y2,
			CrossingComputationType type) {
		assert pi != null : AssertMessages.notNullParameter(1);

		// Copied from the AWT API
		if (!pi.hasNext()) {
			return 0;
		}
		PathElement2ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int curx = movx;
		int cury = movy;
		int endx;
		int endy;
		int numCrosses = crossings;
		while (pi.hasNext()) {
			element = pi.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = element.getToX();
				curx = movx;
				movy = element.getToY();
				cury = movy;
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = Segment2ai.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, x2, y2,
						curx, cury,
						endx, endy);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						CrossingComputationType.STANDARD);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						CrossingComputationType.STANDARD);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.arcTo(
						endx, endy,
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						CrossingComputationType.STANDARD);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
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
					if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert numCrosses != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				numCrosses = Segment2ai.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, x2, y2,
						curx, cury,
						movx, movy);
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
	 * @param radius is the radius of the circle.
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:magicnumber"})
	static int computeCrossingsFromCircle(int crossings, PathIterator2ai<?> pi, int cx, int cy, int radius,
			CrossingComputationType type) {
		assert pi != null : AssertMessages.notNullParameter(1);
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(4);

		// Copied from the AWT API
		if (!pi.hasNext()) {
			return 0;
		}
		PathElement2ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int curx = movx;
		int cury = movy;
		int endx;
		int endy;
		int numCrosses = crossings;
		while (pi.hasNext()) {
			element = pi.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = element.getToX();
				curx = movx;
				movy = element.getToY();
				cury = movy;
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrosses = Segment2ai.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, radius,
						curx, cury,
						endx, endy);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.arcTo(
						endx, endy,
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				numCrosses = computeCrossingsFromCircle(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
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
					if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert numCrosses != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Auto close
				numCrosses = Segment2ai.computeCrossingsFromCircle(
						numCrosses,
						cx, cy, radius,
						curx, cury,
						movx, movy);
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
	 * crosses the ray extending to the right from (px, py).
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
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity",
			"checkstyle:returncount"})
	static int computeCrossingsFromPoint(int crossings, PathIterator2ai<?> pi, int px, int py, CrossingComputationType type) {
		assert pi != null : AssertMessages.notNullParameter(1);

		// Copied and adapted from the AWT API
		if (!pi.hasNext()) {
			return 0;
		}
		PathElement2ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int curx = movx;
		int cury = movy;
		int endx;
		int endy;
		int numCrossings = crossings;

		while (pi.hasNext()) {
			element = pi.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = element.getToX();
				curx = movx;
				movy = element.getToY();
				cury = movy;
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				numCrossings = Segment2ai.computeCrossingsFromPoint(
						numCrossings,
						px, py,
						curx, cury,
						endx, endy);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2ai<?, ?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY());
				curve.quadTo(element.getCtrlX1(), element.getCtrlY1(), endx, endy);
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY());
				curve.arcTo(
						endx, endy,
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrossings = Segment2ai.computeCrossingsFromPoint(
							numCrossings,
							px, py,
							curx, cury,
							movx, movy);
					if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
						return numCrossings;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert numCrossings != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				if (movx == px && movy == py) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				numCrossings = Segment2ai.computeCrossingsFromPoint(
						numCrossings,
						px, py,
						curx, cury,
						movx, movy);
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
	 * The caller must check r[xy]{min, max} for NaN values.
	 *
     * @param crossings the initial crossing.
	 * @param iterator is the iterator on the path elements.
	 * @param shadow is the description of the shape to project to the right.
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static int computeCrossingsFromPath(
	        int crossings,
			PathIterator2ai<?> iterator,
			BasicPathShadow2ai shadow,
			CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		assert shadow != null : AssertMessages.notNullParameter(2);

		if (!iterator.hasNext()) {
			return 0;
		}

		PathElement2ai pathElement1 = iterator.next();

		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		final GeomFactory2ai<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2ai<?, ?, ?, ?, ?, ?> subPath;
		int curx = pathElement1.getToX();
		int movx = curx;
		int cury = pathElement1.getToY();
		int movy = cury;
		int numCrossings = crossings;
		int endx;
		int endy;

		while (numCrossings != MathConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement1 = iterator.next();
			switch (pathElement1.getType()) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert (crossings & 1 != 0);
				movx = pathElement1.getToX();
				curx = movx;
				movy = pathElement1.getToY();
				cury = movy;
				break;
			case LINE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				numCrossings = shadow.computeCrossings(numCrossings,
						curx, cury,
						endx, endy);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				break;
			case ARC_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				// only for local use
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.arcTo(
						endx, endy,
						pathElement1.getRadiusX(), pathElement1.getRadiusY(),
						pathElement1.getRotationX(), pathElement1.getLargeArcFlag(),
						pathElement1.getSweepFlag());
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
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					numCrossings = shadow.computeCrossings(numCrossings,
							curx, cury,
							movx, movy);
				}
				// Stop as soon as possible
				if (numCrossings != 0) {
					return numCrossings;
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert numCrossings != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = shadow.computeCrossings(numCrossings,
						curx, cury,
						movx, movy);
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
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator2ai}.
	 *
	 * <p>This method provides a basic facility for implementors of
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
		assert pi != null : AssertMessages.notNullParameter(0);
		// Copied from the AWT API
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		final int cross = computeCrossingsFromPoint(0, pi, x, y, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (cross & mask) != 0;
	}

	/**
	 * Tests if the specified rectangle is inside the closed
	 * boundary of the specified {@link PathIterator2ai}.
	 *
	 * <p>The points on the path are assumed to be outside the path area.
	 * It means that is the rectangle is intersecting the path, this
	 * function replies <code>false</code>.
	 *
	 * @param pi the specified {@code PathIterator2ai}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	static boolean contains(PathIterator2ai<?> pi, int rx, int ry, int rwidth, int rheight) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert rwidth >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert rheight >= 0 : AssertMessages.positiveOrZeroParameter(4);
		// Copied from AWT API
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = computeCrossingsFromRect(
				0,
				pi,
				rx, ry, rx + rwidth, ry + rheight,
				CrossingComputationType.AUTO_CLOSE);
		return crossings != MathConstants.SHAPE_INTERSECTS
				&& (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean contains(Rectangle2ai<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		return contains(getPathIterator(),
				box.getMinX(), box.getMinY(), box.getWidth(), box.getHeight());
	}

	@Override
	default boolean contains(int x, int y) {
		return contains(getPathIterator(), x, y);
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
	 * The caller must check r[xy]{min, max} for NaN values.
	 *
	 * @param crossings the initial crossing.
	 * @param pi is the iterator on the path elements.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static int computeCrossingsFromRect(
			int crossings,
			PathIterator2ai<?> pi,
			int rxmin, int rymin,
			int rxmax, int rymax,
			CrossingComputationType type) {
		assert pi != null : AssertMessages.notNullParameter(1);
		// Copied from AWT API
		if (!pi.hasNext()) {
			return 0;
		}

		PathElement2ai pathElement = pi.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		int curx = pathElement.getToX();
		int movx = curx;
		int cury = pathElement.getToY();
		int movy = cury;
		int numCrossings = crossings;
		int endx;
		int endy;

		while (pi.hasNext()) {
			pathElement = pi.next();
			switch (pathElement.getType()) {
			case MOVE_TO:
				// Count should always be a multiple of 2 here.
				// assert (crossings & 1 != 0);
				movx = pathElement.getToX();
				curx = movx;
				movy = pathElement.getToY();
				cury = movy;
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				numCrossings = Segment2ai.computeCrossingsFromRect(
						numCrossings,
						rxmin, rymin,
						rxmax, rymax,
						curx, cury,
						endx, endy);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				Path2ai<?, ?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(pathElement.getFromX(), pathElement.getFromY());
				curve.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), endx, endy);
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rxmax, rymax,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(pathElement.getFromX(), pathElement.getFromY());
				curve.curveTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlX2(),
						pathElement.getCtrlY2(), endx, endy);
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rxmax, rymax,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(pathElement.getFromX(), pathElement.getFromY());
				curve.arcTo(endx, endy, pathElement.getRadiusX(), pathElement.getRadiusY(),
						pathElement.getRotationX(), pathElement.getLargeArcFlag(), pathElement.getSweepFlag());
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rxmax, rymax,
						CrossingComputationType.STANDARD);
				if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					numCrossings = Segment2ai.computeCrossingsFromRect(
							numCrossings,
							rxmin, rymin,
							rxmax, rymax,
							curx, cury,
							movx, movy);
				}
				// Stop as soon as possible
				if (numCrossings != 0) {
					return numCrossings;
				}
				curx = movx;
				cury = movy;
				// Count should always be a multiple of 2 here.
				// assert (crossings & 1 != 0);
				break;
			default:
			}
		}

		assert numCrossings != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = Segment2ai.computeCrossingsFromRect(
						numCrossings,
						rxmin, rymin,
						rxmax, rymax,
						curx, cury,
						movx, movy);
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
	 * Tests if the interior of the specified {@link PathIterator2ai}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 *
	 * <p>This method provides a basic facility for implementors of
	 * the {@link Shape2ai} interface to implement support for the
	 * {@code intersects()} method.
	 *
	 * <p>This method object may conservatively return true in
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
	 * @param width the width of the specified rectangular coordinates
	 * @param height the height of the specified rectangular coordinates
	 * @return {@code true} if the specified {@code PathIterator} and
	 *         the interior of the specified set of rectangular
	 *         coordinates intersect each other; {@code false} otherwise.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	static boolean intersects(PathIterator2ai<?> pi, int x, int y, int width, int height) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(4);

		if (width == 0 || height == 0) {
			return false;
		}
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = computeCrossingsFromRect(0, pi, x, y, x + width, y + height,
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = computeCrossingsFromCircle(
				0,
				getPathIterator(),
				circle.getX(), circle.getY(), circle.getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		return intersects(getPathIterator(), rectangle.getMinX(), rectangle.getMinY(),
				rectangle.getWidth(), rectangle.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = computeCrossingsFromSegment(
				0,
				getPathIterator(),
				segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Override
	default boolean intersects(PathIterator2ai<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = computeCrossingsFromPath(
		        0, iterator,
				new BasicPathShadow2ai(this),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	/** Replies the point on the path that is closest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2D#isPolyline()} of {@code pi} is replying
	 * <code>true</code>.
	 * {@link #getClosestPointTo(Point2D)} avoids this restriction.
	 *
	 * @param pi is the iterator on the elements of the path.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param result the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static void getClosestPointTo(PathIterator2ai<? extends PathElement2ai> pi, int x, int y, Point2D<?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);

		int bestManhantanDist = Integer.MAX_VALUE;
		int bestLinfinvDist = Integer.MAX_VALUE;
		Point2D<?, ?> candidate;
		PathElement2ai pe;

		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		int crossings = 0;
		boolean isClosed = false;
		int moveX = 0;
		int moveY = 0;
		int currentX = 0;
		int currentY = 0;

		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			currentX = pe.getToX();
			currentY = pe.getToY();

			switch (pe.getType()) {
			case MOVE_TO:
				moveX = pe.getToX();
				moveY = pe.getToY();
				isClosed = false;
				break;
			case LINE_TO:
				isClosed = false;
				candidate = new InnerComputationPoint2ai();
				Segment2ai.computeClosestPointToPoint(pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(), x, y, candidate);
				if (crossings != MathConstants.SHAPE_INTERSECTS) {
					crossings = Segment2ai.computeCrossingsFromPoint(
							crossings,
							x, y,
							pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				}
				break;
			case CLOSE:
				isClosed = true;
				if (!pe.isEmpty()) {
					candidate = new InnerComputationPoint2ai();
					Segment2ai.computeClosestPointToPoint(pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(),
					        x, y, candidate);
					if (crossings != MathConstants.SHAPE_INTERSECTS) {
						crossings = Segment2ai.computeCrossingsFromPoint(
								crossings,
								x, y,
								pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
					}
				}
				break;
			case QUAD_TO:
			case CURVE_TO:
			case ARC_TO:
			default:
				throw new IllegalStateException();
			}

			if (candidate != null) {
				final int dx = Math.abs(x - candidate.ix());
				final int dy = Math.abs(y - candidate.iy());
				final int manhatanDist = dx + dy;
				if (manhatanDist <= 0) {
					result.set(candidate);
					return;
				}
				final int linfinvDist = Math.min(dx, dy);
				if (manhatanDist < bestManhantanDist || (manhatanDist == bestManhantanDist && linfinvDist < bestLinfinvDist)) {
					bestManhantanDist = manhatanDist;
					bestLinfinvDist = linfinvDist;
					result.set(candidate);
				}
			}
		}

		if (!isClosed && crossings != MathConstants.SHAPE_INTERSECTS) {
			crossings = Segment2ai.computeCrossingsFromPoint(
					crossings,
					x, y,
					currentX, currentY,
					moveX, moveY);
		}

		if (crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0) {
			result.set(x, y);
		}
	}

	/** Replies the point on the path of pi that is closest to the given shape.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator2D#isCurved()} of {@code pi} is replying
	 * <code>false</code>.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param shape the shape to which the closest point must be computed.
	 * @param result the closest point on pi.
	 * @return <code>true</code> if a point was found. Otherwise <code>false</code>.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	@Unefficient
	static boolean getClosestPointTo(PathIterator2ai<? extends PathElement2ai> pi,
	        PathIterator2ai<? extends PathElement2ai> shape, Point2D<?, ?> result) {
	    assert pi != null : AssertMessages.notNullParameter(0);
	    assert shape != null : AssertMessages.notNullParameter(1);
	    assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
	    assert result != null : AssertMessages.notNullParameter(2);
	    if (!pi.hasNext() || !shape.hasNext()) {
	        return false;
	    }
	    PathElement2ai pathElement1 = pi.next();
	    if (pathElement1.getType() != PathElementType.MOVE_TO) {
	        throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
	    }
	    if (shape.next().getType() != PathElementType.MOVE_TO) {
	        throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
	    }
	    if (!pi.hasNext() || !shape.hasNext()) {
	        return false;
	    }
	    final Rectangle2ai<?, ?, ?, ?, ?, ?> box = pi.getGeomFactory().newBox();
	    computeDrawableElementBoundingBox(shape.restartIterations(), box);
	    final ClosestPointPathShadow2ai shadow = new ClosestPointPathShadow2ai(shape.restartIterations(), box);
	    int crossings = 0;
	    int curx = pathElement1.getToX();
	    int movx = curx;
	    int cury = pathElement1.getToY();
	    int movy = cury;
	    int endx;
	    int endy;
	    while (pi.hasNext()) {
	        pathElement1 = pi.next();
	        switch (pathElement1.getType()) {
	        case MOVE_TO:
	            movx = pathElement1.getToX();
	            curx = movx;
	            movy = pathElement1.getToY();
	            cury = movy;
	            break;
	        case LINE_TO:
	            endx = pathElement1.getToX();
	            endy = pathElement1.getToY();
	            crossings = shadow.computeCrossings(crossings, curx, cury, endx, endy);
	            if (crossings == MathConstants.SHAPE_INTERSECTS) {
	                result.set(shadow.getClosestPointInOtherShape());
	                return true;
	            }
	            curx = endx;
	            cury = endy;
	            break;
	        case CLOSE:
	            if (curx != movx || cury != movy) {
	                crossings = shadow.computeCrossings(crossings, curx, cury, movx, movy);
	                if (crossings == MathConstants.SHAPE_INTERSECTS) {
	                    result.set(shadow.getClosestPointInOtherShape());
	                    return true;
	                }
	            }
	            curx = movx;
	            cury = movy;
	            break;
	        case QUAD_TO:
	        case CURVE_TO:
	        case ARC_TO:
	        default:
	            throw new IllegalArgumentException();
	        }
	    }
	    if (curx == movx && cury == movy) {
	        assert crossings != MathConstants.SHAPE_INTERSECTS;
	        final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
	        if ((crossings & mask) != 0) {
	            // Second path is inside the first shape
	            result.set(shadow.getClosestPointInShadowShape());
	            return true;
	        }
	    }
	    result.set(shadow.getClosestPointInOtherShape());
	    return true;
	}

	@Override
	default P getClosestPointTo(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		getClosestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), pt.ix(), pt.iy(), point);
		return point;
	}

	@Override
	default P getClosestPointTo(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Path2ai<?, ?, ?, ?, ?, ?> path) {
		throw new UnsupportedOperationException();
	}

	/** Replies the point on the path that is farthest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator2D#isPolyline()} of {@code pi} is replying
	 * <code>true</code>.
	 * {@link #getFarthestPointTo(Point2D)} avoids this restriction.
	 *
	 * @param pi is the iterator on the elements of the path.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param result the farthest point on the shape.
	 */
	static void getFarthestPointTo(PathIterator2ai<? extends PathElement2ai> pi, int x, int y, Point2D<?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);

		int bestX = x;
		int bestY = y;
		int bestManhatanDist = Integer.MIN_VALUE;
		int bestLinfinvDist = Integer.MIN_VALUE;
		PathElement2ai pe;
		final Point2D<?, ?> point = new InnerComputationPoint2ai();

		while (pi.hasNext()) {
			pe = pi.next();

			boolean foundCandidate = false;
			final int candidateX;
			final int candidateY;

			switch (pe.getType()) {
			case MOVE_TO:
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				break;
			case LINE_TO:
			case CLOSE:
				Segment2ai.computeFarthestPointToPoint(
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(),
						x, y, point);
				foundCandidate = true;
				candidateX = point.ix();
				candidateY = point.iy();
				break;
			case QUAD_TO:
			case CURVE_TO:
			case ARC_TO:
			default:
				throw new IllegalStateException(
						pe.getType() == null ? null : pe.getType().toString());
			}

			if (foundCandidate) {
				final int dx = Math.abs(x - candidateX);
				final int dy = Math.abs(y - candidateY);
				final int manhatanDist = dx + dy;
				final int linfinvDist = Math.min(dx, dy);
				if ((manhatanDist > bestManhatanDist) || (manhatanDist == bestManhatanDist && linfinvDist < bestLinfinvDist)) {
					bestManhatanDist = manhatanDist;
					bestLinfinvDist = linfinvDist;
					bestX = candidateX;
					bestY = candidateY;
				}
			}
		}

		result.set(bestX, bestY);
	}

	@Override
	default P getFarthestPointTo(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		getFarthestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), pt.ix(), pt.iy(), point);
		return point;
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
	 * @param iterator the iterator on the elements to add.
	 */
	default void add(Iterator<? extends PathElement2ai> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		PathElement2ai element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
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
				curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlX2(),
						element.getCtrlY2(), element.getToX(), element.getToY());
				break;
			case ARC_TO:
				arcTo(element.getToX(), element.getToY(), element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(), element.getSweepFlag());
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
	 * @param path the path to copy.
	 */
	default void set(Path2ai<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		clear();
		add(path.getPathIterator());
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
	default void moveTo(Point2D<?, ?> position) {
		assert position != null : AssertMessages.notNullParameter();
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
	default void lineTo(Point2D<?, ?> to) {
		assert to != null : AssertMessages.notNullParameter();
		lineTo(to.ix(), to.iy());
	}

	/**
	 * Adds a curved segment, defined by two new points, to the path by
	 * drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x2, y2)},
	 * using the specified point {@code (x1, y1)} as a quadratic
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
	default void quadTo(Point2D<?, ?> ctrl, Point2D<?, ?> to) {
		assert ctrl != null : AssertMessages.notNullParameter(0);
		assert to != null : AssertMessages.notNullParameter(1);
		quadTo(ctrl.ix(), ctrl.iy(), to.ix(), to.iy());
	}

	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x3, y3)},
	 * using the specified points {@code (x1, y1)} and {@code (x2, y2)} as
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
	default void curveTo(Point2D<?, ?> ctrl1, Point2D<?, ?> ctrl2, Point2D<?, ?> to) {
		assert ctrl1 != null : AssertMessages.notNullParameter(0);
		assert ctrl2 != null : AssertMessages.notNullParameter(1);
		assert to != null : AssertMessages.notNullParameter(2);
		curveTo(ctrl1.ix(), ctrl1.iy(), ctrl2.ix(), ctrl2.iy(), to.ix(), to.iy());
	}

	/**
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
	 * An initial {@link #moveTo(int, int)} or {@link #lineTo(int, int)} can be added to direct
	 * the path to the starting point of the ellipse section if
	 * {@link org.arakhne.afc.math.geometry.d2.Path2D.ArcType#MOVE_THEN_ARC} or
	 * {@link org.arakhne.afc.math.geometry.d2.Path2D.ArcType#LINE_THEN_ARC} are
	 * specified by the type argument.
	 * The {@code lineTo} path segment will only be added if the current point
	 * is not already at the indicated location to avoid spurious empty line
	 * segments.
	 * The type can be specified as
	 * {@link org.arakhne.afc.math.geometry.d2.Path2D.ArcType#ARC_ONLY} if the current point
	 * on the path is known to be at the starting point of the ellipse section.
	 *
	 * @param ctrlx the x coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param ctrly the y coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param tox the x coordinate of the target point.
	 * @param toy the y coordinate of the target point.
	 * @param tfrom the fraction of the ellipse section where the curve should start.
	 * @param tto the fraction of the ellipse section where the curve should end
	 * @param type the specification of what additional path segments should
	 *               be appended to lead the current path to the starting point.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:magicnumber"})
	default void arcTo(int ctrlx, int ctrly, int tox, int toy, double tfrom, double tto, ArcType type) {
		// Copied from JavaFX Path2D
		assert tfrom >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert tto >= tfrom : AssertMessages.lowerEqualParameters(4, tfrom, 5, tto);
		assert tto <= 1. : AssertMessages.lowerEqualParameter(5, tto, 1);
		int currentx = getCurrentX();
		int currenty = getCurrentY();
		final int ocurrentx = currentx;
		final int ocurrenty = currenty;
		int targetx = tox;
		int targety = toy;
		double realtfrom = tfrom;
		double cx0 = currentx + (ctrlx - currentx) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
		double cy0 = currenty + (ctrly - currenty) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
		double cx1 = targetx + (ctrlx - targetx) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
		double cy1 = targety + (ctrly - targety) * AbstractCirclePathIterator.CTRL_POINT_DISTANCE;
		if (tto < 1.) {
			final double t = 1. - tto;
			targetx += (cx1 - targetx) * t;
			targety += (cy1 - targety) * t;
			cx1 += (cx0 - cx1) * t;
			cy1 += (cy0 - cy1) * t;
			cx0 += (currentx - cx0) * t;
			cy0 += (currenty - cy0) * t;
			targetx += (cx1 - targetx) * t;
			targety += (cy1 - targety) * t;
			cx1 += (cx0 - cx1) * t;
			cy1 += (cy0 - cy1) * t;
			targetx += (cx1 - targetx) * t;
			targety += (cy1 - targety) * t;
		}
		if (realtfrom > 0.) {
			if (tto < 1.) {
				realtfrom = realtfrom / tto;
			}
			currentx += (cx0 - currentx) * realtfrom;
			currenty += (cy0 - currenty) * realtfrom;
			cx0 += (cx1 - cx0) * realtfrom;
			cy0 += (cy1 - cy0) * realtfrom;
			cx1 += (targetx - cx1) * realtfrom;
			cy1 += (targety - cy1) * realtfrom;
			currentx += (cx0 - currentx) * realtfrom;
			currenty += (cy0 - currenty) * realtfrom;
			cx0 += (cx1 - cx0) * realtfrom;
			cy0 += (cy1 - cy0) * realtfrom;
			currentx += (cx0 - currentx) * realtfrom;
			currenty += (cy0 - currenty) * realtfrom;
		}
		if (type == ArcType.MOVE_THEN_ARC && (currentx != ocurrentx || currenty != ocurrenty)) {
			moveTo(currentx, currenty);
		} else if (type == ArcType.LINE_THEN_ARC && (currentx != ocurrentx || currenty != ocurrenty)) {
			lineTo(currentx, currenty);
		}
		if (realtfrom == tto
				|| (currentx == cx0 && cx0 == cx1 && cx1 == targetx
				&& currenty == cy0 && cy0 == cy1 && cy1 == targety)) {
			if (type != ArcType.LINE_THEN_ARC) {
				lineTo(targetx, targety);
			}
		} else {
			curveTo((int) Math.round(cx0), (int) Math.round(cy0), (int) Math.round(cx1), (int) Math.round(cy1), targetx, targety);
		}
	}

	@Override
	default void arcTo(Point2D<?, ?> ctrl, Point2D<?, ?> to, double tfrom, double tto,
			org.arakhne.afc.math.geometry.d2.Path2D.ArcType type) {
		assert ctrl != null : AssertMessages.notNullParameter(0);
		assert to != null : AssertMessages.notNullParameter(1);
		arcTo(ctrl.ix(), ctrl.iy(), to.ix(), to.iy(), tfrom, tto, type);
	}

	/**
	 * Adds a section of an shallow ellipse to the current path.
	 *
	 * <p>This function is equivalent to:<pre><code>
	 * this.arcTo(ctrlx, ctrly, tox, toy, 0.0, 1.0, ArcType.ARCONLY);
	 * </code></pre>
	 *
	 * @param ctrlx the x coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param ctrly the y coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param tox the x coordinate of the target point.
	 * @param toy the y coordinate of the target point.
	 */
	default void arcTo(int ctrlx, int ctrly, int tox, int toy) {
		arcTo(ctrlx, ctrly, tox, toy, 0., 1., ArcType.ARC_ONLY);
	}

	@Override
	default void arcTo(Point2D<?, ?> to, Vector2D<?, ?> radii, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
		assert radii != null : AssertMessages.notNullParameter(1);
		assert to != null : AssertMessages.notNullParameter(0);
		arcTo(to.ix(), to.iy(), radii.ix(), radii.iy(), xAxisRotation, largeArcFlag, sweepFlag);
	}

	/**
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
	 * @param radiusx the X radius of the tilted ellipse.
	 * @param radiusy the Y radius of the tilted ellipse.
	 * @param xAxisRotation the angle of tilt of the ellipse.
	 * @param largeArcFlag <code>true</code> iff the path will sweep the long way around the ellipse.
	 * @param sweepFlag <code>true</code> iff the path will sweep clockwise around the ellipse.
	 * @see "http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands"
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity",
			"checkstyle:parameternumber"})
	default void arcTo(int tox, int toy, int radiusx, int radiusy, double xAxisRotation,
			boolean largeArcFlag, boolean sweepFlag) {
		// Copied for JavaFX
		assert radiusx >= 0. : AssertMessages.positiveOrZeroParameter(2);
		assert radiusy >= 0. : AssertMessages.positiveOrZeroParameter(3);
		if (radiusx == 0. || radiusy == 0.) {
			lineTo(tox, toy);
			return;
		}
		final int ocurrentx = getCurrentX();
		final int ocurrenty = getCurrentY();
		int x1 = ocurrentx;
		int y1 = ocurrenty;
		final  int x2 = tox;
		final int y2 = toy;
		if (x1 == x2 && y1 == y2) {
			return;
		}
		final  double cosphi;
		final double sinphi;
		if (xAxisRotation == 0.) {
			cosphi = 1.;
			sinphi = 0.;
		} else {
			cosphi = Math.cos(xAxisRotation);
			sinphi = Math.sin(xAxisRotation);
		}
		double mx = (x1 + x2) / 2.;
		double my = (y1 + y2) / 2.;
		final double relx1 = x1 - mx;
		final double rely1 = y1 - my;
		final double x1p = (cosphi * relx1 + sinphi * rely1) / radiusx;
		final double y1p = (cosphi * rely1 - sinphi * relx1) / radiusy;
		final double lenpsq = x1p * x1p + y1p * y1p;
		if (lenpsq >= 1.) {
			double xqpr = y1p * radiusx;
			double yqpr = x1p * radiusy;
			if (sweepFlag) {
				xqpr = -xqpr;
			} else {
				yqpr = -yqpr;
			}
			final double relxq = cosphi * xqpr - sinphi * yqpr;
			final double relyq = cosphi * yqpr + sinphi * xqpr;
			final int xq = (int) Math.round(mx + relxq);
			final int yq = (int) Math.round(my + relyq);
			double xc = x1 + relxq;
			double yc = y1 + relyq;
			if (x1 != ocurrentx || y1 != ocurrenty) {
				lineTo(x1, y1);
			}
			arcTo((int) Math.round(xc), (int) Math.round(yc), xq, yq, 0, 1, ArcType.ARC_ONLY);
			xc = x2 + relxq;
			yc = y2 + relyq;
			arcTo((int) Math.round(xc), (int) Math.round(yc), x2, y2, 0, 1, ArcType.ARC_ONLY);
			return;
		}
		final double scalef = Math.sqrt((1. - lenpsq) / lenpsq);
		double cxp = scalef * y1p;
		double cyp = scalef * x1p;
		if (largeArcFlag == sweepFlag) {
			cxp = -cxp;
		} else {
			cyp = -cyp;
		}
		mx += cosphi * cxp * radiusx - sinphi * cyp * radiusy;
		my += cosphi * cyp * radiusy + sinphi * cxp * radiusx;
		double ux = x1p - cxp;
		double uy = y1p - cyp;
		final double vx = -(x1p + cxp);
		final double vy = -(y1p + cyp);
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
				final double dot = ux * vx + uy * vy;
				if (dot >= 0) {
					quadlen = Math.acos(dot) / MathConstants.DEMI_PI;
					done = true;
				}
				wasclose = true;
			} else if (wasclose) {
				break;
			}
			final double relxq = cosphi * xqp * radiusx - sinphi * yqp * radiusy;
			final  double relyq = cosphi * yqp * radiusy + sinphi * xqp * radiusx;
			final  int xq = (int) Math.round(mx + relxq);
			final int yq = (int) Math.round(my + relyq);
			final double xc = x1 + relxq;
			final double yc = y1 + relyq;
			arcTo((int) Math.round(xc), (int) Math.round(yc), xq, yq, 0, quadlen, ArcType.ARC_ONLY);
			x1 = xq;
			y1 = yq;
			ux = xqp;
			uy = yqp;
		} while (!done);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final Point2D<?, ?> c = getClosestPointTo(pt);
		return c.getDistanceSquared(pt);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final Point2D<?, ?> c = getClosestPointTo(pt);
		return c.getDistanceL1(pt);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final Point2D<?, ?> c = getClosestPointTo(pt);
		return c.getDistanceLinf(pt);
	}

	@Override
	default double getLengthSquared() {
		if (isEmpty()) {
			return 0;
		}

		double length = 0;

		final PathIterator2ai<?> pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		PathElement2ai pathElement = pi.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		while (pi.hasNext()) {
			pathElement = pi.next();

			switch (pathElement.getType()) {
			case LINE_TO:
				length += Point2D.getDistanceSquaredPointPoint(
						pathElement.getFromX(), pathElement.getFromY(),
						pathElement.getToX(), pathElement.getToY());
				break;
			case CLOSE:
				length += Point2D.getDistanceSquaredPointPoint(
						pathElement.getFromX(), pathElement.getFromY(),
						pathElement.getToX(), pathElement.getToY());
				break;
			case QUAD_TO:
			case CURVE_TO:
			case ARC_TO:
				throw new IllegalStateException();
			case MOVE_TO:
			default:
			}

		}

		return length;
	}

	@Pure
	@Override
	default P getCurrentPoint() {
		return getGeomFactory().newPoint(getCurrentX(), getCurrentY());
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

	/** Replies the coordinate at the given index.
	 * The index is in [0;{@link #size()}*2).
	 *
	 * @param index the index.
	 * @return the coordinate at the given index.
	 */
	@Pure
	int getCoordAt(int index);

	/** Change the coordinates of the last inserted point.
	 *
	 * @param x the new x coordinate of the last point.
	 * @param y the new y coordinate of the last point.
	 */
	void setLastPoint(int x, int y);

	@Override
	default void setLastPoint(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		setLastPoint(point.ix(), point.iy());
	}

	/** Transform the current path.
	 * This function changes the current path.
	 *
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform2D transform);

	/** Remove the point with the given coordinates.
	 *
	 * <p>If the given coordinates do not match exactly a point in the path, nothing is removed.
	 *
	 * @param x the x coordinate of the point to remove.
	 * @param y the y coordinate of the point to remove.
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(int x, int y);

	@Pure
	@Override
	default PathIterator2ai<IE> getPathIterator(double flatness) {
		return new FlatteningPathIterator<>(getPathIterator(null), flatness, DEFAULT_FLATTENING_LIMIT);
	}

	@Pure
	@Override
	default PathIterator2ai<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new PathPathIterator<>(this);
		}
		return new TransformedPathIterator<>(this, transform);
	}

    /** Replies a path iterator on this shape that is replacing the
     * curves by line approximations.
     *
     * @return the iterator on the approximation.
     * @see #getPathIterator()
     * @see MathConstants#SPLINE_APPROXIMATION_RATIO
     */
    @Pure
    default PathIterator2ai<IE> getFlatteningPathIterator() {
        return new FlatteningPathIterator<>(
                getPathIterator(null),
                MathConstants.SPLINE_APPROXIMATION_RATIO,
                Path2ai.DEFAULT_FLATTENING_LIMIT);
    }

    @Pure
    @Override
	default Iterator<P> getPointIterator() {
		final PathIterator2ai<IE> pathIterator = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		return new PixelIterator<>(pathIterator, getGeomFactory());
	}

	@Pure
	@Override
	default Collection<P> toCollection() {
		return new PointCollection<>(this);
	}

	/** Private API for Path2ai.
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
		 * @param rxmin x coordinate of the minimum corner.
		 * @param rymin y coordinate of the minimum corner.
		 * @param rxmax x coordinate of the maximum corner.
		 * @param rymax y coordinate of the maximum corner.
		 * @param curx x coordinate of the current point.
		 * @param cury y coordinate of the current point.
		 * @param movx x coordinate of the last move.
		 * @param movy y coordinate of the last mobe.
		 * @param intersectingBehavior <code>true</code> if the expected behavior is intersection,
		 *     <code>false</code> for simple crossing computation.
		 * @return thr crossing.
		 */
		@Pure
		@SuppressWarnings("checkstyle:parameternumber")
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
			if (!intersectingBehavior && crosses == MathConstants.SHAPE_INTERSECTS) {
				final int x1 = rxmin + 1;
				final int x2 = rxmax - 1;
				final int y1 = rymin + 1;
				final int y2 = rymax - 1;
				crosses = Segment2ai.computeCrossingsFromRect(crossings,
						x1, y1,
						x2, y2,
						curx, cury,
						movx, movy);
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
	abstract class AbstractPathIterator<E extends PathElement2ai> implements PathIterator2ai<E> {

		/** Path.
		 */
		protected final Path2ai<?, ?, E, ?, ?, ?> path;

		/**
		 * @param path the path.
		 */
		public AbstractPathIterator(Path2ai<?, ?, E, ?, ?, ?> path) {
			assert path != null : AssertMessages.notNullParameter();
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
		public GeomFactory2ai<E, ?, ?, ?> getGeomFactory() {
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
	@SuppressWarnings("checkstyle:magicnumber")
	class PathPathIterator<E extends PathElement2ai> extends AbstractPathIterator<E> {

		private final Point2D<?, ?> p1;

		private final Point2D<?, ?> p2;

		private int typeIndex;

		private int coordIndex;

		private int movex;

		private int movey;

		/**
		 * @param path the path.
		 */
		public PathPathIterator(Path2ai<?, ?, E, ?, ?, ?> path) {
			super(path);
			this.p1 = new InnerComputationPoint2ai();
			this.p2 = new InnerComputationPoint2ai();
		}

		@Override
		public PathIterator2ai<E> restartIterations() {
			return new PathPathIterator<>(this.path);
		}

		@Override
		public boolean hasNext() {
			return this.typeIndex < this.path.getPathElementCount();
		}

		@Override
		public E next() {
			final int type = this.typeIndex;
			if (this.typeIndex >= this.path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			E element = null;
			switch (this.path.getPathElementTypeAt(type)) {
			case MOVE_TO:
				if ((this.coordIndex + 2) > (this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.movex = this.path.getCoordAt(this.coordIndex++);
				this.movey = this.path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey);
				element = getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy());
				break;
			case LINE_TO:
				if ((this.coordIndex + 2) > (this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				element = getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case QUAD_TO:
				if ((this.coordIndex + 4) > (this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				final int ctrlx = this.path.getCoordAt(this.coordIndex++);
				final int ctrly = this.path.getCoordAt(this.coordIndex++);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						ctrlx, ctrly,
						this.p2.ix(), this.p2.iy());
				break;
			case CURVE_TO:
				if (this.coordIndex + 6 > (this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				final int ctrlx1 = this.path.getCoordAt(this.coordIndex++);
				final int ctrly1 = this.path.getCoordAt(this.coordIndex++);
				final int ctrlx2 = this.path.getCoordAt(this.coordIndex++);
				final int ctrly2 = this.path.getCoordAt(this.coordIndex++);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.p2.ix(), this.p2.iy());
				break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				element = getGeomFactory().newClosePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case ARC_TO:
			default:
				throw new IllegalStateException();
			}
			if (element == null) {
				throw new NoSuchElementException();
			}

			++this.typeIndex;

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

		private final Point2D<?, ?> p1;

		private final Point2D<?, ?> p2;

		private final Point2D<?, ?> ptmp1;

		private final Point2D<?, ?> ptmp2;

		private int typeIndex;

		private int coordIndex;

		private int movex;

		private int movey;

		/**
		 * @param path the path.
		 * @param transform the transformation.
		 */
		public TransformedPathIterator(Path2ai<?, ?, E, ?, ?, ?> path, Transform2D transform) {
			super(path);
			assert transform != null : AssertMessages.notNullParameter(1);
			this.transform = transform;
			this.p1 = new InnerComputationPoint2ai();
			this.p2 = new InnerComputationPoint2ai();
			this.ptmp1 = new InnerComputationPoint2ai();
			this.ptmp2 = new InnerComputationPoint2ai();
		}

		@Override
		public PathIterator2ai<E> restartIterations() {
			return new TransformedPathIterator<>(this.path, this.transform);
		}

		@Override
		public boolean hasNext() {
			return this.typeIndex < this.path.getPathElementCount();
		}

		@Override
		public E next() {
			if (this.typeIndex >= this.path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			E element = null;
			switch (this.path.getPathElementTypeAt(this.typeIndex++)) {
			case MOVE_TO:
				this.movex = this.path.getCoordAt(this.coordIndex++);
				this.movey = this.path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case QUAD_TO:
				this.p1.set(this.p2);
				this.ptmp1.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp1);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						this.ptmp1.ix(), this.ptmp1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case CURVE_TO:
				this.p1.set(this.p2);
				this.ptmp1.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp2);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(),
						this.ptmp1.ix(), this.ptmp1.iy(),
						this.ptmp2.ix(), this.ptmp2.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = getGeomFactory().newClosePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
				break;
			case ARC_TO:
			default:
				throw new IllegalStateException();
			}
			if (element == null) {
				throw new NoSuchElementException();
			}
			return element;
		}

	}

	/** Iterator on the pixels of the path.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PixelIterator<P extends Point2D<? super P, ? super V>,
			V extends Vector2D<? super V, ? super P>> implements Iterator<P> {

		private final PathIterator2ai<?> pathIterator;

		private final GeomFactory2ai<?, P, V, ?> factory;

		private Iterator<P> lineIterator;

		private P next;

		/**
		 * @param pi the iterator.
		 * @param factory the element factory.
		 */
		public PixelIterator(PathIterator2ai<?> pi, GeomFactory2ai<?, P, V, ?> factory) {
			assert pi != null : AssertMessages.notNullParameter(0);
			assert factory != null : AssertMessages.notNullParameter(1);
			this.pathIterator = pi;
			this.factory = factory;
			searchNext();
		}

		private void searchNext() {
			final P old = this.next;
			this.next = null;
			while (this.pathIterator.hasNext() && (this.lineIterator == null || !this.lineIterator.hasNext())) {
				this.lineIterator = null;
				final PathElement2ai elt = this.pathIterator.next();
				if (elt.isDrawable()) {
					switch (elt.getType()) {
					case LINE_TO:
					case CLOSE:
						final Segment2ai<?, ?, ?, P, V, ?> segment = this.factory.newSegment(
								elt.getFromX(), elt.getFromY(),
								elt.getToX(), elt.getToY());
						this.lineIterator = segment.getPointIterator();
						break;
					case MOVE_TO:
					case CURVE_TO:
					case QUAD_TO:
					case ARC_TO:
					default:
						throw new IllegalStateException();
					}
				}
			}
			if (this.lineIterator != null && this.lineIterator.hasNext()) {
				this.next = this.lineIterator.next();
				while (this.next.equals(old)) {
					this.next = this.lineIterator.next();
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public P next() {
			final P n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
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
	@SuppressWarnings("checkstyle:magicnumber")
	class FlatteningPathIterator<E extends PathElement2ai> implements PathIterator2ai<E> {

		/** The source iterator.
		 */
		private final PathIterator2ai<E> pathIterator;

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
		private int[] levels;

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
		private double[] hold = new double[14];

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
		 * at the holdIndex.
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
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 *     control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions
		 *     allowed for any curved segment
		 */
		public FlatteningPathIterator(PathIterator2ai<E> pathIterator,
				double flatness, int limit) {
			assert pathIterator != null : AssertMessages.notNullParameter(0);
			assert flatness > 0f : AssertMessages.positiveOrZeroParameter(1);
			assert limit >= 0 : AssertMessages.positiveOrZeroParameter(2);
			this.pathIterator = pathIterator;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit;
			this.levels = new int[limit + 1];
			searchNext(true);
		}

		@Override
		public PathIterator2ai<E> restartIterations() {
			return new FlatteningPathIterator<>(this.pathIterator.restartIterations(),
					Math.sqrt(this.squaredFlatness), this.limit);
		}

		/**
		 * Ensures that the hold array can hold up to (want) more values.
		 * It is currently holding (hold.length - holdIndex) values.
		 */
		private void ensureHoldCapacity(int want) {
			if (this.holdIndex - want < 0) {
				final int have = this.hold.length - this.holdIndex;
				final int newsize = this.hold.length + GROW_SIZE;
				final double[] newhold = new double[newsize];
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
		private static double getQuadSquaredFlatness(double[] coords, int offset) {
			return Segment2afp.computeDistanceSquaredLinePoint(
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
		 *     the 6 source coordinates
		 * @param left the array for storing the coordinates for the first
		 *     half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 *     the 6 left coordinates
		 * @param right the array for storing the coordinates for the second
		 *     half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 *     the 6 right coordinates
		 */
		private static void subdivideQuad(double[] src, int srcoff,
				double[] left, int leftoff,
				double[] right, int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
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
			double ctrlx = src[srcoff + 2];
			double ctrly = src[srcoff + 3];
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
		private static double getCurveSquaredFlatness(double[] coords, int offset) {
			return Math.max(
					Segment2afp.computeDistanceSquaredSegmentPoint(
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 2],
							coords[offset + 3],
							coords[offset + 0],
							coords[offset + 1]),
					Segment2afp.computeDistanceSquaredSegmentPoint(
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 4],
							coords[offset + 5],
							coords[offset + 0],
							coords[offset + 1]));
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
		 *     the 6 source coordinates
		 * @param left the array for storing the coordinates for the first
		 *     half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 *     the 6 left coordinates
		 * @param right the array for storing the coordinates for the second
		 *     half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 *     the 6 right coordinates
		 */
		private static void subdivideCurve(
				double[] src, int srcoff,
				double[] left, int leftoff,
				double[] right, int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
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
			double ctrlx1 = src[srcoff + 2];
			double ctrly1 = src[srcoff + 3];
			x1 = (x1 + ctrlx1) / 2f;
			y1 = (y1 + ctrly1) / 2f;
			double ctrlx2 = src[srcoff + 4];
			double ctrly2 = src[srcoff + 5];
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
			final PathElementType type = this.holdType;
			final int x;
			final int y;
			if (type == PathElementType.CLOSE) {
				x = (int) Math.round(this.moveX);
				y = (int) Math.round(this.moveY);
			} else {
				x = (int) Math.round(this.hold[this.holdIndex + 0]);
				y = (int) Math.round(this.hold[this.holdIndex + 1]);
			}
			return x == this.lastNextX && y == this.lastNextY;
		}

		@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
		private void flattening() {
			int level;

			if (this.holdIndex >= this.holdEnd) {
				if (!this.pathIterator.hasNext()) {
					this.done = true;
					return;
				}
				final PathElement2ai pathElement = this.pathIterator.next();
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
					this.hold[this.holdIndex + 4] = this.hold[2];
					this.currentX = this.hold[2];
					this.hold[this.holdIndex + 5] = this.hold[3];
					this.currentY = this.hold[3];
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
					this.hold[this.holdIndex + 6] = this.hold[4];
					this.currentX = this.hold[4];
					this.hold[this.holdIndex + 7] = this.hold[5];
					this.currentY = this.hold[5];
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getCurveSquaredFlatness(this.hold, this.holdIndex) < this.squaredFlatness) {
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
			case ARC_TO:
			default:
				throw new IllegalStateException();
			}
		}

		@Override
		public boolean hasNext() {
			return !this.done;
		}

		@Override
		public E next() {
			if (this.done) {
				throw new NoSuchElementException();
			}

			final E element;
			final PathElementType type = this.holdType;
			if (type != PathElementType.CLOSE) {
				final int x = (int) Math.round(this.hold[this.holdIndex + 0]);
				final int y = (int) Math.round(this.hold[this.holdIndex + 1]);
				if (type == PathElementType.MOVE_TO) {
					element = this.pathIterator.getGeomFactory().newMovePathElement(x, y);
				} else {
					element = this.pathIterator.getGeomFactory().newLinePathElement(
							this.lastNextX, this.lastNextY,
							x, y);
				}
				this.lastNextX = x;
				this.lastNextY = y;
			} else {
				final int x = (int) Math.round(this.moveX);
				final int y = (int) Math.round(this.moveY);
				element = this.pathIterator.getGeomFactory().newClosePathElement(
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
			return this.pathIterator.getWindingRule();
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
			return this.pathIterator.isMultiParts();
		}

		@Override
		public boolean isPolygon() {
			return this.pathIterator.isPolygon();
		}

		@Override
		public GeomFactory2ai<E, ?, ?, ?> getGeomFactory() {
			return this.pathIterator.getGeomFactory();
		}

	}

}

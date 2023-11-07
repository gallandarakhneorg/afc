/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;
import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.arakhne.afc.math.geometry.d2.PathIterator2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp.AbstractCirclePathIterator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/** Fonctional interface that represented a 2D path on a plane.
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
public interface Path2afp<
		ST extends Shape2afp<?, ?, IE, P, V, B>, IT extends Path2afp<?, ?, IE, P, V, B>,
		IE extends PathElement2afp, P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>, B extends Rectangle2afp<?, ?, IE, P, V, B>>
		extends Shape2afp<ST, IT, IE, P, V, B>, Path2D<ST, IT, PathIterator2afp<IE>, P, V, B> {

	/** Multiple of cubic &amp; quad curve size.
	 */
	int GROW_SIZE = 24;

	/** The default flattening depth limit.
	 */
	int DEFAULT_FLATTENING_LIMIT = 10;

	/** The default winding rule: {@link PathWindingRule#NON_ZERO}.
	 */
	PathWindingRule DEFAULT_WINDING_RULE = PathWindingRule.NON_ZERO;

	/**
	 * Accumulate the number of times the path crosses the shadow extending to the right of the second path.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details. The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path, or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the rectangle. The path must start with a SEG_MOVETO, otherwise an exception is
	 * thrown. The caller must check r[xy]{min, max} for NaN values.
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the iterator on the path elements.
	 * @param shadow is the description of the shape to project to the right.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	static int calculatesCrossingsPathIteratorPathShadow(int crossings, PathIterator2afp<?> iterator,
			BasicPathShadow2afp shadow, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		assert shadow != null : AssertMessages.notNullParameter(2);
		if (!iterator.hasNext()) {
			return 0;
		}
		PathElement2afp pathElement1 = iterator.next();
		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> subPath;
		double curx = pathElement1.getToX();
		double movx = curx;
		double cury = pathElement1.getToY();
		double movy = cury;
		int numCrossings = crossings;
		double endx;
		double endy;
		while (numCrossings != GeomConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement1 = iterator.next();
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
				numCrossings = shadow.computeCrossings(numCrossings, curx, cury, endx, endy);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(), endx, endy);
				numCrossings = calculatesCrossingsPathIteratorPathShadow(
						numCrossings, subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						shadow, CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.curveTo(pathElement1.getCtrlX1(), pathElement1.getCtrlY1(),
						pathElement1.getCtrlX2(), pathElement1.getCtrlY2(), endx, endy);
				numCrossings = calculatesCrossingsPathIteratorPathShadow(
						numCrossings, subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						shadow, CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.arcTo(endx, endy, pathElement1.getRadiusX(), pathElement1.getRadiusY(),
						pathElement1.getRotationX(), pathElement1.getLargeArcFlag(),
						pathElement1.getSweepFlag());
				numCrossings = calculatesCrossingsPathIteratorPathShadow(
						numCrossings, subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						shadow, CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					numCrossings = shadow.computeCrossings(numCrossings, curx, cury, movx, movy);
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
		assert numCrossings != GeomConstants.SHAPE_INTERSECTS;
		final boolean isOpen = (curx != movx) || (cury != movy);
		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				numCrossings = shadow.computeCrossings(numCrossings, curx, cury, movx, movy);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				numCrossings = 0;
				break;
				//$CASES-OMITTED$
			default:
				break;
			}
		}
		return numCrossings;
	}

	/** Replies the point on the path that is closest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the {@link PathIterator2D#isCurved()} of {@code pi} is replying
	 * {@code false}. {@link #getClosestPointTo(Point2D)} avoids this restriction.
	 * @param pi is the iterator on the elements of the path.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param result the closest point on the shape; or the point itself if it is inside the shape.
	 */
	static void findsClosestPointPathIteratorPoint(PathIterator2afp<? extends PathElement2afp> pi, double x,
			double y, Point2D<?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(3);
		double bestDist = Double.POSITIVE_INFINITY;
		PathElement2afp pe;
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		int crossings = 0;
		while (pi.hasNext()) {
			pe = pi.next();
			boolean foundCandidate = false;
			double candidateX = Double.NaN;
			double candidateY = Double.NaN;
			switch (pe.getType()) {
			case MOVE_TO:
				crossings = 0;
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				break;
			case LINE_TO:
				double factor =  Segment2afp.findsProjectedPointPointLine(x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				factor = MathUtil.clamp(factor, 0, 1);
				double vx = (pe.getToX() - pe.getFromX()) * factor;
				double vy = (pe.getToY() - pe.getFromY()) * factor;
				foundCandidate = true;
				candidateX = pe.getFromX() + vx;
				candidateY = pe.getFromY() + vy;
				crossings += Segment2afp.calculatesCrossingsPointShadowSegment(x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				break;
			case CLOSE:
				crossings += Segment2afp.calculatesCrossingsPointShadowSegment(x, y,
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
				if ((crossings & mask) != 0) {
					result.set(x, y);
					return;
				}
				if (!pe.isEmpty()) {
					factor =  Segment2afp.findsProjectedPointPointLine(x, y,
							pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY());
					factor = MathUtil.clamp(factor, 0, 1);
					vx = (pe.getToX() - pe.getFromX()) * factor;
					vy = (pe.getToY() - pe.getFromY()) * factor;
					foundCandidate = true;
					candidateX = pe.getFromX() + vx;
					candidateY = pe.getFromY() + vy;
				}
				crossings = 0;
				break;
				//$CASES-OMITTED$
			default:
				throw new IllegalStateException(pe.getType().toString());
			}
			if (foundCandidate) {
				final double d = Point2D.getDistanceSquaredPointPoint(x, y, candidateX, candidateY);
				if (d < bestDist) {
					bestDist = d;
					result.set(candidateX, candidateY);
				}
			}
		}
	}

	/** Replies the point on the path of pi that is closest to the given shape.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the {@link PathIterator2D#isCurved()} of {@code pi} is replying
	 * {@code false}. {@link #getClosestPointTo(org.arakhne.afc.math.geometry.d2.Shape2D)} avoids this restriction.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param shape the shape to which the closest point must be computed.
	 * @param result the closest point on pi.
	 * @return {@code true} if a point was found. Otherwise {@code false}.
	 */
	@Unefficient
	static boolean findsClosestPointPathIteratorPathIterator(PathIterator2afp<? extends PathElement2afp> pi,
			PathIterator2afp<? extends PathElement2afp> shape, Point2D<?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert shape != null : AssertMessages.notNullParameter(1);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(2);
		if (!pi.hasNext() || !shape.hasNext()) {
			return false;
		}
		PathElement2afp pathElement1 = pi.next();
		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (shape.next().getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (!pi.hasNext() || !shape.hasNext()) {
			return false;
		}
		final Rectangle2afp<?, ?, ?, ?, ?, ?> box = pi.getGeomFactory().newBox();
		calculatesDrawableElementBoundingBox(shape.restartIterations(), box);
		final ClosestPointPathShadow2afp shadow = new ClosestPointPathShadow2afp(shape.restartIterations(), box);
		int crossings = 0;
		double curx = pathElement1.getToX();
		double movx = curx;
		double cury = pathElement1.getToY();
		double movy = cury;
		double endx;
		double endy;
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
				if (crossings == GeomConstants.SHAPE_INTERSECTS) {
					result.set(shadow.getClosestPointInOtherShape());
					return true;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					crossings = shadow.computeCrossings(crossings, curx, cury, movx, movy);
					if (crossings == GeomConstants.SHAPE_INTERSECTS) {
						result.set(shadow.getClosestPointInOtherShape());
						return true;
					}
				}
				curx = movx;
				cury = movy;
				break;
				//$CASES-OMITTED$
			default:
				throw new IllegalArgumentException();
			}
		}
		if (curx == movx && cury == movy) {
			assert crossings != GeomConstants.SHAPE_INTERSECTS;
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

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Path2afp.findsClosestPointPathIteratorPoint(
				getPathIterator(getGeomFactory().getSplineApproximationRatio()),
				pt.getX(), pt.getY(),
				point);
		return point;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPoint(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					circle.getCenterX(), circle.getCenterY(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPoint(getPathIterator(), circle.getCenterX(), circle.getCenterY(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					ellipse.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), ellipse.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					rectangle.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), rectangle.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					segment.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), segment.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					triangle.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), triangle.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					path.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), path.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					orientedRectangle.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), orientedRectangle.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					parallelogram.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), parallelogram.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					roundRectangle.getPathIterator(), result);
		} else {
			Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), roundRectangle.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Path2afp.findsFarthestPointPathIteratorPoint(
				getPathIterator(getGeomFactory().getSplineApproximationRatio()),
				pt.getX(), pt.getY(),
				point);
		return point;
	}

	/** Replies the point on the path that is farthest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the {@link PathIterator2D#isCurved()} of {@code pi} is replying
	 * {@code false}. {@link #getFarthestPointTo(Point2D)} avoids this restriction.
	 * @param pi is the iterator on the elements of the path.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param result the farthest point on the shape.
	 */
	static void findsFarthestPointPathIteratorPoint(PathIterator2afp<? extends PathElement2afp> pi, double x,
			double y, Point2D<?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(3);
		double bestDist = Double.NEGATIVE_INFINITY;
		PathElement2afp pe;
		final Point2D<?, ?> point = new InnerComputationPoint2afp();
		while (pi.hasNext()) {
			pe = pi.next();
			switch (pe.getType()) {
			case MOVE_TO:
				break;
			case LINE_TO:
			case CLOSE:
				Segment2afp.findsFarthestPointSegmentPoint(
						pe.getFromX(), pe.getFromY(), pe.getToX(), pe.getToY(),
						x, y, point);
				final double d = Point2D.getDistanceSquaredPointPoint(x, y, point.getX(), point.getY());
				if (d > bestDist) {
					bestDist = d;
					result.set(point.getX(), point.getY());
				}
				break;
				//$CASES-OMITTED$
			default:
				throw new IllegalStateException(pe.getType().toString());
			}
		}
	}

	/** Tests if the specified coordinates are inside the closed boundary of the specified {@link PathIterator2afp}.
	 *
	 * <p>This method provides a basic facility for implementors of the {@link Shape2afp} interface to implement support for the
	 * {@link Shape2afp#contains(double, double)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @return {@code true} if the specified coordinates are inside the specified {@code PathIterator2f}; {@code false} otherwise
	 */
	static boolean containsPoint(PathIterator2afp<? extends PathElement2afp> pi, double x, double y) {
		assert pi != null : AssertMessages.notNullParameter(0);
		// Copied from the AWT API
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		final int cross = calculatesCrossingsPathIteratorPointShadow(0, pi, x, y,
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (cross & mask) != 0;
	}

	/** Tests if the specified rectangle is inside the closed boundary of the specified {@link PathIterator2afp}.
	 *
	 * <p>This method provides a basic facility for implementors of the {@link Shape2afp} interface to implement support for the
	 * {@link Shape2afp#contains(Rectangle2afp)} method.
	 * @param pi the specified {@code PathIterator2f}
	 * @param rx the lowest corner of the rectangle.
	 * @param ry the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the width of the rectangle.
	 * @return {@code true} if the specified rectangle is inside the specified {@code PathIterator2f}; {@code false} otherwise.
	 */
	static boolean containsRectangle(PathIterator2afp<? extends PathElement2afp> pi, double rx, double ry,
			double rwidth, double rheight) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert rwidth >= 0. : AssertMessages.positiveOrZeroParameter(3);
		assert rheight >= 0. : AssertMessages.notNullParameter(4);
		// Copied from AWT API
		if (rwidth <= 0 || rheight <= 0) {
			return false;
		}
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorRectangleShadow(
				0, pi, rx, ry, rx + rwidth, ry + rheight,
				CrossingComputationType.AUTO_CLOSE);
		return crossings != GeomConstants.SHAPE_INTERSECTS
				&& (crossings & mask) != 0;
	}

	/** Tests if the interior of the specified {@link PathIterator2afp} intersects the interior of a specified set of rectangular
	 * coordinates.
	 * @param pi the specified {@link PathIterator2afp}.
	 * @param x the specified X coordinate of the rectangle.
	 * @param y the specified Y coordinate of the rectangle.
	 * @param width the width of the specified rectangular coordinates.
	 * @param height the height of the specified rectangular coordinates.
	 * @return {@code true} if the specified {@link PathIterator2afp} and the interior of the specified set of rectangular
	 *         coordinates intersect each other; {@code false} otherwise.
	 */
	static boolean intersectsPathIteratorRectangle(PathIterator2afp<? extends PathElement2afp> pi, double x, double y,
			double width, double height) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert width >= 0. : AssertMessages.notNullParameter(3);
		assert height >= 0. : AssertMessages.notNullParameter(4);
		if (width <= 0 || height <= 0) {
			return false;
		}
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorRectangleShadow(0, pi, x, y, x + width, y + height,
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	/** Calculates the number of times the given path crosses the ray extending to the right from (px, py).
	 * If the point lies on a part of the path, then no crossings are counted for that intersection.
	 * +1 is added for each crossing where the Y coordinate is increasing -1 is added for each crossing where the Y
	 * coordinate is decreasing. The return value is the sum of all crossings for every segment in the path.
	 * The path must start with a MOVE_TO, otherwise an exception is thrown.
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int calculatesCrossingsPathIteratorPointShadow(int crossings, PathIterator2afp<? extends PathElement2afp> iterator,
			double px, double py, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		// Copied from the AWT API
		if (!iterator.hasNext()) {
			return 0;
		}
		PathElement2afp element;
		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> subPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx;
		double endy;
		int numCrossings = crossings;
		while (iterator.hasNext()) {
			element = iterator.next();
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
				if (endx == px && endy == py) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				numCrossings += Segment2afp.calculatesCrossingsPointShadowSegment(
						px, py,
						curx, cury,
						endx, endy);
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				if (endx == px && endy == py) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrossings = calculatesCrossingsPathIteratorPointShadow(
						numCrossings,
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						px, py,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				if (endx == px && endy == py) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				numCrossings = calculatesCrossingsPathIteratorPointShadow(
						numCrossings,
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						px, py,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				if (endx == px && endy == py) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.arcTo(
						endx, endy,
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				numCrossings = calculatesCrossingsPathIteratorPointShadow(
						numCrossings,
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						px, py,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					if (movx == px && movy == py) {
						return GeomConstants.SHAPE_INTERSECTS;
					}
					numCrossings += Segment2afp.calculatesCrossingsPointShadowSegment(
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

		assert numCrossings != GeomConstants.SHAPE_INTERSECTS;
		final boolean isOpen = (curx != movx) || (cury != movy);
		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				if (movx == px && movy == py) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				numCrossings += Segment2afp.calculatesCrossingsPointShadowSegment(
						px, py,
						curx, cury,
						movx, movy);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				numCrossings = 0;
				break;
				//$CASES-OMITTED$
			default:
				break;
			}
		}

		return numCrossings;
	}

	/** Calculates the number of times the given path crosses the given ellipse extending to the right.
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param ex is the first point of the ellipse.
	 * @param ey is the first point of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing or {@link GeomConstants#SHAPE_INTERSECTS}
	 */
	static int calculatesCrossingsPathIteratorEllipseShadow(int crossings, PathIterator2afp<? extends PathElement2afp> iterator,
			double ex, double ey, double ew, double eh, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		assert ew >= 0. : AssertMessages.notNullParameter(4);
		assert eh >= 0. : AssertMessages.notNullParameter(5);
		// Copied from the AWT API
		if (!iterator.hasNext()) {
			return 0;
		}
		PathElement2afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx;
		double endy;
		int numCrosses = crossings;
		while (numCrosses != GeomConstants.SHAPE_INTERSECTS && iterator.hasNext()) {
			element = iterator.next();
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
				numCrosses = Segment2afp.calculatesCrossingsEllipseShadowSegment(
						numCrosses,
						ex, ey, ew, eh,
						curx, cury,
						endx, endy);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = calculatesCrossingsPathIteratorEllipseShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						ex, ey, ew, eh,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
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
				numCrosses = calculatesCrossingsPathIteratorEllipseShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						ex, ey, ew, eh,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.arcTo(
						endx, endy,
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				numCrosses = calculatesCrossingsPathIteratorEllipseShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						ex, ey, ew, eh,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = Segment2afp.calculatesCrossingsEllipseShadowSegment(
							numCrosses,
							ex, ey, ew, eh,
							curx, cury,
							movx, movy);
					if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert numCrosses != GeomConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				numCrosses = Segment2afp.calculatesCrossingsEllipseShadowSegment(
						numCrosses,
						ex, ey, ew, eh,
						curx, cury,
						movx, movy);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only. SHAPE_INTERSECTS may be return.
				numCrosses = 0;
				break;
				//$CASES-OMITTED$
			default:
				break;
			}
		}

		return numCrosses;
	}

	/** Calculates the number of times the given path crosses the given round rectangle extending to the right.
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param arcWidth is the width of the arc.
	 * @param arcHeight is the width of the arc.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing or {@link GeomConstants#SHAPE_INTERSECTS}
	 */
	static int calculatesCrossingsPathIteratorRoundRectangleShadow(int crossings,
			PathIterator2afp<? extends PathElement2afp> iterator, double x1, double y1, double x2, double y2,
			double arcWidth, double arcHeight, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		assert x1 <= x2 : AssertMessages.lowerEqualParameters(2, x1, 4, x2);
		assert y1 <= y2 : AssertMessages.lowerEqualParameters(3, y1, 5, y2);
		if (!iterator.hasNext()) {
			return 0;
		}
		PathElement2afp pathElement = iterator.next();
		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		double curx = pathElement.getToX();
		double movx = curx;
		double cury = pathElement.getToY();
		double movy = cury;
		int numCrossings = crossings;
		Path2afp<?, ?, ?, ?, ?, ?> localPath;
		double endx;
		double endy;
		while (numCrossings != GeomConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement = iterator.next();
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
				numCrossings = Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(numCrossings,
						x1, y1, x2, y2, arcWidth, arcHeight,
						curx, cury,
						endx, endy);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				numCrossings = calculatesCrossingsPathIteratorRoundRectangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2, arcWidth, arcHeight,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				numCrossings = calculatesCrossingsPathIteratorRoundRectangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2, arcWidth, arcHeight,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.arcTo(
						endx, endy,
						pathElement.getRadiusX(), pathElement.getRadiusY(),
						pathElement.getRotationX(), pathElement.getLargeArcFlag(),
						pathElement.getSweepFlag());
				numCrossings = calculatesCrossingsPathIteratorRoundRectangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2, arcWidth, arcHeight,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					numCrossings = Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(numCrossings,
							x1, y1, x2, y2, arcWidth, arcHeight,
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
		assert numCrossings != GeomConstants.SHAPE_INTERSECTS;
		final boolean isOpen = (curx != movx) || (cury != movy);
		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(numCrossings,
						x1, y1, x2, y2, arcWidth, arcHeight,
						curx, cury,
						movx, movy);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrossings = 0;
				break;
				//$CASES-OMITTED$
			default:
				break;
			}
		}
		return numCrossings;
	}

	/** Calculates the number of times the given path crosses the given circle extending to the right.
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int calculatesCrossingsPathIteratorCircleShadow(int crossings, PathIterator2afp<? extends PathElement2afp> iterator,
			double cx, double cy, double radius, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		assert radius >= 0. : AssertMessages.positiveOrZeroParameter(4);
		// Copied from the AWT API
		if (!iterator.hasNext()) {
			return 0;
		}
		PathElement2afp element;
		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx;
		double endy;
		int numCrosses = crossings;
		while (iterator.hasNext()) {
			element = iterator.next();
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
				numCrosses = Segment2afp.calculatesCrossingsCircleShadowSegment(
						numCrosses,
						cx, cy, radius,
						curx, cury,
						endx, endy);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = calculatesCrossingsPathIteratorCircleShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						cx, cy, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
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
				numCrosses = calculatesCrossingsPathIteratorCircleShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						cx, cy, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY());
				localPath.arcTo(
						endx, endy,
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				numCrosses = calculatesCrossingsPathIteratorCircleShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						cx, cy, radius,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = Segment2afp.calculatesCrossingsCircleShadowSegment(
							numCrosses,
							cx, cy, radius,
							curx, cury,
							movx, movy);
					if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}
		assert numCrosses != GeomConstants.SHAPE_INTERSECTS;
		final boolean isOpen = (curx != movx) || (cury != movy);
		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Auto close
				numCrosses = Segment2afp.calculatesCrossingsCircleShadowSegment(
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
				//$CASES-OMITTED$
			default:
				// Standard behavior
				break;
			}
		}
		return numCrosses;
	}

	/** Calculates the number of times the given path crosses the given segment extending to the right.
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the description of the path.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int calculatesCrossingsPathIteratorSegmentShadow(int crossings, PathIterator2afp<? extends PathElement2afp> iterator,
			double x1, double y1, double x2, double y2, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		// Copied from the AWT API
		if (!iterator.hasNext() || crossings == GeomConstants.SHAPE_INTERSECTS) {
			return crossings;
		}
		PathElement2afp element;
		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx;
		double endy;
		int numCrosses = crossings;
		while (numCrosses != GeomConstants.SHAPE_INTERSECTS && iterator.hasNext()) {
			element = iterator.next();
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
				numCrosses = Segment2afp.calculatesCrossingsSegmentShadowSegment(
						numCrosses,
						x1, y1, x2, y2,
						curx, cury,
						endx, endy);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				numCrosses = calculatesCrossingsPathIteratorSegmentShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
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
				numCrosses = calculatesCrossingsPathIteratorSegmentShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.arcTo(
						endx, endy,
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				numCrosses = calculatesCrossingsPathIteratorSegmentShadow(
						numCrosses,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2,
						CrossingComputationType.STANDARD);
				if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (cury != movy || curx != movx) {
					numCrosses = Segment2afp.calculatesCrossingsSegmentShadowSegment(
							numCrosses,
							x1, y1, x2, y2,
							curx, cury,
							movx, movy);
				}
				if (numCrosses != 0) {
					return numCrosses;
				}
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}
		assert numCrosses != GeomConstants.SHAPE_INTERSECTS;
		final boolean isOpen = (curx != movx) || (cury != movy);
		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				numCrosses = Segment2afp.calculatesCrossingsSegmentShadowSegment(
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
				//$CASES-OMITTED$
			default:
			}
		}

		return numCrosses;
	}

	/** Accumulate the number of times the path crosses the shadow extending to the right of the rectangle.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details. The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path, or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the rectangle. The path must start with a SEG_MOVETO, otherwise an exception is thrown.
	 * The caller must check r[xy]{min, max} for NaN values.
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the iterator on the path elements.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 */
	static int calculatesCrossingsPathIteratorRectangleShadow(int crossings, PathIterator2afp<? extends PathElement2afp> iterator,
			double rxmin, double rymin, double rxmax, double rymax, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(2, rxmin, 4, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(3, rymin, 5, rymax);
		// Copied from AWT API
		if (!iterator.hasNext()) {
			return 0;
		}
		PathElement2afp pathElement = iterator.next();
		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> localPath;
		double curx = pathElement.getToX();
		double movx = curx;
		double cury = pathElement.getToY();
		double movy = cury;
		int numCrossings = crossings;
		double endx;
		double endy;
		while (numCrossings != GeomConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement = iterator.next();
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
				numCrossings = Segment2afp.calculatesCrossingsRectangleShadowSegment(
						numCrossings,
						rxmin, rymin,
						rxmax, rymax,
						curx, cury,
						endx, endy);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				numCrossings = calculatesCrossingsPathIteratorRectangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						rxmin, rymin,
						rxmax, rymax,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				numCrossings = calculatesCrossingsPathIteratorRectangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						rxmin, rymin,
						rxmax, rymax,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.arcTo(
						endx, endy,
						pathElement.getRadiusX(), pathElement.getRadiusY(),
						pathElement.getRotationX(), pathElement.getLargeArcFlag(),
						pathElement.getSweepFlag());
				numCrossings = calculatesCrossingsPathIteratorRectangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						rxmin, rymin,
						rxmax, rymax,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					numCrossings = Segment2afp.calculatesCrossingsRectangleShadowSegment(
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
				break;
			default:
			}
		}
		assert numCrossings != GeomConstants.SHAPE_INTERSECTS;
		final boolean isOpen = (curx != movx) || (cury != movy);
		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = Segment2afp.calculatesCrossingsRectangleShadowSegment(
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
				//$CASES-OMITTED$
			default:
				break;
			}
		}
		return numCrossings;
	}

	/** Accumulate the number of times the path crosses the shadow extending to the right of the triangle.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details. The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path, or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the triangle. The path must start with a SEG_MOVETO, otherwise an exception is thrown.
	 * The caller must check for NaN values.
	 *
	 * @param crossings is the initial value for crossing.
	 * @param iterator is the iterator on the path elements.
	 * @param x1 is the first point of the triangle.
	 * @param y1 is the first point of the triangle.
	 * @param x2 is the second point of the triangle.
	 * @param y2 is the second point of the triangle.
	 * @param x3 is the third point of the triangle.
	 * @param y3 is the third point of the triangle.
	 * @param type is the type of special computation to apply. If {@code null}, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 */
	static int calculatesCrossingsPathIteratorTriangleShadow(int crossings, PathIterator2afp<? extends PathElement2afp> iterator,
			double x1, double y1, double x2, double y2, double x3, double y3, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		if (!iterator.hasNext()) {
			return 0;
		}

		PathElement2afp pathElement = iterator.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> localPath;
		double curx = pathElement.getToX();
		double movx = curx;
		double cury = pathElement.getToY();
		double movy = cury;
		int numCrossings = crossings;
		double endx;
		double endy;
		while (numCrossings != GeomConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement = iterator.next();
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
				numCrossings = Segment2afp.calculatesCrossingsTriangleShadowSegment(numCrossings,
						x1, y1, x2, y2, x3, y3,
						curx, cury,
						endx, endy);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				numCrossings = calculatesCrossingsPathIteratorTriangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2, x3, y3,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
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
				numCrossings = calculatesCrossingsPathIteratorTriangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2, x3, y3,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				// for internal use only
				localPath = factory.newPath(iterator.getWindingRule());
				localPath.moveTo(curx, cury);
				localPath.arcTo(
						endx, endy,
						pathElement.getRadiusX(), pathElement.getRadiusY(),
						pathElement.getRotationX(), pathElement.getLargeArcFlag(),
						pathElement.getSweepFlag());
				numCrossings = calculatesCrossingsPathIteratorTriangleShadow(
						numCrossings,
						localPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						x1, y1, x2, y2, x3, y3,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					numCrossings = Segment2afp.calculatesCrossingsTriangleShadowSegment(numCrossings,
							x1, y1, x2, y2, x3, y3,
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
		assert numCrossings != GeomConstants.SHAPE_INTERSECTS;
		final boolean isOpen = (curx != movx) || (cury != movy);
		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = Segment2afp.calculatesCrossingsTriangleShadowSegment(numCrossings,
						x1, y1, x2, y2, x3, y3,
						curx, cury,
						movx, movy);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrossings = 0;
				break;
				//$CASES-OMITTED$
			default:
				break;
			}
		}
		return numCrossings;
	}

	/** Compute the box that corresponds to the drawable elements of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element. The box fits the drawn lines and the
	 * drawn curves. The control points of the curves may be outside the output box. For obtaining the bounding box
	 * of the path's points, use {@link #calculatesControlPointBoundingBox(PathIterator2afp, Rectangle2afp)}.
	 *
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return {@code true} if a drawable element was found.
	 * @see #calculatesControlPointBoundingBox(PathIterator2afp, Rectangle2afp)
	 */
	static boolean calculatesDrawableElementBoundingBox(PathIterator2afp<?> iterator, Rectangle2afp<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		assert box != null : AssertMessages.notNullParameter(1);
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		boolean foundOneLine = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		PathElement2afp element;
		Path2afp<?, ?, ?, ?, ?, ?> subPath;
		Rectangle2afp<?, ?, ?, ?, ?, ?> subBox;
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
				subBox = factory.newBox();
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						element.getToX(), element.getToY());
				if (calculatesDrawableElementBoundingBox(
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						subBox)) {
					if (subBox.getMinX() < xmin) {
						xmin = subBox.getMinX();
					}
					if (subBox.getMinY() < ymin) {
						ymin = subBox.getMinY();
					}
					if (subBox.getMaxX() > xmax) {
						xmax = subBox.getMaxX();
					}
					if (subBox.getMaxY() > ymax) {
						ymax = subBox.getMaxY();
					}
					foundOneLine = true;
				}
				break;
			case ARC_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subBox = factory.newBox();
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.arcTo(
						element.getToX(), element.getToY(),
						element.getRadiusX(), element.getRadiusY(),
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				if (calculatesDrawableElementBoundingBox(
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						subBox)) {
					if (subBox.getMinX() < xmin) {
						xmin = subBox.getMinX();
					}
					if (subBox.getMinY() < ymin) {
						ymin = subBox.getMinY();
					}
					if (subBox.getMaxX() > xmax) {
						xmax = subBox.getMaxX();
					}
					if (subBox.getMaxY() > ymax) {
						ymax = subBox.getMaxY();
					}
					foundOneLine = true;
				}
				break;
			case QUAD_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subBox = factory.newBox();
				subPath.moveTo(element.getFromX(), element.getFromY());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getToX(), element.getToY());
				if (calculatesDrawableElementBoundingBox(
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
						subBox)) {
					if (subBox.getMinX() < xmin) {
						xmin = subBox.getMinX();
					}
					if (subBox.getMinY() < ymin) {
						ymin = subBox.getMinY();
					}
					if (subBox.getMaxX() > xmax) {
						xmax = subBox.getMaxX();
					}
					if (subBox.getMaxY() > ymax) {
						ymax = subBox.getMaxY();
					}
					foundOneLine = true;
				}
				break;
				//$CASES-OMITTED$
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
	 * <p>An element is drawable if it is a line, a curve, or a closing path element. The box fits the drawn lines and the
	 * drawn curves. The control points of the curves may be outside the output box. For obtaining the bounding box
	 * of the drawn lines and cruves, use {@link #calculatesDrawableElementBoundingBox(PathIterator2afp, Rectangle2afp)}.
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return {@code true} if a control point was found.
	 * @see #calculatesDrawableElementBoundingBox(PathIterator2afp, Rectangle2afp)
	 */
	static boolean calculatesControlPointBoundingBox(PathIterator2afp<?> iterator, Rectangle2afp<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		assert box != null : AssertMessages.notNullParameter(1);
		boolean foundOneControlPoint = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		PathElement2afp element;
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
				//$CASES-OMITTED$
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

	/** Compute the total squared length of the path.
	 * @param iterator the iterator on the path elements.
	 * @return the squared length of the path.
	 */
	static double calculatesPathLength(PathIterator2afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		PathElement2afp pathElement = iterator.next();
		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		// only for internal use
		final GeomFactory2afp<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path2afp<?, ?, ?, ?, ?, ?> subPath;
		double curx = pathElement.getToX();
		double movx = curx;
		double cury = pathElement.getToY();
		double movy = cury;
		double length = 0;
		double endx;
		double endy;
		while (iterator.hasNext()) {
			pathElement = iterator.next();
			switch (pathElement.getType()) {
			case MOVE_TO:
				movx = pathElement.getToX();
				curx = movx;
				movy = pathElement.getToY();
				cury = movy;
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				length += Point2D.getDistancePointPoint(
						curx, cury,
						endx, endy);
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						endx, endy);
				length += calculatesPathLength(subPath.getPathIterator(
						iterator.getGeomFactory().getSplineApproximationRatio()));
				curx = endx;
				cury = endy;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(),
						endx, endy);
				length += calculatesPathLength(subPath.getPathIterator(
						iterator.getGeomFactory().getSplineApproximationRatio()));
				curx = endx;
				cury = endy;
				break;
			case ARC_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury);
				subPath.arcTo(
						endx, endy,
						pathElement.getRadiusX(), pathElement.getRadiusY(),
						pathElement.getRotationX(), pathElement.getLargeArcFlag(),
						pathElement.getSweepFlag());
				length += calculatesPathLength(subPath.getPathIterator(
						iterator.getGeomFactory().getSplineApproximationRatio()));
				curx = endx;
				cury = endy;
				break;
			case CLOSE:
				if (curx != movx || cury != movy) {
					length += Point2D.getDistancePointPoint(
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
	 * @param iterator the iterator that provides the elements to add in the path.
	 */
	default void add(Iterator<? extends PathElement2afp> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		PathElement2afp element;
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
						element.getRotationX(), element.getLargeArcFlag(),
						element.getSweepFlag());
				break;
			case CLOSE:
				closePath();
				break;
			default:
			}
		}
	}

	/** Set the path.
	 * @param path the path to copy.
	 */
	default void set(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		clear();
		add(path.getPathIterator());
	}

	/** Adds a point to the path by moving to the specified coordinates specified in double precision.
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	void moveTo(double x, double y);

	@Override
	default void moveTo(Point2D<?, ?> position) {
		assert position != null : AssertMessages.notNullParameter();
		moveTo(position.getX(), position.getY());
	}

	/** Adds a point to the path by moving to the specified coordinates specified in double precision
	 * if and only if the current position does not corresponds to the given position.
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	default void moveToIfFar(double x, double y) {
		if (isEmpty()) {
			moveTo(x, y);
		} else {
			final double dist = Point2D.getDistanceSquaredPointPoint(x, y, getCurrentX(), getCurrentY());
			if (dist > Math.ulp(dist)) {
				moveTo(x, y);
			}
		}
	}

	@Override
	default void moveToIfFar(Point2D<?, ?> position) {
		assert position != null : AssertMessages.notNullParameter();
		moveToIfFar(position.getX(), position.getY());
	}

	/** Adds a point to the path by drawing a straight line from the current coordinates to the new specified coordinates
	 * specified in double precision.
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 */
	void lineTo(double x, double y);

	@Override
	default void lineTo(Point2D<?, ?> to) {
		assert to != null : AssertMessages.notNullParameter();
		lineTo(to.getX(), to.getY());
	}

	/** Adds a curved segment, defined by two new points, to the path by drawing a Quadratic curve that intersects both the
	 * current coordinates and the specified coordinates {@code (x2, y2)}, using the specified point {@code (x1, y1)} as a
	 * quadratic parametric control point. All coordinates are specified in double precision.
	 * @param x1 the X coordinate of the quadratic control point
	 * @param y1 the Y coordinate of the quadratic control point
	 * @param x2 the X coordinate of the final end point
	 * @param y2 the Y coordinate of the final end point
	 */
	void quadTo(double x1, double y1, double x2, double y2);

	@Override
	default void quadTo(Point2D<?, ?> ctrl, Point2D<?, ?> to) {
		assert ctrl != null : AssertMessages.notNullParameter(0);
		assert to != null : AssertMessages.notNullParameter(1);
		quadTo(ctrl.getX(), ctrl.getY(), to.getX(), to.getY());
	}

	/** Adds a curved segment, defined by three new points, to the path by drawing a B&eacute;zier curve that intersects both
	 * the current coordinates and the specified coordinates {@code (x3, y3)}, using the specified points {@code (x1, y1)}
	 * and {@code (x2, y2)} as B&eacute;zier control points. All coordinates are specified in double precision.
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
	default void curveTo(Point2D<?, ?> ctrl1, Point2D<?, ?> ctrl2, Point2D<?, ?> to) {
		assert ctrl1 != null : AssertMessages.notNullParameter(0);
		assert ctrl2 != null : AssertMessages.notNullParameter(1);
		assert to != null : AssertMessages.notNullParameter(2);
		curveTo(ctrl1.getX(), ctrl1.getY(), ctrl2.getX(), ctrl2.getY(), to.getX(), to.getY());
	}

	/** Adds a section of an shallow ellipse to the current path. The ellipse from which a quadrant is taken is the ellipse
	 * that would be inscribed in a parallelogram defined by 3 points. The current point which is considered to be the
	 * midpoint of the edge leading into the corner of the ellipse where the ellipse grazes it, {@code (ctrlx, ctrly)}
	 * which is considered to be the location of the corner of the parallelogram in which the ellipse is inscribed, and
	 * {@code (tox, toy)} which is considered to be the midpoint of the edge leading away from the corner of the oval where
	 * the oval grazes it.
	 *
	 * <p><img alt="" src="../doc-files/arcto0.png">
	 *
	 * <p>Only the portion of the ellipse from {@code tfrom} to {@code tto} will be included where {@code 0f} represents
	 * the point where the ellipse grazes the leading edge, {@code 1f} represents the point where the oval grazes the trailing
	 * edge, and {@code 0.5f} represents the point on the oval closest to the control point. The two values must satisfy the
	 * relation {@code (0 <= tfrom <= tto <= 1)}.
	 *
	 * <p>If {@code tfrom} is not {@code 0f} then the caller would most likely want to use one of the arc {@code type} values
	 * that inserts a segment leading to the initial point. An initial {@link #moveTo(double, double)} or
	 * {@link #lineTo(double, double)} can be added to direct the path to the starting point of the ellipse section if
	 * {@link org.arakhne.afc.math.geometry.d2.Path2D.ArcType#MOVE_THEN_ARC} or
	 * {@link org.arakhne.afc.math.geometry.d2.Path2D.ArcType#LINE_THEN_ARC} are specified by the type argument. The
	 * {@code lineTo} path segment will only be added if the current point is not already at the indicated location to avoid
	 * spurious empty line segments. The type can be specified as {@link org.arakhne.afc.math.geometry.d2.Path2D.ArcType#ARC_ONLY}
	 * if the current point on the path is known to be at the starting point of the ellipse section.
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
	default void arcTo(double ctrlx, double ctrly, double tox, double toy, double tfrom, double tto, ArcType type) {
		// Copied from JavaFX Path2D
		assert tfrom >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert tto >= tfrom : AssertMessages.lowerEqualParameters(4, tfrom, 5, tto);
		assert tto <= 1. : AssertMessages.lowerEqualParameter(5, tto, 1);
		double currentx = getCurrentX();
		double currenty = getCurrentY();
		final double ocurrentx = currentx;
		final double ocurrenty = currenty;
		double targetx = tox;
		double targety = toy;
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
		double realtfrom = tfrom;
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
		if (type == ArcType.MOVE_THEN_ARC) {
			if (currentx != ocurrentx || currenty != ocurrenty) {
				moveTo(currentx, currenty);
			}
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
			curveTo(cx0, cy0, cx1, cy1, targetx, targety);
		}
	}

	@Override
	default void arcTo(Point2D<?, ?> ctrl, Point2D<?, ?> to, double tfrom, double tto,
			org.arakhne.afc.math.geometry.d2.Path2D.ArcType type) {
		assert ctrl != null : AssertMessages.notNullParameter(0);
		assert to != null : AssertMessages.notNullParameter(1);
		arcTo(ctrl.getX(), ctrl.getY(), to.getX(), to.getY(), tfrom, tto, type);
	}

	/**
	 * Adds a section of an shallow ellipse to the current path.
	 *
	 * <p>This function is equivalent to:<pre>{@code 
	 * this.arcTo(ctrl, to, 0.0, 1.0, ArcType.ARCONLY);
	 * }</pre>
	 *
	 * @param ctrlx the x coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param ctrly the y coordinate of the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param tox the x coordinate of the target point.
	 * @param toy the y coordinate of the target point.
	 */
	default void arcTo(double ctrlx, double ctrly, double tox, double toy) {
		arcTo(ctrlx, ctrly, tox, toy, 0., 1., ArcType.ARC_ONLY);
	}

	@Override
	default void arcTo(Point2D<?, ?> to, Vector2D<?, ?> radii, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
		assert radii != null : AssertMessages.notNullParameter(1);
		assert to != null : AssertMessages.notNullParameter(0);
		arcTo(to.getX(), to.getY(), radii.getX(), radii.getY(), xAxisRotation, largeArcFlag, sweepFlag);
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
	 * <p><img alt="" src="../doc-files/arcto1.png" >
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
	 * @param largeArcFlag {@code true} iff the path will sweep the long way around the ellipse.
	 * @param sweepFlag {@code true} iff the path will sweep clockwise around the ellipse.
	 * @see "http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands"
	 */
	default void arcTo(double tox, double toy, double radiusx, double radiusy, double xAxisRotation,
			boolean largeArcFlag, boolean sweepFlag) {
		// Copied for JavaFX
		assert radiusx >= 0. : AssertMessages.positiveOrZeroParameter(2);
		assert radiusy >= 0. : AssertMessages.positiveOrZeroParameter(3);
		if (radiusx == 0. || radiusy == 0.) {
			lineTo(tox, toy);
			return;
		}
		final double ocurrentx = getCurrentX();
		final double ocurrenty = getCurrentY();
		double x1 = ocurrentx;
		double y1 = ocurrenty;
		final double x2 = tox;
		final double y2 = toy;
		if (x1 == x2 && y1 == y2) {
			return;
		}
		final double cosphi;
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
			final double xq = mx + relxq;
			final double yq = my + relyq;
			double xc = x1 + relxq;
			double yc = y1 + relyq;
			if (x1 != ocurrentx || y1 != ocurrenty) {
				lineTo(x1, y1);
			}
			arcTo(xc, yc, xq, yq, 0, 1, ArcType.ARC_ONLY);
			xc = x2 + relxq;
			yc = y2 + relyq;
			arcTo(xc, yc, x2, y2, 0, 1, ArcType.ARC_ONLY);
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
			final double relyq = cosphi * yqp * radiusy + sinphi * xqp * radiusx;
			final double xq = mx + relxq;
			final double yq = my + relyq;
			final double xc = x1 + relxq;
			final double yc = y1 + relyq;
			arcTo(xc, yc, xq, yq, 0, quadlen, ArcType.ARC_ONLY);
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

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsPoint(getPathIterator(getGeomFactory().getSplineApproximationRatio()), x, y);
	}

	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		return containsRectangle(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
				rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		// Copied from AWT API
		if (rectangle.isEmpty()) {
			return false;
		}
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorRectangleShadow(
				0, getPathIterator(),
				rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : AssertMessages.notNullParameter();
		return roundRectangle.intersects(this);
	}

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorEllipseShadow(
				0,
				getPathIterator(),
				ellipse.getMinX(), ellipse.getMinY(), ellipse.getWidth(), ellipse.getHeight(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorCircleShadow(
				0,
				getPathIterator(),
				circle.getX(), circle.getY(), circle.getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorSegmentShadow(
				0,
				getPathIterator(),
				segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorTriangleShadow(
				0,
				getPathIterator(),
				triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		assert orientedRectangle != null : AssertMessages.notNullParameter();
		return OrientedRectangle2afp.intersectsOrientedRectanglePathIterator(
				orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
				orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(),
				orientedRectangle.getFirstAxisExtent(), orientedRectangle.getSecondAxisExtent(),
				getPathIterator());
	}

	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> sparallelogram) {
		assert sparallelogram != null : AssertMessages.notNullParameter();
		return Parallelogram2afp.intersectsParallelogramPathIterator(
				sparallelogram.getCenterX(), sparallelogram.getCenterY(),
				sparallelogram.getFirstAxisX(), sparallelogram.getFirstAxisY(), sparallelogram.getFirstAxisExtent(),
				sparallelogram.getSecondAxisX(), sparallelogram.getSecondAxisY(), sparallelogram.getSecondAxisExtent(),
				getPathIterator());
	}

	@Pure
	@Override
	default boolean intersects(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorPathShadow(
				0,
				path.getPathIterator(),
				new BasicPathShadow2afp(this),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = calculatesCrossingsPathIteratorPathShadow(
				0,
				iterator,
				new BasicPathShadow2afp(this),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == GeomConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	/** Replies the coordinate at the given index.
	 * The index is in [0;{@link #size()}*2).
	 *
	 * @param index the index.
	 * @return the coordinate at the given index.
	 */
	@Pure
	double getCoordAt(int index);

	/** Change the coordinates of the last inserted point.
	 *
	 * @param x the new x coordinate of the last point.
	 * @param y the new y coordinate of the last point.
	 */
	void setLastPoint(double x, double y);

	@Override
	default void setLastPoint(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
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
	default double getLength() {
		return calculatesPathLength(getPathIterator());
	}

	@Override
	default double getLengthSquared() {
		final double length = getLength();
		return length * length;
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
		return new FlatteningPathIterator<>(getPathIterator(null), flatness, DEFAULT_FLATTENING_LIMIT);
	}

	/** Replies an iterator on the path elements.
	 *
	 * <p>Only {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, and
	 * {@link PathElementType#CLOSE} types are returned by the iterator.
	 *
	 * <p>The amount of subdivision of the curved segments is controlled by the
	 * flatness parameter, which specifies the maximum distance that any point
	 * on the unflattened transformed curve can deviate from the returned
	 * flattened path segments. Note that a limit on the accuracy of the
	 * flattened path might be silently imposed, causing very small flattening
	 * parameters to be treated as larger values. This limit, if there is one,
	 * is defined by the particular implementation that is used.
	 *
	 * <p>The iterator for this class is not multi-threaded safe.
	 *
	 * @param transform is an optional affine Transform2D to be applied to the
	 *     coordinates as they are returned in the iteration, or {@code null} if
	 *     untransformed coordinates are desired.
	 * @param flatness is the maximum distance that the line segments used to approximate
	 *     the curved segments are allowed to deviate from any point on the original curve.
	 * @return an iterator on the path elements.
	 */
	@Pure
	default PathIterator2afp<IE> getPathIterator(Transform2D transform, double flatness) {
		return new FlatteningPathIterator<>(getPathIterator(transform), flatness, DEFAULT_FLATTENING_LIMIT);
	}

	/** Replies a path iterator on this path that is replacing the
	 * curves and corner arcs by line approximations.
	 *
	 * @return the iterator on the approximation.
	 * @see #getPathIterator()
	 * @see GeomConstants#SPLINE_APPROXIMATION_RATIO
	 */
	@Pure
	default PathIterator2afp<IE> getFlatteningPathIterator() {
		return new FlatteningPathIterator<>(
				getPathIterator(null),
				getGeomFactory().getSplineApproximationRatio(),
				Path2afp.DEFAULT_FLATTENING_LIMIT);
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

	@Override
	@Pure
	default P getCurrentPoint() {
		return getGeomFactory().newPoint(getCurrentX(), getCurrentY());
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
	 * @return {@code true} if the point was removed; {@code false} otherwise.
	 */
	boolean remove(double x, double y);

	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		Path2afp.calculatesDrawableElementBoundingBox(
				getPathIterator(getGeomFactory().getSplineApproximationRatio()),
				box);
	}

	/** Replies this path with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the path.
	 * @since 18.0
	 */
	default String toGeogebra() {
		return GeogebraUtil.toPolygonDefinition(2, toDoubleArray());
	}

	/** Abstract iterator on the path elements of the path.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractPathPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		private final Path2afp<?, ?, T, ?, ?, ?> path;

		/** Constructor.
		 * @param path the iterated path.
		 */
		public AbstractPathPathIterator(Path2afp<?, ?, T, ?, ?, ?> path) {
			assert path != null : AssertMessages.notNullParameter();
			this.path = path;
		}

		@Override
		public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
		}

		/** Replies the path.
		 *
		 * @return the path.
		 */
		public Path2afp<?, ?, T, ?, ?, ?> getPath() {
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

		private Point2D<?, ?> p1;

		private Point2D<?, ?> p2;

		private int typeIndex;

		private int coordIndex;

		private double movex;

		private double movey;

		/** Constructor.
		 * @param path the path to iterate on.
		 */
		public PathPathIterator(Path2afp<?, ?, T, ?, ?, ?> path) {
			super(path);
			this.p1 = new InnerComputationPoint2afp();
			this.p2 = new InnerComputationPoint2afp();
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new PathPathIterator<>(getPath());
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.typeIndex < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			final Path2afp<?, ?, T, ?, ?, ?> path = getPath();
			final int type = this.typeIndex;
			if (this.typeIndex >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			T element = null;
			switch (path.getPathElementTypeAt(type)) {
			case MOVE_TO:
				if ((this.coordIndex + 2) > (getPath().size() * 2)) {
					throw new NoSuchElementException();
				}
				this.movex = path.getCoordAt(this.coordIndex++);
				this.movey = path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey);
				element = getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				if ((this.coordIndex + 2) > (path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				element = getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
				if ((this.coordIndex + 4) > (path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				final double ctrlx = path.getCoordAt(this.coordIndex++);
				final double ctrly = path.getCoordAt(this.coordIndex++);
				this.p2.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						ctrlx, ctrly,
						this.p2.getX(), this.p2.getY());
				break;
			case CURVE_TO:
				if ((this.coordIndex + 6) > (path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				final double ctrlx1 = path.getCoordAt(this.coordIndex++);
				final double ctrly1 = path.getCoordAt(this.coordIndex++);
				final double ctrlx2 = path.getCoordAt(this.coordIndex++);
				final double ctrly2 = path.getCoordAt(this.coordIndex++);
				this.p2.set(
						getPath().getCoordAt(this.coordIndex++),
						getPath().getCoordAt(this.coordIndex++));
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						ctrlx1, ctrly1,
						ctrlx2, ctrly2,
						this.p2.getX(), this.p2.getY());
				break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				element = getGeomFactory().newClosePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case ARC_TO:
				throw new IllegalStateException();
			default:
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
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedPathPathIterator<T extends PathElement2afp> extends AbstractPathPathIterator<T> {

		private final Transform2D transform;

		private final Point2D<?, ?> p1;

		private final Point2D<?, ?> p2;

		private final Point2D<?, ?> ptmp1;

		private final Point2D<?, ?> ptmp2;

		private int typeIndex;

		private int coordIndex;

		private double movex;

		private double movey;

		/** Constructor.
		 * @param path the path to iterate on.
		 * @param transform the transformation to apply on the path.
		 */
		public TransformedPathPathIterator(Path2afp<?, ?, T, ?, ?, ?> path, Transform2D transform) {
			super(path);
			assert transform != null : AssertMessages.notNullParameter(1);
			this.transform = transform;
			this.p1 = new InnerComputationPoint2afp();
			this.p2 = new InnerComputationPoint2afp();
			this.ptmp1 = new InnerComputationPoint2afp();
			this.ptmp2 = new InnerComputationPoint2afp();
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new TransformedPathPathIterator<>(getPath(), this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.typeIndex < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			final Path2afp<?, ?, T, ?, ?, ?> path = getPath();
			if (this.typeIndex >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			T element = null;
			switch (path.getPathElementTypeAt(this.typeIndex++)) {
			case MOVE_TO:
				this.movex = path.getCoordAt(this.coordIndex++);
				this.movey = path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case QUAD_TO:
				this.p1.set(this.p2);
				this.ptmp1.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp1);
				this.p2.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case CURVE_TO:
				this.p1.set(this.p2);
				this.ptmp1.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp2);
				this.p2.set(
						path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(
						this.p1.getX(), this.p1.getY(),
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey);
				this.transform.transform(this.p2);
				element = getGeomFactory().newClosePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
				break;
			case ARC_TO:
				throw new IllegalStateException();
			default:
			}
			if (element == null) {
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
		private double lastNextX;

		/** The y of the last move segment replied by next.
		 */
		private double lastNextY;

		/** Constructor.
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 *     control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions
		 *     allowed for any curved segment
		 */
		public FlatteningPathIterator(PathIterator2afp<T> pathIterator, double flatness, int limit) {
			assert pathIterator != null : AssertMessages.notNullParameter(0);
			assert flatness >= 0. : AssertMessages.positiveOrZeroParameter(1);
			assert limit >= 0 : AssertMessages.positiveOrZeroParameter(2);
			this.pathIterator = pathIterator;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit;
			this.levels = new int[limit + 1];
			searchNext();
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
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
		 * @param offset the index into {@code coords} from which to
		 *          to start getting the values from the array
		 * @return the flatness of the quadratic curve that is defined by the
		 *          values in the specified array at the specified index.
		 */
		private static double getQuadSquaredFlatness(double[] coords, int offset) {
			return Segment2afp.calculatesDistanceSquaredLinePoint(
					coords[offset + 0], coords[offset + 1],
					coords[offset + 4], coords[offset + 5],
					coords[offset + 2], coords[offset + 3]);
		}

		/**
		 * Subdivides the quadratic curve specified by the coordinates
		 * stored in the {@code src} array at indices
		 * {@code srcoff} through {@code srcoff}&nbsp;+&nbsp;5
		 * and stores the resulting two subdivided curves into the two
		 * result arrays at the corresponding indices.
		 * Either or both of the {@code left} and {@code right}
		 * arrays can be {@code null} or a reference to the same array
		 * and offset as the {@code src} array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve.  Thus,
		 * it is possible to pass the same array for {@code left} and
		 * {@code right} and to use offsets such that
		 * {@code rightoff} equals {@code leftoff} + 4 in order
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
		 * @param offset the index of {@code coords} from which to begin
		 *          getting the end points and control points of the curve
		 * @return the square of the flatness of the {@code CubicCurve2D}
		 *          specified by the coordinates in {@code coords} at
		 *          the specified offset.
		 */
		private static double getCurveSquaredFlatness(double[] coords, int offset) {
			return Math.max(
					Segment2afp.calculatesDistanceSquaredSegmentPoint(
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 2],
							coords[offset + 3],
							coords[offset + 0],
							coords[offset + 1]),
					Segment2afp.calculatesDistanceSquaredSegmentPoint(
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 4],
							coords[offset + 5],
							coords[offset + 0],
							coords[offset + 1]));
		}

		/**
		 * Subdivides the cubic curve specified by the coordinates
		 * stored in the {@code src} array at indices {@code srcoff}
		 * through ({@code srcoff}&nbsp;+&nbsp;7) and stores the
		 * resulting two subdivided curves into the two result arrays at the
		 * corresponding indices.
		 * Either or both of the {@code left} and {@code right}
		 * arrays may be {@code null} or a reference to the same array
		 * as the {@code src} array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve. Thus,
		 * it is possible to pass the same array for {@code left}
		 * and {@code right} and to use offsets, such as {@code rightoff}
		 * equals ({@code leftoff} + 6), in order
		 * to avoid allocating extra storage for this common point.
		 * @param src the array holding the coordinates for the source curve
		 * @param srcoff the offset into the array of the beginning of the 6 source coordinates
		 * @param left the array for storing the coordinates for the first half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the 6 left coordinates
		 * @param right the array for storing the coordinates for the second half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the 6 right coordinates
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
			x1 = (x1 + ctrlx1) / 2;
			double ctrly1 = src[srcoff + 3];
			y1 = (y1 + ctrly1) / 2;
			double ctrlx2 = src[srcoff + 4];
			x2 = (x2 + ctrlx2) / 2;
			double ctrly2 = src[srcoff + 5];
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
				final T pathElement = this.pathIterator.next();
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
					this.currentX = this.hold[2];
					this.hold[this.holdIndex + 4] = this.currentX;
					this.currentY = this.hold[3];
					this.hold[this.holdIndex + 5] = this.currentY;
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

					// Now that we have subdivided, we have constructed two curves of one depth lower than the original
					// curve.  One of those curves is in the place of the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+4 and holdIndex+5 now contain the endpoint of the curve which can be the
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
					this.currentX = this.hold[4];
					this.hold[this.holdIndex + 6] = this.currentX;
					this.currentY = this.hold[5];
					this.hold[this.holdIndex + 7] = this.currentY;
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

					// Now that we have subdivided, we have constructed two curves of one depth lower than the original
					// curve.  One of those curves is in the place of the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+6 and holdIndex+7 now contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 6;
				this.levelIndex--;
				break;
			case ARC_TO:
				throw new IllegalStateException();
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
				throw new NoSuchElementException();
			}
			final T element;
			final PathElementType type = this.holdType;
			if (type != PathElementType.CLOSE) {
				final double x = this.hold[this.holdIndex + 0];
				final double y = this.hold[this.holdIndex + 1];
				if (type == PathElementType.MOVE_TO) {
					element = getGeomFactory().newMovePathElement(x, y);
				} else {
					element = getGeomFactory().newLinePathElement(
							this.lastNextX, this.lastNextY,
							x, y);
				}
				this.lastNextX = x;
				this.lastNextY = y;
			} else {
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
			return this.pathIterator.isPolyline() || (!this.pathIterator.isMultiParts() && !this.pathIterator.isPolygon());
		}

		@Pure
		@Override
		public boolean isCurved() {
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
		public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
			return this.pathIterator.getGeomFactory();
		}

	}

}


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

package org.arakhne.afc.math.geometry.d3.afp;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Path3D;
import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.d.Path3d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Fonctional interface that represented a 3D path.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
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
			ST extends Shape3afp<?, ?, IE, P, V, Q, B>,
			IT extends Path3afp<?, ?, IE, P, V, Q, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>,
			B extends AlignedBox3afp<?, ?, IE, P, V, Q, B>>
		extends Shape3afp<ST, IT, IE, P, V, Q, B>, Path3D<ST, IT, PathIterator3afp<IE>, P, V, Q, B> {

	/**
	 * Multiple of cubic &amp; quad curve size.
	 */
	int GROW_SIZE = 24;

	/**
	 * The default flatening depth limit.
	 */
	int DEFAULT_FLATENING_LIMIT = 10;

	/**
	 * The default winding rule: {@link PathWindingRule#NON_ZERO}.
	 */
	PathWindingRule DEFAULT_WINDING_RULE = PathWindingRule.NON_ZERO;

	/**
	 * Replies the point on the path that is closest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of {@code pi} is replying
	 * {@code false}.
	 * {@link #getClosestPointTo(Point3D)}
	 * avoids this restriction.
	 *
	 * @param pi is the iterator on the elements of the path.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @param result the closest point on the shape; or the point itself if it is inside the shape.
	 */
	static void getClosestPointTo(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z,
			Point3D<?, ?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue("isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(4);

		double bestDist = Double.POSITIVE_INFINITY;
		PathElement3afp pe;

		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		int crossings = 0;

		while (pi.hasNext()) {
			pe = pi.next();

			boolean foundCandidate = false;
			double candidateX = Double.NaN;
			double candidateY = Double.NaN;
			double candidateZ = Double.NaN;

			switch (pe.getType()) {
			case MOVE_TO:
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				candidateZ = pe.getToZ();
				break;
			case LINE_TO:
				double factor = Segment3afp.calculatesProjectedPointOnLine(x, y, z, pe.getFromX(), pe.getFromY(), pe.getFromZ(),
						pe.getToX(), pe.getToY(), pe.getToZ());
				factor = MathUtil.clamp(factor, 0, 1);
				foundCandidate = true;
				double vx = (pe.getToX() - pe.getFromX()) * factor;
				double vy = (pe.getToY() - pe.getFromY()) * factor;
				double vz = (pe.getToZ() - pe.getFromZ()) * factor;
				candidateX = pe.getFromX() + vx;
				candidateY = pe.getFromY() + vy;
				candidateZ = pe.getFromZ() + vz;
				crossings += Segment3afp.calculatesCrossingsFromPoint(x, y, z, pe.getFromX(), pe.getFromY(), pe.getFromZ(),
						pe.getToX(), pe.getToY(), pe.getToZ());
				break;
			case CLOSE:
				crossings += Segment3afp.calculatesCrossingsFromPoint(x, y, z, pe.getFromX(), pe.getFromY(), pe.getFromZ(),
						pe.getToX(), pe.getToY(), pe.getToZ());
				if ((crossings & mask) != 0) {
					result.set(x, y, z);
					return;
				}

				if (!pe.isEmpty()) {
					factor = Segment3afp.calculatesProjectedPointOnLine(x, y, z, pe.getFromX(), pe.getFromY(), pe.getFromZ(),
							pe.getToX(), pe.getToY(), pe.getToZ());
					factor = MathUtil.clamp(factor, 0, 1);
					vx = (pe.getToX() - pe.getFromX()) * factor;
					vy = (pe.getToY() - pe.getFromY()) * factor;
					vz = (pe.getToZ() - pe.getFromZ()) * factor;
					foundCandidate = true;
					candidateX = pe.getFromX() + vx;
					candidateY = pe.getFromY() + vy;
					candidateZ = pe.getFromY() + vz;
				}
				crossings = 0;
				break;
				//$CASES-OMITTED$
			default:
				throw new IllegalStateException(pe.getType().toString());
			}

			if (foundCandidate) {
				final double d = Point3D.getDistanceSquaredPointPoint(x, y, z, candidateX, candidateY, candidateZ);
				if (d < bestDist) {
					bestDist = d;
					result.set(candidateX, candidateY, candidateZ);
				}
			}
		}
	}

	/** Replies the point on the path of pi that is closest to the given shape.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of {@code pi} is replying
	 * {@code false}.
	 * {@link #getClosestPointTo(org.arakhne.afc.math.geometry.d3.Shape3D)} avoids this restriction.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param shape the shape to which the closest point must be computed.
	 * @param result the closest point on pi.
	 * @return {@code true} if a point was found. Otherwise {@code false}.
	 */
	@Unefficient
	static boolean getClosestPointTo(PathIterator3afp<? extends PathElement3afp> pi,
			PathIterator3afp<? extends PathElement3afp> shape, Point3D<?, ?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert shape != null : AssertMessages.notNullParameter(1);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(2);
		if (!pi.hasNext() || !shape.hasNext()) {
			return false;
		}
		/* TODO
		PathElement3afp pathElement1 = pi.next();
		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (shape.next().getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (!pi.hasNext() || !shape.hasNext()) {
			return false;
		}
		final AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> box = pi.getGeomFactory().newBox();
		calculatesDrawableElementBoundingBox(shape.restartIterations(), box);
		final ClosestPointPathShadow3afp shadow = new ClosestPointPathShadow3afp(shape.restartIterations(), box);
		int crossings = 0;
		double curx = pathElement1.getToX();
		double movx = curx;
		double cury = pathElement1.getToY();
		double movy = cury;
		double curz = pathElement1.getToZ();
		double movz = curz;
		double endx;
		double endy;
		double endz;
		while (pi.hasNext()) {
			pathElement1 = pi.next();
			switch (pathElement1.getType()) {
			case MOVE_TO:
				movx = pathElement1.getToX();
				curx = movx;
				movy = pathElement1.getToY();
				cury = movy;
				movz = pathElement1.getToZ();
				curz = movz;
				break;
			case LINE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				crossings = shadow.calculatesCrossings(crossings, curx, cury, curz, endx, endy, endz);
				if (crossings == GeomConstants.SHAPE_INTERSECTS) {
					result.set(shadow.getClosestPointInOtherShape());
					return true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					crossings = shadow.calculatesCrossings(crossings, curx, cury, curz, movx, movy, movz);
					if (crossings == GeomConstants.SHAPE_INTERSECTS) {
						result.set(shadow.getClosestPointInOtherShape());
						return true;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
				//$CASES-OMITTED$
			default:
				throw new IllegalArgumentException();
			}
		}
		if (curx == movx && cury == movy && curz == movz) {
			assert crossings != GeomConstants.SHAPE_INTERSECTS;
			final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
			if ((crossings & mask) != 0) {
				// Second path is inside the first shape
				result.set(shadow.getClosestPointInShadowShape());
				return true;
			}
		}
		result.set(shadow.getClosestPointInOtherShape());*/
		return true;
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Path3afp.getClosestPointTo(getPathIterator(
				getGeomFactory().getSplineApproximationRatio()), pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path3afp.getClosestPointTo(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					sphere.getCenterX(), sphere.getCenterY(), sphere.getCenterZ(), result);
		} else {
			Path3afp.getClosestPointTo(getPathIterator(), sphere.getCenterX(), sphere.getCenterY(), sphere.getCenterZ(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> AlignedBox) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path3afp.getClosestPointTo(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					AlignedBox.getPathIterator(), result);
		} else {
			Path3afp.getClosestPointTo(getPathIterator(), AlignedBox.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path3afp.getClosestPointTo(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					segment.getPathIterator(), result);
		} else {
			Path3afp.getClosestPointTo(getPathIterator(), segment.getPathIterator(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?, ?> path) {
		final P result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path3afp.getClosestPointTo(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					path.getPathIterator(), result);
		} else {
			Path3afp.getClosestPointTo(getPathIterator(), path.getPathIterator(), result);
		}
		return result;
	}

	/**
	 * Replies the point on the path that is farthest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of {@code pi}  is replying {@code false}. {@link #getFarthestPointTo(Point3D)}
	 * avoids this restriction.
	 *
	 * @param pi is the iterator on the elements of the path.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @param result the fartheset point on the shape.
	 */
	static void getFarthestPointTo(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z,
			Point3D<?, ?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue("isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(4);
		double bestDist = Double.NEGATIVE_INFINITY;
		PathElement3afp pe;
		// Only for internal use.
		final Point3D<?, ?, ?> point = new InnerComputationPoint3afp();

		while (pi.hasNext()) {
			pe = pi.next();

			switch (pe.getType()) {
			case MOVE_TO:
				break;
			case LINE_TO:
			case CLOSE:
				Segment3afp.calculatesFarthestPointTo(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(),
						pe.getToZ(), x, y, z, point);
				final double d = Point3D.getDistanceSquaredPointPoint(x, y, z, point.getX(), point.getY(), point.getZ());
				if (d > bestDist) {
					bestDist = d;
					result.set(point.getX(), point.getY(), point.getZ());
				}
				break;
				//$CASES-OMITTED$
			default:
				throw new IllegalStateException(pe.getType().toString());
			}
		}
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Path3afp.getFarthestPointTo(getPathIterator(
				getGeomFactory().getSplineApproximationRatio()), pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	/**
	 * Tests if the specified coordinates are inside the closed boundary of the specified {@link PathIterator3afp}.
	 *
	 * <p>This method provides a basic facility for implementors of the {@link Shape3afp} interface to implement support for the
	 * {@link Shape3afp#contains(double, double, double)} method.
	 *
	 * @param pi
	 *            the specified {@code PathIterator2f}
	 * @param x
	 *            the specified X coordinate
	 * @param y
	 *            the specified Y coordinate
	 * @param z
	 *            the specified Z coordinate
	 * @return {@code true} if the specified coordinates are inside the specified {@code PathIterator2f}; {@code false} otherwise
	 */
	static boolean containsPoint(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z) {
		assert pi != null : AssertMessages.notNullParameter(0);
		// Copied from the AWT API
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		final int cross = calculatesCrossingsFromPoint(0, pi, x, y, z, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (cross & mask) != 0;
	}

	/**
	 * Tests if the interior of the specified {@link PathIterator3afp} intersects the interior of a specified set of rectangular
	 * coordinates.
	 *
	 * @param pi
	 *            the specified {@link PathIterator3afp}.
	 * @param x
	 *            the specified X coordinate of the rectangle.
	 * @param y
	 *            the specified Y coordinate of the rectangle.
	 * @param z
	 *            the specified Y coordinate of the rectangle.
	 * @param width
	 *            the width of the specified rectangular coordinates.
	 * @param height
	 *            the height of the specified rectangular coordinates.
	 * @param depth
	 *            the depth of the specified rectangular coordinates.
	 * @return {@code true} if the specified {@link PathIterator3afp} and the interior of the specified set of rectangular
	 *         coordinates intersect each other; {@code false} otherwise.
	 */
	static boolean intersectsPathIteratorRectangle(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z,
			double width, double height, double depth) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(5);
		assert depth >= 0. : AssertMessages.positiveOrZeroParameter(6);
		if (width <= 0 || height <= 0 || depth <= 0) {
			return false;
		}
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;
	}

	/**
	 * Calculates the number of times the given path crosses the ray extending to the right from (px,py). If the point lies on a
	 * part of the path, then no crossings are counted for that intersection. +1 is added for each crossing where the Y coordinate
	 * is increasing -1 is added for each crossing where the Y coordinate is decreasing The return value is the sum of all
	 * crossings for every segment in the path. The path must start with a MOVE_TO, otherwise an exception is thrown.
	 *
	 * @param crossings
	 *            is the initial value for crossing.
	 * @param iterator
	 *            is the description of the path.
	 * @param px
	 *            is the reference point to test.
	 * @param py
	 *            is the reference point to test.
	 * @param pz
	 *            is the reference point to test.
	 * @param type
	 *            is the type of special computation to apply. If {@code null}, it is equivalent to
	 *            {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	static int calculatesCrossingsFromPoint(int crossings, PathIterator3afp<? extends PathElement3afp> iterator, double px,
			double py, double pz, CrossingComputationType type) {
		assert iterator != null : AssertMessages.notNullParameter(1);
		// Copied from the AWT API
		if (!iterator.hasNext()) {
			return 0;
		}
		PathElement3afp element;

		element = iterator.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		final GeomFactory3afp<?, ?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?, ?> subPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double movz = element.getToZ();
		double curx = movx;
		double cury = movy;
		double curz = movz;
		double endx;
		double endy;
		double endz;
		int numCrossings = crossings;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = element.getToX();
				curx = movx;
				movy = element.getToY();
				cury = movy;
				movz = element.getToZ();
				curz = movz;
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				if (endx == px && endy == py && endz == pz) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				numCrossings += Segment3afp.calculatesCrossingsFromPoint(px, py, pz, curx, cury, curz, endx, endy, endz);
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				if (endx == px && endy == py && endz == pz) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				// For internal use only
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), endx, endy, endz);
				numCrossings = calculatesCrossingsFromPoint(numCrossings,
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()), px, py, pz,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
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
					return GeomConstants.SHAPE_INTERSECTS;
				}
				// For internal use only
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getCtrlX2(),
						element.getCtrlY2(), element.getCtrlZ2(), endx, endy, endz);
				numCrossings = calculatesCrossingsFromPoint(numCrossings,
						subPath.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()), px, py, pz,
						CrossingComputationType.STANDARD);
				if (numCrossings == GeomConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					if (movx == px && movy == py && movz == pz) {
						return GeomConstants.SHAPE_INTERSECTS;
					}
					numCrossings += Segment3afp.calculatesCrossingsFromPoint(px, py, pz, curx, cury, curz, movx, movy, movz);
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
				//$CASES-OMITTED$
			default:
			}
		}

		assert numCrossings != GeomConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				if (movx == px && movy == py && movz == pz) {
					return GeomConstants.SHAPE_INTERSECTS;
				}
				numCrossings += Segment3afp.calculatesCrossingsFromPoint(px, py, pz, curx, cury, curz, movx, movy, movz);
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

	/**
	 * Compute the box that corresponds to the drawable elements of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element. The box fits the drawn lines and the drawn
	 * curves. The control points of the curves may be outside the output box. For obtaining the bounding box of the path's
	 * points, use {@link #calculatesControlPointBoundingBox(PathIterator3afp, AlignedBox3afp)}.
	 *
	 * @param iterator
	 *            the iterator on the path elements.
	 * @param box
	 *            the box to set.
	 * @return {@code true} if a drawable element was found.
	 * @see #calculatesControlPointBoundingBox(PathIterator3afp, AlignedBox3afp)
	 */
	static boolean calculatesDrawableElementBoundingBox(PathIterator3afp<?> iterator, AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		boolean foundOneLine = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double zmin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		double zmax = Double.NEGATIVE_INFINITY;
		PathElement3afp element;
		Path3afp<?, ?, ?, ?, ?, ?, ?> subPath;
		while (iterator.hasNext()) {
			element = iterator.next();
			if (element.isDrawable()) {
				switch(element.getType()) {
				case LINE_TO:
					if (element.getFromX() < xmin) {
						xmin = element.getFromX();
					}
					if (element.getFromY() < ymin) {
						ymin = element.getFromY();
					}
					if (element.getFromZ() < zmin) {
						zmin = element.getFromZ();
					}
					if (element.getFromX() > xmax) {
						xmax = element.getFromX();
					}
					if (element.getFromY() > ymax) {
						ymax = element.getFromY();
					}
					if (element.getFromZ() > zmax) {
						zmax = element.getFromZ();
					}
					if (element.getToX() < xmin) {
						xmin = element.getToX();
					}
					if (element.getToY() < ymin) {
						ymin = element.getToY();
					}
					if (element.getToZ() < zmin) {
						zmin = element.getToZ();
					}
					if (element.getToX() > xmax) {
						xmax = element.getToX();
					}
					if (element.getToY() > ymax) {
						ymax = element.getToY();
					}
					if (element.getToZ() > zmax) {
						zmax = element.getToZ();
					}
					foundOneLine = true;
					break;
				case CURVE_TO:
					subPath = new Path3d();
					subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
					subPath.curveTo(
							element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
							element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
							element.getToX(), element.getToY(), element.getToZ());
					if (calculatesDrawableElementBoundingBox(
							subPath.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO),
							box)) {
						if (box.getMinX()<xmin) xmin = box.getMinX();
						if (box.getMinY()<ymin) ymin = box.getMinY();
						if (box.getMinZ()<zmin) zmin = box.getMinZ();
						if (box.getMaxX()>xmax) xmax = box.getMaxX();
						if (box.getMinY()>ymax) ymax = box.getMinY();
						if (box.getMinZ()>zmax) zmax = box.getMinZ();
						foundOneLine = true;
					}
					break;
				case QUAD_TO:
					subPath = new Path3d();
					subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
					subPath.quadTo(
							element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
							element.getToX(), element.getToY(), element.getToZ());
					if (calculatesDrawableElementBoundingBox(
							subPath.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO),
							box)) {
						if (box.getMinX()<xmin) xmin = box.getMinX();
						if (box.getMinY()<ymin) ymin = box.getMinY();
						if (box.getMinZ()<zmin) zmin = box.getMinZ();
						if (box.getMaxX()>xmax) xmax = box.getMaxX();
						if (box.getMinY()>ymax) ymax = box.getMinY();
						if (box.getMinZ()>zmax) zmax = box.getMinZ();
						foundOneLine = true;
					}
					break;
				default:
					break;
				}
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

	/**
	 * Compute the box that corresponds to the control points of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element. The box fits the drawn lines and the drawn
	 * curves. The control points of the curves may be outside the output box. For obtaining the bounding box of the drawn lines
	 * and cruves, use {@link #calculatesDrawableElementBoundingBox(PathIterator3afp, AlignedBox3afp)}.
	 *
	 * @param iterator
	 *            the iterator on the path elements.
	 * @param box
	 *            the box to set.
	 * @return {@code true} if a control point was found.
	 * @see #calculatesDrawableElementBoundingBox(PathIterator3afp, AlignedBox3afp)
	 */
	static boolean calculatesControlPointBoundingBox(PathIterator3afp<?> iterator, AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		assert box != null : AssertMessages.notNullParameter(1);
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
			switch (element.getType()) {
			case LINE_TO:
				if (element.getFromX() < xmin) {
					xmin = element.getFromX();
				}
				if (element.getFromY() < ymin) {
					ymin = element.getFromY();
				}
				if (element.getFromZ() < zmin) {
					zmin = element.getFromZ();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getFromZ() > zmax) {
					zmax = element.getFromZ();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToZ() < zmin) {
					zmin = element.getToZ();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
				if (element.getToZ() > zmax) {
					zmax = element.getToZ();
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
				if (element.getFromZ() < zmin) {
					zmin = element.getFromZ();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getFromZ() > zmax) {
					zmax = element.getFromZ();
				}
				if (element.getCtrlX1() < xmin) {
					xmin = element.getCtrlX1();
				}
				if (element.getCtrlY1() < ymin) {
					ymin = element.getCtrlY1();
				}
				if (element.getCtrlZ1() < zmin) {
					zmin = element.getCtrlZ1();
				}
				if (element.getCtrlX1() > xmax) {
					xmax = element.getCtrlX1();
				}
				if (element.getCtrlY1() > ymax) {
					ymax = element.getCtrlY1();
				}
				if (element.getCtrlZ1() > zmax) {
					zmax = element.getCtrlZ1();
				}
				if (element.getCtrlX2() < xmin) {
					xmin = element.getCtrlX2();
				}
				if (element.getCtrlY2() < ymin) {
					ymin = element.getCtrlY2();
				}
				if (element.getCtrlZ2() < zmin) {
					zmin = element.getCtrlZ2();
				}
				if (element.getCtrlX2() > xmax) {
					xmax = element.getCtrlX2();
				}
				if (element.getCtrlY2() > ymax) {
					ymax = element.getCtrlY2();
				}
				if (element.getCtrlZ2() > zmax) {
					zmax = element.getCtrlZ2();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToZ() < zmin) {
					zmin = element.getToZ();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
				if (element.getToZ() > zmax) {
					zmax = element.getToZ();
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
				if (element.getFromZ() < zmin) {
					zmin = element.getFromZ();
				}
				if (element.getFromX() > xmax) {
					xmax = element.getFromX();
				}
				if (element.getFromY() > ymax) {
					ymax = element.getFromY();
				}
				if (element.getFromZ() > zmax) {
					zmax = element.getFromZ();
				}
				if (element.getCtrlX1() < xmin) {
					xmin = element.getCtrlX1();
				}
				if (element.getCtrlY1() < ymin) {
					ymin = element.getCtrlY1();
				}
				if (element.getCtrlZ1() < zmin) {
					zmin = element.getCtrlZ1();
				}
				if (element.getCtrlX1() > xmax) {
					xmax = element.getCtrlX1();
				}
				if (element.getCtrlY1() > ymax) {
					ymax = element.getCtrlY1();
				}
				if (element.getCtrlZ1() > zmax) {
					zmax = element.getCtrlZ1();
				}
				if (element.getToX() < xmin) {
					xmin = element.getToX();
				}
				if (element.getToY() < ymin) {
					ymin = element.getToY();
				}
				if (element.getToZ() < zmin) {
					zmin = element.getToZ();
				}
				if (element.getToX() > xmax) {
					xmax = element.getToX();
				}
				if (element.getToY() > ymax) {
					ymax = element.getToY();
				}
				if (element.getToZ() > zmax) {
					zmax = element.getToZ();
				}
				foundOneControlPoint = true;
				break;
				//$CASES-OMITTED$
			default:
			}
		}
		if (foundOneControlPoint) {
			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
		} else {
			box.clear();
		}
		return foundOneControlPoint;
	}

	/**
	 * Compute the total squared length of the path.
	 *
	 * @param iterator
	 *            the iterator on the path elements.
	 * @return the squared length of the path.
	 */
	static double calculatesLength(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		PathElement3afp pathElement = iterator.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		// only for internal use
		final GeomFactory3afp<?, ?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3afp<?, ?, ?, ?, ?, ?, ?> subPath;
		double movx = pathElement.getToX();
		double curx = movx;
		double movy = pathElement.getToY();
		double cury =  movy;
		double movz = pathElement.getToZ();
		double curz = movz;

		double length = 0;

		double endx;
		double endy;
		double endz;

		while (iterator.hasNext()) {
			pathElement = iterator.next();

			switch (pathElement.getType()) {
			case MOVE_TO:
				movx = pathElement.getToX();
				curx = movx;
				movy = pathElement.getToY();
				cury = movy;
				movz = pathElement.getToZ();
				curz = movz;
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				length += Point3D.getDistancePointPoint(curx, cury, curz, endx, endy, endz);
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
				subPath.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), endx, endy, endz);
				length += calculatesLength(subPath.getPathIterator(
						iterator.getGeomFactory().getSplineApproximationRatio()));
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
				subPath.curveTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(), endx, endy, endz);
				length += calculatesLength(subPath.getPathIterator(
						iterator.getGeomFactory().getSplineApproximationRatio()));
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					length += Point3D.getDistancePointPoint(curx, cury, curz, movx, movy, movz);
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
				//$CASES-OMITTED$
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

	/**
	 * Add the elements replied by the iterator into this path.
	 *
	 * @param iterator the iterator that provides the elements to add in the path.
	 */
	default void add(Iterator<? extends PathElement3afp> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		PathElement3afp element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch (element.getType()) {
			case MOVE_TO:
				moveTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case LINE_TO:
				lineTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case QUAD_TO:
				quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getToX(), element.getToY(),
						element.getToZ());
				break;
			case CURVE_TO:
				curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getCtrlX2(), element.getCtrlY2(),
						element.getCtrlZ2(), element.getToX(), element.getToY(), element.getToZ());
				break;
			case CLOSE:
				closePath();
				break;
				//$CASES-OMITTED$
			default:
			}
		}
	}

	/**
	 * Set the path.
	 *
	 * @param path
	 *            the path to copy.
	 */
	default void set(Path3afp<?, ?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		clear();
		add(path.getPathIterator());
	}

	/**
	 * Adds a point to the path by moving to the specified coordinates specified in double precision.
	 *
	 * @param x
	 *            the specified X coordinate
	 * @param y
	 *            the specified Y coordinate
	 * @param z
	 *            the specified Y coordinate
	 */
	void moveTo(double x, double y, double z);

	@Override
	default void moveTo(Point3D<?, ?, ?> position) {
		assert position != null : AssertMessages.notNullParameter();
		moveTo(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * Adds a point to the path by drawing a straight line from the current coordinates to the new specified coordinates specified
	 * in double precision.
	 *
	 * @param x
	 *            the specified X coordinate
	 * @param y
	 *            the specified Y coordinate
	 * @param z
	 *            the specified Y coordinate
	 */
	void lineTo(double x, double y, double z);

	@Override
	default void lineTo(Point3D<?, ?, ?> to) {
		assert to != null : AssertMessages.notNullParameter();
		lineTo(to.getX(), to.getY(), to.getZ());
	}

	/**
	 * Adds a curved segment, defined by two new points, to the path by drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x2,y2)}, using the specified point {@code (x1,y1)} as a quadratic
	 * parametric control point. All coordinates are specified in double precision.
	 *
	 * @param x1
	 *            the X coordinate of the quadratic control point
	 * @param y1
	 *            the Y coordinate of the quadratic control point
	 * @param z1
	 *            the Z coordinate of the quadratic control point
	 * @param x2
	 *            the X coordinate of the final end point
	 * @param y2
	 *            the Y coordinate of the final end point
	 * @param z2
	 *            the Y coordinate of the final end point
	 */
	void quadTo(double x1, double y1, double z1, double x2, double y2, double z2);

	@Override
	default void quadTo(Point3D<?, ?, ?> ctrl, Point3D<?, ?, ?> to) {
		assert ctrl != null : AssertMessages.notNullParameter(0);
		assert to != null : AssertMessages.notNullParameter(1);
		quadTo(ctrl.getX(), ctrl.getY(), ctrl.getZ(), to.getX(), to.getY(), to.getZ());
	}

	/**
	 * Adds a curved segment, defined by three new points, to the path by drawing a B&eacute;zier curve that intersects both the
	 * current coordinates and the specified coordinates {@code (x3,y3)}, using the specified points {@code (x1,y1)} and
	 * {@code (x2,y2)} as B&eacute;zier control points. All coordinates are specified in double precision.
	 *
	 * @param x1
	 *            the X coordinate of the first B&eacute;zier control point
	 * @param y1
	 *            the Y coordinate of the first B&eacute;zier control point
	 * @param z1
	 *            the Z coordinate of the first B&eacute;zier control point
	 * @param x2
	 *            the X coordinate of the second B&eacute;zier control point
	 * @param y2
	 *            the Y coordinate of the second B&eacute;zier control point
	 * @param z2
	 *            the Z coordinate of the second B&eacute;zier control point
	 * @param x3
	 *            the X coordinate of the final end point
	 * @param y3
	 *            the Y coordinate of the final end point
	 * @param z3
	 *            the Z coordinate of the final end point
	 */
	void curveTo(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3);

	@Override
	default void curveTo(Point3D<?, ?, ?> ctrl1, Point3D<?, ?, ?> ctrl2, Point3D<?, ?, ?> to) {
		assert ctrl1 != null : AssertMessages.notNullParameter(0);
		assert ctrl2 != null : AssertMessages.notNullParameter(1);
		assert to != null : AssertMessages.notNullParameter(2);
		curveTo(ctrl1.getX(), ctrl1.getY(), ctrl1.getZ(), ctrl2.getX(), ctrl2.getY(), ctrl2.getZ(), to.getX(), to.getY(),
				to.getZ());

	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final Point3D<?, ?, ?> c = getClosestPointTo(point);
		return c.getDistanceSquared(point);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final Point3D<?, ?, ?> c = getClosestPointTo(point);
		return c.getDistanceL1(point);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final Point3D<?, ?, ?> c = getClosestPointTo(point);
		return c.getDistanceLinf(point);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return containsPoint(getPathIterator(getGeomFactory().getSplineApproximationRatio()), x, y, z);
	}

	@Override
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		// Copied from AWT API
		if (prism.isEmpty()) {
			return false;
		}
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	/**
	 * Replies the coordinate at the given index. The index is in [0;{@link #size()}*2).
	 *
	 * @param index the index.
	 * @return the coordinate at the given index.
	 */
	@Pure
	double getCoordAt(int index);

	/**
	 * Change the coordinates of the last inserted point.
	 *
	 * @param x the new x coordinate of the last point.
	 * @param y the new y coordinate of the last point.
	 * @param z the new z coordinate of the last point.
	 */
	void setLastPoint(double x, double y, double z);

	@Override
	default void setLastPoint(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		setLastPoint(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Transform the current path. This function changes the current path.
	 *
	 * @param transform
	 *            is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform3D transform);

	@Override
	default double getLength() {
		return calculatesLength(getPathIterator());
	}

	@Override
	default double getLengthSquared() {
		final double length = getLength();
		return length * length;
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new PathElementPathIterator<>(this);
		}
		return new TransformedPathPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(double flatness) {
		return new FlatteningPathIterator<>(getWindingRule(), getPathIterator(null), flatness, DEFAULT_FLATENING_LIMIT);
	}

	/**
	 * Replies an iterator on the path elements.
	 *
	 * <p>Only {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, and
	 * {@link PathElementType#CLOSE} types are returned by the iterator.
	 *
	 * <p>The amount of subdivision of the curved segments is controlled by the flatness parameter, which specifies the maximum
	 * distance that any point on the unflattened transformed curve can deviate from the returned flattened path segments. Note
	 * that a limit on the accuracy of the flattened path might be silently imposed, causing very small flattening parameters to
	 * be treated as larger values. This limit, if there is one, is defined by the particular implementation that is used.
	 *
	 * <p>The iterator for this class is not multi-threaded safe.
	 *
	 * @param transform
	 *            is an optional affine Transform3D to be applied to the coordinates as they are returned in the iteration, or
	 *            {@code null} if untransformed coordinates are desired.
	 * @param flatness
	 *            is the maximum distance that the line segments used to approximate the curved segments are allowed to deviate
	 *            from any point on the original curve.
	 * @return an iterator on the path elements.
	 */
	@Pure
	default PathIterator3afp<IE> getPathIterator(Transform3D transform, double flatness) {
		return new FlatteningPathIterator<>(getWindingRule(), getPathIterator(transform), flatness, DEFAULT_FLATENING_LIMIT);
	}

	/**
	 * Replies the x coordinate of the last point in the path.
	 *
	 * @return the x coordinate of the last point in the path.
	 */
	@Pure
	double getCurrentX();

	/**
	 * Replies the y coordinate of the last point in the path.
	 *
	 * @return the y coordinate of the last point in the path.
	 */
	@Pure
	double getCurrentY();

	/**
	 * Replies the z coordinate of the last point in the path.
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

	@Override
	default Collection<P> toCollection() {
		return new PointCollection<>(this);
	}

	/**
	 * Remove the point with the given coordinates.
	 *
	 * <p>If the given coordinates do not match exactly a point in the path, nothing is removed.
	 *
	 * @param x
	 *            the x coordinate of the ponit to remove.
	 * @param y
	 *            the y coordinate of the ponit to remove.
	 * @param z
	 *            the z coordinate of the ponit to remove.
	 * @return {@code true} if the point was removed; {@code false} otherwise.
	 */
	boolean remove(double x, double y, double z);

	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		Path3afp.calculatesDrawableElementBoundingBox(getPathIterator(
				getGeomFactory().getSplineApproximationRatio()), box);
	}

	/** Replies this segment with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the segment.
	 * @since 18.0
	 */
	default String toGeogebra() {
		return GeogebraUtil.toPolygonDefinition(3, toDoubleArray());
	}

	/**
	 * Abstract iterator on the path elements of the path.
	 *
	 * @param <T>
	 *            the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractPathElementPathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		private final Path3afp<?, ?, T, ?, ?, ?, ?> path;

		/** Constructor.
		 * @param path the iterated path.
		 */
		public AbstractPathElementPathIterator(Path3afp<?, ?, T, ?, ?, ?, ?> path) {
			assert path != null : AssertMessages.notNullParameter();
			this.path = path;
		}

		@Override
		public GeomFactory3afp<T, ?, ?, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
		}

		/**
		 * Replies the path.
		 *
		 * @return the path.
		 */
		public Path3afp<?, ?, T, ?, ?, ?, ?> getPath() {
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

	/**
	 * A path iterator that does not transform the coordinates.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PathElementPathIterator<T extends PathElement3afp> extends AbstractPathElementPathIterator<T> {

		private Point3D<?, ?, ?> p1;

		private Point3D<?, ?, ?> p2;

		private int typeIndex;

		private int coordIndex;

		private double movex;

		private double movey;

		private double movez;

		/** Constructor.
		 * @param path the path to iterate on.
		 */
		public PathElementPathIterator(Path3afp<?, ?, T, ?, ?, ?, ?> path) {
			super(path);
			this.p1 = new InnerComputationPoint3afp();
			this.p2 = new InnerComputationPoint3afp();
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new PathElementPathIterator<>(getPath());
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.typeIndex < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			final Path3afp<?, ?, T, ?, ?, ?, ?> path = getPath();
			final int type = this.typeIndex++;
			if (type >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}

			this.p1.set(this.p2);

			T element = null;
			final int lastCoordIndex = path.size() * 3;
			switch (path.getPathElementTypeAt(type)) {
			case MOVE_TO:
				if ((this.coordIndex + 3) > lastCoordIndex) {
					// Ensure that a next call to next() will fail.
					this.typeIndex = path.getPathElementCount();
					throw new NoSuchElementException();
				}
				this.movex = path.getCoordAt(this.coordIndex++);
				this.movey = path.getCoordAt(this.coordIndex++);
				this.movez = path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newMovePathElement(this.movex, this.movey, this.movez);
				break;
			case LINE_TO:
				if ((this.coordIndex + 3) > lastCoordIndex) {
					// Ensure that a next call to next() will fail.
					this.typeIndex = path.getPathElementCount();
					throw new NoSuchElementException();
				}
				double x = path.getCoordAt(this.coordIndex++);
				double y = path.getCoordAt(this.coordIndex++);
				double z = path.getCoordAt(this.coordIndex++);
				this.p2.set(x, y, z);
				element = getGeomFactory().newLinePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), x, y, z);
				break;
			case QUAD_TO:
				if ((this.coordIndex + 6) > lastCoordIndex) {
					// Ensure that a next call to next() will fail.
					this.typeIndex = path.getPathElementCount();
					throw new NoSuchElementException();
				}
				final double ctrlx = path.getCoordAt(this.coordIndex++);
				final double ctrly = path.getCoordAt(this.coordIndex++);
				final double ctrlz = path.getCoordAt(this.coordIndex++);
				x = path.getCoordAt(this.coordIndex++);
				y = path.getCoordAt(this.coordIndex++);
				z = path.getCoordAt(this.coordIndex++);
				this.p2.set(x, y, z);
				element = getGeomFactory().newCurvePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), ctrlx, ctrly, ctrlz, x, y, z);
				break;
			case CURVE_TO:
				if ((this.coordIndex + 9) > lastCoordIndex) {
					// Ensure that a next call to next() will fail.
					this.typeIndex = path.getPathElementCount();
					throw new NoSuchElementException();
				}
				final double ctrlx1 = path.getCoordAt(this.coordIndex++);
				final double ctrly1 = path.getCoordAt(this.coordIndex++);
				final double ctrlz1 = path.getCoordAt(this.coordIndex++);
				final double ctrlx2 = path.getCoordAt(this.coordIndex++);
				final double ctrly2 = path.getCoordAt(this.coordIndex++);
				final double ctrlz2 = path.getCoordAt(this.coordIndex++);
				x = path.getCoordAt(this.coordIndex++);
				y = path.getCoordAt(this.coordIndex++);
				z = path.getCoordAt(this.coordIndex++);
				this.p2.set(x, y, z);
				element = getGeomFactory().newCurvePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), ctrlx1, ctrly1,
						ctrlz1, ctrlx2, ctrly2, ctrlz2, x, y, z);
				break;
			case CLOSE:
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newClosePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), this.movex, this.movey, this.movez);
				break;
				//$CASES-OMITTED$
			default:
			}
			if (element == null) {
				// Ensure that a next call to next() will fail.
				this.typeIndex = path.getPathElementCount();
				throw new NoSuchElementException();
			}

			return element;
		}

	}

	/**
	 * A path iterator that transforms the coordinates.
	 *
	 * @param <T>
	 *            the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedPathPathIterator<T extends PathElement3afp> extends AbstractPathElementPathIterator<T> {

		private final Transform3D transform;

		private final Point3D<?, ?, ?> p1;

		private final Point3D<?, ?, ?> p2;

		private final Point3D<?, ?, ?> ptmp1;

		private final Point3D<?, ?, ?> ptmp2;

		private int typeIndex;

		private int coordIndex;

		private double movex;

		private double movey;

		private double movez;

		/** Constructor.
		 * @param path the path to iterate on.
		 * @param transform the transformation to apply on the path.
		 */
		public TransformedPathPathIterator(Path3afp<?, ?, T, ?, ?, ?, ?> path, Transform3D transform) {
			super(path);
			assert transform != null : AssertMessages.notNullParameter(1);
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
			return this.typeIndex < getPath().getPathElementCount();
		}

		@Override
		public T next() {
			final Path3afp<?, ?, T, ?, ?, ?, ?> path = getPath();
			if (this.typeIndex >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			T element = null;
			switch (path.getPathElementTypeAt(this.typeIndex++)) {
			case MOVE_TO:
				this.movex = path.getCoordAt(this.coordIndex++);
				this.movey = path.getCoordAt(this.coordIndex++);
				this.movez = path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newMovePathElement(this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(path.getCoordAt(this.coordIndex++), path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newLinePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), this.p2.getX(),
						this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
				this.p1.set(this.p2);
				this.ptmp1.set(path.getCoordAt(this.coordIndex++), path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp1);
				this.p2.set(path.getCoordAt(this.coordIndex++), path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), this.ptmp1.getX(),
						this.ptmp1.getY(), this.ptmp1.getZ(), this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case CURVE_TO:
				this.p1.set(this.p2);
				this.ptmp1.set(path.getCoordAt(this.coordIndex++), path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(path.getCoordAt(this.coordIndex++), path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.ptmp2);
				this.p2.set(path.getCoordAt(this.coordIndex++), path.getCoordAt(this.coordIndex++),
						path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newCurvePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), this.ptmp1.getX(),
						this.ptmp1.getY(), this.ptmp1.getZ(), this.ptmp2.getX(), this.ptmp2.getY(), this.ptmp2.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newClosePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), this.p2.getX(),
						this.p2.getY(), this.p2.getZ());
				break;
				//$CASES-OMITTED$
			default:
			}
			if (element == null) {
				throw new NoSuchElementException();
			}
			return element;
		}

	}

	/**
	 * A path iterator that is flattening the path. This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @param <T>
	 *            the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class FlatteningPathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		/** Winding rule of the path.
		 */
		private final PathWindingRule windingRule;

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
		 *
		 */
		private double hold[] = new double[36];

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

		/** The y of the last move segment replied by next.
		 */
		private double lastNextZ;

		/**
		 * @param windingRule is the winding rule of the path.
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions allowed for any curved segment
		 */
		public FlatteningPathIterator(PathWindingRule windingRule, PathIterator3afp<T> pathIterator, double flatness, int limit) {
			assert windingRule != null : AssertMessages.notNullParameter(0);
			assert pathIterator != null : AssertMessages.notNullParameter(1);
			assert flatness > 0. : AssertMessages.positiveStrictlyParameter(2);
			assert limit >= 0 : AssertMessages.positiveOrZeroParameter(3);
			this.windingRule = windingRule;
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
				System.arraycopy(this.hold,
						this.holdIndex,
						newhold,
						this.holdIndex + GROW_SIZE,
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
		@Pure
		private static double getQuadSquaredFlatness(double coords[], int offset) {
			return Segment3afp.calculatesDistanceSquaredSegmentPoint(
					coords[offset + 0], coords[offset + 1], coords[offset + 2],
					coords[offset + 6], coords[offset + 7], coords[offset + 8],
					coords[offset + 3], coords[offset + 4], coords[offset + 5]);
		}

		/**
		 * Subdivides the quadratic curve specified by the coordinates
		 * stored in the {@code src} array at indices
		 * {@code srcoff} through {@code srcoff}&nbsp;+&nbsp;8
		 * and stores the resulting two subdivided curves into the two
		 * result arrays at the corresponding indices.
		 * Either or both of the {@code left} and {@code right}
		 * arrays can be {@code null} or a reference to the same array
		 * and offset as the {@code src} array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve.  Thus,
		 * it is possible to pass the same array for {@code left} and
		 * {@code right} and to use offsets such that
		 * {@code rightoff} equals {@code leftoff} + 6 in order
		 * to avoid allocating extra storage for this common point.
		 * @param src the array holding the coordinates for the source curve
		 * @param srcoff the offset into the array of the beginning of the
		 * the 9 source coordinates
		 * @param left the array for storing the coordinates for the first
		 * half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 * the 9 left coordinates
		 * @param right the array for storing the coordinates for the second
		 * half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 * the 9 right coordinates
		 */
		private static void subdivideQuad(double src[], int srcoff, double left[], int leftoff, double right[], int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double z1 = src[srcoff + 2];
			double ctrlx = src[srcoff + 3];
			double ctrly = src[srcoff + 4];
			double ctrlz = src[srcoff + 5];
			double x2 = src[srcoff + 6];
			double y2 = src[srcoff + 7];
			double z2 = src[srcoff + 8];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
				left[leftoff + 2] = z1;
			}
			if (right != null) {
				right[rightoff + 6] = x2;
				right[rightoff + 7] = y2;
				right[rightoff + 8] = z2;
			}
			x1 = (x1 + ctrlx) * .5;
			y1 = (y1 + ctrly) * .5;
			z1 = (z1 + ctrlz) * .5;
			x2 = (x2 + ctrlx) * .5;
			y2 = (y2 + ctrly) * .5;
			z2 = (z2 + ctrlz) * .5;
			ctrlx = (x1 + x2) * .5;
			ctrly = (y1 + y2) * .5;
			ctrlz = (z1 + z2) * .5;
			if (left != null) {
				left[leftoff + 3] = x1;
				left[leftoff + 4] = y1;
				left[leftoff + 5] = z1;
				left[leftoff + 6] = ctrlx;
				left[leftoff + 7] = ctrly;
				left[leftoff + 8] = ctrlz;
			}
			if (right != null) {
				right[rightoff + 0] = ctrlx;
				right[rightoff + 1] = ctrly;
				right[rightoff + 2] = ctrlz;
				right[rightoff + 3] = x2;
				right[rightoff + 4] = y2;
				right[rightoff + 5] = z2;
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
		 * @return the square of the flatness of the 3D cubic curve
		 *          specified by the coordinates in {@code coords} at
		 *          the specified offset.
		 */
		@Pure
		private static double getCurveSquaredFlatness(double coords[], int offset) {
			final double dist1 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
					coords[offset + 0],
					coords[offset + 1],
					coords[offset + 2],
					coords[offset + 9],
					coords[offset + 10],
					coords[offset + 11],
					coords[offset + 3],
					coords[offset + 4],
					coords[offset + 5]);
			final double dist2 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
					coords[offset + 0],
					coords[offset + 1],
					coords[offset + 2],
					coords[offset + 9],
					coords[offset + 10],
					coords[offset + 11],
					coords[offset + 6],
					coords[offset + 7],
					coords[offset + 8]);
			return Math.max(dist1, dist2);
		}

		/**
		 * Subdivides the cubic curve specified by the coordinates
		 * stored in the {@code src} array at indices {@code srcoff}
		 * through ({@code srcoff}&nbsp;+&nbsp;11) and stores the
		 * resulting two subdivided curves into the two result arrays at the
		 * corresponding indices.
		 * Either or both of the {@code left} and {@code right}
		 * arrays may be {@code null} or a reference to the same array
		 * as the {@code src} array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve. Thus,
		 * it is possible to pass the same array for {@code left}
		 * and {@code right} and to use offsets, such as {@code rightoff}
		 * equals ({@code leftoff} + 9), in order
		 * to avoid allocating extra storage for this common point.
		 * @param src the array holding the coordinates for the source curve
		 * @param srcoff the offset into the array of the beginning of the
		 * the 9 source coordinates
		 * @param left the array for storing the coordinates for the first
		 * half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 * the 9 left coordinates
		 * @param right the array for storing the coordinates for the second
		 * half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 * the 9 right coordinates
		 */
		private static void subdivideCurve(double src[], int srcoff, double left[], int leftoff, double right[], int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double z1 = src[srcoff + 2];
			double ctrlx1 = src[srcoff + 3];
			double ctrly1 = src[srcoff + 4];
			double ctrlz1 = src[srcoff + 5];
			double ctrlx2 = src[srcoff + 6];
			double ctrly2 = src[srcoff + 7];
			double ctrlz2 = src[srcoff + 8];
			double x2 = src[srcoff + 9];
			double y2 = src[srcoff + 10];
			double z2 = src[srcoff + 11];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
				left[leftoff + 2] = z1;
			}
			if (right != null) {
				right[rightoff + 9] = x2;
				right[rightoff + 10] = y2;
				right[rightoff + 11] = z2;
			}
			x1 = (x1 + ctrlx1) * .5;
			y1 = (y1 + ctrly1) * .5;
			y1 = (z1 + ctrlz1) * .5;
			x2 = (x2 + ctrlx2) * .5;
			y2 = (y2 + ctrly2) * .5;
			z2 = (z2 + ctrlz2) * .5;
			double centerx = (ctrlx1 + ctrlx2) * .5;
			double centery = (ctrly1 + ctrly2) * .5;
			double centerz = (ctrlz1 + ctrlz2) * .5;
			ctrlx1 = (x1 + centerx) * .5;
			ctrly1 = (y1 + centery) * .5;
			ctrlz1 = (z1 + centerz) * .5;
			ctrlx2 = (x2 + centerx) * .5;
			ctrly2 = (y2 + centery) * .5;
			ctrlz2 = (z2 + centerz) * .5;
			centerx = (ctrlx1 + ctrlx2) * .5;
			centery = (ctrly1 + ctrly2) * .5;
			centerz = (ctrlz1 + ctrlz2) * .5;
			if (left != null) {
				left[leftoff + 3] = x1;
				left[leftoff + 4] = y1;
				left[leftoff + 5] = z1;
				left[leftoff + 6] = ctrlx1;
				left[leftoff + 7] = ctrly1;
				left[leftoff + 8] = ctrlz1;
				left[leftoff + 9] = centerx;
				left[leftoff + 10] = centery;
				left[leftoff + 11] = centerz;
			}
			if (right != null) {
				right[rightoff + 0] = centerx;
				right[rightoff + 1] = centery;
				right[rightoff + 2] = centerz;
				right[rightoff + 3] = ctrlx2;
				right[rightoff + 4] = ctrly2;
				right[rightoff + 5] = ctrly2;
				right[rightoff + 6] = x2;
				right[rightoff + 7] = y2;
				right[rightoff + 8] = z2;
			}
		}

		private void searchNext() {
			int level;

			if (this.holdIndex >= this.holdEnd) {
				if (!this.pathIterator.hasNext()) {
					this.done = true;
					return;
				}
				PathElement3afp pathElement = this.pathIterator.next();
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
				this.currentZ = this.hold[2];
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
					this.holdIndex = this.hold.length - 9;
					this.holdEnd = this.hold.length - 3;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.currentZ;
					this.hold[this.holdIndex + 3] = this.hold[0];
					this.hold[this.holdIndex + 4] = this.hold[1];
					this.hold[this.holdIndex + 5] = this.hold[2];
					this.currentX = this.hold[3];
					this.hold[this.holdIndex + 6] = this.currentX;
					this.currentY = this.hold[4];
					this.hold[this.holdIndex + 7] = this.currentY;
					this.currentZ = this.hold[5];
					this.hold[this.holdIndex + 8] = this.currentZ;
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getQuadSquaredFlatness(this.hold, this.holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(6);
					subdivideQuad(
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
					++level;
					this.levels[this.levelIndex] = level;
					++this.levelIndex;
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
			case CURVE_TO:
				if (this.holdIndex >= this.holdEnd) {
					// Move the coordinates to the end of the array.
					this.holdIndex = this.hold.length - 12;
					this.holdEnd = this.hold.length - 3;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.currentZ;
					this.hold[this.holdIndex + 3] = this.hold[0];
					this.hold[this.holdIndex + 4] = this.hold[1];
					this.hold[this.holdIndex + 5] = this.hold[2];
					this.hold[this.holdIndex + 6] = this.hold[3];
					this.hold[this.holdIndex + 7] = this.hold[4];
					this.hold[this.holdIndex + 8] = this.hold[5];
					this.currentX = this.hold[6];
					this.hold[this.holdIndex + 9] = this.currentX;
					this.currentY = this.hold[7];
					this.hold[this.holdIndex + 10] = this.currentY;
					this.currentZ = this.hold[6];
					this.hold[this.holdIndex + 11] = this.currentZ;
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getCurveSquaredFlatness(this.hold, this.holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(9);
					subdivideCurve(
							this.hold, this.holdIndex,
							this.hold, this.holdIndex - 9,
							this.hold, this.holdIndex);
					this.holdIndex -= 9;

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					++level;
					this.levels[this.levelIndex] = level;
					++this.levelIndex;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+9 and holdIndex+10 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 9;
				--this.levelIndex;
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
				throw new NoSuchElementException("flattening iterator out of bounds"); 
			}

			final T element;
			final PathElementType type = this.holdType;
			if (type != PathElementType.CLOSE) {
				final double x = this.hold[this.holdIndex + 0];
				final double y = this.hold[this.holdIndex + 1];
				final double z = this.hold[this.holdIndex + 2];
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
			return this.windingRule;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return !this.pathIterator.isPolygon();
		}

		@Override
		public boolean isPolygon() {
			return this.pathIterator.isPolygon();
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
		public GeomFactory3afp<T, ?, ?, ?, ?> getGeomFactory() {
			return this.pathIterator.getGeomFactory();
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new FlatteningPathIterator<>(this.windingRule, this.pathIterator.restartIterations(), Math.sqrt(this.squaredFlatness), this.limit);
		}

	}

	/**
	 * An collection of the points of the path.
	 *
	 * @param <P>
	 *            the type of the points.
	 * @param <V>
	 *            the type of the vectors.
	 * @param <Q>
	 *            the type of the quaternions.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointCollection<P extends Point3D<? super P, ? super V, ? super Q>, V extends Vector3D<? super V, ? super P, ? super Q>, Q extends Quaternion<? super P, ? super V, ? super Q>>
	implements Collection<P> {

		private final Path3afp<?, ?, ?, P, V, Q, ?> path;

		/** Constructor.
		 * @param path the path to iterate on.
		 */
		public PointCollection(Path3afp<?, ?, ?, P, V, Q, ?> path) {
			assert path != null : AssertMessages.notNullParameter();
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
		public boolean contains(Object obj) {
			if (obj instanceof Point3D) {
				return this.path.containsControlPoint((Point3D<?, ?, ?>) obj);
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
		public <T> T[] toArray(T[] array) {
			assert array != null : AssertMessages.notNullParameter();
			final Iterator<P> iterator = new PointIterator<>(this.path);
			for (int i = 0; i < array.length && iterator.hasNext(); ++i) {
				array[i] = (T) iterator.next();
			}
			return array;
		}

		@Override
		public boolean add(P element) {
			if (element != null) {
				if (this.path.size() == 0) {
					this.path.moveTo(element.getX(), element.getY(), element.getZ());
				} else {
					this.path.lineTo(element.getX(), element.getY(), element.getZ());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object object) {
			if (object instanceof Point3D) {
				final Point3D<?, ?, ?> p = (Point3D<?, ?, ?>) object;
				return this.path.remove(p.getX(), p.getY(), p.getZ());
			}
			return false;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> collection) {
			assert collection != null : AssertMessages.notNullParameter();
			for (final Object obj : collection) {
				if ((!(obj instanceof Point3D)) || (!this.path.containsControlPoint((Point3D<?, ?, ?>) obj))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends P> collection) {
			assert collection != null : AssertMessages.notNullParameter();
			boolean changed = false;
			for (final P pts : collection) {
				if (add(pts)) {
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public boolean removeAll(Collection<?> collection) {
			assert collection != null : AssertMessages.notNullParameter();
			boolean changed = false;
			for (final Object obj : collection) {
				if (obj instanceof Point3D) {
					final Point3D<?, ?, ?> pts = (Point3D<?, ?, ?>) obj;
					if (this.path.remove(pts.getX(), pts.getY(), pts.getZ())) {
						changed = true;
					}
				}
			}
			return changed;
		}

		@Override
		public boolean retainAll(Collection<?> collection) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			this.path.clear();
		}

	}

	/**
	 * Iterator on the points of the path.
	 *
	 * @param <P>
	 *            the type of the points.
	 * @param <V>
	 *            the type of the vectors.
	 * @param <Q>
	 *            the type of the quaternions.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointIterator<P extends Point3D<? super P, ? super V, ? super Q>, V extends Vector3D<? super V, ? super P, ? super Q>, Q extends Quaternion<? super P, ? super V, ? super Q>>
	implements Iterator<P> {

		private final Path3afp<?, ?, ?, P, V, Q, ?> path;

		private int index;

		private P lastReplied;

		/** Constructor.
		 * @param path the path to iterate on.
		 */
		public PointIterator(Path3afp<?, ?, ?, P, V, Q, ?> path) {
			assert path != null : AssertMessages.notNullParameter();
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
			} catch (Throwable e) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			final Point3D<?, ?, ?> p = this.lastReplied;
			this.lastReplied = null;
			if (p == null) {
				throw new NoSuchElementException();
			}
			this.path.remove(p.getX(), p.getY(), p.getZ());
		}

	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Path3D;
import org.arakhne.afc.math.geometry.base.d3.PathIterator3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Functional interface that represented a 3D path.
 *
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
@SuppressWarnings("checkstyle:magicnumber")
public interface Path3afp<
		IT extends Path3afp<?, IE, P, V, Q, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, IE, P, V, Q, B>>
	extends Shape3afp<IT, IE, P, V, Q, B>, Path3D<IT, PathIterator3afp<IE>, P, V, Q, B> {

	/**
	 * Multiple of cubic &amp; quad curve size.
	 */
	int GROW_SIZE = 24;

	/**
	 * The default flattening depth limit.
	 */
	int DEFAULT_FLATENING_LIMIT = 10;

	/**
	 * The default winding rule: {@link PathWindingRule#NON_ZERO}.
	 */
	PathWindingRule DEFAULT_WINDING_RULE = PathWindingRule.NON_ZERO;

	@Override
	default Shape3DType getType() {
		return Shape3DType.PATH;
	}

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
	 * @param result the closest point on the shape; or the point itself if it is inside the shape. It cannot be {@code null}.
	 * @throws IllegalStateException invalid move.
	 */
	static void findsClosestPointToPoint(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z,
			Point3D<?, ?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue("isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(4);

		var bestDist = Double.POSITIVE_INFINITY;

		while (pi.hasNext()) {
			final var pe = pi.next();

			var foundCandidate = false;
			var candidateX = Double.NaN;
			var candidateY = Double.NaN;
			var candidateZ = Double.NaN;

			switch (pe.getType()) {
			case MOVE_TO:
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				candidateZ = pe.getToZ();
				break;
			case LINE_TO:
			case CLOSE:
				var factor = Segment3afp.calculatesProjectedPointOnLine(
						x, y, z,
						pe.getFromX(), pe.getFromY(), pe.getFromZ(),
						pe.getToX(), pe.getToY(), pe.getToZ());
				if (factor < 0.) {
					factor = 0.;
				} else if (factor > 1.) {
					factor = 1.;
				}
				foundCandidate = true;
				final var vx = (pe.getToX() - pe.getFromX()) * factor;
				final var vy = (pe.getToY() - pe.getFromY()) * factor;
				final var vz = (pe.getToZ() - pe.getFromZ()) * factor;
				candidateX = pe.getFromX() + vx;
				candidateY = pe.getFromY() + vy;
				candidateZ = pe.getFromZ() + vz;
				break;
				//$CASES-OMITTED$
			default:
				throw new IllegalStateException(pe.getType().toString());
			}

			if (foundCandidate) {
				final var d = Point3D.getDistanceSquaredPointPoint(x, y, z, candidateX, candidateY, candidateZ);
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
	 * {@link #getClosestPointTo(org.arakhne.afc.math.geometry.base.d3.Shape3D)} avoids this restriction.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param shape the shape to which the closest point must be computed.
	 * @param result the closest point on pi. It cannot be {@code null}.
	 * @return {@code true} if a point was found. Otherwise {@code false}.
	 * @throws IllegalArgumentException if the path is malformed.
	 */
	@Unefficient
	static boolean findsClosestPointToPath(PathIterator3afp<? extends PathElement3afp> pi,
			PathIterator3afp<? extends PathElement3afp> shape, Point3D<?, ?, ?> result) {
		return findsClosestPointToPath(pi, shape, result, null);
	}

	/** Replies the point on the path of pi that is closest to the given shape.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of {@code pi} is replying
	 * {@code false}.
	 * {@link #getClosestPointTo(org.arakhne.afc.math.geometry.base.d3.Shape3D)} avoids this restriction.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param shape the shape to which the closest point must be computed.
	 * @param result1 the closest point on pi. It cannot be {@code null}.
	 * @param result2 the closest point on the shape.  It can be {@code null}.
	 * @return {@code true} if a point was found. Otherwise {@code false}.
	 * @throws IllegalArgumentException if the path is malformed.
	 */
	@Unefficient
	static boolean findsClosestPointToPath(PathIterator3afp<? extends PathElement3afp> pi,
			PathIterator3afp<? extends PathElement3afp> shape, Point3D<?, ?, ?> result1, Point3D<?, ?, ?> result2) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert shape != null : AssertMessages.notNullParameter(1);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
		assert !shape.isCurved() : AssertMessages.invalidTrueValue(1, "isCurved"); //$NON-NLS-1$
		assert result1 != null : AssertMessages.notNullParameter(2);
		if (!pi.hasNext()) {
			return false;
		}
		var pathElement = pi.next();
		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (!pi.hasNext()) {
			return false;
		}
		var bestDistance = Double.POSITIVE_INFINITY;
		var curx = pathElement.getToX();
		var movx = curx;
		var cury = pathElement.getToY();
		var movy = cury;
		var curz = pathElement.getToZ();
		var movz = curz;
		final var point1 = new InnerComputationPoint3D();
		final var point2 = new InnerComputationPoint3D();
		var foundPoint = false;
		while (pi.hasNext()) {
			pathElement = pi.next();
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
				final var endx = pathElement.getToX();
				final var endy = pathElement.getToY();
				final var endz = pathElement.getToZ();
				findsClosestPointToSegment(
						shape.restartIterations(),
						curx, cury, curz, endx, endy, endz,
						point1, point2);
				var dist = Point3D.getDistanceSquaredPointPoint(
						point1.getX(), point1.getY(), point1.getZ(),
						point2.getX(), point2.getY(), point2.getZ());
				if (dist < bestDistance) {
					bestDistance = dist;
					result1.set(point2);
					if (result2 != null) {
						result2.set(point1);
					}
					foundPoint = true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					findsClosestPointToSegment(
							shape.restartIterations(),
							curx, cury, curz, movx, movy, movz,
							point1, point2);
					dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
							point2.getX(), point2.getY(), point2.getZ());
					if (dist < bestDistance) {
						bestDistance = dist;
						result1.set(point2);
						if (result2 != null) {
							result2.set(point1);
						}
						foundPoint = true;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case ARC_TO:
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalArgumentException();
			}
		}
		return foundPoint;
	}

	/** Replies the point on the path of pi that is closest to the given segment.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of {@code pi} is replying
	 * {@code false}.
	 * {@link #getClosestPointTo(org.arakhne.afc.math.geometry.base.d3.Shape3D)} avoids this restriction.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param z1 the z coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @param z2 the z coordinate of the second point of the segment.
	 * @param result the closest point on pi. It cannot be {@code null}.
	 * @return {@code true} if a point was found. Otherwise {@code false}.
	 * @throws IllegalArgumentException if the path os malformed.
	 */
	@Unefficient
	static boolean findsClosestPointToSegment(PathIterator3afp<? extends PathElement3afp> pi,
			double x1, double y1, double z1, double x2, double y2, double z2, Point3D<?, ?, ?> result) {
		return findsClosestPointToSegment(pi, x1, y1, z1, x2, y2, z2, result, null);
	}

	/** Replies the point on the path of pi that is closest to the given segment.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of {@code pi} is replying
	 * {@code false}.
	 * {@link #getClosestPointTo(org.arakhne.afc.math.geometry.base.d3.Shape3D)} avoids this restriction.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param z1 the z coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @param z2 the z coordinate of the second point of the segment.
	 * @param result1 the closest point on pi. It cannot be {@code null}.
	 * @param result2 the closest point on the segment. It can be {@code null}.
	 * @return {@code true} if a point was found. Otherwise {@code false}.
	 * @throws IllegalArgumentException if the path is malformed.
	 */
	@Unefficient
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean findsClosestPointToSegment(PathIterator3afp<? extends PathElement3afp> pi,
			double x1, double y1, double z1, double x2, double y2, double z2,
			Point3D<?, ?, ?> result1, Point3D<?, ?, ?> result2) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
		assert result1 != null : AssertMessages.notNullParameter(2);
		if (!pi.hasNext()) {
			return false;
		}
		var pathElement = pi.next();
		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (!pi.hasNext()) {
			return false;
		}
		var bestDistance = Double.POSITIVE_INFINITY;
		var curx = pathElement.getToX();
		var movx = curx;
		var cury = pathElement.getToY();
		var movy = cury;
		var curz = pathElement.getToZ();
		var movz = curz;
		final var point1 = new InnerComputationPoint3D();
		final var point2 = new InnerComputationPoint3D();
		var foundPoint = false;
		while (pi.hasNext()) {
			pathElement = pi.next();
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
				final var endx = pathElement.getToX();
				final var endy = pathElement.getToY();
				final var endz = pathElement.getToZ();
				Segment3afp.findsClosestPointToSegment(
						curx, cury, curz, endx, endy, endz,
						x1, y1, z1, x2, y2, z2,
						point1, point2);
				var dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
						point2.getX(), point2.getY(), point2.getZ());
				if (dist < bestDistance) {
					bestDistance = dist;
					result1.set(point1);
					if (result2 != null) {
						result2.set(point2);
					}
					foundPoint = true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					Segment3afp.findsClosestPointToSegment(
							curx, cury, curz, movx, movy, movz,
							x1, y1, z1, x2, y2, z2,
							point1, point2);
					dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
							point2.getX(), point2.getY(), point2.getZ());
					if (dist < bestDistance) {
						bestDistance = dist;
						result1.set(point1);
						if (result2 != null) {
							result2.set(point2);
						}
						foundPoint = true;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case ARC_TO:
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalArgumentException();
			}
		}
		return foundPoint;
	}

	/** Replies the point on the path of pi that is closest to the given aligned box.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying not-curved primitives, ie. if the
	 * {@link PathIterator3D#isCurved()} of {@code pi} is replying
	 * {@code false}.
	 * {@link #getClosestPointTo(org.arakhne.afc.math.geometry.base.d3.Shape3D)} avoids this restriction.
	 *
	 * @param pi is the iterator of path elements, on one of which the closest point is located.
	 * @param rx is the x coordinate of the rectangle.
	 * @param ry is the y coordinate of the rectangle.
	 * @param rz is the z coordinate of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @param rdepth is the depth of the rectangle.
	 * @param result the closest point on pi. It cannot be {@code null}.
	 * @return {@code true} if a point was found. Otherwise {@code false}.
	 * @throws IllegalArgumentException if the path os malformed.
	 */
	@Unefficient
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean findsClosestPointPathIteratorAlignedBox(PathIterator3afp<? extends PathElement3afp> pi,
			double rx, double ry, double rz, double rwidth, double rheight, double rdepth, Point3D<?, ?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue(0, "isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(2);
		if (!pi.hasNext()) {
			return false;
		}
		var pathElement = pi.next();
		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (!pi.hasNext()) {
			return false;
		}
		var bestDistance = Double.POSITIVE_INFINITY;
		var curx = pathElement.getToX();
		var movx = curx;
		var cury = pathElement.getToY();
		var movy = cury;
		var curz = pathElement.getToZ();
		var movz = curz;
		final var point1 = new InnerComputationPoint3D();
		final var point2 = new InnerComputationPoint3D();
		var foundPoint = false;
		while (pi.hasNext()) {
			pathElement = pi.next();
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
				final var endx = pathElement.getToX();
				final var endy = pathElement.getToY();
				final var endz = pathElement.getToZ();
				AlignedBox3afp.findsClosestPointAlignedBoxSegment(
						rx, ry, rz, rx + rwidth, ry + rheight, rz + rdepth,
						curx, cury, curz, endx, endy, endz,
						null, point1);
				var dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
						point2.getX(), point2.getY(), point2.getZ());
				if (dist < bestDistance) {
					bestDistance = dist;
					result.set(point1);
					foundPoint = true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					Segment3afp.findsClosestPointToSegment(
							curx, cury, curz, movx, movy, movz,
							rx, ry, rz, rwidth, rheight, rdepth,
							point1, point2);
					dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
							point2.getX(), point2.getY(), point2.getZ());
					if (dist < bestDistance) {
						bestDistance = dist;
						result.set(point1);
						foundPoint = true;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case ARC_TO:
			case QUAD_TO:
			case CURVE_TO:
			default:
				throw new IllegalArgumentException();
			}
		}
		return foundPoint;
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		Path3afp.findsClosestPointToPoint(getPathIterator(
				getGeomFactory().getSplineApproximationRatio()), pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		final var result = getGeomFactory().newPoint();
		if (isCurved()) {
			Path3afp.findsClosestPointToPoint(getPathIterator(getGeomFactory().getSplineApproximationRatio()),
					sphere.getCenterX(), sphere.getCenterY(), sphere.getCenterZ(), result);
		} else {
			Path3afp.findsClosestPointToPoint(getPathIterator(),
					sphere.getCenterX(), sphere.getCenterY(), sphere.getCenterZ(), result);
		}
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> alignedBox) {
		final PathIterator3afp<?> iterator;
		if (isCurved()) {
			iterator = getPathIterator(getGeomFactory().getSplineApproximationRatio());
		} else {
			iterator = getPathIterator();
		}
		final var result = getGeomFactory().newPoint();
		Path3afp.findsClosestPointPathIteratorAlignedBox(
				iterator,
				alignedBox.getMinX(), alignedBox.getMinY(), alignedBox.getMinZ(),
				alignedBox.getWidth(), alignedBox.getHeight(), alignedBox.getDepth(),
				result);
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final PathIterator3afp<?> iterator;
		if (isCurved()) {
			iterator = getPathIterator(getGeomFactory().getSplineApproximationRatio());
		} else {
			iterator = getPathIterator();
		}
		final var result = getGeomFactory().newPoint();
		Path3afp.findsClosestPointToSegment(iterator,
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2(),
				result);
		return result;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final PathIterator3afp<?> i1;
		if (isCurved()) {
			i1 = getPathIterator(getGeomFactory().getSplineApproximationRatio());
		} else {
			i1 = getPathIterator();
		}
		final PathIterator3afp<?> i2;
		if (path.isCurved()) {
			i2 = path.getPathIterator(path.getGeomFactory().getSplineApproximationRatio());
		} else {
			i2 = path.getPathIterator();
		}
		final var result = getGeomFactory().newPoint();
		Path3afp.findsClosestPointToPath(i1, i2, result);
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
	 * @param result the farthest point on the shape. It cannot be {@code null}.
	 * @throws IllegalStateException invalid move.
	 */
	static void findsFarthestPointToPoint(PathIterator3afp<? extends PathElement3afp> pi, double x, double y, double z,
			Point3D<?, ?, ?> result) {
		assert pi != null : AssertMessages.notNullParameter(0);
		assert !pi.isCurved() : AssertMessages.invalidTrueValue("isCurved"); //$NON-NLS-1$
		assert result != null : AssertMessages.notNullParameter(4);
		var bestDist = Double.NEGATIVE_INFINITY;

		// Only for internal use.
		final var point = new InnerComputationPoint3D();

		while (pi.hasNext()) {
			final var pe = pi.next();

			switch (pe.getType()) {
			case MOVE_TO:
				break;
			case LINE_TO:
			case CLOSE:
				Segment3afp.calculatesFarthestPointToPoint(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(),
						pe.getToZ(), x, y, z, point);
				final var d = Point3D.getDistanceSquaredPointPoint(x, y, z, point.getX(), point.getY(), point.getZ());
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
		final var point = getGeomFactory().newPoint();
		Path3afp.findsFarthestPointToPoint(getPathIterator(
				getGeomFactory().getSplineApproximationRatio()), pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
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
		//TODO
		return false;
	}

	/**
	 * Compute the box that corresponds to the drawable elements of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element. The box fits the drawn lines and the drawn
	 * curves. The control points of the curves may be outside the output box. For obtaining the bounding box of the path's
	 * points, use {@link #calculatesControlPointBoundingBox(PathIterator3afp, AlignedBox3afp)}.
	 *
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return {@code true} if a drawable element was found.
	 * @see #calculatesControlPointBoundingBox(PathIterator3afp, AlignedBox3afp)
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
	static boolean calculatesDrawableElementBoundingBox(PathIterator3afp<?> iterator, AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		assert box != null : AssertMessages.notNullParameter(1);
		var foundOneLine = false;
		var xmin = Double.POSITIVE_INFINITY;
		var ymin = Double.POSITIVE_INFINITY;
		var zmin = Double.POSITIVE_INFINITY;
		var xmax = Double.NEGATIVE_INFINITY;
		var ymax = Double.NEGATIVE_INFINITY;
		var zmax = Double.NEGATIVE_INFINITY;
		while (iterator.hasNext()) {
			final var element = iterator.next();
			if (element.isDrawable()) {
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
					foundOneLine = true;
					break;
				case QUAD_TO:
					final var subPath0 = iterator.getGeomFactory().newPath();
					subPath0.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
					subPath0.quadTo(
							element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
							element.getToX(), element.getToY(), element.getToZ());
					final var box0 = iterator.getGeomFactory().newBox();
					if (calculatesDrawableElementBoundingBox(
							subPath0.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
							box0)) {
						if (box0.getMinX() < xmin) {
							xmin = box0.getMinX();
						}
						if (box0.getMinY() < ymin) {
							ymin = box0.getMinY();
						}
						if (box0.getMinZ() < zmin) {
							zmin = box0.getMinZ();
						}
						if (box0.getMaxX() > xmax) {
							xmax = box0.getMaxX();
						}
						if (box0.getMaxY() > ymax) {
							ymax = box0.getMaxY();
						}
						if (box0.getMaxZ() > zmax) {
							zmax = box0.getMaxZ();
						}
						foundOneLine = true;
					}
					break;
				case CURVE_TO:
					final var subPath1 = iterator.getGeomFactory().newPath();
					subPath1.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
					subPath1.curveTo(
							element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
							element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
							element.getToX(), element.getToY(), element.getToZ());
					final var box1 = iterator.getGeomFactory().newBox();
					if (calculatesDrawableElementBoundingBox(
							subPath1.getPathIterator(iterator.getGeomFactory().getSplineApproximationRatio()),
							box1)) {
						if (box1.getMinX() < xmin) {
							xmin = box1.getMinX();
						}
						if (box1.getMinY() < ymin) {
							ymin = box1.getMinY();
						}
						if (box1.getMinZ() < zmin) {
							zmin = box1.getMinZ();
						}
						if (box1.getMaxX() > xmax) {
							xmax = box1.getMaxX();
						}
						if (box1.getMaxY() > ymax) {
							ymax = box1.getMaxY();
						}
						if (box1.getMaxZ() > zmax) {
							zmax = box1.getMaxZ();
						}
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
		} else {
			box.clear();
		}
		return foundOneLine;
	}

	/**
	 * Compute the box that corresponds to the control points of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element. The box fits the drawn lines and the drawn
	 * curves. The control points of the curves may be outside the output box. For obtaining the bounding box of the drawn lines
	 * and curves, use {@link #calculatesDrawableElementBoundingBox(PathIterator3afp, AlignedBox3afp)}.
	 *
	 * @param iterator
	 *            the iterator on the path elements.
	 * @param box
	 *            the box to set.
	 * @return {@code true} if a control point was found.
	 * @see #calculatesDrawableElementBoundingBox(PathIterator3afp, AlignedBox3afp)
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
	static boolean calculatesControlPointBoundingBox(PathIterator3afp<?> iterator, AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : AssertMessages.notNullParameter(0);
		assert box != null : AssertMessages.notNullParameter(1);
		var foundOneControlPoint = false;
		var xmin = Double.POSITIVE_INFINITY;
		var ymin = Double.POSITIVE_INFINITY;
		var zmin = Double.POSITIVE_INFINITY;
		var xmax = Double.NEGATIVE_INFINITY;
		var ymax = Double.NEGATIVE_INFINITY;
		var zmax = Double.NEGATIVE_INFINITY;
		while (iterator.hasNext()) {
			final var element = iterator.next();
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
	 * @throws IllegalArgumentException invalid move.
	 */
	static double calculatesLength(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		var pathElement = iterator.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}

		// only for internal use
		final var factory = iterator.getGeomFactory();
		var movx = pathElement.getToX();
		var curx = movx;
		var movy = pathElement.getToY();
		var cury =  movy;
		var movz = pathElement.getToZ();
		var curz = movz;

		var length = 0.;

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
				var elementLength = Point3D.getDistancePointPoint(curx, cury, curz, endx, endy, endz);
				length += elementLength;
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				var subPath = factory.newPath();
				subPath.moveTo(curx, cury, curz);
				subPath.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), endx, endy, endz);
				elementLength = calculatesLength(subPath.getPathIterator(
						iterator.getGeomFactory().getSplineApproximationRatio()));
				length += elementLength;
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = factory.newPath();
				subPath.moveTo(curx, cury, curz);
				subPath.curveTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(), endx, endy, endz);
				elementLength = calculatesLength(subPath.getPathIterator(
						iterator.getGeomFactory().getSplineApproximationRatio()));
				length += elementLength;
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					elementLength = Point3D.getDistancePointPoint(curx, cury, curz, movx, movy, movz);
					length += elementLength;
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

	/** Replies this shape as the same path iterator as the given one.
	 *
	 * <p>The equality test does not flatten the paths. It means that
	 * is function has is functionnality equivalent to: <pre><code>
	 * PathIterator2D it = this.getPathIterator();
	 * while (it.hasNext() &amp;&amp; pathIterator.hasNext()) {
	 *   PathElement2D e1 = it.next();
	 *   PathElement2D e2 = it.next();
	 *   if (!e1.equals(e2)) return false;
	 * }
	 * return !it.hasNext() &amp;&amp; !pathIterator.hasNext();
	 * </code></pre>
	 *
	 * @param pathIterator the path iterator to compare to the one of this shape.
	 * @return {@code true} if the path iterator of this shape replies the same
	 *     elements as the given path iterator.
	 */
	@Pure
	default boolean equalsToPathIterator(PathIterator3D<?> pathIterator) {
		final var localIterator = getPathIterator();
		if (pathIterator == null) {
			return false;
		}
		while (localIterator.hasNext() && pathIterator.hasNext()) {
			final var element1 = localIterator.next();
			final var element2 = pathIterator.next();
			if (!Objects.equals(element1, element2)) {
				return false;
			}
		}
		return !localIterator.hasNext() && !pathIterator.hasNext();
	}

	/**
	 * Add the elements replied by the iterator into this path.
	 *
	 * @param iterator the iterator that provides the elements to add in the path.
	 */
	default void add(Iterator<? extends PathElement3afp> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		while (iterator.hasNext()) {
			final var element = iterator.next();
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
	default void set(Path3afp<?, ?, ?, ?, ?, ?> path) {
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
	@SuppressWarnings("checkstyle:parameternumber")
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
		final var c = getClosestPointTo(point);
		return c.getDistanceSquared(point);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var c = getClosestPointTo(point);
		return c.getDistanceL1(point);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var c = getClosestPointTo(point);
		return c.getDistanceLinf(point);
	}

	@Pure
	@Override
	@Unefficient
	default boolean contains(double x, double y, double z) {
		final var point = new InnerComputationPoint3D(x, y, z);
		final var distance = getDistanceSquared(point);
		return MathUtil.isEpsilonZero(distance);
	}

	@Pure
	@Override
	@Inline(value = "MathUtil.isEpsilonZero(($0).getDistanceSquared($1))", imported = {MathUtil.class}, constantExpression = true)
	@Unefficient
	default boolean contains(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var distance = getDistanceSquared(point);
		return MathUtil.isEpsilonZero(distance);
	}

	@Override
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		return box.isDegeneratedPoint() && contains(box.getMinX(), box.getMinY(), box.getMinZ());
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		// Copied from AWT API
		if (prism.isEmpty()) {
			return false;
		}
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		//TODO
		return false;
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
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
		final var length = getLength();
		return length * length;
	}

	/** Replies an iterator on the path elements.
	 *
	 * <p>The iterator for this class is not multi-threaded safe.
	 *
	 * @return an iterator on the path elements.
	 */
	@Pure
	default PathIterator3afp<IE> getPathIterator() {
		return getPathIterator(null);
	}

	/** Replies the elements of the paths.
	 *
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	@Pure
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new PathElementPathIterator<>(this);
		}
		return new TransformedPathPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(double flatness) {
		return new FlatteningPathIterator<>(getPathIterator(null), flatness, DEFAULT_FLATENING_LIMIT);
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
		return new FlatteningPathIterator<>(getPathIterator(transform), flatness, DEFAULT_FLATENING_LIMIT);
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
	 * Otherwise, the following rules apply:
	 * <ul>
	 * <li>If the point is for a {@code MOVE_TO}, a {@code LINE_TO} or a {@code CLOSE} or the end
	 *     points or a curve, the associated path elements are removed;</li>
	 * <li>If the point is the control point of a quadratic curve; then the curve is converted to a line.</li>
	 * <li>If the point is one of the control point of a cubic spline; then the spline is converted to a quadratic curve.</li>
	 * </ul>
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
		Path3afp.calculatesDrawableElementBoundingBox(getPathIterator(), box);
	}

	/** Replies this segment with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the segment.
	 * @since 18.0
	 */
	@Override
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

		private final Path3afp<?, T, ?, ?, ?, ?> path;

		/** Constructor.
		 * @param path the iterated path.
		 */
		public AbstractPathElementPathIterator(Path3afp<?, T, ?, ?, ?, ?> path) {
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
		public Path3afp<?, T, ?, ?, ?, ?> getPath() {
			return this.path;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
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
		public PathElementPathIterator(Path3afp<?, T, ?, ?, ?, ?> path) {
			super(path);
			this.p1 = new InnerComputationPoint3D();
			this.p2 = new InnerComputationPoint3D();
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
			final Path3afp<?, T, ?, ?, ?, ?> path = getPath();
			final var type = this.typeIndex++;
			if (type >= path.getPathElementCount()) {
				throw new NoSuchElementException();
			}

			this.p1.set(this.p2);

			T element = null;
			final var lastCoordIndex = path.size() * 3;
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
				element = getGeomFactory().newCurvePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx, ctrly, ctrlz, x, y, z);
				break;
			case CURVE_TO:
				if ((this.coordIndex + 9) > lastCoordIndex) {
					// Ensure that a next call to next() will fail.
					this.typeIndex = path.getPathElementCount();
					throw new NoSuchElementException();
				}
				final var ctrlx1 = path.getCoordAt(this.coordIndex++);
				final var ctrly1 = path.getCoordAt(this.coordIndex++);
				final var ctrlz1 = path.getCoordAt(this.coordIndex++);
				final var ctrlx2 = path.getCoordAt(this.coordIndex++);
				final var ctrly2 = path.getCoordAt(this.coordIndex++);
				final var ctrlz2 = path.getCoordAt(this.coordIndex++);
				x = path.getCoordAt(this.coordIndex++);
				y = path.getCoordAt(this.coordIndex++);
				z = path.getCoordAt(this.coordIndex++);
				this.p2.set(x, y, z);
				element = getGeomFactory().newCurvePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(), ctrlx1, ctrly1,
						ctrlz1, ctrlx2, ctrly2, ctrlz2, x, y, z);
				break;
			case CLOSE:
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newClosePathElement(this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.movex, this.movey, this.movez);
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
		public TransformedPathPathIterator(Path3afp<?, T, ?, ?, ?, ?> path, Transform3D transform) {
			super(path);
			assert transform != null : AssertMessages.notNullParameter(1);
			this.transform = transform;
			this.p1 = new InnerComputationPoint3D();
			this.p2 = new InnerComputationPoint3D();
			this.ptmp1 = new InnerComputationPoint3D();
			this.ptmp2 = new InnerComputationPoint3D();
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
			final Path3afp<?, T, ?, ?, ?, ?> path = getPath();
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
	 * Curve:
			//a=Curve((1-t)^(3) CA + 3 (1-t)^(2) t CB + 3 (1-t) t^(2) CC + t^(3) CD, t,0,1)
			// =Curve(
			//	x(CA) (1-t)^(3)+3 x(CB) (1-t)^(2) t+3 x(CC) (1-t) t^(2)+x(CD) t^(3),
			//	y(CA) (1-t)^(3)+3 y(CB) (1-t)^(2) t+3 y(CC) (1-t) t^(2)+y(CD) t^(3),
			//	z(CA) (1-t)^(3)+3 z(CB) (1-t)^(2) t+3 z(CC) (1-t) t^(2)+z(CD) t^(3),
			//	t,0,1)
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

		/** The next element to be replied by the iterator.
		 */
		private T nextElement;

		/** List of flatten points from a curve. If this attribute is {@code null},
		 * there is no flatten curve under iteration.
		 */
		private List<FPoint> flattenPoints;

		/** Index of the segment to reply for the {@code flattenPoints}.
		 */
		private int flattenPointsIndex;

		/** Constructor.
		 *
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions allowed for any curved segment
		 */
		public FlatteningPathIterator(PathIterator3afp<T> pathIterator, double flatness, int limit) {
			this(pathIterator, limit, flatness * flatness);
		}

		/** Constructor.
		 *
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param limit the maximum number of recursive subdivisions allowed for any curved segment
		 * @param squaredFlatness the squared value of the maximum allowable distance between the control points and the flattened curve
		 */
		private FlatteningPathIterator(PathIterator3afp<T> pathIterator, int limit, double squaredFlatness) {
			assert pathIterator != null : AssertMessages.notNullParameter(1);
			assert squaredFlatness > 0. : AssertMessages.positiveStrictlyParameter(3);
			assert limit >= 0 : AssertMessages.positiveOrZeroParameter(2);
			this.pathIterator = pathIterator;
			this.squaredFlatness = squaredFlatness;
			this.limit = limit;
			searchNext();
		}

		private void searchNext() {
			this.nextElement = null;
			if (this.flattenPoints != null) {
				if (this.flattenPointsIndex + 1 < this.flattenPoints.size()) {
					final var p1 = this.flattenPoints.get(this.flattenPointsIndex);
					++this.flattenPointsIndex;
					final var p2 = this.flattenPoints.get(this.flattenPointsIndex);
					this.nextElement = getGeomFactory().newLinePathElement(
							p1.x(), p1.y(), p1.z(),
							p2.x(), p2.y(), p2.z());
				} else {
					this.flattenPoints = null;
				}
			}
			if (this.nextElement == null && this.pathIterator.hasNext()) {
				final var originalElement = this.pathIterator.next();
				switch (originalElement.getType()) {
				case MOVE_TO:
				case LINE_TO:
				case CLOSE:
					this.nextElement = originalElement;
					break;
				case QUAD_TO:
					this.flattenPoints = flattenQuad(
							originalElement.getFromX(),
							originalElement.getFromY(),
							originalElement.getFromZ(),
							originalElement.getCtrlX1(),
							originalElement.getCtrlY1(),
							originalElement.getCtrlZ1(),
							originalElement.getToX(),
							originalElement.getToY(),
							originalElement.getToZ());
					if (this.flattenPoints.size() < 2) {
						this.flattenPoints = null;
					} else {
						final var p1 = this.flattenPoints.get(0);
						final var p2 = this.flattenPoints.get(1);
						this.nextElement = getGeomFactory().newLinePathElement(
								p1.x(), p1.y(), p1.z(),
								p2.x(), p2.y(), p2.z());
						this.flattenPointsIndex = 1;
					}
					break;
				case CURVE_TO:
					this.flattenPoints = flattenCubic(
							originalElement.getFromX(),
							originalElement.getFromY(),
							originalElement.getFromZ(),
							originalElement.getCtrlX1(),
							originalElement.getCtrlY1(),
							originalElement.getCtrlZ1(),
							originalElement.getCtrlX2(),
							originalElement.getCtrlY2(),
							originalElement.getCtrlZ2(),
							originalElement.getToX(),
							originalElement.getToY(),
							originalElement.getToZ());
					if (this.flattenPoints.size() < 2) {
						this.flattenPoints = null;
					} else {
						final var p1 = this.flattenPoints.get(0);
						final var p2 = this.flattenPoints.get(1);
						this.nextElement = getGeomFactory().newLinePathElement(
								p1.x(), p1.y(), p1.z(),
								p2.x(), p2.y(), p2.z());
						this.flattenPointsIndex = 1;
					}
					break;
				case ARC_TO:
				default:
					throw new UnsupportedOperationException();
				}
			}
		}

		/**
		 * Flattens a 3D quadratic Bézier curve into a polyline.
		 *
		 * <p>The curve is defined by the standard parametric equation:
		 * <pre>
		 *   P(t) = (1-t)² QA + 2(1-t)t QB + t² QC,  t in [0, 1]
		 * </pre>
		 * where QA is the start point, QB the control point, and QC the end point.
		 *
		 * <p><b>Algorithm — recursive De Casteljau subdivision</b><br>
		 * For a quadratic Bézier, the maximum perpendicular deviation of the chord
		 * [QA, QC] from the exact curve occurs at t = 0.5 and equals:
		 * <pre>
		 *   deviation = ‖ midCurve − midChord ‖
		 *             = ‖ (QA/4 + QB/2 + QC/4) − (QA/2 + QC/2) ‖
		 *             = ‖ QB/2 − QA/4 − QC/4 ‖
		 * </pre>
		 * This closed-form expression makes the flatness test O(1) per recursion level,
		 * with no sampling required.
		 *
		 * <p>When the deviation exceeds {@code flatness}, the curve is split at t = 0.5
		 * via De Casteljau into two sub-curves, each recursed independently. Recursion
		 * stops either when the deviation is within tolerance or when {@code maxIterations}
		 * depth is reached (safety cap).
		 *
		 * @param qax x coordinate of the start point {@code QA}.
		 * @param qay y coordinate of the start point {@code QA}.
		 * @param qaz z coordinate of the start point {@code QA}.
		 * @param qbx x coordinate of the control point {@code QB}.
		 * @param qby y coordinate of the control point {@code QB}.
		 * @param qbz z coordinate of the control point {@code QB}.
		 * @param qcx x coordinate of the end point {@code QC}.
		 * @param qcy y coordinate of the end point {@code QC}.
		 * @param qcz z coordinate of the end point {@code QC}.
		 * @return the points, starting with {@code QA} and ending with
		 *     {@code QC}, representing the approximating polyline.
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		protected List<FPoint> flattenQuad(
				double qax, double qay, double qaz,
				double qbx, double qby, double qbz,
				double qcx, double qcy, double qcz) {
			// Pre-allocate a reasonably sized list; typical curves need O(log(1/flatness)) points.
			final var points = new ArrayList<FPoint>(64);
			// Add the first point
			points.add(new FPoint(qax, qay, qaz));
			// Subdivise
			subdivideQuad(
					qax, qay, qaz,
					qbx, qby, qbz,
					qcx, qcy, qcz,
					0,
					points);
			return points;
		}

		@SuppressWarnings("checkstyle:parameternumber")
		private void subdivideQuad(
				double qax, double qay, double qaz,
				double qbx, double qby, double qbz,
				double qcx, double qcy, double qcz,
				int depth,
				List<FPoint> points) {
			// Deviation test:
			// midCurve = P(0.5) = 0.25*QA + 0.5*QB + 0.25*QC
			// midChord = 0.5*QA + 0.5*QC
			// delta    = midCurve - midChord = 0.5*QB - 0.25*QA - 0.25*QC
			//          = 0.25*(2QB - QA - QC)
			final var dx = .25 * (2. * qbx - qax - qcx);
			final var dy = .25 * (2. * qby - qay - qcy);
			final var dz = .25 * (2. * qbz - qaz - qcz);
			final var squaredDeviation = dx * dx + dy * dy + dz * dz;

			if (depth >= this.limit || squaredDeviation <= this.squaredFlatness) {
				// Chord [QA, QC] is flat enough
				points.add(new FPoint(qcx, qcy, qcz));
			} else {
				// De Casteljau split at t = 0.5:
				// Left  sub-curve: L0=QA,  L1=M_AB,  L2=M_mid
				// Right sub-curve: R0=M_mid, R1=M_BC, R2=QC
				final var mabx = .5 * (qax + qbx);
				final var maby = .5 * (qay + qby);
				final var mabz = .5 * (qaz + qbz);
				final var mbcx = .5 * (qbx + qcx);
				final var mbcy = .5 * (qby + qcy);
				final var mbcz = .5 * (qbz + qcz);
				final var midx = .5 * (mabx + mbcx);
				final var midy = .5 * (maby + mbcy);
				final var midz = .5 * (mabz + mbcz);
				final var next = depth + 1;
				subdivideQuad(qax, qay, qaz, mabx, maby, mabz, midx, midy, midz, next, points);
				subdivideQuad(midx, midy, midz, mbcx, mbcy, mbcz, qcx, qcy, qcz, next, points);
			}
		}

		/**
		 * Flattens a cubic Bézier curve into a polyline approximation.
		 *
		 * @param qax x coordinate of the start point {@code QA}.
		 * @param qay y coordinate of the start point {@code QA}.
		 * @param qaz z coordinate of the start point {@code QA}.
		 * @param qbx x coordinate of the first control point {@code QB}.
		 * @param qby y coordinate of the first control point {@code QB}.
		 * @param qbz z coordinate of the first control point {@code QB}.
		 * @param qcx x coordinate of the second control point {@code QC}.
		 * @param qcy y coordinate of the second control point {@code QC}.
		 * @param qcz z coordinate of the second control point {@code QC}.
		 * @param qdx x coordinate of the end point {@code QD}.
		 * @param qdy y coordinate of the end point {@code QD}.
		 * @param qdz z coordinate of the end point {@code QD}.
		 * @return the points, starting with {@code QA} and ending with
		 *     {@code QD}, representing the approximating polyline.
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		protected List<FPoint> flattenCubic(
				double qax, double qay, double qaz,
				double qbx, double qby, double qbz,
				double qcx, double qcy, double qcz,
				double qdx, double qdy, double qdz) {
			// Pre-allocate a reasonably sized list; typical curves need O(log(1/flatness)) points.
			final var points = new ArrayList<FPoint>(64);
			// Add the first point
			points.add(new FPoint(qax, qay, qaz));
			// Subdivise
			subdivideCubic(
					qax, qay, qaz,
					qbx, qby, qbz,
					qcx, qcy, qcz,
					qdx, qdy, qdz,
					0,
					points);
			// Add the last point
			points.add(new FPoint(qdx, qdy, qdz));
			return points;
		}

		@SuppressWarnings("checkstyle:parameternumber")
		private void subdivideCubic(
				double qax, double qay, double qaz,
				double qbx, double qby, double qbz,
				double qcx, double qcy, double qcz,
				double qdx, double qdy, double qdz,
				int depth,
				List<FPoint> points) {

			// Base case 1: maximum depth reached.
			// Base case 2: curve is already flat enough.
			if (depth >= this.limit || isCubicFlat(qax, qay, qaz, qbx, qby, qbz, qcx, qcy, qcz, qdx, qdy, qdz)) {
				points.add(new FPoint(qcx, qcy, qcz));
			} else {
				// De Casteljau split at t = 0.5 — numerically stable, branchless.
				//
				//  m01  = midpoint(QA, QB)
				//  m12  = midpoint(QB, QC)
				//  m23  = midpoint(QC, QD)
				//  m012 = midpoint(m01, m12)
				//  m123 = midpoint(m12, m23)
				//  mid  = midpoint(m012, m123) - point on the curve at t = 0.5
				//
				//  Left  sub-curve: [QA,  m01,  m012, mid]
				//  Right sub-curve: [mid, m123, m23,  QD]

				final var m01x = .5 * (qax + qbx);
				final var m01y = .5 * (qay + qby);
				final var m01z = .5 * (qaz + qbz);
				final var m12x = .5 * (qbx + qcx);
				final var m12y = .5 * (qby + qcy);
				final var m12z = .5 * (qbz + qcz);
				final var m23x = .5 * (qcx + qdx);
				final var m23y = .5 * (qcy + qdy);
				final var m23z = .5 * (qcz + qdz);
				final var m012x = .5 * (m01x + m12x);
				final var m012y = .5 * (m01y + m12y);
				final var m012z = .5 * (m01z + m12z);
				final var m123x = .5 * (m12x + m23x);
				final var m123y = .5 * (m12y + m23y);
				final var m123z = .5 * (m12z + m23z);
				final var midx = .5 * (m012x + m123x);
				final var midy = .5 * (m012y + m123y);
				final var midz = .5 * (m012z + m123z);
				final var next = depth + 1;
				subdivideCubic(
						qax, qay, qaz,
						m01x, m01y, m01z,
						m012x, m012y, m012z,
						midx, midy, midz,
						next, points);
				subdivideCubic(
						midx, midy, midz,
						m123x, m123y, m123z,
						m23x, m23y, m23z,
						qdx, qdy, qdz,
						next, points);
			}
		}

		@SuppressWarnings("checkstyle:parameternumber")
		private boolean isCubicFlat(
				double qax, double qay, double qaz,
				double qbx, double qby, double qbz,
				double qcx, double qcy, double qcz,
				double qdx, double qdy, double qdz) {
			// Chord vector
			final var dx = qdx - qax;
			final var dy = qdy - qay;
			final var dz = qdz - qaz;
			final var squareChord = dx * dx + dy * dy + dz * dz;
			var dist = squaredDistanceToSegment(
					qbx, qby, qbz,
					qax, qay, qaz,
					dx, dy, dz,
					squareChord);
			if (dist <= this.squaredFlatness) {
				return true;
			}
			dist = squaredDistanceToSegment(
					qcx, qcy, qcz,
					qax, qay, qaz,
					dx, dy, dz, squareChord);
			return dist <= this.squaredFlatness;
		}

		@SuppressWarnings("checkstyle:parameternumber")
		private static double squaredDistanceToSegment(
				double px, double py, double pz,
				double ax, double ay, double az,
				double dx, double dy, double dz,
				double squareChord) {
			final var apx = px - ax;
			final var apy = py - ay;
			final var apz = pz - az;
			if (MathUtil.isEpsilonZero(squareChord)) {
				// Degenerate segment: distance to the single point a
				return apx * apx + apy * apy + apz * apz;
			}
			// Clamp projection parameter to [0, 1]
			var t = (apx * dx + apy * dy + apz * dz) / squareChord;
			if (t < 0.) {
				t = 0.;
			} else if (t > 1.) {
				t = 1.;
			}
			final var ex = apx - t * dx;
			final var ey = apy - t * dy;
			final var ez = apz - t * dz;
			return ex * ex + ey * ey + ez * ez;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.nextElement != null;
		}

		@Override
		public T next() {
			assert this.nextElement != null : AssertMessages.noSuchElement();
			final var element = this.nextElement;
			searchNext();
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
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
			return new FlatteningPathIterator<>(this.pathIterator.restartIterations(),
					this.limit, this.squaredFlatness);
		}

		/** Coordinates that corresponds to the approximation of the curve.
		 *
		 * @author $Author: sgalland$
		 * @author $Author: hjaffali$
		 * @author $Author: tpiotrow$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 * @since 13.0
		 */
		private record FPoint(double x, double y, double z) {
			//
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
	class PointCollection<P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>, Q extends Quaternion<? super P, ? super V, ? super Q>>
		implements Collection<P> {

		private final Path3afp<?, ?, P, V, Q, ?> path;

		/** Constructor.
		 * @param path the path to iterate on.
		 */
		public PointCollection(Path3afp<?, ?, P, V, Q, ?> path) {
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
			if (obj instanceof Point3D pt0) {
				return this.path.containsControlPoint(pt0);
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
			final var iterator = new PointIterator<>(this.path);
			for (var i = 0; i < array.length && iterator.hasNext(); ++i) {
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
			if (object instanceof Point3D p) {
				return this.path.remove(p.getX(), p.getY(), p.getZ());
			}
			return false;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> collection) {
			assert collection != null : AssertMessages.notNullParameter();
			for (final var obj : collection) {
				if (!(obj instanceof Point3D) || !this.path.containsControlPoint((Point3D<?, ?, ?>) obj)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends P> collection) {
			assert collection != null : AssertMessages.notNullParameter();
			var changed = false;
			for (final var pts : collection) {
				if (add(pts)) {
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public boolean removeAll(Collection<?> collection) {
			assert collection != null : AssertMessages.notNullParameter();
			var changed = false;
			for (final var obj : collection) {
				if (obj instanceof Point3D pts) {
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
	class PointIterator<P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>>
		implements Iterator<P> {

		private final Path3afp<?, ?, P, V, Q, ?> path;

		private int index;

		private P lastReplied;

		/** Constructor.
		 * @param path the path to iterate on.
		 */
		public PointIterator(Path3afp<?, ?, P, V, Q, ?> path) {
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
			final var p = this.lastReplied;
			this.lastReplied = null;
			if (p == null) {
				throw new NoSuchElementException();
			}
			this.path.remove(p.getX(), p.getY(), p.getZ());
		}

	}

}

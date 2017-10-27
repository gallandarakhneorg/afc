/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.gis.mapelement;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.d.DefaultSegment1d;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.d.Path2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.util.OutputParameter;

/**
 * Class the permits to display a polyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapPolyline extends MapComposedElement {

	private static final long serialVersionUID = -5742146638409712002L;

	private double length = -1;

	private boolean isWidePolyline;

	private SoftReference<Path2d> path;

	/** Create a new map element.
	 */
	public MapPolyline() {
		super(null, null);
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the source of the attributes for this map element.
	 */
	public MapPolyline(AttributeCollection attributeSource) {
		super(null, attributeSource);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @since 4.0
	 */
	public MapPolyline(UUID id) {
		super(id, null);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the source of the attributes for this map element.
	 * @since 4.0
	 */
	public MapPolyline(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	/** Set if this polyline must be drawn with a wide height.
	 *
	 * @param isWidePolyline is <code>true</code> if this polyline must have
	 *     a wide height, otherwise <code>false</code>
	 */
	public void setWidePolyline(boolean isWidePolyline) {
		this.isWidePolyline = isWidePolyline;
		fireShapeChanged();
		fireElementChanged();
	}

	/** Replies if this polyline must be drawn with a wide height.
	 *
	 * @return <code>true</code> if this polyline must have
	 *     a wide height, otherwise <code>false</code>
	 */
	@Pure
	public boolean isWidePolyline() {
		return this.isWidePolyline;
	}

	/**
	 * Replies the distance between this MapElement and
	 * point.
	 *
	 * @return the distance. Should be negative depending of the MapElement type.
	 */
	@Override
	@Pure
	public final double getDistance(Point2D<?, ?> point) {
		if (isWidePolyline()) {
			return distance(point, getWidth());
		}
		return distance(point, 0);
	}

	/** Replies the distance between this figure and the specified point.
	 *
	 * @param point is the x-coordinate of the point.
	 * @param width is the width of the polyline.
	 * @return the computed distance; the distance could be negative
	 *     if the point lies on the polyline shape (if the polyline has
	 *     wide width).
	 */
	@Pure
	public double distance(Point2D<?, ?> point, double width) {
		double mind = Double.POSITIVE_INFINITY;
		double dist;
		double w = width;
		w = Math.abs(w) / 2.;

		Point2d previousPoint;
		Point2d currentPoint;
		Iterator<Point2d> points;
		boolean treatFirstPoint;

		for (final PointGroup grp : groups()) {
			previousPoint = null;
			treatFirstPoint = false;
			points = grp.iterator();
			while (points.hasNext()) {
				currentPoint = points.next();
				if (previousPoint != null) {
					treatFirstPoint = true;
					dist = Segment2afp.calculatesDistanceSegmentPoint(
							previousPoint.getX(), previousPoint.getY(),
							currentPoint.getX(), currentPoint.getY(),
							point.getX(), point.getY())
							- w;
					if (dist < mind) {
						mind = dist;
					}
				}
				previousPoint = currentPoint;
			}
			if (previousPoint != null && !treatFirstPoint) {
				dist = previousPoint.getDistance(point);
				if (dist < mind) {
					mind = dist;
				}
			}
		}

		return mind;
	}

	/**
	 * Replies the distance between the nearest end of this MapElement and
	 * the point.
	 *
	 * @param point the point.
	 * @return the distance. Should be negative depending of the MapElement type.
	 */
	@Pure
	public final double distanceToEnd(Point2D<?, ?> point) {
		return distanceToEnd(point, 0);
	}

	/** Replies the distance between the nearest end of this MapElement and
	 * the point.
	 *
	 * @param point is the x-coordinate of the point.
	 * @param width is the width of the polyline.
	 * @return the computed distance
	 */
	@Pure
	public double distanceToEnd(Point2D<?, ?> point, double width) {
		final Point2d firstPoint = getPointAt(0);
		final Point2d lastPoint = getPointAt(-1);
		double d1 = firstPoint.getDistance(point);
		double d2 = lastPoint.getDistance(point);
		d1 -= width;
		if (d1 < 0) {
			d1 = 0;
		}
		d2 -= width;
		if (d2 < 0) {
			d2 = 0;
		}
		return d1 < d2 ? d1 : d2;
	}

	/**
	 * Replies the index of the nearest end of line according to the specified point.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return index of the nearest end point.
	 */
	@Pure
	public final int getNearestEndIndex(double x, double y) {
		return getNearestEndIndex(x, y, null);
	}

	/**
	 * Replies the index of the nearest end of line according to the specified point.
	 *
	 * @param x is the point coordinate from which the distance must be computed
	 * @param y is the point coordinate from which the distance must be computed
	 * @param distance is the distance value that will be set by this function (if the parameter is not <code>null</code>).
	 * @return index of the nearest end point.
	 */
	@Pure
	public final int getNearestEndIndex(double x, double y, OutputParameter<Double> distance) {
		final int count = getPointCount();
		final Point2d firstPoint = getPointAt(0);
		final Point2d lastPoint = getPointAt(count - 1);
		final double d1 = Point2D.getDistancePointPoint(firstPoint.getX(), firstPoint.getY(), x, y);
		final double d2 = Point2D.getDistancePointPoint(lastPoint.getX(), lastPoint.getY(), x, y);
		if (d1 <= d2) {
			if (distance != null) {
				distance.set(d1);
			}
			return 0;
		}
		if (distance != null) {
			distance.set(d2);
		}
		return count - 1;
	}

	/**
	 * Return the nearest point 1.5D from a 2D position.
	 *
	 * @param pos is the testing position.
	 * @return the nearest 1.5D position on the road network.
	 */
	@Pure
	public Point1d getNearestPosition(Point2D<?, ?> pos) {
		Point2d pp = null;
		final Vector2d v = new Vector2d();
		double currentPosition = 0.;
		double bestPosition = Double.NaN;
		double bestDistance = Double.POSITIVE_INFINITY;
		for (final Point2d p : points()) {
			if (pp != null) {
				double position = Segment2afp.findsProjectedPointPointLine(
						p.getX(), p.getY(),
						pos.getX(), pos.getY(),
						pp.getX(), pp.getY());
				if (position < 0.) {
					position = 0.;
				}
				if (position > 1.) {
					position = 1.;
				}
				v.sub(p, pp);
				final double t = v.getLength();
				v.scale(position);
				final double dist = Point2D.getDistanceSquaredPointPoint(
						pos.getX(), pos.getY(),
						pp.getX() + v.getX(), pp.getY() + v.getY());
				if (dist < bestDistance) {
					bestDistance = dist;
					bestPosition = currentPosition + v.getLength();
				}
				currentPosition += t;
			}
			pp = p;
		}
		if (Double.isNaN(bestPosition)) {
			return null;
		}
		return new Point1d(toSegment1D(), bestPosition, 0.);
	}

	/** Clear the current bounding box to force the computation of it at
	 * the next call to {@link #getBoundingBox()}.
	 */
	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		this.length = -1;
		this.path = null;
	}

	/**
	 * Replies the length of this polyline.
	 * The length is the distance between first point and the
	 * last point of the polyline.
	 *
	 * <p>The returned value is the sum of the lengths of the polyline' groups that
	 * compose this polyline.
	 *
	 * @return the length of the segment in meters.
	 */
	@Pure
	public double getLength() {
		if (this.length < 0) {
			double segmentLength = 0;
			for (final PointGroup group : groups()) {
				Point2d previousPts = null;
				for (final Point2d pts : group.points()) {
					if (previousPts != null) {
						segmentLength += previousPts.getDistance(pts);
					}
					previousPts = pts;
				}
			}
			this.length = segmentLength;
		}
		return this.length;
	}

	/**
	 * Replies the subsegment which is corresponding to the given position.
	 *
	 * <p>A subsegment is a pair if connected points in the polyline.
	 *
	 * @param distance is position on the polyline (in {@code 0} to {@code getLength()}).
	 * @return the point pair, never <code>null</code>.
	 */
	@Pure
	public Segment1D<?, ?> getSubSegmentForDistance(double distance) {
		final double rd = distance < 0. ? 0. : distance;
		double onGoingDistance = 0.;
		for (final PointGroup group : groups()) {
			Point2d previousPts = null;
			for (final Point2d pts : group.points()) {
				if (previousPts != null) {
					onGoingDistance += previousPts.getDistance(pts);
					if (rd <= onGoingDistance) {
						// The desired distance is on the current point pair
						return new DefaultSegment1d(previousPts, pts);
					}
				}
				previousPts = pts;
			}
		}
		throw new IllegalArgumentException("distance must be lower or equal than getLength(); distance=" //$NON-NLS-1$
				+ Double.toString(distance)
				+ "; length=" //$NON-NLS-1$
				+ Double.toString(getLength()));
	}

	/** Returns the poly-element width in the geo-located referencial.
	 * The width is the distance between the border lines of the polyline.
	 *
	 * <p>The implementation provided by the MapPolyline class
	 * always replies 1 meter. The function {@link #getWidth()} must be
	 * overrided to provide other way to compute the width the
	 * polyelements.
	 *
	 * @return the width of the poly-element in meters.
	 */
	@SuppressWarnings("static-method")
	@Pure
	public double getWidth() {
		return 1.;
	}

	/**
	 * Replies if this element has an intersection
	 * with the specified rectangle.
	 *
	 * @return <code>true</code> if this MapElement is intersecting the specified area,
	 *     otherwise <code>false</code>
	 */
	@Override
	@Pure
	public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle) {
		if (boundsIntersects(rectangle)) {

			if (rectangle.intersects(getBoundingBox())) {
				return true;
			}

			if (this.isWidePolyline) {
				final Rectangle2afp<?, ?, ?, ?, ?, ?> box = rectangle.toBoundingBox();
				final double rminX = box.getMinX();
				final double rminY = box.getMinY();
				final double rmaxX = box.getMaxX();
				final double rmaxY = box.getMaxY();

				final double width = getWidth();

				boolean firstPoint;
				double px = 0;
				double py = 0;

				for (final PointGroup grp : groups()) {
					firstPoint = true;
					for (final Point2d pts : grp) {
						final double x = pts.getX();
						final double y = pts.getY();

						if (firstPoint) {
							firstPoint = false;
						} else if (MathUtil.min(
								Segment2afp.calculatesDistanceSquaredSegmentSegment(rminX, rmaxY, rmaxX, rmaxY, px, py, x, y),
								Segment2afp.calculatesDistanceSquaredSegmentSegment(rminX, rminY, rminX, rmaxY, px, py, x, y),
								Segment2afp.calculatesDistanceSquaredSegmentSegment(rmaxX, rminY, rmaxX, rmaxY, px, py, x, y))
								<= width * width) {
							return true;
						}

						px = x;
						py = y;
					}
				}
			} else {
				return toPath2D().intersects(rectangle);
			}
		}
		return false;
	}

	@Override
	public Shape2d<?> getShape() {
		return toPath2D();
	}

	/**
	 * Replies the Path2D that corresponds to this polyline.
	 *
	 * @return the 2D path.
	 */
	@Pure
	public final Path2d toPath2D() {
		Path2d path = null;
		if (this.path != null) {
			path = this.path.get();
		}
		if (path == null) {
			// this is the path to draw
			path = new Path2d();
			toPath2D(path);
			this.path = new SoftReference<>(path);
		}
		return path;
	}

	/**
	 * Replies the Path2D that corresponds to this polyline.
	 *
	 * @param path the 2D path to update.
	 */
	@Pure
	public final void toPath2D(Path2d path) {
		// loop on parts and build the path to draw
		boolean firstPoint;
		for (final PointGroup grp : groups()) {
			firstPoint = true;
			for (final Point2d pts : grp) {
				final double x = pts.getX();
				final double y = pts.getY();
				if (firstPoint) {
					path.moveTo(x, y);
					firstPoint = false;
				} else {
					path.lineTo(x, y);
				}
			}
		}
	}

	/**
	 * Replies the Path2D that corresponds to this polyline.
	 * If <var>startPosition</var> is greater to zero,
	 * the replied path will be clipped to ignore the part of
	 * the polyline before the given value.
	 * If <var>endPosition</var> is lower to the length of the polyline,
	 * the replied path will be clipped to ignore the part of
	 * the polyline after the given value.
	 *
	 * @param startPosition is the curviline position from which the polyline is drawn.
	 * @param endPosition is the curviline position to which the polyline is drawn.
	 * @return the clipped 2D path.
	 * @since 4.0
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	@Pure
	public final Path2d toPath2D(double startPosition, double endPosition) {
		final Path2d path = new Path2d();
		toPath2D(path, startPosition, endPosition);
		return path;
	}

	/**
	 * Replies the Path2D that corresponds to this polyline.
	 * If <var>startPosition</var> is greater to zero,
	 * the replied path will be clipped to ignore the part of
	 * the polyline before the given value.
	 * If <var>endPosition</var> is lower to the length of the polyline,
	 * the replied path will be clipped to ignore the part of
	 * the polyline after the given value.
	 *
	 * @param path the path to fill out.
	 * @param startPosition is the curviline position from which the polyline is drawn.
	 * @param endPosition is the curviline position to which the polyline is drawn.
	 * @since 4.0
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	@Pure
	public final void toPath2D(Path2d path, double startPosition, double endPosition) {
		final double length = getLength();
		// For performance purpose
		if ((Double.isNaN(startPosition) || startPosition < 0f)
				&&
				(Double.isNaN(endPosition) || endPosition >= length)) {
			toPath2D(path);
			return;
		}

		final double p1;
		final double p2;
		if (Double.isNaN(startPosition) || startPosition <= 0f) {
			p1 = 0.;
		} else {
			p1 = startPosition;
		}
		if (Double.isNaN(endPosition) || endPosition >= length) {
			p2 = length;
		} else {
			p2 = endPosition;
		}
		if (p2 <= p1) {
			return;
		}

		boolean firstDrawn;
		double curvilinePosition = 0.;
		double previousCurvilinePosition = 0.;

		for (final PointGroup grp : groups()) {
			firstDrawn = true;
			Point2d previous = null;
			for (final Point2d pts : grp) {
				if (p2 <= previousCurvilinePosition) {
					return;
				}

				if (previous != null) {
					curvilinePosition += previous.getDistance(pts);
				}

				if (curvilinePosition >= p1) {
					final Point2d f;
					double curvilineDiff;
					if (previous == null || previousCurvilinePosition >= p1) {
						f = pts;
					} else {
						curvilineDiff = curvilinePosition - previousCurvilinePosition;
						if (curvilineDiff <= 0.) {
							f = pts;
						} else {
							f = new Point2d();
							Segment2afp.interpolates(previous.getX(), previous.getY(), pts.getX(), pts.getY(),
									(p1 - previousCurvilinePosition) / curvilineDiff, f);
						}
					}
					final Point2d l;
					if (p2 < curvilinePosition) {
						assert previous != null && p2 >= previousCurvilinePosition && p2 <= curvilinePosition;
						curvilineDiff = curvilinePosition - previousCurvilinePosition;
						if (curvilineDiff <= 0.) {
							l = null;
						} else {
							l = new Point2d();
							Segment2afp.interpolates(previous.getX(), previous.getY(), pts.getX(), pts.getY(),
									(p2 - previousCurvilinePosition) / curvilineDiff, l);
						}
					} else {
						l = null;
					}

					if (l == null) {
						if (firstDrawn) {
							firstDrawn = false;
							path.moveTo(f.getX(), f.getY());
						} else {
							path.lineTo(f.getX(), f.getY());
						}
						if (f != pts) {
							path.lineTo(pts.getX(), pts.getY());
						}
					} else {
						if (firstDrawn) {
							firstDrawn = false;
							path.moveTo(l.getX(), l.getY());
						} else {
							path.lineTo(l.getX(), l.getY());
						}
					}
				}

				previous = pts;
				previousCurvilinePosition = curvilinePosition;
			}
		}
	}

	/** Compute the bounds of this element.
	 * This function does not update the internal
	 * attribute replied by {@link #getBoundingBox()}
	 */
	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		final Rectangle2d bounds = super.calcBounds().toBoundingBox();
		if (bounds != null && this.isWidePolyline) {
			final double w = getWidth();
			final double mx = bounds.getMinX();
			final double my = bounds.getMinY();
			final double xx = bounds.getMaxX();
			final double xy = bounds.getMaxY();
			bounds.add(mx - w, my - w);
			bounds.add(xx + w, xy + w);
		}
		return bounds;
	}

	/** Replies the Segment1D representation of this object.
	 *
	 * @return the Segment1D, never <code>null</code>.
	 * @since 4.0
	 */
	@Pure
	protected Segment1D<?, ?> toSegment1D() {
		return new SegmentRepresentation();
	}

	/** Replies the geo-location of the point described by the specified distance.
	 * The desired distance is <code>0</code> for the starting point and {@link #getLength()}
	 * for the ending point.
	 *
	 * @param desired_distance is the distance for which the geo location must be computed.
	 * @param shifting is the shifting distance.
	 * @param system is the 2D coordinate system used to determine the sign of the shifting value.
	 * @param geoLocation is the point to set with geo-localized coordinates.
	 * @param tangent is the vector which will be set by the coordinates of the tangent at the replied point.
	 *     If <code>null</code> the tangent will not be computed.
	 */
	protected final void computeGeoLocationForDistance(double desired_distance, double shifting,
			CoordinateSystem2D system, Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
		assert geoLocation != null;
		double desiredDistance = desired_distance;
		for (final PointGroup group : groups()) {
			Point2d prevPoint = null;

			for (final Point2d thepoint : group.points()) {
				if (prevPoint != null) {
					// Compute the length betwen the current point and the previous point
					final double distance = prevPoint.getDistance(thepoint);
					if (desiredDistance < distance && distance != 0) {
						// The desired distance is on this part's segment
						double vx = thepoint.getX() - prevPoint.getX();
						double vy = thepoint.getY() - prevPoint.getY();
						final double norm = Math.hypot(vx, vy);
						if (norm != 0) {

							// Compute the vector and the new point
							vx /= norm;
							double px = vx * desiredDistance + prevPoint.getX();

							vy /= norm;
							double py = vy * desiredDistance + prevPoint.getY();

							if (tangent != null) {
								tangent.set(vx, vy);
							}

							// Shift the point on the left or on the right depending on
							// the sign of the shifting value
							if (shifting != 0 && system != null) {
								final Vector2d perpend = new Vector2d(vx, vy);
								perpend.makeOrthogonal(system);
								perpend.scale(shifting);
								px += perpend.getX();
								py += perpend.getY();
							}

							geoLocation.set(px, py);
							return;
						}
						geoLocation.set(thepoint);
						return;
					}
					// pass to the next couple of points
					desiredDistance -= distance;
				}
				prevPoint = thepoint;
			}
		}
		// The end of the segment was reached
		final Point2d antepenulvianPoint = getPointAt(getPointCount() - 2);
		final Point2d lastPoint = getPointAt(getPointCount() - 1).clone();
		if (tangent != null) {
			// The tangent is colinear to the last segment
			tangent.set(
					lastPoint.getX() - antepenulvianPoint.getX(),
					lastPoint.getY() - antepenulvianPoint.getY());
		}
		if (shifting != 0. && system != null) {
			final Vector2d perpend = new Vector2d(lastPoint.getX() - antepenulvianPoint.getX(),
					lastPoint.getY() - antepenulvianPoint.getY());
			perpend.makeOrthogonal(system);
			perpend.scale(shifting);
			lastPoint.set(lastPoint.getX() + perpend.getX(), lastPoint.getY() + perpend.getY());
		}
		geoLocation.set(lastPoint);
	}

	/** Segment representation.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@SuppressWarnings("rawtypes")
	private class SegmentRepresentation implements Segment1D {

		SegmentRepresentation() {
			//
		}

		@Override
		@Pure
		public double getLength() {
			return MapPolyline.this.getLength();
		}

		@Override
		@Pure
		public Point2d getFirstPoint() {
			return MapPolyline.this.getPointAt(0);
		}

		@Override
		@Pure
		public Point2d getLastPoint() {
			return MapPolyline.this.getPointAt(MapPolyline.this.getPointCount() - 1);
		}

		@Override
		@Pure
		public Vector2d getTangentAt(double positionOnSegment) {
			final Vector2d tangent = new Vector2d();
			computeGeoLocationForDistance(positionOnSegment, 0.,
					CoordinateSystem2D.getDefaultCoordinateSystem(),
					null,
					tangent);
			return tangent;
		}

		@Override
		@Pure
		public boolean isFirstPointConnectedTo(Segment1D otherSegment) {
			return false;
		}

		@Override
		@Pure
		public boolean isLastPointConnectedTo(Segment1D otherSegment) {
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void projectsOnPlane(double positionOnSegment, Point2D position, Vector2D tangent,
				CoordinateSystem2D system) {
			final Point2d gl = new Point2d();
			computeGeoLocationForDistance(positionOnSegment, 0., system, gl, tangent);
			if (position != null) {
				position.set(gl);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void projectsOnPlane(double positionOnSegment, double shiftDistance, Point2D position, Vector2D tangent,
				CoordinateSystem2D system) {
			final Point2d gl = new Point2d();
			computeGeoLocationForDistance(positionOnSegment, shiftDistance, system, gl, tangent);
			if (position != null) {
				position.set(gl);
			}
		}

	} // class SegmentRepresentation

}

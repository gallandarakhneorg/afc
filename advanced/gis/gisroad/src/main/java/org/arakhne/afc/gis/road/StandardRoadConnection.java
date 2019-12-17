/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.gis.road;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.references.WeakArrayList;
import org.arakhne.afc.vmutil.locale.Locale;

/** This class represents the connection point inside a road network.
 *
 * <p>A RoadConnection is a point that permits to link several road.
 * Two RoadConnections are assumed to be the same if they have
 * the same position.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
class StandardRoadConnection implements RoadConnection {

	private WeakArrayList<IClockwiseIterator> listeningIterators;

	private final List<Connection> connectedSegments = new ArrayList<>();

	private SoftReference<Point2d> location;

	private SoftReference<GeoLocationPoint> geolocation;

	/** Create a new road connection.
	 *
	 * <p>A RoadConnection must be created thru a {@link RoadNetwork}.
	 */
	StandardRoadConnection() {
		//
	}

	@Override
	@Pure
	public String toString() {
		return getUUID().toString()
				+ "@" + getGeoLocation().toString() //$NON-NLS-1$
				+ "\nSegments:\n" + this.connectedSegments.toString(); //$NON-NLS-1$
	}

	@Override
	@Pure
	public final RoadConnection getWrappedRoadConnection() {
		return this;
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof RoadConnection) {
			return compareTo((RoadConnection) obj) == 0;
		}
		if (obj instanceof Point2D<?, ?>) {
			return compareTo((Point2D<?, ?>) obj) == 0;
		}
		return super.equals(obj);
	}

	@Override
	@Pure
	public int compareTo(GraphPoint<RoadConnection, RoadSegment> other) {
		if (other == null) {
			return Integer.MAX_VALUE;
		}
		if (other instanceof RoadConnection) {
			final RoadConnection conn = ((RoadConnection) other).getWrappedRoadConnection();
			assert conn != null;
			if (conn == this) {
				return 0;
			}
			final Point2d p1 = getPoint();
			final Point2d p2 = conn.getPoint();
			assert p1 != null && p2 != null;
			final double sqDist = p1.getDistanceSquared(p2);
			if (MathUtil.isEpsilonZero(sqDist)) {
				return 0;
			}
			if (p1.getX() < p2.getX()) {
				return -1;
			}
			if (p1.getX() > p2.getX()) {
				return 1;
			}
			if (p1.getY() < p2.getY()) {
				return -1;
			}
			return 1;
		}
		return hashCode() - other.hashCode();
	}

	@Override
	@Pure
	public int compareTo(Point2D<?, ?> pts) {
		if (pts == null) {
			return Integer.MAX_VALUE;
		}
		final Point2d p1 = getPoint();
		assert p1 != null;
		final double sqDist = p1.getDistanceSquared(pts);
		if (MathUtil.isEpsilonZero(sqDist)) {
			return 0;
		}
		if (p1.getX() < pts.getX()) {
			return -1;
		}
		if (p1.getX() > pts.getX()) {
			return 1;
		}
		if (p1.getY() < pts.getY()) {
			return -1;
		}
		return 1;
	}

	@Override
	@Pure
	public UUID getUUID() {
		final GeoLocationPoint pt = getGeoLocation();
		if (pt != null) {
			return pt.toUUID();
		}
		return null;
	}

	@Pure
	@Override
	public boolean isNearPoint(Point2D<?, ?> point) {
		return GeoLocationUtil.epsilonEqualsDistance(getPoint(), point);
	}

	@Override
	@Pure
	public int hashCode() {
		final Point2d p = getPoint();
		assert p != null;
		return p.hashCode();
	}

	@Override
	@Pure
	public Point2d getPoint() {
		Point2d pts = this.location == null ? null : this.location.get();

		if (pts == null && !this.connectedSegments.isEmpty()) {
			final RoadPolyline segment = this.connectedSegments.get(0).getRoadPolyline();
			if (segment.getBeginPoint(StandardRoadConnection.class) == this) {
				pts = segment.getPointAt(0);
			} else if (segment.getEndPoint(StandardRoadConnection.class) == this) {
				pts = segment.getPointAt(-1);
			}

			this.location = pts == null ? null : new SoftReference<>(pts);
		}

		return pts;
	}

	@Override
	@Pure
	public GeoLocationPoint getGeoLocation() {
		GeoLocationPoint pt = this.geolocation == null ? null : this.geolocation.get();
		if (pt == null) {
			final Point2d position = getPoint();
			if (position != null) {
				pt = new GeoLocationPoint(position.getX(), position.getY());
				this.geolocation = new SoftReference<>(pt);
			}
		}
		return pt;
	}

	/** Set the temporary buffer of the position of the road connection.
	 *
	 * @param position a position.
	 * @since 4.0
	 */
	void setPosition(Point2D<?, ?> position) {
		this.location = position == null ? null : new SoftReference<>(Point2d.convert(position));
	}

	/** Compute the angle of the given road segment according
	 * to the vector (1,0).
	 *
	 * @return the angle between the vector (<code>RoadConnection.this</code>,<var>segment</var>)
	 *     and (1,0).
	 */
	private double computeAngle(RoadPolyline segment, boolean fromStartPoint) {
		double angle = Double.NaN;
		Point2d p1 = null;
		Point2d p2 = null;
		if (segment.getPointCount() > 1) {
			if (fromStartPoint) {
				final RoadConnection startPoint = segment.getBeginPoint(StandardRoadConnection.class);
				if (startPoint == this) {
					p1 = startPoint.getPoint();
					p2 = segment.getPointAt(1);
					// Because the Y axis is directly to the bottom of the screen
					// it is necessary to invert it to reply an counterclockwise angle
					angle = MathUtil.clampCyclic(
							Vector2D.signedAngle(
									1, 0, p2.getX() - p1.getX(), p1.getY() - p2.getY()),
							0, MathConstants.TWO_PI);
				} else {
					throw new IllegalArgumentException(
							Locale.getString("ERROR_ALREADY_CONNECTED_START", //$NON-NLS-1$
									segment,
									p1,
									p2,
									startPoint));
				}
			} else {
				final RoadConnection endPoint = segment.getEndPoint(StandardRoadConnection.class);
				if (endPoint == this) {
					p1 = endPoint.getPoint();
					p2 = segment.getAntepenulvianPoint();
					// Because the Y axis is directly to the bottom of the screen
					// it is necessary to invert it to reply an counterclockwise angle
					angle = MathUtil.clampCyclic(
							Vector2D.signedAngle(
									1, 0, p2.getX() - p1.getX(), p1.getY() - p2.getY()),
							0, MathConstants.TWO_PI);
				} else {
					throw new IllegalArgumentException(
							Locale.getString("ERROR_ALREADY_CONNECTED_END", //$NON-NLS-1$
									segment,
									p1,
									p2,
									endPoint));
				}
			}

			if (Double.isNaN(angle) || Double.isInfinite(angle)) {
				throw new IllegalArgumentException(
						Locale.getString("ERROR_SAME_POINTS", //$NON-NLS-1$
								segment, p1, p2));
			}
		} else {
			throw new IllegalArgumentException(
					Locale.getString("ERROR_NOT_ENOUGH_POINTS", //$NON-NLS-1$
							segment));
		}

		return angle;
	}

	/**
	 * Add a segment to this connection point.
	 *
	 * <p>The segments are ordered according to there
	 * geo-localization along a trigonometric cicle.
	 * The first segment has the nearest angle to the
	 * vector (1,0), and the following segments are
	 * ordered according to the positive value of there
	 * angles to this unity vector (counterclockwise order).
	 *
	 * @param segment is the segment to add.
	 * @param attachToStartPoint indicates if the segment must be attached by
	 *     its start point (if value is <code>true</code>) or by its end point
	 *     (if value is <code>false</code>).
	 */
	void addConnectedSegment(RoadPolyline segment, boolean attachToStartPoint) {
		if (segment == null) {
			return;
		}
		if (this.connectedSegments.isEmpty()) {
			this.connectedSegments.add(new Connection(segment, attachToStartPoint));
		} else {
			// Compute the angle to the unit vector for the new segment
			final double newSegmentAngle = computeAngle(segment, attachToStartPoint);

			// Search for the insertion index
			final int insertionIndex = searchInsertionIndex(newSegmentAngle, 0, this.connectedSegments.size() - 1);
			// Insert
			this.connectedSegments.add(insertionIndex, new Connection(segment, attachToStartPoint));
		}
		fireIteratorUpdate();
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity"})
	private int searchInsertionIndex(double baseAngle, int startIdx, int endIdx) {
		final int eIdx = Math.min(endIdx, this.connectedSegments.size() - 1);

		int idxSegment = startIdx;
		while (idxSegment <= eIdx) {
			final RoadPolyline existingSegment = this.connectedSegments.get(idxSegment).getRoadPolyline();
			if (existingSegment != null) {
				final RoadConnection startPt = existingSegment.getBeginPoint(StandardRoadConnection.class);
				final RoadConnection endPt = existingSegment.getEndPoint(StandardRoadConnection.class);

				double angle;
				if (startPt == this && endPt != this) {
					angle = computeAngle(existingSegment, true);
				} else if (startPt != this && endPt == this) {
					angle = computeAngle(existingSegment, false);
				} else if (startPt == this && endPt == this) {
					angle = computeAngle(existingSegment, true);
					double anglePrime = computeAngle(existingSegment, false);

					if (angle > anglePrime) {
						final double t = anglePrime;
						anglePrime = angle;
						angle = t;
					}

					if (baseAngle <= angle) {
						return idxSegment;
					}

					if (baseAngle >= anglePrime) {
						// Jump to the next connection with the same segment
						++idxSegment;
						while (idxSegment <= eIdx
								&& this.connectedSegments.get(idxSegment).getRoadPolyline() != existingSegment) {
							++idxSegment;
						}
						//Continue the search from the segment
						continue;
					}

					// The angle is between the two connections of the segment.
					// Search the index of the next segment connection
					int otherConnectionIdx = idxSegment + 1;
					while (otherConnectionIdx <= eIdx
							&& this.connectedSegments.get(otherConnectionIdx).getRoadPolyline() != existingSegment) {
						++otherConnectionIdx;
					}
					return searchInsertionIndex(baseAngle, idxSegment + 1, otherConnectionIdx - 1);
				} else {
					// This case should never occurs
					++idxSegment;
					continue;
				}

				if (baseAngle <= angle) {
					return idxSegment;
				}
			}
			++idxSegment;
		}

		return eIdx + 1;
	}

	@SuppressWarnings("unlikely-arg-type")
	private int indexOf(RoadSegment segment, boolean attachToStartPoint) {
		if (segment != null) {
			for (int i = 0; i < this.connectedSegments.size(); ++i) {
				final Connection info = this.connectedSegments.get(i);
				if (info != null && info.equals(segment) && info.connectedWithStartPoint == attachToStartPoint) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Remove a connection with a segment.
	 *
	 * @param segment is the segment to remove.
	 * @param attachToStartPoint indicates if the segment is attached by
	 *     its start point (if value is <code>true</code>) or by its end point
	 * @return the index of the removed segment, or {@code -1} if none was found
	 */
	int removeConnectedSegment(RoadSegment segment, boolean attachToStartPoint) {
		if (segment == null) {
			return -1;
		}
		final int idx = indexOf(segment, attachToStartPoint);
		if (idx != -1) {
			this.connectedSegments.remove(idx);
			fireIteratorUpdate();
		}
		return idx;
	}

	@Override
	@Pure
	public int getConnectedSegmentCount() {
		return this.connectedSegments.size();
	}

	@Override
	@Pure
	public RoadSegment getConnectedSegment(int index) throws ArrayIndexOutOfBoundsException {
		return this.connectedSegments.get(index).getRoadPolyline();
	}

	/** Replies all the information about a connection to a segment.
	 *
	 * @param index the index.
	 * @return all the information about a connection to a segment.
	 * @throws ArrayIndexOutOfBoundsException in case of error.
	 */
	@Pure
	protected Connection getConnectionInfo(int index) throws ArrayIndexOutOfBoundsException {
		return this.connectedSegments.get(index);
	}

	private Iterator<Connection> toCounterclockwiseConnectionIterator(RoadSegment startSegment,
			Boolean startConnection, RoadSegment endSegment, Boolean endConnection,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		if (system.isLeftHanded()) {
			return new CounterclockwiseIterator(startSegment, startConnection, endSegment, endConnection, boundType);
		}
		return new ClockwiseIterator(startSegment, startConnection, endSegment, endConnection, boundType);
	}

	private Iterator<Connection> toClockwiseConnectionIterator(RoadSegment startSegment,
			Boolean startConnection, RoadSegment endSegment, Boolean endConnection,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		if (system.isLeftHanded()) {
			return new ClockwiseIterator(startSegment, startConnection, endSegment, endConnection, boundType);
		}
		return new CounterclockwiseIterator(startSegment, startConnection, endSegment, endConnection, boundType);
	}

	@Override
	@Pure
	public Iterable<RoadSegment> getConnectedSegments() {
		return new ConnectionListWrapper(this.connectedSegments);
	}

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnections() {
		return this.connectedSegments;
	}

	@Override
	@Pure
	public Iterable<RoadSegment> getConnectedSegmentsStartingFrom(RoadSegment startingSegment) {
		return new ConnectionBoundedListWrapper(startingSegment, false);
	}

	@Override
	public Iterable<RoadSegment> getConnectedSegmentsStartingFromInReverseOrder(RoadSegment startingPoint) {
		return new ConnectionBoundedListWrapper(startingPoint, true);
	}

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFrom(
			RoadSegment startingSegment) {
		return new ConnectionBoundedListIterable(startingSegment, false);
	}

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFromInReverseOrder(
			RoadSegment startingSegment) {
		return new ConnectionBoundedListIterable(startingSegment, true);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	@Pure
	public boolean isConnectedSegment(RoadSegment segment) {
		if (segment == null) {
			return false;
		}
		for (final Connection info : this.connectedSegments) {
			if (info.equals(segment)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Pure
	public boolean isFinalConnectionPoint() {
		return this.connectedSegments.size() <= 1;
	}

	@Override
	@Pure
	public boolean isReallyCulDeSac() {
		return this.connectedSegments.size() <= 1;
	}

	@Override
	@Pure
	public boolean isEmpty() {
		return this.connectedSegments.isEmpty();
	}

	@Override
	@Pure
	public RoadSegment getOtherSideSegment(RoadSegment ref_segment) {
		if (this.connectedSegments.size() != 2) {
			return null;
		}
		final RoadSegment s0 = getConnectedSegment(0);
		final RoadSegment s1 = getConnectedSegment(1);
		if (ref_segment == s0) {
			return s1;
		}
		if (ref_segment == s1) {
			return s0;
		}
		return null;
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startConnection, RoadSegment endSegment, Boolean endConnection, ClockwiseBoundType boundType) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, startConnection,
						endSegment, endConnection, boundType,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startConnection, RoadSegment endSegment, Boolean endConnection,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, startConnection,
						endSegment, endConnection,
						boundType, system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, startSegmentConnectedByItsStart,
						endSegment, endSegmentConnectedByItsStart,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, ClockwiseBoundType boundType) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						boundType,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						null, null,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						null, null,
						boundType,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						DEFAULT_CLOCKWHISE_TYPE,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, startSegmentConnectedByItsStart,
						endSegment, endSegmentConnectedByItsStart,
						DEFAULT_CLOCKWHISE_TYPE,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						boundType,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						null, null,
						DEFAULT_CLOCKWHISE_TYPE,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toCounterclockwiseConnectionIterator(
						startSegment, null,
						null, null,
						boundType,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment, Boolean startConnection,
			RoadSegment endSegment, Boolean endConnection,
			ClockwiseBoundType boundType) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, startConnection,
						endSegment, endConnection, boundType,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, startSegmentConnectedByItsStart,
						endSegment, endSegmentConnectedByItsStart,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, ClockwiseBoundType boundType) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						boundType,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						null, null,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						null, null,
						boundType,
						CoordinateSystem2D.getDefaultCoordinateSystem()));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, Boolean startConnection,
			RoadSegment endSegment, Boolean endConnection, ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, startConnection,
						endSegment, endConnection,
						boundType,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						DEFAULT_CLOCKWHISE_TYPE,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, startSegmentConnectedByItsStart,
						endSegment, endSegmentConnectedByItsStart,
						DEFAULT_CLOCKWHISE_TYPE,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						endSegment, null,
						boundType,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						null, null,
						DEFAULT_CLOCKWHISE_TYPE,
						system));
	}

	@Override
	@Pure
	public final Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ConnectionListWrappingIterator(
				toClockwiseConnectionIterator(
						startSegment, null,
						null, null,
						boundType,
						system));
	}

	/** Add a listening iterator.
	 *
	 * @param iterator the iterator.
	 */
	protected void addListeningIterator(IClockwiseIterator iterator) {
		if (this.listeningIterators == null) {
			this.listeningIterators = new WeakArrayList<>();
		}
		this.listeningIterators.add(iterator);
	}

	/** Remove a listening iterator.
	 *
	 * @param iterator the iterator.
	 */
	protected void removeListeningIterator(IClockwiseIterator iterator) {
		if (this.listeningIterators != null) {
			this.listeningIterators.remove(iterator);
			if (this.listeningIterators.isEmpty()) {
				this.listeningIterators = null;
			}
		}
	}

	/** Notify the iterators about changes.
	 */
	protected void fireIteratorUpdate() {
		if (this.listeningIterators != null) {
			for (final IClockwiseIterator iterator : this.listeningIterators) {
				if (iterator != null) {
					iterator.dataStructureUpdated();
				}
			}
		}
	}

	/**
	 * This class represents the information on a connected segment.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	protected class Connection implements GraphPointConnection<RoadConnection, RoadSegment> {

		/**
		 * Indicates if this connection is done with a start point.
		 */
		@SuppressWarnings("checkstyle:visibilitymodifier")
		public final boolean connectedWithStartPoint;

		private final WeakReference<RoadPolyline> segment;

		/** Constructor.
		 * @param segment the connected segment
		 * @param connectedWithStartPoint indicates if the segment is connected by its start point, or not.
		 */
		public Connection(RoadPolyline segment, boolean connectedWithStartPoint) {
			this.segment = new WeakReference<>(segment);
			this.connectedWithStartPoint = connectedWithStartPoint;
		}

		/** Replies the linked road polyline.
		 * @return the connected segment.
		 */
		@Pure
		public RoadPolyline getRoadPolyline() {
			return this.segment.get();
		}

		@Override
		@Pure
		public boolean equals(Object obj) {
			if (obj instanceof RoadSegment) {
				return this.segment.get().equals(obj);
			}
			if (obj instanceof Connection) {
				return this.segment.get().equals(((Connection) obj).getRoadPolyline());
			}
			return super.equals(obj);
		}

		@Override
		@Pure
		public int hashCode() {
			final RoadSegment s = this.segment.get();
			if (s == null) {
				return StandardRoadConnection.this.hashCode();
			}
			return s.hashCode();
		}

		@Override
		@Pure
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			if (this.connectedWithStartPoint) {
				buffer.append('+');
			}
			buffer.append(this.segment.get().toString());
			if (!this.connectedWithStartPoint) {
				buffer.append('+');
			}
			return buffer.toString();
		}

		@Override
		@Pure
		public RoadConnection getGraphPoint() {
			return StandardRoadConnection.this;
		}

		@Override
		@Pure
		public RoadSegment getGraphSegment() {
			return this.segment.get();
		}

		@Override
		@Pure
		public boolean isSegmentStartConnected() {
			return this.connectedWithStartPoint;
		}

	} /* Connection */

	/**
	 * Iterator that replies the segments connected to the associated point in
	 * a counterclockwise order.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected interface IClockwiseIterator extends Iterator<Connection> {

		/**
		 * Invoked when the data strucure was updated.
		 */
		void dataStructureUpdated();
	}

	/**
	 * Iterator that replies the segments connected to the associated point in
	 * a counterclockwise order according to the coordinate system {@link CoordinateSystem2D#XY_LEFT_HAND}.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class CounterclockwiseIterator implements IClockwiseIterator {

		private final ClockwiseBoundType boundType;

		private int finalIndex;

		private boolean dataStructureUpdated;

		private int index = -1;

		private Connection nextSegment;

		private Connection removableSegment;

		/** Constructor.
		 * @param startSegment start segment.
		 * @param startConnection start connection.
		 * @param endSegment end segment.
		 * @param endConnection end connection.
		 * @param boundType type of bounds.
		 */
		@SuppressWarnings({"checkstyle:methodlength", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
		CounterclockwiseIterator(RoadSegment startSegment, Boolean startConnection, RoadSegment endSegment,
				Boolean endConnection, ClockwiseBoundType boundType) {
			this.boundType = boundType;
			this.dataStructureUpdated = false;
			StandardRoadConnection.this.addListeningIterator(this);

			// Seach the start and end indexes.
			int sIdx = -1;
			int eIdx = -1;

			Connection connectionInfo;
			RoadSegment sgmt;

			for (int i = 0; i < StandardRoadConnection.this.getConnectedSegmentCount() && (sIdx == -1 || eIdx == -1); ++i) {
				if (this.dataStructureUpdated) {
					StandardRoadConnection.this.removeListeningIterator(this);
					throw new ConcurrentModificationException();
				}

				connectionInfo = StandardRoadConnection.this.getConnectionInfo(i);
				sgmt = connectionInfo.getRoadPolyline();

				if (sIdx == -1 && sgmt.equals(startSegment)
						&& (startConnection == null
						|| startConnection == connectionInfo.connectedWithStartPoint)) {
					sIdx = i;
				}

				if (eIdx == -1 && sgmt.equals(endSegment)
						&& (endConnection == null || endConnection != connectionInfo.connectedWithStartPoint)) {
					eIdx = i;
				}
			}

			if (sIdx != -1 && (eIdx != -1 || endSegment == null)) {
				// If the final segment is null, loop until the end of the list
				if (endSegment == null && eIdx == -1) {
					eIdx = sIdx == 0
							? StandardRoadConnection.this.getConnectedSegmentCount() - 1
									: (sIdx - 1) % StandardRoadConnection.this.getConnectedSegmentCount();
				}
				// Remove the bounds if necessary
				if (sIdx == eIdx) {
					if (boundType.includeStart() || boundType.includeEnd()) {
						this.finalIndex = sIdx;
						this.nextSegment = StandardRoadConnection.this.getConnectionInfo(sIdx);
						this.index = -1;
					} else {
						this.finalIndex = -1;
					}
				} else {
					if (!boundType.includeStart()) {
						++sIdx;
						if (sIdx >= StandardRoadConnection.this.getConnectedSegmentCount()) {
							sIdx = 0;
						}
					}

					this.finalIndex = eIdx;
					this.nextSegment = sIdx == eIdx && !boundType.includeEnd() ? null
							: StandardRoadConnection.this.getConnectionInfo(sIdx);
					if (sIdx == eIdx) {
						this.index = -1;
					} else {
						this.index = (sIdx + 1) % StandardRoadConnection.this.getConnectedSegmentCount();
					}
				}
			} else {
				this.finalIndex = -1;
			}
		}

		@Override
		public void dataStructureUpdated() {
			this.dataStructureUpdated = true;
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (this.dataStructureUpdated) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new ConcurrentModificationException();
			}

			final boolean hasNext = this.nextSegment != null;

			if (!hasNext) {
				StandardRoadConnection.this.removeListeningIterator(this);
				this.removableSegment = null;
				this.index = -1;
			}

			return hasNext;
		}

		@Override
		public Connection next() {
			if (this.dataStructureUpdated) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new ConcurrentModificationException();
			}

			final Connection toReturn = this.nextSegment;
			if (toReturn == null) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new NoSuchElementException();
			}
			this.removableSegment = this.nextSegment;

			// Search for the next segment
			final int count = StandardRoadConnection.this.getConnectedSegmentCount();
			if (this.index == -1
					|| (this.index == (this.finalIndex + 1) % count && this.boundType.includeEnd())
					|| (this.index == this.finalIndex && !this.boundType.includeEnd())) {
				this.index = -1;
				this.nextSegment = null;
			} else {
				this.nextSegment = StandardRoadConnection.this.getConnectionInfo(this.index);
				this.index = (this.index + 1) % count;
			}

			return toReturn;
		}

		@Override
		public void remove() {
			if (this.dataStructureUpdated) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new ConcurrentModificationException();
			}

			if (this.removableSegment == null) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new NoSuchElementException();
			}

			if (this.removableSegment.getRoadPolyline() == null) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new NoSuchElementException();
			}

			final int idx = StandardRoadConnection.this.removeConnectedSegment(
					this.removableSegment.getRoadPolyline(), this.removableSegment.connectedWithStartPoint);

			if (this.index >= idx) {
				--this.index;
			}
			if (this.finalIndex >= idx) {
				--this.finalIndex;
			}
			this.dataStructureUpdated = false;
			this.removableSegment = null;
		}

	} /* class CounterclockwiseIterator */

	/**
	 * Iterator that replies the segments connected to the associated point in
	 * a clockwise order according to the coordinate system {@link CoordinateSystem2D#XY_LEFT_HAND}.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ClockwiseIterator implements IClockwiseIterator {

		private final ClockwiseBoundType boundType;

		private int finalIndex;

		private boolean dataStructureUpdated;

		private int index = -1;

		private Connection nextSegment;

		private Connection removableSegment;

		/** Constructor.
		 *
		 * @param startSegment the start segment.
		 * @param startConnection the start connection.
		 * @param endSegment the end segment.
		 * @param endConnection the end connection.
		 * @param boundType the type of bounds.
		 */
		@SuppressWarnings({"checkstyle:methodlength", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
		ClockwiseIterator(RoadSegment startSegment, Boolean startConnection, RoadSegment endSegment,
				Boolean endConnection, ClockwiseBoundType boundType) {
			this.boundType = boundType;
			this.dataStructureUpdated = false;
			StandardRoadConnection.this.addListeningIterator(this);

			// Seach the start and end indexes.
			int sIdx = -1;
			int eIdx = -1;
			Connection connectionInfo;
			RoadSegment sgmt;

			for (int i = 0; i < StandardRoadConnection.this.getConnectedSegmentCount() && (sIdx == -1 || eIdx == -1); ++i) {
				if (this.dataStructureUpdated) {
					StandardRoadConnection.this.removeListeningIterator(this);
					throw new ConcurrentModificationException();
				}

				connectionInfo = StandardRoadConnection.this.getConnectionInfo(i);
				sgmt = connectionInfo.getRoadPolyline();

				if (sIdx == -1 && sgmt.equals(startSegment)
						&& (startConnection == null
						|| startConnection == connectionInfo.connectedWithStartPoint)) {
					sIdx = i;
				}

				if (eIdx == -1 && sgmt.equals(endSegment)
						&& (endConnection == null
						|| endConnection != connectionInfo.connectedWithStartPoint)) {
					eIdx = i;
				}
			}

			if (sIdx != -1 && (eIdx != -1 || endSegment == null)) {
				// If the final segment is null, loop until the end of the list
				if (endSegment == null && eIdx == -1) {
					eIdx = (sIdx + 1) % StandardRoadConnection.this.getConnectedSegmentCount();
				}
				// Remove the bounds if necessary
				if (sIdx == eIdx) {
					if (boundType.includeStart() || boundType.includeEnd()) {
						this.finalIndex = sIdx;
						this.nextSegment = StandardRoadConnection.this.getConnectionInfo(sIdx);
						this.index = -1;
					} else {
						this.finalIndex = -1;
					}
				} else {
					if (!boundType.includeStart()) {
						--sIdx;
						if (sIdx < 0) {
							sIdx = StandardRoadConnection.this.getConnectedSegmentCount() - 1;
						}
					}

					this.finalIndex = eIdx;
					this.nextSegment = sIdx == eIdx && !boundType.includeEnd() ? null
							: StandardRoadConnection.this.getConnectionInfo(sIdx);
					if (sIdx == eIdx) {
						this.index = -1;
					} else {
						--sIdx;
						if (sIdx < 0) {
							sIdx = StandardRoadConnection.this.getConnectedSegmentCount() - 1;
						}
						this.index = sIdx;
					}
				}
			} else {
				this.finalIndex = -1;
			}
		}

		@Override
		public void dataStructureUpdated() {
			this.dataStructureUpdated = true;
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (this.dataStructureUpdated) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new ConcurrentModificationException();
			}

			final boolean hasNext = this.nextSegment != null;

			if (!hasNext) {
				StandardRoadConnection.this.removeListeningIterator(this);
				this.removableSegment = null;
				this.index = -1;
			}

			return hasNext;
		}

		@Override
		public Connection next() {
			if (this.dataStructureUpdated) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new ConcurrentModificationException();
			}

			final Connection toReturn = this.nextSegment;
			if (toReturn == null) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new NoSuchElementException();
			}
			this.removableSegment = this.nextSegment;

			// Search for the next segment
			final int lastIdx = StandardRoadConnection.this.getConnectedSegmentCount() - 1;
			int ee = this.finalIndex - 1;
			if (ee < 0) {
				ee = lastIdx;
			}
			if (this.index == -1 || (this.index == ee && this.boundType.includeEnd())
					|| (this.index == this.finalIndex && !this.boundType.includeEnd())) {
				this.index = -1;
				this.nextSegment = null;
			} else {
				this.nextSegment = StandardRoadConnection.this.getConnectionInfo(this.index);
				--this.index;
				if (this.index < 0) {
					this.index = lastIdx;
				}
			}

			return toReturn;
		}

		@Override
		public void remove() {
			if (this.dataStructureUpdated) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new ConcurrentModificationException();
			}

			if (this.removableSegment == null) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new NoSuchElementException();
			}

			if (this.removableSegment.getRoadPolyline() == null) {
				StandardRoadConnection.this.removeListeningIterator(this);
				throw new NoSuchElementException();
			}

			final int idx = StandardRoadConnection.this.removeConnectedSegment(
					this.removableSegment.getRoadPolyline(), this.removableSegment.connectedWithStartPoint);

			if (this.index >= idx) {
				++this.index;
			}
			if (this.finalIndex >= idx) {
				++this.finalIndex;
			}
			this.dataStructureUpdated = false;
			this.removableSegment = null;
		}

	} /* class ClockwiseIterator */

	/**
	 * This class represents a wrapping list to the connection's list.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class ConnectionListWrappingIterator implements Iterator<RoadSegment> {

		private final Iterator<Connection> iterator;

		/** Constructor.
		 * @param iter the original iterator.
		 */
		ConnectionListWrappingIterator(Iterator<Connection> iter) {
			this.iterator = iter;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public RoadSegment next() {
			final Connection con = this.iterator.next();
			if (con == null) {
				throw new NoSuchElementException();
			}
			return con.getRoadPolyline();
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

	}

	/**
	 * This class represents a wrapping list to the connection's list.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class ConnectionListWrapper implements Iterable<RoadSegment> {

		private final Iterable<Connection> list;

		/** Constructor.
		 * @param list the wrapped list.
		 */
		ConnectionListWrapper(Iterable<Connection> list) {
			this.list = list;
		}

		@Override
		@Pure
		public Iterator<RoadSegment> iterator() {
			return new ConnectionListWrappingIterator(this.list.iterator());
		}

	}

	/**
	 * This class represents a wrapping list to the connection's list.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ConnectionBoundedListWrapper implements Iterable<RoadSegment> {

		private final RoadSegment startingSegment;

		private final boolean reverse;

		/** Constructor.
		 *
		 * @param startingSegment the wrapped element.
		 * @param reverse indicates if the list is reverse or not.
		 */
		ConnectionBoundedListWrapper(RoadSegment startingSegment, boolean reverse) {
			this.startingSegment = startingSegment;
			this.reverse = reverse;
		}

		@Override
		@Pure
		public Iterator<RoadSegment> iterator() {
			final Iterator<Connection> source;
			if (this.reverse) {
				source = toClockwiseConnectionIterator(
						this.startingSegment, null, null, null,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem());
			} else {
				source = toCounterclockwiseConnectionIterator(
						this.startingSegment, null, null, null,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem());
			}
			return new ConnectionListWrappingIterator(source);
		}

	}

	/**
	 * This class represents a wrapping list to the connection's list.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ConnectionBoundedListIterable implements Iterable<Connection> {

		private final RoadSegment startingSegment;

		private final boolean reverse;

		/** Constructor.
		 *
		 * @param startingSegment the wrapped element.
		 * @param reverse indicates if the connections should be replied in reverse order, or not.
		 */
		ConnectionBoundedListIterable(RoadSegment startingSegment, boolean reverse) {
			this.startingSegment = startingSegment;
			this.reverse = reverse;
		}

		@Override
		@Pure
		public Iterator<Connection> iterator() {
			if (this.reverse) {
				return toClockwiseConnectionIterator(
						this.startingSegment, null, null, null,
						DEFAULT_CLOCKWHISE_TYPE,
						CoordinateSystem2D.getDefaultCoordinateSystem());
			}
			return toCounterclockwiseConnectionIterator(
					this.startingSegment, null, null, null,
					DEFAULT_CLOCKWHISE_TYPE,
					CoordinateSystem2D.getDefaultCoordinateSystem());
		}

	}

}

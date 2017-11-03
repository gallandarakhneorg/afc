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

package org.arakhne.afc.gis.road;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.graph.GraphPoint;

/**
 * This class represents the connection point inside a road network and
 * which is knowing the road segment from which it was obtained.
 *
 * <p>This type of connection is usefull to deal with
 * an order when moving from a segment to another. Indeed,
 * this connection knows the segment to arrive to the connection.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
final class RoadConnectionWithArrivalSegment implements RoadConnection, Iterable<RoadSegment> {

	private final SoftReference<StandardRoadConnection> connection;

	private final SoftReference<RoadSegment> arrivalSegment;

	private final boolean startConnection;

	/** Create a new road connection.
	 *
	 * <p>A RoadConnection must be created thru a {@link StandardRoadNetwork}.
	 *
	 * @param connection is the wrapped connection
	 * @param arrivalSegment is the segment that permits to arrive to the road connection
	 * @param startConnection is <code>true</code> is the enter point is the start.
	 */
	RoadConnectionWithArrivalSegment(StandardRoadConnection connection, RoadSegment arrivalSegment, boolean startConnection) {
		this.connection = new SoftReference<>(connection);
		this.arrivalSegment = new SoftReference<>(arrivalSegment);
		this.startConnection = startConnection;
	}

	@Override
	@Pure
	public RoadConnection getWrappedRoadConnection() {
		final RoadConnection c = this.connection.get();
		if (c == null) {
			return this;
		}
		return c.getWrappedRoadConnection();
	}

	@Override
	@Pure
	public String toString() {
		return this.connection.get().toString();
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		return this.connection.get().equals(obj);
	}

	@Override
	@Pure
	public int hashCode() {
		return this.connection.get().hashCode();
	}

	/** Replies the segment that is the arrival segment at this road
	 * connection.
	 *
	 * @return the segment
	 */
	@Pure
	public RoadSegment getArrivalSegment() {
		return this.arrivalSegment.get();
	}

	/** Replies if the arrival segment is connected to this road connection
	 * by its start point.
	 *
	 * @return <code>true</code> if the arrival segment is connected by its
	 *     start point, <code>false</code> if it is connected by its end segment.
	 */
	@Pure
	public boolean isArrivalSegmentConnectedByStartPoint() {
		return this.startConnection;
	}

	@Override
	@Pure
	public UUID getUUID() {
		return this.connection.get().getUUID();
	}

	@Override
	@Pure
	public boolean isNearPoint(Point2D<?, ?> point) {
		return this.connection.get().isNearPoint(point);
	}

	@Override
	@Pure
	public Point2d getPoint() {
		return this.connection.get().getPoint();
	}

	@Override
	@Pure
	public GeoLocationPoint getGeoLocation() {
		return this.connection.get().getGeoLocation();
	}

	@Override
	@Pure
	public int getConnectedSegmentCount() {
        return this.connection.get().getConnectedSegmentCount();
    }

	@Override
	@Pure
	public RoadSegment getConnectedSegment(int index) throws ArrayIndexOutOfBoundsException {
        return this.connection.get().getConnectedSegment(index);
    }

	@Override
	@Pure
	public Iterable<RoadSegment> getConnectedSegments() {
    	return this;
    }

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnections() {
		return this.connection.get().getConnections();
	}

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFrom(
			RoadSegment startingPoint) {
		return this.connection.get().getConnectionsStartingFrom(startingPoint);
	}

	@Override
	@Pure
	public Iterable<RoadSegment> getConnectedSegmentsStartingFrom(RoadSegment segment) {
    	return new ConnectionBoundedListIterable(segment);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> iterator() {
		return toClockwiseIterator(this.getArrivalSegment());
	}

	@Override
	@Pure
	public boolean isConnectedSegment(RoadSegment segment) {
    	return this.connection.get().isConnectedSegment(segment);
	}

	@Override
	@Pure
	public boolean isFinalConnectionPoint() {
    	return this.connection.get().isFinalConnectionPoint();
	}

	@Override
	@Pure
	public boolean isReallyCulDeSac() {
    	return this.connection.get().isReallyCulDeSac();
	}

	@Override
	@Pure
	public boolean isEmpty() {
    	return this.connection.get().isEmpty();
	}

	@Override
	@Pure
	public RoadSegment getOtherSideSegment(RoadSegment ref_segment) {
    	return this.connection.get().getOtherSideSegment(ref_segment);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment) {
		return toCounterclockwiseIterator(startSegment, null, endSegment, null, DEFAULT_CLOCKWHISE_TYPE);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
    		Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
    		Boolean endSegmentConnectedByItsStart) {
		return toCounterclockwiseIterator(startSegment, startSegmentConnectedByItsStart, endSegment,
				endSegmentConnectedByItsStart, DEFAULT_CLOCKWHISE_TYPE);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType) {
		return toCounterclockwiseIterator(startSegment, null, endSegment, null, boundType);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
    		RoadSegment endSegment, Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType) {
		final RoadSegment arrivalSegment = this.arrivalSegment.get();
		assert arrivalSegment != null;
		Boolean theStartConnection = null;
		Boolean theEndConnection = null;
		if (startSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(startSegment)) {
			theStartConnection = this.startConnection;
		}
		if (endSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(endSegment)) {
			theEndConnection = this.startConnection;
		}
    	return this.connection.get().toCounterclockwiseIterator(startSegment, theStartConnection,
    			endSegment, theEndConnection, boundType);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment) {
    	return toCounterclockwiseIterator(startSegment, (RoadSegment) null);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType) {
    	return toCounterclockwiseIterator(startSegment, (RoadSegment) null, boundType);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			CoordinateSystem2D system) {
		return toCounterclockwiseIterator(startSegment, null, endSegment, null, DEFAULT_CLOCKWHISE_TYPE, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
    		Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
    		Boolean endSegmentConnectedByItsStart, CoordinateSystem2D system) {
		return toCounterclockwiseIterator(startSegment, startSegmentConnectedByItsStart, endSegment,
				endSegmentConnectedByItsStart, DEFAULT_CLOCKWHISE_TYPE, system);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return toCounterclockwiseIterator(startSegment, null, endSegment, null, boundType, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
    		Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
    		Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType, CoordinateSystem2D system) {
		final RoadSegment arrivalSegment = this.arrivalSegment.get();
		assert arrivalSegment != null;
		Boolean theStartConnection = null;
		Boolean theEndConnection = null;
		if (startSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(startSegment)) {
			theStartConnection = this.startConnection;
		}
		if (endSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(endSegment)) {
			theEndConnection = this.startConnection;
		}
    	return this.connection.get().toCounterclockwiseIterator(startSegment, theStartConnection, endSegment,
    			theEndConnection, boundType, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, CoordinateSystem2D system) {
    	return toCounterclockwiseIterator(startSegment, (RoadSegment) null, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType,
    		CoordinateSystem2D system) {
    	return toCounterclockwiseIterator(startSegment, (RoadSegment) null, boundType, system);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment) {
		return toClockwiseIterator(startSegment, null, endSegment, null, DEFAULT_CLOCKWHISE_TYPE);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
    		RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
		return toClockwiseIterator(startSegment, startSegmentConnectedByItsStart, endSegment,
				endSegmentConnectedByItsStart, DEFAULT_CLOCKWHISE_TYPE);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType) {
		return toClockwiseIterator(startSegment, null, endSegment, null, boundType);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
    		Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
    		Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType) {
		final RoadSegment arrivalSegment = this.arrivalSegment.get();
		assert arrivalSegment != null;
		Boolean theStartConnection = null;
		Boolean theEndConnection = null;
		if (startSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(startSegment)) {
			theStartConnection = this.startConnection;
		}
		if (endSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(endSegment)) {
			theEndConnection = this.startConnection;
		}
    	return this.connection.get().toClockwiseIterator(startSegment, theStartConnection,
    			endSegment, theEndConnection, boundType);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment) {
    	return toClockwiseIterator(startSegment, (RoadSegment) null);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType) {
    	return toClockwiseIterator(startSegment, (RoadSegment) null, boundType);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			CoordinateSystem2D system) {
		return toClockwiseIterator(startSegment, null, endSegment, null, DEFAULT_CLOCKWHISE_TYPE, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
    		Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
    		Boolean endSegmentConnectedByItsStart, CoordinateSystem2D system) {
		return toClockwiseIterator(startSegment, startSegmentConnectedByItsStart, endSegment,
				endSegmentConnectedByItsStart, DEFAULT_CLOCKWHISE_TYPE, system);
    }

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return toClockwiseIterator(startSegment, null, endSegment, null, boundType, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
    		RoadSegment endSegment, Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType,
    		CoordinateSystem2D system) {
		final RoadSegment arrivalSegment = this.arrivalSegment.get();
		assert arrivalSegment != null;
		Boolean theStartConnection = null;
		Boolean theEndConnection = null;
		if (startSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(startSegment)) {
			theStartConnection = this.startConnection;
		}
		if (endSegmentConnectedByItsStart == null
				&& arrivalSegment.equals(endSegment)) {
			theEndConnection = this.startConnection;
		}
    	return this.connection.get().toClockwiseIterator(startSegment, theStartConnection, endSegment,
    			theEndConnection, boundType, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, CoordinateSystem2D system) {
    	return toClockwiseIterator(startSegment, (RoadSegment) null, system);
    }

	@Override
	@Pure
    public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType,
    		CoordinateSystem2D system) {
    	return toClockwiseIterator(startSegment, (RoadSegment) null, boundType, system);
    }

	@Override
	@Pure
	public int compareTo(GraphPoint<RoadConnection, RoadSegment> other) {
		return this.connection.get().compareTo(other);
	}

	@Override
	@Pure
    public int compareTo(Point2D<?, ?> pts) {
		return this.connection.get().compareTo(pts);
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
    private class ConnectionBoundedListIterable implements Iterable<RoadSegment> {

    	private final RoadSegment startingSegment;

    	/** Constructor.
    	 *
    	 * @param startingSegment the start segment.
    	 */
    	ConnectionBoundedListIterable(RoadSegment startingSegment) {
    		this.startingSegment = startingSegment;
    	}

		@Override
		@Pure
		public Iterator<RoadSegment> iterator() {
			return toCounterclockwiseIterator(this.startingSegment);
		}

    }

}

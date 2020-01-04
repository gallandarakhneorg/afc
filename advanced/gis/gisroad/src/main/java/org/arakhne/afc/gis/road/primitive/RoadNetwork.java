/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.gis.road.primitive;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.BoundedGISElement;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * This interface describes a road network.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface RoadNetwork extends BoundedGISElement, RoadSegmentContainer {

	//-------------------------------------------------
	// Graph interface
	//-------------------------------------------------

	@Override
	@Pure
	boolean isLeftSidedTrafficDirection();

	@Override
	@Pure
	boolean isRightSidedTrafficDirection();

	//-------------------------------------------------
	// Getter functions
	//-------------------------------------------------

	/**
	 * Replies a collection of segments inside this road network.
	 *
	 * @return a collection of segments inside this road network.
	 */
	@Pure
	Collection<? extends RoadSegment> getRoadSegments();

	@Override
	@Pure
	boolean contains(RoadSegment segment);

	@Override
	@Pure
	RoadSegment getRoadSegment(GeoId geoId);

	@Override
	@Pure
	RoadSegment getRoadSegment(GeoLocation location);

	/**
	 * Return the nearest point from a position.
	 *
	 * @param pos is the testing position.
	 * @return the nearest road network point to the given point.
	 */
	@Pure
	RoadConnection getNearestConnection(Point2D<?, ?> pos);

	/**
	 * Return the the road connections inside the given bounds.
	 *
	 * @param bounds are the bounds to explore.
	 * @return the road connections in the given bounds.
	 * @since 4.0
	 */
	@Pure
	Collection<RoadConnection> getConnections(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds);

	/**
	 * Return the the road connections inside the given bounds.
	 *
	 * @param bounds are the bounds to explore.
	 * @return the road connections in the given bounds.
	 * @since 4.0
	 */
	@Pure
	Collection<RoadConnection> getConnections(Rectangle2d bounds);

	/**
	 * Return the nearest segment from a position.
	 *
	 * @param pos is the testing position.
	 * @return the nearest road network segment to the given point.
	 */
	@Pure
	RoadSegment getNearestSegment(Point2D<?, ?> pos);

	/**
	 * Return the nearest segment from a position; and its
	 * distance to the point.
	 *
	 * @param pos is the testing position.
	 * @return the nearest road network segment and its distance
	 *     to the given point;
	 *     or <code>null</code> if none.
	 * @since 4.0
	 */
	@Pure
	Pair<? extends RoadSegment, Double> getNearestSegmentData(Point2D<?, ?> pos);

	/**
	 * Return the nearest point 1.5D from a 2D position.
	 *
	 * @param pos is the testing position.
	 * @return the nearest 1.5D position on the road network.
	 */
	@Pure
	Point1d getNearestPosition(Point2D<?, ?> pos);

	/**
	 * Return the nearest point 1.5D on the road borders from a 2D position.
	 *
	 * @param pos is the testing position.
	 * @return the nearest 1.5D position on the road network.
	 * @since 16.0
	 */
	@Pure
	Point1d getNearestPositionOnRoadBorder(Point2D<?, ?> pos);

	//-------------------------------------------------
	// Updating functions
	//-------------------------------------------------

	/** Add a road segment inside the road network.
	 *
	 * @param segment is the road segment to insert
	 */
	void addRoadSegment(RoadSegment segment);

	/** Remove a segment from this network.
	 *
	 * @param segment is the segment to remove
	 * @return <code>true</code> if the segment was successfully removed, otherwhise
	 * <code>false</code>
	 */
	boolean removeRoadSegment(RoadSegment segment);

	/** Clear this road network by removing all the road segments.
	 *
	 * @return <code>true</code> if the road network changed due to
	 *     this call; otherwise <code>false</code>.
	 * @since 4.0
	 */
	boolean clear();

	/**
	 * Merge the given connections to obtain only one connection.
	 *
	 * @param connections are the connections to merge.
	 * @return the result of the merging action.
	 * @since 4.0
	 */
	RoadConnection mergeRoadConnections(RoadConnection... connections);

	/**
	 * Merge the given connections to obtain only one connection.
	 *
	 * @param connections are the connections to merge.
	 * @return the result of the merging action.
	 * @since 4.0
	 */
	RoadConnection mergeRoadConnections(Collection<? extends RoadConnection> connections);

	/**
	 * Connection the start point of the given road segment
	 * to the given road connection or to a new connection.
	 *
	 * @param connection is the connection to connect to, or
	 * <code>null</code> if a new connection should be created.
	 * @param segment is the road to connected to.
	 * @param position is the position where the first point of the
	 *     segment should be located, or <code>null</code> if this position
	 *     may be unchanged.
	 * @return the connection to which the segment was attached.
	 * @since 4.0
	 */
	RoadConnection connectSegmentStartPoint(RoadConnection connection, RoadSegment segment, Point2D<?, ?> position);

	/**
	 * Connection the end point of the given road segment
	 * to the given road connection or to a new connection.
	 *
	 * @param connection is the connection to connect to, or
	 * <code>null</code> if a new connection should be created.
	 * @param segment is the road to connected to.
	 * @param position is the position where the end point of the
	 *     segment should be located, or <code>null</code> if this position
	 *     may be unchanged.
	 * @return the connection to which the segment was attached.
	 * @since 4.0
	 */
	RoadConnection connectSegmentEndPoint(RoadConnection connection, RoadSegment segment, Point2D<?, ?> position);

	/** Add a listener.
	 *
	 * @param listener the listener.
	 */
	void addRoadNetworkListener(RoadNetworkListener listener);

	/** Remove a listener.
	 *
	 * @param listener the listener.
	 */
	void removeRoadNetworkListener(RoadNetworkListener listener);

	//-------------------------------------------------
	// Iterator functions
	//-------------------------------------------------

	@Override
	@Pure
    Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds);

	@Override
	@Pure
    Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget);

	@Override
	@Pure
    Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds);

	@Override
	@Pure
    Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget);

	/** Replies the bounding rectangles of the internal data-structure elements.
	 *
	 * @return the bounding boxes in the data-structure.
	 */
	@Pure
	Iterator<Rectangle2afp<?, ?, ?, ?, ?, ?>> boundsIterator();

}

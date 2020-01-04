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

import java.util.Iterator;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystemConstants;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.graph.GraphPoint;

/** This class represents the connection point inside a road network.
 *
 * <p>A RoadConnection is a point that permits to link several roads.
 * Two RoadConnections are assumed to be the same if they have
 * the same position.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface RoadConnection extends GraphPoint<RoadConnection, RoadSegment> {

	/**
	 * When traversing a road connection, the iterator replies both bounds
	 * of the connected segment list.
	 */
	ClockwiseBoundType DEFAULT_CLOCKWHISE_TYPE = ClockwiseBoundType.INCLUDE_BOTH;

	/**
	 * Compares this object with the specified point for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified point.
	 *
	 * @param pts is the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object
	 *     is less than, equal to, or greater than the specified point.
	 */
	@Pure
	int compareTo(Point2D<?, ?> pts);

	/** Replies an unique identifier for element.
	 *
	 * <p>A Unique IDentifier (UID) must be unique for all the object instances.
	 *
	 * @return the identifier
	 */
	@Pure
	UUID getUUID();

	/** Replies if the specified point is near this connection point.
	 *
	 * @param point is the point to test
	 * @return <code>true</code> if the point is near this connection, otherwise false.
	 */
	@Pure
	boolean isNearPoint(Point2D<?, ?> point);

	/** Replies the coordinates of this road connection.
	 *
	 * @return the coordinates of this road connection.
	 */
	@Pure
	Point2d getPoint();

	/** Replies the geographic coordinates of this road connection.
	 *
	 * @return the geographic coordinates of this road connection.
	 */
	@Pure
	GeoLocationPoint getGeoLocation();

	@Override
	@Pure
	int getConnectedSegmentCount();

	/**
	 * Replies the connected segment at the specified index.
	 *
	 * @param index is the index of the segment to remove.
	 * @return the connected segment at the given index
	 * @throws ArrayIndexOutOfBoundsException in case of error.
	 */
	@Pure
	RoadSegment getConnectedSegment(int index) throws ArrayIndexOutOfBoundsException;

	@Override
	@Pure
	Iterable<RoadSegment> getConnectedSegments();

	@Override
	@Pure
	Iterable<RoadSegment> getConnectedSegmentsStartingFrom(RoadSegment startingSegment);

	@Override
	@Pure
	boolean isConnectedSegment(RoadSegment segment);

	/** Replies if this point is a final connection point ie,
	 * a point connected to only one segment.
	 *
	 * <p>The difference between {@link #isReallyCulDeSac()}
	 * and {@link #isFinalConnectionPoint()} is on the
	 * treatement of sub-road-network elements.
	 * {@link #isFinalConnectionPoint()} takes into account only
	 * the <i>current</i> (sub)-road network.
	 * {@link #isReallyCulDeSac()} takes into account only the top-most
	 * road network element. See the following table for details:
	 * <table summary="">
	 * <thead>
	 * <tr><th>Inside RoadNetwork</th>
	 *     <th># Connected Segments at Top-Level</th>
	 *     <th>Inside SubRoadNetwork</th>
	 *     <th># Connected Segments at Lower-Level</th>
	 *     <th>isFinalConnectionPoint</th>
	 *     <th>isReallyCulDeSac</th></tr>
	 * </thead>
	 * <tbody>
	 * <tr>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td>false</td>
	 *     <td>n/a</td>
	 *     <td><code>true</code></td>
	 *     <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>n</td>
	 *     <td>false</td>
	 *     <td>n/a</td>
	 *     <td><code>false</code></td>
	 *     <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td><code>true</code></td>
	 *     <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>n</td>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td><code>true</code></td>
	 *     <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>n</td>
	 *     <td>true</td>
	 *     <td>m&lt;=n</td>
	 *     <td><code>false</code></td>
	 *     <td><code>false</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @return <code>true</code> if zero or one segment was connected
	 *     to this point, otherwise <code>false</code>
	 * @see #isReallyCulDeSac()
	 */
	@Override
	@Pure
	boolean isFinalConnectionPoint();

	/** Replies if this point is a cul-de-sac.
	 *
	 * <p>The difference between {@link #isReallyCulDeSac()}
	 * and {@link #isFinalConnectionPoint()} is on the
	 * treatement of sub-road-network elements.
	 * {@link #isFinalConnectionPoint()} takes into account only
	 * the <i>current</i> (sub)-road network.
	 * {@link #isReallyCulDeSac()} takes into account only the top-most
	 * road network element. See the following table for details:
	 * <table summary="">
	 * <thead>
	 * <tr><th>Inside RoadNetwork</th>
	 *     <th># Connected Segments at Top-Level</th>
	 *     <th>Inside SubRoadNetwork</th>
	 *     <th># Connected Segments at Lower-Level</th>
	 *     <th>isFinalConnectionPoint</th>
	 *     <th>isReallyCulDeSac</th></tr>
	 * </thead>
	 * <tbody>
	 * <tr>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td>false</td>
	 *     <td>n/a</td>
	 *     <td><code>true</code></td>
	 *     <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>n</td>
	 *     <td>false</td>
	 *     <td>n/a</td>
	 *     <td><code>false</code></td>
	 *     <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td><code>true</code></td>
	 *     <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>n</td>
	 *     <td>true</td>
	 *     <td>1</td>
	 *     <td><code>true</code></td>
	 *     <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 *     <td>true</td>
	 *     <td>n</td>
	 *     <td>true</td>
	 *     <td>m&lt;=n</td>
	 *     <td><code>false</code></td>
	 *     <td><code>false</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @return <code>true</code> if zero or one segment was connected
	 *     to this point, otherwise <code>false</code>
	 * @see #isFinalConnectionPoint()
	 */
	@Pure
	boolean isReallyCulDeSac();

	/** Replies if this connection has no connected segment.
	 *
	 * @return <code>true</code> if this object has no connected segment,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isEmpty();

	/** Replies the other segment also connected to this point.
	 * If more than 2 segments were connected to this point,
	 * this function replies <code>null</code>.
	 *
	 * @param refSegment the segment.
	 * @return the first segment if <var>ref_segment</var> was the second one.
	 *     the second segment if <var>ref_segment</var> was the first one.
	 *     otherwise <code>null</code>.
	 */
	@Pure
	RoadSegment getOtherSideSegment(RoadSegment refSegment);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate (inclusive).
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate (inclusive).
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment, Boolean endSegmentConnectedByItsStart);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate.
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * Assuming that the segment candidates should be replied only once time
	 * if they are all included, the <var>startSegment</var> will never be
	 * replied as the last segment.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * last or antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @param system is the 2D coordinate system used to project a counterclockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 *
	 * @param startSegment is the first segment to iterate (inclusive).
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate (inclusive).
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param system is the 2D coordinate system used to project a counterclockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @param system is the 2D coordinate system used to project a counterclockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate.
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @param system is the 2D coordinate system used to project a counterclockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * Assuming that the segment candidates should be replied only once time
	 * if they are all included, the <var>startSegment</var> will never be
	 * replied as the last segment.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param system is the 2D coordinate system used to project a counterclockwise circle.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a counterclockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * last or antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @param system is the 2D coordinate system used to project a counterclockwise circle.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toCounterclockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType,
			CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate (inclusive).
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate (inclusive).
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment, ClockwiseBoundType boundType);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate.
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * Assuming that the segment candidates should be replied only once time
	 * if they are all included, the <var>startSegment</var> will never be
	 * replied as the last segment.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * last or antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * <p>This function assumes a {@link CoordinateSystemConstants#GIS_2D}
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @param system is the 2D coordinate system used to project a clockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 *
	 * @param startSegment is the first segment to iterate (inclusive).
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate (inclusive).
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param system is the 2D coordinate system used to project a clockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the first occurrence. of specified start segment to the first
	 * occurrence. of the specified end segment.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param endSegment is the last segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @param system is the 2D coordinate system used to project a clockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified start segment to the specified end segment.
	 *
	 * <p>This function assumes that <var>startSegmentConnectedByItsStart</var>
	 * and <var>endSegmentConnectedByItsStart</var> parameters indicates how
	 * the segments should be connected. These values are useful to
	 * indicates how to iterate when the segment is connected to this
	 * RoadConnexion by its two ends.
	 * The semantics of the these values are:
	 * <ul>
	 * <li><code>true</code>, the segment will be replied by the iterator
	 * when its start point will be found in the list;</li>
	 * <li><code>false</code>, the segment will be replied by the iterator
	 * when its end point will be found in the list;</li>
	 * <li><code>null</code>, the segment will be replied when it will be
	 * found at the first time in the list.</li>
	 * </ul>
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * <var>endSegment</var> or the antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param startSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param endSegment is the last segment to iterate.
	 * @param endSegmentConnectedByItsStart indicates if the start segment is connected by its first point.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @param system is the 2D coordinate system used to project a clockwise circle.
	 * @return the iterator on segments.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType,
			CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to the {@link #DEFAULT_CLOCKWHISE_TYPE default clockwhise type}.
	 * Assuming that the segment candidates should be replied only once time
	 * if they are all included, the <var>startSegment</var> will never be
	 * replied as the last segment.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param system is the 2D coordinate system used to project a clockwise circle.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, CoordinateSystem2D system);

	/** Replies an iterator which loop on the segment in a clockwise order
	 * from the specified segment.
	 *
	 * <p>The first segment replied by this function will be the
	 * <var>startSegment</var> or the second segment from the list, according
	 * to <var>bountType</var>.
	 * The last segment replied by this function will be the
	 * last or antepenulvian segment from the list, according
	 * to <var>bountType</var>.
	 *
	 * @param startSegment is the first segment to iterate.
	 * @param boundType indicates if the two given segments will be included into the iterated segment set.
	 * @param system is the 2D coordinate system used to project a clockwise circle.
	 * @return an iterable data structure.
	 */
	@Pure
	Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, ClockwiseBoundType boundType, CoordinateSystem2D system);

	/** Replies the wrapped road connection if this object is a wrapper to
	 * another road connection. If this object is not a wrapper to another
	 * road connection, relies this object iteself.
	 *
	 * @return the wrapped road connection or this road connection itself.
	 * @since 4.0
	 */
	@Pure
	RoadConnection getWrappedRoadConnection();

	/**
	 * Describes the type of treatment for the bounds of a (counter)clockwise
	 * iterator on the segments.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	enum ClockwiseBoundType {

		/** The start segment is excluded from iterations.
		 * Caution, if the end segment is the same as the start
		 * segment, the segment is still replied as the last segment.
		 */
		EXCLUDE_START_SEGMENT,
		/** The end segment is excluded from iterations.
		 * Caution, if the end segment is the same as the start
		 * segment, the segment is still replied as the first segment.
		 */
		EXCLUDE_END_SEGMENT,
		/** The start and end segments are excluded from iterations.
		 */
		EXCLUDE_BOTH,
		/** The start and end segments are included into iterations.
		 */
		INCLUDE_BOTH;

		/** Replies if the current constant includes the start connection point.
		 * @return <code>true</code> if this type of inclusion allows the start segment to be iterated,
		 *     otherwise <code>false</code>
		 */
		@Pure
		public boolean includeStart() {
			return this == INCLUDE_BOTH || this == EXCLUDE_END_SEGMENT;
		}

		/** Replies if the current constant includes the end connection point.
		 * @return <code>true</code> if this type of inclusion allows the end segment to be iterated,
		 *     otherwise <code>false</code>
		 */
		@Pure
		public boolean includeEnd() {
			return this == INCLUDE_BOTH || this == EXCLUDE_START_SEGMENT;
		}
	}

}

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

package org.arakhne.afc.gis.road.primitive;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.GISFlagContainer;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Path2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.graph.GraphIterator;
import org.arakhne.afc.math.graph.GraphSegment;


/**
 * This interface describes a road segment
 *
 * <p>A RoadSegment is an elementary polyline which could be connected
 * to other RoadSegments by {@link RoadConnection}.
 *
 * <p><strong>Caution:</strong> the road segment has weak references on its RoadConnections, and
 * the RoadConnections have weak references to the RoadSegments.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:methodcount")
public interface RoadSegment extends AttributeCollection, GISFlagContainer,
		GraphSegment<RoadSegment, RoadConnection>, Segment1D<Point2d, Vector2d> {

	@Override
	@Pure
	RoadSegment clone();

	//-------------------------------------------------
	// Getter functions
	//-------------------------------------------------

	@Override
	@Pure
	UUID getUUID();

	/** Replies the road network that contains this segment.
	 *
	 * @return the road network that contains this segment.
	 */
	@Pure
	RoadNetwork getRoadNetwork();

	@Override
	@Pure
	RoadConnection getOtherSidePoint(RoadConnection ref_point);

	@Override
	@Pure
	RoadConnection getBeginPoint();

	@Override
	@Pure
	RoadConnection getEndPoint();

	/** Replies the coordinates of the antepenulvian (before last) point.
	 *
	 * @return the coordinates of the antepenulvian (before last) point.
	 */
	@Pure
	Point2d getAntepenulvianPoint();

	@Override
	@Pure
	Point2d getFirstPoint();

	@Override
	@Pure
	Point2d getLastPoint();

	/** Returns the road length in the geo-located referencial.
	 * The length is the distance between the first point and
	 * the last point of the road.
	 *
	 * @return the length of the road in meters.
	 */
	@Override
	@Pure
	double getLength();

	/** Replies the distance to the road border according to the driving side on the road.
	 *
	 * <p>This function is equivalent to calls to {@link #getLaneCenter(int)} and
	 * {@link #getLaneSize(int)}:
	 * <pre><code>
	 * getLaneCenter(0) - getLaneSize(0) / 2
	 * </code></pre>
	 *
	 * @return shift distance from the segment's center to the road border.
	 * @since 14.0
	 */
	@Pure
	double getRoadBorderDistance();

	/** Returns the road width in the geo-located referencial.
	 * The width is the distance between the border lines of the road.
	 *
	 * @return the width of the road in meters.
	 */
	@Pure
	double getWidth();

	/** Set the road width in the geo-located referencial.
	 * The width is the distance between the border lines of the road.
	 *
	 * @param width is the width of the road in meters.
	 * @since 4.0
	 */
	void setWidth(double width);

	/** Returns the name of the road.
	 *
	 * @return the name of the road.
	 * @since 4.0
	 */
	@Pure
	String getName();

	/** Set the name of the road.
	 *
	 * @param name is the name of the road.
	 * @since 4.0
	 */
	void setName(String name);

	/** Replies if this segment is connected to the specified segment.
	 *
	 * @param otherSegment a segment.
	 * @return <code>true</code> if this segment is connected to the given one,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isConnectedTo(RoadSegment otherSegment);

	/** Replies the shared connection between this segment and
	 * the specified one.
	 *
	 * @param otherSegment a segment.
	 * @return a shared connection if the two segments are connected, otherwise
	 * <code>null</code>
	 */
	@Pure
	RoadConnection getSharedConnectionWith(RoadSegment otherSegment);

	/** Replies the distance from the starting point.
	 *
	 * @param ratio is the position on the segment. <code>0</code> for the
	 *     starting point and <code>1</code> for the ending point.
	 * @return the distance
	 */
	@Pure
	double getDistanceFromStart(double ratio);

	/** Replies the distance to the ending point.
	 *
	 * @param ratio is the position on the segment. <code>0</code> for the
	 *     starting point and <code>1</code> for the ending point.
	 * @return the distance
	 */
	@Pure
	double getDistanceToEnd(double ratio);

	/** Replies the geo-location of the point described by the location ratio.
	 * The location ratio is <code>0</code> for the starting point and <code>1</code>
	 * for the ending point.
	 *
	 * @param ratio is the location ratio.
	 * @return the location.
	 */
	@Pure
	Point2d getGeoLocationForLocationRatio(double ratio);

	/** Replies the geo-location of the point described by the location ratio.
	 * The location ratio is <code>0</code> for the starting point and <code>1</code>
	 * for the ending point.
	 *
	 * <p>The shifting value depends on the given 2D coordinate system.
	 *
	 * @param ratio is the location ratio.
	 * @param shifting is the shifting distance.
	 * @return the geo-location.
	 */
	@Pure
	Point2d getGeoLocationForLocationRatio(double ratio, double shifting);

	/** Replies the geo-location of the point described by the location ratio.
	 * The location ratio is <code>0</code> for the starting point and <code>1</code>
	 * for the ending point.
	 *
	 * @param ratio is the location ratio.
	 * @param shifting is the shifting distance.
	 * @param tangent is the vector which will be set by the coordinates of the tangent at the replied point.
	 *     If <code>null</code> the tangent will not be computed.
	 * @return the geo-location.
	 */
	@Pure
	Point2d getGeoLocationForLocationRatio(double ratio, double shifting,
			Vector2D<?, ?> tangent);

	/** Replies the geo-location of the point described by the location ratio.
	 * The location ratio is <code>0</code> for the starting point and <code>1</code>
	 * for the ending point.
	 *
	 * @param ratio is the location ratio.
	 * @param geoLocation is the point to set with geo-localized coordinates.
	 */
	void getGeoLocationForLocationRatio(double ratio, Point2D<?, ?> geoLocation);

	/** Replies the geo-location of the point described by the location ratio.
	 * The location ratio is <code>0</code> for the starting point and <code>1</code>
	 * for the ending point.
	 *
	 * <p>The shifting value depends on the given 2D coordinate system.
	 *
	 * @param ratio is the location ratio.
	 * @param shifting is the shifting distance.
	 * @param geoLocation is the point to set with geo-localized coordinates.
	 */
	void getGeoLocationForLocationRatio(double ratio, double shifting,
			Point2D<?, ?> geoLocation);

	/** Replies the geo-location of the point described by the location ratio.
	 * The location ratio is <code>0</code> for the starting point and <code>1</code>
	 * for the ending point.
	 *
	 * @param ratio is the location ratio.
	 * @param shifting is the shifting distance.
	 * @param geoLocation is the point to set with geo-localized coordinates.
	 * @param tangent is the vector which will be set by the coordinates of the tangent at the replied point.
	 *     If <code>null</code> the tangent will not be computed.
	 */
	void getGeoLocationForLocationRatio(double ratio, double shifting,
			Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent);

	/** Replies the geo-location of the point described by the specified distance.
	 * The desired distance is <code>0</code> for the starting point and {@link #getLength()}
	 * for the ending point.
	 *
	 * @param desired_distance is the distance for which the geo location must be computed.
	 * @return the geo-location.
	 */
	@Pure
	Point2d getGeoLocationForDistance(double desired_distance);

	/** Replies the geo-location of the point described by the specified distance.
	 * The desired distance is <code>0</code> for the starting point and {@link #getLength()}
	 * for the ending point.
	 *
	 * @param desired_distance is the distance for which the geo location must be computed.
	 * @param shifting is the shifting distance.
	 * @return the geo-location.
	 */
	@Pure
	Point2d getGeoLocationForDistance(double desired_distance, double shifting);

	/** Replies the geo-location of the point described by the specified distance.
	 * The desired distance is <code>0</code> for the starting point and {@link #getLength()}
	 * for the ending point.
	 *
	 * @param desired_distance is the distance for which the geo location must be computed.
	 * @param shifting is the shifting distance.
	 * @param tangent is the vector which will be set by the coordinates of the tangent at the replied point.
	 *     If <code>null</code> the tangent will not be computed.
	 * @return the geo-location.
	 */
	@Pure
	Point2d getGeoLocationForDistance(double desired_distance, double shifting,
			Vector2D<?, ?> tangent);

	/** Replies the geo-location of the point described by the specified distance.
	 * The desired distance is <code>0</code> for the starting point and {@link #getLength()}
	 * for the ending point.
	 *
	 * @param desired_distance is the distance for which the geo location must be computed.
	 * @param geoLocation is the point to set with geo-localized coordinates.
	 */
	void getGeoLocationForDistance(double desired_distance, Point2D<?, ?> geoLocation);

	/** Replies the geo-location of the point described by the specified distance.
	 * The desired distance is <code>0</code> for the starting point and {@link #getLength()}
	 * for the ending point.
	 *
	 * @param desired_distance is the distance for which the geo location must be computed.
	 * @param shifting is the shifting distance.
	 * @param geoLocation is the point to set with geo-localized coordinates.
	 */
	void getGeoLocationForDistance(double desired_distance, double shifting,
			Point2D<?, ?> geoLocation);

	/** Replies the geo-location of the point described by the specified distance.
	 * The desired distance is <code>0</code> for the starting point and {@link #getLength()}
	 * for the ending point.
	 *
	 * @param desired_distance is the distance for which the geo location must be computed.
	 * @param shifting is the shifting distance.
	 * @param geoLocation is the point to set with geo-localized coordinates.
	 * @param tangent is the vector which will be set by the coordinates of the tangent at the replied point.
	 *     If <code>null</code> the tangent will not be computed.
	 */
	void getGeoLocationForDistance(double desired_distance, double shifting,
			Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent);

	//-------------------------------------------------
	// Get Chain functions
	//-------------------------------------------------

	/**
	 * Replies a list of chained road segments without any cross-road
	 * which contains this road segment.
	 *
	 * @return a list of road segments.
	 */
	@Pure
	List<RoadSegment> getSegmentChain();

	/**
	 * Replies a list of chained road segments without any cross-road
	 * which contains this road segment.
	 *
	 * @param forward_search must be <code>true</code> to search the chain's segments in the
	 *     forward direction for this road segment.
	 * @param backward_search must be <code>true</code> to search the chain's segments in the
	 *     backward direction for this road segment.
	 * @return a list of road segments.
	 */
	@Pure
	List<RoadSegment> getSegmentChain(boolean forward_search, boolean backward_search);

	//-------------------------------------------------
	// Iterator functions
	//-------------------------------------------------

	/** Replies an iterator that permits to move along the road segment's graph
	 * starting from this road segment and from the specified starting point.
	 * If the specified starting point is not one of the ends of th segment,
	 * this function assumes to start from the point replied by
	 * {@link #getBeginPoint()}.
	 *
	 * @param starting_point is the point from which the iteration must start.
	 * @param allowManyReplies is <code>true</code> to allow cycles during iterations, otherwise <code>false</code>
	 * @param assumeOrientedSegments indicates if the iterator is taking into account
	 *     the orientation of the road segments. If <code>true</code> it assumes that
	 *     a segment could be reached by both its end points. If <code>false</code> it
	 *     assumes that a segment could be reach only one time. This parameter is used only
	 *     when <var>allowManyReplies</var> was set to <code>true</code>.
	 * @return an iterator
	 */
	@Pure
	GraphIterator<RoadSegment, RoadConnection> iterator(RoadConnection starting_point,
			boolean allowManyReplies, boolean assumeOrientedSegments);

	/** Replies an iterator that permits to move along the road segment's graph
	 * starting from this road segment and from the specified starting point.
	 * This function assumes to start from the point replied by
	 * {@link #getBeginPoint()}.
	 * This function does not allow the cycles during the iterations.
	 *
	 * @return an iterator
	 */
	@Pure
	GraphIterator<RoadSegment, RoadConnection> iterator();

	/** Replies an iterator that permits to move along the road segment's graph
	 * starting from this road segment and from the specified starting point.
	 * If the specified starting point is not one of the ends of th segment,
	 * this function assumes to start from the point replied by
	 * {@link #getBeginPoint()}.
	 *
	 * @param depth is the maximal depth to reach (in meters).
	 * @param position_from_starting_point is the starting position from
	 *     the <var>starting_point</var> (in meters).
	 * @param starting_point is the point from which the iteration must start.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same
	 *     segment, otherwise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of
	 *     the segment and the second instance is associated to the last point of the segment.
	 *     If this parameter is <code>false</code> to assume that the end points of a segment
	 *     are not distinguished.
	 * @return an iterator
	 */
	@Pure
	GraphIterator<RoadSegment, RoadConnection> depthIterator(
			double depth, double position_from_starting_point,
			RoadConnection starting_point,
			boolean allowManyReplies, boolean assumeOrientedSegments);

	//-------------------------------------------------
	// User data functions
	//-------------------------------------------------

	/** Replies if at least one user data is associated
	 * to the given identifier.
	 *
	 * <p>The user data ar not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param id is the identifier of the group
	 * @return <code>true</code> if one user data exists,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean hasUserData(String id);

	/** Replies if the given one user data is associated
	 * to the given identifier.
	 *
	 * <p>The user data ar not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param id is the identifier of the group
	 * @param data is the data to insert in the group
	 * @return <code>true</code> if the user data exists,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean containsUserData(String id, Object data);

	/** Add an user data associated to this segment.
	 *
	 * <p>The user data ar not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param id is the identifier of the group
	 * @param data is the data to insert in the group
	 */
	void addUserData(String id, Object data);

	/** Set an user data associated to this segment.
	 *
	 * <p>The user data ar not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param id is the identifier of the group
	 * @param data is the data to insert in the group
	 */
	void setUserData(String id, Object data);

	/** Remove an user data associated to this segment.
	 *
	 * <p>The user data ar not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param id is the identifier of the group
	 * @param data is the data to remove from the group
	 * @return <code>true</code> if the data was removed,
	 *     otherwise <code>false</code>.
	 */
	boolean removeUserData(String id, Object data);

	/** Clear all the data from a group.
	 *
	 * <p>The user data ar not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param id is the identifier of the group
	 */
	void clearUserData(String id);

	/** Clear all the data from all the groups.
	 *
	 * <p>The user data ar not stored as attributes because
	 * they are assumed as transient.
	 */
	void clearUserData();

	/** Replies an user data associated to this segment.
	 *
	 * <p>The user data are not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param <T> is the type of the data to reply
	 * @param id is the identifier of the group
	 * @return the first data in the group or <code>null</code>.
	 */
	@Pure
	<T> T getUserData(String id);

	/** Replies the user data associated to this segment.
	 *
	 * <p>The user data are not stored as attributes because
	 * they are assumed as transient.
	 *
	 * @param <T> is the type of the data to reply
	 * @param id is the identifier of the group
	 * @return the list of the user data in the group, never <code>null</code>.
	 */
	@Pure
	<T> Collection<? extends T> getUserDataCollection(String id);

	//-------------------------------------------------
	// Geometry functions
	//-------------------------------------------------

	/**
	 * Replies the distance between this MapElement and
	 * point.
	 *
	 * @param point a point
	 * @return the distance. Could be negative depending of the implementation type.
	 */
	@Pure
	double distance(Point2D<?, ?> point);

	/** Replies the distance between this figure and the specified point.
	 *
	 * @param point is the coordinate of the point.
	 * @param width is the width of the polyline.
	 * @return the computed distance
	 */
	@Pure
	double distance(Point2D<?, ?> point, double width);

	/**
	 * Replies the distance between the nearest end of this MapElement and
	 * the point.
	 *
	 * @param point a point.
	 * @return the distance. Should be negative depending of the MapElement type.
	 */
	@Pure
	double distanceToEnd(Point2D<?, ?> point);

	/** Replies the distance between the nearest end of this MapElement and
	 * the point.
	 *
	 * @param point is the coordinate of the point.
	 * @param width is the width of the polyline.
	 * @return the computed distance
	 */
	@Pure
	double distanceToEnd(Point2D<?, ?> point, double width);

	/**
	 * Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the figure of this MapElement.
	 *
	 * @param point is a geo-referenced coordinate
	 * @param delta is the geo-referenced distance that corresponds to a approximation
	 *     distance in the screen coordinate system
	 * @return <code>true</code> if the specified point has a distance nearest than delta
	 *     to this element, otherwise <code>false</code>
	 */
	@Pure
	boolean contains(Point2D<?, ?> point, double delta);

	/**
	 * Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the figure of this MapElement.
	 *
	 * <p>If this MapElement has no associated figure, this method
	 * always returns <code>false</code>.
	 *
	 * @param point is a geo-referenced coordinate
	 * @return <code>true</code> if this MapElement had an associated figure and
	 *     the specified point was inside this bounds of this figure, otherwise
	 * <code>false</code>
	 */
	@Pure
	boolean contains(Point2D<?, ?> point);

	@Override
	@Pure
	GeoLocation getGeoLocation();

	/** Replies the count of points in all the parts.
	 *
	 * @return the count of points
	 */
	@Pure
	int getPointCount();

	/** Replies the iterator on the points.
	 *
	 * @return the iterator on the points.
	 */
	@Pure
	Iterable<Point2d> points();

	/** Replies the iterator on the points.
	 *
	 * @return the iterator on the points.
	 */
	@Pure
	Iterator<Point2d> pointIterator();

	/** Replies the specified point at the given index.
	 *
	 * <p>If the <var>index</var> is negative, it will corresponds
	 * to an index starting from the end of the list.
	 *
	 * @param index is the index of the desired point
	 * @return the point at the given index
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	Point2d getPointAt(int index);

	/**
	 * Replies if this element has an intersection
	 * with the specified rectangle.
	 *
	 * <p>If this MapElement has no associated figure, this method
	 * always returns <code>false</code>.
	 *
	 * @param bounds the bounds.s
	 * @return <code>true</code> if this MapElement has an associated figure and
	 *     the specified rectangle intersecting the figure, otherwise
	 * <code>false</code>
	 */
	@Pure
	boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> bounds);

	/** Replies the bounding box of this element.
	 *
	 * @return the bounding box or <code>null</code> if not applicable.
	 */
	@Pure
	Rectangle2d getBoundingBox();

	//-------------------------------------------------
	// Lane functions
	//-------------------------------------------------

	/** Replies the count of lanes on this road segment.
	 *
	 * @return the count of lanes on this road segment.
	 */
	@Pure
	int getLaneCount();

	/** Replies the direction of the lane at the given index.
	 *
	 * <p>The lane order is influence by the side-rule of the
	 * road network, which is replied by {@link RoadNetwork#isLeftSidedTrafficDirection()}
	 * or by {@link RoadNetwork#isRightSidedTrafficDirection()}:
	 * if left-sided the left-most lane has index 0, if right-sided the
	 * right-most lane has index 0.
	 *
	 * @param laneIndex the lane index.
	 * @return the count of lanes on this road segment.
	 */
	@Pure
	Direction1D getLaneDirection(int laneIndex);

	/** Replies the size of the lane at the given index.
	 *
	 * <p>The lane order is influence by the side-rule of the
	 * road network, which is replied by {@link RoadNetwork#isLeftSidedTrafficDirection()}
	 * or by {@link RoadNetwork#isRightSidedTrafficDirection()}:
	 * if left-sided the left-most lane has index 0, if right-sided the
	 * right-most lane has index 0.
	 *
	 * @param laneIndex the lane index.
	 * @return the size of the lane in meters
	 */
	@Pure
	double getLaneSize(int laneIndex);

	/** Replies the center line of the lane at the given index.
	 *
	 * <p>The center is given by the jutting distance from the road segment
	 * center to the center of the lane.
	 *
	 * <p>The lane order is influence by the side-rule of the
	 * road network, which is replied by {@link RoadNetwork#isLeftSidedTrafficDirection()}
	 * or by {@link RoadNetwork#isRightSidedTrafficDirection()}:
	 * if left-sided the left-most lane has index 0, if right-sided the
	 * right-most lane has index 0.
	 *
	 * @param laneIndex the lane index.
	 * @return shift distance from the segment's center to the lane center.
	 */
	@Pure
	double getLaneCenter(int laneIndex);

	/** Replies the wrapped road segment if this object is a wrapper to
	 * another road segment. If this object is not a wrapper to another
	 * road segment, relies this object iteself.
	 *
	 * @return the wrapped road segment or this road segment itself.
	 */
	@Pure
	RoadSegment getWrappedRoadSegment();

	/** Replies if this road segment is traversable from the given point.
	 *
	 * <p>If the given point is not the start point nor the end point, then this
	 * function replies <code>false</code>.
	 *
	 * @param point a point.
	 * @return <code>true</code> if this segment is traversable starting from
	 *     the given point; otherwise <code>false</code>.
	 */
	@Pure
	boolean isTraversableFrom(RoadConnection point);

	/** Replies the traffic direction on this road segment.
	 *
	 * @return the traffic direction
	 * @since 4.0
	 */
	@Pure
	TrafficDirection getTrafficDirection();

	/** Set the traffic direction on this road segment.
	 *
	 * @param direction is the new traffic direction. If <code>null</code>,
	 *     the default traffic direction will be set.
	 * @since 4.0
	 */
	void setTrafficDirection(TrafficDirection direction);

	/** Replies the type of the road segment.
	 *
	 * @return the type of the road segment.
	 * @since 4.0
	 */
	@Pure
	RoadType getRoadType();

	/** Set the type of the road segment.
	 *
	 * @param type is the type of the road segment.
	 * @since 4.0
	 */
	void setRoadType(RoadType type);

	/**
	 * Return the nearest point 1.5D from a 2D position.
	 *
	 * @param pos is the testing position.
	 * @return the nearest 1.5D position on the road network.
	 */
	@Pure
	Point1d getNearestPosition(Point2D<?, ?> pos);

	/** Replies the number of the road segment.
	 *
	 * @return the number of the road segment.
	 * @since 4.0
	 */
	@Pure
	String getRoadNumber();

	/** Set the number of the road segment.
	 *
	 * @param number is the number of the road segment.
	 * @since 4.0
	 */
	void setRoadNumber(String number);

	/** Replies the path representing this road segment.
	 *
	 * @return the path.
	 */
	@Pure
	@Inline(value = "toPath2D($1.NaN, $1.NaN)", imported = {Double.class})
	default Path2d toPath2D() {
		return toPath2D(Double.NaN, Double.NaN);
	}

	/** Replies the path representing this road segment.
	 *
	 * @param startPosition the position along the segment at which the path representation should start.
	 *     If {@link Double#NaN} or negative, the path starts at the beginning of the road segment.
	 * @param endPosition the position along the segment at which the path representation should end.
	 *     If {@link Double#NaN} or greater than the segment length, the path ends at the end of the road segment.
	 * @return the path.
	 */
	default Path2d toPath2D(double startPosition, double endPosition) {
		final Path2d path = new Path2d();
		toPath2D(path, startPosition, endPosition);
		return path;
	}

	/** Fill the given path with the values representing this road segment.
	 *
	 * @param path the path to fill out.
	 */
	@Inline(value = "toPath2D($1, $2.NaN, $2.NaN)", imported = {Double.class})
	default void toPath2D(Path2d path) {
		toPath2D(path, Double.NaN, Double.NaN);
	}

	/** Fill the given path with the values representing this road segment.
	 *
	 * @param path the path to fill out.
	 * @param startPosition the position along the segment at which the path representation should start.
	 *     If {@link Double#NaN} or negative, the path starts at the beginning of the road segment.
	 * @param endPosition the position along the segment at which the path representation should end.
	 *     If {@link Double#NaN} or greater than the segment length, the path ends at the end of the road segment.
	 */
	void toPath2D(Path2d path, double startPosition, double endPosition);

}

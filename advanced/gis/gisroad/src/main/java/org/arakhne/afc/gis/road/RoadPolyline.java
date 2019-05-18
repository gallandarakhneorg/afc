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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.mapelement.PointFusionValidator;
import org.arakhne.afc.gis.road.path.RoadPath;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetworkConstants;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.gis.road.primitive.RoadType;
import org.arakhne.afc.gis.road.primitive.TrafficDirection;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.graph.GraphIterator;
import org.arakhne.afc.util.OutputParameter;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class describes a road segment
 *
 * <p>A RoadSegment is an elementary polyline which could be connected
 * to other RoadSegments by {@link RoadConnection}.
 *
 * <p><strong>Caution:</strong> the road segment has weak references on its RoadConnections, and
 * the RoadConnections have weak references to the RoadSegments.
 *
 * <p><strong>Caution:</strong> when loaded, this class set the default coordinate systems
 * with {@link GeoLocationUtil#setGISCoordinateSystemAsDefault()}
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings({"checkstyle:methodcount"})
public class RoadPolyline extends MapPolyline implements RoadSegment {

	private static final long serialVersionUID = -2881502228274578187L;

	static {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
	}

	private static final Validator DEFAULT_VALIDATOR = new Validator();

	private StandardRoadConnection firstConnection;

	private StandardRoadConnection lastConnection;

	private WeakReference<StandardRoadNetwork> roadNetwork;

	private transient Map<String, List<Object>> userData;

	private RoadType roadType;

	private double width = Double.NaN;


	/** Create a new road segment.
	 */
	public RoadPolyline() {
		this(null, null);
	}

	/** Create a new road segment.
	 *
	 * @param attributeProvider is the attribute provider associated to this segment.
	 */
	public RoadPolyline(AttributeCollection attributeProvider) {
		this(null, attributeProvider);
	}

	/** Create a new road segment.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @since 4.0
	 */
	public RoadPolyline(UUID id) {
		this(id, null);
	}

	/** Create a new road segment.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute provider associated to this segment.
	 * @since 4.0
	 */
	public RoadPolyline(UUID id, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		this.userData = null;
		setWidePolyline(true);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("roadType", getRoadType()); //$NON-NLS-1$
		buffer.add("width", getWidth()); //$NON-NLS-1$
	}


	/** Clone this object to obtain a valid copy.
	 *
	 * @return a copy
	 */
	@Override
	@Pure
	public RoadPolyline clone() {
		final RoadPolyline element = (RoadPolyline) super.clone();

		if (this.userData == null) {
			element.userData = null;
		} else {
			element.userData = new TreeMap<>();
			List<Object> dta;
			for (final Entry<String, List<Object>> entry : this.userData.entrySet()) {
				if (entry.getValue() == null) {
					dta = null;
				} else {
					dta = new ArrayList<>(entry.getValue());
				}
				element.userData.put(entry.getKey(), dta);
			}
		}

		element.firstConnection = null;
		element.lastConnection = null;
		element.roadNetwork = null;
		element.clearUserData();

		return element;
	}

	@Override
	@SuppressWarnings({"checkstyle:equalshashcode", "unlikely-arg-type"})
	@Pure
	public boolean equals(Object element) {
		if (element instanceof RoadSegment && !(element instanceof MapElement)) {
			RoadSegment road = (RoadSegment) element;
			RoadSegment rd = road.getWrappedRoadSegment();
			while (rd != road) {
				road = rd;
				rd = road.getWrappedRoadSegment();
			}
			return super.equals(road);
		}
		return super.equals(element);
	}

	//-------------------------------------------------
	// Getter functions
	//-------------------------------------------------

	@Override
	@Pure
	public PointFusionValidator getPointFusionValidator() {
		DEFAULT_VALIDATOR.refresh();
		return DEFAULT_VALIDATOR;
	}

	@Override
	@Pure
	public StandardRoadNetwork getRoadNetwork() {
		return this.roadNetwork == null ? null : this.roadNetwork.get();
	}

	/** Set the road network that contains this segment.
	 *
	 * @param network the network.
	 */
	void setRoadNetwork(StandardRoadNetwork network) {
		this.roadNetwork = network == null ? null : new WeakReference<>(network);
	}

	@Pure
	@Override
	public RoadConnection getOtherSidePoint(RoadConnection ref_point) {
		final RoadConnection pt = ref_point.getWrappedRoadConnection();
		if (getBeginPoint(StandardRoadConnection.class).equals(pt)) {
			return getEndPoint(StandardRoadConnection.class);
		}
		if (getEndPoint(StandardRoadConnection.class).equals(pt)) {
			return getBeginPoint(StandardRoadConnection.class);
		}
		return null;
	}

	@Pure
	@Override
	public RoadConnection getBeginPoint() {
		return getBeginPoint(RoadConnectionWithArrivalSegment.class);
	}

	/**
	 * Replies the first point of this segment.
	 *
	 * @param <CT> is the type of the connection to reply
	 * @param connectionClass is the type of the connection to reply
	 * @return the first point of <code>null</code>
	 */
	@Pure
	<CT extends RoadConnection> CT getBeginPoint(Class<CT> connectionClass) {
		final StandardRoadConnection connection = this.firstConnection;
		if (connection == null) {
			return null;
		}
		if (connectionClass.isAssignableFrom(StandardRoadConnection.class)) {
			return connectionClass.cast(connection);
		}
		if (connectionClass.isAssignableFrom(RoadConnectionWithArrivalSegment.class)) {
			return connectionClass.cast(new RoadConnectionWithArrivalSegment(connection, this, true));
		}
		throw new IllegalArgumentException("unsupported RoadConnection class");  //$NON-NLS-1$
	}

	@Pure
	@Override
	public RoadConnection getEndPoint() {
		return getEndPoint(RoadConnectionWithArrivalSegment.class);
	}

	/** Replies the last connection point of this segment.
	 *
	 * @param <CT> is the type of the connection to reply
	 * @param connectionClass is the type of the connection to reply
	 * @return the last point of <code>null</code>
	 */
	@Pure
	<CT extends RoadConnection> CT getEndPoint(Class<CT> connectionClass) {
		final StandardRoadConnection connection = this.lastConnection;
		if (connection == null) {
			return null;
		}
		if (connectionClass.isAssignableFrom(StandardRoadConnection.class)) {
			return connectionClass.cast(connection);
		}
		if (connectionClass.isAssignableFrom(RoadConnectionWithArrivalSegment.class)) {
			return connectionClass.cast(new RoadConnectionWithArrivalSegment(connection, this, false));
		}
		throw new IllegalArgumentException("unsupported RoadConnection class");  //$NON-NLS-1$
	}

	/**
	 * Replies the nearest start/end point to the specified point.
	 *
	 * @param <CT> is the type of the connection to reply
	 * @param connectionClass is the type of the connection to reply
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the nearest point
	 */
	@Pure
	<CT extends RoadConnection> CT getNearestPoint(Class<CT> connectionClass, double x, double y) {
		final int index = getNearestEndIndex(x, y);
		if (index == 0) {
			return getBeginPoint(connectionClass);
		}
		return getEndPoint(connectionClass);
	}

	/**
	 * Replies the nearest start/end point to the specified point.
	 *
	 * @param <CT> is the type of the connection to reply
	 * @param connectionClass is the type of the connection to reply
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param distance is the distance between the given point and the start/end point.
	 * @return the nearest point
	 */
	@Pure
	<CT extends RoadConnection> CT getNearestPoint(Class<CT> connectionClass, double x, double y,
			OutputParameter<Double> distance) {
		final int index = getNearestEndIndex(x, y, distance);
		if (index == 0) {
			return getBeginPoint(connectionClass);
		}
		return getEndPoint(connectionClass);
	}

	@Override
	@Pure
	public Point2d getAntepenulvianPoint() {
		return getPointAt(getPointCount() - 2);
	}

	@Override
	@Pure
	public Point2d getFirstPoint() {
		final RoadConnection connection = getBeginPoint(RoadConnectionWithArrivalSegment.class);
		if (connection == null) {
			return getPointAt(0);
		}
		return connection.getPoint();
	}

	@Override
	@Pure
	public Point2d getLastPoint() {
		final RoadConnection connection = getEndPoint(RoadConnectionWithArrivalSegment.class);
		if (connection == null) {
			return getPointAt(getPointCount() - 1);
		}
		return connection.getPoint();
	}

	@Override
	public RoadPolyline invert() {
		return (RoadPolyline) super.invert();
	}

	@Override
	public RoadPolyline invertPointsIn(int groupIndex) {
		return (RoadPolyline) super.invertPointsIn(groupIndex);
	}

	@Override
	@Pure
	public boolean isConnectedTo(RoadSegment otherSegment) {
		final RoadConnection startPoint = getBeginPoint(StandardRoadConnection.class);
		final RoadConnection endPoint = getEndPoint(StandardRoadConnection.class);
		if (otherSegment != this) {
			if (startPoint != null && startPoint.isConnectedSegment(otherSegment)) {
				return true;
			}
			if (endPoint != null && endPoint.isConnectedSegment(otherSegment)) {
				return true;
			}
		} else if (startPoint != null && startPoint.equals(endPoint)) {
			return true;
		}
		return false;
	}

	@Override
	@Pure
	public boolean isTraversableFrom(RoadConnection point) {
		final RoadConnection startPoint = getBeginPoint(StandardRoadConnection.class);
		final RoadConnection endPoint = getEndPoint(StandardRoadConnection.class);
		if (startPoint.equals(point)) {
			final TrafficDirection d = getTrafficDirection();
			return d == TrafficDirection.DOUBLE_WAY || d == TrafficDirection.ONE_WAY;
		}
		if (endPoint.equals(point)) {
			final TrafficDirection d = getTrafficDirection();
			return d == TrafficDirection.DOUBLE_WAY || d == TrafficDirection.NO_ENTRY;
		}
		return false;
	}

	@Override
	@Pure
	public TrafficDirection getTrafficDirection() {
		try {
			final TrafficDirection direction = TrafficDirection.fromString(
					getAttribute(RoadNetworkConstants.getPreferredAttributeNameForTrafficDirection(), (String) null));
			if (direction != null) {
				return direction;
			}
		} catch (Throwable exception) {
			//
		}
		return TrafficDirection.DOUBLE_WAY;
	}

	@Override
	public void setTrafficDirection(TrafficDirection direction) {
		if (direction == null) {
			removeAttribute(RoadNetworkConstants.getPreferredAttributeNameForTrafficDirection());
		} else {
			setAttribute(RoadNetworkConstants.getPreferredAttributeNameForTrafficDirection(),
					RoadNetworkConstants.getPreferredAttributeValueForTrafficDirection(direction, 0));
		}
	}

	@Override
	@Pure
	public RoadType getRoadType() {
		if (this.roadType == null) {
			final String attrName = RoadNetworkConstants.getPreferredAttributeNameForRoadType();
			this.roadType = getAttribute(
					attrName,
					RoadType.OTHER);
		}
		return this.roadType;
	}

	@Override
	public void setRoadType(RoadType type) {
		if (type != null && type != this.roadType) {
			this.roadType = type;
			setAttribute(
					RoadNetworkConstants.getPreferredAttributeNameForRoadType(),
					type);
		}
	}

	@Override
	@Pure
	public String getRoadNumber() {
		return getAttribute(
				RoadNetworkConstants.getPreferredAttributeNameForRoadNumber(),
				(String) null);
	}

	@Override
	public void setRoadNumber(String number) {
		setAttribute(
				RoadNetworkConstants.getPreferredAttributeNameForRoadNumber(),
				number);
	}

	@Override
	@Pure
	public String getName() {
		final String name = getAttribute(
				RoadNetworkConstants.getPreferredAttributeNameForRoadName(),
				(String) null);
		if (name != null && !"".equals(name)) { //$NON-NLS-1$
			return name;
		}
		return super.getName();
	}

	@Override
	public void setName(String name) {
		setAttribute(
				RoadNetworkConstants.getPreferredAttributeNameForRoadName(),
				name);
		super.setName(name);
	}

	@Override
	@Pure
	public RoadConnection getSharedConnectionWith(RoadSegment otherSegment) {
		if (otherSegment != this) {
			final RoadConnection startPoint = getBeginPoint(StandardRoadConnection.class);
			final RoadConnection endPoint = getEndPoint(StandardRoadConnection.class);
			if (startPoint != null && startPoint.isConnectedSegment(otherSegment)) {
				return startPoint;
			}
			if (endPoint != null && endPoint.isConnectedSegment(otherSegment)) {
				return endPoint;
			}
		}
		return null;
	}

	@Override
	@Pure
	public double getDistanceFromStart(double ratio) {
		if (ratio < 0) {
			return 0.;
		}
		if (ratio > 1) {
			return getLength();
		}
		return ratio * getLength();
	}

	@Override
	@Pure
	public double getDistanceToEnd(double ratio) {
		if (ratio > 1) {
			return 0.;
		}
		final double segmentLength = getLength();
		if (ratio < 0) {
			return segmentLength;
		}
		return segmentLength - (ratio * segmentLength);
	}

	@Override
	@Pure
	public Vector2d getTangentAt(double positionOnSegment) {
		final Vector2d tangent = new Vector2d();
		getGeoLocationForDistance(positionOnSegment, 0.,
				tangent);
		return tangent;
	}

	@Override
	public void projectsOnPlane(double positionOnSegment, Point2D<?, ?> position,
			Vector2D<?, ?> tangent) {
		final Point2d p;
		if (tangent != null) {
			p = getGeoLocationForDistance(positionOnSegment, 0., tangent);
		} else {
			p = getGeoLocationForDistance(positionOnSegment, 0.);
		}
		if (p != null && position != null) {
			position.set(p);
		}
	}

	@Override
	public void projectsOnPlane(double positionOnSegment, double shiftDistance,
			Point2D<?, ?> position, Vector2D<?, ?> tangent) {
		final Point2d p;
		if (tangent != null) {
			p = getGeoLocationForDistance(positionOnSegment, shiftDistance, tangent);
		} else {
			p = getGeoLocationForDistance(positionOnSegment, shiftDistance);
		}
		if (p != null && position != null) {
			position.set(p);
		}
	}

	@Override
	@Pure
	public final Point2d getGeoLocationForLocationRatio(double ratio) {
		final Point2d gl = new Point2d();
		getGeoLocationForLocationRatio(ratio, 0., gl, null);
		return gl;
	}

	@Override
	@Pure
	public final Point2d getGeoLocationForLocationRatio(double ratio, double shifting) {
		final Point2d gl = new Point2d();
		getGeoLocationForLocationRatio(ratio, shifting, gl, null);
		return gl;
	}

	@Override
	@Pure
	public final void getGeoLocationForLocationRatio(double ratio, Point2D<?, ?> geoLocation) {
		getGeoLocationForLocationRatio(ratio, 0., geoLocation, null);
	}

	@Override
	@Pure
	public final void getGeoLocationForLocationRatio(double ratio, double shifting,
			Point2D<?, ?> geoLocation) {
		getGeoLocationForLocationRatio(ratio, shifting, geoLocation, null);
	}

	@Override
	@Pure
	public final Point2d getGeoLocationForLocationRatio(double ratio, double shifting,
			Vector2D<?, ?> tangent) {
		final Point2d gl = new Point2d();
		getGeoLocationForLocationRatio(ratio, shifting, gl, tangent);
		return gl;
	}

	@Override
	@Pure
	public final void getGeoLocationForLocationRatio(double ratio, double shifting,
			Point2D<?, ?> output, Vector2D<?, ?> tangent) {
		assert output != null;
		if (ratio <= 0.) {
			final Point2d startPoint = getFirstPoint();
			if (tangent != null) {
				final Point2d secondPoint = getPointAt(1);
				tangent.set(
						secondPoint.getX() - startPoint.getX(),
						secondPoint.getY() - startPoint.getY());
			}
			output.set(startPoint);
			return;
		}
		if (ratio >= 1.) {
			final Point2d endPoint = getLastPoint();
			if (tangent != null) {
				final Point2d antepenulvianPoint = getAntepenulvianPoint();
				tangent.set(
						endPoint.getX() - antepenulvianPoint.getX(),
						endPoint.getY() - antepenulvianPoint.getY());
			}
			output.set(endPoint);
			return;
		}

		// Compute the location corresponding to the specified ration and shifting
		final double desiredLength = ratio * getLength();
		getGeoLocationForDistance(desiredLength, shifting, output, tangent);
	}

	@Override
	@Pure
	public final Point2d getGeoLocationForDistance(double desired_distance) {
		final Point2d gl = new Point2d();
		getGeoLocationForDistance(desired_distance, 0., gl, null);
		return gl;
	}

	@Override
	@Pure
	public final Point2d getGeoLocationForDistance(double desired_distance, double shifting) {
		final Point2d gl = new Point2d();
		getGeoLocationForDistance(desired_distance, shifting, gl, null);
		return gl;
	}

	@Override
	@Pure
	public Point2d getGeoLocationForDistance(double desired_distance, double shifting,
			Vector2D<?, ?> tangent) {
		final Point2d gl = new Point2d();
		getGeoLocationForDistance(desired_distance, shifting, gl, tangent);
		return gl;
	}

	@Override
	@Pure
	public final void getGeoLocationForDistance(double desired_distance, Point2D<?, ?> geoLocation) {
		getGeoLocationForDistance(desired_distance, 0., geoLocation, null);
	}

	@Override
	@Pure
	public final void getGeoLocationForDistance(double desired_distance, double shifting,
			Point2D<?, ?> geoLocation) {
		getGeoLocationForDistance(desired_distance, shifting, geoLocation, null);
	}

	@Override
	@Pure
	public void getGeoLocationForDistance(double desired_distance, double shifting,
			Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
		computeGeoLocationForDistance(desired_distance, shifting, geoLocation, tangent);
	}

	/**
	 * Move the start point of a road segment.
	 *
	 * <p>This function forces the start point of the given
	 * segment to correspond to the specified connection point
	 * even if the original start point was not near the connection
	 * point.
	 *
	 * @param desiredConnection the connection.
	 */
	void setStartPoint(StandardRoadConnection desiredConnection) {
		final StandardRoadConnection oldPoint = getBeginPoint(StandardRoadConnection.class);
		if (oldPoint != null) {
			oldPoint.removeConnectedSegment(this, true);
		}
		this.firstConnection = desiredConnection;
		if (desiredConnection != null) {
			final Point2d pts = desiredConnection.getPoint();
			if (pts != null) {
				setPointAt(0, pts, true);
			}
			desiredConnection.addConnectedSegment(this, true);
		}
	}

	/**
	 * Move the end point of a road segment.
	 *
	 * <p>This function forces the end point of the given
	 * segment to correspond to the specified connection point
	 * even if the original end point was not near the connection
	 * point.
	 *
	 * @param desiredConnection the connection.
	 */
	void setEndPoint(StandardRoadConnection desiredConnection) {
		final StandardRoadConnection oldPoint = getEndPoint(StandardRoadConnection.class);
		if (oldPoint != null) {
			oldPoint.removeConnectedSegment(this, false);
		}
		this.lastConnection = desiredConnection;
		if (desiredConnection != null) {
			final Point2d pts = desiredConnection.getPoint();
			if (pts != null) {
				setPointAt(-1, pts, true);
			}
			desiredConnection.addConnectedSegment(this, false);
		}
	}

	//-------------------------------------------------
	// Get Chain functions
	//-------------------------------------------------

	@Override
	@Pure
	public List<RoadSegment> getSegmentChain() {
		return getSegmentChain(true, true);
	}

	@Override
	@Pure
	public List<RoadSegment> getSegmentChain(boolean forward_search, boolean backward_search) {
		final List<RoadSegment> chain = new ArrayList<>();

		chain.add(this);

		// Search for segments in the backward direction (starting at the first point)
		if (backward_search) {
			RoadConnection currPoint = getBeginPoint(StandardRoadConnection.class);
			RoadSegment currSegment = currPoint.getOtherSideSegment(this);

			while (currSegment != null) {
				chain.add(0, currSegment);
				currPoint = currSegment.getOtherSidePoint(currPoint);
				currSegment = currPoint.getOtherSideSegment(currSegment);
			}
		}

		// Search for segments in the forward direction (starting at the last point)
		if (forward_search) {
			RoadConnection currPoint = getEndPoint(StandardRoadConnection.class);
			RoadSegment currSegment = currPoint.getOtherSideSegment(this);

			while (currSegment != null) {
				chain.add(chain.size(), currSegment);
				currPoint = currSegment.getOtherSidePoint(currPoint);
				currSegment = currPoint.getOtherSideSegment(currSegment);
			}
		}

		final RoadPath path = new RoadPath();

		path.addAll(chain);

		return path;
	}

	//-------------------------------------------------
	// Iterator functions
	//-------------------------------------------------

	@Override
	@Pure
	public GraphIterator<RoadSegment, RoadConnection> depthIterator(
			double depth, double position_from_starting_point,
			RoadConnection starting_point, boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		RoadConnection pt = starting_point.getWrappedRoadConnection();

		if ((pt == null)
				|| ((!getBeginPoint(StandardRoadConnection.class).equals(pt))
						&& (!getEndPoint(StandardRoadConnection.class).equals(pt)))) {
			pt = getBeginPoint(StandardRoadConnection.class);
		}

		return new DistanceBasedRoadNetworkIterator(
				getRoadNetwork(),
				depth, position_from_starting_point,
				this, pt,
				allowManyReplies, assumeOrientedSegments);
	}

	@Override
	@Pure
	public GraphIterator<RoadSegment, RoadConnection> iterator(
			RoadConnection starting_point, boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		RoadConnection pt = starting_point.getWrappedRoadConnection();

		if ((pt == null)
				|| ((!getBeginPoint(StandardRoadConnection.class).equals(pt))
						&& (!getEndPoint(StandardRoadConnection.class).equals(pt)))) {
			pt = getBeginPoint(StandardRoadConnection.class);
		}
		return new RoadNetworkIterator(
				getRoadNetwork(),
				this, pt,
				allowManyReplies, assumeOrientedSegments,
				0.);
	}

	@Override
	@Pure
	public GraphIterator<RoadSegment, RoadConnection> iterator() {
		return iterator(getBeginPoint(StandardRoadConnection.class),
				RoadNetworkConstants.ENABLE_GRAPH_ITERATION_CYCLES,
				RoadNetworkConstants.ENABLE_ORIENTED_SEGMENTS);
	}

	@Override
	@Pure
	public boolean isFirstPointConnectedTo(Segment1D<?, ?> segment) {
		if (segment instanceof RoadSegment) {
			final RoadConnection startPoint = getBeginPoint(StandardRoadConnection.class);
			final RoadConnection endPoint = getEndPoint(StandardRoadConnection.class);
			if (segment != this) {
				if (startPoint != null && startPoint.isConnectedSegment((RoadSegment) segment)) {
					return true;
				}
			} else if (startPoint != null && startPoint.equals(endPoint)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Pure
	public boolean isLastPointConnectedTo(Segment1D<?, ?> segment) {
		if (segment instanceof RoadSegment) {
			final RoadConnection startPoint = getBeginPoint(StandardRoadConnection.class);
			final RoadConnection endPoint = getEndPoint(StandardRoadConnection.class);
			if (segment != this) {
				if (endPoint != null && endPoint.isConnectedSegment((RoadSegment) segment)) {
					return true;
				}
			} else if (endPoint != null && startPoint.equals(endPoint)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addUserData(String id, Object data) {
		List<Object> group = null;
		if (this.userData == null) {
			this.userData = new TreeMap<>();
		} else {
			group = this.userData.get(id);
		}
		if (group == null) {
			group = new ArrayList<>();
			this.userData.put(id, group);
		}
		group.add(data);
	}

	@Override
	public void setUserData(String id, Object data) {
		List<Object> group = null;
		if (this.userData == null) {
			this.userData = new TreeMap<>();
		} else {
			group = this.userData.get(id);
		}
		if (group == null) {
			group = new ArrayList<>();
			this.userData.put(id, group);
		} else {
			group.clear();
		}
		group.add(data);
	}

	@Override
	public boolean removeUserData(String id, Object data) {
		if (this.userData != null) {
			final List<Object> group = this.userData.get(id);
			if (group != null) {
				if (group.remove(data)) {
					if (group.isEmpty()) {
						this.userData.remove(id);
					}
					if (this.userData.isEmpty()) {
						this.userData = null;
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void clearUserData(String id) {
		if (this.userData != null) {
			this.userData.remove(id);
			if (this.userData.isEmpty()) {
				this.userData = null;
			}
		}
	}

	@Override
	public void clearUserData() {
		this.userData = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Pure
	public <T> T getUserData(String id) {
		if (this.userData != null) {
			final List<Object> group = this.userData.get(id);
			if (group != null && !group.isEmpty()) {
				return (T) group.get(0);
			}
		}
		return null;
	}

	@Override
	@Pure
	@SuppressWarnings("unchecked")
	public <T> Collection<? extends T> getUserDataCollection(String id) {
		if (this.userData != null) {
			final List<Object> group = this.userData.get(id);
			if (group != null) {
				return Collections.unmodifiableCollection((Collection<? extends T>) group);
			}
		}
		return Collections.emptyList();
	}

	@Override
	@Pure
	public boolean hasUserData(String id) {
		return this.userData != null && this.userData.containsKey(id);
	}

	@Override
	@Pure
	public boolean containsUserData(String id, Object data) {
		if (this.userData != null) {
			final List<Object> group = this.userData.get(id);
			return group != null && group.contains(data);
		}
		return false;
	}

	//-------------------------------------------------
	// Lane management
	//-------------------------------------------------

	@Override
	@Pure
	public int getLaneCount() {
		return getAttribute(
				RoadNetworkConstants.getPreferredAttributeNameForLaneCount(),
				RoadNetworkConstants.getPreferredLaneCount());
	}

	@Override
	@Pure
	public double getWidth() {
		if (Double.isNaN(this.width)) {
			this.width = getAttribute(
					RoadNetworkConstants.getPreferredAttributeNameForRoadWidth(), Double.NaN);
			if (Double.isNaN(this.width)) {
				this.width = getLaneCount() * RoadNetworkConstants.getPreferredLaneWidth();
			}
		}
		return this.width;
	}

	@Override
	public void setWidth(double width) {
		final String attrName = RoadNetworkConstants.getPreferredAttributeNameForRoadWidth();
		if (width <= 0 || Double.isNaN(width)) {
			this.width = Double.NaN;
			removeAttribute(attrName);
		} else {
			this.width = width;
			setAttribute(attrName, width);
		}
	}

	@Override
	@Pure
	public final double distance(Point2D<?, ?> point) {
		return getDistance(point);
	}

	@Override
	@Pure
	public double getLaneSize(int laneIndex) {
		final int laneCount = getLaneCount();
		if (laneIndex < 0 || laneIndex >= laneCount) {
			throw new ArrayIndexOutOfBoundsException(laneIndex);
		}
		return getWidth() / laneCount;
	}

	@Override
	@Pure
	public double getLaneCenter(int laneIndex) {
		double dist = getWidth() / 2.;
		for (int i = 0; i < laneIndex; ++i) {
			dist -= getLaneSize(i);
		}
		dist -= getLaneSize(laneIndex) / 2.;

		final boolean isRightSided = getRoadNetwork().isRightSidedTrafficDirection();
		final boolean isLeftHanded = CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded();
		if (isRightSided == isLeftHanded) {
			return dist;
		}
		return -dist;
	}

	@Override
	@Pure
	public double getRoadBorderDistance() {
		final double dist = getWidth() / 2.;
		final boolean isRightSided = getRoadNetwork().isRightSidedTrafficDirection();
		final boolean isLeftHanded = CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded();
		if (isRightSided == isLeftHanded) {
			return dist;
		}
		return -dist;
	}

	@Override
	@Pure
	public Direction1D getLaneDirection(int laneIndex) {
		final int laneCount = getLaneCount();
		if (laneIndex < 0 || laneIndex >= laneCount) {
			throw new ArrayIndexOutOfBoundsException(laneIndex);
		}
		if (getRoadNetwork().isRightSidedTrafficDirection()) {
			if (laneIndex < laneCount / 2) {
				return Direction1D.SEGMENT_DIRECTION;
			}
			return Direction1D.REVERTED_DIRECTION;
		}

		if (laneIndex < laneCount / 2) {
			return Direction1D.REVERTED_DIRECTION;
		}
		return Direction1D.SEGMENT_DIRECTION;
	}

	@Override
	@Pure
	public RoadSegment getWrappedRoadSegment() {
		return this;
	}

	@Override
	@Pure
	protected final Segment1D<?, ?> toSegment1D() {
		return this;
	}

	/** Internationa point fusion validator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class Validator implements PointFusionValidator {

		private double sqDistance;

		/** Constructor.
		 */
		Validator() {
			//
		}

		/** Refresh the max distance from the user preferences.
		 */
		public void refresh() {
			final double d = RoadNetworkConstants.getPreferredRoadConnectionDistance();
			this.sqDistance = d * d;
		}

		@Override
		@Pure
		public boolean isSame(double x1, double y1, double x2, double y2) {
			return Point2D.getDistanceSquaredPointPoint(x1, y1, x2, y2) <= this.sqDistance;
		}

	} /* class Validator */

}

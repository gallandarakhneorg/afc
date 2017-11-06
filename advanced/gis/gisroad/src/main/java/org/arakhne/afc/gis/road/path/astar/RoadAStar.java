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

package org.arakhne.afc.gis.road.path.astar;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.location.GeoLocationPointList;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.path.RoadPath;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.gis.road.primitive.RoadType;
import org.arakhne.afc.gis.road.primitive.TrafficDirection;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
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
import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphPoint.GraphPointConnection;
import org.arakhne.afc.math.graph.astar.AStar;
import org.arakhne.afc.math.graph.astar.AStarHeuristic;
import org.arakhne.afc.math.graph.astar.AStarNode;

/**
 * This class provides an implementation of the
 * A* algorithm dedicated to roads.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class RoadAStar extends AStar<RoadPath, RoadSegment, RoadConnection> {

	/**
	 * Create the A* algorithm with the {@link RoadAStarDistanceHeuristic euclidian
	 * distance heuristic}.
	 */
	public RoadAStar() {
		this(null, null);
	}

	/** Constructor.
	 * @param heuristic is the heuristic that must be use by the A* algorithm.
	 */
	public RoadAStar(AStarHeuristic<? super RoadConnection> heuristic) {
		this(heuristic, null);
	}

	/**
	 * Create the A* algorithm with the {@link RoadAStarDistanceHeuristic euclidian
	 * distance heuristic}.
	 *
	 * @param pathFactory is the factory to use to create a road path.
	 */
	public RoadAStar(RoadAStarPathFactory pathFactory) {
		this(null, pathFactory);
	}

	/** Constructor.
	 * @param heuristic is the heuristic that must be use by the A* algorithm.
	 * @param pathFactory is the factory to use to create a road path.
	 */
	public RoadAStar(AStarHeuristic<? super RoadConnection> heuristic, RoadAStarPathFactory pathFactory) {
		super(
				makeHeuristic(heuristic),
				makePathFactory(pathFactory));
		setSegmentOrientationTool(new RoadAStarSegmentOrientation());
	}

	private static AStarHeuristic<? super RoadConnection> makeHeuristic(AStarHeuristic<? super RoadConnection> heuristic) {
		if (heuristic == null) {
			return new RoadAStarDistanceHeuristic();
		}
		return heuristic;
	}

	private static RoadAStarPathFactory makePathFactory(RoadAStarPathFactory pathFactory) {
		if (pathFactory == null) {
			return new RoadAStarPathFactory();
		}
		return pathFactory;
	}

	/** Run the A* algorithm from the nearest segment to
	 * <var>startPoint</var> to the nearest segment to
	 * <var>endPoint</var>.
	 *
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @param network is the road network to explore.
	 * @return the found path, or <code>null</code> if none found.
	 */
	public RoadPath solve(Point2D<?, ?> startPoint, Point2D<?, ?> endPoint, RoadNetwork network) {
		assert network != null && startPoint != null && endPoint != null;
		final RoadSegment startSegment = network.getNearestSegment(startPoint);
		if (startSegment != null) {
			final RoadSegment endSegment = network.getNearestSegment(endPoint);
			if (endSegment != null) {
				final VirtualPoint start = new VirtualPoint(startPoint, startSegment);
				final VirtualPoint end = new VirtualPoint(endPoint, endSegment);
				return solve(start, end);
			}
		}
		return null;
	}

	/** Run the A* algorithm from the nearest segment to
	 * <var>startPoint</var> to the nearest segment to
	 * <var>endPoint</var>.
	 *
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @param network is the road network to explore.
	 * @return the found path, or <code>null</code> if none found.
	 */
	public RoadPath solve(RoadConnection startPoint, Point2D<?, ?> endPoint, RoadNetwork network) {
		assert network != null && startPoint != null && endPoint != null;
		final RoadSegment endSegment = network.getNearestSegment(endPoint);
		if (endSegment != null) {
			final VirtualPoint end = new VirtualPoint(endPoint, endSegment);
			return solve(startPoint, end);
		}
		return null;
	}

	/** Run the A* algorithm from the nearest segment to
	 * <var>startPoint</var> to the nearest segment to
	 * <var>endPoint</var>.
	 *
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @param network is the road network to explore.
	 * @return the found path, or <code>null</code> if none found.
	 */
	public RoadPath solve(Point2D<?, ?> startPoint, RoadConnection endPoint, RoadNetwork network) {
		assert network != null && startPoint != null && endPoint != null;
		final RoadSegment startSegment = network.getNearestSegment(startPoint);
		if (startSegment != null) {
			final VirtualPoint start = new VirtualPoint(startPoint, startSegment);
			return solve(start, endPoint);
		}
		return null;
	}

	@Override
	protected AStarNode<RoadSegment, RoadConnection> translateCandidate(
			RoadConnection endPoint,
			AStarNode<RoadSegment, RoadConnection> node) {
		assert endPoint instanceof VirtualPoint;
		assert node != null;

		if (endPoint.equals(node.getGraphPoint())) {
			return null;
		}

		//
		// Try to build a virtual connection to the target point
		// to reach it during the A* algorithm.
		// This is required because VirtualSegment is
		// oriented from the point to the road connection.
		//

		RoadSegment seg;

		seg = endPoint.getConnectedSegment(0);
		assert seg instanceof VirtualSegment;
		assert seg.getEndPoint() != null;
		if (seg.getEndPoint().equals(node.getGraphPoint())) {
			return new VirtualCandidate((VirtualSegment) seg, node);
		}

		seg = endPoint.getConnectedSegment(1);
		assert seg instanceof VirtualSegment;
		assert seg.getEndPoint() != null;
		if (seg.getEndPoint().equals(node.getGraphPoint())) {
			return new VirtualCandidate((VirtualSegment) seg, node);
		}
		return node;
	}

	@Override
	protected boolean invalidPathSegmentFound(
			int index,
			RoadSegment segment,
			RoadPath path) {
		return segment instanceof VirtualSegment;
	}

	/** This virtual point corresponds to a virtual graph connection point
	 * that could be the start or the end of the A* search.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	static class VirtualPoint implements RoadConnection {

		private Point2D<?, ?> originalPoint;

		private RoadSegment virtualizedSegment;

		private VirtualSegment segment1;

		private VirtualSegment segment2;

		private VirtualConnection connection1;

		private VirtualConnection connection2;

		/** Constructor.
		 * @param p2d the point.
		 * @param segment the segment.
		 */
		VirtualPoint(Point2D<?, ?> p2d, RoadSegment segment) {
			this.originalPoint = p2d;

			final RoadConnection target1 = segment.getBeginPoint();
			final RoadConnection target2 = segment.getEndPoint();

			this.segment1 = new VirtualSegment(segment, this, target1);
			this.segment2 = new VirtualSegment(segment, this, target2);

			this.connection1 = new VirtualConnection(this, this.segment1);
			this.connection2 = new VirtualConnection(this, this.segment2);

			this.virtualizedSegment = segment;
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append("VP["); //$NON-NLS-1$
			buffer.append(this.virtualizedSegment.toString());
			buffer.append("]"); //$NON-NLS-1$
			return buffer.toString();
		}

		/** Replies the virtualized segment.
		 *
		 * @return the virtualized segment.
		 */
		public RoadSegment getVirtualizedSegment() {
			return this.virtualizedSegment;
		}

		@Override
		public final RoadConnection getWrappedRoadConnection() {
			return this;
		}

		@Override
		public int compareTo(GraphPoint<RoadConnection, RoadSegment> other) {
			if (other == this) {
				return 0;
			}
			if (other == null) {
				return Integer.MAX_VALUE;
			}
			if (other instanceof VirtualPoint) {
				return System.identityHashCode(this) - System.identityHashCode(other);
			}
			return -other.compareTo(this);
		}

		@Override
		public int compareTo(Point2D<?, ?> pts) {
			if (pts == null) {
				return 1;
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
		public RoadSegment getConnectedSegment(int index) throws ArrayIndexOutOfBoundsException {
			switch (index) {
			case 0:
				return this.segment1;
			case 1:
				return this.segment2;
			default:
			}
			throw new ArrayIndexOutOfBoundsException();
		}

		@Override
		public int getConnectedSegmentCount() {
			return 2;
		}

		@Override
		public Iterable<RoadSegment> getConnectedSegments() {
			return Arrays.<RoadSegment>asList(this.segment1, this.segment2);
		}

		@Override
		public Iterable<RoadSegment> getConnectedSegmentsStartingFrom(RoadSegment startingSegment) {
			return getConnectedSegments();
		}

		@Override
		public RoadSegment getOtherSideSegment(RoadSegment refSegment) {
			if (refSegment.equals(this.segment1)) {
				return this.segment2;
			}
			if (refSegment.equals(this.segment2)) {
				return this.segment1;
			}
			return null;
		}

		@Override
		public Point2d getPoint() {
			return Point2d.convert(this.originalPoint);
		}

		@Override
		public GeoLocationPoint getGeoLocation() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UUID getUUID() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isConnectedSegment(RoadSegment segment) {
			return segment.equals(this.segment1) || segment.equals(this.segment2);
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean isFinalConnectionPoint() {
			return false;
		}

		@Override
		public boolean isNearPoint(Point2D<?, ?> point) {
			return GeoLocationUtil.epsilonEqualsDistance(getPoint(), point);
		}

		@Override
		public boolean isReallyCulDeSac() {
			return false;
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType,
				CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType,
				CoordinateSystem2D system) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnections() {
			return Arrays.asList(this.connection1, this.connection2);
		}

		@Override
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFrom(
				RoadSegment startingPoint) {
			return getConnections();
		}

	} // class VirtualPoint

	/** Is a road segment that connects a virtual point to a real road connection.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	@SuppressWarnings({"checkstyle:methodcount"})
	static class VirtualSegment implements RoadSegment {

		private static final long serialVersionUID = 7395980522764768114L;

		private RoadSegment virtualizedSegment;

		private VirtualPoint startPoint;

		private RoadConnection endPoint;

		private GeoLocation geoLocation;

		/** Constructor.
		 *
		 * @param virtualizedSegment the segment to virtualize.
		 * @param startPoint the start point.
		 * @param endPoint the end point.
		 */
		VirtualSegment(RoadSegment virtualizedSegment, VirtualPoint startPoint, RoadConnection endPoint) {
			assert virtualizedSegment != null;
			assert startPoint != null;
			assert endPoint != null;
			this.virtualizedSegment = virtualizedSegment;
			this.startPoint = startPoint;
			this.endPoint = endPoint;
			this.geoLocation = new GeoLocationPointList(
					startPoint.getPoint().getX(),
					startPoint.getPoint().getY(),
					endPoint.getPoint().getX(),
					endPoint.getPoint().getY());
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append("VS["); //$NON-NLS-1$
			buffer.append(this.virtualizedSegment.toString());
			buffer.append("->"); //$NON-NLS-1$
			buffer.append(this.endPoint.toString());
			buffer.append("]"); //$NON-NLS-1$
			return buffer.toString();
		}

		/** Replies the virtualized segment.
		 *
		 * @return the virtualized segment.
		 */
		public RoadSegment getVirtualizedSegment() {
			return this.virtualizedSegment;
		}

		@Override
		public VirtualSegment clone() {
			try {
				return (VirtualSegment) super.clone();
			} catch (Throwable e) {
				throw new Error(e);
			}
		}

		@Override
		public void addUserData(String id, Object data) {
			getVirtualizedSegment().addUserData(id, data);
		}

		@Override
		public void clearUserData(String id) {
			getVirtualizedSegment().clearUserData(id);
		}

		@Override
		public void clearUserData() {
			getVirtualizedSegment().clearUserData();
		}

		@Override
		public boolean contains(Point2D<?, ?> point, double delta) {
			return getVirtualizedSegment().contains(point, delta);
		}

		@Override
		public boolean contains(Point2D<?, ?> point) {
			return getVirtualizedSegment().contains(point);
		}

		@Override
		public boolean containsUserData(String id, Object data) {
			return getVirtualizedSegment().containsUserData(id, data);
		}

		@Override
		public GraphIterator<RoadSegment, RoadConnection> depthIterator(
				double depth, double positionFromStartingPoint,
				RoadConnection startingPoint, boolean allowManyReplies,
				boolean assumeOrientedSegments) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double distance(Point2D<?, ?> point) {
			return getVirtualizedSegment().distance(point);
		}

		@Override
		public double distance(Point2D<?, ?> point, double width) {
			return getVirtualizedSegment().distance(point, width);
		}

		@Override
		public double distanceToEnd(Point2D<?, ?> point) {
			return getVirtualizedSegment().distanceToEnd(point);
		}

		@Override
		public double distanceToEnd(Point2D<?, ?> point, double width) {
			return getVirtualizedSegment().distanceToEnd(point, width);
		}

		@Override
		public Point2d getAntepenulvianPoint() {
			return getVirtualizedSegment().getAntepenulvianPoint();
		}

		@Override
		public Rectangle2d getBoundingBox() {
			return getVirtualizedSegment().getBoundingBox();
		}

		@Override
		public double getDistanceFromStart(double ratio) {
			return getVirtualizedSegment().getDistanceFromStart(ratio);
		}

		@Override
		public double getDistanceToEnd(double ratio) {
			return getVirtualizedSegment().getDistanceToEnd(ratio);
		}

		@Override
		public RoadConnection getEndPoint() {
			return this.endPoint;
		}

		@Override
		public Point2d getFirstPoint() {
			final RoadConnection r = getBeginPoint();
			if (r == null) {
				return null;
			}
			return r.getPoint();
		}

		@Override
		public GeoLocation getGeoLocation() {
			return this.geoLocation;
		}

		@Override
		public Point2d getGeoLocationForDistance(double desiredDistance) {
			return getVirtualizedSegment().getGeoLocationForDistance(desiredDistance);
		}

		@Override
		public Point2d getGeoLocationForDistance(double desiredDistance, double shifting) {
			return getVirtualizedSegment().getGeoLocationForDistance(desiredDistance, shifting);
		}

		@Override
		public Point2d getGeoLocationForDistance(double desiredDistance,
				double shifting, Vector2D<?, ?> tangent) {
			return getVirtualizedSegment().getGeoLocationForDistance(desiredDistance, shifting, tangent);
		}

		@Override
		public void getGeoLocationForDistance(double desiredDistance,
				Point2D<?, ?> geoLocation) {
			getVirtualizedSegment().getGeoLocationForDistance(desiredDistance, geoLocation);
		}

		@Override
		public void getGeoLocationForDistance(double desiredDistance,
				double shifting, Point2D<?, ?> geoLocation) {
			getVirtualizedSegment().getGeoLocationForDistance(desiredDistance,
					shifting, geoLocation);
		}

		@Override
		public void getGeoLocationForDistance(double desiredDistance,
				double shifting, Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
			getVirtualizedSegment().getGeoLocationForDistance(desiredDistance,
					shifting, geoLocation, tangent);
		}

		@Override
		public Point2d getGeoLocationForLocationRatio(double ratio) {
			return getVirtualizedSegment().getGeoLocationForLocationRatio(ratio);
		}

		@Override
		public Point2d getGeoLocationForLocationRatio(double ratio, double shifting) {
			return getVirtualizedSegment().getGeoLocationForLocationRatio(ratio, shifting);
		}

		@Override
		public Point2d getGeoLocationForLocationRatio(double ratio,
				double shifting, Vector2D<?, ?> tangent) {
			return getVirtualizedSegment().getGeoLocationForLocationRatio(ratio,
					shifting, tangent);
		}

		@Override
		public void getGeoLocationForLocationRatio(double ratio,
				Point2D<?, ?> geoLocation) {
			getVirtualizedSegment().getGeoLocationForLocationRatio(ratio, geoLocation);
		}

		@Override
		public void getGeoLocationForLocationRatio(double ratio,
				double shifting, Point2D<?, ?> geoLocation) {
			getVirtualizedSegment().getGeoLocationForLocationRatio(ratio,
					shifting, geoLocation);
		}

		@Override
		public void getGeoLocationForLocationRatio(double ratio,
				double shifting, Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
			getVirtualizedSegment().getGeoLocationForLocationRatio(ratio,
					shifting, geoLocation, tangent);
		}

		@Override
		public double getLaneCenter(int laneIndex) {
			return getVirtualizedSegment().getLaneCenter(laneIndex);
		}

		@Override
		public int getLaneCount() {
			return getVirtualizedSegment().getLaneCount();
		}

		@Override
		public Direction1D getLaneDirection(int laneIndex) {
			return getVirtualizedSegment().getLaneDirection(laneIndex);
		}

		@Override
		public double getLaneSize(int laneIndex) {
			return getVirtualizedSegment().getLaneSize(laneIndex);
		}

		@Override
		public Point2d getLastPoint() {
			final RoadConnection r = getEndPoint();
			if (r == null) {
				return null;
			}
			return r.getPoint();
		}

		@Override
		public double getLength() {
			final Point2d p1 = getFirstPoint();
			final Point2d p2 = getLastPoint();
			return p1.getDistance(p2);
		}

		@Override
		public Point1d getNearestPosition(Point2D<?, ?> pos) {
			return getVirtualizedSegment().getNearestPosition(pos);
		}

		@Override
		public RoadConnection getOtherSidePoint(RoadConnection refPoint) {
			if (refPoint.equals(getBeginPoint())) {
				return getEndPoint();
			}
			if (refPoint.equals(getEndPoint())) {
				return getBeginPoint();
			}
			return null;
		}

		@Override
		public Point2d getPointAt(int index) {
			return getVirtualizedSegment().getPointAt(index);
		}

		@Override
		public int getPointCount() {
			return getVirtualizedSegment().getPointCount();
		}

		@Override
		public RoadNetwork getRoadNetwork() {
			return getVirtualizedSegment().getRoadNetwork();
		}

		@Override
		public List<RoadSegment> getSegmentChain() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<RoadSegment> getSegmentChain(boolean forwardSearch,
				boolean backwardSearch) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RoadConnection getSharedConnectionWith(RoadSegment otherSegment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RoadConnection getBeginPoint() {
			return this.startPoint;
		}

		@Override
		public TrafficDirection getTrafficDirection() {
			return getVirtualizedSegment().getTrafficDirection();
		}

		@Override
		public RoadType getRoadType() {
			return getVirtualizedSegment().getRoadType();
		}

		@Override
		public void setRoadType(RoadType type) {
			getVirtualizedSegment().setRoadType(type);
		}

		@Override
		public <T> T getUserData(String id) {
			return getVirtualizedSegment().<T>getUserData(id);
		}

		@Override
		public <T> Collection<? extends T> getUserDataCollection(String id) {
			return getVirtualizedSegment().<T>getUserDataCollection(id);
		}

		@Override
		public double getWidth() {
			return getVirtualizedSegment().getWidth();
		}

		@Override
		public double getRoadBorderDistance() {
			return getVirtualizedSegment().getRoadBorderDistance();
		}

		@Override
		public void setWidth(double width) {
			getVirtualizedSegment().setWidth(width);
		}

		@Override
		public String getName() {
			return getVirtualizedSegment().getName();
		}

		@Override
		public void setName(String name) {
			getVirtualizedSegment().setName(name);
		}

		@Override
		public RoadSegment getWrappedRoadSegment() {
			return this;
		}

		@Override
		public boolean hasUserData(String id) {
			return getVirtualizedSegment().hasUserData(id);
		}

		@Override
		public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> bounds) {
			return getVirtualizedSegment().intersects(bounds);
		}

		@Override
		public boolean isConnectedTo(RoadSegment otherSegment) {
			return getBeginPoint().isConnectedSegment(otherSegment)
				|| getEndPoint().isConnectedSegment(otherSegment);
		}

		@Override
		public boolean isTraversableFrom(RoadConnection point) {
			if (point.equals(getBeginPoint())) {
				final RoadSegment sgmt = getVirtualizedSegment();
				final RoadConnection c1 = getEndPoint();
				final RoadConnection c2 = sgmt.getOtherSidePoint(c1);
				return sgmt.isTraversableFrom(c2);
			}
			return false;
		}

		@Override
		public GraphIterator<RoadSegment, RoadConnection> iterator(
				RoadConnection startingPoint, boolean allowManyReplies,
				boolean assumeOrientedSegments) {
			throw new UnsupportedOperationException();
		}

		@Override
		public GraphIterator<RoadSegment, RoadConnection> iterator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<Point2d> pointIterator() {
			return getVirtualizedSegment().pointIterator();
		}

		@Override
		public Iterable<Point2d> points() {
			return getVirtualizedSegment().points();
		}

		@Override
		public boolean removeUserData(String id, Object data) {
			return getVirtualizedSegment().removeUserData(id, data);
		}

		@Override
		public void setTrafficDirection(TrafficDirection direction) {
			getVirtualizedSegment().setTrafficDirection(direction);
		}

		@Override
		public void setUserData(String id, Object data) {
			getVirtualizedSegment().setUserData(id, data);
		}

		@Override
		public void addAttributeChangeListener(AttributeChangeListener listener) {
			getVirtualizedSegment().addAttributeChangeListener(listener);
		}

		@Override
		public void flush() {
			getVirtualizedSegment().flush();
		}

		@Override
		public boolean removeAllAttributes() {
			return getVirtualizedSegment().removeAllAttributes();
		}

		@Override
		public boolean removeAttribute(String name) {
			return getVirtualizedSegment().removeAttribute(name);
		}

		@Override
		public void removeAttributeChangeListener(
				AttributeChangeListener listener) {
			getVirtualizedSegment().removeAttributeChangeListener(listener);
		}

		@Override
		public boolean renameAttribute(String oldname, String newname) {
			return getVirtualizedSegment().renameAttribute(oldname, newname);
		}

		@Override
		public boolean renameAttribute(String oldname, String newname,
				boolean overwrite) {
			return getVirtualizedSegment().renameAttribute(oldname, newname, overwrite);
		}

		@Override
		public Attribute setAttribute(Attribute value)
				throws AttributeException {
			return getVirtualizedSegment().setAttribute(value);
		}

		@Override
		public Attribute setAttribute(String name, InetAddress value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, InetSocketAddress value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, Enum<?> value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, Class<?> value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, AttributeValue value)
				throws AttributeException {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, boolean value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, int value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, long value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, float value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, double value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, String value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, UUID value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, URL value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, URI value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, Date value) {
			return getVirtualizedSegment().setAttribute(name, value);
		}

		@Override
		public Iterable<Attribute> attributes() {
			return getVirtualizedSegment().attributes();
		}

		@Override
		public void freeMemory() {
			getVirtualizedSegment().freeMemory();
		}

		@Override
		public Collection<String> getAllAttributeNames() {
			return getVirtualizedSegment().getAllAttributeNames();
		}

		@Override
		public Collection<Attribute> getAllAttributes() {
			return getVirtualizedSegment().getAllAttributes();
		}

		@Override
		public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
			return getVirtualizedSegment().getAllAttributesByType();
		}

		@Override
		public AttributeValue getAttribute(String name) {
			return getVirtualizedSegment().getAttribute(name);
		}

		@Override
		public URI getAttribute(String name, URI defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public Date getAttribute(String name, Date defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public boolean getAttribute(String name, boolean defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public Class<?> getAttribute(String name, Class<?> defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public InetAddress getAttribute(String name, InetAddress defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public InetAddress getAttribute(String name,
				InetSocketAddress defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public <T extends Enum<T>> T getAttribute(String name, T defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public double getAttribute(String name, double defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public float getAttribute(String name, float defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public int getAttribute(String name, int defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public long getAttribute(String name, long defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public String getAttribute(String name, String defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public UUID getAttribute(String name, UUID defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public URL getAttribute(String name, URL defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public AttributeValue getAttribute(String name,
				AttributeValue defaultValue) {
			return getVirtualizedSegment().getAttribute(name, defaultValue);
		}

		@Override
		public boolean getAttributeAsBool(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsBool(name);
		}

		@Override
		public double getAttributeAsDouble(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsDouble(name);
		}

		@Override
		public float getAttributeAsFloat(String name) throws AttributeException {
			return getVirtualizedSegment().getAttributeAsFloat(name);
		}

		@Override
		public int getAttributeAsInt(String name) throws AttributeException {
			return getVirtualizedSegment().getAttributeAsInt(name);
		}

		@Override
		public long getAttributeAsLong(String name) throws AttributeException {
			return getVirtualizedSegment().getAttributeAsLong(name);
		}

		@Override
		public String getAttributeAsString(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsString(name);
		}

		@Override
		public UUID getAttributeAsUUID(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsUUID(name);
		}

		@Override
		public URL getAttributeAsURL(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsURL(name);
		}

		@Override
		public URI getAttributeAsURI(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsURI(name);
		}

		@Override
		public Date getAttributeAsDate(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsDate(name);
		}

		@Override
		public int getAttributeCount() {
			return getVirtualizedSegment().getAttributeCount();
		}

		@Override
		public Attribute getAttributeObject(String name) {
			return getVirtualizedSegment().getAttributeObject(name);
		}

		@Override
		public boolean hasAttribute(String name) {
			return getVirtualizedSegment().hasAttribute(name);
		}

		@Override
		public GeoId getGeoId() {
			return this.geoLocation.toGeoId();
		}

		@Override
		public UUID getUUID() {
			return getVirtualizedSegment().getUUID();
		}

		@Override
		public int getFlags() {
			return getVirtualizedSegment().getFlags();
		}

		@Override
		public boolean hasFlag(int flagIndex) {
			return getVirtualizedSegment().hasFlag(flagIndex);
		}

		@Override
		public void setFlag(int flag) {
			getVirtualizedSegment().setFlag(flag);
		}

		@Override
		public void switchFlag(int flagIndex) {
			getVirtualizedSegment().switchFlag(flagIndex);
		}

		@Override
		public void unsetFlag(int flagIndex) {
			getVirtualizedSegment().unsetFlag(flagIndex);
		}

		@Override
		public Vector2d getTangentAt(double positionOnSegment) {
			return getVirtualizedSegment().getTangentAt(positionOnSegment);
		}

		@Override
		public boolean isFirstPointConnectedTo(Segment1D<?, ?> otherSegment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isLastPointConnectedTo(Segment1D<?, ?> otherSegment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
			return getVirtualizedSegment().setAttributeType(name, type);
		}

		@Override
		public String getRoadNumber() {
			return getVirtualizedSegment().getRoadNumber();
		}

		@Override
		public void setRoadNumber(String number) {
			getVirtualizedSegment().setRoadNumber(number);
		}

		@Override
		public Enum<?> getAttributeAsEnumeration(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsEnumeration(name);
		}

		@Override
		public <T extends Enum<T>> T getAttributeAsEnumeration(String name,
				Class<T> type) throws AttributeException {
			return getVirtualizedSegment().getAttributeAsEnumeration(name, type);
		}

		@Override
		public Class<?> getAttributeAsJavaClass(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsJavaClass(name);
		}

		@Override
		public InetAddress getAttributeAsInetAddress(String name)
				throws AttributeException {
			return getVirtualizedSegment().getAttributeAsInetAddress(name);
		}

		@Override
		public boolean isEventFirable() {
			return getVirtualizedSegment().isEventFirable();
		}

		@Override
		public void setEventFirable(boolean isFirable) {
			getVirtualizedSegment().setEventFirable(isFirable);
		}

		@Override
		public void setAttributes(Map<String, Object> content) {
			getVirtualizedSegment().setAttributes(content);
		}

		@Override
		public void setAttributes(AttributeProvider content) throws AttributeException {
			getVirtualizedSegment().setAttributes(content);
		}

		@Override
		public void addAttributes(Map<String, Object> content) {
			getVirtualizedSegment().addAttributes(content);
		}

		@Override
		public void addAttributes(AttributeProvider content) throws AttributeException {
			getVirtualizedSegment().addAttributes(content);
		}

		@Override
		public void toMap(Map<String, Object> mapToFill) {
			getVirtualizedSegment().toMap(mapToFill);
		}

		@Override
		public void projectsOnPlane(double positionOnSegment, Point2D<?, ?> position, Vector2D<?, ?> tangent) {
			getVirtualizedSegment().projectsOnPlane(positionOnSegment, position, tangent);
		}

		@Override
		public void projectsOnPlane(double positionOnSegment, double shiftDistance, Point2D<?, ?> position,
				Vector2D<?, ?> tangent) {
			getVirtualizedSegment().projectsOnPlane(positionOnSegment, shiftDistance, position, tangent);
		}

		@Override
		public void toPath2D(Path2d path, double startPosition, double endPosition) {
			getVirtualizedSegment().toPath2D(path, startPosition, endPosition);
		}

	} // class VirtualSegment

	/**
	 * This is the relationship between a {@link VirtualPoint} and a {@link VirtualSegment}.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	static class VirtualConnection implements GraphPointConnection<RoadConnection, RoadSegment> {

		private VirtualPoint point;

		private VirtualSegment segment;

		/** Constructor.
		 * @param point the point.
		 * @param segment the segment.
		 */
		VirtualConnection(VirtualPoint point, VirtualSegment segment) {
			assert point != null;
			assert segment != null;
			this.point = point;
			this.segment = segment;
		}

		@Override
		public RoadConnection getGraphPoint() {
			return this.point;
		}

		@Override
		public RoadSegment getGraphSegment() {
			return this.segment;
		}

		@Override
		public boolean isSegmentStartConnected() {
			return true;
		}

	} // class VirtualConnection

	/** This is a graph point connected to a {@link VirtualPoint} that is
	 * a valid candidate for the A* algorithm.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	class VirtualCandidate implements AStarNode<RoadSegment, RoadConnection> {

		private final VirtualSegment virtualSegment;

		private final AStarNode<RoadSegment, RoadConnection> virtualizedCandidate;

		/** Constructor.
		 * @param segment a segment.
		 * @param node a A* node.
		 */
		VirtualCandidate(VirtualSegment segment, AStarNode<RoadSegment, RoadConnection> node) {
			this.virtualizedCandidate = node;
			this.virtualSegment = segment;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			return getVirtualizedCandidate().equals(obj);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return getVirtualizedCandidate().hashCode();
		}

		/** Replies the virtualized candidate.
		 *
		 * @return the virtualized candidate.
		 */
		public AStarNode<RoadSegment, RoadConnection> getVirtualizedCandidate() {
			return this.virtualizedCandidate;
		}

		@Override
		public RoadConnection getGraphPoint() {
			return getVirtualizedCandidate().getGraphPoint();
		}

		@Override
		public Iterable<RoadSegment> getGraphSegments() {
			final List<RoadSegment> list = new ArrayList<>();
			list.add(this.virtualSegment);
			for (final RoadSegment s : getVirtualizedCandidate().getGraphSegments()) {
				list.add(s);
			}
			return list;
		}

		@Override
		public RoadSegment setArrivalConnection(RoadSegment connection) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RoadSegment getArrivalConnection() {
			return getVirtualizedCandidate().getArrivalConnection();
		}

		@Override
		public double getCost() {
			return getVirtualizedCandidate().getCost();
		}

		@Override
		public double setCost(double cost) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getEstimatedCost() {
			return getVirtualizedCandidate().getEstimatedCost();
		}

		@Override
		public double setEstimatedCost(double cost) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getPathCost() {
			return getCost() + getEstimatedCost();
		}

	} // class VirtualCandidate

}

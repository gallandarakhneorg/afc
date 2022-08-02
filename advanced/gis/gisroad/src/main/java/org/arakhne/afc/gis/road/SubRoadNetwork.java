/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.road.path.RoadPath;
import org.arakhne.afc.gis.road.primitive.AbstractWrapRoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkConstants;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.gis.road.primitive.RoadSegmentContainer;
import org.arakhne.afc.gis.road.primitive.RoadType;
import org.arakhne.afc.gis.road.primitive.TrafficDirection;
import org.arakhne.afc.gis.tree.WeakGISTreeSet;
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
import org.arakhne.afc.math.graph.DynamicDepthUpdater;
import org.arakhne.afc.math.graph.GraphIterator;
import org.arakhne.afc.math.graph.GraphPoint.GraphPointConnection;
import org.arakhne.afc.math.graph.SubGraph;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class describes a road network sub graph.
 *
 * <p>This function wrap the segments and the connection points
 * to avoid to go thru the rest of the graph.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class SubRoadNetwork extends SubGraph<RoadSegment, RoadConnection, RoadPath> implements RoadSegmentContainer {

	private UUID id;

	/** Constructor.
	 */
	public SubRoadNetwork() {
		this(null);
	}

	/** Constructor.
	 * @param id the identifier of the network.
	 * @since 4.0
	 */
	public SubRoadNetwork(UUID id) {
		super(
				new WeakGISTreeSet<>(),
				0,
				RoadSegmentIterationComparator.ORIENTED_SEGMENT_SINGLETON,
				RoadSegmentIterationComparator.NOT_ORIENTED_SEGMENT_SINGLETON);
		this.id = id;
	}

	/** Replies the identifier of the network.
	 *
	 * @return the identifier.
	 */
	@Pure
	public UUID getUUID() {
		if (this.id == null) {
			this.id = UUID.randomUUID();
		}
		return this.id;
	}

	/** Replies the parent road network.
	 *
	 * @return the parent road network.
	 */
	@Pure
	protected final RoadSegmentContainer getParentRoadNetwork() {
		return (RoadSegmentContainer) getParentGraph();
	}

	@Override
	@Pure
	protected RoadSegment wrapSegment(RoadSegment segment) {
		assert segment != null;
		if (segment instanceof WrapSegment) {
			return segment;
		}

		final RoadSegment uwSegment = unwrap(segment);

		RoadConnection con;

		con = uwSegment.getBeginPoint();
		final TerminalConnection start = isTerminalPoint(con) ? new TerminalConnection(con, uwSegment, true) : null;

		con = uwSegment.getEndPoint();
		final TerminalConnection end = isTerminalPoint(con) ? new TerminalConnection(con, uwSegment, false) : null;

		return new WrapSegment(uwSegment, start, end);
	}

	@Override
	@Pure
	protected RoadConnection wrapPoint(RoadConnection point, RoadSegment segment, boolean isTerminal) {
		assert point != null;
		if (isTerminal) {
			assert segment != null;
			return new TerminalConnection(point, segment, segment.getBeginPoint().equals(point));
		}
		return point;
	}

	/** Wrap the given connection.
	 *
	 * @param point the connection to wrap.
	 * @return the wrapper.
	 * @since 16.0
	 */
	@Pure
	protected RoadConnection wrapPoint(RoadConnection point) {
		assert point != null;
		if (point instanceof AbstractWrapConnection) {
			return point;
		}
		return new WrapConnection(point.getWrappedRoadConnection());
	}

	/** Wrap the given graph point connection.
	 *
	 * @param connection the graph point connection to wrap.
	 * @return the wrapper.
	 * @since 16.0
	 */
	@Pure
	protected GraphPointConnection<RoadConnection, RoadSegment> wrapGraphPointConnection(
			GraphPointConnection<RoadConnection, RoadSegment> connection) {
		assert connection != null;
		if (connection instanceof WrapGraphPointConnection) {
			return connection;
		}
		return new WrapGraphPointConnection(connection);
	}

	/** Unwrap a connection.
	 *
	 * @param <O> the type of the object to unwrap.
	 * @param obj the object to unwrap.
	 * @return the unwrapped segment
	 */
	@SuppressWarnings({ "unchecked", "static-method" })
	@Pure
	protected final <O> O unwrap(O obj) {
		O unwrapped = obj;
		if (obj instanceof TerminalConnection) {
			unwrapped = (O) ((TerminalConnection) obj).getWrappedRoadConnection();
		} else if (obj instanceof WrapSegment) {
			unwrapped = (O) ((WrapSegment) obj).getWrappedRoadSegment();
		}
		assert unwrapped != null;
		return unwrapped;
	}

	@Override
	@Pure
	public boolean contains(RoadSegment segment) {
		return getGraphSegments().contains(segment);
	}

	@Override
	@Pure
	public RoadSegment getRoadSegment(GeoId geoId) {
		final RoadSegment original = ((WeakGISTreeSet<RoadSegment>) getGraphSegments()).get(geoId);
		if (original == null) {
			return null;
		}
		return wrapSegment(original);
	}

	@Override
	@Pure
	public RoadSegment getRoadSegment(GeoLocation location) {
		final RoadSegment original = ((WeakGISTreeSet<RoadSegment>) getGraphSegments()).get(location);
		if (original == null) {
			return null;
		}
		return wrapSegment(original);
	}

	@Override
	@Pure
	public Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return new WrapSegmentIterator(((WeakGISTreeSet<RoadSegment>) getGraphSegments()).iterator(bounds));
	}

	@Override
	@Pure
	public Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget) {
		return new WrapSegmentIterator(((WeakGISTreeSet<RoadSegment>) getGraphSegments()).iterator(bounds, budget));
	}

	@Override
	@Pure
	public Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return new WrapSegmentIterable(((WeakGISTreeSet<RoadSegment>) getGraphSegments()).toIterable(bounds));
	}

	@Override
	@Pure
	public Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget) {
		return new WrapSegmentIterable(((WeakGISTreeSet<RoadSegment>) getGraphSegments()).toIterable(bounds, budget));
	}

	@Override
	@Pure
	public boolean isLeftSidedTrafficDirection() {
		return getParentRoadNetwork().isLeftSidedTrafficDirection();
	}

	@Override
	@Pure
	public boolean isRightSidedTrafficDirection() {
		return getParentRoadNetwork().isRightSidedTrafficDirection();
	}

	/** Internal connection.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 16.0
	 */
	abstract class AbstractWrapConnection extends AbstractWrapRoadConnection {

		/** Constructor.
		 * @param connection the wrapped connection.
		 */
		AbstractWrapConnection(RoadConnection connection) {
			super(connection);
		}

		@Override
		@Pure
		public RoadSegment getOtherSideSegment(RoadSegment refSegment) {
			return wrapSegment(this.connection.get().getOtherSideSegment(unwrap(refSegment)));
		}

		@Override
		@Pure
		public String toString() {
			return "WRAPPED//" + this.connection.get().toString(); //$NON-NLS-1$
		}

	}

	/** Internal connection.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	class TerminalConnection extends AbstractWrapConnection implements GraphPointConnection<RoadConnection, RoadSegment> {

		private final SoftReference<RoadSegment> segment;

		private final boolean connectedToStart;

		/** Constructor.
		 * @param connection the wrapped connection.
		 * @param segment the segment.
		 * @param connectedToStart indicates if the segment is connected to its start.
		 */
		TerminalConnection(RoadConnection connection, RoadSegment segment, boolean connectedToStart) {
			super(connection);
			assert segment != null;
			this.segment = new SoftReference<>(unwrap(segment));
			this.connectedToStart = connectedToStart;
		}

		private RoadSegment wrapConnectedSegment() {
			final TerminalConnection start = this.connectedToStart ? this : null;
			final TerminalConnection end = this.connectedToStart ? null : this;
			return new WrapSegment(this.segment.get(), start, end);
		}

		@Override
		@Pure
		public final RoadSegment getConnectedSegment(int index)
				throws ArrayIndexOutOfBoundsException {
			return wrapSegment(this.segment.get());
		}

		@Override
		@Pure
		public final int getConnectedSegmentCount() {
			return 1;
		}

		@Override
		@Pure
		public final List<RoadSegment> getConnectedSegments() {
			return Collections.singletonList(wrapSegment(this.segment.get()));
		}

		@Override
		@Pure
		public final List<RoadSegment> getConnectedSegmentsStartingFrom(RoadSegment startingSegment) {
			final RoadSegment sgmt = this.segment.get();
			assert startingSegment != null && startingSegment.equals(sgmt);
			return Collections.singletonList(wrapSegment(sgmt));
		}

		@Override
		public Iterable<RoadSegment> getConnectedSegmentsStartingFromInReverseOrder(RoadSegment startingSegment) {
			final RoadSegment sgmt = this.segment.get();
			assert startingSegment != null && startingSegment.equals(sgmt);
			return Collections.singletonList(wrapSegment(sgmt));
		}

		@Override
		@Pure
		public final RoadSegment getOtherSideSegment(RoadSegment ref_segment) {
			return null;
		}

		@Override
		@Pure
		public final boolean isConnectedSegment(RoadSegment sgmt) {
			return this.segment.get().equals(unwrap(sgmt));
		}

		@Override
		@Pure
		public final boolean isFinalConnectionPoint() {
			return true;
		}

		@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
		private boolean isValidSegment(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			final RoadSegment sgmt = this.segment.get();
			return (sgmt.equals(unwrap(startSegment)) && boundType.includeStart()
					&& (startSegmentConnectedByItsStart == null
					|| startSegmentConnectedByItsStart.booleanValue() == this.connectedToStart))
					||
					(sgmt.equals(unwrap(endSegment)) && boundType.includeEnd()
							&& (endSegmentConnectedByItsStart == null
							|| endSegmentConnectedByItsStart.booleanValue() == this.connectedToStart));
		}

		private Iterator<RoadSegment> makeIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			return isValidSegment(startSegment, startSegmentConnectedByItsStart, endSegment,
					endSegmentConnectedByItsStart, boundType)
					? Iterators.singletonIterator(wrapConnectedSegment()) : Collections.<RoadSegment>emptyIterator();
		}

		@Override
		@Pure
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnections() {
			return Collections.singleton(this);
		}

		@Override
		@Pure
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFrom(
				RoadSegment startingPoint) {
			final RoadSegment sgmt = this.segment.get();
			if (sgmt.equals(unwrap(startingPoint))) {
				return Collections.singleton(this);
			}
			return Collections.<GraphPointConnection<RoadConnection, RoadSegment>>emptyList();
		}

		@Override
		@Pure
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFromInReverseOrder(
				RoadSegment startingPoint) {
			final RoadSegment sgmt = this.segment.get();
			if (sgmt.equals(unwrap(startingPoint))) {
				return Collections.singleton(this);
			}
			return Collections.<GraphPointConnection<RoadConnection, RoadSegment>>emptyList();
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment) {
			return makeIterator(
					startSegment, null,
					null, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType) {
			return makeIterator(
					startSegment, null,
					endSegment, null,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment) {
			return makeIterator(
					startSegment, null,
					null, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType) {
			return makeIterator(
					startSegment, null,
					null, null,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					endSegment, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				CoordinateSystem2D system) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					endSegment, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					null, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType,
				CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					null, null,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment) {
			return makeIterator(
					startSegment, null,
					endSegment, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType) {
			return makeIterator(
					startSegment, null,
					endSegment, null,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment) {
			return makeIterator(
					startSegment, null,
					null, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType) {
			return makeIterator(
					startSegment, null,
					null, null,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					endSegment, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				CoordinateSystem2D system) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					endSegment, null,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return makeIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					null, null,
					DEFAULT_CLOCKWHISE_TYPE);
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType,
				CoordinateSystem2D system) {
			return makeIterator(
					startSegment, null,
					null, null,
					boundType);
		}

		@Override
		@Pure
		public String toString() {
			return "TERMINAL//" + this.connection.get().toString(); //$NON-NLS-1$
		}

		@Override
		@Pure
		public RoadConnection getGraphPoint() {
			return this;
		}

		@Override
		@Pure
		public RoadSegment getGraphSegment() {
			return wrapConnectedSegment();
		}

		@Override
		@Pure
		public boolean isSegmentStartConnected() {
			return this.connectedToStart;
		}

	}

	/** Internal connection.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 16.0
	 */
	class WrapConnection extends AbstractWrapConnection {

		/** Constructor.
		 * @param connection the wrapped connection.
		 */
		WrapConnection(RoadConnection connection) {
			super(connection);
		}

		@Override
		@Pure
		public final RoadSegment getConnectedSegment(int index)
				throws ArrayIndexOutOfBoundsException {
			return wrapSegment(this.connection.get().getConnectedSegment(index));
		}

		@Override
		@Pure
		public final int getConnectedSegmentCount() {
			return this.connection.get().getConnectedSegmentCount();
		}

		@Override
		@Pure
		public final Iterable<RoadSegment> getConnectedSegments() {
			return Iterables.transform(this.connection.get().getConnectedSegments(), it -> wrapSegment(it));
		}

		@Override
		@Pure
		public final Iterable<RoadSegment> getConnectedSegmentsStartingFrom(RoadSegment startingSegment) {
			return Iterables.transform(this.connection.get().getConnectedSegmentsStartingFrom(startingSegment),
				it -> wrapSegment(it));
		}

		@Override
		public Iterable<RoadSegment> getConnectedSegmentsStartingFromInReverseOrder(RoadSegment startingSegment) {
			return Iterables.transform(this.connection.get().getConnectedSegmentsStartingFromInReverseOrder(startingSegment),
				it -> wrapSegment(it));
		}

		@Override
		@Pure
		public final boolean isConnectedSegment(RoadSegment sgmt) {
			return this.connection.get().isConnectedSegment(unwrap(sgmt));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(startSegment, endSegment));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, endSegment, boundType));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(startSegment));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(startSegment, boundType));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, endSegment, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, endSegment,
					boundType, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(startSegment, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toClockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType,
				CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toClockwiseIterator(
					startSegment, boundType, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(startSegment, endSegment));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, endSegment, boundType));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(startSegment));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(startSegment, boundType));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, endSegment, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, RoadSegment endSegment,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, endSegment,
					boundType, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment,
				Boolean startSegmentConnectedByItsStart,
				RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
				ClockwiseBoundType boundType, CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, startSegmentConnectedByItsStart,
					endSegment, endSegmentConnectedByItsStart,
					boundType, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(startSegment, system));
		}

		@Override
		@Pure
		public final Iterator<RoadSegment> toCounterclockwiseIterator(
				RoadSegment startSegment, ClockwiseBoundType boundType,
				CoordinateSystem2D system) {
			return new WrapSegmentIterator(this.connection.get().toCounterclockwiseIterator(
					startSegment, boundType, system));
		}

		@Override
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnections() {
			final Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> original = this.connection.get().getConnections();
			return Iterables.transform(original, it -> wrapGraphPointConnection(it));
		}

		@Override
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFrom(
				RoadSegment startingPoint) {
			final Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> original = this.connection.get()
					.getConnectionsStartingFrom(startingPoint);
			return Iterables.transform(original, it -> wrapGraphPointConnection(it));
		}

		@Override
		public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFromInReverseOrder(
				RoadSegment startingPoint) {
			final Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> original = this.connection.get()
					.getConnectionsStartingFromInReverseOrder(startingPoint);
			return Iterables.transform(original, it -> wrapGraphPointConnection(it));
		}

	}

	/** Internal graph point connection.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 16.0
	 */
	class WrapGraphPointConnection implements GraphPointConnection<RoadConnection, RoadSegment> {

		private final GraphPointConnection<RoadConnection, RoadSegment> source;

		/** Constructor.
		 *
		 * @param source the wrapped element.
		 */
		WrapGraphPointConnection(GraphPointConnection<RoadConnection, RoadSegment> source) {
			this.source = source;
		}

		@Override
		public RoadSegment getGraphSegment() {
			return wrapSegment(this.source.getGraphSegment());
		}

		@Override
		public RoadConnection getGraphPoint() {
			return wrapPoint(this.source.getGraphPoint());
		}

		@Override
		public boolean isSegmentStartConnected() {
			return this.source.isSegmentStartConnected();
		}

	}

	/** Internal segment wrapper.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	@SuppressWarnings({"checkstyle:methodcount", "checkstyle:classfanoutcomplexity"})
	class WrapSegment implements RoadSegment {

		private static final long serialVersionUID = -3612626387290684475L;

		private final SoftReference<RoadSegment> segment;

		private final TerminalConnection terminalStart;

		private final TerminalConnection terminalEnd;

		/** Constructor.
		 * @param segment the wrapped segment.
		 * @param terminalStart start point.
		 * @param terminalEnd end point.
		 */
		WrapSegment(RoadSegment segment, TerminalConnection terminalStart, TerminalConnection terminalEnd) {
			assert segment != null;
			final RoadSegment sgmt = unwrap(segment);
			assert sgmt != null;
			this.segment = new SoftReference<>(sgmt);
			this.terminalStart = terminalStart;
			this.terminalEnd = terminalEnd;
		}

		@Override
		@Pure
		public RoadSegment clone() {
			try {
				return (RoadSegment) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		@Pure
		public RoadSegment getWrappedRoadSegment() {
			return this.segment.get();
		}

		@Override
		@Pure
		public final int hashCode() {
			return this.segment.get().hashCode();
		}

		@Override
		@Pure
		public final boolean equals(Object obj) {
			if (obj instanceof WrapSegment) {
				return this.segment.get().equals(((WrapSegment) obj).getWrappedRoadSegment());
			}
			return this.segment.get().equals(obj);
		}

		/** Replies the terminal start point if it exists.
		 *
		 * @return the terminal start point or <code>null</code>
		 */
		@Pure
		public final RoadConnection getTerminalStart() {
			return this.terminalStart;
		}

		/** Replies the terminal end point if it exists.
		 *
		 * @return the terminal end point or <code>null</code>
		 */
		@Pure
		public final RoadConnection getTerminalEnd() {
			return this.terminalEnd;
		}

		@Override
		public final void addUserData(String id, Object data) {
			//
		}

		@Override
		public final void setUserData(String id, Object data) {
			//
		}

		@Override
		public final void clearUserData(String id) {
			//
		}

		@Override
		public final void clearUserData() {
			//
		}

		@Override
		@Pure
		public final boolean hasUserData(String id) {
			return false;
		}

		@Override
		@Pure
		public final boolean containsUserData(String id, Object data) {
			return false;
		}

		@Override
		@Pure
		public final boolean contains(Point2D<?, ?> point, double delta) {
			return this.segment.get().contains(point, delta);
		}

		@Override
		@Pure
		public final boolean contains(Point2D<?, ?> point) {
			return this.segment.get().contains(point);
		}

		@Override
		@Pure
		public final double distance(Point2D<?, ?> point) {
			return this.segment.get().distance(point);
		}

		@Override
		@Pure
		public final double distance(Point2D<?, ?> point, double width) {
			return this.segment.get().distance(point, width);
		}

		@Override
		@Pure
		public final double distanceToEnd(Point2D<?, ?> point) {
			return this.segment.get().distanceToEnd(point);
		}

		@Override
		@Pure
		public final double distanceToEnd(Point2D<?, ?> point, double width) {
			return this.segment.get().distanceToEnd(point, width);
		}

		@Override
		@Pure
		public final Point2d getAntepenulvianPoint() {
			return this.segment.get().getAntepenulvianPoint();
		}

		@Override
		@Pure
		public final Rectangle2d getBoundingBox() {
			return this.segment.get().getBoundingBox();
		}

		@Override
		@Pure
		public final double getDistanceFromStart(double ratio) {
			return this.segment.get().getDistanceFromStart(ratio);
		}

		@Override
		@Pure
		public final double getDistanceToEnd(double ratio) {
			return this.segment.get().getDistanceToEnd(ratio);
		}

		@Override
		@Pure
		public final Point2d getFirstPoint() {
			return this.segment.get().getFirstPoint();
		}

		@Override
		@Pure
		public final GeoLocation getGeoLocation() {
			return this.segment.get().getGeoLocation();
		}

		@Override
		@Pure
		public final Point2d getGeoLocationForDistance(double desired_distance) {
			return this.segment.get().getGeoLocationForDistance(desired_distance);
		}

		@Override
		@Pure
		public final Point2d getGeoLocationForDistance(double desired_distance,
				double shifting) {
			return this.segment.get().getGeoLocationForDistance(desired_distance, shifting);
		}

		@Override
		@Pure
		public final Point2d getGeoLocationForDistance(double desired_distance,
				double shifting, Vector2D<?, ?> tangent) {
			return this.segment.get().getGeoLocationForDistance(desired_distance, shifting, tangent);
		}

		@Override
		@Pure
		public final void getGeoLocationForDistance(double desired_distance, Point2D<?, ?> geoLocation) {
			this.segment.get().getGeoLocationForDistance(desired_distance, geoLocation);
		}

		@Override
		@Pure
		public final void getGeoLocationForDistance(double desired_distance,
				double shifting, Point2D<?, ?> geoLocation) {
			this.segment.get().getGeoLocationForDistance(desired_distance, shifting, geoLocation);
		}

		@Override
		@Pure
		public final void getGeoLocationForDistance(double desired_distance,
				double shifting, Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
			this.segment.get().getGeoLocationForDistance(desired_distance, shifting, geoLocation, tangent);
		}

		@Override
		@Pure
		public final Point2d getGeoLocationForLocationRatio(double ratio) {
			return this.segment.get().getGeoLocationForLocationRatio(ratio);
		}

		@Override
		@Pure
		public final Point2d getGeoLocationForLocationRatio(double ratio,
				double shifting) {
			return this.segment.get().getGeoLocationForLocationRatio(ratio, shifting);
		}

		@Override
		@Pure
		public final Point2d getGeoLocationForLocationRatio(double ratio,
				double shifting, Vector2D<?, ?> tangent) {
			return this.segment.get().getGeoLocationForLocationRatio(ratio, shifting, tangent);
		}

		@Override
		@Pure
		public final void getGeoLocationForLocationRatio(double ratio, Point2D<?, ?> geoLocation) {
			this.segment.get().getGeoLocationForLocationRatio(ratio, geoLocation);
		}

		@Override
		@Pure
		public final void getGeoLocationForLocationRatio(double ratio,
				double shifting, Point2D<?, ?> geoLocation) {
			this.segment.get().getGeoLocationForLocationRatio(ratio, shifting, geoLocation);
		}

		@Override
		@Pure
		public final void getGeoLocationForLocationRatio(double ratio,
				double shifting, Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
			this.segment.get().getGeoLocationForLocationRatio(ratio, shifting, geoLocation, tangent);
		}

		@Override
		@Pure
		public final Point2d getLastPoint() {
			return this.segment.get().getLastPoint();
		}

		@Override
		@Pure
		public final double getLength() {
			return this.segment.get().getLength();
		}

		@Override
		@Pure
		public final Point2d getPointAt(int index) {
			return this.segment.get().getPointAt(index);
		}

		@Override
		@Pure
		public final int getPointCount() {
			return this.segment.get().getPointCount();
		}

		@Override
		@Pure
		public final RoadNetwork getRoadNetwork() {
			return null;
		}

		@Override
		@Pure
		public final <T> T getUserData(String id) {
			return null;
		}

		@Override
		@Pure
		public final <T> Collection<? extends T> getUserDataCollection(String id) {
			return Collections.emptyList();
		}

		@Override
		@Pure
		public final double getWidth() {
			return this.segment.get().getWidth();
		}

		@Override
		public double getRoadBorderDistance() {
			return this.segment.get().getRoadBorderDistance();
		}

		@Override
		public final void setWidth(double width) {
			this.segment.get().setWidth(width);
		}

		@Override
		@Pure
		public final String getName() {
			return this.segment.get().getName();
		}

		@Override
		public final void setName(String name) {
			this.segment.get().setName(name);
		}

		@Override
		@Pure
		public final boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> shape) {
			return this.segment.get().intersects(shape);
		}

		@Override
		@Pure
		public final Iterator<Point2d> pointIterator() {
			return this.segment.get().pointIterator();
		}

		@Override
		@Pure
		public final Iterable<Point2d> points() {
			return this.segment.get().points();
		}

		@Override
		public final boolean removeUserData(String id, Object data) {
			return false;
		}

		@Override
		@Pure
		public final GeoId getGeoId() {
			return this.segment.get().getGeoId();
		}

		@Override
		@Pure
		public final boolean isConnectedTo(RoadSegment otherSegment) {
			return this.segment.get().isConnectedTo(unwrap(otherSegment));
		}

		@Override
		@Pure
		public boolean isTraversableFrom(RoadConnection point) {
			return this.segment.get().isTraversableFrom(unwrap(point));
		}

		@Override
		@Pure
		public TrafficDirection getTrafficDirection() {
			return this.segment.get().getTrafficDirection();
		}

		@Override
		public void setTrafficDirection(TrafficDirection direction) {
			this.segment.get().setTrafficDirection(direction);
		}

		@Override
		@Pure
		public final RoadConnection getBeginPoint() {
			final RoadConnection terminated = getTerminalStart();
			return terminated != null ? terminated : wrapPoint(this.segment.get().getBeginPoint());
		}

		@Override
		@Pure
		public final RoadConnection getEndPoint() {
			final RoadConnection terminated = getTerminalEnd();
			return terminated != null ? terminated : wrapPoint(this.segment.get().getEndPoint());
		}

		@Override
		@Pure
		public final RoadConnection getOtherSidePoint(RoadConnection ref_point) {
			final RoadConnection tS = getBeginPoint();
			final RoadConnection tE = getEndPoint();
			if (tS.equals(unwrap(ref_point))) {
				return tE;
			}
			return tS;
		}

		@Override
		@Pure
		public final RoadConnection getSharedConnectionWith(RoadSegment otherSegment) {
			final RoadSegment sgmt = this.segment.get();
			final RoadSegment usgmt = unwrap(otherSegment);
			if (sgmt.isFirstPointConnectedTo(usgmt)) {
				return getBeginPoint();
			}
			if (sgmt.isLastPointConnectedTo(usgmt)) {
				return getEndPoint();
			}
			return null;
		}

		@Override
		@Pure
		public final boolean isFirstPointConnectedTo(Segment1D<?, ?> arg0) {
			final RoadSegment sgmt = this.segment.get();
			final Segment1D<?, ?> usgmt = unwrap(arg0);
			return sgmt.isFirstPointConnectedTo(usgmt);
		}

		@Override
		@Pure
		public final boolean isLastPointConnectedTo(Segment1D<?, ?> arg0) {
			final RoadSegment sgmt = this.segment.get();
			final Segment1D<?, ?> usgmt = unwrap(arg0);
			return sgmt.isLastPointConnectedTo(usgmt);
		}

		@Override
		@Pure
		public final List<RoadSegment> getSegmentChain() {
			return getSegmentChain(true, true);
		}

		@Override
		@Pure
		public final List<RoadSegment> getSegmentChain(boolean forward_search,
				boolean backward_search) {
			final List<RoadSegment> chain = new ArrayList<>();

			chain.add(this);

			// Search for segments in the backward direction (starting at the first point)
			if (backward_search) {
				RoadConnection currPoint = getBeginPoint();
				RoadSegment currSegment = currPoint.getOtherSideSegment(this);

				while (currSegment != null) {
					chain.add(0, wrapSegment(currSegment));
					currPoint = currSegment.getOtherSidePoint(currPoint);
					currSegment = currPoint.getOtherSideSegment(currSegment);
				}
			}

			// Search for segments in the forward direction (starting at the last point)
			if (forward_search) {
				RoadConnection currPoint = getEndPoint();
				RoadSegment currSegment = currPoint.getOtherSideSegment(this);

				while (currSegment != null) {
					chain.add(chain.size(), wrapSegment(currSegment));
					currPoint = currSegment.getOtherSidePoint(currPoint);
					currSegment = currPoint.getOtherSideSegment(currSegment);
				}
			}

			final RoadPath path = new RoadPath();

			path.addAll(chain);

			return path;
		}

		@Override
		@Pure
		public String toString() {
			final RoadSegment sgmt = this.segment.get();
			assert sgmt != null;
			return sgmt.toString();
		}

		@Override
		@Pure
		public double getLaneCenter(int laneIndex) {
			return this.segment.get().getLaneCenter(laneIndex);
		}

		@Override
		@Pure
		public int getLaneCount() {
			return this.segment.get().getLaneCount();
		}

		@Override
		@Pure
		public Direction1D getLaneDirection(int laneIndex) {
			return this.segment.get().getLaneDirection(laneIndex);
		}

		@Override
		@Pure
		public double getLaneSize(int laneIndex) {
			return this.segment.get().getLaneSize(laneIndex);
		}

		@Override
		@Pure
		public Vector2d getTangentAt(double positionOnSegment) {
			return this.segment.get().getTangentAt(positionOnSegment);
		}

		@Override
		@Pure
		public Point1d getNearestPosition(Point2D<?, ?> pos, double lateralDistance) {
			return this.segment.get().getNearestPosition(pos, lateralDistance);
		}

		@Override
		@Pure
		public void projectsOnPlane(double positionOnSegment, double shiftDistance, Point2D<?, ?> position,
				Vector2D<?, ?> tangent) {
			this.segment.get().projectsOnPlane(positionOnSegment, shiftDistance, position, tangent);
		}

		@Override
		@Pure
		public void projectsOnPlane(double positionOnSegment, Point2D<?, ?> position, Vector2D<?, ?> tangent) {
			this.segment.get().projectsOnPlane(positionOnSegment, position, tangent);
		}

		@Override
		@Pure
		public GraphIterator<RoadSegment, RoadConnection> depthIterator(
				double depth, double position_from_starting_point,
				RoadConnection starting_point, boolean allowManyReplies,
				boolean assumeOrientedSegments,
				DynamicDepthUpdater<RoadSegment, RoadConnection> dynamicDepthUpdater) {
			return new DistanceBasedRoadNetworkIterator(
					SubRoadNetwork.this,
					depth, position_from_starting_point,
					this, starting_point,
					allowManyReplies, assumeOrientedSegments, dynamicDepthUpdater);
		}

		@Override
		@Pure
		public GraphIterator<RoadSegment, RoadConnection> iterator(
				RoadConnection starting_point, boolean allowManyReplies,
				boolean assumeOrientedSegments) {
			return new RoadNetworkIterator(
					SubRoadNetwork.this,
					this, starting_point,
					allowManyReplies, assumeOrientedSegments,
					0.);
		}

		@Override
		@Pure
		public GraphIterator<RoadSegment, RoadConnection> iterator() {
			return iterator(getBeginPoint(),
					RoadNetworkConstants.ENABLE_GRAPH_ITERATION_CYCLES,
					RoadNetworkConstants.ENABLE_ORIENTED_SEGMENTS);
		}

		@Override
		@Pure
		public UUID getUUID() {
			return this.segment.get().getUUID();
		}

		@Override
		public void addAttributeChangeListener(AttributeChangeListener listener) {
			this.segment.get().addAttributeChangeListener(listener);
		}

		@Override
		public void flush() {
			this.segment.get().flush();
		}

		@Override
		public boolean removeAllAttributes() {
			return this.segment.get().removeAllAttributes();
		}

		@Override
		public boolean removeAttribute(String name) {
			return this.segment.get().removeAttribute(name);
		}

		@Override
		public void removeAttributeChangeListener(AttributeChangeListener listener) {
			this.segment.get().removeAttributeChangeListener(listener);
		}

		@Override
		public boolean renameAttribute(String oldname, String newname) {
			return this.segment.get().renameAttribute(oldname, newname);
		}

		@Override
		public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
			return this.segment.get().renameAttribute(oldname, newname, overwrite);
		}

		@Override
		public Attribute setAttribute(Attribute value) throws AttributeException {
			return this.segment.get().setAttribute(value);
		}

		@Override
		public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, boolean value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, int value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, long value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, float value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, InetAddress value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, InetSocketAddress value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, Enum<?> value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, Class<?> value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, double value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, String value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, UUID value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, URL value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, URI value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		public Attribute setAttribute(String name, Date value) {
			return this.segment.get().setAttribute(name, value);
		}

		@Override
		@Pure
		public Iterable<Attribute> attributes() {
			return this.segment.get().attributes();
		}

		@Override
		public void freeMemory() {
			this.segment.get().freeMemory();
		}

		@Override
		@Pure
		public Collection<String> getAllAttributeNames() {
			return this.segment.get().getAllAttributeNames();
		}

		@Override
		@Pure
		public Collection<Attribute> getAllAttributes() {
			return this.segment.get().getAllAttributes();
		}

		@Override
		@Pure
		public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
			return this.segment.get().getAllAttributesByType();
		}

		@Override
		@Pure
		public AttributeValue getAttribute(String name) {
			return this.segment.get().getAttribute(name);
		}

		@Override
		@Pure
		public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public boolean getAttribute(String name, boolean defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public double getAttribute(String name, double defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public float getAttribute(String name, float defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public int getAttribute(String name, int defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public long getAttribute(String name, long defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public String getAttribute(String name, String defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public InetAddress getAttribute(String name, InetAddress defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public InetAddress getAttribute(String name, InetSocketAddress defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public <T extends Enum<T>> T getAttribute(String name, T defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public Class<?> getAttribute(String name, Class<?> defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public UUID getAttribute(String name, UUID defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public URL getAttribute(String name, URL defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public URI getAttribute(String name, URI defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public Date getAttribute(String name, Date defaultValue) {
			return this.segment.get().getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public boolean getAttributeAsBool(String name) throws AttributeException {
			return this.segment.get().getAttributeAsBool(name);
		}

		@Override
		@Pure
		public double getAttributeAsDouble(String name) throws AttributeException {
			return this.segment.get().getAttributeAsDouble(name);
		}

		@Override
		@Pure
		public float getAttributeAsFloat(String name) throws AttributeException {
			return this.segment.get().getAttributeAsFloat(name);
		}

		@Override
		@Pure
		public int getAttributeAsInt(String name) throws AttributeException {
			return this.segment.get().getAttributeAsInt(name);
		}

		@Override
		@Pure
		public long getAttributeAsLong(String name) throws AttributeException {
			return this.segment.get().getAttributeAsLong(name);
		}

		@Override
		@Pure
		public String getAttributeAsString(String name) throws AttributeException {
			return this.segment.get().getAttributeAsString(name);
		}

		@Override
		@Pure
		public UUID getAttributeAsUUID(String name) throws AttributeException {
			return this.segment.get().getAttributeAsUUID(name);
		}

		@Override
		@Pure
		public URL getAttributeAsURL(String name) throws AttributeException {
			return this.segment.get().getAttributeAsURL(name);
		}

		@Override
		@Pure
		public URI getAttributeAsURI(String name) throws AttributeException {
			return this.segment.get().getAttributeAsURI(name);
		}

		@Override
		@Pure
		public Date getAttributeAsDate(String name) throws AttributeException {
			return this.segment.get().getAttributeAsDate(name);
		}

		@Override
		@Pure
		public int getAttributeCount() {
			return this.segment.get().getAttributeCount();
		}

		@Override
		@Pure
		public Attribute getAttributeObject(String name) {
			return this.segment.get().getAttributeObject(name);
		}

		@Override
		@Pure
		public boolean hasAttribute(String name) {
			return this.segment.get().hasAttribute(name);
		}

		@Override
		@Pure
		public int getFlags() {
			return this.segment.get().getFlags();
		}

		@Override
		@Pure
		public boolean hasFlag(int flagIndex) {
			return this.segment.get().hasFlag(flagIndex);
		}

		@Override
		public void setFlag(int flag) {
			this.segment.get().setFlag(flag);
		}

		@Override
		public void switchFlag(int flagIndex) {
			this.segment.get().switchFlag(flagIndex);
		}

		@Override
		public void unsetFlag(int flagIndex) {
			this.segment.get().unsetFlag(flagIndex);
		}

		@Override
		@Pure
		public RoadType getRoadType() {
			return this.segment.get().getRoadType();
		}

		@Override
		public void setRoadType(RoadType type) {
			this.segment.get().setRoadType(type);
		}

		@Override
		public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
			return this.segment.get().setAttributeType(name, type);
		}

		@Override
		@Pure
		public String getRoadNumber() {
			return this.segment.get().getRoadNumber();
		}

		@Override
		public void setRoadNumber(String number) {
			this.segment.get().setRoadNumber(number);
		}

		@Override
		@Pure
		public Enum<?> getAttributeAsEnumeration(String name) throws AttributeException {
			return this.segment.get().getAttributeAsEnumeration(name);
		}

		@Override
		@Pure
		public <T extends Enum<T>> T getAttributeAsEnumeration(String name,
				Class<T> type) throws AttributeException {
			return this.segment.get().getAttributeAsEnumeration(name, type);
		}

		@Override
		@Pure
		public Class<?> getAttributeAsJavaClass(String name) throws AttributeException {
			return this.segment.get().getAttributeAsJavaClass(name);
		}

		@Override
		@Pure
		public InetAddress getAttributeAsInetAddress(String name)
				throws AttributeException {
			return this.segment.get().getAttributeAsInetAddress(name);
		}

		@Override
		@Pure
		public boolean isEventFirable() {
			return this.segment.get().isEventFirable();
		}

		@Override
		public void setEventFirable(boolean isFirable) {
			this.segment.get().setEventFirable(isFirable);
		}

		@Override
		public void setAttributes(Map<String, Object> content) {
			this.segment.get().setAttributes(content);
		}

		@Override
		public void setAttributes(AttributeProvider content) throws AttributeException {
			this.segment.get().setAttributes(content);
		}

		@Override
		public void addAttributes(Map<String, Object> content) {
			this.segment.get().addAttributes(content);
		}

		@Override
		public void addAttributes(AttributeProvider content) throws AttributeException {
			this.segment.get().addAttributes(content);
		}

		@Override
		@Pure
		public void toMap(Map<String, Object> mapToFill) {
			this.segment.get().toMap(mapToFill);
		}

		@Override
		public void toPath2D(Path2d path, double startPosition, double endPosition) {
			this.segment.get().toPath2D(path, startPosition, endPosition);
		}

		@Override
		public void toJson(JsonBuffer buffer) {
			this.segment.get().toJson(buffer);
		}

	} // class WrapSegment

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	class WrapSegmentIterator implements Iterator<RoadSegment> {

		private final Iterator<RoadSegment> iterator;

		/** Constructor.
		 * @param iter the original iterator.
		 */
		WrapSegmentIterator(Iterator<RoadSegment> iter) {
			this.iterator = iter;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public RoadSegment next() {
			return wrapSegment(this.iterator.next());
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

	}

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	class WrapSegmentIterable implements Iterable<RoadSegment> {

		private final Iterable<RoadSegment> iterable;

		/** Constructor.
		 * @param iter the original iterator.
		 */
		WrapSegmentIterable(Iterable<RoadSegment> iter) {
			this.iterable = iter;
		}

		@Override
		@Pure
		public Iterator<RoadSegment> iterator() {
			return new WrapSegmentIterator(this.iterable.iterator());
		}

	}

}

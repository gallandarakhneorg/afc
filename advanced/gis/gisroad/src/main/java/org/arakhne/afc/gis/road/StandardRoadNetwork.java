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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.GISPolylineSet;
import org.arakhne.afc.gis.grid.MapPolylineGridSet;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationArea;
import org.arakhne.afc.gis.mapelement.MapElementConstants;
import org.arakhne.afc.gis.primitive.AbstractBoundedGISElement;
import org.arakhne.afc.gis.primitive.GISContainer;
import org.arakhne.afc.gis.primitive.GISEditable;
import org.arakhne.afc.gis.primitive.GISEditableChangeListener;
import org.arakhne.afc.gis.road.primitive.LegalTrafficSide;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkConstants;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadNetworkListener;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.gis.road.primitive.UnexpectedRoadNetworkException;
import org.arakhne.afc.gis.road.primitive.UnsupportedRoadConnectionException;
import org.arakhne.afc.gis.road.primitive.UnsupportedRoadSegmentException;
import org.arakhne.afc.gis.tree.GISTreeSet;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.math.graph.GraphIterator;
import org.arakhne.afc.math.graph.GraphPoint.GraphPointConnection;
import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.util.OutputParameter;

/**
 * This class describes a road network.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class StandardRoadNetwork extends AbstractBoundedGISElement<GISContainer<?>, StandardRoadNetwork> implements RoadNetwork {

	private static final long serialVersionUID = 4183798734164630125L;

	private final EventHandler eventHandler = new EventHandler();

	/** List of road segments.
	 */
	//private MapPolylineTreeSet<RoadPolyline> roadSegments;
	private  GISPolylineSet<RoadPolyline> roadSegments;

	/** Number of road connections in the network.
	 */
	private int connectionCount;

	/** Event Listeners.
	 */
	private Collection<RoadNetworkListener> listeners;

	/** Constructor.
	 * @param originalBounds are the bounds of the road network.
	 * @param attributeProvider is the attribute collection associated to this network.
	 */
	public StandardRoadNetwork(Rectangle2afp<?, ?, ?, ?, ?, ?> originalBounds, AttributeCollection attributeProvider) {
		this(null, originalBounds, attributeProvider);
	}

	/** Constructor.
	 * @param originalBounds are the bounds of the road network.
	 */
	public StandardRoadNetwork(Rectangle2afp<?, ?, ?, ?, ?, ?> originalBounds) {
		this(null, originalBounds);
	}

	/** Constructor.
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param originalBounds are the bounds of the road network.
	 * @param attributeProvider is the attribute collection associated to this network.
	 * @since 4.0
	 */
	public StandardRoadNetwork(UUID id, Rectangle2afp<?, ?, ?, ?, ?, ?> originalBounds, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		this.roadSegments = createInternalDataStructure(originalBounds);
		initAttributes();
	}

	/** Constructor.
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param originalBounds are the bounds of the road network.
	 * @since 4.0
	 */
	public StandardRoadNetwork(UUID id, Rectangle2afp<?, ?, ?, ?, ?, ?> originalBounds) {
		super(id, null);
		this.roadSegments = createInternalDataStructure(originalBounds);
		initAttributes();
	}

	/** Create the internal data structure.
	 *
	 * @param originalBounds are the bounds of the road network.
	 * @return the internal data structure.
	 */
	@SuppressWarnings({"static-method", "checkstyle:magicnumber"})
	protected GISPolylineSet<RoadPolyline> createInternalDataStructure(Rectangle2afp<?, ?, ?, ?, ?, ?> originalBounds) {
		return new MapPolylineGridSet<>(100, 100,
				originalBounds.getMinX(),
				originalBounds.getMinY(),
				originalBounds.getWidth(),
				originalBounds.getHeight());
	}

	private void initAttributes() {
		setAttribute("LEGAL_TRAFFIC_SIDE", RoadNetworkConstants.getPreferredLegalTrafficSide().name()); //$NON-NLS-1$
	}

	/** Clone this object to obtain a valid copy.
	 *
	 * @return a copy
	 */
	@Override
	@Pure
	public StandardRoadNetwork clone() {
		final StandardRoadNetwork clone = super.clone();

		clone.listeners = null;

		if (this.roadSegments != null) {
			clone.roadSegments = createInternalDataStructure(getBoundingBox());

			for (final RoadPolyline segment : this.roadSegments) {
				try {
					clone.addRoadSegment(segment.clone());
				} catch (RoadNetworkException e) {
					//
				}
			}
		} else {
			clone.roadSegments = null;
		}

		return clone;
	}

	/** Replies the internal data-structure as set.
	 *
	 * @return the internal data-structure as set.
	 */
	@Pure
	public GISPolylineSet<RoadPolyline> getInternalSet() {
		return this.roadSegments;
	}

	/** Replies the internal data-structure as tree.
	 *
	 * @return the internal data-structure as tree.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public Tree<RoadPolyline, ?> getInternalTree() {
		if (this.roadSegments instanceof GISTreeSet<?, ?>) {
			return ((GISTreeSet<RoadPolyline, ?>) this.roadSegments).getTree();
		}
		return null;
	}

	/** Set the unique identifier for element.
	 *
	 * <p>A Unique IDentifier (UID) must be unique for all the object instances.
	 *
	 * @param id is the new identifier
	 * @since 4.0
	 */
	@Override
	public final void setUUID(UUID id) {
		super.setUUID(id);
	}

	//-------------------------------------------------
	// Graph interface
	//-------------------------------------------------

	/** Replies the legal traffic side.
	 *
	 * <p>When left-side circulation direction rule is used, it is supposed that all
	 * vehicles are going on the left side of the roads. For example,
	 * this rule is used in UK.
	 *
	 * @return the legal traffic side
	 */
	@Pure
	public LegalTrafficSide getLegalTrafficSide() {
		final String side;
		try {
			side = getAttributeAsString("LEGAL_TRAFFIC_SIDE"); //$NON-NLS-1$
			LegalTrafficSide sd;
			try {
				sd = LegalTrafficSide.valueOf(side);
			} catch (Throwable exception) {
				sd = null;
			}
			if (sd != null) {
				return sd;
			}
		} catch (Throwable exception) {
			//
		}
		return RoadNetworkConstants.getPreferredLegalTrafficSide();
	}

	@Override
	@Pure
	@Inline(value = "getLegalTrafficSide() == $1.LEFT", imported = LegalTrafficSide.class)
	public boolean isLeftSidedTrafficDirection() {
		return getLegalTrafficSide() == LegalTrafficSide.LEFT;
	}

	@Override
	@Pure
	@Inline(value = "getLegalTrafficSide() == $1.RIGHT", imported = LegalTrafficSide.class)
	public boolean isRightSidedTrafficDirection() {
		return getLegalTrafficSide() == LegalTrafficSide.RIGHT;
	}

	@Override
	@Pure
	public int getSegmentCount() {
		return this.roadSegments.size();
	}

	@Override
	@Pure
	public int getPointCount() {
		return this.connectionCount;
	}

	@Override
	@Pure
	public boolean contains(Object obj) {
		return this.roadSegments.contains(obj);
	}

	@Override
	@Pure
	public final boolean contains(RoadSegment segment) {
		return this.roadSegments.contains(segment);
	}

	@Override
	@Pure
	public boolean isEmpty() {
		return this.roadSegments.isEmpty();
	}

	//-------------------------------------------------
	// Getter functions
	//-------------------------------------------------

	/** Compute the bounds of this element.
	 * This function does not update the internal
	 * attribute replied by {@link #getBoundingBox()}
	 */
	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		final Rectangle2d rect = new Rectangle2d();
		Rectangle2afp<?, ?, ?, ?, ?, ?> rs;
		for (final RoadSegment segment : getRoadSegments()) {
			rs = segment.getBoundingBox();
			if (rs != null && !rs.isEmpty()) {
				rect.setUnion(rs);
			}
		}
		return rect.isEmpty() ? null : rect;
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	@Pure
	public GeoLocation getGeoLocation() {
		return new GeoLocationArea(getBoundingBox());
	}

	@Override
	@Pure
	public Collection<? extends RoadSegment> getRoadSegments() {
		return Collections.unmodifiableCollection(this.roadSegments);
	}

	@Override
	@Pure
	public final RoadSegment getRoadSegment(GeoId geoId) {
		return this.roadSegments.get(geoId);
	}

	@Override
	@Pure
	public final RoadSegment getRoadSegment(GeoLocation location) {
		return this.roadSegments.get(location);
	}

	@Override
	@Pure
	public Collection<RoadConnection> getConnections(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return getConnections(bounds, this.roadSegments.iterator(bounds));
	}

	@Override
	@Pure
	public Collection<RoadConnection> getConnections(Rectangle2d bounds) {
		return getConnections(
				new Rectangle2d(
						bounds.getMinX(), bounds.getMinY(),
						bounds.getWidth(), bounds.getHeight()),
				this.roadSegments.iterator(bounds));
	}

	private static Collection<RoadConnection> getConnections(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds,
			Iterator<RoadPolyline> iterator) {
		assert bounds != null;
		final Collection<RoadConnection> connections = new TreeSet<>();
		RoadPolyline road;
		RoadConnection connection;
		while (iterator.hasNext()) {
			road = iterator.next();
			connection = road.getBeginPoint(StandardRoadConnection.class);
			if (bounds.contains(connection.getPoint())) {
				connections.add(connection);
			}
			connection = road.getEndPoint(StandardRoadConnection.class);
			if (bounds.contains(connection.getPoint())) {
				connections.add(connection);
			}
		}
		return connections;
	}

	@Override
	@Pure
	public RoadConnection getNearestConnection(Point2D<?, ?> pos) {
		final RoadPolyline nearestSegment = this.roadSegments.getNearestEnd(pos);
		if (nearestSegment == null) {
			return null;
		}
		return nearestSegment.getNearestPoint(RoadConnection.class, pos.getX(), pos.getY());
	}

	@Override
	@Pure
	public RoadSegment getNearestSegment(Point2D<?, ?> pos) {
		return this.roadSegments.getNearest(pos);
	}

	@Override
	@Pure
	public Pair<? extends RoadSegment, Double> getNearestSegmentData(Point2D<?, ?> pos) {
		return this.roadSegments.getNearestData(pos);
	}

	@Override
	@Pure
	public Point1d getNearestPosition(Point2D<?, ?> pos) {
		final RoadSegment segment = this.roadSegments.getNearest(pos);
		if (segment == null) {
			return null;
		}
		return segment.getNearestPosition(pos);
	}

	//-------------------------------------------------
	// Updating functions
	//-------------------------------------------------

	@Override
	public final void addRoadSegment(RoadSegment segment) throws RoadNetworkException {
		if (segment instanceof RoadPolyline) {
			addRoadPolyline((RoadPolyline) segment);
		} else {
			throw new UnsupportedRoadSegmentException();
		}
	}

	/** Add a road segment inside the road network.
	 *
	 * @param segment is the road segment to insert
	 * @return <code>true</code> if segment successfully added, otherwise <code>false</code>
	 * @throws RoadNetworkException in case of error.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public final boolean addRoadPolyline(RoadPolyline segment) throws RoadNetworkException {
		// Check element validity
		assert segment != null;
		final RoadNetwork currentNetwork = segment.getRoadNetwork();
		if (currentNetwork == this) {
			return false;
		}
		if (currentNetwork != null) {
			throw new RoadNetworkException("segment is already in a road network"); //$NON-NLS-1$;
		}
		if (segment.getPointCount() <= 1) {
			throw new RoadNetworkException("segment has not two points"); //$NON-NLS-1$;
		}

		// Get segment information
		StandardRoadConnection startConnection = null;
		StandardRoadConnection endConnection = null;
		final Point2d firstPoint;
		final Point2d lastPoint;
		try {
			firstPoint = segment.getFirstPoint();
			if (firstPoint == null) {
				throw new RoadNetworkException("segment has not a first point"); //$NON-NLS-1$
			}
		} catch (IndexOutOfBoundsException exception) {
			throw new RoadNetworkException("segment has not a first point"); //$NON-NLS-1$
		}
		try {
			lastPoint = segment.getLastPoint();
			if (lastPoint == null) {
				throw new RoadNetworkException("segment has not a last point"); //$NON-NLS-1$
			}
		} catch (IndexOutOfBoundsException exception) {
			throw new RoadNetworkException("segment has not a last point"); //$NON-NLS-1$
		}

		final OutputParameter<RoadPolyline> firstNeighbour = new OutputParameter<>();
		final OutputParameter<RoadPolyline> secondNeighbour = new OutputParameter<>();

		if (this.roadSegments.add(
				segment,
				RoadNetworkConstants.getPreferredRoadConnectionDistance(),
				firstNeighbour, secondNeighbour)) {
			RoadPolyline seg = firstNeighbour.get();
			if (seg != null) {
				startConnection = seg.getNearestPoint(StandardRoadConnection.class, firstPoint.getX(), firstPoint.getY());
			}
			seg = secondNeighbour.get();
			if (seg != null) {
				endConnection = seg.getNearestPoint(StandardRoadConnection.class, lastPoint.getX(), lastPoint.getY());
			}

			int nbNewConn = 0;

			if (startConnection == null && endConnection == null
					&& firstPoint.epsilonEquals(lastPoint, MapElementConstants.POINT_FUSION_DISTANCE)) {
				startConnection = new StandardRoadConnection();
				endConnection = startConnection;
				nbNewConn += 2;
			}
			if (startConnection == null) {
				startConnection = new StandardRoadConnection();
				++nbNewConn;
			}
			if (endConnection == null) {
				endConnection = new StandardRoadConnection();
				++nbNewConn;
			}

			try {
				segment.setRoadNetwork(this);
				segment.setStartPoint(startConnection);
				segment.setEndPoint(endConnection);
				this.connectionCount += nbNewConn;
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				this.roadSegments.remove(segment);
				throw new RuntimeException(e);
			}

			Rectangle2d bb = getBoundingBox();
			if (bb == null) {
				bb = segment.getBoundingBox();
			} else {
				bb = bb.createUnion(segment.getBoundingBox());
			}
			setBoundingBox(bb);

			segment.addGISEditableChangeListener(this.eventHandler);

			fireSegmentAdded(segment);

			return true;
		}

		return false;
	}

	@Override
	public boolean removeRoadSegment(RoadSegment segment) {
		if (segment == null) {
			return false;
		}
		if (this.roadSegments.remove(segment)) {
			if (segment instanceof RoadPolyline) {
				final RoadPolyline pl = (RoadPolyline) segment;
				pl.removeGISEditableChangeListener(this.eventHandler);
				pl.setStartPoint(null);
				pl.setEndPoint(null);
				pl.setRoadNetwork(null);
			}

			resetBoundingBox();

			fireSegmentRemoved(segment);

			return true;
		}
		return false;
	}

	@Override
	public boolean clear() {
		boolean changed = false;
		if (!this.roadSegments.isEmpty()) {
			final Iterator<RoadPolyline> iterator = this.roadSegments.iterator();
			RoadPolyline road;
			while (iterator.hasNext()) {
				road = iterator.next();
				road.removeGISEditableChangeListener(this.eventHandler);
				road.setStartPoint(null);
				road.setEndPoint(null);
				road.setRoadNetwork(null);
				changed = true;
				resetBoundingBox();
				fireSegmentRemoved(road);
			}
		}
		if (changed) {
			resetBoundingBox();
		}
		return changed;
	}

	@Override
	public final RoadConnection mergeRoadConnections(RoadConnection... connections) {
		return mergeRoadConnections(Arrays.asList(connections));
	}

	@Override
	@SuppressWarnings({"checkstyle:npathcomplexity"})
	public RoadConnection mergeRoadConnections(Collection<? extends RoadConnection> connections) {
		if (connections == null || connections.isEmpty()) {
			return null;
		}
		if (connections.size() == 1) {
			return connections.iterator().next();
		}
		final Point2d baryCenter = new Point2d();
		for (final RoadConnection con : connections) {
			final Point2D<?, ?> pts = con.getPoint();
			baryCenter.add(pts.getX(), pts.getY());
		}
		baryCenter.scale(1. / connections.size());

		final StandardRoadConnection newConnection = new StandardRoadConnection();
		newConnection.setPosition(baryCenter);

		// Use a list to avoid concurrent modification exception
		final List<Pair<RoadPolyline, Boolean>> list = new ArrayList<>();

		RoadSegment sgmt;
		RoadPolyline road;
		for (final RoadConnection con : connections) {
			for (final GraphPointConnection<RoadConnection, RoadSegment> c : con.getConnections()) {
				sgmt = c.getGraphSegment();
				if (this != sgmt.getRoadNetwork()) {
					throw new UnexpectedRoadNetworkException();
				}
				if (sgmt instanceof RoadPolyline) {
					road = (RoadPolyline) sgmt;
					list.add(new Pair<>(road, c.isSegmentStartConnected()));
				}
			}
		}

		for (final Pair<RoadPolyline, Boolean> pair : list) {
			road = pair.getKey();
			if (pair.getValue().booleanValue()) {
				road.setStartPoint(newConnection);
			} else {
				road.setEndPoint(newConnection);
			}
		}

		resetBoundingBox();

		for (final Pair<RoadPolyline, Boolean> pair : list) {
			fireSegmentChanged(pair.getKey());
		}

		return newConnection;
	}

	@Override
	public RoadConnection connectSegmentStartPoint(
			RoadConnection connection,
			RoadSegment segment,
			Point2D<?, ?> position) {
		assert segment != null;

		final RoadSegment sgmt = segment.getWrappedRoadSegment();
		if (!(sgmt instanceof RoadPolyline)) {
			throw new UnsupportedRoadSegmentException();
		}
		if (this != sgmt.getRoadNetwork()) {
			throw new UnexpectedRoadNetworkException();
		}

		final StandardRoadConnection theConnection;
		if (connection == null) {
			theConnection = new StandardRoadConnection();
			if (position != null) {
				theConnection.setPosition(position);
			}
		} else {
			final RoadConnection c = connection.getWrappedRoadConnection();
			if (c instanceof StandardRoadConnection) {
				theConnection = (StandardRoadConnection) c;
			} else {
				throw new UnsupportedRoadConnectionException();
			}
		}

		final RoadPolyline road = (RoadPolyline) sgmt;
		road.setStartPoint(theConnection);

		resetBoundingBox();

		fireSegmentChanged(road);

		return theConnection;
	}

	@Override
	public RoadConnection connectSegmentEndPoint(
			RoadConnection connection,
			RoadSegment segment,
			Point2D<?, ?> position) {
		assert segment != null;

		final RoadSegment sgmt = segment.getWrappedRoadSegment();
		if (!(sgmt instanceof RoadPolyline)) {
			throw new UnsupportedRoadSegmentException();
		}
		if (this != sgmt.getRoadNetwork()) {
			throw new UnexpectedRoadNetworkException();
		}

		final StandardRoadConnection theConnection;
		if (connection == null) {
			theConnection = new StandardRoadConnection();
			if (position != null) {
				theConnection.setPosition(position);
			}
		} else {
			final RoadConnection c = connection.getWrappedRoadConnection();
			if (c instanceof StandardRoadConnection) {
				theConnection = (StandardRoadConnection) c;
			} else {
				throw new UnsupportedRoadConnectionException();
			}
		}

		final RoadPolyline road = (RoadPolyline) sgmt;
		road.setEndPoint(theConnection);

		resetBoundingBox();

		fireSegmentChanged(road);

		return theConnection;
	}

	@Override
	public void addRoadNetworkListener(RoadNetworkListener listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<>();
		}
		this.listeners.add(listener);
	}

	@Override
	public void removeRoadNetworkListener(RoadNetworkListener listener) {
		if (this.listeners != null) {
			this.listeners.remove(listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	/** Fire the addition event.
	 *
	 * @param segment is the added segment
	 */
	protected void fireSegmentAdded(RoadSegment segment) {
		if (this.listeners != null && isEventFirable()) {
			for (final RoadNetworkListener listener : this.listeners) {
				listener.onRoadSegmentAdded(this, segment);
			}
		}
	}

	/** Fire the change event.
	 *
	 * @param segment is the changed segment
	 */
	protected void fireSegmentChanged(RoadSegment segment) {
		if (this.listeners != null && isEventFirable()) {
			for (final RoadNetworkListener listener : this.listeners) {
				listener.onRoadSegmentChanged(this, segment);
			}
		}
	}

	/** Fire the addition event.
	 *
	 * @param segment is the removed segment
	 */
	protected void fireSegmentRemoved(RoadSegment segment) {
		if (this.listeners != null && isEventFirable()) {
			for (final RoadNetworkListener listener : this.listeners) {
				listener.onRoadSegmentRemoved(this, segment);
			}
		}
	}

	//-------------------------------------------------
	// Iterator functions
	//-------------------------------------------------

	@Override
	@Pure
	public GraphIterator<RoadSegment, RoadConnection> depthIterator(
			RoadSegment startingSegment, double depth,
			double position_from_starting_point, RoadConnection startingPoint,
			boolean allowManyReplies, boolean assumeOrientedSegments) {
		return startingSegment.depthIterator(
				depth, position_from_starting_point, startingPoint,
				allowManyReplies, assumeOrientedSegments);
	}

	@Override
	@Pure
	public GraphIterator<RoadSegment, RoadConnection> iterator(
			RoadSegment starting_segment, RoadConnection starting_point,
			boolean allowManyReplies, boolean assumeOrientedSegments) {
		return starting_segment.iterator(starting_point, allowManyReplies, assumeOrientedSegments);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public Iterator<RoadSegment> iterator() {
		return (Iterator<RoadSegment>) (Object) this.roadSegments.iterator();
	}

	@Override
	@Pure
	public Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return this.roadSegments.iterator(bounds);
	}

	@Override
	@Pure
	public Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget) {
		return this.roadSegments.iterator(bounds, budget);
	}

	@Override
	@Pure
	public Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return this.roadSegments.toIterable(bounds);
	}

	@Override
	@Pure
	public Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget) {
		return this.roadSegments.toIterable(bounds, budget);
	}

	@Override
	@Pure
	public Iterator<Rectangle2afp<?, ?, ?, ?, ?, ?>> boundsIterator() {
		return this.roadSegments.boundsIterator();
	}

	/** Default evant handler for the road network.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public class EventHandler implements GISEditableChangeListener {

		/** Constructor.
		 */
		public EventHandler() {
			//
		}

		@Override
		public void editableGISElementHasChanged(GISEditable changedElement) {
			if (changedElement instanceof RoadSegment) {
				resetBoundingBox();
			}
		}

	}

}

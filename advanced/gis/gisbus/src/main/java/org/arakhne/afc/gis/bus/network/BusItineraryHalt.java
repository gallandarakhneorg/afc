/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.gis.bus.network;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A bus halt is where a bus is able to halt its course.
 * It is associated to a bus stop and an itinerary. It is situated on a
 * road segment.
 * <h3>Validation</h3>
 * An bus halt on an itinerary could be invalid if one of the following critera is not true:
 * <ol>
 * <li>no itinerary is associated to the bus halt,</li>
 * <li>no curviline position is given to the bus halt,</li>
 * <li>the bus halt on the itinerary is not associated to a {@link BusStop bus stop}.</li>
 * </ol>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusItineraryHalt extends AbstractBusPrimitive<BusItinerary> {

	/** Distance between the road border and the bus halt.
	 */
	public static final double DISTANCE_BETWEEN_HALT_AND_ROAD_BORDER = 1.;

	private static final long serialVersionUID = 5282365930067534990L;

	private Point1d bufferPosition1D;

	/** Index of the road segment on which the bus halt lies.
	 */
	private int roadSegmentIndex;

	/** Is the position of the bus halt on the road segment from the segment's start.
	 */
	private double curvilineDistance;

	/** Type of this bus halt.
	 */
	private BusItineraryHaltType type = BusItineraryHaltType.STOP_ON_DEMAND;

	/** Real bus halt.
	 */
	private WeakReference<BusStop> busStop;

	/** This index is used to provide a unique id to the inserted bus halts.
	 * This identifier is used to put the halts in the sorted list of
	 * invalid halts.
	 */
	private int invalidListIndex = Integer.MAX_VALUE;

	//------------------------------------------------------------------
	// Constructors
	//------------------------------------------------------------------

	/** Create a halt with attributes stored in memory.
	 *
	 * @param itinerary is the itinerary on which thus bus halt was located.
	 * @param type is the type of the bus halt.
	 * @param name is the name of the new bus halt
	 */
	BusItineraryHalt(BusItinerary itinerary, String name, BusItineraryHaltType type) {
		this((UUID) null, itinerary, name, type, new HeapAttributeCollection());
	}

	/** Create a halt with attributes stored in memory.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param itinerary is the itinerary on which thus bus halt was located.
	 * @param type is the type of the bus halt.
	 * @param name is the name of the new bus halt
	 * @since 2.0
	 */
	BusItineraryHalt(UUID id, BusItinerary itinerary, String name, BusItineraryHaltType type) {
		this(id, itinerary, name, type, new HeapAttributeCollection());
	}

	/** Create a bus halt.
	 *
	 * @param itinerary is the itinerary on which thus bus halt was located.
	 * @param name is the name of the new bus halt
	 * @param type is the type of the bus halt.
	 * @param attributeProvider is the provider of attributes used by this bus halt.
	 */
	BusItineraryHalt(BusItinerary itinerary, String name, BusItineraryHaltType type, AttributeCollection attributeProvider) {
		this((UUID) null, itinerary, name, type, attributeProvider);
	}

	/** Create a bus halt.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param itinerary is the itinerary on which thus bus halt was located.
	 * @param name is the name of the new bus halt
	 * @param type is the type of the bus halt.
	 * @param attributeProvider is the provider of attributes used by this bus halt.
	 * @since 2.0
	 */
	BusItineraryHalt(UUID id, BusItinerary itinerary, String name, BusItineraryHaltType type,
			AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		assert itinerary != null;
		assert type != null;

		// Set the name of the element
		setName(name);

		// Set the type
		this.type = type;

		// Set the position
		this.roadSegmentIndex = -1;
		this.curvilineDistance = Float.NaN;

		// Link this halt to its container
		setContainer(itinerary);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		if (getBusStop() != null) {
			buffer.add("stop", getBusStop().getUUID()); //$NON-NLS-1$
		}
		if (getRoadSegment() != null) {
			buffer.add("road", getRoadSegment().getUUID()); //$NON-NLS-1$
		}
		if (getPosition1D() != null) {
			buffer.add("position", getPosition1D().getCurvilineCoordinate()); //$NON-NLS-1$
		}
		buffer.add("type", getType()); //$NON-NLS-1$
	}

	/** Replies a bus halt name that was not exist in the specified
	 * bus itinerary.
	 *
	 * @param busItinerary is the bus itinerary for which a free bus halt name may be computed.
	 * @return a name suitable for bus halt.
	 */
	@Pure
	public static String getFirstFreeBushaltName(BusItinerary busItinerary) {
		if (busItinerary == null) {
			return null;
		}
		int nb = busItinerary.size();
		String name;
		do {
			++nb;
			name = Locale.getString("NAME_TEMPLATE", Integer.toString(nb)); //$NON-NLS-1$
		}
		while (busItinerary.getBusHalt(name) != null);
		return name;
	}

	@Override
	public void rebuild(boolean fireEvents) {
		resetBoundingBox();
		clearPositionBuffers();
		if (fireEvents) {
			setEventFirable(true);
		}
		checkPrimitiveValidity();
		if (!fireEvents) {
			setEventFirable(true);
		}
	}

	//------------------------------------------------------------------
	// Validity management
	//------------------------------------------------------------------

	/** Replies the unique id in the sorted list of
	 * invalid halts.
	 *
	 * @return the id.
	 */
	@Pure
	public int getInvalidListIndex() {
		return this.invalidListIndex;
	}

	/** Set the unique id in the sorted list of
	 * invalid halts.
	 *
	 * @param id the index.
	 */
	void setInvalidListIndex(int id) {
		this.invalidListIndex = id;
	}

	@Override
	protected void checkPrimitiveValidity() {
		final BusItinerary itinerary = getContainer();
		BusPrimitiveInvalidity invalidityReason = null;
		if (itinerary == null) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.HALT_NOT_IN_ITINERARY,
					null);
		} else {
			assert itinerary != null;
			final BusStop stop = getBusStop();
			if (stop == null) {
				invalidityReason = new BusPrimitiveInvalidity(
						BusPrimitiveInvalidityType.NO_STOP_IN_HALT,
						null);
			} else if (!stop.isValidPrimitive()) {
				invalidityReason = new BusPrimitiveInvalidity(
						BusPrimitiveInvalidityType.INVALID_LINKED_STOP,
						stop.getName());
			} else if (this.roadSegmentIndex < 0 || this.roadSegmentIndex >= itinerary.getRoadSegmentCount()) {
				invalidityReason = new BusPrimitiveInvalidity(
						BusPrimitiveInvalidityType.HALT_NOT_ON_ROAD_SEGMENT,
						null);
			} else if (Double.isNaN(this.curvilineDistance)) {
				invalidityReason = new BusPrimitiveInvalidity(
						BusPrimitiveInvalidityType.INVALID_CURVILINE_POSITION,
						Double.toString(this.curvilineDistance));
			} else {
				assert stop != null;
				final BusNetwork bn = stop.getBusNetwork();
				if (bn == null || bn != getBusNetwork()) {
					invalidityReason = new BusPrimitiveInvalidity(
							BusPrimitiveInvalidityType.NOT_IN_SAME_NETWORK,
							null);
				}
			}
		}
		setPrimitiveValidity(invalidityReason);
	}

	@Override
	protected void fireValidityChangedFor(Object changedObject, int index, BusPrimitiveInvalidity oldReason,
			BusPrimitiveInvalidity newReason) {
		clearPositionBuffers();
		super.fireValidityChangedFor(changedObject, index, oldReason, newReason);
	}

	//------------------------------------------------------------------
	// Getter/Setter
	//------------------------------------------------------------------

	@Override
	@Pure
	public BusNetwork getBusNetwork() {
		final BusItinerary itinerary = getContainer();
		if (itinerary != null) {
			return itinerary.getBusNetwork();
		}
		return null;
	}

	/** Replies the bus stop associated to this halt.
	 *
	 * @return the bus halt associated to this halt,
	 *     or {@code null} if this halt is not associated to
	 *     a bus halt.
	 */
	@Pure
	public BusStop getBusStop() {
		return this.busStop == null ? null : this.busStop.get();
	}

	/** Set the bus stop associated to this halt.
	 *
	 * @param busStop is the bus stop associated to this halt,
	 *     or {@code null} if this halt is not associated to
	 *     a bus stop.
	 * @return {@code true} if the bus stop was correctly set,
	 *     otherwise {@code false}.
	 */
	public boolean setBusStop(BusStop busStop) {
		final BusStop old = getBusStop();
		if ((busStop == null && old != null)
			|| (busStop != null && !busStop.equals(old))) {
			if (old != null) {
				old.removeBusHalt(this);
			}
			this.busStop = busStop == null ? null : new WeakReference<>(busStop);
			if (busStop != null) {
				busStop.addBusHalt(this);
			}
			clearPositionBuffers();
			fireShapeChanged();
			checkPrimitiveValidity();
			return true;
		}
		return false;
	}

	/** Set the name of the element.
	 *
	 * @param name the name.
	 */
	@Override
	public void setName(String name) {
		if (name == null) {
			super.setName(getFirstFreeBushaltName(getContainer()));
		} else {
			super.setName(name);
		}
	}

	/**
	 * Clear internal buffers related to the bus halt position.
	 */
	protected void clearPositionBuffers() {
		this.bufferPosition1D = null;
	}

	/** Replies the position of the bus halt on the road.
	 * The replied position may differ from the
	 * position of the associated bus stop because the
	 * position on the road is a projection of the stop's position
	 * on the road.
	 *
	 * @return the 2D position or {@code null} if
	 *     the halt is not associated to a road segment.
	 */
	@Pure
	public Point2d getPosition2D() {
		final Point1d p1d5 = getPosition1D();
		if (p1d5 != null) {
			final Point2d pos = new Point2d();
			p1d5.getSegment().projectsOnPlane(
					p1d5.getCurvilineCoordinate(),
					p1d5.getLateralDistance(),
					pos,
					null);
			return pos;
		}
		return null;
	}

	/** Replies the position of the bus halt on the road.
	 * The replied position may differ from the
	 * position of the associated bus stop because the
	 * position on the road is a projection of the stop's position
	 * on the road.
	 *
	 * @return the 1.5D position or {@code null} if
	 *     the halt is not associated to a road segment.
	 */
	@Pure
	public Point1d getPosition1D() {
		if (this.bufferPosition1D == null) {
			final RoadSegment segment = getRoadSegment();
			if (segment != null && !Double.isNaN(this.curvilineDistance)) {
				final RoadNetwork network = segment.getRoadNetwork();
				assert network != null;

				double lateral = segment.getRoadBorderDistance();
				if (lateral < 0.) {
					lateral -= DISTANCE_BETWEEN_HALT_AND_ROAD_BORDER;
				} else {
					lateral += DISTANCE_BETWEEN_HALT_AND_ROAD_BORDER;
				}

				final boolean isSegmentDirection = getRoadSegmentDirection().isSegmentDirection();
				if (!isSegmentDirection) {
					lateral = -lateral;
				}

				this.bufferPosition1D = new Point1d(segment, this.curvilineDistance, lateral);
			}
		}
		return this.bufferPosition1D;
	}

	/**
	 * Replies the road segment on which this bus halt was.
	 *
	 * @return the road segment
	 */
	@Pure
	public RoadSegment getRoadSegment() {
		final BusItinerary itinerary = getContainer();
		if (itinerary != null && this.roadSegmentIndex >= 0 && this.roadSegmentIndex < itinerary.getRoadSegmentCount()) {
			return itinerary.getRoadSegmentAt(this.roadSegmentIndex);
		}
		return null;
	}

	/**
	 * Replies the index of the road segment on which this bus halt was.
	 *
	 * @return the index of the road segment in the itinerary.
	 */
	@Pure
	public int getRoadSegmentIndex() {
		return this.roadSegmentIndex;
	}

	/**
	 * Set the index of the road segment on which this bus halt was.
	 *
	 * @param idx is the index of the road segment
	 */
	void setRoadSegmentIndex(int idx) {
		if (this.roadSegmentIndex != idx) {
			this.roadSegmentIndex = idx;
			clearPositionBuffers();
		}
	}

	/**
	 * Replies the position on the segment.
	 *
	 * @return the curviline distance on the segment from
	 *     its first point.
	 */
	@Pure
	public double getPositionOnSegment() {
		return this.curvilineDistance;
	}

	/**
	 * Set the position on the segment. The position is
	 * the curviline distance on the segment.
	 *
	 * <p>This function does not check the validity of the halt.
	 *
	 * @param position is the position on the segment.
	 */
	void setPositionOnSegment(double position) {
		if (this.curvilineDistance != position) {
			this.curvilineDistance = position;
			clearPositionBuffers();
		}
	}

	/**
	 * Replies on which direction the bus halt is located on the road segment.
	 *
	 * @return {@link Direction1D#SEGMENT_DIRECTION} or {@link Direction1D#REVERTED_DIRECTION}.
	 */
	@Pure
	public Direction1D getRoadSegmentDirection() {
		final BusItinerary itinerary = getContainer();
		if (itinerary != null && this.roadSegmentIndex >= 0 && this.roadSegmentIndex < itinerary.getRoadSegmentCount()) {
			return itinerary.getRoadSegmentDirection(this.roadSegmentIndex);
		}
		return null;
	}

	/**
	 * Replies the type of the bus halt.
	 *
	 * @return the type of the bus.
	 */
	@Pure
	public BusItineraryHaltType getType() {
		return this.type;
	}

	/**
	 * Set the type of the bus halt.
	 *
	 * @param type the type of the bus.
	 */
	public void setType(BusItineraryHaltType type) {
		if (type != null && type != this.type) {
			this.type = type;
			firePrimitiveChanged();
		}
	}

	/**
	 * Replies if this bus halt is a terminus (aka. first or last bus halt)
	 * of an itinerary.
	 *
	 * @return {@code true} if the bus halt is a terminus,
	 *     otherwise {@code false}
	 */
	@Pure
	public boolean isTerminus() {
		return isStartingBusHalt() || isEndingBusHalt();
	}

	@Override
	@Pure
	public int indexInParent() {
		final BusItinerary itinerary = getContainer();
		if (itinerary == null) {
			return -1;
		}
		return itinerary.indexOf(this);
	}

	/**
	 * Replies if this bus halt is a starting bus halt (aka. first bus halt)
	 * of an itinerary.
	 *
	 * @return {@code true} if the bus halt is a starting bus halt,
	 *     otherwise {@code false}
	 */
	@Pure
	public boolean isStartingBusHalt() {
		final BusItinerary itinerary = getContainer();
		if (itinerary != null && itinerary.isValidPrimitive()) {
			try {
				return itinerary.getBusHaltAt(0) == this;
			} catch (IndexOutOfBoundsException exception) {
				// invalid halt
			}
		}
		return false;
	}

	/**
	 * Replies if this bus halt is a ending bus halt (aka. last bus halt)
	 * of an itinerary.
	 *
	 * @return {@code true} if the bus halt is a ending bus halt,
	 *     otherwise {@code false}
	 */
	@Pure
	public boolean isEndingBusHalt() {
		final BusItinerary itinerary = getContainer();
		if (itinerary != null && itinerary.isValidPrimitive()) {
			final int count = itinerary.getValidBusHaltCount();
			if (count > 0) {
				try {
					return itinerary.getBusHaltAt(count - 1) == this;
				} catch (IndexOutOfBoundsException exception) {
					// invalid halt
				}
			}
		}
		return false;
	}

	//------------------------------------------------------------------
	// Bus Hub API
	//------------------------------------------------------------------

	/** Replies if this bus halt is inside a hub throught an associated bus stop.
	 *
	 * @return {@code true} if this bus stop is inside a hub,
	 *     otherwise {@code false}
	 */
	@Pure
	public boolean insideBusHub() {
		final BusStop busStop = getBusStop();
		if (busStop != null) {
			return busStop.insideBusHub();
		}
		return false;
	}

	/** Replies the hubs in which this bus halt is located in
	 * throught an associated bus stop.
	 *
	 * @return the hubs
	 */
	@Pure
	public Iterable<BusHub> busHubs() {
		final BusStop busStop = getBusStop();
		if (busStop != null) {
			return busStop.busHubs();
		}
		return Collections.emptyList();
	}

	/** Replies the hubs in which this bus stop is located in
	 * throught an associated bus stop.
	 *
	 * @return the hubs
	 */
	@Pure
	public Iterator<BusHub> busHubIterator() {
		final BusStop busStop = getBusStop();
		if (busStop != null) {
			return busStop.busHubIterator();
		}
		return Collections.emptyIterator();
	}

	//------------------------------------------------------------------
	// GIS API
	//------------------------------------------------------------------

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		final GeoLocationPoint p = getGeoPosition();
		if (p == null) {
			return null;
		}
		return new Rectangle2d(p.getX(), p.getY(), 0, 0);
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	@Pure
	public final GeoLocation getGeoLocation() {
		final GeoLocationPoint pt = getGeoPosition();
		if (pt == null) {
			return new GeoLocationNowhere(getUUID());
		}
		return pt;
	}

	/** Replies the geo position.
	 *
	 * @return the geo position.
	 */
	@Pure
	public GeoLocationPoint getGeoPosition() {
		final Point2d p = getPosition2D();
		if (p != null) {
			return new GeoLocationPoint(p.getX(), p.getY());
		}
		return null;
	}

	/** Replies the distance between the position of this bus itinerary halt
	 * and the position of the bus stop associated to this bus itinerary
	 * halt.
	 *
	 * @return the distance between the position of this bus itinerary halt
	 *     and the position of the bus stop associated to this bus itinerary
	 *     halt.
	 * @since 2.0
	 */
	@Pure
	public double distanceToBusStop() {
		final Point2d  p1 = getPosition2D();
		if (p1 != null) {
			final BusStop stop = getBusStop();
			if (stop != null) {
				final Point2d p2 = stop.getPosition2D();
				if (p2 != null) {
					return p1.getDistance(p2);
				}
			}
		}
		return Double.NaN;
	}

	/** Replies the distance between the given point and this bus halt.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the distance to this bus halt
	 */
	@Pure
	public double distance(double x, double y) {
		final GeoLocationPoint p = getGeoPosition();
		if (p != null) {
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), x, y);
		}
		return Double.NaN;
	}

	/** Replies the distance between the given point and this bus halt.
	 *
	 * @param point the point
	 * @return the distance to this bus halt
	 */
	@Pure
	public double distance(Point2D<?, ?> point) {
		assert point != null;
		final GeoLocationPoint p = getGeoPosition();
		if (p != null) {
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), point.getX(), point.getY());
		}
		return Double.NaN;
	}

	/** Replies the distance between the given bus halt and this bus halt.
	 *
	 * @param halt the halt.
	 * @return the distance to this bus halt
	 */
	@Pure
	public double distance(BusItineraryHalt halt) {
		assert halt != null;
		final GeoLocationPoint p = getGeoPosition();
		final GeoLocationPoint p2 = halt.getGeoPosition();
		if (p != null && p2 != null) {
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), p2.getX(), p2.getY());
		}
		return Double.NaN;
	}

	/** Replies the distance between the given bus stop and this bus halt.
	 *
	 * @param busStop the stop.
	 * @return the distance to this bus halt
	 */
	@Pure
	public double distance(BusStop busStop) {
		assert busStop != null;
		final GeoLocationPoint p = getGeoPosition();
		final GeoLocationPoint p2 = busStop.getGeoPosition();
		if (p != null && p2 != null) {
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), p2.getX(), p2.getY());
		}
		return Double.NaN;
	}

	/** Replies the distance between the given point and this bus halt.
	 *
	 * @param point the point
	 * @return the distance to this bus halt
	 */
	@Pure
	public double distance(GeoLocationPoint point) {
		assert point != null;
		final GeoLocationPoint p = getGeoPosition();
		if (p != null) {
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), point.getX(), point.getY());
		}
		return Double.NaN;
	}

	//------------------------------------------------------------------
	// Types
	//------------------------------------------------------------------

	/** Type of bus halt.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public enum BusItineraryHaltType {
		/** Stop everytimes.
		 */
		SYSTEMATIC_STOP,

		/** Stop on demand.
		 */
		STOP_ON_DEMAND;
	}

}

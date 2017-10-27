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

package org.arakhne.afc.gis.bus.network;

import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import com.google.common.collect.Iterators;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.references.WeakArrayList;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A bus stop is a designated place where buses stop for passengers to board or
 * leave a bus. These are normally positioned on the highway and are distinct
 * from off-highway facilities such as bus hubs. The construction of
 * bus stops tends to reflect the level of usage. Stops at busy locations
 * may have shelters, seating and possibly electronic passenger information
 * systems; less busy stops may use a simple pole and flag to mark the
 * location and 'customary stops' have no specific infrastructure being
 * known by their description. Bus stops may be clustered together
 * into transport hubs allowing interchange between routes from nearby stops
 * and with other public transport modes.
 * <br>
 * Source: <a href="http://en.wikipedia.org/wiki/Bus_stop">WikiPedia</a>
 * <h3>Validation</h3>
 * A bus stop is valid only if the bus stop has a position.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusStop extends AbstractBusPrimitive<BusNetwork> {

	private static final long serialVersionUID = -4841464826170663992L;

	/** Geo-location of this bus stop.
	 */
	private GeoLocationPoint position;

	/** Hubs where this busstop is located.
	 */
	private WeakArrayList<BusHub> hubs;

	/** Bus initinerary halts associated to this busstop.
	 */
	private WeakArrayList<BusItineraryHalt> halts;

	//------------------------------------------------------------------
	// Constructors
	//------------------------------------------------------------------

	/** Create a stop with attributes stored in memory.
	 *
	 * @param name is the name of the new bus stop
	 */
	public BusStop(String name) {
		this((UUID) null, name, null, new HeapAttributeCollection());
	}

	/** Create a stop with attributes stored in memory.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus stop
	 */
	public BusStop(UUID id, String name) {
		this(id, name, null, new HeapAttributeCollection());
	}

	/** Create a bus stop.
	 *
	 * @param name is the name of the new bus stop
	 * @param attributeProvider is the provider of attributes used by this bus stop.
	 */
	public BusStop(String name, AttributeCollection attributeProvider) {
		this((UUID) null, name, null, attributeProvider);
	}

	/** Create a bus stop.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus stop
	 * @param attributeProvider is the provider of attributes used by this bus stop.
	 */
	public BusStop(UUID id, String name, AttributeCollection attributeProvider) {
		this(id, name, null, attributeProvider);
	}

	/** Create a stop with attributes stored in memory.
	 *
	 * @param name is the name of the new bus stop
	 * @param position is the position of the bus stop.
	 */
	public BusStop(String name, GeoLocationPoint position) {
		this((UUID) null, name, position, new HeapAttributeCollection());
	}

	/** Create a stop with attributes stored in memory.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus stop
	 * @param position is the position of the bus stop.
	 */
	public BusStop(UUID id, String name, GeoLocationPoint position) {
		this(id, name, position, new HeapAttributeCollection());
	}

	/** Create a bus stop.
	 *
	 * @param name is the name of the new bus stop
	 * @param position is the position of the bus stop.
	 * @param attributeProvider is the provider of attributes used by this bus stop.
	 */
	public BusStop(String name, GeoLocationPoint position, AttributeCollection attributeProvider) {
		this((UUID) null, name, position, attributeProvider);
	}

	/** Create a bus stop.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus stop.
	 * @param position is the position of the bus stop.
	 * @param attributeProvider is the provider of attributes used by this bus stop.
	 */
	public BusStop(UUID id, String name, GeoLocationPoint position, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		// Set the name of the element
		setName(name);
		// Set the position of the element
		setPosition(position);
	}

	/** Replies a bus stop name that was not exist in the specified
	 * bus network.
	 *
	 * @param busNetwork is the bus network for which a free bus stop name may be computed.
	 * @return a name suitable for bus stop.
	 */
	@Pure
	public static String getFirstFreeBusStopName(BusNetwork busNetwork) {
		if (busNetwork == null) {
			return null;
		}
		int nb = busNetwork.getBusStopCount();
		String name;
		do {
			++nb;
			name = Locale.getString("NAME_TEMPLATE", Integer.toString(nb)); //$NON-NLS-1$
		}
		while (busNetwork.getBusStop(name) != null);
		return name;
	}

	//------------------------------------------------------------------
	// Validity management
	//------------------------------------------------------------------

	@Override
	protected void checkPrimitiveValidity() {
		BusPrimitiveInvalidity invalidityReason = null;
		if (this.position == null) {
			invalidityReason =
					new BusPrimitiveInvalidity(
							BusPrimitiveInvalidityType.NO_STOP_POSITION,
							null);
		} else {
			final BusNetwork busNetwork = getBusNetwork();
			if (busNetwork == null) {
				invalidityReason = new BusPrimitiveInvalidity(
						BusPrimitiveInvalidityType.STOP_NOT_IN_NETWORK,
						null);
			} else {
				final RoadNetwork roadNetwork = busNetwork.getRoadNetwork();
				if (roadNetwork == null) {
					invalidityReason = new BusPrimitiveInvalidity(
							BusPrimitiveInvalidityType.STOP_NOT_IN_NETWORK,
							null);
				} else {
					final Rectangle2d bounds = roadNetwork.getBoundingBox();
					if (bounds == null || !bounds.contains(this.position.getPoint())) {
						invalidityReason = new BusPrimitiveInvalidity(
								BusPrimitiveInvalidityType.OUTSIDE_MAP_BOUNDS,
								bounds == null ? null : bounds.toString());
					}
				}
			}
		}
		setPrimitiveValidity(invalidityReason);
	}

	/** Notifies any dependent object about a validation change from
	 * this bus stop.
	 */
	private void notifyDependencies() {
		if (getContainer() != null) {
			for (final BusHub hub : busHubs()) {
				hub.checkPrimitiveValidity();
			}
			for (final BusItineraryHalt halt : getBindedBusHalts()) {
				halt.checkPrimitiveValidity();
			}
		}
	}

	@Override
	protected void fireValidityChangedFor(Object changedObject, int index, BusPrimitiveInvalidity oldReason,
			BusPrimitiveInvalidity newReason) {
		super.fireValidityChangedFor(changedObject, index, oldReason, newReason);
		notifyDependencies();
	}

	//------------------------------------------------------------------
	// Getter/Setter
	//------------------------------------------------------------------

	@Override
	public boolean setContainer(BusNetwork container) {
		if (super.setContainer(container)) {
			notifyDependencies();
			return true;
		}
		return false;
	}

	@Override
	@Pure
	public BusNetwork getBusNetwork() {
		return getContainer();
	}

	/** Set the name of the element.
	 *
	 * @param name the name.
	 */
	@Override
	public void setName(String name) {
		if (name == null) {
			super.setName(getFirstFreeBusStopName(getContainer()));
		} else {
			super.setName(name);
		}
	}

	/** Set the position of the element.
	 *
	 * @param position the position.
	 */
	public void setPosition(GeoLocationPoint position) {
		if ((this.position == null && position != null)
			|| (this.position != null && !this.position.equals(position))) {
			this.position = position;
			fireShapeChanged();
			checkPrimitiveValidity();
		}
	}

	/** Replies the position of the bus stop.
	 *
	 * @return the position or <code>null</code> if never set before.
	 */
	@Pure
	public GeoLocationPoint getGeoPosition() {
		return this.position;
	}

	/** Replies the position of the bus stop.
	 *
	 * @return the position
	 */
	@Pure
	public Point2d getPosition2D() {
		if (this.position == null) {
			return null;
		}
		return this.position.getPoint();
	}

	//------------------------------------------------------------------
	// GIS API
	//------------------------------------------------------------------

	@Override
	public void rebuild(boolean fireEvents) {
		resetBoundingBox();
		if (fireEvents) {
			setEventFirable(true);
		}
		checkPrimitiveValidity();
		if (!fireEvents) {
			setEventFirable(true);
		}
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		final GeoLocationPoint p2d = getGeoPosition();
		if (p2d == null) {
			return null;
		}
		final Rectangle2d rect = new Rectangle2d();
		rect.setFromCorners(p2d.getX(), p2d.getY(), p2d.getX(), p2d.getY());
		return rect;
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	@Pure
	public GeoLocation getGeoLocation() {
		final GeoLocationPoint pt = getGeoPosition();
		if (pt == null) {
			return new GeoLocationNowhere(getUUID());
		}
		return pt;
	}

	/** Replies the distance between the given point and this bus stop.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the distance to this bus stop
	 */
	@Pure
	public double distance(double x, double y) {
		if (isValidPrimitive()) {
			final GeoLocationPoint p = getGeoPosition();
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), x, y);
		}
		return Double.NaN;
	}

	/** Replies the distance between the given bus stop and this bus stop.
	 *
	 * @param stop the stop.
	 * @return the distance to this bus stop
	 */
	@Pure
	public double distance(BusStop stop) {
		if (isValidPrimitive() && stop.isValidPrimitive()) {
			final GeoLocationPoint p = getGeoPosition();
			final GeoLocationPoint p2 = stop.getGeoPosition();
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), p2.getX(), p2.getY());
		}
		return Double.NaN;
	}

	/** Replies the distance between the given bus itinerary halt and this bus stop.
	 *
	 * @param halt the halt.
	 * @return the distance to this bus stop
	 */
	@Pure
	public double distance(BusItineraryHalt halt) {
		if (isValidPrimitive() && halt.isValidPrimitive()) {
			final GeoLocationPoint p = getGeoPosition();
			final GeoLocationPoint p2 = halt.getGeoPosition();
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), p2.getX(), p2.getY());
		}
		return Double.NaN;
	}

	/** Replies the distance between the given point and this bus stop.
	 *
	 * @param point the pint
	 * @return the distance to this bus stop
	 */
	@Pure
	public double distance(Point2D<?, ?> point) {
		if (isValidPrimitive()) {
			final GeoLocationPoint p = getGeoPosition();
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), point.getX(), point.getY());
		}
		return Double.NaN;
	}

	/** Replies the distance between the given point and this bus stop.
	 *
	 * @param point the point.
	 * @return the distance to this bus stop
	 */
	@Pure
	public double distance(GeoLocationPoint point) {
		if (isValidPrimitive()) {
			final GeoLocationPoint p = getGeoPosition();
			return Point2D.getDistancePointPoint(p.getX(), p.getY(), point.getX(), point.getY());
		}
		return Double.NaN;
	}

	//------------------------------------------------------------------
	// Hub management
	//------------------------------------------------------------------

	/** Add a hub reference.
	 *
	 * @param hub the hub.
	 */
	void addBusHub(BusHub hub) {
		if (this.hubs == null) {
			this.hubs = new WeakArrayList<>();
		}
		this.hubs.add(hub);
	}

	/** Remove a hub reference.
	 *
	 * @param hub the hub.
	 */
	void removeBusHub(BusHub hub) {
		if (this.hubs != null) {
			this.hubs.remove(hub);
			if (this.hubs.isEmpty()) {
				this.hubs = null;
			}
		}
	}

	/** Replies if this bus stop is inside a hub.
	 *
	 * @return <code>true</code> if this bus stop is inside a hub,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean insideBusHub() {
		return this.hubs != null && !this.hubs.isEmpty();
	}

	/** Replies the hubs in which this bus stop is located.
	 *
	 * @return the hubs
	 */
	@Pure
	public Iterable<BusHub> busHubs() {
		if (this.hubs == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(this.hubs);
	}

	/** Replies the hubs in which this bus stop is located.
	 *
	 * @return the hubs
	 */
	@Pure
	public Iterator<BusHub> busHubIterator() {
		if (this.hubs == null) {
			return Collections.emptyIterator();
		}
		return Iterators.unmodifiableIterator(this.hubs.iterator());
	}

	//------------------------------------------------------------------
	// BusItineraryHalt management
	//------------------------------------------------------------------

	/** Add a bus halt reference.
	 *
	 * @param halt the halt.
	 */
	void addBusHalt(BusItineraryHalt halt) {
		if (this.halts == null) {
			this.halts = new WeakArrayList<>();
		}
		this.halts.add(halt);
	}

	/** Remove a bus halt reference.
	 *
	 * @param halt the halt.
	 */
	void removeBusHalt(BusItineraryHalt halt) {
		if (this.halts != null) {
			this.halts.remove(halt);
			if (this.halts.isEmpty()) {
				this.halts = null;
			}
		}
	}

	/** Replies if this bus stop is associated to a bus itinerary halt.
	 *
	 * @return <code>true</code> if this bus stop is associated to a bus itinerary halt,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isBusHaltBinded() {
		return this.halts != null && !this.halts.isEmpty();
	}

	@Override
	@Pure
	public int indexInParent() {
		return -1;
	}

	/** Replies the bus itinerary halts associated to this bus stop.
	 *
	 * @return the bus itinerary halts.
	 */
	@Pure
	public Iterable<BusItineraryHalt> getBindedBusHalts() {
		if (this.halts == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(this.halts);
	}

}

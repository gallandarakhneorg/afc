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
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.references.WeakArrayList;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A bus hub is a set of {@link BusStop bus stops} located
 * inside the same geographical area. Bus network's users could easily pass
 * from a bus stop to another when they are located inside the same bus hub.
 * <h3>Definition from Wikipedia</h3>
 * A bus hub is a structure where city or intercity buses stop to pick up
 * and drop off passengers. It is larger than a bus stop, which is usually
 * simply a place on the roadside, where buses can stop. It may be intended
 * as a terminal hub for a number of routes, or as a transfer hub
 * where the routes continue. Bus hub platforms may be assigned to fixed
 * bus lines, or variable in combination with a dynamic passenger information
 * system. The latter requires fewer platforms, but does not supply the
 * passenger the comfort of knowing the platform well in advance and waiting there.
 * <br>
 * Source: <a href="http://en.wikipedia.org/wiki/Bus_hub">WikiPedia</a>
 * <h3>Validation</h3>
 * A bus hub is valid if it has at least one stop inside and all its stops are valid in turn.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusHub extends AbstractBusPrimitive<BusNetwork> implements Iterable<BusStop> {

	private static final long serialVersionUID = -6882619797564379960L;

	/** List of bus stops in this hub.
	 */
	protected final List<BusStop> busStops = new WeakArrayList<>();

	/** Create bus hub with attributes in memory.
	 *
	 * @param network is the bus network in which this hub is located.
	 * @param name is the name of the bus hub.
	 */
	BusHub(BusNetwork network, String name) {
		this(network, name, new HeapAttributeCollection());
	}

	/** Create bus hub.
	 *
	 * @param network is the bus network in which this hub is located.
	 * @param name is the name of the bus hub.
	 * @param attributeProvider the attribute provider.
	 */
	BusHub(BusNetwork network, String name, AttributeCollection attributeProvider) {
		super(null, attributeProvider);
		if (network != null) {
			setContainer(network);
		}
		if (name != null) {
			setName(name);
		}
	}

	/** Replies a bus hub name that was not exist in the specified
	 * bus network.
	 *
	 * @param busnetwork is the bus network from which a free bus hub name may be computed.
	 * @return a name suitable for a bus hub.
	 */
	@Pure
	public static String getFirstFreeBusHubName(BusNetwork busnetwork) {
		int nb = busnetwork.getBusHubCount();
		String name;
		do {
			++nb;
			name = Locale.getString(
					"NAME_TEMPLATE", //$NON-NLS-1$
					Integer.toString(nb));
		}
		while (busnetwork.getBusHub(name) != null);
		return name;
	}

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

	//------------------------------------------------------------------
	// Validity management
	//------------------------------------------------------------------

	@Override
	protected void checkPrimitiveValidity() {
		BusPrimitiveInvalidity invalidityReason = null;
		if (this.busStops.isEmpty()) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.NO_STOP_IN_HUB,
					null);
		} else {
			final Iterator<BusStop> iterator = this.busStops.iterator();
			while (iterator.hasNext() && invalidityReason == null) {
				final BusStop stop = iterator.next();
				if (!stop.isValidPrimitive()) {
					invalidityReason = new BusPrimitiveInvalidity(
							BusPrimitiveInvalidityType.INVALID_LINKED_STOP,
							stop.getName());
				} else {
					final BusNetwork bn = stop.getBusNetwork();
					if (bn == null || bn != getBusNetwork()) {
						invalidityReason = new BusPrimitiveInvalidity(
								BusPrimitiveInvalidityType.NOT_IN_SAME_NETWORK,
								null);
					}
				}
			}
		}
		setPrimitiveValidity(invalidityReason);
	}

	//------------------------------------------------------------------
	// Getter/Setter
	//------------------------------------------------------------------

	/**
	 * Replies the geo-location of this hub.
	 *
	 * @return the geo-location of the hub, never <code>null</code>.
	 */
	@Override
	@Pure
	public GeoLocation getGeoLocation() {
		final Rectangle2d b = getBoundingBox().toBoundingBox();
		if (b == null) {
			return new GeoLocationNowhere(getUUID());
		}
		return new GeoLocationPoint(b.getCenterX(), b.getCenterY());
	}

	/** Replies the position of the bus hub.
	 *
	 * @return the position or <code>null</code> if never set before.
	 */
	@Pure
	public GeoLocationPoint getGeoPosition() {
		final Rectangle2d b = getBoundingBox().toBoundingBox();
		if (b == null) {
			return null;
		}
		return new GeoLocationPoint(b.getCenterX(), b.getCenterY());
	}

	/** Replies the position of the bus hub.
	 *
	 * @return the position
	 */
	@Pure
	public Point2d getPosition2D() {
		final Rectangle2d b = getBoundingBox().toBoundingBox();
		if (b == null) {
			return null;
		}
		return new Point2d(b.getCenterX(), b.getCenterY());
	}

	@Override
	@Pure
	public BusNetwork getBusNetwork() {
		return getContainer();
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("stops", Iterables.transform(this.busStops, it -> it.getUUID())); //$NON-NLS-1$
	}

	/** Replies the name of this hub.
	 * If the bus stops inside this hub have the same
	 * names, this method replies the name of the bus stops.
	 * Otherwhise, it replies the value returned by
	 * {@link #getName()}.
	 *
	 * <p>The string comparison is case-insensitive.
	 *
	 * @return a name for this hub extended to the bus stops.
	 */
	@Override
	@Pure
	public String getName() {
		String name = super.getName();
		if (name == null) {
			for (final BusStop busstop : this.busStops) {
				if (name == null) {
					name = busstop.getName();
				} else if (!name.equalsIgnoreCase(busstop.getName())) {
					return null;
				}
			}
		}
		return name;
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		if (this.busStops == null || this.busStops.isEmpty()) {
			return null;
		}
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		for (final BusStop stop : this.busStops) {
			if (stop != null) {
				final Point2d p = stop.getPosition2D();
				if (p != null) {
					if (first) {
						first = false;
						r.set(p, p);
					} else {
						r.add(p);
					}
				}
			}
		}

		return first ? null : r;
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	/**
	 * Replies if this hub is empty.
	 *
	 * @return <code>true</code> if the hub is empty, otherwise <code>false</code>
	 */
	@Pure
	public boolean isEmpty() {
		return this.busStops.isEmpty();
	}

	@Override
	@Pure
	public int indexInParent() {
		return -1;
	}

	/**
	 * Replies if this hub contains at least two bus stops.
	 *
	 * @return <code>true</code> if the hub contains at least 2 bus stops,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isSignificant() {
		return this.busStops.size() >= 2;
	}

	/** Replies the distance between the given point and this bus hub.
	 *
	 * <p>The distance to the hub is the distance to the nearest bus stop
	 * located in the hub.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the distance to this bus hub
	 */
	@Pure
	public double distance(double x, double y) {
		double dist = Double.POSITIVE_INFINITY;
		if (isValidPrimitive()) {
			for (final BusStop stop : this.busStops) {
				final double d = stop.distance(x, y);
				if (!Double.isNaN(d) && d < dist) {
					dist = d;
				}
			}
		}
		return Double.isInfinite(dist) ? Double.NaN : dist;
	}

	/** Replies the distance between the given point and this bus hub.
	 *
	 * <p>The distance to the hub is the distance to the nearest bus stop
	 * located in the hub.
	 *
	 * @param point the point
	 * @return the distance to this bus hub
	 */
	@Pure
	public double distance(Point2D<?, ?> point) {
		return distance(point.getX(), point.getY());
	}

	/** Replies the distance between the given point and this bus hub.
	 *
	 * <p>The distance to the hub is the distance to the nearest bus stop
	 * located in the hub.
	 *
	 * @param point the point
	 * @return the distance to this bus hub
	 */
	@Pure
	public final double distance(GeoLocationPoint point) {
		return distance(point.getX(), point.getY());
	}

	//------------------------------------------------------------------
	// Bus stop management
	//------------------------------------------------------------------

	/**
	 * Add a bus stop inside the bus hub.
	 *
	 * @param busStop is the bus stop to insert.
	 * @return <code>true</code> if the bus stop was added, otherwise <code>false</code>
	 */
	public boolean addBusStop(BusStop busStop) {
		return addBusStop(busStop, true);
	}

	/**
	 * Add a bus stop inside the bus hub.
	 *
	 * @param busStop is the bus stop to insert.
	 * @param fireEvents indicates if the events should be fired and the validity checked.
	 * @return <code>true</code> if the bus stop was added, otherwise <code>false</code>
	 */
	boolean addBusStop(BusStop busStop, boolean fireEvents) {
		if (busStop == null) {
			return false;
		}
		if (this.busStops.indexOf(busStop) != -1) {
			return false;
		}
		if (!this.busStops.add(busStop)) {
			return false;
		}
		busStop.addBusHub(this);
		resetBoundingBox();
		if (fireEvents) {
			firePrimitiveChanged(new BusChangeEvent(this,
					BusChangeEventType.STOP_ADDED,
					busStop,
					this.busStops.size() - 1,
					null,
					null,
					null));
			checkPrimitiveValidity();
		}
		return true;
	}

	/**
	 * Remove all the bus stops from the current hub.
	 */
	public void removeAllBusStops() {
		for (final BusStop busStop : this.busStops) {
			busStop.removeBusHub(this);
		}
		this.busStops.clear();
		resetBoundingBox();
		firePrimitiveChanged(new BusChangeEvent(this,
				BusChangeEventType.ALL_STOPS_REMOVED,
				null,
				-1,
				null,
				null,
				null));
		checkPrimitiveValidity();
	}

	/**
	 * Remove a bus stop from this hub.
	 *
	 * @param busStop is the bus stop to remove.
	 * @return <code>true</code> if the bus stop was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeBusStop(BusStop busStop) {
		final int index = this.busStops.indexOf(busStop);
		if (index >= 0) {
			this.busStops.remove(index);
			busStop.removeBusHub(this);
			resetBoundingBox();
			firePrimitiveChanged(new BusChangeEvent(this,
					BusChangeEventType.STOP_REMOVED,
					busStop,
					index,
					null,
					null,
					null));
			checkPrimitiveValidity();
			return true;
		}
		return false;
	}

	/**
	 * Remove the bus stop at the specified index.
	 *
	 * @param index is the index of the bus stop to remove.
	 * @return <code>true</code> if the bus stop was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeBusStop(int index) {
		try {
			final BusStop busStop = this.busStops.remove(index);
			busStop.removeBusHub(this);
			resetBoundingBox();
			firePrimitiveChanged(new BusChangeEvent(this,
					BusChangeEventType.STOP_REMOVED,
					busStop,
					index,
					null,
					null,
					null));
			checkPrimitiveValidity();
			return true;
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	/**
	 * Replies the count of bus stops into this hub.
	 *
	 * @return the count of bus stops into this hub.
	 */
	@Pure
	public int getBusStopCount() {
		return this.busStops.size();
	}

	/**
	 * Replies the index of the specified bus stop.
	 *
	 * @param busStop is the bus stop to search for.
	 * @return the index or <code>-1</code> if it was not found.
	 */
	@Pure
	public int indexOf(BusStop busStop) {
		return this.busStops.indexOf(busStop);
	}

	/**
	 * Replies if the given bus stop is inside this hub.
	 *
	 * @param busStop is the bus stop to search for.
	 * @return <code>true</code> if the bus stop is inside the hub,
	 * <code>false</code> otherwise.
	 */
	@Pure
	public boolean contains(BusStop busStop) {
		return this.busStops.contains(busStop);
	}

	/**
	 * Replies the bus stop at the specified index.
	 *
	 * @param index the index.
	 * @return a bus stop
	 */
	@Pure
	public BusStop getBusStopAt(int index) {
		return this.busStops.get(index);
	}

	/** Replies the list of the bus stops of the bus hub.
	 *
	 * @return a list of bus stops
	 */
	@Pure
	public Iterator<BusStop> busStopIterator() {
		return Iterators.unmodifiableIterator(this.busStops.iterator());
	}

	/** Replies the list of the bus stops of the bus hub.
	 *
	 * @return a list of bus stops
	 */
	@Pure
	public Iterable<BusStop> busStops() {
		return Collections.unmodifiableList(this.busStops);
	}

	/** Replies the array of the bus stops.
	 * This function copies the bus stops in an array.
	 *
	 * @return the array of bus stops.
	 */
	@Pure
	public BusStop[] busStopsArray() {
		return Collections.unmodifiableList(this.busStops).toArray(new BusStop[this.busStops.size()]);
	}

	//------------------------------------------------------------------
	// Iterable interface
	//------------------------------------------------------------------

	@Override
	@Pure
	public Iterator<BusStop> iterator() {
		return busStopIterator();
	}

}

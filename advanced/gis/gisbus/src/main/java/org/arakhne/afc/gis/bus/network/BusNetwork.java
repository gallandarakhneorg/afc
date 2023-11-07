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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.tree.StandardGISTreeSet;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.util.ListUtil;
import org.arakhne.afc.util.MultiCollection;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * A bus network is composed of {@link BusLine bus lines}.
 * <h3>Validation</h3>
 * A bus network is valid if it has at least one line and all its lines are valid in turn,
 * and all bus stops are valid.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusNetwork extends AbstractBusContainer<BusContainer<?>, BusLine> {

	private static final long serialVersionUID = -395580791551473140L;

	private static final BusStopComparator INVALID_STOP_COMPARATOR = new BusStopComparator();

	private static final BusHubComparator INVALID_HUB_COMPARATOR = new BusHubComparator();

	//------------------------------------------------------------------
	// Attributes
	//------------------------------------------------------------------

	/** Road network on which the bus network is mapped.
	 */
	private final RoadNetwork roadNetwork;

	/** List of lines in this network.
	 */
	private final List<BusLine> busLines = new ArrayList<>();

	/** List of hubs in this network.
	 */
	private final StandardGISTreeSet<BusHub> validBusHubs = new StandardGISTreeSet<>();

	/** List of not-localized hubs in this network.
	 */
	private final List<BusHub> invalidBusHubs = new ArrayList<>();

	/** List of localized stops in this network.
	 */
	private final StandardGISTreeSet<BusStop> validBusStops = new StandardGISTreeSet<>();

	/** List of not-localized stops in this network.
	 */
	private final List<BusStop> invalidBusStops = new ArrayList<>();

	//------------------------------------------------------------------
	// Constructors
	//------------------------------------------------------------------

	/** Create bus network.
	 *
	 * @param name is the name of the new bus network
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 * @param attributeProvider is the attribute provider which must be used by this object.
	 */
	public BusNetwork(String name, RoadNetwork roadNetwork, AttributeCollection attributeProvider) {
		this((UUID) null, name, roadNetwork, attributeProvider);
	}

	/** Create bus network.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param name is the name of the new bus network
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 * @param attributeProvider is the attribute provider which must be used by this object.
	 * @since 2.0
	 */
	public BusNetwork(UUID id, String name, RoadNetwork roadNetwork, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		this.roadNetwork = roadNetwork;
		if (name != null) {
			setName(name);
		}
	}

	/** Create bus network.
	 *
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 * @param attributeProvider is the attribute provider which must be used by this object.
	 */
	public BusNetwork(RoadNetwork roadNetwork, AttributeCollection attributeProvider) {
		this((UUID) null, null, roadNetwork, attributeProvider);
	}

	/** Create bus network.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 * @param attributeProvider is the attribute provider which must be used by this object.
	 * @since 2.0
	 */
	public BusNetwork(UUID id, RoadNetwork roadNetwork, AttributeCollection attributeProvider) {
		this(id, null, roadNetwork, attributeProvider);
	}

	/** Create bus network with a storage in memory of the attributes.
	 *
	 * @param name is the name of the new bus network
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 */
	public BusNetwork(String name, RoadNetwork roadNetwork) {
		this((UUID) null, name, roadNetwork, new HeapAttributeCollection());
	}

	/** Create bus network with a storage in memory of the attributes.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param name is the name of the new bus network
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 * @since 2.0
	 */
	public BusNetwork(UUID id, String name, RoadNetwork roadNetwork) {
		this(id, name, roadNetwork, new HeapAttributeCollection());
	}

	/** Create bus network with a storage in memory of the attributes.
	 *
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 */
	public BusNetwork(RoadNetwork roadNetwork) {
		this((UUID) null, null, roadNetwork, new HeapAttributeCollection());
	}

	/** Create bus network with a storage in memory of the attributes.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 * @since 2.0
	 */
	public BusNetwork(UUID id, RoadNetwork roadNetwork) {
		this(id, null, roadNetwork, new HeapAttributeCollection());
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("lines", busLines()); //$NON-NLS-1$
		buffer.add("validHubs", this.validBusHubs); //$NON-NLS-1$
		buffer.add("invalidHubs", this.invalidBusHubs); //$NON-NLS-1$
	}

	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public void rebuild(boolean fireEvents) {
		for (final BusLine line : this.busLines) {
			line.rebuild();
		}

		final BusStop[] tabV = new BusStop[this.validBusStops.size()];
		this.validBusStops.toArray(tabV);
		final BusStop[] tabI = new BusStop[this.invalidBusStops.size()];
		this.invalidBusStops.toArray(tabI);

		for (final BusStop stop : tabV) {
			assert stop != null;
			stop.rebuild();
			if (!stop.isValidPrimitive() && isEventFirable()) {
				this.validBusStops.remove(stop);
				ListUtil.addIfAbsent(this.invalidBusStops, INVALID_STOP_COMPARATOR, stop);
			}
		}

		for (final BusStop stop : tabI) {
			assert stop != null;
			stop.revalidate();
			if (stop.isValidPrimitive() && isEventFirable()) {
				ListUtil.remove(this.invalidBusStops, INVALID_STOP_COMPARATOR, stop);
				this.validBusStops.add(stop);
			}
		}

		final BusHub[] tabV2 = new BusHub[this.validBusHubs.size()];
		this.validBusHubs.toArray(tabV2);
		final BusHub[] tabI2 = new BusHub[this.invalidBusHubs.size()];
		this.invalidBusHubs.toArray(tabI2);

		for (final BusHub hub : tabV2) {
			assert hub != null;
			hub.rebuild();
			if (!hub.isValidPrimitive() && isEventFirable()) {
				this.validBusHubs.remove(hub);
				ListUtil.addIfAbsent(this.invalidBusHubs, INVALID_HUB_COMPARATOR, hub);
			}
		}

		for (final BusHub hub : tabI2) {
			assert hub != null;
			hub.revalidate();
			if (hub.isValidPrimitive() && isEventFirable()) {
				ListUtil.remove(this.invalidBusHubs, INVALID_HUB_COMPARATOR, hub);
				this.validBusHubs.add(hub);
			}
		}

		resetBoundingBox();
		if (fireEvents) {
			setEventFirable(true);
			fireShapeChanged();
		}
		checkPrimitiveValidity();
		if (!fireEvents) {
			setEventFirable(true);
		}
	}

	//------------------------------------------------------------------
	// Getter/setter
	//------------------------------------------------------------------

	@Override
	public Class<? extends BusLine> getElementType() {
		return BusLine.class;
	}

	@Override
	@Pure
	public BusNetwork getBusNetwork() {
		return this;
	}

	/** Replies the road network in which this bus network is mapped.
	 *
	 * @return the road network in which this bus network is mapped.
	 */
	@Pure
	public RoadNetwork getRoadNetwork() {
		return this.roadNetwork;
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		Rectangle2d lr;
		if (this.busLines != null) {
			for (final BusLine line : this.busLines) {
				lr = line.getBoundingBox();
				if (lr != null) {
					if (first) {
						first = false;
						r.set(lr);
					} else {
						r.setUnion(lr);
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

	@Override
	@Pure
	public int size() {
		return this.busLines.size();
	}

	@Override
	@Pure
	public Iterator<BusLine> iterator() {
		return Iterators.unmodifiableIterator(this.busLines.iterator());
	}

	//------------------------------------------------------------------
	// Validity management
	//------------------------------------------------------------------

	/** Replies if all the bus stops are valid.
	 *
	 * @return {@code true} if all the bus stops are valid,
	 *     otherwise {@code false}.
	 */
	@Pure
	public boolean isValidBusStops() {
		return this.invalidBusStops.isEmpty() && !this.validBusStops.isEmpty();
	}

	/** Replies if all the bus hubs are valid.
	 *
	 * @return {@code true} if all the bus hubs are valid,
	 *     otherwise {@code false}.
	 */
	@Pure
	public boolean isValidBusHubs() {
		return this.invalidBusHubs.isEmpty() && !this.validBusHubs.isEmpty();
	}

	/**
	 * {@inheritDoc}.
	 *
	 * <p>Override this function to be sure that
	 * the invalid and valid collections
	 * has no impact of the iteration on
	 * the elements in this itinerary
	 *
	 * @see #checkPrimitiveValidity()
	 */
	@Override
	public void revalidate() {
		//
		// Revalidate bus stops
		//
		final BusStop[] tabV = new BusStop[this.validBusStops.size()];
		this.validBusStops.toArray(tabV);
		final BusStop[] tabI = new BusStop[this.invalidBusStops.size()];
		this.invalidBusStops.toArray(tabI);

		// Revalidate valid stops
		for (final BusStop stop : tabV) {
			assert stop != null;
			stop.revalidate();
		}

		// Revalidate invalid stops
		for (final BusStop stop : tabI) {
			assert stop != null;
			stop.revalidate();
		}

		//
		// Revalidate bus hubs
		//
		final BusHub[] tabV2 = new BusHub[this.validBusHubs.size()];
		this.validBusHubs.toArray(tabV2);
		final BusHub[] tabI2 = new BusHub[this.invalidBusHubs.size()];
		this.invalidBusHubs.toArray(tabI2);

		// Revalidate valid hubs
		for (final BusHub hub : tabV2) {
			assert hub != null;
			hub.revalidate();
		}

		// Revalidate invalid hubs
		for (final BusHub hub : tabI2) {
			assert hub != null;
			hub.revalidate();
		}

		//
		// Revalidate bus lines
		//
		for (final BusLine line : this.busLines) {
			assert line != null;
			line.revalidate();
		}

		checkPrimitiveValidity();
	}

	@Override
	protected void checkPrimitiveValidity() {
		BusPrimitiveInvalidity invalidityReason = null;
		if (this.validBusStops.size() < 2) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.NOT_ENOUGH_VALID_BUS_STOPS,
					null);
		} else if (!this.invalidBusStops.isEmpty()) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.INVALID_STOP_IN_NETWORK,
					null);
		} else if (!this.invalidBusHubs.isEmpty()) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.INVALID_HUB_IN_NETWORK,
					null);
		} else if (this.busLines.isEmpty()) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.NO_LINE_IN_NETWORK,
					null);
		} else {
			final Iterator<BusLine> lineIterator = this.busLines.iterator();
			BusLine line;
			while (lineIterator.hasNext() && invalidityReason == null) {
				line = lineIterator.next();
				if (!line.isValidPrimitive()) {
					invalidityReason = new BusPrimitiveInvalidity(
							BusPrimitiveInvalidityType.INVALID_LINE_IN_NETWORK,
							line.getName());
				}
			}
		}
		setPrimitiveValidity(invalidityReason);
	}

	//------------------------------------------------------------------
	// Bus line management
	//------------------------------------------------------------------

	/**
	 * Add a bus line inside the bus network.
	 *
	 * @param busLine is the bus line to insert.
	 * @return {@code true} if the bus line was added, otherwise {@code false}
	 */
	public boolean addBusLine(BusLine busLine) {
		if (busLine == null) {
			return false;
		}
		if (this.busLines.indexOf(busLine) != -1) {
			return false;
		}
		if (!this.busLines.add(busLine)) {
			return false;
		}
		final boolean isValidLine = busLine.isValidPrimitive();
		busLine.setEventFirable(isEventFirable());
		busLine.setContainer(this);
		if (isEventFirable()) {
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.LINE_ADDED,
					busLine,
					this.busLines.size() - 1,
					"shape", //$NON-NLS-1$
					null,
					null));
			if (!isValidLine) {
				revalidate();
			} else {
				checkPrimitiveValidity();
			}
		}
		return true;
	}

	/**
	 * Add a bus line at the specified index.
	 *
	 * @param busLine is the bus line to insert.
	 * @param index the index.
	 * @return {@code true} if the bus line was added, otherwise {@code false}
	 */
	public boolean addBusLine(BusLine busLine, int index) {
		if (busLine == null) {
			return false;
		}
		if (index < 0 || index > this.busLines.size()) {
			return false;
		}
		if (this.busLines.indexOf(busLine) != -1) {
			return false;
		}
		final boolean isValidLine = busLine.isValidPrimitive();
		try {
			this.busLines.add(index, busLine);
		} catch (Throwable exception) {
			return false;
		}
		busLine.setEventFirable(isEventFirable());
		busLine.setContainer(this);
		if (isEventFirable()) {
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.LINE_ADDED,
					busLine,
					index,
					"shape", //$NON-NLS-1$
					null,
					null));
			if (!isValidLine) {
				revalidate();
			} else {
				checkPrimitiveValidity();
			}
		}
		return true;
	}

	/**
	 * Remove all the bus lines from the current network.
	 */
	public void removeAllBusLines() {
		for (final BusLine busline : this.busLines) {
			busline.setContainer(null);
			busline.setEventFirable(true);
		}
		this.busLines.clear();
		fireShapeChanged(new BusChangeEvent(this,
				BusChangeEventType.ALL_LINES_REMOVED,
				null,
				-1,
				"shape", //$NON-NLS-1$
				null,
				null));
		checkPrimitiveValidity();
	}

	/**
	 * Remove a bus line from this network.
	 * All the itineraries and the associated stops will be removed also.
	 * If this bus line removal implies empty hubs, these hubs
	 * will be also removed.
	 *
	 * @param busLine is the bus line to remove.
	 * @return {@code true} if the bus line was successfully removed, otherwise {@code false}
	 */
	public boolean removeBusLine(BusLine busLine) {
		final int index = this.busLines.indexOf(busLine);
		if (index >= 0) {
			return removeBusLine(index);
		}
		return false;
	}

	/**
	 * Remove the bus line with the given name.
	 * All the itineraries and the associated stops will be removed also.
	 * If this bus line removal implies empty hubs, these hubs
	 * will be also removed.
	 *
	 * @param name is the name of the bus line to remove.
	 * @return {@code true} if the bus line was successfully removed, otherwise {@code false}
	 */
	public boolean removeBusLine(String name) {
		final Iterator<BusLine> iterator = this.busLines.iterator();
		BusLine busLine;
		int i = 0;
		while (iterator.hasNext()) {
			busLine = iterator.next();
			if (name.equals(busLine.getName())) {
				iterator.remove();
				busLine.setContainer(null);
				busLine.setEventFirable(true);
				fireShapeChanged(new BusChangeEvent(this,
						BusChangeEventType.LINE_REMOVED,
						busLine,
						i,
						"shape", //$NON-NLS-1$
						null,
						null));
				checkPrimitiveValidity();
				return true;
			}
			++i;
		}
		return false;
	}

	/**
	 * Remove the bus line at the specified index.
	 * All the itineraries and the associated stops will be removed also.
	 * If this bus line removal implies empty hubs, these hubs
	 * will be also removed.
	 *
	 * @param index is the index of the bus line to remove.
	 * @return {@code true} if the bus line was successfully removed, otherwise {@code false}
	 */
	public boolean removeBusLine(int index) {
		try {
			final BusLine busLine = this.busLines.remove(index);
			busLine.setContainer(null);
			busLine.setEventFirable(true);
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.LINE_REMOVED,
					busLine,
					index,
					"shape", //$NON-NLS-1$
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
	 * Replies the count of bus lines into this network.
	 *
	 * @return the count of bus lines into this network.
	 */
	@Pure
	public int getBusLineCount() {
		return this.busLines.size();
	}

	/**
	 * Replies the index of the specified bus line.
	 *
	 * @param busLine is the bus line to search for.
	 * @return the index or {@code -1} if it was not found.
	 */
	@Pure
	public int indexOf(BusLine busLine) {
		return this.busLines.indexOf(busLine);
	}

	@Override
	@Pure
	public int indexInParent() {
		return -1;
	}

	/**
	 * Replies the line at the specified index.
	 *
	 * @param index the index.
	 * @return BusLine
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public BusLine getBusLineAt(int index) {
		return this.busLines.get(index);
	}

	/**
	 * Replies the bus line with the specified name.
	 *
	 * @param name is the desired name
	 * @return BusLine or {@code null}
	 */
	@Pure
	public BusLine getBusLine(String name) {
		return getBusLine(name, null);
	}

	/**
	 * Replies the bus line with the specified uuid.
	 *
	 * @param uuid the identifier.
	 * @return BusLine or {@code null}
	 */
	@Pure
	public BusLine getBusLine(UUID uuid) {
		if (uuid == null) {
			return null;
		}
		for (final BusLine busLine : this.busLines) {
			if (uuid.equals(busLine.getUUID())) {
				return busLine;
			}
		}
		return null;
	}

	/**
	 * Replies the bus line with the specified name.
	 *
	 * @param name is the desired name
	 * @param nameComparator is used to compare the names.
	 * @return BusLine or {@code null}
	 */
	@Pure
	public BusLine getBusLine(String name, Comparator<String> nameComparator) {
		if (name == null) {
			return null;
		}
		final Comparator<String> cmp = nameComparator == null ? BusNetworkUtilities.NAME_COMPARATOR : nameComparator;
		for (final BusLine busLine : this.busLines) {
			if (cmp.compare(name, busLine.getName()) == 0) {
				return busLine;
			}
		}
		return null;
	}

	/** Replies the list of the bus lines of the bus network.
	 *
	 * @return a list of bus lines
	 */
	@Pure
	public Iterator<BusLine> busLineIterator() {
		return Iterators.unmodifiableIterator(this.busLines.iterator());
	}

	/** Replies the list of the bus lines of the bus network.
	 *
	 * @return a list of bus lines
	 */
	@Pure
	public Iterable<BusLine> busLines() {
		return Collections.unmodifiableList(this.busLines);
	}

	//------------------------------------------------------------------
	// Bus stop management
	//------------------------------------------------------------------

	/**
	 * Add a bus stop inside the bus network.
	 *
	 * @param busStop is the bus stop to insert.
	 * @return {@code true} if the bus stop was added, otherwise {@code false}
	 */
	public boolean addBusStop(BusStop busStop) {
		if (busStop == null) {
			return false;
		}
		if (this.validBusStops.contains(busStop)) {
			return false;
		}
		if (ListUtil.contains(this.invalidBusStops, INVALID_STOP_COMPARATOR, busStop)) {
			return false;
		}

		final boolean isValidPrimitive = busStop.isValidPrimitive();

		if (isValidPrimitive) {
			if (!this.validBusStops.add(busStop)) {
				return false;
			}
		} else if (ListUtil.addIfAbsent(this.invalidBusStops, INVALID_STOP_COMPARATOR, busStop) < 0) {
			return false;
		}

		busStop.setEventFirable(isEventFirable());
		busStop.setContainer(this);
		if (isEventFirable()) {
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.STOP_ADDED,
					busStop,
					-1,
					"shape", //$NON-NLS-1$
					null,
					null));
			busStop.checkPrimitiveValidity();
			checkPrimitiveValidity();
		}
		return true;
	}

	/**
	 * Remove all the bus stops from the current network.
	 */
	public void removeAllBusStops() {
		for (final BusStop busStop : this.validBusStops) {
			busStop.setContainer(null);
			busStop.setEventFirable(true);
		}
		for (final BusStop busStop : this.invalidBusStops) {
			busStop.setContainer(null);
			busStop.setEventFirable(true);
		}
		this.validBusStops.clear();
		this.invalidBusStops.clear();
		fireShapeChanged(new BusChangeEvent(this,
				BusChangeEventType.ALL_STOPS_REMOVED,
				null,
				-1,
				"shape", //$NON-NLS-1$
				null,
				null));
		revalidate();
	}

	/**
	 * Remove a bus stop from this network.
	 * If this bus stop removal implies empty hubs, these hubs
	 * will be also removed.
	 *
	 * @param busStop is the bus stop to remove.
	 * @return {@code true} if the bus stop was successfully removed, otherwise {@code false}
	 */
	public boolean removeBusStop(BusStop busStop) {
		if (this.validBusStops.remove(busStop)) {
			busStop.setContainer(null);
			busStop.setEventFirable(true);
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.STOP_REMOVED,
					busStop,
					-1,
					"shape", //$NON-NLS-1$
					null,
					null));
			checkPrimitiveValidity();
			return true;
		}
		final int idx = ListUtil.remove(this.invalidBusStops, INVALID_STOP_COMPARATOR, busStop);
		if (idx >= 0) {
			busStop.setContainer(null);
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.STOP_REMOVED,
					busStop,
					-1,
					"shape", //$NON-NLS-1$
					null,
					null));
			checkPrimitiveValidity();
			return true;
		}
		return false;
	}

	/**
	 * Remove the bus stops with the given name.
	 * If this bus stop removal implies empty hubs, these hubs
	 * will be also removed.
	 *
	 * @param name is the name of the bus stop to remove.
	 * @return {@code true} if the bus stop was successfully removed, otherwise {@code false}
	 */
	public boolean removeBusStop(String name) {
		Iterator<BusStop> iterator;
		BusStop busStop;

		iterator = this.validBusStops.iterator();
		while (iterator.hasNext()) {
			busStop = iterator.next();
			if (name.equals(busStop.getName())) {
				iterator.remove();
				busStop.setContainer(null);
				busStop.setEventFirable(true);
				fireShapeChanged(new BusChangeEvent(this,
						BusChangeEventType.STOP_REMOVED,
						busStop,
						-1,
						"shape", //$NON-NLS-1$
						null,
						null));
				checkPrimitiveValidity();
				return true;
			}
		}

		iterator = this.invalidBusStops.iterator();
		while (iterator.hasNext()) {
			busStop = iterator.next();
			if (name.equals(busStop.getName())) {
				iterator.remove();
				busStop.setContainer(null);
				fireShapeChanged(new BusChangeEvent(this,
						BusChangeEventType.STOP_REMOVED,
						busStop,
						-1,
						"shape", //$NON-NLS-1$
						null,
						null));
				checkPrimitiveValidity();
				return true;
			}
		}

		return false;
	}

	/**
	 * Replies the count of bus stops into this network.
	 *
	 * @return the count of bus stops into this network.
	 */
	@Pure
	public int getBusStopCount() {
		return this.validBusStops.size() + this.invalidBusStops.size();
	}

	/**
	 * Replies the bus stop with the specified name.
	 *
	 * @param name is the desired name
	 * @return BusStop or {@code null}
	 */
	@Pure
	public BusStop getBusStop(String name) {
		return getBusStop(name, null);
	}

	/**
	 * Replies the bus stop with the specified id.
	 *
	 * @param id is the desired identifier
	 * @return BusStop or {@code null}
	 */
	@Pure
	public BusStop getBusStop(UUID id) {
		if (id == null) {
			return null;
		}
		for (final BusStop busStop : this.validBusStops) {
			final UUID busid = busStop.getUUID();
			if (id.equals(busid)) {
				return busStop;
			}
		}
		for (final BusStop busStop : this.invalidBusStops) {
			final UUID busid = busStop.getUUID();
			if (id.equals(busid)) {
				return busStop;
			}
		}
		return null;
	}

	/**
	 * Replies the bus stop with the specified name.
	 *
	 * @param name is the desired name
	 * @param nameComparator is used to compare the names.
	 * @return BusStop or {@code null}
	 */
	@Pure
	public BusStop getBusStop(String name, Comparator<String> nameComparator) {
		if (name == null) {
			return null;
		}
		final Comparator<String> cmp = nameComparator == null ? BusNetworkUtilities.NAME_COMPARATOR : nameComparator;
		for (final BusStop busStop : this.validBusStops) {
			if (cmp.compare(name, busStop.getName()) == 0) {
				return busStop;
			}
		}
		for (final BusStop busStop : this.invalidBusStops) {
			if (cmp.compare(name, busStop.getName()) == 0) {
				return busStop;
			}
		}
		return null;
	}

	/** Replies the list of the bus stops of the bus network.
	 *
	 * @return a list of bus stops
	 */
	@Pure
	public Iterator<BusStop> busStopIterator() {
		return Iterators.unmodifiableIterator(
				Iterators.concat(this.validBusStops.iterator(), this.invalidBusStops.iterator()));
	}

	/** Replies the list of the bus stops of the bus network.
	 *
	 * @return a list of bus stops
	 */
	@Pure
	public Iterable<BusStop> busStops() {
		final MultiCollection<BusStop> col = new MultiCollection<>();
		col.addCollection(this.validBusStops);
		col.addCollection(this.invalidBusStops);
		return Iterables.unmodifiableIterable(col);
	}

	/** Replies the set of bus stops that have an intersection with the specified rectangle.
	 *
	 * <p>This function replies the bus stops with a broad-first iteration on the elements' tree.
	 *
	 * @param clipBounds is the bounds outside which the bus stops will not be replied
	 * @return the bus stops inside the specified bounds.
	 */
	@Pure
	public Iterator<BusStop> getBusStopsIn(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
		return Iterators.unmodifiableIterator(
				this.validBusStops.iterator(clipBounds));
	}

	/**
	 * Replies the nearest bus stop to the given point.
	 *
	 * @param point the point.
	 * @return the nearest bus stop or {@code null} if none was found.
	 */
	@Pure
	public final BusStop getNearestBusStop(Point2D<?, ?> point) {
		return getNearestBusStop(point.getX(), point.getY());
	}

	/**
	 * Replies the nearest bus stop to the given point.
	 *
	 * @param point the point
	 * @return the nearest bus stop or {@code null} if none was found.
	 */
	@Pure
	public final BusStop getNearestBusStop(GeoLocationPoint point) {
		return getNearestBusStop(point.getX(), point.getY());
	}

	/**
	 * Replies the nearest bus stops to the given point.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the nearest bus stop or {@code null} if none was found.
	 */
	@Pure
	public BusStop getNearestBusStop(double x, double y) {
		double distance = Double.POSITIVE_INFINITY;
		BusStop bestStop = null;
		double dist;

		for (final BusStop stop : this.validBusStops) {
			dist = stop.distance(x, y);
			if (dist < distance) {
				distance = dist;
				bestStop = stop;
			}
		}

		return bestStop;
	}

	//------------------------------------------------------------------
	// Bus Hub management
	//------------------------------------------------------------------

	/**
	 * Add a bus hub in this network.
	 * At least, one bus stop must be given to create a hub.
	 * The name of the hub is deduced from the bus stops names.
	 *
	 * @param stops are the bus stops to put inside the hub.
	 * @return the create bus hub, or {@code null} if not created.
	 */
	public BusHub addBusHub(BusStop... stops) {
		return addBusHub(null, stops);
	}

	/**
	 * Add a bus hub in this network.
	 * At least, one bus stop must be given to create a hub if you pass a
	 * {@code null} name.
	 *
	 * @param name is the name of the hub.
	 * @param stops are the bus stops to put inside the hub.
	 * @return the create bus hub, or {@code null} if not created.
	 */
	public BusHub addBusHub(String name, BusStop... stops) {
		// Do not set immediatly the container to
		// avoid event firing when adding the stops
		final BusHub hub = new BusHub(null, name);
		for (final BusStop stop : stops) {
			hub.addBusStop(stop, false);
		}
		if (addBusHub(hub)) {
			return hub;
		}
		return null;
	}

	/** Add the given bus hub in the network.
	 *
	 * @param hub is the new bus hub.
	 * @return {@code true} on success, otherwise {@code false}.
	 */
	private boolean addBusHub(BusHub hub) {
		if (hub == null) {
			return false;
		}
		if (this.validBusHubs.contains(hub)) {
			return false;
		}
		if (ListUtil.contains(this.invalidBusHubs, INVALID_HUB_COMPARATOR, hub)) {
			return false;
		}

		final boolean isValidPrimitive = hub.isValidPrimitive();

		if (isValidPrimitive) {
			if (!this.validBusHubs.add(hub)) {
				return false;
			}
		} else if (ListUtil.addIfAbsent(this.invalidBusHubs, INVALID_HUB_COMPARATOR, hub) < 0) {
			return false;
		}

		hub.setEventFirable(isEventFirable());
		hub.setContainer(this);
		if (isEventFirable()) {
			firePrimitiveChanged(new BusChangeEvent(this,
					BusChangeEventType.HUB_ADDED,
					hub,
					-1,
					null,
					null,
					null));
			hub.checkPrimitiveValidity();
			checkPrimitiveValidity();
		}
		return true;
	}

	/**
	 * Remove all the bus hubs from the current network.
	 */
	public void removeAllBusHubs() {
		for (final BusHub busHub : this.validBusHubs) {
			busHub.setContainer(null);
			busHub.setEventFirable(true);
		}
		for (final BusHub busHub : this.invalidBusHubs) {
			busHub.setContainer(null);
			busHub.setEventFirable(true);
		}
		this.validBusHubs.clear();
		this.invalidBusHubs.clear();
		firePrimitiveChanged(new BusChangeEvent(this,
				BusChangeEventType.ALL_HUBS_REMOVED,
				null,
				-1,
				null,
				null,
				null));
		revalidate();
	}

	/**
	 * Remove a bus hubs from this network.
	 *
	 * @param busHub is the bus hub to remove.
	 * @return {@code true} if the bus hub was successfully removed, otherwise {@code false}
	 */
	public boolean removeBusHub(BusHub busHub) {
		if (this.validBusHubs.remove(busHub)) {
			busHub.setContainer(null);
			busHub.setEventFirable(true);
			firePrimitiveChanged(new BusChangeEvent(this,
					BusChangeEventType.HUB_REMOVED,
					busHub,
					-1,
					null,
					null,
					null));
			checkPrimitiveValidity();
			return true;
		}
		final int idx = ListUtil.remove(this.invalidBusHubs, INVALID_HUB_COMPARATOR, busHub);
		if (idx >= 0) {
			busHub.setContainer(null);
			busHub.setEventFirable(true);
			firePrimitiveChanged(new BusChangeEvent(this,
					BusChangeEventType.HUB_REMOVED,
					busHub,
					-1,
					null,
					null,
					null));
			checkPrimitiveValidity();
			return true;
		}
		return false;
	}

	/**
	 * Remove the bus hub with the given name.
	 *
	 * @param name is the name of the bus hub to remove.
	 * @return {@code true} if the bus hub was successfully removed, otherwise {@code false}
	 */
	public boolean removeBusHub(String name) {
		Iterator<BusHub> iterator;
		BusHub busHub;

		iterator = this.validBusHubs.iterator();
		while (iterator.hasNext()) {
			busHub = iterator.next();
			if (name.equals(busHub.getName())) {
				iterator.remove();
				busHub.setContainer(null);
				busHub.setEventFirable(true);
				firePrimitiveChanged(new BusChangeEvent(this,
						BusChangeEventType.HUB_REMOVED,
						busHub,
						-1,
						null,
						null,
						null));
				checkPrimitiveValidity();
				return true;
			}
		}

		iterator = this.invalidBusHubs.iterator();
		while (iterator.hasNext()) {
			busHub = iterator.next();
			if (name.equals(busHub.getName())) {
				iterator.remove();
				busHub.setContainer(null);
				firePrimitiveChanged(new BusChangeEvent(this,
						BusChangeEventType.HUB_REMOVED,
						busHub,
						-1,
						null,
						null,
						null));
				checkPrimitiveValidity();
				return true;
			}
		}

		return false;
	}

	/**
	 * Replies the count of bus hubs into this network.
	 *
	 * @return the count of bus hubs into this network.
	 */
	@Pure
	public int getBusHubCount() {
		return this.validBusHubs.size() + this.invalidBusHubs.size();
	}

	/**
	 * Replies the bus hub with the specified name.
	 *
	 * @param name is the desired name
	 * @return a bus hub or {@code null}
	 */
	@Pure
	public BusHub getBusHub(String name) {
		return getBusHub(name, null);
	}

	/**
	 * Replies the bus hub with the specified uuid.
	 *
	 * @param uuid the identifier.
	 * @return a bus hub or {@code null}
	 */
	@Pure
	public BusHub getBusHub(UUID uuid) {
		if (uuid == null) {
			return null;
		}
		for (final BusHub busHub : this.validBusHubs) {
			if (uuid.equals(busHub.getUUID())) {
				return busHub;
			}
		}
		for (final BusHub busHub : this.invalidBusHubs) {
			if (uuid.equals(busHub.getUUID())) {
				return busHub;
			}
		}
		return null;
	}

	/**
	 * Replies the bus hub with the specified name.
	 *
	 * @param name is the desired name
	 * @param nameComparator is used to compare the names.
	 * @return a bus hub or {@code null}
	 */
	@Pure
	public BusHub getBusHub(String name, Comparator<String> nameComparator) {
		if (name == null) {
			return null;
		}
		final Comparator<String> cmp = nameComparator == null ? BusNetworkUtilities.NAME_COMPARATOR : nameComparator;
		for (final BusHub busHub : this.validBusHubs) {
			if (cmp.compare(name, busHub.getName()) == 0) {
				return busHub;
			}
		}
		for (final BusHub busHub : this.invalidBusHubs) {
			if (cmp.compare(name, busHub.getName()) == 0) {
				return busHub;
			}
		}
		return null;
	}

	/** Replies the list of the bus hubs of the bus network.
	 *
	 * @return a list of bus hubs.
	 */
	@Pure
	public Iterator<BusHub> busHubIterator() {
		return Iterators.unmodifiableIterator(
				Iterators.concat(this.validBusHubs.iterator(), this.invalidBusHubs.iterator()));
	}

	/** Replies the list of the bus hubs of the bus network.
	 *
	 * @return a list of bus hubs
	 */
	@Pure
	public Iterable<BusHub> busHubs() {
		final MultiCollection<BusHub> col = new MultiCollection<>();
		col.addCollection(this.validBusHubs);
		col.addCollection(this.invalidBusHubs);
		return Collections.unmodifiableCollection(col);
	}

	/** Replies the set of bus hubs that have an intersection with the specified rectangle.
	 *
	 * <p>This function replies the bus hubs with a broad-first iteration on the hubs' tree.
	 *
	 * @param clipBounds is the bounds outside which the bus hubs will not be replied
	 * @return the bus hubs inside the specified bounds.
	 */
	@Pure
	public Iterator<BusHub> getBusHubsIn(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
		return Iterators.unmodifiableIterator(
				this.validBusHubs.iterator(clipBounds));
	}

	/**
	 * Replies the nearest bus hub to the given point.
	 *
	 * @param point the point
	 * @return the nearest bus hub or {@code null} if none was found.
	 */
	@Pure
	public final BusHub getNearestBusHub(Point2D<?, ?> point) {
		return getNearestBusHub(point.getX(), point.getY());
	}

	/**
	 * Replies the nearest bus hub to the given point.
	 *
	 * @param point the point
	 * @return the nearest bus hub or {@code null} if none was found.
	 */
	@Pure
	public final BusHub getNearestBusHub(GeoLocationPoint point) {
		return getNearestBusHub(point.getX(), point.getY());
	}

	/**
	 * Replies the nearest bus hub to the given point.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the nearest bus hub or {@code null} if none was found.
	 */
	@Pure
	public BusHub getNearestBusHub(double x, double y) {
		double distance = Double.POSITIVE_INFINITY;
		BusHub bestHub = null;
		double dist;

		for (final BusHub hub : this.validBusHubs) {
			dist = hub.distance(x, y);
			if (dist < distance) {
				distance = dist;
				bestHub = hub;
			}
		}

		return bestHub;
	}

	//----------------------------------------------
	// Event Management
	//----------------------------------------------

	private void onBusStopChanged(BusStop stop) {
		if (stop.getContainer() == this) {
			final boolean oldValidity = !ListUtil.contains(this.invalidBusStops, INVALID_STOP_COMPARATOR, stop);
			final boolean currentValidity = stop.isValidPrimitive();
			if (oldValidity != currentValidity) {
				if (currentValidity) {
					ListUtil.remove(this.invalidBusStops, INVALID_STOP_COMPARATOR, stop);
					this.validBusStops.add(stop);
				} else {
					this.validBusStops.remove(stop);
					ListUtil.addIfAbsent(this.invalidBusStops, INVALID_STOP_COMPARATOR, stop);
				}
			}
		}
	}

	private void onBusHubChanged(BusHub hub) {
		if (hub.getContainer() == this) {
			final boolean oldValidity = !ListUtil.contains(this.invalidBusHubs, INVALID_HUB_COMPARATOR, hub);
			final boolean currentValidity = hub.isValidPrimitive();
			if (oldValidity != currentValidity) {
				if (currentValidity) {
					ListUtil.remove(this.invalidBusHubs, INVALID_HUB_COMPARATOR, hub);
					this.validBusHubs.add(hub);
				} else {
					this.validBusHubs.remove(hub);
					ListUtil.addIfAbsent(this.invalidBusHubs, INVALID_HUB_COMPARATOR, hub);
				}
			}
		}
	}

	@Override
	public void onBusPrimitiveChanged(BusChangeEvent event) {
		final Object source = event.getSource();
		if (source instanceof BusStop) {
			onBusStopChanged((BusStop) source);
		} else if (source instanceof BusHub) {
			onBusHubChanged((BusHub) source);
		}
		super.onBusPrimitiveChanged(event);
	}

	/** Internal comparator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class BusStopComparator implements Comparator<BusStop> {

		/** Constructor.
		 */
		BusStopComparator() {
			//
		}

		@Override
		@Pure
		public int compare(BusStop o1, BusStop o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MAX_VALUE;
			}
			if (o2 == null) {
				return Integer.MIN_VALUE;
			}
			return o1.getUUID().compareTo(o2.getUUID());
		}

	} // class BusStopComparator

	/** Internal comparator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class BusHubComparator implements Comparator<BusHub> {

		/** Constructor.
		 */
		BusHubComparator() {
			//
		}

		@Override
		@Pure
		public int compare(BusHub o1, BusHub o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MAX_VALUE;
			}
			if (o2 == null) {
				return Integer.MIN_VALUE;
			}
			return o1.getUUID().compareTo(o2.getUUID());
		}

	} // class BusHubComparator

}

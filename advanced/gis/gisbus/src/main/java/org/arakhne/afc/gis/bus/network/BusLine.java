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

package org.arakhne.afc.gis.bus.network;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Iterators;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.math.geometry.d2.d.Path2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A bus line is group of {@link BusItinerary itineraries}.
 * <h3>Validation</h3>
 * A bus line is valid if it has at list one itinerary and all its itineraries are valid in turn.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusLine extends AbstractBusContainer<BusNetwork, BusItinerary> {

	private static final long serialVersionUID = -8578378454127838065L;

	/** List of the itineraries of this line.
	 */
	private final List<BusItinerary> itineraries = new ArrayList<>();

	private SoftReference<Path2d> path;

	//------------------------------------------------------------------
	// Constructors
	//------------------------------------------------------------------

	/** Create bus line with a memory-based attribute provider.
	 *
	 * @param network is the network which is containing this bus line.
	 * @param name is the name of the new bus line
	 */
	public BusLine(BusNetwork network, String name) {
		this((UUID) null, network, name, new HeapAttributeCollection());
	}

	/** Create bus line with a memory-based attribute provider.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param network is the network which is containing this bus line.
	 * @param name is the name of the new bus line
	 * @since 2.0
	 */
	public BusLine(UUID id, BusNetwork network, String name) {
		this(id, network, name, new HeapAttributeCollection());
	}

	/** Create bus line.
	 *
	 * @param network is the network which is containing this bus line.
	 * @param name is the name of the new bus line
	 * @param attributeProvider the attribute provider.
	 */
	public BusLine(BusNetwork network, String name, AttributeCollection attributeProvider) {
		this((UUID) null, network, name, attributeProvider);
	}

	/** Create bus line.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param network is the network which is containing this bus line.
	 * @param name is the name of the new bus line
	 * @param attributeProvider the attribute provider.
	 * @since 2.0
	 */
	public BusLine(UUID id, BusNetwork network, String name, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		setContainer(network);
		setName(name);
	}

	/** Create bus line with a memory-based attribute provider.
	 *
	 * @param name is the name of the new bus line
	 */
	public BusLine(String name) {
		this((UUID) null, name, new HeapAttributeCollection());
	}

	/** Create bus line with a memory-based attribute provider.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus line
	 * @since 2.0
	 */
	public BusLine(UUID id, String name) {
		this(id, name, new HeapAttributeCollection());
	}

	/** Create bus line.
	 *
	 * @param name is the name of the new bus line
	 * @param attributeProvider the attribute provider.
	 */
	public BusLine(String name, AttributeCollection attributeProvider) {
		this((UUID) null, name, attributeProvider);
	}

	/** Create bus line.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus line
	 * @param attributeProvider the attribute provider.
	 * @since 2.0
	 */
	public BusLine(UUID id, String name, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		setName(name);
	}

	/** Create bus line with a memory-based attribute provider.
	 *
	 * @param network is the network which is containing this bus line.
	 */
	public BusLine(BusNetwork network) {
		this((UUID) null, network, new HeapAttributeCollection());
	}

	/** Create bus line with a memory-based attribute provider.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param network is the network which is containing this bus line.
	 * @since 2.0
	 */
	public BusLine(UUID id, BusNetwork network) {
		this(id, network, new HeapAttributeCollection());
	}

	/** Create bus line.
	 *
	 * @param network is the network which is containing this bus line.
	 * @param attributeProvider the attribute provider.
	 */
	public BusLine(BusNetwork network, AttributeCollection attributeProvider) {
		this((UUID) null, network, attributeProvider);
	}

	/** Create bus line.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param network is the network which is containing this bus line.
	 * @param attributeProvider the attribute provider.
	 * @since 2.0
	 */
	public BusLine(UUID id, BusNetwork network, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		setContainer(network);
	}

	/** Create bus line with a memory-based attribute provider.
	 */
	public BusLine() {
		this(new HeapAttributeCollection());
	}

	/** Create bus line with a memory-based attribute provider.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @since 2.0
	 */
	public BusLine(UUID id) {
		this(id, new HeapAttributeCollection());
	}

	/** Create bus line.
	 *
	 * @param attributeProvider the attribute provider.
	 */
	public BusLine(AttributeCollection attributeProvider) {
		this((UUID) null, attributeProvider);
	}

	/** Create bus line.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider the attribute provider.
	 * @since 2.0
	 */
	public BusLine(UUID id, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("itineraries", busItineraries()); //$NON-NLS-1$
	}

	/** Replies a bus line name that was not exist in the specified
	 * network.
	 *
	 * @param network is the bus network from which a free bus line name may be computed.
	 * @return the name suitable for a bus line.
	 */
	public static String getFirstFreeBusLineName(BusNetwork network) {
		if (network == null) {
			return null;
		}
		int nb = network.getBusLineCount();
		String name;
		do {
			++nb;
			name = Locale.getString("NAME_TEMPLATE", Integer.toString(nb)); //$NON-NLS-1$
		}
		while (network.getBusLine(name) != null);
		return name;
	}

	@Override
	public void rebuild(boolean fireEvents) {
		for (final BusItinerary itinerary : this.itineraries) {
			itinerary.rebuild();
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
	// Validity management
	//------------------------------------------------------------------

	@Override
	protected void checkPrimitiveValidity() {
		BusPrimitiveInvalidity invalidityReason = null;
		if (this.itineraries.isEmpty()) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.NO_ITINERARY_IN_LINE,
					null);
		} else {
			final Iterator<BusItinerary> iterator = this.itineraries.iterator();
			BusItinerary itinerary;
			while (iterator.hasNext() && invalidityReason == null) {
				itinerary = iterator.next();
				if (!itinerary.isValidPrimitive()) {
					invalidityReason = new BusPrimitiveInvalidity(
							BusPrimitiveInvalidityType.INVALID_ITINERARY_IN_LINE,
							itinerary.getName());
				}
			}
		}
		setPrimitiveValidity(invalidityReason);
	}

	//------------------------------------------------------------------
	// Getter/Setter
	//------------------------------------------------------------------

	@Override
	public Class<? extends BusItinerary> getElementType() {
		return BusItinerary.class;
	}

	@Override
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
			super.setName(getFirstFreeBusLineName(getContainer()));
		} else {
			super.setName(name);
		}
	}

	@Override
	protected Rectangle2d calcBounds() {
		Rectangle2d ir;
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		for (final BusItinerary itinerary : this.itineraries) {
			ir = itinerary.getBoundingBox();
			if (ir != null) {
				if (first) {
					first = true;
					r.set(ir);
				} else {
					r.setUnion(ir);
				}
			}
		}
		return first ? null : r;
	}

	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		this.path = null;
	}

	@Override
	public Shape2d<?> getShape() {
		return toPath2D();
	}

	/** Replies the path representation of this itinerary.
	 *
	 * @return the path.
	 */
	@Pure
	public Path2d toPath2D() {
		Path2d path = null;
		if (this.path != null) {
			path = this.path.get();
		}
		if (path == null) {
			path = new Path2d();
			toPath2D(path);
			this.path = new SoftReference<>(path);
		}
		return path;
	}

	/** Replies the path representation of this itinerary.
	 *
	 * @param path the path to fill.
	 */
	@Pure
	public void toPath2D(Path2d path) {
		// loop on parts and build the path to draw
		for (final BusItinerary iti : busItineraries()) {
			iti.toPath2D(path);
		}
	}

	@Override
	public int size() {
		return this.itineraries.size();
	}

	@Override
	public Iterator<BusItinerary> iterator() {
		return Iterators.unmodifiableIterator(this.itineraries.iterator());
	}

	//------------------------------------------------------------------
	// Bus itinerary management
	//------------------------------------------------------------------

	/**
	 * Add a bus itinerary inside the bus line.
	 *
	 * @param busItinerary is the bus itinerary to insert.
	 * @return <code>true</code> if the bus itinerary was added, otherwise <code>false</code>
	 */
	public boolean addBusItinerary(BusItinerary busItinerary) {
		if (busItinerary == null) {
			return false;
		}
		if (this.itineraries.indexOf(busItinerary) != -1) {
			return false;
		}
		if (!this.itineraries.add(busItinerary)) {
			return false;
		}
		final boolean isValidItinerary = busItinerary.isValidPrimitive();
		busItinerary.setEventFirable(isEventFirable());
		busItinerary.setContainer(this);
		if (isEventFirable()) {
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.ITINERARY_ADDED,
					busItinerary,
					this.itineraries.size() - 1,
					"shape", //$NON-NLS-1$
					null,
					null));
			if (!isValidItinerary) {
				revalidate();
			} else {
				checkPrimitiveValidity();
			}
		}
		return true;
	}

	/**
	 * Add a bus itinerary at the specified index.
	 *
	 * @param busItinerary is the bus itinerary to insert.
	 * @param index the index.
	 * @return <code>true</code> if the bus itinerary was added, otherwise <code>false</code>
	 */
	public boolean addBusItinerary(BusItinerary busItinerary, int index) {
		if (busItinerary == null) {
			return false;
		}
		if (index < 0 || index > this.itineraries.size()) {
			return false;
		}
		if (this.itineraries.indexOf(busItinerary) != -1) {
			return false;
		}
		final boolean isValidItinerary = busItinerary.isValidPrimitive();
		try {
			this.itineraries.add(index, busItinerary);
		} catch (Throwable exception) {
			return false;
		}
		busItinerary.setEventFirable(isEventFirable());
		busItinerary.setContainer(this);
		if (isEventFirable()) {
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.ITINERARY_ADDED,
					busItinerary,
					index,
					"shape", //$NON-NLS-1$
					null,
					null));
			if (!isValidItinerary) {
				revalidate();
			} else {
				checkPrimitiveValidity();
			}
		}
		return true;
	}

	/**
	 * Remove all the bus itineraries from the current line.
	 */
	public void removeAllBusItineraries() {
		for (final BusItinerary itinerary : this.itineraries) {
			itinerary.setContainer(null);
			itinerary.setEventFirable(true);
		}
		this.itineraries.clear();
		fireShapeChanged(new BusChangeEvent(this,
				BusChangeEventType.ALL_ITINERARIES_REMOVED,
				null,
				-1,
				"shape", //$NON-NLS-1$
				null,
				null));
		checkPrimitiveValidity();
	}

	/**
	 * Remove a bus itinerary from this line.
	 * All the associated stops will be removed also.
	 *
	 * @param itinerary is the bus itinerary to remove.
	 * @return <code>true</code> if the bus itinerary was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeBusItinerary(BusItinerary itinerary) {
		final int index = this.itineraries.indexOf(itinerary);
		if (index >= 0) {
			return removeBusItinerary(index);
		}
		return false;
	}

	/**
	 * Remove the bus itinerary at the specified index.
	 * All the associated stops will be removed also.
	 *
	 * @param index is the index of the bus itinerary to remove.
	 * @return <code>true</code> if the bus itinerary was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeBusItinerary(int index) {
		try {
			final BusItinerary busItinerary = this.itineraries.remove(index);
			busItinerary.setContainer(null);
			busItinerary.setEventFirable(true);
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.ITINERARY_REMOVED,
					busItinerary,
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
	 * Replies the count of bus itineraries into this line.
	 * @return the count of bus itineraries into this line.
	 */
	public int getBusItineraryCount() {
		return this.itineraries.size();
	}

	/**
	 * Replies the index of the specified bus itinerary.
	 *
	 * @param itinerary is the bus itinerary to search for.
	 * @return the index or <code>-1</code> if it was not found.
	 */
	public int indexOf(BusItinerary itinerary) {
		return this.itineraries.indexOf(itinerary);
	}

	@Override
	public int indexInParent() {
		final BusNetwork network = getContainer();
		if (network == null) {
			return -1;
		}
		return network.indexOf(this);
	}

	/**
	 * Replies the itinerary at the specified index.
	 *
	 * @param index the index.
	 * @return a bus itinerary
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public BusItinerary getBusItineraryAt(int index) {
		return this.itineraries.get(index);
	}

	/**
	 * Replies the bus itinerary with the specified name.
	 *
	 * @param name is the desired name
	 * @return a bus itinerary or <code>null</code>
	 */
	public BusItinerary getBusItinerary(String name) {
		return getBusItinerary(name, null);
	}

	/**
	 * Replies the bus itinerary with the specified uuid.
	 *
	 * @param uuid the identifier.
	 * @return BusItinerary or <code>null</code>
	 */
	public BusItinerary getBusItinerary(UUID uuid) {
		if (uuid == null) {
			return null;
		}
		for (final BusItinerary busItinerary : this.itineraries) {
			if (uuid.equals(busItinerary.getUUID())) {
				return busItinerary;
			}
		}
		return null;
	}

	/**
	 * Replies the bus itinerary with the specified name.
	 *
	 * @param name is the desired name
	 * @param nameComparator is used to compare the names.
	 * @return a bus itinerary or <code>null</code>
	 */
	public BusItinerary getBusItinerary(String name, Comparator<String> nameComparator) {
		if (name == null) {
			return null;
		}
		final Comparator<String> cmp = nameComparator == null ? BusNetworkUtilities.NAME_COMPARATOR : nameComparator;
		for (final BusItinerary itinerary : this.itineraries) {
			if (cmp.compare(name, itinerary.getName()) == 0) {
				return itinerary;
			}
		}
		return null;
	}

	/** Replies the list of the bus itineraries of the bus line.
	 *
	 * @return a list of bus itineraries
	 */
	public Iterator<BusItinerary> busItineraryIterator() {
		return Iterators.unmodifiableIterator(this.itineraries.iterator());
	}

	/** Replies the list of the bus itineraries of the bus line.
	 *
	 * @return a list of bus itineraries
	 */
	public Iterable<BusItinerary> busItineraries() {
		return Collections.unmodifiableList(this.itineraries);
	}

}

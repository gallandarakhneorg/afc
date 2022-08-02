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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.path.ClusteredRoadPath;
import org.arakhne.afc.gis.road.path.CrossRoad;
import org.arakhne.afc.gis.road.path.RoadPath;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkListener;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Path2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.util.ArrayUtil;
import org.arakhne.afc.util.IntegerList;
import org.arakhne.afc.util.ListUtil;
import org.arakhne.afc.util.MultiCollection;
import org.arakhne.afc.util.Triplet;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A bus itinerary is a sequence of {@link RoadSegment road segments} which are followed
 * by a bus.
 * <h3>Validation</h3>
 * An itinerary could be invalid if one of the following critera is not true:
 * <ol>
 * <li>an itinerary must contains one road segment,</li>
 * <li>all the road segments are connected in a sequence,</li>
 * <li>the count of bus halts must be greater or equal to 2,</li>
 * <li>each bus halt must be valid,</li>
 * <li>the first halt must be marked as a starting bus halt,</li>
 * <li>the last halt must be marked as a terminus,</li>
 * <li>the other bus halts must not be marked as starting bus halt nor terminus,</li>
 * <li>each bus halt must be located on a road segment of the itinerary,</li>
 * <li>the bus halts must be ordered, no cycle nor go-back is allowed.</li>
 * </ol>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings({"checkstyle:methodcount"})
public class BusItinerary extends AbstractBusContainer<BusLine, BusItineraryHalt> implements RoadNetworkListener {

	private static final long serialVersionUID = 2291205406053263827L;

	private static final BusItineraryHaltComparator VALID_HALT_COMPARATOR = new BusItineraryHaltComparator(true);

	private static final BusItineraryHaltComparator INVALID_HALT_COMPARATOR = new BusItineraryHaltComparator(false);

	private final List<BusItineraryHalt> validHalts = new ArrayList<>();

	private final List<BusItineraryHalt> invalidHalts = new ArrayList<>();

	private final ClusteredRoadPath roadSegments = new ClusteredRoadPath();

	private WeakReference<RoadNetwork> roadNetwork;

	/** This index is used to provide a unique id to the inserted bus halts.
	 * This identifier is used to put the halts in the sorted list of
	 * invalid halts.
	 */
	private int insertionIndex;

	private SoftReference<Path2d> path;

	/** Create bus itinerary with attributes stored in memory.
	 *
	 * @param busline is the line which is containing this itinerary.
	 * @param name is the name of the new bus itinerary
	 */
	public BusItinerary(BusLine busline, String name) {
		this(busline, name, new HeapAttributeCollection());
	}

	/** Create bus itinerary with attributes stored in memory.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param busline is the line which is containing this itinerary.
	 * @param name is the name of the new bus itinerary
	 * @since 2.0
	 */
	public BusItinerary(UUID id, BusLine busline, String name) {
		this(id, busline, name, new HeapAttributeCollection());
	}

	/** Create bus itinerary.
	 *
	 * @param busline is the line which is containing this itinerary.
	 * @param name is the name of the new bus itinerary
	 * @param attributeProvider is the attribute provider for this instance.
	 */
	public BusItinerary(BusLine busline, String name, AttributeCollection attributeProvider) {
		this((UUID) null, busline, name, attributeProvider);
	}

	/** Create bus itinerary.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param busline is the line which is containing this itinerary.
	 * @param name is the name of the new bus itinerary
	 * @param attributeProvider is the attribute provider for this instance.
	 * @since 2.0
	 */
	public BusItinerary(UUID id, BusLine busline, String name, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		setContainer(busline);
		setName(name);
	}

	/** Create bus itinerary with attributes stored in memory.
	 *
	 * @param name is the name of the new bus itinerary
	 */
	public BusItinerary(String name) {
		this(name, new HeapAttributeCollection());
	}

	/** Create bus itinerary with attributes stored in memory.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus itinerary
	 * @since 2.0
	 */
	public BusItinerary(UUID id, String name) {
		this(id, name, new HeapAttributeCollection());
	}

	/** Create bus itinerary.
	 *
	 * @param name is the name of the new bus itinerary
	 * @param attributeProvider is the attribute provider for this instance.
	 */
	public BusItinerary(String name, AttributeCollection attributeProvider) {
		this((UUID) null, name, attributeProvider);
	}

	/** Create bus itinerary.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param name is the name of the new bus itinerary
	 * @param attributeProvider is the attribute provider for this instance.
	 * @since 2.0
	 */
	public BusItinerary(UUID id, String name, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		setName(name);
	}

	/** Create bus itinerary with attributes stored in memory.
	 *
	 * @param busline is the line which is containing this itinerary.
	 */
	public BusItinerary(BusLine busline) {
		this((UUID) null, busline, new HeapAttributeCollection());
	}

	/** Create bus itinerary with attributes stored in memory.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param busline is the line which is containing this itinerary.
	 * @since 2.0
	 */
	public BusItinerary(UUID id, BusLine busline) {
		this(id, busline, new HeapAttributeCollection());
	}

	/** Create bus itinerary.
	 *
	 * @param busline is the line which is containing this itinerary.
	 * @param attributeProvider is the attribute provider for this instance.
	 */
	public BusItinerary(BusLine busline, AttributeCollection attributeProvider) {
		this((UUID) null, busline, attributeProvider);
	}

	/** Create bus itinerary.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param busline is the line which is containing this itinerary.
	 * @param attributeProvider is the attribute provider for this instance.
	 * @since 2.0
	 */
	public BusItinerary(UUID id, BusLine busline, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		setContainer(busline);
	}

	/** Create bus itinerary with attributes stored in memory.
	 */
	public BusItinerary() {
		this((UUID) null, new HeapAttributeCollection());
	}

	/** Create bus itinerary with attributes stored in memory.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @since 2.0
	 */
	public BusItinerary(UUID id) {
		this(id, new HeapAttributeCollection());
	}

	/** Create bus itinerary.
	 *
	 * @param attributeProvider is the attribute provider for this instance.
	 */
	public BusItinerary(AttributeCollection attributeProvider) {
		this((UUID) null, attributeProvider);
	}

	/** Create bus itinerary.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute provider for this instance.
	 * @since 2.0
	 */
	public BusItinerary(UUID id, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("roads", Iterables.transform(roadSegments(), it -> it.getGeoId())); //$NON-NLS-1$
		buffer.add("validHalts", this.validHalts); //$NON-NLS-1$
		buffer.add("invalidHalts", this.invalidHalts); //$NON-NLS-1$
	}

	/** Replies a bus itinerary name that was not exist in the specified
	 * bus line.
	 *
	 * @param busline the line.
	 * @return a free name
	 */
	@Pure
	public static String getFirstFreeBusItineraryName(BusLine busline) {
		if (busline == null) {
			return null;
		}
		int nb = busline.getBusItineraryCount();
		String name;
		do {
			++nb;
			name = Locale.getString("NAME_TEMPLATE", Integer.toString(nb)); //$NON-NLS-1$
		}
		while (busline.getBusItinerary(name) != null);
		return name;
	}

	@Override
	public void rebuild(boolean fireEvents) {
		final BusItineraryHalt[] tabV = new BusItineraryHalt[this.validHalts.size()];
		this.validHalts.toArray(tabV);
		final BusItineraryHalt[] tabI = new BusItineraryHalt[this.invalidHalts.size()];
		this.invalidHalts.toArray(tabI);

		// Revalidate valid halts
		for (final BusItineraryHalt halt : tabV) {
			assert halt != null;
			halt.rebuild();
			if (!halt.isValidPrimitive() && !halt.isEventFirable()) {
				ListUtil.remove(this.validHalts, VALID_HALT_COMPARATOR, halt);
				ListUtil.addIfAbsent(this.invalidHalts, INVALID_HALT_COMPARATOR, halt);
			}
		}

		// Revalidate invalid halts
		for (final BusItineraryHalt halt : tabI) {
			assert halt != null;
			halt.rebuild();
			if (halt.isValidPrimitive() && !halt.isEventFirable()) {
				ListUtil.remove(this.invalidHalts, INVALID_HALT_COMPARATOR, halt);
				ListUtil.addIfAbsent(this.validHalts, VALID_HALT_COMPARATOR, halt);
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
	// Validity management
	//------------------------------------------------------------------

	@Override
	public void revalidate() {
		// Override this function to be sure that
		// the invalid and valid collections
		// has no impact of the iteration on
		// the elements in this itinerary
		final BusItineraryHalt[] tabV = new BusItineraryHalt[this.validHalts.size()];
		this.validHalts.toArray(tabV);
		final BusItineraryHalt[] tabI = new BusItineraryHalt[this.invalidHalts.size()];
		this.invalidHalts.toArray(tabI);

		// Revalidate valid halts
		for (final BusItineraryHalt halt : tabV) {
			assert halt != null;
			halt.revalidate();
		}

		// Revalidate invalid halts
		for (final BusItineraryHalt halt : tabI) {
			assert halt != null;
			halt.revalidate();
		}

		checkPrimitiveValidity();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>An itinerary could be invalid if one of the following critera is not true:
	 * <ol>
	 * <li>an itinerary must contains one road segment,</li>
	 * <li>all the road segment are connected in a sequence,</li>
	 * <li>the count of bus halts must be at least 2,</li>
	 * <li>each bus halt must be valid,</li>
	 * <li>the first halt must be a starting bus halt,</li>
	 * <li>the last halt must be of terminus,</li>
	 * <li>the other bus halts must not be of type starting bus halt nor terminus,</li>
	 * <li>each bus halt must be located on a road segment of the itinerary,</li>
	 * <li>the bus halts must be ordered, no cycle nor go-back is allowed.</li>
	 * </ol>
	 */
	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:nestedifdepth"})
	protected void checkPrimitiveValidity() {
		BusPrimitiveInvalidity invalidityReason = null;
		if (this.roadSegments.getRoadSegmentCount() <= 0) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.NO_ROAD_SEGMENT_IN_ITINERARY,
					null);
		} else if (this.roadSegments.size() > 1) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.DISCONTINUOUS_PATH_IN_ITINERARY,
					Integer.toString(this.roadSegments.size()));
		} else if (this.validHalts.size() < 2) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.NOT_ENOUGH_VALID_BUS_HALTS,
					null);
		} else if (!this.invalidHalts.isEmpty()) {
			invalidityReason = new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.INVALID_HALT_IN_ITINERARY,
					null);
		} else {
			final BusNetwork busNetwork = getBusNetwork();
			if (busNetwork == null) {
				invalidityReason = new BusPrimitiveInvalidity(
						BusPrimitiveInvalidityType.NO_BUS_NETWORK,
						null);
			} else {
				final RoadNetwork roadNetwork = busNetwork.getRoadNetwork();
				if (roadNetwork == null) {
					invalidityReason = new BusPrimitiveInvalidity(
							BusPrimitiveInvalidityType.NO_ROAD_NETWORK,
							null);
				} else {
					final Iterator<RoadSegment> segmentIterator = this.roadSegments.roadSegments();
					RoadSegment segment;
					RoadNetwork segmentNetwork;
					while (segmentIterator.hasNext() && invalidityReason == null) {
						segment = segmentIterator.next();
						segmentNetwork = segment.getRoadNetwork();
						if (!roadNetwork.equals(segmentNetwork)) {
							invalidityReason = new BusPrimitiveInvalidity(
									BusPrimitiveInvalidityType.SEGMENT_OUTSIDE_ROAD_NETWORK,
									segment.toString());
						}
					}

					if (invalidityReason == null) {
						BusItineraryHalt firstHalt = null;
						BusItineraryHalt lastHalt = null;
						final Iterator<BusItineraryHalt> iterator = this.validHalts.iterator();
						int lastSegmentIndex = -1;
						int idx;
						while (iterator.hasNext() && invalidityReason == null) {
							lastHalt = iterator.next();
							if (firstHalt == null) {
								firstHalt = lastHalt;
							}
							if (!lastHalt.isValidPrimitive()) {
								invalidityReason = new BusPrimitiveInvalidity(
										BusPrimitiveInvalidityType.INVALID_HALT_IN_ITINERARY,
										lastHalt.getName());
							} else {
								idx = lastHalt.getRoadSegmentIndex();
								if (idx >= lastSegmentIndex) {
									lastSegmentIndex = idx;
								} else {
									invalidityReason = new BusPrimitiveInvalidity(
											BusPrimitiveInvalidityType.INVALID_HALT_ORDER,
											lastHalt.getName());
								}
							}
						}
						if (invalidityReason == null && (firstHalt == null
								|| lastHalt == null || firstHalt.equals(lastHalt))) {
							invalidityReason = new BusPrimitiveInvalidity(
									BusPrimitiveInvalidityType.NOT_ENOUGH_VALID_BUS_HALTS,
									null);
						}
						if (invalidityReason == null) {
							assert firstHalt != null;
							if (firstHalt.getRoadSegmentIndex() > 0) {
								invalidityReason = new BusPrimitiveInvalidity(
										BusPrimitiveInvalidityType.UNUSED_ROAD_SEGMENT_BEYOND_TERMINUS,
										firstHalt.getName());
							}
						}
						//check last halt from itinerary
						if (invalidityReason == null) {
							assert lastHalt != null;
							if (!(lastHalt.getRoadSegmentIndex() == this.getRoadSegmentCount() - 1)) {
								invalidityReason = new BusPrimitiveInvalidity(
										BusPrimitiveInvalidityType.UNUSED_ROAD_SEGMENT_BEYOND_TERMINUS,
										lastHalt.getName());
							}
						}
					}
				}
			}
		}


		setPrimitiveValidity(invalidityReason);
	}

	//------------------------------------------------------------------
	// Getter/Setter
	//------------------------------------------------------------------

	@Override
	public Class<? extends BusItineraryHalt> getElementType() {
		return BusItineraryHalt.class;
	}

	@Override
	@Pure
	public BusNetwork getBusNetwork() {
		final BusLine line = getContainer();
		if (line != null) {
			return line.getBusNetwork();
		}
		return null;
	}

	@Override
	public void setName(String name) {
		if (name == null) {
			super.setName(getFirstFreeBusItineraryName(getContainer()));
		} else {
			super.setName(name);
		}
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		Rectangle2d sr;
		final Rectangle2d r = new Rectangle2d();
		boolean first = true;
		final Iterator<RoadSegment> iterator = this.roadSegments.roadSegments();
		RoadSegment segment;
		while (iterator.hasNext()) {
			segment = iterator.next();
			sr = segment.getBoundingBox();
			if (sr != null) {
				if (first) {
					first = false;
					r.set(sr);
				} else {
					r.setUnion(sr);
				}
			}
		}
		for (final BusItineraryHalt halt : this.validHalts) {
			sr = halt.getBoundingBox();
			if (sr != null) {
				if (first) {
					first = false;
					r.set(sr);
				} else {
					r.setUnion(sr);
				}
			}
		}
		for (final BusItineraryHalt halt: this.invalidHalts) {
			if (halt.getBusStop() != null && halt.getBusStop().getPosition2D() != null) {
				sr = halt.getBoundingBox();
				if (sr != null) {
					if (first) {
						first = false;
						r.set(sr);
					} else {
						r.setUnion(sr);
					}
				}
				sr = halt.getBusStop().getBoundingBox();
				if (sr != null) {
					if (first) {
						first = false;
						r.set(sr);
					} else {
						r.setUnion(sr);
					}
				}
			}
		}
		return first ? null : r;
	}

	@Override
	public Shape2d<?> getShape() {
		return toPath2D();
	}

	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		this.path = null;
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
		if (isValidPrimitive()) {
			final BusItineraryHalt startTerminus = getBusHaltAt(0);
			final BusItineraryHalt endTerminus = getBusHaltAt(getValidBusHaltCount() - 1);

			if (startTerminus.getRoadSegmentIndex() == endTerminus.getRoadSegmentIndex()) {
				// One segment to render
				double p1 = startTerminus.getPositionOnSegment();
				double p2 = endTerminus.getPositionOnSegment();
				final RoadSegment segment = startTerminus.getRoadSegment();

				if (p1 > p2) {
					final double t = p1;
					p1 = p2;
					p2 = t;
				}
				// Caution: Assume that all given road segments are MapPolyline instances
				segment.toPath2D(path, p1, p2);
			} else {
				RoadSegment seg = startTerminus.getRoadSegment();
				if (seg != null) {
					final Direction1D d = getRoadSegmentDirection(startTerminus.getRoadSegmentIndex());
					final double p1;
					final double p2;
					if (d.isSegmentDirection()) {
						p1 = startTerminus.getPositionOnSegment();
						p2 = Double.NaN;
					} else {
						p1 = Double.NaN;
						p2 = startTerminus.getPositionOnSegment();
					}

					// Caution: Assume that all given road segments are MapPolyline instances
					seg.toPath2D(path, p1, p2);
				}

				for (int i = startTerminus.getRoadSegmentIndex() + 1; i < endTerminus.getRoadSegmentIndex(); ++i) {
					getRoadSegmentAt(i).toPath2D(path);
				}

				seg = endTerminus.getRoadSegment();
				if (seg != null) {
					final Direction1D d = getRoadSegmentDirection(endTerminus.getRoadSegmentIndex());
					final double p1;
					final double p2;
					if (d.isSegmentDirection()) {
						p1 = Double.NaN;
						p2 = endTerminus.getPositionOnSegment();
					} else {
						p1 = endTerminus.getPositionOnSegment();
						p2 = Double.NaN;
					}

					// Caution: Assume that all given road segments are MapPolyline instances
					seg.toPath2D(path, p1, p2);
				}
			}
		} else {
			// Because there is no enough valid bus halt in the itinerary,
			// all the road segments covered by the itinerary should be
			// drawn to indicate where the itinerary is.
			// Moreover, bus halts are simply drawn.
			for (final RoadSegment segment : roadSegments()) {
				segment.toPath2D(path);
			}
		}
	}

	/** {@inheritDoc}
	 * @see #getValidBusHaltCount() for the number of valid halts only.
	 * @see #getInvalidBusHaltCount() for the number of invalid halts only.
	 */
	@Override
	@Pure
	public int size() {
		return this.validHalts.size() + this.invalidHalts.size();
	}

	@Override
	@Pure
	public Iterator<BusItineraryHalt> iterator() {
		return Iterators.unmodifiableIterator(Iterators.concat(this.validHalts.iterator(), this.invalidHalts.iterator()));
	}

	/**
	 * Replies the direction of the itinerary along the road segment at the specified index.
	 *
	 * @param idxSegment is the index of the road segment in the itinerary.
	 * @return the direction followed on the road segment.
	 */
	@Pure
	public Direction1D getRoadSegmentDirection(int idxSegment) {
		return this.roadSegments.getRoadSegmentDirectionAt(idxSegment);
	}

	/**
	 * Invert the order of this itinerary.
	 *
	 * <p>This function reverses the direction of the itinerary, ie. the order
	 * of the road segments of the itinerary.
	 *
	 * <p>This function does not reverse the location of the bus halts on the itinerary.
	 * Their locations will be unchanged.
	 */
	public void invert() {
		final boolean isEventFirable = isEventFirable();
		setEventFirable(false);
		try {
			final int count = this.roadSegments.getRoadSegmentCount();

			// Invert the segments
			this.roadSegments.invert();

			// Invert the bus halts and their indexes.
			final int middle = this.validHalts.size() / 2;
			for (int i = 0, j = this.validHalts.size() - 1; i < middle; ++i, --j) {
				final BusItineraryHalt h1 = this.validHalts.get(i);
				final BusItineraryHalt h2 = this.validHalts.get(j);

				this.validHalts.set(i, h2);
				this.validHalts.set(j, h1);

				int idx = h1.getRoadSegmentIndex();
				idx = count - idx - 1;
				h1.setRoadSegmentIndex(idx);

				idx = h2.getRoadSegmentIndex();
				idx = count - idx - 1;
				h2.setRoadSegmentIndex(idx);
			}

			if (middle * 2 != this.validHalts.size()) {
				final BusItineraryHalt h1 = this.validHalts.get(middle);
				int idx = h1.getRoadSegmentIndex();
				idx = count - idx - 1;
				h1.setRoadSegmentIndex(idx);
			}
		} finally {
			setEventFirable(isEventFirable);
		}
		fireShapeChanged(new BusChangeEvent(this,
				BusChangeEventType.ITINERARY_INVERTED,
				this,
				indexInParent(),
				"busHalts", //$NON-NLS-1$
				null, null));
	}

	/** Replies if the given segment has a bus halt on it.
	 *
	 * @param segment is the segment to search on.
	 * @return <code>true</code> if a bus halt was located on the segment,
	 *     otherwhise <code>false</code>
	 */
	@Pure
	public boolean hasBusHaltOnSegment(RoadSegment segment) {
		if (this.roadSegments.isEmpty() || this.validHalts.isEmpty()) {
			return false;
		}

		int cIdxSegment;
		int lIdxSegment = -1;
		RoadSegment cSegment;

		for (final BusItineraryHalt bushalt : this.validHalts) {
			cIdxSegment = bushalt.getRoadSegmentIndex();
			if (cIdxSegment >= 0 && cIdxSegment < this.roadSegments.getRoadSegmentCount()
					&& cIdxSegment != lIdxSegment) {
				cSegment = this.roadSegments.getRoadSegmentAt(cIdxSegment);
				lIdxSegment = cIdxSegment;
				if (segment == cSegment) {
					return true;
				}
			}
		}

		return false;
	}

	/** Replies the bus halts on the given segment.
	 *
	 * @param segment is the segment to search on.
	 * @return the list of bus halts.
	 */
	@Pure
	public List<BusItineraryHalt> getBusHaltsOnSegment(RoadSegment segment) {
		if (this.roadSegments.isEmpty() || this.validHalts.isEmpty()) {
			return Collections.emptyList();
		}

		int cIdxSegment;
		int lIdxSegment = -1;
		int sIdxSegment = -1;
		RoadSegment cSegment;

		final List<BusItineraryHalt> halts = new ArrayList<>();

		for (final BusItineraryHalt bushalt : this.validHalts) {
			cIdxSegment = bushalt.getRoadSegmentIndex();
			if (cIdxSegment >= 0 && cIdxSegment < this.roadSegments.getRoadSegmentCount()
					&& cIdxSegment != lIdxSegment) {
				cSegment = this.roadSegments.getRoadSegmentAt(cIdxSegment);
				lIdxSegment = cIdxSegment;
				if (segment == cSegment) {
					sIdxSegment = cIdxSegment;
				} else if (sIdxSegment != -1) {
					// halt loop now because I'm sure there has no more bus halt
					break;
				}
			}
			if (sIdxSegment != -1) {
				halts.add(bushalt);
			}
		}

		return halts;
	}

	/** Replies the binding informations for all the bus halts of this itinerary.
	 *
	 * @return the pairs containing the index of the road segment and the
	 *     curviline position of each bus itinerary halt.
	 */
	@Pure
	public Map<BusItineraryHalt, Pair<Integer, Double>> getBusHaltBinding() {
		final Comparator<BusItineraryHalt> comp = (elt1, elt2) -> {
			if (elt1 == elt2) {
				return 0;
			}
			if (elt1 == null) {
				return -1;
			}
			if (elt2 == null) {
				return 1;
			}
			return elt1.getName().compareTo(elt2.getName());
		};
		final Map<BusItineraryHalt, Pair<Integer, Double>> haltBinding = new TreeMap<>(comp);
		for (final BusItineraryHalt halt : busHalts()) {
			haltBinding.put(halt, new Pair<>(
					halt.getRoadSegmentIndex(),
					halt.getPositionOnSegment()));
		}
		return haltBinding;
	}

	/** Set the binding informations for all the bus halts of this itinerary.
	 *
	 * @param binding are the pairs containing the index of the road segment and the
	 *     curviline position of each bus itinerary halt.
	 */
	public void setBusHaltBinding(Map<BusItineraryHalt, Pair<Integer, Float>> binding) {
		if (binding != null) {
			BusItineraryHalt halt;
			boolean shapeChanged = false;
			for (final Entry<BusItineraryHalt, Pair<Integer, Float>> entry : binding.entrySet()) {
				halt = entry.getKey();
				halt.setRoadSegmentIndex(entry.getValue().getKey());
				halt.setPositionOnSegment(entry.getValue().getValue());
				halt.checkPrimitiveValidity();
				shapeChanged = true;
			}
			if (shapeChanged) {
				fireShapeChanged(new BusChangeEvent(this,
						BusChangeEventType.ITINERARY_CHANGED,
						null,
						-1,
						"shape", //$NON-NLS-1$
						null,
						null));
			}
			checkPrimitiveValidity();
		}
	}

	/**
	 * Replies the length of this itinerary.
	 *
	 * <p>If the itinerary is valid, replies the distance
	 * between the start bus halt and the terminus.
	 * If the itinerary is not valid, replies the sum
	 * of the road segment lengths.
	 *
	 * @return the length of the itinerary.
	 */
	@Pure
	public double getLength() {
		double length = this.roadSegments.getLength();
		if (isValidPrimitive()) {
			BusItineraryHalt halt = this.validHalts.get(0);
			assert halt != null;
			RoadSegment sgmt = halt.getRoadSegment();
			assert sgmt != null;
			Direction1D dir = this.roadSegments.getRoadSegmentDirectionAt(halt.getRoadSegmentIndex());
			assert dir != null;
			if (dir == Direction1D.SEGMENT_DIRECTION) {
				length -= halt.getPositionOnSegment();
			} else {
				length -= sgmt.getLength() - halt.getPositionOnSegment();
			}

			halt = this.validHalts.get(this.validHalts.size() - 1);
			assert halt != null;
			sgmt = halt.getRoadSegment();
			assert sgmt != null;
			dir = this.roadSegments.getRoadSegmentDirectionAt(halt.getRoadSegmentIndex());
			assert dir != null;
			if (dir == Direction1D.SEGMENT_DIRECTION) {
				length -= sgmt.getLength() - halt.getPositionOnSegment();
			} else {
				length -= halt.getPositionOnSegment();
			}

			if (length < 0.) {
				length = 0.;
			}
		}
		return length;
	}

	/**
	 * Replies the distance between two bus halt.
	 *
	 * @param firsthaltIndex is the index of the first bus halt.
	 * @param lasthaltIndex is the index of the last bus halt.
	 * @return the distance in meters between the given bus halts.
	 * @throws ArrayIndexOutOfBoundsException if the last halt index is lower or equal to
	 *     the first halt index, or if this last halt index is out of range.
	 */
	@Pure
	public double getDistanceBetweenBusHalts(int firsthaltIndex, int lasthaltIndex) {
		if (firsthaltIndex < 0 || firsthaltIndex >= this.validHalts.size() - 1) {
			throw new ArrayIndexOutOfBoundsException(firsthaltIndex);
		}
		if (lasthaltIndex <= firsthaltIndex || lasthaltIndex >= this.validHalts.size()) {
			throw new ArrayIndexOutOfBoundsException(lasthaltIndex);
		}

		double length = 0;

		final BusItineraryHalt b1 = this.validHalts.get(firsthaltIndex);
		final BusItineraryHalt b2 = this.validHalts.get(lasthaltIndex);
		final int firstSegment = b1.getRoadSegmentIndex();
		final int lastSegment = b2.getRoadSegmentIndex();

		for (int i = firstSegment + 1; i < lastSegment; ++i) {
			final RoadSegment segment = this.roadSegments.getRoadSegmentAt(i);
			length += segment.getLength();
		}

		Direction1D direction = getRoadSegmentDirection(firstSegment);
		if (direction.isRevertedSegmentDirection()) {
			length += b1.getPositionOnSegment();
		} else {
			length += this.roadSegments.getRoadSegmentAt(firstSegment).getLength() - b1.getPositionOnSegment();
		}

		direction = getRoadSegmentDirection(lastSegment);
		if (direction.isSegmentDirection()) {
			length += b2.getPositionOnSegment();
		} else {
			length += this.roadSegments.getRoadSegmentAt(firstSegment).getLength() - b2.getPositionOnSegment();
		}

		return length;
	}

	/**
	 * Replies the distance between two bus halt. A segment between
	 * bus halts is named <em>troneon</em>.
	 *
	 * @param troneonIndex is the index of the troneon
	 * @return the distance in meters between the given bus halt and the
	 *     following one.
	 */
	@Pure
	public double getTroneonLength(int troneonIndex) {
		return getDistanceBetweenBusHalts(troneonIndex, troneonIndex + 1);
	}

	/**
	 * Replies the count of troneon. A segment between
	 * bus halts is named <em>troneon</em>.
	 *
	 * @return the count of troneons.
	 */
	@Pure
	public int getTroneonCount() {
		return Math.max(0, this.validHalts.size() - 1);
	}

	//-----------------------------------------------------
	// Bus halt management
	//-----------------------------------------------------

	/**
	 * Replies the nearest bus halt to the given point.
	 *
	 * @param point the point.
	 * @return the nearest bus halt or <code>null</code> if none was found.
	 */
	@Pure
	public final BusItineraryHalt getNearestBusHalt(Point2D<?, ?> point) {
		return getNearestBusHalt(point.getX(), point.getY());
	}

	/**
	 * Replies the nearest bus halt to the given point.
	 *
	 * @param point the point.
	 * @return the nearest bus halt or <code>null</code> if none was found.
	 */
	@Pure
	public final BusItineraryHalt getNearestBusHalt(GeoLocationPoint point) {
		return getNearestBusHalt(point.getX(), point.getY());
	}

	/**
	 * Replies the nearest bus halt to the given point.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the nearest bus halt or <code>null</code> if none was found.
	 */
	@Pure
	public BusItineraryHalt getNearestBusHalt(double x, double y) {
		double distance = Double.POSITIVE_INFINITY;
		BusItineraryHalt besthalt = null;
		double dist;

		for (final BusItineraryHalt halt : this.validHalts) {
			dist = halt.distance(x, y);
			if (dist < distance) {
				distance = dist;
				besthalt = halt;
			}
		}

		return besthalt;
	}

	/**
	 * Add a bus halt inside the bus itinerary.
	 *
	 * @param name is the name of the bus halt
	 * @param type is the type of the bus halt.
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public final BusItineraryHalt addBusHalt(String name, BusItineraryHaltType type) {
		return addBusHalt(null, name, type);
	}

	/**
	 * Add a bus halt inside the bus itinerary.
	 *
	 * @param type is the type of the bus halt.
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public final BusItineraryHalt addBusHalt(BusItineraryHaltType type) {
		return addBusHalt(null, null, type);
	}

	/**
	 * Add a bus halt inside the bus itinerary.
	 *
	 * <p>The insertion index is computed automatically
	 *
	 * @param id is the identifier of the bus halt.
	 * @param name is the name of the bus halt
	 * @param type is the type of the bus halt.
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public BusItineraryHalt addBusHalt(UUID id, String name, BusItineraryHaltType type) {
		return this.addBusHalt(id, name, type, -1);
	}

	/**
	 * Add a bus halt inside the bus itinerary.
	 *
	 * @param id is the identifier of the bus halt.
	 * @param name is the name of the bus halt
	 * @param type is the type of the bus halt.
	 * @param insertToIndex the inserted index
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	BusItineraryHalt addBusHalt(UUID id, String name, BusItineraryHaltType type, int insertToIndex) {
		String haltName = name;
		if (haltName == null || "".equals(haltName)) { //$NON-NLS-1$
			haltName = BusItineraryHalt.getFirstFreeBushaltName(this);
			assert haltName != null && !"".equals(haltName); //$NON-NLS-1$
		}
		final BusItineraryHalt halt;
		if (id == null) {
			halt = new BusItineraryHalt(this, haltName, type);
		} else {
			halt = new BusItineraryHalt(id, this, haltName, type);
		}

		if (addBusHalt(halt, insertToIndex)) {
			return halt;
		}
		return null;
	}

	/**
	 * Add a bus halt inside the bus itinerary.
	 *
	 * @param id is the identifier of the bus halt.
	 * @param type is the type of the bus halt.
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public final BusItineraryHalt addBusHalt(UUID id, BusItineraryHaltType type) {
		return addBusHalt(id, null, type);
	}

	/** Add the given bus halt in this itinerary.
	 *
	 * @param halt the halt.
	 * @param insertToIndex the insertion index.
	 * @return <code>true</code> if the addition was successful, <code>false</code>
	 *     otherwise.
	 */
	boolean addBusHalt(BusItineraryHalt halt, int insertToIndex) {
		//set index for right ordering when add to invalid list !
		if (insertToIndex < 0) {
			halt.setInvalidListIndex(this.insertionIndex);
		} else {
			halt.setInvalidListIndex(insertToIndex);
		}

		if (halt.isValidPrimitive()) {
			ListUtil.addIfAbsent(this.validHalts, VALID_HALT_COMPARATOR, halt);
		} else {
			ListUtil.addIfAbsent(this.invalidHalts, INVALID_HALT_COMPARATOR, halt);
		}
		halt.setEventFirable(isEventFirable());
		++this.insertionIndex;
		if (isEventFirable()) {
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.ITINERARY_HALT_ADDED,
					halt,
					halt.indexInParent(),
					"shape", null, null)); //$NON-NLS-1$
			checkPrimitiveValidity();
		}
		return true;
	}

	/**
	 * Insert newHalt after afterHalt in the ordered list of {@link BusItineraryHalt}.
	 *
	 * @param afterHalt the halt where insert the new halt
	 * @param id id of the new halt
	 * @param type the type of bus halt
	 *
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public BusItineraryHalt insertBusHaltAfter(BusItineraryHalt afterHalt, UUID id, BusItineraryHaltType type) {
		return insertBusHaltAfter(afterHalt, id, null, type);
	}

	/**
	 * Insert newHalt after afterHalt in the ordered list of {@link BusItineraryHalt}.
	 *
	 * @param afterHalt the halt where insert the new halt
	 * @param name name of the new halt
	 * @param type the type of bus halt
	 *
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public BusItineraryHalt insertBusHaltAfter(BusItineraryHalt afterHalt, String name, BusItineraryHaltType type) {
		return insertBusHaltAfter(afterHalt, null, name, type);
	}

	/**
	 * Insert newHalt after afterHalt in the ordered list of {@link BusItineraryHalt}.
	 *
	 * @param afterHalt the halt where insert the new halt
	 * @param id id of the new halt
	 * @param name name of the new halt
	 * @param type the type of bus halt
	 *
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public BusItineraryHalt insertBusHaltAfter(BusItineraryHalt afterHalt, UUID id, String name, BusItineraryHaltType type) {
		assert afterHalt != null;
		assert this.contains(afterHalt);

		if (this.getValidBusHaltCount() + this.getInvalidBusHaltCount() == 1) {
			return this.addBusHalt(id, name, type);
		}

		int haltIndex = -1;
		int indexAfter = -1;
		if (!afterHalt.isValidPrimitive()) {
			haltIndex =  ListUtil.indexOf(this.invalidHalts, INVALID_HALT_COMPARATOR, afterHalt);
			assert haltIndex != -1;
			indexAfter = this.invalidHalts.get(haltIndex).getInvalidListIndex();
		} else {
			haltIndex =  ListUtil.indexOf(this.validHalts, VALID_HALT_COMPARATOR, afterHalt);
			assert haltIndex != -1;
			indexAfter = this.validHalts.get(haltIndex).getInvalidListIndex();
		}
		BusItineraryHalt temp;
		//decal insertion id invalid halts
		for (int i = haltIndex + 1; i < this.invalidHalts.size(); ++i) {
			temp = this.invalidHalts.get(i);
			if (temp.getInvalidListIndex() >= indexAfter + 1) {
				temp.setInvalidListIndex(temp.getInvalidListIndex() + 1);
			}
		}
		//decal insertion id valid halts
		final Iterator<BusItineraryHalt> it = this.validHalts.iterator();
		while (it.hasNext()) {
			temp = it.next();
			if (temp.getInvalidListIndex() >= indexAfter + 1) {
				temp.setInvalidListIndex(temp.getInvalidListIndex() + 1);
			}
		}

		return this.addBusHalt(id, name, type, indexAfter + 1);
	}

	/**
	 * Insert newHalt before beforeHalt in the ordered list of {@link BusItineraryHalt}.
	 * @param beforeHalt the halt where insert the new halt
	 * @param name name of the new halt
	 * @param type the type of bus halt
	 *
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public BusItineraryHalt insertBusHaltBefore(BusItineraryHalt beforeHalt, String name, BusItineraryHaltType type) {
		return this.insertBusHaltBefore(beforeHalt, null, name, type);
	}

	/**
	 * Insert newHalt before beforeHalt in the ordered list of {@link BusItineraryHalt}.
	 * @param beforeHalt the halt where insert the new halt
	 * @param id id of the new halt
	 * @param type the type of bus halt
	 *
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public BusItineraryHalt insertBusHaltBefore(BusItineraryHalt beforeHalt, UUID id, BusItineraryHaltType type) {
		return this.insertBusHaltBefore(beforeHalt, id, null, type);
	}

	/**
	 * Insert newHalt before beforeHalt in the ordered list of {@link BusItineraryHalt}.
	 * @param beforeHalt the halt where insert the new halt
	 * @param id id of the new halt
	 * @param name name of the new halt
	 * @param type the type of bus halt
	 *
	 * @return the added bus halt, otherwise <code>null</code>
	 */
	public BusItineraryHalt insertBusHaltBefore(BusItineraryHalt beforeHalt, UUID id, String name, BusItineraryHaltType type) {
		assert beforeHalt != null;
		assert this.contains(beforeHalt);

		int haltIndex = -1;
		int indexBefore = -1;
		if (!beforeHalt.isValidPrimitive()) {
			haltIndex =  ListUtil.indexOf(this.invalidHalts, INVALID_HALT_COMPARATOR, beforeHalt);
			assert haltIndex != -1;
			indexBefore = this.invalidHalts.get(haltIndex).getInvalidListIndex();
		} else {
			haltIndex =  ListUtil.indexOf(this.validHalts, VALID_HALT_COMPARATOR, beforeHalt);
			assert haltIndex != -1;
			indexBefore = this.validHalts.get(haltIndex).getInvalidListIndex();
		}

		BusItineraryHalt temp;
		//decal insertion id invalid halts
		for (int i = haltIndex; i < this.invalidHalts.size(); ++i) {
			temp = this.invalidHalts.get(i);
			if (temp.getInvalidListIndex() >= indexBefore) {
				temp.setInvalidListIndex(temp.getInvalidListIndex() + 1);
			}
		}
		//decal insertion id valid halts
		final Iterator<BusItineraryHalt> it = this.validHalts.iterator();
		while (it.hasNext()) {
			temp = it.next();
			if (temp.getInvalidListIndex() >= indexBefore) {
				temp.setInvalidListIndex(temp.getInvalidListIndex() + 1);
			}
		}
		return this.addBusHalt(id, name, type, indexBefore);
	}

	/**
	 * Remove all the bus halts from the current itinerary.
	 */
	public void removeAllBusHalts() {

		for (final BusItineraryHalt bushalt : this.invalidHalts) {
			bushalt.setContainer(null);
			bushalt.setRoadSegmentIndex(-1);
			bushalt.setPositionOnSegment(Float.NaN);
			bushalt.checkPrimitiveValidity();
		}

		final BusItineraryHalt[] halts = new BusItineraryHalt[this.validHalts.size()];
		this.validHalts.toArray(halts);
		this.validHalts.clear();
		this.invalidHalts.clear();

		for (final BusItineraryHalt bushalt : halts) {
			bushalt.setContainer(null);
			bushalt.setRoadSegmentIndex(-1);
			bushalt.setPositionOnSegment(Float.NaN);
			bushalt.checkPrimitiveValidity();
		}

		fireShapeChanged(new BusChangeEvent(this,
				BusChangeEventType.ALL_ITINERARY_HALTS_REMOVED,
				null,
				-1,
				"shape", //$NON-NLS-1$
				null,
				null));
		checkPrimitiveValidity();
	}

	/**
	 * Remove a bus bus from this itinerary.
	 *
	 * @param bushalt is the bus halt to remove.
	 * @return <code>true</code> if the bus halt was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeBusHalt(BusItineraryHalt bushalt) {
		try {
			int index = ListUtil.indexOf(this.validHalts, VALID_HALT_COMPARATOR, bushalt);
			if (index >= 0) {
				return removeBusHalt(index);
			}
			index = ListUtil.indexOf(this.invalidHalts, INVALID_HALT_COMPARATOR, bushalt);
			if (index >= 0) {
				index += this.validHalts.size();
				return removeBusHalt(index);
			}
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	/**
	 * Remove the bus halt with the given name.
	 *
	 * @param name is the name of the bus halt to remove.
	 * @return <code>true</code> if the bus halt was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeBusHalt(String name) {
		Iterator<BusItineraryHalt> iterator = this.validHalts.iterator();
		BusItineraryHalt bushalt;
		int i = 0;
		while (iterator.hasNext()) {
			bushalt = iterator.next();
			if (name.equals(bushalt.getName())) {
				return removeBusHalt(i);
			}
			++i;
		}

		iterator = this.invalidHalts.iterator();
		i = 0;
		while (iterator.hasNext()) {
			bushalt = iterator.next();
			if (name.equals(bushalt.getName())) {
				return removeBusHalt(i);
			}
			++i;
		}

		return false;
	}

	/**
	 * Remove the bus halt at the specified index.
	 *
	 * @param index is the index of the bus halt to remove.
	 * @return <code>true</code> if the bus halt was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeBusHalt(int index) {
		try {
			final BusItineraryHalt removedBushalt;
			if (index < this.validHalts.size()) {
				removedBushalt = this.validHalts.remove(index);
			} else {
				final int idx = index - this.validHalts.size();
				removedBushalt = this.invalidHalts.remove(idx);
			}
			removedBushalt.setContainer(null);
			removedBushalt.setRoadSegmentIndex(-1);
			removedBushalt.setPositionOnSegment(Float.NaN);
			removedBushalt.setEventFirable(true);
			removedBushalt.checkPrimitiveValidity();
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.ITINERARY_HALT_REMOVED,
					removedBushalt,
					removedBushalt.indexInParent(),
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
	 * Replies the count of valid bus halts into this itinerary.
	 *
	 * @return the count of valid bus halts into this itinerary.
	 * @see #size() for the number of invalid and valid halts.
	 * @see #getInvalidBusHaltCount() for the number of invalid halts only.
	 */
	@Pure
	public int getValidBusHaltCount() {
		return this.validHalts.size();
	}

	/**
	 * Replies the count of valid bus halts into this itinerary.
	 *
	 * @return the count of valid bus halts into this itinerary.
	 * @see #size() for the number of invalid and valid halts.
	 * @see #getValidBusHaltCount() for the number of valid halts only.
	 */
	@Pure
	public int getInvalidBusHaltCount() {
		return this.invalidHalts.size();
	}

	/**
	 * Replies the index of the specified bus halt.
	 *
	 * @param bushalt is the bus halt to search for.
	 * @return the index or <code>-1</code> if it was not found.
	 */
	@Pure
	public int indexOf(BusItineraryHalt bushalt) {
		if (bushalt == null) {
			return -1;
		}
		if (bushalt.isValidPrimitive()) {
			return ListUtil.indexOf(this.validHalts, VALID_HALT_COMPARATOR, bushalt);
		}
		int idx = ListUtil.indexOf(this.invalidHalts, INVALID_HALT_COMPARATOR, bushalt);
		if (idx >= 0) {
			idx += this.validHalts.size();
		}
		return idx;
	}

	/**
	 * Replies the index of the first occurrence of the specified road segment.
	 *
	 * @param segment the segment.
	 * @return the index or <code>-1</code> if it was not found.
	 */
	@Pure
	public int indexOf(RoadSegment segment) {
		return this.roadSegments.indexOf(segment);
	}

	/**
	 * Replies the index of the last occurrence of the specified road segment.
	 *
	 * @param segment the segment.
	 * @return the index or <code>-1</code> if it was not found.
	 */
	@Pure
	public int lastIndexOf(RoadSegment segment) {
		return this.roadSegments.lastIndexOf(segment);
	}

	@Override
	@Pure
	public int indexInParent() {
		final BusLine line = getContainer();
		if (line == null) {
			return -1;
		}
		return line.indexOf(this);
	}

	/**
	 * Replies if the given bus halt is inside this bus itinerary.
	 *
	 * @param bushalt is the bus halt to search for.
	 * @return <code>true</code> if the bus halt is inside the itinerary,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean contains(BusItineraryHalt bushalt) {
		if (bushalt == null) {
			return false;
		}
		if (bushalt.isValidPrimitive()) {
			return ListUtil.contains(this.validHalts, VALID_HALT_COMPARATOR, bushalt);
		}
		return ListUtil.contains(this.invalidHalts, INVALID_HALT_COMPARATOR, bushalt);
	}

	/**
	 * Replies if the given segment is inside this bus itinerary.
	 *
	 * @param segment is the road segment to search for.
	 * @return <code>true</code> if the road segment is inside the itinerary,
	 *     otherwise <code>false</code>
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Pure
	public boolean contains(RoadSegment segment) {
		return this.roadSegments.contains(segment);
	}

	/**
	 * Replies the halt at the specified index.
	 *
	 * @param index the index.
	 * @return a bus halt
	 */
	@Pure
	public BusItineraryHalt getBusHaltAt(int index) {
		if (index < this.validHalts.size()) {
			return this.validHalts.get(index);
		}
		return this.invalidHalts.get(index - this.validHalts.size());
	}

	/**
	 * Replies the bus halt with the specified name.
	 *
	 * @param name is the desired name
	 * @return a bus halt or <code>null</code>
	 */
	@Pure
	public BusItineraryHalt getBusHalt(String name) {
		return getBusHalt(name, null);
	}

	/**
	 * Replies the bus halt with the specified uuid.
	 *
	 * @param uuid the identifier.
	 * @return BusItineraryHalt or <code>null</code>
	 */
	@Pure
	public BusItineraryHalt getBusHalt(UUID uuid) {
		if (uuid == null) {
			return null;
		}
		for (final BusItineraryHalt busHalt : this.validHalts) {
			if (uuid.equals(busHalt.getUUID())) {
				return busHalt;
			}
		}
		for (final BusItineraryHalt busHalt : this.invalidHalts) {
			if (uuid.equals(busHalt.getUUID())) {
				return busHalt;
			}
		}
		return null;
	}

	/**
	 * Replies the bus halt with the specified name.
	 *
	 * @param name is the desired name
	 * @param nameComparator is used to compare the names.
	 * @return a bus halt or <code>null</code>
	 */
	@Pure
	public BusItineraryHalt getBusHalt(String name, Comparator<String> nameComparator) {
		if (name == null) {
			return null;
		}
		final Comparator<String> cmp = nameComparator == null ? BusNetworkUtilities.NAME_COMPARATOR : nameComparator;
		for (final BusItineraryHalt bushalt : this.validHalts) {
			if (cmp.compare(name, bushalt.getName()) == 0) {
				return bushalt;
			}
		}
		for (final BusItineraryHalt bushalt : this.invalidHalts) {
			if (cmp.compare(name, bushalt.getName()) == 0) {
				return bushalt;
			}
		}
		return null;
	}

	/** Replies the list of the bus halts of the bus itinerary.
	 *
	 * @return a list of bus halts
	 */
	@Pure
	public Iterator<BusItineraryHalt> busHaltIterator() {
		return Iterators.concat(this.validHalts.iterator(), this.invalidHalts.iterator());
	}

	/** Replies the list of the bus halts of the bus itinerary.
	 *
	 * @return a list of bus halts
	 */
	@Pure
	public Iterable<BusItineraryHalt> busHalts() {
		final MultiCollection<BusItineraryHalt> halts = new MultiCollection<>();
		halts.addCollection(this.validHalts);
		halts.addCollection(this.invalidHalts);
		return Collections.unmodifiableCollection(halts);
	}

	/** Replies an array of the bus halts inside this itinerary.
	 * This function copy the internal data structures into the array.
	 *
	 * @return an array of the bus halts inside this itinerary.
	 */
	@Pure
	public BusItineraryHalt[] toBusHaltArray() {
		final BusItineraryHalt[] tab = new BusItineraryHalt[this.validHalts.size() + this.invalidHalts.size()];
		int i = 0;
		for (final BusItineraryHalt h : this.validHalts) {
			tab[i] = h;
			++i;
		}
		for (final BusItineraryHalt h : this.invalidHalts) {
			tab[i] = h;
			++i;
		}
		return tab;
	}

	/** Replies an array of the invalid bus halts inside this itinerary.
	 * This function copy the internal data structures into the array.
	 *
	 * @return an array of the invalid bus halts inside this itinerary.
	 */
	@Pure
	public BusItineraryHalt[] toInvalidBusHaltArray() {
		final BusItineraryHalt[] tab = new BusItineraryHalt[this.invalidHalts.size()];
		return this.invalidHalts.toArray(tab);
	}

	/** Replies an array of the valid bus halts inside this itinerary.
	 * This function copy the internal data structures into the array.
	 *
	 * @return an array of the invalid bus halts inside this itinerary.
	 */
	@Pure
	public BusItineraryHalt[] toValidBusHaltArray() {
		final BusItineraryHalt[] tab = new BusItineraryHalt[this.validHalts.size()];
		return this.validHalts.toArray(tab);
	}

	//-----------------------------------------------------
	// Segment functions
	//-----------------------------------------------------

	/**
	 * Replies the nearest road segment from this itinerary to the given point.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate
	 * @return the nearest road segment or <code>null</code> if none was found.
	 */
	@Pure
	public final RoadSegment getNearestRoadSegment(double x, double y) {
		return getNearestRoadSegment(new Point2d(x, y));
	}

	/**
	 * Replies the nearest road segment from this itinerary to the given point.
	 *
	 * @param point the point
	 * @return the nearest road segment or <code>null</code> if none was found.
	 */
	@Pure
	public final RoadSegment getNearestRoadSegment(GeoLocationPoint point) {
		return getNearestRoadSegment(point.getPoint());
	}

	/**
	 * Replies the nearest road segment from this itinerary to the given point.
	 *
	 * @param point the point.
	 * @return the nearest road segment or <code>null</code> if none was found.
	 */
	@Pure
	public RoadSegment getNearestRoadSegment(Point2D<?, ?> point) {
		assert point != null;
		double distance = Double.MAX_VALUE;
		RoadSegment nearestSegment = null;
		final Iterator<RoadSegment> iterator = this.roadSegments.roadSegments();
		RoadSegment segment;
		while (iterator.hasNext()) {
			segment = iterator.next();
			final double d = segment.distance(point);
			if (d < distance) {
				distance = d;
				nearestSegment = segment;
			}
		}
		return nearestSegment;
	}

	/**
	 * Add a road segment inside the bus itinerary.
	 *
	 * <p>This function is equivalent to {@link #addRoadSegment(RoadSegment, boolean)}
	 * with the boolean parameter equals to <code>true</code>.
	 *
	 * <p>This function try to connect the invalid itinerary halts to
	 * the added road segments.
	 *
	 * <p>This function ignores the automatic building of the loops.
	 *
	 * @param segment is the segment to add.
	 * @return <code>true</code> if the segment was added, otherwise <code>false</code>
	 * @see #addRoadSegments(RoadPath)
	 * @see #addRoadSegment(RoadSegment, boolean)
	 * @see #addRoadSegments(RoadPath, boolean)
	 */
	public final boolean addRoadSegment(RoadSegment segment) {
		return addRoadSegment(segment, true);
	}

	/**
	 * Add a road segment inside the bus itinerary.
	 *
	 * <p>This function ignores the automatic building of the loops.
	 *
	 * @param segment is the segment to add.
	 * @param autoConnectHalts indicates if the invalid itinery halts are trying to
	 *     be connected to the added segment.  If <code>true</code> {@link #putInvalidHaltsOnRoads(BusItineraryHalt...)}
	 *     is invoked.
	 * @return <code>true</code> if the segment was added, otherwise <code>false</code>
	 * @see #addRoadSegment(RoadSegment)
	 * @see #addRoadSegments(RoadPath)
	 * @see #addRoadSegments(RoadPath, boolean)
	 * @see #putInvalidHaltsOnRoads(BusItineraryHalt...)
	 */
	public final boolean addRoadSegment(RoadSegment segment, boolean autoConnectHalts) {
		return addRoadSegments(new RoadPath(segment), autoConnectHalts);
	}

	/**
	 * Add road segments inside the bus itinerary.
	 *
	 * <p>This function is equivalent to {@link #addRoadSegments(RoadPath, boolean)}
	 * with the boolean parameter equals to <code>true</code>.
	 *
	 * <p>This function try to connect the invalid itinerary halts to
	 * the added road segments.
	 *
	 * <p>This function ignores the automatic building of the loops.
	 *
	 * @param segments are the segments to add.
	 * @return <code>true</code> if the segment was added, otherwise <code>false</code>
	 * @see #addRoadSegment(RoadSegment)
	 * @see #addRoadSegment(RoadSegment, boolean)
	 * @see #addRoadSegments(RoadPath, boolean)
	 * @see #putInvalidHaltsOnRoads(BusItineraryHalt...)
	 */
	public final boolean addRoadSegments(RoadPath segments) {
		return addRoadSegments(segments, true);
	}

	/**
	 * Add road segments inside the bus itinerary.
	 *
	 * <p>This function ignores the automatic building of the loops.
	 *
	 * @param segments is the segment to add.
	 * @param autoConnectHalts indicates if the invalid itinery halts are trying to
	 *     be connected to the added segments. If <code>true</code> {@link #putInvalidHaltsOnRoads(BusItineraryHalt...)}
	 *     is invoked.
	 * @return <code>true</code> if the segment was added, otherwise <code>false</code>.
	 * @since 4.0
	 * @see #addRoadSegment(RoadSegment)
	 * @see #addRoadSegments(RoadPath)
	 * @see #addRoadSegment(RoadSegment, boolean)
	 * @see #putInvalidHaltsOnRoads(BusItineraryHalt...)
	 */
	public final boolean addRoadSegments(RoadPath segments, boolean autoConnectHalts) {
		return addRoadSegments(segments, autoConnectHalts, false);
	}

	/**
	 * Add road segments inside the bus itinerary.
	 *
	 * @param segments is the segment to add.
	 * @param autoConnectHalts indicates if the invalid itinery halts are trying to
	 *     be connected to the added segments. If <code>true</code> {@link #putInvalidHaltsOnRoads(BusItineraryHalt...)}
	 *     is invoked.
	 * @param enableLoopAutoBuild indicates if the automatic building of loop is enabled.
	 * @return <code>true</code> if the segment was added, otherwise <code>false</code>.
	 * @since 4.0
	 * @see #addRoadSegment(RoadSegment)
	 * @see #addRoadSegments(RoadPath)
	 * @see #addRoadSegment(RoadSegment, boolean)
	 * @see #putInvalidHaltsOnRoads(BusItineraryHalt...)
	 */
	public boolean addRoadSegments(RoadPath segments, boolean autoConnectHalts, boolean enableLoopAutoBuild) {
		if (segments == null || segments.isEmpty()) {
			return false;
		}

		BusItineraryHalt halt;
		RoadSegment sgmt;
		final Map<BusItineraryHalt, RoadSegment> haltMapping = new TreeMap<>((obj1, obj2) ->
			Integer.compare(System.identityHashCode(obj1), System.identityHashCode(obj2)));
		final Iterator<BusItineraryHalt> haltIterator = this.validHalts.iterator();
		while (haltIterator.hasNext()) {
			halt = haltIterator.next();
			sgmt = this.roadSegments.getRoadSegmentAt(halt.getRoadSegmentIndex());
			haltMapping.put(halt, sgmt);
		}

		final boolean isValidBefore = isValidPrimitive();

		final RoadPath changedPath = this.roadSegments.addAndGetPath(segments);

		if (changedPath != null) {

			if (enableLoopAutoBuild) {
				autoLoop(isValidBefore, changedPath, segments);
			}

			int nIdx;
			for (final Entry<BusItineraryHalt, RoadSegment> entry : haltMapping.entrySet()) {
				halt = entry.getKey();
				sgmt = entry.getValue();
				nIdx = this.roadSegments.indexOf(sgmt);
				halt.setRoadSegmentIndex(nIdx);
				halt.checkPrimitiveValidity();
			}

			final BusItineraryHalt[] tabV = new BusItineraryHalt[this.validHalts.size()];
			this.validHalts.toArray(tabV);

			this.validHalts.clear();

			for (final BusItineraryHalt busHalt : tabV) {
				assert busHalt != null && busHalt.isValidPrimitive();
				ListUtil.addIfAbsent(this.validHalts, VALID_HALT_COMPARATOR, busHalt);
			}

			if (this.roadNetwork == null) {
				final RoadNetwork network = segments.getFirstSegment().getRoadNetwork();
				this.roadNetwork = new WeakReference<>(network);
				network.addRoadNetworkListener(this);
			}

			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.SEGMENT_ADDED,
					segments.getLastSegment(),
					this.roadSegments.indexOf(segments.getLastSegment()),
					"shape", //$NON-NLS-1$
					null,
					null));

			// Try to connect the itinerary halts to
			// the road segments

			if (autoConnectHalts) {
				putInvalidHaltsOnRoads();
			} else {
				checkPrimitiveValidity();
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private void autoLoop(boolean isValidBefore, RoadPath changedPath, RoadPath originalSegments) {
		for (final RoadPath path : this.roadSegments) {
			if (path != changedPath) {
				// Search for connection points in the path

				// a is the first point of the changed segment
				// b is the second point of the changed segment
				// c is the end of
				final Iterator<CrossRoad> crossRoadIterator = path.crossRoads();
				CrossRoad a = null;
				CrossRoad b = null;
				CrossRoad c1 = null;
				CrossRoad c2 = null;
				boolean isDirectlyConnected = false;
				while (crossRoadIterator.hasNext()) {
					final CrossRoad crossRoad = crossRoadIterator.next();
					if (!isDirectlyConnected
							&& (originalSegments.getFirstPoint().equals(crossRoad.connectionPoint)
									|| originalSegments.getLastPoint().equals(crossRoad.connectionPoint))) {
						isDirectlyConnected = true;
					}
					if ((a == null || b == null)
							&& (changedPath.getFirstPoint().equals(crossRoad.connectionPoint)
							|| changedPath.getLastPoint().equals(crossRoad.connectionPoint))) {
						if (a == null) {
							a = crossRoad;
						} else if (b == null) {
							b = crossRoad;
						}
					}
					if (c1 == null && path.getFirstPoint().equals(crossRoad.connectionPoint)) {
						c1 = crossRoad;
					}
					if (c2 == null && path.getLastPoint().equals(crossRoad.connectionPoint)) {
						c2 = crossRoad;
					}
				}

				if (isDirectlyConnected && a != null && (!isValidBefore || b != null)) {
					if (b != null) {
						final RoadPath sharedPart = path.splitAt(a.exitingSegmentIndex);
						final RoadPath rest = sharedPart.splitAt(b.exitingSegmentIndex - a.exitingSegmentIndex);
						RoadPath.addPathToPath(path, sharedPart);
						RoadPath.addPathToPath(path, changedPath);
						RoadPath.addPathToPath(path, sharedPart);
						RoadPath.addPathToPath(path, rest);
						this.roadSegments.remove(changedPath);
						this.roadSegments.sync();
						return;
					}
					assert b == null;
					final CrossRoad c;
					if (c1 != null && c2 != null) {
						final double da1 = Math.abs(a.distance - c1.distance);
						final double da2 = Math.abs(a.distance - c2.distance);
						if (da1 < da2) {
							c = c1;
						} else {
							c = c2;
						}
					} else if (c1 != null) {
						c = c1;
					} else if (c2 != null) {
						c = c2;
					} else {
						c = null;
					}

					if (c != null) {

						// Only one end of the changed segment is on the path.
						if (a.distance < c.distance) {
							for (int i = c.enteringSegmentIndex; i >= a.exitingSegmentIndex; --i) {
								path.add(path.get(i));
							}
							RoadPath.addPathToPath(path, changedPath);
							this.roadSegments.remove(changedPath);
						} else {
							for (int i = c.exitingSegmentIndex; i <= a.enteringSegmentIndex; ++i) {
								path.add(path.get(i));
							}
							RoadPath.addPathToPath(path, changedPath);
							this.roadSegments.remove(changedPath);
						}
						this.roadSegments.sync();
						return;
					}
				}

			}
		}
	}

	/** Try to put the invalid halts on the roads traversed by the bus itinerary.
	 * This function is automatically invoked by {@link #addRoadSegments(RoadPath)},
	 * {@link #addRoadSegment(RoadSegment)}, and by {@link #addRoadSegments(RoadPath, boolean)},
	 * {@link #addRoadSegment(RoadSegment, boolean)} when the boolean parameter is <code>true</code>.
	 *
	 * <p>This function traverses all the candidate halts and tries to find the nearest road segment.
	 * If one was found, the bus halt is associated to the road segment.
	 * A bus itinerary halt is a candidate iff:<ol>
	 * <li>it is invalid; and</li>
	 * <li>it is binded to a valid bus stop; and</li>
	 * <li><ol>
	 * 		<li>the size of <var>restrictionList</var> is zero; or</li>
	 * 		<li>the list <var>restrictionList</var> contains the halt.</li>
	 * 	</ol></li>
	 * </ol>
	 *
	 * @param restrictionList is the list of the bus itinerary halts that can be binded.
	 * @since 4.0
	 */
	public void putInvalidHaltsOnRoads(BusItineraryHalt... restrictionList) {
		// CAUTION: The connections must be buffered to avoid ConcurrentModificationException on this.invalidHalts
		final List<Triplet<BusItineraryHalt, Integer, Double>> connectableHalts = new ArrayList<>();

		for (final BusItineraryHalt halt : this.invalidHalts) {
			final BusStop stop = halt.getBusStop();
			if (stop != null && (restrictionList == null || restrictionList.length == 0
					|| ArrayUtil.contains(halt, restrictionList))) {
				final Point2d position = stop.getPosition2D();
				if (position != null) {
					RoadSegment nearestSegment = null;
					double smallerDistance = Double.POSITIVE_INFINITY;
					int nearestSegmentIndex = -1;
					for (int i = 0; i < this.roadSegments.getRoadSegmentCount(); ++i) {
						final RoadSegment segment = this.roadSegments.getRoadSegmentAt(i);
						assert segment != null;
						final double distance = segment.distance(position);
						if (distance < smallerDistance) {
							smallerDistance = distance;
							nearestSegment = segment;
							nearestSegmentIndex = i;
						}
					}
					if (nearestSegment != null) {
						final Point1d pos = nearestSegment.getNearestPosition(position);
						if (pos != null) {
							assert nearestSegmentIndex >= 0;
							connectableHalts.add(new Triplet<>(
									halt,
									Integer.valueOf(nearestSegmentIndex),
									Double.valueOf(pos.getCurvilineCoordinate())));
						}
					}
				}
			}
		}

		BusItineraryHalt halt;
		boolean shapeChanged = false;

		for (final Triplet<BusItineraryHalt, Integer, Double> triplet : connectableHalts) {
			halt = triplet.getA();
			halt.setRoadSegmentIndex(triplet.getB().intValue());
			halt.setPositionOnSegment(triplet.getC().doubleValue());
			halt.checkPrimitiveValidity();
			shapeChanged = true;
		}

		if (shapeChanged) {
			fireShapeChanged(new BusChangeEvent(this,
					BusChangeEventType.ITINERARY_CHANGED,
					null,
					-1,
					"shape", //$NON-NLS-1$
					null,
					null));
		}

		checkPrimitiveValidity();
	}

	/** Put the given bus itinerary halt on the nearest
	 * point on road.
	 *
	 * @param halt is the halt to put on the road.
	 * @param road is the road.
	 * @return <code>false</code> if the road was not found;
	 *     otherwise <code>true</code>. If the bus halt is not binded
	 *     to a valid bus stop, replies <code>true</code> also.
	 */
	@SuppressWarnings("checkstyle:nestedifdepth")
	public boolean putHaltOnRoad(BusItineraryHalt halt, RoadSegment road) {
		if (contains(halt)) {
			final BusStop stop = halt.getBusStop();
			if (stop != null) {
				final Point2d stopPosition = stop.getPosition2D();
				if (stopPosition != null) {
					final int idx = indexOf(road);
					if (idx >= 0) {
						final Point1d pos = road.getNearestPosition(stopPosition);
						if (pos != null) {
							halt.setRoadSegmentIndex(idx);
							halt.setPositionOnSegment(pos.getCurvilineCoordinate());
							halt.checkPrimitiveValidity();
							fireShapeChanged(new BusChangeEvent(this,
									BusChangeEventType.ITINERARY_CHANGED,
									null,
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
			}
		}
		return true;
	}

	/** Put the given bus itinerary halt on the
	 * nearest point on road depending on
	 * nearPoint position.
	 *
	 * @param halt is the halt to put on the road.
	 * @param road is the road.
	 * @param nearPoint the near point from road
	 * @return <code>false</code> if the road was not found;
	 *     otherwise <code>true</code>. If the bus halt is not binded
	 *     to a valid bus stop, replies <code>true</code> also.
	 */
	@SuppressWarnings("checkstyle:nestedifdepth")
	public boolean putHaltOnRoad(BusItineraryHalt halt, RoadSegment road, Point2D<?, ?> nearPoint) {
		if (contains(halt)) {
			final BusStop stop = halt.getBusStop();
			if (stop != null) {
				if (nearPoint != null) {
					final int idx = indexOf(road);
					if (idx >= 0) {
						final Point1d pos = road.getNearestPosition(nearPoint);
						if (pos != null) {
							halt.setRoadSegmentIndex(idx);
							halt.setPositionOnSegment(pos.getCurvilineCoordinate());
							halt.checkPrimitiveValidity();
							fireShapeChanged(new BusChangeEvent(this,
									BusChangeEventType.ITINERARY_CHANGED,
									null,
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
			}
		}
		return true;
	}

	/**
	 * Remove all the road segments from the current itinerary.
	 *
	 * <p>All the bus halts will also be removed.
	 */
	public void removeAllRoadSegments() {
		for (final BusItineraryHalt halt : this.invalidHalts) {
			halt.setRoadSegmentIndex(-1);
			halt.setPositionOnSegment(Float.NaN);
			halt.checkPrimitiveValidity();
		}

		final BusItineraryHalt[] halts = new BusItineraryHalt[this.validHalts.size()];
		this.validHalts.toArray(halts);
		for (final BusItineraryHalt halt : halts) {
			halt.setRoadSegmentIndex(-1);
			halt.setPositionOnSegment(Float.NaN);
			halt.checkPrimitiveValidity();
		}

		if (this.roadNetwork != null) {
			final RoadNetwork network = this.roadNetwork.get();
			if (network != null) {
				network.removeRoadNetworkListener(this);
			}
			this.roadNetwork = null;
		}
		this.roadSegments.clear();
		fireShapeChanged(new BusChangeEvent(this,
				BusChangeEventType.ALL_SEGMENTS_REMOVED,
				null,
				-1,
				"shape", //$NON-NLS-1$
				null,
				null));
		checkPrimitiveValidity();
	}

	/**
	 * Remove a road segment from this itinerary.
	 *
	 * <p>The bus halts on the segment will also be removed.
	 *
	 * @param segmentIndex is the index of the segment to remove.
	 * @return <code>true</code> if the segment was successfully removed, otherwise <code>false</code>
	 */
	public boolean removeRoadSegment(int segmentIndex) {
		if (segmentIndex >= 0 && segmentIndex < this.roadSegments.getRoadSegmentCount()) {
			final RoadSegment segment = this.roadSegments.getRoadSegmentAt(segmentIndex);
			if (segment != null) {
				//
				// Invalidate the bus halts on the segment
				//
				final Map<BusItineraryHalt, RoadSegment> segmentMap = new TreeMap<>((obj1, obj2) ->
					Integer.compare(System.identityHashCode(obj1), System.identityHashCode(obj2)));
				final Iterator<BusItineraryHalt> haltIterator = this.validHalts.iterator();
				while (haltIterator.hasNext()) {
					final BusItineraryHalt halt = haltIterator.next();
					final int sgmtIndex = halt.getRoadSegmentIndex();
					if (sgmtIndex == segmentIndex) {
						segmentMap.put(halt, null);
					} else {
						final RoadSegment sgmt = this.roadSegments.getRoadSegmentAt(sgmtIndex);
						segmentMap.put(halt, sgmt);
					}
				}

				//
				// Remove the road segment itself on the segment
				//
				this.roadSegments.removeRoadSegmentAt(segmentIndex);

				//
				// Force the road segment indexes
				//
				for (final Entry<BusItineraryHalt, RoadSegment> entry : segmentMap.entrySet()) {
					final BusItineraryHalt halt = entry.getKey();
					final RoadSegment sgmt = entry.getValue();
					if (sgmt == null) {
						halt.setRoadSegmentIndex(-1);
						halt.setPositionOnSegment(Float.NaN);
						halt.checkPrimitiveValidity();
					} else {
						final int sgmtIndex = halt.getRoadSegmentIndex();
						final int idx = this.roadSegments.indexOf(sgmt);
						if (idx != sgmtIndex) {
							halt.setRoadSegmentIndex(idx);
							halt.checkPrimitiveValidity();
						}
					}
				}

				//
				// Change the road network reference
				//
				if (this.roadSegments.isEmpty() && this.roadNetwork != null) {
					final RoadNetwork network = this.roadNetwork.get();
					if (network != null) {
						network.removeRoadNetworkListener(this);
					}
					this.roadNetwork = null;
				}

				fireShapeChanged(new BusChangeEvent(this,
						BusChangeEventType.SEGMENT_REMOVED,
						segment,
						segmentIndex,
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
	 * Remove a road segment from this itinerary.
	 * This function remove all the road segment occurrences.
	 *
	 * <p>The bus halts on the segments will also be removed.
	 *
	 * <p>This function  tries to reconnect the paths that
	 * are previously attached to the remove segment.
	 * See {@link #removeRoadSegment(RoadSegment, boolean)}
	 * for details.
	 *
	 * @param segment is the segment to remove.
	 * @return the collection of the indexes of the removed occurrences;
	 *     never null.
	 * @see #removeRoadSegment(RoadSegment, boolean)
	 */
	public Set<Integer> removeRoadSegment(RoadSegment segment) {
		return removeRoadSegment(segment, true);
	}

	/**
	 * Remove a road segment from this itinerary.
	 * This function remove all the road segment occurrences.
	 *
	 * <p>The bus halts on the segments will also be removed.
	 *
	 * <p>The parameter <var>tryToReconnect</var> indicates if the
	 * function tries to reconnect the paths previously attached
	 * to the removed segments.
	 *
	 * @param segment is the segment to remove.
	 * @param tryToReconnect indicates if the path connections should be preserved
	 *     when possible.
	 * @return the collection of the indexes of the removed occurrences;
	 *     never null.
	 * @see #removeRoadSegment(RoadSegment)
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public Set<Integer> removeRoadSegment(RoadSegment segment, boolean tryToReconnect) {
		final IntegerList removedIndexes = new IntegerList();
		if (segment != null) {
			final Map<BusItineraryHalt, RoadSegment> segmentMap = new TreeMap<>((obj1, obj2) ->
				Integer.compare(System.identityHashCode(obj1), System.identityHashCode(obj2)));

			// Save the segment-halt binding
			final Iterator<BusItineraryHalt> haltIterator = this.validHalts.iterator();
			while (haltIterator.hasNext()) {
				final BusItineraryHalt halt = haltIterator.next();
				final int sgmtIndex = halt.getRoadSegmentIndex();
				final RoadSegment s = this.roadSegments.getRoadSegmentAt(sgmtIndex);
				if (s.equals(segment)) {
					segmentMap.put(halt, null);
				} else {
					segmentMap.put(halt, s);
				}
			}

			// Removed the segments
			int segmentIndex = 0;
			final Iterator<RoadSegment> segmentIterator = this.roadSegments.roadSegments();
			while (segmentIterator.hasNext()) {
				final RoadSegment s = segmentIterator.next();
				if (s.equals(segment)) {
					segmentIterator.remove();
					removedIndexes.add(Integer.valueOf(segmentIndex));
				}
				++segmentIndex;
			}

			// Try to reconnect the segments
			if (tryToReconnect) {
				for (int i = 0; i < this.roadSegments.size() - 1; ++i) {
					final RoadPath p1 = this.roadSegments.getRoadPathAt(i);
					int j = i + 1;
					while (j < this.roadSegments.size()) {
						final RoadPath p2 = this.roadSegments.getRoadPathAt(j);
						RoadSegment seg = p2.getConnectableSegmentToFirstPoint(p1);
						final Iterator<RoadSegment> iterator;
						if (seg != null) {
							iterator = p2.iterator();
						} else {
							seg = p2.getConnectableSegmentToLastPoint(p1);
							if (seg != null) {
								iterator = ListUtil.reverseIterator(p2);
							} else {
								iterator = null;
							}
						}
						if (iterator != null) {
							assert seg != null;
							assert iterator.hasNext();
							final RoadSegment sgmt = iterator.next();
							// avoid to connect paths that cause to go back on the same segment.
							if (!seg.equals(sgmt)) {
								p1.add(sgmt);
								while (iterator.hasNext()) {
									p1.add(iterator.next());
								}
								this.roadSegments.remove(p2);
							} else {
								++j;
							}
						} else {
							++j;
						}
					}
				}
				this.roadSegments.sync();
			}

			if (!removedIndexes.isEmpty()) {
				//
				// Force the road segment indexes
				//
				for (final Entry<BusItineraryHalt, RoadSegment> entry : segmentMap.entrySet()) {
					final BusItineraryHalt halt = entry.getKey();
					final RoadSegment s = entry.getValue();
					if (s == null) {
						halt.setRoadSegmentIndex(-1);
						halt.setPositionOnSegment(Float.NaN);
						halt.checkPrimitiveValidity();
					} else {
						final int sgmtIndex = halt.getRoadSegmentIndex();
						final int idx = this.roadSegments.indexOf(s);
						if (idx != sgmtIndex) {
							halt.setRoadSegmentIndex(idx);
							halt.checkPrimitiveValidity();
						}
					}
				}

				//
				// Change the road network reference
				//
				if (this.roadSegments.isEmpty() && this.roadNetwork != null) {
					final RoadNetwork network = this.roadNetwork.get();
					if (network != null) {
						network.removeRoadNetworkListener(this);
					}
					this.roadNetwork = null;
				}

				//
				// Change the road network reference
				//
				for (final Integer index : removedIndexes) {
					fireShapeChanged(new BusChangeEvent(this,
							BusChangeEventType.SEGMENT_REMOVED,
							segment,
							index.intValue(),
							"shape", //$NON-NLS-1$
							null,
							null));
				}
				checkPrimitiveValidity();
			}
		}
		return removedIndexes;
	}

	/**
	 * Replies the count of road segments into this itinerary.
	 *
	 * @return the count of road segments into this itinerary.
	 */
	@Pure
	public int getRoadSegmentCount() {
		return this.roadSegments.getRoadSegmentCount();
	}

	/**
	 * Replies the segment at the specified index.
	 *
	 * @param index the index.
	 * @return a road segment
	 */
	@Pure
	public RoadSegment getRoadSegmentAt(int index) {
		return this.roadSegments.getRoadSegmentAt(index);
	}

	/** Replies the list of the road segments of the bus itinerary.
	 *
	 * @return a list of road segments
	 */
	@Pure
	public Iterator<RoadSegment> roadSegmentsIterator() {
		return this.roadSegments.roadSegments();
	}

	/** Replies the list of the road segments of the bus itinerary.
	 *
	 * @return a list of road segments
	 */
	@Pure
	public Iterable<RoadSegment> roadSegments() {
		return () -> roadSegmentsIterator();
	}

	/** Replies a road path that is containing all road segments
	 * of this itinerary when they are correctly connected.
	 *
	 * <p>Connected Road segments that are covered by the itinerary are put into the
	 * same cluster of road segments. If no path between two road segments,
	 * covered by the itinerary, cannot be found without discontinuity; the
	 * two road segments are inside two different clusters of road segments.
	 *
	 * @return a road path for this itinerary, or <code>null</code> if
	 *     the road segments for this itinerary ar not well connected.
	 */
	@Pure
	public RoadPath getRoadPath() {
		return this.roadSegments.toRoadPath();
	}

	/** Replies the unconnected road paths which are used by this bus itinerary.
	 *
	 * <p>Connected Road segments that are covered by the itinerary are put into the
	 * same cluster of road segments. If no path between two road segments,
	 * covered by the itinerary, cannot be found without discontinuity; the
	 * two road segments are inside two different clusters of road segments.
	 *
	 * @return a set of road paths for this itinerary.
	 */
	@Pure
	public Collection<RoadPath> getRoadPaths() {
		return Collections.unmodifiableCollection(this.roadSegments);
	}

	/** Replies the number of road-path clusters for this itinerary.
	 *
	 * <p>Connected Road segments that are covered by the itinerary are put into the
	 * same cluster of road segments. If no path between two road segments,
	 * covered by the itinerary, cannot be found without discontinuity; the
	 * two road segments are inside two different clusters of road segments.
	 *
	 * @return the number of road-path clusters.
	 */
	@Pure
	public int getRoadPathCount() {
		return this.roadSegments.size();
	}

	//----------------------------------------------
	// Event Management
	//----------------------------------------------

	private void onBusHaltChanged(BusItineraryHalt halt) {
		if (halt.getContainer() == this) {
			final boolean oldValidity = !ListUtil.contains(this.invalidHalts, INVALID_HALT_COMPARATOR, halt);
			final boolean currentValidity = halt.isValidPrimitive();
			if (oldValidity != currentValidity) {
				if (currentValidity) {
					ListUtil.remove(this.invalidHalts, INVALID_HALT_COMPARATOR, halt);
					ListUtil.addIfAbsent(this.validHalts, VALID_HALT_COMPARATOR, halt);
				} else {
					this.validHalts.remove(halt);
					ListUtil.addIfAbsent(this.invalidHalts, INVALID_HALT_COMPARATOR, halt);
				}
				checkPrimitiveValidity();
			}
		}
	}

	@Override
	public void onBusPrimitiveChanged(BusChangeEvent event) {
		final Object source = event.getSource();
		if (source instanceof BusItineraryHalt) {
			onBusHaltChanged((BusItineraryHalt) source);
		}
		super.onBusPrimitiveChanged(event);
	}

	@Override
	public void onBusPrimitiveShapeChanged(BusChangeEvent event) {
		final Object source = event.getSource();
		if (source instanceof BusItineraryHalt) {
			onBusHaltChanged((BusItineraryHalt) source);
		}
		super.onBusPrimitiveShapeChanged(event);
	}

	@Override
	public void onRoadSegmentAdded(RoadNetwork network, RoadSegment segment) {
		resetBoundingBox();
	}

	@Override
	public void onRoadSegmentChanged(RoadNetwork network, RoadSegment segment) {
		resetBoundingBox();
	}

	@Override
	public void onRoadSegmentRemoved(RoadNetwork network, RoadSegment segment) {
		removeRoadSegment(segment);
		resetBoundingBox();
	}

	/** Internal comparator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class BusItineraryHaltComparator implements Comparator<BusItineraryHalt> {

		private final boolean isValid;

		/** Constructor.
		 *
		 * @param isValid validity flag.
		 */
		BusItineraryHaltComparator(boolean isValid) {
			this.isValid = isValid;
		}

		@Override
		@Pure
		public int compare(BusItineraryHalt o1, BusItineraryHalt o2) {
			if (this.isValid) {
				return compareValidHalts(o1, o2);
			}
			return compareInvalidHalts(o1, o2);
		}

		/**
		 * Compare valid halts.
		 * @param o1 the first object to be compared.
		 * @param o2 the second object to be compared.
		 * @return a negative integer, zero, or a positive integer as the
		 * 	       first argument is less than, equal to, or greater than the
		 *	       second.
		 */
		@SuppressWarnings("static-method")
		@Pure
		public int compareValidHalts(BusItineraryHalt o1, BusItineraryHalt o2) {
			if (o1 == o2) {
				return 0;
			}
			assert o1 != null;
			assert o2 != null;

			final int s1 = o1.getRoadSegmentIndex();
			assert s1 >= 0;

			final int s2 = o2.getRoadSegmentIndex();
			assert s2 >= 0;

			final int cmp = s1 - s2;
			if (cmp != 0) {
				return cmp;
			}

			final double p1 = o1.getPositionOnSegment();
			final double p2 = o2.getPositionOnSegment();
			assert !Double.isNaN(p1) && !Double.isNaN(p2);
			return Double.compare(p1, p2);
		}

		/**
		 * Compare invalid halts.
		 * @param o1 the first object to be compared.
		 * @param o2 the second object to be compared.
		 * @return a negative integer, zero, or a positive integer as the
		 * 	       first argument is less than, equal to, or greater than the
		 *	       second.
		 */
		@SuppressWarnings("static-method")
		@Pure
		public int compareInvalidHalts(BusItineraryHalt o1, BusItineraryHalt o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MAX_VALUE;
			}
			if (o2 == null) {
				return Integer.MIN_VALUE;
			}
			return o1.getInvalidListIndex() - o2.getInvalidListIndex();
		}

	} // class BusItineraryHaltComparator

}

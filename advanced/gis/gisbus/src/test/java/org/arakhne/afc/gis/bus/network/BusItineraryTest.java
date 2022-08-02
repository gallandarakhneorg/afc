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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.path.RoadPath;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class BusItineraryTest extends AbstractTestCase {

	private final TestEventHandler eventHandler = new TestEventHandler();
	private BusItinerary itinerary;
	private BusItineraryHalt halt1;
	private BusItineraryHalt halt2;
	private BusItineraryHalt halt3;
	private BusStop stop1;
	private BusStop stop2;
	private BusStop stop3;
	private StandardRoadNetwork network;
	private BusNetwork busNetwork;
	private BusLine busLine;
	private RoadPolyline segment1;
	private RoadPolyline segment2;
	private RoadPolyline segment3;

	@BeforeEach
	public void setUp() throws Exception {
		this.network = new StandardRoadNetwork(new Rectangle2d(-2000, -2000, 4000, 4000));
		this.busNetwork = new BusNetwork(this.network);
		this.busLine = new BusLineStub(this.busNetwork);
		this.itinerary = new BusItinerary();
		this.halt1 = new BusItineraryHalt(this.itinerary, "HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt2 = new BusItineraryHalt(this.itinerary, "HALT2", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt3 = new BusItineraryHalt(this.itinerary, "HALT3", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.stop1 = new BusStopStub(this.busNetwork, "STOP1", new GeoLocationPoint(10., 10.)); //$NON-NLS-1$
		this.stop2 = new BusStopStub(this.busNetwork, "STOP2", new GeoLocationPoint(-10., -10.)); //$NON-NLS-1$
		this.stop3 = new BusStopStub(this.busNetwork, "STOP3", new GeoLocationPoint(-5., 5.)); //$NON-NLS-1$
		this.segment1 = new RoadPolyline();
		this.segment1.addPoint(10., 10.);
		this.segment1.addPoint(100., 100.);
		this.segment1.addPoint(200., 100.);
		this.segment2 = new RoadPolyline();
		this.segment2.addPoint(-10., 10.);
		this.segment2.addPoint(100., -100.);
		this.segment2.addPoint(200., 100.);
		// segment3 is added to enlarge the road network bounds
		this.segment3 = new RoadPolyline();
		this.segment3.addPoint(-1000., 1000.);
		this.segment3.addPoint(1000., -1000.);
		this.segment3.addPoint(2000., 1000.);
		this.network.addRoadSegment(this.segment1);
		this.network.addRoadSegment(this.segment2);
		this.network.addRoadSegment(this.segment3);
		this.itinerary.setContainer(this.busLine);
		this.itinerary.addBusChangeListener(this.eventHandler);
		this.eventHandler.clear();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.itinerary.removeBusChangeListener(this.eventHandler);
		this.network = null;
		this.segment1 = this.segment2 = this.segment3 = null;
		this.stop1 = this.stop2 = null;
		this.halt1 = this.halt2 = this.halt3 = null;
		this.itinerary = null;
		this.busLine = null;
		this.busNetwork = null;
	}

	private void initTest() {
		this.busNetwork.addBusStop(this.stop1);
		this.busNetwork.addBusStop(this.stop2);
		this.itinerary.addBusHalt(this.halt1,0);
		this.halt1.setBusStop(this.stop1);
		this.itinerary.addBusHalt(this.halt2,1);
		this.halt2.setBusStop(this.stop2);
		this.itinerary.addRoadSegment(this.segment1, false);
		this.itinerary.addRoadSegment(this.segment2, false);
		this.halt1.setRoadSegmentIndex(0);
		this.halt1.setPositionOnSegment(1f);
		this.halt2.setRoadSegmentIndex(1);
		this.halt2.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		this.halt2.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();
		assertTrue(this.halt1.isValidPrimitive());
		assertTrue(this.halt2.isValidPrimitive());
		assertTrue(this.itinerary.isValidPrimitive());
	}

	private void initTest2() {
		this.busNetwork.addBusStop(this.stop1);
		this.busNetwork.addBusStop(this.stop2);
		this.itinerary.addBusHalt(this.halt1,0);
		this.halt1.setBusStop(this.stop1);
		this.itinerary.addBusHalt(this.halt2,1);
		this.itinerary.addRoadSegment(this.segment1, false);
		this.itinerary.addRoadSegment(this.segment2, false);
		this.halt1.setRoadSegmentIndex(0);
		this.halt1.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		this.halt2.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();
		assert(this.halt1.isValidPrimitive()) : this.halt1.getInvalidityReason();
		assert(!this.halt2.isValidPrimitive());
		assert(!this.itinerary.isValidPrimitive());
	}

	private void initTest3() {
		this.busNetwork.addBusStop(this.stop1);
		this.busNetwork.addBusStop(this.stop2);
		this.itinerary.addBusHalt(this.halt1,0);
		this.halt1.setBusStop(this.stop1);
		this.itinerary.addBusHalt(this.halt2,1);
		this.halt2.setBusStop(this.stop2);
		assertTrue(this.itinerary.addRoadSegment(this.segment1, false));
		assertTrue(this.itinerary.addRoadSegment(this.segment2, false));
		assertTrue(this.itinerary.addRoadSegment(this.segment2, false));
		assertTrue(this.itinerary.addRoadSegment(this.segment1, false));
		this.halt1.setRoadSegmentIndex(0);
		this.halt1.setPositionOnSegment(1f);
		this.halt2.setRoadSegmentIndex(1);
		this.halt2.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		this.halt2.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();
		assertTrue(this.halt1.isValidPrimitive());
		assertTrue(this.halt2.isValidPrimitive());
		assertTrue(!this.itinerary.isValidPrimitive());
	}

	private void initTest4() {
		this.busNetwork.addBusStop(this.stop1);
		this.busNetwork.addBusStop(this.stop2);
		this.itinerary.addBusHalt(this.halt1,0);
		this.halt1.setBusStop(this.stop1);
		this.itinerary.addBusHalt(this.halt2,1);
		this.halt2.setBusStop(this.stop2);
		assertTrue(this.itinerary.addRoadSegment(this.segment1, false));
		assertTrue(this.itinerary.addRoadSegment(this.segment2, false));
		assertTrue(this.itinerary.addRoadSegment(this.segment2, false));
		assertTrue(this.itinerary.addRoadSegment(this.segment1, false));
		this.halt1.setRoadSegmentIndex(0);
		this.halt1.setPositionOnSegment(1f);
		this.halt2.setRoadSegmentIndex(3);
		this.halt2.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		this.halt2.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();
		assertTrue(this.halt1.isValidPrimitive());
		assertTrue(this.halt2.isValidPrimitive());
		assertTrue(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testGetBusNetwork() {
		assertSame(this.busNetwork, this.itinerary.getBusNetwork());
	}

	@Test
	public void testIsValidPrimitive() {
		this.busNetwork.addBusStop(this.stop1);
		this.busNetwork.addBusStop(this.stop2);

		assertFalse(this.itinerary.isValidPrimitive());

		this.itinerary.addBusHalt(this.halt1,0);
		assertFalse(this.itinerary.isValidPrimitive());

		this.halt1.setBusStop(this.stop1);
		assertFalse(this.itinerary.isValidPrimitive());

		this.itinerary.addBusHalt(this.halt2,1);
		assertFalse(this.itinerary.isValidPrimitive());

		this.halt2.setBusStop(this.stop2);
		assertFalse(this.itinerary.isValidPrimitive());

		this.itinerary.addRoadSegment(this.segment1, false);
		assertFalse(this.itinerary.isValidPrimitive());

		this.itinerary.addRoadSegment(this.segment2, false);
		assertFalse(this.itinerary.isValidPrimitive());

		this.halt1.setRoadSegmentIndex(0);
		this.halt1.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();
		assertFalse(this.itinerary.isValidPrimitive());

		this.halt2.setRoadSegmentIndex(1);
		this.halt2.checkPrimitiveValidity();
		assertFalse(this.itinerary.isValidPrimitive());

		this.halt1.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		assertFalse(this.itinerary.isValidPrimitive());

		this.halt2.setPositionOnSegment(1f);
		this.halt2.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();
		assertTrue(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testIterator() {
		Iterator<BusItineraryHalt> iterator;
		initTest();

		iterator = this.itinerary.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.halt2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetRoadSegmentDirectionInt() {
		initTest();
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.itinerary.getRoadSegmentDirection(0));
		assertEquals(Direction1D.REVERTED_DIRECTION, this.itinerary.getRoadSegmentDirection(1));
	}

	@Test
	public void testInsertBeforeHalt() {
		initTest4();
		BusItineraryHalt halt = this.itinerary.insertBusHaltBefore(this.halt2, "before", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertTrue(halt.getInvalidListIndex() < this.halt2.getInvalidListIndex());
		assertTrue(halt.getInvalidListIndex() > this.halt1.getInvalidListIndex());

		assertFalse(this.itinerary.isValidPrimitive());

		this.busNetwork.addBusStop(this.stop3);
		halt.setBusStop(this.stop3);
		halt.setRoadSegmentIndex(2);
		halt.setPositionOnSegment(1f);
		halt.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();

		assertTrue(halt.isValidPrimitive());
		assertTrue(this.itinerary.isValidPrimitive());

	}

	@Test
	public void testInsertAfterHalt() {
		initTest4();
		BusItineraryHalt halt = this.itinerary.insertBusHaltAfter(this.halt1, "after", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertTrue(halt.getInvalidListIndex() < this.halt2.getInvalidListIndex());
		assertTrue(halt.getInvalidListIndex() > this.halt1.getInvalidListIndex());

		assertFalse(this.itinerary.isValidPrimitive());

		this.busNetwork.addBusStop(this.stop3);
		halt.setBusStop(this.stop3);
		halt.setRoadSegmentIndex(1);
		halt.setPositionOnSegment(1f);
		halt.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();

		assertTrue(halt.isValidPrimitive());
		assertTrue(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testDoubleInsertAfterHalt() {
		initTest4();
		BusItineraryHalt haltAfter = this.itinerary.insertBusHaltAfter(this.halt1, "after", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertTrue(haltAfter.getInvalidListIndex() < this.halt2.getInvalidListIndex());
		assertTrue(haltAfter.getInvalidListIndex() > this.halt1.getInvalidListIndex());

		BusItineraryHalt haltDoubleAfter = this.itinerary.insertBusHaltAfter(haltAfter, "after-after", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertTrue(haltDoubleAfter.getInvalidListIndex() < this.halt2.getInvalidListIndex());
		assertTrue(haltDoubleAfter.getInvalidListIndex() > haltAfter.getInvalidListIndex());
		assertTrue(haltAfter.getInvalidListIndex() > this.halt1.getInvalidListIndex());

		assertFalse(this.itinerary.isValidPrimitive());

		this.busNetwork.addBusStop(this.stop3);
		haltAfter.setBusStop(this.stop3);
		haltAfter.setRoadSegmentIndex(1);
		haltDoubleAfter.setBusStop(this.stop3);
		haltDoubleAfter.setRoadSegmentIndex(2);

		haltAfter.setPositionOnSegment(1f);
		haltDoubleAfter.setPositionOnSegment(1f);
		haltAfter.checkPrimitiveValidity();
		haltDoubleAfter.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();

		assertTrue(haltAfter.isValidPrimitive());
		assertTrue(haltDoubleAfter.isValidPrimitive());
		assertTrue(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testDoubleInsertBeforeHalt() {
		initTest4();
		BusItineraryHalt haltBefore = this.itinerary.insertBusHaltBefore(this.halt2, "Before", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertTrue(haltBefore.getInvalidListIndex() < this.halt2.getInvalidListIndex());
		assertTrue(haltBefore.getInvalidListIndex() > this.halt1.getInvalidListIndex());

		BusItineraryHalt haltDoubleBefore = this.itinerary.insertBusHaltBefore(haltBefore, "Before-Before", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertTrue(haltBefore.getInvalidListIndex() < this.halt2.getInvalidListIndex());
		assertTrue(haltDoubleBefore.getInvalidListIndex() < haltBefore.getInvalidListIndex());
		assertTrue(haltDoubleBefore.getInvalidListIndex() > this.halt1.getInvalidListIndex());

		assertFalse(this.itinerary.isValidPrimitive());

		this.busNetwork.addBusStop(this.stop3);
		haltBefore.setBusStop(this.stop3);
		haltBefore.setRoadSegmentIndex(2);
		haltDoubleBefore.setBusStop(this.stop3);
		haltDoubleBefore.setRoadSegmentIndex(1);

		haltBefore.setPositionOnSegment(1f);
		haltDoubleBefore.setPositionOnSegment(1f);
		haltBefore.checkPrimitiveValidity();
		haltDoubleBefore.checkPrimitiveValidity();
		this.itinerary.checkPrimitiveValidity();

		assertTrue(haltBefore.isValidPrimitive());
		assertTrue(haltDoubleBefore.isValidPrimitive());
		assertTrue(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testInvert() {
		Iterator<BusItineraryHalt> iterator;
		Iterator<RoadSegment> iterator2;
		initTest();

		this.eventHandler.clear();
		this.itinerary.invert();

		iterator = this.itinerary.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertFalse(iterator.hasNext());

		iterator2 = this.itinerary.roadSegmentsIterator();
		assertTrue(iterator2.hasNext());
		assertSame(this.segment2, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertSame(this.segment1, iterator2.next());
		assertFalse(iterator2.hasNext());

		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_INVERTED, this.itinerary);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		this.itinerary.invert();

		iterator = this.itinerary.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.halt2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator2 = this.itinerary.roadSegmentsIterator();
		assertTrue(iterator2.hasNext());
		assertSame(this.segment1, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertSame(this.segment2, iterator2.next());
		assertFalse(iterator2.hasNext());

		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_INVERTED, this.itinerary);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testHasBusHaltOnSegmentRoadSegment() {
		assertFalse(this.itinerary.hasBusHaltOnSegment(this.segment1));
		assertFalse(this.itinerary.hasBusHaltOnSegment(this.segment2));

		initTest2();

		assertTrue(this.itinerary.hasBusHaltOnSegment(this.segment1));
		assertFalse(this.itinerary.hasBusHaltOnSegment(this.segment2));
	}

	@Test
	public void testGetBusHaltsOnSegmentRoadSegment() {
		List<BusItineraryHalt> halts;

		halts = this.itinerary.getBusHaltsOnSegment(this.segment1);
		assertNotNull(halts);
		assertTrue(halts.isEmpty());

		halts = this.itinerary.getBusHaltsOnSegment(this.segment2);
		assertNotNull(halts);
		assertTrue(halts.isEmpty());

		initTest2();

		halts = this.itinerary.getBusHaltsOnSegment(this.segment1);
		assertNotNull(halts);
		assertEquals(1, halts.size());
		assertTrue(halts.contains(this.halt1));

		halts = this.itinerary.getBusHaltsOnSegment(this.segment2);
		assertNotNull(halts);
		assertTrue(halts.isEmpty());
	}

	@Test
	public void testGetLength_validBusStop() {
		this.busNetwork.addBusStop(this.stop1);
		this.busNetwork.addBusStop(this.stop2);

		assertEpsilonEquals(0., this.itinerary.getLength());

		this.itinerary.addBusHalt(this.halt1,0);
		this.itinerary.addRoadSegment(this.segment1);
		this.itinerary.addRoadSegment(this.segment2);

		assertEpsilonEquals(this.segment1.getLength()+this.segment2.getLength(), this.itinerary.getLength());

		this.halt1.setBusStop(this.stop1);
		this.itinerary.addBusHalt(this.halt2,1);
		this.halt2.setBusStop(this.stop2);
		this.halt1.setRoadSegmentIndex(0);
		this.halt1.checkPrimitiveValidity();
		this.halt2.setRoadSegmentIndex(1);
		this.halt2.checkPrimitiveValidity();
		this.halt1.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		this.halt2.setPositionOnSegment(1f);
		this.halt2.checkPrimitiveValidity();

		assertEpsilonEquals(this.segment1.getLength()+this.segment2.getLength()-2., this.itinerary.getLength());
	}

	@Test
	public void testGetLength_invalidBusStop() {
		assertEpsilonEquals(0., this.itinerary.getLength());

		this.itinerary.addBusHalt(this.halt1,0);
		this.itinerary.addRoadSegment(this.segment1);
		this.itinerary.addRoadSegment(this.segment2);

		assertEpsilonEquals(this.segment1.getLength()+this.segment2.getLength(), this.itinerary.getLength());

		this.halt1.setBusStop(this.stop1);
		this.itinerary.addBusHalt(this.halt2,1);
		this.halt2.setBusStop(this.stop2);
		this.halt1.setRoadSegmentIndex(0);
		this.halt1.checkPrimitiveValidity();
		this.halt2.setRoadSegmentIndex(1);
		this.halt2.checkPrimitiveValidity();
		this.halt1.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		this.halt2.setPositionOnSegment(1f);
		this.halt2.checkPrimitiveValidity();

		assertEpsilonEquals(this.segment1.getLength()+this.segment2.getLength(), this.itinerary.getLength());
	}

	@Test
	public void testGetDistanceBetweenBusHaltsIntInt() {
		double l1 = this.segment1.getLength();
		double l2 = this.segment1.getLength();
		double expected = (l1 - 1) + (l2 - 1);
		initTest();
		assertEpsilonEquals(expected, this.itinerary.getDistanceBetweenBusHalts(0, 1));
	}

	@Test
	public void testGetTroneonLengthInt() {
		double l1 = this.segment1.getLength();
		double l2 = this.segment1.getLength();
		double expected = (l1 - 1) + (l2 - 1);
		initTest();
		assertEpsilonEquals(expected, this.itinerary.getTroneonLength(0));
	}

	@Test
	public void testGetTroneonCount() {
		initTest();
		assertEquals(1, this.itinerary.getTroneonCount());
	}

	@Test
	public void testGetNearestBusHaltPoint2d() {
		initTest();
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(new Point2d(0., 0.)));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(new Point2d(200., 100.)));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(new Point2d(100., 100.)));
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(new Point2d(-100., -100.)));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(new Point2d(100., -100.)));
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(new Point2d(-100., 100.)));
	}

	@Test
	public void testGetNearestBusHaltGeoLocationPoint() {
		initTest();
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(new GeoLocationPoint(0., 0.)));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(new GeoLocationPoint(200., 100.)));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(new GeoLocationPoint(100., 100.)));
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(new GeoLocationPoint(-100., -100.)));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(new GeoLocationPoint(100., -100.)));
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(new GeoLocationPoint(-100., 100.)));
	}

	@Test
	public void testGetNearestBusHaltDoubleDouble() {
		initTest();
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(0., 0.));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(200., 100.));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(100., 100.));
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(-100., -100.));
		assertSame(this.halt1, this.itinerary.getNearestBusHalt(100., -100.));
		assertSame(this.halt2, this.itinerary.getNearestBusHalt(-100., 100.));
	}

	@Test
	public void testAddBusHaltStringBusItineraryHaltType() {
		initTest();

		this.eventHandler.clear();
		BusItineraryHalt h4 = this.itinerary.addBusHalt("HALT4", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertNotNull(h4);
		assertFalse(h4.isValidPrimitive());
		assertTrue(this.itinerary.contains(h4));
		assertFalse(this.itinerary.isValidPrimitive());
		assertEquals(2, this.itinerary.getValidBusHaltCount());
		assertEquals(1, this.itinerary.getInvalidBusHaltCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_ADDED, h4);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testAddBusHaltUUIDStringBusItineraryHaltType() {
		initTest();

		this.eventHandler.clear();
		BusItineraryHalt h4 = this.itinerary.addBusHalt(UUID.randomUUID(), "HALT4", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertNotNull(h4);
		assertFalse(h4.isValidPrimitive());
		assertTrue(this.itinerary.contains(h4));
		assertFalse(this.itinerary.isValidPrimitive());
		assertEquals(2, this.itinerary.getValidBusHaltCount());
		assertEquals(1, this.itinerary.getInvalidBusHaltCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_ADDED, h4);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveAllBusHalts() {
		initTest();
		assertTrue(this.itinerary.isValidPrimitive());

		this.eventHandler.clear();
		this.itinerary.removeAllBusHalts();

		Iterator<BusItineraryHalt> iterator = this.itinerary.iterator();
		assertFalse(iterator.hasNext());

		assertFalse(this.itinerary.isValidPrimitive());

		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ALL_ITINERARY_HALTS_REMOVED, this.itinerary);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusHaltBusItineraryHalt() {
		initTest();
		assertTrue(this.itinerary.isValidPrimitive());

		this.eventHandler.clear();
		assertTrue(this.itinerary.removeBusHalt(this.halt2));
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_REMOVED, this.halt2);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.itinerary.removeBusHalt(this.halt2));
		this.eventHandler.assertNoEvent();

		Iterator<BusItineraryHalt> iterator = this.itinerary.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertFalse(iterator.hasNext());

		assertFalse(this.itinerary.isValidPrimitive());

		this.eventHandler.clear();
		assertTrue(this.itinerary.removeBusHalt(this.halt1));
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_REMOVED, this.halt1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		iterator = this.itinerary.iterator();
		assertFalse(iterator.hasNext());

		assertFalse(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testRemoveBusHaltString() {
		initTest();
		assertTrue(this.itinerary.isValidPrimitive());

		this.eventHandler.clear();
		assertTrue(this.itinerary.removeBusHalt("HALT2")); //$NON-NLS-1$
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_REMOVED, this.halt2);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.itinerary.removeBusHalt("HALT2")); //$NON-NLS-1$
		this.eventHandler.assertNoEvent();

		Iterator<BusItineraryHalt> iterator = this.itinerary.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertFalse(iterator.hasNext());

		assertFalse(this.itinerary.isValidPrimitive());

		this.eventHandler.clear();
		assertTrue(this.itinerary.removeBusHalt("HALT1")); //$NON-NLS-1$
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_REMOVED, this.halt1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		iterator = this.itinerary.iterator();
		assertFalse(iterator.hasNext());

		assertFalse(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testRemoveBusHaltInt() {
		initTest();
		assertTrue(this.itinerary.isValidPrimitive());

		this.eventHandler.clear();
		assertTrue(this.itinerary.removeBusHalt(1));
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_REMOVED, this.halt2);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.itinerary.removeBusHalt(1));
		this.eventHandler.assertNoEvent();

		Iterator<BusItineraryHalt> iterator = this.itinerary.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertFalse(iterator.hasNext());

		assertFalse(this.itinerary.isValidPrimitive());

		this.eventHandler.clear();
		assertTrue(this.itinerary.removeBusHalt(0));
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_REMOVED, this.halt1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		iterator = this.itinerary.iterator();
		assertFalse(iterator.hasNext());

		assertFalse(this.itinerary.isValidPrimitive());
	}

	@Test
	public void testGetValidBusHaltCount() {
		assertEquals(0, this.itinerary.getValidBusHaltCount());

		initTest();
		assertEquals(2, this.itinerary.getValidBusHaltCount());

		BusItineraryHalt halt = new BusItineraryHalt(this.itinerary, "HALT_TMP", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.itinerary.addBusHalt(halt,10);
		assertEquals(2, this.itinerary.getValidBusHaltCount());

		halt.setRoadSegmentIndex(0);
		halt.checkPrimitiveValidity();
		assertEquals(2, this.itinerary.getValidBusHaltCount());

		halt.setPositionOnSegment(.5f);
		halt.checkPrimitiveValidity();
		assertEquals(2, this.itinerary.getValidBusHaltCount());

		BusStop stop = new BusStopStub(this.busNetwork, "STOP-TMP"); //$NON-NLS-1$
		stop.setPosition(new GeoLocationPoint(45, 67));
		halt.setBusStop(stop);

		assertEquals(3, this.itinerary.getValidBusHaltCount());
	}

	@Test
	public void testGetInvalidBusHaltCount() {
		assertEquals(0, this.itinerary.getInvalidBusHaltCount());

		initTest();
		assertEquals(0, this.itinerary.getInvalidBusHaltCount());

		BusItineraryHalt halt = new BusItineraryHalt(this.itinerary, "HALT_TMP", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.itinerary.addBusHalt(halt,10);
		assertEquals(1, this.itinerary.getInvalidBusHaltCount());

		halt.setRoadSegmentIndex(0);
		halt.checkPrimitiveValidity();
		assertEquals(1, this.itinerary.getInvalidBusHaltCount());

		halt.setPositionOnSegment(.5f);
		halt.checkPrimitiveValidity();
		assertEquals(1, this.itinerary.getInvalidBusHaltCount());

		BusStop stop = new BusStopStub(this.busNetwork, "STOP-TMP"); //$NON-NLS-1$
		stop.setPosition(new GeoLocationPoint(45, 67));
		halt.setBusStop(stop);

		assertEquals(0, this.itinerary.getInvalidBusHaltCount());
	}

	@Test
	public void testSize() {
		assertEquals(0, this.itinerary.size());

		this.itinerary.addBusHalt(this.halt1,0);
		assertEquals(1, this.itinerary.size());

		this.itinerary.addBusHalt(this.halt1,0);
		assertEquals(1, this.itinerary.size());

		this.itinerary.addBusHalt(this.halt2,1);
		assertEquals(2, this.itinerary.size());

		this.itinerary.removeBusHalt(this.halt1);
		assertEquals(1, this.itinerary.size());

		this.itinerary.removeBusHalt(this.halt2);
		assertEquals(0, this.itinerary.size());

		this.itinerary.removeBusHalt(this.halt1);
		assertEquals(0, this.itinerary.size());
	}

	@Test
	public void testIndexOfBusItineraryHalt() {
		initTest();
		assertEquals(-1, this.itinerary.indexOf((BusItineraryHalt)null));
		assertEquals(0, this.itinerary.indexOf(this.halt1));
		assertEquals(1, this.itinerary.indexOf(this.halt2));
		assertEquals(2, this.itinerary.indexOf(this.halt3));

		this.itinerary.invert();
		assertEquals(-1, this.itinerary.indexOf((BusItineraryHalt)null));
		assertEquals(1, this.itinerary.indexOf(this.halt1));
		assertEquals(0, this.itinerary.indexOf(this.halt2));
		assertEquals(2, this.itinerary.indexOf(this.halt3));
	}

	@Test
	public void testContainsBusItineraryHalt() {
		initTest();
		assertFalse(this.itinerary.contains((BusItineraryHalt)null));
		assertTrue(this.itinerary.contains(this.halt1));
		assertTrue(this.itinerary.contains(this.halt2));
		assertTrue(this.itinerary.contains(this.halt3));

		this.itinerary.invert();
		assertFalse(this.itinerary.contains((BusItineraryHalt)null));
		assertTrue(this.itinerary.contains(this.halt1));
		assertTrue(this.itinerary.contains(this.halt2));
		assertTrue(this.itinerary.contains(this.halt3));
	}

	@Test
	public void testGetBusHaltAtInt() {
		initTest();
		assertSame(this.halt1, this.itinerary.getBusHaltAt(0));
		assertSame(this.halt2, this.itinerary.getBusHaltAt(1));
		try {
			this.itinerary.getBusHaltAt(2);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}
	}

	@Test
	public void testGetBusHaltString() {
		initTest();
		assertSame(this.halt1, this.itinerary.getBusHalt("HALT1")); //$NON-NLS-1$
		assertSame(this.halt2, this.itinerary.getBusHalt("HALT2")); //$NON-NLS-1$
		assertNull(this.itinerary.getBusHalt("HALT3")); //$NON-NLS-1$
	}

	@Test
	public void testBusHaltIterator() {
		initTest();
		Iterator<BusItineraryHalt> iterator = this.itinerary.busHaltIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.halt2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBusHalts() {
		initTest();
		Iterable<BusItineraryHalt> iterable = this.itinerary.busHalts();
		Iterator<BusItineraryHalt> iterator = iterable.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.halt2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetNearestRoadSegmentDoubleDouble() {
		initTest();
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(0., 0.));
		assertSame(this.segment1, this.itinerary.getNearestRoadSegment(200., 100.));
		assertSame(this.segment1, this.itinerary.getNearestRoadSegment(100., 100.));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(-100., -100.));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(100., -100.));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(-100., 100.));
	}

	@Test
	public void testGetNearestRoadSegmentGeoLocationPoint() {
		initTest();
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new GeoLocationPoint(0., 0.)));
		assertSame(this.segment1, this.itinerary.getNearestRoadSegment(new GeoLocationPoint(200., 100.)));
		assertSame(this.segment1, this.itinerary.getNearestRoadSegment(new GeoLocationPoint(100., 100.)));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new GeoLocationPoint(-100., -100.)));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new GeoLocationPoint(100., -100.)));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new GeoLocationPoint(-100., 100.)));
	}

	@Test
	public void testGetNearestRoadSegmentPoint2d() {
		initTest();
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new Point2d(0., 0.)));
		assertSame(this.segment1, this.itinerary.getNearestRoadSegment(new Point2d(200., 100.)));
		assertSame(this.segment1, this.itinerary.getNearestRoadSegment(new Point2d(100., 100.)));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new Point2d(-100., -100.)));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new Point2d(100., -100.)));
		assertSame(this.segment2, this.itinerary.getNearestRoadSegment(new Point2d(-100., 100.)));
	}

	@Test
	public void testAddRoadSegmentRoadSegment() {
		// Implicitly tested through initTest()
	}

	@Test
	public void testRemoveAllRoadSegments() {
		initTest();
		assertTrue(this.itinerary.isValidPrimitive());
		assertEquals(2, this.itinerary.getRoadSegmentCount());

		this.eventHandler.clear();
		this.itinerary.removeAllRoadSegments();
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ALL_SEGMENTS_REMOVED, this.itinerary);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		assertFalse(this.itinerary.isValidPrimitive());
		assertEquals(0, this.itinerary.getRoadSegmentCount());
	}

	@Test
	public void testRemoveRoadSegmentInt() {
		initTest();
		assertTrue(this.itinerary.isValidPrimitive());
		assertEquals(2, this.itinerary.getRoadSegmentCount());

		this.eventHandler.clear();
		assertTrue(this.itinerary.removeRoadSegment(0));
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.SEGMENT_REMOVED, this.segment1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		assertFalse(this.itinerary.isValidPrimitive());
		assertEquals(1, this.itinerary.getRoadSegmentCount());
		assertSame(this.segment2, this.itinerary.getRoadSegmentAt(0));

		Iterator<BusItineraryHalt> iterator = this.itinerary.busHaltIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt2, iterator.next());
		assertTrue(this.halt2.isValidPrimitive());
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertFalse(this.halt1.isValidPrimitive());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testRemoveRoadSegmentRoadSegment() {
		Set<Integer> removedIndexes;

		initTest();
		assertTrue(this.itinerary.isValidPrimitive());
		assertEquals(2, this.itinerary.getRoadSegmentCount());

		this.eventHandler.clear();
		removedIndexes = this.itinerary.removeRoadSegment(this.segment1);
		assertNotNull(removedIndexes);
		assertEquals(1, removedIndexes.size());
		assertTrue(removedIndexes.contains(Integer.valueOf(0)));
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.itinerary);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.SEGMENT_REMOVED, this.segment1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		assertFalse(this.itinerary.isValidPrimitive());
		assertEquals(1, this.itinerary.getRoadSegmentCount());
		assertSame(this.segment2, this.itinerary.getRoadSegmentAt(0));

		Iterator<BusItineraryHalt> iterator = this.itinerary.busHaltIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.halt2, iterator.next());
		assertTrue(this.halt2.isValidPrimitive());
		assertTrue(iterator.hasNext());
		assertSame(this.halt1, iterator.next());
		assertFalse(this.halt1.isValidPrimitive());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetRoadSegmentCount() {
		assertEquals(0, this.itinerary.getRoadSegmentCount());
		initTest();
		assertEquals(2, this.itinerary.getRoadSegmentCount());
	}

	@Test
	public void testContainsRoadSegment() {
		assertFalse(this.itinerary.contains(this.segment1));
		assertFalse(this.itinerary.contains(this.segment2));
		assertFalse(this.itinerary.contains(this.segment3));

		initTest();

		assertTrue(this.itinerary.contains(this.segment1));
		assertTrue(this.itinerary.contains(this.segment2));
		assertFalse(this.itinerary.contains(this.segment3));
	}

	@Test
	public void testIndexOfRoadSegment() {
		assertEquals(-1, this.itinerary.indexOf(this.segment1));
		assertEquals(-1, this.itinerary.indexOf(this.segment2));
		assertEquals(-1, this.itinerary.indexOf(this.segment3));

		initTest();

		assertEquals(0, this.itinerary.indexOf(this.segment1));
		assertEquals(1, this.itinerary.indexOf(this.segment2));
		assertEquals(-1, this.itinerary.indexOf(this.segment3));
	}

	@Test
	public void testLastIndexOfRoadSegment() {
		assertEquals(-1, this.itinerary.indexOf(this.segment1));
		assertEquals(-1, this.itinerary.indexOf(this.segment2));
		assertEquals(-1, this.itinerary.indexOf(this.segment3));

		initTest3();

		assertEquals(3, this.itinerary.lastIndexOf(this.segment1));
		assertEquals(2, this.itinerary.lastIndexOf(this.segment2));
		assertEquals(-1, this.itinerary.lastIndexOf(this.segment3));
	}

	@Test
	public void testGetRoadSegmentAtInt() {
		initTest();
		assertSame(this.segment1, this.itinerary.getRoadSegmentAt(0));
		assertSame(this.segment2, this.itinerary.getRoadSegmentAt(1));
		try {
			this.itinerary.getRoadSegmentAt(2);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}
	}

	@Test
	public void testRoadSegmentsIterator() {
		Iterator<RoadSegment> iterator;

		iterator = this.itinerary.roadSegmentsIterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.itinerary.roadSegmentsIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testRoadSegments() {
		Iterator<RoadSegment> iterator;
		Iterable<RoadSegment> iterable;

		iterable = this.itinerary.roadSegments();
		iterator = iterable.iterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterable = this.itinerary.roadSegments();
		iterator = iterable.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetRoadPath() {
		RoadPath path;

		path = this.itinerary.getRoadPath();
		assertNull(path);

		initTest();

		path = this.itinerary.getRoadPath();
		assertNotNull(path);
		assertSame(this.segment1, path.get(0));
		assertSame(this.segment2, path.get(1));
		assertEquals(2, path.size());

		this.itinerary.addRoadSegment(this.segment3);
		path = this.itinerary.getRoadPath();
		assertNull(path);
	}

	@Test
	public void testGetRoadPaths() {
		Collection<RoadPath> paths;

		paths = this.itinerary.getRoadPaths();
		assertNotNull(paths);
		assertEquals(0, paths.size());

		initTest();

		paths = this.itinerary.getRoadPaths();
		assertNotNull(paths);
		assertEquals(1, paths.size());

		this.itinerary.addRoadSegment(this.segment3);
		paths = this.itinerary.getRoadPaths();
		assertNotNull(paths);
		assertEquals(2, paths.size());
	}

}

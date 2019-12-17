/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.bus.network.BusHub;
import org.arakhne.afc.gis.bus.network.BusItinerary;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt;
import org.arakhne.afc.gis.bus.network.BusLine;
import org.arakhne.afc.gis.bus.network.BusNetwork;
import org.arakhne.afc.gis.bus.network.BusStop;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class BusNetworkTest extends AbstractTestCase {

	private final TestEventHandler eventHandler = new TestEventHandler();
	private BusItinerary itinerary1;
	private BusItinerary itinerary2;
	private BusItineraryHalt halt1;
	private BusItineraryHalt halt2;
	private BusItineraryHalt halt3;
	private BusItineraryHalt halt4;
	private BusStop stop1;
	private BusStop stop2;
	private BusStop stop3;
	private BusStop stop4;
	private StandardRoadNetwork roadNetwork;
	private RoadPolyline segment1;
	private RoadPolyline segment2;
	private RoadPolyline segment3;
	private BusLine line1;
	private BusLine line2;
	private BusNetwork network;

	@Before
	public void setUp() throws Exception {
		this.itinerary1 = new BusItinerary("ITINERARY1"); //$NON-NLS-1$
		this.itinerary2 = new BusItinerary("ITINERARY2"); //$NON-NLS-1$
		this.halt1 = new BusItineraryHalt(this.itinerary1, "HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt2 = new BusItineraryHalt(this.itinerary1, "HALT2", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt3 = new BusItineraryHalt(this.itinerary2, "HALT3", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt4 = new BusItineraryHalt(this.itinerary2, "HALT4", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.stop1 = new BusStop("STOP1", new GeoLocationPoint(10., 10.)); //$NON-NLS-1$
		this.stop2 = new BusStop("STOP2", new GeoLocationPoint(-10., -10.)); //$NON-NLS-1$
		this.stop3 = new BusStop("STOP3", new GeoLocationPoint(90., -90.)); //$NON-NLS-1$
		this.stop4 = new BusStop("STOP4", new GeoLocationPoint(120., -120.)); //$NON-NLS-1$
		this.segment1 = new RoadPolyline();
		this.segment1.addPoint(10., 10.);
		this.segment1.addPoint(100., 100.);
		this.segment1.addPoint(200., 100.);
		this.segment2 = new RoadPolyline();
		this.segment2.addPoint(-10., 10.);
		this.segment2.addPoint(100., -100.);
		this.segment2.addPoint(200., 100.);
		this.segment3 = new RoadPolyline();
		this.segment3.addPoint(-10., 10.);
		this.segment3.addPoint(-150., -150.);
		this.roadNetwork = new StandardRoadNetwork(new Rectangle2d(-2000, -2000, 4000, 4000));
		this.roadNetwork.addRoadSegment(this.segment1);
		this.roadNetwork.addRoadSegment(this.segment2);
		this.roadNetwork.addRoadSegment(this.segment3);
		this.line1 = new BusLine("LINE1"); //$NON-NLS-1$
		this.line2 = new BusLine("LINE2"); //$NON-NLS-1$
		this.network = new BusNetwork(this.roadNetwork);
		this.network.addBusChangeListener(this.eventHandler);
		this.eventHandler.clear();
	}

	@After
	public void tearDown() throws Exception {
		this.network.removeBusChangeListener(this.eventHandler);
		this.roadNetwork = null;
		this.segment1 = this.segment2 = this.segment3 = null;
		this.stop1 = this.stop2 = this.stop3 = this.stop4 = null;
		this.halt1 = this.halt2 = this.halt3 = null;
		this.itinerary1 = this.itinerary2 = null;
		this.line1 = this.line2 = null;
		this.network = null;
	}

	private void initTest() {
		initTest2();
		this.network.addBusStop(this.stop1);
		this.network.addBusStop(this.stop2);
		this.network.addBusStop(this.stop3);
		this.network.addBusStop(this.stop4);
	}

	private void initTest2() {
		this.itinerary1.addBusHalt(this.halt1,0);
		this.halt1.setBusStop(this.stop1);
		this.itinerary1.addBusHalt(this.halt2,1);
		this.halt2.setBusStop(this.stop2);
		this.itinerary1.addRoadSegment(this.segment1);
		this.itinerary1.addRoadSegment(this.segment2);
		this.halt1.setRoadSegmentIndex(0);
		this.halt2.setRoadSegmentIndex(1);
		this.halt1.setPositionOnSegment(1f);
		this.halt2.setPositionOnSegment(1f);
		this.halt1.checkPrimitiveValidity();
		this.halt2.checkPrimitiveValidity();

		this.itinerary2.addBusHalt(this.halt3,2);
		this.halt3.setBusStop(this.stop3);
		this.itinerary2.addBusHalt(this.halt4,3);
		this.halt4.setBusStop(this.stop4);
		this.itinerary2.addRoadSegment(this.segment2);
		this.itinerary2.addRoadSegment(this.segment3);
		this.halt3.setRoadSegmentIndex(0);
		this.halt4.setRoadSegmentIndex(1);
		this.halt3.setPositionOnSegment(5f);
		this.halt4.setPositionOnSegment(2f);
		this.halt3.checkPrimitiveValidity();
		this.halt4.checkPrimitiveValidity();

		this.line1.addBusItinerary(this.itinerary1);
		this.line2.addBusItinerary(this.itinerary2);

		this.network.addBusLine(this.line1);
		this.network.addBusLine(this.line2);
	}

	@Test
	public void testSize() {
		assertEquals(0, this.network.size());
		initTest();
		assertEquals(2, this.network.size());
	}

	@Test
	public void testGetBusLineCount() {
		assertEquals(0, this.network.getBusLineCount());
		initTest();
		assertEquals(2, this.network.getBusLineCount());
	}

	@Test
	public void testIsValidPrimitive() {
		assertFalse(this.network.isValidPrimitive());
		initTest2();
		assertFalse(this.network.isValidPrimitive());
		this.network.addBusStop(this.stop1);
		this.network.addBusStop(this.stop2);
		assertFalse(this.network.isValidPrimitive());
		this.network.addBusStop(this.stop3);
		this.network.addBusStop(this.stop4);
		assertTrue(this.network.isValidPrimitive());
	}

	@Test
	public void testGetBusNetwork() {
		assertSame(this.network, this.network.getBusNetwork());
	}

	@Test
	public void testIterator() {
		Iterator<BusLine> iterator;

		iterator = this.network.iterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.network.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.line1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.line2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testAddBusLineBusLine() {
		initTest();
		assertTrue(this.network.isValidPrimitive());

		this.eventHandler.clear();
		BusLine line = new BusLine("LINE-TMP"); //$NON-NLS-1$
		assertTrue(this.network.addBusLine(line));
		assertEquals(3, this.network.size());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_ADDED, line);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		BusItinerary itinerary = new BusItinerary();
		itinerary.addRoadSegment(this.segment1);
		BusItineraryHalt halt = itinerary.addBusHalt("HALT-TMP1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		halt.setBusStop(this.stop1);
		halt.setRoadSegmentIndex(0);
		halt.setPositionOnSegment(1f);
		halt.checkPrimitiveValidity();
		halt = itinerary.addBusHalt("HALT-TMP2", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		halt.setBusStop(this.stop2);
		halt.setRoadSegmentIndex(0);
		halt.setPositionOnSegment(3f);
		halt.checkPrimitiveValidity();
		line.addBusItinerary(itinerary);

		assertTrue(this.network.isValidPrimitive());

		Iterator<BusLine> iterator = this.network.iterator();
		assertSame(this.line1, iterator.next());
		assertSame(this.line2, iterator.next());
		assertSame(line, iterator.next());
		assertFalse(iterator.hasNext());

		this.eventHandler.clear();
		assertFalse(this.network.addBusLine(this.line1));
		this.eventHandler.assertNoEvent();

		this.eventHandler.clear();
		assertFalse(this.network.addBusLine(null));
		this.eventHandler.assertNoEvent();
	}

	@Test
	public void testAddBusLineBusLineInt() {
		initTest();
		assertTrue(this.network.isValidPrimitive());

		this.eventHandler.clear();
		BusLine line = new BusLine("LINE-TMP"); //$NON-NLS-1$
		assertTrue(this.network.addBusLine(line, 1));
		assertEquals(3, this.network.size());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_ADDED, line);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		BusItinerary itinerary = new BusItinerary();
		itinerary.addRoadSegment(this.segment1);
		BusItineraryHalt halt = itinerary.addBusHalt("HALT-TMP1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		halt.setBusStop(this.stop1);
		halt.setRoadSegmentIndex(0);
		halt.setPositionOnSegment(1f);
		halt.checkPrimitiveValidity();
		halt = itinerary.addBusHalt("HALT-TMP2", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		halt.setBusStop(this.stop2);
		halt.setRoadSegmentIndex(0);
		halt.setPositionOnSegment(3f);
		halt.checkPrimitiveValidity();
		line.addBusItinerary(itinerary);

		assertTrue(this.network.isValidPrimitive());

		Iterator<BusLine> iterator = this.network.iterator();
		assertSame(this.line1, iterator.next());
		assertSame(line, iterator.next());
		assertSame(this.line2, iterator.next());
		assertFalse(iterator.hasNext());

		this.eventHandler.clear();
		assertFalse(this.network.addBusLine(this.line1, 0));
		this.eventHandler.assertNoEvent();

		this.eventHandler.clear();
		assertFalse(this.network.addBusLine(null, 0));
		this.eventHandler.assertNoEvent();
	}

	@Test
	public void testRemoveAllBusLines() {
		initTest();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertEquals(2, this.network.size());

		this.network.removeAllBusLines();

		assertEquals(0, this.network.size());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ALL_LINES_REMOVED, this.network);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusLineBusLine() {
		Iterator<BusLine> iterator;
		initTest();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusLine(this.line1));
		iterator = this.network.iterator();
		assertSame(this.line2, iterator.next());
		assertFalse(iterator.hasNext());
		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_REMOVED, this.line1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.network.removeBusLine(this.line1));
		this.eventHandler.assertNoEvent();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusLine(this.line2));
		iterator = this.network.iterator();
		assertFalse(iterator.hasNext());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_REMOVED, this.line2);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusLineString() {
		Iterator<BusLine> iterator;
		initTest();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusLine("LINE1")); //$NON-NLS-1$
		iterator = this.network.iterator();
		assertSame(this.line2, iterator.next());
		assertFalse(iterator.hasNext());
		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_REMOVED, this.line1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.network.removeBusLine("LINE1")); //$NON-NLS-1$
		this.eventHandler.assertNoEvent();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusLine("LINE2")); //$NON-NLS-1$
		iterator = this.network.iterator();
		assertFalse(iterator.hasNext());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_REMOVED, this.line2);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusLineInt() {
		Iterator<BusLine> iterator;
		initTest();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusLine(0));
		iterator = this.network.iterator();
		assertSame(this.line2, iterator.next());
		assertFalse(iterator.hasNext());
		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_REMOVED, this.line1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusLine(0));
		iterator = this.network.iterator();
		assertFalse(iterator.hasNext());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.LINE_REMOVED, this.line2);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testIndexOfBusLine() {
		assertEquals(-1, this.network.indexOf(this.line1));
		assertEquals(-1, this.network.indexOf(this.line2));
		assertEquals(-1, this.network.indexOf(new BusLine()));

		initTest();

		assertEquals(0, this.network.indexOf(this.line1));
		assertEquals(1, this.network.indexOf(this.line2));
		assertEquals(-1, this.network.indexOf(new BusLine()));
	}

	@Test
	public void testGetBusLineAtInt() {
		try {
			this.network.getBusLineAt(0);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// Expected exception
		}
		try {
			this.network.getBusLineAt(1);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// Expected exception
		}
		try {
			this.network.getBusLineAt(3);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// Expected exception
		}

		initTest();

		assertSame(this.line1, this.network.getBusLineAt(0));
		assertSame(this.line2, this.network.getBusLineAt(1));
		try {
			this.network.getBusLineAt(2);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// Expected exception
		}
	}

	@Test
	public void testGetBusLineString() {
		assertNull(this.network.getBusLine("LINE1")); //$NON-NLS-1$
		assertNull(this.network.getBusLine("LINE2")); //$NON-NLS-1$
		assertNull(this.network.getBusLine("LINE3")); //$NON-NLS-1$

		initTest();

		assertSame(this.line1, this.network.getBusLine("LINE1")); //$NON-NLS-1$
		assertSame(this.line2, this.network.getBusLine("LINE2")); //$NON-NLS-1$
		assertNull(this.network.getBusLine("LINE3")); //$NON-NLS-1$
	}

	@Test
	public void testBusLineIterator() {
		Iterator<BusLine> iterator;

		iterator = this.network.busLineIterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.network.busLineIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.line1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.line2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBusLines() {
		Iterable<BusLine> iterable;
		Iterator<BusLine> iterator;

		iterable = this.network.busLines();
		iterator = iterable.iterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterable = this.network.busLines();
		iterator = iterable.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.line1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.line2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testAddBusStopBusStop() {
		Iterator<BusStop> iterator;
		BusStop stop5, stop6;

		initTest();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		stop5 = new BusStop("STOP5"); //$NON-NLS-1$
		assertTrue(this.network.addBusStop(stop5));
		iterator = this.network.busStopIterator();
		assertSame(this.stop1, iterator.next());
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop3, iterator.next());
		assertSame(this.stop4, iterator.next());
		assertSame(stop5, iterator.next());
		assertFalse(iterator.hasNext());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_ADDED, stop5);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.clear();
		stop6 = new BusStop("STOP6"); //$NON-NLS-1$
		assertTrue(this.network.addBusStop(stop6));
		iterator = this.network.busStopIterator();
		assertSame(this.stop1, iterator.next());
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop3, iterator.next());
		assertSame(this.stop4, iterator.next());
		if (stop5.getUUID().compareTo(stop6.getUUID())<=0) {
			assertSame(stop5, iterator.next());
			assertSame(stop6, iterator.next());
		}
		else {
			assertSame(stop6, iterator.next());
			assertSame(stop5, iterator.next());
		}
		assertFalse(iterator.hasNext());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_ADDED, stop6);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.network.addBusStop(stop6));
		this.eventHandler.assertNoEvent();

		this.eventHandler.clear();
		assertFalse(this.network.addBusStop(null));
		this.eventHandler.assertNoEvent();
	}

	@Test
	public void testRemoveAllBusStops() {
		initTest();
		this.eventHandler.clear();
		assertEquals(4, this.network.getBusStopCount());
		assertTrue(this.network.isValidPrimitive());

		this.network.removeAllBusStops();

		assertEquals(0, this.network.getBusStopCount());
		assertFalse(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.network);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ALL_STOPS_REMOVED, this.network);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusStopBusStop() {
		Iterator<BusStop> iterator;
		initTest();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusStop(this.stop1));
		assertTrue(this.network.isValidPrimitive());
		iterator = this.network.busStopIterator();
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop3, iterator.next());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_REMOVED, this.stop1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.network.removeBusStop(this.stop1));
		this.eventHandler.assertNoEvent();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusStop(this.stop3));
		assertTrue(this.network.isValidPrimitive());
		iterator = this.network.busStopIterator();
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_REMOVED, this.stop3);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusStopString() {
		Iterator<BusStop> iterator;
		initTest();

		this.eventHandler.clear();
		assertTrue(this.network.isValidPrimitive());
		assertTrue(this.network.removeBusStop("STOP1")); //$NON-NLS-1$
		assertTrue(this.network.isValidPrimitive());
		iterator = this.network.busStopIterator();
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop3, iterator.next());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_REMOVED, this.stop1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.network.removeBusStop("STOP1")); //$NON-NLS-1$
		this.eventHandler.assertNoEvent();

		this.eventHandler.clear();
		assertTrue(this.network.isValidPrimitive());
		assertTrue(this.network.removeBusStop("STOP3")); //$NON-NLS-1$
		assertTrue(this.network.isValidPrimitive());
		iterator = this.network.busStopIterator();
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_REMOVED, this.stop3);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testGetBusStopCount() {
		assertEquals(0, this.network.getBusStopCount());
		initTest();
		assertEquals(4, this.network.getBusStopCount());
	}

	@Test
	public void testGetBusStopsInBounds2D() {
		Iterator<BusStop> iterator;

		iterator = this.network.getBusStopsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusStopsIn(new Rectangle2d(-20., 20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.network.getBusStopsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(this.stop1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusStopsIn(new Rectangle2d(-200., -200., 400., 400.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(this.stop1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetBusStopsInRectangle2D() {
		Iterator<BusStop> iterator;

		iterator = this.network.getBusStopsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusStopsIn(new Rectangle2d(-20., 20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.network.getBusStopsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(this.stop1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusStopsIn(new Rectangle2d(-200., -200., 400., 400.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(this.stop1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetBusStopString() {
		assertNull(this.network.getBusStop("STOP1")); //$NON-NLS-1$
		assertNull(this.network.getBusStop("STOP2")); //$NON-NLS-1$
		assertNull(this.network.getBusStop("STOP3")); //$NON-NLS-1$
		assertNull(this.network.getBusStop("STOP4")); //$NON-NLS-1$
		assertNull(this.network.getBusStop("STOP-TMP")); //$NON-NLS-1$

		initTest();

		assertSame(this.stop1, this.network.getBusStop("STOP1")); //$NON-NLS-1$
		assertSame(this.stop2, this.network.getBusStop("STOP2")); //$NON-NLS-1$
		assertSame(this.stop3, this.network.getBusStop("STOP3")); //$NON-NLS-1$
		assertSame(this.stop4, this.network.getBusStop("STOP4")); //$NON-NLS-1$
		assertNull(this.network.getBusStop("STOP-TMP")); //$NON-NLS-1$
	}

	@Test
	public void testGetBusStopUUID() {
		assertNull(this.network.getBusStop(this.stop1.getUUID()));
		assertNull(this.network.getBusStop(this.stop2.getUUID()));
		assertNull(this.network.getBusStop(this.stop3.getUUID()));
		assertNull(this.network.getBusStop(this.stop4.getUUID()));

		initTest();

		assertSame(this.stop1, this.network.getBusStop(this.stop1.getUUID()));
		assertSame(this.stop2, this.network.getBusStop(this.stop2.getUUID()));
		assertSame(this.stop3, this.network.getBusStop(this.stop3.getUUID()));
		assertSame(this.stop4, this.network.getBusStop(this.stop4.getUUID()));
	}

	@Test
	public void testBusStopIterator() {
		Iterator<BusStop> iterator;

		iterator = this.network.busStopIterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.network.busStopIterator();
		assertSame(this.stop1, iterator.next());
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop3, iterator.next());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBusStops() {
		Iterable<BusStop> iterable;
		Iterator<BusStop> iterator;

		iterable = this.network.busStops();
		iterator = iterable.iterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterable = this.network.busStops();
		iterator = iterable.iterator();
		assertSame(this.stop1, iterator.next());
		assertSame(this.stop2, iterator.next());
		assertSame(this.stop3, iterator.next());
		assertSame(this.stop4, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetNearestBusStopPoint2d() {
		assertNull(this.network.getNearestBusStop(new Point2d(0, 0)));

		initTest();

		assertSame(this.stop1, this.network.getNearestBusStop(new Point2d(0, 0)));
		assertSame(this.stop2, this.network.getNearestBusStop(new Point2d(-10, 0)));
		assertSame(this.stop4, this.network.getNearestBusStop(new Point2d(110, -110)));
		assertSame(this.stop1, this.network.getNearestBusStop(new Point2d(100, 0)));
	}

	@Test
	public void testGetNearestBusStopGeoLocationPoint() {
		assertNull(this.network.getNearestBusStop(new GeoLocationPoint(0, 0)));

		initTest();

		assertSame(this.stop1, this.network.getNearestBusStop(new GeoLocationPoint(0, 0)));
		assertSame(this.stop2, this.network.getNearestBusStop(new GeoLocationPoint(-10, 0)));
		assertSame(this.stop4, this.network.getNearestBusStop(new GeoLocationPoint(110, -110)));
		assertSame(this.stop1, this.network.getNearestBusStop(new GeoLocationPoint(100, 0)));
	}

	@Test
	public void testGetNearestBusStopDoubleDouble() {
		assertNull(this.network.getNearestBusStop(0, 0));

		initTest();

		assertSame(this.stop1, this.network.getNearestBusStop(0, 0));
		assertSame(this.stop2, this.network.getNearestBusStop(-10, 0));
		assertSame(this.stop4, this.network.getNearestBusStop(110, -110));
		assertSame(this.stop1, this.network.getNearestBusStop(100, 0));
	}

	@Test
	public void testAddBusHubBusHub() {
		BusHub hub;

		initTest();

		assertEquals(0, this.network.getBusHubCount());
		this.eventHandler.clear();
		assertTrue(this.network.isValidPrimitive());

		hub = this.network.addBusHub(this.stop1, this.stop2);

		assertNotNull(hub);
		assertEquals(1, this.network.getBusHubCount());
		assertTrue(hub.contains(this.stop1));
		assertTrue(hub.contains(this.stop2));
		assertFalse(hub.contains(this.stop3));
		assertFalse(hub.contains(this.stop4));
		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.HUB_ADDED, hub);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveAllBusHubs() {
		initTest();
		assertTrue(this.network.isValidPrimitive());
		this.network.addBusHub(this.stop1, this.stop2);
		this.network.addBusHub(this.stop3, this.stop4);
		assertEquals(2, this.network.getBusHubCount());
		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();

		this.network.removeAllBusHubs();

		assertTrue(this.network.isValidPrimitive());
		assertEquals(0, this.network.getBusHubCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.ALL_HUBS_REMOVED, this.network);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusHubBusHub() {
		initTest();
		BusHub hub1 = this.network.addBusHub(this.stop1, this.stop2);
		BusHub hub2 = this.network.addBusHub(this.stop3, this.stop4);

		assertEquals(2, this.network.getBusHubCount());
		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();

		assertTrue(this.network.removeBusHub(hub1));

		assertTrue(this.network.isValidPrimitive());
		assertEquals(1, this.network.getBusHubCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.HUB_REMOVED, hub1);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.network.removeBusHub(hub1));
		this.eventHandler.assertNoEvent();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusHub(hub2));
		assertEquals(0, this.network.getBusHubCount());
		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.HUB_REMOVED, hub2);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusHubString() {
		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		assertEquals(2, this.network.getBusHubCount());
		this.eventHandler.clear();
		assertTrue(this.network.isValidPrimitive());

		assertTrue(this.network.removeBusHub("HUB1")); //$NON-NLS-1$

		assertTrue(this.network.isValidPrimitive());
		assertEquals(1, this.network.getBusHubCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.HUB_REMOVED, hub1);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.network.removeBusHub("HUB1")); //$NON-NLS-1$
		this.eventHandler.assertNoEvent();

		assertTrue(this.network.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.network.removeBusHub("HUB2")); //$NON-NLS-1$
		assertTrue(this.network.isValidPrimitive());
		assertEquals(0, this.network.getBusHubCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.HUB_REMOVED, hub2);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testGetBusHubCount() {
		initTest();
		assertEquals(0, this.network.getBusHubCount());
	}

	@Test
	public void testGetBusHubString() {
		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		assertSame(hub1, this.network.getBusHub("HUB1")); //$NON-NLS-1$
		assertSame(hub2, this.network.getBusHub("HUB2")); //$NON-NLS-1$
	}

	@Test
	public void testBusHubIterator() {
		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		Iterator<BusHub> iterator = this.network.busHubIterator();
		assertSame(hub1, iterator.next());
		assertSame(hub2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBusHubs() {
		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		Iterable<BusHub> iterable = this.network.busHubs();
		Iterator<BusHub> iterator = iterable.iterator();
		assertSame(hub1, iterator.next());
		assertSame(hub2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetBusHubsInBounds2D() {
		Iterator<BusHub> iterator;

		iterator = this.network.getBusHubsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusHubsIn(new Rectangle2d(-20., 20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		iterator = this.network.getBusHubsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(hub1, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusHubsIn(new Rectangle2d(-200., -200., 400., 400.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(hub1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(hub2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetBusHubsInRectangle2D() {
		Iterator<BusHub> iterator;

		iterator = this.network.getBusHubsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusHubsIn(new Rectangle2d(-20., 20., 40., 40.));
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		iterator = this.network.getBusHubsIn(new Rectangle2d(-20., -20., 40., 40.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(hub1, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.network.getBusHubsIn(new Rectangle2d(-200., -200., 400., 400.));
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(hub1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(hub2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetNearestBusHubPoint2d() {
		assertNull(this.network.getNearestBusHub(new Point2d(0, 0)));

		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		assertTrue(hub1.isValidPrimitive());
		assertTrue(hub2.isValidPrimitive());

		assertSame(hub1, this.network.getNearestBusHub(new Point2d(0, 0)));
		assertSame(hub1, this.network.getNearestBusHub(new Point2d(-10, 0)));
		assertSame(hub2, this.network.getNearestBusHub(new Point2d(110, -110)));
		assertSame(hub1, this.network.getNearestBusHub(new Point2d(100, 0)));
	}

	@Test
	public void testGetNearestBusHubGeoLocationPoint() {
		assertNull(this.network.getNearestBusHub(new GeoLocationPoint(0, 0)));

		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		assertSame(hub1, this.network.getNearestBusHub(new GeoLocationPoint(0, 0)));
		assertSame(hub1, this.network.getNearestBusHub(new GeoLocationPoint(-10, 0)));
		assertSame(hub2, this.network.getNearestBusHub(new GeoLocationPoint(110, -110)));
		assertSame(hub1, this.network.getNearestBusHub(new GeoLocationPoint(100, 0)));
	}

	@Test
	public void testGetNearestBusHubDoubleDouble() {
		assertNull(this.network.getNearestBusHub(0, 0));

		initTest();
		BusHub hub1 = this.network.addBusHub("HUB1", this.stop1, this.stop2); //$NON-NLS-1$
		BusHub hub2 = this.network.addBusHub("HUB2", this.stop3, this.stop4); //$NON-NLS-1$

		assertSame(hub1, this.network.getNearestBusHub(0, 0));
		assertSame(hub1, this.network.getNearestBusHub(-10, 0));
		assertSame(hub2, this.network.getNearestBusHub(110, -110));
		assertSame(hub1, this.network.getNearestBusHub(100, 0));
	}

}

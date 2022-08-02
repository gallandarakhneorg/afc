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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
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
public class BusLineTest extends AbstractTestCase {

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
	private StandardRoadNetwork network;
	private RoadPolyline segment1;
	private RoadPolyline segment2;
	private RoadPolyline segment3;
	private RoadPolyline segment4;
	private BusLine line;
	private BusNetwork busNetwork;

	@BeforeEach
	public void setUp() throws Exception {
		this.network = new StandardRoadNetwork(new Rectangle2d(-2000, -2000, 4000, 4000));
		this.busNetwork = new BusNetwork(this.network);
		this.itinerary1 = new BusItinerary("ITINERARY1"); //$NON-NLS-1$
		this.itinerary2 = new BusItinerary("ITINERARY2"); //$NON-NLS-1$
		this.halt1 = new BusItineraryHalt(this.itinerary1, "HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt2 = new BusItineraryHalt(this.itinerary1, "HALT2", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt3 = new BusItineraryHalt(this.itinerary2, "HALT3", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.halt4 = new BusItineraryHalt(this.itinerary2, "HALT4", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.stop1 = new BusStopStub(this.busNetwork, "STOP1", new GeoLocationPoint(10., 10.)); //$NON-NLS-1$
		this.stop2 = new BusStopStub(this.busNetwork, "STOP2", new GeoLocationPoint(-10., -10.)); //$NON-NLS-1$
		this.stop3 = new BusStopStub(this.busNetwork, "STOP3", new GeoLocationPoint(90., -90.)); //$NON-NLS-1$
		this.stop4 = new BusStopStub(this.busNetwork, "STOP4", new GeoLocationPoint(120., -120.)); //$NON-NLS-1$
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
		// segment4 is only to enlarge the road network
		this.segment4 = new RoadPolyline();
		this.segment4.addPoint(-1000., -1000.);
		this.segment4.addPoint(1000., 1000.);
		this.network.addRoadSegment(this.segment1);
		this.network.addRoadSegment(this.segment2);
		this.network.addRoadSegment(this.segment3);
		this.network.addRoadSegment(this.segment4);
		this.line = new BusLine("LINE1"); //$NON-NLS-1$
		this.line.setContainer(this.busNetwork);
		this.line.addBusChangeListener(this.eventHandler);
		this.eventHandler.clear();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.line.removeBusChangeListener(this.eventHandler);
		this.network = null;
		this.segment1 = this.segment2 = this.segment3 = null;
		this.stop1 = this.stop2 = this.stop3 = this.stop4 = null;
		this.halt1 = this.halt2 = this.halt3 = null;
		this.itinerary1 = this.itinerary2 = null;
		this.line = null;
		this.busNetwork =  null;
	}

	private void initTest() {
		this.busNetwork.addBusStop(this.stop1);
		this.busNetwork.addBusStop(this.stop2);
		this.busNetwork.addBusStop(this.stop3);
		this.busNetwork.addBusStop(this.stop4);

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

		this.line.addBusItinerary(this.itinerary1);
		this.line.addBusItinerary(this.itinerary2);
	}

	@Test
	public void testGetBusNetwork() {
		assertSame(this.busNetwork, this.line.getBusNetwork());
	}

	@Test
	public void testIsValidPrimitive() {
		assertFalse(this.line.isValidPrimitive());
		initTest();
		assertTrue(this.line.isValidPrimitive());
		this.line.addBusItinerary(new BusItinerary());
		assertFalse(this.line.isValidPrimitive());
	}

	@Test
	public void testSize() {
		assertEquals(0, this.line.size());
		initTest();
		assertEquals(2, this.line.size());
	}

	@Test
	public void testIterator() {
		Iterator<BusItinerary> iterator;

		iterator = this.line.iterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.line.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.itinerary1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.itinerary2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testAddBusItineraryBusItinerary() {
		initTest();
		assertTrue(this.line.isValidPrimitive());
		BusItinerary tmp = new BusItinerary("ITINERARY-TMP"); //$NON-NLS-1$
		this.eventHandler.clear();
		this.line.addBusItinerary(tmp);
		assertFalse(this.line.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.line);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_ADDED, tmp);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		Iterator<BusItinerary> iterator = this.line.busItineraryIterator();
		assertSame(this.itinerary1, iterator.next());
		assertSame(this.itinerary2, iterator.next());
		assertSame(tmp, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testAddBusItineraryBusItineraryInt() {
		initTest();
		assertTrue(this.line.isValidPrimitive());
		BusItinerary tmp = new BusItinerary("ITINERARY-TMP"); //$NON-NLS-1$
		this.eventHandler.clear();
		this.line.addBusItinerary(tmp, 1);
		assertFalse(this.line.isValidPrimitive());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.line);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_ADDED, tmp);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		Iterator<BusItinerary> iterator = this.line.busItineraryIterator();
		assertSame(this.itinerary1, iterator.next());
		assertSame(tmp, iterator.next());
		assertSame(this.itinerary2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testRemoveAllBusItineraries() {
		initTest();
		assertEquals(2, this.line.getBusItineraryCount());
		assertTrue(this.line.isValidPrimitive());
		this.eventHandler.clear();

		this.line.removeAllBusItineraries();

		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.line);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ALL_ITINERARIES_REMOVED, this.line);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
		assertEquals(0, this.line.getBusItineraryCount());
		assertFalse(this.line.isValidPrimitive());
	}

	@Test
	public void testRemoveBusItineraryBusItinerary() {
		Iterator<BusItinerary> iterator;
		initTest();

		assertTrue(this.line.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.line.removeBusItinerary(this.itinerary1));
		iterator = this.line.busItineraryIterator();
		assertSame(this.itinerary2, iterator.next());
		assertFalse(iterator.hasNext());
		assertTrue(this.line.isValidPrimitive());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_REMOVED, this.itinerary1);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.line.removeBusItinerary(this.itinerary1));
		this.eventHandler.assertNoEvent();

		assertTrue(this.line.removeBusItinerary(this.itinerary2));
		iterator = this.line.busItineraryIterator();
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testRemoveBusItineraryInt() {
		Iterator<BusItinerary> iterator;
		initTest();

		assertTrue(this.line.isValidPrimitive());
		this.eventHandler.clear();
		assertTrue(this.line.removeBusItinerary(1));
		iterator = this.line.busItineraryIterator();
		assertSame(this.itinerary1, iterator.next());
		assertFalse(iterator.hasNext());
		assertTrue(this.line.isValidPrimitive());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_REMOVED, this.itinerary2);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.line.removeBusItinerary(1));
		this.eventHandler.assertNoEvent();

		assertTrue(this.line.removeBusItinerary(0));
		iterator = this.line.busItineraryIterator();
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetBusItineraryCount() {
		assertEquals(0, this.line.getBusItineraryCount());
		initTest();
		assertEquals(2, this.line.getBusItineraryCount());
	}

	@Test
	public void testIndexOfBusItinerary() {
		assertEquals(-1, this.line.indexOf(this.itinerary1));
		assertEquals(-1, this.line.indexOf(this.itinerary2));
		initTest();
		assertEquals(0, this.line.indexOf(this.itinerary1));
		assertEquals(1, this.line.indexOf(this.itinerary2));
	}

	@Test
	public void testGetBusItineraryAtInt() {
		try {
			this.line.getBusItineraryAt(0);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}
		initTest();
		assertSame(this.itinerary1, this.line.getBusItineraryAt(0));
		assertSame(this.itinerary2, this.line.getBusItineraryAt(1));
		try {
			this.line.getBusItineraryAt(2);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}
	}

	@Test
	public void testGetBusItineraryString() {
		assertNull(this.line.getBusItinerary("ITINERARY1")); //$NON-NLS-1$
		assertNull(this.line.getBusItinerary("ITINERARY2")); //$NON-NLS-1$
		initTest();
		assertSame(this.itinerary1, this.line.getBusItinerary("ITINERARY1")); //$NON-NLS-1$
		assertSame(this.itinerary2, this.line.getBusItinerary("ITINERARY2")); //$NON-NLS-1$
	}

	@Test
	public void testBusItineraryIterator() {
		Iterator<BusItinerary> iterator;

		iterator = this.line.busItineraryIterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterator = this.line.busItineraryIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.itinerary1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.itinerary2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBusItineraries() {
		Iterator<BusItinerary> iterator;
		Iterable<BusItinerary> iterable;

		iterable = this.line.busItineraries();
		iterator = iterable.iterator();
		assertFalse(iterator.hasNext());

		initTest();

		iterable = this.line.busItineraries();
		iterator = iterable.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.itinerary1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.itinerary2, iterator.next());
		assertFalse(iterator.hasNext());
	}

}

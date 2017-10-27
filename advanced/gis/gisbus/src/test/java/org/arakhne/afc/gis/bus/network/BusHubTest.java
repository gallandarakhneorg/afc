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

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.bus.network.BusHub;
import org.arakhne.afc.gis.bus.network.BusNetwork;
import org.arakhne.afc.gis.bus.network.BusStop;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class BusHubTest extends AbstractTestCase {

	private final TestEventHandler eventHandler = new TestEventHandler();
	private BusHub hub;
	private BusNetwork busNetwork;

	private static RoadNetwork readRoadNetwork() {
		Rectangle2d m = new Rectangle2d(-100, -100, 200, 100);
		StandardRoadNetwork network = new StandardRoadNetwork(m);
		RoadPolyline road;
		road = new RoadPolyline();
		road.addPoint(-100, -100);
		road.addPoint(-50, -5);
		network.addRoadPolyline(road);
		road = new RoadPolyline();
		road.addPoint(100, 100);
		road.addPoint(0, 0);
		network.addRoadPolyline(road);
		return network;
	}

	@Before
	public void setUp() throws Exception {
		this.busNetwork = new BusNetwork(readRoadNetwork());
		this.hub = new BusHub(this.busNetwork, "STATION"); //$NON-NLS-1$
		this.hub.setContainer(this.busNetwork);
		this.hub.addBusChangeListener(this.eventHandler);
		this.eventHandler.clear();
	}

	@After
	public void tearDown() throws Exception {
		this.hub.removeBusChangeListener(this.eventHandler);
		this.hub = null;
		this.busNetwork = null;
	}

	@Test
	public void testGetBusNetwork() {
		assertSame(this.busNetwork, this.hub.getBusNetwork());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.hub.isEmpty());
		this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP")); //$NON-NLS-1$
		assertFalse(this.hub.isEmpty());
	}

	@Test
	public void testIsSignificant() {
		assertFalse(this.hub.isSignificant());
		this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP1")); //$NON-NLS-1$
		assertFalse(this.hub.isSignificant());
		this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP2")); //$NON-NLS-1$
		assertTrue(this.hub.isSignificant());
	}

	@Test
	public void testAddBusStopBusStop() {
		assertTrue(this.hub.isEmpty());

		this.eventHandler.clear();
		BusStop stop = new BusStopStub(this.busNetwork, "STOP1"); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(stop));
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.STOP_ADDED, stop);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		stop = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(stop));
		assertFalse(this.hub.isEmpty());
		assertEquals(2, this.hub.getBusStopCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.STOP_ADDED, stop);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveAllBusStops() {
		assertTrue(this.hub.isEmpty());
		assertTrue(this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP1"))); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP2"))); //$NON-NLS-1$
		assertFalse(this.hub.isEmpty());
		assertEquals(2, this.hub.getBusStopCount());

		this.eventHandler.clear();
		this.hub.removeAllBusStops();
		assertTrue(this.hub.isEmpty());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.ALL_STOPS_REMOVED, this.hub);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusStopBusStop() {
		BusStop b = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$
		assertTrue(this.hub.isEmpty());
		assertTrue(this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP1"))); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(b));
		assertFalse(this.hub.isEmpty());
		assertEquals(2, this.hub.getBusStopCount());

		this.eventHandler.clear();
		assertTrue(this.hub.removeBusStop(b));
		assertEquals(1, this.hub.getBusStopCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.STOP_REMOVED, b);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testRemoveBusStopInt() {
		BusStop b = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$
		assertTrue(this.hub.isEmpty());
		assertTrue(this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP1"))); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(b));
		assertFalse(this.hub.isEmpty());
		assertEquals(2, this.hub.getBusStopCount());

		this.eventHandler.clear();
		assertTrue(this.hub.removeBusStop(1));
		assertEquals(1, this.hub.getBusStopCount());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.STOP_REMOVED, b);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testGetBusStopCount() {
		assertEquals(0, this.hub.getBusStopCount());
		assertTrue(this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP1"))); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(new BusStopStub(this.busNetwork, "STOP2"))); //$NON-NLS-1$
		assertEquals(2, this.hub.getBusStopCount());
		assertTrue(this.hub.removeBusStop(0));
		assertEquals(1, this.hub.getBusStopCount());
		assertFalse(this.hub.removeBusStop(1));
		assertEquals(1, this.hub.getBusStopCount());
		assertTrue(this.hub.removeBusStop(0));
		assertEquals(0, this.hub.getBusStopCount());
	}

	@Test
	public void testIndexOfBusStop() {
		BusStop b1 = new BusStopStub(this.busNetwork, "STOP1"); //$NON-NLS-1$
		BusStop b2 = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$
		BusStop b3 = new BusStopStub(this.busNetwork, "STOP3"); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(b1));
		assertTrue(this.hub.addBusStop(b2));

		assertEquals(1, this.hub.indexOf(b2));
		assertEquals(0, this.hub.indexOf(b1));
		assertEquals(-1, this.hub.indexOf(b3));
	}

	@Test
	public void testGetBusStopAtInt() {
		BusStop b1 = new BusStopStub(this.busNetwork, "STOP1"); //$NON-NLS-1$
		BusStop b2 = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(b1));
		assertTrue(this.hub.addBusStop(b2));

		assertSame(b1, this.hub.getBusStopAt(0));
		assertSame(b2, this.hub.getBusStopAt(1));
		try {
			this.hub.getBusStopAt(2);
			fail("expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}
	}

	@Test
	public void testIsValidPrimitive() {
		BusStop b1 = new BusStopStub(this.busNetwork, "STOP1"); //$NON-NLS-1$
		BusStop b2 = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$

		assertFalse(this.hub.isValidPrimitive());

		this.hub.addBusStop(b1);
		assertFalse(this.hub.isValidPrimitive());

		this.hub.addBusStop(b2);
		assertFalse(this.hub.isValidPrimitive());

		b1.setPosition(new GeoLocationPoint(10., 10.));
		assertFalse(this.hub.isValidPrimitive());

		b2.setPosition(new GeoLocationPoint(11., 11.));
		assertTrue(this.hub.isValidPrimitive());
	}

	@Test
	public void testBusStopIterator() {
		Iterator<BusStop> iterator;

		iterator = this.hub.busStopIterator();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		BusStop b1 = new BusStopStub(this.busNetwork, "STOP1"); //$NON-NLS-1$
		BusStop b2 = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(b1));
		assertTrue(this.hub.addBusStop(b2));

		iterator = this.hub.busStopIterator();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(b1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(b2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBusStops() {
		Iterator<BusStop> iterator;

		iterator = this.hub.busStops().iterator();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		BusStop b1 = new BusStopStub(this.busNetwork, "STOP1"); //$NON-NLS-1$
		BusStop b2 = new BusStopStub(this.busNetwork, "STOP2"); //$NON-NLS-1$
		assertTrue(this.hub.addBusStop(b1));
		assertTrue(this.hub.addBusStop(b2));

		iterator = this.hub.busStops().iterator();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertSame(b1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(b2, iterator.next());
		assertFalse(iterator.hasNext());
	}

}

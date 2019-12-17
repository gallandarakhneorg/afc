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

import org.arakhne.afc.gis.bus.network.BusHub;
import org.arakhne.afc.gis.bus.network.BusItinerary;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt;
import org.arakhne.afc.gis.bus.network.BusNetwork;
import org.arakhne.afc.gis.bus.network.BusStop;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;

/**
 * Unit test for BusStop.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class BusStopTest extends AbstractTestCase {

	private static RoadNetwork readRoadNetwork() {
		Rectangle2d m = new Rectangle2d(-100, -100, 200, 200);
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

	private final TestEventHandler eventHandler = new TestEventHandler();
	private BusNetwork busNetwork;
	private BusStop stop;

	@Before
	public void setUp() throws Exception {
		this.busNetwork = new BusNetwork(readRoadNetwork());
		this.stop = new BusStop("BUSSTOP"); //$NON-NLS-1$
		this.stop.addBusChangeListener(this.eventHandler);
		this.busNetwork.addBusStop(this.stop);
		this.eventHandler.clear();
	}

	@After
	public void tearDown() throws Exception {
		this.stop = null;
		this.busNetwork = null;
	}

	@Test
	public void testGetBusNetwork() {
		assertSame(this.busNetwork, this.stop.getBusNetwork());
	}

	@Test
	public void testSetPositionGeoLocationPoint() {
		assertNull(this.stop.getGeoPosition());

		this.eventHandler.clear();
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		GeoLocationPoint p = this.stop.getGeoPosition();
		assertNotNull(p);
		assertEpsilonEquals(34f, p.getX());
		assertEpsilonEquals(35f, p.getY());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.stop);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_CHANGED, this.stop);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testGetGeoPosition() {
		assertNull(this.stop.getGeoPosition());
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		GeoLocationPoint p = this.stop.getGeoPosition();
		assertNotNull(p);
		assertEpsilonEquals(34f, p.getX());
		assertEpsilonEquals(35f, p.getY());
	}

	@Test
	public void testGetPosition2D() {
		assertNull(this.stop.getGeoPosition());
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		Point2d p = this.stop.getPosition2D();
		assertNotNull(p);
		assertEpsilonEquals(34., p.getX());
		assertEpsilonEquals(35., p.getY());
	}

	@Test
	public void testGetGeoLocation() {
		assertNull(this.stop.getGeoPosition());
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		GeoLocation p = this.stop.getGeoLocation();
		assertNotNull(p);
		assertTrue(p instanceof GeoLocationPoint);
		assertEpsilonEquals(34f, ((GeoLocationPoint)p).getX());
		assertEpsilonEquals(35f, ((GeoLocationPoint)p).getY());
	}

	@Test
	public void testDistanceDoubleDouble() {
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		double dist = this.stop.distance(0., 0.);
		assertEpsilonEquals(Math.hypot(34., 35.), dist);
	}

	@Test
	public void testDistanceBusStopTest() {
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		BusStop stop2 = new BusStop("BUSSTOP2"); //$NON-NLS-1$
		stop2.setPosition(new GeoLocationPoint(-34., -30.));
		double dist = this.stop.distance(stop2);
		assertTrue(Double.isNaN(dist));
		this.busNetwork.addBusStop(stop2);
		dist = this.stop.distance(stop2);
		assertEpsilonEquals(Math.hypot(34.-(-34.), 35.-(-30.)), dist);
		double dist2 = stop2.distance(this.stop);
		assertEpsilonEquals(dist, dist2);
	}

	@Test
	public void testDistancePoint2d() {
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		double dist = this.stop.distance(new Point2d(0., 0.));
		assertEpsilonEquals(Math.hypot(34.,35.), dist);
	}

	@Test
	public void testDistanceGeoLocationPoint() {
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		double dist = this.stop.distance(new GeoLocationPoint(0., 0.));
		assertEpsilonEquals(Math.hypot(34.,35.), dist);
	}

	@Test
	public void testInsideBusHub() {
		BusHub hub = new BusHub(this.busNetwork, "MYSTATION"); //$NON-NLS-1$
		assertFalse(this.stop.insideBusHub());
		this.stop.addBusHub(hub);
		assertTrue(this.stop.insideBusHub());
	}

	@Test
	public void testBusHubs() {
		BusHub hub = new BusHub(this.busNetwork, "MYSTATION"); //$NON-NLS-1$

		Iterable<BusHub> hubs;
		Iterator<BusHub> iterator;

		hubs = this.stop.busHubs();
		assertNotNull(hubs);
		iterator = hubs.iterator();
		assertFalse(iterator.hasNext());

		this.stop.addBusHub(hub);

		hubs = this.stop.busHubs();
		assertNotNull(hubs);
		iterator = hubs.iterator();
		assertTrue(iterator.hasNext());
		assertSame(hub, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBusHubIterator() {
		BusHub hub = new BusHub(this.busNetwork, "MYSTATION"); //$NON-NLS-1$

		Iterator<BusHub> iterator;

		iterator = this.stop.busHubIterator();
		assertFalse(iterator.hasNext());

		this.stop.addBusHub(hub);

		iterator = this.stop.busHubIterator();
		assertTrue(iterator.hasNext());
		assertSame(hub, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testRemoveBusHubBusHub() {
		BusHub hub = new BusHub(this.busNetwork, "MYSTATION"); //$NON-NLS-1$
		this.stop.addBusHub(hub);

		Iterator<BusHub> iterator;

		this.eventHandler.clear();
		this.stop.removeBusHub(hub);

		iterator = this.stop.busHubIterator();
		assertFalse(iterator.hasNext());
		this.eventHandler.assertNoEvent();
	}

	@Test
	public void testIsValidPrimitive() {
		assertFalse(this.stop.isValidPrimitive());
		this.eventHandler.clear();
		this.stop.setPosition(new GeoLocationPoint(34., 35.));
		assertTrue(this.stop.isValidPrimitive());

		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.stop);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.STOP_CHANGED, this.stop);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testAddBusHalt() {
		BusItinerary itinerary = new BusItinerary();
		BusItineraryHalt halt = new BusItineraryHalt(itinerary, "HALT", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		Iterator<BusItineraryHalt> iterator;

		this.eventHandler.clear();
		this.stop.addBusHalt(halt);

		iterator = this.stop.getBindedBusHalts().iterator();
		assertTrue(iterator.hasNext());
		assertSame(halt, iterator.next());
		assertFalse(iterator.hasNext());
		this.eventHandler.assertNoEvent();
	}

	@Test
	public void testRemoveBusHalt() {
		BusItinerary itinerary = new BusItinerary();
		BusItineraryHalt halt = new BusItineraryHalt(itinerary, "HALT", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.stop.addBusHalt(halt);

		Iterator<BusItineraryHalt> iterator;

		this.eventHandler.clear();
		this.stop.removeBusHalt(halt);

		iterator = this.stop.getBindedBusHalts().iterator();
		assertFalse(iterator.hasNext());
		this.eventHandler.assertNoEvent();
	}

	@Test
	public void testIsBusHaltBinded() {
		BusItinerary itinerary = new BusItinerary();
		BusItineraryHalt halt = new BusItineraryHalt(itinerary, "HALT", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		assertFalse(this.stop.isBusHaltBinded());

		this.stop.addBusHalt(halt);

		assertTrue(this.stop.isBusHaltBinded());

		this.stop.removeBusHalt(halt);

		assertFalse(this.stop.isBusHaltBinded());
	}

	@Test
	public void testGetBindedBusHalts() {
		BusItinerary itinerary = new BusItinerary();
		BusItineraryHalt halt = new BusItineraryHalt(itinerary, "HALT", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$

		Iterator<BusItineraryHalt> iterator;

		iterator = this.stop.getBindedBusHalts().iterator();
		assertFalse(iterator.hasNext());

		this.stop.addBusHalt(halt);

		iterator = this.stop.getBindedBusHalts().iterator();
		assertTrue(iterator.hasNext());
		assertSame(halt, iterator.next());
		assertFalse(iterator.hasNext());

		this.stop.removeBusHalt(halt);

		iterator = this.stop.getBindedBusHalts().iterator();
		assertFalse(iterator.hasNext());
	}

}

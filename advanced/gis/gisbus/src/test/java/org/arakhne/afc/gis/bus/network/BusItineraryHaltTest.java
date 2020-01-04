/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
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
public class BusItineraryHaltTest extends AbstractTestCase {

	private final TestEventHandler eventHandler = new TestEventHandler();
	private RoadNetwork roadNetwork;
	private RoadPolyline segment1;
	private RoadPolyline segment2;
	private RoadPolyline segment3;
	private BusItineraryStub itinerary;
	private BusItineraryHalt halt;
	private BusStop stop;
	private BusNetwork busNetwork;

	@BeforeEach
	public void setUp() throws Exception {
		this.roadNetwork = new StandardRoadNetwork(new Rectangle2d(-2000, -2000, 4000, 4000));
		this.busNetwork = new BusNetwork(this.roadNetwork);
		this.segment1 = new RoadPolyline();
		this.segment1.addPoint(new Point2d(-10.,150.));
		this.segment1.addPoint(new Point2d(10.,10.));
		this.segment1.addPoint(new Point2d(100.,100.));
		this.segment1.setWidth(2.);
		this.roadNetwork.addRoadSegment(this.segment1);
		this.segment2 = new RoadPolyline();
		this.segment2.addPoint(new Point2d(-10.,150.));
		this.segment2.addPoint(new Point2d(-500.,150.));
		this.segment2.addPoint(new Point2d(-510.,170.));
		this.segment2.setWidth(2.);
		this.roadNetwork.addRoadSegment(this.segment2);
		// segment3 is only to enlarge the bounds of the road network
		this.segment3 = new RoadPolyline();
		this.segment3.addPoint(new Point2d(-1000.,-1000.));
		this.segment3.addPoint(new Point2d(1000.,1000.));
		this.roadNetwork.addRoadSegment(this.segment3);
		this.itinerary = new BusItineraryStub(this.busNetwork);
		this.halt = new BusItineraryHalt(this.itinerary, "HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		this.stop = new BusStopStub(this.busNetwork, "STOP1"); //$NON-NLS-1$
		this.halt.addBusChangeListener(this.eventHandler);
		this.eventHandler.clear();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.halt.removeBusChangeListener(this.eventHandler);
		this.stop = null;
		this.halt = null;
		this.itinerary = null;
		this.segment1 = this.segment2 = null;
		this.roadNetwork = null;
		this.busNetwork = null;
	}

	@Test
	public void testGetBusNetwork() {
		assertSame(this.busNetwork, this.halt.getBusNetwork());
	}

	@Test
	public void testIsValidPrimitive() {
		assertFalse(this.halt.isValidPrimitive());

		this.halt.setBusStop(this.stop);
		assertFalse(this.halt.isValidPrimitive());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isValidPrimitive());

		this.halt.setPositionOnSegment(3f);
		assertFalse(this.halt.isValidPrimitive());

		this.itinerary.addRoadSegment(this.segment1);
		assertFalse(this.halt.isValidPrimitive());

		this.stop.setPosition(new GeoLocationPoint(10., 10.));
		assertTrue(this.halt.isValidPrimitive());
	}

	@Test
	public void testGetBusStop() {
		assertNull(this.halt.getBusStop());

		this.halt.setBusStop(this.stop);
		assertSame(this.stop, this.halt.getBusStop());

		this.halt.setBusStop(null);
		assertNull(this.halt.getBusStop());
	}

	@Test
	public void testSetBusStop() {
		assertNull(this.halt.getBusStop());

		this.eventHandler.clear();
		assertTrue(this.halt.setBusStop(this.stop));
		assertSame(this.stop, this.halt.getBusStop());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.halt);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_CHANGED, this.halt);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.halt.setBusStop(this.stop));
		this.eventHandler.assertNoEvent();

		this.eventHandler.clear();
		assertTrue(this.halt.setBusStop(null));
		assertNull(this.halt.getBusStop());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.VALIDITY, this.halt);
		this.eventHandler.assertBusShapeChangedEvent(BusChangeEventType.ITINERARY_HALT_CHANGED, this.halt);
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		assertFalse(this.halt.setBusStop(null));
		this.eventHandler.assertNoEvent();
	}

	@Test
	public void testGetRoadSegment() {
		assertNull(this.halt.getRoadSegment());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		assertNull(this.halt.getRoadSegment());

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		assertSame(this.segment2, this.halt.getRoadSegment());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		assertSame(this.segment1, this.halt.getRoadSegment());

		this.halt.setRoadSegmentIndex(-1);
		this.halt.checkPrimitiveValidity();
		assertNull(this.halt.getRoadSegment());
	}

	@Test
	public void testGetRoadSegmentIndex() {
		assertEquals(-1, this.halt.getRoadSegmentIndex());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		assertEquals(0, this.halt.getRoadSegmentIndex());

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		assertEquals(0, this.halt.getRoadSegmentIndex());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		assertEquals(1, this.halt.getRoadSegmentIndex());

		this.halt.setRoadSegmentIndex(-1);
		this.halt.checkPrimitiveValidity();
		assertEquals(-1, this.halt.getRoadSegmentIndex());
	}

	@Test
	public void testGetPositionOnSegment() {
		assertTrue(Double.isNaN(this.halt.getPositionOnSegment()));

		this.halt.setPositionOnSegment(4f);
		assertEpsilonEquals(4f, this.halt.getPositionOnSegment());

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		assertEpsilonEquals(4f, this.halt.getPositionOnSegment());

		this.halt.setPositionOnSegment(6f);
		assertEpsilonEquals(6f, this.halt.getPositionOnSegment());

		this.halt.setPositionOnSegment(Float.NaN);
		assertTrue(Double.isNaN(this.halt.getPositionOnSegment()));
	}

	@Test
	public void testGetType() {
		assertEquals(BusItineraryHaltType.STOP_ON_DEMAND, this.halt.getType());
		this.halt.setType(BusItineraryHaltType.SYSTEMATIC_STOP);
		assertEquals(BusItineraryHaltType.SYSTEMATIC_STOP, this.halt.getType());
		this.halt.setType(null);
		assertEquals(BusItineraryHaltType.SYSTEMATIC_STOP, this.halt.getType());
		this.halt.setType(BusItineraryHaltType.STOP_ON_DEMAND);
		assertEquals(BusItineraryHaltType.STOP_ON_DEMAND, this.halt.getType());
	}

	@Test
	public void testSetType() {
		assertEquals(BusItineraryHaltType.STOP_ON_DEMAND, this.halt.getType());

		this.eventHandler.clear();
		this.halt.setType(BusItineraryHaltType.SYSTEMATIC_STOP);
		assertEquals(BusItineraryHaltType.SYSTEMATIC_STOP, this.halt.getType());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.ITINERARY_HALT_CHANGED, this.halt);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();

		this.eventHandler.clear();
		this.halt.setType(null);
		assertEquals(BusItineraryHaltType.SYSTEMATIC_STOP, this.halt.getType());
		this.eventHandler.assertNoEvent();

		this.eventHandler.clear();
		this.halt.setType(BusItineraryHaltType.STOP_ON_DEMAND);
		assertEquals(BusItineraryHaltType.STOP_ON_DEMAND, this.halt.getType());
		this.eventHandler.assertBusChangedEvent(BusChangeEventType.ITINERARY_HALT_CHANGED, this.halt);
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertNoBusShapeAttrChangedEvent();
	}

	@Test
	public void testGetRoadSegmentDirection() {
		assertNull(this.halt.getRoadSegmentDirection());

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		assertNull(this.halt.getRoadSegmentDirection());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		assertEquals(Direction1D.REVERTED_DIRECTION, this.halt.getRoadSegmentDirection());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.halt.getRoadSegmentDirection());
	}

	@Test
	public void testGetPosition1D5() {
		Point1d p;

		assertNull(this.halt.getRoadSegment());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getPosition1D();
		assertNull(p);

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		p = this.halt.getPosition1D();
		assertNull(p);

		this.halt.setPositionOnSegment(4f);
		p = this.halt.getPosition1D();
		assertNotNull(p);
		assertEpsilonEquals(4., p.getCurvilineCoordinate());
		assertEpsilonEquals(2., p.getLateralDistance());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getPosition1D();
		assertNotNull(p);
		assertEpsilonEquals(4., p.getCurvilineCoordinate());
		assertEpsilonEquals(-2, p.getLateralDistance());

		this.halt.setRoadSegmentIndex(-1);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getPosition1D();
		assertNull(p);
	}

	/**
	 * Coordinate computation on segment2:
	 * <pre>
	 * Position 1D5 = (4,1)
	 * Vector = p1-p0 = (-500;150) - (-10;150) = (-490;0)
	 * Length = 490
	 * Unit Vector = (-1;0)
	 * 4 meters vector = 4*Unit Vector = (-4;0)
	 * 4 meters point = p0 + 4 meters vector = (-14;150)
	 * Perpendicular Unit Vector = (0;-4) = (0;-1)
	 * Jutting Vector = Jutting * Perpendicular Unit Vector = 1 * (0;-1) = (0;-1)
	 * Shifted point = 4 meters point + perpendicular unit vector
	 *               = (-14;150) + (0;-1) = (-14;149)
	 * </pre>
	 * <p>
	 * Coordinate computation on segment1:
	 * <pre>
	 * Position 1D5 = (4,-1)
	 * Vector = p1-p0 = (10;10) - (-10;150) = (20;-140)
	 * Length = 141.421356237
	 * Unit Vector = (0.141421356;-0.989949494)
	 * 4 meters vector = 4*Unit Vector = (0.565685424;-3.959797975)
	 * 4 meters point = p0 + 4 meters vector = (-9.434314576;146.040202024)
	 * Perpendicular Unit Vector = (0.9899494936611666;0.14142135623730953)
	 * Jutting Vector = Jutting * Perpendicular Unit Vector = -1 * (0.9899494936611666;0.14142135623730953) = (-0.9899494936611666;-0.14142135623730953)
	 * Shifted point = 4 meters point + perpendicular unit vector
	 *               = (-9.434314576;146.040202024) + (-0.9899494936611666;-0.14142135623730953)
	 *               = (-10.424;145.899)
	 * </pre>
	 */
	@Test
	public void testGetPosition2D() {
		Point2d p;

		assertNull(this.halt.getRoadSegment());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getPosition2D();
		assertNull(p);

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		p = this.halt.getPosition2D();
		assertNull(p);

		this.halt.setPositionOnSegment(4f);
		p = this.halt.getPosition2D();
		assertNotNull(p);
		assertEpsilonEquals(-14., p.getX());
		assertEpsilonEquals(148., p.getY());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getPosition2D();
		assertNotNull(p);
		setDecimalPrecision(3);
		assertEpsilonEquals(-11.41421, p.getX());
		assertEpsilonEquals(145.899, p.getY());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);

		this.halt.setRoadSegmentIndex(-1);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getPosition2D();
		assertNull(p);
	}

	/**
	 * Coordinate computation on segment2:
	 * <pre>
	 * Position 1D5 = (4,1)
	 * Vector = p1-p0 = (-500;150) - (-10;150) = (-490;0)
	 * Length = 490
	 * Unit Vector = (-1;0)
	 * 4 meters vector = 4*Unit Vector = (-4;0)
	 * 4 meters point = p0 + 4 meters vector = (-14;150)
	 * Perpendicular Unit Vector = (0;-4) = (0;-1)
	 * Jutting Vector = Jutting * Perpendicular Unit Vector = 1 * (0;-1) = (0;-1)
	 * Shifted point = 4 meters point + perpendicular unit vector
	 *               = (-14;150) + (0;-1) = (-14;149)
	 * GeoPosition (rounded to one decimal digit) = (-14;149)
	 * </pre>
	 * <p>
	 * Coordinate computation on segment1:
	 * <pre>
	 * Position 1D5 = (4,-1)
	 * Vector = p1-p0 = (10;10) - (-10;150) = (20;-140)
	 * Length = 141.421356237
	 * Unit Vector = (0.141421356;-0.989949494)
	 * 4 meters vector = 4*Unit Vector = (0.565685424;-3.959797975)
	 * 4 meters point = p0 + 4 meters vector = (-9.434314576;146.040202024)
	 * Perpendicular Unit Vector = (0.9899494936611666;0.14142135623730953)
	 * Jutting Vector = Jutting * Perpendicular Unit Vector = -1 * (0.9899494936611666;0.14142135623730953) = (-0.9899494936611666;-0.14142135623730953)
	 * Shifted point = 4 meters point + perpendicular unit vector
	 *               = (-9.434314576;146.040202024) + (-0.9899494936611666;-0.14142135623730953)
	 *               = (-10.424;145.899)
	 * GeoPosition (rounded to one decimal digit) = (-10.4;145.9)
	 * </pre>
	 */
	@Test
	public void testGetGeoPosition() {
		GeoLocationPoint p;

		assertNull(this.halt.getRoadSegment());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getGeoPosition();
		assertNull(p);

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		p = this.halt.getGeoPosition();
		assertNull(p);

		this.halt.setPositionOnSegment(4f);
		p = this.halt.getGeoPosition();
		assertNotNull(p);
		assertEpsilonEquals(-14f, p.getX());
		assertEpsilonEquals(148f, p.getY());

		this.halt.setRoadSegmentIndex(1);
		p = this.halt.getGeoPosition();
		assertNotNull(p);
		setDecimalPrecision(3);
		assertEpsilonEquals(-11.41421f, p.getX());
		assertEpsilonEquals(145.9f, p.getY());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);

		this.halt.setRoadSegmentIndex(-1);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getGeoPosition();
		assertNull(p);
	}

	/**
	 * Coordinate computation on segment2:
	 * <pre>
	 * Position 1D5 = (4,1)
	 * Vector = p1-p0 = (-500;150) - (-10;150) = (-490;0)
	 * Length = 490
	 * Unit Vector = (-1;0)
	 * 4 meters vector = 4*Unit Vector = (-4;0)
	 * 4 meters point = p0 + 4 meters vector = (-14;150)
	 * Perpendicular Unit Vector = (0;-4) = (0;-1)
	 * Jutting Vector = Jutting * Perpendicular Unit Vector = 1 * (0;-1) = (0;-1)
	 * Shifted point = 4 meters point + perpendicular unit vector
	 *               = (-14;150) + (0;-1) = (-14;149)
	 * GeoPosition (rounded to one decimal digit) = (-14;149)
	 * </pre>
	 * <p>
	 * Coordinate computation on segment1:
	 * <pre>
	 * Position 1D5 = (4,-1)
	 * Vector = p1-p0 = (10;10) - (-10;150) = (20;-140)
	 * Length = 141.421356237
	 * Unit Vector = (0.141421356;-0.989949494)
	 * 4 meters vector = 4*Unit Vector = (0.565685424;-3.959797975)
	 * 4 meters point = p0 + 4 meters vector = (-9.434314576;146.040202024)
	 * Perpendicular Unit Vector = (0.9899494936611666;0.14142135623730953)
	 * Jutting Vector = Jutting * Perpendicular Unit Vector = -1 * (0.9899494936611666;0.14142135623730953) = (-0.9899494936611666;-0.14142135623730953)
	 * Shifted point = 4 meters point + perpendicular unit vector
	 *               = (-9.434314576;146.040202024) + (-0.9899494936611666;-0.14142135623730953)
	 *               = (-10.424;145.899)
	 * GeoPosition (rounded to one decimal digit) = (-10.4;145.9)
	 * </pre>
	 */
	@Test
	public void testGetGeoLocation() {
		GeoLocation p;

		assertNull(this.halt.getRoadSegment());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getGeoLocation();
		assertNotNull(p);
		assertTrue(p instanceof GeoLocationNowhere);

		this.itinerary.addRoadSegment(this.segment2);
		this.itinerary.addRoadSegment(this.segment1);
		p = this.halt.getGeoLocation();
		assertNotNull(p);
		assertTrue(p instanceof GeoLocationNowhere);

		this.halt.setPositionOnSegment(4f);
		p = this.halt.getGeoLocation();
		assertNotNull(p);
		assertTrue(p instanceof GeoLocationPoint);
		assertEpsilonEquals(-14f, ((GeoLocationPoint)p).getX());
		assertEpsilonEquals(148f, ((GeoLocationPoint)p).getY());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getGeoLocation();
		assertNotNull(p);
		assertTrue(p instanceof GeoLocationPoint);
		setDecimalPrecision(3);
		assertEpsilonEquals(-11.41421f, ((GeoLocationPoint)p).getX());
		assertEpsilonEquals(145.9f, ((GeoLocationPoint)p).getY());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);

		this.halt.setRoadSegmentIndex(-1);
		this.halt.checkPrimitiveValidity();
		p = this.halt.getGeoLocation();
		assertNotNull(p);
		assertTrue(p instanceof GeoLocationNowhere);
	}

	@Test
	public void testIsStartingBusHalt() {
		this.halt = this.itinerary.addBusHalt("HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		assertFalse(this.halt.isStartingBusHalt());

		this.itinerary.addRoadSegment(this.segment1);
		assertFalse(this.halt.isStartingBusHalt());

		this.itinerary.addRoadSegment(this.segment2);
		assertFalse(this.halt.isStartingBusHalt());

		this.stop.setPosition(new GeoLocationPoint(10., 10.));
		assertFalse(this.halt.isStartingBusHalt());

		this.halt.setBusStop(this.stop);
		assertFalse(this.halt.isStartingBusHalt());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isStartingBusHalt());

		this.halt.setPositionOnSegment(1f);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isStartingBusHalt());

		BusStop tmpStop = new BusStopStub(this.busNetwork, "STOP_TMP"); //$NON-NLS-1$
		tmpStop.setPosition(new GeoLocationPoint(-10., -10.));
		BusItineraryHalt tmpHalt = this.itinerary.addBusHalt("HALT_TMP", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		tmpHalt.setBusStop(tmpStop);
		tmpHalt.setRoadSegmentIndex(1);
		tmpHalt.setPositionOnSegment(1f);
		tmpHalt.checkPrimitiveValidity();

		assertTrue(this.halt.isStartingBusHalt());
	}

	@Test
	public void testIsEndingBusHalt() {
		this.halt = this.itinerary.addBusHalt("HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		assertFalse(this.halt.isEndingBusHalt());

		this.itinerary.addRoadSegment(this.segment1);
		assertFalse(this.halt.isEndingBusHalt());

		this.itinerary.addRoadSegment(this.segment2);
		assertFalse(this.halt.isEndingBusHalt());

		this.stop.setPosition(new GeoLocationPoint(10., 10.));
		assertFalse(this.halt.isEndingBusHalt());

		this.halt.setBusStop(this.stop);
		assertFalse(this.halt.isEndingBusHalt());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isEndingBusHalt());

		this.halt.setPositionOnSegment(1f);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isEndingBusHalt());

		BusStop tmpStop = new BusStopStub(this.busNetwork, "STOP_TMP"); //$NON-NLS-1$
		tmpStop.setPosition(new GeoLocationPoint(-10., -10.));
		BusItineraryHalt tmpHalt = this.itinerary.addBusHalt("HALT_TMP", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		tmpHalt.setBusStop(tmpStop);
		tmpHalt.setRoadSegmentIndex(0);
		tmpHalt.setPositionOnSegment(1f);
		tmpHalt.checkPrimitiveValidity();

		assertTrue(this.halt.isEndingBusHalt());
	}

	@Test
	public void testIsTerminus_1() {
		this.halt = this.itinerary.addBusHalt("HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		assertFalse(this.halt.isTerminus());

		this.itinerary.addRoadSegment(this.segment1);
		assertFalse(this.halt.isTerminus());

		this.itinerary.addRoadSegment(this.segment2);
		assertFalse(this.halt.isTerminus());

		this.stop.setPosition(new GeoLocationPoint(10., 10.));
		assertFalse(this.halt.isTerminus());

		this.halt.setBusStop(this.stop);
		assertFalse(this.halt.isTerminus());

		this.halt.setRoadSegmentIndex(0);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isTerminus());

		this.halt.setPositionOnSegment(1f);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isTerminus());

		BusStop tmpStop = new BusStopStub(this.busNetwork, "STOP_TMP"); //$NON-NLS-1$
		tmpStop.setPosition(new GeoLocationPoint(-10., -10.));
		BusItineraryHalt tmpHalt = this.itinerary.addBusHalt("HALT_TMP", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		tmpHalt.setBusStop(tmpStop);
		tmpHalt.setRoadSegmentIndex(1);
		tmpHalt.setPositionOnSegment(1f);
		tmpHalt.checkPrimitiveValidity();

		assertTrue(this.halt.isTerminus());
	}

	@Test
	public void testIsTerminus_2() {
		this.halt = this.itinerary.addBusHalt("HALT1", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		assertFalse(this.halt.isTerminus());

		this.itinerary.addRoadSegment(this.segment1);
		assertFalse(this.halt.isTerminus());

		this.itinerary.addRoadSegment(this.segment2);
		assertFalse(this.halt.isTerminus());

		this.stop.setPosition(new GeoLocationPoint(10., 10.));
		assertFalse(this.halt.isTerminus());

		this.halt.setBusStop(this.stop);
		assertFalse(this.halt.isTerminus());

		this.halt.setRoadSegmentIndex(1);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isTerminus());

		this.halt.setPositionOnSegment(1f);
		this.halt.checkPrimitiveValidity();
		assertFalse(this.halt.isTerminus());

		BusStop tmpStop = new BusStopStub(this.busNetwork, "STOP_TMP"); //$NON-NLS-1$
		tmpStop.setPosition(new GeoLocationPoint(-10., -10.));
		BusItineraryHalt tmpHalt = this.itinerary.addBusHalt("HALT_TMP", BusItineraryHaltType.STOP_ON_DEMAND); //$NON-NLS-1$
		tmpHalt.setBusStop(tmpStop);
		tmpHalt.setRoadSegmentIndex(0);
		tmpHalt.setPositionOnSegment(1f);
		tmpHalt.checkPrimitiveValidity();

		assertTrue(this.halt.isTerminus());
	}

}

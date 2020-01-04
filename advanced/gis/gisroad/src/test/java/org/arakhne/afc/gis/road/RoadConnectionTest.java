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

package org.arakhne.afc.gis.road;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadConnection.ClockwiseBoundType;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Unit test for RoadConnection.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class RoadConnectionTest extends AbstractGisTest {

	private RoadConnectionStub connection1;
	private RoadSegmentStub segment1;
	private RoadConnectionStub connection2;
	private RoadSegmentStub segment2;
	private RoadConnectionStub connection3;
	private RoadSegmentStub segment3a;
	private RoadSegmentStub segment3b;
	private RoadConnectionStub connection4;
	private RoadConnectionStub connection5;
	private RoadSegmentStub segment5a;
	private RoadSegmentStub segment5b;
	private RoadSegmentStub segment5c;
	private RoadSegmentStub segment5d;
	private RoadSegmentStub segment5e;

	@BeforeEach
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
		this.segment1 = new RoadSegmentStub("segment1"); //$NON-NLS-1$
		this.connection1 = new RoadConnectionStub("connection1"); //$NON-NLS-1$
		this.segment1.setStartPoint(this.connection1);

		this.segment2 = new RoadSegmentStub("segment2"); //$NON-NLS-1$
		this.connection2 = new RoadConnectionStub("connection2"); //$NON-NLS-1$
		this.segment2.setEndPoint(this.connection2);

		this.segment3a = new RoadSegmentStub("segment3a"); //$NON-NLS-1$
		this.segment3b = new RoadSegmentStub("segment3b"); //$NON-NLS-1$
		this.connection3 = new RoadConnectionStub("connection3"); //$NON-NLS-1$
		Point2d p0 = this.segment3a.getPointAt(0);
		Point2d p1 = this.segment3b.getPointAt(this.segment3b.getPointCount()-1);
		Point2d p = new Point2d(
				(p0.getX()+p1.getX())/2.,
				(p0.getY()+p1.getY())/2.);
		this.segment3a.setPointAt(0,p);
		this.segment3b.setPointAt(this.segment3b.getPointCount()-1,p);
		this.segment3a.setStartPoint(this.connection3);
		this.segment3b.setEndPoint(this.connection3);

		this.connection4 = new RoadConnectionStub("connection4"); //$NON-NLS-1$

		this.segment5a = new RoadSegmentStub("segment5a",100,0); //$NON-NLS-1$
		this.segment5b = new RoadSegmentStub("segment5b",50, -100); //$NON-NLS-1$
		this.segment5c = new RoadSegmentStub("segment5c",0,-5); //$NON-NLS-1$
		this.segment5d = new RoadSegmentStub("segment5d",-20,4); //$NON-NLS-1$
		this.segment5e = new RoadSegmentStub("segment5e",50,100); //$NON-NLS-1$
		this.connection5 = new RoadConnectionStub("connection5"); //$NON-NLS-1$
		this.segment5a.setEndPoint(this.connection5);
		this.segment5b.setEndPoint(this.connection5);
		this.segment5c.setEndPoint(this.connection5);
		this.segment5d.setEndPoint(this.connection5);
		this.segment5e.setEndPoint(this.connection5);
	}

	@AfterEach
	public void tearDown() {
		this.connection1 = null;
		this.segment1 = null;
		this.connection2 = null;
		this.segment2 = null;
		this.connection3 = null;
		this.segment3a = null;
		this.segment3b = null;
		this.connection4 = null;
		this.connection5 = null;
		this.segment5a = null;
		this.segment5b = null;
		this.segment5c = null;
		this.segment5d = null;
		this.segment5e = null;
	}

	@Test
	public void testIsEmpty() {
    	assertFalse(this.connection1.isEmpty());
    	assertFalse(this.connection2.isEmpty());
    	assertFalse(this.connection3.isEmpty());
    	assertTrue(this.connection4.isEmpty());
    	assertFalse(this.connection5.isEmpty());
	}

	@Test
    public void testGetConnectedSegmentCount() {
    	assertEquals(1, this.connection1.getConnectedSegmentCount());
    	assertEquals(1, this.connection2.getConnectedSegmentCount());
    	assertEquals(2, this.connection3.getConnectedSegmentCount());
    	assertEquals(0, this.connection4.getConnectedSegmentCount());
    	assertEquals(5, this.connection5.getConnectedSegmentCount());
    }

	@Test
	public void testIsConnectedSegment() {
    	assertTrue(this.connection1.isConnectedSegment(this.segment1));
    	assertFalse(this.connection1.isConnectedSegment(this.segment2));
    	assertFalse(this.connection1.isConnectedSegment(this.segment3a));
    	assertFalse(this.connection1.isConnectedSegment(this.segment3b));
    	assertFalse(this.connection1.isConnectedSegment(this.segment5a));
    	assertFalse(this.connection1.isConnectedSegment(this.segment5b));
    	assertFalse(this.connection1.isConnectedSegment(this.segment5c));
    	assertFalse(this.connection1.isConnectedSegment(this.segment5d));
    	assertFalse(this.connection1.isConnectedSegment(this.segment5e));

    	assertTrue(this.connection2.isConnectedSegment(this.segment2));
    	assertFalse(this.connection2.isConnectedSegment(this.segment1));
    	assertFalse(this.connection2.isConnectedSegment(this.segment3a));
    	assertFalse(this.connection2.isConnectedSegment(this.segment3b));
    	assertFalse(this.connection2.isConnectedSegment(this.segment5a));
    	assertFalse(this.connection2.isConnectedSegment(this.segment5b));
    	assertFalse(this.connection2.isConnectedSegment(this.segment5c));
    	assertFalse(this.connection2.isConnectedSegment(this.segment5d));
    	assertFalse(this.connection2.isConnectedSegment(this.segment5e));

    	assertTrue(this.connection3.isConnectedSegment(this.segment3a));
    	assertTrue(this.connection3.isConnectedSegment(this.segment3b));
    	assertFalse(this.connection3.isConnectedSegment(this.segment2));
    	assertFalse(this.connection3.isConnectedSegment(this.segment1));
    	assertFalse(this.connection3.isConnectedSegment(this.segment5a));
    	assertFalse(this.connection3.isConnectedSegment(this.segment5b));
    	assertFalse(this.connection3.isConnectedSegment(this.segment5c));
    	assertFalse(this.connection3.isConnectedSegment(this.segment5d));
    	assertFalse(this.connection3.isConnectedSegment(this.segment5e));

    	assertFalse(this.connection4.isConnectedSegment(this.segment1));
    	assertFalse(this.connection4.isConnectedSegment(this.segment2));
    	assertFalse(this.connection4.isConnectedSegment(this.segment3a));
    	assertFalse(this.connection4.isConnectedSegment(this.segment3b));
    	assertFalse(this.connection4.isConnectedSegment(this.segment5a));
    	assertFalse(this.connection4.isConnectedSegment(this.segment5b));
    	assertFalse(this.connection4.isConnectedSegment(this.segment5c));
    	assertFalse(this.connection4.isConnectedSegment(this.segment5d));
    	assertFalse(this.connection4.isConnectedSegment(this.segment5e));

    	assertFalse(this.connection5.isConnectedSegment(this.segment1));
    	assertFalse(this.connection5.isConnectedSegment(this.segment2));
    	assertFalse(this.connection5.isConnectedSegment(this.segment3a));
    	assertFalse(this.connection5.isConnectedSegment(this.segment3b));
    	assertTrue(this.connection5.isConnectedSegment(this.segment5a));
    	assertTrue(this.connection5.isConnectedSegment(this.segment5b));
    	assertTrue(this.connection5.isConnectedSegment(this.segment5c));
    	assertTrue(this.connection5.isConnectedSegment(this.segment5d));
    	assertTrue(this.connection5.isConnectedSegment(this.segment5e));
	}

	@Test
	public void testIsFinalConnectionPoint() {
    	assertTrue(this.connection1.isFinalConnectionPoint());
    	assertTrue(this.connection2.isFinalConnectionPoint());
    	assertFalse(this.connection3.isFinalConnectionPoint());
    	assertTrue(this.connection4.isFinalConnectionPoint());
    	assertFalse(this.connection5.isFinalConnectionPoint());
	}

	@Test
    public void testGetOtherSideSegment() {
    	assertNull(this.connection1.getOtherSideSegment(this.segment1));
    	assertNull(this.connection1.getOtherSideSegment(this.segment2));
    	assertNull(this.connection1.getOtherSideSegment(this.segment3a));
    	assertNull(this.connection1.getOtherSideSegment(this.segment3b));
    	assertNull(this.connection1.getOtherSideSegment(this.segment5a));
    	assertNull(this.connection1.getOtherSideSegment(this.segment5b));
    	assertNull(this.connection1.getOtherSideSegment(this.segment5c));
    	assertNull(this.connection1.getOtherSideSegment(this.segment5d));
    	assertNull(this.connection1.getOtherSideSegment(this.segment5e));

    	assertNull(this.connection2.getOtherSideSegment(this.segment2));
    	assertNull(this.connection2.getOtherSideSegment(this.segment1));
    	assertNull(this.connection2.getOtherSideSegment(this.segment3a));
    	assertNull(this.connection2.getOtherSideSegment(this.segment3b));
    	assertNull(this.connection2.getOtherSideSegment(this.segment5a));
    	assertNull(this.connection2.getOtherSideSegment(this.segment5b));
    	assertNull(this.connection2.getOtherSideSegment(this.segment5c));
    	assertNull(this.connection2.getOtherSideSegment(this.segment5d));
    	assertNull(this.connection2.getOtherSideSegment(this.segment5e));

    	assertEquals(this.segment3b,this.connection3.getOtherSideSegment(this.segment3a));
    	assertEquals(this.segment3a,this.connection3.getOtherSideSegment(this.segment3b));
    	assertNull(this.connection3.getOtherSideSegment(this.segment2));
    	assertNull(this.connection3.getOtherSideSegment(this.segment1));
    	assertNull(this.connection3.getOtherSideSegment(this.segment5a));
    	assertNull(this.connection3.getOtherSideSegment(this.segment5b));
    	assertNull(this.connection3.getOtherSideSegment(this.segment5c));
    	assertNull(this.connection3.getOtherSideSegment(this.segment5d));
    	assertNull(this.connection3.getOtherSideSegment(this.segment5e));

    	assertNull(this.connection4.getOtherSideSegment(this.segment1));
    	assertNull(this.connection4.getOtherSideSegment(this.segment2));
    	assertNull(this.connection4.getOtherSideSegment(this.segment3a));
    	assertNull(this.connection4.getOtherSideSegment(this.segment3b));
    	assertNull(this.connection4.getOtherSideSegment(this.segment5a));
    	assertNull(this.connection4.getOtherSideSegment(this.segment5b));
    	assertNull(this.connection4.getOtherSideSegment(this.segment5c));
    	assertNull(this.connection4.getOtherSideSegment(this.segment5d));
    	assertNull(this.connection4.getOtherSideSegment(this.segment5e));

    	assertNull(this.connection5.getOtherSideSegment(this.segment1));
    	assertNull(this.connection5.getOtherSideSegment(this.segment2));
    	assertNull(this.connection5.getOtherSideSegment(this.segment3a));
    	assertNull(this.connection5.getOtherSideSegment(this.segment3b));
    	assertNull(this.connection5.getOtherSideSegment(this.segment5a));
    	assertNull(this.connection5.getOtherSideSegment(this.segment5b));
    	assertNull(this.connection5.getOtherSideSegment(this.segment5c));
    	assertNull(this.connection5.getOtherSideSegment(this.segment5d));
    	assertNull(this.connection5.getOtherSideSegment(this.segment5e));
    }

	@Test
    public void testGetPoint() {
		RoadConnection con = this.segment1.getBeginPoint(StandardRoadConnection.class);
		assertEquals(this.connection1,con);
		Point2d p = con.getPoint();
		assertEquals(this.segment1.getPointAt(0), p);
		con = this.segment1.getEndPoint();
		assertNull(con);

		con = this.segment2.getEndPoint();
		assertEquals(this.connection2,con);
		p = con.getPoint();
		assertEquals(this.segment2.getPointAt(this.segment2.getPointCount()-1), p);
		con = this.segment2.getBeginPoint(StandardRoadConnection.class);
		assertNull(con);

		con = this.segment3a.getBeginPoint(StandardRoadConnection.class);
		assertEquals(this.connection3,con);
		p = con.getPoint();
		assertEquals(this.segment3a.getPointAt(0), p);
		con = this.segment1.getEndPoint();
		assertNull(con);

		con = this.segment3b.getEndPoint();
		assertEquals(this.connection3,con);
		p = con.getPoint();
		assertEquals(this.segment3b.getPointAt(this.segment3b.getPointCount()-1), p);
		con = this.segment3b.getBeginPoint(StandardRoadConnection.class);
		assertNull(con);
	}

	/**
	 * @param boundType
	 */
    protected void runTestToCounterclockwiseIterator(ClockwiseBoundType boundType) {

    	//------------------------------------------ 1

    	Iterator<RoadSegment> itSegments = this.connection1.toCounterclockwiseIterator(this.segment1, this.segment1, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toCounterclockwiseIterator(this.segment2, this.segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toCounterclockwiseIterator(this.segment1, this.segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toCounterclockwiseIterator(null, this.segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toCounterclockwiseIterator(this.segment1, null, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
           	assertEquals(this.segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 3

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3a, this.segment3a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(this.segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3b, this.segment3b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3a, this.segment3b, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3b, this.segment3a, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment2, this.segment3a, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment2, this.segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3a, this.segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3a, null, boundType);
    	if (boundType.includeStart()) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(this.segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3b, this.segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3b, null, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 4

    	itSegments = this.connection4.toCounterclockwiseIterator(this.segment3b, this.segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 5

    	itSegments = this.connection5.toCounterclockwiseIterator(this.segment5a, this.segment5a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toCounterclockwiseIterator(this.segment5b, this.segment5b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toCounterclockwiseIterator(this.segment5c, this.segment5c, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toCounterclockwiseIterator(this.segment5d, this.segment5d, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toCounterclockwiseIterator(this.segment5e, this.segment5e, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5e,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toCounterclockwiseIterator(this.segment5a, this.segment5d, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5a,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5b,itSegments.next());
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5c,itSegments.next());
    	}
    	else {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5e,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toCounterclockwiseIterator(this.segment5d, this.segment5c, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5d,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment5e,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment5a,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment5b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());
    }

	/**
	 * @param boundType
	 */
    protected void runTestToClockwiseIterator(ClockwiseBoundType boundType) {

    	//------------------------------------------ 1

    	Iterator<RoadSegment> itSegments = this.connection1.toClockwiseIterator(this.segment1, this.segment1, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toClockwiseIterator(this.segment2, this.segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toClockwiseIterator(this.segment1, this.segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toClockwiseIterator(null, this.segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection1.toClockwiseIterator(this.segment1, null, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
           	assertEquals(this.segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 3

    	itSegments = this.connection3.toClockwiseIterator(this.segment3a, this.segment3a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(this.segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment3b, this.segment3b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment3a, this.segment3b, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment3b, this.segment3a, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment2, this.segment3a, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment2, this.segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toCounterclockwiseIterator(this.segment3a, this.segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment3a, null, boundType);
    	if (boundType.includeStart()) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(this.segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment3b, this.segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = this.connection3.toClockwiseIterator(this.segment3b, null, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 4

    	itSegments = this.connection4.toClockwiseIterator(this.segment3b, this.segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 5

    	itSegments = this.connection5.toClockwiseIterator(this.segment5a, this.segment5a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toClockwiseIterator(this.segment5b, this.segment5b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toClockwiseIterator(this.segment5c, this.segment5c, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toClockwiseIterator(this.segment5d, this.segment5d, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toClockwiseIterator(this.segment5e, this.segment5e, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5e,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toClockwiseIterator(this.segment5a, this.segment5d, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5a,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5e,itSegments.next());
    	}
    	else {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5b,itSegments.next());
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5c,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = this.connection5.toClockwiseIterator(this.segment5d, this.segment5c, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5d,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment5e,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment5a,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(this.segment5b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(this.segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());
    }

    /**
	 */
    public void testToCounterclockwiseIteratorIncludeBoth() {
    	runTestToCounterclockwiseIterator(ClockwiseBoundType.INCLUDE_BOTH);
    }

	/**
	 */
    public void testToCounterclockwiseIteratorExcludeStart() {
    	runTestToCounterclockwiseIterator(ClockwiseBoundType.EXCLUDE_START_SEGMENT);
    }

	/**
	 */
    public void testToCounterclockwiseIteratorExcludeEnd() {
    	runTestToCounterclockwiseIterator(ClockwiseBoundType.EXCLUDE_END_SEGMENT);
    }

	/**
	 */
    public void testToCounterclockwiseIteratorExcludeBoth() {
    	runTestToCounterclockwiseIterator(ClockwiseBoundType.EXCLUDE_BOTH);
    }

    /**
	 */
    public void testToClockwiseIteratorIncludeBoth() {
    	runTestToClockwiseIterator(ClockwiseBoundType.INCLUDE_BOTH);
    }

	/**
	 */
    public void testToClockwiseIteratorExcludeStart() {
    	runTestToClockwiseIterator(ClockwiseBoundType.EXCLUDE_START_SEGMENT);
    }

	/**
	 */
    public void testToClockwiseIteratorExcludeEnd() {
    	runTestToClockwiseIterator(ClockwiseBoundType.EXCLUDE_END_SEGMENT);
    }

	/**
	 */
    public void testToClockwiseIteratorExcludeBoth() {
    	runTestToClockwiseIterator(ClockwiseBoundType.EXCLUDE_BOTH);
    }

    /**
	 */
    public void testToCounterclockwiseIteratorRemove() {
    	// Check the initial content of the connection
    	List<RoadSegment> segments = new ArrayList<>();
    	for(RoadSegment s : this.connection5.getConnectedSegments()) {
    		segments.add(s);
    	}
    	assertEquals(5,segments.size());
    	assertEquals(this.segment5a, segments.get(0));
    	assertEquals(this.segment5b, segments.get(1));
    	assertEquals(this.segment5c, segments.get(2));
    	assertEquals(this.segment5d, segments.get(3));
    	assertEquals(this.segment5e, segments.get(4));

    	// Use the iterator and its removal operation
    	Iterator<RoadSegment> itSegments =
    		CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()
    		? this.connection5.toCounterclockwiseIterator(this.segment5d, this.segment5c)
    		: this.connection5.toClockwiseIterator(this.segment5d, this.segment5c);
    	assertTrue(itSegments.hasNext());
    	assertEquals(this.segment5d,itSegments.next());
    	assertTrue(itSegments.hasNext());
    	assertEquals(this.segment5e,itSegments.next());
    	itSegments.remove();
    	assertTrue(itSegments.hasNext());
    	assertEquals(this.segment5a,itSegments.next());
    	assertTrue(itSegments.hasNext());
    	assertEquals(this.segment5b,itSegments.next());
    	itSegments.remove();
    	assertTrue(itSegments.hasNext());
    	assertEquals(this.segment5c,itSegments.next());
    	assertFalse(itSegments.hasNext());

    	// Check the list of the connected segments
    	segments = new ArrayList<>();
    	for(RoadSegment s : this.connection5.getConnectedSegments()) {
    		segments.add(s);
    	}
    	assertEquals(3,segments.size());
    	assertEquals(this.segment5a, segments.get(0));
    	assertEquals(this.segment5c, segments.get(1));
    	assertEquals(this.segment5d, segments.get(2));
    }

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadConnection.ClockwiseBoundType;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Unit test for RoadConnection.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("RoadConnection")
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
		segment1 = new RoadSegmentStub("segment1"); //$NON-NLS-1$
		connection1 = new RoadConnectionStub("connection1"); //$NON-NLS-1$
		segment1.setStartPoint(connection1);

		segment2 = new RoadSegmentStub("segment2"); //$NON-NLS-1$
		connection2 = new RoadConnectionStub("connection2"); //$NON-NLS-1$
		segment2.setEndPoint(connection2);

		segment3a = new RoadSegmentStub("segment3a"); //$NON-NLS-1$
		segment3b = new RoadSegmentStub("segment3b"); //$NON-NLS-1$
		connection3 = new RoadConnectionStub("connection3"); //$NON-NLS-1$
		Point2d p0 = segment3a.getPointAt(0);
		Point2d p1 = segment3b.getPointAt(segment3b.getPointCount()-1);
		Point2d p = new Point2d(
				(p0.getX()+p1.getX())/2.,
				(p0.getY()+p1.getY())/2.);
		segment3a.setPointAt(0,p);
		segment3b.setPointAt(segment3b.getPointCount()-1,p);
		segment3a.setStartPoint(connection3);
		segment3b.setEndPoint(connection3);

		connection4 = new RoadConnectionStub("connection4"); //$NON-NLS-1$

		segment5a = new RoadSegmentStub("segment5a",100,0); //$NON-NLS-1$
		segment5b = new RoadSegmentStub("segment5b",50, -100); //$NON-NLS-1$
		segment5c = new RoadSegmentStub("segment5c",0,-5); //$NON-NLS-1$
		segment5d = new RoadSegmentStub("segment5d",-20,4); //$NON-NLS-1$
		segment5e = new RoadSegmentStub("segment5e",50,100); //$NON-NLS-1$
		connection5 = new RoadConnectionStub("connection5"); //$NON-NLS-1$
		segment5a.setEndPoint(connection5);
		segment5b.setEndPoint(connection5);
		segment5c.setEndPoint(connection5);
		segment5d.setEndPoint(connection5);
		segment5e.setEndPoint(connection5);
	}

	@AfterEach
	public void tearDown() {
		connection1 = null;
		segment1 = null;
		connection2 = null;
		segment2 = null;
		connection3 = null;
		segment3a = null;
		segment3b = null;
		connection4 = null;
		connection5 = null;
		segment5a = null;
		segment5b = null;
		segment5c = null;
		segment5d = null;
		segment5e = null;
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmptyTest {

		@DisplayName("#1")
		@Test
		public void testIsEmpty_1() {
	    	assertFalse(connection1.isEmpty());
		}

		@DisplayName("#2")
		@Test
		public void testIsEmpty_2() {
	    	assertFalse(connection2.isEmpty());
		}

		@DisplayName("#3")
		@Test
		public void testIsEmpty_3() {
	    	assertFalse(connection3.isEmpty());
		}

		@DisplayName("#4")
		@Test
		public void testIsEmpty_4() {
	    	assertTrue(connection4.isEmpty());
		}

		@DisplayName("#5")
		@Test
		public void testIsEmpty_5() {
	    	assertFalse(connection5.isEmpty());
		}
	}

	@DisplayName("getConnectedSegmentCount")
	@Nested
	public class GetConnectedSegmentCountTest {

		@DisplayName("#1")
		@Test
	    public void testGetConnectedSegmentCount_1() {
	    	assertEquals(1, connection1.getConnectedSegmentCount());
		}

		@DisplayName("#2")
		@Test
	    public void testGetConnectedSegmentCount_2() {
	    	assertEquals(1, connection2.getConnectedSegmentCount());
		}

		@DisplayName("#3")
		@Test
	    public void testGetConnectedSegmentCount_3() {
	    	assertEquals(2, connection3.getConnectedSegmentCount());
		}

		@DisplayName("#4")
		@Test
	    public void testGetConnectedSegmentCount_4() {
	    	assertEquals(0, connection4.getConnectedSegmentCount());
		}

		@DisplayName("#5")
		@Test
	    public void testGetConnectedSegmentCount_5() {
	    	assertEquals(5, connection5.getConnectedSegmentCount());
	    }
	}

	@DisplayName("isConnectedSegment")
	@Nested
	public class IsConnectedSegmentTest {

		@DisplayName("#1")
		@Test
		public void testIsConnectedSegment_1() {
	    	assertTrue(connection1.isConnectedSegment(segment1));
		}

		@DisplayName("#2")
		@Test
		public void testIsConnectedSegment_2() {
	    	assertFalse(connection1.isConnectedSegment(segment2));
		}

		@DisplayName("#3")
		@Test
		public void testIsConnectedSegment_3() {
	    	assertFalse(connection1.isConnectedSegment(segment3a));
		}

		@DisplayName("#4")
		@Test
		public void testIsConnectedSegment_4() {
	    	assertFalse(connection1.isConnectedSegment(segment3b));
		}

		@DisplayName("#5")
		@Test
		public void testIsConnectedSegment_6() {
	    	assertFalse(connection1.isConnectedSegment(segment5a));
		}

		@DisplayName("#7")
		@Test
		public void testIsConnectedSegment_7() {
	    	assertFalse(connection1.isConnectedSegment(segment5b));
		}

		@DisplayName("#8")
		@Test
		public void testIsConnectedSegment_8() {
	    	assertFalse(connection1.isConnectedSegment(segment5c));
		}

		@DisplayName("#9")
		@Test
		public void testIsConnectedSegment_9() {
	    	assertFalse(connection1.isConnectedSegment(segment5d));
		}

		@DisplayName("#10")
		@Test
		public void testIsConnectedSegment_10() {
	    	assertFalse(connection1.isConnectedSegment(segment5e));
		}

		@DisplayName("#11")
		@Test
		public void testIsConnectedSegment_11() {
	    	assertTrue(connection2.isConnectedSegment(segment2));
		}

		@DisplayName("#12")
		@Test
		public void testIsConnectedSegment_12() {
	    	assertFalse(connection2.isConnectedSegment(segment1));
		}

		@DisplayName("#13")
		@Test
		public void testIsConnectedSegment_13() {
	    	assertFalse(connection2.isConnectedSegment(segment3a));
		}

		@DisplayName("#14")
		@Test
		public void testIsConnectedSegment_14() {
	    	assertFalse(connection2.isConnectedSegment(segment3b));
		}

		@DisplayName("#15")
		@Test
		public void testIsConnectedSegment_15() {
	    	assertFalse(connection2.isConnectedSegment(segment5a));
		}

		@DisplayName("#16")
		@Test
		public void testIsConnectedSegment_16() {
	    	assertFalse(connection2.isConnectedSegment(segment5b));
		}

		@DisplayName("#17")
		@Test
		public void testIsConnectedSegment_17() {
	    	assertFalse(connection2.isConnectedSegment(segment5c));
		}

		@DisplayName("#18")
		@Test
		public void testIsConnectedSegment_18() {
	    	assertFalse(connection2.isConnectedSegment(segment5d));
		}

		@DisplayName("#19")
		@Test
		public void testIsConnectedSegment_19() {
	    	assertFalse(connection2.isConnectedSegment(segment5e));
		}

		@DisplayName("#20")
		@Test
		public void testIsConnectedSegment_20() {
	    	assertTrue(connection3.isConnectedSegment(segment3a));
		}

		@DisplayName("#21")
		@Test
		public void testIsConnectedSegment_21() {
	    	assertTrue(connection3.isConnectedSegment(segment3b));
		}

		@DisplayName("#22")
		@Test
		public void testIsConnectedSegment_22() {
	    	assertFalse(connection3.isConnectedSegment(segment2));
		}

		@DisplayName("#23")
		@Test
		public void testIsConnectedSegment_23() {
	    	assertFalse(connection3.isConnectedSegment(segment1));
		}

		@DisplayName("#24")
		@Test
		public void testIsConnectedSegment_24() {
	    	assertFalse(connection3.isConnectedSegment(segment5a));
		}

		@DisplayName("#25")
		@Test
		public void testIsConnectedSegment_25() {
	    	assertFalse(connection3.isConnectedSegment(segment5b));
		}

		@DisplayName("#26")
		@Test
		public void testIsConnectedSegment_26() {
	    	assertFalse(connection3.isConnectedSegment(segment5c));
		}

		@DisplayName("#27")
		@Test
		public void testIsConnectedSegment_27() {
	    	assertFalse(connection3.isConnectedSegment(segment5d));
		}

		@DisplayName("#28")
		@Test
		public void testIsConnectedSegment_28() {
	    	assertFalse(connection3.isConnectedSegment(segment5e));
		}

		@DisplayName("#29")
		@Test
		public void testIsConnectedSegment_29() {
	    	assertFalse(connection4.isConnectedSegment(segment1));
		}

		@DisplayName("#30")
		@Test
		public void testIsConnectedSegment_30() {
	    	assertFalse(connection4.isConnectedSegment(segment2));
		}

		@DisplayName("#31")
		@Test
		public void testIsConnectedSegment_31() {
	    	assertFalse(connection4.isConnectedSegment(segment3a));
		}

		@DisplayName("#32")
		@Test
		public void testIsConnectedSegment_32() {
	    	assertFalse(connection4.isConnectedSegment(segment3b));
		}

		@DisplayName("#33")
		@Test
		public void testIsConnectedSegment_33() {
	    	assertFalse(connection4.isConnectedSegment(segment5a));
		}

		@DisplayName("#34")
		@Test
		public void testIsConnectedSegment_34() {
	    	assertFalse(connection4.isConnectedSegment(segment5b));
		}

		@DisplayName("#35")
		@Test
		public void testIsConnectedSegment_35() {
	    	assertFalse(connection4.isConnectedSegment(segment5c));
		}

		@DisplayName("#36")
		@Test
		public void testIsConnectedSegment_36() {
	    	assertFalse(connection4.isConnectedSegment(segment5d));
		}

		@DisplayName("#37")
		@Test
		public void testIsConnectedSegment_37() {
	    	assertFalse(connection4.isConnectedSegment(segment5e));
		}

		@DisplayName("#38")
		@Test
		public void testIsConnectedSegment_38() {
	    	assertFalse(connection5.isConnectedSegment(segment1));
		}

		@DisplayName("#39")
		@Test
		public void testIsConnectedSegment_39() {
	    	assertFalse(connection5.isConnectedSegment(segment2));
		}

		@DisplayName("#40")
		@Test
		public void testIsConnectedSegment_40() {
	    	assertFalse(connection5.isConnectedSegment(segment3a));
		}

		@DisplayName("#41")
		@Test
		public void testIsConnectedSegment_41() {
	    	assertFalse(connection5.isConnectedSegment(segment3b));
		}

		@DisplayName("#42")
		@Test
		public void testIsConnectedSegment_42() {
	    	assertTrue(connection5.isConnectedSegment(segment5a));
		}

		@DisplayName("#43")
		@Test
		public void testIsConnectedSegment_43() {
	    	assertTrue(connection5.isConnectedSegment(segment5b));
		}

		@DisplayName("#44")
		@Test
		public void testIsConnectedSegment_44() {
	    	assertTrue(connection5.isConnectedSegment(segment5c));
		}

		@DisplayName("#45")
		@Test
		public void testIsConnectedSegment_45() {
	    	assertTrue(connection5.isConnectedSegment(segment5d));
		}

		@DisplayName("#46")
		@Test
		public void testIsConnectedSegment_46() {
	    	assertTrue(connection5.isConnectedSegment(segment5e));
		}
	}

	@DisplayName("isFinalConnectionPoint")
	@Nested
	public class IsFinalConnectionPointTest {

		@DisplayName("#1")
		@Test
		public void testIsFinalConnectionPoint_1() {
	    	assertTrue(connection1.isFinalConnectionPoint());
		}

		@DisplayName("#2")
		@Test
		public void testIsFinalConnectionPoint_2() {
	    	assertTrue(connection2.isFinalConnectionPoint());
		}

		@DisplayName("#3")
		@Test
		public void testIsFinalConnectionPoint_3() {
	    	assertFalse(connection3.isFinalConnectionPoint());
		}

		@DisplayName("#4")
		@Test
		public void testIsFinalConnectionPoint_4() {
	    	assertTrue(connection4.isFinalConnectionPoint());
		}

		@DisplayName("#5")
		@Test
		public void testIsFinalConnectionPoint_5() {
	    	assertFalse(connection5.isFinalConnectionPoint());
		}
	}

	@DisplayName("getOtherSideSegment")
	@Nested
	public class GetOtherSideSegmentTest {

		@DisplayName("#1")
		@Test
	    public void testGetOtherSideSegment_1() {
	    	assertNull(connection1.getOtherSideSegment(segment1));
		}

		@DisplayName("#2")
		@Test
	    public void testGetOtherSideSegment_2() {
	    	assertNull(connection1.getOtherSideSegment(segment2));
		}

		@DisplayName("#3")
		@Test
	    public void testGetOtherSideSegment_3() {
	    	assertNull(connection1.getOtherSideSegment(segment3a));
		}

		@DisplayName("#4")
		@Test
	    public void testGetOtherSideSegment_4() {
	    	assertNull(connection1.getOtherSideSegment(segment3b));
		}

		@DisplayName("#5")
		@Test
	    public void testGetOtherSideSegment_5() {
	    	assertNull(connection1.getOtherSideSegment(segment5a));
		}

		@DisplayName("#6")
		@Test
	    public void testGetOtherSideSegment_6() {
	    	assertNull(connection1.getOtherSideSegment(segment5b));
		}

		@DisplayName("#7")
		@Test
	    public void testGetOtherSideSegment_7() {
	    	assertNull(connection1.getOtherSideSegment(segment5c));
		}

		@DisplayName("#8")
		@Test
	    public void testGetOtherSideSegment_8() {
	    	assertNull(connection1.getOtherSideSegment(segment5d));
		}

		@DisplayName("#9")
		@Test
	    public void testGetOtherSideSegment_9() {
	    	assertNull(connection1.getOtherSideSegment(segment5e));
		}

		@DisplayName("#10")
		@Test
	    public void testGetOtherSideSegment_10() {
	    	assertNull(connection2.getOtherSideSegment(segment2));
		}

		@DisplayName("#11")
		@Test
	    public void testGetOtherSideSegment_11() {
	    	assertNull(connection2.getOtherSideSegment(segment1));
		}

		@DisplayName("#12")
		@Test
	    public void testGetOtherSideSegment_12() {
	    	assertNull(connection2.getOtherSideSegment(segment3a));
		}

		@DisplayName("#13")
		@Test
	    public void testGetOtherSideSegment_13() {
	    	assertNull(connection2.getOtherSideSegment(segment3b));
		}

		@DisplayName("#14")
		@Test
	    public void testGetOtherSideSegment_14() {
	    	assertNull(connection2.getOtherSideSegment(segment5a));
		}

		@DisplayName("#15")
		@Test
	    public void testGetOtherSideSegment_15() {
	    	assertNull(connection2.getOtherSideSegment(segment5b));
		}

		@DisplayName("#16")
		@Test
	    public void testGetOtherSideSegment_16() {
	    	assertNull(connection2.getOtherSideSegment(segment5c));
		}

		@DisplayName("#17")
		@Test
	    public void testGetOtherSideSegment_17() {
	    	assertNull(connection2.getOtherSideSegment(segment5d));
		}

		@DisplayName("#18")
		@Test
	    public void testGetOtherSideSegment_18() {
	    	assertNull(connection2.getOtherSideSegment(segment5e));
		}

		@DisplayName("#19")
		@Test
	    public void testGetOtherSideSegment_19() {
	    	assertEquals(segment3b,connection3.getOtherSideSegment(segment3a));
		}

		@DisplayName("#20")
		@Test
	    public void testGetOtherSideSegment_20() {
	    	assertEquals(segment3a,connection3.getOtherSideSegment(segment3b));
		}

		@DisplayName("#21")
		@Test
	    public void testGetOtherSideSegment_21() {
	    	assertNull(connection3.getOtherSideSegment(segment2));
		}

		@DisplayName("#22")
		@Test
	    public void testGetOtherSideSegment_22() {
	    	assertNull(connection3.getOtherSideSegment(segment1));
		}

		@DisplayName("#23")
		@Test
	    public void testGetOtherSideSegment_23() {
	    	assertNull(connection3.getOtherSideSegment(segment5a));
		}

		@DisplayName("#24")
		@Test
	    public void testGetOtherSideSegment_24() {
	    	assertNull(connection3.getOtherSideSegment(segment5b));
		}

		@DisplayName("#25")
		@Test
	    public void testGetOtherSideSegment_25() {
	    	assertNull(connection3.getOtherSideSegment(segment5c));
		}

		@DisplayName("#26")
		@Test
	    public void testGetOtherSideSegment_26() {
	    	assertNull(connection3.getOtherSideSegment(segment5d));
		}

		@DisplayName("#27")
		@Test
	    public void testGetOtherSideSegment_27() {
	    	assertNull(connection3.getOtherSideSegment(segment5e));
		}

		@DisplayName("#28")
		@Test
	    public void testGetOtherSideSegment_28() {
	    	assertNull(connection4.getOtherSideSegment(segment1));
		}

		@DisplayName("#29")
		@Test
	    public void testGetOtherSideSegment_29() {
	    	assertNull(connection4.getOtherSideSegment(segment2));
		}

		@DisplayName("#30")
		@Test
	    public void testGetOtherSideSegment_30() {
	    	assertNull(connection4.getOtherSideSegment(segment3a));
		}

		@DisplayName("#31")
		@Test
	    public void testGetOtherSideSegment_31() {
	    	assertNull(connection4.getOtherSideSegment(segment3b));
		}

		@DisplayName("#32")
		@Test
	    public void testGetOtherSideSegment_32() {
	    	assertNull(connection4.getOtherSideSegment(segment5a));
		}

		@DisplayName("#33")
		@Test
	    public void testGetOtherSideSegment_33() {
	    	assertNull(connection4.getOtherSideSegment(segment5b));
		}

		@DisplayName("#34")
		@Test
	    public void testGetOtherSideSegment_34() {
	    	assertNull(connection4.getOtherSideSegment(segment5c));
		}

		@DisplayName("#35")
		@Test
	    public void testGetOtherSideSegment_35() {
	    	assertNull(connection4.getOtherSideSegment(segment5d));
		}

		@DisplayName("#36")
		@Test
	    public void testGetOtherSideSegment_36() {
	    	assertNull(connection4.getOtherSideSegment(segment5e));
		}

		@DisplayName("#37")
		@Test
	    public void testGetOtherSideSegment_37() {
	    	assertNull(connection5.getOtherSideSegment(segment1));
		}

		@DisplayName("#38")
		@Test
	    public void testGetOtherSideSegment_38() {
	    	assertNull(connection5.getOtherSideSegment(segment2));
		}

		@DisplayName("#39")
		@Test
	    public void testGetOtherSideSegment_39() {
	    	assertNull(connection5.getOtherSideSegment(segment3a));
		}

		@DisplayName("#40")
		@Test
	    public void testGetOtherSideSegment_40() {
	    	assertNull(connection5.getOtherSideSegment(segment3b));
		}

		@DisplayName("#41")
		@Test
	    public void testGetOtherSideSegment_41() {
	    	assertNull(connection5.getOtherSideSegment(segment5a));
		}

		@DisplayName("#42")
		@Test
	    public void testGetOtherSideSegment_42() {
	    	assertNull(connection5.getOtherSideSegment(segment5b));
		}

		@DisplayName("#43")
		@Test
	    public void testGetOtherSideSegment_43() {
	    	assertNull(connection5.getOtherSideSegment(segment5c));
		}

		@DisplayName("#44")
		@Test
	    public void testGetOtherSideSegment_44() {
	    	assertNull(connection5.getOtherSideSegment(segment5d));
		}

		@DisplayName("#45")
		@Test
	    public void testGetOtherSideSegment_45() {
	    	assertNull(connection5.getOtherSideSegment(segment5e));
	    }
	}

	@DisplayName("getPoint")
	@Nested
	public class GetPointTest {

		@DisplayName("#1")
		@Test
	    public void testGetPoint_1() {
			RoadConnection con = segment1.getBeginPoint(StandardRoadConnection.class);
			assertEquals(connection1,con);
			Point2d p = con.getPoint();
			assertEquals(segment1.getPointAt(0), p);
		}

		@DisplayName("#2")
		@Test
	    public void testGetPoint_2() {
			var con = segment1.getEndPoint();
			assertNull(con);
		}

		@DisplayName("#3")
		@Test
	    public void testGetPoint_3() {
			var con = segment2.getEndPoint();
			assertEquals(connection2,con);
			var p = con.getPoint();
			assertEquals(segment2.getPointAt(segment2.getPointCount()-1), p);
		}

		@DisplayName("#4")
		@Test
	    public void testGetPoint_4() {
			var con = segment2.getBeginPoint(StandardRoadConnection.class);
			assertNull(con);
		}

		@DisplayName("#5")
		@Test
	    public void testGetPoint_5() {
			var con = segment3a.getBeginPoint(StandardRoadConnection.class);
			assertEquals(connection3,con);
			var p = con.getPoint();
			assertEquals(segment3a.getPointAt(0), p);
		}

		@DisplayName("#6")
		@Test
	    public void testGetPoint_6() {
			var con = segment1.getEndPoint();
			assertNull(con);
		}

		@DisplayName("#7")
		@Test
	    public void testGetPoint_7() {
			var con = segment3b.getEndPoint();
			assertEquals(connection3,con);
			var p = con.getPoint();
			assertEquals(segment3b.getPointAt(segment3b.getPointCount()-1), p);
		}

		@DisplayName("#8")
		@Test
	    public void testGetPoint_8() {
			var con = segment3b.getBeginPoint(StandardRoadConnection.class);
			assertNull(con);
		}
	}

    protected void runTestToCounterclockwiseIterator(ClockwiseBoundType boundType) {

    	//------------------------------------------ 1

    	Iterator<RoadSegment> itSegments = connection1.toCounterclockwiseIterator(segment1, segment1, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toCounterclockwiseIterator(segment2, segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toCounterclockwiseIterator(segment1, segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toCounterclockwiseIterator(null, segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toCounterclockwiseIterator(segment1, null, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
           	assertEquals(segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 3

    	itSegments = connection3.toCounterclockwiseIterator(segment3a, segment3a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3b, segment3b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3a, segment3b, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3b, segment3a, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment2, segment3a, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment2, segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3a, segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3a, null, boundType);
    	if (boundType.includeStart()) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3b, segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3b, null, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 4

    	itSegments = connection4.toCounterclockwiseIterator(segment3b, segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 5

    	itSegments = connection5.toCounterclockwiseIterator(segment5a, segment5a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toCounterclockwiseIterator(segment5b, segment5b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toCounterclockwiseIterator(segment5c, segment5c, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toCounterclockwiseIterator(segment5d, segment5d, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toCounterclockwiseIterator(segment5e, segment5e, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5e,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toCounterclockwiseIterator(segment5a, segment5d, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5a,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5b,itSegments.next());
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5c,itSegments.next());
    	}
    	else {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5e,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toCounterclockwiseIterator(segment5d, segment5c, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5d,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment5e,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment5a,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment5b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());
    }

    protected void runTestToClockwiseIterator(ClockwiseBoundType boundType) {

    	//------------------------------------------ 1

    	Iterator<RoadSegment> itSegments = connection1.toClockwiseIterator(segment1, segment1, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toClockwiseIterator(segment2, segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toClockwiseIterator(segment1, segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toClockwiseIterator(null, segment1, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection1.toClockwiseIterator(segment1, null, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
           	assertEquals(segment1,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 3

    	itSegments = connection3.toClockwiseIterator(segment3a, segment3a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment3b, segment3b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment3a, segment3b, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment3b, segment3a, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment2, segment3a, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment2, segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toCounterclockwiseIterator(segment3a, segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment3a, null, boundType);
    	if (boundType.includeStart()) {
    		assertTrue(itSegments.hasNext());
        	assertEquals(segment3a,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment3b, segment2, boundType);
    	assertFalse(itSegments.hasNext());

    	itSegments = connection3.toClockwiseIterator(segment3b, null, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment3a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 4

    	itSegments = connection4.toClockwiseIterator(segment3b, segment3b, boundType);
    	assertFalse(itSegments.hasNext());

    	//------------------------------------------ 5

    	itSegments = connection5.toClockwiseIterator(segment5a, segment5a, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5a,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toClockwiseIterator(segment5b, segment5b, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5b,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toClockwiseIterator(segment5c, segment5c, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toClockwiseIterator(segment5d, segment5d, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toClockwiseIterator(segment5e, segment5e, boundType);
    	if (boundType!=ClockwiseBoundType.EXCLUDE_BOTH) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5e,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toClockwiseIterator(segment5a, segment5d, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5a,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5e,itSegments.next());
    	}
    	else {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5b,itSegments.next());
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5c,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5d,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());

    	//---
    	itSegments = connection5.toClockwiseIterator(segment5d, segment5c, boundType);
    	if (boundType.includeStart()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5d,itSegments.next());
    	}
    	if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment5e,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment5a,itSegments.next());
    		assertTrue(itSegments.hasNext());
    		assertEquals(segment5b,itSegments.next());
    	}
    	if (boundType.includeEnd()) {
	    	assertTrue(itSegments.hasNext());
	    	assertEquals(segment5c,itSegments.next());
    	}
    	assertFalse(itSegments.hasNext());
    }

	@DisplayName("toCounterclockwiseIterator")
	@Nested
	public class ToCounterclockwiseIteratorTest {
		@DisplayName("(INCLUDE_BOTH)")
		@Test
	    public void testToCounterclockwiseIteratorIncludeBoth() {
	    	runTestToCounterclockwiseIterator(ClockwiseBoundType.INCLUDE_BOTH);
	    }
	
		@DisplayName("(EXCLUDE_START_SEGMENT)")
		@Test
	    public void testToCounterclockwiseIteratorExcludeStart() {
	    	runTestToCounterclockwiseIterator(ClockwiseBoundType.EXCLUDE_START_SEGMENT);
	    }
	
		@DisplayName("(EXCLUDE_END_SEGMENT)")
		@Test
	    public void testToCounterclockwiseIteratorExcludeEnd() {
	    	runTestToCounterclockwiseIterator(ClockwiseBoundType.EXCLUDE_END_SEGMENT);
	    }
	
		@DisplayName("(EXCLUDE_BOTH)")
		@Test
	    public void testToCounterclockwiseIteratorExcludeBoth() {
	    	runTestToCounterclockwiseIterator(ClockwiseBoundType.EXCLUDE_BOTH);
	    }
	}

	@DisplayName("toClockwiseIterator")
	@Nested
	public class ToClockwiseIteratorTest {
		@DisplayName("(INCLUDE_BOTH)")
		@Test
	    public void testToClockwiseIteratorIncludeBoth() {
	    	runTestToClockwiseIterator(ClockwiseBoundType.INCLUDE_BOTH);
	    }
	
		@DisplayName("(EXCLUDE_START_SEGMENT)")
		@Test
	    public void testToClockwiseIteratorExcludeStart() {
	    	runTestToClockwiseIterator(ClockwiseBoundType.EXCLUDE_START_SEGMENT);
	    }
	
		@DisplayName("(EXCLUDE_END_SEGMENT)")
		@Test
	    public void testToClockwiseIteratorExcludeEnd() {
	    	runTestToClockwiseIterator(ClockwiseBoundType.EXCLUDE_END_SEGMENT);
	    }
	
		@DisplayName("(EXCLUDE_BOTH)")
		@Test
	    public void testToClockwiseIteratorExcludeBoth() {
	    	runTestToClockwiseIterator(ClockwiseBoundType.EXCLUDE_BOTH);
	    }
	}

	@DisplayName("toCounterclockwiseIterator.remove")
	@Test
    public void testToCounterclockwiseIteratorRemove() {
    	// Check the initial content of the connection
    	List<RoadSegment> segments = new ArrayList<>();
    	for(RoadSegment s : connection5.getConnectedSegments()) {
    		segments.add(s);
    	}
    	assertEquals(5,segments.size());
    	assertEquals(segment5a, segments.get(0));
    	assertEquals(segment5b, segments.get(1));
    	assertEquals(segment5c, segments.get(2));
    	assertEquals(segment5d, segments.get(3));
    	assertEquals(segment5e, segments.get(4));

    	// Use the iterator and its removal operation
    	Iterator<RoadSegment> itSegments =
    		CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded()
    		? connection5.toCounterclockwiseIterator(segment5d, segment5c)
    		: connection5.toClockwiseIterator(segment5d, segment5c);
    	assertTrue(itSegments.hasNext());
    	assertEquals(segment5d,itSegments.next());
    	assertTrue(itSegments.hasNext());
    	assertEquals(segment5e,itSegments.next());
    	itSegments.remove();
    	assertTrue(itSegments.hasNext());
    	assertEquals(segment5a,itSegments.next());
    	assertTrue(itSegments.hasNext());
    	assertEquals(segment5b,itSegments.next());
    	itSegments.remove();
    	assertTrue(itSegments.hasNext());
    	assertEquals(segment5c,itSegments.next());
    	assertFalse(itSegments.hasNext());

    	// Check the list of the connected segments
    	segments = new ArrayList<>();
    	for(RoadSegment s : connection5.getConnectedSegments()) {
    		segments.add(s);
    	}
    	assertEquals(3,segments.size());
    	assertEquals(segment5a, segments.get(0));
    	assertEquals(segment5c, segments.get(1));
    	assertEquals(segment5d, segments.get(2));
    }

}

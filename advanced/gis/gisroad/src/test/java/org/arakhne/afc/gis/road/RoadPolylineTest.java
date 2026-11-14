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

import java.util.Random;
import java.util.stream.DoubleStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;

/** Unit test for RoadSegment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("RoadPolyline")
@SuppressWarnings("all")
public class RoadPolylineTest extends AbstractGisTest {

	private static final int TEST_COUNT = 50;
	private static final double SHIFTING_RANGE = 50.;

	private RoadSegmentStub testedSegment;
	private RoadSegmentStub firstNeighbour;
	private RoadSegmentStub secondNeighbour;
	private RoadSegmentStub loopSegment;
	private RoadSegmentStub thirdNeighbour;
	private RoadConnectionStub connection1;
	private RoadConnectionStub connection2;
	private RoadConnectionStub culDeSac1;
	private RoadConnectionStub culDeSac2;
	private RoadConnectionStub culDeSac3;
	private RoadConnectionStub connection3;

	@BeforeEach
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();

		testedSegment = new RoadSegmentStub("testedSegment", true, false); //$NON-NLS-1$
		connection1 = new RoadConnectionStub("connection1"); //$NON-NLS-1$
		testedSegment.setStartPoint(connection1);
		connection2 = new RoadConnectionStub("connection2"); //$NON-NLS-1$
		testedSegment.setEndPoint(connection2);

		firstNeighbour = new RoadSegmentStub("firstNeightbour", true, false); //$NON-NLS-1$
		firstNeighbour.setEndPoint(connection1);
		culDeSac1 = new RoadConnectionStub("culDeSac1"); //$NON-NLS-1$
		firstNeighbour.setStartPoint(culDeSac1);

		secondNeighbour = new RoadSegmentStub("secondNeightbour", true, false); //$NON-NLS-1$
		secondNeighbour.setEndPoint(connection2);
		culDeSac2 = new RoadConnectionStub("culDeSac2"); //$NON-NLS-1$
		secondNeighbour.setStartPoint(culDeSac2);

		loopSegment = new RoadSegmentStub("loopSegment", true, true); //$NON-NLS-1$
		connection3 = new RoadConnectionStub("connection3"); //$NON-NLS-1$
		loopSegment.setStartPoint(connection3);
		loopSegment.setEndPoint(connection3);

		thirdNeighbour = new RoadSegmentStub("thirdNeightbour", true, false); //$NON-NLS-1$
		thirdNeighbour.setEndPoint(connection3);
		culDeSac3 = new RoadConnectionStub("culDeSac3"); //$NON-NLS-1$
		thirdNeighbour.setStartPoint(culDeSac3);
	}

	@AfterEach
	public void tearDown() {
		firstNeighbour = null;
		secondNeighbour = null;
		thirdNeighbour = null;
		culDeSac1 = null;
		culDeSac2 = null;
		culDeSac3 = null;
		connection3 = null;
		connection2 = null;
		connection1 = null;
		loopSegment = null;
		testedSegment = null;
	}

	@DisplayName("getDistanceFromStart")
	@Nested
	public class GetDistanceFromStartTest {

		@DisplayName("#1")
	    @Test
		public void testGetDistanceFromStart_1() {
	    	assertEpsilonEquals(0.,testedSegment.getDistanceFromStart(0));
		}

		@DisplayName("#2")
	    @Test
		public void testGetDistanceFromStart_2() {
	    	assertEpsilonEquals(0.,firstNeighbour.getDistanceFromStart(0));
		}

		@DisplayName("#3")
	    @Test
		public void testGetDistanceFromStart_3() {
	    	assertEpsilonEquals(0.,secondNeighbour.getDistanceFromStart(0));
		}

		@DisplayName("#4")
	    @Test
		public void testGetDistanceFromStart_4() {
	    	assertEpsilonEquals(0.,loopSegment.getDistanceFromStart(0));
		}

		@DisplayName("#5")
	    @Test
		public void testGetDistanceFromStart_5() {
	    	assertEpsilonEquals(0.,thirdNeighbour.getDistanceFromStart(0));
		}

		@DisplayName("#6")
	    @Test
		public void testGetDistanceFromStart_6() {
	    	assertEpsilonEquals(testedSegment.getOriginalLength(),testedSegment.getDistanceFromStart(1));
		}

		@DisplayName("#7")
	    @Test
		public void testGetDistanceFromStart_7() {
	    	assertEpsilonEquals(firstNeighbour.getOriginalLength(),firstNeighbour.getDistanceFromStart(1));
		}

		@DisplayName("#8")
	    @Test
		public void testGetDistanceFromStart_8() {
	    	assertEpsilonEquals(secondNeighbour.getOriginalLength(),secondNeighbour.getDistanceFromStart(1));
		}

		@DisplayName("#9")
	    @Test
		public void testGetDistanceFromStart_9() {
	    	assertEpsilonEquals(loopSegment.getOriginalLength(),loopSegment.getDistanceFromStart(1));
		}

		@DisplayName("#10")
	    @Test
		public void testGetDistanceFromStart_10() {
	    	assertEpsilonEquals(thirdNeighbour.getOriginalLength(),thirdNeighbour.getDistanceFromStart(1));
		}

		@DisplayName("#11")
		@Nested
		public class RandomRatioTest {

			public static DoubleStream providesDoubles() {
				var array = new double[TEST_COUNT];
		    	var rnd = new Random();
		    	for(int i = 0; i < TEST_COUNT; ++i) {
		    		array[i] = rnd.nextDouble();
		    	}
				return DoubleStream.of(array);
			}
	
			@DisplayName("(a)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_a(double ratio) {
	    		assertEpsilonEquals(testedSegment.getOriginalStartDistance(ratio),testedSegment.getDistanceFromStart(ratio));
			}
			
			@DisplayName("(b)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_b(double ratio) {
	    		assertEpsilonEquals(firstNeighbour.getOriginalStartDistance(ratio),firstNeighbour.getDistanceFromStart(ratio));
			}
			
			@DisplayName("(c)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_c(double ratio) {
	    		assertEpsilonEquals(secondNeighbour.getOriginalStartDistance(ratio),secondNeighbour.getDistanceFromStart(ratio));
			}
			
			@DisplayName("(d)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_d(double ratio) {
	    		assertEpsilonEquals(loopSegment.getOriginalStartDistance(ratio),loopSegment.getDistanceFromStart(ratio));
			}
			
			@DisplayName("(e)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_e(double ratio) {
	    		assertEpsilonEquals(thirdNeighbour.getOriginalStartDistance(ratio),thirdNeighbour.getDistanceFromStart(ratio));
			}
		}
	}

	@DisplayName("getDistanceToEnd")
	@Nested
	public class GetDistanceToEndTest {

		@DisplayName("#1")
	    @Test
		public void testGetDistanceToEnd_1() {
	    	assertEpsilonEquals(testedSegment.getOriginalLength(),testedSegment.getDistanceToEnd(0));
		}

		@DisplayName("#2")
	    @Test
		public void testGetDistanceToEnd_2() {
	    	assertEpsilonEquals(firstNeighbour.getOriginalLength(),firstNeighbour.getDistanceToEnd(0));
		}

		@DisplayName("#3")
	    @Test
		public void testGetDistanceToEnd_3() {
	    	assertEpsilonEquals(secondNeighbour.getOriginalLength(),secondNeighbour.getDistanceToEnd(0));
		}

		@DisplayName("#4")
	    @Test
		public void testGetDistanceToEnd_4() {
	    	assertEpsilonEquals(loopSegment.getOriginalLength(),loopSegment.getDistanceToEnd(0));
		}

		@DisplayName("#5")
	    @Test
		public void testGetDistanceToEnd_5() {
	    	assertEpsilonEquals(thirdNeighbour.getOriginalLength(),thirdNeighbour.getDistanceToEnd(0));
		}

		@DisplayName("#6")
	    @Test
		public void testGetDistanceToEnd_6() {
	    	assertEpsilonEquals(0.,testedSegment.getDistanceToEnd(1));
		}

		@DisplayName("#7")
	    @Test
		public void testGetDistanceToEnd_7() {
	    	assertEpsilonEquals(0.,firstNeighbour.getDistanceToEnd(1));
		}

		@DisplayName("#8")
	    @Test
		public void testGetDistanceToEnd_8() {
	    	assertEpsilonEquals(0.,secondNeighbour.getDistanceToEnd(1));
		}

		@DisplayName("#9")
	    @Test
		public void testGetDistanceToEnd_9() {
	    	assertEpsilonEquals(0.,loopSegment.getDistanceToEnd(1));
		}

		@DisplayName("#10")
	    @Test
		public void testGetDistanceToEnd_10() {
	    	assertEpsilonEquals(0.,thirdNeighbour.getDistanceToEnd(1));
		}

		@DisplayName("#11")
		@Nested
		public class RandomRatioTest {

			public static DoubleStream providesDoubles() {
				var array = new double[TEST_COUNT];
		    	var rnd = new Random();
		    	for(int i = 0; i < TEST_COUNT; ++i) {
		    		array[i] = rnd.nextDouble();
		    	}
				return DoubleStream.of(array);
			}
	
			@DisplayName("(a)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_a(double ratio) {
	    		assertEpsilonEquals(testedSegment.getOriginalEndDistance(ratio),testedSegment.getDistanceToEnd(ratio));
			}
			
			@DisplayName("(b)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_b(double ratio) {
	    		assertEpsilonEquals(firstNeighbour.getOriginalEndDistance(ratio),firstNeighbour.getDistanceToEnd(ratio));
			}
			
			@DisplayName("(c)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_c(double ratio) {
	    		assertEpsilonEquals(secondNeighbour.getOriginalEndDistance(ratio),secondNeighbour.getDistanceToEnd(ratio));
			}
			
			@DisplayName("(d)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_d(double ratio) {
	    		assertEpsilonEquals(loopSegment.getOriginalEndDistance(ratio),loopSegment.getDistanceToEnd(ratio));
			}
			
			@DisplayName("(e)")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("providesDoubles")
			public void test_e(double ratio) {
	    		assertEpsilonEquals(thirdNeighbour.getOriginalEndDistance(ratio),thirdNeighbour.getDistanceToEnd(ratio));
			}
		}
	}

	@DisplayName("getAntepenulvianPoint")
	@Nested
	public class GetAntepenulvianPointTest {

		@DisplayName("#1")
	    @Test
		public void testGetAntepenulvianPoint_1() {
	    	assertEquals(testedSegment.getOriginalAntepenulvian(),testedSegment.getAntepenulvianPoint());
		}

		@DisplayName("#2")
	    @Test
		public void testGetAntepenulvianPoint_2() {
	    	assertEquals(firstNeighbour.getOriginalAntepenulvian(),firstNeighbour.getAntepenulvianPoint());
		}

		@DisplayName("#3")
	    @Test
		public void testGetAntepenulvianPoint_3() {
	    	assertEquals(secondNeighbour.getOriginalAntepenulvian(),secondNeighbour.getAntepenulvianPoint());
		}

		@DisplayName("#4")
	    @Test
		public void testGetAntepenulvianPoint_4() {
	    	assertEquals(loopSegment.getOriginalAntepenulvian(),loopSegment.getAntepenulvianPoint());
		}

		@DisplayName("#5")
	    @Test
		public void testGetAntepenulvianPoint_5() {
	    	assertEquals(thirdNeighbour.getOriginalAntepenulvian(),thirdNeighbour.getAntepenulvianPoint());
	    }
	}

	@DisplayName("getOtherSidePoint")
	@Nested
	public class GetOtherSidePointTest {

		@DisplayName("#1")
	    @Test
	    public void testGetOtherSidePoint_1() {
	    	assertEquals(connection2,testedSegment.getOtherSidePoint(connection1));
		}

		@DisplayName("#2")
	    @Test
	    public void testGetOtherSidePoint_2() {
	    	assertEquals(connection1,testedSegment.getOtherSidePoint(connection2));
		}

		@DisplayName("#3")
	    @Test
	    public void testGetOtherSidePoint_3() {
	    	assertNull(testedSegment.getOtherSidePoint(connection3));
		}

		@DisplayName("#4")
	    @Test
	    public void testGetOtherSidePoint_4() {
	    	assertNull(testedSegment.getOtherSidePoint(culDeSac1));
		}

		@DisplayName("#5")
	    @Test
	    public void testGetOtherSidePoint_5() {
	    	assertNull(testedSegment.getOtherSidePoint(culDeSac2));
		}

		@DisplayName("#6")
	    @Test
	    public void testGetOtherSidePoint_6() {
	    	assertNull(testedSegment.getOtherSidePoint(culDeSac3));
		}

		@DisplayName("#7")
	    @Test
	    public void testGetOtherSidePoint_7() {
	    	assertEquals(culDeSac1,firstNeighbour.getOtherSidePoint(connection1));
		}

		@DisplayName("#8")
	    @Test
	    public void testGetOtherSidePoint_8() {
	    	assertNull(firstNeighbour.getOtherSidePoint(connection2));
		}

		@DisplayName("#9")
	    @Test
	    public void testGetOtherSidePoint_9() {
	    	assertNull(firstNeighbour.getOtherSidePoint(connection3));
		}

		@DisplayName("#10")
	    @Test
	    public void testGetOtherSidePoint_10() {
	    	assertEquals(connection1,firstNeighbour.getOtherSidePoint(culDeSac1));
		}

		@DisplayName("#11")
	    @Test
	    public void testGetOtherSidePoint_11() {
	    	assertNull(firstNeighbour.getOtherSidePoint(culDeSac2));
		}

		@DisplayName("#12")
	    @Test
	    public void testGetOtherSidePoint_12() {
	    	assertNull(firstNeighbour.getOtherSidePoint(culDeSac3));
		}

		@DisplayName("#13")
	    @Test
	    public void testGetOtherSidePoint_13() {
	    	assertNull(secondNeighbour.getOtherSidePoint(connection1));
		}

		@DisplayName("#14")
	    @Test
	    public void testGetOtherSidePoint_14() {
	    	assertEquals(culDeSac2,secondNeighbour.getOtherSidePoint(connection2));
		}

		@DisplayName("#15")
	    @Test
	    public void testGetOtherSidePoint_15() {
	    	assertNull(secondNeighbour.getOtherSidePoint(connection3));
		}

		@DisplayName("#16")
	    @Test
	    public void testGetOtherSidePoint_16() {
	    	assertNull(secondNeighbour.getOtherSidePoint(culDeSac1));
		}

		@DisplayName("#17")
	    @Test
	    public void testGetOtherSidePoint_17() {
	    	assertEquals(connection2,secondNeighbour.getOtherSidePoint(culDeSac2));
		}

		@DisplayName("#18")
	    @Test
	    public void testGetOtherSidePoint_18() {
	    	assertNull(secondNeighbour.getOtherSidePoint(culDeSac3));
		}

		@DisplayName("#19")
	    @Test
	    public void testGetOtherSidePoint_19() {
	    	assertNull(loopSegment.getOtherSidePoint(connection1));
		}

		@DisplayName("#20")
	    @Test
	    public void testGetOtherSidePoint_20() {
	    	assertNull(loopSegment.getOtherSidePoint(connection2));
		}

		@DisplayName("#21")
	    @Test
	    public void testGetOtherSidePoint_21() {
	    	assertEquals(connection3,loopSegment.getOtherSidePoint(connection3));
		}

		@DisplayName("#22")
	    @Test
	    public void testGetOtherSidePoint_22() {
	    	assertNull(loopSegment.getOtherSidePoint(culDeSac1));
		}

		@DisplayName("#23")
	    @Test
	    public void testGetOtherSidePoint_23() {
	    	assertNull(loopSegment.getOtherSidePoint(culDeSac2));
		}

		@DisplayName("#24")
	    @Test
	    public void testGetOtherSidePoint_24() {
	    	assertNull(loopSegment.getOtherSidePoint(culDeSac3));
		}

		@DisplayName("#25")
	    @Test
	    public void testGetOtherSidePoint_25() {
	    	assertNull(thirdNeighbour.getOtherSidePoint(connection1));
		}

		@DisplayName("#26")
	    @Test
	    public void testGetOtherSidePoint_26() {
	    	assertNull(thirdNeighbour.getOtherSidePoint(connection2));
		}

		@DisplayName("#27")
	    @Test
	    public void testGetOtherSidePoint_27() {
	    	assertEquals(culDeSac3,thirdNeighbour.getOtherSidePoint(connection3));
		}

		@DisplayName("#28")
	    @Test
	    public void testGetOtherSidePoint_28() {
	    	assertNull(thirdNeighbour.getOtherSidePoint(culDeSac1));
		}

		@DisplayName("#29")
	    @Test
	    public void testGetOtherSidePoint_29() {
	    	assertNull(thirdNeighbour.getOtherSidePoint(culDeSac2));
		}

		@DisplayName("#30")
	    @Test
	    public void testGetOtherSidePoint_30() {
	    	assertEquals(connection3,thirdNeighbour.getOtherSidePoint(culDeSac3));
	    }
	}

	@DisplayName("isConnectedTo")
	@Nested
	public class IsConnectedToTest {

		@DisplayName("#1")
	    @Test
	    public void testIsConnectedTo_1() {
	    	assertFalse(testedSegment.isConnectedTo(testedSegment));
		}

		@DisplayName("#2")
	    @Test
	    public void testIsConnectedTo_2() {
	    	assertFalse(testedSegment.isConnectedTo(loopSegment));
		}

		@DisplayName("#3")
	    @Test
	    public void testIsConnectedTo_3() {
	    	assertTrue(testedSegment.isConnectedTo(firstNeighbour));
		}

		@DisplayName("#4")
	    @Test
	    public void testIsConnectedTo_4() {
	    	assertTrue(testedSegment.isConnectedTo(secondNeighbour));
		}

		@DisplayName("#5")
	    @Test
	    public void testIsConnectedTo_5() {
	    	assertFalse(testedSegment.isConnectedTo(thirdNeighbour));
		}

		@DisplayName("#6")
	    @Test
	    public void testIsConnectedTo_6() {
	    	assertFalse(loopSegment.isConnectedTo(testedSegment));
		}

		@DisplayName("#7")
	    @Test
	    public void testIsConnectedTo_7() {
	    	assertTrue(loopSegment.isConnectedTo(loopSegment));
		}

		@DisplayName("#8")
	    @Test
	    public void testIsConnectedTo_8() {
	    	assertFalse(loopSegment.isConnectedTo(firstNeighbour));
		}

		@DisplayName("#9")
	    @Test
	    public void testIsConnectedTo_9() {
	    	assertFalse(loopSegment.isConnectedTo(secondNeighbour));
		}

		@DisplayName("#10")
	    @Test
	    public void testIsConnectedTo_10() {
	    	assertTrue(loopSegment.isConnectedTo(thirdNeighbour));
		}

		@DisplayName("#11")
	    @Test
	    public void testIsConnectedTo_11() {
	    	assertTrue(firstNeighbour.isConnectedTo(testedSegment));
		}

		@DisplayName("#12")
	    @Test
	    public void testIsConnectedTo_12() {
	    	assertFalse(firstNeighbour.isConnectedTo(loopSegment));
		}

		@DisplayName("#13")
	    @Test
	    public void testIsConnectedTo_13() {
	    	assertFalse(firstNeighbour.isConnectedTo(firstNeighbour));
		}

		@DisplayName("#14")
	    @Test
	    public void testIsConnectedTo_14() {
	    	assertFalse(firstNeighbour.isConnectedTo(secondNeighbour));
		}

		@DisplayName("#15")
	    @Test
	    public void testIsConnectedTo_15() {
	    	assertFalse(firstNeighbour.isConnectedTo(thirdNeighbour));
		}

		@DisplayName("#16")
	    @Test
	    public void testIsConnectedTo_16() {
	    	assertTrue(secondNeighbour.isConnectedTo(testedSegment));
		}

		@DisplayName("#17")
	    @Test
	    public void testIsConnectedTo_17() {
	    	assertFalse(secondNeighbour.isConnectedTo(loopSegment));
		}

		@DisplayName("#18")
	    @Test
	    public void testIsConnectedTo_18() {
	    	assertFalse(secondNeighbour.isConnectedTo(firstNeighbour));
		}

		@DisplayName("#19")
	    @Test
	    public void testIsConnectedTo_19() {
	    	assertFalse(secondNeighbour.isConnectedTo(secondNeighbour));
		}

		@DisplayName("#20")
	    @Test
	    public void testIsConnectedTo_20() {
	    	assertFalse(secondNeighbour.isConnectedTo(thirdNeighbour));
		}

		@DisplayName("#21")
	    @Test
	    public void testIsConnectedTo_21() {
	    	assertFalse(thirdNeighbour.isConnectedTo(testedSegment));
		}

		@DisplayName("#22")
	    @Test
	    public void testIsConnectedTo_22() {
	    	assertTrue(thirdNeighbour.isConnectedTo(loopSegment));
		}

		@DisplayName("#23")
	    @Test
	    public void testIsConnectedTo_23() {
	    	assertFalse(thirdNeighbour.isConnectedTo(firstNeighbour));
		}

		@DisplayName("#24")
	    @Test
	    public void testIsConnectedTo_24() {
	    	assertFalse(thirdNeighbour.isConnectedTo(secondNeighbour));
		}

		@DisplayName("#25")
	    @Test
	    public void testIsConnectedTo_25() {
	    	assertFalse(thirdNeighbour.isConnectedTo(thirdNeighbour));
	    }
	}

	@DisplayName("getGeoLocationForDistance")
	@Nested
	public class GetGeoLocationForDistanceTest {

		private void runTestGetGeoLocationForDistance(Vector2d tangent) {
			assertEpsilonEquals(testedSegment.getOriginalPointAt(0),
					testedSegment.getGeoLocationForDistance(0,0,tangent));
			if (tangent!=null)
				assertColinear(testedSegment.getOriginalVectorAt(0),tangent);
	
			assertEpsilonEquals(loopSegment.getOriginalPointAt(0),
					loopSegment.getGeoLocationForDistance(0,0,tangent));
			if (tangent!=null)
				assertColinear(loopSegment.getOriginalVectorAt(0),tangent);
	
			assertEpsilonEquals(firstNeighbour.getOriginalPointAt(0),
					firstNeighbour.getGeoLocationForDistance(0,0,tangent));
			if (tangent!=null)
				assertColinear(firstNeighbour.getOriginalVectorAt(0),tangent);
	
			assertEpsilonEquals(secondNeighbour.getOriginalPointAt(0),
					secondNeighbour.getGeoLocationForDistance(0,0,tangent));
			if (tangent!=null)
				assertColinear(secondNeighbour.getOriginalVectorAt(0),tangent);
	
			assertEpsilonEquals(thirdNeighbour.getOriginalPointAt(0),
					thirdNeighbour.getGeoLocationForDistance(0,0,tangent));
			if (tangent!=null)
				assertColinear(thirdNeighbour.getOriginalVectorAt(0),tangent);
	
			//
	
			assertEpsilonEquals(testedSegment.getOriginalPointAt(testedSegment.getOriginalPointCount()-1),
					testedSegment.getGeoLocationForDistance(testedSegment.getOriginalLength(),0,tangent));
			if (tangent!=null)
				assertColinear(testedSegment.getOriginalVectorAt(testedSegment.getOriginalPointCount()-2),tangent);
	
			assertEpsilonEquals(loopSegment.getOriginalPointAt(loopSegment.getOriginalPointCount()-1),
					loopSegment.getGeoLocationForDistance(loopSegment.getOriginalLength(),0,tangent));
			if (tangent!=null)
				assertColinear(loopSegment.getOriginalVectorAt(loopSegment.getOriginalPointCount()-2),tangent);
	
			assertEpsilonEquals(firstNeighbour.getOriginalPointAt(firstNeighbour.getOriginalPointCount()-1),
					firstNeighbour.getGeoLocationForDistance(firstNeighbour.getOriginalLength(),0,tangent));
			if (tangent!=null)
				assertColinear(firstNeighbour.getOriginalVectorAt(firstNeighbour.getOriginalPointCount()-2),tangent);
	
			assertEpsilonEquals(secondNeighbour.getOriginalPointAt(secondNeighbour.getOriginalPointCount()-1),
					secondNeighbour.getGeoLocationForDistance(secondNeighbour.getOriginalLength(),0,tangent));
			if (tangent!=null)
				assertColinear(secondNeighbour.getOriginalVectorAt(secondNeighbour.getOriginalPointCount()-2),tangent);
	
			assertEpsilonEquals(thirdNeighbour.getOriginalPointAt(thirdNeighbour.getOriginalPointCount()-1),
					thirdNeighbour.getGeoLocationForDistance(thirdNeighbour.getOriginalLength(),0,tangent));
			if (tangent!=null)
				assertColinear(thirdNeighbour.getOriginalVectorAt(thirdNeighbour.getOriginalPointCount()-2),tangent);
	
			//
	
	    	Random rnd = new Random();
	    	double distance, ratio;
	    	for(int i=0; i<TEST_COUNT; ++i) {
	    		ratio = rnd.nextDouble();
	
	    		distance = ratio * testedSegment.getOriginalLength();
	    		assertEpsilonEquals(testedSegment.getOriginalPointFor(ratio),
	    				testedSegment.getGeoLocationForDistance(distance,0,tangent));
	    		if (tangent!=null)
	    			assertColinear(testedSegment.getOriginalVectorFor(ratio),tangent);
	
	    		distance = ratio * loopSegment.getOriginalLength();
	    		assertEpsilonEquals(loopSegment.getOriginalPointFor(ratio),
	    				loopSegment.getGeoLocationForDistance(distance,0,tangent));
	    		if (tangent!=null)
	    			assertColinear(loopSegment.getOriginalVectorFor(ratio),tangent);
	
	    		distance = ratio * firstNeighbour.getOriginalLength();
	    		assertEpsilonEquals(firstNeighbour.getOriginalPointFor(ratio),
	    				firstNeighbour.getGeoLocationForDistance(distance,0,tangent));
	    		if (tangent!=null)
	    			assertColinear(firstNeighbour.getOriginalVectorFor(ratio),tangent);
	
	    		distance = ratio * secondNeighbour.getOriginalLength();
	    		assertEpsilonEquals(secondNeighbour.getOriginalPointFor(ratio),
	    				secondNeighbour.getGeoLocationForDistance(distance,0,tangent));
	    		if (tangent!=null)
	    			assertColinear(secondNeighbour.getOriginalVectorFor(ratio),tangent);
	
	    		distance = ratio * thirdNeighbour.getOriginalLength();
	    		assertEpsilonEquals(thirdNeighbour.getOriginalPointFor(ratio),
	    				thirdNeighbour.getGeoLocationForDistance(distance,0,tangent));
	    		if (tangent!=null)
	    			assertColinear(thirdNeighbour.getOriginalVectorFor(ratio),tangent);
	    	}
		}
	
		private void runTestGetGeoLocationForDistanceWithShift(RoadSegmentStub sgmt, double ratio, double shift) {
			Point2d p = sgmt.getOriginalPointFor(ratio);
			Vector2d v = sgmt.getOriginalVectorFor(ratio);
	
			Vector2d vp = new Vector2d(v);
			vp.makeOrthogonal();
			vp.scale(shift);
			Point2d pp = new Point2d(p.getX(),p.getY());
			pp.add(vp);
	
			double distance = ratio * sgmt.getOriginalLength();
			assertEpsilonEquals(pp, sgmt.getGeoLocationForDistance(distance,shift,(Vector2d)null));
		}
	
		@DisplayName("#1")
	    @Test
		public void testGetGeoLocationForDistance() {
			runTestGetGeoLocationForDistance(null);
			runTestGetGeoLocationForDistance(new Vector2d());
	
			runTestGetGeoLocationForDistanceWithShift(testedSegment, 0., -2.);
			runTestGetGeoLocationForDistanceWithShift(testedSegment, 0., 2.);
	
			runTestGetGeoLocationForDistanceWithShift(testedSegment, 1., -2.);
			runTestGetGeoLocationForDistanceWithShift(testedSegment, 1., 2.);
	
	    	Random rnd = new Random();
	    	double ratio, shift;
	    	for(int i=0; i<TEST_COUNT; ++i) {
	    		ratio = rnd.nextDouble();
	    		shift = rnd.nextDouble() * SHIFTING_RANGE;
	
	    		runTestGetGeoLocationForDistanceWithShift(testedSegment, ratio, -shift);
	    		runTestGetGeoLocationForDistanceWithShift(testedSegment, ratio, shift);
	       	}
		}
	}
}

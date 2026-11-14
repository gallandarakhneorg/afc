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

package org.arakhne.afc.gis.road.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.road.AbstractGisTest;
import org.arakhne.afc.math.geometry.base.d1.Direction1D;

/** Unit test for RoadPath.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("RoadPath")
@SuppressWarnings("all")
public class RoadPathTest extends AbstractGisTest {

	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9, segment10;
	private RoadSegmentStub segment11;
	private RoadConnectionStub connection1;
	private RoadConnectionStub connection2;
	private RoadConnectionStub connection3;
	private RoadConnectionStub connection4;
	private RoadConnectionStub connection5;
	private RoadConnectionStub connection6;
	private RoadConnectionStub connection7;
	private RoadConnectionStub connection8;
	private RoadConnectionStub connection9;

	@BeforeEach
	public void setUp() {
		connection1 = new RoadConnectionStub("connection1(1,4)", 0., 0.); //$NON-NLS-1$
		connection2 = new RoadConnectionStub("connection2(1,2)",100.,100.); //$NON-NLS-1$
		connection3 = new RoadConnectionStub("connection3(2,3,5,6,6,7,8,9,11)",100.,7.); //$NON-NLS-1$
		connection4 = new RoadConnectionStub("connection4(3,4)",-100.,-300); //$NON-NLS-1$
		connection5 = new RoadConnectionStub("connection5(5,10,11)",0.,10.); //$NON-NLS-1$
		connection6 = new RoadConnectionStub("connection6(7)",100.,-10.); //$NON-NLS-1$
		connection7 = new RoadConnectionStub("connection7(8)",170.,7.); //$NON-NLS-1$
		connection8 = new RoadConnectionStub("connection8(9)",120.,100.); //$NON-NLS-1$
		connection9 = new RoadConnectionStub("connection9(10)",-100.,0.); //$NON-NLS-1$

		segment1 = new RoadSegmentStub("segment1",0.,0.,100.,100.); //$NON-NLS-1$
		connection1.addSegment(segment1);
		connection2.addSegment(segment1);

		segment2 = new RoadSegmentStub("segment2",100.,7.,100.,100.); //$NON-NLS-1$
		connection3.addSegment(segment2);
		connection2.addSegment(segment2);

		segment3 = new RoadSegmentStub("segment3",100.0001,7.,-100.,-300.); //$NON-NLS-1$
		connection3.addSegment(segment3);
		connection4.addSegment(segment3);

		segment4 = new RoadSegmentStub("segment4",-100.,-300.,0.,0.); //$NON-NLS-1$
		connection4.addSegment(segment4);
		connection1.addSegment(segment4);

		segment5 = new RoadSegmentStub("segment5",100.,7.,0.,10.); //$NON-NLS-1$
		connection3.addSegment(segment5);
		connection5.addSegment(segment5);

		segment6 = new RoadSegmentStub("segment6", 100.,7.,200.,-20.,400.,0.,150.,50.,100.,7.); //$NON-NLS-1$
		connection3.addSegment(segment6);
		connection3.addSegment(segment6);

		segment7 = new RoadSegmentStub("segment7",100.,7.,100.,-10.); //$NON-NLS-1$
		connection3.addSegment(segment7);
		connection6.addSegment(segment7);

		segment8 = new RoadSegmentStub("segment8",170.,7.,100.,7.); //$NON-NLS-1$
		connection7.addSegment(segment8);
		connection3.addSegment(segment8);

		segment9 = new RoadSegmentStub("segment9",120.,100.,100.,7.); //$NON-NLS-1$
		connection8.addSegment(segment9);
		connection3.addSegment(segment9);

		segment10 = new RoadSegmentStub("segment10",-100.,0.,0.,10.); //$NON-NLS-1$
		connection9.addSegment(segment10);
		connection5.addSegment(segment10);

		segment11 = new RoadSegmentStub("segment11",0.,10.,-50.,100.,100.,500.,150.,100.,100.,7.); //$NON-NLS-1$
		connection5.addSegment(segment11);
		connection3.addSegment(segment11);
	}

	@AfterEach
	public void tearDown() {
		segment1 = segment2 = segment3 = segment4 = segment5 = null;
		segment6 = segment7 = segment8 = segment9 = null;
		connection1 = connection2 = connection3 = null;
		connection4 = connection5 = connection6 = null;
		connection7 = connection8 = connection9 = null;
	}

	@DisplayName("Constructor")
	@Nested
	public class ConstructorTest {

		@DisplayName("()")
		@Test
		public void testRoadPathVoid() {
			RoadPath path = new RoadPath();
			assertTrue(path.isEmpty());
		}
	
		@DisplayName("(segment,point)")
		@Nested
		public class RoadPathConstructorTest {
	
			private RoadPath path;
	
			@BeforeEach
			public void setUp() {
				path = new RoadPath(segment1, segment1.getBeginPoint());
			}
	
			@DisplayName("isEmpty")
			@Test
			public void isEmpty() {
				assertFalse(path.isEmpty());
			}
	
			@DisplayName("size")
			@Test
			public void size() {
				assertEquals(1, path.size());
			}
	
			@DisplayName("get")
			@Test
			public void get() {
				assertSame(segment1, path.get(0));
			}
		}
	}

	@DisplayName("getFirstCrossRoad")
	@Test
	public void testGetFirstCrossRoad() {
		RoadPath path = new RoadPath();
		path.add(segment3, segment3.getBeginPoint());
		path.add(segment4);
		path.add(segment1);
		path.add(segment2);
		path.add(segment6, segment6.getEndPoint());
		path.add(segment8);

		assertFalse(path.isEmpty());
		assertEquals(6, path.size());

		assertEquals(segment2.getBeginPoint(), path.getFirstCrossRoad());
	}

	@DisplayName("getFirstJunctionPoint")
	@Test
	public void testGetFirstJunctionPoint() {
		RoadPath path = new RoadPath();
		path.add(segment3, segment3.getBeginPoint());
		path.add(segment4);
		path.add(segment1);
		path.add(segment2);
		path.add(segment6, segment6.getEndPoint());
		path.add(segment8);

		CrossRoad cross = path.getFirstJunctionPoint();

		double distance = segment3.getLength()
							+segment4.getLength()
							+segment1.getLength()
							+segment2.getLength();

		assertEquals(segment2.getBeginPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(distance, cross.distanceFromPreviousCrossRoad);
		assertSame(segment2, cross.enteringSegment);
		assertSame(segment6, cross.exitingSegment);
	}

	@DisplayName("crossRoads")
	@Nested
	public class CrossRoadsTest {

		@DisplayName("Thru segment6")
		@Test
		public void testCrossRoadsVoid_thru_segment6() {
			RoadPath path = new RoadPath();
			path.add(segment3, segment3.getBeginPoint());
			path.add(segment4);
			path.add(segment1);
			path.add(segment2);
			path.add(segment6, segment6.getEndPoint());
			path.add(segment8);
	
			CrossRoad cross;
	
			double distance = segment3.getLength()
								+segment4.getLength()
								+segment1.getLength()
								+segment2.getLength();
	
			Iterator<CrossRoad> iterator = path.crossRoads();
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment2.getBeginPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(distance, cross.distanceFromPreviousCrossRoad);
			assertSame(segment2, cross.enteringSegment);
			assertSame(segment6, cross.exitingSegment);
	
			distance += segment6.getLength();
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment6.getEndPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(segment6.getLength(), cross.distanceFromPreviousCrossRoad);
			assertSame(segment6, cross.enteringSegment);
			assertSame(segment8, cross.exitingSegment);
	
			distance += segment8.getLength();
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment8.getBeginPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(segment8.getLength(), cross.distanceFromPreviousCrossRoad);
			assertSame(segment8, cross.enteringSegment);
			assertNull(cross.exitingSegment);
	
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("Not thru segment6")
		@Test
		public void testCrossRoadsVoid_not_thru_segment6() {
			RoadPath path = new RoadPath();
			path.add(segment3, segment3.getBeginPoint());
			path.add(segment4);
			path.add(segment1);
			path.add(segment2);
			path.add(segment3);
			path.add(segment4);
			path.add(segment1);
			path.add(segment2);
			path.add(segment7);
	
			CrossRoad cross;
	
			double distance = segment3.getLength()
								+segment4.getLength()
								+segment1.getLength()
								+segment2.getLength();
	
			Iterator<CrossRoad> iterator = path.crossRoads();
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment2.getBeginPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(distance, cross.distanceFromPreviousCrossRoad);
			assertSame(segment2, cross.enteringSegment);
			assertSame(segment3, cross.exitingSegment);
	
			double deltaD = segment3.getLength()
							+segment4.getLength()
							+segment1.getLength()
							+segment2.getLength();
			distance += deltaD;
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment2.getBeginPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
			assertSame(segment2, cross.enteringSegment);
			assertSame(segment7, cross.exitingSegment);
	
			deltaD = segment7.getLength();
			distance += deltaD;
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment7.getEndPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
			assertSame(segment7, cross.enteringSegment);
			assertNull(cross.exitingSegment);
	
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("Thru segment11 wuth cul-de-sac")
		@Test
		public void testCrossRoadsVoid_thru_segment11_with_culdesac() {
			RoadPath path = new RoadPath();
			path.add(segment5, segment5.getBeginPoint());
			path.add(segment10);
			path.add(segment10);
			path.add(segment11);
	
			CrossRoad cross;
	
			double deltaD = segment5.getLength();
			double distance = deltaD;
	
			Iterator<CrossRoad> iterator = path.crossRoads();
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment5.getEndPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
			assertSame(segment5, cross.enteringSegment);
			assertSame(segment10, cross.exitingSegment);
	
			deltaD = segment10.getLength();
			distance += deltaD;
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment10.getBeginPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
			assertSame(segment10, cross.enteringSegment);
			assertSame(segment10, cross.exitingSegment);
	
			deltaD = segment10.getLength();
			distance += deltaD;
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment10.getEndPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
			assertSame(segment10, cross.enteringSegment);
			assertSame(segment11, cross.exitingSegment);
	
			deltaD = segment11.getLength();
			distance += deltaD;
	
			assertTrue(iterator.hasNext());
			cross = iterator.next();
			assertNotNull(cross);
			assertEquals(segment11.getEndPoint(), cross.connectionPoint);
			assertEpsilonEquals(distance, cross.distance);
			assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
			assertSame(segment11, cross.enteringSegment);
			assertNull(cross.exitingSegment);
	
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("getSegmentDirectionAt")
	@Nested
	public class GetSegmentDirectionAtTest {

		private RoadPath path;

		@BeforeEach
		public void setUp() {
			path = new RoadPath();
			path.add(segment3, segment3.getBeginPoint()); // start -> end
			path.add(segment4); // start -> end
			path.add(segment1); // start -> end
			path.add(segment2); // end -> start
			path.add(segment6, segment6.getEndPoint()); // end -> start but also start -> end
			path.add(segment8); // end -> start
		}

		@DisplayName("(0)")
		@Test
		public void testGetSegmentDirectionAtInt_0() {
			assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(0));
		}

		@DisplayName("(1)")
		@Test
		public void testGetSegmentDirectionAtInt_1() {
			assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(1));
		}

		@DisplayName("(2)")
		@Test
		public void testGetSegmentDirectionAtInt_2() {
			assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(2));
		}

		@DisplayName("(3)")
		@Test
		public void testGetSegmentDirectionAtInt_3() {
			assertEquals(Direction1D.REVERTED_DIRECTION, path.getSegmentDirectionAt(3));
		}

		@DisplayName("(4)")
		@Test
		public void testGetSegmentDirectionAtInt_4() {
			assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(4));
		}

		@DisplayName("(5)")
		@Test
		public void testGetSegmentDirectionAtInt_5() {
			assertEquals(Direction1D.REVERTED_DIRECTION, path.getSegmentDirectionAt(5));
		}
	}
}

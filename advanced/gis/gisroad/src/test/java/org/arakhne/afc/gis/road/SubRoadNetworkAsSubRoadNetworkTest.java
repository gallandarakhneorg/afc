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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.graph.GraphIterator;

/**
 * Unit test for SubRoadNetwork as a SubRoadNetwork, assuming
 * that its SubGraph part was successfully tested.
 * This test class is testing if the road primitives replied by the
 * sub road network are really shadow of the primitive instances.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("SubRoadNetwork as SubRoadNetwork")
@SuppressWarnings("all")
public class SubRoadNetworkAsSubRoadNetworkTest extends AbstractGisTest {

	private StandardRoadNetwork network;
	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9, segment10;
	private RoadSegmentStub segment11;

	private SubRoadNetwork subNetwork;

	@BeforeEach
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
		try {
			Rectangle2d worldRect = new Rectangle2d(-500, -500, 1000, 1000);
			network = new StandardRoadNetwork(worldRect, new HeapAttributeCollection());

			//----------------------------------------
			// Picture testnetwork1.png
			//----------------------------------------

			segment1 = new RoadSegmentStub("segment1",0.,0.,100.,100.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 1);
			network.addRoadSegment(segment1);

			segment2 = new RoadSegmentStub("segment2",100.,7.,100.,100.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 2);
			network.addRoadSegment(segment2);

			segment3 = new RoadSegmentStub("segment3",100.0001,7.,-100.,-300.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 3);
			network.addRoadSegment(segment3);

			segment4 = new RoadSegmentStub("segment4",-100.,-300.,0.,0.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 4);
			network.addRoadSegment(segment4);

			segment5 = new RoadSegmentStub("segment5",100.,7.,0.,10.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 5);
			network.addRoadSegment(segment5);

			segment6 = new RoadSegmentStub("segment6",new double[] {100.,7.,200.,-20.,400.,0.,150.,50.,100.,7.}); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 6);
			network.addRoadSegment(segment6);

			segment7 = new RoadSegmentStub("segment7",100.,7.,100.,-10.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 7);
			network.addRoadSegment(segment7);

			segment8 = new RoadSegmentStub("segment8",170.,7.,100.,7.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 8);
			network.addRoadSegment(segment8);

			segment9 = new RoadSegmentStub("segment9",120.,100.,100.,7.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 9);
			network.addRoadSegment(segment9);

			segment10 = new RoadSegmentStub("segment10",-100.,0.,0.,10.); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 10);
			network.addRoadSegment(segment10);

			segment11 = new RoadSegmentStub("segment11",new double[] {0.,10.,-50.,100.,100.,500.,150.,100.,100.,7.}); //$NON-NLS-1$
			segment1.addUserData(USER_DATA_TEST, 11);
			network.addRoadSegment(segment11);

			subNetwork = new SubRoadNetwork();
		}
		catch(RoadNetworkException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterEach
	public void tearDown() {
		subNetwork = null;

		segment1.setStartPoint(null);
		segment1.setEndPoint(null);
		segment1.clear();
		segment1 = null;

		segment2.setStartPoint(null);
		segment2.setEndPoint(null);
		segment2.clear();
		segment2 = null;

		segment3.setStartPoint(null);
		segment3.setEndPoint(null);
		segment3.clear();
		segment3 = null;

		segment4.setStartPoint(null);
		segment4.setEndPoint(null);
		segment4.clear();
		segment4 = null;

		segment5.setStartPoint(null);
		segment5.setEndPoint(null);
		segment5.clear();
		segment5 = null;

		segment6.setStartPoint(null);
		segment6.setEndPoint(null);
		segment6.clear();
		segment6 = null;

		segment7.setStartPoint(null);
		segment7.setEndPoint(null);
		segment7.clear();
		segment7 = null;

		segment8.setStartPoint(null);
		segment8.setEndPoint(null);
		segment8.clear();
		segment8 = null;

		segment9.setStartPoint(null);
		segment9.setEndPoint(null);
		segment9.clear();
		segment9 = null;

		segment10.setStartPoint(null);
		segment10.setEndPoint(null);
		segment10.clear();
		segment10 = null;

		segment11.setStartPoint(null);
		segment11.setEndPoint(null);
		segment11.clear();
		segment11 = null;

		network = null;

		System.gc(); System.gc();
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@BeforeEach
		public void setUp() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		}
		
		@DisplayName("#1")
		@Test
		public void testIteratorWithWrappingTest_1() {
			var iterator = network.depthIterator(segment3, 50., 10.,
					segment3.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_T(segment3, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#2")
		@Test
		public void testIteratorWithWrappingTest_2() {
			var iterator = network.depthIterator(segment3, 400., 10.,
					segment3.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment4, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#3")
		@Test
		public void testIteratorWithWrappingTest_3() {
			var iterator = network.depthIterator(segment3, 690., 10.,
					segment3.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment4, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment1, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#4")
		@Test
		public void testIteratorWithWrappingTest_4() {
			var iterator = network.depthIterator(segment3, 830, 10.,
					segment3.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment4, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment1, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#5")
		@Test
		public void testIteratorWithWrappingTest_5() {
			var iterator = network.depthIterator(segment3, 920, 10.,
					segment3.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment4, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment1, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#6")
		@Test
		public void testIteratorWithWrappingTest_6() {
			var iterator = network.depthIterator(segment3, 1020, 10.,
					segment3.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment4, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment1, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#7")
		@Test
		public void testIteratorWithWrappingTest_7() {
			var iterator = network.depthIterator(segment8, 50., 10.,
					segment8.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_T(segment8, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#8")
		@Test
		public void testIteratorWithWrappingTest_8() {
			var iterator = network.depthIterator(segment8, 400., 10.,
					segment8.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment8, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment2, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment5, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment7, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment6, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment11, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#9")
		@Test
		public void testIteratorWithWrappingTest_9() {
			var iterator = network.depthIterator(segment8, 690., 10.,
					segment8.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment8, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment2, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment5, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment7, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment6, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment11, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment4, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#10")
		@Test
		public void testIteratorWithWrappingTest_10() {
			var iterator = network.depthIterator(segment8, 830, 10.,
					segment8.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment8, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment2, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment5, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment7, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment6, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment11, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment4, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment1, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#11")
		@Test
		public void testIteratorWithWrappingTest_11() {
			var iterator = network.depthIterator(segment8, 920, 10.,
					segment8.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment8, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment2, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment5, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment7, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment6, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment11, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment4, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment1, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
		
		@DisplayName("#12")
		@Test
		public void testIteratorWithWrappingTest_12() {
			var iterator = network.depthIterator(segment8, 1020, 10.,
					segment8.getBeginPoint(),false,true);
			subNetwork.build(iterator);
			var iterator2 = subNetwork.iterator();
			assertTrue(iterator2.hasNext());
			assertWrapSegment_T_W(segment8, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment2, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment5, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment3, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment7, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment6, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment11, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_W(segment4, iterator2.next());
			assertTrue(iterator2.hasNext());
			assertWrapSegment_W_T(segment1, iterator2.next());
			assertFalse(iterator2.hasNext());
		}
	}
}

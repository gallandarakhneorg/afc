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

/** Unit test for SubRoadNetwork as a SubGraph.
 *
 * <p>PICTURE: {@code testnetwork1.png}
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("SubRoadNetwork as SubGraphNetwork")
@SuppressWarnings("all")
public class SubRoadNetworkAsSubGraphTest extends AbstractGisTest {

	private StandardRoadNetwork network;
	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9, segment10;
	private RoadSegmentStub segment11;

	private SubRoadNetwork subNetwork;

	protected void initNetwork() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
		try {
			Rectangle2d worldRect = new Rectangle2d(-500, -500, 1000, 1000);
			network = new StandardRoadNetwork(worldRect, new HeapAttributeCollection());

			//-------------------------------
			// PICTURE testnetwork1.png
			//-------------------------------

			segment1 = new RoadSegmentStub("segment1",0.,0.,100.,100.); //$NON-NLS-1$
			network.addRoadSegment(segment1);

			segment2 = new RoadSegmentStub("segment2",100.,7.,100.,100.); //$NON-NLS-1$
			network.addRoadSegment(segment2);

			segment3 = new RoadSegmentStub("segment3",100.0001,7.,-100.,-300.); //$NON-NLS-1$
			network.addRoadSegment(segment3);

			segment4 = new RoadSegmentStub("segment4",-100.,-300.,0.,0.); //$NON-NLS-1$
			network.addRoadSegment(segment4);

			segment5 = new RoadSegmentStub("segment5",100.,7.,0.,10.); //$NON-NLS-1$
			network.addRoadSegment(segment5);

			segment6 = new RoadSegmentStub("segment6",new double[] {100.,7.,200.,-20.,400.,0.,150.,50.,100.,7.}); //$NON-NLS-1$
			network.addRoadSegment(segment6);

			segment7 = new RoadSegmentStub("segment7",100.,7.,100.,-10.); //$NON-NLS-1$
			network.addRoadSegment(segment7);

			segment8 = new RoadSegmentStub("segment8",170.,7.,100.,7.); //$NON-NLS-1$
			network.addRoadSegment(segment8);

			segment9 = new RoadSegmentStub("segment9",120.,100.,100.,7.); //$NON-NLS-1$
			network.addRoadSegment(segment9);

			segment10 = new RoadSegmentStub("segment10",-100.,0.,0.,10.); //$NON-NLS-1$
			network.addRoadSegment(segment10);

			segment11 = new RoadSegmentStub("segment11",new double[] {0.,10.,-50.,100.,100.,500.,150.,100.,100.,7.}); //$NON-NLS-1$
			network.addRoadSegment(segment11);

			subNetwork = new SubRoadNetwork();
		}
		catch(RoadNetworkException e) {
			throw new RuntimeException(e);
		}
	}

	@DisplayName("getSegmentCount")
	@Nested
	public class GetSegmentCountTest {

		@BeforeEach
		public void setUp() throws Exception {
			initNetwork();
		}

		@DisplayName("#1")
		@Test
		public void testSize_1() {
			var iterator = network.depthIterator(
					segment3, 10., 50.,
					segment3.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(1, subNetwork.getSegmentCount());
		}

		@DisplayName("#2")
		@Test
		public void testSize_2() {
			var iterator = network.depthIterator(
					segment3, 400., 10.,
					segment3.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(2, subNetwork.getSegmentCount());
		}

		@DisplayName("#3")
		@Test
		public void testSize_3() {
			var iterator = network.depthIterator(
					segment3, 690., 10.,
					segment3.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(3, subNetwork.getSegmentCount());
		}

		@DisplayName("#4")
		@Test
		public void testSize_4() {
			var iterator = network.depthIterator(
					segment3, 830, 10.,
					segment3.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(3, subNetwork.getSegmentCount());
		}

		@DisplayName("#5")
		@Test
		public void testSize_5() {
			var iterator = network.depthIterator(
					segment3, 920, 10.,
					segment3.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(3, subNetwork.getSegmentCount());
		}

		@DisplayName("#6")
		@Test
		public void testSize_6() {
			var iterator = network.depthIterator(
					segment8, 10., 50.,
					segment8.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(1, subNetwork.getSegmentCount());
		}

		@DisplayName("#7")
		@Test
		public void testSize_7() {
			var iterator = network.depthIterator(
					segment8, 400., 10.,
					segment8.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(7, subNetwork.getSegmentCount());
		}

		@DisplayName("#8")
		@Test
		public void testSize_8() {
			var iterator = network.depthIterator(
					segment8, 690., 10.,
					segment8.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(8, subNetwork.getSegmentCount());
		}

		@DisplayName("#9")
		@Test
		public void testSize_9() {
			var iterator = network.depthIterator(
					segment8, 830, 10.,
					segment8.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(9, subNetwork.getSegmentCount());
		}

		@DisplayName("#10")
		@Test
		public void testSize_10() {
			var iterator = network.depthIterator(
					segment8, 920, 10.,
					segment8.getBeginPoint(),
					false,true);
			subNetwork.build(iterator);
			assertEquals(9, subNetwork.getSegmentCount());
		}
	}

	@DisplayName("depthIterator")
	@Nested
	public class DepthIteratorTest {

		@BeforeEach
		public void setUp() throws Exception {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			initNetwork();
		}
		
		@DisplayName("(RoadSegment,double,double,RoadConnection)")
		@Nested
		public class RoadSegmentDoubleDoubleRoadConnectionTest {

			@DisplayName("Test 1")
			@Nested
			public class ATest {

				@BeforeEach
				public void setUp() {
					var mainIterator = network.depthIterator(
							segment3, 1020, 10.,
							segment3.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
				}
				
				@DisplayName("#1")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_1() {
					var iterator = subNetwork.depthIterator(segment3, 50., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#2")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_2() {
					var iterator = subNetwork.depthIterator(segment3, 400., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#3")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_3() {
					var iterator = subNetwork.depthIterator(segment3, 690., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#4")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_4() {
					var iterator = subNetwork.depthIterator(segment3, 830., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#5")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_5() {
					var iterator = subNetwork.depthIterator(segment3, 920., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#6")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_6() {
					var iterator = subNetwork.depthIterator(segment3, 1020., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}

			@DisplayName("Test 2")
			@Nested
			public class BTest {

				@BeforeEach
				public void setUp() {
					var mainIterator = network.depthIterator(
							segment8, 1020, 10.,
							segment8.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
				}
				
				@DisplayName("#1")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_1() {
					var iterator = subNetwork.depthIterator(segment8, 50., 10.,
							segment8.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#2")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_2() {
					var iterator = subNetwork.depthIterator(segment8, 400., 10.,
							segment8.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#3")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_3() {
					var iterator = subNetwork.depthIterator(segment8, 690., 10.,
							segment8.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#4")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_4() {
					var iterator = subNetwork.depthIterator(segment8, 830., 10.,
							segment8.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#5")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_5() {
					var iterator = subNetwork.depthIterator(segment8, 920., 10.,
							segment8.getBeginPoint(),false,true);
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#6")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_6() {
					var iterator = subNetwork.depthIterator(segment8, 1020., 10.,
							segment8.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}

			@DisplayName("Test 3")
			@Nested
			public class CTest {

				@BeforeEach
				public void setUp() {
					var mainIterator = network.depthIterator(
							segment3, 1020, 10., segment3.getEndPoint(),
							false,true);
					subNetwork.build(mainIterator);
				}
				
				@DisplayName("#1")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_1() {
					var iterator = subNetwork.depthIterator(segment3, 50., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#2")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_2() {
					var iterator = subNetwork.depthIterator(segment3, 400., 10.,
							segment3.getBeginPoint(),false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}
		}
		
		@DisplayName("(RoadSegment,double,double)")
		@Nested
		public class RoadSegmentDoubleDoubleTest {

			@DisplayName("Test 1")
			@Nested
			public class ATest {

				@BeforeEach
				public void setUp() {
					var mainIterator = network.depthIterator(
							segment3, 1020, 10.,
							segment3.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
				}
				
				@DisplayName("#1")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_1() {
					var iterator = subNetwork.depthIterator(segment3, 50., 10.,
							segment3.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#2")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_2() {
					var iterator = subNetwork.depthIterator(segment3, 400., 10.,
							segment3.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#3")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_3() {
					var iterator = subNetwork.depthIterator(segment3, 690., 10.,
							segment3.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#4")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_4() {
					var iterator = subNetwork.depthIterator(segment3, 830., 10.,
							segment3.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#5")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_5() {
					var iterator = subNetwork.depthIterator(segment3, 920., 10.,
							segment3.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#6")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_6() {
					var iterator = subNetwork.depthIterator(segment3, 1020., 10.,
							segment3.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}			

			@DisplayName("Test 2")
			@Nested
			public class BTest {

				@BeforeEach
				public void setUp() {
					var mainIterator = network.depthIterator(
							segment8, 1020, 10.,
							segment8.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
				}
				
				@DisplayName("#1")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_1() {
					var iterator = subNetwork.depthIterator(segment8, 50., 10.,
							segment8.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#2")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_2() {
					var iterator = subNetwork.depthIterator(segment8, 400., 10.,
							segment8.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#3")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_3() {
					var iterator = subNetwork.depthIterator(segment8, 690., 10.,
							segment8.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#4")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_4() {
					var iterator = subNetwork.depthIterator(segment8, 830., 10.,
							segment8.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#5")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_5() {
					var iterator = subNetwork.depthIterator(segment8, 920., 10.,
							segment8.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
				
				@DisplayName("#6")
				@Test
				public void testDepthIteratorRoadSegmentDoubleDouble_6() {
					var iterator = subNetwork.depthIterator(segment8, 1020., 10.,
							segment8.getBeginPoint(),
							false,true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}
		}
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {
	
		@DisplayName("()")
		@Nested
		public class VoidTest {

			@BeforeEach
			public void setUp() throws Exception {
				assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
				initNetwork();
			}

			@DisplayName("#1")
			@Test
			public void testIteratorVoid_1() {
				var mainIterator = network.depthIterator(
						segment3, 920, 10.,
						segment3.getBeginPoint(),
						false,true);
				subNetwork.build(mainIterator);

				var iterator = subNetwork.iterator();
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertFalse(iterator.hasNext());
			}
		
			@DisplayName("#2")
			@Test
			public void testIteratorVoid_2() {
				var mainIterator = network.depthIterator(
						segment8, 920, 10.,
						segment8.getBeginPoint(),
						false,true);
				subNetwork.build(mainIterator);
		
				var iterator = subNetwork.iterator();
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment11, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("(RoadSegment,RoadConnection,boolean,boolean)")
		@Nested
		public class RoadSegmentRoadConnectionBooleanBooleanTest {

			@DisplayName("No cycle - Not oriented")
			@Nested
			public class NoCycleNotOrientedTest {
				
				@BeforeEach
				public void setUp() throws Exception {
					assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
					initNetwork();
					var mainIterator = network.depthIterator(
							segment3, 1020, 10.,
							segment3.getBeginPoint(),
							false,false);
					subNetwork.build(mainIterator);
				}

				@DisplayName("#1")
				@Test
				public void testIteratorRoadSegmentRoadConnectionBoolean_disableCycles_notOriented() throws Exception {
					var iterator = subNetwork.iterator(segment3, segment3.getBeginPoint(), false, false);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment9, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment10, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}

			@DisplayName("No cycle - Oriented")
			@Nested
			public class NoCycleOrientedTest {
				
				@BeforeEach
				public void setUp() throws Exception {
					assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
					initNetwork();
				}

				@DisplayName("#1")
				@Test
				public void testIteratorRoadSegmentRoadConnectionBoolean_disableCycles_oriented_1() throws Exception {
					var mainIterator = network.depthIterator(
							segment3, 1020, 10.,
							segment3.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
					var iterator = subNetwork.iterator(segment3, segment3.getBeginPoint(), false, true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}

				@DisplayName("#2")
				@Test
				public void testIteratorRoadSegmentRoadConnectionBoolean_disableCycles_oriented_2() throws Exception {
					var mainIterator = network.depthIterator(
							segment8, 1020, 10.,
							segment8.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
					var iterator = subNetwork.iterator(segment8, segment8.getBeginPoint(), false, true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}

			@DisplayName("Cycle - Not oriented")
			@Nested
			public class CycleNotOrientedTest {
				
				@BeforeEach
				public void setUp() throws Exception {
					assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
					initNetwork();
				}

				@DisplayName("#1")
				@Test
				public void testIteratorRoadSegmentRoadConnectionBoolean_enableCycles_notOriented_1() throws Exception {
					var mainIterator = network.depthIterator(segment3, 1020, 10.,
							segment3.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
					var iterator = subNetwork.iterator(
							segment3, segment3.getBeginPoint(),
							true, false);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}

				@DisplayName("#2")
				@Test
				public void testIteratorRoadSegmentRoadConnectionBoolean_enableCycles_notOriented_2() throws Exception {
					var mainIterator = network.depthIterator(segment8, 1020, 10.,
							segment8.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
					var iterator = subNetwork.iterator(
							segment8, segment8.getBeginPoint(),
							true, false);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment9, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment9, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment10, iterator.next());
					assertTrue(iterator.hasNext());
				}
			}

			@DisplayName("Cycle - Oriented")
			@Nested
			public class CycleOrientedTest {
				
				@BeforeEach
				public void setUp() throws Exception {
					assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
					initNetwork();
				}

				@DisplayName("#1")
				@Test
				public void testIteratorRoadSegmentRoadConnectionBoolean_enableCycles_oriented_1() throws Exception {
					var mainIterator = network.depthIterator(segment3, 1020, 10.,
							segment3.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
					var iterator = subNetwork.iterator(segment3, segment3.getBeginPoint(),
							true, true);
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment1, iterator.next());
					assertFalse(iterator.hasNext());
				}

				@DisplayName("#2")
				@Test
				public void testIteratorRoadSegmentRoadConnectionBoolean_enableCycles_oriented_2() throws Exception {
					var mainIterator = network.depthIterator(segment8, 1020, 10.,
							segment8.getBeginPoint(),
							false,true);
					subNetwork.build(mainIterator);
					var iterator = subNetwork.iterator(segment8, segment8.getBeginPoint(),
							true, true);
					assertTrue(iterator.hasNext());
					assertEquals(segment8, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment7, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment6, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertEquals(segment5, iterator.next());
					assertTrue(iterator.hasNext());
				}
			}
		}
	}
}

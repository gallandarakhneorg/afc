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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.graph.GraphIterator;

/** Unit test for RoadIterator.
 *
 * <p>Picture {@code testnetwork2.png}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("RoadIterator")
@SuppressWarnings("all")
public class RoadIteratorTest extends AbstractGisTest {

	private RoadNetwork network;
	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9;

	@BeforeEach
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
		try {

			//-----------------------------------
			// Picture testnetwork2.png
			//-----------------------------------

			Rectangle2d worldRect = new Rectangle2d(-500, -500, 1000, 1000);

			network = new StandardRoadNetwork(worldRect, new HeapAttributeCollection());
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
		}
		catch(RoadNetworkException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterEach
	public void tearDown() {
		segment1 = segment2 = segment3 = segment4 = segment5 = null;
		segment6 = segment7 = segment8 = segment9 = null;
		network = null;
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@DisplayName("No cycle - not oriented")
		@Test
	    public void testIteratorNoCycleNotOriented() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
	
			GraphIterator<RoadSegment,RoadConnection> iterator = network.iterator(segment1, segment1.getEndPoint(), false, false);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment4, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment3, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment7, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment6, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment8, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment9, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment2, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment5, iterator.next());
	    	assertFalse(iterator.hasNext());
	    }

		@DisplayName("No cycle - oriented #1")
		@Test
	    public void testIteratorNoCycleOriented_01() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

			GraphIterator<RoadSegment,RoadConnection> iterator = network.iterator(segment1, segment1.getEndPoint(), false, true);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());

	    	assertFalse(iterator.hasNext());
	    }

		@DisplayName("No cycle - oriented #2")
		@Test
	    public void testIteratorNoCycleOriented_02() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

			GraphIterator<RoadSegment,RoadConnection> iterator = network.iterator(segment1, segment1.getBeginPoint(), false, true);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());

	    	assertFalse(iterator.hasNext());
	    }

		@DisplayName("No cycle - oriented #3")
		@Test
	    public void testIteratorNoCycleOriented_03() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

			GraphIterator<RoadSegment,RoadConnection> iterator = network.iterator(segment8, segment1.getBeginPoint(), false, true);
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
	    	assertEquals(segment4, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());

	    	assertFalse(iterator.hasNext());
	    }

		@DisplayName("Cycle - not oriented")
		@Test
	    public void testIteratorCycleNotOriented() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

	    	GraphIterator<RoadSegment,RoadConnection> iterator = network.iterator(segment1, segment1.getEndPoint(), true, false);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment4, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment3, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment7, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment6, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment8, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment6, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment9, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment2, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment5, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment8, iterator.next());
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
	    	assertEquals(segment8, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment9, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment2, iterator.next());

	    	assertTrue(iterator.hasNext());
	    }
	}

	@DisplayName("depthIterator")
	@Nested
	public class DepthIteratorTest {

		@DisplayName("#1")
		@Test
		public void testDepthIterator_1() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			var iterator = network.depthIterator(segment1, 1000., 0., segment1.getEndPoint(),false,true);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertFalse(iterator.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void testDepthIterator_2() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
	    	var iterator = network.depthIterator(segment1, 500., 0., segment1.getEndPoint(),false,true);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertFalse(iterator.hasNext());
		}

		@DisplayName("#3")
		@Test
		public void testDepthIterator_3() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
	    	var iterator = network.depthIterator(segment1, 500., Math.hypot(100,100)-1, segment1.getEndPoint(),false,true);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertFalse(iterator.hasNext());
		}

		@DisplayName("#4")
		@Test
		public void testDepthIterator_4() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			var iterator = network.depthIterator(segment1, 1500., 0., segment1.getEndPoint(),false,true);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertFalse(iterator.hasNext());
		}
	
		@DisplayName("#5")
		@Test
		public void testDepthIterator_5() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			var iterator = network.depthIterator(segment1, 1000., 0., segment1.getEndPoint(),false,false);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment4, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment3, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment7, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment6, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment8, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment9, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment2, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment5, iterator.next());
	    	assertFalse(iterator.hasNext());
		}
		
		@DisplayName("#6")
		@Test
		public void testDepthIterator_6() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
	    	var iterator = network.depthIterator(segment1, 500., 0., segment1.getEndPoint(),false,false);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment4, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment3, iterator.next());
	    	assertFalse(iterator.hasNext());
		}
		
		@DisplayName("#7")
		@Test
		public void testDepthIterator_7() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
	    	var iterator = network.depthIterator(segment1, 500., Math.hypot(100,100)-1, segment1.getEndPoint(),false,false);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment4, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment3, iterator.next());
	    	assertFalse(iterator.hasNext());
		}
		
		@DisplayName("#8")
		@Test
		public void testDepthIterator_8() {
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			var iterator = network.depthIterator(segment1, 1500., 0., segment1.getEndPoint(),false,false);
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment1, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment4, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment3, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment7, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment6, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment8, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment9, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment2, iterator.next());
	    	assertTrue(iterator.hasNext());
	    	assertEquals(segment5, iterator.next());
	    	assertFalse(iterator.hasNext());
		}
	}
}

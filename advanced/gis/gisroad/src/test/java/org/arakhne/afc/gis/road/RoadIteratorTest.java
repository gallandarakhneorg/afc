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

package org.arakhne.afc.gis.road;

import static org.junit.Assume.assumeTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
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
@SuppressWarnings("all")
public class RoadIteratorTest extends AbstractGisTest {

	private RoadNetwork network;
	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9;

	@Before
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
		try {

			//-----------------------------------
			// Picture testnetwork2.png
			//-----------------------------------

			Rectangle2d worldRect = new Rectangle2d(-500, -500, 1000, 1000);

			this.network = new StandardRoadNetwork(worldRect, new HeapAttributeCollection());
			this.segment1 = new RoadSegmentStub("segment1",0.,0.,100.,100.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment1);

			this.segment2 = new RoadSegmentStub("segment2",100.,7.,100.,100.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment2);

			this.segment3 = new RoadSegmentStub("segment3",100.0001,7.,-100.,-300.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment3);

			this.segment4 = new RoadSegmentStub("segment4",-100.,-300.,0.,0.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment4);

			this.segment5 = new RoadSegmentStub("segment5",100.,7.,0.,10.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment5);

			this.segment6 = new RoadSegmentStub("segment6",new double[] {100.,7.,200.,-20.,400.,0.,150.,50.,100.,7.}); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment6);

			this.segment7 = new RoadSegmentStub("segment7",100.,7.,100.,-10.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment7);

			this.segment8 = new RoadSegmentStub("segment8",170.,7.,100.,7.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment8);

			this.segment9 = new RoadSegmentStub("segment9",120.,100.,100.,7.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment9);
		}
		catch(RoadNetworkException e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void tearDown() {
		this.segment1 = this.segment2 = this.segment3 = this.segment4 = this.segment5 = null;
		this.segment6 = this.segment7 = this.segment8 = this.segment9 = null;
		this.network = null;
	}

	@Test
    public void testIteratorNoCycleNotOriented() {
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> iterator = this.network.iterator(this.segment1, this.segment1.getEndPoint(), false, false);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment4, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment7, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment6, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment8, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment9, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment2, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment5, iterator.next());
    	assertFalse(iterator.hasNext());
    }

	@Test
    public void testIteratorNoCycleOriented_01() {
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> iterator = this.network.iterator(this.segment1, this.segment1.getEndPoint(), false, true);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());

    	assertFalse(iterator.hasNext());
    }

	@Test
    public void testIteratorNoCycleOriented_02() {
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> iterator = this.network.iterator(this.segment1, this.segment1.getBeginPoint(), false, true);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());

    	assertFalse(iterator.hasNext());
    }

	@Test
    public void testIteratorNoCycleOriented_03() {
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> iterator = this.network.iterator(this.segment8, this.segment1.getBeginPoint(), false, true);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment8, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment2, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment5, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment7, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment6, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment4, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());

    	assertFalse(iterator.hasNext());
    }

	@Test
    public void testIteratorCycleNotOriented() {
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

    	GraphIterator<RoadSegment,RoadConnection> iterator = this.network.iterator(this.segment1, this.segment1.getEndPoint(), true, false);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment4, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment7, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment6, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment8, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment6, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment9, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment2, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment5, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment8, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment9, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment2, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment5, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment7, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment8, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment9, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment2, iterator.next());

    	assertTrue(iterator.hasNext());
    }

	@Test
	public void testDepthIterator_01() {
    	GraphIterator<RoadSegment,RoadConnection> iterator;

		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		iterator = this.network.depthIterator(this.segment1, 1000., 0., this.segment1.getEndPoint(),false,true);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertFalse(iterator.hasNext());

    	iterator = this.network.depthIterator(this.segment1, 500., 0., this.segment1.getEndPoint(),false,true);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertFalse(iterator.hasNext());

    	iterator = this.network.depthIterator(this.segment1, 500., Math.hypot(100,100)-1, this.segment1.getEndPoint(),false,true);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertFalse(iterator.hasNext());

		iterator = this.network.depthIterator(this.segment1, 1500., 0., this.segment1.getEndPoint(),false,true);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertFalse(iterator.hasNext());
	}

	@Test
	public void testDepthIterator_02() {
    	GraphIterator<RoadSegment,RoadConnection> iterator;

		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		iterator = this.network.depthIterator(this.segment1, 1000., 0., this.segment1.getEndPoint(),false,false);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment4, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment7, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment6, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment8, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment9, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment2, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment5, iterator.next());
    	assertFalse(iterator.hasNext());

    	iterator = this.network.depthIterator(this.segment1, 500., 0., this.segment1.getEndPoint(),false,false);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment4, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertFalse(iterator.hasNext());

    	iterator = this.network.depthIterator(this.segment1, 500., Math.hypot(100,100)-1, this.segment1.getEndPoint(),false,false);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment4, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertFalse(iterator.hasNext());

		iterator = this.network.depthIterator(this.segment1, 1500., 0., this.segment1.getEndPoint(),false,false);
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment1, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment4, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment3, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment7, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment6, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment8, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment9, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment2, iterator.next());
    	assertTrue(iterator.hasNext());
    	assertEquals(this.segment5, iterator.next());
    	assertFalse(iterator.hasNext());
	}

}

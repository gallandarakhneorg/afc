/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Unit test for RoadNetwork.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class StandardRoadNetworkTest extends AbstractGisTest {

	private StandardRoadNetwork network;
	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9;

	@Before
	public void setUp() throws Exception {
		try {
			GeoLocationUtil.setGISCoordinateSystemAsDefault();
			Rectangle2d worldRect = new Rectangle2d(-500, -500, 1000, 1000);
			this.network = new StandardRoadNetwork(worldRect, new HeapAttributeCollection());

			//-----------------------------------
			// Picture testnetwork2.png
			//-----------------------------------

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
	public void testAddRoadSegment() throws Exception {
		try {
			this.network.addRoadSegment(null);
			fail("an exception must be throwned when a null road segment is added into a network"); //$NON-NLS-1$
		}
		catch(RoadNetworkException exception) {
			// Expected behavior
		}

		// Try to add a segment that has approximatively the coordinates as
		// an existing segment
		RoadSegmentStub segment3bis = new RoadSegmentStub("segment3bis",100.0001,7.,-100.,-300.); //$NON-NLS-1$
		this.network.addRoadSegment(segment3bis);

		// Must do nothing because the segment is already in the road segment.
		this.network.addRoadSegment(this.segment4);

		// Check the network segment list

		assertEquals(9, this.network.getSegmentCount());

		Collection<RoadSegmentStub> expectedList = new ArrayList<>();
		Collections.addAll(expectedList,
				this.segment1, this.segment2, this.segment3, this.segment4, this.segment5,
				this.segment6, this.segment7, this.segment8, this.segment9);

		for(RoadSegment segment : this.network) {
			assertTrue(expectedList.contains(segment));
			expectedList.remove(segment);
		}
		assertTrue(expectedList.isEmpty());

		Collections.addAll(expectedList,
				this.segment1, this.segment2, this.segment3, this.segment4, this.segment5,
				this.segment6, this.segment7, this.segment8, this.segment9);
		for(RoadSegment segment : this.network.getRoadSegments()) {
			assertTrue(expectedList.contains(segment));
			expectedList.remove(segment);
		}
		assertTrue(expectedList.isEmpty());

		// Check the network connections
		// -100;-300	=> segment3,segment4
		// 0;0			=> segment1,segment4
		// 0;10			=> segment5
		// 100;7		=> segment2,segment3,segment5
		// 100;100		=> segment1,segment2

		assertEquals(this.segment1.getFirstPoint(),this.segment4.getLastPoint());
		assertEquals(this.segment1.getLastPoint(),this.segment2.getLastPoint());

		assertEquals(this.segment2.getFirstPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment6.getFirstPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment6.getLastPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(this.segment2.getLastPoint(),this.segment1.getLastPoint());

		assertEquals(this.segment3.getFirstPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment6.getFirstPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment6.getLastPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(this.segment3.getLastPoint(),this.segment4.getFirstPoint());

		assertEquals(this.segment4.getFirstPoint(),this.segment3.getLastPoint());
		assertEquals(this.segment4.getLastPoint(),this.segment1.getFirstPoint());

		assertEquals(this.segment5.getFirstPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment6.getFirstPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment6.getLastPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(new Point2d(0.,10.), this.segment5.getLastPoint());

		assertEquals(this.segment6.getFirstPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment6.getFirstPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment6.getFirstPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment6.getFirstPoint(),this.segment6.getLastPoint());
		assertEquals(this.segment6.getFirstPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment6.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment6.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(this.segment6.getLastPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment6.getLastPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment6.getLastPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment6.getLastPoint(),this.segment6.getFirstPoint());
		assertEquals(this.segment6.getLastPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment6.getLastPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment6.getLastPoint(),this.segment9.getLastPoint());

		assertEquals(this.segment7.getFirstPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment6.getFirstPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment6.getLastPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(new Point2d(100.,-10.), this.segment7.getLastPoint());

		assertEquals(new Point2d(170.,7.), this.segment8.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment6.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment6.getLastPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment9.getLastPoint());

		assertEquals(new Point2d(120.,100.), this.segment9.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment6.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment6.getLastPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment8.getLastPoint());
	}

	@Test
	public void testConnectionIterator_LeftHand() {
		CoordinateSystem2D system = CoordinateSystem2D.XY_LEFT_HAND;

		// 0;0			=> segment1 (out), segment4 (in)
		Iterator<RoadSegment> iterator = this.segment1.getBeginPoint().toCounterclockwiseIterator(this.segment1, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;100		=> segment1 (in), segment2 (in)
		iterator = this.segment1.getEndPoint().toCounterclockwiseIterator(this.segment1, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment2.getBeginPoint().toCounterclockwiseIterator(this.segment2, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;100		=> segment1 (in), segment2 (in)
		iterator = this.segment2.getEndPoint().toCounterclockwiseIterator(this.segment2, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment3.getBeginPoint().toCounterclockwiseIterator(this.segment3, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertFalse(iterator.hasNext());

		// -100;-300	=> segment3 (in), segment4 (out)
		iterator = this.segment3.getEndPoint().toCounterclockwiseIterator(this.segment3, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertFalse(iterator.hasNext());

		// -100;-300	=> segment3 (in), segment4 (out)
		iterator = this.segment4.getBeginPoint().toCounterclockwiseIterator(this.segment4, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		// 0;0			=> segment1 (out), segment4 (in)
		iterator = this.segment4.getEndPoint().toCounterclockwiseIterator(this.segment4, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment5.getBeginPoint().toCounterclockwiseIterator(this.segment5, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		// 0;10			=> segment5 (in)
		iterator = this.segment5.getEndPoint().toCounterclockwiseIterator(this.segment5, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment6.getBeginPoint().toCounterclockwiseIterator(this.segment6, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment6.getEndPoint().toCounterclockwiseIterator(this.segment6, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;-10		=> segment7 (in)
		iterator = this.segment7.getBeginPoint().toCounterclockwiseIterator(this.segment7, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment7.getEndPoint().toCounterclockwiseIterator(this.segment7, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertFalse(iterator.hasNext());

		// 170;7		=> segment8 (out)
		iterator = this.segment8.getBeginPoint().toCounterclockwiseIterator(this.segment8, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment8.getEndPoint().toCounterclockwiseIterator(this.segment8, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertFalse(iterator.hasNext());

		// 120;100		=> segment9 (out)
		iterator = this.segment9.getBeginPoint().toCounterclockwiseIterator(this.segment9, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment9.getEndPoint().toCounterclockwiseIterator(this.segment9, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testConnectionIterator_RightHand() {
		CoordinateSystem2D system = CoordinateSystem2D.XY_RIGHT_HAND;

		// 0;0			=> segment1 (out), segment4 (in)
		Iterator<RoadSegment> iterator = this.segment1.getBeginPoint().toCounterclockwiseIterator(this.segment1, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;100		=> segment1 (in), segment2 (in)
		iterator = this.segment1.getEndPoint().toCounterclockwiseIterator(this.segment1, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment2.getBeginPoint().toCounterclockwiseIterator(this.segment2, system);
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
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;100		=> segment1 (in), segment2 (in)
		iterator = this.segment2.getEndPoint().toCounterclockwiseIterator(this.segment2, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment3.getBeginPoint().toCounterclockwiseIterator(this.segment3, system);
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
		assertFalse(iterator.hasNext());

		// -100;-300	=> segment3 (in), segment4 (out)
		iterator = this.segment3.getEndPoint().toCounterclockwiseIterator(this.segment3, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertFalse(iterator.hasNext());

		// -100;-300	=> segment3 (in), segment4 (out)
		iterator = this.segment4.getBeginPoint().toCounterclockwiseIterator(this.segment4, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		// 0;0			=> segment1 (out), segment4 (in)
		iterator = this.segment4.getEndPoint().toCounterclockwiseIterator(this.segment4, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment5.getBeginPoint().toCounterclockwiseIterator(this.segment5, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
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
		assertFalse(iterator.hasNext());

		// 0;10			=> segment5 (in)
		iterator = this.segment5.getEndPoint().toCounterclockwiseIterator(this.segment5, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment6.getBeginPoint().toCounterclockwiseIterator(this.segment6, system);
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
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment6.getEndPoint().toCounterclockwiseIterator(this.segment6, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
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
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;-10		=> segment7 (in)
		iterator = this.segment7.getBeginPoint().toCounterclockwiseIterator(this.segment7, system);
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
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment7.getEndPoint().toCounterclockwiseIterator(this.segment7, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertFalse(iterator.hasNext());

		// 170;7		=> segment8 (out)
		iterator = this.segment8.getBeginPoint().toCounterclockwiseIterator(this.segment8, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment8.getEndPoint().toCounterclockwiseIterator(this.segment8, system);
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
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertFalse(iterator.hasNext());

		// 120;100		=> segment9 (out)
		iterator = this.segment9.getBeginPoint().toCounterclockwiseIterator(this.segment9, system);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());

		// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
		iterator = this.segment9.getEndPoint().toCounterclockwiseIterator(this.segment9, system);
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
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testRemoveRoadSegment() {
		// Remove the segments
		assertTrue(this.network.removeRoadSegment(this.segment4));
		assertTrue(this.network.removeRoadSegment(this.segment6));

		// Check the network segment list
		assertEquals(7, this.network.getSegmentCount());

		Collection<RoadSegmentStub> expectedList = new ArrayList<>();
		Collections.addAll(expectedList,
				this.segment1, this.segment2, this.segment3, this.segment5,
				this.segment7, this.segment8, this.segment9);

		for(RoadSegment segment : this.network) {
			assertTrue(expectedList.contains(segment));
			expectedList.remove(segment);
		}
		assertTrue(expectedList.isEmpty());

		Collections.addAll(expectedList,
				this.segment1, this.segment2, this.segment3, this.segment5,
				this.segment7, this.segment8, this.segment9);
		for(RoadSegment segment : this.network.getRoadSegments()) {
			assertTrue(expectedList.contains(segment));
			expectedList.remove(segment);
		}
		assertTrue(expectedList.isEmpty());

		// Check the network connections

		assertEquals(new Point2d(0.,0.), this.segment1.getFirstPoint());
		assertEquals(this.segment1.getLastPoint(),this.segment2.getLastPoint());

		assertEquals(this.segment2.getFirstPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment2.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(this.segment2.getLastPoint(),this.segment1.getLastPoint());

		assertEquals(this.segment3.getFirstPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment3.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(new Point2d(-100.,-300), this.segment3.getLastPoint());

		assertEquals(this.segment5.getFirstPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment5.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(new Point2d(0.,10.), this.segment5.getLastPoint());

		assertEquals(this.segment7.getFirstPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment8.getLastPoint());
		assertEquals(this.segment7.getFirstPoint(),this.segment9.getLastPoint());
		assertEquals(new Point2d(100.,-10.), this.segment7.getLastPoint());

		assertEquals(new Point2d(170.,7.), this.segment8.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment8.getLastPoint(),this.segment9.getLastPoint());

		assertEquals(new Point2d(120.,100.), this.segment9.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment2.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment3.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment5.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment7.getFirstPoint());
		assertEquals(this.segment9.getLastPoint(),this.segment8.getLastPoint());

		// Test invalid calls
		assertFalse(this.network.removeRoadSegment(null));
		assertFalse(this.network.removeRoadSegment(this.segment4));
	}

	@Test
	public void testConnectionIteratorAfterRemoval() {
		this.network.removeRoadSegment(this.segment4);
		this.network.removeRoadSegment(this.segment6);
		this.segment4 = this.segment6 = null;

		// CAUTION: Remember that segment4 and segment6 were removed!

		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		Iterator<RoadSegment> iterator = this.segment1.getBeginPoint().toCounterclockwiseIterator(this.segment1);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment1.getEndPoint().toCounterclockwiseIterator(this.segment1);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment2.getBeginPoint().toCounterclockwiseIterator(this.segment2);
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
		assertFalse(iterator.hasNext());

		iterator = this.segment2.getEndPoint().toCounterclockwiseIterator(this.segment2);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment3.getBeginPoint().toCounterclockwiseIterator(this.segment3);
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
		assertEquals(this.segment5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment3.getEndPoint().toCounterclockwiseIterator(this.segment3);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment5.getBeginPoint().toCounterclockwiseIterator(this.segment5);
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
		assertFalse(iterator.hasNext());

		iterator = this.segment5.getEndPoint().toCounterclockwiseIterator(this.segment5);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment7.getBeginPoint().toCounterclockwiseIterator(this.segment7);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
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
		assertFalse(iterator.hasNext());

		iterator = this.segment7.getEndPoint().toCounterclockwiseIterator(this.segment7);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment8.getBeginPoint().toCounterclockwiseIterator(this.segment8);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment8.getEndPoint().toCounterclockwiseIterator(this.segment8);
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
		assertFalse(iterator.hasNext());

		iterator = this.segment9.getBeginPoint().toCounterclockwiseIterator(this.segment9);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.segment9.getEndPoint().toCounterclockwiseIterator(this.segment9);
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
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetNearestConnection() {
		assertEquals(this.segment1.getBeginPoint(),
				this.network.getNearestConnection(
						new Point2d(-5.,0.)));
		assertEquals(this.segment9.getBeginPoint(),
				this.network.getNearestConnection(
						new Point2d(1000.,1000.)));
		assertEquals(this.segment4.getEndPoint(),
				this.network.getNearestConnection(
						new Point2d(0.,0.)));
	}

	@Test
	public void testGetNearestSegment() {
		assertEquals(this.segment4,
				this.network.getNearestSegment(
						new Point2d(-5.,0.)));
		assertEquals(this.segment6,
				this.network.getNearestSegment(
						new Point2d(1000.,1000.)));
		assertEquals(this.segment6,
				this.network.getNearestSegment(
						new Point2d(1000.,0.)));
		assertEquals(this.segment1,
				this.network.getNearestSegment(
						new Point2d(0.,1000.)));
		assertTrue(Arrays.asList(this.segment4, this.segment1).contains(this.network.getNearestSegment(
						new Point2d(0.,0.))));
	}

	@Test
	public void testGetNearestPosition() {
		Point1d p;

		p = this.network.getNearestPosition(new Point2d(-5.,0.));
		assertNotNull(p);
		assertSame(this.segment4, p.getSegment());
		assertEpsilonEquals(314.6466271867538, p.getCurvilineCoordinate());
		assertEpsilonEquals(0., p.getLateralDistance());

		p = this.network.getNearestPosition(new Point2d(1000.,1000.));
		assertNotNull(p);
		assertSame(this.segment6, p.getSegment());
		assertEpsilonEquals(304.5784, p.getCurvilineCoordinate());
		assertEpsilonEquals(0., p.getLateralDistance());

		p = this.network.getNearestPosition(new Point2d(1000.,0.));
		assertNotNull(p);
		assertSame(this.segment6, p.getSegment());
		assertEpsilonEquals(304.57839869, p.getCurvilineCoordinate());
		assertEpsilonEquals(0., p.getLateralDistance());
	}

	@Test
    public void testGetSegmentChainForwardBackward() {
    	List<RoadSegment> chain;

    	chain = this.segment1.getSegmentChain(true,true);
    	assertEquals(4, chain.size());
    	assertEquals(this.segment3, chain.get(0));
    	assertEquals(this.segment4, chain.get(1));
    	assertEquals(this.segment1, chain.get(2));
    	assertEquals(this.segment2, chain.get(3));

    	chain = this.segment2.getSegmentChain(true,true);
    	assertEquals(4, chain.size());
    	assertEquals(this.segment2, chain.get(0));
    	assertEquals(this.segment1, chain.get(1));
    	assertEquals(this.segment4, chain.get(2));
    	assertEquals(this.segment3, chain.get(3));

    	chain = this.segment3.getSegmentChain(true,true);
    	assertEquals(4, chain.size());
    	assertEquals(this.segment3, chain.get(0));
    	assertEquals(this.segment4, chain.get(1));
    	assertEquals(this.segment1, chain.get(2));
    	assertEquals(this.segment2, chain.get(3));

    	chain = this.segment4.getSegmentChain(true,true);
    	assertEquals(4, chain.size());
    	assertEquals(this.segment3, chain.get(0));
    	assertEquals(this.segment4, chain.get(1));
    	assertEquals(this.segment1, chain.get(2));
    	assertEquals(this.segment2, chain.get(3));

    	chain = this.segment5.getSegmentChain(true,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment5, chain.get(0));

    	chain = this.segment6.getSegmentChain(true,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment6, chain.get(0));

    	chain = this.segment7.getSegmentChain(true,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment7, chain.get(0));

    	chain = this.segment8.getSegmentChain(true,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment8, chain.get(0));

    	chain = this.segment9.getSegmentChain(true,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment9, chain.get(0));
    }

	@Test
    public void testGetSegmentChainForward() {
    	List<RoadSegment> chain;

    	chain = this.segment1.getSegmentChain(true,false);
    	assertEquals(2, chain.size());
    	assertEquals(this.segment1, chain.get(0));
    	assertEquals(this.segment2, chain.get(1));

    	chain = this.segment2.getSegmentChain(true,false);
    	assertEquals(4, chain.size());
    	assertEquals(this.segment2, chain.get(0));
    	assertEquals(this.segment1, chain.get(1));
    	assertEquals(this.segment4, chain.get(2));
    	assertEquals(this.segment3, chain.get(3));

    	chain = this.segment3.getSegmentChain(true,false);
    	assertEquals(4, chain.size());
    	assertEquals(this.segment3, chain.get(0));
    	assertEquals(this.segment4, chain.get(1));
    	assertEquals(this.segment1, chain.get(2));
    	assertEquals(this.segment2, chain.get(3));

    	chain = this.segment4.getSegmentChain(true,false);
    	assertEquals(3, chain.size());
    	assertEquals(this.segment4, chain.get(0));
    	assertEquals(this.segment1, chain.get(1));
    	assertEquals(this.segment2, chain.get(2));

    	chain = this.segment5.getSegmentChain(true,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment5, chain.get(0));

    	chain = this.segment6.getSegmentChain(true,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment6, chain.get(0));

    	chain = this.segment7.getSegmentChain(true,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment7, chain.get(0));

    	chain = this.segment8.getSegmentChain(true,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment8, chain.get(0));

    	chain = this.segment9.getSegmentChain(true,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment9, chain.get(0));
    }

	@Test
    public void testGetSegmentChainBackward() {
    	List<RoadSegment> chain;

    	chain = this.segment1.getSegmentChain(false,true);
    	assertEquals(3, chain.size());
    	assertEquals(this.segment3, chain.get(0));
    	assertEquals(this.segment4, chain.get(1));
    	assertEquals(this.segment1, chain.get(2));

    	chain = this.segment2.getSegmentChain(false,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment2, chain.get(0));

    	chain = this.segment3.getSegmentChain(false,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment3, chain.get(0));

    	chain = this.segment4.getSegmentChain(false,true);
    	assertEquals(2, chain.size());
    	assertEquals(this.segment3, chain.get(0));
    	assertEquals(this.segment4, chain.get(1));

    	chain = this.segment5.getSegmentChain(false,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment5, chain.get(0));

    	chain = this.segment6.getSegmentChain(false,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment6, chain.get(0));

    	chain = this.segment7.getSegmentChain(false,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment7, chain.get(0));

    	chain = this.segment8.getSegmentChain(false,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment8, chain.get(0));

    	chain = this.segment9.getSegmentChain(false,true);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment9, chain.get(0));
    }

	@Test
    public void testGetSegmentChainNoForwardNoBackward() {
    	List<RoadSegment> chain;

    	chain = this.segment1.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment1, chain.get(0));

    	chain = this.segment2.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment2, chain.get(0));

    	chain = this.segment3.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment3, chain.get(0));

    	chain = this.segment4.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment4, chain.get(0));

    	chain = this.segment5.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment5, chain.get(0));

    	chain = this.segment6.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment6, chain.get(0));

    	chain = this.segment7.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment7, chain.get(0));

    	chain = this.segment8.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment8, chain.get(0));

    	chain = this.segment9.getSegmentChain(false,false);
    	assertEquals(1, chain.size());
    	assertEquals(this.segment9, chain.get(0));
    }

	@Test
	public void testGetRoadSegmentString() {
		assertSame(this.segment1, this.network.getRoadSegment(this.segment1.getGeoId()));
		assertSame(this.segment9, this.network.getRoadSegment(this.segment9.getGeoId()));
		assertSame(this.segment6, this.network.getRoadSegment(this.segment6.getGeoId()));
	}

	@Test
	public void testGetRoadSegmentGeoLocation() {
		assertSame(this.segment1, this.network.getRoadSegment(this.segment1.getGeoLocation()));
		assertSame(this.segment9, this.network.getRoadSegment(this.segment9.getGeoLocation()));
		assertSame(this.segment6, this.network.getRoadSegment(this.segment6.getGeoLocation()));
	}

	@Test
	public void testIsLeftSidedCirculationDirection() {
		assertFalse(this.network.isLeftSidedTrafficDirection());
	}

	@Test
	public void testIsRightSidedCirculationDirection() {
		assertTrue(this.network.isRightSidedTrafficDirection());
	}

}

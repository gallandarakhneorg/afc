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

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.graph.GraphIterator;

/** Unit test for SubRoadNetwork as a SubGraph.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class SubRoadNetworkAsSubGraphTest extends AbstractGisTest {

	private StandardRoadNetwork network;
	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9, segment10;
	private RoadSegmentStub segment11;

	private SubRoadNetwork subNetwork;

	@Before
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
		try {
			Rectangle2d worldRect = new Rectangle2d(-500, -500, 1000, 1000);
			this.network = new StandardRoadNetwork(worldRect, new HeapAttributeCollection());

			//-------------------------------
			// PICTURE testnetwork1.png
			//-------------------------------

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

			this.segment10 = new RoadSegmentStub("segment10",-100.,0.,0.,10.); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment10);

			this.segment11 = new RoadSegmentStub("segment11",new double[] {0.,10.,-50.,100.,100.,500.,150.,100.,100.,7.}); //$NON-NLS-1$
			this.network.addRoadSegment(this.segment11);

			this.subNetwork = new SubRoadNetwork();
		}
		catch(RoadNetworkException e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void tearDown() {
		this.subNetwork = null;

		this.segment1.setStartPoint(null);
		this.segment1.setEndPoint(null);
		this.segment1.clear();
		this.segment1 = null;

		this.segment2.setStartPoint(null);
		this.segment2.setEndPoint(null);
		this.segment2.clear();
		this.segment2 = null;

		this.segment3.setStartPoint(null);
		this.segment3.setEndPoint(null);
		this.segment3.clear();
		this.segment3 = null;

		this.segment4.setStartPoint(null);
		this.segment4.setEndPoint(null);
		this.segment4.clear();
		this.segment4 = null;

		this.segment5.setStartPoint(null);
		this.segment5.setEndPoint(null);
		this.segment5.clear();
		this.segment5 = null;

		this.segment6.setStartPoint(null);
		this.segment6.setEndPoint(null);
		this.segment6.clear();
		this.segment6 = null;

		this.segment7.setStartPoint(null);
		this.segment7.setEndPoint(null);
		this.segment7.clear();
		this.segment7 = null;

		this.segment8.setStartPoint(null);
		this.segment8.setEndPoint(null);
		this.segment8.clear();
		this.segment8 = null;

		this.segment9.setStartPoint(null);
		this.segment9.setEndPoint(null);
		this.segment9.clear();
		this.segment9 = null;

		this.segment10.setStartPoint(null);
		this.segment10.setEndPoint(null);
		this.segment10.clear();
		this.segment10 = null;

		this.segment11.setStartPoint(null);
		this.segment11.setEndPoint(null);
		this.segment11.clear();
		this.segment11 = null;

		this.network = null;

		System.gc(); System.gc();
	}

	@Test
	public void testSize() {
		GraphIterator<RoadSegment,RoadConnection> iterator;

		iterator = this.network.depthIterator(
				this.segment3, 10., 50.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(iterator);
		assertEquals(1, this.subNetwork.getSegmentCount());

		iterator = this.network.depthIterator(
				this.segment3, 400., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(iterator);
		assertEquals(2, this.subNetwork.getSegmentCount());

		iterator = this.network.depthIterator(
				this.segment3, 690., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(iterator);
		assertEquals(3, this.subNetwork.getSegmentCount());

		iterator = this.network.depthIterator(
				this.segment3, 830, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(iterator);
		assertEquals(4, this.subNetwork.getSegmentCount());

		iterator = this.network.depthIterator(
				this.segment3, 920, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(iterator);
		assertEquals(11, this.subNetwork.getSegmentCount());
	}

	@Test
	public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(
				this.segment3, 1020, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.depthIterator(this.segment3, 50., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 400., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 690., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 830., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 920., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 1020., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment10, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_2() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(
				this.segment3, 1020, 10., this.segment3.getEndPoint(),
				false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.depthIterator(this.segment3, 50., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 400., 10.,
				this.segment3.getBeginPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testDepthIteratorRoadSegmentDoubleDoubleRoadConnection_3() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(
				this.segment3, 1020, 10.,
				this.segment3.getEndPoint(),false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.depthIterator(this.segment3, 50., 10.,
				this.segment3.getEndPoint(),false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 400., 10.,
				this.segment3.getEndPoint(),false,true);
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
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 690., 10.,
				this.segment3.getEndPoint(),false,true);
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
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment10, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testDepthIteratorRoadSegmentDoubleDouble() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(
				this.segment3, 1020, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.depthIterator(this.segment3, 50., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 400., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 690., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 830., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 920., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.subNetwork.depthIterator(this.segment3, 1020., 10.,
				this.segment3.getBeginPoint(),
				false,true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment10, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorRoadSegmentRoadConnectionBoolean_disableCycles_notOriented() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(
				this.segment3, 1020, 10.,
				this.segment3.getBeginPoint(),
				false,false);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.iterator(this.segment3, this.segment3.getBeginPoint(), false, false);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment10, iterator.next());

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorRoadSegmentRoadConnectionBoolean_disableCycles_oriented() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(
				this.segment3, 1020, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.iterator(this.segment3, this.segment3.getBeginPoint(), false, true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment10, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorRoadSegmentRoadConnectionBoolean_enableCycles_notOriented() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(this.segment3, 1020, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.iterator(
				this.segment3, this.segment3.getBeginPoint(),
				true, false);
		//3 4 1 2
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		//5
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		//3
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		//7 6
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		//8 6
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		//11
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next()); //
		//9 11
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next()); //
		//10 4
		assertTrue(iterator.hasNext());
		assertEquals(this.segment10, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		//11
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next()); //
		//9 2
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		//5
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next()); //
		//3
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		//7
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
	}

	@Test
	public void testIteratorRoadSegmentRoadConnectionBoolean_enableCycles_oriented() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		GraphIterator<RoadSegment,RoadConnection> iterator;

		mainIterator = this.network.depthIterator(this.segment3, 1020, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.iterator(this.segment3, this.segment3.getBeginPoint(),
				true, true);
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
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
		assertEquals(this.segment11, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment10, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next()); //
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
	}

	@Test
	public void testIteratorVoid() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> mainIterator;
		Iterator<RoadSegment> iterator;

		mainIterator = this.network.depthIterator(
				this.segment3, 920, 10.,
				this.segment3.getBeginPoint(),
				false,true);
		this.subNetwork.build(mainIterator);

		iterator = this.subNetwork.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment2, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment7, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment8, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment6, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment11, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(this.segment9, iterator.next());
		assertFalse(iterator.hasNext());
	}

}

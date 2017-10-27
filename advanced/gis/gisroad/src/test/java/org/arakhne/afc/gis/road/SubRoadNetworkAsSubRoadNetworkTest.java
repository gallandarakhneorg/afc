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
@SuppressWarnings("all")
public class SubRoadNetworkAsSubRoadNetworkTest extends AbstractGisTest {

	private static final String USER_DATA_TEST = "MY_USER_DATA_TEST"; //$NON-NLS-1$

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

			//----------------------------------------
			// Picture testnetwork1.png
			//----------------------------------------

			this.segment1 = new RoadSegmentStub("segment1",0.,0.,100.,100.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 1);
			this.network.addRoadSegment(this.segment1);

			this.segment2 = new RoadSegmentStub("segment2",100.,7.,100.,100.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 2);
			this.network.addRoadSegment(this.segment2);

			this.segment3 = new RoadSegmentStub("segment3",100.0001,7.,-100.,-300.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 3);
			this.network.addRoadSegment(this.segment3);

			this.segment4 = new RoadSegmentStub("segment4",-100.,-300.,0.,0.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 4);
			this.network.addRoadSegment(this.segment4);

			this.segment5 = new RoadSegmentStub("segment5",100.,7.,0.,10.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 5);
			this.network.addRoadSegment(this.segment5);

			this.segment6 = new RoadSegmentStub("segment6",new double[] {100.,7.,200.,-20.,400.,0.,150.,50.,100.,7.}); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 6);
			this.network.addRoadSegment(this.segment6);

			this.segment7 = new RoadSegmentStub("segment7",100.,7.,100.,-10.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 7);
			this.network.addRoadSegment(this.segment7);

			this.segment8 = new RoadSegmentStub("segment8",170.,7.,100.,7.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 8);
			this.network.addRoadSegment(this.segment8);

			this.segment9 = new RoadSegmentStub("segment9",120.,100.,100.,7.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 9);
			this.network.addRoadSegment(this.segment9);

			this.segment10 = new RoadSegmentStub("segment10",-100.,0.,0.,10.); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 10);
			this.network.addRoadSegment(this.segment10);

			this.segment11 = new RoadSegmentStub("segment11",new double[] {0.,10.,-50.,100.,100.,500.,150.,100.,100.,7.}); //$NON-NLS-1$
			this.segment1.addUserData(USER_DATA_TEST, 11);
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

	private static void assertShadowSegment(RoadSegment shadowed, RoadSegment shadow) {
		assertNotNull("Shadow segment is null", shadow); //$NON-NLS-1$
		assertNotSame("Shadow and real segments are same instance", shadowed, shadow); //$NON-NLS-1$
		assertEquals("Shadow and real segments are not equal", shadowed, shadow); //$NON-NLS-1$
		// User data should be inaccessible
		assertNull("User date may be inaccessible", shadow.getUserData(USER_DATA_TEST)); //$NON-NLS-1$
		shadow.addUserData(USER_DATA_TEST, 123);
		assertNull("User date may be inaccessible", shadow.getUserData(USER_DATA_TEST)); //$NON-NLS-1$
	}

	private static void assertShadowConnection(RoadConnection shadowed, RoadConnection shadow, RoadSegment connectedSegment) {
		assertNotNull("Shadow connection must not be null", shadow); //$NON-NLS-1$
		assertNotSame("Shadow and real connections are same instance", shadowed, shadow); //$NON-NLS-1$
		assertEquals("Shadow and real connections are not equal", shadowed, shadow); //$NON-NLS-1$

		assertTrue(
				"Real connection is not of valid type", //$NON-NLS-1$
				shadowed instanceof RoadConnectionWithArrivalSegment || shadowed instanceof StandardRoadConnection);
		assertFalse(
				"Shadow connection is of invalid type, expecting different than: " //$NON-NLS-1$
				+RoadConnectionWithArrivalSegment.class+"; actual: " //$NON-NLS-1$
				+shadow.getClass(),
				shadow instanceof StandardRoadConnection);
		assertFalse(
				"Shadow connection is of invalid type, expecting different than: " //$NON-NLS-1$
				+RoadConnectionWithArrivalSegment.class+"; actual: " //$NON-NLS-1$
				+shadow.getClass(),
				shadow instanceof RoadConnectionWithArrivalSegment);

		assertEquals(
				"Shadow connection has not exactly one connected segment", //$NON-NLS-1$
				1, shadow.getConnectedSegmentCount());
		assertEquals(
				"Unexpected connected segment", //$NON-NLS-1$
				connectedSegment, shadow.getConnectedSegment(0));
	}

	private static void assertRealConnection(RoadConnection shadowed, RoadConnection shadow) {
		assertNotNull("Shadow connection must not be null", shadow); //$NON-NLS-1$
		assertNotSame("Shadow connection must not be the connection itself", shadowed, shadow); //$NON-NLS-1$
		assertEquals("Shadown and real connections are not equal", shadowed, shadow); //$NON-NLS-1$

		assertTrue(
				"Real connection is not of valid type, expecting:" //$NON-NLS-1$
				+"TaggedRoadConnection or StandardRoadConnection; actual: " //$NON-NLS-1$
				+shadow.getClass(),
				shadowed instanceof RoadConnectionWithArrivalSegment || shadowed instanceof StandardRoadConnection);
		assertTrue(
				"Shadow connection is not of valid type, expecting:" //$NON-NLS-1$
				+"TaggedRoadConnection or StandardRoadConnection; actual: " //$NON-NLS-1$
				+shadow.getClass(),
				shadow instanceof RoadConnectionWithArrivalSegment || shadow instanceof StandardRoadConnection);

		assertEquals(
				"Not same connected segment count, expecting:" //$NON-NLS-1$
				+shadowed.getConnectedSegmentCount()+"; actual: " //$NON-NLS-1$
				+shadow.getConnectedSegmentCount(),
				shadowed.getConnectedSegmentCount(), shadow.getConnectedSegmentCount());
	}

	/** Assert the given shadow segment is binded to the given shadowed segment
	 * and assuming that the last end is not terminal and the start end
	 * is terminal.
	 */
	private static void assertShadowSegmentEO(RoadSegment shadowed, RoadSegment shadow) {
		assertShadowSegment(shadowed, shadow);
		assertShadowConnection(shadowed.getBeginPoint(), shadow.getBeginPoint(), shadow);
		assertRealConnection(shadowed.getEndPoint(), shadow.getEndPoint());
	}

	/** Assert the given shadow segment is binded to the given shadowed segment
	 * and assuming that the start end is not terminal and the last end
	 * is terminal.
	 */
	private static void assertShadowSegmentOE(RoadSegment shadowed, RoadSegment shadow) {
		assertShadowSegment(shadowed, shadow);
		assertRealConnection(shadowed.getBeginPoint(), shadow.getBeginPoint());
		assertShadowConnection(shadowed.getEndPoint(), shadow.getEndPoint(), shadow);
	}

	/** Assert the given shadow segment is binded to the given shadowed segment
	 * and assuming that the ends of the segments are terminal ends.
	 */
	private static void assertShadowSegmentEE(RoadSegment shadowed, RoadSegment shadow) {
		assertShadowSegment(shadowed, shadow);
		assertShadowConnection(shadowed.getBeginPoint(), shadow.getBeginPoint(), shadow);
		assertShadowConnection(shadowed.getEndPoint(), shadow.getEndPoint(), shadow);
	}

	/** Assert the given shadow segment is binded to the given shadowed segment
	 * and assuming that the ends of the segments are not terminal ends.
	 */
	private static void assertShadowSegmentOO(RoadSegment shadowed, RoadSegment shadow) {
		assertShadowSegment(shadowed, shadow);
		assertRealConnection(shadowed.getBeginPoint(), shadow.getBeginPoint());
		assertRealConnection(shadowed.getEndPoint(), shadow.getEndPoint());
	}

	@Test
	public void testIteratorWithWrappingTest() {
		assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());

		GraphIterator<RoadSegment,RoadConnection> iterator;
		Iterator<RoadSegment> iterator2;

		iterator = this.network.depthIterator(this.segment3, 50., 10.,
				this.segment3.getBeginPoint(),false,true);
		this.subNetwork.build(iterator);
		iterator2 = this.subNetwork.iterator();
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEE(this.segment3, iterator2.next());
		assertFalse(iterator2.hasNext());

		iterator = this.network.depthIterator(this.segment3, 400., 10.,
				this.segment3.getBeginPoint(),false,true);
		this.subNetwork.build(iterator);
		iterator2 = this.subNetwork.iterator();
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment3, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOE(this.segment4, iterator2.next());
		assertFalse(iterator2.hasNext());

		iterator = this.network.depthIterator(this.segment3, 690., 10.,
				this.segment3.getBeginPoint(),false,true);
		this.subNetwork.build(iterator);
		iterator2 = this.subNetwork.iterator();
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment3, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment4, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOE(this.segment1, iterator2.next());
		assertFalse(iterator2.hasNext());

		iterator = this.network.depthIterator(this.segment3, 830, 10.,
				this.segment3.getBeginPoint(),false,true);
		this.subNetwork.build(iterator);
		iterator2 = this.subNetwork.iterator();
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment3, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment4, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment1, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment2, iterator2.next());
		assertFalse(iterator2.hasNext());

		iterator = this.network.depthIterator(this.segment3, 920, 10.,
				this.segment3.getBeginPoint(),false,true);
		this.subNetwork.build(iterator);
		iterator2 = this.subNetwork.iterator();
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment3, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment4, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment1, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment2, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOE(this.segment5, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOE(this.segment7, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment6, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment8, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment6, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment11, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment9, iterator2.next());
		assertFalse(iterator2.hasNext());

		iterator = this.network.depthIterator(this.segment3, 1020, 10.,
				this.segment3.getBeginPoint(),false,true);
		this.subNetwork.build(iterator);
		iterator2 = this.subNetwork.iterator();
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment3, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment4, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment1, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment2, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment5, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOE(this.segment7, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment6, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment8, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment6, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentOO(this.segment11, iterator2.next());
		assertTrue(iterator2.hasNext());
		assertShadowSegmentEO(this.segment9, iterator2.next());
	}

}

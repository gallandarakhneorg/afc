/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.road.AbstractGisTest;
import org.arakhne.afc.math.geometry.d1.Direction1D;

/** Unit test for RoadPath.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
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
		this.connection1 = new RoadConnectionStub("connection1(1,4)", 0., 0.); //$NON-NLS-1$
		this.connection2 = new RoadConnectionStub("connection2(1,2)",100.,100.); //$NON-NLS-1$
		this.connection3 = new RoadConnectionStub("connection3(2,3,5,6,6,7,8,9,11)",100.,7.); //$NON-NLS-1$
		this.connection4 = new RoadConnectionStub("connection4(3,4)",-100.,-300); //$NON-NLS-1$
		this.connection5 = new RoadConnectionStub("connection5(5,10,11)",0.,10.); //$NON-NLS-1$
		this.connection6 = new RoadConnectionStub("connection6(7)",100.,-10.); //$NON-NLS-1$
		this.connection7 = new RoadConnectionStub("connection7(8)",170.,7.); //$NON-NLS-1$
		this.connection8 = new RoadConnectionStub("connection8(9)",120.,100.); //$NON-NLS-1$
		this.connection9 = new RoadConnectionStub("connection9(10)",-100.,0.); //$NON-NLS-1$

		this.segment1 = new RoadSegmentStub("segment1",0.,0.,100.,100.); //$NON-NLS-1$
		this.connection1.addSegment(this.segment1);
		this.connection2.addSegment(this.segment1);

		this.segment2 = new RoadSegmentStub("segment2",100.,7.,100.,100.); //$NON-NLS-1$
		this.connection3.addSegment(this.segment2);
		this.connection2.addSegment(this.segment2);

		this.segment3 = new RoadSegmentStub("segment3",100.0001,7.,-100.,-300.); //$NON-NLS-1$
		this.connection3.addSegment(this.segment3);
		this.connection4.addSegment(this.segment3);

		this.segment4 = new RoadSegmentStub("segment4",-100.,-300.,0.,0.); //$NON-NLS-1$
		this.connection4.addSegment(this.segment4);
		this.connection1.addSegment(this.segment4);

		this.segment5 = new RoadSegmentStub("segment5",100.,7.,0.,10.); //$NON-NLS-1$
		this.connection3.addSegment(this.segment5);
		this.connection5.addSegment(this.segment5);

		this.segment6 = new RoadSegmentStub("segment6", 100.,7.,200.,-20.,400.,0.,150.,50.,100.,7.); //$NON-NLS-1$
		this.connection3.addSegment(this.segment6);
		this.connection3.addSegment(this.segment6);

		this.segment7 = new RoadSegmentStub("segment7",100.,7.,100.,-10.); //$NON-NLS-1$
		this.connection3.addSegment(this.segment7);
		this.connection6.addSegment(this.segment7);

		this.segment8 = new RoadSegmentStub("segment8",170.,7.,100.,7.); //$NON-NLS-1$
		this.connection7.addSegment(this.segment8);
		this.connection3.addSegment(this.segment8);

		this.segment9 = new RoadSegmentStub("segment9",120.,100.,100.,7.); //$NON-NLS-1$
		this.connection8.addSegment(this.segment9);
		this.connection3.addSegment(this.segment9);

		this.segment10 = new RoadSegmentStub("segment10",-100.,0.,0.,10.); //$NON-NLS-1$
		this.connection9.addSegment(this.segment10);
		this.connection5.addSegment(this.segment10);

		this.segment11 = new RoadSegmentStub("segment11",0.,10.,-50.,100.,100.,500.,150.,100.,100.,7.); //$NON-NLS-1$
		this.connection5.addSegment(this.segment11);
		this.connection3.addSegment(this.segment11);
	}

	@AfterEach
	public void tearDown() {
		this.segment1 = this.segment2 = this.segment3 = this.segment4 = this.segment5 = null;
		this.segment6 = this.segment7 = this.segment8 = this.segment9 = null;
		this.connection1 = this.connection2 = this.connection3 = null;
		this.connection4 = this.connection5 = this.connection6 = null;
		this.connection7 = this.connection8 = this.connection9 = null;
	}

	@Test
	public void testRoadPathVoid() {
		RoadPath path = new RoadPath();
		assertTrue(path.isEmpty());
	}

	@Test
	public void testRoadPathRoadSegmentRoadConnection() {
		RoadPath path = new RoadPath(this.segment1, this.segment1.getBeginPoint());
		assertFalse(path.isEmpty());
		assertEquals(1, path.size());
		assertSame(this.segment1, path.get(0));
	}

	@Test
	public void testGetFirstCrossRoad() {
		RoadPath path = new RoadPath();
		path.add(this.segment3, this.segment3.getBeginPoint());
		path.add(this.segment4);
		path.add(this.segment1);
		path.add(this.segment2);
		path.add(this.segment6, this.segment6.getEndPoint());
		path.add(this.segment8);

		assertFalse(path.isEmpty());
		assertEquals(6, path.size());

		assertEquals(this.segment2.getBeginPoint(), path.getFirstCrossRoad());
	}

	@Test
	public void testGetFirstJunctionPoint() {
		RoadPath path = new RoadPath();
		path.add(this.segment3, this.segment3.getBeginPoint());
		path.add(this.segment4);
		path.add(this.segment1);
		path.add(this.segment2);
		path.add(this.segment6, this.segment6.getEndPoint());
		path.add(this.segment8);

		CrossRoad cross = path.getFirstJunctionPoint();

		double distance = this.segment3.getLength()
							+this.segment4.getLength()
							+this.segment1.getLength()
							+this.segment2.getLength();

		assertEquals(this.segment2.getBeginPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(distance, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment2, cross.enteringSegment);
		assertSame(this.segment6, cross.exitingSegment);
	}

	@Test
	public void testCrossRoadsVoid_thru_segment6() {
		RoadPath path = new RoadPath();
		path.add(this.segment3, this.segment3.getBeginPoint());
		path.add(this.segment4);
		path.add(this.segment1);
		path.add(this.segment2);
		path.add(this.segment6, this.segment6.getEndPoint());
		path.add(this.segment8);

		CrossRoad cross;

		double distance = this.segment3.getLength()
							+this.segment4.getLength()
							+this.segment1.getLength()
							+this.segment2.getLength();

		Iterator<CrossRoad> iterator = path.crossRoads();

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment2.getBeginPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(distance, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment2, cross.enteringSegment);
		assertSame(this.segment6, cross.exitingSegment);

		distance += this.segment6.getLength();

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment6.getEndPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(this.segment6.getLength(), cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment6, cross.enteringSegment);
		assertSame(this.segment8, cross.exitingSegment);

		distance += this.segment8.getLength();

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment8.getBeginPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(this.segment8.getLength(), cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment8, cross.enteringSegment);
		assertNull(cross.exitingSegment);

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testCrossRoadsVoid_not_thru_segment6() {
		RoadPath path = new RoadPath();
		path.add(this.segment3, this.segment3.getBeginPoint());
		path.add(this.segment4);
		path.add(this.segment1);
		path.add(this.segment2);
		path.add(this.segment3);
		path.add(this.segment4);
		path.add(this.segment1);
		path.add(this.segment2);
		path.add(this.segment7);

		CrossRoad cross;

		double distance = this.segment3.getLength()
							+this.segment4.getLength()
							+this.segment1.getLength()
							+this.segment2.getLength();

		Iterator<CrossRoad> iterator = path.crossRoads();

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment2.getBeginPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(distance, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment2, cross.enteringSegment);
		assertSame(this.segment3, cross.exitingSegment);

		double deltaD = this.segment3.getLength()
						+this.segment4.getLength()
						+this.segment1.getLength()
						+this.segment2.getLength();
		distance += deltaD;

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment2.getBeginPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment2, cross.enteringSegment);
		assertSame(this.segment7, cross.exitingSegment);

		deltaD = this.segment7.getLength();
		distance += deltaD;

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment7.getEndPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment7, cross.enteringSegment);
		assertNull(cross.exitingSegment);

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testCrossRoadsVoid_thru_segment11_with_culdesac() {
		RoadPath path = new RoadPath();
		path.add(this.segment5, this.segment5.getBeginPoint());
		path.add(this.segment10);
		path.add(this.segment10);
		path.add(this.segment11);

		CrossRoad cross;

		double deltaD = this.segment5.getLength();
		double distance = deltaD;

		Iterator<CrossRoad> iterator = path.crossRoads();

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment5.getEndPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment5, cross.enteringSegment);
		assertSame(this.segment10, cross.exitingSegment);

		deltaD = this.segment10.getLength();
		distance += deltaD;

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment10.getBeginPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment10, cross.enteringSegment);
		assertSame(this.segment10, cross.exitingSegment);

		deltaD = this.segment10.getLength();
		distance += deltaD;

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment10.getEndPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment10, cross.enteringSegment);
		assertSame(this.segment11, cross.exitingSegment);

		deltaD = this.segment11.getLength();
		distance += deltaD;

		assertTrue(iterator.hasNext());
		cross = iterator.next();
		assertNotNull(cross);
		assertEquals(this.segment11.getEndPoint(), cross.connectionPoint);
		assertEpsilonEquals(distance, cross.distance);
		assertEpsilonEquals(deltaD, cross.distanceFromPreviousCrossRoad);
		assertSame(this.segment11, cross.enteringSegment);
		assertNull(cross.exitingSegment);

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetSegmentDirectionAtInt() {
		RoadPath path = new RoadPath();
		path.add(this.segment3, this.segment3.getBeginPoint()); // start -> end
		path.add(this.segment4); // start -> end
		path.add(this.segment1); // start -> end
		path.add(this.segment2); // end -> start
		path.add(this.segment6, this.segment6.getEndPoint()); // end -> start but also start -> end
		path.add(this.segment8); // end -> start

		assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(0));
		assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(1));
		assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(2));
		assertEquals(Direction1D.REVERTED_DIRECTION, path.getSegmentDirectionAt(3));
		assertEquals(Direction1D.SEGMENT_DIRECTION, path.getSegmentDirectionAt(4));
		assertEquals(Direction1D.REVERTED_DIRECTION, path.getSegmentDirectionAt(5));
	}

}

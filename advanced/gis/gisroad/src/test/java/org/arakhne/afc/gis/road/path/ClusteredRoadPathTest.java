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

package org.arakhne.afc.gis.road.path;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.road.AbstractGisTest;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d1.Direction1D;

/** Unit test for ClusteredRoadPath.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ClusteredRoadPathTest extends AbstractGisTest {

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
	private RoadPath path1;
	private RoadPath path2;
	private RoadPath path3;
	private ClusteredRoadPath cPath;

	@Before
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

		this.path1 = new RoadPath();
		this.path1.add(this.segment1); // end -> start
		this.path1.add(this.segment4); // end -> start
		this.path1.add(this.segment3); // end -> start
		this.path1.add(this.segment5); // start -> end

		this.path2 = new RoadPath();
		this.path2.add(this.segment9); // start -> end
		this.path2.add(this.segment6); // start -> end

		this.path3 = new RoadPath();
		this.path3.add(this.segment11); // start -> end
		this.path3.add(this.segment3); // start -> end
		this.path3.add(this.segment4); // start -> end
		this.path3.add(this.segment1); // start -> end
		this.path3.add(this.segment2); // end -> start

		this.cPath = new ClusteredRoadPath();
	}

	@After
	public void tearDown() {
		this.cPath = null;
		this.path1 = this.path2 = this.path3 = null;
		this.segment1 = this.segment2 = this.segment3 = this.segment4 = this.segment5 = null;
		this.segment6 = this.segment7 = this.segment8 = this.segment9 = null;
		this.connection1 = this.connection2 = this.connection3 = null;
		this.connection4 = this.connection5 = this.connection6 = null;
		this.connection7 = this.connection8 = this.connection9 = null;
	}

	@Test
	public void testGetRoadPathAt() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);
		this.cPath.add(this.path3);

		assertSame(this.path1, this.cPath.getRoadPathAt(0));
		try {
			this.cPath.getRoadPathAt(1);
			fail("expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}
		try {
			this.cPath.getRoadPathAt(2);
			fail("expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}

		this.cPath.clear();

		this.cPath.add(this.path1);
		this.cPath.add(this.path3);

		assertSame(this.path1, this.cPath.getRoadPathAt(0));
		assertSame(this.path3, this.cPath.getRoadPathAt(1));
		try {
			this.cPath.getRoadPathAt(2);
			fail("expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException exception) {
			// expected exception
		}
	}

	@Test
	public void testAddRoadPath_withHole() {
		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.add(this.path2);
		assertEquals(2, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertTrue(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.add(this.path3);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));
	}

	@Test
	public void testAddRoadPath_withoutHole() {
		// path1 = [ s1, s4, s3, s5 ]
		// path2 = [ s9, s6 ]
		// path3 = [ s11, s3, s4, s1, s2 ]
		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.add(this.path3);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.add(this.path2);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));
	}

	@Test
	public void testClear() {
		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.clear();
		assertEquals(0, this.cPath.size());
		assertFalse(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));
	}

	@Test
	public void testContainsRoadSegment() {
		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		assertTrue(this.cPath.contains(this.segment1));
		assertFalse(this.cPath.contains(this.segment2));
		assertTrue(this.cPath.contains(this.segment3));
		assertTrue(this.cPath.contains(this.segment4));
		assertTrue(this.cPath.contains(this.segment5));
		assertFalse(this.cPath.contains(this.segment6));
		assertFalse(this.cPath.contains(this.segment7));
		assertFalse(this.cPath.contains(this.segment8));
		assertFalse(this.cPath.contains(this.segment9));
		assertFalse(this.cPath.contains(this.segment10));
		assertFalse(this.cPath.contains(this.segment11));
	}

	@Test
	public void testContainsRoadConnection() {
		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		assertFalse(this.cPath.contains(this.connection1));
		assertTrue(this.cPath.contains(this.connection2));
		assertFalse(this.cPath.contains(this.connection3));
		assertFalse(this.cPath.contains(this.connection4));
		assertTrue(this.cPath.contains(this.connection5));
		assertFalse(this.cPath.contains(this.connection6));
		assertFalse(this.cPath.contains(this.connection7));
		assertFalse(this.cPath.contains(this.connection8));
		assertFalse(this.cPath.contains(this.connection9));
	}

	@Test
	public void testIterator() {
		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.add(this.path2);
		assertEquals(2, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertTrue(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		Iterator<RoadPath> iterator = this.cPath.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.path1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.path2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testRemoveRoadPath() {
		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.add(this.path2);
		assertEquals(2, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertTrue(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.remove(this.path3);
		assertEquals(2, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertTrue(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.remove(this.path1);
		assertEquals(1, this.cPath.size());
		assertFalse(this.cPath.contains(this.path1));
		assertTrue(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		this.cPath.remove(this.path2);
		assertEquals(0, this.cPath.size());
		assertFalse(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));
	}

	@Test
	public void testToRoadPath() {
		assertNull(this.cPath.toRoadPath());

		this.cPath.add(this.path1);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		assertSame(this.path1, this.cPath.toRoadPath());

		this.cPath.add(this.path2);
		assertEquals(2, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertTrue(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		assertNull(this.cPath.toRoadPath());

		this.cPath.add(this.path3);
		assertEquals(1, this.cPath.size());
		assertTrue(this.cPath.contains(this.path1));
		assertFalse(this.cPath.contains(this.path2));
		assertFalse(this.cPath.contains(this.path3));

		assertSame(this.path1, this.cPath.toRoadPath());
	}

	@Test
	public void testRoadSegments() {
		// path1 = [ s1, s4, s3, s5 ]
		// path2 = [ s9, s6 ]
		// path3 = [ s11, s3, s4, s1, s2 ]
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);

		Iterator<RoadSegment> iterator = this.cPath.roadSegments();
		assertTrue(iterator.hasNext());
		assertSame(this.segment1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.segment4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.segment3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.segment5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.segment9, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.segment6, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testInvert_withHole() {
		RoadPath inverted1 = new RoadPath(this.path1);
		inverted1.invert();
		RoadPath inverted2 = new RoadPath(this.path2);
		inverted2.invert();

		this.cPath.add(this.path1);
		this.cPath.add(this.path2);

		this.cPath.invert();

		RoadPath p;

		p = this.cPath.first();
		assertEquals(inverted1.size(), p.size());
		for(int i=0; i<inverted1.size(); ++i) {
			assertEquals(inverted1.get(i), p.get(i));
		}
		p = this.cPath.last();
		assertEquals(inverted2.size(), p.size());
		for(int i=0; i<inverted2.size(); ++i) {
			assertEquals(inverted2.get(i), p.get(i));
		}
	}

	@Test
	public void testInvert_withoutHole() {
		RoadPath inverted = new RoadPath(this.path1);
		inverted.addAll(this.path3);
		inverted.invert();

		this.cPath.add(this.path1);
		this.cPath.add(this.path3);

		this.cPath.invert();

		RoadPath p;

		p = this.cPath.first();
		assertEquals(inverted.size(), p.size());
		for(int i=0; i<inverted.size(); ++i) {
			assertEquals(Integer.toString(i), inverted.get(i), p.get(i));
		}
	}

	@Test
	public void testGetRoadSegmentCount_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);
		assertEquals(6, this.cPath.getRoadSegmentCount());
	}

	@Test
	public void testGetRoadSegmentCount_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);
		assertEquals(9, this.cPath.getRoadSegmentCount());
	}

	@Test
	public void testIndexOfRoadSegment_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);
		assertEquals(0, this.cPath.indexOf(this.segment1));
		assertEquals(-1, this.cPath.indexOf(this.segment2));
		assertEquals(2, this.cPath.indexOf(this.segment3));
		assertEquals(1, this.cPath.indexOf(this.segment4));
		assertEquals(3, this.cPath.indexOf(this.segment5));
		assertEquals(5, this.cPath.indexOf(this.segment6));
		assertEquals(-1, this.cPath.indexOf(this.segment7));
		assertEquals(-1, this.cPath.indexOf(this.segment8));
		assertEquals(4, this.cPath.indexOf(this.segment9));
		assertEquals(-1, this.cPath.indexOf(this.segment10));
		assertEquals(-1, this.cPath.indexOf(this.segment11));
	}

	@Test
	public void testIndexOfRoadSegment_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);
		assertEquals(0, this.cPath.indexOf(this.segment1));
		assertEquals(8, this.cPath.indexOf(this.segment2));
		assertEquals(2, this.cPath.indexOf(this.segment3));
		assertEquals(1, this.cPath.indexOf(this.segment4));
		assertEquals(3, this.cPath.indexOf(this.segment5));
		assertEquals(-1, this.cPath.indexOf(this.segment6));
		assertEquals(-1, this.cPath.indexOf(this.segment7));
		assertEquals(-1, this.cPath.indexOf(this.segment8));
		assertEquals(-1, this.cPath.indexOf(this.segment9));
		assertEquals(-1, this.cPath.indexOf(this.segment10));
		assertEquals(4, this.cPath.indexOf(this.segment11));
	}

	@Test
	public void testLastIndexOfRoadSegment_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);
		assertEquals(0, this.cPath.lastIndexOf(this.segment1));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment2));
		assertEquals(2, this.cPath.lastIndexOf(this.segment3));
		assertEquals(1, this.cPath.lastIndexOf(this.segment4));
		assertEquals(3, this.cPath.lastIndexOf(this.segment5));
		assertEquals(5, this.cPath.lastIndexOf(this.segment6));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment7));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment8));
		assertEquals(4, this.cPath.lastIndexOf(this.segment9));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment10));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment11));
	}

	@Test
	public void testLastIndexOfRoadSegment_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);
		assertEquals(7, this.cPath.lastIndexOf(this.segment1));
		assertEquals(8, this.cPath.lastIndexOf(this.segment2));
		assertEquals(5, this.cPath.lastIndexOf(this.segment3));
		assertEquals(6, this.cPath.lastIndexOf(this.segment4));
		assertEquals(3, this.cPath.lastIndexOf(this.segment5));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment6));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment7));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment8));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment9));
		assertEquals(-1, this.cPath.lastIndexOf(this.segment10));
		assertEquals(4, this.cPath.lastIndexOf(this.segment11));
	}

	@Test
	public void testGetRoadSegmentAtInt_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);
		assertEquals(this.segment1, this.cPath.getRoadSegmentAt(0));
		assertEquals(this.segment4, this.cPath.getRoadSegmentAt(1));
		assertEquals(this.segment3, this.cPath.getRoadSegmentAt(2));
		assertEquals(this.segment5, this.cPath.getRoadSegmentAt(3));
		assertEquals(this.segment9, this.cPath.getRoadSegmentAt(4));
		assertEquals(this.segment6, this.cPath.getRoadSegmentAt(5));
	}

	@Test
	public void testGetRoadSegmentAtInt_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);
		assertEquals(this.segment1, this.cPath.getRoadSegmentAt(0));
		assertEquals(this.segment4, this.cPath.getRoadSegmentAt(1));
		assertEquals(this.segment3, this.cPath.getRoadSegmentAt(2));
		assertEquals(this.segment5, this.cPath.getRoadSegmentAt(3));
		assertEquals(this.segment11, this.cPath.getRoadSegmentAt(4));
		assertEquals(this.segment3, this.cPath.getRoadSegmentAt(5));
		assertEquals(this.segment4, this.cPath.getRoadSegmentAt(6));
		assertEquals(this.segment1, this.cPath.getRoadSegmentAt(7));
		assertEquals(this.segment2, this.cPath.getRoadSegmentAt(8));
	}

	@Test
	public void testFirst_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);
		assertEquals(this.path1, this.cPath.first());
	}

	@Test
	public void testFirst_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);
		assertEquals(this.path1, this.cPath.first());
	}

	@Test
	public void testLast_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);
		assertEquals(this.path2, this.cPath.last());
	}

	@Test
	public void testLast_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);
		assertEquals(this.path1, this.cPath.last());
	}

	@Test
	public void testRemoveRoadSegmentAtInt_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);

		RoadSegment s = this.cPath.removeRoadSegmentAt(2);
		assertSame(this.segment3, s);
		assertEquals(2, this.cPath.size());

		Iterator<RoadSegment> iterator = this.cPath.roadSegments();
		assertTrue(iterator.hasNext());
		s = iterator.next();
		if (this.segment1==s) {
			assertSame(this.segment1, s);
			assertTrue(iterator.hasNext());
			assertSame(this.segment4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment11, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment2, iterator.next());
			assertFalse(iterator.hasNext());
		}
		else {
			assertSame(this.segment5, s);
			assertTrue(iterator.hasNext());
			assertSame(this.segment11, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(this.segment4, iterator.next());
			assertFalse(iterator.hasNext());
		}
	}

	@Test
	public void testGetPathForRoadSegmentAtInt_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);

		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(0));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(1));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(2));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(3));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(4));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(5));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(6));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(7));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(8));
	}

	@Test
	public void testGetPathForRoadSegmentAtInt_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);

		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(0));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(1));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(2));
		assertSame(this.path1, this.cPath.getPathForRoadSegmentAt(3));
		assertSame(this.path2, this.cPath.getPathForRoadSegmentAt(4));
		assertSame(this.path2, this.cPath.getPathForRoadSegmentAt(5));
	}

	@Test
	public void testGetRoadSegmentDirectionAtInt_withoutHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);

		assertEquals(Direction1D.REVERTED_DIRECTION, this.cPath.getRoadSegmentDirectionAt(0));
		assertEquals(Direction1D.REVERTED_DIRECTION, this.cPath.getRoadSegmentDirectionAt(1));
		assertEquals(Direction1D.REVERTED_DIRECTION, this.cPath.getRoadSegmentDirectionAt(2));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(3));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(4));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(5));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(6));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(7));
		assertEquals(Direction1D.REVERTED_DIRECTION, this.cPath.getRoadSegmentDirectionAt(8));
	}

	@Test
	public void testGetRoadSegmentDirectionAtInt_withHole() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path2);

		assertEquals(Direction1D.REVERTED_DIRECTION, this.cPath.getRoadSegmentDirectionAt(0));
		assertEquals(Direction1D.REVERTED_DIRECTION, this.cPath.getRoadSegmentDirectionAt(1));
		assertEquals(Direction1D.REVERTED_DIRECTION, this.cPath.getRoadSegmentDirectionAt(2));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(3));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(4));
		assertEquals(Direction1D.SEGMENT_DIRECTION, this.cPath.getRoadSegmentDirectionAt(5));
	}

	@Test
	public void testGetLength_withoutHole() {
		double lengthPath1 = this.path1.getLength();
		double lengthPath3 = this.path3.getLength();

		this.cPath.add(this.path1);
		assertEpsilonEquals(lengthPath1, this.cPath.getLength());

		this.cPath.add(this.path3);
		assertEpsilonEquals(lengthPath1+lengthPath3, this.cPath.getLength());
	}

	@Test
	public void testGetLength_withHole() {
		double lengthPath1 = this.path1.getLength();
		double lengthPath2 = this.path2.getLength();

		this.cPath.add(this.path1);
		assertEpsilonEquals(lengthPath1, this.cPath.getLength());

		this.cPath.add(this.path2);
		assertEpsilonEquals(lengthPath1+lengthPath2, this.cPath.getLength());
	}

	@Test
	public void testIteratorRemove() {
		this.cPath.add(this.path1);
		this.cPath.add(this.path3);
		assertEquals(1, this.cPath.size());

		RoadSegment s;

		Iterator<RoadSegment> iterator = this.cPath.roadSegments();

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment1, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment4, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment3, s);

		iterator.remove();

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment5, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment11, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment3, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment4, s);

		iterator.remove();

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment1, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment2, s);

		assertFalse(iterator.hasNext());

		assertEquals(3, this.cPath.size());

		iterator = this.cPath.roadSegments();

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment1, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment4, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment5, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment11, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment3, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment1, s);

		assertTrue(iterator.hasNext());
		s = iterator.next();
		assertNotNull(s);
		assertSame(this.segment2, s);

		assertFalse(iterator.hasNext());
	}

}

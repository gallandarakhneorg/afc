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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.road.AbstractGisTest;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.base.d1.Direction1D;

/** Unit test for ClusteredRoadPath.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ClusteredRoadPath")
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

	protected void initRoadNetwork() {
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

		path1 = new RoadPath();
		path1.add(segment1); // end -> start
		path1.add(segment4); // end -> start
		path1.add(segment3); // end -> start
		path1.add(segment5); // start -> end

		path2 = new RoadPath();
		path2.add(segment9); // start -> end
		path2.add(segment6); // start -> end

		path3 = new RoadPath();
		path3.add(segment11); // start -> end
		path3.add(segment3); // start -> end
		path3.add(segment4); // start -> end
		path3.add(segment1); // start -> end
		path3.add(segment2); // end -> start

		cPath = new ClusteredRoadPath();
	}

	@DisplayName("getRoadPathAt")
	@Nested
	public class GetRoadPathAtTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("#1")
		@Test
		public void testGetRoadPathAt_1() {
			cPath.add(path1);
			cPath.add(path2);
			cPath.add(path3);
	
			assertSame(path1, cPath.getRoadPathAt(0));
			assertThrows(IndexOutOfBoundsException.class, () -> {
				cPath.getRoadPathAt(1);
			});
			assertThrows(IndexOutOfBoundsException.class, () -> {
				cPath.getRoadPathAt(2);
			});
		}
	}

	@DisplayName("add")
	@Nested
	public class AddTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With hole #1")
		@Test
		public void testAddRoadPath_withHole_1() {
			cPath.add(path1);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}

		@DisplayName("With hole #2")
		@Test
		public void testAddRoadPath_withHole_2() {
			cPath.add(path1);
			cPath.add(path2);
			assertEquals(2, cPath.size());
			assertTrue(cPath.contains(path1));
			assertTrue(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}

		@DisplayName("With hole #3")
		@Test
		public void testAddRoadPath_withHole_3() {
			cPath.add(path1);
			cPath.add(path2);
			cPath.add(path3);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}
	
		@DisplayName("Without hole #1")
		@Test
		public void testAddRoadPath_withoutHole_1() {
			// path1 = [ s1, s4, s3, s5 ]
			// path2 = [ s9, s6 ]
			// path3 = [ s11, s3, s4, s1, s2 ]
			cPath.add(path1);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}
		
		@DisplayName("Without hole #2")
		@Test
		public void testAddRoadPath_withoutHole_2() {
			cPath.add(path1);
			cPath.add(path3);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}
		
		@DisplayName("Without hole #3")
		@Test
		public void testAddRoadPath_withoutHole_3() {
			cPath.add(path1);
			cPath.add(path3);
			cPath.add(path2);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}
	}

	@DisplayName("clear")
	@Nested
	public class ClearTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With path")
		@Test
		public void withPath() {
			cPath.add(path1);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}

		@DisplayName("Without path")
		@Test
		public void withoutPath() {
			assertEquals(0, cPath.size());
			assertFalse(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}
	}

	@DisplayName("contains")
	@Nested
	public class ContainsTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("(RoadSegment)")
		@Nested
		public class RoadSegmentTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
			}
			
			@DisplayName("#1")
			@Test
			public void testContainsRoadSegment_1() {
				assertEquals(1, cPath.size());
			}
			
			@DisplayName("#2")
			@Test
			public void testContainsRoadSegment_2() {
				assertTrue(cPath.contains(path1));
			}
			
			@DisplayName("#3")
			@Test
			public void testContainsRoadSegment_3() {
				assertFalse(cPath.contains(path2));
			}
			
			@DisplayName("#4")
			@Test
			public void testContainsRoadSegment_4() {
				assertFalse(cPath.contains(path3));
			}
			
			@DisplayName("#5")
			@Test
			public void testContainsRoadSegment_5() {
				assertTrue(cPath.contains(segment1));
			}
			
			@DisplayName("#6")
			@Test
			public void testContainsRoadSegment_6() {
				assertFalse(cPath.contains(segment2));
			}
			
			@DisplayName("#7")
			@Test
			public void testContainsRoadSegment_7() {
				assertTrue(cPath.contains(segment3));
			}
			
			@DisplayName("#8")
			@Test
			public void testContainsRoadSegment_8() {
				assertTrue(cPath.contains(segment4));
			}
			
			@DisplayName("#9")
			@Test
			public void testContainsRoadSegment_9() {
				assertTrue(cPath.contains(segment5));
			}
			
			@DisplayName("#10")
			@Test
			public void testContainsRoadSegment_10() {
				assertFalse(cPath.contains(segment6));
			}
			
			@DisplayName("#11")
			@Test
			public void testContainsRoadSegment_11() {
				assertFalse(cPath.contains(segment7));
			}
			
			@DisplayName("#12")
			@Test
			public void testContainsRoadSegment_12() {
				assertFalse(cPath.contains(segment8));
			}
			
			@DisplayName("#13")
			@Test
			public void testContainsRoadSegment_13() {
				assertFalse(cPath.contains(segment9));
			}
			
			@DisplayName("#14")
			@Test
			public void testContainsRoadSegment_14() {
				assertFalse(cPath.contains(segment10));
			}
			
			@DisplayName("#15")
			@Test
			public void testContainsRoadSegment_15() {
				assertFalse(cPath.contains(segment11));
			}
		}

		@DisplayName("(RoadConnection)")
		@Nested
		public class RoadConnectionTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
			}
			
			@DisplayName("#1")
			@Test
			public void testContainsRoadConnection_1() {
				assertEquals(1, cPath.size());
			}
			
			@DisplayName("#2")
			@Test
			public void testContainsRoadConnection_2() {
				assertTrue(cPath.contains(path1));
			}
			
			@DisplayName("#3")
			@Test
			public void testContainsRoadConnection_3() {
				assertFalse(cPath.contains(path2));
			}
			
			@DisplayName("#4")
			@Test
			public void testContainsRoadConnection_4() {
				assertFalse(cPath.contains(path3));
			}
			
			@DisplayName("#5")
			@Test
			public void testContainsRoadConnection_5() {
				assertFalse(cPath.contains(connection1));
			}
			
			@DisplayName("#6")
			@Test
			public void testContainsRoadConnection_6() {
				assertTrue(cPath.contains(connection2));
			}
			
			@DisplayName("#7")
			@Test
			public void testContainsRoadConnection_7() {
				assertFalse(cPath.contains(connection3));
			}
			
			@DisplayName("#8")
			@Test
			public void testContainsRoadConnection_8() {
				assertFalse(cPath.contains(connection4));
			}
			
			@DisplayName("#9")
			@Test
			public void testContainsRoadConnection_9() {
				assertTrue(cPath.contains(connection5));
			}
			
			@DisplayName("#10")
			@Test
			public void testContainsRoadConnection_10() {
				assertFalse(cPath.contains(connection6));
			}
			
			@DisplayName("#11")
			@Test
			public void testContainsRoadConnection_11() {
				assertFalse(cPath.contains(connection7));
			}
			
			@DisplayName("#12")
			@Test
			public void testContainsRoadConnection_12() {
				assertFalse(cPath.contains(connection8));
			}
			
			@DisplayName("#13")
			@Test
			public void testContainsRoadConnection_13() {
				assertFalse(cPath.contains(connection9));
			}
		}
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@DisplayName("One path")
		@Nested
		public class OnePathTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
			}
	
			@DisplayName("#1")
			@Test
			public void testIterator_1() {
				assertEquals(1, cPath.size());
			}
			
			@DisplayName("#2")
			@Test
			public void testIterator_2() {
				assertTrue(cPath.contains(path1));
			}
			
			@DisplayName("#3")
			@Test
			public void testIterator_3() {
				assertFalse(cPath.contains(path2));
			}
			
			@DisplayName("#4")
			@Test
			public void testIterator_4() {
				assertFalse(cPath.contains(path3));
			}
		}

		@DisplayName("Two paths")
		@Nested
		public class TwoPathsTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testIterator_1() {
				assertEquals(2, cPath.size());
			}

			@DisplayName("#2")
			@Test
			public void testIterator_2() {
				assertTrue(cPath.contains(path1));
			}

			@DisplayName("#3")
			@Test
			public void testIterator_3() {
				assertTrue(cPath.contains(path2));
			}

			@DisplayName("#4")
			@Test
			public void testIterator_4() {
				assertFalse(cPath.contains(path3));
			}

			@DisplayName("#5")
			@Test
			public void testIterator_5() {
				Iterator<RoadPath> iterator = cPath.iterator();
				assertTrue(iterator.hasNext());
				assertSame(path1, iterator.next());
				assertTrue(iterator.hasNext());
				assertSame(path2, iterator.next());
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("Remove")
		@Nested
		public class RemoveTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path3);
				assertEquals(1, cPath.size());
			}

			@DisplayName("#1")
			@Test
			public void testIteratorRemove_1() {
				Iterator<RoadSegment> iterator = cPath.roadSegments();
	
				assertTrue(iterator.hasNext());
				var s = iterator.next();
				assertNotNull(s);
				assertSame(segment1, s);
	
				assertTrue(iterator.hasNext());
				s = iterator.next();
				assertNotNull(s);
				assertSame(segment4, s);
	
				assertTrue(iterator.hasNext());
				s = iterator.next();
				assertNotNull(s);
				assertSame(segment3, s);
	
				iterator.remove();
	
				assertTrue(iterator.hasNext());
				s = iterator.next();
				assertNotNull(s);
				assertSame(segment5, s);
	
				assertTrue(iterator.hasNext());
				s = iterator.next();
				assertNotNull(s);
				assertSame(segment11, s);
	
				assertTrue(iterator.hasNext());
				s = iterator.next();
				assertNotNull(s);
				assertSame(segment3, s);
	
				assertTrue(iterator.hasNext());
				s = iterator.next();
				assertNotNull(s);
				assertSame(segment4, s);
			}

			@DisplayName("#2")
			@Test
			public void testIteratorRemove_2() {
				Iterator<RoadSegment> iterator = cPath.roadSegments();
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.remove();
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();

				iterator.remove();
	
				assertTrue(iterator.hasNext());
				var s = iterator.next();
				assertNotNull(s);
				assertSame(segment1, s);
	
				assertTrue(iterator.hasNext());
				s = iterator.next();
				assertNotNull(s);
				assertSame(segment2, s);
	
				assertFalse(iterator.hasNext());
	
				assertEquals(3, cPath.size());
			}
		}
	}

	@DisplayName("remove")
	@Nested
	public class RemoveTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();

			cPath.add(path1);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
	
			cPath.add(path2);
			assertEquals(2, cPath.size());
			assertTrue(cPath.contains(path1));
			assertTrue(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}

		@DisplayName("#1")
		@Test
		public void testRemoveRoadPath_1() {
			cPath.remove(path3);
			assertEquals(2, cPath.size());
			assertTrue(cPath.contains(path1));
			assertTrue(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}

		@DisplayName("#2")
		@Test
		public void testRemoveRoadPath_2() {
			cPath.remove(path1);
			assertEquals(1, cPath.size());
			assertFalse(cPath.contains(path1));
			assertTrue(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}

		@DisplayName("#3")
		@Test
		public void testRemoveRoadPath_3() {
			cPath.remove(path1);
			cPath.remove(path2);
			assertEquals(0, cPath.size());
			assertFalse(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
		}
	}

	@DisplayName("toRoadPath")
	@Nested
	public class ToRoadPathTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("#1")
		@Test
		public void testToRoadPath_1() {
			assertNull(cPath.toRoadPath());
		}

		@DisplayName("#2")
		@Test
		public void testToRoadPath_2() {
			cPath.add(path1);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
	
			assertSame(path1, cPath.toRoadPath());
		}

		@DisplayName("#3")
		@Test
		public void testToRoadPath_3() {
			cPath.add(path1);
			cPath.add(path2);
			assertEquals(2, cPath.size());
			assertTrue(cPath.contains(path1));
			assertTrue(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
	
			assertNull(cPath.toRoadPath());
		}

		@DisplayName("#4")
		@Test
		public void testToRoadPath_4() {
			cPath.add(path1);
			cPath.add(path2);
			cPath.add(path3);
			assertEquals(1, cPath.size());
			assertTrue(cPath.contains(path1));
			assertFalse(cPath.contains(path2));
			assertFalse(cPath.contains(path3));
	
			assertSame(path1, cPath.toRoadPath());
		}
	}


	@DisplayName("roadSegments")
	@Nested
	public class RoadSegmentsTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("#1")
		@Test
		public void testRoadSegments() {
			// path1 = [ s1, s4, s3, s5 ]
			// path2 = [ s9, s6 ]
			// path3 = [ s11, s3, s4, s1, s2 ]
			cPath.add(path1);
			cPath.add(path2);
	
			Iterator<RoadSegment> iterator = cPath.roadSegments();
			assertTrue(iterator.hasNext());
			assertSame(segment1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(segment4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(segment5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(segment9, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(segment6, iterator.next());
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("invert")
	@Nested
	public class InvertTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With hole")
		@Test
		public void testInvert_withHole() {
			RoadPath inverted1 = new RoadPath(path1);
			inverted1.invert();
			RoadPath inverted2 = new RoadPath(path2);
			inverted2.invert();
	
			cPath.add(path1);
			cPath.add(path2);
	
			cPath.invert();
	
			RoadPath p;
	
			p = cPath.first();
			assertEquals(inverted1.size(), p.size());
			for(int i=0; i<inverted1.size(); ++i) {
				assertEquals(inverted1.get(i), p.get(i));
			}
			p = cPath.last();
			assertEquals(inverted2.size(), p.size());
			for(int i=0; i<inverted2.size(); ++i) {
				assertEquals(inverted2.get(i), p.get(i));
			}
		}
	
		@DisplayName("Without hole")
		@Test
		public void testInvert_withoutHole() {
			RoadPath inverted = new RoadPath(path1);
			inverted.addAll(path3);
			inverted.invert();
	
			cPath.add(path1);
			cPath.add(path3);
	
			cPath.invert();
	
			RoadPath p;
	
			p = cPath.first();
			assertEquals(inverted.size(), p.size());
			for(int i=0; i<inverted.size(); ++i) {
				assertEquals(inverted.get(i), p.get(i), Integer.toString(i));
			}
		}
	}

	@DisplayName("getRoadSegmentCount")
	@Nested
	public class GetRoadSegmentCountTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With hole")
		@Test
		public void testGetRoadSegmentCount_withHole() {
			cPath.add(path1);
			cPath.add(path2);
			assertEquals(6, cPath.getRoadSegmentCount());
		}

		@DisplayName("Without hole")
		@Test
		public void testGetRoadSegmentCount_withoutHole() {
			cPath.add(path1);
			cPath.add(path3);
			assertEquals(9, cPath.getRoadSegmentCount());
		}
	}

	@DisplayName("indexOf")
	@Nested
	public class IndexOfTest {

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testIndexOfRoadSegment_withHole_1() {
				assertEquals(0, cPath.indexOf(segment1));
			}

			@DisplayName("#2")
			@Test
			public void testIndexOfRoadSegment_withHole_2() {
				assertEquals(-1, cPath.indexOf(segment2));
			}

			@DisplayName("#3")
			@Test
			public void testIndexOfRoadSegment_withHole_3() {
				assertEquals(2, cPath.indexOf(segment3));
			}

			@DisplayName("#4")
			@Test
			public void testIndexOfRoadSegment_withHole_4() {
				assertEquals(1, cPath.indexOf(segment4));
			}

			@DisplayName("#5")
			@Test
			public void testIndexOfRoadSegment_withHole_5() {
				assertEquals(3, cPath.indexOf(segment5));
			}

			@DisplayName("#6")
			@Test
			public void testIndexOfRoadSegment_withHole_6() {
				assertEquals(5, cPath.indexOf(segment6));
			}

			@DisplayName("#7")
			@Test
			public void testIndexOfRoadSegment_withHole_7() {
				assertEquals(-1, cPath.indexOf(segment7));
			}

			@DisplayName("#8")
			@Test
			public void testIndexOfRoadSegment_withHole_8() {
				assertEquals(-1, cPath.indexOf(segment8));
			}

			@DisplayName("#9")
			@Test
			public void testIndexOfRoadSegment_withHole_9() {
				assertEquals(4, cPath.indexOf(segment9));
			}

			@DisplayName("#10")
			@Test
			public void testIndexOfRoadSegment_withHole_10() {
				assertEquals(-1, cPath.indexOf(segment10));
			}

			@DisplayName("#11")
			@Test
			public void testIndexOfRoadSegment_withHole_11() {
				assertEquals(-1, cPath.indexOf(segment11));
			}
		}		

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path3);
			}

			@DisplayName("#1")
			@Test
			public void testIndexOfRoadSegment_withoutHole_1() {
				assertEquals(0, cPath.indexOf(segment1));
			}

			@DisplayName("#2")
			@Test
			public void testIndexOfRoadSegment_withoutHole_2() {
				assertEquals(8, cPath.indexOf(segment2));
			}

			@DisplayName("#3")
			@Test
			public void testIndexOfRoadSegment_withoutHole_3() {
				assertEquals(2, cPath.indexOf(segment3));
			}

			@DisplayName("#4")
			@Test
			public void testIndexOfRoadSegment_withoutHole_4() {
				assertEquals(1, cPath.indexOf(segment4));
			}

			@DisplayName("#5")
			@Test
			public void testIndexOfRoadSegment_withoutHole_5() {
				assertEquals(3, cPath.indexOf(segment5));
			}

			@DisplayName("#6")
			@Test
			public void testIndexOfRoadSegment_withoutHole_6() {
				assertEquals(-1, cPath.indexOf(segment6));
			}

			@DisplayName("#7")
			@Test
			public void testIndexOfRoadSegment_withoutHole_7() {
				assertEquals(-1, cPath.indexOf(segment7));
			}

			@DisplayName("#8")
			@Test
			public void testIndexOfRoadSegment_withoutHole_8() {
				assertEquals(-1, cPath.indexOf(segment8));
			}

			@DisplayName("#9")
			@Test
			public void testIndexOfRoadSegment_withoutHole_9() {
				assertEquals(-1, cPath.indexOf(segment9));
			}

			@DisplayName("#10")
			@Test
			public void testIndexOfRoadSegment_withoutHole_10() {
				assertEquals(-1, cPath.indexOf(segment10));
			}

			@DisplayName("#11")
			@Test
			public void testIndexOfRoadSegment_withoutHole_11() {
				assertEquals(4, cPath.indexOf(segment11));
			}
		}
	}

	@DisplayName("lastIndexOf")
	@Nested
	public class LastIndexOfTest {

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testLastIndexOfRoadSegment_withHole_1() {
				assertEquals(0, cPath.lastIndexOf(segment1));
			}

			@DisplayName("#2")
			@Test
			public void testLastIndexOfRoadSegment_withHole_2() {
				assertEquals(-1, cPath.lastIndexOf(segment2));
			}

			@DisplayName("#3")
			@Test
			public void testLastIndexOfRoadSegment_withHole_3() {
				assertEquals(2, cPath.lastIndexOf(segment3));
			}

			@DisplayName("#4")
			@Test
			public void testLastIndexOfRoadSegment_withHole_4() {
				assertEquals(1, cPath.lastIndexOf(segment4));
			}

			@DisplayName("#5")
			@Test
			public void testLastIndexOfRoadSegment_withHole_5() {
				assertEquals(3, cPath.lastIndexOf(segment5));
			}

			@DisplayName("#6")
			@Test
			public void testLastIndexOfRoadSegment_withHole_6() {
				assertEquals(5, cPath.lastIndexOf(segment6));
			}

			@DisplayName("#7")
			@Test
			public void testLastIndexOfRoadSegment_withHole_7() {
				assertEquals(-1, cPath.lastIndexOf(segment7));
			}

			@DisplayName("#8")
			@Test
			public void testLastIndexOfRoadSegment_withHole_8() {
				assertEquals(-1, cPath.lastIndexOf(segment8));
			}

			@DisplayName("#9")
			@Test
			public void testLastIndexOfRoadSegment_withHole_9() {
				assertEquals(4, cPath.lastIndexOf(segment9));
			}

			@DisplayName("#10")
			@Test
			public void testLastIndexOfRoadSegment_withHole_10() {
				assertEquals(-1, cPath.lastIndexOf(segment10));
			}

			@DisplayName("#11")
			@Test
			public void testLastIndexOfRoadSegment_withHole_11() {
				assertEquals(-1, cPath.lastIndexOf(segment11));
			}
		}

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path3);
			}

			@DisplayName("#1")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_1() {
				assertEquals(7, cPath.lastIndexOf(segment1));
			}

			@DisplayName("#2")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_2() {
				assertEquals(8, cPath.lastIndexOf(segment2));
			}

			@DisplayName("#3")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_3() {
				assertEquals(5, cPath.lastIndexOf(segment3));
			}

			@DisplayName("#4")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_4() {
				assertEquals(6, cPath.lastIndexOf(segment4));
			}

			@DisplayName("#5")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_5() {
				assertEquals(3, cPath.lastIndexOf(segment5));
			}

			@DisplayName("#6")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_6() {
				assertEquals(-1, cPath.lastIndexOf(segment6));
			}

			@DisplayName("#7")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_7() {
				assertEquals(-1, cPath.lastIndexOf(segment7));
			}

			@DisplayName("#8")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_8() {
				assertEquals(-1, cPath.lastIndexOf(segment8));
			}

			@DisplayName("#9")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_9() {
				assertEquals(-1, cPath.lastIndexOf(segment9));
			}

			@DisplayName("#10")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_10() {
				assertEquals(-1, cPath.lastIndexOf(segment10));
			}

			@DisplayName("#11")
			@Test
			public void testLastIndexOfRoadSegment_withoutHole_11() {
				assertEquals(4, cPath.lastIndexOf(segment11));
			}
		}
	}

	@DisplayName("getRoadSegmentAt")
	@Nested
	public class GetRoadSegmentAtTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testGetRoadSegmentAtInt_withHole_1() {
				assertEquals(segment1, cPath.getRoadSegmentAt(0));
			}

			@DisplayName("#2")
			@Test
			public void testGetRoadSegmentAtInt_withHole_2() {
				assertEquals(segment4, cPath.getRoadSegmentAt(1));
			}

			@DisplayName("#3")
			@Test
			public void testGetRoadSegmentAtInt_withHole_3() {
				assertEquals(segment3, cPath.getRoadSegmentAt(2));
			}

			@DisplayName("#4")
			@Test
			public void testGetRoadSegmentAtInt_withHole_4() {
				assertEquals(segment5, cPath.getRoadSegmentAt(3));
			}

			@DisplayName("#5")
			@Test
			public void testGetRoadSegmentAtInt_withHole_5() {
				assertEquals(segment9, cPath.getRoadSegmentAt(4));
			}

			@DisplayName("#6")
			@Test
			public void testGetRoadSegmentAtInt_withHole_6() {
				assertEquals(segment6, cPath.getRoadSegmentAt(5));
			}
		}

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
				cPath.add(path3);
			}

			@DisplayName("#1")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_1() {
				assertEquals(segment1, cPath.getRoadSegmentAt(0));
			}

			@DisplayName("#2")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_2() {
				assertEquals(segment4, cPath.getRoadSegmentAt(1));
			}

			@DisplayName("#3")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_3() {
				assertEquals(segment3, cPath.getRoadSegmentAt(2));
			}

			@DisplayName("#4")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_4() {
				assertEquals(segment5, cPath.getRoadSegmentAt(3));
			}

			@DisplayName("#5")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_5() {
				assertEquals(segment11, cPath.getRoadSegmentAt(4));
			}

			@DisplayName("#6")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_6() {
				assertEquals(segment3, cPath.getRoadSegmentAt(5));
			}

			@DisplayName("#7")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_7() {
				assertEquals(segment4, cPath.getRoadSegmentAt(6));
			}

			@DisplayName("#8")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_8() {
				assertEquals(segment1, cPath.getRoadSegmentAt(7));
			}

			@DisplayName("#9")
			@Test
			public void testGetRoadSegmentAtInt_withoutHole_9() {
				assertEquals(segment2, cPath.getRoadSegmentAt(8));
			}
		}
	}

	@DisplayName("first")
	@Nested
	public class FirstTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testFirst_withHole() {
				assertEquals(path1, cPath.first());
			}
		}

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
				cPath.add(path3);
			}

			@DisplayName("#1")
			@Test
			public void testFirst_withoutHole() {
				assertEquals(path1, cPath.first());
			}
		}
	}

	@DisplayName("last")
	@Nested
	public class LastTest {

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testLast_withHole() {
				assertEquals(path2, cPath.last());
			}
		}

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path3);
			}

			@DisplayName("#1")
			@Test
			public void testLast_withoutHole() {
				assertEquals(path1, cPath.last());
			}
		}
	}

	@DisplayName("removeRoadSegmentAt")
	@Nested
	public class RemoveRoadSegmentAtTest {

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			private RoadSegment s;

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path3);
				s = cPath.removeRoadSegmentAt(2);
			}

			@DisplayName("#1")
			@Test
			public void testRemoveRoadSegmentAtInt_withoutHole_1() {
				assertSame(segment3, s);
			}

			@DisplayName("#2")
			@Test
			public void testRemoveRoadSegmentAtInt_withoutHole_2() {
				assertEquals(2, cPath.size());
			}

			@DisplayName("#3")
			@Test
			public void testRemoveRoadSegmentAtInt_withoutHole_3() {
				Iterator<RoadSegment> iterator = cPath.roadSegments();
				assertTrue(iterator.hasNext());
				s = iterator.next();
				if (segment1==s) {
					assertSame(segment1, s);
					assertTrue(iterator.hasNext());
					assertSame(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment5, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment1, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment2, iterator.next());
					assertFalse(iterator.hasNext());
				}
				else {
					assertSame(segment5, s);
					assertTrue(iterator.hasNext());
					assertSame(segment11, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment3, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment4, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment1, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment2, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment1, iterator.next());
					assertTrue(iterator.hasNext());
					assertSame(segment4, iterator.next());
					assertFalse(iterator.hasNext());
				}
			}
		}
	}

	@DisplayName("getPathForRoadSegmentAt")
	@Nested
	public class GetPathForRoadSegmentAtTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testGetPathForRoadSegmentAtInt_withHole_1() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(0));
			}

			@DisplayName("#2")
			@Test
			public void testGetPathForRoadSegmentAtInt_withHole_2() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(1));
			}

			@DisplayName("#3")
			@Test
			public void testGetPathForRoadSegmentAtInt_withHole_3() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(2));
			}

			@DisplayName("#4")
			@Test
			public void testGetPathForRoadSegmentAtInt_withHole_4() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(3));
			}

			@DisplayName("#5")
			@Test
			public void testGetPathForRoadSegmentAtInt_withHole_5() {
				assertSame(path2, cPath.getPathForRoadSegmentAt(4));
			}

			@DisplayName("#6")
			@Test
			public void testGetPathForRoadSegmentAtInt_withHole_6() {
				assertSame(path2, cPath.getPathForRoadSegmentAt(5));
			}
		}

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@BeforeEach
			public void setUp() {
				cPath.add(path1);
				cPath.add(path3);
			}

			@DisplayName("#1")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_1() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(0));
			}

			@DisplayName("#2")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_2() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(1));
			}

			@DisplayName("#3")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_3() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(2));
			}

			@DisplayName("#4")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_4() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(3));
			}

			@DisplayName("#5")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_5() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(4));
			}

			@DisplayName("#6")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_6() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(5));
			}

			@DisplayName("#7")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_7() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(6));
			}

			@DisplayName("#8")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_8() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(7));
			}

			@DisplayName("#9")
			@Test
			public void testGetPathForRoadSegmentAtInt_withoutHole_9() {
				assertSame(path1, cPath.getPathForRoadSegmentAt(8));
			}
		}
	}

	@DisplayName("getRoadSegmentDirectionAt")
	@Nested
	public class GetRoadSegmentDirectionAtTest {

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path2);
			}

			@DisplayName("#1")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withHole_1() {
				assertEquals(Direction1D.REVERTED_DIRECTION, cPath.getRoadSegmentDirectionAt(0));
			}

			@DisplayName("#2")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withHole_2() {
				assertEquals(Direction1D.REVERTED_DIRECTION, cPath.getRoadSegmentDirectionAt(1));
			}

			@DisplayName("#3")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withHole_3() {
				assertEquals(Direction1D.REVERTED_DIRECTION, cPath.getRoadSegmentDirectionAt(2));
			}

			@DisplayName("#4")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withHole_4() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(3));
			}

			@DisplayName("#5")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withHole_5() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(4));
			}

			@DisplayName("#6")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withHole_6() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(5));
			}
		}

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@BeforeEach
			public void setUp() {
				initRoadNetwork();
				cPath.add(path1);
				cPath.add(path3);
			}

			@DisplayName("#1")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_1() {
				assertEquals(Direction1D.REVERTED_DIRECTION, cPath.getRoadSegmentDirectionAt(0));
			}

			@DisplayName("#2")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_2() {
				assertEquals(Direction1D.REVERTED_DIRECTION, cPath.getRoadSegmentDirectionAt(1));
			}

			@DisplayName("#2")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_3() {
				assertEquals(Direction1D.REVERTED_DIRECTION, cPath.getRoadSegmentDirectionAt(2));
			}

			@DisplayName("#4")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_4() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(3));
			}

			@DisplayName("#5")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_5() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(4));
			}

			@DisplayName("#6")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_6() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(5));
			}

			@DisplayName("#7")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_7() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(6));
			}

			@DisplayName("#8")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_8() {
				assertEquals(Direction1D.SEGMENT_DIRECTION, cPath.getRoadSegmentDirectionAt(7));
			}

			@DisplayName("#9")
			@Test
			public void testGetRoadSegmentDirectionAtInt_withoutHole_9() {
				assertEquals(Direction1D.REVERTED_DIRECTION, cPath.getRoadSegmentDirectionAt(8));
			}
		}
	}

	@DisplayName("getLength")
	@Nested
	public class GetLengthTest {

		@BeforeEach
		public void setUp() {
			initRoadNetwork();
		}

		@DisplayName("With hole")
		@Nested
		public class WithHoleTest {

			@DisplayName("#1")
			@Test
			public void testGetLength_withHole_1() {
				double lengthPath1 = path1.getLength();
				cPath.add(path1);
				assertEpsilonEquals(lengthPath1, cPath.getLength());
			}

			@DisplayName("#2")
			@Test
			public void testGetLength_withHole_2() {
				double lengthPath1 = path1.getLength();
				double lengthPath2 = path2.getLength();

				cPath.add(path1);
				cPath.add(path2);
				assertEpsilonEquals(lengthPath1+lengthPath2, cPath.getLength());
			}
		}

		@DisplayName("Without hole")
		@Nested
		public class WithoutHoleTest {

			@Test
			public void testGetLength_withoutHole_1() {
				double lengthPath1 = path1.getLength();
				cPath.add(path1);
				assertEpsilonEquals(lengthPath1, cPath.getLength());
			}

			@Test
			public void testGetLength_withoutHole_2() {
				double lengthPath1 = path1.getLength();
				double lengthPath3 = path3.getLength();
				cPath.add(path1);
				cPath.add(path3);
				assertEpsilonEquals(lengthPath1+lengthPath3, cPath.getLength());
			}
		}
	}
}

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

package org.arakhne.afc.math.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("GraphPath")
@SuppressWarnings("all")
public class GraphPathTest {

	private static abstract class AbstractBaseTestCase extends AbstractTestCase {
		
		protected SegmentStub s1;
		protected SegmentStub s2;
		protected SegmentStub s3;
		protected SegmentStub s4;
		protected SegmentStub s5;
		protected SegmentStub s6;
		protected ConnectionStub c1;
		protected ConnectionStub c2;
		protected ConnectionStub c3;
		protected ConnectionStub c4;
		protected ConnectionStub c5;
		protected ConnectionStub c6;
		protected ConnectionStub c7;
		protected GraphPathStub path;
		
		@BeforeEach
		public void setUp() throws Exception {
			path = new GraphPathStub();
			
			c1 = new ConnectionStub("c1[s1,s4]");  //$NON-NLS-1$
			c2 = new ConnectionStub("c2[s1,s2]");  //$NON-NLS-1$
			c3 = new ConnectionStub("c3[s2,s3,s4]");  //$NON-NLS-1$
			c4 = new ConnectionStub("c4[s3,s5]");  //$NON-NLS-1$
			c5 = new ConnectionStub("c5[s5]");  //$NON-NLS-1$
			c6 = new ConnectionStub("c6[s6]");  //$NON-NLS-1$
			c7 = new ConnectionStub("c7[s6]");  //$NON-NLS-1$
			
			s1 = new SegmentStub("s1", c1, c2);  //$NON-NLS-1$
			s2 = new SegmentStub("s2", c2, c3);  //$NON-NLS-1$
			s3 = new SegmentStub("s3", c3, c4);  //$NON-NLS-1$
			s4 = new SegmentStub("s4", c1, c3);  //$NON-NLS-1$
			s5 = new SegmentStub("s5", c4, c5);  //$NON-NLS-1$
			s6 = new SegmentStub("s6", c6, c7);  //$NON-NLS-1$
		}
		
		@AfterEach
		public void tearDown() throws Exception {
			path = null;
			c1 = c2 = c3 = c4 = c5 = c6 = c7 = null;
			s1 = s2 = s3 = s4 = s5 = s6 = null;
		}
	}

	@DisplayName("size")
	@Nested
	public class Size extends AbstractBaseTestCase {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(0, path.size());
		}
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty extends AbstractBaseTestCase {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(path.isEmpty());
		}
	}

	@DisplayName("getStartingPointFor")
	@Nested
	public class GetStartingPointFor extends AbstractBaseTestCase {

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c2 - 1 4 - c3
			path.add(s2); // c2 - 1 4 2 - c2
			path.add(s1); // c2 - 1 4 2 1 - c1
			path.add(s1); // c2 - 1 4 2 1 1 - c2
			path.add(s2); // c2 - 1 4 2 1 1 2 - c3
			path.add(s3); // c2 - 1 4 2 1 1 2 3 - c4
			path.add(s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		}

		@DisplayName("Not reversable #1")
		@Test
		public void getStartingPointFor_notReversable_1() {
			initNotReversable();
			assertSame(c5, path.getStartingPointFor(0));
		}

		@DisplayName("Not reversable #2")
		@Test
		public void getStartingPointFor_notReversable_2() {
			initNotReversable();
			assertSame(c4, path.getStartingPointFor(1));
		}

		@DisplayName("Not reversable #3")
		@Test
		public void getStartingPointFor_notReversable_3() {
			initNotReversable();
			assertSame(c3, path.getStartingPointFor(2));
		}

		@DisplayName("Not reversable #4")
		@Test
		public void getStartingPointFor_notReversable_4() {
			initNotReversable();
			assertSame(c1, path.getStartingPointFor(3));
		}

		@DisplayName("Not reversable #5")
		@Test
		public void getStartingPointFor_notReversable_5() {
			initNotReversable();
			assertSame(c2, path.getStartingPointFor(4));
		}

		@DisplayName("Not reversable #6")
		@Test
		public void getStartingPointFor_notReversable_6() {
			initNotReversable();
			assertSame(c3, path.getStartingPointFor(5));
		}

		@DisplayName("Not reversable #7")
		@Test
		public void getStartingPointFor_notReversable_7() {
			initNotReversable();
			assertThrows(IndexOutOfBoundsException.class, () -> path.getStartingPointFor(6));
		}
		
		@DisplayName("Reversable #1")
		@Test
		public void getStartingPointFor_reversable_1() {
			initReversable();
			assertSame(c2, path.getStartingPointFor(0));
		}
		
		@DisplayName("Reversable #2")
		@Test
		public void getStartingPointFor_reversable_2() {
			initReversable();
			assertSame(c1, path.getStartingPointFor(1));
		}
		
		@DisplayName("Reversable #3")
		@Test
		public void getStartingPointFor_reversable_3() {
			initReversable();
			assertSame(c3, path.getStartingPointFor(2));
		}
		
		@DisplayName("Reversable #4")
		@Test
		public void getStartingPointFor_reversable_4() {
			initReversable();
			assertSame(c2, path.getStartingPointFor(3));
		}
		
		@DisplayName("Reversable #5")
		@Test
		public void getStartingPointFor_reversable_5() {
			initReversable();
			assertSame(c1, path.getStartingPointFor(4));
		}
		
		@DisplayName("Reversable #6")
		@Test
		public void getStartingPointFor_reversable_6() {
			initReversable();
			assertSame(c2, path.getStartingPointFor(5));
		}
		
		@DisplayName("Reversable #7")
		@Test
		public void getStartingPointFor_reversable_7() {
			initReversable();
			assertSame(c3, path.getStartingPointFor(6));
		}
		
		@DisplayName("Reversable #8")
		@Test
		public void getStartingPointFor_reversable_8() {
			initReversable();
			assertSame(c4, path.getStartingPointFor(7));
		}
		
		@DisplayName("Reversable #9")
		@Test
		public void getStartingPointFor_reversable_9() {
			initReversable();
			assertThrows(IndexOutOfBoundsException.class, () -> path.getStartingPointFor(8));
		}
	}
	
	@DisplayName("add")
	@Nested
	public class Add extends AbstractBaseTestCase {

		@DisplayName("(int,ST) #1")
		@Test
		public void addIntegerST_1() {
			assertTrue(path.isEmpty());
		}

		@DisplayName("(int,ST) #2")
		@Test
		public void addIntegerST_2() {
			assertThrows(IndexOutOfBoundsException.class, () -> path.add(45, s1));
		}

		@DisplayName("(int,ST) #3")
		@Test
		public void addIntegerST_3() {
			path.add(0, s1); // c1 - 1 - c2
			assertFalse(path.isEmpty());
			assertEquals(1, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(int,ST) #4")
		@Test
		public void addIntegerST_4() {
			path.add(0, s1); // c1 - 1 - c2
			path.add(0, s1); // c2 - 1 1 - c2
			assertFalse(path.isEmpty());
			assertEquals(2, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(int,ST) #5")
		@Test
		public void addIntegerST_5() {
			path.add(0, s1); // c1 - 1 - c2
			path.add(0, s1); // c2 - 1 1 - c2
			assertThrows(IllegalArgumentException.class, () -> path.add(1, s2));
		}

		@DisplayName("(int,ST) #6")
		@Test
		public void addIntegerST_6() {
			path.add(0, s1); // c1 - 1 - c2
			path.add(0, s1); // c2 - 1 1 - c2
			path.add(2, s2);
			assertFalse(path.isEmpty());
			assertEquals(3, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(int,ST) #7")
		@Test
		public void addIntegerST_7() {
			path.add(0, s1); // c1 - 1 - c2
			path.add(0, s1); // c2 - 1 1 - c2
			path.add(2, s2);
			path.add(3, s4);
			assertFalse(path.isEmpty());
			assertEquals(4, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}

		@DisplayName("(int,ST) #8")
		@Test
		public void addIntegerST_8() {
			path.add(0, s1); // c1 - 1 - c2
			path.add(0, s1); // c2 - 1 1 - c2
			path.add(2, s2);
			path.add(3, s4);
			path.add(0, s2);
			assertFalse(path.isEmpty());
			assertEquals(5, path.size());
			assertSame(s2, path.getFirstSegment());
			assertSame(s4, path.getLastSegment());
			assertSame(c3, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}

		@DisplayName("(ST,PT) #1")
		@Test
		public void addSTPT_1() {
			assertTrue(path.isEmpty());
		}

		@DisplayName("(ST,PT) #2")
		@Test
		public void addSTPT_2() {
			assertTrue(path.add(s1, c2));
			assertFalse(path.isEmpty());
			assertEquals(1, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}

		@DisplayName("(ST,PT) #3")
		@Test
		public void addSTPT_3() {
			path.add(s1, c2);
			assertFalse(path.add(s4, c3));
		}

		@DisplayName("(ST,PT) #4")
		@Test
		public void addSTPT_4() {
			path.add(s1, c2);
			assertTrue(path.add(s4, c1));
			assertFalse(path.isEmpty());
			assertEquals(2, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getSecondSegment());
			assertSame(s1, path.getAntepenulvianSegment());
			assertSame(s4, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST,PT) #5")
		@Test
		public void addSTPT_5() {
			path.add(s1, c2);
			path.add(s4, c1);
			assertFalse(path.add(s5, c5));
		}

		@DisplayName("(ST,PT) #6")
		@Test
		public void addSTPT_6() {
			path.add(s1, c2);
			path.add(s4, c1);
			assertFalse(path.add(s5, c4));
		}

		@DisplayName("(ST,PT) #7")
		@Test
		public void addSTPT_7() {
			path.add(s1, c2);
			path.add(s4, c1);
			assertFalse(path.add(s1, c1));
		}

		@DisplayName("(ST,PT) #8")
		@Test
		public void addSTPT_8() {
			path.add(s1, c2);
			path.add(s4, c1);
			assertTrue(path.add(s1, c2));
			assertFalse(path.isEmpty());
			assertEquals(3, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s1, path.getAntepenulvianSegment());
			assertSame(s4, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST,PT) #9")
		@Test
		public void addSTPT_9() {
			path.add(s1, c2);
			path.add(s4, c1);
			path.add(s1, c2);
			assertFalse(path.add(s2, c2));
		}

		@DisplayName("(ST,PT) #10")
		@Test
		public void addSTPT_10() {
			path.add(s1, c2);
			path.add(s4, c1);
			path.add(s1, c2);
			assertTrue(path.add(s2, c3));
			assertFalse(path.isEmpty());
			assertEquals(4, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s4, path.getAntepenulvianSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) #11")
		@Test
		public void addSTPT_11() {
			path.add(s1, c2);
			path.add(s4, c1);
			path.add(s1, c2);
			path.add(s2, c3);
			assertFalse(path.add(s5, c5));
		}

		@DisplayName("(ST,PT) #12")
		@Test
		public void addSTPT_12() {
			path.add(s1, c2);
			path.add(s4, c1);
			path.add(s1, c2);
			path.add(s2, c3);
			assertFalse(path.add(s5, c4));
		}

		@DisplayName("(ST,PT) #13")
		@Test
		public void addSTPT_13() {
			path.add(s1, c2);
			path.add(s4, c1);
			path.add(s1, c2);
			path.add(s2, c3);
			assertTrue(path.add(s1, c1));		
			assertFalse(path.isEmpty());
			assertEquals(5, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s4, path.getAntepenulvianSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) #14")
		@Test
		public void addSTPT_14() {
			path.add(s1, c2);
			path.add(s4, c1);
			path.add(s1, c2);
			path.add(s2, c3);
			path.add(s1, c1);		
			assertTrue(path.add(s1, c2));
			assertFalse(path.isEmpty());
			assertEquals(6, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s2, path.getAntepenulvianSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void notReversable_1() {
			path.setFirstSegmentReversable(false);
			assertTrue(path.isEmpty());
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void notReversable_2() {
			path.setFirstSegmentReversable(false);
			assertTrue(path.add(s1));
			assertFalse(path.isEmpty());
			assertEquals(1, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #3")
		@Test
		public void notReversable_3() {
			path.setFirstSegmentReversable(false);
			path.add(s1);
			assertTrue(path.add(s4));
			assertFalse(path.isEmpty());
			assertEquals(2, path.size());
			assertSame(s4, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s4, path.getAntepenulvianSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c3, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #4")
		@Test
		public void notReversable_4() {
			path.setFirstSegmentReversable(false);
			path.add(s1);
			path.add(s4);
			assertFalse(path.add(s5));
		}

		@DisplayName("(ST) Not reversable #5")
		@Test
		public void notReversable_5() {
			path.setFirstSegmentReversable(false);
			path.add(s1);
			path.add(s4);
			assertTrue(path.add(s2));
			assertFalse(path.isEmpty());
			assertEquals(3, path.size());
			assertSame(s4, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s1, path.getAntepenulvianSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c3, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #6")
		@Test
		public void notReversable_6() {
			path.setFirstSegmentReversable(false);
			path.add(s1);
			path.add(s4);
			path.add(s2);
			assertFalse(path.add(s5));
		}

		@DisplayName("(ST) Not reversable #7")
		@Test
		public void notReversable_7() {
			path.setFirstSegmentReversable(false);
			path.add(s1);
			path.add(s4);
			path.add(s2);
			assertFalse(path.add(s1));
		}

		@DisplayName("(ST) Not reversable #8")
		@Test
		public void notReversable_8() {
			path.setFirstSegmentReversable(false);
			path.add(s1);
			path.add(s4);
			path.add(s2);
			assertTrue(path.add(s3));		
			assertFalse(path.isEmpty());
			assertEquals(4, path.size());
			assertSame(s4, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s2, path.getAntepenulvianSegment());
			assertSame(s3, path.getLastSegment());
			assertSame(c3, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #9")
		@Test
		public void notReversable_9() {
			path.setFirstSegmentReversable(false);
			path.add(s1);
			path.add(s4);
			path.add(s2);
			path.add(s3);		
			assertTrue(path.add(s3));		
			assertFalse(path.isEmpty());
			assertEquals(5, path.size());
			assertSame(s4, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s3, path.getAntepenulvianSegment());
			assertSame(s3, path.getLastSegment());
			assertSame(c3, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void reversable_1() {
			path.setFirstSegmentReversable(true);
			assertTrue(path.isEmpty());
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void reversable_2() {
			path.setFirstSegmentReversable(true);
			assertTrue(path.add(s1));
			assertFalse(path.isEmpty());
			assertEquals(1, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #3")
		@Test
		public void reversable_3() {
			path.setFirstSegmentReversable(true);
			path.add(s1);
			assertTrue(path.add(s4));
			assertFalse(path.isEmpty());
			assertEquals(2, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getSecondSegment());
			assertSame(s1, path.getAntepenulvianSegment());
			assertSame(s4, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #4")
		@Test
		public void reversable_4() {
			path.setFirstSegmentReversable(true);
			path.add(s1);
			path.add(s4);
			assertFalse(path.add(s5));
		}
		
		@DisplayName("(ST) Reversable #5")
		@Test
		public void reversable_5() {
			path.setFirstSegmentReversable(true);
			path.add(s1);
			path.add(s4);
			assertTrue(path.add(s2));
			assertFalse(path.isEmpty());
			assertEquals(3, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getSecondSegment());
			assertSame(s4, path.getAntepenulvianSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #6")
		@Test
		public void reversable_6() {
			path.setFirstSegmentReversable(true);
			path.add(s1);
			path.add(s4);
			path.add(s2);
			assertFalse(path.add(s5));
		}
		
		@DisplayName("(ST) Reversable #7")
		@Test
		public void reversable_7() {
			path.setFirstSegmentReversable(true);
			path.add(s1);
			path.add(s4);
			path.add(s2);
			assertTrue(path.add(s1));
			assertFalse(path.isEmpty());
			assertEquals(4, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getSecondSegment());
			assertSame(s2, path.getAntepenulvianSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #8")
		@Test
		public void reversable_8() {
			path.setFirstSegmentReversable(true);
			path.add(s1);
			path.add(s4);
			path.add(s2);
			path.add(s1);
			assertFalse(path.add(s3));		
		}
	
		@DisplayName("(ST) First is reversed #1")
		@Test
		public void firstIsReversed_1() {
			assertTrue(path.isEmpty());
		}
		
		@DisplayName("(ST) First is reversed #2")
		@Test
		public void firstIsReversed_2() {
			assertTrue(path.add(s1, c2));
			assertFalse(path.isEmpty());
			assertEquals(1, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}
		
		@DisplayName("(ST) First is reversed #3")
		@Test
		public void firstIsReversed_3() {
			path.add(s1, c2);
			assertTrue(path.add(s4));
			assertFalse(path.isEmpty());
			assertEquals(2, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getSecondSegment());
			assertSame(s1, path.getAntepenulvianSegment());
			assertSame(s4, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
		
		@DisplayName("(ST) First is reversed #4")
		@Test
		public void firstIsReversed_4() {
			path.add(s1, c2);
			path.add(s4);
			assertFalse(path.add(s5));
		}
		
		@DisplayName("(ST) First is reversed #5")
		@Test
		public void firstIsReversed_5() {
			path.add(s1, c2);
			path.add(s4);
			assertTrue(path.add(s2));
			assertFalse(path.isEmpty());
			assertEquals(3, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getSecondSegment());
			assertSame(s4, path.getAntepenulvianSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
		
		@DisplayName("(ST) First is reversed #6")
		@Test
		public void firstIsReversed_6() {
			path.add(s1, c2);
			path.add(s4);
			path.add(s2);
			assertFalse(path.add(s5));
		}
		
		@DisplayName("(ST) First is reversed #7")
		@Test
		public void firstIsReversed_7() {
			path.add(s1, c2);
			path.add(s4);
			path.add(s2);
			assertTrue(path.add(s1));		
			assertFalse(path.isEmpty());
			assertEquals(4, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s4, path.getSecondSegment());
			assertSame(s2, path.getAntepenulvianSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}
		
		@DisplayName("(ST) First is reversed #8")
		@Test
		public void firstIsReversed_8() {
			path.add(s1, c2);
			path.add(s4);
			path.add(s2);
			path.add(s1);		
			assertFalse(path.add(s3));		
		}
	
		@DisplayName("(ST) Add first #1")
		@Test
		public void addFirst_1() {
			assertTrue(path.isEmpty());
		}
		
		@DisplayName("(ST) Add first #2")
		@Test
		public void addFirst_2() {
			assertTrue(path.add(s1));
			assertFalse(path.isEmpty());
			assertEquals(1, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
		
		@DisplayName("(ST) Add first #3")
		@Test
		public void addFirst_3() {
			path.add(s1);
			assertTrue(path.add(s2));
			assertFalse(path.isEmpty());
			assertEquals(2, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s2, path.getSecondSegment());
			assertSame(s1, path.getAntepenulvianSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
		
		@DisplayName("(ST) Add first #4")
		@Test
		public void addFirst_4() {
			path.add(s1);
			path.add(s2);
			assertFalse(path.add(s5));
		}
		
		@DisplayName("(ST) Add first #5")
		@Test
		public void addFirst_5() {
			path.add(s1);
			path.add(s2);
			assertTrue(path.add(s1));
			assertFalse(path.isEmpty());
			assertEquals(3, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s1, path.getAntepenulvianSegment());
			assertSame(s2, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
		
		@DisplayName("(ST) Add first #6")
		@Test
		public void addFirst_6() {
			path.add(s1);
			path.add(s2);
			path.add(s1);
			assertTrue(path.add(s3));
			assertFalse(path.isEmpty());
			assertEquals(4, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s2, path.getAntepenulvianSegment());
			assertSame(s3, path.getLastSegment());
			assertSame(c2, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}
		
		@DisplayName("(ST) Add first #7")
		@Test
		public void addFirst_7() {
			path.add(s1);
			path.add(s2);
			path.add(s1);
			path.add(s3);
			assertTrue(path.add(s1));
			assertFalse(path.isEmpty());
			assertEquals(5, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s2, path.getAntepenulvianSegment());
			assertSame(s3, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}
		
		@DisplayName("(ST) Add first #8")
		@Test
		public void addFirst_8() {
			path.add(s1);
			path.add(s2);
			path.add(s1);
			path.add(s3);
			path.add(s1);
			assertTrue(path.add(s5));
			assertFalse(path.isEmpty());
			assertEquals(6, path.size());
			assertSame(s1, path.getFirstSegment());
			assertSame(s1, path.getSecondSegment());
			assertSame(s3, path.getAntepenulvianSegment());
			assertSame(s5, path.getLastSegment());
			assertSame(c1, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
	}

	@DisplayName("pointIterator")
	@Nested
	public class PointIterator extends AbstractBaseTestCase {

		private Iterator<ConnectionStub> iterator;

		@BeforeEach
		public void setUp() throws Exception {
			super.setUp();
			assertTrue(path.add(s1));
			assertTrue(path.add(s2));
			assertTrue(path.add(s3));
			assertTrue(path.add(s5));		
			assertTrue(path.add(s5));
			iterator = path.pointIterator();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(iterator.hasNext());
			assertSame(c1, iterator.next());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			for (int i = 1; i < 2; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c2, iterator.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			for (int i = 1; i < 3; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c3, iterator.next());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			for (int i = 1; i < 4; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c4, iterator.next());
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			for (int i = 1; i < 5; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c5, iterator.next());
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			for (int i = 1; i < 6; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c4, iterator.next());
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			for (int i = 1; i < 7; ++i) {
				iterator.next();
			}
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("points")
	@Nested
	public class Points extends AbstractBaseTestCase {

		private Iterable<ConnectionStub> iterable;
		private Iterator<ConnectionStub> iterator;

		@BeforeEach
		public void setUp() throws Exception {
			super.setUp();
			assertTrue(path.add(s1));
			assertTrue(path.add(s2));
			assertTrue(path.add(s3));
			assertTrue(path.add(s5));		
			assertTrue(path.add(s5));
			iterable = path.points();
			iterator = iterable.iterator();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(iterator.hasNext());
			assertSame(c1, iterator.next());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			for (int i = 1; i < 2; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c2, iterator.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			for (int i = 1; i < 3; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c3, iterator.next());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			for (int i = 1; i < 4; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c4, iterator.next());
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			for (int i = 1; i < 5; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c5, iterator.next());
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			for (int i = 1; i < 6; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(c4, iterator.next());
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			for (int i = 1; i < 7; ++i) {
				iterator.next();
			}
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("addAll")
	@Nested
	public class AddAll extends AbstractBaseTestCase {

		private List<SegmentStub> collection;

		@BeforeEach
		public void setUp() throws Exception {
			super.setUp();
			collection = new ArrayList<>();
			collection.add(s1); // c1 - 1 - c2
			collection.add(s4); // c2 - 1 4 - c3
			collection.add(s5); // c2 - 1 4 - c3
			collection.add(s1); // c1 - 1 1 4 - c3
			collection.add(s2); // c1 - 1 1 4 2 - c2
			collection.add(s5); // c1 - 1 1 4 2 - c2
			collection.add(s1); // c1 - 1 1 4 2 1 - c1
			collection.add(s1); // c1 - 1 1 4 2 1 1 - c2
		}
		
		@DisplayName("Not reversable")
		@Test
		public void addAllCollection_notReversable() {
			path.setFirstSegmentReversable(false);
			assertTrue(path.addAll(collection));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("Reversable")
		@Test
		public void addAllCollection_reversable() {
			path.setFirstSegmentReversable(true);
			assertTrue(path.addAll(collection));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("retainAll")
	@Nested
	public class RetainAll extends AbstractBaseTestCase {
		
		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		@DisplayName("Not reversable #1")
		@Test
		public void retainAllCollection_notReversable_1() {
			path.setFirstSegmentReversable(false);
			initPath();
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("Not reversable #2")
		@Test
		public void retainAllCollection_notReversable_2() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertTrue(path.retainAll(Arrays.asList(s2, s3)));
			// c5 - 5 3 4 1 2 2 - c2
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c4, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
	
		@DisplayName("Reversable #1")
		@Test
		public void retainAllCollection_reversable_1() {
			path.setFirstSegmentReversable(true);
			initPath();
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("Reversable #2")
		@Test
		public void retainAllCollection_reversable_2() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertTrue(path.retainAll(Arrays.asList(s2, s3)));
			
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c3, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear extends AbstractBaseTestCase {

		@BeforeEach
		public void setUp() throws Exception {
			super.setUp();
			path.add(s1);
			path.add(s4);
			path.add(s2);
			path.add(s1);
			path.add(s1);
		}

		@DisplayName("#1")
		@Test
		public void clear_1() {
			assertFalse(path.isEmpty());
		}

		@DisplayName("#2")
		@Test
		public void clear_2() {
			path.clear();
			assertTrue(path.isEmpty());
		}
	}

	@DisplayName("removeAll")
	@Nested
	public class RemoveAll extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		@DisplayName("Not reversable")
		@Test
		public void removeAllCollection_notReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
			
			assertTrue(path.removeAll(Arrays.asList(s2, s3)));
			// c5 - 5 - c4
			
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c5, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}
	
		@DisplayName("Reversable")
		@Test
		public void removeAllCollection_reversable() {
			path.setFirstSegmentReversable(true);
			initPath();
			
			assertTrue(path.removeAll(Arrays.asList(s2, s3)));
			
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
	}

	@DisplayName("get")
	@Nested
	public class Get extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		@DisplayName("Not reversable #1")
		@Test
		public void getInteger_notReversable_1() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertSame(s5, path.get(0));
		}

		@DisplayName("Not reversable #2")
		@Test
		public void getInteger_notReversable_2() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertSame(s3, path.get(1));
		}

		@DisplayName("Not reversable #3")
		@Test
		public void getInteger_notReversable_3() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertSame(s4, path.get(2));
		}

		@DisplayName("Not reversable #4")
		@Test
		public void getInteger_notReversable_4() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertSame(s1, path.get(3));
		}

		@DisplayName("Not reversable #5")
		@Test
		public void getInteger_notReversable_5() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertSame(s2, path.get(4));
		}

		@DisplayName("Not reversable #6")
		@Test
		public void getInteger_notReversable_6() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertSame(s2, path.get(5));
		}

		@DisplayName("Not reversable #7")
		@Test
		public void getInteger_notReversable_7() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertThrows(Exception.class, () -> path.get(6));
		}
	
		@DisplayName("Reversable #1")
		@Test
		public void getInteger_reversable_1() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s1, path.get(0));
		}
		
		@DisplayName("Reversable #2")
		@Test
		public void getInteger_reversable_2() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s4, path.get(1));
		}
		
		@DisplayName("Reversable #3")
		@Test
		public void getInteger_reversable_3() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s2, path.get(2));
		}
		
		@DisplayName("Reversable #4")
		@Test
		public void getInteger_reversable_4() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s1, path.get(3));
		}
		
		@DisplayName("Reversable #5")
		@Test
		public void getInteger_reversable_5() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s1, path.get(4));
		}
		
		@DisplayName("Reversable #6")
		@Test
		public void getInteger_reversable_6() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s2, path.get(5));
		}
		
		@DisplayName("Reversable #7")
		@Test
		public void getInteger_reversable_7() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s3, path.get(6));
		}
		
		@DisplayName("Reversable #8")
		@Test
		public void getInteger_reversable_8() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s5, path.get(7));
		}
		
		@DisplayName("Reversable #9")
		@Test
		public void getInteger_reversable_9() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertThrows(Exception.class, () -> path.get(8));
		}
	}

	@DisplayName("set")
	@Nested
	public class Set extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		@DisplayName("(int,ST) Not reversable")
		@Test
		public void setIntegerST_notReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
			assertSame(s4, path.set(2, s3));
			// c5 - 5 3 3 - c4
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c5, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}
	
		@DisplayName("(int,ST) Reversable")
		@Test
		public void setIntegerST_reversable() {
			path.setFirstSegmentReversable(true);
			initPath();
			assertSame(s2, path.set(2, s3));
	
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c2, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(int) - Middle - Not reversable")
		@Test
		public void removeInteger_InMiddle_notReversable() {
			initNotReversable();
			assertSame(s1, path.remove(3));
	
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c5, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}
	
		@DisplayName("(int) - Middle - Reversable")
		@Test
		public void removeInteger_InMiddle_reversable() {
			initReversable();
			assertSame(s1, path.remove(3));
	
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
	
		@DisplayName("(int) - First - Not reversable")
		@Test
		public void removeInteger_First_notReversable() {
			initNotReversable();
			assertSame(s5, path.remove(0));
	
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c4, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
	
		@DisplayName("(int) - First - Reversable")
		@Test
		public void removeInteger_First_reversable() {
			initReversable();
			assertSame(s1, path.remove(0));
	
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c1, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
	
		@DisplayName("(int) - Last - Not reversable")
		@Test
		public void removeInteger_Last_notReversable() {
			initNotReversable();
			assertSame(s2, path.remove(5));
	
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c5, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
	
		@DisplayName("(int) - Last - Reversable")
		@Test
		public void removeInteger_Last_reversable() {
			initReversable();
			assertSame(s5, path.remove(7));
	
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertFalse(iterator.hasNext());
			
			assertSame(c2, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}
	}

	@DisplayName("indexOf")
	@Nested
	public class IndexOf extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("Not reversable #1")
		@Test
		public void indexOfObject_notReversable_1() {
			initNotReversable();
			assertEquals(3, path.indexOf(s1));
		}

		@DisplayName("Not reversable #2")
		@Test
		public void indexOfObject_notReversable_2() {
			initNotReversable();
			assertEquals(4, path.indexOf(s2));
		}

		@DisplayName("Not reversable #3")
		@Test
		public void indexOfObject_notReversable_3() {
			initNotReversable();
			assertEquals(1, path.indexOf(s3));
		}

		@DisplayName("Not reversable #4")
		@Test
		public void indexOfObject_notReversable_4() {
			initNotReversable();
			assertEquals(2, path.indexOf(s4));
		}

		@DisplayName("Not reversable #5")
		@Test
		public void indexOfObject_notReversable_5() {
			initNotReversable();
			assertEquals(0, path.indexOf(s5));
		}

		@DisplayName("Not reversable #6")
		@Test
		public void indexOfObject_notReversable_6() {
			initNotReversable();
			assertEquals(-1, path.indexOf(s6));
		}
	
		@DisplayName("Reversable #1")
		@Test
		public void indexOfObject_reversable_() {
			initReversable();
			assertEquals(0, path.indexOf(s1));
		}
		
		@DisplayName("Reversable #2")
		@Test
		public void indexOfObject_reversable_2() {
			initReversable();
			assertEquals(2, path.indexOf(s2));
		}
		
		@DisplayName("Reversable #3")
		@Test
		public void indexOfObject_reversable_3() {
			initReversable();
			assertEquals(6, path.indexOf(s3));
		}
		
		@DisplayName("Reversable #4")
		@Test
		public void indexOfObject_reversable_4() {
			initReversable();
			assertEquals(1, path.indexOf(s4));
		}
		
		@DisplayName("Reversable #5")
		@Test
		public void indexOfObject_reversable_5() {
			initReversable();
			assertEquals(7, path.indexOf(s5));
		}
		
		@DisplayName("Reversable #6")
		@Test
		public void indexOfObject_reversable_6() {
			initReversable();
			assertEquals(-1, path.indexOf(s6));
		}
	}

	@DisplayName("lastIndexOf")
	@Nested
	public class LastIndexOf extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("Not reversable #1")
		@Test
		public void lastIndexOfObject_notReversable_1() {
			initNotReversable();
			assertEquals(3, path.lastIndexOf(s1));
		}

		@DisplayName("Not reversable #2")
		@Test
		public void lastIndexOfObject_notReversable_2() {
			initNotReversable();
			assertEquals(5, path.lastIndexOf(s2));
		}

		@DisplayName("Not reversable #3")
		@Test
		public void lastIndexOfObject_notReversable_3() {
			initNotReversable();
			assertEquals(1, path.lastIndexOf(s3));
		}

		@DisplayName("Not reversable #4")
		@Test
		public void lastIndexOfObject_notReversable_4() {
			initNotReversable();
			assertEquals(2, path.lastIndexOf(s4));
		}

		@DisplayName("Not reversable #5")
		@Test
		public void lastIndexOfObject_notReversable_5() {
			initNotReversable();
			assertEquals(0, path.lastIndexOf(s5));
		}

		@DisplayName("Not reversable #6")
		@Test
		public void lastIndexOfObject_notReversable_6() {
			initNotReversable();
			assertEquals(-1, path.lastIndexOf(s6));
		}
	
		@DisplayName("Reversable #1")
		@Test
		public void lastIndexOfObject_reversable_1() {
			initReversable();
			assertEquals(4, path.lastIndexOf(s1));
		}
		
		@DisplayName("Reversable #2")
		@Test
		public void lastIndexOfObject_reversable_2() {
			initReversable();
			assertEquals(5, path.lastIndexOf(s2));
		}
		
		@DisplayName("Reversable #3")
		@Test
		public void lastIndexOfObject_reversable_3() {
			initReversable();
			assertEquals(6, path.lastIndexOf(s3));
		}
		
		@DisplayName("Reversable #4")
		@Test
		public void lastIndexOfObject_reversable_4() {
			initReversable();
			assertEquals(1, path.lastIndexOf(s4));
		}
		
		@DisplayName("Reversable #5")
		@Test
		public void lastIndexOfObject_reversable_5() {
			initReversable();
			assertEquals(7, path.lastIndexOf(s5));
		}
		
		@DisplayName("Reversable #6")
		@Test
		public void lastIndexOfObject_reversable_6() {
			initReversable();
			assertEquals(-1, path.lastIndexOf(s6));
		}
	}

	@DisplayName("listIterator")
	@Nested
	public class ListIterator extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}
		
		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("() Not reversable")
		@Test
		public void listIterator_notReversable() {
			initNotReversable();
			var iterator = path.listIterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("() Reversable")
		@Test
		public void listIterator_reversable() {
			initReversable();
			var iterator = path.listIterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("(int) Not reversable #1")
		@Test
		public void listIteratorInteger_notReversable_1() {
			initNotReversable();
			var iterator = path.listIterator(0);
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Not reversable #2")
		@Test
		public void listIteratorInteger_notReversable_2() {
			initNotReversable();
			var iterator = path.listIterator(1);
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Not reversable #3")
		@Test
		public void listIteratorInteger_notReversable_3() {
			initNotReversable();
			var iterator = path.listIterator(2);
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Not reversable #4")
		@Test
		public void listIteratorInteger_notReversable_4() {
			initNotReversable();
			var iterator = path.listIterator(3);
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Not reversable #5")
		@Test
		public void listIteratorInteger_notReversable_5() {
			initNotReversable();
			var iterator = path.listIterator(4);
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Not reversable #6")
		@Test
		public void listIteratorInteger_notReversable_6() {
			initNotReversable();
			var iterator = path.listIterator(5);
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Not reversable #7")
		@Test
		public void listIteratorInteger_notReversable_7() {
			initNotReversable();
			var iterator = path.listIterator(6);
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("(int) Reversable #1")
		@Test
		public void listIteratorInteger_reversable_1() {
			initReversable();
			var iterator = path.listIterator(0);
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #2")
		@Test
		public void listIteratorInteger_reversable_2() {
			initReversable();
			var iterator = path.listIterator(1);
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #3")
		@Test
		public void listIteratorInteger_reversable_3() {
			initReversable();
			var iterator = path.listIterator(2);
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #4")
		@Test
		public void listIteratorInteger_reversable_4() {
			initReversable();
			var iterator = path.listIterator(3);
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #5")
		@Test
		public void listIteratorInteger_reversable_5() {
			initReversable();
			var iterator = path.listIterator(4);
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #6")
		@Test
		public void listIteratorInteger_reversable_6() {
			initReversable();
			var iterator = path.listIterator(5);
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #7")
		@Test
		public void listIteratorInteger_reversable_7() {
			initReversable();
			var iterator = path.listIterator(6);
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #8")
		@Test
		public void listIteratorInteger_reversable_8() {
			initReversable();
			var iterator = path.listIterator(7);
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int) Reversable #9")
		@Test
		public void listIteratorInteger_reversable_9() {
			initReversable();
			var iterator = path.listIterator(8);
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("subList")
	@Nested
	public class SubList extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(int,int) Not reversable #1")
		@Test
		public void subListIntegerInteger_notReversable_1() {
			initNotReversable();
			List<SegmentStub> subList = path.subList(0, 4);
			Iterator<SegmentStub> iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(int,int) Not reversable #2")
		@Test
		public void subListIntegerInteger_notReversable_2() {
			initNotReversable();
			var subList = path.subList(1, 4);
			var iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(int,int) Not reversable #3")
		@Test
		public void subListIntegerInteger_notReversable_3() {
			initNotReversable();
			var subList = path.subList(2, 4);
			var iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(int,int) Not reversable #4")
		@Test
		public void subListIntegerInteger_notReversable_4() {
			initNotReversable();
			var subList = path.subList(3, 4);
			var iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(int,int) Not reversable #5")
		@Test
		public void subListIntegerInteger_notReversable_5() {
			initNotReversable();
			var subList = path.subList(4, 4);
			var iterator = subList.iterator();
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("(int,int) Reversable #1")
		@Test
		public void subListIntegerInteger_reversable_1() {
			initReversable();
			List<SegmentStub> subList = path.subList(0, 4);
			// c2 - 1 4 2 1 - c1
			Iterator<SegmentStub> iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int,int) Reversable #2")
		@Test
		public void subListIntegerInteger_reversable_2() {
			initReversable();
			var subList = path.subList(1, 4);
			// c1 - 4 2 1 - c1
			var iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int,int) Reversable #3")
		@Test
		public void subListIntegerInteger_reversable_3() {
			initReversable();
			var subList = path.subList(2, 4);
			// c3 - 2 1 - c1
			var iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int,int) Reversable #4")
		@Test
		public void subListIntegerInteger_reversable_4() {
			initReversable();
			var subList = path.subList(3, 4);
			// c2 - 1 - c1
			var iterator = subList.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
		}
		
		@DisplayName("(int,int) Reversable #5")
		@Test
		public void subListIntegerInteger_reversable_5() {
			initReversable();
			var subList = path.subList(4, 4);
			var iterator = subList.iterator();
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("removeBefore")
	@Nested
	public class RemoveBefore extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeBeforeST_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeBefore(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeBeforeST_notReversable_2() {
			initNotReversable();
			assertFalse(path.removeBefore(s5));
		}

		@DisplayName("(ST) Not reversable #3")
		@Test
		public void removeBeforeST_notReversable_3() {
			initNotReversable();
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #4")
		@Test
		public void removeBeforeST_notReversable_4() {
			initNotReversable();
			assertTrue(path.removeBefore(s2));
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #5")
		@Test
		public void removeBeforeST_notReversable_5() {
			initNotReversable();
			path.removeBefore(s2);
			assertFalse(path.removeBefore(s5));
		}

		@DisplayName("(ST) Not reversable #6")
		@Test
		public void removeBeforeST_notReversable_6() {
			initNotReversable();
			path.removeBefore(s2);
			assertFalse(path.removeBefore(s2));
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeBeforeST_reversable_1() {
			initReversable();
			assertFalse(path.removeBefore(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeBeforeST_reversable_2() {
			initReversable();
			assertFalse(path.removeBefore(s1));
		}
		
		@DisplayName("(ST) Reversable #3")
		@Test
		public void removeBeforeST_reversable_3() {
			initReversable();
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #4")
		@Test
		public void removeBeforeST_reversable_4() {
			initReversable();
			assertTrue(path.removeBefore(s2));
			// c3 - 2 1 1 2 3 5 - c5
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #5")
		@Test
		public void removeBeforeST_reversable_5() {
			initReversable();
			path.removeBefore(s2);
			assertFalse(path.removeBefore(s2));
		}
		
		@DisplayName("(ST) Reversable #6")
		@Test
		public void removeBeforeST_reversable_6() {
			initReversable();
			path.removeBefore(s2);
			assertFalse(path.removeBefore(s4));
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeBeforeSTPT_notReversable_1() {
			initNotReversable();			
			assertFalse(path.removeBefore(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeBeforeSTPT_notReversable_2() {
			initNotReversable();			
			assertFalse(path.removeBefore(s1, c2));
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void removeBeforeSTPT_notReversable_3() {
			initNotReversable();			
			assertTrue(path.removeBefore(s1,c1));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c1, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeBeforeSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeBefore(s6, c6));
		}

		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeBeforeSTPT_reversable_2() {
			initReversable();
			assertFalse(path.removeBefore(s4, c3));
		}

		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void removeBeforeSTPT_reversable_3() {
			initReversable();
			assertTrue(path.removeBefore(s1,c1));
			// c1 - 1 2 3 5 - c5
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c1, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
	}

	@DisplayName("removeUntil")
	@Nested
	public class RemoveUntil extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeUntilST_notReversable_1() {
			initNotReversable();			
			assertFalse(path.removeUntil(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeUntilST_notReversable_2() {
			initNotReversable();			
			assertTrue(path.removeUntil(s2));
			
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #3")
		@Test
		public void removeUntilST_notReversable_3() {
			initNotReversable();
			path.removeUntil(s2);
			assertTrue(path.removeUntil(s2));
			var iterator = path.iterator();
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeUntilST_reversable_1() {
			initReversable();			
			assertFalse(path.removeUntil(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeUntilST_reversable_2() {
			initReversable();			
			assertTrue(path.removeUntil(s2));
			// c2 - 1 1 2 3 5 - c5
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #3")
		@Test
		public void removeUntilST_reversable_3() {
			initReversable();			
			path.removeUntil(s2);
			assertTrue(path.removeUntil(s2));
			// c3 - 3 5 - c5
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeUntilSTPT_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeUntil(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeUntilSTPT_notReversable_2() {
			initNotReversable();
			assertFalse(path.removeUntil(s1, c2));
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void removeUntilSTPT_notReversable_3() {
			initNotReversable();
			assertTrue(path.removeUntil(s1,c1));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeUntilSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeUntil(s6, c6));
		}

		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeUntilSTPT_reversable_2() {
			initReversable();
			assertFalse(path.removeUntil(s4, c3));
		}

		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void removeUntilSTPT_reversable_3() {
			initReversable();
			assertTrue(path.removeUntil(s1,c1));
			// c2 - 2 3 5 - c5
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
	}

	@DisplayName("removeBeforeLast")
	@Nested
	public class RemoveBeforeLast extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeBeforeLastST_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeBeforeLast(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeBeforeLastST_notReversable_2() {
			initNotReversable();
			assertTrue(path.removeBeforeLast(s2));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #3")
		@Test
		public void removeBeforeLastST_notReversable_3() {
			initNotReversable();
			path.removeBeforeLast(s2);
			assertFalse(path.removeBeforeLast(s2));
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeBeforeLastST_reversable_1() {
			initReversable();
			assertFalse(path.removeBeforeLast(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeBeforeLastST_reversable_2() {
			initReversable();
			assertTrue(path.removeBeforeLast(s2));
			// c2 - 2 3 5 - c5
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #3")
		@Test
		public void removeBeforeLastST_reversable_3() {
			initReversable();
			path.removeBeforeLast(s2);
			assertFalse(path.removeBeforeLast(s2));
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeBeforeLastSTPT_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeBeforeLast(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeBeforeLastSTPT_notReversable_2() {
			initNotReversable();
			assertFalse(path.removeBeforeLast(s1, c2));
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void removeBeforeLastSTPT_notReversable_3() {
			initNotReversable();
			assertTrue(path.removeBeforeLast(s1,c1));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c1, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeBeforeLastSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeBeforeLast(s6, c6));
		}

		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeBeforeLastSTPT_reversable_2() {
			initReversable();
			assertFalse(path.removeBeforeLast(s4, c3));
		}

		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void removeBeforeLastSTPT_reversable_3() {
			initReversable();
			assertTrue(path.removeBeforeLast(s1,c1));
			// c1 - 1 2 3 5 - c5
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c1, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
	}

	@DisplayName("removeUntilLast")
	@Nested
	public class RemoveUntilLast extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeUntilLastST_notReversable_1() {
			initNotReversable();			
			assertFalse(path.removeUntilLast(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeUntilLastST_notReversable_2() {
			initNotReversable();			
			assertTrue(path.removeUntilLast(s2));
			Iterator<SegmentStub> iterator = path.iterator();
			assertFalse(iterator.hasNext());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeUntilLastST_reversable_1() {
			initReversable();
			assertFalse(path.removeUntilLast(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeUntilLastST_reversable_2() {
			initReversable();
			assertTrue(path.removeUntilLast(s2));
			// c3 - 3 5 - c5
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeUntilLastSTPT_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeUntilLast(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeUntilLastSTPT_notReversable_2() {
			initNotReversable();
			assertFalse(path.removeUntilLast(s1, c2));
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void removeUntilLastSTPT_notReversable_3() {
			initNotReversable();
			assertTrue(path.removeUntilLast(s1,c1));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
			
		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeUntilLastSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeUntilLast(s6, c6));
		}
		
		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeUntilLastSTPT_reversable_2() {
			initReversable();
			assertFalse(path.removeUntilLast(s4, c3));
		}
		
		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void removeUntilLastSTPT_reversable_3() {
			initReversable();
			assertTrue(path.removeUntilLast(s1,c1));
			// c2 - 2 3 5 - c5
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}
	}

	@DisplayName("invert")
	@Nested
	public class Invert extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
			path.invert();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
			path.invert();
		}

		@DisplayName("Not reversable #1")
		@Test
		public void invert_notReversable_1() {
			initNotReversable();
			assertSame(s2, path.get(0));
		}

		@DisplayName("Not reversable #2")
		@Test
		public void invert_notReversable_2() {
			initNotReversable();
			assertSame(s2, path.get(1));
		}

		@DisplayName("Not reversable #3")
		@Test
		public void invert_notReversable_3() {
			initNotReversable();
			assertSame(s1, path.get(2));
		}

		@DisplayName("Not reversable #4")
		@Test
		public void invert_notReversable_4() {
			initNotReversable();
			assertSame(s4, path.get(3));
		}

		@DisplayName("Not reversable #5")
		@Test
		public void invert_notReversable_5() {
			initNotReversable();
			assertSame(s3, path.get(4));
		}

		@DisplayName("Not reversable #6")
		@Test
		public void invert_notReversable_6() {
			initNotReversable();
			assertSame(s5, path.get(5));
		}
	
		@DisplayName("Reversable #1")
		@Test
		public void invert_reversable_1() {
			initReversable();	
			assertSame(s5, path.get(0));
		}
		
		@DisplayName("Reversable #2")
		@Test
		public void invert_reversable_2() {
			initReversable();	
			assertSame(s3, path.get(1));
		}
		
		@DisplayName("Reversable #3")
		@Test
		public void invert_reversable_3() {
			initReversable();	
			assertSame(s2, path.get(2));
		}
		
		@DisplayName("Reversable #4")
		@Test
		public void invert_reversable_4() {
			initReversable();	
			assertSame(s1, path.get(3));
		}
		
		@DisplayName("Reversable #5")
		@Test
		public void invert_reversable_5() {
			initReversable();	
			assertSame(s1, path.get(4));
		}
		
		@DisplayName("Reversable #6")
		@Test
		public void invert_reversable_6() {
			initReversable();	
			assertSame(s2, path.get(5));
		}
		
		@DisplayName("Reversable #7")
		@Test
		public void invert_reversable_7() {
			initReversable();	
			assertSame(s4, path.get(6));
		}
		
		@DisplayName("Reversable #8")
		@Test
		public void invert_reversable_8() {
			initReversable();	
			assertSame(s1, path.get(7));
		}
	}

	@DisplayName("removeAfter")
	@Nested
	public class RemoveAfter extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeAfterST_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeAfter(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeAfterST_notReversable_2() {
			initNotReversable();
			assertTrue(path.removeAfter(s2));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #3")
		@Test
		public void removeAfterST_notReversable_3() {
			initNotReversable();
			path.removeAfter(s2);
			assertFalse(path.removeAfter(s2));
		}

		@DisplayName("(ST) Not reversable #4")
		@Test
		public void removeAfterST_notReversable_4() {
			initNotReversable();
			assertTrue(path.removeAfter(s1));
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeAfterST_reversable_1() {
			initReversable();
			assertFalse(path.removeAfter(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeAfterST_reversable_2() {
			initReversable();
			assertTrue(path.removeAfter(s2));
			// c2 - 1 4 2 - c2
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #3")
		@Test
		public void removeAfterST_reversable_3() {
			initReversable();
			path.removeAfter(s2);
			assertFalse(path.removeAfter(s2));
			// c2 - 1 4 2 - c2
		}
		
		@DisplayName("(ST) Reversable #4")
		@Test
		public void removeAfterST_reversable_4() {
			initReversable();
			assertTrue(path.removeAfter(s1));
			// c2 - 1 - c1
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeAfterSTPT_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeAfter(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeAfterSTPT_notReversable_2() {
			initNotReversable();
			assertFalse(path.removeAfter(s2, c3));
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void removeAfterSTPT_notReversable_3() {
			initNotReversable();
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #4")
		@Test
		public void removeAfterSTPT_notReversable_4() {
			initNotReversable();
			assertTrue(path.removeAfter(s2, c2));
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #5")
		@Test
		public void removeAfterSTPT_notReversable_5() {
			initNotReversable();
			path.removeAfter(s2, c2);
			assertFalse(path.removeAfter(s5,c4));
		}

		@DisplayName("(ST,PT) Not reversable #6")
		@Test
		public void removeAfterSTPT_notReversable_6() {
			initNotReversable();
			path.removeAfter(s2, c2);
			assertFalse(path.removeAfter(s5,c4));
		}

		@DisplayName("(ST,PT) Not reversable #7")
		@Test
		public void removeAfterSTPT_notReversable_7() {
			initNotReversable();
			path.removeAfter(s2, c2);
			assertTrue(path.removeAfter(s5,c5));
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #8")
		@Test
		public void removeAfterSTPT_notReversable_8() {
			initNotReversable();
			path.removeAfter(s2, c2);
			path.removeAfter(s5,c5);
			assertFalse(path.removeAfter(s5, c5));
		}

		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeAfterSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeAfter(s6, c6));
		}

		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeAfterSTPT_reversable_2() {
			initReversable();
			assertFalse(path.removeAfter(s4, c3));
		}

		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void removeAfterSTPT_reversable_3() {
			initReversable();
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c5, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Reversable #4")
		@Test
		public void removeAfterSTPT_reversable_4() {
			initReversable();
			assertTrue(path.removeAfter(s2, c2));
			// c2 - 1 4 2 1 1 2 - c3
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Reversable #5")
		@Test
		public void removeAfterSTPT_reversable_5() {
			initReversable();
			path.removeAfter(s2, c2);
			assertFalse(path.removeAfter(s5,c4));
			// c2 - 1 4 2 1 1 2 - c3
		}

		@DisplayName("(ST,PT) Reversable #6")
		@Test
		public void removeAfterSTPT_reversable_6() {
			initReversable();
			path.removeAfter(s2, c2);
			assertFalse(path.removeAfter(s2,c2));
			// c2 - 1 4 2 1 1 2 - c3
		}
	}

	@DisplayName("removeFrom")
	@Nested
	public class RemoveFrom extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeFromST_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeFrom(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeFromST_notReversable_2() {
			initNotReversable();
			assertTrue(path.removeFrom(s2));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #3")
		@Test
		public void removeFromST_notReversable_3() {
			initNotReversable();
			path.removeFrom(s2);
			assertFalse(path.removeFrom(s6));
		}

		@DisplayName("(ST) Not reversable #4")
		@Test
		public void removeFromST_notReversable_4() {
			initNotReversable();
			path.removeFrom(s2);
			assertFalse(path.removeFrom(s2));
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeFromST_reversable_1() {
			initReversable();
			assertFalse(path.removeFrom(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeFromST_reversable_2() {
			initReversable();
			assertTrue(path.removeFrom(s2));
			// c2 - 1 4 - c3
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #3")
		@Test
		public void removeFromST_reversable_3() {
			initReversable();
			path.removeFrom(s2);
			assertFalse(path.removeFrom(s2));
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeFromSTPT_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeFrom(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeFromSTPT_notReversable_2() {
			initNotReversable();
			assertTrue(path.removeFrom(s2, c3));
			// c5 - 5 3 4 1 2 - c3
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void removeFromSTPT_notReversable_3() {
			initNotReversable();
			path.removeFrom(s2, c3);
			assertFalse(path.removeFrom(s2, c3));
			// c5 - 5 3 4 1 2 - c3
		}

		@DisplayName("(ST,PT) Not reversable #4")
		@Test
		public void removeFromSTPT_notReversable_4() {
			initNotReversable();
			assertFalse(path.removeFrom(s5,c4));
			// c5 - 5 3 4 1 2 - c3
		}

		@DisplayName("(ST,PT) Not reversable #5")
		@Test
		public void removeFromSTPT_notReversable_5() {
			initNotReversable();
			assertFalse(path.removeFrom(s5,c4));
			// c5 - 5 3 4 1 2 - c3
		}

		@DisplayName("(ST,PT) Not reversable #6")
		@Test
		public void removeFromSTPT_notReversable_6() {
			initNotReversable();
			path.removeFrom(s2, c3);
			assertTrue(path.removeFrom(s5,c5));
			// c5 - 5 3 4 1 2 - c3
			var iterator = path.iterator();
			assertFalse(iterator.hasNext());
			assertNull(path.getFirstPoint());
			assertNull(path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #7")
		@Test
		public void removeFromSTPT_notReversable_7() {
			initNotReversable();
			path.removeFrom(s2, c3);
			path.removeFrom(s5,c5);
			assertFalse(path.removeFrom(s5,c5));
		}

		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeFromSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeFrom(s6, c6));
		}

		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeFromSTPT_reversable_2() {
			initReversable();
			assertTrue(path.removeFrom(s2, c3));
			// c2 - 1 4 - c3
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
	}

	@DisplayName("removeAfterLast")
	@Nested
	public class RemoveAfterLast extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeAfterLastST_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeAfterLast(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeAfterLastST_notReversable_2() {
			initNotReversable();
			assertFalse(path.removeAfterLast(s2));
		}

		@DisplayName("(ST) Not reversable #3")
		@Test
		public void removeAfterLastST_notReversable_3() {
			initNotReversable();
			assertTrue(path.removeAfterLast(s1));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeAfterLastST_reversable_1() {
			initReversable();
			assertFalse(path.removeAfterLast(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeAfterLastST_reversable_2() {
			initReversable();
			assertFalse(path.removeAfterLast(s5));
		}
		
		@DisplayName("(ST) Reversable #3")
		@Test
		public void removeAfterLastST_reversable_3() {
			initReversable();
			assertTrue(path.removeAfterLast(s1));
			// c2 - 1 4 2 1 1 - c2
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeAfterLastSTPT_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeAfterLast(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeAfterLastSTPT_notReversable_2() {
			initNotReversable();
			assertFalse(path.removeAfterLast(s2, c3));
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void removeAfterLastSTPT_notReversable_3() {
			initNotReversable();
			assertTrue(path.removeAfterLast(s2, c2));
			// c5 - 5 3 4 1 2 - c3
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeAfterLastSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeAfterLast(s6, c6));
		}

		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeAfterLastSTPT_reversable_2() {
			initReversable();
			assertTrue(path.removeAfterLast(s1, c2));
			// c2 - 1 4 2 1 - c1
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}
	}

	@DisplayName("removeFromLast")
	@Nested
	public class RemoveFromLast extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void removeFromLastST_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeFromLast(s6));
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void removeFromLastST_notReversable_2() {
			initNotReversable();
			assertTrue(path.removeFromLast(s2));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void removeFromLastST_reversable_1() {
			initReversable();
			assertFalse(path.removeFromLast(s6));
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void removeFromLastST_reversable_2() {
			initReversable();
			assertTrue(path.removeFromLast(s2));
			// c2 - 1 4 2 1 1 - c2
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void removeFromLastSTPT_notReversable_1() {
			initNotReversable();
			assertFalse(path.removeFromLast(s6, c6));
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void removeFromLastSTPT_notReversable_2() {
			initNotReversable();
			assertTrue(path.removeFromLast(s2, c3));
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
		}

		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void removeFromLastSTPT_reversable_1() {
			initReversable();
			assertFalse(path.removeFromLast(s6, c6));
		}

		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void removeFromLastSTPT_reversable_2() {
			initReversable();
			assertTrue(path.removeFromLast(s1, c2));
			// c2 - 1 4 2 - c2
			Iterator<SegmentStub> iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
		}
	}

	@DisplayName("splitAfter")
	@Nested
	public class SplitAfter extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void splitAfterST_notReversable_1() {
			initNotReversable();
			var sp = path.splitAfter(s4);
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c1, sp.getFirstPoint());
			assertSame(c2, sp.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void splitAfterST_notReversable_2() {
			initNotReversable();
			path.splitAfter(s4);
			var sp = path.splitAfter(s4);
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void splitAfterST_reversable_1() {
			initReversable();
			var sp = path.splitAfter(s4);
			// c2 - 1 4 - c3
			// c3 - 2 1 1 2 3 5 - c5
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void splitAfterST_reversable_2() {
			initReversable();
			path.splitAfter(s4);
			var sp = path.splitAfter(s4);
			// c2 - 1 4 - c3
			// empty
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
	
		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void splitAfterSTPT_notReversable_1() {
			initNotReversable();
			var sp = path.splitAfter(s4, c1);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void splitAfterSTPT_notReversable_2() {
			initNotReversable();
			path.splitAfter(s4, c1);
			var sp = path.splitAfter(s5, c4);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void splitAfterSTPT_notReversable_3() {
			initNotReversable();
			path.splitAfter(s4, c1);
			path.splitAfter(s5, c4);
			var sp = path.splitAfter(s4, c3);
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
		}
	
		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void splitAfterSTPT_reversable_1() {
			initReversable();
			var sp = path.splitAfter(s4, c1);
			// c2 - 1 4 - c3
			// c3 - 2 1 1 2 3 5 - c5
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void splitAfterSTPT_reversable_2() {
			initReversable();
			path.splitAfter(s4, c1);
			var sp = path.splitAfter(s4, c1);
			// c2 - 1 4 - c3
			// empty
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
	}

	@DisplayName("splitAfterLast")
	@Nested
	public class SplitAfterLast extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void splitAfterLastST_notReversable_1() {
			initNotReversable();
			var sp = path.splitAfterLast(s2);
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void splitAfterLastST_notReversable_2() {
			initNotReversable();
			path.splitAfterLast(s2);
			var sp = path.splitAfterLast(s4);
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
	
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c1, sp.getFirstPoint());
			assertSame(c2, sp.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void splitAfterLastST_reversable_1() {
			initReversable();
			var sp = path.splitAfterLast(s2);
			// c2 - 1 4 2 1 1 2 - c3
			// c3 - 3 5 - c5
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void splitAfterLastST_reversable_2() {
			initReversable();
			path.splitAfterLast(s2);
			var sp = path.splitAfterLast(s2);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void splitAfterLastSTPT_notReversable_1() {
			initNotReversable();
			var sp = path.splitAfter(s6, c6);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void splitAfterLastSTPT_notReversable_2() {
			initNotReversable();
			path.splitAfter(s6, c6);
			var sp = path.splitAfter(s5, c4);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}

		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void splitAfterLastSTPT_notReversable_3() {
			initNotReversable();
			path.splitAfter(s6, c6);
			path.splitAfter(s5, c4);
			var sp = path.splitAfterLast(s5, c5);
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
			
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c4, sp.getFirstPoint());
			assertSame(c2, sp.getLastPoint());
		}
	
		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void splitAfterLastSTPT_reversable_1() {
			initReversable();
			var sp = path.splitAfter(s6, c6);
			// c2 - 1 4 2 1 1 2 3 5 - c5
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void splitAfterLastSTPT_reversable_2() {
			initReversable();
			path.splitAfter(s6, c6);
			var sp = path.splitAfter(s5, c4);
			// c2 - 1 4 2 1 1 2 3 5 - c5
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void splitAfterLastSTPT_reversable_3() {
			initReversable();
			path.splitAfter(s6, c6);
			path.splitAfter(s5, c4);
			var sp = path.splitAfterLast(s1, c1);
			// c2 - 1 4 2 1 1 - c2
			// c2 - 2 3 5 - c5
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
			
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
	}

	@DisplayName("splitAt")
	@Nested
	public class SplitAt extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void splitAtST_notReversable_1() {
			initNotReversable();
			var sp = path.splitAt(s1);
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, path.getFirstPoint());
			assertSame(c1, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c1, sp.getFirstPoint());
			assertSame(c2, sp.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void splitAtST_reversable_1() {
			initReversable();
			var sp = path.splitAt(s2);
			// c2 - 1 4 - c3
			// c3 - 2 1 1 2 3 5 - c5
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c3, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c3, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
	
		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void splitAtSTPT_notReversable_1() {
			initNotReversable();
			var sp = path.splitAt(s6, c6);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void splitAtSTPT_notReversable_2() {
			initNotReversable();
			path.splitAt(s6, c6);
			var sp = path.splitAt(s5, c4);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void splitAtSTPT_notReversable_3() {
			initNotReversable();
			path.splitAt(s6, c6);
			path.splitAt(s5, c4);
			var sp = path.splitAt(s5, c5);
	
			var iterator = path.iterator();
			assertFalse(iterator.hasNext());
			assertNull(path.getFirstPoint());
			assertNull(path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, sp.getFirstPoint());
			assertSame(c2, sp.getLastPoint());
		}
	
		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void splitAtSTPT_reversable_1() {
			initReversable();
			var sp = path.splitAt(s6, c6);
			// c2 - 1 4 2 1 1 2 3 5 - c5
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void splitAtSTPT_reversable_2() {
			initReversable();
			path.splitAt(s6, c6);
			var sp = path.splitAt(s5, c5);
			// c2 - 1 4 2 1 1 2 3 5 - c5
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void splitAtSTPT_reversable_3() {
			initReversable();
			path.splitAt(s6, c6);
			path.splitAt(s5, c5);
			var sp = path.splitAt(s5, c4);
			// c2 - 1 4 2 1 1 2 3 - c4
			// c4 - 5 - c5
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c4, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c4, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
	}

	@DisplayName("splitAtLast")
	@Nested
	public class SplitAtLast extends AbstractBaseTestCase {

		private void initPath() {
			path.add(s1); // c1 - 1 - c2
			path.add(s4); // c3 - 4 1 - c2
			path.add(s2); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s1); // c3 - 4 1 2 - c3
			path.add(s2); // c3 - 4 1 2 2 - c2
			path.add(s3); // c4 - 3 4 1 2 2 - c2
			path.add(s5); // c5 - 5 3 4 1 2 2 - c2
		}

		private void initNotReversable() {
			path.setFirstSegmentReversable(false);
			initPath();
		}

		private void initReversable() {
			path.setFirstSegmentReversable(true);
			initPath();
		}

		@DisplayName("(ST) Not reversable #1")
		@Test
		public void splitAtLastST_notReversable_1() {
			initNotReversable();
			var sp = path.splitAtLast(s6);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}

		@DisplayName("(ST) Not reversable #2")
		@Test
		public void splitAtLastST_notReversable_2() {
			initNotReversable();
			path.splitAtLast(s6);
			var sp = path.splitAtLast(s5);
			var iterator = path.iterator();
			assertFalse(iterator.hasNext());
			assertNull(path.getFirstPoint());
			assertNull(path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, sp.getFirstPoint());
			assertSame(c2, sp.getLastPoint());
		}
	
		@DisplayName("(ST) Reversable #1")
		@Test
		public void splitAtLastST_reversable_1() {
			initReversable();
			var sp = path.splitAtLast(s6);
			// c2 - 1 4 2 1 1 2 3 5 - c5
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST) Reversable #2")
		@Test
		public void splitAtLastST_reversable_2() {
			initReversable();
			path.splitAtLast(s6);
			var sp = path.splitAtLast(s2);
			// c2 - 1 4 2 1 1 - c2
			// c2 - 2 3 5 - c5
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
	
		@DisplayName("(ST,PT) Not reversable #1")
		@Test
		public void splitAtLastSTPT_notReversable_1() {
			initNotReversable();
			var sp = path.splitAtLast(s6, c6);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Not reversable #2")
		@Test
		public void splitAtLastSTPT_notReversable_2() {
			initNotReversable();
			path.splitAtLast(s6, c6);
			var sp = path.splitAtLast(s5, c4);
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Not reversable #3")
		@Test
		public void splitAtLastSTPT_notReversable_3() {
			initNotReversable();
			path.splitAtLast(s6, c6);
			path.splitAtLast(s5, c4);
			var sp = path.splitAtLast(s5, c5);
	
			var iterator = path.iterator();
			assertFalse(iterator.hasNext());
			assertNull(path.getFirstPoint());
			assertNull(path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c5, sp.getFirstPoint());
			assertSame(c2, sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #1")
		@Test
		public void splitAtLastSTPT_reversable_1() {
			initReversable();
			var sp = path.splitAtLast(s6, c6);
			// c2 - 1 4 2 1 1 2 3 5 - c5
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #2")
		@Test
		public void splitAtLastSTPT_reversable_2() {
			initReversable();
			path.splitAtLast(s6, c6);
			var sp = path.splitAtLast(s5, c5);
			// c2 - 1 4 2 1 1 2 3 5 - c5
			assertNotNull(sp);
			var iterator = sp.iterator();
			assertFalse(iterator.hasNext());
			assertNull(sp.getFirstPoint());
			assertNull(sp.getLastPoint());
		}
		
		@DisplayName("(ST,PT) Reversable #3")
		@Test
		public void splitAtLastSTPT_reversable_3() {
			initReversable();
			path.splitAtLast(s6, c6);
			path.splitAtLast(s5, c5);
			var sp = path.splitAtLast(s1, c2);
			// c2 - 1 4 2 - c2
			// c2 - 1 1 2 3 5 - c5
	
			var iterator = path.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s4, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, path.getFirstPoint());
			assertSame(c2, path.getLastPoint());
			
			assertNotNull(sp);
			iterator = sp.iterator();
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s1, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s2, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s3, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(s5, iterator.next());
			assertFalse(iterator.hasNext());
			assertSame(c2, sp.getFirstPoint());
			assertSame(c5, sp.getLastPoint());
		}
	}

}


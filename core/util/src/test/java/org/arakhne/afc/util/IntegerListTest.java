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

package org.arakhne.afc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@DisplayName("IntegerList")
@SuppressWarnings("all")
public class IntegerListTest {

	private IntegerList list;
	
	@BeforeEach
	public void setUp() throws Exception {
		list = new IntegerList();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(10);
		list.add(20);
		list.add(21);
		list.add(22);
		list.add(23);
		list.add(24);
		list.add(25);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		list = null;
	}

	@DisplayName("comparator")
	@Nested
	public class Comparator {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNull(list.comparator());
		}
	}

	@DisplayName("getLastValueOnSegment")
	@Nested
	public class GetLastValueOnSegment {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(3, list.getLastValueOnSegment(0));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(10, list.getLastValueOnSegment(2));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(25, list.getLastValueOnSegment(4));
		}
	}

	@DisplayName("getFirstValueOnSegment")
	@Nested
	public class GetFirstValueOnSegment {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(1, list.getFirstValueOnSegment(0));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(10, list.getFirstValueOnSegment(2));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(20, list.getFirstValueOnSegment(4));
		}
	}

	@DisplayName("getSegmentIndexFor")
	@Nested
	public class GetSegmentIndexFor {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(-1, list.getSegmentIndexFor(0));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(0, list.getSegmentIndexFor(1));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(0, list.getSegmentIndexFor(2));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(0, list.getSegmentIndexFor(3));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(-1, list.getSegmentIndexFor(4));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(-1, list.getSegmentIndexFor(5));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(-1, list.getSegmentIndexFor(6));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEquals(-1, list.getSegmentIndexFor(7));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEquals(-1, list.getSegmentIndexFor(8));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEquals(-1, list.getSegmentIndexFor(9));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEquals(2, list.getSegmentIndexFor(10));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEquals(-1, list.getSegmentIndexFor(11));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEquals(-1, list.getSegmentIndexFor(12));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertEquals(-1, list.getSegmentIndexFor(13));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertEquals(-1, list.getSegmentIndexFor(14));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertEquals(-1, list.getSegmentIndexFor(15));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertEquals(-1, list.getSegmentIndexFor(16));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertEquals(-1, list.getSegmentIndexFor(17));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertEquals(-1, list.getSegmentIndexFor(18));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertEquals(-1, list.getSegmentIndexFor(19));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertEquals(4, list.getSegmentIndexFor(20));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertEquals(4, list.getSegmentIndexFor(21));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertEquals(4, list.getSegmentIndexFor(22));
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertEquals(4, list.getSegmentIndexFor(23));
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertEquals(4, list.getSegmentIndexFor(24));
		}

		@DisplayName("#26")
		@Test
		public void test_26() {
			assertEquals(4, list.getSegmentIndexFor(25));
		}

		@DisplayName("#27")
		@Test
		public void test_27() {
			assertEquals(-1, list.getSegmentIndexFor(26));
		}

		@DisplayName("#28")
		@Test
		public void test_28() {
			assertEquals(-1, list.getSegmentIndexFor(27));
		}

		@DisplayName("#29")
		@Test
		public void test_29() {
		}

		@DisplayName("#30")
		@Test
		public void test_30() {
			assertEquals(-1, list.getSegmentIndexFor(28));
		}

		@DisplayName("#31")
		@Test
		public void test_31() {
			assertEquals(-1, list.getSegmentIndexFor(29));
		}

		@DisplayName("#32")
		@Test
		public void test_32() {
			assertEquals(-1, list.getSegmentIndexFor(30));
		}
	}

	@DisplayName("removeElementInSegment")
	@Nested
	public class RemoveElementInSegment {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertFalse(list.removeElementInSegment(2, 11));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(list.removeElementInSegment(2, 10));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(25), i.next());
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("size")
	@Nested
	public class Size {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(10, list.size());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(list.add(Integer.valueOf(34)));
			assertEquals(11, list.size());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			list.add(Integer.valueOf(34));
			assertFalse(list.add(Integer.valueOf(34)));
			assertEquals(11, list.size());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			list.add(Integer.valueOf(34));
			assertTrue(list.remove(Integer.valueOf(1)));
			assertEquals(10, list.size());
		}
	}

	@DisplayName("add")
	@Nested
	public class Add {

		@DisplayName("#1")
		@Test
		public void test_1() {
			list.add(4);
			var i = list.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(4), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			list.add(4);
			list.add(100);
			var i = list.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(4), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
			assertEquals(Integer.valueOf(100), i.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			list.add(4);
			list.add(100);
			list.add(-10);
			var i = list.iterator();
			assertEquals(Integer.valueOf(-10), i.next());
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(4), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
			assertEquals(Integer.valueOf(100), i.next());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			list.add(4);
			list.add(100);
			list.add(-10);
			list.add(5);
			list.add(9);
			list.add(8);
			list.add(6);
			list.add(7);
			var i = list.iterator();
			assertEquals(Integer.valueOf(-10), i.next());
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(4), i.next());
			assertEquals(Integer.valueOf(5), i.next());
			assertEquals(Integer.valueOf(6), i.next());
			assertEquals(Integer.valueOf(7), i.next());
			assertEquals(Integer.valueOf(8), i.next());
			assertEquals(Integer.valueOf(9), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
			assertEquals(Integer.valueOf(100), i.next());
		}
	}

	@DisplayName("first")
	@Nested
	public class First {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(Integer.valueOf(1), list.first());
		}
	}

	@DisplayName("headSet")
	@Nested
	public class HeadSet {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var s = list.headSet(1);
			assertTrue(s.isEmpty());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var s = list.headSet(5);
			assertEquals(3, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var s = list.headSet(21);
			assertEquals(5, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
		}
	}

	@DisplayName("last")
	@Nested
	public class Last {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(Integer.valueOf(25), list.last());
		}
	}

	@DisplayName("subSet")
	@Nested
	public class SubSet {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var s = list.subSet(0, 1);
			assertTrue(s.isEmpty());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var s = list.subSet(0, 10);
			assertEquals(3, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var s = list.subSet(0, 21);
			assertEquals(5, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			var s = list.subSet(3, 21);
			assertEquals(3, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			var s = list.subSet(10, 21);
			assertEquals(2, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
		}
	}

	@DisplayName("tailSet")
	@Nested
	public class TailSet {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var s = list.tailSet(30);
			assertTrue(s.isEmpty());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var s = list.tailSet(21);
			assertEquals(5, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var s = list.tailSet(4);
			assertEquals(7, s.size());
			var i = s.iterator();
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
		}
	}

	@DisplayName("addAll")
	@Nested
	public class AddAll {

		@DisplayName("#1")
		@Test
		public void test_1() {
			list.addAll(Collections.singleton(4));
			var i = list.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(4), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			list.addAll(Collections.singleton(4));
			list.addAll(Collections.singleton(100));
			var i = list.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(4), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
			assertEquals(Integer.valueOf(100), i.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			list.addAll(Collections.singleton(4));
			list.addAll(Collections.singleton(100));
			list.addAll(Arrays.asList(5, 6, 7, 8, 9));
			var i = list.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(4), i.next());
			assertEquals(Integer.valueOf(5), i.next());
			assertEquals(Integer.valueOf(6), i.next());
			assertEquals(Integer.valueOf(7), i.next());
			assertEquals(Integer.valueOf(8), i.next());
			assertEquals(Integer.valueOf(9), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
			assertEquals(Integer.valueOf(21), i.next());
			assertEquals(Integer.valueOf(22), i.next());
			assertEquals(Integer.valueOf(23), i.next());
			assertEquals(Integer.valueOf(24), i.next());
			assertEquals(Integer.valueOf(25), i.next());
			assertEquals(Integer.valueOf(100), i.next());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@Test
		public void test_1() {
			list.clear();	
			var i = list.iterator();
			assertFalse(i.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			list.clear();
			assertTrue(list.isEmpty());
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertFalse(list.contains(0));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(list.contains(1));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertTrue(list.contains(2));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertTrue(list.contains(3));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertFalse(list.contains(4));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertFalse(list.contains(5));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertFalse(list.contains(6));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertFalse(list.contains(7));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertFalse(list.contains(8));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertFalse(list.contains(9));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertTrue(list.contains(10));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertFalse(list.contains(11));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertFalse(list.contains(12));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertFalse(list.contains(13));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertFalse(list.contains(14));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertFalse(list.contains(15));
		}
	}

	@DisplayName("containsAll")
	@Nested
	public class ContainsAll {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertFalse(list.containsAll(Collections.singleton(0)));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(list.containsAll(Arrays.asList(1, 2, 3)));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertFalse(list.containsAll(Arrays.asList(4, 5, 6, 7, 8, 9)));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertFalse(list.containsAll(Arrays.asList(10, 11, 12, 13, 14, 15)));
		}
	}

	@DisplayName("iterator")
	@Nested
	public class Iterator {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(25), i.next());
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("segmentIterator")
	@Nested
	public class SegmentIterator {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var i = list.segmentIterator();
			assertTrue(i.hasNext());
			var s = i.next();
			assertEquals(1, s.getFirst());
			assertEquals(3, s.getLast());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var i = list.segmentIterator();
			i.next();
			assertTrue(i.hasNext());
			var s = i.next();
			assertEquals(10, s.getFirst());
			assertEquals(10, s.getLast());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var i = list.segmentIterator();
			i.next();
			i.next();
			assertTrue(i.hasNext());
			var s = i.next();
			assertEquals(20, s.getFirst());
			assertEquals(25, s.getLast());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			var i = list.segmentIterator();
			i.next();
			i.next();
			i.next();
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("toSegmentIterable")
	@Nested
	public class ToSegmentIterable {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var it = list.toSegmentIterable();
			var i = it.iterator();
			assertTrue(i.hasNext());
			var s = i.next();
			assertEquals(1, s.getFirst());
			assertEquals(3, s.getLast());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var it = list.toSegmentIterable();
			var i = it.iterator();
			i.next();
			assertTrue(i.hasNext());
			var s = i.next();
			assertEquals(10, s.getFirst());
			assertEquals(10, s.getLast());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var it = list.toSegmentIterable();
			var i = it.iterator();
			i.next();
			i.next();
			assertTrue(i.hasNext());
			var s = i.next();
			assertEquals(20, s.getFirst());
			assertEquals(25, s.getLast());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			var it = list.toSegmentIterable();
			var i = it.iterator();
			i.next();
			i.next();
			i.next();
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@DisplayName("(Object) #1")
		@Test
		public void obj_1() {
			assertTrue(list.remove(Integer.valueOf(25)));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("(Object) #2")
		@Test
		public void obj_2() {
			list.remove(Integer.valueOf(25));
			assertTrue(list.remove(Integer.valueOf(20)));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("(Object) #3")
		@Test
		public void obj_3() {
			list.remove(Integer.valueOf(25));
			list.remove(Integer.valueOf(20));
			assertTrue(list.remove(Integer.valueOf(10)));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("(Object) #4")
		@Test
		public void obj_4() {
			list.remove(Integer.valueOf(25));
			list.remove(Integer.valueOf(20));
			list.remove(Integer.valueOf(10));
			assertTrue(list.remove(Integer.valueOf(1)));
			assertFalse(list.remove(Integer.valueOf(1)));
			assertNotNull(list.remove(Integer.valueOf(3)));
			assertNotNull(list.remove(Integer.valueOf(2)));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("(int) #1")
		@Test
		public void int_1() {
			assertEquals(Integer.valueOf(2), list.remove(1));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(25), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("(int) #2")
		@Test
		public void int_2() {
			list.remove(1);
			assertEquals(Integer.valueOf(22), list.remove(5));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(25), i.next());
			assertFalse(i.hasNext());
		}
	}
		
	@DisplayName("removeAll")
	@Nested
	public class RemoveAll {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(list.removeAll(Collections.singleton(Integer.valueOf(25))));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			list.removeAll(Collections.singleton(Integer.valueOf(25)));
			assertTrue(list.removeAll(Collections.singleton(Integer.valueOf(20))));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			list.removeAll(Collections.singleton(Integer.valueOf(25)));
			list.removeAll(Collections.singleton(Integer.valueOf(20)));
			assertTrue(list.removeAll(Collections.singleton(Integer.valueOf(10))));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			list.removeAll(Collections.singleton(Integer.valueOf(25)));
			list.removeAll(Collections.singleton(Integer.valueOf(20)));
			list.removeAll(Collections.singleton(Integer.valueOf(10)));
			assertTrue(list.removeAll(Arrays.asList(
					Integer.valueOf(1),
					Integer.valueOf(1),
					Integer.valueOf(3),
					Integer.valueOf(2))));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("retainAll")
	@Nested
	public class RetainAll {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(list.retainAll(Arrays.asList(
					Integer.valueOf(1),
					Integer.valueOf(1),
					Integer.valueOf(3),
					Integer.valueOf(2))));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertFalse(i.hasNext());
		}
	}
	
	@DisplayName("set")
	@Nested
	public class Set {

		private SortedSet<Integer> set = new TreeSet<>();
		
		@BeforeEach
		public void setUp() {
			set.add(Integer.valueOf(00));
			set.add(Integer.valueOf(10));
			set.add(Integer.valueOf(20));
			set.add(Integer.valueOf(30));
			set.add(Integer.valueOf(40));
		}

		@DisplayName("(SortedSet) #1")
		@Test
		public void sortedset_1() {
			list.set(set);
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(0), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(30), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(40), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("(int, Object) #1")
		@Test
		public void intobject_1() {
			assertEquals(Integer.valueOf(2), list.set(1, Integer.valueOf(123)));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(25), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(123), i.next());
			assertFalse(i.hasNext());
		}

		@DisplayName("(int, Object) #2")
		@Test
		public void intobject_2() {
			list.set(1, Integer.valueOf(123));
			assertEquals(Integer.valueOf(3), list.set(1, Integer.valueOf(456)));
			var i = list.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(25), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(123), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(456), i.next());
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("getSegmentCount")
	@Nested
	public class GetSegmentCount {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(3, list.getSegmentCount());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			list.add(Integer.valueOf(00));
			list.add(Integer.valueOf(10));
			list.add(Integer.valueOf(20));
			list.add(Integer.valueOf(30));
			list.add(Integer.valueOf(40));
			assertEquals(5, list.getSegmentCount());
		}
	}

	@DisplayName("get")
	@Nested
	public class Get {

		private int[] tab;

		@BeforeEach
		public void setUp() {
			tab = new int[2];
		}

		@DisplayName("(int,int[]) #1")
		@Test
		public void intintarray_1() {
			assertTrue(list.get(0, tab));
			assertEquals(0, tab[0]);
			assertEquals(1, tab[1]);
		}

		@DisplayName("(int,int[]) #2")
		@Test
		public void intintarray_2() {
			assertTrue(list.get(1, tab));
			assertEquals(0, tab[0]);
			assertEquals(2, tab[1]);
		}

		@DisplayName("(int,int[]) #3")
		@Test
		public void intintarray_3() {
			assertTrue(list.get(2, tab));
			assertEquals(0, tab[0]);
			assertEquals(3, tab[1]);
		}

		@DisplayName("(int,int[]) #4")
		@Test
		public void intintarray_4() {
			assertTrue(list.get(3, tab));
			assertEquals(2, tab[0]);
			assertEquals(10, tab[1]);
		}

		@DisplayName("(int,int[]) #5")
		@Test
		public void intintarray_5() {
			assertTrue(list.get(4, tab));
			assertEquals(4, tab[0]);
			assertEquals(20, tab[1]);
		}

		@DisplayName("(int) #1")
		@Test
		public void int_1() {
			assertEquals(Integer.valueOf(1), list.get(0));
		}

		@DisplayName("(int) #2")
		@Test
		public void int_2() {
			assertEquals(Integer.valueOf(2), list.get(1));
		}

		@DisplayName("(int) #3")
		@Test
		public void int_3() {
			assertEquals(Integer.valueOf(3), list.get(2));
		}

		@DisplayName("(int) #4")
		@Test
		public void int_4() {
			assertEquals(Integer.valueOf(10), list.get(3));
		}

		@DisplayName("(int) #5")
		@Test
		public void int_5() {
			assertEquals(Integer.valueOf(20), list.get(4));
		}

		@DisplayName("(int) #6")
		@Test
		public void int_6() {
			assertEquals(Integer.valueOf(21), list.get(5));
		}

		@DisplayName("(int) #7")
		@Test
		public void int_7() {
			assertEquals(Integer.valueOf(22), list.get(6));
		}

		@DisplayName("(int) #8")
		@Test
		public void int_8() {
			assertEquals(Integer.valueOf(23), list.get(7));
		}

		@DisplayName("(int) #9")
		@Test
		public void int_9() {
			assertEquals(Integer.valueOf(24), list.get(8));
		}

		@DisplayName("(int) #10")
		@Test
		public void int_10() {
			assertEquals(Integer.valueOf(25), list.get(9));
		}
	}

	@DisplayName("valueOf")
	@Nested
	public class ValueOf {

		private int[] tab;

		@BeforeEach
		public void setUp() {
			tab = list.toIntArray();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(Integer.valueOf(1),  tab[0]);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(Integer.valueOf(2),  tab[1]);
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(Integer.valueOf(3),  tab[2]);
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(Integer.valueOf(10), tab[3]);
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(Integer.valueOf(20), tab[4]);
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(Integer.valueOf(21), tab[5]);
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(Integer.valueOf(22), tab[6]);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEquals(Integer.valueOf(23), tab[7]);
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEquals(Integer.valueOf(24), tab[8]);
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEquals(Integer.valueOf(25), tab[9]);
		}
	}

	@DisplayName("toIntArray")
	@Nested
	public class ToIntArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			int[] tab = list.toIntArray();
			assertEquals(1,  tab[0]);
			assertEquals(2,  tab[1]);
			assertEquals(3,  tab[2]);
			assertEquals(10, tab[3]);
			assertEquals(20, tab[4]);
			assertEquals(21, tab[5]);
			assertEquals(22, tab[6]);
			assertEquals(23, tab[7]);
			assertEquals(24, tab[8]);
			assertEquals(25, tab[9]);
		}
	}

	@DisplayName("toArray")
	@Nested
	public class ToArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var tab = new Object[15];
			assertSame(tab, list.toArray(tab));
			assertEquals(Integer.valueOf(1),  tab[0]);
			assertEquals(Integer.valueOf(2),  tab[1]);
			assertEquals(Integer.valueOf(3),  tab[2]);
			assertEquals(Integer.valueOf(10), tab[3]);
			assertEquals(Integer.valueOf(20), tab[4]);
			assertEquals(Integer.valueOf(21), tab[5]);
			assertEquals(Integer.valueOf(22), tab[6]);
			assertEquals(Integer.valueOf(23), tab[7]);
			assertEquals(Integer.valueOf(24), tab[8]);
			assertEquals(Integer.valueOf(25), tab[9]);
			assertNull(tab[10]);
			assertNull(tab[11]);
			assertNull(tab[12]);
			assertNull(tab[13]);
			assertNull(tab[14]);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var tab = new Object[5];
			var tab2 = list.toArray(tab);
			assertNotSame(tab, tab2);
			assertEquals(Integer.valueOf(1),  tab2[0]);
			assertEquals(Integer.valueOf(2),  tab2[1]);
			assertEquals(Integer.valueOf(3),  tab2[2]);
			assertEquals(Integer.valueOf(10), tab2[3]);
			assertEquals(Integer.valueOf(20), tab2[4]);
		}
	}
	
	@DisplayName("toSortedSet")
	@Nested
	public class ToSortedSet {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var set = list.toSortedSet();
			var i = set.iterator();
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(1), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(2), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(3), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(10), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(20), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(21), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(22), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(23), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(24), i.next());
			assertTrue(i.hasNext());
			assertEquals(Integer.valueOf(25), i.next());
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("indexOf")
	@Nested
	public class IndexOf {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(-1, list.indexOf(Integer.valueOf(0)));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(0,  list.indexOf(Integer.valueOf(1)));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(1,  list.indexOf(Integer.valueOf(2)));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(2,  list.indexOf(Integer.valueOf(3)));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(-1, list.indexOf(Integer.valueOf(4)));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(-1, list.indexOf(Integer.valueOf(5)));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(-1, list.indexOf(Integer.valueOf(6)));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEquals(-1, list.indexOf(Integer.valueOf(7)));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEquals(-1, list.indexOf(Integer.valueOf(8)));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEquals(-1, list.indexOf(Integer.valueOf(9)));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEquals(3,  list.indexOf(Integer.valueOf(10)));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEquals(-1, list.indexOf(Integer.valueOf(11)));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEquals(-1, list.indexOf(Integer.valueOf(12)));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertEquals(-1, list.indexOf(Integer.valueOf(13)));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertEquals(-1, list.indexOf(Integer.valueOf(14)));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertEquals(-1, list.indexOf(Integer.valueOf(15)));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertEquals(-1, list.indexOf(Integer.valueOf(16)));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertEquals(-1, list.indexOf(Integer.valueOf(17)));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertEquals(-1, list.indexOf(Integer.valueOf(18)));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertEquals(-1, list.indexOf(Integer.valueOf(19)));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertEquals(4,  list.indexOf(Integer.valueOf(20)));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertEquals(5,  list.indexOf(Integer.valueOf(21)));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertEquals(6,  list.indexOf(Integer.valueOf(22)));
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertEquals(7,  list.indexOf(Integer.valueOf(23)));
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertEquals(8,  list.indexOf(Integer.valueOf(24)));
		}

		@DisplayName("#26")
		@Test
		public void test_26() {
			assertEquals(9,  list.indexOf(Integer.valueOf(25)));
		}

		@DisplayName("#27")
		@Test
		public void test_27() {
		}

		@DisplayName("#28")
		@Test
		public void test_28() {
			assertEquals(-1, list.indexOf(Integer.valueOf(26)));
		}

		@DisplayName("#29")
		@Test
		public void test_29() {
			assertEquals(-1, list.indexOf(Integer.valueOf(27)));
		}

		@DisplayName("#30")
		@Test
		public void test_30() {
			assertEquals(-1, list.indexOf(Integer.valueOf(28)));
		}

		@DisplayName("#31")
		@Test
		public void test_31() {
			assertEquals(-1, list.indexOf(Integer.valueOf(29)));
		}

		@DisplayName("#32")
		@Test
		public void test_32() {
			assertEquals(-1, list.indexOf(Integer.valueOf(30)));
		}
	}

	@DisplayName("lastIndexOf")
	@Nested
	public class LastIndexOf {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(0)));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(0,  list.lastIndexOf(Integer.valueOf(1)));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(1,  list.lastIndexOf(Integer.valueOf(2)));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(2,  list.lastIndexOf(Integer.valueOf(3)));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(4)));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(5)));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(6)));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(7)));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(8)));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(9)));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEquals(3,  list.lastIndexOf(Integer.valueOf(10)));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(11)));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(12)));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(13)));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(14)));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(15)));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(16)));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(17)));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(18)));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(19)));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertEquals(4,  list.lastIndexOf(Integer.valueOf(20)));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertEquals(5,  list.lastIndexOf(Integer.valueOf(21)));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertEquals(6,  list.lastIndexOf(Integer.valueOf(22)));
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertEquals(7,  list.lastIndexOf(Integer.valueOf(23)));
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertEquals(8,  list.lastIndexOf(Integer.valueOf(24)));
		}

		@DisplayName("#26")
		@Test
		public void test_26() {
			assertEquals(9,  list.lastIndexOf(Integer.valueOf(25)));
		}

		@DisplayName("#27")
		@Test
		public void test_27() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(26)));
		}

		@DisplayName("#28")
		@Test
		public void test_28() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(27)));
		}

		@DisplayName("#29")
		@Test
		public void test_29() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(28)));
		}

		@DisplayName("#30")
		@Test
		public void test_30() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(29)));
		}

		@DisplayName("#31")
		@Test
		public void test_31() {
			assertEquals(-1, list.lastIndexOf(Integer.valueOf(30)));
		}
	}

	@DisplayName("listIterator")
	@Nested
	public class ListIterator {

		@DisplayName("()")
		@Test
		public void empty_1() {
			var i = list.listIterator();
	
			assertFalse(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(0, i.nextIndex());
			assertEquals(Integer.valueOf(1), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(1, i.nextIndex());
			assertEquals(Integer.valueOf(2), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(2, i.nextIndex());
			assertEquals(Integer.valueOf(3), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(3, i.nextIndex());
			assertEquals(Integer.valueOf(10), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(4, i.nextIndex());
			assertEquals(Integer.valueOf(20), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(5, i.nextIndex());
			assertEquals(Integer.valueOf(21), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(6, i.nextIndex());
			assertEquals(Integer.valueOf(22), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(7, i.nextIndex());
			assertEquals(Integer.valueOf(23), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(8, i.nextIndex());
			assertEquals(Integer.valueOf(24), i.next());
	
			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(9, i.nextIndex());
			assertEquals(Integer.valueOf(25), i.next());
	
			assertTrue(i.hasPrevious());
			assertFalse(i.hasNext());
		}

		@DisplayName("(int)")
		@Test
		public void listIteratorInt() {
			var i = list.listIterator(2);

			assertFalse(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(2, i.nextIndex());
			assertEquals(Integer.valueOf(3), i.next());

			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(3, i.nextIndex());
			assertEquals(Integer.valueOf(10), i.next());

			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(4, i.nextIndex());
			assertEquals(Integer.valueOf(20), i.next());

			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(5, i.nextIndex());
			assertEquals(Integer.valueOf(21), i.next());

			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(6, i.nextIndex());
			assertEquals(Integer.valueOf(22), i.next());

			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(7, i.nextIndex());
			assertEquals(Integer.valueOf(23), i.next());

			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(8, i.nextIndex());
			assertEquals(Integer.valueOf(24), i.next());

			assertTrue(i.hasPrevious());
			assertTrue(i.hasNext());
			assertEquals(9, i.nextIndex());
			assertEquals(Integer.valueOf(25), i.next());

			assertTrue(i.hasPrevious());
			assertFalse(i.hasNext());
		}
	}

	@DisplayName("subList")
	@Nested
	public class SubList {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var l = list.subList(0, 3);
			assertEquals(3, l.size());
			var i = l.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var l = list.subList(0, 5);
			assertEquals(5, l.size());
			var i = l.iterator();
			assertEquals(Integer.valueOf(1), i.next());
			assertEquals(Integer.valueOf(2), i.next());
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var l = list.subList(2, 5);
			assertEquals(3, l.size());
			var i = l.iterator();
			assertEquals(Integer.valueOf(3), i.next());
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			var l = list.subList(3, 5);
			assertEquals(2, l.size());
			var i = l.iterator();
			assertEquals(Integer.valueOf(10), i.next());
			assertEquals(Integer.valueOf(20), i.next());
		}
	}

}
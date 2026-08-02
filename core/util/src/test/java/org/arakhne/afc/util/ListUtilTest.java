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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("ListUtil")
@SuppressWarnings("all")
public class ListUtilTest {

	@DisplayName("contains")
	@Nested
	public class Contains {

		private List<Integer> list;

		@BeforeEach			
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		}

		@DisplayName("(List,Comparator,Integer) #1")
		@Test
		public void containsComparatorTList_1() {
			assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 0));
		}

		@DisplayName("(List,Comparator,Integer) #2")
		@Test
		public void containsComparatorTList_2() {
			assertTrue(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 1));
		}

		@DisplayName("(List,Comparator,Integer) #3")
		@Test
		public void containsComparatorTList_3() {
			assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 2));
		}

		@DisplayName("(List,Comparator,Integer) #4")
		@Test
		public void containsComparatorTList_4() {
			assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 3));
		}

		@DisplayName("(List,Comparator,Integer) #5")
		@Test
		public void containsComparatorTList_5() {
			assertTrue(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("(List,Comparator,Integer) #6")
		@Test
		public void containsComparatorTList_6() {
			assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 5));
		}

		@DisplayName("(List,Comparator,Integer) #7")
		@Test
		public void containsComparatorTList_7() {
			assertTrue(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 6));
		}
	}

	@DisplayName("add")
	@Nested
	public class Add {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		}

		@DisplayName("(List,Comparator,Integer,true,false) #1")
		@Test
		public void addComparatorTListBoolean_true_false_1() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, true, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,true,false) #2")
		@Test
		public void addComparatorTListBoolean_true_false_2() {
			assertEquals(2, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, true, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,true,false) #3")
		@Test
		public void addComparatorTListBoolean_true_false_3() {
			assertEquals(4, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, true, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,true,false) #4")
		@Test
		public void addComparatorTListBoolean_true_false_4() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, true, false));
			assertEquals(3, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, true, false));
			assertEquals(6, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, true, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,false) #1")
		@Test
		public void addComparatorTListBoolean_false_false_1() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, false, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,false) #2")
		@Test
		public void addComparatorTListBoolean_false_false_2() {
			assertEquals(-1, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, false, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,false) #3")
		@Test
		public void addComparatorTListBoolean_false_false_3() {
			assertEquals(4, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, false, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,false) #4")
		@Test
		public void addComparatorTListBoolean_false_false_4() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, false, false));
			assertEquals(-1, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, false, false));
			assertEquals(5, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, false, false));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,true,true) #1")
		@Test
		public void addComparatorTListBoolean_true_true_1() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, true, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,true,true) #2")
		@Test
		public void addComparatorTListBoolean_true_true_2() {
			assertEquals(2, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, true, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,true,true) #3")
		@Test
		public void addComparatorTListBoolean_true_true_3() {
			assertEquals(4, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, true, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,true,true) #4")
		@Test
		public void addComparatorTListBoolean_true_true_4() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, true, true));
			assertEquals(3, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, true, true));
			assertEquals(6, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, true, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,true) #1")
		@Test
		public void addComparatorTListBoolean_false_true_1() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, false, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,true) #2")
		@Test
		public void addComparatorTListBoolean_false_true_2() {
			assertEquals(1, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, false, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,true) #3")
		@Test
		public void addComparatorTListBoolean_false_true_3() {
			assertEquals(4, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, false, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("(List,Comparator,Integer,false,true) #4")
		@Test
		public void addComparatorTListBoolean_false_true_() {
			assertEquals(0, ListUtil.add(list, new NaturalOrderComparator<Integer>(), -2, false, true));
			assertEquals(2, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 4, false, true));
			assertEquals(5, ListUtil.add(list, new NaturalOrderComparator<Integer>(), 16, false, true));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("getInsertionIndex")
	@Nested
	public class GetInsertionIndex {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		}

		@DisplayName("(List,Comparator,Integer) #1")
		@Test
		public void getInsertionIndexComparatorTList_1() {
			assertEquals(0, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("(List,Comparator,Integer) #2")
		@Test
		public void getInsertionIndexComparatorTList_2() {
			assertEquals(2, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("(List,Comparator,Integer) #3")
		@Test
		public void getInsertionIndexComparatorTList_() {
			assertEquals(4, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 16));
		}
		
		@DisplayName("(List,Comparator,Integer,true) #1")
		@Test
		public void getInsertionIndexComparatorTListBoolean_true_1() {
			assertEquals(0, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), -2, true));
		}
		
		@DisplayName("(List,Comparator,Integer,true) #2")
		@Test
		public void getInsertionIndexComparatorTListBoolean_true_2() {
			assertEquals(2, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 4, true));
		}
		
		@DisplayName("(List,Comparator,Integer,true) #3")
		@Test
		public void getInsertionIndexComparatorTListBoolean_true_3() {
			assertEquals(4, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 16, true));
		}
	
		@DisplayName("(List,Comparator,Integer,false) #1")
		@Test
		public void getInsertionIndexComparatorTListBoolean_false_1() {
			assertEquals(0, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), -2, false));
		}
		
		@DisplayName("(List,Comparator,Integer,false) #2")
		@Test
		public void getInsertionIndexComparatorTListBoolean_false_2() {
			assertEquals(-1, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 4, false));
		}
		
		@DisplayName("(List,Comparator,Integer,false) #3")
		@Test
		public void getInsertionIndexComparatorTListBoolean_false_3() {
			assertEquals(4, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 16, false));
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void removeComparatorTList_1() {
			assertEquals(-1, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("#2")
		@Test
		public void removeComparatorTList_2() {
			assertEquals(-1, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), 34));
		}

		@DisplayName("#3")
		@Test
		public void removeComparatorTList_3() {
			assertEquals(2, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), 6));
		}

		@DisplayName("#4")
		@Test
		public void removeComparatorTList_4() {
			assertEquals(1, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), 4));
		}
	}

	@DisplayName("indexOf")
	@Nested
	public class IndexOf {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void indexOfComparatorTList_1() {
			assertEquals(-1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("#2")
		@Test
		public void indexOfComparatorTList_2() {
			assertEquals(-1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 34));
		}

		@DisplayName("#3")
		@Test
		public void indexOfComparatorTList_3() {
			assertEquals(-1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 7));
		}

		@DisplayName("#4")
		@Test
		public void indexOfComparatorTList_4() {
			assertEquals(0, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 1));
		}

		@DisplayName("#5")
		@Test
		public void indexOfComparatorTList_5() {
			assertEquals(1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("#6")
		@Test
		public void indexOfComparatorTList_6() {
			assertEquals(2, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 6));
		}

		@DisplayName("#7")
		@Test
		public void indexOfComparatorTList_7() {
			assertEquals(5, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 12));
		}

		@DisplayName("#8")
		@Test
		public void indexOfComparatorTList_8() {
			assertEquals(6, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 30));
		}
	}

	@DisplayName("lastIndexOf")
	@Nested
	public class LastIndexOf {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void lastIndexOfComparatorTList_1() {
			assertEquals(-1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("#2")
		@Test
		public void lastIndexOfComparatorTList_2() {
			assertEquals(-1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 34));
		}

		@DisplayName("#3")
		@Test
		public void lastIndexOfComparatorTList_3() {
			assertEquals(-1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 7));
		}

		@DisplayName("#4")
		@Test
		public void lastIndexOfComparatorTList_4() {
			assertEquals(0, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 1));
		}

		@DisplayName("#5")
		@Test
		public void lastIndexOfComparatorTList_5() {
			assertEquals(1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("#6")
		@Test
		public void lastIndexOfComparatorTList_6() {
			assertEquals(4, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 6));
		}

		@DisplayName("#7")
		@Test
		public void lastIndexOfComparatorTList_7() {
			assertEquals(5, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 12));
		}

		@DisplayName("#8")
		@Test
		public void lastIndexOfComparatorTList_8() {
			assertEquals(6, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 30));
		}
	}

	@DisplayName("floorIndex")
	@Nested
	public class FloorIndex {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void floorIndexComparatorTList_1() {
			assertEquals(-1, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("#2")
		@Test
		public void floorIndexComparatorTList_2() {
			assertEquals(4, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 7));
		}

		@DisplayName("#3")
		@Test
		public void floorIndexComparatorTList_3() {
			assertEquals(6, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 34));
		}

		@DisplayName("#4")
		@Test
		public void floorIndexComparatorTList_4() {
			assertEquals(0, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 1));
		}

		@DisplayName("#5")
		@Test
		public void floorIndexComparatorTList_5() {
			assertEquals(1, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("#6")
		@Test
		public void floorIndexComparatorTList_6() {
			assertEquals(4, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 6));
		}

		@DisplayName("#7")
		@Test
		public void floorIndexComparatorTList_7() {
			assertEquals(5, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 12));
		}

		@DisplayName("#8")
		@Test
		public void floorIndexComparatorTList_8() {
			assertEquals(6, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 30));
		}
	}

	@DisplayName("higherIndex")
	@Nested
	public class HigherIndex {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void higherIndexComparatorTList_1() {
			assertEquals(0, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("#2")
		@Test
		public void higherIndexComparatorTList_2() {
			assertEquals(5, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 7));
		}

		@DisplayName("#3")
		@Test
		public void higherIndexComparatorTList_3() {
			assertEquals(-1, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 34));
		}

		@DisplayName("#4")
		@Test
		public void higherIndexComparatorTList_4() {
			assertEquals(1, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 1));
		}

		@DisplayName("#5")
		@Test
		public void higherIndexComparatorTList_5() {
			assertEquals(2, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("#6")
		@Test
		public void higherIndexComparatorTList_6() {
			assertEquals(5, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 6));
		}

		@DisplayName("#7")
		@Test
		public void higherIndexComparatorTList_7() {
			assertEquals(6, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 12));
		}

		@DisplayName("#8")
		@Test
		public void higherIndexComparatorTList_8() {
			assertEquals(-1, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 30));
		}
	}

	@DisplayName("lowerIndex")
	@Nested
	public class LowerIndex {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void lowerIndexComparatorTList_1() {
			assertEquals(-1, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("#2")
		@Test
		public void lowerIndexComparatorTList_2() {
			assertEquals(4, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 7));
		}

		@DisplayName("#3")
		@Test
		public void lowerIndexComparatorTList_3() {
			assertEquals(6, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 34));
		}

		@DisplayName("#4")
		@Test
		public void lowerIndexComparatorTList_4() {
			assertEquals(-1, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 1));
		}

		@DisplayName("#5")
		@Test
		public void lowerIndexComparatorTList_5() {
			assertEquals(0, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("#6")
		@Test
		public void lowerIndexComparatorTList_6() {
			assertEquals(1, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 6));
		}

		@DisplayName("#7")
		@Test
		public void lowerIndexComparatorTList_7() {
			assertEquals(4, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 12));
		}

		@DisplayName("#8")
		@Test
		public void lowerIndexComparatorTList_8() {
			assertEquals(5, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 30));
		}
	}

	@DisplayName("ceilingIndex")
	@Nested
	public class CeilingIndex {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void ceilingIndexComparatorTList_1() {
			assertEquals(0, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), -2));
		}

		@DisplayName("#2")
		@Test
		public void ceilingIndexComparatorTList_2() {
			assertEquals(5, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 7));
		}

		@DisplayName("#3")
		@Test
		public void ceilingIndexComparatorTList_3() {
			assertEquals(-1, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 34));
		}

		@DisplayName("#4")
		@Test
		public void ceilingIndexComparatorTList_4() {
			assertEquals(0, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 1));
		}

		@DisplayName("#5")
		@Test
		public void ceilingIndexComparatorTList_5() {
			assertEquals(1, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 4));
		}

		@DisplayName("#6")
		@Test
		public void ceilingIndexComparatorTList_6() {
			assertEquals(2, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 6));
		}

		@DisplayName("#7")
		@Test
		public void ceilingIndexComparatorTList_7() {
			assertEquals(5, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 12));
		}

		@DisplayName("#8")
		@Test
		public void ceilingIndexComparatorTList_8() {
			assertEquals(6, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 30));
		}
	}

	@DisplayName("addIfAbsent")
	@Nested
	public class AddIfAbsent {

		private List<Integer> list;

		@BeforeEach
		public void setUp() {
			list = new ArrayList<>();
			list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		}

		@DisplayName("#1")
		@Test
		public void addIfAbsent_1() {
			assertEquals(0, ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), -2));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void addIfAbsent_2() {
			ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), -2);
			assertEquals(-1, ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), 4));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#3")
		@Test
		public void addIfAbsent_3() {
			ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), -2);
			assertEquals(5, ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), 16));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#4")
		@Test
		public void addIfAbsent_4() {
			assertEquals(0, ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), -2));
			assertEquals(-1, ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), 4));
			assertEquals(5, ListUtil.addIfAbsent(list, new NaturalOrderComparator<Integer>(), 16));
			Iterator<Integer> iterator = list.iterator();
			assertEquals(Integer.valueOf(-2), iterator.next());
			assertEquals(Integer.valueOf(1), iterator.next());
			assertEquals(Integer.valueOf(4), iterator.next());
			assertEquals(Integer.valueOf(6), iterator.next());
			assertEquals(Integer.valueOf(12), iterator.next());
			assertEquals(Integer.valueOf(16), iterator.next());
			assertEquals(Integer.valueOf(30), iterator.next());
			assertFalse(iterator.hasNext());
		}
	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ListUtilTest {

	@Test
	public void containsComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		
		assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 0));
		assertTrue(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 1));
		assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 2));
		assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 3));
		assertTrue(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 4));
		assertFalse(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 5));
		assertTrue(ListUtil.contains(list, new NaturalOrderComparator<Integer>(), 6));
	}

	@Test
	public void addComparatorTListBoolean_true_false() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		
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

	@Test
	public void addComparatorTListBoolean_false_false() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		
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

	@Test
	public void addComparatorTListBoolean_true_true() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		
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

	@Test
	public void addComparatorTListBoolean_false_true() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		
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

	/**
	 */
	@Test
	public void getInsertionIndexComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));

		assertEquals(0, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(2, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 4));
		assertEquals(4, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 16));

		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	/**
	 */
	@Test
	public void getInsertionIndexComparatorTListBoolean_true() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));

		assertEquals(0, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), -2, true));
		assertEquals(2, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 4, true));
		assertEquals(4, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 16, true));

		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void getInsertionIndexComparatorTListBoolean_false() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));

		assertEquals(0, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), -2, false));
		assertEquals(-1, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 4, false));
		assertEquals(4, ListUtil.getInsertionIndex(list, new NaturalOrderComparator<Integer>(), 16, false));

		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void removeComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));

		assertEquals(-1, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(-1, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), 34));
		assertEquals(2, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), 6));
		assertEquals(1, ListUtil.remove(list, new NaturalOrderComparator<Integer>(), 4));
		
		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void indexOfComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));

		assertEquals(-1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(-1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 34));
		assertEquals(-1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 7));
		assertEquals(0, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 1));
		assertEquals(1, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 4));
		assertEquals(2, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 6));
		assertEquals(5, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 12));
		assertEquals(6, ListUtil.indexOf(list, new NaturalOrderComparator<Integer>(), 30));
		
		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void lastIndexOfComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));

		assertEquals(-1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(-1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 34));
		assertEquals(-1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 7));
		assertEquals(0, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 1));
		assertEquals(1, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 4));
		assertEquals(4, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 6));
		assertEquals(5, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 12));
		assertEquals(6, ListUtil.lastIndexOf(list, new NaturalOrderComparator<Integer>(), 30));
		
		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void floorIndexComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));

		assertEquals(-1, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(4, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 7));
		assertEquals(6, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 34));
		assertEquals(0, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 1));
		assertEquals(1, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 4));
		assertEquals(4, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 6));
		assertEquals(5, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 12));
		assertEquals(6, ListUtil.floorIndex(list, new NaturalOrderComparator<Integer>(), 30));
		
		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void higherIndexComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));

		assertEquals(0, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(5, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 7));
		assertEquals(-1, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 34));
		assertEquals(1, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 1));
		assertEquals(2, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 4));
		assertEquals(5, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 6));
		assertEquals(6, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 12));
		assertEquals(-1, ListUtil.higherIndex(list, new NaturalOrderComparator<Integer>(), 30));
		
		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void lowerIndexComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));

		assertEquals(-1, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(4, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 7));
		assertEquals(6, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 34));
		assertEquals(-1, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 1));
		assertEquals(0, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 4));
		assertEquals(1, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 6));
		assertEquals(4, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 12));
		assertEquals(5, ListUtil.lowerIndex(list, new NaturalOrderComparator<Integer>(), 30));
		
		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void ceilingIndexComparatorTList() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 6, 6, 12, 30));

		assertEquals(0, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), -2));
		assertEquals(5, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 7));
		assertEquals(-1, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 34));
		assertEquals(0, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 1));
		assertEquals(1, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 4));
		assertEquals(2, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 6));
		assertEquals(5, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 12));
		assertEquals(6, ListUtil.ceilingIndex(list, new NaturalOrderComparator<Integer>(), 30));
		
		Iterator<Integer> iterator = list.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(6), iterator.next());
		assertEquals(Integer.valueOf(12), iterator.next());
		assertEquals(Integer.valueOf(30), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void addIfAbsent() {
		List<Integer> list = new ArrayList<>();
		list.addAll(Arrays.asList(1, 4, 6, 12, 30));
		
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

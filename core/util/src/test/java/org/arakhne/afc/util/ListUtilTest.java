/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("static-method")
public class ListUtilTest {

	/**
	 */
	@Test
	public void testContainsComparatorTList() {
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

	/**
	 */
	@Test
	public void testAddComparatorTListBoolean_true_false() {
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

	/**
	 */
	@Test
	public void testAddComparatorTListBoolean_false_false() {
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

	/**
	 */
	@Test
	public void testAddComparatorTListBoolean_true_true() {
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

	/**
	 */
	@Test
	public void testAddComparatorTListBoolean_false_true() {
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
	public void testGetInsertionIndexComparatorTList() {
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
	public void testGetInsertionIndexComparatorTListBoolean_true() {
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
	public void testGetInsertionIndexComparatorTListBoolean_false() {
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
	public void testRemoveComparatorTList() {
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
	public void testIndexOfComparatorTList() {
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
	public void testLastIndexOfComparatorTList() {
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
	public void testFloorIndexComparatorTList() {
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
	public void testHigherIndexComparatorTList() {
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
	public void testLowerIndexComparatorTList() {
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
	public void testCeilingIndexComparatorTList() {
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

}

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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.util.IntegerList.IntegerSegment;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class IntegerListTest {

	private IntegerList list;
	
	/**
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.list = new IntegerList();
		this.list.add(1);
		this.list.add(2);
		this.list.add(3);
		this.list.add(10);
		this.list.add(20);
		this.list.add(21);
		this.list.add(22);
		this.list.add(23);
		this.list.add(24);
		this.list.add(25);
	}
	
	/**
	 * @throws Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.list = null;
	}
	
	/**
     */
	@Test
	public void comparator() {
		assertNull(this.list.comparator());
	}

    /**
     */
	@Test
	public void getLastValueOnSegmentInt() {
		assertEquals(3, this.list.getLastValueOnSegment(0));
		assertEquals(10, this.list.getLastValueOnSegment(2));
		assertEquals(25, this.list.getLastValueOnSegment(4));
	}

    /**
     */
	@Test
	public void getFirstValueOnSegmentInt() {
		assertEquals(1, this.list.getFirstValueOnSegment(0));
		assertEquals(10, this.list.getFirstValueOnSegment(2));
		assertEquals(20, this.list.getFirstValueOnSegment(4));
	}

    /**
     */
	@Test
	public void getSegmentIndexForInt() {
		assertEquals(-1, this.list.getSegmentIndexFor(0));
		assertEquals(0, this.list.getSegmentIndexFor(1));
		assertEquals(0, this.list.getSegmentIndexFor(2));
		assertEquals(0, this.list.getSegmentIndexFor(3));
		assertEquals(-1, this.list.getSegmentIndexFor(4));
		assertEquals(-1, this.list.getSegmentIndexFor(5));
		assertEquals(-1, this.list.getSegmentIndexFor(6));
		assertEquals(-1, this.list.getSegmentIndexFor(7));
		assertEquals(-1, this.list.getSegmentIndexFor(8));
		assertEquals(-1, this.list.getSegmentIndexFor(9));
		assertEquals(2, this.list.getSegmentIndexFor(10));
		assertEquals(-1, this.list.getSegmentIndexFor(11));
		assertEquals(-1, this.list.getSegmentIndexFor(12));
		assertEquals(-1, this.list.getSegmentIndexFor(13));
		assertEquals(-1, this.list.getSegmentIndexFor(14));
		assertEquals(-1, this.list.getSegmentIndexFor(15));
		assertEquals(-1, this.list.getSegmentIndexFor(16));
		assertEquals(-1, this.list.getSegmentIndexFor(17));
		assertEquals(-1, this.list.getSegmentIndexFor(18));
		assertEquals(-1, this.list.getSegmentIndexFor(19));
		assertEquals(4, this.list.getSegmentIndexFor(20));
		assertEquals(4, this.list.getSegmentIndexFor(21));
		assertEquals(4, this.list.getSegmentIndexFor(22));
		assertEquals(4, this.list.getSegmentIndexFor(23));
		assertEquals(4, this.list.getSegmentIndexFor(24));
		assertEquals(4, this.list.getSegmentIndexFor(25));
		assertEquals(-1, this.list.getSegmentIndexFor(26));
		assertEquals(-1, this.list.getSegmentIndexFor(27));
		assertEquals(-1, this.list.getSegmentIndexFor(28));
		assertEquals(-1, this.list.getSegmentIndexFor(29));
		assertEquals(-1, this.list.getSegmentIndexFor(30));
	}

	/**
	 */
	@Test
	public void removeElementInSegmentIntInt() {
		Iterator<Integer> i;

		assertFalse(this.list.removeElementInSegment(2, 11));

		assertTrue(this.list.removeElementInSegment(2, 10));
		i = this.list.iterator();
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

    /**
     */
	@Test
	public void size() {
		assertEquals(10, this.list.size());
		assertTrue(this.list.add(Integer.valueOf(34)));
		assertEquals(11, this.list.size());
		assertFalse(this.list.add(Integer.valueOf(34)));
		assertEquals(11, this.list.size());
		assertTrue(this.list.remove(Integer.valueOf(1)));
		assertEquals(10, this.list.size());
	}

    /**
     */
	@Test
	public void addIntInteger() {
		addInteger();
	}

    /**
     */
	@Test
	public void addAllIntCollection() {
		addAllCollection();
	}

	/**
     */
	@Test
	public void addInteger() {
		Iterator<Integer> i;

		this.list.add(4);
		i = this.list.iterator();
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

		this.list.add(100);
		i = this.list.iterator();
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

		this.list.add(-10);
		i = this.list.iterator();
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

		this.list.add(5);
		this.list.add(9);
		this.list.add(8);
		this.list.add(6);
		this.list.add(7);
		i = this.list.iterator();
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

	/**
     */
	@Test
	public void first() {
		assertEquals(Integer.valueOf(1), this.list.first());
	}

    /**
     */
	@Test
	public void headSetInteger() {
		SortedSet<Integer> s;
		Iterator<Integer> i;

		s = this.list.headSet(1);
		assertTrue(s.isEmpty());

		s = this.list.headSet(5);
		assertEquals(3, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(1), i.next());
		assertEquals(Integer.valueOf(2), i.next());
		assertEquals(Integer.valueOf(3), i.next());

		s = this.list.headSet(21);
		assertEquals(5, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(1), i.next());
		assertEquals(Integer.valueOf(2), i.next());
		assertEquals(Integer.valueOf(3), i.next());
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());
	}

    /**
     */
	@Test
	public void last() {
		assertEquals(Integer.valueOf(25), this.list.last());
	}

    /**
     */
	@Test
	public void subSetIntegerInteger() {
		SortedSet<Integer> s;
		Iterator<Integer> i;

		s = this.list.subSet(0, 1);
		assertTrue(s.isEmpty());

		s = this.list.subSet(0, 10);
		assertEquals(3, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(1), i.next());
		assertEquals(Integer.valueOf(2), i.next());
		assertEquals(Integer.valueOf(3), i.next());

		s = this.list.subSet(0, 21);
		assertEquals(5, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(1), i.next());
		assertEquals(Integer.valueOf(2), i.next());
		assertEquals(Integer.valueOf(3), i.next());
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());

		s = this.list.subSet(3, 21);
		assertEquals(3, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(3), i.next());
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());

		s = this.list.subSet(10, 21);
		assertEquals(2, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());
	}

    /**
     */
	@Test
	public void tailSetInteger() {
		SortedSet<Integer> s;
		Iterator<Integer> i;

		s = this.list.tailSet(30);
		assertTrue(s.isEmpty());

		s = this.list.tailSet(21);
		assertEquals(5, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(21), i.next());
		assertEquals(Integer.valueOf(22), i.next());
		assertEquals(Integer.valueOf(23), i.next());
		assertEquals(Integer.valueOf(24), i.next());
		assertEquals(Integer.valueOf(25), i.next());

		s = this.list.tailSet(4);
		assertEquals(7, s.size());
		i = s.iterator();
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());
		assertEquals(Integer.valueOf(21), i.next());
		assertEquals(Integer.valueOf(22), i.next());
		assertEquals(Integer.valueOf(23), i.next());
		assertEquals(Integer.valueOf(24), i.next());
		assertEquals(Integer.valueOf(25), i.next());
	}

    /**
     */
	@Test
	public void addAllCollection() {
		Iterator<Integer> i;

		this.list.addAll(Collections.singleton(4));
		i = this.list.iterator();
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

		this.list.addAll(Collections.singleton(100));
		i = this.list.iterator();
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

		this.list.addAll(Arrays.asList(5, 6, 7, 8, 9));
		i = this.list.iterator();
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

    /**
     */
	@Test
	public void clear() {
		Iterator<Integer> i;

		this.list.clear();

		i = this.list.iterator();
		assertFalse(i.hasNext());
	}

    /**
     */
	@Test
	public void containsObject() {
		assertFalse(this.list.contains(0));
		assertTrue(this.list.contains(1));
		assertTrue(this.list.contains(2));
		assertTrue(this.list.contains(3));
		assertFalse(this.list.contains(4));
		assertFalse(this.list.contains(5));
		assertFalse(this.list.contains(6));
		assertFalse(this.list.contains(7));
		assertFalse(this.list.contains(8));
		assertFalse(this.list.contains(9));
		assertTrue(this.list.contains(10));
		assertFalse(this.list.contains(11));
		assertFalse(this.list.contains(12));
		assertFalse(this.list.contains(13));
		assertFalse(this.list.contains(14));
		assertFalse(this.list.contains(15));
	}

    /**
     */
	@Test
	public void containsAllCollection() {
		assertFalse(this.list.containsAll(Collections.singleton(0)));
		assertTrue(this.list.containsAll(Arrays.asList(1, 2, 3)));
		assertFalse(this.list.containsAll(Arrays.asList(4, 5, 6, 7, 8, 9)));
		assertFalse(this.list.containsAll(Arrays.asList(10, 11, 12, 13, 14, 15)));
	}

    /**
     */
	@Test
	public void isEmpty() {
		assertFalse(this.list.isEmpty());
		this.list.clear();
		assertTrue(this.list.isEmpty());
	}

    /**
     */
	@Test
	public void iterator() {
		Iterator<Integer> i;

		i = this.list.iterator();
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

    /**
     */
	@Test
	public void segmentIterator() {
		Iterator<IntegerSegment> i = this.list.segmentIterator();
		IntegerSegment s;
		
		assertTrue(i.hasNext());
		s = i.next();
		assertEquals(1, s.getFirst());
		assertEquals(3, s.getLast());

		assertTrue(i.hasNext());
		s = i.next();
		assertEquals(10, s.getFirst());
		assertEquals(10, s.getLast());

		assertTrue(i.hasNext());
		s = i.next();
		assertEquals(20, s.getFirst());
		assertEquals(25, s.getLast());

		assertFalse(i.hasNext());
	}

    /**
     */
	@Test
	public void toSegmentIterable() {
		Iterable<IntegerSegment> it = this.list.toSegmentIterable();
		Iterator<IntegerSegment> i = it.iterator();
		IntegerSegment s;
		
		assertTrue(i.hasNext());
		s = i.next();
		assertEquals(1, s.getFirst());
		assertEquals(3, s.getLast());

		assertTrue(i.hasNext());
		s = i.next();
		assertEquals(10, s.getFirst());
		assertEquals(10, s.getLast());

		assertTrue(i.hasNext());
		s = i.next();
		assertEquals(20, s.getFirst());
		assertEquals(25, s.getLast());

		assertFalse(i.hasNext());
	}

	/**
     */
	@Test
	public void removeObject() {
		Iterator<Integer> i;

		assertTrue(this.list.remove(Integer.valueOf(25)));
		i = this.list.iterator();
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

		assertTrue(this.list.remove(Integer.valueOf(20)));
		i = this.list.iterator();
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

		assertTrue(this.list.remove(Integer.valueOf(10)));
		i = this.list.iterator();
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

		assertTrue(this.list.remove(Integer.valueOf(1)));
		assertFalse(this.list.remove(Integer.valueOf(1)));
		assertNotNull(this.list.remove(Integer.valueOf(3)));
		assertNotNull(this.list.remove(Integer.valueOf(2)));
		i = this.list.iterator();
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
	
	/**
	 */
	@Test
	public void removeSegmentInt() {
		Iterator<Integer> i;

		assertEquals(Integer.valueOf(25), this.list.remove(9));
		i = this.list.iterator();
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

		assertEquals(Integer.valueOf(20), this.list.remove(4));
		i = this.list.iterator();
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

		assertEquals(Integer.valueOf(10), this.list.remove(3));
		i = this.list.iterator();
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

		assertEquals(Integer.valueOf(1), this.list.remove(0));
		assertEquals(Integer.valueOf(3), this.list.remove(1));
		assertEquals(Integer.valueOf(2), this.list.remove(0));
		i = this.list.iterator();
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
	
    /**
     */
	@Test
	public void removeAllCollection() {
		Iterator<Integer> i;

		assertTrue(this.list.removeAll(Collections.singleton(Integer.valueOf(25))));
		i = this.list.iterator();
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

		assertTrue(this.list.removeAll(Collections.singleton(Integer.valueOf(20))));
		i = this.list.iterator();
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

		assertTrue(this.list.removeAll(Collections.singleton(Integer.valueOf(10))));
		i = this.list.iterator();
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

		assertTrue(this.list.removeAll(Arrays.asList(
				Integer.valueOf(1),
				Integer.valueOf(1),
				Integer.valueOf(3),
				Integer.valueOf(2))));
		i = this.list.iterator();
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

    /**
     */
	@Test
	public void retainAllCollection() {
		Iterator<Integer> i;

		assertTrue(this.list.retainAll(Arrays.asList(
				Integer.valueOf(1),
				Integer.valueOf(1),
				Integer.valueOf(3),
				Integer.valueOf(2))));
		i = this.list.iterator();
		assertTrue(i.hasNext());
		assertEquals(Integer.valueOf(1), i.next());
		assertTrue(i.hasNext());
		assertEquals(Integer.valueOf(2), i.next());
		assertTrue(i.hasNext());
		assertEquals(Integer.valueOf(3), i.next());
		assertFalse(i.hasNext());
	}
	
	/**
	 */
	@Test
	public void setSortedSet() {
		Iterator<Integer> i;
		SortedSet<Integer> set = new TreeSet<>();
		
		set.add(Integer.valueOf(00));
		set.add(Integer.valueOf(10));
		set.add(Integer.valueOf(20));
		set.add(Integer.valueOf(30));
		set.add(Integer.valueOf(40));
		
		this.list.set(set);

		i = this.list.iterator();
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

    /**
     */
	@Test
	public void getSegmentCount() {
		assertEquals(3, this.list.getSegmentCount());

		this.list.add(Integer.valueOf(00));
		this.list.add(Integer.valueOf(10));
		this.list.add(Integer.valueOf(20));
		this.list.add(Integer.valueOf(30));
		this.list.add(Integer.valueOf(40));

		assertEquals(5, this.list.getSegmentCount());
	}

    /**
     */
	@Test
	public void getIntIntArray() {
		int[] tab = new int[2];
		
		assertTrue(this.list.get(0, tab));
		assertEquals(0, tab[0]);
		assertEquals(1, tab[1]);

		assertTrue(this.list.get(1, tab));
		assertEquals(0, tab[0]);
		assertEquals(2, tab[1]);

		assertTrue(this.list.get(2, tab));
		assertEquals(0, tab[0]);
		assertEquals(3, tab[1]);

		assertTrue(this.list.get(3, tab));
		assertEquals(2, tab[0]);
		assertEquals(10, tab[1]);

		assertTrue(this.list.get(4, tab));
		assertEquals(4, tab[0]);
		assertEquals(20, tab[1]);
	}

	/**
     */
	@Test
	public void toArray() {
		Object[] tab = this.list.toArray();
		
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
	}

	/**
     */
	@Test
	public void toIntArray() {
		int[] tab = this.list.toIntArray();
		
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

	/**
     */
	@Test
	public void toArrayTArray() {
		Object[] tab, tab2;
		
		tab = new Object[15];
		assertSame(tab, this.list.toArray(tab));
		
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

		tab = new Object[5];
		tab2 = this.list.toArray(tab);
		assertNotSame(tab, tab2);
		
		assertEquals(Integer.valueOf(1),  tab2[0]);
		assertEquals(Integer.valueOf(2),  tab2[1]);
		assertEquals(Integer.valueOf(3),  tab2[2]);
		assertEquals(Integer.valueOf(10), tab2[3]);
		assertEquals(Integer.valueOf(20), tab2[4]);
	}
	
	/**
	 */
	@Test
	public void toSortedSet() {
		SortedSet<Integer> set;
		Iterator<Integer> i;
		
		set = this.list.toSortedSet();

		i = set.iterator();
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

    /**
     */
	@Test
	public void getInt() {
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Integer.valueOf(2), this.list.get(1));
		assertEquals(Integer.valueOf(3), this.list.get(2));
		assertEquals(Integer.valueOf(10), this.list.get(3));
		assertEquals(Integer.valueOf(20), this.list.get(4));
		assertEquals(Integer.valueOf(21), this.list.get(5));
		assertEquals(Integer.valueOf(22), this.list.get(6));
		assertEquals(Integer.valueOf(23), this.list.get(7));
		assertEquals(Integer.valueOf(24), this.list.get(8));
		assertEquals(Integer.valueOf(25), this.list.get(9));
	}

    /**
     */
	@Test
	public void indexOfObject() {
		assertEquals(-1, this.list.indexOf(Integer.valueOf(0)));
		assertEquals(0,  this.list.indexOf(Integer.valueOf(1)));
		assertEquals(1,  this.list.indexOf(Integer.valueOf(2)));
		assertEquals(2,  this.list.indexOf(Integer.valueOf(3)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(4)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(5)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(6)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(7)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(8)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(9)));
		assertEquals(3,  this.list.indexOf(Integer.valueOf(10)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(11)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(12)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(13)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(14)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(15)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(16)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(17)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(18)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(19)));
		assertEquals(4,  this.list.indexOf(Integer.valueOf(20)));
		assertEquals(5,  this.list.indexOf(Integer.valueOf(21)));
		assertEquals(6,  this.list.indexOf(Integer.valueOf(22)));
		assertEquals(7,  this.list.indexOf(Integer.valueOf(23)));
		assertEquals(8,  this.list.indexOf(Integer.valueOf(24)));
		assertEquals(9,  this.list.indexOf(Integer.valueOf(25)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(26)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(27)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(28)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(29)));
		assertEquals(-1, this.list.indexOf(Integer.valueOf(30)));
	}

    /**
     */
	@Test
	public void lastIndexOfObject() {
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(0)));
		assertEquals(0,  this.list.lastIndexOf(Integer.valueOf(1)));
		assertEquals(1,  this.list.lastIndexOf(Integer.valueOf(2)));
		assertEquals(2,  this.list.lastIndexOf(Integer.valueOf(3)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(4)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(5)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(6)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(7)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(8)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(9)));
		assertEquals(3,  this.list.lastIndexOf(Integer.valueOf(10)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(11)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(12)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(13)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(14)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(15)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(16)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(17)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(18)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(19)));
		assertEquals(4,  this.list.lastIndexOf(Integer.valueOf(20)));
		assertEquals(5,  this.list.lastIndexOf(Integer.valueOf(21)));
		assertEquals(6,  this.list.lastIndexOf(Integer.valueOf(22)));
		assertEquals(7,  this.list.lastIndexOf(Integer.valueOf(23)));
		assertEquals(8,  this.list.lastIndexOf(Integer.valueOf(24)));
		assertEquals(9,  this.list.lastIndexOf(Integer.valueOf(25)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(26)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(27)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(28)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(29)));
		assertEquals(-1, this.list.lastIndexOf(Integer.valueOf(30)));
	}

    /**
     */
	@Test
	public void listIterator() {
		ListIterator<Integer> i;
		
		i = this.list.listIterator();

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

    /**
     */
	@Test
	public void listIteratorInt() {
		ListIterator<Integer> i;
		
		i = this.list.listIterator(2);

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

    /**
     */
	@Test
	public void removeInt() {
		Iterator<Integer> i;
		
		assertEquals(Integer.valueOf(2), this.list.remove(1));
		i = this.list.iterator();
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

		assertEquals(Integer.valueOf(22), this.list.remove(5));
		i = this.list.iterator();
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

    /**
     */
	@Test
	public void setIntInteger() {
		Iterator<Integer> i;
		
		assertEquals(Integer.valueOf(2), this.list.set(1, Integer.valueOf(123)));
		i = this.list.iterator();
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

		assertEquals(Integer.valueOf(3), this.list.set(1, Integer.valueOf(456)));
		i = this.list.iterator();
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

    /**
     */
	@Test
	public void subListIntInt() {
		List<Integer> l;
		Iterator<Integer> i;

		l = this.list.subList(0, 3);
		assertEquals(3, l.size());
		i = l.iterator();
		assertEquals(Integer.valueOf(1), i.next());
		assertEquals(Integer.valueOf(2), i.next());
		assertEquals(Integer.valueOf(3), i.next());

		l = this.list.subList(0, 5);
		assertEquals(5, l.size());
		i = l.iterator();
		assertEquals(Integer.valueOf(1), i.next());
		assertEquals(Integer.valueOf(2), i.next());
		assertEquals(Integer.valueOf(3), i.next());
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());

		l = this.list.subList(2, 5);
		assertEquals(3, l.size());
		i = l.iterator();
		assertEquals(Integer.valueOf(3), i.next());
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());

		l = this.list.subList(3, 5);
		assertEquals(2, l.size());
		i = l.iterator();
		assertEquals(Integer.valueOf(10), i.next());
		assertEquals(Integer.valueOf(20), i.next());
	}

}
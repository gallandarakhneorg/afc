/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import junit.framework.TestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GraphPathTest extends TestCase {

	private SegmentStub s1;
	private SegmentStub s2;
	private SegmentStub s3;
	private SegmentStub s4;
	private SegmentStub s5;
	private SegmentStub s6;
	private ConnectionStub c1;
	private ConnectionStub c2;
	private ConnectionStub c3;
	private ConnectionStub c4;
	private ConnectionStub c5;
	private ConnectionStub c6;
	private ConnectionStub c7;
	private GraphPathStub path;
	
	/**
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.path = new GraphPathStub();
		
		this.c1 = new ConnectionStub("c1[s1,s4]"); //$NON-NLS-1$
		this.c2 = new ConnectionStub("c2[s1,s2]"); //$NON-NLS-1$
		this.c3 = new ConnectionStub("c3[s2,s3,s4]"); //$NON-NLS-1$
		this.c4 = new ConnectionStub("c4[s3,s5]"); //$NON-NLS-1$
		this.c5 = new ConnectionStub("c5[s5]"); //$NON-NLS-1$
		this.c6 = new ConnectionStub("c6[s6]"); //$NON-NLS-1$
		this.c7 = new ConnectionStub("c7[s6]"); //$NON-NLS-1$
		
		this.s1 = new SegmentStub("s1", this.c1, this.c2); //$NON-NLS-1$
		this.s2 = new SegmentStub("s2", this.c2, this.c3); //$NON-NLS-1$
		this.s3 = new SegmentStub("s3", this.c3, this.c4); //$NON-NLS-1$
		this.s4 = new SegmentStub("s4", this.c1, this.c3); //$NON-NLS-1$
		this.s5 = new SegmentStub("s5", this.c4, this.c5); //$NON-NLS-1$
		this.s6 = new SegmentStub("s6", this.c6, this.c7); //$NON-NLS-1$
	}
	
	/**
	 */
	@Override
	public void tearDown() throws Exception {
		this.path = null;
		this.c1 = this.c2 = this.c3 = this.c4 = this.c5 = this.c6 = this.c7 = null;
		this.s1 = this.s2 = this.s3 = this.s4 = this.s5 = this.s6 = null;
		super.tearDown();
	}

	/**
	 */
	public void testSize() {
		assertEquals(0,this.path.size());
	}

	/**
	 */
	public void testIsEmpty() {
		assertTrue(this.path.isEmpty());
	}

	/**
	 */
	public void testGetStartingPointFor_notReversable() {
		this.path.setFirstSegmentReversable(false);
		
		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertSame(this.c5, this.path.getStartingPointFor(0));
		assertSame(this.c4, this.path.getStartingPointFor(1));
		assertSame(this.c3, this.path.getStartingPointFor(2));
		assertSame(this.c1, this.path.getStartingPointFor(3));
		assertSame(this.c2, this.path.getStartingPointFor(4));
		assertSame(this.c3, this.path.getStartingPointFor(5));
		try {
			this.path.getStartingPointFor(6);
			fail("expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}
	
	/**
	 */
	public void testGetStartingPointFor_reversable() {
		this.path.setFirstSegmentReversable(true);
		
		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertSame(this.c2, this.path.getStartingPointFor(0));
		assertSame(this.c1, this.path.getStartingPointFor(1));
		assertSame(this.c3, this.path.getStartingPointFor(2));
		assertSame(this.c2, this.path.getStartingPointFor(3));
		assertSame(this.c1, this.path.getStartingPointFor(4));
		assertSame(this.c2, this.path.getStartingPointFor(5));
		assertSame(this.c3, this.path.getStartingPointFor(6));
		assertSame(this.c4, this.path.getStartingPointFor(7));
		try {
			this.path.getStartingPointFor(8);
			fail("expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}
	
	/**
	 */
	public void testAddST_notReversable() {
		this.path.setFirstSegmentReversable(false);
		
		assertTrue(this.path.isEmpty());
		
		assertTrue(this.path.add(this.s1));
		assertFalse(this.path.isEmpty());
		assertEquals(1, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertTrue(this.path.add(this.s4));
		assertFalse(this.path.isEmpty());
		assertEquals(2, this.path.size());
		assertSame(this.s4, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s4, this.path.getAntepenulvianSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5));
		
		assertTrue(this.path.add(this.s2));
		assertFalse(this.path.isEmpty());
		assertEquals(3, this.path.size());
		assertSame(this.s4, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s1, this.path.getAntepenulvianSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5));

		assertFalse(this.path.add(this.s1));

		assertTrue(this.path.add(this.s3));		
		assertFalse(this.path.isEmpty());
		assertEquals(4, this.path.size());
		assertSame(this.s4, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s2, this.path.getAntepenulvianSegment());
		assertSame(this.s3, this.path.getLastSegment());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());

		assertTrue(this.path.add(this.s3));		
		assertFalse(this.path.isEmpty());
		assertEquals(5, this.path.size());
		assertSame(this.s4, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s3, this.path.getAntepenulvianSegment());
		assertSame(this.s3, this.path.getLastSegment());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
	}

	/**
	 */
	public void testAddST_reversable() {
		this.path.setFirstSegmentReversable(true);
		
		assertTrue(this.path.isEmpty());
		
		assertTrue(this.path.add(this.s1));
		assertFalse(this.path.isEmpty());
		assertEquals(1, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertTrue(this.path.add(this.s4));
		assertFalse(this.path.isEmpty());
		assertEquals(2, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getSecondSegment());
		assertSame(this.s1, this.path.getAntepenulvianSegment());
		assertSame(this.s4, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5));
		
		assertTrue(this.path.add(this.s2));
		assertFalse(this.path.isEmpty());
		assertEquals(3, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getSecondSegment());
		assertSame(this.s4, this.path.getAntepenulvianSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5));

		assertTrue(this.path.add(this.s1));
		assertFalse(this.path.isEmpty());
		assertEquals(4, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getSecondSegment());
		assertSame(this.s2, this.path.getAntepenulvianSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());

		assertFalse(this.path.add(this.s3));		
	}

	/**
	 */
	public void testAddST_FirstIsReversed() {
		assertTrue(this.path.isEmpty());
		
		assertTrue(this.path.add(this.s1, this.c2));
		assertFalse(this.path.isEmpty());
		assertEquals(1, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());

		assertTrue(this.path.add(this.s4));
		assertFalse(this.path.isEmpty());
		assertEquals(2, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getSecondSegment());
		assertSame(this.s1, this.path.getAntepenulvianSegment());
		assertSame(this.s4, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5));
		
		assertTrue(this.path.add(this.s2));
		assertFalse(this.path.isEmpty());
		assertEquals(3, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getSecondSegment());
		assertSame(this.s4, this.path.getAntepenulvianSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5));

		assertTrue(this.path.add(this.s1));		
		assertFalse(this.path.isEmpty());
		assertEquals(4, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getSecondSegment());
		assertSame(this.s2, this.path.getAntepenulvianSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());

		assertFalse(this.path.add(this.s3));		
	}

	/**
	 */
	public void testAddST_AddFirst() {
		assertTrue(this.path.isEmpty());
		
		assertTrue(this.path.add(this.s1));
		assertFalse(this.path.isEmpty());
		assertEquals(1, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertTrue(this.path.add(this.s2));
		assertFalse(this.path.isEmpty());
		assertEquals(2, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s2, this.path.getSecondSegment());
		assertSame(this.s1, this.path.getAntepenulvianSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5));
		
		assertTrue(this.path.add(this.s1));
		assertFalse(this.path.isEmpty());
		assertEquals(3, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s1, this.path.getAntepenulvianSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
		
		assertTrue(this.path.add(this.s3));
		assertFalse(this.path.isEmpty());
		assertEquals(4, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s2, this.path.getAntepenulvianSegment());
		assertSame(this.s3, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());

		assertTrue(this.path.add(this.s1));
		assertFalse(this.path.isEmpty());
		assertEquals(5, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s2, this.path.getAntepenulvianSegment());
		assertSame(this.s3, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());

		assertTrue(this.path.add(this.s5));
		assertFalse(this.path.isEmpty());
		assertEquals(6, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s3, this.path.getAntepenulvianSegment());
		assertSame(this.s5, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testPointIterator() {
		assertTrue(this.path.add(this.s1));
		assertTrue(this.path.add(this.s2));
		assertTrue(this.path.add(this.s3));
		assertTrue(this.path.add(this.s5));		
		assertTrue(this.path.add(this.s5));
		
		Iterator<ConnectionStub> iterator = this.path.pointIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.c1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.pointIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.c1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testPoints() {
		assertTrue(this.path.add(this.s1));
		assertTrue(this.path.add(this.s2));
		assertTrue(this.path.add(this.s3));
		assertTrue(this.path.add(this.s5));		
		assertTrue(this.path.add(this.s5));

		Iterable<ConnectionStub> iterable = this.path.points();
		Iterator<ConnectionStub> iterator = iterable.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.c1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = iterable.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.c1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.c4, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testAddSTPT() {
		assertTrue(this.path.isEmpty());
		
		assertTrue(this.path.add(this.s1, this.c2));
		assertFalse(this.path.isEmpty());
		assertEquals(1, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
		
		assertFalse(this.path.add(this.s4, this.c3));

		assertTrue(this.path.add(this.s4, this.c1));
		assertFalse(this.path.isEmpty());
		assertEquals(2, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getSecondSegment());
		assertSame(this.s1, this.path.getAntepenulvianSegment());
		assertSame(this.s4, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5, this.c5));
		assertFalse(this.path.add(this.s5, this.c4));
		
		assertFalse(this.path.add(this.s1, this.c1));
		
		assertTrue(this.path.add(this.s1, this.c2));
		assertFalse(this.path.isEmpty());
		assertEquals(3, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s1, this.path.getAntepenulvianSegment());
		assertSame(this.s4, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.add(this.s2, this.c2));
		
		assertTrue(this.path.add(this.s2, this.c3));
		assertFalse(this.path.isEmpty());
		assertEquals(4, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s4, this.path.getAntepenulvianSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.add(this.s5, this.c5));
		assertFalse(this.path.add(this.s5, this.c4));

		assertTrue(this.path.add(this.s1, this.c1));		
		assertFalse(this.path.isEmpty());
		assertEquals(5, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s4, this.path.getAntepenulvianSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertTrue(this.path.add(this.s1, this.c2));		
		assertFalse(this.path.isEmpty());
		assertEquals(6, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getSecondSegment());
		assertSame(this.s2, this.path.getAntepenulvianSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
	}
	
	/**
	 */
	public void testAddAllCollection_notReversable() {
		this.path.setFirstSegmentReversable(false);

		List<SegmentStub> collection = new ArrayList<SegmentStub>();
		collection.add(this.s1);
		collection.add(this.s4);
		collection.add(this.s5);
		collection.add(this.s1);
		collection.add(this.s2);
		collection.add(this.s5);
		collection.add(this.s1);
		collection.add(this.s1);
		
		assertTrue(this.path.addAll(collection));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	/**
	 */
	public void testAddAllCollection_reversable() {
		this.path.setFirstSegmentReversable(true);

		List<SegmentStub> collection = new ArrayList<SegmentStub>();
		collection.add(this.s1); // c1 - 1 - c2
		collection.add(this.s4); // c2 - 1 4 - c3
		collection.add(this.s5); // c2 - 1 4 - c3
		collection.add(this.s1); // c1 - 1 1 4 - c3
		collection.add(this.s2); // c1 - 1 1 4 2 - c2
		collection.add(this.s5); // c1 - 1 1 4 2 - c2
		collection.add(this.s1); // c1 - 1 1 4 2 1 - c1
		collection.add(this.s1); // c1 - 1 1 4 2 1 1 - c2

		assertTrue(this.path.addAll(collection));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testRetainAllCollection_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());

		assertTrue(this.path.retainAll(Arrays.asList(this.s2, this.s3)));
		// c5 - 5 3 4 1 2 2 - c2
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c4, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRetainAllCollection_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		assertTrue(this.path.retainAll(Arrays.asList(this.s2, this.s3)));
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());
	}

	/**
	 */
	public void testClear() {
		this.path.add(this.s1);
		this.path.add(this.s4);
		this.path.add(this.s2);
		this.path.add(this.s1);
		this.path.add(this.s1);
		assertFalse(this.path.isEmpty());
		
		this.path.clear();
		assertTrue(this.path.isEmpty());
	}

	/**
	 */
	public void testRemoveAllCollection_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertTrue(this.path.removeAll(Arrays.asList(this.s2, this.s3)));
		// c5 - 5 - c4
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveAllCollection_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertTrue(this.path.removeAll(Arrays.asList(this.s2, this.s3)));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
	}

	/**
	 */
	public void testGetInteger_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertSame(this.s5, this.path.get(0));
		assertSame(this.s3, this.path.get(1));
		assertSame(this.s4, this.path.get(2));
		assertSame(this.s1, this.path.get(3));
		assertSame(this.s2, this.path.get(4));
		assertSame(this.s2, this.path.get(5));
		try {
			this.path.get(6);
			fail("expecting IndexOutOfBounds"); //$NON-NLS-1$
		}
		catch (Exception _) {
			//
		}
	}

	/**
	 */
	public void testGetInteger_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertSame(this.s1, this.path.get(0));
		assertSame(this.s4, this.path.get(1));
		assertSame(this.s2, this.path.get(2));
		assertSame(this.s1, this.path.get(3));
		assertSame(this.s1, this.path.get(4));
		assertSame(this.s2, this.path.get(5));
		assertSame(this.s3, this.path.get(6));
		assertSame(this.s5, this.path.get(7));
		try {
			this.path.get(8);
			fail("expecting IndexOutOfBounds"); //$NON-NLS-1$
		}
		catch (Exception _) {
			//
		}
	}

	/**
	 */
	public void testSetIntegerST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertSame(this.s4, this.path.set(2, this.s3));
		// c5 - 5 3 3 - c4

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());
	}

	/**
	 */
	public void testSetIntegerST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertSame(this.s2, this.path.set(2, this.s3));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());
	}

	/**
	 */
	public void testAddIntegerST() {
		assertTrue(this.path.isEmpty());
		
		try {
			this.path.add(45, this.s1);
			fail("Expecting IndexOutOfBoundsException"); //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException _ ) {
			// Expected exception
		}
		
		this.path.add(0, this.s1); // c1 - 1 - c2
		assertFalse(this.path.isEmpty());
		assertEquals(1, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		this.path.add(0, this.s1); // c2 - 1 1 - c2
		assertFalse(this.path.isEmpty());
		assertEquals(2, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s1, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		try {
			this.path.add(1, this.s2);
			fail("Expecting IllegalArgumentException"); //$NON-NLS-1$
		}
		catch(IllegalArgumentException _) {
			// expected exception
		}
		
		this.path.add(2, this.s2);
		assertFalse(this.path.isEmpty());
		assertEquals(3, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s2, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		this.path.add(3, this.s4);
		assertFalse(this.path.isEmpty());
		assertEquals(4, this.path.size());
		assertSame(this.s1, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getLastSegment());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());

		this.path.add(0, this.s2);
		assertFalse(this.path.isEmpty());
		assertEquals(5, this.path.size());
		assertSame(this.s2, this.path.getFirstSegment());
		assertSame(this.s4, this.path.getLastSegment());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveInteger_InMiddle_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertSame(this.s1, this.path.remove(3));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveInteger_InMiddle_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertSame(this.s1, this.path.remove(3));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveInteger_First_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertSame(this.s5, this.path.remove(0));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c4, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveInteger_First_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertSame(this.s1, this.path.remove(0));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveInteger_Last_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertSame(this.s2, this.path.remove(5));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveInteger_Last_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertSame(this.s5, this.path.remove(7));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertFalse(iterator.hasNext());
		
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());
	}

	/**
	 */
	public void testIndexOfObject_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertEquals(3, this.path.indexOf(this.s1));
		assertEquals(4, this.path.indexOf(this.s2));
		assertEquals(1, this.path.indexOf(this.s3));
		assertEquals(2, this.path.indexOf(this.s4));
		assertEquals(0, this.path.indexOf(this.s5));
		assertEquals(-1, this.path.indexOf(this.s6));
	}

	/**
	 */
	public void testIndexOfObject_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertEquals(0, this.path.indexOf(this.s1));
		assertEquals(2, this.path.indexOf(this.s2));
		assertEquals(6, this.path.indexOf(this.s3));
		assertEquals(1, this.path.indexOf(this.s4));
		assertEquals(7, this.path.indexOf(this.s5));
		assertEquals(-1, this.path.indexOf(this.s6));
	}

	/**
	 */
	public void testLastIndexOfObject_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertEquals(3, this.path.lastIndexOf(this.s1));
		assertEquals(5, this.path.lastIndexOf(this.s2));
		assertEquals(1, this.path.lastIndexOf(this.s3));
		assertEquals(2, this.path.lastIndexOf(this.s4));
		assertEquals(0, this.path.lastIndexOf(this.s5));
		assertEquals(-1, this.path.lastIndexOf(this.s6));
	}

	/**
	 */
	public void testLastIndexOfObject_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertEquals(4, this.path.lastIndexOf(this.s1));
		assertEquals(5, this.path.lastIndexOf(this.s2));
		assertEquals(6, this.path.lastIndexOf(this.s3));
		assertEquals(1, this.path.lastIndexOf(this.s4));
		assertEquals(7, this.path.lastIndexOf(this.s5));
		assertEquals(-1, this.path.lastIndexOf(this.s6));
	}

	/**
	 */
	public void testListIterator_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		ListIterator<SegmentStub> iterator = this.path.listIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testListIterator_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		ListIterator<SegmentStub> iterator = this.path.listIterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testListIteratorInteger_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		ListIterator<SegmentStub> iterator = this.path.listIterator(0);
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(1);
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(2);
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(3);
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(4);
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(5);
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(6);
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testListIteratorInteger_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		ListIterator<SegmentStub> iterator = this.path.listIterator(0);
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(1);
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(2);
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(3);
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(4);
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(5);
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(6);
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(7);
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.path.listIterator(8);
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testSubListIntegerInteger_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		List<SegmentStub> subList = this.path.subList(0, 4);
		Iterator<SegmentStub> iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(1, 4);
		iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(2, 4);
		iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(3, 4);
		iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(4, 4);
		iterator = subList.iterator();
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testSubListIntegerInteger_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		List<SegmentStub> subList = this.path.subList(0, 4);
		// c2 - 1 4 2 1 - c1
		Iterator<SegmentStub> iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(1, 4);
		// c1 - 4 2 1 - c1
		iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(2, 4);
		// c3 - 2 1 - c1
		iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(3, 4);
		// c2 - 1 - c1
		iterator = subList.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());

		subList = this.path.subList(4, 4);
		iterator = subList.iterator();
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testRemoveBeforeST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeBefore(this.s6));
		
		assertFalse(this.path.removeBefore(this.s5));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertTrue(this.path.removeBefore(this.s2));

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.removeBefore(this.s5));
		
		assertFalse(this.path.removeBefore(this.s2));
	}

	/**
	 */
	public void testRemoveBeforeST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeBefore(this.s6));
		
		assertFalse(this.path.removeBefore(this.s1));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());

		assertTrue(this.path.removeBefore(this.s2));
		// c3 - 2 1 1 2 3 5 - c5

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());

		assertFalse(this.path.removeBefore(this.s2));
		
		assertFalse(this.path.removeBefore(this.s4));
	}

	/**
	 */
	public void testRemoveUntilST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeUntil(this.s6));
		
		assertTrue(this.path.removeUntil(this.s2));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertTrue(this.path.removeUntil(this.s2));
		
		iterator = this.path.iterator();
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testRemoveUntilST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeUntil(this.s6));
		
		assertTrue(this.path.removeUntil(this.s2));
		// c2 - 1 1 2 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());

		assertTrue(this.path.removeUntil(this.s2));
		// c3 - 3 5 - c5
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveBeforeLastST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeBeforeLast(this.s6));
		
		assertTrue(this.path.removeBeforeLast(this.s2));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.removeBeforeLast(this.s2));
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveBeforeLastST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeBeforeLast(this.s6));
		
		assertTrue(this.path.removeBeforeLast(this.s2));
		// c2 - 2 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());

		assertFalse(this.path.removeBeforeLast(this.s2));
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveUntilLastST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeUntilLast(this.s6));
		
		assertTrue(this.path.removeUntilLast(this.s2));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testRemoveUntilLastST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeUntilLast(this.s6));
		
		assertTrue(this.path.removeUntilLast(this.s2));
		// c3 - 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveBeforeSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeBefore(this.s6, this.c6));
		
		assertFalse(this.path.removeBefore(this.s1, this.c2));

		assertTrue(this.path.removeBefore(this.s1,this.c1));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveBeforeSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeBefore(this.s6, this.c6));
		
		assertFalse(this.path.removeBefore(this.s4, this.c3));

		assertTrue(this.path.removeBefore(this.s1,this.c1));
		// c1 - 1 2 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveUntilSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeUntil(this.s6, this.c6));
		
		assertFalse(this.path.removeUntil(this.s1, this.c2));

		assertTrue(this.path.removeUntil(this.s1,this.c1));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveUntilSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeUntil(this.s6, this.c6));
		
		assertFalse(this.path.removeUntil(this.s4, this.c3));

		assertTrue(this.path.removeUntil(this.s1,this.c1));
		// c2 - 2 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveBeforeLastSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeBeforeLast(this.s6, this.c6));
		
		assertFalse(this.path.removeBeforeLast(this.s1, this.c2));

		assertTrue(this.path.removeBeforeLast(this.s1,this.c1));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveBeforeLastSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeBeforeLast(this.s6, this.c6));
		
		assertFalse(this.path.removeBeforeLast(this.s4, this.c3));

		assertTrue(this.path.removeBeforeLast(this.s1,this.c1));
		// c1 - 1 2 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c1, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveUntilLastSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeUntilLast(this.s6, this.c6));
		
		assertFalse(this.path.removeUntilLast(this.s1, this.c2));

		assertTrue(this.path.removeUntilLast(this.s1,this.c1));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}
		
	/**
	 */
	public void testRemoveUntilLastSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeUntilLast(this.s6, this.c6));
		
		assertFalse(this.path.removeUntilLast(this.s4, this.c3));

		assertTrue(this.path.removeUntilLast(this.s1,this.c1));
		// c2 - 2 3 5 - c5
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());
	}

	/**
	 */
	public void testInvert_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		this.path.invert();
		
		assertSame(this.s2, this.path.get(0));
		assertSame(this.s2, this.path.get(1));
		assertSame(this.s1, this.path.get(2));
		assertSame(this.s4, this.path.get(3));
		assertSame(this.s3, this.path.get(4));
		assertSame(this.s5, this.path.get(5));
	}

	/**
	 */
	public void testInvert_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		this.path.invert();
		
		assertSame(this.s5, this.path.get(0));
		assertSame(this.s3, this.path.get(1));
		assertSame(this.s2, this.path.get(2));
		assertSame(this.s1, this.path.get(3));
		assertSame(this.s1, this.path.get(4));
		assertSame(this.s2, this.path.get(5));
		assertSame(this.s4, this.path.get(6));
		assertSame(this.s1, this.path.get(7));
	}

	/**
	 */
	public void testRemoveAfterST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeAfter(this.s6));
		
		assertTrue(this.path.removeAfter(this.s2));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.removeAfter(this.s2));
		
		assertTrue(this.path.removeAfter(this.s1));
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveAfterST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeAfter(this.s6));
		
		assertTrue(this.path.removeAfter(this.s2));
		// c2 - 1 4 2 - c2
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.removeAfter(this.s2));
		// c2 - 1 4 2 - c2
		
		assertTrue(this.path.removeAfter(this.s1));
		// c2 - 1 - c1
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveFromST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeFrom(this.s6));
		
		assertTrue(this.path.removeFrom(this.s2));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertFalse(this.path.removeFrom(this.s2));
	}

	/**
	 */
	public void testRemoveFromST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeFrom(this.s6));
		
		assertTrue(this.path.removeFrom(this.s2));
		// c2 - 1 4 - c3
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.removeFrom(this.s2));
	}

	/**
	 */
	public void testRemoveAfterLastST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeAfterLast(this.s6));
		
		assertFalse(this.path.removeAfterLast(this.s2));
		
		assertTrue(this.path.removeAfterLast(this.s1));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveAfterLastST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeAfterLast(this.s6));
		
		assertFalse(this.path.removeAfterLast(this.s5));
		
		assertTrue(this.path.removeAfterLast(this.s1));
		// c2 - 1 4 2 1 1 - c2
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveFromLastST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeFromLast(this.s6));
		
		assertTrue(this.path.removeFromLast(this.s2));
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveFromLastST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeFromLast(this.s6));
		
		assertTrue(this.path.removeFromLast(this.s2));
		// c2 - 1 4 2 1 1 - c2
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveAfterSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeAfter(this.s6, this.c6));
		
		assertFalse(this.path.removeAfter(this.s2, this.c3));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());

		assertTrue(this.path.removeAfter(this.s2, this.c2));

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.removeAfter(this.s5,this.c4));
		
		assertFalse(this.path.removeAfter(this.s5,this.c4));

		assertTrue(this.path.removeAfter(this.s5,this.c5));

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());

		assertFalse(this.path.removeAfter(this.s5, this.c5));
	}

	/**
	 */
	public void testRemoveAfterSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeAfter(this.s6, this.c6));
		
		assertFalse(this.path.removeAfter(this.s4, this.c3));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c5, this.path.getLastPoint());

		assertTrue(this.path.removeAfter(this.s2, this.c2));
		// c2 - 1 4 2 1 1 2 - c3
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.removeAfter(this.s5,this.c4));
		// c2 - 1 4 2 1 1 2 - c3
		
		assertFalse(this.path.removeAfter(this.s2,this.c2));
		// c2 - 1 4 2 1 1 2 - c3
	}

	/**
	 */
	public void testRemoveFromSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeFrom(this.s6, this.c6));
		
		assertTrue(this.path.removeFrom(this.s2, this.c3));
		// c5 - 5 3 4 1 2 - c3

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());

		assertFalse(this.path.removeFrom(this.s2, this.c3));
		// c5 - 5 3 4 1 2 - c3

		assertFalse(this.path.removeFrom(this.s5,this.c4));
		// c5 - 5 3 4 1 2 - c3
		
		assertFalse(this.path.removeFrom(this.s5,this.c4));
		// c5 - 5 3 4 1 2 - c3

		assertTrue(this.path.removeFrom(this.s5,this.c5));
		// c5 - 5 3 4 1 2 - c3

		iterator = this.path.iterator();
		assertFalse(iterator.hasNext());
		assertNull(this.path.getFirstPoint());
		assertNull(this.path.getLastPoint());

		assertFalse(this.path.removeFrom(this.s5,this.c5));
	}

	/**
	 */
	public void testRemoveFromSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeFrom(this.s6, this.c6));
		
		assertTrue(this.path.removeFrom(this.s2, this.c3));
		// c2 - 1 4 - c3
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveAfterLastSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeAfterLast(this.s6, this.c6));
		
		assertFalse(this.path.removeAfterLast(this.s2, this.c3));

		assertTrue(this.path.removeAfterLast(this.s2, this.c2));
		// c5 - 5 3 4 1 2 - c3
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveAfterLastSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeAfterLast(this.s6, this.c6));
		
		assertTrue(this.path.removeAfterLast(this.s1, this.c2));
		// c2 - 1 4 2 1 - c1
		
		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveFromLastSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2
		
		assertFalse(this.path.removeFromLast(this.s6, this.c6));
		
		assertTrue(this.path.removeFromLast(this.s2, this.c3));

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
	}

	/**
	 */
	public void testRemoveFromLastSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5
		
		assertFalse(this.path.removeFromLast(this.s6, this.c6));
		
		assertTrue(this.path.removeFromLast(this.s1, this.c2));
		// c2 - 1 4 2 - c2

		Iterator<SegmentStub> iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPath<?,SegmentStub,ConnectionStub> sp;
		
		sp = this.path.splitAfter(this.s4);

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c1, sp.getFirstPoint());
		assertSame(this.c2, sp.getLastPoint());

		sp = this.path.splitAfter(this.s4);

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPath<?,SegmentStub,ConnectionStub> sp;
		
		sp = this.path.splitAfter(this.s4);
		// c2 - 1 4 - c3
		// c3 - 2 1 1 2 3 5 - c5

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());

		sp = this.path.splitAfter(this.s4);
		// c2 - 1 4 - c3
		// empty

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAfter(this.s4, this.c1);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAfter(this.s5, this.c4);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAfter(this.s4, this.c3);

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAfter(this.s4, this.c1);
		// c2 - 1 4 - c3
		// c3 - 2 1 1 2 3 5 - c5
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());

		sp = this.path.splitAfter(this.s4, this.c1);
		// c2 - 1 4 - c3
		// empty

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterLastST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAfterLast(this.s2);

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAfterLast(this.s4);

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());

		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c1, sp.getFirstPoint());
		assertSame(this.c2, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterLastST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAfterLast(this.s2);
		// c2 - 1 4 2 1 1 2 - c3
		// c3 - 3 5 - c5

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());

		sp = this.path.splitAfterLast(this.s2);

		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterLastSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAfter(this.s6, this.c6);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAfter(this.s5, this.c4);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAfterLast(this.s5, this.c5);

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());
		
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c4, sp.getFirstPoint());
		assertSame(this.c2, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAfterLastSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAfter(this.s6, this.c6);
		// c2 - 1 4 2 1 1 2 3 5 - c5
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAfter(this.s5, this.c4);
		// c2 - 1 4 2 1 1 2 3 5 - c5
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAfterLast(this.s1, this.c1);
		// c2 - 1 4 2 1 1 - c2
		// c2 - 2 3 5 - c5
		
		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
		
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAtST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAt(this.s1);

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, this.path.getFirstPoint());
		assertSame(this.c1, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c1, sp.getFirstPoint());
		assertSame(this.c2, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAtST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAt(this.s2);
		// c2 - 1 4 - c3
		// c3 - 2 1 1 2 3 5 - c5

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c3, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c3, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAtSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAt(this.s6, this.c6);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAt(this.s5, this.c4);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAt(this.s5, this.c5);

		iterator = this.path.iterator();
		assertFalse(iterator.hasNext());
		assertNull(this.path.getFirstPoint());
		assertNull(this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, sp.getFirstPoint());
		assertSame(this.c2, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAtSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAt(this.s6, this.c6);
		// c2 - 1 4 2 1 1 2 3 5 - c5
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAt(this.s5, this.c5);
		// c2 - 1 4 2 1 1 2 3 5 - c5
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAt(this.s5, this.c4);
		// c2 - 1 4 2 1 1 2 3 - c4
		// c4 - 5 - c5

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c4, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c4, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAtLastST_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAtLast(this.s6);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAtLast(this.s5);

		iterator = this.path.iterator();
		assertFalse(iterator.hasNext());
		assertNull(this.path.getFirstPoint());
		assertNull(this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, sp.getFirstPoint());
		assertSame(this.c2, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAtLastST_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAtLast(this.s6);
		// c2 - 1 4 2 1 1 2 3 5 - c5
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAtLast(this.s2);
		// c2 - 1 4 2 1 1 - c2
		// c2 - 2 3 5 - c5

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());
	}

	/**
	 */
	public void testSplitAtLastSTPT_notReversable() {
		this.path.setFirstSegmentReversable(false);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c3 - 4 1 - c2
		this.path.add(this.s2); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s1); // c3 - 4 1 2 - c3
		this.path.add(this.s2); // c3 - 4 1 2 2 - c2
		this.path.add(this.s3); // c4 - 3 4 1 2 2 - c2
		this.path.add(this.s5); // c5 - 5 3 4 1 2 2 - c2

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAtLast(this.s6, this.c6);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAtLast(this.s5, this.c4);
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAtLast(this.s5, this.c5);

		iterator = this.path.iterator();
		assertFalse(iterator.hasNext());
		assertNull(this.path.getFirstPoint());
		assertNull(this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c5, sp.getFirstPoint());
		assertSame(this.c2, sp.getLastPoint());
	}
	
	/**
	 */
	public void testSplitAtLastSTPT_reversable() {
		this.path.setFirstSegmentReversable(true);

		this.path.add(this.s1); // c1 - 1 - c2
		this.path.add(this.s4); // c2 - 1 4 - c3
		this.path.add(this.s2); // c2 - 1 4 2 - c2
		this.path.add(this.s1); // c2 - 1 4 2 1 - c1
		this.path.add(this.s1); // c2 - 1 4 2 1 1 - c2
		this.path.add(this.s2); // c2 - 1 4 2 1 1 2 - c3
		this.path.add(this.s3); // c2 - 1 4 2 1 1 2 3 - c4
		this.path.add(this.s5); // c2 - 1 4 2 1 1 2 3 5 - c5

		Iterator<SegmentStub> iterator;
		GraphPathStub sp;
		
		sp = this.path.splitAtLast(this.s6, this.c6);
		// c2 - 1 4 2 1 1 2 3 5 - c5
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());
		
		sp = this.path.splitAtLast(this.s5, this.c5);
		// c2 - 1 4 2 1 1 2 3 5 - c5
		assertNotNull(sp);
		iterator = sp.iterator();
		assertFalse(iterator.hasNext());
		assertNull(sp.getFirstPoint());
		assertNull(sp.getLastPoint());

		sp = this.path.splitAtLast(this.s1, this.c2);
		// c2 - 1 4 2 - c2
		// c2 - 1 1 2 3 5 - c5

		iterator = this.path.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s4, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, this.path.getFirstPoint());
		assertSame(this.c2, this.path.getLastPoint());
		
		assertNotNull(sp);
		iterator = sp.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s1, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s2, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s3, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.s5, iterator.next());
		assertFalse(iterator.hasNext());
		assertSame(this.c2, sp.getFirstPoint());
		assertSame(this.c5, sp.getLastPoint());
	}

}


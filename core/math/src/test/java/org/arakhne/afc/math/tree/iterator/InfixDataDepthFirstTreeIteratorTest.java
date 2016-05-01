/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
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
package org.arakhne.afc.math.tree.iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.tree.node.BinaryTreeNode.DefaultBinaryTreeNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class InfixDataDepthFirstTreeIteratorTest extends AbstractDataTreeIteratorTest {

	private InfixDataDepthFirstTreeIterator<Object,DefaultBinaryTreeNode<Object>> iterator;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.iterator = new InfixDataDepthFirstTreeIterator<>(this.tree);
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		this.iterator = null;
		super.tearDown();
	}

	@Test
	@Override
	public void iterate() {
		assertTrue(this.iterator.hasNext());
		assertEquals("b", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("a", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("h", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("e", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("f", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("g", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("j", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("k", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("i", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("l", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("m", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("n", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("c", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("d", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("q", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("r", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("o", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("p", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("s", this.iterator.next()); //$NON-NLS-1$

		assertTrue(this.iterator.hasNext());
		assertEquals("t", this.iterator.next()); //$NON-NLS-1$

		assertFalse(this.iterator.hasNext());
	}
	
	@Test
	@Override
	public void remove() {
		//
		// Remove o
		//
		assertEquals("b", this.iterator.next()); //$NON-NLS-1$
		assertEquals("a", this.iterator.next()); //$NON-NLS-1$
		assertEquals("h", this.iterator.next()); //$NON-NLS-1$
		assertEquals("e", this.iterator.next()); //$NON-NLS-1$
		assertEquals("f", this.iterator.next()); //$NON-NLS-1$
		assertEquals("g", this.iterator.next()); //$NON-NLS-1$
		assertEquals("j", this.iterator.next()); //$NON-NLS-1$
		assertEquals("k", this.iterator.next()); //$NON-NLS-1$
		assertEquals("i", this.iterator.next()); //$NON-NLS-1$
		assertEquals("l", this.iterator.next()); //$NON-NLS-1$
		assertEquals("m", this.iterator.next()); //$NON-NLS-1$
		assertEquals("n", this.iterator.next()); //$NON-NLS-1$
		assertEquals("c", this.iterator.next()); //$NON-NLS-1$
		assertEquals("d", this.iterator.next()); //$NON-NLS-1$
		assertEquals("q", this.iterator.next()); //$NON-NLS-1$
		assertEquals("r", this.iterator.next()); //$NON-NLS-1$
		assertEquals("o", this.iterator.next()); //$NON-NLS-1$

		this.iterator.remove();
		try {
			this.iterator.remove();
			fail("Expecting NoSuchElementException"); //$NON-NLS-1$
		}
		catch(NoSuchElementException e) {
			// Expected exception
		}

		assertEquals("p", this.iterator.next()); //$NON-NLS-1$
		assertEquals("s", this.iterator.next()); //$NON-NLS-1$
		assertEquals("t", this.iterator.next()); //$NON-NLS-1$
		assertFalse(this.iterator.hasNext());

		Iterator<Object> it = this.tree.dataBroadFirstIterator();
		assertEquals("a", it.next()); //$NON-NLS-1$
		assertEquals("b", it.next()); //$NON-NLS-1$
		assertEquals("c", it.next()); //$NON-NLS-1$
		assertEquals("d", it.next()); //$NON-NLS-1$
		assertEquals("p", it.next()); //$NON-NLS-1$
		assertEquals("e", it.next()); //$NON-NLS-1$
		assertEquals("f", it.next()); //$NON-NLS-1$
		assertEquals("g", it.next()); //$NON-NLS-1$
		assertEquals("q", it.next()); //$NON-NLS-1$
		assertEquals("r", it.next()); //$NON-NLS-1$
		assertEquals("s", it.next()); //$NON-NLS-1$
		assertEquals("t", it.next()); //$NON-NLS-1$
		assertEquals("h", it.next()); //$NON-NLS-1$
		assertEquals("i", it.next()); //$NON-NLS-1$
		assertEquals("j", it.next()); //$NON-NLS-1$
		assertEquals("k", it.next()); //$NON-NLS-1$
		assertEquals("l", it.next()); //$NON-NLS-1$
		assertEquals("m", it.next()); //$NON-NLS-1$
		assertEquals("n", it.next()); //$NON-NLS-1$
		assertFalse(it.hasNext());

		//
		// Remove f
		//
		this.iterator = new InfixDataDepthFirstTreeIterator<>(this.tree);

		assertEquals("b", this.iterator.next()); //$NON-NLS-1$
		assertEquals("a", this.iterator.next()); //$NON-NLS-1$
		assertEquals("h", this.iterator.next()); //$NON-NLS-1$
		assertEquals("e", this.iterator.next()); //$NON-NLS-1$
		assertEquals("f", this.iterator.next()); //$NON-NLS-1$

		this.iterator.remove();
		try {
			this.iterator.remove();
			fail("Expecting NoSuchElementException"); //$NON-NLS-1$
		}
		catch(NoSuchElementException e) {
			// Expected exception
		}

		assertEquals("g", this.iterator.next()); //$NON-NLS-1$
		assertEquals("j", this.iterator.next()); //$NON-NLS-1$
		assertEquals("k", this.iterator.next()); //$NON-NLS-1$
		assertEquals("i", this.iterator.next()); //$NON-NLS-1$
		assertEquals("l", this.iterator.next()); //$NON-NLS-1$
		assertEquals("m", this.iterator.next()); //$NON-NLS-1$
		assertEquals("n", this.iterator.next()); //$NON-NLS-1$
		assertEquals("c", this.iterator.next()); //$NON-NLS-1$
		assertEquals("d", this.iterator.next()); //$NON-NLS-1$
		assertEquals("q", this.iterator.next()); //$NON-NLS-1$
		assertEquals("r", this.iterator.next()); //$NON-NLS-1$
		assertEquals("p", this.iterator.next()); //$NON-NLS-1$
		assertEquals("s", this.iterator.next()); //$NON-NLS-1$
		assertEquals("t", this.iterator.next()); //$NON-NLS-1$
		assertFalse(this.iterator.hasNext());

		it = this.tree.dataBroadFirstIterator();
		assertEquals("a", it.next()); //$NON-NLS-1$
		assertEquals("b", it.next()); //$NON-NLS-1$
		assertEquals("c", it.next()); //$NON-NLS-1$
		assertEquals("d", it.next()); //$NON-NLS-1$
		assertEquals("p", it.next()); //$NON-NLS-1$
		assertEquals("e", it.next()); //$NON-NLS-1$
		assertEquals("g", it.next()); //$NON-NLS-1$
		assertEquals("q", it.next()); //$NON-NLS-1$
		assertEquals("r", it.next()); //$NON-NLS-1$
		assertEquals("s", it.next()); //$NON-NLS-1$
		assertEquals("t", it.next()); //$NON-NLS-1$
		assertEquals("h", it.next()); //$NON-NLS-1$
		assertEquals("i", it.next()); //$NON-NLS-1$
		assertEquals("j", it.next()); //$NON-NLS-1$
		assertEquals("k", it.next()); //$NON-NLS-1$
		assertEquals("l", it.next()); //$NON-NLS-1$
		assertEquals("m", it.next()); //$NON-NLS-1$
		assertEquals("n", it.next()); //$NON-NLS-1$
		assertFalse(it.hasNext());
	}
	
}
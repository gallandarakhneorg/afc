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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

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
public class BroadFirstTreeIteratorTest extends AbstractTreeIteratorTest {

	private IterationListener listener;
	private BroadFirstTreeIterator<DefaultBinaryTreeNode<Object>> iterator;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.listener = new IterationListener();
		this.iterator = new BroadFirstTreeIterator<>(this.tree);
		this.iterator.setBroadFirstIterationListener(this.listener);
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		this.listener = null;
		this.iterator.setBroadFirstIterationListener(null);
		this.iterator = null;
		super.tearDown();
	}

	@Test
	@Override
	public void iterate() {
		assertTrue(this.iterator.hasNext());
		assertSame(this.root, this.iterator.next());
		this.listener.assertInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child1, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child2, this.iterator.next());
		this.listener.assertInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child11, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child12, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child21, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child22, this.iterator.next());
		this.listener.assertInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child121, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child122, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child211, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child222, this.iterator.next());
		this.listener.assertInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child1211, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child1212, this.iterator.next());
		this.listener.assertInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child12121, this.iterator.next());
		this.listener.assertNotInvoked();

		assertTrue(this.iterator.hasNext());
		assertSame(this.child12122, this.iterator.next());
		this.listener.assertInvoked();

		assertFalse(this.iterator.hasNext());
	}
	
	@Test
	@Override
	public void remove() {
		assertSame(this.root, this.iterator.next());
		assertSame(this.child1, this.iterator.next());
		assertSame(this.child2, this.iterator.next());
		assertSame(this.child11, this.iterator.next());
		assertSame(this.child12, this.iterator.next());
		assertSame(this.child21, this.iterator.next());
		
		this.iterator.remove();
		try {
			this.iterator.remove();
			fail("Expecting NoSuchElementException"); //$NON-NLS-1$
		}
		catch(NoSuchElementException _) {
			// Expected exception
		}
		
		assertSame(this.child22, this.iterator.next());
		assertSame(this.child121, this.iterator.next());
		assertSame(this.child122, this.iterator.next());
		assertSame(this.child211, this.iterator.next());
		assertSame(this.child222, this.iterator.next());
		assertSame(this.child1211, this.iterator.next());
		assertSame(this.child1212, this.iterator.next());
		assertSame(this.child12121, this.iterator.next());
		assertSame(this.child12122, this.iterator.next());
		assertFalse(this.iterator.hasNext());
		
		Iterator<DefaultBinaryTreeNode<Object>> it = this.tree.broadFirstIterator();
		assertSame(this.root, it.next());
		assertSame(this.child1, it.next());
		assertSame(this.child2, it.next());
		assertSame(this.child11, it.next());
		assertSame(this.child12, it.next());
		assertSame(this.child22, it.next());
		assertSame(this.child121, it.next());
		assertSame(this.child122, it.next());
		assertSame(this.child222, it.next());
		assertSame(this.child1211, it.next());
		assertSame(this.child1212, it.next());
		assertSame(this.child12121, it.next());
		assertSame(this.child12122, it.next());
		assertFalse(it.hasNext());
	}

	private static class IterationListener implements BroadFirstIterationListener {
		
		private final AtomicBoolean invoked = new AtomicBoolean(false);
		
		/**
		 */
		public IterationListener() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onBoardFirstIterationLevelFinished() {
			this.invoked.set(true);
		}

		public void assertInvoked() {
			assertTrue(this.invoked.getAndSet(false));
		}
		
		public void assertNotInvoked() {
			assertFalse(this.invoked.getAndSet(false));
		}

	}
	
}
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

package org.arakhne.afc.math.tree.iterator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.tree.node.BinaryTreeNode.DefaultBinaryTreeNode;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PrefixDepthFirstTreeIteratorTest extends AbstractTreeIteratorTest {

	private PrefixDepthFirstTreeIterator<DefaultBinaryTreeNode<Object>> iterator;

	@BeforeEach
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.iterator = new PrefixDepthFirstTreeIterator<>(this.tree);
	}

	@AfterEach
	@Override
	public void tearDown() throws Exception {
		this.iterator = null;
		super.tearDown();
	}

	@Test
	@Override
	public void iterate() {
		assertTrue(this.iterator.hasNext());
		assertSame(this.root, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child1, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child11, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child12, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child121, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child1211, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child1212, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child12121, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child12122, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child122, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child2, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child21, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child211, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child22, this.iterator.next());

		assertTrue(this.iterator.hasNext());
		assertSame(this.child222, this.iterator.next());

		assertFalse(this.iterator.hasNext());
	}
	
	@Test
	@Override
	public void remove() {
		assertSame(this.root, this.iterator.next());
		assertSame(this.child1, this.iterator.next());
		assertSame(this.child11, this.iterator.next());
		assertSame(this.child12, this.iterator.next());
		assertSame(this.child121, this.iterator.next());
		assertSame(this.child1211, this.iterator.next());
		assertSame(this.child1212, this.iterator.next());
		assertSame(this.child12121, this.iterator.next());
		assertSame(this.child12122, this.iterator.next());
		assertSame(this.child122, this.iterator.next());
		assertSame(this.child2, this.iterator.next());
		assertSame(this.child21, this.iterator.next());

		this.iterator.remove();
		try {
			this.iterator.remove();
			fail("Expecting NoSuchElementException");  //$NON-NLS-1$
		}
		catch(NoSuchElementException e) {
			// Expected exception
		}

		assertSame(this.child211, this.iterator.next());
		assertSame(this.child22, this.iterator.next());
		assertSame(this.child222, this.iterator.next());
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
	
}
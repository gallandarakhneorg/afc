/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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
@SuppressWarnings("all")
public class PrefixDataDepthFirstTreeIteratorTest extends AbstractDataTreeIteratorTest {

	private PrefixDataDepthFirstTreeIterator<Object,DefaultBinaryTreeNode<Object>> iterator;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.iterator = new PrefixDataDepthFirstTreeIterator<>(this.tree);
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
		assertEquals("a", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("b", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("c", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("d", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("e", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("f", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("g", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("h", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("i", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("j", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("k", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("l", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("m", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("n", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("o", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("q", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("r", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("p", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("s", this.iterator.next()); 

		assertTrue(this.iterator.hasNext());
		assertEquals("t", this.iterator.next()); 

		assertFalse(this.iterator.hasNext());
	}
	
	@Test
	@Override
	public void remove() {
		//
		// Remove o
		//
		assertEquals("a", this.iterator.next()); 
		assertEquals("b", this.iterator.next()); 
		assertEquals("c", this.iterator.next()); 
		assertEquals("d", this.iterator.next()); 
		assertEquals("e", this.iterator.next()); 
		assertEquals("f", this.iterator.next()); 
		assertEquals("g", this.iterator.next()); 
		assertEquals("h", this.iterator.next()); 
		assertEquals("i", this.iterator.next()); 
		assertEquals("j", this.iterator.next()); 
		assertEquals("k", this.iterator.next()); 
		assertEquals("l", this.iterator.next()); 
		assertEquals("m", this.iterator.next()); 
		assertEquals("n", this.iterator.next()); 
		assertEquals("o", this.iterator.next()); 
		
		this.iterator.remove();
		try {
			this.iterator.remove();
			fail("Expecting NoSuchElementException"); 
		}
		catch(NoSuchElementException e) {
			// Expected exception
		}
		
		assertEquals("q", this.iterator.next()); 
		assertEquals("r", this.iterator.next()); 
		assertEquals("p", this.iterator.next()); 
		assertEquals("s", this.iterator.next()); 
		assertEquals("t", this.iterator.next()); 
		assertFalse(this.iterator.hasNext());

		Iterator<Object> it = this.tree.dataBroadFirstIterator();
		assertEquals("a", it.next()); 
		assertEquals("b", it.next()); 
		assertEquals("c", it.next()); 
		assertEquals("d", it.next()); 
		assertEquals("p", it.next()); 
		assertEquals("e", it.next()); 
		assertEquals("f", it.next()); 
		assertEquals("g", it.next()); 
		assertEquals("q", it.next()); 
		assertEquals("r", it.next()); 
		assertEquals("s", it.next()); 
		assertEquals("t", it.next()); 
		assertEquals("h", it.next()); 
		assertEquals("i", it.next()); 
		assertEquals("j", it.next()); 
		assertEquals("k", it.next()); 
		assertEquals("l", it.next()); 
		assertEquals("m", it.next()); 
		assertEquals("n", it.next()); 
		assertFalse(it.hasNext());

		//
		// Remove f
		//
		this.iterator = new PrefixDataDepthFirstTreeIterator<>(this.tree);

		assertEquals("a", this.iterator.next()); 
		assertEquals("b", this.iterator.next()); 
		assertEquals("c", this.iterator.next()); 
		assertEquals("d", this.iterator.next()); 
		assertEquals("e", this.iterator.next()); 
		assertEquals("f", this.iterator.next()); 
		
		this.iterator.remove();
		try {
			this.iterator.remove();
			fail("Expecting NoSuchElementException"); 
		}
		catch(NoSuchElementException e) {
			// Expected exception
		}
		
		assertEquals("g", this.iterator.next()); 
		assertEquals("h", this.iterator.next()); 
		assertEquals("i", this.iterator.next()); 
		assertEquals("j", this.iterator.next()); 
		assertEquals("k", this.iterator.next()); 
		assertEquals("l", this.iterator.next()); 
		assertEquals("m", this.iterator.next()); 
		assertEquals("n", this.iterator.next()); 
		assertEquals("q", this.iterator.next()); 
		assertEquals("r", this.iterator.next()); 
		assertEquals("p", this.iterator.next()); 
		assertEquals("s", this.iterator.next()); 
		assertEquals("t", this.iterator.next()); 
		assertFalse(this.iterator.hasNext());

		it = this.tree.dataBroadFirstIterator();
		assertEquals("a", it.next()); 
		assertEquals("b", it.next()); 
		assertEquals("c", it.next()); 
		assertEquals("d", it.next()); 
		assertEquals("p", it.next()); 
		assertEquals("e", it.next()); 
		assertEquals("g", it.next()); 
		assertEquals("q", it.next()); 
		assertEquals("r", it.next()); 
		assertEquals("s", it.next()); 
		assertEquals("t", it.next()); 
		assertEquals("h", it.next()); 
		assertEquals("i", it.next()); 
		assertEquals("j", it.next()); 
		assertEquals("k", it.next()); 
		assertEquals("l", it.next()); 
		assertEquals("m", it.next()); 
		assertEquals("n", it.next()); 
		assertFalse(it.hasNext());
	}
	
}
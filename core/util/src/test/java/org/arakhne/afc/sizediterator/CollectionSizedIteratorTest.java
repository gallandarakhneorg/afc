/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.sizediterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CollectionSizedIteratorTest {

	private String s1, s2, s3, s4, s5;
	private Collection<String> collection;
	private CollectionSizedIterator<String> iterator;
	
	/**
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.collection = new ArrayList<>();
		this.collection.add(this.s1 = "s1");  //$NON-NLS-1$
		this.collection.add(this.s2 = "s2");  //$NON-NLS-1$
		this.collection.add(this.s3 = "s3");  //$NON-NLS-1$
		this.collection.add(this.s4 = "s4");  //$NON-NLS-1$
		this.collection.add(this.s5 = "s5");  //$NON-NLS-1$
		this.iterator = new CollectionSizedIterator<>(this.collection);
	}
	
	/**
	 * @throws Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.iterator = null;
		this.collection = null;
		this.s1 = this.s2 = this.s3 = this.s4 = this.s5 = null;
	}

	/**
	 */
	@Test
	public void hasNext() {
		assertTrue(this.iterator.hasNext());
		this.iterator.next();
		assertTrue(this.iterator.hasNext());
		this.iterator.next();
		assertTrue(this.iterator.hasNext());
		this.iterator.next();
		assertTrue(this.iterator.hasNext());
		this.iterator.next();
		assertTrue(this.iterator.hasNext());
		this.iterator.next();
		assertFalse(this.iterator.hasNext());
	}

	/**
	 */
	@Test
	public void next() {
		assertSame(this.s1, this.iterator.next());
		assertSame(this.s2, this.iterator.next());
		assertSame(this.s3, this.iterator.next());
		assertSame(this.s4, this.iterator.next());
		assertSame(this.s5, this.iterator.next());
		try {
			this.iterator.next();
			fail("expecting NoSuchElementException");  //$NON-NLS-1$
		}
		catch(NoSuchElementException exception) {
			// Expected exception
		}
	}

	/**
	 */
	@Test
	public void remove() {
		assertSame(this.s1, this.iterator.next());
		assertSame(this.s2, this.iterator.next());
		this.iterator.remove();
		assertSame(this.s3, this.iterator.next());
		assertSame(this.s4, this.iterator.next());
		this.iterator.remove();
		assertSame(this.s5, this.iterator.next());
		
		assertEquals(3, this.collection.size());
		assertTrue(this.collection.contains(this.s1));
		assertFalse(this.collection.contains(this.s2));
		assertTrue(this.collection.contains(this.s3));
		assertFalse(this.collection.contains(this.s4));
		assertTrue(this.collection.contains(this.s5));
	}

	/**
	 */
	@Test
	public void index() {
		assertEquals(-1, this.iterator.index());
		assertSame(this.s1, this.iterator.next());
		assertEquals(0, this.iterator.index());
		assertSame(this.s2, this.iterator.next());
		assertEquals(1, this.iterator.index());
		assertSame(this.s3, this.iterator.next());
		assertEquals(2, this.iterator.index());
		assertSame(this.s4, this.iterator.next());
		assertEquals(3, this.iterator.index());
		assertSame(this.s5, this.iterator.next());
		assertEquals(4, this.iterator.index());
	}

	/**
	 */
	@Test
	public void rest() {
		assertEquals(5, this.iterator.rest());
		assertSame(this.s1, this.iterator.next());
		assertEquals(4, this.iterator.rest());
		assertSame(this.s2, this.iterator.next());
		assertEquals(3, this.iterator.rest());
		assertSame(this.s3, this.iterator.next());
		assertEquals(2, this.iterator.rest());
		assertSame(this.s4, this.iterator.next());
		assertEquals(1, this.iterator.rest());
		assertSame(this.s5, this.iterator.next());
		assertEquals(0, this.iterator.rest());
	}

	/**
	 */
	@Test
	public void totalSize() {
		assertEquals(5, this.iterator.totalSize());
		assertSame(this.s1, this.iterator.next());
		assertEquals(5, this.iterator.totalSize());
		assertSame(this.s2, this.iterator.next());
		assertEquals(5, this.iterator.totalSize());
		this.iterator.remove();
		assertEquals(4, this.iterator.totalSize());
		assertSame(this.s3, this.iterator.next());
		assertEquals(4, this.iterator.totalSize());
		assertSame(this.s4, this.iterator.next());
		assertEquals(4, this.iterator.totalSize());
		this.iterator.remove();
		assertEquals(3, this.iterator.totalSize());
		assertSame(this.s5, this.iterator.next());
		assertEquals(3, this.iterator.totalSize());
	}

}

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

package org.arakhne.afc.sizediterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.UUID;

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
public class SingleIteratorTest {

	private String s1;
	private SingleIterator<String> iterator;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.s1 = UUID.randomUUID().toString();
		this.iterator = new SingleIterator<>(this.s1);
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.iterator = null;
		this.s1 = null;
	}

	/**
	 */
	@Test
	public void hasNext() {
		assertTrue(this.iterator.hasNext());
		this.iterator.next();
		assertFalse(this.iterator.hasNext());
	}

	/**
	 */
	@Test
	public void next() {
		assertSame(this.s1, this.iterator.next());
		try {
			this.iterator.next();
			fail("expecting NoSuchElementException"); 
		}
		catch(NoSuchElementException exception) {
			// expected exception
		}
	}

	/**
	 */
	@Test
	public void remove() {
		try {
			this.iterator.remove();
			fail("expecting UnsupportedOperationException"); 
		}
		catch(UnsupportedOperationException exception) {
			// exepcted exception
		}
	}

	/**
	 */
	@Test
	public void index() {
		assertEquals(-1, this.iterator.index());
		this.iterator.next();
		assertEquals(0, this.iterator.index());
	}

	/**
	 */
	@Test
	public void rest() {
		assertEquals(1, this.iterator.rest());
		this.iterator.next();
		assertEquals(0, this.iterator.rest());
	}

	/**
	 */
	@Test
	public void totalSize() {
		assertEquals(1, this.iterator.totalSize());
		this.iterator.next();
		assertEquals(1, this.iterator.totalSize());
	}
	
}

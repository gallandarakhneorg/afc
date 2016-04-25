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
package org.arakhne.afc.sizediterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CollectionSizedIteratorTest {

	private String s1, s2, s3, s4, s5;
	private Collection<String> collection;
	private CollectionSizedIterator<String> iterator;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.collection = new ArrayList<>();
		this.collection.add(this.s1 = "s1"); //$NON-NLS-1$
		this.collection.add(this.s2 = "s2"); //$NON-NLS-1$
		this.collection.add(this.s3 = "s3"); //$NON-NLS-1$
		this.collection.add(this.s4 = "s4"); //$NON-NLS-1$
		this.collection.add(this.s5 = "s5"); //$NON-NLS-1$
		this.iterator = new CollectionSizedIterator<>(this.collection);
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.iterator = null;
		this.collection = null;
		this.s1 = this.s2 = this.s3 = this.s4 = this.s5 = null;
	}

	/**
	 */
	@Test
	public void testHasNext() {
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
	public void testNext() {
		assertSame(this.s1, this.iterator.next());
		assertSame(this.s2, this.iterator.next());
		assertSame(this.s3, this.iterator.next());
		assertSame(this.s4, this.iterator.next());
		assertSame(this.s5, this.iterator.next());
		try {
			this.iterator.next();
			fail("expecting NoSuchElementException"); //$NON-NLS-1$
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
	public void testIndex() {
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
	public void testRest() {
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
	public void testTotalSize() {
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

/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.util.NoSuchElementException;
import java.util.UUID;

import junit.framework.TestCase;

/** 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SingleIteratorTest extends TestCase {

	private String s1;
	private SingleIterator<String> iterator;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.s1 = UUID.randomUUID().toString();
		this.iterator = new SingleIterator<String>(this.s1);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.iterator = null;
		this.s1 = null;
		super.tearDown();
	}

	/**
	 */
	public void testHasNext() {
		assertTrue(this.iterator.hasNext());
		this.iterator.next();
		assertFalse(this.iterator.hasNext());
	}

	/**
	 */
	public void testNext() {
		assertSame(this.s1, this.iterator.next());
		try {
			this.iterator.next();
			fail("expecting NoSuchElementException"); //$NON-NLS-1$
		}
		catch(NoSuchElementException _) {
			// expected exception
		}
	}

	/**
	 */
	public void testRemove() {
		try {
			this.iterator.remove();
			fail("expecting UnsupportedOperationException"); //$NON-NLS-1$
		}
		catch(UnsupportedOperationException _) {
			// exepcted exception
		}
	}

	/**
	 */
	public void testIndex() {
		assertEquals(-1, this.iterator.index());
		this.iterator.next();
		assertEquals(0, this.iterator.index());
	}

	/**
	 */
	public void testRest() {
		assertEquals(1, this.iterator.rest());
		this.iterator.next();
		assertEquals(0, this.iterator.rest());
	}

	/**
	 */
	public void testTotalSize() {
		assertEquals(1, this.iterator.totalSize());
		this.iterator.next();
		assertEquals(1, this.iterator.totalSize());
	}
	
}

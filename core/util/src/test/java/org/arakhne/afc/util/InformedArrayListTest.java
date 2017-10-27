/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class InformedArrayListTest extends TestCase {

	private InformedArrayList<Object> list;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.list = new InformedArrayList<>();
	}
	
	@Override
	public void tearDown() throws Exception {
		this.list = null;
		super.tearDown();
	}
	
	public void testExtractClassFromCollection() {
		for(int i=0; i<10; ++i) {
			this.list.add(Integer.valueOf(i));
		}
		assertEquals(Integer.class, InformedArrayList.extractClassFrom(this.list));
		
		for(int i=0; i<10; ++i) {
			this.list.add(Long.valueOf(i));
		}
		assertEquals(Number.class, InformedArrayList.extractClassFrom(this.list));
		
		for(int i=0; i<10; ++i) {
			this.list.add(Integer.toHexString(i));
		}
		assertEquals(Object.class, InformedArrayList.extractClassFrom(this.list));
	}
	
	public void testUpdateComponentTypeE() {
		assertNull(this.list.getComponentType());
		this.list.updateComponentType(Integer.valueOf(1));
		assertEquals(Integer.class, this.list.getComponentType());
		this.list.updateComponentType(Float.valueOf(1f));
		assertEquals(Number.class, this.list.getComponentType());
		this.list.updateComponentType(toString());
		assertEquals(Object.class, this.list.getComponentType());
	}

	public void testUpdateComponentTypeCollection() {
		assertNull(this.list.getComponentType());
		this.list.updateComponentType(Arrays.asList(
				Integer.valueOf(1),
				Float.valueOf(1f)));
		assertEquals(Number.class, this.list.getComponentType());
		this.list.updateComponentType(Collections.singleton(toString()));
		assertEquals(Object.class, this.list.getComponentType());
	}

	public void testGetComponentType() {
		assertNull(this.list.getComponentType());
	}
	
	public void testAddE() {
		assertNull(this.list.getComponentType());
		
		assertTrue(this.list.add(Integer.valueOf(1)));
		assertEquals(Integer.class, this.list.getComponentType());
		assertEquals(1, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));

		assertTrue(this.list.add(Float.valueOf(2f)));
		assertEquals(Number.class, this.list.getComponentType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));

		assertTrue(this.list.add(toString()));
		assertEquals(Object.class, this.list.getComponentType());
		assertEquals(3, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));
		assertEquals(toString(), this.list.get(2));
	}

	public void testAddIntE() {
		assertNull(this.list.getComponentType());
		
		this.list.add(0, Integer.valueOf(1));
		assertEquals(Integer.class, this.list.getComponentType());
		assertEquals(1, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));

		this.list.add(0, Float.valueOf(2f));
		assertEquals(Number.class, this.list.getComponentType());
		assertEquals(2, this.list.size());
		assertEquals(Float.valueOf(2f), this.list.get(0));
		assertEquals(Integer.valueOf(1), this.list.get(1));

		this.list.add(1, toString());
		assertEquals(Object.class, this.list.getComponentType());
		assertEquals(3, this.list.size());
		assertEquals(Float.valueOf(2f), this.list.get(0));
		assertEquals(toString(), this.list.get(1));
		assertEquals(Integer.valueOf(1), this.list.get(2));
	}

	public void testAddAllCollection() {
		assertNull(this.list.getComponentType());

		assertTrue(this.list.addAll(Arrays.asList(
				Integer.valueOf(1), Float.valueOf(2f))));
		assertEquals(Number.class, this.list.getComponentType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));

		assertTrue(this.list.addAll(Collections.singleton(toString())));
		assertEquals(Object.class, this.list.getComponentType());
		assertEquals(3, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));
		assertEquals(toString(), this.list.get(2));
	}

	public void testAddAllIntCollection() {
		assertNull(this.list.getComponentType());

		assertTrue(this.list.addAll(0, Arrays.asList(
				Integer.valueOf(1), Float.valueOf(2f))));
		assertEquals(Number.class, this.list.getComponentType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));

		assertTrue(this.list.addAll(1, Collections.singleton(toString())));
		assertEquals(Object.class, this.list.getComponentType());
		assertEquals(3, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(toString(), this.list.get(1));
		assertEquals(Float.valueOf(2f), this.list.get(2));
	}

	public void testClear() {
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		assertEquals(Number.class, this.list.getComponentType());
		
		this.list.clear();
		
		assertNull(this.list.getComponentType());
		assertEquals(0, this.list.size());
	}

	public void testSetIntE() {
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		assertEquals(Number.class, this.list.getComponentType());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));
		
		this.list.set(1, Double.valueOf(3.));
		
		assertEquals(Number.class, this.list.getComponentType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Double.valueOf(3.), this.list.get(1));

		this.list.set(0, toString());
		
		assertEquals(Object.class, this.list.getComponentType());
		assertEquals(2, this.list.size());
		assertEquals(toString(), this.list.get(0));
		assertEquals(Double.valueOf(3.), this.list.get(1));
	}

	public void testRemoveInt_noUpdate() {
		assertFalse(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getComponentType());

		assertEquals(toString(), this.list.remove(2));
		assertEquals(Object.class, this.list.getComponentType());

		assertEquals(Integer.valueOf(1), this.list.remove(0));
		assertEquals(Object.class, this.list.getComponentType());

		assertEquals(Float.valueOf(2f), this.list.remove(0));
		assertNull(this.list.getComponentType());
	}
	
	public void testRemoveInt_update() {
		this.list.setTypeRecomputedAfterRemoval(true);
		assertTrue(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getComponentType());

		assertEquals(toString(), this.list.remove(2));
		assertEquals(Number.class, this.list.getComponentType());

		assertEquals(Integer.valueOf(1), this.list.remove(0));
		assertEquals(Float.class, this.list.getComponentType());

		assertEquals(Float.valueOf(2f), this.list.remove(0));
		assertNull(this.list.getComponentType());
	}
	
	public void testRemoveObject_noUpdate() {
		assertFalse(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getComponentType());

		assertTrue(this.list.remove(toString()));
		assertEquals(Object.class, this.list.getComponentType());

		assertTrue(this.list.remove(Integer.valueOf(1)));
		assertEquals(Object.class, this.list.getComponentType());

		assertTrue(this.list.remove(Float.valueOf(2f)));
		assertNull(this.list.getComponentType());
	}
	
	public void testRemoveObject_update() {
		this.list.setTypeRecomputedAfterRemoval(true);
		assertTrue(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getComponentType());

		assertTrue(this.list.remove(toString()));
		assertEquals(Number.class, this.list.getComponentType());

		assertTrue(this.list.remove(Integer.valueOf(1)));
		assertEquals(Float.class, this.list.getComponentType());

		assertTrue(this.list.remove(Float.valueOf(2f)));
		assertNull(this.list.getComponentType());
	}
	
	public void testRemoveRangeIntInt_noUpdate() {
		assertFalse(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getComponentType());

		this.list.removeRange(2,3);
		assertEquals(Object.class, this.list.getComponentType());

		this.list.removeRange(0,1);
		assertEquals(Object.class, this.list.getComponentType());

		this.list.removeRange(0, 1);
		assertNull(this.list.getComponentType());
	}

	public void testRemoveRangeIntInt_update() {
		this.list.setTypeRecomputedAfterRemoval(true);
		assertTrue(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getComponentType());

		this.list.removeRange(2,3);
		assertEquals(Number.class, this.list.getComponentType());

		this.list.removeRange(0,1);
		assertEquals(Float.class, this.list.getComponentType());

		this.list.removeRange(0, 1);
		assertNull(this.list.getComponentType());
	}

}

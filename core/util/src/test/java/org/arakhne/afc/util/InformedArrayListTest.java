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

package org.arakhne.afc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class InformedArrayListTest {

	private InformedArrayList<Object> list;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.list = new InformedArrayList<>();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		this.list = null;
	}

	@Test
	public void extractClassFromCollection() {
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
	
	@Test
	public void updateComponentTypeE() {
		assertNull(this.list.getElementType());
		this.list.updateComponentType(Integer.valueOf(1));
		assertEquals(Integer.class, this.list.getElementType());
		this.list.updateComponentType(Float.valueOf(1f));
		assertEquals(Number.class, this.list.getElementType());
		this.list.updateComponentType(toString());
		assertEquals(Object.class, this.list.getElementType());
	}

	@Test
	public void updateComponentTypeCollection() {
		assertNull(this.list.getElementType());
		this.list.updateComponentType(Arrays.asList(
				Integer.valueOf(1),
				Float.valueOf(1f)));
		assertEquals(Number.class, this.list.getElementType());
		this.list.updateComponentType(Collections.singleton(toString()));
		assertEquals(Object.class, this.list.getElementType());
	}

	@Test
	public void getElementType() {
		assertNull(this.list.getElementType());
	}
	
	@Test
	public void addE() {
		assertNull(this.list.getElementType());
		
		assertTrue(this.list.add(Integer.valueOf(1)));
		assertEquals(Integer.class, this.list.getElementType());
		assertEquals(1, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));

		assertTrue(this.list.add(Float.valueOf(2f)));
		assertEquals(Number.class, this.list.getElementType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));

		assertTrue(this.list.add(toString()));
		assertEquals(Object.class, this.list.getElementType());
		assertEquals(3, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));
		assertEquals(toString(), this.list.get(2));
	}

	@Test
	public void addIntE() {
		assertNull(this.list.getElementType());
		
		this.list.add(0, Integer.valueOf(1));
		assertEquals(Integer.class, this.list.getElementType());
		assertEquals(1, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));

		this.list.add(0, Float.valueOf(2f));
		assertEquals(Number.class, this.list.getElementType());
		assertEquals(2, this.list.size());
		assertEquals(Float.valueOf(2f), this.list.get(0));
		assertEquals(Integer.valueOf(1), this.list.get(1));

		this.list.add(1, toString());
		assertEquals(Object.class, this.list.getElementType());
		assertEquals(3, this.list.size());
		assertEquals(Float.valueOf(2f), this.list.get(0));
		assertEquals(toString(), this.list.get(1));
		assertEquals(Integer.valueOf(1), this.list.get(2));
	}

	@Test
	public void addAllCollection() {
		assertNull(this.list.getElementType());

		assertTrue(this.list.addAll(Arrays.asList(
				Integer.valueOf(1), Float.valueOf(2f))));
		assertEquals(Number.class, this.list.getElementType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));

		assertTrue(this.list.addAll(Collections.singleton(toString())));
		assertEquals(Object.class, this.list.getElementType());
		assertEquals(3, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));
		assertEquals(toString(), this.list.get(2));
	}

	@Test
	public void addAllIntCollection() {
		assertNull(this.list.getElementType());

		assertTrue(this.list.addAll(0, Arrays.asList(
				Integer.valueOf(1), Float.valueOf(2f))));
		assertEquals(Number.class, this.list.getElementType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));

		assertTrue(this.list.addAll(1, Collections.singleton(toString())));
		assertEquals(Object.class, this.list.getElementType());
		assertEquals(3, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(toString(), this.list.get(1));
		assertEquals(Float.valueOf(2f), this.list.get(2));
	}

	@Test
	public void clear() {
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		assertEquals(Number.class, this.list.getElementType());
		
		this.list.clear();
		
		assertNull(this.list.getElementType());
		assertEquals(0, this.list.size());
	}

	@Test
	public void setIntE() {
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		assertEquals(Number.class, this.list.getElementType());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Float.valueOf(2f), this.list.get(1));
		
		this.list.set(1, Double.valueOf(3.));
		
		assertEquals(Number.class, this.list.getElementType());
		assertEquals(2, this.list.size());
		assertEquals(Integer.valueOf(1), this.list.get(0));
		assertEquals(Double.valueOf(3.), this.list.get(1));

		this.list.set(0, toString());
		
		assertEquals(Object.class, this.list.getElementType());
		assertEquals(2, this.list.size());
		assertEquals(toString(), this.list.get(0));
		assertEquals(Double.valueOf(3.), this.list.get(1));
	}

	@Test
	public void removeInt_noUpdate() {
		assertFalse(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getElementType());

		assertEquals(toString(), this.list.remove(2));
		assertEquals(Object.class, this.list.getElementType());

		assertEquals(Integer.valueOf(1), this.list.remove(0));
		assertEquals(Object.class, this.list.getElementType());

		assertEquals(Float.valueOf(2f), this.list.remove(0));
		assertNull(this.list.getElementType());
	}
	
	@Test
	public void removeInt_update() {
		this.list.setTypeRecomputedAfterRemoval(true);
		assertTrue(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getElementType());

		assertEquals(toString(), this.list.remove(2));
		assertEquals(Number.class, this.list.getElementType());

		assertEquals(Integer.valueOf(1), this.list.remove(0));
		assertEquals(Float.class, this.list.getElementType());

		assertEquals(Float.valueOf(2f), this.list.remove(0));
		assertNull(this.list.getElementType());
	}
	
	@Test
	public void removeObject_noUpdate() {
		assertFalse(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getElementType());

		assertTrue(this.list.remove(toString()));
		assertEquals(Object.class, this.list.getElementType());

		assertTrue(this.list.remove(Integer.valueOf(1)));
		assertEquals(Object.class, this.list.getElementType());

		assertTrue(this.list.remove(Float.valueOf(2f)));
		assertNull(this.list.getElementType());
	}
	
	@Test
	public void removeObject_update() {
		this.list.setTypeRecomputedAfterRemoval(true);
		assertTrue(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getElementType());

		assertTrue(this.list.remove(toString()));
		assertEquals(Number.class, this.list.getElementType());

		assertTrue(this.list.remove(Integer.valueOf(1)));
		assertEquals(Float.class, this.list.getElementType());

		assertTrue(this.list.remove(Float.valueOf(2f)));
		assertNull(this.list.getElementType());
	}
	
	@Test
	public void removeRangeIntInt_noUpdate() {
		assertFalse(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getElementType());

		this.list.removeRange(2,3);
		assertEquals(Object.class, this.list.getElementType());

		this.list.removeRange(0,1);
		assertEquals(Object.class, this.list.getElementType());

		this.list.removeRange(0, 1);
		assertNull(this.list.getElementType());
	}

	@Test
	public void removeRangeIntInt_update() {
		this.list.setTypeRecomputedAfterRemoval(true);
		assertTrue(this.list.isTypeRecomputedAfterRemoval());
		this.list.add(Integer.valueOf(1));
		this.list.add(Float.valueOf(2f));
		this.list.add(toString());
		assertEquals(Object.class, this.list.getElementType());

		this.list.removeRange(2,3);
		assertEquals(Number.class, this.list.getElementType());

		this.list.removeRange(0,1);
		assertEquals(Float.class, this.list.getElementType());

		this.list.removeRange(0, 1);
		assertNull(this.list.getElementType());
	}

}

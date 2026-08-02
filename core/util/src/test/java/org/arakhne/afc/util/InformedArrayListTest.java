/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("InformedArray")
@SuppressWarnings("all")
public class InformedArrayListTest {

	private InformedArrayList<Object> list;
	
	@BeforeEach
	public void setUp() throws Exception {
		list = new InformedArrayList<>();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		list = null;
	}

	@DisplayName("extractClassFrom")
	@Nested
	public class ExtractClassFrom {

		@DisplayName("#1")
		@Test
		public void test_1() {
			for(int i=0; i<10; ++i) {
				list.add(Integer.valueOf(i));
			}
			assertEquals(Integer.class, InformedArrayList.extractClassFrom(list));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			for(int i=0; i<10; ++i) {
				list.add(Integer.valueOf(i));
			}
			for(int i=0; i<10; ++i) {
				list.add(Long.valueOf(i));
			}
			assertEquals(Number.class, InformedArrayList.extractClassFrom(list));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			for(int i=0; i<10; ++i) {
				list.add(Integer.valueOf(i));
			}
			for(int i=0; i<10; ++i) {
				list.add(Long.valueOf(i));
			}
			for(int i=0; i<10; ++i) {
				list.add(Integer.toHexString(i));
			}
			assertEquals(Object.class, InformedArrayList.extractClassFrom(list));
		}
	}
	
	@DisplayName("updateComponentType")
	@Nested
	public class UpdateComponentType {

		@DisplayName("(Object) #1")
		@Test
		public void obj_1() {
			assertNull(list.getElementType());
		}

		@DisplayName("(Object) #2")
		@Test
		public void obj_2() {
			list.updateComponentType(Integer.valueOf(1));
			assertEquals(Integer.class, list.getElementType());
		}

		@DisplayName("(Object) #3")
		@Test
		public void obj_3() {
			list.updateComponentType(Integer.valueOf(1));
			list.updateComponentType(Float.valueOf(1f));
			assertEquals(Number.class, list.getElementType());
		}

		@DisplayName("(Object) #4")
		@Test
		public void obj_4() {
			list.updateComponentType(Integer.valueOf(1));
			list.updateComponentType(Float.valueOf(1f));
			list.updateComponentType(toString());
			assertEquals(Object.class, list.getElementType());
		}

		@DisplayName("(Collection) #1")
		@Test
		public void updateComponentTypeCollection_1() {
			assertNull(list.getElementType());
		}

		@DisplayName("(Collection) #2")
		@Test
		public void updateComponentTypeCollection_2() {
			list.updateComponentType(Arrays.asList(
					Integer.valueOf(1),
					Float.valueOf(1f)));
			assertEquals(Number.class, list.getElementType());
		}

		@DisplayName("(Collection) #3")
		@Test
		public void updateComponentTypeCollection_3() {
			list.updateComponentType(Arrays.asList(Integer.valueOf(1), Float.valueOf(1f)));
			list.updateComponentType(Collections.singleton(toString()));
			assertEquals(Object.class, list.getElementType());
		}
	}

	@DisplayName("getElementType")
	@Nested
	public class GetElementType {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNull(list.getElementType());
		}
	}
	
	@DisplayName("add")
	@Nested
	public class Add {

		@DisplayName("(Object) #1")
		@Test
		public void obj_1() {
			assertTrue(list.add(Integer.valueOf(1)));
			assertEquals(Integer.class, list.getElementType());
			assertEquals(1, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
		}

		@DisplayName("(Object) #2")
		@Test
		public void obj_2() {
			list.add(Integer.valueOf(1));
			assertTrue(list.add(Float.valueOf(2f)));
			assertEquals(Number.class, list.getElementType());
			assertEquals(2, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(Float.valueOf(2f), list.get(1));
		}

		@DisplayName("(Object) #3")
		@Test
		public void obj_3() {
			list.add(Integer.valueOf(1));
			list.add(Float.valueOf(2f));
			assertTrue(list.add(toString()));
			assertEquals(Object.class, list.getElementType());
			assertEquals(3, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(Float.valueOf(2f), list.get(1));
			assertEquals(toString(), list.get(2));
		}

		@DisplayName("(int,Object) #1")
		@Test
		public void addIntE_1() {
			list.add(0, Integer.valueOf(1));
			assertEquals(Integer.class, list.getElementType());
			assertEquals(1, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
		}

		@DisplayName("(int,Object) #2")
		@Test
		public void addIntE_2() {
			list.add(0, Integer.valueOf(1));
			list.add(0, Float.valueOf(2f));
			assertEquals(Number.class, list.getElementType());
			assertEquals(2, list.size());
			assertEquals(Float.valueOf(2f), list.get(0));
			assertEquals(Integer.valueOf(1), list.get(1));
		}

		@DisplayName("(int,Object) #3")
		@Test
		public void addIntE_3() {
			list.add(0, Integer.valueOf(1));
			list.add(0, Float.valueOf(2f));
			list.add(1, toString());
			assertEquals(Object.class, list.getElementType());
			assertEquals(3, list.size());
			assertEquals(Float.valueOf(2f), list.get(0));
			assertEquals(toString(), list.get(1));
			assertEquals(Integer.valueOf(1), list.get(2));
		}
	}

	@DisplayName("addAll")
	@Nested
	public class AddAll {

		@DisplayName("(Collection) #1")
		@Test
		public void collection_1() {
			assertTrue(list.addAll(Arrays.asList(
					Integer.valueOf(1), Float.valueOf(2f))));
			assertEquals(Number.class, list.getElementType());
			assertEquals(2, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(Float.valueOf(2f), list.get(1));
		}

		@DisplayName("(Collection) #2")
		@Test
		public void collection_2() {
			list.addAll(Arrays.asList(Integer.valueOf(1), Float.valueOf(2f)));
			assertTrue(list.addAll(Collections.singleton(toString())));
			assertEquals(Object.class, list.getElementType());
			assertEquals(3, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(Float.valueOf(2f), list.get(1));
			assertEquals(toString(), list.get(2));
		}

		@DisplayName("(int,Collection) #1")
		@Test
		public void addAllIntCollection_1() {
			assertTrue(list.addAll(0, Arrays.asList(
					Integer.valueOf(1), Float.valueOf(2f))));
			assertEquals(Number.class, list.getElementType());
			assertEquals(2, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(Float.valueOf(2f), list.get(1));
		}

		@DisplayName("(int,Collection) #2")
		@Test
		public void addAllIntCollection_2() {
			list.addAll(0, Arrays.asList(Integer.valueOf(1), Float.valueOf(2f)));
			assertTrue(list.addAll(1, Collections.singleton(toString())));
			assertEquals(Object.class, list.getElementType());
			assertEquals(3, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(toString(), list.get(1));
			assertEquals(Float.valueOf(2f), list.get(2));
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@BeforeEach
		public void setUp() {
			list.add(Integer.valueOf(1));
			list.add(Float.valueOf(2f));
			assertEquals(Number.class, list.getElementType());
		}
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			list.clear();
			assertNull(list.getElementType());
			assertEquals(0, list.size());
		}
	}

	@DisplayName("set")
	@Nested
	public class Set {

		@BeforeEach
		public void setUp() {
			list.add(Integer.valueOf(1));
			list.add(Float.valueOf(2f));
			assertEquals(Number.class, list.getElementType());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(Float.valueOf(2f), list.get(1));
		}
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			list.set(1, Double.valueOf(3.));
			assertEquals(Number.class, list.getElementType());
			assertEquals(2, list.size());
			assertEquals(Integer.valueOf(1), list.get(0));
			assertEquals(Double.valueOf(3.), list.get(1));
		}	

		@DisplayName("#2")
		@Test
		public void test_2() {
			list.set(1, Double.valueOf(3.));
			list.set(0, toString());
			assertEquals(Object.class, list.getElementType());
			assertEquals(2, list.size());
			assertEquals(toString(), list.get(0));
			assertEquals(Double.valueOf(3.), list.get(1));
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		private void prepare1() {
			assertFalse(list.isTypeRecomputedAfterRemoval());
			list.add(Integer.valueOf(1));
			list.add(Float.valueOf(2f));
			list.add(toString());
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(int) #1")
		@Test
		public void removeInt_noUpdate_1() {
			prepare1();
			assertEquals(toString(), list.remove(2));
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(int) #2")
		@Test
		public void removeInt_noUpdate_2() {
			prepare1();
			list.remove(2);
			assertEquals(Integer.valueOf(1), list.remove(0));
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(int) #3")
		@Test
		public void removeInt_noUpdate_3() {
			prepare1();
			list.remove(2);
			list.remove(0);
			assertEquals(Float.valueOf(2f), list.remove(0));
			assertNull(list.getElementType());
		}

		private void prepare2() {
			list.setTypeRecomputedAfterRemoval(true);
			assertTrue(list.isTypeRecomputedAfterRemoval());
			list.add(Integer.valueOf(1));
			list.add(Float.valueOf(2f));
			list.add(toString());
			assertEquals(Object.class, list.getElementType());
		}

		@DisplayName("(int) #4")
		@Test
		public void removeInt_update_4() {
			prepare2();
			assertEquals(toString(), list.remove(2));
			assertEquals(Number.class, list.getElementType());
		}

		@DisplayName("(int) #5")
		@Test
		public void removeInt_update_5() {
			prepare2();
			list.remove(2);
			assertEquals(Integer.valueOf(1), list.remove(0));
			assertEquals(Float.class, list.getElementType());
		}

		@DisplayName("(int) #6")
		@Test
		public void removeInt_update_6() {
			prepare2();
			list.remove(2);
			list.remove(0);
			assertEquals(Float.valueOf(2f), list.remove(0));
			assertNull(list.getElementType());
		}
		
		@DisplayName("(Object) #1")
		@Test
		public void removeObject_noUpdate_1() {
			prepare1();
			assertTrue(list.remove(toString()));
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(Object) #2")
		@Test
		public void removeObject_noUpdate_2() {
			prepare1();
			list.remove(toString());
			assertTrue(list.remove(Integer.valueOf(1)));
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(Object) #3")
		@Test
		public void removeObject_noUpdate_3() {
			prepare1();
			list.remove(toString());
			list.remove(Integer.valueOf(1));
			assertTrue(list.remove(Float.valueOf(2f)));
			assertNull(list.getElementType());
		}
		
		@DisplayName("(Object) #4")
		@Test
		public void removeObject_update_4() {
			prepare2();
			assertTrue(list.remove(toString()));
			assertEquals(Number.class, list.getElementType());
		}
		
		@DisplayName("(Object) #5")
		@Test
		public void removeObject_update_5() {
			prepare2();
			list.remove(toString());
			assertTrue(list.remove(Integer.valueOf(1)));
			assertEquals(Float.class, list.getElementType());
		}
		
		@DisplayName("(Object) #6")
		@Test
		public void removeObject_update_6() {
			prepare2();
			list.remove(toString());
			list.remove(Integer.valueOf(1));
			assertTrue(list.remove(Float.valueOf(2f)));
			assertNull(list.getElementType());
		}
	}
	
	@DisplayName("removeRange")
	@Nested
	public class RemoveRange {

		private void prepare1() {
			assertFalse(list.isTypeRecomputedAfterRemoval());
			list.add(Integer.valueOf(1));
			list.add(Float.valueOf(2f));
			list.add(toString());
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(int,int) #1")
		@Test
		public void removeInt_noUpdate_1() {
			prepare1();
			list.removeRange(2, 3);
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(int,int) #2")
		@Test
		public void removeInt_noUpdate_2() {
			prepare1();
			list.removeRange(2, 3);
			list.removeRange(0, 1);
			assertEquals(Object.class, list.getElementType());
		}
		
		@DisplayName("(int,int) #3")
		@Test
		public void removeInt_noUpdate_3() {
			prepare1();
			list.removeRange(2, 3);
			list.removeRange(0, 1);
			list.removeRange(0, 1);
			assertNull(list.getElementType());
		}

		private void prepare2() {
			list.setTypeRecomputedAfterRemoval(true);
			assertTrue(list.isTypeRecomputedAfterRemoval());
			list.add(Integer.valueOf(1));
			list.add(Float.valueOf(2f));
			list.add(toString());
			assertEquals(Object.class, list.getElementType());
		}

		@DisplayName("(int,int) #4")
		@Test
		public void removeRangeIntInt_update_4() {
			prepare2();
			list.removeRange(2,3);
			assertEquals(Number.class, list.getElementType());
		}

		@DisplayName("(int,int) #5")
		@Test
		public void removeRangeIntInt_update_5() {
			prepare2();
			list.removeRange(2,3);
			list.removeRange(0,1);
			assertEquals(Float.class, list.getElementType());
		}

		@DisplayName("(int,int) #6")
		@Test
		public void removeRangeIntInt_update_6() {
			prepare2();
			list.removeRange(2,3);
			list.removeRange(0,1);
			list.removeRange(0, 1);
			assertNull(list.getElementType());
		}
	}

}

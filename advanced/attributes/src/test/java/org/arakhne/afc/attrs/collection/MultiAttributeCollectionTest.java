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

package org.arakhne.afc.attrs.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.testtools.AbstractTestCase;

@DisplayName("MultiAttributeCollection")
@SuppressWarnings("all")
public class MultiAttributeCollectionTest extends AbstractTestCase {

	private MultiAttributeCollection provider;
	private HeapAttributeCollection subprovider1;
	private HeapAttributeCollection subprovider2;
	private HeapAttributeCollection subprovider3;
	private AttributeContainerStub subcontainer4;
	
	@BeforeEach
	public void setUp() throws Exception {
		provider = new MultiAttributeCollection();
		subprovider1 = new HeapAttributeCollection();
		subprovider2 = new HeapAttributeCollection();
		subprovider3 = new HeapAttributeCollection();
		subcontainer4 = new AttributeContainerStub();
		
		subprovider1.setAttribute("A", true);  //$NON-NLS-1$
		subprovider1.setAttribute("B", 1);  //$NON-NLS-1$
		subprovider1.setAttribute("C", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		subprovider1.setAttribute("E", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		subprovider1.setAttribute("Z1", "Z1");   //$NON-NLS-1$ //$NON-NLS-2$

		subprovider2.setAttribute("A", true);  //$NON-NLS-1$
		subprovider2.setAttribute("B", 1.);  //$NON-NLS-1$
		subprovider2.setAttribute("D", "abc");   //$NON-NLS-1$ //$NON-NLS-2$
		subprovider2.setAttribute("E", 1);  //$NON-NLS-1$
		subprovider2.setAttribute("Z2", "Z2");   //$NON-NLS-1$ //$NON-NLS-2$

		subprovider3.setAttribute("A", false);  //$NON-NLS-1$
		subprovider3.setAttribute("B", 1);  //$NON-NLS-1$
		subprovider3.setAttribute("C", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		subprovider3.setAttribute("D", "abc");   //$NON-NLS-1$ //$NON-NLS-2$
		subprovider3.setAttribute("E", true);  //$NON-NLS-1$
		subprovider3.setAttribute("Z3", "Z3");   //$NON-NLS-1$ //$NON-NLS-2$
		
		subcontainer4.provider.setAttribute("A", true);  //$NON-NLS-1$
		subcontainer4.provider.setAttribute("Z4", "Z4");   //$NON-NLS-1$ //$NON-NLS-2$
		
		provider.addAttributeContainer(subprovider1);
		provider.addAttributeContainer(subprovider2);
		provider.addAttributeContainer(subprovider3);
		provider.addAttributeContainer(subcontainer4);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		provider = null;
		subprovider1 = subprovider2 = subprovider3 = null;
		subcontainer4 = null;
	}
	
	private static Attribute makeUninitialized(String name, AttributeType type) {
		return new AttributeImpl(name, type);
	}

	@DisplayName("getAttributeContainerCount")
	@Nested
	public class GetAttributeContainerCount {
		@DisplayName("#1")
		@Test
		public void getAttributeContainerCount() {
			assertEquals(4, provider.getAttributeContainerCount());
		}
	}

	@DisplayName("removeAllAttributes")
	@Nested
	public class RemoveAllAttributes {
		@DisplayName("#1")
		@Test
		public void removeAllAttributes_1() {
			assertTrue(provider.removeAllAttributes());
			assertEquals(0, subprovider1.getAttributeCount());
			assertEquals(0, subprovider2.getAttributeCount());
			assertEquals(0, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(2, provider.getAttributeCount());
	
			assertEpsilonEquals(Collections.emptyList(), subprovider1.getAllAttributes());
			assertEpsilonEquals(Collections.emptyList(), subprovider2.getAllAttributes());
			assertEpsilonEquals(Collections.emptyList(), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("#2")
		@Test
		public void removeAllAttributes_2() {
			provider.removeAllAttributes();
			assertFalse(provider.removeAllAttributes());
	
			assertEpsilonEquals(Collections.emptyList(), subprovider1.getAllAttributes());
			assertEpsilonEquals(Collections.emptyList(), subprovider2.getAllAttributes());
			assertEpsilonEquals(Collections.emptyList(), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}
	}

	@DisplayName("removeAttribute")
	@Nested
	public class RemoveAttribute {
		@DisplayName("remove A")
		@Test
		public void removeAttributeString_A() throws Exception {
			assertTrue(provider.removeAttribute("A"));  //$NON-NLS-1$
			assertEquals(4, subprovider1.getAttributeCount());
			assertEquals(4, subprovider2.getAttributeCount());
			assertEquals(5, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(9, provider.getAttributeCount());		
			
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("remove ZZZ")
		@Test
		public void removeAttributeString_ZZZ() throws Exception {
			provider.removeAttribute("A");  //$NON-NLS-1$
			assertFalse(provider.removeAttribute("ZZZ"));  //$NON-NLS-1$
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("remove Z3")
		@Test
		public void removeAttributeString_Z3() throws Exception {
			provider.removeAttribute("A");  //$NON-NLS-1$
			assertTrue(provider.removeAttribute("Z3"));  //$NON-NLS-1$
			assertEquals(4, subprovider1.getAttributeCount());
			assertEquals(4, subprovider2.getAttributeCount());
			assertEquals(4, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(8, provider.getAttributeCount());
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true)  //$NON-NLS-1$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}
	}

	@DisplayName("renameAttribute")
	@Nested
	public class RenameAttribute {

		@DisplayName("A -> ZZZ")
		@Test
		public void renameAttributeStringString_A_ZZZ() throws Exception {
			assertTrue(provider.renameAttribute("A", "ZZZ"));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("TOTOZZZ -> A")
		@Test
		public void renameAttributeStringString_TOTOZZZ_A() throws Exception {
			provider.renameAttribute("A", "ZZZ");   //$NON-NLS-1$ //$NON-NLS-2$
			
			assertFalse(provider.renameAttribute("TOTOZZZ", "A"));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("Z4 -> C")
		@Test
		public void renameAttributeStringString_Z4_C() throws Exception {
			provider.renameAttribute("A", "ZZZ");   //$NON-NLS-1$ //$NON-NLS-2$

			assertFalse(provider.renameAttribute("Z4", "C"));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("A -> ZZZ no override")
		@Test
		public void renameAttributeStringStringBoolean_false_A_ZZZ() throws Exception {
			assertTrue(provider.renameAttribute("A", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("TOTOZZZ -> A no override")
		@Test
		public void renameAttributeStringStringBoolean_false_TOTOZZZ_A() throws Exception {
			provider.renameAttribute("A", "ZZZ", false);

			assertFalse(provider.renameAttribute("TOTOZZZ", "A", false));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("Z4 -> C no override")
		@Test
		public void renameAttributeStringStringBoolean_false_Z4_C() throws Exception {
			provider.renameAttribute("A", "ZZZ", false);   //$NON-NLS-1$ //$NON-NLS-2$

			assertFalse(provider.renameAttribute("Z4", "C", false));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("A -> ZZZ override")
		@Test
		public void renameAttributeStringStringBoolean_true_A_ZZZ() throws Exception {
			assertTrue(provider.renameAttribute("A", "ZZZ", true));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("TOTOZZZ -> A override")
		@Test
		public void renameAttributeStringStringBoolean_true_TOTOZZZ_A() throws Exception {
			provider.renameAttribute("A", "ZZZ", true);

			assertFalse(provider.renameAttribute("TOTOZZZ", "A", true));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("Z4 -> C override")
		@Test
		public void renameAttributeStringStringBoolean_true_Z4_C() throws Exception {
			provider.renameAttribute("A", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$

			assertFalse(provider.renameAttribute("Z4", "C", true));   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(10, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", true),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("ZZZ", false),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("ZZZ", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}
	}

	@DisplayName("setAttribute")
	@Nested
	public class SetAttribute {

		@DisplayName("(String,AttributeValue)")
		@Nested
		public class StringAttributeValue {

			@DisplayName("ZZZ")
			@Test
			public void setAttributeStringAttributeValue_1() throws Exception {
				assertEquals(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						provider.setAttribute("ZZZ", new AttributeValueImpl("xyz")));   //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(6, subprovider1.getAttributeCount());
				assertEquals(6, subprovider2.getAttributeCount());
				assertEquals(7, subprovider3.getAttributeCount());
				assertEquals(2, subcontainer4.getAttributeCount());
				assertEquals(10, provider.getAttributeCount());		
		
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", 1),  //$NON-NLS-1$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider1.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", 1),  //$NON-NLS-1$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", 1),  //$NON-NLS-1$
						new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider2.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", false),  //$NON-NLS-1$
						new AttributeImpl("B", 1.),  //$NON-NLS-1$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", true),  //$NON-NLS-1$
						new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider3.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
				), subcontainer4.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						makeUninitialized("ZZZ", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
						makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
						makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
						makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
						makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
				), provider.getAllAttributes());
			}
			
			@DisplayName("B")
			@Test
			public void setAttributeStringAttributeValue_2() throws Exception {
				provider.setAttribute("ZZZ", new AttributeValueImpl("xyz"));

				assertEquals(
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						provider.setAttribute("B", new AttributeValueImpl("def")));   //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(6, subprovider1.getAttributeCount());
				assertEquals(6, subprovider2.getAttributeCount());
				assertEquals(7, subprovider3.getAttributeCount());
				assertEquals(2, subcontainer4.getAttributeCount());
				assertEquals(10, provider.getAttributeCount());		
		
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider1.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", 1),  //$NON-NLS-1$
						new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider2.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", false),  //$NON-NLS-1$
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", true),  //$NON-NLS-1$
						new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider3.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
				), subcontainer4.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						makeUninitialized("ZZZ", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
						makeUninitialized("B", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
						makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
						makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
				), provider.getAllAttributes());
			}
		}

		@DisplayName("(Attribute)")
		@Nested
		public class Attribute {

			@DisplayName("ZZZ")
			@Test
			public void setAttributeAttribute_1() throws Exception {
				assertEquals(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						provider.setAttribute(new AttributeImpl("ZZZ", "xyz")));   //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(6, subprovider1.getAttributeCount());
				assertEquals(6, subprovider2.getAttributeCount());
				assertEquals(7, subprovider3.getAttributeCount());
				assertEquals(2, subcontainer4.getAttributeCount());
				assertEquals(10, provider.getAttributeCount());		
		
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", 1),  //$NON-NLS-1$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider1.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", 1),  //$NON-NLS-1$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", 1),  //$NON-NLS-1$
						new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider2.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", false),  //$NON-NLS-1$
						new AttributeImpl("B", 1.),  //$NON-NLS-1$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", true),  //$NON-NLS-1$
						new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider3.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
				), subcontainer4.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						makeUninitialized("ZZZ", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
						makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
						makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
						makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
						makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
				), provider.getAllAttributes());
			}

			@DisplayName("B")
			@Test
			public void setAttributeAttribute_2() throws Exception {
				provider.setAttribute(new AttributeImpl("ZZZ", "xyz"));
				
				assertEquals(
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						provider.setAttribute(new AttributeImpl("B", "def")));   //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(6, subprovider1.getAttributeCount());
				assertEquals(6, subprovider2.getAttributeCount());
				assertEquals(7, subprovider3.getAttributeCount());
				assertEquals(2, subcontainer4.getAttributeCount());
				assertEquals(10, provider.getAttributeCount());		
		
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider1.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", 1),  //$NON-NLS-1$
						new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider2.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("ZZZ", "xyz"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("A", false),  //$NON-NLS-1$
						new AttributeImpl("B", "def"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
						new AttributeImpl("E", true),  //$NON-NLS-1$
						new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
				), subprovider3.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						new AttributeImpl("A", true),  //$NON-NLS-1$
						new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
				), subcontainer4.getAllAttributes());
				assertEpsilonEquals(Arrays.asList(
						makeUninitialized("ZZZ", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
						makeUninitialized("B", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
						makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
						makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
						makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
				), provider.getAllAttributes());
			}
		}
	}


	@DisplayName("setAttributeType")
	@Nested
	public class SetAttributeType {
		@DisplayName("#1")
		@Test
		public void setAttributeTypeStringAttributeType_1() throws Exception {
			assertEquals(
					makeUninitialized("A", AttributeType.STRING),  //$NON-NLS-1$
					provider.setAttributeType("A", AttributeType.STRING));  //$NON-NLS-1$
	
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(9, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", Boolean.TRUE.toString()),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", Boolean.TRUE.toString()),  //$NON-NLS-1$
					new AttributeImpl("B", 1),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", Boolean.FALSE.toString()),  //$NON-NLS-1$
					new AttributeImpl("B", 1.),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.INTEGER),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}

		@DisplayName("#2")
		@Test
		public void setAttributeTypeStringAttributeType_2() throws Exception {
			assertEquals(
					makeUninitialized("B", AttributeType.STRING),  //$NON-NLS-1$
					provider.setAttributeType("B", AttributeType.STRING));  //$NON-NLS-1$
	
			assertEquals(5, subprovider1.getAttributeCount());
			assertEquals(5, subprovider2.getAttributeCount());
			assertEquals(6, subprovider3.getAttributeCount());
			assertEquals(2, subcontainer4.getAttributeCount());
			assertEquals(9, provider.getAttributeCount());		
	
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", Boolean.TRUE.toString()),  //$NON-NLS-1$
					new AttributeImpl("B", Long.toString(1)),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("Z1", "Z1")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider1.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", Boolean.TRUE.toString()),  //$NON-NLS-1$
					new AttributeImpl("B", Double.toString(1.)),  //$NON-NLS-1$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", 1),  //$NON-NLS-1$
					new AttributeImpl("Z2", "Z2")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider2.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", Boolean.FALSE.toString()),  //$NON-NLS-1$
					new AttributeImpl("B", Long.toString(1)),  //$NON-NLS-1$
					new AttributeImpl("C", new URL("http://www.multiagent.fr")),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("D", "abc"),   //$NON-NLS-1$ //$NON-NLS-2$
					new AttributeImpl("E", true),  //$NON-NLS-1$
					new AttributeImpl("Z3", "Z3")   //$NON-NLS-1$ //$NON-NLS-2$
			), subprovider3.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("A", true),  //$NON-NLS-1$
					new AttributeImpl("Z4", "Z4")   //$NON-NLS-1$ //$NON-NLS-2$
			), subcontainer4.getAllAttributes());
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("B", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING),  //$NON-NLS-1$
					makeUninitialized("Z4", AttributeType.STRING)  //$NON-NLS-1$
			), provider.getAllAttributes());
		}
	}

	private static class AttributeContainerStub extends AbstractAttributeProvider {

		private static final long serialVersionUID = 4440233943216959812L;

		public final HeapAttributeCollection provider = new HeapAttributeCollection();
		
		public AttributeContainerStub() {
			//
		}
		
		@Override
		public void freeMemory() {
			provider.freeMemory();
		}

		@Override
		public Collection<String> getAllAttributeNames() {
			return provider.getAllAttributeNames();
		}

		@Override
		public Collection<Attribute> getAllAttributes() {
			return provider.getAllAttributes();
		}

		@Override
		public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
			return provider.getAllAttributesByType();
		}

		@Override
		public AttributeValue getAttribute(String name) {
			return provider.getAttribute(name);
		}

		@Override
		public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
			return provider.getAttribute(name, defaultValue);
		}

		@Override
		public int getAttributeCount() {
			return provider.getAttributeCount();
		}

		@Override
		public Attribute getAttributeObject(String name) {
			return provider.getAttributeObject(name);
		}

		@Override
		public boolean hasAttribute(String name) {
			return provider.hasAttribute(name);
		}

		@Override
		public void toMap(Map<String, Object> mapToFill) {
			provider.toMap(mapToFill);
		}
		
	}
	
}

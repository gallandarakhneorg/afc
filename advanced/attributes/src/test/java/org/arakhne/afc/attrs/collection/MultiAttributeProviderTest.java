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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MultiAttributeProvider")
@SuppressWarnings("all")
public class MultiAttributeProviderTest extends AbstractTestCase {

	private MultiAttributeProvider container;
	private HeapAttributeCollection subcontainer1;
	private HeapAttributeCollection subcontainer2;
	private HeapAttributeCollection subcontainer3;
	
	@BeforeEach
	public void setUp() throws Exception {
		container = new MultiAttributeProvider();
		subcontainer1 = new HeapAttributeCollection();
		subcontainer2 = new HeapAttributeCollection();
		subcontainer3 = new HeapAttributeCollection();
		
		subcontainer1.setAttribute("A", true);  //$NON-NLS-1$
		subcontainer1.setAttribute("B", 1);  //$NON-NLS-1$
		subcontainer1.setAttribute("C", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		subcontainer1.setAttribute("E", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		subcontainer1.setAttribute("Z1", "Z1");   //$NON-NLS-1$ //$NON-NLS-2$

		subcontainer2.setAttribute("A", true);  //$NON-NLS-1$
		subcontainer2.setAttribute("B", 1.);  //$NON-NLS-1$
		subcontainer2.setAttribute("D", "abc");   //$NON-NLS-1$ //$NON-NLS-2$
		subcontainer2.setAttribute("E", 1);  //$NON-NLS-1$
		subcontainer2.setAttribute("Z2", "Z2");   //$NON-NLS-1$ //$NON-NLS-2$

		subcontainer3.setAttribute("A", false);  //$NON-NLS-1$
		subcontainer3.setAttribute("B", 1);  //$NON-NLS-1$
		subcontainer3.setAttribute("C", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		subcontainer3.setAttribute("D", "abc");   //$NON-NLS-1$ //$NON-NLS-2$
		subcontainer3.setAttribute("E", true);  //$NON-NLS-1$
		subcontainer3.setAttribute("Z3", "Z3");   //$NON-NLS-1$ //$NON-NLS-2$
		
		container.addAttributeContainer(subcontainer1);
		container.addAttributeContainer(subcontainer2);
		container.addAttributeContainer(subcontainer3);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		container = null;
		subcontainer1 = subcontainer2 = subcontainer3 = null;
	}
	
	private static void assertUninitialized(AttributeType type, AttributeValue v) {
		assertNotNull(v);
		assertEquals(type, v.getType());
		assertFalse(v.isAssigned());
	}

	private static Attribute makeUninitialized(String name, AttributeType type) {
		return new AttributeImpl(name, type);
	}

	@DisplayName("getAttributeCount")
	@Nested
	public class GetAttributecount {
		@DisplayName("#1")
		@Test
		public void testGetAttributeCount() {
			assertEquals(8, container.getAttributeCount());
		}
	}
	
	@DisplayName("getAttributeContainerCount")
	@Nested
	public class GetAttributeContainerCount {
		@DisplayName("#1")
		@Test
		public void testGetAttributeContainerCount() {
			assertEquals(3, container.getAttributeContainerCount());
		}
	}

	@DisplayName("getAllAttributeNames")
	@Nested
	public class GetAllAttributeNames {
		@DisplayName("#1")
		@Test
		public void testGetAllAttributeNames() {
			Collection<String> names = container.getAllAttributeNames();
			assertNotNull(names);
			assertEquals(8, names.size());
			assertEpsilonEquals(Arrays.asList(
					"A",  //$NON-NLS-1$
					"B",  //$NON-NLS-1$
					"C",  //$NON-NLS-1$
					"D",  //$NON-NLS-1$
					"E",  //$NON-NLS-1$
					"Z1",  //$NON-NLS-1$
					"Z2",  //$NON-NLS-1$
					"Z3"  //$NON-NLS-1$
					), names);
		}
	}

	@DisplayName("hasAttribute")
	@Nested
	public class HasAttribute {

		@DisplayName("testHasAttributeString_1")
		@Test
		public void testHasAttributeString_1() {
			assertTrue(container.hasAttribute("A")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_2")
		@Test
		public void testHasAttributeString_2() {
			assertTrue(container.hasAttribute("B")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_3")
		@Test
		public void testHasAttributeString_3() {
			assertTrue(container.hasAttribute("C")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_4")
		@Test
		public void testHasAttributeString_4() {
			assertTrue(container.hasAttribute("D")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_5")
		@Test
		public void testHasAttributeString_5() {
			assertTrue(container.hasAttribute("E")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_6")
		@Test
		public void testHasAttributeString_6() {
			assertFalse(container.hasAttribute("F")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_7")
		@Test
		public void testHasAttributeString_7() {
			assertTrue(container.hasAttribute("Z1")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_8")
		@Test
		public void testHasAttributeString_8() {
			assertTrue(container.hasAttribute("Z2")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_9")
		@Test
		public void testHasAttributeString_9() {
			assertTrue(container.hasAttribute("Z3")); //$NON-NLS-1$
		}

		@DisplayName("testHasAttributeString_10")
		@Test
		public void testHasAttributeString_10() {
			assertFalse(container.hasAttribute("Z4")); //$NON-NLS-1$
		}
	}

	@DisplayName("getAttribute")
	@Nested
	public class GetAttribute {

		private AttributeValue defaultValue;

		@BeforeEach
		public void setUp() {
			defaultValue = new AttributeValueImpl(456);
		}

		@DisplayName("testGetAttributeString_1")
		@Test
		public void testGetAttributeString_1() {
			assertUninitialized(AttributeType.BOOLEAN, container.getAttribute("A")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_2")
		@Test
		public void testGetAttributeString_2() {
			assertEquals(new AttributeValueImpl(1), container.getAttribute("B")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_3")
		@Test
		public void testGetAttributeString_3() {
			assertUninitialized(AttributeType.URL, container.getAttribute("C")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_4")
		@Test
		public void testGetAttributeString_4() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("D")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_5")
		@Test
		public void testGetAttributeString_5() {
			assertUninitialized(AttributeType.OBJECT, container.getAttribute("E")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_6")
		@Test
		public void testGetAttributeString_6() {
			assertNull(container.getAttribute("F")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_7")
		@Test
		public void testGetAttributeString_7() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("Z1")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_8")
		@Test
		public void testGetAttributeString_8() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("Z2")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_9")
		@Test
		public void testGetAttributeString_9() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("Z3")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeString_10")
		@Test
		public void testGetAttributeString_10() {
			assertNull(container.getAttribute("Z4")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_1")
		@Test
		public void testGetAttributeStringAttributeValue_1() {
			assertUninitialized(AttributeType.BOOLEAN, container.getAttribute("A", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_2")
		@Test
		public void testGetAttributeStringAttributeValue_2() {
			assertEquals(new AttributeValueImpl(1), container.getAttribute("B", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_3")
		@Test
		public void testGetAttributeStringAttributeValue_3() {
			assertUninitialized(AttributeType.URL, container.getAttribute("C", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_4")
		@Test
		public void testGetAttributeStringAttributeValue_4() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("D", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_5")
		@Test
		public void testGetAttributeStringAttributeValue_5() {
			assertUninitialized(AttributeType.OBJECT, container.getAttribute("E", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_6")
		@Test
		public void testGetAttributeStringAttributeValue_6() {
			assertSame(defaultValue, container.getAttribute("F", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_7")
		@Test
		public void testGetAttributeStringAttributeValue_7() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("Z1", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_8")
		@Test
		public void testGetAttributeStringAttributeValue_8() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("Z2", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_9")
		@Test
		public void testGetAttributeStringAttributeValue_9() {
			assertUninitialized(AttributeType.STRING, container.getAttribute("Z3", defaultValue)); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeStringAttributeValue_10")
		@Test
		public void testGetAttributeStringAttributeValue_10() {
			assertSame(defaultValue, container.getAttribute("Z4", defaultValue)); //$NON-NLS-1$
		}
	}

	@DisplayName("getAttributeObject")
	@Nested
	public class GetAttributeObject {

		@DisplayName("testGetAttributeObjectString_1")
		@Test
		public void testGetAttributeObjectString_1() {
			assertUninitialized(AttributeType.BOOLEAN, container.getAttributeObject("A")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_2")
		@Test
		public void testGetAttributeObjectString_2() {
			assertEquals(new AttributeImpl("B", 1), container.getAttributeObject("B")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("testGetAttributeObjectString_3")
		@Test
		public void testGetAttributeObjectString_3() {
			assertUninitialized(AttributeType.URL, container.getAttributeObject("C")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_4")
		@Test
		public void testGetAttributeObjectString_4() {
			assertUninitialized(AttributeType.STRING, container.getAttributeObject("D")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_5")
		@Test
		public void testGetAttributeObjectString_5() {
			assertUninitialized(AttributeType.OBJECT, container.getAttributeObject("E")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_6")
		@Test
		public void testGetAttributeObjectString_6() {
			assertNull(container.getAttribute("F")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_7")
		@Test
		public void testGetAttributeObjectString_7() {
			assertUninitialized(AttributeType.STRING, container.getAttributeObject("Z1")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_8")
		@Test
		public void testGetAttributeObjectString_8() {
			assertUninitialized(AttributeType.STRING, container.getAttributeObject("Z2")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_9")
		@Test
		public void testGetAttributeObjectString_9() {
			assertUninitialized(AttributeType.STRING, container.getAttributeObject("Z3")); //$NON-NLS-1$
		}

		@DisplayName("testGetAttributeObjectString_10")
		@Test
		public void testGetAttributeObjectString_10() {
			assertNull(container.getAttributeObject("Z4")); //$NON-NLS-1$
		}
	}

	@DisplayName("getAllAttributes")
	@Nested
	public class GetAllAttributes {

		private Collection<Attribute> attrs;

		@BeforeEach
		public void setUp() throws Exception {
			attrs = container.getAllAttributes();
		}

		@DisplayName("testGetAllAttributes_1")
		@Test
		public void testGetAllAttributes_1() {
			assertNotNull(attrs);
		}

		@DisplayName("testGetAllAttributes_2")
		@Test
		public void testGetAllAttributes_2() {
			assertEquals(8, attrs.size());
		}

		@DisplayName("testGetAllAttributes_3")
		@Test
		public void testGetAllAttributes_3() {
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
					new AttributeImpl("B", 1), //$NON-NLS-1$
					makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
					makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
					makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING) //$NON-NLS-1$
			), attrs);
		}
	}
	
	@DisplayName("getAllAttributesByType")
	@Nested
	public class GetAllAttributesByType {

		private Map<AttributeType, Collection<Attribute>> attrsbytype;
		private Collection<Attribute> attrs;

		@BeforeEach
		public void setUp() {
			attrsbytype = container.getAllAttributesByType();
		}

		@DisplayName("testGetAllAttributesByType_1")
		@Test
		public void testGetAllAttributesByType_1() {
			assertNotNull(attrsbytype);
		}

		@DisplayName("testGetAllAttributesByType_2")
		@Test
		public void testGetAllAttributesByType_2() {
			assertEquals(5, attrsbytype.size());
		}

		@DisplayName("testGetAllAttributesByType_3")
		@Test
		public void testGetAllAttributesByType_3() {
			attrs = attrsbytype.get(AttributeType.BOOLEAN);
			assertNotNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_4")
		@Test
		public void testGetAllAttributesByType_4() {
			attrs = attrsbytype.get(AttributeType.BOOLEAN);
			assertEquals(1, attrs.size());
		}

		@DisplayName("testGetAllAttributesByType_5")
		@Test
		public void testGetAllAttributesByType_5() {
			attrs = attrsbytype.get(AttributeType.BOOLEAN);
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("A", AttributeType.BOOLEAN) //$NON-NLS-1$
			), attrs);
		}

		@DisplayName("testGetAllAttributesByType_6")
		@Test
		public void testGetAllAttributesByType_6() {
			attrs = attrsbytype.get(AttributeType.DATE);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_7")
		@Test
		public void testGetAllAttributesByType_7() {
			attrs = attrsbytype.get(AttributeType.INTEGER);
			assertNotNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_8")
		@Test
		public void testGetAllAttributesByType_8() {
			attrs = attrsbytype.get(AttributeType.INTEGER);
			assertEquals(1, attrs.size());
		}

		@DisplayName("testGetAllAttributesByType_9")
		@Test
		public void testGetAllAttributesByType_9() {
			attrs = attrsbytype.get(AttributeType.INTEGER);
			assertEpsilonEquals(Arrays.asList(
					new AttributeImpl("B", 1) //$NON-NLS-1$
			), attrs);
		}

		@DisplayName("testGetAllAttributesByType_10")
		@Test
		public void testGetAllAttributesByType_10() {
			attrs = attrsbytype.get(AttributeType.OBJECT);
			assertNotNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_11")
		@Test
		public void testGetAllAttributesByType_11() {
			attrs = attrsbytype.get(AttributeType.OBJECT);
			assertEquals(1, attrs.size());
		}

		@DisplayName("testGetAllAttributesByType_12")
		@Test
		public void testGetAllAttributesByType_12() {
			attrs = attrsbytype.get(AttributeType.OBJECT);
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("E", AttributeType.OBJECT) //$NON-NLS-1$
			), attrs);
		}

		@DisplayName("testGetAllAttributesByType_13")
		@Test
		public void testGetAllAttributesByType_13() {
			attrs = attrsbytype.get(AttributeType.POINT);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_14")
		@Test
		public void testGetAllAttributesByType_14() {
			attrs = attrsbytype.get(AttributeType.POINT3D);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_15")
		@Test
		public void testGetAllAttributesByType_15() {
			attrs = attrsbytype.get(AttributeType.POLYLINE);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_16")
		@Test
		public void testGetAllAttributesByType_16() {
			attrs = attrsbytype.get(AttributeType.POLYLINE3D);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_17")
		@Test
		public void testGetAllAttributesByType_17() {
			attrs = attrsbytype.get(AttributeType.REAL);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_18")
		@Test
		public void testGetAllAttributesByType_18() {
			attrs = attrsbytype.get(AttributeType.STRING);
			assertNotNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_19")
		@Test
		public void testGetAllAttributesByType_19() {
			attrs = attrsbytype.get(AttributeType.STRING);
			assertEquals(4, attrs.size());
		}

		@DisplayName("testGetAllAttributesByType_20")
		@Test
		public void testGetAllAttributesByType_20() {
			attrs = attrsbytype.get(AttributeType.STRING);
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
					makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
					makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
					makeUninitialized("Z3", AttributeType.STRING) //$NON-NLS-1$
			), attrs);
		}

		@DisplayName("testGetAllAttributesByType_21")
		@Test
		public void testGetAllAttributesByType_21() {
			attrs = attrsbytype.get(AttributeType.TIMESTAMP);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_22")
		@Test
		public void testGetAllAttributesByType_22() {
			attrs = attrsbytype.get(AttributeType.URI);
			assertNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_23")
		@Test
		public void testGetAllAttributesByType_23() {
			attrs = attrsbytype.get(AttributeType.URL);
			assertNotNull(attrs);
		}

		@DisplayName("testGetAllAttributesByType_24")
		@Test
		public void testGetAllAttributesByType_24() {
			attrs = attrsbytype.get(AttributeType.URL);
			assertEquals(1, attrs.size());
		}

		@DisplayName("testGetAllAttributesByType_25")
		@Test
		public void testGetAllAttributesByType_25() {
			attrs = attrsbytype.get(AttributeType.URL);
			assertEpsilonEquals(Arrays.asList(
					makeUninitialized("C", AttributeType.URL) //$NON-NLS-1$
			), attrs);
		}

		@DisplayName("testGetAllAttributesByType_26")
		@Test
		public void testGetAllAttributesByType_26() {
			attrs = attrsbytype.get(AttributeType.UUID);
			assertNull(attrs);
		}
	}
}

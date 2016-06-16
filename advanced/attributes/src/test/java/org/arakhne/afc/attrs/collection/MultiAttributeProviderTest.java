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

package org.arakhne.afc.attrs.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.testtools.AbstractTestCase;

@SuppressWarnings("all")
public class MultiAttributeProviderTest extends AbstractTestCase {

	private MultiAttributeProvider container;
	private HeapAttributeCollection subcontainer1;
	private HeapAttributeCollection subcontainer2;
	private HeapAttributeCollection subcontainer3;
	
	@Before
	public void setUp() throws Exception {
		this.container = new MultiAttributeProvider();
		this.subcontainer1 = new HeapAttributeCollection();
		this.subcontainer2 = new HeapAttributeCollection();
		this.subcontainer3 = new HeapAttributeCollection();
		
		this.subcontainer1.setAttribute("A", true);  //$NON-NLS-1$
		this.subcontainer1.setAttribute("B", 1);  //$NON-NLS-1$
		this.subcontainer1.setAttribute("C", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer1.setAttribute("E", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer1.setAttribute("Z1", "Z1");   //$NON-NLS-1$ //$NON-NLS-2$

		this.subcontainer2.setAttribute("A", true);  //$NON-NLS-1$
		this.subcontainer2.setAttribute("B", 1.);  //$NON-NLS-1$
		this.subcontainer2.setAttribute("D", "abc");   //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer2.setAttribute("E", 1);  //$NON-NLS-1$
		this.subcontainer2.setAttribute("Z2", "Z2");   //$NON-NLS-1$ //$NON-NLS-2$

		this.subcontainer3.setAttribute("A", false);  //$NON-NLS-1$
		this.subcontainer3.setAttribute("B", 1);  //$NON-NLS-1$
		this.subcontainer3.setAttribute("C", new URL("http://www.multiagent.fr"));   //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer3.setAttribute("D", "abc");   //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer3.setAttribute("E", true);  //$NON-NLS-1$
		this.subcontainer3.setAttribute("Z3", "Z3");   //$NON-NLS-1$ //$NON-NLS-2$
		
		this.container.addAttributeContainer(this.subcontainer1);
		this.container.addAttributeContainer(this.subcontainer2);
		this.container.addAttributeContainer(this.subcontainer3);
	}
	
	@After
	public void tearDown() throws Exception {
		this.container = null;
		this.subcontainer1 = this.subcontainer2 = this.subcontainer3 = null;
	}
	
	@Test
	public void testGetAttributeCount() {
		assertEquals(8, this.container.getAttributeCount());
	}
	
	@Test
	public void testGetAttributeContainerCount() {
		assertEquals(3, this.container.getAttributeContainerCount());
	}	

	@Test
	public void testGetAllAttributeNames() {
		Collection<String> names = this.container.getAllAttributeNames();
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
	
	private static void assertUninitialized(AttributeType type, AttributeValue v) {
		assertNotNull(v);
		assertEquals(type, v.getType());
		assertFalse(v.isAssigned());
	}

	private static Attribute makeUninitialized(String name, AttributeType type) {
		return new AttributeImpl(name, type);
	}

	@Test
	public void testHasAttributeString() {
		assertTrue(this.container.hasAttribute("A"));  //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("D"));  //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("E"));  //$NON-NLS-1$
		assertFalse(this.container.hasAttribute("F"));  //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("Z1"));  //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("Z2"));  //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("Z3"));  //$NON-NLS-1$
		assertFalse(this.container.hasAttribute("Z4"));  //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeString() {
		assertUninitialized(AttributeType.BOOLEAN,
				this.container.getAttribute("A"));  //$NON-NLS-1$
		assertEquals(new AttributeValueImpl(1),
				this.container.getAttribute("B"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.URL,
				this.container.getAttribute("C"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("D"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.OBJECT,
				this.container.getAttribute("E"));  //$NON-NLS-1$
		assertNull(this.container.getAttribute("F"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z1"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z2"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z3"));  //$NON-NLS-1$
		assertNull(this.container.getAttribute("Z4"));  //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeStringAttributeValue() {
		AttributeValue defaultValue = new AttributeValueImpl(456);
		assertUninitialized(AttributeType.BOOLEAN,
				this.container.getAttribute("A", defaultValue));  //$NON-NLS-1$
		assertEquals(new AttributeValueImpl(1),
				this.container.getAttribute("B", defaultValue));  //$NON-NLS-1$
		assertUninitialized(AttributeType.URL,
				this.container.getAttribute("C", defaultValue));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("D", defaultValue));  //$NON-NLS-1$
		assertUninitialized(AttributeType.OBJECT,
				this.container.getAttribute("E", defaultValue));  //$NON-NLS-1$
		assertSame(defaultValue, this.container.getAttribute("F", defaultValue));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z1", defaultValue));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z2", defaultValue));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z3", defaultValue));  //$NON-NLS-1$
		assertSame(defaultValue, this.container.getAttribute("Z4", defaultValue));  //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeObjectString() {
		assertUninitialized(AttributeType.BOOLEAN,
				this.container.getAttributeObject("A"));  //$NON-NLS-1$
		assertEquals(new AttributeImpl("B",1),  //$NON-NLS-1$
				this.container.getAttributeObject("B"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.URL,
				this.container.getAttributeObject("C"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("D"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.OBJECT,
				this.container.getAttributeObject("E"));  //$NON-NLS-1$
		assertNull(this.container.getAttribute("F"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("Z1"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("Z2"));  //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("Z3"));  //$NON-NLS-1$
		assertNull(this.container.getAttributeObject("Z4"));  //$NON-NLS-1$
	}

	@Test
	public void testGetAllAttributes() throws Exception {
		Collection<Attribute> attrs = this.container.getAllAttributes();
		assertNotNull(attrs);
		assertEquals(8, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN),  //$NON-NLS-1$
				new AttributeImpl("B", 1),  //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL),  //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT),  //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING)  //$NON-NLS-1$
		), attrs);
	}

	@Test
	public void testGetAllAttributesByType() {
		Map<AttributeType,Collection<Attribute>> attrsbytype = this.container.getAllAttributesByType();
		assertNotNull(attrsbytype);
		assertEquals(5, attrsbytype.size());
		
		Collection<Attribute> attrs;
		
		attrs = attrsbytype.get(AttributeType.BOOLEAN);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN)  //$NON-NLS-1$
		), attrs);
		
		attrs = attrsbytype.get(AttributeType.COLOR);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.DATE);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.IMAGE);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.INTEGER);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1)  //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.OBJECT);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("E", AttributeType.OBJECT)  //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.POINT);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.POINT3D);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.POLYLINE);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.POLYLINE3D);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.REAL);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.STRING);
		assertNotNull(attrs);
		assertEquals(4, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("D", AttributeType.STRING),  //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING),  //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING),  //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING)  //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.TIMESTAMP);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.URI);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.URL);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("C", AttributeType.URL)  //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.UUID);
		assertNull(attrs);
	}

}

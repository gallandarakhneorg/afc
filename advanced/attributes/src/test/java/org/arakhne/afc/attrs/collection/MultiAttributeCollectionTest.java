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
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
public class MultiAttributeCollectionTest extends AbstractTestCase {

	private MultiAttributeCollection provider;
	private HeapAttributeCollection subprovider1;
	private HeapAttributeCollection subprovider2;
	private HeapAttributeCollection subprovider3;
	private AttributeContainerStub subcontainer4;
	
	@Before
	public void setUp() throws Exception {
		this.provider = new MultiAttributeCollection();
		this.subprovider1 = new HeapAttributeCollection();
		this.subprovider2 = new HeapAttributeCollection();
		this.subprovider3 = new HeapAttributeCollection();
		this.subcontainer4 = new AttributeContainerStub();
		
		this.subprovider1.setAttribute("A", true); 
		this.subprovider1.setAttribute("B", 1); 
		this.subprovider1.setAttribute("C", new URL("http://www.multiagent.fr"));  
		this.subprovider1.setAttribute("E", new URL("http://www.multiagent.fr"));  
		this.subprovider1.setAttribute("Z1", "Z1");  

		this.subprovider2.setAttribute("A", true); 
		this.subprovider2.setAttribute("B", 1.); 
		this.subprovider2.setAttribute("D", "abc");  
		this.subprovider2.setAttribute("E", 1); 
		this.subprovider2.setAttribute("Z2", "Z2");  

		this.subprovider3.setAttribute("A", false); 
		this.subprovider3.setAttribute("B", 1); 
		this.subprovider3.setAttribute("C", new URL("http://www.multiagent.fr"));  
		this.subprovider3.setAttribute("D", "abc");  
		this.subprovider3.setAttribute("E", true); 
		this.subprovider3.setAttribute("Z3", "Z3");  
		
		this.subcontainer4.provider.setAttribute("A", true); 
		this.subcontainer4.provider.setAttribute("Z4", "Z4");  
		
		this.provider.addAttributeContainer(this.subprovider1);
		this.provider.addAttributeContainer(this.subprovider2);
		this.provider.addAttributeContainer(this.subprovider3);
		this.provider.addAttributeContainer(this.subcontainer4);
	}
	
	@After
	public void tearDown() throws Exception {
		this.provider = null;
		this.subprovider1 = this.subprovider2 = this.subprovider3 = null;
		this.subcontainer4 = null;
	}
	
	private static Attribute makeUninitialized(String name, AttributeType type) {
		return new AttributeImpl(name, type);
	}

	@Test
	public void getAttributeContainerCount() {
		assertEquals(4, this.provider.getAttributeContainerCount());
	}	

	@Test
	public void removeAllAttributes() {
		assertTrue(this.provider.removeAllAttributes());
		assertEquals(0, this.subprovider1.getAttributeCount());
		assertEquals(0, this.subprovider2.getAttributeCount());
		assertEquals(0, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(2, this.provider.getAttributeCount());

		assertEpsilonEquals(Collections.emptyList(), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		assertFalse(this.provider.removeAllAttributes());

		assertEpsilonEquals(Collections.emptyList(), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());
	}

	@Test
	public void removeAttributeString() throws Exception {
		//
		// Remove A
		//
		assertTrue(this.provider.removeAttribute("A")); 
		assertEquals(4, this.subprovider1.getAttributeCount());
		assertEquals(4, this.subprovider2.getAttributeCount());
		assertEquals(5, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(9, this.provider.getAttributeCount());		
		
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Failure on remove of ZZZ
		//
		assertFalse(this.provider.removeAttribute("ZZZ")); 

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Remove Z3
		//
		assertTrue(this.provider.removeAttribute("Z3")); 
		assertEquals(4, this.subprovider1.getAttributeCount());
		assertEquals(4, this.subprovider2.getAttributeCount());
		assertEquals(4, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(8, this.provider.getAttributeCount());

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true) 
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());
	}

	@Test
	public void renameAttributeStringString() throws Exception {
		//
		// Rename A to ZZZ
		//
		assertTrue(this.provider.renameAttribute("A", "ZZZ"));  
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Rename TOTOZZZ to A
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A"));  
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Rename Z4 to C
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A"));  
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());
	}

	@Test
	public void renameAttributeStringStringBoolean() throws Exception {
		//
		// Rename A to ZZZ
		//
		assertTrue(this.provider.renameAttribute("A", "ZZZ"));  
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Rename TOTOZZZ to A
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A"));  
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Rename Z4 to C
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A"));  
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());
	}

	@Test
	public void setAttributeStringAttributeValue() throws Exception {
		//
		// Set ZZZ
		//
		assertEquals(
				new AttributeImpl("ZZZ", "xyz"),  
				this.provider.setAttribute("ZZZ", new AttributeValueImpl("xyz")));  
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Set B
		//
		assertEquals(
				new AttributeImpl("B", "def"),  
				this.provider.setAttribute("B", new AttributeValueImpl("def")));  
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", "def"),  
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", "def"),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", false), 
				new AttributeImpl("B", "def"),  
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.STRING), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());
	}

	@Test
	public void setAttributeAttribute() throws Exception {
		//
		// Set ZZZ
		//
		assertEquals(
				new AttributeImpl("ZZZ", "xyz"),  
				this.provider.setAttribute(new AttributeImpl("ZZZ", "xyz")));  
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", false), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Set B
		//
		assertEquals(
				new AttributeImpl("B", "def"),  
				this.provider.setAttribute(new AttributeImpl("B", "def")));  
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", "def"),  
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", true), 
				new AttributeImpl("B", "def"),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"),  
				new AttributeImpl("A", false), 
				new AttributeImpl("B", "def"),  
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), 
				makeUninitialized("A", AttributeType.BOOLEAN), 
				makeUninitialized("B", AttributeType.STRING), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());
	}

	@Test
	public void setAttributeTypeStringAttributeType() throws Exception {
		//
		// Set type of A to STRING
		//
		assertEquals(
				makeUninitialized("A", AttributeType.STRING), 
				this.provider.setAttributeType("A", AttributeType.STRING)); 

		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(9, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), 
				new AttributeImpl("B", 1), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.FALSE.toString()), 
				new AttributeImpl("B", 1.), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.OBJECT), 
				makeUninitialized("B", AttributeType.INTEGER), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());

		//
		// Set type of B to STRING
		//
		assertEquals(
				makeUninitialized("B", AttributeType.STRING), 
				this.provider.setAttributeType("B", AttributeType.STRING)); 

		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(9, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), 
				new AttributeImpl("B", Long.toString(1)), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("E", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("Z1", "Z1")  
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), 
				new AttributeImpl("B", Double.toString(1.)), 
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", 1), 
				new AttributeImpl("Z2", "Z2")  
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.FALSE.toString()), 
				new AttributeImpl("B", Long.toString(1)), 
				new AttributeImpl("C", new URL("http://www.multiagent.fr")),  
				new AttributeImpl("D", "abc"),  
				new AttributeImpl("E", true), 
				new AttributeImpl("Z3", "Z3")  
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), 
				new AttributeImpl("Z4", "Z4")  
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.OBJECT), 
				makeUninitialized("B", AttributeType.STRING), 
				makeUninitialized("C", AttributeType.URL), 
				makeUninitialized("D", AttributeType.STRING), 
				makeUninitialized("E", AttributeType.OBJECT), 
				makeUninitialized("Z1", AttributeType.STRING), 
				makeUninitialized("Z2", AttributeType.STRING), 
				makeUninitialized("Z3", AttributeType.STRING), 
				makeUninitialized("Z4", AttributeType.STRING) 
		), this.provider.getAllAttributes());
	}

	private static class AttributeContainerStub extends AbstractAttributeProvider {

		private static final long serialVersionUID = 4440233943216959812L;

		public final HeapAttributeCollection provider = new HeapAttributeCollection();
		
		public AttributeContainerStub() {
			//
		}
		
		@Override
		public void freeMemory() {
			this.provider.freeMemory();
		}

		@Override
		public Collection<String> getAllAttributeNames() {
			return this.provider.getAllAttributeNames();
		}

		@Override
		public Collection<Attribute> getAllAttributes() {
			return this.provider.getAllAttributes();
		}

		@Override
		public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
			return this.provider.getAllAttributesByType();
		}

		@Override
		public AttributeValue getAttribute(String name) {
			return this.provider.getAttribute(name);
		}

		@Override
		public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
			return this.provider.getAttribute(name, defaultValue);
		}

		@Override
		public int getAttributeCount() {
			return this.provider.getAttributeCount();
		}

		@Override
		public Attribute getAttributeObject(String name) {
			return this.provider.getAttributeObject(name);
		}

		@Override
		public boolean hasAttribute(String name) {
			return this.provider.hasAttribute(name);
		}

		@Override
		public void toMap(Map<String, Object> mapToFill) {
			this.provider.toMap(mapToFill);
		}
		
	}
	
}

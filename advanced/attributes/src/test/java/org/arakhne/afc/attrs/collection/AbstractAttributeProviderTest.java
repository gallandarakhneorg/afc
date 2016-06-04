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
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.attr.InvalidAttributeTypeException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.testtools.AbstractTestCase;

@SuppressWarnings("all")
public abstract class AbstractAttributeProviderTest<T extends AttributeProvider> extends AbstractTestCase {

	protected final String id;

	protected T testData;
	
	protected Attribute[] baseData;

	
	public AbstractAttributeProviderTest(String id) {
		super();
		this.id = id;
	}
	
	protected static void assertInvalidValue(AttributeProvider provider, String methodName, Object... parameters) throws Exception {
		assertInvalidValue(null, provider, methodName, parameters);
	}

	protected static void assertInvalidValue(String message, AttributeProvider provider, String methodName, Object... parameters) throws Exception {
		StringBuilder msg = new StringBuilder();
		if (message!=null && !message.isEmpty()) msg.append(": "); 
		try {
			Class<?>[] classTab = new Class<?>[parameters.length];
			for(int i=0; i<parameters.length; ++i) {
				classTab[i] = parameters[i].getClass();
			}
			Class<? extends AttributeProvider> clazz = provider.getClass();
			Method method = clazz.getMethod(methodName,classTab);
			method.invoke(provider,parameters);
			msg.append("the exception InvalidAttributeTypeException was not thrown: standard return from the function "); 
			msg.append(methodName);
			fail(msg.toString());
		}
		catch(InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof InvalidAttributeTypeException) {
				// normal case
			}
			else {
				msg.append("the exception InvalidAttributeTypeException was not thrown, exception: "); 
				msg.append(ex);
				msg.append(", file: "); 
				msg.append(ex.getStackTrace()[0].getFileName());
				msg.append(", line: "); 
				msg.append(ex.getStackTrace()[0].getLineNumber());
				fail(msg.toString());
			}
		}
	}	
	
	/**
	 * Fill the attribute provider with test case data.
	 * 
	 * @param provider
	 * @throws AttributeException
	 */
	protected void createTestCaseData(AttributeCollection provider) throws AttributeException {
		for (Attribute a : this.baseData) {
			provider.setAttribute(a);
		}
	}
	
	@Before
	public void setUp() throws Exception {
		this.baseData = new Attribute[] {
				new AttributeImpl("A",1), 
				new AttributeImpl("B",2.), 
				new AttributeImpl("C",true), 
				new AttributeImpl("D","Hello"),  
				new AttributeImpl("E",new Point2d(1,2)), 
				new AttributeImpl("F","false"),  
		};
		this.testData = setUpTestCase();
		if (this.testData instanceof AttributeCollection) {
			createTestCaseData((AttributeCollection)this.testData);
		}
	}

	/** Initialize the test case.
	 * @return the set up test case.
	 * @throws Exception
	 */
	protected abstract T setUpTestCase() throws Exception;

	@After
	public void tearDown() throws Exception {
		this.testData = null;
		this.baseData = null;
	}

	@Test
	public void iterator() {
		ArrayList<Attribute> ref = new ArrayList<Attribute>();
		ref.addAll(Arrays.asList(this.baseData));

		Iterator<Attribute> it = this.testData.attributes().iterator();
		while(!ref.isEmpty()) {
			assertTrue(this.id, it.hasNext());
			Attribute attr = it.next();
			assertNotNull(this.id, attr);
			assertTrue(this.id, ref.remove(attr));
		}
		
		assertFalse(this.id, it.hasNext());
	}

	@Test
	public void hasAttribute() {
		assertTrue(this.id, this.testData.hasAttribute("A")); 
		assertFalse(this.id, this.testData.hasAttribute("X")); 
		assertTrue(this.id, this.testData.hasAttribute("B")); 
		assertFalse(this.id, this.testData.hasAttribute("Y")); 
		assertTrue(this.id, this.testData.hasAttribute("C")); 
		assertTrue(this.id, this.testData.hasAttribute("D")); 
		assertFalse(this.id, this.testData.hasAttribute("Z")); 
		assertTrue(this.id, this.testData.hasAttribute("E")); 
		assertTrue(this.id, this.testData.hasAttribute("F")); 
	}

	@Test
	public void getAllAttributes() {
		assertEpsilonEquals(this.id, this.baseData, this.testData.getAllAttributes().toArray());
	}

	@Test
	public void getAllAttributesByType() {
		HashMap<AttributeType,Collection<Attribute>> map = new HashMap<AttributeType,Collection<Attribute>>();
		for(Attribute data : this.baseData) {
			AttributeType type = data.getType();
			Collection<Attribute> col = map.get(type);
			if (col==null) {
				col = new ArrayList<Attribute>();
				map.put(type,col);
			}
			col.add(data);
		}
		
		assertEquals(this.id, map, this.testData.getAllAttributesByType());
	}


	@Test
	public void getAllAttributeNames() {
		assertEpsilonEquals(this.id, new String[] {
				"A", 
				"B", 
				"C", 
				"D", 
				"E", 
				"F", 
		}, this.testData.getAllAttributeNames().toArray());
	}

	@Test
	public void getAttributeString() {
		assertEquals(this.id, this.baseData[0],this.testData.getAttribute("A")); 
		assertNull(this.id, this.testData.getAttribute("X")); 
		assertEquals(this.id, this.baseData[1],this.testData.getAttribute("B")); 
		assertNull(this.id, this.testData.getAttribute("Y")); 
		assertEquals(this.id, this.baseData[2],this.testData.getAttribute("C")); 
		assertNull(this.id, this.testData.getAttribute("Z")); 
		assertEquals(this.id, this.baseData[3],this.testData.getAttribute("D")); 
		assertNull(this.id, this.testData.getAttribute("W")); 
		assertEquals(this.id, this.baseData[4],this.testData.getAttribute("E")); 
		assertEquals(this.id, this.baseData[5],this.testData.getAttribute("F")); 
	}

	@Test
	public void getAttributeStringAttributeValue() {
		AttributeValue defaultValue = new AttributeValueImpl();
		assertEquals(this.id, this.baseData[0],this.testData.getAttribute("A",defaultValue)); 
		assertSame(this.id, defaultValue, this.testData.getAttribute("X", defaultValue)); 
		assertEquals(this.id, this.baseData[1],this.testData.getAttribute("B", defaultValue)); 
		assertSame(this.id, defaultValue, this.testData.getAttribute("Y", defaultValue)); 
		assertEquals(this.id, this.baseData[2],this.testData.getAttribute("C", defaultValue)); 
		assertSame(this.id, defaultValue, this.testData.getAttribute("Z", defaultValue)); 
		assertEquals(this.id, this.baseData[3],this.testData.getAttribute("D", defaultValue)); 
		assertSame(this.id, defaultValue, this.testData.getAttribute("W", defaultValue)); 
		assertEquals(this.id, this.baseData[4],this.testData.getAttribute("E", defaultValue)); 
		assertEquals(this.id, this.baseData[5],this.testData.getAttribute("F", defaultValue)); 
	}

	@Test
	public void getAttributeObject() {
		assertEquals(this.id, this.baseData[0],this.testData.getAttributeObject("A")); 
		assertNull(this.id, this.testData.getAttribute("X")); 
		assertEquals(this.id, this.baseData[1],this.testData.getAttributeObject("B")); 
		assertNull(this.id, this.testData.getAttribute("Y")); 
		assertEquals(this.id, this.baseData[2],this.testData.getAttributeObject("C")); 
		assertNull(this.id, this.testData.getAttribute("Z")); 
		assertEquals(this.id, this.baseData[3],this.testData.getAttributeObject("D")); 
		assertNull(this.id, this.testData.getAttribute("W")); 
		assertEquals(this.id, this.baseData[4],this.testData.getAttributeObject("E")); 
		assertEquals(this.id, this.baseData[5],this.testData.getAttributeObject("F")); 
	}

	@Test
	public void getAttributeAsBoolString() throws Exception {
		assertTrue(this.id, this.testData.getAttributeAsBool("A"));
		assertTrue(this.id, this.testData.getAttributeAsBool("B"));
		assertTrue(this.id, this.testData.getAttributeAsBool("C"));
		assertInvalidValue(this.id, this.testData,"getAttributeAsBool","D"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsBool","E"); 
		assertFalse(this.id, this.testData.getAttributeAsBool("F"));
	}

	@Test
	public void getAttributeStringBoolean() throws Exception {
		assertTrue(this.id, this.testData.getAttribute("A",true));
		assertTrue(this.id, this.testData.getAttribute("B",false));
		assertTrue(this.id, this.testData.getAttribute("C",false));
		assertTrue(this.id, this.testData.getAttribute("D",true));
		assertFalse(this.id, this.testData.getAttribute("E",false));
		assertFalse(this.id, this.testData.getAttribute("F",true));
	}

	@Test
	public void getAttributeAsIntString() throws Exception {
		assertEquals(this.id, 1,this.testData.getAttributeAsInt("A"));
		assertEquals(this.id, 2,this.testData.getAttributeAsInt("B"));
		assertEquals(this.id, 1, this.testData.getAttributeAsInt("C"));
		assertInvalidValue(this.id, this.testData,"getAttributeAsInt","D"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsInt","E"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsInt","F"); 
	}
	
	@Test
	public void getAttributeStringInt() throws Exception {
		assertEquals(this.id, 1,this.testData.getAttribute("A",5));
		assertEquals(this.id, 2,this.testData.getAttribute("B",34));
		assertEquals(this.id, 1,this.testData.getAttribute("C",18));
		assertEquals(this.id, 24,this.testData.getAttribute("D",24));
		assertEquals(this.id, -34,this.testData.getAttribute("E",-34));
		assertEquals(this.id, 18,this.testData.getAttribute("F",18));
	}

	@Test
	public void getAttributeAsLongString() throws Exception {
		assertEquals(this.id, 1,this.testData.getAttributeAsLong("A"));
		assertEquals(this.id, 2,this.testData.getAttributeAsLong("B"));
		assertEquals(this.id, 1, this.testData.getAttributeAsLong("C"));
		assertInvalidValue(this.id, this.testData,"getAttributeAsLong","D"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsLong","E"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsLong","F"); 
	}

	@Test
	public void getAttributegStringLong() throws Exception {
		assertEquals(this.id, 1,this.testData.getAttribute("A",5));
		assertEquals(this.id, 2,this.testData.getAttribute("B",34));
		assertEquals(this.id, 1,this.testData.getAttribute("C",18));
		assertEquals(this.id, 24,this.testData.getAttribute("D",24));
		assertEquals(this.id, -34,this.testData.getAttribute("E",-34));
		assertEquals(this.id, 18,this.testData.getAttribute("F",18));
	}

	@Test
	public void getAttributeAsFloatString() throws Exception {
		assertEpsilonEquals(this.id, 1f,this.testData.getAttributeAsFloat("A"));
		assertEpsilonEquals(this.id, 2f,this.testData.getAttributeAsFloat("B"));
		assertEpsilonEquals(this.id, 1f, this.testData.getAttributeAsFloat("C"));
		assertInvalidValue(this.id, this.testData,"getAttributeAsFloat","D"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsFloat","E"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsFloat","F"); 
	}

	@Test
	public void getAttributeStringFloat() throws Exception {
		assertEpsilonEquals(this.id, 1f,this.testData.getAttribute("A",5f));
		assertEpsilonEquals(this.id, 2f,this.testData.getAttribute("B",34f));
		assertEpsilonEquals(this.id, 1f,this.testData.getAttribute("C",18f));
		assertEpsilonEquals(this.id, 24f,this.testData.getAttribute("D",24f));
		assertEpsilonEquals(this.id, -34f,this.testData.getAttribute("E",-34f));
		assertEpsilonEquals(this.id, 18f,this.testData.getAttribute("F",18f));
	}

	@Test
	public void getAttributeAsDoubleString() throws Exception {
		assertEpsilonEquals(this.id, 1.,this.testData.getAttributeAsDouble("A"));
		assertEpsilonEquals(this.id, 2.,this.testData.getAttributeAsDouble("B"));
		assertEpsilonEquals(this.id, 1., this.testData.getAttributeAsDouble("C"));
		assertInvalidValue(this.id, this.testData,"getAttributeAsDouble","D"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsDouble","E"); 
		assertInvalidValue(this.id, this.testData,"getAttributeAsDouble","F"); 
	}

	@Test
	public void getAttributeStringDouble() throws Exception {
		assertEpsilonEquals(this.id, 1.,this.testData.getAttribute("A",5.));
		assertEpsilonEquals(this.id, 2.,this.testData.getAttribute("B",34.));
		assertEpsilonEquals(this.id, 1.,this.testData.getAttribute("C",18.));
		assertEpsilonEquals(this.id, 24.,this.testData.getAttribute("D",24.));
		assertEpsilonEquals(this.id, -34.,this.testData.getAttribute("E",-34.));
		assertEpsilonEquals(this.id, 18.,this.testData.getAttribute("F",18.));
	}

	@Test
	public void getAttributeAsStringString() throws Exception {
		assertEquals(this.id, Long.toString(1),this.testData.getAttributeAsString("A"));
		assertEquals(this.id, Double.toString(2.),this.testData.getAttributeAsString("B"));
		assertEquals(this.id, Boolean.toString(true),this.testData.getAttributeAsString("C"));
		assertEquals(this.id, "Hello",this.testData.getAttributeAsString("D")); 
		assertEquals(this.id, 1.+";"+2.,this.testData.getAttributeAsString("E")); 		
		assertEquals(this.id, Boolean.toString(false),this.testData.getAttributeAsString("F"));
	}

	@Test
	public void getAttributeStringString() throws Exception {
		assertEquals(this.id, Long.toString(1),this.testData.getAttribute("A","default")); 
		assertEquals(this.id, Double.toString(2.),this.testData.getAttribute("B","default")); 
		assertEquals(this.id, Boolean.toString(true),this.testData.getAttribute("C","default")); 
		assertEquals(this.id, "Hello",this.testData.getAttribute("D","default"));  
		assertEquals(this.id, 1.+";"+2.,this.testData.getAttribute("E","default"));  
		assertEquals(this.id, Boolean.toString(false),this.testData.getAttribute("F","default")); 
	}

	@Test
	public void freeMemory() {
		this.testData.freeMemory();
		iterator();
	}

}

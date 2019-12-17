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

package org.arakhne.afc.attrs.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		if (message!=null && !message.isEmpty()) msg.append(": ");  //$NON-NLS-1$
		try {
			Class<?>[] classTab = new Class<?>[parameters.length];
			for(int i=0; i<parameters.length; ++i) {
				classTab[i] = parameters[i].getClass();
			}
			Class<? extends AttributeProvider> clazz = provider.getClass();
			Method method = clazz.getMethod(methodName,classTab);
			method.invoke(provider,parameters);
			msg.append("the exception InvalidAttributeTypeException was not thrown: standard return from the function ");  //$NON-NLS-1$
			msg.append(methodName);
			fail(msg.toString());
		}
		catch(InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof InvalidAttributeTypeException) {
				// normal case
			}
			else {
				msg.append("the exception InvalidAttributeTypeException was not thrown, exception: ");  //$NON-NLS-1$
				msg.append(ex);
				msg.append(", file: ");  //$NON-NLS-1$
				msg.append(ex.getStackTrace()[0].getFileName());
				msg.append(", line: ");  //$NON-NLS-1$
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
	
	@BeforeEach
	public void setUp() throws Exception {
		this.baseData = new Attribute[] {
				new AttributeImpl("A",1),  //$NON-NLS-1$
				new AttributeImpl("B",2.),  //$NON-NLS-1$
				new AttributeImpl("C",true),  //$NON-NLS-1$
				new AttributeImpl("D","Hello"),   //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E",new Point2d(1,2)),  //$NON-NLS-1$
				new AttributeImpl("F","false"),   //$NON-NLS-1$ //$NON-NLS-2$
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

	@AfterEach
	public void tearDown() throws Exception {
		this.testData = null;
		this.baseData = null;
	}

	@Test
	public void iterator() {
		ArrayList<Attribute> ref = new ArrayList<>();
		ref.addAll(Arrays.asList(this.baseData));

		Iterator<Attribute> it = this.testData.attributes().iterator();
		while(!ref.isEmpty()) {
			assertTrue(it.hasNext(), this.id);
			Attribute attr = it.next();
			assertNotNull(attr, this.id);
			assertTrue(ref.remove(attr), this.id);
		}
		
		assertFalse(it.hasNext(), this.id);
	}

	@Test
	public void hasAttribute() {
		assertTrue(this.testData.hasAttribute("A"), this.id);  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"), this.id);  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("B"), this.id);  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"), this.id);  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"), this.id);  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"), this.id);  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"), this.id);  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"), this.id);  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"), this.id);  //$NON-NLS-1$
	}

	@Test
	public void getAllAttributes() {
		assertEpsilonEquals(this.baseData, this.testData.getAllAttributes().toArray());
	}

	@Test
	public void getAllAttributesByType() {
		HashMap<AttributeType,Collection<Attribute>> map = new HashMap<>();
		for(Attribute data : this.baseData) {
			AttributeType type = data.getType();
			Collection<Attribute> col = map.get(type);
			if (col==null) {
				col = new ArrayList<>();
				map.put(type,col);
			}
			col.add(data);
		}
		
		assertEquals(map, this.testData.getAllAttributesByType(), this.id);
	}


	@Test
	public void getAllAttributeNames() {
		assertEpsilonEquals(new String[] {
				"A",  //$NON-NLS-1$
				"B",  //$NON-NLS-1$
				"C",  //$NON-NLS-1$
				"D",  //$NON-NLS-1$
				"E",  //$NON-NLS-1$
				"F",  //$NON-NLS-1$
		}, this.testData.getAllAttributeNames().toArray());
	}

	@Test
	public void getAttributeString() {
		assertEquals(this.baseData[0],this.testData.getAttribute("A"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("X"));  //$NON-NLS-1$
		assertEquals(this.baseData[1],this.testData.getAttribute("B"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("Y"));  //$NON-NLS-1$
		assertEquals(this.baseData[2],this.testData.getAttribute("C"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("Z"));  //$NON-NLS-1$
		assertEquals(this.baseData[3],this.testData.getAttribute("D"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("W"));  //$NON-NLS-1$
		assertEquals(this.baseData[4],this.testData.getAttribute("E"));  //$NON-NLS-1$
		assertEquals(this.baseData[5],this.testData.getAttribute("F"));  //$NON-NLS-1$
	}

	@Test
	public void getAttributeStringAttributeValue() {
		AttributeValue defaultValue = new AttributeValueImpl();
		assertEquals(this.baseData[0],this.testData.getAttribute("A",defaultValue));  //$NON-NLS-1$
		assertSame(defaultValue, this.testData.getAttribute("X", defaultValue));  //$NON-NLS-1$
		assertEquals(this.baseData[1],this.testData.getAttribute("B", defaultValue));  //$NON-NLS-1$
		assertSame(defaultValue, this.testData.getAttribute("Y", defaultValue));  //$NON-NLS-1$
		assertEquals(this.baseData[2],this.testData.getAttribute("C", defaultValue));  //$NON-NLS-1$
		assertSame(defaultValue, this.testData.getAttribute("Z", defaultValue));  //$NON-NLS-1$
		assertEquals(this.baseData[3],this.testData.getAttribute("D", defaultValue));  //$NON-NLS-1$
		assertSame(defaultValue, this.testData.getAttribute("W", defaultValue));  //$NON-NLS-1$
		assertEquals(this.baseData[4],this.testData.getAttribute("E", defaultValue));  //$NON-NLS-1$
		assertEquals(this.baseData[5],this.testData.getAttribute("F", defaultValue));  //$NON-NLS-1$
	}

	@Test
	public void getAttributeObject() {
		assertEquals(this.baseData[0],this.testData.getAttributeObject("A"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("X"));  //$NON-NLS-1$
		assertEquals(this.baseData[1],this.testData.getAttributeObject("B"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("Y"));  //$NON-NLS-1$
		assertEquals(this.baseData[2],this.testData.getAttributeObject("C"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("Z"));  //$NON-NLS-1$
		assertEquals(this.baseData[3],this.testData.getAttributeObject("D"));  //$NON-NLS-1$
		assertNull(this.testData.getAttribute("W"));  //$NON-NLS-1$
		assertEquals(this.baseData[4],this.testData.getAttributeObject("E"));  //$NON-NLS-1$
		assertEquals(this.baseData[5],this.testData.getAttributeObject("F"));  //$NON-NLS-1$
	}

	@Test
	public void getAttributeAsBoolString() throws Exception {
		assertTrue(this.testData.getAttributeAsBool("A")); //$NON-NLS-1$
		assertTrue(this.testData.getAttributeAsBool("B")); //$NON-NLS-1$
		assertTrue(this.testData.getAttributeAsBool("C")); //$NON-NLS-1$
		assertInvalidValue(this.testData,"getAttributeAsBool","D");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsBool","E");  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(this.testData.getAttributeAsBool("F")); //$NON-NLS-1$
	}

	@Test
	public void getAttributeStringBoolean() throws Exception {
		assertTrue(this.testData.getAttribute("A",true)); //$NON-NLS-1$
		assertTrue(this.testData.getAttribute("B",false)); //$NON-NLS-1$
		assertTrue(this.testData.getAttribute("C",false)); //$NON-NLS-1$
		assertTrue(this.testData.getAttribute("D",true)); //$NON-NLS-1$
		assertFalse(this.testData.getAttribute("E",false)); //$NON-NLS-1$
		assertFalse(this.testData.getAttribute("F",true)); //$NON-NLS-1$
	}

	@Test
	public void getAttributeAsIntString() throws Exception {
		assertEquals(1,this.testData.getAttributeAsInt("A")); //$NON-NLS-1$
		assertEquals(2,this.testData.getAttributeAsInt("B")); //$NON-NLS-1$
		assertEquals(1, this.testData.getAttributeAsInt("C")); //$NON-NLS-1$
		assertInvalidValue(this.testData,"getAttributeAsInt","D");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsInt","E");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsInt","F");  //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Test
	public void getAttributeStringInt() throws Exception {
		assertEquals(1,this.testData.getAttribute("A",5)); //$NON-NLS-1$
		assertEquals(2,this.testData.getAttribute("B",34)); //$NON-NLS-1$
		assertEquals(1,this.testData.getAttribute("C",18)); //$NON-NLS-1$
		assertEquals(24,this.testData.getAttribute("D",24)); //$NON-NLS-1$
		assertEquals(-34,this.testData.getAttribute("E",-34)); //$NON-NLS-1$
		assertEquals(18,this.testData.getAttribute("F",18)); //$NON-NLS-1$
	}

	@Test
	public void getAttributeAsLongString() throws Exception {
		assertEquals(1,this.testData.getAttributeAsLong("A")); //$NON-NLS-1$
		assertEquals(2,this.testData.getAttributeAsLong("B")); //$NON-NLS-1$
		assertEquals(1, this.testData.getAttributeAsLong("C")); //$NON-NLS-1$
		assertInvalidValue(this.testData,"getAttributeAsLong","D");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsLong","E");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsLong","F");  //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void getAttributegStringLong() throws Exception {
		assertEquals(1,this.testData.getAttribute("A",5)); //$NON-NLS-1$
		assertEquals(2,this.testData.getAttribute("B",34)); //$NON-NLS-1$
		assertEquals(1,this.testData.getAttribute("C",18)); //$NON-NLS-1$
		assertEquals(24,this.testData.getAttribute("D",24)); //$NON-NLS-1$
		assertEquals(-34,this.testData.getAttribute("E",-34)); //$NON-NLS-1$
		assertEquals(18,this.testData.getAttribute("F",18)); //$NON-NLS-1$
	}

	@Test
	public void getAttributeAsFloatString() throws Exception {
		assertEpsilonEquals(1f,this.testData.getAttributeAsFloat("A")); //$NON-NLS-1$
		assertEpsilonEquals(2f,this.testData.getAttributeAsFloat("B")); //$NON-NLS-1$
		assertEpsilonEquals(1f, this.testData.getAttributeAsFloat("C")); //$NON-NLS-1$
		assertInvalidValue(this.testData,"getAttributeAsFloat","D");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsFloat","E");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsFloat","F");  //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void getAttributeStringFloat() throws Exception {
		assertEpsilonEquals(1f,this.testData.getAttribute("A",5f)); //$NON-NLS-1$
		assertEpsilonEquals(2f,this.testData.getAttribute("B",34f)); //$NON-NLS-1$
		assertEpsilonEquals(1f,this.testData.getAttribute("C",18f)); //$NON-NLS-1$
		assertEpsilonEquals(24f,this.testData.getAttribute("D",24f)); //$NON-NLS-1$
		assertEpsilonEquals(-34f,this.testData.getAttribute("E",-34f)); //$NON-NLS-1$
		assertEpsilonEquals(18f,this.testData.getAttribute("F",18f)); //$NON-NLS-1$
	}

	@Test
	public void getAttributeAsDoubleString() throws Exception {
		assertEpsilonEquals(1.,this.testData.getAttributeAsDouble("A")); //$NON-NLS-1$
		assertEpsilonEquals(2.,this.testData.getAttributeAsDouble("B")); //$NON-NLS-1$
		assertEpsilonEquals(1., this.testData.getAttributeAsDouble("C")); //$NON-NLS-1$
		assertInvalidValue(this.testData,"getAttributeAsDouble","D");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsDouble","E");  //$NON-NLS-1$ //$NON-NLS-2$
		assertInvalidValue(this.testData,"getAttributeAsDouble","F");  //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void getAttributeStringDouble() throws Exception {
		assertEpsilonEquals(1.,this.testData.getAttribute("A",5.)); //$NON-NLS-1$
		assertEpsilonEquals(2.,this.testData.getAttribute("B",34.)); //$NON-NLS-1$
		assertEpsilonEquals(1.,this.testData.getAttribute("C",18.)); //$NON-NLS-1$
		assertEpsilonEquals(24.,this.testData.getAttribute("D",24.)); //$NON-NLS-1$
		assertEpsilonEquals(-34.,this.testData.getAttribute("E",-34.)); //$NON-NLS-1$
		assertEpsilonEquals(18.,this.testData.getAttribute("F",18.)); //$NON-NLS-1$
	}

	@Test
	public void getAttributeAsStringString() throws Exception {
		assertEquals(Long.toString(1),this.testData.getAttributeAsString("A")); //$NON-NLS-1$
		assertEquals(Double.toString(2.),this.testData.getAttributeAsString("B")); //$NON-NLS-1$
		assertEquals(Boolean.toString(true),this.testData.getAttributeAsString("C")); //$NON-NLS-1$
		assertEquals("Hello",this.testData.getAttributeAsString("D"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(1.+";"+2.,this.testData.getAttributeAsString("E")); 		 //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Boolean.toString(false),this.testData.getAttributeAsString("F")); //$NON-NLS-1$
	}

	@Test
	public void getAttributeStringString() throws Exception {
		assertEquals(Long.toString(1),this.testData.getAttribute("A","default"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Double.toString(2.),this.testData.getAttribute("B","default"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Boolean.toString(true),this.testData.getAttribute("C","default"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Hello",this.testData.getAttribute("D","default"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(1.+";"+2.,this.testData.getAttribute("E","default"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(Boolean.toString(false),this.testData.getAttribute("F","default"));  //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void freeMemory() {
		this.testData.freeMemory();
		iterator();
	}

}

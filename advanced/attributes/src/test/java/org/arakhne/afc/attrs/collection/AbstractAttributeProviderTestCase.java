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
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.attr.InvalidAttributeTypeException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public abstract class AbstractAttributeProviderTestCase<T extends AttributeProvider> extends AbstractTestCase {

	protected String id;

	protected T testData;
	
	protected Attribute[] baseData;

	
	public AbstractAttributeProviderTestCase(String id) {
		super();
		id = id;
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
		for (Attribute a : baseData) {
			provider.setAttribute(a);
		}
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		baseData = new Attribute[] {
				new AttributeImpl("A",1),  //$NON-NLS-1$
				new AttributeImpl("B",2.),  //$NON-NLS-1$
				new AttributeImpl("C",true),  //$NON-NLS-1$
				new AttributeImpl("D","Hello"),   //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E",new Point2d(1,2)),  //$NON-NLS-1$
				new AttributeImpl("F","false"),   //$NON-NLS-1$ //$NON-NLS-2$
		};
		testData = setUpTestCase();
		if (testData instanceof AttributeCollection) {
			createTestCaseData((AttributeCollection)testData);
		}
	}

	/** Initialize the test case.
	 * @return the set up test case.
	 * @throws Exception
	 */
	protected abstract T setUpTestCase() throws Exception;

	@AfterEach
	public void tearDown() throws Exception {
		testData = null;
		baseData = null;
	}

	@DisplayName("iterator")
	@Test
	public void iterator() {
		ArrayList<Attribute> ref = new ArrayList<>();
		ref.addAll(Arrays.asList(baseData));

		Iterator<Attribute> it = testData.attributes().iterator();
		while(!ref.isEmpty()) {
			assertTrue(it.hasNext(), id);
			Attribute attr = it.next();
			assertNotNull(attr, id);
			assertTrue(ref.remove(attr), id);
		}
		
		assertFalse(it.hasNext(), id);
	}

	@DisplayName("hasAttribute")
	@Nested
	public class HasAttribute {

		@DisplayName("hasAttribute_1")
		@Test
		public void hasAttribute_1() {
			assertTrue(testData.hasAttribute("A"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_2")
		@Test
		public void hasAttribute_2() {
			assertFalse(testData.hasAttribute("X"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_3")
		@Test
		public void hasAttribute_3() {
			assertTrue(testData.hasAttribute("B"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_4")
		@Test
		public void hasAttribute_4() {
			assertFalse(testData.hasAttribute("Y"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_5")
		@Test
		public void hasAttribute_5() {
			assertTrue(testData.hasAttribute("C"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_6")
		@Test
		public void hasAttribute_6() {
			assertTrue(testData.hasAttribute("D"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_7")
		@Test
		public void hasAttribute_7() {
			assertFalse(testData.hasAttribute("Z"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_8")
		@Test
		public void hasAttribute_8() {
			assertTrue(testData.hasAttribute("E"), id); //$NON-NLS-1$
		}

		@DisplayName("hasAttribute_9")
		@Test
		public void hasAttribute_9() {
			assertTrue(testData.hasAttribute("F"), id); //$NON-NLS-1$
		}
	}

	@DisplayName("getAllAttributes")
	@Nested
	public class GetAllAttributes {
		@DisplayName("#1")
		@Test
		public void getAllAttributes() {
			assertEpsilonEquals(baseData, testData.getAllAttributes().toArray());
		}
	}

	@DisplayName("getAllAttributesByType")
	@Nested
	public class GetAllAttributesByType {

		private HashMap<AttributeType, Collection<Attribute>> map;

		@BeforeEach
		public void setUp() {
			map = new HashMap<>();
			for (Attribute data : baseData) {
				var type = data.getType();
				var col = map.get(type);
				if (col == null) {
					col = new ArrayList<>();
					map.put(type, col);
				}
				col.add(data);
			}
		}

		@DisplayName("getAllAttributesByType_1")
		@Test
		public void getAllAttributesByType_1() {
			assertEquals(map, testData.getAllAttributesByType(), id);
		}
	}

	@DisplayName("getAllAttributeNames")
	@Nested
	public class GetAllAttributeNames {
		
		@DisplayName("#1")
		@Test
		public void getAllAttributeNames() {
			assertEpsilonEquals(new String[] {
					"A",  //$NON-NLS-1$
					"B",  //$NON-NLS-1$
					"C",  //$NON-NLS-1$
					"D",  //$NON-NLS-1$
					"E",  //$NON-NLS-1$
					"F",  //$NON-NLS-1$
			}, testData.getAllAttributeNames().toArray());
		}
	}

	@DisplayName("getAttribute")
	@Nested
	public class GetAttribute {

		private AttributeValue defaultValue;

		@BeforeEach
		public void setUp() {
			defaultValue = new AttributeValueImpl();
		}

		@DisplayName("(String)")
		@Nested
		public class WithString {

			@DisplayName("getAttributeString_1")
			@Test
			public void getAttributeString_1() {
				assertEquals(baseData[0], testData.getAttribute("A")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_2")
			@Test
			public void getAttributeString_2() {
				assertNull(testData.getAttribute("X")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_3")
			@Test
			public void getAttributeString_3() {
				assertEquals(baseData[1], testData.getAttribute("B")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_4")
			@Test
			public void getAttributeString_4() {
				assertNull(testData.getAttribute("Y")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_5")
			@Test
			public void getAttributeString_5() {
				assertEquals(baseData[2], testData.getAttribute("C")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_6")
			@Test
			public void getAttributeString_6() {
				assertNull(testData.getAttribute("Z")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_7")
			@Test
			public void getAttributeString_7() {
				assertEquals(baseData[3], testData.getAttribute("D")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_8")
			@Test
			public void getAttributeString_8() {
				assertNull(testData.getAttribute("W")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_9")
			@Test
			public void getAttributeString_9() {
				assertEquals(baseData[4], testData.getAttribute("E")); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeString_10")
			@Test
			public void getAttributeString_10() {
				assertEquals(baseData[5], testData.getAttribute("F")); //$NON-NLS-1$
			}
		}

		@DisplayName("(String,AttributeValue)")
		@Nested
		public class WithStringAttributeValue {

			@DisplayName("getAttributeStringAttributeValue_1")
			@Test
			public void getAttributeStringAttributeValue_1() {
				assertEquals(baseData[0], testData.getAttribute("A", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_2")
			@Test
			public void getAttributeStringAttributeValue_2() {
				assertSame(defaultValue, testData.getAttribute("X", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_3")
			@Test
			public void getAttributeStringAttributeValue_3() {
				assertEquals(baseData[1], testData.getAttribute("B", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_4")
			@Test
			public void getAttributeStringAttributeValue_4() {
				assertSame(defaultValue, testData.getAttribute("Y", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_5")
			@Test
			public void getAttributeStringAttributeValue_5() {
				assertEquals(baseData[2], testData.getAttribute("C", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_6")
			@Test
			public void getAttributeStringAttributeValue_6() {
				assertSame(defaultValue, testData.getAttribute("Z", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_7")
			@Test
			public void getAttributeStringAttributeValue_7() {
				assertEquals(baseData[3], testData.getAttribute("D", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_8")
			@Test
			public void getAttributeStringAttributeValue_8() {
				assertSame(defaultValue, testData.getAttribute("W", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_9")
			@Test
			public void getAttributeStringAttributeValue_9() {
				assertEquals(baseData[4], testData.getAttribute("E", defaultValue)); //$NON-NLS-1$
			}
	
			@DisplayName("getAttributeStringAttributeValue_10")
			@Test
			public void getAttributeStringAttributeValue_10() {
				assertEquals(baseData[5], testData.getAttribute("F", defaultValue)); //$NON-NLS-1$
			}
		}

		@DisplayName("(String,boolean)")
		@Nested
		public class WithStringBoolean {

			@DisplayName("getAttributeStringBoolean_1")
			@Test
			public void getAttributeStringBoolean_1() throws Exception {
				assertTrue(testData.getAttribute("A", true)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringBoolean_2")
			@Test
			public void getAttributeStringBoolean_2() throws Exception {
				assertTrue(testData.getAttribute("B", false)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringBoolean_3")
			@Test
			public void getAttributeStringBoolean_3() throws Exception {
				assertTrue(testData.getAttribute("C", false)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringBoolean_4")
			@Test
			public void getAttributeStringBoolean_4() throws Exception {
				assertTrue(testData.getAttribute("D", true)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringBoolean_5")
			@Test
			public void getAttributeStringBoolean_5() throws Exception {
				assertFalse(testData.getAttribute("E", false)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringBoolean_6")
			@Test
			public void getAttributeStringBoolean_6() throws Exception {
				assertFalse(testData.getAttribute("F", true)); //$NON-NLS-1$
			}
		}

		@DisplayName("(String,int)")
		@Nested
		public class WithStringInt {

			@DisplayName("getAttributeStringInt_1")
			@Test
			public void getAttributeStringInt_1() throws Exception {
				assertEquals(1, testData.getAttribute("A", 5)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringInt_2")
			@Test
			public void getAttributeStringInt_2() throws Exception {
				assertEquals(2, testData.getAttribute("B", 34)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringInt_3")
			@Test
			public void getAttributeStringInt_3() throws Exception {
				assertEquals(1, testData.getAttribute("C", 18)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringInt_4")
			@Test
			public void getAttributeStringInt_4() throws Exception {
				assertEquals(24, testData.getAttribute("D", 24)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringInt_5")
			@Test
			public void getAttributeStringInt_5() throws Exception {
				assertEquals(-34, testData.getAttribute("E", -34)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringInt_6")
			@Test
			public void getAttributeStringInt_6() throws Exception {
				assertEquals(18, testData.getAttribute("F", 18)); //$NON-NLS-1$
			}
		}

		@DisplayName("(String,long)")
		@Nested
		public class WithStringLong {

			@DisplayName("getAttributegStringLong_1")
			@Test
			public void getAttributegStringLong_1() throws Exception {
				assertEquals(1, testData.getAttribute("A", 5)); //$NON-NLS-1$
			}

			@DisplayName("getAttributegStringLong_2")
			@Test
			public void getAttributegStringLong_2() throws Exception {
				assertEquals(2, testData.getAttribute("B", 34)); //$NON-NLS-1$
			}

			@DisplayName("getAttributegStringLong_3")
			@Test
			public void getAttributegStringLong_3() throws Exception {
				assertEquals(1, testData.getAttribute("C", 18)); //$NON-NLS-1$
			}

			@DisplayName("getAttributegStringLong_4")
			@Test
			public void getAttributegStringLong_4() throws Exception {
				assertEquals(24, testData.getAttribute("D", 24)); //$NON-NLS-1$
			}

			@DisplayName("getAttributegStringLong_5")
			@Test
			public void getAttributegStringLong_5() throws Exception {
				assertEquals(-34, testData.getAttribute("E", -34)); //$NON-NLS-1$
			}

			@DisplayName("getAttributegStringLong_6")
			@Test
			public void getAttributegStringLong_6() throws Exception {
				assertEquals(18, testData.getAttribute("F", 18)); //$NON-NLS-1$
			}
		}

		@DisplayName("(String,float)")
		@Nested
		public class WithStringFloat {

			@DisplayName("getAttributeStringFloat_1")
			@Test
			public void getAttributeStringFloat_1() throws Exception {
				assertEpsilonEquals(1f, testData.getAttribute("A", 5f)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringFloat_2")
			@Test
			public void getAttributeStringFloat_2() throws Exception {
				assertEpsilonEquals(2f, testData.getAttribute("B", 34f)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringFloat_3")
			@Test
			public void getAttributeStringFloat_3() throws Exception {
				assertEpsilonEquals(1f, testData.getAttribute("C", 18f)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringFloat_4")
			@Test
			public void getAttributeStringFloat_4() throws Exception {
				assertEpsilonEquals(24f, testData.getAttribute("D", 24f)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringFloat_5")
			@Test
			public void getAttributeStringFloat_5() throws Exception {
				assertEpsilonEquals(-34f, testData.getAttribute("E", -34f)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringFloat_6")
			@Test
			public void getAttributeStringFloat_6() throws Exception {
				assertEpsilonEquals(18f, testData.getAttribute("F", 18f)); //$NON-NLS-1$
			}
		}

		@DisplayName("(String,double)")
		@Nested
		public class WithStringDouble {

			@DisplayName("getAttributeStringDouble_1")
			@Test
			public void getAttributeStringDouble_1() throws Exception {
				assertEpsilonEquals(1., testData.getAttribute("A", 5.)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringDouble_2")
			@Test
			public void getAttributeStringDouble_2() throws Exception {
				assertEpsilonEquals(2., testData.getAttribute("B", 34.)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringDouble_3")
			@Test
			public void getAttributeStringDouble_3() throws Exception {
				assertEpsilonEquals(1., testData.getAttribute("C", 18.)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringDouble_4")
			@Test
			public void getAttributeStringDouble_4() throws Exception {
				assertEpsilonEquals(24., testData.getAttribute("D", 24.)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringDouble_5")
			@Test
			public void getAttributeStringDouble_5() throws Exception {
				assertEpsilonEquals(-34., testData.getAttribute("E", -34.)); //$NON-NLS-1$
			}

			@DisplayName("getAttributeStringDouble_6")
			@Test
			public void getAttributeStringDouble_6() throws Exception {
				assertEpsilonEquals(18., testData.getAttribute("F", 18.)); //$NON-NLS-1$
			}
		}
		
		@DisplayName("(String,String)")
		@Nested
		public class WithStringString {

			@DisplayName("getAttributeStringString_1")
			@Test
			public void getAttributeStringString_1() throws Exception {
				assertEquals(Long.toString(1), testData.getAttribute("A", "default")); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@DisplayName("getAttributeStringString_2")
			@Test
			public void getAttributeStringString_2() throws Exception {
				assertEquals(Double.toString(2.), testData.getAttribute("B", "default")); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@DisplayName("getAttributeStringString_3")
			@Test
			public void getAttributeStringString_3() throws Exception {
				assertEquals(Boolean.toString(true), testData.getAttribute("C", "default")); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@DisplayName("getAttributeStringString_4")
			@Test
			public void getAttributeStringString_4() throws Exception {
				assertEquals("Hello", testData.getAttribute("D", "default")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

			@DisplayName("getAttributeStringString_5")
			@Test
			public void getAttributeStringString_5() throws Exception {
				assertEquals(1. + ";" + 2., testData.getAttribute("E", "default")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

			@DisplayName("getAttributeStringString_6")
			@Test
			public void getAttributeStringString_6() throws Exception {
				assertEquals(Boolean.toString(false), testData.getAttribute("F", "default")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	@DisplayName("getAttributeObject")
	@Nested
	public class GetAttributeObject {

		@DisplayName("getAttributeObject_1")
		@Test
		public void getAttributeObject_1() {
			assertEquals(baseData[0], testData.getAttributeObject("A")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_2")
		@Test
		public void getAttributeObject_2() {
			assertNull(testData.getAttribute("X")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_3")
		@Test
		public void getAttributeObject_3() {
			assertEquals(baseData[1], testData.getAttributeObject("B")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_4")
		@Test
		public void getAttributeObject_4() {
			assertNull(testData.getAttribute("Y")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_5")
		@Test
		public void getAttributeObject_5() {
			assertEquals(baseData[2], testData.getAttributeObject("C")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_6")
		@Test
		public void getAttributeObject_6() {
			assertNull(testData.getAttribute("Z")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_7")
		@Test
		public void getAttributeObject_7() {
			assertEquals(baseData[3], testData.getAttributeObject("D")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_8")
		@Test
		public void getAttributeObject_8() {
			assertNull(testData.getAttribute("W")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_9")
		@Test
		public void getAttributeObject_9() {
			assertEquals(baseData[4], testData.getAttributeObject("E")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeObject_10")
		@Test
		public void getAttributeObject_10() {
			assertEquals(baseData[5], testData.getAttributeObject("F")); //$NON-NLS-1$
		}
	}

	@DisplayName("getAttributeAsBool")
	@Nested
	public class GetAttributeAsBool {

		@DisplayName("getAttributeAsBoolString_1")
		@Test
		public void getAttributeAsBoolString_1() throws Exception {
			assertTrue(testData.getAttributeAsBool("A")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsBoolString_2")
		@Test
		public void getAttributeAsBoolString_2() throws Exception {
			assertTrue(testData.getAttributeAsBool("B")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsBoolString_3")
		@Test
		public void getAttributeAsBoolString_3() throws Exception {
			assertTrue(testData.getAttributeAsBool("C")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsBoolString_4")
		@Test
		public void getAttributeAsBoolString_4() throws Exception {
			assertInvalidValue(testData, "getAttributeAsBool", "D"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsBoolString_5")
		@Test
		public void getAttributeAsBoolString_5() throws Exception {
			assertInvalidValue(testData, "getAttributeAsBool", "E"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsBoolString_6")
		@Test
		public void getAttributeAsBoolString_6() throws Exception {
			assertFalse(testData.getAttributeAsBool("F")); //$NON-NLS-1$
		}
	}

	@DisplayName("getAttributeAsInt")
	@Nested
	public class GetAttributeAsInt {

		@DisplayName("getAttributeAsIntString_1")
		@Test
		public void getAttributeAsIntString_1() throws Exception {
			assertEquals(1, testData.getAttributeAsInt("A")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsIntString_2")
		@Test
		public void getAttributeAsIntString_2() throws Exception {
			assertEquals(2, testData.getAttributeAsInt("B")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsIntString_3")
		@Test
		public void getAttributeAsIntString_3() throws Exception {
			assertEquals(1, testData.getAttributeAsInt("C")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsIntString_4")
		@Test
		public void getAttributeAsIntString_4() throws Exception {
			assertInvalidValue(testData, "getAttributeAsInt", "D"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsIntString_5")
		@Test
		public void getAttributeAsIntString_5() throws Exception {
			assertInvalidValue(testData, "getAttributeAsInt", "E"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsIntString_6")
		@Test
		public void getAttributeAsIntString_6() throws Exception {
			assertInvalidValue(testData, "getAttributeAsInt", "F"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("getAttributeAsLong")
	@Nested
	public class GetAttributeAsLong {

		@DisplayName("getAttributeAsLongString_1")
		@Test
		public void getAttributeAsLongString_1() throws Exception {
			assertEquals(1, testData.getAttributeAsLong("A")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsLongString_2")
		@Test
		public void getAttributeAsLongString_2() throws Exception {
			assertEquals(2, testData.getAttributeAsLong("B")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsLongString_3")
		@Test
		public void getAttributeAsLongString_3() throws Exception {
			assertEquals(1, testData.getAttributeAsLong("C")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsLongString_4")
		@Test
		public void getAttributeAsLongString_4() throws Exception {
			assertInvalidValue(testData, "getAttributeAsLong", "D"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsLongString_5")
		@Test
		public void getAttributeAsLongString_5() throws Exception {
			assertInvalidValue(testData, "getAttributeAsLong", "E"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsLongString_6")
		@Test
		public void getAttributeAsLongString_6() throws Exception {
			assertInvalidValue(testData, "getAttributeAsLong", "F"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("getAttributeAsFloat")
	@Nested
	public class GetAttributeAsFloat {

		@DisplayName("getAttributeAsFloatString_1")
		@Test
		public void getAttributeAsFloatString_1() throws Exception {
			assertEpsilonEquals(1f, testData.getAttributeAsFloat("A")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsFloatString_2")
		@Test
		public void getAttributeAsFloatString_2() throws Exception {
			assertEpsilonEquals(2f, testData.getAttributeAsFloat("B")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsFloatString_3")
		@Test
		public void getAttributeAsFloatString_3() throws Exception {
			assertEpsilonEquals(1f, testData.getAttributeAsFloat("C")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsFloatString_4")
		@Test
		public void getAttributeAsFloatString_4() throws Exception {
			assertInvalidValue(testData, "getAttributeAsFloat", "D"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsFloatString_5")
		@Test
		public void getAttributeAsFloatString_5() throws Exception {
			assertInvalidValue(testData, "getAttributeAsFloat", "E"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsFloatString_6")
		@Test
		public void getAttributeAsFloatString_6() throws Exception {
			assertInvalidValue(testData, "getAttributeAsFloat", "F"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("getAttributeAsDouble")
	@Nested
	public class GetAttributeAsDouble {

		@DisplayName("getAttributeAsDoubleString_1")
		@Test
		public void getAttributeAsDoubleString_1() throws Exception {
			assertEpsilonEquals(1., testData.getAttributeAsDouble("A")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsDoubleString_2")
		@Test
		public void getAttributeAsDoubleString_2() throws Exception {
			assertEpsilonEquals(2., testData.getAttributeAsDouble("B")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsDoubleString_3")
		@Test
		public void getAttributeAsDoubleString_3() throws Exception {
			assertEpsilonEquals(1., testData.getAttributeAsDouble("C")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsDoubleString_4")
		@Test
		public void getAttributeAsDoubleString_4() throws Exception {
			assertInvalidValue(testData, "getAttributeAsDouble", "D"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsDoubleString_5")
		@Test
		public void getAttributeAsDoubleString_5() throws Exception {
			assertInvalidValue(testData, "getAttributeAsDouble", "E"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsDoubleString_6")
		@Test
		public void getAttributeAsDoubleString_6() throws Exception {
			assertInvalidValue(testData, "getAttributeAsDouble", "F"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("getAttributeAsString")
	@Nested
	public class GetAttributeAsString {

		@DisplayName("getAttributeAsStringString_1")
		@Test
		public void getAttributeAsStringString_1() throws Exception {
			assertEquals(Long.toString(1), testData.getAttributeAsString("A")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsStringString_2")
		@Test
		public void getAttributeAsStringString_2() throws Exception {
			assertEquals(Double.toString(2.), testData.getAttributeAsString("B")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsStringString_3")
		@Test
		public void getAttributeAsStringString_3() throws Exception {
			assertEquals(Boolean.toString(true), testData.getAttributeAsString("C")); //$NON-NLS-1$
		}

		@DisplayName("getAttributeAsStringString_4")
		@Test
		public void getAttributeAsStringString_4() throws Exception {
			assertEquals("Hello", testData.getAttributeAsString("D")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsStringString_5")
		@Test
		public void getAttributeAsStringString_5() throws Exception {
			assertEquals(1. + ";" + 2., testData.getAttributeAsString("E")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("getAttributeAsStringString_6")
		@Test
		public void getAttributeAsStringString_6() throws Exception {
			assertEquals(Boolean.toString(false), testData.getAttributeAsString("F")); //$NON-NLS-1$
		}
	}
	
	@DisplayName("freeMemory")
	@Nested
	public class FreeMemory {

		@DisplayName("#1")
		@Test
		public void freeMemory() {
			testData.freeMemory();
			iterator();
		}
	}

}

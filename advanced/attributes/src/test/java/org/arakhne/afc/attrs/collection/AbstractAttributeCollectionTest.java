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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractAttributeCollectionTest<T extends AttributeCollection> extends AbstractAttributeProviderTestCase<T> {

	protected static final Attribute[] NEW_VALUES = new Attribute[] {
			new AttributeImpl("A",false),	  //$NON-NLS-1$
			new AttributeImpl("D","34"),	   //$NON-NLS-1$ //$NON-NLS-2$
			new AttributeImpl("Z",17f),	  //$NON-NLS-1$
		};
	
	protected ListenerStub listenerStub;
	
	public AbstractAttributeCollectionTest(String id) {
		super(id);
	}
	
	@Override
	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		listenerStub = new ListenerStub();
		testData.addAttributeChangeListener(listenerStub);
	}
	
	@Override
	@AfterEach
	public void tearDown() throws Exception {
		listenerStub.reset();
		listenerStub = null;
		super.tearDown();
	}
	
	public static Stream<Arguments> providesNewValueArguments() {
		final List<Arguments> args = new ArrayList<>();
		for (final var value : NEW_VALUES) {
			args.add(Arguments.of(value));
		}
		return args.stream();
	}
		
	@DisplayName("setAttribute")
	@Nested
	public class SetAttribute {

		private void runSetAttributeValue(Class<?>[] types, Object[] parameters, Attribute attr) throws Exception {
			String name = attr.getName();
			
			boolean attrExists = testData.hasAttribute(name);
			AttributeValue oldValue = null;
			if (attrExists) {
				oldValue = testData.getAttribute(name);
			}
		
			Method method = testData.getClass().getMethod("setAttribute", types);  //$NON-NLS-1$
			Object o = method.invoke(testData, parameters);

			assertTrue(o instanceof Attribute);
			assertEquals(attr,o);
			
			assertNotNull(testData.getAttribute(name));
			assertEquals(attr.getType(),testData.getAttribute(name).getType());
			assertEquals(attr,testData.getAttribute(name));
			
			// Test events
			String message = id+": set attribute "+name;  //$NON-NLS-1$
			listenerStub.assertNames(message, name);
			listenerStub.assertValues(message, attr);
			if (!attrExists) {
				listenerStub.assertTypes(message,
						AttributeChangeEvent.Type.ADDITION);
				listenerStub.assertOldNames(message, new String[]{null});
				listenerStub.assertOldValues(message, new AttributeValue[]{null});
			}
			else {
				listenerStub.assertTypes(message,
						AttributeChangeEvent.Type.VALUE_UPDATE);
				listenerStub.assertOldNames(message, name);
				listenerStub.assertOldValues(message, oldValue);
			}
			
			listenerStub.reset();
		}

		private void runSetAttributeValue(Class<?> type, Object parameter, Attribute attr) throws Exception {
			runSetAttributeValue(
					new Class<?>[] {String.class, type},
					new Object[] {attr.getName(), parameter}, attr);
		}

		@DisplayName("(Attribute)")
		@Nested
		public class FromAttribute {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("org.arakhne.afc.attrs.collection.AbstractAttributeCollectionTest#providesNewValueArguments")
			public void setAttributeAttribute(Attribute attr) throws Exception {
				runSetAttributeValue(
						new Class<?>[] {Attribute.class},
						new Object[] {attr},
						attr);
			}
		}

		@DisplayName("(String,AttributeValue)")
		@Nested
		public class FromStringAttributeValue {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("org.arakhne.afc.attrs.collection.AbstractAttributeCollectionTest#providesNewValueArguments")
			public void setAttributeStringAttributeValue(Attribute attr) throws Exception {
				runSetAttributeValue(
						AttributeValue.class,
						attr,
						attr);
			}
		}

		@DisplayName("(String,boolean)")
		@Nested
		public class FromStringboolean {

			@DisplayName("#1")
			@Test
			public void setAttributeStringBoolean_1() throws Exception {
				Attribute attr = new AttributeImpl("A", false);  //$NON-NLS-1$
				runSetAttributeValue(
						boolean.class,
						attr.getBoolean(),
						attr);
			}

			@DisplayName("#2")
			@Test
			public void setAttributeStringBoolean_2() throws Exception {
				var attr = new AttributeImpl("X", false);  //$NON-NLS-1$
				runSetAttributeValue(
						boolean.class,
						attr.getBoolean(),
						attr);
			}
		}

		@DisplayName("(String,int)")
		@Nested
		public class FromStringInt {

			@DisplayName("#1")
			@Test
			public void setAttributeStringInt_1() throws Exception {
				Attribute attr = new AttributeImpl("E", 34);  //$NON-NLS-1$
				runSetAttributeValue(
						int.class,
						(int)attr.getInteger(),
						attr);
			}

			@DisplayName("#2")
			@Test
			public void setAttributeStringInt_2() throws Exception {
				var attr = new AttributeImpl("X", 34);  //$NON-NLS-1$
				runSetAttributeValue(
						int.class,
						(int)attr.getInteger(),
						attr);
			}
		}

		@DisplayName("(String,longAttribute)")
		@Nested
		public class FromStringLong {

			@DisplayName("#1")
			@Test
			public void setAttributeStringLong_1() throws Exception {
				Attribute attr = new AttributeImpl("E", 34);  //$NON-NLS-1$
				runSetAttributeValue(
						long.class,
						attr.getInteger(),
						attr);
			}

			@DisplayName("#2")
			@Test
			public void setAttributeStringLong_2() throws Exception {
				var attr = new AttributeImpl("X", 34);  //$NON-NLS-1$
				runSetAttributeValue(
						long.class,
						attr.getInteger(),
						attr);
			}
		}

		@DisplayName("(String,float)")
		@Nested
		public class FromStringFloat {

			@DisplayName("#1")
			@Test
			public void setAttributeStringFloat_1() throws Exception {
				Attribute attr = new AttributeImpl("E", 34f);  //$NON-NLS-1$
				runSetAttributeValue(
						float.class,
						(float)attr.getReal(),
						attr);
			}

			@DisplayName("#2")
			@Test
			public void setAttributeStringFloat_2() throws Exception {
				var attr = new AttributeImpl("X", 34f);  //$NON-NLS-1$
				runSetAttributeValue(
						float.class,
						(float)attr.getReal(),
						attr);
			}
		}

		@DisplayName("(String,double)")
		@Nested
		public class FromStringDouble {

			@DisplayName("#1")
			@Test
			public void setAttributeStringDouble_1() throws Exception {
				Attribute attr = new AttributeImpl("E", 34.);  //$NON-NLS-1$
				runSetAttributeValue(
						double.class,
						attr.getReal(),
						attr);
			}

			@DisplayName("#2")
			@Test
			public void setAttributeStringDouble_2() throws Exception {
				var attr = new AttributeImpl("X", 34.);  //$NON-NLS-1$
				runSetAttributeValue(
						double.class,
						attr.getReal(),
						attr);
			}
		}

		@DisplayName("(String,String)")
		@Nested
		public class FromStringString {

			@DisplayName("#1")
			@Test
			public void setAttributeStringString_1() throws Exception {
				Attribute attr = new AttributeImpl("E", "Toto");   //$NON-NLS-1$ //$NON-NLS-2$
				runSetAttributeValue(
						String.class,
						attr.getString(),
						attr);
			}

			@DisplayName("#2")
			@Test
			public void setAttributeStringString_2() throws Exception {
				var attr = new AttributeImpl("X", "Titi et Rominet");   //$NON-NLS-1$ //$NON-NLS-2$
				runSetAttributeValue(
						String.class,
						attr.getString(),
						attr);
			}
		}
	}

	@DisplayName("removeAttribute")
	@Nested
	public class RemoveAttribute {
	
		@DisplayName("#1")
		@Test
		public void removeAttributeString_1() {
			assertFalse(testData.removeAttribute("Y"));  //$NON-NLS-1$
			// Testing events
			var message = id+": removing Y";  //$NON-NLS-1$
			listenerStub.assertEmpty(message);
			listenerStub.reset();
		}

		@DisplayName("#2")
		@Test
		public void removeAttributeString_2() {
			assertTrue(testData.removeAttribute("C"));  //$NON-NLS-1$
			// Testing events
			var message = "removing C";  //$NON-NLS-1$
			listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL);
			listenerStub.assertNames(message, "C");  //$NON-NLS-1$
			listenerStub.assertOldNames(message, "C");  //$NON-NLS-1$
			listenerStub.assertValues(message, new AttributeValueImpl(true));
			listenerStub.assertOldValues(message, new AttributeValueImpl(true));
			listenerStub.reset();
		}

		@DisplayName("#1")
		@Test
		public void removeAttributeString_3() {
			assertFalse(testData.removeAttribute("X"));  //$NON-NLS-1$
			// Testing events
			var message = id+": removing X";  //$NON-NLS-1$
			listenerStub.assertEmpty(message);
			listenerStub.reset();
		}

		@DisplayName("#4")
		@Test
		public void removeAttributeString_4() {
			testData.removeAttribute("C");

			assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("B"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("C"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
			
		}
	}

	@DisplayName("removeAllAttributes")
	@Nested
	public class RemoveAllAttributes {
	
		@BeforeEach
		public void setUp() {
			assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("B"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
		}
		
		@DisplayName("#1")
		@Test
		public void removeAllAttributes_1() {
			assertTrue(testData.removeAllAttributes());
			var message = id+": removing all attributes";  //$NON-NLS-1$
			listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVE_ALL);
			listenerStub.assertNames(message, new String[]{null});
			listenerStub.assertOldNames(message, new String[]{null});
			listenerStub.assertValues(message, new AttributeValue[]{null});
			listenerStub.assertOldValues(message, new AttributeValue[]{null});
			listenerStub.reset();
		}
		
		@DisplayName("#2")
		@Test
		public void removeAllAttributes_2() {
			assertTrue(testData.removeAllAttributes());
			assertFalse(testData.hasAttribute("A"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("C"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("D"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("E"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("F"));  //$NON-NLS-1$
		}
		
		@DisplayName("#3")
		@Test
		public void removeAllAttributes_3() {
			testData.removeAllAttributes();
			listenerStub.reset();

			assertFalse(testData.removeAllAttributes());
			var message = id+": removing all attributes";  //$NON-NLS-1$
			listenerStub.assertEmpty(message);
			listenerStub.reset();
		}
		
		@DisplayName("#4")
		@Test
		public void removeAllAttributes_4() {
			testData.removeAllAttributes();
			testData.removeAllAttributes();
			assertFalse(testData.hasAttribute("A"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("C"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("D"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("E"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("F"));  //$NON-NLS-1$
		}
	}
	
	@DisplayName("renameAttribute")
	@Nested
	public class RenameAttribute {
	
		private AttributeValue oldValue;
		
		@BeforeEach
		public void setUp() {
			assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("B"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
			assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
			assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
			oldValue = testData.getAttribute("B");  //$NON-NLS-1$
		}

		@DisplayName("(String,Object,false)")
		@Nested
		public class WithoutOverwrite {
		
			@DisplayName("#1")
			@Test
			public void renameAttribute_1() {
				assertTrue(testData.renameAttribute("B", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
				// Testing events
				var message = id+": renaming B to ZZZ";  //$NON-NLS-1$
				listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
				listenerStub.assertNames(message, "ZZZ");  //$NON-NLS-1$
				listenerStub.assertOldNames(message, "B");  //$NON-NLS-1$
				listenerStub.assertValues(message, oldValue);
				listenerStub.assertOldValues(message, oldValue);
				listenerStub.reset();
			}
			
			@DisplayName("#2")
			@Test
			public void renameAttribute_2() {
				assertTrue(testData.renameAttribute("B", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
				assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
			}
			
			@DisplayName("#3")
			@Test
			public void renameAttribute_3() {
				assertTrue(testData.renameAttribute("B", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(oldValue, testData.getAttribute("ZZZ"));  //$NON-NLS-1$
			}
			
			@DisplayName("#4")
			@Test
			public void renameAttribute_4() {
				assertTrue(testData.renameAttribute("B", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
				listenerStub.reset();
				assertFalse(testData.renameAttribute("toto", "XXX", false));   //$NON-NLS-1$ //$NON-NLS-2$
				// Testing events
				var message = id+": renaming toto to XXX";  //$NON-NLS-1$
				listenerStub.assertEmpty(message);
				listenerStub.reset();
			}
			
			@DisplayName("#5")
			@Test
			public void renameAttribute_5() {
				assertTrue(testData.renameAttribute("B", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
				assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
			}
			
			@DisplayName("#6")
			@Test
			public void renameAttribute_6() {
				testData.renameAttribute("B", "ZZZ", false);   //$NON-NLS-1$ //$NON-NLS-2$
				oldValue = testData.getAttribute("F");  //$NON-NLS-1$
			}
			
			@DisplayName("#7")
			@Test
			public void renameAttribute_7() {
				testData.renameAttribute("B", "ZZZ", false);   //$NON-NLS-1$ //$NON-NLS-2$
				listenerStub.reset();
				assertFalse(testData.renameAttribute("F", "A", false));   //$NON-NLS-1$ //$NON-NLS-2$
				// Testing events
				var message = id+": renaming F to A";  //$NON-NLS-1$
				listenerStub.assertEmpty(message);
				listenerStub.reset();
			}
			
			@DisplayName("#8")
			@Test
			public void renameAttribute_8() {
				testData.renameAttribute("B", "ZZZ", false);   //$NON-NLS-1$ //$NON-NLS-2$
				oldValue = testData.getAttribute("F");  //$NON-NLS-1$
				AttributeValue oldValue2 = testData.getAttribute("A");  //$NON-NLS-1$
				testData.renameAttribute("F", "A", false);   //$NON-NLS-1$ //$NON-NLS-2$
				assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
				assertEquals(oldValue, testData.getAttribute("F"));  //$NON-NLS-1$
				assertEquals(oldValue2, testData.getAttribute("A"));  //$NON-NLS-1$
			}
		}

		@DisplayName("(String,Object,true)")
		@Nested
		public class WithOverwrite {

			@DisplayName("#1")
			@Test
			public void renameAttributeOverwrite_1() {
				assertTrue(testData.renameAttribute("B", "ZZZ", true));   //$NON-NLS-1$ //$NON-NLS-2$
				// Testing events
				var message = id+": renaming B to ZZZ";  //$NON-NLS-1$
				listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
				listenerStub.assertNames(message, "ZZZ");  //$NON-NLS-1$
				listenerStub.assertOldNames(message, "B");  //$NON-NLS-1$
				listenerStub.assertValues(message, oldValue);
				listenerStub.assertOldValues(message, oldValue);
				listenerStub.reset();
			}

			@DisplayName("#2")
			@Test
			public void renameAttributeOverwrite_2() {
				testData.renameAttribute("B", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$
				assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
			}

			@DisplayName("#3")
			@Test
			public void renameAttributeOverwrite_3() {
				testData.renameAttribute("B", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(oldValue, testData.getAttribute("ZZZ"));  //$NON-NLS-1$
			}

			@DisplayName("#4")
			@Test
			public void renameAttributeOverwrite_4() {
				testData.renameAttribute("B", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$
				listenerStub.reset();
				assertFalse(testData.renameAttribute("toto", "XXX", true));   //$NON-NLS-1$ //$NON-NLS-2$
				// Testing events
				var message = id+": renaming toto to XXX";  //$NON-NLS-1$
				listenerStub.assertEmpty(message);
				listenerStub.reset();
			}

			@DisplayName("#6")
			@Test
			public void renameAttributeOverwrite_6() {
				testData.renameAttribute("B", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$
				assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("F"));  //$NON-NLS-1$
			}

			@DisplayName("#7")
			@Test
			public void renameAttributeOverwrite_7() {
				testData.renameAttribute("B", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$
				listenerStub.reset();
				oldValue = testData.getAttribute("F");  //$NON-NLS-1$
				AttributeValue oldValue2 = testData.getAttribute("A");  //$NON-NLS-1$
		
				assertTrue(testData.renameAttribute("F", "A", true));   //$NON-NLS-1$ //$NON-NLS-2$
				// Testing events
				var message = id+": renaming F to A";  //$NON-NLS-1$
				listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL, AttributeChangeEvent.Type.RENAME);
				listenerStub.assertNames(message, "A","A");   //$NON-NLS-1$ //$NON-NLS-2$
				listenerStub.assertOldNames(message, "A", "F");   //$NON-NLS-1$ //$NON-NLS-2$
				listenerStub.assertValues(message, oldValue2, oldValue);
				listenerStub.assertOldValues(message, oldValue2, oldValue);
				listenerStub.reset();
			}

			@DisplayName("#8")
			@Test
			public void renameAttributeOverwrite_8() {
				testData.renameAttribute("B", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$
				oldValue = testData.getAttribute("F");  //$NON-NLS-1$
				AttributeValue oldValue2 = testData.getAttribute("A");  //$NON-NLS-1$
		
				assertTrue(testData.renameAttribute("F", "A", true));   //$NON-NLS-1$ //$NON-NLS-2$
				assertTrue(testData.hasAttribute("A"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("X"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("B"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Y"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("C"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("D"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("Z"));  //$NON-NLS-1$
				assertTrue(testData.hasAttribute("E"));  //$NON-NLS-1$
				assertFalse(testData.hasAttribute("F"));  //$NON-NLS-1$
			}

			@DisplayName("#9")
			@Test
			public void renameAttributeOverwrite_9() {
				testData.renameAttribute("B", "ZZZ", true);   //$NON-NLS-1$ //$NON-NLS-2$
				oldValue = testData.getAttribute("F");  //$NON-NLS-1$
				AttributeValue oldValue2 = testData.getAttribute("A");  //$NON-NLS-1$
		
				assertTrue(testData.renameAttribute("F", "A", true));   //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(oldValue, testData.getAttribute("A"));  //$NON-NLS-1$
			}
		}
	}
	
	protected class ListenerStub implements AttributeChangeListener {

		private final ArrayList<AttributeChangeEvent> eventList = new ArrayList<>();

		public void reset() {
			eventList.clear();
		}
		
		@Override
		public void onAttributeChangeEvent(AttributeChangeEvent event) {
			eventList.add(event);
		}
		
		public void assertEmpty(String message) {
			assertEquals(0,eventList.size(), message);
		}

		public void assertTypes(String message, AttributeChangeEvent.Type... desiredTypes) {
			assertEquals(desiredTypes.length, eventList.size(), message);
			for(int i=0; i<desiredTypes.length; ++i) {
				assertEquals(desiredTypes[i], eventList.get(i).getType(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertNames(String message, String... desiredNames) {
			assertEquals(desiredNames.length, eventList.size(), message);
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(desiredNames[i], eventList.get(i).getName(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertOldNames(String message, String... desiredNames) {
			assertEquals(desiredNames.length, eventList.size(), message);
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(desiredNames[i], eventList.get(i).getOldName(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertValues(String message, AttributeValue... desiredValues) {
			assertEquals(desiredValues.length, eventList.size(), message);
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(desiredValues[i], eventList.get(i).getValue(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertOldValues(String message, AttributeValue... desiredValues) {
			assertEquals(desiredValues.length, eventList.size(), message);
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(desiredValues[i], eventList.get(i).getOldValue(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertAttributes(String message, Attribute... desiredAttributes) {
			assertEquals(desiredAttributes.length, eventList.size(), message);
			for(int i=0; i<desiredAttributes.length; ++i) {
				assertEquals(desiredAttributes[i], eventList.get(i).getAttribute(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

	}

}

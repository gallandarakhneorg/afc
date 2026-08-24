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

package org.arakhne.afc.attrs.attr;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("AtributeValue")
@SuppressWarnings("all")
public class AttributeValueTest extends AbstractMathTestCase {

	protected static void assertAllGetFailed(AttributeValue attr, AttributeType type) {
		try {
			attr.getValue();
			fail("getValue: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getBoolean();
			fail("getBoolean: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getDate();
			fail("getDate: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getInteger();
			fail("getInteger: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getJavaObject();
			if (type.isBaseType())
				fail("getJavaObject: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPoint();
			fail("getPoint: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPoint3D();
			fail("getPoint3D: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPolyline();
			fail("getPolyline: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPolyline3D();
			fail("getPolyline3D: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getReal();
			fail("getReal: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getString();
			fail("getString: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeNotInitializedException exception) {
			//
		}
		catch(InvalidAttributeTypeException exception) {
			if (!attr.isObjectValue())
				fail("unexpected exception InvalidAttributeTypeException for "+type);  //$NON-NLS-1$
		}

		try {
			attr.getTimestamp();
			fail("getTimestamp: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getUUID();
			fail("getUUID: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getURI();
			fail("getURI: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getURL();
			fail("getURL: the exception InvalidAttributeTypeException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}
	}

	protected static void assertAttributeException(AttributeValue attr, String methodName) throws Exception {
		try {
			Class<? extends AttributeValue> clazz = attr.getClass();
			Method method = clazz.getMethod(methodName);
			method.invoke(attr);
			fail("the exception AttributeException was not thrown");  //$NON-NLS-1$
		}
		catch(InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof AssertionError ex0) {
				throw ex0;
			}
			if (ex instanceof AttributeException) {
				//
			}
			else {
				fail("the exception AttributeException was not thrown");  //$NON-NLS-1$
			}
		}
	}

	@DisplayName("AttributeValueImpl Constructors")
	@Nested
	public class AttributeValueImplConstructor {

		private AttributeValue attr;
	
		@BeforeEach
		public void setUp() {
			attr = new AttributeValueImpl();
		}

		@DisplayName("()")
		@Nested
		public class NoParameter {

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImpl_1() {
				assertFalse(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImpl_2() {
				assertFalse(attr.isBaseType());
			}
	
			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImpl_3() {
				assertTrue(attr.isObjectValue());
			}
	
			@DisplayName("getType")
			@Test
			public void attributeValueImpl_4() {
				assertEquals(AttributeType.OBJECT, attr.getType());
			}
			
			@DisplayName("getValue")
			@Test
			public void attributeValueImpl_5() {
				assertThrows(AttributeNotInitializedException.class, () -> attr.getValue());
			}
			
			@DisplayName("getBoolean")
			@Test
			public void attributeValueImpl_6() {
				assertThrows(AttributeNotInitializedException.class, () -> attr.getBoolean());
			}
		}

		@DisplayName("(type)")
		@Nested
		public class TypeParameter {

			private AttributeType type;
			private AttributeValue attr;
			
			private void setUp(AttributeType type) {
				this.type = type;
				this.attr = new AttributeValueImpl(type);
			}

			@DisplayName("getType")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_1(AttributeType type) throws Exception {
				setUp(type);
				assertEquals(type, attr.getType());
			}

			@DisplayName("isAssigned")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_2(AttributeType type) throws Exception {
				setUp(type);
				assertFalse(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_3(AttributeType type) throws Exception {
				setUp(type);
				assertEquals(type.isBaseType(),attr.isBaseType());
			}

			@DisplayName("isObjectType")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_4(AttributeType type) throws Exception {
				setUp(type);
				assertEquals(
						!type.isBaseType(),
						attr.isObjectValue(),
						"on type "+type);  //$NON-NLS-1$
			}

			@DisplayName("getBoolean")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_5(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_6(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_7(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getJavaObject")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_8(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertNull(attr.getJavaObject());
			}

			@DisplayName("getPoint")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_9(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPoint");  //$NON-NLS-1$
			}

			@DisplayName("getPoint3D")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_10(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPoint3D");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_11(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_12(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPolyline3D");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_13(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getReal");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_14(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getString");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_15(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getURI")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_16(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getURI");  //$NON-NLS-1$
			}

			@DisplayName("getURL")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_17(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getURL");  //$NON-NLS-1$
			}

			@DisplayName("getUUID")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_18(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getUUID");  //$NON-NLS-1$
			}

			@DisplayName("getValue")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_19(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getValue");  //$NON-NLS-1$
			}

			@DisplayName("No valid getter")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeValueImplAttributeType_20(AttributeType type) throws Exception {
				assumeFalse(type.isNullAllowed());
				setUp(type);
				assertAllGetFailed(attr, type);
			}
		}

		@DisplayName("(false)")
		@Nested
		public class FalseParameter {

			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() {
				attr = new AttributeValueImpl(false);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplBoolean_1() throws Exception {
				assertEquals(AttributeType.BOOLEAN, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplBoolean_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplBoolean_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectType")
			@Test
			public void attributeValueImplBoolean_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplBoolean_5() throws Exception {
				assertFalse((Boolean)attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplBoolean_6() throws Exception {
				assertFalse(attr.getBoolean());
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplBoolean_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplBoolean_8() throws Exception {
				assertEquals(0, attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplBoolean_9() throws Exception {
				assertEpsilonEquals(0., attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplBoolean_10() throws Exception {
				assertEquals(0, attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplBoolean_11() throws Exception {
				assertEquals(Boolean.toString(false),attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplBoolean_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplBoolean_13() throws Exception {
				assertAttributeException(attr,"getPoint");  //$NON-NLS-1$
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplBoolean_14() throws Exception {
				assertAttributeException(attr,"getPoint3D");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplBoolean_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplBoolean_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(date)")
		@Nested
		public class DateParameter {

			private Date currentDate;
			private SimpleDateFormat fmt;
			private String txt;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				currentDate = new Date();
				fmt = new SimpleDateFormat("yyyy-MM-dd");  //$NON-NLS-1$
				txt = fmt.format(currentDate);
				attr = new AttributeValueImpl(currentDate);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplDate_1() throws Exception {
				assertEquals(AttributeType.DATE, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplDate_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplDate_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplDate_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplDate_5() throws Exception {
				assertEquals(currentDate,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplDate_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplDate_7() throws Exception {
				assertEquals(currentDate,attr.getDate());
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplDate_8() throws Exception {
				assertEquals(currentDate.getTime(),attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplDate_9() throws Exception {
				assertEpsilonEquals(currentDate.getTime(),attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplDate_10() throws Exception {
				assertEquals(currentDate.getTime(),attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplDate_11() throws Exception {
				assertEquals(txt,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplDate_12() throws Exception {
				assertEquals(currentDate, attr.getJavaObject());
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplDate_13() throws Exception {
				assertFpPointEquals(currentDate.getTime(), 0, attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplDate_14() throws Exception {
				assertEquals(new Point3d(currentDate.getTime(), 0, 0), attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplDate_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplDate_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(float)")
		@Nested
		public class FloatParameter {

			private float nb;
			private String txt;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				nb = (float) Math.random();
				txt = Double.toString(nb);
				attr = new AttributeValueImpl(nb);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplFloat_1() throws Exception {
				assertEquals(AttributeType.REAL, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplFloat_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplFloat_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplFloat_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplFloat_5() throws Exception {
				assertEpsilonEquals(nb,((Number)attr.getValue()).floatValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplFloat_6() throws Exception {
				assertEquals(nb!=0f, attr.getBoolean());
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplFloat_7() throws Exception {
				assertEquals(new Date((long)nb),attr.getDate());
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplFloat_8() throws Exception {
				assertEquals((long)nb,attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplFloat_9() throws Exception {
				assertEpsilonEquals(nb,attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplFloat_10() throws Exception {
				assertEquals((long)nb,attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplFloat_11() throws Exception {
				assertEquals(txt,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplFloat_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplFloat_13() throws Exception {
				assertEquals(new Point2d(nb,0),attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplFloat_14() throws Exception {
				assertEquals(new Point3d(nb,0,0),attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplFloat_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplFloat_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(double)")
		@Nested
		public class DoubleParameter {

			private double nb;
			private String txt;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				nb = Math.random();
				txt = Double.toString(nb);
				attr = new AttributeValueImpl(nb);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplFloat_1() throws Exception {
				assertEquals(AttributeType.REAL, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplFloat_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplFloat_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplFloat_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplFloat_5() throws Exception {
				assertEpsilonEquals(nb,((Number)attr.getValue()).doubleValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplFloat_6() throws Exception {
				assertEquals(nb!=0f, attr.getBoolean());
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplFloat_7() throws Exception {
				assertEquals(new Date((long)nb),attr.getDate());
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplFloat_8() throws Exception {
				assertEquals((long)nb,attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplFloat_9() throws Exception {
				assertEpsilonEquals(nb,attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplFloat_10() throws Exception {
				assertEquals((long)nb,attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplFloat_11() throws Exception {
				assertEquals(txt,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplFloat_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplFloat_13() throws Exception {
				assertEquals(new Point2d(nb,0),attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplFloat_14() throws Exception {
				assertEquals(new Point3d(nb,0,0),attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplFloat_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplFloat_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(int)")
		@Nested
		public class IntParameter {

			private int nb;
			private String txt;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				nb = new Random().nextInt();
				txt = Integer.toString(nb);
				attr = new AttributeValueImpl(nb);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplInt_1() throws Exception {
				assertEquals(AttributeType.INTEGER, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplInt_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplInt_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplInt_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplInt_5() throws Exception {
				assertEquals(nb,((Number)attr.getValue()).intValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplInt_6() throws Exception {
				assertEquals(nb!=0, attr.getBoolean());
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplInt_7() throws Exception {
				assertEquals(new Date(nb),attr.getDate());
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplInt_8() throws Exception {
				assertEquals(nb,attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplInt_9() throws Exception {
				assertEquals(nb,(int)attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplInt_10() throws Exception {
				assertEquals(nb,attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplInt_11() throws Exception {
				assertEquals(txt,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplInt_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplInt_13() throws Exception {
				assertFpPointEquals(nb,0,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplInt_14() throws Exception {
				assertFpPointEquals(nb,0,0,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplInt_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplInt_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(long)")
		@Nested
		public class LongParameter {

			private long nb;
			private String txt;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				nb = new Random().nextLong();
				txt = Long.toString(nb);
				attr = new AttributeValueImpl(nb);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplInt_1() throws Exception {
				assertEquals(AttributeType.INTEGER, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplInt_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplInt_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplInt_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplInt_5() throws Exception {
				assertEquals(nb,((Number)attr.getValue()).longValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplInt_6() throws Exception {
				assertEquals(nb!=0, attr.getBoolean());
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplInt_7() throws Exception {
				assertEquals(new Date(nb),attr.getDate());
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplInt_8() throws Exception {
				assertEquals(nb,attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplInt_9() throws Exception {
				assertEpsilonEquals((double) nb, attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplInt_10() throws Exception {
				assertEquals(nb,attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplInt_11() throws Exception {
				assertEquals(txt,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplInt_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplInt_13() throws Exception {
				assertFpPointEquals(nb,0,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplInt_14() throws Exception {
				assertFpPointEquals(nb,0,0,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplInt_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplInt_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(Point)")
		@Nested
		public class PointParameter {

			private Point2D pt;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				pt = new Point2d(Math.random(),Math.random());
				pt3d = new Point3d(pt.getX(),pt.getY(),0);
				str = pt.getX()+";"+pt.getY();  //$NON-NLS-1$
				attr = new AttributeValueImpl(pt);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplPoint2d_1() throws Exception {
				assertEquals(AttributeType.POINT, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplPoint2d_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplPoint2d_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplPoint2d_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplPoint2d_5() throws Exception {
				assertEquals(pt,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplPoint2d_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplPoint2d_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplPoint2d_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplPoint2d_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplPoint2d_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplPoint2d_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplPoint2d_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplPoint2d_13() throws Exception {
				assertEquals(pt,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplPoint2d_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplPoint2d_15() throws Exception {
				assertArrayEquals(new Point2D[]{pt},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplPoint2d_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(double,double)")
		@Nested
		public class DoubleDoubleParameter {

			private double x;
			private double y;
			private Point2D pt;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				x = Math.random();
				y = Math.random();
				pt = new Point2d(x,y);
				pt3d = new Point3d(x,y,0);
				str = x+";"+y;  //$NON-NLS-1$
				attr = new AttributeValueImpl(x,y);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplPoint2d_1() throws Exception {
				assertEquals(AttributeType.POINT, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplPoint2d_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplPoint2d_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplPoint2d_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplPoint2d_5() throws Exception {
				assertEquals(pt,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplPoint2d_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplPoint2d_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplPoint2d_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplPoint2d_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplPoint2d_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplPoint2d_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplPoint2d_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplPoint2d_13() throws Exception {
				assertEquals(pt,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplPoint2d_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplPoint2d_15() throws Exception {
				assertArrayEquals(new Point2D[]{pt},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplPoint2d_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(Point3D)")
		@Nested
		public class Point3DParameter {

			private double x;
			private double y;
			private double z;
			private Point3D pt;
			private Point2D pt2d;
			private String str;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				x = Math.random();
				y = Math.random();
				z = Math.random();
				pt = new Point3d(x,y,z);
				pt2d = new Point2d(x,y);
				str = x+";"+y+";"+z;   //$NON-NLS-1$ //$NON-NLS-2$
				attr = new AttributeValueImpl(pt);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplPoint3d_1() throws Exception {
				assertEquals(AttributeType.POINT3D, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplPoint3d_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplPoint3d_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplPoint3d_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplPoint3d_5() throws Exception {
				assertEquals(pt,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplPoint3d_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplPoint3d_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplPoint3d_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplPoint3d_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplPoint3d_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplPoint3d_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplPoint3d_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplPoint3d_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplPoint3d_14() throws Exception {
				assertEquals(pt,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplPoint3d_15() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplPoint3d_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt},attr.getPolyline3D());
			}
		}

		@DisplayName("(double,double,double)")
		@Nested
		public class DoubleDoubleDoubleParameter {

			private double x;
			private double y;
			private double z;
			private Point3D pt;
			private Point2D pt2d;
			private String str;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				x = Math.random();
				y = Math.random();
				z = Math.random();
				pt = new Point3d(x,y,z);
				pt2d = new Point2d(x,y);
				str = x+";"+y+";"+z;   //$NON-NLS-1$ //$NON-NLS-2$
				attr = new AttributeValueImpl(x, y, z);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplPoint3d_1() throws Exception {
				assertEquals(AttributeType.POINT3D, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplPoint3d_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplPoint3d_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplPoint3d_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplPoint3d_5() throws Exception {
				assertEquals(pt,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplPoint3d_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplPoint3d_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplPoint3d_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplPoint3d_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplPoint3d_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplPoint3d_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplPoint3d_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplPoint3d_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplPoint3d_14() throws Exception {
				assertEquals(pt,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplPoint3d_15() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplPoint3d_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt},attr.getPolyline3D());
			}
		}

		@DisplayName("(string)")
		@Nested
		public class StringParameter {

			private double x;
			private Point2D pt2d;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				x = Math.random();
				pt2d = new Point2d(x,0);
				pt3d = new Point3d(x,0,0);
				str = Double.toHexString(x);
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_random_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_random_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_random_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_random_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_random_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_random_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_random_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_random_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_random_9() throws Exception {
				assertEpsilonEquals(x,attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_random_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_random_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_random_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_random_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_random_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_random_15() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_random_16() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_random_17() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(\"true\")")
		@Nested
		public class TrueStringParameter {

			private String str;
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				str = Boolean.toString(true);
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_boolean_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_boolean_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_boolean_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_boolean_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_boolean_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_boolean_6() throws Exception {
				assertTrue(attr.getBoolean());
			}

			@DisplayName("getDate7")
			@Test
			public void attributeValueImplString_boolean_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_boolean_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_boolean_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_boolean_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_boolean_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_boolean_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_boolean_13() throws Exception {
				assertAttributeException(attr,"getPoint");  //$NON-NLS-1$
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_boolean_14() throws Exception {
				assertAttributeException(attr,"getPoint3D");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_boolean_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_boolean_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(\"yyyy-MM-dd\")")
		@Nested
		public class DateStringParameter {

			private Date currentDate;
			private SimpleDateFormat format;
			private String str; 
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				currentDate = new Date();
				format = new SimpleDateFormat("yyyy-MM-dd");  //$NON-NLS-1$
				str = format.format(currentDate); 
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_Date_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_Date_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_Date_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_Date_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_Date_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_Date_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_Date_7() throws Exception {
				assertEpsilonEquals(currentDate,attr.getDate());
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_Date_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_Date_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_Date_101() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_Date_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_Date_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_Date_13() throws Exception {
				assertAttributeException(attr,"getPoint");  //$NON-NLS-1$
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_Date_14() throws Exception {
				assertAttributeException(attr,"getPoint3D");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_Date_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_Date_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(\"date\")")
		@Nested
		public class FullDateStringParameter {

			private Date currentDate;
			private String str; 
			private AttributeValue attr;
			
			@BeforeEach
			public void setUp() throws Exception {
				currentDate = new Date();
				str = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL).format(currentDate); 
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_JDate_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_JDate_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_JDate_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_JDate_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_JDate_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_JDate_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_JDate_7() throws Exception {
				assertEpsilonEquals(currentDate,attr.getDate());
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_JDate_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_JDate_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_JDate_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_JDate_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_JDate_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_JDate_13() throws Exception {
				assertAttributeException(attr,"getPoint");  //$NON-NLS-1$
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_JDate_14() throws Exception {
				assertAttributeException(attr,"getPoint3D");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_JDate_15() throws Exception {
				assertAttributeException(attr,"getPolyline");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_JDate_16() throws Exception {
				assertAttributeException(attr,"getPolyline3D");  //$NON-NLS-1$
			}
		}

		@DisplayName("(\"123\")")
		@Nested
		public class IntStringParameter {

			private int nb;
			private Point2D pt2d;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;

			@BeforeEach
			public void setUp() throws Exception {
				nb = new Random().nextInt(20000)+256;
				pt2d = new Point2d(nb,0);
				pt3d = new Point3d(nb,0,0);
				str = Integer.toString(nb);
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_Integer_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_Integer_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_Integer_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_Integer_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_Integer_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_Integer_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_Integer_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_Integer_8() throws Exception {
				assertEquals(nb,attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_Integer_9() throws Exception {
				assertEpsilonEquals(nb,attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_Integer_10() throws Exception {
				assertEquals(nb,attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_Integer_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_Integer_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_Integer_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_Integer_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_Integer_15() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_Integer_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(\"123l\")")
		@Nested
		public class LongStringParameter {

			private long nb;
			private Point2D pt2d;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;

			@BeforeEach
			public void setUp() throws Exception {
				nb = new Random().nextInt(20000)+256;
				pt2d = new Point2d(nb,0);
				pt3d = new Point3d(nb,0,0);
				str = Long.toString(nb);
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_Long_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_Long_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_Long_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_Long_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_Long_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_Long_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_Long_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_Long_8() throws Exception {
				assertEquals(nb,attr.getInteger());
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_Long_9() throws Exception {
				assertEpsilonEquals(nb,attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_Long_10() throws Exception {
				assertEquals(nb,attr.getTimestamp());
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_Long_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_Long_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_Long_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_Long_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_Long_15() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_Long_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(\"123.456\")")
		@Nested
		public class DoubleStringParameter {

			private double nb;
			private Point2D pt2d;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;

			@BeforeEach
			public void setUp() throws Exception {
				nb = Math.random()+256;
				pt2d = new Point2d(nb,0);
				pt3d = new Point3d(nb,0,0);
				str = Double.toString(nb);
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_Double_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_Double_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_Double_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_Double_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_Double_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_Double_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_Double_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_Double_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_Double_9() throws Exception {
				assertEpsilonEquals(nb,attr.getReal());
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_Double_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_Double_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_Double_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_Double_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_Double_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_Double_151() throws Exception {
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_Double_16() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d},attr.getPolyline());
			}

			@DisplayName("get")
			@Test
			public void attributeValueImplString_Double_() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(\"x,y\")")
		@Nested
		public class PointStringParameter {

			private double x;
			private double y;
			private Point2D pt2d;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;

			@BeforeEach
			public void setUp() throws Exception {
				x = Math.random()+256;
				y = Math.random()+256;
				pt2d = new Point2d(x,y);
				pt3d = new Point3d(x,y,0);
				str = x+";"+y;  //$NON-NLS-1$
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_Point2D_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_Point2D_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_Point2D_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_Point2D_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_Point2D_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_Point2D_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_Point2D_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_Point2D_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_Point2D_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_Point2D_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_Point2D_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_Point2D_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_Point2D_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_Point2D_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_Point2D_151() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_Point2D_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(\"x,y,z\")")
		@Nested
		public class Point3DStringParameter {

			private double x;
			private double y;
			private double z;
			private Point2D pt2d;
			private Point2D pt2d2;
			private Point3D pt3d;
			private String str;
			private AttributeValue attr;

			@BeforeEach
			public void setUp() throws Exception {
				x = Math.random()+256;
				y = Math.random()+256;
				z = Math.random()+256;
				pt2d = new Point2d(x,y);
				pt2d2 = new Point2d(z,0);
				pt3d = new Point3d(x,y,z);
				str = x+";"+y+";"+z;   //$NON-NLS-1$ //$NON-NLS-2$
				attr = new AttributeValueImpl(str);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplString_Point3D_1() throws Exception {
				assertEquals(AttributeType.STRING, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplString_Point3D_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplString_Point3D_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplString_Point3D_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplString_Point3D_5() throws Exception {
				assertEquals(str,attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplString_Point3D_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplString_Point3D_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplString_Point3D_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplString_Point3D_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplString_Point3D_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplString_Point3D_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplString_Point3D_12() throws Exception {
				assertAttributeException(attr,"getJavaObject");  //$NON-NLS-1$
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplString_Point3D_13() throws Exception {
				assertEquals(pt2d,attr.getPoint());
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplString_Point3D_14() throws Exception {
				assertEquals(pt3d,attr.getPoint3D());
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplString_Point3D_15() throws Exception {
				assertArrayEquals(new Point2D[]{pt2d,pt2d2},attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplString_Point3D_16() throws Exception {
				assertArrayEquals(new Point3D[]{pt3d},attr.getPolyline3D());
			}
		}

		@DisplayName("(\"x,y;x,y\")")
		@Nested
		public class PointArrayStringParameter {

			private double x1;
			private double y1;
			private double x2;
			private double y2;
			private Point2D pt1;
			private Point2D pt2;
			private Point2D[] list;
			private Point3D[] list2;
			private String str;
			private AttributeValue attr;

			@BeforeEach
			public void setUp() throws Exception {
				x1 = Math.random();
				y1 = Math.random();
				x2 = Math.random();
				y2 = Math.random();

				pt1 = new Point2d(x1,y1);
				pt2 = new Point2d(x2,y2);
				
				list = new Point2D[]{ pt1, pt2 };
				list2 = new Point3D[]{ new Point3d(x1,y1,0), new Point3d(x2,y2,0) };

				str = x1+";"+y1+";"+x2+";"+y2;    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				attr = new AttributeValueImpl(list);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplPoint2DArray_1() throws Exception {
				assertEquals(AttributeType.POLYLINE, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplPoint2DArray_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplPoint2DArray_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplPoint2DArray_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplPoint2DArray_5() throws Exception {
				assertArrayEquals(list,(Point2D[])attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplPoint2DArray_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplPoint2DArray_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplPoint2DArray_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplPoint2DArray_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplPoint2DArray_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplPoint2DArray_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplPoint2DArray_12() throws Exception {
				assertTrue(Arrays.equals(list, (Point2D[])attr.getJavaObject()));
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplPoint2DArray_13() throws Exception {
				assertAttributeException(attr,"getPoint");  //$NON-NLS-1$
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplPoint2DArray_14() throws Exception {
				assertAttributeException(attr,"getPoint3D");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplPoint2DArray_15() throws Exception {
				assertArrayEquals(list,attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplPoint2DArray_16() throws Exception {
				assertArrayEquals(list2,attr.getPolyline3D());
			}
		}

		@DisplayName("(\"x,y,z;x,y,z\")")
		@Nested
		public class Point3DArrayStringParameter {

			private double x1;
			private double y1;
			private double z1;
			private double x2;
			private double y2;
			private double z2;
			private Point3D pt1;
			private Point3D pt2;
			private Point3D[] list;
			private Point2D[] list2;
			private String str;
			private AttributeValue attr;

			@BeforeEach
			public void setUp() throws Exception {
				x1 = Math.random();
				y1 = Math.random();
				z1 = Math.random();
				x2 = Math.random();
				y2 = Math.random();
				z2 = Math.random();

				pt1 = new Point3d(x1,y1,z1);
				pt2 = new Point3d(x2,y2,z2);
				
				list = new Point3D[]{ pt1, pt2 };
				list2 = new Point2D[]{ new Point2d(x1,y1), new Point2d(x2,y2) };

				str = x1+";"+y1+";"+z1+";"+x2+";"+y2+";"+z2;

				attr = new AttributeValueImpl(list);
			}

			@DisplayName("getType")
			@Test
			public void attributeValueImplPoint3DArray_1() throws Exception {
				assertEquals(AttributeType.POLYLINE3D, attr.getType());
			}

			@DisplayName("isAssigned")
			@Test
			public void attributeValueImplPoint3DArray_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("isBaseType")
			@Test
			public void attributeValueImplPoint3DArray_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("isObjectValue")
			@Test
			public void attributeValueImplPoint3DArray_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("getValue")
			@Test
			public void attributeValueImplPoint3DArray_5() throws Exception {
				assertArrayEquals(list,(Point3D[])attr.getValue());
			}

			@DisplayName("getBoolean")
			@Test
			public void attributeValueImplPoint3DArray_6() throws Exception {
				assertAttributeException(attr,"getBoolean");  //$NON-NLS-1$
			}

			@DisplayName("getDate")
			@Test
			public void attributeValueImplPoint3DArray_7() throws Exception {
				assertAttributeException(attr,"getDate");  //$NON-NLS-1$
			}

			@DisplayName("getInteger")
			@Test
			public void attributeValueImplPoint3DArray_8() throws Exception {
				assertAttributeException(attr,"getInteger");  //$NON-NLS-1$
			}

			@DisplayName("getReal")
			@Test
			public void attributeValueImplPoint3DArray_9() throws Exception {
				assertAttributeException(attr,"getReal");  //$NON-NLS-1$
			}

			@DisplayName("getTimestamp")
			@Test
			public void attributeValueImplPoint3DArray_10() throws Exception {
				assertAttributeException(attr,"getTimestamp");  //$NON-NLS-1$
			}

			@DisplayName("getString")
			@Test
			public void attributeValueImplPoint3DArray_11() throws Exception {
				assertEquals(str,attr.getString());
			}

			@DisplayName("getJavaObject")
			@Test
			public void attributeValueImplPoint3DArray_12() throws Exception {
				assertTrue(Arrays.equals(list, (Point3D[])attr.getJavaObject()));
			}

			@DisplayName("getPoint")
			@Test
			public void attributeValueImplPoint3DArray_13() throws Exception {
				assertAttributeException(attr,"getPoint");  //$NON-NLS-1$
			}

			@DisplayName("getPoint3D")
			@Test
			public void attributeValueImplPoint3DArray_14() throws Exception {
				assertAttributeException(attr,"getPoint3D");  //$NON-NLS-1$
			}

			@DisplayName("getPolyline")
			@Test
			public void attributeValueImplPoint3DArray_15() throws Exception {
				assertArrayEquals(list2,attr.getPolyline());
			}

			@DisplayName("getPolyline3D")
			@Test
			public void attributeValueImplPoint3DArray_16() throws Exception {
				assertArrayEquals(list,attr.getPolyline3D());
			}
		}
	}

	@DisplayName("cast")
	@Nested
	public class Cast {

		@DisplayName("boolean -> boolean")
		@Test
		public void boolean_boolean() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("boolean -> date")
		@Test
		public void boolean_date() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("boolean -> integer")
		@Test
		public void boolean_integer() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getInteger(), msg);
		}

		@DisplayName("boolean -> object")
		@Test
		public void boolean_object() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getJavaObject(), msg);
		}

		@DisplayName("boolean -> point")
		@Test
		public void boolean_point() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("boolean -> point3d")
		@Test
		public void boolean_point3d() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("boolean -> polyline")
		@Test
		public void boolean_polyline() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("boolean -> polyline3d")
		@Test
		public void boolean_polyline3d() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("boolean -> real")
		@Test
		public void boolean_real() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("boolean -> string")
		@Test
		public void boolean_string() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(Boolean.toString((Boolean)AttributeType.BOOLEAN.getDefaultValue()), attr2.getValue(), msg);
			assertEquals(Boolean.toString((Boolean)AttributeType.BOOLEAN.getDefaultValue()), attr2.getString(), msg);
		}

		@DisplayName("boolean -> timestamp")
		@Test
		public void boolean_timestamp() throws AttributeException {
			var source = AttributeType.BOOLEAN;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("date -> boolean")
		@Test
		public void date_boolean() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("date -> date")
		@Test
		public void date_date() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("date -> integer")
		@Test
		public void date_integer() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			var dt = attr2.getDate();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(dt.getTime(), attr2.getValue(), msg);
			assertEquals(dt.getTime(), attr2.getInteger(), msg);
		}

		@DisplayName("date -> object")
		@Test
		public void date_object() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertNotNull(attr2.getValue(), msg);
			assertNotNull(attr2.getJavaObject(), msg);
		}

		@DisplayName("date -> point")
		@Test
		public void date_point() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			Point2D pt = attr2.getPoint();
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(pt, attr2.getValue(), msg);
			assertEquals(pt, attr2.getPoint(), msg);
		}

		@DisplayName("date -> point3d")
		@Test
		public void date_point3d() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			Point3D pt3 = attr2.getPoint3D();
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(pt3, attr2.getValue(), msg);
			assertEquals(pt3, attr2.getPoint3D(), msg);
		}

		@DisplayName("date -> polyline")
		@Test
		public void date_polyline() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("date -> polyline3d")
		@Test
		public void date_polyline3d() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("date -> real")
		@Test
		public void date_real() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			var dt = attr2.getDate();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(dt.getTime(), ((Double)attr2.getValue()).longValue(), msg);
			assertEquals(dt.getTime(), (long)attr2.getReal(), msg);
		}

		@DisplayName("date -> string")
		@Test
		public void date_string() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var dt = (Date)source.getDefaultValue();
			var format = new SimpleDateFormat("yyyy-MM-dd");  //$NON-NLS-1$
			var str = format.format(dt);
			assertEquals(str, attr2.getValue(), msg);
			assertEquals(str, attr2.getString(), msg);
		}

		@DisplayName("date -> timestamp")
		@Test
		public void date_timestamp() throws AttributeException {
			var source = AttributeType.DATE;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("integer -> boolean")
		@Test
		public void integer_boolean() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("integer -> date")
		@Test
		public void integer_date() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("integer -> integer")
		@Test
		public void integer_integer() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(),attr2.getInteger(), msg);
		}

		@DisplayName("integer -> object")
		@Test
		public void integer_object() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertNull(attr2.getValue(), msg);
			assertNull(attr2.getJavaObject(), msg);
		}

		@DisplayName("integer -> point")
		@Test
		public void integer_point() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("integer -> point3d")
		@Test
		public void integer_point3d() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("integer -> polyline")
		@Test
		public void integer_polyline() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("integer -> polyline3d")
		@Test
		public void integer_polyline3d() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("integer -> real")
		@Test
		public void integer_real() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("integer -> string")
		@Test
		public void integer_string() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals("0", attr2.getValue(), msg);  //$NON-NLS-1$
			assertEquals("0", attr2.getString(), msg);  //$NON-NLS-1$
		}

		@DisplayName("integer -> timestamp")
		@Test
		public void integer_timestamp() throws AttributeException {
			var source = AttributeType.INTEGER;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("object -> boolean")
		@Test
		public void object_boolean() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("object -> date")
		@Test
		public void object_date() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("object -> integer")
		@Test
		public void object_integer() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
		}

		@DisplayName("object -> object")
		@Test
		public void object_object() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertNull(attr2.getValue(), msg);
			assertNull(attr2.getJavaObject(), msg);
		}

		@DisplayName("object -> point")
		@Test
		public void object_point() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("object -> point3d")
		@Test
		public void object_point3d() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("object -> polyline")
		@Test
		public void object_polyline() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("object -> polyline3d")
		@Test
		public void object_polyline3d() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("object -> real")
		@Test
		public void object_real() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("object -> string")
		@Test
		public void object_string() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getString(), msg);
		}

		@DisplayName("object -> timestamp")
		@Test
		public void object_timestamp() throws AttributeException {
			var source = AttributeType.OBJECT;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("point -> boolean")
		@Test
		public void point_boolean() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("point -> date")
		@Test
		public void point_date() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("point -> integer")
		@Test
		public void point_integer() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(),attr2.getInteger(), msg);
		}

		@DisplayName("point -> object")
		@Test
		public void point_object() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(new Point2d(), attr2.getValue(), msg);
			assertEquals(new Point2d(), attr2.getJavaObject(), msg);
		}

		@DisplayName("point -> point")
		@Test
		public void point_point() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("point -> point3d")
		@Test
		public void point_point3d() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("point -> polyline")
		@Test
		public void point_polyline() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals(new Point2D[] {(Point2D)source.getDefaultValue()}, (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals(new Point2D[] {(Point2D)source.getDefaultValue()}, attr2.getPolyline(), msg);
		}

		@DisplayName("point -> polyline3d")
		@Test
		public void point_polyline3d() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertArrayEquals(new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, (Point3D[])attr2.getValue(), msg);
			assertArrayEquals(new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, attr2.getPolyline3D(), msg);
		}

		@DisplayName("point -> real")
		@Test
		public void point_real() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("point -> string")
		@Test
		public void point_string() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var pt2d = (Point2D)source.getDefaultValue();
			var str = pt2d.getX()+";"+pt2d.getY();  //$NON-NLS-1$
			assertEquals(str, attr2.getValue(), msg);
			assertEquals(str, attr2.getString(), msg);
		}

		@DisplayName("point -> timestamp")
		@Test
		public void point_timestamp() throws AttributeException {
			var source = AttributeType.POINT;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("point3d -> boolean")
		@Test
		public void point3d_boolean() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("point3d -> date")
		@Test
		public void point3d_date() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("point3d -> integer")
		@Test
		public void point3d_integer() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(),attr2.getInteger(), msg);
		}

		@DisplayName("point3d -> object")
		@Test
		public void point3d_object() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(new Point3d(), attr2.getValue(), msg);
			assertEquals(new Point3d(), attr2.getJavaObject(), msg);
		}

		@DisplayName("point3d -> point")
		@Test
		public void point3d_point() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("point3d -> point3d")
		@Test
		public void point3d_point3d() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("point3d -> polyline")
		@Test
		public void point3d_polyline() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertArrayEquals(new Point2D[] {(Point2D)AttributeType.POINT.getDefaultValue()}, (Point2D[])attr2.getValue(), msg);
			assertArrayEquals(new Point2D[] {(Point2D)AttributeType.POINT.getDefaultValue()}, attr2.getPolyline(), msg);
		}

		@DisplayName("point3d -> polyline3d")
		@Test
		public void point3d_polyline3d() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertArrayEquals(new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, (Point3D[])attr2.getValue(), msg);
			assertArrayEquals(new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, attr2.getPolyline3D(), msg);
		}

		@DisplayName("point3d -> real")
		@Test
		public void point3d_real() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("point3d -> string")
		@Test
		public void point3d_string() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var pt3d = (Point3D)source.getDefaultValue();
			var str = pt3d.getX()+";"+pt3d.getY()+";"+pt3d.getZ();   //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(str, attr2.getValue(), msg);
			assertEquals(str, attr2.getString(), msg);
		}

		@DisplayName("point3d -> timetamp")
		@Test
		public void point3d_timestamp() throws AttributeException {
			var source = AttributeType.POINT3D;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("polyline -> boolean")
		@Test
		public void polyline_boolean() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("polyline -> date")
		@Test
		public void polyline_date() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("polyline -> integer")
		@Test
		public void polyline_integer() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(),attr2.getInteger(), msg);
		}

		@DisplayName("polyline -> object")
		@Test
		public void polyline_object() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(Arrays.equals(new Point2d[0], (Point2D[])attr2.getValue()), msg);
			assertTrue(Arrays.equals(new Point2d[0], (Point2D[])attr2.getJavaObject()), msg);
		}

		@DisplayName("polyline -> point")
		@Test
		public void polyline_point() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("polyline -> point3d")
		@Test
		public void polyline_point3d() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("polyline -> polyline")
		@Test
		public void polyline_polyline() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])source.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])source.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("polyline -> polyline3d")
		@Test
		public void polyline_polyline3d() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("polyline -> real")
		@Test
		public void polyline_real() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("polyline -> string")
		@Test
		public void polyline_string() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var str = "";  //$NON-NLS-1$
			assertEquals(str, attr2.getValue(), msg);
			assertEquals(str, attr2.getString(), msg);
		}

		@DisplayName("polyline -> timestamp")
		@Test
		public void polyline_timestamp() throws AttributeException {
			var source = AttributeType.POLYLINE;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("polyline3d -> boolean")
		@Test
		public void polyline3d_boolean() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("polyline3d -> date")
		@Test
		public void polyline3d_date() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("polyline3d -> integer")
		@Test
		public void polyline3d_integer() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(),attr2.getInteger(), msg);
		}

		@DisplayName("polyline3d -> object")
		@Test
		public void polyline3d_object() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(Arrays.equals(new Point3d[0], (Point3D[])attr2.getValue()), msg);
			assertTrue(Arrays.equals(new Point3d[0], (Point3D[])attr2.getJavaObject()), msg);
		}

		@DisplayName("polyline3d -> point")
		@Test
		public void polyline3d_point() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("polyline3d -> point3d")
		@Test
		public void polyline3d_point3d() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("polyline3d -> polyline")
		@Test
		public void polyline3d_polyline() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])AttributeType.POLYLINE.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])AttributeType.POLYLINE.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("polyline3d -> polyline3d")
		@Test
		public void polyline3d_polyline3d() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("polyline3d -> real")
		@Test
		public void polyline3d_real() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("polyline3d -> string")
		@Test
		public void polyline3d_string() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var str = "";  //$NON-NLS-1$
			assertEquals(str, attr2.getValue(), msg);
			assertEquals(str, attr2.getString(), msg);
		}

		@DisplayName("polyline3d -> timestamp")
		@Test
		public void polyline3d_timestamp() throws AttributeException {
			var source = AttributeType.POLYLINE3D;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("real -> boolean")
		@Test
		public void real_boolean() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("real -> date")
		@Test
		public void real_date() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("real -> integer")
		@Test
		public void real_integer() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(),attr2.getInteger(), msg);
		}

		@DisplayName("real -> object")
		@Test
		public void real_object() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertNull(attr2.getValue(), msg);
			assertNull(attr2.getJavaObject(), msg);
		}

		@DisplayName("real -> point")
		@Test
		public void real_point() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("real -> point3d")
		@Test
		public void real_point3d() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("real -> polyline")
		@Test
		public void real_polyline() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("real -> polyline3d")
		@Test
		public void real_polyline3d() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("real -> real")
		@Test
		public void real_real() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("real -> string")
		@Test
		public void real_string() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(Double.toString(0), attr2.getValue(), msg);
			assertEquals(Double.toString(0), attr2.getString(), msg);
		}

		@DisplayName("real -> timestamp")
		@Test
		public void real_timestamp() throws AttributeException {
			var source = AttributeType.REAL;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("string -> boolean")
		@Test
		public void string_boolean() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getBoolean(), msg);
		}

		@DisplayName("string -> date")
		@Test
		public void string_date() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("string -> integer")
		@Test
		public void string_integer() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(),attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(),attr2.getInteger(), msg);
		}

		@DisplayName("string -> object")
		@Test
		public void string_object() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertNull(attr2.getValue(), msg);
			assertNull(attr2.getJavaObject(), msg);
		}

		@DisplayName("string -> point")
		@Test
		public void string_point() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint(), msg);
		}

		@DisplayName("string -> point3d")
		@Test
		public void string_point3d() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getPoint3D(), msg);
		}

		@DisplayName("string -> polyline")
		@Test
		public void string_polyline() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("string -> polyline3d")
		@Test
		public void string_polyline3d() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("string -> real")
		@Test
		public void string_real() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(target.getDefaultValue(), attr2.getValue(), msg);
			assertEquals(target.getDefaultValue(), attr2.getReal(), msg);
		}

		@DisplayName("string -> string")
		@Test
		public void string_string() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var str = "";  //$NON-NLS-1$
			assertEquals(str, attr2.getValue(), msg);
			assertEquals(str, attr2.getString(), msg);
		}

		@DisplayName("string -> timestamp")
		@Test
		public void string_timestamp() throws AttributeException {
			var source = AttributeType.STRING;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("timestamp -> boolean")
		@Test
		public void timestamp_boolean() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.BOOLEAN;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(true, attr2.getValue(), msg);
			assertEquals(true, attr2.getBoolean(), msg);
		}

		@DisplayName("timestamp -> date")
		@Test
		public void timestamp_date() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.DATE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue(((Date)attr2.getValue()).getTime()<=time, msg);
			assertTrue(attr2.getDate().getTime()<=time, msg);
		}

		@DisplayName("timestamp -> integer")
		@Test
		public void timestamp_integer() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.INTEGER;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var time = System.currentTimeMillis();
			assertTrue((Long)attr2.getValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}

		@DisplayName("timestamp -> object")
		@Test
		public void timestamp_object() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.OBJECT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertNull(attr2.getValue(), msg);
			assertNull(attr2.getJavaObject(), msg);
		}

		@DisplayName("timestamp -> point")
		@Test
		public void timestamp_point() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.POINT;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			var time = attr2.getTimestamp();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(new Point2d(time,0), attr2.getValue(), msg);
			assertEquals(new Point2d(time,0), attr2.getPoint(), msg);
		}

		@DisplayName("timestamp -> point3d")
		@Test
		public void timestamp_point3d() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.POINT3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			var time = attr2.getTimestamp();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(new Point3d(time,0,0), attr2.getValue(), msg);
			assertEquals(new Point3d(time,0,0), attr2.getPoint3D(), msg);
		}

		@DisplayName("timestamp -> polyline")
		@Test
		public void timestamp_polyline() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.POLYLINE;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point2D[])target.getDefaultValue(), attr2.getPolyline(), msg);
		}

		@DisplayName("timestamp -> polyline3d")
		@Test
		public void timestamp_polyline3d() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.POLYLINE3D;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue(), msg);
			assertEpsilonEquals((Point3D[])target.getDefaultValue(), attr2.getPolyline3D(), msg);
		}

		@DisplayName("timestamp -> real")
		@Test
		public void timestamp_real() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.REAL;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			var time = attr2.getTimestamp();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertEquals(Double.valueOf(time), attr2.getValue(), msg);
			assertEpsilonEquals(Long.valueOf(time).doubleValue(), attr2.getReal(), msg);
		}

		@DisplayName("timestamp -> timestamp #1")
		@Test
		public void timestamp_timestamp_1() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.STRING;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();
			var time = attr2.getTimestamp();
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //$NON-NLS-1$
			var str = format.format(new Date(time));
			assertEquals(str, attr2.getValue(), msg);
			assertEquals(str, attr2.getString(), msg);
		}

		@DisplayName("timestamp -> timestamp #2")
		@Test
		public void timestamp_timestamp_2() throws AttributeException {
			var source = AttributeType.TIMESTAMP;
			var target = AttributeType.TIMESTAMP;
			var msg = "from '"+source.toString()+"' to '"+target.toString()+"'";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			var attr1 = new AttributeValueImpl(source);
			var attr2 = new AttributeValueImpl(source);
			attr2.setToDefault();		
			attr1.cast(target);
			assertFalse(attr1.isAssigned(), msg);
			attr2.cast(target);
			assertTrue(attr2.isAssigned(), msg);
			assertTrue(attr2.getValue() instanceof Timestamp, msg);
			var time = System.currentTimeMillis();
			assertTrue(((Number)attr2.getValue()).longValue()<=time, msg);
			assertTrue(attr2.getTimestamp()<=time, msg);
		}
	}
	
	@DisplayName("equals(Object)")
	@Nested
	public class ObjectTest {

		private AttributeValueImpl attr;

		@BeforeEach
		public void setUp() {
			attr = new AttributeValueImpl();
		}

		@DisplayName("attr(true) == attr(true)")
		@Test
		public void testEquals_1() {
			attr.setBoolean(true);
			assertTrue(attr.equals(new AttributeValueImpl(true)));
		}

		@DisplayName("attr(true) == attr(false)")
		@Test
		public void testEquals_2() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl(false)));
		}

		@DisplayName("attr(true) == true")
		@Test
		public void testEquals_3() {
			attr.setBoolean(true);
			assertTrue(attr.equals(true));
		}

		@DisplayName("attr(true) == false")
		@Test
		public void testEquals_4() {
			attr.setBoolean(true);
			assertFalse(attr.equals(false));
		}

		@DisplayName("attr(true) == attr(\"true\")")
		@Test
		public void testEquals_5() {
			attr.setBoolean(true);
			assertTrue(attr.equals(new AttributeValueImpl("true")));  //$NON-NLS-1$
		}

		@DisplayName("attr(true) == attr(\"false\")")
		@Test
		public void testEquals_6() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl("false")));  //$NON-NLS-1$
		}

		@DisplayName("attr(true) == \"true\"")
		@Test
		public void testEquals_7() {
			attr.setBoolean(true);
			assertTrue(attr.equals("true"));  //$NON-NLS-1$
		}

		@DisplayName("attr(true) == \"false\"")
		@Test
		public void testEquals_8() {
			attr.setBoolean(true);
			assertFalse(attr.equals("false"));  //$NON-NLS-1$
		}

		@DisplayName("attr(true) == attr(1.0)")
		@Test
		public void testEquals_9() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl(1.)));
		}

		@DisplayName("attr(true) == attr(\"1.0\")")
		@Test
		public void testEquals_10() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl("1.")));  //$NON-NLS-1$
		}

		@DisplayName("attr(true) == 1.0")
		@Test
		public void testEquals_11() {
			attr.setBoolean(true);
			assertFalse(attr.equals(1.));
		}

		@DisplayName("attr(true) == \"toto\"")
		@Test
		public void testEquals_12() {
			attr.setBoolean(true);
			assertFalse(attr.equals("toto"));  //$NON-NLS-1$
		}

		@DisplayName("attr(false) == attr(true)")
		@Test
		public void testEquals_13() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl(true)));
		}

		@DisplayName("attr(false) == attr(false)")
		@Test
		public void testEquals_14() {
			attr.setBoolean(false);
			assertTrue(attr.equals(new AttributeValueImpl(false)));
		}

		@DisplayName("attr(false) == true")
		@Test
		public void testEquals_15() {
			attr.setBoolean(false);
			assertFalse(attr.equals(true));
		}

		@DisplayName("attr(false) == false")
		@Test
		public void testEquals_16() {
			attr.setBoolean(false);
			assertTrue(attr.equals(false));
		}

		@DisplayName("attr(false) == attr(\"true\")")
		@Test
		public void testEquals_17() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl("true")));  //$NON-NLS-1$
		}

		@DisplayName("attr(false) == attr(\"false\")")
		@Test
		public void testEquals_18() {
			attr.setBoolean(false);
			assertTrue(attr.equals(new AttributeValueImpl("false")));  //$NON-NLS-1$
		}

		@DisplayName("attr(false) == \"true\"")
		@Test
		public void testEquals_19() {
			attr.setBoolean(false);
			assertFalse(attr.equals("true"));  //$NON-NLS-1$
		}

		@DisplayName("attr(false) == \"false\"")
		@Test
		public void testEquals_20() {
			attr.setBoolean(false);
			assertTrue(attr.equals("false"));  //$NON-NLS-1$
		}

		@DisplayName("attr(false) == attr(1.0)")
		@Test
		public void testEquals_21() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl(1.)));
		}

		@DisplayName("attr(false) == attr(\"1.0\")")
		@Test
		public void testEquals_22() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl("1.")));  //$NON-NLS-1$
		}

		@DisplayName("attr(false) == 1.0")
		@Test
		public void testEquals_23() {
			attr.setBoolean(false);
			assertFalse(attr.equals(1.));
		}

		@DisplayName("attr(false) == \"toto\"")
		@Test
		public void testEquals_24() {
			attr.setBoolean(false);
			assertFalse(attr.equals("toto"));  //$NON-NLS-1$
		}
	}
	
	@DisplayName("parse")
	@Nested
	public class Parse {

		@DisplayName("127.0.0.1")
		@Test
		public void parse_1() {
			var v = AttributeValueImpl.parse("127.0.0.1");  //$NON-NLS-1$
			assertSame(AttributeType.INET_ADDRESS, v.getType());
		}

		@DisplayName("localhost")
		@Test
		public void parse_2() {
			var v = AttributeValueImpl.parse("localhost");  //$NON-NLS-1$
			assertSame(AttributeType.INET_ADDRESS, v.getType());
		}

		@DisplayName("java.lang.String")
		@Test
		public void parse_3() {
			var v = AttributeValueImpl.parse("java.lang.String");  //$NON-NLS-1$
			assertSame(AttributeType.TYPE, v.getType());
		}

		@DisplayName("org.arakhne.afc.attrs.attr.AttributeType.ENUMERATION")
		@Test
		public void parse_4() {
			var v = AttributeValueImpl.parse(AttributeType.class.getName()+"."+AttributeType.ENUMERATION.name());  //$NON-NLS-1$
			assertSame(AttributeType.ENUMERATION, v.getType());
		}

		@DisplayName("3eade434-b267-4ffa-a574-2e2cbff0151a")
		@Test
		public void parse_5() {
			var v = AttributeValueImpl.parse("3eade434-b267-4ffa-a574-2e2cbff0151a");  //$NON-NLS-1$
			assertSame(AttributeType.UUID, v.getType());
		}

		@DisplayName("134")
		@Test
		public void parse_6() {
			var v = AttributeValueImpl.parse("134");  //$NON-NLS-1$
			assertSame(AttributeType.INTEGER, v.getType());
		}

		@DisplayName("-134")
		@Test
		public void parse_7() {
			var v = AttributeValueImpl.parse("-134");  //$NON-NLS-1$
			assertSame(AttributeType.INTEGER, v.getType());
		}

		@DisplayName("134e34")
		@Test
		public void parse_8() {
			var v = AttributeValueImpl.parse("134e34");  //$NON-NLS-1$
			assertSame(AttributeType.REAL, v.getType());
		}

		@DisplayName("-134.5")
		@Test
		public void parse_9() {
			var v = AttributeValueImpl.parse("-134.5");  //$NON-NLS-1$
			assertSame(AttributeType.REAL, v.getType());
		}

		@DisplayName("2012-11-30 18:22:34")
		@Test
		public void parse_10() {
			var v = AttributeValueImpl.parse("2012-11-30 18:22:34");  //$NON-NLS-1$
			assertSame(AttributeType.DATE, v.getType());
		}

		@DisplayName("Fri, 30 Nov 2012 18:22:42 +0100")
		@Test
		public void parse_11() {
			var v = AttributeValueImpl.parse("Fri, 30 Nov 2012 18:22:42 +0100");  //$NON-NLS-1$
			assertSame(AttributeType.DATE, v.getType());
		}

		@DisplayName("True")
		@Test
		public void parse_12() {
			var v = AttributeValueImpl.parse("True");  //$NON-NLS-1$
			assertSame(AttributeType.BOOLEAN, v.getType());
		}

		@DisplayName("False")
		@Test
		public void parse_13() {
			var v = AttributeValueImpl.parse("False");  //$NON-NLS-1$
			assertSame(AttributeType.BOOLEAN, v.getType());
		}

		@DisplayName("TrUe")
		@Test
		public void parse_14() {
			var v = AttributeValueImpl.parse("TrUe");  //$NON-NLS-1$
			assertSame(AttributeType.BOOLEAN, v.getType());
		}

		@DisplayName("http://www.multiagent.fr")
		@Test
		public void parse_15() {
			var v = AttributeValueImpl.parse("http://www.multiagent.fr");  //$NON-NLS-1$
			assertSame(AttributeType.URL, v.getType());
		}

		@DisplayName("mailto:stephane.galland@utbm.fr")
		@Test
		public void parse_16() {
			var v = AttributeValueImpl.parse("mailto:stephane.galland@utbm.fr");  //$NON-NLS-1$
			assertSame(AttributeType.URL, v.getType());
		}

		@DisplayName("urn:isbn:096139210x")
		@Test
		public void parse_17() {
			var v = AttributeValueImpl.parse("urn:isbn:096139210x");  //$NON-NLS-1$
			assertSame(AttributeType.URI, v.getType());
		}

		@DisplayName("1;2;3;4;5;6;7;8;9")
		@Test
		public void parse_18() {
			var v = AttributeValueImpl.parse("1;2;3;4;5;6;7;8;9");  //$NON-NLS-1$
			assertSame(AttributeType.POLYLINE3D, v.getType());
		}

		@DisplayName("1;2;3;4;5;6;7;8")
		@Test
		public void parse_19() {
			var v = AttributeValueImpl.parse("1;2;3;4;5;6;7;8");  //$NON-NLS-1$
			assertSame(AttributeType.POLYLINE, v.getType());
		}

		@DisplayName("1;2;300")
		@Test
		public void parse_20() {
			var v = AttributeValueImpl.parse("1;2;300");  //$NON-NLS-1$
			assertSame(AttributeType.POINT3D, v.getType());
		}

		@DisplayName("1;2")
		@Test
		public void parse_21() {
			var v = AttributeValueImpl.parse("1;2");  //$NON-NLS-1$
			assertSame(AttributeType.POINT, v.getType());
		}

		@DisplayName("blablabla")
		@Test
		public void parse_22() {
			var v = AttributeValueImpl.parse("blablabla");  //$NON-NLS-1$
			assertSame(AttributeType.STRING, v.getType());
		}
	}

}

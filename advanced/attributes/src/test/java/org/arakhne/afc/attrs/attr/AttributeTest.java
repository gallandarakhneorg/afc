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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Attribute")
@SuppressWarnings("all")
public class AttributeTest extends AbstractTestCase {

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
			fail("getBoolean: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getDate();
			fail("getDate: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getInteger();
			fail("getInteger: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getJavaObject();
			if (type.isBaseType())
				fail("getJavaObject: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeNotInitializedException exception) {
			// expected case
		}
		catch(InvalidAttributeTypeException exception) {
			if (attr.isObjectValue())
				fail("getJavaObject: unexpected exception InvalidAttributeTypeException for "+type);  //$NON-NLS-1$
		}

		try {
			attr.getPoint();
			fail("getPoint: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPoint3D();
			fail("getPoint3D: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPoint3D();
			fail("getPoint3D: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPolyline3D();
			fail("getPolyline3D: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getReal();
			fail("getReal: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getString();
			fail("getString: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeNotInitializedException exception) {
			// expected case
		}
		catch(InvalidAttributeTypeException exception) {
			if (!attr.isObjectValue())
				fail("getString: unexpected exception InvalidAttributeTypeException for "+type);  //$NON-NLS-1$
		}

		try {
			attr.getTimestamp();
			fail("getTimestamp: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getURI();
			fail("getURI: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getURL();
			fail("getURL: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getUUID();
			fail("getUUID: the exception AttributeNotInitializedException was not thrown for "+type);  //$NON-NLS-1$
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
			if (ex instanceof AttributeException) {
				//
			}
			else {
				fail("the exception AttributeException was not thrown");  //$NON-NLS-1$
			}
		}
	}

	@DisplayName("AttributeImpl Constructor")
	@Nested
	public class AttributeImplConstructor {

		@DisplayName("()")
		@Nested
		public class WithoutArgument {

			private Attribute attr;

			@BeforeEach
			public void setUp() {
				attr = new AttributeImpl();
			}

			@DisplayName("attributeImpl_1")
			@Test
			public void attributeImpl_1() {
				assertFalse(attr.isAssigned());
			}

			@DisplayName("attributeImpl_2")
			@Test
			public void attributeImpl_2() {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImpl_3")
			@Test
			public void attributeImpl_3() {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImpl_4")
			@Test
			public void attributeImpl_4() {
				assertEquals(AttributeType.OBJECT, attr.getType());
			}

			@DisplayName("attributeImpl_5")
			@Test
			public void attributeImpl_5() {
				assertThrows(AttributeNotInitializedException.class, () -> attr.getValue());
			}

			@DisplayName("attributeImpl_6")
			@Test
			public void attributeImpl_6() {
				assertThrows(AttributeNotInitializedException.class, () -> attr.getBoolean());
			}
		}

		@DisplayName("(boolean)")
		@Nested
		public class WithBoolean {

			private Attribute attr;

			@BeforeEach
			public void setUp() {
				attr = new AttributeImpl(randomString(), false);
			}

			@DisplayName("attributeImplBoolean_1")
			@Test
			public void attributeImplBoolean_1() throws Exception {
				assertEquals(AttributeType.BOOLEAN, attr.getType());
			}

			@DisplayName("attributeImplBoolean_2")
			@Test
			public void attributeImplBoolean_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplBoolean_3")
			@Test
			public void attributeImplBoolean_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("attributeImplBoolean_4")
			@Test
			public void attributeImplBoolean_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("attributeImplBoolean_5")
			@Test
			public void attributeImplBoolean_5() throws Exception {
				assertFalse((Boolean) attr.getValue());
			}

			@DisplayName("attributeImplBoolean_6")
			@Test
			public void attributeImplBoolean_6() throws Exception {
				assertFalse(attr.getBoolean());
			}

			@DisplayName("attributeImplBoolean_7")
			@Test
			public void attributeImplBoolean_7() throws Exception {
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplBoolean_8")
			@Test
			public void attributeImplBoolean_8() throws Exception {
				assertEquals(0, attr.getInteger());
			}

			@DisplayName("attributeImplBoolean_9")
			@Test
			public void attributeImplBoolean_9() throws Exception {
				assertEpsilonEquals(0., attr.getReal());
			}

			@DisplayName("attributeImplBoolean_10")
			@Test
			public void attributeImplBoolean_10() throws Exception {
				assertEquals(0, attr.getTimestamp());
			}

			@DisplayName("attributeImplBoolean_11")
			@Test
			public void attributeImplBoolean_11() throws Exception {
				assertEquals(Boolean.toString(false), attr.getString());
			}

			@DisplayName("attributeImplBoolean_12")
			@Test
			public void attributeImplBoolean_12() throws Exception {
				assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplBoolean_13")
			@Test
			public void attributeImplBoolean_13() throws Exception {
				assertAttributeException(attr, "getPoint"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplBoolean_14")
			@Test
			public void attributeImplBoolean_14() throws Exception {
				assertAttributeException(attr, "getPoint3D"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplBoolean_15")
			@Test
			public void attributeImplBoolean_15() throws Exception {
				assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplBoolean_16")
			@Test
			public void attributeImplBoolean_16() throws Exception {
				assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
			}
		}

		@DisplayName("(Date)")
		@Nested
		public class WithDate {

			private Date currentDate;
			private SimpleDateFormat fmt;
			private String txt;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				currentDate = new Date();
				fmt = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
				txt = fmt.format(currentDate);
				attr = new AttributeImpl(randomString(), currentDate);
			}

			@DisplayName("attributeImplDate_1")
			@Test
			public void attributeImplDate_1() throws Exception {
				assertEquals(AttributeType.DATE, attr.getType());
			}

			@DisplayName("attributeImplDate_2")
			@Test
			public void attributeImplDate_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplDate_3")
			@Test
			public void attributeImplDate_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImplDate_4")
			@Test
			public void attributeImplDate_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImplDate_5")
			@Test
			public void attributeImplDate_5() throws Exception {
				assertEquals(currentDate, attr.getValue());
			}

			@DisplayName("attributeImplDate_6")
			@Test
			public void attributeImplDate_6() throws Exception {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDate_7")
			@Test
			public void attributeImplDate_7() throws Exception {
				assertEquals(currentDate, attr.getDate());
			}

			@DisplayName("attributeImplDate_8")
			@Test
			public void attributeImplDate_8() throws Exception {
				assertEquals(currentDate.getTime(), attr.getInteger());
			}

			@DisplayName("attributeImplDate_9")
			@Test
			public void attributeImplDate_9() throws Exception {
				assertEpsilonEquals(currentDate.getTime(), attr.getReal());
			}

			@DisplayName("attributeImplDate_10")
			@Test
			public void attributeImplDate_10() throws Exception {
				assertEquals(currentDate.getTime(), attr.getTimestamp());
			}

			@DisplayName("attributeImplDate_11")
			@Test
			public void attributeImplDate_11() throws Exception {
				assertEquals(txt, attr.getString());
			}

			@DisplayName("attributeImplDate_12")
			@Test
			public void attributeImplDate_12() throws Exception {
				assertEquals(currentDate, attr.getJavaObject());
			}

			@DisplayName("attributeImplDate_13")
			@Test
			public void attributeImplDate_13() throws Exception {
				assertEquals(new Point2d(currentDate.getTime(), 0), attr.getPoint());
			}

			@DisplayName("attributeImplDate_14")
			@Test
			public void attributeImplDate_14() throws Exception {
				assertEquals(new Point3d(currentDate.getTime(), 0, 0), attr.getPoint3D());
			}

			@DisplayName("attributeImplDate_15")
			@Test
			public void attributeImplDate_15() throws Exception {
				assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDate_16")
			@Test
			public void attributeImplDate_16() throws Exception {
				assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
			}
		}

		@DisplayName("(float)")
		@Nested
		public class WithFloat {

			private double nb;
			private String txt;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				nb = Math.random();
				txt = Double.toString(nb);
				attr = new AttributeImpl(randomString(), nb);
			}

			@DisplayName("attributeImplFloat_1")
			@Test
			public void attributeImplFloat_1() throws Exception {
				assertEquals(AttributeType.REAL, attr.getType());
			}

			@DisplayName("attributeImplFloat_2")
			@Test
			public void attributeImplFloat_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplFloat_3")
			@Test
			public void attributeImplFloat_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("attributeImplFloat_4")
			@Test
			public void attributeImplFloat_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("attributeImplFloat_5")
			@Test
			public void attributeImplFloat_5() throws Exception {
				assertEpsilonEquals(nb, ((Number) attr.getValue()).floatValue());
			}

			@DisplayName("attributeImplFloat_6")
			@Test
			public void attributeImplFloat_6() throws Exception {
				assertEquals(nb != 0f, attr.getBoolean());
			}

			@DisplayName("attributeImplFloat_7")
			@Test
			public void attributeImplFloat_7() throws Exception {
				assertEquals(new Date((long) nb), attr.getDate());
			}

			@DisplayName("attributeImplFloat_8")
			@Test
			public void attributeImplFloat_8() throws Exception {
				assertEquals((long) nb, attr.getInteger());
			}

			@DisplayName("attributeImplFloat_9")
			@Test
			public void attributeImplFloat_9() throws Exception {
				assertEpsilonEquals(nb, attr.getReal());
			}

			@DisplayName("attributeImplFloat_10")
			@Test
			public void attributeImplFloat_10() throws Exception {
				assertEquals((long) nb, attr.getTimestamp());
			}

			@DisplayName("attributeImplFloat_11")
			@Test
			public void attributeImplFloat_11() throws Exception {
				assertEquals(txt, attr.getString());
			}

			@DisplayName("attributeImplFloat_12")
			@Test
			public void attributeImplFloat_12() throws Exception {
				assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplFloat_13")
			@Test
			public void attributeImplFloat_13() throws Exception {
				assertEquals(new Point2d(nb, 0), attr.getPoint());
			}

			@DisplayName("attributeImplFloat_14")
			@Test
			public void attributeImplFloat_14() throws Exception {
				assertEquals(new Point3d(nb, 0, 0), attr.getPoint3D());
			}

			@DisplayName("attributeImplFloat_15")
			@Test
			public void attributeImplFloat_15() throws Exception {
				assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplFloat_16")
			@Test
			public void attributeImplFloat_16() throws Exception {
				assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
			}
		}

		@DisplayName("(double)")
		@Nested
		public class WithDouble {

			private double nb;
			private String txt;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				nb = Math.random();
				txt = Double.toString(nb);
				attr = new AttributeImpl(randomString(), nb);
			}

			@DisplayName("attributeImplDouble_1")
			@Test
			public void attributeImplDouble_1() throws Exception {
				assertEquals(AttributeType.REAL, attr.getType());
			}

			@DisplayName("attributeImplDouble_2")
			@Test
			public void attributeImplDouble_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplDouble_3")
			@Test
			public void attributeImplDouble_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("attributeImplDouble_4")
			@Test
			public void attributeImplDouble_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("attributeImplDouble_5")
			@Test
			public void attributeImplDouble_5() throws Exception {
				assertEpsilonEquals(nb, ((Number) attr.getValue()).doubleValue());
			}

			@DisplayName("attributeImplDouble_6")
			@Test
			public void attributeImplDouble_6() throws Exception {
				assertEquals(nb != 0., attr.getBoolean());
			}

			@DisplayName("attributeImplDouble_7")
			@Test
			public void attributeImplDouble_7() throws Exception {
				assertEquals(new Date((long) nb), attr.getDate());
			}

			@DisplayName("attributeImplDouble_8")
			@Test
			public void attributeImplDouble_8() throws Exception {
				assertEquals((long) nb, attr.getInteger());
			}

			@DisplayName("attributeImplDouble_9")
			@Test
			public void attributeImplDouble_9() throws Exception {
				assertEpsilonEquals(nb, attr.getReal());
			}

			@DisplayName("attributeImplDouble_10")
			@Test
			public void attributeImplDouble_10() throws Exception {
				assertEquals((long) nb, attr.getTimestamp());
			}

			@DisplayName("attributeImplDouble_11")
			@Test
			public void attributeImplDouble_11() throws Exception {
				assertEquals(txt, attr.getString());
			}

			@DisplayName("attributeImplDouble_12")
			@Test
			public void attributeImplDouble_12() throws Exception {
				assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDouble_13")
			@Test
			public void attributeImplDouble_13() throws Exception {
				assertEquals(new Point2d(nb, 0), attr.getPoint());
			}

			@DisplayName("attributeImplDouble_14")
			@Test
			public void attributeImplDouble_14() throws Exception {
				assertEquals(new Point3d(nb, 0, 0), attr.getPoint3D());
			}

			@DisplayName("attributeImplDouble_15")
			@Test
			public void attributeImplDouble_15() throws Exception {
				assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDouble_16")
			@Test
			public void attributeImplDouble_16() throws Exception {
				assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
			}
		}

		@DisplayName("(int)")
		@Nested
		public class WithInt {

			private int nb;
			private String txt;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				nb = new Random().nextInt();
				txt = Long.toString(nb);
				attr = new AttributeImpl(randomString(), nb);
			}

			@DisplayName("attributeImplInt_1")
			@Test
			public void attributeImplInt_1() throws Exception {
				assertEquals(AttributeType.INTEGER, attr.getType());
			}

			@DisplayName("attributeImplInt_2")
			@Test
			public void attributeImplInt_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplInt_3")
			@Test
			public void attributeImplInt_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("attributeImplInt_4")
			@Test
			public void attributeImplInt_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("attributeImplInt_5")
			@Test
			public void attributeImplInt_5() throws Exception {
				assertEquals(nb, ((Number) attr.getValue()).intValue());
			}

			@DisplayName("attributeImplInt_6")
			@Test
			public void attributeImplInt_6() throws Exception {
				assertEquals(nb != 0, attr.getBoolean());
			}

			@DisplayName("attributeImplInt_7")
			@Test
			public void attributeImplInt_7() throws Exception {
				assertEquals(new Date(nb), attr.getDate());
			}

			@DisplayName("attributeImplInt_8")
			@Test
			public void attributeImplInt_8() throws Exception {
				assertEquals(nb, attr.getInteger());
			}

			@DisplayName("attributeImplInt_9")
			@Test
			public void attributeImplInt_9() throws Exception {
				assertEquals(nb, (int) attr.getReal());
			}

			@DisplayName("attributeImplInt_10")
			@Test
			public void attributeImplInt_10() throws Exception {
				assertEquals(nb, attr.getTimestamp());
			}

			@DisplayName("attributeImplInt_11")
			@Test
			public void attributeImplInt_11() throws Exception {
				assertEquals(txt, attr.getString());
			}

			@DisplayName("attributeImplInt_12")
			@Test
			public void attributeImplInt_12() throws Exception {
				assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplInt_13")
			@Test
			public void attributeImplInt_13() throws Exception {
				assertEquals(new Point2d(nb, 0), attr.getPoint());
			}

			@DisplayName("attributeImplInt_14")
			@Test
			public void attributeImplInt_14() throws Exception {
				assertEquals(new Point3d(nb, 0, 0), attr.getPoint3D());
			}

			@DisplayName("attributeImplInt_15")
			@Test
			public void attributeImplInt_15() throws Exception {
				assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplInt_16")
			@Test
			public void attributeImplInt_16() throws Exception {
				assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
			}
		}

		@DisplayName("(long)")
		@Nested
		public class WithLong {

			private long nb;
			private String txt;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				nb = new Random().nextLong();
				txt = Long.toString(nb);
				attr = new AttributeImpl(randomString(), nb);
			}

			@DisplayName("attributeImplLong_1")
			@Test
			public void attributeImplLong_1() throws Exception {
				assertEquals(AttributeType.INTEGER, attr.getType());
			}

			@DisplayName("attributeImplLong_2")
			@Test
			public void attributeImplLong_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplLong_3")
			@Test
			public void attributeImplLong_3() throws Exception {
				assertTrue(attr.isBaseType());
			}

			@DisplayName("attributeImplLong_4")
			@Test
			public void attributeImplLong_4() throws Exception {
				assertFalse(attr.isObjectValue());
			}

			@DisplayName("attributeImplLong_5")
			@Test
			public void attributeImplLong_5() throws Exception {
				assertEquals(nb, ((Number) attr.getValue()).longValue());
			}

			@DisplayName("attributeImplLong_6")
			@Test
			public void attributeImplLong_6() throws Exception {
				assertEquals(nb != 0, attr.getBoolean());
			}

			@DisplayName("attributeImplLong_7")
			@Test
			public void attributeImplLong_7() throws Exception {
				assertEquals(new Date(nb), attr.getDate());
			}

			@DisplayName("attributeImplLong_8")
			@Test
			public void attributeImplLong_8() throws Exception {
				assertEquals(nb, attr.getInteger());
			}

			@DisplayName("attributeImplLong_9")
			@Test
			public void attributeImplLong_9() throws Exception {
				assertEpsilonEquals(nb, attr.getReal());
			}

			@DisplayName("attributeImplLong_10")
			@Test
			public void attributeImplLong_10() throws Exception {
				assertEquals(nb, attr.getTimestamp());
			}

			@DisplayName("attributeImplLong_11")
			@Test
			public void attributeImplLong_11() throws Exception {
				assertEquals(txt, attr.getString());
			}

			@DisplayName("attributeImplLong_12")
			@Test
			public void attributeImplLong_12() throws Exception {
				assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplLong_13")
			@Test
			public void attributeImplLong_13() throws Exception {
				assertEquals(new Point2d(nb, 0), attr.getPoint());
			}

			@DisplayName("attributeImplLong_14")
			@Test
			public void attributeImplLong_14() throws Exception {
				assertEquals(new Point3d(nb, 0, 0), attr.getPoint3D());
			}

			@DisplayName("attributeImplLong_15")
			@Test
			public void attributeImplLong_15() throws Exception {
				assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplLong_16")
			@Test
			public void attributeImplLong_16() throws Exception {
				assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
			}
		}
		
		@DisplayName("(Point2d)")
		@Nested
		public class WithPoint {

			private Point2D pt;
			private Point3D pt3d;
			private String str;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				pt = new Point2d(Math.random(), Math.random());
				pt3d = new Point3d(pt.getX(), pt.getY(), 0.);
				str = pt.getX() + ";" + pt.getY(); //$NON-NLS-1$
				attr = new AttributeImpl(randomString(), pt);
			}

			@DisplayName("attributeImplPoint2d_1")
			@Test
			public void attributeImplPoint2d_1() throws Exception {
				assertEquals(AttributeType.POINT, attr.getType());
			}

			@DisplayName("attributeImplPoint2d_2")
			@Test
			public void attributeImplPoint2d_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplPoint2d_3")
			@Test
			public void attributeImplPoint2d_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImplPoint2d_4")
			@Test
			public void attributeImplPoint2d_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImplPoint2d_5")
			@Test
			public void attributeImplPoint2d_5() throws Exception {
				assertEquals(pt, attr.getValue());
			}

			@DisplayName("attributeImplPoint2d_6")
			@Test
			public void attributeImplPoint2d_6() throws Exception {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2d_7")
			@Test
			public void attributeImplPoint2d_7() throws Exception {
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2d_8")
			@Test
			public void attributeImplPoint2d_8() throws Exception {
				assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2d_9")
			@Test
			public void attributeImplPoint2d_9() throws Exception {
				assertAttributeException(attr, "getReal"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2d_10")
			@Test
			public void attributeImplPoint2d_10() throws Exception {
				assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2d_11")
			@Test
			public void attributeImplPoint2d_11() throws Exception {
				assertEquals(str, attr.getString());
			}

			@DisplayName("attributeImplPoint2d_12")
			@Test
			public void attributeImplPoint2d_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("attributeImplPoint2d_13")
			@Test
			public void attributeImplPoint2d_13() throws Exception {
				assertEquals(pt, attr.getPoint());
			}

			@DisplayName("attributeImplPoint2d_14")
			@Test
			public void attributeImplPoint2d_14() throws Exception {
				assertEquals(pt3d, attr.getPoint3D());
			}

			@DisplayName("attributeImplPoint2d_15")
			@Test
			public void attributeImplPoint2d_15() throws Exception {
				assertArrayEquals(new Point2D[] { pt }, attr.getPolyline());
			}

			@DisplayName("attributeImplPoint2d_16")
			@Test
			public void attributeImplPoint2d_16() throws Exception {
				assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
			}
		}

		@DisplayName("(double,double)")
		@Nested
		public class WithDoubleDouble {

			private double x;
			private double y;
			private Point2D pt;
			private Point3D pt3d;
			private String str;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				x = Math.random();
				y = Math.random();
				pt = new Point2d(x, y);
				pt3d = new Point3d(x, y, 0.);
				str = (x) + ";" + (y); //$NON-NLS-1$
				attr = new AttributeImpl(randomString(), x, y);
			}

			@DisplayName("attributeImplDoubleDouble_1")
			@Test
			public void attributeImplDoubleDouble_1() throws Exception {
				assertEquals(AttributeType.POINT, attr.getType());
			}

			@DisplayName("attributeImplDoubleDouble_2")
			@Test
			public void attributeImplDoubleDouble_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplDoubleDouble_3")
			@Test
			public void attributeImplDoubleDouble_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImplDoubleDouble_4")
			@Test
			public void attributeImplDoubleDouble_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImplDoubleDouble_5")
			@Test
			public void attributeImplDoubleDouble_5() throws Exception {
				assertEquals(pt, attr.getValue());
			}

			@DisplayName("attributeImplDoubleDouble_6")
			@Test
			public void attributeImplDoubleDouble_6() throws Exception {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDouble_7")
			@Test
			public void attributeImplDoubleDouble_7() throws Exception {
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDouble_8")
			@Test
			public void attributeImplDoubleDouble_8() throws Exception {
				assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDouble_9")
			@Test
			public void attributeImplDoubleDouble_9() throws Exception {
				assertAttributeException(attr, "getReal"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDouble_10")
			@Test
			public void attributeImplDoubleDouble_10() throws Exception {
				assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDouble_11")
			@Test
			public void attributeImplDoubleDouble_11() throws Exception {
				assertEquals(str, attr.getString());
			}

			@DisplayName("attributeImplDoubleDouble_12")
			@Test
			public void attributeImplDoubleDouble_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("attributeImplDoubleDouble_13")
			@Test
			public void attributeImplDoubleDouble_13() throws Exception {
				assertEquals(pt, attr.getPoint());
			}

			@DisplayName("attributeImplDoubleDouble_14")
			@Test
			public void attributeImplDoubleDouble_14() throws Exception {
				assertEquals(pt3d, attr.getPoint3D());
			}

			@DisplayName("attributeImplDoubleDouble_15")
			@Test
			public void attributeImplDoubleDouble_15() throws Exception {
				assertArrayEquals(new Point2D[] { pt }, attr.getPolyline());
			}

			@DisplayName("attributeImplDoubleDouble_16")
			@Test
			public void attributeImplDoubleDouble_16() throws Exception {
				assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
			}
		}

		@DisplayName("(Point3d)")
		@Nested
		public class WithPoint3d {

			private double x;
			private double y;
			private double z;
			private Point3D pt;
			private Point2D pt2d;
			private String str;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				x = Math.random();
				y = Math.random();
				z = Math.random();
				pt = new Point3d(x, y, z);
				pt2d = new Point2d(x, y);
				str = (x) + ";" + (y) + ";" + (z); //$NON-NLS-1$ //$NON-NLS-2$
				attr = new AttributeImpl(randomString(), pt);
			}

			@DisplayName("attributeImplPoint3d_1")
			@Test
			public void attributeImplPoint3d_1() throws Exception {
				assertEquals(AttributeType.POINT3D, attr.getType());
			}

			@DisplayName("attributeImplPoint3d_2")
			@Test
			public void attributeImplPoint3d_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplPoint3d_3")
			@Test
			public void attributeImplPoint3d_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImplPoint3d_4")
			@Test
			public void attributeImplPoint3d_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImplPoint3d_5")
			@Test
			public void attributeImplPoint3d_5() throws Exception {
				assertEquals(pt, attr.getValue());
			}

			@DisplayName("attributeImplPoint3d_6")
			@Test
			public void attributeImplPoint3d_6() throws Exception {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3d_7")
			@Test
			public void attributeImplPoint3d_7() throws Exception {
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3d_8")
			@Test
			public void attributeImplPoint3d_8() throws Exception {
				assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3d_9")
			@Test
			public void attributeImplPoint3d_9() throws Exception {
				assertAttributeException(attr, "getReal"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3d_10")
			@Test
			public void attributeImplPoint3d_10() throws Exception {
				assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3d_11")
			@Test
			public void attributeImplPoint3d_11() throws Exception {
				assertEquals(str, attr.getString());
			}

			@DisplayName("attributeImplPoint3d_12")
			@Test
			public void attributeImplPoint3d_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("attributeImplPoint3d_13")
			@Test
			public void attributeImplPoint3d_13() throws Exception {
				assertEquals(pt2d, attr.getPoint());
			}

			@DisplayName("attributeImplPoint3d_14")
			@Test
			public void attributeImplPoint3d_14() throws Exception {
				assertEquals(pt, attr.getPoint3D());
			}

			@DisplayName("attributeImplPoint3d_15")
			@Test
			public void attributeImplPoint3d_15() throws Exception {
				assertArrayEquals(new Point2D[] { pt2d }, attr.getPolyline());
			}

			@DisplayName("attributeImplPoint3d_16")
			@Test
			public void attributeImplPoint3d_16() throws Exception {
				assertArrayEquals(new Point3D[] { pt }, attr.getPolyline3D());
			}
		}

		@DisplayName("(double,double,double)")
		@Nested
		public class WithDoubleDoubleDouble {

			private double x;
			private double y;
			private double z;
			private Point3D pt;
			private Point2D pt2d;
			private String str;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				x = Math.random();
				y = Math.random();
				z = Math.random();
				pt = new Point3d(x, y, z);
				pt2d = new Point2d(x, y);
				str = (x) + ";" + (y) + ";" + (z); //$NON-NLS-1$ //$NON-NLS-2$
				attr = new AttributeImpl(randomString(), x, y, z);
			}

			@DisplayName("attributeImplDoubleDoubleDouble_1")
			@Test
			public void attributeImplDoubleDoubleDouble_1() throws Exception {
				assertEquals(AttributeType.POINT3D, attr.getType());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_2")
			@Test
			public void attributeImplDoubleDoubleDouble_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_3")
			@Test
			public void attributeImplDoubleDoubleDouble_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_4")
			@Test
			public void attributeImplDoubleDoubleDouble_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_5")
			@Test
			public void attributeImplDoubleDoubleDouble_5() throws Exception {
				assertEquals(pt, attr.getValue());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_6")
			@Test
			public void attributeImplDoubleDoubleDouble_6() throws Exception {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDoubleDouble_7")
			@Test
			public void attributeImplDoubleDoubleDouble_7() throws Exception {
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDoubleDouble_8")
			@Test
			public void attributeImplDoubleDoubleDouble_8() throws Exception {
				assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDoubleDouble_9")
			@Test
			public void attributeImplDoubleDoubleDouble_9() throws Exception {
				assertAttributeException(attr, "getReal"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDoubleDouble_10")
			@Test
			public void attributeImplDoubleDoubleDouble_10() throws Exception {
				assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplDoubleDoubleDouble_11")
			@Test
			public void attributeImplDoubleDoubleDouble_11() throws Exception {
				assertEquals(str, attr.getString());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_12")
			@Test
			public void attributeImplDoubleDoubleDouble_12() throws Exception {
				assertEquals(pt, attr.getJavaObject());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_13")
			@Test
			public void attributeImplDoubleDoubleDouble_13() throws Exception {
				assertEquals(pt2d, attr.getPoint());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_14")
			@Test
			public void attributeImplDoubleDoubleDouble_14() throws Exception {
				assertEquals(pt, attr.getPoint3D());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_15")
			@Test
			public void attributeImplDoubleDoubleDouble_15() throws Exception {
				assertArrayEquals(new Point2D[] { pt2d }, attr.getPolyline());
			}

			@DisplayName("attributeImplDoubleDoubleDouble_16")
			@Test
			public void attributeImplDoubleDoubleDouble_16() throws Exception {
				assertArrayEquals(new Point3D[] { pt }, attr.getPolyline3D());
			}
		}

		@DisplayName("(Point[])")
		@Nested
		public class WithPointArray {

			private double x1;
			private double y1;
			private double x2;
			private double y2;
			private Point2D pt1;
			private Point2D pt2;
			private Point2D[] list;
			private Point3D[] list2;
			private String str;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				x1 = Math.random();
				y1 = Math.random();
				x2 = Math.random();
				y2 = Math.random();

				pt1 = new Point2d(x1, y1);
				pt2 = new Point2d(x2, y2);

				list = new Point2D[] { pt1, pt2 };
				list2 = new Point3D[] { new Point3d(x1, y1, 0), new Point3d(x2, y2, 0) };

				str = (x1) + ";" + (y1) + ";" + (x2) + ";" + (y2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				attr = new AttributeImpl(randomString(), list);
			}

			@DisplayName("attributeImplPoint2DArray_1")
			@Test
			public void attributeImplPoint2DArray_1() throws Exception {
				assertEquals(AttributeType.POLYLINE, attr.getType());
			}

			@DisplayName("attributeImplPoint2DArray_2")
			@Test
			public void attributeImplPoint2DArray_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplPoint2DArray_3")
			@Test
			public void attributeImplPoint2DArray_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImplPoint2DArray_4")
			@Test
			public void attributeImplPoint2DArray_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImplPoint2DArray_5")
			@Test
			public void attributeImplPoint2DArray_5() throws Exception {
				assertArrayEquals(list, (Point2D[]) attr.getValue());
			}

			@DisplayName("attributeImplPoint2DArray_6")
			@Test
			public void attributeImplPoint2DArray_6() throws Exception {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2DArray_7")
			@Test
			public void attributeImplPoint2DArray_7() throws Exception {
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2DArray_8")
			@Test
			public void attributeImplPoint2DArray_8() throws Exception {
				assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2DArray_9")
			@Test
			public void attributeImplPoint2DArray_9() throws Exception {
				assertAttributeException(attr, "getReal"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2DArray_10")
			@Test
			public void attributeImplPoint2DArray_10() throws Exception {
				assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2DArray_11")
			@Test
			public void attributeImplPoint2DArray_11() throws Exception {
				assertEquals(str, attr.getString());
			}

			@DisplayName("attributeImplPoint2DArray_12")
			@Test
			public void attributeImplPoint2DArray_12() throws Exception {
				assertArrayEquals(list, attr.getJavaObject());
			}

			@DisplayName("attributeImplPoint2DArray_13")
			@Test
			public void attributeImplPoint2DArray_13() throws Exception {
				assertAttributeException(attr, "getPoint"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2DArray_14")
			@Test
			public void attributeImplPoint2DArray_14() throws Exception {
				assertAttributeException(attr, "getPoint3D"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint2DArray_15")
			@Test
			public void attributeImplPoint2DArray_15() throws Exception {
				assertArrayEquals(list, attr.getPolyline());
			}

			@DisplayName("attributeImplPoint2DArray_16")
			@Test
			public void attributeImplPoint2DArray_16() throws Exception {
				assertArrayEquals(list2, attr.getPolyline3D());
			}
		}

		@DisplayName("(Point3D[])")
		@Nested
		public class WithPoint3dArray {

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
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				x1 = Math.random();
				y1 = Math.random();
				z1 = Math.random();
				x2 = Math.random();
				y2 = Math.random();
				z2 = Math.random();

				pt1 = new Point3d(x1, y1, z1);
				pt2 = new Point3d(x2, y2, z2);

				list = new Point3D[] { pt1, pt2 };
				list2 = new Point2D[] { new Point2d(x1, y1), new Point2d(x2, y2) };

				str = (x1) + ";" + (y1) + ";" + (z1) + ";" + (x2) + ";" + (y2) + ";" + (z2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

				attr = new AttributeImpl(randomString(), list);
			}

			@DisplayName("attributeImplPoint3DArray_1")
			@Test
			public void attributeImplPoint3DArray_1() throws Exception {
				assertEquals(AttributeType.POLYLINE3D, attr.getType());
			}

			@DisplayName("attributeImplPoint3DArray_2")
			@Test
			public void attributeImplPoint3DArray_2() throws Exception {
				assertTrue(attr.isAssigned());
			}

			@DisplayName("attributeImplPoint3DArray_3")
			@Test
			public void attributeImplPoint3DArray_3() throws Exception {
				assertFalse(attr.isBaseType());
			}

			@DisplayName("attributeImplPoint3DArray_4")
			@Test
			public void attributeImplPoint3DArray_4() throws Exception {
				assertTrue(attr.isObjectValue());
			}

			@DisplayName("attributeImplPoint3DArray_5")
			@Test
			public void attributeImplPoint3DArray_5() throws Exception {
				assertArrayEquals(list, (Point3D[]) attr.getValue());
			}

			@DisplayName("attributeImplPoint3DArray_6")
			@Test
			public void attributeImplPoint3DArray_6() throws Exception {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3DArray_7")
			@Test
			public void attributeImplPoint3DArray_7() throws Exception {
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3DArray_8")
			@Test
			public void attributeImplPoint3DArray_8() throws Exception {
				assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3DArray_9")
			@Test
			public void attributeImplPoint3DArray_9() throws Exception {
				assertAttributeException(attr, "getReal"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3DArray_10")
			@Test
			public void attributeImplPoint3DArray_10() throws Exception {
				assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3DArray_11")
			@Test
			public void attributeImplPoint3DArray_11() throws Exception {
				assertEquals(str, attr.getString());
			}

			@DisplayName("attributeImplPoint3DArray_12")
			@Test
			public void attributeImplPoint3DArray_12() throws Exception {
				assertArrayEquals(list, attr.getJavaObject());
			}

			@DisplayName("attributeImplPoint3DArray_13")
			@Test
			public void attributeImplPoint3DArray_13() throws Exception {
				assertAttributeException(attr, "getPoint"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3DArray_14")
			@Test
			public void attributeImplPoint3DArray_14() throws Exception {
				assertAttributeException(attr, "getPoint3D"); //$NON-NLS-1$
			}

			@DisplayName("attributeImplPoint3DArray_15")
			@Test
			public void attributeImplPoint3DArray_15() throws Exception {
				assertArrayEquals(list2, attr.getPolyline());
			}

			@DisplayName("attributeImplPoint3DArray_16")
			@Test
			public void attributeImplPoint3DArray_16() throws Exception {
				assertArrayEquals(list, attr.getPolyline3D());
			}
		}

		@DisplayName("(String)")
		@Nested
		public class WithString {

			@DisplayName("Random string")
			@Nested
			public class FromRandomString {

				private double x;
				private Point2D pt2d;
				private Point3D pt3d;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					x = Math.random();
					pt2d = new Point2d(x, 0);
					pt3d = new Point3d(x, 0, 0);
					str = Double.toHexString(x);
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_random_1")
				@Test
				public void attributeImplString_random_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_random_2")
				@Test
				public void attributeImplString_random_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_random_3")
				@Test
				public void attributeImplString_random_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_random_4")
				@Test
				public void attributeImplString_random_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_random_5")
				@Test
				public void attributeImplString_random_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_random_6")
				@Test
				public void attributeImplString_random_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_random_7")
				@Test
				public void attributeImplString_random_7() throws Exception {
					assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_random_8")
				@Test
				public void attributeImplString_random_8() throws Exception {
					assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_random_9")
				@Test
				public void attributeImplString_random_9() throws Exception {
					assertEpsilonEquals(x, attr.getReal());
				}

				@DisplayName("attributeImplString_random_10")
				@Test
				public void attributeImplString_random_10() throws Exception {
					assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_random_11")
				@Test
				public void attributeImplString_random_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_random_12")
				@Test
				public void attributeImplString_random_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_random_13")
				@Test
				public void attributeImplString_random_13() throws Exception {
					assertEquals(pt2d, attr.getPoint());
				}

				@DisplayName("attributeImplString_random_14")
				@Test
				public void attributeImplString_random_14() throws Exception {
					assertEquals(pt3d, attr.getPoint3D());
				}

				@DisplayName("attributeImplString_random_15")
				@Test
				public void attributeImplString_random_15() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_random_16")
				@Test
				public void attributeImplString_random_16() throws Exception {
					assertArrayEquals(new Point2D[] { pt2d }, attr.getPolyline());
				}

				@DisplayName("attributeImplString_random_17")
				@Test
				public void attributeImplString_random_17() throws Exception {
					assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
				}
			}

			@DisplayName("Boolean")
			@Nested
			public class FromBoolean {

				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					str = Boolean.toString(true);
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_boolean_1")
				@Test
				public void attributeImplString_boolean_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_boolean_2")
				@Test
				public void attributeImplString_boolean_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_boolean_3")
				@Test
				public void attributeImplString_boolean_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_boolean_4")
				@Test
				public void attributeImplString_boolean_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_boolean_5")
				@Test
				public void attributeImplString_boolean_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_boolean_6")
				@Test
				public void attributeImplString_boolean_6() throws Exception {
					assertTrue(attr.getBoolean());
				}

				@DisplayName("attributeImplString_boolean_7")
				@Test
				public void attributeImplString_boolean_7() throws Exception {
					assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_8")
				@Test
				public void attributeImplString_boolean_8() throws Exception {
					assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_9")
				@Test
				public void attributeImplString_boolean_9() throws Exception {
					assertAttributeException(attr, "getReal"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_10")
				@Test
				public void attributeImplString_boolean_10() throws Exception {
					assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_11")
				@Test
				public void attributeImplString_boolean_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_boolean_12")
				@Test
				public void attributeImplString_boolean_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_13")
				@Test
				public void attributeImplString_boolean_13() throws Exception {
					assertAttributeException(attr, "getPoint"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_14")
				@Test
				public void attributeImplString_boolean_14() throws Exception {
					assertAttributeException(attr, "getPoint3D"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_15")
				@Test
				public void attributeImplString_boolean_15() throws Exception {
					assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_boolean_16")
				@Test
				public void attributeImplString_boolean_16() throws Exception {
					assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
				}
			}

			@DisplayName("Date string")
			@Nested
			public class FromDateString {

				private Date currentDate;
				private SimpleDateFormat format;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					currentDate = new Date();
					format = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
					str = format.format(currentDate);
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_Date_1")
				@Test
				public void attributeImplString_Date_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_Date_2")
				@Test
				public void attributeImplString_Date_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_Date_3")
				@Test
				public void attributeImplString_Date_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_Date_4")
				@Test
				public void attributeImplString_Date_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_Date_5")
				@Test
				public void attributeImplString_Date_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_Date_6")
				@Test
				public void attributeImplString_Date_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_7")
				@Test
				public void attributeImplString_Date_7() throws Exception {
					assertEpsilonEquals(currentDate, attr.getDate());
				}

				@DisplayName("attributeImplString_Date_8")
				@Test
				public void attributeImplString_Date_8() throws Exception {
					assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_9")
				@Test
				public void attributeImplString_Date_9() throws Exception {
					assertAttributeException(attr, "getReal"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_10")
				@Test
				public void attributeImplString_Date_10() throws Exception {
					assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_11")
				@Test
				public void attributeImplString_Date_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_Date_12")
				@Test
				public void attributeImplString_Date_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_13")
				@Test
				public void attributeImplString_Date_13() throws Exception {
					assertAttributeException(attr, "getPoint"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_14")
				@Test
				public void attributeImplString_Date_14() throws Exception {
					assertAttributeException(attr, "getPoint3D"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_15")
				@Test
				public void attributeImplString_Date_15() throws Exception {
					assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Date_16")
				@Test
				public void attributeImplString_Date_16() throws Exception {
					assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
				}
			}

			@DisplayName("Date object")
			@Nested
			public class FromDateObject {

				private Date currentDate;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					currentDate = new Date();
					str = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(currentDate);
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_JDate_1")
				@Test
				public void attributeImplString_JDate_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_JDate_2")
				@Test
				public void attributeImplString_JDate_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_JDate_3")
				@Test
				public void attributeImplString_JDate_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_JDate_4")
				@Test
				public void attributeImplString_JDate_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_JDate_5")
				@Test
				public void attributeImplString_JDate_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_JDate_6")
				@Test
				public void attributeImplString_JDate_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_7")
				@Test
				public void attributeImplString_JDate_7() throws Exception {
					assertEpsilonEquals(currentDate, attr.getDate());
				}

				@DisplayName("attributeImplString_JDate_8")
				@Test
				public void attributeImplString_JDate_8() throws Exception {
					assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_9")
				@Test
				public void attributeImplString_JDate_9() throws Exception {
					assertAttributeException(attr, "getReal"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_10")
				@Test
				public void attributeImplString_JDate_10() throws Exception {
					assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_11")
				@Test
				public void attributeImplString_JDate_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_JDate_12")
				@Test
				public void attributeImplString_JDate_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_13")
				@Test
				public void attributeImplString_JDate_13() throws Exception {
					assertAttributeException(attr, "getPoint"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_14")
				@Test
				public void attributeImplString_JDate_14() throws Exception {
					assertAttributeException(attr, "getPoint3D"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_15")
				@Test
				public void attributeImplString_JDate_15() throws Exception {
					assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_JDate_16")
				@Test
				public void attributeImplString_JDate_16() throws Exception {
					assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
				}
			}

			@DisplayName("integer")
			@Nested
			public class FromInteger {

				private int nb;
				private Point2D pt2d;
				private Point3D pt3d;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					nb = new Random().nextInt(20000) + 256;
					pt2d = new Point2d(nb, 0);
					pt3d = new Point3d(nb, 0, 0);
					str = Integer.toString(nb);
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_Integer_1")
				@Test
				public void attributeImplString_Integer_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_Integer_2")
				@Test
				public void attributeImplString_Integer_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_Integer_3")
				@Test
				public void attributeImplString_Integer_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_Integer_4")
				@Test
				public void attributeImplString_Integer_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_Integer_5")
				@Test
				public void attributeImplString_Integer_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_Integer_6")
				@Test
				public void attributeImplString_Integer_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Integer_7")
				@Test
				public void attributeImplString_Integer_7() throws Exception {
					assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Integer_8")
				@Test
				public void attributeImplString_Integer_8() throws Exception {
					assertEquals(nb, attr.getInteger());
				}

				@DisplayName("attributeImplString_Integer_9")
				@Test
				public void attributeImplString_Integer_9() throws Exception {
					assertEpsilonEquals(nb, attr.getReal());
				}

				@DisplayName("attributeImplString_Integer_10")
				@Test
				public void attributeImplString_Integer_10() throws Exception {
					assertEquals(nb, attr.getTimestamp());
				}

				@DisplayName("attributeImplString_Integer_11")
				@Test
				public void attributeImplString_Integer_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_Integer_12")
				@Test
				public void attributeImplString_Integer_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Integer_13")
				@Test
				public void attributeImplString_Integer_13() throws Exception {
					assertEquals(pt2d, attr.getPoint());
				}

				@DisplayName("attributeImplString_Integer_14")
				@Test
				public void attributeImplString_Integer_14() throws Exception {
					assertEquals(pt3d, attr.getPoint3D());
				}

				@DisplayName("attributeImplString_Integer_15")
				@Test
				public void attributeImplString_Integer_15() throws Exception {
					assertArrayEquals(new Point2D[] { pt2d }, attr.getPolyline());
				}

				@DisplayName("attributeImplString_Integer_16")
				@Test
				public void attributeImplString_Integer_16() throws Exception {
					assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
				}
			}

			@DisplayName("long")
			@Nested
			public class FromLong {

				private long nb;
				private Point2D pt2d;
				private Point3D pt3d;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					nb = new Random().nextInt(20000) + 256;
					pt2d = new Point2d(nb, 0);
					pt3d = new Point3d(nb, 0, 0);
					str = Long.toString(nb);
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_Long_1")
				@Test
				public void attributeImplString_Long_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_Long_2")
				@Test
				public void attributeImplString_Long_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_Long_3")
				@Test
				public void attributeImplString_Long_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_Long_4")
				@Test
				public void attributeImplString_Long_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_Long_5")
				@Test
				public void attributeImplString_Long_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_Long_6")
				@Test
				public void attributeImplString_Long_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Long_7")
				@Test
				public void attributeImplString_Long_7() throws Exception {
					assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Long_8")
				@Test
				public void attributeImplString_Long_8() throws Exception {
					assertEquals(nb, attr.getInteger());
				}

				@DisplayName("attributeImplString_Long_9")
				@Test
				public void attributeImplString_Long_9() throws Exception {
					assertEpsilonEquals(nb, attr.getReal());
				}

				@DisplayName("attributeImplString_Long_10")
				@Test
				public void attributeImplString_Long_10() throws Exception {
					assertEquals(nb, attr.getTimestamp());
				}

				@DisplayName("attributeImplString_Long_11")
				@Test
				public void attributeImplString_Long_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_Long_12")
				@Test
				public void attributeImplString_Long_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Long_13")
				@Test
				public void attributeImplString_Long_13() throws Exception {
					assertEquals(pt2d, attr.getPoint());
				}

				@DisplayName("attributeImplString_Long_14")
				@Test
				public void attributeImplString_Long_14() throws Exception {
					assertEquals(pt3d, attr.getPoint3D());
				}

				@DisplayName("attributeImplString_Long_15")
				@Test
				public void attributeImplString_Long_15() throws Exception {
					assertArrayEquals(new Point2D[] { pt2d }, attr.getPolyline());
				}

				@DisplayName("attributeImplString_Long_16")
				@Test
				public void attributeImplString_Long_16() throws Exception {
					assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
				}
			}
			
			@DisplayName("double")
			@Nested
			public class FromDouble {

				private double nb;
				private Point2D pt2d;
				private Point3D pt3d;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					nb = Math.random() + 256;
					pt2d = new Point2d(nb, 0);
					pt3d = new Point3d(nb, 0, 0);
					str = Double.toString(nb);
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_Double_1")
				@Test
				public void attributeImplString_Double_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_Double_2")
				@Test
				public void attributeImplString_Double_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_Double_3")
				@Test
				public void attributeImplString_Double_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_Double_4")
				@Test
				public void attributeImplString_Double_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_Double_5")
				@Test
				public void attributeImplString_Double_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_Double_6")
				@Test
				public void attributeImplString_Double_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Double_7")
				@Test
				public void attributeImplString_Double_7() throws Exception {
					assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Double_8")
				@Test
				public void attributeImplString_Double_8() throws Exception {
					assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Double_9")
				@Test
				public void attributeImplString_Double_9() throws Exception {
					assertEpsilonEquals(nb, attr.getReal());
				}

				@DisplayName("attributeImplString_Double_10")
				@Test
				public void attributeImplString_Double_10() throws Exception {
					assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Double_11")
				@Test
				public void attributeImplString_Double_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_Double_12")
				@Test
				public void attributeImplString_Double_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Double_13")
				@Test
				public void attributeImplString_Double_13() throws Exception {
					assertEquals(pt2d, attr.getPoint());
				}

				@DisplayName("attributeImplString_Double_14")
				@Test
				public void attributeImplString_Double_14() throws Exception {
					assertEquals(pt3d, attr.getPoint3D());
				}

				@DisplayName("attributeImplString_Double_15")
				@Test
				public void attributeImplString_Double_15() throws Exception {
					assertArrayEquals(new Point2D[] { pt2d }, attr.getPolyline());
				}

				@DisplayName("attributeImplString_Double_16")
				@Test
				public void attributeImplString_Double_16() throws Exception {
					assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
				}
			}

			@DisplayName("point")
			@Nested
			public class FromPoint {

				private double x;
				private double y;
				private Point2D pt2d;
				private Point3D pt3d;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					x = Math.random() + 256;
					y = Math.random() + 256;
					pt2d = new Point2d(x, y);
					pt3d = new Point3d(x, y, 0);
					str = x + ";" + y; //$NON-NLS-1$
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_Point2D_1")
				@Test
				public void attributeImplString_Point2D_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_Point2D_2")
				@Test
				public void attributeImplString_Point2D_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_Point2D_3")
				@Test
				public void attributeImplString_Point2D_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_Point2D_4")
				@Test
				public void attributeImplString_Point2D_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_Point2D_5")
				@Test
				public void attributeImplString_Point2D_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_Point2D_6")
				@Test
				public void attributeImplString_Point2D_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point2D_7")
				@Test
				public void attributeImplString_Point2D_7() throws Exception {
					assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point2D_8")
				@Test
				public void attributeImplString_Point2D_8() throws Exception {
					assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point2D_9")
				@Test
				public void attributeImplString_Point2D_9() throws Exception {
					assertAttributeException(attr, "getReal"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point2D_10")
				@Test
				public void attributeImplString_Point2D_10() throws Exception {
					assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point2D_11")
				@Test
				public void attributeImplString_Point2D_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_Point2D_12")
				@Test
				public void attributeImplString_Point2D_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point2D_13")
				@Test
				public void attributeImplString_Point2D_13() throws Exception {
					assertEquals(pt2d, attr.getPoint());
				}

				@DisplayName("attributeImplString_Point2D_14")
				@Test
				public void attributeImplString_Point2D_14() throws Exception {
					assertEquals(pt3d, attr.getPoint3D());
				}

				@DisplayName("attributeImplString_Point2D_15")
				@Test
				public void attributeImplString_Point2D_15() throws Exception {
					assertArrayEquals(new Point2D[] { pt2d }, attr.getPolyline());
				}

				@DisplayName("attributeImplString_Point2D_16")
				@Test
				public void attributeImplString_Point2D_16() throws Exception {
					assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
				}
			}

			@DisplayName("point3d")
			@Nested
			public class FromPoint3D {

				private double x;
				private double y;
				private double z;
				private Point2D pt2d;
				private Point2D pt2d2;
				private Point3D pt3d;
				private String str;
				private Attribute attr;

				@BeforeEach
				public void setUp() {
					x = Math.random() + 256;
					y = Math.random() + 256;
					z = Math.random() + 256;
					pt2d = new Point2d(x, y);
					pt2d2 = new Point2d(z, 0);
					pt3d = new Point3d(x, y, z);
					str = x + ";" + y + ";" + z; //$NON-NLS-1$ //$NON-NLS-2$
					attr = new AttributeImpl(randomString(), str);
				}

				@DisplayName("attributeImplString_Point3D_1")
				@Test
				public void attributeImplString_Point3D_1() throws Exception {
					assertEquals(AttributeType.STRING, attr.getType());
				}

				@DisplayName("attributeImplString_Point3D_2")
				@Test
				public void attributeImplString_Point3D_2() throws Exception {
					assertTrue(attr.isAssigned());
				}

				@DisplayName("attributeImplString_Point3D_3")
				@Test
				public void attributeImplString_Point3D_3() throws Exception {
					assertTrue(attr.isBaseType());
				}

				@DisplayName("attributeImplString_Point3D_4")
				@Test
				public void attributeImplString_Point3D_4() throws Exception {
					assertFalse(attr.isObjectValue());
				}

				@DisplayName("attributeImplString_Point3D_5")
				@Test
				public void attributeImplString_Point3D_5() throws Exception {
					assertEquals(str, attr.getValue());
				}

				@DisplayName("attributeImplString_Point3D_6")
				@Test
				public void attributeImplString_Point3D_6() throws Exception {
					assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point3D_7")
				@Test
				public void attributeImplString_Point3D_7() throws Exception {
					assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point3D_8")
				@Test
				public void attributeImplString_Point3D_8() throws Exception {
					assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point3D_9")
				@Test
				public void attributeImplString_Point3D_9() throws Exception {
					assertAttributeException(attr, "getReal"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point3D_10")
				@Test
				public void attributeImplString_Point3D_10() throws Exception {
					assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point3D_11")
				@Test
				public void attributeImplString_Point3D_11() throws Exception {
					assertEquals(str, attr.getString());
				}

				@DisplayName("attributeImplString_Point3D_12")
				@Test
				public void attributeImplString_Point3D_12() throws Exception {
					assertAttributeException(attr, "getJavaObject"); //$NON-NLS-1$
				}

				@DisplayName("attributeImplString_Point3D_13")
				@Test
				public void attributeImplString_Point3D_13() throws Exception {
					assertEquals(pt2d, attr.getPoint());
				}

				@DisplayName("attributeImplString_Point3D_14")
				@Test
				public void attributeImplString_Point3D_14() throws Exception {
					assertEquals(pt3d, attr.getPoint3D());
				}

				@DisplayName("attributeImplString_Point3D_15")
				@Test
				public void attributeImplString_Point3D_15() throws Exception {
					assertArrayEquals(new Point2D[] { pt2d, pt2d2 }, attr.getPolyline());
				}

				@DisplayName("attributeImplString_Point3D_16")
				@Test
				public void attributeImplString_Point3D_16() throws Exception {
					assertArrayEquals(new Point3D[] { pt3d }, attr.getPolyline3D());
				}
			}			
		}

		@DisplayName("(AttributeType)")
		@Nested
		public class WithAttributeType {

			private Attribute attr;

			public void setUp(AttributeType type) {
				attr = new AttributeImpl(type);
			}
			
			@DisplayName("getType")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_1(AttributeType type) throws Exception {
				setUp(type);
				assertEquals(type, attr.getType());
			}
			
			@DisplayName("isAssigned")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_2(AttributeType type) throws Exception {
				setUp(type);
				assertFalse(attr.isAssigned());
			}
			
			@DisplayName("isBaseType")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_3(AttributeType type) throws Exception {
				setUp(type);
				assertEquals(type.isBaseType(),attr.isBaseType());
			}
			
			@DisplayName("isObjectValue")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_4(AttributeType type) throws Exception {
				setUp(type);
				assertEquals(
						!type.isBaseType(),
						attr.isObjectValue(),
						"on type "+type);  //$NON-NLS-1$
			}
			
			@DisplayName("getBoolean")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_5(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getBoolean");  //$NON-NLS-1$
			}
			
			@DisplayName("getDate")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_6(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getDate");  //$NON-NLS-1$
			}
			
			@DisplayName("getInteger")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_7(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getInteger");  //$NON-NLS-1$
			}
			
			@DisplayName("getJavaObject")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_8(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertNull(attr.getJavaObject());
			}
			
			@DisplayName("getPoint")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_9(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPoint");  //$NON-NLS-1$
			}
			
			@DisplayName("getPoint3D")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_10(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPoint3D");  //$NON-NLS-1$
			}
			
			@DisplayName("getPolyline")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_11(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPolyline");  //$NON-NLS-1$
			}
			
			@DisplayName("getPolyline3D")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_12(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getPolyline3D");  //$NON-NLS-1$
			}
			
			@DisplayName("getReal")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_13(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getReal");  //$NON-NLS-1$
			}
			
			@DisplayName("getString")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_14(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getString");  //$NON-NLS-1$
			}
			
			@DisplayName("getTimestamp")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_15(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getTimestamp");  //$NON-NLS-1$
			}
			
			@DisplayName("getURI")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_16(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getURI");  //$NON-NLS-1$
			}
			
			@DisplayName("getURL")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_17(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getURL");  //$NON-NLS-1$
			}
			
			@DisplayName("getUUID")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_18(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getUUID");  //$NON-NLS-1$
			}
			
			@DisplayName("getValue")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_19(AttributeType type) throws Exception {
				assumeTrue(type.isNullAllowed());
				setUp(type);
				assertAttributeException(attr, "getValue");  //$NON-NLS-1$
			}
			
			@DisplayName("Null is not allowed")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(AttributeType.class)
			public void attributeImplAttributeType_20(AttributeType type) throws Exception {
				assumeFalse(type.isNullAllowed());
				setUp(type);
				assertAllGetFailed(attr, type);
			}
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsTest {

		private AttributeImpl attr;

		@BeforeEach
		public void setUp() {
			attr = new AttributeImpl("A1"); //$NON-NLS-1$
		}

		@DisplayName("testEquals_1")
		@Test
		public void testEquals_1() {
			attr.setBoolean(true);
			assertTrue(attr.equals(new AttributeImpl("A1", true))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_2")
		@Test
		public void testEquals_2() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeImpl("A1", false))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_3")
		@Test
		public void testEquals_3() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeImpl("A2", true))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_4")
		@Test
		public void testEquals_4() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeImpl("A2", false))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_5")
		@Test
		public void testEquals_5() {
			attr.setBoolean(true);
			assertTrue(attr.equals(new AttributeValueImpl(true)));
		}

		@DisplayName("testEquals_6")
		@Test
		public void testEquals_6() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl(false)));
		}

		@DisplayName("testEquals_7")
		@Test
		public void testEquals_7() {
			attr.setBoolean(true);
			assertTrue(attr.equals(true));
		}

		@DisplayName("testEquals_8")
		@Test
		public void testEquals_8() {
			attr.setBoolean(true);
			assertFalse(attr.equals(false));
		}

		@DisplayName("testEquals_9")
		@Test
		public void testEquals_9() {
			attr.setBoolean(true);
			assertTrue(attr.equals(new AttributeValueImpl("true"))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_10")
		@Test
		public void testEquals_10() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl("false"))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_11")
		@Test
		public void testEquals_11() {
			attr.setBoolean(true);
			assertTrue(attr.equals("true")); //$NON-NLS-1$
		}

		@DisplayName("testEquals_12")
		@Test
		public void testEquals_12() {
			attr.setBoolean(true);
			assertFalse(attr.equals("false")); //$NON-NLS-1$
		}

		@DisplayName("testEquals_13")
		@Test
		public void testEquals_13() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl(1.)));
		}

		@DisplayName("testEquals_14")
		@Test
		public void testEquals_14() {
			attr.setBoolean(true);
			assertFalse(attr.equals(new AttributeValueImpl("1."))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_15")
		@Test
		public void testEquals_15() {
			attr.setBoolean(true);
			assertFalse(attr.equals(1.));
		}

		@DisplayName("testEquals_16")
		@Test
		public void testEquals_16() {
			attr.setBoolean(true);
			assertFalse(attr.equals("toto")); //$NON-NLS-1$
		}

		@DisplayName("testEquals_17")
		@Test
		public void testEquals_17() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeImpl("A1", true))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_18")
		@Test
		public void testEquals_18() {
			attr.setBoolean(false);
			assertTrue(attr.equals(new AttributeImpl("A1", false))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_19")
		@Test
		public void testEquals_19() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeImpl("A2", true))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_20")
		@Test
		public void testEquals_20() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeImpl("A2", false))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_21")
		@Test
		public void testEquals_21() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl(true)));
		}

		@DisplayName("testEquals_22")
		@Test
		public void testEquals_22() {
			attr.setBoolean(false);
			assertTrue(attr.equals(new AttributeValueImpl(false)));
		}

		@DisplayName("testEquals_23")
		@Test
		public void testEquals_23() {
			attr.setBoolean(false);
			assertFalse(attr.equals(true));
		}

		@DisplayName("testEquals_24")
		@Test
		public void testEquals_24() {
			attr.setBoolean(false);
			assertTrue(attr.equals(false));
		}

		@DisplayName("testEquals_25")
		@Test
		public void testEquals_25() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl("true"))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_26")
		@Test
		public void testEquals_26() {
			attr.setBoolean(false);
			assertTrue(attr.equals(new AttributeValueImpl("false"))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_27")
		@Test
		public void testEquals_27() {
			attr.setBoolean(false);
			assertFalse(attr.equals("true")); //$NON-NLS-1$
		}

		@DisplayName("testEquals_28")
		@Test
		public void testEquals_28() {
			attr.setBoolean(false);
			assertTrue(attr.equals("false")); //$NON-NLS-1$
		}

		@DisplayName("testEquals_29")
		@Test
		public void testEquals_29() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl(1.)));
		}

		@DisplayName("testEquals_30")
		@Test
		public void testEquals_30() {
			attr.setBoolean(false);
			assertFalse(attr.equals(new AttributeValueImpl("1."))); //$NON-NLS-1$
		}

		@DisplayName("testEquals_31")
		@Test
		public void testEquals_31() {
			attr.setBoolean(false);
			assertFalse(attr.equals(1.));
		}

		@DisplayName("testEquals_32")
		@Test
		public void testEquals_32() {
			attr.setBoolean(false);
			assertFalse(attr.equals("toto")); //$NON-NLS-1$
		}
	}
}

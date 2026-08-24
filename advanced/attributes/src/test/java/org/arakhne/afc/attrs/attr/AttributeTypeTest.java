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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.i.Point2i;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("AttributeType")
@SuppressWarnings("all")
public class AttributeTypeTest extends AbstractTestCase {

	protected static void assertCastException(AttributeType type, Object value) {
		assertThrows(ClassCastException.class, () -> {
			type.cast(value);
		});
	}

	protected static void assertNullException(AttributeType type, Object value) {
		assertThrows(NullPointerException.class, () -> {
			type.cast(value);
		});
	}

	public static Stream<Arguments> provideIntAttributeType() {
		final List<Arguments> args = new ArrayList<>();
		for (var type : AttributeType.values()) {
			args.add(Arguments.of(
					Integer.valueOf(type.ordinal()),
					type));
		}
		return args.stream();
	}

	@DisplayName("getName")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(AttributeType.class)
	public void getName(AttributeType type) {
		String name = type.getLocalizedName();
		assertNotNull(name);
		assertNotSame("", name);  //$NON-NLS-1$
	}

	@DisplayName("isNumberType")
	@Nested
	public class IsNumberType {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(AttributeType.class)
		public void isNumberType_1(AttributeType type) {
			assertEquals(type==AttributeType.INTEGER || type==AttributeType.REAL || type==AttributeType.TIMESTAMP,
					type.isNumberType());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(AttributeType.class)
		public void isNumberType_2(AttributeType type) {
			assumeTrue(type.isNumberType());
			assertTrue(type.isBaseType());
		}
	}

	@DisplayName("fromInteger")
	@Nested
	public class FromInteger {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.attrs.attr.AttributeTypeTest#provideIntAttributeType")
		public void fromInteger_1(Integer i, AttributeType type) {
			AttributeType type0 = AttributeType.fromInteger(i);
			assertEquals(i, type0.ordinal());
			assertSame(type, type0);
		}

		@DisplayName("#2")
		@Test
		public void fromInteger_2() {
			var type = AttributeType.fromInteger(AttributeType.values().length);
			assertNotNull(type);
			assertEquals(AttributeType.OBJECT, type);
		}

		@DisplayName("#3")
		@Test
		public void fromInteger_3() {
			var type = AttributeType.fromInteger(AttributeType.values().length + 1);
			assertNotNull(type);
			assertEquals(AttributeType.OBJECT, type);
		}

		@DisplayName("#4")
		@Test
		public void fromInteger_4() {
			var type = AttributeType.fromInteger(AttributeType.values().length + 2);
			assertNotNull(type);
			assertEquals(AttributeType.OBJECT, type);
		}
	}

	@DisplayName("fromValue")
	@Nested
	public class FromValue {

		@DisplayName("null")
		@Test
		public void fromValue_1() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromValue(null));
		}

		@DisplayName("true")
		@Test
		public void fromValue_2() throws Exception {
			assertEquals(AttributeType.BOOLEAN, AttributeType.fromValue(true));
		}

		@DisplayName("(char)")
		@Test
		public void fromValue_3() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromValue('c'));
		}

		@DisplayName("(byte)")
		@Test
		public void fromValue_4() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromValue((byte)1));
		}

		@DisplayName("(short)")
		@Test
		public void fromValue_5() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromValue((short)1));
		}

		@DisplayName("(int)")
		@Test
		public void fromValue_6() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromValue(1));
		}

		@DisplayName("(long)")
		@Test
		public void fromValue_7() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromValue(1l));
		}

		@DisplayName("(float)")
		@Test
		public void fromValue_8() throws Exception {
			assertEquals(AttributeType.REAL, AttributeType.fromValue(1f));
		}

		@DisplayName("(double)")
		@Test
		public void fromValue_9() throws Exception {
			assertEquals(AttributeType.REAL, AttributeType.fromValue(1.));
		}

		@DisplayName("(String)")
		@Test
		public void fromValue_10() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromValue("Hello"));  //$NON-NLS-1$
		}

		@DisplayName("(StringBuffer)")
		@Test
		public void fromValue_11() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromValue(new StringBuffer()));
		}

		@DisplayName("(Calendar)")
		@Test
		public void fromValue_12() throws Exception {
			assertEquals(AttributeType.DATE, AttributeType.fromValue(Calendar.getInstance()));
		}

		@DisplayName("(Date)")
		@Test
		public void fromValue_13() throws Exception {
			assertEquals(AttributeType.DATE, AttributeType.fromValue(new Date()));
		}

		@DisplayName("UUID")
		@Test
		public void fromValue_14() throws Exception {
			assertEquals(AttributeType.UUID, AttributeType.fromValue(UUID.randomUUID()));
		}

		@DisplayName("(Point2d)")
		@Test
		public void fromValue_15() throws Exception {
			assertEquals(AttributeType.POINT, AttributeType.fromValue(new Point2d(0,0)));
		}

		@DisplayName("(Point2i)")
		@Test
		public void fromValue_16() throws Exception {
			assertEquals(AttributeType.POINT, AttributeType.fromValue(new Point2i(0,0)));
		}

		@DisplayName("(Point3d)")
		@Test
		public void fromValue_17() throws Exception {
			assertEquals(AttributeType.POINT3D, AttributeType.fromValue(new Point3d(0,0,0)));
		}

		@DisplayName("(Point2D[])")
		@Test
		public void fromValue_18() throws Exception {
			assertEquals(AttributeType.POLYLINE, AttributeType.fromValue(new Point2D[0]));
		}

		@DisplayName("(Point3D[])")
		@Test
		public void fromValue_19() throws Exception {
			assertEquals(AttributeType.POLYLINE3D, AttributeType.fromValue(new Point3D[0]));
		}

		@DisplayName("(URL)")
		@Test
		public void fromValue_20() throws Exception {
			assertEquals(AttributeType.URL, AttributeType.fromValue(new URL("http://www.google.com")));  //$NON-NLS-1$
		}

		@DisplayName("(URI)")
		@Test
		public void fromValue_21() throws Exception {
			assertEquals(AttributeType.URI, AttributeType.fromValue(new URI("http://www.google.com")));  //$NON-NLS-1$
		}

		@DisplayName("(InetAddress)")
		@Test
		public void fromValue_22() throws Exception {
			assertEquals(AttributeType.INET_ADDRESS, AttributeType.fromValue(InetAddress.getLocalHost()));
		}

		@DisplayName("(Enumeration)")
		@Test
		public void fromValue_23() throws Exception {
			AttributeType randomType = randomEnum(AttributeType.class);
			assertEquals(AttributeType.ENUMERATION, AttributeType.fromValue(randomType));
		}

		@DisplayName("(int[])")
		@Test
		public void fromValue_24() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromValue(new int[0]));
		}

		@DisplayName("(Locale)")
		@Test
		public void fromValue_25() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromValue(Locale.getDefault()));
		}

		@DisplayName("(Type)")
		@Test
		public void fromValue_26() throws Exception {
			assertEquals(AttributeType.TYPE, AttributeType.fromValue(AttributeTypeTest.class));
		}

		@DisplayName("(double.class)")
		@Test
		public void fromValue_27() throws Exception {
			assertEquals(AttributeType.TYPE, AttributeType.fromValue(double.class));
		}
	}

	@DisplayName("fromClass")
	@Nested
	public class FromClass {

		@DisplayName("null")
		@Test
		public void fromClass_1() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromClass(null));
		}

		@DisplayName("boolean")
		@Test
		public void fromClass_2() throws Exception {
			assertEquals(AttributeType.BOOLEAN, AttributeType.fromClass(boolean.class));
		}

		@DisplayName("Boolean")
		@Test
		public void fromClass_3() throws Exception {
			assertEquals(AttributeType.BOOLEAN, AttributeType.fromClass(Boolean.class));
		}

		@DisplayName("char")
		@Test
		public void fromClass_4() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromClass(char.class));
		}

		@DisplayName("Character")
		@Test
		public void fromClass_5() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromClass(Character.class));
		}

		@DisplayName("byte")
		@Test
		public void fromClass_6() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(byte.class));
		}

		@DisplayName("Byte")
		@Test
		public void fromClass_7() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Byte.class));
		}

		@DisplayName("short")
		@Test
		public void fromClass_8() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(short.class));
		}

		@DisplayName("Short")
		@Test
		public void fromClass_9() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Short.class));
		}

		@DisplayName("int")
		@Test
		public void fromClass_10() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(int.class));
		}

		@DisplayName("Integer")
		@Test
		public void fromClass_11() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Integer.class));
		}

		@DisplayName("long")
		@Test
		public void fromClass_12() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(long.class));
		}

		@DisplayName("Long")
		@Test
		public void fromClass_13() throws Exception {
			assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Long.class));
		}

		@DisplayName("float")
		@Test
		public void fromClass_14() throws Exception {
			assertEquals(AttributeType.REAL, AttributeType.fromClass(float.class));
		}

		@DisplayName("Float")
		@Test
		public void fromClass_15() throws Exception {
			assertEquals(AttributeType.REAL, AttributeType.fromClass(Float.class));
		}

		@DisplayName("double")
		@Test
		public void fromClass_16() throws Exception {
			assertEquals(AttributeType.REAL, AttributeType.fromClass(double.class));
		}

		@DisplayName("Double")
		@Test
		public void fromClass_17() throws Exception {
			assertEquals(AttributeType.REAL, AttributeType.fromClass(Double.class));
		}

		@DisplayName("String")
		@Test
		public void fromClass_18() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromClass(String.class));
		}

		@DisplayName("StringBuffer")
		@Test
		public void fromClass_19() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromClass(StringBuffer.class));
		}

		@DisplayName("StringBuilder")
		@Test
		public void fromClass_20() throws Exception {
			assertEquals(AttributeType.STRING, AttributeType.fromClass(StringBuilder.class));
		}

		@DisplayName("Caldnar")
		@Test
		public void fromClass_21() throws Exception {
			assertEquals(AttributeType.DATE, AttributeType.fromClass(Calendar.class));
		}

		@DisplayName("Date")
		@Test
		public void fromClass_22() throws Exception {
			assertEquals(AttributeType.DATE, AttributeType.fromClass(Date.class));
		}

		@DisplayName("UUID")
		@Test
		public void fromClass_23() throws Exception {
			assertEquals(AttributeType.UUID, AttributeType.fromClass(UUID.class));
		}

		@DisplayName("Point2D")
		@Test
		public void fromClass_24() throws Exception {
			assertEquals(AttributeType.POINT, AttributeType.fromClass(Point2D.class));
		}

		@DisplayName("Point3D")
		@Test
		public void fromClass_25() throws Exception {
			assertEquals(AttributeType.POINT3D, AttributeType.fromClass(Point3D.class));
		}

		@DisplayName("Tuple2D[]")
		@Test
		public void fromClass_26() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromClass(Tuple2D[].class));
		}

		@DisplayName("Point2D[]")
		@Test
		public void fromClass_27() throws Exception {
			assertEquals(AttributeType.POLYLINE, AttributeType.fromClass(Point2D[].class));
		}

		@DisplayName("Tuple3D[]")
		@Test
		public void fromClass_28() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromClass(Tuple3D[].class));
		}

		@DisplayName("Point3D[]")
		@Test
		public void fromClass_29() throws Exception {
			assertEquals(AttributeType.POLYLINE3D, AttributeType.fromClass(Point3D[].class));
		}

		@DisplayName("URL")
		@Test
		public void fromClass_30() throws Exception {
			assertEquals(AttributeType.URL, AttributeType.fromClass(URL.class));
		}

		@DisplayName("URI")
		@Test
		public void fromClass_31() throws Exception {
			assertEquals(AttributeType.URI, AttributeType.fromClass(URI.class));
		}

		@DisplayName("InetAddress")
		@Test
		public void fromClass_32() throws Exception {
			assertEquals(AttributeType.INET_ADDRESS, AttributeType.fromClass(InetAddress.class));
		}

		@DisplayName("AttributeType")
		@Test
		public void fromClass_33() throws Exception {
			assertEquals(AttributeType.ENUMERATION, AttributeType.fromClass(AttributeType.class));
		}

		@DisplayName("int[]")
		@Test
		public void fromClass_34() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromClass(int[].class));
		}

		@DisplayName("Locale")
		@Test
		public void fromClass_35() throws Exception {
			assertEquals(AttributeType.OBJECT, AttributeType.fromClass(Locale.class));
		}

		@DisplayName("Class")
		@Test
		public void fromClass_36() throws Exception {
			assertEquals(AttributeType.TYPE, AttributeType.fromClass(Class.class));
		}
	}

	@DisplayName("isBaseType")
	@Nested
	public class IsBaseType {

		@DisplayName("BOOLEAN")
		@Test
		public void isBaseType_1() {
			assertTrue(AttributeType.BOOLEAN.isBaseType());
		}

		@DisplayName("INTEGER")
		@Test
		public void isBaseType_2() {
			assertTrue(AttributeType.INTEGER.isBaseType());
		}

		@DisplayName("REAL")
		@Test
		public void isBaseType_3() {
			assertTrue(AttributeType.REAL.isBaseType());
		}

		@DisplayName("STRING")
		@Test
		public void isBaseType_4() {
			assertTrue(AttributeType.STRING.isBaseType());
		}

		@DisplayName("TIMESTAMP")
		@Test
		public void isBaseType_5() {
			assertTrue(AttributeType.TIMESTAMP.isBaseType());
		}

		@DisplayName("DATE")
		@Test
		public void isBaseType_6() {
			assertFalse(AttributeType.DATE.isBaseType());
		}

		@DisplayName("OBJECT")
		@Test
		public void isBaseType_7() {
			assertFalse(AttributeType.OBJECT.isBaseType());
		}

		@DisplayName("POINT")
		@Test
		public void isBaseType_8() {
			assertFalse(AttributeType.POINT.isBaseType());
		}

		@DisplayName("POINT3D")
		@Test
		public void isBaseType_9() {
			assertFalse(AttributeType.POINT3D.isBaseType());
		}

		@DisplayName("POLYLINE")
		@Test
		public void isBaseType_10() {
			assertFalse(AttributeType.POLYLINE.isBaseType());
		}

		@DisplayName("POLYLINE3D")
		@Test
		public void isBaseType_11() {
			assertFalse(AttributeType.POLYLINE3D.isBaseType());
		}

		@DisplayName("URL")
		@Test
		public void isBaseType_12() {
			assertFalse(AttributeType.URL.isBaseType());
		}

		@DisplayName("URI")
		@Test
		public void isBaseType_13() {
			assertFalse(AttributeType.URI.isBaseType());
		}

		@DisplayName("UUID")
		@Test
		public void isBaseType_14() {
			assertFalse(AttributeType.UUID.isBaseType());
		}

		@DisplayName("INET_ADDRESS")
		@Test
		public void isBaseType_15() {
			assertFalse(AttributeType.INET_ADDRESS.isBaseType());
		}

		@DisplayName("ENUMERATION")
		@Test
		public void isBaseType_16() {
			assertFalse(AttributeType.ENUMERATION.isBaseType());
		}

		@DisplayName("TYPE")
		@Test
		public void isBaseType_17() {
			assertFalse(AttributeType.TYPE.isBaseType());
		}
	}

	@DisplayName("isNullAllowed")
	@Nested
	public class IsNullAllowed {

		@DisplayName("BOOLEAN")
		@Test
		public void isNullAllowed_1() {
			assertFalse(AttributeType.BOOLEAN.isNullAllowed());
		}

		@DisplayName("INTEGER")
		@Test
		public void isNullAllowed_2() {
			assertFalse(AttributeType.INTEGER.isNullAllowed());
		}

		@DisplayName("REAL")
		@Test
		public void isNullAllowed_3() {
			assertFalse(AttributeType.REAL.isNullAllowed());
		}

		@DisplayName("STRING")
		@Test
		public void isNullAllowed_4() {
			assertFalse(AttributeType.STRING.isNullAllowed());
		}

		@DisplayName("TIMESTAMP")
		@Test
		public void isNullAllowed_5() {
			assertFalse(AttributeType.TIMESTAMP.isNullAllowed());
		}

		@DisplayName("DATE")
		@Test
		public void isNullAllowed_6() {
			assertFalse(AttributeType.DATE.isNullAllowed());
		}

		@DisplayName("OBJECT")
		@Test
		public void isNullAllowed_7() {
			assertTrue(AttributeType.OBJECT.isNullAllowed());
		}

		@DisplayName("POINT")
		@Test
		public void isNullAllowed_8() {
			assertFalse(AttributeType.POINT.isNullAllowed());
		}

		@DisplayName("POINT3D")
		@Test
		public void isNullAllowed_9() {
			assertFalse(AttributeType.POINT3D.isNullAllowed());
		}

		@DisplayName("POLYLINE")
		@Test
		public void isNullAllowed_10() {
			assertFalse(AttributeType.POLYLINE.isNullAllowed());
		}

		@DisplayName("POLYLINE3D")
		@Test
		public void isNullAllowed_11() {
			assertFalse(AttributeType.POLYLINE3D.isNullAllowed());
		}

		@DisplayName("URL")
		@Test
		public void isNullAllowed_12() {
			assertTrue(AttributeType.URL.isNullAllowed());
		}

		@DisplayName("URI")
		@Test
		public void isNullAllowed_13() {
			assertTrue(AttributeType.URI.isNullAllowed());
		}

		@DisplayName("UUID")
		@Test
		public void isNullAllowed_14() {
			assertFalse(AttributeType.UUID.isNullAllowed());
		}

		@DisplayName("INET_ADDRESS")
		@Test
		public void isNullAllowed_15() {
			assertTrue(AttributeType.INET_ADDRESS.isNullAllowed());
		}

		@DisplayName("ENUMERATION")
		@Test
		public void isNullAllowed_16() {
			assertTrue(AttributeType.ENUMERATION.isNullAllowed());
		}

		@DisplayName("TYPE")
		@Test
		public void isNullAllowed_17() {
			assertFalse(AttributeType.TYPE.isNullAllowed());
		}
	}

	@DisplayName("getDefaultValue")
	@Nested
	public class GetDefaultValue {

		@DisplayName("BOOLEAN")
		@Test
		public void getDefaultValue_1() throws Exception {
			assertEquals(Boolean.FALSE, AttributeType.BOOLEAN.getDefaultValue());
		}

		@DisplayName("INTEGER")
		@Test
		public void getDefaultValue_2() throws Exception {
			assertEquals(Long.valueOf(0l), AttributeType.INTEGER.getDefaultValue());
		}

		@DisplayName("REAL")
		@Test
		public void getDefaultValue_3() throws Exception {
			assertEquals(Double.valueOf(0.), AttributeType.REAL.getDefaultValue());
		}

		@DisplayName("STRING")
		@Test
		public void getDefaultValue_4() throws Exception {
			assertEquals(new String(), AttributeType.STRING.getDefaultValue());
		}

		@DisplayName("TIMESTAMP")
		@Test
		public void getDefaultValue_5() throws Exception {
			assertNotNull(AttributeType.TIMESTAMP.getDefaultValue());
		}

		@DisplayName("DATE")
		@Test
		public void getDefaultValue_6() throws Exception {
			assertNotNull(AttributeType.DATE.getDefaultValue());
		}

		@DisplayName("OBJECT")
		@Test
		public void getDefaultValue_7() throws Exception {
			assertNull(AttributeType.OBJECT.getDefaultValue());
		}

		@DisplayName("POINT")
		@Test
		public void getDefaultValue_8() throws Exception {
			assertEquals(new Point2d(), AttributeType.POINT.getDefaultValue());
		}

		@DisplayName("POINT3D")
		@Test
		public void getDefaultValue_9() throws Exception {
			assertEquals(new Point3d(), AttributeType.POINT3D.getDefaultValue());
		}

		@DisplayName("POLYLINE")
		@Test
		public void getDefaultValue_10() throws Exception {
			assertTrue(Arrays.equals(new Point2D[0], (Point2D[])AttributeType.POLYLINE.getDefaultValue()));
		}

		@DisplayName("POLYLINE3D")
		@Test
		public void getDefaultValue_11() throws Exception {
			assertTrue(Arrays.equals(new Point3D[0], (Point3D[])AttributeType.POLYLINE3D.getDefaultValue()));
		}

		@DisplayName("URL")
		@Test
		public void getDefaultValue_12() throws Exception {
			assertNull(AttributeType.URL.getDefaultValue());
		}

		@DisplayName("URI")
		@Test
		public void getDefaultValue_13() throws Exception {
			assertNull(AttributeType.URI.getDefaultValue());
		}

		@DisplayName("UUID")
		@Test
		public void getDefaultValue_14() throws Exception {
			assertEquals(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), AttributeType.UUID.getDefaultValue());  //$NON-NLS-1$
		}

		@DisplayName("INET_ADDRESS")
		@Test
		public void getDefaultValue_15() throws Exception {
			assertEquals(InetAddress.getLocalHost(), AttributeType.INET_ADDRESS.getDefaultValue());
		}

		@DisplayName("ENUMERATION")
		@Test
		public void getDefaultValue_16() throws Exception {
			assertNull(AttributeType.ENUMERATION.getDefaultValue());
		}

		@DisplayName("TYPE")
		@Test
		public void getDefaultValue_17() throws Exception {
			assertEquals(Object.class, AttributeType.TYPE.getDefaultValue());
		}
	}

	@DisplayName("cast")
	@Nested
	class Cast {

		private char vChar;
		private byte vByte;
		private short vShort;
		private int vInt;
		private long vLong;
		private float vFloat;
		private double vDouble;
		private String vStr;
		private StringBuilder vStrB;
		private Calendar cal;
		private Date dt;
		private Point2d pt2d;
		private UUID uuid;
		private Point3D pt3d;
		private Point2D[] tabpt2d;
		private Point3D[] tabpt3d;
		private int[] tabint;
		private Object obj;
		private URL url;
		private URI uri;
		private InetAddress ipAddress;
		private InetAddress worldAddress;
		private String vIpStr;
		private AttributeType enumeration;
		private String vEnumStr;

		@BeforeEach
		public void setUp() throws Exception {
			vChar = 'c';
			vByte = 1;
			vShort = 1;
			vInt = 1;
			vLong = 1;
			vFloat = 1f;
			vDouble = 1.;
			vStr = "Hello";  //$NON-NLS-1$
			vStrB = new StringBuilder("www.arakhne.org");  //$NON-NLS-1$
			cal = Calendar.getInstance();
			dt = new Date();
			pt2d = new Point2d(0,0);
			uuid = UUID.nameUUIDFromBytes("abcd".getBytes());  //$NON-NLS-1$
			pt3d = new Point3d(0,0,0);
			tabpt2d = new Point2D[0];
			tabpt3d = new Point3D[0];
			tabint = new int[0];
			obj = Locale.getDefault();
			url = new URI("http://www.google.com").toURL();  //$NON-NLS-1$
			uri = new URI("http://www.google.com");  //$NON-NLS-1$
			ipAddress = InetAddress.getLocalHost();
			worldAddress = InetAddress.getByName(vStrB.toString());
			vIpStr = ipAddress.toString();
			enumeration = randomEnum(AttributeType.class);
			vEnumStr = AttributeType.class.getCanonicalName()+"."+AttributeType.values()[1].name();  //$NON-NLS-1$
		}

		@DisplayName("To boolean")
		@Nested
		public class ToBoolean {

			private final AttributeType type = AttributeType.BOOLEAN;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertEquals(Boolean.TRUE, type.cast(true));
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertEquals(Boolean.FALSE, type.cast(false));
			}

			@DisplayName("(char)")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("(byte)")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("(short)")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("(int)")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("(long)")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("(float)")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("(double)")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("(string)")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("(url in string)")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("(calendar)")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("(date)")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("(uuid)")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("(point)")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("(point3d)")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("(point[])")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("(point3d[])")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("(int[])")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("(object)")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("(url)")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("(uri)")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("(ip)")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("(enumeration)")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}

		@DisplayName("To uuid")
		@Nested
		public class ToUuid {

			private final AttributeType type = AttributeType.UUID;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("(char)")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("(byte)")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("(short)")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("(int)")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("(long)")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("(float)")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("(double)")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("(string)")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("(url in string)")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("(calendar)")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("(date)")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("(uuid)")
			@Test
			public void test_15() throws Exception {
				assertSame(uuid,type.cast(uuid));
			}

			@DisplayName("(point)")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("(point3d)")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("(point[])")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("(point3d[])")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("(int[])")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("(object)")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("(url)")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("(uri)")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("(ip)")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("(enumeration)")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}

		@DisplayName("To date")
		@Nested
		public class ToDate {

			private final AttributeType type = AttributeType.DATE;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("(char)")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("(byte)")
			@Test
			public void test_6() throws Exception {
				assertEquals(new Date(vByte),type.cast(vByte));
			}

			@DisplayName("(short)")
			@Test
			public void test_7() throws Exception {
				assertEquals(new Date(vShort),type.cast(vShort));
			}

			@DisplayName("(int)")
			@Test
			public void test_8() throws Exception {
				assertEquals(new Date(vInt),type.cast(vInt));
			}

			@DisplayName("(long)")
			@Test
			public void test_9() throws Exception {
				assertEquals(new Date(vLong),type.cast(vLong));
			}

			@DisplayName("(float)")
			@Test
			public void test_11() throws Exception {
				assertEquals(new Date((long)vFloat),type.cast(vFloat));
			}

			@DisplayName("(double)")
			@Test
			public void test_12() throws Exception {
				assertEquals(new Date((long)vDouble),type.cast(vDouble));
			}

			@DisplayName("(string)")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("(url in string)")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("(calendar)")
			@Test
			public void test_17() throws Exception {
				assertEquals(cal.getTime(),type.cast(cal));
			}

			@DisplayName("(date)")
			@Test
			public void test_18() throws Exception {
				assertSame(dt,type.cast(dt));
			}

			@DisplayName("(uuid)")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("(point)")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("(point3d)")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("(point[])")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("(point3d[])")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("(int[])")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("(object)")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("(url)")
			@Test
			public void test_26() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("(uri)")
			@Test
			public void test_28() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("(ip)")
			@Test
			public void test_29() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("(enumeration)")
			@Test
			public void test_30() throws Exception {
				assertCastException(type,enumeration);
			}
		}

		@DisplayName("To integer")
		@Nested
		public class ToInteger {

			private final AttributeType type = AttributeType.INTEGER;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("(char)")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("(byte)")
			@Test
			public void test_5() throws Exception {
				assertEquals((long)vByte,type.cast(vByte));
			}

			@DisplayName("(short)")
			@Test
			public void test_6() throws Exception {
				assertEquals((long)vShort,type.cast(vShort));
			}

			@DisplayName("(int)")
			@Test
			public void test_7() throws Exception {
				assertEquals((long)vInt,type.cast(vInt));
			}

			@DisplayName("(long)")
			@Test
			public void test_8() throws Exception {
				assertEquals(vLong,type.cast(vLong));
			}

			@DisplayName("(float)")
			@Test
			public void test_9() throws Exception {
				assertEquals((long)vFloat,type.cast(vFloat));
			}

			@DisplayName("(double)")
			@Test
			public void test_10() throws Exception {
				assertEquals((long)vDouble,type.cast(vDouble));
			}

			@DisplayName("(string)")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("(url in string)")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("(calendar)")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("(date)")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("(uuid)")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("(point)")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("(point3d)")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("(point[])")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("(point3d[])")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("(int[])")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("(object)")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("(url)")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("(uri)")
			@Test
			public void test_26() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("(ip)")
			@Test
			public void test_27() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("(enumeration)")
			@Test
			public void test_28() throws Exception {
				assertEquals((long)enumeration.ordinal(), type.cast(enumeration));
			}
		}

		@DisplayName("To object")
		@Nested
		public class ToObject {

			private final AttributeType type = AttributeType.OBJECT;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertEquals(new NullAttribute(type),type.cast(null));
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertEquals(true,type.cast(true));
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertEquals(false,type.cast(false));
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertEquals(vChar,type.cast(vChar));
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertEquals(vByte,type.cast(vByte));
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertEquals(vShort,type.cast(vShort));
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertEquals(vInt,type.cast(vInt));
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertEquals(vLong,type.cast(vLong));
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertEquals(vFloat, type.cast(vFloat));
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertEquals(vDouble,type.cast(vDouble));
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertEquals(vStr,type.cast(vStr));
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertEquals(vStrB,type.cast(vStrB));
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertSame(cal,type.cast(cal));
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertSame(dt,type.cast(dt));
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertSame(uuid,type.cast(uuid));
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertSame(pt2d,type.cast(pt2d));
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertSame(pt3d,type.cast(pt3d));
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertSame(tabpt2d,type.cast(tabpt2d));
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertSame(tabpt3d,type.cast(tabpt3d));
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertSame(tabint,type.cast(tabint));
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertSame(obj,type.cast(obj));
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertSame(url,type.cast(url));
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertSame(uri,type.cast(uri));
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertSame(ipAddress, type.cast(ipAddress));
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertSame(enumeration,type.cast(enumeration));
			}
		}

		@DisplayName("To point")
		@Nested
		public class ToPoint {

			private final AttributeType type = AttributeType.POINT;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}
			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertSame(pt2d,type.cast(pt2d));
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}
			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}

		@DisplayName("To point3d")
		@Nested
		public class ToPoint3d {

			private final AttributeType type = AttributeType.POINT3D;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertSame(pt3d,type.cast(pt3d));
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}


		@DisplayName("To polyline")
		@Nested
		public class ToPolyline {

			private final AttributeType type = AttributeType.POLYLINE;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertSame(tabpt2d,type.cast(tabpt2d));
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}


		@DisplayName("To polyline3d")
		@Nested
		public class ToPolyline3d {

			private final AttributeType type = AttributeType.POLYLINE3D;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertSame(tabpt3d,type.cast(tabpt3d));
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}


		@DisplayName("To real")
		@Nested
		public class ToReal {

			private final AttributeType type = AttributeType.REAL;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertEquals((double)vByte,type.cast(vByte));
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertEquals((double)vShort,type.cast(vShort));
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertEquals((double)vInt,type.cast(vInt));
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertEquals((double)vLong,type.cast(vLong));
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertEquals((double)vFloat,type.cast(vFloat));
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertEquals(vDouble,type.cast(vDouble));
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertEquals((double)enumeration.ordinal(), type.cast(enumeration));
			}
		}


		@DisplayName("To string")
		@Nested
		public class ToString {

			private final AttributeType type = AttributeType.STRING;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertEquals(type.getDefaultValue(),type.cast(null));
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertEquals(Boolean.toString(true),type.cast(true));
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertEquals(Boolean.toString(false),type.cast(false));
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertEquals(Character.toString(vChar),type.cast(vChar));
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertEquals(Byte.toString(vByte),type.cast(vByte));
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertEquals(Short.toString(vShort),type.cast(vShort));
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertEquals(Integer.toString(vInt),type.cast(vInt));
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertEquals(Long.toString(vLong),type.cast(vLong));
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertEquals(Float.toString(vFloat),type.cast(vFloat));
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertEquals(Double.toString(vDouble),type.cast(vDouble));
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertEquals(vStr,type.cast(vStr));
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertEquals(vStrB.toString(),type.cast(vStrB));
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertEquals(cal.toString(),type.cast(cal));
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertEquals(dt.toString(),type.cast(dt));
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertEquals(uuid.toString(),type.cast(uuid));
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertEquals(pt2d.toString(),type.cast(pt2d));
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertEquals(pt3d.toString(),type.cast(pt3d));
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertEquals(tabpt2d.toString(),type.cast(tabpt2d));
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertEquals(tabpt3d.toString(),type.cast(tabpt3d));
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertEquals(tabint.toString(),type.cast(tabint));
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertEquals(obj.toString(),type.cast(obj));
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertEquals(url.toString(), type.cast(url));
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertEquals(uri.toString(), type.cast(uri));
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertEquals(ipAddress.toString(), type.cast(ipAddress));
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertEquals(enumeration.getClass().getCanonicalName()+"."+enumeration.name(), type.cast(enumeration));  //$NON-NLS-1$
			}
		}	

		@DisplayName("To timestamp")
		@Nested
		public class ToTimestamp {

			private final AttributeType type = AttributeType.TIMESTAMP;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertNullException(type,null);
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertEquals(new Timestamp(vByte),type.cast(vByte));
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertEquals(new Timestamp(vShort),type.cast(vShort));
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertEquals(new Timestamp(vInt),type.cast(vInt));
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertEquals(new Timestamp(vLong),type.cast(vLong));
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertEquals(new Timestamp((int)vFloat),type.cast(vFloat));
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertEquals(new Timestamp((int)vDouble),type.cast(vDouble));
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertEquals(cal.getTimeInMillis(),type.cast(cal));
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertEquals(dt.getTime(),type.cast(dt));
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}


		@DisplayName("To uri")
		@Nested
		public class ToUri {

			private final AttributeType type = AttributeType.URI;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertEquals(new NullAttribute(type),type.cast(null));
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertEquals(url.toURI(), type.cast(url));
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertSame(uri, type.cast(uri));
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertEquals(new URI(AttributeConstants.DEFAULT_SCHEME.name(), ipAddress.getHostAddress(), ""), type.cast(ipAddress));  //$NON-NLS-1$
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}


		@DisplayName("To url")
		@Nested
		public class ToUrl {

			private final AttributeType type = AttributeType.URL;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertEquals(new NullAttribute(type),type.cast(null));
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("url in str")
			@Test
			public void test_12() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_22() throws Exception {
				assertSame(url, type.cast(url));
			}

			@DisplayName("uri")
			@Test
			public void test_23() throws Exception {
				assertEquals(uri.toURL(), type.cast(uri));
			}

			@DisplayName("ip")
			@Test
			public void test_24() throws Exception {
				assertEquals(new URI(AttributeConstants.DEFAULT_SCHEME.name(), ipAddress.getHostAddress(), "", "").toURL(), type.cast(ipAddress));  //$NON-NLS-1$
			}

			@DisplayName("enumeration")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,enumeration);
			}
		}


		@DisplayName("To ip")
		@Nested
		public class ToIp {

			private final AttributeType type = AttributeType.INET_ADDRESS;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertEquals(new NullAttribute(type),type.cast(null));
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertNull(type.cast(vStr));
			}

			@DisplayName("ip in str")
			@Test
			public void test_12() throws Exception {
				assertEquals(ipAddress,type.cast(vIpStr));
			}

			@DisplayName("url in str")
			@Test
			public void test_13() throws Exception {
				assertEquals(worldAddress, type.cast(vStrB));
			}

			@DisplayName("calendar")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_23() throws Exception {
				assertEquals(InetAddress.getByName("www.google.com"), type.cast(url));  //$NON-NLS-1$
			}

			@DisplayName("uri")
			@Test
			public void test_24() throws Exception {
				assertEquals(InetAddress.getByName("www.google.com"), type.cast(uri));  //$NON-NLS-1$
			}

			@DisplayName("ip")
			@Test
			public void test_25() throws Exception {
				assertSame(ipAddress, type.cast(ipAddress));
			}

			@DisplayName("enumeration")
			@Test
			public void test_26() throws Exception {
				assertCastException(type,enumeration);
			}
		}


		@DisplayName("To enumeration")
		@Nested
		public class ToEnumeration {

			private final AttributeType type = AttributeType.ENUMERATION;

			@DisplayName("null")
			@Test
			public void test_1() throws Exception {
				assertEquals(new NullAttribute(type),type.cast(null));
			}

			@DisplayName("true")
			@Test
			public void test_2() throws Exception {
				assertCastException(type,true);
			}

			@DisplayName("false")
			@Test
			public void test_3() throws Exception {
				assertCastException(type,false);
			}

			@DisplayName("char")
			@Test
			public void test_4() throws Exception {
				assertCastException(type,vChar);
			}

			@DisplayName("byte")
			@Test
			public void test_5() throws Exception {
				assertCastException(type,vByte);
			}

			@DisplayName("short")
			@Test
			public void test_6() throws Exception {
				assertCastException(type,vShort);
			}

			@DisplayName("int")
			@Test
			public void test_7() throws Exception {
				assertCastException(type,vInt);
			}

			@DisplayName("long")
			@Test
			public void test_8() throws Exception {
				assertCastException(type,vLong);
			}

			@DisplayName("float")
			@Test
			public void test_9() throws Exception {
				assertCastException(type,vFloat);
			}

			@DisplayName("double")
			@Test
			public void test_10() throws Exception {
				assertCastException(type,vDouble);
			}

			@DisplayName("str")
			@Test
			public void test_11() throws Exception {
				assertCastException(type,vStr);
			}

			@DisplayName("enum in str")
			@Test
			public void test_12() throws Exception {
				assertSame(AttributeType.values()[1],type.cast(vEnumStr));
			}

			@DisplayName("url in str")
			@Test
			public void test_13() throws Exception {
				assertCastException(type,vStrB);
			}

			@DisplayName("calendar")
			@Test
			public void test_14() throws Exception {
				assertCastException(type,cal);
			}

			@DisplayName("date")
			@Test
			public void test_15() throws Exception {
				assertCastException(type,dt);
			}

			@DisplayName("uuid")
			@Test
			public void test_16() throws Exception {
				assertCastException(type,uuid);
			}

			@DisplayName("point")
			@Test
			public void test_17() throws Exception {
				assertCastException(type,pt2d);
			}

			@DisplayName("point3d")
			@Test
			public void test_18() throws Exception {
				assertCastException(type,pt3d);
			}

			@DisplayName("point[]")
			@Test
			public void test_19() throws Exception {
				assertCastException(type,tabpt2d);
			}

			@DisplayName("point3d[]")
			@Test
			public void test_20() throws Exception {
				assertCastException(type,tabpt3d);
			}

			@DisplayName("int[]")
			@Test
			public void test_21() throws Exception {
				assertCastException(type,tabint);
			}

			@DisplayName("object")
			@Test
			public void test_22() throws Exception {
				assertCastException(type,obj);
			}

			@DisplayName("url")
			@Test
			public void test_23() throws Exception {
				assertCastException(type,url);
			}

			@DisplayName("uri")
			@Test
			public void test_24() throws Exception {
				assertCastException(type,uri);
			}

			@DisplayName("ip")
			@Test
			public void test_25() throws Exception {
				assertCastException(type,ipAddress);
			}

			@DisplayName("enumeration")
			@Test
			public void test_26() throws Exception {
				assertSame(enumeration,type.cast(enumeration));
			}
		}
	}

	@DisplayName("isAssignableFrom")
	@Nested
	public class IsAssignableFrom {

		@DisplayName("To boolean")
		@Nested
		public class ToBoolean {

			@DisplayName("From boolean")
			@Test
			public void test_1() {
				assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To date")
		@Nested
		public class ToDate {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To integer")
		@Nested
		public class ToInteger {

			@DisplayName("INTEGER")
			@Test
			public void test_1() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To object")
		@Nested
		public class ToObject {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To point")
		@Nested
		public class ToPoint {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To point3d")
		@Nested
		public class ToPoint3d {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To polyline")
		@Nested
		public class ToPolyline {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To polyline3d")
		@Nested
		public class ToPolyline3d {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To real")
		@Nested
		public class ToReal {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To string")
		@Nested
		public class ToString {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To timestamp")
		@Nested
		public class ToTimestamp {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To uri")
		@Nested
		public class ToUri {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To url")
		@Nested
		public class ToUrl {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To uuid")
		@Nested
		public class ToUuid {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("UUID")
			@Test
			public void test_14() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.UUID));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_15() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_16() {
				assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To Ip")
		@Nested
		public class ToIp {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_14() {
				assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_15() {
				assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}

		@DisplayName("To enumeration")
		@Nested
		public class ToEnumeration {

			@DisplayName("BOOLEAN")
			@Test
			public void test_1() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.BOOLEAN));
			}

			@DisplayName("DATE")
			@Test
			public void test_2() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.DATE));
			}

			@DisplayName("INTEGER")
			@Test
			public void test_3() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.INTEGER));
			}

			@DisplayName("OBJECT")
			@Test
			public void test_4() {
				assertTrue(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.OBJECT));
			}

			@DisplayName("POINT")
			@Test
			public void test_5() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POINT));
			}

			@DisplayName("POINT3D")
			@Test
			public void test_6() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POINT3D));
			}

			@DisplayName("POLYLINE")
			@Test
			public void test_7() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POLYLINE));
			}

			@DisplayName("POLYLINE3D")
			@Test
			public void test_8() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POLYLINE3D));
			}

			@DisplayName("REAL")
			@Test
			public void test_9() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.REAL));
			}

			@DisplayName("STRING")
			@Test
			public void test_10() {
				assertTrue(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.STRING));
			}

			@DisplayName("TIMESTAMP")
			@Test
			public void test_11() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.TIMESTAMP));
			}

			@DisplayName("URI")
			@Test
			public void test_12() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.URI));
			}

			@DisplayName("URL")
			@Test
			public void test_13() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.URL));
			}

			@DisplayName("INET_ADDRESS")
			@Test
			public void test_14() {
				assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.INET_ADDRESS));
			}

			@DisplayName("ENUMERATION")
			@Test
			public void test_15() {
				assertTrue(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.ENUMERATION));
			}
		}
	}

	@DisplayName("getLocalizedName")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(AttributeType.class)
	public void getLocalizedName(AttributeType type) {
		String name = type.getLocalizedName();
		assertNotEquals("OTHER", name, () -> "Invalid attribute name for " + type.name()); //$NON-NLS-1$ //$NON-NLS-2$
	}

}

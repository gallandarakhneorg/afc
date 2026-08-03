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

package org.arakhne.afc.inputoutput.xml;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.common.io.Files;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.text.TextUtil;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.OperatingSystem;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@DisplayName("XMLUtil")
@SuppressWarnings("all")
public class XMLUtilTest extends AbstractTestCase {

	private final static URL url = Resources.getResource(XMLUtilTest.class, "test.xml"); //$NON-NLS-1$

	private final static URL url2 = Resources.getResource(XMLUtilTest.class, "test2.xml"); //$NON-NLS-1$

	private final static URL url3 = Resources.getResource(XMLUtilTest.class, "test3.xml"); //$NON-NLS-1$

	private final static URL url4 = Resources.getResource(XMLUtilTest.class, "test4.xml"); //$NON-NLS-1$

	private Document document;

	@BeforeEach
	public void setUp() throws Exception {
		assertNotNull(url, "testing resource not found"); //$NON-NLS-1$
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		try (InputStream is = url.openStream()) {
			document = builder.parse(is);
		}
	}

	@AfterEach
	public void tearDown() {
		document = null;
	}

	@DisplayName("parseColor")
	@Nested
	public class ParseColor {

		@DisplayName("Valid #1")
		@Test
		public void valid_1() throws Exception {
			assertHexEquals(0xFF0000FF, XMLUtil.parseColor("blue")); //$NON-NLS-1$
		}

		@DisplayName("Valid #2")
		@Test
		public void valid_2() throws Exception {
			assertHexEquals(0xFFFF0000, XMLUtil.parseColor("red")); //$NON-NLS-1$
		}

		@DisplayName("Valid #3")
		@Test
		public void valid_3() throws Exception {
			assertHexEquals(0xFFFF0022, XMLUtil.parseColor("rgb(255, 0, 34)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #4")
		@Test
		public void valid_4() throws Exception {
			assertHexEquals(0xFF00FF01, XMLUtil.parseColor("#00FF01")); //$NON-NLS-1$
		}

		@DisplayName("Valid #5")
		@Test
		public void valid_5() throws Exception {
			assertHexEquals(0x3400FF01, XMLUtil.parseColor("#3400FF01")); //$NON-NLS-1$
		}

		@DisplayName("Valid #6")
		@Test
		public void valid_6() throws Exception {
			assertHexEquals(0x04FF0022, XMLUtil.parseColor("rgba(255, 0, 34, 4)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #7")
		@Test
		public void valid_7() throws Exception {
			assertHexEquals(0xFF333333, XMLUtil.parseColor("hsl(0.7, 0, 0.2)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #8")
		@Test
		public void valid_8() throws Exception {
			assertHexEquals(0x04333333, XMLUtil.parseColor("hsla(0.7, 0, 0.2, 4)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #9")
		@Test
		public void valid_9() throws Exception {
			assertHexEquals(0xFF333333, XMLUtil.parseColor("hsl(70%, 0%, 0.2)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #10")
		@Test
		public void valid_10() throws Exception {
			assertHexEquals(0x04333333, XMLUtil.parseColor("hsla(70%, 0%, 0.2, 4)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #11")
		@Test
		public void valid_11() throws Exception {
			assertHexEquals(0xFF23194C, XMLUtil.parseColor("hsl(0.7, .5, 0.2)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #12")
		@Test
		public void valid_12() throws Exception {
			assertHexEquals(0x0423194C, XMLUtil.parseColor("hsla(0.7, .5, 0.2, 4)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #13")
		@Test
		public void valid_13() throws Exception {
			assertHexEquals(0xFF23194C, XMLUtil.parseColor("hsl(70%, 50%, 0.2)")); //$NON-NLS-1$
		}

		@DisplayName("Valid #14")
		@Test
		public void valid_14() throws Exception {
			assertHexEquals(0x0423194C, XMLUtil.parseColor("hsla(70%, 50%, 0.2, 4)")); //$NON-NLS-1$
		}

		@DisplayName("Invalid #1")
		@Test
		public void invalid_1() throws Exception {
			assertThrows(ColorFormatException.class, () -> assertHexEquals(0, XMLUtil.parseColor("0x00FF01"))); //$NON-NLS-1$
		}

		@DisplayName("Invalid #2")
		@Test
		public void invalid_2() throws Exception {
			assertThrows(ColorFormatException.class, () -> assertHexEquals(0, XMLUtil.parseColor("0xFF00FF01"))); //$NON-NLS-1$
		}

		@DisplayName("Invalid #3")
		@Test
		public void invalid_3() throws Exception {
			assertThrows(ColorFormatException.class, () -> assertHexEquals(0, XMLUtil.parseColor("1"))); //$NON-NLS-1$
		}
	}

	@DisplayName("parseDate")
	@Nested
	public class ParseDate {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			Date base = dt.parse("2017-11-12 12:14:15"); //$NON-NLS-1$
			assertEquals(base, XMLUtil.parseDate("2017-11-12T12:14:15")); //$NON-NLS-1$
		}
	}

	@DisplayName("parseString")
	@Nested
	public class ParseString {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			String b64 = Base64.getEncoder().encodeToString("hello".getBytes()); //$NON-NLS-1$
			assertEquals("hello", new String(XMLUtil.parseString(b64))); //$NON-NLS-1$
		}
	}

	@DisplayName("parseObject")
	@Nested
	public class ParseObject {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			Object obj = Integer.valueOf(4);
			String enc;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
					oos.writeObject(obj);
				}
				enc = Base64.getEncoder().encodeToString(baos.toByteArray());
			}
			Object obj2 = XMLUtil.parseObject(enc);
			assertNotSame(obj, obj2);
			assertEquals(obj, obj2);
		}
	}

	@DisplayName("toColor")
	@Nested
	public class ToColor {

		@DisplayName("(int) #1")
		@Test
		public void int_1() throws Exception {
			assertEquals("#45123456", XMLUtil.toColor(0x45123456)); //$NON-NLS-1$
		}

		@DisplayName("(int) #2")
		@Test
		public void int_2() throws Exception {
			assertEquals("lime", XMLUtil.toColor(0xFF00FF00)); //$NON-NLS-1$
		}

		@DisplayName("(int,int,int,int) #1")
		@Test
		public void toColorIntIntIntInt_1() {
			assertEquals("#45123456", XMLUtil.toColor(18, 52, 86, 69)); //$NON-NLS-1$
		}

		@DisplayName("(int,int,int,int) #2")
		@Test
		public void toColorIntIntIntInt_2() {
			assertEquals("lime", XMLUtil.toColor(0, 255, 0, 255)); //$NON-NLS-1$
		}
	}

	@DisplayName("toString")
	@Nested
	public class ToString {

		@DisplayName("(byte[]) #1")
		@Test
		public void toStringByteArray_1() {
			String b64 = Base64.getEncoder().encodeToString("hello".getBytes()); //$NON-NLS-1$
			assertEquals(b64, XMLUtil.toString("hello".getBytes())); //$NON-NLS-1$
		}

		@DisplayName("(Date) #1")
		@Test
		public void toStringDate_1() throws Exception {
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			Date base = dt.parse("2017-11-12 12:14:15"); //$NON-NLS-1$
			assertEquals("2017-11-12T12:14:15", XMLUtil.toString(base)); //$NON-NLS-1$
		}

		@DisplayName("(Serializable) #1")
		@Test
		public void toStringSerializable() throws Exception {
			Serializable obj = Integer.valueOf(4);
			String b64;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
					oos.writeObject(obj);
				}
				b64 = Base64.getEncoder().encodeToString(baos.toByteArray());
			}
			assertEquals(b64, XMLUtil.toString(obj));
		}

		@DisplayName("(Node) #1")
		@Test
		public void toStringNode() {
			Document doc = XMLUtil.parseXML("<a><b id = \"v\"/></a>"); //$NON-NLS-1$
			assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><a><b id=\"v\"/></a>", XMLUtil.toString(doc)); //$NON-NLS-1$
		}
	}

	@DisplayName("getAttributeBoolean")
	@Nested
	public class GetAttributeBoolean {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_1() {
			assertTrue(XMLUtil.getAttributeBoolean(document, true, "catalog", "book", "author", "fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_2() {
			assertFalse(XMLUtil.getAttributeBoolean(document, true, "catalog", "book", "title", "fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_3() {
			assertTrue(XMLUtil.getAttributeBoolean(document, false, "catalog", "book", "author", "fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_4() {
			assertFalse(XMLUtil.getAttributeBoolean(document, false, "catalog", "book", "title", "fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #5")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_5() {
			assertFalse(XMLUtil.getAttributeBoolean(document, true, "catalog", "book", "author", "Fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #6")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_6() {
			assertFalse(XMLUtil.getAttributeBoolean(document, true, "catalog", "book", "title", "Fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #7")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_7() {
			assertFalse(XMLUtil.getAttributeBoolean(document, false, "catalog", "book", "author", "Fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #8")
		@Test
		public void getAttributeBooleanNodeBooleanStringArray_8() {
			assertFalse(XMLUtil.getAttributeBoolean(document, false, "catalog", "book", "title", "Fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeBooleanNodeStringArray_1() {
			assertTrue(XMLUtil.getAttributeBoolean(document, "catalog", "book", "author", "fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeBooleanNodeStringArray_2() {
			assertFalse(XMLUtil.getAttributeBoolean(document, "catalog", "book", "title", "fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #3")
		@Test
		public void getAttributeBooleanNodeStringArray_3() {
			assertFalse(XMLUtil.getAttributeBoolean(document, "catalog", "book", "author", "Fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #4")
		@Test
		public void getAttributeBooleanNodeStringArray_4() {
			assertFalse(XMLUtil.getAttributeBoolean(document, "catalog", "book", "title", "Fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeClass")
	@Nested
	public class GetAttributeClass {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeClassNodeBooleanStringArray_1() {
			assertEquals(Integer.class, XMLUtil.getAttributeClass(document, true, "catalog", "book", "genre", "fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeClassNodeBooleanStringArray_2() {
			assertEquals(Integer.class, XMLUtil.getAttributeClass(document, false, "catalog", "book", "genre", "fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeClassNodeBooleanStringArray_3() {
			assertNull(XMLUtil.getAttributeClass(document, true, "catalog", "book", "genre", "Fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeClassNodeBooleanStringArray_4() {
			assertEquals(Integer.class, XMLUtil.getAttributeClass(document, false, "catalog", "book", "genre", "Fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeClassNodeStringArray_1() {
			assertEquals(Integer.class, XMLUtil.getAttributeClass(document, "catalog", "book", "genre", "fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeClassNodeStringArray_2() {
			assertNull(XMLUtil.getAttributeClass(document, "catalog", "book", "genre", "Fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeColor")
	@Nested
	public class GetAttributeColor {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeColorNodeBooleanStringArray_1() {
			assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(document, true, "catalog", "book", "price", "color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeColorNodeBooleanStringArray_2() {
			assertHexEquals(0, XMLUtil.getAttributeColor(document, true, "catalog", "book", "price", "Color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeColorNodeBooleanStringArray_3() {
			assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(document, false, "catalog", "book", "price", "color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeColorNodeBooleanStringArray_4() {
			assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(document, false, "catalog", "book", "price", "Color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeColorNodeStringArray_1() {
			assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(document, "catalog", "book", "price", "color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeColorNodeStringArray_2() {
			assertHexEquals(0, XMLUtil.getAttributeColor(document, "catalog", "book", "price", "Color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeDate")
	@Nested
	public class GetAttributeDate {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeDateNodeBooleanStringArray_1() throws Exception {
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			Date base = dt.parse("2017-11-12 12:45:47"); //$NON-NLS-1$
			assertEquals(base, XMLUtil.getAttributeDate(document, true, "catalog", "book", "publish", "date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeDateNodeBooleanStringArray_2() throws Exception {
			assertNull(XMLUtil.getAttributeDate(document, true, "catalog", "book", "publish", "Date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeDateNodeBooleanStringArray_3() throws Exception {
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			Date base = dt.parse("2017-11-12 12:45:47"); //$NON-NLS-1$
			assertEquals(base, XMLUtil.getAttributeDate(document, false, "catalog", "book", "publish", "date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeDateNodeBooleanStringArray_4() throws Exception {
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			Date base = dt.parse("2017-11-12 12:45:47"); //$NON-NLS-1$
			assertEquals(base, XMLUtil.getAttributeDate(document, false, "catalog", "book", "publish", "Date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeDateNodeStringArray_1() throws Exception {
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			Date base = dt.parse("2017-11-12 12:45:47"); //$NON-NLS-1$
			assertEquals(base, XMLUtil.getAttributeDate(document, "catalog", "book", "publish", "date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeDateNodeStringArray_2() throws Exception {
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			assertNull(XMLUtil.getAttributeDate(document, "catalog", "book", "publish", "Date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeDouble")
	@Nested
	public class GetAttributeDouble {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeDoubleNodeBooleanStringArray_1() {
			assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(document, true, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeDoubleNodeBooleanStringArray_2() {
			assertEpsilonEquals(0., XMLUtil.getAttributeDouble(document, true, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeDoubleNodeBooleanStringArray_3() {
			assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(document, false, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeDoubleNodeBooleanStringArray_4() {
			assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(document, false, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeDoubleNodeStringArray_1() {
			assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(document, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeDoubleNodeStringArray_2() {
			assertEpsilonEquals(0., XMLUtil.getAttributeDouble(document, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeFloat")
	@Nested
	public class GetAttributeFloat {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeFloatNodeBooleanStringArray_1() {
			assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(document, true, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeFloatNodeBooleanStringArray_2() {
			assertEpsilonEquals(0.f, XMLUtil.getAttributeFloat(document, true, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeFloatNodeBooleanStringArray_3() {
			assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(document, false, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeFloatNodeBooleanStringArray_4() {
			assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(document, false, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeFloatNodeStringArray_1() {
			assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(document, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeFloatNodeStringArray_2() {
			assertEpsilonEquals(0.f, XMLUtil.getAttributeFloat(document, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeInt")
	@Nested
	public class GetAttributeInt {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeIntNodeBooleanStringArray_1() {
			assertEquals(12564, XMLUtil.getAttributeInt(document, true, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeIntNodeBooleanStringArray_2() {
			assertEquals(0, XMLUtil.getAttributeInt(document, true, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeIntNodeBooleanStringArray_3() {
			assertEquals(12564, XMLUtil.getAttributeInt(document, false, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeIntNodeBooleanStringArray_4() {
			assertEquals(12564, XMLUtil.getAttributeInt(document, false, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeIntNodeStringArray_1() {
			assertEquals(12564, XMLUtil.getAttributeInt(document, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeIntNodeStringArray_2() {
			assertEquals(0, XMLUtil.getAttributeInt(document, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeLong")
	@Nested
	public class GetAttributeLong {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeLongNodeBooleanStringArray_1() {
			assertEquals(12564l, XMLUtil.getAttributeLong(document, true, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeLongNodeBooleanStringArray_2() {
			assertEquals(0l, XMLUtil.getAttributeLong(document, true, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeLongNodeBooleanStringArray_3() {
			assertEquals(12564l, XMLUtil.getAttributeLong(document, false, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeLongNodeBooleanStringArray_4() {
			assertEquals(12564l, XMLUtil.getAttributeLong(document, false, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeLongNodeStringArray_1() {
			assertEquals(12564l, XMLUtil.getAttributeLong(document, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeLongNodeStringArray_2() {
			assertEquals(0l, XMLUtil.getAttributeLong(document, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeUUID")
	@Nested
	public class GetAttributeUUID {

		private UUID id1;

		@BeforeEach
		public void setUp() {
			id1 = UUID.fromString("e48d046f-975c-4a22-92ff-cb23b50716ce"); //$NON-NLS-1$
		}
		
		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeUUIDNodeBooleanStringArray_1() {
			assertEquals(id1, XMLUtil.getAttributeUUID(document, true, "catalog", "book", "description", "uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeUUIDNodeBooleanStringArray_2() {
			assertNull(XMLUtil.getAttributeUUID(document, true, "catalog", "book", "description", "Uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeUUIDNodeBooleanStringArray_3() {
			assertEquals(id1, XMLUtil.getAttributeUUID(document, false, "catalog", "book", "description", "uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeUUIDNodeBooleanStringArray_4() {
			assertEquals(id1, XMLUtil.getAttributeUUID(document, false, "catalog", "book", "description", "Uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeUUIDNodeStringArray_1() {
			assertEquals(id1, XMLUtil.getAttributeUUID(document, "catalog", "book", "description", "uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeUUIDNodeStringArray_2() {
			assertNull(XMLUtil.getAttributeUUID(document, "catalog", "book", "description", "Uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeUUIDs")
	@Nested
	public class GetAttributeUUIDs {

		private UUID id1;
		private UUID id2;
		private List<UUID> ids;

		@BeforeEach
		public void setUp() {
			id1 = UUID.fromString("e48d046f-975c-4a22-92ff-cb23b50716ce"); //$NON-NLS-1$
			id2 = UUID.fromString("4f89a62d-04d3-4aec-ab67-8f60bff3d5b0"); //$NON-NLS-1$
			ids = Arrays.asList(id1, id2);
		}
		
		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeUUIDsNodeBooleanStringArray_1() {
			assertEquals(ids, XMLUtil.getAttributeUUIDs(document, true, "catalog", "book", "title", "uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeUUIDsNodeBooleanStringArray_2() {
			assertEquals(Collections.emptyList(), XMLUtil.getAttributeUUIDs(document, true, "catalog", "book", "title", "Uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeUUIDsNodeBooleanStringArray_3() {
			assertEquals(ids, XMLUtil.getAttributeUUIDs(document, false, "catalog", "book", "title", "uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeUUIDsNodeBooleanStringArray_4() {
			assertEquals(ids, XMLUtil.getAttributeUUIDs(document, false, "catalog", "book", "title", "Uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeUUIDsNodeStringArray_1() {
			assertEquals(ids, XMLUtil.getAttributeUUIDs(document, "catalog", "book", "title", "uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeUUIDsNodeStringArray_2() {
			assertEquals(Collections.emptyList(), XMLUtil.getAttributeUUIDs(document, "catalog", "book", "title", "Uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeURL")
	@Nested
	public class GetAttributeURL {

		private URL url;

		@BeforeEach
		public void setUp() throws Exception {
			url = new URI("http://www.arakhne.org").toURL(); //$NON-NLS-1$
		}
		
		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeURLNodeBooleanStringArray_1() throws Exception {
			assertEquals(url, XMLUtil.getAttributeURL(document, true, "catalog", "book", "price", "url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeURLNodeBooleanStringArray_2() throws Exception {
			assertNull(XMLUtil.getAttributeURL(document, true, "catalog", "book", "price", "Url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeURLNodeBooleanStringArray_3() throws Exception {
			assertEquals(url, XMLUtil.getAttributeURL(document, false, "catalog", "book", "price", "url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeURLNodeBooleanStringArray_4() throws Exception {
			assertEquals(url, XMLUtil.getAttributeURL(document, false, "catalog", "book", "price", "Url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeURLNodeStringArray_1() throws Exception {
			assertEquals(url, XMLUtil.getAttributeURL(document, "catalog", "book", "price", "url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeURLNodeStringArray_2() throws Exception {
			assertNull(XMLUtil.getAttributeURL(document, "catalog", "book", "price", "Url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeEnum")
	@Nested
	public class GetAttributeEnum {

		@DisplayName("(Node,Class,boolean,String...) #1")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_1() {
			assertSame(TestEnum.B, XMLUtil.getAttributeEnum(document, TestEnum.class, true, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,String...) #2")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_2() {
			assertNull(XMLUtil.getAttributeEnum(document, TestEnum.class, true, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,String...) #3")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_3() {
			assertSame(TestEnum.B, XMLUtil.getAttributeEnum(document, TestEnum.class, false, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,String...) #4")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_4() {
			assertSame(TestEnum.B, XMLUtil.getAttributeEnum(document, TestEnum.class, false, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,String...) #5")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_5() {
			assertNull(XMLUtil.getAttributeEnum(document, TestEnum.class, true, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,String...) #6")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_6() {
			assertNull(XMLUtil.getAttributeEnum(document, TestEnum.class, true, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,String...) #7")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_7() {
			assertSame(TestEnum.B, XMLUtil.getAttributeEnum(document, TestEnum.class, false, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,String...) #8")
		@Test
		public void getAttributeEnumNodeClassBooleanStringArray_8() {
			assertSame(TestEnum.B, XMLUtil.getAttributeEnum(document, TestEnum.class, false, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	
		@DisplayName("(Node,Class,String...) #1")
		@Test
		public void getAttributeEnumNodeClassStringArray_1() {
			assertSame(TestEnum.B, XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,Class,String...) #2")
		@Test
		public void getAttributeEnumNodeClassStringArray_2() {
			assertNull(XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,Class,String...) #3")
		@Test
		public void getAttributeEnumNodeClassStringArray_3() {
			assertNull(XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@DisplayName("(Node,Class,String...) #4")
		@Test
		public void getAttributeEnumNodeClassStringArray_4() {
			assertNull(XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("setAttributeEnum")
	@Nested
	public class SetAttributeEnum {

		@DisplayName("(Node,Class,boolean,T,String...) #1")
		@Test
		public void setAttributeEnumNodeClassBooleanTStringArray_1() {
			assertTrue(XMLUtil.setAttributeEnum(document, TestEnum.class, true, TestEnum.C, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			assertSame(TestEnum.C, XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,T,String...) #2")
		@Test
		public void setAttributeEnumNodeClassBooleanTStringArray_2() {
			assertTrue(XMLUtil.setAttributeEnum(document, TestEnum.class, true, TestEnum.A, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			assertSame(TestEnum.A, XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			assertSame(TestEnum.B, XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,T,String...) #3")
		@Test
		public void setAttributeEnumNodeClassBooleanTStringArray_3() {
			assertTrue(XMLUtil.setAttributeEnum(document, TestEnum.class, false, TestEnum.C, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			assertSame(TestEnum.C, XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(Node,Class,boolean,T,String...) #4")
		@Test
		public void setAttributeEnumNodeClassBooleanTStringArray_4() {
			assertTrue(XMLUtil.setAttributeEnum(document, TestEnum.class, false, TestEnum.A, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			assertSame(TestEnum.A, XMLUtil.getAttributeEnum(document, TestEnum.class, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			assertSame(TestEnum.A, XMLUtil.getAttributeEnum(document, TestEnum.class, false, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("getAttributeValue")
	@Nested
	public class GetAttributeValue {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getAttributeValueNodeBooleanStringArray_1() {
			assertEquals("B", XMLUtil.getAttributeValue(document, true, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getAttributeValueNodeBooleanStringArray_2() {
			assertEquals("", XMLUtil.getAttributeValue(document, true, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getAttributeValueNodeBooleanStringArray_3() {
			assertEquals("B", XMLUtil.getAttributeValue(document, false, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getAttributeValueNodeBooleanStringArray_4() {
			assertEquals("B", XMLUtil.getAttributeValue(document, false, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		@DisplayName("(Node,boolean,String...) #5")
		@Test
		public void getAttributeValueNodeBooleanStringArray_5() {
			assertEquals("b", XMLUtil.getAttributeValue(document, true, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		@DisplayName("(Node,boolean,String...) #6")
		@Test
		public void getAttributeValueNodeBooleanStringArray_6() {
			assertEquals("", XMLUtil.getAttributeValue(document, true, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		@DisplayName("(Node,boolean,String...) #7")
		@Test
		public void getAttributeValueNodeBooleanStringArray_7() {
			assertEquals("b", XMLUtil.getAttributeValue(document, false, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		@DisplayName("(Node,boolean,String...) #8")
		@Test
		public void getAttributeValueNodeBooleanStringArray_8() {
			assertEquals("b", XMLUtil.getAttributeValue(document, false, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getAttributeValueNodeStringArray_1() {
			assertEquals("B", XMLUtil.getAttributeValue(document, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getAttributeValueNodeStringArray_2() {
			assertEquals("", XMLUtil.getAttributeValue(document, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		
		@DisplayName("(Node,String...) #3")
		@Test
		public void getAttributeValueNodeStringArray_3() {
			assertEquals("b", XMLUtil.getAttributeValue(document, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		
		@DisplayName("(Node,String...) #4")
		@Test
		public void getAttributeValueNodeStringArray_4() {
			assertEquals("", XMLUtil.getAttributeValue(document, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
	}

	@DisplayName("getChild")
	@Nested
	public class GetChild {

		@DisplayName("#1")
		@Test
		public void getChildNodeClass() {
			Element node = XMLUtil.getChild(document, Element.class);
			assertEquals("catalog", node.getTagName()); //$NON-NLS-1$
		}
	}

	@DisplayName("getDocumentFor")
	@Nested
	public class GetDocumentFor {

		@DisplayName("#1")
		@Test
		public void getDocumentForNode() {
			Element node = XMLUtil.getChild(document, Element.class);
			node = XMLUtil.getChild(node, Element.class);
			Node parent = XMLUtil.getDocumentFor(node);
			assertSame(document, parent);
		}
	}

	@DisplayName("getElementFromPath")
	@Nested
	public class GetElementFromPath {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getElementFromPathNodeBooleanStringArray_1() {
			var element = XMLUtil.getElementFromPath(document, true, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getTagName()); //$NON-NLS-1$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getElementFromPathNodeBooleanStringArray_2() {
			var element = XMLUtil.getElementFromPath(document, true, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(element);
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getElementFromPathNodeBooleanStringArray_3() {
			var element = XMLUtil.getElementFromPath(document, false, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getTagName()); //$NON-NLS-1$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getElementFromPathNodeBooleanStringArray_4() {
			var element = XMLUtil.getElementFromPath(document, false, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getTagName()); //$NON-NLS-1$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getElementFromPathNodeStringArray_1() {
			var element = XMLUtil.getElementFromPath(document, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getTagName()); //$NON-NLS-1$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getElementFromPathNodeStringArray_2() {
			var element = XMLUtil.getElementFromPath(document, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(element);
		}
	}

	@DisplayName("getNodeFromPath")
	@Nested
	public class GetNodeFromPath {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getNodeFromPathNodeBooleanStringArray_1() {
			var element = XMLUtil.getNodeFromPath(document, true, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getNodeName()); //$NON-NLS-1$
		}

		@DisplayName("(Node,boolean,String...) #2")
		@Test
		public void getNodeFromPathNodeBooleanStringArray_2() {
			var element = XMLUtil.getNodeFromPath(document, true, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(element);
		}

		@DisplayName("(Node,boolean,String...) #3")
		@Test
		public void getNodeFromPathNodeBooleanStringArray_3() {
			var element = XMLUtil.getNodeFromPath(document, false, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getNodeName()); //$NON-NLS-1$
		}

		@DisplayName("(Node,boolean,String...) #4")
		@Test
		public void getNodeFromPathNodeBooleanStringArray_4() {
			var element = XMLUtil.getNodeFromPath(document, false, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getNodeName()); //$NON-NLS-1$
		}
	
		@DisplayName("(Node,String...) #1")
		@Test
		public void getNodeFromPathNodeStringArray_1() {
			var element = XMLUtil.getNodeFromPath(document, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("book", element.getNodeName()); //$NON-NLS-1$
		}
		
		@DisplayName("(Node,String...) #2")
		@Test
		public void getNodeFromPathNodeStringArray_2() {
			var element = XMLUtil.getNodeFromPath(document, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(element);
		}
	}

	@DisplayName("getText")
	@Nested
	public class GetText {

		@DisplayName("(Node,boolean,String...) #1")
		@Test
		public void getTextNodeStringArray() {
			assertEquals("An in-depth look at creating applications \n      with XML.", //$NON-NLS-1$
					XMLUtil.getText(document, "catalog", "book", "description")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	@DisplayName("iterate")
	@Nested
	public class Iterate {

		@DisplayName("#1")
		@Test
		public void iterateNodeString() {
			Node node;
			Iterator<Node> iterator = XMLUtil.iterate(XMLUtil.getNodeFromPath(document, "catalog"), "book"); //$NON-NLS-1$ //$NON-NLS-2$
			assertTrue(iterator.hasNext());
			node = iterator.next();
			assertEquals("bk101", XMLUtil.getAttributeValue(node, "id")); //$NON-NLS-1$ //$NON-NLS-2$
			assertTrue(iterator.hasNext());
			node = iterator.next();
			assertEquals("bk202", XMLUtil.getAttributeValue(node, "id")); //$NON-NLS-1$ //$NON-NLS-2$
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("parseXML")
	@Nested
	public class ParseXML {

		@DisplayName("#1")
		@Test
		public void parseXML() {
			Document doc = XMLUtil.parseXML("<a><b id = \"v\"/></a>"); //$NON-NLS-1$
			assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("readXML")
	@Nested
	public class ReadXML {

		@DisplayName("(Reader)")
		@Test
		public void readXMLReader() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><a><b id=\"v\"/></a>"; //$NON-NLS-1$
			Document doc;
			try (StringReader reader = new StringReader(source)) {
				doc = XMLUtil.readXML(reader);
			}
			assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(InputStream)")
		@Test
		public void readXMLInputStream() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><a><b id=\"v\"/></a>"; //$NON-NLS-1$
			Document doc;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				doc = XMLUtil.readXML(reader);
			}
			assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(URL)")
		@Test
		public void readXMLURL() throws Exception {
			Document doc = XMLUtil.readXML(url2);
			assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(File)")
		@Test
		public void readXMLFile() throws Exception {
			Document doc = XMLUtil.readXML(FileSystem.convertURLToFile(url2));
			assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(String)")
		@Test
		public void readXMLString() throws Exception {
			Document doc = XMLUtil.readXML(FileSystem.convertURLToFile(url2).getAbsolutePath());
			assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("readXMLFragment")
	@Nested
	public class ReadXMLFragment {

		@DisplayName("(Reader)")
		@Test
		public void readXMLFragmentReader_1() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (StringReader reader = new StringReader(source)) {
				fragment = XMLUtil.readXMLFragment(reader);
			}
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
	
		@DisplayName("(Reader, true)")
		@Test
		public void readXMLFragmentReader_skipRoot_2() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (StringReader reader = new StringReader(source)) {
				fragment = XMLUtil.readXMLFragment(reader, true);
			}
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
		}
	
		@DisplayName("(InputStream)")
		@Test
		public void readXMLFragmentInputStream() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				fragment = XMLUtil.readXMLFragment(reader);
			}
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
	
		@DisplayName("(InputStream, true)")
		@Test
		public void readXMLFragmentInputStream_skipRoot() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				fragment = XMLUtil.readXMLFragment(reader, true);
			}
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
		}
	
		@DisplayName("(URL)")
		@Test
		public void readXMLFragmentURL() throws Exception {
			DocumentFragment fragment = XMLUtil.readXMLFragment(url3);
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
	
		@DisplayName("(URL, true)")
		@Test
		public void readXMLFragmentURL_skipRoot() throws Exception {
			DocumentFragment fragment = XMLUtil.readXMLFragment(url3, true);
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
		}
	
		@DisplayName("(File)")
		@Test
		public void readXMLFragmentFile() throws Exception {
			DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3));
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
	
		@DisplayName("(File, true)")
		@Test
		public void readXMLFragmentFile_skipRoot() throws Exception {
			DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3), true);
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
		}
	
		@DisplayName("(String)")
		@Test
		public void readXMLFragmentString() throws Exception {
			DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3).getAbsolutePath());
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
	
		@DisplayName("(String, true)")
		@Test
		public void readXMLFragmentString_skipRoot() throws Exception {
			DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3).getAbsolutePath(), true);
			assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
		}
	}

	@DisplayName("writeXML")
	@Nested
	public class WriteXML {

		@DisplayName("(Document,OutputStream)")
		@Test
		public void writeXMLDocumentOutputStream() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			Document doc;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				doc = XMLUtil.readXML(reader);
			}
			String actual;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				XMLUtil.writeXML(doc, baos);
				baos.flush();
				actual = new String(baos.toByteArray());
			}
			assertEquals(source, actual);
		}
	
		@DisplayName("(Document,Writer)")
		@Test
		public void writeXMLDocumentWriter() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			Document doc;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				doc = XMLUtil.readXML(reader);
			}
			String actual;
			try (StringWriter sw = new StringWriter()) {
				XMLUtil.writeXML(doc, sw);
				sw.flush();
				actual = sw.toString();
			}
			assertEquals(source, actual);
		}
	
		@DisplayName("(Document,File)")
		@Test
		public void writeXMLDocumentFile() throws Exception  {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			Document doc;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				doc = XMLUtil.readXML(reader);
			}
			File file = File.createTempFile("unittest", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				XMLUtil.writeXML(doc, file);
				String actual = TextUtil.join("\n", Files.readLines(file, Charset.defaultCharset())); //$NON-NLS-1$
				assertEquals(source, actual);
			} finally {
				file.delete();
			}
		}
	
		@DisplayName("(Document,String)")
		@Test
		public void writeXMLDocumentString() throws Exception  {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			Document doc;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				doc = XMLUtil.readXML(reader);
			}
			File file = File.createTempFile("unittest", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				XMLUtil.writeXML(doc, file.getAbsolutePath());
				String actual = TextUtil.join("\n", Files.readLines(file, Charset.defaultCharset())); //$NON-NLS-1$
				assertEquals(source, actual);
			} finally {
				file.delete();
			}
		}
	
		@DisplayName("(Fragment,OutputStream)")
		@Test
		public void writeXMLDocumentFragmentOutputStream() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				fragment = XMLUtil.readXMLFragment(reader, true);
			}
			String actual;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				XMLUtil.writeXML(fragment, baos);
				baos.flush();
				actual = new String(baos.toByteArray());
			}
			final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c>"; //$NON-NLS-1$
			assertEquals(expected, actual);
		}
	
		@DisplayName("(Fragment,Writer)")
		@Test
		public void writeXMLDocumentFragmentWriter() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				fragment = XMLUtil.readXMLFragment(reader, true);
			}
			String actual;
			try (StringWriter sw = new StringWriter()) {
				XMLUtil.writeXML(fragment, sw);
				sw.flush();
				actual = sw.toString();
			}
			final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c>"; //$NON-NLS-1$
			assertEquals(expected, actual);
		}
	
		@DisplayName("(Fragment,File)")
		@Test
		public void writeXMLDocumentFragmentFile() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				fragment = XMLUtil.readXMLFragment(reader, true);
			}
			File file = File.createTempFile("unittest", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				XMLUtil.writeXML(fragment, file);
				String actual = TextUtil.join("\n", Files.readLines(file, Charset.defaultCharset())); //$NON-NLS-1$
				final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c>"; //$NON-NLS-1$
				assertEquals(expected, actual);
			} finally {
				file.delete();
			}
		}
	
		@DisplayName("(Fragment,String)")
		@Test
		public void writeXMLDocumentFragmentString() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			DocumentFragment fragment;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				fragment = XMLUtil.readXMLFragment(reader, true);
			}
			File file = File.createTempFile("unittest", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				XMLUtil.writeXML(fragment, file.getAbsolutePath());
				String actual = TextUtil.join("\n", Files.readLines(file, Charset.defaultCharset())); //$NON-NLS-1$
				final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c>"; //$NON-NLS-1$
				assertEquals(expected, actual);
			} finally {
				file.delete();
			}
		}
	
		@DisplayName("(Node,OutputStream)")
		@Test
		public void writeXMLNodeOutputStream() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			Document doc;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				doc = XMLUtil.readXML(reader);
			}
			String actual;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				XMLUtil.writeXML(doc.getFirstChild().getFirstChild().getFirstChild(), baos);
				baos.flush();
				actual = new String(baos.toByteArray());
			}
			final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><b id=\"v\"/>"; //$NON-NLS-1$
			assertEquals(expected, actual);
		}
	
		@DisplayName("(Node,Writer)")
		@Test
		public void writeXMLNodeWriter() throws Exception {
			final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
			Document doc;
			try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
				doc = XMLUtil.readXML(reader);
			}
			String actual;
			try (StringWriter sw = new StringWriter()) {
				XMLUtil.writeXML(doc.getFirstChild().getFirstChild().getFirstChild(), sw);
				sw.flush();
				actual = sw.toString();
			}
			final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><b id=\"v\"/>"; //$NON-NLS-1$
			assertEquals(expected, actual);
		}
	}

	@DisplayName("readResources")
	@Nested
	public class ReadResources {

		private Document doc;
		private PathBuilder builder;
		private XMLResources res;

		@BeforeEach
		public void setUp() throws Exception {
			doc = XMLUtil.readXML(url4);
			builder = new SimplePathBuilder();
			res = new XMLResources(builder);	
		}
		
		@DisplayName("(Element,XMLResources) #1")
		@Test
		public void readResourcesElementXMLResources_1() throws Exception {
			var nb = XMLUtil.readResources((Element) doc.getFirstChild(), res);
			assertEquals(3, nb);
		}
		
		@DisplayName("(Element,XMLResources) #2")
		@Test
		public void readResourcesElementXMLResources_2() throws Exception {
			var nb = XMLUtil.readResources((Element) doc.getFirstChild(), res);
			var entry = res.getResource(1);
			assertTrue(entry.isURL());
			assertFalse(entry.isFile());
			assertFalse(entry.isEmbeddedData());
			assertEquals(new URI("file:/path/to/file.txt").toURL(), entry.getURL()); //$NON-NLS-1$
			assertEquals("text/plain", entry.getMimeType()); //$NON-NLS-1$
		}
		
		@DisplayName("(Element,XMLResources) #3")
		@Test
		public void readResourcesElementXMLResources_3() throws Exception {
			var nb = XMLUtil.readResources((Element) doc.getFirstChild(), res);
			var entry = res.getResource(3);
			assertFalse(entry.isURL());
			assertTrue(entry.isFile());
			assertFalse(entry.isEmbeddedData());
			assertEquals(FileSystem.convertURLToFile(new URL("file:/path/to/file.html")), entry.getFile()); //$NON-NLS-1$
			assertEquals("text/html", entry.getMimeType()); //$NON-NLS-1$
		}
		
		@DisplayName("(Element,XMLResources) #4")
		@Test
		public void readResourcesElementXMLResources_4() throws Exception {
			var nb = XMLUtil.readResources((Element) doc.getFirstChild(), res);
			var entry = res.getResource(4);
			assertFalse(entry.isURL());
			assertFalse(entry.isFile());
			assertTrue(entry.isEmbeddedData());
			assertArrayEquals("This is a text.\n".getBytes(), entry.getEmbeddedData()); //$NON-NLS-1$
			assertEquals("application/octet-stream", entry.getMimeType()); //$NON-NLS-1$
		}
	}

	@DisplayName("readResourceURL")
	@Nested
	public class ReadResourceURL {

		private Document doc;
		private PathBuilder builder;
		private XMLResources res;

		@BeforeEach
		public void setUp() throws Exception {
			doc = XMLUtil.readXML(url4);
			builder = new SimplePathBuilder();
			res = new XMLResources(builder);	
			XMLUtil.readResources((Element) doc.getFirstChild(), res);
		}
		
		@DisplayName("(Element,XMLResources,String...) #1")
		@Test
		public void readResourceURLElementXMLResourcesStringArray_1() throws Exception {
			URL url = XMLUtil.readResourceURL((Element) doc.getFirstChild(), res, "a", "b", "c", "res"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			if (OperatingSystem.WIN.isCurrentOS()) {
				assertEquals("file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/") + "//path/to/file.txt", url.toExternalForm()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} else {
				assertEquals("file:/path/to/file.txt", url.toExternalForm()); //$NON-NLS-1$
			}
		}
		
		@DisplayName("(Element,XMLResources,String...) #2")
		@Test
		public void readResourceURLElementXMLResourcesStringArray_2() throws Exception {
			URL url = XMLUtil.readResourceURL((Element) doc.getFirstChild(), res, "a", "b", "c", "res"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			assertNull(XMLUtil.readResourceURL((Element) doc.getFirstChild(), res, "a", "b", "c", "res2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("writeResources")
	@Nested
	public class WriteResources {

		private PathBuilder pathBuilder;
		private XMLResources res;
		private DocumentBuilderFactory factory;
		private DocumentBuilder builder;
		private Document doc;
		private XMLBuilder xmlBuilder;
		private Element root;

		@BeforeEach
		public void setUp() throws Exception {
			pathBuilder = new SimplePathBuilder();
			res = new XMLResources(pathBuilder);
			res.add(new URI("file:/path/to/file.txt").toURL(), "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
			res.add(FileSystem.convertURLToFile(new URI("file:/path/to/file.html").toURL()), "text/html"); //$NON-NLS-1$ //$NON-NLS-2$
	
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			
			xmlBuilder = new XMLBuilder() {
				@Override
				public Document getDocument() throws DOMException {
					return doc;
				}
			};
			
			root = xmlBuilder.createElement("root"); //$NON-NLS-1$
			doc.appendChild(root);
		}
		
		@DisplayName("(Element,XMLResources,String...) #1")
		@Test
		public void writeResourcesElementXMLResourcesXMLBuilder() throws Exception {
			XMLUtil.writeResources(doc.getDocumentElement(), res, xmlBuilder);
			String actual;
			try (StringWriter sw = new StringWriter()) {
				XMLUtil.writeXML(doc, sw);
				sw.flush();
				actual = sw.toString();
			}
			assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root><resources>" //$NON-NLS-1$
					+ "<resource id=\"#resource0\" mime=\"text/plain\" url=\"file:/path/to/file.txt\"/>" //$NON-NLS-1$
					+ "<resource file=\"file:/path/to/file.html\" id=\"#resource1\" mime=\"text/html\"/>" //$NON-NLS-1$
					+ "</resources></root>", actual); //$NON-NLS-1$
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum TestEnum {
		A, B, C;
	}

}

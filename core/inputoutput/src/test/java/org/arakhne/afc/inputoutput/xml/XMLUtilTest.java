/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.text.TextUtil;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.OperatingSystem;
import org.arakhne.afc.vmutil.Resources;

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
			this.document = builder.parse(is);
		}
	}

	@AfterEach
	public void tearDown() {
		this.document = null;
	}

	@Test
	public void parseColor_valid() throws Exception {
		assertHexEquals(0xFF0000FF, XMLUtil.parseColor("blue")); //$NON-NLS-1$
		assertHexEquals(0xFFFF0000, XMLUtil.parseColor("red")); //$NON-NLS-1$
		assertHexEquals(0xFFFF0022, XMLUtil.parseColor("rgb(255, 0, 34)")); //$NON-NLS-1$
		assertHexEquals(0xFF00FF01, XMLUtil.parseColor("#00FF01")); //$NON-NLS-1$
		assertHexEquals(0x3400FF01, XMLUtil.parseColor("#3400FF01")); //$NON-NLS-1$
		assertHexEquals(0x04FF0022, XMLUtil.parseColor("rgba(255, 0, 34, 4)")); //$NON-NLS-1$
		assertHexEquals(0xFF333333, XMLUtil.parseColor("hsl(0.7, 0, 0.2)")); //$NON-NLS-1$
		assertHexEquals(0x04333333, XMLUtil.parseColor("hsla(0.7, 0, 0.2, 4)")); //$NON-NLS-1$
		assertHexEquals(0xFF333333, XMLUtil.parseColor("hsl(70%, 0%, 0.2)")); //$NON-NLS-1$
		assertHexEquals(0x04333333, XMLUtil.parseColor("hsla(70%, 0%, 0.2, 4)")); //$NON-NLS-1$
		assertHexEquals(0xFF23194C, XMLUtil.parseColor("hsl(0.7, .5, 0.2)")); //$NON-NLS-1$
		assertHexEquals(0x0423194C, XMLUtil.parseColor("hsla(0.7, .5, 0.2, 4)")); //$NON-NLS-1$
		assertHexEquals(0xFF23194C, XMLUtil.parseColor("hsl(70%, 50%, 0.2)")); //$NON-NLS-1$
		assertHexEquals(0x0423194C, XMLUtil.parseColor("hsla(70%, 50%, 0.2, 4)")); //$NON-NLS-1$
	}

	@Test
	public void parseColor_invalid01() throws Exception {
		assertThrows(ColorFormatException.class, () -> assertHexEquals(0, XMLUtil.parseColor("0x00FF01"))); //$NON-NLS-1$
	}

	@Test
	public void parseColor_invalid02() throws Exception {
		assertThrows(ColorFormatException.class, () -> assertHexEquals(0, XMLUtil.parseColor("0xFF00FF01"))); //$NON-NLS-1$
	}

	@Test
	public void parseColor_invalid03() throws Exception {
		assertThrows(ColorFormatException.class, () -> assertHexEquals(0, XMLUtil.parseColor("1"))); //$NON-NLS-1$
	}

	@Test
	public void parseDate() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		Date base = dt.parse("2017-11-12 12:14:15"); //$NON-NLS-1$
		assertEquals(base, XMLUtil.parseDate("2017-11-12T12:14:15")); //$NON-NLS-1$
	}

	@Test
	public void parseString() {
		String b64 = Base64.getEncoder().encodeToString("hello".getBytes()); //$NON-NLS-1$
		assertEquals("hello", new String(XMLUtil.parseString(b64))); //$NON-NLS-1$
	}

	@Test
	public void parseObject() throws Exception {
		Object obj = new Integer(4);
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

	@Test
	public void toColorInt() {
		assertEquals("#45123456", XMLUtil.toColor(0x45123456)); //$NON-NLS-1$
		assertEquals("lime", XMLUtil.toColor(0xFF00FF00)); //$NON-NLS-1$
	}

	@Test
	public void toColorIntIntIntInt() {
		assertEquals("#45123456", XMLUtil.toColor(18, 52, 86, 69)); //$NON-NLS-1$
		assertEquals("lime", XMLUtil.toColor(0, 255, 0, 255)); //$NON-NLS-1$
	}

	@Test
	public void toStringByteArray() {
		String b64 = Base64.getEncoder().encodeToString("hello".getBytes()); //$NON-NLS-1$
		assertEquals(b64, XMLUtil.toString("hello".getBytes())); //$NON-NLS-1$
	}

	@Test
	public void toStringDate() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		Date base = dt.parse("2017-11-12 12:14:15"); //$NON-NLS-1$
		assertEquals("2017-11-12T12:14:15", XMLUtil.toString(base)); //$NON-NLS-1$
	}

	@Test
	public void toStringSerializable() throws Exception {
		Serializable obj = new Integer(4);
		String b64;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(obj);
			}
			b64 = Base64.getEncoder().encodeToString(baos.toByteArray());
		}
		assertEquals(b64, XMLUtil.toString(obj));
	}

	@Test
	public void getAttributeBooleanNodeBooleanStringArray() {
		assertTrue(XMLUtil.getAttributeBoolean(this.document, true, "catalog", "book", "author", "fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, true, "catalog", "book", "title", "fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertTrue(XMLUtil.getAttributeBoolean(this.document, false, "catalog", "book", "author", "fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, false, "catalog", "book", "title", "fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, true, "catalog", "book", "author", "Fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, true, "catalog", "book", "title", "Fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, false, "catalog", "book", "author", "Fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, false, "catalog", "book", "title", "Fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeBooleanNodeStringArray() {
		assertTrue(XMLUtil.getAttributeBoolean(this.document, "catalog", "book", "author", "fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, "catalog", "book", "title", "fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, "catalog", "book", "author", "Fakebool1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertFalse(XMLUtil.getAttributeBoolean(this.document, "catalog", "book", "title", "Fakebool2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeClassNodeBooleanStringArray() {
		assertEquals(Integer.class, XMLUtil.getAttributeClass(this.document, true, "catalog", "book", "genre", "fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(Integer.class, XMLUtil.getAttributeClass(this.document, false, "catalog", "book", "genre", "fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeClass(this.document, true, "catalog", "book", "genre", "Fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(Integer.class, XMLUtil.getAttributeClass(this.document, false, "catalog", "book", "genre", "Fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeClassNodeStringArray() {
		assertEquals(Integer.class, XMLUtil.getAttributeClass(this.document, "catalog", "book", "genre", "fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeClass(this.document, "catalog", "book", "genre", "Fakeclass1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeColorNodeBooleanStringArray() {
		assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(this.document, true, "catalog", "book", "price", "color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertHexEquals(0, XMLUtil.getAttributeColor(this.document, true, "catalog", "book", "price", "Color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(this.document, false, "catalog", "book", "price", "color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(this.document, false, "catalog", "book", "price", "Color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeColorNodeStringArray() {
		assertHexEquals(0xFF00FF01, XMLUtil.getAttributeColor(this.document, "catalog", "book", "price", "color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertHexEquals(0, XMLUtil.getAttributeColor(this.document, "catalog", "book", "price", "Color1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeDateNodeBooleanStringArray() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		Date base = dt.parse("2017-11-12 12:45:47"); //$NON-NLS-1$
		assertEquals(base, XMLUtil.getAttributeDate(this.document, true, "catalog", "book", "publish", "date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeDate(this.document, true, "catalog", "book", "publish", "Date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(base, XMLUtil.getAttributeDate(this.document, false, "catalog", "book", "publish", "date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(base, XMLUtil.getAttributeDate(this.document, false, "catalog", "book", "publish", "Date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeDateNodeStringArray() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		Date base = dt.parse("2017-11-12 12:45:47"); //$NON-NLS-1$
		assertEquals(base, XMLUtil.getAttributeDate(this.document, "catalog", "book", "publish", "date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeDate(this.document, "catalog", "book", "publish", "Date1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeDoubleNodeBooleanStringArray() {
		assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(this.document, true, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(0., XMLUtil.getAttributeDouble(this.document, true, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(this.document, false, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(this.document, false, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeDoubleNodeStringArray() {
		assertEpsilonEquals(12.56e4, XMLUtil.getAttributeDouble(this.document, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(0., XMLUtil.getAttributeDouble(this.document, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeFloatNodeBooleanStringArray() {
		assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(this.document, true, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(0.f, XMLUtil.getAttributeFloat(this.document, true, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(this.document, false, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(this.document, false, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeFloatNodeStringArray() {
		assertEpsilonEquals(12.56e4f, XMLUtil.getAttributeFloat(this.document, "catalog", "book", "description", "number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEpsilonEquals(0.f, XMLUtil.getAttributeFloat(this.document, "catalog", "book", "description", "Number1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeIntNodeBooleanStringArray() {
		assertEquals(12564, XMLUtil.getAttributeInt(this.document, true, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(0, XMLUtil.getAttributeInt(this.document, true, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(12564, XMLUtil.getAttributeInt(this.document, false, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(12564, XMLUtil.getAttributeInt(this.document, false, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeIntNodeStringArray() {
		assertEquals(12564, XMLUtil.getAttributeInt(this.document, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(0, XMLUtil.getAttributeInt(this.document, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeLongNodeBooleanStringArray() {
		assertEquals(12564l, XMLUtil.getAttributeLong(this.document, true, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(0l, XMLUtil.getAttributeLong(this.document, true, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(12564l, XMLUtil.getAttributeLong(this.document, false, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(12564l, XMLUtil.getAttributeLong(this.document, false, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeLongNodeStringArray() {
		assertEquals(12564l, XMLUtil.getAttributeLong(this.document, "catalog", "book", "author", "number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(0l, XMLUtil.getAttributeLong(this.document, "catalog", "book", "author", "Number2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeUUIDNodeBooleanStringArray() {
		UUID id = UUID.fromString("e48d046f-975c-4a22-92ff-cb23b50716ce"); //$NON-NLS-1$
		assertEquals(id, XMLUtil.getAttributeUUID(this.document, true, "catalog", "book", "description", "uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeUUID(this.document, true, "catalog", "book", "description", "Uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(id, XMLUtil.getAttributeUUID(this.document, false, "catalog", "book", "description", "uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(id, XMLUtil.getAttributeUUID(this.document, false, "catalog", "book", "description", "Uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeUUIDNodeStringArray() {
		UUID id = UUID.fromString("e48d046f-975c-4a22-92ff-cb23b50716ce"); //$NON-NLS-1$
		assertEquals(id, XMLUtil.getAttributeUUID(this.document, "catalog", "book", "description", "uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeUUID(this.document, "catalog", "book", "description", "Uuid1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeUUIDsNodeBooleanStringArray() {
		UUID id1 = UUID.fromString("e48d046f-975c-4a22-92ff-cb23b50716ce"); //$NON-NLS-1$
		UUID id2 = UUID.fromString("4f89a62d-04d3-4aec-ab67-8f60bff3d5b0"); //$NON-NLS-1$
		List<UUID> ids = Arrays.asList(id1, id2);
		assertEquals(ids, XMLUtil.getAttributeUUIDs(this.document, true, "catalog", "book", "title", "uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(Collections.emptyList(), XMLUtil.getAttributeUUIDs(this.document, true, "catalog", "book", "title", "Uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(ids, XMLUtil.getAttributeUUIDs(this.document, false, "catalog", "book", "title", "uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(ids, XMLUtil.getAttributeUUIDs(this.document, false, "catalog", "book", "title", "Uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeUUIDsNodeStringArray() {
		UUID id1 = UUID.fromString("e48d046f-975c-4a22-92ff-cb23b50716ce"); //$NON-NLS-1$
		UUID id2 = UUID.fromString("4f89a62d-04d3-4aec-ab67-8f60bff3d5b0"); //$NON-NLS-1$
		List<UUID> ids = Arrays.asList(id1, id2);
		assertEquals(ids, XMLUtil.getAttributeUUIDs(this.document, "catalog", "book", "title", "uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(Collections.emptyList(), XMLUtil.getAttributeUUIDs(this.document, "catalog", "book", "title", "Uuids1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeURLNodeBooleanStringArray() throws Exception {
		URL url = new URL("http://www.arakhne.org"); //$NON-NLS-1$
		assertEquals(url, XMLUtil.getAttributeURL(this.document, true, "catalog", "book", "price", "url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeURL(this.document, true, "catalog", "book", "price", "Url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(url, XMLUtil.getAttributeURL(this.document, false, "catalog", "book", "price", "url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(url, XMLUtil.getAttributeURL(this.document, false, "catalog", "book", "price", "Url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeURLNodeStringArray() throws Exception {
		URL url = new URL("http://www.arakhne.org"); //$NON-NLS-1$
		assertEquals(url, XMLUtil.getAttributeURL(this.document, "catalog", "book", "price", "url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeURL(this.document, "catalog", "book", "price", "Url1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeEnumNodeClassBooleanStringArray() {
		assertSame(TestEnum.B, XMLUtil.getAttributeEnum(this.document, TestEnum.class, true, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeEnum(this.document, TestEnum.class, true, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.B, XMLUtil.getAttributeEnum(this.document, TestEnum.class, false, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.B, XMLUtil.getAttributeEnum(this.document, TestEnum.class, false, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		//
		assertNull(XMLUtil.getAttributeEnum(this.document, TestEnum.class, true, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeEnum(this.document, TestEnum.class, true, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.B, XMLUtil.getAttributeEnum(this.document, TestEnum.class, false, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.B, XMLUtil.getAttributeEnum(this.document, TestEnum.class, false, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeEnumNodeClassStringArray() {
		assertSame(TestEnum.B, XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		//
		assertNull(XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertNull(XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void setAttributeEnumNodeClassBooleanTStringArray() {
		assertTrue(XMLUtil.setAttributeEnum(this.document, TestEnum.class, true, TestEnum.C, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.C, XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertTrue(XMLUtil.setAttributeEnum(this.document, TestEnum.class, true, TestEnum.A, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.A, XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.C, XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		//
		assertTrue(XMLUtil.setAttributeEnum(this.document, TestEnum.class, false, TestEnum.C, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.C, XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertTrue(XMLUtil.setAttributeEnum(this.document, TestEnum.class, false, TestEnum.A, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.A, XMLUtil.getAttributeEnum(this.document, TestEnum.class, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertSame(TestEnum.A, XMLUtil.getAttributeEnum(this.document, TestEnum.class, false, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void getAttributeValueNodeBooleanStringArray() {
		assertEquals("B", XMLUtil.getAttributeValue(this.document, true, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("", XMLUtil.getAttributeValue(this.document, true, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("B", XMLUtil.getAttributeValue(this.document, false, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("B", XMLUtil.getAttributeValue(this.document, false, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		//
		assertEquals("b", XMLUtil.getAttributeValue(this.document, true, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("", XMLUtil.getAttributeValue(this.document, true, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("b", XMLUtil.getAttributeValue(this.document, false, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("b", XMLUtil.getAttributeValue(this.document, false, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Test
	public void getAttributeValueNodeStringArray() {
		assertEquals("B", XMLUtil.getAttributeValue(this.document, "catalog", "book", "publish", "enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("", XMLUtil.getAttributeValue(this.document, "catalog", "book", "publish", "Enum1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		//
		assertEquals("b", XMLUtil.getAttributeValue(this.document, "catalog", "book", "genre", "enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("", XMLUtil.getAttributeValue(this.document, "catalog", "book", "genre", "Enum2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Test
	public void getChildNodeClass() {
		Element node = XMLUtil.getChild(this.document, Element.class);
		assertEquals("catalog", node.getTagName()); //$NON-NLS-1$
	}

	@Test
	public void getDocumentForNode() {
		Element node = XMLUtil.getChild(this.document, Element.class);
		node = XMLUtil.getChild(node, Element.class);
		Node parent = XMLUtil.getDocumentFor(node);
		assertSame(this.document, parent);
	}

	@Test
	public void getElementFromPathNodeBooleanStringArray() {
		Element element;
		element = XMLUtil.getElementFromPath(this.document, true, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getTagName()); //$NON-NLS-1$
		element = XMLUtil.getElementFromPath(this.document, true, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(element);
		element = XMLUtil.getElementFromPath(this.document, false, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getTagName()); //$NON-NLS-1$
		element = XMLUtil.getElementFromPath(this.document, false, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getTagName()); //$NON-NLS-1$
	}

	@Test
	public void getElementFromPathNodeStringArray() {
		Element element;
		element = XMLUtil.getElementFromPath(this.document, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getTagName()); //$NON-NLS-1$
		element = XMLUtil.getElementFromPath(this.document, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(element);
	}

	@Test
	public void getNodeFromPathNodeBooleanStringArray() {
		Node element;
		element = XMLUtil.getNodeFromPath(this.document, true, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getNodeName()); //$NON-NLS-1$
		element = XMLUtil.getNodeFromPath(this.document, true, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(element);
		element = XMLUtil.getNodeFromPath(this.document, false, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getNodeName()); //$NON-NLS-1$
		element = XMLUtil.getNodeFromPath(this.document, false, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getNodeName()); //$NON-NLS-1$
	}

	@Test
	public void getNodeFromPathNodeStringArray() {
		Node element;
		element = XMLUtil.getNodeFromPath(this.document, "catalog", "book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("book", element.getNodeName()); //$NON-NLS-1$
		element = XMLUtil.getNodeFromPath(this.document, "catalog", "Book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(element);
	}

	@Test
	public void getTextNodeStringArray() {
		assertEquals("An in-depth look at creating applications \n      with XML.", //$NON-NLS-1$
				XMLUtil.getText(this.document, "catalog", "book", "description")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Test
	public void iterateNodeString() {
		Node node;
		Iterator<Node> iterator = XMLUtil.iterate(XMLUtil.getNodeFromPath(this.document, "catalog"), "book"); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(iterator.hasNext());
		node = iterator.next();
		assertEquals("bk101", XMLUtil.getAttributeValue(node, "id")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(iterator.hasNext());
		node = iterator.next();
		assertEquals("bk202", XMLUtil.getAttributeValue(node, "id")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(iterator.hasNext());
	}

	@Test
	public void parseXML() {
		Document doc = XMLUtil.parseXML("<a><b id = \"v\"/></a>"); //$NON-NLS-1$
		assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void toStringNode() {
		Document doc = XMLUtil.parseXML("<a><b id = \"v\"/></a>"); //$NON-NLS-1$
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><a><b id=\"v\"/></a>", XMLUtil.toString(doc)); //$NON-NLS-1$
	}

	@Test
	public void readXMLReader() throws Exception {
		final String source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><a><b id=\"v\"/></a>"; //$NON-NLS-1$
		Document doc;
		try (StringReader reader = new StringReader(source)) {
			doc = XMLUtil.readXML(reader);
		}
		assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void readXMLInputStream() throws Exception {
		final String source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><a><b id=\"v\"/></a>"; //$NON-NLS-1$
		Document doc;
		try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
			doc = XMLUtil.readXML(reader);
		}
		assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void readXMLURL() throws Exception {
		Document doc = XMLUtil.readXML(url2);
		assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void readXMLFile() throws Exception {
		Document doc = XMLUtil.readXML(FileSystem.convertURLToFile(url2));
		assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void readXMLString() throws Exception {
		Document doc = XMLUtil.readXML(FileSystem.convertURLToFile(url2).getAbsolutePath());
		assertEquals("v", XMLUtil.getAttributeValue(doc, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void readXMLFragmentReader() throws Exception {
		final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
		DocumentFragment fragment;
		try (StringReader reader = new StringReader(source)) {
			fragment = XMLUtil.readXMLFragment(reader);
		}
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}

	@Test
	public void readXMLFragmentReader_skipRoot() throws Exception {
		final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><a><b id=\"v\"/></a><c><d><e id=\"w\"/></d></c></root>"; //$NON-NLS-1$
		DocumentFragment fragment;
		try (StringReader reader = new StringReader(source)) {
			fragment = XMLUtil.readXMLFragment(reader, true);
		}
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	}

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

	@Test
	public void readXMLFragmentURL() throws Exception {
		DocumentFragment fragment = XMLUtil.readXMLFragment(url3);
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}

	@Test
	public void readXMLFragmentURL_skipRoot() throws Exception {
		DocumentFragment fragment = XMLUtil.readXMLFragment(url3, true);
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	}

	@Test
	public void readXMLFragmentFile() throws Exception {
		DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3));
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}

	@Test
	public void readXMLFragmentFile_skipRoot() throws Exception {
		DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3), true);
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	}

	@Test
	public void readXMLFragmentString() throws Exception {
		DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3).getAbsolutePath());
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "root", "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "root", "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}

	@Test
	public void readXMLFragmentString_skipRoot() throws Exception {
		DocumentFragment fragment = XMLUtil.readXMLFragment(FileSystem.convertURLToFile(url3).getAbsolutePath(), true);
		assertEquals("v", XMLUtil.getAttributeValue(fragment, "a", "b", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
		assertEquals("w", XMLUtil.getAttributeValue(fragment, "c", "d", "e", "id")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	}

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

	@Test
	public void readResourcesElementXMLResources() throws Exception {
		Document doc = XMLUtil.readXML(url4);
		PathBuilder builder = new SimplePathBuilder();
		XMLResources res = new XMLResources(builder);

		int nb = XMLUtil.readResources((Element) doc.getFirstChild(), res);

		assertEquals(3, nb);
		
		XMLResources.Entry entry;
		
		entry = res.getResource(1);
		assertTrue(entry.isURL());
		assertFalse(entry.isFile());
		assertFalse(entry.isEmbeddedData());
		assertEquals(new URL("file:/path/to/file.txt"), entry.getURL()); //$NON-NLS-1$
		assertEquals("text/plain", entry.getMimeType()); //$NON-NLS-1$

		entry = res.getResource(3);
		assertFalse(entry.isURL());
		assertTrue(entry.isFile());
		assertFalse(entry.isEmbeddedData());
		assertEquals(FileSystem.convertURLToFile(new URL("file:/path/to/file.html")), entry.getFile()); //$NON-NLS-1$
		assertEquals("text/html", entry.getMimeType()); //$NON-NLS-1$

		entry = res.getResource(4);
		assertFalse(entry.isURL());
		assertFalse(entry.isFile());
		assertTrue(entry.isEmbeddedData());
		assertArrayEquals("This is a text.\n".getBytes(), entry.getEmbeddedData()); //$NON-NLS-1$
		assertEquals("application/octet-stream", entry.getMimeType()); //$NON-NLS-1$
	}

	@Test
	public void readResourceURLElementXMLResourcesStringArray() throws Exception {
		Document doc = XMLUtil.readXML(url4);
		PathBuilder builder = new SimplePathBuilder();
		XMLResources res = new XMLResources(builder);
		XMLUtil.readResources((Element) doc.getFirstChild(), res);

		URL url = XMLUtil.readResourceURL((Element) doc.getFirstChild(), res, "a", "b", "c", "res"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		if (OperatingSystem.WIN.isCurrentOS()) {
			assertEquals("file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/") + "//path/to/file.txt", url.toExternalForm()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else {
			assertEquals("file:/path/to/file.txt", url.toExternalForm()); //$NON-NLS-1$
		}

		assertNull(XMLUtil.readResourceURL((Element) doc.getFirstChild(), res, "a", "b", "c", "res2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void writeResourcesElementXMLResourcesXMLBuilder() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		res.add(new URL("file:/path/to/file.txt"), "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
		res.add(FileSystem.convertURLToFile(new URL("file:/path/to/file.html")), "text/html"); //$NON-NLS-1$ //$NON-NLS-2$

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.newDocument();
		
		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};
		
		Element root = xmlBuilder.createElement("root"); //$NON-NLS-1$
		doc.appendChild(root);
		
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

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.FileSystem;

@DisplayName("XMLResources")
@SuppressWarnings("all")
public class XMLResourcesTest extends AbstractTestCase {

	private SimplePathBuilder path;
	private XMLResources xml;
	private byte[] data;

	@BeforeEach
	public void setUp() throws Exception {
		data = new byte[] {1, 2, 3};
		path = new SimplePathBuilder();
		path.setCurrentDirectory(FileSystem.getUserHomeDirectory());
		xml = new XMLResources(path);
		fillResources();
	}
	
	@AfterEach
	public void tearDown() {
		data = null;
		path = null;
		xml = null;
	}

	private URL newURL(String url) throws Exception {
		return new URI(url).toURL();
	}

	protected void fillResources() throws Exception {
		xml.add(1, data, MimeName.MIME_PDF.getMimeConstant());
		xml.add(2, new File("a.txt"), MimeName.MIME_PLAIN_TEXT.getMimeConstant()); //$NON-NLS-1$
		xml.add(3, newURL("file:b.txt"), MimeName.MIME_PLAIN_TEXT.getMimeConstant()); //$NON-NLS-1$
		xml.add(4, new File("a.jpg"), MimeName.MIME_JPG.getMimeConstant()); //$NON-NLS-1$
	}
	
	@DisplayName("compare")
	@Nested
	public class Compare {

		@DisplayName("(Object, Object) #1")
		@Test
		public void compareObjectObject_1() throws Exception {
			assertZero(xml.compare(null, null));
		}

		@DisplayName("(Object, Object) #2")
		@Test
		public void compareObjectObject_2() throws Exception {
			assertStrictlyPositive(xml.compare(new Object(), null));
		}

		@DisplayName("(Object, Object) #3")
		@Test
		public void compareObjectObject_3() throws Exception {
			assertStrictlyNegative(xml.compare(null, new Object()));
		}

		@DisplayName("(Object, Object) #4")
		@Test
		public void compareObjectObject_4() throws Exception {
			assertZero(xml.compare(new File("a"), new File("a"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(Object, Object) #5")
		@Test
		public void compareObjectObject_5() throws Exception {
			assertStrictlyNegative(xml.compare(new File("a"), new File("b"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(Object, Object) #6")
		@Test
		public void compareObjectObject_6() throws Exception {
			assertZero(xml.compare(newURL("file:a"), newURL("file:a"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(Object, Object) #7")
		@Test
		public void compareObjectObject_7() throws Exception {
			assertStrictlyNegative(xml.compare(newURL("file:a"), newURL("file:b"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(Object, Object) #8")
		@Test
		public void compareObjectObject_8() throws Exception {
			assertZero(xml.compare(new File("a"), newURL("file:a"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(Object, Object) #9")
		@Test
		public void compareObjectObject_9() throws Exception {
			assertStrictlyNegative(xml.compare(new File("a"), newURL("file:b"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("getStringIdentifier")
	@Nested
	public class GetStringIdentifier {

		@DisplayName("#1")
		@Test
		public void getStringIdentifier_1() {
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", XMLResources.getStringIdentifier(0)); //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void getStringIdentifier_2() {
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "351", XMLResources.getStringIdentifier(351)); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void getStringIdentifier_3() {
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "-4521", XMLResources.getStringIdentifier(-4521)); //$NON-NLS-1$
		}
	}

	@DisplayName("getNumericalIdentifier")
	@Nested
	public class GetNumericalIdentifier {

		@DisplayName("#1")
		@Test
		public void getNumericalIdentifier_1() {
			assertEquals(0, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "0")); //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void getNumericalIdentifier_2() {
			assertEquals(351, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "351")); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void getNumericalIdentifier_3() {
			assertEquals(-4521, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "-4521")); //$NON-NLS-1$
		}
	}

	@DisplayName("isStringIdentifier")
	@Nested
	public class IsStringIdentifier {

		@DisplayName("#1")
		@Test
		public void isStringIdentifier_1() {
			assertTrue(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX + "0")); //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void isStringIdentifier_2() {
			assertTrue(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX + "351")); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void isStringIdentifier_3() {
			assertTrue(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX + "-4521")); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void isStringIdentifier_4() {
			assertFalse(XMLResources.isStringIdentifier(null));
		}

		@DisplayName("#5")
		@Test
		public void isStringIdentifier_5() {
			assertFalse(XMLResources.isStringIdentifier("")); //$NON-NLS-1$
		}

		@DisplayName("#6")
		@Test
		public void isStringIdentifier_6() {
			assertFalse(XMLResources.isStringIdentifier("-4521")); //$NON-NLS-1$
		}

		@DisplayName("#7")
		@Test
		public void isStringIdentifier_7() {
			assertFalse(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX));
		}
	}

	@DisplayName("getIdentifier")
	@Nested
	public class GetIdentifier {

		@DisplayName("(URL) #1")
		@Test
		public void getIdentifierURL_1() throws Exception {
			assertEquals(-1, xml.getIdentifier(newURL("file:x"))); //$NON-NLS-1$
		}

		@DisplayName("(URL) #2")
		@Test
		public void getIdentifierURL_2() throws Exception {
			assertEquals(2, xml.getIdentifier(newURL("file:a.txt"))); //$NON-NLS-1$
		}

		@DisplayName("(URL) #3")
		@Test
		public void getIdentifierURL_3() throws Exception {
			assertEquals(3, xml.getIdentifier(newURL("file:b.txt"))); //$NON-NLS-1$
		}

		@DisplayName("(URL) #4")
		@Test
		public void getIdentifierURL_4() throws Exception {
			assertEquals(4, xml.getIdentifier(newURL("file:a.jpg"))); //$NON-NLS-1$
		}
	
		@DisplayName("(File) #1")
		@Test
		public void getIdentifierFile_1() {
			assertEquals(-1, xml.getIdentifier(new File("x"))); //$NON-NLS-1$
		}
		
		@DisplayName("(File) #2")
		@Test
		public void getIdentifierFile_2() {
			assertEquals(2, xml.getIdentifier(new File("a.txt"))); //$NON-NLS-1$
		}
		
		@DisplayName("(File) #3")
		@Test
		public void getIdentifierFile_3() {
			assertEquals(3, xml.getIdentifier(new File("b.txt"))); //$NON-NLS-1$
		}
		
		@DisplayName("(File) #4")
		@Test
		public void getIdentifierFile_4() {
			assertEquals(4, xml.getIdentifier(new File("a.jpg"))); //$NON-NLS-1$
		}
	
		@DisplayName("(byte[]) #1")
		@Test
		public void getIdentifierByteAray_1() {
			assertEquals(-1, xml.getIdentifier(new byte[0]));
		}
		
		@DisplayName("(byte[]) #2")
		@Test
		public void getIdentifierByteAray_2() {
			assertEquals(-1, xml.getIdentifier(new byte[] { 1, 2, 3 }));
		}
		
		@DisplayName("(byte[]) #3")
		@Test
		public void getIdentifierByteAray_3() {
			assertEquals(1, xml.getIdentifier(data));
		}
	}

	@DisplayName("getResourceURL")
	@Nested
	public class GetResourceURL {

		@DisplayName("#1")
		@Test
		public void getResourceURL_1() throws Exception {
			assertEquals(path.makeAbsolute(new File("a.txt")), xml.getResourceURL(2)); //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void getResourceURL_2() throws Exception {
			assertEquals(path.makeAbsolute(newURL("file:b.txt")), xml.getResourceURL(3)); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void getResourceURL_3() throws Exception {
			assertEquals(path.makeAbsolute(newURL("file:a.jpg")), xml.getResourceURL(4)); //$NON-NLS-1$
		}
	}

	@DisplayName("getResource")
	@Nested
	public class GetResource {

		@DisplayName("#1")
		@Test
		public void getResource_1() throws Exception {
			var e = xml.getResource(1);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(data, e.getEmbeddedData());
			assertNull(e.getFile());
			assertNull(e.getURL());
			assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("#2")
		@Test
		public void getResource_2() throws Exception {
			var e = xml.getResource(2);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("#3")
		@Test
		public void getResource_3() throws Exception {
			var e = xml.getResource(3);
			assertNotNull(e);
			assertTrue(e.isURL());
			assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
			assertNull(e.getFile());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("#4")
		@Test
		public void getResource_4() throws Exception {
			var e = xml.getResource(4);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
		}
	}

	@DisplayName("computeNextIdentifier")
	@Nested
	public class ComputeNextIdentifier {

		@DisplayName("#1")
		@Test
		public void computeNextIdentifier_1() {
			assertEquals(0, xml.computeNextIdentifier());
		}

		@DisplayName("#2")
		@Test
		public void computeNextIdentifier_2() {
			xml.computeNextIdentifier();
			assertEquals(5, xml.computeNextIdentifier());
		}

		@DisplayName("#3")
		@Test
		public void computeNextIdentifier_3() {
			xml.computeNextIdentifier();
			xml.computeNextIdentifier();
			assertEquals(6, xml.computeNextIdentifier());
		}

		@DisplayName("#4")
		@Test
		public void computeNextIdentifier_4() {
			xml.computeNextIdentifier();
			xml.computeNextIdentifier();
			xml.computeNextIdentifier();
			assertEquals(7, xml.computeNextIdentifier());
		}
	}

	@DisplayName("getPairs")
	@Nested
	public class GetPairs {

		@DisplayName("#1")
		@Test
		public void getPairs_1() throws Exception {
			Map<Long, XMLResources.Entry> pairs = xml.getPairs();
			assertEquals(4, pairs.size());
		}

		@DisplayName("#2")
		@Test
		public void getPairs_2() throws Exception {
			Map<Long, XMLResources.Entry> pairs = xml.getPairs();
			var e = pairs.get(1l);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(data, e.getEmbeddedData());
			assertNull(e.getFile());
			assertNull(e.getURL());
			assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("#3")
		@Test
		public void getPairs_3() throws Exception {
			Map<Long, XMLResources.Entry> pairs = xml.getPairs();
			var e = pairs.get(2l);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("#4")
		@Test
		public void getPairs_4() throws Exception {
			Map<Long, XMLResources.Entry> pairs = xml.getPairs();
			var e = pairs.get(3l);
			assertNotNull(e);
			assertTrue(e.isURL());
			assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
			assertNull(e.getFile());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("#5")
		@Test
		public void getPairs_5() throws Exception {
			Map<Long, XMLResources.Entry> pairs = xml.getPairs();
			var e = pairs.get(4l);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
		}
	}

	@DisplayName("add")
	@Nested
	public class Add {

		@DisplayName("(Long, URL, String)")
		@Test
		public void addLongURLString() throws Exception {
			String k = xml.add(5, newURL("http://www.arakhne.org"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "5", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(5);
			assertNotNull(e);
			assertTrue(e.isURL());
			assertEquals(newURL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
			assertNull(e.getFile());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(URL)")
		@Test
		public void addURL() throws Exception {
			String k = xml.add(newURL("http://www.arakhne.org")); //$NON-NLS-1$
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(0);
			assertNotNull(e);
			assertTrue(e.isURL());
			assertEquals(newURL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
			assertNull(e.getFile());
			assertNull(e.getEmbeddedData());
			// If Internet connection is up, is should be HTML mime type.
			// Otherwise it should be Octet-stream.
			String type = e.getMimeType();
			var t0 = type.startsWith(MimeName.MIME_HTML.getMimeConstant());
			var t1 = type.startsWith(MimeName.MIME_OCTET_STREAM.getMimeConstant());
			assertTrue(t0 || t1, "expecting " + MimeName.MIME_HTML.getMimeConstant() + " or " + MimeName.MIME_OCTET_STREAM.getMimeConstant() + "; Actual: " + type);
		}
	
		@DisplayName("(URL, String)")
		@Test
		public void addURLString() throws Exception {
			String k = xml.add(newURL("http://www.arakhne.org"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(0);
			assertNotNull(e);
			assertTrue(e.isURL());
			assertEquals(newURL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
			assertNull(e.getFile());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(Long, File, String)")
		@Test
		public void addLongFileString() {
			String k = xml.add(5, new File("xxx.3ds"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "5", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(5);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("xxx.3ds"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(File)")
		@Test
		public void addFile() throws Exception {
			String k = xml.add(new File("xxx.3ds")); //$NON-NLS-1$
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(0);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("xxx.3ds"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertNull(e.getMimeType());
		}
	
		@DisplayName("(File, String)")
		@Test
		public void addFileString() throws Exception {
			String k = xml.add(new File("xxx.3ds"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(0);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("xxx.3ds"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(Long, byte[], String)")
		@Test
		public void addLongByteArrayString() {
			byte[] dt = new byte[] {1, 2, 3};
			String k = xml.add(5, dt, MimeName.MIME_AVI.getMimeConstant());
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "5", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(5);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(dt, e.getEmbeddedData());
			assertNull(e.getURL());
			assertNull(e.getFile());
			assertEquals(MimeName.MIME_AVI.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(byte[])")
		@Test
		public void addByteArray() throws Exception {
			byte[] dt = new byte[] {1, 2, 3};
			String k = xml.add(dt);
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(0);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(dt, e.getEmbeddedData());
			assertNull(e.getURL());
			assertNull(e.getFile());
			assertEquals(MimeName.MIME_OCTET_STREAM.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(byte[], String)")
		@Test
		public void addByteArrayString() throws Exception {
			byte[] dt = new byte[] {1, 2, 3};
			String k = xml.add(dt, MimeName.MIME_AVI.getMimeConstant());
			assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
			XMLResources.Entry e = xml.getResource(0);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(dt, e.getEmbeddedData());
			assertNull(e.getURL());
			assertNull(e.getFile());
			assertEquals(MimeName.MIME_AVI.getMimeConstant(), e.getMimeType());
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@DisplayName("(Long) #1")
		@Test
		public void removeLong_1() {
			xml.remove(3l);
			var e = xml.getResource(1);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(data, e.getEmbeddedData());
			assertNull(e.getFile());
			assertNull(e.getURL());
			assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("(Long) #2")
		@Test
		public void removeLong_2() {
			xml.remove(3l);
			var e = xml.getResource(2);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}

		@DisplayName("(Long) #3")
		@Test
		public void removeLong_3() {
			xml.remove(3l);
			var e = xml.getResource(4);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(URL) #1")
		@Test
		public void removeURL_1() throws Exception {
			xml.remove(newURL("file:b.txt")); //$NON-NLS-1$
			var e = xml.getResource(1);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(data, e.getEmbeddedData());
			assertNull(e.getFile());
			assertNull(e.getURL());
			assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());
		}
		
		@DisplayName("(URL) #2")
		@Test
		public void removeURL_2() throws Exception {
			xml.remove(newURL("file:b.txt")); //$NON-NLS-1$
			var e = xml.getResource(2);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}
		
		@DisplayName("(URL) #3")
		@Test
		public void removeURL_3() throws Exception {
			xml.remove(newURL("file:b.txt")); //$NON-NLS-1$
			var e = xml.getResource(4);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(File) #1")
		@Test
		public void removeFile_1() throws Exception {
			xml.remove(new File("a.txt")); //$NON-NLS-1$
			var e = xml.getResource(1);
			assertNotNull(e);
			assertTrue(e.isEmbeddedData());
			assertSame(data, e.getEmbeddedData());
			assertNull(e.getFile());
			assertNull(e.getURL());
			assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());
		}
		
		@DisplayName("(File) #2")
		@Test
		public void removeFile_2() throws Exception {
			xml.remove(new File("a.txt")); //$NON-NLS-1$
			var e = xml.getResource(3);
			assertNotNull(e);
			assertTrue(e.isURL());
			assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
			assertNull(e.getFile());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}
		
		@DisplayName("(File) #3")
		@Test
		public void removeFile_3() throws Exception {
			xml.remove(new File("a.txt")); //$NON-NLS-1$
			var e = xml.getResource(4);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
		}
	
		@DisplayName("(byte[]) #1")
		@Test
		public void removeByteArray_1() throws Exception {
			xml.remove(data);
			var e = xml.getResource(2);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}
		
		@DisplayName("(byte[]) #2")
		@Test
		public void removeByteArray_2() throws Exception {
			xml.remove(data);
			var e = xml.getResource(3);
			assertNotNull(e);
			assertTrue(e.isURL());
			assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
			assertNull(e.getFile());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());
		}
		
		@DisplayName("(byte[]) #3")
		@Test
		public void removeByteArray_3() throws Exception {
			xml.remove(data);
			var e = xml.getResource(4);
			assertNotNull(e);
			assertTrue(e.isFile());
			assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
			assertNull(e.getURL());
			assertNull(e.getEmbeddedData());
			assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@Test
		public void clear() {
			xml.clear();
			assertNull(xml.getResource(0));
			assertNull(xml.getResource(1));
			assertNull(xml.getResource(2));
			assertNull(xml.getResource(3));
			assertNull(xml.getResource(4));
			assertNull(xml.getResource(5));
		}
	}

}

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
import java.net.URL;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.FileSystem;

@SuppressWarnings("all")
public class XMLResourcesTest extends AbstractTestCase {

	private SimplePathBuilder path;
	private XMLResources xml;
	private byte[] data;

	@BeforeEach
	public void setUp() throws Exception {
		this.data = new byte[] {1, 2, 3};
		this.path = new SimplePathBuilder();
		this.path.setCurrentDirectory(FileSystem.getUserHomeDirectory());
		this.xml = new XMLResources(this.path);
		fillResources();
	}
	
	@AfterEach
	public void tearDown() {
		this.data = null;
		this.path = null;
		this.xml = null;
	}

	private URL newURL(String url) throws Exception {
		return new URL(url);
	}

	protected void fillResources() throws Exception {
		this.xml.add(1, this.data, MimeName.MIME_PDF.getMimeConstant());
		this.xml.add(2, new File("a.txt"), MimeName.MIME_PLAIN_TEXT.getMimeConstant()); //$NON-NLS-1$
		this.xml.add(3, newURL("file:b.txt"), MimeName.MIME_PLAIN_TEXT.getMimeConstant()); //$NON-NLS-1$
		this.xml.add(4, new File("a.jpg"), MimeName.MIME_JPG.getMimeConstant()); //$NON-NLS-1$
	}
	
	@Test
	@DisplayName("compare(Object, Object)")
	public void compareObjectObject() throws Exception {
		assertZero(this.xml.compare(null, null));
		assertStrictlyPositive(this.xml.compare(new Object(), null));
		assertStrictlyNegative(this.xml.compare(null, new Object()));
		assertZero(this.xml.compare(new File("a"), new File("a"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertStrictlyNegative(this.xml.compare(new File("a"), new File("b"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertZero(this.xml.compare(newURL("file:a"), newURL("file:a"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertStrictlyNegative(this.xml.compare(newURL("file:a"), newURL("file:b"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertZero(this.xml.compare(new File("a"), newURL("file:a"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertStrictlyNegative(this.xml.compare(new File("a"), newURL("file:b"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("getStringIdentifier")
	public void getStringIdentifier() {
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", XMLResources.getStringIdentifier(0)); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "351", XMLResources.getStringIdentifier(351)); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "-4521", XMLResources.getStringIdentifier(-4521)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("getNumericalIdentifier")
	public void getNumericalIdentifier() {
		assertEquals(0, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "0")); //$NON-NLS-1$
		assertEquals(351, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "351")); //$NON-NLS-1$
		assertEquals(-4521, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "-4521")); //$NON-NLS-1$
	}

	@Test
	@DisplayName("isStringIdentifier")
	public void isStringIdentifier() {
		assertTrue(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX + "0")); //$NON-NLS-1$
		assertTrue(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX + "351")); //$NON-NLS-1$
		assertTrue(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX + "-4521")); //$NON-NLS-1$
		assertFalse(XMLResources.isStringIdentifier(null));
		assertFalse(XMLResources.isStringIdentifier("")); //$NON-NLS-1$
		assertFalse(XMLResources.isStringIdentifier("-4521")); //$NON-NLS-1$
		assertFalse(XMLResources.isStringIdentifier(XMLResources.IDENTIFIER_PREFIX));
	}

	@Test
	@DisplayName("getIdentifier(URL)")
	public void getIdentifierURL() throws Exception {
		assertEquals(-1, this.xml.getIdentifier(newURL("file:x"))); //$NON-NLS-1$
		assertEquals(2, this.xml.getIdentifier(newURL("file:a.txt"))); //$NON-NLS-1$
		assertEquals(3, this.xml.getIdentifier(newURL("file:b.txt"))); //$NON-NLS-1$
		assertEquals(4, this.xml.getIdentifier(newURL("file:a.jpg"))); //$NON-NLS-1$
	}

	@Test
	@DisplayName("getIdentifier(File)")
	public void getIdentifierFile() {
		assertEquals(-1, this.xml.getIdentifier(new File("x"))); //$NON-NLS-1$
		assertEquals(2, this.xml.getIdentifier(new File("a.txt"))); //$NON-NLS-1$
		assertEquals(3, this.xml.getIdentifier(new File("b.txt"))); //$NON-NLS-1$
		assertEquals(4, this.xml.getIdentifier(new File("a.jpg"))); //$NON-NLS-1$
	}

	@Test
	@DisplayName("getIdentifier(byte[])")
	public void getIdentifierByteAray() {
		assertEquals(-1, this.xml.getIdentifier(new byte[0]));
		assertEquals(-1, this.xml.getIdentifier(new byte[] { 1, 2, 3 }));
		assertEquals(1, this.xml.getIdentifier(this.data));
	}

	@Test
	@DisplayName("getResourceURL")
	public void getResourceURL() throws Exception {
		assertEquals(this.path.makeAbsolute(new File("a.txt")), this.xml.getResourceURL(2)); //$NON-NLS-1$
		assertEquals(this.path.makeAbsolute(newURL("file:b.txt")), this.xml.getResourceURL(3)); //$NON-NLS-1$
		assertEquals(this.path.makeAbsolute(newURL("file:a.jpg")), this.xml.getResourceURL(4)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("getResource")
	public void getResource() throws Exception {
		XMLResources.Entry e;
		e = this.xml.getResource(1);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(this.data, e.getEmbeddedData());
		assertNull(e.getFile());
		assertNull(e.getURL());
		assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(2);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(3);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(4);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("computeNextIdentifier")
	public void computeNextIdentifier() {
		assertEquals(0, this.xml.computeNextIdentifier());
		assertEquals(5, this.xml.computeNextIdentifier());
		assertEquals(6, this.xml.computeNextIdentifier());
		assertEquals(7, this.xml.computeNextIdentifier());
	}

	@Test
	@DisplayName("getPairs")
	public void getPairs() throws Exception {
		Map<Long, XMLResources.Entry> pairs = this.xml.getPairs();
		assertEquals(4, pairs.size());
	
		XMLResources.Entry e;

		e = pairs.get(1l);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(this.data, e.getEmbeddedData());
		assertNull(e.getFile());
		assertNull(e.getURL());
		assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());

		e = pairs.get(2l);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = pairs.get(3l);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = pairs.get(4l);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
	}


	@Test
	@DisplayName("add(Long, URL, String)")
	public void addLongURLString() throws Exception {
		String k = this.xml.add(5, newURL("http://www.arakhne.org"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "5", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(5);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(newURL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("add(URL)")
	public void addURL() throws Exception {
		String k = this.xml.add(newURL("http://www.arakhne.org")); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
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

	@Test
	@DisplayName("add(URL, String)")
	public void addURLString() throws Exception {
		String k = this.xml.add(newURL("http://www.arakhne.org"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(newURL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("add(Long, File, String)")
	public void addLongFileString() {
		String k = this.xml.add(5, new File("xxx.3ds"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "5", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(5);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("xxx.3ds"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("add(File)")
	public void addFile() throws Exception {
		String k = this.xml.add(new File("xxx.3ds")); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("xxx.3ds"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertNull(e.getMimeType());
	}

	@Test
	@DisplayName("add(File, String)")
	public void addFileString() throws Exception {
		String k = this.xml.add(new File("xxx.3ds"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("xxx.3ds"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("add(Long, byte[], String)")
	public void addLongByteArrayString() {
		byte[] dt = new byte[] {1, 2, 3};
		String k = this.xml.add(5, dt, MimeName.MIME_AVI.getMimeConstant());
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "5", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(5);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(dt, e.getEmbeddedData());
		assertNull(e.getURL());
		assertNull(e.getFile());
		assertEquals(MimeName.MIME_AVI.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("add(byte[])")
	public void addByteArray() throws Exception {
		byte[] dt = new byte[] {1, 2, 3};
		String k = this.xml.add(dt);
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(dt, e.getEmbeddedData());
		assertNull(e.getURL());
		assertNull(e.getFile());
		assertEquals(MimeName.MIME_OCTET_STREAM.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("add(byte[], String)")
	public void addByteArrayString() throws Exception {
		byte[] dt = new byte[] {1, 2, 3};
		String k = this.xml.add(dt, MimeName.MIME_AVI.getMimeConstant());
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(dt, e.getEmbeddedData());
		assertNull(e.getURL());
		assertNull(e.getFile());
		assertEquals(MimeName.MIME_AVI.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("remove(Long)")
	public void removeLong() {
		this.xml.remove(3l);
		
		XMLResources.Entry e;
		e = this.xml.getResource(1);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(this.data, e.getEmbeddedData());
		assertNull(e.getFile());
		assertNull(e.getURL());
		assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(2);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(4);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("remove(URL)")
	public void removeURL() throws Exception {
		this.xml.remove(newURL("file:b.txt")); //$NON-NLS-1$

		XMLResources.Entry e;
		e = this.xml.getResource(1);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(this.data, e.getEmbeddedData());
		assertNull(e.getFile());
		assertNull(e.getURL());
		assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(2);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(4);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("remove(File)")
	public void removeFile() throws Exception {
		this.xml.remove(new File("a.txt")); //$NON-NLS-1$

		XMLResources.Entry e;
		e = this.xml.getResource(1);
		assertNotNull(e);
		assertTrue(e.isEmbeddedData());
		assertSame(this.data, e.getEmbeddedData());
		assertNull(e.getFile());
		assertNull(e.getURL());
		assertEquals(MimeName.MIME_PDF.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(3);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(4);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("remove(byte[])")
	public void removeByteArray() throws Exception {
		this.xml.remove(this.data);

		XMLResources.Entry e;
		e = this.xml.getResource(2);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.txt"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(3);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(newURL("file:b.txt"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_PLAIN_TEXT.getMimeConstant(), e.getMimeType());

		e = this.xml.getResource(4);
		assertNotNull(e);
		assertTrue(e.isFile());
		assertEquals(new File("a.jpg"), e.getFile()); //$NON-NLS-1$
		assertNull(e.getURL());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_JPG.getMimeConstant(), e.getMimeType());
	}

	@Test
	@DisplayName("clear")
	public void clear() {
		this.xml.clear();
		assertNull(this.xml.getResource(0));
		assertNull(this.xml.getResource(1));
		assertNull(this.xml.getResource(2));
		assertNull(this.xml.getResource(3));
		assertNull(this.xml.getResource(4));
		assertNull(this.xml.getResource(5));
	}

}

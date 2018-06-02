/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.FileSystem;

@SuppressWarnings("all")
public class XMLResourcesTest extends AbstractTestCase {

	private SimplePathBuilder path;
	private XMLResources xml;
	private byte[] data;

	@Before
	public void setUp() throws Exception {
		this.data = new byte[] {1, 2, 3};
		this.path = new SimplePathBuilder();
		this.path.setCurrentDirectory(FileSystem.getUserHomeDirectory());
		this.xml = new XMLResources(this.path);
		fillResources();
	}
	
	@After
	public void tearDown() {
		this.data = null;
		this.path = null;
		this.xml = null;
	}
	
	protected void fillResources() throws Exception {
		this.xml.add(1, this.data, MimeName.MIME_PDF.getMimeConstant());
		this.xml.add(2, new File("a.txt"), MimeName.MIME_PLAIN_TEXT.getMimeConstant()); //$NON-NLS-1$
		this.xml.add(3, new URL("file:b.txt"), MimeName.MIME_PLAIN_TEXT.getMimeConstant()); //$NON-NLS-1$
		this.xml.add(4, new File("a.jpg"), MimeName.MIME_JPG.getMimeConstant()); //$NON-NLS-1$
	}
	
	@Test
	public void compareObjectObject() throws Exception {
		assertZero(this.xml.compare(null, null));
		assertStrictlyPositive(this.xml.compare(new Object(), null));
		assertStrictlyNegative(this.xml.compare(null, new Object()));
		assertZero(this.xml.compare(new File("a"), new File("a"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertStrictlyNegative(this.xml.compare(new File("a"), new File("b"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertZero(this.xml.compare(new URL("file:a"), new URL("file:a"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertStrictlyNegative(this.xml.compare(new URL("file:a"), new URL("file:b"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertZero(this.xml.compare(new File("a"), new URL("file:a"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertStrictlyNegative(this.xml.compare(new File("a"), new URL("file:b"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void getStringIdentifier() {
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", XMLResources.getStringIdentifier(0)); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "351", XMLResources.getStringIdentifier(351)); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "-4521", XMLResources.getStringIdentifier(-4521)); //$NON-NLS-1$
	}

	@Test
	public void getNumericalIdentifier() {
		assertEquals(0, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "0")); //$NON-NLS-1$
		assertEquals(351, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "351")); //$NON-NLS-1$
		assertEquals(-4521, XMLResources.getNumericalIdentifier(XMLResources.IDENTIFIER_PREFIX + "-4521")); //$NON-NLS-1$
	}

	@Test
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
	public void getIdentifierURL() throws Exception {
		assertEquals(-1, this.xml.getIdentifier(new URL("file:x"))); //$NON-NLS-1$
		assertEquals(2, this.xml.getIdentifier(new URL("file:a.txt"))); //$NON-NLS-1$
		assertEquals(3, this.xml.getIdentifier(new URL("file:b.txt"))); //$NON-NLS-1$
		assertEquals(4, this.xml.getIdentifier(new URL("file:a.jpg"))); //$NON-NLS-1$
	}

	@Test
	public void getIdentifierFile() {
		assertEquals(-1, this.xml.getIdentifier(new File("x"))); //$NON-NLS-1$
		assertEquals(2, this.xml.getIdentifier(new File("a.txt"))); //$NON-NLS-1$
		assertEquals(3, this.xml.getIdentifier(new File("b.txt"))); //$NON-NLS-1$
		assertEquals(4, this.xml.getIdentifier(new File("a.jpg"))); //$NON-NLS-1$
	}

	@Test
	public void getIdentifierByteAray() {
		assertEquals(-1, this.xml.getIdentifier(new byte[0]));
		assertEquals(-1, this.xml.getIdentifier(new byte[] { 1, 2, 3 }));
		assertEquals(1, this.xml.getIdentifier(this.data));
	}

	@Test
	public void getResourceURL() throws Exception {
		assertEquals(this.path.makeAbsolute(new File("a.txt")), this.xml.getResourceURL(2)); //$NON-NLS-1$
		assertEquals(this.path.makeAbsolute(new URL("file:b.txt")), this.xml.getResourceURL(3)); //$NON-NLS-1$
		assertEquals(this.path.makeAbsolute(new URL("file:a.jpg")), this.xml.getResourceURL(4)); //$NON-NLS-1$
	}

	@Test
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
		assertEquals(new URL("file:b.txt"), e.getURL()); //$NON-NLS-1$
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
	public void computeNextIdentifier() {
		assertEquals(0, this.xml.computeNextIdentifier());
		assertEquals(5, this.xml.computeNextIdentifier());
		assertEquals(6, this.xml.computeNextIdentifier());
		assertEquals(7, this.xml.computeNextIdentifier());
	}

	@Test
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
		assertEquals(new URL("file:b.txt"), e.getURL()); //$NON-NLS-1$
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
	public void addLongURLString() throws Exception {
		String k = this.xml.add(5, new URL("http://www.arakhne.org"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "5", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(5);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(new URL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
	}

	@Test
	public void addURL() throws Exception {
		String k = this.xml.add(new URL("http://www.arakhne.org")); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(new URL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		// If internet connection is up, is should be HTML mime type.
		// Otherwise it should be Octet-stream.
		String type = e.getMimeType();
		assertTrue(
			type.equals(MimeName.MIME_HTML.getMimeConstant())
			|| type.equals(MimeName.MIME_OCTET_STREAM.getMimeConstant()));
	}

	@Test
	public void addURLString() throws Exception {
		String k = this.xml.add(new URL("http://www.arakhne.org"), MimeName.MIME_3DS.getMimeConstant()); //$NON-NLS-1$
		assertEquals(XMLResources.IDENTIFIER_PREFIX + "0", k); //$NON-NLS-1$
		XMLResources.Entry e = this.xml.getResource(0);
		assertNotNull(e);
		assertTrue(e.isURL());
		assertEquals(new URL("http://www.arakhne.org"), e.getURL()); //$NON-NLS-1$
		assertNull(e.getFile());
		assertNull(e.getEmbeddedData());
		assertEquals(MimeName.MIME_3DS.getMimeConstant(), e.getMimeType());
	}

	@Test
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
	public void removeURL() throws Exception {
		this.xml.remove(new URL("file:b.txt")); //$NON-NLS-1$

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
		assertEquals(new URL("file:b.txt"), e.getURL()); //$NON-NLS-1$
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
		assertEquals(new URL("file:b.txt"), e.getURL()); //$NON-NLS-1$
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

/* 
 * $Id$
 * 
 * Copyright (C) 2010 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.vmutil.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.arakhne.afc.vmutil.Resources;
import org.arakhne.afc.vmutil.URLHandlerUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
public class URLConnectionTest {

	private static final String RESOURCE_URL = "org/arakhne/afc/vmutil/test.txt"; //$NON-NLS-1$

	private URLConnection connection;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		URL resourceUrl = Resources.getResource(RESOURCE_URL);
		assertNotNull(resourceUrl);
		this.connection = new URLConnection(resourceUrl);
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.connection = null;
		URLHandlerUtil.uninstallArakhneHandlers();
	}

	/**
	 */
	@Test
	public void getHeaderFieldKeyInt() {
		assertEquals("content-type", this.connection.getHeaderFieldKey(0)); //$NON-NLS-1$
		assertEquals("content-length", this.connection.getHeaderFieldKey(1)); //$NON-NLS-1$
		assertEquals("last-modified", this.connection.getHeaderFieldKey(2)); //$NON-NLS-1$
		assertNull(this.connection.getHeaderFieldKey(3));
	}

	/**
	 */
	@Test
	public void getHeaderFieldInt() {
		assertEquals("text/plain", this.connection.getHeaderField(0)); //$NON-NLS-1$
		assertEquals("25", this.connection.getHeaderField(1)); //$NON-NLS-1$
		assertNotNull(this.connection.getHeaderField(2));
		assertNull(this.connection.getHeaderField(3));
	}

	/**
	 */
	@Test
	public void getHeaderFieldString() {
		assertEquals("text/plain", this.connection.getHeaderField("content-type")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("25", this.connection.getHeaderField("content-length")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(this.connection.getHeaderField("last-modified")); //$NON-NLS-1$
		assertNull(this.connection.getHeaderField("expires")); //$NON-NLS-1$
	}

	/**
	 */
	@Test
	public void getHeaderFields() {
		Map<?,?> map = this.connection.getHeaderFields();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals(Collections.singletonList("text/plain"), map.get("content-type")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Collections.singletonList("25"), map.get("content-length")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(map.get("last-modified")); //$NON-NLS-1$
		assertNull(map.get("expires")); //$NON-NLS-1$
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void getInputStream() throws IOException {
		String line;
		try (InputStream is = this.connection.getInputStream()) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				line = br.readLine();
			}
		}
		assertEquals("TEST1: FOR UNIT TEST ONLY", line); //$NON-NLS-1$
	}

	/**
	 * @throws IOException
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getOutputStream() throws IOException {
		File tmpFile = File.createTempFile("unittest", ".txt"); //$NON-NLS-1$ //$NON-NLS-2$
		tmpFile.deleteOnExit();

		URLConnection con = new URLConnection(tmpFile.toURI().toURL());
		con.setDoOutput(true);

		try (OutputStream os = con.getOutputStream()) {
			try (OutputStreamWriter osw = new OutputStreamWriter(os)) {
				try (BufferedWriter bw = new BufferedWriter(osw)) {
					bw.write("HELLO WORLD!"); //$NON-NLS-1$
				}
			}
		}

		assertEquals(12, tmpFile.length());

		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(tmpFile))) {
			line = br.readLine();
		}
		assertEquals("HELLO WORLD!", line); //$NON-NLS-1$
	}

}

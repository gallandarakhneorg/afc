/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.vmutil.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.vmutil.Resources;
import org.arakhne.afc.vmutil.URLHandlerUtil;

@SuppressWarnings("all")
public class URLConnectionTest {

	private static final String RESOURCE_URL = "org/arakhne/afc/vmutil/test.txt";  //$NON-NLS-1$

	private URLConnection connection;

	/**
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		URL resourceUrl = Resources.getResource(RESOURCE_URL);
		assertNotNull(resourceUrl);
		this.connection = new URLConnection(resourceUrl);
	}

	/**
	 * @throws Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.connection = null;
		URLHandlerUtil.uninstallArakhneHandlers();
	}

	/**
	 */
	@Test
	public void getHeaderFieldKeyInt() {
		assertEquals("content-type", this.connection.getHeaderFieldKey(0));  //$NON-NLS-1$
		assertEquals("content-length", this.connection.getHeaderFieldKey(1));  //$NON-NLS-1$
		assertEquals("last-modified", this.connection.getHeaderFieldKey(2));  //$NON-NLS-1$
		assertNull(this.connection.getHeaderFieldKey(3));
	}

	/**
	 */
	@Test
	public void getHeaderFieldInt() {
		assertEquals("text/plain", this.connection.getHeaderField(0));  //$NON-NLS-1$
		assertEquals("25", this.connection.getHeaderField(1));  //$NON-NLS-1$
		assertNotNull(this.connection.getHeaderField(2));
		assertNull(this.connection.getHeaderField(3));
	}

	/**
	 */
	@Test
	public void getHeaderFieldString() {
		assertEquals("text/plain", this.connection.getHeaderField("content-type"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("25", this.connection.getHeaderField("content-length"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(this.connection.getHeaderField("last-modified"));  //$NON-NLS-1$
		assertNull(this.connection.getHeaderField("expires"));  //$NON-NLS-1$
	}

	/**
	 */
	@Test
	public void getHeaderFields() {
		Map<?,?> map = this.connection.getHeaderFields();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals(Collections.singletonList("text/plain"), map.get("content-type"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Collections.singletonList("25"), map.get("content-length"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(map.get("last-modified"));  //$NON-NLS-1$
		assertNull(map.get("expires"));  //$NON-NLS-1$
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
		assertEquals("TEST1: FOR UNIT TEST ONLY", line);  //$NON-NLS-1$
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void getOutputStream() throws IOException {
		File tmpFile = File.createTempFile("unittest", ".txt");   //$NON-NLS-1$ //$NON-NLS-2$
		tmpFile.deleteOnExit();

		URLConnection con = new URLConnection(tmpFile.toURI().toURL());
		con.setDoOutput(true);

		try (OutputStream os = con.getOutputStream()) {
			try (OutputStreamWriter osw = new OutputStreamWriter(os)) {
				try (BufferedWriter bw = new BufferedWriter(osw)) {
					bw.write("HELLO WORLD!");  //$NON-NLS-1$
				}
			}
		}

		assertEquals(12, tmpFile.length());

		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(tmpFile))) {
			line = br.readLine();
		}
		assertEquals("HELLO WORLD!", line);  //$NON-NLS-1$
	}

}

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
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.vmutil.Resources;

@SuppressWarnings("all")
public class URLConnectionTest {

	private static final String RESOURCE_PATH = "org/arakhne/afc/vmutil/test.txt";  //$NON-NLS-1$

	private URLConnection connection;

	@BeforeEach
	public void setUp() throws Exception {
		// Find the resource file
		URL resourceUrl = Resources.getResource(RESOURCE_PATH);
		// Force the usage of the specific file handler.
		resourceUrl = new URL(resourceUrl.getProtocol(), null, -1, resourceUrl.getPath(), new Handler());
		assertNotNull(resourceUrl);
		connection = new URLConnection(resourceUrl);
	}

	@AfterEach
	public void tearDown() throws Exception {
		connection = null;
	}

	@DisplayName("getHeaderFieldKey")
	@Nested
	public class GetHeaderFieldKey {

		@DisplayName("#1")
		@Test
		public void getHeaderFieldKeyInt_1() {
			assertEquals("content-type", connection.getHeaderFieldKey(0));  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void getHeaderFieldKeyInt_2() {
			assertEquals("content-length", connection.getHeaderFieldKey(1));  //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void getHeaderFieldKeyInt_3() {
			assertEquals("last-modified", connection.getHeaderFieldKey(2));  //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void getHeaderFieldKeyInt_4() {
			assertNull(connection.getHeaderFieldKey(3));
		}
	}

	@DisplayName("getHeaderField")
	@Nested
	public class GetHeaderField {

		@DisplayName("(int) #1")
		@Test
		public void getHeaderFieldInt_1() {
			assertEquals("text/plain", connection.getHeaderField(0));  //$NON-NLS-1$
		}

		@DisplayName("(int) #2")
		@Test
		public void getHeaderFieldInt_2() {
			assertEquals("25", connection.getHeaderField(1));  //$NON-NLS-1$
		}

		@DisplayName("(int) #3")
		@Test
		public void getHeaderFieldInt_3() {
			assertNotNull(connection.getHeaderField(2));
		}

		@DisplayName("(int) #4")
		@Test
		public void getHeaderFieldInt_4() {
			assertNull(connection.getHeaderField(3));
		}

		@DisplayName("(String) #1")
		@Test
		public void getHeaderFieldString_1() {
			assertEquals("text/plain", connection.getHeaderField("content-type"));   //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #2")
		@Test
		public void getHeaderFieldString_2() {
			assertEquals("25", connection.getHeaderField("content-length"));   //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #3")
		@Test
		public void getHeaderFieldString_3() {
			assertNotNull(connection.getHeaderField("last-modified"));  //$NON-NLS-1$
		}

		@DisplayName("(String) #4")
		@Test
		public void getHeaderFieldString_4() {
			assertNull(connection.getHeaderField("expires"));  //$NON-NLS-1$
		}
	}

	@DisplayName("getHeaderFields")
	@Nested
	public class GetHeaderFields {

		private Map<?,?> map;

		@BeforeEach
		public void setUp() {
			map = connection.getHeaderFields();
		}

		@DisplayName("#1")
		@Test
		public void getHeaderFields_1() {
			assertNotNull(map);
		}

		@DisplayName("#2")
		@Test
		public void getHeaderFields_2() {
			assertEquals(3, map.size());
		}

		@DisplayName("#3")
		@Test
		public void getHeaderFields_3() {
			assertEquals(Collections.singletonList("text/plain"), map.get("content-type"));   //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#4")
		@Test
		public void getHeaderFields_4() {
			assertEquals(Collections.singletonList("25"), map.get("content-length"));   //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#5")
		@Test
		public void getHeaderFields_5() {
			assertNotNull(map.get("last-modified"));  //$NON-NLS-1$
		}

		@DisplayName("#6")
		@Test
		public void getHeaderFields_6() {
			assertNull(map.get("expires"));  //$NON-NLS-1$
		}
	}

	@DisplayName("getInputStream")
	@Nested
	public class GetInputStream {

		@DisplayName("#1")
		@Test
		public void getInputStream() throws IOException {
			String line;
			try (InputStream is = connection.getInputStream()) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
					line = br.readLine();
				}
			}
			assertEquals("TEST1: FOR UNIT TEST ONLY", line);  //$NON-NLS-1$
		}
		}

	@DisplayName("getOutputStream")
	@Nested
	public class GetOutputStream {

		@DisplayName("#1")
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

}

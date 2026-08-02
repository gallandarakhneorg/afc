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

package org.arakhne.afc.vmutil.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("URLConnection")
@SuppressWarnings("all")
public class URLConnectionTest {

	private static final String RESOURCE_SCHEME = "resource";  //$NON-NLS-1$

	private static final String RESOURCE_PATH = "org/arakhne/afc/vmutil/test.txt";  //$NON-NLS-1$

	private URLConnection connection;

	@BeforeEach
	public void setUp() throws Exception {
		URL resourceUrl = new URL(RESOURCE_SCHEME, null, -1, RESOURCE_PATH, new Handler());
		assertNotNull(resourceUrl);
		this.connection = new URLConnection(resourceUrl);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.connection = null;
	}

	@DisplayName("getInputStream")
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

}

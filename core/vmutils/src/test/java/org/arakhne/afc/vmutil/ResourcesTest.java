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

package org.arakhne.afc.vmutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class ResourcesTest {

	private static final String TEST_NAME_1 = "/org/arakhne/afc/vmutil/test.txt";  //$NON-NLS-1$
	private static final String TEST_NAME_2 = "org/arakhne/afc/vmutil/test.txt";  //$NON-NLS-1$
	private static final String TEST_NAME_3 = "test.txt";  //$NON-NLS-1$
	private static final String TEST_NAME_4 = "/test.txt";  //$NON-NLS-1$
	private static final String PACKAGE_NAME = "org.arakhne.afc.vmutil";  //$NON-NLS-1$
	
	@Test
	public void getResourceString() {
		assertNull(Resources.getResource(null));

		URL u1 = Resources.getResource(TEST_NAME_1);
		assertNotNull(u1);

		URL u2 = Resources.getResource(TEST_NAME_2);
		assertNotNull(u2);

		assertEquals(u1,u2);
	}

	@Test
	public void getResourceClassString() {
		assertNull(Resources.getResource(ResourcesTest.class, null));

		URL u1 = Resources.getResource(ResourcesTest.class, TEST_NAME_1);
		assertNotNull(u1);

		URL u2 = Resources.getResource(ResourcesTest.class, TEST_NAME_2);
		assertNotNull(u2);

		URL u3 = Resources.getResource(ResourcesTest.class, TEST_NAME_3);
		assertNotNull(u3);

		assertEquals(u1,u2);
		assertEquals(u1,u3);

		assertNull(Resources.getResource((Class<?>)null, null));

		u1 = Resources.getResource((Class<?>)null, TEST_NAME_1);
		assertNull(u1);

		u2 = Resources.getResource((Class<?>)null, TEST_NAME_2);
		assertNull(u2);
	}

	@Test
	public void getResourceAsStreamString() throws IOException {
		assertNull(Resources.getResourceAsStream(null));

		try (InputStream is = Resources.getResourceAsStream(TEST_NAME_1)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(TEST_NAME_2)) {
			assertNotNull(is);
		}
	}

	@Test
	public void getResourceAsStreamClassString() throws IOException {
		assertNull(Resources.getResourceAsStream(ResourcesTest.class, null));

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_1)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_2)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_3)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_4)) {
			assertNotNull(is);
		}

		assertNull(Resources.getResourceAsStream((Class<?>)null, null));

		try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_1)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_2)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_3)) {
			assertNull(is);
		}
	}

}

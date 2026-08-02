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

package org.arakhne.afc.vmutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Resources")
@SuppressWarnings("all")
public class ResourcesTest {

	private static final String TEST_NAME_1 = "/org/arakhne/afc/vmutil/test.txt";  //$NON-NLS-1$
	private static final String TEST_NAME_2 = "org/arakhne/afc/vmutil/test.txt";  //$NON-NLS-1$
	private static final String TEST_NAME_3 = "test.txt";  //$NON-NLS-1$
	private static final String TEST_NAME_4 = "/test.txt";  //$NON-NLS-1$
	private static final String PACKAGE_NAME = "org.arakhne.afc.vmutil";  //$NON-NLS-1$
	
	@DisplayName("getResource")
	@Nested
	public class GetResource {

		@DisplayName("(String) #1")
		@Test
		public void string_1() {
			assertNull(Resources.getResource(null));
		}

		@DisplayName("(String) #2")
		@Test
		public void string_2() {
			URL u1 = Resources.getResource(TEST_NAME_1);
			assertNotNull(u1);
		}

		@DisplayName("(String) #3")
		@Test
		public void string_3() {
			URL u2 = Resources.getResource(TEST_NAME_2);
			assertNotNull(u2);
		}

		@DisplayName("(String) #4")
		@Test
		public void string_4() {
			URL u1 = Resources.getResource(TEST_NAME_1);
			URL u2 = Resources.getResource(TEST_NAME_2);
			assertEquals(u1,u2);
		}

		@DisplayName("(Class,String) #1")
		@Test
		public void test_1() {
			assertNull(Resources.getResource(ResourcesTest.class, null));
		}

		@DisplayName("(Class,String) #2")
		@Test
		public void test_2() {
			URL u1 = Resources.getResource(ResourcesTest.class, TEST_NAME_1);
			assertNotNull(u1);
		}

		@DisplayName("(Class,String) #3")
		@Test
		public void test_3() {
			URL u2 = Resources.getResource(ResourcesTest.class, TEST_NAME_2);
			assertNotNull(u2);
		}

		@DisplayName("(Class,String) #4")
		@Test
		public void test_4() {
			URL u3 = Resources.getResource(ResourcesTest.class, TEST_NAME_3);
			assertNotNull(u3);
		}

		@DisplayName("(Class,String) #5")
		@Test
		public void test_5() {
			URL u1 = Resources.getResource(ResourcesTest.class, TEST_NAME_1);
			URL u2 = Resources.getResource(ResourcesTest.class, TEST_NAME_2);
			assertEquals(u1,u2);
		}

		@DisplayName("(Class,String) #6")
		@Test
		public void test_6() {
			URL u1 = Resources.getResource(ResourcesTest.class, TEST_NAME_1);
			URL u3 = Resources.getResource(ResourcesTest.class, TEST_NAME_3);
			assertEquals(u1,u3);
		}

		@DisplayName("(Class,String) #7")
		@Test
		public void test_7() {
			assertNull(Resources.getResource((Class<?>)null, null));
		}

		@DisplayName("(Class,String) #8")
		@Test
		public void test_8() {
			var u1 = Resources.getResource((Class<?>)null, TEST_NAME_1);
			assertNull(u1);
		}

		@DisplayName("(Class,String) #9")
		@Test
		public void test_9() {
			var u2 = Resources.getResource((Class<?>)null, TEST_NAME_2);
			assertNull(u2);
		}
	}

	@DisplayName("getResourceAsStream")
	@Nested
	public class GetResourceAsStream {

		@DisplayName("(String) #1")
		@Test
		public void string_1() throws IOException {
			assertNull(Resources.getResourceAsStream(null));
		}

		@DisplayName("(String) #2")
		@Test
		public void string_2() throws IOException {
			try (InputStream is = Resources.getResourceAsStream(TEST_NAME_1)) {
				assertNotNull(is);
			}
		}

		@DisplayName("(String) #3")
		@Test
		public void string_3() throws IOException {
			try (InputStream is = Resources.getResourceAsStream(TEST_NAME_2)) {
				assertNotNull(is);
			}
		}

		@DisplayName("(Class,String) #1")
		@Test
		public void classstring_1() throws IOException {
			assertNull(Resources.getResourceAsStream(ResourcesTest.class, null));
		}

		@DisplayName("(Class,String) #2")
		@Test
		public void classstring_2() throws IOException {
			try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_1)) {
				assertNotNull(is);
			}
		}

		@DisplayName("(Class,String) #3")
		@Test
		public void classstring_3() throws IOException {
			try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_2)) {
				assertNotNull(is);
			}
		}

		@DisplayName("(Class,String) #4")
		@Test
		public void classstring_4() throws IOException {
			try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_3)) {
				assertNotNull(is);
			}
		}

		@DisplayName("(Class,String) #5")
		@Test
		public void classstring_5() throws IOException {
			try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_4)) {
				assertNotNull(is);
			}
		}

		@DisplayName("(Class,String) #6")
		@Test
		public void classstring_6() throws IOException {
			assertNull(Resources.getResourceAsStream((Class<?>)null, null));
		}

		@DisplayName("(Class,String) #7")
		@Test
		public void classstring_7() throws IOException {
			try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_1)) {
				assertNull(is);
			}
		}

		@DisplayName("(Class,String) #8")
		@Test
		public void classstring_8() throws IOException {
			try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_2)) {
				assertNull(is);
			}
		}

		@DisplayName("(Class,String) #9")
		@Test
		public void classstring_9() throws IOException {
			try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_3)) {
				assertNull(is);
			}
		}
	}

}

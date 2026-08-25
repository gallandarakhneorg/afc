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

package org.arakhne.afc.io.dbase.attr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("DBaseFileAttributeAccessor")
@SuppressWarnings("all")
public class DBaseFileAttributeAccessorTest extends AbstractTestCase {

	private URL resource;
	private DBaseFileAttributePool pool;
	private DBaseFileAttributeAccessor accessor;
	
	private static final String TEST_FILENAME = "org/arakhne/afc/io/dbase/test.dbf"; //$NON-NLS-1$
	
	@BeforeEach
	public void setUp() throws Exception {
		resource = Resources.getResource(TEST_FILENAME);
		assertNotNull(resource);
		pool = DBaseFileAttributePool.getPool(resource);
		assertNotNull(pool);
		accessor = pool.getAccessor(4);
	}

	@AfterEach
	public void tearDown() throws Exception {
		pool.close();
		accessor = null;
		pool = null;
		resource = null;
	}

	@DisplayName("getResource")
	@Nested
	public class GetResource {
		@Test
		public void testGetResource() {
			assertEquals(resource, accessor.getResource());
		}
	}

	@DisplayName("getRecordNumber")
	@Nested
	public class GetRecordNumber {
		@Test
		public void testGetRecordNumber() {
			assertEquals(4, accessor.getRecordNumber());
		}
	}

	@DisplayName("getAttributeCount")
	@Nested
	public class GetAttributeCount {
		@Test
		public void testGetAttributeCount() {
			assertEquals(4, accessor.getAttributeCount());
		}
	}

	@DisplayName("getAllAttributeNames")
	@Nested
	public class GetAllAttributeNames {

		@DisplayName("testGetAllAttributeNames_1")
		@Test
		public void testGetAllAttributeNames_1() {
			Collection<String> names = accessor.getAllAttributeNames();
			assertNotNull(names);
		}

		@DisplayName("testGetAllAttributeNames_2")
		@Test
		public void testGetAllAttributeNames_2() {
			Collection<String> names = accessor.getAllAttributeNames();
			assertEquals(4, names.size());
		}

		@DisplayName("testGetAllAttributeNames_3")
		@Test
		public void testGetAllAttributeNames_3() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("testGetAllAttributeNames_4")
		@Test
		public void testGetAllAttributeNames_4() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
		}

		@DisplayName("testGetAllAttributeNames_5")
		@Test
		public void testGetAllAttributeNames_5() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			iterator.next();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("testGetAllAttributeNames_6")
		@Test
		public void testGetAllAttributeNames_6() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			iterator.next();
			assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
		}

		@DisplayName("testGetAllAttributeNames_7")
		@Test
		public void testGetAllAttributeNames_7() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			iterator.next();
			iterator.next();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("testGetAllAttributeNames_8")
		@Test
		public void testGetAllAttributeNames_8() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			iterator.next();
			iterator.next();
			assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
		}

		@DisplayName("testGetAllAttributeNames_9")
		@Test
		public void testGetAllAttributeNames_9() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			iterator.next();
			iterator.next();
			iterator.next();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("testGetAllAttributeNames_10")
		@Test
		public void testGetAllAttributeNames_10() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			iterator.next();
			iterator.next();
			iterator.next();
			assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
		}

		@DisplayName("testGetAllAttributeNames_11")
		@Test
		public void testGetAllAttributeNames_11() {
			Collection<String> names = accessor.getAllAttributeNames();
			Iterator<String> iterator = names.iterator();
			while (iterator.hasNext()) {
				iterator.next();
			}
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("loadValue")
	@Nested
	public class LoadValue {

		@DisplayName("testLoadValue_1")
		@Test
		public void testLoadValue_1() throws Exception {
			AttributeValue value = accessor.loadValue("CATEGORIE"); //$NON-NLS-1$
			assertNotNull(value);
		}

		@DisplayName("testLoadValue_2")
		@Test
		public void testLoadValue_2() throws Exception {
			AttributeValue value = accessor.loadValue("CATEGORIE"); //$NON-NLS-1$
			assertEquals("Religieux", value.getValue()); //$NON-NLS-1$
		}
	}
}

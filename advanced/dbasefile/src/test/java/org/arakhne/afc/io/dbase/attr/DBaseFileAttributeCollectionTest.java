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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;
import org.arakhne.afc.vmutil.locale.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("DBaseFileAttributeCollection")
@SuppressWarnings("all")
public class DBaseFileAttributeCollectionTest extends AbstractTestCase {

	private static final String PHOTOGRAMMETRIE_VALUE;
	
	static {
		PHOTOGRAMMETRIE_VALUE = Locale.getStringWithDefaultFrom(
				"org/arakhne/afc/io/dbase/test", //$NON-NLS-1$
				"PHOTOGRAMMETRIE", //$NON-NLS-1$
				null);
		assert(PHOTOGRAMMETRIE_VALUE!=null);
	}
	
	private URL resource;
	private DBaseFileAttributeCollection provider;
	
	private static final String TEST_FILENAME = "org/arakhne/afc/io/dbase/test.dbf"; //$NON-NLS-1$

	@BeforeEach
	public void setUp() throws Exception {
		resource = Resources.getResource(TEST_FILENAME);
		assertNotNull(resource);
		provider = DBaseFileAttributePool.getCollection(resource, 19);
	}

	@AfterEach
	public void tearDown() throws Exception {
		provider = null;
		resource = null;
	}

	@DisplayName("getResource")
	@Nested
	public class GetResource {
		@Test
		public void testGetResource() {
			assertEquals(resource, provider.getResource());
		}
	}

	@DisplayName("getRecordNumber")
	@Nested
	public class GetRecordNumber {
		@Test
		public void testGetRecordNumber() {
			assertEquals(19, provider.getRecordNumber());
		}
	}

	@DisplayName("getAttributeCount")
	@Nested
	public class GetAttributeCount {
		@Test
		public void testGetAttributeCount() {
			assertEquals(4, provider.getAttributeCount());
		}
	}

	@DisplayName("hasAttribute")
	@Nested
	public class HasAttribute {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testHasAttribute_1")
			@Test
			public void testHasAttribute_1() {
				assertTrue(provider.hasAttribute("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_2")
			@Test
			public void testHasAttribute_2() {
				assertTrue(provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_3")
			@Test
			public void testHasAttribute_3() {
				assertTrue(provider.hasAttribute("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_4")
			@Test
			public void testHasAttribute_4() {
				assertTrue(provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_5")
			@Test
			public void testHasAttribute_5() {
				assertFalse(provider.hasAttribute("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_6")
			@Test
			public void testHasAttribute_6() {
				assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After set")
		@Nested
		public class Set {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_1")
			@Test
			public void testHasAttribute_1() {
				assertTrue(provider.hasAttribute("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_2")
			@Test
			public void testHasAttribute_2() {
				assertTrue(provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_3")
			@Test
			public void testHasAttribute_3() {
				assertTrue(provider.hasAttribute("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_4")
			@Test
			public void testHasAttribute_4() {
				assertTrue(provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_5")
			@Test
			public void testHasAttribute_5() {
				assertFalse(provider.hasAttribute("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_6")
			@Test
			public void testHasAttribute_6() {
				assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class Remove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				provider.removeAttribute("SOURCE"); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_1")
			@Test
			public void testHasAttribute_1() {
				assertTrue(provider.hasAttribute("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_2")
			@Test
			public void testHasAttribute_2() {
				assertTrue(provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_3")
			@Test
			public void testHasAttribute_3() {
				assertTrue(provider.hasAttribute("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_4")
			@Test
			public void testHasAttribute_4() {
				assertTrue(provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_5")
			@Test
			public void testHasAttribute_5() {
				assertFalse(provider.hasAttribute("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttribute_6")
			@Test
			public void testHasAttribute_6() {
				assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
			}
		}
	}

	@DisplayName("hasAttributeInDBase")
	@Nested
	public class HasAttributeInDBase {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testHasAttributeInDBase_1")
			@Test
			public void testHasAttributeInDBase_1() {
				assertTrue(provider.hasAttributeInDBase("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_2")
			@Test
			public void testHasAttributeInDBase_2() {
				assertTrue(provider.hasAttributeInDBase("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_3")
			@Test
			public void testHasAttributeInDBase_3() {
				assertTrue(provider.hasAttributeInDBase("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_4")
			@Test
			public void testHasAttributeInDBase_4() {
				assertTrue(provider.hasAttributeInDBase("HAUTEUR")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_5")
			@Test
			public void testHasAttributeInDBase_5() {
				assertFalse(provider.hasAttributeInDBase("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_6")
			@Test
			public void testHasAttributeInDBase_6() {
				assertFalse(provider.hasAttributeInDBase("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After set")
		@Nested
		public class Set {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_1")
			@Test
			public void testHasAttributeInDBase_1() {
				assertTrue(provider.hasAttributeInDBase("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_2")
			@Test
			public void testHasAttributeInDBase_2() {
				assertTrue(provider.hasAttributeInDBase("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_3")
			@Test
			public void testHasAttributeInDBase_3() {
				assertTrue(provider.hasAttributeInDBase("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_4")
			@Test
			public void testHasAttributeInDBase_4() {
				assertTrue(provider.hasAttributeInDBase("HAUTEUR")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_5")
			@Test
			public void testHasAttributeInDBase_5() {
				assertFalse(provider.hasAttributeInDBase("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_6")
			@Test
			public void testHasAttributeInDBase_6() {
				assertFalse(provider.hasAttributeInDBase("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class Remove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				provider.removeAttribute("SOURCE"); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_1")
			@Test
			public void testHasAttributeInDBase_1() {
				assertTrue(provider.hasAttributeInDBase("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_2")
			@Test
			public void testHasAttributeInDBase_2() {
				assertTrue(provider.hasAttributeInDBase("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_3")
			@Test
			public void testHasAttributeInDBase_3() {
				assertTrue(provider.hasAttributeInDBase("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_4")
			@Test
			public void testHasAttributeInDBase_4() {
				assertTrue(provider.hasAttributeInDBase("HAUTEUR")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_5")
			@Test
			public void testHasAttributeInDBase_5() {
				assertFalse(provider.hasAttributeInDBase("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testHasAttributeInDBase_6")
			@Test
			public void testHasAttributeInDBase_6() {
				assertFalse(provider.hasAttributeInDBase("NEWFIELD")); //$NON-NLS-1$
			}
		}
	}

	@DisplayName("getAllAttributeNames")
	@Nested
	public class GetAllAttributeNames {
		@DisplayName("No change")
		@Nested
		public class NoChange {

			private Collection<String> names;
			private Iterator<String> iterator;

			@BeforeEach
			public void setUp() {
				names = provider.getAllAttributeNames();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllAttributeNames_1")
			@Test
			public void testGetAllAttributeNames_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllAttributeNames_2")
			@Test
			public void testGetAllAttributeNames_2() {
				assertEquals(4, names.size());
			}

			@DisplayName("testGetAllAttributeNames_3")
			@Test
			public void testGetAllAttributeNames_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_4")
			@Test
			public void testGetAllAttributeNames_4() {
				assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_5")
			@Test
			public void testGetAllAttributeNames_5() {
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_6")
			@Test
			public void testGetAllAttributeNames_6() {
				iterator.next();
				assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_7")
			@Test
			public void testGetAllAttributeNames_7() {
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_8")
			@Test
			public void testGetAllAttributeNames_8() {
				iterator.next();
				iterator.next();
				assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_9")
			@Test
			public void testGetAllAttributeNames_9() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_10")
			@Test
			public void testGetAllAttributeNames_10() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_11")
			@Test
			public void testGetAllAttributeNames_11() {
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After set")
		@Nested
		public class Set {

			private Collection<String> names;
			private Iterator<String> iterator;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				names = provider.getAllAttributeNames();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllAttributeNames_1")
			@Test
			public void testGetAllAttributeNames_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllAttributeNames_2")
			@Test
			public void testGetAllAttributeNames_2() {
				assertEquals(5, names.size());
			}

			@DisplayName("testGetAllAttributeNames_3")
			@Test
			public void testGetAllAttributeNames_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_4")
			@Test
			public void testGetAllAttributeNames_4() {
				assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_5")
			@Test
			public void testGetAllAttributeNames_5() {
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_6")
			@Test
			public void testGetAllAttributeNames_6() {
				iterator.next();
				assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_7")
			@Test
			public void testGetAllAttributeNames_7() {
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_8")
			@Test
			public void testGetAllAttributeNames_8() {
				iterator.next();
				iterator.next();
				assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_9")
			@Test
			public void testGetAllAttributeNames_9() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_10")
			@Test
			public void testGetAllAttributeNames_10() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertEquals("NEWFIELD", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_11")
			@Test
			public void testGetAllAttributeNames_11() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_12")
			@Test
			public void testGetAllAttributeNames_12() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_13")
			@Test
			public void testGetAllAttributeNames_13() {
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After remove")
		@Nested
		public class Remove {

			private Collection<String> names;
			private Iterator<String> iterator;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				provider.removeAttribute("SOURCE"); //$NON-NLS-1$
				names = provider.getAllAttributeNames();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllAttributeNames_1")
			@Test
			public void testGetAllAttributeNames_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllAttributeNames_2")
			@Test
			public void testGetAllAttributeNames_2() {
				assertEquals(5, names.size());
			}

			@DisplayName("testGetAllAttributeNames_3")
			@Test
			public void testGetAllAttributeNames_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_4")
			@Test
			public void testGetAllAttributeNames_4() {
				assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_5")
			@Test
			public void testGetAllAttributeNames_5() {
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_6")
			@Test
			public void testGetAllAttributeNames_6() {
				iterator.next();
				assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_7")
			@Test
			public void testGetAllAttributeNames_7() {
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_8")
			@Test
			public void testGetAllAttributeNames_8() {
				iterator.next();
				iterator.next();
				assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_9")
			@Test
			public void testGetAllAttributeNames_9() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_10")
			@Test
			public void testGetAllAttributeNames_10() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertEquals("NEWFIELD", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_11")
			@Test
			public void testGetAllAttributeNames_11() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributeNames_12")
			@Test
			public void testGetAllAttributeNames_12() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributeNames_13")
			@Test
			public void testGetAllAttributeNames_13() {
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}
	}

	@DisplayName("getAttributeObjectFromDBase")
	@Nested
	public class GetAttributeObjectFromDBase {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeObjectFromDBase_1")
			@Test
			public void testGetAttributeObjectFromDBase_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE,
						provider.getAttributeObjectFromDBase("SOURCE").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_2")
			@Test
			public void testGetAttributeObjectFromDBase_2() throws Exception {
				assertEquals(15.,
						provider.getAttributeObjectFromDBase("HAUTEUR").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_3")
			@Test
			public void testGetAttributeObjectFromDBase_3() throws Exception {
				assertNull(provider.getAttributeObjectFromDBase("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_4")
			@Test
			public void testGetAttributeObjectFromDBase_4() throws Exception {
				assertNull(provider.getAttributeObjectFromDBase("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_1")
			@Test
			public void testGetAttributeObjectFromDBase_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE,
						provider.getAttributeObjectFromDBase("SOURCE").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_2")
			@Test
			public void testGetAttributeObjectFromDBase_2() throws Exception {
				assertEquals(15.,
						provider.getAttributeObjectFromDBase("HAUTEUR").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_3")
			@Test
			public void testGetAttributeObjectFromDBase_3() throws Exception {
				assertNull(provider.getAttributeObjectFromDBase("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_4")
			@Test
			public void testGetAttributeObjectFromDBase_4() throws Exception {
				assertNull(provider.getAttributeObjectFromDBase("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				provider.removeAttribute("SOURCE"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_1")
			@Test
			public void testGetAttributeObjectFromDBase_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE,
						provider.getAttributeObjectFromDBase("SOURCE").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_2")
			@Test
			public void testGetAttributeObjectFromDBase_2() throws Exception {
				assertEquals(15.,
						provider.getAttributeObjectFromDBase("HAUTEUR").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_3")
			@Test
			public void testGetAttributeObjectFromDBase_3() throws Exception {
				assertNull(provider.getAttributeObjectFromDBase("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObjectFromDBase_4")
			@Test
			public void testGetAttributeObjectFromDBase_4() throws Exception {
				assertNull(provider.getAttributeObjectFromDBase("NEWFIELD")); //$NON-NLS-1$
			}
		}
	}

	@DisplayName("getAttributeObject")
	@Nested
	public class GetAttributeObject {
		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeObject_1")
			@Test
			public void testGetAttributeObject_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE,
						provider.getAttributeObject("SOURCE").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_2")
			@Test
			public void testGetAttributeObject_2() throws Exception {
				assertEquals(15.,
						provider.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_3")
			@Test
			public void testGetAttributeObject_3() throws Exception {
				assertNull(provider.getAttributeObject("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_4")
			@Test
			public void testGetAttributeObject_4() throws Exception {
				assertNull(provider.getAttributeObject("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_1")
			@Test
			public void testGetAttributeObject_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE,
						provider.getAttributeObject("SOURCE").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_2")
			@Test
			public void testGetAttributeObject_2() throws Exception {
				assertEquals(15.,
						provider.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_3")
			@Test
			public void testGetAttributeObject_3() throws Exception {
				assertNull(provider.getAttributeObject("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_4")
			@Test
			public void testGetAttributeObject_4() throws Exception {
				assertEquals(1.,
						provider.getAttributeObject("NEWFIELD").getValue()); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				provider.removeAttribute("SOURCE"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_1")
			@Test
			public void testGetAttributeObject_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE,
						provider.getAttributeObject("SOURCE").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_2")
			@Test
			public void testGetAttributeObject_2() throws Exception {
				assertEquals(15.,
						provider.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_3")
			@Test
			public void testGetAttributeObject_3() throws Exception {
				assertNull(provider.getAttributeObject("NOFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeObject_4")
			@Test
			public void testGetAttributeObject_4() throws Exception {
				assertEquals(1.,
						provider.getAttributeObject("NEWFIELD").getValue()); //$NON-NLS-1$
			}
		}
	}

	@DisplayName("getAttribute")
	@Nested
	public class GetAttribute {

		@DisplayName("(String)")
		@Nested
		public class WithString {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testGetAttributeString_1")
				@Test
				public void testGetAttributeString_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE,
							provider.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_2")
				@Test
				public void testGetAttributeString_2() throws Exception {
					assertEquals(15.,
							provider.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_3")
				@Test
				public void testGetAttributeString_3() throws Exception {
					assertNull(provider.getAttribute("NOFIELD")); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_4")
				@Test
				public void testGetAttributeString_4() throws Exception {
					assertNull(provider.getAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_1")
				@Test
				public void testGetAttributeString_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE,
							provider.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_2")
				@Test
				public void testGetAttributeString_2() throws Exception {
					assertEquals(15.,
							provider.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_3")
				@Test
				public void testGetAttributeString_3() throws Exception {
					assertNull(provider.getAttribute("NOFIELD")); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_4")
				@Test
				public void testGetAttributeString_4() throws Exception {
					assertEquals(1.,
							provider.getAttribute("NEWFIELD").getValue()); //$NON-NLS-1$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
					provider.removeAttribute("SOURCE"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_1")
				@Test
				public void testGetAttributeString_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE,
							provider.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_2")
				@Test
				public void testGetAttributeString_2() throws Exception {
					assertEquals(15.,
							provider.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_3")
				@Test
				public void testGetAttributeString_3() throws Exception {
					assertNull(provider.getAttribute("NOFIELD")); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeString_4")
				@Test
				public void testGetAttributeString_4() throws Exception {
					assertEquals(1.,
							provider.getAttribute("NEWFIELD").getValue()); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,AttributeValue)")
		@Nested
		public class WithStringAttributeValue {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				private AttributeValue defaultValue;

				@BeforeEach
				public void setUp() {
					defaultValue = new AttributeValueImpl(false);
				}

				@DisplayName("testGetAttributeStringAttributeValue_1")
				@Test
				public void testGetAttributeStringAttributeValue_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE,
							provider.getAttribute("SOURCE", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_2")
				@Test
				public void testGetAttributeStringAttributeValue_2() throws Exception {
					assertEquals(15.,
							provider.getAttribute("HAUTEUR", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_3")
				@Test
				public void testGetAttributeStringAttributeValue_3() throws Exception {
					assertEquals(false, provider.getAttribute("NOFIELD", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_4")
				@Test
				public void testGetAttributeStringAttributeValue_4() throws Exception {
					assertEquals(false, provider.getAttribute("NEWFIELD", defaultValue).getValue()); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				private AttributeValue defaultValue;

				@BeforeEach
				public void setUp() {
					defaultValue = new AttributeValueImpl(false);
					provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_1")
				@Test
				public void testGetAttributeStringAttributeValue_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE,
							provider.getAttribute("SOURCE", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_2")
				@Test
				public void testGetAttributeStringAttributeValue_2() throws Exception {
					assertEquals(15.,
							provider.getAttribute("HAUTEUR", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_3")
				@Test
				public void testGetAttributeStringAttributeValue_3() throws Exception {
					assertEquals(false, provider.getAttribute("NOFIELD", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_4")
				@Test
				public void testGetAttributeStringAttributeValue_4() throws Exception {
					assertEquals(1., provider.getAttribute("NEWFIELD", defaultValue).getValue()); //$NON-NLS-1$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				private AttributeValue defaultValue;

				@BeforeEach
				public void setUp() {
					defaultValue = new AttributeValueImpl(false);
					provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
					provider.removeAttribute("SOURCE"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_1")
				@Test
				public void testGetAttributeStringAttributeValue_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE,
							provider.getAttribute("SOURCE", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_2")
				@Test
				public void testGetAttributeStringAttributeValue_2() throws Exception {
					assertEquals(15.,
							provider.getAttribute("HAUTEUR", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_3")
				@Test
				public void testGetAttributeStringAttributeValue_3() throws Exception {
					assertEquals(false, provider.getAttribute("NOFIELD", defaultValue).getValue()); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringAttributeValue_4")
				@Test
				public void testGetAttributeStringAttributeValue_4() throws Exception {
					assertEquals(1., provider.getAttribute("NEWFIELD", defaultValue).getValue()); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,boolean)")
		@Nested
		public class WithStringBoolean {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testGetAttributeStringBoolean_1")
				@Test
				public void testGetAttributeStringBoolean_1() throws Exception {
					assertTrue(provider.getAttribute("SOURCE", true)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringBoolean_2")
				@Test
				public void testGetAttributeStringBoolean_2() throws Exception {
					assertTrue(provider.getAttribute("NEWFIELD", true)); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", false); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringBoolean_1")
				@Test
				public void testGetAttributeStringBoolean_1() throws Exception {
					assertTrue(provider.getAttribute("SOURCE", true)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringBoolean_2")
				@Test
				public void testGetAttributeStringBoolean_2() throws Exception {
					assertFalse(provider.getAttribute("NEWFIELD", true)); //$NON-NLS-1$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", false); //$NON-NLS-1$
					provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringBoolean_1")
				@Test
				public void testGetAttributeStringBoolean_1() throws Exception {
					assertTrue(provider.getAttribute("SOURCE", true)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringBoolean_2")
				@Test
				public void testGetAttributeStringBoolean_2() throws Exception {
					assertTrue(provider.getAttribute("NEWFIELD", true)); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,double)")
		@Nested
		public class WithStringDouble {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testGetAttributeStringDouble_1")
				@Test
				public void testGetAttributeStringDouble_1() throws Exception {
					assertEpsilonEquals(10., provider.getAttribute("SOURCE", 10.)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringDouble_2")
				@Test
				public void testGetAttributeStringDouble_2() throws Exception {
					assertEpsilonEquals(11., provider.getAttribute("NEWFIELD", 11.)); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringDouble_1")
				@Test
				public void testGetAttributeStringDouble_1() throws Exception {
					assertEpsilonEquals(14., provider.getAttribute("SOURCE", 14.)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringDouble_2")
				@Test
				public void testGetAttributeStringDouble_2() throws Exception {
					assertEpsilonEquals(7., provider.getAttribute("NEWFIELD", 15.)); //$NON-NLS-1$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
					provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringDouble_1")
				@Test
				public void testGetAttributeStringDouble_1() throws Exception {
					assertEpsilonEquals(14., provider.getAttribute("SOURCE", 14.)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringDouble_2")
				@Test
				public void testGetAttributeStringDouble_2() throws Exception {
					assertEpsilonEquals(15., provider.getAttribute("NEWFIELD", 15.)); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,String)")
		@Nested
		public class WithStringString {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testGetAttributeStringString_1")
				@Test
				public void testGetAttributeStringString_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE, provider.getAttribute("SOURCE", "a")); //$NON-NLS-1$ //$NON-NLS-2$
				}

				@DisplayName("testGetAttributeStringString_2")
				@Test
				public void testGetAttributeStringString_2() throws Exception {
					assertEquals("b", provider.getAttribute("NEWFIELD", "b")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", "c"); //$NON-NLS-1$ //$NON-NLS-2$
				}

				@DisplayName("testGetAttributeStringString_1")
				@Test
				public void testGetAttributeStringString_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE, provider.getAttribute("SOURCE", "d")); //$NON-NLS-1$ //$NON-NLS-2$
				}

				@DisplayName("testGetAttributeStringString_2")
				@Test
				public void testGetAttributeStringString_2() throws Exception {
					assertEquals("c", provider.getAttribute("NEWFIELD", "e")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", "c"); //$NON-NLS-1$ //$NON-NLS-2$
					provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringString_1")
				@Test
				public void testGetAttributeStringString_1() throws Exception {
					assertEquals(PHOTOGRAMMETRIE_VALUE, provider.getAttribute("SOURCE", "d")); //$NON-NLS-1$ //$NON-NLS-2$
				}

				@DisplayName("testGetAttributeStringString_2")
				@Test
				public void testGetAttributeStringString_2() throws Exception {
					assertEquals("e", provider.getAttribute("NEWFIELD", "e")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
		}

		@DisplayName("(String,int)")
		@Nested
		public class WithStringInt {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testGetAttributeStringInt_1")
				@Test
				public void testGetAttributeStringInt_1() throws Exception {
					assertEquals(10, provider.getAttribute("SOURCE", 10)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringInt_2")
				@Test
				public void testGetAttributeStringInt_2() throws Exception {
					assertEquals(11, provider.getAttribute("NEWFIELD", 11)); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringInt_1")
				@Test
				public void testGetAttributeStringInt_1() throws Exception {
					assertEquals(14, provider.getAttribute("SOURCE", 14)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringInt_2")
				@Test
				public void testGetAttributeStringInt_2() throws Exception {
					assertEquals(7, provider.getAttribute("NEWFIELD", 15)); //$NON-NLS-1$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7); //$NON-NLS-1$
					provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringInt_1")
				@Test
				public void testGetAttributeStringInt_1() throws Exception {
					assertEquals(14, provider.getAttribute("SOURCE", 14)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringInt_2")
				@Test
				public void testGetAttributeStringInt_2() throws Exception {
					assertEquals(15, provider.getAttribute("NEWFIELD", 15)); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,float)")
		@Nested
		public class WithStringFloat {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testGetAttributeStringFloat_1")
				@Test
				public void testGetAttributeStringFloat_1() throws Exception {
					assertEpsilonEquals(10f, provider.getAttribute("SOURCE", 10f)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringFloat_2")
				@Test
				public void testGetAttributeStringFloat_2() throws Exception {
					assertEpsilonEquals(11f, provider.getAttribute("NEWFIELD", 11f)); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7f); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringFloat_1")
				@Test
				public void testGetAttributeStringFloat_1() throws Exception {
					assertEpsilonEquals(14f, provider.getAttribute("SOURCE", 14f)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringFloat_2")
				@Test
				public void testGetAttributeStringFloat_2() throws Exception {
					assertEpsilonEquals(7f, provider.getAttribute("NEWFIELD", 15f)); //$NON-NLS-1$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7f); //$NON-NLS-1$
					provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringFloat_1")
				@Test
				public void testGetAttributeStringFloat_1() throws Exception {
					assertEpsilonEquals(14f, provider.getAttribute("SOURCE", 14f)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringFloat_2")
				@Test
				public void testGetAttributeStringFloat_2() throws Exception {
					assertEpsilonEquals(15f, provider.getAttribute("NEWFIELD", 15f)); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,long)")
		@Nested
		public class WithStringLong {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testGetAttributeStringLong_1")
				@Test
				public void testGetAttributeStringLong_1() throws Exception {
					assertEquals(10l, provider.getAttribute("SOURCE", 10l)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringLong_2")
				@Test
				public void testGetAttributeStringLong_2() throws Exception {
					assertEquals(11l, provider.getAttribute("NEWFIELD", 11l)); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7l); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringLong_1")
				@Test
				public void testGetAttributeStringLong_1() throws Exception {
					assertEquals(14l, provider.getAttribute("SOURCE", 14l)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringLong_2")
				@Test
				public void testGetAttributeStringLong_2() throws Exception {
					assertEquals(7l, provider.getAttribute("NEWFIELD", 15l)); //$NON-NLS-1$
				}
			}

			@DisplayName("After remove")
			@Nested
			public class AfterRemove {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 7l); //$NON-NLS-1$
					provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringLong_1")
				@Test
				public void testGetAttributeStringLong_1() throws Exception {
					assertEquals(14l, provider.getAttribute("SOURCE", 14l)); //$NON-NLS-1$
				}

				@DisplayName("testGetAttributeStringLong_2")
				@Test
				public void testGetAttributeStringLong_2() throws Exception {
					assertEquals(15l, provider.getAttribute("NEWFIELD", 15l)); //$NON-NLS-1$
				}
			}
		}
	}

	@DisplayName("getAllAttributes")
	@Nested
	public class GetAllAttributes {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			private Collection<Attribute> attrs;
			private Iterator<Attribute> iterator;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				attrs = provider.getAllAttributes();
				iterator = attrs.iterator();
				attr = null;
			}

			@DisplayName("testGetAllAttributes_1")
			@Test
			public void testGetAllAttributes_1() {
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributes_2")
			@Test
			public void testGetAllAttributes_2() {
				assertEquals(4, attrs.size());
			}

			@DisplayName("testGetAllAttributes_3")
			@Test
			public void testGetAllAttributes_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_4")
			@Test
			public void testGetAllAttributes_4() {
				attr = iterator.next();
				assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_5")
			@Test
			public void testGetAllAttributes_5() throws Exception {
				attr = iterator.next();
				assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_6")
			@Test
			public void testGetAllAttributes_6() {
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_7")
			@Test
			public void testGetAllAttributes_7() {
				iterator.next();
				attr = iterator.next();
				assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_8")
			@Test
			public void testGetAllAttributes_8() throws Exception {
				iterator.next();
				attr = iterator.next();
				assertEquals(15., attr.getValue());
			}

			@DisplayName("testGetAllAttributes_9")
			@Test
			public void testGetAllAttributes_9() {
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_10")
			@Test
			public void testGetAllAttributes_10() {
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_11")
			@Test
			public void testGetAllAttributes_11() throws Exception {
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_12")
			@Test
			public void testGetAllAttributes_12() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_13")
			@Test
			public void testGetAllAttributes_13() {
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_14")
			@Test
			public void testGetAllAttributes_14() throws Exception {
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			}

			@DisplayName("testGetAllAttributes_15")
			@Test
			public void testGetAllAttributes_15() {
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			private Collection<Attribute> attrs;
			private Iterator<Attribute> iterator;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				attrs = provider.getAllAttributes();
				iterator = attrs.iterator();
				attr = null;
			}

			@DisplayName("testGetAllAttributes_1")
			@Test
			public void testGetAllAttributes_1() {
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributes_2")
			@Test
			public void testGetAllAttributes_2() {
				assertEquals(5, attrs.size());
			}

			@DisplayName("testGetAllAttributes_3")
			@Test
			public void testGetAllAttributes_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_4")
			@Test
			public void testGetAllAttributes_4() {
				attr = iterator.next();
				assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_5")
			@Test
			public void testGetAllAttributes_5() throws Exception {
				attr = iterator.next();
				assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_6")
			@Test
			public void testGetAllAttributes_6() {
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_7")
			@Test
			public void testGetAllAttributes_7() {
				iterator.next();
				attr = iterator.next();
				assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_8")
			@Test
			public void testGetAllAttributes_8() throws Exception {
				iterator.next();
				attr = iterator.next();
				assertEquals(15., attr.getValue());
			}

			@DisplayName("testGetAllAttributes_9")
			@Test
			public void testGetAllAttributes_9() {
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_10")
			@Test
			public void testGetAllAttributes_10() {
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_11")
			@Test
			public void testGetAllAttributes_11() throws Exception {
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_12")
			@Test
			public void testGetAllAttributes_12() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_13")
			@Test
			public void testGetAllAttributes_13() {
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_14")
			@Test
			public void testGetAllAttributes_14() throws Exception {
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals(1., attr.getValue());
			}

			@DisplayName("testGetAllAttributes_15")
			@Test
			public void testGetAllAttributes_15() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_16")
			@Test
			public void testGetAllAttributes_16() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_17")
			@Test
			public void testGetAllAttributes_17() throws Exception {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			}

			@DisplayName("testGetAllAttributes_18")
			@Test
			public void testGetAllAttributes_18() {
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			private Collection<Attribute> attrs;
			private Iterator<Attribute> iterator;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				provider.removeAttribute("SOURCE"); //$NON-NLS-1$
				attrs = provider.getAllAttributes();
				iterator = attrs.iterator();
				attr = null;
			}

			@DisplayName("testGetAllAttributes_1")
			@Test
			public void testGetAllAttributes_1() {
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributes_2")
			@Test
			public void testGetAllAttributes_2() {
				assertEquals(5, attrs.size());
			}

			@DisplayName("testGetAllAttributes_3")
			@Test
			public void testGetAllAttributes_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_4")
			@Test
			public void testGetAllAttributes_4() {
				attr = iterator.next();
				assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_5")
			@Test
			public void testGetAllAttributes_5() throws Exception {
				attr = iterator.next();
				assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_6")
			@Test
			public void testGetAllAttributes_6() {
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_7")
			@Test
			public void testGetAllAttributes_7() {
				iterator.next();
				attr = iterator.next();
				assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_8")
			@Test
			public void testGetAllAttributes_8() throws Exception {
				iterator.next();
				attr = iterator.next();
				assertEquals(15., attr.getValue());
			}

			@DisplayName("testGetAllAttributes_9")
			@Test
			public void testGetAllAttributes_9() {
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_10")
			@Test
			public void testGetAllAttributes_10() {
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_11")
			@Test
			public void testGetAllAttributes_11() throws Exception {
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_12")
			@Test
			public void testGetAllAttributes_12() {
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_13")
			@Test
			public void testGetAllAttributes_13() {
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_14")
			@Test
			public void testGetAllAttributes_14() throws Exception {
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals(1., attr.getValue());
			}

			@DisplayName("testGetAllAttributes_15")
			@Test
			public void testGetAllAttributes_15() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributes_16")
			@Test
			public void testGetAllAttributes_16() {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributes_17")
			@Test
			public void testGetAllAttributes_17() throws Exception {
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			}

			@DisplayName("testGetAllAttributes_18")
			@Test
			public void testGetAllAttributes_18() {
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}
	}

	@DisplayName("getAllAttributesByType")
	@Nested
	public class GetAllAttributesByType {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			private Map<AttributeType, Collection<Attribute>> themap;
			private Collection<Attribute> attrs;
			private Iterator<Attribute> iterator;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				themap = provider.getAllAttributesByType();
				attrs = null;
				iterator = null;
				attr = null;
			}

			@DisplayName("testGetAllAttributesByType_1")
			@Test
			public void testGetAllAttributesByType_1() {
				assertNotNull(themap);
			}

			@DisplayName("testGetAllAttributesByType_2")
			@Test
			public void testGetAllAttributesByType_2() {
				assertEquals(2, themap.size());
			}

			@DisplayName("testGetAllAttributesByType_3")
			@Test
			public void testGetAllAttributesByType_3() {
				attrs = themap.get(AttributeType.STRING);
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributesByType_4")
			@Test
			public void testGetAllAttributesByType_4() {
				attrs = themap.get(AttributeType.STRING);
				assertEquals(3, attrs.size());
			}

			@DisplayName("testGetAllAttributesByType_5")
			@Test
			public void testGetAllAttributesByType_5() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_6")
			@Test
			public void testGetAllAttributesByType_6() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_7")
			@Test
			public void testGetAllAttributesByType_7() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_8")
			@Test
			public void testGetAllAttributesByType_8() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_9")
			@Test
			public void testGetAllAttributesByType_9() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_10")
			@Test
			public void testGetAllAttributesByType_10() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_11")
			@Test
			public void testGetAllAttributesByType_11() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_12")
			@Test
			public void testGetAllAttributesByType_12() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_13")
			@Test
			public void testGetAllAttributesByType_13() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_14")
			@Test
			public void testGetAllAttributesByType_14() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_15")
			@Test
			public void testGetAllAttributesByType_15() {
				attrs = themap.get(AttributeType.REAL);
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributesByType_16")
			@Test
			public void testGetAllAttributesByType_16() {
				attrs = themap.get(AttributeType.REAL);
				assertEquals(1, attrs.size());
			}

			@DisplayName("testGetAllAttributesByType_17")
			@Test
			public void testGetAllAttributesByType_17() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_18")
			@Test
			public void testGetAllAttributesByType_18() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_19")
			@Test
			public void testGetAllAttributesByType_19() throws Exception {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals(15., attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_20")
			@Test
			public void testGetAllAttributesByType_20() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			private Map<AttributeType, Collection<Attribute>> themap;
			private Collection<Attribute> attrs;
			private Iterator<Attribute> iterator;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				themap = provider.getAllAttributesByType();
				attrs = null;
				iterator = null;
				attr = null;
			}

			@DisplayName("testGetAllAttributesByType_1")
			@Test
			public void testGetAllAttributesByType_1() {
				assertNotNull(themap);
			}

			@DisplayName("testGetAllAttributesByType_2")
			@Test
			public void testGetAllAttributesByType_2() {
				assertEquals(2, themap.size());
			}

			@DisplayName("testGetAllAttributesByType_3")
			@Test
			public void testGetAllAttributesByType_3() {
				attrs = themap.get(AttributeType.STRING);
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributesByType_4")
			@Test
			public void testGetAllAttributesByType_4() {
				attrs = themap.get(AttributeType.STRING);
				assertEquals(3, attrs.size());
			}

			@DisplayName("testGetAllAttributesByType_5")
			@Test
			public void testGetAllAttributesByType_5() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_6")
			@Test
			public void testGetAllAttributesByType_6() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_7")
			@Test
			public void testGetAllAttributesByType_7() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_8")
			@Test
			public void testGetAllAttributesByType_8() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_9")
			@Test
			public void testGetAllAttributesByType_9() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_10")
			@Test
			public void testGetAllAttributesByType_10() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_11")
			@Test
			public void testGetAllAttributesByType_11() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_12")
			@Test
			public void testGetAllAttributesByType_12() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_13")
			@Test
			public void testGetAllAttributesByType_13() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_14")
			@Test
			public void testGetAllAttributesByType_14() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_15")
			@Test
			public void testGetAllAttributesByType_15() {
				attrs = themap.get(AttributeType.REAL);
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributesByType_16")
			@Test
			public void testGetAllAttributesByType_16() {
				attrs = themap.get(AttributeType.REAL);
				assertEquals(2, attrs.size());
			}

			@DisplayName("testGetAllAttributesByType_17")
			@Test
			public void testGetAllAttributesByType_17() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_18")
			@Test
			public void testGetAllAttributesByType_18() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_19")
			@Test
			public void testGetAllAttributesByType_19() throws Exception {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals(15., attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_20")
			@Test
			public void testGetAllAttributesByType_20() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_21")
			@Test
			public void testGetAllAttributesByType_21() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_22")
			@Test
			public void testGetAllAttributesByType_22() throws Exception {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals(1., attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_23")
			@Test
			public void testGetAllAttributesByType_23() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			private Map<AttributeType, Collection<Attribute>> themap;
			private Collection<Attribute> attrs;
			private Iterator<Attribute> iterator;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				provider.removeAttribute("SOURCE"); //$NON-NLS-1$
				themap = provider.getAllAttributesByType();
				attrs = null;
				iterator = null;
				attr = null;
			}

			@DisplayName("testGetAllAttributesByType_1")
			@Test
			public void testGetAllAttributesByType_1() {
				assertNotNull(themap);
			}

			@DisplayName("testGetAllAttributesByType_2")
			@Test
			public void testGetAllAttributesByType_2() {
				assertEquals(2, themap.size());
			}

			@DisplayName("testGetAllAttributesByType_3")
			@Test
			public void testGetAllAttributesByType_3() {
				attrs = themap.get(AttributeType.STRING);
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributesByType_4")
			@Test
			public void testGetAllAttributesByType_4() {
				attrs = themap.get(AttributeType.STRING);
				assertEquals(3, attrs.size());
			}

			@DisplayName("testGetAllAttributesByType_5")
			@Test
			public void testGetAllAttributesByType_5() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_6")
			@Test
			public void testGetAllAttributesByType_6() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_7")
			@Test
			public void testGetAllAttributesByType_7() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_8")
			@Test
			public void testGetAllAttributesByType_8() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_9")
			@Test
			public void testGetAllAttributesByType_9() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_10")
			@Test
			public void testGetAllAttributesByType_10() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_11")
			@Test
			public void testGetAllAttributesByType_11() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_12")
			@Test
			public void testGetAllAttributesByType_12() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_13")
			@Test
			public void testGetAllAttributesByType_13() throws Exception {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				iterator.next();
				iterator.next();
				attr = iterator.next();
				assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_14")
			@Test
			public void testGetAllAttributesByType_14() {
				attrs = themap.get(AttributeType.STRING);
				iterator = attrs.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_15")
			@Test
			public void testGetAllAttributesByType_15() {
				attrs = themap.get(AttributeType.REAL);
				assertNotNull(attrs);
			}

			@DisplayName("testGetAllAttributesByType_16")
			@Test
			public void testGetAllAttributesByType_16() {
				attrs = themap.get(AttributeType.REAL);
				assertEquals(2, attrs.size());
			}

			@DisplayName("testGetAllAttributesByType_17")
			@Test
			public void testGetAllAttributesByType_17() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_18")
			@Test
			public void testGetAllAttributesByType_18() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_19")
			@Test
			public void testGetAllAttributesByType_19() throws Exception {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				attr = iterator.next();
				assertEquals(15., attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_20")
			@Test
			public void testGetAllAttributesByType_20() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				iterator.next();
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllAttributesByType_21")
			@Test
			public void testGetAllAttributesByType_21() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllAttributesByType_22")
			@Test
			public void testGetAllAttributesByType_22() throws Exception {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				iterator.next();
				attr = iterator.next();
				assertEquals(1., attr.getValue());
			}

			@DisplayName("testGetAllAttributesByType_23")
			@Test
			public void testGetAllAttributesByType_23() {
				attrs = themap.get(AttributeType.REAL);
				iterator = attrs.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}
	}

	@DisplayName("freeMemory")
	@Nested
	public class FreeMemory {
		@Test
		public void testFreeMemory() throws Exception {
			Collection<Attribute> attrs;
			Iterator<Attribute> iterator;
			Attribute attr;
			provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
	
			provider.freeMemory();
			
			attrs = provider.getAllAttributes();
			assertNotNull(attrs);
			assertEquals(4,attrs.size());
			iterator = attrs.iterator();
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
	    	assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
	    	assertEquals(15., attr.getValue());
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
	    	assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
	    	assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("getAttributeAsBool")
	@Nested
	public class GetAttributeAsBool {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeAsBoolString_1")
			@Test
			public void testGetAttributeAsBoolString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsBool("SOURCE"));
			}

			@DisplayName("testGetAttributeAsBoolString_2")
			@Test
			public void testGetAttributeAsBoolString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsBool("NEWFIELD"));
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", true); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsBoolString_1")
			@Test
			public void testGetAttributeAsBoolString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsBool("SOURCE"));
			}

			@DisplayName("testGetAttributeAsBoolString_2")
			@Test
			public void testGetAttributeAsBoolString_2() throws Exception {
				assertTrue(provider.getAttributeAsBool("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", true); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsBoolString_1")
			@Test
			public void testGetAttributeAsBoolString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsBool("SOURCE"));
			}

			@DisplayName("testGetAttributeAsBoolString_2")
			@Test
			public void testGetAttributeAsBoolString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsBool("NEWFIELD"));
			}
		}
	}

	@DisplayName("getAttributeAsInt")
	@Nested
	public class GetAttributeAsInt {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeAsIntString_1")
			@Test
			public void testGetAttributeAsIntString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsInt("SOURCE"));
			}

			@DisplayName("testGetAttributeAsIntString_2")
			@Test
			public void testGetAttributeAsIntString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsInt("NEWFIELD"));
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsIntString_1")
			@Test
			public void testGetAttributeAsIntString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsInt("SOURCE"));
			}

			@DisplayName("testGetAttributeAsIntString_2")
			@Test
			public void testGetAttributeAsIntString_2() throws Exception {
				assertEquals(3, provider.getAttributeAsInt("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsIntString_1")
			@Test
			public void testGetAttributeAsIntString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsInt("SOURCE"));
			}

			@DisplayName("testGetAttributeAsIntString_2")
			@Test
			public void testGetAttributeAsIntString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsInt("NEWFIELD"));
			}
		}
	}

	@DisplayName("getAttributeAsLong")
	@Nested
	public class GetAttributeAsLong {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeAsLongString_1")
			@Test
			public void testGetAttributeAsLongString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsLong("SOURCE"));
			}

			@DisplayName("testGetAttributeAsLongString_2")
			@Test
			public void testGetAttributeAsLongString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsLong("NEWFIELD"));
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3l); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsLongString_1")
			@Test
			public void testGetAttributeAsLongString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsLong("SOURCE"));
			}

			@DisplayName("testGetAttributeAsLongString_2")
			@Test
			public void testGetAttributeAsLongString_2() throws Exception {
				assertEquals(3l, provider.getAttributeAsLong("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3l); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsLongString_1")
			@Test
			public void testGetAttributeAsLongString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsLong("SOURCE"));
			}

			@DisplayName("testGetAttributeAsLongString_2")
			@Test
			public void testGetAttributeAsLongString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsLong("NEWFIELD"));
			}
		}
	}

	@DisplayName("getAttributeAsFloat")
	@Nested
	public class GetAttributeAsFloat {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeAsFloatString_1")
			@Test
			public void testGetAttributeAsFloatString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsFloat("SOURCE"));
			}

			@DisplayName("testGetAttributeAsFloatString_2")
			@Test
			public void testGetAttributeAsFloatString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsFloat("NEWFIELD"));
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3f); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsFloatString_1")
			@Test
			public void testGetAttributeAsFloatString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsFloat("SOURCE"));
			}

			@DisplayName("testGetAttributeAsFloatString_2")
			@Test
			public void testGetAttributeAsFloatString_2() throws Exception {
				assertEpsilonEquals(3f, provider.getAttributeAsFloat("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3f); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsFloatString_1")
			@Test
			public void testGetAttributeAsFloatString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsFloat("SOURCE"));
			}

			@DisplayName("testGetAttributeAsFloatString_2")
			@Test
			public void testGetAttributeAsFloatString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsFloat("NEWFIELD"));
			}
		}
	}

	@DisplayName("getAttributeAsDouble")
	@Nested
	public class GetAttributeAsDouble {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeAsDoubleString_1")
			@Test
			public void testGetAttributeAsDoubleString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsDouble("SOURCE"));
			}

			@DisplayName("testGetAttributeAsDoubleString_2")
			@Test
			public void testGetAttributeAsDoubleString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsDouble("NEWFIELD"));
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3.); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsDoubleString_1")
			@Test
			public void testGetAttributeAsDoubleString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsDouble("SOURCE"));
			}

			@DisplayName("testGetAttributeAsDoubleString_2")
			@Test
			public void testGetAttributeAsDoubleString_2() throws Exception {
				assertEpsilonEquals(3., provider.getAttributeAsDouble("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 3.); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsDoubleString_1")
			@Test
			public void testGetAttributeAsDoubleString_1() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsDouble("SOURCE"));
			}

			@DisplayName("testGetAttributeAsDoubleString_2")
			@Test
			public void testGetAttributeAsDoubleString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsDouble("NEWFIELD"));
			}
		}
	}

	@DisplayName("getAttributeAsString")
	@Nested
	public class GetAttributeAsString {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetAttributeAsStringString_1")
			@Test
			public void testGetAttributeAsStringString_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE, provider.getAttributeAsString("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsStringString_2")
			@Test
			public void testGetAttributeAsStringString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsString("NEWFIELD"));
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", "toto"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@DisplayName("testGetAttributeAsStringString_1")
			@Test
			public void testGetAttributeAsStringString_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE, provider.getAttributeAsString("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsStringString_2")
			@Test
			public void testGetAttributeAsStringString_2() throws Exception {
				assertEquals("toto", provider.getAttributeAsString("NEWFIELD")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", "toto"); //$NON-NLS-1$ //$NON-NLS-2$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsStringString_1")
			@Test
			public void testGetAttributeAsStringString_1() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE, provider.getAttributeAsString("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testGetAttributeAsStringString_2")
			@Test
			public void testGetAttributeAsStringString_2() {
				assertThrows(AttributeException.class, () -> provider.getAttributeAsString("NEWFIELD"));
			}
		}
	}

	@DisplayName("getAllBufferedAttributeNames")
	@Nested
	public class GetAllBufferedAttributeNames {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			private Collection<String> names;
			private Iterator<String> iterator;

			@BeforeEach
			public void setUp() {
				names = provider.getAllBufferedAttributeNames();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllBufferedAttributeNames_1")
			@Test
			public void testGetAllBufferedAttributeNames_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllBufferedAttributeNames_2")
			@Test
			public void testGetAllBufferedAttributeNames_2() {
				assertEquals(0, names.size());
			}

			@DisplayName("testGetAllBufferedAttributeNames_3")
			@Test
			public void testGetAllBufferedAttributeNames_3() {
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			private Collection<String> names;
			private Iterator<String> iterator;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
				names = provider.getAllBufferedAttributeNames();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllBufferedAttributeNames_1")
			@Test
			public void testGetAllBufferedAttributeNames_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllBufferedAttributeNames_2")
			@Test
			public void testGetAllBufferedAttributeNames_2() {
				assertEquals(1, names.size());
			}

			@DisplayName("testGetAllBufferedAttributeNames_3")
			@Test
			public void testGetAllBufferedAttributeNames_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllBufferedAttributeNames_4")
			@Test
			public void testGetAllBufferedAttributeNames_4() {
				assertEquals("NEWFIELD", iterator.next()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllBufferedAttributeNames_5")
			@Test
			public void testGetAllBufferedAttributeNames_5() {
				iterator.next();
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			private Collection<String> names;
			private Iterator<String> iterator;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				names = provider.getAllBufferedAttributeNames();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllBufferedAttributeNames_1")
			@Test
			public void testGetAllBufferedAttributeNames_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllBufferedAttributeNames_2")
			@Test
			public void testGetAllBufferedAttributeNames_2() {
				assertEquals(0, names.size());
			}

			@DisplayName("testGetAllBufferedAttributeNames_3")
			@Test
			public void testGetAllBufferedAttributeNames_3() {
				assertFalse(iterator.hasNext());
			}
		}
	}

	@DisplayName("getBufferedAttributeCount")
	@Nested
	public class GetBufferedAttributeCount {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testGetBufferedAttributeCount_1")
			@Test
			public void testGetBufferedAttributeCount_1() {
				assertEquals(0, provider.getBufferedAttributeCount());
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
			}

			@DisplayName("testGetBufferedAttributeCount_1")
			@Test
			public void testGetBufferedAttributeCount_1() {
				assertEquals(1, provider.getBufferedAttributeCount());
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testGetBufferedAttributeCount_1")
			@Test
			public void testGetBufferedAttributeCount_1() {
				assertEquals(0, provider.getBufferedAttributeCount());
			}
		}
	}

	@DisplayName("getAllBufferedAttributes")
	@Nested
	public class GetAllBufferedAttributes {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			private Collection<Attribute> names;
			private Iterator<Attribute> iterator;

			@BeforeEach
			public void setUp() {
				names = provider.getAllBufferedAttributes();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllBufferedAttributes_1")
			@Test
			public void testGetAllBufferedAttributes_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllBufferedAttributes_2")
			@Test
			public void testGetAllBufferedAttributes_2() {
				assertEquals(0, names.size());
			}

			@DisplayName("testGetAllBufferedAttributes_3")
			@Test
			public void testGetAllBufferedAttributes_3() {
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			private Collection<Attribute> names;
			private Iterator<Attribute> iterator;
			private Attribute attr;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
				names = provider.getAllBufferedAttributes();
				iterator = names.iterator();
				attr = null;
			}

			@DisplayName("testGetAllBufferedAttributes_1")
			@Test
			public void testGetAllBufferedAttributes_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllBufferedAttributes_2")
			@Test
			public void testGetAllBufferedAttributes_2() {
				assertEquals(1, names.size());
			}

			@DisplayName("testGetAllBufferedAttributes_3")
			@Test
			public void testGetAllBufferedAttributes_3() {
				assertTrue(iterator.hasNext());
			}

			@DisplayName("testGetAllBufferedAttributes_4")
			@Test
			public void testGetAllBufferedAttributes_4() {
				attr = iterator.next();
				assertNotNull(attr);
			}

			@DisplayName("testGetAllBufferedAttributes_5")
			@Test
			public void testGetAllBufferedAttributes_5() {
				attr = iterator.next();
				assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
			}

			@DisplayName("testGetAllBufferedAttributes_6")
			@Test
			public void testGetAllBufferedAttributes_6() throws Exception {
				attr = iterator.next();
				assertEquals(7., attr.getValue());
			}

			@DisplayName("testGetAllBufferedAttributes_7")
			@Test
			public void testGetAllBufferedAttributes_7() {
				iterator.next();
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			private Collection<Attribute> names;
			private Iterator<Attribute> iterator;

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
				names = provider.getAllBufferedAttributes();
				iterator = names.iterator();
			}

			@DisplayName("testGetAllBufferedAttributes_1")
			@Test
			public void testGetAllBufferedAttributes_1() {
				assertNotNull(names);
			}

			@DisplayName("testGetAllBufferedAttributes_2")
			@Test
			public void testGetAllBufferedAttributes_2() {
				assertEquals(0, names.size());
			}

			@DisplayName("testGetAllBufferedAttributes_3")
			@Test
			public void testGetAllBufferedAttributes_3() {
				assertFalse(iterator.hasNext());
			}
		}
	}
	
	@DisplayName("isBufferedAttribute")
	@Nested
	public class IsBufferedAttribute {

		@DisplayName("No change")
		@Nested
		public class NoChange {

			@DisplayName("testIsBufferedAttribute_1")
			@Test
			public void testIsBufferedAttribute_1() {
				assertFalse(provider.isBufferedAttribute("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After set")
		@Nested
		public class AfterSet {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
			}

			@DisplayName("testIsBufferedAttribute_1")
			@Test
			public void testIsBufferedAttribute_1() {
				assertTrue(provider.isBufferedAttribute("NEWFIELD")); //$NON-NLS-1$
			}
		}

		@DisplayName("After remove")
		@Nested
		public class AfterRemove {

			@BeforeEach
			public void setUp() {
				provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
				provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$
			}

			@DisplayName("testIsBufferedAttribute_1")
			@Test
			public void testIsBufferedAttribute_1() {
				assertFalse(provider.isBufferedAttribute("NEWFIELD")); //$NON-NLS-1$
			}
		}
	}

	@DisplayName("setAttribute")
	@Nested
	public class SetAttribute {
		
		@DisplayName("(String,AttributeValue)")
		@Nested
		public class WithStringAttributeValue {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeStringAttributeValue_1")
				@Test
				public void testSetAttributeStringAttributeValue_1() throws Exception {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() throws Exception {
					provider.setAttribute("NEWFIELD", new AttributeValueImpl(5.)); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeStringAttributeValue_1")
				@Test
				public void testSetAttributeStringAttributeValue_1() throws Exception {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeStringAttributeValue_2")
				@Test
				public void testSetAttributeStringAttributeValue_2() throws Exception {
					provider.setAttribute("NEWFIELD", (AttributeValue) null); //$NON-NLS-1$
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() throws Exception {
					provider.setAttribute("NEWFIELD", new AttributeValueImpl(5.)); //$NON-NLS-1$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeStringAttributeValue_1")
				@Test
				public void testSetAttributeStringAttributeValue_1() throws Exception {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,boolean)")
		@Nested
		public class WithStringBoolean {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeStringBoolean_1")
				@Test
				public void testSetAttributeStringBoolean_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", true); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeStringBoolean_1")
				@Test
				public void testSetAttributeStringBoolean_1() {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", true); //$NON-NLS-1$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeStringBoolean_1")
				@Test
				public void testSetAttributeStringBoolean_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,int)")
		@Nested
		public class WithStringInt {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeStringInt_1")
				@Test
				public void testSetAttributeStringInt_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeStringInt_1")
				@Test
				public void testSetAttributeStringInt_1() {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1); //$NON-NLS-1$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeStringInt_1")
				@Test
				public void testSetAttributeStringInt_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,long)")
		@Nested
		public class WithStringLong {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeStringLong_1")
				@Test
				public void testSetAttributeStringLong_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1l); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeStringLong_1")
				@Test
				public void testSetAttributeStringLong_1() {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1l); //$NON-NLS-1$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeStringLong_1")
				@Test
				public void testSetAttributeStringLong_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,float)")
		@Nested
		public class WithStringFloat {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeStringFloat_1")
				@Test
				public void testSetAttributeStringFloat_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeStringFloat_1")
				@Test
				public void testSetAttributeStringFloat_1() {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeStringFloat_1")
				@Test
				public void testSetAttributeStringFloat_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,double)")
		@Nested
		public class WithStringDouble {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeStringDouble_1")
				@Test
				public void testSetAttributeStringDouble_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1.); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeStringDouble_1")
				@Test
				public void testSetAttributeStringDouble_1() {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", 1.); //$NON-NLS-1$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeStringDouble_1")
				@Test
				public void testSetAttributeStringDouble_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(String,String)")
		@Nested
		public class WithStringString {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeStringString_1")
				@Test
				public void testSetAttributeStringString_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", "toto"); //$NON-NLS-1$ //$NON-NLS-2$
				}

				@DisplayName("testSetAttributeStringString_1")
				@Test
				public void testSetAttributeStringString_1() {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() {
					provider.setAttribute("NEWFIELD", "toto"); //$NON-NLS-1$ //$NON-NLS-2$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeStringString_1")
				@Test
				public void testSetAttributeStringString_1() {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}

		@DisplayName("(Attribute)")
		@Nested
		public class WithAttribute {

			@DisplayName("No change")
			@Nested
			public class NoChange {

				@DisplayName("testSetAttributeAttribute_1")
				@Test
				public void testSetAttributeAttribute_1() throws Exception {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After set")
			@Nested
			public class AfterSet {

				@BeforeEach
				public void setUp() throws Exception {
					provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
				}

				@DisplayName("testSetAttributeAttribute_1")
				@Test
				public void testSetAttributeAttribute_1() throws Exception {
					assertTrue(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}

			@DisplayName("After freeMemory")
			@Nested
			public class AfterFreeMemory {

				@BeforeEach
				public void setUp() throws Exception {
					provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
					provider.freeMemory();
				}

				@DisplayName("testSetAttributeAttribute_1")
				@Test
				public void testSetAttributeAttribute_1() throws Exception {
					assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
				}
			}
		}
	}

	@DisplayName("removeAllAttributes")
	@Nested
	public class RemoveAllAttributes {

		@BeforeEach
		public void setUp() throws Exception {
			provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
			provider.removeAllAttributes();
		}

		@DisplayName("testRemoveAllAttributes_1")
		@Test
		public void testRemoveAllAttributes_1() throws Exception {
			assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		}

		@DisplayName("testRemoveAllAttributes_2")
		@Test
		public void testRemoveAllAttributes_2() throws Exception {
			assertTrue(provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		}

		@DisplayName("testRemoveAllAttributes_3")
		@Test
		public void testRemoveAllAttributes_3() throws Exception {
			assertTrue(provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		}

		@DisplayName("testRemoveAllAttributes_4")
		@Test
		public void testRemoveAllAttributes_4() throws Exception {
			assertTrue(provider.hasAttribute("NATURE")); //$NON-NLS-1$
		}

		@DisplayName("testRemoveAllAttributes_5")
		@Test
		public void testRemoveAllAttributes_5() throws Exception {
			assertTrue(provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
		}
	}

	@DisplayName("renameAttribute")
	@Nested
	public class RenameAttribute {
		
		@DisplayName("After one renaming")
		@Nested
		public class AfterOneRenaming {

			@BeforeEach
			public void setUp() throws Exception {
				provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
				assertTrue(provider.renameAttribute("NEWFIELD", "NEWFIELD2")); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@DisplayName("testRenameAttributeStringString_1")
			@Test
			public void testRenameAttributeStringString_1() throws Exception {
				assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_2")
			@Test
			public void testRenameAttributeStringString_2() throws Exception {
				assertTrue(provider.hasAttribute("NEWFIELD2")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_3")
			@Test
			public void testRenameAttributeStringString_3() throws Exception {
				assertTrue(provider.hasAttribute("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_4")
			@Test
			public void testRenameAttributeStringString_4() throws Exception {
				assertTrue(provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_5")
			@Test
			public void testRenameAttributeStringString_5() throws Exception {
				assertTrue(provider.hasAttribute("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_6")
			@Test
			public void testRenameAttributeStringString_6() throws Exception {
				assertTrue(provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
			}
		}

		@DisplayName("Failure")
		@Nested
		public class Failure {

			@BeforeEach
			public void setUp() throws Exception {
				provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
				assertTrue(provider.renameAttribute("NEWFIELD", "NEWFIELD2")); //$NON-NLS-1$ //$NON-NLS-2$
				assertFalse(provider.renameAttribute("SOURCE", "SOURCE2")); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@DisplayName("testRenameAttributeStringString_1")
			@Test
			public void testRenameAttributeStringString_1() throws Exception {
				assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_2")
			@Test
			public void testRenameAttributeStringString_2() throws Exception {
				assertTrue(provider.hasAttribute("NEWFIELD2")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_3")
			@Test
			public void testRenameAttributeStringString_3() throws Exception {
				assertTrue(provider.hasAttribute("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_4")
			@Test
			public void testRenameAttributeStringString_4() throws Exception {
				assertFalse(provider.hasAttribute("SOURCE2")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_5")
			@Test
			public void testRenameAttributeStringString_5() throws Exception {
				assertTrue(provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_6")
			@Test
			public void testRenameAttributeStringString_6() throws Exception {
				assertTrue(provider.hasAttribute("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_7")
			@Test
			public void testRenameAttributeStringString_7() throws Exception {
				assertTrue(provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
			}
		}

		@DisplayName("After two renamings")
		@Nested
		public class AfterTwoRenamings {

			@BeforeEach
			public void setUp() throws Exception {
				provider.setAttribute(new AttributeImpl("NEWFIELD2", 1f)); //$NON-NLS-1$
				provider.setAttribute(new AttributeImpl("SOURCE", 1f)); //$NON-NLS-1$
				assertTrue(provider.renameAttribute("SOURCE", "SOURCE2")); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@DisplayName("testRenameAttributeStringString_1")
			@Test
			public void testRenameAttributeStringString_1() throws Exception {
				assertFalse(provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_2")
			@Test
			public void testRenameAttributeStringString_2() throws Exception {
				assertTrue(provider.hasAttribute("NEWFIELD2")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_3")
			@Test
			public void testRenameAttributeStringString_3() throws Exception {
				assertTrue(provider.hasAttribute("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_4")
			@Test
			public void testRenameAttributeStringString_4() throws Exception {
				assertEquals(PHOTOGRAMMETRIE_VALUE, provider.getAttributeAsString("SOURCE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_5")
			@Test
			public void testRenameAttributeStringString_5() throws Exception {
				assertTrue(provider.hasAttribute("SOURCE2")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_6")
			@Test
			public void testRenameAttributeStringString_6() throws Exception {
				assertEpsilonEquals(1f, provider.getAttributeAsFloat("SOURCE2")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_7")
			@Test
			public void testRenameAttributeStringString_7() throws Exception {
				assertTrue(provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_8")
			@Test
			public void testRenameAttributeStringString_8() throws Exception {
				assertTrue(provider.hasAttribute("NATURE")); //$NON-NLS-1$
			}

			@DisplayName("testRenameAttributeStringString_9")
			@Test
			public void testRenameAttributeStringString_9() throws Exception {
				assertTrue(provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
			}
		}
	}

	@DisplayName("flush")
	@Test
	public void testFlush() {
		provider.flush();
	}

}

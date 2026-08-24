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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;
import org.arakhne.afc.vmutil.locale.Locale;

@DisplayName("DBaseFileAttributeProvider")
@SuppressWarnings("all")
public class DBaseFileAttributeProviderTest extends AbstractTestCase {

	private static final String PHOTOGRAMMETRIE_VALUE;
	
	static {
		PHOTOGRAMMETRIE_VALUE = Locale.getStringWithDefaultFrom(
				"org/arakhne/afc/io/dbase/test", //$NON-NLS-1$
				"PHOTOGRAMMETRIE", //$NON-NLS-1$
				null);
		assert(PHOTOGRAMMETRIE_VALUE!=null);
	}

	private URL resource;
	private DBaseFileAttributeProvider provider;
	
	private static final String TEST_FILENAME = "org/arakhne/afc/io/dbase/test.dbf"; //$NON-NLS-1$

	@BeforeEach
	public void setUp() throws Exception {
		resource = Resources.getResource(TEST_FILENAME);
		assertNotNull(resource);
		provider = DBaseFileAttributePool.getContainer(resource, 19);
	}

	@AfterEach
	public void tearDown() throws Exception {
		provider = null;
		resource = null;
	}

	@DisplayName("getResource")
	@Nested
	public class GetResource {

		@DisplayName("#1")
		@Test
		public void testGetResource() {
			assertEquals(resource, provider.getResource());
		}
	}

	@DisplayName("getRecordNumber")
	@Nested
	public class GetRecordNumber {

		@DisplayName("#1")
		@Test
		public void testGetRecordNumber() {
			assertEquals(19, provider.getRecordNumber());
		}
	}

	@DisplayName("getAttributeCount")
	@Nested
	public class GetAttributeCount {

		@DisplayName("#1")
		@Test
		public void testGetAttributeCount() {
			assertEquals(4, provider.getAttributeCount());
		}
	}

	@DisplayName("hasAttribute")
	@Nested
	public class HasAttribute {

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

	@DisplayName("hasAttributeInDBase")
	@Nested
	public class HasAttributeInDBase {

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

	@DisplayName("getAllAttributeNames")
	@Nested
	public class GetAllAttributeNames {

		@DisplayName("#1")
		@Test
		public void testGetAllAttributeNames() {
			Collection<String> names = provider.getAllAttributeNames();
			assertNotNull(names);
			assertEquals(4, names.size());
			Iterator<String> iterator = names.iterator();
			assertTrue(iterator.hasNext());
			assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("getAttributeObjectFromDBase")
	@Nested
	public class GetAttributeObjectFromDBase {

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

	@DisplayName("getAttributeObject")
	@Nested
	public class GetAttributeObject {

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

	@DisplayName("getAttribute")
	@Nested
	public class GetAttribute {

		private AttributeValue defaultValue;

		@BeforeEach
		public void setUp() {
			defaultValue = new AttributeValueImpl(false);
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
			assertNull(provider.getAttribute("NEWFIELD")); //$NON-NLS-1$
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

	@DisplayName("getAllAttributes")
	@Nested
	public class GetAllAttributes {

		@DisplayName("#1")
		@Test
		public void testGetAllAttributes() throws Exception {
			Collection<Attribute> attrs = provider.getAllAttributes();
			assertNotNull(attrs);
			assertEquals(4,attrs.size());
			Iterator<Attribute> iterator = attrs.iterator();
			Attribute attr;
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
	    	assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
	    	assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
	    	assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
	    	assertEquals(15., attr.getValue());
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("getAllAttributesByType")
	@Nested
	public class GetAllAttributesByType {

		@DisplayName("#1")
		@Test
		public void testGetAllAttributesByType() throws Exception {
			Map<AttributeType,Collection<Attribute>> themap = provider.getAllAttributesByType();
			assertNotNull(themap);
			assertEquals(2,themap.size());
			
			Collection<Attribute> attrs;
			Attribute attr;
			Iterator<Attribute> iterator;
			
			attrs = themap.get(AttributeType.STRING);
			assertNotNull(attrs);
			assertEquals(3, attrs.size());		
			iterator = attrs.iterator();
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
	    	assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("CATEGORIE", attr.getName()); //$NON-NLS-1$
	    	assertEquals("Religieux", attr.getValue()); //$NON-NLS-1$
			assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("NATURE", attr.getName()); //$NON-NLS-1$
	    	assertEquals("Eglise", attr.getValue()); //$NON-NLS-1$
			assertFalse(iterator.hasNext());		
	    	
			attrs = themap.get(AttributeType.REAL);
			assertNotNull(attrs);
			assertEquals(1, attrs.size());		
			iterator = attrs.iterator();
	    	assertTrue(iterator.hasNext());
			attr = iterator.next();
			assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
	    	assertEquals(15., attr.getValue());
			assertFalse(iterator.hasNext());		
		}
	}

}

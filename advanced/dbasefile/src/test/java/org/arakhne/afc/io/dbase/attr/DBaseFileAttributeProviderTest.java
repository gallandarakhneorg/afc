/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
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
		this.resource = Resources.getResource(TEST_FILENAME);
		assertNotNull(this.resource);
		this.provider = DBaseFileAttributePool.getContainer(this.resource, 19);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.provider = null;
		this.resource = null;
	}

	@Test
	public void testGetResource() {
		assertEquals(this.resource, this.provider.getResource());
	}

	@Test
	public void testGetRecordNumber() {
		assertEquals(19, this.provider.getRecordNumber());
	}

	@Test
	public void testGetAttributeCount() {
		assertEquals(4, this.provider.getAttributeCount());
	}

	@Test
	public void testHasAttribute() {
		assertTrue(this.provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttribute("NOFIELD")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testHasAttributeInDBase() {
		assertTrue(this.provider.hasAttributeInDBase("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("HAUTEUR")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttributeInDBase("NOFIELD")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttributeInDBase("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAllAttributeNames() {
		Collection<String> names = this.provider.getAllAttributeNames();
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

	@Test
	public void testGetAttributeObjectFromDBase() throws Exception {
		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttributeObjectFromDBase("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttributeObjectFromDBase("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObjectFromDBase("NOFIELD")); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObjectFromDBase("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeObject() throws Exception {
		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttributeObject("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObject("NOFIELD")); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObject("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeString() throws Exception {
		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttribute("NOFIELD")); //$NON-NLS-1$
		assertNull(this.provider.getAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeStringAttributeValue() throws Exception {
		AttributeValue defaultValue = new AttributeValueImpl(false);
		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttribute("SOURCE",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttribute("HAUTEUR",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(false,this.provider.getAttribute("NOFIELD",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(false,this.provider.getAttribute("NEWFIELD",defaultValue).getValue()); //$NON-NLS-1$
	}

	@Test
	public void testGetAllAttributes() throws Exception {
		Collection<Attribute> attrs = this.provider.getAllAttributes();
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

	@Test
	public void testGetAllAttributesByType() throws Exception {
		Map<AttributeType,Collection<Attribute>> themap = this.provider.getAllAttributesByType();
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

	@Test
	public void testFreeMemory() {
		this.provider.freeMemory();
	}

}

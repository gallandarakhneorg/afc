/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
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

	@Before
	public void setUp() throws Exception {
		this.resource = Resources.getResource(TEST_FILENAME);
		assertNotNull(this.resource);
		this.provider = DBaseFileAttributePool.getCollection(this.resource, 19);
	}

	@After
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
		
		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		assertTrue(this.provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttribute("NOFIELD")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

		assertTrue(this.provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttribute("NOFIELD")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testHasAttributeInDBase() {
		assertTrue(this.provider.hasAttributeInDBase("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("HAUTEUR")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttributeInDBase("NOFIELD")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttributeInDBase("NEWFIELD")); //$NON-NLS-1$
		
		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		assertTrue(this.provider.hasAttributeInDBase("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttributeInDBase("HAUTEUR")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttributeInDBase("NOFIELD")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttributeInDBase("NEWFIELD")); //$NON-NLS-1$

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

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
		assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
		assertFalse(iterator.hasNext());

		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		names = this.provider.getAllAttributeNames();
		assertNotNull(names);
		assertEquals(5, names.size());
		iterator = names.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("NEWFIELD", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
		assertFalse(iterator.hasNext());

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

		names = this.provider.getAllAttributeNames();
		assertNotNull(names);
		assertEquals(5, names.size());
		iterator = names.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("CATEGORIE", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("HAUTEUR", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("NATURE", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("NEWFIELD", iterator.next()); //$NON-NLS-1$
		assertTrue(iterator.hasNext());
		assertEquals("SOURCE", iterator.next()); //$NON-NLS-1$
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

		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttributeObjectFromDBase("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttributeObjectFromDBase("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObjectFromDBase("NOFIELD")); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObjectFromDBase("NEWFIELD")); //$NON-NLS-1$

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

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

		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttributeObject("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObject("NOFIELD")); //$NON-NLS-1$
		assertEquals(1.,
				this.provider.getAttributeObject("NEWFIELD").getValue()); //$NON-NLS-1$

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttributeObject("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttributeObject("NOFIELD")); //$NON-NLS-1$
		assertEquals(1.,
				this.provider.getAttributeObject("NEWFIELD").getValue()); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeString() throws Exception {
		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttribute("NOFIELD")); //$NON-NLS-1$
		assertNull(this.provider.getAttribute("NEWFIELD")); //$NON-NLS-1$

		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttribute("NOFIELD")); //$NON-NLS-1$
		assertEquals(1.,
				this.provider.getAttribute("NEWFIELD").getValue()); //$NON-NLS-1$

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		assertNull(this.provider.getAttribute("NOFIELD")); //$NON-NLS-1$
		assertEquals(1.,
				this.provider.getAttribute("NEWFIELD").getValue()); //$NON-NLS-1$
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

		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttribute("SOURCE",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttribute("HAUTEUR",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(false,this.provider.getAttribute("NOFIELD",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(1.,this.provider.getAttribute("NEWFIELD",defaultValue).getValue()); //$NON-NLS-1$

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

		assertEquals(PHOTOGRAMMETRIE_VALUE,
				this.provider.getAttribute("SOURCE",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(15.,
				this.provider.getAttribute("HAUTEUR",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(false,this.provider.getAttribute("NOFIELD",defaultValue).getValue()); //$NON-NLS-1$
		assertEquals(1.,this.provider.getAttribute("NEWFIELD",defaultValue).getValue()); //$NON-NLS-1$
	}

	@Test
	public void testGetAllAttributes() throws Exception {
		Collection<Attribute> attrs;
		Iterator<Attribute> iterator;
		Attribute attr;
		
		attrs = this.provider.getAllAttributes();
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

		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		attrs = this.provider.getAllAttributes();
		assertNotNull(attrs);
		assertEquals(5,attrs.size());
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
		assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
    	assertEquals(1., attr.getValue());
		assertTrue(iterator.hasNext());
		attr = iterator.next();
		assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
		assertFalse(iterator.hasNext());

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

		attrs = this.provider.getAllAttributes();
		assertNotNull(attrs);
		assertEquals(5,attrs.size());
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
		assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
    	assertEquals(1., attr.getValue());
		assertTrue(iterator.hasNext());
		attr = iterator.next();
		assertEquals("SOURCE", attr.getName()); //$NON-NLS-1$
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attr.getValue());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetAllAttributesByType() throws Exception {
		Collection<Attribute> attrs;
		Attribute attr;
		Iterator<Attribute> iterator;
		Map<AttributeType,Collection<Attribute>> themap;
		
		themap = this.provider.getAllAttributesByType();
		assertNotNull(themap);
		assertEquals(2,themap.size());		
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

		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		themap = this.provider.getAllAttributesByType();
		assertNotNull(themap);
		assertEquals(2,themap.size());		
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
		assertEquals(2, attrs.size());		
		iterator = attrs.iterator();
    	assertTrue(iterator.hasNext());
		attr = iterator.next();
		assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
    	assertEquals(15., attr.getValue());
    	assertTrue(iterator.hasNext());
		attr = iterator.next();
		assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
    	assertEquals(1., attr.getValue());
		assertFalse(iterator.hasNext());		

		this.provider.removeAttribute("SOURCE"); //$NON-NLS-1$

		themap = this.provider.getAllAttributesByType();
		assertNotNull(themap);
		assertEquals(2,themap.size());		
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
		assertEquals(2, attrs.size());		
		iterator = attrs.iterator();
    	assertTrue(iterator.hasNext());
		attr = iterator.next();
		assertEquals("HAUTEUR", attr.getName()); //$NON-NLS-1$
    	assertEquals(15., attr.getValue());
    	assertTrue(iterator.hasNext());
		attr = iterator.next();
		assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
    	assertEquals(1., attr.getValue());
		assertFalse(iterator.hasNext());		
	}

	@Test
	public void testFreeMemory() throws Exception {
		Collection<Attribute> attrs;
		Iterator<Attribute> iterator;
		Attribute attr;
		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$

		this.provider.freeMemory();
		
		attrs = this.provider.getAllAttributes();
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

	@Test
	public void testGetAttributeAsBoolString() throws Exception {
		try {
			this.provider.getAttributeAsBool("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		try {
			this.provider.getAttributeAsBool("NEWFIELD"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		this.provider.setAttribute("NEWFIELD", true); //$NON-NLS-1$
		try {
			this.provider.getAttributeAsBool("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		assertTrue(this.provider.getAttributeAsBool("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeStringBoolean() throws Exception {
		assertTrue(this.provider.getAttribute("SOURCE", true)); //$NON-NLS-1$
		assertTrue(this.provider.getAttribute("NEWFIELD", true)); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", false); //$NON-NLS-1$
		assertTrue(this.provider.getAttribute("SOURCE", true)); //$NON-NLS-1$
		assertFalse(this.provider.getAttribute("NEWFIELD", true)); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeAsIntString() throws Exception {
		try {
			this.provider.getAttributeAsInt("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		try {
			this.provider.getAttributeAsInt("NEWFIELD"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		this.provider.setAttribute("NEWFIELD", 3); //$NON-NLS-1$
		try {
			this.provider.getAttributeAsInt("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		assertEquals(3, this.provider.getAttributeAsInt("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeStringInt() throws Exception {
		assertEquals(10, this.provider.getAttribute("SOURCE", 10)); //$NON-NLS-1$
		assertEquals(11, this.provider.getAttribute("NEWFIELD", 11)); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 7); //$NON-NLS-1$
		assertEquals(14, this.provider.getAttribute("SOURCE", 14)); //$NON-NLS-1$
		assertEquals(7, this.provider.getAttribute("NEWFIELD", 15)); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeAsLongString() throws Exception {
		try {
			this.provider.getAttributeAsLong("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		try {
			this.provider.getAttributeAsLong("NEWFIELD"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		this.provider.setAttribute("NEWFIELD", 3l); //$NON-NLS-1$
		try {
			this.provider.getAttributeAsLong("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		assertEquals(3l, this.provider.getAttributeAsLong("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeStringLong() throws Exception {
		assertEquals(10l, this.provider.getAttribute("SOURCE", 10l)); //$NON-NLS-1$
		assertEquals(11l, this.provider.getAttribute("NEWFIELD", 11l)); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 7l); //$NON-NLS-1$
		assertEquals(14l, this.provider.getAttribute("SOURCE", 14l)); //$NON-NLS-1$
		assertEquals(7l, this.provider.getAttribute("NEWFIELD", 15l)); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeAsFloatString() throws Exception {
		try {
			this.provider.getAttributeAsFloat("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		try {
			this.provider.getAttributeAsFloat("NEWFIELD"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		this.provider.setAttribute("NEWFIELD", 3f); //$NON-NLS-1$
		try {
			this.provider.getAttributeAsFloat("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		assertEpsilonEquals(3f, this.provider.getAttributeAsFloat("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeStringFloat() throws Exception {
		assertEpsilonEquals(10f, this.provider.getAttribute("SOURCE", 10f)); //$NON-NLS-1$
		assertEpsilonEquals(11f, this.provider.getAttribute("NEWFIELD", 11f)); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 7f); //$NON-NLS-1$
		assertEpsilonEquals(14f, this.provider.getAttribute("SOURCE", 14f)); //$NON-NLS-1$
		assertEpsilonEquals(7f, this.provider.getAttribute("NEWFIELD", 15f)); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeAsDoubleString() throws Exception {
		try {
			this.provider.getAttributeAsDouble("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		try {
			this.provider.getAttributeAsDouble("NEWFIELD"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		this.provider.setAttribute("NEWFIELD", 3.); //$NON-NLS-1$
		try {
			this.provider.getAttributeAsDouble("SOURCE"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		assertEpsilonEquals(3., this.provider.getAttributeAsDouble("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeStringDouble() throws Exception {
		assertEpsilonEquals(10., this.provider.getAttribute("SOURCE", 10.)); //$NON-NLS-1$
		assertEpsilonEquals(11., this.provider.getAttribute("NEWFIELD", 11.)); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$
		assertEpsilonEquals(14., this.provider.getAttribute("SOURCE", 14.)); //$NON-NLS-1$
		assertEpsilonEquals(7., this.provider.getAttribute("NEWFIELD", 15.)); //$NON-NLS-1$
	}

	@Test
	public void testGetAttributeAsStringString() throws Exception {
		assertEquals(PHOTOGRAMMETRIE_VALUE, this.provider.getAttributeAsString("SOURCE")); //$NON-NLS-1$
		try {
			this.provider.getAttributeAsString("NEWFIELD"); //$NON-NLS-1$
			fail("AttributeException was expected"); //$NON-NLS-1$
		}
		catch (AttributeException exception) {
			// expected exception
		}
		this.provider.setAttribute("NEWFIELD", "toto"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(PHOTOGRAMMETRIE_VALUE, this.provider.getAttributeAsString("SOURCE")); //$NON-NLS-1$
		assertEquals("toto", this.provider.getAttributeAsString("NEWFIELD")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testGetAttributeStringString() throws Exception {
		assertEquals(PHOTOGRAMMETRIE_VALUE, this.provider.getAttribute("SOURCE", "a")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("b", this.provider.getAttribute("NEWFIELD", "b")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.provider.setAttribute("NEWFIELD", "c"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(PHOTOGRAMMETRIE_VALUE, this.provider.getAttribute("SOURCE", "d")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("c", this.provider.getAttribute("NEWFIELD", "e")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Test
	public void testGetAllBufferedAttributeNames() {
		Collection<String> names;
		Iterator<String> iterator;
		
		names = this.provider.getAllBufferedAttributeNames();
		assertNotNull(names);
		assertEquals(0, names.size());
		iterator = names.iterator();
		assertFalse(iterator.hasNext());

		this.provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$

		names = this.provider.getAllBufferedAttributeNames();
		assertNotNull(names);
		assertEquals(1, names.size());
		iterator = names.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("NEWFIELD", iterator.next()); //$NON-NLS-1$
		assertFalse(iterator.hasNext());

		this.provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$

		names = this.provider.getAllBufferedAttributeNames();
		assertNotNull(names);
		assertEquals(0, names.size());
		iterator = names.iterator();
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetBufferedAttributeCount() {
		assertEquals(0, this.provider.getBufferedAttributeCount());

		this.provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$

		assertEquals(1, this.provider.getBufferedAttributeCount());

		this.provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$

		assertEquals(0, this.provider.getBufferedAttributeCount());
	}

	@Test
	public void testGetAllBufferedAttributes() throws Exception {
		Collection<Attribute> names;
		Iterator<Attribute> iterator;
		Attribute attr;
		
		names = this.provider.getAllBufferedAttributes();
		assertNotNull(names);
		assertEquals(0, names.size());
		iterator = names.iterator();
		assertFalse(iterator.hasNext());

		this.provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$

		names = this.provider.getAllBufferedAttributes();
		assertNotNull(names);
		assertEquals(1, names.size());
		iterator = names.iterator();
		assertTrue(iterator.hasNext());
		attr = iterator.next();
		assertNotNull(attr);
		assertEquals("NEWFIELD", attr.getName()); //$NON-NLS-1$
		assertEquals(7., attr.getValue());
		assertFalse(iterator.hasNext());

		this.provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$

		names = this.provider.getAllBufferedAttributes();
		assertNotNull(names);
		assertEquals(0, names.size());
		iterator = names.iterator();
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIsBufferedAttribute() {
		assertFalse(this.provider.isBufferedAttribute("NEWFIELD")); //$NON-NLS-1$

		this.provider.setAttribute("NEWFIELD", 7.); //$NON-NLS-1$

		assertTrue(this.provider.isBufferedAttribute("NEWFIELD")); //$NON-NLS-1$

		this.provider.removeAttribute("NEWFIELD"); //$NON-NLS-1$

		assertFalse(this.provider.isBufferedAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeStringAttributeValue() throws Exception {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", new AttributeValueImpl(5.)); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", (AttributeValue)null); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeStringBoolean() {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", true); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeStringInt() {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 1); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeStringLong() {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 1l); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeStringFloat() {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 1f); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeStringDouble() {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", 1.); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeStringString() {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute("NEWFIELD", "toto"); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testSetAttributeAttribute() throws Exception {
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		this.provider.freeMemory();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
	}

	@Test
	public void testRemoveAllAttributes() throws Exception {
		this.provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
		this.provider.removeAllAttributes();
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
	}

	@Test
	public void testRenameAttributeStringString() throws Exception {
		this.provider.setAttribute(new AttributeImpl("NEWFIELD", 1f)); //$NON-NLS-1$
		assertTrue(this.provider.renameAttribute("NEWFIELD", "NEWFIELD2")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD2")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$

		assertFalse(this.provider.renameAttribute("SOURCE", "SOURCE2")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD2")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		assertFalse(this.provider.hasAttribute("SOURCE2")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$

		this.provider.setAttribute(new AttributeImpl("SOURCE", 1f)); //$NON-NLS-1$
		assertTrue(this.provider.renameAttribute("SOURCE", "SOURCE2")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(this.provider.hasAttribute("NEWFIELD")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NEWFIELD2")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("SOURCE")); //$NON-NLS-1$
		assertEquals(PHOTOGRAMMETRIE_VALUE, this.provider.getAttributeAsString("SOURCE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("SOURCE2")); //$NON-NLS-1$
		assertEpsilonEquals(1f, this.provider.getAttributeAsFloat("SOURCE2")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("CATEGORIE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("NATURE")); //$NON-NLS-1$
		assertTrue(this.provider.hasAttribute("HAUTEUR")); //$NON-NLS-1$
	}

	@Test
	public void testFlush() {
		this.provider.flush();
	}

}

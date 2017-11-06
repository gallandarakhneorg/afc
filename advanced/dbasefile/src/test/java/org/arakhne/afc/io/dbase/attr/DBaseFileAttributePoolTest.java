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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.io.dbase.DBaseFileRecord;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.util.OutputParameter;
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
public class DBaseFileAttributePoolTest extends AbstractTestCase {

	private static final String BDTOPO_VALUE;
	private static final String BATIMENT_RELIGIEUX_VALUE;
	
	static {
		BDTOPO_VALUE = Locale.getStringWithDefaultFrom(
				"org/arakhne/afc/io/dbase/test", //$NON-NLS-1$
				"BDTOPO", //$NON-NLS-1$
				null);
		assert(BDTOPO_VALUE!=null);
		BATIMENT_RELIGIEUX_VALUE = Locale.getStringWithDefaultFrom(
				"org/arakhne/afc/io/dbase/test", //$NON-NLS-1$
				"BATIMENT_RELIGIEUX", //$NON-NLS-1$
				null);
		assert(BATIMENT_RELIGIEUX_VALUE!=null);
	}

	private URL resource;
	private DBaseFileAttributePool pool;
	
	private static final String TEST_FILENAME = "org/arakhne/afc/io/dbase/test.dbf"; //$NON-NLS-1$

	@Before
	public void setUp() throws Exception {
		this.resource = Resources.getResource(TEST_FILENAME);
		assertNotNull(this.resource);
		this.pool = DBaseFileAttributePool.getPool(this.resource);
	}

	@After
	public void tearDown() throws Exception {
		this.pool.close();
		this.pool = null;
		this.resource = null;
	}

	@Test
	public void testGetResource() {
		assertEquals(this.resource, this.pool.getResource());
	}

	@Test
	public void testGetReader() throws Exception {
		DBaseFileReader reader = this.pool.getReader();
		assertNotNull(reader);
		DBaseFileRecord record = reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(0, record.getRecordIndex());
    	assertEquals(161, record.getRecordOffset());
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
    	assertEquals(15., record.getFieldValue(3));
	}

	@Test
	public void testGetAllAttributeNames() {
		Collection<String> names = this.pool.getAllAttributeNames(2);
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
	public void testGetAttributeCount() {
		assertEquals(4, this.pool.getAttributeCount());
	}

	@Test
	public void testGetRawValue() throws Exception {
		OutputParameter<AttributeType> type = new OutputParameter<>();
		Object value;
		
		value = this.pool.getRawValue(1, "NATURE", type); //$NON-NLS-1$
		assertEquals(AttributeType.STRING, type.get());
		assertEquals(BATIMENT_RELIGIEUX_VALUE, value);

		value = this.pool.getRawValue(8, "SOURCE", type); //$NON-NLS-1$
		assertEquals(AttributeType.STRING, type.get());
		assertEquals(BDTOPO_VALUE, value);
	}

	@Test
	public void testGetContainerURLInt() {
		assertNotNull(DBaseFileAttributePool.getContainer(this.resource, 3));
	}

	@Test
	public void testGetContainerURIInt() throws Exception {
		assertNotNull(DBaseFileAttributePool.getProvider(this.resource.toURI(), 3));
	}

	@Test
	public void testGetProviderURLInt() {
		assertNotNull(DBaseFileAttributePool.getCollection(this.resource, 3));
	}

	@Test
	public void testGetProviderURIInt() throws Exception {
		assertNotNull(DBaseFileAttributePool.getCollection(this.resource.toURI(), 3));
	}

}

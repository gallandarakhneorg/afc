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
@SuppressWarnings("all")
public class DBaseFileAttributeAccessorTest extends AbstractTestCase {

	private URL resource;
	private DBaseFileAttributePool pool;
	private DBaseFileAttributeAccessor accessor;
	
	private static final String TEST_FILENAME = "org/arakhne/afc/io/dbase/test.dbf"; //$NON-NLS-1$
	
	@Before
	public void setUp() throws Exception {
		this.resource = Resources.getResource(TEST_FILENAME);
		assertNotNull(this.resource);
		this.pool = DBaseFileAttributePool.getPool(this.resource);
		assertNotNull(this.pool);
		this.accessor = this.pool.getAccessor(4);
	}

	@After
	public void tearDown() throws Exception {
		this.pool.close();
		this.accessor = null;
		this.pool = null;
		this.resource = null;
	}

	@Test
	public void testGetResource() {
		assertEquals(this.resource, this.accessor.getResource());
	}

	@Test
	public void testGetRecordNumber() {
		assertEquals(4, this.accessor.getRecordNumber());
	}

	@Test
	public void testGetAttributeCount() {
		assertEquals(4, this.accessor.getAttributeCount());
	}

	@Test
	public void testGetAllAttributeNames() {
		Collection<String> names = this.accessor.getAllAttributeNames();
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
	public void testLoadValue() throws Exception {
		AttributeValue value = this.accessor.loadValue("CATEGORIE"); //$NON-NLS-1$
		assertNotNull(value);
		assertEquals("Religieux", value.getValue()); //$NON-NLS-1$
	}

}

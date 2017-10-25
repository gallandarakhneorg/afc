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

package org.arakhne.afc.inputoutput.filefilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.Resources;

@SuppressWarnings("all")
public abstract class AbstractBaseFileFilterTest {

	protected URL resource;
	protected URL nothing;
	
	protected abstract String getExtension();
	
	protected abstract AbstractFileFilter getFilter();

	@Before
	public void setUp() throws Exception {
		this.resource = Resources.getResource(getClass(), "test" + getExtension()); //$NON-NLS-1$
		assertNotNull("Resource not found: " + "test" + getExtension(), this.resource); //$NON-NLS-1$ //$NON-NLS-2$
		this.nothing = Resources.getResource(getClass(), "nothing.bin"); //$NON-NLS-1$
		assertNotNull("Resource not found: nothing.bin", this.nothing); //$NON-NLS-1$
	}

	@After
	public void tearDown() throws Exception {
		this.resource = null;
		this.nothing = null;
	}
	
	@Test
	public void acceptFile() throws Exception {
		AbstractFileFilter filter = getFilter();
		
		File filename = null;
		
		try {
			filename = File.createTempFile(getClass().getName(), getExtension());
			FileSystem.copy(this.resource, filename);
		
			assertTrue(filter.accept(filename));
			assertTrue(filter.accept(filename.getParentFile()));
		} finally {
			FileSystem.delete(filename);
		}

		try {
			filename = File.createTempFile(getClass().getName(), ".xxx"); //$NON-NLS-1$
			filename.deleteOnExit();
			FileSystem.copy(this.resource, filename);

			assertFalse(filter.accept(filename));
			assertTrue(filter.accept(filename.getParentFile()));
		} finally {
			FileSystem.delete(filename);
		}

	}

}

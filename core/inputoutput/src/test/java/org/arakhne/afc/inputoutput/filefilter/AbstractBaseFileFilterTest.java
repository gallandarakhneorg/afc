/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.Resources;

@SuppressWarnings("all")
public abstract class AbstractBaseFileFilterTest {

	protected URL resource;
	protected URL nothing;
	
	protected abstract String getExtension();
	
	protected abstract AbstractFileFilter getFilter();

	@BeforeEach
	public void setUp() throws Exception {
		URL tmp1 = Thread.currentThread().getContextClassLoader().getResource("org/arakhne/afc/inputoutput/filefilter/test" + getExtension());
		URL tmp2 = Thread.currentThread().getContextClassLoader().getResource("/org/arakhne/afc/inputoutput/filefilter/test" + getExtension());
		URL tmp3 = getClass().getResource("org/arakhne/afc/inputoutput/filefilter/test" + getExtension());
		URL tmp4 = getClass().getResource("/org/arakhne/afc/inputoutput/filefilter/test" + getExtension());
		URL tmp5 = getClass().getClassLoader().getResource("org/arakhne/afc/inputoutput/filefilter/test" + getExtension());
		URL tmp6 = getClass().getClassLoader().getResource("/org/arakhne/afc/inputoutput/filefilter/test" + getExtension());
		URL tmp7 = getClass().getResource("/org/arakhne/afc/inputoutput/filefilter/test.obj");
		this.resource = Resources.getResource(getClass(), "test" + getExtension()); //$NON-NLS-1$
		assertNotNull(this.resource, "Resource not found: " + "test" + getExtension()); //$NON-NLS-1$ //$NON-NLS-2$
		this.nothing = Resources.getResource(getClass(), "nothing.bin"); //$NON-NLS-1$
		assertNotNull(this.nothing, "Resource not found: nothing.bin"); //$NON-NLS-1$
	}

	@AfterEach
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

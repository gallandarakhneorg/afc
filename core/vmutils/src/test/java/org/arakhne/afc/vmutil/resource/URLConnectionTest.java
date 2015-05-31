/* 
 * $Id$
 * 
 * Copyright (C) 2010 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.vmutil.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.arakhne.afc.vmutil.URLHandlerUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
public class URLConnectionTest {

	private static final String RESOURCE_URL = "resource:org/arakhne/afc/vmutil/test.txt"; //$NON-NLS-1$

	private URLConnection connection;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		URL resourceUrl = new URL(RESOURCE_URL);
		assertNotNull(resourceUrl);
		this.connection = new URLConnection(resourceUrl);
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.connection = null;
		URLHandlerUtil.uninstallArakhneHandlers();
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void testGetInputStream() throws IOException {
		String line;
		try (InputStream is = this.connection.getInputStream()) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				line = br.readLine();
			}
		}
		assertEquals("TEST1: FOR UNIT TEST ONLY", line); //$NON-NLS-1$
	}

}

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
package org.arakhne.vmutil.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.arakhne.vmutil.Resources;
import org.arakhne.vmutil.URLHandlerUtil;

import junit.framework.TestCase;

/**
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
public class URLConnectionTest extends TestCase {

	private URLConnection connection;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		URLHandlerUtil.installArakhneHandlers();
		URL resourceUrl = Resources.getResource("org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(resourceUrl);
		this.connection = new URLConnection(resourceUrl);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		this.connection = null;
		URLHandlerUtil.uninstallArakhneHandlers();
		super.tearDown();
	}

	/**
	 */
	public void testGetHeaderFieldKeyInt() {
		assertEquals("content-type", this.connection.getHeaderFieldKey(0)); //$NON-NLS-1$
		assertEquals("content-length", this.connection.getHeaderFieldKey(1)); //$NON-NLS-1$
		assertEquals("last-modified", this.connection.getHeaderFieldKey(2)); //$NON-NLS-1$
		assertNull(this.connection.getHeaderFieldKey(3));
	}

	/**
	 */
	public void testGetHeaderFieldInt() {
		assertEquals("application/octet-stream", this.connection.getHeaderField(0)); //$NON-NLS-1$
		assertEquals("25", this.connection.getHeaderField(1)); //$NON-NLS-1$
		assertNotNull(this.connection.getHeaderField(2));
		assertNull(this.connection.getHeaderField(3));
	}

	/**
	 */
	public void testGetHeaderFieldString() {
		assertEquals("application/octet-stream", this.connection.getHeaderField("content-type")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("25", this.connection.getHeaderField("content-length")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(this.connection.getHeaderField("last-modified")); //$NON-NLS-1$
		assertNull(this.connection.getHeaderField("expires")); //$NON-NLS-1$
	}

	/**
	 */
	public void testGetHeaderFields() {
		Map<?,?> map = this.connection.getHeaderFields();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals(Collections.singletonList("application/octet-stream"), map.get("content-type")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Collections.singletonList("25"), map.get("content-length")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(map.get("last-modified")); //$NON-NLS-1$
		assertNull(map.get("expires")); //$NON-NLS-1$
	}

	/**
	 * @throws IOException
	 */
	public void testGetInputStream() throws IOException {
		String line;
		InputStream is = this.connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			line = br.readLine();
		}
		finally {
			br.close();
			is.close();
		}
		assertEquals("TEST1: FOR UNIT TEST ONLY", line); //$NON-NLS-1$
	}

	/**
	 * @throws IOException
	 */
	public static void testGetOutputStream() throws IOException {
		File tmpFile = File.createTempFile("unittest", ".txt"); //$NON-NLS-1$ //$NON-NLS-2$
		tmpFile.deleteOnExit();

		URLConnection con = new URLConnection(tmpFile.toURI().toURL());
		con.setDoOutput(true);

		OutputStream os = con.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		try {
			bw.write("HELLO WORLD!"); //$NON-NLS-1$
		}
		finally {
			bw.close();
			osw.close();
			os.close();
		}

		assertEquals(12, tmpFile.length());

		String line;
		BufferedReader br = new BufferedReader(new FileReader(tmpFile));
		try {
			line = br.readLine();
		}
		finally {
			br.close();
		}
		assertEquals("HELLO WORLD!", line); //$NON-NLS-1$
	}

}

/* $Id$
 * 
 * Copyright (C) 2007-09 Stephane GALLAND.
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
package org.arakhne.afc.vmutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
@SuppressWarnings("static-method")
public class ResourcesTest {

	private static final String TEST_NAME_1 = "/org/arakhne/afc/vmutil/test.txt"; //$NON-NLS-1$
	private static final String TEST_NAME_2 = "org/arakhne/afc/vmutil/test.txt"; //$NON-NLS-1$
	private static final String TEST_NAME_3 = "test.txt"; //$NON-NLS-1$
	private static final String TEST_NAME_4 = "/test.txt"; //$NON-NLS-1$
	private static final String PACKAGE_NAME = "org.arakhne.afc.vmutil"; //$NON-NLS-1$
	
	/**
	 */
	@Test
	public void testGetResourceString() {
		assertNull(Resources.getResource(null));

		URL u1 = Resources.getResource(TEST_NAME_1);
		assertNotNull(u1);

		URL u2 = Resources.getResource(TEST_NAME_2);
		assertNotNull(u2);

		assertEquals(u1,u2);
	}

	/**
	 */
	@Test
	public void testGetResourceClassString() {
		assertNull(Resources.getResource(ResourcesTest.class, null));

		URL u1 = Resources.getResource(ResourcesTest.class, TEST_NAME_1);
		assertNotNull(u1);

		URL u2 = Resources.getResource(ResourcesTest.class, TEST_NAME_2);
		assertNotNull(u2);

		URL u3 = Resources.getResource(ResourcesTest.class, TEST_NAME_3);
		assertNotNull(u3);

		assertEquals(u1,u2);
		assertEquals(u1,u3);

		assertNull(Resources.getResource((Class<?>)null, null));

		u1 = Resources.getResource((Class<?>)null, TEST_NAME_1);
		assertNull(u1);

		u2 = Resources.getResource((Class<?>)null, TEST_NAME_2);
		assertNull(u2);
	}

	/**
	 */
	@Test
	public void testGetResourcePackageString() {
		ClassLoader l = ResourcesTest.class.getClassLoader();
		Package p = Package.getPackage(PACKAGE_NAME);
		assertNull(Resources.getResource(ResourcesTest.class, null));

		URL u1 = Resources.getResource(l, p, TEST_NAME_1);
		assertNull(u1);

		URL u2 = Resources.getResource(l, p, TEST_NAME_2);
		assertNull(u2);

		URL u3 = Resources.getResource(l, p, TEST_NAME_3);
		assertNotNull(u3);

		URL u4 = Resources.getResource(l, p, TEST_NAME_4);
		assertNotNull(u4);

		assertEquals(u3, u4);

		assertNull(Resources.getResource(l, (Package)null, null));

		u1 = Resources.getResource(l, (Package)null, TEST_NAME_3);
		assertNull(u1);

		u2 = Resources.getResource(l, (Package)null, TEST_NAME_4);
		assertNull(u2);
	}

	/**
	 */
	@Test
	public void testGetResourceClassLoaderString() {
		assertNull(Resources.getResource(ResourcesTest.class.getClassLoader(), null));    	

		URL u1 = Resources.getResource(ResourcesTest.class.getClassLoader(), TEST_NAME_1);
		assertNotNull(u1);

		URL u2 = Resources.getResource(ResourcesTest.class.getClassLoader(), TEST_NAME_2);
		assertNotNull(u2);

		assertEquals(u1,u2);

		assertNull(Resources.getResource((ClassLoader)null, null));    	

		u1 = Resources.getResource((ClassLoader)null, TEST_NAME_1);
		assertNotNull(u1);

		u2 = Resources.getResource((ClassLoader)null, TEST_NAME_2);
		assertNotNull(u2);

		assertEquals(u1,u2);
	}

	/**
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceAsStreamString() throws IOException {
		assertNull(Resources.getResourceAsStream(null));

		try (InputStream is = Resources.getResourceAsStream(TEST_NAME_1)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(TEST_NAME_2)) {
			assertNotNull(is);
		}
	}

	/**
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceAsStreamClassString() throws IOException {
		assertNull(Resources.getResourceAsStream(ResourcesTest.class, null));

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_1)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_2)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_3)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class, TEST_NAME_4)) {
			assertNotNull(is);
		}

		assertNull(Resources.getResourceAsStream((Class<?>)null, null));

		try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_1)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_2)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream((Class<?>)null, TEST_NAME_3)) {
			assertNull(is);
		}
	}

	/**
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceAsStreamPackageString() throws IOException {
		ClassLoader l = ResourcesTest.class.getClassLoader();
		Package p = Package.getPackage(PACKAGE_NAME);
		assertNull(Resources.getResourceAsStream(ResourcesTest.class, null));

		try (InputStream is = Resources.getResourceAsStream(l, p, TEST_NAME_1)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(l, p, TEST_NAME_2)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(l, p, TEST_NAME_3)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(l, p, TEST_NAME_4)) {
			assertNotNull(is);
		}

		assertNull(Resources.getResourceAsStream(l, (Package)null, null));

		try (InputStream is = Resources.getResourceAsStream(l, (Package)null, TEST_NAME_1)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(l, (Package)null, TEST_NAME_2)) {
			assertNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(l, (Package)null, TEST_NAME_3)) {
			assertNull(is);
		}
	}

	/**
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceAsStreamClassLoaderString() throws IOException {
		assertNull(Resources.getResourceAsStream(ResourcesTest.class.getClassLoader(), null));

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class.getClassLoader(), TEST_NAME_1)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream(ResourcesTest.class.getClassLoader(), TEST_NAME_2)) {
			assertNotNull(is);
		}

		assertNull(Resources.getResourceAsStream((ClassLoader)null, null));

		try (InputStream is = Resources.getResourceAsStream((ClassLoader)null, TEST_NAME_1)) {
			assertNotNull(is);
		}

		try (InputStream is = Resources.getResourceAsStream((ClassLoader)null, TEST_NAME_2)) {
			assertNotNull(is);
		}
	}

}

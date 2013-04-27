/* $Id$
 * 
 * Copyright (C) 2007-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.vmutil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

/**
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
public class ResourcesTest extends TestCase {

	/**
	 */
	public static void testGetResourceString() {
		assertNull(Resources.getResource(null));

		URL u1 = Resources.getResource("/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u1);

		URL u2 = Resources.getResource("org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u2);

		assertEquals(u1,u2);
	}

	/**
	 */
	public static void testGetResourceClassString() {
		assertNull(Resources.getResource(ResourcesTest.class, null));

		URL u1 = Resources.getResource(ResourcesTest.class, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u1);

		URL u2 = Resources.getResource(ResourcesTest.class, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u2);

		URL u3 = Resources.getResource(ResourcesTest.class, "test.txt"); //$NON-NLS-1$
		assertNotNull(u3);

		assertEquals(u1,u2);
		assertEquals(u1,u3);

		assertNull(Resources.getResource((Class<?>)null, null));

		u1 = Resources.getResource((Class<?>)null, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNull(u1);

		u2 = Resources.getResource((Class<?>)null, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNull(u2);
	}

	/**
	 */
	public static void testGetResourcePackageString() {
		ClassLoader l = ResourcesTest.class.getClassLoader();
		Package p = Package.getPackage("org.arakhne.vmutil"); //$NON-NLS-1$
		assertNull(Resources.getResource(ResourcesTest.class, null));

		URL u1 = Resources.getResource(l, p, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNull(u1);

		URL u2 = Resources.getResource(l, p, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNull(u2);

		URL u3 = Resources.getResource(l, p, "test.txt"); //$NON-NLS-1$
		assertNotNull(u3);

		URL u4 = Resources.getResource(l, p, "/test.txt"); //$NON-NLS-1$
		assertNotNull(u4);

		assertEquals(u3, u4);

		assertNull(Resources.getResource(l, (Package)null, null));

		u1 = Resources.getResource(l, (Package)null, "test.txt"); //$NON-NLS-1$
		assertNull(u1);

		u2 = Resources.getResource(l, (Package)null, "/test.txt"); //$NON-NLS-1$
		assertNull(u2);
	}

	/**
	 */
	public static void testGetResourceClassLoaderString() {
		assertNull(Resources.getResource(ResourcesTest.class.getClassLoader(), null));    	

		URL u1 = Resources.getResource(ResourcesTest.class.getClassLoader(), "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u1);

		URL u2 = Resources.getResource(ResourcesTest.class.getClassLoader(), "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u2);

		assertEquals(u1,u2);

		assertNull(Resources.getResource((ClassLoader)null, null));    	

		u1 = Resources.getResource((ClassLoader)null, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u1);

		u2 = Resources.getResource((ClassLoader)null, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(u2);

		assertEquals(u1,u2);
	}

	/**
	 * @throws IOException 
	 */
	public static void testGetResourceAsStreamString() throws IOException {
		assertNull(Resources.getResourceAsStream(null));

		InputStream is = Resources.getResourceAsStream("/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$;
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream("org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}
	}

	/**
	 * @throws IOException 
	 */
	public static void testGetResourceAsStreamClassString() throws IOException {
		assertNull(Resources.getResourceAsStream(ResourcesTest.class, null));

		InputStream is = Resources.getResourceAsStream(ResourcesTest.class, "/org/arakhne/vmutil/test.txt");//$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(ResourcesTest.class, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(ResourcesTest.class, "/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(ResourcesTest.class, "test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		assertNull(Resources.getResourceAsStream((Class<?>)null, null));

		is = Resources.getResourceAsStream((Class<?>)null, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream((Class<?>)null, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream((Class<?>)null, "test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}
	}

	/**
	 * @throws IOException 
	 */
	public static void testGetResourceAsStreamPackageString() throws IOException {
		ClassLoader l = ResourcesTest.class.getClassLoader();
		Package p = Package.getPackage("org.arakhne.vmutil"); //$NON-NLS-1$
		assertNull(Resources.getResourceAsStream(ResourcesTest.class, null));

		InputStream is = Resources.getResourceAsStream(l, p, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(l, p, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(l, p, "/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(l, p, "test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		assertNull(Resources.getResourceAsStream(l, (Package)null, null));

		is = Resources.getResourceAsStream(l, (Package)null, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(l, (Package)null, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(l, (Package)null, "test.txt"); //$NON-NLS-1$
		try {
			assertNull(is);
		}
		finally {
			if (is!=null) is.close();
		}
	}

	/**
	 * @throws IOException 
	 */
	public static void testGetResourceAsStreamClassLoaderString() throws IOException {
		assertNull(Resources.getResourceAsStream(ResourcesTest.class.getClassLoader(), null));

		InputStream is = Resources.getResourceAsStream(ResourcesTest.class.getClassLoader(), "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream(ResourcesTest.class.getClassLoader(), "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		assertNull(Resources.getResourceAsStream((ClassLoader)null, null));

		is = Resources.getResourceAsStream((ClassLoader)null, "/org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}

		is = Resources.getResourceAsStream((ClassLoader)null, "org/arakhne/vmutil/test.txt"); //$NON-NLS-1$
		try {
			assertNotNull(is);
		}
		finally {
			if (is!=null) is.close();
		}
	}

}

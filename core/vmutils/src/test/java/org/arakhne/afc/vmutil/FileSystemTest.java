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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
@SuppressWarnings("static-method")
public class FileSystemTest {

	private final static File f1 = new File("/home/test.x.z.z"); //$NON-NLS-1$
	private final static File f2 = new File("/home"); //$NON-NLS-1$
	private final static File pf1 = f1.getParentFile();
	private final static File pf2 = f2.getParentFile();
	private final static URL u1, u2, u3, u4, u5, u6, u7, u8, u9, u10, u11, u13, u14, u15;
	private final static URL pu1, pu2, pu3, pu7, pu13;
	private final static String TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"; //$NON-NLS-1$
	private final static String JOIN_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z.z/home/test.x.z.z?toto#frag"; //$NON-NLS-1$
	private final static String PARENT_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/"; //$NON-NLS-1$
	private final static String WOEXT_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z?toto#frag"; //$NON-NLS-1$
	private final static String REPEXT_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z.toto?toto#frag"; //$NON-NLS-1$
	private final static String TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"; //$NON-NLS-1$
	private final static String PARENT_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/"; //$NON-NLS-1$
	private final static String JOIN_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; //$NON-NLS-1$
	private final static String WOEXT_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z"; //$NON-NLS-1$
	private final static String REPEXT_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.toto"; //$NON-NLS-1$
	private final static String JARPART_TEST_URL2 = "file:/home/test/j.jar"; //$NON-NLS-1$
	private final static File f3 = new File("/home/test/j.jar"); //$NON-NLS-1$
	private final static File f4 = new File("/org/arakhne/afc/vmutil/file.x.z.z"); //$NON-NLS-1$
	private final static String TEST_URL3 = "jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"; //$NON-NLS-1$
	private final static String PARENT_TEST_URL3 = "jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/"; //$NON-NLS-1$
	private final static String JARPART_TEST_URL3 = "jar:http://www.arakhne.org/j.jar!/inner/myjar.jar"; //$NON-NLS-1$
	private final static String JOIN_TEST_URL3 = "jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; //$NON-NLS-1$
	private final static String JARJAR_TEST_URL1 = "jar:file:/home/test/j.jar!/inner/myjar.jar"; //$NON-NLS-1$
	private final static String JARJAR_TEST_URL2 = "/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; //$NON-NLS-1$
	private final static String JARJAR_TEST_URL3 = "jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; //$NON-NLS-1$

	private final static String STRING_WITH_SPACE = "/the path/to/file with space.toto"; //$NON-NLS-1$
	private final static URL URL_WITH_SPACE;
	
	static {
		try {
			u1 = f1.toURI().toURL();
			u2 = f2.toURI().toURL();
			u3 = new URL(TEST_URL1);
			u4 = new URL(JOIN_TEST_URL1);
			u5 = new URL(WOEXT_TEST_URL1);
			u6 = new URL(REPEXT_TEST_URL1);
			u7 = new URL(TEST_URL2);
			u8 = new URL(JOIN_TEST_URL2);
			u9 = new URL(WOEXT_TEST_URL2);
			u10 = new URL(REPEXT_TEST_URL2);
			u11 = new URL(JARPART_TEST_URL2);
			u13 = new URL(TEST_URL3);
			u14 = new URL(JARPART_TEST_URL3);
			u15 = new URL(JOIN_TEST_URL3);
			pu1 = pf1.toURI().toURL();
			pu2 = pf2.toURI().toURL();
			pu3 = new URL(PARENT_TEST_URL1);
			pu7 = new URL(PARENT_TEST_URL2);
			pu13 = new URL(PARENT_TEST_URL3);
			URL_WITH_SPACE = new File(STRING_WITH_SPACE).toURI().toURL();
		}
		catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private boolean oldLibraryLoaderState;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Disable native library loading during unit tests
		this.oldLibraryLoaderState = LibraryLoader.isEnable();
		LibraryLoader.setEnable(false);
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Restore library loading state
		LibraryLoader.setEnable(this.oldLibraryLoaderState);
	}
	
	/**
	 */
	@Test
	public void testIsWindowNativeFilename() {
		assertFalse(FileSystem.isWindowsNativeFilename("D:/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("/")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$

		assertTrue(FileSystem.isWindowsNativeFilename("file:C:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:C:a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://\\a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$

		assertTrue(FileSystem.isWindowsNativeFilename("C:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("C:a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("a\\b\\c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$

		assertFalse(FileSystem.isWindowsNativeFilename("file:C:/a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://C:/a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:C:a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://C:a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:/a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:///a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://host/a/b/c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:////host/a/b/c.txt")); //$NON-NLS-1$

		assertTrue(FileSystem.isWindowsNativeFilename("C:c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:C:c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:c.txt")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:c.txt")); //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://c.txt")); //$NON-NLS-1$
	}

	/**
	 */
	@Test
	public void testNormalizeWindowNativeFilename() {
		assertEquals(new File("C:/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file:C:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("C:/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file://C:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("C:a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file:C:a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("C:a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file://\\a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file:a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file://a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("//host/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("//host/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$

		assertEquals(new File("C:/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("C:\\a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("C:a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("C:a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("C:a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("\\a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("a\\b\\c.txt")); //$NON-NLS-1$
		assertEquals(new File("//host/a/b/c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$

		assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:/a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:/a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:/a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:///a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://host/a/b/c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:////host/a/b/c.txt")); //$NON-NLS-1$

		assertEquals(new File("C:c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("C:c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("c.txt")); //$NON-NLS-1$
		assertEquals(new File("C:c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file:C:c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:c.txt")); //$NON-NLS-1$
		assertEquals(new File("C:c.txt"), //$NON-NLS-1$
				FileSystem.normalizeWindowsNativeFilename("file://C:c.txt")); //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://c.txt")); //$NON-NLS-1$
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testIsJarURLURL() throws Exception {
		assertFalse(FileSystem.isJarURL(u1));
		assertFalse(FileSystem.isJarURL(u2));
		assertFalse(FileSystem.isJarURL(u3));
		assertTrue(FileSystem.isJarURL(u7));
		assertTrue(FileSystem.isJarURL(u13));

		assertFalse(FileSystem.isJarURL(new URL("file:"+STRING_WITH_SPACE)));  //$NON-NLS-1$
		assertFalse(FileSystem.isJarURL(URL_WITH_SPACE));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testGetJarURLURL() throws Exception {
		assertNull(FileSystem.getJarURL(u1));
		assertNull(FileSystem.getJarURL(u2));
		assertNull(FileSystem.getJarURL(u3));
		assertEquals(u11, FileSystem.getJarURL(u7));
		assertEquals(u14, FileSystem.getJarURL(u13));

		assertEquals(new URL("file:"+STRING_WITH_SPACE),  //$NON-NLS-1$
				FileSystem.getJarURL(new URL("jar:file:"+STRING_WITH_SPACE+"!/titi")));  //$NON-NLS-1$//$NON-NLS-2$
		assertNull(FileSystem.getJarFile(URL_WITH_SPACE));

		assertEquals(new URL(JARJAR_TEST_URL1), FileSystem.getJarURL(
				new URL(JARJAR_TEST_URL3)));
	}

	/**
	 * @throws Exception
	 */
	public void testGetJarFileURL() throws Exception {
		assertNull(FileSystem.getJarFile(u1));
		assertNull(FileSystem.getJarFile(u2));
		assertNull(FileSystem.getJarFile(u3));
		assertEquals(f4, FileSystem.getJarFile(u7));
		assertEquals(f4, FileSystem.getJarFile(u13));

		assertEquals(new File("/titi"),  //$NON-NLS-1$
				FileSystem.getJarFile(new URL("jar:file:"+STRING_WITH_SPACE+"!/titi")));  //$NON-NLS-1$//$NON-NLS-2$
		assertNull(FileSystem.getJarFile(URL_WITH_SPACE));

		assertEquals(new File(JARJAR_TEST_URL2), FileSystem.getJarFile(
				new URL(JARJAR_TEST_URL3)));
	}

	/**
	 * @throws MalformedURLException
	 */
	@Test
	public void testToJarURLFileFile() throws MalformedURLException {
		assertEquals(u7, FileSystem.toJarURL(f3, f4));
	}

	/**
	 * @throws MalformedURLException
	 */
	@Test
	public void testToJarURLFileString() throws MalformedURLException {
		assertEquals(u7, FileSystem.toJarURL(f3, f4.getPath()));
	}

	/**
	 * @throws MalformedURLException
	 */
	@Test
	public void testToJarURLURLFile() throws MalformedURLException {
		assertEquals(u7, FileSystem.toJarURL(u11, f4));
		assertEquals(new URL(JARJAR_TEST_URL3), FileSystem.toJarURL(new URL(JARJAR_TEST_URL1), new File(JARJAR_TEST_URL2)));
	}

	/**
	 * @throws MalformedURLException
	 */
	@Test
	public void testToJarURLURLString() throws MalformedURLException {
		assertEquals(u7, FileSystem.toJarURL(u11, f4.getPath()));
		assertEquals(new URL(JARJAR_TEST_URL3), FileSystem.toJarURL(new URL(JARJAR_TEST_URL1), JARJAR_TEST_URL2));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testDirnameFile() throws Exception {
		assertEquals(new URL("file", "", "/home"),  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
				FileSystem.dirname(f1));
		assertEquals(new URL("file", "", "/"),  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
				FileSystem.dirname(f2));
		assertNull(FileSystem.dirname(new File("/"))); //$NON-NLS-1$

		assertEquals(new URL("file:/the path/to"),  //$NON-NLS-1$
				FileSystem.dirname(new File(STRING_WITH_SPACE)));
	}

	/**
	 * @throws MalformedURLException 
	 */
	@Test
	public void testDirnameURL() throws MalformedURLException {
		assertEquals(new URL("file", "", "."), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.dirname(new URL("file", "", "marbre.jpg"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("http", "www.arakhne.org", "."), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.dirname(new URL("http", "www.arakhne.org", "marbre.jpg"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(pu1, FileSystem.dirname(u1));
		assertEquals(pu2, FileSystem.dirname(u2));
		assertEquals(pu3, FileSystem.dirname(u3));
		assertEquals(pu7, FileSystem.dirname(u7));
		assertEquals(pu13, FileSystem.dirname(u13));
		assertEquals(new URL("file", "", "/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.dirname(new URL("file:///a/"))); //$NON-NLS-1$
		assertNull(FileSystem.dirname(new URL("file://"))); //$NON-NLS-1$
		
		assertEquals(new URL("file", "", "/a/b%20c/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.dirname(new File("/a/b c/d.txt").toURI().toURL())); //$NON-NLS-1$

		assertEquals(new URL("file:/the%20path/to/"),  //$NON-NLS-1$
				FileSystem.dirname(new URL("file:"+STRING_WITH_SPACE))); //$NON-NLS-1$
		assertEquals(new URL("file:/the%20path/to/"),  //$NON-NLS-1$
				FileSystem.dirname(URL_WITH_SPACE));
	}

	/**
	 */
	@Test
	public void testLargeBasenameString() {
		assertEquals("test.x.z.z", FileSystem.largeBasename(f1.getAbsolutePath())); //$NON-NLS-1$
		assertEquals("home", FileSystem.largeBasename(f2.getAbsolutePath())); //$NON-NLS-1$
		assertEquals("a.b.c", FileSystem.largeBasename("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file:", FileSystem.largeBasename("file://")); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			assertEquals("terrain_physx.dae", FileSystem.largeBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError _) {
			//
		}
		try {
			assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError _) {
			//
		}

		assertEquals("file with space.toto",  //$NON-NLS-1$
				FileSystem.largeBasename(STRING_WITH_SPACE));
	}

	/**
	 */
	@Test
	public void testLargeBasenameFile() {
		assertEquals("test.x.z.z", FileSystem.largeBasename(f1)); //$NON-NLS-1$
		assertEquals("home", FileSystem.largeBasename(f2)); //$NON-NLS-1$
		assertEquals("a.b.c", FileSystem.largeBasename(new File("/a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.largeBasename(new File("/"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("file with space.toto",  //$NON-NLS-1$
				FileSystem.largeBasename(new File(STRING_WITH_SPACE)));
	}

	/**
	 * @throws MalformedURLException 
	 */
	@Test
	public void testLargeBasenameURL() throws MalformedURLException {
		assertEquals("test.x.z.z", FileSystem.largeBasename(u1)); //$NON-NLS-1$
		assertEquals("home", FileSystem.largeBasename(u2)); //$NON-NLS-1$
		assertEquals("file.x.z.z", FileSystem.largeBasename(u3)); //$NON-NLS-1$
		assertEquals("file.x.z.z", FileSystem.largeBasename(u7)); //$NON-NLS-1$
		assertEquals("a.b.c", FileSystem.largeBasename(new URL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.largeBasename(new URL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$

		URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			assertEquals("terrain_physx.dae", FileSystem.largeBasename(url)); //$NON-NLS-1$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError _) {
			//
		}

		assertEquals("file with space.toto",  //$NON-NLS-1$
				FileSystem.largeBasename(new URL("file:"+STRING_WITH_SPACE))); //$NON-NLS-1$
		assertEquals("file with space.toto",  //$NON-NLS-1$
				FileSystem.largeBasename(new File(STRING_WITH_SPACE)));
	}

	/**
	 */
	@Test
	public void testBasenameString() {
		assertEquals("test.x.z", FileSystem.basename(f1.getAbsolutePath())); //$NON-NLS-1$
		assertEquals("home", FileSystem.basename(f2.getAbsolutePath())); //$NON-NLS-1$
		assertEquals("a.b", FileSystem.basename("/a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.basename("")); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			assertEquals("terrain_physx", FileSystem.basename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError _) {
			//
		}
		try {
			assertEquals("terrain_physx", FileSystem.basename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError _) {
			//
		}

		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.basename(STRING_WITH_SPACE));
	}

	/**
	 */
	@Test
	public void testBasenameFile() {
		assertEquals("test.x.z", FileSystem.basename(f1)); //$NON-NLS-1$
		assertEquals("home", FileSystem.basename(f2)); //$NON-NLS-1$
		assertEquals("a.b", FileSystem.basename(new File("/a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.basename(new File("/"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.basename(new File(STRING_WITH_SPACE)));
	}

	/**
	 * @throws MalformedURLException 
	 */
	@Test
	public void testBasenameURL() throws MalformedURLException {
		assertEquals("test.x.z", FileSystem.basename(u1)); //$NON-NLS-1$
		assertEquals("home", FileSystem.basename(u2)); //$NON-NLS-1$
		assertEquals("file.x.z", FileSystem.basename(u3)); //$NON-NLS-1$
		assertEquals("file.x.z", FileSystem.basename(u7)); //$NON-NLS-1$
		assertEquals("a.b", FileSystem.basename(new URL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.basename(new URL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$

		URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			assertEquals("terrain_physx", FileSystem.basename(url)); //$NON-NLS-1$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError _) {
			//
		}

		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.basename(new URL("file:"+STRING_WITH_SPACE))); //$NON-NLS-1$
		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.basename(new File(STRING_WITH_SPACE)));
	}

	/**
	 */
	@Test
	public void testShortBasenameString() {
		assertEquals("test", FileSystem.shortBasename(f1.getAbsolutePath())); //$NON-NLS-1$
		assertEquals("home", FileSystem.shortBasename(f2.getAbsolutePath())); //$NON-NLS-1$
		assertEquals("a", FileSystem.shortBasename("/a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.shortBasename("")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.shortBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.shortBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.shortBasename(STRING_WITH_SPACE));
	}

	/**
	 */
	@Test
	public void testShortBasenameFile() {
		assertEquals("test", FileSystem.shortBasename(f1)); //$NON-NLS-1$
		assertEquals("home", FileSystem.shortBasename(f2)); //$NON-NLS-1$
		assertEquals("a", FileSystem.shortBasename(new File("/a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.shortBasename(new File("/"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.shortBasename(new File(STRING_WITH_SPACE)));
	}

	/**
	 * @throws MalformedURLException 
	 */
	@Test
	public void testShortBasenameURL() throws MalformedURLException {
		assertEquals("test", FileSystem.shortBasename(u1)); //$NON-NLS-1$
		assertEquals("home", FileSystem.shortBasename(u2)); //$NON-NLS-1$
		assertEquals("file", FileSystem.shortBasename(u3)); //$NON-NLS-1$
		assertEquals("file", FileSystem.shortBasename(u7)); //$NON-NLS-1$
		assertEquals("a", FileSystem.shortBasename(new URL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.shortBasename(new URL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			assertEquals("terrain_physx", FileSystem.shortBasename(url)); //$NON-NLS-1$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError _) {
			//
		}

		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.shortBasename(new URL("file:"+STRING_WITH_SPACE))); //$NON-NLS-1$
		assertEquals("file with space",  //$NON-NLS-1$
				FileSystem.shortBasename(URL_WITH_SPACE));
	}

	/**
	 */
	@Test
	public void testExtensionFile() {
		assertEquals(".z", FileSystem.extension(f1)); //$NON-NLS-1$
		assertEquals("", FileSystem.extension(f2)); //$NON-NLS-1$
		assertEquals(".c", FileSystem.extension(new File("/a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.extension(new File("/"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(".toto",  //$NON-NLS-1$
				FileSystem.extension(new File(STRING_WITH_SPACE)));
	}

	/**
	 * @throws MalformedURLException 
	 */
	@Test
	public void testExtensionURL() throws MalformedURLException {
		assertEquals(".z", FileSystem.extension(u1)); //$NON-NLS-1$
		assertEquals("", FileSystem.extension(u2)); //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(u3)); //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(u7)); //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(u13)); //$NON-NLS-1$
		assertEquals(".c", FileSystem.extension(new URL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.extension(new URL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(".toto",  //$NON-NLS-1$
				FileSystem.extension(new URL("file:"+STRING_WITH_SPACE))); //$NON-NLS-1$
		assertEquals(".toto",  //$NON-NLS-1$
				FileSystem.extension(URL_WITH_SPACE));
	}

	/**
	 */
	@Test
	public void testExtensionsFile() {
		assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(f1))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Arrays.equals(new String[0], FileSystem.extensions(f2)));
		assertTrue(Arrays.equals(new String[]{"b","c"}, FileSystem.extensions(new File("/a.b.c/")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Arrays.equals(new String[0], FileSystem.extensions(new File("/")))); //$NON-NLS-1$

		assertTrue(Arrays.equals(new String[] {"toto"},  //$NON-NLS-1$
				FileSystem.extensions(new File(STRING_WITH_SPACE))));
	}

	/**
	 * @throws MalformedURLException 
	 */
	@Test
	public void testExtensionsURL() throws MalformedURLException {
		assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(u1))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Arrays.equals(new String[0], FileSystem.extensions(u2)));
		assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(u3))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(u7))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Arrays.equals(new String[]{"b","c"}, FileSystem.extensions(new URL("file:///a.b.c/")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Arrays.equals(new String[0], FileSystem.extensions(new URL("file://")))); //$NON-NLS-1$

		assertTrue(Arrays.equals(new String[] {"toto"},  //$NON-NLS-1$
				FileSystem.extensions(new File(STRING_WITH_SPACE))));
	}

	/**
	 */
	@Test
	public void testSplitFile() {
		assertTrue(Arrays.equals(
				new String[] {
					"", //$NON-NLS-1$
					"home", //$NON-NLS-1$
					"test.x.z.z", //$NON-NLS-1$
				},
				FileSystem.split(f1)));
		assertTrue(Arrays.equals(
				new String[] {
					"", //$NON-NLS-1$
					"home", //$NON-NLS-1$
				},
				FileSystem.split(f2)));

		assertTrue(Arrays.equals(
				new String[] {
							"", //$NON-NLS-1$
							"the path", //$NON-NLS-1$
							"to", //$NON-NLS-1$
							"file with space.toto" //$NON-NLS-1$
				},
				FileSystem.split(new File(STRING_WITH_SPACE))));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testSplitURL() throws Exception {
		String[] tab;
		
		tab = FileSystem.split(u1);
		assertTrue(Arrays.equals(
				new String[] {
					"", //$NON-NLS-1$
					"home", //$NON-NLS-1$
					"test.x.z.z", //$NON-NLS-1$
				},
				tab));
		
		tab = FileSystem.split(u2);
		assertTrue(Arrays.equals(
				new String[] {
					"", //$NON-NLS-1$
					"home", //$NON-NLS-1$
				},
				tab));
		
		tab = FileSystem.split(u7);
		assertTrue(Arrays.equals(
				new String[] {
					"", //$NON-NLS-1$
					"org", //$NON-NLS-1$
					"arakhne", //$NON-NLS-1$
					"afc", //$NON-NLS-1$
					"vmutil", //$NON-NLS-1$
					"file.x.z.z", //$NON-NLS-1$
				},
				tab));
		
		tab = FileSystem.split(u13);
		assertTrue(Arrays.equals(
				new String[] {
					"", //$NON-NLS-1$
					"org", //$NON-NLS-1$
					"arakhne", //$NON-NLS-1$
					"afc", //$NON-NLS-1$
					"vmutil", //$NON-NLS-1$
					"file.x.z.z", //$NON-NLS-1$
				},
				tab));

		assertTrue(Arrays.equals(
				new String[] {
							"", //$NON-NLS-1$
							"the path", //$NON-NLS-1$
							"to", //$NON-NLS-1$
							"file with space.toto" //$NON-NLS-1$
				},
				FileSystem.split(new URL("file:"+STRING_WITH_SPACE)))); //$NON-NLS-1$
		assertTrue(Arrays.equals(
				new String[] {
							"", //$NON-NLS-1$
							"the path", //$NON-NLS-1$
							"to", //$NON-NLS-1$
							"file with space.toto" //$NON-NLS-1$
				},
				FileSystem.split(URL_WITH_SPACE)));
	}

	/**
	 */
	@Test
	public void testJoinFileStringArray() {
		assertEquals(new File(new File(f1, "home"), "test.x.z.z"), //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.join(f1,
				"", //$NON-NLS-1$
				"home", //$NON-NLS-1$
				"test.x.z.z")); //$NON-NLS-1$
	}

	/**
	 */
	@Test
	public void testJoinFileFileArray() {
		assertEquals(new File(new File(f1, "home"), "test.x.z.z"), //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.join(f1,
				new File("home"), //$NON-NLS-1$
				new File("test.x.z.z"))); //$NON-NLS-1$
		assertEquals(new File(new File(f1, "home"), "test.x.z.z"), //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.join(f1,
				new File(File.separator+"home"), //$NON-NLS-1$
				new File("test.x.z.z"))); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testJoinURLStringArray() throws Exception {
		assertEquals(new File(new File(f1, "home"), "test.x.z.z").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.join(u1,
				"", //$NON-NLS-1$
				"home", //$NON-NLS-1$
				"test.x.z.z")); //$NON-NLS-1$

		assertEquals(u4,
				FileSystem.join(u3,
				"", //$NON-NLS-1$
				"home", //$NON-NLS-1$
				"test.x.z.z")); //$NON-NLS-1$

		assertEquals(u8,
				FileSystem.join(u7,
				"", //$NON-NLS-1$
				"home", //$NON-NLS-1$
				"test.x.z.z")); //$NON-NLS-1$

		assertEquals(u15,
				FileSystem.join(u13,
				"", //$NON-NLS-1$
				"home", //$NON-NLS-1$
				"test.x.z.z")); //$NON-NLS-1$

		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  //$NON-NLS-1$
				FileSystem.join(new URL("file:"+STRING_WITH_SPACE), "a", "b"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  //$NON-NLS-1$
				FileSystem.join(URL_WITH_SPACE, "a", "b")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testJoinURLFileArray() throws Exception {
		assertEquals(new File(new File(f1, "home"), "test.x.z.z").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.join(u1,
				new File("home"), //$NON-NLS-1$
				new File("test.x.z.z"))); //$NON-NLS-1$
		assertEquals(new File(new File(f1, "home"), "test.x.z.z").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.join(u1,
				new File(File.separator+"home"), //$NON-NLS-1$
				new File("test.x.z.z"))); //$NON-NLS-1$
		assertEquals(u4,
				FileSystem.join(u3,
				new File(File.separator+"home"), //$NON-NLS-1$
				new File("test.x.z.z"))); //$NON-NLS-1$
		assertEquals(u8,
				FileSystem.join(u7,
				new File(File.separator+"home"), //$NON-NLS-1$
				new File("test.x.z.z"))); //$NON-NLS-1$
		assertEquals(u15,
				FileSystem.join(u13,
				new File(File.separator+"home"), //$NON-NLS-1$
				new File("test.x.z.z"))); //$NON-NLS-1$
		
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  //$NON-NLS-1$
				FileSystem.join(new URL("file:"+STRING_WITH_SPACE), new File("a"), new File("b"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  //$NON-NLS-1$
				FileSystem.join(URL_WITH_SPACE, new File("a"), new File("b"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 */
	@Test
	public void testHasExtensionFileString() {
		assertTrue(FileSystem.hasExtension(f1, ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(f1, "z")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(f1, ".x")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(f1, "")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(f2, "")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(f2, ".z")); //$NON-NLS-1$

		assertTrue(FileSystem.hasExtension(new File(STRING_WITH_SPACE), ".toto"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(new File(STRING_WITH_SPACE), "toto"));  //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(new File(STRING_WITH_SPACE), ".zip"));  //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(new File(STRING_WITH_SPACE), "zip"));  //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testHasExtensionURLString() throws Exception {
		assertTrue(FileSystem.hasExtension(u1, ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(u1, "z")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(u1, ".x")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(u1, "")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(u2, "")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(u2, ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(u3, ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(u3, "z")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(u3, ".x")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(u3, "")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(u7, ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(u7, "z")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(u7, ".x")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(u7, "")); //$NON-NLS-1$

		assertTrue(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), ".toto"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), "toto"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), ".zip"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), "zip"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension(URL_WITH_SPACE, ".toto")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(URL_WITH_SPACE, "toto")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(URL_WITH_SPACE, ".zip")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(URL_WITH_SPACE, "zip")); //$NON-NLS-1$
	}

	/**
	 */
	@Test
	public void testRemoveExtensionFile() {
		assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(f1)); //$NON-NLS-1$
		assertEquals(new File("/home"), FileSystem.removeExtension(f2)); //$NON-NLS-1$

		assertEquals(new File("/the path/to/file with space"),  //$NON-NLS-1$
				FileSystem.removeExtension(new File(STRING_WITH_SPACE)));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testRemoveExtensionURL() throws Exception {
		assertEquals(new File("/home/test.x.z").toURI().toURL(), //$NON-NLS-1$
				FileSystem.removeExtension(u1));
		assertEquals(new File("/home").toURI().toURL(), //$NON-NLS-1$ 
				FileSystem.removeExtension(u2));
		assertEquals(u5, FileSystem.removeExtension(u3));
		assertEquals(u9, FileSystem.removeExtension(u7));

		assertEquals(new URL("file:/the%20path/to/file%20with%20space"),  //$NON-NLS-1$
				FileSystem.removeExtension(new URL("file:"+STRING_WITH_SPACE))); //$NON-NLS-1$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space"),  //$NON-NLS-1$
				FileSystem.removeExtension(URL_WITH_SPACE));
	}

	/**
	 */
	public void testReplaceExtensionFileString() {
		assertEquals(new File("/home/test.x.z.toto"), FileSystem.replaceExtension(f1, ".toto")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new File("/home.toto"), FileSystem.replaceExtension(f2, ".toto")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(new File("/the path/to/file with space.zip"),  //$NON-NLS-1$
				FileSystem.replaceExtension(new File(STRING_WITH_SPACE), ".zip")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testReplaceExtensionURLString() throws Exception {
		assertEquals(new File("/home/test.x.z.toto").toURI().toURL(), //$NON-NLS-1$ 
				FileSystem.replaceExtension(u1, ".toto")); //$NON-NLS-1$
		assertEquals(new File("/home.toto").toURI().toURL(), //$NON-NLS-1$ 
				FileSystem.replaceExtension(u2, ".toto")); //$NON-NLS-1$
		assertEquals(u6, FileSystem.replaceExtension(u3, ".toto")); //$NON-NLS-1$
		assertEquals(u10, FileSystem.replaceExtension(u7, ".toto")); //$NON-NLS-1$

		assertEquals(new URL("file:/the%20path/to/file%20with%20space.zip"),  //$NON-NLS-1$
				FileSystem.replaceExtension(new URL("file:"+STRING_WITH_SPACE), ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.zip"),  //$NON-NLS-1$
				FileSystem.replaceExtension(URL_WITH_SPACE, ".zip")); //$NON-NLS-1$
	}

	/**
	 */
	@Test
	public void testAddExtensionFileString() {
		assertEquals(new File("/home/test.x.z.z"), FileSystem.addExtension(f1, ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new File("/home/test.x.z.z.toto"), FileSystem.addExtension(f1, ".toto")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new File("/home.toto"), FileSystem.addExtension(f2, ".toto")); //$NON-NLS-1$ //$NON-NLS-2$
		
		assertEquals(new File(STRING_WITH_SPACE+".zip"), //$NON-NLS-1$
				FileSystem.addExtension(new File(STRING_WITH_SPACE), "zip")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testAddExtensionURLString() throws Exception {
		assertEquals(new File("/home/test.x.z.z").toURI().toURL(), //$NON-NLS-1$ 
				FileSystem.addExtension(u1, ".z")); //$NON-NLS-1$
		assertEquals(new File("/home/test.x.z.z.toto").toURI().toURL(), //$NON-NLS-1$ 
				FileSystem.addExtension(u1, ".toto")); //$NON-NLS-1$
		assertEquals(new File("/home.toto").toURI().toURL(), //$NON-NLS-1$ 
				FileSystem.addExtension(u2, ".toto")); //$NON-NLS-1$
		
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.zip"), //$NON-NLS-1$
				FileSystem.addExtension(new URL("file:"+STRING_WITH_SPACE), ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testConvertStringToUrl() throws Exception {
		URL rr;

		// The following test permits to check if a specifical behaviour of URL
		// is still present in the JRE.
		rr = new URL("file://marbre.jpg"); //$NON-NLS-1$
		assertEquals("file", rr.getProtocol()); //$NON-NLS-1$
		assertEquals("marbre.jpg", rr.getAuthority()); //$NON-NLS-1$
		assertEquals("", rr.getPath()); //$NON-NLS-1$
		//-----
		
		assertNull(FileSystem.convertStringToURL(null, true));
		assertNull(FileSystem.convertStringToURL("", true)); //$NON-NLS-1$
		assertNull(FileSystem.convertStringToURL(null, false));
		assertNull(FileSystem.convertStringToURL("", false)); //$NON-NLS-1$

		rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
		assertNotNull(rr);
		assertEquals("file", rr.getProtocol()); //$NON-NLS-1$
		assertEquals("", rr.getAuthority()); //$NON-NLS-1$s
		assertEquals("", rr.getHost()); //$NON-NLS-1$
		assertNull(rr.getQuery());
		assertEquals("marbre.jpg", rr.getPath()); //$NON-NLS-1$

		assertEquals(new URL("http", "www.arakhne.org", "/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					 FileSystem.convertStringToURL("http://www.arakhne.org/", true)); //$NON-NLS-1$
		assertEquals(new URL("http", "www.arakhne.org", "/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 FileSystem.convertStringToURL("http://www.arakhne.org/", false)); //$NON-NLS-1$

		assertEquals(new URL("file", "", f1.getAbsolutePath()), //$NON-NLS-1$ //$NON-NLS-2$
				 FileSystem.convertStringToURL("file:"+f1.getAbsolutePath(), true)); //$NON-NLS-1$
		assertEquals(new URL("file", "", f1.getAbsolutePath()), //$NON-NLS-1$ //$NON-NLS-2$
			 FileSystem.convertStringToURL("file:"+f1.getAbsolutePath(), false)); //$NON-NLS-1$
		assertEquals(new URL("file", "", "./toto"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 FileSystem.convertStringToURL("file:./toto", false)); //$NON-NLS-1$

		// CAUTION: testing right-formed jar URL.
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); //$NON-NLS-1$
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); //$NON-NLS-1$

		// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
		assertEquals(new URL("file", "", "/home/test/j.jar"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar", true)); //$NON-NLS-1$
		assertEquals(new URL("file", "", "/home/test/j.jar"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar", false)); //$NON-NLS-1$

		// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); //$NON-NLS-1$
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); //$NON-NLS-1$
		
		URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
		assertNotNull(testResource);
		URL testResourceFileRel = new URL("file","", "org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		URL testResourceFileAbs = new File("/org/arakhne/afc/vmutil/test.txt").toURI().toURL(); //$NON-NLS-1$
		
		assertEquals(testResource,
				 FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
		assertEquals(null,
				 FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$

		assertEquals(testResource,
				 FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
		assertEquals(null,
				 FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$

		assertEquals(testResource,
				 FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
		assertEquals(testResourceFileAbs,
				 FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$

		assertEquals(testResource,
				 FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
		assertEquals(testResourceFileRel,
				 FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$

		assertEquals(new URL("file:"+STRING_WITH_SPACE),  //$NON-NLS-1$
				FileSystem.convertStringToURL(STRING_WITH_SPACE, false));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testConvertURLToFile() throws Exception {
		assertEquals(f1,
				 FileSystem.convertURLToFile(new URL("file:"+f1.getAbsolutePath()))); //$NON-NLS-1$

		try {
			FileSystem.convertURLToFile(new URL("http://www.arakhne.org")); //$NON-NLS-1$
			fail("not a file URL"); //$NON-NLS-1$
		}
		catch(IllegalArgumentException _) {
			//
		}
		
		assertEquals(new File("toto").getCanonicalPath(), //$NON-NLS-1$
				 FileSystem.convertURLToFile(new URL("file:./toto")).getCanonicalPath()); //$NON-NLS-1$

		assertEquals(new File("toto").getCanonicalPath(), //$NON-NLS-1$
				 FileSystem.convertURLToFile(new URL("file:toto")).getCanonicalPath()); //$NON-NLS-1$

		assertEquals(new File("toto").getCanonicalPath(), //$NON-NLS-1$
				 FileSystem.convertURLToFile(new URL("file:./abs/../toto")).getCanonicalPath()); //$NON-NLS-1$

		assertEquals(new File("/toto").getCanonicalPath(), //$NON-NLS-1$
				 FileSystem.convertURLToFile(new URL("file:/toto")).getCanonicalPath()); //$NON-NLS-1$

		assertEquals(new File(STRING_WITH_SPACE),
				FileSystem.convertURLToFile(new URL("file:"+STRING_WITH_SPACE))); //$NON-NLS-1$
		assertEquals(new File(STRING_WITH_SPACE),
				FileSystem.convertURLToFile(URL_WITH_SPACE));
	}
	
	/**
	 */
	@Test
	public void testMakeAbsoluteFileFile() {
		File root = new File(File.separator+"myroot"); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((File)null, (File)null));
		assertNull(FileSystem.makeAbsolute((File)null, root));

		assertEquals(new File(File.separator+"toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File(File.separator+"toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new File("toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("toto"), (File)null)); //$NON-NLS-1$

		assertEquals(new File(File.separator+"toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File(File.separator+"toto"), root)); //$NON-NLS-1$
		assertEquals(new File(File.separator+"myroot"+File.separator+"toto"), //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.makeAbsolute(new File("toto"), root)); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testMakeAbsoluteURLFile() throws Exception {
		File root = new File(File.separator+"myroot"); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((URL)null, (File)null));
		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("file:toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("file:/myroot/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("http://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:/myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testMakeAbsoluteURLURL_withfileroot() throws Exception {
		URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));
		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("file:/myroot/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("http://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:/myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testMakeAbsoluteURLURL_withhttproot() throws Exception {
		URL root = new URL("http://maven.arakhne.org"); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));
		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("http://maven.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("http://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
		assertEquals(new URL("jar:http://maven.arakhne.org/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testMakeAbsoluteFileURL_withfileroot() throws Exception {
		URL root = new URL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((File)null, (URL)null));
		assertNull(FileSystem.makeAbsolute((File)null, root));

		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("http://maven.arakhne.org/myroot/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("toto"), root)); //$NON-NLS-1$

		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("a/b/c"), new URL("file:"+STRING_WITH_SPACE)));  //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("a/b/c"), URL_WITH_SPACE));  //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testMakeAbsoluteFileURL_withhttproot() throws Exception {
		URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((File)null, (URL)null));
		assertNull(FileSystem.makeAbsolute((File)null, root));

		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("/toto"), root)); //$NON-NLS-1$
		assertEquals(new URL("file:/myroot/toto"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("toto"), root)); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testGetParentURLURL() throws Exception {
		assertEquals(
				new URL("http://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org"))); //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/"))); //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto"))); //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/"))); //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$

		assertEquals(
				new URL("https://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org"))); //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/"))); //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto"))); //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/"))); //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$

		assertEquals(
				new URL("ftp://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org"))); //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/"))); //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto"))); //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/"))); //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$

		assertEquals(
				new URL("file:/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto/titi/"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto/"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:./toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto/titi"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:./toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto/titi/"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:./"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:./"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto/"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:../"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:."))); //$NON-NLS-1$
		assertEquals(
				new URL("file:../"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:../"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:toto"))); //$NON-NLS-1$
		assertEquals(
				new URL("file:../"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:toto/"))); //$NON-NLS-1$

		assertEquals(
				new URL("jar:file:test.jar!/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/toto/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi/"))); //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto"))); //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/"))); //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/"), //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/"))); //$NON-NLS-1$

		assertEquals(new URL("file:/the path/to/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:"+STRING_WITH_SPACE)));  //$NON-NLS-1$
		assertEquals(new URL("file:/the%20path/to/"),  //$NON-NLS-1$
				FileSystem.getParentURL(URL_WITH_SPACE));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testConvertFileToURLFile() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		try {
			File f1 = new File("/toto"); //$NON-NLS-1$
			URL u1 = f1.toURI().toURL();
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			File f2 = FileSystem.convertURLToFile(u2);
			
			URL actual;
			
			actual = FileSystem.convertFileToURL(f1);
			assertEqualUrls(u1, actual);

			actual = FileSystem.convertFileToURL(f2);
			assertEqualUrls(u2e, actual);
		}
		finally {
			URLHandlerUtil.uninstallArakhneHandlers();
		}

		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto"),  //$NON-NLS-1$
				FileSystem.convertFileToURL(new File(STRING_WITH_SPACE)));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testToShortestURLURL() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		try {
			File f1 = new File("/toto"); //$NON-NLS-1$
			URL u1 = f1.toURI().toURL();
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			
			URL actual;
			
			actual = FileSystem.toShortestURL(u1);
			assertEqualUrls(u1, actual);

			actual = FileSystem.toShortestURL(u2);
			assertEqualUrls(u2e, actual);
		}
		finally {
			URLHandlerUtil.uninstallArakhneHandlers();
		}
	}

	private void assertEqualUrls(URL expected, URL actual) {
		String u1 = expected==null ? null : expected.toExternalForm().replaceAll("/$", ""); //$NON-NLS-1$//$NON-NLS-2$
		String u2 = actual==null ? null : actual.toExternalForm().replaceAll("/$", ""); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(u1, u2);
	}
	
	/**
	 * @throws Exception 
	 */
	@Test
	public void testMakeRelativeFileFile() throws Exception {
		File root, abs, rel;
		
		root = FileSystem.getUserHomeDirectory();
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b"); //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b"); //$NON-NLS-1$//$NON-NLS-2$
		rel = new File("a","b"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		
		
		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc"); //$NON-NLS-1$ //$NON-NLS-2$
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

	
	
		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc"); //$NON-NLS-1$//$NON-NLS-2$
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	/**
	 * @throws Exception 
	 */
	@Test
	public void testMakeRelativeFileURL() throws Exception {
		File abs, rel;
		URL root;
		
		root = FileSystem.getUserHomeDirectory().toURI().toURL();
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b"); //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b"); //$NON-NLS-1$//$NON-NLS-2$
		rel = new File("a","b"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		
		
		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

	
	
		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); //$NON-NLS-1$//$NON-NLS-2$
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	/**
	 * @throws Exception 
	 */
	@Test
	public void testMakeRelativeURLURL() throws Exception {
		File rel;
		URL root, abs;
		
		root = FileSystem.getUserHomeDirectory().toURI().toURL();
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b").toURI().toURL(); //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		
		
		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

	
	
		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); //$NON-NLS-1$//$NON-NLS-2$
		
		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc").toURI().toURL(); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}
	
	/**
	 * @throws MalformedURLException
	 */
	@Test
	public void testMakeCanonicalURL() throws MalformedURLException {
		assertEquals(
				new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL(TEST_URL1)));

		assertEquals(
				new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL(TEST_URL2)));

		assertEquals(
				new URL("jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL(TEST_URL3)));
		
		assertEquals(
				new URL("file:/a/b/c/d/e"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/./d/e"))); //$NON-NLS-1$
		
		assertEquals(
				new URL("file:/a/d/e"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:/a/b/../c/../d/e"))); //$NON-NLS-1$

		assertEquals(
				new URL("file:/a/b/d/e"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/../d/e"))); //$NON-NLS-1$

		assertEquals(
				new URL("file:../a/b/c/d/e"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:../a/b/./c/./d/e"))); //$NON-NLS-1$

		assertEquals(
				new URL("file:../a/c/d/e"), //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:../a/b/../c/./d/e"))); //$NON-NLS-1$
	}
	
	private String readInputStream(InputStream is) throws IOException {
		StringBuilder b = new StringBuilder();
		byte[] buffer = new byte[2048];
		int len;
		while ((len=is.read(buffer))>0) {
			b.append(new String(buffer, 0, len));
		}
		is.close();
		return b.toString();
	}
	
	private void createZip(File testArchive) throws IOException {
		File testDir = FileSystem.createTempDirectory("unittest", null); //$NON-NLS-1$
		FileSystem.deleteOnExit(testDir);
		FileSystem.copy(FileSystemTest.class.getResource("test.txt"), testDir); //$NON-NLS-1$
		FileSystem.copy(FileSystemTest.class.getResource("test2.txt"), testDir); //$NON-NLS-1$
		File subdir = new File(testDir, "subdir"); //$NON-NLS-1$
		subdir.mkdirs();
		FileSystem.copy(FileSystemTest.class.getResource("test.txt"), subdir); //$NON-NLS-1$
		FileSystem.zipFile(testDir, testArchive);
	}
	
	/**
	 * @throws IOException
	 */
	@Test
	public void testZipFileFile() throws IOException {
		File testArchive = File.createTempFile("unittest", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
		testArchive.deleteOnExit();
		
		createZip(testArchive);
		
		try (ZipFile zipFile = new ZipFile(testArchive)) {

			ZipEntry zipEntry = zipFile.getEntry("test.txt"); //$NON-NLS-1$
			assertNotNull(zipEntry);
			assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); //$NON-NLS-1$
	
			zipEntry = zipFile.getEntry("test2.txt"); //$NON-NLS-1$
			assertNotNull(zipEntry);
			assertEquals("TEST2: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); //$NON-NLS-1$
			
			zipEntry = zipFile.getEntry("subdir/test.txt"); //$NON-NLS-1$
			assertNotNull(zipEntry);
			assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); //$NON-NLS-1$
		}
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void testUnzipFileFile() throws IOException {
		File testArchive = File.createTempFile("unittest", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
		testArchive.deleteOnExit();
		createZip(testArchive);

		File testDir = FileSystem.createTempDirectory("unittest", null); //$NON-NLS-1$
		FileSystem.deleteOnExit(testDir);
		File subDir = new File(testDir, "subdir"); //$NON-NLS-1$
		
		FileSystem.unzipFile(testArchive, testDir);
		
		assertTrue(testDir.isDirectory());
		assertTrue(subDir.isDirectory());
		
		String txt;
		
		File file = new File(testDir, "test.txt"); //$NON-NLS-1$
		try (FileInputStream fis = new FileInputStream(file)) {
			txt = readInputStream(fis);
		}
		assertEquals("TEST1: FOR UNIT TEST ONLY", txt); //$NON-NLS-1$
		
		file = new File(testDir, "test2.txt"); //$NON-NLS-1$
		try (FileInputStream fis = new FileInputStream(file)) {
			txt = readInputStream(fis);
		}
		assertEquals("TEST2: FOR UNIT TEST ONLY", txt); //$NON-NLS-1$

		file = new File(subDir, "test.txt"); //$NON-NLS-1$
		try (FileInputStream fis = new FileInputStream(file)) {
			txt = readInputStream(fis);
		}
		assertEquals("TEST1: FOR UNIT TEST ONLY", txt); //$NON-NLS-1$
	}

}

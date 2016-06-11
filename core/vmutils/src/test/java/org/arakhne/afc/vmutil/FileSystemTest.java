/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.vmutil;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.arakhne.afc.vmutil.FileSystemTest.UnixFilenameStandardFileSystemTest;

//@RunWith(Suite.class)
//@SuiteClasses({UnixFilenameStandardFileSystemTest.class})
@SuppressWarnings("all")
public class FileSystemTest {

	public static void assertEquals(Object a, Object b) {
		if (!Objects.equals(a, b)) {
			throw new ComparisonFailure("not equal", Objects.toString(a), Objects.toString(b));
		}
	}

	/** Replace "/" by the file separator.
	 *
	 * @param filename
	 * @return
	 */
	public static File normFile(String filename) {
		return new File(norm(filename));
	}
	
	/** Replace "/" by the file separator.
	 *
	 * @param filename
	 * @return
	 */
	public static String norm(String filename) {
		return filename.replaceAll(Pattern.quote("/"), Matcher.quoteReplacement(File.separator));
	}

	public static void assertNormedFile(String expected, File actual) {
		assertEquals(normFile(expected), actual);
	}
	
	private static abstract class AbstractFileSystemTest {
		
//		private final File f1; 
//		private final File f2; 
//		private final File pf1;
//		private final File pf2;
//		private final URL u1, u2, u3, u4, u5, u6, u7, u8, u9, u10, u11, u13, u14, u15;
//		private final URL pu1, pu2, pu3, pu7, pu13;
//		private final String TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"; 
//		private final String JOIN_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z.z/home/test.x.z.z?toto#frag"; 
//		private final String PARENT_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/"; 
//		private final String WOEXT_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z?toto#frag"; 
//		private final String REPEXT_TEST_URL1 = "http://toto:titi@www.arakhne.org/path/to/file.x.z.toto?toto#frag"; 
//		private final String TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"; 
//		private final String PARENT_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/"; 
//		private final String JOIN_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; 
//		private final String WOEXT_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z"; 
//		private final String REPEXT_TEST_URL2 = "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.toto"; 
//		private final String JARPART_TEST_URL2 = "file:/home/test/j.jar"; 
//		private final File f3; 
//		private final File f4; 
//		private final String TEST_URL3 = "jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"; 
//		private final String PARENT_TEST_URL3 = "jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/"; 
//		private final String JARPART_TEST_URL3 = "jar:http://www.arakhne.org/j.jar!/inner/myjar.jar"; 
//		private final String JOIN_TEST_URL3 = "jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; 
//		private final String JARJAR_TEST_URL1 = "jar:file:/home/test/j.jar!/inner/myjar.jar"; 
//		private final String JARJAR_TEST_URL2 = "/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; 
//		private final String JARJAR_TEST_URL3 = "jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/home/test.x.z.z"; 
//	
//		private final String STRING_WITH_SPACE; 
//		private final URL URL_WITH_SPACE;
		
		private boolean oldLibraryLoaderState;
		
//		protected abstract File createF1();
//		protected abstract URL createU1_F1();
//		protected abstract File createF2();
//		protected abstract URL createU2_F2();
//		protected abstract File createF3();
//		protected abstract File createF4();
//		protected abstract String createStingWithSpace();
		
		protected abstract File createAbsoluteStandardFile();

		protected abstract File createAbsoluteFolderFile();

		protected URL createAbsoluteStandardFileUrl() throws MalformedURLException {
			File file = createAbsoluteStandardFile();
			return new URL("file:" + file.getName());
		}

		protected URL createAbsoluteFolderUrl() throws MalformedURLException {
			File file = createAbsoluteFolderFile();
			return new URL("file:" + file.getName());
		}

		/** @return "http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"
		 */
		protected URL createHttpUrl() throws MalformedURLException {
			return new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag");
		}
		
		/** @return "/home/test/j.jar"
		 */
		protected String createJarFilenameForUrl() {
			return "/home/test/j.jar";
		}

		/** @return "/inner/myjar.jar"
		 */
		protected String createJarInJarFilenameForUrl() {
			return "/inner/myjar.jar";
		}

		/** @return "/org/arakhne/afc/vmutil/file.x.z.z"
		 */
		protected String createInJarFilename() {
			return "/org/arakhne/afc/vmutil/file.x.z.z";
		}

		/** @return "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"
		 */
		protected URL createFileInJarUrl() throws MalformedURLException {
			return new URL("jar:file:" + createJarFilenameForUrl() + "!" + createInJarFilename());
		}
		
		/** @return "jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"
		 */
		protected URL createFileInJarInJarUrl() throws MalformedURLException {
			return new URL("jar:jar:file:" + createJarFilenameForUrl() + "!"
					+ createJarInJarFilenameForUrl() + "!" + createInJarFilename());
		}
		
		/** @return "/the path/to/file with space.toto"
		 */
		protected String createJarFilenameForUrlWithSpaces() {
			return "/the path/to/file with space.toto";
		}

		/** @return "file:/the path/to/file with space.toto"
		 */
		protected URL createFileUrlWithSpacesHardCoded() throws MalformedURLException {
			return new URL("file:" + createJarFilenameForUrlWithSpaces());
		}

		/** @return "jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"
		 */
		protected URL createFileInJarUrlWithSpaces() throws MalformedURLException {
			return new URL("jar:file:" + createJarFilenameForUrlWithSpaces() + "!" + createInJarFilename());
		}

		protected abstract File createStandardFileWithSpaces();

		protected URL createFileUrlWithSpacesWithFile() throws MalformedURLException {
			File file = createStandardFileWithSpaces();
			return file.toURI().toURL();
		}

		public AbstractFileSystemTest() throws Exception {
//			f1 = createF1(); 
//			f2 = createF2(); 
//			f3 = createF3(); 
//			f4 = createF4(); 
//			pf1 = f1.getParentFile();
//			pf2 = f2.getParentFile();
//			u1 = createU1_F1();
//			u2 = createU2_F2();
//			u3 = new URL(TEST_URL1);
//			u4 = new URL(JOIN_TEST_URL1);
//			u5 = new URL(WOEXT_TEST_URL1);
//			u6 = new URL(REPEXT_TEST_URL1);
//			u7 = new URL(TEST_URL2);
//			u8 = new URL(JOIN_TEST_URL2);
//			u9 = new URL(WOEXT_TEST_URL2);
//			u10 = new URL(REPEXT_TEST_URL2);
//			u11 = new URL(JARPART_TEST_URL2);
//			u13 = new URL(TEST_URL3);
//			u14 = new URL(JARPART_TEST_URL3);
//			u15 = new URL(JOIN_TEST_URL3);
//			pu1 = pf1.toURI().toURL();
//			pu2 = pf2.toURI().toURL();
//			pu3 = new URL(PARENT_TEST_URL1);
//			pu7 = new URL(PARENT_TEST_URL2);
//			pu13 = new URL(PARENT_TEST_URL3);
//			STRING_WITH_SPACE = createStingWithSpace();
//			URL_WITH_SPACE = new File(STRING_WITH_SPACE).toURI().toURL();
		}
	
		@Before
		public void setUp() throws Exception {
			// Disable native library loading during unit tests
			this.oldLibraryLoaderState = LibraryLoader.isEnable();
			LibraryLoader.setEnable(false);
		}
		
		@After
		public void tearDown() throws Exception {
			// Restore library loading state
			LibraryLoader.setEnable(this.oldLibraryLoaderState);
		}
		
		@Test
		public void isWindowNativeFilename() {
			assertFalse(FileSystem.isWindowsNativeFilename("D:/vivus_test/export dae/yup/terrain_physx.dae")); 
			assertTrue(FileSystem.isWindowsNativeFilename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
			assertFalse(FileSystem.isWindowsNativeFilename("/vivus_test/export dae/yup/terrain_physx.dae")); 
			assertFalse(FileSystem.isWindowsNativeFilename("/")); 
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\")); 
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
	
			assertTrue(FileSystem.isWindowsNativeFilename("file:C:\\a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:\\a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file:C:a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file:\\a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file://\\a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file:a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file://a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); 
	
			assertTrue(FileSystem.isWindowsNativeFilename("C:\\a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("C:a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("\\a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("a\\b\\c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); 
	
			assertFalse(FileSystem.isWindowsNativeFilename("file:C:/a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file://C:/a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file:C:a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file://C:a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file:/a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file:///a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file:a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file://a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file://host/a/b/c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file:////host/a/b/c.txt")); 
	
			assertTrue(FileSystem.isWindowsNativeFilename("C:c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file:C:c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file:c.txt")); 
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:c.txt")); 
			assertFalse(FileSystem.isWindowsNativeFilename("file://c.txt")); 
		}
	
		@Test
		public void normalizeWindowNativeFilename() {
			assertEquals(new File("C:/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file:C:\\a\\b\\c.txt")); 
			assertEquals(new File("C:/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file://C:\\a\\b\\c.txt")); 
			assertEquals(new File("C:a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file:C:a\\b\\c.txt")); 
			assertEquals(new File("C:a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); 
			assertEquals(new File("/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file:\\a\\b\\c.txt")); 
			assertEquals(new File("/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file://\\a\\b\\c.txt")); 
			assertEquals(new File("a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file:a\\b\\c.txt")); 
			assertEquals(new File("a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file://a\\b\\c.txt")); 
			assertEquals(new File("//host/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); 
			assertEquals(new File("//host/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); 
	
			assertEquals(new File("C:/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("C:\\a\\b\\c.txt")); 
			assertEquals(new File("C:a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("C:a\\b\\c.txt")); 
			assertEquals(new File("C:a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); 
			assertEquals(new File("/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("\\a\\b\\c.txt")); 
			assertEquals(new File("a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("a\\b\\c.txt")); 
			assertEquals(new File("//host/a/b/c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); 
	
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:/a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:/a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:/a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:///a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://host/a/b/c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:////host/a/b/c.txt")); 
	
			assertEquals(new File("C:c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("C:c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("c.txt")); 
			assertEquals(new File("C:c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file:C:c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:c.txt")); 
			assertEquals(new File("C:c.txt"), 
					FileSystem.normalizeWindowsNativeFilename("file://C:c.txt")); 
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://c.txt")); 
		}
		
		@Test
		public void isJarURLURL() throws Exception {
			assertFalse(FileSystem.isJarURL(createAbsoluteStandardFileUrl()));
			assertFalse(FileSystem.isJarURL(createAbsoluteFolderUrl()));
			assertFalse(FileSystem.isJarURL(createHttpUrl()));
			assertTrue(FileSystem.isJarURL(createFileInJarUrl()));
			assertTrue(FileSystem.isJarURL(createFileInJarInJarUrl()));
			assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesHardCoded()));  
			assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesWithFile()));
	
			assertInlineParameterUsage(FileSystem.class, "isJarURL", URL.class);
		}
		
		@Test
		public void getJarURLURL() throws Exception {
			assertNull(FileSystem.getJarURL(createAbsoluteStandardFileUrl()));
			assertNull(FileSystem.getJarURL(createAbsoluteFolderUrl()));
			assertNull(FileSystem.getJarURL(createHttpUrl()));
			assertEquals(new URL("file:" + createJarFilenameForUrl()), FileSystem.getJarURL(createFileInJarUrl()));
			assertEquals(new URL("jar:file:"
					+ createJarFilenameForUrl() + "!"
					+ createJarInJarFilenameForUrl()),
					FileSystem.getJarURL(createFileInJarInJarUrl()));
	
			assertEquals(new URL("file:" + createJarFilenameForUrlWithSpaces()), FileSystem.getJarURL(createFileInJarUrlWithSpaces()));  
			assertNull(FileSystem.getJarFile(createFileUrlWithSpacesHardCoded()));
		}
		
		@Test
		public void convertStringToFile() {
			assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:/vivus_test/export dae/yup/terrain_physx.dae")); 
			assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
			assertNormedFile("/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("/vivus_test/export dae/yup/terrain_physx.dae")); 
			assertNormedFile("/", FileSystem.convertStringToFile("/")); 
			assertNormedFile("//", FileSystem.convertStringToFile("\\\\")); 
			assertNormedFile("//vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
			assertNormedFile("////vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
	
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:\\a\\b\\c.txt")); 
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:\\a\\b\\c.txt")); 
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a\\b\\c.txt")); 
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt")); 
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:\\a\\b\\c.txt")); 
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file://\\a\\b\\c.txt")); 
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a\\b\\c.txt")); 
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a\\b\\c.txt")); 
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:\\\\host\\a\\b\\c.txt")); 
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file://\\\\host\\a\\b\\c.txt")); 
	
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("C:\\a\\b\\c.txt")); 
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("C:a\\b\\c.txt")); 
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt")); 
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("\\a\\b\\c.txt")); 
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("a\\b\\c.txt")); 
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("\\\\host\\a\\b\\c.txt")); 
	
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:/a/b/c.txt")); 
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:/a/b/c.txt")); 
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a/b/c.txt")); 
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a/b/c.txt")); 
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:/a/b/c.txt")); 
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:///a/b/c.txt")); 
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a/b/c.txt")); 
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a/b/c.txt")); 
			assertNormedFile("host/a/b/c.txt", FileSystem.convertStringToFile("file://host/a/b/c.txt")); 
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:////host/a/b/c.txt")); 
	
			assertNormedFile("C:c.txt", FileSystem.convertStringToFile("C:c.txt")); 
			assertNormedFile("c.txt", FileSystem.convertStringToFile("c.txt")); 
			assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file:C:c.txt")); 
			assertNormedFile("c.txt", FileSystem.convertStringToFile("file:c.txt")); 
			assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file://C:c.txt")); 
			assertNormedFile("c.txt", FileSystem.convertStringToFile("file://c.txt")); 
		}
	
//		@Test
//		public void getJarFileURL() throws Exception {
//			assertNull(FileSystem.getJarFile(createAbsoluteStandardFileUrl()));
//			assertNull(FileSystem.getJarFile(createAbsoluteFolderUrl()));
//			assertNull(FileSystem.getJarFile(createHttpUrl()));
//			assertEquals(new File(create), FileSystem.getJarFile(createFileInJarUrl()));
//			assertEquals(f4, FileSystem.getJarFile(createFileInJarInJarUrl()));
//	
//			assertEquals(new File("/titi"),  
//					FileSystem.getJarFile(new URL("jar:file:"+STRING_WITH_SPACE+"!/titi")));  
//			assertNull(FileSystem.getJarFile(URL_WITH_SPACE));
//	
//			assertEquals(new File(JARJAR_TEST_URL2), FileSystem.getJarFile(
//					new URL(JARJAR_TEST_URL3)));
//		}
	
//		@Test
//		public void toJarURLFileFile() throws MalformedURLException {
//			assertEquals(u7, FileSystem.toJarURL(f3, f4));
//		}
//	
//		@Test
//		public void toJarURLFileString() throws MalformedURLException {
//			assertEquals(u7, FileSystem.toJarURL(f3, f4.getPath()));
//			assertInlineParameterUsage(FileSystem.class, "toJarURL", File.class, String.class);
//		}
//	
//		@Test
//		public void toJarURLURLFile() throws MalformedURLException {
//			assertEquals(u7, FileSystem.toJarURL(u11, f4));
//			assertEquals(new URL(JARJAR_TEST_URL3), FileSystem.toJarURL(new URL(JARJAR_TEST_URL1), new File(JARJAR_TEST_URL2)));
//		}
//	
//		@Test
//		public void toJarURLURLString() throws MalformedURLException {
//			assertEquals(u7, FileSystem.toJarURL(u11, f4.getPath()));
//			assertEquals(new URL(JARJAR_TEST_URL3), FileSystem.toJarURL(new URL(JARJAR_TEST_URL1), JARJAR_TEST_URL2));
//		}
//	
//		@Test
//		public void dirnameFile() throws Exception {
//			assertEquals(new URL("file", "", "/home"),   
//					FileSystem.dirname(f1));
//			assertEquals(new URL("file", "", "/"),   
//					FileSystem.dirname(f2));
//			assertNull(FileSystem.dirname(new File("/"))); 
//	
//			assertEquals(new URL("file:/the path/to"),  
//					FileSystem.dirname(new File(STRING_WITH_SPACE)));
//		}
//	
//		/**
//		 * @throws MalformedURLException 
//		 */
//		@Test
//		public void dirnameURL() throws MalformedURLException {
//			assertEquals(new URL("file", "", "."),   
//					FileSystem.dirname(new URL("file", "", "marbre.jpg")));   
//			assertEquals(new URL("http", "www.arakhne.org", "."),   
//					FileSystem.dirname(new URL("http", "www.arakhne.org", "marbre.jpg")));   
//			assertEquals(pu1, FileSystem.dirname(u1));
//			assertEquals(pu2, FileSystem.dirname(u2));
//			assertEquals(pu3, FileSystem.dirname(u3));
//			assertEquals(pu7, FileSystem.dirname(u7));
//			assertEquals(pu13, FileSystem.dirname(u13));
//			assertEquals(new URL("file", "", "/"),   
//					FileSystem.dirname(new URL("file:///a/"))); 
//			assertNull(FileSystem.dirname(new URL("file://"))); 
//			
//			assertEquals(new URL("file", "", "/a/b%20c/"),   
//					FileSystem.dirname(new File("/a/b c/d.txt").toURI().toURL())); 
//	
//			assertEquals(new URL("file:/the%20path/to/"),  
//					FileSystem.dirname(new URL("file:"+STRING_WITH_SPACE))); 
//			assertEquals(new URL("file:/the%20path/to/"),  
//					FileSystem.dirname(URL_WITH_SPACE));
//		}
//	
//		/**
//		 */
//		@Test
//		public void largeBasenameString() {
//			assertEquals("test.x.z.z", FileSystem.largeBasename(f1.getAbsolutePath())); 
//			assertEquals("home", FileSystem.largeBasename(f2.getAbsolutePath())); 
//			assertEquals("a.b.c", FileSystem.largeBasename("file:///a.b.c/"));  
//			assertEquals("file:", FileSystem.largeBasename("file://"));  
//	
//			try {
//				assertEquals("terrain_physx.dae", FileSystem.largeBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
//				fail("expecting assertion failure"); 
//			}
//			catch(AssertionError exception) {
//				//
//			}
//			try {
//				assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
//				fail("expecting assertion failure"); 
//			}
//			catch(AssertionError exception) {
//				//
//			}
//	
//			assertEquals("file with space.toto",  
//					FileSystem.largeBasename(STRING_WITH_SPACE));
//		}
//	
//		/**
//		 */
//		@Test
//		public void largeBasenameFile() {
//			assertEquals("test.x.z.z", FileSystem.largeBasename(f1)); 
//			assertEquals("home", FileSystem.largeBasename(f2)); 
//			assertEquals("a.b.c", FileSystem.largeBasename(new File("/a.b.c/")));  
//			assertEquals("", FileSystem.largeBasename(new File("/")));  
//	
//			assertEquals("file with space.toto",  
//					FileSystem.largeBasename(new File(STRING_WITH_SPACE)));
//			
//			assertInlineParameterUsage(FileSystem.class, "largeBasename", File.class);
//		}
//	
//		/**
//		 * @throws MalformedURLException 
//		 */
//		@Test
//		public void largeBasenameURL() throws MalformedURLException {
//			assertEquals("test.x.z.z", FileSystem.largeBasename(u1)); 
//			assertEquals("home", FileSystem.largeBasename(u2)); 
//			assertEquals("file.x.z.z", FileSystem.largeBasename(u3)); 
//			assertEquals("file.x.z.z", FileSystem.largeBasename(u7)); 
//			assertEquals("a.b.c", FileSystem.largeBasename(new URL("file:///a.b.c/")));  
//			assertEquals("", FileSystem.largeBasename(new URL("file://")));  
//	
//			URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae");   
//			try {
//				assertEquals("terrain_physx.dae", FileSystem.largeBasename(url)); 
//				fail("expecting assertion failure"); 
//			}
//			catch(AssertionError exception) {
//				//
//			}
//	
//			assertEquals("file with space.toto",  
//					FileSystem.largeBasename(new URL("file:"+STRING_WITH_SPACE))); 
//			assertEquals("file with space.toto",  
//					FileSystem.largeBasename(new File(STRING_WITH_SPACE)));
//		}
//	
//		/**
//		 */
//		@Test
//		public void basenameString() {
//			assertEquals("test.x.z", FileSystem.basename(f1.getAbsolutePath())); 
//			assertEquals("home", FileSystem.basename(f2.getAbsolutePath())); 
//			assertEquals("a.b", FileSystem.basename("/a.b.c/"));  
//			assertEquals("", FileSystem.basename(""));  
//	
//			try {
//				assertEquals("terrain_physx", FileSystem.basename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
//				fail("expecting assertion failure"); 
//			}
//			catch(AssertionError exception) {
//				//
//			}
//			try {
//				assertEquals("terrain_physx", FileSystem.basename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
//				fail("expecting assertion failure"); 
//			}
//			catch(AssertionError exception) {
//				//
//			}
//	
//			assertEquals("file with space",  
//					FileSystem.basename(STRING_WITH_SPACE));
//		}
//	
//		/**
//		 */
//		@Test
//		public void basenameFile() {
//			assertEquals("test.x.z", FileSystem.basename(f1)); 
//			assertEquals("home", FileSystem.basename(f2)); 
//			assertEquals("a.b", FileSystem.basename(new File("/a.b.c/")));  
//			assertEquals("", FileSystem.basename(new File("/")));  
//	
//			assertEquals("file with space",  
//					FileSystem.basename(new File(STRING_WITH_SPACE)));
//		}
//	
//		/**
//		 * @throws MalformedURLException 
//		 */
//		@Test
//		public void basenameURL() throws MalformedURLException {
//			assertEquals("test.x.z", FileSystem.basename(u1)); 
//			assertEquals("home", FileSystem.basename(u2)); 
//			assertEquals("file.x.z", FileSystem.basename(u3)); 
//			assertEquals("file.x.z", FileSystem.basename(u7)); 
//			assertEquals("a.b", FileSystem.basename(new URL("file:///a.b.c/")));  
//			assertEquals("", FileSystem.basename(new URL("file://")));  
//	
//			URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae");   
//			try {
//				assertEquals("terrain_physx", FileSystem.basename(url)); 
//				fail("expecting assertion failure"); 
//			}
//			catch(AssertionError exception) {
//				//
//			}
//	
//			assertEquals("file with space",  
//					FileSystem.basename(new URL("file:"+STRING_WITH_SPACE))); 
//			assertEquals("file with space",  
//					FileSystem.basename(new File(STRING_WITH_SPACE)));
//		}
//	
//		/**
//		 */
//		@Test
//		public void shortBasenameString() {
//			assertEquals("test", FileSystem.shortBasename(f1.getAbsolutePath())); 
//			assertEquals("home", FileSystem.shortBasename(f2.getAbsolutePath())); 
//			assertEquals("a", FileSystem.shortBasename("/a.b.c/"));  
//			assertEquals("", FileSystem.shortBasename(""));  
//			assertEquals("terrain_physx", FileSystem.shortBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
//			assertEquals("terrain_physx", FileSystem.shortBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
//	
//			assertEquals("file with space",  
//					FileSystem.shortBasename(STRING_WITH_SPACE));
//		}
//	
//		/**
//		 */
//		@Test
//		public void shortBasenameFile() {
//			assertEquals("test", FileSystem.shortBasename(f1)); 
//			assertEquals("home", FileSystem.shortBasename(f2)); 
//			assertEquals("a", FileSystem.shortBasename(new File("/a.b.c/")));  
//			assertEquals("", FileSystem.shortBasename(new File("/")));  
//	
//			assertEquals("file with space",  
//					FileSystem.shortBasename(new File(STRING_WITH_SPACE)));
//		}
//	
//		/**
//		 * @throws MalformedURLException 
//		 */
//		@Test
//		public void shortBasenameURL() throws MalformedURLException {
//			assertEquals("test", FileSystem.shortBasename(u1)); 
//			assertEquals("home", FileSystem.shortBasename(u2)); 
//			assertEquals("file", FileSystem.shortBasename(u3)); 
//			assertEquals("file", FileSystem.shortBasename(u7)); 
//			assertEquals("a", FileSystem.shortBasename(new URL("file:///a.b.c/")));  
//			assertEquals("", FileSystem.shortBasename(new URL("file://")));  
//			
//			URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae");   
//			try {
//				assertEquals("terrain_physx", FileSystem.shortBasename(url)); 
//				fail("expecting assertion failure"); 
//			}
//			catch(AssertionError exception) {
//				//
//			}
//	
//			assertEquals("file with space",  
//					FileSystem.shortBasename(new URL("file:"+STRING_WITH_SPACE))); 
//			assertEquals("file with space",  
//					FileSystem.shortBasename(URL_WITH_SPACE));
//		}
//	
//		/**
//		 */
//		@Test
//		public void extensionFile() {
//			assertEquals(".z", FileSystem.extension(f1)); 
//			assertEquals("", FileSystem.extension(f2)); 
//			assertEquals(".c", FileSystem.extension(new File("/a.b.c/")));  
//			assertEquals("", FileSystem.extension(new File("/")));  
//	
//			assertEquals(".toto",  
//					FileSystem.extension(new File(STRING_WITH_SPACE)));
//		}
//	
//		/**
//		 * @throws MalformedURLException 
//		 */
//		@Test
//		public void extensionURL() throws MalformedURLException {
//			assertEquals(".z", FileSystem.extension(u1)); 
//			assertEquals("", FileSystem.extension(u2)); 
//			assertEquals(".z", FileSystem.extension(u3)); 
//			assertEquals(".z", FileSystem.extension(u7)); 
//			assertEquals(".z", FileSystem.extension(u13)); 
//			assertEquals(".c", FileSystem.extension(new URL("file:///a.b.c/")));  
//			assertEquals("", FileSystem.extension(new URL("file://")));  
//	
//			assertEquals(".toto",  
//					FileSystem.extension(new URL("file:"+STRING_WITH_SPACE))); 
//			assertEquals(".toto",  
//					FileSystem.extension(URL_WITH_SPACE));
//		}
//	
//		/**
//		 */
//		@Test
//		public void extensionsFile() {
//			assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(f1)));   
//			assertTrue(Arrays.equals(new String[0], FileSystem.extensions(f2)));
//			assertTrue(Arrays.equals(new String[]{"b","c"}, FileSystem.extensions(new File("/a.b.c/"))));   
//			assertTrue(Arrays.equals(new String[0], FileSystem.extensions(new File("/")))); 
//	
//			assertTrue(Arrays.equals(new String[] {"toto"},  
//					FileSystem.extensions(new File(STRING_WITH_SPACE))));
//		}
//	
//		/**
//		 * @throws MalformedURLException 
//		 */
//		@Test
//		public void extensionsURL() throws MalformedURLException {
//			assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(u1)));   
//			assertTrue(Arrays.equals(new String[0], FileSystem.extensions(u2)));
//			assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(u3)));   
//			assertTrue(Arrays.equals(new String[]{"x","z","z"}, FileSystem.extensions(u7)));   
//			assertTrue(Arrays.equals(new String[]{"b","c"}, FileSystem.extensions(new URL("file:///a.b.c/"))));   
//			assertTrue(Arrays.equals(new String[0], FileSystem.extensions(new URL("file://")))); 
//	
//			assertTrue(Arrays.equals(new String[] {"toto"},  
//					FileSystem.extensions(new File(STRING_WITH_SPACE))));
//		}
//	
//		/**
//		 */
//		@Test
//		public void splitFile() {
//			assertTrue(Arrays.equals(
//					new String[] {
//						"", 
//						"home", 
//						"test.x.z.z", 
//					},
//					FileSystem.split(f1)));
//			assertTrue(Arrays.equals(
//					new String[] {
//						"", 
//						"home", 
//					},
//					FileSystem.split(f2)));
//	
//			assertTrue(Arrays.equals(
//					new String[] {
//								"", 
//								"the path", 
//								"to", 
//								"file with space.toto" 
//					},
//					FileSystem.split(new File(STRING_WITH_SPACE))));
//		}
//	
//		@Test
//		public void splitURL() throws Exception {
//			String[] tab;
//			
//			tab = FileSystem.split(u1);
//			assertTrue(Arrays.equals(
//					new String[] {
//						"", 
//						"home", 
//						"test.x.z.z", 
//					},
//					tab));
//			
//			tab = FileSystem.split(u2);
//			assertTrue(Arrays.equals(
//					new String[] {
//						"", 
//						"home", 
//					},
//					tab));
//			
//			tab = FileSystem.split(u7);
//			assertTrue(Arrays.equals(
//					new String[] {
//						"", 
//						"org", 
//						"arakhne", 
//						"afc", 
//						"vmutil", 
//						"file.x.z.z", 
//					},
//					tab));
//			
//			tab = FileSystem.split(u13);
//			assertTrue(Arrays.equals(
//					new String[] {
//						"", 
//						"org", 
//						"arakhne", 
//						"afc", 
//						"vmutil", 
//						"file.x.z.z", 
//					},
//					tab));
//	
//			assertTrue(Arrays.equals(
//					new String[] {
//								"", 
//								"the path", 
//								"to", 
//								"file with space.toto" 
//					},
//					FileSystem.split(new URL("file:"+STRING_WITH_SPACE)))); 
//			assertTrue(Arrays.equals(
//					new String[] {
//								"", 
//								"the path", 
//								"to", 
//								"file with space.toto" 
//					},
//					FileSystem.split(URL_WITH_SPACE)));
//		}
//	
//		/**
//		 */
//		@Test
//		public void joinFileStringArray() {
//			assertEquals(new File(new File(f1, "home"), "test.x.z.z"),  
//					FileSystem.join(f1,
//					"", 
//					"home", 
//					"test.x.z.z")); 
//		}
//	
//		/**
//		 */
//		@Test
//		public void joinFileFileArray() {
//			assertEquals(new File(new File(f1, "home"), "test.x.z.z"),  
//					FileSystem.join(f1,
//					new File("home"), 
//					new File("test.x.z.z"))); 
//			assertEquals(new File(new File(f1, "home"), "test.x.z.z"),  
//					FileSystem.join(f1,
//					new File(File.separator+"home"), 
//					new File("test.x.z.z"))); 
//		}
//	
//		@Test
//		public void joinURLStringArray() throws Exception {
//			assertEquals(new File(new File(f1, "home"), "test.x.z.z").toURI().toURL(),  
//					FileSystem.join(u1,
//					"", 
//					"home", 
//					"test.x.z.z")); 
//	
//			assertEquals(u4,
//					FileSystem.join(u3,
//					"", 
//					"home", 
//					"test.x.z.z")); 
//	
//			assertEquals(u8,
//					FileSystem.join(u7,
//					"", 
//					"home", 
//					"test.x.z.z")); 
//	
//			assertEquals(u15,
//					FileSystem.join(u13,
//					"", 
//					"home", 
//					"test.x.z.z")); 
//	
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  
//					FileSystem.join(new URL("file:"+STRING_WITH_SPACE), "a", "b"));    
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  
//					FileSystem.join(URL_WITH_SPACE, "a", "b"));  
//		}
//	
//		@Test
//		public void joinURLFileArray() throws Exception {
//			assertEquals(new File(new File(f1, "home"), "test.x.z.z").toURI().toURL(),  
//					FileSystem.join(u1,
//					new File("home"), 
//					new File("test.x.z.z"))); 
//			assertEquals(new File(new File(f1, "home"), "test.x.z.z").toURI().toURL(),  
//					FileSystem.join(u1,
//					new File(File.separator+"home"), 
//					new File("test.x.z.z"))); 
//			assertEquals(u4,
//					FileSystem.join(u3,
//					new File(File.separator+"home"), 
//					new File("test.x.z.z"))); 
//			assertEquals(u8,
//					FileSystem.join(u7,
//					new File(File.separator+"home"), 
//					new File("test.x.z.z"))); 
//			assertEquals(u15,
//					FileSystem.join(u13,
//					new File(File.separator+"home"), 
//					new File("test.x.z.z"))); 
//			
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  
//					FileSystem.join(new URL("file:"+STRING_WITH_SPACE), new File("a"), new File("b")));   
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b"),  
//					FileSystem.join(URL_WITH_SPACE, new File("a"), new File("b")));  
//		}
//	
//		/**
//		 */
//		@Test
//		public void hasExtensionFileString() {
//			assertTrue(FileSystem.hasExtension(f1, ".z")); 
//			assertTrue(FileSystem.hasExtension(f1, "z")); 
//			assertFalse(FileSystem.hasExtension(f1, ".x")); 
//			assertFalse(FileSystem.hasExtension(f1, "")); 
//			assertTrue(FileSystem.hasExtension(f2, "")); 
//			assertFalse(FileSystem.hasExtension(f2, ".z")); 
//	
//			assertTrue(FileSystem.hasExtension(new File(STRING_WITH_SPACE), ".toto"));  
//			assertTrue(FileSystem.hasExtension(new File(STRING_WITH_SPACE), "toto"));  
//			assertFalse(FileSystem.hasExtension(new File(STRING_WITH_SPACE), ".zip"));  
//			assertFalse(FileSystem.hasExtension(new File(STRING_WITH_SPACE), "zip"));  
//		}
//	
//		@Test
//		public void hasExtensionURLString() throws Exception {
//			assertTrue(FileSystem.hasExtension(u1, ".z")); 
//			assertTrue(FileSystem.hasExtension(u1, "z")); 
//			assertFalse(FileSystem.hasExtension(u1, ".x")); 
//			assertFalse(FileSystem.hasExtension(u1, "")); 
//			assertTrue(FileSystem.hasExtension(u2, "")); 
//			assertFalse(FileSystem.hasExtension(u2, ".z")); 
//			assertTrue(FileSystem.hasExtension(u3, ".z")); 
//			assertTrue(FileSystem.hasExtension(u3, "z")); 
//			assertFalse(FileSystem.hasExtension(u3, ".x")); 
//			assertFalse(FileSystem.hasExtension(u3, "")); 
//			assertTrue(FileSystem.hasExtension(u7, ".z")); 
//			assertTrue(FileSystem.hasExtension(u7, "z")); 
//			assertFalse(FileSystem.hasExtension(u7, ".x")); 
//			assertFalse(FileSystem.hasExtension(u7, "")); 
//	
//			assertTrue(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), ".toto"));   
//			assertTrue(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), "toto"));   
//			assertFalse(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), ".zip"));   
//			assertFalse(FileSystem.hasExtension(new URL("file:"+STRING_WITH_SPACE), "zip"));   
//			assertTrue(FileSystem.hasExtension(URL_WITH_SPACE, ".toto")); 
//			assertTrue(FileSystem.hasExtension(URL_WITH_SPACE, "toto")); 
//			assertFalse(FileSystem.hasExtension(URL_WITH_SPACE, ".zip")); 
//			assertFalse(FileSystem.hasExtension(URL_WITH_SPACE, "zip")); 
//		}
//	
//		/**
//		 */
//		@Test
//		public void removeExtensionFile() {
//			assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(f1)); 
//			assertEquals(new File("/home"), FileSystem.removeExtension(f2)); 
//	
//			assertEquals(new File("/the path/to/file with space"),  
//					FileSystem.removeExtension(new File(STRING_WITH_SPACE)));
//		}
//	
//		@Test
//		public void removeExtensionURL() throws Exception {
//			assertEquals(new File("/home/test.x.z").toURI().toURL(), 
//					FileSystem.removeExtension(u1));
//			assertEquals(new File("/home").toURI().toURL(),  
//					FileSystem.removeExtension(u2));
//			assertEquals(u5, FileSystem.removeExtension(u3));
//			assertEquals(u9, FileSystem.removeExtension(u7));
//	
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space"),  
//					FileSystem.removeExtension(new URL("file:"+STRING_WITH_SPACE))); 
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space"),  
//					FileSystem.removeExtension(URL_WITH_SPACE));
//		}
//	
//		/**
//		 */
//		public void replaceExtensionFileString() {
//			assertEquals(new File("/home/test.x.z.toto"), FileSystem.replaceExtension(f1, ".toto"));  
//			assertEquals(new File("/home.toto"), FileSystem.replaceExtension(f2, ".toto"));  
//	
//			assertEquals(new File("/the path/to/file with space.zip"),  
//					FileSystem.replaceExtension(new File(STRING_WITH_SPACE), ".zip")); 
//		}
//	
//		@Test
//		public void replaceExtensionURLString() throws Exception {
//			assertEquals(new File("/home/test.x.z.toto").toURI().toURL(),  
//					FileSystem.replaceExtension(u1, ".toto")); 
//			assertEquals(new File("/home.toto").toURI().toURL(),  
//					FileSystem.replaceExtension(u2, ".toto")); 
//			assertEquals(u6, FileSystem.replaceExtension(u3, ".toto")); 
//			assertEquals(u10, FileSystem.replaceExtension(u7, ".toto")); 
//	
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.zip"),  
//					FileSystem.replaceExtension(new URL("file:"+STRING_WITH_SPACE), ".zip"));  
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.zip"),  
//					FileSystem.replaceExtension(URL_WITH_SPACE, ".zip")); 
//		}
//	
//		/**
//		 */
//		@Test
//		public void addExtensionFileString() {
//			assertEquals(new File("/home/test.x.z.z"), FileSystem.addExtension(f1, ".z"));  
//			assertEquals(new File("/home/test.x.z.z.toto"), FileSystem.addExtension(f1, ".toto"));  
//			assertEquals(new File("/home.toto"), FileSystem.addExtension(f2, ".toto"));  
//			
//			assertEquals(new File(STRING_WITH_SPACE+".zip"), 
//					FileSystem.addExtension(new File(STRING_WITH_SPACE), "zip")); 
//		}
//	
//		@Test
//		public void addExtensionURLString() throws Exception {
//			assertEquals(new File("/home/test.x.z.z").toURI().toURL(),  
//					FileSystem.addExtension(u1, ".z")); 
//			assertEquals(new File("/home/test.x.z.z.toto").toURI().toURL(),  
//					FileSystem.addExtension(u1, ".toto")); 
//			assertEquals(new File("/home.toto").toURI().toURL(),  
//					FileSystem.addExtension(u2, ".toto")); 
//			
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.zip"), 
//					FileSystem.addExtension(new URL("file:"+STRING_WITH_SPACE), ".zip"));  
//		}
//	
//		@Test
//		public void convertStringToURL() throws Exception {
//			URL rr;
//	
//			// The following test permits to check if a specifical behaviour of URL
//			// is still present in the JRE.
//			rr = new URL("file://marbre.jpg"); 
//			assertEquals("file", rr.getProtocol()); 
//			assertEquals("marbre.jpg", rr.getAuthority()); 
//			assertEquals("", rr.getPath()); 
//			//-----
//			
//			assertNull(FileSystem.convertStringToURL(null, true));
//			assertNull(FileSystem.convertStringToURL("", true)); 
//			assertNull(FileSystem.convertStringToURL(null, false));
//			assertNull(FileSystem.convertStringToURL("", false)); 
//	
//			rr = FileSystem.convertStringToURL("file://marbre.jpg", false); 
//			assertNotNull(rr);
//			assertEquals("file", rr.getProtocol()); 
//			assertEquals("", rr.getAuthority());
//			assertEquals("", rr.getHost()); 
//			assertNull(rr.getQuery());
//			assertEquals("marbre.jpg", rr.getPath()); 
//	
//			assertEquals(new URL("http", "www.arakhne.org", "/"),   
//						 FileSystem.convertStringToURL("http://www.arakhne.org/", true)); 
//			assertEquals(new URL("http", "www.arakhne.org", "/"),   
//					 FileSystem.convertStringToURL("http://www.arakhne.org/", false)); 
//	
//			assertEquals(new URL("file", "", f1.getAbsolutePath()),
//					 FileSystem.convertStringToURL("file:"+f1.getAbsolutePath(), true)); 
//			assertEquals(new URL("file", "", f1.getAbsolutePath()),  
//				 FileSystem.convertStringToURL("file:"+f1.getAbsolutePath(), false)); 
//			assertEquals(new URL("file", "", "./toto"),   
//					 FileSystem.convertStringToURL("file:./toto", false)); 
//	
//			// CAUTION: testing right-formed jar URL.
//			assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
//					FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); 
//			assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
//					FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); 
//	
//			// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
//			assertEquals(new URL("file", "", "/home/test/j.jar"),   
//					FileSystem.convertStringToURL("jar:/home/test/j.jar", true)); 
//			assertEquals(new URL("file", "", "/home/test/j.jar"),   
//					FileSystem.convertStringToURL("jar:/home/test/j.jar", false)); 
//	
//			// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
//			assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
//					FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); 
//			assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
//					FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); 
//			
//			URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); 
//			assertNotNull(testResource);
//			URL testResourceFileRel = new URL("file","", "org/arakhne/afc/vmutil/test.txt");   
//			URL testResourceFileAbs = new File("/org/arakhne/afc/vmutil/test.txt").toURI().toURL(); 
//			
//			assertEquals(testResource,
//					 FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", true)); 
//			assertEquals(null,
//					 FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", false)); 
//	
//			assertEquals(testResource,
//					 FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", true)); 
//			assertEquals(null,
//					 FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", false)); 
//	
//			assertEquals(testResource,
//					 FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", true)); 
//			assertEquals(testResourceFileAbs,
//					 FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", false)); 
//	
//			assertEquals(testResource,
//					 FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", true)); 
//			assertEquals(testResourceFileRel,
//					 FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", false)); 
//	
//			assertEquals(new URL("file:"+STRING_WITH_SPACE),  
//					FileSystem.convertStringToURL(STRING_WITH_SPACE, false));
//		}
//	
//		@Test
//		public void convertURLToFile() throws Exception {
//			assertEquals(f1,
//					 FileSystem.convertURLToFile(new URL("file:"+f1.getAbsolutePath()))); 
//	
//			try {
//				FileSystem.convertURLToFile(new URL("http://www.arakhne.org")); 
//				fail("not a file URL"); 
//			}
//			catch(IllegalArgumentException exception) {
//				//
//			}
//			
//			assertEquals(new File("toto").getCanonicalPath(), 
//					 FileSystem.convertURLToFile(new URL("file:./toto")).getCanonicalPath()); 
//	
//			assertEquals(new File("toto").getCanonicalPath(), 
//					 FileSystem.convertURLToFile(new URL("file:toto")).getCanonicalPath()); 
//	
//			assertEquals(new File("toto").getCanonicalPath(), 
//					 FileSystem.convertURLToFile(new URL("file:./abs/../toto")).getCanonicalPath()); 
//	
//			assertEquals(new File("/toto").getCanonicalPath(), 
//					 FileSystem.convertURLToFile(new URL("file:/toto")).getCanonicalPath()); 
//	
//			assertEquals(new File(STRING_WITH_SPACE),
//					FileSystem.convertURLToFile(new URL("file:"+STRING_WITH_SPACE))); 
//			assertEquals(new File(STRING_WITH_SPACE),
//					FileSystem.convertURLToFile(URL_WITH_SPACE));
//		}
//		
//		/**
//		 */
//		@Test
//		public void makeAbsoluteFileFile() {
//			File root = new File(File.separator+"myroot"); 
//	
//			assertNull(FileSystem.makeAbsolute((File)null, (File)null));
//			assertNull(FileSystem.makeAbsolute((File)null, root));
//	
//			assertEquals(new File(File.separator+"toto"), 
//					FileSystem.makeAbsolute(new File(File.separator+"toto"), (File)null)); 
//			assertEquals(new File("toto"), 
//					FileSystem.makeAbsolute(new File("toto"), (File)null)); 
//	
//			assertEquals(new File(File.separator+"toto"), 
//					FileSystem.makeAbsolute(new File(File.separator+"toto"), root)); 
//			assertEquals(new File(File.separator+"myroot"+File.separator+"toto"),  
//					FileSystem.makeAbsolute(new File("toto"), root)); 
//		}
//	
//		@Test
//		public void makeAbsoluteURLFile() throws Exception {
//			File root = new File(File.separator+"myroot"); 
//	
//			assertNull(FileSystem.makeAbsolute((URL)null, (File)null));
//			assertNull(FileSystem.makeAbsolute((URL)null, root));
//	
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new URL("file:/toto"), (File)null)); 
//			assertEquals(new URL("file:toto"), 
//					FileSystem.makeAbsolute(new URL("file:toto"), (File)null)); 
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new URL("file:/toto"), root)); 
//			assertEquals(new URL("file:/myroot/toto"), 
//					FileSystem.makeAbsolute(new URL("file:toto"), root)); 
//	
//			assertEquals(new URL("http://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (File)null)); 
//			assertEquals(new URL("http://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (File)null)); 
//			assertEquals(new URL("http://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("http://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("https://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (File)null)); 
//			assertEquals(new URL("https://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (File)null)); 
//			assertEquals(new URL("https://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("https://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("ftp://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (File)null)); 
//			assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (File)null)); 
//			assertEquals(new URL("ftp://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); 
//			assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); 
//			assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
//			assertEquals(new URL("jar:file:/myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
//		}
//	
//		@Test
//		public void makeAbsoluteURLURL_withfileroot() throws Exception {
//			URL root = new File(File.separator+"myroot").toURI().toURL(); 
//	
//			assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));
//			assertNull(FileSystem.makeAbsolute((URL)null, root));
//	
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new URL("file:/toto"), (URL)null)); 
//			assertEquals(new URL("file:toto"), 
//					FileSystem.makeAbsolute(new URL("file:toto"), (URL)null)); 
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new URL("file:/toto"), root)); 
//			assertEquals(new URL("file:/myroot/toto"), 
//					FileSystem.makeAbsolute(new URL("file:toto"), root)); 
//	
//			assertEquals(new URL("http://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (URL)null)); 
//			assertEquals(new URL("http://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (URL)null)); 
//			assertEquals(new URL("http://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("http://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("https://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (URL)null)); 
//			assertEquals(new URL("https://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (URL)null)); 
//			assertEquals(new URL("https://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("https://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("ftp://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (URL)null)); 
//			assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (URL)null)); 
//			assertEquals(new URL("ftp://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); 
//			assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); 
//			assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
//			assertEquals(new URL("jar:file:/myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
//		}
//		
//		@Test
//		public void makeAbsoluteURLURL_withhttproot() throws Exception {
//			URL root = new URL("http://maven.arakhne.org"); 
//	
//			assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));
//			assertNull(FileSystem.makeAbsolute((URL)null, root));
//	
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new URL("file:/toto"), (URL)null)); 
//			assertEquals(new URL("file:toto"), 
//					FileSystem.makeAbsolute(new URL("file:toto"), (URL)null)); 
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new URL("file:/toto"), root)); 
//			assertEquals(new URL("http://maven.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("file:toto"), root)); 
//	
//			assertEquals(new URL("http://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (URL)null)); 
//			assertEquals(new URL("http://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (URL)null)); 
//			assertEquals(new URL("http://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("http://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("https://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (URL)null)); 
//			assertEquals(new URL("https://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (URL)null)); 
//			assertEquals(new URL("https://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("https://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("ftp://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (URL)null)); 
//			assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (URL)null)); 
//			assertEquals(new URL("ftp://www.arakhne.org/toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); 
//			assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
//					FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); 
//	
//			assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); 
//			assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); 
//			assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
//			assertEquals(new URL("jar:http://maven.arakhne.org/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
//					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
//		}
//	
//		@Test
//		public void makeAbsoluteFileURL_withfileroot() throws Exception {
//			URL root = new URL("http://maven.arakhne.org/myroot"); 
//	
//			assertNull(FileSystem.makeAbsolute((File)null, (URL)null));
//			assertNull(FileSystem.makeAbsolute((File)null, root));
//	
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new File("/toto"), (URL)null)); 
//			assertEquals(new URL("file:toto"), 
//					FileSystem.makeAbsolute(new File("toto"), (URL)null)); 
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new File("/toto"), root)); 
//			assertEquals(new URL("http://maven.arakhne.org/myroot/toto"), 
//					FileSystem.makeAbsolute(new File("toto"), root)); 
//	
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"),  
//					FileSystem.makeAbsolute(new File("a/b/c"), new URL("file:"+STRING_WITH_SPACE)));  
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"),  
//					FileSystem.makeAbsolute(new File("a/b/c"), URL_WITH_SPACE));  
//		}
//	
//		@Test
//		public void makeAbsoluteFileURL_withhttproot() throws Exception {
//			URL root = new File(File.separator+"myroot").toURI().toURL(); 
//	
//			assertNull(FileSystem.makeAbsolute((File)null, (URL)null));
//			assertNull(FileSystem.makeAbsolute((File)null, root));
//	
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new File("/toto"), (URL)null)); 
//			assertEquals(new URL("file:toto"), 
//					FileSystem.makeAbsolute(new File("toto"), (URL)null)); 
//			assertEquals(new URL("file:/toto"), 
//					FileSystem.makeAbsolute(new File("/toto"), root)); 
//			assertEquals(new URL("file:/myroot/toto"), 
//					FileSystem.makeAbsolute(new File("toto"), root)); 
//		}
//	
//		@Test
//		public void getParentURLURL() throws Exception {
//			assertEquals(
//					new URL("http://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("http://www.arakhne.org"))); 
//			assertEquals(
//					new URL("http://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("http://www.arakhne.org/"))); 
//			assertEquals(
//					new URL("http://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("http://www.arakhne.org/toto"))); 
//			assertEquals(
//					new URL("http://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/"))); 
//			assertEquals(
//					new URL("http://www.arakhne.org/toto/"), 
//					FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi"))); 
//			assertEquals(
//					new URL("http://www.arakhne.org/toto/"), 
//					FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi/"))); 
//	
//			assertEquals(
//					new URL("https://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("https://www.arakhne.org"))); 
//			assertEquals(
//					new URL("https://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("https://www.arakhne.org/"))); 
//			assertEquals(
//					new URL("https://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("https://www.arakhne.org/toto"))); 
//			assertEquals(
//					new URL("https://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/"))); 
//			assertEquals(
//					new URL("https://www.arakhne.org/toto/"), 
//					FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi"))); 
//			assertEquals(
//					new URL("https://www.arakhne.org/toto/"), 
//					FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi/"))); 
//	
//			assertEquals(
//					new URL("ftp://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("ftp://www.arakhne.org"))); 
//			assertEquals(
//					new URL("ftp://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("ftp://www.arakhne.org/"))); 
//			assertEquals(
//					new URL("ftp://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto"))); 
//			assertEquals(
//					new URL("ftp://www.arakhne.org/"), 
//					FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/"))); 
//			assertEquals(
//					new URL("ftp://www.arakhne.org/toto/"), 
//					FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi"))); 
//			assertEquals(
//					new URL("ftp://www.arakhne.org/toto/"), 
//					FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi/"))); 
//	
//			assertEquals(
//					new URL("file:/toto/"), 
//					FileSystem.getParentURL(new URL("file:/toto/titi"))); 
//			assertEquals(
//					new URL("file:/toto/"), 
//					FileSystem.getParentURL(new URL("file:/toto/titi/"))); 
//			assertEquals(
//					new URL("file:/"), 
//					FileSystem.getParentURL(new URL("file:/toto"))); 
//			assertEquals(
//					new URL("file:/"), 
//					FileSystem.getParentURL(new URL("file:/toto/"))); 
//			assertEquals(
//					new URL("file:./toto/"), 
//					FileSystem.getParentURL(new URL("file:./toto/titi"))); 
//			assertEquals(
//					new URL("file:./toto/"), 
//					FileSystem.getParentURL(new URL("file:./toto/titi/"))); 
//			assertEquals(
//					new URL("file:./"), 
//					FileSystem.getParentURL(new URL("file:./toto"))); 
//			assertEquals(
//					new URL("file:./"), 
//					FileSystem.getParentURL(new URL("file:./toto/"))); 
//			assertEquals(
//					new URL("file:../"), 
//					FileSystem.getParentURL(new URL("file:."))); 
//			assertEquals(
//					new URL("file:../"), 
//					FileSystem.getParentURL(new URL("file:./"))); 
//			assertEquals(
//					new URL("file:../"), 
//					FileSystem.getParentURL(new URL("file:toto"))); 
//			assertEquals(
//					new URL("file:../"), 
//					FileSystem.getParentURL(new URL("file:toto/"))); 
//	
//			assertEquals(
//					new URL("jar:file:test.jar!/toto/"), 
//					FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi"))); 
//			assertEquals(
//					new URL("jar:file:test.jar!/toto/"), 
//					FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi/"))); 
//			assertEquals(
//					new URL("jar:file:test.jar!/"), 
//					FileSystem.getParentURL(new URL("jar:file:test.jar!/toto"))); 
//			assertEquals(
//					new URL("jar:file:test.jar!/"), 
//					FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/"))); 
//			assertEquals(
//					new URL("jar:file:test.jar!/"), 
//					FileSystem.getParentURL(new URL("jar:file:test.jar!/"))); 
//	
//			assertEquals(new URL("file:/the path/to/"),  
//					FileSystem.getParentURL(new URL("file:"+STRING_WITH_SPACE)));  
//			assertEquals(new URL("file:/the%20path/to/"),  
//					FileSystem.getParentURL(URL_WITH_SPACE));
//		}
//		
//		@Test
//		public void convertFileToURLFile() throws Exception {
//			URLHandlerUtil.installArakhneHandlers();
//			try {
//				File f1 = new File("/toto"); 
//				URL u1 = f1.toURI().toURL();
//				URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); 
//				URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt"); 
//				File f2 = FileSystem.convertURLToFile(u2);
//				
//				URL actual;
//				
//				actual = FileSystem.convertFileToURL(f1);
//				assertEqualUrls(u1, actual);
//	
//				actual = FileSystem.convertFileToURL(f2);
//				assertEqualUrls(u2e, actual);
//			}
//			finally {
//				URLHandlerUtil.uninstallArakhneHandlers();
//			}
//	
//			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto"),  
//					FileSystem.convertFileToURL(new File(STRING_WITH_SPACE)));
//		}
//		
//		@Test
//		public void toShortestURLURL() throws Exception {
//			URLHandlerUtil.installArakhneHandlers();
//			try {
//				File f1 = new File("/toto"); 
//				URL u1 = f1.toURI().toURL();
//				URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); 
//				URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt"); 
//				
//				URL actual;
//				
//				actual = FileSystem.toShortestURL(u1);
//				assertEqualUrls(u1, actual);
//	
//				actual = FileSystem.toShortestURL(u2);
//				assertEqualUrls(u2e, actual);
//			}
//			finally {
//				URLHandlerUtil.uninstallArakhneHandlers();
//			}
//		}
//	
//		private void assertEqualUrls(URL expected, URL actual) {
//			String u1 = expected==null ? null : expected.toExternalForm().replaceAll("/$", ""); 
//			String u2 = actual==null ? null : actual.toExternalForm().replaceAll("/$", ""); 
//			assertEquals(u1, u2);
//		}
//		
//		@Test
//		public void makeRelativeFileFile() throws Exception {
//			File root, abs, rel;
//			
//			root = FileSystem.getUserHomeDirectory();
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b");  
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");  
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			abs = new File("a","b"); 
//			rel = new File("a","b"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			
//			
//			root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc");  
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+"a"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//		
//		
//			root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc"); 
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+"a"+File.separator+"zz"+File.separator+"bc"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//		}
//	
//		@Test
//		public void makeRelativeFileURL() throws Exception {
//			File abs, rel;
//			URL root;
//			
//			root = FileSystem.getUserHomeDirectory().toURI().toURL();
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b");  
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");  
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			abs = new File("a","b"); 
//			rel = new File("a","b"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			
//			
//			root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");  
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+"a"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//		
//		
//			root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); 
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+"a"+File.separator+"zz"+File.separator+"bc"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//		}
//	
//		@Test
//		public void makeRelativeURLURL() throws Exception {
//			File rel;
//			URL root, abs;
//			
//			root = FileSystem.getUserHomeDirectory().toURI().toURL();
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b").toURI().toURL();  
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");  
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//			
//			
//			root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");  
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+"a"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//	
//		
//		
//			root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); 
//			
//			abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc").toURI().toURL(); 
//			rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+FileSystem.PARENT_DIRECTORY+File.separator
//					+"a"+File.separator+"zz"+File.separator+"bc"); 
//			assertEquals(rel, FileSystem.makeRelative(abs, root));
//		}
//		
//		@Test
//		public void makeCanonicalURL() throws MalformedURLException {
//			assertEquals(
//					new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"), 
//					FileSystem.makeCanonicalURL(new URL(TEST_URL1)));
//	
//			assertEquals(
//					new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"), 
//					FileSystem.makeCanonicalURL(new URL(TEST_URL2)));
//	
//			assertEquals(
//					new URL("jar:jar:http://www.arakhne.org/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"), 
//					FileSystem.makeCanonicalURL(new URL(TEST_URL3)));
//			
//			assertEquals(
//					new URL("file:/a/b/c/d/e"), 
//					FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/./d/e"))); 
//			
//			assertEquals(
//					new URL("file:/a/d/e"), 
//					FileSystem.makeCanonicalURL(new URL("file:/a/b/../c/../d/e"))); 
//	
//			assertEquals(
//					new URL("file:/a/b/d/e"), 
//					FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/../d/e"))); 
//	
//			assertEquals(
//					new URL("file:../a/b/c/d/e"), 
//					FileSystem.makeCanonicalURL(new URL("file:../a/b/./c/./d/e"))); 
//	
//			assertEquals(
//					new URL("file:../a/c/d/e"), 
//					FileSystem.makeCanonicalURL(new URL("file:../a/b/../c/./d/e"))); 
//		}
//		
//		private String readInputStream(InputStream is) throws IOException {
//			StringBuilder b = new StringBuilder();
//			byte[] buffer = new byte[2048];
//			int len;
//			while ((len=is.read(buffer))>0) {
//				b.append(new String(buffer, 0, len));
//			}
//			is.close();
//			return b.toString();
//		}
//		
//		private void createZip(File testArchive) throws IOException {
//			File testDir = FileSystem.createTempDirectory("unittest", null); 
//			FileSystem.deleteOnExit(testDir);
//			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), testDir); 
//			FileSystem.copy(FileSystemTest.class.getResource("test2.txt"), testDir); 
//			File subdir = new File(testDir, "subdir"); 
//			subdir.mkdirs();
//			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), subdir); 
//			FileSystem.zipFile(testDir, testArchive);
//		}
//		
//		@Test
//		public void zipFileFile() throws IOException {
//			File testArchive = File.createTempFile("unittest", ".zip");  
//			testArchive.deleteOnExit();
//			
//			createZip(testArchive);
//			
//			try (ZipFile zipFile = new ZipFile(testArchive)) {
//	
//				ZipEntry zipEntry = zipFile.getEntry("test.txt"); 
//				assertNotNull(zipEntry);
//				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); 
//		
//				zipEntry = zipFile.getEntry("test2.txt"); 
//				assertNotNull(zipEntry);
//				assertEquals("TEST2: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); 
//				
//				zipEntry = zipFile.getEntry("subdir/test.txt"); 
//				assertNotNull(zipEntry);
//				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); 
//			}
//		}
//	
//		@Test
//		public void testUnzipFileFile() throws IOException {
//			File testArchive = File.createTempFile("unittest", ".zip");  
//			testArchive.deleteOnExit();
//			createZip(testArchive);
//	
//			File testDir = FileSystem.createTempDirectory("unittest", null); 
//			FileSystem.deleteOnExit(testDir);
//			File subDir = new File(testDir, "subdir"); 
//			
//			FileSystem.unzipFile(testArchive, testDir);
//			
//			assertTrue(testDir.isDirectory());
//			assertTrue(subDir.isDirectory());
//			
//			String txt;
//			
//			File file = new File(testDir, "test.txt"); 
//			try (FileInputStream fis = new FileInputStream(file)) {
//				txt = readInputStream(fis);
//			}
//			assertEquals("TEST1: FOR UNIT TEST ONLY", txt); 
//			
//			file = new File(testDir, "test2.txt"); 
//			try (FileInputStream fis = new FileInputStream(file)) {
//				txt = readInputStream(fis);
//			}
//			assertEquals("TEST2: FOR UNIT TEST ONLY", txt); 
//	
//			file = new File(subDir, "test.txt"); 
//			try (FileInputStream fis = new FileInputStream(file)) {
//				txt = readInputStream(fis);
//			}
//			assertEquals("TEST1: FOR UNIT TEST ONLY", txt); 
//		}
//	
//		@Test
//		public void getFileExtensionCharacter() {
//			assertInlineParameterUsage(FileSystem.class, "getFileExtensionCharacter");
//		}
		
	}

	public static class UnixFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public UnixFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected File createAbsoluteStandardFile() {
			return new File("/home/test.x.z.z");
		}

		@Override
		protected File createAbsoluteFolderFile() {
			return new File("/home");
		}

		@Override
		protected File createStandardFileWithSpaces() {
			return new File("/the path/to/file with space.toto");
		}

	}
	
	public static class WindowsFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public WindowsFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected File createAbsoluteStandardFile() {
			return new File("C:\\home\\test.x.z.z");
		}

		@Override
		protected File createAbsoluteFolderFile() {
			return new File("C:\\home");
		}

		@Override
		protected File createStandardFileWithSpaces() {
			return new File("C:\\the path\\to\\file with space.toto");
		}

	}

	public static class OsXFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public OsXFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected File createAbsoluteStandardFile() {
			return new File("/Users/test.x.z.z");
		}

		@Override
		protected File createAbsoluteFolderFile() {
			return new File("/Users");
		}

		@Override
		protected File createStandardFileWithSpaces() {
			return new File("/the path/to/file with space.toto");
		}

	}

}

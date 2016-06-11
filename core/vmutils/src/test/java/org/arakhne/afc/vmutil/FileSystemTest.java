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
import static org.junit.Assert.*;
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
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
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

@SuppressWarnings("all")
public class FileSystemTest {

	private boolean oldLibraryLoaderState;

	public static void assertEquals(Object a, Object b) {
		if (!Objects.equals(a, b)) {
			throw new ComparisonFailure("not equal", Objects.toString(a), Objects.toString(b));
		}
	}

	public static String getCurrentDir() throws IOException {
		return new File(".").getCanonicalPath();
	}

	/** Replace file separator by "/"
	 *
	 * @param filename
	 * @return
	 */
	public static String fromFileToUrl(String filename, boolean removeStartSlash) {
		String result = filename.replaceAll("[/\\\\]", Matcher.quoteReplacement("/"));
		if (removeStartSlash) {
			if (result.startsWith("/")) {
				result = result.substring(1);
			}
		} else {
			if (!result.startsWith("/")) {
				result = "/" + result;
			}
		}
		return result;
	}

	/** Replace "/" by the file separator.
	 *
	 * @param filename
	 * @return
	 */
	public static File normFile(String filename) {
		return new File(fromUrlToFile(filename));
	}

	public static void assertNormedFile(String expected, File actual) {
		assertEquals(normFile(expected), actual);
	}

	/** Replace "/" by the file separator.
	 *
	 * @param filename
	 * @return
	 */
	public static String fromUrlToFile(String filename) {
		return filename.replaceAll(Pattern.quote("/"), Matcher.quoteReplacement(File.separator));
	}

	/** Remove root slash
	 */
	public static String removeRootSlash(String filename) {
		if (filename != null && filename.startsWith("/")) {
			return filename.substring(1);
		}
		return filename;
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
	public void makeAbsoluteFileURL_httpAsRoot() throws Exception {
		final URL root = new URL("http://maven.arakhne.org/myroot"); 

		assertNull(FileSystem.makeAbsolute((File)null, root));

		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"),  
				FileSystem.makeAbsolute(new File("a" + File.separator + "b" + File.separator + "c"),
						createFileInJarUrlWithSpaces()));  
	}

	@Test
	public void makeRelativeFileFile() throws Exception {
		File root, abs, rel;

		root = FileSystem.getUserHomeDirectory();

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b");  
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");  
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b"); 
		rel = new File("a","b"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc");  

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc"); 

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	public void makeRelativeFileURL() throws Exception {
		File abs, rel;
		URL root;

		root = FileSystem.getUserHomeDirectory().toURI().toURL();

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b");  
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");  
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b"); 
		rel = new File("a","b"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");  

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); 

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	public void convertFileToURLFile() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		try {
			File f1 = new File("/toto"); 
			URL u1 = f1.toURI().toURL();
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); 
			URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt"); 
			File f2 = FileSystem.convertURLToFile(u2);

			URL actual;

			assertEquals(u1, FileSystem.convertFileToURL(f1));

			assertEquals(u2e, FileSystem.convertFileToURL(f2));
		}
		finally {
			URLHandlerUtil.uninstallArakhneHandlers();
		}
	}

	@Test
	public void getParentURLURL() throws Exception {
		assertEquals(
				new URL("http://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("http://www.arakhne.org"))); 
		assertEquals(
				new URL("http://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("http://www.arakhne.org/"))); 
		assertEquals(
				new URL("http://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto"))); 
		assertEquals(
				new URL("http://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/"))); 
		assertEquals(
				new URL("http://www.arakhne.org/toto/"), 
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi"))); 
		assertEquals(
				new URL("http://www.arakhne.org/toto/"), 
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi/"))); 

		assertEquals(
				new URL("https://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("https://www.arakhne.org"))); 
		assertEquals(
				new URL("https://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("https://www.arakhne.org/"))); 
		assertEquals(
				new URL("https://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto"))); 
		assertEquals(
				new URL("https://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/"))); 
		assertEquals(
				new URL("https://www.arakhne.org/toto/"), 
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi"))); 
		assertEquals(
				new URL("https://www.arakhne.org/toto/"), 
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi/"))); 

		assertEquals(
				new URL("ftp://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org"))); 
		assertEquals(
				new URL("ftp://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/"))); 
		assertEquals(
				new URL("ftp://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto"))); 
		assertEquals(
				new URL("ftp://www.arakhne.org/"), 
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/"))); 
		assertEquals(
				new URL("ftp://www.arakhne.org/toto/"), 
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi"))); 
		assertEquals(
				new URL("ftp://www.arakhne.org/toto/"), 
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi/"))); 

		assertEquals(
				new URL("file:/toto/"), 
				FileSystem.getParentURL(new URL("file:/toto/titi"))); 
		assertEquals(
				new URL("file:/toto/"), 
				FileSystem.getParentURL(new URL("file:/toto/titi/"))); 
		assertEquals(
				new URL("file:/"), 
				FileSystem.getParentURL(new URL("file:/toto"))); 
		assertEquals(
				new URL("file:/"), 
				FileSystem.getParentURL(new URL("file:/toto/"))); 
		assertEquals(
				new URL("file:./toto/"), 
				FileSystem.getParentURL(new URL("file:./toto/titi"))); 
		assertEquals(
				new URL("file:./toto/"), 
				FileSystem.getParentURL(new URL("file:./toto/titi/"))); 
		assertEquals(
				new URL("file:./"), 
				FileSystem.getParentURL(new URL("file:./toto"))); 
		assertEquals(
				new URL("file:./"), 
				FileSystem.getParentURL(new URL("file:./toto/"))); 
		assertEquals(
				new URL("file:../"), 
				FileSystem.getParentURL(new URL("file:."))); 
		assertEquals(
				new URL("file:../"), 
				FileSystem.getParentURL(new URL("file:./"))); 
		assertEquals(
				new URL("file:../"), 
				FileSystem.getParentURL(new URL("file:toto"))); 
		assertEquals(
				new URL("file:../"), 
				FileSystem.getParentURL(new URL("file:toto/"))); 

		assertEquals(
				new URL("jar:file:test.jar!/toto/"), 
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi"))); 
		assertEquals(
				new URL("jar:file:test.jar!/toto/"), 
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi/"))); 
		assertEquals(
				new URL("jar:file:test.jar!/"), 
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto"))); 
		assertEquals(
				new URL("jar:file:test.jar!/"), 
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/"))); 
		assertEquals(
				new URL("jar:file:test.jar!/"), 
				FileSystem.getParentURL(new URL("jar:file:test.jar!/"))); 

		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"),  
				FileSystem.getParentURL(createFileInJarUrlWithSpaces()));
	}

	@Test
	public void makeAbsoluteFileURL_noRoot() throws Exception {
		assertNull(FileSystem.makeAbsolute((File)null, (URL)null));

		assertEquals(new URL("file:" + fromFileToUrl(getCurrentDir(), false) + "/toto"), 
				FileSystem.makeAbsolute(new File("toto"), (URL)null)); 
	}

	@Test
	public void makeAbsoluteURLFile_noRoot() throws Exception {
		assertNull(FileSystem.makeAbsolute((URL)null, (File)null));

		assertEquals(new URL("file:/toto"), 
				FileSystem.makeAbsolute(new URL("file:/toto"), (File)null)); 
		assertEquals(new URL("file:toto"), 
				FileSystem.makeAbsolute(new URL("file:toto"), (File)null)); 

		assertEquals(new URL("http://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (File)null)); 
		assertEquals(new URL("http://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (File)null)); 

		assertEquals(new URL("https://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (File)null)); 
		assertEquals(new URL("https://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (File)null)); 

		assertEquals(new URL("ftp://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (File)null)); 
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (File)null)); 

		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); 
		assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); 
	}

	@Test
	public void makeAbsoluteURLFile_root() throws Exception {
		File root = new File(File.separator+"myroot"); 

		assertEquals(new URL("http://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("http://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); 

		assertEquals(new URL("https://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("https://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); 

		assertEquals(new URL("ftp://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); 
	}

	@Test
	public void makeAbsoluteURLURL_notroot() throws Exception {
		assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));

		assertEquals(new URL("file:/toto"), 
				FileSystem.makeAbsolute(new URL("file:/toto"), (URL)null)); 
		assertEquals(new URL("file:toto"), 
				FileSystem.makeAbsolute(new URL("file:toto"), (URL)null)); 

		assertEquals(new URL("http://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (URL)null)); 
		assertEquals(new URL("http://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (URL)null)); 

		assertEquals(new URL("https://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (URL)null)); 
		assertEquals(new URL("https://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (URL)null)); 

		assertEquals(new URL("ftp://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (URL)null)); 
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (URL)null)); 

		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); 
		assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); 
	}

	@Test
	public void makeAbsoluteURLURL_fileAsRoot() throws Exception {
		URL root = new File(File.separator+"myroot").toURI().toURL(); 

		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(new URL("http://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("http://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); 

		assertEquals(new URL("https://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("https://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); 

		assertEquals(new URL("ftp://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); 
	}

	@Test
	public void makeAbsoluteURLURL_httpAsRoot() throws Exception {
		URL root = new URL("http://maven.arakhne.org"); 

		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(new URL("http://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("http://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root)); 

		assertEquals(new URL("https://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("https://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root)); 

		assertEquals(new URL("ftp://www.arakhne.org/toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root)); 
		assertEquals(new URL("ftp://www.arakhne.org/./toto"), 
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root)); 

		assertEquals(new URL("jar:http://maven.arakhne.org/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), 
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
	}

	@Test
	public void makeAbsoluteFileFile_noRoot() {
		assertNull(FileSystem.makeAbsolute((File)null, (File)null));

		assertEquals(new File(File.separator+"toto"), 
				FileSystem.makeAbsolute(new File(File.separator+"toto"), (File)null)); 
		assertEquals(new File("toto"), 
				FileSystem.makeAbsolute(new File("toto"), (File)null)); 
	}

	@Test
	public void makeAbsoluteFileFile_root() {
		File root = new File(File.separator+"myroot"); 

		assertNull(FileSystem.makeAbsolute((File)null, root));
	}

	@Test
	public void jreBehaviorRelatedToURL() throws Exception {
		// The following test permits to check if a specifical behaviour of URL
		// is still present in the JRE.
		URL rr = new URL("file://marbre.jpg"); 
		assertEquals("file", rr.getProtocol()); 
		assertEquals("marbre.jpg", rr.getAuthority()); 
		assertEquals("", rr.getPath()); 
	}

	@Test
	public void convertURLToFile() throws Exception {
		try {
			FileSystem.convertURLToFile(new URL("http://www.arakhne.org")); 
			fail("not a file URL"); 
		}
		catch(IllegalArgumentException exception) {
			//
		}

		assertEquals(new File("toto").getCanonicalPath(), 
				FileSystem.convertURLToFile(new URL("file:./toto")).getCanonicalPath()); 

		assertEquals(new File("toto").getCanonicalPath(), 
				FileSystem.convertURLToFile(new URL("file:toto")).getCanonicalPath()); 

		assertEquals(new File("toto").getCanonicalPath(), 
				FileSystem.convertURLToFile(new URL("file:./abs/../toto")).getCanonicalPath()); 

		assertEquals(new File("/toto").getCanonicalPath(), 
				FileSystem.convertURLToFile(new URL("file:/toto")).getCanonicalPath()); 
	}

	@Test
	public void convertStringToURL() throws Exception {
		assertNull(FileSystem.convertStringToURL(null, true));
		assertNull(FileSystem.convertStringToURL("", true)); 
		assertNull(FileSystem.convertStringToURL(null, false));
		assertNull(FileSystem.convertStringToURL("", false)); 

		URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); 
		assertNotNull(rr);
		assertEquals("file", rr.getProtocol()); 
		assertEquals("", rr.getAuthority());
		assertEquals("", rr.getHost()); 
		assertNull(rr.getQuery());
		assertEquals("marbre.jpg", rr.getPath()); 

		assertEquals(new URL("http", "www.arakhne.org", "/"),   
				FileSystem.convertStringToURL("http://www.arakhne.org/", true)); 
		assertEquals(new URL("http", "www.arakhne.org", "/"),   
				FileSystem.convertStringToURL("http://www.arakhne.org/", false)); 

		// CAUTION: testing right-formed jar URL.
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
				FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); 
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
				FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); 

		// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
		assertEquals(new URL("file", "", "/home/test/j.jar"),   
				FileSystem.convertStringToURL("jar:/home/test/j.jar", true)); 
		assertEquals(new URL("file", "", "/home/test/j.jar"),   
				FileSystem.convertStringToURL("jar:/home/test/j.jar", false)); 

		// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
				FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); 
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),   
				FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); 

		URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); 
		assertNotNull(testResource);

		assertEquals(testResource,
				FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", true)); 
		assertEquals(null,
				FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", false)); 

		assertEquals(testResource,
				FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", true)); 
		assertEquals(null,
				FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", false)); 

		assertEquals(testResource,
				FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", true)); 
		assertEquals(new URL("file", "", "/org/arakhne/afc/vmutil/test.txt"),
				FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", false)); 

		assertEquals(testResource,
				FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", true)); 
		assertEquals(new URL("file","", "org/arakhne/afc/vmutil/test.txt"),
				FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", false)); 
	}

	@Test
	public void joinURLStringArray() throws Exception {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"), FileSystem.join(createHttpUrl(), "a", "b", "c")); 
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrl(), "a", "b", "c")); 
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarInJarUrl(), "a", "b", "c")); 
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"), FileSystem.join(createFileUrlWithSpacesHardCoded(), "a", "b", "c")); 
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrlWithSpaces(), "a", "b", "c")); 
	}

	@Test
	public void joinURLFileArray() throws Exception {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"),
				FileSystem.join(createHttpUrl(), new File("a"), new File("b"), new File("c"))); 
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"),
				FileSystem.join(createFileInJarUrl(), new File("a"), new File("b"), new File("c"))); 
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"),
				FileSystem.join(createFileInJarInJarUrl(), new File("a"), new File("b"), new File("c"))); 
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"),
				FileSystem.join(createFileUrlWithSpacesHardCoded(), new File("a"), new File("b"), new File("c"))); 
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"),
				FileSystem.join(createFileInJarUrlWithSpaces(), new File("a"), new File("b"), new File("c"))); 
	}

	@Test
	public void splitURL() throws Exception {
		assertArrayEquals(new String[] {"", "path", "to", "file.x.z.z"}, FileSystem.split(createHttpUrl())); 
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrl())); 
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarInJarUrl())); 
		assertArrayEquals(new String[] {"", "the path", "to", "file with space.toto"}, FileSystem.split(createFileUrlWithSpacesHardCoded())); 
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrlWithSpaces())); 
		assertArrayEquals(new String[] {"", "a.b.c"}, FileSystem.split(new URL("file:///a.b.c/")));  
		assertArrayEquals(new String[] {""}, FileSystem.split(new URL("file://")));  
	}

	@Test
	public void extensionsURL() throws MalformedURLException {
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createHttpUrl())); 
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrl())); 
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarInJarUrl())); 
		assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesHardCoded())); 
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrlWithSpaces())); 
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions(new URL("file:///a.b.c/")));  
		assertArrayEquals(new String[0], FileSystem.extensions(new URL("file://")));  
	}

	@Test
	public void extensionsString() {
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); 
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:///a.b.c/"));  
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:a.b.c"));  
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("a.b.c"));  
		assertArrayEquals(new String[0], FileSystem.extensions("file://"));  

		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
	}

	@Test
	public void extensionURL() throws MalformedURLException {
		assertEquals(".z", FileSystem.extension(createHttpUrl())); 
		assertEquals(".z", FileSystem.extension(createFileInJarUrl())); 
		assertEquals(".z", FileSystem.extension(createFileInJarInJarUrl())); 
		assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesHardCoded())); 
		assertEquals(".z", FileSystem.extension(createFileInJarUrlWithSpaces())); 
		assertEquals(".c", FileSystem.extension(new URL("file:///a.b.c/")));  
		assertEquals("", FileSystem.extension(new URL("file://")));  
	}

	@Test
	public void extensionString() {
		assertEquals(".z", FileSystem.extension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); 
		assertEquals(".z", FileSystem.extension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals(".z", FileSystem.extension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals(".c", FileSystem.extension("file:///a.b.c/"));  
		assertEquals(".c", FileSystem.extension("file:a.b.c"));  
		assertEquals(".c", FileSystem.extension("a.b.c"));  
		assertEquals("", FileSystem.extension("file://"));  

		assertEquals(".dae", FileSystem.extension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals(".dae", FileSystem.extension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals(".dae", FileSystem.extension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
	}

	@Test
	public void hasExtensionURL() throws MalformedURLException {
		assertTrue(FileSystem.hasExtension(createHttpUrl(), ".z")); 
		assertTrue(FileSystem.hasExtension(createHttpUrl(), "z")); 
		assertFalse(FileSystem.hasExtension(createHttpUrl(), ".c")); 
		assertTrue(FileSystem.hasExtension(createFileInJarUrl(), ".z")); 
		assertTrue(FileSystem.hasExtension(createFileInJarUrl(), "z")); 
		assertFalse(FileSystem.hasExtension(createFileInJarUrl(), ".c")); 
		assertTrue(FileSystem.hasExtension(createFileInJarInJarUrl(), ".z")); 
		assertTrue(FileSystem.hasExtension(createFileInJarInJarUrl(), "z")); 
		assertFalse(FileSystem.hasExtension(createFileInJarInJarUrl(), ".c")); 
		assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".toto")); 
		assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), "toto")); 
		assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".zip")); 
		assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".z")); 
		assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), "z")); 
		assertFalse(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".c")); 
		assertTrue(FileSystem.hasExtension(new URL("file:///a.b.c/"), ".c"));  
		assertTrue(FileSystem.hasExtension(new URL("file:///a.b.c/"), "c"));  
		assertFalse(FileSystem.hasExtension(new URL("file:///a.b.c/"), ".zip"));  
		assertFalse(FileSystem.hasExtension(new URL("file://"), ".c"));  
	}

	@Test
	public void hasExtensionString() {
		assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".z")); 
		assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", "z")); 
		assertFalse(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".c")); 
		assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); 
		assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); 
		assertFalse(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); 
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); 
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); 
		assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); 
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); 
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); 
		assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); 
		assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); 
		assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", "z")); 
		assertFalse(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); 
		assertTrue(FileSystem.hasExtension("file:///a.b.c/", ".c"));  
		assertTrue(FileSystem.hasExtension("file:///a.b.c/", "c"));  
		assertFalse(FileSystem.hasExtension("file:///a.b.c/", ".z"));  
		assertTrue(FileSystem.hasExtension("file:a.b.c", ".c"));  
		assertTrue(FileSystem.hasExtension("file:a.b.c", "c"));  
		assertFalse(FileSystem.hasExtension("file:a.b.c", ".z"));  
		assertTrue(FileSystem.hasExtension("a.b.c", ".c"));  
		assertTrue(FileSystem.hasExtension("a.b.c", "c"));  
		assertFalse(FileSystem.hasExtension("a.b.c", ".z"));  
		assertFalse(FileSystem.hasExtension("file://", ".z"));  

		assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae"));  
		assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae"));  
		assertFalse(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip"));  
		assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae"));  
		assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae"));  
		assertFalse(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip"));  
		assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae"));  
		assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae"));  
		assertFalse(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip"));  
	}

	@Test
	public void removeExtensionURL() throws MalformedURLException {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z?toto#frag"), FileSystem.removeExtension(createHttpUrl())); 
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrl())); 
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarInJarUrl())); 
		assertEquals(new URL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesHardCoded())); 
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrlWithSpaces())); 
		assertEquals(new URL("file:///a.b"), FileSystem.removeExtension(new URL("file:///a.b.c/")));  
		assertEquals(new URL("file", "", ""), FileSystem.removeExtension(new URL("file://")));  
	}

	@Test
	public void replaceExtensionURLString() throws MalformedURLException {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.xyz?toto#frag"), FileSystem.replaceExtension(createHttpUrl(), ".xyz")); 
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrl(), ".xyz")); 
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarInJarUrl(), ".xyz")); 
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesHardCoded(), ".xyz")); 
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrlWithSpaces(), ".xyz")); 
		assertEquals(new URL("file:///a.b.xyz"), FileSystem.replaceExtension(new URL("file:///a.b.c/"), ".xyz"));  
		assertEquals(new URL("file", "", ""), FileSystem.replaceExtension(new URL("file://"), ".xyz"));  
	}

	@Test
	public void addExtensionURLString() throws MalformedURLException {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z.xyz?toto#frag"), FileSystem.addExtension(createHttpUrl(), ".xyz")); 
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrl(), ".xyz")); 
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarInJarUrl(), ".xyz")); 
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesHardCoded(), ".xyz")); 
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrlWithSpaces(), ".xyz")); 
		assertEquals(new URL("file:///a.b.c.xyz"), FileSystem.addExtension(new URL("file:///a.b.c/"), ".xyz"));  
		assertEquals(new URL("file", "", ""), FileSystem.addExtension(new URL("file://"), ".xyz"));  
	}

	@Test
	public void basenameURL() throws MalformedURLException {
		assertEquals("file.x.z", FileSystem.basename(createHttpUrl())); 
		assertEquals("file.x.z", FileSystem.basename(createFileInJarUrl())); 
		assertEquals("file.x.z", FileSystem.basename(createFileInJarInJarUrl())); 
		assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesHardCoded())); 
		assertEquals("file.x.z", FileSystem.basename(createFileInJarUrlWithSpaces())); 
		assertEquals("a.b", FileSystem.basename(new URL("file:///a.b.c/")));  
		assertEquals("", FileSystem.basename(new URL("file://")));  

		URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae");   
		try {
			assertEquals("terrain_physx", FileSystem.basename(url)); 
			fail("expecting assertion failure"); 
		}
		catch(AssertionError exception) {
			//
		}
	}

	@Test
	public void basenameString() {
		assertEquals("file.x.z", FileSystem.basename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); 
		assertEquals("file.x.z", FileSystem.basename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file.x.z", FileSystem.basename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("a.b", FileSystem.basename("file:///a.b.c/"));  
		assertEquals("a.b", FileSystem.basename("file:a.b.c"));  
		assertEquals("a.b", FileSystem.basename("a.b.c"));  
		assertEquals("", FileSystem.basename("file://"));  

		assertEquals("terrain_physx", FileSystem.basename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals("terrain_physx", FileSystem.basename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals("terrain_physx", FileSystem.basename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
	}

	@Test
	public void largeBasenameURL() throws MalformedURLException {
		assertEquals("file.x.z.z", FileSystem.largeBasename(createHttpUrl())); 
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrl())); 
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarInJarUrl())); 
		assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesHardCoded())); 
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrlWithSpaces())); 
		assertEquals("a.b.c", FileSystem.largeBasename(new URL("file:///a.b.c/")));  
		assertEquals("", FileSystem.largeBasename(new URL("file://")));  
	}

	@Test
	public void largeBasenameString() {
		assertEquals("file.x.z.z", FileSystem.largeBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); 
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("a.b.c", FileSystem.largeBasename("file:///a.b.c/"));  
		assertEquals("a.b.c", FileSystem.largeBasename("file:a.b.c"));  
		assertEquals("a.b.c", FileSystem.largeBasename("a.b.c"));  
		assertEquals("", FileSystem.largeBasename("file://"));  

		assertEquals("terrain_physx.dae", FileSystem.largeBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
	}

	@Test
	public void shortBasenameURL() throws MalformedURLException {
		assertEquals("file", FileSystem.shortBasename(createHttpUrl())); 
		assertEquals("file", FileSystem.shortBasename(createFileInJarUrl())); 
		assertEquals("file", FileSystem.shortBasename(createFileInJarInJarUrl())); 
		assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesHardCoded())); 
		assertEquals("file", FileSystem.shortBasename(createFileInJarUrlWithSpaces())); 
		assertEquals("a", FileSystem.shortBasename(new URL("file:///a.b.c/")));  
		assertEquals("", FileSystem.shortBasename(new URL("file://")));  
	}

	@Test
	public void shortBasenameString() {
		assertEquals("file", FileSystem.shortBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); 
		assertEquals("file", FileSystem.shortBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("file", FileSystem.shortBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); 
		assertEquals("a", FileSystem.shortBasename("file:///a.b.c/"));  
		assertEquals("a", FileSystem.shortBasename("file:a.b.c"));  
		assertEquals("a", FileSystem.shortBasename("a.b.c"));  
		assertEquals("", FileSystem.shortBasename("file://"));  

		assertEquals("terrain_physx", FileSystem.shortBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals("terrain_physx", FileSystem.shortBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
		assertEquals("terrain_physx", FileSystem.shortBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  
	}

	@Test
	public void toShortestURLURL() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		try {
			File f1 = new File("/toto"); 
			URL u1 = f1.toURI().toURL();
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); 
			URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt"); 

			URL actual;

			actual = FileSystem.toShortestURL(u1);
			assertEquals(u1, actual);

			actual = FileSystem.toShortestURL(u2);
			assertEquals(u2e, actual);
		}
		finally {
			URLHandlerUtil.uninstallArakhneHandlers();
		}
	}


	@Test
	public void makeRelativeURLURL() throws Exception {
		File rel;
		URL root, abs;

		root = FileSystem.getUserHomeDirectory().toURI().toURL();

		abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b").toURI().toURL();  
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");  
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");  

		abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); 

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc").toURI().toURL(); 
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); 
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	public void makeCanonicalURL() throws MalformedURLException {
		assertEquals(
				createHttpUrl(), 
				FileSystem.makeCanonicalURL(createHttpUrl()));

		assertEquals(
				createFileInJarUrl(), 
				FileSystem.makeCanonicalURL(createFileInJarUrl()));

		assertEquals(
				createFileInJarInJarUrl(), 
				FileSystem.makeCanonicalURL(createFileInJarInJarUrl()));

		assertEquals(
				new URL("file:/a/b/c/d/e"), 
				FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/./d/e"))); 

		assertEquals(
				new URL("file:/a/d/e"), 
				FileSystem.makeCanonicalURL(new URL("file:/a/b/../c/../d/e"))); 

		assertEquals(
				new URL("file:/a/b/d/e"), 
				FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/../d/e"))); 

		assertEquals(
				new URL("file:../a/b/c/d/e"), 
				FileSystem.makeCanonicalURL(new URL("file:../a/b/./c/./d/e"))); 

		assertEquals(
				new URL("file:../a/c/d/e"), 
				FileSystem.makeCanonicalURL(new URL("file:../a/b/../c/./d/e"))); 
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
		File testDir = null;
		try {
			testDir = FileSystem.createTempDirectory("unittest", null); 
			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), testDir); 
			FileSystem.copy(FileSystemTest.class.getResource("test2.txt"), testDir); 
			File subdir = new File(testDir, "subdir"); 
			subdir.mkdirs();
			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), subdir); 
			FileSystem.zipFile(testDir, testArchive);
		} finally {
			FileSystem.delete(testDir);
		}
	}

	@Test
	public void zipFileFile() throws IOException {
		File testArchive = null;

		try {
			testArchive = File.createTempFile("unittest", ".zip");  

			createZip(testArchive);

			try (ZipFile zipFile = new ZipFile(testArchive)) {

				ZipEntry zipEntry = zipFile.getEntry("test.txt"); 
				assertNotNull(zipEntry);
				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); 

				zipEntry = zipFile.getEntry("test2.txt"); 
				assertNotNull(zipEntry);
				assertEquals("TEST2: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); 

				zipEntry = zipFile.getEntry("subdir/test.txt"); 
				assertNotNull(zipEntry);
				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); 
			}
		} finally {
			FileSystem.delete(testArchive);
		}
	}

	@Test
	public void testUnzipFileFile() throws IOException {
		File testArchive = null;
		try {
			testArchive = File.createTempFile("unittest", ".zip");  

			createZip(testArchive);

			File testDir = FileSystem.createTempDirectory("unittest", null); 
			FileSystem.deleteOnExit(testDir);
			File subDir = new File(testDir, "subdir"); 

			FileSystem.unzipFile(testArchive, testDir);

			assertTrue(testDir.isDirectory());
			assertTrue(subDir.isDirectory());

			String txt;

			File file = new File(testDir, "test.txt"); 
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST1: FOR UNIT TEST ONLY", txt); 

			file = new File(testDir, "test2.txt"); 
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST2: FOR UNIT TEST ONLY", txt); 

			file = new File(subDir, "test.txt"); 
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST1: FOR UNIT TEST ONLY", txt);
		} finally {
			FileSystem.delete(testArchive);
		}
	}

	@Test
	public void getFileExtensionCharacter() {
		assertInlineParameterUsage(FileSystem.class, "getFileExtensionCharacter");
	}

	@Test
	public void isWindowNativeFilename() {
		assertFalse(FileSystem.isWindowsNativeFilename("D:/vivus_test/export dae/yup/terrain_physx.dae")); 
		assertTrue(FileSystem.isWindowsNativeFilename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
		assertTrue(FileSystem.isWindowsNativeFilename("D|\\vivus_test\\export dae\\yup\\terrain_physx.dae")); 
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
		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:\\a\\b\\c.txt")); 
		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:\\a\\b\\c.txt")); 
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:a\\b\\c.txt")); 
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); 
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\a\\b\\c.txt")); 
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\a\\b\\c.txt")); 
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:a\\b\\c.txt")); 
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://a\\b\\c.txt")); 
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); 
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); 

		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:\\a\\b\\c.txt")); 
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:a\\b\\c.txt")); 
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); 
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\a\\b\\c.txt")); 
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("a\\b\\c.txt")); 
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); 

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

		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("C:c.txt")); 
		assertNull(FileSystem.normalizeWindowsNativeFilename("c.txt")); 
		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:c.txt")); 
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:c.txt")); 
		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:c.txt")); 
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://c.txt")); 
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
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:/C:/a/b/c.txt")); 
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:/a/b/c.txt")); 
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a/b/c.txt")); 
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:/C:a/b/c.txt")); 
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

	@Test
	public void isJarURLURL() throws Exception {
		assertFalse(FileSystem.isJarURL(createHttpUrl()));
		assertTrue(FileSystem.isJarURL(createFileInJarUrl()));
		assertTrue(FileSystem.isJarURL(createFileInJarInJarUrl()));
		assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesHardCoded()));  

		assertInlineParameterUsage(FileSystem.class, "isJarURL", URL.class);
	}

	@Test
	public void getJarURLURL() throws Exception {
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
	public void getJarFileURL() throws Exception {
		assertNull(FileSystem.getJarFile(createHttpUrl()));
		assertNormedFile(createInJarFilename(), FileSystem.getJarFile(createFileInJarUrl()));
		assertNormedFile(createInJarFilename(), FileSystem.getJarFile(createFileInJarInJarUrl()));

		assertNormedFile(createInJarFilename(), FileSystem.getJarFile(createFileInJarUrlWithSpaces()));  
		assertNull(FileSystem.getJarFile(createFileUrlWithSpacesHardCoded()));
	}

	@Test
	public void dirnameURL() throws Exception {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/"),
				FileSystem.dirname(createHttpUrl()));
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/"),
				FileSystem.dirname(createFileInJarUrl()));
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/"),
				FileSystem.dirname(createFileInJarInJarUrl()));
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"),
				FileSystem.dirname(createFileInJarUrlWithSpaces()));
	}

	private static abstract class AbstractFileSystemTest {

		private boolean oldLibraryLoaderState;

		protected abstract OperatingSystem getOS();

		protected abstract String getSeparator();

		/** Create a file.
		 */
		public File newFile(String filename, boolean addRootSlash) {
			String fn;

			if (OperatingSystem.getCurrentOS() == getOS()) {
				fn = filename;
			} else if (getOS() == OperatingSystem.WIN) {
				fn = filename.replaceAll(Pattern.quote(FileSystem.WINDOWS_SEPARATOR_STRING),
						Matcher.quoteReplacement(File.separator));
			} else {
				fn = filename.replaceAll(Pattern.quote(FileSystem.UNIX_SEPARATOR_STRING),
						Matcher.quoteReplacement(File.separator));
			}
			if (addRootSlash && !fn.startsWith(File.separator)) {
				fn = File.separator + fn;
			}
			return new File(fn);
		}

		/** @return "/home/test.x.z.z" or "C:\home\test.x.z.z" or "/home/test.x.z.z"
		 */
		protected abstract String getAbsoluteStandardFilename();

		/** @return "/home" or "C:\home" or "/home"
		 */
		protected abstract String getAbsoluteFoldername();

		/** @return "/" or "C:\" or "/"
		 */
		protected abstract String getRootnameWithSeparator();

		/** @return "" or "C:" or ""
		 */
		protected abstract String getRootnameWithoutSeparator();

		/** @return "/the path/to/file with space.toto" or "C:\the path\to\file with space.toto" or "/the path/to/file with space.toto"
		 */
		protected abstract String getStandardFilenameWithSpaces();

		/** @return "file:/home/test.x.z.z" or "file:C:\home\test.x.z.z" or "file:/home/test.x.z.z"
		 */
		protected URL createAbsoluteStandardFileUrl() throws MalformedURLException {
			return new URL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false));
		}

		/** @return "file:/home" or "file:C:\home" or "file:/home"
		 */
		protected URL createAbsoluteFolderUrl() throws MalformedURLException {
			return new URL("file:" + fromFileToUrl(getAbsoluteFoldername(), false));
		}

		/** @return "/the path/to/file with space.toto" or "C:\the path\to\file with space.toto" or "/the path/to/file with space.toto"
		 */
		protected URL createFileUrlWithSpacesWithFile() throws MalformedURLException {
			File file = newFile(getStandardFilenameWithSpaces(), false);
			return file.toURI().toURL();
		}

		@Test
		public void isJarURLURL() throws Exception {
			assertFalse(FileSystem.isJarURL(createAbsoluteStandardFileUrl()));
			assertFalse(FileSystem.isJarURL(createAbsoluteFolderUrl()));
			assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void getJarURLURL() throws Exception {
			assertNull(FileSystem.getJarURL(createAbsoluteStandardFileUrl()));
			assertNull(FileSystem.getJarURL(createAbsoluteFolderUrl()));
		}

		@Test
		public void getJarFileURL() throws Exception {
			assertNull(FileSystem.getJarFile(createAbsoluteStandardFileUrl()));
			assertNull(FileSystem.getJarFile(createAbsoluteFolderUrl()));
			assertNull(FileSystem.getJarFile(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void toJarURLFileString() throws MalformedURLException {
			assertEquals(new URL("jar:file:"
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/"
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							getAbsoluteStandardFilename()));
		}

		@Test
		public void toJarURLFileFile() throws MalformedURLException {
			assertEquals(new URL("jar:file:"
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/"
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							newFile(getAbsoluteStandardFilename(), true)));
		}

		@Test
		public void toJarURLURLString() throws MalformedURLException {
			assertEquals(new URL("jar:file:"
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/"
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							getAbsoluteStandardFilename()));
		}

		@Test
		public void toJarURLURLFile() throws MalformedURLException {
			assertEquals(new URL("jar:file:"
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/"
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							newFile(getAbsoluteStandardFilename(), true)));
		}

		@Test
		public void dirnameFile() throws Exception {
			assertEquals(new URL("file", "", fromFileToUrl(getAbsoluteFoldername(), false)),
					FileSystem.dirname(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals(new URL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)),
					FileSystem.dirname(newFile(getAbsoluteFoldername(), false)));
		}

		@Test
		public void dirnameURL() throws Exception {
			assertEquals(new URL("file", "", fromFileToUrl(getAbsoluteFoldername(), false) + "/"),
					FileSystem.dirname(createAbsoluteStandardFileUrl()));
			assertEquals(new URL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)),
					FileSystem.dirname(createAbsoluteFolderUrl()));
		}

		@Test
		public void splitFile() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(newFile(getAbsoluteStandardFilename(), false)));
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(newFile(getAbsoluteFoldername(), false)));
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "the path", "to", "file with space.toto"}, FileSystem.split(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void splitURL() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(createAbsoluteStandardFileUrl()));
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(createAbsoluteFolderUrl()));
		}

		@Test
		public void extensionsURL() throws MalformedURLException {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createAbsoluteStandardFileUrl()));
			assertArrayEquals(new String[0], FileSystem.extensions(createAbsoluteFolderUrl()));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void extensionsFile() throws MalformedURLException {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(newFile(getAbsoluteStandardFilename(), false)));
			assertArrayEquals(new String[0], FileSystem.extensions(newFile(getAbsoluteFoldername(), false)));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void extensionsString() throws MalformedURLException {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(getAbsoluteStandardFilename()));
			assertArrayEquals(new String[0], FileSystem.extensions(getAbsoluteFoldername()));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(getStandardFilenameWithSpaces()));
		}

		@Test
		public void extensionURL() throws MalformedURLException {
			assertEquals(".z", FileSystem.extension(createAbsoluteStandardFileUrl()));
			assertEquals("", FileSystem.extension(createAbsoluteFolderUrl()));
			assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void extensionFile() throws MalformedURLException {
			assertEquals(".z", FileSystem.extension(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals("", FileSystem.extension(newFile(getAbsoluteFoldername(), false)));
			assertEquals(".toto", FileSystem.extension(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void extensionString() throws MalformedURLException {
			assertEquals(".z", FileSystem.extension(getAbsoluteStandardFilename()));
			assertEquals("", FileSystem.extension(getAbsoluteFoldername()));
			assertEquals(".toto", FileSystem.extension(getStandardFilenameWithSpaces()));
		}

		@Test
		public void hasExtensionURL() throws MalformedURLException {
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".z"));
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), "z"));
			assertFalse(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".c"));
			assertFalse(FileSystem.hasExtension(createAbsoluteFolderUrl(), ".z"));
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(), ".toto"));
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(), "toto"));
			assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(), ".z"));
		}

		@Test
		public void hasExtensionFile() throws MalformedURLException {
			assertTrue(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), ".z"));
			assertTrue(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), "z"));
			assertFalse(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), ".c"));
			assertFalse(FileSystem.hasExtension(newFile(getAbsoluteFoldername(), false), ".z"));
			assertTrue(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), ".toto"));
			assertTrue(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), "toto"));
			assertFalse(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), ".z"));
		}

		@Test
		public void hasExtensionString() throws MalformedURLException {
			assertTrue(FileSystem.hasExtension(getAbsoluteStandardFilename(), ".z"));
			assertTrue(FileSystem.hasExtension(getAbsoluteStandardFilename(), "z"));
			assertFalse(FileSystem.hasExtension(getAbsoluteStandardFilename(), ".c"));
			assertFalse(FileSystem.hasExtension(getAbsoluteFoldername(), ".z"));
			assertTrue(FileSystem.hasExtension(getStandardFilenameWithSpaces(), ".toto"));
			assertTrue(FileSystem.hasExtension(getStandardFilenameWithSpaces(), "toto"));
			assertFalse(FileSystem.hasExtension(getStandardFilenameWithSpaces(), ".c"));
		}

		@Test
		public void basenameURL() throws MalformedURLException {
			assertEquals("test.x.z", FileSystem.basename(createAbsoluteStandardFileUrl()));
			assertEquals("home", FileSystem.basename(createAbsoluteFolderUrl()));
			assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void basenameFile() throws MalformedURLException {
			assertEquals("test.x.z", FileSystem.basename(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals("home", FileSystem.basename(newFile(getAbsoluteFoldername(), false)));
			assertEquals("file with space", FileSystem.basename(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void basenameString() throws MalformedURLException {
			assertEquals("test.x.z", FileSystem.basename(getAbsoluteStandardFilename()));
			assertEquals("home", FileSystem.basename(getAbsoluteFoldername()));
			assertEquals("file with space", FileSystem.basename(getStandardFilenameWithSpaces()));
		}

		@Test
		public void largeBasenameURL() throws MalformedURLException {
			assertEquals("test.x.z.z", FileSystem.largeBasename(createAbsoluteStandardFileUrl()));
			assertEquals("home", FileSystem.largeBasename(createAbsoluteFolderUrl()));
			assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void largeBasenameFile() throws MalformedURLException {
			assertEquals("test.x.z.z", FileSystem.largeBasename(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals("home", FileSystem.largeBasename(newFile(getAbsoluteFoldername(), false)));
			assertEquals("file with space.toto", FileSystem.largeBasename(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void largeBasenameString() throws MalformedURLException {
			assertEquals("test.x.z.z", FileSystem.largeBasename(getAbsoluteStandardFilename()));
			assertEquals("home", FileSystem.largeBasename(getAbsoluteFoldername()));
			assertEquals("file with space.toto", FileSystem.largeBasename(getStandardFilenameWithSpaces()));
		}

		@Test
		public void shortBasenameURL() throws MalformedURLException {
			assertEquals("test", FileSystem.shortBasename(createAbsoluteStandardFileUrl()));
			assertEquals("home", FileSystem.shortBasename(createAbsoluteFolderUrl()));
			assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void shortBasenameFile() throws MalformedURLException {
			assertEquals("test", FileSystem.shortBasename(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals("home", FileSystem.shortBasename(newFile(getAbsoluteFoldername(), false)));
			assertEquals("file with space", FileSystem.shortBasename(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void shortBasenameString() throws MalformedURLException {
			assertEquals("test", FileSystem.shortBasename(getAbsoluteStandardFilename()));
			assertEquals("home", FileSystem.shortBasename(getAbsoluteFoldername()));
			assertEquals("file with space", FileSystem.shortBasename(getStandardFilenameWithSpaces()));
		}

		@Test
		public void joinFileStringArray() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),  
					FileSystem.join(base,
							"", 
							"home", 
							"test.x.z.z")); 
		}


		@Test
		public void joinFileFileArray() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),  
					FileSystem.join(base,
							new File("home"), 
							new File("test.x.z.z"))); 
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),  
					FileSystem.join(base,
							new File(File.separator+"home"), 
							new File("test.x.z.z"))); 
		}

		@Test
		public void joinURLStringArray() throws Exception {
			URL base;

			base = new URL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c");
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), "a", "b", "c"));

			base = new URL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c");
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), "a", "b", "c"));

			base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", false).toURI().toURL();
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(), "a", "b", "c"));
		}

		@Test
		public void joinURLFileArray() throws Exception {
			URL base;

			base = new URL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c");
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), new File("a"), new File("b"), new File("c")));

			base = new URL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c");
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), new File("a"), new File("b"), new File("c")));

			base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", false).toURI().toURL();
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(), new File("a"), new File("b"), new File("c")));
		}

		@Test
		public void convertStringToURL() throws Exception {
			URL base;

			base = createAbsoluteStandardFileUrl();
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));

			base = createAbsoluteFolderUrl();
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));

			base = createFileUrlWithSpacesWithFile();
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));
		}

		@Test
		public void convertURLToFile() throws Exception {
			File base;

			base = newFile(getAbsoluteStandardFilename(), false);
			assertEquals(base, FileSystem.convertURLToFile(createAbsoluteStandardFileUrl()));

			base = newFile(getAbsoluteFoldername(), false);
			assertEquals(base, FileSystem.convertURLToFile(createAbsoluteFolderUrl()));
		}

		@Test
		public void convertFileToURLFile() throws Exception {
			assertEquals(createAbsoluteStandardFileUrl(),  
					FileSystem.convertFileToURL(newFile(getAbsoluteStandardFilename(), true)));
			assertEquals(createAbsoluteFolderUrl(),
					FileSystem.convertFileToURL(newFile(getAbsoluteFoldername(), true)));
			assertEquals(createFileUrlWithSpacesWithFile(),  
					FileSystem.convertFileToURL(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void makeAbsoluteFileURL_noRoot() throws Exception {
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator(), false) + "toto"), 
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), (URL)null)); 
		}

		@Test
		public void makeAbsoluteFileURL_httpAsRoot() throws Exception {
			final URL root = new URL("http://maven.arakhne.org/myroot"); 

			assertEquals(new URL("http://maven.arakhne.org/myroot/toto"), 
					FileSystem.makeAbsolute(newFile("toto", false), root)); 
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), 
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root)); 

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "a/b/c", false)),  
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator()
							+ "a" + File.separator + "b" + File.separator + "c", true), root));  
		}

		@Test
		public void makeAbsoluteFileURL_fileAsRoot() throws Exception {
			URL root = new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); 

			assertNull(FileSystem.makeAbsolute((File)null, root));

			assertEquals(new URL("file:/toto"), 
					FileSystem.makeAbsolute(new File("/toto"), root)); 
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)), 
					FileSystem.makeAbsolute(new File("toto"), root)); 
		}

		@Test
		public void makeAbsoluteURLURL_httpAsRoot() throws Exception {
			final URL root = new URL("http://maven.arakhne.org/myroot"); 

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), 
					FileSystem.makeAbsolute(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); 
			assertEquals(new URL("http://maven.arakhne.org/myroot/toto"), 
					FileSystem.makeAbsolute(new URL("file:toto"), root)); 

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), 
					FileSystem.makeAbsolute(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); 
		}

		@Test
		public void makeAbsoluteURLURL_fileAsRoot() throws Exception {
			URL root = new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); 

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), 
					FileSystem.makeAbsolute(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); 
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)), 
					FileSystem.makeAbsolute(new URL("file:toto"), root)); 

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), 
					FileSystem.makeAbsolute(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root));

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), 
					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
		}

		@Test
		public void makeAbsoluteFileFile_root() {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); 

			assertEquals(newFile(getRootnameWithSeparator() + "toto", true),  
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root)); 

			assertEquals(newFile(getRootnameWithSeparator() + "myroot" + getSeparator() + "toto", true),  
					FileSystem.makeAbsolute(new File("toto"), root)); 
		}

		@Test
		public void makeAbsoluteURLFile_root() throws Exception {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); 

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), 
					FileSystem.makeAbsolute(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); 

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)), 
					FileSystem.makeAbsolute(new URL("file:toto"), root)); 

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), 
					FileSystem.makeAbsolute(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root));
			
			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator()
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), 
					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); 
		}

	}

	public static class UnixFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public UnixFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected String getAbsoluteStandardFilename() {
			return "/home/test.x.z.z";
		}

		@Override
		protected String getAbsoluteFoldername() {
			return "/home";
		}

		@Override
		protected String getRootnameWithSeparator() {
			return "/";
		}

		@Override
		protected String getRootnameWithoutSeparator() {
			return "";
		}

		@Override
		protected String getSeparator() {
			return "/";
		}

		@Override
		protected String getStandardFilenameWithSpaces() {
			return "/the path/to/file with space.toto";
		}

		@Override
		protected OperatingSystem getOS() {
			return OperatingSystem.LINUX;
		}

		@Test
		public void removeExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl()));
			assertEquals(new URL("file:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl()));
			assertEquals(new URL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void removeExtensionFile() throws MalformedURLException {
			assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals(new File("/home"), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false)));
			assertEquals(new File("/the path/to/file with space"), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void replaceExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz"));
			assertEquals(new URL("file:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz"));
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesWithFile(), ".xyz"));
		}

		@Test
		public void replaceExtensionFile() throws MalformedURLException {
			assertEquals(new File("/home/test.x.z.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz"));
			assertEquals(new File("/home.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz"));
			assertEquals(new File("/the path/to/file with space.xyz"), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz"));
		}

		@Test
		public void addExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz"));
			assertEquals(new URL("file:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz"));
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesWithFile(), ".xyz"));
		}

		@Test
		public void addExtensionFile() throws MalformedURLException {
			assertEquals(new File("/home/test.x.z.z.xyz"), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz"));
			assertEquals(new File("/home.xyz"), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz"));
			assertEquals(new File("/the path/to/file with space.toto.xyz"), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz"));
		}

	}

	public static class WindowsFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public WindowsFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected String getAbsoluteStandardFilename() {
			return "C:\\home\\test.x.z.z";
		}

		@Override
		protected String getAbsoluteFoldername() {
			return "C:\\home";
		}

		@Override
		protected String getRootnameWithSeparator() {
			return "C:\\";
		}

		@Override
		protected String getRootnameWithoutSeparator() {
			return "C:";
		}

		@Override
		protected String getSeparator() {
			return "\\";
		}

		@Override
		protected String getStandardFilenameWithSpaces() {
			return "C:\\the path\\to\\file with space.toto";
		}

		@Override
		protected OperatingSystem getOS() {
			return OperatingSystem.WIN;
		}

		@Test
		public void removeExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/C:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl()));
			assertEquals(new URL("file:/C:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl()));
		}

		@Test
		public void removeExtensionFile() throws MalformedURLException {
			assertEquals(newFile("C:\\home\\test.x.z", false), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals(newFile("C:\\home", false), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false)));
			assertEquals(newFile("C:\\the path\\to\\file with space", false), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void replaceExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/C:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz"));
			assertEquals(new URL("file:/C:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz"));
		}

		@Test
		public void replaceExtensionFile() throws MalformedURLException {
			assertEquals(newFile("C:\\home\\test.x.z.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz"));
			assertEquals(newFile("C:\\home.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz"));
			assertEquals(newFile("C:\\the path\\to\\file with space.xyz", false), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz"));
		}

		@Test
		public void addExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/C:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz"));
			assertEquals(new URL("file:/C:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz"));
		}

		@Test
		public void addExtensionFile() throws MalformedURLException {
			assertEquals(newFile("C:\\home\\test.x.z.z.xyz", false), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz"));
			assertEquals(newFile("C:\\home.xyz", false), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz"));
			assertEquals(newFile("C:\\the path\\to\\file with space.toto.xyz", false), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz"));
		}

	}

	public static class OSXFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public OSXFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected String getAbsoluteStandardFilename() {
			return "/home/test.x.z.z";
		}

		@Override
		protected String getAbsoluteFoldername() {
			return "/home";
		}

		@Override
		protected String getRootnameWithSeparator() {
			return "/";
		}

		@Override
		protected String getRootnameWithoutSeparator() {
			return "";
		}

		@Override
		protected String getSeparator() {
			return "/";
		}

		@Override
		protected String getStandardFilenameWithSpaces() {
			return "/the path/to/file with space.toto";
		}

		@Override
		protected OperatingSystem getOS() {
			return OperatingSystem.MACOSX;
		}

		@Test
		public void removeExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl()));
			assertEquals(new URL("file:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl()));
			assertEquals(new URL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesWithFile()));
		}

		@Test
		public void removeExtensionFile() throws MalformedURLException {
			assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals(new File("/home"), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false)));
			assertEquals(new File("/the path/to/file with space"), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		public void replaceExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz"));
			assertEquals(new URL("file:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz"));
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesWithFile(), ".xyz"));
		}

		@Test
		public void replaceExtensionFile() throws MalformedURLException {
			assertEquals(new File("/home/test.x.z.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz"));
			assertEquals(new File("/home.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz"));
			assertEquals(new File("/the path/to/file with space.xyz"), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz"));
		}

		@Test
		public void addExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz"));
			assertEquals(new URL("file:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz"));
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesWithFile(), ".xyz"));
		}

		@Test
		public void addExtensionFile() throws MalformedURLException {
			assertEquals(new File("/home/test.x.z.z.xyz"), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz"));
			assertEquals(new File("/home.xyz"), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz"));
			assertEquals(new File("/the path/to/file with space.toto.xyz"), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz"));
		}

	}

}

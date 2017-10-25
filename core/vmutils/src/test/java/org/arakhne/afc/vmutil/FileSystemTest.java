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

package org.arakhne.afc.vmutil;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.Assert.assertArrayEquals;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;

@SuppressWarnings("all")
public class FileSystemTest {

	private boolean oldLibraryLoaderState;

	public static void assertEquals(Object a, Object b) {
		if (!Objects.equals(a, b)) {
			throw new ComparisonFailure("not equal", Objects.toString(a), Objects.toString(b)); //$NON-NLS-1$
		}
	}

	public static String getCurrentDir() throws IOException {
		return new File(".").getCanonicalPath(); //$NON-NLS-1$
	}

	/** Replace file separator by "/"
	 *
	 * @param filename
	 * @return
	 */
	public static String fromFileToUrl(String filename, boolean removeStartSlash) {
		String result = filename.replaceAll("[/\\\\]", Matcher.quoteReplacement("/")); //$NON-NLS-1$ //$NON-NLS-2$
		if (removeStartSlash) {
			if (result.startsWith("/")) { //$NON-NLS-1$
				result = result.substring(1);
			}
		} else {
			if (!result.startsWith("/")) { //$NON-NLS-1$
				result = "/" + result; //$NON-NLS-1$
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
		return filename.replaceAll(Pattern.quote("/"), Matcher.quoteReplacement(File.separator)); //$NON-NLS-1$
	}

	/** Remove root slash
	 */
	public static String removeRootSlash(String filename) {
		if (filename != null && filename.startsWith("/")) { //$NON-NLS-1$
			return filename.substring(1);
		}
		return filename;
	}

	/** @return "http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"
	 */
	protected URL createHttpUrl() throws MalformedURLException {
		return new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"); //$NON-NLS-1$
	}

	/** @return "/home/test/j.jar"
	 */
	protected String createJarFilenameForUrl() {
		return "/home/test/j.jar"; //$NON-NLS-1$
	}

	/** @return "/inner/myjar.jar"
	 */
	protected String createJarInJarFilenameForUrl() {
		return "/inner/myjar.jar"; //$NON-NLS-1$
	}

	/** @return "/org/arakhne/afc/vmutil/file.x.z.z"
	 */
	protected String createInJarFilename() {
		return "/org/arakhne/afc/vmutil/file.x.z.z"; //$NON-NLS-1$
	}

	/** @return "jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"
	 */
	protected URL createFileInJarUrl() throws MalformedURLException {
		return new URL("jar:file:" + createJarFilenameForUrl() + "!" + createInJarFilename()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** @return "jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"
	 */
	protected URL createFileInJarInJarUrl() throws MalformedURLException {
		return new URL("jar:jar:file:" + createJarFilenameForUrl() + "!" //$NON-NLS-1$ //$NON-NLS-2$
				+ createJarInJarFilenameForUrl() + "!" + createInJarFilename()); //$NON-NLS-1$
	}

	/** @return "/the path/to/file with space.toto"
	 */
	protected String createJarFilenameForUrlWithSpaces() {
		return "/the path/to/file with space.toto"; //$NON-NLS-1$
	}

	/** @return "file:/the path/to/file with space.toto"
	 */
	protected URL createFileUrlWithSpacesHardCoded() throws MalformedURLException {
		return new URL("file:" + createJarFilenameForUrlWithSpaces()); //$NON-NLS-1$
	}

	/** @return "jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"
	 */
	protected URL createFileInJarUrlWithSpaces() throws MalformedURLException {
		return new URL("jar:file:" + createJarFilenameForUrlWithSpaces() + "!" + createInJarFilename()); //$NON-NLS-1$ //$NON-NLS-2$
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
		final URL root = new URL("http://maven.arakhne.org/myroot");  //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((File)null, root));

		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"),   //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("a" + File.separator + "b" + File.separator + "c"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						createFileInJarUrlWithSpaces()));  
	}

	@Test
	public void makeRelativeFileFile() throws Exception {
		File root, abs, rel;

		root = FileSystem.getUserHomeDirectory();

		abs = new File(FileSystem.getUserHomeDirectory(), "a");  //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a");  //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b");   //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b");  //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File("a","b");  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc");   //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a");  //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a");  //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc");  //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	public void makeRelativeFileURL() throws Exception {
		File abs, rel;
		URL root;

		root = FileSystem.getUserHomeDirectory().toURI().toURL();

		abs = new File(FileSystem.getUserHomeDirectory(), "a");  //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a");  //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b");   //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b");  //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File("a","b");  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");   //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a");  //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a");  //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");  //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	public void convertFileToURLFile() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		try {
			File f1 = new File("/toto");  //$NON-NLS-1$
			URL u1 = f1.toURI().toURL();
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt");  //$NON-NLS-1$
			URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt");  //$NON-NLS-1$
			File f2 = FileSystem.convertURLToFile(u2);
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
				new URL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org")));  //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/")));  //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto")));  //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/")));  //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi")));  //$NON-NLS-1$
		assertEquals(
				new URL("http://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("http://www.arakhne.org/toto/titi/")));  //$NON-NLS-1$

		assertEquals(
				new URL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org")));  //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/")));  //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto")));  //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/")));  //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi")));  //$NON-NLS-1$
		assertEquals(
				new URL("https://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("https://www.arakhne.org/toto/titi/")));  //$NON-NLS-1$

		assertEquals(
				new URL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org")));  //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/")));  //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto")));  //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/")));  //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi")));  //$NON-NLS-1$
		assertEquals(
				new URL("ftp://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("ftp://www.arakhne.org/toto/titi/")));  //$NON-NLS-1$

		assertEquals(
				new URL("file:/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto/titi")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto/titi/")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:/toto/")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:./toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto/titi")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:./toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto/titi/")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:./"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:./"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./toto/")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:.")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:./")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:toto")));  //$NON-NLS-1$
		assertEquals(
				new URL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("file:toto/")));  //$NON-NLS-1$

		assertEquals(
				new URL("jar:file:test.jar!/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi")));  //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/titi/")));  //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto")));  //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/toto/")));  //$NON-NLS-1$
		assertEquals(
				new URL("jar:file:test.jar!/"),  //$NON-NLS-1$
				FileSystem.getParentURL(new URL("jar:file:test.jar!/")));  //$NON-NLS-1$

		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"),   //$NON-NLS-1$
				FileSystem.getParentURL(createFileInJarUrlWithSpaces()));
	}

	@Test
	public void makeAbsoluteFileURL_noRoot() throws Exception {
		assertNull(FileSystem.makeAbsolute((File)null, (URL)null));

		assertEquals(new URL("file:" + fromFileToUrl(getCurrentDir(), false) + "/toto"),  //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.makeAbsolute(new File("toto"), (URL)null));  //$NON-NLS-1$
	}

	@Test
	public void makeAbsoluteURLFile_noRoot() throws Exception {
		assertNull(FileSystem.makeAbsolute((URL)null, (File)null));

		assertEquals(new URL("file:/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), (File)null));  //$NON-NLS-1$
		assertEquals(new URL("file:toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), (File)null));  //$NON-NLS-1$

		assertEquals(new URL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (File)null));  //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (File)null));  //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (File)null));  //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (File)null));  //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (File)null));  //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (File)null));  //$NON-NLS-1$

		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null));  //$NON-NLS-1$
		assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null));  //$NON-NLS-1$
	}

	@Test
	public void makeAbsoluteURLFile_root() throws Exception {
		File root = new File(File.separator+"myroot");  //$NON-NLS-1$

		assertEquals(new URL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root));  //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root));  //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root));  //$NON-NLS-1$
	}

	@Test
	public void makeAbsoluteURLURL_notroot() throws Exception {
		assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));

		assertEquals(new URL("file:/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:/toto"), (URL)null));  //$NON-NLS-1$
		assertEquals(new URL("file:toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("file:toto"), (URL)null));  //$NON-NLS-1$

		assertEquals(new URL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), (URL)null));  //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), (URL)null));  //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), (URL)null));  //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), (URL)null));  //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), (URL)null));  //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), (URL)null));  //$NON-NLS-1$

		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null));  //$NON-NLS-1$
		assertEquals(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null));  //$NON-NLS-1$
	}

	@Test
	public void makeAbsoluteURLURL_fileAsRoot() throws Exception {
		URL root = new File(File.separator+"myroot").toURI().toURL();  //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(new URL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root));  //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root));  //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root));  //$NON-NLS-1$
	}

	@Test
	public void makeAbsoluteURLURL_httpAsRoot() throws Exception {
		URL root = new URL("http://maven.arakhne.org");  //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(new URL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("http://www.arakhne.org/./toto"), root));  //$NON-NLS-1$

		assertEquals(new URL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("https://www.arakhne.org/./toto"), root));  //$NON-NLS-1$

		assertEquals(new URL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/toto"), root));  //$NON-NLS-1$
		assertEquals(new URL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("ftp://www.arakhne.org/./toto"), root));  //$NON-NLS-1$

		assertEquals(new URL("jar:http://maven.arakhne.org/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root));  //$NON-NLS-1$
	}

	@Test
	public void makeAbsoluteFileFile_noRoot() {
		assertNull(FileSystem.makeAbsolute((File)null, (File)null));

		assertEquals(new File(File.separator+"toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new File(File.separator+"toto"), (File)null));  //$NON-NLS-1$
		assertEquals(new File("toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("toto"), (File)null));  //$NON-NLS-1$
	}

	@Test
	public void makeAbsoluteFileFile_root() {
		File root = new File(File.separator+"myroot");  //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((File)null, root));
	}

	@Test
	public void jreBehaviorRelatedToURL() throws Exception {
		// The following test permits to check if a specifical behaviour of URL
		// is still present in the JRE.
		URL rr = new URL("file://marbre.jpg");  //$NON-NLS-1$
		assertEquals("file", rr.getProtocol());  //$NON-NLS-1$
		assertEquals("marbre.jpg", rr.getAuthority());  //$NON-NLS-1$
		assertEquals("", rr.getPath());  //$NON-NLS-1$
	}

	@Test
	public void convertURLToFile() throws Exception {
		try {
			FileSystem.convertURLToFile(new URL("http://www.arakhne.org"));  //$NON-NLS-1$
			fail("not a file URL");  //$NON-NLS-1$
		}
		catch(IllegalArgumentException exception) {
			//
		}

		assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(new URL("file:./toto")).getCanonicalPath());  //$NON-NLS-1$

		assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(new URL("file:toto")).getCanonicalPath());  //$NON-NLS-1$

		assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(new URL("file:./abs/../toto")).getCanonicalPath());  //$NON-NLS-1$

		assertEquals(new File("/toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(new URL("file:/toto")).getCanonicalPath());  //$NON-NLS-1$
	}

	@Test
	public void convertStringToURL() throws Exception {
		assertNull(FileSystem.convertStringToURL(null, true));
		assertNull(FileSystem.convertStringToURL("", true));  //$NON-NLS-1$
		assertNull(FileSystem.convertStringToURL(null, false));
		assertNull(FileSystem.convertStringToURL("", false));  //$NON-NLS-1$

		URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false);  //$NON-NLS-1$
		assertNotNull(rr);
		assertEquals("file", rr.getProtocol());  //$NON-NLS-1$
		assertEquals("", rr.getAuthority()); //$NON-NLS-1$
		assertEquals("", rr.getHost());  //$NON-NLS-1$
		assertNull(rr.getQuery());
		assertEquals("marbre.jpg", rr.getPath());  //$NON-NLS-1$

		assertEquals(new URL("http", "www.arakhne.org", "/"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("http://www.arakhne.org/", true));  //$NON-NLS-1$
		assertEquals(new URL("http", "www.arakhne.org", "/"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("http://www.arakhne.org/", false));  //$NON-NLS-1$

		// CAUTION: testing right-formed jar URL.
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true));  //$NON-NLS-1$
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false));  //$NON-NLS-1$

		// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
		assertEquals(new URL("file", "", "/home/test/j.jar"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar", true));  //$NON-NLS-1$
		assertEquals(new URL("file", "", "/home/test/j.jar"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar", false));  //$NON-NLS-1$

		// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true));  //$NON-NLS-1$
		assertEquals(new URL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false));  //$NON-NLS-1$

		URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt");  //$NON-NLS-1$
		assertNotNull(testResource);

		assertEquals(testResource,
				FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", true));  //$NON-NLS-1$
		assertEquals(null,
				FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", false));  //$NON-NLS-1$

		assertEquals(testResource,
				FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", true));  //$NON-NLS-1$
		assertEquals(null,
				FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", false));  //$NON-NLS-1$

		assertEquals(testResource,
				FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", true));  //$NON-NLS-1$
		assertEquals(new URL("file", "", "/org/arakhne/afc/vmutil/test.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", false));  //$NON-NLS-1$

		assertEquals(testResource,
				FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", true));  //$NON-NLS-1$
		assertEquals(new URL("file","", "org/arakhne/afc/vmutil/test.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", false));  //$NON-NLS-1$
	}

	@Test
	public void joinURLStringArray() throws Exception {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"), FileSystem.join(createHttpUrl(), "a", "b", "c"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrl(), "a", "b", "c"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarInJarUrl(), "a", "b", "c"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"), FileSystem.join(createFileUrlWithSpacesHardCoded(), "a", "b", "c"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrlWithSpaces(), "a", "b", "c"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void joinURLFileArray() throws Exception {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"), //$NON-NLS-1$
				FileSystem.join(createHttpUrl(), new File("a"), new File("b"), new File("c")));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
				FileSystem.join(createFileInJarUrl(), new File("a"), new File("b"), new File("c")));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
				FileSystem.join(createFileInJarInJarUrl(), new File("a"), new File("b"), new File("c")));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto/a/b/c"), //$NON-NLS-1$
				FileSystem.join(createFileUrlWithSpacesHardCoded(), new File("a"), new File("b"), new File("c")));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
				FileSystem.join(createFileInJarUrlWithSpaces(), new File("a"), new File("b"), new File("c")));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Test
	public void splitURL() throws Exception {
		assertArrayEquals(new String[] {"", "path", "to", "file.x.z.z"}, FileSystem.split(createHttpUrl()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrl()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarInJarUrl()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertArrayEquals(new String[] {"", "the path", "to", "file with space.toto"}, FileSystem.split(createFileUrlWithSpacesHardCoded()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrlWithSpaces()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertArrayEquals(new String[] {"", "a.b.c"}, FileSystem.split(new URL("file:///a.b.c/")));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {""}, FileSystem.split(new URL("file://")));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void extensionsURL() throws MalformedURLException {
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createHttpUrl()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrl()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarInJarUrl()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesHardCoded()));  //$NON-NLS-1$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrlWithSpaces()));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions(new URL("file:///a.b.c/")));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[0], FileSystem.extensions(new URL("file://")));   //$NON-NLS-1$
	}

	@Test
	public void extensionsString() {
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:///a.b.c/"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[0], FileSystem.extensions("file://"));   //$NON-NLS-1$

		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void extensionURL() throws MalformedURLException {
		assertEquals(".z", FileSystem.extension(createHttpUrl()));  //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(createFileInJarUrl()));  //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(createFileInJarInJarUrl()));  //$NON-NLS-1$
		assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesHardCoded()));  //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(createFileInJarUrlWithSpaces()));  //$NON-NLS-1$
		assertEquals(".c", FileSystem.extension(new URL("file:///a.b.c/")));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.extension(new URL("file://")));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void extensionString() {
		assertEquals(".z", FileSystem.extension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".c", FileSystem.extension("file:///a.b.c/"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".c", FileSystem.extension("file:a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".c", FileSystem.extension("a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.extension("file://"));   //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(".dae", FileSystem.extension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".dae", FileSystem.extension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".dae", FileSystem.extension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void hasExtensionURL() throws MalformedURLException {
		assertTrue(FileSystem.hasExtension(createHttpUrl(), ".z"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createHttpUrl(), "z"));  //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createHttpUrl(), ".c"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrl(), ".z"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrl(), "z"));  //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createFileInJarUrl(), ".c"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarInJarUrl(), ".z"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarInJarUrl(), "z"));  //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createFileInJarInJarUrl(), ".c"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".toto"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), "toto"));  //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".zip"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".z"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), "z"));  //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".c"));  //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(new URL("file:///a.b.c/"), ".c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension(new URL("file:///a.b.c/"), "c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension(new URL("file:///a.b.c/"), ".zip"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension(new URL("file://"), ".c"));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void hasExtensionString() {
		assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", "z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".c"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", "z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".c"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:///a.b.c/", ".c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:///a.b.c/", "c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:///a.b.c/", ".z"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:a.b.c", ".c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:a.b.c", "c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:a.b.c", ".z"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("a.b.c", ".c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("a.b.c", "c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("a.b.c", ".z"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file://", ".z"));   //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip"));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void removeExtensionURL() throws MalformedURLException {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z?toto#frag"), FileSystem.removeExtension(createHttpUrl()));  //$NON-NLS-1$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrl()));  //$NON-NLS-1$
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarInJarUrl()));  //$NON-NLS-1$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesHardCoded()));  //$NON-NLS-1$
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrlWithSpaces()));  //$NON-NLS-1$
		assertEquals(new URL("file:///a.b"), FileSystem.removeExtension(new URL("file:///a.b.c/")));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("file", "", ""), FileSystem.removeExtension(new URL("file://")));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	public void replaceExtensionURLString() throws MalformedURLException {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.xyz?toto#frag"), FileSystem.replaceExtension(createHttpUrl(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrl(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarInJarUrl(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesHardCoded(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrlWithSpaces(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("file:///a.b.xyz"), FileSystem.replaceExtension(new URL("file:///a.b.c/"), ".xyz"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("file", "", ""), FileSystem.replaceExtension(new URL("file://"), ".xyz"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Test
	public void addExtensionURLString() throws MalformedURLException {
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z.xyz?toto#frag"), FileSystem.addExtension(createHttpUrl(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrl(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarInJarUrl(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesHardCoded(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrlWithSpaces(), ".xyz"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new URL("file:///a.b.c.xyz"), FileSystem.addExtension(new URL("file:///a.b.c/"), ".xyz"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(new URL("file", "", ""), FileSystem.addExtension(new URL("file://"), ".xyz"));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Test
	public void basenameURL() throws MalformedURLException {
		assertEquals("file.x.z", FileSystem.basename(createHttpUrl()));  //$NON-NLS-1$
		assertEquals("file.x.z", FileSystem.basename(createFileInJarUrl()));  //$NON-NLS-1$
		assertEquals("file.x.z", FileSystem.basename(createFileInJarInJarUrl()));  //$NON-NLS-1$
		assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesHardCoded()));  //$NON-NLS-1$
		assertEquals("file.x.z", FileSystem.basename(createFileInJarUrlWithSpaces()));  //$NON-NLS-1$
		assertEquals("a.b", FileSystem.basename(new URL("file:///a.b.c/")));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.basename(new URL("file://")));   //$NON-NLS-1$ //$NON-NLS-2$

		URL url = new URL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			assertEquals("terrain_physx", FileSystem.basename(url));  //$NON-NLS-1$
			fail("expecting assertion failure");  //$NON-NLS-1$
		}
		catch(AssertionError exception) {
			//
		}
	}

	@Test
	public void basenameString() {
		assertEquals("file.x.z", FileSystem.basename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b", FileSystem.basename("file:///a.b.c/"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b", FileSystem.basename("file:a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b", FileSystem.basename("a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.basename("file://"));   //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("terrain_physx", FileSystem.basename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.basename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.basename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void largeBasenameURL() throws MalformedURLException {
		assertEquals("file.x.z.z", FileSystem.largeBasename(createHttpUrl()));  //$NON-NLS-1$
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrl()));  //$NON-NLS-1$
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarInJarUrl()));  //$NON-NLS-1$
		assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesHardCoded()));  //$NON-NLS-1$
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrlWithSpaces()));  //$NON-NLS-1$
		assertEquals("a.b.c", FileSystem.largeBasename(new URL("file:///a.b.c/")));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.largeBasename(new URL("file://")));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void largeBasenameString() {
		assertEquals("file.x.z.z", FileSystem.largeBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b.c", FileSystem.largeBasename("file:///a.b.c/"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b.c", FileSystem.largeBasename("file:a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b.c", FileSystem.largeBasename("a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.largeBasename("file://"));   //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("terrain_physx.dae", FileSystem.largeBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void shortBasenameURL() throws MalformedURLException {
		assertEquals("file", FileSystem.shortBasename(createHttpUrl()));  //$NON-NLS-1$
		assertEquals("file", FileSystem.shortBasename(createFileInJarUrl()));  //$NON-NLS-1$
		assertEquals("file", FileSystem.shortBasename(createFileInJarInJarUrl()));  //$NON-NLS-1$
		assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesHardCoded()));  //$NON-NLS-1$
		assertEquals("file", FileSystem.shortBasename(createFileInJarUrlWithSpaces()));  //$NON-NLS-1$
		assertEquals("a", FileSystem.shortBasename(new URL("file:///a.b.c/")));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.shortBasename(new URL("file://")));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void shortBasenameString() {
		assertEquals("file", FileSystem.shortBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a", FileSystem.shortBasename("file:///a.b.c/"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a", FileSystem.shortBasename("file:a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a", FileSystem.shortBasename("a.b.c"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.shortBasename("file://"));   //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("terrain_physx", FileSystem.shortBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.shortBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.shortBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));   //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void toShortestURLURL() throws Exception {
		URLHandlerUtil.installArakhneHandlers();
		try {
			File f1 = new File("/toto");  //$NON-NLS-1$
			URL u1 = f1.toURI().toURL();
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt");  //$NON-NLS-1$
			URL u2e = new URL("resource:org/arakhne/afc/vmutil/test.txt");  //$NON-NLS-1$

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

		abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL();  //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a");  //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b").toURI().toURL();   //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b");   //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");   //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL();  //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a");  //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc");  //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc").toURI().toURL();  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
				new URL("file:/a/b/c/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/./d/e")));  //$NON-NLS-1$

		assertEquals(
				new URL("file:/a/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:/a/b/../c/../d/e")));  //$NON-NLS-1$

		assertEquals(
				new URL("file:/a/b/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:/a/b/./c/../d/e")));  //$NON-NLS-1$

		assertEquals(
				new URL("file:../a/b/c/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:../a/b/./c/./d/e")));  //$NON-NLS-1$

		assertEquals(
				new URL("file:../a/c/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(new URL("file:../a/b/../c/./d/e")));  //$NON-NLS-1$
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
			testDir = FileSystem.createTempDirectory("unittest", null);  //$NON-NLS-1$
			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), testDir);  //$NON-NLS-1$
			FileSystem.copy(FileSystemTest.class.getResource("test2.txt"), testDir);  //$NON-NLS-1$
			File subdir = new File(testDir, "subdir");  //$NON-NLS-1$
			subdir.mkdirs();
			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), subdir);  //$NON-NLS-1$
			FileSystem.zipFile(testDir, testArchive);
		} finally {
			FileSystem.delete(testDir);
		}
	}

	@Test
	public void zipFileFile() throws IOException {
		File testArchive = null;

		try {
			testArchive = File.createTempFile("unittest", ".zip");   //$NON-NLS-1$ //$NON-NLS-2$

			createZip(testArchive);

			try (ZipFile zipFile = new ZipFile(testArchive)) {

				ZipEntry zipEntry = zipFile.getEntry("test.txt");  //$NON-NLS-1$
				assertNotNull(zipEntry);
				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry)));  //$NON-NLS-1$

				zipEntry = zipFile.getEntry("test2.txt");  //$NON-NLS-1$
				assertNotNull(zipEntry);
				assertEquals("TEST2: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry)));  //$NON-NLS-1$

				zipEntry = zipFile.getEntry("subdir/test.txt");  //$NON-NLS-1$
				assertNotNull(zipEntry);
				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry)));  //$NON-NLS-1$
			}
		} finally {
			FileSystem.delete(testArchive);
		}
	}

	@Test
	public void testUnzipFileFile() throws IOException {
		File testArchive = null;
		try {
			testArchive = File.createTempFile("unittest", ".zip");   //$NON-NLS-1$ //$NON-NLS-2$

			createZip(testArchive);

			File testDir = FileSystem.createTempDirectory("unittest", null);  //$NON-NLS-1$
			FileSystem.deleteOnExit(testDir);
			File subDir = new File(testDir, "subdir");  //$NON-NLS-1$

			FileSystem.unzipFile(testArchive, testDir);

			assertTrue(testDir.isDirectory());
			assertTrue(subDir.isDirectory());

			String txt;

			File file = new File(testDir, "test.txt");  //$NON-NLS-1$
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST1: FOR UNIT TEST ONLY", txt);  //$NON-NLS-1$

			file = new File(testDir, "test2.txt");  //$NON-NLS-1$
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST2: FOR UNIT TEST ONLY", txt);  //$NON-NLS-1$

			file = new File(subDir, "test.txt");  //$NON-NLS-1$
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST1: FOR UNIT TEST ONLY", txt); //$NON-NLS-1$
		} finally {
			FileSystem.delete(testArchive);
		}
	}

	@Test
	public void getFileExtensionCharacter() {
		assertInlineParameterUsage(FileSystem.class, "getFileExtensionCharacter"); //$NON-NLS-1$
	}

	@Test
	public void isWindowNativeFilename() {
		assertFalse(FileSystem.isWindowsNativeFilename("D:/vivus_test/export dae/yup/terrain_physx.dae"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("D|\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("/vivus_test/export dae/yup/terrain_physx.dae"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("/"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  //$NON-NLS-1$

		assertTrue(FileSystem.isWindowsNativeFilename("file:C:\\a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:\\a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:C:a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:\\a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://\\a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$

		assertTrue(FileSystem.isWindowsNativeFilename("C:\\a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("C:a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("a\\b\\c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$

		assertFalse(FileSystem.isWindowsNativeFilename("file:C:/a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://C:/a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:C:a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://C:a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:/a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:///a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://host/a/b/c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:////host/a/b/c.txt"));  //$NON-NLS-1$

		assertTrue(FileSystem.isWindowsNativeFilename("C:c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file:C:c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file:c.txt"));  //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("file://C:c.txt"));  //$NON-NLS-1$
		assertFalse(FileSystem.isWindowsNativeFilename("file://c.txt"));  //$NON-NLS-1$
	}

	@Test
	public void normalizeWindowNativeFilename() {
		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$

		assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:/a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:/a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:/a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:///a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://host/a/b/c.txt"));  //$NON-NLS-1$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:////host/a/b/c.txt"));  //$NON-NLS-1$

		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("C:c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(FileSystem.normalizeWindowsNativeFilename("c.txt"));  //$NON-NLS-1$
		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:c.txt"));  //$NON-NLS-1$
		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://c.txt"));  //$NON-NLS-1$
	}

	@Test
	public void convertStringToFile() {
		assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:/vivus_test/export dae/yup/terrain_physx.dae"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("/vivus_test/export dae/yup/terrain_physx.dae"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/", FileSystem.convertStringToFile("/"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//", FileSystem.convertStringToFile("\\\\"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("////vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae"));  //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file://\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file://\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("C:\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("\\\\host\\a\\b\\c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:/a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:/C:/a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:/a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:/C:a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:/a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:///a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("host/a/b/c.txt", FileSystem.convertStringToFile("file://host/a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:////host/a/b/c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:c.txt", FileSystem.convertStringToFile("C:c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("c.txt", FileSystem.convertStringToFile("c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file:C:c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("c.txt", FileSystem.convertStringToFile("file:c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file://C:c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("c.txt", FileSystem.convertStringToFile("file://c.txt"));  //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void isJarURLURL() throws Exception {
		assertFalse(FileSystem.isJarURL(createHttpUrl()));
		assertTrue(FileSystem.isJarURL(createFileInJarUrl()));
		assertTrue(FileSystem.isJarURL(createFileInJarInJarUrl()));
		assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesHardCoded()));  

		assertInlineParameterUsage(FileSystem.class, "isJarURL", URL.class); //$NON-NLS-1$
	}

	@Test
	public void getJarURLURL() throws Exception {
		assertNull(FileSystem.getJarURL(createHttpUrl()));
		assertEquals(new URL("file:" + createJarFilenameForUrl()), FileSystem.getJarURL(createFileInJarUrl())); //$NON-NLS-1$
		assertEquals(new URL("jar:file:" //$NON-NLS-1$
				+ createJarFilenameForUrl() + "!" //$NON-NLS-1$
				+ createJarInJarFilenameForUrl()),
				FileSystem.getJarURL(createFileInJarInJarUrl()));

		assertEquals(new URL("file:" + createJarFilenameForUrlWithSpaces()), FileSystem.getJarURL(createFileInJarUrlWithSpaces()));   //$NON-NLS-1$
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
		assertEquals(new URL("http://toto:titi@www.arakhne.org/path/to/"), //$NON-NLS-1$
				FileSystem.dirname(createHttpUrl()));
		assertEquals(new URL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/"), //$NON-NLS-1$
				FileSystem.dirname(createFileInJarUrl()));
		assertEquals(new URL("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/"), //$NON-NLS-1$
				FileSystem.dirname(createFileInJarInJarUrl()));
		assertEquals(new URL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"), //$NON-NLS-1$
				FileSystem.dirname(createFileInJarUrlWithSpaces()));
	}

	private static abstract class AbstractFileSystemTest {

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
			return new URL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false)); //$NON-NLS-1$
		}

		/** @return "file:/home" or "file:C:\home" or "file:/home"
		 */
		protected URL createAbsoluteFolderUrl() throws MalformedURLException {
			return new URL("file:" + fromFileToUrl(getAbsoluteFoldername(), false)); //$NON-NLS-1$
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
			assertEquals(new URL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							getAbsoluteStandardFilename()));
		}

		@Test
		public void toJarURLFileFile() throws MalformedURLException {
			assertEquals(new URL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							newFile(getAbsoluteStandardFilename(), true)));
		}

		@Test
		public void toJarURLURLString() throws MalformedURLException {
			assertEquals(new URL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							getAbsoluteStandardFilename()));
		}

		@Test
		public void toJarURLURLFile() throws MalformedURLException {
			assertEquals(new URL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							newFile(getAbsoluteStandardFilename(), true)));
		}

		@Test
		public void dirnameFile() throws Exception {
			assertEquals(new URL("file", "", fromFileToUrl(getAbsoluteFoldername(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals(new URL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(newFile(getAbsoluteFoldername(), false)));
		}

		@Test
		public void dirnameURL() throws Exception {
			assertEquals(new URL("file", "", fromFileToUrl(getAbsoluteFoldername(), false) + "/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.dirname(createAbsoluteStandardFileUrl()));
			assertEquals(new URL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(createAbsoluteFolderUrl()));
		}

		@Test
		public void splitFile() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$ //$NON-NLS-2$
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "the path", "to", "file with space.toto"}, FileSystem.split(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		public void splitURL() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(createAbsoluteStandardFileUrl())); //$NON-NLS-1$ //$NON-NLS-2$
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@Test
		public void extensionsURL() throws MalformedURLException {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createAbsoluteStandardFileUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertArrayEquals(new String[0], FileSystem.extensions(createAbsoluteFolderUrl()));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesWithFile())); //$NON-NLS-1$
		}

		@Test
		public void extensionsFile() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertArrayEquals(new String[0], FileSystem.extensions(newFile(getAbsoluteFoldername(), false)));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void extensionsString() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(getAbsoluteStandardFilename())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertArrayEquals(new String[0], FileSystem.extensions(getAbsoluteFoldername()));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		public void extensionURL() throws MalformedURLException {
			assertEquals(".z", FileSystem.extension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("", FileSystem.extension(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesWithFile())); //$NON-NLS-1$
		}

		@Test
		public void extensionFile() {
			assertEquals(".z", FileSystem.extension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("", FileSystem.extension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(".toto", FileSystem.extension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void extensionString() {
			assertEquals(".z", FileSystem.extension(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("", FileSystem.extension(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals(".toto", FileSystem.extension(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		public void hasExtensionURL() throws MalformedURLException {
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), "z")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".c")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(createAbsoluteFolderUrl(), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(), ".toto")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(), "toto")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(), ".z")); //$NON-NLS-1$
		}

		@Test
		public void hasExtensionFile() {
			assertTrue(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), "z")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), ".c")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(newFile(getAbsoluteFoldername(), false), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), ".toto")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), "toto")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), ".z")); //$NON-NLS-1$
		}

		@Test
		public void hasExtensionString() {
			assertTrue(FileSystem.hasExtension(getAbsoluteStandardFilename(), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(getAbsoluteStandardFilename(), "z")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(getAbsoluteStandardFilename(), ".c")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(getAbsoluteFoldername(), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(getStandardFilenameWithSpaces(), ".toto")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(getStandardFilenameWithSpaces(), "toto")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(getStandardFilenameWithSpaces(), ".c")); //$NON-NLS-1$
		}

		@Test
		public void basenameURL() throws MalformedURLException {
			assertEquals("test.x.z", FileSystem.basename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("home", FileSystem.basename(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesWithFile())); //$NON-NLS-1$
		}

		@Test
		public void basenameFile() {
			assertEquals("test.x.z", FileSystem.basename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("home", FileSystem.basename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.basename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void basenameString() {
			assertEquals("test.x.z", FileSystem.basename(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("home", FileSystem.basename(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.basename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		public void largeBasenameURL() throws MalformedURLException {
			assertEquals("test.x.z.z", FileSystem.largeBasename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("home", FileSystem.largeBasename(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesWithFile())); //$NON-NLS-1$
		}

		@Test
		public void largeBasenameFile() {
			assertEquals("test.x.z.z", FileSystem.largeBasename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("home", FileSystem.largeBasename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals("file with space.toto", FileSystem.largeBasename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void largeBasenameString() {
			assertEquals("test.x.z.z", FileSystem.largeBasename(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("home", FileSystem.largeBasename(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals("file with space.toto", FileSystem.largeBasename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		public void shortBasenameURL() throws MalformedURLException {
			assertEquals("test", FileSystem.shortBasename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("home", FileSystem.shortBasename(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesWithFile())); //$NON-NLS-1$
		}

		@Test
		public void shortBasenameFile() {
			assertEquals("test", FileSystem.shortBasename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("home", FileSystem.shortBasename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.shortBasename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void shortBasenameString() {
			assertEquals("test", FileSystem.shortBasename(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("home", FileSystem.shortBasename(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.shortBasename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		public void joinFileStringArray() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							"",  //$NON-NLS-1$
							"home",  //$NON-NLS-1$
							"test.x.z.z"));  //$NON-NLS-1$
		}


		@Test
		public void joinFileFileArray() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							new File("home"),  //$NON-NLS-1$
							new File("test.x.z.z")));  //$NON-NLS-1$
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							new File(File.separator+"home"),  //$NON-NLS-1$
							new File("test.x.z.z")));  //$NON-NLS-1$
		}

		@Test
		public void joinURLStringArray() throws Exception {
			URL base;

			base = new URL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = new URL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", false).toURI().toURL(); //$NON-NLS-1$
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		public void joinURLFileArray() throws Exception {
			URL base;

			base = new URL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = new URL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", false).toURI().toURL(); //$NON-NLS-1$
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator(), false) + "toto"),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), (URL)null));  //$NON-NLS-1$
		}

		@Test
		public void makeAbsoluteFileURL_httpAsRoot() throws Exception {
			final URL root = new URL("http://maven.arakhne.org/myroot");  //$NON-NLS-1$

			assertEquals(new URL("http://maven.arakhne.org/myroot/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newFile("toto", false), root));  //$NON-NLS-1$
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root));  //$NON-NLS-1$

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "a/b/c", false)),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator()
							+ "a" + File.separator + "b" + File.separator + "c", true), root));   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		public void makeAbsoluteFileURL_fileAsRoot() throws Exception {
			URL root = new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false));  //$NON-NLS-1$ //$NON-NLS-2$

			assertNull(FileSystem.makeAbsolute((File)null, root));

			assertEquals(new URL("file:/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new File("/toto"), root));  //$NON-NLS-1$
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new File("toto"), root));  //$NON-NLS-1$
		}

		@Test
		public void makeAbsoluteURLURL_httpAsRoot() throws Exception {
			final URL root = new URL("http://maven.arakhne.org/myroot");  //$NON-NLS-1$

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root));  //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("http://maven.arakhne.org/myroot/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new URL("file:toto"), root));  //$NON-NLS-1$

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root));  //$NON-NLS-1$
		}

		@Test
		public void makeAbsoluteURLURL_fileAsRoot() throws Exception {
			URL root = new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false));  //$NON-NLS-1$ //$NON-NLS-2$

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root));  //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new URL("file:toto"), root));  //$NON-NLS-1$

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root));  //$NON-NLS-1$
		}

		@Test
		public void makeAbsoluteFileFile_root() {
			File root = newFile(getRootnameWithSeparator() + "myroot", true);  //$NON-NLS-1$

			assertEquals(newFile(getRootnameWithSeparator() + "toto", true),   //$NON-NLS-1$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root));  //$NON-NLS-1$

			assertEquals(newFile(getRootnameWithSeparator() + "myroot" + getSeparator() + "toto", true),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new File("toto"), root));  //$NON-NLS-1$
		}

		@Test
		public void makeAbsoluteURLFile_root() throws Exception {
			File root = newFile(getRootnameWithSeparator() + "myroot", true);  //$NON-NLS-1$

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root));  //$NON-NLS-1$ //$NON-NLS-2$

			assertEquals(new URL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new URL("file:toto"), root));  //$NON-NLS-1$

			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$
			
			assertEquals(new URL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new URL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root));  //$NON-NLS-1$
		}

	}

	public static class UnixFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public UnixFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected String getAbsoluteStandardFilename() {
			return "/home/test.x.z.z"; //$NON-NLS-1$
		}

		@Override
		protected String getAbsoluteFoldername() {
			return "/home"; //$NON-NLS-1$
		}

		@Override
		protected String getRootnameWithSeparator() {
			return "/"; //$NON-NLS-1$
		}

		@Override
		protected String getRootnameWithoutSeparator() {
			return ""; //$NON-NLS-1$
		}

		@Override
		protected String getSeparator() {
			return "/"; //$NON-NLS-1$
		}

		@Override
		protected String getStandardFilenameWithSpaces() {
			return "/the path/to/file with space.toto"; //$NON-NLS-1$
		}

		@Override
		protected OperatingSystem getOS() {
			return OperatingSystem.LINUX;
		}

		@Test
		public void removeExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals(new URL("file:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals(new URL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesWithFile())); //$NON-NLS-1$
		}

		@Test
		public void removeExtensionFile() {
			assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals(new File("/home"), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(new File("/the path/to/file with space"), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void replaceExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesWithFile(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void replaceExtensionFile() {
			assertEquals(new File("/home/test.x.z.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/home.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/the path/to/file with space.xyz"), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void addExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesWithFile(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void addExtensionFile() {
			assertEquals(new File("/home/test.x.z.z.xyz"), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/home.xyz"), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/the path/to/file with space.toto.xyz"), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	public static class WindowsFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public WindowsFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected String getAbsoluteStandardFilename() {
			return "C:\\home\\test.x.z.z"; //$NON-NLS-1$
		}

		@Override
		protected String getAbsoluteFoldername() {
			return "C:\\home"; //$NON-NLS-1$
		}

		@Override
		protected String getRootnameWithSeparator() {
			return "C:\\"; //$NON-NLS-1$
		}

		@Override
		protected String getRootnameWithoutSeparator() {
			return "C:"; //$NON-NLS-1$
		}

		@Override
		protected String getSeparator() {
			return "\\"; //$NON-NLS-1$
		}

		@Override
		protected String getStandardFilenameWithSpaces() {
			return "C:\\the path\\to\\file with space.toto"; //$NON-NLS-1$
		}

		@Override
		protected OperatingSystem getOS() {
			return OperatingSystem.WIN;
		}

		@Test
		public void removeExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/C:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals(new URL("file:/C:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@Test
		public void removeExtensionFile() {
			assertEquals(newFile("C:\\home\\test.x.z", false), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals(newFile("C:\\home", false), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(newFile("C:\\the path\\to\\file with space", false), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void replaceExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/C:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/C:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void replaceExtensionFile() {
			assertEquals(newFile("C:\\home\\test.x.z.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newFile("C:\\home.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newFile("C:\\the path\\to\\file with space.xyz", false), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void addExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/C:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/C:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void addExtensionFile() {
			assertEquals(newFile("C:\\home\\test.x.z.z.xyz", false), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newFile("C:\\home.xyz", false), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newFile("C:\\the path\\to\\file with space.toto.xyz", false), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	public static class OSXFilenameStandardFileSystemTest extends AbstractFileSystemTest {

		public OSXFilenameStandardFileSystemTest() throws Exception {
			super();
		}

		@Override
		protected String getAbsoluteStandardFilename() {
			return "/home/test.x.z.z"; //$NON-NLS-1$
		}

		@Override
		protected String getAbsoluteFoldername() {
			return "/home"; //$NON-NLS-1$
		}

		@Override
		protected String getRootnameWithSeparator() {
			return "/"; //$NON-NLS-1$
		}

		@Override
		protected String getRootnameWithoutSeparator() {
			return ""; //$NON-NLS-1$
		}

		@Override
		protected String getSeparator() {
			return "/"; //$NON-NLS-1$
		}

		@Override
		protected String getStandardFilenameWithSpaces() {
			return "/the path/to/file with space.toto"; //$NON-NLS-1$
		}

		@Override
		protected OperatingSystem getOS() {
			return OperatingSystem.MACOSX;
		}

		@Test
		public void removeExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals(new URL("file:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals(new URL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesWithFile())); //$NON-NLS-1$
		}

		@Test
		public void removeExtensionFile() {
			assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals(new File("/home"), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(new File("/the path/to/file with space"), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		public void replaceExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesWithFile(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void replaceExtensionFile() {
			assertEquals(new File("/home/test.x.z.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/home.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/the path/to/file with space.xyz"), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void addExtensionURL() throws MalformedURLException {
			assertEquals(new URL("file:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new URL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesWithFile(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		public void addExtensionFile() {
			assertEquals(new File("/home/test.x.z.z.xyz"), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/home.xyz"), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/the path/to/file with space.toto.xyz"), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.vmutil.resource.Handler;
import org.arakhne.afc.vmutil.resource.HandlerProvider;


@SuppressWarnings("all")
public class FileSystemTest {

	private boolean oldLibraryLoaderState;

	public static void assertEquals(Object a, Object b) {
		if (!Objects.equals(a, b)) {
			fail("not equal"); //$NON-NLS-1$
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

	private static URL newURL(String url) throws Exception {
		return new URL(url);
	}
	
	private static URL newURL(String protocol, String host, String path) throws Exception {
		return new URL(protocol, host, path);
	}

	/** @return "http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"
	 */
	protected URL createHttpUrl() throws Exception {
		return newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag"); //$NON-NLS-1$
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
	protected URL createFileInJarUrl() throws Exception {
		return newURL("jar:file:" + createJarFilenameForUrl() + "!" + createInJarFilename()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** @return "jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z"
	 */
	protected URL createFileInJarInJarUrl() throws Exception {
		return newURL("jar:jar:file:" + createJarFilenameForUrl() + "!" //$NON-NLS-1$ //$NON-NLS-2$
				+ createJarInJarFilenameForUrl() + "!" + createInJarFilename()); //$NON-NLS-1$
	}

	/** @return "/the path/to/file with space.toto"
	 */
	protected String createJarFilenameForUrlWithSpaces() {
		return "/the path/to/file with space.toto"; //$NON-NLS-1$
	}

	/** @return "file:/the path/to/file with space.toto"
	 */
	protected URL createFileUrlWithSpacesHardCoded() throws Exception {
		return newURL("file:" + createJarFilenameForUrlWithSpaces()); //$NON-NLS-1$
	}

	/** @return "jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z"
	 */
	protected URL createFileInJarUrlWithSpaces() throws Exception {
		return newURL("jar:file:" + createJarFilenameForUrlWithSpaces() + "!" + createInJarFilename()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@BeforeEach
	public void setUp() throws Exception {
		// Disable native library loading during unit tests
		this.oldLibraryLoaderState = LibraryLoader.isEnable();
		LibraryLoader.setEnable(false);
	}

	@AfterEach
	public void tearDown() throws Exception {
		// Restore library loading state
		LibraryLoader.setEnable(this.oldLibraryLoaderState);
	}

	@Test
	@DisplayName("makeAbsolute(File, URL)")
	public void makeAbsoluteFileURL_httpAsRoot() throws Exception {
		final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((File)null, root));

		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("a" + File.separator + "b" + File.separator + "c"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						createFileInJarUrlWithSpaces()));  
	}

	@Test
	@DisplayName("makeRelative(File, File)")
	public void makeRelativeFileFile() throws Exception {
		File root, abs, rel;

		root = FileSystem.getUserHomeDirectory();

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b"); //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc"); //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc"); //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	@DisplayName("makeRelative(File, URL)")
	public void makeRelativeFileURL() throws Exception {
		File abs, rel;
		URL root;

		root = FileSystem.getUserHomeDirectory().toURI().toURL();

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b"); //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));

		abs = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		rel = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"); //$NON-NLS-1$
		assertEquals(rel, FileSystem.makeRelative(abs, root));



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	@DisplayName("convertURLToFile(File)")
	public void convertFileToURLFile() throws Exception {
		handlesResourceUrl();
		try {
			File f1 = new File("/toto"); //$NON-NLS-1$
			URL u1 = newURL("file:/toto"); //$NON-NLS-1$
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			URL u2e = newURL("resource:org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			File f2 = FileSystem.convertURLToFile(u2);
			assertEquals(u1, FileSystem.convertFileToURL(f1));
			assertEquals(u2e, FileSystem.convertFileToURL(f2));
		} finally {
			unhandlesResourceUrl();
		}
	}

	private static volatile TestingHandler handler;
	
	private void handlesResourceUrl() throws Exception {
		if (FileSystem.class.getModule().getName() == null) {
			// Special testing framework that is not loading the library into a module.
			if (handler == null) {
				handler = new TestingHandler();
				URL.setURLStreamHandlerFactory(handler);
				handler.isActive = true;
			}
		}
	}

	private void unhandlesResourceUrl() throws Exception {
		if (FileSystem.class.getModule().getName() == null) {
			// Special testing framework that is not loading the library into a module.
			if (handler != null) {
				handler.isActive = false;
			}
		}
	}

	/** Testing handler.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 17.0
	 */
	private static class TestingHandler implements URLStreamHandlerFactory {

		public volatile boolean isActive;
		
		@Override
		public URLStreamHandler createURLStreamHandler(String protocol) {
			if (this.isActive && URISchemeType.RESOURCE.isScheme(protocol)) {
				return new Handler();
			}
			return null;
		}
		
	}

	@Test
	@DisplayName("convertURLToFile(File) - Issue 173")
	public void convertFileToURLFile_issue173() throws Exception {
		File f1 = new File("./myfile.txt"); //$NON-NLS-1$
		URL u1 = newURL("file:./myfile.txt"); //$NON-NLS-1$
		assertEquals(u1, FileSystem.convertFileToURL(f1));
	}

	@Test
	@DisplayName("getParentURL(URL)")
	public void getParentURLURL() throws Exception {
		assertEquals(
				newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("http://www.arakhne.org"))); //$NON-NLS-1$
		assertEquals(
				newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("http://www.arakhne.org/"))); //$NON-NLS-1$
		assertEquals(
				newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("http://www.arakhne.org/toto"))); //$NON-NLS-1$
		assertEquals(
				newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("http://www.arakhne.org/toto/"))); //$NON-NLS-1$
		assertEquals(
				newURL("http://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("http://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				newURL("http://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("http://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$

		assertEquals(
				newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("https://www.arakhne.org"))); //$NON-NLS-1$
		assertEquals(
				newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("https://www.arakhne.org/"))); //$NON-NLS-1$
		assertEquals(
				newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("https://www.arakhne.org/toto"))); //$NON-NLS-1$
		assertEquals(
				newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("https://www.arakhne.org/toto/"))); //$NON-NLS-1$
		assertEquals(
				newURL("https://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("https://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				newURL("https://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("https://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$

		assertEquals(
				newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("ftp://www.arakhne.org"))); //$NON-NLS-1$
		assertEquals(
				newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("ftp://www.arakhne.org/"))); //$NON-NLS-1$
		assertEquals(
				newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto"))); //$NON-NLS-1$
		assertEquals(
				newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto/"))); //$NON-NLS-1$
		assertEquals(
				newURL("ftp://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				newURL("ftp://www.arakhne.org/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$

		assertEquals(
				newURL("file:/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:/toto/titi/"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:/toto"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:/toto/"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:./toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:./toto/titi"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:./toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:./toto/titi/"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:./"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:./toto"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:./"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:./toto/"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:."))); //$NON-NLS-1$
		assertEquals(
				newURL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:./"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:toto"))); //$NON-NLS-1$
		assertEquals(
				newURL("file:../"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("file:toto/"))); //$NON-NLS-1$

		assertEquals(
				newURL("jar:file:test.jar!/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("jar:file:test.jar!/toto/titi"))); //$NON-NLS-1$
		assertEquals(
				newURL("jar:file:test.jar!/toto/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("jar:file:test.jar!/toto/titi/"))); //$NON-NLS-1$
		assertEquals(
				newURL("jar:file:test.jar!/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("jar:file:test.jar!/toto"))); //$NON-NLS-1$
		assertEquals(
				newURL("jar:file:test.jar!/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("jar:file:test.jar!/toto/"))); //$NON-NLS-1$
		assertEquals(
				newURL("jar:file:test.jar!/"),  //$NON-NLS-1$
				FileSystem.getParentURL(newURL("jar:file:test.jar!/"))); //$NON-NLS-1$

		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"),   //$NON-NLS-1$
				FileSystem.getParentURL(createFileInJarUrlWithSpaces()));
	}

	@Test
	@DisplayName("makeAbsolute(File, URL) - no root")
	public void makeAbsoluteFileURL_noRoot() throws Exception {
		assertNull(FileSystem.makeAbsolute((File)null, (URL)null));

		assertEquals(newURL("file:" + fromFileToUrl(getCurrentDir(), false) + "/toto"),  //$NON-NLS-1$ //$NON-NLS-2$
				FileSystem.makeAbsolute(new File("toto"), (URL)null)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("makeAbsolute(URL, File) - no root")
	public void makeAbsoluteURLFile_noRoot() throws Exception {
		assertNull(FileSystem.makeAbsolute((URL)null, (File)null));

		assertEquals(newURL("file:/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("file:/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(newURL("file:toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("file:toto"), (File)null)); //$NON-NLS-1$

		assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$

		assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$

		assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$

		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); //$NON-NLS-1$
		assertEquals(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("makeAbsolute(URL, File) - root")
	public void makeAbsoluteURLFile_root() throws Exception {
		File root = new File(File.separator+"myroot"); //$NON-NLS-1$

		assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("makeAbsolute(URL, URL) - no root")
	public void makeAbsoluteURLURL_notroot() throws Exception {
		assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));

		assertEquals(newURL("file:/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("file:/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(newURL("file:toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("file:toto"), (URL)null)); //$NON-NLS-1$

		assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$

		assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$

		assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$

		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
		assertEquals(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("makeAbsolute(URL, URL) - file as root")
	public void makeAbsoluteURLURL_fileAsRoot() throws Exception {
		URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("makeAbsolute(URL, URL) - http as root")
	public void makeAbsoluteURLURL_httpAsRoot() throws Exception {
		URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((URL)null, root));

		assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$

		assertEquals(newURL("jar:http://maven.arakhne.org/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("makeAbsolute(File, File) - no root")
	public void makeAbsoluteFileFile_noRoot() {
		assertNull(FileSystem.makeAbsolute((File)null, (File)null));

		assertEquals(new File(File.separator+"toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new File(File.separator+"toto"), (File)null)); //$NON-NLS-1$
		assertEquals(new File("toto"),  //$NON-NLS-1$
				FileSystem.makeAbsolute(new File("toto"), (File)null)); //$NON-NLS-1$
	}

	@Test
	@DisplayName("makeAbsolute(File, File) - root")
	public void makeAbsoluteFileFile_root() {
		File root = new File(File.separator+"myroot"); //$NON-NLS-1$

		assertNull(FileSystem.makeAbsolute((File)null, root));
	}

	@Test
	@DisplayName("jreBehaviorRelatedToURL")
	public void jreBehaviorRelatedToURL() throws Exception {
		// The following test permits to check if a specific behavior of URL
		// is still present in the JRE.
		URL rr = newURL("file://marbre.jpg"); //$NON-NLS-1$
		assertEquals("file", rr.getProtocol()); //$NON-NLS-1$
		assertEquals("marbre.jpg", rr.getAuthority()); //$NON-NLS-1$
		assertEquals("", rr.getPath()); //$NON-NLS-1$
	}

	@Test
	@DisplayName("convertURLToFile")
	public void convertURLToFile() throws Exception {
		try {
			FileSystem.convertURLToFile(newURL("http://www.arakhne.org")); //$NON-NLS-1$
			fail("not a file URL"); //$NON-NLS-1$
		}
		catch(IllegalArgumentException exception) {
			//
		}

		assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(newURL("file:./toto")).getCanonicalPath()); //$NON-NLS-1$

		assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(newURL("file:toto")).getCanonicalPath()); //$NON-NLS-1$

		assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(newURL("file:./abs/../toto")).getCanonicalPath()); //$NON-NLS-1$

		assertEquals(new File("/toto").getCanonicalPath(),  //$NON-NLS-1$
				FileSystem.convertURLToFile(newURL("file:/toto")).getCanonicalPath()); //$NON-NLS-1$
	}

	@Test
	@DisplayName("convertStringToURL")
	public void convertStringToURL() throws Exception {
		handlesResourceUrl();
		try {
			assertNull(FileSystem.convertStringToURL(null, true));
			assertNull(FileSystem.convertStringToURL("", true)); //$NON-NLS-1$
			assertNull(FileSystem.convertStringToURL(null, false));
			assertNull(FileSystem.convertStringToURL("", false)); //$NON-NLS-1$
	
			URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
			assertNotNull(rr);
			assertEquals("file", rr.getProtocol()); //$NON-NLS-1$
			assertEquals("", rr.getAuthority()); //$NON-NLS-1$
			assertEquals("", rr.getHost()); //$NON-NLS-1$
			assertNull(rr.getQuery());
			assertEquals("marbre.jpg", rr.getPath()); //$NON-NLS-1$
	
			assertEquals(newURL("http", "www.arakhne.org", "/"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("http://www.arakhne.org/", true)); //$NON-NLS-1$
			assertEquals(newURL("http", "www.arakhne.org", "/"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("http://www.arakhne.org/", false)); //$NON-NLS-1$
	
			// CAUTION: testing right-formed jar URL.
			assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); //$NON-NLS-1$
			assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); //$NON-NLS-1$
	
			// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
			assertEquals(newURL("file", "", "/home/test/j.jar"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("jar:/home/test/j.jar", true)); //$NON-NLS-1$
			assertEquals(newURL("file", "", "/home/test/j.jar"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("jar:/home/test/j.jar", false)); //$NON-NLS-1$
	
			// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
			assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); //$NON-NLS-1$
			assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); //$NON-NLS-1$
	
			URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			assertNotNull(testResource);
	
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
			assertEquals(newURL("file", "", "/org/arakhne/afc/vmutil/test.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$
	
			assertEquals(testResource,
					FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
			assertEquals(newURL("file","", "org/arakhne/afc/vmutil/test.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$
		} finally {
			unhandlesResourceUrl();
		}
	}

	@Test
	@DisplayName("join(URL, String...)")
	public void joinURLStringArray() throws Exception {
		assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"), FileSystem.join(createHttpUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto/a/b/c"), FileSystem.join(createFileUrlWithSpacesHardCoded(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrlWithSpaces(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	@DisplayName("join(URL, File...)")
	public void joinURLFileArray() throws Exception {
		assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"), //$NON-NLS-1$
				FileSystem.join(createHttpUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
				FileSystem.join(createFileInJarUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto/a/b/c"), //$NON-NLS-1$
				FileSystem.join(createFileUrlWithSpacesHardCoded(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
				FileSystem.join(createFileInJarUrlWithSpaces(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Test
	@DisplayName("split(URL)")
	public void splitURL() throws Exception {
		assertArrayEquals(new String[] {"", "path", "to", "file.x.z.z"}, FileSystem.split(createHttpUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertArrayEquals(new String[] {"", "the path", "to", "file with space.toto"}, FileSystem.split(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrlWithSpaces())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertArrayEquals(new String[] {"", "a.b.c"}, FileSystem.split(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {""}, FileSystem.split(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("extensions(URL)")
	public void extensionsURL() throws Exception {
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createHttpUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrlWithSpaces())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[0], FileSystem.extensions(newURL("file://"))); //$NON-NLS-1$
	}

	@Test
	@DisplayName("extensions(String)")
	public void extensionsString() {
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertArrayEquals(new String[0], FileSystem.extensions("file://")); //$NON-NLS-1$

		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("extension(URL)")
	public void extensionURL() throws Exception {
		assertEquals(".z", FileSystem.extension(createHttpUrl())); //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(createFileInJarUrl())); //$NON-NLS-1$
		assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		assertEquals(".z", FileSystem.extension(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		assertEquals(".c", FileSystem.extension(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.extension(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("extension(String)")
	public void extensionString() {
		assertEquals(".z", FileSystem.extension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".z", FileSystem.extension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".c", FileSystem.extension("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".c", FileSystem.extension("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".c", FileSystem.extension("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.extension("file://")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(".dae", FileSystem.extension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".dae", FileSystem.extension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(".dae", FileSystem.extension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("hasExtension(URL, String)")
	public void hasExtensionURL() throws Exception {
		assertTrue(FileSystem.hasExtension(createHttpUrl(), ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createHttpUrl(), "z")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createHttpUrl(), ".c")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrl(), ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrl(), "z")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createFileInJarUrl(), ".c")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".toto")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), "toto")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".zip")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".z")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), "z")); //$NON-NLS-1$
		assertFalse(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".c")); //$NON-NLS-1$
		assertTrue(FileSystem.hasExtension(newURL("file:///a.b.c/"), ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension(newURL("file:///a.b.c/"), "c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension(newURL("file:///a.b.c/"), ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension(newURL("file://"), ".c")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("hasExtension(String, String)")
	public void hasExtensionString() {
		assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:///a.b.c/", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:///a.b.c/", "c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:///a.b.c/", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:a.b.c", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:a.b.c", "c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:a.b.c", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("a.b.c", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("a.b.c", "c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("a.b.c", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file://", ".z")); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("removeExtension(URL)")
	public void removeExtensionURL() throws Exception {
		assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z?toto#frag"), FileSystem.removeExtension(createHttpUrl())); //$NON-NLS-1$
		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrl())); //$NON-NLS-1$
		assertEquals(newURL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		assertEquals(newURL("file:///a.b"), FileSystem.removeExtension(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("file", "", ""), FileSystem.removeExtension(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test
	@DisplayName("replaceExtension(URL, String)")
	public void replaceExtensionURLString() throws Exception {
		assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.xyz?toto#frag"), FileSystem.replaceExtension(createHttpUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesHardCoded(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrlWithSpaces(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("file:///a.b.xyz"), FileSystem.replaceExtension(newURL("file:///a.b.c/"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(newURL("file", "", ""), FileSystem.replaceExtension(newURL("file://"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Test
	@DisplayName("addExtension(URL, String)")
	public void addExtensionURLString() throws Exception {
		assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z.xyz?toto#frag"), FileSystem.addExtension(createHttpUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesHardCoded(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrlWithSpaces(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(newURL("file:///a.b.c.xyz"), FileSystem.addExtension(newURL("file:///a.b.c/"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(newURL("file", "", ""), FileSystem.addExtension(newURL("file://"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Test
	@DisplayName("basename(URL)")
	public void basenameURL() throws Exception {
		assertEquals("file.x.z", FileSystem.basename(createHttpUrl())); //$NON-NLS-1$
		assertEquals("file.x.z", FileSystem.basename(createFileInJarUrl())); //$NON-NLS-1$
		assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		assertEquals("file.x.z", FileSystem.basename(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		assertEquals("a.b", FileSystem.basename(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.basename(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$

		URL url = newURL("file", "", "D:\\vivus_test\\export dae\\yup\\terrain_physx.dae");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			assertEquals("terrain_physx", FileSystem.basename(url)); //$NON-NLS-1$
			fail("expecting assertion failure"); //$NON-NLS-1$
		}
		catch(AssertionError exception) {
			//
		}
	}

	@Test
	@DisplayName("basename(String)")
	public void basenameString() {
		assertEquals("file.x.z", FileSystem.basename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z", FileSystem.basename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b", FileSystem.basename("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b", FileSystem.basename("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b", FileSystem.basename("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.basename("file://")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("terrain_physx", FileSystem.basename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.basename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.basename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("largeBasename(URL)")
	public void largeBasenameURL() throws Exception {
		assertEquals("file.x.z.z", FileSystem.largeBasename(createHttpUrl())); //$NON-NLS-1$
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrl())); //$NON-NLS-1$
		assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		assertEquals("a.b.c", FileSystem.largeBasename(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.largeBasename(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("largeBasename(String)")
	public void largeBasenameString() {
		assertEquals("file.x.z.z", FileSystem.largeBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b.c", FileSystem.largeBasename("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b.c", FileSystem.largeBasename("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a.b.c", FileSystem.largeBasename("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.largeBasename("file://")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("terrain_physx.dae", FileSystem.largeBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("shortBasename(URL)")
	public void shortBasenameURL() throws Exception {
		assertEquals("file", FileSystem.shortBasename(createHttpUrl())); //$NON-NLS-1$
		assertEquals("file", FileSystem.shortBasename(createFileInJarUrl())); //$NON-NLS-1$
		assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		assertEquals("file", FileSystem.shortBasename(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		assertEquals("a", FileSystem.shortBasename(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.shortBasename(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("shortBasename(String)")
	public void shortBasenameString() {
		assertEquals("file", FileSystem.shortBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("file", FileSystem.shortBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a", FileSystem.shortBasename("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a", FileSystem.shortBasename("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a", FileSystem.shortBasename("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", FileSystem.shortBasename("file://")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("terrain_physx", FileSystem.shortBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.shortBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("terrain_physx", FileSystem.shortBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("toShortestURL(URL)")
	public void toShortestURLURL() throws Exception {
		handlesResourceUrl();
		try {
			File f1 = new File("/toto"); //$NON-NLS-1$
			URL u1 = f1.toURI().toURL();
			URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
			URL u2e = newURL("resource:org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
	
			URL actual;
	
			actual = FileSystem.toShortestURL(u1);
			assertEquals(u1, actual);
	
			actual = FileSystem.toShortestURL(u2);
			assertEquals(u2e, actual);
		} finally {
			unhandlesResourceUrl();
		}
	}


	@Test
	@DisplayName("makeRelative(URL, URL)")
	public void makeRelativeURLURL() throws Exception {
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



		root = FileSystem.join(FileSystem.getUserHomeDirectory().toURI().toURL(), "zz", "abc"); //$NON-NLS-1$ //$NON-NLS-2$

		abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc").toURI().toURL(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+FileSystem.PARENT_DIRECTORY+File.separator
				+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(rel, FileSystem.makeRelative(abs, root));
	}

	@Test
	@DisplayName("makeCanonical(URL)")
	public void makeCanonicalURL() throws Exception {
		assertEquals(
				createHttpUrl(), 
				FileSystem.makeCanonicalURL(createHttpUrl()));

		assertEquals(
				createFileInJarUrl(), 
				FileSystem.makeCanonicalURL(createFileInJarUrl()));

		assertEquals(
				newURL("file:/a/b/c/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(newURL("file:/a/b/./c/./d/e"))); //$NON-NLS-1$

		assertEquals(
				newURL("file:/a/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(newURL("file:/a/b/../c/../d/e"))); //$NON-NLS-1$

		assertEquals(
				newURL("file:/a/b/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(newURL("file:/a/b/./c/../d/e"))); //$NON-NLS-1$

		assertEquals(
				newURL("file:../a/b/c/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(newURL("file:../a/b/./c/./d/e"))); //$NON-NLS-1$

		assertEquals(
				newURL("file:../a/c/d/e"),  //$NON-NLS-1$
				FileSystem.makeCanonicalURL(newURL("file:../a/b/../c/./d/e"))); //$NON-NLS-1$
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
			testDir = FileSystem.createTempDirectory("unittest", null); //$NON-NLS-1$
			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), testDir); //$NON-NLS-1$
			FileSystem.copy(FileSystemTest.class.getResource("test2.txt"), testDir); //$NON-NLS-1$
			File subdir = new File(testDir, "subdir"); //$NON-NLS-1$
			subdir.mkdirs();
			FileSystem.copy(FileSystemTest.class.getResource("test.txt"), subdir); //$NON-NLS-1$
			FileSystem.zipFile(testDir, testArchive);
		} finally {
			FileSystem.delete(testDir);
		}
	}

	@Test
	@DisplayName("zipFile(File)")
	public void zipFileFile() throws IOException {
		File testArchive = null;

		try {
			testArchive = File.createTempFile("unittest", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$

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
		} finally {
			FileSystem.delete(testArchive);
		}
	}

	@Test
	@DisplayName("unzipFile(File)")
	public void testUnzipFileFile() throws IOException {
		File testArchive = null;
		try {
			testArchive = File.createTempFile("unittest", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$

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
		} finally {
			FileSystem.delete(testArchive);
		}
	}

	@Test
	@DisplayName("getFileExtensionCharacter")
	public void getFileExtensionCharacter() {
		assertInlineParameterUsage(FileSystem.class, "getFileExtensionCharacter"); //$NON-NLS-1$
	}

	@Test
	@DisplayName("isWindowsNativeFilename")
	public void isWindowNativeFilename() {
		assertFalse(FileSystem.isWindowsNativeFilename("D:/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
		assertTrue(FileSystem.isWindowsNativeFilename("D|\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
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

	@Test
	@DisplayName("normalizeWindowsNativeFilename")
	public void normalizeWindowNativeFilename() {
		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$

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

		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(FileSystem.normalizeWindowsNativeFilename("c.txt")); //$NON-NLS-1$
		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file:c.txt")); //$NON-NLS-1$
		assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(FileSystem.normalizeWindowsNativeFilename("file://c.txt")); //$NON-NLS-1$
	}

	@Test
	@DisplayName("convertStringToFile(String)")
	public void convertStringToFile() {
		assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/", FileSystem.convertStringToFile("/")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//", FileSystem.convertStringToFile("\\\\")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("////vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file://\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file://\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:/C:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:/C:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:///a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("host/a/b/c.txt", FileSystem.convertStringToFile("file://host/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:////host/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$

		assertNormedFile("C:c.txt", FileSystem.convertStringToFile("C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("c.txt", FileSystem.convertStringToFile("c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file:C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("c.txt", FileSystem.convertStringToFile("file:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file://C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNormedFile("c.txt", FileSystem.convertStringToFile("file://c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("isJarURL(URL)")
	public void isJarURLURL() throws Exception {
		assertFalse(FileSystem.isJarURL(createHttpUrl()));
		assertTrue(FileSystem.isJarURL(createFileInJarUrl()));
		assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesHardCoded()));  

		assertInlineParameterUsage(FileSystem.class, "isJarURL", URL.class); //$NON-NLS-1$
	}

	@Test
	@DisplayName("getJarURL(URL)")
	public void getJarURLURL() throws Exception {
		assertNull(FileSystem.getJarURL(createHttpUrl()));
		assertEquals(newURL("file:" + createJarFilenameForUrl()), FileSystem.getJarURL(createFileInJarUrl())); //$NON-NLS-1$

		assertEquals(newURL("file:" + createJarFilenameForUrlWithSpaces()), FileSystem.getJarURL(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		assertNull(FileSystem.getJarFile(createFileUrlWithSpacesHardCoded()));
	}

	@Test
	@DisplayName("getJarFile(URL)")
	public void getJarFileURL() throws Exception {
		assertNull(FileSystem.getJarFile(createHttpUrl()));
		assertNormedFile(createInJarFilename(), FileSystem.getJarFile(createFileInJarUrl()));

		assertNormedFile(createInJarFilename(), FileSystem.getJarFile(createFileInJarUrlWithSpaces()));  
		assertNull(FileSystem.getJarFile(createFileUrlWithSpacesHardCoded()));
	}

	@Test
	@DisplayName("dirname(URL)")
	public void dirnameURL() throws Exception {
		assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/"), //$NON-NLS-1$
				FileSystem.dirname(createHttpUrl()));
		assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/"), //$NON-NLS-1$
				FileSystem.dirname(createFileInJarUrl()));
		assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"), //$NON-NLS-1$
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
				if (addRootSlash && Pattern.matches("^[a-zA-Z]\\:.*$", fn)) { //$NON-NLS-1$
					fn = File.separator + fn;
				}
			} else {
				fn = filename.replaceAll(Pattern.quote(FileSystem.UNIX_SEPARATOR_STRING),
						Matcher.quoteReplacement(File.separator));
				if (addRootSlash && !fn.startsWith(File.separator)) {
					fn = File.separator + fn;
				}
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
		protected URL createAbsoluteStandardFileUrl() throws Exception {
			return newURL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false)); //$NON-NLS-1$
		}

		/** @return "file:/home" or "file:C:\home" or "file:/home"
		 */
		protected URL createAbsoluteFolderUrl() throws Exception {
			return newURL("file:" + fromFileToUrl(getAbsoluteFoldername(), false)); //$NON-NLS-1$
		}

		/** @return "/the path/to/file with space.toto" or "C:\the path\to\file with space.toto" or "/the path/to/file with space.toto"
		 */
		protected URL createFileUrlWithSpacesWithFile(boolean replaceSpaceChars, boolean addRootSlash) throws Exception {
			String filename = getStandardFilenameWithSpaces();
			String fn;
			if (getOS() == OperatingSystem.WIN) {
				// A root slash is mandatory because the path starts with a disk name.
				fn = filename.replaceAll(Pattern.quote(FileSystem.WINDOWS_SEPARATOR_STRING),
						Matcher.quoteReplacement(FileSystem.URL_PATH_SEPARATOR));
				if (addRootSlash) {
					fn = "/" + fn; //$NON-NLS-1$
				}
			} else {
				fn = filename.replaceAll(Pattern.quote(FileSystem.UNIX_SEPARATOR_STRING),
						Matcher.quoteReplacement(FileSystem.URL_PATH_SEPARATOR));
			}
			if (replaceSpaceChars) {
				fn = fn.replaceAll(Pattern.quote(" "), //$NON-NLS-1$
						Matcher.quoteReplacement("%20")); //$NON-NLS-1$
			}
			return newURL("file:" + fn); //$NON-NLS-1$
		}

		@Test
		@DisplayName("isJarURL(URL)")
		public void isJarURLURL() throws Exception {
			assertFalse(FileSystem.isJarURL(createAbsoluteStandardFileUrl()));
			assertFalse(FileSystem.isJarURL(createAbsoluteFolderUrl()));
			assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesWithFile(true, true)));
		}

		@Test
		@DisplayName("getJarURL(URL)")
		public void getJarURLURL() throws Exception {
			assertNull(FileSystem.getJarURL(createAbsoluteStandardFileUrl()));
			assertNull(FileSystem.getJarURL(createAbsoluteFolderUrl()));
		}

		@Test
		@DisplayName("getJarFile(URL)")
		public void getJarFileURL() throws Exception {
			assertNull(FileSystem.getJarFile(createAbsoluteStandardFileUrl()));
			assertNull(FileSystem.getJarFile(createAbsoluteFolderUrl()));
			assertNull(FileSystem.getJarFile(createFileUrlWithSpacesWithFile(true, true)));
		}

		@Test
		@DisplayName("toJarURL(File, String)")
		public void toJarURLFileString() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							getAbsoluteStandardFilename()));
		}

		@Test
		@DisplayName("toJarURL(File, File)")
		public void toJarURLFileFile() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							newFile(getAbsoluteStandardFilename(), true)));
		}

		@Test
		@DisplayName("toJarURL(URL, String)")
		public void toJarURLURLString() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							getAbsoluteStandardFilename()));
		}

		@Test
		@DisplayName("toJarURL(URL, File)")
		public void toJarURLURLFile() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							newFile(getAbsoluteStandardFilename(), true)));
		}

		@Test
		@DisplayName("dirname(File)")
		public void dirnameFile() throws Exception {
			assertEquals(newURL("file", "", fromFileToUrl(getAbsoluteFoldername(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(newFile(getAbsoluteStandardFilename(), false)));
			assertEquals(newURL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(newFile(getAbsoluteFoldername(), false)));
		}

		@Test
		@DisplayName("dirname(URL)")
		public void dirnameURL() throws Exception {
			assertEquals(newURL("file", "", fromFileToUrl(getAbsoluteFoldername(), false) + "/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.dirname(createAbsoluteStandardFileUrl()));
			assertEquals(newURL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(createAbsoluteFolderUrl()));
		}

		@Test
		@DisplayName("split(File)")
		public void splitFile() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$ //$NON-NLS-2$
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "the path", "to", "file with space.toto"}, FileSystem.split(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("split(URL)")
		public void splitURL() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(createAbsoluteStandardFileUrl())); //$NON-NLS-1$ //$NON-NLS-2$
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("extensions(URL)")
		public void extensionsURL() throws Exception {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createAbsoluteStandardFileUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertArrayEquals(new String[0], FileSystem.extensions(createAbsoluteFolderUrl()));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("extensions(File)")
		public void extensionsFile() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertArrayEquals(new String[0], FileSystem.extensions(newFile(getAbsoluteFoldername(), false)));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("extensions(String)")
		public void extensionsString() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(getAbsoluteStandardFilename())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertArrayEquals(new String[0], FileSystem.extensions(getAbsoluteFoldername()));
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("extension(URL)")
		public void extensionURL() throws Exception {
			assertEquals(".z", FileSystem.extension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("", FileSystem.extension(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("extension(File)")
		public void extensionFile() {
			assertEquals(".z", FileSystem.extension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("", FileSystem.extension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(".toto", FileSystem.extension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("extension(String)")
		public void extensionString() {
			assertEquals(".z", FileSystem.extension(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("", FileSystem.extension(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals(".toto", FileSystem.extension(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("hasExtension(URL)")
		public void hasExtensionURL() throws Exception {
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), "z")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".c")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(createAbsoluteFolderUrl(), ".z")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(true, true), ".toto")); //$NON-NLS-1$
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(true, true), "toto")); //$NON-NLS-1$
			assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(true, true), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("hasExtension(File)")
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
		@DisplayName("hasExtension(String)")
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
		@DisplayName("basename(URL)")
		public void basenameURL() throws Exception {
			assertEquals("test.x.z", FileSystem.basename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("home", FileSystem.basename(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("basename(File)")
		public void basenameFile() {
			assertEquals("test.x.z", FileSystem.basename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("home", FileSystem.basename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.basename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("basename(String)")
		public void basenameString() {
			assertEquals("test.x.z", FileSystem.basename(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("home", FileSystem.basename(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.basename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("largeBasename(URL)")
		public void largeBasenameURL() throws Exception {
			assertEquals("test.x.z.z", FileSystem.largeBasename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("home", FileSystem.largeBasename(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("largeBasename(File)")
		public void largeBasenameFile() {
			assertEquals("test.x.z.z", FileSystem.largeBasename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("home", FileSystem.largeBasename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals("file with space.toto", FileSystem.largeBasename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("largeBasename(String)")
		public void largeBasenameString() {
			assertEquals("test.x.z.z", FileSystem.largeBasename(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("home", FileSystem.largeBasename(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals("file with space.toto", FileSystem.largeBasename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("shortBasename(URL)")
		public void shortBasenameURL() throws Exception {
			assertEquals("test", FileSystem.shortBasename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals("home", FileSystem.shortBasename(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("shortBasename(File)")
		public void shortBasenameFile() {
			assertEquals("test", FileSystem.shortBasename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals("home", FileSystem.shortBasename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.shortBasename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("shortBasename(String)")
		public void shortBasenameString() {
			assertEquals("test", FileSystem.shortBasename(getAbsoluteStandardFilename())); //$NON-NLS-1$
			assertEquals("home", FileSystem.shortBasename(getAbsoluteFoldername())); //$NON-NLS-1$
			assertEquals("file with space", FileSystem.shortBasename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("join(File, String...)")
		public void joinFileStringArray() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							"",  //$NON-NLS-1$
							"home",  //$NON-NLS-1$
							"test.x.z.z")); //$NON-NLS-1$
		}


		@Test
		@DisplayName("join(File, File...)")
		public void joinFileFileArray() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							new File("home"),  //$NON-NLS-1$
							new File("test.x.z.z"))); //$NON-NLS-1$
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							new File(File.separator+"home"),  //$NON-NLS-1$
							new File("test.x.z.z"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("join(URL, String...)")
		public void joinURLStringArray() throws Exception {
			URL base;

			base = newURL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = newURL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", true).toURI().toURL(); //$NON-NLS-1$
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(false, true), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("join(URL, File...)")
		public void joinURLFileArray() throws Exception {
			URL base;

			base = newURL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = newURL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", true).toURI().toURL(); //$NON-NLS-1$
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(true, true), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("convertStringToURL(String)")
		public void convertStringToURL() throws Exception {
			URL base;

			base = createAbsoluteStandardFileUrl();
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));

			base = createAbsoluteFolderUrl();
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));

			base = createFileUrlWithSpacesWithFile(true, true);
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));
		}

		@Test
		@DisplayName("convertURLToFile(URL)")
		public void convertURLToFile() throws Exception {
			File base;

			base = newFile(getAbsoluteStandardFilename(), false);
			assertEquals(base, FileSystem.convertURLToFile(createAbsoluteStandardFileUrl()));

			base = newFile(getAbsoluteFoldername(), false);
			assertEquals(base, FileSystem.convertURLToFile(createAbsoluteFolderUrl()));
		}

		@Test
		@DisplayName("convertFileToURL(File)")
		public void convertFileToURLFile() throws Exception {
			assertEquals(createAbsoluteStandardFileUrl(),  
					FileSystem.convertFileToURL(newFile(getAbsoluteStandardFilename(), true)));
			assertEquals(createAbsoluteFolderUrl(),
					FileSystem.convertFileToURL(newFile(getAbsoluteFoldername(), true)));
			assertEquals(createFileUrlWithSpacesWithFile(getOS() != OperatingSystem.WIN, false),  
					FileSystem.convertFileToURL(newFile(getStandardFilenameWithSpaces(), false)));
		}

		@Test
		@DisplayName("makeAbsolute(File, URL) - no root")
		public void makeAbsoluteFileURL_noRoot() throws Exception {
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator(), false) + "toto"),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("makeAbsolute(File, URL) - http as root")
		public void makeAbsoluteFileURL_httpAsRoot() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$

			assertEquals(newURL("http://maven.arakhne.org/myroot/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newFile("toto", false), root)); //$NON-NLS-1$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root)); //$NON-NLS-1$

			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "a/b/c", false)),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator()
							+ "a" + File.separator + "b" + File.separator + "c", true), root)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("makeAbsolute(File, URL) - file as root")
		public void makeAbsoluteFileURL_fileAsRoot() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$

			assertNull(FileSystem.makeAbsolute((File)null, root));

			assertEquals(newURL("file:/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new File("/toto"), root)); //$NON-NLS-1$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new File("toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("makeAbsolute(URL, URL) - http as root")
		public void makeAbsoluteURLURL_httpAsRoot() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$

			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("http://maven.arakhne.org/myroot/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("file:toto"), root)); //$NON-NLS-1$

			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("makeAbsolute(URL, URL) - file as root")
		public void makeAbsoluteURLURL_fileAsRoot() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$

			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:toto"), root)); //$NON-NLS-1$

			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$

			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("makeAbsolute(File, File) - root")
		public void makeAbsoluteFileFile_root() {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$

			assertEquals(newFile(getRootnameWithSeparator() + "toto", true),   //$NON-NLS-1$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root)); //$NON-NLS-1$

			assertEquals(newFile(getRootnameWithSeparator() + "myroot" + getSeparator() + "toto", true),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new File("toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("makeAbsolute(URL, File) - root")
		public void makeAbsoluteURLFile_root() throws Exception {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$

			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); //$NON-NLS-1$ //$NON-NLS-2$

			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:toto"), root)); //$NON-NLS-1$

			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$
			
			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
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
		@DisplayName("removeExtension(URL)")
		public void removeExtensionURL() throws Exception {
			assertEquals(newURL("file:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals(newURL("file:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals(newURL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("removeExtension(File)")
		public void removeExtensionFile() {
			assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals(new File("/home"), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(new File("/the path/to/file with space"), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("replaceExtension(URL, String)")
		public void replaceExtensionURL() throws Exception {
			assertEquals(newURL("file:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesWithFile(true, true), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("replaceExtension(File, String)")
		public void replaceExtensionFile() {
			assertEquals(new File("/home/test.x.z.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/home.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/the path/to/file with space.xyz"), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("addExtension(URL, String)")
		public void addExtensionURL() throws Exception {
			assertEquals(newURL("file:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesWithFile(true, true), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("addExtension(File, String)")
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
		@DisplayName("removeExtension(URL)")
		public void removeExtensionURL() throws Exception {
			assertEquals(newURL("file:/C:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals(newURL("file:/C:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("removeExtension(File)")
		public void removeExtensionFile() {
			assertEquals(newFile("C:\\home\\test.x.z", false), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals(newFile("C:\\home", false), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(newFile("C:\\the path\\to\\file with space", false), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("replaceExtension(URL, String)")
		public void replaceExtensionURL() throws Exception {
			assertEquals(newURL("file:/C:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/C:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("replaceExtension(File, String)")
		public void replaceExtensionFile() {
			assertEquals(newFile("C:\\home\\test.x.z.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newFile("C:\\home.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newFile("C:\\the path\\to\\file with space.xyz", false), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("addExtension(URL, String)")
		public void addExtensionURL() throws Exception {
			assertEquals(newURL("file:/C:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/C:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("addExtension(File, String)")
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
		@DisplayName("removeExtension(URL)")
		public void removeExtensionURL() throws Exception {
			assertEquals(newURL("file:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
			assertEquals(newURL("file:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl())); //$NON-NLS-1$
			assertEquals(newURL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("removeExtension(File)")
		public void removeExtensionFile() {
			assertEquals(new File("/home/test.x.z"), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
			assertEquals(new File("/home"), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
			assertEquals(new File("/the path/to/file with space"), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("replaceExtension(URL, String)")
		public void replaceExtensionURL() throws Exception {
			assertEquals(newURL("file:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesWithFile(true, true), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("replaceExtension(File, String)")
		public void replaceExtensionFile() {
			assertEquals(new File("/home/test.x.z.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/home.xyz"), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/the path/to/file with space.xyz"), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("addExtension(URL, String)")
		public void addExtensionURL() throws Exception {
			assertEquals(newURL("file:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesWithFile(true, true), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("replaceExtension(File, String)")
		public void addExtensionFile() {
			assertEquals(new File("/home/test.x.z.z.xyz"), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/home.xyz"), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(new File("/the path/to/file with space.toto.xyz"), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

}

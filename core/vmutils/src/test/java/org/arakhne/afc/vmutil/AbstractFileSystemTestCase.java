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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.arakhne.afc.vmutil.resource.Handler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("all")
public abstract class AbstractFileSystemTestCase {

	private boolean oldLibraryLoaderState;

	private static volatile TestingHandler handler;
	
	protected void handlesResourceUrl() throws Exception {
		if (FileSystem.class.getModule().getName() == null) {
			// Special testing framework that is not loading the library into a module.
			if (handler == null) {
				handler = new TestingHandler();
				URL.setURLStreamHandlerFactory(handler);
				handler.isActive = true;
			}
		}
	}

	protected void unhandlesResourceUrl() throws Exception {
		if (FileSystem.class.getModule().getName() == null) {
			// Special testing framework that is not loading the library into a module.
			if (handler != null) {
				handler.isActive = false;
			}
		}
	}

	protected void createZip(File testArchive) throws IOException {
		File testDir = null;
		try {
			testDir = FileSystem.createTempDirectory("unittest", null); //$NON-NLS-1$
			FileSystem.copy(AbstractFileSystemTestCase.class.getResource("test.txt"), testDir); //$NON-NLS-1$
			FileSystem.copy(AbstractFileSystemTestCase.class.getResource("test2.txt"), testDir); //$NON-NLS-1$
			File subdir = new File(testDir, "subdir"); //$NON-NLS-1$
			subdir.mkdirs();
			FileSystem.copy(AbstractFileSystemTestCase.class.getResource("test.txt"), subdir); //$NON-NLS-1$
			FileSystem.zipFile(testDir, testArchive);
		} finally {
			FileSystem.delete(testDir);
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

	protected static URL newURL(String url) throws Exception {
		return new URL(url);
	}
	
	protected static URL newURL(String protocol, String host, String path) throws Exception {
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

	protected String readInputStream(InputStream is) throws IOException {
		StringBuilder b = new StringBuilder();
		byte[] buffer = new byte[2048];
		int len;
		while ((len=is.read(buffer))>0) {
			b.append(new String(buffer, 0, len));
		}
		is.close();
		return b.toString();
	}

}

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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public abstract class AbstractSpecificFileSystemTestCase extends AbstractFileSystemTestCase {

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

	@DisplayName("isJarURL(URL)")
	@Nested
	public class IsJarURL {

		@DisplayName("#1")
		@Test
		public void isJarURLURL_1() throws Exception {
			assertFalse(FileSystem.isJarURL(createAbsoluteStandardFileUrl()));
		}

		@DisplayName("#2")
		@Test
		public void isJarURLURL_2() throws Exception {
			assertFalse(FileSystem.isJarURL(createAbsoluteFolderUrl()));
		}

		@DisplayName("#3")
		@Test
		public void isJarURLURL_3() throws Exception {
			assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesWithFile(true, true)));
		}
	}

	@DisplayName("getJarURL")
	@Nested
	public class GetJarURL {

		@DisplayName("#1")
		@Test
		public void getJarURLURL_1() throws Exception {
			assertNull(FileSystem.getJarURL(createAbsoluteStandardFileUrl()));
		}

		@DisplayName("#2")
		@Test
		public void getJarURLURL_2() throws Exception {
			assertNull(FileSystem.getJarURL(createAbsoluteFolderUrl()));
		}
	}

	@DisplayName("getJarFile")
	@Nested
	public class GetJarFile {

		@DisplayName("#1")
		@Test
		public void getJarFileURL_1() throws Exception {
			assertNull(FileSystem.getJarFile(createAbsoluteStandardFileUrl()));
		}

		@DisplayName("#2")
		@Test
		public void getJarFileURL_2() throws Exception {
			assertNull(FileSystem.getJarFile(createAbsoluteFolderUrl()));
		}

		@DisplayName("#3")
		@Test
		public void getJarFileURL_3() throws Exception {
			assertNull(FileSystem.getJarFile(createFileUrlWithSpacesWithFile(true, true)));
		}
	}

	@DisplayName("toJarURL")
	@Nested
	public class ToJarURL {

		@DisplayName("(File,String) #1")
		@Test
		public void toJarURLFileString_1() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							getAbsoluteStandardFilename()));
		}
	
		@Test
		@DisplayName("(File,File) #1")
		public void toJarURLFileFile_1() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true),
							newFile(getAbsoluteStandardFilename(), true)));
		}
	
		@Test
		@DisplayName("(URL,String) #1")
		public void toJarURLURLString_1() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ removeRootSlash(getAbsoluteStandardFilename())),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							getAbsoluteStandardFilename()));
		}
	
		@Test
		@DisplayName("(URL, File) #1")
		public void toJarURLURLFile_1() throws Exception {
			assertEquals(newURL("jar:file:" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), false)
					+ "!/" //$NON-NLS-1$
					+ fromFileToUrl(getAbsoluteStandardFilename(), true)),
					FileSystem.toJarURL(newFile(getAbsoluteStandardFilename(), true).toURI().toURL(),
							newFile(getAbsoluteStandardFilename(), true)));
		}
	}

	@DisplayName("dirname")
	@Nested
	public class Dirname {

		@DisplayName("(File) #1")
		@Test
		public void dirnameFile_1() throws Exception {
			assertEquals(newURL("file", "", fromFileToUrl(getAbsoluteFoldername(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(newFile(getAbsoluteStandardFilename(), false)));
		}

		@DisplayName("(File) #2")
		@Test
		public void dirnameFile_2() throws Exception {
			assertEquals(newURL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(newFile(getAbsoluteFoldername(), false)));
		}

		@Test
		@DisplayName("(URL) #1")
		public void dirnameURL_1() throws Exception {
			assertEquals(newURL("file", "", fromFileToUrl(getAbsoluteFoldername(), false) + "/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					FileSystem.dirname(createAbsoluteStandardFileUrl()));
		}

		@Test
		@DisplayName("(URL) #2")
		public void dirnameURL_2() throws Exception {
			assertEquals(newURL("file", "", fromFileToUrl(getRootnameWithSeparator(), false)), //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.dirname(createAbsoluteFolderUrl()));
		}
	}

	@DisplayName("split")
	@Nested
	public class Split {

		@DisplayName("(File) #1")
		@Test
		public void splitFile_1() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(File) #2")
		@Test
		public void splitFile_2() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
		}

		@DisplayName("(File) #3")
		@Test
		public void splitFile_3() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "the path", "to", "file with space.toto"}, FileSystem.split(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	
		@Test
		@DisplayName("(URL) #1")
		public void splitURL_1() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home", "test.x.z.z"}, FileSystem.split(createAbsoluteStandardFileUrl())); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL) #2")
		public void splitURL_2() throws Exception {
			assertArrayEquals(new String[] {getRootnameWithoutSeparator(), "home"}, FileSystem.split(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}
	}

	@DisplayName("extensions")
	@Nested
	public class Extensions {

		@DisplayName("(URL) #1")
		@Test
		public void extensionsURL_1() throws Exception {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createAbsoluteStandardFileUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@DisplayName("(URL) #2")
		@Test
		public void extensionsURL_2() throws Exception {
			assertArrayEquals(new String[0], FileSystem.extensions(createAbsoluteFolderUrl()));
		}

		@DisplayName("(URL) #3")
		@Test
		public void extensionsURL_3() throws Exception {
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(File) #1")
		public void extensionsFile_() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		@Test
		@DisplayName("(File) #2")
		public void extensionsFile_2() {
			assertArrayEquals(new String[0], FileSystem.extensions(newFile(getAbsoluteFoldername(), false)));
		}
		
		@Test
		@DisplayName("(File) #3")
		public void extensionsFile_3() {
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void extensionsString_1() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(getAbsoluteStandardFilename())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void extensionsString_2() {
			assertArrayEquals(new String[0], FileSystem.extensions(getAbsoluteFoldername()));
		}
		
		@Test
		@DisplayName("(String) #3")
		public void extensionsString_3() {
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}
	}

	@DisplayName("extension")
	@Nested
	public class Extension {

		@DisplayName("(URL) #1")
		@Test
		public void extensionURL_1() throws Exception {
			assertEquals(".z", FileSystem.extension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #2")
		@Test
		public void extensionURL_2() throws Exception {
			assertEquals("", FileSystem.extension(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #3")
		@Test
		public void extensionURL_3() throws Exception {
			assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(File) #1")
		public void extensionFile_1() {
			assertEquals(".z", FileSystem.extension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(File) #2")
		public void extensionFile_2() {
			assertEquals("", FileSystem.extension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(File) #3")
		public void extensionFile_3() {
			assertEquals(".toto", FileSystem.extension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void extensionString_1() {
			assertEquals(".z", FileSystem.extension(getAbsoluteStandardFilename())); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void extensionString_2() {
			assertEquals("", FileSystem.extension(getAbsoluteFoldername())); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void extensionString_3() {
			assertEquals(".toto", FileSystem.extension(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}
	}

	@DisplayName("hasExtension")
	@Nested
	public class HasExtension {

		@DisplayName("(URL) #1")
		@Test
		public void hasExtensionURL_1() throws Exception {
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".z")); //$NON-NLS-1$
		}

		@DisplayName("(URL) #2")
		@Test
		public void hasExtensionURL_2() throws Exception {
			assertTrue(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), "z")); //$NON-NLS-1$
		}

		@DisplayName("(URL) #3")
		@Test
		public void hasExtensionURL_3() throws Exception {
			assertFalse(FileSystem.hasExtension(createAbsoluteStandardFileUrl(), ".c")); //$NON-NLS-1$
		}

		@DisplayName("(URL) #4")
		@Test
		public void hasExtensionURL_4() throws Exception {
			assertFalse(FileSystem.hasExtension(createAbsoluteFolderUrl(), ".z")); //$NON-NLS-1$
		}

		@DisplayName("(URL) #5")
		@Test
		public void hasExtensionURL_5() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(true, true), ".toto")); //$NON-NLS-1$
		}

		@DisplayName("(URL) #6")
		@Test
		public void hasExtensionURL_6() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(true, true), "toto")); //$NON-NLS-1$
		}

		@DisplayName("(URL) #7")
		@Test
		public void hasExtensionURL_7() throws Exception {
			assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesWithFile(true, true), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #1")
		public void hasExtensionFile_1() {
			assertTrue(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #2")
		public void hasExtensionFile_2() {
			assertTrue(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), "z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #3")
		public void hasExtensionFile_3() {
			assertFalse(FileSystem.hasExtension(newFile(getAbsoluteStandardFilename(), false), ".c")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #4")
		public void hasExtensionFile_4() {
			assertFalse(FileSystem.hasExtension(newFile(getAbsoluteFoldername(), false), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #5")
		public void hasExtensionFile_5() {
			assertTrue(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), ".toto")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #6")
		public void hasExtensionFile_6() {
			assertTrue(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), "toto")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #7")
		public void hasExtensionFile_7() {
			assertFalse(FileSystem.hasExtension(newFile(getStandardFilenameWithSpaces(), false), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #1")
		public void hasExtensionString_1() {
			assertTrue(FileSystem.hasExtension(getAbsoluteStandardFilename(), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #2")
		public void hasExtensionString_2() {
			assertTrue(FileSystem.hasExtension(getAbsoluteStandardFilename(), "z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #3")
		public void hasExtensionString_3() {
			assertFalse(FileSystem.hasExtension(getAbsoluteStandardFilename(), ".c")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #4")
		public void hasExtensionString_4() {
			assertFalse(FileSystem.hasExtension(getAbsoluteFoldername(), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #5")
		public void hasExtensionString_5() {
			assertTrue(FileSystem.hasExtension(getStandardFilenameWithSpaces(), ".toto")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #6")
		public void hasExtensionString_6() {
			assertTrue(FileSystem.hasExtension(getStandardFilenameWithSpaces(), "toto")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #7")
		public void hasExtensionString_7() {
			assertFalse(FileSystem.hasExtension(getStandardFilenameWithSpaces(), ".c")); //$NON-NLS-1$
		}
	}

	@DisplayName("basename")
	@Nested
	public class Basename {

		@DisplayName("(URL) #1")
		@Test
		public void basenameURL_1() throws Exception {
			assertEquals("test.x.z", FileSystem.basename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #2")
		@Test
		public void basenameURL_2() throws Exception {
			assertEquals("home", FileSystem.basename(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #3")
		@Test
		public void basenameURL_3() throws Exception {
			assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(File) #1")
		public void basenameFile_1() {
			assertEquals("test.x.z", FileSystem.basename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(File) #2")
		public void basenameFile_2() {
			assertEquals("home", FileSystem.basename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(File) #3")
		public void basenameFile_3() {
			assertEquals("file with space", FileSystem.basename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void basenameString_1() {
			assertEquals("test.x.z", FileSystem.basename(getAbsoluteStandardFilename())); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void basenameString_2() {
			assertEquals("home", FileSystem.basename(getAbsoluteFoldername())); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void basenameString_3() {
			assertEquals("file with space", FileSystem.basename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}
	}

	@DisplayName("largeBasename")
	@Nested
	public class LargeBasename {

		@DisplayName("(URL) #1")
		@Test
		public void largeBasenameURL_1() throws Exception {
			assertEquals("test.x.z.z", FileSystem.largeBasename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #2")
		@Test
		public void largeBasenameURL_2() throws Exception {
			assertEquals("home", FileSystem.largeBasename(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #3")
		@Test
		public void largeBasenameURL_3() throws Exception {
			assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(File) #1")
		public void largeBasenameFile_1() {
			assertEquals("test.x.z.z", FileSystem.largeBasename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(File) #2")
		public void largeBasenameFile_2() {
			assertEquals("home", FileSystem.largeBasename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(File) #3")
		public void largeBasenameFile_3() {
			assertEquals("file with space.toto", FileSystem.largeBasename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void largeBasenameString_1() {
			assertEquals("test.x.z.z", FileSystem.largeBasename(getAbsoluteStandardFilename())); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void largeBasenameString_2() {
			assertEquals("home", FileSystem.largeBasename(getAbsoluteFoldername())); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void largeBasenameString_3() {
			assertEquals("file with space.toto", FileSystem.largeBasename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}
	}

	@DisplayName("shortBasename")
	@Nested
	public class ShortBasename {

		@DisplayName("(URL) #1")
		@Test
		public void shortBasenameURL_1() throws Exception {
			assertEquals("test", FileSystem.shortBasename(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #2")
		@Test
		public void shortBasenameURL_2() throws Exception {
			assertEquals("home", FileSystem.shortBasename(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #3")
		@Test
		public void shortBasenameURL_3() throws Exception {
			assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesWithFile(true, true))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #1")
		public void shortBasenameFile_1() {
			assertEquals("test", FileSystem.shortBasename(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #2")
		public void shortBasenameFile_2() {
			assertEquals("home", FileSystem.shortBasename(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #3")
		public void shortBasenameFile_3() {
			assertEquals("file with space", FileSystem.shortBasename(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #1")
		public void shortBasenameString_1() {
			assertEquals("test", FileSystem.shortBasename(getAbsoluteStandardFilename())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #2")
		public void shortBasenameString_2() {
			assertEquals("home", FileSystem.shortBasename(getAbsoluteFoldername())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(String) #3")
		public void shortBasenameString_3() {
			assertEquals("file with space", FileSystem.shortBasename(getStandardFilenameWithSpaces())); //$NON-NLS-1$
		}
	}

	@DisplayName("join")
	@Nested
	public class Join {

		@Test
		@DisplayName("(File, String...)")
		public void joinFileStringArray() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							"",  //$NON-NLS-1$
							"home",  //$NON-NLS-1$
							"test.x.z.z")); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(File, File...) #1")
		public void joinFileFileArray_1() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							new File("home"),  //$NON-NLS-1$
							new File("test.x.z.z"))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(File, File...) #2")
		public void joinFileFileArray_2() {
			File base = newFile(getAbsoluteFoldername(), false);
			assertEquals(new File(new File(base, "home"), "test.x.z.z"),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.join(base,
							new File(File.separator+"home"),  //$NON-NLS-1$
							new File("test.x.z.z"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String...) #1")
		public void joinURLStringArray_1() throws Exception {
			var base = newURL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	
		@Test
		@DisplayName("(URL, String...) #2")
		public void joinURLStringArray_2() throws Exception {
			var base = newURL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, String...) #3")
		public void joinURLStringArray_3() throws Exception {
			var base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", true).toURI().toURL(); //$NON-NLS-1$
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(false, true), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, File...) #1")
		public void joinURLFileArray_1() throws Exception {
			var base = newURL("file:" + fromFileToUrl(getAbsoluteStandardFilename(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteStandardFileUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, File...) #2")
		public void joinURLFileArray_2() throws Exception {
			var base = newURL("file:" + fromFileToUrl(getAbsoluteFoldername(), false) + "/a/b/c"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(base, FileSystem.join(createAbsoluteFolderUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, File...) #3")
		public void joinURLFileArray_3() throws Exception {
			var base = newFile(getStandardFilenameWithSpaces() + "/a/b/c", true).toURI().toURL(); //$NON-NLS-1$
			assertEquals(base, FileSystem.join(createFileUrlWithSpacesWithFile(true, true), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	@DisplayName("convertStringToURL(String)")
	@Nested
	public class ConvertStringToURL {

		@Test
		@DisplayName("#1")
		public void convertStringToURL_1() throws Exception {
			var base = createAbsoluteStandardFileUrl();
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));
		}

		@Test
		@DisplayName("#2")
		public void convertStringToURL_2() throws Exception {
			var base = createAbsoluteFolderUrl();
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));
		}

		@Test
		@DisplayName("#3")
		public void convertStringToURL() throws Exception {
			var base = createFileUrlWithSpacesWithFile(true, true);
			assertEquals(base, FileSystem.convertStringToURL(base.toString(), false));
		}
	}

	@DisplayName("convertURLToFile")
	@Nested
	public class ConvertURLToFile {

		@Test
		@DisplayName("#1")
		public void convertURLToFile_1() throws Exception {
			var base = newFile(getAbsoluteStandardFilename(), false);
			assertEquals(base, FileSystem.convertURLToFile(createAbsoluteStandardFileUrl()));
		}

		@Test
		@DisplayName("#2")
		public void convertURLToFile_2() throws Exception {
			var base = newFile(getAbsoluteFoldername(), false);
			assertEquals(base, FileSystem.convertURLToFile(createAbsoluteFolderUrl()));
		}
	}

	@DisplayName("convertFileToURL")
	@Nested
	public class ConvertFileToURL {

		@Test
		@DisplayName("#1")
		public void convertFileToURLFile_1() throws Exception {
			assertEquals(createAbsoluteStandardFileUrl(),  
					FileSystem.convertFileToURL(newFile(getAbsoluteStandardFilename(), true)));
		}

		@Test
		@DisplayName("#2")
		public void convertFileToURLFile_2() throws Exception {
			assertEquals(createAbsoluteFolderUrl(),
					FileSystem.convertFileToURL(newFile(getAbsoluteFoldername(), true)));
		}

		@Test
		@DisplayName("#3")
		public void convertFileToURLFile_3() throws Exception {
			assertEquals(createFileUrlWithSpacesWithFile(getOS() != OperatingSystem.WIN, false),  
					FileSystem.convertFileToURL(newFile(getStandardFilenameWithSpaces(), false)));
		}
	}

	@DisplayName("makeAbsolute")
	@Nested
	public class MakeAbsolute {

		@DisplayName("(File, URL) - no root")
		@Test
		public void makeAbsoluteFileURL_noRoot() throws Exception {
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator(), false) + "toto"),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, URL) - http as root #1")
		public void makeAbsoluteFileURL_httpAsRoot_1() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertEquals(newURL("http://maven.arakhne.org/myroot/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newFile("toto", false), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, URL) - http as root #2")
		public void makeAbsoluteFileURL_httpAsRoot_2() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, URL) - http as root #3")
		public void makeAbsoluteFileURL_httpAsRoot_3() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "a/b/c", false)),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator()
							+ "a" + File.separator + "b" + File.separator + "c", true), root)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(File, URL) - file as root #1")
		public void makeAbsoluteFileURL_fileAsRoot_1() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(FileSystem.makeAbsolute((File)null, root));
		}

		@Test
		@DisplayName("(File, URL) - file as root #2")
		public void makeAbsoluteFileURL_fileAsRoot_2() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new File("/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, URL) - file as root #3")
		public void makeAbsoluteFileURL_fileAsRoot_3() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new File("toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) - http as root #1")
		public void makeAbsoluteURLURL_httpAsRoot_1() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, URL) - http as root #2")
		public void makeAbsoluteURLURL_httpAsRoot_2() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertEquals(newURL("http://maven.arakhne.org/myroot/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("file:toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) - http as root #3")
		public void makeAbsoluteURLURL_httpAsRoot_3() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) - file as root #1")
		public void makeAbsoluteURLURL_fileAsRoot_1() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, URL) - file as root #2")
		public void makeAbsoluteURLURL_fileAsRoot_2() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) - file as root #3")
		public void makeAbsoluteURLURL_fileAsRoot_3() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) - file as root #4")
		public void makeAbsoluteURLURL_fileAsRoot_4() throws Exception {
			URL root = newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot", false)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, File) - root #1")
		public void makeAbsoluteFileFile_root_1() {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$
			assertEquals(newFile(getRootnameWithSeparator() + "toto", true),   //$NON-NLS-1$
					FileSystem.makeAbsolute(newFile(getRootnameWithSeparator() + "toto", true), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, File) - root #2")
		public void makeAbsoluteFileFile_root_2() {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$
			assertEquals(newFile(getRootnameWithSeparator() + "myroot" + getSeparator() + "toto", true),   //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new File("toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) - root #1")
		public void makeAbsoluteURLFile_root_1() throws Exception {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "toto", false)), root)); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, File) - root #2")
		public void makeAbsoluteURLFile_root_2() throws Exception {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$
			assertEquals(newURL("file:" + fromFileToUrl(getRootnameWithSeparator() + "myroot/toto", false)),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(newURL("file:toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) - root #3")
		public void makeAbsoluteURLFile_root_3() throws Exception {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$
			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
							+ "home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) - root #4")
		public void makeAbsoluteURLFile_root_4() throws Exception {
			File root = newFile(getRootnameWithSeparator() + "myroot", true); //$NON-NLS-1$
			assertEquals(newURL("jar:file:" + fromFileToUrl(getRootnameWithSeparator() //$NON-NLS-1$
					+ "myroot/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
		}
	}

}

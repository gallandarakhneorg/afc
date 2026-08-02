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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FileSystem")
@SuppressWarnings("all")
public class FileSystemTest extends AbstractFileSystemTestCase {

	@DisplayName("makeAbsolute")
	@Nested
	public class MakeAbsolute {

		@DisplayName("(File, URL) #1")
		@Test
		public void makeAbsoluteFileURL_httpAsRoot_1() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertNull(FileSystem.makeAbsolute((File)null, root));
		}

		@DisplayName("(File, URL) #2")
		@Test
		public void makeAbsoluteFileURL_httpAsRoot_2() throws Exception {
			final URL root = newURL("http://maven.arakhne.org/myroot"); //$NON-NLS-1$
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
					FileSystem.makeAbsolute(new File("a" + File.separator + "b" + File.separator + "c"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							createFileInJarUrlWithSpaces()));  
		}

		@Test
		@DisplayName("(File, URL) #3")
		public void makeAbsoluteFileURL_noRoot_3() throws Exception {
			assertNull(FileSystem.makeAbsolute((File)null, (URL)null));
		}

		@Test
		@DisplayName("(File, URL) #4")
		public void makeAbsoluteFileURL_noRoot_4() throws Exception {
			assertEquals(newURL("file:" + fromFileToUrl(getCurrentDir(), false) + "/toto"),  //$NON-NLS-1$ //$NON-NLS-2$
					FileSystem.makeAbsolute(new File("toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #1")
		public void makeAbsoluteURLFile_noRoot_1() throws Exception {
			assertNull(FileSystem.makeAbsolute((URL)null, (File)null));
		}

		@Test
		@DisplayName("(URL, File) #2")
		public void makeAbsoluteURLFile_noRoot_2() throws Exception {
			assertEquals(newURL("file:/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("file:/toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #3")
		public void makeAbsoluteURLFile_noRoot_3() throws Exception {
			assertEquals(newURL("file:toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("file:toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #4")
		public void makeAbsoluteURLFile_noRoot_4() throws Exception {
			assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #5")
		public void makeAbsoluteURLFile_noRoot_5() throws Exception {
			assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #6")
		public void makeAbsoluteURLFile_noRoot_6() throws Exception {
			assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #7")
		public void makeAbsoluteURLFile_noRoot_7() throws Exception {
			assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #8")
		public void makeAbsoluteURLFile_noRoot_8() throws Exception {
			assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #9")
		public void makeAbsoluteURLFile_noRoot_9() throws Exception {
			assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #10")
		public void makeAbsoluteURLFile_noRoot_10() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #11")
		public void makeAbsoluteURLFile_noRoot_11() throws Exception {
			assertEquals(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #12")
		public void makeAbsoluteURLFile_root_12() throws Exception {
			var root = new File(File.separator+"myroot"); //$NON-NLS-1$
			assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #13")
		public void makeAbsoluteURLFile_root_13() throws Exception {
			var root = new File(File.separator+"myroot"); //$NON-NLS-1$
			assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #14")
		public void makeAbsoluteURLFile_root_14() throws Exception {
			var root = new File(File.separator+"myroot"); //$NON-NLS-1$
			assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #15")
		public void makeAbsoluteURLFile_root_15() throws Exception {
			var root = new File(File.separator+"myroot"); //$NON-NLS-1$
			assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #16")
		public void makeAbsoluteURLFile_root_16() throws Exception {
			var root = new File(File.separator+"myroot"); //$NON-NLS-1$
			assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, File) #17")
		public void makeAbsoluteURLFile_root_17() throws Exception {
			var root = new File(File.separator+"myroot"); //$NON-NLS-1$
			assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #1")
		public void makeAbsoluteURLURL_notroot_1() throws Exception {
			assertNull(FileSystem.makeAbsolute((URL)null, (URL)null));
		}

		@Test
		@DisplayName("(URL, URL) #2")
		public void makeAbsoluteURLURL_notroot_2() throws Exception {
			assertEquals(newURL("file:/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("file:/toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #3")
		public void makeAbsoluteURLURL_notroot_3() throws Exception {
			assertEquals(newURL("file:toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("file:toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #4")
		public void makeAbsoluteURLURL_notroot_4() throws Exception {
			assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #5")
		public void makeAbsoluteURLURL_notroot_5() throws Exception {
			assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #6")
		public void makeAbsoluteURLURL_notroot_6() throws Exception {
			assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #7")
		public void makeAbsoluteURLURL_notroot_7() throws Exception {
			assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #8")
		public void makeAbsoluteURLURL_notroot_8() throws Exception {
			assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #9")
		public void makeAbsoluteURLURL_notroot_9() throws Exception {
			assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #10")
		public void makeAbsoluteURLURL_notroot_10() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #11")
		public void makeAbsoluteURLURL_notroot_11() throws Exception {
			assertEquals(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), (URL)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #12")
		public void makeAbsoluteURLURL_fileAsRoot_12() throws Exception {
			URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$
			assertNull(FileSystem.makeAbsolute((URL)null, root));
		}

		@Test
		@DisplayName("(URL, URL) #13")
		public void makeAbsoluteURLURL_fileAsRoot_13() throws Exception {
			URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$
			assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #14")
		public void makeAbsoluteURLURL_fileAsRoot_14() throws Exception {
			URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$
			assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #15")
		public void makeAbsoluteURLURL_fileAsRoot_15() throws Exception {
			URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$
			assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #16")
		public void makeAbsoluteURLURL_fileAsRoot_16() throws Exception {
			URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$
			assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #17")
		public void makeAbsoluteURLURL_fileAsRoot_17() throws Exception {
			URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$
			assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #18")
		public void makeAbsoluteURLURL_fileAsRoot_18() throws Exception {
			URL root = new File(File.separator+"myroot").toURI().toURL(); //$NON-NLS-1$
			assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #19")
		public void makeAbsoluteURLURL_httpAsRoot_19() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertNull(FileSystem.makeAbsolute((URL)null, root));
		}

		@Test
		@DisplayName("(URL, URL) #20")
		public void makeAbsoluteURLURL_httpAsRoot_20() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertEquals(newURL("http://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #21")
		public void makeAbsoluteURLURL_httpAsRoot_21() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertEquals(newURL("http://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("http://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #22")
		public void makeAbsoluteURLURL_httpAsRoot_22() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertEquals(newURL("https://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #23")
		public void makeAbsoluteURLURL_httpAsRoot_23() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertEquals(newURL("https://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("https://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #24")
		public void makeAbsoluteURLURL_httpAsRoot_24() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertEquals(newURL("ftp://www.arakhne.org/toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #25")
		public void makeAbsoluteURLURL_httpAsRoot_25() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertEquals(newURL("ftp://www.arakhne.org/./toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("ftp://www.arakhne.org/./toto"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, URL) #26")
		public void makeAbsoluteURLURL_httpAsRoot_26() throws Exception {
			URL root = newURL("http://maven.arakhne.org"); //$NON-NLS-1$
			assertEquals(newURL("jar:http://maven.arakhne.org/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(newURL("jar:file:home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"), root)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, File) #1")
		public void makeAbsoluteFileFile_noRoot_1() {
			assertNull(FileSystem.makeAbsolute((File)null, (File)null));
		}

		@Test
		@DisplayName("(File, File) #2")
		public void makeAbsoluteFileFile_noRoot_2() {
			assertEquals(new File(File.separator+"toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new File(File.separator+"toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File, File) #3")
		public void makeAbsoluteFileFile_noRoot_3() {
			assertEquals(new File("toto"),  //$NON-NLS-1$
					FileSystem.makeAbsolute(new File("toto"), (File)null)); //$NON-NLS-1$
		}

		@Test
		@DisplayName("makeAbsolute(File, File) #4")
		public void makeAbsoluteFileFile_root_4() {
			File root = new File(File.separator+"myroot"); //$NON-NLS-1$
			assertNull(FileSystem.makeAbsolute((File)null, root));
		}
	}

	@DisplayName("makeRelative")
	@Nested
	public class MakeRelative {

		@DisplayName("Root: ~/")
		@Nested
		public class RootHomeDirectory {
			private File root;
			private URL rootUrl;
	
			@BeforeEach
			public void setUp() throws Exception {
				root = FileSystem.getUserHomeDirectory();
				rootUrl = root.toURI().toURL();
			}
	
			@Test
			@DisplayName("(File, File) #1")
			public void makeRelativeFileFile_1() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}
	
			@Test
			@DisplayName("(File, File) #2")
			public void makeRelativeFileFile_2() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b"); //$NON-NLS-1$ //$NON-NLS-2$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}
	
			@Test
			@DisplayName("(File, File) #3")
			public void makeRelativeFileFile_3() throws Exception {
				var abs = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
				var rel = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(File, URL) #1")
			public void makeRelativeFileURL_1() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(File, URL) #2")
			public void makeRelativeFileURL_2() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b"); //$NON-NLS-1$ //$NON-NLS-2$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(File, URL) #3")
			public void makeRelativeFileURL_3() throws Exception {
				var abs = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
				var rel = new File("a","b"); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(URL, URL) #1")
			public void makeRelativeURLURL_1() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); //$NON-NLS-1$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a"); //$NON-NLS-1$
				assertEquals(rel, FileSystem.makeRelative(abs, rootUrl));
			}

			@Test
			@DisplayName("(URL, URL) #2")
			public void makeRelativeURLURL_2() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"b").toURI().toURL(); //$NON-NLS-1$ //$NON-NLS-2$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator+"a","b"); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals(rel, FileSystem.makeRelative(abs, rootUrl));
			}
		}
	
		@DisplayName("Root: ~/zz/abc/")
		@Nested
		public class Root1 {

			private File root;
			private URL rootUrl;

			@BeforeEach
			public void setUp() throws Exception {
				root = new File(FileSystem.getUserHomeDirectory(), "zz"+File.separator+"abc"); //$NON-NLS-1$ //$NON-NLS-2$
				rootUrl = root.toURI().toURL();
			}
	
			@Test
			@DisplayName("(File, File) #1")
			public void makeRelativeFileFile_1() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+"a"); //$NON-NLS-1$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(File, File) #2")
			public void makeRelativeFileFile_2() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(File, URL) #1")
			public void makeRelativeFileURL_1() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"); //$NON-NLS-1$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+"a"); //$NON-NLS-1$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(File, URL) #2")
			public void makeRelativeFileURL_2() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(rel, FileSystem.makeRelative(abs, root));
			}

			@Test
			@DisplayName("(URL, URL) #1")
			public void makeRelativeURLURL_1() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a").toURI().toURL(); //$NON-NLS-1$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+"a"); //$NON-NLS-1$
				assertEquals(rel, FileSystem.makeRelative(abs, rootUrl));
			}

			@Test
			@DisplayName("(URL, URL) #2")
			public void makeRelativeURLURL_2() throws Exception {
				var abs = new File(FileSystem.getUserHomeDirectory(), "a"+File.separator+"zz"+File.separator+"bc").toURI().toURL(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				var rel = new File(FileSystem.CURRENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+FileSystem.PARENT_DIRECTORY+File.separator
						+"a"+File.separator+"zz"+File.separator+"bc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(rel, FileSystem.makeRelative(abs, rootUrl));
			}
		}
	}

	@DisplayName("convertFileToURL")
	@Nested
	public class ConvertFileToURL {

		@Test
		@DisplayName("(File)")
		public void file() throws Exception {
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

		@Test
		@DisplayName("(File) - Issue 173")
		public void convertFileToURLFile_issue173() throws Exception {
			File f1 = new File("./myfile.txt"); //$NON-NLS-1$
			URL u1 = newURL("file:./myfile.txt"); //$NON-NLS-1$
			assertEquals(u1, FileSystem.convertFileToURL(f1));
		}
	}

	@DisplayName("getParentURL")
	@Nested
	public class GetParentURL {

		@Test
		@DisplayName("(URL) #1")
		public void test_1() throws Exception {
			assertEquals(
					newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("http://www.arakhne.org"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #2")
		public void test_2() throws Exception {
			assertEquals(
					newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("http://www.arakhne.org/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #3")
		public void test_3() throws Exception {
			assertEquals(
					newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("http://www.arakhne.org/toto"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #4")
		public void test_4() throws Exception {
			assertEquals(
					newURL("http://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("http://www.arakhne.org/toto/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #5")
		public void test_5() throws Exception {
			assertEquals(
					newURL("http://www.arakhne.org/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("http://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #6")
		public void test_6() throws Exception {
			assertEquals(
					newURL("http://www.arakhne.org/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("http://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #7")
		public void test_7() throws Exception {
			assertEquals(
					newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("https://www.arakhne.org"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #8")
		public void test_8() throws Exception {
			assertEquals(
					newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("https://www.arakhne.org/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #9")
		public void test_9() throws Exception {
			assertEquals(
					newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("https://www.arakhne.org/toto"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #10")
		public void test_10() throws Exception {
			assertEquals(
					newURL("https://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("https://www.arakhne.org/toto/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #11")
		public void test_11() throws Exception {
			assertEquals(
					newURL("https://www.arakhne.org/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("https://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #12")
		public void test_12() throws Exception {
			assertEquals(
					newURL("https://www.arakhne.org/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("https://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #13")
		public void test_13() throws Exception {
			assertEquals(
					newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("ftp://www.arakhne.org"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #14")
		public void test_14() throws Exception {
			assertEquals(
					newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("ftp://www.arakhne.org/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #15")
		public void test_15() throws Exception {
			assertEquals(
					newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #16")
		public void test_16() throws Exception {
			assertEquals(
					newURL("ftp://www.arakhne.org/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #17")
		public void test_17() throws Exception {
			assertEquals(
					newURL("ftp://www.arakhne.org/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto/titi"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #18")
		public void test_18() throws Exception {
			assertEquals(
					newURL("ftp://www.arakhne.org/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("ftp://www.arakhne.org/toto/titi/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #19")
		public void test_19() throws Exception {
			assertEquals(
					newURL("file:/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:/toto/titi"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #20")
		public void test_20() throws Exception {
			assertEquals(
					newURL("file:/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:/toto/titi/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #21")
		public void test_21() throws Exception {
			assertEquals(
					newURL("file:/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:/toto"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #22")
		public void test_22() throws Exception {
			assertEquals(
					newURL("file:/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:/toto/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #23")
		public void test_23() throws Exception {
			assertEquals(
					newURL("file:./toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:./toto/titi"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #24")
		public void test_24() throws Exception {
			assertEquals(
					newURL("file:./toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:./toto/titi/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #25")
		public void test_25() throws Exception {
			assertEquals(
					newURL("file:./"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:./toto"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #26")
		public void test_26() throws Exception {
			assertEquals(
					newURL("file:./"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:./toto/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #27")
		public void test_27() throws Exception {
			assertEquals(
					newURL("file:../"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:."))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #28")
		public void test_28() throws Exception {
			assertEquals(
					newURL("file:../"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:./"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #29")
		public void test_29() throws Exception {
			assertEquals(
					newURL("file:../"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:toto"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #30")
		public void test_30() throws Exception {
			assertEquals(
					newURL("file:../"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("file:toto/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #31")
		public void test_31() throws Exception {
			assertEquals(
					newURL("jar:file:test.jar!/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("jar:file:test.jar!/toto/titi"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #32")
		public void test_32() throws Exception {
			assertEquals(
					newURL("jar:file:test.jar!/toto/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("jar:file:test.jar!/toto/titi/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #33")
		public void test_33() throws Exception {
			assertEquals(
					newURL("jar:file:test.jar!/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("jar:file:test.jar!/toto"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #34")
		public void test_34() throws Exception {
			assertEquals(
					newURL("jar:file:test.jar!/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("jar:file:test.jar!/toto/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #35")
		public void test_35() throws Exception {
			assertEquals(
					newURL("jar:file:test.jar!/"),  //$NON-NLS-1$
					FileSystem.getParentURL(newURL("jar:file:test.jar!/"))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #36")
		public void test_36() throws Exception {
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"),   //$NON-NLS-1$
					FileSystem.getParentURL(createFileInJarUrlWithSpaces()));
		}
	}

	@DisplayName("jreBehaviorRelatedToURL")
	@Nested
	public class JreBehaviorRelatedToURL {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			// The following test permits to check if a specific behavior of URL
			// is still present in the JRE.
			URL rr = newURL("file://marbre.jpg"); //$NON-NLS-1$
			assertEquals("file", rr.getProtocol()); //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			// The following test permits to check if a specific behavior of URL
			// is still present in the JRE.
			URL rr = newURL("file://marbre.jpg"); //$NON-NLS-1$
			assertEquals("marbre.jpg", rr.getAuthority()); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			// The following test permits to check if a specific behavior of URL
			// is still present in the JRE.
			URL rr = newURL("file://marbre.jpg"); //$NON-NLS-1$
			assertEquals("", rr.getPath()); //$NON-NLS-1$
		}
	}

	@DisplayName("convertURLToFile")
	@Nested
	public class ConvertURLToFile {

		@DisplayName("#1")
		@Test
		public void convertURLToFile_1() throws Exception {
			try {
				FileSystem.convertURLToFile(newURL("http://www.arakhne.org")); //$NON-NLS-1$
				fail("not a file URL"); //$NON-NLS-1$
			}
			catch(IllegalArgumentException exception) {
				//
			}
		}

		@DisplayName("#2")
		@Test
		public void convertURLToFile_2() throws Exception {
			assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
					FileSystem.convertURLToFile(newURL("file:./toto")).getCanonicalPath()); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void convertURLToFile_3() throws Exception {
			assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
					FileSystem.convertURLToFile(newURL("file:toto")).getCanonicalPath()); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void convertURLToFile_4() throws Exception {
			assertEquals(new File("toto").getCanonicalPath(),  //$NON-NLS-1$
					FileSystem.convertURLToFile(newURL("file:./abs/../toto")).getCanonicalPath()); //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void convertURLToFile_5() throws Exception {
			assertEquals(new File("/toto").getCanonicalPath(),  //$NON-NLS-1$
					FileSystem.convertURLToFile(newURL("file:/toto")).getCanonicalPath()); //$NON-NLS-1$
		}
	}

	@DisplayName("convertStringToURL")
	@Nested
	public class ConvertStringToURL {

		@DisplayName("#1")
		@Test
		public void convertStringToURL_1() throws Exception {
			handlesResourceUrl();
			try {
				assertNull(FileSystem.convertStringToURL(null, true));
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#2")
		@Test
		public void convertStringToURL_2() throws Exception {
			handlesResourceUrl();
			try {
				assertNull(FileSystem.convertStringToURL("", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#3")
		@Test
		public void convertStringToURL_3() throws Exception {
			handlesResourceUrl();
			try {
				assertNull(FileSystem.convertStringToURL(null, false));
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#4")
		@Test
		public void convertStringToURL_4() throws Exception {
			handlesResourceUrl();
			try {
				assertNull(FileSystem.convertStringToURL("", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#5")
		@Test
		public void convertStringToURL_5() throws Exception {
			handlesResourceUrl();
			try {
				URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
				assertNotNull(rr);
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#6")
		@Test
		public void convertStringToURL_6() throws Exception {
			handlesResourceUrl();
			try {
				URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
				assertEquals("file", rr.getProtocol()); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#7")
		@Test
		public void convertStringToURL_7() throws Exception {
			handlesResourceUrl();
			try {
				URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
				assertEquals("", rr.getAuthority()); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#8")
		@Test
		public void convertStringToURL_8() throws Exception {
			handlesResourceUrl();
			try {
				URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
				assertEquals("", rr.getHost()); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#9")
		@Test
		public void convertStringToURL_9() throws Exception {
			handlesResourceUrl();
			try {
				URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
				assertNull(rr.getQuery());
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#10")
		@Test
		public void convertStringToURL_10() throws Exception {
			handlesResourceUrl();
			try {
				URL rr = FileSystem.convertStringToURL("file://marbre.jpg", false); //$NON-NLS-1$
				assertEquals("marbre.jpg", rr.getPath()); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#11")
		@Test
		public void convertStringToURL_11() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(newURL("http", "www.arakhne.org", "/"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("http://www.arakhne.org/", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#12")
		@Test
		public void convertStringToURL_12() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(newURL("http", "www.arakhne.org", "/"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("http://www.arakhne.org/", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#13")
		@Test
		public void convertStringToURL_13() throws Exception {
			handlesResourceUrl();
			try {
				// CAUTION: testing right-formed jar URL.
				assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#14")
		@Test
		public void convertStringToURL_14() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#15")
		@Test
		public void convertStringToURL_15() throws Exception {
			handlesResourceUrl();
			try {
				// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
				assertEquals(newURL("file", "", "/home/test/j.jar"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("jar:/home/test/j.jar", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#16")
		@Test
		public void convertStringToURL_16() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(newURL("file", "", "/home/test/j.jar"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("jar:/home/test/j.jar", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#17")
		@Test
		public void convertStringToURL_17() throws Exception {
			handlesResourceUrl();
			try {
				// CAUTION: testing malformed jar URL. Right syntax is: jar:{url}!/{entry}
				assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#18")
		@Test
		public void convertStringToURL_18() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(newURL("jar", "", "file:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties"),    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("jar:/home/test/j.jar!/org/arakhne/afc/vmutil/ff.properties", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#19")
		@Test
		public void convertStringToURL_19() throws Exception {
			handlesResourceUrl();
			try {
				URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				assertNotNull(testResource);
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#20")
		@Test
		public void convertStringToURL_20() throws Exception {
			handlesResourceUrl();
			try {
				URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				assertEquals(testResource,
						FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#21")
		@Test
		public void convertStringToURL_21() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(null,
						FileSystem.convertStringToURL("resource:/org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#22")
		@Test
		public void convertStringToURL_22() throws Exception {
			handlesResourceUrl();
			try {
				URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				assertEquals(testResource,
						FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#23")
		@Test
		public void convertStringToURL_23() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(null,
						FileSystem.convertStringToURL("resource:org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#24")
		@Test
		public void convertStringToURL_24() throws Exception {
			handlesResourceUrl();
			try {
				URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				assertEquals(testResource,
						FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#25")
		@Test
		public void convertStringToURL_25() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(newURL("file", "", "/org/arakhne/afc/vmutil/test.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("/org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#26")
		@Test
		public void convertStringToURL_26() throws Exception {
			handlesResourceUrl();
			try {
				URL testResource = Resources.getResource("/org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				assertEquals(testResource,
						FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", true)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}

		@DisplayName("#27")
		@Test
		public void convertStringToURL_27() throws Exception {
			handlesResourceUrl();
			try {
				assertEquals(newURL("file","", "org/arakhne/afc/vmutil/test.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						FileSystem.convertStringToURL("org/arakhne/afc/vmutil/test.txt", false)); //$NON-NLS-1$
			} finally {
				unhandlesResourceUrl();
			}
		}
	}

	@DisplayName("join")
	@Nested
	public class Join {

		@DisplayName("(URL, String...) #1")
		@Test
		public void joinURLStringArray_1() throws Exception {
			assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"), FileSystem.join(createHttpUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(URL, String...) #2")
		@Test
		public void joinURLStringArray_2() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrl(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(URL, String...) #3")
		@Test
		public void joinURLStringArray_3() throws Exception {
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto/a/b/c"), FileSystem.join(createFileUrlWithSpacesHardCoded(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@DisplayName("(URL, String...) #4")
		@Test
		public void joinURLStringArray_4() throws Exception {
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), FileSystem.join(createFileInJarUrlWithSpaces(), "a", "b", "c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@Test
		@DisplayName("(URL, File...) #1")
		public void joinURLFileArray_1() throws Exception {
			assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z/a/b/c?toto#frag"), //$NON-NLS-1$
					FileSystem.join(createHttpUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, File...) #2")
		public void joinURLFileArray_2() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
					FileSystem.join(createFileInJarUrl(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, File...) #3")
		public void joinURLFileArray_3() throws Exception {
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto/a/b/c"), //$NON-NLS-1$
					FileSystem.join(createFileUrlWithSpacesHardCoded(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, File...) #4")
		public void joinURLFileArray_4() throws Exception {
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z/a/b/c"), //$NON-NLS-1$
					FileSystem.join(createFileInJarUrlWithSpaces(), new File("a"), new File("b"), new File("c"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	@DisplayName("split")
	@Nested
	public class Split {

		@Test
		@DisplayName("(URL) #1")
		public void splitURL_1() throws Exception {
			assertArrayEquals(new String[] {"", "path", "to", "file.x.z.z"}, FileSystem.split(createHttpUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@Test
		@DisplayName("(URL) #2")
		public void splitURL_2() throws Exception {
			assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}

		@Test
		@DisplayName("(URL) #3")
		public void splitURL_3() throws Exception {
			assertArrayEquals(new String[] {"", "the path", "to", "file with space.toto"}, FileSystem.split(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		@Test
		@DisplayName("(URL) #4")
		public void splitURL_4() throws Exception {
			assertArrayEquals(new String[] {"", "org", "arakhne", "afc", "vmutil", "file.x.z.z"}, FileSystem.split(createFileInJarUrlWithSpaces())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}

		@Test
		@DisplayName("(URL) #5")
		public void splitURL_5() throws Exception {
			assertArrayEquals(new String[] {"", "a.b.c"}, FileSystem.split(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL) #6")
		public void splitURL_6() throws Exception {
			assertArrayEquals(new String[] {""}, FileSystem.split(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("extensions")
	@Nested
	public class Extensions {

		@Test
		@DisplayName("(URL) #1")
		public void extensionsURL_1() throws Exception {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createHttpUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL) #2")
		public void extensionsURL_2() throws Exception {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrl())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL) #3")
		public void extensionsURL_3() throws Exception {
			assertArrayEquals(new String[] {"toto"}, FileSystem.extensions(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #4")
		public void extensionsURL_4() throws Exception {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions(createFileInJarUrlWithSpaces())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL) #5")
		public void extensionsURL_5() throws Exception {
			assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL) #6")
		public void extensionsURL_6() throws Exception {
			assertArrayEquals(new String[0], FileSystem.extensions(newURL("file://"))); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void extensionsString_1() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void extensionsString_2() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void extensionsString_3() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@Test
		@DisplayName("(String) #4")
		public void extensionsString_4() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@Test
		@DisplayName("(String) #5")
		public void extensionsString_5() {
			assertArrayEquals(new String[] {"x", "z", "z"}, FileSystem.extensions("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		@Test
		@DisplayName("(String) #6")
		public void extensionsString_6() {
			assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		@Test
		@DisplayName("(String) #7")
		public void extensionsString_7() {
			assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		@Test
		@DisplayName("(String) #8")
		public void extensionsString_8() {
			assertArrayEquals(new String[] {"b", "c"}, FileSystem.extensions("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		@Test
		@DisplayName("(String) #9")
		public void extensionsString_9() {
			assertArrayEquals(new String[0], FileSystem.extensions("file://")); //$NON-NLS-1$
		}
		
		@Test
		@DisplayName("(String) #10")
		public void extensionsString_10() {
			assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #11")
		public void extensionsString_11() {
			assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #12")
		public void extensionsString_12() {
			assertArrayEquals(new String[] {"dae"}, FileSystem.extensions("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("extension")
	@Nested
	public class Extension {

		@Test
		@DisplayName("(URL) #1")
		public void extensionURL_1() throws Exception {
			assertEquals(".z", FileSystem.extension(createHttpUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #2")
		public void extensionURL_2() throws Exception {
			assertEquals(".z", FileSystem.extension(createFileInJarUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #3")
		public void extensionURL_3() throws Exception {
			assertEquals(".toto", FileSystem.extension(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #4")
		public void extensionURL_4() throws Exception {
			assertEquals(".z", FileSystem.extension(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #5")
		public void extensionURL_5() throws Exception {
			assertEquals(".c", FileSystem.extension(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL) #6")
		public void extensionURL_6() throws Exception {
			assertEquals("", FileSystem.extension(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void extensionString_1() {
			assertEquals(".z", FileSystem.extension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void extensionString_2() {
			assertEquals(".z", FileSystem.extension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void extensionString_3() {
			assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #4")
		public void extensionString_4() {
			assertEquals(".z", FileSystem.extension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #5")
		public void extensionString_5() {
			assertEquals(".z", FileSystem.extension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #6")
		public void extensionString_6() {
			assertEquals(".c", FileSystem.extension("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #7")
		public void extensionString_7() {
			assertEquals(".c", FileSystem.extension("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #8")
		public void extensionString_8() {
			assertEquals(".c", FileSystem.extension("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #9")
		public void extensionString_9() {
			assertEquals("", FileSystem.extension("file://")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #10")
		public void extensionString_10() {
			assertEquals(".dae", FileSystem.extension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #11")
		public void extensionString_11() {
			assertEquals(".dae", FileSystem.extension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #12")
		public void extensionString_12() {
			assertEquals(".dae", FileSystem.extension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("hasExtension")
	@Nested
	public class HasExtension {

		@Test
		@DisplayName("(URL, String) #1")
		public void hasExtensionURL_1() throws Exception {
			assertTrue(FileSystem.hasExtension(createHttpUrl(), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #2")
		public void hasExtensionURL_2() throws Exception {
			assertTrue(FileSystem.hasExtension(createHttpUrl(), "z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #3")
		public void hasExtensionURL_3() throws Exception {
			assertFalse(FileSystem.hasExtension(createHttpUrl(), ".c")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #4")
		public void hasExtensionURL_4() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileInJarUrl(), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #5")
		public void hasExtensionURL_5() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileInJarUrl(), "z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #6")
		public void hasExtensionURL_6() throws Exception {
			assertFalse(FileSystem.hasExtension(createFileInJarUrl(), ".c")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #7")
		public void hasExtensionURL_7() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".toto")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #8")
		public void hasExtensionURL_8() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), "toto")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #9")
		public void hasExtensionURL_9() throws Exception {
			assertFalse(FileSystem.hasExtension(createFileUrlWithSpacesHardCoded(), ".zip")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #10")
		public void hasExtensionURL_10() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #11")
		public void hasExtensionURL_11() throws Exception {
			assertTrue(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), "z")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #12")
		public void hasExtensionURL_12() throws Exception {
			assertFalse(FileSystem.hasExtension(createFileInJarUrlWithSpaces(), ".c")); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL, String) #13")
		public void hasExtensionURL_13() throws Exception {
			assertTrue(FileSystem.hasExtension(newURL("file:///a.b.c/"), ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #14")
		public void hasExtensionURL_14() throws Exception {
			assertTrue(FileSystem.hasExtension(newURL("file:///a.b.c/"), "c")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #15")
		public void hasExtensionURL_15() throws Exception {
			assertFalse(FileSystem.hasExtension(newURL("file:///a.b.c/"), ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #16")
		public void hasExtensionURL_16() throws Exception {
			assertFalse(FileSystem.hasExtension(newURL("file://"), ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@Test
		@DisplayName("(String, String) #1")
		public void hasExtensionString_1() {
			assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #2")
		public void hasExtensionString_2() {
			assertTrue(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #3")
		public void hasExtensionString_3() {
			assertFalse(FileSystem.hasExtension("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #4")
		public void hasExtensionString_4() {
			assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #5")
		public void hasExtensionString_5() {
			assertTrue(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #6")
		public void hasExtensionString_6() {
			assertFalse(FileSystem.hasExtension("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #7")
		public void hasExtensionString_7() {
			assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #8")
		public void hasExtensionString_8() {
			assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #9")
		public void hasExtensionString_9() {
			assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #10")
		public void hasExtensionString_10() {
			assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #11")
		public void hasExtensionString_11() {
			assertTrue(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #12")
		public void hasExtensionString_12() {
			assertFalse(FileSystem.hasExtension("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #13")
		public void hasExtensionString_13() {
			assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #14")
		public void hasExtensionString_14() {
			assertTrue(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", "z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #15")
		public void hasExtensionString_15() {
			assertFalse(FileSystem.hasExtension("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #16")
		public void hasExtensionString_16() {
			assertTrue(FileSystem.hasExtension("file:///a.b.c/", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #17")
		public void hasExtensionString_17() {
			assertTrue(FileSystem.hasExtension("file:///a.b.c/", "c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #18")
		public void hasExtensionString_18() {
			assertFalse(FileSystem.hasExtension("file:///a.b.c/", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #19")
		public void hasExtensionString_19() {
			assertTrue(FileSystem.hasExtension("file:a.b.c", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #20")
		public void hasExtensionString_20() {
			assertTrue(FileSystem.hasExtension("file:a.b.c", "c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #21")
		public void hasExtensionString_21() {
			assertFalse(FileSystem.hasExtension("file:a.b.c", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #22")
		public void hasExtensionString_22() {
			assertTrue(FileSystem.hasExtension("a.b.c", ".c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #23")
		public void hasExtensionString_23() {
			assertTrue(FileSystem.hasExtension("a.b.c", "c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #24")
		public void hasExtensionString_24() {
			assertFalse(FileSystem.hasExtension("a.b.c", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #25")
		public void hasExtensionString_25() {
			assertFalse(FileSystem.hasExtension("file://", ".z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #26")
		public void hasExtensionString_26() {
			assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #27")
		public void hasExtensionString_27() {
			assertTrue(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #28")
		public void hasExtensionString_28() {
			assertFalse(FileSystem.hasExtension("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #29")
		public void hasExtensionString_29() {
			assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #30")
		public void hasExtensionString_30() {
			assertTrue(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #31")
		public void hasExtensionString_31() {
			assertFalse(FileSystem.hasExtension("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #32")
		public void hasExtensionString_32() {
			assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #33")
		public void hasExtensionString_33() {
			assertTrue(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", "dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String, String) #34")
		public void hasExtensionString_34() {
			assertFalse(FileSystem.hasExtension("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae", ".zip")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("removeExtension")
	@Nested
	public class RemoveExtension {

		@Test
		@DisplayName("(URL) #1")
		public void removeExtensionURL_1() throws Exception {
			assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z?toto#frag"), FileSystem.removeExtension(createHttpUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #2")
		public void removeExtensionURL_2() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #3")
		public void removeExtensionURL_3() throws Exception {
			assertEquals(newURL("file:/the%20path/to/file%20with%20space"), FileSystem.removeExtension(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #4")
		public void removeExtensionURL_4() throws Exception {
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z"), FileSystem.removeExtension(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #5")
		public void removeExtensionURL_5() throws Exception {
			assertEquals(newURL("file:///a.b"), FileSystem.removeExtension(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL) #6")
		public void removeExtensionURL_6() throws Exception {
			assertEquals(newURL("file", "", ""), FileSystem.removeExtension(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	@DisplayName("replaceExtension")
	@Nested
	public class ReplaceExtension {

		@Test
		@DisplayName("(URL, String) #1")
		public void replaceExtensionURLString_1() throws Exception {
			assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.xyz?toto#frag"), FileSystem.replaceExtension(createHttpUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #2")
		public void replaceExtensionURLString_2() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #3")
		public void replaceExtensionURLString_3() throws Exception {
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.xyz"), FileSystem.replaceExtension(createFileUrlWithSpacesHardCoded(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #4")
		public void replaceExtensionURLString_4() throws Exception {
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.xyz"), FileSystem.replaceExtension(createFileInJarUrlWithSpaces(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #5")
		public void replaceExtensionURLString_5() throws Exception {
			assertEquals(newURL("file:///a.b.xyz"), FileSystem.replaceExtension(newURL("file:///a.b.c/"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, String) #6")
		public void replaceExtensionURLString_6() throws Exception {
			assertEquals(newURL("file", "", ""), FileSystem.replaceExtension(newURL("file://"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
	}

	@DisplayName("addExtension")
	@Nested
	public class AddExtension {

		@Test
		@DisplayName("(URL, String) #1")
		public void addExtensionURLString_1() throws Exception {
			assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/file.x.z.z.xyz?toto#frag"), FileSystem.addExtension(createHttpUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #2")
		public void addExtensionURLString_2() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #3")
		public void addExtensionURLString_3() throws Exception {
			assertEquals(newURL("file:/the%20path/to/file%20with%20space.toto.xyz"), FileSystem.addExtension(createFileUrlWithSpacesHardCoded(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #4")
		public void addExtensionURLString_4() throws Exception {
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z.xyz"), FileSystem.addExtension(createFileInJarUrlWithSpaces(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL, String) #5")
		public void addExtensionURLString_5() throws Exception {
			assertEquals(newURL("file:///a.b.c.xyz"), FileSystem.addExtension(newURL("file:///a.b.c/"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Test
		@DisplayName("(URL, String) #6")
		public void addExtensionURLString_6() throws Exception {
			assertEquals(newURL("file", "", ""), FileSystem.addExtension(newURL("file://"), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
	}

	@DisplayName("basename")
	@Nested
	public class Basename {

		@Test
		@DisplayName("(URL) #1")
		public void basenameURL_1() throws Exception {
			assertEquals("file.x.z", FileSystem.basename(createHttpUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #2")
		public void basenameURL_2() throws Exception {
			assertEquals("file.x.z", FileSystem.basename(createFileInJarUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #3")
		public void basenameURL_3() throws Exception {
			assertEquals("file with space", FileSystem.basename(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #4")
		public void basenameURL_4() throws Exception {
			assertEquals("file.x.z", FileSystem.basename(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #5")
		public void basenameURL_5() throws Exception {
			assertEquals("a.b", FileSystem.basename(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL) #6")
		public void basenameURL_6() throws Exception {
			assertEquals("", FileSystem.basename(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL) #7")
		public void basenameURL_7() throws Exception {
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
		@DisplayName("(String) #1")
		public void basenameString_1() {
			assertEquals("file.x.z", FileSystem.basename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void basenameString_2() {
			assertEquals("file.x.z", FileSystem.basename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void basenameString_3() {
			assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #4")
		public void basenameString_4() {
			assertEquals("file.x.z", FileSystem.basename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #5")
		public void basenameString_5() {
			assertEquals("file.x.z", FileSystem.basename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #6")
		public void basenameString_6() {
			assertEquals("a.b", FileSystem.basename("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #7")
		public void basenameString_7() {
			assertEquals("a.b", FileSystem.basename("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #8")
		public void basenameString_8() {
			assertEquals("a.b", FileSystem.basename("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #9")
		public void basenameString_9() {
			assertEquals("", FileSystem.basename("file://")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #10")
		public void basenameString_10() {
			assertEquals("terrain_physx", FileSystem.basename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #11")
		public void basenameString_11() {
			assertEquals("terrain_physx", FileSystem.basename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #12")
		public void basenameString_12() {
			assertEquals("terrain_physx", FileSystem.basename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("largeBasename")
	@Nested
	public class LargeBasename {

		@Test
		@DisplayName("(URL) #1")
		public void largeBasenameURL_1() throws Exception {
			assertEquals("file.x.z.z", FileSystem.largeBasename(createHttpUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #2")
		public void largeBasenameURL_2() throws Exception {
			assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #3")
		public void largeBasenameURL_3() throws Exception {
			assertEquals("file with space.toto", FileSystem.largeBasename(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #4")
		public void largeBasenameURL_4() throws Exception {
			assertEquals("file.x.z.z", FileSystem.largeBasename(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #5")
		public void largeBasenameURL_5() throws Exception {
			assertEquals("a.b.c", FileSystem.largeBasename(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL) #6")
		public void largeBasenameURL_6() throws Exception {
			assertEquals("", FileSystem.largeBasename(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void largeBasenameString_1() {
			assertEquals("file.x.z.z", FileSystem.largeBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void largeBasenameString_2() {
			assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void largeBasenameString_3() {
			assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #4")
		public void largeBasenameString_4() {
			assertEquals("file.x.z.z", FileSystem.largeBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #5")
		public void largeBasenameString_5() {
			assertEquals("file.x.z.z", FileSystem.largeBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #6")
		public void largeBasenameString_6() {
			assertEquals("a.b.c", FileSystem.largeBasename("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #7")
		public void largeBasenameString_7() {
			assertEquals("a.b.c", FileSystem.largeBasename("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #8")
		public void largeBasenameString_8() {
			assertEquals("a.b.c", FileSystem.largeBasename("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #9")
		public void largeBasenameString_9() {
			assertEquals("", FileSystem.largeBasename("file://")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #10")
		public void largeBasenameString_10() {
			assertEquals("terrain_physx.dae", FileSystem.largeBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #11")
		public void largeBasenameString_11() {
			assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #12")
		public void largeBasenameString_12() {
			assertEquals("terrain_physx.dae", FileSystem.largeBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("shortBasename")
	@Nested
	public class ShortBasename {

		@Test
		@DisplayName("(URL) #1")
		public void shortBasenameURL_1() throws Exception {
			assertEquals("file", FileSystem.shortBasename(createHttpUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #2")
		public void shortBasenameURL_2() throws Exception {
			assertEquals("file", FileSystem.shortBasename(createFileInJarUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #3")
		public void shortBasenameURL_3() throws Exception {
			assertEquals("file with space", FileSystem.shortBasename(createFileUrlWithSpacesHardCoded())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #4")
		public void shortBasenameURL_4() throws Exception {
			assertEquals("file", FileSystem.shortBasename(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(URL) #5")
		public void shortBasenameURL_5() throws Exception {
			assertEquals("a", FileSystem.shortBasename(newURL("file:///a.b.c/"))); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(URL) #6")
		public void shortBasenameURL_6() throws Exception {
			assertEquals("", FileSystem.shortBasename(newURL("file://"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@Test
		@DisplayName("(String) #1")
		public void shortBasenameString_1() {
			assertEquals("file", FileSystem.shortBasename("http://toto:titi@www.arakhne.org/path/to/file.x.z.z?toto#frag")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #2")
		public void shortBasenameString_2() {
			assertEquals("file", FileSystem.shortBasename("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #3")
		public void shortBasenameString_3() {
			assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #4")
		public void shortBasenameString_4() {
			assertEquals("file", FileSystem.shortBasename("jar:jar:file:/home/test/j.jar!/inner/myjar.jar!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #5")
		public void shortBasenameString_5() {
			assertEquals("file", FileSystem.shortBasename("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/file.x.z.z")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #6")
		public void shortBasenameString_6() {
			assertEquals("a", FileSystem.shortBasename("file:///a.b.c/")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #7")
		public void shortBasenameString_7() {
			assertEquals("a", FileSystem.shortBasename("file:a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #8")
		public void shortBasenameString_8() {
			assertEquals("a", FileSystem.shortBasename("a.b.c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #9")
		public void shortBasenameString_9() {
			assertEquals("", FileSystem.shortBasename("file://")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #10")
		public void shortBasenameString_10() {
			assertEquals("terrain_physx", FileSystem.shortBasename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #11")
		public void shortBasenameString_11() {
			assertEquals("terrain_physx", FileSystem.shortBasename("file:D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Test
		@DisplayName("(String) #12")
		public void shortBasenameString_12() {
			assertEquals("terrain_physx", FileSystem.shortBasename("file:\\D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("toShortestURL")
	@Nested
	public class ToShortestURL {

		@Test
		@DisplayName("(URL) #1")
		public void toShortestURLURL_1() throws Exception {
			handlesResourceUrl();
			try {
				File f1 = new File("/toto"); //$NON-NLS-1$
				URL u1 = f1.toURI().toURL();
				URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				URL u2e = newURL("resource:org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				var actual = FileSystem.toShortestURL(u1);
				assertEquals(u1, actual);
			} finally {
				unhandlesResourceUrl();
			}
		}

		@Test
		@DisplayName("(URL) #2")
		public void toShortestURLURL_2() throws Exception {
			handlesResourceUrl();
			try {
				File f1 = new File("/toto"); //$NON-NLS-1$
				URL u1 = f1.toURI().toURL();
				URL u2 = Resources.getResource("org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				URL u2e = newURL("resource:org/arakhne/afc/vmutil/test.txt"); //$NON-NLS-1$
				var actual = FileSystem.toShortestURL(u2);
				assertEquals(u2e, actual);
			} finally {
				unhandlesResourceUrl();
			}
		}
	}

	@DisplayName("makeCanonical")
	@Nested
	public class MakeCanonicalURL {

		@DisplayName("(URL) #1")
		@Test
		public void makeCanonicalURL_1() throws Exception {
			assertEquals(
					createHttpUrl(), 
					FileSystem.makeCanonicalURL(createHttpUrl()));
		}

		@DisplayName("(URL) #2")
		@Test
		public void makeCanonicalURL_2() throws Exception {
			assertEquals(
					createFileInJarUrl(), 
					FileSystem.makeCanonicalURL(createFileInJarUrl()));
		}

		@DisplayName("(URL) #3")
		@Test
		public void makeCanonicalURL_3() throws Exception {
			assertEquals(
					newURL("file:/a/b/c/d/e"),  //$NON-NLS-1$
					FileSystem.makeCanonicalURL(newURL("file:/a/b/./c/./d/e"))); //$NON-NLS-1$
		}

		@DisplayName("(URL) #4")
		@Test
		public void makeCanonicalURL_4() throws Exception {
			assertEquals(
					newURL("file:/a/d/e"),  //$NON-NLS-1$
					FileSystem.makeCanonicalURL(newURL("file:/a/b/../c/../d/e"))); //$NON-NLS-1$
		}

		@DisplayName("(URL) #5")
		@Test
		public void makeCanonicalURL_5() throws Exception {
			assertEquals(
					newURL("file:/a/b/d/e"),  //$NON-NLS-1$
					FileSystem.makeCanonicalURL(newURL("file:/a/b/./c/../d/e"))); //$NON-NLS-1$
		}

		@DisplayName("(URL) #6")
		@Test
		public void makeCanonicalURL_6() throws Exception {
			assertEquals(
					newURL("file:../a/b/c/d/e"),  //$NON-NLS-1$
					FileSystem.makeCanonicalURL(newURL("file:../a/b/./c/./d/e"))); //$NON-NLS-1$
		}

		@DisplayName("(URL) #7")
		@Test
		public void makeCanonicalURL_7() throws Exception {
			assertEquals(
					newURL("file:../a/c/d/e"),  //$NON-NLS-1$
					FileSystem.makeCanonicalURL(newURL("file:../a/b/../c/./d/e"))); //$NON-NLS-1$
		}
	}

	@DisplayName("zipFile")
	@Nested
	public class ZipFileTest {

		private File testArchive;

		@BeforeEach
		public void setUp() throws Exception {
			testArchive = File.createTempFile("unittest", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
			createZip(testArchive);
		}

		@AfterEach
		public void tearDown() throws Exception {
			if (testArchive != null) {
				FileSystem.delete(testArchive);
			}
		}

		@Test
		@DisplayName("(File) #1")
		public void zipFileFile_1() throws IOException {
			try (var zipFile = new ZipFile(testArchive)) {
				ZipEntry zipEntry = zipFile.getEntry("test.txt"); //$NON-NLS-1$
				assertNotNull(zipEntry);
				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); //$NON-NLS-1$
			}
		}

		@Test
		@DisplayName("(File) #2")
		public void zipFileFile_2() throws IOException {
			try (var zipFile = new ZipFile(testArchive)) {
				var zipEntry = zipFile.getEntry("test2.txt"); //$NON-NLS-1$
				assertNotNull(zipEntry);
				assertEquals("TEST2: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); //$NON-NLS-1$
			}
		}

		@Test
		@DisplayName("(File) #3")
		public void zipFileFile_3() throws IOException {
			try (var zipFile = new ZipFile(testArchive)) {
				var zipEntry = zipFile.getEntry("subdir/test.txt"); //$NON-NLS-1$
				assertNotNull(zipEntry);
				assertEquals("TEST1: FOR UNIT TEST ONLY", readInputStream(zipFile.getInputStream(zipEntry))); //$NON-NLS-1$
			}
		}
	}	
	
	@DisplayName("unzipFile")
	@Nested
	public class UnzipFile {

		private File testArchive;
		private File testDir;
		private File subDir;

		@BeforeEach
		public void setUp() throws Exception {
			testArchive = File.createTempFile("unittest", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
			createZip(testArchive);

			testDir = FileSystem.createTempDirectory("unittest", null); //$NON-NLS-1$
			FileSystem.deleteOnExit(testDir);
			subDir = new File(testDir, "subdir"); //$NON-NLS-1$

			FileSystem.unzipFile(testArchive, testDir);

			assertTrue(testDir.isDirectory());
			assertTrue(subDir.isDirectory());
		}

		@AfterEach
		public void tearDown() throws Exception {
			if (testArchive != null) {
				FileSystem.delete(testArchive);
			}
		}

		@Test
		@DisplayName("(File) #1")
		public void testUnzipFileFile_1() throws IOException {
			String txt;
			File file = new File(testDir, "test.txt"); //$NON-NLS-1$
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST1: FOR UNIT TEST ONLY", txt); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #2")
		public void testUnzipFileFile_2() throws IOException {
			String txt;
			var file = new File(testDir, "test2.txt"); //$NON-NLS-1$
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST2: FOR UNIT TEST ONLY", txt); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #3")
		public void testUnzipFileFile_3() throws IOException {
			String txt;
			var file = new File(subDir, "test.txt"); //$NON-NLS-1$
			try (FileInputStream fis = new FileInputStream(file)) {
				txt = readInputStream(fis);
			}
			assertEquals("TEST1: FOR UNIT TEST ONLY", txt); //$NON-NLS-1$
		}
	}

	@DisplayName("getFileExtensionCharacter")
	@Nested
	public class GetFileExtensionCharacter {

		@DisplayName("#1")
		@Test
		public void getFileExtensionCharacter() {
			assertInlineParameterUsage(FileSystem.class, "getFileExtensionCharacter"); //$NON-NLS-1$
		}
	}

	@DisplayName("isWindowsNativeFilename")
	@Nested
	public class IsWindowsNativeFilename {

		@DisplayName("#1")
		@Test
		public void getFileExtensionCharacter_1() {
			assertFalse(FileSystem.isWindowsNativeFilename("D:/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void getFileExtensionCharacter_2() {
			assertTrue(FileSystem.isWindowsNativeFilename("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void getFileExtensionCharacter_3() {
			assertTrue(FileSystem.isWindowsNativeFilename("D|\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void getFileExtensionCharacter_4() {
			assertFalse(FileSystem.isWindowsNativeFilename("/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void getFileExtensionCharacter_5() {
			assertFalse(FileSystem.isWindowsNativeFilename("/")); //$NON-NLS-1$
		}

		@DisplayName("#6")
		@Test
		public void getFileExtensionCharacter_6() {
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\")); //$NON-NLS-1$
		}

		@DisplayName("#7")
		@Test
		public void getFileExtensionCharacter_7() {
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
		}

		@DisplayName("#8")
		@Test
		public void getFileExtensionCharacter_8() {
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$
		}

		@DisplayName("#9")
		@Test
		public void getFileExtensionCharacter_9() {
			assertTrue(FileSystem.isWindowsNativeFilename("file:C:\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#10")
		@Test
		public void getFileExtensionCharacter_10() {
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#11")
		@Test
		public void getFileExtensionCharacter_11() {
			assertTrue(FileSystem.isWindowsNativeFilename("file:C:a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#12")
		@Test
		public void getFileExtensionCharacter_12() {
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#13")
		@Test
		public void getFileExtensionCharacter_13() {
			assertTrue(FileSystem.isWindowsNativeFilename("file:\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#14")
		@Test
		public void getFileExtensionCharacter_14() {
			assertTrue(FileSystem.isWindowsNativeFilename("file://\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#15")
		@Test
		public void getFileExtensionCharacter_15() {
			assertTrue(FileSystem.isWindowsNativeFilename("file:a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#16")
		@Test
		public void getFileExtensionCharacter_16() {
			assertTrue(FileSystem.isWindowsNativeFilename("file://a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#17")
		@Test
		public void getFileExtensionCharacter_17() {
			assertTrue(FileSystem.isWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#18")
		@Test
		public void getFileExtensionCharacter_18() {
			assertTrue(FileSystem.isWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#19")
		@Test
		public void getFileExtensionCharacter_19() {
			assertTrue(FileSystem.isWindowsNativeFilename("C:\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#20")
		@Test
		public void getFileExtensionCharacter_20() {
			assertTrue(FileSystem.isWindowsNativeFilename("C:a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#21")
		@Test
		public void getFileExtensionCharacter_21() {
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#22")
		@Test
		public void getFileExtensionCharacter_22() {
			assertTrue(FileSystem.isWindowsNativeFilename("\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#23")
		@Test
		public void getFileExtensionCharacter_23() {
			assertTrue(FileSystem.isWindowsNativeFilename("a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#24")
		@Test
		public void getFileExtensionCharacter_24() {
			assertTrue(FileSystem.isWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#25")
		@Test
		public void getFileExtensionCharacter_25() {
			assertFalse(FileSystem.isWindowsNativeFilename("file:C:/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#26")
		@Test
		public void getFileExtensionCharacter_26() {
			assertFalse(FileSystem.isWindowsNativeFilename("file://C:/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#27")
		@Test
		public void getFileExtensionCharacter_27() {
			assertFalse(FileSystem.isWindowsNativeFilename("file:C:a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#28")
		@Test
		public void getFileExtensionCharacter_28() {
			assertFalse(FileSystem.isWindowsNativeFilename("file://C:a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#29")
		@Test
		public void getFileExtensionCharacter_29() {
			assertFalse(FileSystem.isWindowsNativeFilename("file:/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#30")
		@Test
		public void getFileExtensionCharacter_30() {
			assertFalse(FileSystem.isWindowsNativeFilename("file:///a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#31")
		@Test
		public void getFileExtensionCharacter_31() {
			assertFalse(FileSystem.isWindowsNativeFilename("file:a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#32")
		@Test
		public void getFileExtensionCharacter_32() {
			assertFalse(FileSystem.isWindowsNativeFilename("file://a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#33")
		@Test
		public void getFileExtensionCharacter_33() {
			assertFalse(FileSystem.isWindowsNativeFilename("file://host/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#34")
		@Test
		public void getFileExtensionCharacter_34() {
			assertFalse(FileSystem.isWindowsNativeFilename("file:////host/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#35")
		@Test
		public void getFileExtensionCharacter_35() {
			assertTrue(FileSystem.isWindowsNativeFilename("C:c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#36")
		@Test
		public void getFileExtensionCharacter_36() {
			assertFalse(FileSystem.isWindowsNativeFilename("c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#37")
		@Test
		public void getFileExtensionCharacter_37() {
			assertTrue(FileSystem.isWindowsNativeFilename("file:C:c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#38")
		@Test
		public void getFileExtensionCharacter_38() {
			assertFalse(FileSystem.isWindowsNativeFilename("file:c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#39")
		@Test
		public void getFileExtensionCharacter_39() {
			assertTrue(FileSystem.isWindowsNativeFilename("file://C:c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#40")
		@Test
		public void getFileExtensionCharacter_40() {
			assertFalse(FileSystem.isWindowsNativeFilename("file://c.txt")); //$NON-NLS-1$
		}
	}

	@DisplayName("normalizeWindowsNativeFilename")
	@Nested
	public class NormalizeWindowsNativeFilename {

		@DisplayName("#1")
		public void normalizeWindowNativeFilename_1() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#2")
		public void normalizeWindowNativeFilename_2() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#3")
		public void normalizeWindowNativeFilename_3() {
			assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#4")
		public void normalizeWindowNativeFilename_4() {
			assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#5")
		public void normalizeWindowNativeFilename_5() {
			assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#6")
		public void normalizeWindowNativeFilename_6() {
			assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#7")
		public void normalizeWindowNativeFilename_7() {
			assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#8")
		public void normalizeWindowNativeFilename_8() {
			assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#9")
		public void normalizeWindowNativeFilename_9() {
			assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file:\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#10")
		public void normalizeWindowNativeFilename_10() {
			assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#11")
		public void normalizeWindowNativeFilename_11() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#12")
		public void normalizeWindowNativeFilename_12() {
			assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#13")
		public void normalizeWindowNativeFilename_13() {
			assertNormedFile("C:a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#14")
		public void normalizeWindowNativeFilename_14() {
			assertNormedFile("/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#15")
		public void normalizeWindowNativeFilename_15() {
			assertNormedFile("a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#16")
		public void normalizeWindowNativeFilename_16() {
			assertNormedFile("//host/a/b/c.txt", FileSystem.normalizeWindowsNativeFilename("\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#17")
		public void normalizeWindowNativeFilename_17() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#18")
		public void normalizeWindowNativeFilename_18() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#19")
		public void normalizeWindowNativeFilename_19() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:C:a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#20")
		public void normalizeWindowNativeFilename_20() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://C:a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#21")
		public void normalizeWindowNativeFilename_21() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#22")
		public void normalizeWindowNativeFilename_22() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:///a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#23")
		public void normalizeWindowNativeFilename_23() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#24")
		public void normalizeWindowNativeFilename_24() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#25")
		public void normalizeWindowNativeFilename_25() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://host/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#26")
		public void normalizeWindowNativeFilename_26() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:////host/a/b/c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#27")
		public void normalizeWindowNativeFilename_27() {
			assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#28")
		public void normalizeWindowNativeFilename_28() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#29")
		public void normalizeWindowNativeFilename_29() {
			assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file:C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#30")
		public void normalizeWindowNativeFilename_30() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file:c.txt")); //$NON-NLS-1$
		}

		@DisplayName("#31")
		public void normalizeWindowNativeFilename_31() {
			assertNormedFile("C:c.txt", FileSystem.normalizeWindowsNativeFilename("file://C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#32")
		public void normalizeWindowNativeFilename_32() {
			assertNull(FileSystem.normalizeWindowsNativeFilename("file://c.txt")); //$NON-NLS-1$
		}
	}

	@DisplayName("convertStringToFile")
	@Nested
	public class ConvertStringToFile {

		@DisplayName("(String) #1")
		@Test
		public void convertStringToFile_1() {
			assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #2")
		@Test
		public void convertStringToFile_2() {
			assertNormedFile("D:/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("D:\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #3")
		@Test
		public void convertStringToFile_3() {
			assertNormedFile("/vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("/vivus_test/export dae/yup/terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #4")
		@Test
		public void convertStringToFile_4() {
			assertNormedFile("/", FileSystem.convertStringToFile("/")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #5")
		@Test
		public void convertStringToFile_5() {
			assertNormedFile("//", FileSystem.convertStringToFile("\\\\")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #6")
		@Test
		public void convertStringToFile_6() {
			assertNormedFile("//vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #7")
		@Test
		public void convertStringToFile_7() {
			assertNormedFile("////vivus_test/export dae/yup/terrain_physx.dae", FileSystem.convertStringToFile("\\\\\\\\vivus_test\\export dae\\yup\\terrain_physx.dae")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #8")
		@Test
		public void convertStringToFile_8() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #9")
		@Test
		public void convertStringToFile_9() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #10")
		@Test
		public void convertStringToFile_10() {
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #11")
		@Test
		public void convertStringToFile_11() {
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #12")
		@Test
		public void convertStringToFile_12() {
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #13")
		@Test
		public void convertStringToFile_13() {
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file://\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #14")
		@Test
		public void convertStringToFile_14() {
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #15")
		@Test
		public void convertStringToFile_15() {
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #16")
		@Test
		public void convertStringToFile_16() {
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #17")
		@Test
		public void convertStringToFile_17() {
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file://\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #18")
		@Test
		public void convertStringToFile_18() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("C:\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #19")
		@Test
		public void convertStringToFile_19() {
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #20")
		@Test
		public void convertStringToFile_20() {
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #21")
		@Test
		public void convertStringToFile_21() {
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #22")
		@Test
		public void convertStringToFile_22() {
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #23")
		@Test
		public void convertStringToFile_23() {
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("\\\\host\\a\\b\\c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #24")
		@Test
		public void convertStringToFile_24() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:C:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #25")
		@Test
		public void convertStringToFile_25() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file:/C:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #26")
		@Test
		public void convertStringToFile_26() {
			assertNormedFile("C:/a/b/c.txt", FileSystem.convertStringToFile("file://C:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #27")
		@Test
		public void convertStringToFile_27() {
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:C:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #28")
		@Test
		public void convertStringToFile_28() {
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file:/C:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #29")
		@Test
		public void convertStringToFile_29() {
			assertNormedFile("C:a/b/c.txt", FileSystem.convertStringToFile("file://C:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #30")
		@Test
		public void convertStringToFile_30() {
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #31")
		@Test
		public void convertStringToFile_31() {
			assertNormedFile("/a/b/c.txt", FileSystem.convertStringToFile("file:///a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #32")
		@Test
		public void convertStringToFile_32() {
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file:a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #33")
		@Test
		public void convertStringToFile_33() {
			assertNormedFile("a/b/c.txt", FileSystem.convertStringToFile("file://a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #34")
		@Test
		public void convertStringToFile_34() {
			assertNormedFile("host/a/b/c.txt", FileSystem.convertStringToFile("file://host/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #35")
		@Test
		public void convertStringToFile_35() {
			assertNormedFile("//host/a/b/c.txt", FileSystem.convertStringToFile("file:////host/a/b/c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #36")
		@Test
		public void convertStringToFile_36() {
			assertNormedFile("C:c.txt", FileSystem.convertStringToFile("C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #37")
		@Test
		public void convertStringToFile_37() {
			assertNormedFile("c.txt", FileSystem.convertStringToFile("c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #38")
		@Test
		public void convertStringToFile_38() {
			assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file:C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #39")
		@Test
		public void convertStringToFile_39() {
			assertNormedFile("c.txt", FileSystem.convertStringToFile("file:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #40")
		@Test
		public void convertStringToFile_40() {
			assertNormedFile("C:c.txt", FileSystem.convertStringToFile("file://C:c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("(String) #41")
		@Test
		public void convertStringToFile_41() {
			assertNormedFile("c.txt", FileSystem.convertStringToFile("file://c.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("isJarURL")
	@Nested
	public class IsJarURL {

		@DisplayName("(URL) #1")
		@Test
		public void isJarURLURL_1() throws Exception {
			assertFalse(FileSystem.isJarURL(createHttpUrl()));
		}

		@DisplayName("(URL) #2")
		@Test
		public void isJarURLURL_2() throws Exception {
			assertTrue(FileSystem.isJarURL(createFileInJarUrl()));
		}

		@DisplayName("(URL) #3")
		@Test
		public void isJarURLURL_3() throws Exception {
			assertFalse(FileSystem.isJarURL(createFileUrlWithSpacesHardCoded()));  
		}

		@DisplayName("(URL) #4")
		@Test
		public void isJarURLURL_4() throws Exception {
			assertInlineParameterUsage(FileSystem.class, "isJarURL", URL.class); //$NON-NLS-1$
		}
	}

	@DisplayName("getJarURL")
	@Nested
	public class GetJarURL {

		@DisplayName("(URL) #1")
		@Test
		public void getJarURLURL_1() throws Exception {
			assertNull(FileSystem.getJarURL(createHttpUrl()));
		}

		@DisplayName("(URL) #2")
		@Test
		public void getJarURLURL_2() throws Exception {
			assertEquals(newURL("file:" + createJarFilenameForUrl()), FileSystem.getJarURL(createFileInJarUrl())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #3")
		@Test
		public void getJarURLURL_3() throws Exception {
			assertEquals(newURL("file:" + createJarFilenameForUrlWithSpaces()), FileSystem.getJarURL(createFileInJarUrlWithSpaces())); //$NON-NLS-1$
		}

		@DisplayName("(URL) #4")
		@Test
		public void getJarURLURL_4() throws Exception {
			assertNull(FileSystem.getJarFile(createFileUrlWithSpacesHardCoded()));
		}
	}

	@DisplayName("getJarFile")
	@Nested
	public class GetJarFile {

		@DisplayName("(URL) #1")
		@Test
		public void getJarFileURL_1() throws Exception {
			assertNull(FileSystem.getJarFile(createHttpUrl()));
		}

		@DisplayName("(URL) #2")
		@Test
		public void getJarFileURL_2() throws Exception {
			assertNormedFile(createInJarFilename(), FileSystem.getJarFile(createFileInJarUrl()));
		}

		@DisplayName("(URL) #3")
		@Test
		public void getJarFileURL_3() throws Exception {
			assertNormedFile(createInJarFilename(), FileSystem.getJarFile(createFileInJarUrlWithSpaces()));  
		}

		@DisplayName("(URL) #4")
		@Test
		public void getJarFileURL_4() throws Exception {
			assertNull(FileSystem.getJarFile(createFileUrlWithSpacesHardCoded()));
		}
	}

	@DisplayName("dirname")
	@Nested
	public class Dirname {

		@DisplayName("(URL) #1")
		@Test
		public void dirnameURL_1() throws Exception {
			assertEquals(newURL("http://toto:titi@www.arakhne.org/path/to/"), //$NON-NLS-1$
					FileSystem.dirname(createHttpUrl()));
		}

		@DisplayName("(URL) #2")
		@Test
		public void dirnameURL_2() throws Exception {
			assertEquals(newURL("jar:file:/home/test/j.jar!/org/arakhne/afc/vmutil/"), //$NON-NLS-1$
					FileSystem.dirname(createFileInJarUrl()));
		}

		@DisplayName("(URL) #3")
		@Test
		public void dirnameURL_3() throws Exception {
			assertEquals(newURL("jar:file:/the path/to/file with space.toto!/org/arakhne/afc/vmutil/"), //$NON-NLS-1$
					FileSystem.dirname(createFileInJarUrlWithSpaces()));
		}
	}

}

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

package org.arakhne.afc.inputoutput.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.net.URL;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.OperatingSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@DisplayName("SimplePathBuilder")
@SuppressWarnings("all")
public class SimplePathBuilderTest extends AbstractTestCase {

	private File userHome;

	private SimplePathBuilder builder;
	
	@BeforeEach
	public void setUp() throws Exception {
		builder = new SimplePathBuilder();
		userHome = FileSystem.getUserHomeDirectory();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		userHome = null;
		builder = null;
	}

	@DisplayName("getCurrentDirectoryURL")
	@Nested
	public class GetCurrentDirectoryURL {

		@DisplayName("#1")
		@Test
		public void testGetCurrentDirectoryURL_1() throws Exception {
			assertEquals(userHome.toURI().toURL(), builder.getCurrentDirectoryURL());
		}

		@DisplayName("#2")
		@Test
		public void testGetCurrentDirectoryURL_2() throws Exception {
			builder.setCurrentDirectory("file:./toto"); //$NON-NLS-1$
			assertEquals(new URL("file:./toto"), builder.getCurrentDirectoryURL()); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void testGetCurrentDirectoryURL_3() throws Exception {
			builder.setCurrentDirectory("http://www.multiagent.fr/toto"); //$NON-NLS-1$
			assertEquals(new URL("http://www.multiagent.fr/toto"), builder.getCurrentDirectoryURL()); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void testGetCurrentDirectoryURL_4() throws Exception {
			builder.setCurrentDirectory("https://set.utbm.fr/toto"); //$NON-NLS-1$
			assertEquals(new URL("https://set.utbm.fr/toto"), builder.getCurrentDirectoryURL()); //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void testGetCurrentDirectoryURL_5() throws Exception {
			builder.setCurrentDirectory("ftp://set.utbm.fr/toto"); //$NON-NLS-1$
			assertEquals(new URL("ftp://set.utbm.fr/toto"), builder.getCurrentDirectoryURL()); //$NON-NLS-1$
		}

		@DisplayName("#6")
		@Test
		public void testGetCurrentDirectoryURL_6() throws Exception {
			builder.setCurrentDirectory("jar:file:test.jar!/toto"); //$NON-NLS-1$
			assertEquals(new URL("jar:file:test.jar!/toto"), builder.getCurrentDirectoryURL()); //$NON-NLS-1$
		}

		@DisplayName("#7")
		@Test
		public void testGetCurrentDirectoryURL_7() throws Exception {
			builder.setCurrentDirectory("./toto"); //$NON-NLS-1$
			assertEquals(new File("./toto").toURI().toURL(), builder.getCurrentDirectoryURL()); //$NON-NLS-1$
		}
	}

	@DisplayName("getCurrentDirectoryFile")
	@Nested
	public class GetCurrentDirectoryFile {

		@DisplayName("#1")
		@Test
		public void testGetCurrentDirectoryFile_1() throws Exception {
			assertEquals(userHome, builder.getCurrentDirectoryFile());
		}

		@DisplayName("#2")
		@Test
		public void testGetCurrentDirectoryFile_2() throws Exception {
			builder.setCurrentDirectory("file:./toto"); //$NON-NLS-1$
			assertEquals(new File("./toto"), builder.getCurrentDirectoryFile()); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void testGetCurrentDirectoryFile_3() throws Exception {
			builder.setCurrentDirectory("http://www.multiagent.fr/toto"); //$NON-NLS-1$
			assertEquals(userHome, builder.getCurrentDirectoryFile());
		}

		@DisplayName("#4")
		@Test
		public void testGetCurrentDirectoryFile_4() throws Exception {
			builder.setCurrentDirectory("https://set.utbm.fr/toto"); //$NON-NLS-1$
			assertEquals(userHome, builder.getCurrentDirectoryFile());
		}

		@DisplayName("#5")
		@Test
		public void testGetCurrentDirectoryFile_5() throws Exception {
			builder.setCurrentDirectory("ftp://set.utbm.fr/toto"); //$NON-NLS-1$
			assertEquals(userHome, builder.getCurrentDirectoryFile());
		}

		@DisplayName("#6")
		@Test
		public void testGetCurrentDirectoryFile_6() throws Exception {
			builder.setCurrentDirectory("jar:file:test.jar!/toto"); //$NON-NLS-1$
			assertEquals(userHome, builder.getCurrentDirectoryFile());
		}

		@DisplayName("#7")
		@Test
		public void testGetCurrentDirectoryFile_7() throws Exception {
			builder.setCurrentDirectory("./toto"); //$NON-NLS-1$
			assertEquals(new File("./toto"), builder.getCurrentDirectoryFile()); //$NON-NLS-1$
		}
	}

	@DisplayName("getCurrentDirectoryString")
	@Nested
	public class GetCurrentDirectoryString {

		@DisplayName("#1")
		@Test
		public void testGetCurrentDirectoryString_1() throws Exception {
			assertEquals(userHome.toURI().toURL().toExternalForm(), builder.getCurrentDirectoryString());
		}

		@DisplayName("#2")
		@Test
		public void testGetCurrentDirectoryString_2() throws Exception {
			builder.setCurrentDirectory("file:./toto"); //$NON-NLS-1$
			assertEquals("file:./toto", builder.getCurrentDirectoryString()); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void testGetCurrentDirectoryString_3() throws Exception {
			builder.setCurrentDirectory("http://www.multiagent.fr/toto"); //$NON-NLS-1$
			assertEquals("http://www.multiagent.fr/toto", builder.getCurrentDirectoryString()); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void testGetCurrentDirectoryString_4() throws Exception {
			builder.setCurrentDirectory("https://set.utbm.fr/toto"); //$NON-NLS-1$
			assertEquals("https://set.utbm.fr/toto", builder.getCurrentDirectoryString()); //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void testGetCurrentDirectoryString_5() throws Exception {
			builder.setCurrentDirectory("ftp://set.utbm.fr/toto"); //$NON-NLS-1$
			assertEquals("ftp://set.utbm.fr/toto", builder.getCurrentDirectoryString()); //$NON-NLS-1$
		}

		@DisplayName("#6")
		@Test
		public void testGetCurrentDirectoryString_6() throws Exception {
			builder.setCurrentDirectory("jar:file:test.jar!/toto"); //$NON-NLS-1$
			assertEquals("jar:file:test.jar!/toto", builder.getCurrentDirectoryString()); //$NON-NLS-1$
		}

		@DisplayName("#7")
		@Test
		public void testGetCurrentDirectoryString_7() throws Exception {
			builder.setCurrentDirectory("./toto"); //$NON-NLS-1$
			assertEquals(new File("./toto").toURI().toURL().toExternalForm(), builder.getCurrentDirectoryString()); //$NON-NLS-1$
		}
	}
	
	@DisplayName("makeAbsolute")
	@Nested
	public class MakeAbsolute {

		@DisplayName("(File) window #1")
		@Test
		public void testMakeAbsoluteFile_win_1() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			URL aexpected = new URL("file:/C:/toto"); //$NON-NLS-1$
			File aref = new File("C:\\toto"); //$NON-NLS-1$
			assertEquals(aexpected, builder.makeAbsolute(aref));
		}

		@DisplayName("(File) window #2")
		@Test
		public void testMakeAbsoluteFile_win_2() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			URL rexpected1 = new File(FileSystem.getUserHomeDirectory(), "./toto").toURI().toURL(); //$NON-NLS-1$
			File rref = new File(".\\toto"); //$NON-NLS-1$
			assertEquals(rexpected1, builder.makeAbsolute(rref));
		}

		@DisplayName("(File) window #3")
		@Test
		public void testMakeAbsoluteFile_win_3() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			File root1 = new File("C:\\toto"); //$NON-NLS-1$
			URL aexpected = new URL("file:/C:/toto"); //$NON-NLS-1$
			File aref = new File("C:\\toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root1);
			assertEquals(aexpected, builder.makeAbsolute(aref));
		}

		@DisplayName("(File) window #4")
		@Test
		public void testMakeAbsoluteFile_win_4() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			File root1 = new File("C:\\toto"); //$NON-NLS-1$
			URL rexpected2 = new File(root1, ".\\toto").toURI().toURL(); //$NON-NLS-1$
			File rref = new File(".\\toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root1);
			assertEquals(rexpected2, builder.makeAbsolute(rref));
		}

		@DisplayName("(File) window #5")
		@Test
		public void testMakeAbsoluteFile_win_5() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			URL root2 = new URL("http://www.multiagent.fr/root"); //$NON-NLS-1$
			URL aexpected = new URL("file:/C:/toto"); //$NON-NLS-1$
			File aref = new File("C:\\toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root2);
			assertEquals(aexpected, builder.makeAbsolute(aref));
		}

		@DisplayName("(File) window #6")
		@Test
		public void testMakeAbsoluteFile_win_6() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			URL root2 = new URL("http://www.multiagent.fr/root"); //$NON-NLS-1$
			URL rexpected3 = new URL("http://www.multiagent.fr/root/./toto"); //$NON-NLS-1$
			File rref = new File(".\\toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root2);
			assertEquals(rexpected3, builder.makeAbsolute(rref));
		}
	
		@DisplayName("(File) other #1")
		@Test
		public void testMakeAbsoluteFile_other_1() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			URL aexpected = new URL("file:/toto"); //$NON-NLS-1$
			File aref = new File("/toto"); //$NON-NLS-1$
			assertEquals(aexpected, builder.makeAbsolute(aref));
		}
		
		@DisplayName("(File) other #2")
		@Test
		public void testMakeAbsoluteFile_other_2() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			URL rexpected1 = new File(FileSystem.getUserHomeDirectory(), "./toto").toURI().toURL(); //$NON-NLS-1$
			File rref = new File("./toto"); //$NON-NLS-1$
			assertEquals(rexpected1, builder.makeAbsolute(rref));
		}
		
		@DisplayName("(File) other #3")
		@Test
		public void testMakeAbsoluteFile_other_3() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			File root1 = new File("/toto"); //$NON-NLS-1$
			URL aexpected = new URL("file:/toto"); //$NON-NLS-1$
			File aref = new File("/toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root1);
			assertEquals(aexpected, builder.makeAbsolute(aref));
		}
		
		@DisplayName("(File) other #4")
		@Test
		public void testMakeAbsoluteFile_other_4() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			File root1 = new File("/toto"); //$NON-NLS-1$
			URL rexpected2 = new File(root1, "./toto").toURI().toURL(); //$NON-NLS-1$
			File rref = new File("./toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root1);
			assertEquals(rexpected2, builder.makeAbsolute(rref));
		}
		
		@DisplayName("(File) other #5")
		@Test
		public void testMakeAbsoluteFile_other_5() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			URL root2 = new URL("http://www.multiagent.fr/root"); //$NON-NLS-1$
			URL aexpected = new URL("file:/toto"); //$NON-NLS-1$
			File aref = new File("/toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root2);
			assertEquals(aexpected, builder.makeAbsolute(aref));
		}
		
		@DisplayName("(File) other #6")
		@Test
		public void testMakeAbsoluteFile_other_6() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			URL root2 = new URL("http://www.multiagent.fr/root"); //$NON-NLS-1$
			URL rexpected3 = new URL("http://www.multiagent.fr/root/./toto"); //$NON-NLS-1$
			File rref = new File("./toto"); //$NON-NLS-1$
			builder.setCurrentDirectory(root2);
			assertEquals(rexpected3, builder.makeAbsolute(rref));
		}
		
		@DisplayName("(URL) window #")
		@Test
		public void testMakeAbsoluteURL_win_() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			assertEquals("http://www.multiagent.fr/toto", //$NON-NLS-1$
					builder.makeAbsolute(new URL("http://www.multiagent.fr/toto")).toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) window #2")
		@Test
		public void testMakeAbsoluteURL_win_2() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			final String current = "file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			assertEquals(current + "//toto", //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:/toto")).toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) window #3")
		@Test
		public void testMakeAbsoluteURL_win_3() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			final String current = "file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			assertEquals(current + "/toto", //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:toto")).toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) window #4")
		@Test
		public void testMakeAbsoluteURL_win_4() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals("file:/C:/root//toto", //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:/toto")).toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) window #5")
		@Test
		public void testMakeAbsoluteURL_win_5() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals("file:/C:/root/toto", //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:toto")).toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) window #6")
		@Test
		public void testMakeAbsoluteURL_win_6() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$			
			assertEquals("http://www.multiagent.fr/root//toto", //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:/toto")).toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) window #7")
		@Test
		public void testMakeAbsoluteURL_win_7() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$			
			assertEquals("http://www.multiagent.fr/root/toto", //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:toto")).toExternalForm()); //$NON-NLS-1$
		}
	
		@DisplayName("(URL) other #1")
		@Test
		public void testMakeAbsoluteURL_other_1() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new URL("http://www.multiagent.fr/toto"), //$NON-NLS-1$
					builder.makeAbsolute(new URL("http://www.multiagent.fr/toto"))); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) other #2")
		@Test
		public void testMakeAbsoluteURL_other_2() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new URL("file:/toto"), //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:/toto"))); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) other #3")
		@Test
		public void testMakeAbsoluteURL_other_3() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new File(FileSystem.getUserHomeDirectory(), "toto").toURI().toURL(), //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:toto"))); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) other #4")
		@Test
		public void testMakeAbsoluteURL_other_4() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals(new URL("file:/toto"), //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:/toto"))); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) other #5")
		@Test
		public void testMakeAbsoluteURL_other_5() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals(new File("/root", "toto").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
					builder.makeAbsolute(new URL("file:toto"))); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) other #6")
		@Test
		public void testMakeAbsoluteURL_other_6() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$			
			assertEquals(new URL("file:/toto"), //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:/toto"))); //$NON-NLS-1$
		}
		
		@DisplayName("(URL) other #7")
		@Test
		public void testMakeAbsoluteURL_other_7() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
			assertEquals(new URL("http://www.multiagent.fr/root/toto"), //$NON-NLS-1$
					builder.makeAbsolute(new URL("file:toto"))); //$NON-NLS-1$
		}
	
		@DisplayName("(String) window #1")
		@Test
		public void testMakeAbsoluteString_win_1() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new URL("http://www.multiagent.fr/toto"), //$NON-NLS-1$
					builder.makeAbsolute("http://www.multiagent.fr/toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #2")
		@Test
		public void testMakeAbsoluteString_win_2() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			final String current = "file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			assertEquals(current + "//toto", //$NON-NLS-1$
					builder.makeAbsolute("file:/toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #3")
		@Test
		public void testMakeAbsoluteString_win_3() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			final String current = "file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			assertEquals(current + "/toto", //$NON-NLS-1$
					builder.makeAbsolute("file:toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #4")
		@Test
		public void testMakeAbsoluteString_win_4() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			final String current = "file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			assertEquals(current + "/toto", //$NON-NLS-1$
					builder.makeAbsolute("toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #5")
		@Test
		public void testMakeAbsoluteString_win_5() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals("file:/C:/root//toto", //$NON-NLS-1$ 
					builder.makeAbsolute("file:/toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #6")
		@Test
		public void testMakeAbsoluteString_win_6() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals("file:/C:/root/toto", //$NON-NLS-1$
					builder.makeAbsolute("file:toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #7")
		@Test
		public void testMakeAbsoluteString_win_7() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals("file:/C:/root/toto", //$NON-NLS-1$
					builder.makeAbsolute("toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #8")
		@Test
		public void testMakeAbsoluteString_win_8() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
			assertEquals("http://www.multiagent.fr/root//toto", //$NON-NLS-1$
					builder.makeAbsolute("file:/toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #9")
		@Test
		public void testMakeAbsoluteString_win_9() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
			assertEquals("http://www.multiagent.fr/root/toto", //$NON-NLS-1$
					builder.makeAbsolute("file:toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) window #10")
		@Test
		public void testMakeAbsoluteString_win_10() throws Exception {
			assumeTrue(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
			assertEquals("http://www.multiagent.fr/root/toto", //$NON-NLS-1$
					builder.makeAbsolute("toto").toExternalForm()); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #1")
		@Test
		public void testMakeAbsoluteString_other_1() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new URL("http://www.multiagent.fr/toto"), //$NON-NLS-1$
					builder.makeAbsolute("http://www.multiagent.fr/toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #2")
		@Test
		public void testMakeAbsoluteString_other_2() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new URL("file:/toto"), //$NON-NLS-1$
					builder.makeAbsolute("file:/toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #3")
		@Test
		public void testMakeAbsoluteString_other_3() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new File(FileSystem.getUserHomeDirectory(), "toto").toURI().toURL(), //$NON-NLS-1$
					builder.makeAbsolute("file:toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #4")
		@Test
		public void testMakeAbsoluteString_other_4() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			assertEquals(new File(FileSystem.getUserHomeDirectory(), "toto").toURI().toURL(), //$NON-NLS-1$
					builder.makeAbsolute("toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #5")
		@Test
		public void testMakeAbsoluteString_other_5() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals(new URL("file:/toto"), //$NON-NLS-1$
					builder.makeAbsolute("file:/toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #6")
		@Test
		public void testMakeAbsoluteString_other_6() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals(new File("/root", "toto").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
					builder.makeAbsolute("file:toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #7")
		@Test
		public void testMakeAbsoluteString_other_7() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
			assertEquals(new File("/root", "toto").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
					builder.makeAbsolute("toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #8")
		@Test
		public void testMakeAbsoluteString_other_8() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
			assertEquals(new URL("file:/toto"), //$NON-NLS-1$
					builder.makeAbsolute("file:/toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #9")
		@Test
		public void testMakeAbsoluteString_other_9() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
			assertEquals(new URL("http://www.multiagent.fr/root/toto"), //$NON-NLS-1$
					builder.makeAbsolute("file:toto")); //$NON-NLS-1$
		}
		
		@DisplayName("(String) other #10")
		@Test
		public void testMakeAbsoluteString_other_10() throws Exception {
			assumeFalse(OperatingSystem.WIN.isCurrentOS());
			builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
			assertEquals(new URL("http://www.multiagent.fr/root/toto"), //$NON-NLS-1$
					builder.makeAbsolute("toto")); //$NON-NLS-1$
		}
	}

}
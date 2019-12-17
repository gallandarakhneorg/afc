/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.OperatingSystem;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public class SimplePathBuilderTest extends AbstractTestCase {

	private File userHome;

	private SimplePathBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		this.builder = new SimplePathBuilder();
		this.userHome = FileSystem.getUserHomeDirectory();
	}
	
	@After
	public void tearDown() throws Exception {
		this.userHome = null;
		this.builder = null;
	}

	@Test
	public void testGetCurrentDirectoryURL() throws Exception {
		assertEquals(this.userHome.toURI().toURL(), this.builder.getCurrentDirectoryURL());

		this.builder.setCurrentDirectory("file:./toto"); //$NON-NLS-1$
		assertEquals(new URL("file:./toto"), this.builder.getCurrentDirectoryURL()); //$NON-NLS-1$
		
		this.builder.setCurrentDirectory("http://www.multiagent.fr/toto"); //$NON-NLS-1$
		assertEquals(new URL("http://www.multiagent.fr/toto"), this.builder.getCurrentDirectoryURL()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("https://set.utbm.fr/toto"); //$NON-NLS-1$
		assertEquals(new URL("https://set.utbm.fr/toto"), this.builder.getCurrentDirectoryURL()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("ftp://set.utbm.fr/toto"); //$NON-NLS-1$
		assertEquals(new URL("ftp://set.utbm.fr/toto"), this.builder.getCurrentDirectoryURL()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("jar:file:test.jar!/toto"); //$NON-NLS-1$
		assertEquals(new URL("jar:file:test.jar!/toto"), this.builder.getCurrentDirectoryURL()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("./toto"); //$NON-NLS-1$
		assertEquals(new File("./toto").toURI().toURL(), this.builder.getCurrentDirectoryURL()); //$NON-NLS-1$
	}

	@Test
	public void testGetCurrentDirectoryFile() throws Exception {
		assertEquals(this.userHome, this.builder.getCurrentDirectoryFile());

		this.builder.setCurrentDirectory("file:./toto"); //$NON-NLS-1$
		assertEquals(new File("./toto"), this.builder.getCurrentDirectoryFile()); //$NON-NLS-1$
		
		this.builder.setCurrentDirectory("http://www.multiagent.fr/toto"); //$NON-NLS-1$
		assertEquals(this.userHome, this.builder.getCurrentDirectoryFile());

		this.builder.setCurrentDirectory("https://set.utbm.fr/toto"); //$NON-NLS-1$
		assertEquals(this.userHome, this.builder.getCurrentDirectoryFile());

		this.builder.setCurrentDirectory("ftp://set.utbm.fr/toto"); //$NON-NLS-1$
		assertEquals(this.userHome, this.builder.getCurrentDirectoryFile());

		this.builder.setCurrentDirectory("jar:file:test.jar!/toto"); //$NON-NLS-1$
		assertEquals(this.userHome, this.builder.getCurrentDirectoryFile());

		this.builder.setCurrentDirectory("./toto"); //$NON-NLS-1$
		assertEquals(new File("./toto"), this.builder.getCurrentDirectoryFile()); //$NON-NLS-1$
	}

	@Test
	public void testGetCurrentDirectoryString() throws Exception {
		assertEquals(this.userHome.toURI().toURL().toExternalForm(), this.builder.getCurrentDirectoryString());

		this.builder.setCurrentDirectory("file:./toto"); //$NON-NLS-1$
		assertEquals("file:./toto", this.builder.getCurrentDirectoryString()); //$NON-NLS-1$
		
		this.builder.setCurrentDirectory("http://www.multiagent.fr/toto"); //$NON-NLS-1$
		assertEquals("http://www.multiagent.fr/toto", this.builder.getCurrentDirectoryString()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("https://set.utbm.fr/toto"); //$NON-NLS-1$
		assertEquals("https://set.utbm.fr/toto", this.builder.getCurrentDirectoryString()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("ftp://set.utbm.fr/toto"); //$NON-NLS-1$
		assertEquals("ftp://set.utbm.fr/toto", this.builder.getCurrentDirectoryString()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("jar:file:test.jar!/toto"); //$NON-NLS-1$
		assertEquals("jar:file:test.jar!/toto", this.builder.getCurrentDirectoryString()); //$NON-NLS-1$

		this.builder.setCurrentDirectory("./toto"); //$NON-NLS-1$
		assertEquals(new File("./toto").toURI().toURL().toExternalForm(), this.builder.getCurrentDirectoryString()); //$NON-NLS-1$
	}
	
	@Test
	public void testMakeAbsoluteFile() throws Exception {
		//System.out.println("currentOS=" + System.getProperty("os.name").trim().toLowerCase()); //$NON-NLS-1$ //$NON-NLS-2$
		//System.out.println("currentOS=" + OperatingSystem.getCurrentOSName()); //$NON-NLS-1$
		//System.out.println("currentOS=" + OperatingSystem.getCurrentOS()); //$NON-NLS-1$
		if (OperatingSystem.WIN.isCurrentOS()) {
			makeAbsoluteFile_win();
		} else {
			makeAbsoluteFile_unix();
		}
	}

	private void makeAbsoluteFile_win() throws Exception {
		File root1 = new File("C:\\toto"); //$NON-NLS-1$
		URL root2 = new URL("http://www.multiagent.fr/root"); //$NON-NLS-1$
		URL aexpected = new URL("file:/C:/toto"); //$NON-NLS-1$
		File aref = new File("C:\\toto"); //$NON-NLS-1$
		URL rexpected1 = new File(FileSystem.getUserHomeDirectory(), "./toto").toURI().toURL(); //$NON-NLS-1$
		URL rexpected2 = new File(root1, ".\\toto").toURI().toURL(); //$NON-NLS-1$
		URL rexpected3 = new URL("http://www.multiagent.fr/root/./toto"); //$NON-NLS-1$
		File rref = new File(".\\toto"); //$NON-NLS-1$
		
		assertEquals(aexpected, this.builder.makeAbsolute(aref));

		assertEquals(rexpected1, this.builder.makeAbsolute(rref));

		this.builder.setCurrentDirectory(root1);
		
		assertEquals(aexpected, this.builder.makeAbsolute(aref));

		assertEquals(rexpected2, this.builder.makeAbsolute(rref));

		this.builder.setCurrentDirectory(root2);
		
		assertEquals(aexpected, this.builder.makeAbsolute(aref));

		assertEquals(rexpected3, this.builder.makeAbsolute(rref));
	}

	private void makeAbsoluteFile_unix() throws Exception {
		File root1 = new File("/toto"); //$NON-NLS-1$
		URL root2 = new URL("http://www.multiagent.fr/root"); //$NON-NLS-1$
		URL aexpected = new URL("file:/toto"); //$NON-NLS-1$
		File aref = new File("/toto"); //$NON-NLS-1$
		URL rexpected1 = new File(FileSystem.getUserHomeDirectory(), "./toto").toURI().toURL(); //$NON-NLS-1$
		URL rexpected2 = new File(root1, "./toto").toURI().toURL(); //$NON-NLS-1$
		URL rexpected3 = new URL("http://www.multiagent.fr/root/./toto"); //$NON-NLS-1$
		File rref = new File("./toto"); //$NON-NLS-1$
		
		assertEquals(aexpected, this.builder.makeAbsolute(aref));

		assertEquals(rexpected1, this.builder.makeAbsolute(rref));

		this.builder.setCurrentDirectory(root1);
		
		assertEquals(aexpected, this.builder.makeAbsolute(aref));

		assertEquals(rexpected2, this.builder.makeAbsolute(rref));

		this.builder.setCurrentDirectory(root2);
		
		assertEquals(aexpected, this.builder.makeAbsolute(aref));

		assertEquals(rexpected3, this.builder.makeAbsolute(rref));
	}
	
	@Test
	public void testMakeAbsoluteURL() throws Exception {
		//System.out.println("currentOS=" + System.getProperty("os.name").trim().toLowerCase()); //$NON-NLS-1$ //$NON-NLS-2$
		//System.out.println("currentOS=" + OperatingSystem.getCurrentOSName()); //$NON-NLS-1$
		//System.out.println("currentOS=" + OperatingSystem.getCurrentOS()); //$NON-NLS-1$
		if (OperatingSystem.WIN.isCurrentOS()) {
			makeAbsoluteURL_win();
		} else {
			makeAbsoluteURL_unix();
		}
	}

	private void makeAbsoluteURL_win() throws Exception {
		assertEquals("http://www.multiagent.fr/toto", //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("http://www.multiagent.fr/toto")).toExternalForm()); //$NON-NLS-1$

		final String current = "file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		assertEquals(current + "//toto", //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:/toto")).toExternalForm()); //$NON-NLS-1$

		assertEquals(current + "/toto", //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:toto")).toExternalForm()); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
		
		assertEquals("file:/C:/root//toto", //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:/toto")).toExternalForm()); //$NON-NLS-1$

		assertEquals("file:/C:/root/toto", //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:toto")).toExternalForm()); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
		
		assertEquals("http://www.multiagent.fr/root//toto", //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:/toto")).toExternalForm()); //$NON-NLS-1$

		assertEquals("http://www.multiagent.fr/root/toto", //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:toto")).toExternalForm()); //$NON-NLS-1$
	}

	private void makeAbsoluteURL_unix() throws Exception {
		assertEquals(new URL("http://www.multiagent.fr/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("http://www.multiagent.fr/toto"))); //$NON-NLS-1$

		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:/toto"))); //$NON-NLS-1$

		assertEquals(new File(FileSystem.getUserHomeDirectory(), "toto").toURI().toURL(), //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:toto"))); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
		
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:/toto"))); //$NON-NLS-1$

		assertEquals(new File("/root", "toto").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
				this.builder.makeAbsolute(new URL("file:toto"))); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
		
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:/toto"))); //$NON-NLS-1$

		assertEquals(new URL("http://www.multiagent.fr/root/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute(new URL("file:toto"))); //$NON-NLS-1$
	}

	@Test
	public void testMakeAbsoluteString() throws Exception {
		//System.out.println("currentOS=" + System.getProperty("os.name").trim().toLowerCase()); //$NON-NLS-1$ //$NON-NLS-2$
		//System.out.println("currentOS=" + OperatingSystem.getCurrentOSName()); //$NON-NLS-1$
		//System.out.println("currentOS=" + OperatingSystem.getCurrentOS()); //$NON-NLS-1$
		if (OperatingSystem.WIN.isCurrentOS()) {
			makeAbsoluteString_win();
		} else {
			makeAbsoluteString_unix();
		}
	}

	private void makeAbsoluteString_win() throws Exception {
		assertEquals(new URL("http://www.multiagent.fr/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute("http://www.multiagent.fr/toto")); //$NON-NLS-1$

		final String current = "file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\", "/");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		
		assertEquals(current + "//toto", //$NON-NLS-1$
				this.builder.makeAbsolute("file:/toto").toExternalForm()); //$NON-NLS-1$

		assertEquals(current + "/toto", //$NON-NLS-1$
				this.builder.makeAbsolute("file:toto").toExternalForm()); //$NON-NLS-1$

		assertEquals(current + "/toto", //$NON-NLS-1$
				this.builder.makeAbsolute("toto").toExternalForm()); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
		
		assertEquals("file:/C:/root//toto", //$NON-NLS-1$ 
				this.builder.makeAbsolute("file:/toto").toExternalForm()); //$NON-NLS-1$

		assertEquals("file:/C:/root/toto", //$NON-NLS-1$
				this.builder.makeAbsolute("file:toto").toExternalForm()); //$NON-NLS-1$

		assertEquals("file:/C:/root/toto", //$NON-NLS-1$
				this.builder.makeAbsolute("toto").toExternalForm()); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
		
		assertEquals("http://www.multiagent.fr/root//toto", //$NON-NLS-1$
				this.builder.makeAbsolute("file:/toto").toExternalForm()); //$NON-NLS-1$

		assertEquals("http://www.multiagent.fr/root/toto", //$NON-NLS-1$
				this.builder.makeAbsolute("file:toto").toExternalForm()); //$NON-NLS-1$

		assertEquals("http://www.multiagent.fr/root/toto", //$NON-NLS-1$
				this.builder.makeAbsolute("toto").toExternalForm()); //$NON-NLS-1$
	}

	private void makeAbsoluteString_unix() throws Exception {
		assertEquals(new URL("http://www.multiagent.fr/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute("http://www.multiagent.fr/toto")); //$NON-NLS-1$

		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute("file:/toto")); //$NON-NLS-1$

		assertEquals(new File(FileSystem.getUserHomeDirectory(), "toto").toURI().toURL(), //$NON-NLS-1$
				this.builder.makeAbsolute("file:toto")); //$NON-NLS-1$

		assertEquals(new File(FileSystem.getUserHomeDirectory(), "toto").toURI().toURL(), //$NON-NLS-1$
				this.builder.makeAbsolute("toto")); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new File("/root")); //$NON-NLS-1$
		
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute("file:/toto")); //$NON-NLS-1$

		assertEquals(new File("/root", "toto").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
				this.builder.makeAbsolute("file:toto")); //$NON-NLS-1$

		assertEquals(new File("/root", "toto").toURI().toURL(), //$NON-NLS-1$ //$NON-NLS-2$
				this.builder.makeAbsolute("toto")); //$NON-NLS-1$

		this.builder.setCurrentDirectory(new URL("http://www.multiagent.fr/root")); //$NON-NLS-1$
		
		assertEquals(new URL("file:/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute("file:/toto")); //$NON-NLS-1$

		assertEquals(new URL("http://www.multiagent.fr/root/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute("file:toto")); //$NON-NLS-1$

		assertEquals(new URL("http://www.multiagent.fr/root/toto"), //$NON-NLS-1$
				this.builder.makeAbsolute("toto")); //$NON-NLS-1$
	}

}
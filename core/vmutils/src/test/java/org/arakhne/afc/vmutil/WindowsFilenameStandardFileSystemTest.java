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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FileSystem for Windows OS")
@SuppressWarnings("all")
public class WindowsFilenameStandardFileSystemTest extends AbstractSpecificFileSystemTestCase {

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

	@DisplayName("removeExtension")
	@Nested
	public class RemoveExtension {

		@Test
		@DisplayName("(URL) #1")
		public void removeExtensionURL_1() throws Exception {
			assertEquals(newURL("file:/C:/home/test.x.z"), FileSystem.removeExtension(createAbsoluteStandardFileUrl())); //$NON-NLS-1$
		}
	
		@Test
		@DisplayName("(URL) #2")
		public void removeExtensionURL_2() throws Exception {
			assertEquals(newURL("file:/C:/home"), FileSystem.removeExtension(createAbsoluteFolderUrl())); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #1")
		public void removeExtensionFile_1() {
			assertEquals(newFile("C:\\home\\test.x.z", false), FileSystem.removeExtension(newFile(getAbsoluteStandardFilename(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #2")
		public void removeExtensionFile_2() {
			assertEquals(newFile("C:\\home", false), FileSystem.removeExtension(newFile(getAbsoluteFoldername(), false))); //$NON-NLS-1$
		}

		@Test
		@DisplayName("(File) #3")
		public void removeExtensionFile_3() {
			assertEquals(newFile("C:\\the path\\to\\file with space", false), FileSystem.removeExtension(newFile(getStandardFilenameWithSpaces(), false))); //$NON-NLS-1$
		}
	}

	@DisplayName("replaceExtension")
	@Nested
	public class ReplaceExtension {

		@Test
		@DisplayName("(URL,String) #1")
		public void replaceExtensionURL_1() throws Exception {
			assertEquals(newURL("file:/C:/home/test.x.z.xyz"), FileSystem.replaceExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@Test
		@DisplayName("(URL,String) #2")
		public void replaceExtensionURL_2() throws Exception {
			assertEquals(newURL("file:/C:/home.xyz"), FileSystem.replaceExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(File,String) #1")
		public void replaceExtensionFile_1() {
			assertEquals(newFile("C:\\home\\test.x.z.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(File,String) #2")
		public void replaceExtensionFile_2() {
			assertEquals(newFile("C:\\home.xyz", false), FileSystem.replaceExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(File,String) #3")
		public void replaceExtensionFile_3() {
			assertEquals(newFile("C:\\the path\\to\\file with space.xyz", false), FileSystem.replaceExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("addExtension")
	@Nested
	public class AddExtension {

		@Test
		@DisplayName("(URL,String) #1")
		public void addExtensionURL_1() throws Exception {
			assertEquals(newURL("file:/C:/home/test.x.z.z.xyz"), FileSystem.addExtension(createAbsoluteStandardFileUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@Test
		@DisplayName("(URL,String) #2")
		public void addExtensionURL_2() throws Exception {
			assertEquals(newURL("file:/C:/home.xyz"), FileSystem.addExtension(createAbsoluteFolderUrl(), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(File,String) #1")
		public void addExtensionFile_1() {
			assertEquals(newFile("C:\\home\\test.x.z.z.xyz", false), FileSystem.addExtension(newFile(getAbsoluteStandardFilename(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(File,String) #2")
		public void addExtensionFile_2() {
			assertEquals(newFile("C:\\home.xyz", false), FileSystem.addExtension(newFile(getAbsoluteFoldername(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Test
		@DisplayName("(File,String) #3")
		public void addExtensionFile_3() {
			assertEquals(newFile("C:\\the path\\to\\file with space.toto.xyz", false), FileSystem.addExtension(newFile(getStandardFilenameWithSpaces(), false), ".xyz")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

}

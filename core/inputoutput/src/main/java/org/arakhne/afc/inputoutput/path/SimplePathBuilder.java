/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.URISchemeType;

/** Simple implementation of a tool that permits to convert filenames from
 * absolute to relative and the opposite..
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class SimplePathBuilder implements PathBuilder {

	private File currentFile;

	private URL currentURL;

	/** Constructor.
	 */
	public SimplePathBuilder() {
		//
	}

	/** Constructor with current directory.
	 * @param currentDirectory is the default/current directory used to make absolute the reltive paths.
	 */
	public SimplePathBuilder(String currentDirectory) {
		setCurrentDirectory(currentDirectory);
	}

	/** Constructor with current directory.
	 * @param currentDirectory is the default/current directory used to make absolute the reltive paths.
	 */
	public SimplePathBuilder(File currentDirectory) {
		setCurrentDirectory(currentDirectory);
	}

	/** Constructor with current directory.
	 * @param currentDirectory is the default/current directory used to make absolute the reltive paths.
	 */
	public SimplePathBuilder(URL currentDirectory) {
		setCurrentDirectory(currentDirectory);
	}

	@Override
	public void setCurrentDirectory(String currentDirectory) {
		if (currentDirectory == null) {
			this.currentFile =  null;
			this.currentURL = null;
		} else {
			try {
				this.currentURL = new URL(currentDirectory);
				this.currentFile = null;
			} catch (MalformedURLException exception) {
				this.currentFile = new File(currentDirectory);
				this.currentURL = null;
			}
		}
	}

	@Override
	public void setCurrentDirectory(File currentDirectory) {
		this.currentFile = currentDirectory;
		this.currentURL = null;
	}

	@Override
	public void setCurrentDirectory(URL currentDirectory) {
		this.currentURL = currentDirectory;
		this.currentFile = null;
	}

	@Override
	public File getCurrentDirectoryFile() {
		if (this.currentFile != null) {
			return this.currentFile;
		}
		if (URISchemeType.FILE.isURL(this.currentURL)) {
			return new File(this.currentURL.getPath());
		}
		try {
			return FileSystem.getUserHomeDirectory();
		} catch (IOException exception) {
			return new File((String) null);
		}
	}

	@Override
	public URL getCurrentDirectoryURL() {
		try {
			if (this.currentFile != null) {
				return this.currentFile.toURI().toURL();
			}
			if (this.currentURL != null) {
				return this.currentURL;
			}
			try {
				return FileSystem.getUserHomeDirectory().toURI().toURL();
			} catch (IOException exception) {
				return new File((String) null).toURI().toURL();
			}
		} catch (MalformedURLException exception) {
			return null;
		}
	}

}

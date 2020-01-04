/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.inputoutput.filefilter;

import java.io.File;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.locale.Locale;

/** Abstract implementation of a file filter that may be
 * used in all the standard Java tools.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractFileFilter implements FileFilter {

	private final boolean acceptDirectories;

	private final String description;

	private final String[] extensions;

	/** Constructor.
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 * @param description is the description of the file filter.
	 * @param extensions are the supported extensions.
	 */
	public AbstractFileFilter(boolean acceptDirectories, String description, String... extensions) {
		this.acceptDirectories = acceptDirectories;
		this.extensions = extensions;

		final StringBuilder b = new StringBuilder();
		for (int i = 0; i < this.extensions.length; ++i) {
			final String ext;
			if (this.extensions[i].startsWith(FileSystem.EXTENSION_SEPARATOR)) {
				ext = this.extensions[i].substring(1);
			} else {
				ext = this.extensions[i];
			}
			final String name = Locale.getString("EXTENSION_FORMAT", ext); //$NON-NLS-1$
			if (i > 0) {
				b.append(Locale.getString("EXTENSION_APPEND", this.extensions[i])); //$NON-NLS-1$
			} else {
				b.append(name);
			}
		}
		this.description = Locale.getString("EXTENSION_STRING", description, b.toString()); //$NON-NLS-1$
	}

	@Pure
	@Override
	public final boolean accept(File file) {
		if (file.isDirectory()) {
			return this.acceptDirectories;
		}
		for (final String ext : this.extensions) {
			if (FileSystem.hasExtension(file, ext)) {
				return true;
			}
		}
		return false;
	}

	@Pure
	@Override
	public final boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}

	@Pure
	@Override
	public final String getDescription() {
		return this.description;
	}

	@Pure
	@Override
	public final String[] getExtensions() {
		return this.extensions;
	}

}

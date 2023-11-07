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

package org.arakhne.afc.inputoutput.filefilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

/** Multi file filter.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiFileFilter implements FileFilter {

	private final boolean acceptDirectories;

	private final FileFilter[] filters;

	private final String description;

	/** Construct.
	 * @param description description.
	 * @param filters filters.
	 */
	public MultiFileFilter(String description, FileFilter... filters) {
		this(true, description, filters);
	}

	/** Constructor.
	 * @param acceptDirectories1 is {@code true} to
	 *     permit to this file filter to accept directories;
	 *     {@code false} if the directories should not
	 *     match.
	 * @param description description.
	 * @param filters filters.
	 */
	public MultiFileFilter(boolean acceptDirectories1, String description, FileFilter... filters) {
		this.acceptDirectories = acceptDirectories1;
		this.filters = filters;
		this.description = description;
	}

	@Pure
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return this.acceptDirectories;
		}
		for (final FileFilter ff : this.filters) {
			if (ff.accept(file)) {
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
	public String getDescription() {
		return this.description;
	}

	@Pure
	@Override
	public String[] getExtensions() {
		final List<String> extensions = new ArrayList<>();
		for (final FileFilter ff : this.filters) {
			extensions.addAll(Arrays.asList(ff.getExtensions()));
		}
		final String[] tab = new String[extensions.size()];
		extensions.toArray(tab);
		return tab;
	}

}

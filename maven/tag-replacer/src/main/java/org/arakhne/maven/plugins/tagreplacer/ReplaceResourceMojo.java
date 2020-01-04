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

package org.arakhne.maven.plugins.tagreplacer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.maven.plugin.MojoExecutionException;

import org.arakhne.maven.PropertyFileFilter;

/**
 * Generate the property files and replace the macros by the corresponding values
 * on the fly.
 * Supported macros are described in {@link AbstractReplaceMojo}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 *
 * @goal replaceresource
 * @phase process-resources
 * @requireProject true
 * @threadSafe
 */
public class ReplaceResourceMojo extends AbstractReplaceMojo {

	/** Are the directories where the property files are located.
	 * By default, the directory "target/classes" is used.
	 *
	 * @parameter
	 */
	private File[] sources;

	/** Set of the files inside the file system.
	 */
	private final Map<File, Collection<File>> bufferedFiles = new TreeMap<>();

	@Override
    protected synchronized void executeMojo() throws MojoExecutionException {
		final File[] sourceDirs;
		if (this.sources == null || this.sources.length == 0) {
			sourceDirs = new File[] {
				getClassDirectory(),
			};
		} else {
			sourceDirs = this.sources;
		}

		clearInternalBuffers();

		for (final File sourceDir : sourceDirs) {
			Collection<File> textBasedFiles = this.bufferedFiles.get(sourceDir);
			if (textBasedFiles == null) {
				textBasedFiles = new ArrayList<>();
	    		if (sourceDir.isDirectory()) {
	    			// Search for .properties files
	    			findFiles(sourceDir, new PropertyFileFilter(), textBasedFiles);
	    		}
	    		this.bufferedFiles.put(sourceDir, textBasedFiles);
			}
			for (final File file : textBasedFiles) {
				replaceInFileBuffered(
						null,
						file,
						ReplacementType.TEXT,
						sourceDirs,
						true);
			}
    	}
	}

}

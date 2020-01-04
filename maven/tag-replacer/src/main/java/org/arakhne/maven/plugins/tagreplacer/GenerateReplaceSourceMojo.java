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

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Generate the Java source files and replace the macros by the corresponding values
 * on the fly.
 * Supported macros are described in {@link AbstractReplaceMojo}.
 *
 * <p>CAUTION: This Mojo override the value of the source directory.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 *
 * @goal generatereplacesrc
 * @phase pre-integration-test
 * @requireProject true
 * @threadSafe
 */
public class GenerateReplaceSourceMojo extends GenerateSourceMojo {

	@Override
    protected synchronized void executeMojo(File targetDir) throws MojoExecutionException {
		super.executeMojo(targetDir);
		setSourceDirectoryForAllMojo(targetDir);
	}

}

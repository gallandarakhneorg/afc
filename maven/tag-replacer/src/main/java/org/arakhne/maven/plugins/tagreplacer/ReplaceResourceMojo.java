/* 
 * $Id$
 * 
 * Copyright (C) 2011-12 Stephane GALLAND This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.maven.plugins.tagreplacer;

import org.apache.maven.plugin.MojoExecutionException;
import org.arakhne.maven.PropertyFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

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
	private final Map<File,Collection<File>> bufferedFiles = new TreeMap<>();

	/**
     * {@inheritDoc}
     */
	@Override
    protected synchronized void executeMojo() throws MojoExecutionException {
		File[] sourceDirs;
		if (this.sources==null || this.sources.length==0) {
			sourceDirs = new File[] {
					getClassDirectory()
			};
		}
		else {
			sourceDirs = this.sources;
		}
		
		clearInternalBuffers();

		
		for(File sourceDir : sourceDirs) {
			Collection<File> textBasedFiles = this.bufferedFiles.get(sourceDir);
			if (textBasedFiles==null) {
				textBasedFiles = new ArrayList<>();
	    		if ( sourceDir.isDirectory() ) {
	    			// Search for .properties files
	    			findFiles(sourceDir, new PropertyFileFilter(), textBasedFiles);
	    		}
	    		this.bufferedFiles.put(sourceDir, textBasedFiles);
			}
			for(File file : textBasedFiles) {
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

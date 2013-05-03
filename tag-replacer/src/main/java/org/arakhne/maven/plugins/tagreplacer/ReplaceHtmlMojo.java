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
 * Replace the macros by the corresponding values
 * on the fly in HTML files.
 * Supported macros are described in {@link AbstractReplaceMojo}.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.3
 * 
 * @goal replacehtml
 * @phase process-resources
 * @requireProject true
 * @threadSafe
 */
public class ReplaceHtmlMojo extends AbstractReplaceMojo {

	/** Are the directories where the HTML files are located.
	 * By default, the directory "target/site/javadoc" is used.
	 * 
	 * @parameter
	 */
	private File[] htmls;

	/** Set of the files inside the file system.
	 */
	private final Map<File,Collection<File>> bufferedFiles = new TreeMap<File,Collection<File>>();

	/**
     * {@inheritDoc}
     */
	@Override
    protected synchronized void executeMojo() throws MojoExecutionException {
		File[] htmlDirs;
		if (this.htmls==null || this.htmls.length==0) {
			htmlDirs = new File[] {
					new File(getSiteDirectory(), "javadoc"), //$NON-NLS-1$
			};
		}
		else {
			htmlDirs = this.htmls;
		}
		
		clearInternalBuffers();

		for(File htmlDir : htmlDirs) {
			Collection<File> textBasedFiles = this.bufferedFiles.get(htmlDir);
			if (textBasedFiles==null) {
				textBasedFiles = new ArrayList<File>();
	    		if ( htmlDir.isDirectory() ) {
	    			// Search for .html files
	    			findFiles(htmlDir, new PropertyFileFilter(), textBasedFiles);
	    		}
	    		this.bufferedFiles.put(htmlDir, textBasedFiles);
			}
			for(File file : textBasedFiles) {
				replaceInFileBuffered(
						null,
						file,
						ReplacementType.HTML,
						htmlDirs,
						true);
			}
    	}
	}
    
}

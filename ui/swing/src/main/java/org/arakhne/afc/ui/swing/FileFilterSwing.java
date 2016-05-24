/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.ui.swing;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/** Implementation of swing file filter.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class FileFilterSwing extends FileFilter implements org.arakhne.afc.inputoutput.filefilter.FileFilter  {

	private final org.arakhne.afc.inputoutput.filefilter.FileFilter fileFilter;
	
	/**
	 * @param filter
	 */
	public FileFilterSwing(org.arakhne.afc.inputoutput.filefilter.FileFilter filter) {
		this.fileFilter = filter;
	}
	
	/** Replies the file filter.
	 * 
	 * @return the file filter.
	 */
	public final org.arakhne.afc.inputoutput.filefilter.FileFilter getFileFilter() {
		return this.fileFilter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean accept(File dir, String name) {
		return accept(new File(dir,name));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(File f) {
		if (f!=null) {
			if (this.fileFilter!=null)
				return this.fileFilter.accept(f);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		if (this.fileFilter!=null)
			return this.fileFilter.getDescription();
		return null;
	}

	@Override
	public String[] getExtensions() {
		if (this.fileFilter!=null)
			return this.fileFilter.getExtensions();
		return new String[0];
	}

}

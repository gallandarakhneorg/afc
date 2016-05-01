/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2005-10 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
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

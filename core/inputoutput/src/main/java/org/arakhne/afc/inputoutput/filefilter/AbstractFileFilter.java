/* 
 * $Id$
 * 
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.inputoutput.filefilter ;

import java.io.File;

import org.arakhne.afc.vmutil.FileSystem;

/** Abstract implementation of a file filter that may be
 * used in all the standard Java tools.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractFileFilter implements FileFilter {

	private final boolean acceptDirectories;
	private final String description;
	private final String[] extensions;
	
	/**
	 * @param acceptDirectories is <code>true</code> to
	 * permit to this file filter to accept directories;
	 * <code>false</code> if the directories should not
	 * match.
	 * @param description is the description of the file filter.
	 * @param extensions are the supported extensions.
	 */
	public AbstractFileFilter(boolean acceptDirectories, String description, String... extensions) {
		this.acceptDirectories = acceptDirectories;
		this.extensions = extensions;

		StringBuilder b = new StringBuilder();
		b.append(description);
		b.append(" ("); //$NON-NLS-1$
		for(int i=0; i<this.extensions.length; ++i) {
			if (i>0) b.append(", "); //$NON-NLS-1$
			b.append("*"); //$NON-NLS-1$
			if (!this.extensions[i].startsWith(".")) //$NON-NLS-1$
				b.append("."); //$NON-NLS-1$
			b.append(this.extensions[i]);
		}
		b.append(")"); //$NON-NLS-1$
		this.description = b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean accept(File f) {
		if (f.isDirectory()) return  this.acceptDirectories;
		for(String ext : this.extensions) {
			if (FileSystem.hasExtension(f, ext)) {
				return true;
			}
		}
		return false;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final String getDescription() {
		return this.description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final String[] getExtensions() {
		return this.extensions;
	}
	
}

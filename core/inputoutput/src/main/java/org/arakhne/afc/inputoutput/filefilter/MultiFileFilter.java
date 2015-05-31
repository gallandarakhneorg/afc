/* 
 * $Id$
 * 
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
package org.arakhne.afc.inputoutput.filefilter ;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiFileFilter implements FileFilter {

	private final boolean acceptDirectories;
	private final FileFilter[] filters;
	private final String description;
	
	/**
	 * @param description
	 * @param filters
	 */
	public MultiFileFilter(String description, FileFilter... filters) {
		this(true, description, filters);
	}

	/**
	 * @param acceptDirectories is <code>true</code> to
	 * permit to this file filter to accept directories;
	 * <code>false</code> if the directories should not
	 * match.
	 * @param description
	 * @param filters
	 */
	public MultiFileFilter(boolean acceptDirectories, String description, FileFilter... filters) {
		this.acceptDirectories = acceptDirectories;
		this.filters = filters;
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) return  this.acceptDirectories;
		for(FileFilter ff : this.filters) {
			if (ff.accept(f)) return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}

	@Override
	public String[] getExtensions() {
		List<String> extensions = new ArrayList<>();
		for(FileFilter ff : this.filters) {
			extensions.addAll(Arrays.asList(ff.getExtensions()));
		}
		String[] tab = new String[extensions.size()];
		extensions.toArray(tab);
		return tab;
	}
	
}

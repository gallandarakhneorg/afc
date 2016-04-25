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
package org.arakhne.maven;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * File filter that is matching a collection
 * of file filters.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiFileFilter implements FileFilter {

	private final Collection<FileFilter> filters = new ArrayList<>();
	
	/**
	 */
	public MultiFileFilter() {
		//
	}

	/**
	 * @param filters
	 */
	public MultiFileFilter(Collection<? extends FileFilter> filters) {
		if (filters!=null) this.filters.addAll(filters);
	}

	/**
	 * @param filters
	 */
	public MultiFileFilter(FileFilter... filters) {
		this.filters.addAll(Arrays.asList(filters));
	}
	
	/** Add file filter.
	 * 
	 * @param filter
	 */
	public void addFileFilter(FileFilter... filter) {
		this.filters.addAll(Arrays.asList(filter));
	}

	/** Add file filters.
	 * 
	 * @param filters
	 */
	public void addFileFilter(Collection<? extends FileFilter> filters) {
		if (filters!=null) this.filters.addAll(filters);
	}
	
	/** Remove all the filter filters.
	 */
	public void clear() {
		this.filters.clear();
	}
	
	/** Remove the given filter filter.
	 * 
	 * @param filter
	 */
	public void removeFileFilter(FileFilter... filter) {
		this.filters.removeAll(Arrays.asList(filter));
	}

	/** Remove the given filter filters.
	 * 
	 * @param filters
	 */
	public void removeFileFilter(Collection<? super FileFilter> filters) {
		this.filters.removeAll(filters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(File pathname) {
		if (pathname==null) return false;
		if (pathname.isDirectory()) return true;
		for(FileFilter filter : this.filters) {
			if (filter.accept(pathname)) return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Multiple file"; //$NON-NLS-1$
	}

}
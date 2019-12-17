/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

	/** Construct.
	 */
	public MultiFileFilter() {
		//
	}

	/** Construct.
	 *
	 * @param filters filters.
	 */
	public MultiFileFilter(Collection<? extends FileFilter> filters) {
		if (filters != null) {
			this.filters.addAll(filters);
		}
	}

	/** Construct.
	 *
	 * @param filters filters.
	 */
	public MultiFileFilter(FileFilter... filters) {
		this.filters.addAll(Arrays.asList(filters));
	}

	/** Add file filter.
	 *
	 * @param filter filters.
	 */
	public void addFileFilter(FileFilter... filter) {
		this.filters.addAll(Arrays.asList(filter));
	}

	/** Add file filters.
	 *
	 * @param filters filters.
	 */
	public void addFileFilter(Collection<? extends FileFilter> filters) {
		if (filters != null) {
			this.filters.addAll(filters);
		}
	}

	/** Remove all the filter filters.
	 */
	public void clear() {
		this.filters.clear();
	}

	/** Remove the given filter filter.
	 *
	 * @param filter filters.
	 */
	public void removeFileFilter(FileFilter... filter) {
		this.filters.removeAll(Arrays.asList(filter));
	}

	/** Remove the given filter filters.
	 *
	 * @param filters filters.
	 */
	public void removeFileFilter(Collection<? super FileFilter> filters) {
		this.filters.removeAll(filters);
	}

	@Override
	public boolean accept(File pathname) {
		if (pathname == null) {
			return false;
		}
		if (pathname.isDirectory()) {
			return true;
		}
		for (final FileFilter filter : this.filters) {
			if (filter.accept(pathname)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Multiple file"; //$NON-NLS-1$
	}

}

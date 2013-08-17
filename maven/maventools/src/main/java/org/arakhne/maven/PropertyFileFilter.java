/* 
 * $Id$
 * 
 * Copyright (C) 2011 Stephane GALLAND This library is free software; you can redistribute it and/or
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

/**
 * File filter for property file.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PropertyFileFilter implements FileFilter {

	/**
	 */
	public PropertyFileFilter() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(File pathname) {
		return pathname!=null &&
		(pathname.isDirectory() ||
				pathname.getName().endsWith(".properties")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Java Properties (.properties)"; //$NON-NLS-1$
	}

}
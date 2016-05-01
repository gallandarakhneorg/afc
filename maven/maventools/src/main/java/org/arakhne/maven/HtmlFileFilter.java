/* 
 * $Id$
 * 
 * Copyright (C) 2010-11 Stephane GALLAND This library is free software; you can redistribute it and/or
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
 * File filter for HTML file.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HtmlFileFilter implements FileFilter {

	/**
	 */
	public HtmlFileFilter() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(File pathname) {
		if (pathname!=null) {
			if (pathname.isDirectory()) return true;
			String basename = pathname.getName();
			if (basename!=null && basename.length()>0) {
				basename = basename.toLowerCase();
				if (basename.endsWith(".html")) return true; //$NON-NLS-1$
				if (basename.endsWith(".htm")) return true; //$NON-NLS-1$
				if (basename.endsWith(".ht")) return true; //$NON-NLS-1$
				if (basename.endsWith(".phtml")) return true; //$NON-NLS-1$
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Hypertext (.html)"; //$NON-NLS-1$
	}

}

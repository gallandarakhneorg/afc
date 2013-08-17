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
package org.arakhne.maven.plugins.licenseinstaller;

/**
 * Global constants.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Constants {

	/**
	 * Maven tag for artifcat id
	 */
	public static final String PROP_ARTIFACTID = "artifactId"; //$NON-NLS-1$
	
	/**
	 * Maven tag for goup id
	 */
	public static final String PROP_GROUPID = "groupId"; //$NON-NLS-1$
	
	/**
	 * Maven tag for version description
	 */
	public static final String PROP_VERSION = "version"; //$NON-NLS-1$
	
	/**
	 * Name of the directory classes/. 
	 */
	public static final String CLASSES_DIR = "classes"; //$NON-NLS-1$

	/**
	 * Name of the directory META-INF. 
	 */
	public static final String METAINF_DIR = "META-INF"; //$NON-NLS-1$

	/**
	 * Name of the subdirectory of META-INF in which licenses may appear. 
	 */
	public static final String LICENSE_DIR = "license"; //$NON-NLS-1$

	/**
	 * Pattern for the license filenames.
	 * %s should be replaced by the software name.
	 * %l should be replaced by the license name.
	 */
	public static final String LICENSE_FILENAME_PATTERN = "LICENSE.%s.%l.txt"; //$NON-NLS-1$

	/**
	 * Pattern for the notice filenames.
	 * %s should be replaced by the software name.
	 */
	public static final String NOTICE_FILENAME_PATTERN = "NOTICE.%s.txt"; //$NON-NLS-1$

	/**
	 * Pattern for the author filenames.
	 * %s should be replaced by the software name.
	 */
	public static final String AUTHOR_FILENAME_PATTERN = "AUTHORS.%s.txt"; //$NON-NLS-1$

}

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

import java.net.URL;

/**
 * Supported licenses.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated
 */
@Deprecated
public enum License {

	/** GNU General Public License v3. */
	GPLv3(
			"gpl-3.0.txt", //$NON-NLS-1$
			"GNU General Public License version 3.0", //$NON-NLS-1$
			true),
	
	/** GNU General Public License v2. */
	GPLv2(
			"gpl-2.0.txt", //$NON-NLS-1$
			"GNU General Public License version 2.0", //$NON-NLS-1$
			true),

	/** GNU Lesser General Public License v3. */
	LGPLv3(
			"lgpl-3.0.txt", //$NON-NLS-1$
			"GNU Lesser General Public License version 3.0", //$NON-NLS-1$
			true),
	
	/** GNU Lesser General Public License v2.1. */
	LGPLv2_1(
			"lgpl-2.1.txt", //$NON-NLS-1$
			"GNU Lesser General Public License version 2.1", //$NON-NLS-1$
			true),

	/** GNU Affero General Public License v3. */
	AGPLv3(
			"agpl-3.0.txt", //$NON-NLS-1$
			"GNU Affero General Public License version 3.0", //$NON-NLS-1$
			true),

	/** Apache License v2.0. */
	APACHEv2(
			"apache-2.0.txt", //$NON-NLS-1$
			"Apache License version 2.0", //$NON-NLS-1$
			true),

	/** GNU Free Documentation License v1.3. */
	FDLv1_3(
			"fdl-1.3.txt", //$NON-NLS-1$
			"GNU Free Documentation License version 1.3", //$NON-NLS-1$
			true),
		
	/** BSD 3-Clause ("BSD New" or "Revised") */ 
	BSDv3(
			"bsd-3.txt", //$NON-NLS-1$
			"BSD 3-Clause License ('BSD New' or 'BSD Revised')", //$NON-NLS-1$
			true),
			
	/** BSD 2-Clause ("Simplified" or "FreeBSD") */ 
	BSDv2(
			"bsd-2.txt", //$NON-NLS-1$
			"BSD 2-Clause License ('BSD Simplified' or 'FreeBSD')", //$NON-NLS-1$
			true),
			
	/** Mozilla Public License v1.1 */ 
	MPLv1_1(
			"mpl-1.1.txt", //$NON-NLS-1$
			"Mozilla Public License 1.1", //$NON-NLS-1$
			true),

	/** COMMON DEVELOPMENT AND DISTRIBUTION LICENSE v1.0 */ 
	CDDTv1_0(
			"cddt-1.0.txt", //$NON-NLS-1$
			"Common Development and Distribution License 1.0", //$NON-NLS-1$
			true),

	/** Eclipse Plublic License v1.0 */ 
	EPLv1_0(
			"epl-1.0.txt", //$NON-NLS-1$
			"Eclipse Public License 1.0", //$NON-NLS-1$
			true),

	/** MIT License */ 
	MIT(
			"mit.txt", //$NON-NLS-1$
			"MIT License", //$NON-NLS-1$
			true),
	
	/** EULA License */ 
	EULA(
			"eula.txt", //$NON-NLS-1$
			"End-User License Agreement", //$NON-NLS-1$
			false);
			
	private final String filename;
	private final String licenseName;
	private final boolean isOpenSource;
	
	private License(String filename, String licenseName, boolean isOpenSource) {
		Package p = getClass().getPackage();
		if (p==null) {
			this.filename = filename;
		}
		else {
			this.filename = p.getName().replace('.', '/') +
							"/" + //$NON-NLS-1$
							filename;
		}
		this.licenseName = licenseName;
		this.isOpenSource = isOpenSource;
	}
	
	/** Replies the full name of the license.
	 * 
	 * @return the full name of the license.
	 */
	public String getLicenseName() {
		return this.licenseName;
	}
	
	/** Replies if this license is an open-source license.
	 * 
	 * @return <code>true</code> if open-source; <code>false</code> if not.
	 * @since 2.3
	 */
	public boolean isOpenSource() {
		return this.isOpenSource;
	}
	
	
	/** Replies the name of the resource which is containing the full text of the license.
	 * 
	 * @return the resource path.
	 */
	public URL getFullTextResource() {
		ClassLoader loader = License.class.getClassLoader();
    	URL url = loader.getResource(this.filename);
    	if (url==null) {
    		// Try to find in ./resources sub directory
    		url = loader.getResource("resources/"+this.filename); //$NON-NLS-1$
    	}
    	return url;
	}
	
	/** Parse a string representation of a license.
	 * 
	 * @param s
	 * @param defaultValue
	 * @return the license or <code>null</code>.
	 */
	public static License parse(String s, License defaultValue) {
		for(License l : License.values()) {
			if (s.equalsIgnoreCase(l.name())) return l;
			if (s.equalsIgnoreCase(l.name().replaceAll("_", "."))) return l; //$NON-NLS-1$//$NON-NLS-2$
		}
		return defaultValue;
	}

}

/* 
 * $Id$
 * 
 * Copyright (C) 2008-2013 Stephane GALLAND.
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

package org.arakhne.vmutil;

import java.io.InputStream;
import java.net.URL;

/**
 * This interface provides the standard JRE implementation to load resources according to
 * several heuristics:<ul>
 * <li>search the resource in class paths;</li>
 * <li>search the resource in ./resources subdirectory in class paths.</li>
 * </ul>
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
class StandardJREResourceWrapper implements ResourceWrapper {

	/** Prefix (or directory name) where resources may be located.
	 */
	public static final String RESOURCE_PREFIX = "resources/"; //$NON-NLS-1$

	/**
	 */
	public StandardJREResourceWrapper() {
		//
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
    public URL getResource(ClassLoader classLoader, String path) {
    	if (path==null) return null;
    	String resourcePath = path;
    	if (path.startsWith("/")) { //$NON-NLS-1$
    		resourcePath = path.substring(1);
    	}
    	
    	ClassLoader loader = (classLoader==null)
    			? ClassLoaderFinder.findClassLoader() //Resources.class.getClassLoader()
    			: classLoader;
    	assert(loader!=null);
    			
    	URL url = loader.getResource(resourcePath);
    	
    	if (url==null) {
    		// Try to find in ./resources sub directory
    		url = loader.getResource(RESOURCE_PREFIX+resourcePath);
    	}
    	return url;
    }

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("resource")
	@Override
    public InputStream getResourceAsStream(ClassLoader classLoader, String path) {
    	if (path==null) return null;
    	String resourcePath = path;
    	if (path.startsWith("/")) { //$NON-NLS-1$
    		resourcePath = path.substring(1);
    	}
    	ClassLoader loader = classLoader;
    	if (loader==null) {
    		loader = ClassLoaderFinder.findClassLoader();
    	}
    	if (loader==null) {
    		loader = Resources.class.getClassLoader();
    	}
    	
    	assert(loader!=null);
    	InputStream is = loader.getResourceAsStream(resourcePath);
    	if (is==null) {
    		// Try to find in ./resources sub directory
    		is = loader.getResourceAsStream(RESOURCE_PREFIX+resourcePath);
    	}
    	return is;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public String translateResourceName(String resourceName) {
		return resourceName;
	}

}

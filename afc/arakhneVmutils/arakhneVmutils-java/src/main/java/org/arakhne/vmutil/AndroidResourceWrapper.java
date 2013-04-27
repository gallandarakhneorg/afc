/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import org.arakhne.vmutil.Android.AndroidException;

/**
 * This interface provides the Android implementation to load resources.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
class AndroidResourceWrapper implements ResourceWrapper {

	private static String decodeResourceName(String resourceName) {
		if (resourceName.startsWith("/")) { //$NON-NLS-1$
			return resourceName.substring(1);
		}
		return resourceName;
	}
	
	/**
	 */
	public AndroidResourceWrapper() {
		//
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
    public URL getResource(ClassLoader classLoader, String path) {
		String resourceName = decodeResourceName(path);
		ClassLoader androidClassLoader;
		try {
			androidClassLoader = Android.getContextClassLoader();
			assert(androidClassLoader!=null);
			URL url = androidClassLoader.getResource(resourceName);
			if (url!=null) return url;
		}
		catch (AndroidException e) {
			//
		}
		return classLoader.getResource(resourceName);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public InputStream getResourceAsStream(ClassLoader classLoader, String path) {
		String resourceName = decodeResourceName(path);
		ClassLoader androidClassLoader;
		try {
			androidClassLoader = Android.getContextClassLoader();
			assert(androidClassLoader!=null);
			InputStream stream = androidClassLoader.getResourceAsStream(resourceName);
			if (stream!=null) return stream;
		}
		catch (AndroidException e) {
			//
		}
		return classLoader.getResourceAsStream(resourceName);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
    public String translateResourceName(String resourceName) {
		return resourceName.replaceAll("[.]", "/");  //$NON-NLS-1$//$NON-NLS-2$
	}

}

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

package org.arakhne.afc.vmutil;

import java.io.InputStream;
import java.net.URL;

import org.arakhne.afc.vmutil.Android.AndroidException;

/**
 * This interface provides the Android implementation to load resources.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
class AndroidResourceWrapper implements ResourceWrapper {

	/** Construct the wrapper.
	 */
	AndroidResourceWrapper() {
		//
	}

	private static String decodeResourceName(String resourceName) {
		if (resourceName.startsWith("/")) { //$NON-NLS-1$
			if (!resourceName.startsWith("/")) { //$NON-NLS-1$
				return "/" + resourceName; //$NON-NLS-1$
			}
		}
		return resourceName;
	}

	@Override
	@Deprecated(since = "17.0")
    public URL getResource(ClassLoader classLoader, String path) {
		final String resourceName = decodeResourceName(path);
		final ClassLoader androidClassLoader;
		try {
			androidClassLoader = Android.getContextClassLoader();
			assert androidClassLoader != null;
			final URL url = androidClassLoader.getResource(resourceName);
			if (url != null) {
				return url;
			}
		} catch (AndroidException e) {
			//
		}
		if (classLoader != null) {
			return classLoader.getResource(resourceName);
		}
		return null;
    }

	@Override
    public URL getResource(Class<?> clazz, String path) {
		final String resourceName = decodeResourceName(path);
		final ClassLoader androidClassLoader;
		try {
			androidClassLoader = Android.getContextClassLoader();
			assert androidClassLoader != null;
			final URL url = androidClassLoader.getResource(resourceName);
			if (url != null) {
				return url;
			}
		} catch (AndroidException e) {
			//
		}
		if (clazz != null) {
			return clazz.getResource(resourceName);
		}
		return null;
    }

	@Override
	@Deprecated(since = "17.0")
    public InputStream getResourceAsStream(ClassLoader classLoader, String path) {
		final String resourceName = decodeResourceName(path);
		final ClassLoader androidClassLoader;
		try {
			androidClassLoader = Android.getContextClassLoader();
			assert androidClassLoader != null;
			final InputStream stream = androidClassLoader.getResourceAsStream(resourceName);
			if (stream != null) {
				return stream;
			}
		} catch (AndroidException e) {
			//
		}
		if (classLoader != null) {
			return classLoader.getResourceAsStream(resourceName);
		}
		return null;
    }

	@Override
    public InputStream getResourceAsStream(Class<?> clazz, String path) {
		final String resourceName = decodeResourceName(path);
		final ClassLoader androidClassLoader;
		try {
			androidClassLoader = Android.getContextClassLoader();
			assert androidClassLoader != null;
			final InputStream stream = androidClassLoader.getResourceAsStream(resourceName);
			if (stream != null) {
				return stream;
			}
		} catch (AndroidException e) {
			//
		}
		if (clazz != null) {
			return clazz.getResourceAsStream(resourceName);
		}
		return null;
    }

	@Override
    public String translateResourceName(String resourceName) {
		return resourceName.replaceAll("[.]", "/"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}

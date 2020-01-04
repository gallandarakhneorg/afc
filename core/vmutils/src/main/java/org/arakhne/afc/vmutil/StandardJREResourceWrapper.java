/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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
import java.util.regex.Pattern;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface provides the standard JRE implementation to load resources.
 * The following heuristics are applied:<ul>
 * <li>search the resource in class paths;</li>
 * <li>search the resource in ./resources subdirectory in class paths.</li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
class StandardJREResourceWrapper implements ResourceWrapper {

	/** Prefix (or directory name) where resources may be located.
	 */
	public static final String RESOURCE_PREFIX = "/resources"; //$NON-NLS-1$

	/** Construct a wrapper.
	 */
	StandardJREResourceWrapper() {
		//
	}

	private static String ensurePathPrefix(String path) {
		assert path != null;
		if (!path.startsWith("/")) { //$NON-NLS-1$
			return "/" + path; //$NON-NLS-1$
		}
		return path;
	}

	private static String makeRelativePath(Class<?> type, String path) {
		assert type != null;
		assert path != null;
		final StringBuilder buffer = new StringBuilder();
		buffer.append("/"); //$NON-NLS-1$
		buffer.append(type.getPackageName().replaceAll(Pattern.quote("."), "/")); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append(path);
		return buffer.toString();
	}

	@Override
	@Pure
	@Deprecated(since = "17.0")
	public URL getResource(ClassLoader classLoader, String path) {
		if (path == null) {
			return null;
		}
		final String resourcePath = ensurePathPrefix(path);
		final ClassLoader loader = (classLoader == null)
				? ClassLoaderFinder.findClassLoader()
				: classLoader;
		assert loader != null;

		URL url = loader.getResource(resourcePath);

		if (url == null) {
			// Try to find in ./resources sub directory
			url = loader.getResource(RESOURCE_PREFIX + resourcePath);
		}
		return url;
	}

	@Override
	@Pure
	public URL getResource(Class<?> clazz, String path) {
		if (path == null) {
			return null;
		}
		final String resourcePath = ensurePathPrefix(path);

		assert clazz != null;

		URL url = clazz.getResource(resourcePath);

		if (url == null) {
			// Try to find in ./resources sub directory
			url = clazz.getResource(RESOURCE_PREFIX + resourcePath);
			if (url == null) {
				// Try to find in the class's package
				final String resourcePath2 = makeRelativePath(clazz, resourcePath);
				url = clazz.getResource(resourcePath2);
				if (url == null) {
					// Try to find in ./resources sub directory
					url = clazz.getResource(RESOURCE_PREFIX + resourcePath2);
				}
			}
		}
		return url;
	}

	@SuppressWarnings("resource")
	@Override
	@Pure
	@Deprecated(since = "17.0")
	public InputStream getResourceAsStream(ClassLoader classLoader, String path) {
		if (path == null) {
			return null;
		}
		final String resourcePath = ensurePathPrefix(path);
		ClassLoader loader = classLoader;
		if (loader == null) {
			loader = ClassLoaderFinder.findClassLoader();
		}
		if (loader == null) {
			loader = Resources.class.getClassLoader();
		}

		assert loader != null;
		InputStream is = loader.getResourceAsStream(resourcePath);
		if (is == null) {
			// Try to find in ./resources sub directory
			is = loader.getResourceAsStream(RESOURCE_PREFIX + resourcePath);
		}
		return is;
	}

	@SuppressWarnings("resource")
	@Override
	@Pure
	public InputStream getResourceAsStream(Class<?> clazz, String path) {
		if (path == null) {
			return null;
		}
		final String resourcePath = ensurePathPrefix(path);
		assert clazz != null;
		InputStream is = clazz.getResourceAsStream(resourcePath);
		if (is == null) {
			// Try to find in ./resources sub directory
			is = clazz.getResourceAsStream(RESOURCE_PREFIX + resourcePath);
			if (is == null) {
				// Try to find in the class's package
				final String resourcePath2 = makeRelativePath(clazz, resourcePath);
				is = clazz.getResourceAsStream(resourcePath2);
				if (is == null) {
					// Try to find in ./resources sub directory
					is = clazz.getResourceAsStream(RESOURCE_PREFIX + resourcePath2);
				}
			}
		}
		return is;
	}

	@Override
	@Pure
	public String translateResourceName(String resourceName) {
		return resourceName;
	}

}

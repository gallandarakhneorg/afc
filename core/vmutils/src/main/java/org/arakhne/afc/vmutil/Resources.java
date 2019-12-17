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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This utility class provides to load resources.
 * The following heuristics are applied:<ul>
 * <li>search the resource in class paths;</li>
 * <li>search the resource in ./resources subdirectory in class paths.</li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.2
 */
public final class Resources {

	/** Character used to separate paths on an resource name.
	 */
	public static final String NAME_SEPARATOR = "/"; //$NON-NLS-1$

	private static ResourceWrapper currentResourceInstance;

	static {
		URLHandlerUtil.installArakhneHandlers();

		ResourceWrapper wrapper = null;

		switch (OperatingSystem.getCurrentOS()) {
		case ANDROID:
			wrapper = new AndroidResourceWrapper();
			break;
			//$CASES-OMITTED$
		default:
		}

		if (wrapper == null) {
			currentResourceInstance = new StandardJREResourceWrapper();
		} else {
			currentResourceInstance = wrapper;
		}
	}

	private Resources() {
		//
	}

	/** Replies the URL of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames.
	 *
	 * <p>The class loader replied by {@link ClassLoaderFinder} is used.
	 * If it is <code>null</code>, the class loader of
	 * the Resources class is used.
	 *
	 * @param path is the absolute path of the resource.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 */
	@Pure
	public static URL getResource(String path) {
		return getResource((ClassLoader) null, path);
	}

	/**
	 * Replies the URL of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames.
	 *
	 * <p>The name of {@code packagename} is translated into a resource
	 * path (by replacing the dots by slashes) and the given path
	 * is append to. For example, the two following codes are equivalent:<pre><code>
	 * Resources.getResources(Package.getPackage("org.arakhne.afc"), "/a/b/c/d.png");
	 * Resources.getResources("org/arakhne/afc/a/b/c/d.png");
	 * </code></pre>
	 *
	 * <p>If the {@code classLoader} parameter is <code>null</code>,
	 * the class loader replied by {@link ClassLoaderFinder} is used.
	 * If this last is <code>null</code>, the class loader of
	 * the Resources class is used.
	 *
	 * @param classLoader is the research scope. If <code>null</code>,
	 *     the class loader replied by {@link ClassLoaderFinder} is used.
	 * @param packagename is the package in which the resource should be located.
	 * @param path is the relative path of the resource in the package.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 * @since 6.2
	 */
	@Pure
	public static URL getResource(ClassLoader classLoader, Package packagename, String path) {
		if (packagename == null || path == null) {
			return null;
		}
		final StringBuilder b = new StringBuilder();
		b.append(packagename.getName().replaceAll(
				Pattern.quote("."), //$NON-NLS-1$
				Matcher.quoteReplacement(NAME_SEPARATOR)));
		if (!path.startsWith(NAME_SEPARATOR)) {
			b.append(NAME_SEPARATOR);
		}
		b.append(path);
		ClassLoader cl = classLoader;
		if (cl == null) {
			cl = packagename.getClass().getClassLoader();
		}
		return getResource(cl, b.toString());
	}

	/**
	 * Replies the URL of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames.
	 *
	 * <p>The name of {@code classname} is translated into a resource
	 * path (by remove the name of the class and replacing the dots by slashes) and the given path
	 * is append to. For example, the two following codes are equivalent:<pre><code>
	 * Resources.getResources(Resources.class, "/a/b/c/d.png");
	 * Resources.getResources("org/arakhne/vmutil/a/b/c/d.png");
	 * </code></pre>
	 *
	 * <p>The class loader of the given class is used. If it is <code>null</code>,
	 * the class loader replied by {@link ClassLoaderFinder} is used.
	 * If it is also <code>null</code>, the class loader of this Resources
	 * class is used.
	 *
	 * @param classname is located in the package in which the resource should be also located.
	 * @param path is the absolute path of the resource.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 */
	@Pure
	public static URL getResource(Class<?> classname, String path) {
		if (classname == null) {
			return null;
		}
		URL u = getResource(classname.getClassLoader(), classname.getPackage(), path);
		if (u == null) {
			u = getResource(classname.getClassLoader(), path);
		}
		return u;
	}

	/**
	 * Replies the URL of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames.
	 *
	 * <p>If the {@code classLoader} parameter is <code>null</code>,
	 * the class loader replied by {@link ClassLoaderFinder} is used.
	 * If this last is <code>null</code>, the class loader of
	 * the Resources class is used.
	 *
	 * @param classLoader is the research scope. If <code>null</code>,
	 *     the class loader replied by {@link ClassLoaderFinder} is used.
	 * @param path is the absolute path of the resource.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 */
	@Pure
	public static URL getResource(ClassLoader classLoader, String path) {
		return currentResourceInstance.getResource(classLoader, path);
	}

	/**
	 * Replies the input stream of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames, and may not start the
	 * path with a slash.
	 *
	 * <p>The class loader replied by {@link ClassLoaderFinder} is used.
	 * If it is <code>null</code>, the class loader of
	 * the Resources class is used.
	 *
	 * @param path is the absolute path of the resource.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 */
	@Pure
	public static InputStream getResourceAsStream(String path) {
		return getResourceAsStream((ClassLoader) null, path);
	}

	/**
	 * Replies the input stream of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames.
	 *
	 * <p>The name of {@code packagename} is translated into a resource
	 * path (by replacing the dots by slashes) and the given path
	 * is append to. For example, the two following codes are equivalent:<pre><code>
	 * Resources.getResources(Package.getPackage("org.arakhne.afc"), "/a/b/c/d.png");
	 * Resources.getResources("org/arakhne/afc/a/b/c/d.png");
	 * </code></pre>
	 *
	 * <p>If the {@code classLoader} parameter is <code>null</code>,
	 * the class loader replied by {@link ClassLoaderFinder} is used.
	 * If this last is <code>null</code>, the class loader of
	 * the Resources class is used.
	 *
	 * @param classLoader is the research scope. If <code>null</code>,
	 *     the class loader replied by {@link ClassLoaderFinder} is used.
	 * @param packagename is the package in which the resource should be located.
	 * @param path is the relative path of the resource in the package.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 * @since 6.2
	 */
	@Pure
	public static InputStream getResourceAsStream(ClassLoader classLoader, Package packagename, String path) {
		if (packagename == null || path == null) {
			return null;
		}
		final StringBuilder b = new StringBuilder();
		b.append(packagename.getName().replaceAll(
				Pattern.quote("."), //$NON-NLS-1$
				Matcher.quoteReplacement(NAME_SEPARATOR)));
		if (!path.startsWith(NAME_SEPARATOR)) {
			b.append(NAME_SEPARATOR);
		}
		b.append(path);
		ClassLoader cl = classLoader;
		if (cl == null) {
			cl = packagename.getClass().getClassLoader();
		}
		return getResourceAsStream(cl, b.toString());
	}

	/**
	 * Replies the input stream of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames.
	 *
	 * <p>The name of {@code classname} is translated into a resource
	 * path (by remove the name of the class and replacing the dots by slashes) and the given path
	 * is append to. For example, the two following codes are equivalent:<pre><code>
	 * Resources.getResources(Resources.class, "/a/b/c/d.png");
	 * Resources.getResources("org/arakhne/vmutil/a/b/c/d.png");
	 * </code></pre>
	 *
	 * <p>The class loader of the given class is used. If it is <code>null</code>,
	 * the class loader replied by {@link ClassLoaderFinder} is used.
	 * If it is also <code>null</code>, the class loader of this Resources
	 * class is used.
	 *
	 * @param classname is located in the package in which the resource should be also located.
	 * @param path is the absolute path of the resource.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 */
	@Pure
	@SuppressWarnings("resource")
	public static InputStream getResourceAsStream(Class<?> classname, String path) {
		if (classname == null) {
			return null;
		}
		InputStream is = getResourceAsStream(classname.getClassLoader(), classname.getPackage(), path);
		if (is == null) {
			is = getResourceAsStream(classname.getClassLoader(), path);
		}
		return is;
	}

	/**
	 * Replies the input stream of a resource.
	 *
	 * <p>You may use Unix-like syntax to write the resource path, ie.
	 * you may use slashes to separate filenames, and may not start the
	 * path with a slash.
	 *
	 * <p>If the {@code classLoader} parameter is <code>null</code>,
	 * the class loader replied by {@link ClassLoaderFinder} is used.
	 * If this last is <code>null</code>, the class loader of
	 * the Resources class is used.
	 *
	 * @param classLoader is the research scope. If <code>null</code>,
	 *     the class loader replied by {@link ClassLoaderFinder} is used.
	 * @param path is the absolute path of the resource.
	 * @return the url of the resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 */
	@Pure
	public static InputStream getResourceAsStream(ClassLoader classLoader, String path) {
		return currentResourceInstance.getResourceAsStream(classLoader, path);
	}

	/**
	 * Replies the URL of a property resource that is associated to the given class.
	 *
	 * @param classname is the class for which the property resource should be replied.
	 * @param locale is the expected localization of the resource file; or <code>null</code>
	 *     for the default.
	 * @return the url of the property resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 * @since 7.0
	 */
	@Pure
	@Inline(value = "Resources.getPropertyFile(($1).getClassLoader(), ($1), ($2))", imported = {Resources.class})
	public static URL getPropertyFile(Class<?> classname, Locale locale) {
		return getPropertyFile(classname.getClassLoader(), classname, locale);
	}

	/**
	 * Replies the URL of a property resource that is associated to the given class.
	 *
	 * @param classLoader is the research scope. If <code>null</code>,
	 *     the class loader replied by {@link ClassLoaderFinder} is used.
	 * @param classname is the class for which the property resource should be replied.
	 * @param locale is the expected localization of the resource file; or <code>null</code>
	 *     for the default.
	 * @return the url of the property resource or <code>null</code> if the resource was
	 *     not found in class paths.
	 */
	@Pure
	public static URL getPropertyFile(ClassLoader classLoader, Class<?> classname, Locale locale) {
		final StringBuilder name = new StringBuilder();

		// Localized file
		if (locale != null) {
			final String country = locale.getCountry();
			if (country != null && !country.isEmpty()) {
				name.append(classname.getSimpleName());
				name.append("_"); //$NON-NLS-1$
				name.append(country);
				name.append(".properties"); //$NON-NLS-1$
				final URL url = getResource(classLoader, classname.getPackage(), name.toString());
				if (url != null) {
					return url;
				}
			}
		}

		// Default property file
		name.setLength(0);
		name.append(classname.getSimpleName());
		name.append(".properties"); //$NON-NLS-1$
		return getResource(classLoader, classname.getPackage(), name.toString());
	}

	/** Translate the given resource name according to the current JVM standard.
	 *
	 * <p>The <code>resourceName</code> argument should be a fully
	 * qualified class name. However, for compatibility with earlier
	 * versions, Sun's Java SE Runtime Environments do not verify this,
	 * and so it is possible to access <code>PropertyResourceBundle</code>s
	 * by specifying a path name (using "/") instead of a fully
	 * qualified class name (using ".").
	 * In several VM, such as Dalvik, the translation from "." to "/" is not
	 * automatically done by the VM to retreive the file.
	 *
	 * @param resourceName the name to translate.
	 * @return the translated resource name.
	 * @since 7.0
	 */
	@Pure
	public static String translateResourceName(String resourceName) {
		return currentResourceInstance.translateResourceName(resourceName);
	}

}

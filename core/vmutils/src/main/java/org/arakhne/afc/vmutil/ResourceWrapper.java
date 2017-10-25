/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface provides implementations to load resources according to
 * several heuristics:<ul>
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
public interface ResourceWrapper {

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
	URL getResource(ClassLoader classLoader, String path);

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
	InputStream getResourceAsStream(ClassLoader classLoader, String path);

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
	 */
	@Pure
	String translateResourceName(String resourceName);

}

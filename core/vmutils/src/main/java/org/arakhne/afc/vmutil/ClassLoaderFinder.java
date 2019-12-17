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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This utility class permits to find the better class loader
 * for your application.
 *
 * <p>It tries to find the preferred class loader registered with
 * {@link #setPreferredClassLoader(ClassLoader)}.
 * If none was found, the default class loader will be replied.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class ClassLoaderFinder {

	private static volatile ClassLoader dynamicLoader;

	private ClassLoaderFinder() {
		//
	}

	/** Replies the better class loader.
	 *
	 * <p>It tries to find the preferred class loader.
	 * If none was found, the default class loader will be replied.
	 *
	 * @return the class loader, never <code>null</code>
	 */
	@Pure
	public static ClassLoader findClassLoader() {
		if (dynamicLoader == null) {
			return ClassLoaderFinder.class.getClassLoader();
		}
		return dynamicLoader;
	}

	/**
	 * Set the preferred class loader.
	 *
	 * @param classLoader is the preferred class loader
	 */
	public static void setPreferredClassLoader(ClassLoader classLoader) {
		if (classLoader != dynamicLoader) {
			dynamicLoader = classLoader;
			final Thread[] threads = new Thread[Thread.activeCount()];
			Thread.enumerate(threads);
			for (final Thread t : threads) {
				if (t != null) {
					t.setContextClassLoader(classLoader);
				}
			}
		}
	}

	/** Pop the preferred class loader.
	 */
	public static void popPreferredClassLoader() {
		final ClassLoader sysLoader = ClassLoaderFinder.class.getClassLoader();

		if ((dynamicLoader == null) || (dynamicLoader == sysLoader)) {
			dynamicLoader = null;
			final Thread[] threads = new Thread[Thread.activeCount()];
			Thread.enumerate(threads);
			for (final Thread t : threads) {
				if (t != null) {
					t.setContextClassLoader(sysLoader);
				}
			}
			return;
		}

		final ClassLoader parent = dynamicLoader.getParent();

		dynamicLoader = (parent == sysLoader) ? null : parent;

		final Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		for (final Thread t : threads) {
			if (t != null) {
				t.setContextClassLoader(parent);
			}
		}
	}

}

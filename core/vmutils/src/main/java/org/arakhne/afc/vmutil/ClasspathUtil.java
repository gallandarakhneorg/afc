/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Current classpath and associated utility functions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.0
 */
public final class ClasspathUtil {

	private ClasspathUtil() {
		//
	}

	/** Replies the classpath at start of the virtual machine.
	 *
	 * @return the startup classpath, never <code>null</code>.
	 * @since 6.0
	 */
	public static Iterator<URL> getStartClasspath() {
		return new PathIterator(System.getProperty("java.class.path")); //$NON-NLS-1$
	}

	/** Replies the current classpath.
	 *
	 * @return the current classpath, never <code>null</code>.
	 * @since 6.0
	 */
	@SuppressWarnings("resource")
	public static Iterator<URL> getClasspath() {
		Iterator<URL> iterator = getStartClasspath();

		final ClassLoader loader = ClassLoaderFinder.findClassLoader();
		try {
			final DynamicURLClassLoader dLoader = (DynamicURLClassLoader) loader;
			iterator = new IteratorIterator(
					new FilteringIterator(Arrays.asList(dLoader.getURLs()).iterator()),
					iterator);
		} catch (ClassCastException exception1) {
			try {
				final URLClassLoader dLoader = (URLClassLoader) loader;
				iterator = new IteratorIterator(
						new FilteringIterator(Arrays.asList(dLoader.getURLs()).iterator()),
						iterator);
			} catch (ClassCastException exception2) {
				//
			}
		}

		return iterator;
	}

	/** Merging Iterator on URLs in a classpath.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 6.0
	 */
	private static class IteratorIterator implements Iterator<URL> {

		private final Iterator<URL> i1;

		private final Iterator<URL> i2;

		/** Construct the iterator.
		 *
		 * @param i1 the first iterator.
		 * @param i2 the second iterator.
		 */
		IteratorIterator(Iterator<URL> i1, Iterator<URL> i2) {
			assert i1 != null && i2 != null;
			this.i1 = i1;
			this.i2 = i2;
		}

		@Override
		public boolean hasNext() {
			return this.i1.hasNext() || this.i2.hasNext();
		}

		@Override
		public URL next() {
			if (this.i1.hasNext()) {
				return this.i1.next();
			}
			return this.i2.next();
		}

	}

	/** Filtering Iterator on URLs in a classpath.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 6.0
	 */
	private static class FilteringIterator implements Iterator<URL> {

		private final Iterator<URL> iterator;

		private URL next;

		/** Construct the iterator.
		 *
		 * @param iterator the iterator to filter.
		 */
		FilteringIterator(Iterator<URL> iterator) {
			assert iterator != null;
			this.iterator = iterator;
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			URL u;
			while (this.next == null && this.iterator.hasNext()) {
				u = this.iterator.next();
				if (u != null) {
					this.next = u;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public URL next() {
			final URL n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

	/** Iterator on paths in a classpath.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 6.0
	 */
	private static class PathIterator implements Iterator<URL> {

		private String path;

		private URL next;

		private int nextIndex;

		/** Construct the iterator.
		 *
		 * @param path the classpath.
		 */
		PathIterator(String path) {
			this.path = path;
			this.nextIndex = -1;
			searchNext();
		}

		private void searchNext() {
			String path;
			int index;
			URL url;

			this.next = null;

			while (this.next == null && this.path != null && this.nextIndex < this.path.length()) {
				index = this.path.indexOf(File.pathSeparatorChar, this.nextIndex + 1);

				if (index > this.nextIndex + 1) {
					path = this.path.substring(this.nextIndex + 1, index);
				} else {
					path = this.path.substring(this.nextIndex + 1);
					// no more element
					this.path = null;
				}

				this.nextIndex = index;

				if (path != null && !"".equals(path)) { //$NON-NLS-1$
					try {
						url = FileSystem.convertStringToURL(path, false, true, false);
						if (url != null) {
							this.next = url;
						}
					} catch (AssertionError e) {
						throw e;
					} catch (Throwable e) {
						//
					}
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public URL next() {
			final URL n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

}

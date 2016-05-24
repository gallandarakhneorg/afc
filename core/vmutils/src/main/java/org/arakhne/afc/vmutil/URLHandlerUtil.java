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

import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/** Utilities around URLHandler.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.0
 */
public final class URLHandlerUtil {

	private static final String HANDLER_PACKAGES = "java.protocol.handler.pkgs"; //$NON-NLS-1$

	private URLHandlerUtil() {
		//
	}

	/** Replies an iterator on the handlers that are supporting the given protocol.
	 *
	 * @param protocol the protocol.
	 * @return the iterator.
	 * @since 7.2
	 */
	public static Iterator<Class<? extends URLStreamHandler>> getHandlersFor(String protocol) {
		return new HandlerIterator(protocol);
	}

	private static void install(String... packageNames) {
		final List<String> array = new LinkedList<>();

		final String str = System.getProperty(HANDLER_PACKAGES);
		if (str != null && !"".equals(str)) { //$NON-NLS-1$
			array.addAll(Arrays.asList(str.split("\\|"))); //$NON-NLS-1$
		}

		for (final String packageName : packageNames) {
			if (!array.contains(packageName)) {
				array.add(0, packageName);
			}
		}

		final StringBuilder buffer = new StringBuilder();
		for (final String s : array) {
			if (buffer.length() > 0) {
				buffer.append('|');
			}
			buffer.append(s);
		}

		final String nstr = buffer.toString();
		if (!nstr.equals(str)) {
			System.setProperty(HANDLER_PACKAGES, nstr);
		}
	}

	private static void uninstall(String... packageNames) {
		final List<String> array = new LinkedList<>();

		final String str = System.getProperty(HANDLER_PACKAGES);
		if (str != null && !"".equals(str)) { //$NON-NLS-1$
			array.addAll(Arrays.asList(str.split("\\|"))); //$NON-NLS-1$
		}

		array.removeAll(Arrays.asList(packageNames));

		final StringBuilder buffer = new StringBuilder();
		for (final String s : array) {
			if (buffer.length() > 0) {
				buffer.append('|');
			}
			buffer.append(s);
		}

		final String nstr = buffer.toString();
		if (!nstr.equals(str)) {
			System.setProperty(HANDLER_PACKAGES, nstr);
		}
	}

	/** Install the URL handlers provided by Arakhn&ecirc;
	 * libraries.
	 *
	 * <p>Handlers are provided for:<ul>
	 * <li><code>file</code>: handler for files which is supported input and output.</li>
	 * <li><code>resource</code>: handler for resource URL.</li>
	 * </ul>
	 *
	 * @see #uninstallArakhneHandlers()
	 */
	public static void installArakhneHandlers() {
		install(URLHandlerUtil.class.getPackage().getName());
	}

	/** Uninstall the URL handlers provided by Arakhn&ecirc;
	 * libraries.
	 *
	 * <p>Handlers are provided for:<ul>
	 * <li><code>file</code>: handler for files which is supported input and output.</li>
	 * <li><code>resource</code>: handler for resource URL.</li>
	 * </ul>
	 *
	 * @see #installArakhneHandlers()
	 */
	public static void uninstallArakhneHandlers() {
		uninstall(URLHandlerUtil.class.getPackage().getName());
	}

	/** Iterator on protocol handlers.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 7.2
	 */
	private static class HandlerIterator implements Iterator<Class<? extends URLStreamHandler>> {

		private final String protocol;

		private final String[] packages;

		private int position;

		private Class<? extends URLStreamHandler> next;

		/** Construct the iterator.
		 *
		 * @param protocol the protocol.
		 */
		HandlerIterator(String protocol) {
			this.protocol = protocol;
			final String str = System.getProperty(HANDLER_PACKAGES);
			if (str == null) {
				this.packages = new String[0];
			} else {
				this.packages = str.split("\\|"); //$NON-NLS-1$
			}

			searchNext();
		}

		@SuppressWarnings("unchecked")
		private void searchNext() {
			this.next = null;
			final ClassLoader clsLoader = getClass().getClassLoader();
			while (this.next == null && this.position < this.packages.length) {
				final String typename = this.packages[this.position++];

				try {
					final Class<?> type = clsLoader.loadClass(typename + "." //$NON-NLS-1$
							+ this.protocol + ".Handler"); //$NON-NLS-1$
					if (type != null && URLStreamHandler.class.isAssignableFrom(type)) {
						this.next = (Class<? extends URLStreamHandler>) type;
					}
				} catch (Throwable exception) {
					//
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public Class<? extends URLStreamHandler> next() {
			final Class<? extends URLStreamHandler> n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

}


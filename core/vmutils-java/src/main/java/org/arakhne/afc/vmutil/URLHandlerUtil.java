/* 
 * $Id$
 * 
 * Copyright (C) 2004-2009 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
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

package org.arakhne.afc.vmutil;

import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/** Utilities around URLHandler.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.0
 */
public class URLHandlerUtil {

	private static final String HANDLER_PACKAGES = "java.protocol.handler.pkgs"; //$NON-NLS-1$
	
	/** Replies an iterator on the handlers that are supporting the given protocol.
	 * 
	 * @param protocol
	 * @return the iterator.
	 * @since 7.2
	 */
	public static Iterator<Class<? extends URLStreamHandler>> getHandlersFor(String protocol) {
		return new HandlerIterator(protocol);
	}
	
	private static void install(String... packageNames) {
		List<String> array = new LinkedList<String>();

		String str = System.getProperty(HANDLER_PACKAGES);
		if (str!=null && !"".equals(str)) { //$NON-NLS-1$
			array.addAll(Arrays.asList(str.split("\\|"))); //$NON-NLS-1$
		}
		
		for(String packageName : packageNames) {
			if (!array.contains(packageName)) {
				array.add(0, packageName);
			}
		}

		StringBuilder buffer = new StringBuilder();
		for(String s : array) {
			if (buffer.length()>0)
				buffer.append('|');
			buffer.append(s);
		}

		String nstr = buffer.toString();
		if (!nstr.equals(str)) {
			System.setProperty(HANDLER_PACKAGES, nstr);
		}
	}

	private static void uninstall(String... packageNames) {
		List<String> array = new LinkedList<String>();

		String str = System.getProperty(HANDLER_PACKAGES);
		if (str!=null && !"".equals(str)) { //$NON-NLS-1$
			array.addAll(Arrays.asList(str.split("\\|"))); //$NON-NLS-1$
		}
			
		array.removeAll(Arrays.asList(packageNames));

		StringBuilder buffer = new StringBuilder();
		for(String s : array) {
			if (buffer.length()>0)
				buffer.append('|');
			buffer.append(s);
		}

		String nstr = buffer.toString();
		if (!nstr.equals(str)) {
			System.setProperty(HANDLER_PACKAGES, nstr);
		}
	}

	/** Install the URL handlers provided by Arakhn&ecirc;
	 * libraries.
	 * <p>
	 * Handlers are provided for:<ul>
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
	 * <p>
	 * Handlers are provided for:<ul>
	 * <li><code>file</code>: handler for files which is supported input and output.</li>
	 * <li><code>resource</code>: handler for resource URL.</li>
	 * </ul>
	 * 
	 * @see #installArakhneHandlers()
	 */
	public static void uninstallArakhneHandlers() {
		uninstall(URLHandlerUtil.class.getPackage().getName());
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 7.2
	 */
	private static class HandlerIterator implements Iterator<Class<? extends URLStreamHandler>> {

		private final String protocol;
		private final String[] packages;
		private int position = 0;
		
		private Class<? extends URLStreamHandler> next;
		
		/**
		 * @param protocol
		 */
		public HandlerIterator(String protocol) {
			this.protocol = protocol;
			String str = System.getProperty(HANDLER_PACKAGES);
			if (str==null)
				this.packages = new String[0];
			else
				this.packages = str.split("\\|"); //$NON-NLS-1$
			
			searchNext();
		}
		
		@SuppressWarnings("unchecked")
		private void searchNext() {
			this.next = null;
			ClassLoader clsLoader = getClass().getClassLoader();
			while (this.next==null && this.position<this.packages.length) {
				String typename = this.packages[this.position++];

				try {
					Class<?> type = clsLoader.loadClass(typename+"."+this.protocol+".Handler"); //$NON-NLS-1$ //$NON-NLS-2$
					if (type!=null && URLStreamHandler.class.isAssignableFrom(type)) {
						this.next = (Class<? extends URLStreamHandler>)type;
					}
				}
				catch(Throwable _) {
					//
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public Class<? extends URLStreamHandler> next() {
			Class<? extends URLStreamHandler> n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}


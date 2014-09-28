/* 
 * $Id$
 * 
 * Copyright (C) 2005-2010 Stephane GALLAND.
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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Current classpath and associated utility functions.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.0
 */
public class ClasspathUtil {

	/** Replies the classpath at start of the virtual machine.
	 * 
	 * @return the startup classpath, never <code>null</code>.
	 * @deprecated see {@link #getStartClasspath()}
	 */
	@Deprecated
	public static URL[] getStartupClasspath() {
		Iterator<URL> iterator = getStartClasspath();
		List<URL> list = new ArrayList<URL>();
		while (iterator.hasNext())
			list.add(iterator.next());
		URL[] tab = new URL[list.size()];
		list.toArray(tab);
		list.clear();
		return tab;
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
	 * @deprecated see {@link #getClasspath()}
	 */
	@Deprecated
	public static URL[] getCurrentClasspath() {
		Iterator<URL> iterator = getClasspath();
		List<URL> list = new ArrayList<URL>();
		while (iterator.hasNext())
			list.add(iterator.next());
		URL[] tab = new URL[list.size()];
		list.toArray(tab);
		list.clear();
		return tab;
	}
	
	/** Replies the current classpath.
	 * 
	 * @return the current classpath, never <code>null</code>.
	 * @since 6.0
	 */
	public static Iterator<URL> getClasspath() {
		Iterator<URL> iterator = getStartClasspath();
		
		ClassLoader loader = ClassLoaderFinder.findClassLoader();
		if (loader instanceof DynamicURLClassLoader) {
			DynamicURLClassLoader dLoader = (DynamicURLClassLoader)loader;
			iterator = new IteratorIterator(
					new FilteringIterator(Arrays.asList(dLoader.getURLs()).iterator()),
					iterator);
		}
		else if (loader instanceof URLClassLoader) {
			URLClassLoader dLoader = (URLClassLoader)loader;
			iterator = new IteratorIterator(
					new FilteringIterator(Arrays.asList(dLoader.getURLs()).iterator()),
					iterator);
		}
		
		return iterator;
	}
	
	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 6.0
	 */
	private static class IteratorIterator implements Iterator<URL> {
		
		private final Iterator<URL> i1;
		private final Iterator<URL> i2;
		
		/**
		 * @param i1
		 * @param i2
		 */
		public IteratorIterator(Iterator<URL> i1, Iterator<URL> i2) {
			assert(i1!=null && i2!=null);
			this.i1 = i1;
			this.i2 = i2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return (this.i1.hasNext() || this.i2.hasNext());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public URL next() {
			if (this.i1.hasNext())
				return this.i1.next();
			return this.i2.next();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 6.0
	 */
	private static class FilteringIterator implements Iterator<URL> {
		
		private final Iterator<URL> iterator;
		private URL next;
		
		/**
		 * @param iterator
		 */
		public FilteringIterator(Iterator<URL> iterator) {
			assert(iterator!=null);
			this.iterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			this.next = null;
			URL u;
			while (this.next==null && this.iterator.hasNext()) {
				u = this.iterator.next();
				if (u!=null) {
					this.next = u;
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public URL next() {
			URL n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 6.0
	 */
	private static class PathIterator implements Iterator<URL> {
		
		private String path;
		private URL next;
		private int nextIndex;
		
		/**
		 * @param path
		 */
		public PathIterator(String path) {
			this.path = path;
			this.nextIndex = -1;
			searchNext();
		}
		
		private void searchNext() {
			String p;
			int index;
			URL url;
			
			this.next = null;
			
			while (this.next==null && this.path!=null && this.nextIndex<this.path.length()) {
				index = this.path.indexOf(File.pathSeparatorChar, this.nextIndex + 1);
				
				if (index>this.nextIndex+1) {
					p = this.path.substring(this.nextIndex+1, index);
				}
				else {
					p = this.path.substring(this.nextIndex+1);
					this.path = null; // no more element
				}

				this.nextIndex = index;

				if (p!=null && !"".equals(p)) { //$NON-NLS-1$
					try {
						url = FileSystem.convertStringToURL(p, false, true, false);
						if (url!=null) {
							this.next = url;
						}
					}
					catch(AssertionError e) {
						throw e;
					}
					catch(Throwable e) {
						//
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public URL next() {
			URL n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
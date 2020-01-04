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

import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.eclipse.xtext.xbase.lib.Pure;

/** This class loader permits to load classes from
 * a set of classpaths.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DynamicURLClassLoader extends URLClassLoader {

	/**
	 * Constructs a new ClassPathClassLoader for the given URLs. The URLs will be
	 * searched in the order specified for classes and resources after first
	 * searching in the specified parent class loader. Any URL that ends with
	 * a '/' is assumed to refer to a directory. Otherwise, the URL is assumed
	 * to refer to a JAR file which will be downloaded and opened as needed.
	 *
	 * <p>If there is a security manager, this method first
	 * calls the security manager's <code>checkCreateClassLoader</code> method
	 * to ensure creation of a class loader is allowed.
	 *
	 * @param parent the parent class loader for delegation
	 * @param urls the URLs from which to load classes and resources
	 * @throws  SecurityException  if a security manager exists and its
	 *     <code>checkCreateClassLoader</code> method doesn't allow
	 *     creation of a class loader.
	 * @see SecurityManager#checkCreateClassLoader
	 */
	protected DynamicURLClassLoader(ClassLoader parent, URL... urls) {
		super(urls, parent);
	}

	/**
	 * Appends the specified URL to the list of URLs to search for
	 * classes and resources.
	 *
	 * @param url the URL to be added to the search path of URLs
	 */
	@Override
	public void addURL(final URL url) {
		super.addURL(url);
	}

	/**
	 * Appends the specified URL to the list of URLs to search for
	 * classes and resources.
	 *
	 * @param urls the URLs to be added to the search path of URLs
	 */
	public void addURLs(URL... urls) {
		for (final URL url : urls) {
			addURL(url);
		}
	}

	/**
	 * Creates a new instance of DynamicURLClassLoader for the specified
	 * URLs and parent class loader. If a security manager is
	 * installed, the <code>loadClass</code> method of the URLClassLoader
	 * returned by this method will invoke the
	 * <code>SecurityManager.checkPackageAccess</code> method before
	 * loading the class.
	 *
	 * @param parent the parent class loader for delegation
	 * @param urls the URLs to search for classes and resources
	 * @return the resulting class loader
	 */
	@Pure
	public static DynamicURLClassLoader newInstance(final ClassLoader parent, final URL... urls) {
		// Need a privileged block to create the class loader
		return AccessController.doPrivileged(new PrivilegedAction<>() {
				@Override
				public DynamicURLClassLoader run() {
					// Now set the context on the loader using the one we saved,
					// not the one inside the privileged block...
					return new FactoryDynamicURLClassLoader(parent, urls);
				}
			});
	}

    /** This class loader permits to load classes from a set of classpaths.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    protected static final class FactoryDynamicURLClassLoader extends DynamicURLClassLoader {

    	/** Constructor.
    	 * @param parent is the parent class loader.
    	 * @param urls is the list of urls to insert inside the class loading path.
    	 */
    	protected FactoryDynamicURLClassLoader(ClassLoader parent, URL... urls) {
    		super(parent, urls);
    	}

    	@Override
    	public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    		// First check if we have permission to access the package. This
    		// should go away once we've added support for exported packages.
    		final SecurityManager sm = System.getSecurityManager();
    		if (sm != null) {
    			final int i = name.lastIndexOf('.');
    			if (i != -1) {
    				sm.checkPackageAccess(name.substring(0, i));
    			}
    		}
    		return super.loadClass(name, resolve);
    	}

    }

}

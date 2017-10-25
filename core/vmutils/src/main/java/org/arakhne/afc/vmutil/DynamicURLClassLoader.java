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

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/** This class loader permits to load classes from
 * a set of classpaths.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"restriction", "checkstyle:illegalimport"})
public class DynamicURLClassLoader extends SecureClassLoader {

	/**
	 * The search path for classes and resources.
	 */
	protected sun.misc.URLClassPath ucp;

	/** The context to be used when loading classes and resources.
	 */
	protected AccessControlContext acc;

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
	 * @param acc is the current access context
	 * @param urls the URLs from which to load classes and resources
	 * @throws  SecurityException  if a security manager exists and its
	 *     <code>checkCreateClassLoader</code> method doesn't allow
	 *     creation of a class loader.
	 * @see SecurityManager#checkCreateClassLoader
	 */
	protected DynamicURLClassLoader(ClassLoader parent, AccessControlContext acc, URL... urls) {
		super(parent);
		// this is to make the stack depth consistent with 1.1
		final SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkCreateClassLoader();
		}
		this.ucp = new sun.misc.URLClassPath(mergeClassPath(urls));
		this.acc = acc;
	}

	/**
	 * Appends the specified URL to the list of URLs to search for
	 * classes and resources.
	 *
	 * @param url the URL to be added to the search path of URLs
	 */
	public void addURL(final URL url) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				DynamicURLClassLoader.this.ucp.addURL(url);
				return null;
			}
		}, this.acc);
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
	 * Appends the specified URL to the list of URLs to search for
	 * classes and resources.
	 *
	 * @param urls the URL to be added to the search path of URLs
	 */
	public void removeURLs(URL... urls) {
		final Set<URL> set = new HashSet<>();
		set.addAll(Arrays.asList(this.ucp.getURLs()));
		set.removeAll(Arrays.asList(urls));
		final URL[] tab = new URL[set.size()];
		set.toArray(tab);
		this.ucp = new sun.misc.URLClassPath(tab);
	}

	/**
	 * Appends the specified URL to the list of URLs to search for
	 * classes and resources.
	 *
	 * @param url the URL to be added to the search path of URLs
	 */
	public void removeURL(URL url) {
		removeURLs(url);
	}

	/**
	 * Returns the search path of URLs for loading classes and resources.
	 * This includes the original list of URLs specified to the constructor,
	 * along with any URLs subsequently appended by the addURL() method.
	 * @return the search path of URLs for loading classes and resources.
	 */
	@Pure
	public URL[] getURLs() {
		return this.ucp.getURLs();
	}

	/**
	 * Finds and loads the class with the specified name from the URL search
	 * path. Any URLs referring to JAR files are loaded and opened as needed
	 * until the class is found.
	 *
	 * @param name the name of the class
	 * @return the resulting class
	 * @exception ClassNotFoundException if the class could not be found
	 */
	@Override
	@Pure
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
				@Override
				public Class<?> run() throws ClassNotFoundException {
					final String path = name.replace('.', '/').concat(".class"); //$NON-NLS-1$
					final sun.misc.Resource res = DynamicURLClassLoader.this.ucp.getResource(path, false);
					if (res != null) {
						try {
							return defineClass(name, res);
						} catch (IOException e) {
							throw new ClassNotFoundException(name, e);
						}
					}
					throw new ClassNotFoundException(name);
				}
			}, this.acc);
		} catch (java.security.PrivilegedActionException pae) {
			throw (ClassNotFoundException) pae.getException();
		}
	}

	/**
	 * Defines a Class using the class bytes obtained from the specified
	 * Resource. The resulting Class must be resolved before it can be
	 * used.
	 *
	 * @param name is the name of the class to define
	 * @param res is the resource from which the class byte-code could be obtained
	 * @return the loaded class.
	 * @throws IOException in case the byte-code was unavailable.
	 */
	protected Class<?> defineClass(String name, sun.misc.Resource res) throws IOException {
		final int i = name.lastIndexOf('.');
		final URL url = res.getCodeSourceURL();
		if (i != -1) {
			final String pkgname = name.substring(0, i);
			// Check if package already loaded.
			final Package pkg = getPackage(pkgname);
			final Manifest man = res.getManifest();
			if (pkg != null) {
				// Package found, so check package sealing.
				if (pkg.isSealed()) {
					// Verify that code source URL is the same.
					if (!pkg.isSealed(url)) {
						throw new SecurityException(Locale.getString("E1", pkgname)); //$NON-NLS-1$
					}
				} else {
					// Make sure we are not attempting to seal the package
					// at this code source URL.
					if ((man != null) && isSealed(pkgname, man)) {
						throw new SecurityException(Locale.getString("E2", pkgname)); //$NON-NLS-1$
					}
				}
			} else {
				if (man != null) {
					definePackage(pkgname, man, url);
				} else {
					definePackage(pkgname, null, null, null, null, null, null, null);
				}
			}
		}
		// Now read the class bytes and define the class
		final java.nio.ByteBuffer bb = res.getByteBuffer();
		if (bb != null) {
			// Use (direct) ByteBuffer:
			final CodeSigner[] signers = res.getCodeSigners();
			final CodeSource cs = new CodeSource(url, signers);
			return defineClass(name, bb, cs);
		}

		final byte[] b = res.getBytes();
		// must read certificates AFTER reading bytes.
		final CodeSigner[] signers = res.getCodeSigners();
		final CodeSource cs = new CodeSource(url, signers);
		return defineClass(name, b, 0, b.length, cs);

	}

	/**
	 * Defines a new package by name in this ClassLoader. The attributes
	 * contained in the specified Manifest will be used to obtain package
	 * version and sealing information. For sealed packages, the additional
	 * URL specifies the code source URL from which the package was loaded.
	 *
	 * @param name  the package name
	 * @param man   the Manifest containing package version and sealing
	 *              information
	 * @param url   the code source url for the package, or null if none
	 * @exception   IllegalArgumentException if the package name duplicates
	 *              an existing package either in this class loader or one
	 *              of its ancestors
	 * @return the newly defined Package object
	 */
	@SuppressWarnings("checkstyle:npathcomplexity")
	protected Package definePackage(String name, Manifest man, URL url) throws IllegalArgumentException {
		final String path = name.replace('.', '/').concat("/"); //$NON-NLS-1$
		String specTitle = null;
		String specVersion = null;
		String specVendor = null;
		String implTitle = null;
		String implVersion = null;
		String implVendor = null;
		String sealed = null;
		URL sealBase = null;

		Attributes attr = man.getAttributes(path);
		if (attr != null) {
			specTitle   = attr.getValue(Name.SPECIFICATION_TITLE);
			specVersion = attr.getValue(Name.SPECIFICATION_VERSION);
			specVendor  = attr.getValue(Name.SPECIFICATION_VENDOR);
			implTitle   = attr.getValue(Name.IMPLEMENTATION_TITLE);
			implVersion = attr.getValue(Name.IMPLEMENTATION_VERSION);
			implVendor  = attr.getValue(Name.IMPLEMENTATION_VENDOR);
			sealed      = attr.getValue(Name.SEALED);
		}
		attr = man.getMainAttributes();
		if (attr != null) {
			if (specTitle == null) {
				specTitle = attr.getValue(Name.SPECIFICATION_TITLE);
			}
			if (specVersion == null) {
				specVersion = attr.getValue(Name.SPECIFICATION_VERSION);
			}
			if (specVendor == null) {
				specVendor = attr.getValue(Name.SPECIFICATION_VENDOR);
			}
			if (implTitle == null) {
				implTitle = attr.getValue(Name.IMPLEMENTATION_TITLE);
			}
			if (implVersion == null) {
				implVersion = attr.getValue(Name.IMPLEMENTATION_VERSION);
			}
			if (implVendor == null) {
				implVendor = attr.getValue(Name.IMPLEMENTATION_VENDOR);
			}
			if (sealed == null) {
				sealed = attr.getValue(Name.SEALED);
			}
		}
		if ("true".equalsIgnoreCase(sealed)) { //$NON-NLS-1$
			sealBase = url;
		}
		return definePackage(name, specTitle, specVersion, specVendor,
				implTitle, implVersion, implVendor, sealBase);
	}

	/*
	 * Returns true if the specified package name is sealed according to the
	 * given manifest.
	 */
	private static boolean isSealed(String name, Manifest man) {
		final String path = name.replace('.', '/').concat("/"); //$NON-NLS-1$
		Attributes attr = man.getAttributes(path);
		String sealed = null;
		if (attr != null) {
			sealed = attr.getValue(Name.SEALED);
		}
		if (sealed == null) {
			attr = man.getMainAttributes();
			if (attr != null) {
				sealed = attr.getValue(Name.SEALED);
			}
		}
		return "true".equalsIgnoreCase(sealed); //$NON-NLS-1$
	}

	/**
	 * Finds the resource with the specified name on the URL search path.
	 *
	 * @param name the name of the resource
	 * @return a <code>URL</code> for the resource, or <code>null</code>
	 *     if the resource could not be found.
	 */
	@Override
	@Pure
	public URL findResource(final String name) {
		/*
		 * The same restriction to finding classes applies to resources
		 */
		final URL url = AccessController.doPrivileged(new PrivilegedAction<URL>() {
				@Override
				public URL run() {
					return DynamicURLClassLoader.this.ucp.findResource(name, true);
				}
			}, this.acc);

		return url != null ? this.ucp.checkURL(url) : null;
	}

	/**
	 * Returns an Enumeration of URLs representing all of the resources
	 * on the URL search path having the specified name.
	 *
	 * @param name the resource name
	 * @exception IOException if an I/O exception occurs
	 * @return an <code>Enumeration</code> of <code>URL</code>s
	 */
	@Override
	@Pure
	public Enumeration<URL> findResources(final String name) throws IOException {
		final Enumeration<?> e = this.ucp.findResources(name, true);

		return new Enumeration<URL>() {
			private URL url;

			private boolean next() {
				if (this.url != null) {
					return true;
				}
				do {
					final URL u = AccessController.doPrivileged(new PrivilegedAction<URL>() {
						@Override
						public URL run() {
							if (!e.hasMoreElements()) {
								return null;
							}
							return (URL) e.nextElement();
						}
					}, DynamicURLClassLoader.this.acc);
					if (u == null) {
						break;
					}
					this.url = DynamicURLClassLoader.this.ucp.checkURL(u);
				}
				while (this.url == null);

				return this.url != null;
			}

			@Override
			public URL nextElement() {
				if (!next()) {
					throw new NoSuchElementException();
				}
				final URL u = this.url;
				this.url = null;
				return u;
			}

			@Override
			public boolean hasMoreElements() {
				return next();
			}
		};
	}

	/**
	 * Returns the permissions for the given codesource object.
	 * The implementation of this method first calls super.getPermissions
	 * and then adds permissions based on the URL of the codesource.
	 *
	 * <p>If the protocol is "file"
	 * and the path specifies a file, then permission to read that
	 * file is granted. If protocol is "file" and the path is
	 * a directory, permission is granted to read all files
	 * and (recursively) all files and subdirectories contained in
	 * that directory.
	 *
	 * <p>If the protocol is not "file", then
	 * to connect to and accept connections from the URL's host is granted.
	 * @param codesource the codesource
	 * @return the permissions granted to the codesource
	 */
	@Override
	protected PermissionCollection getPermissions(CodeSource codesource) {
		final PermissionCollection perms = super.getPermissions(codesource);

		final URL url = codesource.getLocation();

		Permission permission;
		URLConnection urlConnection;

		try {
			urlConnection = url.openConnection();
			permission = urlConnection.getPermission();
		} catch (IOException ioe) {
			permission = null;
			urlConnection = null;
		}

		if ((permission != null) && (permission instanceof FilePermission)) {
			// if the permission has a separator char on the end,
			// it means the codebase is a directory, and we need
			// to add an additional permission to read recursively
			String path = permission.getName();
			if (path.endsWith(File.separator)) {
				path += "-"; //$NON-NLS-1$
				permission = new FilePermission(path, sun.security.util.SecurityConstants.FILE_READ_ACTION);
			}
		} else if ((permission == null) && (URISchemeType.FILE.isURL(url))) {
			String path = url.getFile().replace('/', File.separatorChar);
			path = sun.net.www.ParseUtil.decode(path);
			if (path.endsWith(File.separator)) {
				path += "-"; //$NON-NLS-1$
			}
			permission =  new FilePermission(path, sun.security.util.SecurityConstants.FILE_READ_ACTION);
		} else {
			URL locUrl = url;
			if (urlConnection instanceof JarURLConnection) {
				locUrl = ((JarURLConnection) urlConnection).getJarFileURL();
			}
			String host = locUrl.getHost();
			if (host == null) {
				host = "localhost"; //$NON-NLS-1$
			}
			permission = new SocketPermission(host,
					sun.security.util.SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION);
		}

		// make sure the person that created this class loader
		// would have this permission
		final SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			final Permission fp = permission;
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() throws SecurityException {
					sm.checkPermission(fp);
					return null;
				}
			}, this.acc);
		}
		perms.add(permission);

		return perms;
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
		// Save the caller's context
		final AccessControlContext acc = AccessController.getContext();
		// Need a privileged block to create the class loader
		return AccessController.doPrivileged(new PrivilegedAction<DynamicURLClassLoader>() {
				@Override
				public DynamicURLClassLoader run() {
					// Now set the context on the loader using the one we saved,
					// not the one inside the privileged block...
					return new FactoryDynamicURLClassLoader(parent, acc, urls);
				}
			});
	}

    /**
     * Merge the specified URLs to the current classpath.
     */
    private static URL[] mergeClassPath(URL... urls) {
    	final String path = System.getProperty("java.class.path"); //$NON-NLS-1$
    	final String separator = System.getProperty("path.separator"); //$NON-NLS-1$
    	final String[] parts = path.split(Pattern.quote(separator));
    	final URL[] u = new URL[parts.length + urls.length];
    	for (int i = 0; i < parts.length; ++i) {
    		try {
				u[i] = new File(parts[i]).toURI().toURL();
			} catch (MalformedURLException exception) {
				// ignore exception
			}
    	}
    	System.arraycopy(urls, 0, u, parts.length, urls.length);
    	return u;
    }

    /** This class loader permits to load classes from a set of classpaths.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    protected static final class FactoryDynamicURLClassLoader extends DynamicURLClassLoader {

    	/**
    	 * @param parent is the parent class loader.
    	 * @param acc is the accessible context.
    	 * @param urls is the list of urls to insert inside the class loading path.
    	 */
    	protected FactoryDynamicURLClassLoader(ClassLoader parent, AccessControlContext acc, URL... urls) {
    		super(parent, acc, urls);
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

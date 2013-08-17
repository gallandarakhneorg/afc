/* 
 * $Id$
 * 
 * Copyright (C) 2004-2008 Stephane GALLAND.
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
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.regex.Pattern;

import sun.misc.Resource;
import sun.misc.URLClassPath;
import sun.net.www.ParseUtil;
import sun.security.util.SecurityConstants;

/** This class loader permits to load classes from
 * a set of classpaths.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("restriction")
public class DynamicURLClassLoader extends SecureClassLoader {

	/**
	 * The search path for classes and resources.
	 */
	protected URLClassPath _ucp;
	
	/**
	 * The context to be used when loading classes and resources
	 */
	protected AccessControlContext _acc;
	
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
	 * @exception  SecurityException  if a security manager exists and its  
	 *             <code>checkCreateClassLoader</code> method doesn't allow 
	 *             creation of a class loader.
	 * @see SecurityManager#checkCreateClassLoader
	 */
	protected DynamicURLClassLoader(ClassLoader parent, AccessControlContext acc, URL... urls) {
		super(parent);
		// this is to make the stack depth consistent with 1.1
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkCreateClassLoader();
		}
		this._ucp = new URLClassPath(mergeClassPath(urls));
		this._acc = acc;
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
				DynamicURLClassLoader.this._ucp.addURL(url);
				return null;
			}
		}, this._acc);
	}
	
	/**
	 * Appends the specified URL to the list of URLs to search for
	 * classes and resources.
	 *
	 * @param urls the URLs to be added to the search path of URLs
	 */
	public void addURLs(URL... urls) {
		for (URL url : urls) {
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
		HashSet<URL> set = new HashSet<URL>();
		set.addAll(Arrays.asList(this._ucp.getURLs()));
		set.removeAll(Arrays.asList(urls));
		URL[] tab = new URL[set.size()];
		set.toArray(tab);
		this._ucp = new URLClassPath(tab);
		tab = null;
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
	public URL[] getURLs() {
		return this._ucp.getURLs();
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
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
				@Override
				public Class<?> run() throws ClassNotFoundException {
					String path = name.replace('.', '/').concat(".class"); //$NON-NLS-1$
					Resource res = DynamicURLClassLoader.this._ucp.getResource(path, false);
					if (res != null) {
						try {
							return defineClass(name, res);
						}
						catch (IOException e) {
							throw new ClassNotFoundException(name, e);
						}
					}
					throw new ClassNotFoundException(name);
				}
			}, this._acc);
		}
		catch (java.security.PrivilegedActionException pae) {
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
	protected Class<?> defineClass(String name, Resource res) throws IOException {
		int i = name.lastIndexOf('.');
		URL url = res.getCodeSourceURL();
		if (i != -1) {
			String pkgname = name.substring(0, i);
			// Check if package already loaded.
			Package pkg = getPackage(pkgname);
			Manifest man = res.getManifest();
			if (pkg != null) {
				// Package found, so check package sealing.
				if (pkg.isSealed()) {
					// Verify that code source URL is the same.
					if (!pkg.isSealed(url)) {
						throw new SecurityException(
								"sealing violation: package " + pkgname + " is sealed"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
				} else {
					// Make sure we are not attempting to seal the package
					// at this code source URL.
					if ((man != null) && isSealed(pkgname, man)) {
						throw new SecurityException(
								"sealing violation: can't seal package " + pkgname +  //$NON-NLS-1$
						": already loaded"); //$NON-NLS-1$
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
		java.nio.ByteBuffer bb = res.getByteBuffer();
		if (bb != null) {
			// Use (direct) ByteBuffer:
			CodeSigner[] signers = res.getCodeSigners();
			CodeSource cs = new CodeSource(url, signers);
			return defineClass(name, bb, cs);
		}

		byte[] b = res.getBytes();
		// must read certificates AFTER reading bytes.
		CodeSigner[] signers = res.getCodeSigners();
		CodeSource cs = new CodeSource(url, signers);
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
	protected Package definePackage(String name, Manifest man, URL url)
	throws IllegalArgumentException
	{
		String path = name.replace('.', '/').concat("/"); //$NON-NLS-1$
		String specTitle = null, specVersion = null, specVendor = null;
		String implTitle = null, implVersion = null, implVendor = null;
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
		String path = name.replace('.', '/').concat("/"); //$NON-NLS-1$
		Attributes attr = man.getAttributes(path);
		String sealed = null;
		if (attr != null) {
			sealed = attr.getValue(Name.SEALED);
		}
		if (sealed == null) {
			if ((attr = man.getMainAttributes()) != null) {
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
	 * if the resource could not be found.
	 */
	@Override
	public URL findResource(final String name) {
		/*
		 * The same restriction to finding classes applies to resources
		 */
		URL url = 
			AccessController.doPrivileged(new PrivilegedAction<URL>() {
				@Override
				public URL run() {
					return DynamicURLClassLoader.this._ucp.findResource(name, true);
				}
			}, this._acc);
		
		return url != null ? this._ucp.checkURL(url) : null;
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
	public Enumeration<URL> findResources(final String name) throws IOException {
		final Enumeration<?> e = this._ucp.findResources(name, true);
		
		return new Enumeration<URL>() {
			private URL url = null;
			
			private boolean next() {
				if (this.url != null) {
					return true;
				}
				do {
					URL u = AccessController.doPrivileged(new PrivilegedAction<URL>() {
						@Override
						public URL run() {
							if (!e.hasMoreElements())
								return null;
							return (URL)e.nextElement();
						}
					}, DynamicURLClassLoader.this._acc);
					if (u == null) break;
					this.url = DynamicURLClassLoader.this._ucp.checkURL(u);
				}
				while (this.url == null);
				
				return (this.url != null);
			}
			
			@Override
			public URL nextElement() {
				if (!next()) {
					throw new NoSuchElementException();
				}
				URL u = this.url;
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
	 * <p>
	 * If the protocol is "file"
	 * and the path specifies a file, then permission to read that
	 * file is granted. If protocol is "file" and the path is
	 * a directory, permission is granted to read all files
	 * and (recursively) all files and subdirectories contained in
	 * that directory.
	 * <p>
	 * If the protocol is not "file", then
	 * to connect to and accept connections from the URL's host is granted.
	 * @param codesource the codesource
	 * @return the permissions granted to the codesource
	 */
	@Override
	protected PermissionCollection getPermissions(CodeSource codesource) {
		PermissionCollection perms = super.getPermissions(codesource);
		
		URL url = codesource.getLocation();
		
		Permission p;
		URLConnection urlConnection;
		
		try {
			urlConnection = url.openConnection();
			p = urlConnection.getPermission();
		}
		catch (java.io.IOException ioe) {
			p = null;
			urlConnection = null;
		}
		
		if ((p!=null)&&(p instanceof FilePermission)) {
			// if the permission has a separator char on the end,
			// it means the codebase is a directory, and we need
			// to add an additional permission to read recursively
			String path = p.getName();
			if (path.endsWith(File.separator)) {
				path += "-"; //$NON-NLS-1$
				p = new FilePermission(path, SecurityConstants.FILE_READ_ACTION);
			}
		}
		else if ((p == null) && (URISchemeType.FILE.isURL(url))) {
			String path = url.getFile().replace('/', File.separatorChar);
			path = ParseUtil.decode(path);
			if (path.endsWith(File.separator))
				path += "-"; //$NON-NLS-1$
			p =  new FilePermission(path, SecurityConstants.FILE_READ_ACTION);
		}
		else {
			URL locUrl = url;
			if (urlConnection instanceof JarURLConnection) {
				locUrl = ((JarURLConnection)urlConnection).getJarFileURL();
			}
			String host = locUrl.getHost();
			if (host == null)
				host = "localhost"; //$NON-NLS-1$
			p = new SocketPermission(host,
					SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION);
		}
		
		// make sure the person that created this class loader
		// would have this permission
		
		final SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			final Permission fp = p;
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() throws SecurityException {
					sm.checkPermission(fp);
					return null;
				}
			}, this._acc);
		}
		perms.add(p);

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
	public static DynamicURLClassLoader newInstance(final ClassLoader parent, final URL... urls) {
		// Save the caller's context
		final AccessControlContext acc = AccessController.getContext();
		// Need a privileged block to create the class loader
		DynamicURLClassLoader ucl =
			AccessController.doPrivileged(new PrivilegedAction<DynamicURLClassLoader>() {
				@Override
				public DynamicURLClassLoader run() {
					// Now set the context on the loader using the one we saved,
					// not the one inside the privileged block...
					return new FactoryDynamicURLClassLoader(parent, acc, urls);
				}
			});
		return ucl;
	}
	
    /**
     * Merge the specified URLs to the current classpath.
     */
    private static URL[] mergeClassPath(URL... urls) {
    	String path = System.getProperty("java.class.path"); //$NON-NLS-1$
    	String separator = System.getProperty("path.separator"); //$NON-NLS-1$
    	String[] parts = path.split(Pattern.quote(separator));
    	URL[] u = new URL[parts.length+urls.length];
    	for(int i=0; i<parts.length; i++) {
    		try {
				u[i] = new File(parts[i]).toURI().toURL();
			}
    		catch (MalformedURLException _) {
				// ignore exception
			}
    	}
    	System.arraycopy(urls,0,u,parts.length,urls.length);
    	return u;
    }

    /** 
     * @author $Author: galland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    protected final static class FactoryDynamicURLClassLoader extends DynamicURLClassLoader {

    	/**
    	 * @param parent is the parent class loader.
    	 * @param acc is the accessible context.
    	 * @param urls is the list of urls to insert inside the class loading path.
    	 */
    	protected FactoryDynamicURLClassLoader(ClassLoader parent, AccessControlContext acc, URL... urls) {
    		super(parent,acc,urls);
    	}
    	
    	/** {@inheritDoc}
    	 * 
    	 * @param name {@inheritDoc}
    	 * @param resolve {@inheritDoc}
    	 * @return {@inheritDoc}
    	 * @throws ClassNotFoundException {@inheritDoc}
    	 */
    	@Override
    	public final synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    		// First check if we have permission to access the package. This
    		// should go away once we've added support for exported packages.
    		SecurityManager sm = System.getSecurityManager();
    		if (sm != null) {
    			int i = name.lastIndexOf('.');
    			if (i != -1) {
    				sm.checkPackageAccess(name.substring(0, i));
    			}
    		}
    		return super.loadClass(name, resolve);
    	}
    	
    }

}

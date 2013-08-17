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

package org.arakhne.vmutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class provides more generic means for loading
 * dynamical libraries.
 * <p>
 * The library loader may be enabled or not.
 * When library loader is enable, it is able to
 * retreive and load native libraries. When it is
 * disable, it ignore all the loading queries. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class LibraryLoader {

	private static volatile boolean DISABLE = false;
	
	/** Replies if this library loader is enable.
	 * <p>
	 * The library loader is able to load native libraries
	 * when it is enable. Otherwise it ignore all the loading
	 * queries.
	 *
	 * @return <code>true</code> if the library loader is enable,
	 * otherwise <code>false</code>
	 * @since 5.0
	 */
	public static boolean isEnable() {
		return !DISABLE;
	}
	
	/** Replies if this library loader is enable.
	 * <p>
	 * The library loader is able to load native libraries
	 * when it is enable. Otherwise it ignore all the loading
	 * queries.
	 *
	 * @param enable is <code>true</code> to allow this loader
	 * to retreive native libraries, or <code>false</code> to
	 * ignore all the loading queries.
	 * @since 5.0
	 */
	public static void setEnable(boolean enable) {
		DISABLE = !enable;
	}

	/**
     * Loads a code file with the specified filename from the local file
     * system as a dynamic library. The filename
     * argument must be a complete path name.
     * <p>
     * The call <code>LibraryLoader.load(name)</code> is effectively equivalent
     * to the call:
     * <blockquote><pre>
     * System.load(name)
     * </pre></blockquote>
     *
     * @param filename is the file to load.
     * @throws  SecurityException if a security manager exists and its
     *             <code>checkLink</code> method doesn't allow
     *             loading of the specified dynamic library
     * @throws  UnsatisfiedLinkError if the file does not exist.
     * @throws  NullPointerException if <code>filename</code> is
     *             <code>null</code>
     * @see        java.lang.System#load(java.lang.String)
     */
    public static void load(String filename) {
    	// Silently ignore loading query
    	if (DISABLE) return;
    	Runtime.getRuntime().load(filename);
    }

    /**
     * Loads the system library specified by the <code>libname</code>
     * argument. The manner in which a library name is mapped to the
     * actual system library is system dependent.
     * <p>
     * The call <code>LibraryLoader.loadLibrary(name)</code> is effectively
     * equivalent to the call
     * <blockquote><pre>
     * System.loadLibrary(name)
     * </pre></blockquote>
     *
     * @param      libname   the name of the library.
     * @throws  SecurityException  if a security manager exists and its
     *             <code>checkLink</code> method doesn't allow
     *             loading of the specified dynamic library
     * @throws  UnsatisfiedLinkError  if the library does not exist.
     * @throws  NullPointerException if <code>libname</code> is
     *             <code>null</code>
     * @see        java.lang.System#loadLibrary(java.lang.String)
     */
    public static void loadLibrary(String libname) {
    	// Silently ignore loading query
    	if (DISABLE) return;
    	Runtime.getRuntime().loadLibrary(libname);
    }

    /**
     * Loads a code file with the specified filename from the local file
     * system as a dynamic library. The filename
     * argument must be a complete path name.
     * <p>
     * The call <code>LibraryLoader.load(name)</code> is effectively equivalent
     * to the call:
     * <blockquote><pre>
     * System.load(name.getAbsolutePath())
     * </pre></blockquote>
     *
     * @param filename is the file to load.
     * @throws  SecurityException if a security manager exists and its
     *             <code>checkLink</code> method doesn't allow
     *             loading of the specified dynamic library
     * @throws  UnsatisfiedLinkError if the file does not exist.
     * @throws  NullPointerException if <code>filename</code> is
     *             <code>null</code>
     * @see        java.lang.System#load(java.lang.String)
     */
    public static void load(File filename) {
    	// Silently ignore loading query
    	if (DISABLE) return;
    	Runtime.getRuntime().load(filename.getAbsolutePath());
    }

    /** Replies the URL for the specified library.
     * 
     * @param libName is the name of the library
     * @return the URL where the specified library was located. 
     */
    public static URL findLibraryURL(String libName) {
        return findLibraryURL(null,libName,null,null);
    }
    
    /** Replies the URL for the specified library.
     * <p>
     * The call <code>LibraryLoader.findLibraryURL(path,name)</code> is effectively equivalent
     * to the call:
     * <blockquote><pre>
     * getClassLoader().getResource(path+System.mapLibraryName(name))
     * </pre></blockquote>
     * 
     * @param path is the resource's path where the library was located.
     * @param libName is the name of the library
     * @return the URL where the specified library was located. 
     */
    public static URL findLibraryURL(String path, String libName) {
    	return findLibraryURL(path, libName, null, null);
    }
    
    private static URL findLibraryURL(String path, String libName, String platform, String arch) {
        ClassLoader cl = ClassLoaderFinder.findClassLoader();
        assert(cl!=null);
        String resourcePath = path;
        if (resourcePath==null) resourcePath = ""; //$NON-NLS-1$
        else if ((resourcePath.length()>0)&&(!resourcePath.endsWith("/"))) { //$NON-NLS-1$
        	resourcePath += "/"; //$NON-NLS-1$
        }
        // Find the 64bits version of the DLL
        String realLibName;
        if (platform!=null) {
        	StringBuilder buf = new StringBuilder(libName);
        	buf.append("-"); //$NON-NLS-1$
        	buf.append(platform);
        	if (arch!=null) buf.append(arch);
        	realLibName = System.mapLibraryName(buf.toString());
        	int idx = realLibName.indexOf(libName);
        	if (idx>0) realLibName = realLibName.substring(idx);
        }
        else {
        	StringBuilder buf = new StringBuilder(libName);
        	if (arch!=null) buf.append(arch);
        	realLibName = System.mapLibraryName(buf.toString());
        }
        URL libRes = Resources.getResource(cl, resourcePath+realLibName);
        if (libRes!=null) return libRes;
        return Resources.getResource(cl,realLibName);
    }

    /**
     * Loads a code file with the specified filename from the local file
     * system as a dynamic library. The filename
     * argument must be a complete path name.
     *
     * @param filename is the file to load.
     * @throws IOException 
     * @throws  SecurityException if a security manager exists and its
     *             <code>checkLink</code> method doesn't allow
     *             loading of the specified dynamic library
     * @throws  UnsatisfiedLinkError if the file does not exist.
     * @throws  NullPointerException if <code>filename</code> is
     *             <code>null</code>
     * @see        java.lang.System#load(java.lang.String)
     */
	public static void load(URL filename) throws IOException {
    	// Silently ignore loading query
    	if (DISABLE) return;
    	
        if (URISchemeType.FILE.isURL(filename)) {
        	try {
        		load(new File(filename.toURI()));
        	} 
        	catch (URISyntaxException e) {
        		throw new FileNotFoundException(filename.toExternalForm());
        	}
        }
        else {
    	  // Create a tmp file to receive the library code.
    	  String libName = System.mapLibraryName("javaDynLib"); //$NON-NLS-1$
    	  String suffix = ".dll"; //$NON-NLS-1$
    	  String prefix = "javaDynLib"; //$NON-NLS-1$
    	  int pos = libName.lastIndexOf('.');
    	  if (pos>=0) {
    		  suffix = libName.substring(pos);
    		  prefix = libName.substring(0,pos);
    	  }
    	  File f = File.createTempFile(prefix,suffix);
    	  
    	  // Copy the library code into the local file
    	  FileOutputStream outs = new FileOutputStream(f);
		  InputStream ins = filename.openStream();
    	  try {
	    	  byte[] buffer = new byte[2048];
	    	  int lu;
	    	  while ((lu=ins.read(buffer))>0) {
	    		  outs.write(buffer,0,lu);
	    	  }
    	  }
    	  finally {
    		  ins.close();
    		  outs.close();
    	  }

    	  // Load the library from the local file
    	  load(f);
    	  
    	  // Delete local file
    	  f.deleteOnExit();
        }
    }
    
    /**
     * Search and load the dynamic library which is fitting the
     * current operating system (32 or 64bits operating system...).
     * A 64 bits library is assumed to be named <code>libname64.dll</code>
     * on Windows&reg; and <code>liblibname64.so</code> on Unix.
     * A 32 bits library is assumed to be named <code>libname32.dll</code>
     * on Windows&reg; and <code>liblibname32.so</code> on Unix.
     * A library which could be ran either on 32 and 64 platforms is assumed
     * to be named <code>libname.dll</code> on Windows&reg; and
     * <code>liblibname.so</code> on Unix.
     *
     * @param libname is the name of the library.
     * @throws IOException 
     * @throws  SecurityException if a security manager exists and its
     *             <code>checkLink</code> method doesn't allow
     *             loading of the specified dynamic library
     * @throws  UnsatisfiedLinkError if the file does not exist.
     * @throws  NullPointerException if <code>filename</code> is
     *             <code>null</code>
     * @see        java.lang.System#load(java.lang.String)
     */
    public static void loadPlatformDependentLibrary(String libname) throws IOException {
    	loadPlatformDependentLibrary(null,libname);
    }

	/** Replies the data model of the current operating system: 32 or 64 bits.
	 * 
	 * @return the integer which is corresponding to the data model, or <code>0</code> if
	 * it could not be determined.
	 */
	static int getOperatingSystemArchitectureDataModel() {
		String arch = System.getProperty("sun.arch.data.model"); //$NON-NLS-1$
		if (arch!=null) {
			try {
				return Integer.parseInt(arch); 
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable _) {
				//
			}
		}
		return 0;
	}

    /**
     * Search and load the dynamic library which is fitting the
     * current operating system (32 or 64bits operating system...).
     * A 64 bits library is assumed to be named <code>libname64.dll</code>
     * on Windows&reg; and <code>liblibname64.so</code> on Unix.
     * A 32 bits library is assumed to be named <code>libname32.dll</code>
     * on Windows&reg; and <code>liblibname32.so</code> on Unix.
     * A library which could be ran either on 32 and 64 platforms is assumed
     * to be named <code>libname.dll</code> on Windows&reg; and
     * <code>liblibname.so</code> on Unix.
     *
     * @param path is the resource's path where the library was located.
     * @param libname is the name of the library.
     * @throws IOException 
     * @throws  SecurityException if a security manager exists and its
     *             <code>checkLink</code> method doesn't allow
     *             loading of the specified dynamic library
     * @throws  UnsatisfiedLinkError if the file does not exist.
     * @throws  NullPointerException if <code>filename</code> is
     *             <code>null</code>
     * @see        java.lang.System#load(java.lang.String)
     */
    public static void loadPlatformDependentLibrary(String path, String libname) throws IOException {
    	loadPlatformDependentLibrary(libname, null, path);
    }
    
    private static URL getPlatformDependentLibrary(String[] paths, String libname, String platform) {
    	URL url;
    	int dataModel = getOperatingSystemArchitectureDataModel();
    	for(String path : paths) {
	    	// Load the 64 library
	    	if (dataModel==64) {
	    		url = findLibraryURL(path, libname, platform, "64"); //$NON-NLS-1$
	    		if (url!=null) return url;
	    	}
	    	// Load the 32 library
	    	else if (dataModel==32) {
	    		url = findLibraryURL(path, libname, platform, "32"); //$NON-NLS-1$
	    		if (url!=null) return url;
	    	}
	    	// Load the multi-platform library
			url = findLibraryURL(path, libname, platform, null);
			if (url!=null) return url;
    	}
    	return null;
    }

    /**
     * Search and load the dynamic library which is fitting the
     * current operating system (32 or 64bits operating system...).
     * A 64 bits library is assumed to be named <code>libname64.dll</code>
     * on Windows&reg; and <code>liblibname64.so</code> on Unix.
     * A 32 bits library is assumed to be named <code>libname32.dll</code>
     * on Windows&reg; and <code>liblibname32.so</code> on Unix.
     * A library which could be ran either on 32 and 64 platforms is assumed
     * to be named <code>libname.dll</code> on Windows&reg; and
     * <code>liblibname.so</code> on Unix.
     *
     * @param libname is the name of the library.
     * @param platform is the name of the current OS platform.
     * @param paths are the resource's paths where the library was located.
     * @throws IOException 
     * @throws  SecurityException if a security manager exists and its
     *             <code>checkLink</code> method doesn't allow
     *             loading of the specified dynamic library
     * @throws  UnsatisfiedLinkError if the file does not exist.
     * @throws  NullPointerException if <code>filename</code> is
     *             <code>null</code>
     * @see        java.lang.System#load(java.lang.String)
     */
    static void loadPlatformDependentLibrary(String libname, String platform, String... paths) throws IOException {    
    	URL url;
    	// Package version (according to Maven module)
    	url = getPlatformDependentLibrary(paths, libname, null);
    	if (url!=null) {
			try {
				load(url);
				// library loaded
				return;
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable e) {
				System.err.println("could not load "+url); //$NON-NLS-1$
				e.printStackTrace();
			}
    	}
    	// Eclipse version  (according to Maven module)
    	if (platform!=null) {
        	url = getPlatformDependentLibrary(paths, libname, platform);
        	if (url!=null) {
    			try {
    				load(url);
    				// library loaded
    				return;
    			}
				catch(AssertionError e) {
					throw e;
				}
    			catch(Throwable e) {
    				System.err.println("could not load "+url); //$NON-NLS-1$
    				e.printStackTrace();
    			}
        	}
    	}
    	
		// System-based loading
		loadLibrary(libname);
    }

}

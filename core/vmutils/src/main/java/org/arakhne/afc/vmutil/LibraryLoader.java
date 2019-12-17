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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class provides more generic means for loading
 * dynamical libraries.
 *
 * <p>The library loader may be enabled or not.
 * When library loader is enable, it is able to
 * retreive and load native libraries. When it is
 * disable, it ignore all the loading queries.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class LibraryLoader {

	private static final int BUFFER_SIZE = 4096;

	private static volatile boolean disable;

	private LibraryLoader() {
		//
	}

	/** Replies if this library loader is enable.
	 *
	 * <p>The library loader is able to load native libraries
	 * when it is enable. Otherwise it ignore all the loading
	 * queries.
	 *
	 * @return <code>true</code> if the library loader is enable,
	 *     otherwise <code>false</code>
	 * @since 5.0
	 */
	@Pure
	public static boolean isEnable() {
		return !disable;
	}

	/** Replies if this library loader is enable.
	 *
	 * <p>The library loader is able to load native libraries
	 * when it is enable. Otherwise it ignore all the loading
	 * queries.
	 *
	 * @param enable is <code>true</code> to allow this loader
	 *     to retreive native libraries, or <code>false</code> to
	 *     ignore all the loading queries.
	 * @since 5.0
	 */
	public static void setEnable(boolean enable) {
		disable = !enable;
	}

	/** Loads a code file with the specified filename from the local file
	 * system as a dynamic library. The filename
	 * argument must be a complete path name.
	 *
	 * <p>The call <code>LibraryLoader.load(name)</code> is effectively equivalent
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
		if (disable) {
			return;
		}
		Runtime.getRuntime().load(filename);
	}

	/**
	 * Loads a code file with the specified filename from the local file
	 * system as a dynamic library. The filename
	 * argument must be a complete path name.
	 *
	 * @param filename is the file to load.
	 * @throws IOException when reading error occurs.
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
		if (disable) {
			return;
		}

		if (URISchemeType.FILE.isURL(filename)) {
			try {
				load(new File(filename.toURI()));
			} catch (URISyntaxException e) {
				throw new FileNotFoundException(filename.toExternalForm());
			}
		} else {
			// Create a tmp file to receive the library code.
			final String libName = System.mapLibraryName("javaDynLib"); //$NON-NLS-1$
			String suffix = ".dll"; //$NON-NLS-1$
			String prefix = "javaDynLib"; //$NON-NLS-1$
			final int pos = libName.lastIndexOf('.');
			if (pos >= 0) {
				suffix = libName.substring(pos);
				prefix = libName.substring(0, pos);
			}
			final File file = File.createTempFile(prefix, suffix);

			// Copy the library code into the local file
			try (FileOutputStream outs = new FileOutputStream(file)) {
				try (InputStream ins = filename.openStream()) {
					final byte[] buffer = new byte[BUFFER_SIZE];
					int lu;
					while ((lu = ins.read(buffer)) > 0) {
						outs.write(buffer, 0, lu);
					}
				}
			}

			// Load the library from the local file
			load(file);

			// Delete local file
			file.deleteOnExit();
		}
	}

	/** Loads a code file with the specified filename from the local file
	 * system as a dynamic library. The filename
	 * argument must be a complete path name.
	 *
	 * <p>The call <code>LibraryLoader.load(name)</code> is effectively equivalent
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
		if (disable) {
			return;
		}
		Runtime.getRuntime().load(filename.getAbsolutePath());
	}

	/**
	 * Loads the system library specified by the <code>libname</code>
	 * argument. The manner in which a library name is mapped to the
	 * actual system library is system dependent.
	 *
	 * <p>The call <code>LibraryLoader.loadLibrary(name)</code> is effectively
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
		if (disable) {
			return;
		}
		Runtime.getRuntime().loadLibrary(libname);
	}

	/** Replies the URL for the specified library.
	 *
	 * @param libName is the name of the library
	 * @return the URL where the specified library was located.
	 */
	@Pure
	public static URL findLibraryURL(String libName) {
		return findLibraryURL(null, libName, null, null);
	}

	/** Replies the URL for the specified library.
	 *
	 * <p>The call <code>LibraryLoader.findLibraryURL(path,name)</code> is effectively equivalent
	 * to the call:
	 * <blockquote><pre>
	 * getClassLoader().getResource(path+System.mapLibraryName(name))
	 * </pre></blockquote>
	 *
	 * @param path is the resource's path where the library was located.
	 * @param libName is the name of the library
	 * @return the URL where the specified library was located.
	 */
	@Pure
	public static URL findLibraryURL(String path, String libName) {
		return findLibraryURL(path, libName, null, null);
	}

	private static URL findLibraryURL(String path, String libName, String platform, String arch) {
		final ClassLoader cl = ClassLoaderFinder.findClassLoader();
		assert cl != null;
		String resourcePath = path;
		if (resourcePath == null) {
			resourcePath = ""; //$NON-NLS-1$
		} else if ((resourcePath.length() > 0) && (!resourcePath.endsWith("/"))) { //$NON-NLS-1$
			resourcePath += "/"; //$NON-NLS-1$
		}
		// Find the 64bits version of the DLL
		String realLibName;
		if (platform != null) {
			final StringBuilder buf = new StringBuilder(libName);
			buf.append("-"); //$NON-NLS-1$
			buf.append(platform);
			if (arch != null) {
				buf.append(arch);
			}
			realLibName = System.mapLibraryName(buf.toString());
			final int idx = realLibName.indexOf(libName);
			if (idx > 0) {
				realLibName = realLibName.substring(idx);
			}
		} else {
			final StringBuilder buf = new StringBuilder(libName);
			if (arch != null) {
				buf.append(arch);
			}
			realLibName = System.mapLibraryName(buf.toString());
		}
		final URL libRes = Resources.getResource(cl, resourcePath + realLibName);
		if (libRes != null) {
			return libRes;
		}
		return Resources.getResource(cl, realLibName);
	}

	/** Replies the data model of the current operating system: 32 or 64 bits.
	 *
	 * @return the integer which is corresponding to the data model, or <code>0</code> if
	 *     it could not be determined.
	 */
	@Pure
	static int getOperatingSystemArchitectureDataModel() {
		final String arch = System.getProperty("sun.arch.data.model"); //$NON-NLS-1$
		if (arch != null) {
			try {
				return Integer.parseInt(arch);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable exception) {
				//
			}
		}
		return 0;
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static URL getPlatformDependentLibrary(String[] paths, String libname, String platform) {
		URL url;
		final int dataModel = getOperatingSystemArchitectureDataModel();
		for (final String path : paths) {
			if (dataModel == 64) {
				// Load the 64 library
				url = findLibraryURL(path, libname, platform, "64"); //$NON-NLS-1$
				if (url != null) {
					return url;
				}
			} else if (dataModel == 32) {
				// Load the 32 library
				url = findLibraryURL(path, libname, platform, "32"); //$NON-NLS-1$
				if (url != null) {
					return url;
				}
			}
			// Load the multi-platform library
			url = findLibraryURL(path, libname, platform, null);
			if (url != null) {
				return url;
			}
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
	 * @throws IOException when reading error occurs.
	 * @throws  SecurityException if a security manager exists and its
	 *             <code>checkLink</code> method doesn't allow
	 *             loading of the specified dynamic library
	 * @throws  UnsatisfiedLinkError if the file does not exist.
	 * @throws  NullPointerException if <code>filename</code> is
	 *             <code>null</code>
	 * @see        java.lang.System#load(java.lang.String)
	 */
	@Inline(value = "LibraryLoader.loadPlatformDependentLibrary(null, $1)", imported = {LibraryLoader.class},
			statementExpression = true)
	public static void loadPlatformDependentLibrary(String libname) throws IOException {
		loadPlatformDependentLibrary(null, libname);
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
	 * @throws IOException when reading error occurs.
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
	 * @throws IOException when reading error occurs.
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
		if (url != null) {
			try {
				load(url);
				// library loaded
				return;
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		// Eclipse version  (according to Maven module)
		if (platform != null) {
			url = getPlatformDependentLibrary(paths, libname, platform);
			if (url != null) {
				try {
					load(url);
					// library loaded
					return;
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable e) {
					//
				}
			}
		}

		// System-based loading
		loadLibrary(libname);
	}

}

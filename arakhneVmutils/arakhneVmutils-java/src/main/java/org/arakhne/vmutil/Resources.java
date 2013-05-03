/* 
 * $Id$
 * 
 * Copyright (C) 2008-2013 Stephane GALLAND.
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

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This utility class provides to load resources according to
 * several heuristics:<ul>
 * <li>search the resource in class paths;</li>
 * <li>search the resource in ./resources subdirectory in class paths.</li>
 * </ul>
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.2
 */
public class Resources {

	private static ResourceWrapper currentResourceInstance = null;

	static {
		URLHandlerUtil.installArakhneHandlers();
		
		ResourceWrapper wrapper = null;
		
		switch(OperatingSystem.getCurrentOS()) {
		case ANDROID:
			wrapper = new AndroidResourceWrapper();
			break;
		case AIX:
		case BSD:
		case FREEBSD:
		case HPUX:
		case LINUX:
		case MACOSX:
		case NETBSD:
		case OPENBSD:
		case OTHER:
		case SOLARIS:
		case WIN:
		default:
		}
		
		if (wrapper==null) {
			currentResourceInstance = new StandardJREResourceWrapper();
		}
		else {
			currentResourceInstance = wrapper;
		}
	}

	/** Character used to separate paths on an resource name.
	 */
	public static final String NAME_SEPARATOR = "/"; //$NON-NLS-1$

	/**
     * Replies the URL of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames.
     * <p>
     * The class loader replied by {@link ClassLoaderFinder} is used.
     * If it is <code>null</code>, the class loader of
     * the Resources class is used.
     *
     * @param path is the absolute path of the resource. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     */
    public static URL getResource(String path) {
    	return getResource((ClassLoader)null, path);
    }

   	/**
     * Replies the URL of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames.
     * <p>
     * The name of <var>packagename</var> is translated into a resource
     * path (by replacing the dots by slashes) and the given path
     * is append to. For example, the two following codes are equivalent:<pre><code>
     * Resources.getResources(Package.getPackage("org.arakhne.afc"), "/a/b/c/d.png");
     * Resources.getResources("org/arakhne/afc/a/b/c/d.png");
     * </code></pre>
     * <p>
     * If the <var>classLoader</var> parameter is <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * If this last is <code>null</code>, the class loader of
     * the Resources class is used.
     *
     * @param classLoader is the research scope. If <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * @param packagename is the package in which the resource should be located.
     * @param path is the relative path of the resource in the package. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     * @since 6.2
     */
    public static URL getResource(ClassLoader classLoader, Package packagename, String path) {
    	if (packagename==null || path==null) return null;
    	StringBuilder b = new StringBuilder();
    	b.append(packagename.getName().replaceAll(
    			Pattern.quote("."), //$NON-NLS-1$
    			Matcher.quoteReplacement(NAME_SEPARATOR)));
    	if (!path.startsWith(NAME_SEPARATOR)) {
    		b.append(NAME_SEPARATOR);
    	}
    	b.append(path);
    	return getResource(packagename.getClass().getClassLoader(), b.toString());
    }

   	/**
     * Replies the URL of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames.
     * <p>
     * The name of <var>classname</var> is translated into a resource
     * path (by remove the name of the class and replacing the dots by slashes) and the given path
     * is append to. For example, the two following codes are equivalent:<pre><code>
     * Resources.getResources(Resources.class, "/a/b/c/d.png");
     * Resources.getResources("org/arakhne/vmutil/a/b/c/d.png");
     * </code></pre>
     * <p>
     * The class loader of the given class is used. If it is <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * If it is also <code>null</code>, the class loader of this Resources
     * class is used.
     *
     * @param classname is located in the package in which the resource should be also located.
     * @param path is the absolute path of the resource. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     */
    public static URL getResource(Class<?> classname, String path) {
    	if (classname==null) return null;
    	URL u = getResource(classname.getClassLoader(), classname.getPackage(), path);
    	if (u==null)
    		u = getResource(classname.getClassLoader(), path);
    	return u;
    }

    /**
     * Replies the URL of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames.
     * <p>
     * If the <var>classLoader</var> parameter is <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * If this last is <code>null</code>, the class loader of
     * the Resources class is used.
     *
     * @param classLoader is the research scope. If <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * @param path is the absolute path of the resource. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     */
    public static URL getResource(ClassLoader classLoader, String path) {
    	return currentResourceInstance.getResource(classLoader, path);
    }
    
    /**
     * Replies the input stream of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames, and may not start the
     * path with a slash.
     * <p>
     * The class loader replied by {@link ClassLoaderFinder} is used.
     * If it is <code>null</code>, the class loader of
     * the Resources class is used.
     *
     * @param path is the absolute path of the resource. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     */
    public static InputStream getResourceAsStream(String path) {
    	return getResourceAsStream((ClassLoader)null, path);
    }
    
   	/**
     * Replies the input stream of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames.
     * <p>
     * The name of <var>packagename</var> is translated into a resource
     * path (by replacing the dots by slashes) and the given path
     * is append to. For example, the two following codes are equivalent:<pre><code>
     * Resources.getResources(Package.getPackage("org.arakhne.afc"), "/a/b/c/d.png");
     * Resources.getResources("org/arakhne/afc/a/b/c/d.png");
     * </code></pre>
     * <p>
     * If the <var>classLoader</var> parameter is <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * If this last is <code>null</code>, the class loader of
     * the Resources class is used.
     *
     * @param classLoader is the research scope. If <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * @param packagename is the package in which the resource should be located.
     * @param path is the relative path of the resource in the package. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     * @since 6.2
     */
    public static InputStream getResourceAsStream(ClassLoader classLoader, Package packagename, String path) {
    	if (packagename==null || path==null) return null;
    	StringBuilder b = new StringBuilder();
    	b.append(packagename.getName().replaceAll(
    			Pattern.quote("."), //$NON-NLS-1$
    			Matcher.quoteReplacement(NAME_SEPARATOR)));
    	if (!path.startsWith(NAME_SEPARATOR)) {
    		b.append(NAME_SEPARATOR);
    	}
    	b.append(path);
    	return getResourceAsStream(classLoader, b.toString());
    }

   	/**
     * Replies the input stream of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames.
     * <p>
     * The name of <var>classname</var> is translated into a resource
     * path (by remove the name of the class and replacing the dots by slashes) and the given path
     * is append to. For example, the two following codes are equivalent:<pre><code>
     * Resources.getResources(Resources.class, "/a/b/c/d.png");
     * Resources.getResources("org/arakhne/vmutil/a/b/c/d.png");
     * </code></pre>
     * <p>
     * The class loader of the given class is used. If it is <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * If it is also <code>null</code>, the class loader of this Resources
     * class is used.
     *
     * @param classname is located in the package in which the resource should be also located.
     * @param path is the absolute path of the resource. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     */
    @SuppressWarnings("resource")
	public static InputStream getResourceAsStream(Class<?> classname, String path) {
    	if (classname==null) return null;
    	InputStream is = getResourceAsStream(classname.getClassLoader(), classname.getPackage(), path);
    	if (is==null)
    		is = getResourceAsStream(classname.getClassLoader(), path);
    	return is;
    }

    /**
     * Replies the input stream of a resource.
     * <p>
     * You may use Unix-like syntax to write the resource path, ie.
     * you may use slashes to separate filenames, and may not start the
     * path with a slash.
     * <p>
     * If the <var>classLoader</var> parameter is <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * If this last is <code>null</code>, the class loader of
     * the Resources class is used.
     *
     * @param classLoader is the research scope. If <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * @param path is the absolute path of the resource. 
     * @return the url of the resource or <code>null</code> if the resource was
     * not found in class paths.
     */
    public static InputStream getResourceAsStream(ClassLoader classLoader, String path) {
    	return currentResourceInstance.getResourceAsStream(classLoader, path);
    }

   	/**
     * Replies the URL of a property resource that is associated to the given class.
     *
     * @param classname is the class for which the property resource should be replied.
     * @param locale is the expected localization of the resource file; or <code>null</code>
     * for the default. 
     * @return the url of the property resource or <code>null</code> if the resource was
     * not found in class paths.
     * @since 7.0
     */
    public static URL getPropertyFile(Class<?> classname, Locale locale) {
    	return getPropertyFile(classname.getClassLoader(), classname, locale);
    }

   	/**
     * Replies the URL of a property resource that is associated to the given class.
     *
     * @param classLoader is the research scope. If <code>null</code>,
     * the class loader replied by {@link ClassLoaderFinder} is used.
     * @param classname is the class for which the property resource should be replied.
     * @param locale is the expected localization of the resource file; or <code>null</code>
     * for the default. 
     * @return the url of the property resource or <code>null</code> if the resource was
     * not found in class paths.
     */
    public static URL getPropertyFile(ClassLoader classLoader, Class<?> classname, Locale locale) {
    	StringBuilder name = new StringBuilder();
    	
    	// Localized file
    	if (locale!=null) {
    		String country = locale.getCountry();
    		if (country!=null && !country.isEmpty()) {
    	    	name.append(classname.getSimpleName());
    	    	name.append("_"); //$NON-NLS-1$
    	    	name.append(country);
    	    	name.append(".properties"); //$NON-NLS-1$
    	    	URL url = getResource(classLoader, classname.getPackage(), name.toString());
    	    	if (url!=null) {
    	    		return url;
    	    	}
    		}
    	}
    	
    	// Default property file
    	name.setLength(0);
    	name.append(classname.getSimpleName());
    	name.append(".properties"); //$NON-NLS-1$
    	return getResource(classLoader, classname.getPackage(), name.toString());
    }

    /** Translate the given resource name according to the current JVM standard.
     * <p>
     * The <code>resourceName</code> argument should be a fully 
     * qualified class name. However, for compatibility with earlier 
     * versions, Sun's Java SE Runtime Environments do not verify this,
     * and so it is possible to access <code>PropertyResourceBundle</code>s
     * by specifying a path name (using "/") instead of a fully 
     * qualified class name (using ".").
     * In several VM, such as Dalvik, the translation from "." to "/" is not
     * automatically done by the VM to retreive the file.
     * 
     * @param resourceName
     * @return the translated resource name.
     * @since 7.0
     */
    public static String translateResourceName(String resourceName) {
    	return currentResourceInstance.translateResourceName(resourceName);
    }

}

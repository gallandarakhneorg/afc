/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

/**
 * This interface provides implementations to load resources according to
 * several heuristics:<ul>
 * <li>search the resource in class paths;</li>
 * <li>search the resource in ./resources subdirectory in class paths.</li>
 * </ul>
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
public interface ResourceWrapper {

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
    public URL getResource(ClassLoader classLoader, String path);
    
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
    public InputStream getResourceAsStream(ClassLoader classLoader, String path);
    
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
     */
    public String translateResourceName(String resourceName);
    
}

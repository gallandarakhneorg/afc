/* 
 * $Id$
 * 
 * Copyright (C) 2004-2008, 2013 Stephane GALLAND.
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

/**
 * This utility class permits to find the better class loader
 * for your application.
 * <p>
 * It tries to find the preferred class loader registered with
 * {@link #setPreferredClassLoader(ClassLoader)}.
 * If none was found, the default class loader will be replied.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class ClassLoaderFinder {

	private static volatile ClassLoader dynamicLoader = null;
	
    /**
     * Replies the better class loader.
	 * <p>
	 * It tries to find the preferred class loader.
	 * If none was found, the default class loader will be replied.
	 * 
     * @return the class loader, never <code>null</code>
     */
    public static ClassLoader findClassLoader() {

    	if (dynamicLoader==null)
    		return ClassLoaderFinder.class.getClassLoader();
    	return dynamicLoader;
    }

    /**
     * Set the preferred class loader.
	 * 
     * @param class_loader is the preferred class loader
     */
    public static void setPreferredClassLoader(ClassLoader class_loader) {
    	if (class_loader!=dynamicLoader) {
	    	dynamicLoader = class_loader;
	    	Thread[] threads = new Thread[Thread.activeCount()];
	    	Thread.enumerate(threads);
	    	for(Thread t : threads) {
	    		if (t!=null)
	    			t.setContextClassLoader(class_loader);
	    	}
    	}
    }
    
    /**
     * Pop the preferred class loader.
     */
    public static void popPreferredClassLoader() {
    	ClassLoader sysLoader = ClassLoaderFinder.class.getClassLoader();
    	
    	if ((dynamicLoader==null)||
    		(dynamicLoader==sysLoader)) {
    		dynamicLoader = null;
	    	Thread[] threads = new Thread[Thread.activeCount()];
	    	Thread.enumerate(threads);
	    	for(Thread t : threads) {
	    		if (t!=null)
	    			t.setContextClassLoader(sysLoader);
	    	}
    		return;
    	}
    	
    	ClassLoader parent = dynamicLoader.getParent();
    	
    	dynamicLoader = (parent==sysLoader) ? null : parent;    	

    	Thread[] threads = new Thread[Thread.activeCount()];
    	Thread.enumerate(threads);
    	for(Thread t : threads) {
    		if (t!=null)
    			t.setContextClassLoader(parent);
    	}
    }

}

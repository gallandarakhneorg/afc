/* $Id$
 * 
 * Copyright (C) 2007-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.arakhne.afc.vmutil.ClasspathUtil;
import org.arakhne.afc.vmutil.FileSystem;

import junit.framework.TestCase;

/**
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
public class ClasspathUtilTest extends TestCase {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 */
	public static void testGetStartClasspath() {
		Iterator<URL> urls = ClasspathUtil.getStartClasspath();
		assertNotNull(urls);
		
		String[] paths = System.getProperty("java.class.path").split( //$NON-NLS-1$
				Pattern.quote(File.pathSeparator));
		
		for(int i=0; i<paths.length; i++) {
			URL u = FileSystem.convertStringToURL(paths[i], true);
			assertTrue(urls.hasNext());
			assertEquals(u, urls.next());
		}
	}
	
	/**
	 */
	public static void testGetCurrentClasspath_standardClassLoader() {
		Iterator<URL> urls = ClasspathUtil.getClasspath();
		assertNotNull(urls);
		
		String[] paths = System.getProperty("java.class.path").split( //$NON-NLS-1$
				Pattern.quote(File.pathSeparator));
		List<String> list = new ArrayList<String>(Arrays.asList(paths));

		while (urls.hasNext()) {
			URL u2 = urls.next();
			assertUrl(list, u2);
		}
	}
	
	private static void assertUrl(List<String> expected, URL actual) {
		assertNotNull("An url cannot be null", actual); //$NON-NLS-1$
		Iterator<String> iterator = expected.iterator();
		String u;
		if (iterator.hasNext()) {
			while (iterator.hasNext()) {
				u = iterator.next();
				URL url = FileSystem.convertStringToURL(u, true);
				if (isEquals(url, actual)) {
					iterator.remove();
					return;
				}
			}
		}
		
		System.out.println("Additional class path: "+actual); //$NON-NLS-1$
	}
		
	private static boolean isEquals(URL expected, URL actual) {
		String u1 = expected==null ? null : expected.toExternalForm().replaceFirst("/$", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String u2 = actual==null ? null : actual.toExternalForm().replaceFirst("/$", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (u1==u2) return true;
		if (u1==null || u2==null) return false;
		return u1.equals(u2);
	}

}

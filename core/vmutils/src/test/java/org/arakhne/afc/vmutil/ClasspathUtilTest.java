/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
@SuppressWarnings("static-method")
public class ClasspathUtilTest {

	/**
	 */
	@Test
	public void getStartClasspath() {
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
	@Test
	public void getCurrentClasspath_standardClassLoader() {
		Iterator<URL> urls = ClasspathUtil.getClasspath();
		assertNotNull(urls);
		
		String[] paths = System.getProperty("java.class.path").split( //$NON-NLS-1$
				Pattern.quote(File.pathSeparator));
		List<String> list = new ArrayList<>(Arrays.asList(paths));

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

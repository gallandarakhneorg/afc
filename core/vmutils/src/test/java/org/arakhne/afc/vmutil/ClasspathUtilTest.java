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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

@SuppressWarnings("all")
public class ClasspathUtilTest {

	private void assertSystemClasspath(Iterator<URL> urls) throws MalformedURLException {
		assertClasspathEquals(urls, notExpandedClasspath());
	}
	
	private static String toString(String url) {
		if (Pattern.matches("^.*[/\\\\]$", url)) {
			return url.substring(0, url.length() - 1);
		}
		return url;
	}

	private static String toString(URL url) {
		return toString(FileSystem.convertURLToFile(url).getAbsolutePath());
	}
	
	private void assertClasspathEquals(Iterator<URL> actuals, String... expecteds) {
		assertNotNull(actuals);
		List<URL> list = new ArrayList<>();
		while (actuals.hasNext()) {
			list.add(actuals.next());
		}
		String[] tab = new String[list.size()];
		for (int i = 0; i < tab.length; ++i) {
			tab[i] = toString(list.get(i));
		}
		try {
			assertArrayEquals(expecteds, tab);
		} catch (Throwable exception) {
			throw new ComparisonFailure(exception.getMessage(),
					Arrays.toString(expecteds).replaceAll(" +", "\n"), Arrays.toString(tab).replaceAll(" +", "\n"));
		}
	}

	@Test
	public void getStartClasspath_noChange() throws Exception {
		assertSystemClasspath(ClasspathUtil.getStartClasspath());
	}
	
	@Test
	public void getCurrentClasspath_noChange() throws Exception {
		assertSystemClasspath(ClasspathUtil.getClasspath());
	}
	
	private static final URL URL1;
	private static final URL URL2;

	static {
		try {
			URL1 = new File((File) null, "classpath1").toURI().toURL();
			URL2 = new File((File) null, "classpath2").toURI().toURL();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	private static String[] notExpandedClasspath() throws MalformedURLException {
		String[] system = System.getProperty("java.class.path").split( 
				Pattern.quote(File.pathSeparator));
		for (int i = 0; i < system.length; ++i) {
			system[i] = toString(system[i]);
		}
		return system;
	}

	private static String[] expandedClasspath() throws MalformedURLException {
		String[] system = notExpandedClasspath();
		String[] exp = Arrays.copyOf(system, system.length + 2);
		exp[exp.length - 2] = toString(URL1);
		exp[exp.length - 1] = toString(URL2);
		return exp;
	}
	
	private void installDynamicClassLoader() {
		DynamicURLClassLoader cl = DynamicURLClassLoader.newInstance(getClass().getClassLoader(), URL1, URL2);
		ClassLoaderFinder.setPreferredClassLoader(cl);
	}

	private void uninstallDynamicClassLoader() {
		ClassLoaderFinder.popPreferredClassLoader();
	}
	
	@Test
	public void getStartClasspath_change() throws Exception {
		installDynamicClassLoader();
		try {
			assertSystemClasspath(ClasspathUtil.getStartClasspath());
		} finally {
			uninstallDynamicClassLoader();
		}
	}
	
	@Test
	public void getCurrentClasspath_change() throws Exception {
		installDynamicClassLoader();
		try {
			assertClasspathEquals(
					ClasspathUtil.getClasspath(),
					expandedClasspath());
		} finally {
			uninstallDynamicClassLoader();
		}
	}

}

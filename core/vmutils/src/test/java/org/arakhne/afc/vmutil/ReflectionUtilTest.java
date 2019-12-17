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

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.junit.Test;

@SuppressWarnings("all")
public class ReflectionUtilTest {

	@Test
	public void matchesParametersClassObjectArray() {
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[0],
				new Object[0]));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[0],
				new Object[] { 1 }));
		
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class },
				new Object[0]));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class },
				new Object[] { 'c' }));
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class },
				new Object[] { 3. }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class },
				new Object[] { 4f }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class },
				new Object[] { 1 }));

		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[0]));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 'c' }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 3. }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 4. }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 1 }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 'c', "a" }));  //$NON-NLS-1$
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 3., "a" }));  //$NON-NLS-1$
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 4., "a" }));  //$NON-NLS-1$
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 1, "a" }));  //$NON-NLS-1$
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 'c', true }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 3., true }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 4., true }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 1, true }));

		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, Array.class },
				new Object[0]));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, Array.class },
				new Object[] { 1. }));
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, Array.class },
				new Object[] { 1., "a" }));  //$NON-NLS-1$
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, Array.class },
				new Object[] { 1., "a", null }));  //$NON-NLS-1$
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, Array.class },
				new Object[] { 1., "a", new int[0] }));  //$NON-NLS-1$
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, int[].class },
				new Object[] { 1., "a", new int[0] }));  //$NON-NLS-1$

		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Number.class, String.class, int[].class },
				new Object[] { 1., "a", new int[0] }));  //$NON-NLS-1$
	}
	
	@Test
	public void forNameStringClassLoader() {
		assertInlineParameterUsage(ReflectionUtil.class, "forName", String.class, ClassLoader.class); //$NON-NLS-1$
	}
	
	@Test
	public void getPackageClassesPackage() {
		assertInlineParameterUsage(ReflectionUtil.class, "getPackageClasses", Package.class); //$NON-NLS-1$
	}

	@Test
	public void matchesParametersMethodObjectArray() {
		assertInlineParameterUsage(ReflectionUtil.class, "matchesParameters", Method.class, Object[].class); //$NON-NLS-1$
	}

}

/* $Id$
 * 
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
package org.arakhne.vmutil;

import java.lang.reflect.Array;

import junit.framework.TestCase;

/**
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
public class ReflectionUtilTest extends TestCase {

	/**
	 */
	public static void testMatchesParametersClassObjectArray() {
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
				new Object[] { 'c', "a" })); //$NON-NLS-1$
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 3., "a" })); //$NON-NLS-1$
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 4., "a" })); //$NON-NLS-1$
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class },
				new Object[] { 1, "a" })); //$NON-NLS-1$
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
				new Object[] { 1., "a" })); //$NON-NLS-1$
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, Array.class },
				new Object[] { 1., "a", null })); //$NON-NLS-1$
		assertFalse(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, Array.class },
				new Object[] { 1., "a", new int[0] })); //$NON-NLS-1$
		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Double.class, String.class, int[].class },
				new Object[] { 1., "a", new int[0] })); //$NON-NLS-1$

		assertTrue(ReflectionUtil.matchesParameters(
				new Class<?>[] { Number.class, String.class, int[].class },
				new Object[] { 1., "a", new int[0] })); //$NON-NLS-1$
	}
	
}

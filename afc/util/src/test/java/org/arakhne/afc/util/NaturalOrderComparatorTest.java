/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.util;

import junit.framework.TestCase;

/** 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NaturalOrderComparatorTest extends TestCase {

	
	/**
	 */
	public static void testCompare() {
		NaturalOrderComparator<Object> comparator = new NaturalOrderComparator<Object>();
		
		Integer o1 = 1;
		Integer o2 = 2;
		Integer o3 = 3;
		Integer o4 = 10;
		Float v1 = 3f;
		Double v2 = 3.;
		String v3 = "3"; //$NON-NLS-1$
		Object v4 = new Object();
		
		assertEquals(0, comparator.compare(o1, o1));
		assertEquals(-1, comparator.compare(o1, o2));
		assertEquals(-1, comparator.compare(o1, o3));
		assertEquals(-1, comparator.compare(o1, o4));
		try {
			comparator.compare(o1, v1);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o1, v2);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o1, v3);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o1, v4);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}

		assertEquals(1, comparator.compare(o2, o1));
		assertEquals(0, comparator.compare(o2, o2));
		assertEquals(-1, comparator.compare(o2, o3));
		assertEquals(-1, comparator.compare(o2, o4));
		try {
			comparator.compare(o2, v1);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o2, v2);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o2, v3);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o2, v4);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}

	
		assertEquals(1, comparator.compare(o3, o1));
		assertEquals(1, comparator.compare(o3, o2));
		assertEquals(0, comparator.compare(o3, o3));
		assertEquals(-1, comparator.compare(o3, o4));
		try {
			comparator.compare(o3, v1);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o3, v2);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o3, v3);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o3, v4);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}

	
		assertEquals(1, comparator.compare(o4, o1));
		assertEquals(1, comparator.compare(o4, o2));
		assertEquals(1, comparator.compare(o4, o3));
		assertEquals(0, comparator.compare(o4, o4));
		try {
			comparator.compare(o4, v1);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o4, v2);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o4, v3);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
		try {
			comparator.compare(o4, v4);
			fail("Expecting UnsupportedNaturalOrderException"); //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException _) {
			// expected exception
		}
	}

}

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

package org.arakhne.afc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/** 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class NaturalOrderComparatorTest {

	/**
	 */
	@Test
	public void compare() {
		NaturalOrderComparator<Object> comparator = new NaturalOrderComparator<>();
		
		Integer o1 = 1;
		Integer o2 = 2;
		Integer o3 = 3;
		Integer o4 = 10;
		Float v1 = 3f;
		Double v2 = 3.;
		String v3 = "3";  //$NON-NLS-1$
		Object v4 = new Object();
		
		assertEquals(0, comparator.compare(o1, o1));
		assertEquals(-1, comparator.compare(o1, o2));
		assertEquals(-1, comparator.compare(o1, o3));
		assertEquals(-1, comparator.compare(o1, o4));
		try {
			comparator.compare(o1, v1);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o1, v2);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o1, v3);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o1, v4);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}

		assertEquals(1, comparator.compare(o2, o1));
		assertEquals(0, comparator.compare(o2, o2));
		assertEquals(-1, comparator.compare(o2, o3));
		assertEquals(-1, comparator.compare(o2, o4));
		try {
			comparator.compare(o2, v1);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o2, v2);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o2, v3);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o2, v4);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}

	
		assertEquals(1, comparator.compare(o3, o1));
		assertEquals(1, comparator.compare(o3, o2));
		assertEquals(0, comparator.compare(o3, o3));
		assertEquals(-1, comparator.compare(o3, o4));
		try {
			comparator.compare(o3, v1);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o3, v2);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o3, v3);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o3, v4);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}

	
		assertEquals(1, comparator.compare(o4, o1));
		assertEquals(1, comparator.compare(o4, o2));
		assertEquals(1, comparator.compare(o4, o3));
		assertEquals(0, comparator.compare(o4, o4));
		try {
			comparator.compare(o4, v1);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o4, v2);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o4, v3);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
		try {
			comparator.compare(o4, v4);
			fail("Expecting UnsupportedNaturalOrderException");  //$NON-NLS-1$
		}
		catch(UnsupportedNaturalOrderException exception) {
			// expected exception
		}
	}

}

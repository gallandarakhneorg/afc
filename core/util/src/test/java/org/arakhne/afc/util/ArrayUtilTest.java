/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import org.junit.Test;

@SuppressWarnings("all")
public class ArrayUtilTest {

	@Test
	public void shuffleObjectArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", Object[].class); //$NON-NLS-1$
	}

	@Test
	public void shuffleBooleanArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", boolean[].class); //$NON-NLS-1$
	}

	@Test
	public void shuffleByteArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", byte[].class); //$NON-NLS-1$
	}

	@Test
	public void shuffleCharArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", char[].class); //$NON-NLS-1$
	}

	@Test
	public void shuffleIntArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", int[].class); //$NON-NLS-1$
	}

	@Test
	public void shuffleLongArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", long[].class); //$NON-NLS-1$
	}

	@Test
	public void shuffleFloatArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", float[].class); //$NON-NLS-1$
	}

	@Test
	public void shuffleDoubleArray() {
		assertInlineParameterUsage(ArrayUtil.class, "shuffle", double[].class); //$NON-NLS-1$
	}

	@Test
	public void sizedIteratorObjectArray() {
		assertInlineParameterUsage(ArrayUtil.class, "sizedIterator", Object[].class); //$NON-NLS-1$
	}

	@Test
	public void iteratorObjectArray() {
		assertInlineParameterUsage(ArrayUtil.class, "iterator", Object[].class); //$NON-NLS-1$
	}

}

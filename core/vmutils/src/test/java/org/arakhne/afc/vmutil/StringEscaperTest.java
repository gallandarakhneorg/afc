/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class StringEscaperTest {

	@Test
	public void escape_01() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("abc", escaper.escape("abc")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void escape_02() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("a\\nb\\tc", escaper.escape("a\nb\tc")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void escape_03() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("a\\\\nbc\\\\T", escaper.escape("a\\nbc\\T")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void escape_04() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("ab/c", escaper.escape("ab/c")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void escape_05() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("ab\\\"c", escaper.escape("ab\"c")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void escape_06() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("ab'c", escaper.escape("ab'c")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void escape_07() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("ab\\u0004c", escaper.escape("ab\u0004c")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void escape_08() {
		StringEscaper escaper = new StringEscaper();
		assertEquals("ab\\\\u0004c", escaper.escape("ab\\u0004c")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void formatHex_01() {
		assertEquals("4f", StringEscaper.formatHex(0x4F, 1)); //$NON-NLS-1$
	}

	@Test
	public void formatHex_02() {
		assertEquals("4f", StringEscaper.formatHex(0x4F, 2)); //$NON-NLS-1$
	}

	@Test
	public void formatHex_03() {
		assertEquals("04f", StringEscaper.formatHex(0x4F, 3)); //$NON-NLS-1$
	}

	@Test
	public void formatHex_04() {
		assertEquals("004f", StringEscaper.formatHex(0x4F, 4)); //$NON-NLS-1$
	}

}

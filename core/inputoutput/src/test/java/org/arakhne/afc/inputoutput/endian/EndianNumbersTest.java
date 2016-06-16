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

package org.arakhne.afc.inputoutput.endian;

import static org.junit.Assert.*;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.*;

import org.junit.Test;

import org.arakhne.afc.testtools.AbstractTestCase;

@SuppressWarnings("all")
public final class EndianNumbersTest extends AbstractTestCase {

	@Test
	public void toBEShort() {
		assertEquals(31688, EndianNumbers.toBEShort(123, 456));
    }

	@Test
	public void toLEShort() {
		assertEquals(-14213, EndianNumbers.toLEShort(123, 456));
    }

	@Test
	public void toLEInt() {
		assertEquals(-1625962373, EndianNumbers.toLEInt(123, 456, 789, 159));
    }

	@Test
	public void toBEInt() {
		assertEquals(2076710303, EndianNumbers.toBEInt(123, 456, 789, 159));
   }

	@Test
	public void toLELong() {
		assertEquals(754801260, EndianNumbers.toLELong(123, 456, 789, 159, 753, 145, 487, 653));
    }

	@Test
	public void toBELong() {
		assertEquals(1834614060, EndianNumbers.toBELong(123, 456, 789, 159, 753, 145, 487, 653));
    }

	@Test
	public void toLEDouble() {
		assertEpsilonEquals(0, EndianNumbers.toLEDouble(123, 456, 789, 159, 753, 145, 487, 653));
		assertInlineParameterUsage(EndianNumbers.class, "toLEDouble", int.class, int.class, int.class, int.class, //$NON-NLS-1$
				int.class, int.class, int.class, int.class);
	}

	@Test
	public void toBEDouble() {
		assertEpsilonEquals(0, EndianNumbers.toBEDouble(123, 456, 789, 159, 753, 145, 487, 653));
		assertInlineParameterUsage(EndianNumbers.class, "toBEDouble", int.class, int.class, int.class, int.class, //$NON-NLS-1$
				int.class, int.class, int.class, int.class);
	}

	@Test
	public void toLEFloat() {
		assertEpsilonEquals(0, EndianNumbers.toLEFloat(123, 456, 789, 159));
		assertInlineParameterUsage(EndianNumbers.class, "toLEFloat", int.class, int.class, int.class, int.class); //$NON-NLS-1$
	}

	@Test
	public void toBEFloat() {
		assertEpsilonEquals(2.077795799172964e36, EndianNumbers.toBEFloat(123, 456, 789, 159));
		assertInlineParameterUsage(EndianNumbers.class, "toBEFloat", int.class, int.class, int.class, int.class); //$NON-NLS-1$
	}

	@Test
	public void parseLEShort() {
		assertArrayEquals(new byte[]{64, -30}, EndianNumbers.parseLEShort((short) 123456));
    }

	@Test
	public void parseLEInt() {
		assertArrayEquals(new byte[]{21, -51, 91, 7}, EndianNumbers.parseLEInt(123456789));
    }

	@Test
	public void parseLEFloat() {
		assertArrayEquals(new byte[]{-51, -27, 64, 70}, EndianNumbers.parseLEFloat(12345.45f));
		assertInlineParameterUsage(EndianNumbers.class, "parseLEFloat", float.class); //$NON-NLS-1$
    }

	@Test
	public void parseLELong() {
		assertArrayEquals(new byte[]{21, -51, 91, 7, 0, 0, 0, 0}, EndianNumbers.parseLELong(123456789));
    }

	@Test
	public void parseLEDouble() {
		assertArrayEquals(new byte[]{-113, -62, -43, 85, 52, 111, -99, 65}, EndianNumbers.parseLEDouble(123456789.45875));
		assertInlineParameterUsage(EndianNumbers.class, "parseLEDouble", double.class); //$NON-NLS-1$
    }	

	@Test
	public void parseBEShort() {
		assertArrayEquals(new byte[]{-30, 64}, EndianNumbers.parseBEShort((short) 123456));
    }

	@Test
	public void parseBEInt() {
		assertArrayEquals(new byte[]{7, 91, -51, 21}, EndianNumbers.parseBEInt(123456789));
    }

	@Test
	public void parseBEFloat() {
		assertArrayEquals(new byte[]{70, 64, -27, -51}, EndianNumbers.parseBEFloat(12345.45f));
		assertInlineParameterUsage(EndianNumbers.class, "parseBEFloat", float.class); //$NON-NLS-1$
    }

	@Test
	public void parseBELong() {
		assertArrayEquals(new byte[]{0, 0, 0, 0, 7, 91, -51, 21}, EndianNumbers.parseBELong(123456789));
    }

	@Test
	public void parseBEDouble() {
		assertArrayEquals(new byte[]{65, -99, 111, 52, 85, -43, -62, -113}, EndianNumbers.parseBEDouble(123456789.45875));
		assertInlineParameterUsage(EndianNumbers.class, "parseBEDouble", double.class); //$NON-NLS-1$
    }	
	
}

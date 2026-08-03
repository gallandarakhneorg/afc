/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.testtools.AbstractTestCase;

@DisplayName("EndianNumbers")
@SuppressWarnings("all")
public final class EndianNumbersTest extends AbstractTestCase {

	@DisplayName("toBEShort")
	@Test
	public void toBEShort() {
		assertEquals(31688, EndianNumbers.toBEShort(123, 456));
    }

	@DisplayName("toLEShort")
	@Test
	public void toLEShort() {
		assertEquals(-14213, EndianNumbers.toLEShort(123, 456));
    }

	@DisplayName("toLEInt")
	@Test
	public void toLEInt() {
		assertEquals(-1625962373, EndianNumbers.toLEInt(123, 456, 789, 159));
    }

	@DisplayName("toBEInt")
	@Test
	public void toBEInt() {
		assertEquals(2076710303, EndianNumbers.toBEInt(123, 456, 789, 159));
   }

	@DisplayName("toLELong")
	@Test
	public void toLELong() {
		assertEquals(754801260, EndianNumbers.toLELong(123, 456, 789, 159, 753, 145, 487, 653));
    }

	@DisplayName("toBELong")
	@Test
	public void toBELong() {
		assertEquals(1834614060, EndianNumbers.toBELong(123, 456, 789, 159, 753, 145, 487, 653));
    }

	@DisplayName("toLEDouble #1")
	@Test
	public void toLEDouble_1() {
		assertEpsilonEquals(0, EndianNumbers.toLEDouble(123, 456, 789, 159, 753, 145, 487, 653));
	}

	@DisplayName("toLEDouble #2")
	@Test
	public void toLEDouble_2() {
		assertInlineParameterUsage(EndianNumbers.class, "toLEDouble", int.class, int.class, int.class, int.class, //$NON-NLS-1$
				int.class, int.class, int.class, int.class);
	}

	@DisplayName("toBEDouble #1")
	@Test
	public void toBEDouble_1() {
		assertEpsilonEquals(0, EndianNumbers.toBEDouble(123, 456, 789, 159, 753, 145, 487, 653));
	}

	@DisplayName("toBEDouble #2")
	@Test
	public void toBEDouble_2() {
		assertInlineParameterUsage(EndianNumbers.class, "toBEDouble", int.class, int.class, int.class, int.class, //$NON-NLS-1$
				int.class, int.class, int.class, int.class);
	}

	@DisplayName("toLEFloat #1")
	@Test
	public void toLEFloat_1() {
		assertEpsilonEquals(0, EndianNumbers.toLEFloat(123, 456, 789, 159));
	}

	@DisplayName("toLEFloat #2")
	@Test
	public void toLEFloat_2() {
		assertInlineParameterUsage(EndianNumbers.class, "toLEFloat", int.class, int.class, int.class, int.class); //$NON-NLS-1$
	}

	@DisplayName("toBEFloat #1")
	@Test
	public void toBEFloat_1() {
		assertEpsilonEquals(2.077795799172964e36, EndianNumbers.toBEFloat(123, 456, 789, 159));
	}

	@DisplayName("toBEFloat #2")
	@Test
	public void toBEFloat_2() {
		assertInlineParameterUsage(EndianNumbers.class, "toBEFloat", int.class, int.class, int.class, int.class); //$NON-NLS-1$
	}

	@DisplayName("parseLEShort")
	@Test
	public void parseLEShort() {
		assertArrayEquals(new byte[]{64, -30}, EndianNumbers.parseLEShort((short) 123456));
    }

	@DisplayName("parseLEInt")
	@Test
	public void parseLEInt() {
		assertArrayEquals(new byte[]{21, -51, 91, 7}, EndianNumbers.parseLEInt(123456789));
    }

	@DisplayName("parseLEFloat #1")
	@Test
	public void parseLEFloat_1() {
		assertArrayEquals(new byte[]{-51, -27, 64, 70}, EndianNumbers.parseLEFloat(12345.45f));
    }

	@DisplayName("parseLEFloat #2")
	@Test
	public void parseLEFloat_2() {
		assertInlineParameterUsage(EndianNumbers.class, "parseLEFloat", float.class); //$NON-NLS-1$
    }

	@DisplayName("parseLELong")
	@Test
	public void parseLELong() {
		assertArrayEquals(new byte[]{21, -51, 91, 7, 0, 0, 0, 0}, EndianNumbers.parseLELong(123456789));
    }

	@DisplayName("parseLEDouble #1")
	@Test
	public void parseLEDouble_1() {
		assertArrayEquals(new byte[]{-113, -62, -43, 85, 52, 111, -99, 65}, EndianNumbers.parseLEDouble(123456789.45875));
    }	

	@DisplayName("parseLEDouble #2")
	@Test
	public void parseLEDouble_2() {
		assertInlineParameterUsage(EndianNumbers.class, "parseLEDouble", double.class); //$NON-NLS-1$
    }	

	@DisplayName("parseBEShort")
	@Test
	public void parseBEShort() {
		assertArrayEquals(new byte[]{-30, 64}, EndianNumbers.parseBEShort((short) 123456));
    }

	@DisplayName("parseBEInt")
	@Test
	public void parseBEInt() {
		assertArrayEquals(new byte[]{7, 91, -51, 21}, EndianNumbers.parseBEInt(123456789));
    }

	@DisplayName("parseBEFloat #1")
	@Test
	public void parseBEFloat_1() {
		assertArrayEquals(new byte[]{70, 64, -27, -51}, EndianNumbers.parseBEFloat(12345.45f));
    }

	@DisplayName("parseBEFloat #2")
	@Test
	public void parseBEFloat_2() {
		assertInlineParameterUsage(EndianNumbers.class, "parseBEFloat", float.class); //$NON-NLS-1$
    }

	@DisplayName("parseBELong")
	@Test
	public void parseBELong() {
		assertArrayEquals(new byte[]{0, 0, 0, 0, 7, 91, -51, 21}, EndianNumbers.parseBELong(123456789));
    }

	@DisplayName("parseBEDouble #1")
	@Test
	public void parseBEDouble_1() {
		assertArrayEquals(new byte[]{65, -99, 111, 52, 85, -43, -62, -113}, EndianNumbers.parseBEDouble(123456789.45875));
    }	
	
	@DisplayName("parseBEDouble #2")
	@Test
	public void parseBEDouble_2() {
		assertInlineParameterUsage(EndianNumbers.class, "parseBEDouble", double.class); //$NON-NLS-1$
    }	

}

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

package org.arakhne.afc.math.geometry.d1.tests.afp;

import static org.junit.jupiter.api.Assertions.*;

import org.arakhne.afc.math.geometry.base.d1.Point1D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d1.Vector1D;
import org.arakhne.afc.math.geometry.d1.afp.Rectangle1afp;
import org.arakhne.afc.math.geometry.d1.afp.RectangularShape1afp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public abstract class AbstractRectangle1afpTestCase<
			SH extends RectangularShape1afp<?, ? super SH, ?, ?, ? super SG, ?>,
			SG extends Segment1D<?, ?>,
			B extends Rectangle1afp<?, ?, ?, ?, ?, ?>>
		extends AbstractRectangularShape1afpTestCase<SH, SG, B> {

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertFalse(getSH().equalsToShape(null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(getSH().equalsToShape(getSH()));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertTrue(getSH().equalsToShape(createShape()));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertTrue(getSH().equalsToShape((SH) getSH().clone()));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertFalse(getSH().equalsToShape(createShape(1.235, -3.459, 10.254, 14.)));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertFalse(getSH().equalsToShape(createShape(1.235, 0., 10.254, 14.)));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertFalse(getSH().equalsToShape(createShape(2.235, -3.459, 10.254, 14.963)));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertFalse(getSH().equalsToShape(createShape(1.235, -3.459, 12.0, 14.963)));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertFalse(getSH().equalsToShape(createShape(1.235, -3.459, 10.254, 15.0)));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertTrue(getSH().equalsToShape(createShape(1.235, -3.459, 10.254, 14.963)));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertFalse(getSH().equalsToShape(createShape(1.235 + 1e-12, -3.459, 10.254, 14.963)));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertFalse(getSH().equalsToShape(createShape(1.235, -3.459, 10.254, 14.963 + 1e-12)));
		}

	}

}

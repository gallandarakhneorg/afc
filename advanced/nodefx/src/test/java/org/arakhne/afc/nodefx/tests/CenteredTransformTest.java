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

package org.arakhne.afc.nodefx.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.nodefx.CenteringTransform;
import org.arakhne.afc.testtools.AbstractTestCase;

@DisplayName("CenteredTransform")
@SuppressWarnings("all")
public class CenteredTransformTest extends AbstractTestCase {

	private BooleanProperty trueProp;

	private BooleanProperty falseProp;

	private CenteringTransform[] ts;

	private Rectangle2d viz;

	private ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> vizProp;

	@BeforeEach
	public void setUp() {
		trueProp = new SimpleBooleanProperty(true);
		falseProp = new SimpleBooleanProperty(false);
		viz = new Rectangle2d(1, 2, 3, 4);
		vizProp = new SimpleObjectProperty<>(viz);
		ts = new CenteringTransform[] {
				new CenteringTransform(falseProp, falseProp, vizProp),
				new CenteringTransform(trueProp, falseProp, vizProp),
				new CenteringTransform(falseProp, trueProp, vizProp),
				new CenteringTransform(trueProp, trueProp, vizProp),
		};
	}

	@AfterEach
	public void tearDown() {
		trueProp = null;
		falseProp = null;
		vizProp = null;
		viz = null;
		ts = null;
	}
	
	@DisplayName("isInvertedAxisX")
	@Nested
	public class IsInvertedAxisX {

		@DisplayName("#1")
		@Test
		public void isInvertedAxisX_assertion1() {
			assertFalse(ts[0].isInvertedAxisX());
		}

		@DisplayName("#2")
		@Test
		public void isInvertedAxisX_assertion2() {
			assertTrue(ts[1].isInvertedAxisX());
		}

		@DisplayName("#3")
		@Test
		public void isInvertedAxisX_assertion3() {
			assertFalse(ts[2].isInvertedAxisX());
		}

		@DisplayName("#4")
		@Test
		public void isInvertedAxisX_assertion4() {
			assertTrue(ts[3].isInvertedAxisX());
		}

		@DisplayName("#5")
		@Test
		public void isInvertedAxisX_assertion5() {
			trueProp.set(false);
			assertFalse(ts[0].isInvertedAxisX());
		}

		@DisplayName("#6")
		@Test
		public void isInvertedAxisX_assertion6() {
			trueProp.set(false);
			assertFalse(ts[1].isInvertedAxisX());
		}

		@DisplayName("#7")
		@Test
		public void isInvertedAxisX_assertion7() {
			trueProp.set(false);
			assertFalse(ts[2].isInvertedAxisX());
		}

		@DisplayName("#8")
		@Test
		public void isInvertedAxisX_assertion8() {
			trueProp.set(false);
			assertFalse(ts[3].isInvertedAxisX());
		}
	}

	@DisplayName("isInvertedAxisY")
	@Nested
	public class IsInvertedAxisY {

		@DisplayName("#1")
		@Test
		public void isInvertedAxisY_assertion1() {
			assertFalse(ts[0].isInvertedAxisY());
		}

		@DisplayName("#2")
		@Test
		public void isInvertedAxisY_assertion2() {
			assertFalse(ts[1].isInvertedAxisY());
		}

		@DisplayName("#3")
		@Test
		public void isInvertedAxisY_assertion3() {
			assertTrue(ts[2].isInvertedAxisY());
		}

		@DisplayName("#4")
		@Test
		public void isInvertedAxisY_assertion4() {
			assertTrue(ts[3].isInvertedAxisY());
		}

		@DisplayName("#5")
		@Test
		public void isInvertedAxisY_assertion5() {
			trueProp.set(false);
			assertFalse(ts[0].isInvertedAxisY());
		}

		@DisplayName("#6")
		@Test
		public void isInvertedAxisY_assertion6() {
			trueProp.set(false);
			assertFalse(ts[1].isInvertedAxisY());
		}

		@DisplayName("#7")
		@Test
		public void isInvertedAxisY_assertion7() {
			trueProp.set(false);
			assertFalse(ts[2].isInvertedAxisY());
		}

		@DisplayName("#8")
		@Test
		public void isInvertedAxisY_assertion8() {
			trueProp.set(false);
			assertFalse(ts[3].isInvertedAxisY());
		}
	}

	@DisplayName("toCenterX")
	@Nested
	public class ToCenterX {

		@DisplayName("#1")
		@Test
		public void toCenterX_assertion1() {
			assertEpsilonEquals(2.5, ts[0].toCenterX(5));
		}

		@DisplayName("#2")
		@Test
		public void toCenterX_assertion2() {
			assertEpsilonEquals(-2.5, ts[1].toCenterX(5));
		}

		@DisplayName("#3")
		@Test
		public void toCenterX_assertion3() {
			assertEpsilonEquals(2.5, ts[2].toCenterX(5));
		}

		@DisplayName("#4")
		@Test
		public void toCenterX_assertion4() {
			assertEpsilonEquals(-2.5, ts[3].toCenterX(5));
		}
	}

	@DisplayName("toCenterY")
	@Nested
	public class ToCenterY {

		@DisplayName("#1")
		@Test
		public void toCenterY_assertion1() {
			assertEpsilonEquals(1, ts[0].toCenterY(5));
		}

		@DisplayName("#2")
		@Test
		public void toCenterY_assertion2() {
			assertEpsilonEquals(1, ts[1].toCenterY(5));
		}

		@DisplayName("#3")
		@Test
		public void toCenterY_assertion3() {
			assertEpsilonEquals(-1, ts[2].toCenterY(5));
		}

		@DisplayName("#4")
		@Test
		public void toCenterY_assertion4() {
			assertEpsilonEquals(-1, ts[3].toCenterY(5));
		}
	}

	@DisplayName("toGlobalX")
	@Nested
	public class ToGlobalX {

		@DisplayName("#1")
		@Test
		public void toGlobalX_assertion1() {
			assertEpsilonEquals(7.5, ts[0].toGlobalX(5));
		}

		@DisplayName("#2")
		@Test
		public void toGlobalX_assertion2() {
			assertEpsilonEquals(-2.5, ts[1].toGlobalX(5));
		}

		@DisplayName("#3")
		@Test
		public void toGlobalX_assertion3() {
			assertEpsilonEquals(7.5, ts[2].toGlobalX(5));
		}

		@DisplayName("#4")
		@Test
		public void toGlobalX_assertion4() {
			assertEpsilonEquals(-2.5, ts[3].toGlobalX(5));
		}
	}

	@DisplayName("toGlobalY")
	@Nested
	public class ToGlobalY {

		@DisplayName("#1")
		@Test
		public void toGlobalY_assertion1() {
			assertEpsilonEquals(9, ts[0].toGlobalY(5));
		}

		@DisplayName("#2")
		@Test
		public void toGlobalY_assertion2() {
			assertEpsilonEquals(9, ts[1].toGlobalY(5));
		}

		@DisplayName("#3")
		@Test
		public void toGlobalY_assertion3() {
			assertEpsilonEquals(-1, ts[2].toGlobalY(5));
		}

		@DisplayName("#4")
		@Test
		public void toGlobalY_assertion4() {
			assertEpsilonEquals(-1, ts[3].toGlobalY(5));
		}
	}
}

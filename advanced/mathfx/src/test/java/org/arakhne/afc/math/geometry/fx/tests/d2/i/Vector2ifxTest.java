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

package org.arakhne.afc.math.geometry.fx.tests.d2.i;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import org.arakhne.afc.math.geometry.base.tests.AbstractVector2DTestCase;
import org.arakhne.afc.math.geometry.fx.d2.i.Point2ifx;
import org.arakhne.afc.math.geometry.fx.d2.i.Vector2ifx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Vector2ifx")
@SuppressWarnings("all")
public class Vector2ifxTest extends AbstractVector2DTestCase<Vector2ifx, Point2ifx, Vector2ifx> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Vector2ifx createTuple(double x, double y) {
		return new Vector2ifx(x, y);
	}
	
	@Override
	public Vector2ifx createVector(double x, double y) {
		return new Vector2ifx(x, y);
	}

	@Override
	public Point2ifx createPoint(double x, double y) {
		return new Point2ifx(x, y);
	}

	@DisplayName("toOrientationVector")
	@Nested
	public class ToOrientationVector {

		@DisplayName("#1")
		@Test
		public void staticToOrientationVector_assertion1() {
			assertFpVectorEquals(1, 0, Vector2ifx.toOrientationVector(0));
		}

		@DisplayName("#2")
		@Test
		public void staticToOrientationVector_assertion2() {
			assertFpVectorEquals(-1, 0, Vector2ifx.toOrientationVector(Math.PI));
		}

		@DisplayName("#3")
		@Test
		public void staticToOrientationVector_assertion3() {
			assertFpVectorEquals(0, 1, Vector2ifx.toOrientationVector(Math.PI/2));
		}

		@DisplayName("#4")
		@Test
		public void staticToOrientationVector_assertion4() {
			assertFpVectorEquals(0, -1, Vector2ifx.toOrientationVector(-Math.PI/2));
		}

		@DisplayName("#5")
		@Test
		public void staticToOrientationVector_assertion5() {
			assertInlineParameterUsage(Vector2ifx.class, "toOrientationVector", double.class);
		}
	}
}

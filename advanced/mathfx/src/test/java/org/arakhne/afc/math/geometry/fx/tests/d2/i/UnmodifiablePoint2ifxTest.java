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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractUnmodifiablePoint2DTestCase;
import org.arakhne.afc.math.geometry.fx.d2.i.Circle2ifx;
import org.arakhne.afc.math.geometry.fx.d2.i.Point2ifx;
import org.arakhne.afc.math.geometry.fx.d2.i.Vector2ifx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("UnmodifiablePoint2ifx")
@SuppressWarnings("all")
public class UnmodifiablePoint2ifxTest extends AbstractUnmodifiablePoint2DTestCase<Point2ifx, Vector2ifx> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Point2D createTuple(double x, double y) {
		return new Point2ifx(x, y).toUnmodifiable();
	}
	
	@Override
	public Vector2ifx createVector(double x, double y) {
		return new Vector2ifx(x, y);
	}

	@Override
	public Point2ifx createPoint(double x, double y) {
		return new Point2ifx(x, y);
	}

	@DisplayName("this && Shape2D")
	@Nested
	public class OperatorAndShape2D {

		private Shape2D shape;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape = new Circle2ifx(5, 8, 5);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(createPoint(0,0).operator_and(shape));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(createPoint(11,10).operator_and(shape));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(createPoint(11,50).operator_and(shape));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(createPoint(9,12).operator_and(shape));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertTrue(createPoint(9,11).operator_and(shape));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertTrue(createPoint(8,12).operator_and(shape));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion7(CoordinateSystem2D cs) {
			setUp(cs);
			assertTrue(createPoint(3,7).operator_and(shape));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion8(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(createPoint(10,11).operator_and(shape));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_andShape2D_assertion9(CoordinateSystem2D cs) {
			setUp(cs);
			assertTrue(createPoint(9,10).operator_and(shape));
		}
	}

	@DisplayName("this .. Shape2D")
	@Nested
	public class OperatorUpToShape2D {

		private Shape2D shape;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape = new Circle2ifx(5, 8, 5);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_upToShape2D_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0f, createPoint(5,8).operator_upTo(shape));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_upToShape2D_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0f, createPoint(10,10).operator_upTo(shape));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_upToShape2D_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0f, createPoint(4,8).operator_upTo(shape));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_upToShape2D_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(4.242640687f, createPoint(0,0).operator_upTo(shape));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void operator_upToShape2D_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1f, createPoint(5,14).operator_upTo(shape));
		}
	}
}

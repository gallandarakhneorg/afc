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

package org.arakhne.afc.math.geometry.base.tests;

import static org.arakhne.afc.math.MathConstants.PI;
import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D.PowerResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractVector2DTestCase<V extends Vector2D<? super V, ? super P>, P extends Point2D<? super P, ? super V>,
	TT extends Tuple2D>
	extends AbstractTuple2DTestCase<TT> {

	public abstract V createVector(double x, double y);

	public abstract P createPoint(double x, double y);

	@DisplayName("isUnitVector(double,double)")
	@Nested
	public class IsUnitVectorDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isUnitVector(1., 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isUnitVector(1.0001, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double length = Math.sqrt(5. * 5. + 18. * 18.);
			assertTrue(Vector2D.isUnitVector(5. / length, 18. / length));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "isUnitVector", double.class, double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("isUnitVector(double,double,double)")
	@Nested
	public class IsUnitVectorDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isUnitVector(1., 0, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isUnitVector(1.0001, 0, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double length = Math.sqrt(5. * 5. + 18. * 18.);
			assertTrue(Vector2D.isUnitVector(5. / length, 18. / length, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "isUnitVector", double.class, double.class, double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("isOrthogonal(double,double,double,double)")
	@Nested
	public class IsOrthogonalDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isOrthogonal(1., 0, 1., 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isOrthogonal(1., 0, -1., 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(1., 0, 0., 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(1., 0, 0., -1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isOrthogonal(1., 0, 1., 2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(1., 0, 0, 1 + Math.ulp(1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "isOrthogonal", //$NON-NLS-1$
					double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("isOrthogonal(double,double,double,double,double)")
	@Nested
	public class IsOrthogonalDoubleDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isOrthogonal(1., 0, 1., 0, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isOrthogonal(1., 0, -1., 0, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(1., 0, 0., 1, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(1., 0, 0., -1, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isOrthogonal(1., 0, 1., 2, GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(1., 0, 0, 1 + Math.ulp(1), GeomConstants.UNIT_VECTOR_EPSILON));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "isOrthogonal", //$NON-NLS-1$
					double.class, double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("isCollinearVectors")
	@Nested
	public class IsCollinearVectors {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isCollinearVectors(1, 0, 3, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isCollinearVectors(1, 0, -3, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isCollinearVectors(1, 0, 4, 4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "isCollinearVectors", //$NON-NLS-1$
					double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("perpProduct")
	@Nested
	public class PerpProduct {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Vector2D.perpProduct(1, 0, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Vector2D.perpProduct(1, 0, 5, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(243, Vector2D.perpProduct(1, 45, -5, 18));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Vector2D.perpProduct(1, 2, 1, 2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2, Vector2D.perpProduct(1, 2, 3, 4));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4, Vector2D.perpProduct(1, 2, 1, -2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "perpProduct", //$NON-NLS-1$
					double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("dotProduct")
	@Nested
	public class DotProduct {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Vector2D.dotProduct(1, 0, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Vector2D.dotProduct(1, 0, 5, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(805, Vector2D.dotProduct(1, 45, -5, 18));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Vector2D.dotProduct(1, 2, 1, 2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11, Vector2D.dotProduct(1, 2, 3, 4));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3, Vector2D.dotProduct(1, 2, 1, -2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "dotProduct", //$NON-NLS-1$
					double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("signedAngle")
	@Nested
	public class SignedAngleStatic {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Vector2D.signedAngle(1, 0, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Vector2D.signedAngle(1, 0, 5, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-MathConstants.DEMI_PI, Vector2D.signedAngle(2, 0, 0, -3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.PI, Vector2D.signedAngle(1, 0, -1, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.29317, Vector2D.signedAngle(1, 45, -5, 18));
		}

	}

	@DisplayName("isCCW")
	@Nested
	public class IsCCW {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isCCW(1, 0, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isCCW(1, 0, 5, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Vector2D.isCCW(2, 0, 0, -3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isCCW(1, 0, -1, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isCCW(1, 45, -5, 18));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertInlineParameterUsage(Vector2D.class, "isCCW", //$NON-NLS-1$
	                double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("angleOfVector(double,double,double,double)")
	@Nested
	public class AngleOfVectorDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(
					0.,
					Vector2D.angleOfVector(0, 0, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(
					0.,
					Vector2D.angleOfVector(14, 15, 18, 15));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(
					Math.PI / 4,
					Vector2D.angleOfVector(0, 0, 5, 5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(
					Math.PI,
					Vector2D.angleOfVector(0, 0, -1, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "angleOfVector", //$NON-NLS-1$
					double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("angleOfVector(double,double)")
	@Nested
	public class AngleOfVector {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.acos(1. / Math.sqrt(5)), Vector2D.angleOfVector(1, 2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI / 2. + Math.acos(1 / Math.sqrt(5)), Vector2D.angleOfVector(-2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI / 4., Vector2D.angleOfVector(1, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Vector2D.class, "angleOfVector", //$NON-NLS-1$
					double.class, double.class);
		}

	}

	@DisplayName("dot(Vector2D)")
	@Nested
	public class DotVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2);
			this.vector2 = createVector(3, 4);
			this.vector3 = createVector(1, -2);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5,vector.dot(vector));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11,vector.dot(vector2));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3,vector.dot(vector3));
		}
	}

	@DisplayName("perp(Vector2D)")
	@Nested
	public class PerpVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2);
			this.vector2 = createVector(3, 4);
			this.vector3 = createVector(1, -2);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, vector.perp(vector));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2, vector.perp(vector2));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4, vector.perp(vector3));
		}

	}

	@DisplayName("getLength")
	@Nested
	public class GetLength {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2);
			this.vector2 = createVector(0, 0);
			this.vector3 = createVector(-1, 1);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(5),vector.getLength());
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0,vector2.getLength());
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2),vector3.getLength());
		}

	}

	@DisplayName("getLengthSquared")
	@Nested
	public class GetLengthSquared {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2);
			this.vector2 = createVector(0, 0);
			this.vector3 = createVector(Math.sqrt(2.) / 2., Math.sqrt(2.) / 2.);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(5,vector.getLengthSquared());
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(0,vector2.getLengthSquared());
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1,vector3.getLengthSquared());
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(5,vector.getLengthSquared());
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0,vector2.getLengthSquared());
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(2,vector3.getLengthSquared());
		}

	}

	@DisplayName("angle(Vector2D)")
	@Nested
	public class AngleVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2);
			this.vector2 = createVector(-2, 1);
			this.vector3 = createVector(1, 1);
			this.vector4 = createVector(1, 0);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f,vector.angle(vector2));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/4f,vector4.angle(vector3));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.angle(vector));
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.angle(vector2));
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0,vector.angle(vector));
		}
	}

	@DisplayName("signedAngle(Vector2D)")
	@Nested
	public class SignedAngleVector2D {

		@DisplayName("With double coords")
		@Nested
		public class DoubleCoords {

			private Vector2D v1;
			private Vector2D v2;
	
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble());
				this.v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble());
			}
			
			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void double_1(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v1.signedAngle(v1));
			}
			
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void double_2(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v2.signedAngle(v2));
			}
			
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void double_3(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}
			
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void double_4(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}
			
			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void double_5(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}
			
			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void double_6(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();
				if (sin < 0) {
					assertTrue(sAngle1 <= 0);
					assertTrue(sAngle2 >= 0);
				} else {
					assertTrue(sAngle1 >= 0);
					assertTrue(sAngle2 <= 0);
				}
			}

		}


		@DisplayName("With int coords")
		@Nested
		public class IntCoords {

			private Vector2D v1;
			private Vector2D v2;
	
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
				this.v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void int_1(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v1.signedAngle(v1));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void int_2(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v2.signedAngle(v2));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void int_3(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void int_4(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void int_5(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void int_6(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();
				if (sin < 0) {
					assertTrue(sAngle1 <= 0);
					assertTrue(sAngle2 >= 0);
				} else {
					assertTrue(sAngle1 >= 0);
					assertTrue(sAngle2 <= 0);
				}
			}

		}

	}

	@DisplayName("isUnitVector")
	@Nested
	public class IsUnitVector {

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertFalse(createVector(7.15161,6.7545).isUnitVector());
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertTrue(createVector(0,-1).isUnitVector());
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertTrue((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2)).isUnitVector());
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertTrue((createVector(1,0)).isUnitVector()); 
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertFalse(createVector(7.15161,6.7545).isUnitVector());
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertTrue(createVector(0,-1).isUnitVector());
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertFalse(createVector(0.72700, 0.68663).isUnitVector());
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertFalse((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2)).isUnitVector());
		}
		
		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertTrue((createVector(1,0)).isUnitVector()); 
		}

	}

	@DisplayName("isOrthogonal")
	@Nested
	public class IsOrthogonal {

		private	Vector2D v;

		@BeforeEach
		public void setUp() {
			this.v = createVector(1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(v.isOrthogonal(createVector(1., 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(v.isOrthogonal(createVector(-1., 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(v.isOrthogonal(createVector(0., 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(v.isOrthogonal(createVector(0., -1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(v.isOrthogonal(createVector(1., 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(v.isOrthogonal(createVector(0, 1 + Math.ulp(1))));
		}

	}

	@DisplayName("toUnmodifiable")
	@Nested
	public class ToUnmodifiable {

		private Vector2D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(2, 3);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeMutable(origin);
			Vector2D immutable = origin.toUnmodifiable();
			assertEpsilonEquals(origin, immutable);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeMutable(origin);
			Vector2D immutable = origin.toUnmodifiable();
			origin.add(1, 2);
			assertNotEpsilonEquals(origin, immutable);
		}

	}

	@DisplayName("clone")
	@Nested
	public class Clone {

		private Vector2D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D clone = origin.clone();
			assertNotNull(clone);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D clone = origin.clone();
			assertNotSame(origin, clone);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D clone = origin.clone();
			assertEpsilonEquals(origin.getX(), clone.getX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D clone = origin.clone();
			assertEpsilonEquals(origin.getY(), clone.getY());
		}

	}

	@DisplayName("toUnitVector")
	@Nested
	public class ToUnitVector {

		private Vector2D origin;
		private Vector2D origin2;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45);
			this.origin2 = createVector(-45, 0);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertNotNull(unitVector);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertNotSame(origin, unitVector);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertEpsilonEquals(.45511, unitVector.getX());
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertEpsilonEquals(.89043, unitVector.getY());
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertNotNull(unitVector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertNotSame(origin, unitVector);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertEpsilonEquals(0, unitVector.getX());
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D unitVector = origin.toUnitVector();
			assertEpsilonEquals(1, unitVector.getY());
		}

		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var unitVector = origin2.toUnitVector();
			assertNotNull(unitVector);
		}

		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var unitVector = origin2.toUnitVector();
			assertNotSame(origin2, unitVector);
		}

		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var unitVector = origin2.toUnitVector();
			assertEpsilonEquals(-1, unitVector.getX());
		}

		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var unitVector = origin2.toUnitVector();
			assertEpsilonEquals(0, unitVector.getY());
		}

	}

	@DisplayName("toOrthogonalVector")
	@Nested
	public class ToOrthogonalVector {

		private Vector2D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D orthoVector = origin.toOrthogonalVector();
			assertNotNull(orthoVector);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D orthoVector = origin.toOrthogonalVector();
			assertNotSame(origin, orthoVector);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D orthoVector = origin.toOrthogonalVector();
			assertEpsilonEquals(-45, orthoVector.getX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D orthoVector = origin.toOrthogonalVector();
			assertEpsilonEquals(23, orthoVector.getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			origin = createVector(1, 0);
			Vector2D orthoVector = origin.toOrthogonalVector();
			assertNotNull(orthoVector);
			assertNotSame(origin, orthoVector);
			assertEpsilonEquals(0, orthoVector.getX());
			assertEpsilonEquals(1, orthoVector.getY());
		}

	}

	@DisplayName("toColinearVector")
	@Nested
	public class ToColinearVector {

		private Vector2D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertNotNull(colVector);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertNotSame(origin, colVector);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertEpsilonEquals(8.4196, colVector.getX());
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertEpsilonEquals(16.4730, colVector.getY());
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertNotNull(colVector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertNotSame(origin, colVector);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertEpsilonEquals(8, colVector.getX());
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D colVector = origin.toColinearVector(18.5);
			assertEpsilonEquals(16, colVector.getY());
		}

	}

	public final void assertEpsilonEquals(double expected, PowerResult<?> actual) {
		if (actual == null) {
			fail("Result is null"); //$NON-NLS-1$
			return;
		}
		if (actual.isVectorial()) {
			failCompare("Not same result type", Double.toString(expected), actual.toString()); //$NON-NLS-1$
		}
		assertEpsilonEquals(expected, actual.getScalar());
	}

	public final void assertEpsilonEquals(double expectedX, double expectedY, PowerResult<?> actual) {
		if (actual == null) {
			fail("Result is null"); //$NON-NLS-1$
			return;
		}
		if (!actual.isVectorial()) {
			failCompare("Not same result type", "[" + expectedX + ";" + expectedY + "]", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					actual.toString());
		}
		Vector2D<?, ?> vector = actual.getVector();
		assert (vector != null);
		if (!isEpsilonEquals(expectedX, vector.getX())
			|| !isEpsilonEquals(expectedY, vector.getY())) {
			failCompare("Not same result type", "[" + expectedX + ";" + expectedY + "]", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					actual.toString());
		}
	}

	@DisplayName("power(double)")
	@Nested
	public class PowerDouble {

		private Vector2D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.power(6));
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.power(5));
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.power(4));
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.power(3));
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(2554, origin.power(2));
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(1));
		}

		@DisplayName("With double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1, origin.power(0));
		}

		@DisplayName("With double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(-1));
		}

		@DisplayName("With double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.power(-2));
		}

		@DisplayName("With double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./2554, 45./2554, origin.power(-3));
		}

		@DisplayName("With double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.power(-4));
		}

		@DisplayName("With double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./6522916, 45./6522916, origin.power(-5));
		}

		@DisplayName("With double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.power(-6));
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.power(6));
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.power(5));
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.power(4));
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.power(3));
		}
		
		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(2554, origin.power(2));
		}
		
		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(1));
		}
		
		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1, origin.power(0));
		}
		
		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(-1));
		}
		
		@DisplayName("With int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.power(-2));
		}
		
		@DisplayName("With int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.power(-3));
		}
		
		@DisplayName("With int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.power(-4));
		}
		
		@DisplayName("With int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.power(-5));
		}
		
		@DisplayName("With int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.power(-6));
		}

	}

	@DisplayName("this == Tuple2D")
	@Nested
	public class OperatorEqualsTuple2D {

		private Vector2D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(49, -2);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_equals(vector));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_equals(createVector(49, -3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_equals(createVector(0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_equals(createVector(49, -2)));
		}

	}

	@DisplayName("this != Tuple2D")
	@Nested
	public class OperatorNotEqualsTuple2D {

		private Vector2D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(49, -2);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_notEquals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_notEquals(vector));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_notEquals(createVector(49, -3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_notEquals(createVector(0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_notEquals(createVector(49, -2)));
		}

	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		private Vector2D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(49, -2);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.equals((Object) null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.equals((Object) vector));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.equals((Object) createVector(49, -3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.equals((Object) createVector(0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.equals((Object) createVector(49, -2)));
		}
	
	}

	@DisplayName("this .. Vector2D")
	@Nested
	public class OperatorUpToVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2);
			this.vector2 = createVector(-2, 1);
			this.vector3 = createVector(1, 1);
			this.vector4 = createVector(1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f,vector.operator_upTo(vector2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/4f,vector4.operator_upTo(vector3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0,vector.operator_upTo(vector));
		}

	}
	
	@DisplayName("this >.. Vector2D")
	@Nested
	public class OperatorGreaterThanDoubleDot {

		@DisplayName("With double coords")
		@Nested
		public class DoubleCoords {

			private Vector2D v1;
			private Vector2D v2;
	
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble());
				this.v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble());
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_1(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v1.operator_greaterThanDoubleDot(v1));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_2(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v2.operator_greaterThanDoubleDot(v2));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_3(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_4(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_5(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_6(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();
				if (sin < 0) {
					assertTrue(sAngle1 <= 0);
					assertTrue(sAngle2 >= 0);
				} else {
					assertTrue(sAngle1 >= 0);
					assertTrue(sAngle2 <= 0);
				}
			}

		}

		@DisplayName("With int coords")
		@Nested
		public class IntCoords {

			private Vector2D v1;
			private Vector2D v2;
	
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
				this.v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_1(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v1.operator_greaterThanDoubleDot(v1));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_2(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v2.operator_greaterThanDoubleDot(v2));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_3(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_4(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_5(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public final void test_6(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();
				if (sin < 0) {
					assertTrue(sAngle1 <= 0);
					assertTrue(sAngle2 >= 0);
				} else {
					assertTrue(sAngle1 >= 0);
					assertTrue(sAngle2 <= 0);
				}
			}

		}

	}

	@DisplayName("-this")
	@Nested
	public class OperatorMinus {

		private Vector2D vect;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(45, -78);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D result = vect.operator_minus();
			assertNotSame(vect, result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D result = vect.operator_minus();
			assertEpsilonEquals(-vect.getX(), result.getX());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D result = vect.operator_minus();
			assertEpsilonEquals(-vect.getY(), result.getY());
		}

	}

	@DisplayName("this * double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void test_1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var vect = createVector(45, -78);
		Vector2D result = vect.operator_multiply(5);
		assertNotSame(vect, result);
		assertEpsilonEquals(225, result.getX());
		assertEpsilonEquals(-390, result.getY());
	}

	@DisplayName("this / double")
	@Nested
	public class OperatorDivideDouble {

		private Vector2D vect;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(45, -78);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D result = vect.operator_divide(5);
			assertNotSame(vect, result);
			assertEquals(9, result.ix());
			assertEquals(-16, result.iy());
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D result = vect.operator_divide(5);
			assertNotSame(vect, result);
			assertEpsilonEquals(9, result.getX());
			assertEpsilonEquals(-15.6, result.getY());
		}

	}

	@DisplayName("this ?: Vector2D")
	@Nested
	public class OperatorElvisVector2D {

		private Vector2D orig1;
		private Vector2D orig2;
		private Vector2D param;

		@BeforeEach
		public void setUp() {
			this.orig1 = createVector(45, -78);
			this.orig2 = createVector(0, 0);
			this.param = createVector(-5, -1.4);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(null);
			assertSame(orig1, result);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(orig1);
			assertSame(orig1, result);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(param);
			assertSame(orig1, result);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(null);
			assertNull(result);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(orig2);
			assertSame(orig2, result);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(param);
			assertSame(param, result);
		}

	}

	@DisplayName("this - Vector2D")
	@Nested
	public class OperatorMinusVector2D {

		private Vector2D vect;
		private Vector2D vect2;
		private Vector2D vector;
		private Vector2D vector2;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(0, 0);
			this.vect2 = createVector(-1, 0);
			this.vector = createVector(-1.2, -1.2);
			this.vector2 = createVector(-2., -1.5);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D r = vect.operator_minus(vector);
			assertFpVectorEquals(1.2, 1.2, r);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var r = vect2.operator_minus(vector2);
			assertFpVectorEquals(1.0, 1.5, r);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void in_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var r = vect.operator_minus(vector);
			assertIntVectorEquals(1, 1, r);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void in_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var r = vect2.operator_minus(vector2);
			assertIntVectorEquals(1, 1, r);
		}

	}

	@DisplayName("this + Vector2D")
	@Nested
	public class OperatorPlusVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0, 0);
			this.vector2 = createVector(-1, 0);
			this.vector3 = createVector(1.2, 1.2);
			this.vector4 = createVector(2., 1.5);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector2D r = vector3.operator_plus(vector3);
			assertFpVectorEquals(2.4, 2.4, r);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var r = vector4.operator_plus(vector4);
			assertFpVectorEquals(4., 3.0, r);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D r = vector.operator_plus(vector3);
			assertIntVectorEquals(1, 1, r);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var r = vector2.operator_plus(vector4);
			assertIntVectorEquals(1, 2, r);
		}

	}

	@DisplayName("this + double")
	@Nested
	public class OperatorPlusDouble {

		private Vector2D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1,2);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeTrue(isIntCoordinates());
	        assertIntVectorEquals(49, 50, vector.operator_plus(48.2));
	    }
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeFalse(isIntCoordinates());
	        assertFpVectorEquals(49.2, 50.2, vector.operator_plus(48.2));
	    }

	}

	@DisplayName("this - double")
	@Nested
	public class OperatorMinusDouble {

		private Vector2D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1,2);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeTrue(isIntCoordinates());
	        assertIntVectorEquals(-47, -46, vector.operator_minus(48.2));
	    }
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeFalse(isIntCoordinates());
	        assertFpVectorEquals(-47.2, -46.2, vector.operator_minus(48.2));
	    }

	}

	@DisplayName("this - Point2D")
	@Nested
	public class OperatorMinusPoint2D {

		private Vector2D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1,2);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D vector = createVector(1,2);
	        Point2D point = createPoint(3, 4);
	        assertFpPointEquals(-2, -2, vector.operator_minus(point));
	    }

	}

	@DisplayName("this ^ Vector2D")
	@Nested
	public class OperatorPowerVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1,2);
			this.vector2 = createVector(3,4);
			this.vector3 = createVector(1,-2);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, vector.operator_power(vector));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2, vector.operator_power(vector2));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4, vector.operator_power(vector3));
		}

	}


	@DisplayName("this ^ int")
	@Nested
	public class OperatorPowerInt {

		private Vector2D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.operator_power(6));
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.operator_power(5));
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.operator_power(4));
		}
		
		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.operator_power(3));
		}
		
		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(2554, origin.operator_power(2));
		}
		
		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(1));
		}
		
		@DisplayName("With double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1, origin.operator_power(0));
		}
		
		@DisplayName("With double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(-1));
		}
		
		@DisplayName("With double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.operator_power(-2));
		}
		
		@DisplayName("With double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./2554, 45./2554, origin.operator_power(-3));
		}
		
		@DisplayName("With double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.operator_power(-4));
		}
		
		@DisplayName("With double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./6522916, 45./6522916, origin.operator_power(-5));
		}
		
		@DisplayName("With double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.operator_power(-6));
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.operator_power(6));
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.operator_power(5));
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.operator_power(3));
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(2554, origin.operator_power(2));
		}
		
		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(1));
		}
		
		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1, origin.operator_power(0));
		}
		
		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(-1));
		}
		
		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.operator_power(-2));
		}
		
		@DisplayName("With int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.operator_power(-3));
		}
		
		@DisplayName("With int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.operator_power(-4));
		}
		
		@DisplayName("With int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.operator_power(-5));
		}
		
		@DisplayName("With int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.operator_power(-6));
		}

		@DisplayName("With int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.operator_power(4));
		}
		
	}

	@DisplayName("this + Point2D")
	@Nested
	public class OperatorPlusPoint2D {

		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = vector1.operator_plus(point);
			assertFpPointEquals(1, 2, r);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = vector2.operator_plus(point);
			assertFpPointEquals(2, 4, r);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = vector3.operator_plus(point);
			assertFpPointEquals(2, -3, r);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = vector1.operator_plus(point2);
			assertFpPointEquals(3, 0, r);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = vector2.operator_plus(point2);
			assertFpPointEquals(4, 2, r);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = vector3.operator_plus(point2);
			assertFpPointEquals(4, -5, r);
		}

	}

	@DisplayName("add(Vector2D,Vector2D)")
	@Nested
	public class AddVector2DVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;
		private Vector2D vector5;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0, 0);
			this.vector2 = createVector(-1, 0);
			this.vector3 = createVector(1.2, 1.2);
			this.vector4 = createVector(2.0, 1.5);
			this.vector5 = createVector(0.0, 0.0);
			assumeMutable(this.vector5);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector5.add(vector3,vector);
			assertFpVectorEquals(1.2, 1.2, vector5);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector5.add(vector4,vector2);
			assertFpVectorEquals(1.0, 1.5, vector5); 
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector5.add(vector3,vector);
			assertIntVectorEquals(1, 1, vector5);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector5.add(vector4,vector2);
			assertIntVectorEquals(1, 2, vector5); 
		}

	}

	@DisplayName("add(Vector2D)")
	@Nested
	public class AddVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0, 0);
			this.vector2 = createVector(-1, 0);
			this.vector3 = createVector(1.2, 1.2);
			this.vector4 = createVector(2.0, 1.5);
			assumeMutable(this.vector);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.add(vector3);
			assertFpVectorEquals(1.2, 1.2, vector);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector2.add(vector4);
			assertFpVectorEquals(1., 1.5, vector2);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.add(vector3);
			assertIntVectorEquals(1, 1, vector);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector2.add(vector4);
			assertIntVectorEquals(1, 2, vector2);
		}

	}

	@DisplayName("scaleAdd(int,Vector2D,Vector2D)")
	@Nested
	public class ScaleAddIntVector2DVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(-1, 0);
			this.vector2 = createVector(1.2, 1.2);
			this.vector3 = createVector(0, 0);
			assumeMutable(this.vector3);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(0,vector2,vector);
			assertFpVectorEquals(-1, 0, vector3);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(1,vector2,vector);
			assertFpVectorEquals(0.2, 1.2, vector3);
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(-1,vector2,vector);
			assertFpVectorEquals(-2.2, -1.2, vector3);
		}
		
		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(10,vector2,vector);
			assertFpVectorEquals(11, 12, vector3);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(0,vector2,vector);
			assertFpVectorEquals(-1, 0, vector3);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(1,vector2,vector);
			assertFpVectorEquals(0, 1, vector3);
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(-1,vector2,vector);
			assertFpVectorEquals(-2, -1, vector3);
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(10,vector2,vector);
			assertFpVectorEquals(9, 10, vector3);
		}

	}

	@DisplayName("scaleAdd(double,Vector2D,Vector2D)")
	@Nested
	public class ScaleAddDoubleVector2DVector2D {

		private Vector2D vect;
		private Vector2D vector;
		private Vector2D newPoint;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(1, 0);
			this.vector = createVector(-1, 1);
			this.newPoint = createVector(0, 0);
			assumeMutable(this.newPoint);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(0.0, vector, vect);
			assertFpVectorEquals(1, 0, newPoint);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(1.5,vector, vect);
			assertFpVectorEquals(-0.5, 1.5, newPoint);
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(-1.5,vector, vect);
			assertFpVectorEquals(2.5, -1.5, newPoint);
		}
		
		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(0.1,vector, vect);
			assertFpVectorEquals(0.9, 0.1, newPoint);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(0.0, vector, vect);
			assertIntVectorEquals(1, 0, newPoint);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(1.5,vector,vect);
			assertIntVectorEquals(0, 2, newPoint);
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(-1.5,vector,vect);
			assertIntVectorEquals(3, -1, newPoint);
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(0.1,vector,vect);
			assertIntVectorEquals(1, 0, newPoint);
		}

	}

	@DisplayName("scaleAdd(int,Vector2D)")
	@Nested
	public class ScaleAddIntVector2D {

		private Vector2D vector;
		private Vector2D newPoint;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 0);
			this.newPoint = createVector(1, 1);
			assumeMutable(this.newPoint);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			newPoint.scaleAdd(0,vector);
			assertFpVectorEquals(1, 0, newPoint);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			newPoint.scaleAdd(1,vector);
			assertFpVectorEquals(2, 1, newPoint);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			newPoint.scaleAdd(-10,vector);
			assertFpVectorEquals(-9, -10, newPoint);
		}

	}

	@DisplayName("scaleAdd(double,Vector2D)")
	@Nested
	public class ScaleAddDoubleVector2D {

		private Vector2D vector;
		private Vector2D newPoint;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 0);
			this.newPoint = createVector(1, 1);
			assumeMutable(this.newPoint);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(0.5,vector);
			assertFpVectorEquals(1.5, 0.5, newPoint);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(1.2,vector);
			assertFpVectorEquals(2.2, 1.2, newPoint);
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(-10.,vector);
			assertFpVectorEquals(-9, -10, newPoint);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(0.5,vector);
			assertIntVectorEquals(1, 0, newPoint);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(1.2,vector);
			assertIntVectorEquals(2, 0, newPoint);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(-10.,vector);
			assertIntVectorEquals(-19, 0, newPoint);
		}

	}

	@DisplayName("sub(Vector2D,Vector2D)")
	@Nested
	public class SubVector2DVector2D {

		private Vector2D vect;
		private Vector2D vect2;
		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D newPoint;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(0, 0);
			this.vect2 = createVector(1, 0);
			this.vector = createVector(-1.2, -1.2);
			this.vector2 = createVector(2.0, 1.5);
			this.newPoint = createVector(0.0, 0.0);
			assumeMutable(this.newPoint);
		}
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(vect,vector);
			assertFpVectorEquals(1.2, 1.2, newPoint);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(vect2,vector2);
			assertFpVectorEquals(-1.0, -1.5, newPoint); 
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D vect = createVector(0, 0);
			newPoint.sub(vect,vector);
			assertIntVectorEquals(1, 1, newPoint);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.sub(vect2,vector2);
			assertIntVectorEquals(-1, -2, newPoint); 
		}

	}

	@DisplayName("sub(Point2D,Point2D)")
	@Nested
	public class SubPoint2DPoint2D {

		private Point2D point;
		private Point2D point2;
		private Point2D vector;
		private Point2D vector2;
		private Vector2D newPoint;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(1, 0);
			this.vector = createPoint(-1.2, -1.2);
			this.vector2 = createPoint(2.0, 1.5);
			this.newPoint = createVector(0.0, 0.0);
			assumeMutable(this.newPoint);
		}
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(point,vector);
			assertFpVectorEquals(1.2, 1.2, newPoint);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(point2,vector2);
			assertFpVectorEquals(-1.0, -1.5, newPoint); 
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.sub(point,vector);
			assertIntVectorEquals(1, 1, newPoint);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.sub(point2,vector2);
			assertIntVectorEquals(-1, -2, newPoint); 
		}

	}

	@DisplayName("sub(Vector2D)")
	@Nested
	public class SubVector2D {

		private Vector2D vect;
		private Vector2D vect2;
		private Vector2D vector;
		private Vector2D vector2;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(0, 0);
			this.vect2 = createVector(-1, 0);
			this.vector = createVector(-1.2, -1.2);
			this.vector2 = createVector(-2.0, -1.5);
			assumeMutable(this.vect);
		}
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect.sub(vector);
			assertFpVectorEquals(1.2, 1.2, vect);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect2.sub(vector2);
			assertFpVectorEquals(1.0, 1.5, vect2);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vect.sub(vector);
			assertIntVectorEquals(1, 1, vect);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vect2.sub(vector2);
			assertIntVectorEquals(1, 1, vect2);
		}

	}

	@DisplayName("makeOrthogonal")
	@Nested
	public class MakeOrthogonal {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1,2);
			this.vector2 = createVector(0,0);
			this.vector3 = createVector(1,1);
			this.vector4 = createVector(1,0);
			assumeMutable(this.vector);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			vector.makeOrthogonal();
			assertFpVectorEquals(-2, 1, vector);
			assertFpVectorNotEquals(2, -1, vector);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			vector2.makeOrthogonal();
			assertFpVectorEquals(0, 0, vector2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			vector3.makeOrthogonal();
			assertFpVectorEquals(-1, 1, vector3);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			vector4.makeOrthogonal();
			assertFpVectorEquals(0, 1, vector4);
		}

	}
	
	@DisplayName("normalize")
	@Nested
	public class Normalize {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1,2);
			this.vector2 = createVector(0,0);
			this.vector3 = createVector(-1,1);
			this.vector4 = createVector(0,-5);
			assumeMutable(this.vector);
		}
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.normalize();
			assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), vector);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector2.normalize();
			assertZero(vector2.getX());
			assertZero(vector2.getY());
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.normalize();
			assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), vector3);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.normalize();
			assertIntVectorEquals(0, 1, vector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector2.normalize();
			assertIntVectorEquals(0, 0, vector2);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.normalize();
			assertIntVectorEquals(-1, 1, vector3);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector4.normalize();
			assertIntVectorEquals(0, -1, vector4);
		}

	}

	@DisplayName("normalize(Vector2D)")
	@Nested
	public class NormalizeVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0,0);
			this.vector2 = createVector(0,0);
			this.vector3 = createVector(0,0);
			this.vector4 = createVector(0,0);
			assumeMutable(this.vector);
		}
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.normalize(createVector(1,2));
			assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), vector);
		}
	
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector2.normalize(createVector(0,0));
			assertZero(vector2.getX());
			assertZero(vector2.getY());
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.normalize(createVector(-1,1));
			assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), vector3);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.normalize(createVector(1,2));
			assertIntVectorEquals(0, 1, vector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector2.normalize(createVector(0,0));
			assertIntVectorEquals(0, 0, vector2);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D vector3 = createVector(0,0);
			vector3.normalize(createVector(-1,1));
			assertIntVectorEquals(-1, 1, vector3);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector2D vector4 = createVector(0,0);
			vector4.normalize(createVector(0,-5));
			assertIntVectorEquals(0, -1, vector4);
		}

	}

	@DisplayName("turn(double)")
	@Nested
	public class TurnDouble {

		private Vector2D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(2, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(2. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(3. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(4. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector = createVector(2, 0);
			vector.turn(-MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(-6. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("With double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(-7. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("With double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(-8. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("With double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.turn(-MathConstants.DEMI_PI/3);
			assertFpVectorEquals(1.732, -1, vector); 
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector = createVector(2, 0);
			vector.turn(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.turn(MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.turn(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.turn(MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}

		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector = createVector(2, 0);
			vector.turn(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}

		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.turn(-MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}

		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.turn(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}

		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.turn(-MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}

		@DisplayName("With int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector = createVector(2, 0);
			vector.turn(-MathConstants.DEMI_PI/3);
			assertIntVectorEquals(2, -1, vector); 
		}

	}

	@DisplayName("turn(double,Vector2D)")
	@Nested
	public class TurnDoubleVector2D {

		private Vector2D vector1;
		private Vector2D vector2;
		
		@BeforeEach
		public void setUp() {
			this.vector1 = createVector(Double.NaN, Double.NaN);
			this.vector2 = createVector(2, 0);
			assumeMutable(this.vector1);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector1.turn(MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, 2, vector1); 
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector1.turn(-MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, -2, vector1); 
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector1.turn(-MathConstants.DEMI_PI/3, vector2);
			assertFpVectorEquals(1.732, -1, vector1); 
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector1.turn(MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, 2, vector1); 
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector1.turn(-MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, -2, vector1); 
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector1.turn(-MathConstants.DEMI_PI/3, vector2);
			assertIntVectorEquals(2, -1, vector1); 
		}

	}

	@DisplayName("turnLeft(double)")
	@Nested
	public class TurnLeftDouble {

		private Vector2D vector;
		
		@BeforeEach
		public void setUp() {
			this.vector = createVector(2, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(2. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(3. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(4. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-5. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(-6. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(-7. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(-8. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-MathConstants.DEMI_PI/3);
			assertFpVectorEquals(1.732, -1, vector); 
		}
	
		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnLeft(MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}
		
		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(2. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}
		
		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(3. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}
		
		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(4. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}
		
		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-5. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}
		
		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(-6. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}
		
		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(-7. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}
		
		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(-8. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}
		
		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-MathConstants.DEMI_PI/3);
			assertFpVectorEquals(1.732, 1, vector); 
		}

		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}

		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}

		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}

		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}

		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}

		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-MathConstants.DEMI_PI/3);
			assertIntVectorEquals(2, -1, vector); 
		}

		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}

		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}

		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}

		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}

		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}

		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}

		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnLeft(-MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}

		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnLeft(-MathConstants.DEMI_PI/3);
			assertIntVectorEquals(2, 1, vector); 
		}

	}

	@DisplayName("turnRight(double)")
	@Nested
	public class TurnRightDouble {

		private Vector2D vector;
		
		@BeforeEach
		public void setUp() {
			this.vector = createVector(2, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(2. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(3. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(4. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector = createVector(2, 0);
			vector.turnRight(-5. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(-6. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(-7. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(-8. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(-MathConstants.DEMI_PI/3);
			assertFpVectorEquals(1.732, 1, vector); 
		}

		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(2. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(3. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(4. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnRight(-5. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, -2, vector); 
		}

		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(-6. * MathConstants.DEMI_PI);
			assertFpVectorEquals(-2, 0, vector); 
		}

		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(-7. * MathConstants.DEMI_PI);
			assertFpVectorEquals(0, 2, vector); 
		}

		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(-8. * MathConstants.DEMI_PI);
			assertFpVectorEquals(2, 0, vector); 
		}

		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnRight(-MathConstants.DEMI_PI/3);
			assertFpVectorEquals(1.732, -1, vector); 
		}
	
		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}
		
		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}
		
		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}
		
		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}
		
		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector = createVector(2, 0);
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}
		
		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}
		
		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}
		
		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}
		
		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector = createVector(2, 0);
			vector.turnRight(-MathConstants.DEMI_PI/3);
			assertIntVectorEquals(2, 1, vector); 
		}
	
		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}
		
		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}
		
		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}
		
		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}
		
		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, -2, vector); 
		}
		
		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(-2, 0, vector); 
		}
		
		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(0, 2, vector); 
		}
		
		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector.turnRight(-MathConstants.DEMI_PI);
			assertIntVectorEquals(2, 0, vector); 
		}
		
		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector = createVector(2, 0);
			vector.turnRight(-MathConstants.DEMI_PI/3);
			assertIntVectorEquals(2, -1, vector); 
		}

	}

	@DisplayName("turnLeft(double,Vector2D)")
	@Nested
	public class TurnLeftDoubleVector2D {

		private Vector2D vector1;
		private Vector2D vector2;
		
		@BeforeEach
		public void setUp() {
			this.vector1 = createVector(Double.NaN, Double.NaN);
			this.vector2 = createVector(2, 0);
			assumeMutable(this.vector1);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnLeft(MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, 2, vector1); 
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, -2, vector1); 
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
			assertFpVectorEquals(1.732, -1, vector1); 
		}

		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnLeft(MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, -2, vector1); 
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, 2, vector1); 
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
			assertFpVectorEquals(1.732, 1, vector1); 
		}
	
		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnLeft(MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, 2, vector1); 
		}
		
		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, -2, vector1); 
		}
		
		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
			assertIntVectorEquals(2, -1, vector1); 
		}
	
		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnLeft(MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, -2, vector1); 
		}
		
		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, 2, vector1); 
		}
		
		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
			assertIntVectorEquals(2, 1, vector1); 
		}

	}

	@DisplayName("turnRight(double,Vector2D)")
	@Nested
	public class TurnRightDoubleVector2D {

		private Vector2D vector1;
		private Vector2D vector2;
		
		@BeforeEach
		public void setUp() {
			this.vector1 = createVector(Double.NaN, Double.NaN);
			this.vector2 = createVector(2, 0);
			assumeMutable(this.vector1);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnRight(MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, -2, vector1); 
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnRight(-MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, 2, vector1); 
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
			assertFpVectorEquals(1.732, 1, vector1); 
		}

		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnRight(MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, 2, vector1); 
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnRight(-MathConstants.DEMI_PI, vector2);
			assertFpVectorEquals(0, -2, vector1); 
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
			assertFpVectorEquals(1.732, -1, vector1); 
		}
	
		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnRight(MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, -2, vector1); 
		}
		
		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnRight(-MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, 2, vector1); 
		}
		
		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
			vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
			assertIntVectorEquals(2, 1, vector1); 
		}
	
		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnRight(MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, 2, vector1); 
		}
		
		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnRight(-MathConstants.DEMI_PI, vector2);
			assertIntVectorEquals(0, -2, vector1); 
		}
		
		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
			assertIntVectorEquals(2, -1, vector1); 
		}

	}

	@DisplayName("setLength")
	@Nested
	public class SetLength {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D oldVector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(getRandom().nextDouble(), getRandom().nextDouble());
			this.vector2 = createVector(0,0);
			this.oldVector = (Vector2D) vector.clone();
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			double newLength = getRandom().nextDouble();
			vector.setLength(newLength);
			assertEpsilonEquals(vector.angle(oldVector), 0);
			assertEpsilonEquals(vector.getLength()*oldVector.getLength()/newLength,oldVector.getLength());
		}
	
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			double newLength = getRandom().nextDouble();
			vector2.setLength(newLength);
			assertFpVectorEquals(newLength,0, vector2);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			double newLength = getRandom().nextDouble();
			vector.setLength(newLength);
			assertEpsilonEquals(vector.angle(oldVector), 0);
			assertEpsilonEquals(vector.getLength()*oldVector.getLength()/newLength,oldVector.getLength());
		}
	
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			double newLength = getRandom().nextDouble();
			vector2.setLength(newLength);
			assertFpVectorEquals(newLength,0, vector2);
		}

	}

	@DisplayName("this += Vector2D")
	@Nested
	public class OperatorAddVector2D {

		private Vector2D vector;
		private Vector2D vector2;
		private Vector2D vector3;
		private Vector2D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0,0);
			this.vector2 = createVector(-1,0);
			this.vector3 = createVector(1.2,1.2);
			this.vector4 = createVector(2.0,1.5);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coordinates #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.operator_add(vector3);
			assertFpVectorEquals(1.2, 1.2, vector);
		}

		@DisplayName("With double coordinates #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector2.add(vector4);
			assertFpVectorEquals(1., 1.5, vector2);
		}
	
		@DisplayName("With int coordinates #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.operator_add(vector3);
			assertIntVectorEquals(1, 1, vector);
		}
		
		@DisplayName("With int coordinates #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector2.operator_add(vector4);
			assertIntVectorEquals(1, 2, vector2);
		}

	}

	@DisplayName("this -= Vector2D")
	@Nested
	public class OperatorRemoveVector2D {

		private Vector2D vect;
		private Vector2D vect2;
		private Vector2D vector;
		private Vector2D vector2;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(0,0);
			this.vect2 = createVector(-1,0);
			this.vector = createVector(1.2,1.2);
			this.vector2 = createVector(-2.0,-1.5);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coordinates #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect.operator_remove(vector);
			assertFpVectorEquals(-1.2, -1.2, vect);
		}

		@DisplayName("With double coordinates #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect2.operator_remove(vector2);
			assertFpVectorEquals(1.0, 1.5, vect2);
		}

		@DisplayName("With int coordinates #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(isIntCoordinates());
			vect.operator_remove(vector);
			assertIntVectorEquals(1, 1, vect);
		}

		@DisplayName("With int coordinates #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assumeTrue(isIntCoordinates());
			vect2.operator_remove(vector2);
			assertIntVectorEquals(1, 1, vect2);
		}

	}

}

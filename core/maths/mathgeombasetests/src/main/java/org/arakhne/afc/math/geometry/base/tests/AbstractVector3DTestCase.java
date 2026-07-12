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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D.PowerResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractVector3DTestCase<V extends Vector3D<? super V, ? super P, ? super Q>,
	P extends Point3D<? super P, ? super V, ? super Q>,
	Q extends Quaternion<? super P, ? super V, ? super Q>,
	TT extends Tuple3D>
	extends AbstractTuple3DTestCase<TT> {

	/** Assert that the result of the power operator is equal to an expected value.
	 *
	 * @param expected the expected value.
	 * @param actual the poer computation result.
	 */
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

	/** Assert that the result of the power operator is equal to an expected value.
	 *
	 * @param expectedX x coordinate of the expected value.
	 * @param expectedY y coordinate of the expected value.
	 * @param actual the poer computation result.
	 */
	public final void assertEpsilonEquals(double expectedX, double expectedY, PowerResult<?> actual) {
		if (actual == null) {
			fail("Result is null"); //$NON-NLS-1$
			return;
		}
		if (!actual.isVectorial()) {
			failCompare("Not same result type", "[" + expectedX + ";" + expectedY + "]", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					actual.toString());
		}
		Vector3D<?, ?, ?> vector = actual.getVector();
		assert (vector != null);
		if (!isEpsilonEquals(expectedX, vector.getX())
			|| !isEpsilonEquals(expectedY, vector.getY())) {
			failCompare("Not same result type", "[" + expectedX + ";" + expectedY + "]", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					actual.toString());
		}
	}

	public abstract V createVector(double x, double y, double z);

	public abstract P createPoint(double x, double y, double z);

	@DisplayName("isUnitVector(double,double,double)")
	@Nested
	public class IsUnitVectorStatic {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertTrue(Vector3D.isUnitVector(1., 0 , 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertFalse(Vector3D.isUnitVector(1.0001, 0, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			double length = Math.sqrt(5. * 5. + 18. * 18. + 3. * 3.);
			assertTrue(Vector3D.isUnitVector(5. / length, 18. / length, 3. / length));
		}

	}

	@DisplayName("isCollinearVectors(double,double,double,double,double,double)")
	@Nested
	public class IsCollinearVectors {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertTrue(Vector3D.isColinearVectors(1, 0, 0, 3, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertTrue(Vector3D.isColinearVectors(1, 0, 0, -3, 0, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertFalse(Vector3D.isColinearVectors(1, 0, 0, 4, 4, 0));
		}

	}

	@DisplayName("perpProduct(double,double,double,double,double,double)")
	@Nested
	public class PerpProduct {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertEpsilonEquals(0, Vector3D.perpProduct(1, 0, 0, 1, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertEpsilonEquals(0, Vector3D.perpProduct(1, 0, 0, 5, 0, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertEpsilonEquals(243, Vector3D.perpProduct(1, 45, 0, -5, 18, 0));
		}

		@DisplayName("#4")
		@Test
		public final void test_4() {
			assertEpsilonEquals(0, Vector3D.perpProduct(1, 2, 0, 1, 2, 0));
		}

		@DisplayName("#5")
		@Test
		public final void test_5() {
			assertEpsilonEquals(-2, Vector3D.perpProduct(1, 2, 0, 3, 4, 0));
		}

		@DisplayName("#6")
		@Test
		public final void test_6() {
			assertEpsilonEquals(-4, Vector3D.perpProduct(1, 2, 0, 1, -2, 0));
		}

	}

	@DisplayName("dotProduct(double,double,double,double,double,double)")
	@Nested
	public class DotProduct {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertEpsilonEquals(1, Vector3D.dotProduct(1, 0, 0, 1, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertEpsilonEquals(5, Vector3D.dotProduct(1, 0, 0, 5, 0, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertEpsilonEquals(805, Vector3D.dotProduct(1, 45, 0, -5, 18, 0));
		}

		@DisplayName("#4")
		@Test
		public final void test_4() {
			assertEpsilonEquals(5, Vector3D.dotProduct(1, 2, 0, 1, 2, 0));
		}

		@DisplayName("#5")
		@Test
		public final void test_5() {
			assertEpsilonEquals(11, Vector3D.dotProduct(1, 2, 0, 3, 4, 0));
		}

		@DisplayName("#6")
		@Test
		public final void test_6() {
			assertEpsilonEquals(-3, Vector3D.dotProduct(1, 2, 0, 1, -2, 0));
		}

	}

	@DisplayName("signedAngle(double,double,double,double,double,double)")
	@Nested
	public class SignedAngleStatic {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertEpsilonEquals(0, Vector3D.signedAngle(1, 0, 0, 1, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertEpsilonEquals(0, Vector3D.signedAngle(1, 0, 0, 5, 0, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertEpsilonEquals(-MathConstants.DEMI_PI, Vector3D.signedAngle(2, 0, 0, 0, -3, 0));
		}

		@DisplayName("#4")
		@Test
		public final void test_4() {
			assertEpsilonEquals(Math.PI, Vector3D.signedAngle(1, 0, 0, -1, 0, 0));
		}

		@DisplayName("#5")
		@Test
		public final void test_5() {
			assertEpsilonEquals(0.29317, Vector3D.signedAngle(1, 45, 0, -5, 18, 0));
		}
		
		@DisplayName("#6")
		@Test
		public final void test_6() {
			double v1x = getRandom().nextDouble();
			double v1y = getRandom().nextDouble();
			double v1z = getRandom().nextDouble();
			double v2x = getRandom().nextDouble();
			double v2y = getRandom().nextDouble();
			double v2z = getRandom().nextDouble();
			assertEpsilonEquals(
					0.,
					Vector3D.signedAngle(v1x, v1y, v1z, v1x, v1y, v1z));
		}

		@DisplayName("#7")
		@Test
		public final void test_7() {
			double v1x = getRandom().nextDouble();
			double v1y = getRandom().nextDouble();
			double v1z = getRandom().nextDouble();
			double v2x = getRandom().nextDouble();
			double v2y = getRandom().nextDouble();
			double v2z = getRandom().nextDouble();
			assertEpsilonEquals(
					0.,
					Vector3D.signedAngle(v2x, v2y, v2z, v2x, v2y, v2z));
		}

		@DisplayName("#8")
		@Test
		public final void test_8() {
			double v1x = getRandom().nextDouble();
			double v1y = getRandom().nextDouble();
			double v1z = getRandom().nextDouble();
			double v2x = getRandom().nextDouble();
			double v2y = getRandom().nextDouble();
			double v2z = getRandom().nextDouble();

			double sAngle1 = Vector3D.signedAngle(v1x, v1y, v1z, v2x, v2y, v2z);
			double sAngle2 = Vector3D.signedAngle(v2x, v2y, v2z, v1x, v1y, v1z);

			assertEpsilonEquals(-sAngle1, sAngle2);
		}

	}

	@DisplayName("dot(Vector3D)")
	@Nested
	public class DotVector3D {

		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2, 0);
			this.vector2 = createVector(3, 4, 0);
			this.vector3 = createVector(1, -2, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5,vector.dot(vector));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11,vector.dot(vector2));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3,vector.dot(vector3));
		}

	}

	@DisplayName("perp(Vector3D)")
	@Nested
	public class PerpVector3D {

		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2, 0);
			this.vector2 = createVector(3, 4, 0);
			this.vector3 = createVector(1, -2, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, vector.perp(vector));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2, vector.perp(vector2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4, vector.perp(vector3));
		}

	}

	@DisplayName("getLength")
	@Nested
	public class GetLength {

		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2, 0);
			this.vector2 = createVector(0, 0, 0);
			this.vector3 = createVector(-1, 1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(5),vector.getLength());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0,vector2.getLength());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2),vector3.getLength());
		}

	}

	@DisplayName("getLengthSquared")
	@Nested
	public class GetLengthSquared {

		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2, 0);
			this.vector2 = createVector(0, 0, 0);
			this.vector3 = createVector(Math.sqrt(2.) / 2., Math.sqrt(2.) / 2., 0);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(5,vector.getLengthSquared());
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(0,vector2.getLengthSquared());
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1,vector3.getLengthSquared());
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(5,vector.getLengthSquared());
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0,vector2.getLengthSquared());
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(2,vector3.getLengthSquared());
		}

	}

	@DisplayName("angle(Vector3D)")
	@Nested
	public class AngleVector3D {

		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;
		private Vector3D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2, 0);
			this.vector2 = createVector(-2, 1, 0);
			this.vector3 = createVector(1, 1, 0);
			this.vector4 = createVector(1, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f,vector.angle(vector2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/4f,vector4.angle(vector3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.angle(vector));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.angle(vector2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0,vector.angle(vector));
		}

	}

	@DisplayName("signedAngle(Vector3D)")
	@Nested
	public class SignedAngleVector3D {

		@DisplayName("With double coords")
		@Nested
		public class DoubleCoords {

			private Vector3D v1;
			private Vector3D v2;
			
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
				this.v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
			}
	
			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void double_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v1.signedAngle(v1));
			}
	
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void double_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v2.signedAngle(v2));
			}
	
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void double_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}
	
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void double_4(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}
	
			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void double_5(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}
		}


		@DisplayName("With int coords")
		@Nested
		public class IntCoords {

			private Vector3D v1;
			private Vector3D v2;
			
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);;
				this.v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void int_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v1.signedAngle(v1));
			}
	
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void int_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v2.signedAngle(v2));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void int_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void int_4(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void int_5(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.signedAngle(v2);
				double sAngle2 = v2.signedAngle(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void int_6(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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

		@DisplayName("With double coords")
		@Nested
		public class DoubleCoords {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertFalse(createVector(7.15161,6.7545, 0).isUnitVector());
			}
	
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertTrue(createVector(0,-1, 0).isUnitVector());
			}
	
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertTrue((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2,0)).isUnitVector());
			}
	
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertTrue((createVector(1,0,0)).isUnitVector()); 
			}

		}
	
		@DisplayName("With int coords")
		@Nested
		public class IntCoords {
		
			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertFalse(createVector(7.15161,6.7545, 0).isUnitVector());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertTrue(createVector(0,-1, 0).isUnitVector());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertFalse(createVector(0.72700, 0.68663, 0).isUnitVector());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertFalse((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2, Math.sqrt(2)/2)).isUnitVector());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertTrue((createVector(1,0,0)).isUnitVector()); 
			}

		}

	}

	@DisplayName("toUnmodifiable")
	@Nested
	public class ToUnmodifiable {

		private Vector3D origin;
		
		@BeforeEach
		public void setUp() {
			this.origin = createVector(2, 3, 0);
			assumeMutable(origin);
		}

		@DisplayName("With exception")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toUnmodifiable_exception(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				Vector3D immutable = origin.toUnmodifiable();
				assertEpsilonEquals(origin, immutable);
				immutable.add(1, 2, 0);
			});
		}
	
		@DisplayName("Change in origin")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toUnmodifiable_changeInOrigin(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D immutable = origin.toUnmodifiable();
			assertEpsilonEquals(origin, immutable);
			origin.add(1, 2, 0);
			assertEpsilonEquals(createVector(2, 3, 0), immutable);
		}

	}

	@DisplayName("clone")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void testCloneVector(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D origin = createVector(23, 45, 0);
		Tuple3D clone = origin.clone();
		assertNotNull(clone);
		assertNotSame(origin, clone);
		assertEpsilonEquals(origin.getX(), clone.getX());
		assertEpsilonEquals(origin.getY(), clone.getY());
	}

	@DisplayName("toUnitVector")
	@Nested
	public class ToUnitVector {
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D origin = createVector(23, 45, 0);
			Vector3D unitVector = origin.toUnitVector();
			assertNotNull(unitVector);
			assertNotSame(origin, unitVector);
			assertEpsilonEquals(.45511, unitVector.getX());
			assertEpsilonEquals(.89043, unitVector.getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D origin = createVector(23, 45, 0);
			Vector3D unitVector = origin.toUnitVector();
			assertNotNull(unitVector);
			assertNotSame(origin, unitVector);
			assertEpsilonEquals(0, unitVector.getX());
			assertEpsilonEquals(1, unitVector.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D origin = createVector(-45, 0, 0);
			Vector3D unitVector = origin.toUnitVector();
			assertNotNull(unitVector);
			assertNotSame(origin, unitVector);
			assertEpsilonEquals(-1, unitVector.getX());
			assertEpsilonEquals(0, unitVector.getY());
		}

	}

	@DisplayName("power(double)")
	@Nested
	public class Power {

		private Vector3D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45, 0);
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.power(6));
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.power(5));
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.power(4));
		}
		
		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.power(3));
		}
		
		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(2554, origin.power(2));
		}
		
		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(1));
		}
		
		@DisplayName("With double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1, origin.power(0));
		}
		
		@DisplayName("With double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(-1));
		}
		
		@DisplayName("With double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.power(-2));
		}
		
		@DisplayName("With double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./2554, 45./2554, origin.power(-3));
		}
		
		@DisplayName("With double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.power(-4));
		}
		
		@DisplayName("With double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./6522916, 45./6522916, origin.power(-5));
		}
		
		@DisplayName("With double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.power(-6));
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.power(6));
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.power(4));
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.power(3));
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(2554, origin.power(2));
		}
		
		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(1));
		}
		
		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1, origin.power(0));
		}
		
		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.power(-1));
		}
		
		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.power(-2));
		}
		
		@DisplayName("With int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.power(-3));
		}
		
		@DisplayName("With int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.power(-4));
		}
		
		@DisplayName("With int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.power(-5));
		}
		
		@DisplayName("With int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.power(-6));
		}

		@DisplayName("With int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.power(5));
		}
		
	}

	@DisplayName("v == Vector3D")
	@Nested
	public class OperatorEqualsVector3D {

		private Vector3D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(49, -2, 0);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_equals(null));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_equals(vector));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_equals(createVector(49, -3, 0)));
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_equals(createVector(0, 0, 0)));
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_equals(createVector(49, -2, 0)));
		}

	}

	@DisplayName("v != Vector3D")
	@Nested
	public class OperatorDiffVector3D {

		private Vector3D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(49, -2, 0);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_notEquals(null));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_notEquals(vector));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_notEquals(createVector(49, -3, 0)));
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.operator_notEquals(createVector(0, 0, 0)));
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.operator_notEquals(createVector(49, -2, 0)));
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsFunction {

		private Vector3D vector;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(49, -2, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.equals((Object) null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.equals((Object) vector));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.equals((Object) createVector(49, -3, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(vector.equals((Object) createVector(0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(vector.equals((Object) createVector(49, -2, 0)));
		}

	}

	@DisplayName("v .. Vector3D")
	@Nested
	public class DotOperatorDotVector3D {

		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;
		private Vector3D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 2, 0);
			this.vector2 = createVector(-2, 1, 0);
			this.vector3 = createVector(1, 1, 0);
			this.vector4 = createVector(1, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f,vector.operator_upTo(vector2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/4f,vector4.operator_upTo(vector3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0,vector.operator_upTo(vector));
		}

	}
	
	@DisplayName("v >.. Vector3D")
	@Nested
	public class OperatorGtDotDotVector3D {

		@DisplayName("With double coords")
		@Nested
		public class DoubleCoords {

			private Vector3D v1;
			private Vector3D v2;
			
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
				this.v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
			}
			
			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v1.operator_greaterThanDoubleDot(v1));
			}
			
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v2.operator_greaterThanDoubleDot(v2));
			}
			
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}
			
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}
			
			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}

		}

		@DisplayName("With int coords")
		@Nested
		public class IntCoords {

			private Vector3D v1;
			private Vector3D v2;
			
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
				this.v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
			}
			
			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v1.operator_greaterThanDoubleDot(v1));
			}
			
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v2.operator_greaterThanDoubleDot(v2));
			}
			
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}
			
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}
			
			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
				double sAngle2 = v2.operator_greaterThanDoubleDot(v1);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}
			
			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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

	@DisplayName("v ..< Vector3D")
	@Nested
	public class OperatorDotDorLt {

		@DisplayName("With double coords")
		@Nested
		public class DoubleCoords {

			private Vector3D v1;
			private Vector3D v2;
			
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
				this.v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v1.operator_greaterThanDoubleDot(v1));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				assertEpsilonEquals(
						0.f,
						v2.operator_greaterThanDoubleDot(v2));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeFalse(isIntCoordinates());
				double sAngle1 = v2.operator_greaterThanDoubleDot(v1);
				double sAngle2 = v1.operator_greaterThanDoubleDot(v2);
				assertEpsilonEquals(-sAngle1, sAngle2);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}

		}
	
		@DisplayName("With int coords")
		@Nested
		public class IntCoords {

			private Vector3D v1;
			private Vector3D v2;
			
			@BeforeEach
			public void setUp() {
				this.v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
				this.v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v1.operator_greaterThanDoubleDot(v1));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				assertEpsilonEquals(
						0,
						v2.operator_greaterThanDoubleDot(v2));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v2.operator_greaterThanDoubleDot(v1);
				double sAngle2 = v1.operator_greaterThanDoubleDot(v2);
				assertEpsilonEquals(-sAngle1, sAngle2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v2.operator_greaterThanDoubleDot(v1);
				double sAngle2 = v1.operator_greaterThanDoubleDot(v2);
				assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v2.operator_greaterThanDoubleDot(v1);
				double sAngle2 = v1.operator_greaterThanDoubleDot(v2);
				assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assumeTrue(isIntCoordinates());
				double sAngle1 = v2.operator_greaterThanDoubleDot(v1);
				double sAngle2 = v1.operator_greaterThanDoubleDot(v2);
				double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();
				if (sin < 0) {
					assertTrue(sAngle1 >= 0);
					assertTrue(sAngle2 <= 0);
				} else {
					assertTrue(sAngle1 <= 0);
					assertTrue(sAngle2 >= 0);
				}
			}
		}

	}

	@DisplayName("-v")
	@Nested
	public class OperatorMinus {

		private Vector3D vect;
		
		@BeforeEach
		public void setUp() {
			this.vect = createVector(45, -78, 0);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D result = vect.operator_minus();
			assertNotSame(vect, result);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D result = vect.operator_minus();
			assertEpsilonEquals(-vect.getX(), result.getX());
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D result = vect.operator_minus();
			assertEpsilonEquals(-vect.getY(), result.getY());
		}

	}

	@DisplayName("v * double")
	@Nested
	public class OperatorMultiplyDouble {

		private Vector3D vect;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(45, -78, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D result = vect.operator_multiply(5);
			assertNotSame(vect, result);
			assertEpsilonEquals(225, result.getX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D result = vect.operator_multiply(5);
			assertNotSame(vect, result);
			assertEpsilonEquals(-390, result.getY());
		}

	}

	@DisplayName("v / double")
	@Nested
	public class OperatorDivideDouble {

		private Vector3D vect;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(45, -78, 0);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D result = vect.operator_divide(5);
			assertNotSame(vect, result);
		}
	
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D result = vect.operator_divide(5);
			assertEquals(9, result.ix());
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D result = vect.operator_divide(5);
			assertEquals(-16, result.iy());
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D result = vect.operator_divide(5);
			assertNotSame(vect, result);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D result = vect.operator_divide(5);
			assertEpsilonEquals(9, result.getX());
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D result = vect.operator_divide(5);
			assertEpsilonEquals(-15.6, result.getY());
		}

	}

	@DisplayName("v ?: Vector3D")
	@Nested
	public class OperatorElvisVector3D {

		private Vector3D orig1 = createVector(45, -78, 0);
		private Vector3D orig2 = createVector(0, 0, 0);
		private Vector3D param = createVector(-5, -1.4, 0);

		@BeforeEach
		public void setUp() {
			this.orig1 = createVector(45, -78, 0);
			this.orig2 = createVector(0, 0, 0);
			this.param = createVector(-5, -1.4, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(null);
			assertSame(orig1, result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(orig1);
			assertSame(orig1, result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(param);
			assertSame(orig1, result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(null);
			assertNull(result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(orig2);
			assertSame(orig2, result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(param);
			assertSame(param, result);
		}

	}

	@DisplayName("v - Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		private Vector3D vect;
		private Vector3D vect2;
		private Vector3D vector;
		private Vector3D vector2;

		@BeforeEach
		public void SetUp() {
			this.vect = createVector(0, 0, 0);
			this.vect2 = createVector(-1, 0, 0);
			this.vector = createVector(-1.2, -1.2, 0);
			this.vector2 = createVector(-2.0, -1.5, 0);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var r = vect.operator_minus(vector);
			assertFpVectorEquals(1.2, 1.2, 0, r);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var r = vect2.operator_minus(vector2);
			assertFpVectorEquals(1.0, 1.5, 0, r);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var r = vect.operator_minus(vector);
			assertIntVectorEquals(1, 0, 0, r);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var r = vect2.operator_minus(vector2);
			assertIntVectorEquals(1, 0, 0, r);
		}

	}

	@DisplayName("v + Vector3D")
	@Nested
	public class OperatorPlusVector3D {
		
		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;
		private Vector3D vector4;

		@BeforeEach
		public void SetUp() {
			this.vector = createVector(0, 0, 0);
			this.vector2 = createVector(-1, 0, 0);
			this.vector3 = createVector(1.2, 1.2, 0);
			this.vector4 = createVector(2.0, 1.5, 0);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var r = vector.operator_plus(vector3);
			assertFpVectorEquals(1.2, 1.2, 0, r);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var r = vector2.operator_plus(vector4);
			assertFpVectorEquals(1., 1.5, 0, r);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var r = vector.operator_plus(vector3);
			assertIntVectorEquals(1, 0, 0, r);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var r = vector2.operator_plus(vector4);
			assertIntVectorEquals(1, 0, 0, r);
		}

	}

	@DisplayName("v ^ Vector3D")
	@Nested
	public class OperatorPowerVector3D {

		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void SetUp() {
			this.vector = createVector(1, 2, 0);
			this.vector2 = createVector(3, 4, 0);
			this.vector3 = createVector(1, -2, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, vector.operator_power(vector));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2, vector.operator_power(vector2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4, vector.operator_power(vector3));
		}
	}

	@DisplayName("v ^ int")
	@Nested
	public class OperatorPowerInt {

		private Vector3D origin;
		
		@BeforeEach
		public void setUp() {
			this.origin = createVector(23, 45, 0);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.operator_power(6));
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.operator_power(5));
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.operator_power(4));
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.operator_power(3));
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(2554, origin.operator_power(2));
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(1));
		}

		@DisplayName("With double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1, origin.operator_power(0));
		}

		@DisplayName("With double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(-1));
		}

		@DisplayName("With double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.operator_power(-2));
		}

		@DisplayName("With double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./2554, 45./2554, origin.operator_power(-3));
		}

		@DisplayName("With double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.operator_power(-4));
		}

		@DisplayName("With double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(23./6522916, 45./6522916, origin.operator_power(-5));
		}

		@DisplayName("With double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.operator_power(-6));
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1.6659527464e10, origin.operator_power(6));
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(150027068, 293531220, origin.operator_power(5));
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(6522916, origin.operator_power(4));
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(58742, 114930, origin.operator_power(3));
		}
		
		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(2554, origin.operator_power(2));
		}
		
		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(1));
		}
		
		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1, origin.operator_power(0));
		}
		
		@DisplayName("With int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(23, 45, origin.operator_power(-1));
		}
		
		@DisplayName("With int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./2554, origin.operator_power(-2));
		}
		
		@DisplayName("With int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.operator_power(-3));
		}
		
		@DisplayName("With int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./6522916, origin.operator_power(-4));
		}
		
		@DisplayName("With int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(0, 0, origin.operator_power(-5));
		}
		
		@DisplayName("With int coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
		}
		
		@DisplayName("With int coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertEpsilonEquals(1./1.6659527464e10, origin.operator_power(-6));
		}

	}

	@DisplayName("v + Point3D")
	@Nested
	public class OperationPlusPoint3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = vector1.operator_plus(point);
			assertFpPointEquals(1, 2, 0, r);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = vector2.operator_plus(point);
			assertFpPointEquals(2, 4, 0, r);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = vector3.operator_plus(point);
			assertFpPointEquals(2, -3, 0, r);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = vector1.operator_plus(point2);
			assertFpPointEquals(3, 0, 0, r);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = vector2.operator_plus(point2);
			assertFpPointEquals(4, 2, 0, r);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = vector3.operator_plus(point2);
			assertFpPointEquals(4, -5, 0, r);
		}

	}

	@DisplayName("add(Vector3D,Vector3D)")
	@Nested
	public class AddVector3DVector3D {
		
		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;
		private Vector3D vector4;
		private Vector3D vector5;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0, 0, 0);
			this.vector2 = createVector(-1, 0, 0);
			this.vector3 = createVector(1.2, 1.2, 0);
			this.vector4 = createVector(2.0, 1.5, 0);
			this.vector5 = createVector(0.0, 0.0, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector5.add(vector3,vector);
			assertFpVectorEquals(1.2, 1.2, 0, vector5);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector5.add(vector4,vector2);
			assertFpVectorEquals(1.0, 1.5, 0, vector5); 
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector5.add(vector3,vector);
			assertIntVectorEquals(1, 0, 0, vector5);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector5.add(vector4,vector2);
			assertIntVectorEquals(1, 0, 0, vector5); 
		}

	}

	@DisplayName("add(Vector3D)")
	@Nested
	public class AddVector3D {
		
		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;
		private Vector3D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0, 0, 0);
			this.vector2 = createVector(-1, 0, 0);
			this.vector3 = createVector(1.2, 1.2, 0);
			this.vector4 = createVector(2.0, 1.5, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.add(vector3);
			assertFpVectorEquals(1.2, 1.2, 0, vector);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector2.add(vector4);
			assertFpVectorEquals(1., 1.5, 0, vector2);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.add(vector3);
			assertIntVectorEquals(1, 0, 0, vector);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector2.add(vector4);
			assertIntVectorEquals(1, 0, 0, vector2);
		}

	}

	@DisplayName("scaleAdd(int,Vector3D,Vector3D)")
	@Nested
	public class ScaleAddIntVector3DVector3D {
		
		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(-1, 0, 0);
			this.vector2 = createVector(1, 1.2, 0);
			this.vector3 = createVector(0, 0, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(0,vector2,vector);
			assertFpVectorEquals(-1, 0, 0, vector3);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(1,vector2,vector);
			assertFpVectorEquals(0.0, 1.2, 0, vector3);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(-1,vector2,vector);
			assertFpVectorEquals(-2.0, -1.2, 0, vector3);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector3.scaleAdd(10,vector2,vector);
			assertFpVectorEquals(9, 12, 0, vector3);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(0,vector2,vector);
			assertFpVectorEquals(-1, 0, 0, vector3);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(1,vector2,vector);
			assertFpVectorEquals(0, 1, 0, vector3);
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(-1,vector2,vector);
			assertFpVectorEquals(-2, -1, 0, vector3);
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector3.scaleAdd(10,vector2,vector);
			assertFpVectorEquals(9, 10, 0, vector3);
		}

	}

	@DisplayName("scaleAdd(double,Vector3D,Vector3D)")
	@Nested
	public class ScaleAddDoubleVector3DVector3D {

		private Vector3D vect;
		private Vector3D vector;
		private Vector3D newPoint;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(1, 2, 1);
			this.vector = createVector(-1, 1, 0);
			this.newPoint = createVector(34, 35, 36);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(0.0, vect, vector);
			assertFpVectorEquals(-1, 1, 0, newPoint);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(1.5, vect, vector);
			assertFpVectorEquals(.5, 4., 1.5, newPoint);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(-1.5, vect, vector);
			assertFpVectorEquals(-2.5, -2., -1.5, newPoint);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(.1, vect, vector);
			assertFpVectorEquals(-.9, 1.2, 0.1, newPoint);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(0.0, vect, vector);
			assertIntVectorEquals(1, 0, 0, newPoint);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(1.5, vect, vector);
			assertIntVectorEquals(0, 2, 0, newPoint);
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(-1.5, vect, vector);
			assertIntVectorEquals(3, -1, 0, newPoint);
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(0.1, vect, vector);
			assertIntVectorEquals(1, 0, 0, newPoint);
		}

	}

	@DisplayName("scaleAdd(int,Vector3D)")
	@Nested
	public class ScaleAddIntVector3D {

		private Vector3D vector;
		private Vector3D newPoint;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 0, -1);
			this.newPoint = createVector(1, 2, 1);
			assumeMutable(this.vector);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			newPoint.scaleAdd(0, vector);
			assertFpVectorEquals(1, 0, -1, newPoint);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			newPoint.scaleAdd(1, vector);
			assertFpVectorEquals(2, 2, 0, newPoint);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			newPoint.scaleAdd(-10, vector);
			assertFpVectorEquals(-9, -20, -11, newPoint);
		}

	}

	@DisplayName("scaleAdd(double,Vector3D)")
	@Nested
	public class ScaleAddDoubleVector3D {

		private Vector3D vector;
		private Vector3D newPoint;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(1, 0, 1);
			this.newPoint = createVector(1, 2, 1);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(0.5,vector);
			assertFpVectorEquals(1.5, 1, 1.5, newPoint);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(1.2,vector);
			assertFpVectorEquals(2.2, 2.4, 2.2, newPoint);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.scaleAdd(-10.,vector);
			assertFpVectorEquals(-9, -20, -9, newPoint);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(0.5,vector);
			assertIntVectorEquals(1, 0, 0, newPoint);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(1.2,vector);
			assertIntVectorEquals(2, 0, 0, newPoint);
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.scaleAdd(-10.,vector);
			assertIntVectorEquals(-19, 0, 0, newPoint);
		}

	}

	@DisplayName("sub(Vector3D,Vector3D)")
	@Nested
	public class SubVector3DVector3D {

		private Vector3D vect;
		private Vector3D vect2;
		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D newPoint;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(0, 0, 0);
			this.vect2 = createVector(1, 0, 0);
			this.vector = createVector(-1.2, -1.2, 0);
			this.vector2 = createVector(2.0, 1.5, 0);
			this.newPoint = createVector(0.0, 0.0, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(vect,vector);
			assertFpVectorEquals(1.2, 1.2, 0, newPoint);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(vect2,vector2);
			assertFpVectorEquals(-1.0, -1.5, 0, newPoint); 
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.sub(vect,vector);
			assertIntVectorEquals(1, 1, 0, newPoint);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.sub(vect2,vector2);
			assertIntVectorEquals(-1, -2, 0, newPoint); 
		}

	}

	@DisplayName("sub(Point3D,Point3D)")
	@Nested
	public class SubPoint3DPoint3D {

		private Point3D point;
		private Point3D point2;
		private Point3D vector;
		private Point3D vector2;
		private Vector3D newPoint;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0, 0);
			this.point2 = createPoint(1, 0, 0);
			this.vector = createPoint(-1.2, -1.2, 0);
			this.vector2 = createPoint(2.0, 1.5, 0);
			this.newPoint = createVector(0.0, 0.0, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(point,vector);
			assertFpVectorEquals(1.2, 1.2, 0, newPoint);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			newPoint.sub(point2,vector2);
			assertFpVectorEquals(-1.0, -1.5, 0, newPoint); 
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.sub(point,vector);
			assertIntVectorEquals(1, 1, 0, newPoint);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			newPoint.sub(point2,vector2);
			assertIntVectorEquals(-1, -2, 0, newPoint); 
		}

	}

	@DisplayName("sub(Vector3D)")
	@Nested
	public class SubVector3D {
		
		private Vector3D vect;
		private Vector3D vect2;
		private Vector3D vector;
		private Vector3D vector2;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(0, 0, 0);
			this.vect2 = createVector(1, 0, 0);
			this.vector = createVector(-1.2, -1.2, 0);
			this.vector2 = createVector(2.0, 1.5, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect.sub(vector);
			assertFpVectorEquals(1.2, 1.2, 0, vect);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect2.sub(vector2);
			assertFpVectorEquals(-1., -1.5, 0, vect2);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vect.sub(vector);
			assertIntVectorEquals(1, 1, 0, vect);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vect2.sub(vector2);
			assertIntVectorEquals(1, 1, 0, vect2);
		}

	}

	@DisplayName("normalize")
	@Nested
	public class Normalize {

		@BeforeEach
		public void setUp() {
			assumeMutable(createVector(1, 2, 0));
		}
		
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var vector = createVector(1, 2, 0);
			vector.normalize();
			assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), 0, vector);
		}
	
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var vector2 = createVector(0, 0, 0);
			vector2.normalize();
			assertZero(vector2.getX());
			assertZero(vector2.getY());
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var vector3 = createVector(-1, 1, 0);
			vector3.normalize();
			assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), 0, vector3);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var vector = createVector(1,2, 0);
			vector.normalize();
			assertIntVectorEquals(0, 1, 0, vector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector2 = createVector(0,0, 0);
			vector2.normalize();
			assertIntVectorEquals(0, 0, 0, vector2);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector3 = createVector(-1,1, 0);
			vector3.normalize();
			assertIntVectorEquals(-1, 1, 0, vector3);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector4 = createVector(0,-5, 0);
			vector4.normalize();
			assertIntVectorEquals(0, -1, 0, vector4);
		}

	}

	@DisplayName("normalize(Vector3D)")
	@Nested
	public class NormalizeVector3D {

		@BeforeEach
		public void setUp() {
			assumeMutable(createVector(1, 2, 0));
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D vector = createVector(0,0, 0);
			vector.normalize(createVector(1,2,0));
			assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), 0, vector);
		}
	
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D vector2 = createVector(0,0, 0);
			vector2.normalize(createVector(0,0,0));
			assertZero(vector2.getX());
			assertZero(vector2.getY());
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D vector3 = createVector(0,0, 0);
			vector3.normalize(createVector(-1,1,0));
			assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), 0, vector3);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector = createVector(0,0, 0);
			vector.normalize(createVector(1,2,0));
			assertIntVectorEquals(0, 1, 0, vector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector2 = createVector(0,0, 0);
			vector2.normalize(createVector(0,0,0));
			assertIntVectorEquals(0, 0, 0, vector2);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector3 = createVector(0,0, 0);
			vector3.normalize(createVector(-1,1,0));
			assertIntVectorEquals(-1, 1, 0, vector3);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector4 = createVector(0,0, 0);
			vector4.normalize(createVector(0,-5,0));
			assertIntVectorEquals(0, -1, 0, vector4);
		}

	}

	@DisplayName("setLength")
	@Nested
	public class SetLength {
		
		@BeforeEach
		public void setUp() {
			assumeMutable(createVector(1, 2, 0));
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Vector3D vector = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
			Vector3D oldVector = (Vector3D) vector.clone();
			
			double length = oldVector.getLength();
			double newLength = getRandom().nextDouble() * 20;
			while (length == newLength) {
				newLength = getRandom().nextDouble() * 20;
			}
			
			vector.setLength(newLength);
			
			assertEpsilonEquals(newLength, vector.getLength());
			
			oldVector.normalize();
			vector.normalize();
			if (!Vector3D.isColinearVectors(
					oldVector.getX(), oldVector.getY(), oldVector.getZ(),
					vector.getX(), vector.getY(), vector.getZ())) {
				fail("No colinear vectors; oldVector = " + oldVector + "; vector = " + vector);
			}
		}

		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void setLength_ifi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Vector3D vector = createVector(0, 2, 0);
			
			int newLength = 5;
			
			vector.setLength(newLength);
			
			assertIntVectorEquals(0, newLength, 0, vector);
		}

	}

	@DisplayName("v += Vector3D")
	@Nested
	public class OperatorAddVector3D {
		
		private Vector3D vector;
		private Vector3D vector2;
		private Vector3D vector3;
		private Vector3D vector4;

		@BeforeEach
		public void setUp() {
			this.vector = createVector(0, 0, 0);
			this.vector2 = createVector(-1, 0, 0);
			this.vector3 = createVector(1.2, 1.2, 0);
			this.vector4 = createVector(2.0, 1.5, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector.operator_add(vector3);
			assertFpVectorEquals(1.2, 1.2, 0, vector);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vector2.operator_add(vector4);
			assertFpVectorEquals(1., 1.5, 0, vector2);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector.operator_add(vector3);
			assertIntVectorEquals(1, 1, 0, vector);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vector2.operator_add(vector4);
			assertIntVectorEquals(1, 2, 0, vector2);
		}
	}

	@DisplayName("v -= Vector3D")
	@Nested
	public class OperatorSubVector3D {
		
		private Vector3D vect;
		private Vector3D vect2;
		private Vector3D vector;
		private Vector3D vector2;

		@BeforeEach
		public void setUp() {
			this.vect = createVector(0, 0, 0);
			this.vect2 = createVector(-1, 0, 0);
			this.vector = createVector(-1.2, -1.2, 0);
			this.vector2 = createVector(-2.0, -1.5, 0);
			assumeMutable(this.vector);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect.operator_remove(vector);
			assertFpVectorEquals(1.2, 1.2, 0, vect);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			vect2.operator_remove(vector2);
			assertFpVectorEquals(1.0, 1.5, 0, vect2);
		}
	
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vect.operator_remove(vector);
			assertIntVectorEquals(1, 1, 0, vect);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			vect2.operator_remove(vector2);
			assertIntVectorEquals(1, 1, 0, vect2);
		}

	}

}

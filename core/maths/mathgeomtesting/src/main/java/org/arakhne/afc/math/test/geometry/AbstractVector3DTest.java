/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry;

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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D.PowerResult;

@SuppressWarnings("all")
public abstract class AbstractVector3DTest<V extends Vector3D<? super V, ? super P, ? super Q>,
	P extends Point3D<? super P, ? super V, ? super Q>,
	Q extends Quaternion<? super P, ? super V, ? super Q>,
	TT extends Tuple3D>
	extends AbstractTuple3DTest<TT> {

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
	@Test
	public final void staticIsUnitVector() {
		assertTrue(Vector3D.isUnitVector(1., 0 , 0));
		assertFalse(Vector3D.isUnitVector(1.0001, 0, 0));
		double length = Math.sqrt(5. * 5. + 18. * 18. + 3. * 3.);
		assertTrue(Vector3D.isUnitVector(5. / length, 18. / length, 3. / length));
	}

	@DisplayName("isCollinearVectors(double,double,double,double,double,double)")
	@Test
	public final void staticIsCollinearVectors() {
		assertTrue(Vector3D.isColinearVectors(1, 0, 0, 3, 0, 0));
		assertTrue(Vector3D.isColinearVectors(1, 0, 0, -3, 0, 0));
		assertFalse(Vector3D.isColinearVectors(1, 0, 0, 4, 4, 0));
	}

	@DisplayName("perpProduct(double,double,double,double,double,double)")
	@Test
	public final void staticPerpProduct() {
		assertEpsilonEquals(0, Vector3D.perpProduct(1, 0, 0, 1, 0, 0));
		assertEpsilonEquals(0, Vector3D.perpProduct(1, 0, 0, 5, 0, 0));
		assertEpsilonEquals(243, Vector3D.perpProduct(1, 45, 0, -5, 18, 0));
		assertEpsilonEquals(0, Vector3D.perpProduct(1, 2, 0, 1, 2, 0));
		assertEpsilonEquals(-2, Vector3D.perpProduct(1, 2, 0, 3, 4, 0));
		assertEpsilonEquals(-4, Vector3D.perpProduct(1, 2, 0, 1, -2, 0));
	}

	@DisplayName("dotProduct(double,double,double,double,double,double)")
	@Test
	public final void staticDotProduct() {
		assertEpsilonEquals(1, Vector3D.dotProduct(1, 0, 0, 1, 0, 0));
		assertEpsilonEquals(5, Vector3D.dotProduct(1, 0, 0, 5, 0, 0));
		assertEpsilonEquals(805, Vector3D.dotProduct(1, 45, 0, -5, 18, 0));
		assertEpsilonEquals(5, Vector3D.dotProduct(1, 2, 0, 1, 2, 0));
		assertEpsilonEquals(11, Vector3D.dotProduct(1, 2, 0, 3, 4, 0));
		assertEpsilonEquals(-3, Vector3D.dotProduct(1, 2, 0, 1, -2, 0));
	}

	@DisplayName("signedAngle(double,double,double,double,double,double) 1")
	@Test
	public final void staticSignedAngle_1() {
		assertEpsilonEquals(0, Vector3D.signedAngle(1, 0, 0, 1, 0, 0));
		assertEpsilonEquals(0, Vector3D.signedAngle(1, 0, 0, 5, 0, 0));
		assertEpsilonEquals(-MathConstants.DEMI_PI, Vector3D.signedAngle(2, 0, 0, 0, -3, 0));
		assertEpsilonEquals(Math.PI, Vector3D.signedAngle(1, 0, 0, -1, 0, 0));
		assertEpsilonEquals(0.29317, Vector3D.signedAngle(1, 45, 0, -5, 18, 0));
	}

	@DisplayName("signedAngle(double,double,double,double,double,double) 2")
	@Test
	public final void staticSignedAngle_2() {
		double v1x = getRandom().nextDouble();
		double v1y = getRandom().nextDouble();
		double v1z = getRandom().nextDouble();
		double v2x = getRandom().nextDouble();
		double v2y = getRandom().nextDouble();
		double v2z = getRandom().nextDouble();

		assertEpsilonEquals(
				0.,
				Vector3D.signedAngle(v1x, v1y, v1z, v1x, v1y, v1z));
		assertEpsilonEquals(
				0.,
				Vector3D.signedAngle(v2x, v2y, v2z, v2x, v2y, v2z));

		double sAngle1 = Vector3D.signedAngle(v1x, v1y, v1z, v2x, v2y, v2z);
		double sAngle2 = Vector3D.signedAngle(v2x, v2y, v2z, v1x, v1y, v1z);

		assertEpsilonEquals(-sAngle1, sAngle2);
	}

	@DisplayName("dot(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void dotVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(1, 2, 0);
		Vector3D vector2 = createVector(3, 4, 0);
		Vector3D vector3 = createVector(1, -2, 0);

		assertEpsilonEquals(5,vector.dot(vector));
		assertEpsilonEquals(11,vector.dot(vector2));
		assertEpsilonEquals(-3,vector.dot(vector3));
	}

	@DisplayName("perp(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void perpVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(1,2, 0);
		Vector3D vector2 = createVector(3,4, 0);
		Vector3D vector3 = createVector(1,-2, 0);

		assertEpsilonEquals(0, vector.perp(vector));
		assertEpsilonEquals(-2, vector.perp(vector2));
		assertEpsilonEquals(-4, vector.perp(vector3));
	}

	@DisplayName("getLength")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getLength(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(1, 2, 0);
		Vector3D vector2 = createVector(0, 0, 0);
		Vector3D vector3 = createVector(-1, 1, 0);

		assertEpsilonEquals(Math.sqrt(5),vector.getLength());
		assertEpsilonEquals(0,vector2.getLength());
		assertEpsilonEquals(Math.sqrt(2),vector3.getLength());
	}

	@DisplayName("getLengthSquared with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getLengthSquared_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(1, 2, 0);
		Vector3D vector2 = createVector(0, 0, 0);
		Vector3D vector3 = createVector(Math.sqrt(2.) / 2., Math.sqrt(2.) / 2., 0);

		assertEpsilonEquals(5,vector.getLengthSquared());
		assertEpsilonEquals(0,vector2.getLengthSquared());
		assertEpsilonEquals(1,vector3.getLengthSquared());
	}

	@DisplayName("getLengthSquared with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void lengthSquared_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(1, 2, 0);
		Vector3D vector2 = createVector(0, 0, 0);
		Vector3D vector3 = createVector(Math.sqrt(2.) / 2., Math.sqrt(2.) / 2., 0);

		assertEpsilonEquals(5,vector.getLengthSquared());
		assertEpsilonEquals(0,vector2.getLengthSquared());
		assertEpsilonEquals(2,vector3.getLengthSquared());
	}

	@DisplayName("angle(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void angleVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(1, 2, 0);
		Vector3D vector2 = createVector(-2, 1, 0);
		Vector3D vector3 = createVector(1, 1, 0);
		Vector3D vector4 = createVector(1, 0, 0);

		assertEpsilonEquals(PI/2f,vector.angle(vector2));
		assertEpsilonEquals(PI/4f,vector4.angle(vector3));
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.angle(vector));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.angle(vector2));
		assertEpsilonEquals(0,vector.angle(vector));
	}

	@DisplayName("signedAngle(Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void signedAngleVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
		Vector3D v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());

		assertEpsilonEquals(
				0.f,
				v1.signedAngle(v1));
		assertEpsilonEquals(
				0.f,
				v2.signedAngle(v2));

		double sAngle1 = v1.signedAngle(v2);
		double sAngle2 = v2.signedAngle(v1);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
	}

	@DisplayName("signedAngle(Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void signedAngleVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
		Vector3D v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);

		assertEpsilonEquals(
				0,
				v1.signedAngle(v1));
		assertEpsilonEquals(
				0,
				v2.signedAngle(v2));

		double sAngle1 = v1.signedAngle(v2);
		double sAngle2 = v2.signedAngle(v1);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@DisplayName("isUnitVector with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void isUnitVector_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertFalse(createVector(7.15161,6.7545, 0).isUnitVector());
		assertTrue(createVector(0,-1, 0).isUnitVector());
		assertTrue((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2,0)).isUnitVector());
		assertTrue((createVector(1,0,0)).isUnitVector()); 
	}

	@DisplayName("isUnitVector with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void isUnitVector_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertFalse(createVector(7.15161,6.7545, 0).isUnitVector());
		assertTrue(createVector(0,-1, 0).isUnitVector());
		assertFalse(createVector(0.72700, 0.68663, 0).isUnitVector());
		assertFalse((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2, Math.sqrt(2)/2)).isUnitVector());
		assertTrue((createVector(1,0,0)).isUnitVector()); 
	}

	@DisplayName("toUnmodifiable with exception")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toUnmodifiable_exception(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			Vector3D origin = createVector(2, 3, 0);
			Vector3D immutable = origin.toUnmodifiable();
			assertEpsilonEquals(origin, immutable);
			immutable.add(1, 2, 0);
		});
	}

	@DisplayName("toUnmodifiable")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toUnmodifiable_changeInOrigin(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D origin = createVector(2, 3, 0);
		assumeMutable(origin);
		Vector3D immutable = origin.toUnmodifiable();
		assertEpsilonEquals(origin, immutable);
		origin.add(1, 2, 0);
		assertEpsilonEquals(origin, immutable);
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

	@DisplayName("toUnitVector with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toUnitVector_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D origin = createVector(23, 45, 0);
		Vector3D unitVector = origin.toUnitVector();
		assertNotNull(unitVector);
		assertNotSame(origin, unitVector);
		assertEpsilonEquals(.45511, unitVector.getX());
		assertEpsilonEquals(.89043, unitVector.getY());
	}

	@DisplayName("toUnitVector with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toUnitVector_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D origin = createVector(23, 45, 0);
		Vector3D unitVector = origin.toUnitVector();
		assertNotNull(unitVector);
		assertNotSame(origin, unitVector);
		assertEpsilonEquals(0, unitVector.getX());
		assertEpsilonEquals(1, unitVector.getY());
		//
		origin = createVector(-45, 0, 0);
		unitVector = origin.toUnitVector();
		assertNotNull(unitVector);
		assertNotSame(origin, unitVector);
		assertEpsilonEquals(-1, unitVector.getX());
		assertEpsilonEquals(0, unitVector.getY());
	}

	@DisplayName("power(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void power_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D origin = createVector(23, 45, 0);
		assertEpsilonEquals(1.6659527464e10, origin.power(6));
		assertEpsilonEquals(150027068, 293531220, origin.power(5));
		assertEpsilonEquals(6522916, origin.power(4));
		assertEpsilonEquals(58742, 114930, origin.power(3));
		assertEpsilonEquals(2554, origin.power(2));
		assertEpsilonEquals(23, 45, origin.power(1));
		assertEpsilonEquals(1, origin.power(0));
		assertEpsilonEquals(23, 45, origin.power(-1));
		assertEpsilonEquals(1./2554, origin.power(-2));
		assertEpsilonEquals(23./2554, 45./2554, origin.power(-3));
		assertEpsilonEquals(1./6522916, origin.power(-4));
		assertEpsilonEquals(23./6522916, 45./6522916, origin.power(-5));
		assertEpsilonEquals(1./1.6659527464e10, origin.power(-6));
	}

	@DisplayName("power(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void power_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D origin = createVector(23, 45, 0);
		assertEpsilonEquals(1.6659527464e10, origin.power(6));
		assertEpsilonEquals(150027068, 293531220, origin.power(5));
		assertEpsilonEquals(6522916, origin.power(4));
		assertEpsilonEquals(58742, 114930, origin.power(3));
		assertEpsilonEquals(2554, origin.power(2));
		assertEpsilonEquals(23, 45, origin.power(1));
		assertEpsilonEquals(1, origin.power(0));
		assertEpsilonEquals(23, 45, origin.power(-1));
		assertEpsilonEquals(1./2554, origin.power(-2));
		assertEpsilonEquals(0, 0, origin.power(-3));
		assertEpsilonEquals(1./6522916, origin.power(-4));
		assertEpsilonEquals(0, 0, origin.power(-5));
		assertEpsilonEquals(1./1.6659527464e10, origin.power(-6));
	}

	@DisplayName("v == Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_equalsVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(49, -2, 0);
		assertFalse(vector.operator_equals(null));
		assertTrue(vector.operator_equals(vector));
		assertFalse(vector.operator_equals(createVector(49, -3, 0)));
		assertFalse(vector.operator_equals(createVector(0, 0, 0)));
		assertTrue(vector.operator_equals(createVector(49, -2, 0)));
	}

	@DisplayName("v != Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_notEqualsVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(49, -2, 0);
		assertTrue(vector.operator_notEquals(null));
		assertFalse(vector.operator_notEquals(vector));
		assertTrue(vector.operator_notEquals(createVector(49, -3, 0)));
		assertTrue(vector.operator_notEquals(createVector(0, 0, 0)));
		assertFalse(vector.operator_notEquals(createVector(49, -2, 0)));
	}

	@DisplayName("equals(Object)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void testEqualsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(49, -2, 0);
		assertFalse(vector.equals((Object) null));
		assertTrue(vector.equals((Object) vector));
		assertFalse(vector.equals((Object) createVector(49, -3, 0)));
		assertFalse(vector.equals((Object) createVector(0, 0, 0)));
		assertTrue(vector.equals((Object) createVector(49, -2, 0)));
	}

	@DisplayName("v .. Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_upToVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(1, 2, 0);
		Vector3D vector2 = createVector(-2, 1, 0);
		Vector3D vector3 = createVector(1, 1, 0);
		Vector3D vector4 = createVector(1, 0, 0);

		assertEpsilonEquals(PI/2f,vector.operator_upTo(vector2));
		assertEpsilonEquals(PI/4f,vector4.operator_upTo(vector3));
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector2));
		assertEpsilonEquals(0,vector.operator_upTo(vector));
	}
	
	@DisplayName("v >.. Vector3D with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_greaterThanDoubleDotVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
		Vector3D v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());

		assertEpsilonEquals(
				0.f,
				v1.operator_greaterThanDoubleDot(v1));
		assertEpsilonEquals(
				0.f,
				v2.operator_greaterThanDoubleDot(v2));

		double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
		double sAngle2 = v2.operator_greaterThanDoubleDot(v1);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
	}

	@DisplayName("v >.. Vector3D with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_greaterThanDoubleDotVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
		Vector3D v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);

		assertEpsilonEquals(
				0,
				v1.operator_greaterThanDoubleDot(v1));
		assertEpsilonEquals(
				0,
				v2.operator_greaterThanDoubleDot(v2));

		double sAngle1 = v1.operator_greaterThanDoubleDot(v2);
		double sAngle2 = v2.operator_greaterThanDoubleDot(v1);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@DisplayName("v ..< Vector3D with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_doubleDotLessThanVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
		Vector3D v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());

		assertEpsilonEquals(
				0.f,
				v1.operator_greaterThanDoubleDot(v1));
		assertEpsilonEquals(
				0.f,
				v2.operator_greaterThanDoubleDot(v2));

		double sAngle1 = v2.operator_greaterThanDoubleDot(v1);
		double sAngle2 = v1.operator_greaterThanDoubleDot(v2);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
	}

	@DisplayName("v ..< Vector3D with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_doubleDotLessThanVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
		Vector3D v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);

		assertEpsilonEquals(
				0,
				v1.operator_greaterThanDoubleDot(v1));
		assertEpsilonEquals(
				0,
				v2.operator_greaterThanDoubleDot(v2));

		double sAngle1 = v2.operator_greaterThanDoubleDot(v1);
		double sAngle2 = v1.operator_greaterThanDoubleDot(v2);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		} else {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		}
	}

	@DisplayName("-v")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_minus(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vect = createVector(45, -78, 0);
		Vector3D result = vect.operator_minus();
		assertNotSame(vect, result);
		assertEpsilonEquals(-vect.getX(), result.getX());
		assertEpsilonEquals(-vect.getY(), result.getY());
	}

	@DisplayName("v * double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_multiplyDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vect = createVector(45, -78, 0);
		Vector3D result = vect.operator_multiply(5);
		assertNotSame(vect, result);
		assertEpsilonEquals(225, result.getX());
		assertEpsilonEquals(-390, result.getY());
	}

	@DisplayName("v / double with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_divideDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(45, -78, 0);
		Vector3D result = vect.operator_divide(5);
		assertNotSame(vect, result);
		assertEquals(9, result.ix());
		assertEquals(-16, result.iy());
	}

	@DisplayName("v / double with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_divideDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(45, -78, 0);
		Vector3D result = vect.operator_divide(5);
		assertNotSame(vect, result);
		assertEpsilonEquals(9, result.getX());
		assertEpsilonEquals(-15.6, result.getY());
	}

	@DisplayName("v ?: Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_elvisVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D orig1 = createVector(45, -78, 0);
		Vector3D orig2 = createVector(0, 0, 0);
		Vector3D param = createVector(-5, -1.4, 0);
		Vector3D result;
		
		result = orig1.operator_elvis(null);
		assertSame(orig1, result);

		result = orig1.operator_elvis(orig1);
		assertSame(orig1, result);

		result = orig1.operator_elvis(param);
		assertSame(orig1, result);

		result = orig2.operator_elvis(null);
		assertNull(result);

		result = orig2.operator_elvis(orig2);
		assertSame(orig2, result);

		result = orig2.operator_elvis(param);
		assertSame(param, result);
	}

	@DisplayName("v - Vector3D with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_minusVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(-1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(-2.0, -1.5, 0);

		Vector3D r = vect.operator_minus(vector);
		assertFpVectorEquals(1.2, 1.2, 0, r);

		r = vect2.operator_minus(vector2);
		assertFpVectorEquals(1.0, 1.5, 0, r);
	}

	@DisplayName("v - Vector3D with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_minusVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(-1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(-2.0, -1.5, 0);
		Vector3D r;

		r = vect.operator_minus(vector);
		assertIntVectorEquals(1, 0, 0, r);

		r = vect2.operator_minus(vector2);
		assertIntVectorEquals(1, 0, 0, r);
	}

	@DisplayName("v + Vector3D with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_plusVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(-1,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		Vector3D vector4 = createVector(2.0,1.5, 0);

		Vector3D r = vector.operator_plus(vector3);
		assertFpVectorEquals(1.2, 1.2, 0, r);

		r = vector2.operator_plus(vector4);
		assertFpVectorEquals(1., 1.5, 0, r);
	}

	@DisplayName("v + Vector3D with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_plusVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(-1,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		Vector3D vector4 = createVector(2.0,1.5, 0);

		Vector3D r = vector.operator_plus(vector3);
		assertIntVectorEquals(1, 0, 0, r);

		r = vector2.operator_plus(vector4);
		assertIntVectorEquals(1, 0, 0, r);
	}

	@DisplayName("v ^ Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_powerVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(1,2, 0);
		Vector3D vector2 = createVector(3,4, 0);
		Vector3D vector3 = createVector(1,-2, 0);

		assertEpsilonEquals(0, vector.operator_power(vector));
		assertEpsilonEquals(-2, vector.operator_power(vector2));
		assertEpsilonEquals(-4, vector.operator_power(vector3));
	}

	@DisplayName("v ^ int with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_powerInteger_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D origin = createVector(23, 45, 0);
		assertEpsilonEquals(1.6659527464e10, origin.operator_power(6));
		assertEpsilonEquals(150027068, 293531220, origin.operator_power(5));
		assertEpsilonEquals(6522916, origin.operator_power(4));
		assertEpsilonEquals(58742, 114930, origin.operator_power(3));
		assertEpsilonEquals(2554, origin.operator_power(2));
		assertEpsilonEquals(23, 45, origin.operator_power(1));
		assertEpsilonEquals(1, origin.operator_power(0));
		assertEpsilonEquals(23, 45, origin.operator_power(-1));
		assertEpsilonEquals(1./2554, origin.operator_power(-2));
		assertEpsilonEquals(23./2554, 45./2554, origin.operator_power(-3));
		assertEpsilonEquals(1./6522916, origin.operator_power(-4));
		assertEpsilonEquals(23./6522916, 45./6522916, origin.operator_power(-5));
		assertEpsilonEquals(1./1.6659527464e10, origin.operator_power(-6));
	}

	@DisplayName("v ^ int with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_powerInteger_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D origin = createVector(23, 45, 0);
		assertEpsilonEquals(1.6659527464e10, origin.operator_power(6));
		assertEpsilonEquals(150027068, 293531220, origin.operator_power(5));
		assertEpsilonEquals(6522916, origin.operator_power(4));
		assertEpsilonEquals(58742, 114930, origin.operator_power(3));
		assertEpsilonEquals(2554, origin.operator_power(2));
		assertEpsilonEquals(23, 45, origin.operator_power(1));
		assertEpsilonEquals(1, origin.operator_power(0));
		assertEpsilonEquals(23, 45, origin.operator_power(-1));
		assertEpsilonEquals(1./2554, origin.operator_power(-2));
		assertEpsilonEquals(0, 0, origin.operator_power(-3));
		assertEpsilonEquals(1./6522916, origin.operator_power(-4));
		assertEpsilonEquals(0, 0, origin.operator_power(-5));
		assertEpsilonEquals(1./1.6659527464e10, origin.operator_power(-6));
	}

	@DisplayName("v + Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_plusPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		Point3D r;
		
		r = vector1.operator_plus(point);
		assertFpPointEquals(1, 2, 0, r);

		r = vector2.operator_plus(point);
		assertFpPointEquals(2, 4, 0, r);

		r = vector3.operator_plus(point);
		assertFpPointEquals(2, -3, 0, r);

		r = vector1.operator_plus(point2);
		assertFpPointEquals(3, 0, 0, r);

		r = vector2.operator_plus(point2);
		assertFpPointEquals(4, 2, 0, r);

		r = vector3.operator_plus(point2);
		assertFpPointEquals(4, -5, 0, r);
	}

	@DisplayName("add(Vector3D,Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0, 0, 0);
		Vector3D vector2 = createVector(-1, 0, 0);
		Vector3D vector3 = createVector(1.2, 1.2, 0);
		Vector3D vector4 = createVector(2.0, 1.5, 0);
		Vector3D vector5 = createVector(0.0, 0.0, 0);

		vector5.add(vector3,vector);
		assertFpVectorEquals(1.2, 1.2, 0, vector5);

		vector5.add(vector4,vector2);
		assertFpVectorEquals(1.0, 1.5, 0, vector5); 
	}

	@DisplayName("add(Vector3D,Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0, 0, 0);
		Vector3D vector2 = createVector(-1, 0, 0);
		Vector3D vector3 = createVector(1.2, 1.2, 0);
		Vector3D vector4 = createVector(2.0, 1.5, 0);
		Vector3D vector5 = createVector(0.0, 0.0, 0);

		vector5.add(vector3,vector);
		assertIntVectorEquals(1, 0, 0, vector5);

		vector5.add(vector4,vector2);
		assertIntVectorEquals(1, 0, 0, vector5); 
	}

	@DisplayName("add(Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(-1,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		Vector3D vector4 = createVector(2.0,1.5, 0);

		vector.add(vector3);
		assertFpVectorEquals(1.2, 1.2, 0, vector);

		vector2.add(vector4);
		assertFpVectorEquals(1., 1.5, 0, vector2);
	}

	@DisplayName("add(Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(-1,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		Vector3D vector4 = createVector(2.0,1.5, 0);

		vector.add(vector3);
		assertIntVectorEquals(1, 0, 0, vector);

		vector2.add(vector4);
		assertIntVectorEquals(1, 0, 0, vector2);
	}

	@DisplayName("scaleAdd(int,Vector3D,Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleAddIntVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(-1,0, 0);
		Vector3D vector2 = createVector(1.0,1.2, 0);
		Vector3D vector3 = createVector(0.0,0.0, 0);

		vector3.scaleAdd(0,vector2,vector);
		assertFpVectorEquals(-1, 0, 0, vector3);

		vector3.scaleAdd(1,vector2,vector);
		assertFpVectorEquals(0.0, 1.2, 0, vector3);

		vector3.scaleAdd(-1,vector2,vector);
		assertFpVectorEquals(-2.0, -1.2, 0, vector3);

		vector3.scaleAdd(10,vector2,vector);
		assertFpVectorEquals(9, 12, 0, vector3);
	}

	@DisplayName("scaleAdd(int,Vector3D,Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleAddIntVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(-1,0, 0);
		Vector3D vector2 = createVector(1.0,1.2, 0);
		Vector3D vector3 = createVector(0.0,0.0, 0);

		vector3.scaleAdd(0,vector2,vector);
		assertFpVectorEquals(-1, 0, 0, vector3);

		vector3.scaleAdd(1,vector2,vector);
		assertFpVectorEquals(0, 1, 0, vector3);

		vector3.scaleAdd(-1,vector2,vector);
		assertFpVectorEquals(-2, -1, 0, vector3);

		vector3.scaleAdd(10,vector2,vector);
		assertFpVectorEquals(9, 10, 0, vector3);
	}

	@DisplayName("scaleAdd(double,Vector3D,Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleAddDoubleVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(1,0, 0);
		Vector3D vector = createVector(-1,1, 0);
		Vector3D newPoint = createVector(0.0,0.0, 0);

		newPoint.scaleAdd(0.0, vector, vect);
		assertFpVectorEquals(1, 0, 0, newPoint);

		newPoint.scaleAdd(1.5,vector, vect);
		assertFpVectorEquals(-0.5, 1.5, 0, newPoint);

		newPoint.scaleAdd(-1.5,vector, vect);
		assertFpVectorEquals(2.5, -1.5, 0, newPoint);

		newPoint.scaleAdd(0.1,vector, vect);
		assertFpVectorEquals(0.9, 0.1, 0, newPoint);
	}

	@DisplayName("scaleAdd(double,Vector3D,Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleAddDoubleVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(1,0, 0);
		Vector3D vector = createVector(-1,1, 0);
		Vector3D newPoint = createVector(0.0,0.0, 0);

		newPoint.scaleAdd(0.0, vector, vect);
		assertIntVectorEquals(1, 0, 0, newPoint);

		newPoint.scaleAdd(1.5,vector,vect);
		assertIntVectorEquals(0, 2, 0, newPoint);

		newPoint.scaleAdd(-1.5,vector,vect);
		assertIntVectorEquals(3, -1, 0, newPoint);

		newPoint.scaleAdd(0.1,vector,vect);
		assertIntVectorEquals(1, 0, 0, newPoint);
	}

	@DisplayName("scaleAdd(int,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleAddIntVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D vector = createVector(1,0, 0);
		Vector3D newPoint = createVector(0,0, 0);

		newPoint.scaleAdd(0,vector);
		assertFpVectorEquals(1, 0, 0, newPoint);

		newPoint.scaleAdd(1,vector);
		assertFpVectorEquals(2, 0, 0, newPoint);

		newPoint.scaleAdd(-10,vector);
		assertFpVectorEquals(-19, 0, 0, newPoint);
	}

	@DisplayName("scaleAdd(double,Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleAddDoubleVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(1,0, 0);
		Vector3D newPoint = createVector(0.0,0.0, 0);

		newPoint.scaleAdd(0.5,vector);
		assertFpVectorEquals(1, 0, 0, newPoint);

		newPoint.scaleAdd(1.2,vector);
		assertFpVectorEquals(2.2, 0.0, 0, newPoint);

		newPoint.scaleAdd(-10,vector);
		assertFpVectorEquals(-21, 0, 0, newPoint);
	}

	@DisplayName("scaleAdd(double,Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleAddDoubleVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(1,0, 0);
		Vector3D newPoint = createVector(0.0,0.0, 0);

		newPoint.scaleAdd(0.5,vector);
		assertIntVectorEquals(1, 0, 0, newPoint);

		newPoint.scaleAdd(1.2,vector);
		assertIntVectorEquals(2, 0, 0, newPoint);

		newPoint.scaleAdd(-10,vector);
		assertIntVectorEquals(-19, 0, 0, newPoint);
	}

	@DisplayName("sub(Vector3D,Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(2.0, 1.5, 0);
		Vector3D newPoint = createVector(0.0, 0.0, 0);

		newPoint.sub(vect,vector);
		assertFpVectorEquals(1.2, 1.2, 0, newPoint);

		newPoint.sub(vect2,vector2);
		assertFpVectorEquals(-1.0, -1.5, 0, newPoint); 
	}

	@DisplayName("sub(Vector3D,Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(2.0, 1.5, 0);
		Vector3D newPoint = createVector(0.0, 0.0, 0);

		newPoint.sub(vect,vector);
		assertIntVectorEquals(1, 1, 0, newPoint);

		newPoint.sub(vect2,vector2);
		assertIntVectorEquals(-1, -2, 0, newPoint); 
	}

	@DisplayName("sub(Point3D,Point3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subPoint3DPoint3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Point3D point = createPoint(0, 0, 0);
		Point3D point2 = createPoint(1, 0, 0);
		Point3D vector = createPoint(-1.2, -1.2, 0);
		Point3D vector2 = createPoint(2.0, 1.5, 0);
		Vector3D newPoint = createVector(0.0, 0.0, 0);

		newPoint.sub(point,vector);
		assertFpVectorEquals(1.2, 1.2, 0, newPoint);

		newPoint.sub(point2,vector2);
		assertFpVectorEquals(-1.0, -1.5, 0, newPoint); 
	}

	@DisplayName("sub(Point3D,Point3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subPoint3DPoint3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Point3D point = createPoint(0, 0, 0);
		Point3D point2 = createPoint(1, 0, 0);
		Point3D vector = createPoint(-1.2, -1.2, 0);
		Point3D vector2 = createPoint(2.0, 1.5, 0);
		Vector3D newPoint = createVector(0.0, 0.0, 0);

		newPoint.sub(point,vector);
		assertIntVectorEquals(1, 1, 0, newPoint);

		newPoint.sub(point2,vector2);
		assertIntVectorEquals(-1, -2, 0, newPoint); 
	}

	@DisplayName("sub(Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(-1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(-2.0, -1.5, 0);

		vect.sub(vector);
		assertFpVectorEquals(1.2, 1.2, 0, vect);

		vect2.sub(vector2);
		assertFpVectorEquals(1.0, 1.5, 0, vect2);
	}

	@DisplayName("sub(Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(-1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(-2.0, -1.5, 0);

		vect.sub(vector);
		assertIntVectorEquals(1, 1, 0, vect);

		vect2.sub(vector2);
		assertIntVectorEquals(1, 1, 0, vect2);
	}

	@DisplayName("normalize with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void normalize_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(1,2, 0);
		Vector3D vector2 = createVector(0,0, 0);
		Vector3D vector3 = createVector(-1,1, 0);

		vector.normalize();
		vector2.normalize();
		vector3.normalize();

		assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), 0, vector);
		assertZero(vector2.getX());
		assertZero(vector2.getY());
		assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), 0, vector3);
	}

	@DisplayName("normalize with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void normalize_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(1,2, 0);
		Vector3D vector2 = createVector(0,0, 0);
		Vector3D vector3 = createVector(-1,1, 0);
		Vector3D vector4 = createVector(0,-5, 0);

		vector.normalize();
		vector2.normalize();
		vector3.normalize();
		vector4.normalize();

		assertIntVectorEquals(0, 1, 0, vector);
		assertIntVectorEquals(0, 0, 0, vector2);
		assertIntVectorEquals(-1, 1, 0, vector3);
		assertIntVectorEquals(0, -1, 0, vector4);
	}

	@DisplayName("normalize(Vector3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void normalizeVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(0,0, 0);
		Vector3D vector3 = createVector(0,0, 0);

		vector.normalize(createVector(1,2,0));
		vector2.normalize(createVector(0,0,0));
		vector3.normalize(createVector(-1,1,0));

		assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), 0, vector);
		assertZero(vector2.getX());
		assertZero(vector2.getY());
		assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), 0, vector3);
	}

	@DisplayName("normalize(Vector3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void normalizeVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(0,0, 0);
		Vector3D vector3 = createVector(0,0, 0);
		Vector3D vector4 = createVector(0,0, 0);

		vector.normalize(createVector(1,2,0));
		vector2.normalize(createVector(0,0,0));
		vector3.normalize(createVector(-1,1,0));
		vector4.normalize(createVector(0,-5,0));

		assertIntVectorEquals(0, 1, 0, vector);
		assertIntVectorEquals(0, 0, 0, vector2);
		assertIntVectorEquals(-1, 1, 0, vector3);
		assertIntVectorEquals(0, -1, 0, vector4);
	}

	@DisplayName("setLength(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setLength_iffp(CoordinateSystem3D cs) {
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

	@DisplayName("setLength(double) with int coords")
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

	@DisplayName("v += Vector3D with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_addVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(-1,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		Vector3D vector4 = createVector(2.0,1.5, 0);

		vector.operator_add(vector3);
		assertFpVectorEquals(1.2, 1.2, 0, vector);

		vector2.add(vector4);
		assertFpVectorEquals(1., 1.5, 0, vector2);
	}

	@DisplayName("v += Vector3D with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_addVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0, 0);
		Vector3D vector2 = createVector(-1,0, 0);
		Vector3D vector3 = createVector(1.2,1.2,0);
		Vector3D vector4 = createVector(2.0,1.5,0);

		vector.operator_add(vector3);
		assertIntVectorEquals(1, 1, 0, vector);

		vector2.operator_add(vector4);
		assertIntVectorEquals(1, 2, 0, vector2);
	}

	@DisplayName("v -= Vector3D with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_removeVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(-1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(-2.0, -1.5, 0);

		vect.operator_remove(vector);
		assertFpVectorEquals(1.2, 1.2, 0, vect);

		vect2.operator_remove(vector2);
		assertFpVectorEquals(1.0, 1.5, 0, vect2);
	}

	@DisplayName("v -= Vector3D with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_removeVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vect2 = createVector(-1, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D vector2 = createVector(-2.0, -1.5, 0);

		vect.operator_remove(vector);
		assertIntVectorEquals(1, 1, 0, vect);

		vect2.operator_remove(vector2);
		assertIntVectorEquals(1, 1, 0, vect2);
	}

}

/**
 * 
 * fr.utbm.v3g.core.math.Tuple2dTest.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 *
 * Primary author : Olivier LAMOTTE (olivier.lamotte@utbm.fr) - 2015
 *
 */
package org.arakhne.afc.math.geometry.d3;

import static org.arakhne.afc.math.MathConstants.PI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Vector3D.PowerResult;
import org.junit.Assume;
import org.junit.ComparisonFailure;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractVector3DTest<V extends Vector3D<? super V, ? super P>, P extends Point3D<? super P, ? super V>,
	TT extends Tuple3D>
	extends AbstractTuple3DTest<V, TT> {

	public abstract V createVector(double x, double y, double z);

	public abstract P createPoint(double x, double y, double z);

	@Test
	public final void staticIsUnitVector() {
		assertTrue(Vector3D.isUnitVector(1., 0));
		assertFalse(Vector3D.isUnitVector(1.0001, 0));
		double length = Math.sqrt(5. * 5. + 18. * 18.);
		assertTrue(Vector3D.isUnitVector(5. / length, 18. / length));
	}

	@Test
	public final void staticIsOrthogonal() {
		assertFalse(Vector3D.isOrthogonal(1., 0, 1., 0));
		assertFalse(Vector3D.isOrthogonal(1., 0, -1., 0));
		assertTrue(Vector3D.isOrthogonal(1., 0, 0., 1));
		assertTrue(Vector3D.isOrthogonal(1., 0, 0., -1));
		assertFalse(Vector3D.isOrthogonal(1., 0, 1., 2));
		assertTrue(Vector3D.isOrthogonal(1., 0, 0, 1 + Math.ulp(1)));
	}

	@Test
	public final void staticIsCollinearVectors() {
		assertTrue(Vector3D.isCollinearVectors(1, 0, 3, 0));
		assertTrue(Vector3D.isCollinearVectors(1, 0, -3, 0));
		assertFalse(Vector3D.isCollinearVectors(1, 0, 4, 4));
	}

	@Test
	public final void staticPerpProduct() {
		assertEpsilonEquals(0, Vector3D.perpProduct(1, 0, 1, 0));
		assertEpsilonEquals(0, Vector3D.perpProduct(1, 0, 5, 0));
		assertEpsilonEquals(243, Vector3D.perpProduct(1, 45, -5, 18));
		assertEpsilonEquals(0, Vector3D.perpProduct(1, 2, 1, 2));
		assertEpsilonEquals(-2, Vector3D.perpProduct(1, 2, 3, 4));
		assertEpsilonEquals(-4, Vector3D.perpProduct(1, 2, 1, -2));
	}

	@Test
	public final void staticDotProduct() {
		assertEpsilonEquals(1, Vector3D.dotProduct(1, 0, 1, 0));
		assertEpsilonEquals(5, Vector3D.dotProduct(1, 0, 5, 0));
		assertEpsilonEquals(805, Vector3D.dotProduct(1, 45, -5, 18));
		assertEpsilonEquals(5, Vector3D.dotProduct(1, 2, 1, 2));
		assertEpsilonEquals(11, Vector3D.dotProduct(1, 2, 3, 4));
		assertEpsilonEquals(-3, Vector3D.dotProduct(1, 2, 1, -2));
	}

	@Test
	public final void statisSignedAngle() {
		assertEpsilonEquals(0, Vector3D.signedAngle(1, 0, 1, 0));
		assertEpsilonEquals(0, Vector3D.signedAngle(1, 0, 5, 0));
		assertEpsilonEquals(-MathConstants.DEMI_PI, Vector3D.signedAngle(2, 0, 0, -3));
		assertEpsilonEquals(Math.PI, Vector3D.signedAngle(1, 0, -1, 0));
		assertEpsilonEquals(0.29317, Vector3D.signedAngle(1, 45, -5, 18));
	}

	@Test
	public final void staticAngleOfVectorDoubleDoubleDoubleDouble() {
		double v1x = getRandom().nextDouble();
		double v1y = getRandom().nextDouble();
		double v2x = getRandom().nextDouble();
		double v2y = getRandom().nextDouble();

		assertEpsilonEquals(
				0.,
				Vector3D.signedAngle(v1x, v1y, v1x, v1y));
		assertEpsilonEquals(
				0.,
				Vector3D.signedAngle(v2x, v2y, v2x, v2y));

		double sAngle1 = Vector3D.signedAngle(v1x, v1y, v2x, v2y);
		double sAngle2 = Vector3D.signedAngle(v2x, v2y, v1x, v1y);

		assertEpsilonEquals(-sAngle1, sAngle2);

		double sin = v1x * v2y - v1y * v2x;

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@Test
	public final void staticAngleOfVectorDoubleDouble() {
		assertEpsilonEquals(Math.acos(1. / Math.sqrt(5)), Vector3D.angleOfVector(1, 2));
		assertEpsilonEquals(PI / 2. + Math.acos(1 / Math.sqrt(5)), Vector3D.angleOfVector(-2, 1));
		assertEpsilonEquals(PI / 4., Vector3D.angleOfVector(1, 1));
	}

	@Test
	public final void dotVector3D() {
		Vector3D vector = createVector(1, 2);
		Vector3D vector2 = createVector(3, 4);
		Vector3D vector3 = createVector(1, -2);

		assertEpsilonEquals(5,vector.dot(vector));
		assertEpsilonEquals(11,vector.dot(vector2));
		assertEpsilonEquals(-3,vector.dot(vector3));
	}

	@Test
	public final void perpVector3D() {
		Vector3D vector = createVector(1,2);
		Vector3D vector2 = createVector(3,4);
		Vector3D vector3 = createVector(1,-2);

		assertEpsilonEquals(0, vector.perp(vector));
		assertEpsilonEquals(-2, vector.perp(vector2));
		assertEpsilonEquals(-4, vector.perp(vector3));
	}

	@Test
	public final void length() {
		Vector3D vector = createVector(1, 2);
		Vector3D vector2 = createVector(0, 0);
		Vector3D vector3 = createVector(-1, 1);

		assertEpsilonEquals(Math.sqrt(5),vector.getLength());
		assertEpsilonEquals(0,vector2.getLength());
		assertEpsilonEquals(Math.sqrt(2),vector3.getLength());
	}

	@Test
	public final void lengthSquared_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(1, 2);
		Vector3D vector2 = createVector(0, 0);
		Vector3D vector3 = createVector(Math.sqrt(2.) / 2., Math.sqrt(2.) / 2.);

		assertEpsilonEquals(5,vector.getLengthSquared());
		assertEpsilonEquals(0,vector2.getLengthSquared());
		assertEpsilonEquals(1,vector3.getLengthSquared());
	}

	@Test
	public final void lengthSquared_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(1, 2);
		Vector3D vector2 = createVector(0, 0);
		Vector3D vector3 = createVector(Math.sqrt(2.) / 2., Math.sqrt(2.) / 2.);

		assertEpsilonEquals(5,vector.getLengthSquared());
		assertEpsilonEquals(0,vector2.getLengthSquared());
		assertEpsilonEquals(2,vector3.getLengthSquared());
	}

	@Test
	public final void angleVector3D() {
		Vector3D vector = createVector(1, 2);
		Vector3D vector2 = createVector(-2, 1);
		Vector3D vector3 = createVector(1, 1);
		Vector3D vector4 = createVector(1, 0);

		assertEpsilonEquals(PI/2f,vector.angle(vector2));
		assertEpsilonEquals(PI/4f,vector4.angle(vector3));
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.angle(vector));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.angle(vector2));
		assertEpsilonEquals(0,vector.angle(vector));
	}

	@Test
	public final void signedAngleVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble());
		Vector3D v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble());

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

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@Test
	public final void signedAngleVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
		Vector3D v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);

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

	@Test
	public final void isUnitVector_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		assertFalse(createVector(7.15161,6.7545).isUnitVector());
		assertTrue(createVector(0,-1).isUnitVector());
		assertTrue((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2)).isUnitVector());
		assertTrue((createVector(1,0)).isUnitVector()); 
	}

	@Test
	public final void isUnitVector_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		assertFalse(createVector(7.15161,6.7545).isUnitVector());
		assertTrue(createVector(0,-1).isUnitVector());
		assertFalse(createVector(0.72700, 0.68663).isUnitVector());
		assertFalse((createVector(Math.sqrt(2)/2,Math.sqrt(2)/2)).isUnitVector());
		assertTrue((createVector(1,0)).isUnitVector()); 
	}

	@Test
	public final void isOrthogonal() {
		Vector3D v = createVector(1, 0);
		assertFalse(v.isOrthogonal(createVector(1., 0)));
		assertFalse(v.isOrthogonal(createVector(-1., 0)));
		assertTrue(v.isOrthogonal(createVector(0., 1)));
		assertTrue(v.isOrthogonal(createVector(0., -1)));
		assertFalse(v.isOrthogonal(createVector(1., 2)));
		assertTrue(v.isOrthogonal(createVector(0, 1 + Math.ulp(1))));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void toUnmodifiable_exception() {
		Vector3D origin = createVector(2, 3);
		Vector3D immutable = origin.toUnmodifiable();
		assertEpsilonEquals(origin, immutable);
		immutable.add(1, 2);
	}

	@Test
	public final void toUnmodifiable_changeInOrigin() {
		Vector3D origin = createVector(2, 3);
		assumeMutable(origin);
		Vector3D immutable = origin.toUnmodifiable();
		assertEpsilonEquals(origin, immutable);
		origin.add(1, 2);
		assertEpsilonEquals(origin, immutable);
	}

	@Test
	public final void testCloneVector() {
		Vector3D origin = createVector(23, 45);
		Tuple3D clone = origin.clone();
		assertNotNull(clone);
		assertNotSame(origin, clone);
		assertEpsilonEquals(origin.getX(), clone.getX());
		assertEpsilonEquals(origin.getY(), clone.getY());
	}

	@Test
	public final void toUnitVector_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D origin = createVector(23, 45);
		Vector3D unitVector = origin.toUnitVector();
		assertNotNull(unitVector);
		assertNotSame(origin, unitVector);
		assertEpsilonEquals(.45511, unitVector.getX());
		assertEpsilonEquals(.89043, unitVector.getY());
	}

	@Test
	public final void toUnitVector_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D origin = createVector(23, 45);
		Vector3D unitVector = origin.toUnitVector();
		assertNotNull(unitVector);
		assertNotSame(origin, unitVector);
		assertEpsilonEquals(0, unitVector.getX());
		assertEpsilonEquals(1, unitVector.getY());
		//
		origin = createVector(-45, 0);
		unitVector = origin.toUnitVector();
		assertNotNull(unitVector);
		assertNotSame(origin, unitVector);
		assertEpsilonEquals(-1, unitVector.getX());
		assertEpsilonEquals(0, unitVector.getY());
	}

	@Test
	public final void toOrthogonalVector() {
		Vector3D origin = createVector(23, 45);
		Vector3D orthoVector = origin.toOrthogonalVector();
		assertNotNull(orthoVector);
		assertNotSame(origin, orthoVector);
		assertEpsilonEquals(-45, orthoVector.getX());
		assertEpsilonEquals(23, orthoVector.getY());
	}
	
	public final void assertEpsilonEquals(double expected, PowerResult<?> actual) {
		if (actual == null) {
			fail("Result is null");
			return;
		}
		if (actual.isVectorial()) {
			throw new ComparisonFailure("Not same result type", Double.toString(expected), actual.toString());
		}
		assertEpsilonEquals(expected, actual.getScalar());
	}

	public final void assertEpsilonEquals(double expectedX, double expectedY, PowerResult<?> actual) {
		if (actual == null) {
			fail("Result is null");
			return;
		}
		if (!actual.isVectorial()) {
			throw new ComparisonFailure("Not same result type", "[" + expectedX + ";" + expectedY + "]",
					actual.toString());
		}
		Vector3D<?, ?> vector = actual.getVector();
		assert (vector != null);
		if (!isEpsilonEquals(expectedX, vector.getX())
			|| !isEpsilonEquals(expectedY, vector.getY())) {
			throw new ComparisonFailure("Not same result type", "[" + expectedX + ";" + expectedY + "]",
					actual.toString());
		}
	}

	@Test
	public final void power_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D origin = createVector(23, 45);
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

	@Test
	public final void power_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D origin = createVector(23, 45);
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

	@Test
	public final void operator_equalsVector3D() {
		Vector3D vector = createVector(49, -2);
		assertFalse(vector.operator_equals(null));
		assertTrue(vector.operator_equals(vector));
		assertFalse(vector.operator_equals(createVector(49, -3)));
		assertFalse(vector.operator_equals(createVector(0, 0)));
		assertTrue(vector.operator_equals(createVector(49, -2)));
	}

	@Test
	public final void operator_notEqualsVector3D() {
		Vector3D vector = createVector(49, -2);
		assertTrue(vector.operator_notEquals(null));
		assertFalse(vector.operator_notEquals(vector));
		assertTrue(vector.operator_notEquals(createVector(49, -3)));
		assertTrue(vector.operator_notEquals(createVector(0, 0)));
		assertFalse(vector.operator_notEquals(createVector(49, -2)));
	}

	@Test
	public final void testEqualsObject() {
		Vector3D vector = createVector(49, -2);
		assertFalse(vector.equals((Object) null));
		assertTrue(vector.equals((Object) vector));
		assertFalse(vector.equals((Object) createVector(49, -3)));
		assertFalse(vector.equals((Object) createVector(0, 0)));
		assertTrue(vector.equals((Object) createVector(49, -2)));
	}

	@Test
	public final void operator_upToVector3D() {
		Vector3D vector = createVector(1, 2);
		Vector3D vector2 = createVector(-2, 1);
		Vector3D vector3 = createVector(1, 1);
		Vector3D vector4 = createVector(1, 0);

		assertEpsilonEquals(PI/2f,vector.operator_upTo(vector2));
		assertEpsilonEquals(PI/4f,vector4.operator_upTo(vector3));
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.operator_upTo(vector2));
		assertEpsilonEquals(0,vector.operator_upTo(vector));
	}
	
	@Test
	public final void operator_greaterThanDoubleDotVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble());
		Vector3D v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble());

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

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@Test
	public final void operator_greaterThanDoubleDotVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
		Vector3D v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);

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

	@Test
	public final void operator_operator_doubleDotLessThanVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextDouble(), getRandom().nextDouble());
		Vector3D v2 = createVector(getRandom().nextDouble(), getRandom().nextDouble());

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

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		} else {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		}
	}

	@Test
	public final void operator_operator_doubleDotLessThanVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D v1 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);
		Vector3D v2 = createVector(getRandom().nextInt(48) + 2, getRandom().nextInt(48) + 2);

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

	@Test
	public final void operator_minus() {
		Vector3D vect = createVector(45, -78);
		Vector3D result = vect.operator_minus();
		assertNotSame(vect, result);
		assertEpsilonEquals(-vect.getX(), result.getX());
		assertEpsilonEquals(-vect.getY(), result.getY());
	}

	@Test
	public final void operator_multiplyDouble() {
		Vector3D vect = createVector(45, -78);
		Vector3D result = vect.operator_multiply(5);
		assertNotSame(vect, result);
		assertEpsilonEquals(225, result.getX());
		assertEpsilonEquals(-390, result.getY());
	}

	@Test
	public final void operator_divideDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(45, -78);
		Vector3D result = vect.operator_divide(5);
		assertNotSame(vect, result);
		assertEquals(9, result.ix());
		assertEquals(-16, result.iy());
	}

	@Test
	public final void operator_divideDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(45, -78);
		Vector3D result = vect.operator_divide(5);
		assertNotSame(vect, result);
		assertEpsilonEquals(9, result.getX());
		assertEpsilonEquals(-15.6, result.getY());
	}

	@Test
	public final void operator_elvisVector3D() {
		Vector3D orig1 = createVector(45, -78);
		Vector3D orig2 = createVector(0, 0);
		Vector3D param = createVector(-5, -1.4);
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

	@Test
	public final void operator_minusVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(-1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(-2.0, -1.5);

		Vector3D r = vect.operator_minus(vector);
		assertFpVectorEquals(1.2, 1.2, r);

		r = vect2.operator_minus(vector2);
		assertFpVectorEquals(1.0, 1.5, r);
	}

	@Test
	public final void operator_minusVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(-1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(-2.0, -1.5);
		Vector3D r;

		r = vect.operator_minus(vector);
		assertIntVectorEquals(1, 1, r);

		r = vect2.operator_minus(vector2);
		assertIntVectorEquals(1, 1, r);
	}

	@Test
	public final void operator_plusVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(-1,0);
		Vector3D vector3 = createVector(1.2,1.2);
		Vector3D vector4 = createVector(2.0,1.5);

		Vector3D r = vector.operator_plus(vector3);
		assertFpVectorEquals(1.2, 1.2, r);

		r = vector2.operator_plus(vector4);
		assertFpVectorEquals(1., 1.5, r);
	}

	@Test
	public final void operator_plusVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(-1,0);
		Vector3D vector3 = createVector(1.2,1.2);
		Vector3D vector4 = createVector(2.0,1.5);

		Vector3D r = vector.operator_plus(vector3);
		assertIntVectorEquals(1, 1, r);

		r = vector2.operator_plus(vector4);
		assertIntVectorEquals(1, 2, r);
	}

	@Test
	public final void operator_powerVector3D() {
		Vector3D vector = createVector(1,2);
		Vector3D vector2 = createVector(3,4);
		Vector3D vector3 = createVector(1,-2);

		assertEpsilonEquals(0, vector.operator_power(vector));
		assertEpsilonEquals(-2, vector.operator_power(vector2));
		assertEpsilonEquals(-4, vector.operator_power(vector3));
	}

	@Test
	public final void operator_powerInteger_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D origin = createVector(23, 45);
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

	@Test
	public final void operator_powerInteger_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D origin = createVector(23, 45);
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

	@Test
	public final void operator_plusPoint3D() {
		Point3D point = createPoint(1, 2);
		Point3D point2 = createPoint(3, 0);
		Vector3D vector1 = createVector(0, 0);
		Vector3D vector2 = createVector(1, 2);
		Vector3D vector3 = createVector(1, -5);
		Point3D r;
		
		r = vector1.operator_plus(point);
		assertFpPointEquals(1, 2, r);

		r = vector2.operator_plus(point);
		assertFpPointEquals(2, 4, r);

		r = vector3.operator_plus(point);
		assertFpPointEquals(2, -3, r);

		r = vector1.operator_plus(point2);
		assertFpPointEquals(3, 0, r);

		r = vector2.operator_plus(point2);
		assertFpPointEquals(4, 2, r);

		r = vector3.operator_plus(point2);
		assertFpPointEquals(4, -5, r);
	}

	@Test
	public void addVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0, 0);
		Vector3D vector2 = createVector(-1, 0);
		Vector3D vector3 = createVector(1.2, 1.2);
		Vector3D vector4 = createVector(2.0, 1.5);
		Vector3D vector5 = createVector(0.0, 0.0);

		vector5.add(vector3,vector);
		assertFpVectorEquals(1.2, 1.2, vector5);

		vector5.add(vector4,vector2);
		assertFpVectorEquals(1.0, 1.5, vector5); 
	}

	@Test
	public void addVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0, 0);
		Vector3D vector2 = createVector(-1, 0);
		Vector3D vector3 = createVector(1.2, 1.2);
		Vector3D vector4 = createVector(2.0, 1.5);
		Vector3D vector5 = createVector(0.0, 0.0);

		vector5.add(vector3,vector);
		assertIntVectorEquals(1, 1, vector5);

		vector5.add(vector4,vector2);
		assertIntVectorEquals(1, 2, vector5); 
	}

	@Test
	public void addVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(-1,0);
		Vector3D vector3 = createVector(1.2,1.2);
		Vector3D vector4 = createVector(2.0,1.5);

		vector.add(vector3);
		assertFpVectorEquals(1.2, 1.2, vector);

		vector2.add(vector4);
		assertFpVectorEquals(1., 1.5, vector2);
	}

	@Test
	public void addVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(-1,0);
		Vector3D vector3 = createVector(1.2,1.2);
		Vector3D vector4 = createVector(2.0,1.5);

		vector.add(vector3);
		assertIntVectorEquals(1, 1, vector);

		vector2.add(vector4);
		assertIntVectorEquals(1, 2, vector2);
	}

	@Test
	public void scaleAddIntVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(-1,0);
		Vector3D vector2 = createVector(1.0,1.2);
		Vector3D vector3 = createVector(0.0,0.0);

		vector3.scaleAdd(0,vector2,vector);
		assertFpVectorEquals(-1, 0, vector3);

		vector3.scaleAdd(1,vector2,vector);
		assertFpVectorEquals(0.0, 1.2, vector3);

		vector3.scaleAdd(-1,vector2,vector);
		assertFpVectorEquals(-2.0, -1.2, vector3);

		vector3.scaleAdd(10,vector2,vector);
		assertFpVectorEquals(9, 12, vector3);
	}

	@Test
	public void scaleAddIntVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(-1,0);
		Vector3D vector2 = createVector(1.0,1.2);
		Vector3D vector3 = createVector(0.0,0.0);

		vector3.scaleAdd(0,vector2,vector);
		assertFpVectorEquals(-1, 0, vector3);

		vector3.scaleAdd(1,vector2,vector);
		assertFpVectorEquals(0, 1, vector3);

		vector3.scaleAdd(-1,vector2,vector);
		assertFpVectorEquals(-2, -1, vector3);

		vector3.scaleAdd(10,vector2,vector);
		assertFpVectorEquals(9, 10, vector3);
	}

	@Test
	public void scaleAddDoubleVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(1,0);
		Vector3D vector = createVector(-1,1);
		Vector3D newPoint = createVector(0.0,0.0);

		newPoint.scaleAdd(0.0, vector, vect);
		assertFpVectorEquals(1, 0, newPoint);

		newPoint.scaleAdd(1.5,vector, vect);
		assertFpVectorEquals(-0.5, 1.5, newPoint);

		newPoint.scaleAdd(-1.5,vector, vect);
		assertFpVectorEquals(2.5, -1.5, newPoint);

		newPoint.scaleAdd(0.1,vector, vect);
		assertFpVectorEquals(0.9, 0.1, newPoint);
	}

	@Test
	public void scaleAddDoubleVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(1,0);
		Vector3D vector = createVector(-1,1);
		Vector3D newPoint = createVector(0.0,0.0);

		newPoint.scaleAdd(0.0, vector, vect);
		assertIntVectorEquals(1, 0, newPoint);

		newPoint.scaleAdd(1.5,vector,vect);
		assertIntVectorEquals(0, 2, newPoint);

		newPoint.scaleAdd(-1.5,vector,vect);
		assertIntVectorEquals(3, -1, newPoint);

		newPoint.scaleAdd(0.1,vector,vect);
		assertIntVectorEquals(1, 0, newPoint);
	}

	@Test
	public void scaleAddIntVector3D() {
		Vector3D vector = createVector(1,0);
		Vector3D newPoint = createVector(0,0);

		newPoint.scaleAdd(0,vector);
		assertFpVectorEquals(1, 0, newPoint);

		newPoint.scaleAdd(1,vector);
		assertFpVectorEquals(2, 0, newPoint);

		newPoint.scaleAdd(-10,vector);
		assertFpVectorEquals(-19, 0, newPoint);
	}

	@Test
	public void scaleAddDoubleVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(1,0);
		Vector3D newPoint = createVector(0.0,0.0);

		newPoint.scaleAdd(0.5,vector);
		assertFpVectorEquals(1, 0, newPoint);

		newPoint.scaleAdd(1.2,vector);
		assertFpVectorEquals(2.2, 0.0, newPoint);

		newPoint.scaleAdd(-10,vector);
		assertFpVectorEquals(-21, 0, newPoint);
	}

	@Test
	public void scaleAddDoubleVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(1,0);
		Vector3D newPoint = createVector(0.0,0.0);

		newPoint.scaleAdd(0.5,vector);
		assertIntVectorEquals(1, 0, newPoint);

		newPoint.scaleAdd(1.2,vector);
		assertIntVectorEquals(2, 0, newPoint);

		newPoint.scaleAdd(-10,vector);
		assertIntVectorEquals(-19, 0, newPoint);
	}

	@Test
	public void subVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(2.0, 1.5);
		Vector3D newPoint = createVector(0.0, 0.0);

		newPoint.sub(vect,vector);
		assertFpVectorEquals(1.2, 1.2, newPoint);

		newPoint.sub(vect2,vector2);
		assertFpVectorEquals(-1.0, -1.5, newPoint); 
	}

	@Test
	public void subVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(2.0, 1.5);
		Vector3D newPoint = createVector(0.0, 0.0);

		newPoint.sub(vect,vector);
		assertIntVectorEquals(1, 1, newPoint);

		newPoint.sub(vect2,vector2);
		assertIntVectorEquals(-1, -2, newPoint); 
	}

	@Test
	public void subPoint3DPoint3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createPoint(0, 0);
		Point3D point2 = createPoint(1, 0);
		Point3D vector = createPoint(-1.2, -1.2);
		Point3D vector2 = createPoint(2.0, 1.5);
		Vector3D newPoint = createVector(0.0, 0.0);

		newPoint.sub(point,vector);
		assertFpVectorEquals(1.2, 1.2, newPoint);

		newPoint.sub(point2,vector2);
		assertFpVectorEquals(-1.0, -1.5, newPoint); 
	}

	@Test
	public void subPoint3DPoint3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createPoint(0, 0);
		Point3D point2 = createPoint(1, 0);
		Point3D vector = createPoint(-1.2, -1.2);
		Point3D vector2 = createPoint(2.0, 1.5);
		Vector3D newPoint = createVector(0.0, 0.0);

		newPoint.sub(point,vector);
		assertIntVectorEquals(1, 1, newPoint);

		newPoint.sub(point2,vector2);
		assertIntVectorEquals(-1, -2, newPoint); 
	}

	@Test
	public void subVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(-1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(-2.0, -1.5);

		vect.sub(vector);
		assertFpVectorEquals(1.2, 1.2, vect);

		vect2.sub(vector2);
		assertFpVectorEquals(1.0, 1.5, vect2);
	}

	@Test
	public void subVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(-1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(-2.0, -1.5);

		vect.sub(vector);
		assertIntVectorEquals(1, 1, vect);

		vect2.sub(vector2);
		assertIntVectorEquals(1, 1, vect2);
	}

	@Test
	public void makeOrthogonal() {
		Vector3D vector = createVector(1,2);
		Vector3D vector2 = createVector(0,0);
		Vector3D vector3 = createVector(1,1);
		Vector3D vector4 = createVector(1,0);

		vector.makeOrthogonal();
		vector2.makeOrthogonal();
		vector3.makeOrthogonal();
		vector4.makeOrthogonal();

		assertFpVectorEquals(-2, 1, vector);
		assertFpVectorNotEquals(2, -1, vector);
		assertFpVectorEquals(0, 0, vector2);
		assertFpVectorEquals(-1, 1, vector3);
		assertFpVectorEquals(0, 1, vector4);
	}

	@Test
	public void normalize_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(1,2);
		Vector3D vector2 = createVector(0,0);
		Vector3D vector3 = createVector(-1,1);

		vector.normalize();
		vector2.normalize();
		vector3.normalize();

		assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), vector);
		assertZero(vector2.getX());
		assertZero(vector2.getY());
		assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), vector3);
	}

	@Test  
	public void normalize_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(1,2);
		Vector3D vector2 = createVector(0,0);
		Vector3D vector3 = createVector(-1,1);
		Vector3D vector4 = createVector(0,-5);

		vector.normalize();
		vector2.normalize();
		vector3.normalize();
		vector4.normalize();

		assertIntVectorEquals(0, 1, vector);
		assertIntVectorEquals(0, 0, vector2);
		assertIntVectorEquals(-1, 1, vector3);
		assertIntVectorEquals(0, -1, vector4);
	}

	@Test
	public void normalizeVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(0,0);
		Vector3D vector3 = createVector(0,0);

		vector.normalize(createVector(1,2));
		vector2.normalize(createVector(0,0));
		vector3.normalize(createVector(-1,1));

		assertFpVectorEquals(1/Math.sqrt(5),2/Math.sqrt(5), vector);
		assertZero(vector2.getX());
		assertZero(vector2.getY());
		assertFpVectorEquals(-1/Math.sqrt(2),1/Math.sqrt(2), vector3);
	}

	@Test  
	public void normalizeVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(0,0);
		Vector3D vector3 = createVector(0,0);
		Vector3D vector4 = createVector(0,0);

		vector.normalize(createVector(1,2));
		vector2.normalize(createVector(0,0));
		vector3.normalize(createVector(-1,1));
		vector4.normalize(createVector(0,-5));

		assertIntVectorEquals(0, 1, vector);
		assertIntVectorEquals(0, 0, vector2);
		assertIntVectorEquals(-1, 1, vector3);
		assertIntVectorEquals(0, -1, vector4);
	}

	@Test
	public void turn_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turn(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turn(MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turn(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turn(MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turn(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turn(-MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turn(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turn(-MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turn(-MathConstants.DEMI_PI/3);
		assertFpVectorEquals(1.732, -1, vector); 
	}

	@Test
	public void turn_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turn(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turn(MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turn(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turn(MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turn(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turn(-MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turn(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turn(-MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turn(-MathConstants.DEMI_PI/3);
		assertIntVectorEquals(2, -1, vector); 
	}

	@Test
	public void turnVector_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turn(MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turn(-MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turn(-MathConstants.DEMI_PI/3, vector2);
		assertFpVectorEquals(1.732, -1, vector1); 
	}

	@Test
	public void turnVector_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turn(MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turn(-MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turn(-MathConstants.DEMI_PI/3, vector2);
		assertIntVectorEquals(2, -1, vector1); 
	}

	@Test
	public void turnLeft_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI/3);
		assertFpVectorEquals(1.732, -1, vector); 
	}

	@Test
	public void turnLeft_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI/3);
		assertFpVectorEquals(1.732, 1, vector); 
	}

	@Test
	public void turnLeft_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI/3);
		assertIntVectorEquals(2, -1, vector); 
	}

	@Test
	public void turnLeft_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnLeft(MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnLeft(-MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnLeft(-MathConstants.DEMI_PI/3);
		assertIntVectorEquals(2, 1, vector); 
	}

	@Test
	public void turnRight_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI/3);
		assertFpVectorEquals(1.732, 1, vector); 
	}

	@Test
	public void turnRight_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, -2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(-2, 0, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(0, 2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertFpVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI/3);
		assertFpVectorEquals(1.732, -1, vector); 
	}

	@Test
	public void turnRight_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI/3);
		assertIntVectorEquals(2, 1, vector); 
	}

	@Test
	public void turnRight_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector;
		
		vector = createVector(2, 0);
		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnRight(MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, -2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(-2, 0, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(0, 2, vector); 

		vector.turnRight(-MathConstants.DEMI_PI);
		assertIntVectorEquals(2, 0, vector); 

		vector = createVector(2, 0);
		vector.turnRight(-MathConstants.DEMI_PI/3);
		assertIntVectorEquals(2, -1, vector); 
	}

	@Test
	public void turnLeftVector_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
		assertFpVectorEquals(1.732, -1, vector1); 
	}

	@Test
	public void turnLeftVector_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
		assertFpVectorEquals(1.732, 1, vector1); 
	}

	@Test
	public void turnLeftVector_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
		assertIntVectorEquals(2, -1, vector1); 
	}

	@Test
	public void turnLeftVector_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnLeft(-MathConstants.DEMI_PI/3, vector2);
		assertIntVectorEquals(2, 1, vector1); 
	}

	@Test
	public void turnRightVector_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
		assertFpVectorEquals(1.732, 1, vector1); 
	}

	@Test
	public void turnRightVector_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI, vector2);
		assertFpVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
		assertFpVectorEquals(1.732, -1, vector1); 
	}

	@Test
	public void turnRightVector_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
		assertIntVectorEquals(2, 1, vector1); 
	}

	@Test
	public void turnRightVector_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded());
		Vector3D vector1;
		Vector3D vector2;
		
		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, 2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI, vector2);
		assertIntVectorEquals(0, -2, vector1); 

		vector1 = createVector(Double.NaN, Double.NaN);
		vector2 = createVector(2, 0);
		vector1.turnRight(-MathConstants.DEMI_PI/3, vector2);
		assertIntVectorEquals(2, -1, vector1); 
	}

	@Test
	public void setLength_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(getRandom().nextDouble(), getRandom().nextDouble());
		Vector3D vector2 = createVector(0,0);
		Vector3D oldVector = (Vector3D) vector.clone();
		
		double newLength = getRandom().nextDouble();
		
		vector.setLength(newLength);
		vector2.setLength(newLength);
		
		assertEpsilonEquals(vector.angle(oldVector), 0);
		assertEpsilonEquals(vector.getLength()*oldVector.getLength()/newLength,oldVector.getLength());
		assertFpVectorEquals(newLength,0, vector2);
	}

	@Test
	public void setLength_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0, 2);
		Vector3D vector2 = createVector(0, 0);
		
		int newLength = 5;
		
		vector.setLength(newLength);
		vector2.setLength(newLength);
		
		assertIntVectorEquals(0, newLength, vector);
		assertIntVectorEquals(newLength, 0, vector2);
	}

	@Test
	public void operator_addVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(-1,0);
		Vector3D vector3 = createVector(1.2,1.2);
		Vector3D vector4 = createVector(2.0,1.5);

		vector.operator_add(vector3);
		assertFpVectorEquals(1.2, 1.2, vector);

		vector2.add(vector4);
		assertFpVectorEquals(1., 1.5, vector2);
	}

	@Test
	public void operator_addVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0,0);
		Vector3D vector2 = createVector(-1,0);
		Vector3D vector3 = createVector(1.2,1.2);
		Vector3D vector4 = createVector(2.0,1.5);

		vector.operator_add(vector3);
		assertIntVectorEquals(1, 1, vector);

		vector2.operator_add(vector4);
		assertIntVectorEquals(1, 2, vector2);
	}

	@Test
	public void operator_removeVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(-1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(-2.0, -1.5);

		vect.operator_remove(vector);
		assertFpVectorEquals(1.2, 1.2, vect);

		vect2.operator_remove(vector2);
		assertFpVectorEquals(1.0, 1.5, vect2);
	}

	@Test
	public void operator_removeVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0);
		Vector3D vect2 = createVector(-1, 0);
		Vector3D vector = createVector(-1.2, -1.2);
		Vector3D vector2 = createVector(-2.0, -1.5);

		vect.operator_remove(vector);
		assertIntVectorEquals(1, 1, vect);

		vect2.operator_remove(vector2);
		assertIntVectorEquals(1, 1, vect2);
	}

}

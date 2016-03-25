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
package org.arakhne.afc.math.geometry.d2;

import static org.arakhne.afc.math.MathConstants.PI;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableVector2DTest extends AbstractUnmodifiableTuple2DTest {

	protected abstract Vector2D createVector(double x, double y);

	protected abstract Point2D createPoint(double x, double y);

	@Override
	protected Tuple2D createTuple(int x, int y) {
		return createVector(x, y);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addVector2DVector2D() {
		Vector2D vector = createVector(0, 0);
		Vector2D vector3 = createVector(1.2, 1.2);
		Vector2D vector5 = createVector(0.0, 0.0);
		vector5.add(vector3,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addVector2D() {
		Vector2D vector = createVector(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		vector.add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddIntVector2DVector2D() {
		Vector2D vector = createVector(-1,0);
		Vector2D vector2 = createVector(1.0,1.2);
		Vector2D vector3 = createVector(0.0,0.0);
		vector3.scaleAdd(0,vector2,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddDoubleVector2DVector2D() {
		Vector2D point = createVector(1,0);
		Vector2D vector = createVector(-1,1);
		Vector2D newPoint = createVector(0.0,0.0);
		newPoint.scaleAdd(0.0, vector, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddIntVector2D() {
		Vector2D vector = createVector(1,0);
		Vector2D newPoint = createVector(0,0);
		newPoint.scaleAdd(0,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddDoubleVector2D() {
		Vector2D vector = createVector(1,0);
		Vector2D newPoint = createVector(0.0,0.0);
		newPoint.scaleAdd(0.5,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subVector2DVector2D() {
		Vector2D point = createVector(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		Vector2D newPoint = createVector(0.0, 0.0);
		newPoint.sub(point,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subPoint2DPoint2D() {
		Point2D point = createPoint(0, 0);
		Point2D vector = createPoint(-1.2, -1.2);
		Vector2D newPoint = createVector(0.0, 0.0);
		newPoint.sub(point,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subVector2D() {
		Vector2D point = createVector(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		point.sub(vector);
	}

	@Test
	public void dotVector2D() {
		Vector2D vector = createVector(1, 2);
		Vector2D vector2 = createVector(3, 4);
		Vector2D vector3 = createVector(1, -2);

		assertEpsilonEquals(5,vector.dot(vector));
		assertEpsilonEquals(11,vector.dot(vector2));
		assertEpsilonEquals(-3,vector.dot(vector3));
	}

	@Test
	public void perpVector2D() {
		Vector2D vector = createVector(1,2);
		Vector2D vector2 = createVector(3,4);
		Vector2D vector3 = createVector(1,-2);

		assertEpsilonEquals(0, vector.perp(vector));
		assertEpsilonEquals(-2, vector.perp(vector2));
		assertEpsilonEquals(-4, vector.perp(vector3));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void perpendicularize() {
		Vector2D vector = createVector(1,2);
		vector.perpendicularize();
	}

	@Test
	public void length() {
		Vector2D vector = createVector(1, 2);
		Vector2D vector2 = createVector(0, 0);
		Vector2D vector3 = createVector(-1, 1);

		assertEpsilonEquals(Math.sqrt(5),vector.length());
		assertEpsilonEquals(0,vector2.length());
		assertEpsilonEquals(Math.sqrt(2),vector3.length());
	}

	@Test
	public void lengthSquared() {
		Vector2D vector = createVector(1, 2);
		Vector2D vector2 = createVector(0, 0);
		Vector2D vector3 = createVector(Math.sqrt(2.) / 2., Math.sqrt(2.) / 2.);

		assertEpsilonEquals(5,vector.lengthSquared());
		assertEpsilonEquals(0,vector2.lengthSquared());
		assertEpsilonEquals(1,vector3.lengthSquared());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void normalizeVector2D() {
		Vector2D vector = createVector(1, 2);
		vector.normalize(vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void normalize() {
		Vector2D vector = createVector(1,2);
		vector.normalize();
	}

	@Test
	public void angleVector2D() {
		Vector2D vector = createVector(1, 2);
		Vector2D vector2 = createVector(-2, 1);
		Vector2D vector3 = createVector(1, 1);
		Vector2D vector4 = createVector(1, 0);

		assertEpsilonEquals(PI/2f,vector.angle(vector2));
		assertEpsilonEquals(PI/4f,vector4.angle(vector3));
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.angle(vector));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.angle(vector2));
		assertEpsilonEquals(0,vector.angle(vector));
	}

	@Test
	public void signedAngleVector2D() {
		Vector2D v1 = createVector(this.random.nextDouble(), this.random.nextDouble());
		Vector2D v2 = createVector(this.random.nextDouble(), this.random.nextDouble());

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

	@Test(expected = UnsupportedOperationException.class)
	public void turn() {
		Vector2D vector = createVector(this.random.nextDouble(), this.random.nextDouble());
		double angle = this.random.nextDouble();
		vector.turn(angle);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void turnLeft() {
		Vector2D vector = createVector(this.random.nextDouble(), this.random.nextDouble());
		double angle = this.random.nextDouble();
		vector.turnLeft(angle);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void turnRight() {
		Vector2D vector = createVector(this.random.nextDouble(), this.random.nextDouble());
		double angle = this.random.nextDouble();
		vector.turnRight(angle);
	}

	@Test
	public void isUnitVector() {
		Vector2D vector = createVector(7.15161,6.7545);
		assertFalse(vector.isUnitVector());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setLength() {
		Vector2D vector = createVector(this.random.nextDouble(), this.random.nextDouble());
		double newLength = this.random.nextDouble();
		vector.setLength(newLength);
	}

	public void toUnmodifiable() {
		Vector2D origin = createVector(2, 3);
		Vector2D immutable = origin.toUnmodifiable();
		assertSame(origin, immutable);
	}

}

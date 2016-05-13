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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.Vector2D.PowerResult;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.Assume;
import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableVector2DTest<V extends Vector2D<? super V, ? super P>, P extends Point2D<? super P, ? super V>>
		extends AbstractVector2DTest<V, P, Vector2D> {

	@Test(expected = UnsupportedOperationException.class)
	public final void absolute() {
		getT().absolute();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void absoluteT() {
		Tuple2D c = new Tuple2d();
		getT().absolute(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addIntInt() {
		getT().add(6, 7);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble() {
		getT().add(6.5, 7.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXInt() {
		getT().addX(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble() {
		getT().addX(6.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYInt() {
		getT().addY(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble() {
		getT().addY(6.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void negateT() {
		Tuple2D c = new Tuple2d();
		getT().negate(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void negate() {
		getT().negate();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleIntT() {
		Tuple2D c = new Tuple2d(2, -1);
		getT().scale(4, c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT() {
		Tuple2D c = new Tuple2d(2, -1);
		getT().scale(4.5, c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleInt() {
		getT().scale(4);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble() {
		getT().scale(4.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setTuple2D() {
		Tuple2D c = new Tuple2d(-45, 78);
		getT().set(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setIntInt() {
		getT().set(-45, 78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble() {
		getT().set(-45.5, 78.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setIntArray() {
		getT().set(new int[]{-45, 78});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray() {
		getT().set(new double[]{-45.5, 78.5});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXInt() {
		getT().setX(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble() {
		getT().setX(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYInt() {
		getT().setY(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble() {
		getT().setY(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subIntInt() {
		getT().sub(45, 78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXInt() {
		getT().subX(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYInt() {
		getT().subY(78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble() {
		getT().sub(45.5, 78.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble() {
		getT().subX(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble() {
		getT().subY(78.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().add(12.3, 4.56);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().add(12.3, 4.56);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().scale(12.3, createTuple(1,2));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().scale(12.3, createTuple(1,2));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().scale(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().scale(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(12.3, 4.56);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(12.3, 4.56);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(new double[] {12.3, 4.56});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(new double[] {12.3, 4.56});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().sub(12.3, 4.56);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().sub(12.3, 4.56);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector2DVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createVector(0, 0);
		Vector2D vector3 = createVector(1.2, 1.2);
		Vector2D vector5 = createTuple(0.0, 0.0);
		vector5.add(vector3,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector2DVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createVector(0, 0);
		Vector2D vector3 = createVector(1.2, 1.2);
		Vector2D vector5 = createTuple(0.0, 0.0);
		vector5.add(vector3,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		vector.add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		vector.add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector2DVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createVector(-1,0);
		Vector2D vector2 = createVector(1.0,1.2);
		Vector2D vector3 = createTuple(0.0,0.0);
		vector3.scaleAdd(0,vector2,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector2DVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createVector(-1,0);
		Vector2D vector2 = createVector(1.0,1.2);
		Vector2D vector3 = createTuple(0.0,0.0);
		vector3.scaleAdd(0,vector2,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2DVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vect = createVector(1,0);
		Vector2D vector = createVector(-1,1);
		Vector2D newVector = createTuple(0.0,0.0);
		newVector.scaleAdd(0.0, vector, vect);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2DVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vect = createVector(1,0);
		Vector2D vector = createVector(-1,1);
		Vector2D newVector = createTuple(0.0,0.0);
		newVector.scaleAdd(0.0, vector, vect);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector2D() {
		Vector2D vector = createVector(1,0);
		Vector2D newVector = createTuple(0,0);
		newVector.scaleAdd(0,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createVector(1,0);
		Vector2D newVector = createTuple(0.0,0.0);
		newVector.scaleAdd(0.5,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createVector(1,0);
		Vector2D newTuple = createTuple(0.0,0.0);
		newTuple.scaleAdd(0.5,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector2DVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vect = createVector(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		Vector2D newVector = createTuple(0.0, 0.0);
		newVector.sub(vect,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector2DVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vect = createVector(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		Vector2D newVector = createTuple(0.0, 0.0);
		newVector.sub(vect,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subPoint2DPoint2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createPoint(0, 0);
		Point2D vector = createPoint(-1.2, -1.2);
		Vector2D newVector = createTuple(0.0, 0.0);
		newVector.sub(point,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subPoint2DPoint2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createPoint(0, 0);
		Point2D vector = createPoint(-1.2, -1.2);
		Vector2D newPoint = createTuple(0.0, 0.0);
		newPoint.sub(point,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		vect.sub(vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		vect.sub(vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void makeOrthogonal() {
		Vector2D vector = createTuple(1,2);
		vector.makeOrthogonal();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void normalize_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(1,2);
		vector.normalize();
	}

	@Test(expected = UnsupportedOperationException.class)  
	public final void normalize_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(1,2);
		vector.normalize();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void normalizeVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		vector.normalize(createVector(1,2));
	}

	@Test(expected = UnsupportedOperationException.class)  
	public final void normalizeVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		vector.normalize(createVector(1,2));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turn_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().turn(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turn_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().turn(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnVector_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turn(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnVector_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turn(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeft_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		getT().turnLeft(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeft_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnLeft(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeft_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		getT().turnLeft(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeft_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnLeft(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRight_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		getT().turnRight(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRight_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnRight(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRight_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		getT().turnRight(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRight_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnRight(MathConstants.DEMI_PI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftVector_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnLeft(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftVector_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnLeft(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftVector_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnLeft(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftVector_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnLeft(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightVector_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnRight(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightVector_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnRight(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightVector_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnRight(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightVector_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		getT().turnRight(MathConstants.DEMI_PI, vector2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setLength_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(getRandom().nextDouble(), getRandom().nextDouble());
		double newLength = getRandom().nextDouble();
		vector.setLength(newLength);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setLength_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0, 2);
		int newLength = 5;
		vector.setLength(newLength);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_addVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		vector.operator_add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_addVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		vector.operator_add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_removeVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		vect.operator_remove(vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_removeVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		vect.operator_remove(vector);
	}

}

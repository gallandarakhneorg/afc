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

import static org.junit.Assert.*;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiablePoint2DTest<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>>
		extends AbstractPoint2DTest<P, V, Point2D> {
	
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
	public final void addPoint2DVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.add(point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector2DPoint2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.add(vector1, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.add(vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2DPoint2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, vector1, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2DPoint2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, vector1, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector2DPoint2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2, vector1, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntPoint2DVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2, point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoublePoint2DVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoublePoint2DVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subPoint2DVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.sub(point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.sub(vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_addVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.operator_add(vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_removeVector2D() {
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.operator_remove(vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().turn(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDoublePoint2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().turn(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDoublePoint2DPoint2D_origin_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D origin = createPoint(0, 0);
		getT().turn(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDoublePoint2DPoint2D_aroundP_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D origin = createPoint(-45, 12);
		getT().turn(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDouble_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnLeft(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDouble_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnLeft(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2D_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnLeft(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2D_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnLeft(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDouble_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnRight(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDouble_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnRight(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2D_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnRight(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2D_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnRight(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_origin_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_origin_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().turn(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDoublePoint2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().turn(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDoublePoint2DPoint2D_origin_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D origin = createPoint(0, 0);
		getT().turn(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnDoublePoint2DPoint2D_aroundP_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D origin = createPoint(-45, 12);
		getT().turn(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDouble_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnLeft(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDouble_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnLeft(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2D_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnLeft(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2D_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnLeft(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDouble_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		getT().turnRight(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDouble_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnRight(Math.PI/2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2D_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnRight(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2D_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		getT().turnRight(Math.PI/2, createPoint(1, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_origin_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_origin_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
	}

}

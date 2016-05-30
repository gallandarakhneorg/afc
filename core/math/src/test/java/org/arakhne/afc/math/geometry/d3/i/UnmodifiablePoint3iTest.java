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
package org.arakhne.afc.math.geometry.d3.i;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.geometry.d3.AbstractUnmodifiablePoint3DTest;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;

@SuppressWarnings("all")
public class UnmodifiablePoint3iTest extends AbstractUnmodifiablePoint3DTest<Point3i, Vector3i> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Point3D createTuple(double x, double y, double z) {
		return new Point3i(x, y, z).toUnmodifiable();
	}
	
	@Override
	public Vector3i createVector(double x, double y, double z) {
		return new Vector3i(x, y, z);
	}

	@Override
	public Point3i createPoint(double x, double y, double z) {
		return new Point3i(x, y, z);
	}

	@Override
	public void operator_andShape3D() {
		Shape3D shape = new Sphere3i(5, 8, 5);
		assertFalse(createPoint(0,0).operator_and(shape));
		assertFalse(createPoint(11,10).operator_and(shape));
		assertFalse(createPoint(11,50).operator_and(shape));
		assertFalse(createPoint(9,12).operator_and(shape));
		assertTrue(createPoint(9,11).operator_and(shape));
		assertTrue(createPoint(8,12).operator_and(shape));
		assertTrue(createPoint(3,7).operator_and(shape));
		assertFalse(createPoint(10,11).operator_and(shape));
		assertTrue(createPoint(9,10).operator_and(shape));
	}
	
	@Override
	public void operator_upToShape3D() {
		Shape3D shape = new Sphere3i(5, 8, 5);
		assertEpsilonEquals(0f, createPoint(5,8).operator_upTo(shape));
		assertEpsilonEquals(0f, createPoint(10,10).operator_upTo(shape));
		assertEpsilonEquals(0f, createPoint(4,8).operator_upTo(shape));
		assertEpsilonEquals(4.242640687f, createPoint(0,0).operator_upTo(shape));
		assertEpsilonEquals(1f, createPoint(5,14).operator_upTo(shape));
	}

}

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
package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.geometry.d3.AbstractVector3DTest;
import org.junit.Test;

@SuppressWarnings("all")
public class Vector3dTest extends AbstractVector3DTest<Vector3d, Point3d, Vector3d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector3d createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public Point3d createPoint(double x, double y, double z) {
		return new Point3d(x, y, z);
	}

	@Override
	public Vector3d createTuple(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Test
	public void staticToOrientationVector() {
		assertFpVectorEquals(1, 0, Vector2d.toOrientationVector(0));
		assertFpVectorEquals(-1, 0, Vector2d.toOrientationVector(Math.PI));
		assertFpVectorEquals(0, 1, Vector2d.toOrientationVector(Math.PI/2));
		assertFpVectorEquals(0, -1, Vector2d.toOrientationVector(-Math.PI/2));
	}

}

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
package org.arakhne.afc.math.geometry.d2.d;

import org.arakhne.afc.math.geometry.d2.AbstractVector2DTest;
import org.junit.Test;

@SuppressWarnings("all")
public class Vector2dTest extends AbstractVector2DTest<Vector2d, Point2d, Vector2d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector2d createVector(double x, double y) {
		return new Vector2d(x, y);
	}

	@Override
	public Point2d createPoint(double x, double y) {
		return new Point2d(x, y);
	}

	@Override
	public Vector2d createTuple(double x, double y) {
		return new Vector2d(x, y);
	}

	@Test
	public void staticToOrientationVector() {
		assertFpVectorEquals(1, 0, Vector2d.toOrientationVector(0));
		assertFpVectorEquals(-1, 0, Vector2d.toOrientationVector(Math.PI));
		assertFpVectorEquals(0, 1, Vector2d.toOrientationVector(Math.PI/2));
		assertFpVectorEquals(0, -1, Vector2d.toOrientationVector(-Math.PI/2));
	}

}
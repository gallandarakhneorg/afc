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
package org.arakhne.afc.math.geometry.d2.dfx;

import static org.junit.Assert.assertNotNull;

import org.arakhne.afc.math.geometry.d2.AbstractVector2DTest;
import org.junit.Test;

import javafx.beans.property.ReadOnlyDoubleProperty;

@SuppressWarnings("all")
public class Vector2dfxTest extends AbstractVector2DTest<Vector2dfx, Point2dfx, Vector2dfx> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector2dfx createTuple(double x, double y) {
		return new Vector2dfx(x, y);
	}
	
	@Override
	public Vector2dfx createVector(double x, double y) {
		return new Vector2dfx(x, y);
	}

	@Override
	public Point2dfx createPoint(double x, double y) {
		return new Point2dfx(x, y);
	}

	@Test
	public void staticToOrientationVector() {
		assertFpVectorEquals(1, 0, Vector2dfx.toOrientationVector(0));
		assertFpVectorEquals(-1, 0, Vector2dfx.toOrientationVector(Math.PI));
		assertFpVectorEquals(0, 1, Vector2dfx.toOrientationVector(Math.PI/2));
		assertFpVectorEquals(0, -1, Vector2dfx.toOrientationVector(-Math.PI/2));
	}

	@Test
	public void lengthProperty() {
		Vector2dfx vector = new Vector2dfx(1, 2);
		assertEpsilonEquals(2.23607, vector.getLength());
		
		ReadOnlyDoubleProperty property = vector.lengthProperty();
		assertNotNull(property);
		assertEpsilonEquals(2.23607, property.get());
		
		vector.set(4, -10);
		assertEpsilonEquals(10.77033, property.get());
	}

	@Test
	public void lengthSquaredProperty() {
		Vector2dfx vector = new Vector2dfx(1, 2);
		assertEpsilonEquals(5, vector.getLengthSquared());
		
		ReadOnlyDoubleProperty property = vector.lengthSquaredProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		vector.set(4, -10);
		assertEpsilonEquals(116, property.get());
	}

}

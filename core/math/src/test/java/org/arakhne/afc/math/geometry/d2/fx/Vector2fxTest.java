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
package org.arakhne.afc.math.geometry.d2.fx;

import static org.junit.Assert.assertNotNull;

import org.arakhne.afc.math.geometry.d2.AbstractVector2DTest;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.fx.Point2fx;
import org.arakhne.afc.math.geometry.d2.fx.Vector2fx;
import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;

@SuppressWarnings("all")
public class Vector2fxTest extends AbstractVector2DTest {

	@Override
	protected boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	protected Vector2D createVector(double x, double y) {
		return new Vector2fx(x, y);
	}

	@Override
	protected Point2D createPoint(double x, double y) {
		return new Point2fx(x, y);
	}

	@Override
	@Ignore
	public void staticGetOrientationAngle() {
		//
	}

	@Test
	public void lengthProperty() {
		Vector2fx vector = new Vector2fx(1, 2);
		assertEpsilonEquals(2.23607, vector.getLength());
		
		ReadOnlyDoubleProperty property = vector.lengthProperty();
		assertNotNull(property);
		assertEpsilonEquals(2.23607, property.get());
		
		vector.set(4, -10);
		assertEpsilonEquals(10.77033, property.get());
	}

	@Test
	public void lengthSquaredProperty() {
		Vector2fx vector = new Vector2fx(1, 2);
		assertEpsilonEquals(5, vector.getLengthSquared());
		
		ReadOnlyDoubleProperty property = vector.lengthSquaredProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		vector.set(4, -10);
		assertEpsilonEquals(116, property.get());
	}

}

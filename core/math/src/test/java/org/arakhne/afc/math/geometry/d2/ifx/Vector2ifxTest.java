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
package org.arakhne.afc.math.geometry.d2.ifx;

import static org.junit.Assert.assertNotSame;

import org.arakhne.afc.math.geometry.d2.AbstractVector2DTest;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("all")
public class Vector2ifxTest extends AbstractVector2DTest {

	@Override
	protected boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	protected Vector2D createVector(double x, double y) {
		return new Vector2ifx(x, y);
	}

	@Override
	protected Point2D createPoint(double x, double y) {
		return new Point2ifx(x, y);
	}

	@Override
	@Ignore
	public void staticGetOrientationAngle() {
		//
	}

	@Test
	public void testClone() {
		super.testClone();
		Vector2ifx origin = (Vector2ifx) createVector(23, 45);
		Vector2ifx clone = (Vector2ifx) origin.clone();
		assertNotSame(origin.xProperty(), clone.xProperty());
		assertNotSame(origin.yProperty(), clone.yProperty());
	}

}

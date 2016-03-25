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

import org.arakhne.afc.math.geometry.d2.AbstractPoint2DTest;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.Test;

@SuppressWarnings("all")
public class Point2ifxTest extends AbstractPoint2DTest {

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

	@Test
	public void testClone() {
		super.testClone();
		Point2ifx origin = (Point2ifx) createPoint(23, 45);
		Point2ifx clone = (Point2ifx) origin.clone();
		assertNotSame(origin.xProperty(), clone.xProperty());
		assertNotSame(origin.yProperty(), clone.yProperty());
	}

}

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

import org.arakhne.afc.math.geometry.d2.AbstractUnmodifiableVector2DTest;
import org.arakhne.afc.math.geometry.d2.Vector2D;

@SuppressWarnings("all")
public class UnmodifiableVector2ifxTest extends AbstractUnmodifiableVector2DTest<Vector2ifx, Point2ifx> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Vector2D createTuple(double x, double y) {
		return new Vector2ifx(x, y).toUnmodifiable();
	}
	
	@Override
	public Vector2ifx createVector(double x, double y) {
		return new Vector2ifx(x, y);
	}

	@Override
	public Point2ifx createPoint(double x, double y) {
		return new Point2ifx(x, y);
	}

}

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
package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.d2.AbstractVector2DTest;

@SuppressWarnings("all")
public class Vector2iTest extends AbstractVector2DTest<Vector2i, Point2i, Vector2i> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Vector2i createTuple(double x, double y) {
		return new Vector2i(x, y);
	}
	
	@Override
	public Vector2i createVector(double x, double y) {
		return new Vector2i(x, y);
	}

	@Override
	public Point2i createPoint(double x, double y) {
		return new Point2i(x, y);
	}

}
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
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.junit.Ignore;

@SuppressWarnings("all")
public class Vector2dTest extends AbstractVector2DTest {

	@Override
	protected boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	protected Vector2D createVector(double x, double y) {
		return new Vector2d(x, y);
	}

	@Override
	protected Point2D createPoint(double x, double y) {
		return new Point2d(x, y);
	}

	@Override
	@Ignore
	public void staticGetOrientationAngle() {
		//
	}

}

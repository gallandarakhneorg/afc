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

import org.arakhne.afc.math.geometry.d3.AbstractVector3DTest;

@SuppressWarnings("all")
public class Vector3iTest extends AbstractVector3DTest<Vector3i, Point3i, Vector3i> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Vector3i createTuple(double x, double y, double z) {
		return new Vector3i(x, y, z);
	}
	
	@Override
	public Vector3i createVector(double x, double y, double z) {
		return new Vector3i(x, y, z);
	}

	@Override
	public Point3i createPoint(double x, double y, double z) {
		return new Point3i(x, y, z);
	}

}

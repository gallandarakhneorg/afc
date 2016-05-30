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

import org.arakhne.afc.math.geometry.d3.AbstractUnmodifiableVector3DTest;
import org.arakhne.afc.math.geometry.d3.Vector3D;

@SuppressWarnings("all")
public class UnmodifiableVector3iTest extends AbstractUnmodifiableVector3DTest<Vector3i, Point3i> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Vector3D createTuple(double x, double y, double z) {
		return new Vector3i(x, y, z).toUnmodifiable();
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

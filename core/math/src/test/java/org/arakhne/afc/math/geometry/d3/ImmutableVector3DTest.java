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
package org.arakhne.afc.math.geometry.d3;

@SuppressWarnings("all")
public class ImmutableVector3DTest extends AbstractUnmodifiableVector3DTest<ImmutableVector3D, ImmutablePoint3D> {
	
	@Override
	public ImmutableVector3D createVector(final double tx, final double ty, final double tz) {
		return new ImmutableVector3D(tx, ty, tz);
	}

	@Override
	public ImmutablePoint3D createPoint(final double tx, final double ty, final double tz) {
		return new ImmutablePoint3D(tx, ty, tz);
	}

	@Override
	public Vector3D createTuple(double x, double y, double z) {
		return new ImmutableVector3D(x, y, z);
	}

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
}
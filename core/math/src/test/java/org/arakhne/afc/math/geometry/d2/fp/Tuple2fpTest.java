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
package org.arakhne.afc.math.geometry.d2.fp;

import org.arakhne.afc.math.geometry.d2.AbstractTuple2DTest;
import org.arakhne.afc.math.geometry.d2.Tuple2D;

@SuppressWarnings("all")
public class Tuple2fpTest extends AbstractTuple2DTest<Tuple2fp> {

	@Override
	protected boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	protected Tuple2fp createTuple(double x, double y) {
		return new Tuple2fp(x, y);
	}

}

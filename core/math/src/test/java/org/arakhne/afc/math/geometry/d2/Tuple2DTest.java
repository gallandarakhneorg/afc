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
package org.arakhne.afc.math.geometry.d2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.fp.Tuple2fp;
import org.arakhne.afc.math.geometry.d2.i.Tuple2i;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("all")
public class Tuple2DTest extends AbstractTuple2DTest<Tuple2D> {
	
	@Override
	protected boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	protected Tuple2D createTuple(final double tx, final double ty) {
		return new Tuple2D() {
			private double x = tx;
			private double y = ty;
			@Override
			public Tuple2D clone() {
				throw new UnsupportedOperationException();
			}

			@Override
			public double getX() {
				return this.x;
			}

			@Override
			public int ix() {
				return (int) this.x;
			}

			@Override
			public void setX(int x) {
				this.x = x;
			}

			@Override
			public void setX(double x) {
				this.x = x;
			}

			@Override
			public double getY() {
				return this.y;
			}

			@Override
			public int iy() {
				return (int) this.y;
			}

			@Override
			public void setY(int y) {
				this.y = y;
			}

			@Override
			public void setY(double y) {
				this.y = y;
			}
			
		};
	}

}

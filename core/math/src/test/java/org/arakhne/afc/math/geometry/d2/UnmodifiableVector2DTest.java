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

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("all")
public class UnmodifiableVector2DTest extends AbstractUnmodifiableVector2DTest {
	
	@Override
	protected Vector2D createVector(final double tx, final double ty) {
		return new UnmodifiableVector2D() {
			private double x = tx;
			private double y = ty;
			@Override
			public Vector2D clone() {
				return createVector(this.x, this.y);
			}
			
			@Override
			public GeomFactory getGeomFactory() {
				return ImmutableGeomFactory.SINGLETON;
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
			public double getY() {
				return this.y;
			}

			@Override
			public int iy() {
				return (int) this.y;
			}

			@Override
			public UnmodifiableVector2D toUnmodifiable() {
				return this;
			}

			@Override
			public Vector2D toUnitVector() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Vector2D toOrthogonalVector() {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	protected Point2D createPoint(final double tx, final double ty) {
		return new Point2DStub(tx, ty);
	}

}

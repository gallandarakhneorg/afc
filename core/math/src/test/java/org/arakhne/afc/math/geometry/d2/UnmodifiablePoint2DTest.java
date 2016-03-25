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

@SuppressWarnings("all")
public class UnmodifiablePoint2DTest extends AbstractUnmodifiablePoint2DTest {
	
	@Override
	protected Vector2D createVector(final double tx, final double ty) {
		return new Vector2D() {
			private double x = tx;
			private double y = ty;
			@Override
			public Vector2D clone() {
				return createVector(this.x, this.y);
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

			@Override
			public Vector2D toUnmodifiable() {
				return new ImVector2D();
			}

			class ImVector2D implements Vector2D {

				@Override
				public Vector2D clone() {
					return new ImVector2D();
				}

				@Override
				public double getX() {
					return x;
				}

				@Override
				public int ix() {
					return (int) x;
				}

				@Override
				public void setX(int x) {
					throw new UnsupportedOperationException();
				}

				@Override
				public void setX(double x) {
					throw new UnsupportedOperationException();
				}

				@Override
				public double getY() {
					return y;
				}

				@Override
				public int iy() {
					return (int) y;
				}

				@Override
				public void setY(int y) {
					throw new UnsupportedOperationException();
				}

				@Override
				public void setY(double y) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Vector2D toUnmodifiable() {
					return this;
				}
				
			}
		};
	}

	@Override
	protected Point2D createPoint(final double tx, final double ty) {
		return new UnmodifiablePoint2D() {
			private double x = tx;
			private double y = ty;
			@Override
			public Point2D clone() {
				return createPoint(this.x, this.y);
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
			public Point2D toUnmodifiable() {
				return this;
			}

		};
	}

}

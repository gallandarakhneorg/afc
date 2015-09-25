/**
 * 
 * fr.utbm.v3g.core.math.Vector2d.java
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
package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.concurrent.Callable;

import org.arakhne.afc.math.geometry.d2.FunctionalVector2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
@SuppressWarnings("restriction")
public class Vector2d extends Tuple2d<Vector2D>implements FunctionalVector2D {

	private static final long serialVersionUID = 2049284731039698587L;

	
	protected ReadOnlyDoubleWrapper lengthSquareProperty = new ReadOnlyDoubleWrapper();
	private ReadOnlyDoubleWrapper lengthProperty = new ReadOnlyDoubleWrapper();
	
	
	public Vector2d() {
		this(0d, 0d);
	}
	
	public Vector2d(Tuple2d<?> v) {
		this(v.getX(), v.getY());
	}
	
	public Vector2d(double x, double y) {
		super(x, y);
		
		this.lengthSquareProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return new Double(Vector2d.this.xProperty.doubleValue() * Vector2d.this.xProperty.doubleValue() + Vector2d.this.yProperty.doubleValue() * Vector2d.this.yProperty.doubleValue());
			}
		}, this.xProperty, this.yProperty));
		
		this.lengthProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return new Double(Math.sqrt(Vector2d.this.lengthSquareProperty.doubleValue()));
			}
		}, this.lengthSquareProperty));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2d clone() {
		return (Vector2d)super.clone();
	}

	@Override
	public Vector2D toUnmodifiable() {
		return new UnmodifiableVector2d();
	}

	
	public class UnmodifiableVector2d implements FunctionalVector2D.UnmodifiableVector2f {

		private static final long serialVersionUID = -1444735824393226579L;

		/**
		 */
		public UnmodifiableVector2d() {
			//
		}

		@Override
		public UnmodifiableVector2d clone() {
			return new UnmodifiableVector2d();
		}

		@Override
		public void get(Vector2D t) {
			Vector2d.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Vector2d.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Vector2d.this.get(t);
		}

		@Override
		public double getX() {
			return Vector2d.this.getX();
		}

		@Override
		public int ix() {
			return Vector2d.this.ix();
		}

		@Override
		public double getY() {
			return Vector2d.this.getY();
		}

		@Override
		public int iy() {
			return Vector2d.this.iy();
		}

		@Override
		public boolean equals(Tuple2D<?> t1) {
			return Vector2d.this.equals(t1);
		}

		@Override
		public int hashCode() {
			return Vector2d.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Vector2D t1, double epsilon) {
			return Vector2d.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public double dot(Vector2D v1) {
			return Vector2d.this.dot(v1);
		}

		@Override
		public double length() {
			return Vector2d.this.length();
		}

		@Override
		public double lengthSquared() {
			return Vector2d.this.lengthSquared();
		}

		@Override
		public double angle(Vector2D v1) {
			return Vector2d.this.angle(v1);
		}

		@Override
		public double signedAngle(Vector2D v) {
			return Vector2d.this.signedAngle(v);
		}

		@Override
		public double getOrientationAngle() {
			return Vector2d.this.getOrientationAngle();
		}

		@Override
		public boolean isUnitVector() {
			return Vector2d.this.isUnitVector();
		}

		@Override
		public Vector2D toUnmodifiable() {
			return this;
		}

		@Override
		public double perp(Vector2D x2) {
			return Vector2d.this.perp(x2);
		}

	}	
}

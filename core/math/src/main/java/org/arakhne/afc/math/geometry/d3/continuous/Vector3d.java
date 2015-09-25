/**
 * 
 * fr.utbm.v3g.core.math.Vector3d.java
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
package org.arakhne.afc.math.geometry.d3.continuous;

import java.util.concurrent.Callable;

import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
@SuppressWarnings("restriction")
public class Vector3d extends Tuple3d<Vector3D> implements FunctionalVector3D {
	
	private static final long serialVersionUID = -2885355291282425412L;

	
	protected ReadOnlyDoubleWrapper lengthSquareProperty = new ReadOnlyDoubleWrapper();
	private ReadOnlyDoubleWrapper lengthProperty = new ReadOnlyDoubleWrapper();
	
	
	public Vector3d() {
		this(0d, 0d, 0d);
	}
	
	public Vector3d(Tuple3d<?> v) {
		this(v.getX(), v.getY(), v.getZ());
	}
	
	public Vector3d(double x, double y, double z) {
		super(x, y, z);
		
		this.lengthSquareProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return new Double(Vector3d.this.xProperty.doubleValue() * Vector3d.this.xProperty.doubleValue() + Vector3d.this.yProperty.doubleValue() * Vector3d.this.yProperty.doubleValue() + Vector3d.this.zProperty.doubleValue() * Vector3d.this.zProperty.doubleValue());
			}
		}, this.xProperty, this.yProperty, this.zProperty));
		
		this.lengthProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return new Double(Math.sqrt(Vector3d.this.lengthSquareProperty.doubleValue()));
			}
		}, this.lengthSquareProperty));
	}

	
	public ReadOnlyDoubleProperty lengthSquareProperty() {
		return this.lengthSquareProperty.getReadOnlyProperty();
	}
	
	public ReadOnlyDoubleProperty lengthProperty() {
		return this.lengthProperty.getReadOnlyProperty();
	}
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#length()
	 */
	@Override
	public double length() {
		return this.lengthProperty.doubleValue();
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#lengthSquared()
	 */
	@Override
	public double lengthSquared() {
		return this.lengthSquareProperty.doubleValue();
	}
	
	public void initWithCrossProduct(Vector3d v1, Vector3d v2) {
		Vector3D v = v1.cross(v2);
		this.setX(v.getX());
		this.setY(v.getY());
		this.setZ(v.getZ());
	}
	
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#toUnmodifiable()
	 */
	@Override
	public Vector3D toUnmodifiable() {
		return new UnmodifiableVector3d();
	}
	
	
	/**
	 * 
	 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
	 *
	 */
	private class UnmodifiableVector3d implements FunctionalVector3D.UnmodifiableVector3D {
		
		private static final long serialVersionUID = 6113750458070037483L;

		public UnmodifiableVector3d() {
			//
		}

		@Override
		public Vector3D clone() {
			try {
				return (Vector3D) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		@Override
		public void get(Vector3D t) {
			Vector3d.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Vector3d.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Vector3d.this.get(t);
		}

		@Override
		public double getX() {
			return Vector3d.this.getX();
		}

		@Override
		public int ix() {
			return Vector3d.this.ix();
		}

		@Override
		public double getY() {
			return Vector3d.this.getY();
		}

		@Override
		public int iy() {
			return Vector3d.this.iy();
		}

		@Override
		public double getZ() {
			return Vector3d.this.getZ();
		}

		@Override
		public int iz() {
			return Vector3d.this.iz();
		}

		@Override
		public boolean equals(Tuple3D<?> t1) {
			return Vector3d.this.equals(t1);
		}
		
		@Override
		public int hashCode() {
			return Vector3d.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Vector3D t1, double epsilon) {
			return Vector3d.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public double dot(Vector3D v1) {
			return Vector3d.this.dot(v1);
		}

		@Override
		public Vector3D cross(Vector3D v1) {
			return Vector3d.this.cross(v1);
		}

		@Override
		public Vector3D crossLeftHand(Vector3D v1) {
			return Vector3d.this.crossLeftHand(v1);
		}

		@Override
		public Vector3D crossRightHand(Vector3D v1) {
			return Vector3d.this.crossRightHand(v1);
		}

		@Override
		public double length() {
			return Vector3d.this.length();
		}

		@Override
		public double lengthSquared() {
			return Vector3d.this.lengthSquared();
		}

		@Override
		public double angle(Vector3D v1) {
			return Vector3d.this.angle(v1);
		}

		@Override
		public boolean isUnitVector() {
			return Vector3d.this.isUnitVector();
		}

		@Override
		public boolean isColinear(Vector3D v) {
			return Vector3d.this.isColinear(v);
		}

		@Override
		public Vector3D toUnmodifiable() {
			return this;
		}

		@Override
		public double perp(Vector3D v) {
			return Vector3d.this.perp(v);
		}
		
	}
	
}

/**
 * 
 * fr.utbm.v3g.core.math.Point3d.java
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


import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;


/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public class Point3d extends Tuple3d<Point3D> implements FunctionalPoint3D {

	private static final long serialVersionUID = 6029764593238894104L;

	public Point3d() {
		super();
	}
	
	public Point3d(double x, double y, double z) {
		super(x, y, z);
	}
	
	public Point3d(Tuple3d<?> point) {
		super(point.getX(), point.getY(), point.getZ());
	}
	
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#toUnmodifiable()
	 */
	@Override
	public Point3D toUnmodifiable() {
		return new UnmodifiablePoint3d();
	}
	

	private class UnmodifiablePoint3d implements FunctionalPoint3D.UnmodifiablePoint3f {
		

		private static final long serialVersionUID = 4886976526818120413L;

		public UnmodifiablePoint3d() {
			//
		}

		@Override
		public Point3D clone() {
			try {
				return (Point3D) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		@Override
		public void get(Point3D t) {
			Point3d.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Point3d.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Point3d.this.get(t);
		}

		@Override
		public double getX() {
			return Point3d.this.getX();
		}

		@Override
		public int ix() {
			return Point3d.this.ix();
		}

		@Override
		public double getY() {
			return Point3d.this.getY();
		}

		@Override
		public int iy() {
			return Point3d.this.iy();
		}

		@Override
		public double getZ() {
			return Point3d.this.getZ();
		}

		@Override
		public int iz() {
			return Point3d.this.iz();
		}

		@Override
		public boolean equals(Tuple3D<?> t1) {
			return Point3d.this.equals(t1);
		}
		
		@Override
		public int hashCode() {
			return Point3d.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Point3D t1, double epsilon) {
			return Point3d.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public int distanceSquared(Point3D p1) {
			return Point3d.this.distanceSquared(p1);
		}

		@Override
		public int distance(Point3D p1) {
			return Point3d.this.distance(p1);
		}

		@Override
		public int distanceL1(Point3D p1) {
			return Point3d.this.distanceL1(p1);
		}

		@Override
		public int distanceLinf(Point3D p1) {
			return Point3d.this.distanceLinf(p1);
		}

		@Override
		public Point3D toUnmodifiable() {
			return this;
		}
		
	}
	
}

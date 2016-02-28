/**
 * 
 * fr.utbm.v3g.core.math.Point2d.java
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

import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;


/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public class Point2d extends Tuple2d<Point2D> implements FunctionalPoint2D {

	private static final long serialVersionUID = -7665998909350010869L;
	
	/**
	 */
	public Point2d() {
		//
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public Point2d(double x, double y) {
		super(x, y);
	}
	
	public Point2d(Point2d p) {
		super(p.xProperty,p.yProperty);
	}
	
	public Point2d(Tuple2D<?> tuple) {
		super(tuple);
	}
	
	public Point2d(Tuple2d<?> tuple) {
		super(tuple);
	}
	
	public Point2d(DoubleProperty x, DoubleProperty y) {
		super(x,y);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#toUnmodifiable()
	 */
	@Pure
	@Override
	public Point2D toUnmodifiable() {
		return new UnmodifiablePoint2d();
	}

	/**
	 * 
	 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
	 *
	 */
	public class UnmodifiablePoint2d implements FunctionalPoint2D.UnmodifiablePoint2f {

		private static final long serialVersionUID = -8670105082548919880L;

		/**
		 */
		public UnmodifiablePoint2d() {
		}

		@Pure
		@Override
		public UnmodifiablePoint2f clone() {
			return new UnmodifiablePoint2d();
		}

		@Pure
		@Override
		public void get(Point2D t) {
			Point2d.this.get(t);
		}

		@Pure
		@Override
		public void get(int[] t) {
			Point2d.this.get(t);
		}

		@Pure
		@Override
		public void get(double[] t) {
			Point2d.this.get(t);
		}

		@Pure
		public double getX() {
			return Point2d.this.getX();
		}

		@Pure
		@Override
		public int ix() {
			return Point2d.this.ix();
		}

		@Pure
		@Override
		public double getY() {
			return Point2d.this.getY();
		}

		@Pure
		@Override
		public int iy() {
			return Point2d.this.iy();
		}

		@Pure
		@Override
		public boolean equals(Tuple2D<?> t1) {
			return Point2d.this.equals(t1);
		}

		@Pure
		@Override
		public int hashCode() {
			return Point2d.this.hashCode();
		}

		@Pure
		@Override
		public boolean epsilonEquals(Point2D t1, double epsilon) {
			return Point2d.this.epsilonEquals(t1, epsilon);
		}

		@Pure
		@Override
		public double getDistanceSquared(Point2D p1) {
			return Point2d.this.getDistanceSquared(p1);
		}

		@Pure
		@Override
		public double getDistance(Point2D p1) {
			return Point2d.this.getDistance(p1);
		}

		@Pure
		@Override
		public double getDistanceL1(Point2D p1) {
			return Point2d.this.getDistanceL1(p1);
		}

		@Pure
		@Override
		public double getDistanceLinf(Point2D p1) {
			return Point2d.this.getDistanceLinf(p1);
		}

		@Pure
		@Override
		public Point2D toUnmodifiable() {
			return this;
		}

		@Pure
		@Override
		public int distanceSquared(Point2D p1) {
			return Point2d.this.distanceSquared(p1);
		}

		@Pure
		@Override
		public int distance(Point2D p1) {
			return Point2d.this.distance(p1);
		}

		@Pure
		@Override
		public int distanceL1(Point2D p1) {
			return Point2d.this.distanceL1(p1);
		}

		@Pure
		@Override
		public int distanceLinf(Point2D p1) {
			return Point2d.this.distanceLinf(p1);
		}
		
	}

}

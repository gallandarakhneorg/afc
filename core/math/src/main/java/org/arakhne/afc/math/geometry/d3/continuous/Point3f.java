/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D Point with 3 floating-point numbers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Point3f extends Tuple3f<Point3D> implements FunctionalPoint3D {

	private static final long serialVersionUID = -4821663886493835147L;
	
//	/**
//	 * Replies if three points are colinear, ie. one the same line.
//	 * <p>
//	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
//	 * <p>
//	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
//	 * 
//	 * @param x1
//	 *            is the X coordinate of the first point
//	 * @param y1
//	 *            is the Y coordinate of the first point
//	 * @param z1
//	 *            is the Z coordinate of the first point
//	 * @param x2
//	 *            is the X coordinate of the second point
//	 * @param y2
//	 *            is the Y coordinate of the second point
//	 * @param z2
//	 *            is the Z coordinate of the second point
//	 * @param x3
//	 *            is the X coordinate of the third point
//	 * @param y3
//	 *            is the Y coordinate of the third point
//	 * @param z3
//	 *            is the Z coordinate of the third point
//	 * @param epsilon is the threshold for accepting colinear points.
//	 * @return <code>true</code> if the three given points are colinear.
//	 * @since 3.0
//	 * @see MathUtil#isEpsilonZero(double)
//	 */
//	public static boolean isCollinearPoints(
//			double x1, double y1, double z1,
//			double x2, double y2, double z2,
//			double x3, double y3, double z3,
//			double epsilon) {
//		double dx1 = x2 - x1;
//		double dy1 = y2 - y1;
//		double dz1 = z2 - z1;
//		double dx2 = x3 - x1;
//		double dy2 = y3 - y1;
//		double dz2 = z3 - z1;
//
//		double cx = dy1 * dz2 - dy2 * dz1;
//		double cy = dx2 * dz1 - dx1 * dz2;
//		double cz = dx1 * dy2 - dx2 * dy1;
//
//		double sum = cx * cx + cy * cy + cz * cz;
//
//		return MathUtil.isEpsilonZero(sum, epsilon);
//	}
//
//	/** Compute the distance between 2 points.
//	 *
//	 * @param x1 x position of the first point.
//	 * @param y1 y position of the first point.
//	 * @param z1 z position of the first point.
//	 * @param x2 x position of the second point.
//	 * @param y2 y position of the second point.
//	 * @param z2 z position of the second point.
//	 * @return the distance between given points.
//	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
//	 * @see #distanceL1PointPoint(double, double, double, double, double, double)
//	 */
//	public static double distancePointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
//		double dx, dy, dz;
//		dx = x1 - x2; 
//		dy = y1 - y2;
//		dz = z1 - z2;
//		return Math.sqrt(dx*dx+dy*dy+dz*dz);
//	}
//
//	/** Compute the squared distance between 2 points.
//	 *
//	 * @param x1 x position of the first point.
//	 * @param y1 y position of the first point.
//	 * @param z1 z position of the first point.
//	 * @param x2 x position of the second point.
//	 * @param y2 y position of the second point.
//	 * @param z2 z position of the second point.
//	 * @return the squared distance between given points.
//	 * @see #distancePointPoint(double, double, double, double, double, double)
//	 * @see #distanceL1PointPoint(double, double, double, double, double, double)
//	 */
//	public static double distanceSquaredPointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
//		double dx, dy, dz;
//		dx = x1 - x2;
//		dy = y1 - y2;
//		dz = z1 - z2;
//		return dx*dx+dy*dy+dz*dz;
//	}
//
//	/** Compute the L-1 (Manhattan) distance between 2 points.
//	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2).
//	 *
//	 * @param x1 x position of the first point.
//	 * @param y1 y position of the first point.
//	 * @param z1 z position of the first point.
//	 * @param x2 x position of the second point.
//	 * @param y2 y position of the second point.
//	 * @param z2 z position of the second point.
//	 * @return the distance between given points.
//	 * @see #distancePointPoint(double, double, double, double, double, double)
//	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
//	 */
//	public static double distanceL1PointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
//		return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2);
//	}
//
//	/** Compute the L-infinite distance between 2 points.
//	 * The L-infinite distance is equal to 
//	 * MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2)]. 
//	 *
//	 * @param x1 x position of the first point.
//	 * @param y1 y position of the first point.
//	 * @param z1 z position of the first point.
//	 * @param x2 x position of the second point.
//	 * @param y2 y position of the second point.
//	 * @param z2 z position of the second point.
//	 * @return the distance between given points.
//	 * @see #distancePointPoint(double, double, double, double, double, double)
//	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
//	 */
//	public static double distanceLinfPointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
//		return MathUtil.max(Math.abs(x1 - x2), Math.abs(y1 - y2), Math.abs(z1 - z2));
//	}

	/**
	 */
	public Point3f() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3f(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3f(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3f(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(long x, long y, long z) {
		super(x,y,z);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point3f clone() {
		return (Point3f)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int distanceSquared(Point3D p1) {
	      double dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (int)(dx*dx+dy*dy+dz*dz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int distance(Point3D p1) {
	      double  dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (int)Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int distanceL1(Point3D p1) {
	      return (int)(Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY()) + Math.abs(this.z-p1.getZ()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int distanceLinf(Point3D p1) {
	      return (int)Math.max(Math.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY())),
	    		  Math.abs(this.z-p1.getZ()));
	}

	@Override
	public void add(Point3D t1, Vector3D t2) {
		this.x = (t1.getX()) + (t2.getX());
		this.y = t1.getY() + t2.getY();
		this.z = t1.getZ() + t2.getZ();
	}

	@Override
	public void add(Vector3D t1, Point3D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
		this.z = t1.getZ() + t2.getZ();
	}

	@Override
	public void add(Vector3D t1) {
		this.x += t1.getX();
		this.y += t1.getY();
		this.z += t1.getZ();
	}

	@Override
	public void scaleAdd(int s, Vector3D t1, Point3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(double s, Vector3D t1, Point3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(int s, Point3D t1, Vector3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(double s, Point3D t1, Vector3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(int s, Vector3D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
		this.z = s * this.z + t1.getZ();
	}

	@Override
	public void scaleAdd(double s, Vector3D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
		this.z = s * this.z + t1.getZ();
	}

	@Override
	public void sub(Point3D t1, Vector3D t2) {
		this.x = t1.getX() - t2.getX();
		this.y = t1.getY() - t2.getY();
		this.z = t1.getZ() - t2.getZ();
	}

	@Override
	public void sub(Vector3D t1) {
		this.x -= t1.getX();
		this.y -= t1.getY();
		this.z -= t1.getZ();
	}

	@Pure
	@Override
	public Point3D toUnmodifiable() {
		return new UnmodifiablePoint3f();
	}

	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiablePoint3f implements FunctionalPoint3D.UnmodifiablePoint3f {
		
		private static final long serialVersionUID = -3357133685019699117L;

		public UnmodifiablePoint3f() {
			//
		}

		@Pure
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
			Point3f.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Point3f.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Point3f.this.get(t);
		}



		@Pure
		@Override
		public double getX() {
			return Point3f.this.getX();
		}

		@Pure
		@Override
		public int ix() {
			return Point3f.this.ix();
		}



		@Pure
		@Override
		public double getY() {
			return Point3f.this.getY();
		}

		@Pure
		@Override
		public int iy() {
			return Point3f.this.iy();
		}


		@Pure
		@Override
		public double getZ() {
			return Point3f.this.getZ();
		}

		@Pure
		@Override
		public int iz() {
			return Point3f.this.iz();
		}


		@Pure
		@Override
		public boolean equals(Tuple3D<?> t1) {
			return Point3f.this.equals(t1);
		}

		@Pure
		@Override
		public int hashCode() {
			return Point3f.this.hashCode();
		}

		@Pure
		@Override
		public boolean epsilonEquals(Point3D t1, double epsilon) {
			return Point3f.this.epsilonEquals(t1, epsilon);
		}

		@Pure
		@Override
		public int distanceSquared(Point3D p1) {
			return Point3f.this.distanceSquared(p1);
		}

		@Pure
		@Override
		public int distance(Point3D p1) {
			return Point3f.this.distance(p1);
		}

		@Pure
		@Override
		public int distanceL1(Point3D p1) {
			return Point3f.this.distanceL1(p1);
		}

		@Pure
		@Override
		public int distanceLinf(Point3D p1) {
			return Point3f.this.distanceLinf(p1);
		}


		@Pure
		@Override
		public double getDistanceSquared(Point3D p1) {
			return Point3f.this.getDistanceSquared(p1);
		}

		@Pure
		@Override
		public double getDistance(Point3D p1) {
			return Point3f.this.getDistance(p1);
		}

		@Pure
		@Override
		public double getDistanceL1(Point3D p1) {
			return Point3f.this.getDistanceL1(p1);
		}

		@Pure
		@Override
		public double getDistanceLinf(Point3D p1) {
			return Point3f.this.getDistanceLinf(p1);
		}

		@Pure
		@Override
		public Point3D toUnmodifiable() {
			return this;
		}
	

		
	}
	
}
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Point with 2 floating-point numbers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Point2f extends Tuple2f<Point2D> implements FunctionalPoint2D {

	private static final long serialVersionUID = 8963319137253544821L;

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
//	 * @param x2
//	 *            is the X coordinate of the second point
//	 * @param y2
//	 *            is the Y coordinate of the second point
//	 * @param x3
//	 *            is the X coordinate of the third point
//	 * @param y3
//	 *            is the Y coordinate of the third point
//	 * @return <code>true</code> if the three given points are colinear.
//	 * @since 3.0
//	 * @see MathUtil#isEpsilonZero(double)
//	 */
//	public static boolean isCollinearPoints(double x1, double y1, double x2, double y2, double x3, double y3) {
//		// Test if three points are colinears
//		// iff det( [ x1 x2 x3 ]
//		// [ y1 y2 y3 ]
//		// [ 1 1 1 ] ) = 0
//		// Do not invoked MathUtil.determinant() to limit computations.
//		return MathUtil.isEpsilonZero(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
//	}
//
//	/** Compute the distance between 2 points.
//	 *
//	 * @param x1 horizontal position of the first point.
//	 * @param y1 vertical position of the first point.
//	 * @param x2 horizontal position of the second point.
//	 * @param y2 vertical position of the second point.
//	 * @return the distance between given points.
//	 * @see #distanceSquaredPointPoint(double, double, double, double)
//	 * @see #distanceL1PointPoint(double, double, double, double)
//	 */
//	public static double distancePointPoint(double x1, double y1, double x2, double y2) {
//		double dx, dy;
//		dx = x1 - x2;
//		dy = y1 - y2;
//		return Math.sqrt(dx*dx+dy*dy);
//	}
//
//	/** Compute the squared distance between 2 points.
//	 *
//	 * @param x1 horizontal position of the first point.
//	 * @param y1 vertical position of the first point.
//	 * @param x2 horizontal position of the second point.
//	 * @param y2 vertical position of the second point.
//	 * @return the squared distance between given points.
//	 * @see #distancePointPoint(double, double, double, double)
//	 * @see #distanceL1PointPoint(double, double, double, double)
//	 */
//	public static double distanceSquaredPointPoint(double x1, double y1, double x2, double y2) {
//		double dx, dy;
//		dx = x1 - x2;
//		dy = y1 - y2;
//		return dx*dx+dy*dy;
//	}
//
//	/** Compute the L-1 (Manhattan) distance between 2 points.
//	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
//	 *
//	 * @param x1 horizontal position of the first point.
//	 * @param y1 vertical position of the first point.
//	 * @param x2 horizontal position of the second point.
//	 * @param y2 vertical position of the second point.
//	 * @return the distance between given points.
//	 * @see #distancePointPoint(double, double, double, double)
//	 * @see #distanceSquaredPointPoint(double, double, double, double)
//	 */
//	public static double distanceL1PointPoint(double x1, double y1, double x2, double y2) {
//		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
//	}
//
//	/** Compute the L-infinite distance between 2 points.
//	 * The L-infinite distance is equal to 
//	 * MAX[abs(x1-x2), abs(y1-y2)]. 
//	 *
//	 * @param x1 horizontal position of the first point.
//	 * @param y1 vertical position of the first point.
//	 * @param x2 horizontal position of the second point.
//	 * @param y2 vertical position of the second point.
//	 * @return the distance between given points.
//	 * @see #distancePointPoint(double, double, double, double)
//	 * @see #distanceSquaredPointPoint(double, double, double, double)
//	 */
//	public static double distanceLinfPointPoint(double x1, double y1, double x2, double y2) {
//		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
//	}

	/**
	 */
	public Point2f() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2f(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2f(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2f(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	@SuppressWarnings("hiding")
	public Point2f(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	@SuppressWarnings("hiding")
	public Point2f(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	@SuppressWarnings("hiding")
	public Point2f(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	@SuppressWarnings("hiding")
	public Point2f(long x, long y) {
		super(x,y);
	}
	
	
//	/** {@inheritDoc}
//	 */
//	@Override
//	public Point2f clone() {
//		return (Point2f)super.clone();
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double getDistanceSquared(Point2D p1) {
//	      double dx, dy;
//	      dx = this.x-p1.getX();  
//	      dy = this.y-p1.getY();
//	      return (dx*dx+dy*dy);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double getDistance(Point2D p1) {
//	      double  dx, dy;
//	      dx = this.x-p1.getX();  
//	      dy = this.y-p1.getY();
//	      return Math.sqrt(dx*dx+dy*dy);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double getDistanceL1(Point2D p1) {
//	      return (Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY()));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double getDistanceLinf(Point2D p1) {
//	      return Math.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY()));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int distanceSquared(Point2D p1) {
//	      return (int) getDistanceSquared(p1);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int distance(Point2D p1) {
//	      return (int) getDistance(p1);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int distanceL1(Point2D p1) {
//	      return (int) getDistanceL1(p1);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int distanceLinf(Point2D p1) {
//	      return (int) getDistanceLinf(p1);
//	}
//
//	@Override
//	public void add(Point2D t1, Vector2D t2) {
//		this.x = t1.getX() + t2.getX();
//		this.y = t1.getY() + t2.getY();
//	}
//
//	@Override
//	public void add(Vector2D t1, Point2D t2) {
//		this.x = t1.getX() + t2.getX();
//		this.y = t1.getY() + t2.getY();
//	}
//
//	@Override
//	public void add(Vector2D t1) {
//		this.x += t1.getX();
//		this.y += t1.getY();
//	}
//
//	@Override
//	public void scaleAdd(int s, Vector2D t1, Point2D t2) {
//		this.x = s * t1.getX() + t2.getX();
//		this.y = s * t1.getY() + t2.getY();
//	}
//
//	@Override
//	public void scaleAdd(double s, Vector2D t1, Point2D t2) {
//		this.x = s * t1.getX() + t2.getX();
//		this.y = s * t1.getY() + t2.getY();
//	}
//
//	@Override
//	public void scaleAdd(int s, Point2D t1, Vector2D t2) {
//		this.x = s * t1.getX() + t2.getX();
//		this.y = s * t1.getY() + t2.getY();
//	}
//
//	@Override
//	public void scaleAdd(double s, Point2D t1, Vector2D t2) {
//		this.x = s * t1.getX() + t2.getX();
//		this.y = s * t1.getY() + t2.getY();
//	}
//
//	@Override
//	public void scaleAdd(int s, Vector2D t1) {
//		this.x = s * this.x + t1.getX();
//		this.y = s * this.y + t1.getY();
//	}
//
//	@Override
//	public void scaleAdd(double s, Vector2D t1) {
//		this.x = s * this.x + t1.getX();
//		this.y = s * this.y + t1.getY();
//	}
//
//	@Override
//	public void sub(Point2D t1, Vector2D t2) {
//		this.x = t1.getX() - t2.getX();
//		this.y = t1.getY() - t2.getY();
//	}
//
//	@Override
//	public void sub(Vector2D t1) {
//		this.x -= t1.getX();
//		this.y -= t1.getY();
//	}

	@Pure
	@Override
	public Point2D toUnmodifiable() {
		return new UnmodifiablePoint2f();
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiablePoint2f implements FunctionalPoint2D.UnmodifiablePoint2f {

		private static final long serialVersionUID = -8670105082548919880L;

		/**
		 */
		public UnmodifiablePoint2f() {
		}

		@Pure
		@Override
		public UnmodifiablePoint2f clone() {
			return new UnmodifiablePoint2f();
		}

//		@Override
//		public void absolute() {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void absolute(Point2D t) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void add(int x, int y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void add(double x, double y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void addX(int x) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void addX(double x) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void addY(int y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void addY(double y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clamp(int min, int max) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clamp(double min, double max) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMin(int min) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMin(double min) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMax(int max) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMax(double max) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clamp(int min, int max, Point2D t) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clamp(double min, double max, Point2D t) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMin(int min, Point2D t) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMin(double min, Point2D t) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMax(int max, Point2D t) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void clampMax(double max, Point2D t) {
//			throw new UnsupportedOperationException();
//		}

		@Pure
		@Override
		public void get(Point2D t) {
			Point2f.this.get(t);
		}

		@Pure
		@Override
		public void get(int[] t) {
			Point2f.this.get(t);
		}

		@Pure
		@Override
		public void get(double[] t) {
			Point2f.this.get(t);
		}

//		@Override
//		public void negate(Point2D t1) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void negate() {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scale(int s, Point2D t1) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scale(double s, Point2D t1) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scale(int s) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scale(double s) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void set(Tuple2D<?> t1) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void set(int x, int y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void set(double x, double y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void set(int[] t) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void set(double[] t) {
//			throw new UnsupportedOperationException();
//		}

		@Pure
		@Override
		public double getX() {
			return Point2f.this.getX();
		}

		@Pure
		@Override
		public int ix() {
			return Point2f.this.ix();
		}

//		@Override
//		public void setX(int x) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void setX(double x) {
//			throw new UnsupportedOperationException();
//		}

		@Pure
		@Override
		public double getY() {
			return Point2f.this.getY();
		}

		@Pure
		@Override
		public int iy() {
			return Point2f.this.iy();
		}

//		@Override
//		public void setY(int y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void setY(double y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void sub(int x, int y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void sub(double x, double y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void subX(int x) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void subX(double x) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void subY(int y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void subY(double y) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void interpolate(Point2D t1, Point2D t2, double alpha) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void interpolate(Point2D t1, double alpha) {
//			throw new UnsupportedOperationException();
//		}

		@Pure
		@Override
		public boolean equals(Tuple2D<?> t1) {
			return Point2f.this.equals(t1);
		}

		@Pure
		@Override
		public int hashCode() {
			return Point2f.this.hashCode();
		}

		@Pure
		@Override
		public boolean epsilonEquals(Point2D t1, double epsilon) {
			return Point2f.this.epsilonEquals(t1, epsilon);
		}

		@Pure
		@Override
		public double getDistanceSquared(Point2D p1) {
			return Point2f.this.getDistanceSquared(p1);
		}

		@Pure
		@Override
		public double getDistance(Point2D p1) {
			return Point2f.this.getDistance(p1);
		}

		@Pure
		@Override
		public double getDistanceL1(Point2D p1) {
			return Point2f.this.getDistanceL1(p1);
		}

		@Pure
		@Override
		public double getDistanceLinf(Point2D p1) {
			return Point2f.this.getDistanceLinf(p1);
		}

//		@Override
//		public void add(Point2D t1, Vector2D t2) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void add(Vector2D t1, Point2D t2) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void add(Vector2D t1) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scaleAdd(int s, Vector2D t1, Point2D t2) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scaleAdd(double s, Vector2D t1, Point2D t2) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scaleAdd(int s, Point2D t1, Vector2D t2) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scaleAdd(double s, Point2D t1, Vector2D t2) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scaleAdd(int s, Vector2D t1) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void scaleAdd(double s, Vector2D t1) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void sub(Point2D t1, Vector2D t2) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void sub(Vector2D t1) {
//			throw new UnsupportedOperationException();
//		}

		@Pure
		@Override
		public Point2D toUnmodifiable() {
			return this;
		}

		@Pure
		@Override
		public int distanceSquared(Point2D p1) {
			return Point2f.this.distanceSquared(p1);
		}

		@Pure
		@Override
		public int distance(Point2D p1) {
			return Point2f.this.distance(p1);
		}

		@Pure
		@Override
		public int distanceL1(Point2D p1) {
			return Point2f.this.distanceL1(p1);
		}

		@Pure
		@Override
		public int distanceLinf(Point2D p1) {
			return Point2f.this.distanceLinf(p1);
		}
		
	}

}
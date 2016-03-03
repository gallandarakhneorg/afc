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
package org.arakhne.afc.math.geometry.d2;

import org.arakhne.afc.math.MathUtil;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Point.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Point2D extends Tuple2D<Point2D> {

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	static boolean isCollinearPoints(double x1, double y1, double x2, double y2, double x3, double y3) {
		// Test if three points are colinears
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computations.
		return MathUtil.isEpsilonZero(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
	}

	/** Compute the distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #getDistanceSquaredPointPoint(double, double, double, double)
	 * @see #getDistanceL1PointPoint(double, double, double, double)
	 */
	@Pure
	static double getDistancePointPoint(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		return Math.sqrt(dx*dx+dy*dy);
	}

	/** Compute the squared distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the squared distance between given points.
	 * @see #getDistancePointPoint(double, double, double, double)
	 * @see #getDistanceL1PointPoint(double, double, double, double)
	 */
	@Pure
	static double getDistanceSquaredPointPoint(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		return dx*dx+dy*dy;
	}

	/** Compute the L-1 (Manhattan) distance between 2 points.
	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #getDistancePointPoint(double, double, double, double)
	 * @see #getDistanceSquaredPointPoint(double, double, double, double)
	 */
	@Pure
	static double getDistanceL1PointPoint(double x1, double y1, double x2, double y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	/** Compute the L-infinite distance between 2 points.
	 * The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #getDistancePointPoint(double, double, double, double)
	 * @see #getDistanceSquaredPointPoint(double, double, double, double)
	 */
	@Pure
	static double getDistanceLinfPointPoint(double x1, double y1, double x2, double y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	/**
	 * Computes the square of the distance between this point and point p1.
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceSquared(Point2D p1) {
		return getDistanceSquaredPointPoint(getX(), getY(), p1.getX(), p1.getY());
	}
	
	/**
	 * Computes the distance between this point and point p1.
	 * @param p1 the other point
	 * @return the distance. 
	 */    
	@Pure
	default double getDistance(Point2D p1) {
		return getDistancePointPoint(getX(), getY(), p1.getX(), p1.getY());
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceL1(Point2D p1) {
		return getDistanceL1PointPoint(getX(), getY(), p1.getX(), p1.getY());
	}

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceLinf(Point2D p1) {
		return getDistanceLinfPointPoint(getX(), getY(), p1.getX(), p1.getY());
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	default int getIdistanceL1(Point2D p1) {
		return Math.abs(ix() - p1.ix()) + Math.abs(iy() - p1.iy());
	}

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	default int getIdistanceLinf(Point2D p1) {
		return Math.max(Math.abs(ix() - p1.ix()), Math.abs(iy() - p1.iy()));
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	default void add(Point2D t1, Vector2D t2) {
		set(t1.getX() + t2.getX(),
			t1.getY() + t2.getY());
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	default void add(Vector2D t1, Point2D t2) {
		set(t1.getX() + t2.getX(),
			t1.getY() + t2.getY());
	}
	
	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param t1 the other tuple
	 */
	default void add(Vector2D t1) {
		set(getX() + t1.getX(),
			getY() + t1.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	default void scaleAdd(int s, Vector2D t1, Point2D t2) {
		set(s * t1.getX() + t2.getX(),
			s * t1.getY() + t2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	default void scaleAdd(double s, Vector2D t1, Point2D t2) {
		set(s * t1.getX() + t2.getX(),
			s * t1.getY() + t2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	default void scaleAdd(int s, Point2D t1, Vector2D t2) {
		set(s * t1.getX() + t2.getX(),
			s * t1.getY() + t2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	default void scaleAdd(double s, Point2D t1, Vector2D t2) {
		set(s * t1.getX() + t2.getX(),
			s * t1.getY() + t2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param s the scalar value
	 * @param t1 the tuple to be added
	 */
	default void scaleAdd(int s, Vector2D t1) {
		set(s * getX() + t1.getX(),
			s * getY() + t1.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param s the scalar value
	 * @param t1 the tuple to be added
	 */
	default void scaleAdd(double s, Vector2D t1) {
		set(s * getX() + t1.getX(),
			s * getY() + t1.getY());
	}

	
	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	default void sub(Point2D t1, Vector2D t2) {
		set(t1.getX() - t2.getX(),
			t1.getY() - t2.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param t1 the other tuple
	 */
	default void sub(Vector2D t1) {
		set(getX() - t1.getX(),
			getY() - t1.getY());
	}

	/** Replies an unmodifiable copy of this point.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	Point2D toUnmodifiable();

	/** Unmodifiable2D Point.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public interface UnmodifiablePoint2D extends UnmodifiableTuple2D<Point2D>, Point2D {

		@Override
		default void add(Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		default void add(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default Point2D toUnmodifiable() {
			return this;
		}

	}
	
}
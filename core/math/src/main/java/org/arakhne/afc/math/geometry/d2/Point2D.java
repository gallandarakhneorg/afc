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
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Point.
 *
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Point2D<RP extends Point2D<? super RP, ? super RV>, RV extends Vector2D<? super RV, ? super RP>> extends Tuple2D<RP> {

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
	@Inline(value = "(MathUtil.isEpsilonZero($1 * ($4 - $6) + $3 * ($6 - $2) + $5 * ($2 - $4)))",
			imported = {MathUtil.class})
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
	@Inline(value = "(Math.hypot($1 - $3, $2- $4))",
			imported = {Math.class})
	static double getDistancePointPoint(double x1, double y1, double x2, double y2) {
		return Math.hypot(x1 - x2, y1 - y2);
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
	@Inline(value = "(($1-$3)*($1-$3) + ($2-$4)*($2-$4))")
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
	@Inline(value = "(Math.abs($1 - $3) + Math.abs($2 - $4))",
			imported = {Math.class})
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
	@Inline(value = "(Math.max(Math.abs($1 - $3), Math.abs($2 - $4)))",
			imported = {Math.class})
	static double getDistanceLinfPointPoint(double x1, double y1, double x2, double y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	/**
	 * Computes the square of the distance between this point and point p1.
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceSquared(Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return getDistanceSquaredPointPoint(getX(), getY(), point.getX(), point.getY());
	}
	
	/**
	 * Computes the distance between this point and point p1.
	 * @param point the other point
	 * @return the distance. 
	 */    
	@Pure
	default double getDistance(Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return getDistancePointPoint(getX(), getY(), point.getX(), point.getY());
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceL1(Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return getDistanceL1PointPoint(getX(), getY(), point.getX(), point.getY());
	}

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceLinf(Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return getDistanceLinfPointPoint(getX(), getY(), point.getX(), point.getY());
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default int getIdistanceL1(Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return Math.abs(ix() - point.ix()) + Math.abs(iy() - point.iy());
	}

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default int getIdistanceLinf(Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return Math.max(Math.abs(ix() - point.ix()), Math.abs(iy() - point.iy()));
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param point the first tuple
	 * @param vector the second tuple
	 */
	default void add(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(point.getX() + vector.getX(),
			point.getY() + vector.getY());
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param vector the first tuple
	 * @param point the second tuple
	 */
	default void add(Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(vector.getX() + point.getX(),
			vector.getY() + point.getY());
	}
	
	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param vector the other tuple
	 */
	default void add(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(getX() + vector.getX(),
			getY() + vector.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*vector + point).
	 * @param scale the scalar value
	 * @param vector the tuple to be multipled
	 * @param point the tuple to be added
	 */
	default void scaleAdd(int scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * vector.getX() + point.getX(),
			scale * vector.getY() + point.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*vector + point).
	 * @param scale the scalar value
	 * @param vector the tuple to be multipled
	 * @param point the tuple to be added
	 */
	default void scaleAdd(double scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * vector.getX() + point.getX(),
			scale * vector.getY() + point.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*point + vector).
	 * @param scale the scalar value
	 * @param point the tuple to be multipled
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(int scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * point.getX() + vector.getX(),
			scale * point.getY() + vector.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*point + vector).
	 * @param scale the scalar value
	 * @param point the tuple to be multipled
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(double scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * point.getX() + vector.getX(),
			scale * point.getY() + vector.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + vector).
	 * @param scale the scalar value
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(int scale, Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * getX() + vector.getX(),
			scale * getY() + vector.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + vector).
	 * @param scale the scalar value
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(double scale, Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * getX() + vector.getX(),
			scale * getY() + vector.getY());
	}

	
	/**
	 * Sets the value of this tuple to the difference
	 * of tuples point and vector (this = point - vector).
	 * @param point the first tuple
	 * @param vector the second tuple
	 */
	default void sub(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(point.getX() - vector.getX(),
			point.getY() - vector.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and the given vector (this = this - vector).
	 * @param vector the other tuple
	 */
	default void sub(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(getX() - vector.getX(),
			getY() - vector.getY());
	}

	/** Replies an unmodifiable copy of this point.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	UnmodifiablePoint2D<RP, RV> toUnmodifiable();
	
	/** Replies the geometry factory associated to this point.
	 * 
	 * @return the factory.
	 */
	@Pure
	GeomFactory<RV, RP> getGeomFactory();

	/** Sum of this point and a vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @return the result.
	 * @see #add(Point2D, Vector2D)
	 */
	@Pure
	default RP operator_plus(Vector2D<?, ?> v) {
		RP r = getGeomFactory().newPoint();
		r.add(this, v);
		return r;
	}

	/** Increment this point with the given vector: {@code this += v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @see #add(Vector2D)
	 */
	default void operator_add(Vector2D<?, ?> v) {
		add(v);
	}

	/** Subtract the v vector to this point: {@code this - v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to substract.
	 * @return the result.
	 * @see #sub(Point2D, Vector2D)
	 */
	@Pure
	default RP operator_minus(Vector2D<?, ?> v) {
		RP r = getGeomFactory().newPoint();
		r.sub(this, v);
		return r;
	}

	/** Subtract the p point to this point: {@code this - p}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param p the point to substract
	 * @return the vector from the p to this.
	 * @see Vector2D#sub(Point2D, Point2D)
	 */
	@Pure
	default RV operator_minus(Point2D<?, ?> p) {
		RV r = getGeomFactory().newVector();
		r.sub(this, p);
		return r;
	}

	/** Subtract the v vector to this: {@code this -= v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to substract.
	 * @see #sub(Vector2D)
	 */
	default void operator_remove(Vector2D<?, ?> v) {
		sub(v);
	}

	/** Replies if the given vector is equal to this vector: {@code this == v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple2D)
	 */
	@Pure
	default boolean operator_equals(Tuple2D<?> v) {
		return equals(v);
	}

	/** Replies if the given vector is different than this vector: {@code this != v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple2D)
	 */
	@Pure
	default boolean operator_notEquals(Tuple2D<?> v) {
		return !equals(v);
	}

	/** Replies if the distance between this and the p point: {@code this .. p}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param p the point.
	 * @return the distance.
	 * @see #getDistance(Point2D)
	 */
	@Pure
	default double operator_upTo(Point2D<?, ?> p) {
		return getDistance(p);
	}

	/** If this point is epsilon equal to zero then reply p else reply this: {@code this ?: p}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param p the point.
	 * @return the point.
	 */
	@Pure
	default Point2D<? extends RP, ? extends RV> operator_elvis(Point2D<? extends RP, ? extends RV> p) {
		if (MathUtil.isEpsilonZero(getX()) && MathUtil.isEpsilonZero(getY())) {
			return p;
		}
		return this;
	}
	
	/** Replies if the this point is inside the given shape: {@code this && s}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param s the shape to test.
	 * @return <code>true</code> if the point is inside the shape. Otherwise, <code>false</code>.
	 * @see Shape2D#contains(Point2D)
	 */
	@Pure
	default boolean operator_and(Shape2D<?, ?, ?, ?, ?, ?> s) {
		return s.contains(this);
	}

	/** Replies the distance between this point and the given shape: {@code this .. s}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param s the shape to test.
	 * @return the distance.
	 * @see Shape2D#getDistance(Point2D)
	 */
	@Pure
	default double operator_upTo(Shape2D<?, ?, ?, ?, ?, ?> s) {
		return s.getDistance(this);
	}

	/** Turn this point about the given rotation angle around the origin point.
	 *
	 * <p>The rotation is done according to the trigonometric coordinate.
	 * A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @see #turn(double, Point2D, Point2D)
	 * @see #turnLeft(double)
	 * @see #turnRight(double)
	 */
	default void turn(double angle) {
		turn(angle, this);
	}

	/** Turn the given point about the given rotation angle around the origin point, and set this
	 * point with the result.
	 *
	 * <p>The rotation is done according to the trigonometric coordinate.
	 * A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param pointToTurn the point to turn.
	 * @see #turn(double, Point2D, Point2D)
	 * @see #turn(double)
	 * @see #turnLeft(double)
	 * @see #turnRight(double)
	 */
	default void turn(double angle, Point2D<?, ?> pointToTurn) {
		assert (pointToTurn != null) : "Point to turn must be not null"; //$NON-NLS-1$
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x =  cos * pointToTurn.getX() - sin * pointToTurn.getY(); 
		double y =  sin * pointToTurn.getX() + cos * pointToTurn.getY();
		set(x,y);
	}

	/** Turn the given point about the given rotation angle around the origin point, and set this
	 * point with the result.
	 *
	 * <p>The rotation is done according to the trigonometric coordinate.
	 * A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param pointToTurn the point to turn.
	 * @param origin the origin point.
	 * @see #turn(double, Point2D)
	 * @see #turn(double)
	 * @see #turnLeft(double)
	 * @see #turnRight(double)
	 */
	default void turn(double angle, Point2D<?, ?> pointToTurn, Point2D<?, ?> origin) {
		assert (pointToTurn != null) : "Point to turn must be not null"; //$NON-NLS-1$
		assert (origin != null) : "Origin point must be not null"; //$NON-NLS-1$
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double vx = pointToTurn.getX() - origin.getX();
		double vy = pointToTurn.getY() - origin.getY();
		double x =  cos * vx - sin * vy; 
		double y =  sin * vx + cos * vy;
		set(x + origin.getX(), y + origin.getY());
	}

	/** Turn this vector on the left around the origin when the given rotation angle is positive.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @see CoordinateSystem2D
	 * @see #turnLeft(double, Point2D, Point2D)
	 * @see #turn(double)
	 * @see #turnRight(double)
	 */
	default void turnLeft(double angle) {
		turnLeft(angle, this);
	}

	/** Turn the given vector on the left, and set this
	 * vector with the result.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param pointToTurn the vector to turn.
	 * @see CoordinateSystem2D
	 * @see #turnLeft(double, Point2D, Point2D)
	 * @see #turn(double)
	 * @see #turnRight(double)
	 */
	default void turnLeft(double angle, Point2D<?, ?> pointToTurn) {
		assert (pointToTurn != null) : "Point to turn must be not null"; //$NON-NLS-1$
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x, y;
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			x =  cos * pointToTurn.getX() - sin * pointToTurn.getY(); 
			y =  sin * pointToTurn.getX() + cos * pointToTurn.getY();
		} else {
			x =  cos * pointToTurn.getX() + sin * pointToTurn.getY(); 
			y = -sin * pointToTurn.getX() + cos * pointToTurn.getY();
		}
		set(x,y);
	}

	/** Turn the given vector on the left, and set this
	 * vector with the result.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param pointToTurn the vector to turn.
	 * @param origin the origin point.
	 * @see CoordinateSystem2D
	 * @see #turnLeft(double, Point2D)
	 * @see #turn(double)
	 * @see #turnRight(double)
	 */
	default void turnLeft(double angle, Point2D<?, ?> pointToTurn, Point2D<?, ?> origin) {
		assert (pointToTurn != null) : "Point to turn must be not null"; //$NON-NLS-1$
		assert (origin != null) : "Origin point must be not null"; //$NON-NLS-1$
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double vx = pointToTurn.getX() - origin.getX();
		double vy = pointToTurn.getY() - origin.getY();
		double x, y;
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			x =  cos * vx - sin * vy; 
			y =  sin * vx + cos * vy;
		} else {
			x =  cos * vx + sin * vy; 
			y = -sin * vx + cos * vy;
		}
		set(x + origin.getX(), y + origin.getY());
	}

	/** Turn this vector on the right around the origin when the given rotation angle is positive.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @see CoordinateSystem2D
	 * @see #turn(double)
	 * @see #turnLeft(double)
	 */
	default void turnRight(double angle) {
		turnLeft(-angle, this);
	}

	/** Turn this vector on the right around the origin when the given rotation angle is positive.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param pointToTurn the vector to turn.
	 * @see CoordinateSystem2D
	 * @see #turn(double)
	 * @see #turnLeft(double)
	 */
	default void turnRight(double angle, Point2D<?, ?> pointToTurn) {
		turnLeft(-angle, pointToTurn);
	}

	/** Turn this vector on the right around the origin when the given rotation angle is positive.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param pointToTurn the vector to turn.
	 * @param origin the origin point.
	 * @see CoordinateSystem2D
	 * @see #turn(double)
	 * @see #turnLeft(double)
	 */
	default void turnRight(double angle, Point2D<?, ?> pointToTurn, Point2D<?, ?> origin) {
		turnLeft(-angle, pointToTurn, origin);
	}

}
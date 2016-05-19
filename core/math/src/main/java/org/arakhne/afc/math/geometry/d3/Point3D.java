/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D Point.
 * 
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Point3D<RP extends Point3D<? super RP, ? super RV>, RV extends Vector3D<? super RV, ? super RP>> extends Tuple3D<RP> {

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param z1
	 *            is the Z coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param z2
	 *            is the Z coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @param z3
	 *            is the Z coordinate of the third point
	 * @param epsilon is the threshold for accepting colinear points.
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	static boolean isCollinearPoints(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3) {
		double dx1 = x2 - x1;
		double dy1 = y2 - y1;
		double dz1 = z2 - z1;
		double dx2 = x3 - x1;
		double dy2 = y3 - y1;
		double dz2 = z3 - z1;

		double cx = dy1 * dz2 - dy2 * dz1;
		double cy = dx2 * dz1 - dx1 * dz2;
		double cz = dx1 * dy2 - dx2 * dy1;

		return MathUtil.isEpsilonZero(cx * cx + cy * cy + cz * cz);
	}
	
	/** Compute the distance between 2 points.
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the distance between given points.
	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
	 * @see #distanceL1PointPoint(double, double, double, double, double, double)
	 */
	@Pure
	@Inline(value = "Math.sqrt(($1 - $4) * ($1 - $4) + ($2 - $5) * ($2 - $5) + ($3 - $6) * ($3 - $6))",
			imported = {Math.class})
	static double getDistancePointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = x1 - x2; 
		double dy = y1 - y2;
		double dz = z1 - z2;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}
	
	/** Compute the squared distance between 2 points.
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointPoint(double, double, double, double, double, double)
	 * @see #distanceL1PointPoint(double, double, double, double, double, double)
	 */
	@Pure
	@Inline(value = "($1 - $4) * ($1 - $4) + ($2 - $5) * ($2 - $5) + ($3 - $6) * ($3 - $6)")
	static double getDistanceSquaredPointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dz = z1 - z2;
		return dx*dx+dy*dy+dz*dz;
	}

	/** Compute the L-1 (Manhattan) distance between 2 points.
	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2).
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointPoint(double, double, double, double, double, double)
	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
	 */
	@Pure
	@Inline(value = "Math.abs($1 - $4) + Math.abs($2 - $5) + Math.abs($3 - $6)",
			imported = {Math.class})
	static double getDistanceL1PointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2);
	}

	/** Compute the L-infinite distance between 2 points.
	 * The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2)]. 
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointPoint(double, double, double, double, double, double)
	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
	 */
	@Pure
	@Inline(value = "MathUtil.max(Math.abs($1 - $4) + Math.abs($2 - $5) + Math.abs($3 - $6))",
			imported = {Math.class, MathUtil.class})
	static double getDistanceLinfPointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		return MathUtil.max(Math.abs(x1 - x2), Math.abs(y1 - y2), Math.abs(z1 - z2));
	}
	
	/**
	 * Computes the square of the distance between this point and point p1.
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceSquared(Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return  getDistanceSquaredPointPoint(getX(), getY(), getZ(), point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Computes the distance between this point and point p1.
	 * @param point the other point
	 * @return the distance. 
	 */   
	@Pure 
	default double getDistance(Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return getDistancePointPoint(getX(), getY(), getZ(), point.getX(), point.getY(), point.getZ());
	}
	
	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceL1(Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return getDistanceL1PointPoint(getX(), getY(), getZ(), point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceLinf(Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return getDistanceLinfPointPoint(getX(), getY(), getZ(), point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2).
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default int getIdistanceL1(Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return Math.abs(ix() - point.ix()) + Math.abs(iy() - point.iy()) + Math.abs(iz() - point.iz());
	}

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default int getIdistanceLinf(Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return MathUtil.max(Math.abs(ix() - point.ix()), Math.abs(iy() - point.iy()), Math.abs(iz() - point.iz()));
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param point the first tuple
	 * @param vector the second tuple
	 */
	default void add(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(point.getX() + vector.getX(),
			point.getY() + vector.getY(),
			point.getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param vector the first tuple
	 * @param point the second tuple
	 */
	default void add(Vector3D<?, ?> point, Point3D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(point.getX() + vector.getX(),
			point.getY() + vector.getY(),
			point.getZ() + vector.getZ());
	}
	
	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param vector the other tuple
	 */
	default void add(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(getX() + vector.getX(),
			getY() + vector.getY(),
			getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param vector the tuple to be multipled
	 * @param point the tuple to be added
	 */
	default void scaleAdd(int scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * vector.getX() + point.getX(),
			scale * vector.getY() + point.getY(), 
			scale * vector.getZ() + point.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param vector the tuple to be multipled
	 * @param point the tuple to be added
	 */
	default void scaleAdd(double scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * vector.getX() + point.getX(),
			scale * vector.getY() + point.getY(), 
			scale * vector.getZ() + point.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param point the tuple to be multipled
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(int scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * point.getX() + vector.getX(),
			scale * point.getY() + vector.getY(), 
			scale * point.getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param point the tuple to be multipled
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(double scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * point.getX() + vector.getX(),
			scale * point.getY() + vector.getY(), 
			scale * point.getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param scale the scalar value
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(int scale, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * getX() + vector.getX(),
			scale * getY() + vector.getY(), 
			scale * getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param scale the scalar value
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(double scale, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(scale * getX() + vector.getX(),
			scale * getY() + vector.getY(), 
			scale * getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param point the first tuple
	 * @param vector the second tuple
	 */
	default void sub(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(point.getX() - vector.getX(),
			point.getY() - vector.getY(),
			point.getZ() - vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param vector the other tuple
	 */
	default void sub(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must not be null"; //$NON-NLS-1$
		set(getX() - vector.getX(),
			getY() - vector.getY(),
			getZ() - vector.getZ());
	}

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	UnmodifiablePoint3D<?, ?> toUnmodifiable();

	/** Replies the geometry factory associated to this point.
	 * 
	 * @return the factory.
	 */
	@Pure
	GeomFactory3D<RV, RP> getGeomFactory();
	
	/** Sum of this point and a vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @return the result.
	 * @see #add(Point3D, Vector3D)
	 */
	@Pure
	default RP operator_plus(Vector3D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		return getGeomFactory().newPoint(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
	}

	/** Increment this point with the given vector: {@code this += v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @see #add(Vector3D)
	 */
	default void operator_add(Vector3D<?, ?> v) {
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
	 * @see #sub(Point3D, Vector3D)
	 */
	@Pure
	default RP operator_minus(Vector3D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		return getGeomFactory().newPoint(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
	}

	/** Subtract the p point to this point: {@code this - p}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param p the point to substract
	 * @return the vector from the p to this.
	 * @see Vector3D#sub(Point3D, Point3D)
	 */
	@Pure
	default RV operator_minus(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return getGeomFactory().newVector(getX() - p.getX(), getY() - p.getY(), getZ() - p.getZ());
	}

	/** Subtract the v vector to this: {@code this -= v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to substract.
	 * @see #sub(Vector3D)
	 */
	default void operator_remove(Vector3D<?, ?> v) {
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
	 * @see #equals(Tuple3D)
	 */
	@Pure
	default boolean operator_equals(Tuple3D<?> v) {
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
	 * @see #equals(Tuple3D)
	 */
	@Pure
	default boolean operator_notEquals(Tuple3D<?> v) {
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
	 * @see #getDistance(Point3D)
	 */
	@Pure
	default double operator_upTo(Point3D<?, ?> p) {
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
	default Point3D<? extends RP, ? extends RV> operator_elvis(Point3D<? extends RP, ? extends RV> p) {
		if (MathUtil.isEpsilonZero(getX()) && MathUtil.isEpsilonZero(getY()) && MathUtil.isEpsilonZero(getZ())) {
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
	 * @see Shape3D#contains(Point3D)
	 */
	@Pure
	default boolean operator_and(Shape3D<?, ?, ?, ?, ?, ?> s) {
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
	 * @see Shape3D#getDistance(Point3D)
	 */
	@Pure
	default double operator_upTo(Shape3D<?, ?, ?, ?, ?, ?> s) {
		return s.getDistance(this);
	}
}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 3D Point.
 *
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RQ> is the type of quaternion that can be returned by this tuple.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Point3D<RP extends Point3D<? super RP, ? super RV, ? super RQ>, RV extends Vector3D<? super RV, ? super RP, ? super RQ>, RQ extends Quaternion<? super RP, ? super RV, ? super RQ>>
        extends Tuple3D<RP> {

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 *
	 * <p>Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
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
	 * @return {@code true} if the three given points are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	static boolean isCollinearPoints(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3) {
		final double dx1 = x2 - x1;
		final double dy1 = y2 - y1;
		final double dz1 = z2 - z1;
		final double dx2 = x3 - x1;
		final double dy2 = y3 - y1;
		final double dz2 = z3 - z1;

		final double cx = dy1 * dz2 - dy2 * dz1;
		final double cy = dx2 * dz1 - dx1 * dz2;
		final double cz = dx1 * dy2 - dx2 * dy1;

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
	 * @see #getDistanceSquaredPointPoint(double, double, double, double, double, double)
	 * @see #getDistanceL1PointPoint(double, double, double, double, double, double)
	 */
	@Pure
	@Inline(value = "Math.sqrt(($1 - $4) * ($1 - $4) + ($2 - $5) * ($2 - $5) + ($3 - $6) * ($3 - $6))",
			imported = {Math.class})
	static double getDistancePointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		final double dx = x1 - x2;
		final double dy = y1 - y2;
		final double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
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
	 * @see #getDistancePointPoint(double, double, double, double, double, double)
	 * @see #getDistanceL1PointPoint(double, double, double, double, double, double)
	 */
	@Pure
	@Inline(value = "($1 - $4) * ($1 - $4) + ($2 - $5) * ($2 - $5) + ($3 - $6) * ($3 - $6)")
	static double getDistanceSquaredPointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		final double dx = x1 - x2;
		final double dy = y1 - y2;
		final double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
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
	 * @see #getDistancePointPoint(double, double, double, double, double, double)
	 * @see #getDistanceSquaredPointPoint(double, double, double, double, double, double)
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
	 * @see #getDistancePointPoint(double, double, double, double, double, double)
	 * @see #getDistanceSquaredPointPoint(double, double, double, double, double, double)
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
	default double getDistanceSquared(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return  getDistanceSquaredPointPoint(getX(), getY(), getZ(), point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Computes the distance between this point and point p1.
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistance(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return getDistancePointPoint(getX(), getY(), getZ(), point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default double getDistanceL1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
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
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return getDistanceLinfPointPoint(getX(), getY(), getZ(), point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2).
	 * @param point the other point
	 * @return the distance.
	 */
	@Pure
	default int getIdistanceL1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
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
	default int getIdistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return MathUtil.max(Math.abs(ix() - point.ix()), Math.abs(iy() - point.iy()), Math.abs(iz() - point.iz()));
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param point the first tuple
	 * @param vector the second tuple
	 */
	default void add(Point3D<?, ?, ?> point, Vector3D<?, ?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter();
		assert vector != null : AssertMessages.notNullParameter();
		set(point.getX() + vector.getX(),
			point.getY() + vector.getY(),
			point.getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param vector the first tuple
	 * @param point the second tuple
	 */
	default void add(Vector3D<?, ?, ?> point, Point3D<?, ?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter();
		assert vector != null : AssertMessages.notNullParameter();
		set(point.getX() + vector.getX(),
			point.getY() + vector.getY(),
			point.getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param vector the other tuple
	 */
	default void add(Vector3D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
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
	default void scaleAdd(int scale, Vector3D<?, ?, ?> vector, Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		assert vector != null : AssertMessages.notNullParameter();
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
	default void scaleAdd(double scale, Vector3D<?, ?, ?> vector, Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		assert vector != null : AssertMessages.notNullParameter();
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
	default void scaleAdd(int scale, Point3D<?, ?, ?> point, Vector3D<?, ?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter();
		assert vector != null : AssertMessages.notNullParameter();
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
	default void scaleAdd(double scale, Point3D<?, ?, ?> point, Vector3D<?, ?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter();
		assert vector != null : AssertMessages.notNullParameter();
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
	default void scaleAdd(int scale, Vector3D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
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
	default void scaleAdd(double scale, Vector3D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
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
	default void sub(Point3D<?, ?, ?> point, Vector3D<?, ?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		set(point.getX() - vector.getX(),
			point.getY() - vector.getY(),
			point.getZ() - vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param vector the other tuple
	 */
	default void sub(Vector3D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		set(getX() - vector.getX(),
			getY() - vector.getY(),
			getZ() - vector.getZ());
	}

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	UnmodifiablePoint3D<RP, RV, RQ> toUnmodifiable();

	/** Replies the geometry factory associated to this point.
	 *
	 * @return the factory.
	 */
	@Pure
	GeomFactory3D<RV, RP, RQ> getGeomFactory();

	/** Sum of this point and a vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @return the result.
	 * @see #add(Point3D, Vector3D)
	 */
	@Pure
	@XtextOperator("+")
	default RP operator_plus(Vector3D<?, ?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
	}

	/** Increment this point with the given vector: {@code this += v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @see #add(Vector3D)
	 */
	@XtextOperator("+=")
	default void operator_add(Vector3D<?, ?, ?> v) {
		add(v);
	}

	/** Substract the v vector to this point: {@code this - v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to substract.
	 * @return the result.
	 * @see #sub(Point3D, Vector3D)
	 */
	@Pure
	@XtextOperator("-")
	default RP operator_minus(Vector3D<?, ?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
	}

	/** Substract the p point to this point: {@code this - p}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point to substract
	 * @return the vector from the p to this.
	 * @see Vector3D#sub(Point3D, Point3D)
	 */
	@Pure
	@XtextOperator("-")
	default RV operator_minus(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return getGeomFactory().newVector(getX() - pt.getX(), getY() - pt.getY(), getZ() - pt.getZ());
	}

	/**  the v vector to this: {@code this -= v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to substract.
	 * @see #sub(Vector3D)
	 */
	@XtextOperator("-=")
	default void operator_remove(Vector3D<?, ?, ?> v) {
		sub(v);
	}

	/** Replies if the given vector is equal to this vector: {@code this == v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple3D)
	 */
	@Pure
	@XtextOperator("==")
	default boolean operator_equals(Tuple3D<?> v) {
		return equals(v);
	}

	/** Replies if the given vector is different than this vector: {@code this != v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple3D)
	 */
	@Pure
	@XtextOperator("!=")
	default boolean operator_notEquals(Tuple3D<?> v) {
		return !equals(v);
	}

	/** Replies if the distance between this and the p point: {@code this .. p}
	 *
	 * <p>This function is an implementation of the ".." operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point.
	 * @return the distance.
	 * @see #getDistance(Point3D)
	 */
	@Pure
	@XtextOperator("..")
	default double operator_upTo(Point3D<?, ?, ?> pt) {
		return getDistance(pt);
	}

    /** Replies the distance between this point and the given shape: {@code this .. s}
     *
     * <p>This function is an implementation of the ".." operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param shape the shape to test.
     * @return the distance.
     * @see Shape3D#getDistance(Point3D)
     */
    @Pure
	@XtextOperator("..")
    default double operator_upTo(Shape3D<?, ?, ?, ?, ?, ?, ?> shape) {
        return shape.getDistance(this);
    }

	/** If this point is epsilon equal to zero then reply p else reply this: {@code this ?: p}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point.
	 * @return the point.
	 */
	@Pure
	@XtextOperator("?:")
	default Point3D<? extends RP, ? extends RV, ? extends RQ> operator_elvis(Point3D<? extends RP, ? extends RV, ? extends RQ> pt) {
		if (MathUtil.isEpsilonZero(getX()) && MathUtil.isEpsilonZero(getY()) && MathUtil.isEpsilonZero(getZ())) {
			return pt;
		}
		return this;
	}

	/** Replies if the this point is inside the given shape: {@code this && s}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param shape the shape to test.
	 * @return {@code true} if the point is inside the shape. Otherwise, {@code false}.
	 * @see Shape3D#contains(Point3D)
	 */
	@Pure
	@XtextOperator("&&")
	default boolean operator_and(Shape3D<?, ?, ?, ?, ?, ?, ?> shape) {
		return shape.contains(this);
	}

	/** Substract the p point to this point: {@code this - p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param pt the point to substract
	 * @return the vector from the p to this.
	 * @see Vector3D#sub(Point3D, Point3D)
	 */
	@Pure
	@ScalaOperator("-")
	default RV $minus(Point3D<?, ?, ?> pt) {
		return operator_minus(pt);
	}

	/** Substract the v vector to this point: {@code this - v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector to substract.
	 * @return the result.
	 * @see #sub(Point3D, Vector3D)
	 */
	@Pure
	@ScalaOperator("-")
	default RP $minus(Vector3D<?, ?, ?> v) {
		return operator_minus(v);
	}

	/** Sum of this point and a vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector to add
	 * @return the result.
	 * @see #add(Point3D, Vector3D)
	 */
	@Pure
	@ScalaOperator("+")
	default RP $plus(Vector3D<?, ?, ?> v) {
		return operator_plus(v);
	}
	
	/** Replies the projection of the given point on the plane: {@code this -> plane}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param plane the plane to project one.
	 * @return the result of the projection.
	 * @see Plane3D#getProjection(Point3D)
	 * @since 18.0
	 */
	@Pure
	@XtextOperator("->")
	default RP operator_mappedTo(Plane3D<?, ?, ? extends RP, ? extends RV, ? extends RQ> plane) {
		assert plane != null : AssertMessages.notNullParameter();
		return plane.getProjection(this);
	}

}

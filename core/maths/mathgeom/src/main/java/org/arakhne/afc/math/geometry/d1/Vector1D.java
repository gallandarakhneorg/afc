/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 1.5D Vector.
 *
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RS> is the type of segment that can be returned by this point.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:methodcount")
public interface Vector1D<
		RV extends Vector1D<? super RV, ? super RP, ? super RS>,
		RP extends Point1D<? super RP, ? super RV, ? super RS>,
		RS extends Segment1D<?, ?>>
		extends Tuple2D<RV>, Comparable<Vector1D<?, ?, ?>> {

	@Override
	default int compareTo(Vector1D<?, ?, ?> vector) {
		if (vector == null) {
			return -1;
		}
		final Segment1D<?, ?> mySegment = getSegment();
		final Segment1D<?, ?> otherSegment = vector.getSegment();
		if (mySegment == otherSegment) {
			final int cmp = Double.compare(getX(), vector.getX());
			if (cmp == 0) {
				return Double.compare(getY(), vector.getY());
			}
			return cmp;
		}
		final int h1 = mySegment != null ? mySegment.hashCode() : 0;
		final int h2 = otherSegment != null ? otherSegment.hashCode() : 0;
		return h1 - h2;
	}

	/** Replies the segment.
	 *
	 * @return the segment or <code>null</code> if the weak reference has lost the segment.
	 */
	@Pure
	RS getSegment();

	/**
	 * Replies if the vector is a unit vector.
	 *
	 * <p>Due to the precision on floating-point computations, the test of unit-vector
	 * must consider that the norm of the given vector is approximatively equal
	 * to 1. The precision (i.e. the number of significant decimals) is given
	 * by {@link GeomConstants#UNIT_VECTOR_EPSILON}.
	 *
	 * @param x is the X coordinate of the vector.
	 * @param y is the Y coordinate of the vector.
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 13.0
	 * @see MathUtil#isEpsilonEqual(double, double, double)
	 * @see GeomConstants#UNIT_VECTOR_EPSILON
	 * @see #isUnitVector(double, double, double)
	 */
	@Pure
	@Inline(value = "$3.isUnitVector(($1), ($2), MathConstants.UNIT_VECTOR_EPSILON)",
			imported = {Vector1D.class, MathConstants.class})
	static boolean isUnitVector(double x, double y) {
		return isUnitVector(x, y, GeomConstants.UNIT_VECTOR_EPSILON);
	}

	/**
	 * Replies if the vector is a unit vector.
	 *
	 * <p>Due to the precision on floating-point computations, the test of unit-vector
	 * must consider that the norm of the given vector is approximatively equal
	 * to 1. The precision (i.e. the number of significant decimals) is given
	 * by <code>epsilon</code>.
	 *
	 * @param x is the X coordinate of the vector.
	 * @param y is the Y coordinate of the vector.
	 * @param epsilon the precision distance to assumed for equality.
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 13.0
	 * @see MathUtil#isEpsilonEqual(double, double, double)
	 * @see #isUnitVector(double, double)
	 */
	@Pure
	@Inline(value = "$4.isEpsilonEqual(($1) * ($1) + ($2) * ($2), 1., ($3))",
			imported = {MathUtil.class})
	static boolean isUnitVector(double x, double y, double epsilon) {
		return MathUtil.isEpsilonEqual(x * x + y * y, 1., epsilon);
	}

	/** Replies if this vector is a unit vector.
	 * A unit vector has a length equal to 1.
	 *
	 * @return <code>true</code> if the vector has a length equal to 1.
	 * <code>false</code> otherwise.
	 */
	@Pure
	default boolean isUnitVector() {
		return isUnitVector(getX(), getY());
	}

	@Override
	boolean equals(Object object);

	/** Replies if this point is equals to the given point.
	 *
	 * @param tuple the tuple
	 * @return <code>true</code> if this point has the same coordinates
	 *     on the same segment as for the given point.
	 */
	@Pure
	default boolean equals(Vector1D<?, ?, ?> tuple) {
		try {
			return getSegment() == tuple.getSegment() && getX() == tuple.getX() && getY() == tuple.getY();
		} catch (Throwable exception) {
			return false;
		}
	}

	/**
	 * Sets the value of this tuple to the sum of tuples vector1 and vector2.
	 *
	 * @param vector1 the first tuple
	 * @param vector2 the second tuple
	 */
	default void add(Vector1D<?, ?, ?> vector1, Vector1D<?, ?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		set(vector1.getX() + vector2.getX(),
				vector1.getY() + vector2.getY());
	}

	/**
	 * Sets the value of this tuple to the sum of itself and the given vector.
	 *
	 * @param vector the other tuple
	 */
	default void add(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		set(vector.getX() + getX(),
				vector.getY() + getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	default void scaleAdd(int scale, Vector1D<?, ?, ?> t1, Vector1D<?, ?, ?> t2) {
		assert t1 != null : AssertMessages.notNullParameter(0);
		assert t2 != null : AssertMessages.notNullParameter(1);
		set(scale * t1.getX() + t2.getX(),
				scale * t1.getY() + t2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the tuple vector1 plus the tuple vector2 (this = s*vector1 + vector2).
	 *
	 * @param scale the scalar value
	 * @param vector1 the tuple to be multipled
	 * @param vector2 the tuple to be added
	 */
	default void scaleAdd(double scale, Vector1D<?, ?, ?> vector1, Vector1D<?, ?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		set(scale * vector1.getX() + vector2.getX(),
				scale * vector1.getY() + vector2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple vector (this = s*this + vector).
	 *
	 * @param scale the scalar value
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(int scale, Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		set(scale * getX() + vector.getX(),
				scale * getY() + vector.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple vector (this = s*this + vector).
	 *
	 * @param scale the scalar value
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(double scale, Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		set(scale * getX() + vector.getX(),
				scale * getY() + vector.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples vector1 and vector2 (this = vector1 - vector2).
	 *
	 * @param vector1 the first tuple
	 * @param vector2 the second tuple
	 */
	default void sub(Vector1D<?, ?, ?> vector1, Vector1D<?, ?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		set(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples point1 and point2 (this = point1 - point2).
	 *
	 * @param point1 the first tuple
	 * @param point2 the second tuple
	 */
	default void sub(Point1D<?, ?, ?> point1, Point1D<?, ?, ?> point2) {
		assert point1 != null : AssertMessages.notNullParameter(0);
		assert point2 != null : AssertMessages.notNullParameter(1);
		set(point1.getX() - point2.getX(), point1.getY() - point2.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and vector (this = this - vector).
	 *
	 * @param vector the other tuple
	 */
	default void sub(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		set(getX() - vector.getX(), getY() - vector.getY());
	}

	/**
	 * Returns the length of this vector.
	 *
	 * @return the length of this vector
	 */
	@Pure
	default double getLength() {
		final double x = getX();
		final double y = getY();
		return Math.hypot(x, y);
	}

	/**
	 * Returns the squared length of this vector.
	 *
	 * @return the squared length of this vector
	 */
	@Pure
	default double getLengthSquared() {
		final double x = getX();
		final double y = getY();
		return x * x + y * y;
	}

	/**
	 * Sets the value of this vector to the normalization of vector vector.
	 *
	 * @param vector the un-normalized vector
	 */
	default void normalize(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		final double x = vector.getX();
		final double y = vector.getY();
		double sqlength = x * x + y * y;
		if (sqlength != 0.) {
			sqlength = Math.sqrt(sqlength);
			set(x / sqlength, y / sqlength);
		} else {
			set(0, 0);
		}
	}

	/**
	 * Normalizes this vector in place.
	 *
	 * <p>If the length of the vector is zero, x and y are set to zero.
	 */
	default void normalize() {
		final double x = getX();
		final double y = getY();
		double sqlength = x * x + y * y;
		if (sqlength != 1.) {
			if (sqlength != 0.) {
				sqlength = Math.sqrt(sqlength);
				set(x / sqlength, y / sqlength);
			} else {
				set(0, 0);
			}
		}
	}

	/** Change the length of the vector.
	 * The direction of the vector is unchanged.
	 *
	 * @param newLength - the new length.
	 */
	default void setLength(double newLength) {
		final double l = getLength();
		if (l != 0.) {
			final double f = newLength / l;
			set(getX() * f, getY() * f);
		} else {
			set(newLength, 0);
		}
	}

	/** Replies the unit vector of this vector.
	 *
	 * @return the unit vector of this vector.
	 */
	@Pure
	default RV toUnitVector() {
		final double length = getLength();
		if (length == 0.) {
			return getGeomFactory().newVector(getSegment());
		}
		return getGeomFactory().newVector(getSegment(), getX() / length, getY() / length);
	}

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	UnmodifiableVector1D<RV, RP, RS> toUnmodifiable();

	/** Replies the geometry factory associated to this point.
	 *
	 * @return the factory.
	 */
	@Pure
	GeomFactory1D<RV, RP> getGeomFactory();

	/** Add a vector to this vector: {@code this += v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #add(Vector1D)
	 */
	@XtextOperator("+=")
	default void operator_add(Vector1D<?, ?, ?> v) {
		add(v);
	}

	/** Substract a vector to this vector: {@code this -= v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #sub(Vector1D)
	 */
	@XtextOperator("-=")
	default void operator_remove(Vector1D<?, ?, ?> v) {
		sub(v);
	}

	/** Scale this vector: {@code this * factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code factor * this} is supported by {@link Tuple2DExtensions#operator_multiply(double, Vector1D)}.
	 *
	 * @param factor the scaling factor.
	 * @return the scaled vector.
	 * @see #scale(double)
	 * @see Tuple2DExtensions#operator_multiply(double, Vector1D)
	 */
	@Pure
	@XtextOperator("*")
	default RV operator_multiply(double factor) {
		return getGeomFactory().newVector(getSegment(), getX() * factor, getY() * factor);
	}

	/** Replies if this vector and the given vector are equal: {@code this == v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple2D)
	 */
	@Pure
	@XtextOperator("==")
	default boolean operator_equals(Tuple2D<?> v) {
		return equals(v);
	}

	/** Replies if this vector and the given vector are different: {@code this != v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple2D)
	 */
	@Pure
	@XtextOperator("!=")
	default boolean operator_notEquals(Tuple2D<?> v) {
		return !equals(v);
	}

	/** Negation of this vector: {@code -this}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @return the result.
	 * @see #negate(Tuple2D)
	 */
	@Pure
	@XtextOperator("(-)")
	default RV operator_minus() {
		return getGeomFactory().newVector(getSegment(), -getX(), -getY());
	}

	/** Subtract a vector to this vector: {@code this - v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #sub(Vector1D)
	 */
	@Pure
	@XtextOperator("-")
	default RV operator_minus(Vector1D<?, ?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newVector(getSegment(), getX() - v.getX(), getY() - v.getY());
	}

	/** Subtract a vector to this scalar: {@code this - scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code scalar - this} is supported by {@link Tuple2DExtensions#operator_minus(double, Vector1D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #sub(Vector1D)
	 * @see Tuple2DExtensions#operator_minus(double, Vector1D)
	 */
	@Pure
	@XtextOperator("-")
	default RV operator_minus(double scalar) {
		return getGeomFactory().newVector(getSegment(), getX() - scalar, getY() - scalar);
	}

	/** Subtract a vector to this point: {@code this - point}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param point the point.
	 * @return the result.
	 * @see #sub(Vector1D)
	 */
	@Pure
	@XtextOperator("-")
	default RP operator_minus(Point1D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(point.getSegment(), getX() - point.getX(), getY() - point.getY());
	}

	/** Scale this vector: {@code this / factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code scalar / this} is supported by {@link Tuple2DExtensions#operator_divide(double, Vector1D)}.
	 *
	 * @param factor the scaling factor
	 * @return the scaled vector.
	 * @see Tuple2DExtensions#operator_divide(double, Vector1D)
	 */
	@Pure
	@XtextOperator("/")
	default RV operator_divide(double factor) {
		return getGeomFactory().newVector(getSegment(), getX() / factor, getY() / factor);
	}

	/** If this vector is epsilon equal to zero then reply v else reply this: {@code this ?: v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the vector.
	 */
	@Pure
	@XtextOperator("?:")
	default Vector1D<? extends RV, ? extends RP, ? extends RS> operator_elvis(
			Vector1D<? extends RV, ? extends RP, ? extends RS> v) {
		if (MathUtil.isEpsilonZero(getX()) && MathUtil.isEpsilonZero(getY())) {
			return v;
		}
		return this;
	}

	/** Sum of this vector and the given vector: {@code this + v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #add(Vector1D, Vector1D)
	 */
	@Pure
	@XtextOperator("+")
	default RV operator_plus(Vector1D<?, ?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newVector(getSegment(), getX() + v.getX(), getY() + v.getY());
	}

	/** Add this vector to a point: {@code this + p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point.
	 * @return the result.
	 */
	@Pure
	@XtextOperator("+")
	default RP operator_plus(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(getSegment(), getX() + pt.getX(), getY() + pt.getY());
	}

	/** Sum of this vector and the given scalar: {@code this + scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code scalar + this} is supported by {@link Tuple2DExtensions#operator_plus(double, Vector1D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #add(Vector1D, Vector1D)
	 */
	@Pure
	@XtextOperator("+")
	default RV operator_plus(double scalar) {
		return getGeomFactory().newVector(getSegment(), getX() + scalar, getY() + scalar);
	}

	/** Scale this vector: {@code this * factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code factor * this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$times(double, Vector1D)}.
	 *
	 * @param factor the scaling factor.
	 * @return the scaled vector.
	 * @see #scale(double)
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$times(double, Vector1D)
	 */
	@Pure
	@ScalaOperator("*")
	default RV $times(double factor) {
		return operator_multiply(factor);
	}

	/** Negation of this vector: {@code -this}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @return the result.
	 * @see #negate(Tuple2D)
	 */
	@Pure
	@ScalaOperator("(-)")
	default RV $minus() {
		return operator_minus();
	}

	/** Subtract a vector to this vector: {@code this - v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #sub(Vector1D)
	 */
	@Pure
	@ScalaOperator("-")
	default RV $minus(Vector1D<?, ?, ?> v) {
		return operator_minus(v);
	}

	/** Subtract a vector to this scalar: {@code this - scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code scalar - this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$minus(double, Vector1D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #sub(Vector1D)
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$minus(double, Vector1D)
	 */
	@Pure
	@ScalaOperator("-")
	default RV $minus(double scalar) {
		return operator_minus(scalar);
	}

	/** Subtract a vector to this point: {@code this - point}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param point the point.
	 * @return the result.
	 * @see #sub(Vector1D)
	 */
	@Pure
	@ScalaOperator("-")
	default RP $minus(Point1D<?, ?, ?> point) {
		return operator_minus(point);
	}

	/** Scale this vector: {@code this / factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code scalar / this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$div(double, Vector1D)}.
	 *
	 * @param factor the scaling factor
	 * @return the scaled vector.
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$div(double, Vector1D)
	 */
	@Pure
	@ScalaOperator("/")
	default RV $div(double factor) {
		return operator_divide(factor);
	}

	/** Sum of this vector and the given vector: {@code this + v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #add(Vector1D, Vector1D)
	 */
	@Pure
	@ScalaOperator("+")
	default RV $plus(Vector1D<?, ?, ?> v) {
		return operator_plus(v);
	}

	/** Add this vector to a point: {@code this + p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param pt the point.
	 * @return the result.
	 */
	@Pure
	@ScalaOperator("+")
	default RP $plus(Point1D<?, ?, ?> pt) {
		return operator_plus(pt);
	}

	/** Sum of this vector and the given scalar: {@code this + scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code scalar + this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$plus(double, Vector1D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #add(Vector1D, Vector1D)
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$plus(double, Vector1D)
	 */
	@Pure
	@ScalaOperator("+")
	default RV $plus(double scalar) {
		return operator_plus(scalar);
	}

}

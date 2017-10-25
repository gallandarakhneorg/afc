/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.text.MessageFormat;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 1.5D Point.
 *
 * @param <RP> is the type of point that can be returned by this point.
 * @param <RV> is the type of vector that can be returned by this point.
 * @param <RS> is the type of segment that can be returned by this point.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:methodcount")
public interface Point1D<RP extends Point1D<? super RP, ? super RV, ? super RS>,
	RV extends Vector1D<? super RV, ? super RP, ? super RS>,
	RS extends Segment1D<?, ?>>
	extends Tuple2D<RP>, Comparable<Point1D<?, ?, ?>> {

	/** Pattern for creation a string representation of a {@link Point1D}.
	 */
	String POINT_STRING_PATTERN = "({0}, {1}, {2})"; //$NON-NLS-1$

	@Override
	default int compareTo(Point1D<?, ?, ?> point) {
		if (point == null) {
			return -1;
		}
		final Segment1D<?, ?> mySegment = getSegment();
		final Segment1D<?, ?> otherSegment = point.getSegment();
		if (mySegment == otherSegment) {
			final int cmp = Double.compare(getX(), point.getX());
			if (cmp == 0) {
				return Double.compare(getY(), point.getY());
			}
			return cmp;
		}
		final int h1 = mySegment != null ? mySegment.hashCode() : 0;
		final int h2 = otherSegment != null ? otherSegment.hashCode() : 0;
		return h1 - h2;
	}

	/** Replies the string representation of the tuple.
	 *
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the string representation.
	 */
	static String toString(Segment1D<?, ?> segment, double x, double y) {
		return MessageFormat.format(POINT_STRING_PATTERN, Objects.toString(segment), x, y);
	}

	/** Replies the string representation of the tuple.
	 *
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the string representation.
	 */
	static String toString(Segment1D<?, ?> segment, int x, int y) {
		return MessageFormat.format(POINT_STRING_PATTERN, Objects.toString(segment), x, y);
	}

	/** Clamp the curviline coordinate to the segment.
	 *
	 * @return the amount that was removed from the coordinate. If the amount
	 *     is negative it means that the coordinate was negative and clamped to zero.
	 *     If the amount if positive it means that the coordinate was greater than
	 *     the segment length and clamped to this length. If the amount
	 *     is equal to zero it mean that the coordinate was not clamped.
	 */
	default double clamp() {
		double clamped = 0.;
		double curv = getX();
		if (curv < 0.) {
			clamped = curv;
			curv = 0.;
		} else {
			double length = 0.;
			final Segment1D<?, ?> sgmt = getSegment();
			if (sgmt != null) {
				length = sgmt.getLength();
			}
			if (curv > length) {
				clamped = curv - length;
				curv = length;
			}
		}
		setX(curv);
		return clamped;
	}

	/** Replies if this pint is located on the segment.
	 *
	 * <p>The point is located on the segment only if its curviline
	 * coordinate is in <code>[0;length]</code>, where
	 * <code>length</code> is the length of the segment.
	 *
	 * @return <code>true</code> if the point is on the segment,
	 *     otherwise <code>false</code>
	 */
	@Pure
	default boolean isOnSegment() {
		final double curv = getX();
		if (curv >= 0.) {
			double length = 0.;
			final Segment1D<?, ?> sgmt = getSegment();
			if (sgmt != null) {
				length = sgmt.getLength();
			}
			return curv <= length;
		}
		return false;
	}

	/** Replies if this point is located on the same segment as the given one.
	 *
	 * @param point the point to test.
	 * @return <code>true</code> if the points are on the same segment,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	default boolean isOnSameSegment(Point1D<?, ?, ?> point) {
		assert point != null;
		return getSegment() == point.getSegment();
	}

	@Override
	boolean equals(Object object);

	/** Replies if this point is equals to the given point.
	 *
	 * @param tuple the point to test.
	 * @return <code>true</code> if this point has the same coordinates
	 *     on the same segment as for the given point.
	 */
	@Pure
	default boolean equals(Point1D<?, ?, ?> tuple) {
		try {
			return getSegment() == tuple.getSegment() && getX() == tuple.getX() && getY() == tuple.getY();
		} catch (Throwable exception) {
			return false;
		}
	}

	/** Returns <code>true</code> if the L-infinite distance between this point and point
	 * <var>p</var> is less than or equal to the epsilon parameter,
	 * otherwise returns <code>false</code>. The L-infinite
	 * distance is equal to MAX[abs(curviline-curviline), abs(curviline-curviline)].
	 *
	 * <p>If the points are not on the same segments, they are not equals.
	 *
	 * @param tuple the point to be compared to this point
	 * @param epsilon the threshold value.
	 * @return <code>true</code> if the points are equals, otherwise <code>false</code>
	 */
	@Pure
	default boolean epsilonEquals(Point1D<?, ?, ?> tuple, double epsilon) {
		assert tuple != null : AssertMessages.notNullParameter(0);
		if (getSegment() == tuple.getSegment()) {
			final double dx = getX() - tuple.getX();
			final double dy = getY() - tuple.getY();
			return (dx * dx + dy * dy) <= (epsilon * epsilon);
		}
		return false;
	}

	/**
	 * Returns a hash number based on the data values in this object.
	 * Two different Point1D5 objects with identical data  values
	 * (ie, returns true for equals(Point1D5) ) will return the same hash number.
	 * Two vectors with different data members may return the same hash value,
	 * although this is not likely.
	 */
	@Pure
	@Override
	int hashCode();

	/** Add the given values to this point.
	 *
	 * @param curvilineMove is the quantity to add to the curviline coordinate.
	 */
	default void add(double curvilineMove) {
		setX(getX() + curvilineMove);
	}

	/** Substract the given values to this point.
	 *
	 * @param curvilineMove is the quantity to substract to the curviline coordinate.
	 */
	default void sub(double curvilineMove) {
		setX(getX() - curvilineMove);
	}

	/**
	 * Computes the square of the distance between this point and point p1.
	 *
	 * @param p1
	 *            the other point
	 * @return the square of the distance
	 */
	@Pure
	default double getDistanceSquared(Point1D<?, ?, ?> p1) {
		if (isOnSameSegment(p1)) {
			final double a = getX() - p1.getX();
			final double b = getY() - p1.getY();
			return a * a + b * b;
		}
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * Computes the distance between this point and point p1.
	 *
	 * @param p1
	 *            the other point
	 * @return the distance or {@link Double#POSITIVE_INFINITY} if
	 *     not on the same segments.
	 */
	@Pure
	default double getDistance(Point1D<?, ?, ?> p1) {
		if (isOnSameSegment(p1)) {
			final double a = getX() - p1.getX();
			final double b = getY() - p1.getY();
			return Math.sqrt(a * a + b * b);
		}
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and point p1.
	 * The L-1 distance is equal to abs(curviline1-curviline2) + abs(jutting1-jutting2).
	 *
	 * @param p1
	 *            the other point
	 * @return the manhattan distance or {@link Double#POSITIVE_INFINITY} if
	 *     not on the same segments.
	 */
	@Pure
	default double getDistanceL1(Point1D<?, ?, ?> p1) {
		if (isOnSameSegment(p1)) {
			return Math.abs(getX() - p1.getX()) + Math.abs(getY() - p1.getY());
		}
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * Computes the L-infinite distance between this point and point p1. The
	 * L-infinite distance is equal to MAX[abs(curviline1-curviline2), abs(jutting1-jutting2)].
	 *
	 * @param p1
	 *            the other point
	 * @return the L-infinite distance or {@link Double#POSITIVE_INFINITY} if
	 *      not on the same segments.
	 */
	@Pure
	default double getDistanceLinf(Point1D<?, ?, ?> p1) {
		if (isOnSameSegment(p1)) {
			return Math.max(Math.abs(getX() - p1.getX()), Math.abs(getY() - p1.getY()));
		}
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * Computes the curviline distance between this point and point p1.
	 * The curviline distance is equal to abs(curviline1-curviline2).
	 *
	 * @param p1
	 *            the other point
	 * @return the curviline distance or {@link Double#POSITIVE_INFINITY} if
	 *     not on the same segments.
	 */
	@Pure
	default double getDistanceCurviline(Point1D<?, ?, ?> p1) {
		return isOnSameSegment(p1) ? Math.abs(getX() - p1.getX()) : Double.POSITIVE_INFINITY;
	}

	/**
	 * Computes the jutting distance between this point and point p1.
	 * The jutting distance is equal to abs(jutting1-jutting2).
	 *
	 * @param p1
	 *            the other point
	 * @return the jutting distance or {@link Double#POSITIVE_INFINITY} if
	 *      not on the same segments.
	 */
	@Pure
	default double getDistanceShift(Point1D<?, ?, ?> p1) {
		return isOnSameSegment(p1) ? Math.abs(getY() - p1.getY()) : Double.POSITIVE_INFINITY;
	}

	/** Replies the curviline coordinate.
	 *
	 * @return the curviline coordinate.
	 */
	@Pure
	@Inline(value = "getX()")
	default double getCurvilineCoordinate() {
		return getX();
	}

	/** Set the curviline coordinate.
	 *
	 * @param curviline is the curviline coordinate.
	 */
	@Inline(value = "setX($1)")
	default void setCurvilineCoordinate(double curviline) {
		setX(curviline);
	}

	/** Replies the shift distance.
	 *
	 * @return the shift distance.
	 */
	@Pure
	@Inline(value = "getY()")
	default double getJuttingDistance() {
		return getY();
	}

	/** Set the shift distance.
	 *
	 * @param shift is the shift distance.
	 */
	@Inline(value = "setY($1)")
	default void setJuttingDistance(double shift) {
		setY(shift);
	}

	/** Replies the segment.
	 *
	 * @return the segment or <code>null</code> if the weak reference has lost the segment.
	 */
	@Pure
	RS getSegment();

	/** Set the segment.
	 *
	 * @param segment is the segment.
	 */
	@Inline(value = "set($1, $0.getX(), $0.getY()")
	default void setSegment(RS segment) {
		set(segment, getX(), getY());
	}

	/**
	 * Set this point from the given informations.
	 *
	 * @param segment
	 *            the segment
	 * @param curviline
	 *            the curviline coordinate
	 * @param shift
	 *            the jutting coordinate
	 */
	void set(RS segment, double curviline, double shift);

	/**
	 * Set this point from the given informations.
	 *
	 * @param segment
	 *            the segment
	 * @param tuple are the coordinates of the point.
	 */
	@Pure
	@Inline(value = "set($1, ($2).getX(), ($2).getY())")
	default void set(RS segment, Tuple2D<?> tuple) {
		set(segment, tuple.getX(), tuple.getY());
	}

	/**
	 * Set this point from the given informations.
	 *
	 * @param point are the coordinates of the point.
	 */
	@Inline(value = "set(($1).getSegment(), ($1).getX(), ($1).getY())")
	default void set(Point1D<? extends RP, ? extends RV, ? extends RS> point) {
		set(point.getSegment(), point.getX(), point.getY());
	}

	/** Replies an unmodifiable copy of this point.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	UnmodifiablePoint1D<RP, RV, RS> toUnmodifiable();

	/** Replies the geometry factory associated to this point.
	 *
	 * @return the factory.
	 */
	@Pure
	GeomFactory1D<RV, RP> getGeomFactory();

	/** Sum of this point and a vector: {@code this + v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @return the result.
	 */
	@Pure
	@XtextOperator("+")
	default RP operator_plus(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(getSegment(), getX() + v.getX(), getY() + v.getY());
	}

	/** Sum of this point and a scalar: {@code this + scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code v + this} is supported by {@link Tuple2DExtensions#operator_plus(double, Point2D)}.
	 *
	 * @param scalar the scalar
	 * @return the result.
	 * @see Tuple2DExtensions#operator_plus(double, Point2D)
	 */
	@Pure
	@XtextOperator("+")
	default RP operator_plus(double scalar) {
		return getGeomFactory().newPoint(getSegment(), getX() + scalar, getY() + scalar);
	}

	/** Increment this point with the given vector: {@code this += v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to add
	 * @see #add(double, double)
	 */
	@XtextOperator("+=")
	default void operator_add(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		add(v.getX(), v.getY());
	}

	/** Subtract the v vector to this point: {@code this - v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to substract.
	 * @return the result.
	 */
	@Pure
	@XtextOperator("-")
	default RP operator_minus(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(getSegment(), getX() - v.getX(), getY() - v.getY());
	}

	/** Subtract the scalar to this point: {@code this - scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code scalar - this} is supported by {@link Tuple2DExtensions#operator_minus(double, Point2D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see Tuple2DExtensions#operator_minus(double, Point2D)
	 */
	@Pure
	@XtextOperator("-")
	default RP operator_minus(double scalar) {
		return getGeomFactory().newPoint(getSegment(), getX() - scalar, getY() - scalar);
	}

	/** Subtract the p point to this point: {@code this - p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point to substract
	 * @return the vector from the p to this.
	 * @see Vector2D#sub(Point2D, Point2D)
	 */
	@Pure
	@XtextOperator("-")
	default RV operator_minus(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return getGeomFactory().newVector(getSegment(), getX() - pt.getX(), getY() - pt.getY());
	}

	/** Subtract the v vector to this: {@code this -= v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector to substract.
	 */
	@XtextOperator("-=")
	default void operator_remove(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		sub(v.getX(), v.getY());
	}

	/** Replies if the given vector is equal to this vector: {@code this == v}.
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
	@Inline("equals($1)")
	default boolean operator_equals(Tuple2D<?> v) {
		return equals(v);
	}

	/** Replies if the given vector is different than this vector: {@code this != v}.
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

	/** Replies if the distance between this and the p point: {@code this .. p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point.
	 * @return the distance.
	 * @see #getDistance(Point1D)
	 */
	@Pure
	@XtextOperator("..")
	@Inline("value = getDistance($1)")
	default double operator_upTo(Point1D<?, ?, ?> pt) {
		return getDistance(pt);
	}

	/** If this point is epsilon equal to zero then reply p else reply this: {@code this ?: p}.
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
	default Point1D<? extends RP, ? extends RV, ? extends RS> operator_elvis(
			Point1D<? extends RP, ? extends RV, ? extends RS> pt) {
		if (MathUtil.isEpsilonZero(getX()) && MathUtil.isEpsilonZero(getY())) {
			return pt;
		}
		return this;
	}

	/** Sum of this point and a vector: {@code this + v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector to add
	 * @return the result.
	 */
	@Pure
	@ScalaOperator("+")
	default RP $plus(Vector2D<?, ?> v) {
		return operator_plus(v);
	}

	/** Sum of this point and a scalar: {@code this + scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code v + this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$plus(double, Point2D)}.
	 *
	 * @param scalar the scalar
	 * @return the result.
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$plus(double, Point2D)
	 */
	@Pure
	@ScalaOperator("+")
	default RP $plus(double scalar) {
		return operator_plus(scalar);
	}

	/** Subtract the v vector to this point: {@code this - v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector to substract.
	 * @return the result.
	 */
	@Pure
	@ScalaOperator("-")
	default RP $minus(Vector2D<?, ?> v) {
		return operator_minus(v);
	}

	/** Subtract the scalar to this point: {@code this - scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code scalar - this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$minus(double, Point2D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$minus(double, Point2D)
	 */
	@Pure
	@ScalaOperator("-")
	default RP $minus(double scalar) {
		return operator_minus(scalar);
	}

	/** Subtract the p point to this point: {@code this - p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param pt the point to substract
	 * @return the vector from the p to this.
	 */
	@Pure
	@ScalaOperator("-")
	default RV $minus(Point2D<?, ?> pt) {
		return operator_minus(pt);
	}

}

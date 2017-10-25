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

package org.arakhne.afc.math.geometry.d2;

import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 2D Vector.
 *
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:methodcount")
public interface Vector2D<RV extends Vector2D<? super RV, ? super RP>, RP extends Point2D<? super RP, ? super RV>>
		extends Tuple2D<RV> {

	/**
	 * Replies if the vector is a unit vector.
	 *
	 * <p>Due to the precision on floating-point computations, the test of unit-vector
	 * must consider that the norm of the given vector is approximatively equal
	 * to 1. The precision (i.e. the number of significant decimals) is given
	 * by {@link MathConstants#UNIT_VECTOR_EPSILON}.
	 *
	 * @param x is the X coordinate of the vector.
	 * @param y is the Y coordinate of the vector.
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 13.0
	 * @see MathUtil#isEpsilonEqual(double, double, double)
	 * @see MathConstants#UNIT_VECTOR_EPSILON
	 * @see #isUnitVector(double, double, double)
	 */
	@Pure
	@Inline(value = "Vector2D.isUnitVector(($1), ($2), MathConstants.UNIT_VECTOR_EPSILON)",
			imported = {Vector2D.class, MathConstants.class})
	static boolean isUnitVector(double x, double y) {
		return isUnitVector(x, y, MathConstants.UNIT_VECTOR_EPSILON);
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
	@Inline(value = "MathUtil.isEpsilonEqual(($1) * ($1) + ($2) * ($2), 1., ($3))",
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

	/**
	 * Replies if the vectors are orthogonal vectors.
	 *
	 * <p>Due to the precision on floating-point computations, the test of orthogonality
	 * is approximated. The default epsilon is {@link MathConstants#ORTHOGONAL_VECTOR_EPSILON}.
	 *
	 * @param x1 is the X coordinate of the first unit vector.
	 * @param y1 is the Y coordinate of the first unit vector.
	 * @param x2 is the X coordinate of the second unit vector.
	 * @param y2 is the Y coordinate of the second unit vector.
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 13.0
	 * @see MathUtil#isEpsilonEqual(double, double, double)
	 * @see MathConstants#ORTHOGONAL_VECTOR_EPSILON
	 * @see #isUnitVector(double, double, double)
	 */
	@Pure
	@Inline(value = "Vector2D.isOrthogonal(($1), ($2), ($3), ($4), MathConstants.ORTHOGONAL_VECTOR_EPSILON)",
			imported = {Vector2D.class, MathConstants.class})
	static boolean isOrthogonal(double x1, double y1, double x2, double y2) {
		return isOrthogonal(x1, y1, x2, y2, MathConstants.ORTHOGONAL_VECTOR_EPSILON);
	}

	/**
	 * Replies if the vectors are orthogonal vectors.
	 *
	 * @param x1 is the X coordinate of the first unit vector.
	 * @param y1 is the Y coordinate of the first unit vector.
	 * @param x2 is the X coordinate of the second unit vector.
	 * @param y2 is the Y coordinate of the second unit vector.
	 * @param epsilon the precision distance to assumed for equality.
	 * @return <code>true</code> if the two given vectors are orthogonal.
	 * @since 13.0
	 * @see #isOrthogonal(double, double, double,  double)
	 */
	@Pure
	@Inline(value = "MathUtil.isEpsilonZero(Vector2D.dotProduct(($1), ($2), ($3), ($4)), ($5))",
			imported = {Vector2D.class, MathUtil.class})
	static boolean isOrthogonal(double x1, double y1, double x2, double y2, double epsilon) {
		return MathUtil.isEpsilonZero(dotProduct(x1, y1, x2, y2), epsilon);
	}

	/** Replies if this vector is orthogonal to the given vector.
	 *
	 * @param vector the vector to compare to this vector.
	 * @return <code>true</code> if the vectors are orthogonal.
	 * <code>false</code> otherwise.
	 */
	@Pure
	default boolean isOrthogonal(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return isOrthogonal(getX(), getY(), vector.getX(), vector.getY());
	}

	/**
	 * Replies if two vectors are colinear.
	 *
	 * <p>This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 *
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 13.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	@Inline(value = "MathUtil.isEpsilonZero(($1) * ($4) - ($3) * ($2))",
			imported = {MathUtil.class})
	static boolean isCollinearVectors(double x1, double y1, double x2, double y2) {
		// Test if three points are colinears
		// iff det( [ x1 x2 ]
		// [ y1 y2 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computation consumption.
		return MathUtil.isEpsilonZero(x1 * y2 - x2 * y1);
	}

	/** Compute the the perpendicular product of
	 * the two vectors (aka. the determinant of two vectors).
	 *
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>.
	 *
	 * <p>Let consider that dotProduct projects the point (px,py) on the
	 * Ox axis, and perpProduct projects the point (py,py) on the Oy
	 * axis. Then:
	 * <pre><code>perpProduct(ax, ay, px, py) = dotProduct(px, py, -ay, ax)</code></pre>
	 * You could note that the semantics of the parameters differ:<ul>
	 * <li><code>perpProduct(axisX, axisY, pointX, pointY)</code></li>
	 * <li><code>dotProduct(pointX, pointY, axisX, axisY)</code></li>
	 * </ul>
	 *
	 * @param x1 x coordinate of the first vector.
	 * @param y1 y coordinate of the first vector.
	 * @param x2 x coordinate of the second vector.
	 * @param y2 y coordinate of the second vector.
	 * @return the determinant
	 */
	@Pure
	@Inline(value = "($1) * ($4) - ($3) * ($2)")
	static double perpProduct(double x1, double y1, double x2, double y2) {
		return x1 * y2 - x2 * y1;
	}

	/** Compute the dot product of two vectors.
	 *
	 * <p>Let consider that dotProduct projects the point (px,py) on the
	 * Ox axis, and perpProduct projects the point (py,py) on the Oy
	 * axis. Then:
	 * <pre><code>perpProduct(ax, ay, px, py) = dotProduct(px, py, -ay, ax)</code></pre>
	 * You could note that the semantics of the parameters differ:<ul>
	 * <li><code>perpProduct(axisX, axisY, pointX, pointY)</code></li>
	 * <li><code>dotProduct(pointX, pointY, axisX, axisY)</code></li>
	 * </ul>
	 *
	 * @param x1 x coordinate of the first vector.
	 * @param y1 y coordinate of the first vector.
	 * @param x2 x coordinate of the second vector.
	 * @param y2 y coordinate of the second vector.
	 * @return the dot product.
	 */
	@Pure
	@Inline(value = "($1) * ($3) + ($2) * ($4)")
	static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1 * x2 + y1 * y2;
	}

	/** Replies if the vectors are defined in a counter-clockwise order.
	 *
	 * <p>The two vectors are defined in a counter-clockwise order if the sinus
	 * from the first vector to the second vector is positive.
	 *
	 * <p>In other words, let the angle between the two vectors that
	 * is replied by {@link #signedAngle(double, double, double, double)}.
	 * The vectors are defined in an counter-clockwise order if the angle
	 * is positive.
	 *
	 * @param x1 the first coordinate of the first vector.
	 * @param y1 the second coordinate of the first vector.
	 * @param x2 the first coordinate of the second vector.
	 * @param y2 the second coordinate of the second vector.
	 * @return <code>true</code> if the vectors are defined in a counter-clockwise order.
	 * @see #signedAngle(double, double, double, double)
	 * @see #perpProduct(double, double, double, double)
	 */
	@Pure
	@Inline("(($1) * ($4) - ($2) * ($3)) >= 0.")
	static boolean isCCW(double x1, double y1, double x2, double y2) {
		return (x1 * y2 - y1 * x2) >= 0.;
	}

	/**
	 * Compute the signed angle between two vectors.
	 *
	 * @param x1 the first coordinate of the first vector.
	 * @param y1 the second coordinate of the first vector.
	 * @param x2 the first coordinate of the second vector.
	 * @param y2 the second coordinate of the second vector.
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 * @see #isCCW(double, double, double, double)
	 */
	@Pure
	static double signedAngle(double x1, double y1, double x2, double y2) {
		final double length1 = Math.sqrt(x1 * x1 + y1 * y1);
		final double length2 = Math.sqrt(x2 * x2 + y2 * y2);

		if ((length1 == 0.) || (length2 == 0.)) {
			return Double.NaN;
		}

		double cx1 = x1;
		double cy1 = y1;
		double cx2 = x2;
		double cy2 = y2;

		// A and B are normalized
		if (length1 != 1.) {
			cx1 /= length1;
			cy1 /= length1;
		}

		if (length2 != 1.) {
			cx2 /= length2;
			cy2 /= length2;
		}

		// Second method
		// A . B = |A|.|B|.cos(theta) = cos(theta)
		final double cos = cx1 * cx2 + cy1 * cy2;
		// A x B = |A|.|B|.sin(theta).N = sin(theta) (where N is the unit vector perpendicular to plane AB)
		final double sin = cx1 * cy2 - cy1 * cx2;

		return Math.atan2(sin, cos);
	}

	/** Compute a signed angle between this vector and the given vector.
	 *
	 * <p>The signed angle between this vector and the given {@code vector}
	 * is the rotation angle to apply to this vector
	 * to be colinear to the given {@code vector} and pointing the
	 * same demi-plane. It means that the angle replied
	 * by this function is be negative if the rotation
	 * to apply is clockwise, and positive if
	 * the rotation is counterclockwise.
	 *
	 * <p>The value replied by {@link #angle(Vector2D)}
	 * is the absolute value of the vlaue replied by this
	 * function.
	 *
	 * @param vector is the vector to reach.
	 * @return the rotation angle to turn this vector to reach
	 * {@code v}.
	 */
	@Pure
	default double signedAngle(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return signedAngle(getX(), getY(), vector.getX(), vector.getY());
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *
	 *  <p>The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x2-x1;y2-y1).
	 *
	 * @param x1 is the coordinate of the vector origin point.
	 * @param y1 is the coordinate of the vector origin point.
	 * @param x2 is the coordinate of the vector target point.
	 * @param y2 is the coordinate of the vector target point.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 */
	@Pure
	@Inline(value = "Vector2D.signedAngle(1, 0, ($3) - ($1), ($4) - ($2))",
			imported = {Vector2D.class})
	static double angleOfVector(double x1, double y1, double x2, double y2) {
		return signedAngle(
				1, 0,
				x2 - x1, y2 - y1);
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *
	 * <p>The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x;y).
	 *
	 * @param x is the coordinate of the vector.
	 * @param y is the coordinate of the vector.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 */
	@Pure
	@Inline(value = "Vector2D.signedAngle(1, 0, ($1), ($2))",
			imported = {Vector2D.class})
	static double angleOfVector(double x, double y) {
		return signedAngle(
				1, 0,
				x, y);
	}

	/**
	 * Sets the value of this tuple to the sum of tuples vector1 and vector2.
	 *
	 * @param vector1 the first tuple
	 * @param vector2 the second tuple
	 */
	default void add(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
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
	default void add(Vector2D<?, ?> vector) {
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
	default void scaleAdd(int scale, Vector2D<?, ?> t1, Vector2D<?, ?> t2) {
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
	default void scaleAdd(double scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
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
	default void scaleAdd(int scale, Vector2D<?, ?> vector) {
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
	default void scaleAdd(double scale, Vector2D<?, ?> vector) {
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
	default void sub(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
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
	default void sub(Point2D<?, ?> point1, Point2D<?, ?> point2) {
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
	default void sub(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		set(getX() - vector.getX(), getY() - vector.getY());
	}

	/** Compute the power of this vector.
	 *
	 * <p>If the power is even, the result is a scalar.
	 * If the power is odd, the result is a vector.
	 *
	 * @param power the power factor.
	 * @return the power of this vector.
	 * @see "http://www.euclideanspace.com/maths/algebra/vectors/vecAlgebra/powers/index.htm"
	 */
	@Pure
	default PowerResult<RV> power(int power) {
		final boolean isEven = power % 2 == 0;
		final int evenPower;
		if (isEven) {
			evenPower = power / 2;
		} else {
			evenPower = MathUtil.sign(power) * (Math.abs(power) - 1) / 2;
		}
		final double x = getX();
		final double y = getY();
		final double dot = dotProduct(x, y, x, y);
		final double resultForEven = Math.pow(dot, evenPower);
		if (isEven) {
			return new PowerResult<>(resultForEven);
		}
		final RV r = getGeomFactory().newVector(getX() * resultForEven, getY() * resultForEven);
		return new PowerResult<>(r);

	}

	/**
	 * Computes the dot product of the this vector and the given vector.
	 *
	 * @param vector the other vector
	 * @return the dot product.
	 */
	@Pure
	default double dot(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return dotProduct(getX(), getY(), vector.getX(), vector.getY());
	}

	/** Compute the the perpendicular product of
	 * the two vectors (aka. the determinant of two vectors).
	 *
	 * <pre><code>det(X1, X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>.
	 *
	 * @param vector the vertor.
	 * @return the determinant
	 */
	@Pure
	default double perp(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return perpProduct(getX(), getY(), vector.getX(), vector.getY());
	}

	/** Change the coordinates of this vector to make it an orthogonal
	 * vector to the original coordinates.
	 */
	default void makeOrthogonal() {
		set(-getY(), getX());
	}

	/** Replies the orthogonal vector to this vector.
	 *
	 * @return the orthogonal vector.
	 */
	@Pure
	default RV toOrthogonalVector() {
		return getGeomFactory().newVector(-getY(), getX());
	}

	/** Replies a vector of the given length that is colinear to this vector.
	 *
	 * @param length the length of the new vector.
	 * @return the colinear vector.
	 */
	@Pure
	default RV toColinearVector(double length) {
		assert length >= 0. : AssertMessages.positiveOrZeroParameter();
		final double len = getLength();
		if (len != 0.) {
			final double x = (length * getX()) / len;
			final double y = (length * getY()) / len;
			return getGeomFactory().newVector(x, y);
		}
		return getGeomFactory().newVector();
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
		return Math.sqrt(x * x + y * y);
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
	default void normalize(Vector2D<?, ?> vector) {
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


	/**
	 * Returns the angle in radians between this vector and the vector
	 * parameter; the return value is constrained to the range [0, PI].
	 *
	 * @param vector the other vector
	 * @return the angle in radians in the range [0, PI]
	 */
	@Pure
	default double angle(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		double vDot = dotProduct(getX(), getY(), vector.getX(), vector.getY()) / (getLength() * vector.getLength());
		if (vDot < -1.) {
			vDot = -1.;
		}
		if (vDot >  1.) {
			vDot =  1.;
		}
		return Math.acos(vDot);
	}

	/** Turn this vector about the given rotation angle.
	 *
	 * <p>The rotation is done according to the trigonometric coordinate.
	 * A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @deprecated since 13.0, {@link #turn(double)}
	 */
	@Deprecated
	default void turnVector(double angle) {
		turn(angle);
	}

	/** Turn this vector about the given rotation angle.
	 *
	 * <p>The rotation is done according to the trigonometric coordinate.
	 * A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @see #turn(double, Vector2D)
	 * @see #turnLeft(double)
	 * @see #turnRight(double)
	 */
	default void turn(double angle) {
		turn(angle, this);
	}

	/** Turn the given vector about the given rotation angle, and set this
	 * vector with the result.
	 *
	 * <p>The rotation is done according to the trigonometric coordinate.
	 * A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param vectorToTurn the vector to turn.
	 * @see #turn(double)
	 * @see #turnLeft(double)
	 * @see #turnRight(double)
	 */
	default void turn(double angle, Vector2D<?, ?> vectorToTurn) {
		assert vectorToTurn != null : AssertMessages.notNullParameter(1);
		final double sin = Math.sin(angle);
		final double cos = Math.cos(angle);
		final double x =  cos * vectorToTurn.getX() - sin * vectorToTurn.getY();
		final double y =  sin * vectorToTurn.getX() + cos * vectorToTurn.getY();
		set(x, y);
	}

	/** Turn this vector on the left when the given rotation angle is positive.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @see CoordinateSystem2D
	 * @see #turnLeft(double, Vector2D)
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
	 * @param vectorToTurn the vector to turn.
	 * @see CoordinateSystem2D
	 * @see #turnLeft(double, Vector2D)
	 * @see #turn(double)
	 * @see #turnRight(double)
	 */
	default void turnLeft(double angle, Vector2D<?, ?> vectorToTurn) {
		final double sin = Math.sin(angle);
		final double cos = Math.cos(angle);
		final double x;
		final double y;
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			x =  cos * vectorToTurn.getX() - sin * vectorToTurn.getY();
			y =  sin * vectorToTurn.getX() + cos * vectorToTurn.getY();
		} else {
			x =  cos * vectorToTurn.getX() + sin * vectorToTurn.getY();
			y = -sin * vectorToTurn.getX() + cos * vectorToTurn.getY();
		}
		set(x, y);
	}

	/** Turn this vector on the right when the given rotation angle is positive.
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

	/** Turn this vector on the right when the given rotation angle is positive.
	 *
	 * <p>A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @param vectorToTurn the vector to turn.
	 * @see CoordinateSystem2D
	 * @see #turn(double)
	 * @see #turnLeft(double)
	 */
	default void turnRight(double angle, Vector2D<?, ?> vectorToTurn) {
		turnLeft(-angle, vectorToTurn);
	}

	/** Replies the orientation angle on a trigonometric circle
	 * that is corresponding to the given direction of this vector.
	 *
	 * @return the angle on a trigonometric circle that is corresponding
	 *      to the given orientation vector.
	 */
	@Pure
	default double getOrientationAngle() {
		final double angle = Math.acos(getX());
		if (getY() < 0f) {
			return MathConstants.TWO_PI - angle;
		}
		return angle;
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
			return getGeomFactory().newVector();
		}
		return getGeomFactory().newVector(getX() / length, getY() / length);
	}

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	UnmodifiableVector2D<RV, RP> toUnmodifiable();

	/** Replies the geometry factory associated to this point.
	 *
	 * @return the factory.
	 */
	@Pure
	GeomFactory2D<RV, RP> getGeomFactory();

	/** Add a vector to this vector: {@code this += v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #add(Vector2D)
	 */
	@XtextOperator("+=")
	default void operator_add(Vector2D<?, ?> v) {
		add(v);
	}

	/** Substract a vector to this vector: {@code this -= v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #sub(Vector2D)
	 */
	@XtextOperator("-=")
	default void operator_remove(Vector2D<?, ?> v) {
		sub(v);
	}

	/** Dot product: {@code this * v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #dot(Vector2D)
	 */
	@Pure
	@XtextOperator("*")
	default double operator_multiply(Vector2D<?, ?> v) {
		return dot(v);
	}

	/** Scale this vector: {@code this * factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code factor * this} is supported by {@link Tuple2DExtensions#operator_multiply(double, Vector2D)}.
	 *
	 * @param factor the scaling factor.
	 * @return the scaled vector.
	 * @see #scale(double)
	 * @see Tuple2DExtensions#operator_multiply(double, Vector2D)
	 */
	@Pure
	@XtextOperator("*")
	default RV operator_multiply(double factor) {
		return getGeomFactory().newVector(getX() * factor, getY() * factor);
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

	/** Replies if the absolute angle between this and v: {@code this .. b}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the signed angle.
	 * @see #angle(Vector2D)
	 */
	@Pure
	@XtextOperator("..")
	default double operator_upTo(Vector2D<?, ?> v) {
		return angle(v);
	}

	/** Replies the signed angle from this to v: {@code this >.. v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the signed angle.
	 * @see #signedAngle(Vector2D)
	 */
	@Pure
	@XtextOperator(">..")
	default double operator_greaterThanDoubleDot(Vector2D<?, ?> v) {
		return signedAngle(v);
	}

	/** Replies the signed angle from v to this: {@code this ..< v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the signed angle.
	 * @see #signedAngle(Vector2D)
	 */
	@Pure
	@XtextOperator("..<")
	default double operator_doubleDotLessThan(Vector2D<?, ?> v) {
		return -signedAngle(v);
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
		return getGeomFactory().newVector(-getX(), -getY());
	}

	/** Subtract a vector to this vector: {@code this - v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #sub(Vector2D)
	 */
	@Pure
	@XtextOperator("-")
	default RV operator_minus(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newVector(getX() - v.getX(), getY() - v.getY());
	}

	/** Subtract a vector to this scalar: {@code this - scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code scalar - this} is supported by {@link Tuple2DExtensions#operator_minus(double, Vector2D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #sub(Vector2D)
	 * @see Tuple2DExtensions#operator_minus(double, Vector2D)
	 */
	@Pure
	@XtextOperator("-")
	default RV operator_minus(double scalar) {
		return getGeomFactory().newVector(getX() - scalar, getY() - scalar);
	}

	/** Subtract a vector to this point: {@code this - point}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param point the point.
	 * @return the result.
	 * @see #sub(Vector2D)
	 */
	@Pure
	@XtextOperator("-")
	default RP operator_minus(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(getX() - point.getX(), getY() - point.getY());
	}

	/** Scale this vector: {@code this / factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code scalar / this} is supported by {@link Tuple2DExtensions#operator_divide(double, Vector2D)}.
	 *
	 * @param factor the scaling factor
	 * @return the scaled vector.
	 * @see Tuple2DExtensions#operator_divide(double, Vector2D)
	 */
	@Pure
	@XtextOperator("/")
	default RV operator_divide(double factor) {
		return getGeomFactory().newVector(getX() / factor, getY() / factor);
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
	default Vector2D<? extends RV, ? extends RP> operator_elvis(Vector2D<? extends RV, ? extends RP> v) {
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
	 * @see #add(Vector2D, Vector2D)
	 */
	@Pure
	@XtextOperator("+")
	default RV operator_plus(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newVector(getX() + v.getX(), getY() + v.getY());
	}

	/** Add this vector to a point: {@code this + p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point.
	 * @return the result.
	 * @see Point2D#add(Vector2D, Point2D)
	 */
	@Pure
	@XtextOperator("+")
	default RP operator_plus(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return getGeomFactory().newPoint(getX() + pt.getX(), getY() + pt.getY());
	}

	/** Sum of this vector and the given scalar: {@code this + scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * <p>The operation {@code scalar + this} is supported by {@link Tuple2DExtensions#operator_plus(double, Vector2D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #add(Vector2D, Vector2D)
	 * @see Tuple2DExtensions#operator_plus(double, Vector2D)
	 */
	@Pure
	@XtextOperator("+")
	default RV operator_plus(double scalar) {
		return getGeomFactory().newVector(getX() + scalar, getY() + scalar);
	}

	/** Perp product of this vector and the given vector: {@code this ** v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the other vector.
	 * @return the result.
	 * @see #perp(Vector2D)
	 */
	@Pure
	@XtextOperator("**")
	default double operator_power(Vector2D<?, ?> v) {
		return perp(v);
	}

	/** Compute the power of this vector: {@code this ** n}.
	 *
	 * <p>If the power is even, the result is a scalar.
	 * If the power is odd, the result is a vector.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param power the power factor.
	 * @return the power of this vector.
	 * @see #power(int)
	 * @see "http://www.euclideanspace.com/maths/algebra/vectors/vecAlgebra/powers/index.htm"
	 */
	@Pure
	@XtextOperator("**")
	default PowerResult<RV> operator_power(int power) {
		return power(power);
	}

	/** Dot product: {@code this * v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #dot(Vector2D)
	 */
	@Pure
	@ScalaOperator("*")
	default double $times(Vector2D<?, ?> v) {
		return operator_multiply(v);
	}

	/** Scale this vector: {@code this * factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code factor * this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$times(double, Vector2D)}.
	 *
	 * @param factor the scaling factor.
	 * @return the scaled vector.
	 * @see #scale(double)
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$times(double, Vector2D)
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
	 * @see #sub(Vector2D)
	 */
	@Pure
	@ScalaOperator("-")
	default RV $minus(Vector2D<?, ?> v) {
		return operator_minus(v);
	}

	/** Subtract a vector to this scalar: {@code this - scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code scalar - this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$minus(double, Vector2D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #sub(Vector2D)
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$minus(double, Vector2D)
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
	 * @see #sub(Vector2D)
	 */
	@Pure
	@ScalaOperator("-")
	default RP $minus(Point2D<?, ?> point) {
		return operator_minus(point);
	}

	/** Scale this vector: {@code this / factor}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code scalar / this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$div(double, Vector2D)}.
	 *
	 * @param factor the scaling factor
	 * @return the scaled vector.
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$div(double, Vector2D)
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
	 * @see #add(Vector2D, Vector2D)
	 */
	@Pure
	@ScalaOperator("+")
	default RV $plus(Vector2D<?, ?> v) {
		return operator_plus(v);
	}

	/** Add this vector to a point: {@code this + p}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param pt the point.
	 * @return the result.
	 * @see Point2D#add(Vector2D, Point2D)
	 */
	@Pure
	@ScalaOperator("+")
	default RP $plus(Point2D<?, ?> pt) {
		return operator_plus(pt);
	}

	/** Sum of this vector and the given scalar: {@code this + scalar}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code scalar + this} is supported by
	 * {@link org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$plus(double, Vector2D)}.
	 *
	 * @param scalar the scalar.
	 * @return the result.
	 * @see #add(Vector2D, Vector2D)
	 * @see org.arakhne.afc.math.extensions.scala.Tuple2DExtensions#$plus(double, Vector2D)
	 */
	@Pure
	@ScalaOperator("+")
	default RV $plus(double scalar) {
		return operator_plus(scalar);
	}

	/** Perp product of this vector and the given vector: {@code this ^ v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the other vector.
	 * @return the result.
	 * @see #perp(Vector2D)
	 */
	@Pure
	@ScalaOperator("^")
	default double $up(Vector2D<?, ?> v) {
		return operator_power(v);
	}

	/** Compute the power of this vector: {@code this ^ n}.
	 *
	 * <p>If the power is even, the result is a scalar.
	 * If the power is odd, the result is a vector.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param power the power factor.
	 * @return the power of this vector.
	 * @see #power(int)
	 * @see "http://www.euclideanspace.com/maths/algebra/vectors/vecAlgebra/powers/index.htm"
	 */
	@Pure
	@ScalaOperator("^")
	default PowerResult<RV> $up(int power) {
		return operator_power(power);
	}

	/** Result of the power of a Vector2D.
	 *
	 * @param <T> the type of the vector.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	final class PowerResult<T extends Vector2D<? super T, ?>> {

		private final double scalar;

		private final T vector;

		/** Construct a result for even power.
		 *
		 * @param scalar the scalar result.
		 */
		PowerResult(double scalar) {
			this.scalar = scalar;
			this.vector = null;
		}

		/** Construct a result for the odd power.
		 *
		 * @param vector the vector result.
		 */
		PowerResult(T vector) {
			assert vector != null : AssertMessages.notNullParameter();
			this.scalar = Double.NaN;
			this.vector = vector;
		}

		@Pure
		@Override
		public String toString() {
			if (this.vector != null) {
				return this.vector.toString();
			}
			return Double.toString(this.scalar);
		}

		private boolean isSameScalar(Number number) {
			return number.equals(Double.valueOf(this.scalar));
		}

		private boolean isSameVector(Vector2D<?, ?> vector) {
			if (this.vector == vector) {
				return true;
			}
			if (this.vector != null) {
				return this.vector.equals((Vector2D<?, ?>) vector);
			}
			return false;
		}

		@Override
		@Pure
		public boolean equals(Object obj) {
			if (obj instanceof PowerResult<?>) {
				if (this == obj) {
					return true;
				}
				final PowerResult<?> result = (PowerResult<?>) obj;
				if (result.vector != null) {
					return isSameVector(result.vector);
				}
				return isSameScalar(result.scalar);
			}
			if (obj instanceof Vector2D<?, ?>) {
				return isSameVector((Vector2D<?, ?>) obj);
			}
			if (obj instanceof Number) {
				return isSameScalar((Number) obj);
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1;
			bits = 31 * bits + Double.hashCode(this.scalar);
			bits = 31 * bits + Objects.hashCode(this.vector);
			final int b = (int) bits;
			return b ^ (b >> 31);
		}

		/** Replies the scalar result.
		 *
		 * @return the scalar result.
		 */
		@Pure
		public double getScalar() {
			return this.scalar;
		}

		/** Replies the vector result.
		 *
		 * @return the vector result.
		 */
		@Pure
		public T getVector() {
			return this.vector;
		}

		/** Replies if the result is vectorial.
		 *
		 * @return <code>true</code> if the result is vectorial. <code>false</code>
		 *      if the result if scalar.
		 */
		@Pure
		public boolean isVectorial() {
			return this.vector != null;
		}

	}

}

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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;


/** 3D Vector.
 *
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Vector3D<RV extends Vector3D<? super RV, ? super RP>, RP extends Point3D<? super RP, ? super RV>>
        extends Tuple3D<RV> {

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
	 * @param z is the Z coordinate of the vector.
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @see MathUtil#isEpsilonEqual(double, double, double)
	 * @see MathConstants#UNIT_VECTOR_EPSILON
	 * @see #isUnitVector(double, double, double, double)
	 */
	@Pure
	@Inline(value = "(Vector3D.isUnitVector($1, $2, $3, MathConstants.UNIT_VECTOR_EPSILON))",
	  imported = {Vector3D.class, MathConstants.class})
	static boolean isUnitVector(double x, double y, double z) {
		return isUnitVector(x, y, z, MathConstants.UNIT_VECTOR_EPSILON);
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
	 * @param z is the Z coordinate of the vector.
	 * @param epsilon the precision distance to assumed for equality.
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 13.0
	 * @see MathUtil#isEpsilonEqual(double, double, double)
	 * @see #isUnitVector(double, double, double)
	 */
	@Pure
	@Inline(value = "(MathUtil.isEpsilonEqual($1 * $1 + $2 * $2 + $3 * $3, 1., $4))",
	  imported = {MathUtil.class})
	static boolean isUnitVector(double x, double y, double z, double epsilon) {
		return MathUtil.isEpsilonEqual(x * x + y * y + z * z, 1., epsilon);
	}

	/** Replies if this first is a unit vector.
	 * A unit vector has a length equal to 1.
	 *
	 * <p>This function approximates the test on the length of the vector.
	 * This approximation could be based on {@link MathUtil#isEpsilonEqual(double, double)}.
	 *
	 * @return <code>true</code> if the vector has a length equal to 1.
	 * <code>false</code> otherwise.
	 */
	@Pure
	default boolean isUnitVector() {
	    return isUnitVector(getX(), getY(), getZ());
	}

	/** Compute the determinant of three vectors.
	 *
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param z1
	 *            is the Z coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @param z2
	 *            is the Z coordinate of the second vector
	 * @param x3
	 *            is the X coordinate of the third vector
	 * @param y3
	 *            is the Y coordinate of the third vector
	 * @param z3
	 *            is the Z coordinate of the third vector
	 * @return the determinant
	 * @see #perpProduct(double, double, double, double, double, double)
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static double determinant(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        return x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1);
	}

	/** Compute the determinant of two vectors.
	 *
	 * <p><pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>.
	 *
     * @param x1
     *            is the X coordinate of the first vector
     * @param y1
     *            is the Y coordinate of the first vector
     * @param z1
     *            is the Z coordinate of the first vector
     * @param x2
     *            is the X coordinate of the second vector
     * @param y2
     *            is the Y coordinate of the second vector
     * @param z2
     *            is the Z coordinate of the second vector
	 * @return the determinant
	 * @see #determinant(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static double perpProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
	    /* First method:
	     *
	     * det(A,B) = |A|.|B|.sin(theta)
	     * A x B = |A|.|B|.sin(theta).N, where N is the unit vector
	     * A x B = det(A,B).N
	     * A x B = [ y1*z2 - z1*y2 ] = det(A,B).N
	     *         [ z1*x2 - x1*z2 ]
	     *         [ x1*y2 - y1*x2 ]
	     * det(A,B) = sum(A x B)
	     *
	     * Second method:
	     *
	     * det(A,B) = det( [ x1 x2 1 ]
	     *                 [ y1 y2 1 ]
	     *                 [ z1 z2 1 ] )
	     * det(A,B) = x1*y2*1 + y1*z2*1 + z1*x2*1 - 1*y2*z1 - 1*z2*x1 - 1*x2*y1
	     */
	    return x1 * y2 + y1 * z2 + z1 * x2 - y2 * z1 - z2 * x1 - x2 * y1;
	}

	/**
	 * Replies if two vectors are colinear.
	 *
	 * <p>This function uses the test {@link MathUtil#isEpsilonZero(double)}.
	 *
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param z1
	 *            is the Z coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @param z2
	 *            is the Z coordinate of the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	static boolean isCollinearVectors(double x1, double y1, double z1, double x2, double y2, double z2) {
		// Cross product
		final double cx = y1 * z2 - z1 * y2;
		final double cy = z1 * x2 - x1 * z2;
		final double cz = x1 * y2 - y1 * x2;

		return MathUtil.isEpsilonZero(cx * cx + cy * cy + cz * cz);
	}

	/** Compute the dot product of two vectors.
	 *
	 * @param x1 x coordinate of the first vector.
	 * @param y1 y coordinate of the first vector.
	 * @param z1 y coordinate of the first vector.
	 * @param x2 x coordinate of the second vector.
	 * @param y2 y coordinate of the second vector.
	 * @param z2 z coordinate of the second vector.
	 * @return the dot product.
	 */
	@Pure
	@Inline(value = "($1) * ($4) + ($2) * ($5) +  ($3) * ($6)")
	static double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
		return x1 * x2 + y1 * y2 +  z1 * z2;
	}

	/**
	 * Computes the cross product of the vectors v1 and v2.
	 * This function uses the
	 * {@link #crossProductLeftHand(double, double, double, double, double, double, Vector3D) left-handed cross product}
	 * if the default coordinate system is left-handed. Otherwise, it uses the
	 * {@link #crossProductRightHand(double, double, double, double, double, double, Vector3D) right-handed cross product}.
	 * The default coordinate system is given by
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 *
	 * <img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
	 * <img src="doc-files/left_handed_cross_product.png" alt="[Right-Handed Cross Product]">
	 *
	 * @param x1 x coordinate of the vector v1.
	 * @param y1 y coordinate of the vector v1.
	 * @param z1 z coordinate of the vector v1.
	 * @param x2 x coordinate of the vector v2.
	 * @param y2 y coordinate of the vector v2.
	 * @param z2 z coordinate of the vector v2.
	 * @param result is the result of the cross product.
	 */
	static void crossProduct(
			double x1, double y1, double z1,
			double x2, double y2, double z2, Vector3D<?, ?> result) {
		crossProduct(x1, y1, z1, x2, y2, z2,
				CoordinateSystem3D.getDefaultCoordinateSystem(), result);
	}

	/**
	 * Computes the cross product of the vectors v1 and v2.
	 * This function uses the
	 * {@link #crossProductLeftHand(double, double, double, double, double, double, Vector3D) left-handed cross product}
	 * if the given coordinate system is left-handed. Otherwise, it uses the
	 * {@link #crossProductRightHand(double, double, double, double, double, double, Vector3D) right-handed cross product}.
	 *
	 * <img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
	 * <img src="doc-files/left_handed_cross_product.png" alt="[Right-Handed Cross Product]">
	 *
	 * @param x1 x coordinate of the vector v1.
	 * @param y1 y coordinate of the vector v1.
	 * @param z1 z coordinate of the vector v1.
	 * @param x2 x coordinate of the vector v2.
	 * @param y2 y coordinate of the vector v2.
	 * @param z2 z coordinate of the vector v2.
	 * @param system the coordinate system to consider for computing the cross product.
	 * @param result is the result of the cross product.
	 */
	static void crossProduct(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			CoordinateSystem3D system, Vector3D<?, ?> result) {
		if (system.isLeftHanded()) {
			crossProductLeftHand(
					x1, y1, z1,
					x2, y2, z2,
					result);
		} else {
			crossProductRightHand(
					x1, y1, z1,
					x2, y2, z2,
					result);
		}
	}

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system.
	 *
	 * <img src="doc-files/left_handed_cross_product.png">
	 *
	 * @param x1 x coordinate of the vector v1.
	 * @param y1 y coordinate of the vector v1.
	 * @param z1 z coordinate of the vector v1.
	 * @param x2 x coordinate of the vector v2.
	 * @param y2 y coordinate of the vector v2.
	 * @param z2 z coordinate of the vector v2.
	 * @param result is the result of the cross product.
	 */
	static void crossProductLeftHand(
			double x1, double y1, double z1,
			double x2, double y2, double z2, Vector3D<?, ?> result) {
        final double x = y2 * z1 - z2 * y1;
        final double y = z2 * x1 - x2 * z1;
        final double z = x2 * y1 - y2 * x1;
		result.set(x, y, z);
	}

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a right-hand coordinate system.
	 *
	 * <img src="doc-files/right_handed_cross_product.png">
	 *
	 * @param x1 x coordinate of the vector v1.
	 * @param y1 y coordinate of the vector v1.
	 * @param z1 z coordinate of the vector v1.
	 * @param x2 x coordinate of the vector v2.
	 * @param y2 y coordinate of the vector v2.
	 * @param z2 z coordinate of the vector v2.
	 * @param result is the result of the cross product.
	 */
	static void crossProductRightHand(
			double x1, double y1, double z1,
			double x2, double y2, double z2, Vector3D<?, ?> result) {
        final double x = y1 * z2 - z1 * y2;
        final double y = z1 * x2 - x1 * z2;
        final double z = x1 * y2 - y1 * x2;
		result.set(x, y, z);
	}

	/**
	 * Compute the signed angle between two vectors.
	 *
     * @param x1
     *            is the X coordinate of the first vector
     * @param y1
     *            is the Y coordinate of the first vector
     * @param z1
     *            is the Z coordinate of the first vector
     * @param x2
     *            is the X coordinate of the second vector
     * @param y2
     *            is the Y coordinate of the second vector
     * @param z2
     *            is the Z coordinate of the second vector
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 */
	@Pure
	static double signedAngle(double x1, double y1, double z1, double x2, double y2, double z2) {
		final double lengths = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1) * Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
		if (lengths == 0.) {
            return Double.NaN;
        }

		// First method
		// Angle
		// A . B = |A|.|B|.cos(theta)
		double dot = dotProduct(x1, y1, z1, x2, y2, z2) / lengths;
		if (dot < -1.0) {
            dot = -1.0;
        }
		if (dot > 1.0) {
            dot = 1.0;
        }
		double angle = Math.acos(dot);

		// On which side of A, B is located?
		if ((dot > -1) && (dot < 1)) {
			// det(A,B) = |A|.|B|.sin(theta)
			dot = perpProduct(x1, y1, z1, x2, y2, z2) / lengths;
			if (dot < 0) {
                angle = -angle;
            }
		}

		return angle;
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
     * <p>The value replied by {@link #angle(Vector3D)}
     * is the absolute value of the vlaue replied by this
     * function.
     *
     * @param vector is the vector to reach.
     * @return the rotation angle to turn this vector to reach
     * {@code v}.
     */
    @Pure
    default double signedAngle(Vector3D<?, ?> vector) {
        assert vector != null : AssertMessages.notNullParameter();
        return signedAngle(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ());
    }


	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param vector1 the first tuple
	 * @param vector2 the second tuple
	 */
	default void add(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		set(vector1.getX() + vector2.getX(),
			vector1.getY() + vector2.getY(),
			vector1.getZ() + vector2.getZ());
	}


	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param vector the other tuple
	 */
	default void add(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		set(getX() + vector.getX(),
			getY() + vector.getY(),
			getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param vector1 the tuple to be multipled
	 * @param vector2 the tuple to be added
	 */
	default void scaleAdd(int scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		set(scale * vector1.getX() + vector2.getX(),
			scale * vector1.getY() + vector2.getY(),
			scale * vector1.getZ() + vector2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param vector1 the tuple to be multipled
	 * @param vector2 the tuple to be added
	 */
	default void scaleAdd(double scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		set(scale * vector1.getX() + vector2.getX(),
			scale * vector1.getY() + vector2.getY(),
			scale * vector1.getZ() + vector2.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param scale the scalar value
	 * @param vector the tuple to be added
	 */
	default void scaleAdd(int scale, Vector3D<?, ?> vector) {
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
	default void scaleAdd(double scale, Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		set(scale * getX() + vector.getX(),
			scale * getY() + vector.getY(),
			scale * getZ() + vector.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param vector1 the first tuple
	 * @param vector2 the second tuple
	 */
	default void sub(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		set(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param point1 the first tuple
	 * @param point2 the second tuple
	 */
	default void sub(Point3D<?, ?> point1, Point3D<?, ?> point2) {
		assert point1 != null : AssertMessages.notNullParameter(0);
		assert point2 != null : AssertMessages.notNullParameter(1);
		set(point1.getX() - point2.getX(), point1.getY() - point2.getY(), point1.getZ() - point2.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param vector the other tuple
	 */
	default void sub(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		set(getX() - vector.getX(), getY() - vector.getY(), getZ() - vector.getZ());
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
		final double z = getZ();
		final double dot = dotProduct(x, y, z, x, y, z);
		final double resultForEven = Math.pow(dot, evenPower);
		if (isEven) {
			return new PowerResult<>(resultForEven);
		}
		final RV r = getGeomFactory().newVector(getX() * resultForEven, getY() * resultForEven, getZ() * resultForEven);
		return new PowerResult<>(r);

	}

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param vector the other vector
	 * @return the dot product.
	 */
	@Pure
	default double dot(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return dotProduct(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ());
	}

	/** Compute the determinant of two vectors.
	 *
	 * <pre><code>det(this, V) = |this|.|V|.sin(a)</code></pre>
	 * where <code>this</code> and <code>V</code> are two vectors
	 * and <code>a</code> is the angle between <code>this</code>
	 * and <code>V</code>.
	 *
	 * @param vector the vector.
	 * @return the perp product.
	 */
	@Pure
	default double perp(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return perpProduct(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Computes the cross product of the this vector and vector v1.
	 * The coordinate system's standard depends on the underlying
	 * implementation of the API.
	 * One of {@link #crossLeftHand(Vector3D)} or {@link #crossRightHand(Vector3D)}
	 * will be invoked by this function.
	 *
	 * <p><img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
	 *
	 * <p><img src="doc-files/right_handed_cross_product.png" alt="[Right-Handed Cross Product]">
	 *
	 * @param vector the other vector.
	 * @return the cross product.
	 * @see #crossLeftHand(Vector3D)
	 * @see #crossRightHand(Vector3D)
	 */
	@Pure
	default RV cross(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		final RV result = getGeomFactory().newVector();
		crossProduct(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ(), result);
		return result;
	}

	/**
	 * Computes the cross product of the vectors v1 and v2 and
	 * put the result in this vector.
	 * The coordinate system's standard depends on the underlying
	 * implementation of the API.
	 * One of {@link #crossLeftHand(Vector3D, Vector3D)} or
	 * {@link #crossRightHand(Vector3D, Vector3D)}
	 * will be invoked by this function.
	 *
	 * <p><img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
	 *
	 * <p><img src="doc-files/right_handed_cross_product.png" alt="[Right-Handed Cross Product]">
	 *
	 * @param vector1 the left operand.
	 * @param vector2 the right operand.
	 * @see #crossLeftHand(Vector3D, Vector3D)
	 * @see #crossRightHand(Vector3D, Vector3D)
	 */
	default void cross(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		crossProduct(vector1.getX(), vector1.getY(), vector1.getZ(), vector2.getX(), vector2.getY(), vector2.getZ(), this);
	}

	/**
	 * Computes the cross product of the this vector and vector v1
	 * as if the vectors are inside a left-hand coordinate system.
	 *
	 * <p><img alt="" src="doc-files/left_handed_cross_product.png">
	 *
	 * @param vector the other vector
	 * @return the dot product.
	 */
	@Pure
	default RV crossLeftHand(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		final RV result = getGeomFactory().newVector();
		crossProductLeftHand(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ(), result);
		return result;
	}

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system;
	 * and put the result in this vector.
	 *
	 * <p><img alt="" src="doc-files/left_handed_cross_product.png">
	 *
	 * @param vector1 the left operand.
	 * @param vector2 the right operand.
	 */
	default void crossLeftHand(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
        crossProductLeftHand(vector1.getX(), vector1.getY(), vector1.getZ(), vector2.getX(), vector2.getY(), vector2.getZ(),
                this);
	}

	/**
	 * Computes the cross product of the this vector and vector v1
	 * as if the vectors are inside a left-hand coordinate system.
	 *
	 * <p><img alt="" src="doc-files/right_handed_cross_product.png">
	 *
	 * @param vector the other vector
	 * @return the dot product.
	 */
	@Pure
	default Vector3D<?, ?> crossRightHand(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		final RV result = getGeomFactory().newVector();
		crossProductRightHand(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ(), result);
		return result;
	}

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system;
	 * and put the result in this vector.
	 *
	 * <p><img alt="" src="doc-files/right_handed_cross_product.png">
	 *
	 * @param vector1 the left operand
	 * @param vector2 the right operand
	 */
	default void crossRightHand(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
        crossProductRightHand(vector1.getX(), vector1.getY(), vector1.getZ(), vector2.getX(), vector2.getY(), vector2.getZ(),
                this);
	}

	/**
	 * Returns the length of this vector.
	 * @return the length of this vector
	 */
	@Pure
	default double getLength() {
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns the squared length of this vector.
	 * @return the squared length of this vector
	 */
	@Pure
	default double getLengthSquared() {
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		return x * x + y * y + z * z;
	}

	/**
	 * Sets the value of this vector to the normalization of vector v1.
	 * @param vector the un-normalized vector
	 */
	default void normalize(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		final double x = vector.getX();
		final double y = vector.getY();
		final double z = vector.getZ();
		double sqlength = x * x + y * y + z * z;
        if (sqlength != 0.) {
			sqlength = Math.sqrt(sqlength);
			set(x / sqlength, y / sqlength, z / sqlength);
		} else {
			set(0, 0, 0);
		}
	}

	/**
	 * Normalizes this vector in place.
	 */
	default void normalize() {
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		double sqlength = x * x + y * y + z * z;
        if (sqlength != 0.) {
			sqlength = Math.sqrt(sqlength);
			set(x / sqlength, y / sqlength, z / sqlength);
		} else {
			set(0, 0, 0);
		}
	}


	/**
	 *   Returns the angle in radians between this vector and the vector
	 *   parameter; the return value is constrained to the range [0, PI].
	 *   @param v1    the other vector
	 *   @return   the angle in radians in the range [0, PI]
	 */
	@Pure
	default double angle(Vector3D<?, ?> v1) {
		double vDot = this.dot(v1) / (this.getLength() * v1.getLength());
        if (vDot < -1.) {
            vDot = -1.;
        }
        if (vDot > 1.) {
            vDot =  1.;
        }
		return Math.acos(vDot);
	}

	/** Turn this vector about the given rotation angle.
	 *
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation angle in radians.
	 */
	default void turnVector(Vector3D<?, ?> axis, double angle) {
		final Transform3D mat = new Transform3D();
		mat.setRotation(getGeomFactory().newQuaternion(axis, angle));
		mat.transform(this);
	}

	/** Replies if this vector is colinear to the given vector.
	 *
	 * @param vector the vector
	 * @return <code>true</code> if the vectors are colinear..
	 */
	@Pure
	default boolean isColinear(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return isCollinearVectors(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ());
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
			set(getX() * f, getY() * f, getZ() * f);
		} else {
			set(newLength, 0, 0);
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
		return getGeomFactory().newVector(getX() / length, getY() / length, getZ() / length);
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
			final double z = (length * getZ()) / len;
			return getGeomFactory().newVector(x, y, z);
		}
		return getGeomFactory().newVector();
	}

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	UnmodifiableVector3D<RV, RP> toUnmodifiable();

	/** Replies the geometry factory associated to this point.
	 *
	 * @return the factory.
	 */
	@Pure
	GeomFactory3D<RV, RP> getGeomFactory();

	/** Add a vector to this vector: {@code this += v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #add(Vector3D)
	 */
	default void operator_add(Vector3D<?, ?> v) {
		add(v);
	}

	/** Substract a vector to this vector: {@code this -= v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #sub(Vector3D)
	 */
	default void operator_remove(Vector3D<?, ?> v) {
		sub(v);
	}

	/** Dot product: {@code this * v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #dot(Vector3D)
	 */
	@Pure
	default double operator_multiply(Vector3D<?, ?> v) {
		return dot(v);
	}

    /** Scale this vector: {@code this * f}
     *
     * <p>This function is an implementation of the "-" operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param scale the scaling factor.
     * @return the scaled vector.
     * @see #scale(double)
     */
    @Pure
    default RV operator_multiply(double scale) {
        return getGeomFactory().newVector(getX() * scale, getY() * scale, getZ() * scale);
    }

	/** Replies if this vector and the given vector are equal: {@code this == v}
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

	/** Replies if this vector and the given vector are different: {@code this != v}
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

	/** Replies if the absolute angle between this and v: {@code this .. b}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the signed angle.
	 * @see #angle(Vector3D)
	 */
	@Pure
	default double operator_upTo(Vector3D<?, ?> v) {
		return angle(v);
	}

	/** Replies the signed angle from this to v: {@code this >.. v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the signed angle.
	 * @see #signedAngle(Vector3D)
	 */
	@Pure
	default double operator_greaterThanDoubleDot(Vector3D<?, ?> v) {
		return signedAngle(v);
	}

	/** Replies the signed angle from v to this: {@code this ..< v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the signed angle.
	 * @see #signedAngle(Vector3D)
	 */
	@Pure
	default double operator_doubleDotLessThan(Vector3D<?, ?> v) {
		return -signedAngle(v);
	}

    /** Subtract a vector to this vector: {@code this - v}
     *
     * <p>This function is an implementation of the "-" operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param v the vector
     * @return the result.
     * @see #sub(Vector3D)
     */
    @Pure
    default RV operator_minus(Vector3D<?, ?> v) {
        assert v != null : AssertMessages.notNullParameter();
        return getGeomFactory().newVector(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
    }

	/** Negation of this vector: {@code -this}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @return the result.
	 * @see #negate(Tuple3D)
	 */
	@Pure
	default RV operator_minus() {
		return getGeomFactory().newVector(-getX(), -getY(), -getZ());
	}

	/** Scale this vector: {@code this / f}.
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param scale the scaling factor
	 * @return the scaled vector.
	 */
	@Pure
	default RV operator_divide(double scale) {
		return getGeomFactory().newVector(getX() / scale, getY() / scale, getZ() / scale);
	}

	/** If this vector is epsilon equal to zero then reply v else reply this: {@code this ?: v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return the vector.
	 */
	@Pure
	default Vector3D<? extends RV, ? extends RP> operator_elvis(Vector3D<? extends RV, ? extends RP> v) {
		if (MathUtil.isEpsilonZero(getX()) && MathUtil.isEpsilonZero(getY()) && MathUtil.isEpsilonZero(getZ())) {
			return v;
		}
		return this;
	}


	/** Sum of this vector and the given vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the result.
	 * @see #add(Vector3D, Vector3D)
	 */
	@Pure
	default RV operator_plus(Vector3D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return getGeomFactory().newVector(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
	}

	/** Add this vector to a point: {@code this + p}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param point the point.
	 * @return the result.
	 * @see Point3D#add(Vector3D, Point3D)
	 */
	@Pure
	default RP operator_plus(Point3D<?, ?> point) {
	    assert point != null : AssertMessages.notNullParameter();
	    return getGeomFactory().newPoint(getX() + point.getX(), getY() + point.getY(), getZ() + point.getZ());
	}

	/** Perp product of this vector and the given vector: {@code this ** v}.
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the other vector.
	 * @return the result.
	 * @see #perp(Vector3D)
	 */
	@Pure
	default double operator_power(Vector3D<?, ?> v) {
		return perp(v);
	}

	/** Compute the power of this vector: {@code this ** n}.
	 *
	 * <p>If the power is even, the result is a scalar.
	 * If the power is odd, the result is a vector.
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param power the power factor.
	 * @return the power of this vector.
	 * @see #power(int)
	 * @see "http://www.euclideanspace.com/maths/algebra/vectors/vecAlgebra/powers/index.htm"
	 */
	@Pure
	default PowerResult<RV> operator_power(int power) {
		return power(power);
	}

	/** Result of the power of a Vector3D.
	 *
	 * @param <T> the type of the vector.
	 * @author $Author: tpiotrow$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	final class PowerResult<T extends Vector3D<? super T, ?>> {

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

		private boolean isSameVector(Vector3D<?, ?> vector) {
			if (this.vector == vector) {
				return true;
			}
			if (this.vector != null) {
                return this.vector.equals((Vector3D<?, ?>) vector);
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
			if (obj instanceof Vector3D<?, ?>) {
				return isSameVector((Vector3D<?, ?>) obj);
			}
			if (obj instanceof Number) {
				return isSameScalar((Number) obj);
			}
			return false;
		}

		@Override
		@Pure
		public int hashCode() {
			long bits = 1;
			bits = 31 * bits + Double.doubleToLongBits(this.scalar);
			bits = 31 * bits + ((this.vector == null) ? 0 : this.vector.hashCode());
			final int b = (int) bits;
			return b ^ (b >> 32);
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
		 *     if the result if scalar.
		 */
		@Pure
		public boolean isVectorial() {
			return this.vector != null;
		}

	}

}

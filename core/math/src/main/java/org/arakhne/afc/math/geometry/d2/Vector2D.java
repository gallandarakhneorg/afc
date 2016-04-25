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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Vector.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Vector2D extends Tuple2D<Vector2D> {

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
	static boolean isUnitVector(double x, double y, double epsilon) {
		return MathUtil.isEpsilonEqual(x * x + y * y, 1., epsilon);
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
	static boolean isOrthogonal(double x1, double y1, double x2, double y2, double epsilon) {
		return MathUtil.isEpsilonZero(dotProduct(x1, y1, x2, y2), epsilon);
	}

	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
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
	static boolean isCollinearVectors(double x1, double y1, double x2, double y2) {
		// Test if three points are colinears
		// iff det( [ x1 x2 ]
		// [ y1 y2 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computation consumption.
		return MathUtil.isEpsilonZero(x1 * y2 - x2 * y1);
	}
	
	/** Compute the the perpendicular product of
	 * the two vectors (aka. the determinant of two vectors).
	 * <p>
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>. 
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the determinant
	 */
	@Pure
	static double perpProduct(double x1, double y1, double x2, double y2) {
		return x1 * y2 - x2 * y1;
	}
	
	/** Compute the dot product of two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the dot product.
	 */
	@Pure
	static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1 * x2 + y1 * y2;
	}

	/**
	 * Compute the signed angle between two vectors.
	 * 
	 * @param x1 the first coordinate of the first vector.
	 * @param y1 the second coordinate of the first vector.
	 * @param x2 the first coordinate of the second vector.
	 * @param y2 the second coordinate of the second vector.
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 */
	@Pure
	static double signedAngle(double x1, double y1, double x2, double y2) {
		double length1 = Math.sqrt(x1 * x1 + y1 * y1);
		double length2 = Math.sqrt(x2 * x2 + y2 * y2);

		if ((length1 == 0.) || (length2 == 0.))
			return Double.NaN;

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

		/*
		 * // First method // Angle // A . B = |A|.|B|.cos(theta) = cos(theta) double dot = x1 * x2 + y1 * y2; double angle = Math.acos(dot);
		 * 
		 * // On which side of A, B is located? if ((dot > -1) && (dot < 1)) { dot = MathUtil.determinant(x2, y2, x1, y1); if (dot < 0) angle = -angle; }
		 */

		// Second method
		// A . B = |A|.|B|.cos(theta) = cos(theta)
		double cos = cx1 * cx2 + cy1 * cy2;
		// A x B = |A|.|B|.sin(theta).N = sin(theta) (where N is the unit vector perpendicular to plane AB)
		double sin = cx1 * cy2 - cy1 * cx2;

		double angle = Math.atan2(sin, cos);

		return angle;
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x2-x1;y2-y1).
	 *
	 * @param x1 is the coordinate of the vector origin point.
	 * @param y1 is the coordinate of the vector origin point.
	 * @param x2 is the coordinate of the vector target point.
	 * @param y2 is the coordinate of the vector target point.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 */
	@Pure
	static double angleOfVector(double x1, double y1, double x2, double y2) {
		return signedAngle(
				1, 0, 
				x2-x1, y2-y1);
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x;y).
	 *
	 * @param x is the coordinate of the vector.
	 * @param y is the coordinate of the vector.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 */
	@Pure
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
	default void add(Vector2D vector1, Vector2D vector2) {
		assert (vector1 != null) : "First vector must be not be null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not be null"; //$NON-NLS-1$
		set(vector1.getX() + vector2.getX(),
			vector1.getY() + vector2.getY());
	}

	/**
	 * Sets the value of this tuple to the sum of itself and the given vector.
	 *
	 * @param vector the other tuple
	 */
	default void add(Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
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
	default void scaleAdd(int scale, Vector2D t1, Vector2D t2) {
		assert (t1 != null) : "First vector must be not be null"; //$NON-NLS-1$
		assert (t2 != null) : "Second vector must be not be null"; //$NON-NLS-1$
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
	default void scaleAdd(double scale, Vector2D vector1, Vector2D vector2) {
		assert (vector1 != null) : "First vector must be not be null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not be null"; //$NON-NLS-1$
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
	default void scaleAdd(int scale, Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
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
	default void scaleAdd(double scale, Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
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
	default void sub(Vector2D vector1, Vector2D vector2) {
		assert (vector1 != null) : "First vector must be not be null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not be null"; //$NON-NLS-1$
		set(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples point1 and point2 (this = point1 - point2).
	 *
	 * @param point1 the first tuple
	 * @param point2 the second tuple
	 */
	default void sub(Point2D point1, Point2D point2) {
		assert (point1 != null) : "First point must be not be null"; //$NON-NLS-1$
		assert (point2 != null) : "Second point must be not be null"; //$NON-NLS-1$
		set(point1.getX() - point2.getX(), point1.getY() - point2.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and vector (this = this - vector).
	 *
	 * @param vector the other tuple
	 */
	default void sub(Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
		set(getX() - vector.getX(), getY() - vector.getY());
	}

	/**
	 * Computes the dot product of the this vector and the given vector.
	 *
	 * @param vector the other vector
	 * @return the dot product.
	 */
	@Pure
	default double dot(Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
		return dotProduct(getX(), getY(), vector.getX(), vector.getY());
	}
	
	/** Compute the the perpendicular product of
	 * the two vectors (aka. the determinant of two vectors).
	 * <p>
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>. 
	 * 
	 * @param vector the vertor.
	 * @return the determinant
	 */
	@Pure
	default double perp(Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
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
	Vector2D toOrthogonalVector();

	/**  
	 * Returns the length of this vector.
	 *
	 * @return the length of this vector
	 */  
	@Pure
	default double getLength() {
		double x = getX();
		double y = getY();
		return Math.sqrt(x * x + y * y);
	}

	/**  
	 * Returns the squared length of this vector.
	 *
	 * @return the squared length of this vector
	 */ 
	@Pure 
	default double getLengthSquared() {
		double x = getX();
		double y = getY();
		return x * x + y * y;
	}

	/**
	 * Sets the value of this vector to the normalization of vector vector.
	 *
	 * @param vector the un-normalized vector
	 */  
	default void normalize(Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
		double x = vector.getX();
		double y = vector.getY();
		double sqlength = x * x + y * y;
		if(sqlength != 0.) {
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
		double x = getX();
		double y = getY();
		double sqlength = x * x + y * y;
		if(sqlength != 0.) {
			sqlength = Math.sqrt(sqlength);
			set(x / sqlength, y / sqlength);
		} else {
			set(0, 0);
		}
	}


	/**
	 * Returns the angle in radians between this vector and the vector
	 * parameter; the return value is constrained to the range [0,PI].
	 *
	 * @param vector the other vector
	 * @return the angle in radians in the range [0,PI]
	 */
	@Pure
	default double angle(Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
		double vDot = dotProduct(getX(), getY(), vector.getX(), vector.getY()) / ( getLength()*vector.getLength() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return (Math.acos( vDot ));
	}

	/** Compute a signed angle between this vector and the given vector.
	 * <p>
	 * The signed angle between this vector and the given {@code vector}
	 * is the rotation angle to apply to this vector
	 * to be colinear to the given {@code vector} and pointing the
	 * same demi-plane. It means that the angle replied
	 * by this function is be negative if the rotation
	 * to apply is clockwise, and positive if
	 * the rotation is counterclockwise.
	 * <p>
	 * The value replied by {@link #angle(Vector2D)}
	 * is the absolute value of the vlaue replied by this
	 * function. 
	 *
	 * @param vector is the vector to reach.
	 * @return the rotation angle to turn this vector to reach
	 * {@code v}.
	 */
	@Pure
	default double signedAngle(Vector2D vector) {
		assert (vector != null) : "Vector must be not be null"; //$NON-NLS-1$
		return signedAngle(getX(), getY(), vector.getX(), vector.getY());
	}

	/** Turn this vector about the given rotation angle.
	 *
	 * <p>The rotation is done according to the trigonometric coordinate.
	 * A positive rotation angle corresponds to a left or right rotation
	 * according to the current {@link CoordinateSystem2D}.
	 *
	 * @param angle is the rotation angle in radians.
	 * @deprecated {@link #turn(double)}
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
	default void turn(double angle, Vector2D vectorToTurn) {
		assert (vectorToTurn != null) : "Vector must be not null"; //$NON-NLS-1$
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x =  cos * vectorToTurn.getX() - sin * vectorToTurn.getY(); 
		double y =  sin * vectorToTurn.getX() + cos * vectorToTurn.getY();
		set(x,y);
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
	default void turnLeft(double angle, Vector2D vectorToTurn) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x, y;
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			x =  cos * vectorToTurn.getX() - sin * vectorToTurn.getY(); 
			y =  sin * vectorToTurn.getX() + cos * vectorToTurn.getY();
		} else {
			x =  cos * vectorToTurn.getX() + sin * vectorToTurn.getY(); 
			y = -sin * vectorToTurn.getX() + cos * vectorToTurn.getY();
		}
		set(x,y);
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
	@Inline(value = "turnLeft(-($1))")
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
	default void turnRight(double angle, Vector2D vectorToTurn) {
		turnLeft(-angle, vectorToTurn);
	}

	/** Replies the orientation angle on a trigonometric circle
	 * that is corresponding to the given direction of this vector.
	 * 
	 * @return the angle on a trigonometric circle that is corresponding
	 * to the given orientation vector.
	 */
	@Pure
	default double getOrientationAngle() {
		double angle = Math.acos(getX());
		if (getY() < 0f) {
			return MathConstants.TWO_PI - angle;
		}
		return angle;
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
	
	/** Replies if this vector is orthogonal to the given vector.
	 *
	 * @param vector the vector to compare to this vector.
	 * @return <code>true</code> if the vectors are orthogonal.
	 * <code>false</code> otherwise.
	 */
	@Pure
	default boolean isOrthogonal(Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return isOrthogonal(getX(), getY(), vector.getX(), vector.getY());
	}

	/** Change the length of the vector.
	 * The direction of the vector is unchanged.
	 *
	 * @param newLength - the new length.
	 */
	default void setLength(double newLength) {
		assert (newLength >= 0) : "Length must be positive or zero"; //$NON-NLS-1$
		double l = getLength();
		if (l != 0.) {
			double f = newLength / l;
			set(getX() * f, getY() * f);
		} else {
			set(newLength, 0);
		}
	}

	/** Replies the unit vector of this vector.
	 *
	 * @return the unit vector of this vector.
	 */
	Vector2D toUnitVector();
	
	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	Vector2D toUnmodifiable();
	
}
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
	 * Replies if the vector is unit.
	 * 
	 * @param x is the X coordinate of the vector.
	 * @param y is the Y coordinate of the vector.
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 13.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	static boolean isUnitVector(double x, double y) {
		return MathUtil.isEpsilonEqual(x * x + y *y, 1);
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
		return x1*y2 - x2*y1;
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
		return x1*x2 + y1*y2;
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
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	default void add(Vector2D t1, Vector2D t2) {
		set(t1.getX() + t2.getX(),
			t1.getY() + t2.getY());
	}

	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param t1 the other tuple
	 */
	default void add(Vector2D t1) {
		set(t1.getX() + getX(),
			t1.getY() + getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	default void scaleAdd(int s, Vector2D t1, Vector2D t2) {
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
	default void scaleAdd(double s, Vector2D t1, Vector2D t2) {
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
	default void sub(Vector2D t1, Vector2D t2) {
		set(t1.getX() - t2.getX(), t1.getY() - t2.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	default void sub(Point2D t1, Point2D t2) {
		set(t1.getX() - t2.getX(), t1.getY() - t2.getY());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param t1 the other tuple
	 */
	default void sub(Vector2D t1) {
		set(getX() - t1.getX(), getY() - t1.getY());
	}

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	@Pure
	default double dot(Vector2D v1) {
		return dotProduct(getX(), getY(), v1.getX(), v1.getY());
	}
	
	/** Compute the the perpendicular product of
	 * the two vectors (aka. the determinant of two vectors).
	 * <p>
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>. 
	 * 
	 * @param v1
	 * @return the determinant
	 */
	@Pure
	default double perp(Vector2D v1) {
		return perpProduct(getX(), getY(), v1.getX(), v1.getY());
	}

	/** Change the coordinates of this vector to make it a perpendicular
	 * vector to the original coordinates.
	 */
	default void perpendicularize() {
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			set(getY(), -getX());
		} else {
			set(-getY(), getX());
		}
	}

	/**  
	 * Returns the length of this vector.
	 * @return the length of this vector
	 */  
	@Pure
	default double length() {
		double x = getX();
		double y = getY();
		return Math.sqrt(x * x + y * y);
	}

	/**  
	 * Returns the squared length of this vector.
	 * @return the squared length of this vector
	 */ 
	@Pure 
	default double lengthSquared() {
		double x = getX();
		double y = getY();
		return x * x + y * y;
	}

	/**
	 * Sets the value of this vector to the normalization of vector v1.
	 * @param v1 the un-normalized vector
	 */  
	default void normalize(Vector2D v1) {
		double x = v1.getX();
		double y = v1.getY();
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
	 */  
	default void normalize() {
		double x = getX();
		double y = getY();
		double sqlength = x * x + y * y;
		if(sqlength != 0.) {
			sqlength = Math.sqrt(sqlength);
			set(x / sqlength, y / sqlength);
		}
	}


	/**
	 *   Returns the angle in radians between this vector and the vector
	 *   parameter; the return value is constrained to the range [0,PI].
	 *   @param v1    the other vector
	 *   @return   the angle in radians in the range [0,PI]
	 */
	@Pure
	default double angle(Vector2D v1) {
		double vDot = dotProduct(getX(), getY(), v1.getX(), v1.getY()) / ( length()*v1.length() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return (Math.acos( vDot ));
	}

	/** Compute a signed angle between this vector and the given vector.
	 * <p>
	 * The signed angle between this vector and {@code v}
	 * is the rotation angle to apply to this vector
	 * to be colinear to {@code v} and pointing the
	 * same demi-plane. It means that the angle replied
	 * by this function is be negative if the rotation
	 * to apply is clockwise, and positive if
	 * the rotation is counterclockwise.
	 * <p>
	 * The value replied by {@link #angle(Vector2D)}
	 * is the absolute value of the vlaue replied by this
	 * function. 
	 * 
	 * @param v is the vector to reach.
	 * @return the rotation angle to turn this vector to reach
	 * {@code v}.
	 */
	@Pure
	default double signedAngle(Vector2D v) {
		return signedAngle(getX(), getY(), v.getX(), v.getY());
	}

	/** Turn this vector about the given rotation angle.
	 * 
	 * @param angle is the rotation angle in radians.
	 */
	default void turnVector(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x =  cos * getX() + sin * getY(); 
		double y = -sin * getX() + cos * getY();
		set(x,y);
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
	
	/** Replies if this first is a unit vector.
	 * A unit vector has a length equal to 1.
	 *
	 * @return <code>true</code> if the vector has a length equal to 1.
	 * <code>false</code> otherwise.
	 */
	@Pure
	default boolean isUnitVector() {
		return isUnitVector(getX(), getY());
	}
	
	/** Change the length of the vector.
	 * The direction of the vector is unchanged.
	 *
	 * @param newLength - the new length.
	 */
	default void setLength(double newLength) {
		double nl = Math.max(0., newLength);
		double l = length();
		if (l != 0.) {
			double f = nl / l;
			set(getX() * f, getY() * f);
		} else {
			set(newLength, 0);
		}
	}

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	Vector2D toUnmodifiable();

	/** Unmodifiable 2D Vector.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public interface UnmodifiableVector2D extends UnmodifiableTuple2D<Vector2D>, Vector2D {

		@Override
		default void add(Vector2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector2D t1, Vector2D t2) {
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
		default void sub(Vector2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Point2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void perpendicularize() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void normalize(Vector2D v1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void normalize() {
			throw new UnsupportedOperationException();
		}


		@Override
		default void turnVector(double angle) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		default void setLength(double newLength) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		default Vector2D toUnmodifiable() {
			return this;
		}

	}
	
}
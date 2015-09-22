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

import org.arakhne.afc.math.matrix.Matrix2f;

/** 2D Vector.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Vector2D extends Tuple2D<Vector2D> {

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	public void add(Vector2D t1, Vector2D t2);


	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param t1 the other tuple
	 */
	public void add(Vector2D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	public void scaleAdd(int s, Vector2D t1, Vector2D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	public void scaleAdd(double s, Vector2D t1, Vector2D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param s the scalar value
	 * @param t1 the tuple to be added
	 */
	public void scaleAdd(int s, Vector2D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param s the scalar value
	 * @param t1 the tuple to be added
	 */
	public void scaleAdd(double s, Vector2D t1);

	
	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	public void sub(Vector2D t1, Vector2D t2);

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	public void sub(Point2D t1, Point2D t2);

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param t1 the other tuple
	 */
	public void sub(Vector2D t1);

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	public double dot(Vector2D v1);
	
	/** Compute the the perpendicular product of
	 * the two vectors (aka. the determinant of two vectors).
	 * <p>
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>. 
	 * 
	 * @param x2
	 * @return the determinant
	 */
	public double perp(Vector2D x2);

	/** Change the coordinates of this vector to make it a perpendicular
	 * vector to the original coordinates.
	 */
	public void perpendicularize();

	/**  
	 * Returns the length of this vector.
	 * @return the length of this vector
	 */  
	public double length();

	/**  
	 * Returns the squared length of this vector.
	 * @return the squared length of this vector
	 */  
	public double lengthSquared();

	/**
	 * Sets the value of this vector to the normalization of vector v1.
	 * @param v1 the un-normalized vector
	 */  
	public void normalize(Vector2D v1);

	/**
	 * Normalizes this vector in place.
	 */  
	public void normalize();


	/**
	 *   Returns the angle in radians between this vector and the vector
	 *   parameter; the return value is constrained to the range [0,PI].
	 *   @param v1    the other vector
	 *   @return   the angle in radians in the range [0,PI]
	 */
	public double angle(Vector2D v1);

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
	public double signedAngle(Vector2D v);

	/** Turn this vector about the given rotation angle.
	 * 
	 * @param angle is the rotation angle in radians.
	 */
	public void turnVector(double angle);
	
	/** Replies the orientation angle on a trigonometric circle
	 * that is corresponding to the given direction of this vector.
	 * 
	 * @return the angle on a trigonometric circle that is corresponding
	 * to the given orientation vector.
	 */
	public double getOrientationAngle();
	
	/** Replies if this first is a unit vector.
	 * A unit vector has a length equal to 1.
	 *
	 * @return <code>true</code> if the vector has a length equal to 1.
	 * <code>false</code> otherwise.
	 */
	public boolean isUnitVector();
	
	/** Change the length of the vector.
	 * The direction of the vector is unchanged.
	 *
	 * @param newLength - the new length.
	 */
	public void setLength(double newLength);
	
	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	public Vector2D toUnmodifiable();

}
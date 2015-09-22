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
package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.math.matrix.Matrix3f;


/** 3D Vector.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Vector3D extends Tuple3D<Vector3D> {

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	public void add(Vector3D t1, Vector3D t2);


	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param t1 the other tuple
	 */
	public void add(Vector3D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	public void scaleAdd(int s, Vector3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param s the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	public void scaleAdd(double s, Vector3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param s the scalar value
	 * @param t1 the tuple to be added
	 */
	public void scaleAdd(int s, Vector3D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param s the scalar value
	 * @param t1 the tuple to be added
	 */
	public void scaleAdd(double s, Vector3D t1);

	
	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	public void sub(Vector3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	public void sub(Point3D t1, Point3D t2);

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param t1 the other tuple
	 */
	public void sub(Vector3D t1);

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	public double dot(Vector3D v1);

	/** Compute the determinant of two vectors.
	 * <p>
	 * <pre><code>det(this,V) = |this|.|V|.sin(a)</code></pre>
	 * where <code>this</code> and <code>V</code> are two vectors
	 * and <code>a</code> is the angle between <code>this</code>
	 * and <code>V</code>. 
	 * 
	 * @param v
	 * @return the perp product.
	 */
	public double perp(Vector3D v);

	/**
	 * Computes the cross product of the this vector and vector v1.
	 * The coordinate system's standard depends on the underlying
	 * implementation of the API.
	 * One of {@link #crossLeftHand(Vector3D)} or {@link #crossRightHand(Vector3D)}
	 * will be invoked by this function.
	 * 
	 * <img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
	 * <img src="doc-files/right_handed_cross_product.png" alt="[Right-Handed Cross Product]">
	 * 
	 * @param v1 the other vector
	 * @return the dot product.
	 * @see #crossLeftHand(Vector3D)
	 * @see #crossRightHand(Vector3D)
	 */
	public Vector3D cross(Vector3D v1);

	/**
	 * Computes the cross product of the vectors v1 and v2 and
	 * put the result in this vector.
	 * The coordinate system's standard depends on the underlying
	 * implementation of the API.
	 * One of {@link #crossLeftHand(Vector3D, Vector3D)} or
	 * {@link #crossRightHand(Vector3D, Vector3D)}
	 * will be invoked by this function.
	 * 
	 * <img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
	 * <img src="doc-files/right_handed_cross_product.png" alt="[Right-Handed Cross Product]">
	 * 
	 * @param v1
	 * @param v2
	 * @see #crossLeftHand(Vector3D, Vector3D)
	 * @see #crossRightHand(Vector3D, Vector3D)
	 */
	public void cross(Vector3D v1, Vector3D v2);

	/**
	 * Computes the cross product of the this vector and vector v1
	 * as if the vectors are inside a left-hand coordinate system.
	 * 
	 * <img src="doc-files/left_handed_cross_product.png">
	 *
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	public Vector3D crossLeftHand(Vector3D v1);

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system;
	 * and put the result in this vector.
	 * 
	 * <img src="doc-files/left_handed_cross_product.png">
	 *
	 * @param v1
	 * @param v2
	 */
	public void crossLeftHand(Vector3D v1, Vector3D v2);

	/**
	 * Computes the cross product of the this vector and vector v1
	 * as if the vectors are inside a left-hand coordinate system.
	 * 
	 * <img src="doc-files/right_handed_cross_product.png">
	 *
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	public Vector3D crossRightHand(Vector3D v1);

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system;
	 * and put the result in this vector.
	 * 
	 * <img src="doc-files/right_handed_cross_product.png">
	 *
	 * @param v1
	 * @param v2
	 */
	public void crossRightHand(Vector3D v1, Vector3D v2);

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
	public void normalize(Vector3D v1);

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
	public double angle(Vector3D v1);

	/** Turn this vector about the given rotation angle.
	 * 
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation angle in radians.
	 */
	public void turnVector(Vector3D axis, double angle);
	
	/** Replies if this first is a unit vector.
	 * A unit vector has a length equal to 1.
	 *
	 * This function approximates the test on the length of the vector.
	 * This approximation could be based on {@link MathUtil#isEpsilonEqual(double, double)}. 
	 *
	 * @return <code>true</code> if the vector has a length equal to 1.
	 * <code>false</code> otherwise.
	 */
	public boolean isUnitVector();

	/** Replies if this vector is colinear to the given vector.
	 *
	 * @param v
	 * @return <code>true</code> if the vectors are colinear..
	 */
	public boolean isColinear(Vector3D v);

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
	public Vector3D toUnmodifiable();

}
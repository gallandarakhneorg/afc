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

import org.arakhne.afc.math.MathUtil;


/** 3D Vector.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Vector3D extends Tuple3D<Vector3D> {

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
	@Inline(value = "($1 * $4 + $2 * $5 +  $3 * $6)")
	static double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
		return x1 * x2 + y1 * y2 +  z1 * z2;
	}

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	void add(Vector3D t1, Vector3D t2);


	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param t1 the other tuple
	 */
	void add(Vector3D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	void scaleAdd(int scale, Vector3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	void scaleAdd(double scale, Vector3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param scale the scalar value
	 * @param t1 the tuple to be added
	 */
	void scaleAdd(int scale, Vector3D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param scale the scalar value
	 * @param t1 the tuple to be added
	 */
	void scaleAdd(double scale, Vector3D t1);


	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	void sub(Vector3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	void sub(Point3D t1, Point3D t2);

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param t1 the other tuple
	 */
	void sub(Vector3D t1);

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	@Pure
	double dot(Vector3D v1);

	/** Compute the determinant of two vectors.
	 *
	 * <p><pre><code>det(this, V) = |this|.|V|.sin(a)</code></pre>
	 * where <code>this</code> and <code>V</code> are two vectors
	 * and <code>a</code> is the angle between <code>this</code>
	 * and <code>V</code>.
	 *
	 * @param v the right operand.
	 * @return the perp product.
	 */
	@Pure
	double perp(Vector3D v);

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
	 * @return the cross product.
	 * @see #crossLeftHand(Vector3D)
	 * @see #crossRightHand(Vector3D)
	 */
	@Pure
	Vector3D cross(Vector3D v1);

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
	 * @param v1 the left operand.
	 * @param v2 the right operand.
	 * @see #crossLeftHand(Vector3D, Vector3D)
	 * @see #crossRightHand(Vector3D, Vector3D)
	 */
	void cross(Vector3D v1, Vector3D v2);

	/**
	 * Computes the cross product of the this vector and vector v1
	 * as if the vectors are inside a left-hand coordinate system.
	 *
	 * <img src="doc-files/left_handed_cross_product.png">
	 *
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	@Pure
	Vector3D crossLeftHand(Vector3D v1);

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system;
	 * and put the result in this vector.
	 *
	 * <img src="doc-files/left_handed_cross_product.png">
	 *
	 * @param v1 the left operand.
	 * @param v2 the right operand.
	 */
	void crossLeftHand(Vector3D v1, Vector3D v2);

	/**
	 * Computes the cross product of the this vector and vector v1
	 * as if the vectors are inside a left-hand coordinate system.
	 *
	 * <img src="doc-files/right_handed_cross_product.png">
	 *
	 * @param v1 the other vector
	 * @return the dot product.
	 */
	@Pure
	Vector3D crossRightHand(Vector3D v1);

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system;
	 * and put the result in this vector.
	 *
	 * <img src="doc-files/right_handed_cross_product.png">
	 *
	 * @param v1 the left operand.
	 * @param v2 the right operand.
	 */
	void crossRightHand(Vector3D v1, Vector3D v2);

	/**
	 * Returns the length of this vector.
	 * @return the length of this vector
	 */
	@Pure
	double getLength();

	/**
	 * Returns the squared length of this vector.
	 * @return the squared length of this vector
	 */
	@Pure
	double getLengthSquared();

	/**
	 * Sets the value of this vector to the normalization of vector v1.
	 * @param v1 the un-normalized vector
	 */
	void normalize(Vector3D v1);

	/**
	 * Normalizes this vector in place.
	 */
	void normalize();


	/**
	 *   Returns the angle in radians between this vector and the vector
	 *   parameter; the return value is constrained to the range [0, PI].
	 *   @param v1    the other vector
	 *   @return   the angle in radians in the range [0, PI]
	 */
	@Pure
	double angle(Vector3D v1);

	/** Turn this vector about the given rotation angle.
	 *
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation angle in radians.
	 */
	void turnVector(Vector3D axis, double angle);

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
	boolean isUnitVector();

	/** Replies if this vector is colinear to the given vector.
	 *
	 * @param v the vector to compare.
	 * @return <code>true</code> if the vectors are colinear..
	 */
	@Pure
	boolean isColinear(Vector3D v);

	/** Change the length of the vector.
	 * The direction of the vector is unchanged.
	 *
	 * @param newLength - the new length.
	 */
	void setLength(double newLength);

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	Vector3D toUnmodifiable();

}

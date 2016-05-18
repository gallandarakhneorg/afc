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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;


/** 3D Vector.
 * 
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Vector3D<RP extends Point3D<? super RP, ? super RV>, RV extends Vector3D<? super RP, ? super RV>> extends Tuple3D<RV> {

	/** Compute the determinant of three vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param x3
	 * @param y3
	 * @param z3
	 * @return the determinant
	 * @see #perpProduct(double, double, double, double, double, double)
	 */
	@Pure
	static double determinant(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3) {
		return
				  x1 * (y2 * z3 - y3 * z2)
				+ x2 * (y3 * z1 - y1 * z3)
				+ x3 * (y1 * z2 - y2 * z1);
	}

	/** Compute the determinant of two vectors.
	 * <p>
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>. 
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
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
		return x1*y2 + y1*z2 + z1*x2 - y2*z1 - z2*x1 - x2*y1;
	}
	
	
	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
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
		double cx = y1 * z2 - z1 * y2;
		double cy = z1 * x2 - x1 * z2;
		double cz = x1 * y2 - y1 * x2;

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
		}
		else {
			crossProductRightHand(
					x1, y1, z1,
					x2, y2, z2,
					result);
		}
	}

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a left-hand coordinate system;
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
		double x = y2*z1 - z2*y1;
		double y = z2*x1 - x2*z1;
		double z = x2*y1 - y2*x1;
		result.set(x, y, z);
	}

	/**
	 * Computes the cross product of the vectors v1 and v2
	 * as if the vectors are inside a right-hand coordinate system;
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
		double x = y1*z2 - z1*y2;
		double y = z1*x2 - x1*z2;
		double z = x1*y2 - y1*x2;
		result.set(x,y,z);
	}

	/**
	 * Compute the signed angle between two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 */
	@Pure
	static double signedAngle(double x1, double y1, double z1, double x2, double y2, double z2) {
		double lengths = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1) * Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
		if (lengths == 0.)
			return Double.NaN;

		// First method
		// Angle
		// A . B = |A|.|B|.cos(theta)
		double dot = dotProduct(x1, y1, z1, x2, y2, z2) / lengths;
		if (dot < -1.0)
			dot = -1.0;
		if (dot > 1.0)
			dot = 1.0;
		double angle = Math.acos(dot);

		// On which side of A, B is located?
		if ((dot > -1) && (dot < 1)) {
			// det(A,B) = |A|.|B|.sin(theta)
			dot = perpProduct(x1, y1, z1, x2, y2, z2) / lengths;
			if (dot < 0)
				angle = -angle;
		}

		return angle;
	}
	
	
	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param vector1 the first tuple
	 * @param vector2 the second tuple
	 */
	default void add(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		set(vector1.getX() + vector2.getX(),
			vector1.getY() + vector2.getY(),
			vector1.getZ() + vector2.getZ());
	}


	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param vector the other tuple
	 */
	default void add(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
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
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
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
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
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
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
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
	default void scaleAdd(double scale, Vector3D<?, ?> vector){
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
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
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		set(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param point1 the first tuple
	 * @param point2 the second tuple
	 */
	default void sub(Point3D<?, ?> point1, Point3D<?, ?> point2) {
		assert (point1 != null) : "First point must be not null"; //$NON-NLS-1$
		assert (point2 != null) : "Second point must be not null"; //$NON-NLS-1$
		set(point1.getX() - point2.getX(), point1.getY() - point2.getY(), point1.getZ() - point2.getZ());
	}

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param vector the other tuple
	 */
	default void sub(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		set(getX() - vector.getX(), getY() - vector.getY(), getZ() - vector.getZ());
	}

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param vector the other vector
	 * @return the dot product.
	 */
	@Pure
	default double dot(Vector3D<?, ?> vector) {
		assert(vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return dotProduct(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ());
	}

	/** Compute the determinant of two vectors.
	 *
	 * <p><pre><code>det(this, V) = |this|.|V|.sin(a)</code></pre>
	 * where <code>this</code> and <code>V</code> are two vectors
	 * and <code>a</code> is the angle between <code>this</code>
	 * and <code>V</code>. 
	 * 
	 * @param vector
	 * @return the perp product.
	 */
	@Pure
	default double perp(Vector3D<?, ?> vector) {
		assert(vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return perpProduct(getX(), getY(), getZ(), vector.getX(), vector.getY(), vector.getZ());
	}

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
	public Vector3D<?, ?> cross(Vector3D<?, ?> v1);

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
	public void cross(Vector3D<?, ?> v1, Vector3D<?, ?> v2);

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
	public Vector3D<?, ?> crossLeftHand(Vector3D<?, ?> v1);

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
	public void crossLeftHand(Vector3D<?, ?> v1, Vector3D<?, ?> v2);

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
	public Vector3D<?, ?> crossRightHand(Vector3D<?, ?> v1);

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
	public void crossRightHand(Vector3D<?, ?> v1, Vector3D<?, ?> v2);

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
	public void normalize(Vector3D<?, ?> v1);

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
	public double angle(Vector3D<?, ?> v1);

	/** Turn this vector about the given rotation angle.
	 *
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation angle in radians.
	 */
	public void turnVector(Vector3D<?, ?> axis, double angle);
	
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
	public boolean isColinear(Vector3D<?, ?> v);

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
	public Vector3D<?, ?> toUnmodifiable();

}

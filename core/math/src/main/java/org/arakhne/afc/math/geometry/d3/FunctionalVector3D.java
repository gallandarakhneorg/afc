/**
 * 
 * org.arakhne.afc.math.geometry.d3.FunctionalVector3D.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 *
 * Primary author : Olivier LAMOTTE (olivier.lamotte@utbm.fr) - 2015
 *
 */
package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.continuous.Quaternion;
import org.arakhne.afc.math.geometry.d3.continuous.Transform3D;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public interface FunctionalVector3D extends Vector3D {

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
	public static double determinant(
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
	public static double perpProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
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
	public static boolean isCollinearVectors(double x1, double y1, double z1, double x2, double y2, double z2) {
		// Cross product
		double cx = y1 * z2 - z1 * y2;
		double cy = z1 * x2 - x1 * z2;
		double cz = x1 * y2 - y1 * x2;

		double sum = cx * cx + cy * cy + cz * cz;

		return MathUtil.isEpsilonZero(sum);
	}

	/**
	 * Compute the dot product of two 3D vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return the dot product.
	 */
	@Pure
	public static double dotProduct(double x1, double y1, double z1, double x2,
			double y2, double z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
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
	public static void crossProduct(
			double x1, double y1, double z1,
			double x2, double y2, double z2, Vector3D result) {
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
	public static void crossProduct(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			CoordinateSystem3D system, Vector3D result) {
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
	public static void crossProductLeftHand(
			double x1, double y1, double z1,
			double x2, double y2, double z2, Vector3D result) {
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
	public static void crossProductRightHand(
			double x1, double y1, double z1,
			double x2, double y2, double z2, Vector3D result) {
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
	public static double signedAngle(double x1, double y1, double z1, double x2, double y2, double z2) {
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

	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#add(org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void add(Vector3D t1, Vector3D t2) {
		this.setX(t1.getX() + t2.getX());
		this.setY(t1.getY() + t2.getY());
		this.setZ(t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#add(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void add(Vector3D t1) {
		this.setX(this.getX() + t1.getX());
		this.setY(this.getY() + t1.getY());
		this.setZ(this.getZ() + t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#scaleAdd(int, org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(int s, Vector3D t1, Vector3D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
		this.setZ(s * t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#scaleAdd(double, org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(double s, Vector3D t1, Vector3D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
		this.setZ(s * t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#scaleAdd(int, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(int s, Vector3D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
		this.setZ(s * this.getZ() + t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#scaleAdd(double, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(double s, Vector3D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
		this.setZ(s * this.getZ() + t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#sub(org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void sub(Vector3D t1, Vector3D t2) {
		this.setX(t1.getX() - t2.getX());
		this.setY(t1.getY() - t2.getY());
		this.setZ(t1.getZ() - t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#sub(org.arakhne.afc.math.geometry.d3.Point3D, org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Override
	default void sub(Point3D t1, Point3D t2) {
		this.setX(t1.getX() - t2.getX());
		this.setY(t1.getY() - t2.getY());
		this.setZ(t1.getZ() - t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#sub(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void sub(Vector3D t1) {
		this.setX(this.getX() - t1.getX());
		this.setY(this.getY() - t1.getY());
		this.setZ(this.getZ() - t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#dot(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Pure
	@Override
	default double dot(Vector3D v1) {
		return (this.getX()*v1.getX() + this.getY()*v1.getY() + this.getZ()*v1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#perp(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Pure
	@Override
	default double perp(Vector3D v) {
		return perpProduct(this.getX(), this.getY(), this.getZ(), v.getX(), v.getY(), v.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#cross(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Pure
	@Override
	default Vector3D cross(Vector3D v1) {
		Vector3f v = new Vector3f();
		crossProduct(
				getX(), getY(), getZ(),
				v1.getX(), v1.getY(), v1.getZ(),
				v);
		return v;
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#cross(org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void cross(Vector3D v1, Vector3D v2) {
		crossProduct(v1.getX(), v1.getY(), v1.getZ(),
					 v2.getX(), v2.getY(), v2.getZ(),
					 this);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Pure
	@Override
	default Vector3D crossLeftHand(Vector3D v1) {
		Vector3f v = new Vector3f();
		crossProductLeftHand(
				this.getX(), this.getY(), this.getZ(),
				v1.getX(), v1.getY(), v1.getZ(),
				v);
		return v;
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void crossLeftHand(Vector3D v1, Vector3D v2) {
		crossProductLeftHand(v1.getX(), v1.getY(), v1.getZ(),
							 v2.getX(), v2.getY(), v2.getZ(),
							 this);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Pure
	@Override
	default Vector3D crossRightHand(Vector3D v1) {
		Vector3f v = new Vector3f();
		crossProductRightHand(
				this.getX(), this.getY(), this.getZ(),
				v1.getX(), v1.getY(), v1.getZ(),
				v);
		return v;
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void crossRightHand(Vector3D v1, Vector3D v2) {
		crossProductRightHand(v1.getX(), v1.getY(), v1.getZ(),
							  v2.getX(), v2.getY(), v2.getZ(),
							  this);
	}
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#length()
	 */
	@Pure
	@Override
	default double length() {
		return Math.sqrt(this.getX()*this.getX() + this.getY()*this.getY() + this.getZ()*this.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#lengthSquared()
	 */
	@Pure
	@Override
	default double lengthSquared() {
		return (this.getX()*this.getX() + this.getY()*this.getY() + this.getZ()*this.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#normalize(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void normalize(Vector3D v1) {
		if(v1.length()==0){
			throw new ArithmeticException();
		}
		double norm = 1f / v1.length();
		this.setX((int)(v1.getX()*norm));
		this.setY((int)(v1.getY()*norm));
		this.setZ((int)(v1.getZ()*norm));        
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#normalize()
	 */
	@Override
	default void normalize() {
		double length = this.length();
		
		if(length==0){
			throw new ArithmeticException();
		}
		this.setX(this.getX() / length);
		this.setY(this.getY() / length);
		this.setZ(this.getZ() / length);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#angle(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Pure
	@Override
	default double angle(Vector3D v1) {
		double vDot = this.dot(v1) / ( this.length()*v1.length() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return ((Math.acos( vDot )));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#turnVector(org.arakhne.afc.math.geometry.d3.Vector3D, double)
	 */
	@Override
	default void turnVector(Vector3D axis, double angle) {
		Transform3D mat = new Transform3D();
		mat.setRotation(new Quaternion(axis, angle));
		mat.transform(this);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#isUnitVector()
	 */
	@Pure
	@Override
	default boolean isUnitVector() {
		return MathUtil.isEpsilonEqual(this.lengthSquared(), 1.);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#isColinear(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Pure
	@Override
	default boolean isColinear(Vector3D v) {
		return isCollinearVectors(this.getX(), this.getY(), this.getZ(), v.getX(), v.getY(), v.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Vector3D#setLength(double)
	 */
	@Override
	default void setLength(double newLength) {
		double nl = Math.max(0, newLength);
		double l = this.length();
		if (l != 0) {
			double f = nl / l;
			this.setX(this.getX() * f);
			this.setY(this.getY() * f);
			this.setZ(this.getZ() * f);
		} else {
			this.setX(newLength);
			this.setY(0);
			this.setZ(0);
		}
	}
	

	/**
	 * Multiply this vector, transposed, by the given matrix and replies the resulting vector.
	 * 
	 * @param m
	 * @return transpose(this * m)
	 */
	@Pure
	default Vector3f mul(Matrix3f m) {
		Vector3f r = new Vector3f();
		r.setX(this.getX() * m.m00 + this.getY() * m.m01 + this.getZ() * m.m02);
		r.setY(this.getX() * m.m10 + this.getY() * m.m11 + this.getZ() * m.m12);
		r.setZ(this.getX() * m.m20 + this.getY() * m.m21 + this.getZ() * m.m22);
		return r;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Pure
	default double signedAngle(Vector3D v1) {
		return signedAngle(this.getX(), this.getY(), this.getZ(), v1.getX(), v1.getY(), v1.getZ());
	}
	
	
	public interface UnmodifiableVector3D extends Vector3D {
		
		@Override
		default void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void absolute(Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(double x, double y, double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(int min, int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(int min, int max, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max, Vector3D t) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		default void negate(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(Tuple3D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double x, double y, double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(double x, double y, double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(Vector3D t1, Vector3D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(Vector3D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Point3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void cross(Vector3D v1, Vector3D v2) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		default void crossLeftHand(Vector3D v1, Vector3D v2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void crossRightHand(Vector3D v1, Vector3D v2) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		default void normalize(Vector3D v1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void normalize() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void turnVector(Vector3D axis, double angle) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setLength(double newLength) {
			throw new UnsupportedOperationException();
		}
		
	}
	
}

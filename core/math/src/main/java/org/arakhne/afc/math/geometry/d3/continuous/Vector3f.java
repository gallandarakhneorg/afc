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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D Vector with 3 floating-point values.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Vector3f extends Tuple3f<Vector3D> implements FunctionalVector3D {

	private static final long serialVersionUID = -1222875298451525734L;

//	/** Compute the determinant of three vectors.
//	 * 
//	 * @param x1
//	 * @param y1
//	 * @param z1
//	 * @param x2
//	 * @param y2
//	 * @param z2
//	 * @param x3
//	 * @param y3
//	 * @param z3
//	 * @return the determinant
//	 * @see #perpProduct(double, double, double, double, double, double)
//	 */
//	public static double determinant(
//			double x1, double y1, double z1,
//			double x2, double y2, double z2,
//			double x3, double y3, double z3) {
//		return
//				  x1 * (y2 * z3 - y3 * z2)
//				+ x2 * (y3 * z1 - y1 * z3)
//				+ x3 * (y1 * z2 - y2 * z1);
//	}
//
//	/** Compute the determinant of two vectors.
//	 * <p>
//	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
//	 * where <code>X1</code> and <code>X2</code> are two vectors
//	 * and <code>a</code> is the angle between <code>X1</code>
//	 * and <code>X2</code>. 
//	 * 
//	 * @param x1
//	 * @param y1
//	 * @param z1
//	 * @param x2
//	 * @param y2
//	 * @param z2
//	 * @return the determinant
//	 * @see #determinant(double, double, double, double, double, double, double, double, double)
//	 */
//	public static double perpProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
//		/* First method:
//		 * 
//		 * det(A,B) = |A|.|B|.sin(theta)
//		 * A x B = |A|.|B|.sin(theta).N, where N is the unit vector
//		 * A x B = det(A,B).N
//		 * A x B = [ y1*z2 - z1*y2 ] = det(A,B).N
//		 *         [ z1*x2 - x1*z2 ]
//		 *         [ x1*y2 - y1*x2 ]
//		 * det(A,B) = sum(A x B)        
//		 * 
//		 * Second method:
//		 * 
//		 * det(A,B) = det( [ x1 x2 1 ]
//		 *                 [ y1 y2 1 ]
//		 *                 [ z1 z2 1 ] )
//		 * det(A,B) = x1*y2*1 + y1*z2*1 + z1*x2*1 - 1*y2*z1 - 1*z2*x1 - 1*x2*y1
//		 */
//		return x1*y2 + y1*z2 + z1*x2 - y2*z1 - z2*x1 - x2*y1;
//	}
//
//	/**
//	 * Replies if two vectors are colinear.
//	 * <p>
//	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
//	 * 
//	 * @param x1
//	 *            is the X coordinate of the first vector
//	 * @param y1
//	 *            is the Y coordinate of the first vector
//	 * @param z1
//	 *            is the Z coordinate of the first vector
//	 * @param x2
//	 *            is the X coordinate of the second vector
//	 * @param y2
//	 *            is the Y coordinate of the second vector
//	 * @param z2
//	 *            is the Z coordinate of the second vector
//	 * @return <code>true</code> if the two given vectors are colinear.
//	 * @since 3.0
//	 * @see MathUtil#isEpsilonZero(double)
//	 */
//	public static boolean isCollinearVectors(double x1, double y1, double z1, double x2, double y2, double z2) {
//		// Cross product
//		double cx = y1 * z2 - z1 * y2;
//		double cy = z1 * x2 - x1 * z2;
//		double cz = x1 * y2 - y1 * x2;
//
//		double sum = cx * cx + cy * cy + cz * cz;
//
//		return MathUtil.isEpsilonZero(sum);
//	}
//
//	/**
//	 * Compute the dot product of two 3D vectors.
//	 * 
//	 * @param x1
//	 * @param y1
//	 * @param z1
//	 * @param x2
//	 * @param y2
//	 * @param z2
//	 * @return the dot product.
//	 */
//	public static double dotProduct(double x1, double y1, double z1, double x2,
//			double y2, double z2) {
//		return x1 * x2 + y1 * y2 + z1 * z2;
//	}
//
//	/**
//	 * Computes the cross product of the vectors v1 and v2.
//	 * This function uses the
//	 * {@link #crossProductLeftHand(double, double, double, double, double, double, Vector3D) left-handed cross product}
//	 * if the default coordinate system is left-handed. Otherwise, it uses the
//	 * {@link #crossProductRightHand(double, double, double, double, double, double, Vector3D) right-handed cross product}.
//	 * The default coordinate system is given by
//	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
//	 * 
//	 * <img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
//	 * <img src="doc-files/left_handed_cross_product.png" alt="[Right-Handed Cross Product]">
//	 * 
//	 * @param x1 x coordinate of the vector v1.
//	 * @param y1 y coordinate of the vector v1.
//	 * @param z1 z coordinate of the vector v1.
//	 * @param x2 x coordinate of the vector v2.
//	 * @param y2 y coordinate of the vector v2.
//	 * @param z2 z coordinate of the vector v2.
//	 * @param result is the result of the cross product.
//	 */
//	public static void crossProduct(
//			double x1, double y1, double z1,
//			double x2, double y2, double z2, Vector3D result) {
//		crossProduct(x1, y1, z1, x2, y2, z2,
//				CoordinateSystem3D.getDefaultCoordinateSystem(), result);
//	}
//
//	/**
//	 * Computes the cross product of the vectors v1 and v2.
//	 * This function uses the
//	 * {@link #crossProductLeftHand(double, double, double, double, double, double, Vector3D) left-handed cross product}
//	 * if the given coordinate system is left-handed. Otherwise, it uses the
//	 * {@link #crossProductRightHand(double, double, double, double, double, double, Vector3D) right-handed cross product}.
//	 * 
//	 * <img src="doc-files/left_handed_cross_product.png" alt="[Left-Handed Cross Product]">
//	 * <img src="doc-files/left_handed_cross_product.png" alt="[Right-Handed Cross Product]">
//	 * 
//	 * @param x1 x coordinate of the vector v1.
//	 * @param y1 y coordinate of the vector v1.
//	 * @param z1 z coordinate of the vector v1.
//	 * @param x2 x coordinate of the vector v2.
//	 * @param y2 y coordinate of the vector v2.
//	 * @param z2 z coordinate of the vector v2.
//	 * @param system the coordinate system to consider for computing the cross product.
//	 * @param result is the result of the cross product.
//	 */
//	public static void crossProduct(
//			double x1, double y1, double z1,
//			double x2, double y2, double z2,
//			CoordinateSystem3D system, Vector3D result) {
//		if (system.isLeftHanded()) {
//			crossProductLeftHand(
//					x1, y1, z1,
//					x2, y2, z2,
//					result);
//		}
//		else {
//			crossProductRightHand(
//					x1, y1, z1,
//					x2, y2, z2,
//					result);
//		}
//	}
//
//	/**
//	 * Computes the cross product of the vectors v1 and v2
//	 * as if the vectors are inside a left-hand coordinate system;
//	 * 
//	 * <img src="doc-files/left_handed_cross_product.png">
//	 *
//	 * @param x1 x coordinate of the vector v1.
//	 * @param y1 y coordinate of the vector v1.
//	 * @param z1 z coordinate of the vector v1.
//	 * @param x2 x coordinate of the vector v2.
//	 * @param y2 y coordinate of the vector v2.
//	 * @param z2 z coordinate of the vector v2.
//	 * @param result is the result of the cross product.
//	 */
//	public static void crossProductLeftHand(
//			double x1, double y1, double z1,
//			double x2, double y2, double z2, Vector3D result) {
//		double x = y2*z1 - z2*y1;
//		double y = z2*x1 - x2*z1;
//		double z = x2*y1 - y2*x1;
//		result.set(x, y, z);
//	}
//
//	/**
//	 * Computes the cross product of the vectors v1 and v2
//	 * as if the vectors are inside a right-hand coordinate system;
//	 * 
//	 * <img src="doc-files/right_handed_cross_product.png">
//	 *
//	 * @param x1 x coordinate of the vector v1.
//	 * @param y1 y coordinate of the vector v1.
//	 * @param z1 z coordinate of the vector v1.
//	 * @param x2 x coordinate of the vector v2.
//	 * @param y2 y coordinate of the vector v2.
//	 * @param z2 z coordinate of the vector v2.
//	 * @param result is the result of the cross product.
//	 */
//	public static void crossProductRightHand(
//			double x1, double y1, double z1,
//			double x2, double y2, double z2, Vector3D result) {
//		double x = y1*z2 - z1*y2;
//		double y = z1*x2 - x1*z2;
//		double z = x1*y2 - y1*x2;
//		result.set(x,y,z);
//	}
//
//	/**
//	 * Compute the signed angle between two vectors.
//	 * 
//	 * @param x1
//	 * @param y1
//	 * @param z1
//	 * @param x2
//	 * @param y2
//	 * @param z2
//	 * @return the angle between <code>-PI</code> and <code>PI</code>.
//	 */
//	public static double signedAngle(double x1, double y1, double z1, double x2, double y2, double z2) {
//		double lengths = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1) * Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
//		if (lengths == 0.)
//			return Double.NaN;
//
//		// First method
//		// Angle
//		// A . B = |A|.|B|.cos(theta)
//		double dot = dotProduct(x1, y1, z1, x2, y2, z2) / lengths;
//		if (dot < -1.0)
//			dot = -1.0;
//		if (dot > 1.0)
//			dot = 1.0;
//		double angle = Math.acos(dot);
//
//		// On which side of A, B is located?
//		if ((dot > -1) && (dot < 1)) {
//			// det(A,B) = |A|.|B|.sin(theta)
//			dot = perpProduct(x1, y1, z1, x2, y2, z2) / lengths;
//			if (dot < 0)
//				angle = -angle;
//		}
//
//		return angle;
//	}
	
	/**
	 */
	public Vector3f() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3f(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3f(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3f(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(long x, long y, long z) {
		super(x,y,z);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Vector3f clone() {
		return (Vector3f)super.clone();
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double angle(Vector3D v1) {
//		double vDot = dot(v1) / ( length()*v1.length() );
//		if( vDot < -1.) vDot = -1.;
//		if( vDot >  1.) vDot =  1.;
//		return ((Math.acos( vDot )));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	public double signedAngle(Vector3D v1) {
//		return signedAngle(this.getX(), this.getY(), this.getZ(), v1.getX(), v1.getY(), v1.getZ());
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double dot(Vector3D v1) {
//		return dotProduct(this.x,this.y,this.z,v1.getX(),v1.getY(),v1.getZ());
//	}
//
//	/**
//	 * Multiply this vector, transposed, by the given matrix and replies the resulting vector.
//	 * 
//	 * @param m
//	 * @return transpose(this * m)
//	 */
//	public final Vector3f mul(Matrix3f m) {
//		Vector3f r = new Vector3f();
//		r.x = this.getX() * m.m00 + this.getY() * m.m01 + this.getZ() * m.m02;
//		r.y = this.getX() * m.m10 + this.getY() * m.m11 + this.getZ() * m.m12;
//		r.z = this.getX() * m.m20 + this.getY() * m.m21 + this.getZ() * m.m22;
//		return r;
//	}
//
//	/** {@inheritDoc}
//	 *
//	 * @see #determinant(double, double, double, double, double, double, double, double, double)
//	 */
//	@Override
//	public double perp(Vector3D v) {
//		return perpProduct(this.x, this.y, this.z, v.getX(), v.getY(), v.getZ());
//	}
//
//	@Override
//	public Vector3D cross(Vector3D v1) {
//		Vector3f v = new Vector3f();
//		crossProduct(
//				getX(), getY(), getZ(),
//				v1.getX(), v1.getY(), v1.getZ(),
//				v);
//		return v;
//	}
//
//	@Override
//	public void cross(Vector3D v1, Vector3D v2) {
//		crossProduct(
//			v1.getX(), v1.getY(), v1.getZ(),
//			v2.getX(), v2.getY(), v2.getZ(),
//			this);
//	}
//
//	@Override
//	public Vector3D crossLeftHand(Vector3D v1) {
//		Vector3f v = new Vector3f();
//		crossProductLeftHand(
//				this.getX(), this.getY(), this.getZ(),
//				v1.getX(), v1.getY(), v1.getZ(),
//				v);
//		return v;
//	}
//
//	@Override
//	public void crossLeftHand(Vector3D v1, Vector3D v2) {
//		crossProductLeftHand(
//				v1.getX(), v1.getY(), v1.getZ(),
//				v2.getX(), v2.getY(), v2.getZ(),
//				this);
//	}
//
//	@Override
//	public Vector3D crossRightHand(Vector3D v1) {
//		Vector3f v = new Vector3f();
//		crossProductRightHand(
//				this.getX(), this.getY(), this.getZ(),
//				v1.getX(), v1.getY(), v1.getZ(),
//				v);
//		return v;
//	}
//
//	@Override
//	public void crossRightHand(Vector3D v1, Vector3D v2) {
//		crossProductRightHand(
//				v1.getX(), v1.getY(), v1.getZ(),
//				v2.getX(), v2.getY(), v2.getZ(),
//				this);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double length() {
//        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public double lengthSquared() {
//        return (this.x*this.x + this.y*this.y + this.z*this.z);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void normalize(Vector3D v1) {
//		if(v1.length()==0){
//			throw new ArithmeticException();
//		}
//		else {
//			double norm = 1f / v1.length();
//			this.x = (int)(v1.getX()*norm);
//			this.y = (int)(v1.getY()*norm);
//			this.z = (int)(v1.getZ()*norm);
//		}
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void normalize() {
//        double norm;
//        double length = this.length();
//		
//		if(length==0){
//			throw new ArithmeticException();
//		}
//		else {
//			norm = 1./Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
//        	this.x *= norm;
//        	this.y *= norm;
//        	this.z *= norm;
//		}	
//	}
//
//	/**
//	 * Unstable function, sometimes works, and sometimes not
//	 */
//	@Override
//	public void turnVector(Vector3D axis, double angle) {
//		Transform3D mat = new Transform3D();
//		mat.setRotation(new Quaternion(axis, angle));
//		mat.transform(this);
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void add(Vector3D t1, Vector3D t2) {
//		this.x = t1.getX() + t2.getX();
//		this.y = t1.getY() + t2.getY();
//		this.z = t1.getZ() + t2.getZ();
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void add(Vector3D t1) {
//		this.x += t1.getX();
//		this.y += t1.getY();
//		this.z += t1.getZ();
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void scaleAdd(int s, Vector3D t1, Vector3D t2) {
//		this.x = s * t1.getX() + t2.getX();
//		this.y = s * t1.getY() + t2.getY();
//		this.z = s * t1.getZ() + t2.getZ();
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void scaleAdd(double s, Vector3D t1, Vector3D t2) {
//		this.x = s * t1.getX() + t2.getX();
//		this.y = s * t1.getY() + t2.getY();
//		this.z = s * t1.getZ() + t2.getZ();
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void scaleAdd(int s, Vector3D t1) {
//		this.x = s * this.x + t1.getX();
//		this.y = s * this.y + t1.getY();
//		this.z = s * this.z + t1.getZ();
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void scaleAdd(double s, Vector3D t1) {
//		this.x = s * this.x + t1.getX();
//		this.y = s * this.y + t1.getY();
//		this.z = s * this.z + t1.getZ();
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void sub(Vector3D t1, Vector3D t2) {
//		this.x = t1.getX() - t2.getX();
//		this.y = t1.getY() - t2.getY();
//		this.z = t1.getZ() - t2.getZ();
//	}
//
//	@Override
//	public void sub(Point3D t1, Point3D t2) {
//		this.x = t1.getX() - t2.getX();
//		this.y = t1.getY() - t2.getY();
//		this.z = t1.getZ() - t2.getZ();
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void sub(Vector3D t1) {
//		this.x -= t1.getX();
//		this.y -= t1.getY();
//		this.z -= t1.getZ();
//	}
//
//	@Override
//	public boolean isUnitVector() {
//		return MathUtil.isEpsilonEqual(length(), 1.);
//	}
//	
//	@Override
//	public boolean isColinear(Vector3D v) {
//		return isCollinearVectors(getX(), getY(), getZ(), v.getX(), v.getY(), v.getZ());
//	}
//
//	
//	@Override
//	public void setLength(double newLength) {
//		double nl = Math.max(0, newLength);
//		double l = length();
//		if (l != 0) {
//			double f = nl / l;
//			this.x *= f;
//			this.y *= f;
//			this.z *= f;
//		} else {
//			this.x = newLength;
//			this.y = 0;
//			this.z = 0;
//		}
//	}

	

	@Pure
	@Override
	public Vector3D toUnmodifiable() {
		return new UnmodifiableVector3f();
	}

	
	
	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiableVector3f implements FunctionalVector3D.UnmodifiableVector3D {
		
		private static final long serialVersionUID = 6113750458070037483L;

		public UnmodifiableVector3f() {
			//
		}

		@Pure
		@Override
		public Vector3D clone() {
			try {
				return (Vector3D) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		@Override
		public void get(Vector3D t) {
			Vector3f.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Vector3f.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Vector3f.this.get(t);
		}

		@Pure
		@Override
		public double getX() {
			return Vector3f.this.getX();
		}

		@Pure
		@Override
		public int ix() {
			return Vector3f.this.ix();
		}

		@Pure
		@Override
		public double getY() {
			return Vector3f.this.getY();
		}

		@Pure
		@Override
		public int iy() {
			return Vector3f.this.iy();
		}

		@Pure
		@Override
		public double getZ() {
			return Vector3f.this.getZ();
		}

		@Pure
		@Override
		public int iz() {
			return Vector3f.this.iz();
		}

		@Pure
		@Override
		public boolean equals(Tuple3D<?> t1) {
			return Vector3f.this.equals(t1);
		}

		@Pure
		@Override
		public int hashCode() {
			return Vector3f.this.hashCode();
		}

		@Pure
		@Override
		public boolean epsilonEquals(Vector3D t1, double epsilon) {
			return Vector3f.this.epsilonEquals(t1, epsilon);
		}

		@Pure
		@Override
		public double dot(Vector3D v1) {
			return Vector3f.this.dot(v1);
		}

		@Pure
		@Override
		public Vector3D cross(Vector3D v1) {
			return Vector3f.this.cross(v1);
		}

		@Pure
		@Override
		public Vector3D crossLeftHand(Vector3D v1) {
			return Vector3f.this.crossLeftHand(v1);
		}

		@Pure
		@Override
		public Vector3D crossRightHand(Vector3D v1) {
			return Vector3f.this.crossRightHand(v1);
		}

		@Pure
		@Override
		public double length() {
			return Vector3f.this.length();
		}

		@Pure
		@Override
		public double lengthSquared() {
			return Vector3f.this.lengthSquared();
		}

		@Pure
		@Override
		public double angle(Vector3D v1) {
			return Vector3f.this.angle(v1);
		}

		@Pure
		@Override
		public boolean isUnitVector() {
			return Vector3f.this.isUnitVector();
		}

		@Pure
		@Override
		public boolean isColinear(Vector3D v) {
			return Vector3f.this.isColinear(v);
		}

		@Pure
		@Override
		public Vector3D toUnmodifiable() {
			return this;
		}

		@Pure
		@Override
		public double perp(Vector3D v) {
			return Vector3f.this.perp(v);
		}
		
	}

}
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.matrix.Matrix2f;

/** 2D Vector with 2 floating-point values.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Vector2f extends Tuple2f<Vector2D> implements Vector2D {

	private static final long serialVersionUID = -2062941509400877679L;

	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
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
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	public static boolean isCollinearVectors(double x1, double y1, double x2, double y2) {
		// Test if three points are colinears
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
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
	 * @deprecated see {@link #perpProduct(double, double, double, double)}
	 */
	@Deprecated
	public static double determinant(double x1, double y1, double x2, double y2) {
		return perpProduct(x1, y1, x2, y2);
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
	public static double perpProduct(double x1, double y1, double x2, double y2) {
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
	public static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1*x2 + y1*y2;
	}

	/**
	 * Compute the signed angle between two vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 */
	public static double signedAngle(double x1, double y1, double x2, double y2) {
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
	public static double angleOfVector(double x1, double y1, double x2, double y2) {
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
	public static double angleOfVector(double x, double y) {
		return signedAngle(
				1, 0, 
				x, y);
	}

	/**
	 */
	public Vector2f() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2f(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2f(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2f(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(long x, long y) {
		super(x,y);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f clone() {
		return (Vector2f)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double angle(Vector2D v1) {
		double vDot = dot(v1) / ( length()*v1.length() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return((Math.acos( vDot )));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double dot(Vector2D v1) {
		return dotProduct(this.x, this.y, v1.getX(), v1.getY());
	}

	@Override
	public double perp(Vector2D x2) {
		return perpProduct(this.x, this.y, x2.getX(), x2.getY());
	}

	/**
	 * Multiply this vector, transposed, by the given matrix and replies the resulting vector.
	 * 
	 * @param m
	 * @return transpose(this * m)
	 */
	public final Vector2f mul(Matrix2f m) {
		Vector2f r = new Vector2f();
		r.x = this.getX() * m.m00 + this.getY() * m.m01;
		r.y = this.getX() * m.m10 + this.getY() * m.m11;
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void perpendicularize() {
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), right-handed
		//set(getY(), -getX());
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), left-handed
		set(-getY(), getX());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public double length() {
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	// Problematic when used in isUnitVector. Unintelligible problem
	public double lengthSquared() {
		return (this.x*this.x + this.y*this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize(Vector2D v1) {
		if(v1.length()==0){
			throw new ArithmeticException();
		}
		else {
			double norm = 1f / v1.length();
			this.x = (int)(v1.getX()*norm);
			this.y = (int)(v1.getY()*norm);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize() {
		double norm;
		double length = this.length();
		
		if(length==0){
			throw new ArithmeticException();
		}
		else {
		norm = 1./length;
		this.x *= norm;
		this.y *= norm;
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double signedAngle(Vector2D v) {
		return signedAngle(getX(), getY(), v.getX(), v.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnVector(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x =  cos * getX() + sin * getY(); 
		double y = -sin * getX() + cos * getY();
		set(x,y);
	}

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 * 
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	public static Vector2f toOrientationVector(double angle) {
		return new Vector2f(
				Math.cos(angle),
				Math.sin(angle));
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientationAngle() {
		double angle = Math.acos(getX());
		if (getY()<0f) angle = -angle;
		return MathUtil.clampCyclic(angle, 0., MathConstants.TWO_PI);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Vector2D t1, Vector2D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Vector2D t1) {
		this.x += t1.getX();
		this.y += t1.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(int s, Vector2D t1, Vector2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(double s, Vector2D t1, Vector2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(int s, Vector2D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(double s, Vector2D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(Vector2D t1, Vector2D t2) {
		this.x = t1.getX() - t2.getX();
		this.y = t1.getY() - t2.getY();
	}

	@Override
	public void sub(Point2D t1, Point2D t2) {
		this.x = t1.getX() - t2.getX();
		this.y = t1.getY() - t2.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(Vector2D t1) {
		this.x -= t1.getX();
		this.y -= t1.getY();
	}

	@Override
	public void setLength(double newLength) {
		double nl = Math.max(0, newLength);
		double l = length();
		if (l != 0) {
			double f = nl / l;
			this.x *= f;
			this.y *= f;
		} else {
			this.x = newLength;
			this.y = 0;
		}
	}

	@Override
	public boolean isUnitVector() {
		return ((this.length())==1);
	}

	@Override
	public Vector2D toUnmodifiable() {
		return new UnmodifiableVector2f();
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiableVector2f implements Vector2D {

		private static final long serialVersionUID = -8670105082548919880L;

		/**
		 */
		public UnmodifiableVector2f() {
			//
		}

		@Override
		public UnmodifiableVector2f clone() {
			return new UnmodifiableVector2f();
		}

		@Override
		public void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void absolute(Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(double x, double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clamp(int min, int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clamp(double min, double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(int min) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(double min) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clamp(int min, int max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clamp(double min, double max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(int min, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(double min, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(int max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(double max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void get(Vector2D t) {
			Vector2f.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Vector2f.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Vector2f.this.get(t);
		}

		@Override
		public void negate(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(int s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(double s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(int s) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(double s) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(Tuple2D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(double x, double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(int[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(double[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getX() {
			return Vector2f.this.getX();
		}

		@Override
		public int ix() {
			return Vector2f.this.ix();
		}

		@Override
		public void setX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getY() {
			return Vector2f.this.getY();
		}

		@Override
		public int iy() {
			return Vector2f.this.iy();
		}

		@Override
		public void setY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(double x, double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void subX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void subX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void subY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void subY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void interpolate(Vector2D t1, Vector2D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void interpolate(Vector2D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Tuple2D<?> t1) {
			return Vector2f.this.equals(t1);
		}

		@Override
		public int hashCode() {
			return Vector2f.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Vector2D t1, double epsilon) {
			return Vector2f.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public void add(Vector2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Vector2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Vector2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Vector2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Point2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double dot(Vector2D v1) {
			return Vector2f.this.dot(v1);
		}

		@Override
		public void perpendicularize() {
			throw new UnsupportedOperationException();
		}

		@Override
		public double length() {
			return Vector2f.this.length();
		}

		@Override
		public double lengthSquared() {
			return Vector2f.this.lengthSquared();
		}

		@Override
		public void normalize(Vector2D v1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void normalize() {
			throw new UnsupportedOperationException();
		}

		@Override
		public double angle(Vector2D v1) {
			return Vector2f.this.angle(v1);
		}

		@Override
		public double signedAngle(Vector2D v) {
			return Vector2f.this.signedAngle(v);
		}

		@Override
		public void turnVector(double angle) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getOrientationAngle() {
			return Vector2f.this.getOrientationAngle();
		}

		@Override
		public boolean isUnitVector() {
			return Vector2f.this.isUnitVector();
		}

		@Override
		public void setLength(double newLength) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector2D toUnmodifiable() {
			return this;
		}

		@Override
		public double perp(Vector2D x2) {
			return Vector2f.this.perp(x2);
		}

	}

}
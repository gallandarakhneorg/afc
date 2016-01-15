/**
 * 
 * org.arakhne.afc.math.geometry.d2.FunctionalVector2D.java
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
package org.arakhne.afc.math.geometry.d2;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author $Author: olamotte$
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface FunctionalVector2D extends Vector2D {

	
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
	@Pure
	public static boolean isCollinearVectors(double x1, double y1, double x2, double y2) {
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
	@Pure
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
	@Pure
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
	@Pure
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
	@Pure
	public static double angleOfVector(double x, double y) {
		return signedAngle(
				1, 0, 
				x, y);
	}
	
	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 * 
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	public static Vector2f toOrientationVector(double angle) {
		return new Vector2f(Math.cos(angle), Math.sin(angle));
	}
	
	

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#add(org.arakhne.afc.math.geometry.d2.Vector2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void add(Vector2D t1, Vector2D t2) {
		this.setX(t1.getX() + t2.getX());
		this.setY(t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#add(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void add(Vector2D t1) {
		this.setX(this.getX() + t1.getX());
		this.setY(this.getY() + t1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#scaleAdd(int, org.arakhne.afc.math.geometry.d2.Vector2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(int s, Vector2D t1, Vector2D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#scaleAdd(double, org.arakhne.afc.math.geometry.d2.Vector2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(double s, Vector2D t1, Vector2D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#scaleAdd(int, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(int s, Vector2D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#scaleAdd(double, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(double s, Vector2D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#sub(org.arakhne.afc.math.geometry.d2.Vector2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void sub(Vector2D t1, Vector2D t2) {
		this.setX(t1.getX() - t2.getX());
		this.setY(t1.getY() - t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#sub(org.arakhne.afc.math.geometry.d2.Point2D, org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Override
	default void sub(Point2D t1, Point2D t2) {
		this.setX(t1.getX() - t2.getX());
		this.setY(t1.getY() - t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#sub(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void sub(Vector2D t1) {
		this.setX(this.getX() - t1.getX());
		this.setY(this.getY() - t1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#dot(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Pure
	@Override
	default double dot(Vector2D v1) {
		return dotProduct(this.getX(), this.getY(), v1.getX(), v1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#perp(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Pure
	@Override
	default double perp(Vector2D x2) {
		return perpProduct(this.getX(), this.getY(), x2.getX(), x2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#perpendicularize()
	 */
	@Override
	default void perpendicularize() {
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), right-handed
		//set(getY(), -getX());
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), left-handed
		set(-this.getY(), this.getX());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#length()
	 */
	@Pure
	@Override
	default double length() {
		return Math.sqrt(this.getX()*this.getX() + this.getY()*this.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#lengthSquared()
	 */
	@Pure
	@Override
	default double lengthSquared() {
		return (this.getX()*this.getX() + this.getY()*this.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#normalize(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void normalize(Vector2D v1) {
		if(v1.length()==0){
			throw new ArithmeticException();
		}
		double norm = 1f / v1.length();
		this.setX((int)(v1.getX()*norm));
		this.setY((int)(v1.getY()*norm));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#normalize()
	 */
	@Override
	default void normalize() {
		double length = this.length();
		
		if(length==0){
			throw new ArithmeticException();
		}
		this.setX(this.getX() / length);
		this.setY(this.getY() / length);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#angle(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Pure
	@Override
	default double angle(Vector2D v1) {
		double vDot = dot(v1) / ( length()*v1.length() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return((Math.acos( vDot )));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#signedAngle(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Pure
	@Override
	default double signedAngle(Vector2D v) {
		return signedAngle(this.getX(), this.getY(), v.getX(), v.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#turnVector(double)
	 */
	@Override
	default void turnVector(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x =  cos * this.getX() + sin * this.getY(); 
		double y = -sin * this.getX() + cos * this.getY();
		set(x,y);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#getOrientationAngle()
	 */
	@Pure
	@Override
	default double getOrientationAngle() {
		double angle = Math.acos(this.getX());
		if (this.getY()<0f) angle = -angle;
		return MathUtil.clampCyclic(angle, 0., MathConstants.TWO_PI);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#isUnitVector()
	 */
	@Pure
	@Override
	default boolean isUnitVector() {
		return MathUtil.isEpsilonEqual(this.length(), 1.);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#setLength(double)
	 */
	@Override
	default void setLength(double newLength) {
		double nl = Math.max(0, newLength);
		double l = this.length();
		if (l != 0) {
			double f = nl / l;
			this.setX(this.getX() * f);
			this.setY(this.getY() * f);
		} else {
			this.setX(newLength);
			this.setY(0);
		}
	}
	
	
	/**
	 * Multiply this vector, transposed, by the given matrix and replies the resulting vector.
	 * 
	 * @param m
	 * @return transpose(this * m)
	 */
	@Pure
	default Vector2D mul(Matrix2f m) {
		Vector2D r = new Vector2f();
		r.setX(this.getX() * m.m00 + this.getY() * m.m01);
		r.setY(this.getX() * m.m10 + this.getY() * m.m11);
		return r;
	}
	
	

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Vector2D#toUnmodifiable()
	 */
	@Pure
	@Override
	default Vector2D toUnmodifiable() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public interface UnmodifiableVector2f extends Vector2D {

		@Override
		default void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void absolute(Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(double x, double y) {
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
		default void clamp(int min, int max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max, Vector2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s, Vector2D t1) {
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
		default void set(Tuple2D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double x, double y) {
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
		default void sub(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(double x, double y) {
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
		default void interpolate(Vector2D t1, Vector2D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(Vector2D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

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
		
	}
	
	
}

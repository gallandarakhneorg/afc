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
package org.arakhne.afc.math.geometry3d.continuous;

import java.io.Serializable;

import org.arakhne.afc.math.Matrix3f;
import org.arakhne.afc.math.Matrix4f;
import org.arakhne.afc.math.geometry3d.Vector3D;

/** A 4 element unit quaternion represented by single precision floating 
 * point a,b,c,d coordinates.  The quaternion is always normalized.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "http://en.wikipedia.org/wiki/Quaternion"
 */
public class Quaternion implements Cloneable, Serializable {

	private static final long serialVersionUID = 4494919776986180960L;
	
	private final static double EPS = 0.000001;
	private final static double EPS2 = 1.0e-30;

	/** Create a quaternion from the axis-angle representation.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param angle
	 */
	public static Quaternion newAxisAngle(float x, float y, float z, float angle) {
		Quaternion q = new Quaternion();
		q.setAxisAngle(x, y, z, angle);
		return q;
	}

	/** x coordinate.
	 */
	protected float a;

	/** y coordinate.
	 */
	protected float b;

	/** z coordinate.
	 */
	protected float c;

	/** w coordinate.
	 */
	protected float d;

	/**
	 */
	public Quaternion() {
		this.a = this.b = this.c = this.d = 0;
	}

	/** Create a quaternion {@code a + b.i + c.j + d.k}.
	 * Caution: the parameters are not an axis-angle representation. So, <var>d</var> is
	 * not the angle, and (<var>a</var>, <var>b</var>, <var>c</var>) is not the rotation axis.
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @see If you want to create a quaternion from a axis-angle representatipon, see {@link #newAxisAngle(float, float, float, float)}.
	 */
	public Quaternion(float a, float b, float c, float d) {
		float mag = (float)(1.0/Math.sqrt( a*a + b*b + c*c + d*d ));
		this.a = a*mag;
		this.b = b*mag;
		this.c = c*mag;
		this.d = d*mag;
	}

	/**
	 * @param axis
	 * @param angle
	 */
	public Quaternion(Vector3D axis, float angle) {
		setAxisAngle(axis, angle);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Quaternion clone() {
		try {
			return (Quaternion)super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/** Replies the a factor of the quaternion.
	 * 
	 * @return a
	 */
	public float getA() {
		return this.a;
	}

	/** Set the a factor of the quaternion.
	 * 
	 * @param a
	 */
	public void setA(float a) {
		this.a = a;
	}

	/** Replies the b factor of the quaternion.
	 * 
	 * @return b
	 */
	public float getB() {
		return this.b;
	}

	/** Set the b factor of the quaternion.
	 * 
	 * @param b
	 */
	public void setB(float b) {
		this.b = b;
	}

	/** Replies the c factor of the quaternion.
	 * 
	 * @return c
	 */
	public float getC() {
		return this.c;
	}

	/** Set the c factor of the quaternion.
	 * 
	 * @param c
	 */
	public void setC(float c) {
		this.c = c;
	}

	/** Replies the d factor of the quaternion.
	 * 
	 * @return d
	 */
	public float getD() {
		return this.d;
	}

	/** Set the d factor of the quaternion.
	 * 
	 * @param d
	 */
	public void setD(float d) {
		this.d = d;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object t1) {
		try {
			Quaternion t2 = (Quaternion) t1;
			return(this.a == t2.getA() && this.b == t2.getB() && this.c == t2.getC() && this.d == t2.getD());
		}
		catch(AssertionError e) {
			throw e;
		}
		catch (Throwable e2) {
			return false;
		}
	}

	/**
	 * Returns true if the distance between this quaternion
	 * and quaternion t1 is less than or equal to the epsilon parameter. 
	 * 
	 * @param t1  the quaternion to be compared to this quaternion
	 * @param epsilon  the threshold value  
	 * @return  true or false
	 */
	public boolean epsilonEquals(Quaternion t1, float epsilon) {
		float diff;

		diff = this.a - t1.getA();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.b - t1.getB();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.c - t1.getC();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.d - t1.getD();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Float.floatToIntBits(this.a);
		bits = 31 * bits + Float.floatToIntBits(this.b);
		bits = 31 * bits + Float.floatToIntBits(this.c);
		bits = 31 * bits + Float.floatToIntBits(this.d);
		return bits ^ (bits >> 32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.a
				+";" //$NON-NLS-1$
				+this.b
				+";" //$NON-NLS-1$
				+this.c
				+";" //$NON-NLS-1$
				+this.d
				+")"; //$NON-NLS-1$
	}

	/**
	 * Sets the value of this quaternion to the conjugate of quaternion q1.
	 * @param q1 the source vector
	 */
	public final void conjugate(Quaternion q1) {
		this.a = -q1.a;
		this.b = -q1.b;
		this.c = -q1.c;
		this.d = q1.d;
	}

	/**
	 * Sets the value of this quaternion to the conjugate of itself.
	 */
	public final void conjugate() {
		this.a = -this.a;
		this.b = -this.b;
		this.c = -this.c;
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * quaternions q1 and q2 (this = q1 * q2).  
	 * Note that this is safe for aliasing (e.g. this can be q1 or q2).
	 * @param q1 the first quaternion
	 * @param q2 the second quaternion
	 */
	public final void mul(Quaternion q1, Quaternion q2) {
		if (this != q1 && this != q2) {
			this.d = q1.d*q2.d - q1.a*q2.a - q1.b*q2.b - q1.c*q2.c;
			this.a = q1.d*q2.a + q2.d*q1.a + q1.b*q2.c - q1.c*q2.b;
			this.b = q1.d*q2.b + q2.d*q1.b - q1.a*q2.c + q1.c*q2.a;
			this.c = q1.d*q2.c + q2.d*q1.c + q1.a*q2.b - q1.b*q2.a;
		}
		else {
			float	a, b, d;

			d = q1.d*q2.d - q1.a*q2.a - q1.b*q2.b - q1.c*q2.c;
			a = q1.d*q2.a + q2.d*q1.a + q1.b*q2.c - q1.c*q2.b;
			b = q1.d*q2.b + q2.d*q1.b - q1.a*q2.c + q1.c*q2.a;
			this.c = q1.d*q2.c + q2.d*q1.c + q1.a*q2.b - q1.b*q2.a;
			this.d = d;
			this.a = a;
			this.b = b;
		}
	}


	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this * q1).  
	 * @param q1 the other quaternion
	 */
	public final void mul(Quaternion q1) {
		float     a, b, d; 

		d = this.d*q1.d - this.a*q1.a - this.b*q1.b - this.c*q1.c;
		a = this.d*q1.a + q1.d*this.a + this.b*q1.c - this.c*q1.b;
		b = this.d*q1.b + q1.d*this.b - this.a*q1.c + this.c*q1.a;
		this.c = this.d*q1.c + q1.d*this.c + this.a*q1.b - this.b*q1.a;
		this.d = d;
		this.a = a;
		this.b = b;
	} 

	/** 
	 * Multiplies quaternion q1 by the inverse of quaternion q2 and places
	 * the value into this quaternion.  The value of both argument quaternions 
	 * is preservered (this = q1 * q2^-1).
	 * @param q1 the first quaternion
	 * @param q2 the second quaternion
	 */ 
	public final void mulInverse(Quaternion q1, Quaternion q2) 
	{   
		Quaternion  tempQuat = q2.clone();  

		tempQuat.inverse(); 
		this.mul(q1, tempQuat); 
	}



	/**
	 * Multiplies this quaternion by the inverse of quaternion q1 and places
	 * the value into this quaternion.  The value of the argument quaternion
	 * is preserved (this = this * q^-1).
	 * @param q1 the other quaternion
	 */
	public final void mulInverse(Quaternion q1) {  
		Quaternion  tempQuat = q1.clone();

		tempQuat.inverse();
		this.mul(tempQuat);
	}

	/**
	 * Sets the value of this quaternion to quaternion inverse of quaternion q1.
	 * @param q1 the quaternion to be inverted
	 */
	public final void inverse(Quaternion q1) {
		float norm;

		norm = 1f/(q1.d*q1.d + q1.a*q1.a + q1.b*q1.b + q1.c*q1.c);
		this.d =  norm*q1.d;
		this.a = -norm*q1.a;
		this.b = -norm*q1.b;
		this.c = -norm*q1.c;
	}


	/**
	 * Sets the value of this quaternion to the quaternion inverse of itself.
	 */
	public final void inverse() {
		float norm;  

		norm = 1f/(this.d*this.d + this.a*this.a + this.b*this.b + this.c*this.c);
		this.d *=  norm;
		this.a *= -norm;
		this.b *= -norm;
		this.c *= -norm;
	}

	/**
	 * Sets the value of this quaternion to the normalized value
	 * of quaternion q1.
	 * @param q1 the quaternion to be normalized.
	 */
	public final void normalize(Quaternion q1) {
		float norm;

		norm = (q1.a*q1.a + q1.b*q1.b + q1.c*q1.c + q1.d*q1.d);

		if (norm > 0f) {
			norm = 1f/(float)Math.sqrt(norm);
			this.a = norm*q1.a;
			this.b = norm*q1.b;
			this.c = norm*q1.c;
			this.d = norm*q1.d;
		} else {
			this.a = 0f;
			this.b = 0f;
			this.c = 0f;
			this.d = 0f;
		}
	}


	/**
	 * Normalizes the value of this quaternion in place.
	 */
	public final void normalize() {
		float norm;

		norm = (this.a*this.a + this.b*this.b + this.c*this.c + this.d*this.d);

		if (norm > 0f) {
			norm = 1f / (float)Math.sqrt(norm);
			this.a *= norm;
			this.b *= norm;
			this.c *= norm;
			this.d *= norm;
		} else {
			this.a = 0f;
			this.b = 0f;
			this.c = 0f;
			this.d = 0f;
		}
	}

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param m1 the Matrix4f
	 */
	public final void setFromMatrix(Matrix4f m1) {
		float dd = 0.25f*(m1.m00 + m1.m11 + m1.m22 + m1.m33);

		if (dd >= 0) {
			if (dd >= EPS2) {
				this.d = (float) Math.sqrt(dd);
				dd =  0.25f/this.d;
				this.a = (m1.m21 - m1.m12)*dd;
				this.b = (m1.m02 - m1.m20)*dd;
				this.c = (m1.m10 - m1.m01)*dd;
				return;
			} 
		}
		else {
			this.d = 0;
			this.a = 0;
			this.b = 0;
			this.c = 1;
			return;
		}

		this.d = 0;
		dd = -0.5f*(m1.m11 + m1.m22);

		if (dd >= 0) {
			if (dd >= EPS2) {
				this.a = (float) Math.sqrt(dd);
				dd = 1.0f/(2.0f*this.a);
				this.b = m1.m10*dd;
				this.c = m1.m20*dd;
				return;
			}
		} else {
			this.a = 0;
			this.b = 0;
			this.c = 1;
			return;
		}

		this.a = 0;
		dd = 0.5f*(1.0f - m1.m22);

		if (dd >= EPS2) {
			this.b = (float) Math.sqrt(dd);
			this.c = m1.m21/(2.0f*this.b);
			return;
		}

		this.b = 0;
		this.c = 1;
	}

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param m1 the Matrix3f
	 */
	public final void setFromMatrix(Matrix3f m1) {
		float dd = 0.25f*(m1.m00 + m1.m11 + m1.m22 + 1.0f);

		if (dd >= 0) {
			if (dd >= EPS2) {
				this.d = (float) Math.sqrt(dd);
				dd = 0.25f/this.d;
				this.a = (m1.m21 - m1.m12)*dd;
				this.b = (m1.m02 - m1.m20)*dd;
				this.c = (m1.m10 - m1.m01)*dd;
				return;
			}
		} else {
			this.d = 0;
			this.a = 0;
			this.b = 0;
			this.c = 1;
			return;
		}

		this.d = 0;
		dd = -0.5f*(m1.m11 + m1.m22);
		if (dd >= 0) {
			if (dd >= EPS2) {
				this.a = (float) Math.sqrt(dd);
				dd = 0.5f/this.a;
				this.b = m1.m10*dd;
				this.c = m1.m20*dd;
				return;
			}
		} else {
			this.a = 0;
			this.b = 0;
			this.c = 1;
			return;
		}

		this.a = 0;
		dd =  0.5f*(1.0f - m1.m22);
		if (dd >= EPS2) {
			this.b = (float) Math.sqrt(dd);
			this.c = m1.m21/(2.0f*this.b);
			return;
		}

		this.b = 0;
		this.c = 1;
	}

	/** Set the quaternion coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void set(float x, float y, float z, float w) {
		float mag = (float)(1.0/Math.sqrt( x*x + y*y + z*z + w*w ));
		this.a = x*mag;
		this.b = y*mag;
		this.c = z*mag;
		this.d = w*mag;
	}

	/** Set the quaternion coordinates.
	 * 
	 * @param q
	 */
	public void set(Quaternion q) {
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
		this.w = q.w;
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation around the axis.
	 */
	public final void setAxisAngle(Vector3D axis, float angle) {
		setAxisAngle(axis.getX(),  axis.getY(), axis.getZ(), angle);
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * @param x is the x coordinate of the rotation axis
	 * @param y is the y coordinate of the rotation axis
	 * @param z is the z coordinate of the rotation axis
	 * @param angle is the rotation around the axis.
	 */
	public final void setAxisAngle(float x, float y, float z, float angle) {
		float mag,amag;
		// Quat = cos(theta/2) + sin(theta/2)(roation_axis) 
		amag = (float)Math.sqrt(x*x + y*y + z*z);
		if (amag < EPS ) {
			this.d = 0.0f;
			this.a = 0.0f;
			this.b = 0.0f;
			this.c = 0.0f;
		}
		else {  
			amag = 1.0f/amag; 
			mag = (float)Math.sin(angle/2.0);
			this.d = (float)Math.cos(angle/2.0);
			this.a = x*amag*mag;
			this.b = y*amag*mag;
			this.c = z*amag*mag;
		}
	}

	/** Replies the rotation axis represented by this quaternion.
	 * 
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, float)
	 * @see #setAxisAngle(float, float, float, float)
	 * @see #getAngle()
	 */
	public final Vector3f getAxis() {
		float mag = this.a*this.a + this.b*this.b + this.c*this.c;  

		if ( mag > EPS ) {
			mag = (float)Math.sqrt(mag);
			float invMag = 1f/mag;

			return new Vector3f(
					this.a*invMag,
					this.b*invMag,
					this.c*invMag);
		}
		return new Vector3f(0f, 0f, 1f);
	}

	/** Replies the rotation angle represented by this quaternion.
	 * 
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, float)
	 * @see #setAxisAngle(float, float, float, float)
	 * @see #getAxis()
	 */
	public final float getAngle() {
		float mag = this.a*this.a + this.b*this.b + this.c*this.c;  

		if ( mag > EPS ) {
			mag = (float)Math.sqrt(mag);
			return (2.f*(float)Math.atan2(mag, this.d)); 
		}
		return 0f;
	}

	/**
	 *  Performs a great circle interpolation between this quaternion
	 *  and the quaternion parameter and places the result into this
	 *  quaternion.
	 *  @param q1  the other quaternion
	 *  @param alpha  the alpha interpolation parameter
	 */
	public final void interpolate(Quaternion q1, float alpha) {
		// From "Advanced Animation and Rendering Techniques"
		// by Watt and Watt pg. 364, function as implemented appeared to be 
		// incorrect.  Fails to choose the same quaternion for the double
		// covering. Resulting in change of direction for rotations.
		// Fixed function to negate the first quaternion in the case that the
		// dot product of q1 and this is negative. Second case was not needed. 

		double dot,s1,s2,om,sinom;

		dot = this.a*q1.a + this.b*q1.b + this.c*q1.c + this.d*q1.d;

		if ( dot < 0 ) {
			// negate quaternion
			q1.a = -q1.a;  q1.b = -q1.b;  q1.c = -q1.c;  q1.d = -q1.d;
			dot = -dot;
		}

		if ( (1.0 - dot) > EPS ) {
			om = Math.acos(dot);
			sinom = Math.sin(om);
			s1 = Math.sin((1.0-alpha)*om)/sinom;
			s2 = Math.sin( alpha*om)/sinom;
		} else{
			s1 = 1.0 - alpha;
			s2 = alpha;
		}

		this.d = (float)(s1*this.d + s2*q1.d);
		this.a = (float)(s1*this.a + s2*q1.a);
		this.b = (float)(s1*this.b + s2*q1.b);
		this.c = (float)(s1*this.c + s2*q1.c);
	}



	/** 
	 *  Performs a great circle interpolation between quaternion q1
	 *  and quaternion q2 and places the result into this quaternion. 
	 *  @param q1  the first quaternion
	 *  @param q2  the second quaternion
	 *  @param alpha  the alpha interpolation parameter 
	 */   
	public final void interpolate(Quaternion q1, Quaternion q2, float alpha) { 
		// From "Advanced Animation and Rendering Techniques"
		// by Watt and Watt pg. 364, function as implemented appeared to be 
		// incorrect.  Fails to choose the same quaternion for the double
		// covering. Resulting in change of direction for rotations.
		// Fixed function to negate the first quaternion in the case that the
		// dot product of q1 and this is negative. Second case was not needed. 

		double dot,s1,s2,om,sinom;

		dot = q2.a*q1.a + q2.b*q1.b + q2.c*q1.c + q2.d*q1.d;

		if ( dot < 0 ) {
			// negate quaternion
			q1.a = -q1.a;  q1.b = -q1.b;  q1.c = -q1.c;  q1.d = -q1.d;
			dot = -dot;
		}

		if ( (1.0 - dot) > EPS ) {
			om = Math.acos(dot);
			sinom = Math.sin(om);
			s1 = Math.sin((1.0-alpha)*om)/sinom;
			s2 = Math.sin( alpha*om)/sinom;
		} else{
			s1 = 1.0 - alpha;
			s2 = alpha;
		}
		this.d = (float)(s1*q1.d + s2*q2.d);
		this.a = (float)(s1*q1.a + s2*q2.a);
		this.b = (float)(s1*q1.b + s2*q2.b);
		this.c = (float)(s1*q1.c + s2*q2.c);
	}
}
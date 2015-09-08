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
package org.arakhne.afc.math.continous.object3d;

import java.io.Serializable;

import org.arakhne.afc.math.generic.Vector3D;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.Matrix4f;

/** A 4 element unit quaternion represented by single precision floating 
 * point x,y,z,w coordinates.  The quaternion is always normalized.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d3.continuous.Quaternion}
 */
@Deprecated
public class Quaternion implements Cloneable, Serializable {

	private static final long serialVersionUID = 4494919776986180960L;
	
	private final static double EPS = 0.000001;
	private final static double EPS2 = 1.0e-30;

	/** x coordinate.
	 */
	protected double x;

	/** y coordinate.
	 */
	protected double y;

	/** z coordinate.
	 */
	protected double z;

	/** w coordinate.
	 */
	protected double w;

	/**
	 */
	public Quaternion() {
		this.x = this.y = this.z = this.w = 0;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Quaternion(double x, double y, double z, double w) {
		double mag = (double)(1.0/Math.sqrt( x*x + y*y + z*z + w*w ));
		this.x = x*mag;
		this.y = y*mag;
		this.z = z*mag;
		this.w = w*mag;
	}

	/**
	 * @param axis
	 * @param angle
	 */
	public Quaternion(Vector3D axis, double angle) {
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

	/** Replies the X coordinate.
	 * 
	 * @return x
	 */
	public double getX() {
		return this.x;
	}

	/** Set the X coordinate.
	 * 
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/** Replies the Y coordinate.
	 * 
	 * @return y
	 */
	public double getY() {
		return this.y;
	}

	/** Set the Y coordinate.
	 * 
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/** Replies the Z coordinate.
	 * 
	 * @return z
	 */
	public double getZ() {
		return this.z;
	}

	/** Set the Z coordinate.
	 * 
	 * @param z
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/** Replies the W coordinate.
	 * 
	 * @return w
	 */
	public double getW() {
		return this.w;
	}

	/** Set the W coordinate.
	 * 
	 * @param w
	 */
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object t1) {
		try {
			Quaternion t2 = (Quaternion) t1;
			return(this.x == t2.getX() && this.y == t2.getY() && this.z == t2.getZ() && this.w == t2.getW());
		}
		catch(AssertionError e) {
			throw e;
		}
		catch (Throwable e2) {
			return false;
		}
	}

	/**
	 * Returns true if the L-infinite distance between this tuple
	 * and tuple t1 is less than or equal to the epsilon parameter, 
	 * otherwise returns false.  The L-infinite
	 * distance is equal to MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param t1  the tuple to be compared to this tuple
	 * @param epsilon  the threshold value  
	 * @return  true or false
	 */
	public boolean epsilonEquals(Quaternion t1, double epsilon) {
		double diff;

		diff = this.x - t1.getX();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.y - t1.getY();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.z - t1.getZ();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.w - t1.getW();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.floatToIntBits(this.x);
		bits = 31 * bits + Double.floatToIntBits(this.y);
		bits = 31 * bits + Double.floatToIntBits(this.z);
		bits = 31 * bits + Double.floatToIntBits(this.w);
		return bits ^ (bits >> 32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.x
				+";" //$NON-NLS-1$
				+this.y
				+";" //$NON-NLS-1$
				+this.z
				+";" //$NON-NLS-1$
				+this.w
				+")"; //$NON-NLS-1$
	}

	/**
	 * Sets the value of this quaternion to the conjugate of quaternion q1.
	 * @param q1 the source vector
	 */
	public final void conjugate(Quaternion q1) {
		this.x = -q1.x;
		this.y = -q1.y;
		this.z = -q1.z;
		this.w = q1.w;
	}

	/**
	 * Sets the value of this quaternion to the conjugate of itself.
	 */
	public final void conjugate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
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
			this.w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
			this.x = q1.w*q2.x + q2.w*q1.x + q1.y*q2.z - q1.z*q2.y;
			this.y = q1.w*q2.y + q2.w*q1.y - q1.x*q2.z + q1.z*q2.x;
			this.z = q1.w*q2.z + q2.w*q1.z + q1.x*q2.y - q1.y*q2.x;
		}
		else {
			double	x, y, w;

			w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
			x = q1.w*q2.x + q2.w*q1.x + q1.y*q2.z - q1.z*q2.y;
			y = q1.w*q2.y + q2.w*q1.y - q1.x*q2.z + q1.z*q2.x;
			this.z = q1.w*q2.z + q2.w*q1.z + q1.x*q2.y - q1.y*q2.x;
			this.w = w;
			this.x = x;
			this.y = y;
		}
	}


	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this * q1).  
	 * @param q1 the other quaternion
	 */
	public final void mul(Quaternion q1) {
		double     x, y, w; 

		w = this.w*q1.w - this.x*q1.x - this.y*q1.y - this.z*q1.z;
		x = this.w*q1.x + q1.w*this.x + this.y*q1.z - this.z*q1.y;
		y = this.w*q1.y + q1.w*this.y - this.x*q1.z + this.z*q1.x;
		this.z = this.w*q1.z + q1.w*this.z + this.x*q1.y - this.y*q1.x;
		this.w = w;
		this.x = x;
		this.y = y;
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
		double norm;

		norm = 1f/(q1.w*q1.w + q1.x*q1.x + q1.y*q1.y + q1.z*q1.z);
		this.w =  norm*q1.w;
		this.x = -norm*q1.x;
		this.y = -norm*q1.y;
		this.z = -norm*q1.z;
	}


	/**
	 * Sets the value of this quaternion to the quaternion inverse of itself.
	 */
	public final void inverse() {
		double norm;  

		norm = 1f/(this.w*this.w + this.x*this.x + this.y*this.y + this.z*this.z);
		this.w *=  norm;
		this.x *= -norm;
		this.y *= -norm;
		this.z *= -norm;
	}

	/**
	 * Sets the value of this quaternion to the normalized value
	 * of quaternion q1.
	 * @param q1 the quaternion to be normalized.
	 */
	public final void normalize(Quaternion q1) {
		double norm;

		norm = (q1.x*q1.x + q1.y*q1.y + q1.z*q1.z + q1.w*q1.w);

		if (norm > 0f) {
			norm = 1f/(double)Math.sqrt(norm);
			this.x = norm*q1.x;
			this.y = norm*q1.y;
			this.z = norm*q1.z;
			this.w = norm*q1.w;
		} else {
			this.x = 0f;
			this.y = 0f;
			this.z = 0f;
			this.w = 0f;
		}
	}


	/**
	 * Normalizes the value of this quaternion in place.
	 */
	public final void normalize() {
		double norm;

		norm = (this.x*this.x + this.y*this.y + this.z*this.z + this.w*this.w);

		if (norm > 0f) {
			norm = 1f / (double)Math.sqrt(norm);
			this.x *= norm;
			this.y *= norm;
			this.z *= norm;
			this.w *= norm;
		} else {
			this.x = 0f;
			this.y = 0f;
			this.z = 0f;
			this.w = 0f;
		}
	}

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param m1 the Matrix4f
	 */
	public final void setFromMatrix(Matrix4f m1) {
		double ww = (double) (0.25f*(m1.m00 + m1.m11 + m1.m22 + m1.m33));

		if (ww >= 0) {
			if (ww >= EPS2) {
				this.w = (double) Math.sqrt(ww);
				ww =  0.25f/this.w;
				this.x = (double) ((m1.m21 - m1.m12)*ww);
				this.y = (double) ((m1.m02 - m1.m20)*ww);
				this.z = (double) ((m1.m10 - m1.m01)*ww);
				return;
			} 
		}
		else {
			this.w = 0;
			this.x = 0;
			this.y = 0;
			this.z = 1;
			return;
		}

		this.w = 0;
		ww = (double) (-0.5f*(m1.m11 + m1.m22));

		if (ww >= 0) {
			if (ww >= EPS2) {
				this.x = (double) Math.sqrt(ww);
				ww = 1.0f/(2.0f*this.x);
				this.y = (double) (m1.m10*ww);
				this.z = (double) (m1.m20*ww);
				return;
			}
		} else {
			this.x = 0;
			this.y = 0;
			this.z = 1;
			return;
		}

		this.x = 0;
		ww = (double) (0.5f*(1.0f - m1.m22));

		if (ww >= EPS2) {
			this.y = (double) Math.sqrt(ww);
			this.z = (double) (m1.m21/(2.0f*this.y));
			return;
		}

		this.y = 0;
		this.z = 1;
	}

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param m1 the Matrix3f
	 */
	public final void setFromMatrix(Matrix3f m1) {
		double ww = (double) (0.25f*(m1.m00 + m1.m11 + m1.m22 + 1.0f));

		if (ww >= 0) {
			if (ww >= EPS2) {
				this.w = (double) Math.sqrt(ww);
				ww = 0.25f/this.w;
				this.x = (double) ((m1.m21 - m1.m12)*ww);
				this.y = (double) ((m1.m02 - m1.m20)*ww);
				this.z = (double) ((m1.m10 - m1.m01)*ww);
				return;
			}
		} else {
			this.w = 0;
			this.x = 0;
			this.y = 0;
			this.z = 1;
			return;
		}

		this.w = 0;
		ww = (double) (-0.5f*(m1.m11 + m1.m22));
		if (ww >= 0) {
			if (ww >= EPS2) {
				this.x = (double) Math.sqrt(ww);
				ww = 0.5f/this.x;
				this.y = (double) (m1.m10*ww);
				this.z = (double) (m1.m20*ww);
				return;
			}
		} else {
			this.x = 0;
			this.y = 0;
			this.z = 1;
			return;
		}

		this.x = 0;
		ww =  (double) (0.5f*(1.0f - m1.m22));
		if (ww >= EPS2) {
			this.y = (double) Math.sqrt(ww);
			this.z = (double) (m1.m21/(2.0f*this.y));
			return;
		}

		this.y = 0;
		this.z = 1;
	}

	/** Set the quaternion coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void set(double x, double y, double z, double w) {
		double mag = (double)(1.0/Math.sqrt( x*x + y*y + z*z + w*w ));
		this.x = x*mag;
		this.y = y*mag;
		this.z = z*mag;
		this.w = w*mag;
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
	public final void setAxisAngle(Vector3D axis, double angle) {
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
	public final void setAxisAngle(double x, double y, double z, double angle) {
		double mag,amag;
		// Quat = cos(theta/2) + sin(theta/2)(roation_axis) 
		amag = (double)Math.sqrt(x*x + y*y + z*z);
		if (amag < EPS ) {
			this.w = 0.0f;
			this.x = 0.0f;
			this.y = 0.0f;
			this.z = 0.0f;
		}
		else {  
			amag = 1.0f/amag; 
			mag = (double)Math.sin(angle/2.0);
			this.w = (double)Math.cos(angle/2.0);
			this.x = x*amag*mag;
			this.y = y*amag*mag;
			this.z = z*amag*mag;
		}
	}

	/** Replies the rotation axis represented by this quaternion.
	 * 
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, double)
	 * @see #setAxisAngle(double, double, double, double)
	 * @see #getAngle()
	 */
	public final Vector3f getAxis() {
		double mag = this.x*this.x + this.y*this.y + this.z*this.z;  

		if ( mag > EPS ) {
			mag = (double)Math.sqrt(mag);
			double invMag = 1f/mag;

			return new Vector3f(
					this.x*invMag,
					this.y*invMag,
					this.z*invMag);
		}
		return new Vector3f(0f, 0f, 1f);
	}

	/** Replies the rotation angle represented by this quaternion.
	 * 
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, double)
	 * @see #setAxisAngle(double, double, double, double)
	 * @see #getAxis()
	 */
	public final double getAngle() {
		double mag = this.x*this.x + this.y*this.y + this.z*this.z;  

		if ( mag > EPS ) {
			mag = (double)Math.sqrt(mag);
			return (2.f*(double)Math.atan2(mag, this.w)); 
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
	public final void interpolate(Quaternion q1, double alpha) {
		// From "Advanced Animation and Rendering Techniques"
		// by Watt and Watt pg. 364, function as implemented appeared to be 
		// incorrect.  Fails to choose the same quaternion for the double
		// covering. Resulting in change of direction for rotations.
		// Fixed function to negate the first quaternion in the case that the
		// dot product of q1 and this is negative. Second case was not needed. 

		double dot,s1,s2,om,sinom;

		dot = this.x*q1.x + this.y*q1.y + this.z*q1.z + this.w*q1.w;

		if ( dot < 0 ) {
			// negate quaternion
			q1.x = -q1.x;  q1.y = -q1.y;  q1.z = -q1.z;  q1.w = -q1.w;
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

		this.w = (double)(s1*this.w + s2*q1.w);
		this.x = (double)(s1*this.x + s2*q1.x);
		this.y = (double)(s1*this.y + s2*q1.y);
		this.z = (double)(s1*this.z + s2*q1.z);
	}



	/** 
	 *  Performs a great circle interpolation between quaternion q1
	 *  and quaternion q2 and places the result into this quaternion. 
	 *  @param q1  the first quaternion
	 *  @param q2  the second quaternion
	 *  @param alpha  the alpha interpolation parameter 
	 */   
	public final void interpolate(Quaternion q1, Quaternion q2, double alpha) { 
		// From "Advanced Animation and Rendering Techniques"
		// by Watt and Watt pg. 364, function as implemented appeared to be 
		// incorrect.  Fails to choose the same quaternion for the double
		// covering. Resulting in change of direction for rotations.
		// Fixed function to negate the first quaternion in the case that the
		// dot product of q1 and this is negative. Second case was not needed. 

		double dot,s1,s2,om,sinom;

		dot = q2.x*q1.x + q2.y*q1.y + q2.z*q1.z + q2.w*q1.w;

		if ( dot < 0 ) {
			// negate quaternion
			q1.x = -q1.x;  q1.y = -q1.y;  q1.z = -q1.z;  q1.w = -q1.w;
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
		this.w = (double)(s1*q1.w + s2*q2.w);
		this.x = (double)(s1*q1.x + s2*q2.x);
		this.y = (double)(s1*q1.y + s2*q2.y);
		this.z = (double)(s1*q1.z + s2*q2.z);
	}
}
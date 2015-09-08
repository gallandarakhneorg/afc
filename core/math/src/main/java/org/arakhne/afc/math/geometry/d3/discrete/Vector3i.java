/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d3.discrete;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.continuous.Quaternion;
import org.arakhne.afc.math.geometry.d3.continuous.Transform3D;

/** 3D Vector with 3 integers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Vector3i extends Tuple3i<Vector3D> implements Vector3D {

	private static final long serialVersionUID = 1997599488590527335L;

	/**
	 */
	public Vector3i() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3i(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3i(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3i(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3i(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3i(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3i(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3i(long x, long y, long z) {
		super(x,y,z);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector3i clone() {
		return (Vector3i)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double angle(Vector3D v1) {
		double vDot = dot(v1) / ( length()*v1.length() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return((Math.acos( vDot )));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double dot(Vector3D v1) {
	      return (this.x*v1.getX() + this.y*v1.getY() + this.z*v1.getZ());
	}

	@Override
	public double perp(Vector3D v) {
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
		return getX()*v.getY() + getY()*v.getZ() + getZ()*v.getX() - v.getY()*getZ() - v.getZ()*getX() - v.getX()*getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double length() {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double lengthSquared() {
        return (this.x*this.x + this.y*this.y + this.z*this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize(Vector3D v1) {
        double norm;
        norm = 1./Math.sqrt(v1.getX()*v1.getX() + v1.getY()*v1.getY() + v1.getZ()*v1.getZ());
        this.x = (int)(v1.getX()*norm);
        this.y = (int)(v1.getY()*norm);
        this.z = (int)(v1.getZ()*norm);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize() {
        double norm;
        norm = 1./Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
        this.x *= norm;
        this.y *= norm;
        this.z *= norm;
	}

	@Override
	public void add(Vector3D t1, Vector3D t2) {
		this.x = (int)(t1.getX() + t2.getX());
		this.y = (int)(t1.getY() + t2.getY());
		this.z = (int)(t1.getZ() + t2.getZ());
	}

	@Override
	public void add(Vector3D t1) {
		this.x = (int)(this.x + t1.getX());
		this.y = (int)(this.y + t1.getY());
		this.z = (int)(this.z + t1.getZ());
	}

	@Override
	public void scaleAdd(int s, Vector3D t1, Vector3D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
		this.z = (int)(s * t1.getZ() + t2.getZ());
	}

	@Override
	public void scaleAdd(double s, Vector3D t1, Vector3D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
		this.z = (int)(s * t1.getZ() + t2.getZ());
	}

	@Override
	public void scaleAdd(int s, Vector3D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
		this.z = (int)(s * this.z + t1.getZ());
	}

	@Override
	public void scaleAdd(double s, Vector3D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
		this.z = (int)(s * this.z + t1.getZ());
	}

	@Override
	public void sub(Vector3D t1, Vector3D t2) {
		this.x = (int)(t1.getX() - t2.getX());
		this.y = (int)(t1.getY() - t2.getY());
		this.z = (int)(t1.getZ() - t2.getZ());
	}

	@Override
	public void sub(Point3D t1, Point3D t2) {
		this.x = (int)(t1.getX() - t2.getX());
		this.y = (int)(t1.getY() - t2.getY());
		this.z = (int)(t1.getZ() - t2.getZ());
	}

	@Override
	public void sub(Vector3D t1) {
		this.x = (int)(this.x - t1.getX());
		this.y = (int)(this.y - t1.getY());
		this.z = (int)(this.z - t1.getZ());
	}

	@Override
	public Vector3D cross(Vector3D v1) {
		return crossLeftHand(v1);
	}

	@Override
	public void cross(Vector3D v1, Vector3D v2) {
		crossLeftHand(v1, v2);
	}

	@Override
	public Vector3D crossLeftHand(Vector3D v1) {
		double x = v1.getY()*getZ() - v1.getZ()*getY();
		double y = v1.getZ()*getX() - v1.getX()*getZ();
		double z = v1.getX()*getY() - v1.getY()*getX();
		return new Vector3i(x,y,z);
	}

	@Override
	public void crossLeftHand(Vector3D v1, Vector3D v2) {
		double x = v2.getY()*v1.getZ() - v2.getZ()*v1.getY();
		double y = v2.getZ()*v1.getX() - v2.getX()*v1.getZ();
		double z = v2.getX()*v1.getY() - v2.getY()*v1.getX();
		set(x,y,z);
	}
	
	@Override
	public Vector3D crossRightHand(Vector3D v1) {
		double x = getY()*v1.getZ() - getZ()*v1.getY();
		double y = getZ()*v1.getX() - getX()*v1.getZ();
		double z = getX()*v1.getY() - getY()*v1.getX();
		return new Vector3i(x,y,z);
	}

	@Override
	public void crossRightHand(Vector3D v1, Vector3D v2) {
		double x = v1.getY()*v2.getZ() - v1.getZ()*v2.getY();
		double y = v1.getZ()*v2.getX() - v1.getX()*v2.getZ();
		double z = v1.getX()*v2.getY() - v1.getY()*v2.getX();
		set(x,y,z);
	}

	@Override
	public void turnVector(Vector3D axis, double angle) {
		Transform3D mat = new Transform3D();
		mat.setRotation(new Quaternion(axis, angle));
		mat.transform(this);
	}

	@Override
	public boolean isUnitVector() {
		return MathUtil.isEpsilonEqual(lengthSquared(), 1.);
	}

	@Override
	public boolean isColinear(Vector3D v) {
		int cx = iy() * v.iz() - iz() * v.iy();
		int cy = iz() * v.ix() - ix() * v.iz();
		int cz = ix() * v.iy() - iy() * v.ix();
		int sum = cx * cx + cy * cy + cz * cz;
		return sum == 0;
	}

	@Override
	public void setLength(double newLength) {
		double nl = Math.max(0, newLength);
		double l = length();
		if (l != 0) {
			double f = nl / l;
			this.x *= f;
			this.y *= f;
			this.z *= f;
		} else {
			this.x = (int) newLength;
			this.y = 0;
			this.z = 0;
		}
	}

	@Override
	public Vector3D toUnmodifiable() {
		return new UnmodifiableVector3i();
	}

	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiableVector3i implements Vector3D {
		
		private static final long serialVersionUID = 6113750458070037483L;

		public UnmodifiableVector3i() {
			//
		}

		@Override
		public Vector3D clone() {
			try {
				return (Vector3D) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		@Override
		public void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void absolute(Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(double x, double y, double z) {
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
		public void addZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addZ(double z) {
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
		public void clamp(int min, int max, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clamp(double min, double max, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(int min, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(double min, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(int max, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(double max, Vector3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void get(Vector3D t) {
			Vector3i.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Vector3i.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Vector3i.this.get(t);
		}

		@Override
		public void negate(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(int s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(double s, Vector3D t1) {
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
		public void set(Tuple3D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(double x, double y, double z) {
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
			return Vector3i.this.getX();
		}

		@Override
		public int ix() {
			return Vector3i.this.ix();
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
			return Vector3i.this.getY();
		}

		@Override
		public int iy() {
			return Vector3i.this.iy();
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
		public double getZ() {
			return Vector3i.this.getZ();
		}

		@Override
		public int iz() {
			return Vector3i.this.iz();
		}

		@Override
		public void setZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(double x, double y, double z) {
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
		public void subZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void subZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void interpolate(Vector3D t1, Vector3D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void interpolate(Vector3D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Tuple3D<?> t1) {
			return Vector3i.this.equals(t1);
		}
		
		@Override
		public int hashCode() {
			return Vector3i.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Vector3D t1, double epsilon) {
			return Vector3i.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public void add(Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Vector3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Point3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double dot(Vector3D v1) {
			return Vector3i.this.dot(v1);
		}

		@Override
		public Vector3D cross(Vector3D v1) {
			return Vector3i.this.cross(v1);
		}

		@Override
		public void cross(Vector3D v1, Vector3D v2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector3D crossLeftHand(Vector3D v1) {
			return Vector3i.this.crossLeftHand(v1);
		}

		@Override
		public void crossLeftHand(Vector3D v1, Vector3D v2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector3D crossRightHand(Vector3D v1) {
			return Vector3i.this.crossRightHand(v1);
		}

		@Override
		public void crossRightHand(Vector3D v1, Vector3D v2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double length() {
			return Vector3i.this.length();
		}

		@Override
		public double lengthSquared() {
			return Vector3i.this.lengthSquared();
		}

		@Override
		public void normalize(Vector3D v1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void normalize() {
			throw new UnsupportedOperationException();
		}

		@Override
		public double angle(Vector3D v1) {
			return Vector3i.this.angle(v1);
		}

		@Override
		public void turnVector(Vector3D axis, double angle) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isUnitVector() {
			return Vector3i.this.isUnitVector();
		}

		@Override
		public void setLength(double newLength) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector3D toUnmodifiable() {
			return this;
		}

		@Override
		public double perp(Vector3D v) {
			return Vector3i.this.perp(v);
		}

		@Override
		public boolean isColinear(Vector3D v) {
			return Vector3i.this.isColinear(v);
		}
		
	}

}
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
package org.arakhne.afc.math.geometry.d2.discrete;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.matrix.Matrix2f;

/** 2D Vector with 2 integers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Vector2i extends Tuple2i<Vector2D> implements Vector2D {

	private static final long serialVersionUID = -4528846627184370639L;

	/**
	 */
	public Vector2i() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2i(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2i(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2i(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2i(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2i(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2i(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2i(long x, long y) {
		super(x,y);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2i clone() {
		return (Vector2i)super.clone();
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
	      return (this.x*v1.getX() + this.y*v1.getY());
	}

	@Override
	public double perp(Vector2D x2) {
		return getX()*x2.getY() - x2.getX()*getY();
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
	public double lengthSquared() {
        return (this.x*this.x + this.y*this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize(Vector2D v1) {
        double norm;
        norm = 1./Math.sqrt(v1.getX()*v1.getX() + v1.getY()*v1.getY());
        this.x = (int)(v1.getX()*norm);
        this.y = (int)(v1.getY()*norm);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize() {
        double norm;
        norm = 1./Math.sqrt(this.x*this.x + this.y*this.y);
        this.x *= norm;
        this.y *= norm;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double signedAngle(Vector2D v) {
		assert(v!=null);
		Vector2i a = new Vector2i(this);
		if (a.length()==0) return Double.NaN;
		Vector2i b = new Vector2i(v);
		if (b.length()==0) return Double.NaN;
		a.normalize();
		b.normalize();
		
		double cos = a.getX() * b.getX() + a.getY() * b.getY();
		// A x B = |A|.|B|.sin(theta).N = sin(theta) (where N is the unit vector perpendicular to plane AB)
		double sin = a.getX()*b.getY() - a.getY()*b.getX();
		
		double angle = Math.atan2(sin, cos);

		return angle;
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

	@Override
	public void add(Vector2D t1, Vector2D t2) {
		this.x = (int)(t1.getX() + t2.getX());
		this.y = (int)(t1.getY() + t2.getY());
	}

	@Override
	public void add(Vector2D t1) {
		this.x = (int)(this.x + t1.getX());
		this.y = (int)(this.y + t1.getY());
	}

	@Override
	public void scaleAdd(int s, Vector2D t1, Vector2D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
	}

	@Override
	public void scaleAdd(double s, Vector2D t1, Vector2D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
	}

	@Override
	public void scaleAdd(int s, Vector2D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
	}

	@Override
	public void scaleAdd(double s, Vector2D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
	}

	@Override
	public void sub(Vector2D t1, Vector2D t2) {
		this.x = (int)(t1.getX() - t2.getX());
		this.y = (int)(t1.getY() - t2.getY());
	}

	@Override
	public void sub(Point2D t1, Point2D t2) {
		this.x = (int)(t1.getX() - t2.getX());
		this.y = (int)(t1.getY() - t2.getY());
	}

	@Override
	public void sub(Vector2D t1) {
		this.x = (int)(this.x - t1.getX());
		this.y = (int)(this.y - t1.getY());
	}

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 * 
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	public static Vector2i toOrientationVector(double angle) {
		return new Vector2i(
				Math.cos(angle),
				Math.sin(angle));
	}
	
	@Override
	public double getOrientationAngle() {
		double angle = Math.acos(getX());
		if (getY()<0f) angle = -angle;
		return MathUtil.clampCyclic(angle, 0., MathConstants.TWO_PI);
	}

	@Override
	public void perpendicularize() {
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), right-handed
		//set(y(), -x());
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), left-handed
		set(-iy(), ix());
	}

	@Override
	public boolean isUnitVector() {
		return MathUtil.isEpsilonEqual(lengthSquared(), 1.);
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
			this.x = (int) newLength;
			this.y = 0;
		}
	}

	@Override
	public Vector2D toUnmodifiable() {
		return new UnmodifiableVector2i();
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiableVector2i implements Vector2D {

		private static final long serialVersionUID = -8670105082548919880L;

		/**
		 */
		public UnmodifiableVector2i() {
			//
		}

		@Override
		public UnmodifiableVector2i clone() {
			return new UnmodifiableVector2i();
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
			Vector2i.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Vector2i.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Vector2i.this.get(t);
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
			return Vector2i.this.getX();
		}

		@Override
		public int ix() {
			return Vector2i.this.ix();
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
			return Vector2i.this.getY();
		}

		@Override
		public int iy() {
			return Vector2i.this.iy();
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
			return Vector2i.this.equals(t1);
		}

		@Override
		public int hashCode() {
			return Vector2i.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Vector2D t1, double epsilon) {
			return Vector2i.this.epsilonEquals(t1, epsilon);
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
			return Vector2i.this.dot(v1);
		}

		@Override
		public void perpendicularize() {
			throw new UnsupportedOperationException();
		}

		@Override
		public double length() {
			return Vector2i.this.length();
		}

		@Override
		public double lengthSquared() {
			return Vector2i.this.lengthSquared();
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
			return Vector2i.this.angle(v1);
		}

		@Override
		public double signedAngle(Vector2D v) {
			return Vector2i.this.signedAngle(v);
		}

		@Override
		public void turnVector(double angle) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double getOrientationAngle() {
			return Vector2i.this.getOrientationAngle();
		}

		@Override
		public boolean isUnitVector() {
			return Vector2i.this.isUnitVector();
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
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
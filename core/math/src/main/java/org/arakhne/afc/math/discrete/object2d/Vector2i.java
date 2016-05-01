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
package org.arakhne.afc.math.discrete.object2d;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Tuple2D;
import org.arakhne.afc.math.generic.Vector2D;

/** 2D Vector with 2 integers.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.i.Vector2i}
 */
@Deprecated
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
	public Vector2i(float[] tuple) {
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
		super((float)x,(float)y);
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
	public float angle(Vector2D v1) {
		double vDot = dot(v1) / ( length()*v1.length() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return((float) (Math.acos( vDot )));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float dot(Vector2D v1) {
	      return (this.x*v1.getX() + this.y*v1.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float length() {
        return (float) Math.sqrt(this.x*this.x + this.y*this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float lengthSquared() {
        return (this.x*this.x + this.y*this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize(Vector2D v1) {
        float norm;
        norm = (float) (1./Math.sqrt(v1.getX()*v1.getX() + v1.getY()*v1.getY()));
        this.x = (int)(v1.getX()*norm);
        this.y = (int)(v1.getY()*norm);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize() {
        float norm;
        norm = (float)(1./Math.sqrt(this.x*this.x + this.y*this.y));
        this.x *= norm;
        this.y *= norm;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float signedAngle(Vector2D v) {
		assert(v!=null);
		Vector2f a = new Vector2f(this);
		if (a.length()==0) return Float.NaN;
		Vector2f b = new Vector2f(v);
		if (b.length()==0) return Float.NaN;
		a.normalize();
		b.normalize();
		
		float cos = a.getX() * b.getX() + a.getY() * b.getY();
		// A x B = |A|.|B|.sin(theta).N = sin(theta) (where N is the unit vector perpendicular to plane AB)
		float sin = a.getX()*b.getY() - a.getY()*b.getX();
		
		float angle = (float)Math.atan2(sin, cos);

		return angle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnVector(float angle) {
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		float x =  cos * getX() + sin * getY(); 
		float y = -sin * getX() + cos * getY();
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
	public void scaleAdd(float s, Vector2D t1, Vector2D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
	}

	@Override
	public void scaleAdd(int s, Vector2D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
	}

	@Override
	public void scaleAdd(float s, Vector2D t1) {
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
	public static Vector2i toOrientationVector(float angle) {
		return new Vector2i(
				(float)Math.cos(angle),
				(float)Math.sin(angle));
	}
	
	@Override
	public float getOrientationAngle() {
		float angle = (float)Math.acos(getX());
		if (getY()<0f) angle = -angle;
		return (float) MathUtil.clampCyclic(angle, 0, MathConstants.TWO_PI);
	}

	@Override
	public void perpendicularize() {
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), right-handed
		//set(y(), -x());
		// Based on the cross product in 3D of (vx,vy,0)x(0,0,1), left-handed
		set(-y(), x());
	}

}
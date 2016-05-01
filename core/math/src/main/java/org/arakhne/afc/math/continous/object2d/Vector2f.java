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
package org.arakhne.afc.math.continous.object2d;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Tuple2D;
import org.arakhne.afc.math.generic.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.matrix.Matrix2f;

/** 2D Vector with 2 floating-point values.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link Vector2d}
 */
@Deprecated
public class Vector2f extends Tuple2f<Vector2D> implements Vector2D {

	private static final long serialVersionUID = -2062941509400877679L;

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
	public Vector2f(float[] tuple) {
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
		super((float)x,(float)y);
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
	 * Multiply this vector, transposed, by the given matrix and replies the resulting vector.
	 * 
	 * @param m
	 * @return transpose(this * m)
	 */
	public final Vector2f mul(Matrix2f m) {
		Vector2f r = new Vector2f();
		r.x = (float) (this.getX() * m.getM00() + this.getY() * m.getM01());
		r.y = (float) (this.getX() * m.getM10() + this.getY() * m.getM11());
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
        float norm = 1f / v1.length();
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
		return (float) org.arakhne.afc.math.geometry.d2.Vector2D.signedAngle(getX(), getY(), v.getX(), v.getY());
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
	
	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 * 
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	public static Vector2f toOrientationVector(float angle) {
		return new Vector2f(
				(float)Math.cos(angle),
				(float)Math.sin(angle));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float getOrientationAngle() {
		float angle = (float)Math.acos(getX());
		if (getY()<0f) angle = -angle;
		return (float) MathUtil.clampCyclic(angle, 0, MathConstants.TWO_PI);
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
	public void scaleAdd(float s, Vector2D t1, Vector2D t2) {
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
	public void scaleAdd(float s, Vector2D t1) {
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

}
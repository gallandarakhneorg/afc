/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.continous.object3d;

import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.math.generic.Tuple3D;
import org.arakhne.afc.math.generic.Vector3D;
import org.arakhne.afc.math.matrix.Matrix3d;
import org.arakhne.afc.math.matrix.Transform3D;

/** 3D Vector with 3 floating-point values.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated Replacement will be provided in Version 14.0
 */
@Deprecated
@SuppressWarnings("all")
public class Vector3f extends Tuple3f<Vector3D> implements Vector3D {

	private static final long serialVersionUID = -1222875298451525734L;

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
	public Vector3f(float[] tuple) {
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
		super((float)x,(float)y,(float)z);
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
	@Override
	public Vector3f clone() {
		return (Vector3f)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float angle(Vector3D v1) {
		double vDot = dot(v1) / ( length()*v1.length() );
		if( vDot < -1.) vDot = -1.;
		if( vDot >  1.) vDot =  1.;
		return((float) (Math.acos( vDot )));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float dot(Vector3D v1) {
		return (this.x*v1.getX() + this.y*v1.getY() + this.z*v1.getZ());
	}

	/**
	 * Multiply this vector, transposed, by the given matrix and replies the resulting vector.
	 * 
	 * @param m
	 * @return transpose(this * m)
	 */
	public final Vector3f mul(Matrix3d m) {
		Vector3f r = new Vector3f();
		r.x = (float)(this.getX() * m.getM00() + this.getY() * m.getM01() + this.getZ() * m.getM02());
		r.y = (float)(this.getX() * m.getM10() + this.getY() * m.getM11() + this.getZ() * m.getM12());
		r.z = (float)(this.getX() * m.getM20() + this.getY() * m.getM21() + this.getZ() * m.getM22());
		return r;
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
		float x = v1.getY()*getZ() - v1.getZ()*getY();
		float y = v1.getZ()*getX() - v1.getX()*getZ();
		float z = v1.getX()*getY() - v1.getY()*getX();
		return new Vector3f(x,y,z);
	}

	@Override
	public void crossLeftHand(Vector3D v1, Vector3D v2) {
		float x = v2.getY()*v1.getZ() - v2.getZ()*v1.getY();
		float y = v2.getZ()*v1.getX() - v2.getX()*v1.getZ();
		float z = v2.getX()*v1.getY() - v2.getY()*v1.getX();
		set(x,y,z);
	}

	@Override
	public Vector3D crossRightHand(Vector3D v1) {
		float x = getY()*v1.getZ() - getZ()*v1.getY();
		float y = getZ()*v1.getX() - getX()*v1.getZ();
		float z = getX()*v1.getY() - getY()*v1.getX();
		return new Vector3f(x,y,z);
	}

	@Override
	public void crossRightHand(Vector3D v1, Vector3D v2) {
		float x = v1.getY()*v2.getZ() - v1.getZ()*v2.getY();
		float y = v1.getZ()*v2.getX() - v1.getX()*v2.getZ();
		float z = v1.getX()*v2.getY() - v1.getY()*v2.getX();
		set(x,y,z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float length() {
        return (float) Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float lengthSquared() {
        return (this.x*this.x + this.y*this.y + this.z*this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize(Vector3D v1) {
        float norm = 1f / v1.length();
        this.x = (int)(v1.getX()*norm);
        this.y = (int)(v1.getY()*norm);
        this.z = (int)(v1.getZ()*norm);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void normalize() {
        float norm;
        norm = (float)(1./Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z));
        this.x *= norm;
        this.y *= norm;
        this.z *= norm;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnVector(Vector3D axis, float angle) {
		Transform3D mat = new Transform3D();
		mat.setRotation(new Quaternion(axis, angle));
		mat.transform(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Vector3D t1, Vector3D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
		this.z = t1.getZ() + t2.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Vector3D t1) {
		this.x += t1.getX();
		this.y += t1.getY();
		this.z += t1.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(int s, Vector3D t1, Vector3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(float s, Vector3D t1, Vector3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(int s, Vector3D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
		this.z = s * this.z + t1.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scaleAdd(float s, Vector3D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
		this.z = s * this.z + t1.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(Vector3D t1, Vector3D t2) {
		this.x = t1.getX() - t2.getX();
		this.y = t1.getY() - t2.getY();
		this.z = t1.getZ() - t2.getZ();
	}

	@Override
	public void sub(Point3D t1, Point3D t2) {
		this.x = t1.getX() - t2.getX();
		this.y = t1.getY() - t2.getY();
		this.z = t1.getZ() - t2.getZ();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(Vector3D t1) {
		this.x -= t1.getX();
		this.y -= t1.getY();
		this.z -= t1.getZ();
	}

}

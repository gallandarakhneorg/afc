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

package org.arakhne.afc.math.discrete.object3d;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.math.generic.Tuple3D;
import org.arakhne.afc.math.generic.Vector3D;

/** 3D Point with 3 integers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated Replacement will be provided in Version 14.0
 */
@Deprecated
@SuppressWarnings("all")
public class Point3i extends Tuple3i<Point3D> implements Point3D {

	private static final long serialVersionUID = -1506750779625667216L;

	/**
	 */
	public Point3i() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3i(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3i(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3i(float[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3i(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3i(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3i(double x, double y, double z) {
		super((float)x,(float)y,(float)z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3i(long x, long y, long z) {
		super(x,y,z);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3i clone() {
		return (Point3i)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceSquared(Point3D p1) {
	      float dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (int)(dx*dx+dy*dy+dz*dz);
	}

	@Override
	public float getDistanceSquared(Point3D p1) {
	      float dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (dx*dx+dy*dy+dz*dz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distance(Point3D p1) {
	      float  dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (int)Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	@Override
	public float getDistance(Point3D p1) {
	      float  dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceL1(Point3D p1) {
	      return (int)(Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY())  + Math.abs(this.z-p1.getZ()));
	}

	@Override
	public float getDistanceL1(Point3D p1) {
	      return Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY())  + Math.abs(this.z-p1.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceLinf(Point3D p1) {
	      return (int)(MathUtil.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY()), Math.abs(this.z-p1.getZ())));
	}

	@Override
	public float getDistanceLinf(Point3D p1) {
	      return (MathUtil.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY()), Math.abs(this.z-p1.getZ())));
	}

	@Override
	public void add(Point3D t1, Vector3D t2) {
		this.x = (int)(t1.getX() + t2.getX());
		this.y = (int)(t1.getY() + t2.getY());
		this.z = (int)(t1.getZ() + t2.getZ());
	}

	@Override
	public void add(Vector3D t1, Point3D t2) {
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
	public void scaleAdd(int s, Vector3D t1, Point3D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
		this.z = (int)(s * t1.getZ() + t2.getZ());
	}

	@Override
	public void scaleAdd(float s, Vector3D t1, Point3D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
		this.z = (int)(s * t1.getZ() + t2.getZ());
	}

	@Override
	public void scaleAdd(int s, Point3D t1, Vector3D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
		this.z = (int)(s * t1.getZ() + t2.getZ());
	}

	@Override
	public void scaleAdd(float s, Point3D t1, Vector3D t2) {
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
	public void scaleAdd(float s, Vector3D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
		this.z = (int)(s * this.z + t1.getZ());
	}

	@Override
	public void sub(Point3D t1, Vector3D t2) {
		this.x = (int)(t1.getX() - t1.getX());
		this.y = (int)(t1.getY() - t1.getY());
		this.z = (int)(t1.getZ() - t1.getZ());
	}

	@Override
	public void sub(Vector3D t1) {
		this.x = (int)(this.x - t1.getX());
		this.y = (int)(this.y - t1.getY());
		this.z = (int)(this.z - t1.getZ());
	}

}

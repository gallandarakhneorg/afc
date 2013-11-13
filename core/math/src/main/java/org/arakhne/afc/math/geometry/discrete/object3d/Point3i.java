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
package org.arakhne.afc.math.geometry.discrete.object3d;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.Point3D;
import org.arakhne.afc.math.geometry.Tuple3D;
import org.arakhne.afc.math.geometry.Vector3D;

/** 3D Point with 3 integers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
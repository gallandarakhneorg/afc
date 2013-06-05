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

import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.math.generic.Tuple3D;
import org.arakhne.afc.math.generic.Vector3D;

/** 3D Point with 3 floating-point numbers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Point3f extends Tuple3f<Point3D> implements Point3D {

	private static final long serialVersionUID = -4821663886493835147L;

	/**
	 */
	public Point3f() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3f(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3f(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3f(float[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(double x, double y, double z) {
		super((float)x,(float)y,(float)z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3f(long x, long y, long z) {
		super(x,y,z);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3f clone() {
		return (Point3f)super.clone();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getDistanceSquared(Point3D p1) {
	      float dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return dx*dx+dy*dy+dz*dz;
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

	/**
	 * {@inheritDoc}
	 */
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
	      return (int)(Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY()) + Math.abs(this.z-p1.getZ()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getDistanceL1(Point3D p1) {
	      return Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY() + Math.abs(this.z-p1.getZ()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceLinf(Point3D p1) {
	      return (int)Math.max(Math.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY())),
	    		  Math.abs(this.z-p1.getZ()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getDistanceLinf(Point3D p1) {
	      return Math.max( Math.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY())),
	    		  Math.abs(this.z-p1.getZ()));
	}

	@Override
	public void add(Point3D t1, Vector3D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
		this.z = t1.getZ() + t2.getZ();
	}

	@Override
	public void add(Vector3D t1, Point3D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
		this.z = t1.getZ() + t2.getZ();
	}

	@Override
	public void add(Vector3D t1) {
		this.x += t1.getX();
		this.y += t1.getY();
		this.z += t1.getZ();
	}
	
    /**  
     * Sets the value of this tuple to the vector sum of itself and tuple t1.
     * @param t1 the other tuple
     */  
    public final void add(Point3D t1)
    {
        this.x += t1.getX();
        this.y += t1.getY();
        this.z += t1.getZ();
    }

	@Override
	public void scaleAdd(int s, Vector3D t1, Point3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(float s, Vector3D t1, Point3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(int s, Point3D t1, Vector3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(float s, Point3D t1, Vector3D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
		this.z = s * t1.getZ() + t2.getZ();
	}

	@Override
	public void scaleAdd(int s, Vector3D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
		this.z = s * this.z + t1.getZ();
	}

	@Override
	public void scaleAdd(float s, Vector3D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
		this.z = s * this.z + t1.getZ();
	}

	@Override
	public void sub(Point3D t1, Vector3D t2) {
		this.x = t1.getX() - t2.getX();
		this.y = t1.getY() - t2.getY();
		this.z = t1.getZ() - t2.getZ();
	}

	@Override
	public void sub(Vector3D t1) {
		this.x -= t1.getX();
		this.y -= t1.getY();
		this.z -= t1.getZ();
	}
	
    public final void sub(Point3D t1)
    {
        this.x -= t1.getX();
        this.y -= t1.getY();
        this.z -= t1.getZ();
    }

}
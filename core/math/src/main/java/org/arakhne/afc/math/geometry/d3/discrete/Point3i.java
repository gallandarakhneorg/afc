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
	public Point3i(double[] tuple) {
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
		super(x,y,z);
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
	      double dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (int)(dx*dx+dy*dy+dz*dz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distance(Point3D p1) {
	      double  dx, dy, dz;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      dz = this.z-p1.getZ();
	      return (int)Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceL1(Point3D p1) {
	      return (int)(Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY())  + Math.abs(this.z-p1.getZ()));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceLinf(Point3D p1) {
	      return (int)(MathUtil.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY()), Math.abs(this.z-p1.getZ())));
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
	public void scaleAdd(double s, Vector3D t1, Point3D t2) {
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
	public void scaleAdd(double s, Point3D t1, Vector3D t2) {
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

	@Override
	public Point3D toUnmodifiable() {
		return new UnmodifiablePoint3i();
	}

	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiablePoint3i implements Point3D {
		
		private static final long serialVersionUID = -3357133685019699117L;

		public UnmodifiablePoint3i() {
			//
		}

		@Override
		public Point3D clone() {
			try {
				return (Point3D) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		@Override
		public void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void absolute(Point3D t) {
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
		public void clamp(int min, int max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clamp(double min, double max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(int min, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(double min, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(int max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(double max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void get(Point3D t) {
			Point3i.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Point3i.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Point3i.this.get(t);
		}

		@Override
		public void negate(Point3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(int s, Point3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(double s, Point3D t1) {
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
			return Point3i.this.getX();
		}

		@Override
		public int ix() {
			return Point3i.this.ix();
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
			return Point3i.this.getY();
		}

		@Override
		public int iy() {
			return Point3i.this.iy();
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
			return Point3i.this.getZ();
		}

		@Override
		public int iz() {
			return Point3i.this.iz();
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
		public void interpolate(Point3D t1, Point3D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void interpolate(Point3D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Tuple3D<?> t1) {
			return Point3i.this.equals(t1);
		}
		
		@Override
		public int hashCode() {
			return Point3i.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Point3D t1, double epsilon) {
			return Point3i.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public int distanceSquared(Point3D p1) {
			return Point3i.this.distanceSquared(p1);
		}

		@Override
		public int distance(Point3D p1) {
			return Point3i.this.distance(p1);
		}

		@Override
		public int distanceL1(Point3D p1) {
			return Point3i.this.distanceL1(p1);
		}

		@Override
		public int distanceLinf(Point3D p1) {
			return Point3i.this.distanceLinf(p1);
		}

		@Override
		public void add(Point3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(Vector3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Vector3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Vector3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Point3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Point3D t1, Vector3D t2) {
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
		public void sub(Point3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Point3D toUnmodifiable() {
			return this;
		}
		
	}

}
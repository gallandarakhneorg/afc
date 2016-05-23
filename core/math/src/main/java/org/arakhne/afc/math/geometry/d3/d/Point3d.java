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
package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D Point with 3 double precision floating-point numbers.
 * 
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3d extends Tuple3d<Point3d> implements Point3D<Point3d, Vector3d> {

	private static final long serialVersionUID = -8318943957531471869L;

	/**
	 */
	public Point3d() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3d(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3d(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3d(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3d(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3d(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3d(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3d(long x, long y, long z) {
		super(x,y,z);
	}
	
	@Override
	public GeomFactory3d getGeomFactory() {
		return GeomFactory3d.SINGLETON;
	}

	@Pure
	@Override
	public double getDistanceSquared(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - p.getX();  
		double dy = this.y - p.getY();
		double dz = this.z - p.getZ();
		return (dx*dx+dy*dy+dz*dz);
	}

	@Pure
	@Override
	public double getDistance(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - p.getX();  
		double dy = this.y - p.getY();
		double dz = this.z - p.getZ();
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	@Pure
	@Override
	public double getDistanceL1(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return (Math.abs(this.x-p.getX()) + Math.abs(this.y-p.getY()) + Math.abs(this.z-p.getZ()));
	}

	@Pure
	@Override
	public double getDistanceLinf(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return (MathUtil.max(Math.abs(this.x-p.getX()), Math.abs(this.y-p.getY()), Math.abs(this.z-p.getZ())));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point3D<?, ?> p) {
		return (int) getDistanceL1(p);
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point3D<?, ?> p) {
		return (int) getDistanceLinf(p);
	}

	@Override
	public void add(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = point.getX() + vector.getX();
		this.y = point.getY() + vector.getY();
		this.z = point.getZ() + vector.getZ();
	}

	@Override
	public void add(Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = vector.getX() + point.getX();
		this.y = vector.getY() + point.getY();
		this.z = vector.getZ() + point.getZ();
	}

	@Override
	public void add(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
		this.z = this.z + vector.getZ();
	}

	@Override
	public void scaleAdd(int s, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * vector.getX() + point.getX();
		this.y = s * vector.getY() + point.getY();
		this.z = s * vector.getZ() + point.getZ();
	}

	@Override
	public void scaleAdd(double s, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * vector.getX() + point.getX();
		this.y = s * vector.getY() + point.getY();
		this.z = s * vector.getZ() + point.getZ();
	}

	@Override
	public void scaleAdd(int s, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * point.getX() + vector.getX();
		this.y = s * point.getY() + vector.getY();
		this.z = s * point.getZ() + vector.getZ();
	}

	@Override
	public void scaleAdd(double s, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * point.getX() + vector.getX();
		this.y = s * point.getY() + vector.getY();
		this.z = s * point.getZ() + vector.getZ();
	}

	@Override
	public void scaleAdd(int s, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * this.x + vector.getX();
		this.y = s * this.y + vector.getY();
		this.z = s * this.z + vector.getZ();
	}

	@Override
	public void scaleAdd(double s, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * this.x + vector.getX();
		this.y = s * this.y + vector.getY();
		this.z = s * this.z + vector.getZ();
	}

	@Override
	public void sub(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = point.getX() - vector.getX();
		this.y = point.getY() - vector.getY();
		this.z = point.getZ() - vector.getZ();
	}

	@Override
	public void sub(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x - vector.getX();
		this.y = this.y - vector.getY();
		this.z = this.z - vector.getZ();
	}

	@Pure
	@Override
	public UnmodifiablePoint3D<Point3d, Vector3d> toUnmodifiable() {
		return new UnmodifiablePoint3D<Point3d, Vector3d>() {

			private static final long serialVersionUID = 3305735799920201356L;

			@Override
			public GeomFactory3D<Vector3d, Point3d> getGeomFactory() {
				return Point3d.this.getGeomFactory();
			}
			
			@Override
			public Point3d clone() {
				return Point3d.this.getGeomFactory().newPoint(Point3d.this.getX(), Point3d.this.getY(), Point3d.this.getZ());
			}

			@Override
			public double getX() {
				return Point3d.this.getX();
			}

			@Override
			public int ix() {
				return Point3d.this.ix();
			}
			
			@Override
			public double getY() {
				return Point3d.this.getY();
			}
			
			@Override
			public int iy() {
				return Point3d.this.iy();
			}

			@Override
			public double getZ() {
				return Point3d.this.getZ();
			}

			@Override
			public int iz() {
				return Point3d.this.iz();
			}

		};
	}

}

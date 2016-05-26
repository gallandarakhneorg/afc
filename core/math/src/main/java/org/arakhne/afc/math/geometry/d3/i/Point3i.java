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
package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Point with 2 integer numbers.
 * 
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3i extends Tuple3i<Point3i> implements Point3D<Point3i, Vector3i> {

	private static final long serialVersionUID = -4977158382149954525L;

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

	@Pure
	@Override
	public double getDistanceSquared(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - point.getX();  
		double dy = this.y - point.getY();
		double dz = this.z - point.getZ();
		return dx * dx + dy * dy + dz * dz;
	}

	@Pure
	@Override
	public double getDistance(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - point.getX();  
		double dy = this.y - point.getY();
		double dz = this.z - point.getZ();
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	@Pure
	@Override
	public double getDistanceL1(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return Math.abs(this.x - point.getX()) + Math.abs(this.y - point.getY()) + Math.abs(this.z - point.getZ());
	}

	@Pure
	@Override
	public double getDistanceLinf(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return MathUtil.max(Math.abs(this.x - point.getX()), Math.abs(this.y - point.getY()), Math.abs(this.z - point.getZ()));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return (int) Math.round(getDistanceL1(point));
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return (int) Math.round(getDistanceLinf(point));
	}

	@Override
	public void add(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(point.getX() + vector.getX());
		this.y = (int) Math.round(point.getY() + vector.getY());
	}

	@Override
	public void add(Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(vector.getX() + point.getX());
		this.y = (int) Math.round(vector.getY() + point.getY());
		this.z = (int) Math.round(vector.getZ() + point.getZ());
	}

	@Override
	public void add(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(this.x + vector.getX());
		this.y = (int) Math.round(this.y + vector.getY());
		this.z = (int) Math.round(this.z + vector.getZ());
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
		this.z = (int) Math.round(scale * vector.getZ() + point.getZ());
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
		this.z = (int) Math.round(scale * vector.getZ() + point.getZ());
	}

	@Override
	public void scaleAdd(int scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
		this.z = (int) Math.round(scale * point.getZ() + vector.getZ());
	}

	@Override
	public void scaleAdd(double scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
		this.z = (int) Math.round(scale * point.getZ() + vector.getZ());
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
		this.z = (int) Math.round(scale * this.z + vector.getZ());
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
		this.z = (int) Math.round(scale * this.z + vector.getZ());
	}

	@Override
	public void sub(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(point.getX() - vector.getX());
		this.y = (int) Math.round(point.getY() - vector.getY());
		this.z = (int) Math.round(point.getZ() - vector.getZ());
	}

	@Override
	public void sub(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(this.x - vector.getX());
		this.y = (int) Math.round(this.y - vector.getY());
		this.z = (int) Math.round(this.z - vector.getZ());
	}
	
	@Override
	public GeomFactory3i getGeomFactory() {
		return GeomFactory3i.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiablePoint3D<Point3i, Vector3i> toUnmodifiable() {
		return new UnmodifiablePoint3D<Point3i, Vector3i>() {

			private static final long serialVersionUID = -4844158582025788289L;

			@Override
			public GeomFactory3D<Vector3i, Point3i> getGeomFactory() {
				return Point3i.this.getGeomFactory();
			}
			
			@Override
			public Point3i clone() {
				return Point3i.this.getGeomFactory().newPoint(
						Point3i.this.ix(),
						Point3i.this.iy(),
						Point3i.this.iz());
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
			public double getY() {
				return Point3i.this.getY();
			}
			
			@Override
			public int iy() {
				return Point3i.this.iy();
			}

			@Override
			public double getZ() {
				return Point3i.this.getZ();
			}

			@Override
			public int iz() {
				return Point3i.this.iz();
			}

		};
	}

}

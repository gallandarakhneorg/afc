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

import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D Vector with 3 double precision floating-point numbers.
 * 
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector3d extends Tuple3d<Vector3d> implements Vector3D<Vector3d, Point3d> {

	private static final long serialVersionUID = 9183440606977893371L;

//	/** Replies the orientation vector, which is corresponding
//	 * to the given angle on a trigonometric circle.
//	 * 
//	 * @param angle is the angle in radians to translate.
//	 * @return the orientation vector which is corresponding to the given angle.
//	 */
//	@Pure
//	public static Vector3d toOrientationVector(double angle) {
//		return new Vector3d(Math.cos(angle), Math.sin(angle));
//	}

	/**
	 */
	public Vector3d() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3d(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3d(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3d(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Vector3d(int x, int y, int z) {
		super(x, y, z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Vector3d(float x, float y, float z) {
		super(x, y, z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Vector3d(double x, double y, double z) {
		super(x, y, z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Vector3d(long x, long y, long z) {
		super(x, y, z);
	}

	@Override
	public GeomFactory3d getGeomFactory() {
		return GeomFactory3d.SINGLETON;
	}
	
	@Pure
	@Override
	public double dot(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return this.x * vector.getX() + this.y * vector.getY() + this.z * vector.getZ();
	}

	@Pure
	@Override
	public double perp(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return this.x * vector.getY() + this.y * vector.getZ() + this.z * vector.getX() - this.z * vector.getY() - this.x * vector.getZ() - this.y * vector.getX();
	}

	@Pure
	@Override
	public double getLength() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	@Pure
	@Override
	public double getLengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	@Override
	public void add(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = vector1.getX() + vector2.getX();
		this.y = vector1.getY() + vector2.getY();
	}

	@Override
	public void add(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
	}

	@Override
	public void scaleAdd(int s, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = s * vector1.getX() + vector2.getX();
		this.y = s * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(double s, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = s * vector1.getX() + vector2.getX();
		this.y = s * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(int s, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * this.x + vector.getX();
		this.y = s * this.y + vector.getY();
	}

	@Override
	public void scaleAdd(double s, Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * this.x + vector.getX();
		this.y = s * this.y + vector.getY();
	}

	@Override
	public void sub(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = vector1.getX() - vector2.getX();
		this.y = vector1.getY() - vector2.getY();
	}

	@Override
	public void sub(Point3D<?, ?> point1, Point3D<?, ?> point2) {
		assert (point1 != null) : "First point must be not null"; //$NON-NLS-1$
		assert (point2 != null) : "Second point must be not null"; //$NON-NLS-1$
		this.x = point1.getX() - point2.getX();
		this.y = point1.getY() - point2.getY();
	}

	@Override
	public void sub(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x -= vector.getX();
		this.y -= vector.getY();
	}

	@Override
	public void setLength(double newLength) {
		assert (newLength >= 0.) : "New length must be positive or zero"; //$NON-NLS-1$
		double l = getLength();
		if (l != 0) {
			double f = newLength / l;
			this.x *= f;
			this.y *= f;
		} else {
			this.x = newLength;
			this.y = 0;
		}
	}
	
	@Override
	public Vector3d toUnitVector() {
		double length = getLength();
		if (length == 0.) {
			return getGeomFactory().newVector();
		}
		return getGeomFactory().newVector(getX() / length, getY() / length, getZ() / length);
	}


	@Pure
	@Override
	public UnmodifiableVector3D<Vector3d, Point3d> toUnmodifiable() {
		return new UnmodifiableVector3D<Vector3d, Point3d>() {

			private static final long serialVersionUID = 6848610371671516804L;
			
			@Override
			public GeomFactory3D<Vector3d, Point3d> getGeomFactory() {
				return Vector3d.this.getGeomFactory();
			}
			
			@Override
			public Vector3d toUnitVector() {
				return Vector3d.this.toUnitVector();
			}


			@Override
			public Vector3d clone() {
				return Vector3d.this.getGeomFactory().newVector(Vector3d.this.getX(), Vector3d.this.getY(), Vector3d.this.getZ());
			}

			@Override
			public double getX() {
				return Vector3d.this.getX();
			}

			@Override
			public int ix() {
				return Vector3d.this.ix();
			}
			
			@Override
			public double getY() {
				return Vector3d.this.getY();
			}
			
			@Override
			public int iy() {
				return Vector3d.this.iy();
			}

			@Override
			public double getZ() {
				return Vector3d.this.getZ();
			}

			@Override
			public int iz() {
				return Vector3d.this.iz();
			}

		};
	}

}
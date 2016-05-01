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
package org.arakhne.afc.math.geometry.d2.d;

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Vector with 2 double precision floating-point numbers.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector2d extends Tuple2d<Vector2d> implements Vector2D<Vector2d, Point2d> {

	private static final long serialVersionUID = 9183440606977893371L;

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 * 
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	public static Vector2d toOrientationVector(double angle) {
		return new Vector2d(Math.cos(angle), Math.sin(angle));
	}

	/**
	 */
	public Vector2d() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2d(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2d(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2d(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2d(int x, int y) {
		super(x, y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2d(float x, float y) {
		super(x, y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2d(double x, double y) {
		super(x, y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2d(long x, long y) {
		super(x, y);
	}

	@Override
	public GeomFactory2d getGeomFactory() {
		return GeomFactory2d.SINGLETON;
	}
	
	@Pure
	@Override
	public double dot(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return (this.x * vector.getX() + this.y * vector.getY());
	}

	@Pure
	@Override
	public double perp(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return this.x * vector.getY() - vector.getX() * this.y;
	}

	@Pure
	@Override
	public double getLength() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	@Pure
	@Override
	public double getLengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	@Override
	public void add(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = vector1.getX() + vector2.getX();
		this.y = vector1.getY() + vector2.getY();
	}

	@Override
	public void add(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = s * vector1.getX() + vector2.getX();
		this.y = s * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(double s, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = s * vector1.getX() + vector2.getX();
		this.y = s * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * this.x + vector.getX();
		this.y = s * this.y + vector.getY();
	}

	@Override
	public void scaleAdd(double s, Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * this.x + vector.getX();
		this.y = s * this.y + vector.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert (vector1 != null) : "First vector must be not null"; //$NON-NLS-1$
		assert (vector2 != null) : "Second vector must be not null"; //$NON-NLS-1$
		this.x = vector1.getX() - vector2.getX();
		this.y = vector1.getY() - vector2.getY();
	}

	@Override
	public void sub(Point2D<?, ?> point1, Point2D<?, ?> point2) {
		assert (point1 != null) : "First point must be not null"; //$NON-NLS-1$
		assert (point2 != null) : "Second point must be not null"; //$NON-NLS-1$
		this.x = point1.getX() - point2.getX();
		this.y = point1.getY() - point2.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector) {
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
	public Vector2d toUnitVector() {
		double length = getLength();
		if (length == 0.) {
			return new Vector2d();
		}
		return new Vector2d(getX() / length, getY() / length);
	}
	
	@Override
	public Vector2d toOrthogonalVector() {
		return new Vector2d(-getY(), getX());
	}

	@Pure
	@Override
	public UnmodifiableVector2D<Vector2d, Point2d> toUnmodifiable() {
		return new UnmodifiableVector2D<Vector2d, Point2d>() {

			private static final long serialVersionUID = 6848610371671516804L;
			
			@Override
			public GeomFactory<Vector2d, Point2d> getGeomFactory() {
				return Vector2d.this.getGeomFactory();
			}
			
			@Override
			public Vector2d toUnitVector() {
				return Vector2d.this.toUnitVector();
			}
			
			@Override
			public Vector2d toOrthogonalVector() {
				return Vector2d.this.toOrthogonalVector();
			}

			@Override
			public Vector2d clone() {
				return new Vector2d(Vector2d.this);
			}

			@Override
			public double getX() {
				return Vector2d.this.getX();
			}

			@Override
			public int ix() {
				return Vector2d.this.ix();
			}

			@Override
			public double getY() {
				return Vector2d.this.getY();
			}

			@Override
			public int iy() {
				return Vector2d.this.iy();
			}

		};
	}

}
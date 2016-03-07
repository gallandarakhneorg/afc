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
package org.arakhne.afc.math.geometry.d2.fp;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
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
public class Vector2fp extends Tuple2fp<Vector2D, Vector2fp> implements Vector2D {

	private static final long serialVersionUID = 9183440606977893371L;

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 * 
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	public static Vector2fp toOrientationVector(double angle) {
		return new Vector2fp(Math.cos(angle), Math.sin(angle));
	}

	/**
	 */
	public Vector2fp() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2fp(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2fp(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2fp(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fp(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fp(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fp(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fp(long x, long y) {
		super(x,y);
	}

	@Pure
	@Override
	public double dot(Vector2D v1) {
	      return (this.x * v1.getX() + this.y * v1.getY());
	}

	@Pure
	@Override
	public double perp(Vector2D x2) {
		return this.x * x2.getY() - x2.getX() * this.y;
	}

	@Pure
	@Override
	public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	@Pure
	@Override
	public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
	}

	@Override
	public void add(Vector2D t1, Vector2D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
	}

	@Override
	public void add(Vector2D t1) {
		this.x = this.x + t1.getX();
		this.y = this.y + t1.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D t1, Vector2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(double s, Vector2D t1, Vector2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
	}

	@Override
	public void scaleAdd(double s, Vector2D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
	}

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

	@Override
	public void sub(Vector2D t1) {
		this.x -= t1.getX();
		this.y -= t1.getY();
	}

	@Override
	public void setLength(double newLength) {
		double nl = Math.max(0, newLength);
		double l = length();
		if (l != 0) {
			double f = nl / l;
			this.x *= f;
			this.y *= f;
		} else {
			this.x = newLength;
			this.y = 0;
		}
	}

	@Pure
	@Override
	public Vector2D toUnmodifiable() {
		return new UnmodifiableVector2D() {

			private static final long serialVersionUID = 6848610371671516804L;

			@Override
			public Vector2D clone() {
				return Vector2fp.this.toUnmodifiable();
			}

			@Override
			public double getX() {
				return Vector2fp.this.getX();
			}

			@Override
			public int ix() {
				return Vector2fp.this.ix();
			}

			@Override
			public double getY() {
				return Vector2fp.this.getY();
			}

			@Override
			public int iy() {
				return Vector2fp.this.iy();
			}

		};
	}

}
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
package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Point with 2 integer numbers.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2i extends Tuple2i<Point2D, Point2i> implements Point2D {

	private static final long serialVersionUID = -4977158382149954525L;

	/**
	 */
	public Point2i() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2i(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2i(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2i(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2i(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2i(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2i(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2i(long x, long y) {
		super(x,y);
	}

	@Pure
	@Override
	public double getDistanceSquared(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - point.getX();  
		double dy = this.y - point.getY();
		return dx * dx + dy * dy;
	}

	@Pure
	@Override
	public double getDistance(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - point.getX();  
		double dy = this.y - point.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	@Pure
	@Override
	public double getDistanceL1(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return Math.abs(this.x - point.getX()) + Math.abs(this.y - point.getY());
	}

	@Pure
	@Override
	public double getDistanceLinf(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return Math.max( Math.abs(this.x - point.getX()), Math.abs(this.y - point.getY()));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return (int) Math.round(getDistanceL1(point));
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return (int) Math.round(getDistanceLinf(point));
	}

	@Override
	public void add(Point2D point, Vector2D vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(point.getX() + vector.getX());
		this.y = (int) Math.round(point.getY() + vector.getY());
	}

	@Override
	public void add(Vector2D vector, Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(vector.getX() + point.getX());
		this.y = (int) Math.round(vector.getY() + point.getY());
	}

	@Override
	public void add(Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(this.x + vector.getX());
		this.y = (int) Math.round(this.y + vector.getY());
	}

	@Override
	public void scaleAdd(int scale, Vector2D vector, Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
	}

	@Override
	public void scaleAdd(double scale, Vector2D vector, Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
	}

	@Override
	public void scaleAdd(int scale, Point2D point, Vector2D vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
	}

	@Override
	public void scaleAdd(double scale, Point2D point, Vector2D vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
	}

	@Override
	public void scaleAdd(int scale, Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
	}

	@Override
	public void scaleAdd(double scale, Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
	}

	@Override
	public void sub(Point2D point, Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(point.getX() - vector.getX());
		this.y = (int) Math.round(point.getY() - vector.getY());
	}

	@Override
	public void sub(Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = (int) Math.round(this.x - vector.getX());
		this.y = (int) Math.round(this.y - vector.getY());
	}

	@Pure
	@Override
	public Point2D toUnmodifiable() {
		return new UnmodifiablePoint2D() {

			private static final long serialVersionUID = -4844158582025788289L;

			@Override
			public Point2D clone() {
				return Point2i.this.toUnmodifiable();
			}

			@Override
			public double getX() {
				return Point2i.this.getX();
			}

			@Override
			public int ix() {
				return Point2i.this.ix();
			}

			@Override
			public double getY() {
				return Point2i.this.getY();
			}

			@Override
			public int iy() {
				return Point2i.this.ix();
			}

		};
	}

}
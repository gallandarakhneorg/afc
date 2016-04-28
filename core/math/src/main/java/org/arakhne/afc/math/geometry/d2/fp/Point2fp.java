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

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Point with 2 double precision floating-point numbers.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2fp extends Tuple2fp<Point2fp> implements Point2D<Point2fp, Vector2fp> {

	private static final long serialVersionUID = -8318943957531471869L;

	/**
	 */
	public Point2fp() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2fp(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2fp(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2fp(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fp(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fp(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fp(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fp(long x, long y) {
		super(x,y);
	}
	
	@Override
	public GeomFactory2fp getGeomFactory() {
		return GeomFactory2fp.SINGLETON;
	}

	@Pure
	@Override
	public double getDistanceSquared(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - p.getX();  
		double dy = this.y - p.getY();
		return (dx*dx+dy*dy);
	}

	@Pure
	@Override
	public double getDistance(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double dx = this.x - p.getX();  
		double dy = this.y - p.getY();
		return Math.sqrt(dx*dx+dy*dy);
	}

	@Pure
	@Override
	public double getDistanceL1(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return (Math.abs(this.x-p.getX()) + Math.abs(this.y-p.getY()));
	}

	@Pure
	@Override
	public double getDistanceLinf(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return (Math.max( Math.abs(this.x-p.getX()), Math.abs(this.y-p.getY())));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point2D<?, ?> p) {
		return (int) getDistanceL1(p);
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point2D<?, ?> p) {
		return (int) getDistanceLinf(p);
	}

	@Override
	public void add(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = point.getX() + vector.getX();
		this.y = point.getY() + vector.getY();
	}

	@Override
	public void add(Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = vector.getX() + point.getX();
		this.y = vector.getY() + point.getY();
	}

	@Override
	public void add(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * vector.getX() + point.getX();
		this.y = s * vector.getY() + point.getY();
	}

	@Override
	public void scaleAdd(double s, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * vector.getX() + point.getX();
		this.y = s * vector.getY() + point.getY();
	}

	@Override
	public void scaleAdd(int s, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * point.getX() + vector.getX();
		this.y = s * point.getY() + vector.getY();
	}

	@Override
	public void scaleAdd(double s, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = s * point.getX() + vector.getX();
		this.y = s * point.getY() + vector.getY();
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
	public void sub(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = point.getX() - vector.getX();
		this.y = point.getY() - vector.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x - vector.getX();
		this.y = this.y - vector.getY();
	}

	@Pure
	@Override
	public UnmodifiablePoint2D<Point2fp, Vector2fp> toUnmodifiable() {
		return new UnmodifiablePoint2D<Point2fp, Vector2fp>() {

			private static final long serialVersionUID = 3305735799920201356L;

			@Override
			public GeomFactory<Vector2fp, Point2fp> getGeomFactory() {
				return Point2fp.this.getGeomFactory();
			}
			
			@Override
			public Point2fp clone() {
				return new Point2fp(this);
			}

			@Override
			public double getX() {
				return Point2fp.this.getX();
			}

			@Override
			public int ix() {
				return Point2fp.this.ix();
			}

			@Override
			public double getY() {
				return Point2fp.this.getY();
			}

			@Override
			public int iy() {
				return Point2fp.this.ix();
			}

		};
	}

}
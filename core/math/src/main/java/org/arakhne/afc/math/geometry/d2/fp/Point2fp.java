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
public class Point2fp extends Tuple2fp<Point2D, Point2fp> implements Point2D {

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

	@Pure
	@Override
	public double getDistanceSquared(Point2D p1) {
	      double dx = this.x - p1.getX();  
	      double dy = this.y - p1.getY();
	      return (dx*dx+dy*dy);
	}

	@Pure
	@Override
	public double getDistance(Point2D p1) {
	      double dx = this.x - p1.getX();  
	      double dy = this.y - p1.getY();
	      return Math.sqrt(dx*dx+dy*dy);
	}

	@Pure
	@Override
	public double getDistanceL1(Point2D p1) {
	      return (Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY()));
	}

	@Pure
	@Override
	public double getDistanceLinf(Point2D p1) {
	      return (Math.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY())));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point2D p1) {
	      return (int) getDistanceL1(p1);
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point2D p1) {
	      return (int) getDistanceLinf(p1);
	}

	@Override
	public void add(Point2D t1, Vector2D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
	}

	@Override
	public void add(Vector2D t1, Point2D t2) {
		this.x = t1.getX() + t2.getX();
		this.y = t1.getY() + t2.getY();
	}

	@Override
	public void add(Vector2D t1) {
		this.x = this.x + t1.getX();
		this.y = this.y + t1.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D t1, Point2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(double s, Vector2D t1, Point2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(int s, Point2D t1, Vector2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(double s, Point2D t1, Vector2D t2) {
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
	public void sub(Point2D t1, Vector2D t2) {
		this.x = t1.getX() - t2.getX();
		this.y = t1.getY() - t2.getY();
	}

	@Override
	public void sub(Vector2D t1) {
		this.x = this.x - t1.getX();
		this.y = this.y - t1.getY();
	}

	@Pure
	@Override
	public Point2D toUnmodifiable() {
		return new UnmodifiablePoint2D() {

			private static final long serialVersionUID = 3305735799920201356L;

			@Override
			public Point2D clone() {
				return Point2fp.this.toUnmodifiable();
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
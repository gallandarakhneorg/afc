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
package org.arakhne.afc.math.geometry.d2.discrete;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D Point with 2 integers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("hiding")
public class Point2i extends Tuple2i<Point2D> implements Point2D {

	private static final long serialVersionUID = 6087683508168847436L;

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

	/** {@inheritDoc}
	 */
	@Override
	public Point2i clone() {
		return (Point2i)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDistanceSquared(Point2D p1) {
	      double dx, dy;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      return (dx*dx+dy*dy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDistance(Point2D p1) {
	      double  dx, dy;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      return Math.sqrt(dx*dx+dy*dy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDistanceL1(Point2D p1) {
	      return (Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDistanceLinf(Point2D p1) {
	      return (Math.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceSquared(Point2D p1) {
	      return (int) getDistanceSquared(p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distance(Point2D p1) {
	      return (int) getDistance(p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceL1(Point2D p1) {
	      return (int) getDistanceL1(p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int distanceLinf(Point2D p1) {
	      return (int) getDistanceLinf(p1);
	}

	@Override
	public void add(Point2D t1, Vector2D t2) {
		this.x = (int)(t1.getX() + t2.getX());
		this.y = (int)(t1.getY() + t2.getY());
	}

	@Override
	public void add(Vector2D t1, Point2D t2) {
		this.x = (int)(t1.getX() + t2.getX());
		this.y = (int)(t1.getY() + t2.getY());
	}

	@Override
	public void add(Vector2D t1) {
		this.x = (int)(this.x + t1.getX());
		this.y = (int)(this.y + t1.getY());
	}

	@Override
	public void scaleAdd(int s, Vector2D t1, Point2D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
	}

	@Override
	public void scaleAdd(double s, Vector2D t1, Point2D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
	}

	@Override
	public void scaleAdd(int s, Point2D t1, Vector2D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
	}

	@Override
	public void scaleAdd(double s, Point2D t1, Vector2D t2) {
		this.x = (int)(s * t1.getX() + t2.getX());
		this.y = (int)(s * t1.getY() + t2.getY());
	}

	@Override
	public void scaleAdd(int s, Vector2D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
	}

	@Override
	public void scaleAdd(double s, Vector2D t1) {
		this.x = (int)(s * this.x + t1.getX());
		this.y = (int)(s * this.y + t1.getY());
	}

	@Override
	public void sub(Point2D t1, Vector2D t2) {
		this.x = (int)(t1.getX() - t1.getX());
		this.y = (int)(t1.getY() - t1.getY());
	}

	@Override
	public void sub(Vector2D t1) {
		this.x = (int)(this.x - t1.getX());
		this.y = (int)(this.y - t1.getY());
	}

	@Override
	public Point2D toUnmodifiable() {
		return new UnmodifiablePoint2i();
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiablePoint2i implements Point2D {

		private static final long serialVersionUID = -8670105082548919880L;

		/**
		 */
		public UnmodifiablePoint2i() {
			//
		}
		
		@Override
		public UnmodifiablePoint2i clone() {
			return new UnmodifiablePoint2i();
		}

		@Override
		public void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void absolute(Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(double x, double y) {
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
		public void clamp(int min, int max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clamp(double min, double max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(int min, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMin(double min, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(int max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clampMax(double max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void get(Point2D t) {
			Point2i.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Point2i.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Point2i.this.get(t);
		}

		@Override
		public void negate(Point2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(int s, Point2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scale(double s, Point2D t1) {
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
		public void set(Tuple2D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(double x, double y) {
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
			return Point2i.this.getX();
		}

		@Override
		public int ix() {
			return Point2i.this.ix();
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
			return Point2i.this.getY();
		}

		@Override
		public int iy() {
			return Point2i.this.iy();
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
		public void sub(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(double x, double y) {
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
		public void interpolate(Point2D t1, Point2D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void interpolate(Point2D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Tuple2D<?> t1) {
			return Point2i.this.equals(t1);
		}
		
		@Override
		public int hashCode() {
			return Point2i.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Point2D t1, double epsilon) {
			return Point2i.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public double getDistanceSquared(Point2D p1) {
			return Point2i.this.getDistanceSquared(p1);
		}

		@Override
		public double getDistance(Point2D p1) {
			return Point2i.this.getDistance(p1);
		}

		@Override
		public double getDistanceL1(Point2D p1) {
			return Point2i.this.getDistanceL1(p1);
		}

		@Override
		public double getDistanceLinf(Point2D p1) {
			return Point2i.this.getDistanceLinf(p1);
		}

		@Override
		public void add(Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(int s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scaleAdd(double s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void sub(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Point2D toUnmodifiable() {
			return this;
		}

		@Override
		public int distanceSquared(Point2D p1) {
			return Point2i.this.distanceSquared(p1);
		}

		@Override
		public int distance(Point2D p1) {
			return Point2i.this.distance(p1);
		}

		@Override
		public int distanceL1(Point2D p1) {
			return Point2i.this.distanceL1(p1);
		}

		@Override
		public int distanceLinf(Point2D p1) {
			return Point2i.this.distanceLinf(p1);
		}
		
	}

}
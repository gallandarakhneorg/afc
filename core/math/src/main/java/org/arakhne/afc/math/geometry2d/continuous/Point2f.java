/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry2d.continuous;

import org.arakhne.afc.math.geometry2d.Point2D;
import org.arakhne.afc.math.geometry2d.Tuple2D;
import org.arakhne.afc.math.geometry2d.Vector2D;

/** 2D Point with 2 floating-point numbers.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Point2f extends Tuple2f<Point2D> implements Point2D {

	private static final long serialVersionUID = 8963319137253544821L;

	/**
	 */
	public Point2f() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2f(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2f(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2f(float[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2f(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2f(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2f(double x, double y) {
		super((float)x,(float)y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2f(long x, long y) {
		super(x,y);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f clone() {
		return (Point2f)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2D p1) {
	      float dx, dy;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      return (dx*dx+dy*dy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distance(Point2D p1) {
	      float  dx, dy;
	      dx = this.x-p1.getX();  
	      dy = this.y-p1.getY();
	      return (float)Math.sqrt(dx*dx+dy*dy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceL1(Point2D p1) {
	      return (Math.abs(this.x-p1.getX()) + Math.abs(this.y-p1.getY()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceLinf(Point2D p1) {
	      return Math.max( Math.abs(this.x-p1.getX()), Math.abs(this.y-p1.getY()));
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
		this.x += t1.getX();
		this.y += t1.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D t1, Point2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(float s, Vector2D t1, Point2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(int s, Point2D t1, Vector2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(float s, Point2D t1, Vector2D t2) {
		this.x = s * t1.getX() + t2.getX();
		this.y = s * t1.getY() + t2.getY();
	}

	@Override
	public void scaleAdd(int s, Vector2D t1) {
		this.x = s * this.x + t1.getX();
		this.y = s * this.y + t1.getY();
	}

	@Override
	public void scaleAdd(float s, Vector2D t1) {
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
		this.x -= t1.getX();
		this.y -= t1.getY();
	}

}
/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D circle with floating-point points.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Circle2f extends AbstractCircle2F<Circle2f> {

	private static final long serialVersionUID = -5535463117356287850L;



	/** X-coordinate of the circle center. */
	protected double cx;
	/** Y-coordinate of the circle center. */
	protected double cy;
	/** Radius of the circle center (must be always positive). */
	protected double radius;

	/**
	 */
	public Circle2f() {
		this.cx = 0f;
		this.cy = 0f;
		this.radius = 0f;
	}

	/**
	 * @param center
	 * @param radius1
	 */
	public Circle2f(Point2D center, double radius1) {
		set(center, radius1);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius1
	 */
	public Circle2f(double x, double y, double radius1) {
		set(x, y, radius1);
	}
	
	/** Construct a circle from a circle.
	 * @param c
	 */
	public Circle2f(Circle2f c) {
		this.cx = c.cx;
		this.cy = c.cy;
		this.radius = c.radius;
	}

	@Override
	public void clear() {
		this.cx = this.cy = 0f;
		this.radius = 0f;
	}

	/** Change the frame of the circle.
	 * 
	 * @param x
	 * @param y
	 * @param radius1
	 */
	public void set(double x, double y, double radius1) {
		this.cx = x;
		this.cy = y;
		this.radius = Math.abs(radius1);
	}

	/** Change the frame of te circle.
	 * 
	 * @param center
	 * @param radius1
	 */
	public void set(Point2D center, double radius1) {
		this.cx = center.getX();
		this.cy = center.getY();
		this.radius = Math.abs(radius1);
	}

	@Override
	public void set(Shape2F s) {
		if (s instanceof Circle2f) {
			Circle2f c = (Circle2f) s;
			set(c.getX(), c.getY(), c.getRadius());
		} else {
			Rectangle2f r = s.toBoundingBox();
			set(r.getCenterX(), r.getCenterY(),
					Math.min(r.getWidth(), r.getHeight()) / 2.);
		}
	}

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	@Pure
	public double getX() {
		return this.cx;
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	public double getY() {
		return this.cy;
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	public Point2f getCenter() {
		return new Point2f(this.cx, this.cy);
	}

	/** Change the center.
	 * 
	 * @param center
	 */
	public void setCenter(Point2D center) {
		this.cx = center.getX();
		this.cy = center.getY();
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCenter(double x, double y) {
		this.cx = x;
		this.cy = y;
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	@Pure
	public double getRadius() {
		return this.radius;
	}

	/** Set the radius.
	 * 
	 * @param radius1 is the radius.
	 */
	public void setRadius(double radius1) {
		this.radius = Math.abs(radius1);
	}



}
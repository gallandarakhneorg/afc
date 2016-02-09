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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author Hamza JAFFALI (hjaffali)
 *
 */

public class Circle2d extends AbstractCircle2F<Circle2d>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4084538191693161101L;


	/** X-coordinate of the circle center. */
	protected DoubleProperty cxProperty;
	/** Y-coordinate of the circle center. */
	protected DoubleProperty cyProperty;
	/** Radius of the circle center (must be always positive). */
	protected DoubleProperty radiusProperty;

	/**
	 */
	public Circle2d() {
		this.cxProperty = new SimpleDoubleProperty(0f);
		this.cyProperty = new SimpleDoubleProperty(0f);
		this.radiusProperty = new SimpleDoubleProperty(0f);
	}

	/**
	 * @param center
	 * @param radius1
	 */
	public Circle2d(Point2d center, double radius1) {
		this();
		this.set(center, radius1);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius1
	 */
	public Circle2d(double x, double y, double radius1) {
		this();
		this.set(x, y, radius1);
	}

	/** Construct a circle from a circle.
	 * @param c
	 */
	public Circle2d(Circle2d c) {
		this();
		this.set(c.getX(),c.getY(),c.getRadius());
	}

	@Override
	public void clear() {
		this.cxProperty.set(0f); 
		this.cyProperty.set(0f); 
		this.radiusProperty.set(0f); 
	}


	/** Change the frame of the circle.
	 * 
	 * @param x
	 * @param y
	 * @param radius1
	 */
	public void set(double x, double y, double radius1) {
		this.cxProperty.set(x);
		this.cyProperty.set(y);
		this.radiusProperty.set(Math.abs(radius1));
	}

	/** Change the frame of te circle, associating properties of point to the circle center.
	 * 
	 * When the point in parameter is changed, the Circle will change also.
	 * 
	 * @param center
	 * @param radius1
	 */
	public void set(Point2D center, double radius1) {
		this.cxProperty.set(center.getX());
		this.cyProperty.set(center.getY());
		this.radiusProperty.set(Math.abs(radius1));
	}
	
	/** Change the frame of te circle, associating properties of point to the circle center.
	 * 
	 * When the point in parameter is changed, the Circle will change also.
	 * 
	 * @param center
	 * @param radius1
	 */
	public void setProperties(Point2d center, double radius1) {
		this.cxProperty = center.xProperty;
		this.cyProperty = center.yProperty;
		this.radiusProperty.set(Math.abs(radius1));
	}
	
	/** Change the frame of te circle, associating properties of point to the circle center and radius.
	 * 
	 * When the point and radius in parameter are changed, the Circle will change also.
	 * 
	 * @param center
	 * @param radius1
	 */
	public void setProperties(Point2d center, DoubleProperty radius1) {
		this.cxProperty = center.xProperty;
		this.cyProperty = center.yProperty;
		this.radiusProperty = radius1;
	}

	@Override
	public void set(Shape2F s) {
		if (s instanceof Circle2d) {
			Circle2d c = (Circle2d) s;
			set(c.getCenter(), c.getRadius());
		} else {
			AbstractRectangle2F<?> r = s.toBoundingBox();
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
		return this.cxProperty.doubleValue();
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	public double getY() {
		return this.cyProperty.doubleValue();
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	public Point2d getCenter() {
		return new Point2d(this.cxProperty, this.cyProperty);
	}

	/** Change the center, associating properties of point to the circle center.
	 * 
	 * When the point in parameter is changed, the Circle will change also..
	 * 
	 * @param center
	 */
	public void setCenterProperties(Point2d center) {
		this.cxProperty = center.xProperty;
		this.cyProperty = center.yProperty;
	}
	
	/** Change the center.
	 * 
	 * @param center
	 */
	public void setCenter(Point2D center) {
		this.cxProperty.set(center.getX());
		this.cyProperty.set(center.getY());
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCenter(double x, double y) {
		this.cxProperty.set(x);
		this.cyProperty.set(y);
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	@Pure
	public double getRadius() {
		return this.radiusProperty.doubleValue();
	}

	/** Set the radius.
	 * 
	 * @param radius1 is the radius.
	 */
	public void setRadius(double radius1) {
		this.radiusProperty.set(Math.abs(radius1));
	}
	
	public void setRadiusProperty(DoubleProperty radius1) {
		this.radiusProperty = radius1;
	}


}

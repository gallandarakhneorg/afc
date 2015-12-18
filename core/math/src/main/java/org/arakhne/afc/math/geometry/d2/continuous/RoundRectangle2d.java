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

/** 2D round rectangle with DoubleProperty points.
 * 
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RoundRectangle2d extends AbstractRoundRectangle2F<RoundRectangle2d> {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 101021133567302859L;
	
	
	/** Lowest x-coordinate covered by this rectangular shape. */
	protected DoubleProperty minxProperty = new SimpleDoubleProperty(0f);
	/** Lowest y-coordinate covered by this rectangular shape. */
	protected DoubleProperty minyProperty = new SimpleDoubleProperty(0f);
	/** Highest x-coordinate covered by this rectangular shape. */
	protected DoubleProperty maxxProperty = new SimpleDoubleProperty(0f);
	/** Highest y-coordinate covered by this rectangular shape. */
	protected DoubleProperty maxyProperty = new SimpleDoubleProperty(0f);
	
	/** Width of the arcs at the corner of the box. */
	protected DoubleProperty arcWidthProperty = new SimpleDoubleProperty(0f);
	/** Height of the arcs at the corner of the box. */
	protected DoubleProperty arcHeightProperty = new SimpleDoubleProperty(0f);

	/**
	 */
	public RoundRectangle2d() {
		//
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth1
	 * @param arcHeight1
	 */
	public RoundRectangle2d(Point2f min, Point2f max, double arcWidth1, double arcHeight1) {
		super(min, max, arcWidth1,arcHeight1);
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2d(RoundRectangle2d rr) {
		super(rr);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth1
	 * @param arcHeight1
	 */
	public RoundRectangle2d(double x, double y, double width, double height, double arcWidth1, double arcHeight1) {
		super(x, y, width, height,arcWidth1,arcHeight1);
	}


	
	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	@Override
	public void set(double x, double y, double width, double height) {
		setFromCorners(x, y, x+width, y+height);
	}
	
	/** Change the frame of te rectangle.
	 * 
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	@Override
	public void set(Point2f min, Point2f max) {
		setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}
	
	/** Change the width of the rectangle, not the min corner.
	 * 
	 * @param width
	 */
	@Override
	public void setWidth(double width) {
		this.setMaxX(this.getMinX() + Math.max(0f, width));
	}

	/** Change the height of the rectangle, not the min corner.
	 * 
	 * @param height
	 */
	@Override
	public void setHeight(double height) {
		this.setMaxY(this.getMinY() + Math.max(0f, height));
	}

	@Override
	public void setInitiallyFromCorners(Point2D p1, Point2D p2) {
		this.setInitiallyFromCorners(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	@Override
	public void setInitiallyFromCorners(double x1, double y1, double x2, double y2) {
		if (x1<x2) {
			this.minxProperty.set(x1);
			this.maxxProperty.set(x2);
		}
		else {
			this.minxProperty.set(x2);
			this.maxxProperty.set(x1);
		}
		if (y1<y2) {
			this.minyProperty.set(y1);
			this.maxyProperty.set(y2);
		}
		else {
			this.minyProperty.set(y2);
			this.maxyProperty.set(y1);
		}
	}
	
	/** Change the frame of the rectangle.
	 * 
	 * @param p1 is the coordinate of the first corner.
	 * @param p2 is the coordinate of the second corner.
	 */
	@Override
	public void setFromCorners(Point2D p1, Point2D p2) {
		setFromCorners(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/** Change the frame of the rectangle.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 */
	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		if (x1<x2) {
			this.minxProperty.set(x1);
			this.maxxProperty.set(x2);
		}
		else {
			this.minxProperty.set(x2);
			this.maxxProperty.set(x1);
		}
		if (y1<y2) {
			this.minyProperty.set(y1);
			this.maxyProperty.set(y2);
		}
		else {
			this.minyProperty.set(y2);
			this.maxyProperty.set(y1);
		}
	}
	
	/**
     * Sets the framing rectangle of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     */
	@Override
	public void setFromCenter(double centerX, double centerY, double cornerX, double cornerY) {
		double dx = centerX - cornerX;
		double dy = centerY - cornerY;
		setFromCorners(cornerX, cornerY, centerX + dx, centerY + dy);
	}
	
	/** Replies the min X.
	 * 
	 * @return the min x.
	 */
	@Pure
	@Override
	public double getMinX() {
		return this.minxProperty.doubleValue();
	}

	/** Set the min X.
	 * 
	 * @param x the min x.
	 */
	@Override
	public void setMinX(double x) {
		if (this.minxProperty.doubleValue()>x)
			this.minxProperty.set(x);
	}

	/** Replies the center x.
	 * 
	 * @return the center x.
	 */
	@Pure
	@Override
	public double getCenterX() {
		return (this.minxProperty.doubleValue() + this.maxxProperty.doubleValue()) / 2f;
	}

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	@Pure
	@Override
	public double getMaxX() {
		return this.maxxProperty.doubleValue();
	}

	/** Set the max X.
	 * 
	 * @param x the max x.
	 */
	@Override
	public void setMaxX(double x) {
		if (this.maxxProperty.doubleValue()<x)
			this.maxxProperty.set(x);
	}

	/** Replies the min y.
	 * 
	 * @return the min y.
	 */
	@Pure
	@Override
	public double getMinY() {
		return this.minyProperty.doubleValue();
	}

	/** Set the min Y.
	 * 
	 * @param y the min y.
	 */
	@Override
	public void setMinY(double y) {
		if (this.minyProperty.doubleValue()>y)
			this.minyProperty.set(y);
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	@Override
	public double getCenterY() {
		return (this.minyProperty.doubleValue() + this.maxyProperty.doubleValue()) / 2f;
	}

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	@Pure
	@Override
	public double getMaxY() {
		return this.maxyProperty.doubleValue();
	}
	
	/** Set the max Y.
	 * 
	 * @param y the max y.
	 */
	@Override
	public void setMaxY(double y) {
		if (this.maxyProperty.doubleValue()<y)
			this.maxyProperty.set(y); 
	}

	/** Replies the width.
	 * 
	 * @return the width.
	 */
	@Pure
	@Override
	public double getWidth() {
		return this.maxxProperty.doubleValue() - this.minxProperty.doubleValue();
	}

	/** Replies the height.
	 * 
	 * @return the height.
	 */
	@Pure
	@Override
	public double getHeight() {
		return this.maxyProperty.doubleValue() - this.minyProperty.doubleValue();
	}
	
	/**
	 * Gets the width of the arc that rounds off the corners.
	 * @return the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	@Pure
	public double getArcWidth() {
		return this.arcWidthProperty.doubleValue();
	}

	/**
	 * Gets the height of the arc that rounds off the corners.
	 * @return the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	@Pure
	public double getArcHeight() {
		return this.arcHeightProperty.doubleValue();
	}

	/**
	 * Set the width of the arc that rounds off the corners.
	 * @param a is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public void setArcWidth(double a) {
		this.arcWidthProperty.set(a);
	}

	/**
	 * Set the height of the arc that rounds off the corners.
	 * @param a is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public void setArcHeight(double a) {
		this.arcHeightProperty.set(a);
	}

	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth1 is the width of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 * @param arcHeight1 is the height of the arc that rounds off the corners
	 * of this <code>RoundRectangle2f</code>.
	 */
	public void set(double x, double y, double width, double height, double arcWidth1, double arcHeight1) {
		setFromCorners(x, y, x+width, y+height); 
		this.arcWidthProperty.set(arcWidth1);
		this.arcHeightProperty.set(arcHeight1);
	}

	@Override
	public void set(Shape2F s) {
		if (s instanceof RoundRectangle2d) {
			RoundRectangle2d r = (RoundRectangle2d) s;
			set(r.getMinX(), r.getMinY(),
					r.getWidth(), r.getHeight(),
					r.getArcWidth(), r.getArcHeight());
		} else {
			AbstractRectangle2F<?> r = s.toBoundingBox();
			setFromCorners(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY());
		}
	}


}


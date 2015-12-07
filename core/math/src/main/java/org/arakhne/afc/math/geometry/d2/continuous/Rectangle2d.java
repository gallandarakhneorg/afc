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

import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


/** 2D rectangle with DoubleProperty points.
 * 
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Rectangle2d extends AbstractRectangle2F<Rectangle2d>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7259689465118883953L;


	protected DoubleProperty minxProperty;
	/** Lowest y-coordinate covered by this rectangular shape. */
	protected DoubleProperty minyProperty;
	/** Highest x-coordinate covered by this rectangular shape. */
	protected DoubleProperty maxxProperty;
	/** Highest y-coordinate covered by this rectangular shape. */
	protected DoubleProperty maxyProperty;


	/**
	 */
	public Rectangle2d() {
		this.minxProperty= new SimpleDoubleProperty(0f);
		this.minyProperty = new SimpleDoubleProperty(0f);
		this.maxxProperty = new SimpleDoubleProperty(0f);
		this.maxyProperty = new SimpleDoubleProperty(0f);
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2d(FunctionalPoint2D min, FunctionalPoint2D max) {
		this();
		this.setInitiallyFromCorners(min, max);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectangle2d(double x, double y, double width, double height) {
		this();
		this.setInitiallyFromCorners(x, y, x+width, y+height);
	}

	/**
	 * @param r
	 */
	public Rectangle2d(Rectangle2d r) {
		this();
		this.setInitiallyFromCorners(r.getMinX(),r.getMinY(),r.getMaxX(),r.getMaxY());
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
	
	
	@Override
	public void setFromCorners(Point2D p1, Point2D p2) {
		setFromCorners(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}


	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		if (x1<x2) {
			this.setMinX(x1);
			this.setMaxX(x2);
		}
		else {
			this.setMinX(x2);
			this.setMaxX(x1);
		}
		if (y1<y2) {
			this.setMinY(y1);
			this.setMaxY(y2);
		}
		else {
			this.setMinY(y2);
			this.setMaxY(y1);
		}
	}

	
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
		double o = this.getMaxX();
		if (o<x) {
			this.minxProperty.set(o);
			this.maxxProperty.set(x);
		}
		else {
			this.minxProperty.set(x);
		}
	}

	/** Replies the center x.
	 * 
	 * @return the center x.
	 */
	@Pure
	@Override
	public double getCenterX() {
		return (this.getMinX() + this.getMaxX()) / 2f;
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
		double o = this.getMinX();
		if (o>x) {
			this.maxxProperty.set(o);
			this.minxProperty.set(x);
		}
		else {
			this.maxxProperty.set(x);
		}
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
		double o = this.getMaxY();
		if (o<y) {
			this.minyProperty.set(o);
			this.maxyProperty.set(y);
		}
		else {
			this.minyProperty.set(y);
		}
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	@Override
	public double getCenterY() {
		return (this.getMinY()+ this.getMaxY()) / 2f;
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
		double o = this.getMinY();
		if (o>y) {
			this.maxyProperty.set(o);
			this.minyProperty.set(y);
		}
		else {
			this.maxyProperty.set(y);
		}
	}

	/** Replies the width.
	 * 
	 * @return the width.
	 */
	@Pure
	@Override
	public double getWidth() {
		return this.getMaxX() - this.getMinX();
	}

	/** Replies the height.
	 * 
	 * @return the height.
	 */
	@Pure
	@Override
	public double getHeight() {
		return this.getMaxY() - this.getMinY();
	}


	@Override
	public void set(Shape2F s) {
		Rectangle2f r = new Rectangle2f();
		s.toBoundingBox(r);
		this.setMaxX(r.getMaxX());
		this.setMaxY(r.getMaxY());
		this.setMinX(r.getMinX());
		this.setMinY(r.getMinY());
	}


	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Rectangle2f toBoundingBox() {
		Rectangle2f r = new Rectangle2f();
		r.setMaxX(this.getMaxX());
		r.setMaxY(this.getMaxY());
		r.setMinX(this.getMinX());
		r.setMinY(this.getMinY());

		return r;
	}

	

}

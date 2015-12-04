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


/** 2D line segment with DoubleProperty points coordinates.
 * 
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment2d extends AbstractSegment2F<Segment2d>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1326197951243020619L;

	/** X-coordinate of the first point. */
	protected DoubleProperty axProperty = new SimpleDoubleProperty(0f);
	/** Y-coordinate of the first point. */
	protected DoubleProperty ayProperty = new SimpleDoubleProperty(0f);
	/** X-coordinate of the second point. */
	protected DoubleProperty bxProperty = new SimpleDoubleProperty(0f);
	/** Y-coordinate of the second point. */
	protected DoubleProperty byProperty = new SimpleDoubleProperty(0f);

	/**
	 */
	public Segment2d() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2d(Point2D a, Point2D b) {
		this.set(a, b);
	}

	/**
	 * @param s
	 */
	public Segment2d(Segment2d s) {
		this.setX1(s.getX1());
		this.setY1(s.getY1());
		this.setX2(s.getX2());
		this.setY2(s.getY2());
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Segment2d(double x1, double y1, double x2, double y2) {
		set(x1, y1, x2, y2);
	}

	@Override
	public void clear() {
		this.set(0f,0f,0f,0f);
	}


	/** Change the line.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void set(double x1, double y1, double x2, double y2) {
		this.setX1(x1);
		this.setY1(y1);
		this.setX2(x2);
		this.setY2(y2);
	}

	/** Change the line.
	 * 
	 * @param a
	 * @param b
	 */
	public void set(Point2D a, Point2D b) {
		this.setX1(a.getX());
		this.setY1(a.getY());
		this.setX2(b.getX());
		this.setY2(b.getY());
	}

	@Override
	public void set(Shape2F s) {
		if (s instanceof Segment2f) {
			Segment2f g = (Segment2f) s;
			set(g.getX1(), g.getY1(), g.getX2(), g.getY2());
		} else {
			Rectangle2f r = s.toBoundingBox();
			this.setX1(r.getMinX());
			this.setY1(r.getMinY());
			this.setX2(r.getMaxX());
			this.setY2(r.getMaxY());
		}
	}

	/** Replies the X of the first point.
	 * 
	 * @return the x of the first point.
	 */
	@Pure
	public double getX1() {
		return this.axProperty.doubleValue();
	}

	/** Replies the Y of the first point.
	 * 
	 * @return the y of the first point.
	 */
	@Pure
	public double getY1() {
		return this.ayProperty.doubleValue();
	}

	/** Replies the X of the second point.
	 * 
	 * @return the x of the second point.
	 */
	@Pure
	public double getX2() {
		return this.bxProperty.doubleValue();
	}

	/** Replies the Y of the second point.
	 * 
	 * @return the y of the second point.
	 */
	@Pure
	public double getY2() {
		return this.byProperty.doubleValue();
	}

	/** Replies the first point.
	 * 
	 * @return the first point.
	 */
	@Pure
	public Point2D getP1() {
		return new Point2f(this.getX1(), this.getY1());
	}

	/** Replies the second point.
	 * 
	 * @return the second point.
	 */
	@Pure
	public Point2D getP2() {
		return new Point2f(this.getX2(), this.getY2());
	}

	@Override
	public void setX1(double x) {
		this.axProperty.set(x);
	}

	@Override
	public void setY1(double y) {
		this.ayProperty.set(y);
	}

	@Override
	public void setX2(double x) {
		this.bxProperty.set(x);
	}

	@Override
	public void setY2(double y) {
		this.byProperty.set(y);
	}

}

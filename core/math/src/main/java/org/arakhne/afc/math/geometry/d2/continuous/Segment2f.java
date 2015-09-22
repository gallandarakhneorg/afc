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

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.FunctionalVector2D;
import org.arakhne.afc.math.geometry.d2.Point2D;



/** 2D line segment with floating-point coordinates.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment2f extends AbstractSegment2F<Segment2f> {

	private static final long serialVersionUID = -82425036308183925L;

	/** X-coordinate of the first point. */
	protected double ax = 0f;
	/** Y-coordinate of the first point. */
	protected double ay = 0f;
	/** X-coordinate of the second point. */
	protected double bx = 0f;
	/** Y-coordinate of the second point. */
	protected double by = 0f;

	/**
	 */
	public Segment2f() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2f(Point2D a, Point2D b) {
		set(a, b);
	}

	/**
	 * @param s
	 */
	public Segment2f(Segment2f s) {
		this.ax = s.ax;
		this.ay = s.ay;
		this.bx = s.bx;
		this.by = s.by;
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Segment2f(double x1, double y1, double x2, double y2) {
		set(x1, y1, x2, y2);
	}

	@Override
	public void clear() {
		this.ax = this.ay = this.bx = this.by = 0f;
	}


	/** Change the line.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void set(double x1, double y1, double x2, double y2) {
		this.ax = x1;
		this.ay = y1;
		this.bx = x2;
		this.by = y2;
	}

	/** Change the line.
	 * 
	 * @param a
	 * @param b
	 */
	public void set(Point2D a, Point2D b) {
		this.ax = a.getX();
		this.ay = a.getY();
		this.bx = b.getX();
		this.by = b.getY();
	}

	@Override
	public void set(Shape2F s) {
		if (s instanceof Segment2f) {
			Segment2f g = (Segment2f) s;
			set(g.getX1(), g.getY1(), g.getX2(), g.getY2());
		} else {
			Rectangle2f r = s.toBoundingBox();
			this.ax = r.getMinX();
			this.ay = r.getMinY();
			this.bx = r.getMaxX();
			this.by = r.getMaxY();
		}
	}

	/** Replies the X of the first point.
	 * 
	 * @return the x of the first point.
	 */
	public double getX1() {
		return this.ax;
	}

	/** Replies the Y of the first point.
	 * 
	 * @return the y of the first point.
	 */
	public double getY1() {
		return this.ay;
	}

	/** Replies the X of the second point.
	 * 
	 * @return the x of the second point.
	 */
	public double getX2() {
		return this.bx;
	}

	/** Replies the Y of the second point.
	 * 
	 * @return the y of the second point.
	 */
	public double getY2() {
		return this.by;
	}

	/** Replies the first point.
	 * 
	 * @return the first point.
	 */
	public Point2D getP1() {
		return new Point2f(this.ax, this.ay);
	}

	/** Replies the second point.
	 * 
	 * @return the second point.
	 */
	public Point2D getP2() {
		return new Point2f(this.bx, this.by);
	}

	@Override
	public void setX1(double x) {
		this.ax = x;
	}

	@Override
	public void setY1(double y) {
		this.ay = y;
	}

	@Override
	public void setX2(double x) {
		this.bx = x;
	}

	@Override
	public void setY2(double y) {
		this.by = y;
	}

}
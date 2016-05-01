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

package org.arakhne.afc.math.geometry.d2.d;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** A triangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Triangle2d
		extends AbstractShape2d<Triangle2d>
		implements Triangle2afp<Shape2d<?>, Triangle2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = 6181771392780117178L;

	private double x1;
	
	private double y1;

	private double x2;
	
	private double y2;

	private double x3;
	
	private double y3;
	
	private Boolean isCCW;

	/**
	 */
	public Triangle2d() {
		//
	}

	/**
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Triangle2d(Point2D<?, ?> p1, Point2D<?, ?> p2, Point2D<?, ?> p3) {
		assert (p1 != null) : "Point 1 must not be null"; //$NON-NLS-1$
		assert (p2 != null) : "Point 1 must not be null"; //$NON-NLS-1$
		assert (p3 != null) : "Point 1 must not be null"; //$NON-NLS-1$
		set(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public Triangle2d(double x1, double y1, double x2, double y2, double x3, double y3) {
		set(x1, y1, x2, y2, x3, y3);
	}
	
	/** Construct a triangle from a triangle.
	 * @param t
	 */
	public Triangle2d(Triangle2afp<?, ?, ?, ?, ?, ?> t) {
		assert (t != null) : "Triangle must be not null"; //$NON-NLS-1$
		set(t.getX1(), t.getY1(), t.getX2(), t.getY2(), t.getX3(), t.getY3());
	}
	
	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x1);
		bits = 31 * bits + Double.doubleToLongBits(this.y1);
		bits = 31 * bits + Double.doubleToLongBits(this.x2);
		bits = 31 * bits + Double.doubleToLongBits(this.y2);
		bits = 31 * bits + Double.doubleToLongBits(this.x3);
		bits = 31 * bits + Double.doubleToLongBits(this.y3);
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX1());
		b.append(";"); //$NON-NLS-1$
		b.append(getY1());
		b.append(";"); //$NON-NLS-1$
		b.append(getX2());
		b.append(";"); //$NON-NLS-1$
		b.append(getY2());
		b.append(";"); //$NON-NLS-1$
		b.append(getX3());
		b.append(";"); //$NON-NLS-1$
		b.append(getY3());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public Triangle2d createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		Point2d point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		double x1 = point.getX();
		double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		double x2 = point.getX();
		double y2 = point.getY();
		point.set(getX3(), getY3());
		transform.transform(point);
		return getGeomFactory().newTriangle(x1, y1, x2, y2, point.getX(), point.getY());
	}

	@Override
	public double getX1() {
		return this.x1;
	}

	@Override
	public double getY1() {
		return this.y1;
	}

	@Override
	public double getX2() {
		return this.x2;
	}

	@Override
	public double getY2() {
		return this.y2;
	}

	@Override
	public double getX3() {
		return this.x3;
	}

	@Override
	public double getY3() {
		return this.y3;
	}

	@Override
	public void setX1(double x) {
		if (this.x1 != x) {
			this.x1 = x;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.y1 != y) {
			this.y1 = y;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.x2 != x) {
			this.x2 = x;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.y2 != y) {
			this.y2 = y;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setX3(double x) {
		if (this.x3 != x) {
			this.x3 = x;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setY3(double y) {
		if (this.y3 != y) {
			this.y3 = y;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void set(double x1, double y1, double x2, double y2, double x3, double y3) {
		if (this.x1 != x1 || this.y1 != y1
			|| this.x2 != x2 || this.y2 != y2
			|| this.x3 != x3 || this.y3 != y3) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.x3 = x3;
			this.y3 = y3;
			this.isCCW = null;
			fireGeometryChange();
		}
	}
	
	@Override
	public boolean isCCW() {
		if (this.isCCW == null) {
			this.isCCW = Boolean.valueOf(Triangle2afp.isCCWOrderDefinition(
					getX1(), getY1(), getX2(), getY2(), getX3(), getY3()));
		}
		return this.isCCW.booleanValue();
	}

}

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
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** A 2D segment/line with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2d extends AbstractShape2d<Segment2d>
	implements Segment2afp<Shape2d<?>, Segment2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = -5667213589442134247L;

	private double ax;

	private double ay;

	private double bx;

	private double by;
	
	/**
	 */
	public Segment2d() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2d(Point2D<?, ?> a, Point2D<?, ?> b) {
		this(a.getX(), a.getY(), b.getX(), b.getY());
	}

	/**
	 * @param s
	 */
	public Segment2d(Segment2afp<?, ?, ?, ?, ?, ?> s) {
		this(s.getX1(), s.getY1(), s.getX2(), s.getY2());
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

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.ax);
		bits = 31 * bits + Double.doubleToLongBits(this.ay);
		bits = 31 * bits + Double.doubleToLongBits(this.bx);
		bits = 31 * bits + Double.doubleToLongBits(this.by);
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
		b.append("|"); //$NON-NLS-1$
		b.append(getX2());
		b.append(";"); //$NON-NLS-1$
		b.append(getY2());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public Segment2d createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		Point2d point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		double x1 = point.getX();
		double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, point.getX(), point.getY());
	}
	
	@Override
	public void set(double x1, double y1, double x2, double y2) {
		if (this.ax != x1 || this.ay != y1 || this.bx != x2 || this.by != y2) {
			this.ax = x1;
			this.ay = y1;
			this.bx = x2;
			this.by = y2;
			fireGeometryChange();
		}
	}

	@Override
	public void setX1(double x) {
		if (this.ax != x) {
			this.ax = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.ay != y) {
			this.ay = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.bx != x) {
			this.bx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.by != y) {
			this.by = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getX1() {
		return this.ax;
	}

	@Pure
	@Override
	public double getY1() {
		return this.ay;
	}

	@Pure
	@Override
	public double getX2() {
		return this.bx;
	}

	@Pure
	@Override
	public double getY2() {
		return this.by;
	}

	@Override
	public Point2d getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay);
	}

	@Override
	public Point2d getP2() {
		return getGeomFactory().newPoint(this.bx, this.by);
	}

}

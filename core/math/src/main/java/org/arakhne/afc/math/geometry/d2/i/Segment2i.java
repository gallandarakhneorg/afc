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

package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** A 2D segment/line with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2i extends AbstractShape2i<Segment2i>
	implements Segment2ai<Shape2i<?>, Segment2i, PathElement2i, Point2i, Vector2i, Rectangle2i> {

	private static final long serialVersionUID = 4069080422632034507L;

	private int ax;

	private int ay;

	private int bx;

	private int by;
	
	/**
	 */
	public Segment2i() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2i(Point2D<?, ?> a, Point2D<?, ?> b) {
		this(a.ix(), a.iy(), b.ix(), b.iy());
	}

	/**
	 * @param s
	 */
	public Segment2i(Segment2ai<?, ?, ?, ?, ?, ?> s) {
		this(s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Segment2i(int x1, int y1, int x2, int y2) {
		set(x1, y1, x2, y2);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.ax;
		bits = 31 * bits + this.ay;
		bits = 31 * bits + this.bx;
		bits = 31 * bits + this.by;
		return bits ^ (bits >> 32);
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
	public Shape2i<?> createTransformedShape(Transform2D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point2i point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		int x1 = point.ix();
		int y1 = point.iy();
		point.set(getX2(), getY2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, point.ix(), point.iy());
	}
	
	@Override
	public void set(int x1, int y1, int x2, int y2) {
		if (this.ax != x1 || this.ay != y1 || this.bx != x2 || this.by != y2) {
			this.ax = x1;
			this.ay = y1;
			this.bx = x2;
			this.by = y2;
			fireGeometryChange();
		}
	}

	@Override
	public void setX1(int x) {
		if (this.ax != x) {
			this.ax = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(int y) {
		if (this.ay != y) {
			this.ay = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(int x) {
		if (this.bx != x) {
			this.bx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(int y) {
		if (this.by != y) {
			this.by = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getX1() {
		return this.ax;
	}

	@Pure
	@Override
	public int getY1() {
		return this.ay;
	}

	@Pure
	@Override
	public int getX2() {
		return this.bx;
	}

	@Pure
	@Override
	public int getY2() {
		return this.by;
	}

	@Override
	public Point2i getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay);
	}

	@Override
	public Point2i getP2() {
		return getGeomFactory().newPoint(this.bx, this.by);
	}

}

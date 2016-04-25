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

package org.arakhne.afc.math.geometry.d2.ifx;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** A 2D segment/line with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2ifx extends AbstractShape2ifx<Segment2ifx>
	implements Segment2ai<Shape2ifx<?>, Segment2ifx, PathElement2ifx, Point2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -1406743357357708790L;

	private IntegerProperty ax;

	private IntegerProperty ay;

	private IntegerProperty bx;

	private IntegerProperty by;
	
	/**
	 */
	public Segment2ifx() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2ifx(Point2D a, Point2D b) {
		this(a.ix(), a.iy(), b.ix(), b.iy());
	}

	/**
	 * @param s
	 */
	public Segment2ifx(Segment2ai<?, ?, ?, ?, ?> s) {
		this(s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Segment2ifx(int x1, int y1, int x2, int y2) {
		set(x1, y1, x2, y2);
	}
	
	@Override
	public Segment2ifx clone() {
		Segment2ifx clone = super.clone();
		if (clone.ax != null) {
			clone.ax = null;
			clone.x1Property().set(getX1());
		}
		if (clone.ay != null) {
			clone.ay = null;
			clone.y1Property().set(getY1());
		}
		if (clone.bx != null) {
			clone.bx = null;
			clone.x2Property().set(getX2());
		}
		if (clone.by != null) {
			clone.by = null;
			clone.y2Property().set(getY2());
		}
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + getX1();
		bits = 31 * bits + getY1();
		bits = 31 * bits + getX2();
		bits = 31 * bits + getY2();
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
	public Shape2ifx<?> createTransformedShape(Transform2D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point2ifx point = new Point2ifx(getX1(), getY1());
		transform.transform(point);
		int x1 = point.ix();
		int y1 = point.iy();
		point.set(getX2(), getY2());
		transform.transform(point);
		return new Segment2ifx(x1, y1, point.ix(), point.iy());
	}
	
	@Override
	public void set(int x1, int y1, int x2, int y2) {
		setX1(x1);
		setY1(y1);
		setX2(x2);
		setY2(y2);
	}

	@Override
	public void setX1(int x) {
		x1Property().set(x);
	}

	@Override
	public void setY1(int y) {
		y1Property().set(y);
	}

	@Override
	public void setX2(int x) {
		x2Property().set(x);
	}

	@Override
	public void setY2(int y) {
		y2Property().set(y);
	}

	@Pure
	@Override
	public int getX1() {
		return this.ax == null ? 0 : this.ax.get();
	}

	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public IntegerProperty x1Property() {
		if (this.ax == null) {
			this.ax = new SimpleIntegerProperty(this, "x1"); //$NON-NLS-1$
		}
		return this.ax;
	}
	
	@Pure
	@Override
	public int getY1() {
		return this.ay == null ? 0 : this.ay.get();
	}

	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public IntegerProperty y1Property() {
		if (this.ay == null) {
			this.ay = new SimpleIntegerProperty(this, "y1"); //$NON-NLS-1$
		}
		return this.ay;
	}

	@Pure
	@Override
	public int getX2() {
		return this.bx == null ? 0 : this.bx.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public IntegerProperty x2Property() {
		if (this.bx == null) {
			this.bx = new SimpleIntegerProperty(this, "x2"); //$NON-NLS-1$
		}
		return this.bx;
	}

	@Pure
	@Override
	public int getY2() {
		return this.by == null ? 0 : this.by.get();
	}

	/** Replies the property that is the y coordinate of the second segment point.
	 *
	 * @return the y2 property.
	 */
	@Pure
	public IntegerProperty y2Property() {
		if (this.by == null) {
			this.by = new SimpleIntegerProperty(this, "y2"); //$NON-NLS-1$
		}
		return this.by;
	}

	@Override
	public Point2ifx getP1() {
		return new Point2ifx(this.ax, this.ay);
	}

	@Override
	public Point2ifx getP2() {
		return new Point2ifx(this.bx, this.by);
	}

}

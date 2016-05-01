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

package org.arakhne.afc.math.geometry.d2.fpfx;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/** Segment with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2fx extends AbstractShape2fx<Segment2fx>
	implements Segment2afp<Shape2fx<?>, Segment2fx, PathElement2fx, Point2fx, Vector2fx, Rectangle2fx> {

	private static final long serialVersionUID = -5603953934276693947L;

	private DoubleProperty ax;

	private DoubleProperty ay;

	private DoubleProperty bx;

	private DoubleProperty by;
	
	/**
	 */
	public Segment2fx() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment2fx(Point2D<?, ?> a, Point2D<?, ?> b) {
		this(a.getX(), a.getY(), b.getX(), b.getY());
	}

	/**
	 * @param s
	 */
	public Segment2fx(Segment2afp<?, ?, ?, ?, ?, ?> s) {
		this(s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Segment2fx(double x1, double y1, double x2, double y2) {
		set(x1, y1, x2, y2);
	}
	
	@Override
	public Segment2fx clone() {
		Segment2fx clone = super.clone();
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
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getX1());
		bits = 31 * bits + Double.doubleToLongBits(getY1());
		bits = 31 * bits + Double.doubleToLongBits(getX2());
		bits = 31 * bits + Double.doubleToLongBits(getY2());
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
	public Segment2fx createTransformedShape(Transform2D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point2fx point = new Point2fx(getX1(), getY1());
		transform.transform(point);
		double x1 = point.getX();
		double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		return new Segment2fx(x1, y1, point.getX(), point.getY());
	}

	@Override
	public void set(double x1, double y1, double x2, double y2) {
		setX1(x1);
		setY1(y1);
		setX2(x2);
		setY2(y2);
	}

	@Override
	public void setX1(double x) {
		x1Property().set(x);
	}

	@Override
	public void setY1(double y) {
		y1Property().set(y);
	}

	@Override
	public void setX2(double x) {
		x2Property().set(x);
	}

	@Override
	public void setY2(double y) {
		y2Property().set(y);
	}

	@Pure
	@Override
	public double getX1() {
		return this.ax == null ? 0 : this.ax.get();
	}
	
	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public DoubleProperty x1Property() {
		if (this.ax == null) {
			this.ax = new SimpleDoubleProperty(this, "x1"); //$NON-NLS-1$
		}
		return this.ax;
	}

	@Pure
	@Override
	public double getY1() {
		return this.ay == null ? 0 : this.ay.get();
	}

	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public DoubleProperty y1Property() {
		if (this.ay == null) {
			this.ay = new SimpleDoubleProperty(this, "y1"); //$NON-NLS-1$
		}
		return this.ay;
	}

	@Pure
	@Override
	public double getX2() {
		return this.bx == null ? 0 : this.bx.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty x2Property() {
		if (this.bx == null) {
			this.bx = new SimpleDoubleProperty(this, "x2"); //$NON-NLS-1$
		}
		return this.bx;
	}

	@Pure
	@Override
	public double getY2() {
		return this.by == null ? 0 : this.by.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty y2Property() {
		if (this.by == null) {
			this.by = new SimpleDoubleProperty(this, "y2"); //$NON-NLS-1$
		}
		return this.by;
	}

	@Override
	public Point2fx getP1() {
		return new Point2fx(this.ax, this.ay);
	}

	@Override
	public Point2fx getP2() {
		return new Point2fx(this.bx, this.by);
	}

	@Override
	public ObjectProperty<Rectangle2fx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						return toBoundingBox();
					},
					x1Property(), y1Property(),
					x2Property(), y2Property()));
		}
		return this.boundingBox;
	}

}

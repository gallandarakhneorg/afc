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
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** Circle with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Circle2fx
		extends AbstractShape2fx<Circle2fx>
		implements Circle2afp<Shape2fx<?>, Circle2fx, PathElement2fx, Point2fx, Rectangle2fx> {

	private static final long serialVersionUID = 837592010117981823L;

	private DoubleProperty centerX;
	
	private DoubleProperty centerY;

	private DoubleProperty radius;

	/**
	 */
	public Circle2fx() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Circle2fx(Point2D center, double radius) {
		assert (center != null) : "Center must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle2fx(double x, double y, double radius) {
		set(x, y, radius);
	}
	
	/** Construct a circle from a circle.
	 * @param c
	 */
	public Circle2fx(Circle2afp<?, ?, ?, ?, ?> c) {
		assert (c != null) : "Circle must be not null"; //$NON-NLS-1$
		set(c.getX(), c.getY(), c.getRadius());
	}

	@Pure
	@Override
	public Circle2fx clone() {
		Circle2fx clone = super.clone();
		if (clone.centerX != null) {
			clone.centerX = null;
			clone.xProperty().set(getX());
		}
		if (clone.centerY != null) {
			clone.centerY = null;
			clone.yProperty().set(getY());
		}
		if (clone.radius != null) {
			clone.radius = null;
			clone.radiusProperty().set(getRadius());
		}
		return clone;
	}
	
	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getX());
		bits = 31 * bits + Double.doubleToLongBits(getY());
		bits = 31 * bits + Double.doubleToLongBits(getRadius());
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX());
		b.append(";"); //$NON-NLS-1$
		b.append(getY());
		b.append(";"); //$NON-NLS-1$
		b.append(getRadius());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public double getX() {
		return this.centerX == null ? 0 : this.centerX.get();
	}

	@Pure
	@Override
	public double getY() {
		return this.centerY == null ? 0 : this.centerY.get();
	}

	@Pure
	@Override
	public Point2fx getCenter() {
		return new Point2fx(xProperty(), yProperty());
	}

	@Override
	public void setX(double x) {
		xProperty().set(x);
	}

	@Override
	public void setY(double y) {
		yProperty().set(y);
	}

	/** Replies the property that is the x coordinate of the circle center.
	 *
	 * @return the x property.
	 */
	@Pure
	public DoubleProperty xProperty() {
		if (this.centerX == null) {
			this.centerX = new SimpleDoubleProperty(this, "x"); //$NON-NLS-1$
		}
		return this.centerX;
	}

	/** Replies the property that is the y coordinate of the circle center.
	 *
	 * @return the y property.
	 */
	@Pure
	public DoubleProperty yProperty() {
		if (this.centerY == null) {
			this.centerY = new SimpleDoubleProperty(this, "y"); //$NON-NLS-1$
		}
		return this.centerY;
	}

	@Pure
	@Override
	public double getRadius() {
		return this.radius == null ? 0 : this.radius.get();
	}

	@Override
	public void setRadius(double radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		radiusProperty().set(radius);
	}

	/** Replies the property that is the radius of the circle.
	 *
	 * @return the radius property.
	 */
	@Pure
	public DoubleProperty radiusProperty() {
		if (this.radius == null) {
			this.radius = new SimpleDoubleProperty(this, "radius") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					if (get() < 0.) {
						set(0.);
					}
				}
			};
		}
		return this.radius;
	}

	@Override
	public void set(double x, double y, double radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		xProperty().set(x);
		yProperty().set(y);
		radiusProperty().set(radius);
	}

}

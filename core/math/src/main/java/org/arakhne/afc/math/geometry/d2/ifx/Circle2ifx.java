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
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** A circle with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Circle2ifx
		extends AbstractShape2ifx<Circle2ifx>
		implements Circle2ai<Shape2ifx<?>, Circle2ifx, PathElement2ifx, Point2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = 3750916959512063017L;

	private IntegerProperty centerX;
	
	private IntegerProperty centerY;

	private IntegerProperty radius;

	/**
	 */
	public Circle2ifx() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Circle2ifx(Point2D center, int radius) {
		set(center.ix(), center.iy(), radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle2ifx(int x, int y, int radius) {
		set(x, y, radius);
	}
	
	/** Construct a circle from a circle.
	 * @param c
	 */
	public Circle2ifx(Circle2ai<?, ?, ?, ?, ?> c) {
		assert (c != null) : "Circle must be not null"; //$NON-NLS-1$
		set(c.getX(), c.getY(), c.getRadius());
	}
	
	@Override
	public Circle2ifx clone() {
		Circle2ifx clone = super.clone();
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
		int bits = 1;
		bits = 31 * bits + getX();
		bits = 31 * bits + getY();
		bits = 31 * bits + getRadius();
		return bits ^ (bits >> 32);
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
	public int getX() {
		return this.centerX == null ? 0 : this.centerX.get();
	}

	/** Replies the property that is the x coordinate of the circle center.
	 *
	 * @return the x property.
	 */
	@Pure
	public IntegerProperty xProperty() {
		if (this.centerX == null) {
			this.centerX = new SimpleIntegerProperty(this, "x"); //$NON-NLS-1$
		}
		return this.centerX;
	}
	
	@Pure
	@Override
	public int getY() {
		return this.centerY == null ? 0 : this.centerY.get();
	}

	/** Replies the property that is the y coordinate of the circle center.
	 *
	 * @return the y property.
	 */
	@Pure
	public IntegerProperty yProperty() {
		if (this.centerY == null) {
			this.centerY = new SimpleIntegerProperty(this, "y"); //$NON-NLS-1$
		}
		return this.centerY;
	}
	
	@Override
	public void setX(int x) {
		xProperty().set(x);
	}

	@Override
	public void setY(int y) {
		yProperty().set(y);
	}

	@Pure
	@Override
	public int getRadius() {
		return this.radius == null ? 0 : this.radius.get();
	}

	@Override
	public void setRadius(int radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		radiusProperty().set(radius);
	}

	/** Replies the property that is the radius of the circle.
	 *
	 * @return the radius property.
	 */
	@Pure
	public IntegerProperty radiusProperty() {
		if (this.radius == null) {
			this.radius = new SimpleIntegerProperty(this, "radius") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					if (get() < 0) {
						set(0);
					}
				}
			};
		}
		return this.radius;
	}

	@Override
	public void set(int x, int y, int radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		xProperty().set(x);
		yProperty().set(y);
		radiusProperty().set(radius);
	}

}

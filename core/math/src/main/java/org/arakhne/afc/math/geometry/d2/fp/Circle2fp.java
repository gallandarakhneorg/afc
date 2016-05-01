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

package org.arakhne.afc.math.geometry.d2.fp;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** A circle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Circle2fp
		extends AbstractShape2fp<Circle2fp>
		implements Circle2afp<Shape2fp<?>, Circle2fp, PathElement2fp, Point2fp, Vector2fp, Rectangle2fp> {

	private static final long serialVersionUID = -8532584773530573738L;

	private double centerX;
	
	private double centerY;

	private double radius;

	/**
	 */
	public Circle2fp() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Circle2fp(Point2D<?, ?> center, double radius) {
		set(center.getX(), center.getY(), radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle2fp(double x, double y, double radius) {
		set(x, y, radius);
	}
	
	/** Construct a circle from a circle.
	 * @param c
	 */
	public Circle2fp(Circle2afp<?, ?, ?, ?, ?, ?> c) {
		assert (c != null) : "Circle must be not null"; //$NON-NLS-1$
		set(c.getX(), c.getY(), c.getRadius());
	}
	
	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.centerX);
		bits = 31 * bits + Double.doubleToLongBits(this.centerY);
		bits = 31 * bits + Double.doubleToLongBits(this.radius);
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
		return this.centerX;
	}

	@Pure
	@Override
	public double getY() {
		return this.centerY;
	}

	@Override
	public void setX(double x) {
		if (this.centerX != x) {
			this.centerX = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY(double y) {
		if (this.centerY != y) {
			this.centerY = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getRadius() {
		return this.radius;
	}

	@Override
	public void setRadius(double radius) {
		if (this.radius != radius) {
			this.radius = radius;
			fireGeometryChange();
		}
	}

	@Override
	public void set(double x, double y, double radius) {
		assert (radius >= 0.) : "Radius must be positive or zero"; //$NON-NLS-1$
		if (this.centerX != x || this.centerY != y || this.radius != radius) {
			this.centerX = x;
			this.centerY = y;
			this.radius = radius;
			fireGeometryChange();
		}
	}

}

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
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** A rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Rectangle2fp extends AbstractShape2fp<Rectangle2fp>
	implements Rectangle2afp<Shape2fp<?>, Rectangle2fp, PathElement2fp, Point2fp, Rectangle2fp> {

	private static final long serialVersionUID = -2138921378214589458L;

	private double minx;

	private double miny;

	private double maxx;

	private double maxy;
	

	/**
	 */
	public Rectangle2fp() {
		super();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2fp(Point2D min, Point2D max) {
		setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectangle2fp(double x, double y, double width, double height) {
		setFromCorners(x, y, x + width, y + height);
	}
	
	/**
	 * @param r
	 */
	public Rectangle2fp(Rectangle2fp r) {
		set(r);
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		if (x1 <= x2) {
			this.minx = x1;
			this.maxx = x2;
		} else {
			this.minx = x2;
			this.maxx = x1;
		}
		if (y1 <= y2) {
			this.miny = y1;
			this.maxy = y2;
		} else {
			this.miny = y2;
			this.maxy = y1;
		}
	}

	@Pure
	@Override
	public double getMinX() {
		return this.minx;
	}

	@Override
	public void setMinX(double x) {
		if (x <= this.maxx) {
			this.minx = x;
		} else {
			this.minx = this.maxx;
			this.maxx = x;
		}
	}

	@Pure
	@Override
	public double getMaxX() {
		return this.maxx;
	}

	@Override
	public void setMaxX(double x) {
		if (x > this.minx) {
			this.maxx = x;
		} else {
			this.maxx = this.minx;
			this.minx = x;
		}
	}

	@Pure
	@Override
	public double getMinY() {
		return this.miny;
	}

	@Override
	public void setMinY(double y) {
		if (y <= this.maxy) {
			this.miny = y;
		} else {
			this.miny = this.maxy;
			this.maxy = y;
		}
	}

	@Pure
	@Override
	public double getMaxY() {
		return this.maxy;
	}

	@Override
	public void setMaxY(double y) {
		if (y > this.miny) {
			this.maxy = y;
		} else {
			this.maxy = this.miny;
			this.miny = y;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.minx);
		bits = 31 * bits + Double.doubleToLongBits(this.miny);
		bits = 31 * bits + Double.doubleToLongBits(this.maxx);
		bits = 31 * bits + Double.doubleToLongBits(this.maxy);
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getMinX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMinY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

}

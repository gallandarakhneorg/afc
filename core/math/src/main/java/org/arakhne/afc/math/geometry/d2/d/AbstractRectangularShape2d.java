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

import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** Abstract rectangular shape with 2 double precision floating-point numbers.
 * 
 * @param <IT> is the type of the implementation of this shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractRectangularShape2d<IT extends AbstractRectangularShape2d<?>>
	extends AbstractShape2d<IT>
	implements RectangularShape2afp<Shape2d<?>, IT, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = 562658629477723655L;

	private double minx;

	private double miny;

	private double maxx;

	private double maxy;
	
	/**
	 */
	public AbstractRectangularShape2d() {
		//
	}

	/**
	 * @param r
	 */
	public AbstractRectangularShape2d(RectangularShape2afp<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		this.minx = r.getMinX();
		this.miny = r.getMinY();
		this.maxx = r.getMaxX();
		this.maxy = r.getMaxY();
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		double a, b, c, d;
		if (x1 <= x2) {
			a = x1;
			b = x2;
		} else {
			a = x2;
			b = x1;
		}
		if (y1 <= y2) {
			c = y1;
			d = y2;
		} else {
			c = y2;
			d = y1;
		}
		if (this.minx != a || this.maxx != b || this.miny != c || this.maxy != d) {
			this.minx = a;
			this.maxx = b;
			this.miny = c;
			this.maxy = d;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getMinX() {
		return this.minx;
	}

	@Override
	public void setMinX(double x) {
		if (this.minx != x) {
			this.minx = x;
			if (x > this.maxx) {
				this.maxx = x;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getMaxX() {
		return this.maxx;
	}

	@Override
	public void setMaxX(double x) {
		if (this.maxx != x) {
			this.maxx = x;
			if (x < this.minx) {
				this.minx = x;
			}
			fireGeometryChange(); 
		}
	}

	@Pure
	@Override
	public double getMinY() {
		return this.miny;
	}

	@Override
	public void setMinY(double y) {
		if (this.miny != y) {
			this.miny = y;
			if (y > this.maxy) {
				this.maxy = y;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getMaxY() {
		return this.maxy;
	}

	@Override
	public void setMaxY(double y) {
		if (this.maxy != y) {
			this.maxy = y;
			if (y < this.miny) {
				this.miny = y;
			}
			fireGeometryChange();
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
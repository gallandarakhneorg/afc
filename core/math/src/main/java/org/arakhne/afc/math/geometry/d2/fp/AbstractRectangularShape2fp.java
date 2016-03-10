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
public abstract class AbstractRectangularShape2fp<IT extends AbstractRectangularShape2fp<?>>
	extends AbstractShape2fp<IT>
	implements RectangularShape2afp<Shape2fp<?>, IT, PathElement2fp, Point2fp, Rectangle2fp> {

	private static final long serialVersionUID = 562658629477723655L;

	private double minx;

	private double miny;

	private double maxx;

	private double maxy;
	
	/**
	 */
	public AbstractRectangularShape2fp() {
		//
	}

	/**
	 * @param r
	 */
	public AbstractRectangularShape2fp(RectangularShape2afp<?, ?, ?, ?, ?> r) {
		this.minx = r.getMinX();
		this.miny = r.getMinY();
		this.maxx = r.getMaxX();
		this.maxy = r.getMaxY();
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
	public boolean equals(Object obj) {
		try {
			if (getClass().isAssignableFrom(obj.getClass())) {
				RectangularShape2afp<?, ?, ?, ?, ?> shape = (RectangularShape2afp<?, ?, ?, ?, ?>) obj;
				return this.minx == shape.getMinX()
						&& this.miny == shape.getMinY()
						&& this.maxx == shape.getMaxX()
						&& this.maxy == shape.getMaxY();
			}
		} catch (Throwable exception) {
			//
		}
		return false;
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
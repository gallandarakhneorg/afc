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
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.RectangularShape2ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** A rectangle with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Rectangle2i extends AbstractShape2i<Rectangle2i>
	implements Rectangle2ai<Shape2i<?>, Rectangle2i, PathElement2i, Point2i, Rectangle2i> {

	private static final long serialVersionUID = -527939826840504763L;

	private int minx;

	private int miny;

	private int maxx;

	private int maxy;
	

	/**
	 */
	public Rectangle2i() {
		super();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2i(Point2D min, Point2D max) {
		setFromCorners(min.ix(), min.iy(), max.ix(), max.iy());
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectangle2i(int x, int y, int width, int height) {
		setFromCorners(x, y, x + width, y + height);
	}
	
	/**
	 * @param r
	 */
	public Rectangle2i(Rectangle2i r) {
		set(r);
	}

	@Override
	public void setFromCorners(int x1, int y1, int x2, int y2) {
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
	public int getMinX() {
		return this.minx;
	}

	@Override
	public void setMinX(int x) {
		if (x <= this.maxx) {
			this.minx = x;
		} else {
			this.minx = this.maxx;
			this.maxx = x;
		}
	}

	@Pure
	@Override
	public int getMaxX() {
		return this.maxx;
	}

	@Override
	public void setMaxX(int x) {
		if (x > this.minx) {
			this.maxx = x;
		} else {
			this.maxx = this.minx;
			this.minx = x;
		}
	}

	@Pure
	@Override
	public int getMinY() {
		return this.miny;
	}

	@Override
	public void setMinY(int y) {
		if (y <= this.maxy) {
			this.miny = y;
		} else {
			this.miny = this.maxy;
			this.maxy = y;
		}
	}

	@Pure
	@Override
	public int getMaxY() {
		return this.maxy;
	}

	@Override
	public void setMaxY(int y) {
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
				RectangularShape2ai<?, ?, ?, ?, ?> shape = (RectangularShape2ai<?, ?, ?, ?, ?>) obj;
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
		int bits = 1;
		bits = 31 * bits + this.minx;
		bits = 31 * bits + this.miny;
		bits = 31 * bits + this.maxx;
		bits = 31 * bits + this.maxy;
		return bits ^ (bits >> 32);
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

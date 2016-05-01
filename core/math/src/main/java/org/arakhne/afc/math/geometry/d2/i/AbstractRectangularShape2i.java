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

import org.arakhne.afc.math.geometry.d2.ai.RectangularShape2ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** A rectangular shape with 2 integer numbers.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractRectangularShape2i<IT extends AbstractRectangularShape2i<?>>
	extends AbstractShape2i<IT>
	implements RectangularShape2ai<Shape2i<?>, IT, PathElement2i, Point2i, Vector2i, Rectangle2i> {

	private static final long serialVersionUID = -6441506445885829836L;

	private int minx;

	private int miny;

	private int maxx;

	private int maxy;

	/**
	 */
	public AbstractRectangularShape2i() {
		//
	}
	
	/**
	 * @param r
	 */
	public AbstractRectangularShape2i(RectangularShape2ai<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		this.minx = r.getMinX();
		this.miny = r.getMinY();
		this.maxx = r.getMaxX();
		this.maxy = r.getMaxY();
	}

	@Override
	public void setFromCorners(int x1, int y1, int x2, int y2) {
		int a, b, c, d;
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
	public int getMinX() {
		return this.minx;
	}

	@Override
	public void setMinX(int x) {
		if (this.minx != x) {
			this.minx = x;
			if (this.maxx < x) {
				this.maxx = x;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMaxX() {
		return this.maxx;
	}

	@Override
	public void setMaxX(int x) {
		if (this.maxx != x) {
			this.maxx = x;
			if (this.minx > x) {
				this.minx = x;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMinY() {
		return this.miny;
	}

	@Override
	public void setMinY(int y) {
		if (this.miny != y) {
			this.miny = y;
			if (this.maxy < y) {
				this.maxy = y;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMaxY() {
		return this.maxy;
	}

	@Override
	public void setMaxY(int y) {
		if (this.maxy != y) {
			this.maxy = y;
			if (this.miny > y) {
				this.miny = y;
			}
			fireGeometryChange();
		}
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

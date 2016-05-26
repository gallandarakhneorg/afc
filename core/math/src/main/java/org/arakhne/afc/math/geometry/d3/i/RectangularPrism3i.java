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

package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** A rectangle with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class RectangularPrism3i extends AbstractShape3i<RectangularPrism3i>
	implements RectangularPrism3ai<Shape3i<?>, RectangularPrism3i, PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	private static final long serialVersionUID = -527939826840504763L;

	private int minx;

	private int miny;

	private int minz;

	private int maxx;

	private int maxy;

	private int maxz;
	

	/**
	 */
	public RectangularPrism3i() {
		super();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public RectangularPrism3i(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert (min != null) : "Minimum corner must be not null"; //$NON-NLS-1$
		assert (max != null) : "Maximum corner must be not null"; //$NON-NLS-1$
		setFromCorners(min.ix(), min.iy(), min.iz(), max.ix(), max.iy(), max.iz());
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public RectangularPrism3i(int x, int y, int z, int width, int height, int depth) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (depth >= 0.) : "Depth must be positive or zero"; //$NON-NLS-1$
		set(x, y, z, width, height, depth);
	}
	
	/**
	 * @param r
	 */
	public RectangularPrism3i(RectangularPrism3i r) {
		set(r);
	}

	@Override
	public void setFromCorners(int x1, int y1, int z1, int x2, int y2, int z2) {
		int a, b, c, d, e, f;
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
		if (z1 <= z2) {
			e = z1;
			f = z2;
		} else {
			e = z2;
			f = z1;
		}
		if (this.minx != a || this.maxx != b || this.miny != c || this.maxy != d || this.minz != e || this.maxz != f) {
			this.minx = a;
			this.maxx = b;
			this.miny = c;
			this.maxy = d;
			this.minz = e;
			this.maxz = f;
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
	public int getMinZ() {
		return this.minz;
	}

	@Override
	public void setMinZ(int z) {
		if (this.minz != z) {
			this.minz = z;
			if (this.maxz < z) {
				this.maxz = z;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMaxZ() {
		return this.maxz;
	}

	@Override
	public void setMaxZ(int z) {
		if (this.maxz != z) {
			this.maxz = z;
			if (this.minz > z) {
				this.minz = z;
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
		bits = 31 * bits + this.minz;
		bits = 31 * bits + this.maxx;
		bits = 31 * bits + this.maxy;
		bits = 31 * bits + this.maxz;
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
		b.append(getMinZ());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxZ());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

}

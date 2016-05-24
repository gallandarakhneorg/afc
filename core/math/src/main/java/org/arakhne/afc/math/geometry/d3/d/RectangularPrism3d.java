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

package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** A rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class RectangularPrism3d extends AbstractShape3d<RectangularPrism3d>
	implements RectangularPrism3afp<Shape3d<?>, RectangularPrism3d, PathElement3d, Point3d, Vector3d, RectangularPrism3d> {

	private static final long serialVersionUID = -2138921378214589458L;

	private double minx;

	private double miny;

	private double minz;

	private double maxx;

	private double maxy;

	private double maxz;
	

	/**
	 */
	public RectangularPrism3d() {
		super();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public RectangularPrism3d(Point3D<?, ?> min, Point3D<?, ?> max) {
		setFromCorners(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 * @param width
	 * @param height
	 * @param depth 
	 */
	public RectangularPrism3d(double x, double y, double z, double width, double height, double depth) {
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}
	
	/**
	 * @param r
	 */
	public RectangularPrism3d(RectangularPrism3d r) {
		set(r);
	}

	@Override
	public void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2) {
		double a, b, c, d, e, f;
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
	public double getMinX() {
		return this.minx;
	}

	@Override
	public void setMinX(double x) {
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
	public double getMaxX() {
		return this.maxx;
	}

	@Override
	public void setMaxX(double x) {
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
	public double getMinY() {
		return this.miny;
	}
	
	@Override
	public void setMinY(double y) {
		if (this.miny != y) {
			this.miny = y;
			if (this.maxy < y)  {
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
			if (this.miny > y) {
				this.miny = y;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getMinZ() {
		return this.minz;
	}

	@Override
	public void setMinZ(double z) {
		if (this.minz != z) {
			this.minz = z;
			if (this.maxz < z)  {
				this.maxz = z;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getMaxZ() {
		return this.maxz;
	}

	@Override
	public void setMaxZ(double z) {
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
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.minx);
		bits = 31 * bits + Double.doubleToLongBits(this.miny);
		bits = 31 * bits + Double.doubleToLongBits(this.minz);
		bits = 31 * bits + Double.doubleToLongBits(this.maxx);
		bits = 31 * bits + Double.doubleToLongBits(this.maxy);
		bits = 31 * bits + Double.doubleToLongBits(this.maxz);
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

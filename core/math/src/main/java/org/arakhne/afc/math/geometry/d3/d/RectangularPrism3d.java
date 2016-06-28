/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;

/** A rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
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


	/** Construct an empty rectangular prism.
     */
	public RectangularPrism3d() {
		super();
	}

	/** Construct a rectangular prism with  the given minimum and maxium corners.
     *
     * @param min is the min corner of the rectangular prism.
     * @param max is the max corner of the rectangular prism.
     */
	public RectangularPrism3d(Point3D<?, ?> min, Point3D<?, ?> max) {
		setFromCorners(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
	}

	/** Construct a rectangle with the given minimum corner and sizes.
     * @param x x coordinate of the minimum corner.
     * @param y y coordinate of the minimum corner.
     * @param z z coordinate of the minimum corner.
     * @param width width of the rectangular prism.
     * @param height height of the rectangular prism.
     * @param depth depth of the rectangular prism.
     */
	public RectangularPrism3d(double x, double y, double z, double width, double height, double depth) {
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}

	/** Constructor by copy.
     * @param rectangularPrism the rectangular prism to copy.
     */
	public RectangularPrism3d(RectangularPrism3d rectangularPrism) {
		set(rectangularPrism);
	}

	@Override
	public void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2) {
		final double a;
		final double b;
		final double c;
		final double d;
		final double e;
		final double f;
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
		final int b = (int) bits;
		return b;
	}

}

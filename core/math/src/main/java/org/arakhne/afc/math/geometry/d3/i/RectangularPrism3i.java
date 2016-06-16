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

package org.arakhne.afc.math.geometry.d3.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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


	/** Construct an empty rectangular prism.
     */
	public RectangularPrism3i() {
		super();
	}

	/** Construct a rectangular prism with the given minimum and maximum corners.
	 * @param min is the min corner of the rectangular prism.
	 * @param max is the max corner of the rectangular prism.
	 */
	public RectangularPrism3i(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert min != null : AssertMessages.notNullParameter(0);
		assert max != null : AssertMessages.notNullParameter(1);
		setFromCorners(min.ix(), min.iy(), min.iz(), max.ix(), max.iy(), max.iz());
	}

	/** Construct a rectangle with the given minimum corner and sizes.
     * @param x x coordinate of the minimum corner.
     * @param y y coordinate of the minimum corner.
     * @param z z coordinate of the minimum corner.
     * @param width width of the rectangular prism.
     * @param height height of the rectangular prism.
     * @param depth depth of the rectangular prism.
     */
	@SuppressWarnings("checkstyle:magicnumber")
	public RectangularPrism3i(int x, int y, int z, int width, int height, int depth) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0. : AssertMessages.positiveOrZeroParameter(5);
		set(x, y, z, width, height, depth);
	}

	/**Constructor by copy.
	 * @param rectangle the rectangular prism to copy.
	 */
	public RectangularPrism3i(RectangularPrism3i rectangle) {
		set(rectangle);
	}

	@Override
	public void setFromCorners(int x1, int y1, int z1, int x2, int y2, int z2) {
		final int a;
		final int b;
		final int c;
		final int d;
		final int e;
		final int f;
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
		return bits ^ (bits >> 31);
	}

}

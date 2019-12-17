/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.afp.Rectangle1afp;

/** A rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Rectangle1d extends AbstractShape1d<Rectangle1d>
		implements Rectangle1afp<Shape1d<?>, Rectangle1d, Point1d, Vector1d, Segment1D<?, ?>, Rectangle1d> {

	private static final long serialVersionUID = -2138921378214589458L;

	private double minx;

	private double miny;

	private double maxx;

	private double maxy;

	/** Construct an empty rectangle.
	 */
	public Rectangle1d() {
		super();
	}

	/** Construct a rectangle with  the given minimum and maxium corners.
	 *
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle1d(Point1D<?, ?, ?> min, Point1D<?, ?, ?> max) {
		super(min.getSegment());
		setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}

	/** Construct a rectangle with the given minimum corner and sizes.
	 * @param segment the segment.
	 * @param x x coordinate of the minimum corner.
	 * @param y y coordinate of the minimum corner.
	 * @param width width of the rectangle.
	 * @param height height of the rectangle.
	 */
	public Rectangle1d(Segment1D<?, ?> segment, double x, double y, double width, double height) {
		super(segment);
		setFromCorners(x, y, x + width, y + height);
	}

	/** Constructor by copy.
	 * @param rectangle the rectangle to copy.
	 */
	public Rectangle1d(Rectangle1d rectangle) {
		set(rectangle);
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		final double a;
		final double b;
		final double c;
		final double d;
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
		}
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.minx);
		bits = 31 * bits + Double.hashCode(this.miny);
		bits = 31 * bits + Double.hashCode(this.maxx);
		bits = 31 * bits + Double.hashCode(this.maxy);
        return bits ^ (bits >> 31);
	}

}

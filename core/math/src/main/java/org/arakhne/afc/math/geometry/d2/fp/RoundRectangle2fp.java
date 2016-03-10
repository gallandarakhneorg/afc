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
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** A round rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class RoundRectangle2fp extends AbstractRectangularShape2fp<RoundRectangle2fp>
	implements RoundRectangle2afp<Shape2fp<?>, RoundRectangle2fp, PathElement2fp, Point2fp, Rectangle2fp> {

	private static final long serialVersionUID = -6419985193487310000L;

	private double arcWidth;

	private double arcHeight;

	/**
	 */
	public RoundRectangle2fp() {
		super();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth
	 * @param arcHeight
	 */
	public RoundRectangle2fp(Point2D min, Point2D max, double arcWidth, double arcHeight) {
		this(min.getX(), min.getY(), max.getX(), max.getY(), arcWidth, arcHeight);
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2fp(RoundRectangle2afp<?, ?, ?, ?, ?> rr) {
		this(rr.getMinX(), rr.getMinY(), rr.getMaxX(), rr.getMaxY(), rr.getArcWidth(), rr.getArcHeight());
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2fp(RectangularShape2afp<?, ?, ?, ?, ?> rr) {
		this(rr.getMinX(), rr.getMinY(), rr.getMaxX(), rr.getMaxY(), 0, 0);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth1
	 * @param arcHeight1
	 */
	public RoundRectangle2fp(double x, double y, double width, double height, double arcWidth1, double arcHeight1) {
		set(x, y, width, height, arcWidth1, arcHeight1);
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		try {
			RoundRectangle2afp<?, ?, ?, ?, ?> shape = (RoundRectangle2afp<?, ?, ?, ?, ?>) obj;
			return this.arcWidth == shape.getArcWidth()
					&& this.arcHeight == shape.getArcHeight();
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = super.hashCode();
		bits = 31 * bits + Double.doubleToLongBits(this.arcWidth);
		bits = 31 * bits + Double.doubleToLongBits(this.arcHeight);
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
		b.append("|"); //$NON-NLS-1$
		b.append(getArcWidth());
		b.append("x"); //$NON-NLS-1$
		b.append(getArcHeight());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public double getArcWidth() {
		return this.arcWidth;
	}

	@Pure
	@Override
	public double getArcHeight() {
		return this.arcHeight;
	}

	@Override
	public void setArcWidth(double a) {
		this.arcWidth = Math.max(0, a);
	}

	@Override
	public void setArcHeight(double a) {
		this.arcHeight = Math.max(0,  a);
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2, double arcWidth, double arcHeight) {
		this.arcWidth = Math.max(0, arcWidth);
		this.arcHeight = Math.max(0,  arcHeight);
		super.setFromCorners(x1, y1, x2, y2);
	}

}

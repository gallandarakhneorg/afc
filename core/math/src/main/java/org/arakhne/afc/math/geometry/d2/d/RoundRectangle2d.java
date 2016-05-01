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
public class RoundRectangle2d extends AbstractRectangularShape2d<RoundRectangle2d>
	implements RoundRectangle2afp<Shape2d<?>, RoundRectangle2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = -6419985193487310000L;

	private double arcWidth;

	private double arcHeight;

	/**
	 */
	public RoundRectangle2d() {
		super();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth
	 * @param arcHeight
	 */
	public RoundRectangle2d(Point2D<?, ?> min, Point2D<?, ?> max, double arcWidth, double arcHeight) {
		this(min.getX(), min.getY(), max.getX(), max.getY(), arcWidth, arcHeight);
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2d(RoundRectangle2afp<?, ?, ?, ?, ?, ?> rr) {
		this(rr.getMinX(), rr.getMinY(), rr.getMaxX(), rr.getMaxY(), rr.getArcWidth(), rr.getArcHeight());
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2d(RectangularShape2afp<?, ?, ?, ?, ?, ?> rr) {
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
	public RoundRectangle2d(double x, double y, double width, double height, double arcWidth1, double arcHeight1) {
		set(x, y, width, height, arcWidth1, arcHeight1);
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
		assert (a >= 0) : "Arc width must be positive or zero"; //$NON-NLS-1$
		this.arcWidth = Math.min(getWidth() / 2, a);
	}

	@Override
	public void setArcHeight(double a) {
		assert (a >= 0) : "Arc height must be positive or zero"; //$NON-NLS-1$
		this.arcHeight = Math.min(getHeight() / 2, a);
	}
	
	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		super.setFromCorners(x1, y1, x2, y2);
		ensureValidArcWidth();
		ensureValidArcHeight();
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2, double arcWidth, double arcHeight) {
		super.setFromCorners(x1, y1, x2, y2);
		setArcWidth(arcWidth);
		setArcHeight(arcHeight);
	}
	
	/** Ensure that the size of the arc width is valid, i.e. not too big or too small.
	 */
	protected void ensureValidArcWidth() {
		double halfWidth = getWidth() / 2;
		if (this.arcWidth > halfWidth) {
			this.arcWidth = halfWidth;
		}
	}
	
	/** Ensure that the size of the arc height is valid, i.e. not too big or too small.
	 */
	protected void ensureValidArcHeight() {
		double halfHeight = getHeight() / 2;
		if (this.arcHeight > halfHeight) {
			this.arcHeight = halfHeight;
		}
	}

	@Override
	public void setMinX(double x) {
		super.setMinX(x);
		ensureValidArcWidth();
	}
	
	@Override
	public void setMaxX(double x) {
		super.setMaxX(x);
		ensureValidArcWidth();
	}

	@Override
	public void setMinY(double y) {
		super.setMinY(y);
		ensureValidArcHeight();
	}
	
	@Override
	public void setMaxY(double y) {
		super.setMaxY(y);
		ensureValidArcHeight();
	}

}

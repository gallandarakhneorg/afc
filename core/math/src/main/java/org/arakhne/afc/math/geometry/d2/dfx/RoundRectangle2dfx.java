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

package org.arakhne.afc.math.geometry.d2.dfx;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;

/** Round rectangle with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class RoundRectangle2dfx extends AbstractRectangularShape2dfx<RoundRectangle2dfx>
	implements RoundRectangle2afp<Shape2dfx<?>, RoundRectangle2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

	private static final long serialVersionUID = -2020546629513913417L;

	private DoubleProperty arcWidth;

	private DoubleProperty arcHeight;

	/**
	 */
	public RoundRectangle2dfx() {
		super();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth
	 * @param arcHeight
	 */
	public RoundRectangle2dfx(Point2D<?, ?> min, Point2D<?, ?> max, double arcWidth, double arcHeight) {
		this(min.getX(), min.getY(), max.getX(), max.getY(), arcWidth, arcHeight);
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2dfx(RoundRectangle2afp<?, ?, ?, ?, ?, ?> rr) {
		this(rr.getMinX(), rr.getMinY(), rr.getMaxX(), rr.getMaxY(), rr.getArcWidth(), rr.getArcHeight());
	}

	/**
	 * @param rr
	 */
	public RoundRectangle2dfx(RectangularShape2afp<?, ?, ?, ?, ?, ?> rr) {
		this(rr.getMinX(), rr.getMinY(), rr.getMaxX(), rr.getMaxY(), 0, 0);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth
	 * @param arcHeight
	 */
	public RoundRectangle2dfx(double x, double y, double width, double height, double arcWidth, double arcHeight) {
		set(x, y, width, height, arcWidth, arcHeight);
	}
	
	@Override
	public RoundRectangle2dfx clone() {
		RoundRectangle2dfx clone = super.clone();
		if (clone.arcWidth != null) {
			clone.arcWidth = null;
			clone.arcWidthProperty().set(getArcWidth());
		}
		if (clone.arcHeight != null) {
			clone.arcHeight = null;
			clone.arcHeightProperty().set(getArcHeight());
		}
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = super.hashCode();
		bits = 31 * bits + Double.doubleToLongBits(getArcWidth());
		bits = 31 * bits + Double.doubleToLongBits(getArcHeight());
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
		return this.arcWidth == null ? 0 : this.arcWidth.get();
	}

	/** Replies the property for the arc width.
	 *
	 * @return the arcWidth property.
	 */
	public DoubleProperty arcWidthProperty() {
		if (this.arcWidth == null) {
			this.arcWidth = new DependentSimpleDoubleProperty<ReadOnlyDoubleProperty>(
					this, "arcWidth", widthProperty()) { //$NON-NLS-1$
				@Override
				protected void invalidated(ReadOnlyDoubleProperty dependency) {
					double value = get();
					if (value < 0.) {
						set(0.);
					} else {
						double maxArcWidth = dependency.get() / 2.;
						if (value > maxArcWidth) {
							set(maxArcWidth);
						}
					}
				}
			};
		}
		return this.arcWidth;
	}
	
	@Pure
	@Override
	public double getArcHeight() {
		return this.arcHeight == null ? 0 : this.arcHeight.get();
	}

	/** Replies the property for the arc height.
	 *
	 * @return the arcHeight property.
	 */
	public DoubleProperty arcHeightProperty() {
		if (this.arcHeight == null) {
			this.arcHeight = new DependentSimpleDoubleProperty<ReadOnlyDoubleProperty>(
					this, "arcHeight", heightProperty()) { //$NON-NLS-1$
				@Override
				protected void invalidated(ReadOnlyDoubleProperty dependency) {
					double value = get();
					if (value < 0.) {
						set(0.);
					} else {
						double maxArcHeight = dependency.get() / 2.;
						if (value > maxArcHeight) {
							set(maxArcHeight);
						}
					}
				}
			};
		}
		return this.arcHeight;
	}

	@Override
	public void setArcWidth(double a) {
		assert (a >= 0.) : "Arc width must be positive or zero"; //$NON-NLS-1$
		arcWidthProperty().set(a);
	}

	@Override
	public void setArcHeight(double a) {
		assert (a >= 0.) : "Arc height must be positive or zero"; //$NON-NLS-1$
		arcHeightProperty().set(a);
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		double arcWidth = getArcWidth();
		double arcHeight = getArcHeight();
		setFromCorners(x1, y1, x2, y2, arcWidth, arcHeight);
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2, double arcWidth, double arcHeight) {
		assert (arcWidth >= 0.) : "Arc width must be positive or zero"; //$NON-NLS-1$
		assert (arcHeight >= 0.) : "Arc height must be positive or zero"; //$NON-NLS-1$
		if (x1 <= x2) {
			minXProperty().set(x1);
			maxXProperty().set(x2);
		} else {
			minXProperty().set(x2);
			maxXProperty().set(x1);
		}
		if (y1 <= y2) {
			minYProperty().set(y1);
			maxYProperty().set(y2);
		} else {
			minYProperty().set(y2);
			maxYProperty().set(y1);
		}
		setArcWidth(arcWidth);
		setArcHeight(arcHeight);
	}

}

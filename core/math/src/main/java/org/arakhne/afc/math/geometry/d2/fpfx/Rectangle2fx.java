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

package org.arakhne.afc.math.geometry.d2.fpfx;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

/** Rectangle with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Rectangle2fx extends AbstractShape2fx<Rectangle2fx>
	implements Rectangle2afp<Shape2fx<?>, Rectangle2fx, PathElement2fx, Point2fx, Rectangle2fx> {

	private static final long serialVersionUID = -1393290109630714626L;

	private DoubleProperty minX;

	private DoubleProperty minY;

	private DoubleProperty maxX;

	private DoubleProperty maxY;
		
	/**
	 */
	public Rectangle2fx() {
		//
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2fx(Point2D min, Point2D max) {
		setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectangle2fx(double x, double y, double width, double height) {
		setFromCorners(x, y, x + width, y + height);
	}
	
	/**
	 * @param r
	 */
	public Rectangle2fx(Rectangle2fx r) {
		set(r);
	}
	
	@Override
	public Rectangle2fx clone() {
		Rectangle2fx clone = super.clone();
		if (clone.minX != null) {
			double value = clone.minX.get();
			clone.minX = null;
			clone.setMinX(value);
		}
		if (clone.maxX != null) {
			double value = clone.maxX.get();
			clone.maxX = null;
			clone.setMaxX(value);
		}
		if (clone.minY != null) {
			double value = clone.minY.get();
			clone.minY = null;
			clone.setMinY(value);
		}
		if (clone.maxY != null) {
			double value = clone.maxY.get();
			clone.maxY = null;
			clone.setMaxY(value);
		}
		return clone;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		try {
			RectangularShape2afp<?, ?, ?, ?, ?> shape = (RectangularShape2afp<?, ?, ?, ?, ?>) obj;
			return getMinX() == shape.getMinX()
					&& getMinY() == shape.getMinY()
					&& getMaxX() == shape.getMaxX()
					&& getMaxY() == shape.getMaxY();
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getMinX());
		bits = 31 * bits + Double.doubleToLongBits(getMinY());
		bits = 31 * bits + Double.doubleToLongBits(getMaxX());
		bits = 31 * bits + Double.doubleToLongBits(getMaxY());
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

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		minXProperty().set(x1);
		maxXProperty().set(x2);
		minYProperty().set(y1);
		maxYProperty().set(y2);
	}

	@Pure
	@Override
	public double getMinX() {
		return this.minX == null ? 0 : this.minX.get();
	}
	
	@Override
	public void setMinX(double x) {
		minXProperty().set(x);
	}

	/** Replies the property that is the minimum x coordinate of the box.
	 *
	 * @return the minX property.
	 */
	@Pure
	public DoubleProperty minXProperty() {
		if (this.minX == null) {
			this.minX = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "minX"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return Rectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					double currentMin = get();
					double currentMax = getMaxX();
					if (currentMin > currentMax) {
						// min-max constrain is broken
						set(currentMax);
						maxXProperty().set(currentMin);
					}
				}
			};
		}
		return this.minX;
	}

	@Pure
	@Override
	public double getMaxX() {
		return this.maxX == null ? 0 : this.maxX.get();
	}
	
	@Override
	public void setMaxX(double x) {
		maxXProperty().set(x);
	}

	/** Replies the property that is the maximum x coordinate of the box.
	 *
	 * @return the maxX property.
	 */
	@Pure
	public DoubleProperty maxXProperty() {
		if (this.maxX == null) {
			this.maxX = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "maxX"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return Rectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					double currentMax = get();
					double currentMin = getMinX();
					if (currentMin > currentMax) {
						// min-max constrain is broken
						set(currentMin);
						minXProperty().set(currentMax);
					}
				}
			};
		}
		return this.maxX;
	}

	@Pure
	@Override
	public double getMinY() {
		return this.minY == null ? 0 : this.minY.get();
	}
	
	@Override
	public void setMinY(double y) {
		minYProperty().set(y);
	}

	/** Replies the property that is the minimum y coordinate of the box.
	 *
	 * @return the minY property.
	 */
	@Pure
	public DoubleProperty minYProperty() {
		if (this.minY == null) {
			this.minY = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "minY"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return Rectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					double currentMin = get();
					double currentMax = getMaxY();
					if (currentMin > currentMax) {
						// min-max constrain is broken
						set(currentMax);
						maxYProperty().set(currentMin);
					}
				}
			};
		}
		return this.minY;
	}

	@Pure
	@Override
	public double getMaxY() {
		return this.maxY == null ? 0 : this.maxY.get();
	}
	
	@Override
	public void setMaxY(double y) {
		maxYProperty().set(y);
	}

	/** Replies the property that is the maximum y coordinate of the box.
	 *
	 * @return the maxY property.
	 */
	@Pure
	public DoubleProperty maxYProperty() {
		if (this.maxY == null) {
			this.maxY = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "maxY"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return Rectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					double currentMax = get();
					double currentMin = getMinY();
					if (currentMin > currentMax) {
						// min-max constrain is broken
						set(currentMin);
						minYProperty().set(currentMax);
					}
				}
			};
		}
		return this.maxY;
	}

}

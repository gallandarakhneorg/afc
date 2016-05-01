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
package org.arakhne.afc.math.geometry.d2.fx;

import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/** Abstract rectangular shape with 2 double precision floating-point FX properties.
 * 
 * @param <IT> is the type of the implementation of this shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractRectangularShape2fx<IT extends AbstractRectangularShape2fx<?>>
	extends AbstractShape2fx<IT>
	implements RectangularShape2afp<Shape2fx<?>, IT, PathElement2fx, Point2fx, Vector2fx, Rectangle2fx> {

	private static final long serialVersionUID = 562658629477723655L;

	/** minX property.
	 */
	DoubleProperty minX;

	/** minY property.
	 */
	DoubleProperty minY;

	/** maxX property.
	 */
	DoubleProperty maxX;

	/** maxY property.
	 */
	DoubleProperty maxY;
	
	/** width property.
	 */
	DoubleProperty width;
	
	/** height property.
	 */
	DoubleProperty height;

	/**
	 */
	public AbstractRectangularShape2fx() {
		//
	}
	
	/**
	 * @param r
	 */
	public AbstractRectangularShape2fx(RectangularShape2afp<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		setFromCorners(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY());
	}

	@Override
	public IT clone() {
		IT clone = super.clone();
		if (clone.minX != null) {
			clone.minX = null;
			clone.minXProperty().set(getMinX());
		}
		if (clone.minY != null) {
			clone.minY = null;
			clone.minYProperty().set(getMinY());
		}
		if (clone.maxX != null) {
			clone.maxX = null;
			clone.maxXProperty().set(getMaxX());
		}
		if (clone.maxY != null) {
			clone.maxY = null;
			clone.maxYProperty().set(getMaxY());
		}
		clone.width = null;
		clone.height = null;
		return clone;
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
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
			this.minX = new SimpleDoubleProperty(this, "minX") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					double currentMin = get();
					double currentMax = getMaxX();
					if (currentMin > currentMax) {
						// min-max constrain is broken
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
			this.maxX = new SimpleDoubleProperty(this, "maxX") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					double currentMax = get();
					double currentMin = getMinX();
					if (currentMin > currentMax) {
						// min-max constrain is broken
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
			this.minY = new SimpleDoubleProperty(this, "minY") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					double currentMin = get();
					double currentMax = getMaxY();
					if (currentMin > currentMax) {
						// min-max constrain is broken
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
			this.maxY = new SimpleDoubleProperty(this, "maxY") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					double currentMax = get();
					double currentMin = getMinY();
					if (currentMin > currentMax) {
						// min-max constrain is broken
						minYProperty().set(currentMax);
					}
				}
			};
		}
		return this.maxY;
	}
	
	@Override
	public double getWidth() {
		return widthProperty().get();
	}
	
	/** Replies the property that is the width of the box.
	 *
	 * @return the width property.
	 */
	@Pure
	public DoubleProperty widthProperty() {
		if (this.width == null) {
			this.width = new SimpleDoubleProperty(this, "width"); //$NON-NLS-1$
			this.width.bind(Bindings.subtract(maxXProperty(), minXProperty()));
		}
		return this.width;
	}
	
	@Override
	public double getHeight() {
		return heightProperty().get();
	}

	/** Replies the property that is the height of the box.
	 *
	 * @return the height property.
	 */
	@Pure
	public DoubleProperty heightProperty() {
		if (this.height == null) {
			this.height = new SimpleDoubleProperty(this, "height"); //$NON-NLS-1$
			this.height.bind(Bindings.subtract(maxYProperty(), minYProperty()));
		}
		return this.height;
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
	public ObjectProperty<Rectangle2fx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						return toBoundingBox();
					},
					minXProperty(), minYProperty(), widthProperty(), heightProperty()));
		}
		return this.boundingBox;
	}

}
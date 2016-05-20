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

package org.arakhne.afc.math.geometry.d2.ifx;

import org.arakhne.afc.math.geometry.d2.ai.RectangularShape2ai;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;

/** A rectangular shape with 2 integer FX properties.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractRectangularShape2ifx<IT extends AbstractRectangularShape2ifx<?>>
	extends AbstractShape2ifx<IT>
	implements RectangularShape2ai<Shape2ifx<?>, IT, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -6551989261232962403L;

	/** minX property.
	 */
	IntegerProperty minX;

	/** minY property.
	 */
	IntegerProperty minY;

	/** maxX property.
	 */
	IntegerProperty maxX;

	/** maxY property.
	 */
	IntegerProperty maxY;

	/** width property.
	 */
	ReadOnlyIntegerWrapper width;
	
	/** height property.
	 */
	ReadOnlyIntegerWrapper height;

	/**
	 */
	public AbstractRectangularShape2ifx() {
		//
	}
	
	/**
	 * @param r
	 */
	public AbstractRectangularShape2ifx(RectangularShape2ai<?, ?, ?, ?, ?, ?> r) {
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
	public void setFromCorners(int x1, int y1, int x2, int y2) {
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
	public int getMinX() {
		return this.minX == null ? 0 : this.minX.get();
	}

	@Override
	public void setMinX(int x) {
		minXProperty().set(x);
	}

	/** Replies the property that is the minimum x coordinate of the box.
	 *
	 * @return the minX property.
	 */
	@Pure
	public IntegerProperty minXProperty() {
		if (this.minX == null) {
			this.minX = new SimpleIntegerProperty(this, "minX") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					int currentMin = get();
					int currentMax = getMaxX();
					if (currentMax < currentMin) {
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
	public int getMaxX() {
		return this.maxX == null ? 0 : this.maxX.get();
	}

	@Override
	public void setMaxX(int x) {
		maxXProperty().set(x);
	}

	/** Replies the property that is the maximum x coordinate of the box.
	 *
	 * @return the maxX property.
	 */
	@Pure
	public IntegerProperty maxXProperty() {
		if (this.maxX == null) {
			this.maxX = new SimpleIntegerProperty(this, "maxX") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					int currentMax = get();
					int currentMin = getMinX();
					if (currentMax < currentMin) {
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
	public int getMinY() {
		return this.minY == null ? 0 : this.minY.get();
	}

	@Override
	public void setMinY(int y) {
		minYProperty().set(y);
	}

	/** Replies the property that is the minimum y coordinate of the box.
	 *
	 * @return the minY property.
	 */
	@Pure
	public IntegerProperty minYProperty() {
		if (this.minY == null) {
			this.minY = new SimpleIntegerProperty(this, "minY") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					int currentMin = get();
					int currentMax = getMaxY();
					if (currentMax < currentMin) {
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
	public int getMaxY() {
		return this.maxY == null ? 0 : this.maxY.get();
	}

	@Override
	public void setMaxY(int y) {
		maxYProperty().set(y);
	}

	/** Replies the property that is the maximum y coordinate of the box.
	 *
	 * @return the maxY property.
	 */
	@Pure
	public IntegerProperty maxYProperty() {
		if (this.maxY == null) {
			this.maxY = new SimpleIntegerProperty(this, "maxY") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					int currentMax = get();
					int currentMin = getMinY();
					if (currentMax < currentMin) {
						// min-max constrain is broken
						minYProperty().set(currentMax);
					}
				}
			};
		}
		return this.maxY;
	}
	
	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + getMinX();
		bits = 31 * bits + getMinY();
		bits = 31 * bits + getMaxX();
		bits = 31 * bits + getMaxY();
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
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Override
	public int getWidth() {
		return widthProperty().get();
	}
	
	/** Replies the property that is the width of the box.
	 *
	 * @return the width property.
	 */
	@Pure
	public IntegerProperty widthProperty() {
		if (this.width == null) {
			this.width = new ReadOnlyIntegerWrapper(this, "width"); //$NON-NLS-1$
			this.width.bind(Bindings.subtract(maxXProperty(), minXProperty()));
		}
		return this.width;
	}
	
	@Override
	public int getHeight() {
		return heightProperty().get();
	}

	/** Replies the property that is the height of the box.
	 *
	 * @return the height property.
	 */
	@Pure
	public IntegerProperty heightProperty() {
		if (this.height == null) {
			this.height = new ReadOnlyIntegerWrapper(this, "height"); //$NON-NLS-1$
			this.height.bind(Bindings.subtract(maxYProperty(), minYProperty()));
		}
		return this.height;
	}

}

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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A rectangle with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Rectangle2ifx extends AbstractShape2ifx<Rectangle2ifx>
		implements Rectangle2ai<Shape2ifx<?>, Rectangle2ifx, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -8092385681401129843L;

	private IntegerProperty minX;

	private IntegerProperty minY;

	private IntegerProperty maxX;

	private IntegerProperty maxY;

	/** width property.
	 */
	private ReadOnlyIntegerWrapper width;

	/** height property.
	 */
	private ReadOnlyIntegerWrapper height;

	/** Construct an empty rectangle.
	 */
	public Rectangle2ifx() {
		super();
	}

	/** Construct a rectangle with the given minimum and maximum corners.
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2ifx(Point2D<?, ?> min, Point2D<?, ?> max) {
		assert min != null : AssertMessages.notNullParameter(0);
		assert max != null : AssertMessages.notNullParameter(1);
		setFromCorners(min.ix(), min.iy(), max.ix(), max.iy());
	}

	/** Construct a rectangle with the given minimum corner and sizes.
	 * @param x x coordinate of the minimum corner.
	 * @param y y coordinate of the minimum corner.
	 * @param width width of the rectangle.
	 * @param height height of the rectangle.
	 */
	public Rectangle2ifx(int x, int y, int width, int height) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(2);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(3);
		setFromCorners(x, y, x + width, y + height);
	}

	/** Constructor by copy.
	 * @param rectangle the rectangle to copy.
	 */
	public Rectangle2ifx(Rectangle2ifx rectangle) {
		set(rectangle);
	}

	@Override
	public Rectangle2ifx clone() {
		final Rectangle2ifx clone = super.clone();
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
			this.minX = new SimpleIntegerProperty(this, MathFXAttributeNames.MINIMUM_X) {
				@Override
				protected void invalidated() {
					final int currentMin = get();
					final int currentMax = getMaxX();
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
			this.maxX = new SimpleIntegerProperty(this, MathFXAttributeNames.MAXIMUM_X) {
				@Override
				protected void invalidated() {
					final int currentMax = get();
					final int currentMin = getMinX();
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
			this.minY = new SimpleIntegerProperty(this, MathFXAttributeNames.MINIMUM_Y) {
				@Override
				protected void invalidated() {
					final int currentMin = get();
					final int currentMax = getMaxY();
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
			this.maxY = new SimpleIntegerProperty(this, MathFXAttributeNames.MAXIMUM_Y) {
				@Override
				protected void invalidated() {
					final int currentMax = get();
					final int currentMin = getMinY();
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
		bits = 31 * bits + Integer.hashCode(getMinX());
		bits = 31 * bits + Integer.hashCode(getMinY());
		bits = 31 * bits + Integer.hashCode(getMaxX());
		bits = 31 * bits + Integer.hashCode(getMaxY());
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("("); //$NON-NLS-1$
		b.append(getMinX());
		b.append(", "); //$NON-NLS-1$
		b.append(getMinY());
		b.append(")-("); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(", "); //$NON-NLS-1$
		b.append(getMaxY());
		b.append(")"); //$NON-NLS-1$
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
			this.width = new ReadOnlyIntegerWrapper(this, MathFXAttributeNames.WIDTH);
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
			this.height = new ReadOnlyIntegerWrapper(this, MathFXAttributeNames.HEIGHT);
			this.height.bind(Bindings.subtract(maxYProperty(), minYProperty()));
		}
		return this.height;
	}

	@Override
	public ObjectProperty<Rectangle2ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				return toBoundingBox();
			},
					minXProperty(), minYProperty(), widthProperty(), heightProperty()));
		}
		return this.boundingBox;
	}

}

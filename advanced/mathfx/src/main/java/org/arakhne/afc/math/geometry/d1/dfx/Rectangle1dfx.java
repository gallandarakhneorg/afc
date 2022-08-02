/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.dfx;

import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.afp.Rectangle1afp;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;

/** A rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class Rectangle1dfx extends AbstractShape1dfx<Rectangle1dfx>
		implements Rectangle1afp<Shape1dfx<?>, Rectangle1dfx, Point1dfx, Vector1dfx, Segment1D<?, ?>, Rectangle1dfx> {

	private static final long serialVersionUID = 8019832663705061476L;

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

	/** Construct an empty rectangle.
	 */
	public Rectangle1dfx() {
		super();
	}

	/** Construct a rectangle with  the given minimum and maxium corners.
	 *
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle1dfx(Point1D<?, ?, ?> min, Point1D<?, ?, ?> max) {
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
	public Rectangle1dfx(Segment1D<?, ?> segment, double x, double y, double width, double height) {
		super(segment);
		setFromCorners(x, y, x + width, y + height);
	}

	/** Constructor by copy.
	 * @param rectangle the rectangle to copy.
	 */
	public Rectangle1dfx(Rectangle1dfx rectangle) {
		set(rectangle);
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Objects.hashCode(getSegment());
		bits = 31 * bits + Double.hashCode(getMinX());
		bits = 31 * bits + Double.hashCode(getMinY());
		bits = 31 * bits + Double.hashCode(getMaxX());
		bits = 31 * bits + Double.hashCode(getMaxY());
		final int b = (int) bits;
		return b ^ (b >> 31);
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
			this.minX = new SimpleDoubleProperty(this, MathFXAttributeNames.MINIMUM_X) {
				@Override
				protected void invalidated() {
					final double currentMin = get();
					final double currentMax = getMaxX();
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
			this.maxX = new SimpleDoubleProperty(this, MathFXAttributeNames.MAXIMUM_X) {
				@Override
				protected void invalidated() {
					final double currentMax = get();
					final double currentMin = getMinX();
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
			this.minY = new SimpleDoubleProperty(this, MathFXAttributeNames.MINIMUM_Y) {
				@Override
				protected void invalidated() {
					final double currentMin = get();
					final double currentMax = getMaxY();
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
			this.maxY = new SimpleDoubleProperty(this, MathFXAttributeNames.MAXIMUM_Y) {
				@Override
				protected void invalidated() {
					final double currentMax = get();
					final double currentMin = getMinY();
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
			this.width = new SimpleDoubleProperty(this, MathFXAttributeNames.WIDTH);
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
			this.height = new SimpleDoubleProperty(this, MathFXAttributeNames.HEIGHT);
			this.height.bind(Bindings.subtract(maxYProperty(), minYProperty()));
		}
		return this.height;
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

	@Override
	public ObjectProperty<Rectangle1dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->  toBoundingBox(),
				minXProperty(), minYProperty(), widthProperty(), heightProperty()));
		}
		return this.boundingBox;
	}

}

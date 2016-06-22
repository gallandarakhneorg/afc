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
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
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

	private Point2ifx min = new Point2ifx();

	private Point2ifx max = new Point2ifx();

	/** width property.
	 */
	private ReadOnlyIntegerWrapper width;

	/** height property.
	 */
	private ReadOnlyIntegerWrapper height;

	/** Construct an empty rectangle.
	 */
	public Rectangle2ifx() {
		addListeners();
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
		if (clone.min != null) {
			clone.min = this.min.clone();
		}
		if (clone.max != null) {
			clone.max = this.max.clone();
		}
		return clone;
	}

	@Override
	public void setFromCorners(int x1, int y1, int x2, int y2) {
	    addListeners();
		if (x1 <= x2) {
			this.min.setX(x1);
			this.max.setX(x2);
		} else {
		    this.min.setX(x2);
            this.max.setX(x1);
		}
		if (y1 <= y2) {
		    this.min.setY(y1);
            this.max.setY(y2);
        } else {
            this.min.setY(y2);
            this.max.setY(y1);
		}
	}

	// TODO
	private void addListeners() {
	    this.min.xProperty().addListener((observable, oldValue, nValue) -> {
	        final int currentMin = nValue.intValue();
            final int currentMax = getMaxX();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                maxXProperty().set(currentMin);
            }
	    });
	    this.min.yProperty().addListener((observable, oValue, nValue) -> {
	        final int currentMin = nValue.intValue();
            final int currentMax = getMaxY();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                maxYProperty().set(currentMin);
            }
	    });
	    this.max.xProperty().addListener((observable, oValue, nValue) -> {
	        final int currentMax = nValue.intValue();
            final int currentMin = getMinX();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                minXProperty().set(currentMax);
            }
	    });
	    this.max.yProperty().addListener((observable, oValue, nValue) -> {
	        final int currentMax = nValue.intValue();
            final int currentMin = getMinY();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                minYProperty().set(currentMax);
            }
	    });
    }

	@Pure
	@Override
	public int getMinX() {
	    return this.min.ix();
	}

	@Override
	public void setMinX(int x) {
		this.min.setX(x);
	}

	/** Replies the property that is the minimum x coordinate of the box.
	 *
	 * @return the minX property.
	 */
	@Pure
	public IntegerProperty minXProperty() {
		return this.min.xProperty();
	}

	@Pure
	@Override
	public int getMaxX() {
		return this.max.ix();
	}

	@Override
	public void setMaxX(int x) {
		this.max.setX(x);
	}

	/** Replies the property that is the maximum x coordinate of the box.
	 *
	 * @return the max.x property.
	 */
	@Pure
	public IntegerProperty maxXProperty() {
		return this.max.xProperty();
	}

	@Pure
	@Override
	public int getMinY() {
		return this.min.iy();
	}

	@Override
	public void setMinY(int y) {
		this.min.setY(y);
	}

	/** Replies the property that is the minimum y coordinate of the box.
	 *
	 * @return the min.y property.
	 */
	@Pure
	public IntegerProperty minYProperty() {
		return this.min.yProperty();
	}

	@Pure
	@Override
	public int getMaxY() {
		return this.max.iy();
	}

	@Override
	public void setMaxY(int y) {
		this.max.setY(y);
	}

	/** Replies the property that is the maximum y coordinate of the box.
	 *
	 * @return the max.y property.
	 */
	@Pure
	public IntegerProperty maxYProperty() {
		return this.max.yProperty();
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
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
				toBoundingBox(),
					minXProperty(), minYProperty(), widthProperty(), heightProperty()));
		}
		return this.boundingBox;
	}

}

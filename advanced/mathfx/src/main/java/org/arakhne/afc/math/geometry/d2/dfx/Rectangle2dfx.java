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

package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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
public class Rectangle2dfx extends AbstractShape2dfx<Rectangle2dfx>
		implements Rectangle2afp<Shape2dfx<?>, Rectangle2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

	private static final long serialVersionUID = -1393290109630714626L;

	private Point2dfx min = new Point2dfx();

	private Point2dfx max = new Point2dfx();

	/** width property.
	 */
	private ReadOnlyDoubleWrapper width;

	/** height property.
	 */
	private ReadOnlyDoubleWrapper height;

	/** Construct an empty rectangle.
	 */
	public Rectangle2dfx() {
		addListeners();
	}

	/** Construct a rectangle with the given minimum and maximum corners.
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2dfx(Point2D<?, ?> min, Point2D<?, ?> max) {
	    assert min != null : AssertMessages.notNullParameter(0);
	    assert max != null : AssertMessages.notNullParameter(1);
	    setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}

	/** Construct a rectangle by setting the given minimum and maximum corners.
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public Rectangle2dfx(Point2dfx min, Point2dfx max) {
	    assert min != null : AssertMessages.notNullParameter(0);
	    assert max != null : AssertMessages.notNullParameter(1);
	    this.min = min;
	    this.max = max;
	}

	/** Construct a rectangle.
	 * @param x x coordinate of the minimum corner.
	 * @param y y coordinate of the minimum corner.
	 * @param width width of the rectangle.
	 * @param height height of the rectangle.
	 */
	public Rectangle2dfx(double x, double y, double width, double height) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(2);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(3);
		setFromCorners(x, y, x + width, y + height);
	}

	/** Constructor by copy.
	 * @param rectangle the shape to copy.
	 */
	public Rectangle2dfx(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
	    assert rectangle != null : AssertMessages.notNullParameter();
	    set(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY());
	}

	/** Constructor by setting.
	 * @param rectangle the shape to set.
	 */
	public Rectangle2dfx(Rectangle2dfx rectangle) {
	    assert rectangle != null : AssertMessages.notNullParameter();
		this.min = rectangle.min;
		this.max = rectangle.max;
	}

	@Override
	public Rectangle2dfx clone() {
		final Rectangle2dfx clone = super.clone();
		if (clone.min != null) {
			clone.min = null;
			clone.min = this.min.clone();
		}
		if (clone.max != null) {
			clone.max = null;
			clone.max = this.max.clone();
		}
		clone.width = null;
		clone.height = null;
		return clone;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(getMinX());
		bits = 31 * bits + Double.hashCode(getMinY());
		bits = 31 * bits + Double.hashCode(getMaxX());
		bits = 31 * bits + Double.hashCode(getMaxY());
        return bits ^ (bits >> 31);
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
		addListeners();
	}

	/**
     * Add a listener to the point properties to observe correct min-max behavior.
     */
	private void addListeners() {
	    this.min.xProperty().addListener((observable, oldValue, nValue) -> {
	        final double currentMin = nValue.doubleValue();
	        final double currentMax = getMaxX();
	        if (currentMax < currentMin) {
	            // min-max constrain is broken
	            maxXProperty().set(currentMin);
	        }
	    });
	    this.min.yProperty().addListener((observable, oValue, nValue) -> {
	        final double currentMin = nValue.doubleValue();
	        final double currentMax = getMaxY();
	        if (currentMax < currentMin) {
	            // min-max constrain is broken
	            maxYProperty().set(currentMin);
	        }
	    });
	    this.max.xProperty().addListener((observable, oValue, nValue) -> {
	        final double currentMax = nValue.doubleValue();
	        final double currentMin = getMinX();
	        if (currentMax < currentMin) {
	            // min-max constrain is broken
	            minXProperty().set(currentMax);
	        }
	    });
	    this.max.yProperty().addListener((observable, oValue, nValue) -> {
	        final double currentMax = nValue.doubleValue();
	        final double currentMin = getMinY();
	        if (currentMax < currentMin) {
	            // min-max constrain is broken
	            minYProperty().set(currentMax);
	        }
	    });
	}

	@Pure
	@Override
	public double getMinX() {
	    return this.min.getX();
	}

	@Override
	public void setMinX(double x) {
		this.min.setX(x);
	}

	/** Replies the property that is the minimum x coordinate of the box.
	 *
	 * @return the minX property.
	 */
	@Pure
	public DoubleProperty minXProperty() {
		return this.min.xProperty();
	}

	@Pure
	@Override
	public double getMaxX() {
		return this.max.getX();
	}

	@Override
	public void setMaxX(double x) {
		this.max.setX(x);
	}

	/** Replies the property that is the maximum x coordinate of the box.
	 *
	 * @return the maxX property.
	 */
	@Pure
	public DoubleProperty maxXProperty() {
		return this.max.xProperty();
	}

	@Pure
	@Override
	public double getMinY() {
		return this.min.getY();
	}

	@Override
	public void setMinY(double y) {
		this.min.setY(y);
	}

	/** Replies the property that is the minimum y coordinate of the box.
	 *
	 * @return the minY property.
	 */
	@Pure
	public DoubleProperty minYProperty() {
		return this.min.yProperty();
	}

	@Pure
	@Override
	public double getMaxY() {
		return this.max.getY();
	}

	@Override
	public void setMaxY(double y) {
		this.max.setY(y);
	}

	/** Replies the property that is the maximum y coordinate of the box.
	 *
	 * @return the maxY property.
	 */
	@Pure
	public DoubleProperty maxYProperty() {
		return this.max.yProperty();
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
			this.width = new ReadOnlyDoubleWrapper(this, MathFXAttributeNames.WIDTH);
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
			this.height = new ReadOnlyDoubleWrapper(this, MathFXAttributeNames.HEIGHT);
			this.height.bind(Bindings.subtract(maxYProperty(), minYProperty()));
		}
		return this.height;
	}

	@Override
	public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
				toBoundingBox(),
					minXProperty(), minYProperty(), widthProperty(), heightProperty()));
		}
		return this.boundingBox;
	}

}

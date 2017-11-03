/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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

	/** Construct an empty round rectangle.
	 */
	public RoundRectangle2dfx() {
		super();
	}

	/** Constructor.
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth the width of the arcs.
	 * @param arcHeight the height of the arcs.
	 */
	public RoundRectangle2dfx(Point2D<?, ?> min, Point2D<?, ?> max, double arcWidth, double arcHeight) {
		this(min.getX(), min.getY(), max.getX(), max.getY(), arcWidth, arcHeight);
	}

	/** Constructor by copy.
	 * @param shape the shape to copy.
	 */
	public RoundRectangle2dfx(RoundRectangle2afp<?, ?, ?, ?, ?, ?> shape) {
		this(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY(), shape.getArcWidth(), shape.getArcHeight());
	}

	/** Constructor by copy.
	 * @param shape the shape to copy.
	 */
	public RoundRectangle2dfx(RectangularShape2afp<?, ?, ?, ?, ?, ?> shape) {
		this(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY(), 0, 0);
	}

	/** Construct a round rectangle.
	 * @param x x coordinate of the minimum corner.
	 * @param y y coordinate of the minimum corner.
	 * @param width width of the rectangle.
	 * @param height height of the rectangle.
	 * @param arcWidth width of the arcs.
	 * @param arcHeight height of the arcs.
	 */
	public RoundRectangle2dfx(double x, double y, double width, double height, double arcWidth, double arcHeight) {
		set(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public RoundRectangle2dfx clone() {
		final RoundRectangle2dfx clone = super.clone();
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
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = super.hashCode();
		bits = 31 * bits + Double.hashCode(getArcWidth());
		bits = 31 * bits + Double.hashCode(getArcHeight());
        return bits ^ (bits >> 31);
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
					this, MathFXAttributeNames.ARC_WIDTH, widthProperty()) {
				@Override
				protected void invalidated(ReadOnlyDoubleProperty dependency) {
					final double value = get();
					if (value < 0.) {
						set(0.);
					} else {
						final double maxArcWidth = dependency.get() / 2.;
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
					this, MathFXAttributeNames.ARC_HEIGHT, heightProperty()) {
				@Override
				protected void invalidated(ReadOnlyDoubleProperty dependency) {
					final double value = get();
					if (value < 0.) {
						set(0.);
					} else {
						final double maxArcHeight = dependency.get() / 2.;
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
	public void setArcWidth(double arcWidth) {
		assert arcWidth >= 0. : AssertMessages.positiveOrZeroParameter();
		arcWidthProperty().set(arcWidth);
	}

	@Override
	public void setArcHeight(double arcHeight) {
		assert arcHeight >= 0. : AssertMessages.positiveOrZeroParameter();
		arcHeightProperty().set(arcHeight);
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		final double arcWidth = getArcWidth();
		final double arcHeight = getArcHeight();
		setFromCorners(x1, y1, x2, y2, arcWidth, arcHeight);
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void setFromCorners(double x1, double y1, double x2, double y2, double arcWidth, double arcHeight) {
		assert arcWidth >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert arcHeight >= 0. : AssertMessages.positiveOrZeroParameter(5);
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

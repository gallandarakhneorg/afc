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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Rectangular Prism with 3 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class RectangularPrism3dfx extends AbstractShape3dfx<RectangularPrism3dfx> implements
        RectangularPrism3afp<Shape3dfx<?>, RectangularPrism3dfx, PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> {

	private static final long serialVersionUID = -1393290109630714626L;

	private Point3dfx min = new Point3dfx();

	private Point3dfx max = new Point3dfx();

	/** width property.
	 */
	private ReadOnlyDoubleWrapper width;

	/** height property.
	 */
	private ReadOnlyDoubleWrapper height;

	/** height property.
	 */
	private ReadOnlyDoubleWrapper depth;

	/** Construct an empty rectangular prism.
     */
	public RectangularPrism3dfx() {
		addListeners();
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public RectangularPrism3dfx(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert min != null : AssertMessages.notNullParameter(0);
		assert max != null : AssertMessages.notNullParameter(1);
		setFromCorners(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
	}

	/** Construct a rectangle.
     * @param x x coordinate of the minimum corner.
     * @param y y coordinate of the minimum corner.
     * @param z z coordinate of the minimum corner.
     * @param width width of the rectangle.
     * @param height height of the rectangle.
     * @param depth depth of the rectangle.
     */
	@SuppressWarnings("checkstyle:magicnumber")
	public RectangularPrism3dfx(double x, double y, double z, double width, double height, double depth) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0. : AssertMessages.positiveOrZeroParameter(5);
        setFromCorners(x, y, z, x + width, y + height, z + depth);
	}

	/** Constructor by copy.
     * @param rectangularPrism the shape to copy.
     */
	public RectangularPrism3dfx(RectangularPrism3dfx rectangularPrism) {
		set(rectangularPrism);
	}

	@Override
	public RectangularPrism3dfx clone() {
		final RectangularPrism3dfx clone = super.clone();
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
		clone.depth = null;
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(getMinX());
		bits = 31 * bits + Double.hashCode(getMinY());
		bits = 31 * bits + Double.hashCode(getMinZ());
		bits = 31 * bits + Double.hashCode(getMaxX());
		bits = 31 * bits + Double.hashCode(getMaxY());
		bits = 31 * bits + Double.hashCode(getMaxZ());
		return bits ^ (bits >> 31);
	}

	@Override
	public void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2) {
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
		if (z1 <= z2) {
			minZProperty().set(z1);
			maxZProperty().set(z2);
		} else {
			minZProperty().set(z2);
			maxZProperty().set(z1);
		}
		addListeners();
	}

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
        this.min.zProperty().addListener((observable, oValue, nValue) -> {
            final double currentMin = nValue.doubleValue();
            final double currentMax = getMaxZ();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                maxZProperty().set(currentMin);
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
        this.max.zProperty().addListener((observable, oValue, nValue) -> {
            final double currentMax = nValue.doubleValue();
            final double currentMin = getMinZ();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                minZProperty().set(currentMax);
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
		minXProperty().set(x);
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
		maxXProperty().set(x);
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
		minYProperty().set(y);
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
		maxYProperty().set(y);
	}

	/** Replies the property that is the maximum y coordinate of the box.
	 *
	 * @return the maxY property.
	 */
	@Pure
	public DoubleProperty maxYProperty() {
		return this.max.yProperty();
	}

	@Pure
	@Override
	public double getMinZ() {
		return this.min.getZ();
	}

	@Override
	public void setMinZ(double z) {
		minZProperty().set(z);
	}

	/** Replies the property that is the minimum z coordinate of the box.
	 *
	 * @return the minZ property.
	 */
	@Pure
	public DoubleProperty minZProperty() {
		return this.min.zProperty();
	}

	@Pure
	@Override
	public double getMaxZ() {
		return this.max.getZ();
	}

	@Override
	public void setMaxZ(double z) {
		maxZProperty().set(z);
	}

	/** Replies the property that is the maximum z coordinate of the box.
	 *
	 * @return the maxZ property.
	 */
	@Pure
	public DoubleProperty maxZProperty() {
		return this.max.zProperty();
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
	public double getDepth() {
		return depthProperty().get();
	}

	/** Replies the property that is the depth of the box.
	 *
	 * @return the depth property.
	 */
	@Pure
	public DoubleProperty depthProperty() {
		if (this.depth == null) {
			this.depth = new ReadOnlyDoubleWrapper(this, MathFXAttributeNames.DEPTH);
			this.depth.bind(Bindings.subtract(maxZProperty(), minZProperty()));
		}
		return this.depth;
	}

	@Override
	public ObjectProperty<RectangularPrism3dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
			    toBoundingBox(),
			        minXProperty(), minYProperty(), minZProperty(), widthProperty(), heightProperty(), depthProperty()));
		}
		return this.boundingBox;
	}

}

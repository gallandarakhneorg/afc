/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A rectangular prism with 3 integer FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class RectangularPrism3ifx extends AbstractShape3ifx<RectangularPrism3ifx> implements
        RectangularPrism3ai<Shape3ifx<?>, RectangularPrism3ifx, PathElement3ifx, Point3ifx, Vector3ifx, RectangularPrism3ifx> {

	private static final long serialVersionUID = -8092385681401129843L;

	private Point3ifx min = new Point3ifx();

	private Point3ifx max = new Point3ifx();

	/** width property.
	 */
	private ReadOnlyIntegerWrapper width;

	/** height property.
	 */
	private ReadOnlyIntegerWrapper height;

	/** depth property.
	 */
	private ReadOnlyIntegerWrapper depth;

	/** Construct an empty rectangle.
     */
	public RectangularPrism3ifx() {
		addListeners();
	}

	/** Construct a rectangular prism with the given minimum and maximum corners.
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public RectangularPrism3ifx(Point3D<?, ?> min, Point3D<?, ?> max) {
	    assert min != null : AssertMessages.notNullParameter(0);
	    assert max != null : AssertMessages.notNullParameter(1);
	    setFromCorners(min.ix(), min.iy(), min.iz(), max.ix(), max.iy(), max.iz());
	}

	/** Construct a rectangular prism by setting the given minimum and maximum corners.
     * @param min is the min corner of the rectangle.
     * @param max is the max corner of the rectangle.
     */
	public RectangularPrism3ifx(Point3ifx min, Point3ifx max) {
		assert min != null : AssertMessages.notNullParameter(0);
		assert max != null : AssertMessages.notNullParameter(1);
		this.min = min;
		this.max = max;
	}

	/** Construct a rectangle with the given minimum corner and sizes.
     * @param x x coordinate of the minimum corner.
     * @param y y coordinate of the minimum corner.
     * @param z z coordinate of the minimum corner.
     * @param width width of the rectangle.
     * @param height height of the rectangle.
     * @param depth depth of the rectangle.
     */
	@SuppressWarnings("checkstyle:magicnumber")
	public RectangularPrism3ifx(int x, int y, int z, int width, int height, int depth) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0 : AssertMessages.positiveOrZeroParameter(5);
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}

	/** Constructor by copy.
	 * @param rectangle the rectangle to copy.
	 */
	public RectangularPrism3ifx(RectangularPrism3ai<?, ?, ?, ?, ?, ?> rectangle) {
	    assert rectangle != null : AssertMessages.notNullParameter();
        setFromCorners(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMinZ(), rectangle.getMaxX(), rectangle.getMaxY(),
                rectangle.getMaxZ());
	}

	/** Constructor by setting.
     * @param rectangle the rectangle to set.
     */
	public RectangularPrism3ifx(RectangularPrism3ifx rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		this.min = rectangle.min;
		this.max = rectangle.max;
	}

	@Override
	public RectangularPrism3ifx clone() {
		final RectangularPrism3ifx clone = super.clone();
		if (clone.min != null) {
			clone.min = null;
			clone.min = this.min.clone();
		}
		if (clone.max != null) {
			clone.max = null;
			clone.max = this.max.clone();
		}
		return clone;
	}

	@Override
	public void setFromCorners(int x1, int y1, int z1, int x2, int y2, int z2) {
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

	/**
	 * Add a listener to the point properties to observe correct min-max behavior.
	 */
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
        this.min.zProperty().addListener((observable, oValue, nValue) -> {
            final int currentMin = nValue.intValue();
            final int currentMax = getMaxZ();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                maxZProperty().set(currentMin);
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
        this.max.zProperty().addListener((observable, oValue, nValue) -> {
            final int currentMax = nValue.intValue();
            final int currentMin = getMinZ();
            if (currentMax < currentMin) {
                // min-max constrain is broken
                minZProperty().set(currentMax);
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
		minXProperty().set(x);
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
		maxXProperty().set(x);
	}

	/** Replies the property that is the maximum x coordinate of the box.
	 *
	 * @return the maxX property.
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
		minYProperty().set(y);
	}

	/** Replies the property that is the minimum y coordinate of the box.
	 *
	 * @return the minY property.
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
		maxYProperty().set(y);
	}

	/** Replies the property that is the maximum y coordinate of the box.
	 *
	 * @return the maxY property.
	 */
	@Pure
	public IntegerProperty maxYProperty() {
		return this.max.yProperty();
	}

	@Pure
	@Override
	public int getMinZ() {
		return this.min.iz();
	}

	@Override
	public void setMinZ(int z) {
		minZProperty().set(z);
	}

	/** Replies the property that is the minimum z coordinate of the box.
	 *
	 * @return the minZ property.
	 */
	@Pure
	public IntegerProperty minZProperty() {
		return this.min.zProperty();
	}

	@Pure
	@Override
	public int getMaxZ() {
		return this.max.iz();
	}

	@Override
	public void setMaxZ(int z) {
		maxZProperty().set(z);
	}

	/** Replies the property that is the maximum z coordinate of the box.
	 *
	 * @return the maxZ property.
	 */
	@Pure
	public IntegerProperty maxZProperty() {
		return this.max.zProperty();
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + getMinX();
		bits = 31 * bits + getMinY();
		bits = 31 * bits + getMaxX();
		bits = 31 * bits + getMaxY();
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
	public int getDepth() {
		return depthProperty().get();
	}

	/** Replies the property that is the depth of the box.
	 *
	 * @return the depth property.
	 */
	@Pure
	public IntegerProperty depthProperty() {
		if (this.depth == null) {
			this.depth = new ReadOnlyIntegerWrapper(this, MathFXAttributeNames.DEPTH);
			this.depth.bind(Bindings.subtract(maxZProperty(), minZProperty()));
		}
		return this.depth;
	}

	@Override
	public ObjectProperty<RectangularPrism3ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
			    toBoundingBox(),
			        minXProperty(), minYProperty(), widthProperty(), heightProperty()));
		}
		return this.boundingBox;
	}

}

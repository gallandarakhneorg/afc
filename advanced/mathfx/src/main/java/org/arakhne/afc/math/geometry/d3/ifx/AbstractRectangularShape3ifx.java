/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.arakhne.afc.math.geometry.d3.ai.Prism3ai;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;

/** A rectangular shape with 3 integer FX properties.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractRectangularShape3ifx<IT extends AbstractRectangularShape3ifx<?>>
	extends AbstractShape3ifx<IT>
	implements Prism3ai<Shape3ifx<?>, IT, PathElement3ifx, Point3ifx, Vector3ifx, AlignedBox3ifx> {

	private static final long serialVersionUID = -6551989261232962403L;

	/** minX property.
	 */
	Point3ifx min = new Point3ifx();

	/** maxX property.
	 */
	Point3ifx max = new Point3ifx();

	/** width property.
	 */
	private ReadOnlyIntegerWrapper width;

	/** height property.
	 */
	private ReadOnlyIntegerWrapper height;

	/** depth property.
	 */
	private ReadOnlyIntegerWrapper depth;

	/** Construct an empty rectangular shape.
     */
	public AbstractRectangularShape3ifx() {
		super();
	}

	/** Constructor by copy.
     * @param shape the shape to copy.
     */
	public AbstractRectangularShape3ifx(Prism3ai<?, ?, ?, ?, ?, ?> shape) {
		setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMinZ(), shape.getMaxX(), shape.getMaxY(), shape.getMaxZ());
	}

	@Override
	public IT clone() {
		final IT clone = super.clone();
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
	public ObjectProperty<AlignedBox3ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
			    toBoundingBox(),
			        minXProperty(), minYProperty(), widthProperty(), heightProperty()));
		}
		return this.boundingBox;
	}

}

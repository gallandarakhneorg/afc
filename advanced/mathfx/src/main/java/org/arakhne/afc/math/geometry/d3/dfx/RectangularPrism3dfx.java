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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;

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

	private DoubleProperty minX;

	private DoubleProperty minY;

	private DoubleProperty minZ;

	private DoubleProperty maxX;

	private DoubleProperty maxY;

	private DoubleProperty maxZ;

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
		//
	}

	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public RectangularPrism3dfx(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert min != null : "Minimum corner must be not null"; //$NON-NLS-1$
		assert max != null : "Maximum corner must be not null"; //$NON-NLS-1$
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
	public RectangularPrism3dfx(double x, double y, double z, double width, double height, double depth) {
		assert width >= 0. : "Width must be positive or zero"; //$NON-NLS-1$
		assert height >= 0. : "HeightWidth must be positive or zero"; //$NON-NLS-1$
		assert depth >= 0. : "HeightWidth must be positive or zero"; //$NON-NLS-1$
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
		if (clone.minX != null) {
			clone.minX = null;
			clone.minXProperty().set(getMinX());
		}
		if (clone.minY != null) {
			clone.minY = null;
			clone.minYProperty().set(getMinY());
		}
		if (clone.minZ != null) {
			clone.minZ = null;
			clone.minZProperty().set(getMinZ());
		}
		if (clone.maxX != null) {
			clone.maxX = null;
			clone.maxXProperty().set(getMaxX());
		}
		if (clone.maxY != null) {
			clone.maxY = null;
			clone.maxYProperty().set(getMaxY());
		}
		if (clone.maxZ != null) {
			clone.maxZ = null;
			clone.maxZProperty().set(getMaxZ());
		}
		clone.width = null;
		clone.height = null;
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getMinX());
		bits = 31 * bits + Double.doubleToLongBits(getMinY());
		bits = 31 * bits + Double.doubleToLongBits(getMinZ());
		bits = 31 * bits + Double.doubleToLongBits(getMaxX());
		bits = 31 * bits + Double.doubleToLongBits(getMaxY());
		bits = 31 * bits + Double.doubleToLongBits(getMaxZ());
		final int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getMinX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMinY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMinZ());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxZ());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
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
			minZProperty().set(y1);
			maxZProperty().set(y2);
		} else {
			minZProperty().set(y2);
			maxZProperty().set(y1);
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
					final double currentMin = get();
					final double currentMax = getMaxX();
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
					final double currentMax = get();
					final double currentMin = getMinX();
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
					final double currentMin = get();
					final double currentMax = getMaxY();
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
					final double currentMax = get();
					final double currentMin = getMinY();
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
	public double getMinZ() {
		return this.minZ == null ? 0 : this.minZ.get();
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
		if (this.minZ == null) {
			this.minZ = new SimpleDoubleProperty(this, "minZ") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					final double currentMin = get();
					final double currentMax = getMaxZ();
					if (currentMax < currentMin) {
						// min-max constrain is broken
						maxZProperty().set(currentMin);
					}
				}
			};
		}
		return this.minZ;
	}

	@Pure
	@Override
	public double getMaxZ() {
		return this.maxZ == null ? 0 : this.maxZ.get();
	}

	@Override
	public void setMaxZ(double y) {
		maxZProperty().set(y);
	}

	/** Replies the property that is the maximum z coordinate of the box.
	 *
	 * @return the maxZ property.
	 */
	@Pure
	public DoubleProperty maxZProperty() {
		if (this.maxZ == null) {
			this.maxZ = new SimpleDoubleProperty(this, "maxZ") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					final double currentMax = get();
					final double currentMin = getMinZ();
					if (currentMax < currentMin) {
						// min-max constrain is broken
						minZProperty().set(currentMax);
					}
				}
			};
		}
		return this.maxZ;
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
			this.width = new ReadOnlyDoubleWrapper(this, "width"); //$NON-NLS-1$
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
			this.height = new ReadOnlyDoubleWrapper(this, "height"); //$NON-NLS-1$
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
			this.depth = new ReadOnlyDoubleWrapper(this, "depth"); //$NON-NLS-1$
			this.depth.bind(Bindings.subtract(maxZProperty(), minZProperty()));
		}
		return this.depth;
	}

	@Override
	public ObjectProperty<RectangularPrism3dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
			    return toBoundingBox();
			},
			        minXProperty(), minYProperty(), minZProperty(), widthProperty(), heightProperty(), depthProperty()));
		}
		return this.boundingBox;
	}

}

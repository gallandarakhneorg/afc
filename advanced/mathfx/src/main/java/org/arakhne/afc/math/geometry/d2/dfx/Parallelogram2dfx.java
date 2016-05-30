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

package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;

/** Parallelogram with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: mgrolleau$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Parallelogram2dfx extends AbstractShape2dfx<Parallelogram2dfx>
		implements Parallelogram2afp<Shape2dfx<?>, Parallelogram2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

	private static final long serialVersionUID = -3880367245218796375L;

	/**
	 * Center of the parallelogram.
	 */
	private DoubleProperty cx;

	/**
	 * Center of the parallelogram.
	 */
	private DoubleProperty cy;

	/**
	 * The first axis of the parallelogram.
	 */
	private UnitVectorProperty raxis;

	/**
	 * The second axis of the parallelogram.
	 */
	private UnitVectorProperty saxis;

	/**
	 * Half-size of the first axis of the parallelogram.
	 */
	private DoubleProperty extentR;

	/**
	 * Half-size of the second axis of the parallelogram.
	 */
	private DoubleProperty extentS;

	/** Create an empty parallelogram.
	 */
	public Parallelogram2dfx() {
		//
	}

	/** Create an parallelogram from the given parallelogram.
	 *
	 * @param parallelogram the parallelogram to copy.
	 */
	public Parallelogram2dfx(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : "Oriented Rectangle must be not null"; //$NON-NLS-1$
		set(parallelogram.getCenterX(), parallelogram.getCenterY(),
				parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(),
				parallelogram.getFirstAxisExtent(),
				parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(),
				parallelogram.getSecondAxisExtent());
	}

	/** Construct a parallelogram from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public Parallelogram2dfx(Iterable<? extends Point2D<?, ?>> pointCloud) {
		setFromPointCloud(pointCloud);
	}

	/** Construct a parallelogram from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public Parallelogram2dfx(Point2D<?, ?>... pointCloud) {
		setFromPointCloud(pointCloud);
	}

	/** Construct a parallelogram.
	 *
	 * @param centerX is the X coordinate of the parallelogram center.
	 * @param centerY is the Y coordinate of the parallelogram center.
	 * @param axis1X is the X coordinate of first axis of the parallelogram.
	 * @param axis1Y is the Y coordinate of first axis of the parallelogram.
	 * @param axis1Extent is the extent of the first parallelogram.
	 * @param axis2X is the X coordinate of second axis of the parallelogram.
	 * @param axis2Y is the Y coordinate of second axis of the parallelogram.
	 * @param axis2Extent is the extent of the second parallelogram.
	 */
	public Parallelogram2dfx(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2X, double axis2Y, double axis2Extent) {
		assert Vector2D.isUnitVector(axis1X, axis1Y) : "First axis must be a unit vector"; //$NON-NLS-1$
		assert Vector2D.isUnitVector(axis2X, axis2Y) : "Second axis must be a unit vector"; //$NON-NLS-1$
		assert axis1Extent >= 0. : "Extent for the first axis must be positive or zero"; //$NON-NLS-1$
		assert axis2Extent >= 0. : "Extent for the first axis must be positive or zero"; //$NON-NLS-1$
		set(centerX, centerY, axis1X, axis1Y, axis1Extent, axis2X, axis2Y, axis2Extent);
	}

	/** Construct a parallelogram.
	 *
	 * @param center is the parallelogram center.
	 * @param axis1 is the first axis of the parallelogram.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2 is the second axis of the parallelogram.
	 * @param axis2Extent is the extent of the second axis.
	 */
	public Parallelogram2dfx(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent,
			Vector2D<?, ?> axis2, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2, axis2Extent);
	}

	@Override
	public Parallelogram2dfx clone() {
		final Parallelogram2dfx clone = super.clone();
		if (clone.cx != null) {
			clone.cx = null;
			clone.centerXProperty().set(getCenterX());
		}
		if (clone.cy != null) {
			clone.cy = null;
			clone.centerYProperty().set(getCenterY());
		}
		if (clone.raxis != null) {
			clone.raxis = null;
			clone.firstAxisProperty().set(getFirstAxis());
		}
		if (clone.extentR != null) {
			clone.extentR = null;
			clone.firstAxisExtentProperty().set(getFirstAxisExtent());
		}
		if (clone.saxis != null) {
			clone.saxis = null;
			clone.secondAxisProperty().set(getSecondAxis());
		}
		if (clone.extentS != null) {
			clone.extentS = null;
			clone.secondAxisExtentProperty().set(getSecondAxisExtent());
		}
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getCenterX());
		bits = 31 * bits + Double.doubleToLongBits(getCenterY());
		bits = 31 * bits + Double.doubleToLongBits(getFirstAxisX());
		bits = 31 * bits + Double.doubleToLongBits(getFirstAxisY());
		bits = 31 * bits + Double.doubleToLongBits(getFirstAxisExtent());
		bits = 31 * bits + Double.doubleToLongBits(getSecondAxisX());
		bits = 31 * bits + Double.doubleToLongBits(getSecondAxisY());
		bits = 31 * bits + Double.doubleToLongBits(getSecondAxisExtent());
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getCenterX());
		b.append(";"); //$NON-NLS-1$
		b.append(getCenterY());
		b.append(";"); //$NON-NLS-1$
		b.append(getFirstAxisX());
		b.append(";"); //$NON-NLS-1$
		b.append(getFirstAxisY());
		b.append(";"); //$NON-NLS-1$
		b.append(getFirstAxisExtent());
		b.append(";"); //$NON-NLS-1$
		b.append(getSecondAxisX());
		b.append(";"); //$NON-NLS-1$
		b.append(getSecondAxisY());
		b.append(";"); //$NON-NLS-1$
		b.append(getSecondAxisExtent());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/** Replies the property for the x coordinate of the center.
	 *
	 * @return the property.
	 */
	public DoubleProperty centerXProperty() {
		if (this.cx == null) {
			this.cx = new SimpleDoubleProperty(this, "centerX"); //$NON-NLS-1$
		}
		return this.cx;
	}

	/** Replies the property for the y coordinate of the center.
	 *
	 * @return the property.
	 */
	public DoubleProperty centerYProperty() {
		if (this.cy == null) {
			this.cy = new SimpleDoubleProperty(this, "centerY"); //$NON-NLS-1$
		}
		return this.cy;
	}

	@Pure
	@Override
	public Point2dfx getCenter() {
		return getGeomFactory().newPoint(this.cx, this.cy);
	}

	@Pure
	@Override
	public double getCenterX() {
		return this.cx == null ? 0 : this.cx.get();
	}

	@Override
	public void setCenterX(double cx) {
		centerXProperty().set(cx);
	}

	@Override
	public void setCenterY(double cy) {
		centerYProperty().set(cy);
	}

	@Pure
	@Override
	public double getCenterY() {
		return this.cy == null ? 0 : this.cy.get();
	}

	@Override
	public void setCenter(double cx, double cy) {
		centerXProperty().set(cx);
		centerYProperty().set(cy);
	}

	/** Replies the property for the first axis.
	 *
	 * @return the property.
	 */
	public UnitVectorProperty firstAxisProperty() {
		if (this.raxis == null) {
			this.raxis = new UnitVectorProperty(this, "firstAxis", getGeomFactory()); //$NON-NLS-1$
		}
		return this.raxis;
	}

	@Pure
	@Override
	public Vector2dfx getFirstAxis() {
		return firstAxisProperty().get();
	}

	@Pure
	@Override
	public double getFirstAxisX() {
		return this.raxis == null ? 1 : this.raxis.getX();
	}

	@Pure
	@Override
	public double getFirstAxisY() {
		return this.raxis == null ? 0 : this.raxis.getY();
	}

	/** Replies the property for the second axis.
	 *
	 * @return the property.
	 */
	public UnitVectorProperty secondAxisProperty() {
		if (this.saxis == null) {
			this.saxis = new UnitVectorProperty(this, "firstAxis", getGeomFactory()); //$NON-NLS-1$
		}
		return this.saxis;
	}

	@Pure
	@Override
	public Vector2dfx getSecondAxis() {
		return secondAxisProperty().get();
	}

	@Pure
	@Override
	public double getSecondAxisX() {
		return this.saxis == null ? 0 : this.saxis.getX();
	}

	@Pure
	@Override
	public double getSecondAxisY() {
		return this.saxis == null ? 1 : this.saxis.getY();
	}

	/** Replies the property for the extent of the first axis.
	 *
	 * @return the firstAxisExtent property.
	 */
	@Pure
	public DoubleProperty firstAxisExtentProperty() {
		if (this.extentR == null) {
			this.extentR = new SimpleDoubleProperty(this, "firstAxisExtent") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					if (get() < 0.) {
						set(0.);
					}
				}
			};
		}
		return this.extentR;
	}

	@Pure
	@Override
	public double getFirstAxisExtent() {
		return this.extentR == null ? 0 : this.extentR.get();
	}

	@Override
	public void setFirstAxisExtent(double extent) {
		assert extent >= 0. : "Extent must be positive or zero"; //$NON-NLS-1$
		firstAxisExtentProperty().set(extent);
	}

	/** Replies the property for the extent of the second axis.
	 *
	 * @return the secondAxisExtent property.
	 */
	@Pure
	public DoubleProperty secondAxisExtentProperty() {
		if (this.extentS == null) {
			this.extentS = new SimpleDoubleProperty(this, "secondAxisExtent") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					if (get() < 0.) {
						set(0.);
					}
				}
			};
		}
		return this.extentS;
	}

	@Pure
	@Override
	public double getSecondAxisExtent() {
		return this.extentS == null ? 0 : this.extentS.get();
	}

	@Override
	public void setSecondAxisExtent(double extent) {
		assert extent >= 0. : "Extent must be positive or zero"; //$NON-NLS-1$
		secondAxisExtentProperty().set(extent);
	}

	@Override
	public void setFirstAxis(double x, double y, double extent) {
		assert Vector2D.isUnitVector(x, y) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert extent >= 0. : "Extent must be positive or zero"; //$NON-NLS-1$
		firstAxisProperty().set(x, y);
		firstAxisExtentProperty().set(extent);
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert Vector2D.isUnitVector(x, y) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert extent >= 0. : "Extent must be positive or zero"; //$NON-NLS-1$
		secondAxisProperty().set(x, y);
		secondAxisExtentProperty().set(extent);
	}

	@Override
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2x,
			double axis2y, double axis2Extent) {
		assert Vector2D.isUnitVector(axis1x, axis1y) : "First axis must be a unit vector"; //$NON-NLS-1$
		assert Vector2D.isUnitVector(axis2x, axis2y) : "First axis must be a unit vector"; //$NON-NLS-1$
		assert axis1Extent >= 0. : "First axis extent must be positive or zero"; //$NON-NLS-1$
		assert axis2Extent >= 0. : "Second axis extent must be positive or zero"; //$NON-NLS-1$
		centerXProperty().set(centerX);
		centerYProperty().set(centerY);
		firstAxisProperty().set(axis1x, axis1y);
		firstAxisExtentProperty().set(axis1Extent);
		secondAxisProperty().set(axis2x, axis2y);
		secondAxisExtentProperty().set(axis2Extent);
	}

	@Override
	public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				return toBoundingBox();
			},
					centerXProperty(), centerYProperty(),
					firstAxisProperty(), firstAxisExtentProperty(),
					secondAxisProperty(), secondAxisExtentProperty()));
		}
		return this.boundingBox;
	}

}

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

import java.util.Arrays;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;

/** Orineted rectangle with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: mgrolleau$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class OrientedRectangle2dfx extends AbstractShape2dfx<OrientedRectangle2dfx>
		implements OrientedRectangle2afp<Shape2dfx<?>, OrientedRectangle2dfx, PathElement2dfx,
		Point2dfx, Vector2dfx, Rectangle2dfx> {
	private static final long serialVersionUID = -7570709068803272507L;
	/**
	 *Literal constant.
	 */

	private static final String EXTENT_POSITIVE_OR_ZERO = "Extent must be positive or zero";

	/**
	 *Center of the OBR.
	 */
	private DoubleProperty cx;

	/**
	 *Center of the OBR.
	 */
	private DoubleProperty cy;

	/**
	 *The first axis of the OBR.
	 */
	private UnitVectorProperty raxis;

	/**
	 * The second axis of the OBR.
	 */
	private ReadOnlyUnitVectorWrapper saxis;

	/**
	 * Half-size of the first axis of the OBR.
	 */
	private DoubleProperty extentR;

	/**
	 * Half-size of the second axis of the OBR.
	 */
	private DoubleProperty extentS;

	/** Create an empty oriented rectangle.
	 */
	public OrientedRectangle2dfx() {
		//
	}

	/** Create an oriented rectangle from the given OBR.
	 *
	 * @param obr the oriented rectangle to copy.
	 */
	public OrientedRectangle2dfx(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> obr) {
		assert obr != null : "Oriented rectangle must be not null"; //$NON-NLS-1$
		set(obr.getCenterX(), obr.getCenterY(),
				obr.getFirstAxisX(), obr.getFirstAxisY(),
				obr.getFirstAxisExtent(),
				obr.getSecondAxisExtent());
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2dfx(Iterable<? extends Point2D<?, ?>> pointCloud) {
		assert pointCloud != null : "Collection of points must be not null"; //$NON-NLS-1$
		setFromPointCloud(pointCloud);
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2dfx(Point2D<?, ?>... pointCloud) {
		assert pointCloud != null : "Collection of points must be not null"; //$NON-NLS-1$
		setFromPointCloud(Arrays.asList(pointCloud));
	}

	/** Construct an oriented rectangle.
	 *
	 * @param centerX is the X coordinate of the OBR center.
	 * @param centerY is the Y coordinate of the OBR center.
	 * @param axis1X is the X coordinate of first axis of the OBR.
	 * @param axis1Y is the Y coordinate of first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	public OrientedRectangle2dfx(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2Extent) {
		set(centerX, centerY, axis1X, axis1Y, axis1Extent, axis2Extent);
	}

	/** Construct an oriented rectangle.
	 *
	 * @param center is the OBR center.
	 * @param axis1 is the first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	public OrientedRectangle2dfx(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2Extent);
	}

	@Override
	public OrientedRectangle2dfx clone() {
		final OrientedRectangle2dfx clone = super.clone();
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
		clone.saxis = null;
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
		return b ^ (b >> 32);
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

	@Pure
	@Override
	public Point2dfx getCenter() {
		return getGeomFactory().newPoint(centerXProperty(), centerYProperty());
	}

	@Pure
	@Override
	public double getCenterX() {
		return this.cx == null ? 0 : this.cx.get();
	}

	@Pure
	@Override
	public double getCenterY() {
		return this.cy == null ? 0 : this.cy.get();
	}

	/** Replies the property for the x coordinate of the rectangle's center.
	 *
	 * @return the centerX property.
	 */
	@Pure
	public DoubleProperty centerXProperty() {
		if (this.cx == null) {
			this.cx = new SimpleDoubleProperty(this, "centerX"); //$NON-NLS-1$
		}
		return this.cx;
	}

	/** Replies the property for the y coordinate of the rectangle's center.
	 *
	 * @return the centerY property.
	 */
	@Pure
	public DoubleProperty centerYProperty() {
		if (this.cy == null) {
			this.cy = new SimpleDoubleProperty(this, "centerX"); //$NON-NLS-1$
		}
		return this.cy;
	}

	@Override
	public void setCenter(double cx, double cy) {
		centerXProperty().set(cx);
		centerYProperty().set(cy);
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

	@Pure
	@Override
	public Vector2dfx getSecondAxis() {
		return secondAxisProperty().get();
	}

	@Pure
	@Override
	public double getSecondAxisX() {
		return secondAxisProperty().getX();
	}

	@Pure
	@Override
	public double getSecondAxisY() {
		return secondAxisProperty().getY();
	}

	/** Replies the property for the first rectangle axis.
	 *
	 * @return the firstAxis property.
	 */
	@Pure
	public UnitVectorProperty firstAxisProperty() {
		if (this.raxis == null) {
			this.raxis = new UnitVectorProperty(this, "firstAxis", getGeomFactory()); //$NON-NLS-1$
		}
		return this.raxis;
	}

	/** Replies the property for the second rectangle axis.
	 *
	 * @return the secondAxis property.
	 */
	@Pure
	public ReadOnlyUnitVectorProperty secondAxisProperty() {
		if (this.saxis == null) {
			this.saxis = new ReadOnlyUnitVectorWrapper(this, "secondAxis", getGeomFactory()); //$NON-NLS-1$
			this.saxis.bind(Bindings.createObjectBinding(() -> {
				final Vector2dfx firstAxis = firstAxisProperty().get();
				return firstAxis.toOrthogonalVector();
			},
					firstAxisProperty()));
		}
		return this.saxis.getReadOnlyProperty();
	}

	@Pure
	@Override
	public double getFirstAxisExtent() {
		return this.extentR == null ? 0 : this.extentR.get();
	}

	@Override
	public void setFirstAxisExtent(double extent) {
		assert extent >= 0 : EXTENT_POSITIVE_OR_ZERO; //$NON-NLS-1$
		firstAxisExtentProperty().set(extent);
	}

	/** Replies the property for the extent of the first rectangle axis.
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
	public double getSecondAxisExtent() {
		return this.extentS == null ? 0 : this.extentS.get();
	}

	@Override
	public void setSecondAxisExtent(double extent) {
		assert extent >= 0 : EXTENT_POSITIVE_OR_ZERO; //$NON-NLS-1$
		secondAxisExtentProperty().set(extent);
	}

	/** Replies the property for the extent of the second rectangle axis.
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

	@Override
	public void setFirstAxis(double x, double y, double extent) {
		assert Vector2D.isUnitVector(x, y) : "Axis must be unit vector"; //$NON-NLS-1$
		assert extent >= 0 : EXTENT_POSITIVE_OR_ZERO; //$NON-NLS-1$
		firstAxisProperty().set(x, y);
		firstAxisExtentProperty().set(extent);
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert Vector2D.isUnitVector(x, y) : "Axis must be unit vector"; //$NON-NLS-1$
		assert extent >= 0 : EXTENT_POSITIVE_OR_ZERO; //$NON-NLS-1$
		firstAxisProperty().set(y, -x);
		secondAxisExtentProperty().set(extent);
	}

	@Override
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2Extent) {
		assert Vector2D.isUnitVector(axis1x, axis1y) : "First axis must be unit vector"; //$NON-NLS-1$
		assert axis1Extent >= 0 : "First axis extent must be positive or zero"; //$NON-NLS-1$
		assert axis2Extent >= 0 : "Second axis extent must be positive or zero"; //$NON-NLS-1$
		centerXProperty().set(centerX);
		centerYProperty().set(centerY);
		firstAxisProperty().set(axis1x, axis1y);
		firstAxisExtentProperty().set(axis1Extent);
		// Do not need to the second axis coordinates since it will be automatically done by the properties of the first axis.
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
				secondAxisExtentProperty()));
		}
		return this.boundingBox;
	}

}

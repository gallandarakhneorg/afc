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
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Oriented rectangle with 2 double precision floating-point FX properties.
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
	 *Center of the OBR.
	 */
	private Point2dfx center = new Point2dfx();

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
		assert obr != null : AssertMessages.notNullParameter();
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
		assert pointCloud != null : AssertMessages.notNullParameter();
		setFromPointCloud(pointCloud);
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2dfx(Point2D<?, ?>... pointCloud) {
		assert pointCloud != null : AssertMessages.notNullParameter();
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
		if (clone.center != null) {
			clone.center = null;
			clone.center = this.center.clone();
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
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(getCenterX());
		bits = 31 * bits + Double.hashCode(getCenterY());
		bits = 31 * bits + Double.hashCode(getFirstAxisX());
		bits = 31 * bits + Double.hashCode(getFirstAxisY());
		bits = 31 * bits + Double.hashCode(getFirstAxisExtent());
		bits = 31 * bits + Double.hashCode(getSecondAxisX());
		bits = 31 * bits + Double.hashCode(getSecondAxisY());
		bits = 31 * bits + Double.hashCode(getSecondAxisExtent());
        return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Point2dfx getCenter() {
		return this.center;
	}

	@Pure
	@Override
	public double getCenterX() {
		return this.center.getX();
	}

	@Pure
	@Override
	public double getCenterY() {
		return this.center.getY();
	}

	/** Replies the property for the x coordinate of the rectangle's center.
	 *
	 * @return the centerX property.
	 */
	@Pure
	public DoubleProperty centerXProperty() {
		return this.center.xProperty();
	}

	/** Replies the property for the y coordinate of the rectangle's center.
	 *
	 * @return the centerY property.
	 */
	@Pure
	public DoubleProperty centerYProperty() {
		return this.center.yProperty();
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
			this.raxis = new UnitVectorProperty(this, MathFXAttributeNames.FIRST_AXIS, getGeomFactory());
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
			this.saxis = new ReadOnlyUnitVectorWrapper(this, MathFXAttributeNames.SECOND_AXIS, getGeomFactory());
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
		assert extent >= 0 : AssertMessages.positiveOrZeroParameter();
		firstAxisExtentProperty().set(extent);
	}

	/** Replies the property for the extent of the first rectangle axis.
	 *
	 * @return the firstAxisExtent property.
	 */
	@Pure
	public DoubleProperty firstAxisExtentProperty() {
		if (this.extentR == null) {
			this.extentR = new SimpleDoubleProperty(this, MathFXAttributeNames.FIRST_AXIS_EXTENT) {
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
		assert extent >= 0 : AssertMessages.positiveOrZeroParameter();
		secondAxisExtentProperty().set(extent);
	}

	/** Replies the property for the extent of the second rectangle axis.
	 *
	 * @return the secondAxisExtent property.
	 */
	@Pure
	public DoubleProperty secondAxisExtentProperty() {
		if (this.extentS == null) {
			this.extentS = new SimpleDoubleProperty(this, MathFXAttributeNames.SECOND_AXIS_EXTENT) {
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
		assert Vector2D.isUnitVector(x, y) : AssertMessages.normalizedParameters(0, 1);
		assert extent >= 0 : AssertMessages.positiveOrZeroParameter(2);
		firstAxisProperty().set(x, y);
		firstAxisExtentProperty().set(extent);
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert Vector2D.isUnitVector(x, y) : AssertMessages.normalizedParameters(0, 1);
		assert extent >= 0 : AssertMessages.positiveOrZeroParameter(2);
		firstAxisProperty().set(y, -x);
		secondAxisExtentProperty().set(extent);
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2Extent) {
		assert Vector2D.isUnitVector(axis1x, axis1y) : AssertMessages.normalizedParameters(2, 3);
		assert axis1Extent >= 0 : AssertMessages.positiveOrZeroParameter(4);
		assert axis2Extent >= 0 : AssertMessages.positiveOrZeroParameter(5);
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
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> toBoundingBox(),
				centerXProperty(), centerYProperty(),
				firstAxisProperty(), firstAxisExtentProperty(),
				secondAxisExtentProperty()));
		}
		return this.boundingBox;
	}

}

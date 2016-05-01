/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.arakhne.afc.math.geometry.d2.fpfx;

import java.util.Arrays;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

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
public class OrientedRectangle2fx extends AbstractShape2fx<OrientedRectangle2fx>
	implements OrientedRectangle2afp<Shape2fx<?>, OrientedRectangle2fx, PathElement2fx, Point2fx, Vector2fx, Rectangle2fx> {

	private static final long serialVersionUID = -7570709068803272507L;

	/**
	 * Center of the OBR
	 */
	private DoubleProperty cx;

	/**
	 * Center of the OBR
	 */
	private DoubleProperty cy;

	/**
	 * The first axis of the OBR
	 */
	private UnitVectorProperty rVector;

	/**
	 * The second axis of the OBR
	 */
	private ReadOnlyUnitVectorWrapper sVector;

	/**
	 * Half-size of the first axis of the OBR
	 */
	private DoubleProperty extentR;

	/**
	 * Half-size of the second axis of the OBR
	 */
	private DoubleProperty extentS;

	/** Create an empty oriented rectangle.
	 */
	public OrientedRectangle2fx() {
		//
	}

	/** Create an oriented rectangle from the given OBR.
	 * 
	 * @param obr
	 */
	public OrientedRectangle2fx(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> obr) {
		assert (obr != null) : "Oriented rectangle must be not null"; //$NON-NLS-1$
		set(obr.getCenterX(), obr.getCenterY(),
				obr.getFirstAxisX(), obr.getFirstAxisY(),
				obr.getFirstAxisExtent(),
				obr.getSecondAxisExtent());
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2fx(Iterable<? extends Point2D<?, ?>> pointCloud) {
		assert (pointCloud != null) : "Collection of points must be not null"; //$NON-NLS-1$
		setFromPointCloud(pointCloud);
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2fx(Point2D<?, ?>... pointCloud) {
		assert (pointCloud != null) : "Collection of points must be not null"; //$NON-NLS-1$
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
	public OrientedRectangle2fx(double centerX, double centerY,
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
	public OrientedRectangle2fx(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2Extent);
	}
	
	@Override
	public OrientedRectangle2fx clone() {
		OrientedRectangle2fx clone = super.clone();
		if (clone.cx != null) {
			clone.cx = null;
			clone.centerXProperty().set(getCenterX());
		}
		if (clone.cy != null) {
			clone.cy = null;
			clone.centerYProperty().set(getCenterY());
		}
		if (clone.rVector != null) {
			clone.rVector = null;
			clone.firstAxisProperty().set(getFirstAxis());
		}
		if (clone.extentR != null) {
			clone.extentR = null;
			clone.firstAxisExtentProperty().set(getFirstAxisExtent());
		}
		clone.sVector = null;
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
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
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
	public Point2fx getCenter() {
		return new Point2fx(centerXProperty(), centerYProperty());
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
	public Vector2fx getFirstAxis() {
		return firstAxisProperty().get();
	}

	@Pure
	@Override
	public double getFirstAxisX() {
		return this.rVector == null ? 1 : this.rVector.getX();
	}

	@Pure
	@Override
	public double getFirstAxisY() {
		return this.rVector == null ? 0 : this.rVector.getY();
	}

	@Pure
	@Override
	public Vector2fx getSecondAxis() {
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
		if (this.rVector == null) {
			this.rVector = new UnitVectorProperty(this, "firstAxis"); //$NON-NLS-1$
		}
		return this.rVector;
	}
	
	/** Replies the property for the second rectangle axis.
	 *
	 * @return the secondAxis property.
	 */
	@Pure
	public ReadOnlyUnitVectorProperty secondAxisProperty() {
		if (this.sVector == null) {
			this.sVector = new ReadOnlyUnitVectorWrapper(this, "secondAxis"); //$NON-NLS-1$
			this.sVector.bind(Bindings.createObjectBinding(
					() -> {
						Vector2fx firstAxis = firstAxisProperty().get();
						return firstAxis.toOrthogonalVector();
					},
					firstAxisProperty()));
		}
		return this.sVector.getReadOnlyProperty();
	}

	@Pure
	@Override
	public double getFirstAxisExtent() {
		return this.extentR == null ? 0 : this.extentR.get();
	}

	@Override
	public void setFirstAxisExtent(double extent) {
		assert (extent >= 0) : "Extent must be positive or zero"; //$NON-NLS-1$
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
		assert (extent >= 0) : "Extent must be positive or zero"; //$NON-NLS-1$
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
		assert (Vector2D.isUnitVector(x, y)) : "Axis must be unit vector"; //$NON-NLS-1$
		assert (extent >= 0) : "Extent must be positive or zero"; //$NON-NLS-1$
		firstAxisProperty().set(x, y);
		firstAxisExtentProperty().set(extent);
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert (Vector2D.isUnitVector(x, y)) : "Axis must be unit vector"; //$NON-NLS-1$
		assert (extent >= 0) : "Extent must be positive or zero"; //$NON-NLS-1$
		firstAxisProperty().set(y, -x);
		secondAxisExtentProperty().set(extent);
	}

	@Override
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2Extent) {
		assert (Vector2D.isUnitVector(axis1x, axis1y)) : "First axis must be unit vector"; //$NON-NLS-1$
		assert (axis1Extent >= 0) : "First axis extent must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Second axis extent must be positive or zero"; //$NON-NLS-1$
		centerXProperty().set(centerX);
		centerYProperty().set(centerY);
		firstAxisProperty().set(axis1x, axis1y);
		firstAxisExtentProperty().set(axis1Extent);
		// Do not need to the second axis coordinates since it will be automatically done by the properties of the first axis.
		secondAxisExtentProperty().set(axis2Extent);
	}

	@Override
	public ObjectProperty<Rectangle2fx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						return toBoundingBox();
					},
					centerXProperty(), centerYProperty(),
					firstAxisProperty(), firstAxisExtentProperty(),
					secondAxisExtentProperty()));
		}
		return this.boundingBox;
	}

}

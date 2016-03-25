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

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

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
public class OrientedRectangle2fx extends AbstractShape2fx<OrientedRectangle2fx>
	implements OrientedRectangle2afp<Shape2fx<?>, OrientedRectangle2fx, PathElement2fx, Point2fx, Rectangle2fx> {

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
	 * X coordinate of the first axis of the OBR
	 */
	private DoubleProperty rx;

	/**
	 * Y coordinate of the first axis of the OBR
	 */
	private DoubleProperty ry;

	/**
	 * X coordinate of the second axis of the OBR
	 */
	private DoubleProperty sx;

	/**
	 * Y coordinate of the second axis of the OBR
	 */
	private DoubleProperty sy;

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
	public OrientedRectangle2fx(OrientedRectangle2afp<?, ?, ?, ?, ?> obr) {
		set(obr.getCenterX(), obr.getCenterY(),
				obr.getFirstAxisX(), obr.getFirstAxisY(),
				obr.getFirstAxisExtent(),
				obr.getSecondAxisX(), obr.getSecondAxisY(), obr.getSecondAxisExtent());
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2fx(Iterable<? extends Point2D> pointCloud) {
		setFromPointCloud(pointCloud);
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2fx(Point2D... pointCloud) {
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
	public OrientedRectangle2fx(Point2D center, Vector2D axis1, double axis1Extent, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2Extent);
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
	public Point2D getCenter() {
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
			this.cx = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "centerX"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
			};
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
			this.cy = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "centerY"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
			};
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
	public Vector2D getFirstAxis() {
		return new Vector2fx(firstAxisXProperty(), firstAxisYProperty());
	}

	@Pure
	@Override
	public double getFirstAxisX() {
		return this.rx == null ? 0 : this.rx.get();
	}

	@Pure
	@Override
	public double getFirstAxisY() {
		return this.ry == null ? 0 : this.ry.get();
	}

	@Pure
	@Override
	public Vector2D getSecondAxis() {
		return new Vector2fx(secondAxisXProperty(), secondAxisYProperty());
	}

	@Pure
	@Override
	public double getSecondAxisX() {
		return this.sx == null ? 0 : this.sx.get();
	}

	@Pure
	@Override
	public double getSecondAxisY() {
		return this.sy == null ? 0 : this.sy.get();
	}

	/** Replies the property for the x coordinate of the first rectangle axis.
	 *
	 * @return the firstAxisX property.
	 */
	@Pure
	public DoubleProperty firstAxisXProperty() {
		if (this.rx == null) {
			this.rx = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "firstAxisX"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					// Ensure that the second axis is perpendicular
					ensureValidCoordinateSystemWhenFirstAxisChanged();
				}
			};
		}
		return this.rx;
	}

	/** Replies the property for the y coordinate of the first rectangle axis.
	 *
	 * @return the firstAxisY property.
	 */
	@Pure
	public DoubleProperty firstAxisYProperty() {
		if (this.ry == null) {
			this.ry = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "firstAxisY"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					// Ensure that the second axis is perpendicular
					ensureValidCoordinateSystemWhenFirstAxisChanged();
				}
			};
		}
		return this.ry;
	}

	/** Replies the property for the x coordinate of the second rectangle axis.
	 *
	 * @return the secondAxisX property.
	 */
	@Pure
	public DoubleProperty secondAxisXProperty() {
		if (this.sx == null) {
			this.sx = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "secondAxisX"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					// Ensure that the second axis is perpendicular
					ensureValidCoordinateSystemWhenSecondAxisChanged();
				}
			};
		}
		return this.sx;
	}

	/** Replies the property for the y coordinate of the second rectangle axis.
	 *
	 * @return the secondAxisY property.
	 */
	@Pure
	public DoubleProperty secondAxisYProperty() {
		if (this.sy == null) {
			this.sy = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "secondAxisY"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
				
				@Override
				protected void invalidated() {
					// Ensure that the second axis is perpendicular
					ensureValidCoordinateSystemWhenSecondAxisChanged();
				}
			};
		}
		return this.sy;
	}
	
	/** Ensure that the local coordinate system is valid.
	 *
	 * <p>The exis vectors must be unit vectors. And, the two axis must be perpendicular.
	 */
	protected void ensureValidCoordinateSystemWhenFirstAxisChanged() {
		double baseX = getFirstAxisX();
		double baseY = getFirstAxisY();
		double otherX;
		double otherY;
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			otherX = baseY;
			otherY = -baseX;
		} else {
			otherX = -baseY;
			otherY = baseX;
		}
		double length = otherX * otherX + otherY * otherY;
		if (length != 0.) {
			length = Math.sqrt(length);
			otherX /= length;
			otherY /= length;
		}
		secondAxisXProperty().set(otherX);
		secondAxisYProperty().set(otherY);
	}

	/** Ensure that the local coordinate system is valid.
	 *
	 * <p>The exis vectors must be unit vectors. And, the two axis must be perpendicular.
	 */
	protected void ensureValidCoordinateSystemWhenSecondAxisChanged() {
		double baseX = getSecondAxisX();
		double baseY = getSecondAxisY();
		double otherX;
		double otherY;
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			otherX = -baseY;
			otherY = baseX;
		} else {
			otherX = baseY;
			otherY = -baseX;
		}
		double length = otherX * otherX + otherY * otherY;
		if (length != 0.) {
			length = Math.sqrt(length);
			otherX /= length;
			otherY /= length;
		}
		firstAxisXProperty().set(otherX);
		firstAxisYProperty().set(otherY);
	}

	@Pure
	@Override
	public double getFirstAxisExtent() {
		return this.extentR == null ? 0 : this.extentR.get();
	}

	@Override
	public void setFirstAxisExtent(double extent) {
		firstAxisExtentProperty().set(extent);
	}

	/** Replies the property for the extent of the first rectangle axis.
	 *
	 * @return the firstAxisExtent property.
	 */
	@Pure
	public DoubleProperty firstAxisExtentProperty() {
		if (this.extentR == null) {
			this.extentR = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "firstAxisExtent"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
	
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
		secondAxisExtentProperty().set(extent);
	}

	/** Replies the property for the extent of the second rectangle axis.
	 *
	 * @return the secondAxisExtent property.
	 */
	@Pure
	public DoubleProperty secondAxisExtentProperty() {
		if (this.extentS == null) {
			this.extentS = new DoublePropertyBase(0) {
				@Override
				public String getName() {
					return "secondAxisExtent"; //$NON-NLS-1$
				}
				
				@Override
				public Object getBean() {
					return OrientedRectangle2fx.this;
				}
	
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
		assert (Vector2D.isUnitVector(x, y));
		firstAxisXProperty().set(x);
		firstAxisYProperty().set(y);
		firstAxisExtentProperty().set(extent);
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert (Vector2D.isUnitVector(x, y));
		secondAxisXProperty().set(x);
		secondAxisYProperty().set(y);
		secondAxisExtentProperty().set(extent);
	}

	@Override
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2x,
			double axis2y, double axis2Extent) {
		assert (Vector2D.isUnitVector(axis1x, axis1y));
		assert (Vector2D.isUnitVector(axis2x, axis2y));
		centerXProperty().set(centerX);
		centerYProperty().set(centerY);
		firstAxisXProperty().set(axis1x);
		firstAxisYProperty().set(axis1y);
		firstAxisExtentProperty().set(axis1Extent);
		// Do not need to the second axis coordinates since it will be automatically done by the properties of the first axis.
		secondAxisExtentProperty().set(axis2Extent);
	}

}

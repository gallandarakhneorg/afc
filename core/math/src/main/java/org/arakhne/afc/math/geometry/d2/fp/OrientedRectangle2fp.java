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

package org.arakhne.afc.math.geometry.d2.fp;

import java.util.Arrays;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** Oriented rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: mgrolleau$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class OrientedRectangle2fp extends AbstractShape2fp<OrientedRectangle2fp>
	implements OrientedRectangle2afp<Shape2fp<?>, OrientedRectangle2fp, PathElement2fp, Point2fp, Vector2fp, Rectangle2fp> {

	private static final long serialVersionUID = 7619908159953423088L;

	/**
	 * Center of the OBR
	 */
	private double cx;

	/**
	 * Center of the OBR
	 */
	private double cy;

	/**
	 * X coordinate of the first axis of the OBR
	 */
	private double rx;

	/**
	 * Y coordinate of the first axis of the OBR
	 */
	private double ry;

	/**
	 * Half-size of the first axis of the OBR
	 */
	private double extentR;

	/**
	 * Half-size of the second axis of the OBR
	 */
	private double extentS;

	/** Create an empty oriented rectangle.
	 */
	public OrientedRectangle2fp() {
		//
	}

	/** Create an oriented rectangle from the given OBR.
	 * 
	 * @param obr
	 */
	public OrientedRectangle2fp(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> obr) {
		assert (obr != null) : "Oriented Rectangle must be not null"; //$NON-NLS-1$
		set(obr.getCenterX(), obr.getCenterY(),
				obr.getFirstAxisX(), obr.getFirstAxisY(),
				obr.getFirstAxisExtent(),
				obr.getSecondAxisExtent());
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2fp(Iterable<? extends Point2D<?, ?>> pointCloud) {
		assert (pointCloud != null) : "List of points must be not null"; //$NON-NLS-1$
		setFromPointCloud(pointCloud);
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2fp(Point2D<?, ?>... pointCloud) {
		assert (pointCloud != null) : "List of points must be not null"; //$NON-NLS-1$
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
	public OrientedRectangle2fp(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2Extent) {
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert (axis1Extent >= 0.) : "Extent for the first axis must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Extent for the first axis must be positive or zero"; //$NON-NLS-1$
		set(centerX, centerY, axis1X, axis1Y, axis1Extent, axis2Extent);
	}

	/** Construct an oriented rectangle.
	 *
	 * @param center is the OBR center.
	 * @param axis1 is the first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	public OrientedRectangle2fp(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2Extent);
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.cx);
		bits = 31 * bits + Double.doubleToLongBits(this.cy);
		bits = 31 * bits + Double.doubleToLongBits(this.rx);
		bits = 31 * bits + Double.doubleToLongBits(this.ry);
		bits = 31 * bits + Double.doubleToLongBits(this.extentR);
		bits = 31 * bits + Double.doubleToLongBits(this.extentS);
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
	public Point2fp getCenter() {
		return new Point2fp(this.cx, this.cy);
	}

	@Pure
	@Override
	public double getCenterX() {
		return this.cx;
	}
	
	@Override
	public void setCenterX(double cx) {
		if (this.cx != cx) {
			this.cx = cx;
			fireGeometryChange();
		}
	}

	@Override
	public void setCenterY(double cy) {
		if (this.cy != cy) {
			this.cy = cy;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getCenterY() {
		return this.cy;
	}

	@Override
	public void setCenter(double cx, double cy) {
		if (this.cx != cx || this.cy != cy) {
			this.cx = cx;
			this.cy = cy;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public Vector2fp getFirstAxis() {
		return new Vector2fp(this.rx, this.ry);
	}

	@Pure
	@Override
	public double getFirstAxisX() {
		return this.rx;
	}

	@Pure
	@Override
	public double getFirstAxisY() {
		return this.ry;
	}

	@Pure
	@Override
	public Vector2fp getSecondAxis() {
		return new Vector2fp(-this.ry, this.rx);
	}

	@Pure
	@Override
	public double getSecondAxisX() {
		return -this.ry;
	}

	@Pure
	@Override
	public double getSecondAxisY() {
		return this.rx;
	}

	@Pure
	@Override
	public double getFirstAxisExtent() {
		return this.extentR;
	}

	@Override
	public void setFirstAxisExtent(double extent) {
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		if (this.extentR != extent) {
			this.extentR = extent;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getSecondAxisExtent() {
		return this.extentS;
	}

	@Override
	public void setSecondAxisExtent(double extent) {
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		if (this.extentS != extent) {
			this.extentS = extent;
			fireGeometryChange();
		}
	}

	@Override
	public void setFirstAxis(double x, double y, double extent) {
		assert (Vector2D.isUnitVector(x, y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		if (this.rx != x || this.ry != y || this.extentR != extent) {
			this.rx = x;
			this.ry = y;
			this.extentR = extent;
			fireGeometryChange();
		}
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert (Vector2D.isUnitVector(x, y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		if (this.rx != y || this.ry != -x || this.extentS != extent) {
			this.extentS = extent;
			this.rx = y;
			this.ry = -x;
			fireGeometryChange();
		}
	}

	@Override
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2Extent) {
		assert (Vector2D.isUnitVector(axis1x, axis1y)) : "First axis must be a unit vector"; //$NON-NLS-1$
		assert (axis1Extent >= 0.) : "First axis extent must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Second axis extent must be positive or zero"; //$NON-NLS-1$
		if (this.cx != centerY || this.cy != centerY || this.rx != axis1x || this.ry != axis1y
				|| this.extentR != axis1Extent || this.extentS != axis2Extent) {
			this.cx = centerX;
			this.cy = centerY;
			this.rx = axis1x;
			this.ry = axis1y;
			this.extentR = axis1Extent;
			this.extentS = axis2Extent;
			fireGeometryChange();
		}
	}

}

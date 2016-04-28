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

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** Parallelogram with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: mgrolleau$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Parallelogram2fp extends AbstractShape2fp<Parallelogram2fp>
	implements Parallelogram2afp<Shape2fp<?>, Parallelogram2fp, PathElement2fp, Point2fp, Vector2fp, Rectangle2fp> {

	private static final long serialVersionUID = 8945099277213684150L;

	/**
	 * Center of the parallelogram.
	 */
	private double cx;

	/**
	 * Center of the parallelogram.
	 */
	private double cy;

	/**
	 * X coordinate of the first axis of the parallelogram.
	 */
	private double rx;

	/**
	 * Y coordinate of the first axis of the parallelogram.
	 */
	private double ry;

	/**
	 * X coordinate of the second axis of the parallelogram.
	 */
	private double sx;

	/**
	 * Y coordinate of the second axis of the parallelogram.
	 */
	private double sy;

	/**
	 * Half-size of the first axis of the parallelogram.
	 */
	private double extentR;

	/**
	 * Half-size of the second axis of the parallelogram.
	 */
	private double extentS;

	/** Create an empty parallelogram.
	 */
	public Parallelogram2fp() {
		//
	}

	/** Create an parallelogram from the given parallelogram.
	 * 
	 * @param parallelogram
	 */
	public Parallelogram2fp(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert (parallelogram != null) : "Oriented Rectangle must be not null"; //$NON-NLS-1$
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
	public Parallelogram2fp(Iterable<? extends Point2D<?, ?>> pointCloud) {
		setFromPointCloud(pointCloud);
	}

	/** Construct a parallelogram from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public Parallelogram2fp(Point2D<?, ?>... pointCloud) {
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
	public Parallelogram2fp(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2X, double axis2Y, double axis2Extent) {
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "First axis must be a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Second axis must be a unit vector"; //$NON-NLS-1$
		assert (axis1Extent >= 0.) : "Extent for the first axis must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Extent for the first axis must be positive or zero"; //$NON-NLS-1$
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
	public Parallelogram2fp(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent, Vector2D<?, ?> axis2, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2, axis2Extent);
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
		bits = 31 * bits + Double.doubleToLongBits(this.sx);
		bits = 31 * bits + Double.doubleToLongBits(this.sy);
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
		this.cx = cx;
	}

	@Override
	public void setCenterY(double cy) {
		this.cy = cy;
	}

	@Pure
	@Override
	public double getCenterY() {
		return this.cy;
	}

	@Override
	public void setCenter(double cx, double cy) {
		this.cx = cx;
		this.cy = cy;
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
		return new Vector2fp(this.sx, this.sy);
	}

	@Pure
	@Override
	public double getSecondAxisX() {
		return this.sx;
	}

	@Pure
	@Override
	public double getSecondAxisY() {
		return this.sy;
	}

	@Pure
	@Override
	public double getFirstAxisExtent() {
		return this.extentR;
	}

	@Override
	public void setFirstAxisExtent(double extent) {
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		this.extentR = extent;
	}

	@Pure
	@Override
	public double getSecondAxisExtent() {
		return this.extentS;
	}

	@Override
	public void setSecondAxisExtent(double extent) {
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		this.extentS = extent;
	}

	@Override
	public void setFirstAxis(double x, double y, double extent) {
		assert (Vector2D.isUnitVector(x, y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		this.rx = x;
		this.ry = y;
		this.extentR = extent;
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert (Vector2D.isUnitVector(x, y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert (extent >= 0.) : "Extent must be positive or zero"; //$NON-NLS-1$
		this.sx = x;
		this.sy = y;
		this.extentS = extent;
	}

	@Override
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2x,
			double axis2y, double axis2Extent) {
		assert (Vector2D.isUnitVector(axis1x, axis1y)) : "First axis must be a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2x, axis2y)) : "First axis must be a unit vector"; //$NON-NLS-1$
		assert (axis1Extent >= 0.) : "First axis extent must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Second axis extent must be positive or zero"; //$NON-NLS-1$
		this.cx = centerX;
		this.cy = centerY;
		this.rx = axis1x;
		this.ry = axis1y;
		this.extentR = axis1Extent;
		this.sx = axis2x;
		this.sy = axis2y;
		this.extentS = axis2Extent;
	}

}

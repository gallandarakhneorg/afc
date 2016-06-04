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

package org.arakhne.afc.math.geometry.d2.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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
public class Parallelogram2d extends AbstractShape2d<Parallelogram2d>
		implements Parallelogram2afp<Shape2d<?>, Parallelogram2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

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
	public Parallelogram2d() {
		//
	}

	/** Create an parallelogram from the given parallelogram.
	 *
	 * @param parallelogram the parallelogram to copy.
	 */
	public Parallelogram2d(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : AssertMessages.notNullParameter();
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
	public Parallelogram2d(Iterable<? extends Point2D<?, ?>> pointCloud) {
		setFromPointCloud(pointCloud);
	}

	/** Construct a parallelogram from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public Parallelogram2d(Point2D<?, ?>... pointCloud) {
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
	@SuppressWarnings("checkstyle:magicnumber")
	public Parallelogram2d(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2X, double axis2Y, double axis2Extent) {
		assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
		assert Vector2D.isUnitVector(axis2X, axis2Y) : AssertMessages.normalizedParameters(5, 6);
		assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(7);
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
	public Parallelogram2d(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent,
			Vector2D<?, ?> axis2, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2, axis2Extent);
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.hashCode(this.cx);
		bits = 31 * bits + Double.hashCode(this.cy);
		bits = 31 * bits + Double.hashCode(this.rx);
		bits = 31 * bits + Double.hashCode(this.ry);
		bits = 31 * bits + Double.hashCode(this.extentR);
		bits = 31 * bits + Double.hashCode(this.sx);
		bits = 31 * bits + Double.hashCode(this.sy);
		bits = 31 * bits + Double.hashCode(this.extentS);
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

	@Pure
	@Override
	public Point2d getCenter() {
		return getGeomFactory().newPoint(this.cx, this.cy);
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
	public Vector2d getFirstAxis() {
		return getGeomFactory().newVector(this.rx, this.ry);
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
	public Vector2d getSecondAxis() {
		return getGeomFactory().newVector(this.sx, this.sy);
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
		assert extent >= 0. : AssertMessages.positiveOrZeroParameter();
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
		assert extent >= 0. : AssertMessages.positiveOrZeroParameter();
		if (this.extentS != extent) {
			this.extentS = extent;
			fireGeometryChange();
		}
	}

	@Override
	public void setFirstAxis(double x, double y, double extent) {
		assert Vector2D.isUnitVector(x, y) : AssertMessages.normalizedParameters(0, 1);
		assert extent >= 0. : AssertMessages.positiveOrZeroParameter(2);
		if (this.rx != x || this.ry != y || this.extentR != extent) {
			this.rx = x;
			this.ry = y;
			this.extentR = extent;
			fireGeometryChange();
		}
	}

	@Override
	public void setSecondAxis(double x, double y, double extent) {
		assert Vector2D.isUnitVector(x, y) : AssertMessages.normalizedParameters(0, 1);
		assert extent >= 0. : AssertMessages.positiveOrZeroParameter(2);
		if (this.sx != x || this.sy != y || this.extentS != extent) {
			this.sx = x;
			this.sy = y;
			this.extentS = extent;
			fireGeometryChange();
		}
	}

	@Override
	@SuppressWarnings({"checkstyle:booleanexpressioncomplexity", "checkstyle:magicnumber"})
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2x,
			double axis2y, double axis2Extent) {
		assert Vector2D.isUnitVector(axis1x, axis1y) : AssertMessages.normalizedParameters(2, 3);
		assert Vector2D.isUnitVector(axis2x, axis2y) : AssertMessages.normalizedParameters(5, 6);
		assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(7);
		if (this.cx != centerX || this.cy != centerY
				|| this.rx != axis1x || this.ry != axis1y || this.extentR != axis1Extent
				|| this.sx != axis2x || this.sy != axis2y || this.extentS != axis2Extent) {
			this.cx = centerX;
			this.cy = centerY;
			this.rx = axis1x;
			this.ry = axis1y;
			this.extentR = axis1Extent;
			this.sx = axis2x;
			this.sy = axis2y;
			this.extentS = axis2Extent;
			fireGeometryChange();
		}
	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import java.util.Arrays;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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
public class OrientedRectangle2d extends AbstractShape2d<OrientedRectangle2d>
		implements OrientedRectangle2afp<Shape2d<?>, OrientedRectangle2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = 7619908159953423088L;

	/**
	 * Center of the OBR.
	 */
	private double cx;

	/**
	 * Center of the OBR.
	 */
	private double cy;

	/**
	 * X coordinate of the first axis of the OBR.
	 */
	private double rx;

	/**
	 * Y coordinate of the first axis of the OBR.
	 */
	private double ry;

	/**
	 * Half-size of the first axis of the OBR.
	 */
	private double extentR;

	/**
	 * Half-size of the second axis of the OBR.
	 */
	private double extentS;

	/** Create an empty oriented rectangle.
	 */
	public OrientedRectangle2d() {
		//
	}

	/** Create an oriented rectangle from the given OBR.
	 *
	 * @param obr the oriented rectangle to copy.
	 */
	public OrientedRectangle2d(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> obr) {
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
	public OrientedRectangle2d(Iterable<? extends Point2D<?, ?>> pointCloud) {
		assert pointCloud != null : AssertMessages.notNullParameter();
		setFromPointCloud(pointCloud);
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2d(Point2D<?, ?>... pointCloud) {
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
	@SuppressWarnings("checkstyle:magicnumber")
	public OrientedRectangle2d(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2Extent) {
		assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
		assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(5);
		set(centerX, centerY, axis1X, axis1Y, axis1Extent, axis2Extent);
	}

	/** Construct an oriented rectangle.
	 *
	 * @param center is the OBR center.
	 * @param axis1 is the first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	public OrientedRectangle2d(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2Extent);
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.cx);
		bits = 31 * bits + Double.hashCode(this.cy);
		bits = 31 * bits + Double.hashCode(this.rx);
		bits = 31 * bits + Double.hashCode(this.ry);
		bits = 31 * bits + Double.hashCode(this.extentR);
		bits = 31 * bits + Double.hashCode(this.extentS);
		return bits ^ (bits >> 31);
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
		return getGeomFactory().newVector(-this.ry, this.rx);
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
		if (this.rx != y || this.ry != -x || this.extentS != extent) {
			this.extentS = extent;
			this.rx = y;
			this.ry = -x;
			fireGeometryChange();
		}
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2Extent) {
		assert Vector2D.isUnitVector(axis1x, axis1y) : AssertMessages.normalizedParameters(2, 3);
		assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(5);
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

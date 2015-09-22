/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.Arrays;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/**
 * Definition of a fixed Oriented Bounding Rectangle (OBR),
 * at least a 2D oriented bounding box.
 * <p>
 * Algo inspired from Mathematics for 3D Game Programming and Computer Graphics (MGPCG)
 * and from 3D Game Engine Design (GED)
 * and from Real Time Collision Detection (RTCD).
 * <p>
 * Rotations are not managed yet.
 * 
 * @author $Author: mgrolleau$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see <a href="http://www.terathon.com/books/mathgames2.html">Mathematics for 3D Game Programming &amp; Computer Graphics</a>
 */
public class OrientedRectangle2f extends AbstractOrientedRectangle2F<OrientedRectangle2f> {	

	private static final long serialVersionUID = -7541381548010183873L;


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
	 * X coordinate of the second axis of the OBR
	 */
	private double sx;

	/**
	 * Y coordinate of the second axis of the OBR
	 */
	private double sy;

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
	public OrientedRectangle2f() {
		//
	}

	/** Create an oriented rectangle from the given OBR.
	 * 
	 * @param obr
	 */
	public OrientedRectangle2f(OrientedRectangle2f obr) {
		this.cx = obr.cx;
		this.cy = obr.cy;
		this.rx = obr.rx;
		this.ry = obr.ry;
		this.sx = obr.sx;
		this.sy = obr.sy;
		this.extentR = obr.extentR;
		this.extentS = obr.extentS;
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2f(Iterable<? extends Point2D> pointCloud) {
		setFromPointCloud(pointCloud);
	}

	/** Construct an oriented rectangle from the given cloud of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public OrientedRectangle2f(Point2D[] pointCloud) {
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
	public OrientedRectangle2f(double centerX, double centerY,
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
	public OrientedRectangle2f(Point2D center, Vector2D axis1, double axis1Extent, double axis2Extent) {
		set(center, axis1, axis1Extent, axis2Extent);
	}

	/** Replies the center.
	 *
	 * @return the center.
	 */
	public Point2f getCenter() {
		return new Point2f(this.cx, this.cy);
	}

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	public double getCenterX() {
		return this.cx;
	}

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	public double getCenterY() {
		return this.cy;
	}

	/** Set the center.
	 * 
	 * @param cx1 the center x.
	 * @param cy1 the center y.
	 */
	public void setCenter(double cx1, double cy1) {
		this.cx = cx1;
		this.cy = cy1;
	}

	/** Set the center.
	 * 
	 * @param center
	 */
	public void setCenter(Point2D center) {
		setCenter(center.getX(), center.getY());
	}

	/** Replies the first axis of the oriented rectangle.
	 *
	 * @return the unit vector of the first axis. 
	 */
	public Vector2f getFirstAxis() {
		return new Vector2f(this.rx, this.ry);
	}

	/** Replies coordinate x of the first axis of the oriented rectangle.
	 *
	 * @return the coordinate x of the unit vector of the first axis. 
	 */
	public double getFirstAxisX() {
		return this.rx;
	}

	/** Replies coordinate y of the first axis of the oriented rectangle.
	 *
	 * @return the coordinate y of the unit vector of the first axis. 
	 */
	public double getFirstAxisY() {
		return this.ry;
	}

	/** Replies the second axis of the oriented rectangle.
	 *
	 * @return the unit vector of the second axis. 
	 */
	public Vector2f getSecondAxis() {
		return new Vector2f(this.sx, this.sy);
	}

	/** Replies coordinate x of the second axis of the oriented rectangle.
	 *
	 * @return the coordinate x of the unit vector of the second axis. 
	 */
	public double getSecondAxisX() {
		return this.sx;
	}

	/** Replies coordinate y of the second axis of the oriented rectangle.
	 *
	 * @return the coordinate y of the unit vector of the second axis. 
	 */
	public double getSecondAxisY() {
		return this.sy;
	}

	/** Replies the demi-size of the rectangle along its first axis.
	 * 
	 * @return the extent along the first axis.
	 */
	public double getFirstAxisExtent() {
		return this.extentR;
	}

	/** Change the demi-size of the rectangle along its first axis.
	 * 
	 * @param extent - the extent along the first axis.
	 */
	public void setFirstAxisExtent(double extent) {
		this.extentR = Math.max(extent, 0);
	}

	/** Replies the demi-size of the rectangle along its second axis.
	 * 
	 * @return the extent along the second axis.
	 */
	public double getSecondAxisExtent() {
		return this.extentS;
	}

	/** Change the demi-size of the rectangle along its second axis.
	 * 
	 * @param extent - the extent along the second axis.
	 */
	public void setSecondAxisExtent(double extent) {
		this.extentS = Math.max(extent, 0);
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	public void setFirstAxis(Vector2D axis) {
		setFirstAxis(axis.getX(), axis.getY(), getFirstAxisExtent());
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	public void setFirstAxis(Vector2D axis, double extent) {
		setFirstAxis(axis.getX(), axis.getY(), extent);
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param x
	 * @param y
	 */
	public void setFirstAxis(double x, double y) {
		setFirstAxis(x, y, getFirstAxisExtent());
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param x
	 * @param y
	 * @param extent
	 */
	public void setFirstAxis(double x, double y, double extent) {
		Vector2f axis2 = new Vector2f(x, y);

		assert(axis2.isUnitVector());

		this.rx = x;
		this.ry = y;

		axis2.perpendicularize();
		this.sx = axis2.getX();
		this.sy = axis2.getY();

		this.extentR = extent;
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	public void setSecondAxis(Vector2D axis) {
		setSecondAxis(axis.getX(), axis.getY(), getSecondAxisExtent());
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	public void setSecondAxis(Vector2D axis, double extent) {
		setSecondAxis(axis.getX(), axis.getY(), extent);
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param x - the new values for the first axis.
	 * @param y - the new values for the first axis.
	 */
	public void setSecondAxis(double x, double y) {
		setSecondAxis(x, y, getSecondAxisExtent());
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param x
	 * @param y
	 * @param extent
	 */
	public void setSecondAxis(double x, double y, double extent) {
		Vector2f axis1 = new Vector2f(x, y);

		assert(axis1.isUnitVector());
		this.sx = x;
		this.sy = y;

		axis1.perpendicularize();
		axis1.negate();
		this.rx = axis1.getX();
		this.ry = axis1.getY();

		this.extentS = extent;
	}

	
	@Override
	public void clear() {
		this.extentR = 0.;
		this.extentS = 0.;
		this.rx = 0.;
		this.ry = 0.;
		this.sx = 0.;
		this.sy = 0.;
		this.cx = 0.;
		this.cy = 0.;
	}

	@Override
	public void set(final Shape2F s) {
		if (s instanceof OrientedRectangle2f) {
			OrientedRectangle2f obr = (OrientedRectangle2f) s;
			set(obr.getCenterX(), obr.getCenterY(),
					obr.getFirstAxisX(), obr.getFirstAxisY(), obr.getFirstAxisExtent(),
					obr.getSecondAxisX(), obr.getSecondAxisY(), obr.getSecondAxisExtent());
		} else {
			setFromPointCloud(new Iterable<Point2f>() {
				@Override
				public Iterator<Point2f> iterator() {
					return new PointIterator(s.getPathIterator());
				}
			});
		}
	}

	/** Set the oriented rectangle.
	 * The second axis is automatically computed.
	 *
	 * @param center is the OBR center.
	 * @param axis1 is the first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	public void set(Point2D center, Vector2D axis1, double axis1Extent, double axis2Extent) {
		set(center.getX(), center.getY(),
				axis1.getX(), axis1.getY(),
				axis1Extent, axis2Extent);
	}

	/** Set the oriented rectangle.
	 * The second axis is automatically computed.
	 *
	 * @param centerX is the X coordinate of the OBR center.
	 * @param centerY is the Y coordinate of the OBR center.
	 * @param axis1X is the X coordinate of first axis of the OBR.
	 * @param axis1Y is the Y coordinate of first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	public void set(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2Extent) {
		assert (new Vector2f(axis1X, axis1Y).isUnitVector());
			Vector2f axis2 = new Vector2f(axis1X, axis1Y);
		axis2.perpendicularize();
		assert (axis2.isUnitVector());
		set(centerX, centerY,
				axis1X, axis1Y, axis1Extent,
				axis2.getX(), axis2.getY(), axis2Extent);
	}

	/** Set the oriented rectangle.
	 *
	 * @param centerX is the X coordinate of the OBR center.
	 * @param centerY is the Y coordinate of the OBR center.
	 * @param axis1X is the X coordinate of first axis of the OBR.
	 * @param axis1Y is the Y coordinate of first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2X is the X coordinate of second axis of the OBR.
	 * @param axis2Y is the Y coordinate of second axis of the OBR.
	 * @param axis2Extent is the extent of the second axis.
	 */
	protected void set(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2X, double axis2Y, double axis2Extent) {
		assert (new Vector2f(axis1X, axis1Y).isUnitVector());
		assert (axis1Extent >= 0.);
		assert (new Vector2f(axis2X, axis2Y).isUnitVector());
		assert (axis2Extent >= 0.);

		this.cx = centerX;
		this.cy = centerY;
		this.rx = axis1X;
		this.ry = axis1Y;
		this.sx = axis2X;
		this.sy = axis2Y;
		this.extentR = axis1Extent;
		this.extentS = axis2Extent;
	}

	/** Set the oriented rectangle from a could of points.
 *
	 * @param pointCloud - the cloud of points.
	 */
	public void setFromPointCloud(Iterable<? extends Point2D> pointCloud) {
		Vector2f r = new Vector2f();
		Vector2f s = new Vector2f();
		computeOBRAxis(pointCloud, r, s);
		Point2f center = new Point2f();
		Vector2f extents = new Vector2f();
		computeOBRCenterExtents(pointCloud, r, s, center, extents);
		set(center.getX(), center.getY(),
				r.getX(), r.getY(), extents.getX(),
				s.getX(), s.getY(), extents.getY());
	}

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public void setFromPointCloud(Point2D... pointCloud) {
		setFromPointCloud(Arrays.asList(pointCloud));
	}

}

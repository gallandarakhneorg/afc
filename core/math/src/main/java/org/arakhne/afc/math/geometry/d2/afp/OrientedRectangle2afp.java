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

package org.arakhne.afc.math.geometry.d2.afp;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp.CrossingComputationType;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D oriented rectangle on a plane.
 * An oriented rectangle is a parallelogram with orthogonal axes.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedRectangle2afp<
ST extends Shape2afp<?, ?, IE, P, B>,
IT extends OrientedRectangle2afp<?, ?, IE, P, B>,
IE extends PathElement2afp,
P extends Point2D,
B extends Rectangle2afp<?, ?, IE, P, B>>
extends Shape2afp<ST, IT, IE, P, B> {

	/** Project the given vector on the R axis, assuming S axis is orthogonal.
	 *
	 * <p>This function assumes that axes are orthogonal. For a general projection on the R axis,
	 * see {@link Parallelogram2afp#projectVectorOnParallelogramRAxis(double, double, double, double, double, double)}.
	 *
	 * @param rx the x coordinate of the R axis.
	 * @param ry the y coordinate of the R axis.
	 * @param x the x coordinate of the vector.
	 * @param y the y coordinate of the vector.
	 * @return the coordinate of the projection of the vector on R
	 * @see Parallelogram2afp#projectVectorOnParallelogramRAxis(double, double, double, double, double, double)
	 */
	@Pure
	static double projectVectorOnOrientedRectangleRAxis(double rx, double ry, double x,  double y) {
		assert (Vector2D.isUnitVector(rx, ry)) : "Vector R is not a unit vector"; //$NON-NLS-1$
		return Vector2D.dotProduct(x, y, rx, ry);
	}

	/** Project the given vector on the S axis, assuming R axis is orthogonal.
	 *
	 * <p>This function assumes that axes are orthogonal. For a general projection on the S axis,
	 * see {@link Parallelogram2afp#projectVectorOnParallelogramSAxis(double, double, double, double, double, double)}.
	 *
	 * @param rx the x coordinate of the R axis (NOT the S axis).
	 * @param ry the y coordinate of the R axis (NOT the S axis).
	 * @param x the x coordinate of the vector.
	 * @param y the y coordinate of the vector.
	 * @return the coordinate of the projection of the vector on S
	 * @see Parallelogram2afp#projectVectorOnParallelogramSAxis(double, double, double, double, double, double)
	 */
	@Pure
	static double projectVectorOnOrientedRectangleSAxis(double rx, double ry, double x,  double y) {
		assert (Vector2D.isUnitVector(rx, ry)) : "Vector R is not a unit vector"; //$NON-NLS-1$
		return Vector2D.dotProduct(x, y, -ry, rx);
	}

	/**
	 * Compute the center and extents of an oriented rectangle from a set of points and the oriented rectangle axes.
	 *
	 * <p>This function assumes orthogonal axes, in opposite to
	 * {@link Parallelogram2afp#computeCenterExtents(Iterable, Vector2D, Vector2D, Point2D, Tuple2D)}, which
	 * assumes not constraint on the axes.
	 * 
	 * @param points is the list of the points enclosed by the oriented rectangle.
	 * @param R is the R axis of the oriented rectangle.
	 * @param center is the point which is set with the parallogram's center coordinates.
	 * @param extents are the extents of the parallogram for the R and S axis.
	 * @see "MGPCG pages 222-223 (oriented bounding box)"
	 * @see Parallelogram2afp#computeCenterExtents(Iterable, Vector2D, Vector2D, Point2D, Tuple2D)
	 */
	static void computeCenterExtents(
			Iterable<? extends Point2D> points,
			Vector2D R,
			Point2D center, Tuple2D<?> extents) {
		assert(points != null) : "Collection of points must be not null"; //$NON-NLS-1$
		assert(R != null) : "First axis vector must be not null"; //$NON-NLS-1$
		assert(R.isUnitVector()) : "First axis vector must be unit vector"; //$NON-NLS-1$
		assert(center != null) : "Center point must be not null"; //$NON-NLS-1$
		assert(extents != null) : "Extent tuple must be not null"; //$NON-NLS-1$

		double minR = Double.POSITIVE_INFINITY;
		double maxR = Double.NEGATIVE_INFINITY;
		double minS = Double.POSITIVE_INFINITY;
		double maxS = Double.NEGATIVE_INFINITY;

		double ux = R.getX();
		double uy = R.getY();
		double vx = -uy;
		double vy = ux;

		double PdotR;
		double PdotS;
		for(Point2D tuple : points) {
			PdotR = projectVectorOnOrientedRectangleRAxis(ux, uy, tuple.getX(), tuple.getY());
			PdotS = projectVectorOnOrientedRectangleSAxis(ux, uy, tuple.getX(), tuple.getY());

			if (PdotR < minR) minR = PdotR;			
			if (PdotR > maxR) maxR = PdotR;			
			if (PdotS < minS) minS = PdotS;			
			if (PdotS > maxS) maxS = PdotS;			
		}

		double a = (maxR + minR) / 2.;
		double b = (maxS + minS) / 2.;

		// Set the center of the oriented rectangle
		center.set(
				a * ux
				+b * vx,

				a * uy
				+b * vy);

		// Compute extents
		extents.set(
				(maxR - minR) / 2.,
				(maxS - minS) / 2.);
	}

	/** Replies if a point is inside in the oriented rectangle.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
	 * @param centerX
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @return <code>true</code> if the given point is inside the oriented rectangle;
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsOrientedRectanglePoint(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double px, double py) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		double x = px - centerX;
		double y = py - centerY;
		double coordinate = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		return (coordinate >= -axis2Extent && coordinate <= axis2Extent);
	}

	/** Replies if a point is inside the oriented rectangle.
	 * 
	 * @param centerX
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @param rx
	 *            is the X coordinate of the lower point of the rectangle.
	 * @param ry
	 *            is the Y coordinate of the lower point of the rectangle.
	 * @param rwidth
	 *            is the width of the rectangle.
	 * @param rheight
	 *            is the height of the rectangle.
	 * @return <code>true</code> if the given rectangle is inside the oriented rectangle;
	 * otherwise <code>false</code>.
	 */
	@Pure
	@Unefficient
	static boolean containsOrientedRectangleRectangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double rx, double ry,
			double rwidth, double rheight) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (rwidth >= 0) : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0) : "Height of the rectangle must be positive or zero"; //$NON-NLS-1$

		double x, y, coordinate;

		double basex = rx - centerX;
		double basey = ry - centerY;

		x = basex;
		y = basey;
		coordinate = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis2Extent || coordinate > axis2Extent) {
			return false;
		}

		x = basex + rwidth;
		coordinate = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis2Extent || coordinate > axis2Extent) {
			return false;
		}

		y = basey + rheight;
		coordinate = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis2Extent || coordinate > axis2Extent) {
			return false;
		}

		x = basex;
		coordinate = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		return (coordinate >= -axis2Extent && coordinate <= axis2Extent);
	}

	/**
	 * Given a point p, this function computes the point q1 on (or in) this oriented rectangle,
	 * closest to p; and the point q2 on the oriented rectangle, farthest to p. If there are several
	 * points, the function will return one of those. Remember this function may
	 * return an approximate result when points remain on oriented rectangle plane of symmetry.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
	 * @param centerX
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @param closest the closest point. If <code>null</code>, the closest point is not computed.
	 * @param farthest the farthest point. If <code>null</code>, the farthest point is not computed.
	 */
	static void computeClosestFarthestPoints(
			double px, double py,
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			Point2D closest, Point2D farthest) {
		assert (axis1Extent >= 0.) : "Extent of the first axis must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Extent of the second axis must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (closest != null || farthest != null) : "Neither closest point nor farthest point has a result vector"; //$NON-NLS-1$

		double dx = px - centerX;
		double dy = py - centerY;

		// For each axis project d onto that axis to get the distance along
		// the axis of d from the parallelogram center
		double d1 = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, dx, dy);
		double d2 = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, dx, dy);

		if (closest != null) {
			double clampedD1 = MathUtil.clamp(d1, -axis1Extent, axis1Extent);
			double clampedD2 = MathUtil.clamp(d2, -axis2Extent, axis2Extent);

			// Step that distance along the axis to get world coordinate
			// q1 += dist * this.axis[i]; (If distance farther than the box
			// extents, clamp to the box)
			closest.set(
					centerX + clampedD1 * axis1X - clampedD2 * axis1Y,
					centerY + clampedD1 * axis1Y + clampedD2 * axis1X);
		}

		if (farthest != null) {
			// Clamp to the other side of the box
			if (d1 >= 0.) {
				d1 = -axis1Extent;
			} else {
				d1 = axis1Extent;
			}
			if (d2 >= 0.) {
				d2 = -axis2Extent;
			} else {
				d2 = axis2Extent;
			}

			// Step that distance along the axis to get world coordinate
			// q2 += dist * this.axis[i];
			farthest.set(
					centerX + d1 * axis1X - d2 * axis1Y,
					centerY + d1 * axis1Y + d2 * axis1X);

		}
	}

	/** Replies if the specified rectangle intersects the specified segment.
	 *
	 * @param centerX
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @param s1x is the X coordinate of the first point of the segment.
	 * @param s1y is the Y coordinate of the first point of the segment.
	 * @param s2x is the X coordinate of the second point of the segment.
	 * @param s2y is the Y coordinate of the second point of the segment.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	@Pure
	static boolean intersectsOrientedRectangleSegment(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double s1x, double s1y, double s2x, double s2y) {
		assert (axis1Extent >= 0.) : "Axis 1 extent must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Axis 2 extent must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 must be a unit vector"; //$NON-NLS-1$
		//Changing Segment coordinate basis.
		double x = s1x - centerX;
		double y = s1y - centerY;
		double ax = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		double ay = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		x = s2x - centerX;
		y = s2y - centerY;
		double bx = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		double by = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);

		return Rectangle2afp.intersectsRectangleSegment(
				-axis1Extent, -axis2Extent, axis1Extent, axis2Extent,
				ax, ay,  bx,  by);
	}

	/** Replies if the specified rectangle intersects the specified triangle.
	 *
	 * @param centerX
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @param s1x is the X coordinate of the first point of the triangle.
	 * @param s1y is the Y coordinate of the first point of the triangle.
	 * @param s2x is the X coordinate of the second point of the triangle.
	 * @param s2y is the Y coordinate of the second point of the triangle.
	 * @param s3x is the X coordinate of the third point of the triangle.
	 * @param s3y is the Y coordinate of the third point of the triangle.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	@Pure
	static boolean intersectsOrientedRectangleTriangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double s1x, double s1y, double s2x, double s2y, double s3x, double s3y) {
		assert (axis1Extent >= 0.) : "Axis 1 extent must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Axis 2 extent must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 must be a unit vector"; //$NON-NLS-1$
		//Changing Triangle coordinate basis.
		double x = s1x - centerX;
		double y = s1y - centerY;
		double ax = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		double ay = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		x = s2x - centerX;
		y = s2y - centerY;
		double bx = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		double by = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);
		x = s3x - centerX;
		y = s3y - centerY;
		double cx = projectVectorOnOrientedRectangleRAxis(axis1X, axis1Y, x, y);
		double cy = projectVectorOnOrientedRectangleSAxis(axis1X, axis1Y, x, y);

		return Triangle2afp.intersectsTriangleRectangle(
				ax, ay,  bx,  by, cx, cy,
				-axis1Extent, -axis2Extent, 2. * axis1Extent, 2. * axis2Extent);
	}

	/** Replies if the parallelogram intersects the given ellipse.
	 *
	 * @param centerX
	 *            is the X coordinate of the parallelogram center.
	 * @param centerY
	 *            is the Y coordinate of the parallelogram center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the parallelogram.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the parallelogram.
	 * @param ex is the coordinate of the min point of the ellipse rectangle.
	 * @param ey is the coordinate of the min point of the ellipse rectangle.
	 * @param ewidth is the width of the ellipse.
	 * @param eheight is the height of the ellipse.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	@Pure
	@Unefficient
	static boolean intersectsOrientedRectangleEllipse(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double ex, double ey, double ewidth, double eheight){
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (ewidth >= 0) : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (eheight >= 0) : "Height of the rectangle must be positive or zero"; //$NON-NLS-1$

		if (ewidth <= 0 || eheight <= 0) {
			return false;
		}

		// Convert the oriented rectangle elements so that the ellipse is transformed to a circle centered at the origin.
		double a = ewidth / 2.;
		double b = eheight / 2.;

		double translateX = ex + a;
		double translateY = ey + b;

		double transCenterX = (centerX - translateX) / a;
		double transCenterY = (centerY - translateY) / b;

		double transAxis1X = axis1Extent * axis1X / a;
		double transAxis1Y = axis1Extent * axis1Y / b;
		double length1 = Math.hypot(transAxis1X, transAxis1Y);
		transAxis1X /= length1;
		transAxis1Y /= length1;

		double transAxis2X = axis2Extent * -axis1Y / a;
		double transAxis2Y = axis2Extent * axis1X / b;
		double length2 = Math.hypot(transAxis2X, transAxis2Y);
		transAxis2X /= length2;
		transAxis2Y /= length2;

		return Parallelogram2afp.intersectsParallelogramCircle(
				transCenterX, transCenterY,
				transAxis1X, transAxis1Y, length1,
				transAxis2X, transAxis2Y, length2,
				0, 0, 1);
	}

	/** Replies if the specified parallelogram intersects the specified circle.
	 *
	 * @param centerX
	 *            is the X coordinate of the parallelogram center.
	 * @param centerY
	 *            is the Y coordinate of the parallelogram center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the parallelogram.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the parallelogram.
	 * @param circleX is the coordinate of the circle center.
	 * @param circleY is the coordinate of the circle center.
	 * @param circleRadius is the radius of the circle.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	@Pure
	static boolean intersectsOrientedRectangleCircle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double circleX, double circleY, double circleRadius) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (circleRadius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				circleX, circleY,
				centerX, centerY,
				axis1X, axis1Y, axis1Extent,
				axis2Extent,
				closest, null);
		// Circle and oriented rectangle intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		double squaredRadius = circleRadius * circleRadius;

		return Point2D.getDistanceSquaredPointPoint(
				circleX, circleY,
				closest.getX(), closest.getY()) <= squaredRadius;
	}

	/** Replies if the specified rectangles intersect.
	 * <p>
	 * The extents are assumed to be positive or zero.
	 * The lengths of the given arrays are assumed to be <code>2</code>.
	 * <p>
	 * This function uses the "separating axis theorem" which states that 
	 * for any two oriented rectangles (AABB is a special case of oriented rectangle)
	 * that do not touch, a separating axis can be found.
	 * <p>
	 * This function uses an general intersection test between two oriented rectangle.
	 * If the first box is expected to be an MBR, please use the
	 * optimized algorithm given by
	 * {@link #intersectsOrientedRectangleRectangle(double, double, double, double, double, double, double, double, double, double)}.
	 *
	 * @param centerX1
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY1
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X1
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y1
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent1
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent1
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @param centerX2
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY2
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X2
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y2
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent2
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent2
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.jkh.me/files/tutorials/Separating%20Axis%20Theorem%20for%20Oriented%20Bounding%20Boxes.pdf">Intersection between two oriented boudning rectangles</a>
	 */
	@Pure
	static boolean intersectsOrientedRectangleOrientedRectangle(
			double centerX1, double centerY1,
			double axis1X1, double axis1Y1,
			double axis1Extent1,
			double axis2Extent1,
			double centerX2, double centerY2,
			double axis1X2, double axis1Y2,
			double axis1Extent2,
			double axis2Extent2) {
		assert(axis1Extent1 >= 0.) : "First axis extent of the first rectangle must be positive or zero"; //$NON-NLS-1$
		assert(axis2Extent1 >= 0.) : "Second axis extent of the first rectangle must be positive or zero"; //$NON-NLS-1$
		assert(axis1Extent2 >= 0.) : "First axis extent of the second rectangle must be positive or zero"; //$NON-NLS-1$
		assert(axis2Extent2 >= 0.) : "Second axis extent of the second rectangle must be positive or zero"; //$NON-NLS-1$

		double Tx = centerX2 - centerX1;
		double Ty = centerY2 - centerY1;

		// Let A the first box and B the second one.
		// Let Ax and Ay the axes of A.
		// Let WA the extent related to Ax.
		// Let HA the extent related to Ay.
		// Let Bx and By the axes of B.
		// Let WB the extent related to Bx.
		// Let HB the extent related to By.

		// Case 1: 
		//   L = Ax
		//   L is a separating axis iff:
		//   | T.Ax | > WA + | ( WB * Bx ) . Ax | + |( HB * By ) . Ax |
		double absTAx = Math.abs(projectVectorOnOrientedRectangleRAxis(axis1X1, axis1Y1, Tx, Ty));
		double Bxx = axis1Extent2 * axis1X2;
		double Bxy = axis1Extent2 * axis1Y2;
		double absBxAx = Math.abs(projectVectorOnOrientedRectangleRAxis(axis1X1, axis1Y1, Bxx, Bxy));
		double Byx = axis2Extent2 * -axis1Y2;
		double Byy = axis2Extent2 * axis1X2;
		double absByAx = Math.abs(projectVectorOnOrientedRectangleRAxis(axis1X1, axis1Y1, Byx, Byy));
		if ( absTAx > (axis1Extent1 + absBxAx + absByAx)) {
			return false;
		}

		// Case 2:
		//   L = Ay
		//   L is a separating axis iff:
		//   | T.Ay | > HA + | ( WB * Bx ) . Ay | + |( HB * By ) . Ay |
		double absTAy = Math.abs(projectVectorOnOrientedRectangleSAxis(axis1X1, axis1Y1, Tx, Ty));
		double absBxAy = Math.abs(projectVectorOnOrientedRectangleRAxis(-axis1Y1, axis1X1, Bxx, Bxy));
		double absByAy = Math.abs(projectVectorOnOrientedRectangleRAxis(-axis1Y1, axis1X1, Byx, Byy));
		if (absTAy > (axis2Extent1 + absBxAy + absByAy)) {
			return false;
		}

		// Case 3:
		//   L = Bx
		//   L is a separating axis iff:
		//   | T . Bx | > | ( WA * Ax ) . Bx | + | ( HA * Ay ) . Bx | + WB
		double absTBx = Math.abs(projectVectorOnOrientedRectangleRAxis(axis1X2, axis1Y2, Tx, Ty));
		double Axx = axis1Extent1 * axis1X1;
		double Axy = axis1Extent1 * axis1Y1;
		double absAxBx = Math.abs(projectVectorOnOrientedRectangleRAxis(axis1X2, axis1Y2, Axx, Axy));
		double Ayx = axis2Extent1 * -axis1Y1;
		double Ayy = axis2Extent1 * axis1X1;
		double absAyBx = Math.abs(projectVectorOnOrientedRectangleRAxis(axis1X2, axis1Y2, Ayx, Ayy));		
		if (absTBx > (absAxBx + absAyBx + axis1Extent2)) {
			return false;
		}

		// Case 4:
		//   L = By
		//   L is a separating axis iff:
		//   | T . By | > | ( WA * Ax ) . By | + | ( HA * Ay ) . By | + HB
		double absTBy = Math.abs(projectVectorOnOrientedRectangleRAxis(-axis1Y2, axis1X2, Tx, Ty));
		double absAxBy = Math.abs(projectVectorOnOrientedRectangleRAxis(-axis1Y2, axis1X2, Axx, Axy));
		double absAyBy = Math.abs(projectVectorOnOrientedRectangleRAxis(-axis1Y2, axis1X2, Ayx, Ayy));
		if (absTBy > (absAxBy + absAyBy + axis2Extent2)) {
			return false;
		}

		// No separating axis found, the two boxes are overlaping.
		return true;
	}

	/** Replies if the specified rectangles intersect.
	 * <p>
	 * This function is assuming that <var>lx1</var> is lower
	 * or equal to <var>ux1</var>, and <var>ly1</var> is lower
	 * or equal to <var>uy1</var>.
	 * The extents are assumed to be positive or zero.
	 * The lengths of the given arrays are assumed to be <code>2</code>.
	 * <p>
	 * This function uses the "separating axis theorem" which states that 
	 * for any two oriented rectangles (AABB is a special case of oriented rectangle)
	 * that do not touch, a separating axis can be found.
	 * <p>
	 * This function uses an optimized algorithm for AABB as first parameter.
	 * The general intersection type between two oriented rectangle is given by
	 * {@link #intersectsOrientedRectangleOrientedRectangle}.
	 *
	 * @param centerX
	 *            is the X coordinate of the oriented rectangle center.
	 * @param centerY
	 *            is the Y coordinate of the oriented rectangle center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the oriented rectangle.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the oriented rectangle.
	 * @param rx
	 *            is the X coordinate of the lower point of the rectangle.
	 * @param ry
	 *            is the Y coordinate of the lower point of the rectangle.
	 * @param rwidth
	 *            is the width of the rectangle.
	 * @param rheight
	 *            is the height of the rectangle.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
	 */
	@Pure
	static boolean intersectsOrientedRectangleRectangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double rx, double ry,
			double rwidth, double rheight) {
		assert (rwidth >= 0.) : "Rectangle width must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0.) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		assert (axis1Extent >= 0.) : "Axis 1 extent for the oriented rectangle must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Axis 2 extent for the oriented rectangle must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 of the oriented rectangle must be a unit vector"; //$NON-NLS-1$
		double rx2 = rx + rwidth;
		double ry2 = ry + rheight;
		double axis2X = -axis1Y;
		double axis2Y = axis1X;
		// Test border intersections
		double px1 = centerX + axis1Extent * axis1X + axis2Extent * axis2X;
		double py1 = centerY + axis1Extent * axis1Y + axis2Extent * axis2Y;
		double px2 = centerX - axis1Extent * axis1X + axis2Extent * axis2X;
		double py2 = centerY - axis1Extent * axis1Y + axis2Extent * axis2Y;
		if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
				px1, py1, px2, py2)) {
			return true;
		}
		double px3 = centerX - axis1Extent * axis1X - axis2Extent * axis2X;
		double py3 = centerY - axis1Extent * axis1Y - axis2Extent * axis2Y;
		if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
				px2, py2, px3, py3)) {
			return true;
		}
		double px4 = centerX + axis1Extent * axis1X - axis2Extent * axis2X;
		double py4 = centerY + axis1Extent * axis1Y - axis2Extent * axis2Y;
		if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
				px3, py3, px4, py4)) {
			return true;
		}
		if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
				px4, py4, px1, py1)) {
			return true;
		}

		// The rectangle is entirely outside or entirely inside the parallelogram.

		// Test if one rectangle point is inside the parallelogram.
		// We need to test only one point from the rectangle, since if the first
		// point is not inside, the other three points are not too.
		if (containsOrientedRectanglePoint(
				centerX, centerY, axis1X, axis1Y, axis1Extent, axis2Extent, rx, ry)) {
			return true;
		}

		// Test if one parallelogram point is inside the rectangle
		// We need to test only one point from the rectangle, since if the first
		// point is not inside, the other three points are not too.
		return (Rectangle2afp.containsRectanglePoint(rx, ry, rx2, ry2, px1, py1));
	}

	/**  Replies if the oriented rectangle intersects the given rectangle.
	 * 
	 * @param centerX
	 *            is the X coordinate of the parallelogram center.
	 * @param centerY
	 *            is the Y coordinate of the parallelogram center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the parallelogram.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the parallelogram.
	 * @param rx
	 *            is the X coordinate of the lower point of the rectangle.
	 * @param ry
	 *            is the Y coordinate of the lower point of the rectangle.
	 * @param rwidth
	 *            is the width of the rectangle.
	 * @param rheight
	 *            is the height of the rectangle.
	 * @param rArcWidth
	 *            is the width of the rectangle arcs.
	 * @param rArcHeight
	 *            is the height of the rectangle arcs.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>.
	 */
	@Pure
	@Unefficient
	static boolean intersectsOrientedRectangleRoundRectangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2Extent,
			double rx, double ry,
			double rwidth, double rheight,
			double rArcWidth, double rArcHeight) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (rwidth >= 0) : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0) : "Height of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (rArcWidth >= 0) : "Arc width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (rArcHeight >= 0) : "Arc height of the rectangle must be positive or zero"; //$NON-NLS-1$

		double rx2 = rx + rwidth;
		double ry2 = ry + rheight;
		double rxmin = rx + rArcWidth;
		double rxmax = rx2 - rArcWidth;
		double rymin = ry + rArcHeight;
		double rymax = ry2 - rArcHeight;
		double ew = rArcWidth * 2;
		double eh = rArcWidth * 2;
		double emaxx = rxmax - rArcWidth;
		double emaxy = rymax - rArcHeight;
		double axis2X = -axis1Y;
		double axis2Y = axis1X;
		// Test border intersections
		double px1 = centerX + axis1Extent * axis1X + axis2Extent * axis2X;
		double py1 = centerY + axis1Extent * axis1Y + axis2Extent * axis2Y;
		double px2 = centerX - axis1Extent * axis1X + axis2Extent * axis2X;
		double py2 = centerY - axis1Extent * axis1Y + axis2Extent * axis2Y;
		if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
				px1, py1, px2, py2)
				|| Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax,
						px1, py1, px2, py2)
				|| Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh,
						px1, py1, px2, py2, false)
				|| Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh,
						px1, py1, px2, py2, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh,
						px1, py1, px2, py2, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh,
						px1, py1, px2, py2, false)) {
			return true;
		}
		double px3 = centerX - axis1Extent * axis1X - axis2Extent * axis2X;
		double py3 = centerY - axis1Extent * axis1Y - axis2Extent * axis2Y;
		if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
				px2, py2, px3, py3)
				|| Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax,
						px2, py2, px3, py3)
				|| Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh,
						px2, py2, px3, py3, false)
				|| Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh,
						px2, py2, px3, py3, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh,
						px2, py2, px3, py3, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh,
						px2, py2, px3, py3, false)) {
			return true;
		}
		double px4 = centerX + axis1Extent * axis1X - axis2Extent * axis2X;
		double py4 = centerY + axis1Extent * axis1Y - axis2Extent * axis2Y;
		if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
				px3, py3, px4, py4)
				|| Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax,
						px3, py3, px4, py4)
				|| Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh,
						px3, py3, px4, py4, false)
				|| Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh,
						px3, py3, px4, py4, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh,
						px3, py3, px4, py4, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh,
						px3, py3, px4, py4, false)) {
			return true;
		}
		if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
				px4, py4, px1, py1)
				|| Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax,
						px4, py4, px1, py1)
				|| Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh,
						px4, py4, px1, py1, false)
				|| Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh,
						px4, py4, px1, py1, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh,
						px4, py4, px1, py1, false)
				|| Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh,
						px4, py4, px1, py1, false)) {
			return true;
		}

		// The rectangle is entirely outside or entirely inside the oriented rectangle.

		// Test if one rectangle point is inside the oriented rectangle.
		// We need to test only one point from the rectangle, since if the first
		// point is not inside, the other three points are not too.
		if (containsOrientedRectanglePoint(
				centerX, centerY, axis1X, axis1Y, axis1Extent, axis2Extent, rx, ry)) {
			return true;
		}

		// Test if one oriented rectangle point is inside the rectangle
		// We need to test only one point from the rectangle, since if the first
		// point is not inside, the other three points are not too.
		return (Rectangle2afp.containsRectanglePoint(rx, ry, rx2, ry2, px1, py1));
	}

	/**
	 * Tests if the interior of the specified {@link PathIterator2afp}
	 * intersects the interior of a specified set of oriented rectangular
	 * coordinates.
	 *
	 * @param <T> the type of the path elements to iterate on.
	 * @param centerX the specified X coordinate of the rectangle center.
	 * @param centerY the specified Y coordinate of the rectangle center.
	 * @param axis1X the X coordinate of the first axis of the rectangle.
	 * @param axis1Y the Y coordinate of the first axis of the rectangle.
	 * @param extent1 the extent the rectangle along the first axis.
	 * @param extent2 the extent the rectangle along the second axis.
	 * @param pathIterator the specified {@link PathIterator2afp}.
	 * @return <code>true</code> if the specified {@link PathIterator2afp} and
	 *         the interior of the specified set of rectangular
	 *         coordinates intersect each other; <code>false</code> otherwise.
	 */
	@Pure
	static <T extends PathElement2afp> boolean intersectsOrientedRectanglePathIterator(
			double centerX, double centerY, double axis1X, double axis1Y, double extent1, double extent2,
			PathIterator2afp<T> pathIterator) {
		assert (pathIterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (extent1 >= 0.) : "Axis 1 extent must be positive or zero"; //$NON-NLS-1$
		assert (extent2 >= 0.) : "Axis 2 extent must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		int mask = (pathIterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator<T> localIterator = 
				new ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator<>(
						centerX, centerY, axis1X, axis1Y,
						pathIterator);
		int crossings = Path2afp.computeCrossingsFromRect(
				0,
				localIterator, 
				-extent1, -extent2,
				extent1, extent2,
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}
		return getCenterX() == shape.getCenterX()
				&& getCenterY() == shape.getCenterY()
				&& getFirstAxisX() == shape.getFirstAxisX()
				&& getFirstAxisY() == shape.getFirstAxisY()
				&& getFirstAxisExtent() == shape.getFirstAxisExtent()
				&& getSecondAxisX() == shape.getSecondAxisX()
				&& getSecondAxisY() == shape.getSecondAxisY()
				&& getSecondAxisExtent() == shape.getSecondAxisExtent();
	}

	/** Replies the center.
	 *
	 * @return the center.
	 */
	@Pure
	Point2D getCenter();

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	@Pure
	double getCenterX();

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	double getCenterY();

	/** Set the center.
	 * 
	 * @param cx the center x.
	 * @param cy the center y.
	 */
	void setCenter(double cx, double cy);

	/** Set the center's x.
	 * 
	 * @param cx the center x.
	 */
	void setCenterX(double cx);

	/** Set the center's y.
	 * 
	 * @param cy the center y.
	 */
	void setCenterY(double cy);

	/** Set the center.
	 * 
	 * @param center
	 */
	default void setCenter(Point2D center) {
		setCenter(center.getX(), center.getY());
	}

	/** Replies the first axis of the oriented rectangle.
	 *
	 * @return the unit vector of the first axis. 
	 */
	@Pure
	Vector2D getFirstAxis();

	/** Replies coordinate x of the first axis of the oriented rectangle.
	 *
	 * @return the coordinate x of the unit vector of the first axis. 
	 */
	@Pure
	double getFirstAxisX();

	/** Replies coordinate y of the first axis of the oriented rectangle.
	 *
	 * @return the coordinate y of the unit vector of the first axis. 
	 */
	@Pure
	double getFirstAxisY();

	/** Replies the second axis of the oriented rectangle.
	 *
	 * @return the unit vector of the second axis. 
	 */
	@Pure
	Vector2D getSecondAxis();

	/** Replies coordinate x of the second axis of the oriented rectangle.
	 *
	 * @return the coordinate x of the unit vector of the second axis. 
	 */
	@Pure
	double getSecondAxisX();

	/** Replies coordinate y of the second axis of the oriented rectangle.
	 *
	 * @return the coordinate y of the unit vector of the second axis. 
	 */
	@Pure
	double getSecondAxisY();

	/** Replies the demi-size of the rectangle along its first axis.
	 * 
	 * @return the extent along the first axis.
	 */
	@Pure
	double getFirstAxisExtent();

	/** Change the demi-size of the rectangle along its first axis.
	 * 
	 * @param extent - the extent along the first axis.
	 */
	void setFirstAxisExtent(double extent);

	/** Replies the demi-size of the rectangle along its second axis.
	 * 
	 * @return the extent along the second axis.
	 */
	@Pure
	double getSecondAxisExtent();

	/** Change the demi-size of the rectangle along its second axis.
	 * 
	 * @param extent - the extent along the second axis.
	 */
	void setSecondAxisExtent(double extent);

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	default void setFirstAxis(Vector2D axis) {
		assert (axis != null) : "Axis must be not null"; //$NON-NLS-1$
		setFirstAxis(axis.getX(), axis.getY(), getFirstAxisExtent());
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	default void setFirstAxis(Vector2D axis, double extent) {
		assert (axis != null) : "Axis must be not null"; //$NON-NLS-1$
		setFirstAxis(axis.getX(), axis.getY(), extent);
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param x
	 * @param y
	 */
	default void setFirstAxis(double x, double y) {
		setFirstAxis(x, y, getFirstAxisExtent());
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param x
	 * @param y
	 * @param extent
	 */
	void setFirstAxis(double x, double y, double extent);

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	default void setSecondAxis(Vector2D axis) {
		assert (axis != null) : "Axis must be not null"; //$NON-NLS-1$
		setSecondAxis(axis.getX(), axis.getY(), getSecondAxisExtent());
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	default void setSecondAxis(Vector2D axis, double extent) {
		assert (axis != null) : "Axis must be not null"; //$NON-NLS-1$
		setSecondAxis(axis.getX(), axis.getY(), extent);
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param x - the new values for the first axis.
	 * @param y - the new values for the first axis.
	 */
	default void setSecondAxis(double x, double y) {
		setSecondAxis(x, y, getSecondAxisExtent());
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param x
	 * @param y
	 * @param extent
	 */
	void setSecondAxis(double x, double y, double extent);

	@Pure
	@Override
	default boolean isEmpty() {
		return(getFirstAxisExtent() == 0.
				|| getSecondAxisExtent() == 0.
				|| (getFirstAxisX() == 0. && getFirstAxisY() == 0.)
				|| (getSecondAxisX() == 0. && getSecondAxisY() == 0.));
	}

	@Override
	default public void clear() {
		set(0, 0, 1, 0, 0, 0);
	}

	@Override
	default void set(IT rectangle) {
		assert (rectangle != null) : "Oriented rectangle must be not null"; //$NON-NLS-1$
		set(rectangle.getCenterX(), rectangle.getCenterY(),
				rectangle.getFirstAxisX(), rectangle.getFirstAxisY(), rectangle.getFirstAxisExtent(),
				rectangle.getSecondAxisExtent());
	}

	/** Set the oriented rectangle.
	 * The second axis is automatically computed.
	 *
	 * @param center is the oriented rectangle center.
	 * @param axis1 is the first axis of the oriented rectangle.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	default void set(Point2D center, Vector2D axis1, double axis1Extent, double axis2Extent) {
		assert (center != null) : "Center point must be not null"; //$NON-NLS-1$
		assert (axis1 != null) : "Axis vector must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), axis1.getX(), axis1.getY(), axis1Extent, axis2Extent);
	}

	/** Set the oriented rectangle.
	 * The second axis is automatically computed.
	 *
	 * @param centerX is the X coordinate of the oriented rectangle center.
	 * @param centerY is the Y coordinate of the oriented rectangle center.
	 * @param axis1X is the X coordinate of first axis of the oriented rectangle.
	 * @param axis1Y is the Y coordinate of first axis of the oriented rectangle.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	void set(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2Extent);

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	default void setFromPointCloud(Iterable<? extends Point2D> pointCloud) {
		assert (pointCloud != null) : "Iterable of points must be not null"; //$NON-NLS-1$
		Vector2D r = new InnerComputationVector2afp();
		Parallelogram2afp.computeOrthogonalAxes(pointCloud, r, null);
		Point2D center = new InnerComputationPoint2afp();
		Vector2D extents = new InnerComputationVector2afp();
		OrientedRectangle2afp.computeCenterExtents(pointCloud, r, center, extents);
		set(center.getX(), center.getY(),
				r.getX(), r.getY(), extents.getX(),
				extents.getY());
	}

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	default void setFromPointCloud(Point2D... pointCloud) {
		assert (pointCloud != null) : "Array of points must be not null"; //$NON-NLS-1$
		setFromPointCloud(Arrays.asList(pointCloud));
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				closest,
				null);
		return closest.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				closest,
				null);
		return closest.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				closest, null);
		return closest.getDistanceLinf(p);
	}

	@Override
	default void translate(double dx, double dy) {
		setCenter(getCenterX() + dx, getCenterY() + dy);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsOrientedRectanglePoint(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				x, y);
	}

	@Pure
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return containsOrientedRectangleRectangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				r.getMinX(), r.getMinY(),
				r.getWidth(), r.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		return intersectsOrientedRectangleCircle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				s.getX(),s.getY(), s.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Ellipse must be not null"; //$NON-NLS-1$
		return intersectsOrientedRectangleEllipse(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				s.getMinX(), s.getMinY(), s.getMaxX() - s.getMinX(), s.getMaxY() - s.getMinY());
	}

	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Oriented rectangle must be not null"; //$NON-NLS-1$
		return intersectsOrientedRectangleOrientedRectangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisExtent());
	}

	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Parallelogram must be not null"; //$NON-NLS-1$
		return Parallelogram2afp.intersectsParallelogramParallelogram(
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent());
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return intersectsOrientedRectangleRectangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "round rectangle must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		return intersectsOrientedRectangleSegment(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Triangle must be not null"; //$NON-NLS-1$
		return intersectsOrientedRectangleTriangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2(), s.getX3(), s.getY3());
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new OrientedRectanglePathIterator<>(this);
		}
		return new TransformedOrientedRectanglePathIterator<>(this, transform);
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		assert (iterator != null) : "Oriented rectangle must be not null"; //$NON-NLS-1$
		return intersectsOrientedRectanglePathIterator(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(),
				getFirstAxisExtent(), getSecondAxisExtent(),
				iterator);
	}

	@Pure
	@Override
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		Point2D minCorner;
		Point2D maxCorner;

		minCorner = new InnerComputationPoint2afp(getCenterX(), getCenterY());
		maxCorner = new InnerComputationPoint2afp(getCenterX(), getCenterY());

		double srx = getFirstAxisX() * getFirstAxisExtent();
		double sry = getFirstAxisY() * getFirstAxisExtent();
		double ssx = getSecondAxisX() * getSecondAxisExtent();
		double ssy = getSecondAxisY() * getSecondAxisExtent();

		if(getFirstAxisX() >= 0.) {
			if (getFirstAxisY() >= 0.) {
				minCorner.add(-srx + ssx, -sry - ssy);
				maxCorner.sub(-srx + ssx, -sry - ssy);
			} else {
				minCorner.add(-srx - ssx, sry - ssy);
				maxCorner.sub(-srx - ssx, sry - ssy);
			}
		} else {
			if (getFirstAxisY() >= 0.){
				minCorner.add( srx + ssx, -sry + ssy);
				maxCorner.sub( srx + ssx, -sry + ssy);
			} else {
				minCorner.add( srx - ssx, sry + ssy);
				maxCorner.sub( srx - ssx, sry + ssy);
			}
		}
		box.setFromCorners(minCorner, maxCorner);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				point, null);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisExtent(),
				null, point);
		return point;
	}

	/** Roate the oriented rectangle around its center.
	 *
	 * @param angle the angle of rotation.
	 */
	default void rotate(double angle) {
		Vector2D axis1 = getFirstAxis();
		Vector2D newAxis = getGeomFactory().newVector();
		newAxis.turn(angle, axis1);
		setFirstAxis(newAxis.getX(), newAxis.getY());
	}

	/** Abstract iterator on the path elements of the oriented rectangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractOrientedRectanglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		/** Number of path elements.
		 */
		protected static final int ELEMENT_COUNT = 4;

		/** The iterated shape.
		 */
		protected final OrientedRectangle2afp<?, ?, T, ?, ?> rectangle;

		/**
		 * @param rectangle the iterated rectangle.
		 */
		public AbstractOrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?> rectangle) {
			this.rectangle = rectangle;
		}

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return false;
		}

		@Pure
		@Override
		public boolean isCurved() {
			return false;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return true;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

	}

	/** Iterator on the path elements of an oriented rectangle.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class OrientedRectanglePathIterator<T extends PathElement2afp> extends AbstractOrientedRectanglePathIterator<T> {

		private double x;

		private double y;

		private Vector2D r;

		private Vector2D s;

		private int index;

		private double moveX;

		private double moveY;

		private double lastX;

		private double lastY;

		/**
		 * @param rectangle the oriented rectangle to iterate on.
		 */
		public OrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?> rectangle) {
			super(rectangle);
			if (rectangle.isEmpty()) {
				this.index = ELEMENT_COUNT;
			} else {
				this.r = new InnerComputationVector2afp(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
				this.s = new InnerComputationVector2afp(rectangle.getSecondAxisX(), rectangle.getSecondAxisY());
				this.r.scale(rectangle.getFirstAxisExtent());
				this.s.scale(rectangle.getSecondAxisExtent());
				this.x = rectangle.getCenterX();
				this.y = rectangle.getCenterY();
				this.index = -1;
			}
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new OrientedRectanglePathIterator<>(this.rectangle);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < ELEMENT_COUNT;
		}

		@Override
		public T next() {
			int idx = this.index;
			++this.index;
			if (idx < 0) {
				this.moveX = this.x + this.r.getX() + this.s.getX();
				this.moveY = this.y + this.r.getY() + this.s.getY();
				this.lastX = this.moveX;
				this.lastY = this.moveY;
				return getGeomFactory().newMovePathElement(this.moveX, this.moveY);
			}
			double lx = this.lastX;
			double ly = this.lastY;
			switch(idx) {
			case 0:
				this.lastX = this.x - this.r.getX() + this.s.getX();
				this.lastY = this.y - this.r.getY() + this.s.getY();
				return getGeomFactory().newLinePathElement(lx, ly, this.lastX, this.lastY);
			case 1:
				this.lastX = this.x - this.r.getX() - this.s.getX();
				this.lastY = this.y - this.r.getY() - this.s.getY();
				return getGeomFactory().newLinePathElement(lx, ly, this.lastX, this.lastY);
			case 2:
				this.lastX = this.x + this.r.getX() - this.s.getX();
				this.lastY = this.y + this.r.getY() - this.s.getY();
				return getGeomFactory().newLinePathElement(lx, ly, this.lastX, this.lastY);
			case 3:
				return getGeomFactory().newClosePathElement(
						this.lastX, this.lastY,
						this.moveX, this.moveY);
			default:
				throw new NoSuchElementException();
			}
		}

	}

	/** Iterator on the path elements of a transformed oriented rectangle.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedOrientedRectanglePathIterator<T extends PathElement2afp> extends AbstractOrientedRectanglePathIterator<T> {

		private final Transform2D transform;

		private double x;

		private double y;

		private Vector2D r;

		private Vector2D s;

		private int index;

		private Point2D move;

		private Point2D last;

		/**
		 * @param rectangle the oriented rectangle to iterate on.
		 * @param transform the transformation to apply.
		 */
		public TransformedOrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?> rectangle,
				Transform2D transform) {
			super(rectangle);
			assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = ELEMENT_COUNT;
			} else {
				this.move = getGeomFactory().newPoint();
				this.last = getGeomFactory().newPoint();
				this.r = new InnerComputationVector2afp(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
				this.s = new InnerComputationVector2afp(rectangle.getSecondAxisX(), rectangle.getSecondAxisY());
				this.r.scale(rectangle.getFirstAxisExtent());
				this.s.scale(rectangle.getSecondAxisExtent());
				this.x = rectangle.getCenterX();
				this.y = rectangle.getCenterY();
				this.index = -1;
			}
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new TransformedOrientedRectanglePathIterator<>(this.rectangle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < ELEMENT_COUNT;
		}

		@Override
		public T next() {
			int idx = this.index;
			++this.index;
			if (idx < 0) {
				this.move.set(
						this.x + this.r.getX() + this.s.getX(),
						this.y + this.r.getY() + this.s.getY());
				this.transform.transform(this.move);
				this.last.set(this.move);
				return getGeomFactory().newMovePathElement(this.move.getX(), this.move.getY());
			}
			double lx = this.last.getX();
			double ly = this.last.getY();
			switch(idx) {
			case 0:
				this.last.set(
						this.x - this.r.getX() + this.s.getX(),
						this.y - this.r.getY() + this.s.getY());
				this.transform.transform(this.last);
				return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
			case 1:
				this.last.set(
						this.x - this.r.getX() - this.s.getX(),
						this.y - this.r.getY() - this.s.getY());
				this.transform.transform(this.last);
				return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
			case 2:
				this.last.set(
						this.x + this.r.getX() - this.s.getX(),
						this.y + this.r.getY() - this.s.getY());
				this.transform.transform(this.last);
				return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
			case 3:
				return getGeomFactory().newClosePathElement(
						this.last.getX(), this.last.getY(),
						this.move.getX(), this.move.getY());
			default:
				throw new NoSuchElementException();
			}
		}

	}

	/** An iterator that automatically transform and reply the path elements from the given iterator such that
	 * the coordinates of the path elements are projected in the local coordinate system of the given oriented
	 * box.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @author $Author: mgrolleau$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		private final PathIterator2afp<T> iterator;

		private final double centerX;

		private final double centerY;

		private final double axisX1;

		private final double axisY1;

		/**
		 * @param x the specified X coordinate of the rectangle center.
		 * @param y the specified Y coordinate of the rectangle center.
		 * @param axis1X the X coordinate of the first axis of the rectangle.
		 * @param axis1Y the Y coordinate of the first axis of the rectangle.
		 * @param iterator the iterator to transform.
		 */
		public ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator(
				double x, double y, double axis1X, double axis1Y,
				PathIterator2afp<T> iterator) {
			this.iterator = iterator;
			this.centerX = x;
			this.centerY = y;
			this.axisX1 = axis1X;
			this.axisY1 = axis1Y;
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator<>(
					this.centerX, this.centerY,
					this.axisX1, this.axisY1,
					this.iterator.restartIterations());
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public T next() {
			PathElement2afp elem = this.iterator.next();
			switch(elem.getType()){
			case CURVE_TO:
				return getGeomFactory().newCurvePathElement(
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1, 
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1,
								elem.getCtrlX2() - this.centerX, elem.getCtrlY2() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getCtrlX2() - this.centerX, elem.getCtrlY2() - this.centerY),
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case LINE_TO:
				return getGeomFactory().newLinePathElement(
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case MOVE_TO:
				return getGeomFactory().newMovePathElement(
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case QUAD_TO:
				return getGeomFactory().newCurvePathElement(
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1, 
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case CLOSE:
				return getGeomFactory().newClosePathElement(
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1, 
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnOrientedRectangleRAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnOrientedRectangleSAxis(this.axisX1, this.axisY1,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			default:
				break;

			}
			return null;
		}

		@Override
		public void remove() {
			this.iterator.remove();			
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return this.iterator.getWindingRule();
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return this.iterator.isPolyline();
		}

		@Pure
		@Override
		public boolean isCurved() {
			return this.iterator.isCurved();
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return this.iterator.isPolygon();
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return this.iterator.isMultiParts();
		}

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.iterator.getGeomFactory();
		}

	}

}

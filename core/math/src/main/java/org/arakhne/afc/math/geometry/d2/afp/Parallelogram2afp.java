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
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp.CrossingComputationType;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D parallelogram on a plane.
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
public interface Parallelogram2afp<
		ST extends Shape2afp<?, ?, IE, P, B>,
		IT extends Parallelogram2afp<?, ?, IE, P, B>,
		IE extends PathElement2afp,
		P extends Point2D,
		B extends Rectangle2afp<?, ?, IE, P, B>>
		extends Shape2afp<ST, IT, IE, P, B> {

	/**
	 * Compute the axes of an oriented bounding rectangle that is enclosing the set of points.
	 * 
	 * @param points is the list of the points enclosed by the OBR.
	 * @param R is the vector where the R axis of the OBR is put. If <code>null</code>, S must be not <code>null</code>.
	 * @param S is the vector where the S axis of the OBR is put. If <code>null</code>, R must be not <code>null</code>. 
	 * @see "MGPCG pages 219-221"
	 */
	static void computeOrthogonalAxes(Iterable<? extends Point2D> points, Vector2D R, Vector2D S) {
		assert (points != null) : "Collection of points must be not null"; //$NON-NLS-1$
		assert (R != null || S != null) : "One axis vector must be not null"; //$NON-NLS-1$
		// Determining the covariance matrix of the points
		// and set the center of the box
		Matrix2f cov = new Matrix2f();
		cov.cov(R, points);
		//Determining eigenvectors of covariance matrix and defines RS axis
		Matrix2f rs = new Matrix2f();//eigenvectors                         
		cov.eigenVectorsOfSymmetricMatrix(rs);
		if (R != null) {
			rs.getColumn(0, R);
		}
		if (S != null) {
			rs.getColumn(1, S);
		}
	}

	/** Project the given vector on the R axis, according to the direction of the S axis.
	 *
	 * <p>This function assumes nothing on the axes' orientations. For an efficient implementation for
	 * orthogonal axes, see
	 * {@link OrientedRectangle2afp#projectVectorOnOrientedRectangleRAxis(double, double, double, double)}.
	 *
	 * @param rx the x coordinate of the R axis.
	 * @param ry the y coordinate of the R axis.
	 * @param sx the x coordinate of the S axis.
	 * @param sy the y coordinate of the S axis.
	 * @param x the x coordinate of the vector.
	 * @param y the y coordinate of the vector.
	 * @return the coordinate of the projection of the vector on R
	 * @see OrientedRectangle2afp#projectVectorOnOrientedRectangleRAxis(double, double, double, double)
	 */
	@Pure
	static double projectVectorOnParallelogramRAxis(double rx, double ry, double sx, double sy, double x,  double y) {
		assert (Vector2D.isUnitVector(rx, ry)) : "Vector R is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(sx, sy)) : "Vector S is not a unit vector"; //$NON-NLS-1$
		double det = Vector2D.perpProduct(rx, ry, sx, sy);
		if (det == 0.) {
			return Double.NaN;
		}
		return Vector2D.perpProduct(sx, sy, -x, -y) / det;
	}

	/** Project the given vector on the S axis, according to the direction of the R axis.
	 *
	 * <p>This function assumes nothing on the axes' orientations. For an efficient implementation for
	 * orthogonal axes, see
	 * {@link OrientedRectangle2afp#projectVectorOnOrientedRectangleSAxis(double, double, double, double)}.
	 *
	 * @param rx the x coordinate of the R axis.
	 * @param ry the y coordinate of the R axis.
	 * @param sx the x coordinate of the S axis.
	 * @param sy the y coordinate of the S axis.
	 * @param x the x coordinate of the vector.
	 * @param y the y coordinate of the vector.
	 * @return the coordinate of the projection of the vector on S.
	 * @see OrientedRectangle2afp#projectVectorOnOrientedRectangleSAxis(double, double, double, double)
	 */
	@Pure
	static double projectVectorOnParallelogramSAxis(double rx, double ry, double sx, double sy, double x,  double y) {
		assert (Vector2D.isUnitVector(rx, ry)) : "Vector R is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(sx, sy)) : "Vector S is not a unit vector"; //$NON-NLS-1$
		double det = Vector2D.perpProduct(sx, sy, rx, ry);
		if (det == 0.) {
			return Double.NaN;
		}
		return Vector2D.perpProduct(rx, ry, -x, -y) / det;
	}

	/**
	 * Compute the center and extents of a parallelogram from a set of points and the parallelogram axes.
	 *
	 * <p>This function assumes no constraint on the axes' orientations, in opposite to
	 * {@link OrientedRectangle2afp#computeCenterExtents(Iterable, Vector2D, Point2D, Tuple2D)}, which
	 * assumes orthogonal axes.
	 * 
	 * @param points is the list of the points enclosed by the parallogram
	 * @param R is the R axis of the parallogram
	 * @param S is the S axis of the parallogram
	 * @param center is the point which is set with the parallogram's center coordinates.
	 * @param extents are the extents of the parallogram for the R and S axis.
	 * @see "MGPCG pages 222-223 (oriented bounding box)"
	 * @see OrientedRectangle2afp#computeCenterExtents(Iterable, Vector2D, Point2D, Tuple2D)
	 */
	static void computeCenterExtents(
			Iterable<? extends Point2D> points,
			Vector2D R, Vector2D S,
			Point2D center, Tuple2D<?> extents) {
		assert (points != null) : "Collection of points must be not null"; //$NON-NLS-1$
		assert (R != null) : "First axis vector must be not null"; //$NON-NLS-1$
		assert (R.isUnitVector()) : "First axis vector must be unit vector"; //$NON-NLS-1$
		assert (S != null) : "Second axis vector must be not null"; //$NON-NLS-1$
		assert (S.isUnitVector()) : "Second axis vector must be unit vector"; //$NON-NLS-1$
		assert (center != null) : "Center point must be not null"; //$NON-NLS-1$
		assert (extents != null) : "Extent tuple must be not null"; //$NON-NLS-1$

		double minR = Double.POSITIVE_INFINITY;
		double maxR = Double.NEGATIVE_INFINITY;
		double minS = Double.POSITIVE_INFINITY;
		double maxS = Double.NEGATIVE_INFINITY;

		double ux = R.getX();
		double uy = R.getY();
		double vx = S.getX();
		double vy = S.getY();
		
		double projR;
		double projS;
		for(Point2D tuple : points) {
			projR = projectVectorOnParallelogramRAxis(ux, uy, vx, vy, tuple.getX(), tuple.getY());
			projS = projectVectorOnParallelogramSAxis(ux, uy, vx, vy, tuple.getX(), tuple.getY());
			if (projR < minR) minR = projR;			
			if (projR > maxR) maxR = projR;			
			if (projS < minS) minS = projS;			
			if (projS > maxS) maxS = projS;			
		}

		double a = (maxR + minR) / 2.;
		double b = (maxS + minS) / 2.;

		// Set the center of the OBR
		center.set(
				a * ux + b * vx,
				a * uy + b * vy);

		// Compute extents
		extents.set(
				(maxR - minR) / 2.,
				(maxS - minS) / 2.);
	}

	/**
	 * Given a point p, this function computes the point q1 on (or in) this parallelogram,
	 * closest to p; and the point q2 on the parallelogram, farthest to p. If there are several
	 * points, the function will return one of those. Remember this function may
	 * return an approximate result when points remain on parallelogram plane of symmetry.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
	 * @param centerX
	 *            is the X coordinate of the parallelogram center.
	 * @param centerY
	 *            is the Y coordinate of the parallelogram center.
	 * @param axis1X
	 *            is the X coordinate of the axis 1 vector.
	 * @param axis1Y
	 *            is the Y coordinate of the axis 1 vector.
	 * @param axis1Extent
	 *            is the extent of the axis 1 of the parallelogram.
	 * @param axis2X
	 *            is the X coordinate of the axis 2 vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 vector.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the parallelogram.
	 * @param closest the closest point. If <code>null</code>, the closest point is not computed.
	 * @param farthest the farthest point. If <code>null</code>, the farthest point is not computed.
	 */
	@Unefficient
	static void computeClosestFarthestPoints(
			double px, double py,
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			Point2D closest, Point2D farthest) {
		assert (axis1Extent >= 0.) : "Extent of the first axis must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0.) : "Extent of the second axis must be positive or zero"; //$NON-NLS-1$
		assert (closest != null || farthest != null) : "Neither closest point nor farthest point has a result vector"; //$NON-NLS-1$
		
		if (axis1Extent == 0. && axis2Extent == 0.) {
			if (closest != null) {
				closest.set(centerX, centerY);
			}
			if (farthest != null) {
				farthest.set(centerX, centerY);
			}
			return;
		}
		
		double dx = px - centerX;
		double dy = py - centerY;

		double uex = axis1Extent * axis1X;
		double uey = axis1Extent * axis1Y;
		double vex = axis2Extent * axis2X;
		double vey = axis2Extent * axis2Y;

		double a = uex + vex;
		double b = uey + vey;
		double c = vex - uex;
		double d = vey - uey;

		// Names of the points in the ggb diagram
		// E: (-a, -b)
		// F: (-c, -d)
		// G: (a, b)
		// H: (c, d)
		
		if (closest != null) {
			double sideDiag1 = Vector2D.perpProduct(a, b, dx, dy);
			double sideDiag2 = Vector2D.perpProduct(c, d, dx, dy);
			if (sideDiag1 >= 0.) {
				if (sideDiag2 >= 0.) {
					// (H-E) in the ggb diagram
					double baseX = centerX + c;
					double baseY = centerY + d;
					double baseX2 = centerX - a;
					double baseY2 = centerY - b;
					if (Vector2D.perpProduct(baseX2 - baseX, baseY2 - baseY, px - baseX, py - baseY) <= 0.) {
						Segment2afp.computeClosestPointTo(
								baseX, baseY, baseX2, baseY2,
								px, py,
								closest);
					} else {
						closest.set(px, py);
					}
				} else {
					// (G-H) in the ggb diagram
					double baseX = centerX + a;
					double baseY = centerY + b;
					double baseX2 = centerX + c;
					double baseY2 = centerY + d;
					if (Vector2D.perpProduct(baseX2 - baseX, baseY2 - baseY, px - baseX, py - baseY) <= 0.) {
						Segment2afp.computeClosestPointTo(
								baseX, baseY, baseX2, baseY2,
								px, py,
								closest);
					} else {
						closest.set(px, py);
					}
				}
			} else if (sideDiag2 >= 0.) {
				// (E-F) in the ggb diagram
				double baseX = centerX - a;
				double baseY = centerY - b;
				double baseX2 = centerX - c;
				double baseY2 = centerY - d;
				if (Vector2D.perpProduct(baseX2 - baseX, baseY2 - baseY, px - baseX, py - baseY) <= 0.) {
					Segment2afp.computeClosestPointTo(
							baseX, baseY, baseX2, baseY2,
							px, py,
							closest);
				} else {
					closest.set(px, py);
				}
			} else {
				// (F-G) in the ggb diagram
				double baseX = centerX - c;
				double baseY = centerY - d;
				double baseX2 = centerX + a;
				double baseY2 = centerY + b;
				if (Vector2D.perpProduct(baseX2 - baseX, baseY2 - baseY, px - baseX, py - baseY) <= 0.) {
					Segment2afp.computeClosestPointTo(
							baseX, baseY, baseX2, baseY2,
							px, py,
							closest);
				} else {
					closest.set(px, py);
				}
			}
		}

		if (farthest != null) {
			double pEdist = Math.pow(dx + a, 2) + Math.pow(dy + b, 2);
			double pFdist = Math.pow(dx + c, 2) + Math.pow(dy + d, 2);
			double pGdist = Math.pow(dx - a, 2) + Math.pow(dy - b, 2);
			double pHdist = Math.pow(dx - c, 2) + Math.pow(dy - d, 2);
			
			double max = pEdist;
			double maxx = -a;
			double maxy = -b;
			
			if (pFdist > max) {
				max = pFdist;
				maxx = -c;
				maxy = -d;
			}
			
			if (pGdist > max) {
				max = pGdist;
				maxx = a;
				maxy = b;
			}

			if (pHdist > max) {
				maxx = c;
				maxy = d;
			}
			
			farthest.set(centerX + maxx, centerY + maxy);
		}
	}

	/** Replies if a point is inside in the parallelogram.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the parallelogram.
	 * @return <code>true</code> if the given point is inside the parallelogram;
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsParallelogramPoint(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double px, double py) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$
		
		double x = px - centerX;
		double y = py - centerY;
		
		double coordinate = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}

		coordinate = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		return (coordinate >= -axis2Extent && coordinate <= axis2Extent);
	}

	/** Replies if a rectangle is inside the parallelogram.
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
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
	 * @return <code>true</code> if the given rectangle is inside the parallelogram;
	 * otherwise <code>false</code>.
	 */
	@Pure
	@Unefficient
	static boolean containsParallelogramRectangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double rx, double ry,
			double rwidth, double rheight) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$
		assert (rwidth >= 0) : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0) : "Height of the rectangle must be positive or zero"; //$NON-NLS-1$

		double x, y, coordinate;

		double basex = rx - centerX;
		double basey = ry - centerY;
		
		x = basex;
		y = basey;
		coordinate = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis2Extent || coordinate > axis2Extent) {
			return false;
		}
		
		x = basex + rwidth;
		coordinate = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis2Extent || coordinate > axis2Extent) {
			return false;
		}

		y = basey + rheight;
		coordinate = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis2Extent || coordinate > axis2Extent) {
			return false;
		}

		x = basex;
		coordinate = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		if (coordinate < -axis1Extent || coordinate > axis1Extent) {
			return false;
		}
		coordinate = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, x, y);
		return (coordinate >= -axis2Extent && coordinate <= axis2Extent);
	}

	/** Replies if the specified parallelogram intersects the specified segment.
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param s1x is the X coordinate of the first point of the segment.
	 * @param s1y is the Y coordinate of the first point of the segment.
	 * @param s2x is the X coordinate of the second point of the segment.
	 * @param s2y is the Y coordinate of the second point of the segment.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	@Pure
	static boolean intersectsParallelogramSegment(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double s1x, double s1y, double s2x, double s2y) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$

		// Changing Segment coordinate basis.
		double p1x = s1x - centerX;
		double p1y = s1y - centerY;
		double p2x = s2x - centerX;
		double p2y = s2y - centerY;
		
		double ax = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, p1x, p1y);
		double ay = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, p1x, p1y);
		double bx = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, p2x, p2y);
		double by = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, p2x, p2y);

		return Rectangle2afp.intersectsRectangleSegment(
				-axis1Extent, -axis2Extent, axis1Extent, axis2Extent,
				ax, ay,  bx,  by);
	}

	/** Replies if the specified parallelogram intersects the specified triangle.
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param tx1 is the X coordinate of the first point of the triangle.
	 * @param ty1 is the Y coordinate of the first point of the triangle.
	 * @param tx2 is the X coordinate of the second point of the triangle.
	 * @param ty2 is the Y coordinate of the second point of the triangle.
	 * @param tx3 is the X coordinate of the third point of the triangle.
	 * @param ty3 is the Y coordinate of the third point of the triangle.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	@Pure
	static boolean intersectsParallelogramTriangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double tx1, double ty1, double tx2, double ty2, double tx3, double ty3) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$

		// Changing Triangle coordinate basis.
		double p1x = tx1 - centerX;
		double p1y = ty1 - centerY;
		double p2x = tx2 - centerX;
		double p2y = ty2 - centerY;
		double p3x = tx3 - centerX;
		double p3y = ty3 - centerY;
		
		double ax = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, p1x, p1y);
		double ay = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, p1x, p1y);
		double bx = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, p2x, p2y);
		double by = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, p2x, p2y);
		double cx = projectVectorOnParallelogramRAxis(axis1X, axis1Y, axis2X, axis2Y, p3x, p3y);
		double cy = projectVectorOnParallelogramSAxis(axis1X, axis1Y, axis2X, axis2Y, p3x, p3y);

		return Triangle2afp.intersectsTriangleRectangle(
				ax, ay,  bx,  by, cx, cy,
				-axis1Extent, -axis2Extent, 2. * axis1Extent, 2. * axis2Extent);
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param axis2Extent
	 *            is the extent of the axis 2 of the parallelogram.
	 * @param circleX is the coordinate of the circle center.
	 * @param circleY is the coordinate of the circle center.
	 * @param circleRadius is the radius of the circle.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	@Pure
	static boolean intersectsParallelogramCircle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double circleX, double circleY, double circleRadius) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$
		assert (circleRadius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				circleX, circleY,
				centerX, centerY,
				axis1X, axis1Y, axis1Extent,
				axis2X, axis2Y, axis2Extent,
				closest, null);
		// Circle and parallelogram intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		double squaredRadius = circleRadius * circleRadius;

		return Point2D.getDistanceSquaredPointPoint(
						circleX, circleY,
						closest.getX(), closest.getY()) <= squaredRadius;
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
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
	static boolean intersectsParallelogramEllipse(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double ex, double ey, double ewidth, double eheight){
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$
		assert (ewidth >= 0) : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (eheight >= 0) : "Height of the rectangle must be positive or zero"; //$NON-NLS-1$

		if (ewidth <= 0 || eheight <= 0) {
			return false;
		}
		
		// Convert the parallelogram elements so that the ellipse is transformed to a circle centered at the origin.
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
		
		double transAxis2X = axis2Extent * axis2X / a;
		double transAxis2Y = axis2Extent * axis2Y / b;
		double length2 = Math.hypot(transAxis2X, transAxis2Y);
		transAxis2X /= length2;
		transAxis2Y /= length2;

		// Intersection test between the parallelogram and the circle
		return intersectsParallelogramCircle(
				transCenterX, transCenterY,
				transAxis1X, transAxis1Y, length1,
				transAxis2X, transAxis2Y, length2,
				0, 0, 1);
	}

	/**  Replies if the parallelogram intersects the given rectangle.
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
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
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>.
	 */
	@Pure
	@Unefficient
	static boolean intersectsParallelogramRectangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double rx, double ry,
			double rwidth, double rheight) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$
		assert (rwidth >= 0) : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0) : "Height of the rectangle must be positive or zero"; //$NON-NLS-1$
		double rx2 = rx + rwidth;
		double ry2 = ry + rheight;
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
		if (containsParallelogramPoint(
				centerX, centerY, axis1X, axis1Y, axis1Extent, axis2X, axis2Y, axis2Extent, rx, ry)) {
			return true;
		}
		
		// Test if one parallelogram point is inside the rectangle
		// We need to test only one point from the rectangle, since if the first
		// point is not inside, the other three points are not too.
		return (Rectangle2afp.containsRectanglePoint(rx, ry, rx2, ry2, px1, py1));
	}

	/**  Replies if two parallelograms intersect.
	 * 
	 * @param centerX1
	 *            is the X coordinate of the first parallelogram center.
	 * @param centerY1
	 *            is the Y coordinate of the first parallelogram center.
	 * @param axis1X1
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y1
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent1
	 *            is the extent of the axis 1 of the first parallelogram.
	 * @param axis2X1
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y1
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param axis2Extent1
	 *            is the extent of the axis 2 of the first parallelogram.
	 * @param centerX2
	 *            is the X coordinate of the second parallelogram center.
	 * @param centerY2
	 *            is the Y coordinate of the second parallelogram center.
	 * @param axis1X2
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param axis1Y2
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param axis1Extent2
	 *            is the extent of the axis 1 of the second parallelogram.
	 * @param axis2X2
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y2
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param axis2Extent2
	 *            is the extent of the axis 2 of the second parallelogram.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>.
	 */
	@Pure
	@Unefficient
	static boolean intersectsParallelogramParallelogram(
			double centerX1, double centerY1,
			double axis1X1, double axis1Y1,
			double axis1Extent1,
			double axis2X1, double axis2Y1,
			double axis2Extent1,
			double centerX2, double centerY2,
			double axis1X2, double axis1Y2,
			double axis1Extent2,
			double axis2X2, double axis2Y2,
			double axis2Extent2) {
		assert (axis1Extent1 >= 0) : "Extent of the first paralelogram axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent1 >= 0) : "Extent of the first paralelogram axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X1, axis1Y1)) : "First paralelogram axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X1, axis2Y1)) : "First paralelogram axis 2 is not a unit vector"; //$NON-NLS-1$
		assert (axis1Extent2 >= 0) : "Extent of the second paralelogram axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent2 >= 0) : "Extent of the second paralelogram axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X2, axis1Y2)) : "Second paralelogram axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X2, axis2Y2)) : "Second paralelogram axis 2 is not a unit vector"; //$NON-NLS-1$
		
		// Project the second parallelogram into the local axes of the first parallelogram
		double x = centerX2 - centerX1;
		double y = centerY2 - centerY1;
		double projCenterX = projectVectorOnParallelogramRAxis(axis1X1, axis1Y1, axis2X1, axis2Y1, x, y);
		double projCenterY = projectVectorOnParallelogramSAxis(axis1X1, axis1Y1, axis2X1, axis2Y1, x, y);
		double projAxis1X = projectVectorOnParallelogramRAxis(axis1X1, axis1Y1, axis2X1, axis2Y1, axis1X2, axis1Y2);
		double projAxis1Y = projectVectorOnParallelogramSAxis(axis1X1, axis1Y1, axis2X1, axis2Y1, axis1X2, axis1Y2);
		double length = Math.hypot(projAxis1X, projAxis1Y);
		projAxis1X /= length;
		projAxis1Y /= length;
		double projAxis2X = projectVectorOnParallelogramRAxis(axis1X1, axis1Y1, axis2X1, axis2Y1, axis2X2, axis2Y2);
		double projAxis2Y = projectVectorOnParallelogramSAxis(axis1X1, axis1Y1, axis2X1, axis2Y1, axis2X2, axis2Y2);
		length = Math.hypot(projAxis2X, projAxis2Y);
		projAxis2X /= length;
		projAxis2Y /= length;
		
		return intersectsParallelogramRectangle(
				projCenterX, projCenterY,
				projAxis1X, projAxis1Y, axis1Extent2,
				projAxis2X, projAxis2Y, axis2Extent2,
				-axis1Extent1, -axis2Extent1,
				2. * axis1Extent1, 2. * axis2Extent1);
	}

	/**  Replies if the parallelogram intersects the given rectangle.
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
	 * @param axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
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
	static boolean intersectsParallelogramRoundRectangle(
			double centerX, double centerY,
			double axis1X, double axis1Y,
			double axis1Extent,
			double axis2X, double axis2Y,
			double axis2Extent,
			double rx, double ry,
			double rwidth, double rheight,
			double rArcWidth, double rArcHeight) {
		assert (axis1Extent >= 0) : "Extent of axis 1 must be positive or zero"; //$NON-NLS-1$
		assert (axis2Extent >= 0) : "Extent of axis 2 must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis 1 is not a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis 2 is not a unit vector"; //$NON-NLS-1$
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
		
		// The rectangle is entirely outside or entirely inside the parallelogram.
		
		// Test if one rectangle point is inside the parallelogram.
		// We need to test only one point from the rectangle, since if the first
		// point is not inside, the other three points are not too.
		if (containsParallelogramPoint(
				centerX, centerY, axis1X, axis1Y, axis1Extent, axis2X, axis2Y, axis2Extent, rx, ry)) {
			return true;
		}
		
		// Test if one parallelogram point is inside the rectangle
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
	 * @param axis1X the X coordinate of the first axis of the parallelogram.
	 * @param axis1Y the Y coordinate of the first axis of the parallelogram.
	 * @param extent1 the extent the rectangle along the first axis.
	 * @param axis2X the X coordinate of the second axis of the parallelogram.
	 * @param axis2Y the Y coordinate of the second axis of the parallelogram.
	 * @param extent2 the extent the rectangle along the second axis.
	 * @param pathIterator the specified {@link PathIterator2afp}.
	 * @return <code>true</code> if the specified {@link PathIterator2afp} and
	 *         the interior of the specified set of rectangular
	 *         coordinates intersect each other; <code>false</code> otherwise.
	 */
	@Pure
	static <T extends PathElement2afp> boolean intersectsParallelogramPathIterator(
			double centerX, double centerY, double axis1X, double axis1Y, double extent1,
			double axis2X, double axis2Y, double extent2,
			PathIterator2afp<T> pathIterator) {
		assert (pathIterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		assert (extent1 >= 0.) : "Axis 1 extent must be positive or zero"; //$NON-NLS-1$
		assert (extent2 >= 0.) : "Axis 2 extent must be positive or zero"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(axis2X, axis2Y)) : "Axis must be a unit vector"; //$NON-NLS-1$
		int mask = (pathIterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		ProjectionToParallelogramLocalCoordinateSystemPathIterator<T> localIterator = 
				new ProjectionToParallelogramLocalCoordinateSystemPathIterator<>(
						centerX, centerY, axis1X, axis1Y, axis2X, axis2Y,
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
		assert (center != null) : "Center point must be not null"; //$NON-NLS-1$
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
		return getFirstAxisExtent() <= 0.
				|| getSecondAxisExtent() <= 0.;
	}

	@Override
	default public void clear() {
		set(0, 0, 1, 0, 0, 0, 1, 0);
	}

	@Override
	default void set(IT parallelogram) {
		assert (parallelogram != null) : "Parallelogram must be not null"; //$NON-NLS-1$
		set(parallelogram.getCenterX(), parallelogram.getCenterY(),
				parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(), parallelogram.getFirstAxisExtent(),
				parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(), parallelogram.getSecondAxisExtent());
	}

	/** Set the parallelogram.
	 *
	 * @param center is the parallelogram center.
	 * @param axis1 is the first axis of the parallelogram.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2 is the second axis of the parallelogram.
	 * @param axis2Extent is the extent of the second axis.
	 */
	default void set(Point2D center, Vector2D axis1, double axis1Extent, Vector2D axis2, double axis2Extent) {
		assert (center != null) : "Center point must be not null"; //$NON-NLS-1$
		assert (axis1 != null) : "First axis point must be not null"; //$NON-NLS-1$
		assert (axis2 != null) : "Second axis point must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), axis1.getX(), axis1.getY(), axis1Extent,
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
	void set(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2X, double axis2Y, double axis2Extent);

	/** Set the parallelogram from a could of points.
	 * <p>
	 * This function changes the axes to be orthogonal.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	default void setFromPointCloud(Iterable<? extends Point2D> pointCloud) {
		assert (pointCloud != null) : "The iterable on points must be not null"; //$NON-NLS-1$
		Vector2D r = new InnerComputationVector2afp();
		Vector2D s = new InnerComputationVector2afp();
		computeOrthogonalAxes(pointCloud, r, s);
		Point2D center = new InnerComputationPoint2afp();
		Vector2D extents = new InnerComputationVector2afp();
		Parallelogram2afp.computeCenterExtents(pointCloud, r, s, center, extents);
		set(center.getX(), center.getY(),
				r.getX(), r.getY(), extents.getX(),
				s.getX(), s.getY(), extents.getY());
	}

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	default void setFromPointCloud(Point2D... pointCloud) {
		assert (pointCloud != null) : "The array of points must be not null"; //$NON-NLS-1$
		setFromPointCloud(Arrays.asList(pointCloud));
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		// Only for internal usage.
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				closest,
				null);
		return closest.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		// Only for internal usage.
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				closest,
				null);
		return closest.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		// Only for internal usage.
		Point2D closest = new InnerComputationPoint2afp();
		computeClosestFarthestPoints(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				closest, null);
		return closest.getDistanceLinf(p);
	}

	@Override
	default void translate(double dx, double dy) {
		setCenter(getCenterX() + dx, getCenterY() + dy);
	}
	
	/** Roate the parallelogram around its center.
	 *
	 * @param angle the angle of rotation.
	 */
	default void rotate(double angle) {
		Vector2D axis1 = getFirstAxis();
		Vector2D newAxis1 = getGeomFactory().newVector();
		newAxis1.turn(angle, axis1);
		setFirstAxis(newAxis1);
		Vector2D axis2 = getSecondAxis();
		Vector2D newAxis2 = getGeomFactory().newVector();
		newAxis2.turn(angle, axis2);
		setSecondAxis(newAxis2);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsParallelogramPoint(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				x, y);
	}
	
	@Pure
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return containsParallelogramRectangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				r.getMinX(), r.getMinY(),
				r.getWidth(), r.getHeight());
	}
	
	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		return intersectsParallelogramCircle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getX(),s.getY(), s.getRadius());
	}
	
	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Ellipse must be not null"; //$NON-NLS-1$
		return intersectsParallelogramEllipse(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getMinX(), s.getMinY(), s.getMaxX() - s.getMinX(), s.getMaxY() - s.getMinY());
	}
	
	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Oriented rectangle must be not null"; //$NON-NLS-1$
		return intersectsParallelogramParallelogram(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent());
	}
	
	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Parallelogram must be not null"; //$NON-NLS-1$
		return intersectsParallelogramParallelogram(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent());
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return intersectsParallelogramRectangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight());
	}
	
	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Round rectangle must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}
	
	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		return intersectsParallelogramSegment(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?> s) {
		assert (s != null) : "Triangle must be not null"; //$NON-NLS-1$
		return intersectsParallelogramTriangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2(), s.getX3(), s.getY3());
	}

	@Pure
	@Override
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new ParallelogramPathIterator<>(this);
		}
		return new TransformedParallelogramPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		assert (iterator != null) : "Path iterator must be not null"; //$NON-NLS-1$
		return intersectsParallelogramPathIterator(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				iterator);
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
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
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
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
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				null, point);
		return point;
	}

	/** Abstract iterator on the path elements of the parallelogram.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractParallelogramPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		/** Number of elements in the path (excluding move).
		 */
		protected static final int ELEMENT_NUMBER = 4;
		
		/** The iterated shape.
		 */
		protected final Parallelogram2afp<?, ?, T, ?, ?> parallelogram;

		/**
		 * @param parallelogram the shape.
		 */
		public AbstractParallelogramPathIterator(Parallelogram2afp<?, ?, T, ?, ?> parallelogram) {
			this.parallelogram = parallelogram;
		}

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.parallelogram.getGeomFactory();
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

	/** Iterator on the path elements of a parallelogram.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class ParallelogramPathIterator<T extends PathElement2afp> extends AbstractParallelogramPathIterator<T> {

		private double x;

		private double y;
		
		private double lastX;

		private double lastY;

		private double moveX;

		private double moveY;

		private Vector2D r;
		
		private Vector2D s;
		
		private int index;

		/**
		 * @param parallelogram the parallelogram to iterate on.
		 */
		public ParallelogramPathIterator(Parallelogram2afp<?, ?, T, ?, ?> parallelogram) {
			super(parallelogram);
			if (parallelogram.isEmpty()) {
				this.index = ELEMENT_NUMBER;
			} else {
				this.r = new InnerComputationVector2afp(parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY());
				this.s = new InnerComputationVector2afp(parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY());
				this.r.scale(parallelogram.getFirstAxisExtent());
				this.s.scale(parallelogram.getSecondAxisExtent());
				this.x = parallelogram.getCenterX();
				this.y = parallelogram.getCenterY();
				this.index = -1;
			}
		}
		
		@Override
		public PathIterator2afp<T> restartIterations() {
			return new ParallelogramPathIterator<>(this.parallelogram);
		}
		
		@Pure
		@Override
		public boolean hasNext() {
			return this.index < ELEMENT_NUMBER;
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
				return getGeomFactory().newMovePathElement(
						this.moveX, this.moveY);
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
				return getGeomFactory().newClosePathElement(lx, ly, this.moveX, this.moveY);
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
	class TransformedParallelogramPathIterator<T extends PathElement2afp> extends AbstractParallelogramPathIterator<T> {

		private final Transform2D transform;
		
		private double x;

		private double y;
		
		private Point2D last;

		private Point2D move;

		private Vector2D r;
		
		private Vector2D s;
		
		private int index;

		/**
		 * @param parallelogram the parallelogram to iterate on.
		 * @param transform the transformaion to apply.
		 */
		public TransformedParallelogramPathIterator(Parallelogram2afp<?, ?, T, ?, ?> parallelogram,
				Transform2D transform) {
			super(parallelogram);
			assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			if (parallelogram.isEmpty()) {
				this.index = ELEMENT_NUMBER;
			} else {
				this.move = getGeomFactory().newPoint();
				this.last = getGeomFactory().newPoint();
				this.r = new InnerComputationVector2afp(parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY());
				this.s = new InnerComputationVector2afp(parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY());
				this.r.scale(parallelogram.getFirstAxisExtent());
				this.s.scale(parallelogram.getSecondAxisExtent());
				this.x = parallelogram.getCenterX();
				this.y = parallelogram.getCenterY();
				this.index = -1;
			}
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new TransformedParallelogramPathIterator<>(this.parallelogram, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < ELEMENT_NUMBER;
		}

		@Override
		public T next() {
			int idx = this.index;
			++this.index;
			if (idx < 0) {
				this.move.set(this.x + this.r.getX() + this.s.getX(),
						this.y + this.r.getY() + this.s.getY());
				this.transform.transform(this.move);
				this.last.set(this.move);
				return getGeomFactory().newMovePathElement(
						this.move.getX(), this.move.getY());
			}
			double lx = this.last.getX();
			double ly = this.last.getY();
			switch(idx) {
			case 0:
				this.last.set(this.x - this.r.getX() + this.s.getX(),
						this.y - this.r.getY() + this.s.getY());
				this.transform.transform(this.last);
				return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
			case 1:
				this.last.set(this.x - this.r.getX() - this.s.getX(),
						this.y - this.r.getY() - this.s.getY());
				this.transform.transform(this.last);
				return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
			case 2:
				this.last.set(this.x + this.r.getX() - this.s.getX(),
						this.y + this.r.getY() - this.s.getY());
				this.transform.transform(this.last);
				return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
			case 3:
				return getGeomFactory().newClosePathElement(lx, ly, this.move.getX(), this.move.getY());
			default:
				throw new NoSuchElementException();
			}
		}

	}

	/** An iterator that automatically transform and reply the path elements from the given iterator such that
	 * the coordinates of the path elements are projected in the local coordinate system of the given parallelogram.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @author $Author: mgrolleau$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class ProjectionToParallelogramLocalCoordinateSystemPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		private final PathIterator2afp<T> iterator;

		private final double centerX;

		private final double centerY;

		private final double axisX1;

		private final double axisY1;

		private final double axisX2;

		private final double axisY2;

		/**
		 * @param x the specified X coordinate of the rectangle center.
		 * @param y the specified Y coordinate of the rectangle center.
		 * @param axis1X the X coordinate of the first axis of the parallelogram.
		 * @param axis1Y the Y coordinate of the first axis of the parallelogram.
		 * @param axis2X the X coordinate of the second axis of the parallelogram.
		 * @param axis2Y the Y coordinate of the second axis of the parallelogram.
		 * @param iterator the iterator to transform.
		 */
		public ProjectionToParallelogramLocalCoordinateSystemPathIterator(
				double x, double y, double axis1X, double axis1Y,
				double axis2X, double axis2Y,
				PathIterator2afp<T> iterator) {
			this.iterator = iterator;
			this.centerX = x;
			this.centerY = y;
			this.axisX1 = axis1X;
			this.axisY1 = axis1Y;
			this.axisX2 = axis2X;
			this.axisY2 = axis2Y;
		}
		
		@Override
		public PathIterator2afp<T> restartIterations() {
			return new ProjectionToParallelogramLocalCoordinateSystemPathIterator<>(
					this.centerX, this.centerY,
					this.axisX1, this.axisY1,
					this.axisX2, this.axisY2,
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
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getCtrlX2() - this.centerX, elem.getCtrlY2() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getCtrlX2() - this.centerX, elem.getCtrlY2() - this.centerY),
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case LINE_TO:
				return getGeomFactory().newLinePathElement(
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case MOVE_TO:
				return getGeomFactory().newMovePathElement(
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case QUAD_TO:
				return getGeomFactory().newCurvePathElement(
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY));
			case CLOSE:
				return getGeomFactory().newClosePathElement(
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
						projectVectorOnParallelogramRAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
								elem.getToX() - this.centerX, elem.getToY() - this.centerY),
						projectVectorOnParallelogramSAxis(this.axisX1, this.axisY1, this.axisX2, this.axisY2,
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

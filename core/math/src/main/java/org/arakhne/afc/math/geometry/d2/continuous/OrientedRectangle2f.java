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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.matrix.Matrix2f;

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
public class OrientedRectangle2f extends AbstractShape2f<OrientedRectangle2f> {	

	private static final long serialVersionUID = -7541381548010183873L;

	/**
	 * Compute the oriented bounding box axis.
	 * 
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the vector where the R axis of the OBR is put
	 * @param S is the vector where the S axis of the OBR is put
	 * @see "MGPCG pages 219-221"
	 */
	public static void computeOBRAxis(Iterable<? extends Point2D> points, Vector2f R, Vector2f S) {
		// Determining the covariance matrix of the points
		// and set the center of the box
		Matrix2f cov = new Matrix2f();
		cov.cov(points);

		//Determining eigenvectors of covariance matrix and defines RS axis
		Matrix2f rs = new Matrix2f();//eigenvectors                         
		cov.eigenVectorsOfSymmetricMatrix(rs);

		rs.getColumn(0,R);
		rs.getColumn(1,S);
	}

	/**
	 * Compute the OBR center and extents.
	 * 
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the R axis of the OBR
	 * @param S is the S axis of the OBR
	 * @param center is the point which is set with the OBR's center coordinates.
	 * @param extents are the extents of the OBR for the R and S axis.
	 * @see "MGPCG pages 222-223"
	 */
	public static void computeOBRCenterExtents(
			Iterable<? extends Point2D> points,
			Vector2f R, Vector2f S,
			Point2f center, Tuple2D<?> extents) {
		assert(points!=null);
		assert(center!=null);
		assert(extents!=null);

		double minR = Float.POSITIVE_INFINITY;
		double maxR = Float.NEGATIVE_INFINITY;
		double minS = Float.POSITIVE_INFINITY;
		double maxS = Float.NEGATIVE_INFINITY;

		double PdotR;
		double PdotS;
		Vector2f v = new Vector2f();

		for(Point2D tuple : points) {
			v.set(tuple);

			PdotR = v.dot(R);
			PdotS = v.dot(S);

			if (PdotR < minR) minR = PdotR;			
			if (PdotR > maxR) maxR = PdotR;			
			if (PdotS < minS) minS = PdotS;			
			if (PdotS > maxS) maxS = PdotS;			
		}

		double a = (maxR + minR) / 2.f;
		double b = (maxS + minS) / 2.f;

		// Set the center of the OBR
		center.set(
				a*R.getX()
				+b*S.getX(),

				a*R.getY()
				+b*S.getY());

		// Compute extents
		extents.set(
				(maxR - minR) / 2.,
				(maxS - minS) / 2.);
	}

	/** Replies if the specified rectangle intersects the specified segment.
	 *
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param s1x is the X coordinate of the first point of the segment.
	 * @param s1y is the Y coordinate of the first point of the segment.
	 * @param s2x is the X coordinate of the second point of the segment.
	 * @param s2y is the Y coordinate of the second point of the segment.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsOrientedRectangleSegment(
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			double s1x, double s1y, double s2x, double s2y) {
		//Changing Segment coordinate basis.
		double ax = (s1x-obrCenterX) * obrAxis2Y - (s1y-obrCenterY) * obrAxis2X;
		double ay = (s1y-obrCenterY) * obrAxis1X - (s1x-obrCenterX) * obrAxis1Y;
		double bx = (s2x-obrCenterX) * obrAxis2Y - (s2y-obrCenterY) * obrAxis2X;
		double by = (s2y-obrCenterY) * obrAxis1X - (s2x-obrCenterX) * obrAxis1Y;

		return Rectangle2f.intersectsRectangleSegment(
				-obrAxis1Extent, -obrAxis2Extent, obrAxis1Extent, obrAxis2Extent,
				ax, ay,  bx,  by);
	}

	/** Replies if the specified rectangle intersects the specified ellipse.
	 *
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param ex is the coordinate of the min point of the ellipse rectangle.
	 * @param ey is the coordinate of the min point of the ellipse rectangle.
	 * @param ewidth is the width of the ellipse.
	 * @param eheight is the height of the ellipse.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsOrientedRectangleEllipse(
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			double ex, double ey, double ewidth, double eheight){

		// Get the semimajor and semiminor axes.
		double a = ewidth / 2.;
		double b = eheight / 2.;

		// Translate so the ellipse is centered at the origin.
		double centerx = obrCenterX - (ex + a);
		double centery = obrCenterY - (ey + b);

		return intersectsOrientedRectangleSolidCircle(
				centerx, centery,
				obrAxis1X, obrAxis1Y, obrAxis1Extent / (a*a),
				obrAxis2X, obrAxis2Y, obrAxis2Extent / (b*b),
				0, 0, 1);
	}

	/** Replies if the specified rectangle intersects the specified circle.
	 *
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param circleX is the coordinate of the circle center.
	 * @param circleY is the coordinate of the circle center.
	 * @param circleRadius is the radius of the circle.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsOrientedRectangleSolidCircle(
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			double circleX, double circleY, double circleRadius) {
		// Find points on OBR closest and farest to sphere center
		Point2f closest = new Point2f();
		Point2f farthest = new Point2f();

		computeClosestFarthestPoints(
				circleX, circleY,
				obrCenterX, obrCenterY,
				obrAxis1X, obrAxis1Y, obrAxis1Extent,
				obrAxis2X, obrAxis2Y, obrAxis2Extent,
				closest, farthest);

		// Circle and OBR intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		double squaredRadius = circleRadius * circleRadius;

		return ! (MathUtil.isEpsilonEqual(
				Point2f.distanceSquaredPointPoint(
						obrCenterX, obrCenterY,
						closest.getX(), closest.getY()),
						squaredRadius));
	}

	/** Replies if the specified rectangles intersect.
	 * <p>
	 * The extents are assumed to be positive or zero.
	 * The lengths of the given arrays are assumed to be <code>2</code>.
	 * <p>
	 * This function uses the "separating axis theorem" which states that 
	 * for any two OBBs (AABB is a special case of OBB)
	 * that do not touch, a separating axis can be found.
	 * <p>
	 * This function uses an general intersection test between two OBR.
	 * If the first box is expected to be an MBR, please use the
	 * optimized algorithm given by
	 * {@link #intersectsOrientedRectangleRectangle(double, double, double, double, double, double, double, double, double, double, double, double)}.
	 *
	 * @param obr1CenterX
	 *            is the X coordinate of the OBR center.
	 * @param obr1CenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obr1Axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obr1Axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obr1Axis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obr1Axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obr1Axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obr1Axis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param obr2CenterX
	 *            is the X coordinate of the OBR center.
	 * @param obr2CenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obr2Axis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obr2Axis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obr2Axis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obr2Axis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obr2Axis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obr2Axis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.jkh.me/files/tutorials/Separating%20Axis%20Theorem%20for%20Oriented%20Bounding%20Boxes.pdf">Intersection between two oriented boudning rectangles</a>
	 */
	public static boolean intersectsOrientedRectangleOrientedRectangle(
			double obr1CenterX, double obr1CenterY,
			double obr1Axis1X, double obr1Axis1Y,
			double obr1Axis1Extent,
			double obr1Axis2X, double obr1Axis2Y,
			double obr1Axis2Extent,
			double obr2CenterX, double obr2CenterY,
			double obr2Axis1X, double obr2Axis1Y,
			double obr2Axis1Extent,
			double obr2Axis2X, double obr2Axis2Y,
			double obr2Axis2Extent){
		assert(obr1Axis1Extent >= 0.);
		assert(obr1Axis2Extent >= 0.);
		assert(obr2Axis1Extent >= 0.);
		assert(obr2Axis2Extent >= 0.);

		double tx = obr2CenterX - obr1CenterX;
		double ty = obr2CenterY - obr1CenterY;

		double scaledObr1Axis1X = obr1Axis1X * obr1Axis1Extent;
		double scaledObr1Axis1Y = obr1Axis1Y * obr1Axis1Extent;
		double scaledObr1Axis2X = obr1Axis2X * obr1Axis2Extent;
		double scaledObr1Axis2Y = obr1Axis2Y * obr1Axis2Extent;
		double scaledObr2Axis1X = obr2Axis1X * obr2Axis1Extent;
		double scaledObr2Axis1Y = obr2Axis1Y * obr2Axis1Extent;
		double scaledObr2Axis2X = obr2Axis2X * obr2Axis2Extent;
		double scaledObr2Axis2Y = obr2Axis2Y * obr2Axis2Extent;

		//Let A the first box and B the second one
		//L = Ax
		if (Math.abs(Vector2f.dotProduct(tx, ty, obr1Axis1X, obr1Axis1Y)) 
				> obr1Axis1Extent
				+ Math.abs(Vector2f.dotProduct(scaledObr2Axis1X, scaledObr2Axis1Y, obr1Axis1X, obr1Axis1Y)) 
				+ Math.abs(Vector2f.dotProduct(scaledObr2Axis2X, scaledObr2Axis2Y, obr1Axis1X, obr1Axis1Y))) {
			return false;
		}

		//L = Ay
		if (Math.abs(Vector2f.dotProduct(tx, ty, obr1Axis2X, obr1Axis2Y))
				> obr1Axis2Extent
				+ Math.abs(Vector2f.dotProduct(scaledObr2Axis1X, scaledObr2Axis1Y, obr1Axis2X, obr1Axis2Y))
				+ Math.abs(Vector2f.dotProduct(scaledObr2Axis2X, scaledObr2Axis2Y, obr1Axis2X, obr1Axis2Y))) {
			return false;
		}

		//L=Bx
		if (Math.abs(Vector2f.dotProduct(tx, ty, obr2Axis1X, obr2Axis1Y))
				> obr2Axis1Extent
				+ Math.abs(Vector2f.dotProduct(scaledObr1Axis1X, scaledObr1Axis1Y, obr2Axis1X, obr2Axis1Y))
				+ Math.abs(Vector2f.dotProduct(scaledObr1Axis2X, scaledObr1Axis2Y, obr2Axis1X, obr2Axis1Y))) {
			return false;
		}

		//L=By
		if (Math.abs(Vector2f.dotProduct(tx, ty, obr2Axis2X, obr2Axis2Y))
				> obr2Axis2Extent
				+ Math.abs(Vector2f.dotProduct(scaledObr1Axis1X, scaledObr1Axis1Y, obr2Axis2X, obr2Axis2Y))
				+ Math.abs(Vector2f.dotProduct(scaledObr1Axis2X, scaledObr1Axis2Y, obr2Axis2X, obr2Axis2Y))) {
			return false;
		}

		/*no separating axis found, the two boxes overlap */
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
	 * for any two OBBs (AABB is a special case of OBB)
	 * that do not touch, a separating axis can be found.
	 * <p>
	 * This function uses an optimized algorithm for AABB as first parameter.
	 * The general intersection type between two OBB is given by
	 * {@link #intersectsOrientedRectangleOrientedRectangle}.
	 *
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
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
	public static boolean intersectsOrientedRectangleRectangle(
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			double rx, double ry,
			double rwidth, double rheight) {
		assert(rwidth >= 0.);
		assert(rheight >= 0.);
		assert(obrAxis1Extent >= 0.);
		assert(obrAxis2Extent >= 0.);

		double mbrCenterx = rx + rwidth / 2.;
		double mbrCentery = ry + rheight / 2.;

		return intersectsOrientedRectangleOrientedRectangle(
				mbrCenterx, mbrCentery,
				1., 0., 0., 1.,
				rwidth / 2., rheight / 2.,
				obrCenterX, obrCenterY,
				obrAxis1X, obrAxis1Y, obrAxis1Extent,
				obrAxis2X, obrAxis2Y, obrAxis2Extent);
	}

	/** Replies if a point is inside the round rectangle.
	 * 
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param rx
	 *            is the X coordinate of the lower point of the rectangle.
	 * @param ry
	 *            is the Y coordinate of the lower point of the rectangle.
	 * @param rwidth
	 *            is the width of the rectangle.
	 * @param rheight
	 *            is the height of the rectangle.
	 * @return <code>true</code> if the given rectangle is inside the OBR;
	 * otherwise <code>false</code>.
	 */
	@Unefficient
	public static boolean containsOrientedRectangleRectangle(
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			double rx, double ry,
			double rwidth, double rheight) {
		if (containsOrientedRectanglePoint(
				obrCenterX, obrCenterY,
				obrAxis1X, obrAxis1Y, obrAxis1Extent,
				obrAxis2X, obrAxis2Y, obrAxis2Extent,
				2.5 * rx + rwidth,
				2.5 * ry + rheight)) {
			return !intersectsOrientedRectangleRectangle(
					obrCenterX, obrCenterY,
					obrAxis1X, obrAxis1Y, obrAxis1Extent,
					obrAxis2X, obrAxis2Y, obrAxis2Extent,
					rx, ry, rwidth, rheight);
		}
		return false;
	}

	/** Replies if a point is inside in the round rectangle.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @return <code>true</code> if the given point is inside the OBR;
	 * otherwise <code>false</code>.
	 */
	public static boolean containsOrientedRectanglePoint(
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			double px, double py) {
		// Changing P(x,y) coordinate basis.
		double nx = (px
				- (obrCenterX + obrAxis1Extent/2*obrAxis1X + obrAxis2Extent/2*obrAxis2X)) * obrAxis2Y
				- (py - (obrCenterY + obrAxis1Extent/2*obrAxis1Y + obrAxis2Extent/2*obrAxis2Y))
				* obrAxis2X;
		double ny = (py
				- (obrCenterY + obrAxis1Extent/2*obrAxis1Y + obrAxis2Extent/2*obrAxis2Y)) * obrAxis1X
				- (px - (obrCenterX + obrAxis1Extent/2*obrAxis1X + obrAxis2Extent/2*obrAxis2X))
				* obrAxis1Y;

		double rx = obrAxis1Extent / -2.;
		double ry = obrAxis2Extent / -2.;
		return Rectangle2f.containsRectanglePoint(
				nx, ny,
				rx, ry,
				(obrAxis1Extent / 2.) - rx,
				(obrAxis2Extent / 2.) - ry);
	}

	/**
	 * Given a point p, this function computes the point q1 on (or in) this OBB,
	 * closest to p. If there are several
	 * points, the function will return one of those. Remember this function may
	 * return an approximate result when points remain on OBB plane of symmetry.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @return the closest point.
	 */
	public static Point2f computeClosestPoint(
			double px, double py,
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent) {
		Point2f p = new Point2f();
		computeClosestFarthestPoints(
				px, py,
				obrCenterX, obrCenterY,
				obrAxis1X, obrAxis1Y,
				obrAxis1Extent,
				obrAxis2X, obrAxis2Y,
				obrAxis2Extent,
				p, null);
		return p;
	}

	/**
	 * Given a point p, this function computes the point q1 on this OBB,
	 * farthest to p. If there are several
	 * points, the function will return one of those. Remember this function may
	 * return an approximate result when points remain on OBB plane of symmetry.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @return the farthest point.
	 */
	public static Point2f computeFarthestPoint(
			double px, double py,
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent) {
		Point2f p = new Point2f();
		computeClosestFarthestPoints(
				px, py,
				obrCenterX, obrCenterY,
				obrAxis1X, obrAxis1Y,
				obrAxis1Extent,
				obrAxis2X, obrAxis2Y,
				obrAxis2Extent,
				null, p);
		return p;
	}

	/**
	 * Given a point p, this function computes the point q1 on (or in) this OBB,
	 * closest to p. If there are several
	 * points, the function will return one of those. Remember this function may
	 * return an approximate result when points remain on OBB plane of symmetry.
	 * 
	 * @param px
	 *            is the X coordinate of the point to test.
	 * @param py
	 *            is the Y coordinate of the point to test.
	 * @param obrCenterX
	 *            is the X coordinate of the OBR center.
	 * @param obrCenterY
	 *            is the Y coordinate of the OBR center.
	 * @param obrAxis1X
	 *            is the X coordinate of the axis 1 unit vector.
	 * @param obrAxis1Y
	 *            is the Y coordinate of the axis 1 unit vector.
	 * @param obrAxis1Extent
	 *            is the extent of the axis 1 of the OBB.
	 * @param obrAxis2X
	 *            is the X coordinate of the axis 2 unit vector.
	 * @param obrAxis2Y
	 *            is the Y coordinate of the axis 2 unit vector.
	 * @param obrAxis2Extent
	 *            is the extent of the axis 2 of the OBB.
	 * @param closest the closest point.
	 * @param farthest the farthest point.
	 */
	protected static void computeClosestFarthestPoints(
			double px, double py,
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			Point2f closest, Point2f farthest) {
		assert (obrAxis1Extent >= 0.);
		assert (obrAxis2Extent >= 0.);

		assert (new Vector2f(obrAxis1X, obrAxis1Y).isUnitVector());
		assert (new Vector2f(obrAxis2X, obrAxis2Y).isUnitVector());

		double dx = px - obrCenterX;
		double dy = py - obrCenterY;

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = Vector2f.dotProduct(dx, dy, obrAxis1X, obrAxis1Y);
		double d2 = Vector2f.dotProduct(dx, dy, obrAxis2X, obrAxis2Y);

		if (closest != null) {
			double clampedD1 = MathUtil.clamp(d1, -obrAxis1Extent, obrAxis1Extent);
			double clampedD2 = MathUtil.clamp(d2, -obrAxis2Extent, obrAxis2Extent);

			// Step that distance along the axis to get world coordinate
			// q1 += dist * this.axis[i]; (If distance farther than the box
			// extents, clamp to the box)
			closest.set(
					obrCenterX + clampedD1 * obrAxis1X + clampedD2 * obrAxis2X,
					obrCenterY + clampedD1 * obrAxis1Y + clampedD2 * obrAxis2Y);
		}

		if (farthest != null) {
			// Clamp to the other side of the box
			if (d1 >= 0.) {
				d1 = -obrAxis1Extent;
			} else {
				d1 = obrAxis1Extent;
			}
			if (d2 >= 0.) {
				d2 = -obrAxis2Extent;
			} else {
				d2 = obrAxis2Extent;
			}

			// Step that distance along the axis to get world coordinate
			// q2 += dist * this.axis[i];
			farthest.set(
					obrCenterX + d1 * obrAxis1X + d2 * obrAxis2X,
					obrCenterY + d1 * obrAxis1Y + d2 * obrAxis2Y);

		}
	}

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
	 * @param cx the center x.
	 * @param cy the center y.
	 */
	public void setCenter(double cx, double cy) {
		this.cx = cx;
		this.cy = cy;
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
	public Point2D getClosestPointTo(Point2D p) {
		double dx = p.getX() - this.cx;
		double dy = p.getY() - this.cy;

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = Vector2f.dotProduct(dx, dy, this.rx, this.ry);
		double d2 = Vector2f.dotProduct(dx, dy, this.sx, this.sy);

		double clampedD1 = MathUtil.clamp(d1, -this.extentR, this.extentR);
		double clampedD2 = MathUtil.clamp(d2, -this.extentS, this.extentS);

		// Step that distance along the axis to get world coordinate
		// q1 += dist * this.axis[i]; (If distance farther than the box
		// extents, clamp to the box)
		return new Point2f(
				this.cx + clampedD1 * this.rx + clampedD2 * this.sx,
				this.cy + clampedD1 * this.ry + clampedD2 * this.sy);
	}

	@Override
	public Point2D getFarthestPointTo(Point2D p) {
		double dx = p.getX() - this.cx;
		double dy = p.getY() - this.cy;

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = Vector2f.dotProduct(dx, dy, this.rx, this.ry);
		double d2 = Vector2f.dotProduct(dx, dy, this.sx, this.sy);

		// Clamp to the other side of the box
		if (d1 >= 0.)
			d1 = -this.extentR;
		else
			d1 = this.extentR;

		if (d2 >= 0.)
			d2 = -this.extentS;
		else
			d2 = this.extentR;

		// Step that distance along the axis to get world coordinate
		// q2 += dist * this.axis[i];
		return new Point2f(
				this.cx + d1 * this.rx + d2 * this.sx,
				this.cy	+ d1 * this.ry + d2 * this.sy);
	}

	@Override
	public Rectangle2f toBoundingBox() {
		Rectangle2f rect = new Rectangle2f();
		toBoundingBox(rect);
		return rect;
	}

	@Override
	public void toBoundingBox(Rectangle2f box) {
		Point2f minCorner, maxCorner;

		minCorner = new Point2f(this.cx, this.cy);
		maxCorner = new Point2f(this.cx, this.cy);

		double srx = this.rx * this.extentR;
		double sry = this.ry * this.extentR;
		double ssx = this.sx * this.extentS;
		double ssy = this.sy * this.extentS;

		if(this.rx >= 0.) {
			if (this.ry >= 0.) {
				minCorner.add(-srx + ssx, -sry - ssy);
				maxCorner.sub(-srx + ssx, -sry - ssy);
			} else {
				minCorner.add(-srx - ssx, sry - ssy);
				maxCorner.sub(-srx - ssx, sry - ssy);
			}
		} else {
			if (this.ry >= 0.){
				minCorner.add( srx + ssx, -sry + ssy);
				maxCorner.sub( srx + ssx, -sry + ssy);
			} else {
				minCorner.add( srx - ssx, sry + ssy);
				maxCorner.sub( srx - ssx, sry + ssy);
			}
		}
		box.setFromCorners(minCorner, maxCorner);
	}

	@Override
	public double distanceSquared(Point2D p) {
		Point2f closest = computeClosestPoint(
				p.getX(), p.getY(),
				this.cx, this.cy,
				this.rx, this.ry, this.extentR,
				this.sx, this.sy, this.extentS);
		return Point2f.distanceSquaredPointPoint(
				p.getX(), p.getY(),
				closest.getX(), closest.getY());
	}

	@Override
	public double distanceL1(Point2D p) {
		Point2f closest = computeClosestPoint(
				p.getX(), p.getY(),
				this.cx, this.cy,
				this.rx, this.ry, this.extentR,
				this.sx, this.sy, this.extentS);
		return Point2f.distanceL1PointPoint(
				p.getX(), p.getY(),
				closest.getX(), closest.getY());
	}

	@Override
	public double distanceLinf(Point2D p) {
		Point2f closest = computeClosestPoint(
				p.getX(), p.getY(),
				this.cx, this.cy,
				this.rx, this.ry, this.extentR,
				this.sx, this.sy, this.extentS);
		return Point2f.distanceLinfPointPoint(
				p.getX(), p.getY(),
				closest.getX(), closest.getY());
	}

	@Override
	public void translate(double dx, double dy) {
		this.cx += dx;
		this.cy += dy;
	}

	@Override
	public boolean contains(double x, double y) {
		return containsOrientedRectanglePoint(
				this.cx, this.cy,
				this.rx, this.ry, this.extentR,
				this.sx, this.sy, this.extentS,
				x, y);
	}

	@Override
	public boolean contains(Rectangle2f r) {
		return containsOrientedRectangleRectangle(
				this.cx, this.cy,
				this.rx, this.ry, this.extentR,
				this.sx, this.sy, this.extentS,
				r.getMinX(), r.getMinY(),
				r.getWidth(), r.getHeight());
	}

	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform == null) {
			return new CopyPathIterator(
					this.cx, this.cy, this.rx, this.ry, this.extentR, this.extentS);
		}
		return new TransformPathIterator(
				this.cx, this.cy, this.rx, this.ry, this.extentR, this.extentS,
				transform);
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return intersectsOrientedRectangleRectangle(
				this.cx, this.cy, this.rx, this.ry, this.extentR, this.sx, this.sy, this.extentS,
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return intersectsOrientedRectangleEllipse(
				this.cx, this.cy, this.rx, this.ry, this.extentR, this.sx, this.sy, this.extentS,
				s.minx, s.miny, s.maxy - s.minx, s.maxy - s.maxy);
	}

	@Override
	public boolean intersects(Circle2f s) {
		return intersectsOrientedRectangleSolidCircle(
				this.cx, this.cy, this.rx, this.ry, this.extentR, this.sx, this.sy, this.extentS,
				s.cx,s.cy, s.radius);
	}

	@Override
	public boolean intersects(Segment2f s) {
		return intersectsOrientedRectangleSegment(
				this.cx, this.cy, this.rx, this.ry, this.extentR, this.sx, this.sy, this.extentS,
				s.ax, s.ay, s.bx, s.by);
	}

	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return intersectsOrientedRectangleOrientedRectangle(
				this.cx, this.cy,
				this.rx, this.ry, this.extentR, this.sx, this.sy, this.extentS,
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent());
	}

	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		if (isEmpty()) {
			return false;
		}

		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);

		int crossings = Path2f.computeCrossingsFromRect(
				new TranformPathIteratorWrapper(s), 
				this.extentR/-2., this.extentS/-2., this.extentR/2., this.extentS/2.,
				false, true);

		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean isEmpty() {
		return(this.extentR == 0.
				|| this.extentS == 0.
				|| (this.rx == 0. && this.ry == 0.)
				|| (this.sx == 0. && this.sy == 0.));
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
	public void set(final Shape2f s) {
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

	@Override
	public boolean equals(Object obj) {
		if (this==obj) return true;
		if (obj instanceof OrientedRectangle2f) {
			OrientedRectangle2f s = (OrientedRectangle2f)obj;
			if(s.cx == this.cx &&
					s.cy == this.cy &&
					s.rx == this.rx &&
					s.ry == this.ry &&
					s.sx == this.sx &&
					s.sy == this.sy &&
					s.extentR == this.extentR &&
					s.extentS == this.extentS)
				return true;
		}
		return false;

	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getCenterX());
		bits = 31L * bits + doubleToLongBits(getCenterY());
		bits = 31L * bits + doubleToLongBits(getFirstAxisX());
		bits = 31L * bits + doubleToLongBits(getFirstAxisY());
		bits = 31L * bits + doubleToLongBits(getSecondAxisX());
		bits = 31L * bits + doubleToLongBits(getSecondAxisY());
		bits = 31L * bits + doubleToLongBits(getFirstAxisExtent());
		bits = 31L * bits + doubleToLongBits(getSecondAxisExtent());
		return (int) (bits ^ (bits >> 32));
	}

	/**
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator implements PathIterator2f {

		private double x1;
		private double y1;
		private Vector2D r;
		private Vector2D s;
		private int index = 0;

		/**
		 * @param centerx
		 * @param centery
		 * @param rx
		 * @param ry
		 * @param axis1Extent
		 * @param axis2Extent
		 */
		public CopyPathIterator(double centerx, double centery, double rx, double ry, double axis1Extent, double axis2Extent) {
			assert(axis1Extent > 0 && axis2Extent > 0);

			this.r = new Vector2f(rx, ry);

			assert(this.r.lengthSquared() == 1);

			this.s = new Vector2f(rx, ry);
			this.s.perpendicularize();

			this.r.scale(axis1Extent);
			this.s.scale(axis2Extent);

			this.x1 = centerx - (this.r.getX() + this.s.getX());
			this.y1 = centerx - (this.r.getY() + this.s.getY());

			this.index = 6;

			this.r.scale(2);
			this.s.scale(2);
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2f next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new PathElement2f.MovePathElement2f(
						this.x1, this.y1);
			case 1:
				return new PathElement2f.LinePathElement2f(
						this.x1, this.y1,
						this.x1 + this.r.getX(), this.y1 + this.r.getY());
			case 2:
				return new PathElement2f.LinePathElement2f(
						this.x1 + this.r.getX(), this.y1 + this.r.getY(),
						this.x1 + this.r.getX() + this.s.getX(), this.y1 + this.r.getY() + this.s.getY());
			case 3:
				return new PathElement2f.LinePathElement2f(
						this.x1 + this.r.getX() + this.s.getX(), this.y1 + this.r.getY() + this.s.getY(),
						this.x1 + this.s.getX(), this.y1 + this.s.getY());
			case 4:
				return new PathElement2f.LinePathElement2f(
						this.x1 + this.s.getX(), this.y1 + this.s.getY(),
						this.x1, this.y1);
			case 5:
				return new PathElement2f.ClosePathElement2f(
						this.x1, this.y1,
						this.x1, this.y1);
			default:
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Override
		public boolean isPolyline() {
			return true;
		}
	}

	/**
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator implements PathIterator2f {

		private final Transform2D transform;
		private double x1;
		private double y1;
		private Vector2D r;
		private Vector2D s;

		private int index = 0;

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();

		/**
		 * @param centerx
		 * @param centery
		 * @param rx
		 * @param ry
		 * @param axis1Extent
		 * @param axis2Extent
		 * @param transform
		 */
		public TransformPathIterator(double centerx, double centery, double rx, double ry, double axis1Extent, double axis2Extent, Transform2D transform) {

			assert(axis1Extent > 0 && axis2Extent > 0);

			this.r = new Vector2f(rx, ry);

			assert(this.r.lengthSquared() == 1);

			this.s = new Vector2f(rx, ry);
			this.s.perpendicularize();

			this.r.scale(axis1Extent);
			this.s.scale(axis2Extent);

			this.transform = transform;

			this.x1 = centerx - 1/2*(this.r.getX() + this.s.getX());
			this.y1 = centerx - 1/2*(this.r.getY() + this.s.getY());

			this.index = 6;

		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2f next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.add(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.add(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.sub(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.sub(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return new PathElement2f.ClosePathElement2f(
						this.p2.getX(), this.p2.getY(),
						this.p2.getX(), this.p2.getY());
			default:
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Override
		public boolean isPolyline() {
			return true;
		}

	}

	/**
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TranformPathIteratorWrapper implements PathIterator2f{

		private final PathIterator2f p;

		public TranformPathIteratorWrapper(PathIterator2f p){
			this.p = p;
		}

		@Override
		public boolean hasNext() {
			return this.p.hasNext();
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public PathElement2f next() {
			PathElement2f elem = this.p.next();
			switch(elem.type){
			case CURVE_TO:
				return new PathElement2f.CurvePathElement2f(
						(elem.fromX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.fromY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.fromY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.fromX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry,
						(elem.ctrlX1-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.ctrlY1-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.ctrlY1-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.ctrlX1-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry,
						(elem.ctrlX2-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.ctrlY2-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.ctrlY2-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.ctrlX2-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry,
						(elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry);
			case LINE_TO:
				return new PathElement2f.LinePathElement2f(
						(elem.fromX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.fromY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.fromY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.fromX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry,
						(elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry);
			case MOVE_TO:
				return new PathElement2f.MovePathElement2f((elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry);
			case QUAD_TO:
				return new PathElement2f.QuadPathElement2f(
						(elem.fromX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.fromY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.fromY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.fromX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry,
						(elem.ctrlX1-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.ctrlY1-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.ctrlY1-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.ctrlX1-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry,
						(elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.sy - (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.sx, (elem.toY-OrientedRectangle2f.this.cy) * OrientedRectangle2f.this.rx - (elem.toX-OrientedRectangle2f.this.cx) * OrientedRectangle2f.this.ry);
			case CLOSE:
			default:
				break;

			}
			return null;
		}

		@Override
		public void remove() {
			this.p.remove();			
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.p.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return this.p.isPolyline();
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class PointIterator implements Iterator<Point2f> {

		private final PathIterator2f iterator;
		private final Deque<Point2f> points = new LinkedList<>();

		/**
		 * @param iterator
		 */
		public PointIterator(PathIterator2f iterator) {
			this.iterator = iterator;
			searchCandidates();
		}

		private void searchCandidates() {
			while (this.points.isEmpty() && this.iterator.hasNext()) {
				PathElement2f element = this.iterator.next();
				switch (element.getType()) {
				case MOVE_TO:
				case LINE_TO:
					this.points.push(new Point2f(element.toX, element.toY));
					break;
				case CURVE_TO:
				{
					Path2f p1 = new Path2f();
					p1.moveTo(element.fromX, element.fromY);
					p1.curveTo(
							element.ctrlX1, element.ctrlY1,
							element.ctrlX2, element.ctrlY2,
							element.toX, element.toY);
					PathIterator2f ii1 = p1.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
					while (ii1.hasNext()) {
						PathElement2f elt = ii1.next();
						switch (elt.getType()) {
						case MOVE_TO:
						case LINE_TO:
							this.points.push(new Point2f(element.toX, element.toY));
							break;
						case CURVE_TO:
						case QUAD_TO:
						case CLOSE:
						default:
						}
					}
					break;
				}
				case QUAD_TO:
				{
					Path2f p1 = new Path2f();
					p1.moveTo(element.fromX, element.fromY);
					p1.quadTo(
							element.ctrlX1, element.ctrlY1,
							element.toX, element.toY);
					PathIterator2f ii1 = p1.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
					while (ii1.hasNext()) {
						PathElement2f elt = ii1.next();
						switch (elt.getType()) {
						case MOVE_TO:
						case LINE_TO:
							this.points.push(new Point2f(element.toX, element.toY));
							break;
						case CURVE_TO:
						case QUAD_TO:
						case CLOSE:
						default:
						}
					}
					break;
				}
				case CLOSE:
				default:
				}
			}
		}

		@Override
		public boolean hasNext() {
			return !this.points.isEmpty();
		}

		@Override
		public Point2f next() {
			if (this.points.isEmpty()) {
				throw new NoSuchElementException();
			}
			Point2f p = this.points.removeFirst();
			searchCandidates();
			return p;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}

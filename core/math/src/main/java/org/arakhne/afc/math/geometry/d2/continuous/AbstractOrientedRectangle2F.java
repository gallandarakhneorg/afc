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

package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.FunctionalVector2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Definition of a fixed Oriented Rectangle.
 *  * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: cbohrhauer$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractOrientedRectangle2F<T extends Shape2F> extends AbstractShape2F<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2679497994139764140L;
	
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

		double minR = Double.POSITIVE_INFINITY;
		double maxR = Double.NEGATIVE_INFINITY;
		double minS = Double.POSITIVE_INFINITY;
		double maxS = Double.NEGATIVE_INFINITY;

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
	@Pure
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

		return AbstractRectangle2F.intersectsRectangleSegment(
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
	@Pure
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
	@Pure
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
				FunctionalPoint2D.distanceSquaredPointPoint(
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
	@Pure
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
		if (Math.abs(FunctionalVector2D.dotProduct(tx, ty, obr1Axis1X, obr1Axis1Y)) 
				> obr1Axis1Extent
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr2Axis1X, scaledObr2Axis1Y, obr1Axis1X, obr1Axis1Y)) 
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr2Axis2X, scaledObr2Axis2Y, obr1Axis1X, obr1Axis1Y))) {
			return false;
		}

		//L = Ay
		if (Math.abs(FunctionalVector2D.dotProduct(tx, ty, obr1Axis2X, obr1Axis2Y))
				> obr1Axis2Extent
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr2Axis1X, scaledObr2Axis1Y, obr1Axis2X, obr1Axis2Y))
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr2Axis2X, scaledObr2Axis2Y, obr1Axis2X, obr1Axis2Y))) {
			return false;
		}

		//L=Bx
		if (Math.abs(FunctionalVector2D.dotProduct(tx, ty, obr2Axis1X, obr2Axis1Y))
				> obr2Axis1Extent
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr1Axis1X, scaledObr1Axis1Y, obr2Axis1X, obr2Axis1Y))
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr1Axis2X, scaledObr1Axis2Y, obr2Axis1X, obr2Axis1Y))) {
			return false;
		}

		//L=By
		if (Math.abs(FunctionalVector2D.dotProduct(tx, ty, obr2Axis2X, obr2Axis2Y))
				> obr2Axis2Extent
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr1Axis1X, scaledObr1Axis1Y, obr2Axis2X, obr2Axis2Y))
				+ Math.abs(FunctionalVector2D.dotProduct(scaledObr1Axis2X, scaledObr1Axis2Y, obr2Axis2X, obr2Axis2Y))) {
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
	@Pure
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
	@Pure
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
	@Pure
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
		return AbstractRectangle2F.containsRectanglePoint(
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
	@Pure
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
	@Pure
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
		double d1 = FunctionalVector2D.dotProduct(dx, dy, obrAxis1X, obrAxis1Y);
		double d2 = FunctionalVector2D.dotProduct(dx, dy, obrAxis2X, obrAxis2Y);

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

	/** Replies the center.
	 *
	 * @return the center.
	 */
	@Pure
	abstract public Point2f getCenter();

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	@Pure
	abstract public double getCenterX();

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	abstract public double getCenterY();

	/** Set the center.
	 * 
	 * @param cx the center x.
	 * @param cy the center y.
	 */
	abstract public void setCenter(double cx, double cy);

	/** Set the center.
	 * 
	 * @param center
	 */
	abstract public void setCenter(Point2D center);

	/** Replies the first axis of the oriented rectangle.
	 *
	 * @return the unit vector of the first axis. 
	 */
	@Pure
	abstract public Vector2f getFirstAxis();

	/** Replies coordinate x of the first axis of the oriented rectangle.
	 *
	 * @return the coordinate x of the unit vector of the first axis. 
	 */
	@Pure
	abstract public double getFirstAxisX();

	/** Replies coordinate y of the first axis of the oriented rectangle.
	 *
	 * @return the coordinate y of the unit vector of the first axis. 
	 */
	@Pure
	abstract public double getFirstAxisY();

	/** Replies the second axis of the oriented rectangle.
	 *
	 * @return the unit vector of the second axis. 
	 */
	@Pure
	abstract public Vector2f getSecondAxis();

	/** Replies coordinate x of the second axis of the oriented rectangle.
	 *
	 * @return the coordinate x of the unit vector of the second axis. 
	 */
	@Pure
	abstract public double getSecondAxisX();

	/** Replies coordinate y of the second axis of the oriented rectangle.
	 *
	 * @return the coordinate y of the unit vector of the second axis. 
	 */
	@Pure
	abstract public double getSecondAxisY();

	/** Replies the demi-size of the rectangle along its first axis.
	 * 
	 * @return the extent along the first axis.
	 */
	@Pure
	abstract public double getFirstAxisExtent();

	/** Change the demi-size of the rectangle along its first axis.
	 * 
	 * @param extent - the extent along the first axis.
	 */
	abstract public void setFirstAxisExtent(double extent);

	/** Replies the demi-size of the rectangle along its second axis.
	 * 
	 * @return the extent along the second axis.
	 */
	@Pure
	abstract public double getSecondAxisExtent();

	/** Change the demi-size of the rectangle along its second axis.
	 * 
	 * @param extent - the extent along the second axis.
	 */
	abstract public void setSecondAxisExtent(double extent);

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	abstract public void setFirstAxis(Vector2D axis);

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	abstract public void setFirstAxis(Vector2D axis, double extent);

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param x
	 * @param y
	 */
	abstract public void setFirstAxis(double x, double y);

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param x
	 * @param y
	 * @param extent
	 */
	abstract public void setFirstAxis(double x, double y, double extent);

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	abstract public void setSecondAxis(Vector2D axis);

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	abstract public void setSecondAxis(Vector2D axis, double extent);

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param x - the new values for the first axis.
	 * @param y - the new values for the first axis.
	 */
	abstract public void setSecondAxis(double x, double y);

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param x
	 * @param y
	 * @param extent
	 */
	abstract public void setSecondAxis(double x, double y, double extent);

	@Pure
	@Override
	public Point2D getClosestPointTo(Point2D p) {
		double dx = p.getX() - this.getCenterX();
		double dy = p.getY() - this.getCenterY();

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = FunctionalVector2D.dotProduct(dx, dy, this.getFirstAxisX(), this.getFirstAxisY());
		double d2 = FunctionalVector2D.dotProduct(dx, dy, this.getSecondAxisX(), this.getSecondAxisY());

		double clampedD1 = MathUtil.clamp(d1, -this.getFirstAxisExtent(), this.getFirstAxisExtent());
		double clampedD2 = MathUtil.clamp(d2, -this.getSecondAxisExtent(), this.getSecondAxisExtent());

		// Step that distance along the axis to get world coordinate
		// q1 += dist * this.axis[i]; (If distance farther than the box
		// extents, clamp to the box)
		return new Point2f(
				this.getCenterX() + clampedD1 * this.getFirstAxisX() + clampedD2 * this.getSecondAxisX(),
				this.getCenterY() + clampedD1 * this.getFirstAxisY() + clampedD2 * this.getSecondAxisY());
	}

	@Pure
	@Override
	public Point2D getFarthestPointTo(Point2D p) {
		double dx = p.getX() - this.getCenterX();
		double dy = p.getY() - this.getCenterY();

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = FunctionalVector2D.dotProduct(dx, dy, this.getFirstAxisX(), this.getFirstAxisY());
		double d2 = FunctionalVector2D.dotProduct(dx, dy, this.getSecondAxisX(), this.getSecondAxisY());

		// Clamp to the other side of the box
		if (d1 >= 0.)
			d1 = -this.getFirstAxisExtent();
		else
			d1 = this.getFirstAxisExtent();

		if (d2 >= 0.)
			d2 = -this.getSecondAxisExtent();
		else
			d2 = this.getFirstAxisExtent();

		// Step that distance along the axis to get world coordinate
		// q2 += dist * this.axis[i];
		return new Point2f(
				this.getCenterX() + d1 * this.getFirstAxisX() + d2 * this.getSecondAxisX(),
				this.getCenterY()	+ d1 * this.getFirstAxisY() + d2 * this.getSecondAxisY());
	}

	@Pure
	@Override
	public AbstractRectangle2F<?> toBoundingBox() {
		Rectangle2f rect = new Rectangle2f();
		toBoundingBox(rect);
		return rect;
	}

	@Override
	public void toBoundingBox(AbstractRectangle2F<?> box) {
		Point2f minCorner, maxCorner;

		minCorner = new Point2f(this.getCenterX(), this.getCenterY());
		maxCorner = new Point2f(this.getCenterX(), this.getCenterY());

		double srx = this.getFirstAxisX() * this.getFirstAxisExtent();
		double sry = this.getFirstAxisY() * this.getFirstAxisExtent();
		double ssx = this.getSecondAxisX() * this.getSecondAxisExtent();
		double ssy = this.getSecondAxisY() * this.getSecondAxisExtent();

		if(this.getFirstAxisX() >= 0.) {
			if (this.getFirstAxisY() >= 0.) {
				minCorner.add(-srx + ssx, -sry - ssy);
				maxCorner.sub(-srx + ssx, -sry - ssy);
			} else {
				minCorner.add(-srx - ssx, sry - ssy);
				maxCorner.sub(-srx - ssx, sry - ssy);
			}
		} else {
			if (this.getFirstAxisY() >= 0.){
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
	public double distanceSquared(Point2D p) {
		Point2f closest = computeClosestPoint(
				p.getX(), p.getY(),
				this.getCenterX(), this.getCenterY(),
				this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(),
				this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent());
		return FunctionalPoint2D.distanceSquaredPointPoint(
				p.getX(), p.getY(),
				closest.getX(), closest.getY());
	}

	@Pure
	@Override
	public double distanceL1(Point2D p) {
		Point2f closest = computeClosestPoint(
				p.getX(), p.getY(),
				this.getCenterX(), this.getCenterY(),
				this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(),
				this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent());
		return FunctionalPoint2D.distanceL1PointPoint(
				p.getX(), p.getY(),
				closest.getX(), closest.getY());
	}

	@Pure
	@Override
	public double distanceLinf(Point2D p) {
		Point2f closest = computeClosestPoint(
				p.getX(), p.getY(),
				this.getCenterX(), this.getCenterY(),
				this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(),
				this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent());
		return FunctionalPoint2D.distanceLinfPointPoint(
				p.getX(), p.getY(),
				closest.getX(), closest.getY());
	}

	@Override
	public void translate(double dx, double dy) {
		this.setCenter(this.getCenterX() + dx,this.getCenterY() + dy);
	}

	@Pure
	@Override
	public boolean contains(double x, double y) {
		return containsOrientedRectanglePoint(
				this.getCenterX(), this.getCenterY(),
				this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(),
				this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent(),
				x, y);
	}

	@Pure
	@Override
	public boolean contains(AbstractRectangle2F<?> r) {
		return containsOrientedRectangleRectangle(
				this.getCenterX(), this.getCenterY(),
				this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(),
				this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent(),
				r.getMinX(), r.getMinY(),
				r.getWidth(), r.getHeight());
	}

	@Pure
	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform == null) {
			return new CopyPathIterator2f(
					this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisExtent());
		}
		return new TransformPathIterator2f(
				this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisExtent(),
				transform);
	}
	
	@Pure
	@Override
	public PathIterator2d getPathIteratorProperty(Transform2D transform) {
		if (transform == null) {
			return new CopyPathIterator2d(
					this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisExtent());
		}
		return new TransformPathIterator2d(
				this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisExtent(),
				transform);
	}

	@Pure
	@Override
	public boolean intersects(AbstractRectangle2F<?> s) {
		return intersectsOrientedRectangleRectangle(
				this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent(),
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight());
	}

	@Pure
	@Override
	public boolean intersects(AbstractEllipse2F<?> s) {
		return intersectsOrientedRectangleEllipse(
				this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent(),
				s.getMinX(), s.getMinY(), s.getMaxX() - s.getMinX(), s.getMaxY() - s.getMinY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractCircle2F<?> s) {
		return intersectsOrientedRectangleSolidCircle(
				this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent(),
				s.getX(),s.getY(), s.getRadius());
	}

	@Pure
	@Override
	public boolean intersects(AbstractSegment2F<?> s) {
		return intersectsOrientedRectangleSegment(
				this.getCenterX(), this.getCenterY(), this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	@Pure
	@Override
	public boolean intersects(AbstractOrientedRectangle2F<?> s) {
		return intersectsOrientedRectangleOrientedRectangle(
				this.getCenterX(), this.getCenterY(),
				this.getFirstAxisX(), this.getFirstAxisY(), this.getFirstAxisExtent(), this.getSecondAxisX(), this.getSecondAxisY(), this.getSecondAxisExtent(),
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent());
	}

	@Pure
	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}
	
	@Pure
	@Override
	public boolean intersects(Path2d s) {
		return intersects(s.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		if (isEmpty()) {
			return false;
		}

		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);

		int crossings = Path2f.computeCrossingsFromRect(
				new TranformPathIteratorWrapper2f(s), 
				this.getFirstAxisExtent()/-2., this.getSecondAxisExtent()/-2., this.getFirstAxisExtent()/2., this.getSecondAxisExtent()/2.,
				false, true);

		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	@Override
	public boolean intersects(PathIterator2d s) {
		if (isEmpty()) {
			return false;
		}

		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);

		int crossings = Path2d.computeCrossingsFromRect(
				new TranformPathIteratorWrapper2d(s), 
				this.getFirstAxisExtent()/-2., this.getSecondAxisExtent()/-2., this.getFirstAxisExtent()/2., this.getSecondAxisExtent()/2.,
				false, true);

		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean isEmpty() {
		return(this.getFirstAxisExtent() == 0.
				|| this.getSecondAxisExtent() == 0.
				|| (this.getFirstAxisX() == 0. && this.getFirstAxisY() == 0.)
				|| (this.getSecondAxisX() == 0. && this.getSecondAxisY() == 0.));
	}

	@Override
	abstract  public void clear();

	@Override
	abstract public void set(final Shape2F s);

	/** Set the oriented rectangle.
	 * The second axis is automatically computed.
	 *
	 * @param center is the OBR center.
	 * @param axis1 is the first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	abstract public void set(Point2D center, Vector2D axis1, double axis1Extent, double axis2Extent);

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
	abstract public void set(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2Extent);

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
	abstract protected void set(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2X, double axis2Y, double axis2Extent);

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	abstract public void setFromPointCloud(Iterable<? extends Point2D> pointCloud);

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	abstract public void setFromPointCloud(Point2D... pointCloud);

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (this==obj) return true;
		if (obj instanceof OrientedRectangle2f) {
			OrientedRectangle2f s = (OrientedRectangle2f)obj;
			if(s.getCenterX() == this.getCenterX() &&
					s.getCenterY() == this.getCenterY() &&
					s.getFirstAxisX() == this.getFirstAxisX() &&
					s.getFirstAxisY() == this.getFirstAxisY() &&
					s.getSecondAxisX() == this.getSecondAxisX() &&
					s.getSecondAxisY() == this.getSecondAxisY() &&
					s.getFirstAxisExtent() == this.getFirstAxisExtent() &&
					s.getSecondAxisExtent() == this.getSecondAxisExtent())
				return true;
		}
		return false;

	}

	@Pure
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
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator2f implements PathIterator2f {

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
		public CopyPathIterator2f(double centerx, double centery, double rx, double ry, double axis1Extent, double axis2Extent) {
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

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2F next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new AbstractPathElement2F.MovePathElement2f(
						this.x1, this.y1);
			case 1:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x1, this.y1,
						this.x1 + this.r.getX(), this.y1 + this.r.getY());
			case 2:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x1 + this.r.getX(), this.y1 + this.r.getY(),
						this.x1 + this.r.getX() + this.s.getX(), this.y1 + this.r.getY() + this.s.getY());
			case 3:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x1 + this.r.getX() + this.s.getX(), this.y1 + this.r.getY() + this.s.getY(),
						this.x1 + this.s.getX(), this.y1 + this.s.getY());
			case 4:
				return new AbstractPathElement2F.LinePathElement2f(
						this.x1 + this.s.getX(), this.y1 + this.s.getY(),
						this.x1, this.y1);
			case 5:
				return new AbstractPathElement2F.ClosePathElement2f(
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return true;
		}
	}
	
	
	/**
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator2d implements PathIterator2d {

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
		public CopyPathIterator2d(double centerx, double centery, double rx, double ry, double axis1Extent, double axis2Extent) {
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

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2D next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new AbstractPathElement2D.MovePathElement2d(
						this.x1, this.y1);
			case 1:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x1, this.y1,
						this.x1 + this.r.getX(), this.y1 + this.r.getY());
			case 2:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x1 + this.r.getX(), this.y1 + this.r.getY(),
						this.x1 + this.r.getX() + this.s.getX(), this.y1 + this.r.getY() + this.s.getY());
			case 3:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x1 + this.r.getX() + this.s.getX(), this.y1 + this.r.getY() + this.s.getY(),
						this.x1 + this.s.getX(), this.y1 + this.s.getY());
			case 4:
				return new AbstractPathElement2D.LinePathElement2d(
						this.x1 + this.s.getX(), this.y1 + this.s.getY(),
						this.x1, this.y1);
			case 5:
				return new AbstractPathElement2D.ClosePathElement2d(
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return true;
		}
	}

	/**
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator2f implements PathIterator2f {

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
		 * @param transform1
		 */
		public TransformPathIterator2f(double centerx, double centery, double rx, double ry, double axis1Extent, double axis2Extent, Transform2D transform1) {

			assert(axis1Extent > 0 && axis2Extent > 0);

			this.r = new Vector2f(rx, ry);

			assert(this.r.lengthSquared() == 1);

			this.s = new Vector2f(rx, ry);
			this.s.perpendicularize();

			this.r.scale(axis1Extent);
			this.s.scale(axis2Extent);

			this.transform = transform1;

			this.x1 = centerx - 1/2*(this.r.getX() + this.s.getX());
			this.y1 = centerx - 1/2*(this.r.getY() + this.s.getY());

			this.index = 6;

		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2F next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.add(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.add(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.sub(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.sub(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return new AbstractPathElement2F.ClosePathElement2f(
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return true;
		}

	}
	
	/**
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator2d implements PathIterator2d {

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
		 * @param transform1
		 */
		public TransformPathIterator2d(double centerx, double centery, double rx, double ry, double axis1Extent, double axis2Extent, Transform2D transform1) {

			assert(axis1Extent > 0 && axis2Extent > 0);

			this.r = new Vector2f(rx, ry);

			assert(this.r.lengthSquared() == 1);

			this.s = new Vector2f(rx, ry);
			this.s.perpendicularize();

			this.r.scale(axis1Extent);
			this.s.scale(axis2Extent);

			this.transform = transform1;

			this.x1 = centerx - 1/2*(this.r.getX() + this.s.getX());
			this.y1 = centerx - 1/2*(this.r.getY() + this.s.getY());

			this.index = 6;

		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2D next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.MovePathElement2d(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.add(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.add(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.sub(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.sub(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2D.LinePathElement2d(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return new AbstractPathElement2D.ClosePathElement2d(
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
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
	private class TranformPathIteratorWrapper2f implements PathIterator2f {

		private final PathIterator2f p;

		public TranformPathIteratorWrapper2f(PathIterator2f p1){
			this.p = p1;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.p.hasNext();
		}

		@Override
		public AbstractPathElement2F next() {
			AbstractPathElement2F elem = this.p.next();
			switch(elem.type){
			case CURVE_TO:
				return new AbstractPathElement2F.CurvePathElement2f(
						(elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getCtrlX2()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getCtrlY2()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getCtrlY2()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getCtrlX2()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
			case LINE_TO:
				return new AbstractPathElement2F.LinePathElement2f(
						(elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
			case MOVE_TO:
				return new AbstractPathElement2F.MovePathElement2f((elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
			case QUAD_TO:
				return new AbstractPathElement2F.QuadPathElement2f(
						(elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return this.p.getWindingRule();
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return this.p.isPolyline();
		}

	}

	/**
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TranformPathIteratorWrapper2d implements PathIterator2d {

		private final PathIterator2d p;

		public TranformPathIteratorWrapper2d(PathIterator2d p1){
			this.p = p1;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.p.hasNext();
		}

		@Override
		public AbstractPathElement2D next() {
			AbstractPathElement2D elem = this.p.next();
			switch(elem.type){
			case CURVE_TO:
				return new AbstractPathElement2D.CurvePathElement2d(
						(elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getCtrlX2()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getCtrlY2()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getCtrlY2()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getCtrlX2()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
			case LINE_TO:
				return new AbstractPathElement2D.LinePathElement2d(
						(elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
			case MOVE_TO:
				return new AbstractPathElement2D.MovePathElement2d((elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
			case QUAD_TO:
				return new AbstractPathElement2D.QuadPathElement2d(
						(elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getFromY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getFromX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getCtrlY1()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getCtrlX1()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY(),
						(elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getSecondAxisY() - (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getSecondAxisX(), (elem.getToY()-AbstractOrientedRectangle2F.this.getCenterY()) * AbstractOrientedRectangle2F.this.getFirstAxisX() - (elem.getToX()-AbstractOrientedRectangle2F.this.getCenterX()) * AbstractOrientedRectangle2F.this.getFirstAxisY());
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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return this.p.getWindingRule();
		}

		@Pure
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
	static class PointIterator implements Iterator<Point2f> {

		private final PathIterator2f iterator;
		private final Deque<Point2f> points = new LinkedList<>();

		/**
		 * @param iterator1
		 */
		public PointIterator(PathIterator2f iterator1) {
			this.iterator = iterator1;
			searchCandidates();
		}

		private void searchCandidates() {
			while (this.points.isEmpty() && this.iterator.hasNext()) {
				AbstractPathElement2F element = this.iterator.next();
				switch (element.getType()) {
				case MOVE_TO:
				case LINE_TO:
					this.points.push(new Point2f(element.getToX(), element.getToY()));
					break;
				case CURVE_TO:
				{
					Path2f p1 = new Path2f();
					p1.moveTo(element.getFromX(), element.getFromY());
					p1.curveTo(
							element.getCtrlX1(), element.getCtrlY1(),
							element.getCtrlX2(), element.getCtrlY2(),
							element.getToX(), element.getToY());
					PathIterator2f ii1 = p1.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
					while (ii1.hasNext()) {
						AbstractPathElement2F elt = ii1.next();
						switch (elt.getType()) {
						case MOVE_TO:
						case LINE_TO:
							this.points.push(new Point2f(element.getToX(), element.getToY()));
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
					p1.moveTo(element.getFromX(), element.getFromY());
					p1.quadTo(
							element.getCtrlX1(), element.getCtrlY1(),
							element.getToX(), element.getToY());
					PathIterator2f ii1 = p1.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
					while (ii1.hasNext()) {
						AbstractPathElement2F elt = ii1.next();
						switch (elt.getType()) {
						case MOVE_TO:
						case LINE_TO:
							this.points.push(new Point2f(element.getToX(), element.getToY()));
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

		@Pure
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

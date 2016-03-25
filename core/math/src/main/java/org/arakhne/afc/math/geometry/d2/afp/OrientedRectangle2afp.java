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
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.fp.Point2fp;
import org.arakhne.afc.math.geometry.d2.fp.Vector2fp;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D oriented rectangle on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
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

	/**
	 * Compute the oriented bounding box axis.
	 * 
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the vector where the R axis of the OBR is put
	 * @param S is the vector where the S axis of the OBR is put
	 * @see "MGPCG pages 219-221"
	 */
	static void computeOBRAxis(Iterable<? extends Point2D> points, Vector2D R, Vector2D S) {
		// Determining the covariance matrix of the points
		// and set the center of the box
		Matrix2f cov = new Matrix2f();
		cov.cov(R, points);

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
	static void computeOBRCenterExtents(
			Iterable<? extends Point2D> points,
			Vector2D R, Vector2D S,
			Point2D center, Tuple2D<?> extents) {
		assert(points!=null);
		assert(center!=null);
		assert(extents!=null);

		double minR = Double.POSITIVE_INFINITY;
		double maxR = Double.NEGATIVE_INFINITY;
		double minS = Double.POSITIVE_INFINITY;
		double maxS = Double.NEGATIVE_INFINITY;

		double PdotR;
		double PdotS;
		for(Point2D tuple : points) {
			PdotR = Vector2D.dotProduct(tuple.getX(), tuple.getY(), R.getX(), R.getY());
			PdotS = Vector2D.dotProduct(tuple.getX(), tuple.getY(), S.getX(), S.getY());

			if (PdotR < minR) minR = PdotR;			
			if (PdotR > maxR) maxR = PdotR;			
			if (PdotS < minS) minS = PdotS;			
			if (PdotS > maxS) maxS = PdotS;			
		}

		double a = (maxR + minR) / 2.;
		double b = (maxS + minS) / 2.;

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
	static boolean intersectsOrientedRectangleSegment(
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

		return Rectangle2afp.intersectsRectangleSegment(
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
	static boolean intersectsOrientedRectangleEllipse(
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
	static boolean intersectsOrientedRectangleSolidCircle(
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			double circleX, double circleY, double circleRadius) {
		// Find points on OBR closest and farest to sphere center

		// Create instances of "fp" points since they are used internally.
		Point2D closest = new Point2fp();
		Point2D farthest = new Point2fp();

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
				Point2D.getDistanceSquaredPointPoint(
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
	static boolean intersectsOrientedRectangleOrientedRectangle(
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
		if (Math.abs(Vector2D.dotProduct(tx, ty, obr1Axis1X, obr1Axis1Y)) 
				> obr1Axis1Extent
				+ Math.abs(Vector2D.dotProduct(scaledObr2Axis1X, scaledObr2Axis1Y, obr1Axis1X, obr1Axis1Y)) 
				+ Math.abs(Vector2D.dotProduct(scaledObr2Axis2X, scaledObr2Axis2Y, obr1Axis1X, obr1Axis1Y))) {
			return false;
		}

		//L = Ay
		if (Math.abs(Vector2D.dotProduct(tx, ty, obr1Axis2X, obr1Axis2Y))
				> obr1Axis2Extent
				+ Math.abs(Vector2D.dotProduct(scaledObr2Axis1X, scaledObr2Axis1Y, obr1Axis2X, obr1Axis2Y))
				+ Math.abs(Vector2D.dotProduct(scaledObr2Axis2X, scaledObr2Axis2Y, obr1Axis2X, obr1Axis2Y))) {
			return false;
		}

		//L=Bx
		if (Math.abs(Vector2D.dotProduct(tx, ty, obr2Axis1X, obr2Axis1Y))
				> obr2Axis1Extent
				+ Math.abs(Vector2D.dotProduct(scaledObr1Axis1X, scaledObr1Axis1Y, obr2Axis1X, obr2Axis1Y))
				+ Math.abs(Vector2D.dotProduct(scaledObr1Axis2X, scaledObr1Axis2Y, obr2Axis1X, obr2Axis1Y))) {
			return false;
		}

		//L=By
		if (Math.abs(Vector2D.dotProduct(tx, ty, obr2Axis2X, obr2Axis2Y))
				> obr2Axis2Extent
				+ Math.abs(Vector2D.dotProduct(scaledObr1Axis1X, scaledObr1Axis1Y, obr2Axis2X, obr2Axis2Y))
				+ Math.abs(Vector2D.dotProduct(scaledObr1Axis2X, scaledObr1Axis2Y, obr2Axis2X, obr2Axis2Y))) {
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
	static boolean intersectsOrientedRectangleRectangle(
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
	static boolean containsOrientedRectangleRectangle(
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
	static boolean containsOrientedRectanglePoint(
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
		return Rectangle2afp.containsRectanglePoint(
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
	 * @param result the closest point.
	 */
	@Pure
	static void computeClosestPoint(
			double px, double py,
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			Point2D result) {
		computeClosestFarthestPoints(
				px, py,
				obrCenterX, obrCenterY,
				obrAxis1X, obrAxis1Y,
				obrAxis1Extent,
				obrAxis2X, obrAxis2Y,
				obrAxis2Extent,
				result, null);
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
	 * @param result the farthest point.
	 */
	@Pure
	static void computeFarthestPoint(
			double px, double py,
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			Point2D result) {
		computeClosestFarthestPoints(
				px, py,
				obrCenterX, obrCenterY,
				obrAxis1X, obrAxis1Y,
				obrAxis1Extent,
				obrAxis2X, obrAxis2Y,
				obrAxis2Extent,
				null, result);
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
	static void computeClosestFarthestPoints(
			double px, double py,
			double obrCenterX, double obrCenterY,
			double obrAxis1X, double obrAxis1Y,
			double obrAxis1Extent,
			double obrAxis2X, double obrAxis2Y,
			double obrAxis2Extent,
			Point2D closest, Point2D farthest) {
		assert (obrAxis1Extent >= 0.);
		assert (obrAxis2Extent >= 0.);

		assert (MathUtil.isEpsilonZero(Vector2D.dotProduct(obrAxis1X, obrAxis1Y, obrAxis1X, obrAxis1Y)));
		assert (MathUtil.isEpsilonZero(Vector2D.dotProduct(obrAxis2X, obrAxis2Y, obrAxis2X, obrAxis2Y)));

		double dx = px - obrCenterX;
		double dy = py - obrCenterY;

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = Vector2D.dotProduct(dx, dy, obrAxis1X, obrAxis1Y);
		double d2 = Vector2D.dotProduct(dx, dy, obrAxis2X, obrAxis2Y);

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
		setFirstAxis(axis.getX(), axis.getY(), getFirstAxisExtent());
	}

	/** Set the first axis of the rectangle.
	 * The second axis is updated to be perpendicular to the new first axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	default void setFirstAxis(Vector2D axis, double extent) {
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
		setSecondAxis(axis.getX(), axis.getY(), getSecondAxisExtent());
	}

	/** Set the second axis of the rectangle.
	 * The first axis is updated to be perpendicular to the new second axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	default void setSecondAxis(Vector2D axis, double extent) {
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
	default void set(IT obr) {
		set(obr.getCenterX(), obr.getCenterY(),
				obr.getFirstAxisX(), obr.getFirstAxisY(), obr.getFirstAxisExtent(),
				obr.getSecondAxisX(), obr.getSecondAxisY(), obr.getSecondAxisExtent());
	}

	/** Set the oriented rectangle.
	 * The second axis is automatically computed.
	 *
	 * @param center is the OBR center.
	 * @param axis1 is the first axis of the OBR.
	 * @param axis1Extent is the extent of the first axis.
	 * @param axis2Extent is the extent of the second axis.
	 */
	default void set(Point2D center, Vector2D axis1, double axis1Extent, double axis2Extent) {
		set(center.getX(), center.getY(), axis1.getX(), axis1.getY(), axis1Extent, axis2Extent);
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
	default void set(double centerX, double centerY,
			double axis1X, double axis1Y, double axis1Extent,
			double axis2Extent) {
		assert (MathUtil.isEpsilonZero(Vector2D.dotProduct(axis1X, axis1Y, axis1X, axis1Y)));
		double axis2X;
		double axis2Y;
		if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
			axis2X = axis1Y;
			axis2Y = -axis1X;
		} else {
			axis2X = -axis1Y;
			axis2Y = axis1X;
		}
		assert (MathUtil.isEpsilonZero(Vector2D.dotProduct(axis2X, axis2Y, axis2X, axis2Y)));
		set(centerX, centerY,
				axis1X, axis1Y, axis1Extent,
				axis2X, axis2X, axis2Extent);
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

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	default void setFromPointCloud(Iterable<? extends Point2D> pointCloud) {
		Vector2D r = new Vector2fp();
		Vector2D s = new Vector2fp();
		OrientedRectangle2afp.computeOBRAxis(pointCloud, r, s);
		Point2D center = new Point2fp();
		Vector2D extents = new Vector2fp();
		OrientedRectangle2afp.computeOBRCenterExtents(pointCloud, r, s, center, extents);
		set(center.getX(), center.getY(),
				r.getX(), r.getY(), extents.getX(),
				s.getX(), s.getY(), extents.getY());
	}

	/** Set the oriented rectangle from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	default void setFromPointCloud(Point2D... pointCloud) {
		setFromPointCloud(Arrays.asList(pointCloud));
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		// Only for internal usage.
		Point2D closest = new Point2fp();
		computeClosestPoint(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				closest);
		return closest.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		// Only for internal usage.
		Point2D closest = new Point2fp();
		computeClosestPoint(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				closest);
		return closest.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		// Only for internal usage.
		Point2D closest = new Point2fp();
		computeClosestPoint(
				p.getX(), p.getY(),
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				closest);
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
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				x, y);
	}
	
	@Pure
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		return containsOrientedRectangleRectangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				r.getMinX(), r.getMinY(),
				r.getWidth(), r.getHeight());
	}
	
	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		return intersectsOrientedRectangleSolidCircle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getX(),s.getY(), s.getRadius());
	}
	
	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		return intersectsOrientedRectangleEllipse(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getMinX(), s.getMinY(), s.getMaxX() - s.getMinX(), s.getMaxY() - s.getMinY());
	}
	
	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		return intersectsOrientedRectangleOrientedRectangle(
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
		return intersectsOrientedRectangleRectangle(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight());
	}
	
	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		return s.intersects(this);
	}
	
	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		return intersectsOrientedRectangleSegment(
				getCenterX(), getCenterY(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default boolean intersects(PathIterator2afp<?> iterator) {
		if (isEmpty()) {
			return false;
		}
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		LocalCoordinateSystemPathIterator<?> localIterator = new LocalCoordinateSystemPathIterator(this, iterator);
		int crossings = Path2afp.computeCrossingsFromRect(
				localIterator, 
				getFirstAxisExtent() / -2., getSecondAxisExtent() / -2.,
				getFirstAxisExtent() / 2., getSecondAxisExtent() / 2.,
				false, true);

		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		Point2D minCorner;
		Point2D maxCorner;

		minCorner = new Point2fp(getCenterX(), getCenterY());
		maxCorner = new Point2fp(getCenterX(), getCenterY());

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
		double dx = p.getX() - getCenterX();
		double dy = p.getY() - getCenterY();

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = Vector2D.dotProduct(dx, dy, getFirstAxisX(), getFirstAxisY());
		double d2 = Vector2D.dotProduct(dx, dy, getSecondAxisX(), getSecondAxisY());

		double clampedD1 = MathUtil.clamp(d1, -getFirstAxisExtent(), getFirstAxisExtent());
		double clampedD2 = MathUtil.clamp(d2, -getSecondAxisExtent(), getSecondAxisExtent());

		// Step that distance along the axis to get world coordinate
		// q1 += dist * this.axis[i]; (If distance farther than the box
		// extents, clamp to the box)
		return getGeomFactory().newPoint(
				getCenterX() + clampedD1 * getFirstAxisX() + clampedD2 * getSecondAxisX(),
				getCenterY() + clampedD1 * getFirstAxisY() + clampedD2 * getSecondAxisY());
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D p) {
		double dx = p.getX() - getCenterX();
		double dy = p.getY() - getCenterY();

		// For each OBB axis project d onto that axis to get the distance along
		// the axis of d from the box center
		double d1 = Vector2D.dotProduct(dx, dy, getFirstAxisX(), getFirstAxisY());
		double d2 = Vector2D.dotProduct(dx, dy, getSecondAxisX(), getSecondAxisY());

		// Clamp to the other side of the box
		if (d1 >= 0.)
			d1 = -getFirstAxisExtent();
		else
			d1 = getFirstAxisExtent();

		if (d2 >= 0.)
			d2 = -getSecondAxisExtent();
		else
			d2 = getFirstAxisExtent();

		// Step that distance along the axis to get world coordinate
		// q2 += dist * this.axis[i];
		return getGeomFactory().newPoint(
				getCenterX() + d1 * getFirstAxisX() + d2 * getSecondAxisX(),
				getCenterY() + d1 * getFirstAxisY() + d2 * getSecondAxisY());
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

		private final GeomFactory2afp<T, ?, ?> factory;

		/**
		 * @param factory the factory.
		 */
		public AbstractOrientedRectanglePathIterator(GeomFactory2afp<T, ?, ?> factory) {
			this.factory = factory;
		}

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.factory;
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
		
		private int index = 0;

		/**
		 * @param rectangle the oriented rectangle to iterate on.
		 */
		public OrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?> rectangle) {
			super(rectangle.getGeomFactory());
			if (rectangle.isEmpty()) {
				this.index = 6;
			} else {
				GeomFactory2afp<T, ?, ?> factory = getGeomFactory();
				this.r = factory.newVector(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
				this.s = factory.newVector(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
				this.r.scale(rectangle.getFirstAxisExtent());
				this.s.scale(rectangle.getSecondAxisExtent());
				this.x = rectangle.getCenterX() - (this.r.getX() + this.s.getX());
				this.y = rectangle.getCenterY() - (this.r.getY() + this.s.getY());
				this.r.scale(2);
				this.s.scale(2);
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public T next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return getGeomFactory().newMovePathElement(
						this.x, this.y);
			case 1:
				return getGeomFactory().newLinePathElement(
						this.x, this.y,
						this.x + this.r.getX(), this.y + this.r.getY());
			case 2:
				return getGeomFactory().newLinePathElement(
						this.x + this.r.getX(), this.y + this.r.getY(),
						this.x + this.r.getX() + this.s.getX(), this.y + this.r.getY() + this.s.getY());
			case 3:
				return getGeomFactory().newLinePathElement(
						this.x + this.r.getX() + this.s.getX(), this.y + this.r.getY() + this.s.getY(),
						this.x + this.s.getX(), this.y + this.s.getY());
			case 4:
				return getGeomFactory().newLinePathElement(
						this.x + this.s.getX(), this.y + this.s.getY(),
						this.x, this.y);
			case 5:
				return getGeomFactory().newClosePathElement(
						this.x, this.y,
						this.x, this.y);
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

		private int index = 0;

		private Point2D p1;
		
		private Point2D p2;

		/**
		 * @param rectangle the oriented rectangle to iterate on.
		 * @param transform the transformation to apply on the rectangle.
		 */
		public TransformedOrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?> rectangle, Transform2D transform) {
			super(rectangle.getGeomFactory());
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = 6;
			} else {
				GeomFactory2afp<T, ?, ?> factory = getGeomFactory();
				this.p1 = factory.newPoint();
				this.p2 = factory.newPoint();
				this.r = factory.newVector(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
				this.s = factory.newVector(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
				this.r.scale(rectangle.getFirstAxisExtent());
				this.s.scale(rectangle.getSecondAxisExtent());
				this.x = rectangle.getCenterX() - (this.r.getX() + this.s.getX());
				this.y = rectangle.getCenterY() - (this.r.getY() + this.s.getY());
				this.r.scale(2);
				this.s.scale(2);
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 5;
		}

		@Override
		public T next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x, this.y);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.add(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.add(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.sub(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.sub(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return getGeomFactory().newClosePathElement(
						this.p2.getX(), this.p2.getY(),
						this.p2.getX(), this.p2.getY());
			default:
				throw new NoSuchElementException();
			}
		}

	}

	/** An iterator that replies the path elements in the local coordinate system
	 * of an oriented rectangle.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: mgrolleau$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class LocalCoordinateSystemPathIterator<T extends PathElement2afp> extends AbstractOrientedRectanglePathIterator<T> {

		private final PathIterator2afp<? extends T> iterator;
		
		private final double centerX;

		private final double centerY;

		private final double axisX1;

		private final double axisY1;

		private final double axisX2;

		private final double axisY2;

		/**
		 * @param rectangle the basis rectangle.
		 * @param iterator the iterator to transform.
		 */
		public LocalCoordinateSystemPathIterator(OrientedRectangle2afp<?, ?, T, ?, ?> rectangle,
				PathIterator2afp<? extends T> iterator) {
			super(rectangle.getGeomFactory());
			this.iterator = iterator;
			this.centerX = rectangle.getCenterX();
			this.centerY = rectangle.getCenterY();
			this.axisX1 = rectangle.getFirstAxisX();
			this.axisY1 = rectangle.getFirstAxisY();
			this.axisX2 = rectangle.getSecondAxisX();
			this.axisY2 = rectangle.getSecondAxisY();
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
						(elem.getFromX() - this.centerX) * this.axisY2 - (elem.getFromY() - this.centerY) * this.axisX2,
						(elem.getFromY() - this.centerY) * this.axisX1 - (elem.getFromX() - this.centerX) * this.axisY1,
						(elem.getCtrlX1() - this.centerX) * this.axisY2 - (elem.getCtrlY1() - this.centerY) * this.axisX2,
						(elem.getCtrlY1() - this.centerY) * this.axisX1 - (elem.getCtrlX1() - this.centerX) * this.axisY1,
						(elem.getCtrlX2() - this.centerX) * this.axisY2 - (elem.getCtrlY2() - this.centerY) * this.axisX2,
						(elem.getCtrlY2() - this.centerY) * this.axisX1 - (elem.getCtrlX2() - this.centerX) * this.axisY1,
						(elem.getToX() - this.centerX) * this.axisY2 - (elem.getToY() - this.centerY) * this.axisX2,
						(elem.getToY() - this.centerY) * this.axisX2 - (elem.getToX() - this.centerX) * this.axisY1);
			case LINE_TO:
				return getGeomFactory().newLinePathElement(
						(elem.getFromX() - this.centerX) * this.axisY2 - (elem.getFromY() - this.centerY) * this.axisX2,
						(elem.getFromY() - this.centerY) * this.axisX1 - (elem.getFromX() - this.centerX) * this.axisY1,
						(elem.getToX() - this.centerX) * this.axisY2 - (elem.getToY() - this.centerY) * this.axisX2,
						(elem.getToY() - this.centerY) * this.axisX1 - (elem.getToX() - this.centerX) * this.axisY1);
			case MOVE_TO:
				return getGeomFactory().newMovePathElement(
						(elem.getToX() - this.centerX) * this.axisY2 - (elem.getToY() - this.centerY) * this.axisX2,
						(elem.getToY() - this.centerY) * this.axisX1 - (elem.getFromX()-this.centerX) * this.axisY1);
			case QUAD_TO:
				return getGeomFactory().newCurvePathElement(
						(elem.getFromX() - this.centerX) * this.axisY2 - (elem.getFromY() - this.centerY) * this.axisX2,
						(elem.getFromY() - this.centerY) * this.axisX1 - (elem.getFromX() - this.centerX) * this.axisY1,
						(elem.getCtrlX1() - this.centerX) * this.axisY2 - (elem.getCtrlY1() - this.centerY) * this.axisX2,
						(elem.getCtrlY1() - this.centerY) * this.axisX1 - (elem.getCtrlX1() - this.centerX) * this.axisY1,
						(elem.getToX() - this.centerX) * this.axisY2 - (elem.getToY() - this.centerY) * this.axisX2,
						(elem.getToY() - this.centerY) * this.axisX1 - (elem.getToX() - this.centerX) * this.axisY1);
			case CLOSE:
				return getGeomFactory().newClosePathElement(
						(elem.getFromX() - this.centerX) * this.axisY2 - (elem.getFromY() - this.centerY) * this.axisX2,
						(elem.getFromY() - this.centerY) * this.axisX1 - (elem.getFromX() - this.centerX) * this.axisY1,
						(elem.getToX() - this.centerX) * this.axisY2 - (elem.getToY() - this.centerY) * this.axisX2,
						(elem.getToY() - this.centerY) * this.axisX1 - (elem.getToX() - this.centerX) * this.axisY1);
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

	}

}

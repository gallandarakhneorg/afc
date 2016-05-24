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

package org.arakhne.afc.math.geometry.d3.ad;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ad.Path3ad.CrossingComputationType;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.util.Pair;

/** Fonctional interface that represented a 2D segment/line on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Segment3ad<
		ST extends Shape3ad<?, ?, IE, P, V, B>,
		IT extends Segment3ad<?, ?, IE, P, V, B>,
		IE extends PathElement3ad,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ad<?, ?, IE, P, V, B>>
		extends Shape3ad<ST, IT, IE, P, V, B> {

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double)
	 * @see Point3D#isCollinearPoints(double, double, double, double, double, double)
	 */
	@Pure
	static boolean isCollinearLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		return (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)
				&& Point3D.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3));
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are colinear, see {@link #isCollinearLines(double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean isParallelLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		return Vector3D.isCollinearVectors(x2 - x1, y2 - y1, z2 - z1, x4 - x3, y4 - y3, z4 - z3);
	}

	/**
	 * Replies the intersection point between two segments.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @param result the intersection point.
	 * @return <code>true</code> if an intersection exists.
	 */
	@Pure
	static boolean computeSegmentSegmentIntersection(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4,
			Point3D<?, ?> result) {
		assert (result != null) : "Result must be not null"; //$NON-NLS-1$
		double m = computeSegmentSegmentIntersectionFactor(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (Double.isNaN(m)) {
			return false;
		}
		result.set(x1 + m * (x2 - x1), y1 + m * (y2 - y1), z1 + m * (z2 - z1));
		return true;
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	public static double computeSegmentSegmentIntersectionFactor(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double z21 = z2 - z1;
		double x43 = x4 - x3;
		double y43 = y4 - y3;
		double z43 = z4 - z3;
		double x31 = x3 - x1;
		double y31 = y3 - y1;
		double z31 = z3 - z1;
		
		Vector3D<?, ?> v = InnerComputationGeomFactory.SINGLETON.newVector();
		
		Vector3D.crossProduct(x21, y21, z21, x43, y43, z43, v);
		
		double x = v.getX();
		double y = v.getY();
		double z = v.getZ();
		
		//If the cross product is zero then the two segments are parallels
		if(MathUtil.isEpsilonZero(x*x + y*y + z*z)){
			return Double.NaN;
		}
		
		//If the determinant det(c,a,b)=c.(a x b)!=0 then the two segment are not colinears
		if(MathUtil.isEpsilonZero(Vector3D.dotProduct(x31, y31, z31, x, y, z))){
			return Double.NaN;
		}
		
		return Vector3D.determinant(
				x31, y31, z31,
				x43, y43, z43,
				x, y, z) / (x*x + y*y + z*z);
	}
	/**
	 * Replies one position factor for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	public static Pair<Double, Double> computeSegmentSegmentIntersectionFactors(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double z21 = z2 - z1;
		double x43 = x4 - x3;
		double y43 = y4 - y3;
		double z43 = z4 - z3;
		double x31 = x3 - x1;
		double y31 = y3 - y1;
		double z31 = z3 - z1;
		
		Vector3D<?, ?> v = InnerComputationGeomFactory.SINGLETON.newVector();
		
		Vector3D.crossProduct(x21, y21, z21, x43, y43, z43, v);
		
		double x = v.getX();
		double y = v.getY();
		double z = v.getZ();
		
		//If the cross product is zero then the two segments are parallels
		if(MathUtil.isEpsilonZero(x*x + y*y + z*z)){
			return null;
		}
		
		//If the determinant det(c,a,b)=c.(a x b)!=0 then the two segment are not colinears
		if(MathUtil.isEpsilonZero(Vector3D.dotProduct(x31, y31, z31, x, y, z))){
			return null;
		}
		
		double factor1 = Vector3D.determinant(
				x31, y31, z31,
				x43, y43, z43,
				x, y, z) / (x*x + y*y + z*z);
		
		double factor2 = Vector3D.determinant(
				x31, y31, z31,
				x21, y21, z21,
				x, y, z) / (x*x + y*y + z*z);
		
		return new Pair<>(Double.valueOf(factor1), Double.valueOf(factor2));
	}

	/**
	 * Replies the position factory for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	static double computeLineLineIntersectionFactor(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		double vx1 = x2 - x1;
		double vy1 = y2 - y1;
		double vx2 = x4 - x3;
		double vy2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		double det = Vector2D.perpProduct(vx1, vy1, vx2, vy2);
		if (det == 0.) {
			return Double.NaN;
		}

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		return Vector2D.perpProduct(vx2, vy2, x1 - x3, y1 - y3) / det;
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 * 
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @param x3 horizontal position of the first point of the line.
	 * @param y3 vertical position of the first point of the line.
	 * @param x4 horizontal position of the second point of the line.
	 * @param y4 vertical position of the second point of the line.
	 * @param result the intersection point.
	 * @return <code>true</code> if there is an intersection.
	 */
	@Pure
	static boolean computeLineLineIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4,
			Point2D<?, ?> result) {
		assert (result != null) : "Result must be not null"; //$NON-NLS-1$
		double x21 = x2 - x1;
		double x43 = x4 - x3;
		double y21 = y2 - y1;
		double y43 = y4 - y3;
		
		double denom = y43 * x21 - x43 * y21;
		if (denom == 0.) {
			return false;
		}
		double x13 = x1 - x3;
		double y13 = y1 - y3;
		double intersectionFactor1 = x43 * y13 - y43 * x13;
		double intersectionFactor2 = x21 * y13 - y21 * x13;
		if (intersectionFactor1 == intersectionFactor2) {
			return false;
		}
		intersectionFactor1 = intersectionFactor1 / denom;
		result.set(
				x1 + intersectionFactor1 * x21,
				y1 + intersectionFactor1 * y21);
		return true;
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 x position of the first point of the line.
	 * @param y1 y position of the first point of the line.
	 * @param z1 z position of the first point of the line.
	 * @param x2 x position of the second point of the line.
	 * @param y2 y position of the second point of the line.
	 * @param z2 z position of the second point of the line.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #computeDistanceLinePoint(double, double, double, double, double, double)
	 */
	@Pure
	// TODO : correct formula
	public static double computeDistanceSquaredLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py, double pz) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double z21 = z2 - z1;
		double denomenator = x21 * x21 + y21 * y21 + z21 * z21;
		if (denomenator == 0.) {
			return Point3D.getDistanceSquaredPointPoint(px, py, pz, x1, y1, z1);
		}
		double s = ((y1 - py) * x21 - (x1 - px) * y21 ) / denomenator;
		return (s * s) * Math.abs(denomenator);
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param x1 x position of the first point of the segment.
	 * @param y1 y position of the first point of the segment.
	 * @param z1 z position of the first point of the segment.
	 * @param x2 x position of the second point of the segment.
	 * @param y2 y position of the second point of the segment.
	 * @param z2 y position of the second point of the segment.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	static double computeDistanceSquaredSegmentPoint(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py, double pz) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double z21 = z2 - z1;
		double denomenator = x21 * x21 + y21 * y21 + z21 * z21;
		if (denomenator == 0.) {
			return Point3D.getDistanceSquaredPointPoint(px, py, pz, x1, y1, z1);
		}
		
		double xp1 = px - x1;
		double yp1 = py - y1;
		double zp1 = pz - z1;
		double numerator = xp1 * x21 + yp1 * y21 + zp1 * z21;
		double ratio = numerator / denomenator;

		if (ratio <= 0.) {
			return Math.abs(xp1 * xp1 + yp1 * yp1 + zp1 * zp1);
		}

		if (ratio >= 1.) {
			double xp2 = px - x2;
			double yp2 = py - y2;
			double zp2 = pz - z2;
			return Math.abs(xp2 * xp2 + yp2 * yp2 + zp2 *zp2);
		}

		double factor =  (xp1 * y21 - yp1 * x21 ) / denomenator;		// TODO : check formula
		return (factor * factor) * Math.abs(denomenator);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	static double computeDistanceSegmentPoint(double x1, double y1, double x2, double y2, double px, double py) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;

		double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistancePointPoint(px, py, x1, y1);
		}

		double xp1 = px - x1;
		double yp1 = py - y1;
		double numerator = (xp1) * x21 + (yp1) * y21;
		double ratio = numerator / denomenator;
		
		if (ratio <= 0.) {
			return Math.sqrt(xp1 * xp1 + yp1 * yp1);
		}

		if (ratio >= 1.) {
			double xp2 = px - x2;
			double yp2 = py - y2;
			return Math.sqrt(xp2 * xp2 + yp2 * yp2);
		}

		double factor = (xp1 * y21 - yp1 * x21 ) / denomenator;
		return Math.abs(factor) * Math.sqrt(denomenator);
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #computeDistanceSquaredLinePoint(double, double, double, double, double, double)
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double)
	 */
	@Pure
	static double computeDistanceLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistancePointPoint(px, py, x1, y1);
		}
		double factor = ((y1 - py)* x21 - (x1 - px) * y21 ) / denomenator;
		return Math.abs(factor) * Math.sqrt(denomenator);
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 horizontal location of the first-segment begining.
	 * @param y1 vertical location of the first-segment ending.
	 * @param x2 horizontal location of the second-segment begining.
	 * @param y2 vertical location of the second-segment ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	@Pure
	static boolean isPointClosedToSegment( double x1, double y1, 
			double x2, double y2, 
			double x, double y, double hitDistance ) {
		assert (hitDistance >= 0.) : "Hit distance must be positive or zero"; //$NON-NLS-1$
		return ( computeDistanceSegmentPoint(x1, y1, x2, y2, x, y) < hitDistance );
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 horizontal location of the first-line begining.
	 * @param y1 vertical location of the first-line ending.
	 * @param x2 horizontal location of the second-line begining.
	 * @param y2 vertical location of the second-line ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	@Pure
	static boolean isPointClosedToLine( double x1, double y1, 
			double x2, double y2, 
			double x, double y, double hitDistance ) {
		assert (hitDistance >= 0.) : "Hit distance must be positive or zero"; //$NON-NLS-1$
		return ( computeDistanceLinePoint(x1, y1, x2, y2, x, y) < hitDistance );
	}

	/**
	 * Replies the projection a point on a segment.
	 * 
	 * @param px
	 *            is the coordiante of the point to project
	 * @param py
	 *            is the coordiante of the point to project
	 * @param pz
	 *            is the coordiante of the point to project
	 * @param s1x
	 *            is the x-coordinate of the first line point.
	 * @param s1y
	 *            is the y-coordinate of the first line point.
	 * @param s1z
	 *            is the z-coordinate of the first line point.
	 * @param s2x
	 *            is the x-coordinate of the second line point.
	 * @param s2y
	 *            is the y-coordinate of the second line point.
	 * @param s2z
	 *            is the z-coordinate of the second line point.
	 * @return the projection of the specified point on the line. If 
	 * equal to {@code 0}, the projection is equal to the first segment point. 
	 * If equal to {@code 1}, the projection is equal to the second segment point. 
	 * If inside {@code ]0;1[}, the projection is between the two segment points. 
	 * If inside {@code ]-inf;0[}, the projection is outside on the side of the 
	 * first segment point. If inside {@code ]1;+inf[}, the projection is 
	 * outside on the side of the second segment point.
	 */
	@Pure
	static double computeProjectedPointOnLine(double px, double py, double pz, double s1x, double s1y, double s1z, double s2x, double s2y, double s2z) {
		double vx = s2x - s1x;
		double vy = s2y - s1y;
		double vz = s2z - s1z;
		double numerator = (px - s1x) * vx + (py - s1y) * vy + (pz - s1z) * vz;
		double denomenator = vx * vx + vy * vy + vz * vz;
		return numerator / denomenator;
	}

	/**
	 * Replies the relative distance from the given point to the given line.
	 * The replied distance may be negative, depending on which side of 
	 * the line the point is.
	 * 
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @return the positive or negative distance from the point to the line
	 * @see #ccw(double, double, double, double, double, double, double)
	 * @see #computeSideLinePoint(double, double, double, double, double, double, double)
	 */
	@Pure
	static double computeRelativeDistanceLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistancePointPoint(px, py, x1, y1);
		}
		double factor = ((y1 - py) * x21 -(x1 - px) * y21) / denomenator;
		return factor * Math.sqrt(denomenator);
	}

	/**
	 * Replies on which side of a line the given point is located.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the direction
	 * that takes the positive X axis towards the negative Y axis. In the default
	 * coordinate system used by Java 2D, this direction is counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the direction that takes the positive X axis towards the positive Y axis. In the default coordinate system, this direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line segment. Note that an indicator value of 0 is rare and not useful for determining colinearity because of floating point rounding issues.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 * <p>
	 * In opposite of {@link #ccw(double, double, double, double, double, double, double)},
	 * this function does not try to classify the point if it is colinear
	 * to the segment. If the point is colinear, O is always returns. 
	 * 
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param epsilon approximate epsilon.
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double)
	 * @see MathUtil#isEpsilonZero(double)
	 * @see #ccw(double, double, double, double, double, double, double)
	 */
	@Pure
	static int computeSideLinePoint(double x1, double y1, double x2, double y2, double px, double py, double epsilon) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double xp1 = px - x1;
		double yp1 = py - y1;
		double side = xp1 * y21 - yp1 * x21;
		if (side != 0f && MathUtil.isEpsilonZero(side, epsilon)) {
			side = 0f;
		}
		return (side < 0) ? -1 : ((side > 0) ? 1 : 0);
	}

	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where 
	 * the specified point {@code (px,py)} lies with respect to the line segment from {@code (x1,y1)}
	 *  to {@code (x2,y2)}. The return value can be either 1, -1, or 0 and indicates in which 
	 *  direction the specified line must pivot around its first end point, {@code (x1,y1)}, in 
	 *  order to point at the specified point {@code (px,py)}.
	 * In other words, given three point P1, P2, and P, is the segments (P1-P2-P) a counterclockwise turn?
	 * <p>
	 * In opposite to {@link #computeSideLinePoint(double, double, double, double, double, double, double)},
	 * this function tries to classifies the point if it is colinear to the segment.
	 * The classification is explained below.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the direction that takes the 
	 * positive X axis towards the negative Y axis. In the default coordinate system used by Java 2D, 
	 * this direction is counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the direction that takes the 
	 * positive X axis towards the positive Y axis. In the default coordinate system, this 
	 * direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line segment. 
	 * Note that an indicator value of 0 is rare and not useful for determining colinearity 
	 * because of floating point rounding issues.
	 * <p>
	 * If the point is colinear with the line segment, but not between the end points, then the value will be 
	 * -1 if the point lies "beyond {@code (x1,y1)}" or 1 if the point lies "beyond {@code (x2,y2)}".
	 * 
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param epsilon approximation of the tests for equality to zero.
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double)
	 * @see #computeSideLinePoint(double, double, double, double, double, double, double)
	 */
	@Pure
	static int ccw(double x1, double y1, double x2, double y2, double px, double py, double epsilon) {
		double x21 = x2 - x1;
		double y21 = y2 - y1;
		double xp1 = px - x1;
		double yp1 = py - y1;
		double ccw = xp1 * y21 - yp1 * x21;
		if (MathUtil.isEpsilonZero(ccw, epsilon)) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = xp1 * x21 + yp1 * y21;
			if (ccw > 0.) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				xp1 -= x21;
				yp1 -= y21;
				ccw = xp1 * x21 + yp1 * y21;
				if (ccw < 0) {
					ccw = 0.;
				}
			}
		}
		return (ccw < 0.) ? -1 : ((ccw > 0.) ? 1 : 0);
	}

	/** Compute the interpolate point between the two points.
	 * 
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @param p2x
	 * @param p2y
	 * @param p2z
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @param result the interpolate point.
	 */
	@Pure
	static void interpolate(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double factor, Point3D<?, ?> result) {
		assert (result != null) : "Result must be not null"; //$NON-NLS-1$
		assert (factor >= 0. && factor <= 1.) : "Factor must be in [0;1]"; //$NON-NLS-1$
		double vx = p2x - p1x;
		double vy = p2y - p1y;
		double vz = p2z - p1z;
		result.set(
				p1x + factor * vx,
				p1y + factor * vy,
				p1z + factor * vz);
	}

	/** Replies the point on the segment that is farthest to the given point.
	 * 
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param az is the z coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param bz is the z coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param pz is the z coordinate of the point.
	 * @param result the farthest point on the shape.
	 */
	@Pure
	static void computeFarthestPointTo(
			double ax, double ay, double az, double bx, double by, double bz, double px, double py, double pz, Point3D<?, ?> result) {
		assert (result != null) : "Result must be not null"; //$NON-NLS-1$
		double xpa = px - ax;
		double ypa = py - ay;
		double zpa = pz - az;
		double xpb = px - bx;
		double ypb = py - by;
		double zpb = pz - bz;
		if ((xpa * xpa + ypa * ypa + zpa * zpa) >= (xpb * xpb + ypb * ypb + zpb *zpb)) {
			result.set(ax, ay, az);
		} else {
			result.set(bx, by, bz);
		}
	}

	/** Replies the point on the segment that is closest to the given point.
	 * 
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param az is the z coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param bz is the z coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param pz is the z coordinate of the point.
	 * @param result the is point on the shape.
	 */
	@Pure
	static void computeClosestPointTo(
			double ax, double ay, double az, double bx, double by, double bz, double px, double py, double pz,
			Point3D<?, ?> result) {
		assert (result != null) : "Result must be not null"; //$NON-NLS-1$
		double ratio = Segment3ad.computeProjectedPointOnLine(px, py, pz, ax, ay, az, bx, by, bz);
		if (ratio <= 0.) {
			result.set(ax, ay, az);
		} else if (ratio >= 1.) {
			result.set(bx, by, bz);
		} else {
			result.set(
					ax + (bx - ax) * ratio,
					ay + (by - ay) * ratio,
					az + (bz - az) * ratio); 
		}
	}

	/**
	 * Calculates the number of times the line from (x0,y0,z0) to (x1,y1,z1)
	 * crosses the ray extending to the right from (px,py,pz).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 * <p>
	 * This function differs from {@link #computeCrossingsFromPointWithoutEquality(double, double, double, double, double, double)}.
	 * The equality test is used in this function.
	 * 
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param z1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	@Pure
	static int computeCrossingsFromPoint(
			double px, double py, double pz,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py >= y0 && py >= y1) {
			return 0;
		}
		// assert(y0 != y1);
		if (px >= x0 && px >= x1) {
			return 0;
		}
		if (px <  x0 && px <  x1) {
			return (y0 < y1) ? 1 : -1;
		}
		double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px >= xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 * <p>
	 * This function differs from {@link #computeCrossingsFromPoint(double, double, double, double, double, double)}.
	 * The equality test is not used in this function.
	 * 
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	@Pure
	static int computeCrossingsFromPointWithoutEquality(
			double px, double py,
			double x0, double y0,
			double x1, double y1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py > y0 && py > y1) {
			return 0;
		}
		// assert(y0 != y1);
		if (px > x0 && px > x1) {
			return 0;
		}
		if (px < x0 && px < x1) {
			return (y0 < y1) ? 1 : -1;
		}
		double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px > xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0,z0) to (x1,y1,z1)
	 * crosses the segment (sx0,sy0,sz0) to (sx1,sy1,sz1) extending to the right.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param sx1 is the first point of the segment to extend.
	 * @param sy1 is the first point of the segment to extend.
	 * @param sz1 is the first point of the segment to extend.
	 * @param sx2 is the second point of the segment to extend.
	 * @param sy2 is the second point of the segment to extend.
	 * @param sz2 is the second point of the segment to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the second point of the line.
	 * @param z1 is the second point of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	@Pure
	static int computeCrossingsFromSegment(
			int crossings,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
//		int numCrosses = crossings;
//
//		double xmin = Math.min(sx1, sx2);
//		double xmax = Math.max(sx1, sx2);
//		double ymin = Math.min(sy1, sy2);
//		double ymax = Math.max(sy1, sy2);
//
//		if (y0 <= ymin && y1 <= ymin) {
//			return numCrosses;
//		}
//		if (y0 >= ymax && y1 >= ymax) {
//			return numCrosses;
//		}
//		if (x0 <= xmin && x1 <= xmin) {
//			return numCrosses;
//		}
//		if (x0 >= xmax && x1 >= xmax) {
//			// The line is entirely at the right of the shadow
//			if (y0 < y1) {
//				if (y0 <= ymin) {
//					++numCrosses;
//				}
//				if (y1 >= ymax) {
//					++numCrosses;
//				}
//			}
//			else {
//				if (y1 <= ymin) {
//					--numCrosses;
//				}
//				if (y0 >= ymax) {
//					--numCrosses;
//				}
//			}
//		}
//		else if (intersectsSegmentSegmentWithEnds(x0, y0, x1, y1, sx1, sy1, sx2, sy2)) {
//			return MathConstants.SHAPE_INTERSECTS;
//		}
//		else {
//			int side1, side2;
//			if (sy1 <= sy2) {
//				side1 = computeSideLinePoint(sx1, sy1, sx2, sy2, x0, y0, 0.);
//				side2 = computeSideLinePoint(sx1, sy1, sx2, sy2, x1, y1, 0.);
//			}
//			else {
//				side1 = computeSideLinePoint(sx2, sy2, sx1, sy1, x0, y0, 0.);
//				side2 = computeSideLinePoint(sx2, sy2, sx1, sy1, x1, y1, 0.);
//			}
//			if (side1 > 0 || side2 > 0) {
//				int n1 = computeCrossingsFromPoint(sx1, sy1, x0, y0, x1, y1);
//				int n2;
//				if (n1 != 0) {
//					n2 = computeCrossingsFromPointWithoutEquality(sx2, sy2, x0, y0, x1, y1);
//				}
//				else {
//					n2 = computeCrossingsFromPoint(sx2, sy2, x0, y0, x1, y1);
//				}
//				numCrosses += n1;
//				numCrosses += n2;
//			}
//		}
//
//		return numCrosses;
		return -1;
	}

//	/**
//	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
//	 * crosses the ellipse (ex0,ey0) to (ex1,ey1) extending to the right.
//	 * 
//	 * @param crossings is the initial value for the number of crossings.
//	 * @param ex is the first corner of the ellipse to extend.
//	 * @param ey is the first corner of the ellipse to extend.
//	 * @param ew is the width of the ellipse to extend.
//	 * @param eh is the height of the ellipse  to extend.
//	 * @param x0 is the first point of the line.
//	 * @param y0 is the first point of the line.
//	 * @param x1 is the second point of the line.
//	 * @param y1 is the secondpoint of the line.
//	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
//	 */
//	@Pure
//	static int computeCrossingsFromEllipse(
//			int crossings,
//			double ex, double ey,
//			double ew, double eh,
//			double x0, double y0,
//			double x1, double y1) {
//		assert (ew >= 0.) : "Ellipse width must be positive or zero"; //$NON-NLS-1$
//		assert (eh >= 0) : "Ellipse height must be positive or zero"; //$NON-NLS-1$
//		int numCrosses = crossings;
//
//		double xmin = ex;
//		double ymin = ey;
//		double xmax = ex + ew;
//		double ymax = ey + eh;
//
//		if (y0<=ymin && y1<=ymin) return numCrosses;
//		if (y0>=ymax && y1>=ymax) return numCrosses;
//		if (x0<=xmin && x1<=xmin) return numCrosses;
//
//		if (x0>=xmax && x1>=xmax) {
//			// The line is entirely at the right of the shadow
//			if (y0<y1) {
//				if (y0<=ymin) ++numCrosses;
//				if (y1>=ymax) ++numCrosses;
//			}
//			else {
//				if (y1<=ymin) --numCrosses;
//				if (y0>=ymax) --numCrosses;
//			}
//		}
//		else if (Ellipse3ad.intersectsEllipseSegment(
//				xmin, ymin, xmax-xmin, ymax-ymin,
//				x0, y0, x1, y1, true)) {
//			return MathConstants.SHAPE_INTERSECTS;
//		}
//		else {
//			double xcenter = (xmin+xmax)/2f;
//			numCrosses += computeCrossingsFromPoint(xcenter, ymin, x0, y0, x1, y1);
//			numCrosses += computeCrossingsFromPoint(xcenter, ymax, x0, y0, x1, y1);
//		}
//
//		return numCrosses;
//	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the circle (cx,cy) with radius extending to the right.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param cx is the center of the circle to extend.
	 * @param cy is the center of the circle to extend.
	 * @param cz is the center of the circle to extend.
	 * @param radius is the radius of the circle to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param z1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	static int computeCrossingsFromCircle(
			int crossings,
			double cx, double cy, double cz,
			double radius,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		assert (radius >= 0.) : "Circle radius must be positive or zero"; //$NON-NLS-1$
//		int numCrosses = crossings;
//
//		double xmin = cx - Math.abs(radius);
//		double ymin = cy - Math.abs(radius);
//		double ymax = cy + Math.abs(radius);
//
//		if (y0<=ymin && y1<=ymin) return numCrosses;
//		if (y0>=ymax && y1>=ymax) return numCrosses;
//		if (x0<=xmin && x1<=xmin) return numCrosses;
//
//		if (x0>=cx+radius && x1>=cx+radius) {
//			// The line is entirely at the right of the shadow
//			if (y0<y1) {
//				if (y0<=ymin) ++numCrosses;
//				if (y1>=ymax) ++numCrosses;
//			}
//			else {
//				if (y1<=ymin) --numCrosses;
//				if (y0>=ymax) --numCrosses;
//			}
//		}
//		else if (Sphere3ad.intersectsCircleSegment(
//				cx, cy, radius,
//				x0, y0, x1, y1)) {
//			return MathConstants.SHAPE_INTERSECTS;
//		} else {
//			numCrosses += computeCrossingsFromPoint(cx, ymin, x0, y0, x1, y1);
//			numCrosses += computeCrossingsFromPoint(cx, ymax, x0, y0, x1, y1);
//		}
//		
//		return numCrosses;
		return -1;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the rectangle.  See the comment
	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rzmin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param rzmax is the second corner of the rectangle.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param z1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	static int computeCrossingsFromRect(
			int crossings,
			double rxmin, double rymin, double rzmin,
			double rxmax, double rymax, double rzmax,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		assert (rxmin <= rxmax) : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert (rymin <= rymax) : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
		assert (rzmin <= rzmax) : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
//		int numCrosses = crossings;
//
//		if (y0 >= rymax && y1 >= rymax) return numCrosses;
//		if (y0 <= rymin && y1 <= rymin) return numCrosses;
//		if (x0 <= rxmin && x1 <= rxmin) return numCrosses;
//		if (x0 >= rxmax && x1 >= rxmax) {
//			// Line is entirely to the right of the rect
//			// and the vertical ranges of the two overlap by a non-empty amount
//			// Thus, this line segment is partially in the "right-shadow"
//			// Path may have done a complete crossing
//			// Or path may have entered or exited the right-shadow
//			if (y0 < y1) {
//				// y-increasing line segment...
//				// We know that y0 < rymax and y1 > rymin
//				if (y0 <= rymin) ++numCrosses;
//				if (y1 >= rymax) ++numCrosses;
//			}
//			else if (y1 < y0) {
//				// y-decreasing line segment...
//				// We know that y1 < rymax and y0 > rymin
//				if (y1 <= rymin) --numCrosses;
//				if (y0 >= rymax) --numCrosses;
//			}
//			return numCrosses;
//		}
//		// Remaining case:
//		// Both x and y ranges overlap by a non-empty amount
//		// First do trivial INTERSECTS rejection of the cases
//		// where one of the endpoints is inside the rectangle.
//		if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
//				(x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
//			return MathConstants.SHAPE_INTERSECTS;
//		}
//		// Otherwise calculate the y intercepts and see where
//		// they fall with respect to the rectangle
//		double xi0 = x0;
//		if (y0 < rymin) {
//			xi0 += ((rymin - y0) * (x1 - x0) / (y1 - y0));
//		}
//		else if (y0 > rymax) {
//			xi0 += ((rymax - y0) * (x1 - x0) / (y1 - y0));
//		}
//		double xi1 = x1;
//		if (y1 < rymin) {
//			xi1 += ((rymin - y1) * (x0 - x1) / (y0 - y1));
//		}
//		else if (y1 > rymax) {
//			xi1 += ((rymax - y1) * (x0 - x1) / (y0 - y1));
//		}
//		if (xi0 <= rxmin && xi1 <= rxmin) return numCrosses;
//		if (xi0 >= rxmax && xi1 >= rxmax) {
//			if (y0 < y1) {
//				// y-increasing line segment...
//				// We know that y0 < rymax and y1 > rymin
//				if (y0 <= rymin) ++numCrosses;
//				if (y1 >= rymax) ++numCrosses;
//			}
//			else if (y1 < y0) {
//				// y-decreasing line segment...
//				// We know that y1 < rymax and y0 > rymin
//				if (y1 <= rymin) --numCrosses;
//				if (y0 >= rymax) --numCrosses;
//			}
//			return numCrosses;
//		}
//		return MathConstants.SHAPE_INTERSECTS;
		return -1;
	}

//	/**
//	 * Accumulate the number of times the line crosses the shadow
//	 * extending to the right of the triangle.  See the comment
//	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
//	 * 
//	 * @param crossings is the initial value for the number of crossings.
//	 * @param tx1 is the first point of the triangle.
//	 * @param ty1 is the first point of the triangle.
//	 * @param tx2 is the second point of the triangle.
//	 * @param ty2 is the second point of the triangle.
//	 * @param tx3 is the third point of the triangle.
//	 * @param ty3 is the third point of the triangle.
//	 * @param x0 is the first point of the line.
//	 * @param y0 is the first point of the line.
//	 * @param x1 is the second point of the line.
//	 * @param y1 is the secondpoint of the line.
//	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
//	 */
//	@Pure
//	static int computeCrossingsFromTriangle(
//			int crossings,
//			double tx1, double ty1,
//			double tx2, double ty2,
//			double tx3, double ty3,
//			double x0, double y0,
//			double x1, double y1) {
//		int numCrosses = crossings;
//		
//		double xmin = tx1;
//		double xmax = tx1;
//		double ymin = ty1;
//		double ymax = ty1;
//		double x4ymin = tx1;
//		double x4ymax = tx1;
//		
//		if (tx2 < xmin) {
//			xmin = tx2;
//		}
//		if (tx2 > xmax) {
//			xmax = tx2;
//		}
//		if (ty2 == ymin) {
//			x4ymin = Math.max(tx2, x4ymin);
//		} else if (ty2 < ymin) {
//			ymin = ty2;
//			x4ymin = tx2;
//		}
//		if (ty2 == ymax) {
//			x4ymax = Math.max(tx2, x4ymax);
//		} else if (ty2 > ymax) {
//			ymax = ty2;
//			x4ymax = tx2;
//		}
//
//		if (tx3 < xmin) {
//			xmin = tx3;
//		}
//		if (tx3 > xmax) {
//			xmax = tx3;
//		}
//		if (ty3 == ymin) {
//			x4ymin = Math.max(tx3, x4ymin);
//		} else if (ty3 < ymin) {
//			ymin = ty3;
//			x4ymin = tx3;
//		}
//		if (ty3 == ymax) {
//			x4ymax = Math.max(tx3, x4ymax);
//		} else if (ty3 > ymax) {
//			ymax = ty3;
//			x4ymax = tx3;
//		}
//
//		if (y0 <= ymin && y1 <= ymin) {
//			return numCrosses;
//		}
//		if (y0 >= ymax && y1 >= ymax) {
//			return numCrosses;
//		}
//		if (x0 <= xmin && x1 <= xmin) {
//			return numCrosses;
//		}
//
//		if (x0 >= xmax && x1 >= xmax) {
//			// The line is entirely at the right of the shadow
//			if (y0<y1) {
//				if (y0<=ymin) ++numCrosses;
//				if (y1>=ymax) ++numCrosses;
//			}
//			else {
//				if (y1<=ymin) --numCrosses;
//				if (y0>=ymax) --numCrosses;
//			}
//		}
//		else if (Triangle3ad.intersectsTriangleSegment(
//				tx1, ty1, tx2, ty2, tx3, ty3,
//				x0, y0, x1, y1)) {
//			return MathConstants.SHAPE_INTERSECTS;
//		} else {
//			numCrosses += computeCrossingsFromPoint(x4ymin, ymin, x0, y0, x1, y1);
//			numCrosses += computeCrossingsFromPoint(x4ymax, ymax, x0, y0, x1, y1);
//		}
//		
//		return numCrosses;
//	}

//	/**
//	 * Accumulate the number of times the line crosses the shadow
//	 * extending to the right of the round rectangle.  See the comment
//	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
//	 * 
//	 * @param crossings is the initial value for the number of crossings.
//	 * @param rxmin is the first corner of the rectangle.
//	 * @param rymin is the first corner of the rectangle.
//	 * @param rxmax is the second corner of the rectangle.
//	 * @param rymax is the second corner of the rectangle.
//	 * @param arcWidth is the width of the rectangle arcs.
//	 * @param arcHeight is the height of the rectangle arcs.
//	 * @param x0 is the first point of the line.
//	 * @param y0 is the first point of the line.
//	 * @param x1 is the second point of the line.
//	 * @param y1 is the secondpoint of the line.
//	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
//	 */
//	@Pure
//	static int computeCrossingsFromRoundRect(
//			int crossings,
//			double rxmin, double rymin,
//			double rxmax, double rymax,
//			double arcWidth, double arcHeight,
//			double x0, double y0,
//			double x1, double y1) {
//		assert (rxmin <= rxmax) : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
//		assert (rymin <= rymax) : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
//		assert (arcWidth >= 0. && arcWidth <= (rxmax - rxmin) / 2.) : "arcWidth is too big"; //$NON-NLS-1$
//		assert (arcHeight >= 0. && arcHeight <= (rymax - rymin) / 2.) : "arcHeight is too big"; //$NON-NLS-1$
//		int numCrosses = crossings;
//
//		if (y0 >= rymax && y1 >= rymax) return numCrosses;
//		if (y0 <= rymin && y1 <= rymin) return numCrosses;
//		if (x0 <= rxmin && x1 <= rxmin) return numCrosses;
//		if (x0 >= rxmax && x1 >= rxmax) {
//			// Line is entirely to the right of the rect
//			// and the vertical ranges of the two overlap by a non-empty amount
//			// Thus, this line segment is partially in the "right-shadow"
//			// Path may have done a complete crossing
//			// Or path may have entered or exited the right-shadow
//			if (y0 < y1) {
//				// y-increasing line segment...
//				// We know that y0 < rymax and y1 > rymin
//				if (y0 <= rymin) ++numCrosses;
//				if (y1 >= rymax) ++numCrosses;
//			}
//			else if (y1 < y0) {
//				// y-decreasing line segment...
//				// We know that y1 < rymax and y0 > rymin
//				if (y1 <= rymin) --numCrosses;
//				if (y0 >= rymax) --numCrosses;
//			}
//			return numCrosses;
//		}
//		// Remaining case:
//		// Both x and y ranges overlap by a non-empty amount
//		// First do trivial INTERSECTS rejection.
//		if (RoundRectangle3ad.intersectsRoundRectangleSegment(
//				rxmin, rymin, rxmax, rymax, arcWidth, arcHeight,
//				x0, y0, x1, y1)) {
//			return MathConstants.SHAPE_INTERSECTS;
//		}
//
//		double x = rxmax - arcWidth;
//		numCrosses += computeCrossingsFromPoint(x, rymin, x0, y0, x1, y1);
//		numCrosses += computeCrossingsFromPoint(x, rymax, x0, y0, x1, y1);
//
//		return numCrosses;
//	}

	/** Replies if two lines are intersecting.
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param z1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param z2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsLineLine(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		if (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)) {
			return Point3D.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		}
		return true;
	}

	/** Replies if a segment and a line are intersecting.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsSegmentLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return (computeSideLinePoint(x3, y3, x4, y4, x1, y1, Double.NaN) *
				computeSideLinePoint(x3, y3, x4, y4, x2, y2, Double.NaN)) <= 0;
	}

	/** Do an intersection test of two segments for ensuring that the answer of "no intersect" is safe.
	 * 
	 * <p>If the function replies {@link UncertainIntersection#NO}, we are sure that the two given segments are not
	 * intersecting. If the function replies {@link UncertainIntersection#PERHAPS}, the two segments may intersects
	 * (uncertain answer).
	 * 
	 * <p>This function considers that the ends of the segments are intersecting.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return the type of intersection. 
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static UncertainIntersection getNoSegmentSegmentWithEndsIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		double vx1, vy1, vx2a, vy2a, vx2b, vy2b, f1, f2, sign;

		vx1 = x2 - x1;
		vy1 = y2 - y1;

		// Based on CCW. It is different than MathUtil.ccw(); because
		// this small algorithm is computing a ccw of 0 for colinear points.
		vx2a = x3 - x1;
		vy2a = y3 - y1;
		f1 = vx2a * vy1 - vy2a * vx1;

		vx2b = x4 - x1;
		vy2b = y4 - y1;
		f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  ON SEGMENT?
		// -1   1  -1  T
		//  0  -1   0  ON SEGMENT?
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  ON SEGMENT?
		//  1  -1  -1  T
		//  1   0   0  ON SEGMENT?
		//  1   1   1  F
		sign = f1 * f2;

		if (sign<0) return UncertainIntersection.PERHAPS;
		if (sign>0) return UncertainIntersection.NO;

		double squaredLength = vx1*vx1 + vy1*vy1;

		if (f1==0f && f2==0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return UncertainIntersection.fromBoolean((f1>=0f || f2>=0) && (f1<=1f || f2<=1));
		}

		if (f1==0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return UncertainIntersection.fromBoolean(f1>=0f && f1<=1);
		}

		if (f2==0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return UncertainIntersection.fromBoolean(f2>=0 && f2<=1);
		}

		return UncertainIntersection.NO;
	}

	/** Do an intersection test of two segments for ensuring that the answer of "no intersect" is safe.
	 * If the function replies <code>true</code>, it may
	 * This function does not consider that the ends of
	 * the segments are intersecting.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return the type of intersection. 
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static UncertainIntersection getNoSegmentSegmentWithoutEndsIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		double vx1, vy1, vx2a, vy2a, vx2b, vy2b, f1, f2, sign;

		vx1 = x2 - x1;
		vy1 = y2 - y1;

		vx2a = x3 - x1;
		vy2a = y3 - y1;
		f1 = vx2a * vy1 - vy2a * vx1;

		vx2b = x4 - x1;
		vy2b = y4 - y1;
		f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  F
		// -1   1  -1  T
		//  0  -1   0  F
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  F
		//  1  -1  -1  T
		//  1   0   0  F
		//  1   1   1  F

		sign = f1 * f2;

		if (sign<0) return UncertainIntersection.PERHAPS;
		if (sign>0) return UncertainIntersection.NO;

		if (f1==0f && f2==0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			double squaredLength = vx1*vx1 + vy1*vy1;

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return UncertainIntersection.fromBoolean((f1>0f || f2>0) && (f1<1f || f2<1));
		}

		return UncertainIntersection.NO;
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are not intersecting.
	 * To include the ends of the segments in the intersection ranges, see
	 * {@link #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithoutEnds(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		UncertainIntersection r;
		r = getNoSegmentSegmentWithoutEndsIntersection(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r.booleanValue()) return r.booleanValue();
		return getNoSegmentSegmentWithoutEndsIntersection(x3, y3, x4, y4, x1, y1, x2, y2).booleanValue();
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param z3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param z4 is the second point of the second segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithEnds(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		UncertainIntersection r;
		r = getNoSegmentSegmentWithEndsIntersection(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r.booleanValue()) return r.booleanValue();
		return getNoSegmentSegmentWithEndsIntersection(x3, y3, x4, y4, x1, y1, x2, y2).booleanValue();
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
		return getX1() == shape.getX1()
			&& getY1() == shape.getY1()
			&& getZ1() == shape.getZ1()
			&& getX2() == shape.getX2()
			&& getY2() == shape.getY2()
			&& getZ2() == shape.getZ2();
	}

	@Override
	default boolean isEmpty() {
		return getX1()==getX2() && getY1()==getY2() && getZ1()==getZ2();
	}

	/** Change the line.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2 
	 */
	// No default implementation for ensuring atomic change.
	void set(double x1, double y1, double z1, double x2, double y2, double z2);

	/** Change the line.
	 * 
	 * @param a
	 * @param b
	 */
	default void set(Point3D<?, ?> a, Point3D<?, ?> b) {
		assert (a != null) : "First point must be not null"; //$NON-NLS-1$
		assert (b != null) : "Second point must be not null"; //$NON-NLS-1$
		set(a.getX(), a.getY(), a.getZ(), b.getX(), b.getY(), b.getZ());
	}
	
	@Override
	default void set(IT s) {
		assert (s != null) : "Shape must be not null"; //$NON-NLS-1$
		set(s.getX1(), s.getY1(), s.getZ1(), s.getX2(), s.getY2(), s.getZ2());
	}

	/** Sets a new value in the X of the first point.
	 * 
	 * @param x the new value double x
	 */
	void setX1(double x);
	
	/**Sets a new value in the Y of the first point.
	 * 
	 * @param y the new value double y
	 */
	void setY1(double y);

	/**Sets a new value in the Z of the first point.
	 * 
	 * @param z the new value double z
	 */
	void setZ1(double z);

	/**Sets a new value in the X of the second point.
	 * 
	 * @param x the new value double x
	 */
	void setX2(double x);
	
	/**Sets a new value in the Y of the second point.
	 * 
	 * @param y the new value double y
	 */
	void setY2(double y);

	/**Sets a new value in the Z of the second point.
	 * 
	 * @param z the new value double z
	 */
	void setZ2(double z);

	/** Replies the X of the first point.
	 * 
	 * @return the x of the first point.
	 */
	@Pure
	double getX1();
	
	/** Replies the Y of the first point.
	 * 
	 * @return the y of the first point.
	 */
	@Pure
	double getY1();

	/** Replies the Z of the first point.
	 * 
	 * @return the z of the first point.
	 */
	@Pure
	double getZ1();

	/** Replies the X of the second point.
	 * 
	 * @return the x of the second point.
	 */
	@Pure
	double getX2();
	
	/** Replies the Y of the second point.
	 * 
	 * @return the y of the second point.
	 */
	@Pure
	double getY2();

	/** Replies the Z of the second point.
	 * 
	 * @return the z of the second point.
	 */
	@Pure
	double getZ2();

	/** Replies the first point.
	 * 
	 * @return the first point.
	 */
	@Pure
	default P getP1() {
		return getGeomFactory().newPoint(getX1(), getY1(), getZ1());
	}

	/** Replies the second point.
	 * 
	 * @return the second point.
	 */
	@Pure
	default P getP2() {
		return getGeomFactory().newPoint(getX2(), getY2(), getZ2());
	}

	/** Change the first point.
	 * 
	 * @param x
	 * @param y
	 * @param z 
	 */
	default void setP1(double x, double y, double z) {
		set(x, y, z, getX2(), getY2(), getZ2());
	}

	/** Change the second point.
	 * 
	 * @param x
	 * @param y
	 * @param z 
	 */
	default void setP2(double x, double y, double z) {
		set(getX1(), getY1(), getZ1(), x, y, z);
	}

	/** Change the first point.
	 * 
	 * @param p
	 */
	default void setP1(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		set(p.getX(), p.getY(), p.getZ(), getX2(), getY2(), getZ2());
	}

	/** Change the second point.
	 * 
	 * @param p
	 */
	default void setP2(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		set(getX1(), getY1(), getZ1(), p.getX(), p.getY(), p.getZ());
	}

	/** Replies the length of the segment.
	 *
	 * @return the length.
	 */
	@Pure
	default double getLength() {
		return Point3D.getDistancePointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Replies the squared length of the segment.
	 *
	 * @return the squared length.
	 */
	@Pure
	default double getLengthSquared() {
		return Point3D.getDistanceSquaredPointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		box.setFromCorners(
				this.getX1(),
				this.getY1(),
				this.getZ1(),
				this.getX2(),
				this.getY2(),
				this.getZ2());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return computeDistanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				p.getX(), p.getY(), p.getZ());
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double ratio = computeProjectedPointOnLine(p.getX(), p.getY(), p.getZ(), getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		double vx = (getX2() - getX1()) * ratio;
		double vy = (getY2() - getY1()) * ratio;
		double vz = (getZ2() - getZ1()) * ratio;
		return Math.abs(getX1() + vx - p.getX())
			 + Math.abs(getY1() + vy - p.getY())
			 + Math.abs(getZ1() + vz - p.getZ());
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double ratio = computeProjectedPointOnLine(p.getX(), p.getY(), p.getZ(), getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		double vx = (getX2() - getX1()) * ratio;
		double vy = (getY2() - getY1()) * ratio;
		double vz = (getZ2() - getZ1()) * ratio;
		return MathUtil.max(
				Math.abs(this.getX1() + vx - p.getX()),
				Math.abs(this.getY1() + vy - p.getY()),
				Math.abs(this.getZ1() + vz - p.getZ()));
	}
	
	/** {@inheritDoc}
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 * 
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Override
	default boolean contains(double x, double y, double z) {
		return MathUtil.isEpsilonZero(
				computeDistanceSquaredSegmentPoint(
						getX1(), getY1(), getZ1(),
						getX2(), getY2(), getZ2(),
						x, y, z));
	}
	
	@Override
	default boolean contains(RectangularPrism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return (getX1() == getX2() || getY1() == getY2() || getZ1() == getZ2())
				&& contains(r.getMinX(), r.getMinY(), r.getMinZ())
				&& contains(r.getMaxX(), r.getMaxY(), r.getMaxZ());
	}
	
	@Override
	default void translate(double dx, double dy, double dz) {
		set(getX1() + dx, getY1() + dy, getZ1() + dz, getX2() + dx, getY2() + dy, getZ2() + dz);
	}
	
	/** Transform the current segment.
	 * This function changes the current segment.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	default void transform(Transform3D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point3D<?, ?> p = new InnerComputationPoint3ad(getX1(),  getY1(), getZ1());
		transform.transform(p);
		double x1 = p.getX();
		double y1 = p.getY();
		double z1 = p.getZ();
		p.set(getX2(), getY2(), getZ2());
		transform.transform(p);
		set(x1, y1, z1, p.getX(), p.getY(), p.getZ());
	}
	
	@Override
	default void clear() {
		set(0, 0, 0, 0, 0, 0);
	}

	/** Clip the segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rzmin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @param rzmax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 */
	@Pure
	// TODO
	default boolean clipToRectangle(double rxmin, double rymin, double rzmin, double rxmax, double rymax, double rzmax) {
		assert (rxmin <= rxmax) : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert (rymin <= rymax) : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
		assert (rzmin <= rzmax) : "rzmin must be lower or equal to rzmax"; //$NON-NLS-1$
		double x0 = getX1();
		double y0 = getY1();
		double z0 = getZ1();
		double x1 = getX2();
		double y1 = getY2();
		double z1 = getZ2();
		int code1 = MathUtil.getCohenSutherlandCode3D(x0, y0, z0, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		int code2 = MathUtil.getCohenSutherlandCode3D(x1, y1, z1, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		boolean accept = false;
		boolean cont = true;
		double x, y, z;
		x = y = z = 0;

		while (cont) {
			if ((code1 | code2)==0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			}
			else if ((code1 & code2)!=0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			}
			else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge

				// At least one endpoint is outside the clip rectangle; pick it.
				int code3 = code1!=0 ? code1 : code2;

				// TODO update formula & use FRONT / BACK for z coordinate
				
				// Now find the intersection point;
				// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
				if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP)!=0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				}
				else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM)!=0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				}
				else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT)!=0) { 
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				}
				else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT)!=0) {
					// point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (rxmin - x0) / (x1 - x0);
					x = rxmin;
				}
				else {
					code3 = 0;
				}

				if (code3!=0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = x;
						y0 = y;
						z0 = z;
						code1 = MathUtil.getCohenSutherlandCode3D(x0, y0, z0, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
					}
					else {
						x1 = x;
						y1 = y;
						z1 = z;
						code2 = MathUtil.getCohenSutherlandCode3D(x1, y1, z1, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
					}
				}
			}
		}
		if (accept) {
			set(x0, y0, z0, x1, y1, z1);
		}
		return accept;
	}
	
	@Pure
	@Override
	default boolean intersects(Sphere3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		return Sphere3ad.intersectsSphereSegment(
				s.getX(), s.getY(), s.getZ(),
				s.getRadius(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default boolean intersects(Prism3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return RectangularPrism3ad.intersectsRectangleSegment(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	
	@Pure
	@Override
	default boolean intersects(Segment3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		return intersectsSegmentSegmentWithEnds(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2());
	}


	@Pure
	@Override
	default boolean intersects(PathIterator3ad<?> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path3ad.computeCrossingsFromSegment(
				0,
				iterator,
				getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3ad<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	/** Result of the intersection between segments in a context where a single
	 * test is not enough.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 * @see Segment3ad#getNoSegmentSegmentWithEndsIntersection(double, double, double, double, double, double, double, double)
	 * @see Segment3ad#getNoSegmentSegmentWithoutEndsIntersection(double, double, double, double, double, double, double, double)
	 */
	enum UncertainIntersection {
		/** Intersection, uncertainly.
		 */
		PERHAPS {
			@Override
			public boolean booleanValue() {
				return true;
			}
		},
		/** No intersection, certainly.
		 */
		NO {
			@Override
			public boolean booleanValue() {
				return false;
			}
		};
		
		/** Replies the boolean representation of the constant.
		 *
		 * @return the boolean representation.
		 */
		public abstract boolean booleanValue();

		/** Replies the UncertainIntersection for the given boolean.
		 *
		 * @param value the boolean value.
		 * @return the UncertainIntersection.
		 */
		public static UncertainIntersection fromBoolean(boolean value) {
			if (value) {
				return UncertainIntersection.PERHAPS;
			}
			return UncertainIntersection.NO;
		}
		
	}

	@Pure
	@Override
	default PathIterator3ad<IE> getPathIterator(Transform3D transform) {
		return new SegmentPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		Segment3ad.computeClosestPointTo(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				p.getX(), p.getY(), p.getZ(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		Segment3ad.computeFarthestPointTo(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				p.getX(), p.getY(), p.getZ(),
				point);
		return point;
	}

	/** Iterator on the path elements of the segment.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class SegmentPathIterator<T extends PathElement3ad> implements PathIterator3ad<T> {

		private final Segment3ad<?, ?, T, ?, ?, ?> segment;

		private final Point3D<?, ?> p1;

		private final Point3D<?, ?> p2;

		private final Transform3D transform;

		private final double x1;

		private final double y1;

		private final double z1;

		private final double x2;

		private final double y2;

		private final double z2;

		private int index = 0;

		/**
		 * @param segment the iterated segment.
		 * @param transform the transformation, or <code>null</code>.
		 */
		public SegmentPathIterator(Segment3ad<?, ?, T, ?, ?, ?> segment, Transform3D transform) {
			assert (segment != null) : "Segment must be not null"; //$NON-NLS-1$
			this.segment = segment;
			this.p1 = new InnerComputationPoint3ad();
			this.p2 = new InnerComputationPoint3ad();
			this.transform = (transform == null || transform.isIdentity()) ? null : transform;
			this.x1 = segment.getX1();
			this.y1 = segment.getY1();
			this.z1 = segment.getZ1();
			this.x2 = segment.getX2();
			this.y2 = segment.getY2();
			this.z2 = segment.getZ2();
			if (this.x1 == this.x2 && this.y1 == this.y2 && this.z1 == this.z2) {
				this.index = 2;
			}
		}
		
		@Override
		public PathIterator3ad<T> restartIterations() {
			return new SegmentPathIterator<>(this.segment, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 1;
		}

		@Override
		public T next() {
			if (this.index>1) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1, this.z1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
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

		@Pure
		@Override
		public boolean isCurved() {
			return false;
		}
		
		@Pure
		@Override
		public boolean isPolygon() {
			return false;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public GeomFactory3ad<T, ?, ?, ?> getGeomFactory() {
			return this.segment.getGeomFactory();
		}

	}

}

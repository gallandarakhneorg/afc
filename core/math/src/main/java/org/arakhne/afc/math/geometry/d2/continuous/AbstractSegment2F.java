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

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.FunctionalVector2D;
import org.arakhne.afc.math.geometry.d2.Point2D;


/**
 * @author Hamza JAFFALI (hjaffali)
 *
 */
public abstract class AbstractSegment2F<T extends Shape2F> extends AbstractShape2F<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -266585669808988323L;

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
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double)
	 * @see Point2f#isCollinearPoints(double, double, double, double, double, double)
	 */
	public static boolean isCollinearLines(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return (isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4) && FunctionalPoint2D.isCollinearPoints(x1, y1, x2, y2, x3, y3));
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
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(double, double, double, double, double, double, double, double)
	 */
	public static boolean isParallelLines(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return FunctionalVector2D.isCollinearVectors(x2 - x1, y2 - y1, x4 - x3, y4 - y3);
	}

	/**
	 * Replies the intersection point between two segments.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @return the intersection point or <code>null</code> if none.
	 */
	public static Point2f computeSegmentSegmentIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double m = computeSegmentSegmentIntersectionFactor(x1, y1, x2, y2, x3, y3, x4, y4);
		if (Double.isNaN(m))
			return null;
		return new Point2f(x1 + m * (x2 - x1), y1 + m * (y2 - y1));
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
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	public static double computeSegmentSegmentIntersectionFactor(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double X1 = x2 - x1;
		double Y1 = y2 - y1;
		double X2 = x4 - x3;
		double Y2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		double det = FunctionalVector2D.perpProduct(X1, Y1, X2, Y2);
		if (det == 0.)
			return Double.NaN;

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		double u = FunctionalVector2D.perpProduct(X1, Y1, x1 - x3, y1 - y3) / det;
		if (u < 0. || u > 1.)
			return Double.NaN;
		u = FunctionalVector2D.perpProduct(X2, Y2, x1 - x3, y1 - y3) / det;
		return (u < 0. || u > 1.) ? Double.NaN : u;
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
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	public static double computeLineLineIntersectionFactor(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double X1 = x2 - x1;
		double Y1 = y2 - y1;
		double X2 = x4 - x3;
		double Y2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		double det = FunctionalVector2D.perpProduct(X1, Y1, X2, Y2);
		if (det == 0.)
			return Double.NaN;

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		return FunctionalVector2D.perpProduct(X2, Y2, x1 - x3, y1 - y3) / det;
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
	 * @return the intersection point or <code>null</code> if there is no intersection.
	 */
	public static Point2f computeLineLineIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		double denom = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		if (denom==0.) return null;
		double ua = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3));
		double ub = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3));
		if (ua==ub) return null;
		ua = ua / denom;
		return new Point2f(
				x1 + ua * (x2 - x1),
				y1 + ua * (y2 - y1));
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
	 * @see #distanceLinePoint(double, double, double, double, double, double)
	 */
	public static double distanceSquaredLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		double r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return FunctionalPoint2D.distanceSquaredPointPoint(px, py, x1, y1);
		double s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 */
	public static double distanceSquaredSegmentPoint(double x1, double y1, double x2, double y2, double px, double py, Point2D pts) {
		double r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0f) return FunctionalPoint2D.distanceSquaredPointPoint(px, py, x1, y1);
		double r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		double ratio = r_numerator / r_denomenator;

		if (ratio<=0f) {
			if (pts!=null) pts.set(x1, y1);
			return Math.abs((px-x1)*(px-x1) + (py-y1)*(py-y1));
		}

		if (ratio>=1f) {
			if (pts!=null) pts.set(x2, y2);
			return Math.abs((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}

		if (pts!=null) pts.set(
				ratio * (x2-x1),
				ratio * (y2-y1));

		double s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 */
	public static double distanceSegmentPoint(double x1, double y1, double x2, double y2, double px, double py, Point2D pts) {
		double r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return FunctionalPoint2D.distancePointPoint(px, py, x1, y1);
		double r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		double ratio = r_numerator / r_denomenator;

		if (ratio<=0.) {
			if (pts!=null) pts.set(x1, y1);
			return Math.sqrt((px-x1)*(px-x1) + (py-y1)*(py-y1));
		}

		if (ratio>=1.) {
			if (pts!=null) pts.set(x2, y2);
			return Math.sqrt((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}

		if (pts!=null) pts.set(
				ratio * (x2-x1),
				ratio * (y2-y1));

		double s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return Math.abs(s) * Math.sqrt(r_denomenator);
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
	 * @see #distanceSquaredLinePoint(double, double, double, double, double, double)
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double)
	 */
	public static double distanceLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		double r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return FunctionalPoint2D.distancePointPoint(px, py, x1, y1);
		double s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return Math.abs(s) * Math.sqrt(r_denomenator);
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
	public static boolean isPointClosedToSegment( double x1, double y1, 
			double x2, double y2, 
			double x, double y, double hitDistance ) {
		return ( distanceSegmentPoint(x1, y1, x2, y2, x, y, null) < hitDistance ) ;
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
	public static boolean isPointClosedToLine( double x1, double y1, 
			double x2, double y2, 
			double x, double y, double hitDistance ) {
		return ( distanceLinePoint(x1, y1, x2, y2, x, y) < hitDistance ) ;
	}

	/**
	 * Replies the projection a point on a segment.
	 * 
	 * @param px
	 *            is the coordiante of the point to project
	 * @param py
	 *            is the coordiante of the point to project
	 * @param s1x
	 *            is the x-coordinate of the first line point.
	 * @param s1y
	 *            is the y-coordinate of the first line point.
	 * @param s2x
	 *            is the x-coordinate of the second line point.
	 * @param s2y
	 *            is the y-coordinate of the second line point.
	 * @return the projection of the specified point on the line. If 
	 * equal to {@code 0}, the projection is equal to the first segment point. 
	 * If equal to {@code 1}, the projection is equal to the second segment point. 
	 * If inside {@code ]0;1[}, the projection is between the two segment points. 
	 * If inside {@code ]-inf;0[}, the projection is outside on the side of the 
	 * first segment point. If inside {@code ]1;+inf[}, the projection is 
	 * outside on the side of the second segment point.
	 */
	public static double computeProjectedPointOnLine(double px, double py, double s1x, double s1y, double s2x, double s2y) {
		double r_numerator = (px-s1x)*(s2x-s1x) + (py-s1y)*(s2y-s1y);
		double r_denomenator = (s2x-s1x)*(s2x-s1x) + (s2y-s1y)*(s2y-s1y);
		return r_numerator / r_denomenator;
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
	public static double computeRelativeDistanceLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		double r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return FunctionalPoint2D.distancePointPoint(px, py, x1, y1);
		double s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return s * Math.sqrt(r_denomenator);
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
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
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
	public static int computeSideLinePoint(double x1, double y1, double x2, double y2, double px, double py, double epsilon) {
		double cx2 = x2 - x1;
		double cy2 = y2 - y1;
		double cpx = px - x1;
		double cpy = py - y1;
		double side = cpx * cy2 - cpy * cx2;
		if (side != 0f && MathUtil.isEpsilonZero(side, epsilon)) {
			side = 0f;
		}
		return (side < 0f) ? -1 : ((side > 0f) ? 1 : 0);
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
	public static int ccw(double x1, double y1, double x2, double y2, double px, double py, double epsilon) {
		double cx2 = x2 - x1;
		double cy2 = y2 - y1;
		double cpx = px - x1;
		double cpy = py - y1;
		double ccw = cpx * cy2 - cpy * cx2;
		if (MathUtil.isEpsilonZero(ccw, epsilon)) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = cpx * cx2 + cpy * cy2;
			if (ccw > 0.) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				cpx -= cx2;
				cpy -= cy2;
				ccw = cpx * cx2 + cpy * cy2;
				if (ccw < 0f) {
					ccw = 0f;
				}
			}
		}
		return (ccw < 0f) ? -1 : ((ccw > 0f) ? 1 : 0);
	}

	/** Compute the interpolate point between the two points.
	 * 
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 */
	public static Point2f interpolate(double p1x, double p1y, double p2x, double p2y, double factor) {
		double f = (factor<0f) ? 0f : factor;
		if (f>1f) f = 1f;
		double vx = p2x - p1x;
		double vy = p2y - p1y;
		return new Point2f(
				p1x + factor * vx,
				p1y + factor * vy);
	}

	/** Replies the point on the segment that is farthest to the given point.
	 * 
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @return the farthest point on the shape.
	 */
	public static Point2f computeFarthestPointTo(
			double ax, double ay, double bx, double by, double px, double py) {
		double v1x = px - ax;
		double v1y = py - ay;
		double v2x = px - bx;
		double v2y = py - by;
		if ((v1x*v1x+v1y*v1y) >= (v2x*v2x+v2y*v2y)) {
			return new Point2f(ax, ay);
		}
		return new Point2f(bx, by);
	}

	/** Replies the point on the segment that is closest to the given point.
	 * 
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @return the is point on the shape.
	 */
	public static Point2f computeClosestPointTo(
			double ax, double ay, double bx, double by, double px, double py) {
		double ratio = AbstractSegment2F.computeProjectedPointOnLine(px, py,
				ax, ay,
				bx, by);
		if (ratio<=0f) return new Point2f(ax, ay);
		if (ratio>=1f) return new Point2f(bx, by);
		return new Point2f(
				ax + (bx - ax) * ratio,
				ay + (by - ay) * ratio); 
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 * 
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	public static int computeCrossingsFromPoint(
			double px, double py,
			double x0, double y0,
			double x1, double y1) {
		// Copied from AWT API
		if (py <  y0 && py <  y1) return 0;
		if (py >= y0 && py >= y1) return 0;
		// assert(y0 != y1);
		if (px >= x0 && px >= x1) return 0;
		if (px <  x0 && px <  x1) return (y0 < y1) ? 1 : -1;
		double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px >= xintercept) return 0;
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
	private static int computeCrossingsFromPoint1(
			double px, double py,
			double x0, double y0,
			double x1, double y1) {
		// Copied from AWT API
		if (py <  y0 && py <  y1) return 0;
		if (py > y0 && py > y1) return 0;
		// assert(y0 != y1);
		if (px > x0 && px > x1) return 0;
		if (px <  x0 && px <  x1) return (y0 < y1) ? 1 : -1;
		double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px > xintercept) return 0;
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the segment (sx0,sy0) to (sx1,sy1) extending to the right.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param sx1 is the first point of the segment to extend.
	 * @param sy1 is the first point of the segment to extend.
	 * @param sx2 is the second point of the segment to extend.
	 * @param sy2 is the second point of the segment to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	public static int computeCrossingsFromSegment(
			int crossings,
			double sx1, double sy1,
			double sx2, double sy2,
			double x0, double y0,
			double x1, double y1) {
		int numCrosses = crossings;

		double xmin = Math.min(sx1, sx2);
		double xmax = Math.max(sx1, sx2);
		double ymin = Math.min(sy1, sy2);
		double ymax = Math.max(sy1, sy2);

		if (y0<=ymin && y1<=ymin) return numCrosses;
		if (y0>=ymax && y1>=ymax) return numCrosses;
		if (x0<=xmin && x1<=xmin) return numCrosses;
		if (x0>=xmax && x1>=xmax) {
			// The line is entirely at the right of the shadow
			if (y0<y1) {
				if (y0<=ymin) ++numCrosses;
				if (y1>=ymax) ++numCrosses;
			}
			else {
				if (y1<=ymin) --numCrosses;
				if (y0>=ymax) --numCrosses;
			}
		}
		else if (intersectsSegmentSegmentWithoutEnds(x0, y0, x1, y1, sx1, sy1, sx2, sy2)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			int side1, side2;
			if (sy1<=sy2) {
				side1 = computeSideLinePoint(sx1, sy1, sx2, sy2, x0, y0, 0.);
				side2 = computeSideLinePoint(sx1, sy1, sx2, sy2, x1, y1, 0.);
			}
			else {
				side1 = computeSideLinePoint(sx2, sy2, sx1, sy1, x0, y0, 0.);
				side2 = computeSideLinePoint(sx2, sy2, sx1, sy1, x1, y1, 0.);
			}
			if (side1>0 || side2>0) {
				int n1 = computeCrossingsFromPoint(sx1, sy1, x0, y0, x1, y1);
				int n2;
				if (n1!=0) {
					n2 = computeCrossingsFromPoint1(sx2, sy2, x0, y0, x1, y1);
				}
				else {
					n2 = computeCrossingsFromPoint(sx2, sy2, x0, y0, x1, y1);
				}
				numCrosses += n1;
				numCrosses += n2;
			}
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ellipse (ex0,ey0) to (ex1,ey1) extending to the right.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param ex is the first corner of the ellipse to extend.
	 * @param ey is the first corner of the ellipse to extend.
	 * @param ew is the width of the ellipse to extend.
	 * @param eh is the height of the ellipse  to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromEllipse(
			int crossings,
			double ex, double ey,
			double ew, double eh,
			double x0, double y0,
			double x1, double y1) {
		int numCrosses = crossings;

		double xmin = ex;
		double ymin = ey;
		double xmax = ex + ew;
		double ymax = ey + eh;

		if (y0<=ymin && y1<=ymin) return numCrosses;
		if (y0>=ymax && y1>=ymax) return numCrosses;
		if (x0<=xmin && x1<=xmin) return numCrosses;

		if (x0>=xmax && x1>=xmax) {
			// The line is entirely at the right of the shadow
			if (y0<y1) {
				if (y0<=ymin) ++numCrosses;
				if (y1>=ymax) ++numCrosses;
			}
			else {
				if (y1<=ymin) --numCrosses;
				if (y0>=ymax) --numCrosses;
			}
		}
		else if (Ellipse2f.intersectsEllipseSegment(
				xmin, ymin, xmax-xmin, ymax-ymin,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			double xcenter = (xmin+xmax)/2f;
			numCrosses += computeCrossingsFromPoint(xcenter, ymin, x0, y0, x1, y1);
			numCrosses += computeCrossingsFromPoint(xcenter, ymax, x0, y0, x1, y1);
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ellipse (ex0,ey0) to (ex1,ey1) extending to the right.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param cx is the center of the circle to extend.
	 * @param cy is the center of the circle to extend.
	 * @param radius is the radius of the circle to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromCircle(
			int crossings,
			double cx, double cy,
			double radius,
			double x0, double y0,
			double x1, double y1) {
		int numCrosses = crossings;

		double xmin = cx - Math.abs(radius);
		double ymin = cy - Math.abs(radius);
		double ymax = cy + Math.abs(radius);

		if (y0<=ymin && y1<=ymin) return numCrosses;
		if (y0>=ymax && y1>=ymax) return numCrosses;
		if (x0<=xmin && x1<=xmin) return numCrosses;

		if (x0>=cx+radius && x1>=cx+radius) {
			// The line is entirely at the right of the shadow
			if (y0<y1) {
				if (y0<=ymin) ++numCrosses;
				if (y1>=ymax) ++numCrosses;
			}
			else {
				if (y1<=ymin) --numCrosses;
				if (y0>=ymax) --numCrosses;
			}
		}
		else if (AbstractCircle2F.intersectsCircleSegment(
				cx, cy, radius,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			numCrosses += computeCrossingsFromPoint(cx, ymin, x0, y0, x1, y1);
			numCrosses += computeCrossingsFromPoint(cx, ymax, x0, y0, x1, y1);
		}

		return numCrosses;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the rectangle.  See the comment
	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
	 * 
	 * @param crossings is the initial value for the number of crossings.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	public static int computeCrossingsFromRect(
			int crossings,
			double rxmin, double rymin,
			double rxmax, double rymax,
			double x0, double y0,
			double x1, double y1) {
		int numCrosses = crossings;

		if (y0 >= rymax && y1 >= rymax) return numCrosses;
		if (y0 <= rymin && y1 <= rymin) return numCrosses;
		if (x0 <= rxmin && x1 <= rxmin) return numCrosses;
		if (x0 >= rxmax && x1 >= rxmax) {
			// Line is entirely to the right of the rect
			// and the vertical ranges of the two overlap by a non-empty amount
			// Thus, this line segment is partially in the "right-shadow"
			// Path may have done a complete crossing
			// Or path may have entered or exited the right-shadow
			if (y0 < y1) {
				// y-increasing line segment...
				// We know that y0 < rymax and y1 > rymin
				if (y0 <= rymin) ++numCrosses;
				if (y1 >= rymax) ++numCrosses;
			}
			else if (y1 < y0) {
				// y-decreasing line segment...
				// We know that y1 < rymax and y0 > rymin
				if (y1 <= rymin) --numCrosses;
				if (y0 >= rymax) --numCrosses;
			}
			return numCrosses;
		}
		// Remaining case:
		// Both x and y ranges overlap by a non-empty amount
		// First do trivial INTERSECTS rejection of the cases
		// where one of the endpoints is inside the rectangle.
		if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
				(x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		// Otherwise calculate the y intercepts and see where
		// they fall with respect to the rectangle
		double xi0 = x0;
		if (y0 < rymin) {
			xi0 += ((rymin - y0) * (x1 - x0) / (y1 - y0));
		}
		else if (y0 > rymax) {
			xi0 += ((rymax - y0) * (x1 - x0) / (y1 - y0));
		}
		double xi1 = x1;
		if (y1 < rymin) {
			xi1 += ((rymin - y1) * (x0 - x1) / (y0 - y1));
		}
		else if (y1 > rymax) {
			xi1 += ((rymax - y1) * (x0 - x1) / (y0 - y1));
		}
		if (xi0 <= rxmin && xi1 <= rxmin) return numCrosses;
		if (xi0 >= rxmax && xi1 >= rxmax) {
			if (y0 < y1) {
				// y-increasing line segment...
				// We know that y0 < rymax and y1 > rymin
				if (y0 <= rymin) ++numCrosses;
				if (y1 >= rymax) ++numCrosses;
			}
			else if (y1 < y0) {
				// y-decreasing line segment...
				// We know that y1 < rymax and y0 > rymin
				if (y1 <= rymin) --numCrosses;
				if (y0 >= rymax) --numCrosses;
			}
			return numCrosses;
		}
		return MathConstants.SHAPE_INTERSECTS;
	}

	/** Replies if two lines are intersecting.
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsLineLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		if (isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return FunctionalPoint2D.isCollinearPoints(x1, y1, x2, y2, x3, y3);
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
	public static boolean intersectsSegmentLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return (computeSideLinePoint(x3, y3, x4, y4, x1, y1, MathConstants.EPSILON) *
				computeSideLinePoint(x3, y3, x4, y4, x2, y2, MathConstants.EPSILON)) <= 0;
	}

	private static boolean intersectsSSWE(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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

		if (sign<0f) return true;
		if (sign>0f) return false;

		double squaredLength = vx1*vx1 + vy1*vy1;

		if (f1==0f && f2==0f) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return (f1>=0f || f2>=0) && (f1<=1f || f2<=1f);
		}

		if (f1==0f) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return (f1>=0f && f1<=1f);
		}

		if (f2==0f) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return (f2>=0 && f2<=1f);
		}
		
		return false;
	}

	private static boolean intersectsSSWoE(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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

		if (sign<0f) return true;
		if (sign>0f) return false;

		if (f1==0f && f2==0f) {
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

			return (f1>0f || f2>0) && (f1<1f || f2<1f);
		}

		return false;
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
	public static boolean intersectsSegmentSegmentWithoutEnds(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		boolean r;
		r = intersectsSSWoE(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r) return r;
		return intersectsSSWoE(x3, y3, x4, y4, x1, y1, x2, y2);
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)}.
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
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)
	 */
	public static boolean intersectsSegmentSegmentWithEnds(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		boolean r;
		r = intersectsSSWE(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r) return r;
		return intersectsSSWE(x3, y3, x4, y4, x1, y1, x2, y2);
	}

	

	/**
	 * Replies if this segment is empty.
	 * The segment is empty when the two
	 * points are equal.
	 * 
	 * @return <code>true</code> if the two points are
	 * equal.
	 */
	@Override
	public boolean isEmpty() {
		return this.getX1()==this.getX2() && this.getY1()==this.getY2();
	}

	/** Change the line.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	abstract public void set(double x1, double y1, double x2, double y2);

	/** Change the line.
	 * 
	 * @param a
	 * @param b
	 */
	abstract public void set(Point2D a, Point2D b);

	@Override
	abstract public void set(Shape2F s);
	
	
	/**Sets a new value in the X of the first point.
	 * 
	 * @param the new value double x
	 */
	abstract public void setX1(double x);
	
	/**Sets a new value in the Y of the first point.
	 * 
	 * @param the new value double y
	 */
	abstract public void setY1(double y);
	
	/**Sets a new value in the X of the second point.
	 * 
	 * @param the new value double x
	 */
	abstract public void setX2(double x);
	
	/**Sets a new value in the Y of the second point.
	 * 
	 * @param the new value double y
	 */
	abstract public void setY2(double y);

	/** Replies the X of the first point.
	 * 
	 * @return the x of the first point.
	 */
	abstract public double getX1();

	/** Replies the Y of the first point.
	 * 
	 * @return the y of the first point.
	 */
	abstract public double getY1();

	/** Replies the X of the second point.
	 * 
	 * @return the x of the second point.
	 */
	abstract public double getX2();

	/** Replies the Y of the second point.
	 * 
	 * @return the y of the second point.
	 */
	abstract public double getY2();

	/** Replies the first point.
	 * 
	 * @return the first point.
	 */
	abstract public Point2D getP1();

	/** Replies the second point.
	 * 
	 * @return the second point.
	 */
	abstract public Point2D getP2();

	
	
	
	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2f toBoundingBox() {
		Rectangle2f r = new Rectangle2f();
		r.setFromCorners(
				this.getX1(),
				this.getY1(),
				this.getX2(),
				this.getY2());
		return r;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void toBoundingBox(Rectangle2f box) {
		box.setFromCorners(
				this.getX1(),
				this.getY1(),
				this.getX2(),
				this.getY2());
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceSquared(Point2D p) {
		return distanceSquaredSegmentPoint(
				this.getX1(),this.getY1(),
				this.getX2(),this.getY2(),
				p.getX(), p.getY(),
				null);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceL1(Point2D p) {
		double ratio = AbstractSegment2F.computeProjectedPointOnLine(p.getX(), p.getY(), this.getX1(),this.getY1(),this.getX2(),this.getY2());
		ratio = MathUtil.clamp(ratio, 0f, 1f);
		Vector2f v = new Vector2f(this.getX2(), this.getY2());
		v.sub(this.getX1(),this.getY1());
		v.scale(ratio);
		return Math.abs(this.getX1() + v.getX() - p.getX())
				+ Math.abs(this.getY1() + v.getY() - p.getY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceLinf(Point2D p) {
		double ratio = AbstractSegment2F.computeProjectedPointOnLine(p.getX(), p.getY(), this.getX1(),this.getY1(),this.getX2(),this.getY2());
		ratio = MathUtil.clamp(ratio, 0f, 1f);
		Vector2f v = new Vector2f(this.getX2(), this.getY2());
		v.sub(this.getX1(),this.getY1());
		v.scale(ratio);
		return Math.max(
				Math.abs(this.getX1() + v.getX() - p.getX()),
				Math.abs(this.getY1() + v.getY() - p.getY()));
	}

	/** {@inheritDoc}
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
	 * 
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Override
	public boolean contains(double x, double y) {
		return MathUtil.isEpsilonZero(
				distanceSquaredSegmentPoint(
				this.getX1(),this.getY1(),
				this.getX2(),this.getY2(),
				x, y,
				null));
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean contains(Rectangle2f r) {
		return contains(r.getMinX(), r.getMinY())
				&& contains(r.getMaxX(), r.getMaxY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getClosestPointTo(Point2D p) {
		return computeClosestPointTo(
				this.getX1(), this.getY1(),
				this.getX2(), this.getY2(),
				p.getX(), p.getY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getFarthestPointTo(Point2D p) {
		return computeFarthestPointTo(
				this.getX1(), this.getY1(),
				this.getX2(), this.getY2(),
				p.getX(), p.getY());
	}

	@Override
	public void translate(double dx, double dy) {
		this.set(this.getX1()+dx,this.getY1()+dy,this.getX2()+dx,this.getY2()+dy);
	}

	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		return new SegmentPathIterator(
				this.getX1(), this.getY1(), this.getX2(), this.getY2(),
				transform);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof AbstractSegment2F) {
			AbstractSegment2F<T> rr2d = (AbstractSegment2F<T>) obj;
			return ((this.getX1() == rr2d.getX1()) &&
					(this.getY1() == rr2d.getY1()) &&
					(this.getX2() == rr2d.getX2()) &&
					(this.getY2() == rr2d.getY2()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(this.getX1());
		bits = 31L * bits + doubleToLongBits(this.getY1());
		bits = 31L * bits + doubleToLongBits(this.getX2());
		bits = 31L * bits + doubleToLongBits(this.getY2());
		return (int) (bits ^ (bits >> 32));
	}

	/** Transform the current segment.
	 * This function changes the current segment.
	 * 
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape(Transform2D)
	 */
	public void transform(Transform2D transform) {
		Point2f p = new Point2f(this.getX1(),  this.getY1());
		transform.transform(p);
		this.setX1(p.getX());
		this.setY1(p.getY());
		p.set(this.getX2(), this.getY2());
		transform.transform(p);
		this.setX2(p.getX());
		this.setY2(p.getY());
	}	

	@Override
	public Shape2F createTransformedShape(Transform2D transform) {
		Point2D p1 = transform.transform(this.getX1(), this.getY1());
		Point2D p2 = transform.transform(this.getX2(), this.getY2());
		return new Segment2f(p1, p2);
	}
	
	/** Clip the segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 */
	public boolean clipToRectangle(double rxmin, double rymin, double rxmax, double rymax) {
		double x0 = this.getX1();
		double y0 = this.getY1();
		double x1 = this.getX2();
		double y1 = this.getY2();
		int code1 = MathUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = MathUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		double x, y;
		x = y = 0;
		
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
						code1 = MathUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					}
					else {
						x1 = x;
						y1 = y;
						code2 = MathUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			set(x0, y0, x1, y1);
		}
		return accept;
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return Rectangle2f.intersectsRectangleSegment(
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY(),
				this.getX1(), this.getY1(),
				this.getX2(), this.getY2());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return Ellipse2f.intersectsEllipseSegment(
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight(),
				this.getX1(), this.getY1(),
				this.getX2(), this.getY2());
	}

	@Override
	public boolean intersects(Circle2f s) {
		return AbstractCircle2F.intersectsCircleSegment(
				s.getX(), s.getY(),
				s.getRadius(),
				this.getX1(), this.getY1(),
				this.getX2(), this.getY2());
	}

	@Override
	public boolean intersects(Segment2f s) {
		return intersectsSegmentSegmentWithoutEnds(
				this.getX1(), this.getY1(),
				this.getX2(), this.getY2(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromSegment(
				0,
				s,
				this.getX1(), this.getY1(), this.getX2(), this.getY2(),
				false);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return AbstractOrientedRectangle2F.intersectsOrientedRectangleSegment(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				this.getX1(), this.getY1(), this.getX2(), this.getY2());
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(this.getX1());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getY1());
		b.append("|"); //$NON-NLS-1$
		b.append(this.getX2());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getY2());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/** Iterator on the path elements of the segment.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class SegmentPathIterator implements PathIterator2f {

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		private final Transform2D transform;
		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;
		private int index = 0;

		/**
		 * @param x11
		 * @param y11
		 * @param x21
		 * @param y21
		 * @param transform1
		 */
		public SegmentPathIterator(double x11, double y11, double x21, double y21, Transform2D transform1) {
			this.transform = transform1;
			this.x1 = x11;
			this.y1 = y11;
			this.x2 = x21;
			this.y2 = y21;
			if (this.x1==this.x2 && this.y1==this.y2) {
				this.index = 2;
			}
		}

		@Override
		public boolean hasNext() {
			return this.index<=1;
		}

		@Override
		public AbstractPathElement2F next() {
			if (this.index>1) throw new NoSuchElementException();
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
				this.p2.set(this.x2, this.y2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new AbstractPathElement2F.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
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
	
}

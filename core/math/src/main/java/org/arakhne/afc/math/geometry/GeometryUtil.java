/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stéphane GALLAND
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.math.geometry;

import java.util.Arrays;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Matrix2f;
import org.arakhne.afc.math.Matrix3f;
import org.arakhne.afc.math.geometry2d.Point2D;
import org.arakhne.afc.math.geometry2d.continuous.Point2f;
import org.arakhne.afc.math.geometry2d.continuous.Vector2f;
import org.arakhne.afc.math.geometry3d.Point3D;
import org.arakhne.afc.math.geometry3d.continuous.Point3f;
import org.arakhne.afc.math.geometry3d.continuous.Vector3f;

/** Geometric utilities.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class GeometryUtil implements MathConstants{
	
	private GeometryUtil() {
		//
	}


	/** Compute the distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance between the point and the segment.
	 */
	public static final float distancePointSegment(float px, float py, float x1, float y1, float x2, float y2, Point2D pts) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) {
			if (pts!=null) pts.set(x1, y1);
			return distancePointPoint(px, py, x1, y1);
		}
		float r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		float ratio = r_numerator / r_denomenator;
	
		if (ratio<=0.) {
			if (pts!=null) pts.set(x1, y1);
			return (float)Math.sqrt((px-x1)*(px-x1) + (py-y1)*(py-y1));
		}
	
		if (ratio>=1.) {
			if (pts!=null) pts.set(x2, y2);
			return (float)Math.sqrt((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}
	
		if (pts!=null) pts.set(
				x1 + ratio * (x2-x1),
				y1 + ratio * (y2-y1));
	
		float s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (float)(Math.abs(s) * Math.sqrt(r_denomenator));
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 * if not <code>null</code>.
	 * @return the distance between the point and the segment.
	 */
	public static final float distanceSquaredPointSegment(float px, float py, float x1, float y1, float x2, float y2, Point2D pts) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0f) return distanceSquaredPointPoint(px, py, x1, y1);
		float r_numerator = (px-x1)*(x2-x1) + (py-y1)*(y2-y1);
		float ratio = r_numerator / r_denomenator;
	
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
	
		float s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}
	
    /**
     * Compute and replies the perpendicular distance from a point to a segment.
     * <p>
     * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
     * 
     * @param px
     *            is the X coord of the point
     * @param py
     *            is the Y coord of the point
     * @param pz
     *            is the Z coord of the point
     * @param sx1
     *            is the X coord of the first point of the segment
     * @param sy1
     *            is the Y coord of the first point of the segment
     * @param sz1
     *            is the Z coord of the first point of the segment
     * @param sx2
     *            is the X coord of the second point of the segment
     * @param sy2
     *            is the Y coord of the second point of the segment
     * @param sz2
     *            is the Z coord of the second point of the segment
     * @return the distance
     */
    public static float distancePointSegment(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2) {
            float ratio = getPointProjectionFactorOnSegment(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

            if (ratio <= 0.)
                    return distancePointPoint(px, py, pz, sx1, sy1, sz1);

            if (ratio >= 1.)
                    return distancePointPoint(px, py, pz, sx2, sy2, sz2);

            return distancePointPoint(px, py, pz, (1.f - ratio) * sx1 + ratio * sx2, (1.f - ratio) * sy1 + ratio * sy2, (1.f - ratio) * sz1 + ratio * sz2);
    }

    /**
     * Compute and replies the perpendicular squared distance from a point to a segment.
     * <p>
     * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
     * 
     * @param px
     *            is the X coord of the point
     * @param py
     *            is the Y coord of the point
     * @param pz
     *            is the Z coord of the point
     * @param sx1
     *            is the X coord of the first point of the segment
     * @param sy1
     *            is the Y coord of the first point of the segment
     * @param sz1
     *            is the Z coord of the first point of the segment
     * @param sx2
     *            is the X coord of the second point of the segment
     * @param sy2
     *            is the Y coord of the second point of the segment
     * @param sz2
     *            is the Z coord of the second point of the segment
     * @return the distance
     */
    public static float distanceSquaredPointSegment(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2) {
            float ratio = getPointProjectionFactorOnSegment(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

            if (ratio <= 0.)
                    return distanceSquaredPointPoint(px, py, pz, sx1, sy1, sz1);

            if (ratio >= 1.)
                    return distanceSquaredPointPoint(px, py, pz, sx2, sy2, sz2);

            return distanceSquaredPointPoint(px, py, pz, (1.f - ratio) * sx1 + ratio * sx2, (1.f - ratio) * sy1 + ratio * sy2, (1.f - ratio) * sz1 + ratio * sz2);
    }

	/** Compute the distance between a point and a line.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @return the distance between the point and the line.
	 * @see #distanceSquaredPointLine(float, float, float, float, float, float)
	 * @see #distanceRelativePointLine(float, float, float, float, float, float)
	 */
	public static final float distancePointLine(float px, float py, float x1, float y1, float x2, float y2) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distancePointPoint(px, py, x1, y1);
		float s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (float)(Math.abs(s) * Math.sqrt(r_denomenator));
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @return the distance between the point and the line.
	 * @see #distancePointLine(float, float, float, float, float, float)
	 */
	public static final float distanceSquaredPointLine(float px, float py, float x1, float y1, float x2, float y2) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distanceSquaredPointPoint(px, py, x1, y1);
		float s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (s * s) * Math.abs(r_denomenator);
	}

	/** Compute the distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distanceSquaredPointPoint(float, float, float, float)
	 */
	public static final float distancePointPoint(float x1, float y1, float x2, float y2) {
		float dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}

	/** Compute the squared distance between 2 2D points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointPoint(float, float, float, float)
	 */
	public static final float distanceSquaredPointPoint(float x1, float y1, float x2, float y2) {
		float dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return dx*dx+dy*dy;
	}
	
	/** Compute the squared distance between 2 3D points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointPoint(float, float, float, float)
	 */
	public static final float distanceSquaredPointPoint(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx, dy, dz;
		dx = x1 - x2;
		dy = y1 - y2;
		dz = z1 - z2;
		return dx*dx+dy*dy+dz*dz;
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 horizontal location of the first-segment beginning.
	 * @param y1 vertical location of the first-segment ending.
	 * @param x2 horizontal location of the second-segment beginning.
	 * @param y2 vertical location of the second-segment ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	public static boolean isClosedToSegmentPoint( float x1, float y1, 
			float x2, float y2, 
			float x, float y, float hitDistance ) {
		return ( GeometryUtil.distancePointSegment(x, y, x1, y1, x2, y2, null) < hitDistance ) ;
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 horizontal location of the first-line beginning.
	 * @param y1 vertical location of the first-line ending.
	 * @param x2 horizontal location of the second-line beginning.
	 * @param y2 vertical location of the second-line ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	public static boolean isClosedToLinePoint( float x1, float y1, 
											   float x2, float y2, 
											   float x, float y, float hitDistance ) {
		return ( distancePointLine(x, y, x1, y1, x2, y2) < hitDistance ) ;
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
	 * @return <code>factor1</code> or {@link Float#NaN} if no intersection.
	 * @since 3.0
	 */
	public static float getIntersectionFactorSegmentSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;
	
		// determinant is zero when parallel = det(L1,L2)
		float det = MathUtil.determinant(X1, Y1, X2, Y2);
		if (det == 0.)
			return Float.NaN;
	
		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		float u = MathUtil.determinant(X1, Y1, x1 - x3, y1 - y3) / det;
		if (u < 0. || u > 1.)
			return Float.NaN;
		u = MathUtil.determinant(X2, Y2, x1 - x3, y1 - y3) / det;
		return (u < 0. || u > 1.) ? Float.NaN : u;
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
	 * @param intersection TODO
	 */
	public static void getIntersectionPointSegmentSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Point2D intersection) {
		float m = getIntersectionFactorSegmentSegment(x1, y1, x2, y2, x3, y3, x4, y4);
		if (Float.isNaN(m)){
			intersection = null;
			return;
		}
			
		intersection.set(x1 + m * (x2 - x1), y1 + m * (y2 - y1));
		return;
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 * @param intersection TODO
	 * @param p1 is a point of the first line.
	 * @param v1 is the direction of the first line.
	 * @param p2 is a point of the second line.
	 * @param v2 is the direction of the second line.
	 */
	public static void getIntersectionPointLineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Point2D intersection){
		
		if(intersection != null){
			float denom = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
			if (denom==0.){
				intersection = null;
				return;//if colinear
			}
			float v1 = (x1*y2-y1*x2);
			float v2 = (x3*y4-y3*x4);
			
			intersection.set(
					(v1*(x3-x4) - (x1-x2)*v2) / denom,
					(v1*(y3-y4) - (y1-y2)*v2) / denom);
		}
		return;
	}

	/**
	 * Replies the relative distance from the given point to the given line.
	 * The replied distance may be negative, depending on which side of 
	 * the line the point is. The sign of the result depends on the order of the points of the line:
	 * <pre><code>distanceRelativePointLine(x, y, x1, y1, x2, y2) == -distanceRelativePointLine(x, y, x2, y2, x1, y1)</code></pre>
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * 
	 * @return the positive or negative distance from the point to the line
	 * @see GeometryUtil#ccw(float, float, float, float, float, float, boolean)
	 * @see #getPointSideOfLine(float, float, float, float, float, float, boolean)
	 * @see #distancePointLine(float, float, float, float, float, float)
	 */
	public static float distanceRelativePointLine(float px, float py, float x1, float y1, float x2, float y2) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0f) return distancePointPoint(px, py, x1, y1);
		float s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (float)(s * Math.sqrt(r_denomenator));
	}

	/**
	 * Replies the projection a point on a segment.
	 * 
	 * @param px
	 *            is the coordinate of the point to project
	 * @param py
	 *            is the coordinate of the point to project
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
	public static float getPointProjectionFactorOnLine(float px, float py, float s1x, float s1y, float s2x, float s2y) {
		assert(s2x!=s1x || s2y!=s1y);
		
		float r_numerator = (px-s1x)*(s2x-s1x) + (py-s1y)*(s2y-s1y);
		float r_denomenator = (s2x-s1x)*(s2x-s1x) + (s2y-s1y)*(s2y-s1y);
		return r_numerator / r_denomenator;
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
	 * @return <code>factor1</code> or {@link Float#NaN} if no intersection.
	 */
	public static float getIntersectionFactorLineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;
	
		// determinant is zero when parallel = det(L1,L2)
		float det = MathUtil.determinant(X1, Y1, X2, Y2);
		if (det == 0.)
			return Float.NaN;
	
		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		return MathUtil.determinant(X2, Y2, x1 - x3, y1 - y3) / det;
	}

	/**
	 * Replies if the given point is inside the given ellipse.
	 * 
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param ellx is the min corner of the ellipse.
	 * @param elly is the min corner of the ellipse.
	 * @param ellw is the width of the ellipse.
	 * @param ellh is the height of the ellipse.
	 * @return <code>true</code> if the point is inside the ellipse;
	 * <code>false</code> if not.
	 */
	public static boolean isInsidePointEllipse(float px, float py, float ellx, float elly, float ellw, float ellh) {
		// Copied from AWT Ellipse2D
		assert(ellw >= 0f);
		assert(ellh >= 0f);
		// Normalize the coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		float normx = (px - ellx) / ellw - 0.5f;
		float normy = (py - elly) / ellh - 0.5f;
		return (normx * normx + normy * normy) < 0.25f;
	}

	/**
	 * Replies if the given point is inside the given ellipse.
	 * 
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @return <code>true</code> if the point is inside the circle;
	 * <code>false</code> if not.
	 */
	public static boolean isInsidePointCircle(float px, float py, float cx, float cy, float radius) {
		return distanceSquaredPointPoint(
				px, py,
				cx, cy) <= (radius * radius);
	}

	/** Replies the closest point from the given point in the solid ellipse.
	 * A solid ellipse is an ellipse with a border and an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @param returnNullWhenInside indicates if a <code>null</code> value
	 * may be returned when the point is inside the ellipse, if
	 * <code>true</code>; or a point all the time if <code>false</code>.
	 * @param closest TODO
	 * @see #closestPointPointShallowEllipse(float, float, float, float, float, float, Point2D)
	 */
	public static void closestPointPointSolidEllipse(float px, float py, float ex, float ey, float ew, float eh, boolean returnNullWhenInside, Point2D closest) {
		
		assert(closest != null);
		assert(ew>=0f);
		assert(eh>=0f);
		
		float x, y;
		Point2D p;
		
		if (ew==0f && eh==0f) {
			x = ex;
			y = ey;
		}else if(ew == 0f || eh == 0f){
			p = new Point2f();
			distancePointSegment(px, py, ex, ey, ex+ew, ey+eh, p);
			x = p.getX();
			y = p.getY();
		}else {
			// Normalize the coordinates compared to the ellipse
			// having a center at 0,0 and a radius of 0.5.
			float normx = (px - ex) / ew - 0.5f;
			float normy = (py - ey) / eh - 0.5f;
			if ((normx * normx + normy * normy) <= 0.25f) {//TODO : to check
				// The point is inside the ellipse
				if (returnNullWhenInside) {
					closest = null;
					return;
				}
				x = px;
				y = py;
			}
			else {
				// Compute the intersection between the ellipse
				// centered on (0;0) and the line (0;0)-(x0;y0)
				float a = ew / 2f;
				float b = eh / 2f;
				float x0 = px - (ex + a);
				float y0 = py - (ey + b);
	
				float denom = a*a*y0*y0 + b*b*x0*x0;
				assert(denom>0f); // because the "inside"-test should discard this case.
	
				denom = (float)Math.sqrt(denom);
				float factor = (a * b) / denom;
				x = factor * x0;
				y = factor * y0;
	
				// Translate the point to the original coordinate system
				x += (ex + a);
				y += (ey + b);
			}
		}
		closest.set(x, y);
		return;
	}

	/** Replies the closest point from the given point in the shallow ellipse.
	 * A shallow ellipse is an ellipse with a border and not an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @param closest TODO
	 */
	public static void closestPointPointShallowEllipse(float px, float py, float ex, float ey, float ew, float eh, Point2D closest) {
		
		assert(closest != null);
		
		float x, y;
		
		if (ew<=0f || eh<=0f) {
			x = ex;
			y = ey;
		}
		else {
			// Compute the intersection between the ellipse
			// centered on (0;0) and the line (0;0)-(x0;y0)
			float a = ew / 2f;
			float b = eh / 2f;
			float x0 = px - (ex + a);
			float y0 = py - (ey + b);
	
			float denom = a*a*y0*y0 + b*b*x0*x0;
			if (denom==0f) {
				// The point is at the center of the ellipse.
				// Replies always the same point.
				closest = null;
				return;
			}
	
			denom = (float)Math.sqrt(denom);
			float factor = (a * b) / denom;
			x = factor * x0;
			y = factor * y0;
	
			// Translate the point to the original coordinate system
			x += (ex + a);
			y += (ey + b);
		}
		closest.set(x, y);
		return;
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectively by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are collinear, see {@link #isEqualLines(float, float, float, float, float, float, float, float)}.
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
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isEqualLines(float, float, float, float, float, float, float, float)
	 * @since 3.0
	 */
	public static boolean isParallelLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4,float epsilon) {
		return GeometryUtil.isCollinearVectors(x2 - x1, y2 - y1, x4 - x3, y4 - y3, epsilon);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectively by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * Line through (x<sub>0</sub>,y<sub>0</sub>,z<sub>0</sub>) in direction (a<sub>0</sub>,b<sub>0</sub>,c<sub>0</sub>) and line through (x<sub>1</sub>,yx<sub>1</sub>,zx<sub>1</sub>) in direction (ax<sub>1</sub>,bx<sub>1</sub>,cx<sub>1</sub>): <center><img src="doc-files/parallellines3d.gif" alt="Parallel lines"></center>
	 * <p>
	 * Two lines specified by point and direction are coplanar if and only if the determinant in the numerator is zero. In this case they are concurrent (if the denominator is nonzero) or parallel (if the denominator is zero).
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
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two given lines are parallel.
	 * @since 3.0
	 */
	public static boolean isParallelLines(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4,float epsilon) {
		return GeometryUtil.isCollinearVectors(x2 - x1, y2 - y1, z2 - z1, x4 - x3, y4 - y3, z4 - z3, epsilon);
	}

	/**
	 * Replies if three points are collinear, ie. one the same line.
	 * <p>
	 * Trivial approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the three given points are collinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(float)
	 */
	public static boolean isCollinearPoints(float x1, float y1, float x2, float y2, float x3, float y3, float epsilon) {
		// Test if three points are collinear
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computations.
		return MathUtil.isEpsilonZero(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2), epsilon);
	}

	/**
	 * Replies if three points are collinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param z1
	 *            is the Z coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param z2
	 *            is the Z coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @param z3
	 *            is the Z coordinate of the third point
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the three given points are collinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(float)
	 */
	public static boolean isCollinearPoints(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3,float epsilon) {
		float dx1 = x2 - x1;
		float dy1 = y2 - y1;
		float dz1 = z2 - z1;
		float dx2 = x3 - x1;
		float dy2 = y3 - y1;
		float dz2 = z3 - z1;
	
		float cx = dy1 * dz2 - dy2 * dz1;
		float cy = dx2 * dz1 - dx1 * dz2;
		float cz = dx1 * dy2 - dx2 * dy1;
	
		float sum = cx * cx + cy * cy + cz * cz;
	
		return MathUtil.isEpsilonZero(sum, epsilon);
	}

	/**
	 * Replies if two lines are collinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(float, float, float, float, float, float, float, float)}.
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
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(float, float, float, float, float, float, float, float)
	 * @see #isCollinearPoints(float, float, float, float, float, float)
	 */
	public static boolean isEqualLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4,float epsilon) {
		return (isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4, epsilon) && isCollinearPoints(x1, y1, x2, y2, x3, y3, epsilon));
	}

	/**
	 * Replies if two lines are collinear.
	 * <p>
	 * The given two lines are described respectively by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(float, float, float, float, float, float, float, float, float, float, float, float)}.
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
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(float, float, float, float, float, float, float, float, float, float, float, float)
	 * @see #isCollinearPoints(float, float, float, float, float, float, float, float, float)
	 */
	public static boolean isEqualLines(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4,float epsilon) {
		return (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, epsilon) && isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3, epsilon));
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 */
	public static boolean clipSegmentToAlignedRectangle(Point2D p1, Point2D p2, float rxmin, float rymin, float rxmax, float rymax) {
		
		float x0 = p1.getX();
		float y0 = p1.getY();
		float x1 = p2.getX();
		float y1 = p2.getY();
 
		int code1 = GeometryUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = GeometryUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		float x, y;
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
				if ((code3 & COHEN_SUTHERLAND_TOP)!=0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				}
				else if ((code3 & COHEN_SUTHERLAND_BOTTOM)!=0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				}
				else if ((code3 & COHEN_SUTHERLAND_RIGHT)!=0) { 
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				}
				else if ((code3 & COHEN_SUTHERLAND_LEFT)!=0) {
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
						code1 = GeometryUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					}
					else {
						x1 = x;
						y1 = y;
						code2 = GeometryUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			p1.set(x0, y0);
			p2.set(x1, y1);
		}
		return accept;
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 */
	public static boolean clipSegmentToRectangle(Point2D p1, Point2D p2, int rxmin, int rymin, int rxmax, int rymax) {

		int x0 = p1.x();
		int y0 = p1.y();
		int x1 = p2.x();
		int y1 = p2.y();
		
		int code1 = GeometryUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = GeometryUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		int x, y;
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
				if ((code3 & COHEN_SUTHERLAND_TOP)!=0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				}
				else if ((code3 & COHEN_SUTHERLAND_BOTTOM)!=0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				}
				else if ((code3 & COHEN_SUTHERLAND_RIGHT)!=0) { 
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				}
				else if ((code3 & COHEN_SUTHERLAND_LEFT)!=0) {
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
						code1 = GeometryUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					}
					else {
						x1 = x;
						y1 = y;
						code2 = GeometryUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			p1.set(x0, y0);
			p2.set(x1, y1);
		}
		return accept;
	}

	/** Compute the interpolate point between the two points.
	 * 
     * 
     * @param px1 is the X coordinate of the first point to test.
     * @param py1 is the Y coordinate of the first point to test.
     * @param px2 is the X coordinate of the second point to test.
     * @param py2 is the Y coordinate of the second point to test.
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 */
	public static Point2D interpolate(float p1x, float p1y, float p2x, float p2y, float factor) {
		
		assert(factor >= 0.f && factor <= 1.f);
		
		float vx = p2x - p1x;
		float vy = p2y - p1y;
		return new Point2f(
				p1x + factor * vx,
				p1y + factor * vy);
	}

	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where 
	 * the specified point {@code (px,py)} lies with respect to the line segment from {@code (x1,y1)}
	 *  to {@code (x2,y2)}. The return value can be either 1, -1, or 0 and indicates in which 
	 *  direction the specified line must pivot around its first end point, {@code (x1,y1)}, in 
	 *  order to point at the specified point {@code (px,py)}.
	 * In other words, given three point P1, P2, and P, is the segments (P1-P2-P) a counterclockwise turn?
	 * <p>
	 * In opposite to {@link #getPointSideOfLine(float, float, float, float, float, float, float)},
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
	 * @param epsilon
	 * 			  the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see relativeDistancePointToLine
	 * @see sidePointLine
	 */
	public static int ccw(float x1, float y1, float x2, float y2, float px, float py, float epsilon) {
		float cx2 = x2 - x1;
		float cy2 = y2 - y1;
		float cpx = px - x1;
		float cpy = py - y1;
		float ccw = cpx * cy2 - cpy * cx2;
		if (MathUtil.isEpsilonZero(ccw, epsilon)) {
			// The point is collinear, classify based on which side of
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

	/** Compute the zone where the point is against the given rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param px is the coordinates of the points.
	 * @param py is the coordinates of the points.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return the zone
	 */
	public static int getCohenSutherlandCode(int px, int py, int rxmin, int rymin, int rxmax, int rymax) {
		assert(rxmin<=rxmax);
		assert(rymin<=rymax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px<rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px>rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py<rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py>rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		return code;
	}

	/** Compute the zone where the point is against the given rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 * 
	 * @param px is the coordinates of the points.
	 * @param py is the coordinates of the points.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return the zone
	 */
	public static int getCohenSutherlandCode(float px, float py, float rxmin, float rymin, float rxmax, float rymax) {
		assert(rxmin<=rxmax);
		assert(rymin<=rymax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px<rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px>rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py<rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py>rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		return code;
	}

	/**
	 * Compute the signed angle between two vectors.
	 * 
	 * @param x1 is the X coordinate of the first vector.
	 * @param y1 is the Y coordinate of the first vector.
	 * @param x2 is the X coordinate of the second vector.
	 * @param y2 is the Y coordinate of the second vector.
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 */
	public static float signedAngle(float x1, float y1, float x2, float y2) {
		float length1 = (float)Math.sqrt(x1 * x1 + y1 * y1);
		float length2 = (float)Math.sqrt(x2 * x2 + y2 * y2);
	
		if ((length1 == 0f) || (length2 == 0f))
			return Float.NaN;
	
		float cx1 = x1;
		float cy1 = y1;
		float cx2 = x2;
		float cy2 = y2;
	
		// A and B are normalized
		if (length1 != 1) {
			cx1 /= length1;
			cy1 /= length1;
		}
	
		if (length2 != 1) {
			cx2 /= length2;
			cy2 /= length2;
		}
	
		/*
		 * // First method // Angle // A . B = |A|.|B|.cos(theta) = cos(theta) float dot = x1 * x2 + y1 * y2; float angle = Math.acos(dot);
		 * 
		 * // On which side of A, B is located? if ((dot > -1) && (dot < 1)) { dot = MathUtil.determinant(x2, y2, x1, y1); if (dot < 0) angle = -angle; }
		 */
	
		// Second method
		// A . B = |A|.|B|.cos(theta) = cos(theta)
		float cos = cx1 * cx2 + cy1 * cy2;
		// A x B = |A|.|B|.sin(theta).N = sin(theta) (where N is the unit vector perpendicular to plane AB)
		float sin = cx1 * cy2 - cy1 * cx2;
	
		float angle = (float)Math.atan2(sin, cos);
	
		return angle;
	}

	/**
	 * Replies if two vectors are colinear. i.e.
	 * if  det( [x1 x2]
	 *	 		[y1 y2] ) = 0
	 * 
	 * <p>
	 * This function uses the equal-to-zero test with the error.
	 * 
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @param epsilon
	 * 			  the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(float)
	 */
	public static boolean isCollinearVectors(float x1, float y1, float x2, float y2, float epsilon) {
		// Do not invoked MathUtil.determinant() to limit computation consumption.
		return MathUtil.isEpsilonZero(x1 * y2 - x2 * y1, epsilon);
	}

	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error.
	 * 
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param z1
	 *            is the Z coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @param z2
	 *            is the Z coordinate of the second vector
	 * @param epsilon 
	 * 			  the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(float)
	 */
	public static boolean isCollinearVectors(float x1, float y1, float z1, float x2, float y2, float z2, float epsilon) {
		// Cross product
		float cx = y1 * z2 - z1 * y2;
		float cy = z1 * x2 - x1 * z2;
		float cz = x1 * y2 - y1 * x2;
	
		float sum = cx * cx + cy * cy + cz * cz;
	
		return MathUtil.isEpsilonZero(sum, epsilon);
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
     * @return the projection of the specified point on the line. If equal to {@code 0}, the projection is equal to the first segment point. If equal to {@code 1}, the projection is equal to the second segment point. If inside {@code ]0;1[}, the projection is between the two segment points. If inside {@code ]-inf;0[}, the projection is outside on the side of the first segment point. If inside {@code ]1;+inf[}, the projection is outside on the side of the second segment point.
     */
    public static float getPointProjectionFactorOnSegment(float px, float py, float s1x, float s1y, float s2x, float s2y) {
            float r_numerator = (px-s1x)*(s2x-s1x) + (py-s1y)*(s2y-s1y);
            float r_denomenator = (s2x-s1x)*(s2x-s1x) + (s2y-s1y)*(s2y-s1y);
            return r_numerator / r_denomenator;
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
     * @return the projection of the specified point on the line. If equal to {@code 0}, the projection is equal to the first segment point. If equal to {@code 1}, the projection is equal to the second segment point. If inside {@code ]0;1[}, the projection is between the two segment points. If inside {@code ]-inf;0[}, the projection is outside on the side of the first segment point. If inside {@code ]1;+inf[}, the projection is outside on the side of the second segment point.
     */
    public static float getPointProjectionFactorOnSegment(float px, float py, float pz, float s1x, float s1y, float s1z, float s2x, float s2y, float s2z) {
            float dx = s2x - s1x;
            float dy = s2y - s1y;
            float dz = s2z - s1z;

            if (dx == 0. && dy == 0. && dz == 0.)
                    return 0.f;

            return ((px - s1x) * dx + (py - s1y) * dy + (pz - s1z) * dz) / (dx * dx + dy * dy + dz * dz);
    }
    
    
    /**
     * Compute and replies the euclidian distance between the two points represented by the parameters.
     * 
     * @param x1 is the X coordinate of the first point to test.
     * @param y1 is the Y coordinate of the first point to test.
     * @param z1 is the Z coordinate of the first point to test.
     * @param x2 is the X coordinate of the second point to test.
     * @param y2 is the Y coordinate of the second point to test.
     * @param z2 is the Z coordinate of the second point to test.
     * @return the distance between the two given points
     */
    public static float distancePointPoint(float x1, float y1, float z1, float x2, float y2, float z2) {
            float dx = x1 - x2;
            float dy = y1 - y2;
            float dz = z1 - z2;
            return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

	/**
	 * Given a point p, this function computes the point q1 on (or in) 
	 * this OBB, closest to p and the point q2 on farthest to p.
	 * 
	 * @param boxCenterx is the X coordinate of the box center.
	 * @param boxCentery is the Y coordinate of the box center.
	 * @param boxCenterz is the Z coordinate of the box center.
	 * @param boxAxis1x is the X coordinate of the Axis1 unit vector.
	 * @param boxAxis1y is the Y coordinate of the Axis1 unit vector.
	 * @param boxAxis1z is the Z coordinate of the Axis1 unit vector.
	 * @param boxAxis2x is the X coordinate of the Axis2 unit vector.
	 * @param boxAxis2y is the Y coordinate of the Axis2 unit vector.
	 * @param boxAxis2z is the Z coordinate of the Axis2 unit vector.
	 * @param boxAxis3x is the X coordinate of the Axis3 unit vector.
	 * @param boxAxis3y is the Y coordinate of the Axis3 unit vector.
	 * @param boxAxis3z is the Z coordinate of the Axis3 unit vector.
	 * @param boxExtentAxis1 is the 'Axis1' size of the OBB.
	 * @param boxExtentAxis2 is the 'Axis2' size of the OBB.
	 * @param boxExtentAxis3 is the 'Axis3' size of the OBB.
	 * @param px is the X coordinate of the point to test.
	 * @param py is the Y coordinate of the point to test.
	 * @param pz is the Z coordinate of the point to test.
	 * @param q1 is the closest point or <var>q1</var>, or <code>null</code>.
	 * @param q2 is the farthest point or <var>q2</var>, or <code>null</code>.
	 */
	public static void closestFarthestPointsOBBPoint(
				float boxCenterx, float boxCentery, float boxCenterz,
				float boxAxis1x, float boxAxis1y, float boxAxis1z,			
				float boxAxis2x, float boxAxis2y, float boxAxis2z,
				float boxAxis3x, float boxAxis3y, float boxAxis3z,
				float extent1, float extent2, float extent3,
				float px, float py, float pz,
				Point3D q1, Point3D q2) {
		
		assert(extent1>=0);
		assert(extent2>=0);
		assert(extent3>=0);
		//assert(vecteur unitaire)
		assert(q1!=null||q2!=null);
		
		float dx,dy,dz;
		dx = px - boxCenterx;
		dy = py - boxCentery;
		dz = pz - boxCenterz;
	
		// Start results at center of box; make steps from there
		if (q1!=null) q1.setX(boxCenterx); q1.setY(boxCentery); q1.setZ(boxCenterz);
		if (q2!=null) q2.setX(boxCenterx); q2.setY(boxCentery); q2.setZ(boxCenterz);		
		
		// For each OBB axis project d onto that axis to get the distance along the axis of d from the box center
		float d1 = MathUtil.dotProduct(dx, dy, dz, boxAxis1x, boxAxis1y, boxAxis1z);
		float d2 = MathUtil.dotProduct(dx, dy, dz, boxAxis2x, boxAxis2y, boxAxis2z);
		float d3 = MathUtil.dotProduct(dx, dy, dz, boxAxis3x, boxAxis3y, boxAxis3z);
			
		if (q1!=null) {
			// 
			// Step that distance along the axis to get world coordinate
			//q1 += dist * this.axis[i]; (If distance farther than the box extents, clamp to the box)
			q1.setX(MathUtil.clamp(d1,-extent1,extent1)*boxAxis1x +
					MathUtil.clamp(d2,-extent2,extent2)*boxAxis2x +
					MathUtil.clamp(d3,-extent3,extent3)*boxAxis3x);
			
			q1.setY(MathUtil.clamp(d1,-extent1,extent1)*boxAxis1y +
					MathUtil.clamp(d2,-extent2,extent2)*boxAxis2y +
					MathUtil.clamp(d3,-extent3,extent3)*boxAxis3y);
			
			q1.setZ(MathUtil.clamp(d1,-extent1,extent1)*boxAxis1z +
					MathUtil.clamp(d2,-extent2,extent2)*boxAxis2z +
					MathUtil.clamp(d3,-extent3,extent3)*boxAxis3z);
		}
	
		if (q2!=null) {
			// Clamp to the other side of the box
			if (d1 >= 0.) d1 = -extent1; else d2 = extent1;
			if (d2 >= 0.) d2 = -extent2; else d2 = extent2;
			if (d3 >= 0.) d3 = -extent3; else d2 = extent3;
			
			// Step that distance along the axis to get world coordinate
			//q2 += dist * this.axis[i];
			q1.setX(d1*boxAxis1x + d2*boxAxis2x + d3*boxAxis3x);
			q1.setY(d1*boxAxis1y + d2*boxAxis2y + d3*boxAxis3y);
			q1.setZ(d1*boxAxis1z + d2*boxAxis2z + d3*boxAxis3z);
		}
	}

	/**
	 * Given a point p, this function computes the point q1 on (or in) 
	 * this OBR, closest to p and the point q2 on farthest to p.
	 * @param px is the X coordinate of the point to test.
	 * @param py is the Y coordinate of the point to test.
	 * @param boxCenterx is the X coordinate of the box center.
	 * @param boxCentery is the Y coordinate of the box center.
	 * @param boxAxis1x is the X coordinate of the Axis1 unit vector.
	 * @param boxAxis1y is the Y coordinate of the Axis1 unit vector.
	 * @param boxAxis2x is the X coordinate of the Axis2 unit vector.
	 * @param boxAxis2y is the Y coordinate of the Axis2 unit vector.
	 * @param boxExtentAxis1 is the 'Axis1' size of the OBB.
	 * @param boxExtentAxis2 is the 'Axis2' size of the OBB.
	 * @param closest is the closest point or <var>q1</var>, or <code>null</code>.
	 * @param farthest is the farthest point or <var>q2</var>, or <code>null</code>.
	 */
	public static void closestFarthestPointsOBRPoint(
			float px, float py,
			float boxCenterx, float boxCentery,			
			float boxAxis1x, float boxAxis1y,
			float boxAxis2x, float boxAxis2y,
			float extent1, float extent2,
			Point2D q1, Point2D q2) {
	
	assert(extent1>=0);
	assert(extent2>=0);
	//assert(vecteur unitaire)
	assert(q1!=null||q2!=null);
	
	float dx,dy;
	dx = px - boxCenterx;
	dy = py - boxCentery;

	// Start results at center of box; make steps from there
	if (q1!=null) q1.setX(boxCenterx); q1.setY(boxCentery);
	if (q2!=null) q2.setX(boxCenterx); q2.setY(boxCentery);	
	
	// For each OBB axis project d onto that axis to get the distance along the axis of d from the box center
	float d1 = MathUtil.dotProduct(dx, dy, boxAxis1x, boxAxis1y);
	float d2 = MathUtil.dotProduct(dx, dy, boxAxis2x, boxAxis2y);
		
	if (q1!=null) {
		// 
		// Step that distance along the axis to get world coordinate
		//q1 += dist * this.axis[i]; (If distance farther than the box extents, clamp to the box)
		q1.setX(MathUtil.clamp(d1,-extent1,extent1)*boxAxis1x +
				MathUtil.clamp(d2,-extent2,extent2)*boxAxis2x);
		
		q1.setY(MathUtil.clamp(d1,-extent1,extent1)*boxAxis1y +
				MathUtil.clamp(d2,-extent2,extent2)*boxAxis2y);
	}

	if (q2!=null) {
		// Clamp to the other side of the box
		if (d1 >= 0.) d1 = -extent1; else d2 = extent1;
		if (d2 >= 0.) d2 = -extent2; else d2 = extent2;		
		// Step that distance along the axis to get world coordinate
		//q2 += dist * this.axis[i];
		q1.setX(d1*boxAxis1x + d2*boxAxis2x);
		q1.setY(d1*boxAxis1y + d2*boxAxis2y);
	}
}
	
    /**
     * Computes closest points C1 and C2 of S1(s)=P1+s*(Q1-P1) and S2(t)=P2+t*(Q2-P2), returning s and t. 
     * Function result is squared distance between between S1(s) and S2(t) 
     * see Real Time Collision book page 149
     * @param p1x
     *            - x coordinate of the first point of the first segment.
     * @param p1y
     *            - y coordinate of the first point of the first segment.
     * @param p1z
     *            - z coordinate of the first point of the first segment.
     * @param q1x
     *            - x coordinate of the second point of the first segment.
     * @param q1y
     *            - y coordinate of the second point of the first segment.
     * @param q1z
     *            - z coordinate of the second point of the first segment.
     * @param p2x
     *            - x coordinate of the first point of the second segment.
     * @param p2y
     *            - y coordinate of the first point of the second segment.
     * @param p2z
     *            - z coordinate of the first point of the second segment.
     * @param q2x
     *            - x coordinate of the second point of the second segment.
     * @param q2y
     *            - y coordinate of the second point of the second segment.
     * @param q2z
     *            - z coordinate of the second point of the second segment.
     * @param s is the ratio on the first segment. Could be <code>null</code>
     * @param t is the ratio on the second segment. Could be <code>null</code>
     * @param c1
     *            - the first closest point, filled by this method. Could be <code>null</code>.
     * @param c2
     *            - the second closest point, filled by this method. Could be <code>null</code>.
     * @return squared distance between between S1(s) and S2(t)
     * @since 4.0
     */
    public static float closestPointsSegmentSegment(
                    float p1x, float p1y, float p1z,
                    float q1x, float q1y, float q1z,
                    float p2x, float p2y, float p2z,
                    float q2x, float q2y, float q2z,
                    Point3f c1, Point3f c2) {
    	//TODO : Put expression like q1x-p1x into a variable (better performances?) 
            float s, t;
            float a = GeometryUtil.distanceSquaredPointPoint(q1x, q1y, q1z, p1x, p1y, p1z); // Squared length of segment S1, always nonnegative
            float e = GeometryUtil.distanceSquaredPointPoint(q2x, q2y, q2z, p2x, p2y, p2z); // Squared length of segment S2, always nonnegative
            float f = MathUtil.dotProduct(q2x-p2x, q2y-p2y, q2z-p2z, p1x-p2x, p1y-p2y, p1z-p2z);

            // Check if either or both segments degenerate into points
            if (a <= JVM_MIN_FLOAT_EPSILON && e <= JVM_MIN_FLOAT_EPSILON) {  		
                    // Both segments degenerate into points
                    c1.set(p1x, p1y, p1z);
                    c2.set(p2x, p2y, p2z);
                    return distanceSquaredPointPoint(p1x, p1y, p1z, p2x, p2y, p2z);
            }

            if (a <= JVM_MIN_FLOAT_EPSILON) {
                    // First segment degenerates into a point
                    s = 0.f;
                    t = MathUtil.clamp(f / e, 0.f, 1.f);// s = 0 => t = (b*s + f) / e = f / e
            } else {
                    float c = MathUtil.dotProduct(q1x-p1x, q1y-p1y, q1z-p1z, p1x-p2x, p1y-p2y, p1z-p2z);
                    if (e <= JVM_MIN_FLOAT_EPSILON) {
                            // Second segment degenerates into a point
                    		t = 0.f;
                            s = MathUtil.clamp(-c / a, 0.f, 1.f); // t = 0 => s = (b*t - c) / a = -c / a
                    } else {
                            // The general nondegenerate case starts here
                            float b = MathUtil.dotProduct(q1x-p1x, q1y-p1y, q1z-p1z,q2x-p2x, q2y-p2y, q2z-p2z);
                            float denom = a * e - b * b; // Always nonnegative
                            // If segments not parallel, compute closest point on L1 to L2 and
                            // clamp to segment S1. Else pick arbitrary s (here 0)
                            if (denom != 0.f) {
                                    s = MathUtil.clamp((b * f - c * e) / denom, 0.f, 1.f);
                            } else
                                    s = 0.f;
                            // Compute point on L2 closest to S1(s) using
                            // t = Dot((P1 + D1*s) - P2,D2) / Dot(D2,D2) = (b*s + f) / e
                            t = (b * s + f) / e;
                            // If t in [0,1] done. Else clamp t, recompute s for the new value
                            // of t using s = Dot((P2 + D2*t) - P1,D1) / Dot(D1,D1)= (t*b - c) / a
                            // and clamp s to [0, 1]
                            
                            if (t < 0.f) {
                                    t = 0.f;
                                    s = MathUtil.clamp(-c / a, 0.f, 1.f);
                            } else if (t > 1.f) {
                                    t = 1.f;
                                    s = MathUtil.clamp((b - c) / a, 0.f, 1.f);
                            }
                    }
            }
            //TODO reuse variables  a & f.
            if(c1 != null){
	            // c1 = p1 + d1 * s;
	            c1.set(p1x + (q1x-p1x)*s , p1y + (q1y-p1y)*s, p1z + (q1z-p1z)*s);
            }
            
            if(c2 != null){
                // c2 = p2 + d2 * t;
	            c2.set(p2x + (q2x-p2x)*s , p2y + (q2y-p2y)*s, p2z + (q2z-p2z)*s);
            }
            return distanceSquaredPointPoint(p1x + (q1x-p1x)*s , p1y + (q1y-p1y)*s, p1z + (q1z-p1z)*s,
            								   p2x + (q2x-p2x)*s , p2y + (q2y-p2y)*s, p2z + (q2z-p2z)*s);

    }
    
    /**
     * Computes the distance from a line segment AB to a line segment CD
     * 
	 * @param Ax is the X coordinate of segment AB start point
	 * @param Ay is the Y coordinate of segment AB start point
	 * @param Bx is the X coordinate of segment AB start point
	 * @param By is the Y coordinate of segment AB start point
     * @param Cx is the X coordinate of segment CD start point
     * @param Cy is the Y coordinate of segment CD start point
     * @param Dx is the X coordinate of segment CD start point
     * @param Dy is the Y coordinate of segment CD start point
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
     * @return the distance
     */
    public static float distanceSegmentSegment(float Ax, float Ay, float Bx, float By, float Cx, float Cy, float Dx, float Dy, float epsilon) {

            // check for zero-length segments
            float d_a_cd = distancePointSegment(Ax, Ay, Cx, Cy, Dx, Dy,null);
            if (MathUtil.isEpsilonEqual(Ax, Bx, epsilon)&&MathUtil.isEpsilonEqual(Ay, By, epsilon))
                    return d_a_cd;
            float d_c_ab = distancePointSegment(Cx, Cy, Ax, Ay, Bx, By,null);
            if (MathUtil.isEpsilonEqual(Cx, Dx, epsilon)&&MathUtil.isEpsilonEqual(Cy, Dy, epsilon))
                    return d_c_ab;

            float Sx;
            float Sy;

            float pCD = (Cy - Dy) / (Cx - Dx);
            float pAB = (Ay - By) / (Ax - Bx);
            float oCD = Cy - pCD * Cx;
            float oAB = Ay - pAB * Ax;
            Sx = (oAB - oCD) / (pCD - pAB);
            Sy = pCD * Sx + oCD;

            if ((Sx < Ax && Sx < Bx) | (Sx > Ax && Sx > Bx) | (Sx < Cx && Sx < Dx) | (Sx > Cx && Sx > Dx) | (Sy < Ay && Sy < By) | (Sy > Ay && Sy > By) | (Sy < Cy && Sy < Dy) | (Sy > Cy && Sy > Dy)) {
                    // No intersection
                    float d_b_cd = distancePointSegment(Bx, By, Cx, Cy, Dx, Dy,null);
                    float d_d_ab = distancePointSegment(Dx, Dy, Ax, Ay, Bx, By,null);
                    return MathUtil.min(d_a_cd, d_b_cd, d_c_ab, d_d_ab);
            }
            return 0.f; // intersection exists
    }

	/** Replies if a rectangle is inside in the circle.
	 * 
	 * @param rx is the lowest corner of the rectangle.
	 * @param ry is the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @return <code>true</code> if the given rectangle is inside the circle;
	 * otherwise <code>false</code>.
	 */
	public static boolean isInsideRectangleCircle(float rx, float ry, float rwidth, float rheight, float cx, float cy, float radius) {
		float rcx = (rx + rwidth/2f);
		float rcy = (ry + rheight/2f);
		float farX;
		if (cx<=rcx) farX = rx + rwidth;
		else farX = rx;
		float farY;
		if (cy<=rcy) farY = ry + rheight;
		else farY = ry;
		return isInsidePointCircle(farX, farY, cx, cy, radius);
	}

	/** Replies if a rectangle is inside in the ellipse.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ewidth is the width of the ellipse.
	 * @param eheight is the height of the ellipse.
	 * @param rx is the lowest corner of the rectangle.
	 * @param ry is the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	public static boolean isInsideRectangleEllipse(float ex, float ey, float ewidth, float eheight, float rx, float ry, float rwidth, float rheight) {
		float ecx = (ex + ewidth/2f);
		float ecy = (ey + eheight/2f);
		float rcx = (rx + rwidth/2f);
		float rcy = (ry + rheight/2f);
		float farX;
		if (ecx<=rcx) farX = rx + rwidth;
		else farX = rx;
		float farY;
		if (ecy<=rcy) farY = ry + rheight;
		else farY = ry;
		return isInsidePointEllipse(farX, farY, ex, ey, ewidth, eheight);
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
	 * This function uses the equal-to-zero test with the error.
	 * <p>
	 * In opposite of {@link #ccw(float, float, float, float, float, float, boolean)},
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
	 * @param epsilon
	 * 			  the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see #relativeDistancePointToLine(float, float, float, float, float, float)
	 * @see #isEpsilonZero(float)
	 * @see #ccw(float, float, float, float, float, float, boolean)
	 */
	public static int getPointSideOfLine(float x1, float y1, float x2, float y2, float px, float py, float epsilon) {
	        float cx2 = x2 - x1;
	        float cy2 = y2 - y1;
	        float cpx = px - x1;
	        float cpy = py - y1;
	        float side = cpx * cy2 - cpy * cx2;
	        if (MathUtil.isEpsilonZero(side, epsilon)) {
	                side = 0f;
	        }
	        return (side < 0f) ? -1 : ((side > 0f) ? 1 : 0);
	}

    /**
     * Compute and replies the perpendicular distance from a point to a line.
     * <p>
     * Call the point in space <code>(i,j,k)</code> and let <code>(a,b,c)</code> and <code>(x,y,z)</code> be two points on the line (call it line A). The crucial fact we'll use is that the minimum distance between the point and the line is the perpendicular distance. So we're looking for a point <code>(L,M,N)</code> which is on A, and such that the line connecting <code>(L,M,N)</code> and <code>(i,j,k)</code> is perpendicular to A.
     * <p>
     * See <a href="#pointsegmentdetails">Point Segment Geometry</a> for details.
     * 
     * @param px
     *            is the X coord of the point
     * @param py
     *            is the Y coord of the point
     * @param pz
     *            is the Z coord of the point
     * @param sx1
     *            is the X coord of the first point of the segment
     * @param sy1
     *            is the Y coord of the first point of the segment
     * @param sz1
     *            is the Z coord of the first point of the segment
     * @param sx2
     *            is the X coord of the second point of the segment
     * @param sy2
     *            is the Y coord of the second point of the segment
     * @param sz2
     *            is the Z coord of the second point of the segment
     * @return the distance
     */
    public static float distancePointLine(float px, float py, float pz, float sx1, float sy1, float sz1, float sx2, float sy2, float sz2) {
            // We use the method of the parallelogram
            // P
            // o------+------o
            // \ | \
            // \ |h \
            // \ | \
            // ------o--+----------o-----------
            // S1 S2

            // vector S1S2 = <vx1, vy1, vz1>
            float vx1 = sx2 - sx1;
            float vy1 = sy2 - sy1;
            float vz1 = sz2 - sz1;

            // vector S1P = <vx2, vy2, vz2>
            float vx2 = px - sx1;
            float vy2 = py - sy1;
            float vz2 = pz - sz1;

            // Let's take the cross product S1S2 X S1P.
            Vector3f cross = MathUtil.crossProductRightHand(vx1, vy1, vz1, vx2, vy2, vz2);

            // Let's let (a) be the length of S1S2 X S1P.
            float a = cross.length();

            // If you divide (a) by the distance S1S2 you will get the distance of P
            // from line S1S2.
            float s1s2 = (float) Math.sqrt(vx1 * vx1 + vy1 * vy1 + vz1 * vz1);

            // Thus the distance we are looking for is a/s1s2.
            return a / s1s2;
    }
	
    
    public static void computeClosestFarestOBBPoints(
            Point3f center, Vector3f[] axis,  float[] extent,
            Point3f p, Point3f closest, Point3f farest) {
    assert(axis.length==3);
    assert(extent.length==3);
    assert(extent[0]>=0);
    assert(extent[1]>=0);
    assert(extent[2]>=0);

    Vector3f d = new Vector3f();
        d.sub(p, center); // d = p - center

        // Start results at center of box; make steps from there
        if (closest!=null) closest.set(center);                
        if (farest!=null) farest.set(center);                
        
        // For each OBB axis...
        float d1, d2;
        
        for (int i=0; i<axis.length; ++i) {
                // ...project d onto that axis to get the distance along the axis of d from the box center
                d1 = d2 = d.dot(axis[i]);
                
                if (closest!=null) {
                        // If distance farther than the box extents, clamp to the box
                        if (d1 > extent[i]) d1 = extent[i];
                        else if (d1 < -extent[i]) d1 = -extent[i];

                        // Step that distance along the axis to get world coordinate
                        //q += dist * this.axis[i];
                        closest.scaleAdd(d1, axis[i], closest);
                }

                if (farest!=null) {
                        // Clamp to the other side of the box
                        if (d2 >= 0.) d2 = - extent[i];
                        else d2 = extent[i];
                        
                        // Step that distance along the axis to get world coordinate
                        //q += dist * this.axis[i];
                        farest.scaleAdd(d2, axis[i], farest);
                }
        }
    }
    
    public static Iterable<Point2D> getProjectionOnXYPlane(Iterable<? extends Point3D> points) {
    	return new PointFilterXYIterable(points);
    }

    public static Iterable<Point2D> getProjectionOnXZPlane(Iterable<? extends Point3D> points) {
    	return new PointFilterXZIterable(points);
    }

    public static Iterable<Point2D> getProjectionOnYZPlane(Iterable<? extends Point3D> points) {
    	return new PointFilterYZIterable(points);
    }

    public static float distanceL1PointPoint(float x1, float y1, float x2, float y2){
    	return Math.abs(x1 - x2)+Math.abs(y1 - y2);
    }
    
    public static float distanceLInfPointPoint(float x1, float y1, float x2, float y2){
    	return  MathUtil.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

	public static void closestPointPointCircle(float x , float y, float cx, float cy , float radius, Point2f closest) {
		
		assert(closest != null);
		assert(radius > 0);
		
		float dx, dy;
		
		dx = x - cx;
		dy = y - cy;
		
		float l = distanceSquaredPointPoint(x, y, cx, cy);
		if (l<=(radius*radius)) {
			if (closest != null)
				closest.set(x,y);
		}
		float s = radius/(float)Math.sqrt(l);

		if (closest != null)
			closest.set(cx + s*dx, cy + s*dy);
	}

	/** Replies if a rectangle is inside in the rectangle.
	 * 
	 * @param min1x is the lowest corner of the enclosing-candidate rectangle.
	 * @param min1y is the lowest corner of the enclosing-candidate rectangle.
	 * @param max1x is the width of the enclosing-candidate rectangle.
	 * @param max1y is the height of the enclosing-candidate rectangle.
	 * @param min2x is the lowest corner of the inner-candidate rectangle.
	 * @param min2y is the lowest corner of the inner-candidate rectangle.
	 * @param max2x is the width of the inner-candidate rectangle.
	 * @param max2y is the height of the inner-candidate rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	public static boolean isInsideRectangleRectangle(float min1x, float min1y, float max1x, float max1y, float min2x, float min2y, float max2x, float max2y) {
		
		assert(max2x >= min2x && max2y >= min2y);
		
		if (max1x==0f || max1y==0) {
			return false;
		}
		return (min2x >= min1x &&
				min2y >= min1y &&
				max2x <= max1x &&
				max2y <= max1y);
	}


	public static boolean isInsidePointRectangle(float x, float y, float minX, float minY, float maxX, float maxY) {
		assert(maxX >= minX && maxY >= minY);
		
		return (x>=minX && x<=maxX)
				&&
				(y>=minY && y<=maxY);
	}

	public static float distanceSquaredPointRectangle(float x , float y,  float minX, float minY, float maxX, float maxY) {
		
		assert(maxX >= minX && maxY >= minY);
		
		float dx;
		if (x<minX) {
			dx = minX - x;
		}
		else if (x>maxX) {
			dx = x - maxX;
		}
		else {
			dx = 0f;
		}
		float dy;
		if (y<minY) {
			dy = minY - y;
		}
		else if (y>maxY) {
			dy = y - maxY;
		}
		else {
			dy = 0f;
		}
		return dx*dx+dy*dy;
	}
	
	
	public static void closestPointPointRectangle(float x, float y , float minX, float minY, float maxX, float maxY, Point2D closest) {
		
		assert(maxX >= minX && maxY >= minY);
		assert(closest!= null);
				
		x = MathUtil.clamp(x, minX, maxX);

		y = MathUtil.clamp(y, minY, maxY);
		
		closest.set(x,y);
	}
	
	/** Replies if a rectangle is inside a round rectangle.
	 * 
	 * @param minX1 is the lowest corner of the inner-candidate rectangle.
	 * @param minY1 is the lowest corner of the inner-candidate rectangle.
	 * @param maxX1 is the width of the inner-candidate rectangle.
	 * @param maxY1 is the height of the inner-candidate rectangle.
	 * @param minX2 is the lowest corner of the round rectangle.
	 * @param minY2 is the lowest corner of the round rectangle.
	 * @param maxX2 is the width of the round rectangle.
	 * @param maxY2 is the height of the round rectangle.
	 * @param awidth is the width of the arc of the round rectangle.
	 * @param aheight is the height of the arc of the round rectangle.
	 * @return <code>true</code> if the given rectangle is inside the given round rectangle;
	 * otherwise <code>false</code>.
	 */
	public static boolean isInsideRectangleRoundRectangle(float minX1, float minY1, float maxX1, float maxY1, float minX2, float minY2, float maxX2, float maxY2, float awidth, float aheight) {
		
		assert(maxX1 >= minX1 && maxY1 >= minY1);
		assert(maxX2 >= minX2 && maxY2 >= minY2);
		
		if(isInsideRectangleRectangle(minX1, minY1, maxX1, maxY1, minX2, minY2, maxX2, maxY2))
			return false;
		
		float farX, farY;
		
		if((minX1-minX2) < (maxX2 -maxX1))
			farX = minX1;
		else
			farX = maxX1;
		
		if((minY1-minY2) < (maxY2 -maxY1))
			farY = minY1;
		else
			farY = maxY1;
		
		return isInsidePointRoundRectangle(minX2, minY2, maxX2, maxY2, awidth, aheight, farX, farY);
	}

	/** Replies if a point is inside in the round rectangle.
	 * 
	 * @param px is the point.
	 * @param py is the point.
	 * @param rx is the lowest corner of the round rectangle.
	 * @param ry is the lowest corner of the round rectangle.
	 * @param rwidth is the width of the round rectangle.
	 * @param rheight is the height of the round rectangle.
	 * @param awidth is the width of the arc of the round rectangle.
	 * @param aheight is the height of the arc of the round rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	public static boolean isInsidePointRoundRectangle(float x, float y , float minX, float minY, float maxX, float maxY, float awidth, float aheight) {
		
		assert(maxX >= minX && maxY >= minY);
		
		if (minX == maxX && minY == minY) {
			return x==minX && y==minY;
		}

		// Check for trivial rejection - point is outside bounding rectangle
		if (x < minX || y < minY || x >= maxX || y >= maxY) {
			return false;
		}
		float aw = Math.min(maxX -minX, Math.abs(awidth)) / 2f;
		float ah = Math.min(maxY - minY, Math.abs(aheight)) / 2f;
		// Check which corner point is in and do circular containment
		// test - otherwise simple acceptance
		if (x >= (minX += aw) && x < (minX = maxX - aw)) {
			return true;
		}
		if (y >= (minY += ah) && y < (minY = maxY - ah)) {
			return true;
		}
		float xx = (x - minX) / aw;
		float yy = (y - minY) / ah;
		return (xx * xx + yy * yy <= 1f);
	}
	
	
	public static void closestPointPointRoundRectangle(float px, float py, float rx1, float ry1, float rx2, float ry2, float aw, float ah, Point2D closest) {

		assert(closest != null);
		
		float x, y;

		if (px<rx1+aw) {
			if (py<ry1+ah) {
				GeometryUtil.closestPointPointSolidEllipse(
						px, py,
						rx1, ry1,
						aw, ah, false, closest);
			}
			if (py>ry2-ah) {
				GeometryUtil.closestPointPointSolidEllipse(
						px, py,
						rx1, ry2-ah,
						aw, ah, false, closest);
				return;
			}
		}
		else if (px>rx2-aw) {
			if (py<ry1+ah) {
				GeometryUtil.closestPointPointSolidEllipse(
						px, py,
						rx2-aw, ry1,
						aw, ah, false, closest);
				return;
			}
			if (py>ry2-ah) {
				GeometryUtil.closestPointPointSolidEllipse(
						px, py,
						rx2-aw, ry2-ah,
						aw, ah, false, closest);
				return;
			}
		}

		x = MathUtil.clamp(px, rx1, rx2);

		y = MathUtil.clamp(py, ry1, ry2);
		
		closest.set(x,y);
		
		return;
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
			float px, float py,
			float x0, float y0,
			float x1, float y1) {
		// Copied from AWT API
		if (py <  y0 && py <  y1) return 0;
		if (py >= y0 && py >= y1) return 0;
		// assert(y0 != y1);
		if (px >= x0 && px >= x1) return 0;
		if (px <  x0 && px <  x1) return (y0 < y1) ? 1 : -1;
		float xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
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
	 * This function differs from {@link #computeCrossingsFromPoint(float, float, float, float, float, float)}.
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
			float px, float py,
			float x0, float y0,
			float x1, float y1) {
		// Copied from AWT API
		if (py <  y0 && py <  y1) return 0;
		if (py > y0 && py > y1) return 0;
		// assert(y0 != y1);
		if (px > x0 && px > x1) return 0;
		if (px <  x0 && px <  x1) return (y0 < y1) ? 1 : -1;
		float xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
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
			float sx1, float sy1,
			float sx2, float sy2,
			float x0, float y0,
			float x1, float y1) {
		int numCrosses = crossings;
	
		float xmin = Math.min(sx1, sx2);
		float xmax = Math.max(sx1, sx2);
		float ymin = Math.min(sy1, sy2);
		float ymax = Math.max(sy1, sy2);
	
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
		else if (IntersectionUtil.intersectsSegmentSegmentWithoutEnds(x0, y0, x1, y1, sx1, sy1, sx2, sy2)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			int side1, side2;
			if (sy1<=sy2) {
				side1 = getPointSideOfLine(sx1, sy1, sx2, sy2, x0, y0, 0f);
				side2 = getPointSideOfLine(sx1, sy1, sx2, sy2, x1, y1, 0f);
			}
			else {
				side1 = getPointSideOfLine(sx2, sy2, sx1, sy1, x0, y0, 0f);
				side2 = getPointSideOfLine(sx2, sy2, sx1, sy1, x1, y1, 0f);
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
			float ex, float ey,
			float ew, float eh,
			float x0, float y0,
			float x1, float y1) {
		int numCrosses = crossings;
	
		float xmin = ex;
		float ymin = ey;
		float xmax = ex + ew;
		float ymax = ey + eh;
	
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
		else if (IntersectionUtil.intersectsEllipseSegment(
				xmin, ymin, xmax-xmin, ymax-ymin,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		else {
			float xcenter = (xmin+xmax)/2f;
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
			float cx, float cy,
			float radius,
			float x0, float y0,
			float x1, float y1) {
		int numCrosses = crossings;
	
		float xmin = cx - Math.abs(radius);
		float ymin = cy - Math.abs(radius);
		float ymax = cy + Math.abs(radius);
	
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
		else if (IntersectionUtil.intersectsCircleSegment(
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
	 * for the {@link MathUtil#SHAPE_INTERSECTS} constant for more complete details.
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
			float rxmin, float rymin,
			float rxmax, float rymax,
			float x0, float y0,
			float x1, float y1) {
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
		float xi0 = x0;
		if (y0 < rymin) {
			xi0 += ((rymin - y0) * (x1 - x0) / (y1 - y0));
		}
		else if (y0 > rymax) {
			xi0 += ((rymax - y0) * (x1 - x0) / (y1 - y0));
		}
		float xi1 = x1;
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

	/**
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
	 * 
	 * @see MathUtil#isEpsilonZero(float)
	 */
	public static boolean isInsidePointSegment(float x, float y, float ax, float ay, float bx, float by, float epsilon) {
		return MathUtil.isEpsilonZero(GeometryUtil.distanceSquaredPointSegment(x, y, ax, ay, bx, by, null), epsilon);
	}


	public static void closestPointPointSegment(float x, float y, float ax, float ay,float bx, float by, Point2D closest) {

		assert(closest != null);
		
		float ratio = GeometryUtil.getPointProjectionFactorOnLine(x, y, ax, ay, bx, by);
		ratio = MathUtil.clamp(ratio, 0, 1);
		closest.set(ax + (bx - ax) * ratio, ay + (by - ay) * ratio); 
		return;
	}

	public static boolean isInsidePointPointOrientedRectangle(float x, float y, float cx, float cy, float rx,
			float ry, float sx, float sy, float extentR, float extentS) {
		
		float px, py;
		//Changing P(x,y) coordinate basis.
		px = (x-cx) * sy - (y-cy) * sx;
		py = (y-cy) * rx - (x-cx) * ry;
		
		return GeometryUtil.isInsidePointRectangle(px, py,extentR/-2, extentS/-2, extentR/2, extentS/2);
	}
	
	public static boolean isInsideRectangleRectangleOrientedRectangle(
			float minx, float miny, float maxx, float maxy,
			float cx, float cy, float rx, float ry, float sx, float sy, float extentR, float extentS) {
		
		if(GeometryUtil.isInsidePointPointOrientedRectangle(1.5f*minx + maxx, 1.5f*miny + maxy, cx, cy, rx, ry, sx, sy, extentR, extentS))
			return !IntersectionUtil.intersectsAlignedRectangleOrientedRectangle(
				minx, miny, maxy, maxy,
				cx, cy, rx, ry, sx, sy, extentR, extentS);
		return false;
	}
	
	
	 /**
     * Compute the covariance matrix for the given set of points.
     * <p>
     * The computed matrix is symmetric.
     * 
     * @param cov is the matrix to fill with the covariance elements.
     * @param points are the points.
     * @return the mean of the points.
     */
    public static Point3f cov(Matrix3f cov, Point3f... points) {
            return cov(cov, Arrays.asList(points));
    }

    /**
     * Compute the covariance matrix for the given set of points.
     * <p>
     * The computed matrix is symmetric.
     * 
     * @param cov is the matrix to fill with the covariance elements.
     * @param points are the points.
     * @return the mean of the points.
     */
    public static Point2f cov(Matrix2f cov, Point2f... points) {
            return cov(cov, Arrays.asList(points));
    }

    /**
     * Compute the covariance matrix for the given set of points.
     * <p>
     * The computed matrix is symmetric.
     * 
     * @param cov is the matrix to fill with the covariance elements.
     * @param points are the points.
     * @return the mean of the points.
     */
    public static Point2f cov(Matrix2f cov, Iterable<? extends Point2f> points) {
            assert(cov!=null);

            cov.setZero();
            
            // Compute the mean m
            Point2f m = new Point2f();
            int count = 0;
            for(Point2f p : points) {
                    m.add(new Vector2f(p.getX(),p.getY()));
                    count ++;
            }
            
            if (count==0) return null;
            
            m.scale(1.f/count);
            
            // Compute the covariance term [Gottshalk2000]
            // c_ij = sum(p'_i * p'_j) / n
            // c_ij = sum((p_i - m_i) * (p_j - m_j)) / n
            for(Point2f p : points) {
                    cov.m00 += (p.getX() - m.getX()) * (p.getX() - m.getX());
                    cov.m01 += (p.getX() - m.getX()) * (p.getY() - m.getY()); // same as m10
                    //cov.m10 += (p.getY() - m.getY()) * (p.getX() - m.getX()); // same as m01
                    cov.m11 += (p.getY() - m.getY()) * (p.getY() - m.getY());
            }
            
            cov.m00 /= count;
            cov.m01 /= count;
            cov.m10 = cov.m01;
            cov.m11 /= count;
            
            return m;
    }

    /**
     * Compute the covariance matrix for the given set of points.
     * <p>
     * The computed matrix is symmetric.
     * 
     * @param cov is the matrix to fill with the covariance elements.
     * @param points are the points.
     * @return the mean of the points.
     */
    public static Point3f cov(Matrix3f cov, Iterable<? extends Point3f> points) {
            assert(cov!=null);

            cov.setZero();
            
            // Compute the mean m
            Point3f m = new Point3f();
            int count = 0;
            for(Point3f p : points) {
                    m.add(new Vector3f(p.getX(),p.getY(), p.getZ()));
                    count ++;
            }
            
            if (count==0) return null;
            
            m.scale(1.f/count);
            
            // Compute the covariance term [Gottshalk2000]
            // c_ij = sum(p'_i * p'_j) / n
            // c_ij = sum((p_i - m_i) * (p_j - m_j)) / n
            for(Point3f p : points) {
                    cov.m00 += (p.getX() - m.getX()) * (p.getX() - m.getX());
                    cov.m01 += (p.getX() - m.getX()) * (p.getY() - m.getY()); // same as m10
                    cov.m02 += (p.getX() - m.getX()) * (p.getZ() - m.getZ()); // same as m20
                    //cov.m10 += (p.getY() - m.getY()) * (p.getX() - m.getX()); // same as m01
                    cov.m11 += (p.getY() - m.getY()) * (p.getY() - m.getY());
                    cov.m12 += (p.getY() - m.getY()) * (p.getZ() - m.getZ()); // same as m21
                    //cov.m20 += (p.getZ() - m.getZ()) * (p.getX() - m.getX()); // same as m02
                    //cov.m21 += (p.getZ() - m.getZ()) * (p.getY() - m.getY()); // same as m12
                    cov.m22 += (p.getZ() - m.getZ()) * (p.getZ() - m.getZ());
            }
            
            cov.m00 /= count;
            cov.m01 /= count;
            cov.m02 /= count;
            cov.m10 = cov.m01;
            cov.m11 /= count;
            cov.m12 /= count;
            cov.m20 = cov.m02;
            cov.m21 = cov.m12;
            cov.m22 /= count;
            
            return m;
    }
	
}
//TODO public Point3f getProjection(Point3D point) 


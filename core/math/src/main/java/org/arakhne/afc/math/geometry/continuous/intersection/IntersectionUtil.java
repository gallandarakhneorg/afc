/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.continuous.intersection;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;


/**
 * Several intersection functions.
 * <p>
 * Algo inspired from Mathematics for 3D Game Programming and Computer Graphics (MGPCG)
 * and from 3D Game Engine Design (GED)
 * and from Real Time Collision Detection (RTCD).
 * 
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: jdemange$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class IntersectionUtil implements MathConstants{
	
    private IntersectionUtil() {
    	//
    }

    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppermost point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsHollowSphereHollowAlignedBox(float sphereCenterx, float sphereCentery, float sphereCenterz, float radius,
    															 float lowerx, float lowery, float lowerz,
    															 float upperx, float uppery, float upperz) {
    	float r2 = radius*radius;
    	float a, b, dmin, dmax;
    	boolean face;

		dmin = 0;
		dmax = 0;
		
		face = false;

		// X
		a = sphereCenterx - lowerx;
		a = a*a;

		b = sphereCenterx - upperx;
		b = b*b;
		
		dmax += Math.max(a, b);
		
		if( sphereCenterx < lowerx ) {
			face = true;
			dmin += a;
		}
		else if( sphereCenterx > upperx ) {
			face = true;
			dmin += b;
		}
		else if( Math.min(a, b) <= r2 ) {
			face = true;
		}
		
		// Y
		a = sphereCentery - lowery;
		a = a*a;

		b = sphereCentery - uppery;
		b = b*b;
		
		dmax += Math.max(a, b);
		
		if( sphereCentery < lowery ) {
			face = true;
			dmin += a;
		}
		else if( sphereCentery > uppery ) {
			face = true;
			dmin += b;
		}
		else if( Math.min(a, b) <= r2 ) {
			face = true;
		}
		
		// Z
		a = sphereCenterz - lowerz;
		a = a*a;

		b = sphereCenterz - upperz;
		b = b*b;
		
		dmax += Math.max(a, b);
		
		if( sphereCenterz < lowerz ) {
			face = true;
			dmin += a;
		}
		else if( sphereCenterz > upperz ) {
			face = true;
			dmin += b;
		}
		else if( Math.min(a, b) <= r2 ) {
			face = true;
		}
		
		return (face && ( dmin <= r2 ) && ( r2 <= dmax));
    }
		
    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppermost point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidSphereHollowAlignedBox(float sphereCenterx, float sphereCentery, float sphereCenterz, float radius,
			 													float lowerx, float lowery, float lowerz,
			 													float upperx, float uppery, float upperz) {
    	float r2 = radius*radius;
    	float a, dmin;
    	boolean face;

		dmin = 0;
		
		face = false;

		// X
		if ( sphereCenterx < lowerx ) {
			face = true;
			a = sphereCenterx - lowerx;
			dmin += a*a;
		}
		else if ( sphereCenterx > upperx ) {
			face = true;
			a = sphereCenterx - upperx;
			dmin += a*a;     
		}
		else if ( sphereCenterx - lowerx <= radius ) face = true;
		else if ( upperx - sphereCenterx <= radius ) face = true;
		
		// Y
		if ( sphereCentery < lowery ) {
			face = true;
			a = sphereCentery - lowery;
			dmin += a*a;
		}
		else if ( sphereCentery > uppery ) {
			face = true;
			a = sphereCentery - uppery;
			dmin += a*a;     
		}
		else if ( sphereCentery - lowery <= radius ) face = true;
		else if ( uppery - sphereCentery <= radius ) face = true;
		
		// Z
		if ( sphereCenterz < lowerz ) {
			face = true;
			a = sphereCenterz - lowerz;
			dmin += a*a;
		}
		else if ( sphereCenterz > upperz ) {
			face = true;
			a = sphereCenterz - upperz;
			dmin += a*a;     
		}
		else if ( sphereCenterz - lowerz <= radius ) face = true;
		else if ( upperz - sphereCenterz <= radius ) face = true;
		
		return ( face && ( dmin <= r2 ) );
    }

    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppermost point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsHollowSphereSolidAlignedBox(float sphereCenterx, float sphereCentery, float sphereCenterz, float radius,
			 													float lowerx, float lowery, float lowerz,
			 													float upperx, float uppery, float upperz) {
    	float r2 = radius*radius;
    	float a, b, dmin, dmax;
    	
    	dmax = 0;
    	dmin = 0;

    	// X
    	a = sphereCenterx - lowerx;
    	a = a*a;
    	
    	b = sphereCenterx - upperx;
    	b = b*b;
    	
    	dmax += Math.max(a, b);
    	if( sphereCenterx < lowerx ) dmin += a;
    	else if( sphereCenterx > upperx ) dmin += b;

    	// Y
    	a = sphereCentery - lowery;
    	a = a*a;
    	
    	b = sphereCentery - uppery;
    	b = b*b;
    	
    	dmax += Math.max(a, b);
    	if( sphereCentery < lowery ) dmin += a;
    	else if( sphereCentery > uppery ) dmin += b;

    	// Z
    	a = sphereCenterz - lowerz;
    	a = a*a;
    	
    	b = sphereCenterz - upperz;
    	b = b*b;
    	
    	dmax += Math.max(a, b);
    	if( sphereCenterz < lowerz ) dmin += a;
    	else if( sphereCenterz > upperz ) dmin += b;

    	return ( dmin <= r2 && r2 <= dmax );
    }

    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppermost point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidSphereSolidAlignedBox(float sphereCenterx, float sphereCentery, float sphereCenterz, float radius,
			 												   float lowerx, float lowery, float lowerz,
			 												   float upperx, float uppery, float upperz) {
    	float r2 = radius*radius;
    	float a, dmin;

    	dmin = 0;
    	
    	// X
		if( sphereCenterx < lowerx ) {
			a = sphereCenterx - lowerx;
			dmin += a*a;
		}
		else if( sphereCenterx > upperx ) {
			a = sphereCenterx - upperx;
			dmin += a * a;
		}

    	// Y
		if( sphereCentery < lowery ) {
			a = sphereCentery - lowery;
			dmin += a*a;
		}
		else if( sphereCentery > uppery ) {
			a = sphereCentery - uppery;
			dmin += a * a;
		}

    	// Z
		if( sphereCenterz < lowerz ) {
			a = sphereCenterz - lowerz;
			dmin += a*a;
		}
		else if( sphereCenterz > upperz ) {
			a = sphereCenterz - upperz;
			dmin += a * a;
		}

		return ( dmin <= r2 );
    } 

    /** Replies if the specified rectangle intersects the specified circle.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 2-dimensional             
     * axis-aligned rectangle and an 2-dimensional circle.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param circleCenter are the coordinates of the circle center.
     * @param radius is the radius of the circle.
     * @param lower coordinates of the lowest point of the rectangle.
     * @param upper coordinates of the uppermost point of the rectangle.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidCircleSolidAlignedRectangle(float circleCenterx, float circleCentery, float circleCenterz, float radius,
			 														 float lowerx, float lowery, float lowerz,
			 														 float upperx, float uppery, float upperz) {
    	float r2 = radius*radius;
    	float a, dmin;

    	dmin = 0;
    	
    	// X
		if( circleCenterx < lowerx ) {
			a = circleCenterx - lowerx;
			dmin += a*a;
		}
		else if( circleCenterx > upperx ) {
			a = circleCenterx - upperx;
			dmin += a * a;
		}

    	// Y
		if( circleCentery < lowery ) {
			a = circleCentery - lowery;
			dmin += a*a;
		}
		else if( circleCentery > uppery ) {
			a = circleCentery - uppery;
			dmin += a * a;
		}

		return ( dmin < r2 );
    } 

    /**
     * Tests if the line segment from {@code (x1,y1)} to 
     * {@code (x2,y2)} intersects the line segment
     * from {@code (x3,y3)} to {@code (x4,y4)}.
     *
     * @param x1 the X coordinate of the start point of the
     *           first specified line segment
     * @param y1 the Y coordinate of the start point of the
     *           first specified line segment
     * @param x2 the X coordinate of the end point of the
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the
     *           first specified line segments
     * @param x3 the X coordinate of the start point of the
     *           first specified line segment
     * @param y3 the Y coordinate of the start point of the
     *           first specified line segment
     * @param x4 the X coordinate of the end point of the
     *           specified line segment
     * @param y4 the Y coordinate of the end point of the
     *           first specified line segment
     * @param epsilon the accuracy parameter (a distance here) must be the same unit of measurement as others parameters 
     * @return <code>true</code> if this line segments intersect each 
     * other; <code>false</code> otherwise.
     * @since 3.0
     */
    public static boolean intersectsSegments(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float epsilon) {
    	return ((GeometryUtil.ccw(x1, y1, x2, y2, x3, y3, epsilon) *
    			GeometryUtil.ccw(x1, y1, x2, y2, x4, y4, epsilon) <= 0)
    			&& (GeometryUtil.ccw(x3, y3, x4, y4, x1, y1, epsilon) *
    					GeometryUtil.ccw(x3, y3, x4, y4, x2, y2, epsilon) <= 0));
    }

    /**
     * Tests if the line from {@code (x1,y1)} to 
     * {@code (x2,y2)} intersects the line segment
     * from {@code (x3,y3)} to {@code (x4,y4)}.
     *
     * @param x1 the X coordinate of the start point of the line
     * @param y1 the Y coordinate of the start point of the line
     * @param x2 the X coordinate of the end point of the line
     * @param y2 the Y coordinate of the end point of the line
     * @param x3 the X coordinate of the start point of the line segment
     * @param y3 the Y coordinate of the start point of the line segment
     * @param x4 the X coordinate of the end point of the line segment
     * @param y4 the Y coordinate of the end point of the line segment
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
     * @return <code>true</code> if this line intersects the line segment,
     * <code>false</code> otherwise.
     * @since 3.0
     */
    public static boolean intersectsLineSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float epsilon) {
    	return (GeometryUtil.ccw(x1, y1, x2, y2, x3, y3, epsilon) *
    			GeometryUtil.ccw(x1, y1, x2, y2, x4, y4, epsilon) <= 0);
    }

    /**
     * Tests if the line from {@code (x1,y1)} to 
     * {@code (x2,y2)} intersects the line
     * from {@code (x3,y3)} to {@code (x4,y4)}.
     * <p>
     * If lines are colinear, this function replied <code>false</code>.
     *
     * @param x1 the X coordinate of the start point of the first line
     * @param y1 the Y coordinate of the start point of the first line
     * @param x2 the X coordinate of the end point of the first line
     * @param y2 the Y coordinate of the end point of the first line
     * @param x3 the X coordinate of the start point of the second line
     * @param y3 the Y coordinate of the start point of the second line
     * @param x4 the X coordinate of the end point of the second line
     * @param y4 the Y coordinate of the end point of the second line
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
     * @return <code>true</code> if this line segments intersect each 
     * other; <code>false</code> otherwise.
     * @since 3.0
     */
    public static boolean intersectsLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float epsilon) {
    	if (GeometryUtil.isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4, epsilon)) {
    		return GeometryUtil.isCollinearPoints(x1, y1, x2, y2, x3, y3, epsilon);
    	}
    	return true;
    }

    /**
     * Tests if the point {@code (px,py)} 
     * lies inside a 2D triangle
     * given by {@code (x1,y1)}, {@code (x2,y2)}
     * and {@code (x3,y3)} points.
     * <p>
     * <strong>Caution: Tests are "epsiloned."</strong>
     * <p>
     * <strong>Trigonometric Method (Slowest)</strong>
     * <p>
     * A common way to check if a point is in a triangle is to 
     * find the vectors connecting the point to each of the 
     * triangle's three vertices and sum the angles between 
     * those vectors. If the sum of the angles is 2*pi 
     * then the point is inside the triangle, otherwise it 
     * is not. <em>It works, but it is very slow.</em>
     * <p>
     * <center><img src="doc-files/point_segment.gif" alt="Point-Segment Intersection Picture 1">
     * <img src="doc-files/point_segment_2.jpg" alt="Point-Segment Intersection Picture 2"></center>
     * <p>
     * The advantage of the method above is that it's very simple to understand so that once 
     * you read it you should be able to remember it forever and code it up at 
     * any time without having to refer back to anything.
     * <p>
     * There's another method that is also as easy conceptually but executes faster.
     * The downside is there's a little more math involved, but once you see 
     * it worked out it should be no problem.
     * <p>
     * <strong>Barycenric Method (Fastest)</strong>
     * <p>
     * So remember that the three points of the triangle define a plane in space. 
     * Pick one of the points and we can consider all other locations on the plane 
     * as relative to that point. Let's select A -- it'll be our origin on the 
     * plane. Now what we need are basis vectors so we can give coordinate 
     * values to all the locations on the plane. 
     * We'll pick the two edges of the triangle that touch A, 
     * (C - A) and (B - A). 
     * Now we can get to any point on the plane just by starting at A 
     * and walking some distance along (C - A) and then from there walking 
     * some more in the direction (B - A).
     * <p>
     * <center><img src="doc-files/point_segment_3.png" alt="Point-Segment Intersection Picture 3"></center>
     * <p>
     * With that in mind we can now describe any point on the plane as:<br>
     * P = A + u * (C - A) + v * (B - A)
     * <p>
     * Notice now that if u or v < 0 then we've walked in the wrong direction 
     * and must be outside the triangle. Also if u or v > 1 then we've 
     * walked too far in a direction and are outside the triangle. 
     * Finally if u + v > 1 then we've crossed the edge BC again leaving the triangle.
     * <p>
     * Given u and v we can easily calculate the point P with the above 
     * equation, but how can we go in the reverse direction and calculate 
     * u and v from a given point P?<br>
     * P = A + u * (C - A) + v * (B - A)       // Original equation<br>
     * (P - A) = u * (C - A) + v * (B - A)     // Subtract A from both sides<br>
     * v2 = u * v0 + v * v1                    // Substitute v0, v1, v2 for less writing
     * <p>
     * We have two unknowns (u and v) so we need two equations to solve
     * for them.  Dot both sides by v0 to get one and dot both sides by
     * v1 to get a second.<br>
     * (v2) . v0 = (u * v0 + v * v1) . v0<br>
     * (v2) . v1 = (u * v0 + v * v1) . v1<br>
     * <p>
     * Distribute v0 and v1<br>
     * v2 . v0 = u * (v0 . v0) + v * (v1 . v0)<br>
     * v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
     * <p>
     * Now we have two equations and two unknowns and can solve one
     * equation for one variable and substitute into the other.  Or
     * fire up GNU Octave and save some handwriting.<br>
     * Solve[v2.v0 == {u(v0.v0) + v(v1.v0), v2.v1 == u(v0.v1) + v(v1.v1)}, {u, v}]<br>
     * u = ((v1.v1)(v2.v0)-(v1.v0)(v2.v1)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))<br>
     * v = ((v0.v0)(v2.v1)-(v0.v1)(v2.v0)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param ax the X coordinate of the first point of the triangle
     * @param ay the Y coordinate of the first point of the triangle
     * @param bx the X coordinate of the second point of the triangle
     * @param by the Y coordinate of the second point of the triangle
     * @param cx the X coordinate of the third point of the triangle
     * @param cy the Y coordinate of the third point of the triangle
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
     * @return <code>true</code> if the points is coplanar to the triangle and
     * lies inside it, otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointTriangle(
    		float px, float py, 
    		float ax, float ay,
    		float bx, float by,
    		float cx, float cy, float epsilon) {
    	/* The comment code is the trivial trigonometric implementation:
    	float vx1 = x1 - px;
    	float vy1 = y1 - py;
    	float vx2 = x2 - px;
    	float vy2 = y2 - py;
    	float vx3 = x3 - px;
    	float vy3 = y3 - py;
    	
    	float angle;
    	
    	angle = Math.acos(MathUtil.dotProduct(vx1, vy1, vx2, vy2));
    	angle += Math.acos(MathUtil.dotProduct(vx2, vy2, vx3, vy3));
    	angle += Math.acos(MathUtil.dotProduct(vx3, vy3, vx1, vy1));
    	
    	return MathUtil.epsilonEqualsRadian(angle, MathConstants.TWO_PI);*/
    	
    	//
    	// Compute vectors        
    	//
    	// v0 = C - A
    	float v0x = cx - ax;
    	float v0y = cy - ay;
    	// v1 = B - A
    	float v1x = bx - ax;
    	float v1y = by - ay;
    	// v2 = P - A
    	float v2x = px - ax;
    	float v2y = py - ay;

    	//
    	// Compute dot products
    	//
    	// dot01 = dot(v0, v0)
    	float dot00 = MathUtil.dotProduct(v0x, v0y, v0x, v0y);
    	// dot01 = dot(v0, v1)
    	float dot01 = MathUtil.dotProduct(v0x, v0y, v1x, v1y);
    	// dot02 = dot(v0, v2)
    	float dot02 = MathUtil.dotProduct(v0x, v0y, v2x, v2y);
    	// dot11 = dot(v1, v1)
    	float dot11 = MathUtil.dotProduct(v1x, v1y, v1x, v1y);
    	// dot12 = dot(v1, v2)
    	float dot12 = MathUtil.dotProduct(v1x, v1y, v2x, v2y);

    	//
    	// Compute barycentric coordinates
    	//
    	float invDenom = 1.f / (dot00 * dot11 - dot01 * dot01);
    	float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    	float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

    	// Check if point is in triangle
    	return (MathUtil.epsilonCompareTo(u, 0.f, epsilon) >= 0)
    			&& (MathUtil.epsilonCompareTo(v, 0.f, epsilon) >= 0) 
    		    && (MathUtil.epsilonCompareTo(u + v, 1.f, epsilon) <= 0);
    }

    /**
     * Tests if the point {@code (px,py,pz)} 
     * lies inside a 3D triangle
     * given by {@code (x1,y1,z1)}, {@code (x2,y2,z2)}
     * and {@code (x3,y3,z3)} points.
     * <p>
     * <strong>Caution: Tests are "epsiloned."</strong>
     * <p>
     * Parameter <var>forceCoplanar</var> has a deep influence on the function
     * result. It indicates if coplanarity test must be done or not.
     * Following table explains this influence:
     * <table>
     * <thead>
     * <tr>
     * <th>Point is coplanar?</th>
     * <th>Point projection on plane is inside triangle?</th>
     * <th><var>forceCoplanar</var></th>
     * <th><code>intersectsPointTrangle()</code> Result</th>
     * </tr>
     * </thead>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * </table>
     * <p>
     * <strong>Trigonometric Method (Slowest)</strong>
     * <p>
     * A common way to check if a point is in a triangle is to 
     * find the vectors connecting the point to each of the 
     * triangle's three vertices and sum the angles between 
     * those vectors. If the sum of the angles is 2*pi 
     * then the point is inside the triangle, otherwise it 
     * is not. <em>It works, but it is very slow.</em>
     * <p>
     * <center><img src="doc-files/point_segment.gif" alt="Point-Segment Intersection Picture 1">
     * <img src="doc-files/point_segment_2.jpg" alt="Point-Segment Intersection Picture 2"></center>
     * <p>
     * The advantage of the method above is that it's very simple to understand so that once 
     * you read it you should be able to remember it forever and code it up at 
     * any time without having to refer back to anything.
     * <p>
     * <strong>Barycenric Method (Fastest)</strong>
     * <p>
     * There's another method that is also as easy conceptually but executes faster.
     * The downside is there's a little more math involved, but once you see 
     * it worked out it should be no problem.
     * <p>
     * So remember that the three points of the triangle define a plane in space. 
     * Pick one of the points and we can consider all other locations on the plane 
     * as relative to that point. Let's select A -- it'll be our origin on the 
     * plane. Now what we need are basis vectors so we can give coordinate 
     * values to all the locations on the plane. 
     * We'll pick the two edges of the triangle that touch A, 
     * (C - A) and (B - A). 
     * Now we can get to any point on the plane just by starting at A 
     * and walking some distance along (C - A) and then from there walking 
     * some more in the direction (B - A).
     * <p>
     * <center><img src="doc-files/point_segment_3.png" alt="Point-Segment Intersection Picture 3"></center>
     * <p>
     * With that in mind we can now describe any point on the plane as:<br>
     * P = A + u * (C - A) + v * (B - A)
     * <p>
     * Notice now that if u or v < 0 then we've walked in the wrong direction 
     * and must be outside the triangle. Also if u or v > 1 then we've 
     * walked too far in a direction and are outside the triangle. 
     * Finally if u + v > 1 then we've crossed the edge BC again leaving the triangle.
     * <p>
     * Given u and v we can easily calculate the point P with the above 
     * equation, but how can we go in the reverse direction and calculate 
     * u and v from a given point P?<br>
     * P = A + u * (C - A) + v * (B - A)       // Original equation<br>
     * (P - A) = u * (C - A) + v * (B - A)     // Subtract A from both sides<br>
     * v2 = u * v0 + v * v1                    // Substitute v0, v1, v2 for less writing
     * <p>
     * We have two unknowns (u and v) so we need two equations to solve
     * for them.  Dot both sides by v0 to get one and dot both sides by
     * v1 to get a second.<br>
     * (v2) . v0 = (u * v0 + v * v1) . v0<br>
     * (v2) . v1 = (u * v0 + v * v1) . v1<br>
     * <p>
     * Distribute v0 and v1<br>
     * v2 . v0 = u * (v0 . v0) + v * (v1 . v0)<br>
     * v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
     * <p>
     * Now we have two equations and two unknowns and can solve one
     * equation for one variable and substitute into the other.  Or
     * fire up GNU Octave and save some handwriting.<br>
     * Solve[v2.v0 == {u(v0.v0) + v(v1.v0), v2.v1 == u(v0.v1) + v(v1.v1)}, {u, v}]<br>
     * u = ((v1.v1)(v2.v0)-(v1.v0)(v2.v1)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))<br>
     * v = ((v0.v0)(v2.v1)-(v0.v1)(v2.v0)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param pz the Z coordinate of the point
     * @param ax the X coordinate of the first point of the triangle
     * @param ay the Y coordinate of the first point of the triangle
     * @param az the Z coordinate of the first point of the triangle
     * @param bx the X coordinate of the second point of the triangle
     * @param by the Y coordinate of the second point of the triangle
     * @param bz the Z coordinate of the second point of the triangle
     * @param cx the X coordinate of the third point of the triangle
     * @param cy the Y coordinate of the third point of the triangle
     * @param cz the Z coordinate of the third point of the triangle
     * @param forceCoplanar is <code>true</code> to force to test
     * if the given point is coplanar to the triangle, <code>false</code>
     * to not consider coplanarity of the point.
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
     * @return <code>true</code> if the points is coplanar - or not,
     * depending on <var>forceCoplanar</var> - to the triangle and
     * lies inside it, otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointTriangle(
    		float px, float py, float pz,
    		float ax, float ay, float az,
    		float bx, float by, float bz,
    		float cx, float cy, float cz,
    		boolean forceCoplanar, float epsilon) {

    	//
    	// Compute vectors        
    	//
    	// v0 = C - A
    	float v0x = cx - ax;
    	float v0y = cy - ay;
    	float v0z = cz - az;
    	// v1 = B - A
    	float v1x = bx - ax;
    	float v1y = by - ay;
    	float v1z = bz - az;
    	// v2 = P - A
    	float v2x = px - ax;
    	float v2y = py - ay;
    	float v2z = pz - az;

    	//
    	// Compute dot products
    	//
    	// dot01 = dot(v0, v0)
    	float dot00 = MathUtil.dotProduct(v0x, v0y, v0z, v0x, v0y, v0z);
    	// dot01 = dot(v0, v1)
    	float dot01 = MathUtil.dotProduct(v0x, v0y, v0z, v1x, v1y, v1z);
    	// dot02 = dot(v0, v2)
    	float dot02 = MathUtil.dotProduct(v0x, v0y, v0z, v2x, v2y, v2z);
    	// dot11 = dot(v1, v1)
    	float dot11 = MathUtil.dotProduct(v1x, v1y, v1z, v1x, v1y, v1z);
    	// dot12 = dot(v1, v2)
    	float dot12 = MathUtil.dotProduct(v1x, v1y, v1z, v2x, v2y, v2z);

    	//
    	// Compute barycentric coordinates
    	//
    	float invDenom = 1.f / (dot00 * dot11 - dot01 * dot01);
    	float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    	float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

    	// Check if point is in triangle
    	if ((MathUtil.epsilonCompareTo(u, 0.f, epsilon) >= 0)
			&& (MathUtil.epsilonCompareTo(v, 0.f, epsilon) >= 0) 
		    && (MathUtil.epsilonCompareTo(u + v, 1.f, epsilon) <= 0)) {
    		if (forceCoplanar) {
    			// Triangle's plane equation:
    			// nx = ay * (bz - cz) + by * (cz - az) + cy * (az - bz)
    			// ny = az * (bx - cx) + bz * (cx - ax) + cz * (ax - bx)
    			// nz = ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)
    			// d = - (nx * ax + ny * ay + nz * az)
    			
    	    	// Result dot* variables to prevent memory allocation
    			dot00 = ay * (bz - cz) + by * v0z - cy * v1z;
    			dot01 = az * (bx - cx) + bz * v0x - cz * v1x;
    			dot02 = ax * (by - cy) + bx * v0y - cx * v1y;
    			dot11 = - (dot00 * ax + dot01 * ay + dot02 * az);
    			dot12 = dot00 * px + dot01 * py + dot02 * pz + dot11;
    			
    			return MathUtil.isEpsilonZero(dot12,0.f);
    		}
    		return true;
    	}
    	return false;
    }

    /**
     * Tests if the point {@code (px,py,pz)} 
     * lies inside a 3D segment
     * given by {@code (x1,y1,z1)} and {@code (x2,y2,z2)}
     * points.
     * <p>
     * This function projects the point on the 3D line and tests if the projection
     * is lying on the segment. To force the point to be on the segment, see below.
     * <p>
     * Parameter <var>forceCollinear</var> has a deep influence on the function
     * result. It indicates if collinear test must be done or not.
     * Following table explains this influence:
     * <table>
     * <thead>
     * <tr>
     * <th>Point is collinear?</th>
     * <th>Point projection on line is inside segment?</th>
     * <th><var>forceCollinear</var></th>
     * <th><code>intersectsPointSegment()</code> Result</th>
     * </tr>
     * </thead>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * </table>
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param pz the Z coordinate of the point
     * @param ax the X coordinate of the first point of the segment
     * @param ay the Y coordinate of the first point of the segment
     * @param az the Z coordinate of the first point of the segment
     * @param bx the X coordinate of the second point of the segment
     * @param by the Y coordinate of the second point of the segment
     * @param bz the Z coordinate of the second point of the segment
     * @param forceCollinear is <code>true</code> to force to test
     * if the given point is collinear to the segment, <code>false</code>
     * to not consider collinearity of the point.
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
     * @return <code>true</code> if the points is on segment,
     * otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointSegment(
    		float px, float py, float pz,
    		float ax, float ay, float az,
    		float bx, float by, float bz,
    		boolean forceCollinear, float epsilon) {
    	float ratio = GeometryUtil.getPointProjectionFactorOnSegment(px, py, pz, ax, ay, az, bx, by, bz); //Todo : create this
		
		if (ratio>=0. && ratio<=1.) {
    		if (forceCollinear) {
    			return GeometryUtil.isCollinearPoints(
    					ax, ay, az,
    		    		bx, by, bz,
    					px, py, pz, epsilon);
    		}
    		return true;
		}
		
		return false;
    }

    /**
     * Tests if the point {@code (px,py)} 
     * appromativaly lies inside a 2D segment
     * given by {@code (x1,y1)} and {@code (x2,y2)}
     * points.
     * <p>
     * This function uses {@link MathUtil#epsilonEqualsZero(float)}
     * to approximate ownership of the given point.
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param ax the X coordinate of the first point of the segment
     * @param ay the Y coordinate of the first point of the segment
     * @param bx the X coordinate of the second point of the segment
     * @param by the Y coordinate of the second point of the segment
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
     * @return <code>true</code> if the points is on segment,
     * otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointSegment(
    		float px, float py,
    		float ax, float ay,
    		float bx, float by, float epsilon) {
    	return GeometryUtil.ccw(ax, ay, bx, by, px, py, epsilon)==0;
    }

    /**
     * Tests if the two 1D segments are intersecting.
     * <p>
     * This function is assuming that <var>l1</var> is lower
     * or equal to <var>u1</var> and <var>l2</var> is lower
     * or equal to <var>u2</var>.
     *
     * @param l1 the min coordinate of the first segment
     * @param u1 the max coordinate of the first segment
     * @param l2 the min coordinate of the second segment
     * @param u2 the max coordinate of the second segment
     * @return <code>true</code> if the two 1D segments intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedSegments(float l1, float u1, float l2, float u2) {
    	assert(l1<=u1);
    	assert(l2<=u2);
    	if (l1<l2) return u1>=l2;
    	return u2>=l1;
    }

    /**
     * Tests if the two 2D axis-aligned rectangles are intersecting.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     *
     * @param lx1 the X coordinate of the lowest point of the first rectangle.
     * @param ly1 the Y coordinate of the lowest point of the first rectangle.
     * @param ux1 the X coordinate of the uppermost point of the first rectangle.
     * @param uy1 the Y coordinate of the uppermost point of the first rectangle.
     * @param lx2 the X coordinate of the lowest point of the second rectangle.
     * @param ly2 the Y coordinate of the lowest point of the second rectangle.
     * @param ux2 the X coordinate of the uppermost point of the second rectangle.
     * @param uy2 the Y coordinate of the uppermost point of the second rectangle.
     * @return <code>true</code> if the two 2D rectangles intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedRectangles(float lx1, float ly1, float ux1, float uy1, float lx2, float ly2, float ux2, float uy2) {
    	assert(lx1<=ux1);
    	assert(ly1<=uy1);
    	assert(lx2<=ux2);
    	assert(ly2<=uy2);

    	boolean intersects;
    	if (lx1<lx2) intersects = ux1>lx2;
    	else intersects = ux2>lx1;
    	
    	if (intersects) {
        	if (ly1<ly2) intersects = uy1>ly2;
        	else intersects = uy2>ly1;
    	}
    	
    	return intersects;
    }

    /**
     * Tests if the two 3D axis-aligned boxes are intersecting.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     *
     * @param lower1 coordinates of the lowest point of the first box.
     * @param upper1 coordinates of the uppermost point of the first box.
     * @param lower2 coordinates of the lowest point of the second box.
     * @param upper2 coordinates of the uppermost point of the second box.
     * @return <code>true</code> if the two 3D boxes intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedBoxes(float lower1x, float lower1y, float lower1z, float upper1x, float upper1y, float upper1z,
    											 float lower2x, float lower2y, float lower2z, float upper2x, float upper2y, float upper2z) {
    	assert(lower1x<=upper1x);
    	assert(lower1y<=upper1y);
    	assert(lower1z<=upper1z);
    	assert(lower2x<=upper2x);
    	assert(lower2y<=upper2y);
    	assert(lower2z<=upper2z);
    	
    	boolean intersects;
    	if (lower1x<lower2x) intersects = upper1x>lower2x;
    	else intersects = upper2x>lower1x;
    	
    	if (intersects) {
        	if (lower1y<lower2y) intersects = upper1y>lower2y;
        	else intersects = upper2y>lower1y;
        	
        	if (intersects) {
            	if (lower1z<lower2z) intersects = upper1z>lower2z;
            	else intersects = upper2z>lower1z;
        	}
    	}
    	
    	return intersects;
    }

    /**
     * Tests if the two 2D minimum bounding rectangles are intersecting.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, and <var>ly1</var> is lower
     * or equal to <var>uy1</var>.
     *
     * @param lower1 coordinates of the lowest point of the first rectangle.
     * @param upper1 coordinates of the uppermost point of the first rectangle.
     * @param lower2 coordinates of the lowest point of the second rectangle.
     * @param upper2 coordinates of the uppermost point of the second rectangle.
     * @return <code>true</code> if the two 2D rectangles intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedRectangles(float lower1x, float lower1y, float lower1z, float upper1x, float upper1y, float upper1z,
			 										  float lower2x, float lower2y, float lower2z, float upper2x, float upper2y, float upper2z){
    	assert(lower1x<=upper1x);
    	assert(lower1y<=upper1y);
    	assert(lower2x<=upper2x);
    	assert(lower2y<=upper2y);

    	boolean intersects;
    	if (lower1x<lower2x) intersects = upper1x>lower2x;
    	else intersects = upper2x>lower1x;
    	
    	if (intersects) {
        	if (lower1y<lower2y) intersects = upper1y>lower2y;
        	else intersects = upper2y>lower1y;
    	}
    	
    	return intersects;
    }
    
    /** Replies if the specified box intersects the specified sphere.
     *
     * @param sphereCenter are the coordinates of the sphere center.
     * @param sphereRadius is the radius of the sphere.
     * @param boxCenter is the center point of the oriented box.
     * @param boxAxis are the unit vectors of the oriented box axis.
     * @param boxExtent are the sizes of the oriented box.
     * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidSphereOrientedBox(float sphereCenterx, float sphereCentery, float sphereCenterz, float sphereRadius,
			 											   float boxCenterx, float boxCentery, float boxCenterz,
			 											   float boxAxis1x, float boxAxis1y, float boxAxis1z,
			 											   float boxAxis2x, float boxAxis2y, float boxAxis2z,
			 											   float boxAxis3x, float boxAxis3y, float boxAxis3z,
			 											   float boxExtentAxis1, float boxExtentAxis2, float boxExtentAxis3, float epsilon) {
		// Find points on OBB closest and farest to sphere center
    	Point3f closest = new Point3f();
        Point3f farest = new Point3f();
		
		GeometryUtil.closestFarthestPointsOBBPoint( boxCenterx,  boxCentery,  boxCenterz,
				 								boxAxis1x, boxAxis1y, boxAxis1z,
				 								boxAxis2x, boxAxis2y, boxAxis2z,
				 								boxAxis3x, boxAxis3y, boxAxis3z,
				 								boxExtentAxis1, boxExtentAxis2, boxExtentAxis3,
												sphereCenterx, sphereCentery, sphereCenterz,
												closest,
												farest);
		
		// Sphere and OBB intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		float squaredRadius = sphereRadius * sphereRadius;
		
		if (GeometryUtil.distanceSquaredPointPoint(sphereCenterx, sphereCentery, sphereCenterz,
													 closest.getX(), closest.getY(), closest.getZ())>squaredRadius+epsilon) return false;
		
		return true;
    }

    /** Replies if the specified rectangle intersects the specified circle.
    *
    * @param circleCenter are the coordinates of the circle center.
    * @param circleRadius is the radius of the circle.
    * @param obrCenter is the center point of the OBR.
    * @param obrAxis are the unit vectors of the OBR axis.
    * @param obrExtent are the sizes of the OBR.
    * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	* @return <code>true</code> if intersecting, otherwise <code>false</code>
    */
   public static boolean intersectsSolidCircleOrientedRectangle(float circleCenterx, float circleCentery, float circleRadius,
															    float obrCenterx, float obrCentery,
																float obrAxis1x, float obrAxis1y,
																float obrAxis2x, float obrAxis2y,
																float obrExtentAxis1, float obrExtentAxis2, float epsilon) {
		// Find points on OBR closest and farest to sphere center
		Point2f closest = new Point2f();
		Point2f farest = new Point2f();
	
		GeometryUtil.closestFarthestPointsOBRPoint( obrCenterx,  obrCentery,
											 	obrAxis1x,  obrAxis1y,
												obrAxis2x,  obrAxis2y,
												obrExtentAxis1,  obrExtentAxis2,
												circleCenterx, circleCentery,
												closest, farest);
		
		// Circle and OBR intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		float squaredRadius = circleRadius * circleRadius;
		
		if (GeometryUtil.distanceSquaredPointPoint(obrCenterx, obrCentery,
				closest.getX(), closest.getY())>squaredRadius+epsilon) return false;
		
		return true;
   }

   /** Replies if the specified boxes intersect.
     * <p>
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>3</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an general intersection test between two OBB.
     * If the first box is expected to be an AAB, please use the
     * optimized algorithm given by
     * {@link #intersectsAlignedBoxOrientedBox(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)}.
     *
     * @param center1 is the center point of the first oriented box.
     * @param axis1 are the unit vectors of the first oriented box axis.
     * @param extent1 are the sizes of the first oriented box.
     * @param center2 is the center point of the second oriented box.
     * @param axis2 are the unit vectors of the second oriented box axis.
     * @param extent2 are the sizes of the second oriented box.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static boolean intersectsOrientedBoxes(float center1x, float center1y, float center1z,
			   									  float box1Axis1x, float box1Axis1y, float box1Axis1z,
			   									  float box1Axis2x, float box1Axis2y, float box1Axis2z,
			   									  float box1Axis3x, float box1Axis3y, float box1Axis3z,
			   									  float box1ExtentAxis1, float box1ExtentAxis2, float box1ExtentAxis3,
			   									  float center2x, float center2y, float center2z,
			   									  float box2Axis1x, float box2Axis1y, float box2Axis1z,
			   									  float box2Axis2x, float box2Axis2y, float box2Axis2z,
			   									  float box2Axis3x, float box2Axis3y, float box2Axis3z,
			   									  float box2ExtentAxis1, float box2ExtentAxis2, float box2ExtentAxis3){
    	
	//translation, in parent frame
	float vx, vy, vz;
	vx = center2x - center1x;
	vy = center2y - center1y;
	vz = center2z - center1z;
	
	//translation, in A's frame
	float tx,ty,tz;
	tx = MathUtil.dotProduct(vx, vy, vz, box1Axis1x, box1Axis1y, box1Axis1z);
	ty = MathUtil.dotProduct(vx, vy, vz, box1Axis2x, box1Axis2y, box1Axis2z);
	tz = MathUtil.dotProduct(vx, vy, vz, box1Axis3x, box1Axis3y, box1Axis3z);
	
	//B's basis with respect to A's local frame
	float R_1_1, R_1_2, R_1_3,
	  	  R_2_1, R_2_2, R_2_3,
		  R_3_1, R_3_2, R_3_3,
		  
		  absR_1_1, absR_1_2, absR_1_3,
		  absR_2_1, absR_2_2, absR_2_3,
		  absR_3_1, absR_3_2, absR_3_3;
	
	R_1_1 = MathUtil.dotProduct(box1Axis1x, box1Axis1y,box1Axis1z,box2Axis1x, box2Axis1y,box2Axis1z); absR_1_1 = (R_1_1 < 0) ? -R_1_1 : R_1_1;
	R_1_2 = MathUtil.dotProduct(box1Axis1x, box1Axis1y,box1Axis1z,box2Axis2x, box2Axis2y,box2Axis2z); absR_1_2 = (R_1_2 < 0) ? -R_1_2 : R_1_2;
	R_1_3 = MathUtil.dotProduct(box1Axis1x, box1Axis1y,box1Axis1z,box2Axis3x, box2Axis3y,box2Axis3z); absR_1_3 = (R_1_3 < 0) ? -R_1_3 : R_1_3;
	R_2_1 = MathUtil.dotProduct(box1Axis2x, box1Axis2y,box1Axis2z,box2Axis1x, box2Axis1y,box2Axis1z); absR_2_1 = (R_2_1 < 0) ? -R_2_1 : R_2_1;
	R_2_2 = MathUtil.dotProduct(box1Axis2x, box1Axis2y,box1Axis2z,box2Axis2x, box2Axis2y,box2Axis2z); absR_2_2 = (R_2_2 < 0) ? -R_2_2 : R_2_2;
	R_2_3 = MathUtil.dotProduct(box1Axis2x, box1Axis2y,box1Axis2z,box2Axis3x, box2Axis3y,box2Axis3z); absR_2_3 = (R_2_3 < 0) ? -R_2_3 : R_2_3;
	R_3_1 = MathUtil.dotProduct(box1Axis3x, box1Axis3y,box1Axis3z,box2Axis1x, box2Axis1y,box2Axis1z); absR_3_1 = (R_3_1 < 0) ? -R_3_1 : R_3_1;
	R_3_2 = MathUtil.dotProduct(box1Axis3x, box1Axis3y,box1Axis3z,box2Axis2x, box2Axis2y,box2Axis2z); absR_3_2 = (R_3_2 < 0) ? -R_3_2 : R_3_2;
	R_3_3 = MathUtil.dotProduct(box1Axis3x, box1Axis3y,box1Axis3z,box2Axis3x, box2Axis3y,box2Axis3z); absR_3_3 = (R_3_3 < 0) ? -R_3_3 : R_3_3;

	// ALGORITHM: Use the separating axis test for all 15 potential
	// separating axes. If a separating axis could not be found, the two
	// boxes overlap.
	float ra, rb, t;

	ra = box1ExtentAxis1;
	rb = box2ExtentAxis1*absR_1_1+ box2ExtentAxis2*absR_1_2 + box2ExtentAxis3*absR_1_3;
	t = Math.abs(tx);
	if (t > ra + rb) return false;

	ra = box1ExtentAxis2;
	rb = box2ExtentAxis1*absR_2_1+ box2ExtentAxis2*absR_2_2 + box2ExtentAxis3*absR_3_3;
	t = Math.abs(ty);
	if (t > ra + rb) return false;
	
	ra = box1ExtentAxis3;
	rb = box2ExtentAxis1*absR_3_1+ box2ExtentAxis2*absR_3_2 + box2ExtentAxis3*absR_3_3;
	t = Math.abs(tz);
	if (t > ra + rb) return false;

	//B's basis vectors
	ra = box1ExtentAxis1*absR_1_1+ box1ExtentAxis2*absR_2_1 + box1ExtentAxis3*absR_3_1;
	rb = box2ExtentAxis1;
	t =	Math.abs( tx*R_1_1 + ty*R_2_1 + tz*R_3_1 );
	if (t > ra + rb) return false;
	
	ra = box1ExtentAxis1*absR_1_2+ box1ExtentAxis2*absR_2_2 + box1ExtentAxis3*absR_3_2;
	rb = box2ExtentAxis2;
	t =	Math.abs( tx*R_1_2 + ty*R_2_2 + tz*R_3_2 );
	if (t > ra + rb) return false;
	
	ra = box1ExtentAxis1*absR_1_3+ box1ExtentAxis2*absR_2_3 + box1ExtentAxis3*absR_3_3;
	rb = box2ExtentAxis3;
	t =	Math.abs( tx*R_1_3 + ty*R_2_3 + tz*R_3_3 );
	if (t > ra + rb) return false;

	//9 cross products

	//L = A0 x B0
	ra = box1ExtentAxis1*absR_3_1 + box1ExtentAxis3*absR_2_1;
	rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_2;
	t = Math.abs( tz*R_2_1 - ty*R_3_1 );
	if (t > ra + rb) return false;
	
	
	ra = box1ExtentAxis2*absR_3_1 + box1ExtentAxis3*absR_2_1;
	rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_2;
	t = Math.abs( tz*R_2_1 - ty*R_3_1 );
	if (t > ra + rb) return false;

	//L = A0 x B1
	ra = box1ExtentAxis2*absR_3_2 + box1ExtentAxis3*absR_2_2;
	rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_1;
	t = Math.abs( tz*R_2_2 - ty*R_3_2 );
	if (t > ra + rb) return false;

	//L = A0 x B2
	ra = box1ExtentAxis2*absR_3_3 + box1ExtentAxis3*absR_2_3;
	rb = box1ExtentAxis3*absR_1_2 + box1ExtentAxis3*absR_1_1;
	t = Math.abs( tz*R_2_3 - ty*R_3_3 );
	if (t > ra + rb) return false;

	//L = A1 x B0
	ra = box1ExtentAxis1*absR_3_1 + box1ExtentAxis3*absR_1_1;
	rb = box1ExtentAxis3*absR_2_3 + box1ExtentAxis3*absR_2_2;
	t = Math.abs( tx*R_3_1 - tz*R_1_1 );
	if (t > ra + rb) return false;

	//L = A1 x B1
	ra = box1ExtentAxis1*absR_3_2 + box1ExtentAxis3*absR_1_2;
	rb = box1ExtentAxis3*absR_2_3 + box1ExtentAxis3*absR_2_1;
	t = Math.abs( tx*R_3_2 - tz*R_1_2 );
	if (t > ra + rb) return false;

	//L = A1 x B2
	ra = box1ExtentAxis1*absR_3_3 + box1ExtentAxis3*absR_1_3;
	rb = box1ExtentAxis3*absR_2_2 + box1ExtentAxis3*absR_2_1;
	t = Math.abs( tx*R_3_3 - tz*R_1_3 );
	if (t > ra + rb) return false;

	//L = A2 x B0
	ra = box1ExtentAxis1*absR_2_1 + box1ExtentAxis2*absR_1_1;
	rb = box1ExtentAxis3*absR_3_3 + box1ExtentAxis3*absR_3_2;
	t = Math.abs( ty*R_1_1 - tx*R_2_1 );
	if (t > ra + rb) return false;

	//L = A2 x B1
	ra = box1ExtentAxis1*absR_2_2 + box1ExtentAxis2*absR_1_2;
	rb = box1ExtentAxis3*absR_3_3 + box1ExtentAxis3*absR_3_1;
	t = Math.abs( ty*R_1_2 - tx*R_2_2 );
	if (t > ra + rb) return false;

	//L = A2 x B2
	ra = box1ExtentAxis1*absR_2_3 + box1ExtentAxis2*absR_1_3;
	rb = box1ExtentAxis3*absR_3_2 + box1ExtentAxis3*absR_3_1;
	t = Math.abs( ty*R_1_3 - tx*R_2_3 );
	if (t > ra + rb) return false;

	/*no separating axis found, the two boxes overlap */
	return true;
   
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
     * {@link #intersectsAlignedRectangleOrientedRectangle(float, float, float, float, float, float, float, float, float, float, float, float)}.
     *
     * @param center1 is the center point of the first OBR.
     * @param axis1 are the unit vectors of the first OBR axis.
     * @param extent1 are the sizes of the first OBR.
     * @param center2 is the center point of the second OBR.
     * @param axis2 are the unit vectors of the second OBR axis.
     * @param extent2 are the sizes of the second OBR.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.jkh.me/files/tutorials/Separating%20Axis%20Theorem%20for%20Oriented%20Bounding%20Boxes.pdf">Intersection between two oriented boudning rectangles</a>
     */
    public static boolean intersectsOrientedRectangles(
    		float center1x, float center1y, float rect1Axis1x, float rect1Axis1y, float rect1Axis2x, float  rect1Axis2y, float rect1ExtentAxis1, float rect1ExtentAxis2,
    		float center2x, float center2y, float rect2Axis1x, float rect2Axis1y, float rect2Axis2x, float  rect2Axis2y, float rect2ExtentAxis1, float rect2ExtentAxis2){
    	assert(rect1ExtentAxis1>=0);
    	assert(rect1ExtentAxis2>=0);
    	assert(rect2ExtentAxis1>=0);
    	assert(rect2ExtentAxis2>=0);

    	float tx, ty;
    	tx = center2x - center1x;
    	ty = center2y - center1y;
    	
    	float scaledRect1Axis1x, scaledRect1Axis1y, scaledRect1Axis2x, scaledRect1Axis2y,
    		  scaledRect2Axis1x, scaledRect2Axis1y, scaledRect2Axis2x, scaledRect2Axis2y;
    	
    	scaledRect1Axis1x = rect1Axis1x * rect1ExtentAxis1;
    	scaledRect1Axis1y = rect1Axis1y * rect1ExtentAxis1;
    	scaledRect1Axis2x = rect1Axis2x * rect1ExtentAxis2;
    	scaledRect1Axis2y = rect1Axis2y * rect1ExtentAxis2;
    	scaledRect2Axis1x = rect2Axis1x * rect2ExtentAxis1;
    	scaledRect2Axis1y = rect2Axis1y * rect2ExtentAxis1;
    	scaledRect2Axis2x = rect2Axis2x * rect2ExtentAxis2;
    	scaledRect2Axis2y = rect2Axis2y * rect2ExtentAxis2;
    	
        //Let A the first box and B the second one
        //L = Ax
        if (Math.abs(MathUtil.dotProduct(tx, ty, rect1Axis1x, rect1Axis1y)) > 
        		rect1ExtentAxis1 +
        		Math.abs(MathUtil.dotProduct(scaledRect2Axis1x, scaledRect2Axis1y, rect1Axis1x, rect1Axis1y)) + 
        		Math.abs(MathUtil.dotProduct(scaledRect2Axis2x, scaledRect2Axis2y, rect1Axis1x, rect1Axis1y))
        	)
                return false;
        
        //L = Ay
        if (Math.abs(MathUtil.dotProduct(tx, ty, rect1Axis2x, rect1Axis2y)) >
        		rect1ExtentAxis2 +
        		Math.abs(MathUtil.dotProduct(scaledRect2Axis1x, scaledRect2Axis1y, rect1Axis2x, rect1Axis2y)) +
        		Math.abs(MathUtil.dotProduct(scaledRect2Axis2x, scaledRect2Axis2y, rect1Axis2x, rect1Axis2y))
        	)
                return false;
                    
        //L=Bx
        if (Math.abs(MathUtil.dotProduct(tx, ty, rect2Axis1x, rect2Axis1y)) >
        		rect2ExtentAxis1 +
        		Math.abs(MathUtil.dotProduct(scaledRect1Axis1x, scaledRect1Axis1y, rect2Axis1x, rect2Axis1y)) +
        		Math.abs(MathUtil.dotProduct(scaledRect1Axis2x, scaledRect1Axis2y, rect2Axis1x, rect2Axis1y))
        	)
                return false;
                    
        //L=By
        if (Math.abs(MathUtil.dotProduct(tx, ty, rect2Axis2x, rect2Axis2y)) >
        		rect2ExtentAxis2 +
        		Math.abs(MathUtil.dotProduct(scaledRect1Axis1x, scaledRect1Axis1y, rect2Axis2x, rect2Axis2y)) +
        		Math.abs(MathUtil.dotProduct(scaledRect1Axis2x, scaledRect1Axis2y, rect2Axis2x, rect2Axis2y))
        	)
                return false;
        
		/*no separating axis found, the two boxes overlap */
    	return true;
    }

    /** Replies if the specified boxes intersect.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>3</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an optimized algorithm for AABB as first parameter.
     * The general intersection type between two OBB is given by
     * {@link #intersectsOrientedBoxes(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)}
     *
     * @param lower coordinates of the lowest point of the first AABB box.
     * @param upper coordinates of the uppermost point of the first AABB box.
     * @param center is the center point of the second oriented box.
     * @param axis are the unit vectors of the second oriented box axis.
     * @param extent are the sizes of the second oriented box.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static boolean intersectsAlignedBoxOrientedBox(
				float lowerx,float  lowery,float  lowerz,
				float upperx,float  uppery,float  upperz,
				float centerx,float  centery,float  centerz,
				float axis1x, float axis1y, float axis1z,
				float axis2x, float axis2y, float axis2z,
				float axis3x, float axis3y, float axis3z,
				float extentAxis1, float extentAxis2, float extentAxis3) {
    	assert(lowerx<=upperx);
    	assert(lowery<=uppery);
    	assert(lowerz<=upperz);
    	assert(extentAxis1>=0);
    	assert(extentAxis2>=0);
    	assert(extentAxis3>=0);
    	
    	float aabbCenterx,aabbCentery,aabbCenterz;
    	aabbCenterx = (upperx+lowerx)/2.f;
    	aabbCentery = (uppery+lowery)/2.f;
    	aabbCenterz = (upperz+lowerz)/2.f;
    			
		
    	return intersectsOrientedBoxes(
    			aabbCenterx, aabbCentery, aabbCenterz,
    			1,0,0,	//Axis 1
    			0,1,0,	//Axis 2
    			0,0,1,	//Axis 3
    			upperx - aabbCenterx, uppery - aabbCentery, upperz - aabbCenterz,
    			centerx, centery, centerz,
				axis1x, axis1y, axis1z,
				axis2x, axis2y, axis2z,
				axis3x, axis3y, axis3z,
				extentAxis1, extentAxis2, extentAxis3);
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
     * {@link #intersectsOrientedBoxes(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)}
     *
     * @param lower coordinates of the lowest point of the first MBR.
     * @param upper coordinates of the uppermost point of the first MBR.
     * @param center is the center point of the second OBR.
     * @param axis are the unit vectors of the second OBR axis.
     * @param extent are the sizes of the second OBR.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static boolean intersectsAlignedRectangleOrientedRectangle(
				float lowerx,float  lowery, float upperx,float  uppery,
				float centerx,float  centery, float axis1x, float axis1y, float axis2x, float axis2y, float extentAxis1, float extentAxis2) {
    	assert(lowerx<=upperx);
    	assert(lowery<=uppery);
    	assert(extentAxis1>=0);
    	assert(extentAxis2>=0);
    	
    	float mbrCenterx, mbrCentery;
    	
    	mbrCenterx = (upperx+lowerx)/2.f;
    	mbrCentery = (uppery+lowery)/2.f;
    	
    	return intersectsOrientedRectangles(
    			mbrCenterx, mbrCentery, 1, 0, 0, 1, upperx - mbrCenterx, uppery - mbrCentery,
    			centerx, centery, axis1x, axis1y, axis2x, axis2y, extentAxis1, extentAxis2);
    }

    

	/**
	 * Replies if the specified sphere intersects the specified capsule.
	 * @param sphereCenter - center of the sphere
	 * @param sphereRadius - radius of the sphere
	 * @param capsuleA - Medial line segment start point of the capsule
	 * @param capsuleB - Medial line segment end point of the capsule
	 * @param capsuleRadius - radius of the capsule
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsSphereCapsule(float sphereCenterx, float sphereCentery, float sphereCenterz, float sphereRadius,
												  float capsuleAx, float capsuleAy, float capsuleAz,
												  float capsuleBx, float capsuleBy, float capsuleBz, float capsuleRadius){
		// Compute (squared) distance between sphere center and capsule line segment

		float dist2 = GeometryUtil.distanceSquaredPointSegment(sphereCenterx, sphereCentery, sphereCenterz, capsuleAx, capsuleAy, capsuleAz, capsuleBx, capsuleBy, capsuleBz);

		// If (squared) distance smaller than (squared) sum of radii, they collide
		float radius = sphereRadius + capsuleRadius;
		return dist2 < radius * radius;
	}
	
	/**
	 * Replies if the specified capsules intersect
	 * @param capsule1A - Medial line segment start point of the first capsule
	 * @param capsule1B - Medial line segment end point of the first capsule
	 * @param capsule1Radius - radius of the first capsule
	 * @param capsule2A - Medial line segment start point of the second capsule
	 * @param capsule2B - Medial line segment end point of the second capsule
	 * @param capsule2Radius - radius of the second capsule
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsCapsuleCapsule(
			float capsule1Ax, float capsule1Ay, float capsule1Az, float capsule1Bx, float capsule1By, float capsule1Bz, float capsule1Radius,
			float capsule2Ax, float capsule2Ay, float capsule2Az, float capsule2Bx, float capsule2By, float capsule2Bz, float capsule2Radius) {
		
		float dist2 = GeometryUtil.closestPointsSegmentSegment(
				capsule1Ax, capsule1Ay, capsule1Az, capsule1Bx, capsule1By, capsule1Bz,
				capsule2Ax, capsule2Ay, capsule2Az, capsule2Bx, capsule2By, capsule2Bz,
				null,null);

		// If (squared) distance smaller than (squared) sum of radii, they collide
		float radius = capsule1Radius + capsule2Radius;
		return dist2 <= radius * radius;
	} 
	
	/**
	 * Compute intersection between an OBB and a capsule
	 * @param center is the center point of the oriented box.
     * @param axis are the unit vectors of the oriented box axis.
     * @param extent are the sizes of the oriented box.
	 * @param capsule1A - capsule medial line segment start point
	 * @param capsule1B - capsule medial line segment end point
	 * @param capsule1Radius - capsule radius
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsOrientedBoxCapsule(
				float centerx,float  centery,float  centerz,
				float axis1x, float axis1y, float axis1z, float axis2x, float axis2y, float axis2z,	float axis3x, float axis3y, float axis3z,
				float extentAxis1, float extentAxis2, float extentAxis3,
				float capsule1Ax, float capsule1Ay, float capsule1Az, float capsule1Bx, float capsule1By, float capsule1Bz, float capsule1Radius, float epsilon) {

		Point3f closestFromA = new Point3f();
		Point3f closestFromB = new Point3f();
		
		GeometryUtil.closestFarthestPointsOBBPoint(
				centerx, centery, centerz,axis1x, axis1y, axis1z, axis2x, axis2y, axis2z,	axis3x, axis3y, axis3z,extentAxis1, extentAxis2, extentAxis3,
				capsule1Ax, capsule1Ay, capsule1Az,
				closestFromA, null);
		GeometryUtil.closestFarthestPointsOBBPoint(
				centerx, centery, centerz, axis1x, axis1y, axis1z, axis2x, axis2y, axis2z, axis3x, axis3y, axis3z, extentAxis1, extentAxis2, extentAxis3,
				capsule1Bx, capsule1By, capsule1Bz,
				closestFromB,null);
		
		float distance = GeometryUtil.distanceSegmentSegment(capsule1Ax, capsule1Ay, capsule1Bx, capsule1By, closestFromA.getX(), closestFromA.getY(), closestFromB.getX(), closestFromB.getY(), epsilon);
		
		return (distance <= capsule1Radius);
	}
	
	/**
	 * Compute intersection between a point and a capsule
	 * @param p - the point to test
	 * @param capsule1A - capsule medial line segment start point
	 * @param capsule1B - capsule medial line segment end point
	 * @param capsule1Radius - capsule radius
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsPointCapsule(
						float px, float py, float pz,
						float capsule1Ax, float capsule1Ay, float capsule1Az, float capsule1Bx, float capsule1By, float capsule1Bz, float capsule1Radius) {
		
		
		float distPointToCapsuleSegment = GeometryUtil.distancePointSegment(px,py,pz,capsule1Ax,capsule1Ay,capsule1Az,capsule1Bx,capsule1By,capsule1Bz);
		return (distPointToCapsuleSegment <= capsule1Radius);
	}

	/** Replies if two circles are intersecting.
	 * 
	 * @param x1 is the center of the first circle
	 * @param y1 is the center of the first circle
	 * @param radius1 is the radius of the first circle
	 * @param x2 is the center of the second circle
	 * @param y2 is the center of the second circle
	 * @param radius2 is the radius of the second circle
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsCircleCircle(float x1, float y1, float radius1, float x2, float y2, float radius2) {
		float r = radius1+radius2;
		return GeometryUtil.distanceSquaredPointPoint(x1, y1, x2, y2) < (r*r);
	}

	/** Replies if a circle and a rectangle are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first corner of the rectangle.
	 * @param y2 is the first corner of the rectangle.
	 * @param x3 is the second corner of the rectangle.
	 * @param y3 is the second corner of the rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsCircleRectangle(float x1, float y1, float radius, float x2, float y2, float x3, float y3) {
		float dx;
		if (x1<x2) {
			dx = x2 - x1;
		}
		else if (x1>x3) {
			dx = x1 - x3;
		}
		else {
			dx = 0f;
		}
		float dy;
		if (y1<y2) {
			dy = y2 - y1;
		}
		else if (y1>y3) {
			dy = y1 - y3;
		}
		else {
			dy = 0f;
		}
		return (dx*dx+dy*dy) < (radius*radius);
	}

	/** Replies if a circle and a line are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first point of the line.
	 * @param y2 is the first point of the line.
	 * @param x3 is the second point of the line.
	 * @param y3 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsCircleLine(float x1, float y1, float radius, float x2, float y2, float x3, float y3) {
		float d = GeometryUtil.distanceSquaredPointLine(x1, y1, x2, y2, x3, y3);
		return d<(radius*radius);
	}

	/** Replies if a circle and a segment are intersecting.
	 * 
	 * @param x1 is the center of the circle
	 * @param y1 is the center of the circle
	 * @param radius is the radius of the circle
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param x3 is the second point of the segment.
	 * @param y3 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsCircleSegment(float x1, float y1, float radius, float x2, float y2, float x3, float y3) {
		float d = GeometryUtil.distanceSquaredPointSegment(x1, y1, x2, y2, x3, y3, null);
		return d<(radius*radius);
	}

	/** Replies if two ellipses are intersecting.
	 * 
	 * @param x1 is the first corner of the first ellipse.
	 * @param y1 is the first corner of the first ellipse.
	 * @param x2 is the second corner of the first ellipse.
	 * @param y2 is the second corner of the first ellipse.
	 * @param x3 is the first corner of the second ellipse.
	 * @param y3 is the first corner of the second ellipse.
	 * @param x4 is the second corner of the second ellipse.
	 * @param y4 is the second corner of the second ellipse.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsEllipseEllipse(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float ell2w = Math.abs(x4 - x3);
		float ell2h = Math.abs(y4 - y3);
		float ellw = Math.abs(x2 - x1);
		float ellh = Math.abs(y2 - y1);
	
		if (ell2w <= 0f || ell2h <= 0f) return false;
		if (ellw <= 0f || ellh <= 0f) return false;
	
		// Normalize the second ellipse coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		float normx0 = (x3 - x1) / ellw - 0.5f;
		float normx1 = normx0 + ell2w / ellw;
		float normy0 = (y3 - y1) / ellh - 0.5f;
		float normy1 = normy0 + ell2h / ellh;
	
		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		float nearx, neary;
		if (normx0 > 0f) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0f) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0f;
		}
		if (normy0 > 0f) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0f) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0f;
		}
		return (nearx * nearx + neary * neary) < 0.25f;
	}

	/** Replies if an ellipse and a line are intersecting.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param x1 is the first point of the line.
	 * @param y1 is the first point of the line.
	 * @param x2 is the second point of the line.
	 * @param y2 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
	 */
	public static boolean intersectsEllipseLine(float ex, float ey, float ew, float eh, float x1, float y1, float x2, float y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0f || ew<=0f) {
			return false;
		}
	
		// Get the semimajor and semiminor axes.
		float a = ew / 2f;
		float b = eh / 2f;
	
		// Translate so the ellipse is centered at the origin.
		float ecx = ex + a;
		float ecy = ey + b;
		float px1 = x1 - ecx;
		float py1 = y1 - ecy;
		float px2 = x2 - ecx;
		float py2 = y2 - ecy;
		
		float sq_a = a*a;
		float sq_b = b*b;
		float vx = px2 - px1;
		float vy = py2 - py1;
		
		assert(sq_a!=0f && sq_b!=0f);
	
		// Calculate the quadratic parameters.
		float A = vx * vx / sq_a +
				vy * vy / sq_b;
		float B = 2f * px1 * vx / sq_a +
				2f * py1 * vy / sq_b;
		float C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;
	
		// Calculate the discriminant.
		float discriminant = B * B - 4f * A * C;
		return (discriminant>=0f);
	}

	/** Replies if an ellipse and a segment are intersecting.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the second point of the segment.
	 * @param y2 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
	 */
	public static boolean intersectsEllipseSegment(float ex, float ey, float ew, float eh, float x1, float y1, float x2, float y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0f || ew<=0f) {
			return false;
		}
	
		// Get the semimajor and semiminor axes.
		float a = ew / 2f;
		float b = eh / 2f;
	
		// Translate so the ellipse is centered at the origin.
		float ecx = ex + a;
		float ecy = ey + b;
		float px1 = x1 - ecx;
		float py1 = y1 - ecy;
		float px2 = x2 - ecx;
		float py2 = y2 - ecy;
		
		float sq_a = a*a;
		float sq_b = b*b;
		float vx = px2 - px1;
		float vy = py2 - py1;
		
		assert(sq_a!=0f && sq_b!=0f);
	
		// Calculate the quadratic parameters.
		float A = vx * vx / sq_a + vy * vy / sq_b;
		float B = 2f * px1 * vx / sq_a + 2f * py1 * vy / sq_b;
		float C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;
	
		// Calculate the discriminant.
		float discriminant = B * B - 4f * A * C;
		if (discriminant<0f) {
			// No solution
			return false;
		}
		
		if (discriminant==0f) {
			// One real solution.
			float t = -B / 2f / A;
			return ((t >= 0f) && (t <= 1f));
		}
	
		assert(discriminant>0f);
		
		// Two real solutions.
		float t1 = (-B + (float)Math.sqrt(discriminant)) / 2f / A;
		float t2 = (-B - (float)Math.sqrt(discriminant)) / 2f / A;
		
		return (t1>=0 || t2>=0) && (t1<=1 || t2<=1);
	}

	/** Replies if two ellipses are intersecting.
	 * 
	 * @param x1 is the first corner of the first ellipse.
	 * @param y1 is the first corner of the first ellipse.
	 * @param x2 is the second corner of the first ellipse.
	 * @param y2 is the second corner of the first ellipse.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsEllipseRectangle(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		// From AWT Ellipse2D
	
		float rectw = Math.abs(x4 - x3);
		float recth = Math.abs(y4 - y3);
		float ellw = Math.abs(x2 - x1);
		float ellh = Math.abs(y2 - y1);
	
		if (rectw <= 0f || recth <= 0f) return false;
		if (ellw <= 0f || ellh <= 0f) return false;
	
		// Normalize the rectangular coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		float normx0 = (x3 - x1) / ellw - 0.5f;
		float normx1 = normx0 + rectw / ellw;
		float normy0 = (y3 - y1) / ellh - 0.5f;
		float normy1 = normy0 + recth / ellh;
		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		float nearx, neary;
		if (normx0 > 0f) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0f) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0f;
		}
		if (normy0 > 0f) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0f) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0f;
		}
		return (nearx * nearx + neary * neary) < 0.25f;
	}

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the first rectangle.
	 * @param y1 is the first corner of the first rectangle.
	 * @param x2 is the second corner of the first rectangle.
	 * @param y2 is the second corner of the first rectangle.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsRectangleRectangle(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		assert(x1<=x2);
		assert(y1<=y2);
		assert(x3<=x4);
		assert(y3<=y4);
		return x2 > x3
				&&
				x1 < x4
				&&
				y2 > y3
				&&
				y1 < y4;
	}

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param x3 is the first point of the line.
	 * @param y3 is the first point of the line.
	 * @param x4 is the second point of the line.
	 * @param y4 is the second point of the line.
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsRectangleLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float epsilon) {
		int a, b;
		a = GeometryUtil.ccw(x3, y3, x4, y4, x1, y1, epsilon);
		b = GeometryUtil.ccw(x3, y3, x4, y4, x2, y1, epsilon);
		if (a!=b && b!=0) return true;
		b = GeometryUtil.ccw(x3, y3, x4, y4, x2, y2, epsilon);
		if (a!=b && b!=0) return true;
		b = GeometryUtil.ccw(x3, y3, x4, y4, x1, y2, epsilon);
		return (a!=b && b!=0);
	}

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param x3 is the first point of the segment.
	 * @param y3 is the first point of the segment.
	 * @param x4 is the second point of the segment.
	 * @param y4 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsRectangleSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float px1 = x3;
		float py1 = y3;
		float px2 = x4;
		float py2 = y4;
	
		// Cohen–Sutherland algorithm
		int r1 = GeometryUtil.getCohenSutherlandCode(x1, y1, x2, y2, px1, py1);
		int r2 = GeometryUtil.getCohenSutherlandCode(x1, y1, x2, y2, px2, py2);
		boolean accept = false;
	
		while (true) {
			if ((r1 | r2)==0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept =  true;
				break;//to speed up the algorithm
			}
			if ((r1 & r2)!=0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				break;
			}
	
			// failed both tests, so calculate the line segment to clip
			// from an outside point to an intersection with clip edge
			float x, y;
	
			// At least one endpoint is outside the clip rectangle; pick it.
			int outcodeOut = r1!=0 ? r1 : r2;
	
			// Now find the intersection point;
			// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
			if ((outcodeOut & COHEN_SUTHERLAND_TOP)!=0) { // point is above the clip rectangle
				x = px1 + (px2 - px1) * (y2 - py1) / (py2 - py1);
				y = y2;
			}
			else if ((outcodeOut & COHEN_SUTHERLAND_BOTTOM)!=0) { // point is below the clip rectangle
				x = px1 + (px2 - px1) * (y1- py1) / (py2 - py1);
				y = y1;
			}
			else if ((outcodeOut & COHEN_SUTHERLAND_RIGHT)!=0) {  // point is to the right of clip rectangle
				y = py1 + (py2 - py1) * (x2 - px1) / (px2 - px1);
				x = x2;
			}
			else {
				//else if ((outcodeOut & CS_LEFT)!=0) {   // point is to the left of clip rectangle
				y = py1 + (py2 - py1) * (x1 - px1) / (px2 - px1);
				x = x1;
			}
	
			//NOTE:*****************************************************************************************
	
			/* if you follow this algorithm exactly(at least for c#), then you will fall into an infinite loop 
	        in case a line crosses more than two segments. to avoid that problem, leave out the last else
	        if(outcodeOut & LEFT) and just make it else*/
	
			//**********************************************************************************************
	
			// Now we move outside point to intersection point to clip
			// and get ready for next pass.
			if (outcodeOut == r1) {
				px1 = x;
				py1 = y;
				r1 = GeometryUtil.getCohenSutherlandCode(x1, y1, x2, y2, px1, py1);
			}
			else {
				px2 = x;
				py2 = y;
				r2 = GeometryUtil.getCohenSutherlandCode(x1, y1, x2, y2, px2, py2);
			}
		}
	
		return accept;
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
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsLineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float epsilon) {
		if (GeometryUtil.isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4, epsilon)) {
			return GeometryUtil.isCollinearPoints(x1, y1, x2, y2, x3, y3, epsilon);
		}
		return true;
	}

	/** Replies if a segment and a line are intersecting.
	 * 
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the second point of the segment.
	 * @param y2 is the second point of the segment.
	 * @param x3 is the first point of the line.
	 * @param y3 is the first point of the line.
	 * @param x4 is the second point of the line.
	 * @param y4 is the second point of the line.
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	public static boolean intersectsSegmentLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float epsilon) {
		return (GeometryUtil.getPointSideOfLine(x3, y3, x4, y4, x1, y1, epsilon) *
				GeometryUtil.getPointSideOfLine(x3, y3, x4, y4, x2, y2, epsilon)) <= 0;
	}

	private static boolean intersectsSSWE(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float vx1, vy1, vx2a, vy2a, vx2b, vy2b, f1, f2, sign;
	
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
	
		float squaredLength = vx1*vx1 + vy1*vy1;
	
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

	private static boolean intersectsSSWoE(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float vx1, vy1, vx2a, vy2a, vx2b, vy2b, f1, f2, sign;
	
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
	
			float squaredLength = vx1*vx1 + vy1*vy1;
	
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
	 * {@link #intersectsSegmentSegmentWithEnds(float, float, float, float, float, float, float, float)}.
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
	 * @see #intersectsSegmentSegmentWithEnds(float, float, float, float, float, float, float, float)
	 */
	public static boolean intersectsSegmentSegmentWithoutEnds(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		boolean r;
		r = intersectsSSWoE(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r) return r;
		return intersectsSSWoE(x3, y3, x4, y4, x1, y1, x2, y2);
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(float, float, float, float, float, float, float, float)}.
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
	 * @see #intersectsSegmentSegmentWithoutEnds(float, float, float, float, float, float, float, float)
	 */
	public static boolean intersectsSegmentSegmentWithEnds(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		boolean r;
		r = intersectsSSWE(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!r) return r;
		return intersectsSSWE(x3, y3, x4, y4, x1, y1, x2, y2);
	}
    
}

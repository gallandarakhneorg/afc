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
public final class ClassifierUtil implements MathConstants{

	private ClassifierUtil() {
		//
	}
	
	/**
	 * Classify a sphere against an axis-aligned box.
	 * <p>
	 * This function assumes:
	 * <ul>
	 * <li><code>sphere_radius &gt;= 0</code></li>
	 * <li><code>lowerx &lt;= upperx</code></li>
	 * <li><code>lowery &lt;= uppery</code></li>
	 * <li><code>lowerz &lt;= upperz</code></li>
	 * </ul>
	 * 
	 * @param sphereCenterx is the X coordinate of the sphere center.
	 * @param sphereCentery is the Y coordinate of the sphere center.
	 * @param sphereCenterz is the Z coordinate of the sphere center.
	 * @param radius the radius of the sphere.
	 * @param lowerx is the X coordinate of the lowest point of the box.
	 * @param lowery is the Y coordinate of the lowest point of the box.
	 * @param lowerz is the Z coordinate of the lowest point of the box.
	 * @param upperx is the X coordinate of the uppermost point of the box.
	 * @param uppery is the Y coordinate of the uppermost point of the box.
	 * @param upperz is the Z coordinate of the uppermost point of the box.
	 * @return the value  {@link IntersectionType#INSIDE} if the <var>sphere</var> is inside the <var>box</var>;
	 * {@link IntersectionType#OUTSIDE} if the <var>sphere</var> is outside the <var>box</var>;
	 * {@link IntersectionType#ENCLOSING} if the <var>sphere</var> is enclosing the <var>box</var>;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesSolidSphereSolidAlignedBox(
			float sphereCenterx, float sphereCentery, float sphereCenterz, float radius,
			float lowerx, float lowery, float lowerz,
			float upperx, float uppery, float upperz) {
		
		// Assumptions
		assert(radius >= 0.);
		assert(lowerx <= upperx);
		assert(lowery <= uppery);
		assert(lowerz <= upperz);
		
		// Compute the distance betwen the sphere center and
		// the closest point of the box
	
		// Compute the distance between the sphere center and
		// the farest point of the box
	
		float dmin; // distance between the sphere center and the nearest point of the box. 
		float dmax; // distance between the sphere center and the farest point of the box.
		float a,b; // tmp value
		
		boolean sphereInsideOnAllAxis = false;
		
		dmin = dmax = 0.f;
		
		if (sphereCenterx<lowerx) {
			a = lowerx - sphereCenterx;
			dmin = a*a;
			a = upperx - sphereCenterx;
			dmax = a*a;
		}
		else if (sphereCenterx>upperx) {
			a = sphereCenterx - upperx;
			dmin = a*a;
			a = sphereCenterx - lowerx;
			dmax = a*a;
		}
		else {
			a = sphereCenterx-lowerx;
			b = upperx-sphereCenterx;
			if (a>=b) {
				sphereInsideOnAllAxis = (radius<b);
			}
			else {
				sphereInsideOnAllAxis = (radius<a);
				a = b;
			}
			dmax = a*a;
		}
		
		if (sphereCentery<lowery) {
			a = lowery - sphereCentery;
			dmin += a*a;
			a = uppery - sphereCentery;
			dmax += a*a;
		}
		else if (sphereCentery>uppery) {
			a = sphereCentery - uppery;
			dmin += a*a;
			a = sphereCentery - lowery;
			dmax += a*a;
		}
		else {
			a = sphereCentery-lowery;
			b = uppery-sphereCentery;
			if (a>=b) {
				sphereInsideOnAllAxis &= (radius<b);
			}
			else {
				sphereInsideOnAllAxis &= (radius<a);
				a = b;
			}
			dmax += a*a;
		}
		
		if (sphereCenterz<lowerz) {
			a = lowerz - sphereCenterz;
			dmin += a*a;
			a = upperz - sphereCenterz;
			dmax += a*a;
		}
		else if (sphereCenterz>upperz) {
			a = sphereCenterz - upperz;
			dmin += a*a;
			a = sphereCenterz - lowerz;
			dmax += a*a;
		}
		else {
			a = sphereCenterz-lowerz;
			b = upperz-sphereCenterz;
			if (a>=b) {
				sphereInsideOnAllAxis &= (radius<b);
			}
			else {
				sphereInsideOnAllAxis &= (radius<a);
				a = b;
			}
			dmax += a*a;
		}
	
		float sr = radius * radius;
		
		if (dmin==0.) {
			// Sphere center is inside the box.
			if (sphereInsideOnAllAxis) return IntersectionType.INSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		else {
			// Sphere center is outside the box.
			if (sr<=dmin) return IntersectionType.OUTSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		return IntersectionType.SPANNING;
	}

	/**
	 * Classify a circle against an minimum bounding rectangle.
	 * <p>
	 * This function assumes:
	 * <ul>
	 * <li><code>radius &gt;= 0</code></li>
	 * <li><code>lowerx &lt;= upperx</code></li>
	 * <li><code>lowery &lt;= uppery</code></li>
	 * </ul>
	 *
	 * 
	 * @param circleCenterx is the X coordinate of the circle center.
	 * @param circleCentery is the Y coordinate of the circle center.
	 * @param radius the radius of the circle.
	 * @param lowerx is the X coordinate of the lowest point of the box.
	 * @param lowery is the Y coordinate of the lowest point of the box.
	 * @param upperx is the X coordinate of the uppermost point of the box.
	 * @param uppery is the Y coordinate of the uppermost point of the box.
	 * @return the value  {@link IntersectionType#INSIDE} if the <var>sphere</var> is inside the <var>rectangle</var>;
	 * {@link IntersectionType#OUTSIDE} if the <var>circle</var> is outside the <var>rectangle</var>;
	 * {@link IntersectionType#ENCLOSING} if the <var>circle</var> is enclosing the <var>rectangle</var>;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesSolidCircleSolidAlignedRectangle(
			float circleCenterx, float circleCentery, float radius,
			float lowerx, float lowery,	float upperx, float uppery) {
		// Assumptions
		assert(radius >= 0.);
		assert(lowerx <= upperx);
		assert(lowery <= uppery);
		
		// Compute the distance between the circle center and
		// the closest point of the box
	
		// Compute the distance between the circle center and
		// the farthest point of the box
	
		float dmin; // distance between the circle center and the nearest point of the box. 
		float dmax; // distance between the circle center and the farthest point of the box.
		float a,b; // tmp value
		
		boolean circleInsideOnAllAxis = false;
		
		dmin = dmax = 0.f;
		
		if (circleCenterx<lowerx) {
			a = lowerx - circleCenterx;
			dmin = a*a;
			a = upperx - circleCenterx;
			dmax = a*a;
		}
		else if (circleCenterx>upperx) {
			a = circleCenterx - upperx;
			dmin = a*a;
			a = circleCenterx - lowerx;
			dmax = a*a;
		}
		else {
			a = circleCenterx-lowerx;
			b = upperx-circleCenterx;
			if (a>=b) {
				circleInsideOnAllAxis = (radius<b);
			}
			else {
				circleInsideOnAllAxis = (radius<a);
				a = b;
			}
			dmax = a*a;
		}
		
		if (circleCentery<lowery) {
			a = lowery - circleCentery;
			dmin += a*a;
			a = uppery - circleCentery;
			dmax += a*a;
		}
		else if (circleCentery>uppery) {
			a = circleCentery - uppery;
			dmin += a*a;
			a = circleCentery - lowery;
			dmax += a*a;
		}
		else {
			a = circleCentery-lowery;
			b = uppery-circleCentery;
			if (a>=b) {
				circleInsideOnAllAxis &= (radius<b);
			}
			else {
				circleInsideOnAllAxis &= (radius<a);
				a = b;
			}
			dmax += a*a;
		}
		
		float sr = radius * radius;
		
		if (dmin==0.) {
			// circle center is inside the box.
			if (circleInsideOnAllAxis) return IntersectionType.INSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		else {
			// circle center is outside the box.
			if (sr<=dmin) return IntersectionType.OUTSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		return IntersectionType.SPANNING;
	}

	/**
	 * Classifies two 1D segments.
	 * <p>
	 * This function is assuming that <var>l1</var> is lower
	 * or equal to <var>u1</var> and <var>l2</var> is lower
	 * or equal to <var>u2</var>.
	 *
	 * @param l1 the min coordinate of the first segment
	 * @param u1 the max coordinate of the first segment
	 * @param l2 the min coordinate of the second segment
	 * @param u2 the max coordinate of the second segment
	 * @return the value {@link IntersectionType#INSIDE} if the first segment is inside
	 * the second segment; {@link IntersectionType#OUTSIDE} if the first segment is 
	 * outside the second segment; {@link IntersectionType#ENCLOSING} if the 
	 * first segment is enclosing the second segment;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesAlignedSegments(float l1, float u1, float l2, float u2) {
		assert(l1<=u1);
		assert(l2<=u2);
		if (l1<l2) {
			if (u1<l2) return IntersectionType.OUTSIDE;
			if (u1>u2) return IntersectionType.ENCLOSING;
			return IntersectionType.SPANNING;
		}
		if (u2<l1) return IntersectionType.OUTSIDE;
		if (u2>u1) return IntersectionType.INSIDE;
		return IntersectionType.SPANNING;
	}

	/**
	 * Classifies two 2D axis-aligned rectangles.
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
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#ENCLOSING} if the first rectangle is the same as the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesAlignedRectangles(float lx1, float ly1, float ux1, float uy1, float lx2, float ly2, float ux2, float uy2) {
		assert(lx1<=ux1);
		assert(ly1<=uy1);
		assert(lx2<=ux2);
		assert(ly2<=uy2);
	
		IntersectionType inter;
		
		if (lx1<lx2) {
			if (ux1<=lx2) return IntersectionType.OUTSIDE;
			if (ux1<ux2) inter = IntersectionType.SPANNING;
			else inter = IntersectionType.ENCLOSING;
		} else if (lx1>lx2) {
			if (ux2<=lx1) return IntersectionType.OUTSIDE;
			if (ux1<=ux2) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.SPANNING;
		} else {
			if (ux1==ux2) inter = IntersectionType.SAME;
			else if (ux1<ux2) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.ENCLOSING;
		}
		
		if (ly1<ly2) {
			if (uy1<=ly2) return IntersectionType.OUTSIDE;
			if (uy1<uy2) return inter.and(IntersectionType.SPANNING);
			return inter.and(IntersectionType.ENCLOSING);
		} else if (ly1>ly2) {
			if (uy2<=ly1) return IntersectionType.OUTSIDE;
			if (uy1<=uy2) return inter.and(IntersectionType.INSIDE);
			return inter.and(IntersectionType.SPANNING);
		} else {
			if (uy1==uy2) return inter.and(IntersectionType.SAME);
			else if (uy1<uy2) return inter.and(IntersectionType.INSIDE);
			return inter.and(IntersectionType.ENCLOSING);
		}
		
	}


	/**
	 * Classifies two 3D axis-aligned boxes.
	 * <p>
	 * This function is assuming that <var>lower1x</var> is lower
	 * or equal to <var>upper1x</var>, <var>lower1y</var> is lower
	 * or equal to <var>upper1y</var>, and so on.
	 * <p>
	 * @param lower1x is the X coordinate of the lowest point of the first box.
	 * @param lower1y is the Y coordinate of the lowest point of the first box.
	 * @param lower1z is the Z coordinate of the lowest point of the first box.
	 * @param upper1x is the X coordinate of the uppermost point of the first box.
	 * @param upper1y is the Y coordinate of the uppermost point of the first box.
	 * @param upper1z is the Z coordinate of the uppermost point of the first box.
	 * @param lower2x is the X coordinate of the lowest point of the second box.
	 * @param lower2y is the Y coordinate of the lowest point of the second box.
	 * @param lower2z is the Z coordinate of the lowest point of the second box.
	 * @param upper2x is the X coordinate of the uppermost point of the second box.
	 * @param upper2y is the Y coordinate of the uppermost point of the second box.
	 * @param upper2z is the Z coordinate of the uppermost point of the second box.
	 * @return the value {@link IntersectionType#INSIDE} if the first box is inside
	 * the second box; {@link IntersectionType#OUTSIDE} if the first box is 
	 * outside the second box; {@link IntersectionType#ENCLOSING} if the 
	 * first box is enclosing the second box;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesAlignedBoxes(
			float lower1x, float lower1y, float lower1z,
			float upper1x, float upper1y, float upper1z, 
			float lower2x, float lower2y, float lower2z,
			float upper2x, float upper2y, float upper2z) {
		assert(lower1x<=upper1x);
		assert(lower1y<=upper1y);
		assert(lower1z<=upper1z);
		assert(lower2x<=upper2x);
		assert(lower2y<=upper2y);
		assert(lower2z<=upper2z);
		
		IntersectionType inter;
		
		if (lower1x<lower2x) {
			if (upper1x<=lower2x) return IntersectionType.OUTSIDE;
			if (upper1x<upper2x) inter = IntersectionType.SPANNING;
			else inter = IntersectionType.ENCLOSING;
		} else if (lower1x>lower2x) {
			if (upper2x<=lower1x) return IntersectionType.OUTSIDE;
			if (upper1x<=upper2x) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.SPANNING;
		} else {
			if (upper1x==upper2x) inter = IntersectionType.SAME;
			else if (upper1x<upper2x) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.ENCLOSING;
		}
		
		if (lower1y<lower2y) {
			if (upper1y<=lower2y) return IntersectionType.OUTSIDE;
			if (upper1y<upper2y) inter = inter.and(IntersectionType.SPANNING);
			else inter = inter.and(IntersectionType.ENCLOSING);
		} else if (lower1y>lower2y) {
			if (upper2y<=lower1y) return IntersectionType.OUTSIDE;
			if (upper1y<=upper2y) inter = inter.and(IntersectionType.INSIDE);
			else inter = inter.and(IntersectionType.SPANNING);
		} else {
			if (upper1y==upper2y) inter = inter.and(IntersectionType.SAME);
			else if (upper1y<upper2y) inter = inter.and(IntersectionType.INSIDE);
			else inter = inter.and(IntersectionType.ENCLOSING);
		}
		
		if (lower1z<lower2z) {
			if (upper1z<=lower2z) return IntersectionType.OUTSIDE;
			if (upper1z<upper2z) return inter.and(IntersectionType.SPANNING);
			return inter.and(IntersectionType.ENCLOSING);
		} else if (lower1z>lower2z) {
			if (upper2z<=lower1z) return IntersectionType.OUTSIDE;
			if (upper1z<=upper2z) return inter.and(IntersectionType.INSIDE);
			return inter.and(IntersectionType.SPANNING);
		}
		if (upper1z==upper2z) return inter.and(IntersectionType.SAME);
		else if (upper1z<upper2z) return inter.and(IntersectionType.INSIDE);
		return inter.and(IntersectionType.ENCLOSING);
		
	}

	/**
	 * Classifies two 2D minimum bounding rectangles.
	 * <p>
	 * This function is assuming that <var>lower1x</var> is lower
	 * or equal to <var>upper1x</var>, and <var>lower1y</var> is lower
	 * or equal to <var>upper1y</var>.
	 * <p>
	 * This function is implemented in the best efficient way according
	 * to the priority of the intersections types which are deduced
	 * from {@link IntersectionType#and(IntersectionType, IntersectionType)}:
	 * a) if one axis is {@code OUTSIDE} the boxes are {@code OUTSIDE}, b) if
	 * one axis is {@code SPANNING} the boxes are {@code SPANNING}, c) otherwise
	 * the "and" rule is applied.
	 *
	 * @param lower1x is the X coordinate of the lowest point of the first rectangle.
	 * @param lower1y is the Y coordinate of the lowest point of the first rectangle.
	 * @param upper1x is the X coordinate of the uppermost point of the first rectangle.
	 * @param upper1y is the Y coordinate of the uppermost point of the first rectangle.
	 * @param lower2x is the X coordinate of the lowest point of the second rectangle.
	 * @param lower2y is the Y coordinate of the lowest point of the second rectangle.
	 * @param upper2x is the X coordinate of the uppermost point of the second rectangle.
	 * @param upper2y is the Y coordinate of the uppermost point of the second rectangle.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesAlignedRectangles(
			float lower1x, float lower1y, float lower1z,
			float upper1x, float upper1y, float upper1z, 
			float lower2x, float lower2y, float lower2z,
			float upper2x, float upper2y, float upper2z) {
		assert(lower1x<=upper1x);
		assert(lower1y<=upper1y);
		assert(lower2x<=upper2x);
		assert(lower2y<=upper2y);
	
		IntersectionType inter;
		
		if (lower1x<lower2x) {
			if (upper1x<=lower2x) return IntersectionType.OUTSIDE;
			if (upper1x<upper2x) inter = IntersectionType.SPANNING;
			else inter = IntersectionType.ENCLOSING;
		} else if (lower1x>lower2x) {
			if (upper2x<=lower1x) return IntersectionType.OUTSIDE;
			if (upper1x<=upper2x) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.SPANNING;
		} else {
			if (upper1x==upper2x) inter = IntersectionType.SAME;
			else if (upper1x<upper2x) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.ENCLOSING;
		}
		
		if (lower1y<lower2y) {
			if (upper1y<=lower2y) return IntersectionType.OUTSIDE;
			if (upper1y<upper2y) return inter.and(IntersectionType.SPANNING);
			return inter.and(IntersectionType.ENCLOSING);
		} else if (lower1y>lower2y) {
			if (upper2y<=lower1y) return IntersectionType.OUTSIDE;
			if (upper1y<=upper2y) return inter.and(IntersectionType.INSIDE);
			return inter.and(IntersectionType.SPANNING);
		}
		if (upper1y==upper2y) return inter.and(IntersectionType.SAME);
		else if (upper1y<upper2y) return inter.and(IntersectionType.INSIDE);
		else return inter.and(IntersectionType.ENCLOSING);
		
	}

	/**
	 * Classifies a sphere against an oriented box.
	 * <p>
	 * The extents are assumed to be positive or zero.
	 *
	 * @param sphereCenterx is the X coordinate of the sphere center.
	 * @param sphereCentery is the Y coordinate of the sphere center.
	 * @param sphereCenterz is the Z coordinate of the sphere center.
	 * @param radius is the radius of the sphere.
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
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return the value {@link IntersectionType#INSIDE} if the sphere is inside
	 * the box; {@link IntersectionType#OUTSIDE} if the sphere is 
	 * outside the box; {@link IntersectionType#ENCLOSING} if the 
	 * sphere is enclosing the box;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesSolidSphereOrientedBox(
			   float sphereCenterx, float sphereCentery, float sphereCenterz, float sphereRadius,
			   float boxCenterx, float boxCentery, float boxCenterz,
			   float boxAxis1x, float boxAxis1y, float boxAxis1z,
			   float boxAxis2x, float boxAxis2y, float boxAxis2z,
			   float boxAxis3x, float boxAxis3y, float boxAxis3z,
			   float boxExtentAxis1, float boxExtentAxis2, float boxExtentAxis3,float epsilon) {
		assert(sphereRadius>=0);
		assert(boxExtentAxis1>=0);
		assert(boxExtentAxis2>=0);
		assert(boxExtentAxis3>=0);
		
		// Find points on OBB closest and farest to sphere center
		Point3f closest = new Point3f();
		Point3f farest = new Point3f();
		GeometryUtil.closestFarthestPointsOBBPoint(
				boxCenterx,  boxCentery,  boxCenterz,
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
		
		// Compute (squared) distance to closest point
		if (GeometryUtil.distanceSquaredPointPoint(farest.getX(),farest.getY(), farest.getZ(), sphereCenterx, sphereCentery, sphereCenterz)<squaredRadius) 
			return IntersectionType.ENCLOSING;
		
		// Compute (squared) distance to closest point
		float d = GeometryUtil.distanceSquaredPointPoint(closest.getX(),closest.getY(), closest.getZ(), sphereCenterx, sphereCentery, sphereCenterz);
		if (d>squaredRadius+epsilon) return IntersectionType.OUTSIDE;
	
		// If the sphere center is inside the box and the
		// radius is inside the box's extents, then the
		// sphere is inside the box.
		
		if (MathUtil.isEpsilonZero(d, epsilon)) {
			
			float vx= sphereCenterx - boxCenterx;
			float vy= sphereCentery - boxCentery;
			float vz= sphereCenterz - boxCenterz;
			
			float d1,d2,d3;
			d1 = Math.abs(MathUtil.dotProduct(boxAxis1x, boxAxis1y, boxAxis1z, vx, vy, vz));
			d2 = Math.abs(MathUtil.dotProduct(boxAxis2x, boxAxis2y, boxAxis2z, vx, vy, vz));
			d3 = Math.abs(MathUtil.dotProduct(boxAxis3x, boxAxis3y, boxAxis3z, vx, vy, vz));
			
			if(d1+sphereRadius > boxExtentAxis1 || d2+sphereRadius > boxExtentAxis2 || d3+sphereRadius > boxExtentAxis3)
				return IntersectionType.SPANNING;
			return IntersectionType.INSIDE;
		}
		
		return IntersectionType.SPANNING;
	
	}

	/**
	 * Classifies a circle against an oriented rectangle.
	 * <p>
	 * The extents are assumed to be positive or zero just as circleRadius.
	 *
	 * @param circleCenterx is the X coordinate of the circle center.
	 * @param circleCentery is the Y coordinate of the circle center.
	 * @param radius is the radius of the circle.
	 * @param centerx is the X coordinate of the rectangle center.
	 * @param centery is the Y coordinate of the rectangle center.
	 * @param rectangleAxis1x is the X coordinate of the Axis1 unit vector.
	 * @param rectangleAxis1y is the Y coordinate of the Axis1 unit vector.
	 * @param rectangleAxis2x is the X coordinate of the Axis2 unit vector.
	 * @param rectangleAxis2y is the Y coordinate of the Axis2 unit vector.
	 * @param rectangleExtentAxis1 is the 'Axis1' size of the OBR.
	 * @param rectangleExtentAxis2 is the 'Axis2' size of the OBR.
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return the value {@link IntersectionType#INSIDE} if the circle is inside
	 * the rectangle; {@link IntersectionType#OUTSIDE} if the circle is 
	 * outside the rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * circle is enclosing the rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesSolidCircleOrientedRectangle(
			   float circleCenterx, float circleCentery, float circleRadius,
			   float centerx, float centery,
			   float rectangleAxis1x, float rectangleAxis1y,
			   float rectangleAxis2x, float rectangleAxis2y,
			   float rectangleAxis3x, float rectangleAxis3y,
			   float rectangleExtentAxis1, float rectangleExtentAxis2,float epsilon) {
		assert(circleRadius>=0);
		assert(rectangleExtentAxis1>=0);
		assert(rectangleExtentAxis2>=0);
		
		// Find points on OBB closest and farest to sphere center
		Point2f closest = new Point2f();
		Point2f farest = new Point2f();
		GeometryUtil.closestFarthestPointsOBRPoint(
				centerx,  centery,
				rectangleAxis1x, rectangleAxis1y,
				rectangleAxis2x, rectangleAxis2y,
				rectangleExtentAxis1, rectangleExtentAxis2,
				circleCenterx, circleCentery,
				closest,
				farest);
		
		// Sphere and OBB intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		float squaredRadius = circleRadius * circleRadius;
		
		// Compute (squared) distance to closest point
		if (GeometryUtil.distanceSquaredPointPoint(farest.getX(),farest.getY(), circleCenterx, circleCentery)<squaredRadius) 
			return IntersectionType.ENCLOSING;
		
		// Compute (squared) distance to closest point
		float d = GeometryUtil.distanceSquaredPointPoint(closest.getX(),closest.getY(), circleCenterx, circleCentery);
		if (d>squaredRadius+epsilon) return IntersectionType.OUTSIDE;
	
		// If the sphere center is inside the rectangle and the
		// radius is inside the rectangle's extents, then the
		// sphere is inside the rectangle.
		
		if (MathUtil.isEpsilonZero(d, epsilon)) {
			
			float vx= circleCenterx - centerx;
			float vy= circleCentery - centery;
			
			float d1,d2;
			d1 = Math.abs(MathUtil.dotProduct(rectangleAxis1x, rectangleAxis1y, vx, vy));
			d2 = Math.abs(MathUtil.dotProduct(rectangleAxis2x, rectangleAxis2y, vx, vy));
			
			if(d1+circleRadius > rectangleExtentAxis1 || d2+circleRadius > rectangleExtentAxis2)
				return IntersectionType.SPANNING;
			return IntersectionType.INSIDE;
		}
		
		return IntersectionType.SPANNING;
	
	}

	/**
	 * Classifies the OBB's axis according to {@code |T.L|}, {@code ra} and {@rb}
	 * where {@code T} is the vector between the OBB's centers, {@code L} is
	 * the separation vector, {@code ra} is the size of the projection of the first
	 * OBB on L, and {@code rb} is the size of the projection of the second
	 * OBB on L.
	 * <p>
	 * This function is also working fine for 2D classifications.
	 * 
	 * @param tl is {@code |T.L|}
	 * @param ra is the size of the projection of the first OBB on L
	 * @param rb is the size of the projection of the second OBB on L
	 * @param type is the intersection type previously detected for the other axis.
	 * @return the type of intersection
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">Gamasutra OBB intersection test</a>
	 * @see #classifiesOrientedBoxes(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)
	 * @see #classifiesOrientedRectangles(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)
	 */
	private static IntersectionType classifiesOrientedBoxAxis(float tl, float ra, float rb, IntersectionType type) {
		// Special case: same center, same radius.
		if (tl==0. && ra==rb) return type;
		// A and B do not overlap.
		if (tl > ra+rb) return IntersectionType.OUTSIDE;
		
		IntersectionType t;
		
		// A is enclosing B
		if (tl+rb < ra) t = IntersectionType.ENCLOSING;
		// A is inside B
		else if (tl+ra < rb) t = IntersectionType.INSIDE;
		// A and B do overlap
		else return IntersectionType.SPANNING;
		
		return (type==null) ? t : IntersectionType.and(type, t);
	}

	/**
	 * Classifies two oriented boxes.
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
	 * {@link #classifiesAlignedBoxOrientedBox(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)}.
	 *
	 * @param center1x is the X coordinate of the box1 center.
	 * @param center1y is the Y coordinate of the box1 center.
	 * @param center1z is the Z coordinate of the box1 center.
	 * @param box1Axis1x is the X coordinate of the Axis1 unit vector.
	 * @param box1Axis1y is the Y coordinate of the Axis1 unit vector.
	 * @param box1Axis1z is the Z coordinate of the Axis1 unit vector.
	 * @param box1Axis2x is the X coordinate of the Axis2 unit vector.
	 * @param box1Axis2y is the Y coordinate of the Axis2 unit vector.
	 * @param box1Axis2z is the Z coordinate of the Axis2 unit vector.
	 * @param box1Axis3x is the X coordinate of the Axis3 unit vector.
	 * @param box1Axis3y is the Y coordinate of the Axis3 unit vector.
	 * @param box1Axis3z is the Z coordinate of the Axis3 unit vector.
	 * @param box1ExtentAxis1 is the 'Axis1' size of the box1.
	 * @param box1ExtentAxis2 is the 'Axis2' size of the box1.
	 * @param box1ExtentAxis3 is the 'Axis3' size of the box1.
	 * @param center2x is the X coordinate of the box2 center.
	 * @param center2y is the Y coordinate of the box2 center.
	 * @param center2z is the Z coordinate of the box2 center.
	 * @param box2Axis1x is the X coordinate of the Axis1 unit vector.
	 * @param box2Axis1y is the Y coordinate of the Axis1 unit vector.
	 * @param box2Axis1z is the Z coordinate of the Axis1 unit vector.
	 * @param box2Axis2x is the X coordinate of the Axis2 unit vector.
	 * @param box2Axis2y is the Y coordinate of the Axis2 unit vector.
	 * @param box2Axis2z is the Z coordinate of the Axis2 unit vector.
	 * @param box2Axis3x is the X coordinate of the Axis3 unit vector.
	 * @param box2Axis3y is the Y coordinate of the Axis3 unit vector.
	 * @param box2Axis3z is the Z coordinate of the Axis3 unit vector.
	 * @param box2ExtentAxis1 is the 'Axis1' size of the box2.
	 * @param box2ExtentAxis2 is the 'Axis2' size of the box2.
	 * @param box2ExtentAxis3 is the 'Axis3' size of the box2.
	 * @return the value {@link IntersectionType#INSIDE} if the first box is inside
	 * the second box; {@link IntersectionType#OUTSIDE} if the first box is 
	 * outside the second box; {@link IntersectionType#ENCLOSING} if the 
	 * first box is enclosing the second box;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">Gamasutra OBB intersection test</a>
	 */
	public static IntersectionType classifiesOrientedBoxes(
				  float center1x, float center1y, float center1z,
				  float box1Axis1x, float box1Axis1y, float box1Axis1z,
				  float box1Axis2x, float box1Axis2y, float box1Axis2z,
				  float box1Axis3x, float box1Axis3y, float box1Axis3z,
				  float box1ExtentAxis1, float box1ExtentAxis2, float box1ExtentAxis3,
				  float center2x, float center2y, float center2z,
				  float box2Axis1x, float box2Axis1y, float box2Axis1z,
				  float box2Axis2x, float box2Axis2y, float box2Axis2z,
				  float box2Axis3x, float box2Axis3y, float box2Axis3z,
				  float box2ExtentAxis1, float box2ExtentAxis2, float box2ExtentAxis3) {
		
		assert(box1ExtentAxis1>=0);
		assert(box1ExtentAxis2>=0);
		assert(box1ExtentAxis3>=0);
		assert(box2ExtentAxis1>=0);
		assert(box2ExtentAxis2>=0);
		assert(box2ExtentAxis3>=0);
		
		//translation, in parent frame
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
		IntersectionType type = null;
	
		
		ra = box1ExtentAxis1;
		rb = box2ExtentAxis1*absR_1_1+ box2ExtentAxis2*absR_1_2 + box2ExtentAxis3*absR_1_3;
		t = Math.abs(tx);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		ra = box1ExtentAxis2;
		rb = box2ExtentAxis1*absR_2_1+ box2ExtentAxis2*absR_2_2 + box2ExtentAxis3*absR_3_3;
		t = Math.abs(ty);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
		
		ra = box1ExtentAxis3;
		rb = box2ExtentAxis1*absR_3_1+ box2ExtentAxis2*absR_3_2 + box2ExtentAxis3*absR_3_3;
		t = Math.abs(tz);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//B's basis vectors
		ra = box1ExtentAxis1*absR_1_1+ box1ExtentAxis2*absR_2_1 + box1ExtentAxis3*absR_3_1;
		rb = box2ExtentAxis1;
		t =	Math.abs( tx*R_1_1 + ty*R_2_1 + tz*R_3_1 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
		
		ra = box1ExtentAxis1*absR_1_2+ box1ExtentAxis2*absR_2_2 + box1ExtentAxis3*absR_3_2;
		rb = box2ExtentAxis2;
		t =	Math.abs( tx*R_1_2 + ty*R_2_2 + tz*R_3_2 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
		
		ra = box1ExtentAxis1*absR_1_3+ box1ExtentAxis2*absR_2_3 + box1ExtentAxis3*absR_3_3;
		rb = box2ExtentAxis3;
		t =	Math.abs( tx*R_1_3 + ty*R_2_3 + tz*R_3_3 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//9 cross products

		//L = A0 x B0
		ra = box1ExtentAxis1*absR_3_1 + box1ExtentAxis3*absR_2_1;
		rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_2;
		t = Math.abs( tz*R_2_1 - ty*R_3_1 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
		
		
		ra = box1ExtentAxis2*absR_3_1 + box1ExtentAxis3*absR_2_1;
		rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_2;
		t = Math.abs( tz*R_2_1 - ty*R_3_1 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//L = A0 x B1
		ra = box1ExtentAxis2*absR_3_2 + box1ExtentAxis3*absR_2_2;
		rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_1;
		t = Math.abs( tz*R_2_2 - ty*R_3_2 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//L = A0 x B2
		ra = box1ExtentAxis2*absR_3_3 + box1ExtentAxis3*absR_2_3;
		rb = box1ExtentAxis3*absR_1_2 + box1ExtentAxis3*absR_1_1;
		t = Math.abs( tz*R_2_3 - ty*R_3_3 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//L = A1 x B0
		ra = box1ExtentAxis1*absR_3_1 + box1ExtentAxis3*absR_1_1;
		rb = box1ExtentAxis3*absR_2_3 + box1ExtentAxis3*absR_2_2;
		t = Math.abs( tx*R_3_1 - tz*R_1_1 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//L = A1 x B1
		ra = box1ExtentAxis1*absR_3_2 + box1ExtentAxis3*absR_1_2;
		rb = box1ExtentAxis3*absR_2_3 + box1ExtentAxis3*absR_2_1;
		t = Math.abs( tx*R_3_2 - tz*R_1_2 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//L = A1 x B2
		ra = box1ExtentAxis1*absR_3_3 + box1ExtentAxis3*absR_1_3;
		rb = box1ExtentAxis3*absR_2_2 + box1ExtentAxis3*absR_2_1;
		t = Math.abs( tx*R_3_3 - tz*R_1_3 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//L = A2 x B0
		ra = box1ExtentAxis1*absR_2_1 + box1ExtentAxis2*absR_1_1;
		rb = box1ExtentAxis3*absR_3_3 + box1ExtentAxis3*absR_3_2;
		t = Math.abs( ty*R_1_1 - tx*R_2_1 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		//L = A2 x B1
		ra = box1ExtentAxis1*absR_2_2 + box1ExtentAxis2*absR_1_2;
		rb = box1ExtentAxis3*absR_3_3 + box1ExtentAxis3*absR_3_1;
		t = Math.abs( ty*R_1_2 - tx*R_2_2 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
		
		//L = A2 x B2
		ra = box1ExtentAxis1*absR_2_3 + box1ExtentAxis2*absR_1_3;
		rb = box1ExtentAxis3*absR_3_2 + box1ExtentAxis3*absR_3_1;
		t = Math.abs( ty*R_1_3 - tx*R_2_3 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		/*no separating axis found, the two boxes overlap */
	
		return type;
	}

	/**
	 * Classifies two oriented rectangles.
	 * <p>
	 * The extents are assumed to be positive or zero.
	 * The lengths of the given arrays are assumed to be <code>2</code>.
	 * <p>
	 * This function uses the "separating axis theorem" which states that 
	 * for any two OBRs (MBRis a special case of OBR)
	 * that do not touch, a separating axis can be found.
	 * <p>
	 * This function uses an general intersection test between two OBR.
	 * If the first box is expected to be an AAR, please use the
	 * optimized algorithm given by
	 * {@link #classifiesAlignedRectangleOrientedRectangle(float, float, float, float, float, float, float, float, float, float, float, float)}.
	 * <p>
	 * <strong>Basic Algorithm:</strong>
	 * <>
	 * To be able to decide whether two polygons are intersecting (touching each other) 
	 * we can use the following basic facts:
	 * <ul>
	 * <li>If two convex polygons are not intersecting, there exists a line that passes between them.</li>
	 * <li>Such a line only exists if one of the sides of one of the polygons forms such a line.</li>
	 * </ul>
	 * <p>
	 * The first statement is easy. Since the polygons are both convex, you'll be able to draw a line 
	 * with one polygon on one side and the other polygon on the other side unless they are intersecting. 
	 * The second is slightly less intuitive.
	 * <center><img src="./doc-files/obr_fig1.png" alt="Dividing axis"></center> 
	 * Unless the closest sided of the polygons are parallel to each other, the 
	 * point where they get closest to each other is the point where a corner of one 
	 * polygon gets closest to a side of the other polygon. This side will then 
	 * form a separating axis between the polygons. If the sides are parallel, 
	 * they both are separating axes.
	 * <p>
	 * How does this concretely help us decide whether polygon A and B intersect? 
	 * We just go over each side of each polygon and check whether it forms a 
	 * separating axis. To do this we'll be using some basic vector math to 
	 * squash all the points of both polygons onto a line that is perpendicular 
	 * to the potential separating line. 
	 * <center><img src="./doc-files/obr_fig2.png" alt="Projecting polygons onto a line"></center>
	 * Now the whole problem is conveniently 1-dimensional. We can determine a region 
	 * in which the points for each polygon lie, and this line is a separating axis 
	 * if these regions do not overlap. 
	 * <p>
	 * If, after checking each line from both polygons, no separating axis was 
	 * found, it has been proven that the polygons intersect and something has 
	 * to be done about it. 
	 *
	 * @param center1x is the X coordinate of the rect1 center.
	 * @param center1y is the Y coordinate of the rect1 center.
	 * @param rect1Axis1x is the X coordinate of the Axis1 unit vector.
	 * @param rect1Axis1y is the Y coordinate of the Axis1 unit vector.
	 * @param rect1Axis2x is the X coordinate of the Axis2 unit vector.
	 * @param rect1Axis2y is the Y coordinate of the Axis2 unit vector.
	 * @param rect1ExtentAxis1 is the 'Axis1' size of the rect1.
	 * @param rect1ExtentAxis2 is the 'Axis2' size of the rect1.
	 * @param center2x is the X coordinate of the rect2 center.
	 * @param center2y is the Y coordinate of the rect2 center.
	 * @param rect2Axis1x is the X coordinate of the Axis1 unit vector.
	 * @param rect2Axis1y is the Y coordinate of the Axis1 unit vector.
	 * @param rect2Axis2x is the X coordinate of the Axis2 unit vector.
	 * @param rect2Axis2y is the Y coordinate of the Axis2 unit vector.
	 * @param rect2ExtentAxis1 is the 'Axis1' size of the rect2.
	 * @param rect2ExtentAxis2 is the 'Axis2' size of the rect2.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">Gamasutra OBB intersection test</a>
	 */
	public static IntersectionType classifiesOrientedRectangles(
			float center1x, float center1y, float rect1Axis1x, float rect1Axis1y, float rect1Axis2x, float  rect1Axis2y, float rect1ExtentAxis1, float rect1ExtentAxis2,
    		float center2x, float center2y, float rect2Axis1x, float rect2Axis1y, float rect2Axis2x, float  rect2Axis2y, float rect2ExtentAxis1, float rect2ExtentAxis2){
    	assert(rect1ExtentAxis1>=0);
    	assert(rect1ExtentAxis2>=0);
    	assert(rect2ExtentAxis1>=0);
    	assert(rect2ExtentAxis2>=0);

    	float tx, ty;
    	tx = center2x - center1x;
    	ty = center2y - center1y;
    	
		//B's basis with respect to A's local frame
		float R_1_1, R_1_2,
		  	  R_2_1, R_2_2,
			  
			  absR_1_1, absR_1_2,
			  absR_2_1, absR_2_2;
	
		R_1_1 = MathUtil.dotProduct(rect1Axis1x, rect1Axis1y,rect2Axis1x, rect2Axis1y); absR_1_1 = (R_1_1 < 0) ? -R_1_1 : R_1_1;
		R_1_2 = MathUtil.dotProduct(rect1Axis1x, rect1Axis1x,rect2Axis2x, rect2Axis2y); absR_1_2 = (R_1_2 < 0) ? -R_1_2 : R_1_2;
		R_2_1 = MathUtil.dotProduct(rect1Axis2x, rect1Axis2y,rect2Axis1x, rect2Axis1y); absR_2_1 = (R_2_1 < 0) ? -R_2_1 : R_2_1;
		R_2_2 = MathUtil.dotProduct(rect1Axis2x, rect1Axis2y,rect2Axis2x, rect2Axis2y); absR_2_2 = (R_2_2 < 0) ? -R_2_2 : R_2_2;

		float ra, rb, t;
        IntersectionType type = null;
    	
		//L = A0
		ra = rect1ExtentAxis1;
		rb = rect2ExtentAxis1*absR_1_1 + rect2ExtentAxis2*absR_1_2;
		t = Math.abs(tx);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = A1
		ra = rect1ExtentAxis2;
		rb = rect2ExtentAxis1*absR_2_1 + rect2ExtentAxis2*absR_2_2;
		t = Math.abs(ty);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = B0
		ra = rect1ExtentAxis1*absR_1_1 + rect1ExtentAxis2*absR_2_1;
		rb = rect2ExtentAxis1;
		t = tx*absR_1_1 + ty*absR_2_1;
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = B1
		ra = rect1ExtentAxis1*absR_1_2 + rect1ExtentAxis2*absR_2_2;
		rb = rect2ExtentAxis2;
		t = tx*absR_1_2 + ty*absR_2_2;
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = A0 x B0, ra = rb = t = 0, discarted
		
		//L = A0 x B1, ra = rb = t = 0, discarted
		
		//L = A0 x B2
		ra = rect1ExtentAxis2;
		rb = rect2ExtentAxis1*absR_1_2 + rect2ExtentAxis2*absR_1_1;
		t = Math.abs( ty );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = A1 x B0, ra = rb = t = 0, discarted
	
		//L = A1 x B1, ra = rb = t = 0, discarted
	
		//L = A1 x B2
		ra = rect1ExtentAxis1;
		rb = rect2ExtentAxis1*absR_2_2 + rect2ExtentAxis2*absR_2_1;
		t = Math.abs( tx );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = A2 x B0
		ra = rect1ExtentAxis1*absR_2_1 + rect1ExtentAxis2*absR_1_1;
		rb = rect2ExtentAxis2;
		t = Math.abs( ty*R_1_1 - tx*R_2_1 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = A2 x B1
		ra = rect1ExtentAxis1*absR_2_2 + rect1ExtentAxis2*absR_1_2;
		rb = rect2ExtentAxis1;
		t = Math.abs( ty*R_1_2 - tx*R_2_2 );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;
	
		//L = A2 x B2, ra = rb = t = 0, discarted
	
		/*no separating axis found, the two boxes overlap */
	
		return type;
	}

	/**
	 * Classifies an axis-aligned box and an oriented box.
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
	 * {@link #classifiesOrientedBoxes(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float))}
	 *
	 * @param lowerx is the X coordinate of the lowest point of the box.
	 * @param lowery is the Y coordinate of the lowest point of the box.
	 * @param lowerz is the Z coordinate of the lowest point of the box.
	 * @param upperx is the X coordinate of the uppermost point of the box.
	 * @param uppery is the Y coordinate of the uppermost point of the box.
	 * @param upperz is the Z coordinate of the uppermost point of the box.
	 * @param centerx is the X coordinate of the box center.
	 * @param centery is the Y coordinate of the box center.
	 * @param centerz is the Z coordinate of the box center.
	 * @param axis1x is the X coordinate of the axis1 unit vector.
	 * @param axis1y is the Y coordinate of the axis1 unit vector.
	 * @param axis1z is the Z coordinate of the axis1 unit vector.
	 * @param axis2x is the X coordinate of the axis2 unit vector.
	 * @param axis2y is the Y coordinate of the axis2 unit vector.
	 * @param axis2z is the Z coordinate of the axis2 unit vector.
	 * @param axis3x is the X coordinate of the axis3 unit vector.
	 * @param axis3y is the Y coordinate of the axis3 unit vector.
	 * @param axis3z is the Z coordinate of the axis3 unit vector.
	 * @param Extentaxis1 is the 'axis1' size of the OBB.
	 * @param Extentaxis2 is the 'axis2' size of the OBB.
	 * @param Extentaxis3 is the 'axis3' size of the OBB.
	 * @return the value {@link IntersectionType#INSIDE} if the first box is inside
	 * the second box; {@link IntersectionType#OUTSIDE} if the first box is 
	 * outside the second box; {@link IntersectionType#ENCLOSING} if the 
	 * first box is enclosing the second box;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
	 */
	public static IntersectionType classifiesAlignedBoxOrientedBox(
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
		
		return classifiesOrientedBoxes(
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

	/**
	 * Classifies an minimum bounding rectangle and an oriented rectangle.
	 * <p>
	 * This function is assuming that <var>lx1</var> is lower
	 * or equal to <var>ux1</var>, and <var>ly1</var> is lower
	 * or equal to <var>uy1</var>.
	 * The extents are assumed to be positive or zero.
	 * The lengths of the given arrays are assumed to be <code>2</code>.
	 * <p>
	 * This function uses the "separating axis theorem" which states that 
	 * for any two OBRs (AABB is a special case of OBR)
	 * that do not touch, a separating axis can be found.
	 * <p>
	 * This function uses an optimized algorithm for AABB as first parameter.
	 * The general intersection type between two OBR is given by
	 * {@link #classifiesOrientedRectangles(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)}
	 * 
	 * @param lowerx is the X coordinate of the lowest point of the rectangle.
	 * @param lowery is the Y coordinate of the lowest point of the rectangle.
	 * @param upperx is the X coordinate of the uppermost point of the rectangle.
	 * @param uppery is the Y coordinate of the uppermost point of the rectangle.
	 * @param centerx is the X coordinate of the rectangle center.
	 * @param centery is the Y coordinate of the rectangle center.
	 * @param axis1x is the X coordinate of the axis1 unit vector.
	 * @param axis1y is the Y coordinate of the axis1 unit vector.
	 * @param axis1z is the Z coordinate of the axis1 unit vector.
	 * @param axis2x is the X coordinate of the axis2 unit vector.
	 * @param axis2y is the Y coordinate of the axis2 unit vector.
	 * @param Extentaxis1 is the 'axis1' size of the OBR.
	 * @param Extentaxis2 is the 'axis2' size of the OBR.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
	 */
	public static IntersectionType classifiesAlignedRectangleOrientedRectangle(
			float lowerx,float  lowery, float upperx,float  uppery,
			float centerx,float  centery, float axis1x, float axis1y, float axis2x, float axis2y, float extentAxis1, float extentAxis2) {
	assert(lowerx<=upperx);
	assert(lowery<=uppery);
	assert(extentAxis1>=0);
	assert(extentAxis2>=0);
	
	float mbrCenterx, mbrCentery;
	
	mbrCenterx = (upperx+lowerx)/2.f;
	mbrCentery = (uppery+lowery)/2.f;
		
		return classifiesOrientedRectangles(
    			mbrCenterx, mbrCentery, 1, 0, 0, 1, upperx - mbrCenterx, uppery - mbrCentery,
    			centerx, centery, axis1x, axis1y, axis2x, axis2y, extentAxis1, extentAxis2);
	}

	/**
	 * Replies if the specified sphere intersects the specified capsule.
	 * @param sphereCenterx is the X coordinate of the sphere center.
	 * @param sphereCentery is the Y coordinate of the sphere center.
	 * @param sphereCenterz is the Z coordinate of the sphere center.
	 * @param radius is the radius of the sphere.
	 * @param capsuleAx is the X coordinate of medial line segment start point of the capsule
	 * @param capsuleAy is the Y coordinate of medial line segment start point of the capsule
	 * @param capsuleAz is the Z coordinate of medial line segment start point of the capsule
	 * @param capsuleBx is the X coordinate of medial line segment end point of the capsule
	 * @param capsuleBy is the Y coordinate of medial line segment end point of the capsule
	 * @param capsuleBz is the Z coordinate of medial line segment end point of the capsule
	 * @param capsuleRadius - radius of the capsule
	 * @return the value {@link IntersectionType#INSIDE} if the capsule is inside
	 * the sphere; {@link IntersectionType#OUTSIDE} if the capsule is 
	 * outside the sphere; {@link IntersectionType#ENCLOSING} if the 
	 * capsule is enclosing the sphere;
	 */
	public static IntersectionType classifySphereCapsule(
			float sphereCenterx, float sphereCentery, float sphereCenterz, float sphereRadius,
			float capsuleAx, float capsuleAy, float capsuleAz,
			float capsuleBx, float capsuleBy, float capsuleBz, float capsuleRadius) {
		// Computedistance between sphere center and capsule line segment
		
		float dist2 = GeometryUtil.distanceSquaredPointSegment(sphereCenterx, sphereCentery, sphereCenterz, capsuleAx, capsuleAy, capsuleAz, capsuleBx, capsuleBy, capsuleBz);
		
		// If distance smaller than sum of radii, they collide
		float fullRadius = sphereRadius + capsuleRadius;
		
		if (dist2 < fullRadius) {

			float d1 = GeometryUtil.distanceSquaredPointPoint(capsuleAx, capsuleAy, capsuleAz, sphereCenterx, sphereCenterx, sphereCenterx);
			float d2 = GeometryUtil.distanceSquaredPointPoint(capsuleBx, capsuleBy, capsuleBz, sphereCenterx, sphereCenterx, sphereCenterx);
			
			if((dist2+sphereRadius) <= capsuleRadius) {
				return IntersectionType.ENCLOSING;
			}
			else if (sphereRadius*sphereRadius >= (Math.max(d1,d2)+capsuleRadius)) {
				return IntersectionType.INSIDE;
			}
			return IntersectionType.SPANNING;
		}
		return IntersectionType.OUTSIDE;
	}

	/**
	 * Compute intersection between an OBB and a capsule
	 * @param centerx is the X coordinate of the box center.
	 * @param centery is the Y coordinate of the box center.
	 * @param centerz is the Z coordinate of the box center.
	 * @param axis1x is the X coordinate of the axis1 unit vector.
	 * @param axis1y is the Y coordinate of the axis1 unit vector.
	 * @param axis1z is the Z coordinate of the axis1 unit vector.
	 * @param axis2x is the X coordinate of the axis2 unit vector.
	 * @param axis2y is the Y coordinate of the axis2 unit vector.
	 * @param axis2z is the Z coordinate of the axis2 unit vector.
	 * @param axis3x is the X coordinate of the axis3 unit vector.
	 * @param axis3y is the Y coordinate of the axis3 unit vector.
	 * @param axis3z is the Z coordinate of the axis3 unit vector.
	 * @param Extentaxis1 is the 'axis1' size of the OBB.
	 * @param Extentaxis2 is the 'axis2' size of the OBB.
	 * @param Extentaxis3 is the 'axis3' size of the OBB.
	 * @param capsuleAx is the X coordinate of medial line segment start point of the capsule
	 * @param capsuleAy is the Y coordinate of medial line segment start point of the capsule
	 * @param capsuleAz is the Z coordinate of medial line segment start point of the capsule
	 * @param capsuleBx is the X coordinate of medial line segment end point of the capsule
	 * @param capsuleBy is the Y coordinate of medial line segment end point of the capsule
	 * @param capsuleBz is the Z coordinate of medial line segment end point of the capsule
	 * @param capsuleRadius - radius of the capsule
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return the value {@link IntersectionType#INSIDE} if the capsule is inside
	 * the box; {@link IntersectionType#OUTSIDE} if the capsule is 
	 * outside the box; {@link IntersectionType#ENCLOSING} if the 
	 * capsule is enclosing the box;
	 */
	public static IntersectionType classifyOrientedBoxCapsule(
			float centerx,float  centery,float  centerz,
			float axis1x, float axis1y, float axis1z, float axis2x, float axis2y, float axis2z,	float axis3x, float axis3y, float axis3z,
			float extentAxis1, float extentAxis2, float extentAxis3,
			float capsule1Ax, float capsule1Ay, float capsule1Az, float capsule1Bx, float capsule1By, float capsule1Bz, float capsule1Radius,float epsilon) {

		
		Point3f closestFromA = new Point3f();
		Point3f closestFromB = new Point3f();
		Point3f farestFromA = new Point3f();
		Point3f farestFromB = new Point3f();
		GeometryUtil.closestFarthestPointsOBBPoint(
				centerx, centery, centerz,axis1x, axis1y, axis1z, axis2x, axis2y, axis2z,	axis3x, axis3y, axis3z,extentAxis1, extentAxis2, extentAxis3,
				capsule1Ax, capsule1Ay, capsule1Az,
				closestFromA, farestFromA);
		GeometryUtil.closestFarthestPointsOBBPoint(
				centerx, centery, centerz, axis1x, axis1y, axis1z, axis2x, axis2y, axis2z, axis3x, axis3y, axis3z, extentAxis1, extentAxis2, extentAxis3,
				capsule1Bx, capsule1By, capsule1Bz,
				closestFromB, farestFromB);
		
		float distanceToNearest = GeometryUtil.distanceSegmentSegment(capsule1Ax, capsule1Ay, capsule1Bx, capsule1By, closestFromA.getX(), closestFromA.getY(), closestFromB.getX(), closestFromB.getY(), epsilon);
		
		if (distanceToNearest > capsule1Radius) {
			return IntersectionType.OUTSIDE;
		}
		
		float distanceToFarest = GeometryUtil.distanceSegmentSegment(capsule1Ax, capsule1Ay, capsule1Bx, capsule1By, farestFromA.getX(), farestFromA.getY(), farestFromB.getX(), farestFromB.getY(), epsilon);
		if(distanceToFarest <= capsule1Radius) {
			return IntersectionType.ENCLOSING;
		}
		
		IntersectionType onSphereA = ClassifierUtil.classifiesSolidSphereOrientedBox(
				capsule1Ax, capsule1Ay, capsule1Az, capsule1Radius,
				centerx, centery, centerz, axis1x, axis1y, axis1z, axis2x, axis2y, axis2z, axis3x, axis3y, axis3z, extentAxis1, extentAxis2, extentAxis3, epsilon);
		IntersectionType onSphereB = ClassifierUtil.classifiesSolidSphereOrientedBox(
				capsule1Bx, capsule1By, capsule1Bz, capsule1Radius, 
				centerx, centery, centerz, axis1x, axis1y, axis1z, axis2x, axis2y, axis2z, axis3x, axis3y, axis3z, extentAxis1, extentAxis2, extentAxis3, epsilon);
	
		if(onSphereA.equals(IntersectionType.INSIDE)&&onSphereB.equals(IntersectionType.INSIDE)) {
			return IntersectionType.INSIDE;
	
		}
		else if(onSphereA.equals(IntersectionType.INSIDE) || onSphereB.equals(IntersectionType.INSIDE)) {
			return IntersectionType.SPANNING;					
		}
		else if(onSphereA.equals(IntersectionType.ENCLOSING) || onSphereB.equals(IntersectionType.ENCLOSING)) {
			return IntersectionType.ENCLOSING;					
		}
		
		return IntersectionType.SPANNING;
	}

	/**
	 * Compute intersection between a point and a capsule
	 * @param px is the X coordinate of the point to test.
	 * @param py is the Y coordinate of the point to test.
	 * @param pz is the Z coordinate of the point to test.
	 * @param capsuleAx is the X coordinate of medial line segment start point of the capsule
	 * @param capsuleAy is the Y coordinate of medial line segment start point of the capsule
	 * @param capsuleAz is the Z coordinate of medial line segment start point of the capsule
	 * @param capsuleBx is the X coordinate of medial line segment end point of the capsule
	 * @param capsuleBy is the Y coordinate of medial line segment end point of the capsule
	 * @param capsuleBz is the Z coordinate of medial line segment end point of the capsule
	 * @param capsuleRadius - radius of the capsule
	 * @return the value {@link IntersectionType#INSIDE} if the point is inside
	 * the capsule; {@link IntersectionType#OUTSIDE} if the point is 
	 * outside the capsule; {@link IntersectionType#ENCLOSING} is not defined here;
	 */
	public static IntersectionType classifyPointCapsule(
			float px, float py, float pz,
			float capsule1Ax, float capsule1Ay, float capsule1Az, float capsule1Bx, float capsule1By, float capsule1Bz, float capsule1Radius) {
		
		float distPointToCapsuleSegment = GeometryUtil.distancePointSegment(px,py,pz,capsule1Ax,capsule1Ay,capsule1Az,capsule1Bx,capsule1By,capsule1Bz);
		if (distPointToCapsuleSegment > capsule1Radius) {
			return IntersectionType.OUTSIDE;
		} else if (distPointToCapsuleSegment == capsule1Radius) {
			return IntersectionType.SPANNING;
		}else {
		
			return IntersectionType.INSIDE;
		}
	}
}

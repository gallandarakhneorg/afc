/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.continuous.object3d;

import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/**
 * A facet in space. It is defined by three points.
 * <p>
 * A triangle is transformable. So it has a position given
 * by its first point, an orientation given its normal
 * and no scale factor.
 * <p>
 * Additionnally a triangle may have a pivot point
 * around which rotations will be apply. By default
 * the pivot point is the first point of the triangle.
 * 
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public class Facet3f extends Triangle3f {

	private static final long serialVersionUID = -41831889787562508L;

	/**
	 * Construct a facet 3D.
	 * This constructor does not copy the given points.
	 * The triangle's points will be references to the
	 * given points.
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Facet3f(Point3f p1, Point3f p2, Point3f p3) {
		super(p1, p2, p3);
	}

	/**
	 * Construct a facet 3D.
	 * This constructor does not copy the given points.
	 * The triangle's points will be references to the
	 * given points.
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param copyPoints indicates if the given points may be copied
	 * or referenced by this triangle. If <code>true</code> points
	 * will be copied, <code>false</code> points will be referenced.
	 */
	public Facet3f(Point3f p1, Point3f p2, Point3f p3, boolean copyPoints) {
		super(p1, p2, p3, copyPoints);
	}
	
	/**
	 * Construct a triangle 3D.
	 * 
	 * @param p1x is the x coordinate of the first point.
	 * @param p1y is the y coordinate of the first point.
	 * @param p1z is the z coordinate of the first point.
	 * @param p2x is the x coordinate of the first point.
	 * @param p2y is the y coordinate of the first point.
	 * @param p2z is the z coordinate of the first point.
	 * @param p3x is the x coordinate of the first point.
	 * @param p3y is the y coordinate of the first point.
	 * @param p3z is the z coordinate of the first point.
	 */
	public Facet3f(
			float p1x, float p1y, float p1z,
			float p2x, float p2y, float p2z,
			float p3x, float p3y, float p3z) {
		super(p1x, p1y, p1z, p2x, p2y, p2z, p3x, p3y, p3z);
	}

	/**
	 * Checks if a point is in this triangle.
	 * This method checks only in horizontal plane.
	 * The horizontal plane depends on the current
	 * coordinate system.
	 * 
	 * @param point is the the point
	 * @return <code>true</code> if the point is in the triangle , otherwise <code>false</code>.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 * @see IntersectionUtil#intersectsPointTriangle(float, float, float, float, float, float, float, float)
	 */
	public boolean contains(Point2f point) {
		return contains(point, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Checks if a point is in this triangle.
	 * This method checks only in horizontal plane.
	 * 
	 * @param point is the the point
	 * @param system is the coordinate system to use to project the 2D point in 3D.
	 * @return <code>true</code> if the point is in the triangle , otherwise <code>false</code>.
	 * @see IntersectionUtil#intersectsPointTriangle(float, float, float, float, float, float, float, float)
	 */
	public boolean contains(Point2f point, CoordinateSystem3D system) {
		assert(system!=null);
		int idx = system.getHeightCoordinateIndex();
		assert(idx==1 || idx==2);
		Point3f p1 = getPoint1();
		assert(p1!=null);
		Point3f p2 = getPoint2();
		assert(p2!=null);
		Point3f p3 = getPoint3();
		assert(p3!=null);
		return IntersectionUtil.intersectsPointTriangle(
				point.getX(), point.getY(), 
				p1.getX(), (idx==1) ? p1.getZ() : p1.getY(), 
				p2.getX(), (idx==1) ? p2.getZ() : p2.getY(), 
				p3.getX(), (idx==1) ? p3.getZ() : p3.getY());
	}

	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public Point3f interpolatePoint(float x, float y) {
		return interpolatePoint(x,y,CoordinateSystem3D.getDefaultCoordinateSystem());
	}
	
	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 */
	public Point3f interpolatePoint(float x, float y, CoordinateSystem3D system) {
		assert(system!=null);
		int idx = system.getHeightCoordinateIndex();
		assert(idx==1 || idx==2);
		Point3f p1 = getPoint1();
		assert(p1!=null);
		Vector3f v = getNormal();
		assert(v!=null);
		
		if (idx==1 && v.getY()==0.)
			return new Point3f(x, p1.getY(), y);
		if (idx==2 && v.getZ()==0.)
			return new Point3f(x, y, p1.getZ());
		
		float d = -(v.getX() * p1.getX() + v.getY() * p1.getY() + v.getZ() * p1.getZ());
		
		if (idx==2)
			return new Point3f(x, y, -(v.getX() * x + v.getY() * y + d) / v.getZ());
		
		return new Point3f(x, -(v.getX() * x + v.getZ() * y + d) / v.getY(), y);
	}

	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param point is the the point
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public final Point3f interpolatePoint(Point2f point) {
		return interpolatePoint(point.getX(), point.getY(), CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param point is the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 */
	public Point3f interpolatePoint(Point2f point, CoordinateSystem3D system) {
		return interpolatePoint(point.getX(), point.getY(), system);
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * <p>
	 * Height depends on the current coordinate system.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @return the height of the 2D point when projected on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public float interpolateHeight(float x, float y) {
		return interpolateHeight(x, y, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the height of the 2D point when projected on the facet.
	 */
	public float interpolateHeight(float x, float y, CoordinateSystem3D system) {
		assert(system!=null);
		int idx = system.getHeightCoordinateIndex();
		assert(idx==1 || idx==2);
		Point3f p1 = getPoint1();
		assert(p1!=null);
		Vector3f v = getNormal();
		assert(v!=null);
		
		if (idx==1 && v.getY()==0.)
			return p1.getY();
		if (idx==2 && v.getZ()==0.)
			return p1.getZ();
		
		float d = -(v.getX() * p1.getX() + v.getY() * p1.getY() + v.getZ() * p1.getZ());
		
		if (idx==2)
			return -(v.getX() * x + v.getY() * y + d) / v.getZ();
		
		return -(v.getX() * x + v.getZ() * y + d) / v.getY();
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * <p>
	 * Height depends on the current coordinate system.
	 * 
	 * @param point is the 2D point.
	 * @return the height of the 2D point when projected on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public final float interpolateHeight(Point2f point) {
		return interpolateHeight(point.getX(), point.getY());
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * 
	 * @param point is the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the height of the 2D point when projected on the facet.
	 */
	public float interpolateHeight(Point2f point, CoordinateSystem3D system) {
		return interpolateHeight(point.getX(), point.getY(), system);
	}
	
}

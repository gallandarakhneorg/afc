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
package org.arakhne.afc.math.geometry.d3.continuous;

import java.io.Serializable;

import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;

/** This class represents a 3D plane.
 *
 * @param <PT> is the type of the plane.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Plane3D<PT extends Plane3D<? super PT>> extends Serializable, Cloneable {

	/** Reset this shape to be equivalent to
	 * an just-created instance of this shape type. 
	 */
	void clear();

	/**
	 * Returns the normal to this plane.
	 * 
	 * @return the normal of the plane.
	 */
	FunctionalVector3D getNormal();

	/**
	 * Replies the component a of the plane equation.
	 * 
	 * @return the component a of the plane equation.
	 */
	double getEquationComponentA();

	/**
	 * Replies the component b of the plane equation.
	 * 
	 * @return the component b of the plane equation.
	 */
	double getEquationComponentB();

	/**
	 * Replies the component c of the plane equation.
	 * 
	 * @return the component c of the plane equation.
	 */
	double getEquationComponentC();

	/**
	 * Replies the component d of the plane equation.
	 * 
	 * @return the component d of the plane equation.
	 */
	double getEquationComponentD();

    /**
	 * Negate the normal of the plane.
	 */
	void negate();

	/**
	 * Make the normal of the plane be absolute, ie. all their
	 * components are positive or nul.
	 */
	void absolute();

	/**
	 * Normalizes this plane (i.e. the vector (a,b,c) becomes unit length).
	 * 
	 * @return this plane
	 */
	PT normalize();
	
	/** Set the equation of the plane according to be colinear (if possible)
	 * to the specified plane.
	 * 
	 * @param plane is the plane to copy.
	 */
	void set(Plane3D<?> plane);
	
	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	PT clone();

    /**
     * Replies the distance from the given point to the plane.
     *
     * @param v is the point.
     * @return the distance from the plane to the point.
     * It will be positive if the point is on the side of the
     * plane pointed to by the normal Vec3f, negative otherwise.
     * If the result is 0, the point is on the plane.
     */
	double distanceTo(Point3D v);

	/**
	 * Replies the distance between this plance and the given plane.
	 *
	 * @param p the plane?
	 * @return the distance from the plane to the point.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	double distanceTo(Plane3D<?> p);

	/** Replies the intersection between this plane and the specified one.
	 * 
	 * @param plane is used to compute the intersection.
	 * @return the intersection segment or <code>null</code>
	 */
	AbstractSegment3F getIntersection(Plane3D<?> plane);

	/** Replies the intersection between this plane and the specified line.
	 * 
	 * @param line is used to compute the intersection.
	 * @return the intersection point or <code>null</code>
	 */
	FunctionalPoint3D getIntersection(AbstractSegment3F line);

	/** Replies the projection of the given point on the plane.
	 * 
	 * @param point the point to project on the plane.
	 * @return the projection point never <code>null</code>
	 */
	FunctionalPoint3D getProjection(Point3D point);

	/** Replies the projection of the given point on the plane.
	 * 
	 * @param x x coordinate is the point to project on the plane.
	 * @param y y coordinate is the point to project on the plane.
	 * @param z z coordinate is the point to project on the plane.
	 * @return the projection point never <code>null</code>
	 */
	FunctionalPoint3D getProjection(double x, double y, double z);

	/**
	 * Replies the distance from the given point to the plane.
	 *
	 * @param x is the x-coordinate of the point.
	 * @param y is the y-coordinate of the point.
	 * @param z is the z-coordinate of the point.
	 * @return the distance from the plane to the point.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	double distanceTo(double x, double y, double z);

	/**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	boolean intersects(double x, double y, double z);
	
	/**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param point
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	boolean intersects(Point3D point);
	
	/** Replies if this plane is intersecting the given plane.
	 *
	 * @param otherPlane
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	boolean intersects(Plane3D<?> otherPlane);

	/**
	 * Classifies a plane with respect to the plane.
	 *
	 * @param otherPlane is the plane to classify
	 * @return the classification
	 */
	PlaneClassification classifies(Plane3D<?> otherPlane);

	/**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param point
	 * @return the classification
	 */
	PlaneClassification classifies(Point3D point);

	/**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the classification
	 */
	PlaneClassification classifies(double x, double y, double z);

	/**
	 * Classifies a sphere with respect to the plane.
	 * 
	 * @param sphere
	 * @return the classification
	 */
	PlaneClassification classifies(AbstractSphere3F sphere);
	
	/**
	 * Classifies an aligned box with respect to the plane.
	 * 
	 * @param box
	 * @return the classification
	 */
    PlaneClassification classifies(AbstractBoxedShape3F<?> box);

    /**
	 * Replies if the given sphere is intersecting the plane.
	 * 
	 * @param sphere
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    boolean intersects(AbstractSphere3F sphere);
    
    /**
	 * Replies if the given axis-aligned box is intersecting the plane.
	 * 
	 * @param box
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    boolean intersects(AbstractBoxedShape3F<?> box);

    /**
	 * Replies if the given oriented box is intersecting the plane.
	 * 
	 * @param box
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    boolean intersects(AbstractOrientedBox3F box);

    /**
	 * Replies if the given sphere is intersecting the segment.
	 * If the intersection point between the point and the line that is
	 * colinear to the given segment lies on the segment, then an intersection
	 * exists. Otherwise there is no intersection.
	 * 
	 * @param segment
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    boolean intersects(AbstractSegment3F segment);

    /** Set point that lies on the plane and is used a pivot point.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
    void setPivot(double x, double y, double z);
  
    /** Set point that lies on the plane and is used a pivot point.
	 * 
	 * @param point
	 */
	void setPivot(Point3D point);

}
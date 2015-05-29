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
package org.arakhne.afc.math.geometry3d.continuous;

import java.io.Serializable;

import org.arakhne.afc.math.geometry.continuous.transform.Transformable3D;


/** This interface represents a 3D plane.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Plane extends Cloneable, Serializable, Transformable3D, PlaneClassifier {

	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	public Plane clone();
	
	/**
	 * Returns the normal to this plane.
	 * 
	 * @return the normal of the plane.
	 */
	public Vector3f getNormal();

	/**
	 * Replies the component a of the plane equation.
	 * 
	 * @return the component a of the plane equation.
	 */
	public float getEquationComponentA();

	/**
	 * Replies the component b of the plane equation.
	 * 
	 * @return the component b of the plane equation.
	 */
	public float getEquationComponentB();

	/**
	 * Replies the component c of the plane equation.
	 * 
	 * @return the component c of the plane equation.
	 */
	public float getEquationComponentC();

	/**
	 * Replies the component d of the plane equation.
	 * 
	 * @return the component d of the plane equation.
	 */
	public float getEquationComponentD();

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
    public float distanceTo(float x, float y, float z);

    /**
     * Replies the distance from the given point to the plane.
     *
     * @param v is the point.
     * @return the distance from the plane to the point.
     * It will be positive if the point is on the side of the
     * plane pointed to by the normal Vec3f, negative otherwise.
     * If the result is 0, the point is on the plane.
     */
    public float distanceTo(Tuple3f v);

	/**
	 * Negate the normal of the plane.
	 */
	public void negate();

	/**
	 * Make the normal of the plane be absolute, ie. all their
	 * components are positive or nul.
	 */
	public void absolute();

	/**
	 * Normalizes this plane (i.e. the vector (a,b,c) becomes unit length).
	 * 
	 * @return this plane
	 */
	public Plane normalize();
	
	/** Set the equation of the plane according to be colinear (if possible)
	 * to the specified plane.
	 * 
	 * @param plane is the plane to copy.
	 */
	public void set(Plane plane);
	
	/** Replies the intersection between this plane and the specified one.
	 * 
	 * @param plane is used to compute the intersection.
	 * @return the intersection segment or <code>null</code>
	 */
	public Line3f getIntersection(Plane plane);

	/** Replies the intersection between this plane and the specified line.
	 * 
	 * @param line is used to compute the intersection.
	 * @return the intersection point or <code>null</code>
	 */
	public Point3f getIntersection(Line3f line);
	
	
	//TODO public Point3f getProjection(Point3D point) 

}
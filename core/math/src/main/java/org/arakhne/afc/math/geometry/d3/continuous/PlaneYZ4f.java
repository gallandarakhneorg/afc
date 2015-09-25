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

import org.arakhne.afc.math.geometry.d3.Tuple3D;

/** This class represents a 3D plane which is colinear to the Y and Z axis.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PlaneYZ4f extends AbstractOrthoPlane3D<PlaneYZ4f> {
	
	private static final long serialVersionUID = -1156212885754601129L;

	/** Coordinate of the plane.
	 */
	protected double x;

	/**
	 * @param x1 is the coordinate of the plane.
	 */
	public PlaneYZ4f(double x1) {
		this.x = x1;
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneYZ4f(Tuple3D<?> p) {
		this.x = p.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.x = plane.getEquationComponentA();
		normalize();
	}

	/** {@inheritDoc}
	 */
	@Override
    public void clear() {
    	this.x = 0;
    }

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f getNormal() {
		return new Vector3f(this.isPositive ? 1 : -1, 0, 0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getEquationComponentA() {
		return this.isPositive ? 1 : -1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getEquationComponentB() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getEquationComponentC() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double getEquationComponentD() {
		return this.isPositive ? -this.x : this.x;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceTo(double px, double py, double pz) {
    	double d = px - this.x;
    	return this.isPositive ? d : -d;
    }

	@Override
	public Point3f getProjection(double x1, double y, double z) {
		return new Point3f(this.x, y, z);
	}

	@Override
	public void setPivot(double x1, double y, double z) {
		this.x = x1;
	}
	
	/** Set the x coordinate of the plane.
	 *
	 * @param x1
	 */
	public void setX(double x1) {
		this.x = x1;
	}

	/** Translate the pivot point of the plane.
	 *
	 * @param dx the translation to apply.
	 */
	public void translate(double dx) {
		this.x += dx;
	}

}
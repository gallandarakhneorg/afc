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

/** This class represents a 3D plane which is colinear to the X and Z axis.
 *
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PlaneXZ4f extends AbstractOrthoPlane3D<PlaneXZ4f> {
	
	private static final long serialVersionUID = 8816401203570327193L;

	/** Is the coordinate of the plane.
	 */
	protected double y;

	/**
	 * @param y is the coordinate of the plane
	 */
	public PlaneXZ4f(double y) {
		this.y = y;
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXZ4f(Tuple3D<?> p) {
		this.y = p.getY();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.y = plane.getEquationComponentB();
		normalize();
	}

	/** {@inheritDoc}
	 */
	@Override
    public void clear() {
    	this.y = 0;
    }

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f getNormal() {
		return new Vector3f(0,this.isPositive ? 1 : -1, 0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getEquationComponentA() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getEquationComponentB() {
		return this.isPositive ? 1 : -1;
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
		return this.isPositive ? -this.y : this.y;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceTo(double px, double py, double pz) {
    	double d = py - this.y;
    	return this.isPositive ? d : -d;
    }

	@Override
	public Point3f getProjection(double x, double y, double z) {
		return new Point3f(x, this.y, z);
	}

	@Override
	public void setPivot(double x, double y, double z) {
		this.y = y;
	}

	/** Set the y coordinate of the plane.
	 *
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/** Translate the pivot point of the plane.
	 *
	 * @param dy the translation to apply.
	 */
	public void translate(double dy) {
		this.y += dy;
	}

}
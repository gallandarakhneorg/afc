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

/** This class represents a 3D plane which is colinear to the X and Y axis.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PlaneXY4f extends AbstractOrthoPlane3D<PlaneXY4f> {

	private static final long serialVersionUID = -900201883321568976L;

	/**
	 * Coordinate of the plane.
	 */
	protected double z;

	/**
	 * @param z1 is the coordinate of the plane.
	 */
	public PlaneXY4f(double z1) {
		this.z = z1;
	}

	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXY4f(Tuple3D<?> p) {
		this.z = p.getZ();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.z = plane.getEquationComponentC();
		normalize();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.z = 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f getNormal() {
		return new Vector3f(0,0,this.isPositive ? 1 : -1);
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
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getEquationComponentC() {
		return this.isPositive ? 1 : -1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double getEquationComponentD() {
		return this.isPositive ? -this.z : this.z;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceTo(double px, double py, double pz) {
		double d = pz - this.z;
		return this.isPositive ? d : -d;
	}

	@Override
	public Point3f getProjection(double x, double y, double z1) {
		return new Point3f(x, y, this.z);
	}

	@Override
	public void setPivot(double x, double y, double z1) {
		this.z = z1;
	}

	/** Set the z coordinate of the plane.
	 *
	 * @param z1
	 */
	public void setZ(double z1) {
		this.z = z1;
	}

	/** Translate the pivot point of the plane.
	 *
	 * @param dz the translation to apply.
	 */
	public void translate(double dz) {
		this.z += dz;
	}

}
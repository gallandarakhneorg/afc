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

import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** This class represents a 3D plane which is colinear to the X and Y axis.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPlaneXY4F extends AbstractOrthoPlane3D<AbstractPlaneXY4F> {

	private static final long serialVersionUID = -900201883321568976L;

	

	/** {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.setZ( 0);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Vector3f getNormal() {
		return new Vector3f(0,0,this.isPositive ? 1 : -1);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentA() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentB() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentC() {
		return this.isPositive ? 1 : -1;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public final double getEquationComponentD() {
		return this.isPositive ? -this.getZ() : this.getZ();
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceTo(double px, double py, double pz) {
		double d = pz - this.getZ();
		return this.isPositive ? d : -d;
	}


	/** Translate the pivot point of the plane.
	 *
	 * @param dz the translation to apply.
	 */
	public void translate(double dz) {
		this.setZ(this.getZ() + dz);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	abstract public void set(Plane3D<?> plane);

	@Pure
	@Override
	abstract public FunctionalPoint3D getProjection(double x, double y, double z1);

	@Override
	abstract public void setPivot(double x, double y, double z1);

	/** Set the z coordinate of the plane.
	 *
	 * @param z1
	 */
	abstract public void setZ(double z1);

	/** Replies the z coordinate of the plane.
	 *
	 */
	@Pure
	abstract public double getZ();

}
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

/** This class represents a 3D plane which is colinear to the Y and Z axis.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPlaneYZ4F extends AbstractOrthoPlane3D<AbstractPlaneYZ4F> {
	
	private static final long serialVersionUID = -1156212885754601129L;

	

	/** {@inheritDoc}
	 */
	@Override
    public void clear() {
    	this.setX(0f);
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
		return this.isPositive ? -this.getX() : this.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceTo(double px, double py, double pz) {
    	double d = px - this.getX();
    	return this.isPositive ? d : -d;
    }

	/** Translate the pivot point of the plane.
	 *
	 * @param dx the translation to apply.
	 */
	public void translate(double dx) {
		this.setX(this.getX()+ dx) ;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	abstract public void set(Plane3D<?> plane);
	
	@Override
	abstract public FunctionalPoint3D getProjection(double x1, double y, double z);

	@Override
	abstract public void setPivot(double x1, double y, double z);
	
	/** Set the x coordinate of the plane.
	 *
	 * @param x1
	 */
	abstract public void setX(double x1);
	
	/** Replies the x coordinate of the plane.
	 *
	 */
	abstract public double getX();

}
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

/** This class represents a 3D plane which is colinear to the X and Z axis.
 *
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPlaneXZ4F extends AbstractOrthoPlane3D<AbstractPlaneXZ4F> {
	
	private static final long serialVersionUID = 8816401203570327193L;


	/** {@inheritDoc}
	 */
	@Override
    public void clear() {
    	this.setY(0f);
    }

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Vector3d getNormal() {
		return new Vector3d(0,this.isPositive ? 1 : -1, 0);
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
		return this.isPositive ? 1 : -1;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentC() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public final double getEquationComponentD() {
		return this.isPositive ? -this.getY() : this.getY();
	}

	

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceTo(double px, double py, double pz) {
    	double d = py - this.getY();
    	return this.isPositive ? d : -d;
    }

	
	/** Translate the pivot point of the plane.
	 *
	 * @param dy the translation to apply.
	 */
	public void translate(double dy) {
		this.setY(this.getY()+ dy) ;
	}
	
	
	/** {@inheritDoc}
	 */
	@Override
	abstract public void set(Plane3D<?> plane);

	@Pure
	@Override
	abstract public FunctionalPoint3D getProjection(double x, double y1, double z);

	@Override
	abstract public void setPivot(double x, double y1, double z);

	/** Set the y coordinate of the plane.
	 *
	 * @param y1
	 */
	abstract public void setY(double y1);
	
	/** Replies the y coordinate of the plane.
	 *
	 */
	@Pure
	abstract public double getY();
}
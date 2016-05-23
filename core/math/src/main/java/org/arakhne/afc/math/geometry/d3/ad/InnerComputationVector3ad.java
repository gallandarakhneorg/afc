/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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

package org.arakhne.afc.math.geometry.d3.ad;

import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** A vector that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationVector3ad implements Vector3D<InnerComputationVector3ad, InnerComputationPoint3ad> {

	private static final long serialVersionUID = 8578192819251519051L;
	
	private double x;
	private double y;
	private double z;

	/**
	 */
	public InnerComputationVector3ad() {
		//
	}

	/**
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public InnerComputationVector3ad(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public InnerComputationGeomFactory getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

	@Override
	public String toString() {
		return "[" + this.x + "; " + this.y + "; " + this.z + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Override
	public InnerComputationVector3ad clone() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) Math.round(this.x);
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}
	
	@Override
	public double getY() {
		return this.y;
	}
	
	@Override
	public int iy() {
		return (int) Math.round(this.y);
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return (int) Math.round(this.z);
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public UnmodifiableVector3D<InnerComputationVector3ad, InnerComputationPoint3ad> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public InnerComputationVector3ad toUnitVector() {
		double length = getLength();
		if (length == 0) {
			return new InnerComputationVector3ad();
		}
		return new InnerComputationVector3ad(getX() / length, getY() / length, getZ() / length);
	}
	
//	@Override
//	public InnerComputationVector3ad toOrthogonalVector() {
//		return new InnerComputationVector3ad(-getY(), getX());
//	}

}

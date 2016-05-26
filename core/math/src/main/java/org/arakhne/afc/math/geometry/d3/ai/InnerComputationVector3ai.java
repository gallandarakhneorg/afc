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

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
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
public class InnerComputationVector3ai implements Vector3D<InnerComputationVector3ai, InnerComputationPoint3ai> {

	private static final long serialVersionUID = -9075498295363704480L;
	
	private int x;
	private int y;
	private int z;

	/**
	 */
	public InnerComputationVector3ai() {
		//
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public InnerComputationVector3ai(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public InnerComputationVector3ai clone() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = (int) Math.round(x);
	}
	
	@Override
	public double getY() {
		return this.y;
	}
	
	@Override
	public int iy() {
		return this.y;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void setY(double y) {
		this.y = (int) Math.round(y);
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return this.z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = (int) Math.round(z);
	}

	@Override
	public GeomFactory3D<InnerComputationVector3ai, InnerComputationPoint3ai> getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

	@Override
	public InnerComputationVector3ai toUnitVector() {
		double length = getLength();
		if (length == 0) {
			return new InnerComputationVector3ai();
		}
		return new InnerComputationVector3ai(
				(int) Math.round(getX() / length),
				(int) Math.round(getY() / length),
				(int) Math.round(getZ() / length));
	}

	@Override
	public UnmodifiableVector3D<InnerComputationVector3ai, InnerComputationPoint3ai> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

}

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

package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.geometry.d2.afp.InnerComputationVector2afp;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;

/** A point that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationPoint3afp implements Point3D<InnerComputationPoint3afp, InnerComputationVector3afp> {

	private static final long serialVersionUID = 8578192819251519051L;
	
	private double x;
	private double y;
	private double z;

	/**
	 */
	public InnerComputationPoint3afp() {
		//
	}

	/**
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public InnerComputationPoint3afp(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public InnerComputationGeomFactory3afp getGeomFactory() {
		return InnerComputationGeomFactory3afp.SINGLETON;
	}

	@Override
	public String toString() {
		return "[" + this.x + "; " + this.y + "; " + this.z + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Override
	public InnerComputationPoint3afp clone() {
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
	public UnmodifiablePoint3D<InnerComputationPoint3afp, InnerComputationVector3afp> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}
	
}

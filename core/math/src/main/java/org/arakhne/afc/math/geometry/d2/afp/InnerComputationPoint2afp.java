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

package org.arakhne.afc.math.geometry.d2.afp;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;

/** A point that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationPoint2afp implements Point2D<InnerComputationPoint2afp, InnerComputationVector2afp> {

	private static final long serialVersionUID = 8578192819251519051L;
	
	private double x;
	private double y;

	/**
	 */
	public InnerComputationPoint2afp() {
		//
	}

	/**
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public InnerComputationPoint2afp(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public InnerComputationGeomFactory getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

	@Override
	public String toString() {
		return "[" + this.x + "; " + this.y + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	public InnerComputationPoint2afp clone() {
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
	public UnmodifiablePoint2D<InnerComputationPoint2afp, InnerComputationVector2afp> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}
	
}

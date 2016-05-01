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

package org.arakhne.afc.math.geometry.d2.ai;

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** A vector that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationVector2ai implements Vector2D<InnerComputationVector2ai, InnerComputationPoint2ai> {

	private static final long serialVersionUID = -9075498295363704480L;
	
	private int x;
	private int y;

	/**
	 */
	public InnerComputationVector2ai() {
		//
	}

	/**
	 * @param x
	 * @param y
	 */
	public InnerComputationVector2ai(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public InnerComputationVector2ai clone() {
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
	public GeomFactory<InnerComputationVector2ai, InnerComputationPoint2ai> getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

	@Override
	public InnerComputationVector2ai toOrthogonalVector() {
		return null;
	}

	@Override
	public InnerComputationVector2ai toUnitVector() {
		double length = getLength();
		if (length == 0) {
			return new InnerComputationVector2ai();
		}
		return new InnerComputationVector2ai(
				(int) Math.round(getX() / length),
				(int) Math.round(getY() / length));
	}

	@Override
	public UnmodifiableVector2D<InnerComputationVector2ai, InnerComputationPoint2ai> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

}

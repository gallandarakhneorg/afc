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


/** This class represents a 3D plane which is colinear to the axis.
 *
 * @param <PT> is the type of the plane.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractOrthoPlane3D<PT extends AbstractOrthoPlane3D<? super PT>> extends AbstractPlane3D<PT> {
	
	private static final long serialVersionUID = -4790630745987743450L;

	/** Indicates if this plane is oriented to the positve side.
	 * If <code>true</code>, the normal of the plane is directed
	 * to the positive infinity.
	 */ 
	protected boolean isPositive = true;
	
	/** {@inheritDoc}
	 */
	@Override
	public void negate() {
		this.isPositive = !this.isPositive;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void absolute() {
		this.isPositive = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PT normalize() {
		return (PT) this;
	}

}
/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.continous.object3d;

import org.arakhne.afc.math.generic.Tuple3D;

/** This class implements a Point3f that cannot be modified by
 * the setters. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d3.continuous.Point3f#toUnmodifiable()}
 */
@Deprecated
public class UnmodifiablePoint3f extends Point3f {

	private static final long serialVersionUID = -2156483626057722906L;

	/**
	 */
	public UnmodifiablePoint3f() {
		super();
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public UnmodifiablePoint3f(double x, double y, double z) {
		super(x, y, z);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UnmodifiablePoint3f clone() {
		return (UnmodifiablePoint3f)super.clone();
	}

	@Override
	public void set(double x, double y, double z) {
		//
	}

	@Override
	public void set(double[] t) {
		//
	}
	
	@Override
	public void set(int x, int y, int z) {
		//
	}
	
	@Override
	public void set(int[] t) {
		//
	}
	
	@Override
	public void set(Tuple3D<?> t1) {
		//
	}
	
	@Override
	public void setX(double x) {
		//
	}
	
	@Override
	public void setX(int x) {
		//
	}
	
	@Override
	public void setY(double y) {
		//
	}
	
	@Override
	public void setY(int y) {
		//
	}
	
	@Override
	public void setZ(double z) {
		//
	}

	@Override
	public void setZ(int z) {
		//
	}

}

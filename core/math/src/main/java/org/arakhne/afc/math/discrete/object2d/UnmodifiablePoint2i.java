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
package org.arakhne.afc.math.discrete.object2d;

import org.arakhne.afc.math.generic.Tuple2D;

/** This class implements a Point2i that cannot be modified by
 * the setters. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.discrete.Point2i#toUnmodifiable()}
 */
@Deprecated
public class UnmodifiablePoint2i extends Point2i {

	private static final long serialVersionUID = -6561225929333955341L;

	/**
	 */
	public UnmodifiablePoint2i() {
		super();
	}

	/**
	 * @param x
	 * @param y
	 */
	public UnmodifiablePoint2i(float x, float y) {
		super(x, y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UnmodifiablePoint2i clone() {
		return (UnmodifiablePoint2i)super.clone();
	}

	@Override
	public void set(float x, float y) {
		//
	}

	@Override
	public void set(float[] t) {
		//
	}
	
	@Override
	public void set(int x, int y) {
		//
	}
	
	@Override
	public void set(int[] t) {
		//
	}
	
	@Override
	public void set(Tuple2D<?> t1) {
		//
	}
	
	@Override
	public void setX(float x) {
		//
	}
	
	@Override
	public void setX(int x) {
		//
	}
	
	@Override
	public void setY(float y) {
		//
	}
	
	@Override
	public void setY(int y) {
		//
	}
	
}

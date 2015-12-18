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
package org.arakhne.afc.math.geometry.d2.discrete;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;

/** 2D shape with integer  points.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractShape2i<T extends Shape2i> implements Shape2i {

	private static final long serialVersionUID = -3663448743772835647L;

	/**
	 */
	public AbstractShape2i() {
		//
	}

	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T clone()  {
		try {
			return (T)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Shape2i createTransformedShape(Transform2D transform) {
		return new Path2i(getPathIteratorDiscrete(transform));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final boolean contains(Point2D p) {
		return contains(p.ix(), p.iy());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double distance(Point2D p) {
		 return Math.sqrt(distanceSquared(p));
	}

	/** {@inheritDoc}
	 */
	@Override
	public PathIterator2i getPathIteratorDiscrete() {
		return getPathIteratorDiscrete(null);
	}
	
}
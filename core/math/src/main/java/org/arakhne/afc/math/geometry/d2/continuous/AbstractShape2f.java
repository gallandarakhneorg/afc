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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.Point2D;



/** Abstract implementation of shapes.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractShape2f<T extends Shape2f> implements Shape2f {

	private static final long serialVersionUID = -2724377801599470453L;

	/**
	 */
	public AbstractShape2f() {
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
	public Shape2f createTransformedShape(Transform2D transform) {
		return new Path2f(getPathIterator(transform));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final PathIterator2f getPathIterator() {
		return getPathIterator(null);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double distance(Point2D p) {
		return Math.sqrt(distanceSquared(p));
	}

	/** {@inheritDoc}
	 */
	@Override
	public final boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	
	/** {@inheritDoc}
	 */
    @Override
    public abstract boolean equals(Object obj);
    
    /** Compute the bit representation of the floating-point value.
     * 
     * @param d
     * @return the bit representation.
     */
    protected static long doubleToLongBits(double d) {
		// Check for +0 or -0
		if (d == 0) {
			return 0;
		}
		return Double.doubleToLongBits(d);
	}
    
	/** {@inheritDoc}
	 */
    @Override
    public abstract int hashCode();
    
}
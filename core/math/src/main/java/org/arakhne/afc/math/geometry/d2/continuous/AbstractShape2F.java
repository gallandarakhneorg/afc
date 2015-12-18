/* 

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

import java.util.Iterator;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.discrete.PathIterator2i;
import org.eclipse.xtext.xbase.lib.Pure;



/** Abstract implementation of shapes.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractShape2F<T extends Shape2F> implements Shape2F {

	private static final long serialVersionUID = -2724377801599470453L;

	/**
	 */
	public AbstractShape2F() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Pure
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
	@Pure
	@Override
	public Shape2F createTransformedShape(Transform2D transform) {
		return new Path2f(getPathIterator(transform));
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public PathIterator2f getPathIterator() {
		return getPathIterator(null);
	}
	
	@Pure
	@Override
	public PathIterator2d getPathIteratorProperty() {
		return getPathIteratorProperty(null);
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distance(Point2D p) {
		return Math.sqrt(distanceSquared(p));
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public final boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	
	/** {@inheritDoc}
	 */
	@Pure
    @Override
    public abstract boolean equals(Object obj);
    
    /** Compute the bit representation of the floating-point value.
     * 
     * @param d
     * @return the bit representation.
     */
	@Pure
    protected static long doubleToLongBits(double d) {
		// Check for +0 or -0
		if (d == 0) {
			return 0;
		}
		return Double.doubleToLongBits(d);
	}
    
	/** {@inheritDoc}
	 */
	@Pure
    @Override
    public abstract int hashCode();
    
}
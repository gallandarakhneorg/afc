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
package org.arakhne.afc.math.continous.object2d;

import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.geometry.d2.fp.AbstractShape2fp;
import org.arakhne.afc.math.matrix.Transform2D;



/** Abstract implementation of shapes.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link AbstractShape2fp}
 */
@Deprecated
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
	public float distance(Point2D p) {
		return (float)Math.sqrt(distanceSquared(p));
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
    protected static int floatToIntBits(float d) {
		// Check for +0 or -0
		if (d == 0f) {
			return 0;
		}
		return Float.floatToIntBits(d);
	}
    
	/** {@inheritDoc}
	 */
    @Override
    public abstract int hashCode();
    
}
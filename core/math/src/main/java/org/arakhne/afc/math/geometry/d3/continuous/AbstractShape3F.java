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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;



/** Abstract implementation of shapes.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractShape3F<T extends Shape3F> implements Shape3F {

	private static final long serialVersionUID = -3183131207922820187L;

	/**
	 */
	public AbstractShape3F() {
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
	public double distance(Point3D p) {
		return Math.sqrt(distanceSquared(p));
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public final boolean contains(Point3D p) {
		return contains(p.getX(), p.getY(), p.getZ());
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
    
	@Override
	public final void translate(Vector3D translation) {
		translate(translation.getX(), translation.getY(), translation.getZ());
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public final PathIterator3f getPathIterator() {
		return getPathIterator(null);
	}
	
	@Pure
	@Override
	public PathIterator3d getPathIteratorProperty() {
		return getPathIteratorProperty(null);
	}
	

}
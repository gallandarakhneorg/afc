/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.continous.object2d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.geometry.d2.d.AbstractShape2d;
import org.arakhne.afc.math.matrix.Transform2D;



/** Abstract implementation of shapes.
 *
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link AbstractShape2d}
 */
@Deprecated
@SuppressWarnings("all")
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
	
	@Override
	public float distance(Point2D p) {
		return (float)Math.sqrt(distanceSquared(p));
	}

	@Override
	public final boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	
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
    
    @Override
    @Pure
    public abstract int hashCode();
    
}

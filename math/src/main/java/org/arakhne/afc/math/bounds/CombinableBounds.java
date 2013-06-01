/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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

package org.arakhne.afc.math.bounds;

import java.util.Collection;

import org.arakhne.afc.math.euclide.EuclidianPoint;


/** This interface representes the bounds of an area
 * which could be combine to produce
 * larger or smaller bounds.
 *
 * @param <CB> is the implementation type of this bounding box.
 * @param <P> is the type of the points when replied by a function.
 * @param <PP> is the type of the points when passed as parameter.
 * @param <V> is the type of the vectors when replied by a function.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface CombinableBounds<CB extends Bounds<P,PP,V>, P extends EuclidianPoint, PP, V> extends Bounds<P,PP,V> {

	/**
	 * Reset the content of this bounds
	 */
	public void reset();

	/**
	 * Add the point into the bounds.
	 * 
	 * @param point
	 */
	public abstract void combine(PP point);

	/** Set this bounds with the point.
	 *
	 * @param point is the point used to update this bounding object.
	 */
	public void set(PP point);

	/**
	 * Add the bounds into the bounds.
	 * 
	 * @param bound are the bounds to combine to this object.
	 */
	public void combine(CB bound);

	/**
	 * Add the bounds into the bounds.
	 * 
	 * @param bounds are the bounds to combine to this object.
	 */
	public void combine(Collection<? extends CB> bounds);
	
	/** Set this bounds from another bounds.
	 * 
	 * @param bounds is the bounds to copy
	 */
	public void set(CB bounds);

}
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

package org.arakhne.afc.math.geometry;

import java.util.Collection;

/** This interface representes the bounds of an area
 * with could be composed of other smaller bounds.
 *
 * @param <CB> is the implementation type of this bounding box.
 * @param <CT> is the type of sub bounds
 * @param <P> is the type of the points when replied by a function.
 * @param <PP> is the type of the points when passed as parameter.
 * @param <V> is the type of the vectors when replied by a function.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ComposedBounds<CB extends ComposedBounds<CB,CT,P,V>, CT extends Bounds<P,V>, P,V> extends Bounds<P,V> {

	/**
	 * Add the bounds into the bounds.
	 *
	 * @param bounds are the bounds to add to this object.
	 * @return <code>true</code> if successfully added, otherwise <code>false</code>
	 */
	public boolean add(CT bounds);

	/**
	 * Add the bounds into the bounds.
	 *
	 * @param bounds are the bounds to add to this object.
	 * @return <code>true</code> if the composed bounds changed,
	 * otherwise <code>false</code>
	 */
	public boolean addAll(Collection<? extends CT> bounds);

	/**
	 * Remove the bounds into the bounds.
	 *
	 * @param bounds are the bounds to remove to this object.
	 */
	public void remove(CT bounds);

	/**
	 * Remove the bounds into the bounds.
	 *
	 * @param bounds are the bounds to remove to this object.
	 */
	public void remove(Collection<?> bounds);

	/**
	 * Replies the sub bounds that compose this bounding object.
	 *
	 * @return the list of sub-bounds.
	 */
	public Iterable<CT> getSubBounds();

}
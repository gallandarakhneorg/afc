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

import org.arakhne.afc.math.euclide.EuclidianPoint;
import org.arakhne.afc.sizediterator.SizedIterator;

/** This interface representes an oriented bounds, ie
 * a bounds which could have not its local axis parallel
 * to the global axis.
 *
 * @param <P> is the type of the points when replied by a function.
 * @param <PP> is the type of the points when passed as parameter.
 * @param <V> is the type of the vectors when replied by a function.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedBounds<P extends EuclidianPoint,PP,V> extends Bounds<P,PP,V> {
	
	/**
	 * Replies the vectors from the bounds center to the vertices of the bounds.
	 * 
	 * @return the various vertices that composes this bounds
	 */
	public SizedIterator<V> getLocalOrientedBoundVertices();
	
	/**
	 * Replies the vertices of the bounds in the global coordinate
	 * system.
	 * 
	 * @return the various vertices that composes this bounds
	 */
	public SizedIterator<P> getGlobalOrientedBoundVertices();

	/**
	 * Replies the local axis of the oriented bounds.
	 * <p>
	 * Each replied vector corresponds to the direction of the 
	 * local axis expressed in the global frame.
	 * 
	 * @return the local axis of the oriented bounds
	 */
	public V[] getOrientedBoundAxis();

	/**
	 * Replies the extents of the oriented bounds.
	 * <p>
	 * The extents are positive or null.
	 * 
	 * @return the extents of the oriented bounds
	 */
	public V getOrientedBoundExtentVector();

	/**
	 * Replies the extents of the oriented bounds.
	 * <p>
	 * The extents are positive or null.
	 * 
	 * @return the extents of the oriented bounds
	 */
	public float[] getOrientedBoundExtents();

	/**
	 * Replies the global position (point) of the vertex at 
	 * the given index of the bounds.
	 * <p>
	 * Range for vertex index depends on the dimension and
	 * type of the bounds implementation class.
	 * 
	 * @param index is the index of the vertex to reply
	 * @return the position of the vertex at the given index.
	 * @throws IndexOutOfBoundsException if the given index
	 * is out of range.
	 */
	public P getGlobalVertexAt(int index);

	/**
	 * Replies the local position (vector) of the vertex at 
	 * the given index of the bounds.
	 * <p>
	 * Range for vertex index depends on the dimension and
	 * type of the bounds implementation class.
	 * 
	 * @param index is the index of the vertex to reply
	 * @return the position of the vertex at the given index.
	 * @throws IndexOutOfBoundsException if the given index
	 * is out of range.
	 */
	public V getLocalVertexAt(int index);

	/**
	 * Replies the count of vertex in the bounds and
	 * which may be directly accessed.
	 * <p>
	 * Let m the value replied by this function.
	 * The valid range for the parameters
	 * of {@link #getLocalVertexAt(int)} and
	 * {@link #getGlobalVertexAt(int)} is
	 * <code>[0;m)</code>.
	 * 
	 * @return the count of vertex in the bounds
	 * @see #getLocalVertexAt(int)
	 * @see #getGlobalVertexAt(int)
	 */
	public int getVertexCount();

}
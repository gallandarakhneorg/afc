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

import java.io.Serializable;


/** This interface represents the bounds of an area
 * in a space.
 *
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Bounds<P, V> extends Serializable, Cloneable {

	/** Replies the axis-aligned lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public P getLower();

	/** Replies the axis-aligned lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public P getUpper();

	/** Replies the axis-aligned lower and upper points of this bounds.
	 * <p>
	 * Parameters will be set with lower and upper coordinates respectively.
	 * 
	 * @param lower is the point which will be set with lower coordinates.
	 * @param upper is the point which will be set with upper coordinates.
	 */
	public void getLowerUpper(P lower, P upper);


	/** Replies the axis-aligned center point of this bounds.
	 * 
	 * @return the coordinate of the center point.
	 */
	public P getCenter();

	/** Replies the position (not necessary the center but
	 * commonly it is the case) of this bounds.
	 * 
	 * @return the position of the bounds.
	 */
	public P getPosition();
	
	/** Replies a clone of this bounds.
	 *
	 * @return the clone
	 */
	public Bounds<P,V> clone();

	/**
	 * Replies if this bounding box was not set or empty.
	 * <p>
	 * A box is empty if one of its sides has a size 0.
	 * 
	 * @return <code>true</code> if this box is empty,
	 * otherwise <code>false</code>
	 */
	public boolean isEmpty();

	/** Replies the distance between the given point and the
	 * nearest point of this bounds.
	 * <p>
	 * <code>b.distanceSquared(P) == b.distance(P) * b.distance(P)</code>
	 *
	 * @param reference
	 * @return the smallest distance from the point to the bounds.
	 * @see #distanceSquared(Object)
	 */
	public float distance(P reference);

	/** Replies the squared distance between the given point and the
	 * nearest point of this bounds.
	 * <p>
	 * <code>b.distanceSquared(P) == b.distance(P) * b.distance(P)</code>
	 *
	 * @param reference
	 * @return the squared value of the smallest distance from the point to the bounds.
	 * @see #distance(Object)
	 */
	public float distanceSquared(P reference);

	/** Replies the distance between the given point and the
	 * farest point of this bounds.
	 * <p>
	 * <code>b.distanceMaxSquared(P) == b.distanceMax(P) * b.distanceMax(P)</code>
	 *
	 * @param reference
	 * @return the distance from the point to the bounds.
	 * @see #distanceMaxSquared(Object)
	 */
	public float distanceMax(P reference);

	/** Replies the squared distance between the given point and the
	 * farest point of this bounds.
	 * <p>
	 * <code>b.distanceMaxSquared(P) == b.distanceMax(P) * b.distanceMax(P)</code>
	 *
	 * @param reference
	 * @return the squared distance from the point to the bounds.
	 * @see #distanceMax(Object)
	 */
	public float distanceMaxSquared(P reference);

	/**
	 * Replies the axis-aligned size this bounding object.
	 * 
	 * @return three positive values
	 */
	public V getSize();

	/** Replies if this combinable bounds is initied.
	 * 
	 * @return <code>true</code> if the bounds is init, otherwise <code>false</code>
	 */
	public boolean isInit();

	/** Replies the nearest point on the bounds to the given point.
	 * <p>
	 * Given a <var>reference</var> point, this function computes the point q on (or in) 
	 * this bounds, closest to <var>reference</var>.
	 * 
	 * @param reference is the point from which the nearest point may be computed.
	 * @return the coordinates of the nearest point on the bounds.
	 */
	public P nearestPoint(P reference);

	/** Replies the farest point on the bounds to the given point.
	 * <p>
	 * Given a <var>reference</var> point, this function computes the point q on (or in) 
	 * this bounds, farest to <var>reference</var>.
	 * 
	 * @param reference is the point from which the farest point may be computed.
	 * @return the coordinates of the farest point on the bounds.
	 */
	public P farestPoint(P reference);
	
	/** Replies the area value of bounds.
	 * 
	 * @return the value of bounds area.
	 */
	public float area();
	
}
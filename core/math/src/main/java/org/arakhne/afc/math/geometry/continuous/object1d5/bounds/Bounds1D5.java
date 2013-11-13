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

package org.arakhne.afc.math.geometry.continuous.object1d5.bounds;

import org.arakhne.afc.math.geometry.Bounds;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionClassifier1D5;
import org.arakhne.afc.math.geometry.continuous.object1d.Point1D;
import org.arakhne.afc.math.geometry.continuous.object1d.Segment1D;
import org.arakhne.afc.math.geometry.continuous.object1d.bounds.Bounds1D;
import org.arakhne.afc.math.geometry.continuous.object1d5.Point1D5;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.Bounds2D;
import org.arakhne.afc.math.geometry.continuous.object3d.bounds.Bounds3D;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/** This interface represents the bounds of an area
 * in a 1.5D space.
 * <p>
 * The position of the bounds is always clamped to the bounds of the segments
 * (ie, in <code>[0;segment.getLength()]</code>. It means that the lower and uppers
 * bounds of this bounds could be outside the limits of this interval.
 *
 * @param <S> is the type of the segment to reply
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Bounds1D5<S extends Segment1D> extends Bounds<Point1D5,Point1D5,Tuple2f<?>>, IntersectionClassifier1D5<Bounds1D5<S>> {

	/** Replies the segment on which this bound lies.
	 *
	 * @return the segment.
	 */
	public S getSegment();
	
	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getMinX();

	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getMaxX();

	/** Replies the center point of this bounds.
	 * 
	 * @return the coordinate of the center point.
	 */
	public float getCenterX();

	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getMinY();

	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getMaxY();

	/** Replies the center point of this bounds.
	 * 
	 * @return the coordinate of the center point.
	 * @see #getJutting()
	 */
	public float getCenterY();

	/** Replies the jutting distance from the segment line.
	 * 
	 * @return the jutting distance (could be negative).
	 * @see #getCenterY()
	 */
	public float getJutting();

	/** Replies the lateral size of the bounds.
	 * 
	 * @return the lateral size of the bounds.
	 * @see #getSizeY()
	 */
	public float getLateralSize();

	/** {@inheritDoc}
	 */
	@Override
	public Point1D5 getPosition();

	/** Replies the curviline size of the bounds.
	 * 
	 * @return the curviline size.
	 */
	public float getSizeX();

	/** Replies the lateral size of the bounds.
	 * 
	 * @return the lateral size.
	 */
	public float getSizeY();

	/** Replies the distance between the given point and the
	 * nearest point of this bounds.
	 *
	 * @param reference
	 * @return the smallest distance from the point to the bounds.
	 */
	public float distance(Point1D reference);

	/** Replies the distance between the given point and the
	 * farest point of this bounds.
	 *
	 * @param reference
	 * @return the distance from the point to the bounds.
	 */
	public float distanceMax(Point1D reference);

	/** Replies a 1D equivalent of this 1.5D bounds.
	 * 
	 * @return a 1D bounds.
	 */
	public Bounds1D<S> toBounds1D();

	/** Replies a 2D equivalent of this 1.5D bounds.
	 * <p>
	 * Use the coordinate system replied by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @return a 2D bounds.
	 */
	public Bounds2D toBounds2D();

	/** Replies a 2D equivalent of this 1.5D bounds.
	 * 
	 * @param system is the coordinate system to use for projection.
	 * @return a 2D bounds or <code>null</code> if no segment is attached
	 * to this bound.
	 */
	public Bounds2D toBounds2D(CoordinateSystem2D system);

	/** Replies a 3D equivalent of this 1.5D bounds.
	 * <p>
	 * Use the coordinate system replied by {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 * 
	 * @return a 3D bounds.
	 */
	public Bounds3D toBounds3D();

	/** Replies a 3D equivalent of this 1.5D bounds.
	 * 
	 * @param system is the coordinate system to use for projection.
	 * @return a 3D bounds or <code>null</code> if no segment is attached
	 * to this bound.
	 */
	public Bounds3D toBounds3D(CoordinateSystem3D system);
	
	/** Replies a 3D equivalent of this 1.5D bounds.
	 * 
	 * @param posZ is the position of the bounds along the third axis.
	 * @param sizeZ is the size of the bounds along the third axis.
	 * @param system is the coordinate system to use for projection.
	 * @return a 3D bounds or <code>null</code> if no segment is attached
	 * to this bound.
	 */
	public Bounds3D toBounds3D(float posZ, float sizeZ, CoordinateSystem3D system);

}
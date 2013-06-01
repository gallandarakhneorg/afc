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

package org.arakhne.afc.math.bounds.bounds1f;

import org.arakhne.afc.math.bounds.Bounds;
import org.arakhne.afc.math.bounds.bounds1f5.Bounds1D5;
import org.arakhne.afc.math.bounds.bounds2f.Bounds2D;
import org.arakhne.afc.math.bounds.bounds3f.Bounds3D;
import org.arakhne.afc.math.intersection.IntersectionClassifier1D;
import org.arakhne.afc.math.object.Point1D;
import org.arakhne.afc.math.object.Segment1D;
import org.arakhne.afc.math.system.CoordinateSystem2D;
import org.arakhne.afc.math.system.CoordinateSystem3D;


/** This interface representes the bounds of an area
 * in a 1D space.
 * <p>
 * The position of the bounds is always clamped to the bounds of the segments
 * (ie, in <code>[0;segment.getLength()]</code>. It means that the lower and uppers
 * bounds of this bounds could be outside the limits of this interval.
 *
 * @param <S> is the type of the segment to reply
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Bounds1D<S extends Segment1D> extends Bounds<Point1D,Point1D,Float>, IntersectionClassifier1D<Bounds1D<S>> {

	/** Replies the segment on which this bound lies.
	 *
	 * @return the segment.
	 */
	public S getSegment();
	
	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point. The size of the arry depends of the dimension of the space.
	 */
	public float getMinX();

	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point. The size of the arry depends of the dimension of the space.
	 */
	public float getMaxX();

	/** Replies the center point of this bounds.
	 * 
	 * @return the coordinate of the center point. The size of the arry depends of the dimension of the space.
	 */
	public float getCenterX();

	/** Replies a 1.5D equivalent of this 1D bounds.
	 * 
	 * @return a 1.5D bounds.
	 */
	public Bounds1D5<S> toBounds1D5();

	/** Replies a 1.5D equivalent of this 1D bounds.
	 * 
	 * @param juttingDistance is the signed distance from the segment to the center of the bounds
	 * @param lateralSize is the lateral size of the new bounds. 
	 * @return a 1.5D bounds.
	 */
	public Bounds1D5<S> toBounds1D5(float juttingDistance, float lateralSize);

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

	/** Replies a 2D equivalent of this 1.5D bounds.
	 * 
	 * @param lateralSize is the lateral size of the replied bounds.
	 * @param system is the coordinate system to use for projection.
	 * @return a 2D bounds or <code>null</code> if no segment is attached
	 * to this bound.
	 */
	public Bounds2D toBounds2D(float lateralSize, CoordinateSystem2D system);

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

	/** Replies a 3D equivalent of this 1.5D bounds.
	 * 
	 * @param lateralSize is the lateral size of the replied bounds.
	 * @param posZ is the position of the bounds along the third axis.
	 * @param sizeZ is the size of the bounds along the third axis.
	 * @param system is the coordinate system to use for projection.
	 * @return a 3D bounds or <code>null</code> if no segment is attached
	 * to this bound.
	 */
	public Bounds3D toBounds3D(float lateralSize, float posZ, float sizeZ, CoordinateSystem3D system);

}
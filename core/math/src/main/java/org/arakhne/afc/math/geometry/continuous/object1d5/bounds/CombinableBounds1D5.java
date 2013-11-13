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

import org.arakhne.afc.math.geometry.CombinableBounds;
import org.arakhne.afc.math.geometry.continuous.object1d.Segment1D;
import org.arakhne.afc.math.geometry.continuous.object1d5.Point1D5;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;


/** This interface representes the bounds of an area
 * in a 1.5D space with could be combine to produce
 * larger or smaller bounds.
 *
 * @param <S> is the type of the segment to reply
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface CombinableBounds1D5<S extends Segment1D> extends Bounds1D5<S>, CombinableBounds<Bounds1D5<S>,Point1D5,Point1D5,Tuple2f<?>> {

	/** Clamp the center of the bounds to the current segment.
	 */
	public void clamp();

	/**
	 * Add the not-jutted point into the bounds.
	 * <p>
	 * <strong>Important:</strong>&nbsp;The function {@link #clamp()} is 
	 * automatically invoked. 
	 * 
	 * @param x is the point to combine for updating this bounding object.
	 */
	public void combine(float... x);

	/**
	 * Add the jutted point into the bounds.
	 * <p>
	 * <strong>Important:</strong>&nbsp;The function {@link #clamp()} is 
	 * automatically invoked. 
	 * 
	 * @param x is the point to combine for updating this bounding object.
	 */
	public void combine(Tuple2f<?>... x);

	/** Set this not-jutted bounds from a set of points.
	 * <p>
	 * <strong>Important:</strong>&nbsp;The function {@link #clamp()} is 
	 * automatically invoked. 
	 * 
	 * @param points are the points used to update this bounding object.
	 */
	public void set(float... points);

	/** Set this jutted bounds from a set of points.
	 * <p>
	 * <strong>Important:</strong>&nbsp;The function {@link #clamp()} is 
	 * automatically invoked. 
	 * 
	 * @param points are the points used to update this bounding object.
	 */
	public void set(Tuple2f<?>... points);

	/** Set the jutting.
	 * 
	 * @param juttingDistance
	 */
	public void setJutting(float juttingDistance);

	/** Set the lateral size.
	 * 
	 * @param size
	 */
	public void setLateralSize(float size);

	/** Set this bound from the set of points.
	 * <p>
	 * <strong>Important:</strong>&nbsp;The function {@link #clamp()} is 
	 * not automatically invoked. 
	 *
	 * @param lower is the coordinate of the lowest point of the bound.
	 * @param upper is the coordinate of the uppest point of the bound.
	 * @param juttingDistance is the shifting distance of the center point.
	 * @param lateralSize is the lateral size of the box.
	 */
	public void setBox(float lower, float upper, float juttingDistance, float lateralSize);

	/** Set this bound from the set of points.
	 * Do not change the segment.
	 * <p>
	 * <strong>Important:</strong>&nbsp;The function {@link #clamp()} is 
	 * not automatically invoked. 
	 *
	 * @param bounds is the object to copy.
	 */
	public void setBox(Bounds1D5<?> bounds);

	/** Set the segment on which this bound lies.
	 * <p>
	 * <strong>Important:</strong>&nbsp;The function {@link #clamp()} is 
	 * automatically invoked. 
	 *
	 * @param segment is the new segment on which this bound lies.
	 */
	public void setSegment(S segment);

}
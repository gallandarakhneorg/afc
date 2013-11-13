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

package org.arakhne.afc.math.geometry.continuous.object2d.bounds;

import java.util.Collection;

import org.arakhne.afc.math.geometry.CombinableBounds;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;

/** This interface representes the bounds of an area
 * on a 2D plane with could be combine to produce
 * larger or smaller bounds.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface CombinableBounds2D extends Bounds2D, CombinableBounds<Bounds2D,EuclidianPoint2D,Point2f,Vector2f> {
	
	/**
	 * Add the point into the bounds.
	 * 
	 * @param pointList are the points used to update this bounding object.
	 */
	public void combine(Tuple2f<?>... pointList);
	
	/** Set this bounds from a set of points.
	 *
	 * @param points are the points used to update this bounding object.
	 */
	public void set(Tuple2f<?>... points);

	/** Set this bounds from a set of points.
	 *
	 * @param points are the points used to update this bounding object.
	 */
	public void set(Collection<? extends Tuple2f<?>> points);

}
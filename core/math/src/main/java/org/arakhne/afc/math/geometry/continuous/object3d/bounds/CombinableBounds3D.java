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

package org.arakhne.afc.math.geometry.continuous.object3d.bounds;

import java.util.Collection;

import org.arakhne.afc.math.geometry.CombinableBounds;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;

/** This interface representes the bounds of an area
 * in a 3D space with could be combine to produce
 * larger or smaller bounds.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface CombinableBounds3D extends Bounds3D, CombinableBounds<Bounds3D,EuclidianPoint3D,Point3f,Vector3f> {
	
	/**
	 * Add the point into the bounds.
	 * 
	 * @param pointList are the points used to update this bounding object.
	 */
	public void combine(Tuple3f<?>... pointList);
	
	/**
	 * Add the point into the bounds.
	 * 
	 * @param pointList are the points used to update this bounding object.
	 */
	public void combinePoints(Collection<? extends Tuple3f> pointList);

	/** Set this bounds from a set of points.
	 *
	 * @param points are the points used to update this bounding object.
	 */
	public void set(Tuple3f<?>... points);

	/** Set this bounds from a set of points.
	 *
	 * @param points are the points used to update this bounding object.
	 */
	public void set(Collection<? extends Tuple3f> points);

}
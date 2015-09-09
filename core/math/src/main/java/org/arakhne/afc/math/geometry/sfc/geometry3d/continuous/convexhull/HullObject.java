/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.convexhull;

import org.arakhne.afc.math.geometry.d3.continuous.Point3f;

/** This class represents a 3D object used inside
 * Convex Hull Computation Algorithms.
 * 
 * @see ConvexHull
 * @param <T> is the type of the points.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface HullObject<T extends Point3f> {

	/** Replies the points that composed the Hull Object.
	 * 
	 * @param points is the list of real coordinates of the points.
	 * @return the list of the points of this object. 
	 */
	public Point3f[] getObjectPoints(T[] points);
	
	/** Replies the point indexes that composed the Hull Object.
	 * 
	 * @return the point indexes that composed the Hull Object.
	 */
	public int[] getObjectIndexes();

	/** Replies the creation level of thos object.
	 * 
	 * @return  the creation level of thos object.
	 */
	public int getCreationLevel();
	
	/** Replies if all the indexes of this object are
	 * inside the specified range.
	 * 
	 * @param min is the minimal value to test.
	 * @param max is the maximal value to test.
	 * @return the count of indexes inside the specified range.
	 */
	public int indexesInRange(int min, int max);

}
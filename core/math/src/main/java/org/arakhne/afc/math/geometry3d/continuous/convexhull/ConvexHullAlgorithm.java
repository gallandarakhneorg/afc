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
package org.arakhne.afc.math.geometry3d.continuous.convexhull;

import java.util.Collection;

import org.arakhne.afc.math.geometry3d.continuous.Point3f;

/** This interface describes a convex hull computation algorithm.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ConvexHullAlgorithm {

	/**
	 * Select the points that corresponds to the convex envelop
	 * of the set of points.
	 * 
	 * @param pointList is the set of points from which the
	 * convex hull must be generated.
	 * @return the convex hull
	 */
	public Point3f[] computeConvexHull(Point3f... pointList);

	/**
	 * Select the triangles that corresponds to the convex envelop
	 * of the set of points.
	 * 
	 * @param pointList is the set of points from which the
	 * convex hull must be generated.
	 * @return the convex hull
	 */
	public Collection<HullObject<Point3f>> computeConvexHullTriangles(Point3f... pointList);
	
}
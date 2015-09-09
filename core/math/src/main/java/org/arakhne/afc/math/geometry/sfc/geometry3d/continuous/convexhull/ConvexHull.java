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

import java.util.Collection;

import org.arakhne.afc.math.geometry.d3.continuous.Point3f;






/** This class permits to create convex hull from a
 * set of points.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ConvexHull {
	
	/**
	 * Select the points that corresponds to the convex envelop
	 * of the set of points.
	 * <p>
	 * The divide-and-conquer algorithm is an algorithm for
	 * computing the convex hull of a set of points in two
	 * or more dimensions.
	 * <ol>
	 * 
	 * <li>Divide the points into two equal sized sets <code>L</code>
	 * and <code>R</code> such that all points of <code>L</code> are
	 * to the left of the most leftmost points in <code>R</code>.<br>
	 * Recursively find the convex hull of <code>L</code> (shown in
	 * light blue) and <code>R</code> (shown in purple).</li>
	 * 
	 * <li>To merge in 3D need to construct a cylinder of triangles
	 * connecting the left hull and the right hull.</li>
	 * 
	 * <li>One edge of the cylinder <code>AB</code> is just the lower
	 * common tangent. The upper common tangent can be found in
	 * linear time by scanning around the left hull in a clockwise
	 * direction and around the right hull in an anti-clockwise
	 * direction. The two tangents divide each hull into two pieces.
	 * The edges belonging to one of thse pieces must be deleted.</li>
	 * 
	 * <li>We next need to find a triangle ABC belonging to the
	 * cylinder which has AB as one of its edges. The third vertex
	 * of the triangle (C) must belong to either the left hull or the
	 * right hull. (In this case it belongs to the right hull.)
	 * Consequently, either AC is an edge of the left hull or BC is
	 * an edge of the right hull. (In this case BC is an edge of the
	 * right hull.) Hence, when considering possible candidates for the
	 * third vertex it is only necessary to consider candidates that
	 * are adjacent to A in the left hull or adjacent to B in the
	 * right hull.<br>
	 * If we use a data structure for the hulls that allow us to find
	 * an adjacent vertex in constant time then the search for vertex
	 * C takes time proportional to the number of candidate vertices
	 * checked.</li>
	 * 
	 * <li>After finding triangle ABC we now have a new edge AC that
	 * joins the left hull and the right hull. We can find triangle
	 * ACD just as we did ABC. Continuing in this way, we can find all
	 * the triangles belonging to the cylinder, ending when we get back
	 * to AB.<br>
	 * The total time to find the cylinder is O(n) so the algorithm
	 * takes O(n log n) time.</li>
	 * 
	 * </ol>
	 * 
	 * @param pointList is the set of points from which the
	 * convex hull must be generated.
	 * @return the convex hull
	 */
	public static Point3f[] computeConvexHullWithDivideAndConquerAlgorithm(Point3f... pointList) {
		ConvexHullAlgorithm algo = new DivideAndConquerAlgorithm();
		return algo.computeConvexHull(pointList);
	}
	
	/**
	 * Select the points that corresponds to the convex envelop
	 * of the set of points.
	 * <p>
	 * The divide-and-conquer algorithm is an algorithm for
	 * computing the convex hull of a set of points in two
	 * or more dimensions.
	 * <ol>
	 * 
	 * <li>Divide the points into two equal sized sets <code>L</code>
	 * and <code>R</code> such that all points of <code>L</code> are
	 * to the left of the most leftmost points in <code>R</code>.<br>
	 * Recursively find the convex hull of <code>L</code> (shown in
	 * light blue) and <code>R</code> (shown in purple).</li>
	 * 
	 * <li>To merge in 3D need to construct a cylinder of triangles
	 * connecting the left hull and the right hull.</li>
	 * 
	 * <li>One edge of the cylinder <code>AB</code> is just the lower
	 * common tangent. The upper common tangent can be found in
	 * linear time by scanning around the left hull in a clockwise
	 * direction and around the right hull in an anti-clockwise
	 * direction. The two tangents divide each hull into two pieces.
	 * The edges belonging to one of thse pieces must be deleted.</li>
	 * 
	 * <li>We next need to find a triangle ABC belonging to the
	 * cylinder which has AB as one of its edges. The third vertex
	 * of the triangle (C) must belong to either the left hull or the
	 * right hull. (In this case it belongs to the right hull.)
	 * Consequently, either AC is an edge of the left hull or BC is
	 * an edge of the right hull. (In this case BC is an edge of the
	 * right hull.) Hence, when considering possible candidates for the
	 * third vertex it is only necessary to consider candidates that
	 * are adjacent to A in the left hull or adjacent to B in the
	 * right hull.<br>
	 * If we use a data structure for the hulls that allow us to find
	 * an adjacent vertex in constant time then the search for vertex
	 * C takes time proportional to the number of candidate vertices
	 * checked.</li>
	 * 
	 * <li>After finding triangle ABC we now have a new edge AC that
	 * joins the left hull and the right hull. We can find triangle
	 * ACD just as we did ABC. Continuing in this way, we can find all
	 * the triangles belonging to the cylinder, ending when we get back
	 * to AB.<br>
	 * The total time to find the cylinder is O(n) so the algorithm
	 * takes O(n log n) time.</li>
	 * 
	 * </ol>
	 * 
	 * @param pointList is the set of points from which the
	 * convex hull must be generated.
	 * @return the convex hull
	 */
	public static Collection<HullObject<Point3f>> computeConvexHullTrianglesWithDivideAndConquerAlgorithm(Point3f... pointList) {
		ConvexHullAlgorithm algo = new DivideAndConquerAlgorithm();
		return algo.computeConvexHullTriangles(pointList);
	}
		
}
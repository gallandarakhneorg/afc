/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.tree.structures.octree;

import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.tree.TreeNode;
import fr.utbm.set.tree.node.IcosepOctTreeNode.IcosepOctTreeZone;
import fr.utbm.set.tree.node.OctTreeNode.OctTreeZone;

/**
 * Utility functions for oct-trees.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OctTreeUtil {

	/** Replies the index of the child node which
	 * corresponds to the specified box.
	 * <p>
	 * This function replies the ordinal values described
	 * in {@link OctTreeZone}.
	 * <p>
	 * The returned values are:
	 * <table>
	 * <tr><td><code>0..(getChildCount()-1)</code></td><td>the
	 * index of the child node which completely encloses the
	 * specified bounds.</td></tr>
	 * <tr><td><code>i&lt;0</code></td><td>a negative value that
	 * corresponds to the first plan intersecting the given
	 * bounds. The plan could be taken with the call
	 * <code>getCutLine()[-i-1]</code>.</td></tr>
	 * </table>
	 * 
	 * @param cutX is the coordinate of the partition planes' intersections
	 * @param cutY is the coordinate of the partition planes' intersections
	 * @param cutZ is the coordinate of the partition planes' intersections
	 * @param lower is the lower point of the box.
	 * @param upper is the lower point of the box.
	 * @return a value between <code>0</code> and
	 * the value returned by {@link TreeNode#getChildCount()},
	 * or a negative value that corresponds to the first
	 * plan intersecting the bounding box.
	 * @see OctTreeZone
	 */
	public static int classifiesBox(double cutX, double cutY, double cutZ, EuclidianPoint lower, EuclidianPoint upper) {
		if (upper.compareCoordinateTo(0, cutX)<=0) {
			// On the left side of the vertical plane, parallel to the vision
			if (upper.compareCoordinateTo(1, cutY)<=0) {
				// On the front side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return OctTreeZone.SOUTH_WEST_FRONT.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return OctTreeZone.NORTH_WEST_FRONT.ordinal();
				}
				// Intersect the horizontal plane
				return -3;
			}

			if (lower.compareCoordinateTo(1, cutY)>=0) {
				// On the back side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return OctTreeZone.SOUTH_WEST_BACK.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return OctTreeZone.NORTH_WEST_BACK.ordinal();
				}
				// Intersect the horizontal plane
				return -3;
			}
			
			// Intersect the vertical plane, perpendicular to the vision
			return -2;
		}

		if (lower.compareCoordinateTo(0, cutX)>=0) {
			// On the right side of the vertical plane, parallel to the vision
			if (upper.compareCoordinateTo(1, cutY)<=0) {
				// On the front side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return OctTreeZone.SOUTH_EAST_FRONT.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return OctTreeZone.NORTH_EAST_FRONT.ordinal();
				}
				// Intersect the horizontal plane
				return -3;
			}

			if (lower.compareCoordinateTo(1, cutY)>=0) {
				// On the back side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return OctTreeZone.SOUTH_EAST_BACK.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return OctTreeZone.NORTH_EAST_BACK.ordinal();
				}
				// Intersect the horizontal plane
				return -3;
			}
			
			// Intersect the vertical plane, perpendicular to the vision
			return -2;
		}
		
		// Intersect the vertical plane, parallel to the vision
		return -1;
	}

	/** Replies the index of the child node which
	 * corresponds to the specified box.
	 * <p>
	 * This function replies the ordinal values described
	 * in {@link IcosepOctTreeZone}.
	 * <p>
	 * The returned values are:
	 * <table>
	 * <tr><td><code>0..(getChildCount()-1)</code></td><td>the
	 * index of the child node which completely encloses the
	 * specified bounds.</td></tr>
	 * </table>
	 * 
	 * @param cutX is the coordinate of the partition planes' intersections
	 * @param cutY is the coordinate of the partition planes' intersections
	 * @param cutZ is the coordinate of the partition planes' intersections
	 * @param lower is the lower point of the box.
	 * @param upper is the lower point of the box.
	 * @return a value between <code>0</code> and
	 * the value returned by {@link TreeNode#getChildCount()},
	 * never negative.
	 * @see IcosepOctTreeZone
	 */
	public static int classifiesBoxWithIcosep(double cutX, double cutY, double cutZ, EuclidianPoint lower, EuclidianPoint upper) {
		if (upper.compareCoordinateTo(0, cutX)<=0) {
			// On the left side of the vertical plane, parallel to the vision
			if (upper.compareCoordinateTo(1, cutY)<=0) {
				// On the front side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return IcosepOctTreeZone.SOUTH_WEST_FRONT.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return IcosepOctTreeZone.NORTH_WEST_FRONT.ordinal();
				}
				// Intersect the horizontal plane
				return IcosepOctTreeZone.ICOSEP.ordinal();
			}

			if (lower.compareCoordinateTo(1, cutY)>=0) {
				// On the back side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return IcosepOctTreeZone.SOUTH_WEST_BACK.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return IcosepOctTreeZone.NORTH_WEST_BACK.ordinal();
				}
				// Intersect the horizontal plane
				return IcosepOctTreeZone.ICOSEP.ordinal();
			}
			// Intersect the horizontal plane
			return IcosepOctTreeZone.ICOSEP.ordinal();
		}

		if (lower.compareCoordinateTo(0, cutX)>=0) {
			// On the right side of the vertical plane, parallel to the vision
			if (upper.compareCoordinateTo(1, cutY)<=0) {
				// On the front side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return IcosepOctTreeZone.SOUTH_EAST_FRONT.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return IcosepOctTreeZone.NORTH_EAST_FRONT.ordinal();
				}
				// Intersect the horizontal plane
				return IcosepOctTreeZone.ICOSEP.ordinal();
			}

			if (lower.compareCoordinateTo(1, cutY)>=0) {
				// On the back side of the vertical plane, perpendicular to the vision
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the horizontal plane
					return IcosepOctTreeZone.SOUTH_EAST_BACK.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the horizontal plane
					return IcosepOctTreeZone.NORTH_EAST_BACK.ordinal();
				}
				// Intersect the horizontal plane
				return IcosepOctTreeZone.ICOSEP.ordinal();
			}
		}
		
		// Intersect one of the planes
		return IcosepOctTreeZone.ICOSEP.ordinal();
	}

}
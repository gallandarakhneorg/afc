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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.shagam;

import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.tree.TreeNode;

/**
 * Utility functions for Shagam's trees.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ShagamTreeUtil {

	/** Replies the index of the child node which
	 * corresponds to the specified box.
	 * <p>
	 * This function replies the ordinal values described
	 * in {@link ShagamTreeZone}.
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
	 * @see ShagamTreeZone
	 */
	public static int classifiesBox(double cutX, double cutY, double cutZ, EuclidianPoint lower, EuclidianPoint upper) {
		if (upper.compareCoordinateTo(0, cutX)<=0) {
			// On the left side of the YZ plane
			if (upper.compareCoordinateTo(1, cutY)<=0) {
				// On the front side of the XZ plane
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the XY plane
					return ShagamTreeZone.SOUTH_WEST_FRONT_VOXEL.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the XY plane
					return ShagamTreeZone.NORTH_WEST_FRONT_VOXEL.ordinal();
				}
				
				return ShagamTreeZone.ICOSEP_XY_WEST_FRONT_PLANE.ordinal();
			}

			if (lower.compareCoordinateTo(1, cutY)>=0) {
				// On the back side of the XZ plane
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the XY plane
					return ShagamTreeZone.SOUTH_WEST_BACK_VOXEL.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the XY plane
					return ShagamTreeZone.NORTH_WEST_BACK_VOXEL.ordinal();
				}
				
				return ShagamTreeZone.ICOSEP_XY_WEST_BACK_PLANE.ordinal();
			}

			// Intersecting the XZ plane
			if (upper.compareCoordinateTo(2, cutZ)<=0) {
				// On the bottom side of the XY plane
				return ShagamTreeZone.ICOSEP_XZ_SOUTH_WEST_PLANE.ordinal();
			}
			if (lower.compareCoordinateTo(2, cutZ)>=0) {
				// On the top side of the XY plane
				return ShagamTreeZone.ICOSEP_XZ_NORTH_WEST_PLANE.ordinal();
			}
			
			return ShagamTreeZone.ICOSEP_X_WEST_AXIS.ordinal();
		}

		if (lower.compareCoordinateTo(0, cutX)>=0) {
			// On the right side of the YZ plane
			if (upper.compareCoordinateTo(1, cutY)<=0) {
				// On the front side of the XZ plane
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the XY plane
					return ShagamTreeZone.SOUTH_EAST_FRONT_VOXEL.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the XY plane
					return ShagamTreeZone.NORTH_EAST_FRONT_VOXEL.ordinal();
				}
				
				return ShagamTreeZone.ICOSEP_XY_EAST_FRONT_PLANE.ordinal();
			}

			if (lower.compareCoordinateTo(1, cutY)>=0) {
				// On the back side of the XZ plane
				if (upper.compareCoordinateTo(2, cutZ)<=0) {
					// On the bottom side of the XY plane
					return ShagamTreeZone.SOUTH_EAST_BACK_VOXEL.ordinal();
				}
				if (lower.compareCoordinateTo(2, cutZ)>=0) {
					// On the top side of the XY plane
					return ShagamTreeZone.NORTH_EAST_BACK_VOXEL.ordinal();
				}

				return ShagamTreeZone.ICOSEP_XY_EAST_BACK_PLANE.ordinal();
			}

			// Intersecting XZ plane
			if (upper.compareCoordinateTo(2, cutZ)<=0) {
				// On the bottom side of the XY plane
				return ShagamTreeZone.ICOSEP_XZ_SOUTH_EAST_PLANE.ordinal();
			}
			if (lower.compareCoordinateTo(2, cutZ)>=0) {
				// On the top side of the XY plane
				return ShagamTreeZone.ICOSEP_XZ_NORTH_EAST_PLANE.ordinal();
			}
			
			return ShagamTreeZone.ICOSEP_X_EAST_AXIS.ordinal();
		}
		
		// Intersecting the YZ plane
		
		if (upper.compareCoordinateTo(1, cutY)<=0) {
			// On the front side of the XZ plane
			if (upper.compareCoordinateTo(2, cutZ)<=0) {
				// On the bottom side of the XY plane
				return ShagamTreeZone.ICOSEP_YZ_SOUTH_FRONT_PLANE.ordinal();
			}
			if (lower.compareCoordinateTo(2, cutZ)>=0) {
				// On the top side of the XY plane
				return ShagamTreeZone.ICOSEP_YZ_NORTH_FRONT_PLANE.ordinal();
			}
			
			return ShagamTreeZone.ICOSEP_Y_FRONT_AXIS.ordinal();
		}

		if (lower.compareCoordinateTo(1, cutY)>=0) {
			// On the back side of the XZ plane
			if (upper.compareCoordinateTo(2, cutZ)<=0) {
				// On the bottom side of the XY plane
				return ShagamTreeZone.ICOSEP_YZ_SOUTH_BACK_PLANE.ordinal();
			}
			if (lower.compareCoordinateTo(2, cutZ)>=0) {
				// On the top side of the XY plane
				return ShagamTreeZone.ICOSEP_YZ_NORTH_FRONT_PLANE.ordinal();
			}

			return ShagamTreeZone.ICOSEP_Y_BACK_AXIS.ordinal();
		}

		// Intersecting XZ plane
		if (upper.compareCoordinateTo(2, cutZ)<=0) {
			// On the bottom side of the XY plane
			return ShagamTreeZone.ICOSEP_Z_SOUTH_AXIS.ordinal();
		}
		if (lower.compareCoordinateTo(2, cutZ)>=0) {
			// On the top side of the XY plane
			return ShagamTreeZone.ICOSEP_Z_NORTH_AXIS.ordinal();
		}
		
		return ShagamTreeZone.ICOSEP_CENTER.ordinal();		
	}

}